package ch.united.fastadmin.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.elasticsearch.index.query.QueryBuilders.queryStringQuery;
import static org.hamcrest.Matchers.hasItem;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import ch.united.fastadmin.IntegrationTest;
import ch.united.fastadmin.domain.Permission;
import ch.united.fastadmin.domain.enumeration.DomainResource;
import ch.united.fastadmin.repository.PermissionRepository;
import ch.united.fastadmin.repository.search.PermissionSearchRepository;
import ch.united.fastadmin.service.dto.PermissionDTO;
import ch.united.fastadmin.service.mapper.PermissionMapper;
import java.util.Collections;
import java.util.List;
import java.util.Random;
import java.util.concurrent.atomic.AtomicLong;
import javax.persistence.EntityManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

/**
 * Integration tests for the {@link PermissionResource} REST controller.
 */
@IntegrationTest
@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc
@WithMockUser
class PermissionResourceIT {

    private static final Boolean DEFAULT_NEW_ALL = false;
    private static final Boolean UPDATED_NEW_ALL = true;

    private static final Boolean DEFAULT_EDIT_OWN = false;
    private static final Boolean UPDATED_EDIT_OWN = true;

    private static final Boolean DEFAULT_EDIT_ALL = false;
    private static final Boolean UPDATED_EDIT_ALL = true;

    private static final Boolean DEFAULT_VIEW_OWN = false;
    private static final Boolean UPDATED_VIEW_OWN = true;

    private static final Boolean DEFAULT_VIEW_ALL = false;
    private static final Boolean UPDATED_VIEW_ALL = true;

    private static final Boolean DEFAULT_MANAGE_OWN = false;
    private static final Boolean UPDATED_MANAGE_OWN = true;

    private static final Boolean DEFAULT_MANAGE_ALL = false;
    private static final Boolean UPDATED_MANAGE_ALL = true;

    private static final DomainResource DEFAULT_DOMAIN_RESOURCE = DomainResource.AFFILIATE;
    private static final DomainResource UPDATED_DOMAIN_RESOURCE = DomainResource.CATALOG;

    private static final String ENTITY_API_URL = "/api/permissions";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";
    private static final String ENTITY_SEARCH_API_URL = "/api/_search/permissions";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private PermissionRepository permissionRepository;

    @Autowired
    private PermissionMapper permissionMapper;

    /**
     * This repository is mocked in the ch.united.fastadmin.repository.search test package.
     *
     * @see ch.united.fastadmin.repository.search.PermissionSearchRepositoryMockConfiguration
     */
    @Autowired
    private PermissionSearchRepository mockPermissionSearchRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restPermissionMockMvc;

    private Permission permission;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Permission createEntity(EntityManager em) {
        Permission permission = new Permission()
            .newAll(DEFAULT_NEW_ALL)
            .editOwn(DEFAULT_EDIT_OWN)
            .editAll(DEFAULT_EDIT_ALL)
            .viewOwn(DEFAULT_VIEW_OWN)
            .viewAll(DEFAULT_VIEW_ALL)
            .manageOwn(DEFAULT_MANAGE_OWN)
            .manageAll(DEFAULT_MANAGE_ALL)
            .domainResource(DEFAULT_DOMAIN_RESOURCE);
        return permission;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Permission createUpdatedEntity(EntityManager em) {
        Permission permission = new Permission()
            .newAll(UPDATED_NEW_ALL)
            .editOwn(UPDATED_EDIT_OWN)
            .editAll(UPDATED_EDIT_ALL)
            .viewOwn(UPDATED_VIEW_OWN)
            .viewAll(UPDATED_VIEW_ALL)
            .manageOwn(UPDATED_MANAGE_OWN)
            .manageAll(UPDATED_MANAGE_ALL)
            .domainResource(UPDATED_DOMAIN_RESOURCE);
        return permission;
    }

    @BeforeEach
    public void initTest() {
        permission = createEntity(em);
    }

    @Test
    @Transactional
    void createPermission() throws Exception {
        int databaseSizeBeforeCreate = permissionRepository.findAll().size();
        // Create the Permission
        PermissionDTO permissionDTO = permissionMapper.toDto(permission);
        restPermissionMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(permissionDTO)))
            .andExpect(status().isCreated());

