package ch.united.fastadmin.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.elasticsearch.index.query.QueryBuilders.queryStringQuery;
import static org.hamcrest.Matchers.hasItem;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import ch.united.fastadmin.IntegrationTest;
import ch.united.fastadmin.domain.Contact;
import ch.united.fastadmin.domain.Permission;
import ch.united.fastadmin.domain.Role;
import ch.united.fastadmin.domain.enumeration.DomainArea;
import ch.united.fastadmin.domain.enumeration.PermissionType;
import ch.united.fastadmin.domain.enumeration.PermissionType;
import ch.united.fastadmin.domain.enumeration.PermissionType;
import ch.united.fastadmin.repository.PermissionRepository;
import ch.united.fastadmin.repository.search.PermissionSearchRepository;
import ch.united.fastadmin.service.criteria.PermissionCriteria;
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

    private static final PermissionType DEFAULT_ADD = PermissionType.NONE;
    private static final PermissionType UPDATED_ADD = PermissionType.OWN;

    private static final PermissionType DEFAULT_EDIT = PermissionType.NONE;
    private static final PermissionType UPDATED_EDIT = PermissionType.OWN;

    private static final PermissionType DEFAULT_MANAGE = PermissionType.NONE;
    private static final PermissionType UPDATED_MANAGE = PermissionType.OWN;

    private static final DomainArea DEFAULT_DOMAIN_AREA = DomainArea.AFFILIATE;
    private static final DomainArea UPDATED_DOMAIN_AREA = DomainArea.CATALOG;

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
        Permission permission = new Permission().add(DEFAULT_ADD).edit(DEFAULT_EDIT).manage(DEFAULT_MANAGE).domainArea(DEFAULT_DOMAIN_AREA);
        return permission;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Permission createUpdatedEntity(EntityManager em) {
        Permission permission = new Permission().add(UPDATED_ADD).edit(UPDATED_EDIT).manage(UPDATED_MANAGE).domainArea(UPDATED_DOMAIN_AREA);
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
        assertThat(testPermission.getAdd()).isEqualTo(DEFAULT_ADD);
        assertThat(testPermission.getEdit()).isEqualTo(DEFAULT_EDIT);
        assertThat(testPermission.getManage()).isEqualTo(DEFAULT_MANAGE);
        assertThat(testPermission.getDomainArea()).isEqualTo(DEFAULT_DOMAIN_AREA);

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
    void checkAddIsRequired() throws Exception {
        int databaseSizeBeforeTest = permissionRepository.findAll().size();
        // set the field null
        permission.setAdd(null);

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
    void checkEditIsRequired() throws Exception {
        int databaseSizeBeforeTest = permissionRepository.findAll().size();
        // set the field null
        permission.setEdit(null);

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
    void checkManageIsRequired() throws Exception {
        int databaseSizeBeforeTest = permissionRepository.findAll().size();
        // set the field null
        permission.setManage(null);

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
    void checkDomainAreaIsRequired() throws Exception {
        int databaseSizeBeforeTest = permissionRepository.findAll().size();
        // set the field null
        permission.setDomainArea(null);

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
            .andExpect(jsonPath("$.[*].add").value(hasItem(DEFAULT_ADD.toString())))
            .andExpect(jsonPath("$.[*].edit").value(hasItem(DEFAULT_EDIT.toString())))
            .andExpect(jsonPath("$.[*].manage").value(hasItem(DEFAULT_MANAGE.toString())))
            .andExpect(jsonPath("$.[*].domainArea").value(hasItem(DEFAULT_DOMAIN_AREA.toString())));
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
            .andExpect(jsonPath("$.add").value(DEFAULT_ADD.toString()))
            .andExpect(jsonPath("$.edit").value(DEFAULT_EDIT.toString()))
            .andExpect(jsonPath("$.manage").value(DEFAULT_MANAGE.toString()))
            .andExpect(jsonPath("$.domainArea").value(DEFAULT_DOMAIN_AREA.toString()));
    }

    @Test
    @Transactional
    void getPermissionsByIdFiltering() throws Exception {
        // Initialize the database
        permissionRepository.saveAndFlush(permission);

        Long id = permission.getId();

        defaultPermissionShouldBeFound("id.equals=" + id);
        defaultPermissionShouldNotBeFound("id.notEquals=" + id);

        defaultPermissionShouldBeFound("id.greaterThanOrEqual=" + id);
        defaultPermissionShouldNotBeFound("id.greaterThan=" + id);

        defaultPermissionShouldBeFound("id.lessThanOrEqual=" + id);
        defaultPermissionShouldNotBeFound("id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllPermissionsByAddIsEqualToSomething() throws Exception {
        // Initialize the database
        permissionRepository.saveAndFlush(permission);

        // Get all the permissionList where add equals to DEFAULT_ADD
        defaultPermissionShouldBeFound("add.equals=" + DEFAULT_ADD);

        // Get all the permissionList where add equals to UPDATED_ADD
        defaultPermissionShouldNotBeFound("add.equals=" + UPDATED_ADD);
    }

    @Test
    @Transactional
    void getAllPermissionsByAddIsNotEqualToSomething() throws Exception {
        // Initialize the database
        permissionRepository.saveAndFlush(permission);

        // Get all the permissionList where add not equals to DEFAULT_ADD
        defaultPermissionShouldNotBeFound("add.notEquals=" + DEFAULT_ADD);

        // Get all the permissionList where add not equals to UPDATED_ADD
        defaultPermissionShouldBeFound("add.notEquals=" + UPDATED_ADD);
    }

    @Test
    @Transactional
    void getAllPermissionsByAddIsInShouldWork() throws Exception {
        // Initialize the database
        permissionRepository.saveAndFlush(permission);

        // Get all the permissionList where add in DEFAULT_ADD or UPDATED_ADD
        defaultPermissionShouldBeFound("add.in=" + DEFAULT_ADD + "," + UPDATED_ADD);

        // Get all the permissionList where add equals to UPDATED_ADD
        defaultPermissionShouldNotBeFound("add.in=" + UPDATED_ADD);
    }

    @Test
    @Transactional
    void getAllPermissionsByAddIsNullOrNotNull() throws Exception {
        // Initialize the database
        permissionRepository.saveAndFlush(permission);

        // Get all the permissionList where add is not null
        defaultPermissionShouldBeFound("add.specified=true");

        // Get all the permissionList where add is null
        defaultPermissionShouldNotBeFound("add.specified=false");
    }

    @Test
    @Transactional
    void getAllPermissionsByEditIsEqualToSomething() throws Exception {
        // Initialize the database
        permissionRepository.saveAndFlush(permission);

        // Get all the permissionList where edit equals to DEFAULT_EDIT
        defaultPermissionShouldBeFound("edit.equals=" + DEFAULT_EDIT);

        // Get all the permissionList where edit equals to UPDATED_EDIT
        defaultPermissionShouldNotBeFound("edit.equals=" + UPDATED_EDIT);
    }

    @Test
    @Transactional
    void getAllPermissionsByEditIsNotEqualToSomething() throws Exception {
        // Initialize the database
        permissionRepository.saveAndFlush(permission);

        // Get all the permissionList where edit not equals to DEFAULT_EDIT
        defaultPermissionShouldNotBeFound("edit.notEquals=" + DEFAULT_EDIT);

        // Get all the permissionList where edit not equals to UPDATED_EDIT
        defaultPermissionShouldBeFound("edit.notEquals=" + UPDATED_EDIT);
    }

    @Test
    @Transactional
    void getAllPermissionsByEditIsInShouldWork() throws Exception {
        // Initialize the database
        permissionRepository.saveAndFlush(permission);

        // Get all the permissionList where edit in DEFAULT_EDIT or UPDATED_EDIT
        defaultPermissionShouldBeFound("edit.in=" + DEFAULT_EDIT + "," + UPDATED_EDIT);

        // Get all the permissionList where edit equals to UPDATED_EDIT
        defaultPermissionShouldNotBeFound("edit.in=" + UPDATED_EDIT);
    }

    @Test
    @Transactional
    void getAllPermissionsByEditIsNullOrNotNull() throws Exception {
        // Initialize the database
        permissionRepository.saveAndFlush(permission);

        // Get all the permissionList where edit is not null
        defaultPermissionShouldBeFound("edit.specified=true");

        // Get all the permissionList where edit is null
        defaultPermissionShouldNotBeFound("edit.specified=false");
    }

    @Test
    @Transactional
    void getAllPermissionsByManageIsEqualToSomething() throws Exception {
        // Initialize the database
        permissionRepository.saveAndFlush(permission);

        // Get all the permissionList where manage equals to DEFAULT_MANAGE
        defaultPermissionShouldBeFound("manage.equals=" + DEFAULT_MANAGE);

        // Get all the permissionList where manage equals to UPDATED_MANAGE
        defaultPermissionShouldNotBeFound("manage.equals=" + UPDATED_MANAGE);
    }

    @Test
    @Transactional
    void getAllPermissionsByManageIsNotEqualToSomething() throws Exception {
        // Initialize the database
        permissionRepository.saveAndFlush(permission);

        // Get all the permissionList where manage not equals to DEFAULT_MANAGE
        defaultPermissionShouldNotBeFound("manage.notEquals=" + DEFAULT_MANAGE);

        // Get all the permissionList where manage not equals to UPDATED_MANAGE
        defaultPermissionShouldBeFound("manage.notEquals=" + UPDATED_MANAGE);
    }

    @Test
    @Transactional
    void getAllPermissionsByManageIsInShouldWork() throws Exception {
        // Initialize the database
        permissionRepository.saveAndFlush(permission);

        // Get all the permissionList where manage in DEFAULT_MANAGE or UPDATED_MANAGE
        defaultPermissionShouldBeFound("manage.in=" + DEFAULT_MANAGE + "," + UPDATED_MANAGE);

        // Get all the permissionList where manage equals to UPDATED_MANAGE
        defaultPermissionShouldNotBeFound("manage.in=" + UPDATED_MANAGE);
    }

    @Test
    @Transactional
    void getAllPermissionsByManageIsNullOrNotNull() throws Exception {
        // Initialize the database
        permissionRepository.saveAndFlush(permission);

        // Get all the permissionList where manage is not null
        defaultPermissionShouldBeFound("manage.specified=true");

        // Get all the permissionList where manage is null
        defaultPermissionShouldNotBeFound("manage.specified=false");
    }

    @Test
    @Transactional
    void getAllPermissionsByDomainAreaIsEqualToSomething() throws Exception {
        // Initialize the database
        permissionRepository.saveAndFlush(permission);

        // Get all the permissionList where domainArea equals to DEFAULT_DOMAIN_AREA
        defaultPermissionShouldBeFound("domainArea.equals=" + DEFAULT_DOMAIN_AREA);

        // Get all the permissionList where domainArea equals to UPDATED_DOMAIN_AREA
        defaultPermissionShouldNotBeFound("domainArea.equals=" + UPDATED_DOMAIN_AREA);
    }

    @Test
    @Transactional
    void getAllPermissionsByDomainAreaIsNotEqualToSomething() throws Exception {
        // Initialize the database
        permissionRepository.saveAndFlush(permission);

        // Get all the permissionList where domainArea not equals to DEFAULT_DOMAIN_AREA
        defaultPermissionShouldNotBeFound("domainArea.notEquals=" + DEFAULT_DOMAIN_AREA);

        // Get all the permissionList where domainArea not equals to UPDATED_DOMAIN_AREA
        defaultPermissionShouldBeFound("domainArea.notEquals=" + UPDATED_DOMAIN_AREA);
    }

    @Test
    @Transactional
    void getAllPermissionsByDomainAreaIsInShouldWork() throws Exception {
        // Initialize the database
        permissionRepository.saveAndFlush(permission);

        // Get all the permissionList where domainArea in DEFAULT_DOMAIN_AREA or UPDATED_DOMAIN_AREA
        defaultPermissionShouldBeFound("domainArea.in=" + DEFAULT_DOMAIN_AREA + "," + UPDATED_DOMAIN_AREA);

        // Get all the permissionList where domainArea equals to UPDATED_DOMAIN_AREA
        defaultPermissionShouldNotBeFound("domainArea.in=" + UPDATED_DOMAIN_AREA);
    }

    @Test
    @Transactional
    void getAllPermissionsByDomainAreaIsNullOrNotNull() throws Exception {
        // Initialize the database
        permissionRepository.saveAndFlush(permission);

        // Get all the permissionList where domainArea is not null
        defaultPermissionShouldBeFound("domainArea.specified=true");

        // Get all the permissionList where domainArea is null
        defaultPermissionShouldNotBeFound("domainArea.specified=false");
    }

    @Test
    @Transactional
    void getAllPermissionsByRoleIsEqualToSomething() throws Exception {
        // Initialize the database
        permissionRepository.saveAndFlush(permission);
        Role role = RoleResourceIT.createEntity(em);
        em.persist(role);
        em.flush();
        permission.setRole(role);
        permissionRepository.saveAndFlush(permission);
        Long roleId = role.getId();

        // Get all the permissionList where role equals to roleId
        defaultPermissionShouldBeFound("roleId.equals=" + roleId);

        // Get all the permissionList where role equals to (roleId + 1)
        defaultPermissionShouldNotBeFound("roleId.equals=" + (roleId + 1));
    }

    @Test
    @Transactional
    void getAllPermissionsByContactIsEqualToSomething() throws Exception {
        // Initialize the database
        permissionRepository.saveAndFlush(permission);
        Contact contact = ContactResourceIT.createEntity(em);
        em.persist(contact);
        em.flush();
        permission.setContact(contact);
        permissionRepository.saveAndFlush(permission);
        Long contactId = contact.getId();

        // Get all the permissionList where contact equals to contactId
        defaultPermissionShouldBeFound("contactId.equals=" + contactId);

        // Get all the permissionList where contact equals to (contactId + 1)
        defaultPermissionShouldNotBeFound("contactId.equals=" + (contactId + 1));
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultPermissionShouldBeFound(String filter) throws Exception {
        restPermissionMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(permission.getId().intValue())))
            .andExpect(jsonPath("$.[*].add").value(hasItem(DEFAULT_ADD.toString())))
            .andExpect(jsonPath("$.[*].edit").value(hasItem(DEFAULT_EDIT.toString())))
            .andExpect(jsonPath("$.[*].manage").value(hasItem(DEFAULT_MANAGE.toString())))
            .andExpect(jsonPath("$.[*].domainArea").value(hasItem(DEFAULT_DOMAIN_AREA.toString())));

        // Check, that the count call also returns 1
        restPermissionMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultPermissionShouldNotBeFound(String filter) throws Exception {
        restPermissionMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restPermissionMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
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
        updatedPermission.add(UPDATED_ADD).edit(UPDATED_EDIT).manage(UPDATED_MANAGE).domainArea(UPDATED_DOMAIN_AREA);
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
        assertThat(testPermission.getAdd()).isEqualTo(UPDATED_ADD);
        assertThat(testPermission.getEdit()).isEqualTo(UPDATED_EDIT);
        assertThat(testPermission.getManage()).isEqualTo(UPDATED_MANAGE);
        assertThat(testPermission.getDomainArea()).isEqualTo(UPDATED_DOMAIN_AREA);

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

        partialUpdatedPermission.add(UPDATED_ADD).edit(UPDATED_EDIT).manage(UPDATED_MANAGE).domainArea(UPDATED_DOMAIN_AREA);

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
        assertThat(testPermission.getAdd()).isEqualTo(UPDATED_ADD);
        assertThat(testPermission.getEdit()).isEqualTo(UPDATED_EDIT);
        assertThat(testPermission.getManage()).isEqualTo(UPDATED_MANAGE);
        assertThat(testPermission.getDomainArea()).isEqualTo(UPDATED_DOMAIN_AREA);
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

        partialUpdatedPermission.add(UPDATED_ADD).edit(UPDATED_EDIT).manage(UPDATED_MANAGE).domainArea(UPDATED_DOMAIN_AREA);

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
        assertThat(testPermission.getAdd()).isEqualTo(UPDATED_ADD);
        assertThat(testPermission.getEdit()).isEqualTo(UPDATED_EDIT);
        assertThat(testPermission.getManage()).isEqualTo(UPDATED_MANAGE);
        assertThat(testPermission.getDomainArea()).isEqualTo(UPDATED_DOMAIN_AREA);
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
            .andExpect(jsonPath("$.[*].add").value(hasItem(DEFAULT_ADD.toString())))
            .andExpect(jsonPath("$.[*].edit").value(hasItem(DEFAULT_EDIT.toString())))
            .andExpect(jsonPath("$.[*].manage").value(hasItem(DEFAULT_MANAGE.toString())))
            .andExpect(jsonPath("$.[*].domainArea").value(hasItem(DEFAULT_DOMAIN_AREA.toString())));
    }
}
