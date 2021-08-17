package ch.united.fastadmin.web.rest;

import static ch.united.fastadmin.web.rest.TestUtil.sameInstant;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import ch.united.fastadmin.IntegrationTest;
import ch.united.fastadmin.domain.CatalogService;
import ch.united.fastadmin.repository.CatalogServiceRepository;
import ch.united.fastadmin.service.dto.CatalogServiceDTO;
import ch.united.fastadmin.service.mapper.CatalogServiceMapper;
import java.time.Instant;
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
 * Integration tests for the {@link CatalogServiceResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class CatalogServiceResourceIT {

    private static final Integer DEFAULT_REMOTE_ID = 1;
    private static final Integer UPDATED_REMOTE_ID = 2;

    private static final String DEFAULT_NUMBER = "AAAAAAAAAA";
    private static final String UPDATED_NUMBER = "BBBBBBBBBB";

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_DESCRIPTION = "AAAAAAAAAA";
    private static final String UPDATED_DESCRIPTION = "BBBBBBBBBB";

    private static final String DEFAULT_NOTES = "AAAAAAAAAA";
    private static final String UPDATED_NOTES = "BBBBBBBBBB";

    private static final String DEFAULT_CATEGORY_NAME = "AAAAAAAAAA";
    private static final String UPDATED_CATEGORY_NAME = "BBBBBBBBBB";

    private static final Boolean DEFAULT_INCLUDING_VAT = false;
    private static final Boolean UPDATED_INCLUDING_VAT = true;

    private static final Double DEFAULT_VAT = 1D;
    private static final Double UPDATED_VAT = 2D;

    private static final String DEFAULT_UNIT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_UNIT_NAME = "BBBBBBBBBB";

    private static final Double DEFAULT_PRICE = 1D;
    private static final Double UPDATED_PRICE = 2D;

    private static final Integer DEFAULT_DEFAULT_AMOUNT = 1;
    private static final Integer UPDATED_DEFAULT_AMOUNT = 2;

    private static final ZonedDateTime DEFAULT_CREATED = ZonedDateTime.ofInstant(Instant.ofEpochMilli(0L), ZoneOffset.UTC);
    private static final ZonedDateTime UPDATED_CREATED = ZonedDateTime.now(ZoneId.systemDefault()).withNano(0);

    private static final Boolean DEFAULT_INACTIV = false;
    private static final Boolean UPDATED_INACTIV = true;

    private static final String ENTITY_API_URL = "/api/catalog-services";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private CatalogServiceRepository catalogServiceRepository;

    @Autowired
    private CatalogServiceMapper catalogServiceMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restCatalogServiceMockMvc;

    private CatalogService catalogService;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static CatalogService createEntity(EntityManager em) {
        CatalogService catalogService = new CatalogService()
            .remoteId(DEFAULT_REMOTE_ID)
            .number(DEFAULT_NUMBER)
            .name(DEFAULT_NAME)
            .description(DEFAULT_DESCRIPTION)
            .notes(DEFAULT_NOTES)
            .categoryName(DEFAULT_CATEGORY_NAME)
            .includingVat(DEFAULT_INCLUDING_VAT)
            .vat(DEFAULT_VAT)
            .unitName(DEFAULT_UNIT_NAME)
            .price(DEFAULT_PRICE)
            .defaultAmount(DEFAULT_DEFAULT_AMOUNT)
            .created(DEFAULT_CREATED)
            .inactiv(DEFAULT_INACTIV);
        return catalogService;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static CatalogService createUpdatedEntity(EntityManager em) {
        CatalogService catalogService = new CatalogService()
            .remoteId(UPDATED_REMOTE_ID)
            .number(UPDATED_NUMBER)
            .name(UPDATED_NAME)
            .description(UPDATED_DESCRIPTION)
            .notes(UPDATED_NOTES)
            .categoryName(UPDATED_CATEGORY_NAME)
            .includingVat(UPDATED_INCLUDING_VAT)
            .vat(UPDATED_VAT)
            .unitName(UPDATED_UNIT_NAME)
            .price(UPDATED_PRICE)
            .defaultAmount(UPDATED_DEFAULT_AMOUNT)
            .created(UPDATED_CREATED)
            .inactiv(UPDATED_INACTIV);
        return catalogService;
    }

    @BeforeEach
    public void initTest() {
        catalogService = createEntity(em);
    }

    @Test
    @Transactional
    void createCatalogService() throws Exception {
        int databaseSizeBeforeCreate = catalogServiceRepository.findAll().size();
        // Create the CatalogService
        CatalogServiceDTO catalogServiceDTO = catalogServiceMapper.toDto(catalogService);
        restCatalogServiceMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(catalogServiceDTO))
            )
            .andExpect(status().isCreated());

        // Validate the CatalogService in the database
        List<CatalogService> catalogServiceList = catalogServiceRepository.findAll();
        assertThat(catalogServiceList).hasSize(databaseSizeBeforeCreate + 1);
        CatalogService testCatalogService = catalogServiceList.get(catalogServiceList.size() - 1);
        assertThat(testCatalogService.getRemoteId()).isEqualTo(DEFAULT_REMOTE_ID);
        assertThat(testCatalogService.getNumber()).isEqualTo(DEFAULT_NUMBER);
        assertThat(testCatalogService.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testCatalogService.getDescription()).isEqualTo(DEFAULT_DESCRIPTION);
        assertThat(testCatalogService.getNotes()).isEqualTo(DEFAULT_NOTES);
        assertThat(testCatalogService.getCategoryName()).isEqualTo(DEFAULT_CATEGORY_NAME);
        assertThat(testCatalogService.getIncludingVat()).isEqualTo(DEFAULT_INCLUDING_VAT);
        assertThat(testCatalogService.getVat()).isEqualTo(DEFAULT_VAT);
        assertThat(testCatalogService.getUnitName()).isEqualTo(DEFAULT_UNIT_NAME);
        assertThat(testCatalogService.getPrice()).isEqualTo(DEFAULT_PRICE);
        assertThat(testCatalogService.getDefaultAmount()).isEqualTo(DEFAULT_DEFAULT_AMOUNT);
        assertThat(testCatalogService.getCreated()).isEqualTo(DEFAULT_CREATED);
        assertThat(testCatalogService.getInactiv()).isEqualTo(DEFAULT_INACTIV);
    }

    @Test
    @Transactional
    void createCatalogServiceWithExistingId() throws Exception {
        // Create the CatalogService with an existing ID
        catalogService.setId(1L);
        CatalogServiceDTO catalogServiceDTO = catalogServiceMapper.toDto(catalogService);

        int databaseSizeBeforeCreate = catalogServiceRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restCatalogServiceMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(catalogServiceDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the CatalogService in the database
        List<CatalogService> catalogServiceList = catalogServiceRepository.findAll();
        assertThat(catalogServiceList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkNameIsRequired() throws Exception {
        int databaseSizeBeforeTest = catalogServiceRepository.findAll().size();
        // set the field null
        catalogService.setName(null);

        // Create the CatalogService, which fails.
        CatalogServiceDTO catalogServiceDTO = catalogServiceMapper.toDto(catalogService);

        restCatalogServiceMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(catalogServiceDTO))
            )
            .andExpect(status().isBadRequest());

        List<CatalogService> catalogServiceList = catalogServiceRepository.findAll();
        assertThat(catalogServiceList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllCatalogServices() throws Exception {
        // Initialize the database
        catalogServiceRepository.saveAndFlush(catalogService);

        // Get all the catalogServiceList
        restCatalogServiceMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(catalogService.getId().intValue())))
            .andExpect(jsonPath("$.[*].remoteId").value(hasItem(DEFAULT_REMOTE_ID)))
            .andExpect(jsonPath("$.[*].number").value(hasItem(DEFAULT_NUMBER)))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION)))
            .andExpect(jsonPath("$.[*].notes").value(hasItem(DEFAULT_NOTES)))
            .andExpect(jsonPath("$.[*].categoryName").value(hasItem(DEFAULT_CATEGORY_NAME)))
            .andExpect(jsonPath("$.[*].includingVat").value(hasItem(DEFAULT_INCLUDING_VAT.booleanValue())))
            .andExpect(jsonPath("$.[*].vat").value(hasItem(DEFAULT_VAT.doubleValue())))
            .andExpect(jsonPath("$.[*].unitName").value(hasItem(DEFAULT_UNIT_NAME)))
            .andExpect(jsonPath("$.[*].price").value(hasItem(DEFAULT_PRICE.doubleValue())))
            .andExpect(jsonPath("$.[*].defaultAmount").value(hasItem(DEFAULT_DEFAULT_AMOUNT)))
            .andExpect(jsonPath("$.[*].created").value(hasItem(sameInstant(DEFAULT_CREATED))))
            .andExpect(jsonPath("$.[*].inactiv").value(hasItem(DEFAULT_INACTIV.booleanValue())));
    }

    @Test
    @Transactional
    void getCatalogService() throws Exception {
        // Initialize the database
        catalogServiceRepository.saveAndFlush(catalogService);

        // Get the catalogService
        restCatalogServiceMockMvc
            .perform(get(ENTITY_API_URL_ID, catalogService.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(catalogService.getId().intValue()))
            .andExpect(jsonPath("$.remoteId").value(DEFAULT_REMOTE_ID))
            .andExpect(jsonPath("$.number").value(DEFAULT_NUMBER))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME))
            .andExpect(jsonPath("$.description").value(DEFAULT_DESCRIPTION))
            .andExpect(jsonPath("$.notes").value(DEFAULT_NOTES))
            .andExpect(jsonPath("$.categoryName").value(DEFAULT_CATEGORY_NAME))
            .andExpect(jsonPath("$.includingVat").value(DEFAULT_INCLUDING_VAT.booleanValue()))
            .andExpect(jsonPath("$.vat").value(DEFAULT_VAT.doubleValue()))
            .andExpect(jsonPath("$.unitName").value(DEFAULT_UNIT_NAME))
            .andExpect(jsonPath("$.price").value(DEFAULT_PRICE.doubleValue()))
            .andExpect(jsonPath("$.defaultAmount").value(DEFAULT_DEFAULT_AMOUNT))
            .andExpect(jsonPath("$.created").value(sameInstant(DEFAULT_CREATED)))
            .andExpect(jsonPath("$.inactiv").value(DEFAULT_INACTIV.booleanValue()));
    }

    @Test
    @Transactional
    void getNonExistingCatalogService() throws Exception {
        // Get the catalogService
        restCatalogServiceMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewCatalogService() throws Exception {
        // Initialize the database
        catalogServiceRepository.saveAndFlush(catalogService);

        int databaseSizeBeforeUpdate = catalogServiceRepository.findAll().size();

        // Update the catalogService
        CatalogService updatedCatalogService = catalogServiceRepository.findById(catalogService.getId()).get();
        // Disconnect from session so that the updates on updatedCatalogService are not directly saved in db
        em.detach(updatedCatalogService);
        updatedCatalogService
            .remoteId(UPDATED_REMOTE_ID)
            .number(UPDATED_NUMBER)
            .name(UPDATED_NAME)
            .description(UPDATED_DESCRIPTION)
            .notes(UPDATED_NOTES)
            .categoryName(UPDATED_CATEGORY_NAME)
            .includingVat(UPDATED_INCLUDING_VAT)
            .vat(UPDATED_VAT)
            .unitName(UPDATED_UNIT_NAME)
            .price(UPDATED_PRICE)
            .defaultAmount(UPDATED_DEFAULT_AMOUNT)
            .created(UPDATED_CREATED)
            .inactiv(UPDATED_INACTIV);
        CatalogServiceDTO catalogServiceDTO = catalogServiceMapper.toDto(updatedCatalogService);

        restCatalogServiceMockMvc
            .perform(
                put(ENTITY_API_URL_ID, catalogServiceDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(catalogServiceDTO))
            )
            .andExpect(status().isOk());

        // Validate the CatalogService in the database
        List<CatalogService> catalogServiceList = catalogServiceRepository.findAll();
        assertThat(catalogServiceList).hasSize(databaseSizeBeforeUpdate);
        CatalogService testCatalogService = catalogServiceList.get(catalogServiceList.size() - 1);
        assertThat(testCatalogService.getRemoteId()).isEqualTo(UPDATED_REMOTE_ID);
        assertThat(testCatalogService.getNumber()).isEqualTo(UPDATED_NUMBER);
        assertThat(testCatalogService.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testCatalogService.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
        assertThat(testCatalogService.getNotes()).isEqualTo(UPDATED_NOTES);
        assertThat(testCatalogService.getCategoryName()).isEqualTo(UPDATED_CATEGORY_NAME);
        assertThat(testCatalogService.getIncludingVat()).isEqualTo(UPDATED_INCLUDING_VAT);
        assertThat(testCatalogService.getVat()).isEqualTo(UPDATED_VAT);
        assertThat(testCatalogService.getUnitName()).isEqualTo(UPDATED_UNIT_NAME);
        assertThat(testCatalogService.getPrice()).isEqualTo(UPDATED_PRICE);
        assertThat(testCatalogService.getDefaultAmount()).isEqualTo(UPDATED_DEFAULT_AMOUNT);
        assertThat(testCatalogService.getCreated()).isEqualTo(UPDATED_CREATED);
        assertThat(testCatalogService.getInactiv()).isEqualTo(UPDATED_INACTIV);
    }

    @Test
    @Transactional
    void putNonExistingCatalogService() throws Exception {
        int databaseSizeBeforeUpdate = catalogServiceRepository.findAll().size();
        catalogService.setId(count.incrementAndGet());

        // Create the CatalogService
        CatalogServiceDTO catalogServiceDTO = catalogServiceMapper.toDto(catalogService);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restCatalogServiceMockMvc
            .perform(
                put(ENTITY_API_URL_ID, catalogServiceDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(catalogServiceDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the CatalogService in the database
        List<CatalogService> catalogServiceList = catalogServiceRepository.findAll();
        assertThat(catalogServiceList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchCatalogService() throws Exception {
        int databaseSizeBeforeUpdate = catalogServiceRepository.findAll().size();
        catalogService.setId(count.incrementAndGet());

        // Create the CatalogService
        CatalogServiceDTO catalogServiceDTO = catalogServiceMapper.toDto(catalogService);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCatalogServiceMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(catalogServiceDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the CatalogService in the database
        List<CatalogService> catalogServiceList = catalogServiceRepository.findAll();
        assertThat(catalogServiceList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamCatalogService() throws Exception {
        int databaseSizeBeforeUpdate = catalogServiceRepository.findAll().size();
        catalogService.setId(count.incrementAndGet());

        // Create the CatalogService
        CatalogServiceDTO catalogServiceDTO = catalogServiceMapper.toDto(catalogService);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCatalogServiceMockMvc
            .perform(
                put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(catalogServiceDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the CatalogService in the database
        List<CatalogService> catalogServiceList = catalogServiceRepository.findAll();
        assertThat(catalogServiceList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateCatalogServiceWithPatch() throws Exception {
        // Initialize the database
        catalogServiceRepository.saveAndFlush(catalogService);

        int databaseSizeBeforeUpdate = catalogServiceRepository.findAll().size();

        // Update the catalogService using partial update
        CatalogService partialUpdatedCatalogService = new CatalogService();
        partialUpdatedCatalogService.setId(catalogService.getId());

        partialUpdatedCatalogService
            .remoteId(UPDATED_REMOTE_ID)
            .name(UPDATED_NAME)
            .description(UPDATED_DESCRIPTION)
            .vat(UPDATED_VAT)
            .defaultAmount(UPDATED_DEFAULT_AMOUNT)
            .created(UPDATED_CREATED)
            .inactiv(UPDATED_INACTIV);

        restCatalogServiceMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedCatalogService.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedCatalogService))
            )
            .andExpect(status().isOk());

        // Validate the CatalogService in the database
        List<CatalogService> catalogServiceList = catalogServiceRepository.findAll();
        assertThat(catalogServiceList).hasSize(databaseSizeBeforeUpdate);
        CatalogService testCatalogService = catalogServiceList.get(catalogServiceList.size() - 1);
        assertThat(testCatalogService.getRemoteId()).isEqualTo(UPDATED_REMOTE_ID);
        assertThat(testCatalogService.getNumber()).isEqualTo(DEFAULT_NUMBER);
        assertThat(testCatalogService.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testCatalogService.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
        assertThat(testCatalogService.getNotes()).isEqualTo(DEFAULT_NOTES);
        assertThat(testCatalogService.getCategoryName()).isEqualTo(DEFAULT_CATEGORY_NAME);
        assertThat(testCatalogService.getIncludingVat()).isEqualTo(DEFAULT_INCLUDING_VAT);
        assertThat(testCatalogService.getVat()).isEqualTo(UPDATED_VAT);
        assertThat(testCatalogService.getUnitName()).isEqualTo(DEFAULT_UNIT_NAME);
        assertThat(testCatalogService.getPrice()).isEqualTo(DEFAULT_PRICE);
        assertThat(testCatalogService.getDefaultAmount()).isEqualTo(UPDATED_DEFAULT_AMOUNT);
        assertThat(testCatalogService.getCreated()).isEqualTo(UPDATED_CREATED);
        assertThat(testCatalogService.getInactiv()).isEqualTo(UPDATED_INACTIV);
    }

    @Test
    @Transactional
    void fullUpdateCatalogServiceWithPatch() throws Exception {
        // Initialize the database
        catalogServiceRepository.saveAndFlush(catalogService);

        int databaseSizeBeforeUpdate = catalogServiceRepository.findAll().size();

        // Update the catalogService using partial update
        CatalogService partialUpdatedCatalogService = new CatalogService();
        partialUpdatedCatalogService.setId(catalogService.getId());

        partialUpdatedCatalogService
            .remoteId(UPDATED_REMOTE_ID)
            .number(UPDATED_NUMBER)
            .name(UPDATED_NAME)
            .description(UPDATED_DESCRIPTION)
            .notes(UPDATED_NOTES)
            .categoryName(UPDATED_CATEGORY_NAME)
            .includingVat(UPDATED_INCLUDING_VAT)
            .vat(UPDATED_VAT)
            .unitName(UPDATED_UNIT_NAME)
            .price(UPDATED_PRICE)
            .defaultAmount(UPDATED_DEFAULT_AMOUNT)
            .created(UPDATED_CREATED)
            .inactiv(UPDATED_INACTIV);

        restCatalogServiceMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedCatalogService.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedCatalogService))
            )
            .andExpect(status().isOk());

        // Validate the CatalogService in the database
        List<CatalogService> catalogServiceList = catalogServiceRepository.findAll();
        assertThat(catalogServiceList).hasSize(databaseSizeBeforeUpdate);
        CatalogService testCatalogService = catalogServiceList.get(catalogServiceList.size() - 1);
        assertThat(testCatalogService.getRemoteId()).isEqualTo(UPDATED_REMOTE_ID);
        assertThat(testCatalogService.getNumber()).isEqualTo(UPDATED_NUMBER);
        assertThat(testCatalogService.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testCatalogService.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
        assertThat(testCatalogService.getNotes()).isEqualTo(UPDATED_NOTES);
        assertThat(testCatalogService.getCategoryName()).isEqualTo(UPDATED_CATEGORY_NAME);
        assertThat(testCatalogService.getIncludingVat()).isEqualTo(UPDATED_INCLUDING_VAT);
        assertThat(testCatalogService.getVat()).isEqualTo(UPDATED_VAT);
        assertThat(testCatalogService.getUnitName()).isEqualTo(UPDATED_UNIT_NAME);
        assertThat(testCatalogService.getPrice()).isEqualTo(UPDATED_PRICE);
        assertThat(testCatalogService.getDefaultAmount()).isEqualTo(UPDATED_DEFAULT_AMOUNT);
        assertThat(testCatalogService.getCreated()).isEqualTo(UPDATED_CREATED);
        assertThat(testCatalogService.getInactiv()).isEqualTo(UPDATED_INACTIV);
    }

    @Test
    @Transactional
    void patchNonExistingCatalogService() throws Exception {
        int databaseSizeBeforeUpdate = catalogServiceRepository.findAll().size();
        catalogService.setId(count.incrementAndGet());

        // Create the CatalogService
        CatalogServiceDTO catalogServiceDTO = catalogServiceMapper.toDto(catalogService);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restCatalogServiceMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, catalogServiceDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(catalogServiceDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the CatalogService in the database
        List<CatalogService> catalogServiceList = catalogServiceRepository.findAll();
        assertThat(catalogServiceList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchCatalogService() throws Exception {
        int databaseSizeBeforeUpdate = catalogServiceRepository.findAll().size();
        catalogService.setId(count.incrementAndGet());

        // Create the CatalogService
        CatalogServiceDTO catalogServiceDTO = catalogServiceMapper.toDto(catalogService);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCatalogServiceMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(catalogServiceDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the CatalogService in the database
        List<CatalogService> catalogServiceList = catalogServiceRepository.findAll();
        assertThat(catalogServiceList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamCatalogService() throws Exception {
        int databaseSizeBeforeUpdate = catalogServiceRepository.findAll().size();
        catalogService.setId(count.incrementAndGet());

        // Create the CatalogService
        CatalogServiceDTO catalogServiceDTO = catalogServiceMapper.toDto(catalogService);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCatalogServiceMockMvc
            .perform(
                patch(ENTITY_API_URL)
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(catalogServiceDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the CatalogService in the database
        List<CatalogService> catalogServiceList = catalogServiceRepository.findAll();
        assertThat(catalogServiceList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteCatalogService() throws Exception {
        // Initialize the database
        catalogServiceRepository.saveAndFlush(catalogService);

        int databaseSizeBeforeDelete = catalogServiceRepository.findAll().size();

        // Delete the catalogService
        restCatalogServiceMockMvc
            .perform(delete(ENTITY_API_URL_ID, catalogService.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<CatalogService> catalogServiceList = catalogServiceRepository.findAll();
        assertThat(catalogServiceList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
