package ch.united.fastadmin.web.rest;

import static ch.united.fastadmin.web.rest.TestUtil.sameInstant;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import ch.united.fastadmin.IntegrationTest;
import ch.united.fastadmin.domain.Effort;
import ch.united.fastadmin.domain.enumeration.ReportingEntityType;
import ch.united.fastadmin.repository.EffortRepository;
import ch.united.fastadmin.service.dto.EffortDTO;
import ch.united.fastadmin.service.mapper.EffortMapper;
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
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
 * Integration tests for the {@link EffortResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class EffortResourceIT {

    private static final Integer DEFAULT_REMOTE_ID = 1;
    private static final Integer UPDATED_REMOTE_ID = 2;

    private static final Integer DEFAULT_USER_ID = 1;
    private static final Integer UPDATED_USER_ID = 2;

    private static final String DEFAULT_USER_NAME = "AAAAAAAAAA";
    private static final String UPDATED_USER_NAME = "BBBBBBBBBB";

    private static final ReportingEntityType DEFAULT_ENTITY_TYPE = ReportingEntityType.PROJECT;
    private static final ReportingEntityType UPDATED_ENTITY_TYPE = ReportingEntityType.COST_UNIT;

    private static final Integer DEFAULT_ENTITY_ID = 1;
    private static final Integer UPDATED_ENTITY_ID = 2;

    private static final Double DEFAULT_DURATION = 1D;
    private static final Double UPDATED_DURATION = 2D;

    private static final LocalDate DEFAULT_DATE = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_DATE = LocalDate.now(ZoneId.systemDefault());

    private static final String DEFAULT_ACTIVITY_NAME = "AAAAAAAAAA";
    private static final String UPDATED_ACTIVITY_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_NOTES = "AAAAAAAAAA";
    private static final String UPDATED_NOTES = "BBBBBBBBBB";

    private static final Boolean DEFAULT_IS_INVOICED = false;
    private static final Boolean UPDATED_IS_INVOICED = true;

    private static final ZonedDateTime DEFAULT_UPDATED = ZonedDateTime.ofInstant(Instant.ofEpochMilli(0L), ZoneOffset.UTC);
    private static final ZonedDateTime UPDATED_UPDATED = ZonedDateTime.now(ZoneId.systemDefault()).withNano(0);

    private static final Double DEFAULT_HOURLY_RATE = 1D;
    private static final Double UPDATED_HOURLY_RATE = 2D;

    private static final String ENTITY_API_URL = "/api/efforts";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private EffortRepository effortRepository;

    @Autowired
    private EffortMapper effortMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restEffortMockMvc;

    private Effort effort;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Effort createEntity(EntityManager em) {
        Effort effort = new Effort()
            .remoteId(DEFAULT_REMOTE_ID)
            .userId(DEFAULT_USER_ID)
            .userName(DEFAULT_USER_NAME)
            .entityType(DEFAULT_ENTITY_TYPE)
            .entityId(DEFAULT_ENTITY_ID)
            .duration(DEFAULT_DURATION)
            .date(DEFAULT_DATE)
            .activityName(DEFAULT_ACTIVITY_NAME)
            .notes(DEFAULT_NOTES)
            .isInvoiced(DEFAULT_IS_INVOICED)
            .updated(DEFAULT_UPDATED)
            .hourlyRate(DEFAULT_HOURLY_RATE);
        return effort;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Effort createUpdatedEntity(EntityManager em) {
        Effort effort = new Effort()
            .remoteId(UPDATED_REMOTE_ID)
            .userId(UPDATED_USER_ID)
            .userName(UPDATED_USER_NAME)
            .entityType(UPDATED_ENTITY_TYPE)
            .entityId(UPDATED_ENTITY_ID)
            .duration(UPDATED_DURATION)
            .date(UPDATED_DATE)
            .activityName(UPDATED_ACTIVITY_NAME)
            .notes(UPDATED_NOTES)
            .isInvoiced(UPDATED_IS_INVOICED)
            .updated(UPDATED_UPDATED)
            .hourlyRate(UPDATED_HOURLY_RATE);
        return effort;
    }

    @BeforeEach
    public void initTest() {
        effort = createEntity(em);
    }

    @Test
    @Transactional
    void createEffort() throws Exception {
        int databaseSizeBeforeCreate = effortRepository.findAll().size();
        // Create the Effort
        EffortDTO effortDTO = effortMapper.toDto(effort);
        restEffortMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(effortDTO)))
            .andExpect(status().isCreated());

        // Validate the Effort in the database
        List<Effort> effortList = effortRepository.findAll();
        assertThat(effortList).hasSize(databaseSizeBeforeCreate + 1);
        Effort testEffort = effortList.get(effortList.size() - 1);
        assertThat(testEffort.getRemoteId()).isEqualTo(DEFAULT_REMOTE_ID);
        assertThat(testEffort.getUserId()).isEqualTo(DEFAULT_USER_ID);
        assertThat(testEffort.getUserName()).isEqualTo(DEFAULT_USER_NAME);
        assertThat(testEffort.getEntityType()).isEqualTo(DEFAULT_ENTITY_TYPE);
        assertThat(testEffort.getEntityId()).isEqualTo(DEFAULT_ENTITY_ID);
        assertThat(testEffort.getDuration()).isEqualTo(DEFAULT_DURATION);
        assertThat(testEffort.getDate()).isEqualTo(DEFAULT_DATE);
        assertThat(testEffort.getActivityName()).isEqualTo(DEFAULT_ACTIVITY_NAME);
        assertThat(testEffort.getNotes()).isEqualTo(DEFAULT_NOTES);
        assertThat(testEffort.getIsInvoiced()).isEqualTo(DEFAULT_IS_INVOICED);
        assertThat(testEffort.getUpdated()).isEqualTo(DEFAULT_UPDATED);
        assertThat(testEffort.getHourlyRate()).isEqualTo(DEFAULT_HOURLY_RATE);
    }

    @Test
    @Transactional
    void createEffortWithExistingId() throws Exception {
        // Create the Effort with an existing ID
        effort.setId(1L);
        EffortDTO effortDTO = effortMapper.toDto(effort);

        int databaseSizeBeforeCreate = effortRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restEffortMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(effortDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Effort in the database
        List<Effort> effortList = effortRepository.findAll();
        assertThat(effortList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllEfforts() throws Exception {
        // Initialize the database
        effortRepository.saveAndFlush(effort);

        // Get all the effortList
        restEffortMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(effort.getId().intValue())))
            .andExpect(jsonPath("$.[*].remoteId").value(hasItem(DEFAULT_REMOTE_ID)))
            .andExpect(jsonPath("$.[*].userId").value(hasItem(DEFAULT_USER_ID)))
            .andExpect(jsonPath("$.[*].userName").value(hasItem(DEFAULT_USER_NAME)))
            .andExpect(jsonPath("$.[*].entityType").value(hasItem(DEFAULT_ENTITY_TYPE.toString())))
            .andExpect(jsonPath("$.[*].entityId").value(hasItem(DEFAULT_ENTITY_ID)))
            .andExpect(jsonPath("$.[*].duration").value(hasItem(DEFAULT_DURATION.doubleValue())))
            .andExpect(jsonPath("$.[*].date").value(hasItem(DEFAULT_DATE.toString())))
            .andExpect(jsonPath("$.[*].activityName").value(hasItem(DEFAULT_ACTIVITY_NAME)))
            .andExpect(jsonPath("$.[*].notes").value(hasItem(DEFAULT_NOTES)))
            .andExpect(jsonPath("$.[*].isInvoiced").value(hasItem(DEFAULT_IS_INVOICED.booleanValue())))
            .andExpect(jsonPath("$.[*].updated").value(hasItem(sameInstant(DEFAULT_UPDATED))))
            .andExpect(jsonPath("$.[*].hourlyRate").value(hasItem(DEFAULT_HOURLY_RATE.doubleValue())));
    }

    @Test
    @Transactional
    void getEffort() throws Exception {
        // Initialize the database
        effortRepository.saveAndFlush(effort);

        // Get the effort
        restEffortMockMvc
            .perform(get(ENTITY_API_URL_ID, effort.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(effort.getId().intValue()))
            .andExpect(jsonPath("$.remoteId").value(DEFAULT_REMOTE_ID))
            .andExpect(jsonPath("$.userId").value(DEFAULT_USER_ID))
            .andExpect(jsonPath("$.userName").value(DEFAULT_USER_NAME))
            .andExpect(jsonPath("$.entityType").value(DEFAULT_ENTITY_TYPE.toString()))
            .andExpect(jsonPath("$.entityId").value(DEFAULT_ENTITY_ID))
            .andExpect(jsonPath("$.duration").value(DEFAULT_DURATION.doubleValue()))
            .andExpect(jsonPath("$.date").value(DEFAULT_DATE.toString()))
            .andExpect(jsonPath("$.activityName").value(DEFAULT_ACTIVITY_NAME))
            .andExpect(jsonPath("$.notes").value(DEFAULT_NOTES))
            .andExpect(jsonPath("$.isInvoiced").value(DEFAULT_IS_INVOICED.booleanValue()))
            .andExpect(jsonPath("$.updated").value(sameInstant(DEFAULT_UPDATED)))
            .andExpect(jsonPath("$.hourlyRate").value(DEFAULT_HOURLY_RATE.doubleValue()));
    }

    @Test
    @Transactional
    void getNonExistingEffort() throws Exception {
        // Get the effort
        restEffortMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewEffort() throws Exception {
        // Initialize the database
        effortRepository.saveAndFlush(effort);

        int databaseSizeBeforeUpdate = effortRepository.findAll().size();

        // Update the effort
        Effort updatedEffort = effortRepository.findById(effort.getId()).get();
        // Disconnect from session so that the updates on updatedEffort are not directly saved in db
        em.detach(updatedEffort);
        updatedEffort
            .remoteId(UPDATED_REMOTE_ID)
            .userId(UPDATED_USER_ID)
            .userName(UPDATED_USER_NAME)
            .entityType(UPDATED_ENTITY_TYPE)
            .entityId(UPDATED_ENTITY_ID)
            .duration(UPDATED_DURATION)
            .date(UPDATED_DATE)
            .activityName(UPDATED_ACTIVITY_NAME)
            .notes(UPDATED_NOTES)
            .isInvoiced(UPDATED_IS_INVOICED)
            .updated(UPDATED_UPDATED)
            .hourlyRate(UPDATED_HOURLY_RATE);
        EffortDTO effortDTO = effortMapper.toDto(updatedEffort);

        restEffortMockMvc
            .perform(
                put(ENTITY_API_URL_ID, effortDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(effortDTO))
            )
            .andExpect(status().isOk());

        // Validate the Effort in the database
        List<Effort> effortList = effortRepository.findAll();
        assertThat(effortList).hasSize(databaseSizeBeforeUpdate);
        Effort testEffort = effortList.get(effortList.size() - 1);
        assertThat(testEffort.getRemoteId()).isEqualTo(UPDATED_REMOTE_ID);
        assertThat(testEffort.getUserId()).isEqualTo(UPDATED_USER_ID);
        assertThat(testEffort.getUserName()).isEqualTo(UPDATED_USER_NAME);
        assertThat(testEffort.getEntityType()).isEqualTo(UPDATED_ENTITY_TYPE);
        assertThat(testEffort.getEntityId()).isEqualTo(UPDATED_ENTITY_ID);
        assertThat(testEffort.getDuration()).isEqualTo(UPDATED_DURATION);
        assertThat(testEffort.getDate()).isEqualTo(UPDATED_DATE);
        assertThat(testEffort.getActivityName()).isEqualTo(UPDATED_ACTIVITY_NAME);
        assertThat(testEffort.getNotes()).isEqualTo(UPDATED_NOTES);
        assertThat(testEffort.getIsInvoiced()).isEqualTo(UPDATED_IS_INVOICED);
        assertThat(testEffort.getUpdated()).isEqualTo(UPDATED_UPDATED);
        assertThat(testEffort.getHourlyRate()).isEqualTo(UPDATED_HOURLY_RATE);
    }

    @Test
    @Transactional
    void putNonExistingEffort() throws Exception {
        int databaseSizeBeforeUpdate = effortRepository.findAll().size();
        effort.setId(count.incrementAndGet());

        // Create the Effort
        EffortDTO effortDTO = effortMapper.toDto(effort);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restEffortMockMvc
            .perform(
                put(ENTITY_API_URL_ID, effortDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(effortDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Effort in the database
        List<Effort> effortList = effortRepository.findAll();
        assertThat(effortList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchEffort() throws Exception {
        int databaseSizeBeforeUpdate = effortRepository.findAll().size();
        effort.setId(count.incrementAndGet());

        // Create the Effort
        EffortDTO effortDTO = effortMapper.toDto(effort);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restEffortMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(effortDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Effort in the database
        List<Effort> effortList = effortRepository.findAll();
        assertThat(effortList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamEffort() throws Exception {
        int databaseSizeBeforeUpdate = effortRepository.findAll().size();
        effort.setId(count.incrementAndGet());

        // Create the Effort
        EffortDTO effortDTO = effortMapper.toDto(effort);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restEffortMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(effortDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Effort in the database
        List<Effort> effortList = effortRepository.findAll();
        assertThat(effortList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateEffortWithPatch() throws Exception {
        // Initialize the database
        effortRepository.saveAndFlush(effort);

        int databaseSizeBeforeUpdate = effortRepository.findAll().size();

        // Update the effort using partial update
        Effort partialUpdatedEffort = new Effort();
        partialUpdatedEffort.setId(effort.getId());

        partialUpdatedEffort
            .remoteId(UPDATED_REMOTE_ID)
            .userId(UPDATED_USER_ID)
            .date(UPDATED_DATE)
            .activityName(UPDATED_ACTIVITY_NAME)
            .notes(UPDATED_NOTES)
            .isInvoiced(UPDATED_IS_INVOICED)
            .updated(UPDATED_UPDATED)
            .hourlyRate(UPDATED_HOURLY_RATE);

        restEffortMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedEffort.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedEffort))
            )
            .andExpect(status().isOk());

        // Validate the Effort in the database
        List<Effort> effortList = effortRepository.findAll();
        assertThat(effortList).hasSize(databaseSizeBeforeUpdate);
        Effort testEffort = effortList.get(effortList.size() - 1);
        assertThat(testEffort.getRemoteId()).isEqualTo(UPDATED_REMOTE_ID);
        assertThat(testEffort.getUserId()).isEqualTo(UPDATED_USER_ID);
        assertThat(testEffort.getUserName()).isEqualTo(DEFAULT_USER_NAME);
        assertThat(testEffort.getEntityType()).isEqualTo(DEFAULT_ENTITY_TYPE);
        assertThat(testEffort.getEntityId()).isEqualTo(DEFAULT_ENTITY_ID);
        assertThat(testEffort.getDuration()).isEqualTo(DEFAULT_DURATION);
        assertThat(testEffort.getDate()).isEqualTo(UPDATED_DATE);
        assertThat(testEffort.getActivityName()).isEqualTo(UPDATED_ACTIVITY_NAME);
        assertThat(testEffort.getNotes()).isEqualTo(UPDATED_NOTES);
        assertThat(testEffort.getIsInvoiced()).isEqualTo(UPDATED_IS_INVOICED);
        assertThat(testEffort.getUpdated()).isEqualTo(UPDATED_UPDATED);
        assertThat(testEffort.getHourlyRate()).isEqualTo(UPDATED_HOURLY_RATE);
    }

    @Test
    @Transactional
    void fullUpdateEffortWithPatch() throws Exception {
        // Initialize the database
        effortRepository.saveAndFlush(effort);

        int databaseSizeBeforeUpdate = effortRepository.findAll().size();

        // Update the effort using partial update
        Effort partialUpdatedEffort = new Effort();
        partialUpdatedEffort.setId(effort.getId());

        partialUpdatedEffort
            .remoteId(UPDATED_REMOTE_ID)
            .userId(UPDATED_USER_ID)
            .userName(UPDATED_USER_NAME)
            .entityType(UPDATED_ENTITY_TYPE)
            .entityId(UPDATED_ENTITY_ID)
            .duration(UPDATED_DURATION)
            .date(UPDATED_DATE)
            .activityName(UPDATED_ACTIVITY_NAME)
            .notes(UPDATED_NOTES)
            .isInvoiced(UPDATED_IS_INVOICED)
            .updated(UPDATED_UPDATED)
            .hourlyRate(UPDATED_HOURLY_RATE);

        restEffortMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedEffort.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedEffort))
            )
            .andExpect(status().isOk());

        // Validate the Effort in the database
        List<Effort> effortList = effortRepository.findAll();
        assertThat(effortList).hasSize(databaseSizeBeforeUpdate);
        Effort testEffort = effortList.get(effortList.size() - 1);
        assertThat(testEffort.getRemoteId()).isEqualTo(UPDATED_REMOTE_ID);
        assertThat(testEffort.getUserId()).isEqualTo(UPDATED_USER_ID);
        assertThat(testEffort.getUserName()).isEqualTo(UPDATED_USER_NAME);
        assertThat(testEffort.getEntityType()).isEqualTo(UPDATED_ENTITY_TYPE);
        assertThat(testEffort.getEntityId()).isEqualTo(UPDATED_ENTITY_ID);
        assertThat(testEffort.getDuration()).isEqualTo(UPDATED_DURATION);
        assertThat(testEffort.getDate()).isEqualTo(UPDATED_DATE);
        assertThat(testEffort.getActivityName()).isEqualTo(UPDATED_ACTIVITY_NAME);
        assertThat(testEffort.getNotes()).isEqualTo(UPDATED_NOTES);
        assertThat(testEffort.getIsInvoiced()).isEqualTo(UPDATED_IS_INVOICED);
        assertThat(testEffort.getUpdated()).isEqualTo(UPDATED_UPDATED);
        assertThat(testEffort.getHourlyRate()).isEqualTo(UPDATED_HOURLY_RATE);
    }

    @Test
    @Transactional
    void patchNonExistingEffort() throws Exception {
        int databaseSizeBeforeUpdate = effortRepository.findAll().size();
        effort.setId(count.incrementAndGet());

        // Create the Effort
        EffortDTO effortDTO = effortMapper.toDto(effort);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restEffortMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, effortDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(effortDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Effort in the database
        List<Effort> effortList = effortRepository.findAll();
        assertThat(effortList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchEffort() throws Exception {
        int databaseSizeBeforeUpdate = effortRepository.findAll().size();
        effort.setId(count.incrementAndGet());

        // Create the Effort
        EffortDTO effortDTO = effortMapper.toDto(effort);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restEffortMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(effortDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Effort in the database
        List<Effort> effortList = effortRepository.findAll();
        assertThat(effortList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamEffort() throws Exception {
        int databaseSizeBeforeUpdate = effortRepository.findAll().size();
        effort.setId(count.incrementAndGet());

        // Create the Effort
        EffortDTO effortDTO = effortMapper.toDto(effort);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restEffortMockMvc
            .perform(
                patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(effortDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the Effort in the database
        List<Effort> effortList = effortRepository.findAll();
        assertThat(effortList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteEffort() throws Exception {
        // Initialize the database
        effortRepository.saveAndFlush(effort);

        int databaseSizeBeforeDelete = effortRepository.findAll().size();

        // Delete the effort
        restEffortMockMvc
            .perform(delete(ENTITY_API_URL_ID, effort.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Effort> effortList = effortRepository.findAll();
        assertThat(effortList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
