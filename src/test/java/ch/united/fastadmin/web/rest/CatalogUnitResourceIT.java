package ch.united.fastadmin.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import ch.united.fastadmin.IntegrationTest;
import ch.united.fastadmin.domain.CatalogUnit;
import ch.united.fastadmin.domain.enumeration.CatalogScope;
import ch.united.fastadmin.repository.CatalogUnitRepository;
import ch.united.fastadmin.service.dto.CatalogUnitDTO;
import ch.united.fastadmin.service.mapper.CatalogUnitMapper;
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
 * Integration tests for the {@link CatalogUnitResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class CatalogUnitResourceIT {

    private static final Integer DEFAULT_REMOTE_ID = 1;
    private static final Integer UPDATED_REMOTE_ID = 2;

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_NAME_DE = "AAAAAAAAAA";
    private static final String UPDATED_NAME_DE = "BBBBBBBBBB";

    private static final String DEFAULT_NAME_EN = "AAAAAAAAAA";
    private static final String UPDATED_NAME_EN = "BBBBBBBBBB";

    private static final String DEFAULT_NAME_FR = "AAAAAAAAAA";
    private static final String UPDATED_NAME_FR = "BBBBBBBBBB";

    private static final String DEFAULT_NAME_IT = "AAAAAAAAAA";
    private static final String UPDATED_NAME_IT = "BBBBBBBBBB";

    private static final CatalogScope DEFAULT_SCOPE = CatalogScope.SERVICE;
    private static final CatalogScope UPDATED_SCOPE = CatalogScope.PRODUCT;

    private static final Boolean DEFAULT_CUSTOM = false;
    private static final Boolean UPDATED_CUSTOM = true;

    private static final Boolean DEFAULT_INACTIV = false;
    private static final Boolean UPDATED_INACTIV = true;

    private static final String ENTITY_API_URL = "/api/catalog-units";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private CatalogUnitRepository catalogUnitRepository;

    @Autowired
    private CatalogUnitMapper catalogUnitMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restCatalogUnitMockMvc;

    private CatalogUnit catalogUnit;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static CatalogUnit createEntity(EntityManager em) {
        CatalogUnit catalogUnit = new CatalogUnit()
            .remoteId(DEFAULT_REMOTE_ID)
            .name(DEFAULT_NAME)
            .nameDe(DEFAULT_NAME_DE)
            .nameEn(DEFAULT_NAME_EN)
            .nameFr(DEFAULT_NAME_FR)
            .nameIt(DEFAULT_NAME_IT)
            .scope(DEFAULT_SCOPE)
            .custom(DEFAULT_CUSTOM)
            .inactiv(DEFAULT_INACTIV);
        return catalogUnit;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static CatalogUnit createUpdatedEntity(EntityManager em) {
        CatalogUnit catalogUnit = new CatalogUnit()
            .remoteId(UPDATED_REMOTE_ID)
            .name(UPDATED_NAME)
            .nameDe(UPDATED_NAME_DE)
            .nameEn(UPDATED_NAME_EN)
            .nameFr(UPDATED_NAME_FR)
            .nameIt(UPDATED_NAME_IT)
            .scope(UPDATED_SCOPE)
            .custom(UPDATED_CUSTOM)
            .inactiv(UPDATED_INACTIV);
        return catalogUnit;
    }

    @BeforeEach
    public void initTest() {
        catalogUnit = createEntity(em);
    }

    @Test
    @Transactional
    void createCatalogUnit() throws Exception {
        int databaseSizeBeforeCreate = catalogUnitRepository.findAll().size();
        // Create the CatalogUnit
        CatalogUnitDTO catalogUnitDTO = catalogUnitMapper.toDto(catalogUnit);
        restCatalogUnitMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(catalogUnitDTO))
            )
            .andExpect(status().isCreated());

        // Validate the CatalogUnit in the database
        List<CatalogUnit> catalogUnitList = catalogUnitRepository.findAll();
        assertThat(catalogUnitList).hasSize(databaseSizeBeforeCreate + 1);
        CatalogUnit testCatalogUnit = catalogUnitList.get(catalogUnitList.size() - 1);
        assertThat(testCatalogUnit.getRemoteId()).isEqualTo(DEFAULT_REMOTE_ID);
        assertThat(testCatalogUnit.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testCatalogUnit.getNameDe()).isEqualTo(DEFAULT_NAME_DE);
        assertThat(testCatalogUnit.getNameEn()).isEqualTo(DEFAULT_NAME_EN);
        assertThat(testCatalogUnit.getNameFr()).isEqualTo(DEFAULT_NAME_FR);
        assertThat(testCatalogUnit.getNameIt()).isEqualTo(DEFAULT_NAME_IT);
        assertThat(testCatalogUnit.getScope()).isEqualTo(DEFAULT_SCOPE);
        assertThat(testCatalogUnit.getCustom()).isEqualTo(DEFAULT_CUSTOM);
        assertThat(testCatalogUnit.getInactiv()).isEqualTo(DEFAULT_INACTIV);
    }

    @Test
    @Transactional
    void createCatalogUnitWithExistingId() throws Exception {
        // Create the CatalogUnit with an existing ID
        catalogUnit.setId(1L);
        CatalogUnitDTO catalogUnitDTO = catalogUnitMapper.toDto(catalogUnit);

        int databaseSizeBeforeCreate = catalogUnitRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restCatalogUnitMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(catalogUnitDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the CatalogUnit in the database
        List<CatalogUnit> catalogUnitList = catalogUnitRepository.findAll();
        assertThat(catalogUnitList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkNameIsRequired() throws Exception {
        int databaseSizeBeforeTest = catalogUnitRepository.findAll().size();
        // set the field null
        catalogUnit.setName(null);

        // Create the CatalogUnit, which fails.
        CatalogUnitDTO catalogUnitDTO = catalogUnitMapper.toDto(catalogUnit);

        restCatalogUnitMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(catalogUnitDTO))
            )
            .andExpect(status().isBadRequest());

        List<CatalogUnit> catalogUnitList = catalogUnitRepository.findAll();
        assertThat(catalogUnitList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllCatalogUnits() throws Exception {
        // Initialize the database
        catalogUnitRepository.saveAndFlush(catalogUnit);

        // Get all the catalogUnitList
        restCatalogUnitMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(catalogUnit.getId().intValue())))
            .andExpect(jsonPath("$.[*].remoteId").value(hasItem(DEFAULT_REMOTE_ID)))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)))
            .andExpect(jsonPath("$.[*].nameDe").value(hasItem(DEFAULT_NAME_DE)))
            .andExpect(jsonPath("$.[*].nameEn").value(hasItem(DEFAULT_NAME_EN)))
            .andExpect(jsonPath("$.[*].nameFr").value(hasItem(DEFAULT_NAME_FR)))
            .andExpect(jsonPath("$.[*].nameIt").value(hasItem(DEFAULT_NAME_IT)))
            .andExpect(jsonPath("$.[*].scope").value(hasItem(DEFAULT_SCOPE.toString())))
            .andExpect(jsonPath("$.[*].custom").value(hasItem(DEFAULT_CUSTOM.booleanValue())))
            .andExpect(jsonPath("$.[*].inactiv").value(hasItem(DEFAULT_INACTIV.booleanValue())));
    }

    @Test
    @Transactional
    void getCatalogUnit() throws Exception {
        // Initialize the database
        catalogUnitRepository.saveAndFlush(catalogUnit);

        // Get the catalogUnit
        restCatalogUnitMockMvc
            .perform(get(ENTITY_API_URL_ID, catalogUnit.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(catalogUnit.getId().intValue()))
            .andExpect(jsonPath("$.remoteId").value(DEFAULT_REMOTE_ID))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME))
            .andExpect(jsonPath("$.nameDe").value(DEFAULT_NAME_DE))
            .andExpect(jsonPath("$.nameEn").value(DEFAULT_NAME_EN))
            .andExpect(jsonPath("$.nameFr").value(DEFAULT_NAME_FR))
            .andExpect(jsonPath("$.nameIt").value(DEFAULT_NAME_IT))
            .andExpect(jsonPath("$.scope").value(DEFAULT_SCOPE.toString()))
            .andExpect(jsonPath("$.custom").value(DEFAULT_CUSTOM.booleanValue()))
            .andExpect(jsonPath("$.inactiv").value(DEFAULT_INACTIV.booleanValue()));
    }

    @Test
    @Transactional
    void getNonExistingCatalogUnit() throws Exception {
        // Get the catalogUnit
        restCatalogUnitMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewCatalogUnit() throws Exception {
        // Initialize the database
        catalogUnitRepository.saveAndFlush(catalogUnit);

        int databaseSizeBeforeUpdate = catalogUnitRepository.findAll().size();

        // Update the catalogUnit
        CatalogUnit updatedCatalogUnit = catalogUnitRepository.findById(catalogUnit.getId()).get();
        // Disconnect from session so that the updates on updatedCatalogUnit are not directly saved in db
        em.detach(updatedCatalogUnit);
        updatedCatalogUnit
            .remoteId(UPDATED_REMOTE_ID)
            .name(UPDATED_NAME)
            .nameDe(UPDATED_NAME_DE)
            .nameEn(UPDATED_NAME_EN)
            .nameFr(UPDATED_NAME_FR)
            .nameIt(UPDATED_NAME_IT)
            .scope(UPDATED_SCOPE)
            .custom(UPDATED_CUSTOM)
            .inactiv(UPDATED_INACTIV);
        CatalogUnitDTO catalogUnitDTO = catalogUnitMapper.toDto(updatedCatalogUnit);

        restCatalogUnitMockMvc
            .perform(
                put(ENTITY_API_URL_ID, catalogUnitDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(catalogUnitDTO))
            )
            .andExpect(status().isOk());

        // Validate the CatalogUnit in the database
        List<CatalogUnit> catalogUnitList = catalogUnitRepository.findAll();
        assertThat(catalogUnitList).hasSize(databaseSizeBeforeUpdate);
        CatalogUnit testCatalogUnit = catalogUnitList.get(catalogUnitList.size() - 1);
        assertThat(testCatalogUnit.getRemoteId()).isEqualTo(UPDATED_REMOTE_ID);
        assertThat(testCatalogUnit.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testCatalogUnit.getNameDe()).isEqualTo(UPDATED_NAME_DE);
        assertThat(testCatalogUnit.getNameEn()).isEqualTo(UPDATED_NAME_EN);
        assertThat(testCatalogUnit.getNameFr()).isEqualTo(UPDATED_NAME_FR);
        assertThat(testCatalogUnit.getNameIt()).isEqualTo(UPDATED_NAME_IT);
        assertThat(testCatalogUnit.getScope()).isEqualTo(UPDATED_SCOPE);
        assertThat(testCatalogUnit.getCustom()).isEqualTo(UPDATED_CUSTOM);
        assertThat(testCatalogUnit.getInactiv()).isEqualTo(UPDATED_INACTIV);
    }

    @Test
    @Transactional
    void putNonExistingCatalogUnit() throws Exception {
        int databaseSizeBeforeUpdate = catalogUnitRepository.findAll().size();
        catalogUnit.setId(count.incrementAndGet());

        // Create the CatalogUnit
        CatalogUnitDTO catalogUnitDTO = catalogUnitMapper.toDto(catalogUnit);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restCatalogUnitMockMvc
            .perform(
                put(ENTITY_API_URL_ID, catalogUnitDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(catalogUnitDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the CatalogUnit in the database
        List<CatalogUnit> catalogUnitList = catalogUnitRepository.findAll();
        assertThat(catalogUnitList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchCatalogUnit() throws Exception {
        int databaseSizeBeforeUpdate = catalogUnitRepository.findAll().size();
        catalogUnit.setId(count.incrementAndGet());

        // Create the CatalogUnit
        CatalogUnitDTO catalogUnitDTO = catalogUnitMapper.toDto(catalogUnit);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCatalogUnitMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(catalogUnitDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the CatalogUnit in the database
        List<CatalogUnit> catalogUnitList = catalogUnitRepository.findAll();
        assertThat(catalogUnitList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamCatalogUnit() throws Exception {
        int databaseSizeBeforeUpdate = catalogUnitRepository.findAll().size();
        catalogUnit.setId(count.incrementAndGet());

        // Create the CatalogUnit
        CatalogUnitDTO catalogUnitDTO = catalogUnitMapper.toDto(catalogUnit);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCatalogUnitMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(catalogUnitDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the CatalogUnit in the database
        List<CatalogUnit> catalogUnitList = catalogUnitRepository.findAll();
        assertThat(catalogUnitList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateCatalogUnitWithPatch() throws Exception {
        // Initialize the database
        catalogUnitRepository.saveAndFlush(catalogUnit);

        int databaseSizeBeforeUpdate = catalogUnitRepository.findAll().size();

        // Update the catalogUnit using partial update
        CatalogUnit partialUpdatedCatalogUnit = new CatalogUnit();
        partialUpdatedCatalogUnit.setId(catalogUnit.getId());

        partialUpdatedCatalogUnit
            .remoteId(UPDATED_REMOTE_ID)
            .nameDe(UPDATED_NAME_DE)
            .nameIt(UPDATED_NAME_IT)
            .custom(UPDATED_CUSTOM)
            .inactiv(UPDATED_INACTIV);

        restCatalogUnitMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedCatalogUnit.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedCatalogUnit))
            )
            .andExpect(status().isOk());

        // Validate the CatalogUnit in the database
        List<CatalogUnit> catalogUnitList = catalogUnitRepository.findAll();
        assertThat(catalogUnitList).hasSize(databaseSizeBeforeUpdate);
        CatalogUnit testCatalogUnit = catalogUnitList.get(catalogUnitList.size() - 1);
        assertThat(testCatalogUnit.getRemoteId()).isEqualTo(UPDATED_REMOTE_ID);
        assertThat(testCatalogUnit.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testCatalogUnit.getNameDe()).isEqualTo(UPDATED_NAME_DE);
        assertThat(testCatalogUnit.getNameEn()).isEqualTo(DEFAULT_NAME_EN);
        assertThat(testCatalogUnit.getNameFr()).isEqualTo(DEFAULT_NAME_FR);
        assertThat(testCatalogUnit.getNameIt()).isEqualTo(UPDATED_NAME_IT);
        assertThat(testCatalogUnit.getScope()).isEqualTo(DEFAULT_SCOPE);
        assertThat(testCatalogUnit.getCustom()).isEqualTo(UPDATED_CUSTOM);
        assertThat(testCatalogUnit.getInactiv()).isEqualTo(UPDATED_INACTIV);
    }

    @Test
    @Transactional
    void fullUpdateCatalogUnitWithPatch() throws Exception {
        // Initialize the database
        catalogUnitRepository.saveAndFlush(catalogUnit);

        int databaseSizeBeforeUpdate = catalogUnitRepository.findAll().size();

        // Update the catalogUnit using partial update
        CatalogUnit partialUpdatedCatalogUnit = new CatalogUnit();
        partialUpdatedCatalogUnit.setId(catalogUnit.getId());

        partialUpdatedCatalogUnit
            .remoteId(UPDATED_REMOTE_ID)
            .name(UPDATED_NAME)
            .nameDe(UPDATED_NAME_DE)
            .nameEn(UPDATED_NAME_EN)
            .nameFr(UPDATED_NAME_FR)
            .nameIt(UPDATED_NAME_IT)
            .scope(UPDATED_SCOPE)
            .custom(UPDATED_CUSTOM)
            .inactiv(UPDATED_INACTIV);

        restCatalogUnitMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedCatalogUnit.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedCatalogUnit))
            )
            .andExpect(status().isOk());

        // Validate the CatalogUnit in the database
        List<CatalogUnit> catalogUnitList = catalogUnitRepository.findAll();
        assertThat(catalogUnitList).hasSize(databaseSizeBeforeUpdate);
        CatalogUnit testCatalogUnit = catalogUnitList.get(catalogUnitList.size() - 1);
        assertThat(testCatalogUnit.getRemoteId()).isEqualTo(UPDATED_REMOTE_ID);
        assertThat(testCatalogUnit.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testCatalogUnit.getNameDe()).isEqualTo(UPDATED_NAME_DE);
        assertThat(testCatalogUnit.getNameEn()).isEqualTo(UPDATED_NAME_EN);
        assertThat(testCatalogUnit.getNameFr()).isEqualTo(UPDATED_NAME_FR);
        assertThat(testCatalogUnit.getNameIt()).isEqualTo(UPDATED_NAME_IT);
        assertThat(testCatalogUnit.getScope()).isEqualTo(UPDATED_SCOPE);
        assertThat(testCatalogUnit.getCustom()).isEqualTo(UPDATED_CUSTOM);
        assertThat(testCatalogUnit.getInactiv()).isEqualTo(UPDATED_INACTIV);
    }

    @Test
    @Transactional
    void patchNonExistingCatalogUnit() throws Exception {
        int databaseSizeBeforeUpdate = catalogUnitRepository.findAll().size();
        catalogUnit.setId(count.incrementAndGet());

        // Create the CatalogUnit
        CatalogUnitDTO catalogUnitDTO = catalogUnitMapper.toDto(catalogUnit);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restCatalogUnitMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, catalogUnitDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(catalogUnitDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the CatalogUnit in the database
        List<CatalogUnit> catalogUnitList = catalogUnitRepository.findAll();
        assertThat(catalogUnitList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchCatalogUnit() throws Exception {
        int databaseSizeBeforeUpdate = catalogUnitRepository.findAll().size();
        catalogUnit.setId(count.incrementAndGet());

        // Create the CatalogUnit
        CatalogUnitDTO catalogUnitDTO = catalogUnitMapper.toDto(catalogUnit);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCatalogUnitMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(catalogUnitDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the CatalogUnit in the database
        List<CatalogUnit> catalogUnitList = catalogUnitRepository.findAll();
        assertThat(catalogUnitList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamCatalogUnit() throws Exception {
        int databaseSizeBeforeUpdate = catalogUnitRepository.findAll().size();
        catalogUnit.setId(count.incrementAndGet());

        // Create the CatalogUnit
        CatalogUnitDTO catalogUnitDTO = catalogUnitMapper.toDto(catalogUnit);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCatalogUnitMockMvc
            .perform(
                patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(catalogUnitDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the CatalogUnit in the database
        List<CatalogUnit> catalogUnitList = catalogUnitRepository.findAll();
        assertThat(catalogUnitList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteCatalogUnit() throws Exception {
        // Initialize the database
        catalogUnitRepository.saveAndFlush(catalogUnit);

        int databaseSizeBeforeDelete = catalogUnitRepository.findAll().size();

        // Delete the catalogUnit
        restCatalogUnitMockMvc
            .perform(delete(ENTITY_API_URL_ID, catalogUnit.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<CatalogUnit> catalogUnitList = catalogUnitRepository.findAll();
        assertThat(catalogUnitList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
