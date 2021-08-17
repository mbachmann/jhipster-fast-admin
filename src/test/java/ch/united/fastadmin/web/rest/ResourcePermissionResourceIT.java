package ch.united.fastadmin.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import ch.united.fastadmin.IntegrationTest;
import ch.united.fastadmin.domain.ResourcePermission;
import ch.united.fastadmin.domain.enumeration.DomainArea;
import ch.united.fastadmin.domain.enumeration.PermissionType;
import ch.united.fastadmin.domain.enumeration.PermissionType;
import ch.united.fastadmin.domain.enumeration.PermissionType;
import ch.united.fastadmin.repository.ResourcePermissionRepository;
import ch.united.fastadmin.service.dto.ResourcePermissionDTO;
import ch.united.fastadmin.service.mapper.ResourcePermissionMapper;
import java.util.List;
import java.util.Random;
import java.util.concurrent.atomic.AtomicLong;
import javax.persistence.EntityManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

/**
 * Integration tests for the {@link ResourcePermissionResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class ResourcePermissionResourceIT {

    private static final PermissionType DEFAULT_ADD = PermissionType.NONE;
    private static final PermissionType UPDATED_ADD = PermissionType.OWN;

    private static final PermissionType DEFAULT_EDIT = PermissionType.NONE;
    private static final PermissionType UPDATED_EDIT = PermissionType.OWN;

    private static final PermissionType DEFAULT_MANAGE = PermissionType.NONE;
    private static final PermissionType UPDATED_MANAGE = PermissionType.OWN;

    private static final DomainArea DEFAULT_DOMAIN_AREA = DomainArea.CONTACTS;
    private static final DomainArea UPDATED_DOMAIN_AREA = DomainArea.LETTERS;

    private static final String ENTITY_API_URL = "/api/resource-permissions";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ResourcePermissionRepository resourcePermissionRepository;

    @Autowired
    private ResourcePermissionMapper resourcePermissionMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restResourcePermissionMockMvc;

    private ResourcePermission resourcePermission;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static ResourcePermission createEntity(EntityManager em) {
        ResourcePermission resourcePermission = new ResourcePermission()
            .add(DEFAULT_ADD)
            .edit(DEFAULT_EDIT)
            .manage(DEFAULT_MANAGE)
            .domainArea(DEFAULT_DOMAIN_AREA);
        return resourcePermission;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static ResourcePermission createUpdatedEntity(EntityManager em) {
        ResourcePermission resourcePermission = new ResourcePermission()
            .add(UPDATED_ADD)
            .edit(UPDATED_EDIT)
            .manage(UPDATED_MANAGE)
            .domainArea(UPDATED_DOMAIN_AREA);
        return resourcePermission;
    }

    @BeforeEach
    public void initTest() {
        resourcePermission = createEntity(em);
    }

    @Test
    @Transactional
    void createResourcePermission() throws Exception {
        int databaseSizeBeforeCreate = resourcePermissionRepository.findAll().size();
        // Create the ResourcePermission
        ResourcePermissionDTO resourcePermissionDTO = resourcePermissionMapper.toDto(resourcePermission);
        restResourcePermissionMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(resourcePermissionDTO))
            )
            .andExpect(status().isCreated());

        // Validate the ResourcePermission in the database
        List<ResourcePermission> resourcePermissionList = resourcePermissionRepository.findAll();
        assertThat(resourcePermissionList).hasSize(databaseSizeBeforeCreate + 1);
        ResourcePermission testResourcePermission = resourcePermissionList.get(resourcePermissionList.size() - 1);
        assertThat(testResourcePermission.getAdd()).isEqualTo(DEFAULT_ADD);
        assertThat(testResourcePermission.getEdit()).isEqualTo(DEFAULT_EDIT);
        assertThat(testResourcePermission.getManage()).isEqualTo(DEFAULT_MANAGE);
        assertThat(testResourcePermission.getDomainArea()).isEqualTo(DEFAULT_DOMAIN_AREA);
    }

    @Test
    @Transactional
    void createResourcePermissionWithExistingId() throws Exception {
        // Create the ResourcePermission with an existing ID
        resourcePermission.setId(1L);
        ResourcePermissionDTO resourcePermissionDTO = resourcePermissionMapper.toDto(resourcePermission);

        int databaseSizeBeforeCreate = resourcePermissionRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restResourcePermissionMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(resourcePermissionDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the ResourcePermission in the database
        List<ResourcePermission> resourcePermissionList = resourcePermissionRepository.findAll();
        assertThat(resourcePermissionList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkAddIsRequired() throws Exception {
        int databaseSizeBeforeTest = resourcePermissionRepository.findAll().size();
        // set the field null
        resourcePermission.setAdd(null);

        // Create the ResourcePermission, which fails.
        ResourcePermissionDTO resourcePermissionDTO = resourcePermissionMapper.toDto(resourcePermission);

        restResourcePermissionMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(resourcePermissionDTO))
            )
            .andExpect(status().isBadRequest());

        List<ResourcePermission> resourcePermissionList = resourcePermissionRepository.findAll();
        assertThat(resourcePermissionList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkEditIsRequired() throws Exception {
        int databaseSizeBeforeTest = resourcePermissionRepository.findAll().size();
        // set the field null
        resourcePermission.setEdit(null);

        // Create the ResourcePermission, which fails.
        ResourcePermissionDTO resourcePermissionDTO = resourcePermissionMapper.toDto(resourcePermission);

        restResourcePermissionMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(resourcePermissionDTO))
            )
            .andExpect(status().isBadRequest());

        List<ResourcePermission> resourcePermissionList = resourcePermissionRepository.findAll();
        assertThat(resourcePermissionList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkManageIsRequired() throws Exception {
        int databaseSizeBeforeTest = resourcePermissionRepository.findAll().size();
        // set the field null
        resourcePermission.setManage(null);

        // Create the ResourcePermission, which fails.
        ResourcePermissionDTO resourcePermissionDTO = resourcePermissionMapper.toDto(resourcePermission);

        restResourcePermissionMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(resourcePermissionDTO))
            )
            .andExpect(status().isBadRequest());

        List<ResourcePermission> resourcePermissionList = resourcePermissionRepository.findAll();
        assertThat(resourcePermissionList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkDomainAreaIsRequired() throws Exception {
        int databaseSizeBeforeTest = resourcePermissionRepository.findAll().size();
        // set the field null
        resourcePermission.setDomainArea(null);

        // Create the ResourcePermission, which fails.
        ResourcePermissionDTO resourcePermissionDTO = resourcePermissionMapper.toDto(resourcePermission);

        restResourcePermissionMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(resourcePermissionDTO))
            )
            .andExpect(status().isBadRequest());

        List<ResourcePermission> resourcePermissionList = resourcePermissionRepository.findAll();
        assertThat(resourcePermissionList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllResourcePermissions() throws Exception {
        // Initialize the database
        resourcePermissionRepository.saveAndFlush(resourcePermission);

        // Get all the resourcePermissionList
        restResourcePermissionMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(resourcePermission.getId().intValue())))
            .andExpect(jsonPath("$.[*].add").value(hasItem(DEFAULT_ADD.toString())))
            .andExpect(jsonPath("$.[*].edit").value(hasItem(DEFAULT_EDIT.toString())))
            .andExpect(jsonPath("$.[*].manage").value(hasItem(DEFAULT_MANAGE.toString())))
            .andExpect(jsonPath("$.[*].domainArea").value(hasItem(DEFAULT_DOMAIN_AREA.toString())));
    }

    @Test
    @Transactional
    void getResourcePermission() throws Exception {
        // Initialize the database
        resourcePermissionRepository.saveAndFlush(resourcePermission);

        // Get the resourcePermission
        restResourcePermissionMockMvc
            .perform(get(ENTITY_API_URL_ID, resourcePermission.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(resourcePermission.getId().intValue()))
            .andExpect(jsonPath("$.add").value(DEFAULT_ADD.toString()))
            .andExpect(jsonPath("$.edit").value(DEFAULT_EDIT.toString()))
            .andExpect(jsonPath("$.manage").value(DEFAULT_MANAGE.toString()))
            .andExpect(jsonPath("$.domainArea").value(DEFAULT_DOMAIN_AREA.toString()));
    }

    @Test
    @Transactional
    void getNonExistingResourcePermission() throws Exception {
        // Get the resourcePermission
        restResourcePermissionMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewResourcePermission() throws Exception {
        // Initialize the database
        resourcePermissionRepository.saveAndFlush(resourcePermission);

        int databaseSizeBeforeUpdate = resourcePermissionRepository.findAll().size();

        // Update the resourcePermission
        ResourcePermission updatedResourcePermission = resourcePermissionRepository.findById(resourcePermission.getId()).get();
        // Disconnect from session so that the updates on updatedResourcePermission are not directly saved in db
        em.detach(updatedResourcePermission);
        updatedResourcePermission.add(UPDATED_ADD).edit(UPDATED_EDIT).manage(UPDATED_MANAGE).domainArea(UPDATED_DOMAIN_AREA);
        ResourcePermissionDTO resourcePermissionDTO = resourcePermissionMapper.toDto(updatedResourcePermission);

        restResourcePermissionMockMvc
            .perform(
                put(ENTITY_API_URL_ID, resourcePermissionDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(resourcePermissionDTO))
            )
            .andExpect(status().isOk());

        // Validate the ResourcePermission in the database
        List<ResourcePermission> resourcePermissionList = resourcePermissionRepository.findAll();
        assertThat(resourcePermissionList).hasSize(databaseSizeBeforeUpdate);
        ResourcePermission testResourcePermission = resourcePermissionList.get(resourcePermissionList.size() - 1);
        assertThat(testResourcePermission.getAdd()).isEqualTo(UPDATED_ADD);
        assertThat(testResourcePermission.getEdit()).isEqualTo(UPDATED_EDIT);
        assertThat(testResourcePermission.getManage()).isEqualTo(UPDATED_MANAGE);
        assertThat(testResourcePermission.getDomainArea()).isEqualTo(UPDATED_DOMAIN_AREA);
    }

    @Test
    @Transactional
    void putNonExistingResourcePermission() throws Exception {
        int databaseSizeBeforeUpdate = resourcePermissionRepository.findAll().size();
        resourcePermission.setId(count.incrementAndGet());

        // Create the ResourcePermission
        ResourcePermissionDTO resourcePermissionDTO = resourcePermissionMapper.toDto(resourcePermission);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restResourcePermissionMockMvc
            .perform(
                put(ENTITY_API_URL_ID, resourcePermissionDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(resourcePermissionDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the ResourcePermission in the database
        List<ResourcePermission> resourcePermissionList = resourcePermissionRepository.findAll();
        assertThat(resourcePermissionList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchResourcePermission() throws Exception {
        int databaseSizeBeforeUpdate = resourcePermissionRepository.findAll().size();
        resourcePermission.setId(count.incrementAndGet());

        // Create the ResourcePermission
        ResourcePermissionDTO resourcePermissionDTO = resourcePermissionMapper.toDto(resourcePermission);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restResourcePermissionMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(resourcePermissionDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the ResourcePermission in the database
        List<ResourcePermission> resourcePermissionList = resourcePermissionRepository.findAll();
        assertThat(resourcePermissionList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamResourcePermission() throws Exception {
        int databaseSizeBeforeUpdate = resourcePermissionRepository.findAll().size();
        resourcePermission.setId(count.incrementAndGet());

        // Create the ResourcePermission
        ResourcePermissionDTO resourcePermissionDTO = resourcePermissionMapper.toDto(resourcePermission);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restResourcePermissionMockMvc
            .perform(
                put(ENTITY_API_URL)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(resourcePermissionDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the ResourcePermission in the database
        List<ResourcePermission> resourcePermissionList = resourcePermissionRepository.findAll();
        assertThat(resourcePermissionList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateResourcePermissionWithPatch() throws Exception {
        // Initialize the database
        resourcePermissionRepository.saveAndFlush(resourcePermission);

        int databaseSizeBeforeUpdate = resourcePermissionRepository.findAll().size();

        // Update the resourcePermission using partial update
        ResourcePermission partialUpdatedResourcePermission = new ResourcePermission();
        partialUpdatedResourcePermission.setId(resourcePermission.getId());

        partialUpdatedResourcePermission.add(UPDATED_ADD).edit(UPDATED_EDIT);

        restResourcePermissionMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedResourcePermission.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedResourcePermission))
            )
            .andExpect(status().isOk());

        // Validate the ResourcePermission in the database
        List<ResourcePermission> resourcePermissionList = resourcePermissionRepository.findAll();
        assertThat(resourcePermissionList).hasSize(databaseSizeBeforeUpdate);
        ResourcePermission testResourcePermission = resourcePermissionList.get(resourcePermissionList.size() - 1);
        assertThat(testResourcePermission.getAdd()).isEqualTo(UPDATED_ADD);
        assertThat(testResourcePermission.getEdit()).isEqualTo(UPDATED_EDIT);
        assertThat(testResourcePermission.getManage()).isEqualTo(DEFAULT_MANAGE);
        assertThat(testResourcePermission.getDomainArea()).isEqualTo(DEFAULT_DOMAIN_AREA);
    }

    @Test
    @Transactional
    void fullUpdateResourcePermissionWithPatch() throws Exception {
        // Initialize the database
        resourcePermissionRepository.saveAndFlush(resourcePermission);

        int databaseSizeBeforeUpdate = resourcePermissionRepository.findAll().size();

        // Update the resourcePermission using partial update
        ResourcePermission partialUpdatedResourcePermission = new ResourcePermission();
        partialUpdatedResourcePermission.setId(resourcePermission.getId());

        partialUpdatedResourcePermission.add(UPDATED_ADD).edit(UPDATED_EDIT).manage(UPDATED_MANAGE).domainArea(UPDATED_DOMAIN_AREA);

        restResourcePermissionMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedResourcePermission.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedResourcePermission))
            )
            .andExpect(status().isOk());

        // Validate the ResourcePermission in the database
        List<ResourcePermission> resourcePermissionList = resourcePermissionRepository.findAll();
        assertThat(resourcePermissionList).hasSize(databaseSizeBeforeUpdate);
        ResourcePermission testResourcePermission = resourcePermissionList.get(resourcePermissionList.size() - 1);
        assertThat(testResourcePermission.getAdd()).isEqualTo(UPDATED_ADD);
        assertThat(testResourcePermission.getEdit()).isEqualTo(UPDATED_EDIT);
        assertThat(testResourcePermission.getManage()).isEqualTo(UPDATED_MANAGE);
        assertThat(testResourcePermission.getDomainArea()).isEqualTo(UPDATED_DOMAIN_AREA);
    }

    @Test
    @Transactional
    void patchNonExistingResourcePermission() throws Exception {
        int databaseSizeBeforeUpdate = resourcePermissionRepository.findAll().size();
        resourcePermission.setId(count.incrementAndGet());

        // Create the ResourcePermission
        ResourcePermissionDTO resourcePermissionDTO = resourcePermissionMapper.toDto(resourcePermission);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restResourcePermissionMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, resourcePermissionDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(resourcePermissionDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the ResourcePermission in the database
        List<ResourcePermission> resourcePermissionList = resourcePermissionRepository.findAll();
        assertThat(resourcePermissionList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchResourcePermission() throws Exception {
        int databaseSizeBeforeUpdate = resourcePermissionRepository.findAll().size();
        resourcePermission.setId(count.incrementAndGet());

        // Create the ResourcePermission
        ResourcePermissionDTO resourcePermissionDTO = resourcePermissionMapper.toDto(resourcePermission);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restResourcePermissionMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(resourcePermissionDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the ResourcePermission in the database
        List<ResourcePermission> resourcePermissionList = resourcePermissionRepository.findAll();
        assertThat(resourcePermissionList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamResourcePermission() throws Exception {
        int databaseSizeBeforeUpdate = resourcePermissionRepository.findAll().size();
        resourcePermission.setId(count.incrementAndGet());

        // Create the ResourcePermission
        ResourcePermissionDTO resourcePermissionDTO = resourcePermissionMapper.toDto(resourcePermission);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restResourcePermissionMockMvc
            .perform(
                patch(ENTITY_API_URL)
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(resourcePermissionDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the ResourcePermission in the database
        List<ResourcePermission> resourcePermissionList = resourcePermissionRepository.findAll();
        assertThat(resourcePermissionList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteResourcePermission() throws Exception {
        // Initialize the database
        resourcePermissionRepository.saveAndFlush(resourcePermission);

        int databaseSizeBeforeDelete = resourcePermissionRepository.findAll().size();

        // Delete the resourcePermission
        restResourcePermissionMockMvc
            .perform(delete(ENTITY_API_URL_ID, resourcePermission.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<ResourcePermission> resourcePermissionList = resourcePermissionRepository.findAll();
        assertThat(resourcePermissionList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
