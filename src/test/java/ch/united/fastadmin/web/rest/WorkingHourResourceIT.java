package ch.united.fastadmin.web.rest;

import static ch.united.fastadmin.web.rest.TestUtil.sameInstant;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import ch.united.fastadmin.IntegrationTest;
import ch.united.fastadmin.domain.WorkingHour;
import ch.united.fastadmin.repository.WorkingHourRepository;
import ch.united.fastadmin.service.dto.WorkingHourDTO;
import ch.united.fastadmin.service.mapper.WorkingHourMapper;
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
 * Integration tests for the {@link WorkingHourResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class WorkingHourResourceIT {

    private static final Integer DEFAULT_REMOTE_ID = 1;
    private static final Integer UPDATED_REMOTE_ID = 2;

    private static final String DEFAULT_USER_NAME = "AAAAAAAAAA";
    private static final String UPDATED_USER_NAME = "BBBBBBBBBB";

    private static final LocalDate DEFAULT_DATE = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_DATE = LocalDate.now(ZoneId.systemDefault());

    private static final ZonedDateTime DEFAULT_TIME_START = ZonedDateTime.ofInstant(Instant.ofEpochMilli(0L), ZoneOffset.UTC);
    private static final ZonedDateTime UPDATED_TIME_START = ZonedDateTime.now(ZoneId.systemDefault()).withNano(0);

    private static final ZonedDateTime DEFAULT_TIME_END = ZonedDateTime.ofInstant(Instant.ofEpochMilli(0L), ZoneOffset.UTC);
    private static final ZonedDateTime UPDATED_TIME_END = ZonedDateTime.now(ZoneId.systemDefault()).withNano(0);

    private static final ZonedDateTime DEFAULT_CREATED = ZonedDateTime.ofInstant(Instant.ofEpochMilli(0L), ZoneOffset.UTC);
    private static final ZonedDateTime UPDATED_CREATED = ZonedDateTime.now(ZoneId.systemDefault()).withNano(0);

    private static final String ENTITY_API_URL = "/api/working-hours";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private WorkingHourRepository workingHourRepository;

    @Autowired
    private WorkingHourMapper workingHourMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restWorkingHourMockMvc;

    private WorkingHour workingHour;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static WorkingHour createEntity(EntityManager em) {
        WorkingHour workingHour = new WorkingHour()
            .remoteId(DEFAULT_REMOTE_ID)
            .userName(DEFAULT_USER_NAME)
            .date(DEFAULT_DATE)
            .timeStart(DEFAULT_TIME_START)
            .timeEnd(DEFAULT_TIME_END)
            .created(DEFAULT_CREATED);
        return workingHour;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static WorkingHour createUpdatedEntity(EntityManager em) {
        WorkingHour workingHour = new WorkingHour()
            .remoteId(UPDATED_REMOTE_ID)
            .userName(UPDATED_USER_NAME)
            .date(UPDATED_DATE)
            .timeStart(UPDATED_TIME_START)
            .timeEnd(UPDATED_TIME_END)
            .created(UPDATED_CREATED);
        return workingHour;
    }

    @BeforeEach
    public void initTest() {
        workingHour = createEntity(em);
    }

    @Test
    @Transactional
    void createWorkingHour() throws Exception {
        int databaseSizeBeforeCreate = workingHourRepository.findAll().size();
        // Create the WorkingHour
        WorkingHourDTO workingHourDTO = workingHourMapper.toDto(workingHour);
        restWorkingHourMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(workingHourDTO))
            )
            .andExpect(status().isCreated());

        // Validate the WorkingHour in the database
        List<WorkingHour> workingHourList = workingHourRepository.findAll();
        assertThat(workingHourList).hasSize(databaseSizeBeforeCreate + 1);
        WorkingHour testWorkingHour = workingHourList.get(workingHourList.size() - 1);
        assertThat(testWorkingHour.getRemoteId()).isEqualTo(DEFAULT_REMOTE_ID);
        assertThat(testWorkingHour.getUserName()).isEqualTo(DEFAULT_USER_NAME);
        assertThat(testWorkingHour.getDate()).isEqualTo(DEFAULT_DATE);
        assertThat(testWorkingHour.getTimeStart()).isEqualTo(DEFAULT_TIME_START);
        assertThat(testWorkingHour.getTimeEnd()).isEqualTo(DEFAULT_TIME_END);
        assertThat(testWorkingHour.getCreated()).isEqualTo(DEFAULT_CREATED);
    }

    @Test
    @Transactional
    void createWorkingHourWithExistingId() throws Exception {
        // Create the WorkingHour with an existing ID
        workingHour.setId(1L);
        WorkingHourDTO workingHourDTO = workingHourMapper.toDto(workingHour);

        int databaseSizeBeforeCreate = workingHourRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restWorkingHourMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(workingHourDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the WorkingHour in the database
        List<WorkingHour> workingHourList = workingHourRepository.findAll();
        assertThat(workingHourList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllWorkingHours() throws Exception {
        // Initialize the database
        workingHourRepository.saveAndFlush(workingHour);

        // Get all the workingHourList
        restWorkingHourMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(workingHour.getId().intValue())))
            .andExpect(jsonPath("$.[*].remoteId").value(hasItem(DEFAULT_REMOTE_ID)))
            .andExpect(jsonPath("$.[*].userName").value(hasItem(DEFAULT_USER_NAME)))
            .andExpect(jsonPath("$.[*].date").value(hasItem(DEFAULT_DATE.toString())))
            .andExpect(jsonPath("$.[*].timeStart").value(hasItem(sameInstant(DEFAULT_TIME_START))))
            .andExpect(jsonPath("$.[*].timeEnd").value(hasItem(sameInstant(DEFAULT_TIME_END))))
            .andExpect(jsonPath("$.[*].created").value(hasItem(sameInstant(DEFAULT_CREATED))));
    }

    @Test
    @Transactional
    void getWorkingHour() throws Exception {
        // Initialize the database
        workingHourRepository.saveAndFlush(workingHour);

        // Get the workingHour
        restWorkingHourMockMvc
            .perform(get(ENTITY_API_URL_ID, workingHour.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(workingHour.getId().intValue()))
            .andExpect(jsonPath("$.remoteId").value(DEFAULT_REMOTE_ID))
            .andExpect(jsonPath("$.userName").value(DEFAULT_USER_NAME))
            .andExpect(jsonPath("$.date").value(DEFAULT_DATE.toString()))
            .andExpect(jsonPath("$.timeStart").value(sameInstant(DEFAULT_TIME_START)))
            .andExpect(jsonPath("$.timeEnd").value(sameInstant(DEFAULT_TIME_END)))
            .andExpect(jsonPath("$.created").value(sameInstant(DEFAULT_CREATED)));
    }

    @Test
    @Transactional
    void getNonExistingWorkingHour() throws Exception {
        // Get the workingHour
        restWorkingHourMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewWorkingHour() throws Exception {
        // Initialize the database
        workingHourRepository.saveAndFlush(workingHour);

        int databaseSizeBeforeUpdate = workingHourRepository.findAll().size();

        // Update the workingHour
        WorkingHour updatedWorkingHour = workingHourRepository.findById(workingHour.getId()).get();
        // Disconnect from session so that the updates on updatedWorkingHour are not directly saved in db
        em.detach(updatedWorkingHour);
        updatedWorkingHour
            .remoteId(UPDATED_REMOTE_ID)
            .userName(UPDATED_USER_NAME)
            .date(UPDATED_DATE)
            .timeStart(UPDATED_TIME_START)
            .timeEnd(UPDATED_TIME_END)
            .created(UPDATED_CREATED);
        WorkingHourDTO workingHourDTO = workingHourMapper.toDto(updatedWorkingHour);

        restWorkingHourMockMvc
            .perform(
                put(ENTITY_API_URL_ID, workingHourDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(workingHourDTO))
            )
            .andExpect(status().isOk());

        // Validate the WorkingHour in the database
        List<WorkingHour> workingHourList = workingHourRepository.findAll();
        assertThat(workingHourList).hasSize(databaseSizeBeforeUpdate);
        WorkingHour testWorkingHour = workingHourList.get(workingHourList.size() - 1);
        assertThat(testWorkingHour.getRemoteId()).isEqualTo(UPDATED_REMOTE_ID);
        assertThat(testWorkingHour.getUserName()).isEqualTo(UPDATED_USER_NAME);
        assertThat(testWorkingHour.getDate()).isEqualTo(UPDATED_DATE);
        assertThat(testWorkingHour.getTimeStart()).isEqualTo(UPDATED_TIME_START);
        assertThat(testWorkingHour.getTimeEnd()).isEqualTo(UPDATED_TIME_END);
        assertThat(testWorkingHour.getCreated()).isEqualTo(UPDATED_CREATED);
    }

    @Test
    @Transactional
    void putNonExistingWorkingHour() throws Exception {
        int databaseSizeBeforeUpdate = workingHourRepository.findAll().size();
        workingHour.setId(count.incrementAndGet());

        // Create the WorkingHour
        WorkingHourDTO workingHourDTO = workingHourMapper.toDto(workingHour);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restWorkingHourMockMvc
            .perform(
                put(ENTITY_API_URL_ID, workingHourDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(workingHourDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the WorkingHour in the database
        List<WorkingHour> workingHourList = workingHourRepository.findAll();
        assertThat(workingHourList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchWorkingHour() throws Exception {
        int databaseSizeBeforeUpdate = workingHourRepository.findAll().size();
        workingHour.setId(count.incrementAndGet());

        // Create the WorkingHour
        WorkingHourDTO workingHourDTO = workingHourMapper.toDto(workingHour);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restWorkingHourMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(workingHourDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the WorkingHour in the database
        List<WorkingHour> workingHourList = workingHourRepository.findAll();
        assertThat(workingHourList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamWorkingHour() throws Exception {
        int databaseSizeBeforeUpdate = workingHourRepository.findAll().size();
        workingHour.setId(count.incrementAndGet());

        // Create the WorkingHour
        WorkingHourDTO workingHourDTO = workingHourMapper.toDto(workingHour);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restWorkingHourMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(workingHourDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the WorkingHour in the database
        List<WorkingHour> workingHourList = workingHourRepository.findAll();
        assertThat(workingHourList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateWorkingHourWithPatch() throws Exception {
        // Initialize the database
        workingHourRepository.saveAndFlush(workingHour);

        int databaseSizeBeforeUpdate = workingHourRepository.findAll().size();

        // Update the workingHour using partial update
        WorkingHour partialUpdatedWorkingHour = new WorkingHour();
        partialUpdatedWorkingHour.setId(workingHour.getId());

        partialUpdatedWorkingHour.userName(UPDATED_USER_NAME).date(UPDATED_DATE).timeEnd(UPDATED_TIME_END).created(UPDATED_CREATED);

        restWorkingHourMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedWorkingHour.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedWorkingHour))
            )
            .andExpect(status().isOk());

        // Validate the WorkingHour in the database
        List<WorkingHour> workingHourList = workingHourRepository.findAll();
        assertThat(workingHourList).hasSize(databaseSizeBeforeUpdate);
        WorkingHour testWorkingHour = workingHourList.get(workingHourList.size() - 1);
        assertThat(testWorkingHour.getRemoteId()).isEqualTo(DEFAULT_REMOTE_ID);
        assertThat(testWorkingHour.getUserName()).isEqualTo(UPDATED_USER_NAME);
        assertThat(testWorkingHour.getDate()).isEqualTo(UPDATED_DATE);
        assertThat(testWorkingHour.getTimeStart()).isEqualTo(DEFAULT_TIME_START);
        assertThat(testWorkingHour.getTimeEnd()).isEqualTo(UPDATED_TIME_END);
        assertThat(testWorkingHour.getCreated()).isEqualTo(UPDATED_CREATED);
    }

    @Test
    @Transactional
    void fullUpdateWorkingHourWithPatch() throws Exception {
        // Initialize the database
        workingHourRepository.saveAndFlush(workingHour);

        int databaseSizeBeforeUpdate = workingHourRepository.findAll().size();

        // Update the workingHour using partial update
        WorkingHour partialUpdatedWorkingHour = new WorkingHour();
        partialUpdatedWorkingHour.setId(workingHour.getId());

        partialUpdatedWorkingHour
            .remoteId(UPDATED_REMOTE_ID)
            .userName(UPDATED_USER_NAME)
            .date(UPDATED_DATE)
            .timeStart(UPDATED_TIME_START)
            .timeEnd(UPDATED_TIME_END)
            .created(UPDATED_CREATED);

        restWorkingHourMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedWorkingHour.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedWorkingHour))
            )
            .andExpect(status().isOk());

        // Validate the WorkingHour in the database
        List<WorkingHour> workingHourList = workingHourRepository.findAll();
        assertThat(workingHourList).hasSize(databaseSizeBeforeUpdate);
        WorkingHour testWorkingHour = workingHourList.get(workingHourList.size() - 1);
        assertThat(testWorkingHour.getRemoteId()).isEqualTo(UPDATED_REMOTE_ID);
        assertThat(testWorkingHour.getUserName()).isEqualTo(UPDATED_USER_NAME);
        assertThat(testWorkingHour.getDate()).isEqualTo(UPDATED_DATE);
        assertThat(testWorkingHour.getTimeStart()).isEqualTo(UPDATED_TIME_START);
        assertThat(testWorkingHour.getTimeEnd()).isEqualTo(UPDATED_TIME_END);
        assertThat(testWorkingHour.getCreated()).isEqualTo(UPDATED_CREATED);
    }

    @Test
    @Transactional
    void patchNonExistingWorkingHour() throws Exception {
        int databaseSizeBeforeUpdate = workingHourRepository.findAll().size();
        workingHour.setId(count.incrementAndGet());

        // Create the WorkingHour
        WorkingHourDTO workingHourDTO = workingHourMapper.toDto(workingHour);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restWorkingHourMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, workingHourDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(workingHourDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the WorkingHour in the database
        List<WorkingHour> workingHourList = workingHourRepository.findAll();
        assertThat(workingHourList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchWorkingHour() throws Exception {
        int databaseSizeBeforeUpdate = workingHourRepository.findAll().size();
        workingHour.setId(count.incrementAndGet());

        // Create the WorkingHour
        WorkingHourDTO workingHourDTO = workingHourMapper.toDto(workingHour);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restWorkingHourMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(workingHourDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the WorkingHour in the database
        List<WorkingHour> workingHourList = workingHourRepository.findAll();
        assertThat(workingHourList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamWorkingHour() throws Exception {
        int databaseSizeBeforeUpdate = workingHourRepository.findAll().size();
        workingHour.setId(count.incrementAndGet());

        // Create the WorkingHour
        WorkingHourDTO workingHourDTO = workingHourMapper.toDto(workingHour);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restWorkingHourMockMvc
            .perform(
                patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(workingHourDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the WorkingHour in the database
        List<WorkingHour> workingHourList = workingHourRepository.findAll();
        assertThat(workingHourList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteWorkingHour() throws Exception {
        // Initialize the database
        workingHourRepository.saveAndFlush(workingHour);

        int databaseSizeBeforeDelete = workingHourRepository.findAll().size();

        // Delete the workingHour
        restWorkingHourMockMvc
            .perform(delete(ENTITY_API_URL_ID, workingHour.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<WorkingHour> workingHourList = workingHourRepository.findAll();
        assertThat(workingHourList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