        // Validate the Permission in the database
        List<Permission> permissionList = permissionRepository.findAll();
        assertThat(permissionList).hasSize(databaseSizeBeforeCreate + 1);
        Permission testPermission = permissionList.get(permissionList.size() - 1);
        assertThat(testPermission.getNewAll()).isEqualTo(DEFAULT_NEW_ALL);
        assertThat(testPermission.getEditOwn()).isEqualTo(DEFAULT_EDIT_OWN);
        assertThat(testPermission.getEditAll()).isEqualTo(DEFAULT_EDIT_ALL);
        assertThat(testPermission.getViewOwn()).isEqualTo(DEFAULT_VIEW_OWN);
        assertThat(testPermission.getViewAll()).isEqualTo(DEFAULT_VIEW_ALL);
        assertThat(testPermission.getManageOwn()).isEqualTo(DEFAULT_MANAGE_OWN);
        assertThat(testPermission.getManageAll()).isEqualTo(DEFAULT_MANAGE_ALL);
        assertThat(testPermission.getDomainResource()).isEqualTo(DEFAULT_DOMAIN_RESOURCE);

        // Validate the Permission in Elasticsearch
        verify(mockPermissionSearchRepository, times(1)).save(testPermission);
    }

    @Test
    @Transactional
    void createPermissionWithExistingId() throws Exception {
        // Create the Permission with an existing ID
        permission.setId(1L);
        PermissionDTO permissionDTO = permissionMapper.toDto(permission);

        int databaseSizeBeforeCreate = permissionRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restPermissionMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(permissionDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Permission in the database
        List<Permission> permissionList = permissionRepository.findAll();
        assertThat(permissionList).hasSize(databaseSizeBeforeCreate);

        // Validate the Permission in Elasticsearch
        verify(mockPermissionSearchRepository, times(0)).save(permission);
    }

    @Test
    @Transactional
    void checkNewAllIsRequired() throws Exception {
        int databaseSizeBeforeTest = permissionRepository.findAll().size();
        // set the field null
        permission.setNewAll(null);

        // Create the Permission, which fails.
        PermissionDTO permissionDTO = permissionMapper.toDto(permission);

        restPermissionMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(permissionDTO)))
            .andExpect(status().isBadRequest());

        List<Permission> permissionList = permissionRepository.findAll();
        assertThat(permissionList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkEditOwnIsRequired() throws Exception {
        int databaseSizeBeforeTest = permissionRepository.findAll().size();
        // set the field null
        permission.setEditOwn(null);

        // Create the Permission, which fails.
        PermissionDTO permissionDTO = permissionMapper.toDto(permission);

        restPermissionMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(permissionDTO)))
            .andExpect(status().isBadRequest());

        List<Permission> permissionList = permissionRepository.findAll();
        assertThat(permissionList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkEditAllIsRequired() throws Exception {
        int databaseSizeBeforeTest = permissionRepository.findAll().size();
        // set the field null
        permission.setEditAll(null);

        // Create the Permission, which fails.
        PermissionDTO permissionDTO = permissionMapper.toDto(permission);

        restPermissionMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(permissionDTO)))
            .andExpect(status().isBadRequest());

        List<Permission> permissionList = permissionRepository.findAll();
        assertThat(permissionList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkViewOwnIsRequired() throws Exception {
        int databaseSizeBeforeTest = permissionRepository.findAll().size();
        // set the field null
        permission.setViewOwn(null);

        // Create the Permission, which fails.
        PermissionDTO permissionDTO = permissionMapper.toDto(permission);

        restPermissionMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(permissionDTO)))
            .andExpect(status().isBadRequest());

        List<Permission> permissionList = permissionRepository.findAll();
        assertThat(permissionList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkViewAllIsRequired() throws Exception {
        int databaseSizeBeforeTest = permissionRepository.findAll().size();
        // set the field null
        permission.setViewAll(null);

        // Create the Permission, which fails.
        PermissionDTO permissionDTO = permissionMapper.toDto(permission);

        restPermissionMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(permissionDTO)))
            .andExpect(status().isBadRequest());

        List<Permission> permissionList = permissionRepository.findAll();
        assertThat(permissionList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkManageOwnIsRequired() throws Exception {
        int databaseSizeBeforeTest = permissionRepository.findAll().size();
        // set the field null
        permission.setManageOwn(null);

        // Create the Permission, which fails.
        PermissionDTO permissionDTO = permissionMapper.toDto(permission);

        restPermissionMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(permissionDTO)))
            .andExpect(status().isBadRequest());

        List<Permission> permissionList = permissionRepository.findAll();
        assertThat(permissionList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkManageAllIsRequired() throws Exception {
        int databaseSizeBeforeTest = permissionRepository.findAll().size();
        // set the field null
        permission.setManageAll(null);

        // Create the Permission, which fails.
        PermissionDTO permissionDTO = permissionMapper.toDto(permission);

        restPermissionMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(permissionDTO)))
            .andExpect(status().isBadRequest());

        List<Permission> permissionList = permissionRepository.findAll();
        assertThat(permissionList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkDomainResourceIsRequired() throws Exception {
        int databaseSizeBeforeTest = permissionRepository.findAll().size();
        // set the field null
        permission.setDomainResource(null);

        // Create the Permission, which fails.
        PermissionDTO permissionDTO = permissionMapper.toDto(permission);

        restPermissionMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(permissionDTO)))
            .andExpect(status().isBadRequest());

        List<Permission> permissionList = permissionRepository.findAll();
        assertThat(permissionList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllPermissions() throws Exception {
        // Initialize the database
        permissionRepository.saveAndFlush(permission);

        // Get all the permissionList
        restPermissionMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(permission.getId().intValue())))
            .andExpect(jsonPath("$.[*].newAll").value(hasItem(DEFAULT_NEW_ALL.booleanValue())))
            .andExpect(jsonPath("$.[*].editOwn").value(hasItem(DEFAULT_EDIT_OWN.booleanValue())))
            .andExpect(jsonPath("$.[*].editAll").value(hasItem(DEFAULT_EDIT_ALL.booleanValue())))
            .andExpect(jsonPath("$.[*].viewOwn").value(hasItem(DEFAULT_VIEW_OWN.booleanValue())))
            .andExpect(jsonPath("$.[*].viewAll").value(hasItem(DEFAULT_VIEW_ALL.booleanValue())))
            .andExpect(jsonPath("$.[*].manageOwn").value(hasItem(DEFAULT_MANAGE_OWN.booleanValue())))
            .andExpect(jsonPath("$.[*].manageAll").value(hasItem(DEFAULT_MANAGE_ALL.booleanValue())))
            .andExpect(jsonPath("$.[*].domainResource").value(hasItem(DEFAULT_DOMAIN_RESOURCE.toString())));
    }

    @Test
    @Transactional
    void getPermission() throws Exception {
        // Initialize the database
        permissionRepository.saveAndFlush(permission);

        // Get the permission
        restPermissionMockMvc
            .perform(get(ENTITY_API_URL_ID, permission.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(permission.getId().intValue()))
            .andExpect(jsonPath("$.newAll").value(DEFAULT_NEW_ALL.booleanValue()))
            .andExpect(jsonPath("$.editOwn").value(DEFAULT_EDIT_OWN.booleanValue()))
            .andExpect(jsonPath("$.editAll").value(DEFAULT_EDIT_ALL.booleanValue()))
            .andExpect(jsonPath("$.viewOwn").value(DEFAULT_VIEW_OWN.booleanValue()))
            .andExpect(jsonPath("$.viewAll").value(DEFAULT_VIEW_ALL.booleanValue()))
            .andExpect(jsonPath("$.manageOwn").value(DEFAULT_MANAGE_OWN.booleanValue()))
            .andExpect(jsonPath("$.manageAll").value(DEFAULT_MANAGE_ALL.booleanValue()))
            .andExpect(jsonPath("$.domainResource").value(DEFAULT_DOMAIN_RESOURCE.toString()));
    }

    @Test
    @Transactional
    void getNonExistingPermission() throws Exception {
        // Get the permission
        restPermissionMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewPermission() throws Exception {
        // Initialize the database
        permissionRepository.saveAndFlush(permission);

        int databaseSizeBeforeUpdate = permissionRepository.findAll().size();

        // Update the permission
        Permission updatedPermission = permissionRepository.findById(permission.getId()).get();
        // Disconnect from session so that the updates on updatedPermission are not directly saved in db
        em.detach(updatedPermission);
        updatedPermission
            .newAll(UPDATED_NEW_ALL)
            .editOwn(UPDATED_EDIT_OWN)
            .editAll(UPDATED_EDIT_ALL)
            .viewOwn(UPDATED_VIEW_OWN)
            .viewAll(UPDATED_VIEW_ALL)
            .manageOwn(UPDATED_MANAGE_OWN)
            .manageAll(UPDATED_MANAGE_ALL)
            .domainResource(UPDATED_DOMAIN_RESOURCE);
        PermissionDTO permissionDTO = permissionMapper.toDto(updatedPermission);

        restPermissionMockMvc
            .perform(
                put(ENTITY_API_URL_ID, permissionDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(permissionDTO))
            )
            .andExpect(status().isOk());

        // Validate the Permission in the database
        List<Permission> permissionList = permissionRepository.findAll();
        assertThat(permissionList).hasSize(databaseSizeBeforeUpdate);
        Permission testPermission = permissionList.get(permissionList.size() - 1);
        assertThat(testPermission.getNewAll()).isEqualTo(UPDATED_NEW_ALL);
        assertThat(testPermission.getEditOwn()).isEqualTo(UPDATED_EDIT_OWN);
        assertThat(testPermission.getEditAll()).isEqualTo(UPDATED_EDIT_ALL);
        assertThat(testPermission.getViewOwn()).isEqualTo(UPDATED_VIEW_OWN);
        assertThat(testPermission.getViewAll()).isEqualTo(UPDATED_VIEW_ALL);
        assertThat(testPermission.getManageOwn()).isEqualTo(UPDATED_MANAGE_OWN);
        assertThat(testPermission.getManageAll()).isEqualTo(UPDATED_MANAGE_ALL);
        assertThat(testPermission.getDomainResource()).isEqualTo(UPDATED_DOMAIN_RESOURCE);

        // Validate the Permission in Elasticsearch
        verify(mockPermissionSearchRepository).save(testPermission);
    }

    @Test
    @Transactional
    void putNonExistingPermission() throws Exception {
        int databaseSizeBeforeUpdate = permissionRepository.findAll().size();
        permission.setId(count.incrementAndGet());

        // Create the Permission
        PermissionDTO permissionDTO = permissionMapper.toDto(permission);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restPermissionMockMvc
            .perform(
                put(ENTITY_API_URL_ID, permissionDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(permissionDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Permission in the database
        List<Permission> permissionList = permissionRepository.findAll();
        assertThat(permissionList).hasSize(databaseSizeBeforeUpdate);

        // Validate the Permission in Elasticsearch
        verify(mockPermissionSearchRepository, times(0)).save(permission);
    }

    @Test
    @Transactional
    void putWithIdMismatchPermission() throws Exception {
        int databaseSizeBeforeUpdate = permissionRepository.findAll().size();
        permission.setId(count.incrementAndGet());

        // Create the Permission
        PermissionDTO permissionDTO = permissionMapper.toDto(permission);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restPermissionMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(permissionDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Permission in the database
        List<Permission> permissionList = permissionRepository.findAll();
        assertThat(permissionList).hasSize(databaseSizeBeforeUpdate);

        // Validate the Permission in Elasticsearch
        verify(mockPermissionSearchRepository, times(0)).save(permission);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamPermission() throws Exception {
        int databaseSizeBeforeUpdate = permissionRepository.findAll().size();
        permission.setId(count.incrementAndGet());

        // Create the Permission
        PermissionDTO permissionDTO = permissionMapper.toDto(permission);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restPermissionMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(permissionDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Permission in the database
        List<Permission> permissionList = permissionRepository.findAll();
        assertThat(permissionList).hasSize(databaseSizeBeforeUpdate);

        // Validate the Permission in Elasticsearch
        verify(mockPermissionSearchRepository, times(0)).save(permission);
    }

    @Test
    @Transactional
    void partialUpdatePermissionWithPatch() throws Exception {
        // Initialize the database
        permissionRepository.saveAndFlush(permission);

        int databaseSizeBeforeUpdate = permissionRepository.findAll().size();

        // Update the permission using partial update
        Permission partialUpdatedPermission = new Permission();
        partialUpdatedPermission.setId(permission.getId());

        partialUpdatedPermission
            .newAll(UPDATED_NEW_ALL)
            .editOwn(UPDATED_EDIT_OWN)
            .editAll(UPDATED_EDIT_ALL)
            .viewOwn(UPDATED_VIEW_OWN)
            .viewAll(UPDATED_VIEW_ALL)
            .manageOwn(UPDATED_MANAGE_OWN)
            .domainResource(UPDATED_DOMAIN_RESOURCE);

        restPermissionMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedPermission.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedPermission))
            )
            .andExpect(status().isOk());

        // Validate the Permission in the database
        List<Permission> permissionList = permissionRepository.findAll();
        assertThat(permissionList).hasSize(databaseSizeBeforeUpdate);
        Permission testPermission = permissionList.get(permissionList.size() - 1);
        assertThat(testPermission.getNewAll()).isEqualTo(UPDATED_NEW_ALL);
        assertThat(testPermission.getEditOwn()).isEqualTo(UPDATED_EDIT_OWN);
        assertThat(testPermission.getEditAll()).isEqualTo(UPDATED_EDIT_ALL);
        assertThat(testPermission.getViewOwn()).isEqualTo(UPDATED_VIEW_OWN);
        assertThat(testPermission.getViewAll()).isEqualTo(UPDATED_VIEW_ALL);
        assertThat(testPermission.getManageOwn()).isEqualTo(UPDATED_MANAGE_OWN);
        assertThat(testPermission.getManageAll()).isEqualTo(DEFAULT_MANAGE_ALL);
        assertThat(testPermission.getDomainResource()).isEqualTo(UPDATED_DOMAIN_RESOURCE);
    }

    @Test
    @Transactional
    void fullUpdatePermissionWithPatch() throws Exception {
        // Initialize the database
        permissionRepository.saveAndFlush(permission);

        int databaseSizeBeforeUpdate = permissionRepository.findAll().size();

        // Update the permission using partial update
        Permission partialUpdatedPermission = new Permission();
        partialUpdatedPermission.setId(permission.getId());

        partialUpdatedPermission
            .newAll(UPDATED_NEW_ALL)
            .editOwn(UPDATED_EDIT_OWN)
            .editAll(UPDATED_EDIT_ALL)
            .viewOwn(UPDATED_VIEW_OWN)
            .viewAll(UPDATED_VIEW_ALL)
            .manageOwn(UPDATED_MANAGE_OWN)
            .manageAll(UPDATED_MANAGE_ALL)
            .domainResource(UPDATED_DOMAIN_RESOURCE);

        restPermissionMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedPermission.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedPermission))
            )
            .andExpect(status().isOk());

        // Validate the Permission in the database
        List<Permission> permissionList = permissionRepository.findAll();
        assertThat(permissionList).hasSize(databaseSizeBeforeUpdate);
        Permission testPermission = permissionList.get(permissionList.size() - 1);
        assertThat(testPermission.getNewAll()).isEqualTo(UPDATED_NEW_ALL);
        assertThat(testPermission.getEditOwn()).isEqualTo(UPDATED_EDIT_OWN);
        assertThat(testPermission.getEditAll()).isEqualTo(UPDATED_EDIT_ALL);
        assertThat(testPermission.getViewOwn()).isEqualTo(UPDATED_VIEW_OWN);
        assertThat(testPermission.getViewAll()).isEqualTo(UPDATED_VIEW_ALL);
        assertThat(testPermission.getManageOwn()).isEqualTo(UPDATED_MANAGE_OWN);
        assertThat(testPermission.getManageAll()).isEqualTo(UPDATED_MANAGE_ALL);
        assertThat(testPermission.getDomainResource()).isEqualTo(UPDATED_DOMAIN_RESOURCE);
    }

    @Test
    @Transactional
    void patchNonExistingPermission() throws Exception {
        int databaseSizeBeforeUpdate = permissionRepository.findAll().size();
        permission.setId(count.incrementAndGet());

        // Create the Permission
        PermissionDTO permissionDTO = permissionMapper.toDto(permission);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restPermissionMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, permissionDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(permissionDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Permission in the database
        List<Permission> permissionList = permissionRepository.findAll();
        assertThat(permissionList).hasSize(databaseSizeBeforeUpdate);

        // Validate the Permission in Elasticsearch
        verify(mockPermissionSearchRepository, times(0)).save(permission);
    }

    @Test
    @Transactional
    void patchWithIdMismatchPermission() throws Exception {
        int databaseSizeBeforeUpdate = permissionRepository.findAll().size();
        permission.setId(count.incrementAndGet());

        // Create the Permission
        PermissionDTO permissionDTO = permissionMapper.toDto(permission);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restPermissionMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(permissionDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Permission in the database
        List<Permission> permissionList = permissionRepository.findAll();
        assertThat(permissionList).hasSize(databaseSizeBeforeUpdate);

        // Validate the Permission in Elasticsearch
        verify(mockPermissionSearchRepository, times(0)).save(permission);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamPermission() throws Exception {
        int databaseSizeBeforeUpdate = permissionRepository.findAll().size();
        permission.setId(count.incrementAndGet());

        // Create the Permission
        PermissionDTO permissionDTO = permissionMapper.toDto(permission);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restPermissionMockMvc
            .perform(
                patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(permissionDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the Permission in the database
        List<Permission> permissionList = permissionRepository.findAll();
        assertThat(permissionList).hasSize(databaseSizeBeforeUpdate);

        // Validate the Permission in Elasticsearch
        verify(mockPermissionSearchRepository, times(0)).save(permission);
    }

    @Test
    @Transactional
    void deletePermission() throws Exception {
        // Initialize the database
        permissionRepository.saveAndFlush(permission);

        int databaseSizeBeforeDelete = permissionRepository.findAll().size();

        // Delete the permission
        restPermissionMockMvc
            .perform(delete(ENTITY_API_URL_ID, permission.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Permission> permissionList = permissionRepository.findAll();
        assertThat(permissionList).hasSize(databaseSizeBeforeDelete - 1);

        // Validate the Permission in Elasticsearch
        verify(mockPermissionSearchRepository, times(1)).deleteById(permission.getId());
    }

    @Test
    @Transactional
    void searchPermission() throws Exception {
        // Configure the mock search repository
        // Initialize the database
        permissionRepository.saveAndFlush(permission);
        when(mockPermissionSearchRepository.search(queryStringQuery("id:" + permission.getId())))
            .thenReturn(Collections.singletonList(permission));

        // Search the permission
        restPermissionMockMvc
            .perform(get(ENTITY_SEARCH_API_URL + "?query=id:" + permission.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(permission.getId().intValue())))
            .andExpect(jsonPath("$.[*].newAll").value(hasItem(DEFAULT_NEW_ALL.booleanValue())))
            .andExpect(jsonPath("$.[*].editOwn").value(hasItem(DEFAULT_EDIT_OWN.booleanValue())))
            .andExpect(jsonPath("$.[*].editAll").value(hasItem(DEFAULT_EDIT_ALL.booleanValue())))
            .andExpect(jsonPath("$.[*].viewOwn").value(hasItem(DEFAULT_VIEW_OWN.booleanValue())))
            .andExpect(jsonPath("$.[*].viewAll").value(hasItem(DEFAULT_VIEW_ALL.booleanValue())))
            .andExpect(jsonPath("$.[*].manageOwn").value(hasItem(DEFAULT_MANAGE_OWN.booleanValue())))
            .andExpect(jsonPath("$.[*].manageAll").value(hasItem(DEFAULT_MANAGE_ALL.booleanValue())))
            .andExpect(jsonPath("$.[*].domainResource").value(hasItem(DEFAULT_DOMAIN_RESOURCE.toString())));
    }
}
