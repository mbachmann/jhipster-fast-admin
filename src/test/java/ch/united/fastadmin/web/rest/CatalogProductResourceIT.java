package ch.united.fastadmin.web.rest;

import static ch.united.fastadmin.web.rest.TestUtil.sameInstant;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import ch.united.fastadmin.IntegrationTest;
import ch.united.fastadmin.domain.CatalogCategory;
import ch.united.fastadmin.domain.CatalogProduct;
import ch.united.fastadmin.domain.CatalogUnit;
import ch.united.fastadmin.domain.CustomFieldValue;
import ch.united.fastadmin.domain.ValueAddedTax;
import ch.united.fastadmin.repository.CatalogProductRepository;
import ch.united.fastadmin.service.criteria.CatalogProductCriteria;
import ch.united.fastadmin.service.dto.CatalogProductDTO;
import ch.united.fastadmin.service.mapper.CatalogProductMapper;
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
 * Integration tests for the {@link CatalogProductResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class CatalogProductResourceIT {

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

    private static final Double DEFAULT_PRICE_PURCHASE = 1D;
    private static final Double UPDATED_PRICE_PURCHASE = 2D;
    private static final Double SMALLER_PRICE_PURCHASE = 1D - 1D;

    private static final Double DEFAULT_INVENTORY_AVAILABLE = 1D;
    private static final Double UPDATED_INVENTORY_AVAILABLE = 2D;
    private static final Double SMALLER_INVENTORY_AVAILABLE = 1D - 1D;

    private static final Double DEFAULT_INVENTORY_RESERVED = 1D;
    private static final Double UPDATED_INVENTORY_RESERVED = 2D;
    private static final Double SMALLER_INVENTORY_RESERVED = 1D - 1D;

    private static final Integer DEFAULT_DEFAULT_AMOUNT = 1;
    private static final Integer UPDATED_DEFAULT_AMOUNT = 2;
    private static final Integer SMALLER_DEFAULT_AMOUNT = 1 - 1;

    private static final ZonedDateTime DEFAULT_CREATED = ZonedDateTime.ofInstant(Instant.ofEpochMilli(0L), ZoneOffset.UTC);
    private static final ZonedDateTime UPDATED_CREATED = ZonedDateTime.now(ZoneId.systemDefault()).withNano(0);
    private static final ZonedDateTime SMALLER_CREATED = ZonedDateTime.ofInstant(Instant.ofEpochMilli(-1L), ZoneOffset.UTC);

    private static final Boolean DEFAULT_INACTIV = false;
    private static final Boolean UPDATED_INACTIV = true;

    private static final String ENTITY_API_URL = "/api/catalog-products";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private CatalogProductRepository catalogProductRepository;

    @Autowired
    private CatalogProductMapper catalogProductMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restCatalogProductMockMvc;

    private CatalogProduct catalogProduct;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static CatalogProduct createEntity(EntityManager em) {
        CatalogProduct catalogProduct = new CatalogProduct()
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
            .pricePurchase(DEFAULT_PRICE_PURCHASE)
            .inventoryAvailable(DEFAULT_INVENTORY_AVAILABLE)
            .inventoryReserved(DEFAULT_INVENTORY_RESERVED)
            .defaultAmount(DEFAULT_DEFAULT_AMOUNT)
            .created(DEFAULT_CREATED)
            .inactiv(DEFAULT_INACTIV);
        return catalogProduct;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static CatalogProduct createUpdatedEntity(EntityManager em) {
        CatalogProduct catalogProduct = new CatalogProduct()
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
            .pricePurchase(UPDATED_PRICE_PURCHASE)
            .inventoryAvailable(UPDATED_INVENTORY_AVAILABLE)
            .inventoryReserved(UPDATED_INVENTORY_RESERVED)
            .defaultAmount(UPDATED_DEFAULT_AMOUNT)
            .created(UPDATED_CREATED)
            .inactiv(UPDATED_INACTIV);
        return catalogProduct;
    }

    @BeforeEach
    public void initTest() {
        catalogProduct = createEntity(em);
    }

    @Test
    @Transactional
    void createCatalogProduct() throws Exception {
        int databaseSizeBeforeCreate = catalogProductRepository.findAll().size();
        // Create the CatalogProduct
        CatalogProductDTO catalogProductDTO = catalogProductMapper.toDto(catalogProduct);
        restCatalogProductMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(catalogProductDTO))
            )
            .andExpect(status().isCreated());

        // Validate the CatalogProduct in the database
        List<CatalogProduct> catalogProductList = catalogProductRepository.findAll();
        assertThat(catalogProductList).hasSize(databaseSizeBeforeCreate + 1);
        CatalogProduct testCatalogProduct = catalogProductList.get(catalogProductList.size() - 1);
        assertThat(testCatalogProduct.getRemoteId()).isEqualTo(DEFAULT_REMOTE_ID);
        assertThat(testCatalogProduct.getNumber()).isEqualTo(DEFAULT_NUMBER);
        assertThat(testCatalogProduct.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testCatalogProduct.getDescription()).isEqualTo(DEFAULT_DESCRIPTION);
        assertThat(testCatalogProduct.getNotes()).isEqualTo(DEFAULT_NOTES);
        assertThat(testCatalogProduct.getCategoryName()).isEqualTo(DEFAULT_CATEGORY_NAME);
        assertThat(testCatalogProduct.getIncludingVat()).isEqualTo(DEFAULT_INCLUDING_VAT);
        assertThat(testCatalogProduct.getVat()).isEqualTo(DEFAULT_VAT);
        assertThat(testCatalogProduct.getUnitName()).isEqualTo(DEFAULT_UNIT_NAME);
        assertThat(testCatalogProduct.getPrice()).isEqualTo(DEFAULT_PRICE);
        assertThat(testCatalogProduct.getPricePurchase()).isEqualTo(DEFAULT_PRICE_PURCHASE);
        assertThat(testCatalogProduct.getInventoryAvailable()).isEqualTo(DEFAULT_INVENTORY_AVAILABLE);
        assertThat(testCatalogProduct.getInventoryReserved()).isEqualTo(DEFAULT_INVENTORY_RESERVED);
        assertThat(testCatalogProduct.getDefaultAmount()).isEqualTo(DEFAULT_DEFAULT_AMOUNT);
        assertThat(testCatalogProduct.getCreated()).isEqualTo(DEFAULT_CREATED);
        assertThat(testCatalogProduct.getInactiv()).isEqualTo(DEFAULT_INACTIV);
    }

    @Test
    @Transactional
    void createCatalogProductWithExistingId() throws Exception {
        // Create the CatalogProduct with an existing ID
        catalogProduct.setId(1L);
        CatalogProductDTO catalogProductDTO = catalogProductMapper.toDto(catalogProduct);

        int databaseSizeBeforeCreate = catalogProductRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restCatalogProductMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(catalogProductDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the CatalogProduct in the database
        List<CatalogProduct> catalogProductList = catalogProductRepository.findAll();
        assertThat(catalogProductList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkNameIsRequired() throws Exception {
        int databaseSizeBeforeTest = catalogProductRepository.findAll().size();
        // set the field null
        catalogProduct.setName(null);

        // Create the CatalogProduct, which fails.
        CatalogProductDTO catalogProductDTO = catalogProductMapper.toDto(catalogProduct);

        restCatalogProductMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(catalogProductDTO))
            )
            .andExpect(status().isBadRequest());

        List<CatalogProduct> catalogProductList = catalogProductRepository.findAll();
        assertThat(catalogProductList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllCatalogProducts() throws Exception {
        // Initialize the database
        catalogProductRepository.saveAndFlush(catalogProduct);

        // Get all the catalogProductList
        restCatalogProductMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(catalogProduct.getId().intValue())))
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
            .andExpect(jsonPath("$.[*].pricePurchase").value(hasItem(DEFAULT_PRICE_PURCHASE.doubleValue())))
            .andExpect(jsonPath("$.[*].inventoryAvailable").value(hasItem(DEFAULT_INVENTORY_AVAILABLE.doubleValue())))
            .andExpect(jsonPath("$.[*].inventoryReserved").value(hasItem(DEFAULT_INVENTORY_RESERVED.doubleValue())))
            .andExpect(jsonPath("$.[*].defaultAmount").value(hasItem(DEFAULT_DEFAULT_AMOUNT)))
            .andExpect(jsonPath("$.[*].created").value(hasItem(sameInstant(DEFAULT_CREATED))))
            .andExpect(jsonPath("$.[*].inactiv").value(hasItem(DEFAULT_INACTIV.booleanValue())));
    }

    @Test
    @Transactional
    void getCatalogProduct() throws Exception {
        // Initialize the database
        catalogProductRepository.saveAndFlush(catalogProduct);

        // Get the catalogProduct
        restCatalogProductMockMvc
            .perform(get(ENTITY_API_URL_ID, catalogProduct.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(catalogProduct.getId().intValue()))
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
            .andExpect(jsonPath("$.pricePurchase").value(DEFAULT_PRICE_PURCHASE.doubleValue()))
            .andExpect(jsonPath("$.inventoryAvailable").value(DEFAULT_INVENTORY_AVAILABLE.doubleValue()))
            .andExpect(jsonPath("$.inventoryReserved").value(DEFAULT_INVENTORY_RESERVED.doubleValue()))
            .andExpect(jsonPath("$.defaultAmount").value(DEFAULT_DEFAULT_AMOUNT))
            .andExpect(jsonPath("$.created").value(sameInstant(DEFAULT_CREATED)))
            .andExpect(jsonPath("$.inactiv").value(DEFAULT_INACTIV.booleanValue()));
    }

    @Test
    @Transactional
    void getCatalogProductsByIdFiltering() throws Exception {
        // Initialize the database
        catalogProductRepository.saveAndFlush(catalogProduct);

        Long id = catalogProduct.getId();

        defaultCatalogProductShouldBeFound("id.equals=" + id);
        defaultCatalogProductShouldNotBeFound("id.notEquals=" + id);

        defaultCatalogProductShouldBeFound("id.greaterThanOrEqual=" + id);
        defaultCatalogProductShouldNotBeFound("id.greaterThan=" + id);

        defaultCatalogProductShouldBeFound("id.lessThanOrEqual=" + id);
        defaultCatalogProductShouldNotBeFound("id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllCatalogProductsByRemoteIdIsEqualToSomething() throws Exception {
        // Initialize the database
        catalogProductRepository.saveAndFlush(catalogProduct);

        // Get all the catalogProductList where remoteId equals to DEFAULT_REMOTE_ID
        defaultCatalogProductShouldBeFound("remoteId.equals=" + DEFAULT_REMOTE_ID);

        // Get all the catalogProductList where remoteId equals to UPDATED_REMOTE_ID
        defaultCatalogProductShouldNotBeFound("remoteId.equals=" + UPDATED_REMOTE_ID);
    }

    @Test
    @Transactional
    void getAllCatalogProductsByRemoteIdIsNotEqualToSomething() throws Exception {
        // Initialize the database
        catalogProductRepository.saveAndFlush(catalogProduct);

        // Get all the catalogProductList where remoteId not equals to DEFAULT_REMOTE_ID
        defaultCatalogProductShouldNotBeFound("remoteId.notEquals=" + DEFAULT_REMOTE_ID);

        // Get all the catalogProductList where remoteId not equals to UPDATED_REMOTE_ID
        defaultCatalogProductShouldBeFound("remoteId.notEquals=" + UPDATED_REMOTE_ID);
    }

    @Test
    @Transactional
    void getAllCatalogProductsByRemoteIdIsInShouldWork() throws Exception {
        // Initialize the database
        catalogProductRepository.saveAndFlush(catalogProduct);

        // Get all the catalogProductList where remoteId in DEFAULT_REMOTE_ID or UPDATED_REMOTE_ID
        defaultCatalogProductShouldBeFound("remoteId.in=" + DEFAULT_REMOTE_ID + "," + UPDATED_REMOTE_ID);

        // Get all the catalogProductList where remoteId equals to UPDATED_REMOTE_ID
        defaultCatalogProductShouldNotBeFound("remoteId.in=" + UPDATED_REMOTE_ID);
    }

    @Test
    @Transactional
    void getAllCatalogProductsByRemoteIdIsNullOrNotNull() throws Exception {
        // Initialize the database
        catalogProductRepository.saveAndFlush(catalogProduct);

        // Get all the catalogProductList where remoteId is not null
        defaultCatalogProductShouldBeFound("remoteId.specified=true");

        // Get all the catalogProductList where remoteId is null
        defaultCatalogProductShouldNotBeFound("remoteId.specified=false");
    }

    @Test
    @Transactional
    void getAllCatalogProductsByRemoteIdIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        catalogProductRepository.saveAndFlush(catalogProduct);

        // Get all the catalogProductList where remoteId is greater than or equal to DEFAULT_REMOTE_ID
        defaultCatalogProductShouldBeFound("remoteId.greaterThanOrEqual=" + DEFAULT_REMOTE_ID);

        // Get all the catalogProductList where remoteId is greater than or equal to UPDATED_REMOTE_ID
        defaultCatalogProductShouldNotBeFound("remoteId.greaterThanOrEqual=" + UPDATED_REMOTE_ID);
    }

    @Test
    @Transactional
    void getAllCatalogProductsByRemoteIdIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        catalogProductRepository.saveAndFlush(catalogProduct);

        // Get all the catalogProductList where remoteId is less than or equal to DEFAULT_REMOTE_ID
        defaultCatalogProductShouldBeFound("remoteId.lessThanOrEqual=" + DEFAULT_REMOTE_ID);

        // Get all the catalogProductList where remoteId is less than or equal to SMALLER_REMOTE_ID
        defaultCatalogProductShouldNotBeFound("remoteId.lessThanOrEqual=" + SMALLER_REMOTE_ID);
    }

    @Test
    @Transactional
    void getAllCatalogProductsByRemoteIdIsLessThanSomething() throws Exception {
        // Initialize the database
        catalogProductRepository.saveAndFlush(catalogProduct);

        // Get all the catalogProductList where remoteId is less than DEFAULT_REMOTE_ID
        defaultCatalogProductShouldNotBeFound("remoteId.lessThan=" + DEFAULT_REMOTE_ID);

        // Get all the catalogProductList where remoteId is less than UPDATED_REMOTE_ID
        defaultCatalogProductShouldBeFound("remoteId.lessThan=" + UPDATED_REMOTE_ID);
    }

    @Test
    @Transactional
    void getAllCatalogProductsByRemoteIdIsGreaterThanSomething() throws Exception {
        // Initialize the database
        catalogProductRepository.saveAndFlush(catalogProduct);

        // Get all the catalogProductList where remoteId is greater than DEFAULT_REMOTE_ID
        defaultCatalogProductShouldNotBeFound("remoteId.greaterThan=" + DEFAULT_REMOTE_ID);

        // Get all the catalogProductList where remoteId is greater than SMALLER_REMOTE_ID
        defaultCatalogProductShouldBeFound("remoteId.greaterThan=" + SMALLER_REMOTE_ID);
    }

    @Test
    @Transactional
    void getAllCatalogProductsByNumberIsEqualToSomething() throws Exception {
        // Initialize the database
        catalogProductRepository.saveAndFlush(catalogProduct);

        // Get all the catalogProductList where number equals to DEFAULT_NUMBER
        defaultCatalogProductShouldBeFound("number.equals=" + DEFAULT_NUMBER);

        // Get all the catalogProductList where number equals to UPDATED_NUMBER
        defaultCatalogProductShouldNotBeFound("number.equals=" + UPDATED_NUMBER);
    }

    @Test
    @Transactional
    void getAllCatalogProductsByNumberIsNotEqualToSomething() throws Exception {
        // Initialize the database
        catalogProductRepository.saveAndFlush(catalogProduct);

        // Get all the catalogProductList where number not equals to DEFAULT_NUMBER
        defaultCatalogProductShouldNotBeFound("number.notEquals=" + DEFAULT_NUMBER);

        // Get all the catalogProductList where number not equals to UPDATED_NUMBER
        defaultCatalogProductShouldBeFound("number.notEquals=" + UPDATED_NUMBER);
    }

    @Test
    @Transactional
    void getAllCatalogProductsByNumberIsInShouldWork() throws Exception {
        // Initialize the database
        catalogProductRepository.saveAndFlush(catalogProduct);

        // Get all the catalogProductList where number in DEFAULT_NUMBER or UPDATED_NUMBER
        defaultCatalogProductShouldBeFound("number.in=" + DEFAULT_NUMBER + "," + UPDATED_NUMBER);

        // Get all the catalogProductList where number equals to UPDATED_NUMBER
        defaultCatalogProductShouldNotBeFound("number.in=" + UPDATED_NUMBER);
    }

    @Test
    @Transactional
    void getAllCatalogProductsByNumberIsNullOrNotNull() throws Exception {
        // Initialize the database
        catalogProductRepository.saveAndFlush(catalogProduct);

        // Get all the catalogProductList where number is not null
        defaultCatalogProductShouldBeFound("number.specified=true");

        // Get all the catalogProductList where number is null
        defaultCatalogProductShouldNotBeFound("number.specified=false");
    }

    @Test
    @Transactional
    void getAllCatalogProductsByNumberContainsSomething() throws Exception {
        // Initialize the database
        catalogProductRepository.saveAndFlush(catalogProduct);

        // Get all the catalogProductList where number contains DEFAULT_NUMBER
        defaultCatalogProductShouldBeFound("number.contains=" + DEFAULT_NUMBER);

        // Get all the catalogProductList where number contains UPDATED_NUMBER
        defaultCatalogProductShouldNotBeFound("number.contains=" + UPDATED_NUMBER);
    }

    @Test
    @Transactional
    void getAllCatalogProductsByNumberNotContainsSomething() throws Exception {
        // Initialize the database
        catalogProductRepository.saveAndFlush(catalogProduct);

        // Get all the catalogProductList where number does not contain DEFAULT_NUMBER
        defaultCatalogProductShouldNotBeFound("number.doesNotContain=" + DEFAULT_NUMBER);

        // Get all the catalogProductList where number does not contain UPDATED_NUMBER
        defaultCatalogProductShouldBeFound("number.doesNotContain=" + UPDATED_NUMBER);
    }

    @Test
    @Transactional
    void getAllCatalogProductsByNameIsEqualToSomething() throws Exception {
        // Initialize the database
        catalogProductRepository.saveAndFlush(catalogProduct);

        // Get all the catalogProductList where name equals to DEFAULT_NAME
        defaultCatalogProductShouldBeFound("name.equals=" + DEFAULT_NAME);

        // Get all the catalogProductList where name equals to UPDATED_NAME
        defaultCatalogProductShouldNotBeFound("name.equals=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllCatalogProductsByNameIsNotEqualToSomething() throws Exception {
        // Initialize the database
        catalogProductRepository.saveAndFlush(catalogProduct);

        // Get all the catalogProductList where name not equals to DEFAULT_NAME
        defaultCatalogProductShouldNotBeFound("name.notEquals=" + DEFAULT_NAME);

        // Get all the catalogProductList where name not equals to UPDATED_NAME
        defaultCatalogProductShouldBeFound("name.notEquals=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllCatalogProductsByNameIsInShouldWork() throws Exception {
        // Initialize the database
        catalogProductRepository.saveAndFlush(catalogProduct);

        // Get all the catalogProductList where name in DEFAULT_NAME or UPDATED_NAME
        defaultCatalogProductShouldBeFound("name.in=" + DEFAULT_NAME + "," + UPDATED_NAME);

        // Get all the catalogProductList where name equals to UPDATED_NAME
        defaultCatalogProductShouldNotBeFound("name.in=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllCatalogProductsByNameIsNullOrNotNull() throws Exception {
        // Initialize the database
        catalogProductRepository.saveAndFlush(catalogProduct);

        // Get all the catalogProductList where name is not null
        defaultCatalogProductShouldBeFound("name.specified=true");

        // Get all the catalogProductList where name is null
        defaultCatalogProductShouldNotBeFound("name.specified=false");
    }

    @Test
    @Transactional
    void getAllCatalogProductsByNameContainsSomething() throws Exception {
        // Initialize the database
        catalogProductRepository.saveAndFlush(catalogProduct);

        // Get all the catalogProductList where name contains DEFAULT_NAME
        defaultCatalogProductShouldBeFound("name.contains=" + DEFAULT_NAME);

        // Get all the catalogProductList where name contains UPDATED_NAME
        defaultCatalogProductShouldNotBeFound("name.contains=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllCatalogProductsByNameNotContainsSomething() throws Exception {
        // Initialize the database
        catalogProductRepository.saveAndFlush(catalogProduct);

        // Get all the catalogProductList where name does not contain DEFAULT_NAME
        defaultCatalogProductShouldNotBeFound("name.doesNotContain=" + DEFAULT_NAME);

        // Get all the catalogProductList where name does not contain UPDATED_NAME
        defaultCatalogProductShouldBeFound("name.doesNotContain=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllCatalogProductsByDescriptionIsEqualToSomething() throws Exception {
        // Initialize the database
        catalogProductRepository.saveAndFlush(catalogProduct);

        // Get all the catalogProductList where description equals to DEFAULT_DESCRIPTION
        defaultCatalogProductShouldBeFound("description.equals=" + DEFAULT_DESCRIPTION);

        // Get all the catalogProductList where description equals to UPDATED_DESCRIPTION
        defaultCatalogProductShouldNotBeFound("description.equals=" + UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    void getAllCatalogProductsByDescriptionIsNotEqualToSomething() throws Exception {
        // Initialize the database
        catalogProductRepository.saveAndFlush(catalogProduct);

        // Get all the catalogProductList where description not equals to DEFAULT_DESCRIPTION
        defaultCatalogProductShouldNotBeFound("description.notEquals=" + DEFAULT_DESCRIPTION);

        // Get all the catalogProductList where description not equals to UPDATED_DESCRIPTION
        defaultCatalogProductShouldBeFound("description.notEquals=" + UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    void getAllCatalogProductsByDescriptionIsInShouldWork() throws Exception {
        // Initialize the database
        catalogProductRepository.saveAndFlush(catalogProduct);

        // Get all the catalogProductList where description in DEFAULT_DESCRIPTION or UPDATED_DESCRIPTION
        defaultCatalogProductShouldBeFound("description.in=" + DEFAULT_DESCRIPTION + "," + UPDATED_DESCRIPTION);

        // Get all the catalogProductList where description equals to UPDATED_DESCRIPTION
        defaultCatalogProductShouldNotBeFound("description.in=" + UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    void getAllCatalogProductsByDescriptionIsNullOrNotNull() throws Exception {
        // Initialize the database
        catalogProductRepository.saveAndFlush(catalogProduct);

        // Get all the catalogProductList where description is not null
        defaultCatalogProductShouldBeFound("description.specified=true");

        // Get all the catalogProductList where description is null
        defaultCatalogProductShouldNotBeFound("description.specified=false");
    }

    @Test
    @Transactional
    void getAllCatalogProductsByDescriptionContainsSomething() throws Exception {
        // Initialize the database
        catalogProductRepository.saveAndFlush(catalogProduct);

        // Get all the catalogProductList where description contains DEFAULT_DESCRIPTION
        defaultCatalogProductShouldBeFound("description.contains=" + DEFAULT_DESCRIPTION);

        // Get all the catalogProductList where description contains UPDATED_DESCRIPTION
        defaultCatalogProductShouldNotBeFound("description.contains=" + UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    void getAllCatalogProductsByDescriptionNotContainsSomething() throws Exception {
        // Initialize the database
        catalogProductRepository.saveAndFlush(catalogProduct);

        // Get all the catalogProductList where description does not contain DEFAULT_DESCRIPTION
        defaultCatalogProductShouldNotBeFound("description.doesNotContain=" + DEFAULT_DESCRIPTION);

        // Get all the catalogProductList where description does not contain UPDATED_DESCRIPTION
        defaultCatalogProductShouldBeFound("description.doesNotContain=" + UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    void getAllCatalogProductsByNotesIsEqualToSomething() throws Exception {
        // Initialize the database
        catalogProductRepository.saveAndFlush(catalogProduct);

        // Get all the catalogProductList where notes equals to DEFAULT_NOTES
        defaultCatalogProductShouldBeFound("notes.equals=" + DEFAULT_NOTES);

        // Get all the catalogProductList where notes equals to UPDATED_NOTES
        defaultCatalogProductShouldNotBeFound("notes.equals=" + UPDATED_NOTES);
    }

    @Test
    @Transactional
    void getAllCatalogProductsByNotesIsNotEqualToSomething() throws Exception {
        // Initialize the database
        catalogProductRepository.saveAndFlush(catalogProduct);

        // Get all the catalogProductList where notes not equals to DEFAULT_NOTES
        defaultCatalogProductShouldNotBeFound("notes.notEquals=" + DEFAULT_NOTES);

        // Get all the catalogProductList where notes not equals to UPDATED_NOTES
        defaultCatalogProductShouldBeFound("notes.notEquals=" + UPDATED_NOTES);
    }

    @Test
    @Transactional
    void getAllCatalogProductsByNotesIsInShouldWork() throws Exception {
        // Initialize the database
        catalogProductRepository.saveAndFlush(catalogProduct);

        // Get all the catalogProductList where notes in DEFAULT_NOTES or UPDATED_NOTES
        defaultCatalogProductShouldBeFound("notes.in=" + DEFAULT_NOTES + "," + UPDATED_NOTES);

        // Get all the catalogProductList where notes equals to UPDATED_NOTES
        defaultCatalogProductShouldNotBeFound("notes.in=" + UPDATED_NOTES);
    }

    @Test
    @Transactional
    void getAllCatalogProductsByNotesIsNullOrNotNull() throws Exception {
        // Initialize the database
        catalogProductRepository.saveAndFlush(catalogProduct);

        // Get all the catalogProductList where notes is not null
        defaultCatalogProductShouldBeFound("notes.specified=true");

        // Get all the catalogProductList where notes is null
        defaultCatalogProductShouldNotBeFound("notes.specified=false");
    }

    @Test
    @Transactional
    void getAllCatalogProductsByNotesContainsSomething() throws Exception {
        // Initialize the database
        catalogProductRepository.saveAndFlush(catalogProduct);

        // Get all the catalogProductList where notes contains DEFAULT_NOTES
        defaultCatalogProductShouldBeFound("notes.contains=" + DEFAULT_NOTES);

        // Get all the catalogProductList where notes contains UPDATED_NOTES
        defaultCatalogProductShouldNotBeFound("notes.contains=" + UPDATED_NOTES);
    }

    @Test
    @Transactional
    void getAllCatalogProductsByNotesNotContainsSomething() throws Exception {
        // Initialize the database
        catalogProductRepository.saveAndFlush(catalogProduct);

        // Get all the catalogProductList where notes does not contain DEFAULT_NOTES
        defaultCatalogProductShouldNotBeFound("notes.doesNotContain=" + DEFAULT_NOTES);

        // Get all the catalogProductList where notes does not contain UPDATED_NOTES
        defaultCatalogProductShouldBeFound("notes.doesNotContain=" + UPDATED_NOTES);
    }

    @Test
    @Transactional
    void getAllCatalogProductsByCategoryNameIsEqualToSomething() throws Exception {
        // Initialize the database
        catalogProductRepository.saveAndFlush(catalogProduct);

        // Get all the catalogProductList where categoryName equals to DEFAULT_CATEGORY_NAME
        defaultCatalogProductShouldBeFound("categoryName.equals=" + DEFAULT_CATEGORY_NAME);

        // Get all the catalogProductList where categoryName equals to UPDATED_CATEGORY_NAME
        defaultCatalogProductShouldNotBeFound("categoryName.equals=" + UPDATED_CATEGORY_NAME);
    }

    @Test
    @Transactional
    void getAllCatalogProductsByCategoryNameIsNotEqualToSomething() throws Exception {
        // Initialize the database
        catalogProductRepository.saveAndFlush(catalogProduct);

        // Get all the catalogProductList where categoryName not equals to DEFAULT_CATEGORY_NAME
        defaultCatalogProductShouldNotBeFound("categoryName.notEquals=" + DEFAULT_CATEGORY_NAME);

        // Get all the catalogProductList where categoryName not equals to UPDATED_CATEGORY_NAME
        defaultCatalogProductShouldBeFound("categoryName.notEquals=" + UPDATED_CATEGORY_NAME);
    }

    @Test
    @Transactional
    void getAllCatalogProductsByCategoryNameIsInShouldWork() throws Exception {
        // Initialize the database
        catalogProductRepository.saveAndFlush(catalogProduct);

        // Get all the catalogProductList where categoryName in DEFAULT_CATEGORY_NAME or UPDATED_CATEGORY_NAME
        defaultCatalogProductShouldBeFound("categoryName.in=" + DEFAULT_CATEGORY_NAME + "," + UPDATED_CATEGORY_NAME);

        // Get all the catalogProductList where categoryName equals to UPDATED_CATEGORY_NAME
        defaultCatalogProductShouldNotBeFound("categoryName.in=" + UPDATED_CATEGORY_NAME);
    }

    @Test
    @Transactional
    void getAllCatalogProductsByCategoryNameIsNullOrNotNull() throws Exception {
        // Initialize the database
        catalogProductRepository.saveAndFlush(catalogProduct);

        // Get all the catalogProductList where categoryName is not null
        defaultCatalogProductShouldBeFound("categoryName.specified=true");

        // Get all the catalogProductList where categoryName is null
        defaultCatalogProductShouldNotBeFound("categoryName.specified=false");
    }

    @Test
    @Transactional
    void getAllCatalogProductsByCategoryNameContainsSomething() throws Exception {
        // Initialize the database
        catalogProductRepository.saveAndFlush(catalogProduct);

        // Get all the catalogProductList where categoryName contains DEFAULT_CATEGORY_NAME
        defaultCatalogProductShouldBeFound("categoryName.contains=" + DEFAULT_CATEGORY_NAME);

        // Get all the catalogProductList where categoryName contains UPDATED_CATEGORY_NAME
        defaultCatalogProductShouldNotBeFound("categoryName.contains=" + UPDATED_CATEGORY_NAME);
    }

    @Test
    @Transactional
    void getAllCatalogProductsByCategoryNameNotContainsSomething() throws Exception {
        // Initialize the database
        catalogProductRepository.saveAndFlush(catalogProduct);

        // Get all the catalogProductList where categoryName does not contain DEFAULT_CATEGORY_NAME
        defaultCatalogProductShouldNotBeFound("categoryName.doesNotContain=" + DEFAULT_CATEGORY_NAME);

        // Get all the catalogProductList where categoryName does not contain UPDATED_CATEGORY_NAME
        defaultCatalogProductShouldBeFound("categoryName.doesNotContain=" + UPDATED_CATEGORY_NAME);
    }

    @Test
    @Transactional
    void getAllCatalogProductsByIncludingVatIsEqualToSomething() throws Exception {
        // Initialize the database
        catalogProductRepository.saveAndFlush(catalogProduct);

        // Get all the catalogProductList where includingVat equals to DEFAULT_INCLUDING_VAT
        defaultCatalogProductShouldBeFound("includingVat.equals=" + DEFAULT_INCLUDING_VAT);

        // Get all the catalogProductList where includingVat equals to UPDATED_INCLUDING_VAT
        defaultCatalogProductShouldNotBeFound("includingVat.equals=" + UPDATED_INCLUDING_VAT);
    }

    @Test
    @Transactional
    void getAllCatalogProductsByIncludingVatIsNotEqualToSomething() throws Exception {
        // Initialize the database
        catalogProductRepository.saveAndFlush(catalogProduct);

        // Get all the catalogProductList where includingVat not equals to DEFAULT_INCLUDING_VAT
        defaultCatalogProductShouldNotBeFound("includingVat.notEquals=" + DEFAULT_INCLUDING_VAT);

        // Get all the catalogProductList where includingVat not equals to UPDATED_INCLUDING_VAT
        defaultCatalogProductShouldBeFound("includingVat.notEquals=" + UPDATED_INCLUDING_VAT);
    }

    @Test
    @Transactional
    void getAllCatalogProductsByIncludingVatIsInShouldWork() throws Exception {
        // Initialize the database
        catalogProductRepository.saveAndFlush(catalogProduct);

        // Get all the catalogProductList where includingVat in DEFAULT_INCLUDING_VAT or UPDATED_INCLUDING_VAT
        defaultCatalogProductShouldBeFound("includingVat.in=" + DEFAULT_INCLUDING_VAT + "," + UPDATED_INCLUDING_VAT);

        // Get all the catalogProductList where includingVat equals to UPDATED_INCLUDING_VAT
        defaultCatalogProductShouldNotBeFound("includingVat.in=" + UPDATED_INCLUDING_VAT);
    }

    @Test
    @Transactional
    void getAllCatalogProductsByIncludingVatIsNullOrNotNull() throws Exception {
        // Initialize the database
        catalogProductRepository.saveAndFlush(catalogProduct);

        // Get all the catalogProductList where includingVat is not null
        defaultCatalogProductShouldBeFound("includingVat.specified=true");

        // Get all the catalogProductList where includingVat is null
        defaultCatalogProductShouldNotBeFound("includingVat.specified=false");
    }

    @Test
    @Transactional
    void getAllCatalogProductsByVatIsEqualToSomething() throws Exception {
        // Initialize the database
        catalogProductRepository.saveAndFlush(catalogProduct);

        // Get all the catalogProductList where vat equals to DEFAULT_VAT
        defaultCatalogProductShouldBeFound("vat.equals=" + DEFAULT_VAT);

        // Get all the catalogProductList where vat equals to UPDATED_VAT
        defaultCatalogProductShouldNotBeFound("vat.equals=" + UPDATED_VAT);
    }

    @Test
    @Transactional
    void getAllCatalogProductsByVatIsNotEqualToSomething() throws Exception {
        // Initialize the database
        catalogProductRepository.saveAndFlush(catalogProduct);

        // Get all the catalogProductList where vat not equals to DEFAULT_VAT
        defaultCatalogProductShouldNotBeFound("vat.notEquals=" + DEFAULT_VAT);

        // Get all the catalogProductList where vat not equals to UPDATED_VAT
        defaultCatalogProductShouldBeFound("vat.notEquals=" + UPDATED_VAT);
    }

    @Test
    @Transactional
    void getAllCatalogProductsByVatIsInShouldWork() throws Exception {
        // Initialize the database
        catalogProductRepository.saveAndFlush(catalogProduct);

        // Get all the catalogProductList where vat in DEFAULT_VAT or UPDATED_VAT
        defaultCatalogProductShouldBeFound("vat.in=" + DEFAULT_VAT + "," + UPDATED_VAT);

        // Get all the catalogProductList where vat equals to UPDATED_VAT
        defaultCatalogProductShouldNotBeFound("vat.in=" + UPDATED_VAT);
    }

    @Test
    @Transactional
    void getAllCatalogProductsByVatIsNullOrNotNull() throws Exception {
        // Initialize the database
        catalogProductRepository.saveAndFlush(catalogProduct);

        // Get all the catalogProductList where vat is not null
        defaultCatalogProductShouldBeFound("vat.specified=true");

        // Get all the catalogProductList where vat is null
        defaultCatalogProductShouldNotBeFound("vat.specified=false");
    }

    @Test
    @Transactional
    void getAllCatalogProductsByVatIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        catalogProductRepository.saveAndFlush(catalogProduct);

        // Get all the catalogProductList where vat is greater than or equal to DEFAULT_VAT
        defaultCatalogProductShouldBeFound("vat.greaterThanOrEqual=" + DEFAULT_VAT);

        // Get all the catalogProductList where vat is greater than or equal to UPDATED_VAT
        defaultCatalogProductShouldNotBeFound("vat.greaterThanOrEqual=" + UPDATED_VAT);
    }

    @Test
    @Transactional
    void getAllCatalogProductsByVatIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        catalogProductRepository.saveAndFlush(catalogProduct);

        // Get all the catalogProductList where vat is less than or equal to DEFAULT_VAT
        defaultCatalogProductShouldBeFound("vat.lessThanOrEqual=" + DEFAULT_VAT);

        // Get all the catalogProductList where vat is less than or equal to SMALLER_VAT
        defaultCatalogProductShouldNotBeFound("vat.lessThanOrEqual=" + SMALLER_VAT);
    }

    @Test
    @Transactional
    void getAllCatalogProductsByVatIsLessThanSomething() throws Exception {
        // Initialize the database
        catalogProductRepository.saveAndFlush(catalogProduct);

        // Get all the catalogProductList where vat is less than DEFAULT_VAT
        defaultCatalogProductShouldNotBeFound("vat.lessThan=" + DEFAULT_VAT);

        // Get all the catalogProductList where vat is less than UPDATED_VAT
        defaultCatalogProductShouldBeFound("vat.lessThan=" + UPDATED_VAT);
    }

    @Test
    @Transactional
    void getAllCatalogProductsByVatIsGreaterThanSomething() throws Exception {
        // Initialize the database
        catalogProductRepository.saveAndFlush(catalogProduct);

        // Get all the catalogProductList where vat is greater than DEFAULT_VAT
        defaultCatalogProductShouldNotBeFound("vat.greaterThan=" + DEFAULT_VAT);

        // Get all the catalogProductList where vat is greater than SMALLER_VAT
        defaultCatalogProductShouldBeFound("vat.greaterThan=" + SMALLER_VAT);
    }

    @Test
    @Transactional
    void getAllCatalogProductsByUnitNameIsEqualToSomething() throws Exception {
        // Initialize the database
        catalogProductRepository.saveAndFlush(catalogProduct);

        // Get all the catalogProductList where unitName equals to DEFAULT_UNIT_NAME
        defaultCatalogProductShouldBeFound("unitName.equals=" + DEFAULT_UNIT_NAME);

        // Get all the catalogProductList where unitName equals to UPDATED_UNIT_NAME
        defaultCatalogProductShouldNotBeFound("unitName.equals=" + UPDATED_UNIT_NAME);
    }

    @Test
    @Transactional
    void getAllCatalogProductsByUnitNameIsNotEqualToSomething() throws Exception {
        // Initialize the database
        catalogProductRepository.saveAndFlush(catalogProduct);

        // Get all the catalogProductList where unitName not equals to DEFAULT_UNIT_NAME
        defaultCatalogProductShouldNotBeFound("unitName.notEquals=" + DEFAULT_UNIT_NAME);

        // Get all the catalogProductList where unitName not equals to UPDATED_UNIT_NAME
        defaultCatalogProductShouldBeFound("unitName.notEquals=" + UPDATED_UNIT_NAME);
    }

    @Test
    @Transactional
    void getAllCatalogProductsByUnitNameIsInShouldWork() throws Exception {
        // Initialize the database
        catalogProductRepository.saveAndFlush(catalogProduct);

        // Get all the catalogProductList where unitName in DEFAULT_UNIT_NAME or UPDATED_UNIT_NAME
        defaultCatalogProductShouldBeFound("unitName.in=" + DEFAULT_UNIT_NAME + "," + UPDATED_UNIT_NAME);

        // Get all the catalogProductList where unitName equals to UPDATED_UNIT_NAME
        defaultCatalogProductShouldNotBeFound("unitName.in=" + UPDATED_UNIT_NAME);
    }

    @Test
    @Transactional
    void getAllCatalogProductsByUnitNameIsNullOrNotNull() throws Exception {
        // Initialize the database
        catalogProductRepository.saveAndFlush(catalogProduct);

        // Get all the catalogProductList where unitName is not null
        defaultCatalogProductShouldBeFound("unitName.specified=true");

        // Get all the catalogProductList where unitName is null
        defaultCatalogProductShouldNotBeFound("unitName.specified=false");
    }

    @Test
    @Transactional
    void getAllCatalogProductsByUnitNameContainsSomething() throws Exception {
        // Initialize the database
        catalogProductRepository.saveAndFlush(catalogProduct);

        // Get all the catalogProductList where unitName contains DEFAULT_UNIT_NAME
        defaultCatalogProductShouldBeFound("unitName.contains=" + DEFAULT_UNIT_NAME);

        // Get all the catalogProductList where unitName contains UPDATED_UNIT_NAME
        defaultCatalogProductShouldNotBeFound("unitName.contains=" + UPDATED_UNIT_NAME);
    }

    @Test
    @Transactional
    void getAllCatalogProductsByUnitNameNotContainsSomething() throws Exception {
        // Initialize the database
        catalogProductRepository.saveAndFlush(catalogProduct);

        // Get all the catalogProductList where unitName does not contain DEFAULT_UNIT_NAME
        defaultCatalogProductShouldNotBeFound("unitName.doesNotContain=" + DEFAULT_UNIT_NAME);

        // Get all the catalogProductList where unitName does not contain UPDATED_UNIT_NAME
        defaultCatalogProductShouldBeFound("unitName.doesNotContain=" + UPDATED_UNIT_NAME);
    }

    @Test
    @Transactional
    void getAllCatalogProductsByPriceIsEqualToSomething() throws Exception {
        // Initialize the database
        catalogProductRepository.saveAndFlush(catalogProduct);

        // Get all the catalogProductList where price equals to DEFAULT_PRICE
        defaultCatalogProductShouldBeFound("price.equals=" + DEFAULT_PRICE);

        // Get all the catalogProductList where price equals to UPDATED_PRICE
        defaultCatalogProductShouldNotBeFound("price.equals=" + UPDATED_PRICE);
    }

    @Test
    @Transactional
    void getAllCatalogProductsByPriceIsNotEqualToSomething() throws Exception {
        // Initialize the database
        catalogProductRepository.saveAndFlush(catalogProduct);

        // Get all the catalogProductList where price not equals to DEFAULT_PRICE
        defaultCatalogProductShouldNotBeFound("price.notEquals=" + DEFAULT_PRICE);

        // Get all the catalogProductList where price not equals to UPDATED_PRICE
        defaultCatalogProductShouldBeFound("price.notEquals=" + UPDATED_PRICE);
    }

    @Test
    @Transactional
    void getAllCatalogProductsByPriceIsInShouldWork() throws Exception {
        // Initialize the database
        catalogProductRepository.saveAndFlush(catalogProduct);

        // Get all the catalogProductList where price in DEFAULT_PRICE or UPDATED_PRICE
        defaultCatalogProductShouldBeFound("price.in=" + DEFAULT_PRICE + "," + UPDATED_PRICE);

        // Get all the catalogProductList where price equals to UPDATED_PRICE
        defaultCatalogProductShouldNotBeFound("price.in=" + UPDATED_PRICE);
    }

    @Test
    @Transactional
    void getAllCatalogProductsByPriceIsNullOrNotNull() throws Exception {
        // Initialize the database
        catalogProductRepository.saveAndFlush(catalogProduct);

        // Get all the catalogProductList where price is not null
        defaultCatalogProductShouldBeFound("price.specified=true");

        // Get all the catalogProductList where price is null
        defaultCatalogProductShouldNotBeFound("price.specified=false");
    }

    @Test
    @Transactional
    void getAllCatalogProductsByPriceIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        catalogProductRepository.saveAndFlush(catalogProduct);

        // Get all the catalogProductList where price is greater than or equal to DEFAULT_PRICE
        defaultCatalogProductShouldBeFound("price.greaterThanOrEqual=" + DEFAULT_PRICE);

        // Get all the catalogProductList where price is greater than or equal to UPDATED_PRICE
        defaultCatalogProductShouldNotBeFound("price.greaterThanOrEqual=" + UPDATED_PRICE);
    }

    @Test
    @Transactional
    void getAllCatalogProductsByPriceIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        catalogProductRepository.saveAndFlush(catalogProduct);

        // Get all the catalogProductList where price is less than or equal to DEFAULT_PRICE
        defaultCatalogProductShouldBeFound("price.lessThanOrEqual=" + DEFAULT_PRICE);

        // Get all the catalogProductList where price is less than or equal to SMALLER_PRICE
        defaultCatalogProductShouldNotBeFound("price.lessThanOrEqual=" + SMALLER_PRICE);
    }

    @Test
    @Transactional
    void getAllCatalogProductsByPriceIsLessThanSomething() throws Exception {
        // Initialize the database
        catalogProductRepository.saveAndFlush(catalogProduct);

        // Get all the catalogProductList where price is less than DEFAULT_PRICE
        defaultCatalogProductShouldNotBeFound("price.lessThan=" + DEFAULT_PRICE);

        // Get all the catalogProductList where price is less than UPDATED_PRICE
        defaultCatalogProductShouldBeFound("price.lessThan=" + UPDATED_PRICE);
    }

    @Test
    @Transactional
    void getAllCatalogProductsByPriceIsGreaterThanSomething() throws Exception {
        // Initialize the database
        catalogProductRepository.saveAndFlush(catalogProduct);

        // Get all the catalogProductList where price is greater than DEFAULT_PRICE
        defaultCatalogProductShouldNotBeFound("price.greaterThan=" + DEFAULT_PRICE);

        // Get all the catalogProductList where price is greater than SMALLER_PRICE
        defaultCatalogProductShouldBeFound("price.greaterThan=" + SMALLER_PRICE);
    }

    @Test
    @Transactional
    void getAllCatalogProductsByPricePurchaseIsEqualToSomething() throws Exception {
        // Initialize the database
        catalogProductRepository.saveAndFlush(catalogProduct);

        // Get all the catalogProductList where pricePurchase equals to DEFAULT_PRICE_PURCHASE
        defaultCatalogProductShouldBeFound("pricePurchase.equals=" + DEFAULT_PRICE_PURCHASE);

        // Get all the catalogProductList where pricePurchase equals to UPDATED_PRICE_PURCHASE
        defaultCatalogProductShouldNotBeFound("pricePurchase.equals=" + UPDATED_PRICE_PURCHASE);
    }

    @Test
    @Transactional
    void getAllCatalogProductsByPricePurchaseIsNotEqualToSomething() throws Exception {
        // Initialize the database
        catalogProductRepository.saveAndFlush(catalogProduct);

        // Get all the catalogProductList where pricePurchase not equals to DEFAULT_PRICE_PURCHASE
        defaultCatalogProductShouldNotBeFound("pricePurchase.notEquals=" + DEFAULT_PRICE_PURCHASE);

        // Get all the catalogProductList where pricePurchase not equals to UPDATED_PRICE_PURCHASE
        defaultCatalogProductShouldBeFound("pricePurchase.notEquals=" + UPDATED_PRICE_PURCHASE);
    }

    @Test
    @Transactional
    void getAllCatalogProductsByPricePurchaseIsInShouldWork() throws Exception {
        // Initialize the database
        catalogProductRepository.saveAndFlush(catalogProduct);

        // Get all the catalogProductList where pricePurchase in DEFAULT_PRICE_PURCHASE or UPDATED_PRICE_PURCHASE
        defaultCatalogProductShouldBeFound("pricePurchase.in=" + DEFAULT_PRICE_PURCHASE + "," + UPDATED_PRICE_PURCHASE);

        // Get all the catalogProductList where pricePurchase equals to UPDATED_PRICE_PURCHASE
        defaultCatalogProductShouldNotBeFound("pricePurchase.in=" + UPDATED_PRICE_PURCHASE);
    }

    @Test
    @Transactional
    void getAllCatalogProductsByPricePurchaseIsNullOrNotNull() throws Exception {
        // Initialize the database
        catalogProductRepository.saveAndFlush(catalogProduct);

        // Get all the catalogProductList where pricePurchase is not null
        defaultCatalogProductShouldBeFound("pricePurchase.specified=true");

        // Get all the catalogProductList where pricePurchase is null
        defaultCatalogProductShouldNotBeFound("pricePurchase.specified=false");
    }

    @Test
    @Transactional
    void getAllCatalogProductsByPricePurchaseIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        catalogProductRepository.saveAndFlush(catalogProduct);

        // Get all the catalogProductList where pricePurchase is greater than or equal to DEFAULT_PRICE_PURCHASE
        defaultCatalogProductShouldBeFound("pricePurchase.greaterThanOrEqual=" + DEFAULT_PRICE_PURCHASE);

        // Get all the catalogProductList where pricePurchase is greater than or equal to UPDATED_PRICE_PURCHASE
        defaultCatalogProductShouldNotBeFound("pricePurchase.greaterThanOrEqual=" + UPDATED_PRICE_PURCHASE);
    }

    @Test
    @Transactional
    void getAllCatalogProductsByPricePurchaseIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        catalogProductRepository.saveAndFlush(catalogProduct);

        // Get all the catalogProductList where pricePurchase is less than or equal to DEFAULT_PRICE_PURCHASE
        defaultCatalogProductShouldBeFound("pricePurchase.lessThanOrEqual=" + DEFAULT_PRICE_PURCHASE);

        // Get all the catalogProductList where pricePurchase is less than or equal to SMALLER_PRICE_PURCHASE
        defaultCatalogProductShouldNotBeFound("pricePurchase.lessThanOrEqual=" + SMALLER_PRICE_PURCHASE);
    }

    @Test
    @Transactional
    void getAllCatalogProductsByPricePurchaseIsLessThanSomething() throws Exception {
        // Initialize the database
        catalogProductRepository.saveAndFlush(catalogProduct);

        // Get all the catalogProductList where pricePurchase is less than DEFAULT_PRICE_PURCHASE
        defaultCatalogProductShouldNotBeFound("pricePurchase.lessThan=" + DEFAULT_PRICE_PURCHASE);

        // Get all the catalogProductList where pricePurchase is less than UPDATED_PRICE_PURCHASE
        defaultCatalogProductShouldBeFound("pricePurchase.lessThan=" + UPDATED_PRICE_PURCHASE);
    }

    @Test
    @Transactional
    void getAllCatalogProductsByPricePurchaseIsGreaterThanSomething() throws Exception {
        // Initialize the database
        catalogProductRepository.saveAndFlush(catalogProduct);

        // Get all the catalogProductList where pricePurchase is greater than DEFAULT_PRICE_PURCHASE
        defaultCatalogProductShouldNotBeFound("pricePurchase.greaterThan=" + DEFAULT_PRICE_PURCHASE);

        // Get all the catalogProductList where pricePurchase is greater than SMALLER_PRICE_PURCHASE
        defaultCatalogProductShouldBeFound("pricePurchase.greaterThan=" + SMALLER_PRICE_PURCHASE);
    }

    @Test
    @Transactional
    void getAllCatalogProductsByInventoryAvailableIsEqualToSomething() throws Exception {
        // Initialize the database
        catalogProductRepository.saveAndFlush(catalogProduct);

        // Get all the catalogProductList where inventoryAvailable equals to DEFAULT_INVENTORY_AVAILABLE
        defaultCatalogProductShouldBeFound("inventoryAvailable.equals=" + DEFAULT_INVENTORY_AVAILABLE);

        // Get all the catalogProductList where inventoryAvailable equals to UPDATED_INVENTORY_AVAILABLE
        defaultCatalogProductShouldNotBeFound("inventoryAvailable.equals=" + UPDATED_INVENTORY_AVAILABLE);
    }

    @Test
    @Transactional
    void getAllCatalogProductsByInventoryAvailableIsNotEqualToSomething() throws Exception {
        // Initialize the database
        catalogProductRepository.saveAndFlush(catalogProduct);

        // Get all the catalogProductList where inventoryAvailable not equals to DEFAULT_INVENTORY_AVAILABLE
        defaultCatalogProductShouldNotBeFound("inventoryAvailable.notEquals=" + DEFAULT_INVENTORY_AVAILABLE);

        // Get all the catalogProductList where inventoryAvailable not equals to UPDATED_INVENTORY_AVAILABLE
        defaultCatalogProductShouldBeFound("inventoryAvailable.notEquals=" + UPDATED_INVENTORY_AVAILABLE);
    }

    @Test
    @Transactional
    void getAllCatalogProductsByInventoryAvailableIsInShouldWork() throws Exception {
        // Initialize the database
        catalogProductRepository.saveAndFlush(catalogProduct);

        // Get all the catalogProductList where inventoryAvailable in DEFAULT_INVENTORY_AVAILABLE or UPDATED_INVENTORY_AVAILABLE
        defaultCatalogProductShouldBeFound("inventoryAvailable.in=" + DEFAULT_INVENTORY_AVAILABLE + "," + UPDATED_INVENTORY_AVAILABLE);

        // Get all the catalogProductList where inventoryAvailable equals to UPDATED_INVENTORY_AVAILABLE
        defaultCatalogProductShouldNotBeFound("inventoryAvailable.in=" + UPDATED_INVENTORY_AVAILABLE);
    }

    @Test
    @Transactional
    void getAllCatalogProductsByInventoryAvailableIsNullOrNotNull() throws Exception {
        // Initialize the database
        catalogProductRepository.saveAndFlush(catalogProduct);

        // Get all the catalogProductList where inventoryAvailable is not null
        defaultCatalogProductShouldBeFound("inventoryAvailable.specified=true");

        // Get all the catalogProductList where inventoryAvailable is null
        defaultCatalogProductShouldNotBeFound("inventoryAvailable.specified=false");
    }

    @Test
    @Transactional
    void getAllCatalogProductsByInventoryAvailableIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        catalogProductRepository.saveAndFlush(catalogProduct);

        // Get all the catalogProductList where inventoryAvailable is greater than or equal to DEFAULT_INVENTORY_AVAILABLE
        defaultCatalogProductShouldBeFound("inventoryAvailable.greaterThanOrEqual=" + DEFAULT_INVENTORY_AVAILABLE);

        // Get all the catalogProductList where inventoryAvailable is greater than or equal to UPDATED_INVENTORY_AVAILABLE
        defaultCatalogProductShouldNotBeFound("inventoryAvailable.greaterThanOrEqual=" + UPDATED_INVENTORY_AVAILABLE);
    }

    @Test
    @Transactional
    void getAllCatalogProductsByInventoryAvailableIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        catalogProductRepository.saveAndFlush(catalogProduct);

        // Get all the catalogProductList where inventoryAvailable is less than or equal to DEFAULT_INVENTORY_AVAILABLE
        defaultCatalogProductShouldBeFound("inventoryAvailable.lessThanOrEqual=" + DEFAULT_INVENTORY_AVAILABLE);

        // Get all the catalogProductList where inventoryAvailable is less than or equal to SMALLER_INVENTORY_AVAILABLE
        defaultCatalogProductShouldNotBeFound("inventoryAvailable.lessThanOrEqual=" + SMALLER_INVENTORY_AVAILABLE);
    }

    @Test
    @Transactional
    void getAllCatalogProductsByInventoryAvailableIsLessThanSomething() throws Exception {
        // Initialize the database
        catalogProductRepository.saveAndFlush(catalogProduct);

        // Get all the catalogProductList where inventoryAvailable is less than DEFAULT_INVENTORY_AVAILABLE
        defaultCatalogProductShouldNotBeFound("inventoryAvailable.lessThan=" + DEFAULT_INVENTORY_AVAILABLE);

        // Get all the catalogProductList where inventoryAvailable is less than UPDATED_INVENTORY_AVAILABLE
        defaultCatalogProductShouldBeFound("inventoryAvailable.lessThan=" + UPDATED_INVENTORY_AVAILABLE);
    }

    @Test
    @Transactional
    void getAllCatalogProductsByInventoryAvailableIsGreaterThanSomething() throws Exception {
        // Initialize the database
        catalogProductRepository.saveAndFlush(catalogProduct);

        // Get all the catalogProductList where inventoryAvailable is greater than DEFAULT_INVENTORY_AVAILABLE
        defaultCatalogProductShouldNotBeFound("inventoryAvailable.greaterThan=" + DEFAULT_INVENTORY_AVAILABLE);

        // Get all the catalogProductList where inventoryAvailable is greater than SMALLER_INVENTORY_AVAILABLE
        defaultCatalogProductShouldBeFound("inventoryAvailable.greaterThan=" + SMALLER_INVENTORY_AVAILABLE);
    }

    @Test
    @Transactional
    void getAllCatalogProductsByInventoryReservedIsEqualToSomething() throws Exception {
        // Initialize the database
        catalogProductRepository.saveAndFlush(catalogProduct);

        // Get all the catalogProductList where inventoryReserved equals to DEFAULT_INVENTORY_RESERVED
        defaultCatalogProductShouldBeFound("inventoryReserved.equals=" + DEFAULT_INVENTORY_RESERVED);

        // Get all the catalogProductList where inventoryReserved equals to UPDATED_INVENTORY_RESERVED
        defaultCatalogProductShouldNotBeFound("inventoryReserved.equals=" + UPDATED_INVENTORY_RESERVED);
    }

    @Test
    @Transactional
    void getAllCatalogProductsByInventoryReservedIsNotEqualToSomething() throws Exception {
        // Initialize the database
        catalogProductRepository.saveAndFlush(catalogProduct);

        // Get all the catalogProductList where inventoryReserved not equals to DEFAULT_INVENTORY_RESERVED
        defaultCatalogProductShouldNotBeFound("inventoryReserved.notEquals=" + DEFAULT_INVENTORY_RESERVED);

        // Get all the catalogProductList where inventoryReserved not equals to UPDATED_INVENTORY_RESERVED
        defaultCatalogProductShouldBeFound("inventoryReserved.notEquals=" + UPDATED_INVENTORY_RESERVED);
    }

    @Test
    @Transactional
    void getAllCatalogProductsByInventoryReservedIsInShouldWork() throws Exception {
        // Initialize the database
        catalogProductRepository.saveAndFlush(catalogProduct);

        // Get all the catalogProductList where inventoryReserved in DEFAULT_INVENTORY_RESERVED or UPDATED_INVENTORY_RESERVED
        defaultCatalogProductShouldBeFound("inventoryReserved.in=" + DEFAULT_INVENTORY_RESERVED + "," + UPDATED_INVENTORY_RESERVED);

        // Get all the catalogProductList where inventoryReserved equals to UPDATED_INVENTORY_RESERVED
        defaultCatalogProductShouldNotBeFound("inventoryReserved.in=" + UPDATED_INVENTORY_RESERVED);
    }

    @Test
    @Transactional
    void getAllCatalogProductsByInventoryReservedIsNullOrNotNull() throws Exception {
        // Initialize the database
        catalogProductRepository.saveAndFlush(catalogProduct);

        // Get all the catalogProductList where inventoryReserved is not null
        defaultCatalogProductShouldBeFound("inventoryReserved.specified=true");

        // Get all the catalogProductList where inventoryReserved is null
        defaultCatalogProductShouldNotBeFound("inventoryReserved.specified=false");
    }

    @Test
    @Transactional
    void getAllCatalogProductsByInventoryReservedIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        catalogProductRepository.saveAndFlush(catalogProduct);

        // Get all the catalogProductList where inventoryReserved is greater than or equal to DEFAULT_INVENTORY_RESERVED
        defaultCatalogProductShouldBeFound("inventoryReserved.greaterThanOrEqual=" + DEFAULT_INVENTORY_RESERVED);

        // Get all the catalogProductList where inventoryReserved is greater than or equal to UPDATED_INVENTORY_RESERVED
        defaultCatalogProductShouldNotBeFound("inventoryReserved.greaterThanOrEqual=" + UPDATED_INVENTORY_RESERVED);
    }

    @Test
    @Transactional
    void getAllCatalogProductsByInventoryReservedIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        catalogProductRepository.saveAndFlush(catalogProduct);

        // Get all the catalogProductList where inventoryReserved is less than or equal to DEFAULT_INVENTORY_RESERVED
        defaultCatalogProductShouldBeFound("inventoryReserved.lessThanOrEqual=" + DEFAULT_INVENTORY_RESERVED);

        // Get all the catalogProductList where inventoryReserved is less than or equal to SMALLER_INVENTORY_RESERVED
        defaultCatalogProductShouldNotBeFound("inventoryReserved.lessThanOrEqual=" + SMALLER_INVENTORY_RESERVED);
    }

    @Test
    @Transactional
    void getAllCatalogProductsByInventoryReservedIsLessThanSomething() throws Exception {
        // Initialize the database
        catalogProductRepository.saveAndFlush(catalogProduct);

        // Get all the catalogProductList where inventoryReserved is less than DEFAULT_INVENTORY_RESERVED
        defaultCatalogProductShouldNotBeFound("inventoryReserved.lessThan=" + DEFAULT_INVENTORY_RESERVED);

        // Get all the catalogProductList where inventoryReserved is less than UPDATED_INVENTORY_RESERVED
        defaultCatalogProductShouldBeFound("inventoryReserved.lessThan=" + UPDATED_INVENTORY_RESERVED);
    }

    @Test
    @Transactional
    void getAllCatalogProductsByInventoryReservedIsGreaterThanSomething() throws Exception {
        // Initialize the database
        catalogProductRepository.saveAndFlush(catalogProduct);

        // Get all the catalogProductList where inventoryReserved is greater than DEFAULT_INVENTORY_RESERVED
        defaultCatalogProductShouldNotBeFound("inventoryReserved.greaterThan=" + DEFAULT_INVENTORY_RESERVED);

        // Get all the catalogProductList where inventoryReserved is greater than SMALLER_INVENTORY_RESERVED
        defaultCatalogProductShouldBeFound("inventoryReserved.greaterThan=" + SMALLER_INVENTORY_RESERVED);
    }

    @Test
    @Transactional
    void getAllCatalogProductsByDefaultAmountIsEqualToSomething() throws Exception {
        // Initialize the database
        catalogProductRepository.saveAndFlush(catalogProduct);

        // Get all the catalogProductList where defaultAmount equals to DEFAULT_DEFAULT_AMOUNT
        defaultCatalogProductShouldBeFound("defaultAmount.equals=" + DEFAULT_DEFAULT_AMOUNT);

        // Get all the catalogProductList where defaultAmount equals to UPDATED_DEFAULT_AMOUNT
        defaultCatalogProductShouldNotBeFound("defaultAmount.equals=" + UPDATED_DEFAULT_AMOUNT);
    }

    @Test
    @Transactional
    void getAllCatalogProductsByDefaultAmountIsNotEqualToSomething() throws Exception {
        // Initialize the database
        catalogProductRepository.saveAndFlush(catalogProduct);

        // Get all the catalogProductList where defaultAmount not equals to DEFAULT_DEFAULT_AMOUNT
        defaultCatalogProductShouldNotBeFound("defaultAmount.notEquals=" + DEFAULT_DEFAULT_AMOUNT);

        // Get all the catalogProductList where defaultAmount not equals to UPDATED_DEFAULT_AMOUNT
        defaultCatalogProductShouldBeFound("defaultAmount.notEquals=" + UPDATED_DEFAULT_AMOUNT);
    }

    @Test
    @Transactional
    void getAllCatalogProductsByDefaultAmountIsInShouldWork() throws Exception {
        // Initialize the database
        catalogProductRepository.saveAndFlush(catalogProduct);

        // Get all the catalogProductList where defaultAmount in DEFAULT_DEFAULT_AMOUNT or UPDATED_DEFAULT_AMOUNT
        defaultCatalogProductShouldBeFound("defaultAmount.in=" + DEFAULT_DEFAULT_AMOUNT + "," + UPDATED_DEFAULT_AMOUNT);

        // Get all the catalogProductList where defaultAmount equals to UPDATED_DEFAULT_AMOUNT
        defaultCatalogProductShouldNotBeFound("defaultAmount.in=" + UPDATED_DEFAULT_AMOUNT);
    }

    @Test
    @Transactional
    void getAllCatalogProductsByDefaultAmountIsNullOrNotNull() throws Exception {
        // Initialize the database
        catalogProductRepository.saveAndFlush(catalogProduct);

        // Get all the catalogProductList where defaultAmount is not null
        defaultCatalogProductShouldBeFound("defaultAmount.specified=true");

        // Get all the catalogProductList where defaultAmount is null
        defaultCatalogProductShouldNotBeFound("defaultAmount.specified=false");
    }

    @Test
    @Transactional
    void getAllCatalogProductsByDefaultAmountIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        catalogProductRepository.saveAndFlush(catalogProduct);

        // Get all the catalogProductList where defaultAmount is greater than or equal to DEFAULT_DEFAULT_AMOUNT
        defaultCatalogProductShouldBeFound("defaultAmount.greaterThanOrEqual=" + DEFAULT_DEFAULT_AMOUNT);

        // Get all the catalogProductList where defaultAmount is greater than or equal to UPDATED_DEFAULT_AMOUNT
        defaultCatalogProductShouldNotBeFound("defaultAmount.greaterThanOrEqual=" + UPDATED_DEFAULT_AMOUNT);
    }

    @Test
    @Transactional
    void getAllCatalogProductsByDefaultAmountIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        catalogProductRepository.saveAndFlush(catalogProduct);

        // Get all the catalogProductList where defaultAmount is less than or equal to DEFAULT_DEFAULT_AMOUNT
        defaultCatalogProductShouldBeFound("defaultAmount.lessThanOrEqual=" + DEFAULT_DEFAULT_AMOUNT);

        // Get all the catalogProductList where defaultAmount is less than or equal to SMALLER_DEFAULT_AMOUNT
        defaultCatalogProductShouldNotBeFound("defaultAmount.lessThanOrEqual=" + SMALLER_DEFAULT_AMOUNT);
    }

    @Test
    @Transactional
    void getAllCatalogProductsByDefaultAmountIsLessThanSomething() throws Exception {
        // Initialize the database
        catalogProductRepository.saveAndFlush(catalogProduct);

        // Get all the catalogProductList where defaultAmount is less than DEFAULT_DEFAULT_AMOUNT
        defaultCatalogProductShouldNotBeFound("defaultAmount.lessThan=" + DEFAULT_DEFAULT_AMOUNT);

        // Get all the catalogProductList where defaultAmount is less than UPDATED_DEFAULT_AMOUNT
        defaultCatalogProductShouldBeFound("defaultAmount.lessThan=" + UPDATED_DEFAULT_AMOUNT);
    }

    @Test
    @Transactional
    void getAllCatalogProductsByDefaultAmountIsGreaterThanSomething() throws Exception {
        // Initialize the database
        catalogProductRepository.saveAndFlush(catalogProduct);

        // Get all the catalogProductList where defaultAmount is greater than DEFAULT_DEFAULT_AMOUNT
        defaultCatalogProductShouldNotBeFound("defaultAmount.greaterThan=" + DEFAULT_DEFAULT_AMOUNT);

        // Get all the catalogProductList where defaultAmount is greater than SMALLER_DEFAULT_AMOUNT
        defaultCatalogProductShouldBeFound("defaultAmount.greaterThan=" + SMALLER_DEFAULT_AMOUNT);
    }

    @Test
    @Transactional
    void getAllCatalogProductsByCreatedIsEqualToSomething() throws Exception {
        // Initialize the database
        catalogProductRepository.saveAndFlush(catalogProduct);

        // Get all the catalogProductList where created equals to DEFAULT_CREATED
        defaultCatalogProductShouldBeFound("created.equals=" + DEFAULT_CREATED);

        // Get all the catalogProductList where created equals to UPDATED_CREATED
        defaultCatalogProductShouldNotBeFound("created.equals=" + UPDATED_CREATED);
    }

    @Test
    @Transactional
    void getAllCatalogProductsByCreatedIsNotEqualToSomething() throws Exception {
        // Initialize the database
        catalogProductRepository.saveAndFlush(catalogProduct);

        // Get all the catalogProductList where created not equals to DEFAULT_CREATED
        defaultCatalogProductShouldNotBeFound("created.notEquals=" + DEFAULT_CREATED);

        // Get all the catalogProductList where created not equals to UPDATED_CREATED
        defaultCatalogProductShouldBeFound("created.notEquals=" + UPDATED_CREATED);
    }

    @Test
    @Transactional
    void getAllCatalogProductsByCreatedIsInShouldWork() throws Exception {
        // Initialize the database
        catalogProductRepository.saveAndFlush(catalogProduct);

        // Get all the catalogProductList where created in DEFAULT_CREATED or UPDATED_CREATED
        defaultCatalogProductShouldBeFound("created.in=" + DEFAULT_CREATED + "," + UPDATED_CREATED);

        // Get all the catalogProductList where created equals to UPDATED_CREATED
        defaultCatalogProductShouldNotBeFound("created.in=" + UPDATED_CREATED);
    }

    @Test
    @Transactional
    void getAllCatalogProductsByCreatedIsNullOrNotNull() throws Exception {
        // Initialize the database
        catalogProductRepository.saveAndFlush(catalogProduct);

        // Get all the catalogProductList where created is not null
        defaultCatalogProductShouldBeFound("created.specified=true");

        // Get all the catalogProductList where created is null
        defaultCatalogProductShouldNotBeFound("created.specified=false");
    }

    @Test
    @Transactional
    void getAllCatalogProductsByCreatedIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        catalogProductRepository.saveAndFlush(catalogProduct);

        // Get all the catalogProductList where created is greater than or equal to DEFAULT_CREATED
        defaultCatalogProductShouldBeFound("created.greaterThanOrEqual=" + DEFAULT_CREATED);

        // Get all the catalogProductList where created is greater than or equal to UPDATED_CREATED
        defaultCatalogProductShouldNotBeFound("created.greaterThanOrEqual=" + UPDATED_CREATED);
    }

    @Test
    @Transactional
    void getAllCatalogProductsByCreatedIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        catalogProductRepository.saveAndFlush(catalogProduct);

        // Get all the catalogProductList where created is less than or equal to DEFAULT_CREATED
        defaultCatalogProductShouldBeFound("created.lessThanOrEqual=" + DEFAULT_CREATED);

        // Get all the catalogProductList where created is less than or equal to SMALLER_CREATED
        defaultCatalogProductShouldNotBeFound("created.lessThanOrEqual=" + SMALLER_CREATED);
    }

    @Test
    @Transactional
    void getAllCatalogProductsByCreatedIsLessThanSomething() throws Exception {
        // Initialize the database
        catalogProductRepository.saveAndFlush(catalogProduct);

        // Get all the catalogProductList where created is less than DEFAULT_CREATED
        defaultCatalogProductShouldNotBeFound("created.lessThan=" + DEFAULT_CREATED);

        // Get all the catalogProductList where created is less than UPDATED_CREATED
        defaultCatalogProductShouldBeFound("created.lessThan=" + UPDATED_CREATED);
    }

    @Test
    @Transactional
    void getAllCatalogProductsByCreatedIsGreaterThanSomething() throws Exception {
        // Initialize the database
        catalogProductRepository.saveAndFlush(catalogProduct);

        // Get all the catalogProductList where created is greater than DEFAULT_CREATED
        defaultCatalogProductShouldNotBeFound("created.greaterThan=" + DEFAULT_CREATED);

        // Get all the catalogProductList where created is greater than SMALLER_CREATED
        defaultCatalogProductShouldBeFound("created.greaterThan=" + SMALLER_CREATED);
    }

    @Test
    @Transactional
    void getAllCatalogProductsByInactivIsEqualToSomething() throws Exception {
        // Initialize the database
        catalogProductRepository.saveAndFlush(catalogProduct);

        // Get all the catalogProductList where inactiv equals to DEFAULT_INACTIV
        defaultCatalogProductShouldBeFound("inactiv.equals=" + DEFAULT_INACTIV);

        // Get all the catalogProductList where inactiv equals to UPDATED_INACTIV
        defaultCatalogProductShouldNotBeFound("inactiv.equals=" + UPDATED_INACTIV);
    }

    @Test
    @Transactional
    void getAllCatalogProductsByInactivIsNotEqualToSomething() throws Exception {
        // Initialize the database
        catalogProductRepository.saveAndFlush(catalogProduct);

        // Get all the catalogProductList where inactiv not equals to DEFAULT_INACTIV
        defaultCatalogProductShouldNotBeFound("inactiv.notEquals=" + DEFAULT_INACTIV);

        // Get all the catalogProductList where inactiv not equals to UPDATED_INACTIV
        defaultCatalogProductShouldBeFound("inactiv.notEquals=" + UPDATED_INACTIV);
    }

    @Test
    @Transactional
    void getAllCatalogProductsByInactivIsInShouldWork() throws Exception {
        // Initialize the database
        catalogProductRepository.saveAndFlush(catalogProduct);

        // Get all the catalogProductList where inactiv in DEFAULT_INACTIV or UPDATED_INACTIV
        defaultCatalogProductShouldBeFound("inactiv.in=" + DEFAULT_INACTIV + "," + UPDATED_INACTIV);

        // Get all the catalogProductList where inactiv equals to UPDATED_INACTIV
        defaultCatalogProductShouldNotBeFound("inactiv.in=" + UPDATED_INACTIV);
    }

    @Test
    @Transactional
    void getAllCatalogProductsByInactivIsNullOrNotNull() throws Exception {
        // Initialize the database
        catalogProductRepository.saveAndFlush(catalogProduct);

        // Get all the catalogProductList where inactiv is not null
        defaultCatalogProductShouldBeFound("inactiv.specified=true");

        // Get all the catalogProductList where inactiv is null
        defaultCatalogProductShouldNotBeFound("inactiv.specified=false");
    }

    @Test
    @Transactional
    void getAllCatalogProductsByCustomFieldsIsEqualToSomething() throws Exception {
        // Initialize the database
        catalogProductRepository.saveAndFlush(catalogProduct);
        CustomFieldValue customFields = CustomFieldValueResourceIT.createEntity(em);
        em.persist(customFields);
        em.flush();
        catalogProduct.addCustomFields(customFields);
        catalogProductRepository.saveAndFlush(catalogProduct);
        Long customFieldsId = customFields.getId();

        // Get all the catalogProductList where customFields equals to customFieldsId
        defaultCatalogProductShouldBeFound("customFieldsId.equals=" + customFieldsId);

        // Get all the catalogProductList where customFields equals to (customFieldsId + 1)
        defaultCatalogProductShouldNotBeFound("customFieldsId.equals=" + (customFieldsId + 1));
    }

    @Test
    @Transactional
    void getAllCatalogProductsByCategoryIsEqualToSomething() throws Exception {
        // Initialize the database
        catalogProductRepository.saveAndFlush(catalogProduct);
        CatalogCategory category = CatalogCategoryResourceIT.createEntity(em);
        em.persist(category);
        em.flush();
        catalogProduct.setCategory(category);
        catalogProductRepository.saveAndFlush(catalogProduct);
        Long categoryId = category.getId();

        // Get all the catalogProductList where category equals to categoryId
        defaultCatalogProductShouldBeFound("categoryId.equals=" + categoryId);

        // Get all the catalogProductList where category equals to (categoryId + 1)
        defaultCatalogProductShouldNotBeFound("categoryId.equals=" + (categoryId + 1));
    }

    @Test
    @Transactional
    void getAllCatalogProductsByUnitIsEqualToSomething() throws Exception {
        // Initialize the database
        catalogProductRepository.saveAndFlush(catalogProduct);
        CatalogUnit unit = CatalogUnitResourceIT.createEntity(em);
        em.persist(unit);
        em.flush();
        catalogProduct.setUnit(unit);
        catalogProductRepository.saveAndFlush(catalogProduct);
        Long unitId = unit.getId();

        // Get all the catalogProductList where unit equals to unitId
        defaultCatalogProductShouldBeFound("unitId.equals=" + unitId);

        // Get all the catalogProductList where unit equals to (unitId + 1)
        defaultCatalogProductShouldNotBeFound("unitId.equals=" + (unitId + 1));
    }

    @Test
    @Transactional
    void getAllCatalogProductsByValueAddedTaxIsEqualToSomething() throws Exception {
        // Initialize the database
        catalogProductRepository.saveAndFlush(catalogProduct);
        ValueAddedTax valueAddedTax = ValueAddedTaxResourceIT.createEntity(em);
        em.persist(valueAddedTax);
        em.flush();
        catalogProduct.setValueAddedTax(valueAddedTax);
        catalogProductRepository.saveAndFlush(catalogProduct);
        Long valueAddedTaxId = valueAddedTax.getId();

        // Get all the catalogProductList where valueAddedTax equals to valueAddedTaxId
        defaultCatalogProductShouldBeFound("valueAddedTaxId.equals=" + valueAddedTaxId);

        // Get all the catalogProductList where valueAddedTax equals to (valueAddedTaxId + 1)
        defaultCatalogProductShouldNotBeFound("valueAddedTaxId.equals=" + (valueAddedTaxId + 1));
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultCatalogProductShouldBeFound(String filter) throws Exception {
        restCatalogProductMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(catalogProduct.getId().intValue())))
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
            .andExpect(jsonPath("$.[*].pricePurchase").value(hasItem(DEFAULT_PRICE_PURCHASE.doubleValue())))
            .andExpect(jsonPath("$.[*].inventoryAvailable").value(hasItem(DEFAULT_INVENTORY_AVAILABLE.doubleValue())))
            .andExpect(jsonPath("$.[*].inventoryReserved").value(hasItem(DEFAULT_INVENTORY_RESERVED.doubleValue())))
            .andExpect(jsonPath("$.[*].defaultAmount").value(hasItem(DEFAULT_DEFAULT_AMOUNT)))
            .andExpect(jsonPath("$.[*].created").value(hasItem(sameInstant(DEFAULT_CREATED))))
            .andExpect(jsonPath("$.[*].inactiv").value(hasItem(DEFAULT_INACTIV.booleanValue())));

        // Check, that the count call also returns 1
        restCatalogProductMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultCatalogProductShouldNotBeFound(String filter) throws Exception {
        restCatalogProductMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restCatalogProductMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingCatalogProduct() throws Exception {
        // Get the catalogProduct
        restCatalogProductMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewCatalogProduct() throws Exception {
        // Initialize the database
        catalogProductRepository.saveAndFlush(catalogProduct);

        int databaseSizeBeforeUpdate = catalogProductRepository.findAll().size();

        // Update the catalogProduct
        CatalogProduct updatedCatalogProduct = catalogProductRepository.findById(catalogProduct.getId()).get();
        // Disconnect from session so that the updates on updatedCatalogProduct are not directly saved in db
        em.detach(updatedCatalogProduct);
        updatedCatalogProduct
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
            .pricePurchase(UPDATED_PRICE_PURCHASE)
            .inventoryAvailable(UPDATED_INVENTORY_AVAILABLE)
            .inventoryReserved(UPDATED_INVENTORY_RESERVED)
            .defaultAmount(UPDATED_DEFAULT_AMOUNT)
            .created(UPDATED_CREATED)
            .inactiv(UPDATED_INACTIV);
        CatalogProductDTO catalogProductDTO = catalogProductMapper.toDto(updatedCatalogProduct);

        restCatalogProductMockMvc
            .perform(
                put(ENTITY_API_URL_ID, catalogProductDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(catalogProductDTO))
            )
            .andExpect(status().isOk());

        // Validate the CatalogProduct in the database
        List<CatalogProduct> catalogProductList = catalogProductRepository.findAll();
        assertThat(catalogProductList).hasSize(databaseSizeBeforeUpdate);
        CatalogProduct testCatalogProduct = catalogProductList.get(catalogProductList.size() - 1);
        assertThat(testCatalogProduct.getRemoteId()).isEqualTo(UPDATED_REMOTE_ID);
        assertThat(testCatalogProduct.getNumber()).isEqualTo(UPDATED_NUMBER);
        assertThat(testCatalogProduct.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testCatalogProduct.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
        assertThat(testCatalogProduct.getNotes()).isEqualTo(UPDATED_NOTES);
        assertThat(testCatalogProduct.getCategoryName()).isEqualTo(UPDATED_CATEGORY_NAME);
        assertThat(testCatalogProduct.getIncludingVat()).isEqualTo(UPDATED_INCLUDING_VAT);
        assertThat(testCatalogProduct.getVat()).isEqualTo(UPDATED_VAT);
        assertThat(testCatalogProduct.getUnitName()).isEqualTo(UPDATED_UNIT_NAME);
        assertThat(testCatalogProduct.getPrice()).isEqualTo(UPDATED_PRICE);
        assertThat(testCatalogProduct.getPricePurchase()).isEqualTo(UPDATED_PRICE_PURCHASE);
        assertThat(testCatalogProduct.getInventoryAvailable()).isEqualTo(UPDATED_INVENTORY_AVAILABLE);
        assertThat(testCatalogProduct.getInventoryReserved()).isEqualTo(UPDATED_INVENTORY_RESERVED);
        assertThat(testCatalogProduct.getDefaultAmount()).isEqualTo(UPDATED_DEFAULT_AMOUNT);
        assertThat(testCatalogProduct.getCreated()).isEqualTo(UPDATED_CREATED);
        assertThat(testCatalogProduct.getInactiv()).isEqualTo(UPDATED_INACTIV);
    }

    @Test
    @Transactional
    void putNonExistingCatalogProduct() throws Exception {
        int databaseSizeBeforeUpdate = catalogProductRepository.findAll().size();
        catalogProduct.setId(count.incrementAndGet());

        // Create the CatalogProduct
        CatalogProductDTO catalogProductDTO = catalogProductMapper.toDto(catalogProduct);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restCatalogProductMockMvc
            .perform(
                put(ENTITY_API_URL_ID, catalogProductDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(catalogProductDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the CatalogProduct in the database
        List<CatalogProduct> catalogProductList = catalogProductRepository.findAll();
        assertThat(catalogProductList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchCatalogProduct() throws Exception {
        int databaseSizeBeforeUpdate = catalogProductRepository.findAll().size();
        catalogProduct.setId(count.incrementAndGet());

        // Create the CatalogProduct
        CatalogProductDTO catalogProductDTO = catalogProductMapper.toDto(catalogProduct);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCatalogProductMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(catalogProductDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the CatalogProduct in the database
        List<CatalogProduct> catalogProductList = catalogProductRepository.findAll();
        assertThat(catalogProductList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamCatalogProduct() throws Exception {
        int databaseSizeBeforeUpdate = catalogProductRepository.findAll().size();
        catalogProduct.setId(count.incrementAndGet());

        // Create the CatalogProduct
        CatalogProductDTO catalogProductDTO = catalogProductMapper.toDto(catalogProduct);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCatalogProductMockMvc
            .perform(
                put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(catalogProductDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the CatalogProduct in the database
        List<CatalogProduct> catalogProductList = catalogProductRepository.findAll();
        assertThat(catalogProductList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateCatalogProductWithPatch() throws Exception {
        // Initialize the database
        catalogProductRepository.saveAndFlush(catalogProduct);

        int databaseSizeBeforeUpdate = catalogProductRepository.findAll().size();

        // Update the catalogProduct using partial update
        CatalogProduct partialUpdatedCatalogProduct = new CatalogProduct();
        partialUpdatedCatalogProduct.setId(catalogProduct.getId());

        partialUpdatedCatalogProduct
            .number(UPDATED_NUMBER)
            .notes(UPDATED_NOTES)
            .categoryName(UPDATED_CATEGORY_NAME)
            .price(UPDATED_PRICE)
            .pricePurchase(UPDATED_PRICE_PURCHASE)
            .inventoryAvailable(UPDATED_INVENTORY_AVAILABLE)
            .defaultAmount(UPDATED_DEFAULT_AMOUNT)
            .created(UPDATED_CREATED);

        restCatalogProductMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedCatalogProduct.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedCatalogProduct))
            )
            .andExpect(status().isOk());

        // Validate the CatalogProduct in the database
        List<CatalogProduct> catalogProductList = catalogProductRepository.findAll();
        assertThat(catalogProductList).hasSize(databaseSizeBeforeUpdate);
        CatalogProduct testCatalogProduct = catalogProductList.get(catalogProductList.size() - 1);
        assertThat(testCatalogProduct.getRemoteId()).isEqualTo(DEFAULT_REMOTE_ID);
        assertThat(testCatalogProduct.getNumber()).isEqualTo(UPDATED_NUMBER);
        assertThat(testCatalogProduct.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testCatalogProduct.getDescription()).isEqualTo(DEFAULT_DESCRIPTION);
        assertThat(testCatalogProduct.getNotes()).isEqualTo(UPDATED_NOTES);
        assertThat(testCatalogProduct.getCategoryName()).isEqualTo(UPDATED_CATEGORY_NAME);
        assertThat(testCatalogProduct.getIncludingVat()).isEqualTo(DEFAULT_INCLUDING_VAT);
        assertThat(testCatalogProduct.getVat()).isEqualTo(DEFAULT_VAT);
        assertThat(testCatalogProduct.getUnitName()).isEqualTo(DEFAULT_UNIT_NAME);
        assertThat(testCatalogProduct.getPrice()).isEqualTo(UPDATED_PRICE);
        assertThat(testCatalogProduct.getPricePurchase()).isEqualTo(UPDATED_PRICE_PURCHASE);
        assertThat(testCatalogProduct.getInventoryAvailable()).isEqualTo(UPDATED_INVENTORY_AVAILABLE);
        assertThat(testCatalogProduct.getInventoryReserved()).isEqualTo(DEFAULT_INVENTORY_RESERVED);
        assertThat(testCatalogProduct.getDefaultAmount()).isEqualTo(UPDATED_DEFAULT_AMOUNT);
        assertThat(testCatalogProduct.getCreated()).isEqualTo(UPDATED_CREATED);
        assertThat(testCatalogProduct.getInactiv()).isEqualTo(DEFAULT_INACTIV);
    }

    @Test
    @Transactional
    void fullUpdateCatalogProductWithPatch() throws Exception {
        // Initialize the database
        catalogProductRepository.saveAndFlush(catalogProduct);

        int databaseSizeBeforeUpdate = catalogProductRepository.findAll().size();

        // Update the catalogProduct using partial update
        CatalogProduct partialUpdatedCatalogProduct = new CatalogProduct();
        partialUpdatedCatalogProduct.setId(catalogProduct.getId());

        partialUpdatedCatalogProduct
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
            .pricePurchase(UPDATED_PRICE_PURCHASE)
            .inventoryAvailable(UPDATED_INVENTORY_AVAILABLE)
            .inventoryReserved(UPDATED_INVENTORY_RESERVED)
            .defaultAmount(UPDATED_DEFAULT_AMOUNT)
            .created(UPDATED_CREATED)
            .inactiv(UPDATED_INACTIV);

        restCatalogProductMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedCatalogProduct.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedCatalogProduct))
            )
            .andExpect(status().isOk());

        // Validate the CatalogProduct in the database
        List<CatalogProduct> catalogProductList = catalogProductRepository.findAll();
        assertThat(catalogProductList).hasSize(databaseSizeBeforeUpdate);
        CatalogProduct testCatalogProduct = catalogProductList.get(catalogProductList.size() - 1);
        assertThat(testCatalogProduct.getRemoteId()).isEqualTo(UPDATED_REMOTE_ID);
        assertThat(testCatalogProduct.getNumber()).isEqualTo(UPDATED_NUMBER);
        assertThat(testCatalogProduct.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testCatalogProduct.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
        assertThat(testCatalogProduct.getNotes()).isEqualTo(UPDATED_NOTES);
        assertThat(testCatalogProduct.getCategoryName()).isEqualTo(UPDATED_CATEGORY_NAME);
        assertThat(testCatalogProduct.getIncludingVat()).isEqualTo(UPDATED_INCLUDING_VAT);
        assertThat(testCatalogProduct.getVat()).isEqualTo(UPDATED_VAT);
        assertThat(testCatalogProduct.getUnitName()).isEqualTo(UPDATED_UNIT_NAME);
        assertThat(testCatalogProduct.getPrice()).isEqualTo(UPDATED_PRICE);
        assertThat(testCatalogProduct.getPricePurchase()).isEqualTo(UPDATED_PRICE_PURCHASE);
        assertThat(testCatalogProduct.getInventoryAvailable()).isEqualTo(UPDATED_INVENTORY_AVAILABLE);
        assertThat(testCatalogProduct.getInventoryReserved()).isEqualTo(UPDATED_INVENTORY_RESERVED);
        assertThat(testCatalogProduct.getDefaultAmount()).isEqualTo(UPDATED_DEFAULT_AMOUNT);
        assertThat(testCatalogProduct.getCreated()).isEqualTo(UPDATED_CREATED);
        assertThat(testCatalogProduct.getInactiv()).isEqualTo(UPDATED_INACTIV);
    }

    @Test
    @Transactional
    void patchNonExistingCatalogProduct() throws Exception {
        int databaseSizeBeforeUpdate = catalogProductRepository.findAll().size();
        catalogProduct.setId(count.incrementAndGet());

        // Create the CatalogProduct
        CatalogProductDTO catalogProductDTO = catalogProductMapper.toDto(catalogProduct);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restCatalogProductMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, catalogProductDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(catalogProductDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the CatalogProduct in the database
        List<CatalogProduct> catalogProductList = catalogProductRepository.findAll();
        assertThat(catalogProductList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchCatalogProduct() throws Exception {
        int databaseSizeBeforeUpdate = catalogProductRepository.findAll().size();
        catalogProduct.setId(count.incrementAndGet());

        // Create the CatalogProduct
        CatalogProductDTO catalogProductDTO = catalogProductMapper.toDto(catalogProduct);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCatalogProductMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(catalogProductDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the CatalogProduct in the database
        List<CatalogProduct> catalogProductList = catalogProductRepository.findAll();
        assertThat(catalogProductList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamCatalogProduct() throws Exception {
        int databaseSizeBeforeUpdate = catalogProductRepository.findAll().size();
        catalogProduct.setId(count.incrementAndGet());

        // Create the CatalogProduct
        CatalogProductDTO catalogProductDTO = catalogProductMapper.toDto(catalogProduct);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCatalogProductMockMvc
            .perform(
                patch(ENTITY_API_URL)
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(catalogProductDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the CatalogProduct in the database
        List<CatalogProduct> catalogProductList = catalogProductRepository.findAll();
        assertThat(catalogProductList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteCatalogProduct() throws Exception {
        // Initialize the database
        catalogProductRepository.saveAndFlush(catalogProduct);

        int databaseSizeBeforeDelete = catalogProductRepository.findAll().size();

        // Delete the catalogProduct
        restCatalogProductMockMvc
            .perform(delete(ENTITY_API_URL_ID, catalogProduct.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<CatalogProduct> catalogProductList = catalogProductRepository.findAll();
        assertThat(catalogProductList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
