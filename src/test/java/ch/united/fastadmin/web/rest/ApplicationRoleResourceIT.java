package ch.united.fastadmin.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import ch.united.fastadmin.IntegrationTest;
import ch.united.fastadmin.domain.ApplicationRole;
import ch.united.fastadmin.repository.ApplicationRoleRepository;
import ch.united.fastadmin.service.dto.ApplicationRoleDTO;
import ch.united.fastadmin.service.mapper.ApplicationRoleMapper;
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
 * Integration tests for the {@link ApplicationRoleResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class ApplicationRoleResourceIT {

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/application-roles";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ApplicationRoleRepository applicationRoleRepository;

    @Autowired
    private ApplicationRoleMapper applicationRoleMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restApplicationRoleMockMvc;

    private ApplicationRole applicationRole;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static ApplicationRole createEntity(EntityManager em) {
        ApplicationRole applicationRole = new ApplicationRole().name(DEFAULT_NAME);
        return applicationRole;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static ApplicationRole createUpdatedEntity(EntityManager em) {
        ApplicationRole applicationRole = new ApplicationRole().name(UPDATED_NAME);
        return applicationRole;
    }

    @BeforeEach
    public void initTest() {
        applicationRole = createEntity(em);
    }

    @Test
    @Transactional
    void createApplicationRole() throws Exception {
        int databaseSizeBeforeCreate = applicationRoleRepository.findAll().size();
        // Create the ApplicationRole
        ApplicationRoleDTO applicationRoleDTO = applicationRoleMapper.toDto(applicationRole);
        restApplicationRoleMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(applicationRoleDTO))
            )
            .andExpect(status().isCreated());

        // Validate the ApplicationRole in the database
        List<ApplicationRole> applicationRoleList = applicationRoleRepository.findAll();
        assertThat(applicationRoleList).hasSize(databaseSizeBeforeCreate + 1);
        ApplicationRole testApplicationRole = applicationRoleList.get(applicationRoleList.size() - 1);
        assertThat(testApplicationRole.getName()).isEqualTo(DEFAULT_NAME);
    }

    @Test
    @Transactional
    void createApplicationRoleWithExistingId() throws Exception {
        // Create the ApplicationRole with an existing ID
        applicationRole.setId(1L);
        ApplicationRoleDTO applicationRoleDTO = applicationRoleMapper.toDto(applicationRole);

        int databaseSizeBeforeCreate = applicationRoleRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restApplicationRoleMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(applicationRoleDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the ApplicationRole in the database
        List<ApplicationRole> applicationRoleList = applicationRoleRepository.findAll();
        assertThat(applicationRoleList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllApplicationRoles() throws Exception {
        // Initialize the database
        applicationRoleRepository.saveAndFlush(applicationRole);

        // Get all the applicationRoleList
        restApplicationRoleMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(applicationRole.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)));
    }

    @Test
    @Transactional
    void getApplicationRole() throws Exception {
        // Initialize the database
        applicationRoleRepository.saveAndFlush(applicationRole);

        // Get the applicationRole
        restApplicationRoleMockMvc
            .perform(get(ENTITY_API_URL_ID, applicationRole.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(applicationRole.getId().intValue()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME));
    }

    @Test
    @Transactional
    void getNonExistingApplicationRole() throws Exception {
        // Get the applicationRole
        restApplicationRoleMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewApplicationRole() throws Exception {
        // Initialize the database
        applicationRoleRepository.saveAndFlush(applicationRole);

        int databaseSizeBeforeUpdate = applicationRoleRepository.findAll().size();

        // Update the applicationRole
        ApplicationRole updatedApplicationRole = applicationRoleRepository.findById(applicationRole.getId()).get();
        // Disconnect from session so that the updates on updatedApplicationRole are not directly saved in db
        em.detach(updatedApplicationRole);
        updatedApplicationRole.name(UPDATED_NAME);
        ApplicationRoleDTO applicationRoleDTO = applicationRoleMapper.toDto(updatedApplicationRole);

        restApplicationRoleMockMvc
            .perform(
                put(ENTITY_API_URL_ID, applicationRoleDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(applicationRoleDTO))
            )
            .andExpect(status().isOk());

        // Validate the ApplicationRole in the database
        List<ApplicationRole> applicationRoleList = applicationRoleRepository.findAll();
        assertThat(applicationRoleList).hasSize(databaseSizeBeforeUpdate);
        ApplicationRole testApplicationRole = applicationRoleList.get(applicationRoleList.size() - 1);
        assertThat(testApplicationRole.getName()).isEqualTo(UPDATED_NAME);
    }

    @Test
    @Transactional
    void putNonExistingApplicationRole() throws Exception {
        int databaseSizeBeforeUpdate = applicationRoleRepository.findAll().size();
        applicationRole.setId(count.incrementAndGet());

        // Create the ApplicationRole
        ApplicationRoleDTO applicationRoleDTO = applicationRoleMapper.toDto(applicationRole);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restApplicationRoleMockMvc
            .perform(
                put(ENTITY_API_URL_ID, applicationRoleDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(applicationRoleDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the ApplicationRole in the database
        List<ApplicationRole> applicationRoleList = applicationRoleRepository.findAll();
        assertThat(applicationRoleList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchApplicationRole() throws Exception {
        int databaseSizeBeforeUpdate = applicationRoleRepository.findAll().size();
        applicationRole.setId(count.incrementAndGet());

        // Create the ApplicationRole
        ApplicationRoleDTO applicationRoleDTO = applicationRoleMapper.toDto(applicationRole);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restApplicationRoleMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(applicationRoleDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the ApplicationRole in the database
        List<ApplicationRole> applicationRoleList = applicationRoleRepository.findAll();
        assertThat(applicationRoleList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamApplicationRole() throws Exception {
        int databaseSizeBeforeUpdate = applicationRoleRepository.findAll().size();
        applicationRole.setId(count.incrementAndGet());

        // Create the ApplicationRole
        ApplicationRoleDTO applicationRoleDTO = applicationRoleMapper.toDto(applicationRole);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restApplicationRoleMockMvc
            .perform(
                put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(applicationRoleDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the ApplicationRole in the database
        List<ApplicationRole> applicationRoleList = applicationRoleRepository.findAll();
        assertThat(applicationRoleList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateApplicationRoleWithPatch() throws Exception {
        // Initialize the database
        applicationRoleRepository.saveAndFlush(applicationRole);

        int databaseSizeBeforeUpdate = applicationRoleRepository.findAll().size();

        // Update the applicationRole using partial update
        ApplicationRole partialUpdatedApplicationRole = new ApplicationRole();
        partialUpdatedApplicationRole.setId(applicationRole.getId());

        restApplicationRoleMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedApplicationRole.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedApplicationRole))
            )
            .andExpect(status().isOk());

        // Validate the ApplicationRole in the database
        List<ApplicationRole> applicationRoleList = applicationRoleRepository.findAll();
        assertThat(applicationRoleList).hasSize(databaseSizeBeforeUpdate);
        ApplicationRole testApplicationRole = applicationRoleList.get(applicationRoleList.size() - 1);
        assertThat(testApplicationRole.getName()).isEqualTo(DEFAULT_NAME);
    }

    @Test
    @Transactional
    void fullUpdateApplicationRoleWithPatch() throws Exception {
        // Initialize the database
        applicationRoleRepository.saveAndFlush(applicationRole);

        int databaseSizeBeforeUpdate = applicationRoleRepository.findAll().size();

        // Update the applicationRole using partial update
        ApplicationRole partialUpdatedApplicationRole = new ApplicationRole();
        partialUpdatedApplicationRole.setId(applicationRole.getId());

        partialUpdatedApplicationRole.name(UPDATED_NAME);

        restApplicationRoleMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedApplicationRole.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedApplicationRole))
            )
            .andExpect(status().isOk());

        // Validate the ApplicationRole in the database
        List<ApplicationRole> applicationRoleList = applicationRoleRepository.findAll();
        assertThat(applicationRoleList).hasSize(databaseSizeBeforeUpdate);
        ApplicationRole testApplicationRole = applicationRoleList.get(applicationRoleList.size() - 1);
        assertThat(testApplicationRole.getName()).isEqualTo(UPDATED_NAME);
    }

    @Test
    @Transactional
    void patchNonExistingApplicationRole() throws Exception {
        int databaseSizeBeforeUpdate = applicationRoleRepository.findAll().size();
        applicationRole.setId(count.incrementAndGet());

        // Create the ApplicationRole
        ApplicationRoleDTO applicationRoleDTO = applicationRoleMapper.toDto(applicationRole);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restApplicationRoleMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, applicationRoleDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(applicationRoleDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the ApplicationRole in the database
        List<ApplicationRole> applicationRoleList = applicationRoleRepository.findAll();
        assertThat(applicationRoleList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchApplicationRole() throws Exception {
        int databaseSizeBeforeUpdate = applicationRoleRepository.findAll().size();
        applicationRole.setId(count.incrementAndGet());

        // Create the ApplicationRole
        ApplicationRoleDTO applicationRoleDTO = applicationRoleMapper.toDto(applicationRole);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restApplicationRoleMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(applicationRoleDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the ApplicationRole in the database
        List<ApplicationRole> applicationRoleList = applicationRoleRepository.findAll();
        assertThat(applicationRoleList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamApplicationRole() throws Exception {
        int databaseSizeBeforeUpdate = applicationRoleRepository.findAll().size();
        applicationRole.setId(count.incrementAndGet());

        // Create the ApplicationRole
        ApplicationRoleDTO applicationRoleDTO = applicationRoleMapper.toDto(applicationRole);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restApplicationRoleMockMvc
            .perform(
                patch(ENTITY_API_URL)
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(applicationRoleDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the ApplicationRole in the database
        List<ApplicationRole> applicationRoleList = applicationRoleRepository.findAll();
        assertThat(applicationRoleList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteApplicationRole() throws Exception {
        // Initialize the database
        applicationRoleRepository.saveAndFlush(applicationRole);

        int databaseSizeBeforeDelete = applicationRoleRepository.findAll().size();

        // Delete the applicationRole
        restApplicationRoleMockMvc
            .perform(delete(ENTITY_API_URL_ID, applicationRole.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<ApplicationRole> applicationRoleList = applicationRoleRepository.findAll();
        assertThat(applicationRoleList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
