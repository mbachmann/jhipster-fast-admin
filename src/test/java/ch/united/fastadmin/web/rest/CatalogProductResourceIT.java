package ch.united.fastadmin.web.rest;

import static ch.united.fastadmin.web.rest.TestUtil.sameInstant;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import ch.united.fastadmin.IntegrationTest;
import ch.united.fastadmin.domain.CatalogProduct;
import ch.united.fastadmin.repository.CatalogProductRepository;
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

    private static final Double DEFAULT_PRICE_PURCHASE = 1D;
    private static final Double UPDATED_PRICE_PURCHASE = 2D;

    private static final Double DEFAULT_INVENTORY_AVAILABLE = 1D;
    private static final Double UPDATED_INVENTORY_AVAILABLE = 2D;

    private static final Double DEFAULT_INVENTORY_RESERVED = 1D;
    private static final Double UPDATED_INVENTORY_RESERVED = 2D;

    private static final Integer DEFAULT_DEFAULT_AMOUNT = 1;
    private static final Integer UPDATED_DEFAULT_AMOUNT = 2;

    private static final ZonedDateTime DEFAULT_CREATED = ZonedDateTime.ofInstant(Instant.ofEpochMilli(0L), ZoneOffset.UTC);
    private static final ZonedDateTime UPDATED_CREATED = ZonedDateTime.now(ZoneId.systemDefault()).withNano(0);

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
