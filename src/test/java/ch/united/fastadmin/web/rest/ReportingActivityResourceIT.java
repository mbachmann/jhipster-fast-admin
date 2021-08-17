package ch.united.fastadmin.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import ch.united.fastadmin.IntegrationTest;
import ch.united.fastadmin.domain.ReportingActivity;
import ch.united.fastadmin.repository.ReportingActivityRepository;
import ch.united.fastadmin.service.dto.ReportingActivityDTO;
import ch.united.fastadmin.service.mapper.ReportingActivityMapper;
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
 * Integration tests for the {@link ReportingActivityResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class ReportingActivityResourceIT {

    private static final Integer DEFAULT_REMOTE_ID = 1;
    private static final Integer UPDATED_REMOTE_ID = 2;

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    private static final Boolean DEFAULT_USE_SERVICE_PRICE = false;
    private static final Boolean UPDATED_USE_SERVICE_PRICE = true;

    private static final Boolean DEFAULT_INACTIV = false;
    private static final Boolean UPDATED_INACTIV = true;

    private static final String ENTITY_API_URL = "/api/reporting-activities";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ReportingActivityRepository reportingActivityRepository;

    @Autowired
    private ReportingActivityMapper reportingActivityMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restReportingActivityMockMvc;

    private ReportingActivity reportingActivity;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static ReportingActivity createEntity(EntityManager em) {
        ReportingActivity reportingActivity = new ReportingActivity()
            .remoteId(DEFAULT_REMOTE_ID)
            .name(DEFAULT_NAME)
            .useServicePrice(DEFAULT_USE_SERVICE_PRICE)
            .inactiv(DEFAULT_INACTIV);
        return reportingActivity;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static ReportingActivity createUpdatedEntity(EntityManager em) {
        ReportingActivity reportingActivity = new ReportingActivity()
            .remoteId(UPDATED_REMOTE_ID)
            .name(UPDATED_NAME)
            .useServicePrice(UPDATED_USE_SERVICE_PRICE)
            .inactiv(UPDATED_INACTIV);
        return reportingActivity;
    }

    @BeforeEach
    public void initTest() {
        reportingActivity = createEntity(em);
    }

    @Test
    @Transactional
    void createReportingActivity() throws Exception {
        int databaseSizeBeforeCreate = reportingActivityRepository.findAll().size();
        // Create the ReportingActivity
        ReportingActivityDTO reportingActivityDTO = reportingActivityMapper.toDto(reportingActivity);
        restReportingActivityMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(reportingActivityDTO))
            )
            .andExpect(status().isCreated());

        // Validate the ReportingActivity in the database
        List<ReportingActivity> reportingActivityList = reportingActivityRepository.findAll();
        assertThat(reportingActivityList).hasSize(databaseSizeBeforeCreate + 1);
        ReportingActivity testReportingActivity = reportingActivityList.get(reportingActivityList.size() - 1);
        assertThat(testReportingActivity.getRemoteId()).isEqualTo(DEFAULT_REMOTE_ID);
        assertThat(testReportingActivity.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testReportingActivity.getUseServicePrice()).isEqualTo(DEFAULT_USE_SERVICE_PRICE);
        assertThat(testReportingActivity.getInactiv()).isEqualTo(DEFAULT_INACTIV);
    }

    @Test
    @Transactional
    void createReportingActivityWithExistingId() throws Exception {
        // Create the ReportingActivity with an existing ID
        reportingActivity.setId(1L);
        ReportingActivityDTO reportingActivityDTO = reportingActivityMapper.toDto(reportingActivity);

        int databaseSizeBeforeCreate = reportingActivityRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restReportingActivityMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(reportingActivityDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the ReportingActivity in the database
        List<ReportingActivity> reportingActivityList = reportingActivityRepository.findAll();
        assertThat(reportingActivityList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllReportingActivities() throws Exception {
        // Initialize the database
        reportingActivityRepository.saveAndFlush(reportingActivity);

        // Get all the reportingActivityList
        restReportingActivityMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(reportingActivity.getId().intValue())))
            .andExpect(jsonPath("$.[*].remoteId").value(hasItem(DEFAULT_REMOTE_ID)))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)))
            .andExpect(jsonPath("$.[*].useServicePrice").value(hasItem(DEFAULT_USE_SERVICE_PRICE.booleanValue())))
            .andExpect(jsonPath("$.[*].inactiv").value(hasItem(DEFAULT_INACTIV.booleanValue())));
    }

    @Test
    @Transactional
    void getReportingActivity() throws Exception {
        // Initialize the database
        reportingActivityRepository.saveAndFlush(reportingActivity);

        // Get the reportingActivity
        restReportingActivityMockMvc
            .perform(get(ENTITY_API_URL_ID, reportingActivity.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(reportingActivity.getId().intValue()))
            .andExpect(jsonPath("$.remoteId").value(DEFAULT_REMOTE_ID))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME))
            .andExpect(jsonPath("$.useServicePrice").value(DEFAULT_USE_SERVICE_PRICE.booleanValue()))
            .andExpect(jsonPath("$.inactiv").value(DEFAULT_INACTIV.booleanValue()));
    }

    @Test
    @Transactional
    void getNonExistingReportingActivity() throws Exception {
        // Get the reportingActivity
        restReportingActivityMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewReportingActivity() throws Exception {
        // Initialize the database
        reportingActivityRepository.saveAndFlush(reportingActivity);

        int databaseSizeBeforeUpdate = reportingActivityRepository.findAll().size();

        // Update the reportingActivity
        ReportingActivity updatedReportingActivity = reportingActivityRepository.findById(reportingActivity.getId()).get();
        // Disconnect from session so that the updates on updatedReportingActivity are not directly saved in db
        em.detach(updatedReportingActivity);
        updatedReportingActivity
            .remoteId(UPDATED_REMOTE_ID)
            .name(UPDATED_NAME)
            .useServicePrice(UPDATED_USE_SERVICE_PRICE)
            .inactiv(UPDATED_INACTIV);
        ReportingActivityDTO reportingActivityDTO = reportingActivityMapper.toDto(updatedReportingActivity);

        restReportingActivityMockMvc
            .perform(
                put(ENTITY_API_URL_ID, reportingActivityDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(reportingActivityDTO))
            )
            .andExpect(status().isOk());

        // Validate the ReportingActivity in the database
        List<ReportingActivity> reportingActivityList = reportingActivityRepository.findAll();
        assertThat(reportingActivityList).hasSize(databaseSizeBeforeUpdate);
        ReportingActivity testReportingActivity = reportingActivityList.get(reportingActivityList.size() - 1);
        assertThat(testReportingActivity.getRemoteId()).isEqualTo(UPDATED_REMOTE_ID);
        assertThat(testReportingActivity.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testReportingActivity.getUseServicePrice()).isEqualTo(UPDATED_USE_SERVICE_PRICE);
        assertThat(testReportingActivity.getInactiv()).isEqualTo(UPDATED_INACTIV);
    }

    @Test
    @Transactional
    void putNonExistingReportingActivity() throws Exception {
        int databaseSizeBeforeUpdate = reportingActivityRepository.findAll().size();
        reportingActivity.setId(count.incrementAndGet());

        // Create the ReportingActivity
        ReportingActivityDTO reportingActivityDTO = reportingActivityMapper.toDto(reportingActivity);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restReportingActivityMockMvc
            .perform(
                put(ENTITY_API_URL_ID, reportingActivityDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(reportingActivityDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the ReportingActivity in the database
        List<ReportingActivity> reportingActivityList = reportingActivityRepository.findAll();
        assertThat(reportingActivityList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchReportingActivity() throws Exception {
        int databaseSizeBeforeUpdate = reportingActivityRepository.findAll().size();
        reportingActivity.setId(count.incrementAndGet());

        // Create the ReportingActivity
        ReportingActivityDTO reportingActivityDTO = reportingActivityMapper.toDto(reportingActivity);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restReportingActivityMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(reportingActivityDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the ReportingActivity in the database
        List<ReportingActivity> reportingActivityList = reportingActivityRepository.findAll();
        assertThat(reportingActivityList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamReportingActivity() throws Exception {
        int databaseSizeBeforeUpdate = reportingActivityRepository.findAll().size();
        reportingActivity.setId(count.incrementAndGet());

        // Create the ReportingActivity
        ReportingActivityDTO reportingActivityDTO = reportingActivityMapper.toDto(reportingActivity);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restReportingActivityMockMvc
            .perform(
                put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(reportingActivityDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the ReportingActivity in the database
        List<ReportingActivity> reportingActivityList = reportingActivityRepository.findAll();
        assertThat(reportingActivityList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateReportingActivityWithPatch() throws Exception {
        // Initialize the database
        reportingActivityRepository.saveAndFlush(reportingActivity);

        int databaseSizeBeforeUpdate = reportingActivityRepository.findAll().size();

        // Update the reportingActivity using partial update
        ReportingActivity partialUpdatedReportingActivity = new ReportingActivity();
        partialUpdatedReportingActivity.setId(reportingActivity.getId());

        partialUpdatedReportingActivity.name(UPDATED_NAME);

        restReportingActivityMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedReportingActivity.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedReportingActivity))
            )
            .andExpect(status().isOk());

        // Validate the ReportingActivity in the database
        List<ReportingActivity> reportingActivityList = reportingActivityRepository.findAll();
        assertThat(reportingActivityList).hasSize(databaseSizeBeforeUpdate);
        ReportingActivity testReportingActivity = reportingActivityList.get(reportingActivityList.size() - 1);
        assertThat(testReportingActivity.getRemoteId()).isEqualTo(DEFAULT_REMOTE_ID);
        assertThat(testReportingActivity.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testReportingActivity.getUseServicePrice()).isEqualTo(DEFAULT_USE_SERVICE_PRICE);
        assertThat(testReportingActivity.getInactiv()).isEqualTo(DEFAULT_INACTIV);
    }

    @Test
    @Transactional
    void fullUpdateReportingActivityWithPatch() throws Exception {
        // Initialize the database
        reportingActivityRepository.saveAndFlush(reportingActivity);

        int databaseSizeBeforeUpdate = reportingActivityRepository.findAll().size();

        // Update the reportingActivity using partial update
        ReportingActivity partialUpdatedReportingActivity = new ReportingActivity();
        partialUpdatedReportingActivity.setId(reportingActivity.getId());

        partialUpdatedReportingActivity
            .remoteId(UPDATED_REMOTE_ID)
            .name(UPDATED_NAME)
            .useServicePrice(UPDATED_USE_SERVICE_PRICE)
            .inactiv(UPDATED_INACTIV);

        restReportingActivityMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedReportingActivity.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedReportingActivity))
            )
            .andExpect(status().isOk());

        // Validate the ReportingActivity in the database
        List<ReportingActivity> reportingActivityList = reportingActivityRepository.findAll();
        assertThat(reportingActivityList).hasSize(databaseSizeBeforeUpdate);
        ReportingActivity testReportingActivity = reportingActivityList.get(reportingActivityList.size() - 1);
        assertThat(testReportingActivity.getRemoteId()).isEqualTo(UPDATED_REMOTE_ID);
        assertThat(testReportingActivity.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testReportingActivity.getUseServicePrice()).isEqualTo(UPDATED_USE_SERVICE_PRICE);
        assertThat(testReportingActivity.getInactiv()).isEqualTo(UPDATED_INACTIV);
    }

    @Test
    @Transactional
    void patchNonExistingReportingActivity() throws Exception {
        int databaseSizeBeforeUpdate = reportingActivityRepository.findAll().size();
        reportingActivity.setId(count.incrementAndGet());

        // Create the ReportingActivity
        ReportingActivityDTO reportingActivityDTO = reportingActivityMapper.toDto(reportingActivity);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restReportingActivityMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, reportingActivityDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(reportingActivityDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the ReportingActivity in the database
        List<ReportingActivity> reportingActivityList = reportingActivityRepository.findAll();
        assertThat(reportingActivityList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchReportingActivity() throws Exception {
        int databaseSizeBeforeUpdate = reportingActivityRepository.findAll().size();
        reportingActivity.setId(count.incrementAndGet());

        // Create the ReportingActivity
        ReportingActivityDTO reportingActivityDTO = reportingActivityMapper.toDto(reportingActivity);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restReportingActivityMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(reportingActivityDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the ReportingActivity in the database
        List<ReportingActivity> reportingActivityList = reportingActivityRepository.findAll();
        assertThat(reportingActivityList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamReportingActivity() throws Exception {
        int databaseSizeBeforeUpdate = reportingActivityRepository.findAll().size();
        reportingActivity.setId(count.incrementAndGet());

        // Create the ReportingActivity
        ReportingActivityDTO reportingActivityDTO = reportingActivityMapper.toDto(reportingActivity);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restReportingActivityMockMvc
            .perform(
                patch(ENTITY_API_URL)
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(reportingActivityDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the ReportingActivity in the database
        List<ReportingActivity> reportingActivityList = reportingActivityRepository.findAll();
        assertThat(reportingActivityList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteReportingActivity() throws Exception {
        // Initialize the database
        reportingActivityRepository.saveAndFlush(reportingActivity);

        int databaseSizeBeforeDelete = reportingActivityRepository.findAll().size();

        // Delete the reportingActivity
        restReportingActivityMockMvc
            .perform(delete(ENTITY_API_URL_ID, reportingActivity.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<ReportingActivity> reportingActivityList = reportingActivityRepository.findAll();
        assertThat(reportingActivityList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
