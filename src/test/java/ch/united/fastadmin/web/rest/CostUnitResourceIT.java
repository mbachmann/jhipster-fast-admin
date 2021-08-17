package ch.united.fastadmin.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import ch.united.fastadmin.IntegrationTest;
import ch.united.fastadmin.domain.CostUnit;
import ch.united.fastadmin.domain.enumeration.CostUnitType;
import ch.united.fastadmin.repository.CostUnitRepository;
import ch.united.fastadmin.service.dto.CostUnitDTO;
import ch.united.fastadmin.service.mapper.CostUnitMapper;
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
 * Integration tests for the {@link CostUnitResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class CostUnitResourceIT {

    private static final Integer DEFAULT_REMOTE_ID = 1;
    private static final Integer UPDATED_REMOTE_ID = 2;

    private static final String DEFAULT_NUMBER = "AAAAAAAAAA";
    private static final String UPDATED_NUMBER = "BBBBBBBBBB";

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_DESCRIPTION = "AAAAAAAAAA";
    private static final String UPDATED_DESCRIPTION = "BBBBBBBBBB";

    private static final CostUnitType DEFAULT_TYPE = CostUnitType.PRODUCTIVE;
    private static final CostUnitType UPDATED_TYPE = CostUnitType.NOT_PRODUCTIVE;

    private static final Boolean DEFAULT_INACTIV = false;
    private static final Boolean UPDATED_INACTIV = true;

    private static final String ENTITY_API_URL = "/api/cost-units";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private CostUnitRepository costUnitRepository;

    @Autowired
    private CostUnitMapper costUnitMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restCostUnitMockMvc;

    private CostUnit costUnit;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static CostUnit createEntity(EntityManager em) {
        CostUnit costUnit = new CostUnit()
            .remoteId(DEFAULT_REMOTE_ID)
            .number(DEFAULT_NUMBER)
            .name(DEFAULT_NAME)
            .description(DEFAULT_DESCRIPTION)
            .type(DEFAULT_TYPE)
            .inactiv(DEFAULT_INACTIV);
        return costUnit;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static CostUnit createUpdatedEntity(EntityManager em) {
        CostUnit costUnit = new CostUnit()
            .remoteId(UPDATED_REMOTE_ID)
            .number(UPDATED_NUMBER)
            .name(UPDATED_NAME)
            .description(UPDATED_DESCRIPTION)
            .type(UPDATED_TYPE)
            .inactiv(UPDATED_INACTIV);
        return costUnit;
    }

    @BeforeEach
    public void initTest() {
        costUnit = createEntity(em);
    }

    @Test
    @Transactional
    void createCostUnit() throws Exception {
        int databaseSizeBeforeCreate = costUnitRepository.findAll().size();
        // Create the CostUnit
        CostUnitDTO costUnitDTO = costUnitMapper.toDto(costUnit);
        restCostUnitMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(costUnitDTO)))
            .andExpect(status().isCreated());

        // Validate the CostUnit in the database
        List<CostUnit> costUnitList = costUnitRepository.findAll();
        assertThat(costUnitList).hasSize(databaseSizeBeforeCreate + 1);
        CostUnit testCostUnit = costUnitList.get(costUnitList.size() - 1);
        assertThat(testCostUnit.getRemoteId()).isEqualTo(DEFAULT_REMOTE_ID);
        assertThat(testCostUnit.getNumber()).isEqualTo(DEFAULT_NUMBER);
        assertThat(testCostUnit.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testCostUnit.getDescription()).isEqualTo(DEFAULT_DESCRIPTION);
        assertThat(testCostUnit.getType()).isEqualTo(DEFAULT_TYPE);
        assertThat(testCostUnit.getInactiv()).isEqualTo(DEFAULT_INACTIV);
    }

    @Test
    @Transactional
    void createCostUnitWithExistingId() throws Exception {
        // Create the CostUnit with an existing ID
        costUnit.setId(1L);
        CostUnitDTO costUnitDTO = costUnitMapper.toDto(costUnit);

        int databaseSizeBeforeCreate = costUnitRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restCostUnitMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(costUnitDTO)))
            .andExpect(status().isBadRequest());

        // Validate the CostUnit in the database
        List<CostUnit> costUnitList = costUnitRepository.findAll();
        assertThat(costUnitList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllCostUnits() throws Exception {
        // Initialize the database
        costUnitRepository.saveAndFlush(costUnit);

        // Get all the costUnitList
        restCostUnitMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(costUnit.getId().intValue())))
            .andExpect(jsonPath("$.[*].remoteId").value(hasItem(DEFAULT_REMOTE_ID)))
            .andExpect(jsonPath("$.[*].number").value(hasItem(DEFAULT_NUMBER)))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION)))
            .andExpect(jsonPath("$.[*].type").value(hasItem(DEFAULT_TYPE.toString())))
            .andExpect(jsonPath("$.[*].inactiv").value(hasItem(DEFAULT_INACTIV.booleanValue())));
    }

    @Test
    @Transactional
    void getCostUnit() throws Exception {
        // Initialize the database
        costUnitRepository.saveAndFlush(costUnit);

        // Get the costUnit
        restCostUnitMockMvc
            .perform(get(ENTITY_API_URL_ID, costUnit.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(costUnit.getId().intValue()))
            .andExpect(jsonPath("$.remoteId").value(DEFAULT_REMOTE_ID))
            .andExpect(jsonPath("$.number").value(DEFAULT_NUMBER))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME))
            .andExpect(jsonPath("$.description").value(DEFAULT_DESCRIPTION))
            .andExpect(jsonPath("$.type").value(DEFAULT_TYPE.toString()))
            .andExpect(jsonPath("$.inactiv").value(DEFAULT_INACTIV.booleanValue()));
    }

    @Test
    @Transactional
    void getNonExistingCostUnit() throws Exception {
        // Get the costUnit
        restCostUnitMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewCostUnit() throws Exception {
        // Initialize the database
        costUnitRepository.saveAndFlush(costUnit);

        int databaseSizeBeforeUpdate = costUnitRepository.findAll().size();

        // Update the costUnit
        CostUnit updatedCostUnit = costUnitRepository.findById(costUnit.getId()).get();
        // Disconnect from session so that the updates on updatedCostUnit are not directly saved in db
        em.detach(updatedCostUnit);
        updatedCostUnit
            .remoteId(UPDATED_REMOTE_ID)
            .number(UPDATED_NUMBER)
            .name(UPDATED_NAME)
            .description(UPDATED_DESCRIPTION)
            .type(UPDATED_TYPE)
            .inactiv(UPDATED_INACTIV);
        CostUnitDTO costUnitDTO = costUnitMapper.toDto(updatedCostUnit);

        restCostUnitMockMvc
            .perform(
                put(ENTITY_API_URL_ID, costUnitDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(costUnitDTO))
            )
            .andExpect(status().isOk());

        // Validate the CostUnit in the database
        List<CostUnit> costUnitList = costUnitRepository.findAll();
        assertThat(costUnitList).hasSize(databaseSizeBeforeUpdate);
        CostUnit testCostUnit = costUnitList.get(costUnitList.size() - 1);
        assertThat(testCostUnit.getRemoteId()).isEqualTo(UPDATED_REMOTE_ID);
        assertThat(testCostUnit.getNumber()).isEqualTo(UPDATED_NUMBER);
        assertThat(testCostUnit.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testCostUnit.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
        assertThat(testCostUnit.getType()).isEqualTo(UPDATED_TYPE);
        assertThat(testCostUnit.getInactiv()).isEqualTo(UPDATED_INACTIV);
    }

    @Test
    @Transactional
    void putNonExistingCostUnit() throws Exception {
        int databaseSizeBeforeUpdate = costUnitRepository.findAll().size();
        costUnit.setId(count.incrementAndGet());

        // Create the CostUnit
        CostUnitDTO costUnitDTO = costUnitMapper.toDto(costUnit);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restCostUnitMockMvc
            .perform(
                put(ENTITY_API_URL_ID, costUnitDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(costUnitDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the CostUnit in the database
        List<CostUnit> costUnitList = costUnitRepository.findAll();
        assertThat(costUnitList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchCostUnit() throws Exception {
        int databaseSizeBeforeUpdate = costUnitRepository.findAll().size();
        costUnit.setId(count.incrementAndGet());

        // Create the CostUnit
        CostUnitDTO costUnitDTO = costUnitMapper.toDto(costUnit);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCostUnitMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(costUnitDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the CostUnit in the database
        List<CostUnit> costUnitList = costUnitRepository.findAll();
        assertThat(costUnitList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamCostUnit() throws Exception {
        int databaseSizeBeforeUpdate = costUnitRepository.findAll().size();
        costUnit.setId(count.incrementAndGet());

        // Create the CostUnit
        CostUnitDTO costUnitDTO = costUnitMapper.toDto(costUnit);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCostUnitMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(costUnitDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the CostUnit in the database
        List<CostUnit> costUnitList = costUnitRepository.findAll();
        assertThat(costUnitList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateCostUnitWithPatch() throws Exception {
        // Initialize the database
        costUnitRepository.saveAndFlush(costUnit);

        int databaseSizeBeforeUpdate = costUnitRepository.findAll().size();

        // Update the costUnit using partial update
        CostUnit partialUpdatedCostUnit = new CostUnit();
        partialUpdatedCostUnit.setId(costUnit.getId());

        partialUpdatedCostUnit.remoteId(UPDATED_REMOTE_ID).name(UPDATED_NAME);

        restCostUnitMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedCostUnit.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedCostUnit))
            )
            .andExpect(status().isOk());

        // Validate the CostUnit in the database
        List<CostUnit> costUnitList = costUnitRepository.findAll();
        assertThat(costUnitList).hasSize(databaseSizeBeforeUpdate);
        CostUnit testCostUnit = costUnitList.get(costUnitList.size() - 1);
        assertThat(testCostUnit.getRemoteId()).isEqualTo(UPDATED_REMOTE_ID);
        assertThat(testCostUnit.getNumber()).isEqualTo(DEFAULT_NUMBER);
        assertThat(testCostUnit.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testCostUnit.getDescription()).isEqualTo(DEFAULT_DESCRIPTION);
        assertThat(testCostUnit.getType()).isEqualTo(DEFAULT_TYPE);
        assertThat(testCostUnit.getInactiv()).isEqualTo(DEFAULT_INACTIV);
    }

    @Test
    @Transactional
    void fullUpdateCostUnitWithPatch() throws Exception {
        // Initialize the database
        costUnitRepository.saveAndFlush(costUnit);

        int databaseSizeBeforeUpdate = costUnitRepository.findAll().size();

        // Update the costUnit using partial update
        CostUnit partialUpdatedCostUnit = new CostUnit();
        partialUpdatedCostUnit.setId(costUnit.getId());

        partialUpdatedCostUnit
            .remoteId(UPDATED_REMOTE_ID)
            .number(UPDATED_NUMBER)
            .name(UPDATED_NAME)
            .description(UPDATED_DESCRIPTION)
            .type(UPDATED_TYPE)
            .inactiv(UPDATED_INACTIV);

        restCostUnitMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedCostUnit.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedCostUnit))
            )
            .andExpect(status().isOk());

        // Validate the CostUnit in the database
        List<CostUnit> costUnitList = costUnitRepository.findAll();
        assertThat(costUnitList).hasSize(databaseSizeBeforeUpdate);
        CostUnit testCostUnit = costUnitList.get(costUnitList.size() - 1);
        assertThat(testCostUnit.getRemoteId()).isEqualTo(UPDATED_REMOTE_ID);
        assertThat(testCostUnit.getNumber()).isEqualTo(UPDATED_NUMBER);
        assertThat(testCostUnit.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testCostUnit.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
        assertThat(testCostUnit.getType()).isEqualTo(UPDATED_TYPE);
        assertThat(testCostUnit.getInactiv()).isEqualTo(UPDATED_INACTIV);
    }

    @Test
    @Transactional
    void patchNonExistingCostUnit() throws Exception {
        int databaseSizeBeforeUpdate = costUnitRepository.findAll().size();
        costUnit.setId(count.incrementAndGet());

        // Create the CostUnit
        CostUnitDTO costUnitDTO = costUnitMapper.toDto(costUnit);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restCostUnitMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, costUnitDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(costUnitDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the CostUnit in the database
        List<CostUnit> costUnitList = costUnitRepository.findAll();
        assertThat(costUnitList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchCostUnit() throws Exception {
        int databaseSizeBeforeUpdate = costUnitRepository.findAll().size();
        costUnit.setId(count.incrementAndGet());

        // Create the CostUnit
        CostUnitDTO costUnitDTO = costUnitMapper.toDto(costUnit);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCostUnitMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(costUnitDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the CostUnit in the database
        List<CostUnit> costUnitList = costUnitRepository.findAll();
        assertThat(costUnitList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamCostUnit() throws Exception {
        int databaseSizeBeforeUpdate = costUnitRepository.findAll().size();
        costUnit.setId(count.incrementAndGet());

        // Create the CostUnit
        CostUnitDTO costUnitDTO = costUnitMapper.toDto(costUnit);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCostUnitMockMvc
            .perform(
                patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(costUnitDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the CostUnit in the database
        List<CostUnit> costUnitList = costUnitRepository.findAll();
        assertThat(costUnitList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteCostUnit() throws Exception {
        // Initialize the database
        costUnitRepository.saveAndFlush(costUnit);

        int databaseSizeBeforeDelete = costUnitRepository.findAll().size();

        // Delete the costUnit
        restCostUnitMockMvc
            .perform(delete(ENTITY_API_URL_ID, costUnit.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<CostUnit> costUnitList = costUnitRepository.findAll();
        assertThat(costUnitList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
