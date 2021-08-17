package ch.united.fastadmin.web.rest;

import static ch.united.fastadmin.web.rest.TestUtil.sameInstant;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import ch.united.fastadmin.IntegrationTest;
import ch.united.fastadmin.domain.CatalogCategory;
import ch.united.fastadmin.domain.CatalogService;
import ch.united.fastadmin.domain.CatalogUnit;
import ch.united.fastadmin.domain.CustomFieldValue;
import ch.united.fastadmin.domain.ValueAddedTax;
import ch.united.fastadmin.repository.CatalogServiceRepository;
import ch.united.fastadmin.service.criteria.CatalogServiceCriteria;
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
    private static final Integer SMALLER_REMOTE_ID = 1 - 1;

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
    private static final Double SMALLER_VAT = 1D - 1D;

    private static final String DEFAULT_UNIT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_UNIT_NAME = "BBBBBBBBBB";

    private static final Double DEFAULT_PRICE = 1D;
    private static final Double UPDATED_PRICE = 2D;
    private static final Double SMALLER_PRICE = 1D - 1D;

    private static final Integer DEFAULT_DEFAULT_AMOUNT = 1;
    private static final Integer UPDATED_DEFAULT_AMOUNT = 2;
    private static final Integer SMALLER_DEFAULT_AMOUNT = 1 - 1;

    private static final ZonedDateTime DEFAULT_CREATED = ZonedDateTime.ofInstant(Instant.ofEpochMilli(0L), ZoneOffset.UTC);
    private static final ZonedDateTime UPDATED_CREATED = ZonedDateTime.now(ZoneId.systemDefault()).withNano(0);
    private static final ZonedDateTime SMALLER_CREATED = ZonedDateTime.ofInstant(Instant.ofEpochMilli(-1L), ZoneOffset.UTC);

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
    void getCatalogServicesByIdFiltering() throws Exception {
        // Initialize the database
        catalogServiceRepository.saveAndFlush(catalogService);

        Long id = catalogService.getId();

        defaultCatalogServiceShouldBeFound("id.equals=" + id);
        defaultCatalogServiceShouldNotBeFound("id.notEquals=" + id);

        defaultCatalogServiceShouldBeFound("id.greaterThanOrEqual=" + id);
        defaultCatalogServiceShouldNotBeFound("id.greaterThan=" + id);

        defaultCatalogServiceShouldBeFound("id.lessThanOrEqual=" + id);
        defaultCatalogServiceShouldNotBeFound("id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllCatalogServicesByRemoteIdIsEqualToSomething() throws Exception {
        // Initialize the database
        catalogServiceRepository.saveAndFlush(catalogService);

        // Get all the catalogServiceList where remoteId equals to DEFAULT_REMOTE_ID
        defaultCatalogServiceShouldBeFound("remoteId.equals=" + DEFAULT_REMOTE_ID);

        // Get all the catalogServiceList where remoteId equals to UPDATED_REMOTE_ID
        defaultCatalogServiceShouldNotBeFound("remoteId.equals=" + UPDATED_REMOTE_ID);
    }

    @Test
    @Transactional
    void getAllCatalogServicesByRemoteIdIsNotEqualToSomething() throws Exception {
        // Initialize the database
        catalogServiceRepository.saveAndFlush(catalogService);

        // Get all the catalogServiceList where remoteId not equals to DEFAULT_REMOTE_ID
        defaultCatalogServiceShouldNotBeFound("remoteId.notEquals=" + DEFAULT_REMOTE_ID);

        // Get all the catalogServiceList where remoteId not equals to UPDATED_REMOTE_ID
        defaultCatalogServiceShouldBeFound("remoteId.notEquals=" + UPDATED_REMOTE_ID);
    }

    @Test
    @Transactional
    void getAllCatalogServicesByRemoteIdIsInShouldWork() throws Exception {
        // Initialize the database
        catalogServiceRepository.saveAndFlush(catalogService);

        // Get all the catalogServiceList where remoteId in DEFAULT_REMOTE_ID or UPDATED_REMOTE_ID
        defaultCatalogServiceShouldBeFound("remoteId.in=" + DEFAULT_REMOTE_ID + "," + UPDATED_REMOTE_ID);

        // Get all the catalogServiceList where remoteId equals to UPDATED_REMOTE_ID
        defaultCatalogServiceShouldNotBeFound("remoteId.in=" + UPDATED_REMOTE_ID);
    }

    @Test
    @Transactional
    void getAllCatalogServicesByRemoteIdIsNullOrNotNull() throws Exception {
        // Initialize the database
        catalogServiceRepository.saveAndFlush(catalogService);

        // Get all the catalogServiceList where remoteId is not null
        defaultCatalogServiceShouldBeFound("remoteId.specified=true");

        // Get all the catalogServiceList where remoteId is null
        defaultCatalogServiceShouldNotBeFound("remoteId.specified=false");
    }

    @Test
    @Transactional
    void getAllCatalogServicesByRemoteIdIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        catalogServiceRepository.saveAndFlush(catalogService);

        // Get all the catalogServiceList where remoteId is greater than or equal to DEFAULT_REMOTE_ID
        defaultCatalogServiceShouldBeFound("remoteId.greaterThanOrEqual=" + DEFAULT_REMOTE_ID);

        // Get all the catalogServiceList where remoteId is greater than or equal to UPDATED_REMOTE_ID
        defaultCatalogServiceShouldNotBeFound("remoteId.greaterThanOrEqual=" + UPDATED_REMOTE_ID);
    }

    @Test
    @Transactional
    void getAllCatalogServicesByRemoteIdIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        catalogServiceRepository.saveAndFlush(catalogService);

        // Get all the catalogServiceList where remoteId is less than or equal to DEFAULT_REMOTE_ID
        defaultCatalogServiceShouldBeFound("remoteId.lessThanOrEqual=" + DEFAULT_REMOTE_ID);

        // Get all the catalogServiceList where remoteId is less than or equal to SMALLER_REMOTE_ID
        defaultCatalogServiceShouldNotBeFound("remoteId.lessThanOrEqual=" + SMALLER_REMOTE_ID);
    }

    @Test
    @Transactional
    void getAllCatalogServicesByRemoteIdIsLessThanSomething() throws Exception {
        // Initialize the database
        catalogServiceRepository.saveAndFlush(catalogService);

        // Get all the catalogServiceList where remoteId is less than DEFAULT_REMOTE_ID
        defaultCatalogServiceShouldNotBeFound("remoteId.lessThan=" + DEFAULT_REMOTE_ID);

        // Get all the catalogServiceList where remoteId is less than UPDATED_REMOTE_ID
        defaultCatalogServiceShouldBeFound("remoteId.lessThan=" + UPDATED_REMOTE_ID);
    }

    @Test
    @Transactional
    void getAllCatalogServicesByRemoteIdIsGreaterThanSomething() throws Exception {
        // Initialize the database
        catalogServiceRepository.saveAndFlush(catalogService);

        // Get all the catalogServiceList where remoteId is greater than DEFAULT_REMOTE_ID
        defaultCatalogServiceShouldNotBeFound("remoteId.greaterThan=" + DEFAULT_REMOTE_ID);

        // Get all the catalogServiceList where remoteId is greater than SMALLER_REMOTE_ID
        defaultCatalogServiceShouldBeFound("remoteId.greaterThan=" + SMALLER_REMOTE_ID);
    }

    @Test
    @Transactional
    void getAllCatalogServicesByNumberIsEqualToSomething() throws Exception {
        // Initialize the database
        catalogServiceRepository.saveAndFlush(catalogService);

        // Get all the catalogServiceList where number equals to DEFAULT_NUMBER
        defaultCatalogServiceShouldBeFound("number.equals=" + DEFAULT_NUMBER);

        // Get all the catalogServiceList where number equals to UPDATED_NUMBER
        defaultCatalogServiceShouldNotBeFound("number.equals=" + UPDATED_NUMBER);
    }

    @Test
    @Transactional
    void getAllCatalogServicesByNumberIsNotEqualToSomething() throws Exception {
        // Initialize the database
        catalogServiceRepository.saveAndFlush(catalogService);

        // Get all the catalogServiceList where number not equals to DEFAULT_NUMBER
        defaultCatalogServiceShouldNotBeFound("number.notEquals=" + DEFAULT_NUMBER);

        // Get all the catalogServiceList where number not equals to UPDATED_NUMBER
        defaultCatalogServiceShouldBeFound("number.notEquals=" + UPDATED_NUMBER);
    }

    @Test
    @Transactional
    void getAllCatalogServicesByNumberIsInShouldWork() throws Exception {
        // Initialize the database
        catalogServiceRepository.saveAndFlush(catalogService);

        // Get all the catalogServiceList where number in DEFAULT_NUMBER or UPDATED_NUMBER
        defaultCatalogServiceShouldBeFound("number.in=" + DEFAULT_NUMBER + "," + UPDATED_NUMBER);

        // Get all the catalogServiceList where number equals to UPDATED_NUMBER
        defaultCatalogServiceShouldNotBeFound("number.in=" + UPDATED_NUMBER);
    }

    @Test
    @Transactional
    void getAllCatalogServicesByNumberIsNullOrNotNull() throws Exception {
        // Initialize the database
        catalogServiceRepository.saveAndFlush(catalogService);

        // Get all the catalogServiceList where number is not null
        defaultCatalogServiceShouldBeFound("number.specified=true");

        // Get all the catalogServiceList where number is null
        defaultCatalogServiceShouldNotBeFound("number.specified=false");
    }

    @Test
    @Transactional
    void getAllCatalogServicesByNumberContainsSomething() throws Exception {
        // Initialize the database
        catalogServiceRepository.saveAndFlush(catalogService);

        // Get all the catalogServiceList where number contains DEFAULT_NUMBER
        defaultCatalogServiceShouldBeFound("number.contains=" + DEFAULT_NUMBER);

        // Get all the catalogServiceList where number contains UPDATED_NUMBER
        defaultCatalogServiceShouldNotBeFound("number.contains=" + UPDATED_NUMBER);
    }

    @Test
    @Transactional
    void getAllCatalogServicesByNumberNotContainsSomething() throws Exception {
        // Initialize the database
        catalogServiceRepository.saveAndFlush(catalogService);

        // Get all the catalogServiceList where number does not contain DEFAULT_NUMBER
        defaultCatalogServiceShouldNotBeFound("number.doesNotContain=" + DEFAULT_NUMBER);

        // Get all the catalogServiceList where number does not contain UPDATED_NUMBER
        defaultCatalogServiceShouldBeFound("number.doesNotContain=" + UPDATED_NUMBER);
    }

    @Test
    @Transactional
    void getAllCatalogServicesByNameIsEqualToSomething() throws Exception {
        // Initialize the database
        catalogServiceRepository.saveAndFlush(catalogService);

        // Get all the catalogServiceList where name equals to DEFAULT_NAME
        defaultCatalogServiceShouldBeFound("name.equals=" + DEFAULT_NAME);

        // Get all the catalogServiceList where name equals to UPDATED_NAME
        defaultCatalogServiceShouldNotBeFound("name.equals=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllCatalogServicesByNameIsNotEqualToSomething() throws Exception {
        // Initialize the database
        catalogServiceRepository.saveAndFlush(catalogService);

        // Get all the catalogServiceList where name not equals to DEFAULT_NAME
        defaultCatalogServiceShouldNotBeFound("name.notEquals=" + DEFAULT_NAME);

        // Get all the catalogServiceList where name not equals to UPDATED_NAME
        defaultCatalogServiceShouldBeFound("name.notEquals=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllCatalogServicesByNameIsInShouldWork() throws Exception {
        // Initialize the database
        catalogServiceRepository.saveAndFlush(catalogService);

        // Get all the catalogServiceList where name in DEFAULT_NAME or UPDATED_NAME
        defaultCatalogServiceShouldBeFound("name.in=" + DEFAULT_NAME + "," + UPDATED_NAME);

        // Get all the catalogServiceList where name equals to UPDATED_NAME
        defaultCatalogServiceShouldNotBeFound("name.in=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllCatalogServicesByNameIsNullOrNotNull() throws Exception {
        // Initialize the database
        catalogServiceRepository.saveAndFlush(catalogService);

        // Get all the catalogServiceList where name is not null
        defaultCatalogServiceShouldBeFound("name.specified=true");

        // Get all the catalogServiceList where name is null
        defaultCatalogServiceShouldNotBeFound("name.specified=false");
    }

    @Test
    @Transactional
    void getAllCatalogServicesByNameContainsSomething() throws Exception {
        // Initialize the database
        catalogServiceRepository.saveAndFlush(catalogService);

        // Get all the catalogServiceList where name contains DEFAULT_NAME
        defaultCatalogServiceShouldBeFound("name.contains=" + DEFAULT_NAME);

        // Get all the catalogServiceList where name contains UPDATED_NAME
        defaultCatalogServiceShouldNotBeFound("name.contains=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllCatalogServicesByNameNotContainsSomething() throws Exception {
        // Initialize the database
        catalogServiceRepository.saveAndFlush(catalogService);

        // Get all the catalogServiceList where name does not contain DEFAULT_NAME
        defaultCatalogServiceShouldNotBeFound("name.doesNotContain=" + DEFAULT_NAME);

        // Get all the catalogServiceList where name does not contain UPDATED_NAME
        defaultCatalogServiceShouldBeFound("name.doesNotContain=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllCatalogServicesByDescriptionIsEqualToSomething() throws Exception {
        // Initialize the database
        catalogServiceRepository.saveAndFlush(catalogService);

        // Get all the catalogServiceList where description equals to DEFAULT_DESCRIPTION
        defaultCatalogServiceShouldBeFound("description.equals=" + DEFAULT_DESCRIPTION);

        // Get all the catalogServiceList where description equals to UPDATED_DESCRIPTION
        defaultCatalogServiceShouldNotBeFound("description.equals=" + UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    void getAllCatalogServicesByDescriptionIsNotEqualToSomething() throws Exception {
        // Initialize the database
        catalogServiceRepository.saveAndFlush(catalogService);

        // Get all the catalogServiceList where description not equals to DEFAULT_DESCRIPTION
        defaultCatalogServiceShouldNotBeFound("description.notEquals=" + DEFAULT_DESCRIPTION);

        // Get all the catalogServiceList where description not equals to UPDATED_DESCRIPTION
        defaultCatalogServiceShouldBeFound("description.notEquals=" + UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    void getAllCatalogServicesByDescriptionIsInShouldWork() throws Exception {
        // Initialize the database
        catalogServiceRepository.saveAndFlush(catalogService);

        // Get all the catalogServiceList where description in DEFAULT_DESCRIPTION or UPDATED_DESCRIPTION
        defaultCatalogServiceShouldBeFound("description.in=" + DEFAULT_DESCRIPTION + "," + UPDATED_DESCRIPTION);

        // Get all the catalogServiceList where description equals to UPDATED_DESCRIPTION
        defaultCatalogServiceShouldNotBeFound("description.in=" + UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    void getAllCatalogServicesByDescriptionIsNullOrNotNull() throws Exception {
        // Initialize the database
        catalogServiceRepository.saveAndFlush(catalogService);

        // Get all the catalogServiceList where description is not null
        defaultCatalogServiceShouldBeFound("description.specified=true");

        // Get all the catalogServiceList where description is null
        defaultCatalogServiceShouldNotBeFound("description.specified=false");
    }

    @Test
    @Transactional
    void getAllCatalogServicesByDescriptionContainsSomething() throws Exception {
        // Initialize the database
        catalogServiceRepository.saveAndFlush(catalogService);

        // Get all the catalogServiceList where description contains DEFAULT_DESCRIPTION
        defaultCatalogServiceShouldBeFound("description.contains=" + DEFAULT_DESCRIPTION);

        // Get all the catalogServiceList where description contains UPDATED_DESCRIPTION
        defaultCatalogServiceShouldNotBeFound("description.contains=" + UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    void getAllCatalogServicesByDescriptionNotContainsSomething() throws Exception {
        // Initialize the database
        catalogServiceRepository.saveAndFlush(catalogService);

        // Get all the catalogServiceList where description does not contain DEFAULT_DESCRIPTION
        defaultCatalogServiceShouldNotBeFound("description.doesNotContain=" + DEFAULT_DESCRIPTION);

        // Get all the catalogServiceList where description does not contain UPDATED_DESCRIPTION
        defaultCatalogServiceShouldBeFound("description.doesNotContain=" + UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    void getAllCatalogServicesByNotesIsEqualToSomething() throws Exception {
        // Initialize the database
        catalogServiceRepository.saveAndFlush(catalogService);

        // Get all the catalogServiceList where notes equals to DEFAULT_NOTES
        defaultCatalogServiceShouldBeFound("notes.equals=" + DEFAULT_NOTES);

        // Get all the catalogServiceList where notes equals to UPDATED_NOTES
        defaultCatalogServiceShouldNotBeFound("notes.equals=" + UPDATED_NOTES);
    }

    @Test
    @Transactional
    void getAllCatalogServicesByNotesIsNotEqualToSomething() throws Exception {
        // Initialize the database
        catalogServiceRepository.saveAndFlush(catalogService);

        // Get all the catalogServiceList where notes not equals to DEFAULT_NOTES
        defaultCatalogServiceShouldNotBeFound("notes.notEquals=" + DEFAULT_NOTES);

        // Get all the catalogServiceList where notes not equals to UPDATED_NOTES
        defaultCatalogServiceShouldBeFound("notes.notEquals=" + UPDATED_NOTES);
    }

    @Test
    @Transactional
    void getAllCatalogServicesByNotesIsInShouldWork() throws Exception {
        // Initialize the database
        catalogServiceRepository.saveAndFlush(catalogService);

        // Get all the catalogServiceList where notes in DEFAULT_NOTES or UPDATED_NOTES
        defaultCatalogServiceShouldBeFound("notes.in=" + DEFAULT_NOTES + "," + UPDATED_NOTES);

        // Get all the catalogServiceList where notes equals to UPDATED_NOTES
        defaultCatalogServiceShouldNotBeFound("notes.in=" + UPDATED_NOTES);
    }

    @Test
    @Transactional
    void getAllCatalogServicesByNotesIsNullOrNotNull() throws Exception {
        // Initialize the database
        catalogServiceRepository.saveAndFlush(catalogService);

        // Get all the catalogServiceList where notes is not null
        defaultCatalogServiceShouldBeFound("notes.specified=true");

        // Get all the catalogServiceList where notes is null
        defaultCatalogServiceShouldNotBeFound("notes.specified=false");
    }

    @Test
    @Transactional
    void getAllCatalogServicesByNotesContainsSomething() throws Exception {
        // Initialize the database
        catalogServiceRepository.saveAndFlush(catalogService);

        // Get all the catalogServiceList where notes contains DEFAULT_NOTES
        defaultCatalogServiceShouldBeFound("notes.contains=" + DEFAULT_NOTES);

        // Get all the catalogServiceList where notes contains UPDATED_NOTES
        defaultCatalogServiceShouldNotBeFound("notes.contains=" + UPDATED_NOTES);
    }

    @Test
    @Transactional
    void getAllCatalogServicesByNotesNotContainsSomething() throws Exception {
        // Initialize the database
        catalogServiceRepository.saveAndFlush(catalogService);

        // Get all the catalogServiceList where notes does not contain DEFAULT_NOTES
        defaultCatalogServiceShouldNotBeFound("notes.doesNotContain=" + DEFAULT_NOTES);

        // Get all the catalogServiceList where notes does not contain UPDATED_NOTES
        defaultCatalogServiceShouldBeFound("notes.doesNotContain=" + UPDATED_NOTES);
    }

    @Test
    @Transactional
    void getAllCatalogServicesByCategoryNameIsEqualToSomething() throws Exception {
        // Initialize the database
        catalogServiceRepository.saveAndFlush(catalogService);

        // Get all the catalogServiceList where categoryName equals to DEFAULT_CATEGORY_NAME
        defaultCatalogServiceShouldBeFound("categoryName.equals=" + DEFAULT_CATEGORY_NAME);

        // Get all the catalogServiceList where categoryName equals to UPDATED_CATEGORY_NAME
        defaultCatalogServiceShouldNotBeFound("categoryName.equals=" + UPDATED_CATEGORY_NAME);
    }

    @Test
    @Transactional
    void getAllCatalogServicesByCategoryNameIsNotEqualToSomething() throws Exception {
        // Initialize the database
        catalogServiceRepository.saveAndFlush(catalogService);

        // Get all the catalogServiceList where categoryName not equals to DEFAULT_CATEGORY_NAME
        defaultCatalogServiceShouldNotBeFound("categoryName.notEquals=" + DEFAULT_CATEGORY_NAME);

        // Get all the catalogServiceList where categoryName not equals to UPDATED_CATEGORY_NAME
        defaultCatalogServiceShouldBeFound("categoryName.notEquals=" + UPDATED_CATEGORY_NAME);
    }

    @Test
    @Transactional
    void getAllCatalogServicesByCategoryNameIsInShouldWork() throws Exception {
        // Initialize the database
        catalogServiceRepository.saveAndFlush(catalogService);

        // Get all the catalogServiceList where categoryName in DEFAULT_CATEGORY_NAME or UPDATED_CATEGORY_NAME
        defaultCatalogServiceShouldBeFound("categoryName.in=" + DEFAULT_CATEGORY_NAME + "," + UPDATED_CATEGORY_NAME);

        // Get all the catalogServiceList where categoryName equals to UPDATED_CATEGORY_NAME
        defaultCatalogServiceShouldNotBeFound("categoryName.in=" + UPDATED_CATEGORY_NAME);
    }

    @Test
    @Transactional
    void getAllCatalogServicesByCategoryNameIsNullOrNotNull() throws Exception {
        // Initialize the database
        catalogServiceRepository.saveAndFlush(catalogService);

        // Get all the catalogServiceList where categoryName is not null
        defaultCatalogServiceShouldBeFound("categoryName.specified=true");

        // Get all the catalogServiceList where categoryName is null
        defaultCatalogServiceShouldNotBeFound("categoryName.specified=false");
    }

    @Test
    @Transactional
    void getAllCatalogServicesByCategoryNameContainsSomething() throws Exception {
        // Initialize the database
        catalogServiceRepository.saveAndFlush(catalogService);

        // Get all the catalogServiceList where categoryName contains DEFAULT_CATEGORY_NAME
        defaultCatalogServiceShouldBeFound("categoryName.contains=" + DEFAULT_CATEGORY_NAME);

        // Get all the catalogServiceList where categoryName contains UPDATED_CATEGORY_NAME
        defaultCatalogServiceShouldNotBeFound("categoryName.contains=" + UPDATED_CATEGORY_NAME);
    }

    @Test
    @Transactional
    void getAllCatalogServicesByCategoryNameNotContainsSomething() throws Exception {
        // Initialize the database
        catalogServiceRepository.saveAndFlush(catalogService);

        // Get all the catalogServiceList where categoryName does not contain DEFAULT_CATEGORY_NAME
        defaultCatalogServiceShouldNotBeFound("categoryName.doesNotContain=" + DEFAULT_CATEGORY_NAME);

        // Get all the catalogServiceList where categoryName does not contain UPDATED_CATEGORY_NAME
        defaultCatalogServiceShouldBeFound("categoryName.doesNotContain=" + UPDATED_CATEGORY_NAME);
    }

    @Test
    @Transactional
    void getAllCatalogServicesByIncludingVatIsEqualToSomething() throws Exception {
        // Initialize the database
        catalogServiceRepository.saveAndFlush(catalogService);

        // Get all the catalogServiceList where includingVat equals to DEFAULT_INCLUDING_VAT
        defaultCatalogServiceShouldBeFound("includingVat.equals=" + DEFAULT_INCLUDING_VAT);

        // Get all the catalogServiceList where includingVat equals to UPDATED_INCLUDING_VAT
        defaultCatalogServiceShouldNotBeFound("includingVat.equals=" + UPDATED_INCLUDING_VAT);
    }

    @Test
    @Transactional
    void getAllCatalogServicesByIncludingVatIsNotEqualToSomething() throws Exception {
        // Initialize the database
        catalogServiceRepository.saveAndFlush(catalogService);

        // Get all the catalogServiceList where includingVat not equals to DEFAULT_INCLUDING_VAT
        defaultCatalogServiceShouldNotBeFound("includingVat.notEquals=" + DEFAULT_INCLUDING_VAT);

        // Get all the catalogServiceList where includingVat not equals to UPDATED_INCLUDING_VAT
        defaultCatalogServiceShouldBeFound("includingVat.notEquals=" + UPDATED_INCLUDING_VAT);
    }

    @Test
    @Transactional
    void getAllCatalogServicesByIncludingVatIsInShouldWork() throws Exception {
        // Initialize the database
        catalogServiceRepository.saveAndFlush(catalogService);

        // Get all the catalogServiceList where includingVat in DEFAULT_INCLUDING_VAT or UPDATED_INCLUDING_VAT
        defaultCatalogServiceShouldBeFound("includingVat.in=" + DEFAULT_INCLUDING_VAT + "," + UPDATED_INCLUDING_VAT);

        // Get all the catalogServiceList where includingVat equals to UPDATED_INCLUDING_VAT
        defaultCatalogServiceShouldNotBeFound("includingVat.in=" + UPDATED_INCLUDING_VAT);
    }

    @Test
    @Transactional
    void getAllCatalogServicesByIncludingVatIsNullOrNotNull() throws Exception {
        // Initialize the database
        catalogServiceRepository.saveAndFlush(catalogService);

        // Get all the catalogServiceList where includingVat is not null
        defaultCatalogServiceShouldBeFound("includingVat.specified=true");

        // Get all the catalogServiceList where includingVat is null
        defaultCatalogServiceShouldNotBeFound("includingVat.specified=false");
    }

    @Test
    @Transactional
    void getAllCatalogServicesByVatIsEqualToSomething() throws Exception {
        // Initialize the database
        catalogServiceRepository.saveAndFlush(catalogService);

        // Get all the catalogServiceList where vat equals to DEFAULT_VAT
        defaultCatalogServiceShouldBeFound("vat.equals=" + DEFAULT_VAT);

        // Get all the catalogServiceList where vat equals to UPDATED_VAT
        defaultCatalogServiceShouldNotBeFound("vat.equals=" + UPDATED_VAT);
    }

    @Test
    @Transactional
    void getAllCatalogServicesByVatIsNotEqualToSomething() throws Exception {
        // Initialize the database
        catalogServiceRepository.saveAndFlush(catalogService);

        // Get all the catalogServiceList where vat not equals to DEFAULT_VAT
        defaultCatalogServiceShouldNotBeFound("vat.notEquals=" + DEFAULT_VAT);

        // Get all the catalogServiceList where vat not equals to UPDATED_VAT
        defaultCatalogServiceShouldBeFound("vat.notEquals=" + UPDATED_VAT);
    }

    @Test
    @Transactional
    void getAllCatalogServicesByVatIsInShouldWork() throws Exception {
        // Initialize the database
        catalogServiceRepository.saveAndFlush(catalogService);

        // Get all the catalogServiceList where vat in DEFAULT_VAT or UPDATED_VAT
        defaultCatalogServiceShouldBeFound("vat.in=" + DEFAULT_VAT + "," + UPDATED_VAT);

        // Get all the catalogServiceList where vat equals to UPDATED_VAT
        defaultCatalogServiceShouldNotBeFound("vat.in=" + UPDATED_VAT);
    }

    @Test
    @Transactional
    void getAllCatalogServicesByVatIsNullOrNotNull() throws Exception {
        // Initialize the database
        catalogServiceRepository.saveAndFlush(catalogService);

        // Get all the catalogServiceList where vat is not null
        defaultCatalogServiceShouldBeFound("vat.specified=true");

        // Get all the catalogServiceList where vat is null
        defaultCatalogServiceShouldNotBeFound("vat.specified=false");
    }

    @Test
    @Transactional
    void getAllCatalogServicesByVatIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        catalogServiceRepository.saveAndFlush(catalogService);

        // Get all the catalogServiceList where vat is greater than or equal to DEFAULT_VAT
        defaultCatalogServiceShouldBeFound("vat.greaterThanOrEqual=" + DEFAULT_VAT);

        // Get all the catalogServiceList where vat is greater than or equal to UPDATED_VAT
        defaultCatalogServiceShouldNotBeFound("vat.greaterThanOrEqual=" + UPDATED_VAT);
    }

    @Test
    @Transactional
    void getAllCatalogServicesByVatIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        catalogServiceRepository.saveAndFlush(catalogService);

        // Get all the catalogServiceList where vat is less than or equal to DEFAULT_VAT
        defaultCatalogServiceShouldBeFound("vat.lessThanOrEqual=" + DEFAULT_VAT);

        // Get all the catalogServiceList where vat is less than or equal to SMALLER_VAT
        defaultCatalogServiceShouldNotBeFound("vat.lessThanOrEqual=" + SMALLER_VAT);
    }

    @Test
    @Transactional
    void getAllCatalogServicesByVatIsLessThanSomething() throws Exception {
        // Initialize the database
        catalogServiceRepository.saveAndFlush(catalogService);

        // Get all the catalogServiceList where vat is less than DEFAULT_VAT
        defaultCatalogServiceShouldNotBeFound("vat.lessThan=" + DEFAULT_VAT);

        // Get all the catalogServiceList where vat is less than UPDATED_VAT
        defaultCatalogServiceShouldBeFound("vat.lessThan=" + UPDATED_VAT);
    }

    @Test
    @Transactional
    void getAllCatalogServicesByVatIsGreaterThanSomething() throws Exception {
        // Initialize the database
        catalogServiceRepository.saveAndFlush(catalogService);

        // Get all the catalogServiceList where vat is greater than DEFAULT_VAT
        defaultCatalogServiceShouldNotBeFound("vat.greaterThan=" + DEFAULT_VAT);

        // Get all the catalogServiceList where vat is greater than SMALLER_VAT
        defaultCatalogServiceShouldBeFound("vat.greaterThan=" + SMALLER_VAT);
    }

    @Test
    @Transactional
    void getAllCatalogServicesByUnitNameIsEqualToSomething() throws Exception {
        // Initialize the database
        catalogServiceRepository.saveAndFlush(catalogService);

        // Get all the catalogServiceList where unitName equals to DEFAULT_UNIT_NAME
        defaultCatalogServiceShouldBeFound("unitName.equals=" + DEFAULT_UNIT_NAME);

        // Get all the catalogServiceList where unitName equals to UPDATED_UNIT_NAME
        defaultCatalogServiceShouldNotBeFound("unitName.equals=" + UPDATED_UNIT_NAME);
    }

    @Test
    @Transactional
    void getAllCatalogServicesByUnitNameIsNotEqualToSomething() throws Exception {
        // Initialize the database
        catalogServiceRepository.saveAndFlush(catalogService);

        // Get all the catalogServiceList where unitName not equals to DEFAULT_UNIT_NAME
        defaultCatalogServiceShouldNotBeFound("unitName.notEquals=" + DEFAULT_UNIT_NAME);

        // Get all the catalogServiceList where unitName not equals to UPDATED_UNIT_NAME
        defaultCatalogServiceShouldBeFound("unitName.notEquals=" + UPDATED_UNIT_NAME);
    }

    @Test
    @Transactional
    void getAllCatalogServicesByUnitNameIsInShouldWork() throws Exception {
        // Initialize the database
        catalogServiceRepository.saveAndFlush(catalogService);

        // Get all the catalogServiceList where unitName in DEFAULT_UNIT_NAME or UPDATED_UNIT_NAME
        defaultCatalogServiceShouldBeFound("unitName.in=" + DEFAULT_UNIT_NAME + "," + UPDATED_UNIT_NAME);

        // Get all the catalogServiceList where unitName equals to UPDATED_UNIT_NAME
        defaultCatalogServiceShouldNotBeFound("unitName.in=" + UPDATED_UNIT_NAME);
    }

    @Test
    @Transactional
    void getAllCatalogServicesByUnitNameIsNullOrNotNull() throws Exception {
        // Initialize the database
        catalogServiceRepository.saveAndFlush(catalogService);

        // Get all the catalogServiceList where unitName is not null
        defaultCatalogServiceShouldBeFound("unitName.specified=true");

        // Get all the catalogServiceList where unitName is null
        defaultCatalogServiceShouldNotBeFound("unitName.specified=false");
    }

    @Test
    @Transactional
    void getAllCatalogServicesByUnitNameContainsSomething() throws Exception {
        // Initialize the database
        catalogServiceRepository.saveAndFlush(catalogService);

        // Get all the catalogServiceList where unitName contains DEFAULT_UNIT_NAME
        defaultCatalogServiceShouldBeFound("unitName.contains=" + DEFAULT_UNIT_NAME);

        // Get all the catalogServiceList where unitName contains UPDATED_UNIT_NAME
        defaultCatalogServiceShouldNotBeFound("unitName.contains=" + UPDATED_UNIT_NAME);
    }

    @Test
    @Transactional
    void getAllCatalogServicesByUnitNameNotContainsSomething() throws Exception {
        // Initialize the database
        catalogServiceRepository.saveAndFlush(catalogService);

        // Get all the catalogServiceList where unitName does not contain DEFAULT_UNIT_NAME
        defaultCatalogServiceShouldNotBeFound("unitName.doesNotContain=" + DEFAULT_UNIT_NAME);

        // Get all the catalogServiceList where unitName does not contain UPDATED_UNIT_NAME
        defaultCatalogServiceShouldBeFound("unitName.doesNotContain=" + UPDATED_UNIT_NAME);
    }

    @Test
    @Transactional
    void getAllCatalogServicesByPriceIsEqualToSomething() throws Exception {
        // Initialize the database
        catalogServiceRepository.saveAndFlush(catalogService);

        // Get all the catalogServiceList where price equals to DEFAULT_PRICE
        defaultCatalogServiceShouldBeFound("price.equals=" + DEFAULT_PRICE);

        // Get all the catalogServiceList where price equals to UPDATED_PRICE
        defaultCatalogServiceShouldNotBeFound("price.equals=" + UPDATED_PRICE);
    }

    @Test
    @Transactional
    void getAllCatalogServicesByPriceIsNotEqualToSomething() throws Exception {
        // Initialize the database
        catalogServiceRepository.saveAndFlush(catalogService);

        // Get all the catalogServiceList where price not equals to DEFAULT_PRICE
        defaultCatalogServiceShouldNotBeFound("price.notEquals=" + DEFAULT_PRICE);

        // Get all the catalogServiceList where price not equals to UPDATED_PRICE
        defaultCatalogServiceShouldBeFound("price.notEquals=" + UPDATED_PRICE);
    }

    @Test
    @Transactional
    void getAllCatalogServicesByPriceIsInShouldWork() throws Exception {
        // Initialize the database
        catalogServiceRepository.saveAndFlush(catalogService);

        // Get all the catalogServiceList where price in DEFAULT_PRICE or UPDATED_PRICE
        defaultCatalogServiceShouldBeFound("price.in=" + DEFAULT_PRICE + "," + UPDATED_PRICE);

        // Get all the catalogServiceList where price equals to UPDATED_PRICE
        defaultCatalogServiceShouldNotBeFound("price.in=" + UPDATED_PRICE);
    }

    @Test
    @Transactional
    void getAllCatalogServicesByPriceIsNullOrNotNull() throws Exception {
        // Initialize the database
        catalogServiceRepository.saveAndFlush(catalogService);

        // Get all the catalogServiceList where price is not null
        defaultCatalogServiceShouldBeFound("price.specified=true");

        // Get all the catalogServiceList where price is null
        defaultCatalogServiceShouldNotBeFound("price.specified=false");
    }

    @Test
    @Transactional
    void getAllCatalogServicesByPriceIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        catalogServiceRepository.saveAndFlush(catalogService);

        // Get all the catalogServiceList where price is greater than or equal to DEFAULT_PRICE
        defaultCatalogServiceShouldBeFound("price.greaterThanOrEqual=" + DEFAULT_PRICE);

        // Get all the catalogServiceList where price is greater than or equal to UPDATED_PRICE
        defaultCatalogServiceShouldNotBeFound("price.greaterThanOrEqual=" + UPDATED_PRICE);
    }

    @Test
    @Transactional
    void getAllCatalogServicesByPriceIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        catalogServiceRepository.saveAndFlush(catalogService);

        // Get all the catalogServiceList where price is less than or equal to DEFAULT_PRICE
        defaultCatalogServiceShouldBeFound("price.lessThanOrEqual=" + DEFAULT_PRICE);

        // Get all the catalogServiceList where price is less than or equal to SMALLER_PRICE
        defaultCatalogServiceShouldNotBeFound("price.lessThanOrEqual=" + SMALLER_PRICE);
    }

    @Test
    @Transactional
    void getAllCatalogServicesByPriceIsLessThanSomething() throws Exception {
        // Initialize the database
        catalogServiceRepository.saveAndFlush(catalogService);

        // Get all the catalogServiceList where price is less than DEFAULT_PRICE
        defaultCatalogServiceShouldNotBeFound("price.lessThan=" + DEFAULT_PRICE);

        // Get all the catalogServiceList where price is less than UPDATED_PRICE
        defaultCatalogServiceShouldBeFound("price.lessThan=" + UPDATED_PRICE);
    }

    @Test
    @Transactional
    void getAllCatalogServicesByPriceIsGreaterThanSomething() throws Exception {
        // Initialize the database
        catalogServiceRepository.saveAndFlush(catalogService);

        // Get all the catalogServiceList where price is greater than DEFAULT_PRICE
        defaultCatalogServiceShouldNotBeFound("price.greaterThan=" + DEFAULT_PRICE);

        // Get all the catalogServiceList where price is greater than SMALLER_PRICE
        defaultCatalogServiceShouldBeFound("price.greaterThan=" + SMALLER_PRICE);
    }

    @Test
    @Transactional
    void getAllCatalogServicesByDefaultAmountIsEqualToSomething() throws Exception {
        // Initialize the database
        catalogServiceRepository.saveAndFlush(catalogService);

        // Get all the catalogServiceList where defaultAmount equals to DEFAULT_DEFAULT_AMOUNT
        defaultCatalogServiceShouldBeFound("defaultAmount.equals=" + DEFAULT_DEFAULT_AMOUNT);

        // Get all the catalogServiceList where defaultAmount equals to UPDATED_DEFAULT_AMOUNT
        defaultCatalogServiceShouldNotBeFound("defaultAmount.equals=" + UPDATED_DEFAULT_AMOUNT);
    }

    @Test
    @Transactional
    void getAllCatalogServicesByDefaultAmountIsNotEqualToSomething() throws Exception {
        // Initialize the database
        catalogServiceRepository.saveAndFlush(catalogService);

        // Get all the catalogServiceList where defaultAmount not equals to DEFAULT_DEFAULT_AMOUNT
        defaultCatalogServiceShouldNotBeFound("defaultAmount.notEquals=" + DEFAULT_DEFAULT_AMOUNT);

        // Get all the catalogServiceList where defaultAmount not equals to UPDATED_DEFAULT_AMOUNT
        defaultCatalogServiceShouldBeFound("defaultAmount.notEquals=" + UPDATED_DEFAULT_AMOUNT);
    }

    @Test
    @Transactional
    void getAllCatalogServicesByDefaultAmountIsInShouldWork() throws Exception {
        // Initialize the database
        catalogServiceRepository.saveAndFlush(catalogService);

        // Get all the catalogServiceList where defaultAmount in DEFAULT_DEFAULT_AMOUNT or UPDATED_DEFAULT_AMOUNT
        defaultCatalogServiceShouldBeFound("defaultAmount.in=" + DEFAULT_DEFAULT_AMOUNT + "," + UPDATED_DEFAULT_AMOUNT);

        // Get all the catalogServiceList where defaultAmount equals to UPDATED_DEFAULT_AMOUNT
        defaultCatalogServiceShouldNotBeFound("defaultAmount.in=" + UPDATED_DEFAULT_AMOUNT);
    }

    @Test
    @Transactional
    void getAllCatalogServicesByDefaultAmountIsNullOrNotNull() throws Exception {
        // Initialize the database
        catalogServiceRepository.saveAndFlush(catalogService);

        // Get all the catalogServiceList where defaultAmount is not null
        defaultCatalogServiceShouldBeFound("defaultAmount.specified=true");

        // Get all the catalogServiceList where defaultAmount is null
        defaultCatalogServiceShouldNotBeFound("defaultAmount.specified=false");
    }

    @Test
    @Transactional
    void getAllCatalogServicesByDefaultAmountIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        catalogServiceRepository.saveAndFlush(catalogService);

        // Get all the catalogServiceList where defaultAmount is greater than or equal to DEFAULT_DEFAULT_AMOUNT
        defaultCatalogServiceShouldBeFound("defaultAmount.greaterThanOrEqual=" + DEFAULT_DEFAULT_AMOUNT);

        // Get all the catalogServiceList where defaultAmount is greater than or equal to UPDATED_DEFAULT_AMOUNT
        defaultCatalogServiceShouldNotBeFound("defaultAmount.greaterThanOrEqual=" + UPDATED_DEFAULT_AMOUNT);
    }

    @Test
    @Transactional
    void getAllCatalogServicesByDefaultAmountIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        catalogServiceRepository.saveAndFlush(catalogService);

        // Get all the catalogServiceList where defaultAmount is less than or equal to DEFAULT_DEFAULT_AMOUNT
        defaultCatalogServiceShouldBeFound("defaultAmount.lessThanOrEqual=" + DEFAULT_DEFAULT_AMOUNT);

        // Get all the catalogServiceList where defaultAmount is less than or equal to SMALLER_DEFAULT_AMOUNT
        defaultCatalogServiceShouldNotBeFound("defaultAmount.lessThanOrEqual=" + SMALLER_DEFAULT_AMOUNT);
    }

    @Test
    @Transactional
    void getAllCatalogServicesByDefaultAmountIsLessThanSomething() throws Exception {
        // Initialize the database
        catalogServiceRepository.saveAndFlush(catalogService);

        // Get all the catalogServiceList where defaultAmount is less than DEFAULT_DEFAULT_AMOUNT
        defaultCatalogServiceShouldNotBeFound("defaultAmount.lessThan=" + DEFAULT_DEFAULT_AMOUNT);

        // Get all the catalogServiceList where defaultAmount is less than UPDATED_DEFAULT_AMOUNT
        defaultCatalogServiceShouldBeFound("defaultAmount.lessThan=" + UPDATED_DEFAULT_AMOUNT);
    }

    @Test
    @Transactional
    void getAllCatalogServicesByDefaultAmountIsGreaterThanSomething() throws Exception {
        // Initialize the database
        catalogServiceRepository.saveAndFlush(catalogService);

        // Get all the catalogServiceList where defaultAmount is greater than DEFAULT_DEFAULT_AMOUNT
        defaultCatalogServiceShouldNotBeFound("defaultAmount.greaterThan=" + DEFAULT_DEFAULT_AMOUNT);

        // Get all the catalogServiceList where defaultAmount is greater than SMALLER_DEFAULT_AMOUNT
        defaultCatalogServiceShouldBeFound("defaultAmount.greaterThan=" + SMALLER_DEFAULT_AMOUNT);
    }

    @Test
    @Transactional
    void getAllCatalogServicesByCreatedIsEqualToSomething() throws Exception {
        // Initialize the database
        catalogServiceRepository.saveAndFlush(catalogService);

        // Get all the catalogServiceList where created equals to DEFAULT_CREATED
        defaultCatalogServiceShouldBeFound("created.equals=" + DEFAULT_CREATED);

        // Get all the catalogServiceList where created equals to UPDATED_CREATED
        defaultCatalogServiceShouldNotBeFound("created.equals=" + UPDATED_CREATED);
    }

    @Test
    @Transactional
    void getAllCatalogServicesByCreatedIsNotEqualToSomething() throws Exception {
        // Initialize the database
        catalogServiceRepository.saveAndFlush(catalogService);

        // Get all the catalogServiceList where created not equals to DEFAULT_CREATED
        defaultCatalogServiceShouldNotBeFound("created.notEquals=" + DEFAULT_CREATED);

        // Get all the catalogServiceList where created not equals to UPDATED_CREATED
        defaultCatalogServiceShouldBeFound("created.notEquals=" + UPDATED_CREATED);
    }

    @Test
    @Transactional
    void getAllCatalogServicesByCreatedIsInShouldWork() throws Exception {
        // Initialize the database
        catalogServiceRepository.saveAndFlush(catalogService);

        // Get all the catalogServiceList where created in DEFAULT_CREATED or UPDATED_CREATED
        defaultCatalogServiceShouldBeFound("created.in=" + DEFAULT_CREATED + "," + UPDATED_CREATED);

        // Get all the catalogServiceList where created equals to UPDATED_CREATED
        defaultCatalogServiceShouldNotBeFound("created.in=" + UPDATED_CREATED);
    }

    @Test
    @Transactional
    void getAllCatalogServicesByCreatedIsNullOrNotNull() throws Exception {
        // Initialize the database
        catalogServiceRepository.saveAndFlush(catalogService);

        // Get all the catalogServiceList where created is not null
        defaultCatalogServiceShouldBeFound("created.specified=true");

        // Get all the catalogServiceList where created is null
        defaultCatalogServiceShouldNotBeFound("created.specified=false");
    }

    @Test
    @Transactional
    void getAllCatalogServicesByCreatedIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        catalogServiceRepository.saveAndFlush(catalogService);

        // Get all the catalogServiceList where created is greater than or equal to DEFAULT_CREATED
        defaultCatalogServiceShouldBeFound("created.greaterThanOrEqual=" + DEFAULT_CREATED);

        // Get all the catalogServiceList where created is greater than or equal to UPDATED_CREATED
        defaultCatalogServiceShouldNotBeFound("created.greaterThanOrEqual=" + UPDATED_CREATED);
    }

    @Test
    @Transactional
    void getAllCatalogServicesByCreatedIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        catalogServiceRepository.saveAndFlush(catalogService);

        // Get all the catalogServiceList where created is less than or equal to DEFAULT_CREATED
        defaultCatalogServiceShouldBeFound("created.lessThanOrEqual=" + DEFAULT_CREATED);

        // Get all the catalogServiceList where created is less than or equal to SMALLER_CREATED
        defaultCatalogServiceShouldNotBeFound("created.lessThanOrEqual=" + SMALLER_CREATED);
    }

    @Test
    @Transactional
    void getAllCatalogServicesByCreatedIsLessThanSomething() throws Exception {
        // Initialize the database
        catalogServiceRepository.saveAndFlush(catalogService);

        // Get all the catalogServiceList where created is less than DEFAULT_CREATED
        defaultCatalogServiceShouldNotBeFound("created.lessThan=" + DEFAULT_CREATED);

        // Get all the catalogServiceList where created is less than UPDATED_CREATED
        defaultCatalogServiceShouldBeFound("created.lessThan=" + UPDATED_CREATED);
    }

    @Test
    @Transactional
    void getAllCatalogServicesByCreatedIsGreaterThanSomething() throws Exception {
        // Initialize the database
        catalogServiceRepository.saveAndFlush(catalogService);

        // Get all the catalogServiceList where created is greater than DEFAULT_CREATED
        defaultCatalogServiceShouldNotBeFound("created.greaterThan=" + DEFAULT_CREATED);

        // Get all the catalogServiceList where created is greater than SMALLER_CREATED
        defaultCatalogServiceShouldBeFound("created.greaterThan=" + SMALLER_CREATED);
    }

    @Test
    @Transactional
    void getAllCatalogServicesByInactivIsEqualToSomething() throws Exception {
        // Initialize the database
        catalogServiceRepository.saveAndFlush(catalogService);

        // Get all the catalogServiceList where inactiv equals to DEFAULT_INACTIV
        defaultCatalogServiceShouldBeFound("inactiv.equals=" + DEFAULT_INACTIV);

        // Get all the catalogServiceList where inactiv equals to UPDATED_INACTIV
        defaultCatalogServiceShouldNotBeFound("inactiv.equals=" + UPDATED_INACTIV);
    }

    @Test
    @Transactional
    void getAllCatalogServicesByInactivIsNotEqualToSomething() throws Exception {
        // Initialize the database
        catalogServiceRepository.saveAndFlush(catalogService);

        // Get all the catalogServiceList where inactiv not equals to DEFAULT_INACTIV
        defaultCatalogServiceShouldNotBeFound("inactiv.notEquals=" + DEFAULT_INACTIV);

        // Get all the catalogServiceList where inactiv not equals to UPDATED_INACTIV
        defaultCatalogServiceShouldBeFound("inactiv.notEquals=" + UPDATED_INACTIV);
    }

    @Test
    @Transactional
    void getAllCatalogServicesByInactivIsInShouldWork() throws Exception {
        // Initialize the database
        catalogServiceRepository.saveAndFlush(catalogService);

        // Get all the catalogServiceList where inactiv in DEFAULT_INACTIV or UPDATED_INACTIV
        defaultCatalogServiceShouldBeFound("inactiv.in=" + DEFAULT_INACTIV + "," + UPDATED_INACTIV);

        // Get all the catalogServiceList where inactiv equals to UPDATED_INACTIV
        defaultCatalogServiceShouldNotBeFound("inactiv.in=" + UPDATED_INACTIV);
    }

    @Test
    @Transactional
    void getAllCatalogServicesByInactivIsNullOrNotNull() throws Exception {
        // Initialize the database
        catalogServiceRepository.saveAndFlush(catalogService);

        // Get all the catalogServiceList where inactiv is not null
        defaultCatalogServiceShouldBeFound("inactiv.specified=true");

        // Get all the catalogServiceList where inactiv is null
        defaultCatalogServiceShouldNotBeFound("inactiv.specified=false");
    }

    @Test
    @Transactional
    void getAllCatalogServicesByCustomFieldsIsEqualToSomething() throws Exception {
        // Initialize the database
        catalogServiceRepository.saveAndFlush(catalogService);
        CustomFieldValue customFields = CustomFieldValueResourceIT.createEntity(em);
        em.persist(customFields);
        em.flush();
        catalogService.addCustomFields(customFields);
        catalogServiceRepository.saveAndFlush(catalogService);
        Long customFieldsId = customFields.getId();

        // Get all the catalogServiceList where customFields equals to customFieldsId
        defaultCatalogServiceShouldBeFound("customFieldsId.equals=" + customFieldsId);

        // Get all the catalogServiceList where customFields equals to (customFieldsId + 1)
        defaultCatalogServiceShouldNotBeFound("customFieldsId.equals=" + (customFieldsId + 1));
    }

    @Test
    @Transactional
    void getAllCatalogServicesByCategoryIsEqualToSomething() throws Exception {
        // Initialize the database
        catalogServiceRepository.saveAndFlush(catalogService);
        CatalogCategory category = CatalogCategoryResourceIT.createEntity(em);
        em.persist(category);
        em.flush();
        catalogService.setCategory(category);
        catalogServiceRepository.saveAndFlush(catalogService);
        Long categoryId = category.getId();

        // Get all the catalogServiceList where category equals to categoryId
        defaultCatalogServiceShouldBeFound("categoryId.equals=" + categoryId);

        // Get all the catalogServiceList where category equals to (categoryId + 1)
        defaultCatalogServiceShouldNotBeFound("categoryId.equals=" + (categoryId + 1));
    }

    @Test
    @Transactional
    void getAllCatalogServicesByUnitIsEqualToSomething() throws Exception {
        // Initialize the database
        catalogServiceRepository.saveAndFlush(catalogService);
        CatalogUnit unit = CatalogUnitResourceIT.createEntity(em);
        em.persist(unit);
        em.flush();
        catalogService.setUnit(unit);
        catalogServiceRepository.saveAndFlush(catalogService);
        Long unitId = unit.getId();

        // Get all the catalogServiceList where unit equals to unitId
        defaultCatalogServiceShouldBeFound("unitId.equals=" + unitId);

        // Get all the catalogServiceList where unit equals to (unitId + 1)
        defaultCatalogServiceShouldNotBeFound("unitId.equals=" + (unitId + 1));
    }

    @Test
    @Transactional
    void getAllCatalogServicesByValueAddedTaxIsEqualToSomething() throws Exception {
        // Initialize the database
        catalogServiceRepository.saveAndFlush(catalogService);
        ValueAddedTax valueAddedTax = ValueAddedTaxResourceIT.createEntity(em);
        em.persist(valueAddedTax);
        em.flush();
        catalogService.setValueAddedTax(valueAddedTax);
        catalogServiceRepository.saveAndFlush(catalogService);
        Long valueAddedTaxId = valueAddedTax.getId();

        // Get all the catalogServiceList where valueAddedTax equals to valueAddedTaxId
        defaultCatalogServiceShouldBeFound("valueAddedTaxId.equals=" + valueAddedTaxId);

        // Get all the catalogServiceList where valueAddedTax equals to (valueAddedTaxId + 1)
        defaultCatalogServiceShouldNotBeFound("valueAddedTaxId.equals=" + (valueAddedTaxId + 1));
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultCatalogServiceShouldBeFound(String filter) throws Exception {
        restCatalogServiceMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
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

        // Check, that the count call also returns 1
        restCatalogServiceMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultCatalogServiceShouldNotBeFound(String filter) throws Exception {
        restCatalogServiceMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restCatalogServiceMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
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
