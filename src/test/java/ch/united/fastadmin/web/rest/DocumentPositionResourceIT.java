package ch.united.fastadmin.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import ch.united.fastadmin.IntegrationTest;
import ch.united.fastadmin.domain.DocumentPosition;
import ch.united.fastadmin.domain.enumeration.CatalogScope;
import ch.united.fastadmin.domain.enumeration.DiscountType;
import ch.united.fastadmin.domain.enumeration.DocumentPositionType;
import ch.united.fastadmin.repository.DocumentPositionRepository;
import ch.united.fastadmin.service.dto.DocumentPositionDTO;
import ch.united.fastadmin.service.mapper.DocumentPositionMapper;
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
 * Integration tests for the {@link DocumentPositionResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class DocumentPositionResourceIT {

    private static final DocumentPositionType DEFAULT_TYPE = DocumentPositionType.NORMAL;
    private static final DocumentPositionType UPDATED_TYPE = DocumentPositionType.TEXT;

    private static final CatalogScope DEFAULT_CATALOG_TYPE = CatalogScope.SERVICE;
    private static final CatalogScope UPDATED_CATALOG_TYPE = CatalogScope.PRODUCT;

    private static final String DEFAULT_NUMBER = "AAAAAAAAAA";
    private static final String UPDATED_NUMBER = "BBBBBBBBBB";

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_DESCRIPTION = "AAAAAAAAAA";
    private static final String UPDATED_DESCRIPTION = "BBBBBBBBBB";

    private static final Double DEFAULT_PRICE = 1D;
    private static final Double UPDATED_PRICE = 2D;

    private static final Double DEFAULT_VAT = 1D;
    private static final Double UPDATED_VAT = 2D;

    private static final Double DEFAULT_AMOUNT = 1D;
    private static final Double UPDATED_AMOUNT = 2D;

    private static final Double DEFAULT_DISCOUNT_RATE = 1D;
    private static final Double UPDATED_DISCOUNT_RATE = 2D;

    private static final DiscountType DEFAULT_DISCOUNT_TYPE = DiscountType.IN_PERCENT;
    private static final DiscountType UPDATED_DISCOUNT_TYPE = DiscountType.AMOUNT;

    private static final Double DEFAULT_TOTAL = 1D;
    private static final Double UPDATED_TOTAL = 2D;

    private static final Boolean DEFAULT_SHOW_ONLY_TOTAL = false;
    private static final Boolean UPDATED_SHOW_ONLY_TOTAL = true;

    private static final String ENTITY_API_URL = "/api/document-positions";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private DocumentPositionRepository documentPositionRepository;

    @Autowired
    private DocumentPositionMapper documentPositionMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restDocumentPositionMockMvc;

    private DocumentPosition documentPosition;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static DocumentPosition createEntity(EntityManager em) {
        DocumentPosition documentPosition = new DocumentPosition()
            .type(DEFAULT_TYPE)
            .catalogType(DEFAULT_CATALOG_TYPE)
            .number(DEFAULT_NUMBER)
            .name(DEFAULT_NAME)
            .description(DEFAULT_DESCRIPTION)
            .price(DEFAULT_PRICE)
            .vat(DEFAULT_VAT)
            .amount(DEFAULT_AMOUNT)
            .discountRate(DEFAULT_DISCOUNT_RATE)
            .discountType(DEFAULT_DISCOUNT_TYPE)
            .total(DEFAULT_TOTAL)
            .showOnlyTotal(DEFAULT_SHOW_ONLY_TOTAL);
        return documentPosition;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static DocumentPosition createUpdatedEntity(EntityManager em) {
        DocumentPosition documentPosition = new DocumentPosition()
            .type(UPDATED_TYPE)
            .catalogType(UPDATED_CATALOG_TYPE)
            .number(UPDATED_NUMBER)
            .name(UPDATED_NAME)
            .description(UPDATED_DESCRIPTION)
            .price(UPDATED_PRICE)
            .vat(UPDATED_VAT)
            .amount(UPDATED_AMOUNT)
            .discountRate(UPDATED_DISCOUNT_RATE)
            .discountType(UPDATED_DISCOUNT_TYPE)
            .total(UPDATED_TOTAL)
            .showOnlyTotal(UPDATED_SHOW_ONLY_TOTAL);
        return documentPosition;
    }

    @BeforeEach
    public void initTest() {
        documentPosition = createEntity(em);
    }

    @Test
    @Transactional
    void createDocumentPosition() throws Exception {
        int databaseSizeBeforeCreate = documentPositionRepository.findAll().size();
        // Create the DocumentPosition
        DocumentPositionDTO documentPositionDTO = documentPositionMapper.toDto(documentPosition);
        restDocumentPositionMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(documentPositionDTO))
            )
            .andExpect(status().isCreated());

        // Validate the DocumentPosition in the database
        List<DocumentPosition> documentPositionList = documentPositionRepository.findAll();
        assertThat(documentPositionList).hasSize(databaseSizeBeforeCreate + 1);
        DocumentPosition testDocumentPosition = documentPositionList.get(documentPositionList.size() - 1);
        assertThat(testDocumentPosition.getType()).isEqualTo(DEFAULT_TYPE);
        assertThat(testDocumentPosition.getCatalogType()).isEqualTo(DEFAULT_CATALOG_TYPE);
        assertThat(testDocumentPosition.getNumber()).isEqualTo(DEFAULT_NUMBER);
        assertThat(testDocumentPosition.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testDocumentPosition.getDescription()).isEqualTo(DEFAULT_DESCRIPTION);
        assertThat(testDocumentPosition.getPrice()).isEqualTo(DEFAULT_PRICE);
        assertThat(testDocumentPosition.getVat()).isEqualTo(DEFAULT_VAT);
        assertThat(testDocumentPosition.getAmount()).isEqualTo(DEFAULT_AMOUNT);
        assertThat(testDocumentPosition.getDiscountRate()).isEqualTo(DEFAULT_DISCOUNT_RATE);
        assertThat(testDocumentPosition.getDiscountType()).isEqualTo(DEFAULT_DISCOUNT_TYPE);
        assertThat(testDocumentPosition.getTotal()).isEqualTo(DEFAULT_TOTAL);
        assertThat(testDocumentPosition.getShowOnlyTotal()).isEqualTo(DEFAULT_SHOW_ONLY_TOTAL);
    }

    @Test
    @Transactional
    void createDocumentPositionWithExistingId() throws Exception {
        // Create the DocumentPosition with an existing ID
        documentPosition.setId(1L);
        DocumentPositionDTO documentPositionDTO = documentPositionMapper.toDto(documentPosition);

        int databaseSizeBeforeCreate = documentPositionRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restDocumentPositionMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(documentPositionDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the DocumentPosition in the database
        List<DocumentPosition> documentPositionList = documentPositionRepository.findAll();
        assertThat(documentPositionList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllDocumentPositions() throws Exception {
        // Initialize the database
        documentPositionRepository.saveAndFlush(documentPosition);

        // Get all the documentPositionList
        restDocumentPositionMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(documentPosition.getId().intValue())))
            .andExpect(jsonPath("$.[*].type").value(hasItem(DEFAULT_TYPE.toString())))
            .andExpect(jsonPath("$.[*].catalogType").value(hasItem(DEFAULT_CATALOG_TYPE.toString())))
            .andExpect(jsonPath("$.[*].number").value(hasItem(DEFAULT_NUMBER)))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION)))
            .andExpect(jsonPath("$.[*].price").value(hasItem(DEFAULT_PRICE.doubleValue())))
            .andExpect(jsonPath("$.[*].vat").value(hasItem(DEFAULT_VAT.doubleValue())))
            .andExpect(jsonPath("$.[*].amount").value(hasItem(DEFAULT_AMOUNT.doubleValue())))
            .andExpect(jsonPath("$.[*].discountRate").value(hasItem(DEFAULT_DISCOUNT_RATE.doubleValue())))
            .andExpect(jsonPath("$.[*].discountType").value(hasItem(DEFAULT_DISCOUNT_TYPE.toString())))
            .andExpect(jsonPath("$.[*].total").value(hasItem(DEFAULT_TOTAL.doubleValue())))
            .andExpect(jsonPath("$.[*].showOnlyTotal").value(hasItem(DEFAULT_SHOW_ONLY_TOTAL.booleanValue())));
    }

    @Test
    @Transactional
    void getDocumentPosition() throws Exception {
        // Initialize the database
        documentPositionRepository.saveAndFlush(documentPosition);

        // Get the documentPosition
        restDocumentPositionMockMvc
            .perform(get(ENTITY_API_URL_ID, documentPosition.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(documentPosition.getId().intValue()))
            .andExpect(jsonPath("$.type").value(DEFAULT_TYPE.toString()))
            .andExpect(jsonPath("$.catalogType").value(DEFAULT_CATALOG_TYPE.toString()))
            .andExpect(jsonPath("$.number").value(DEFAULT_NUMBER))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME))
            .andExpect(jsonPath("$.description").value(DEFAULT_DESCRIPTION))
            .andExpect(jsonPath("$.price").value(DEFAULT_PRICE.doubleValue()))
            .andExpect(jsonPath("$.vat").value(DEFAULT_VAT.doubleValue()))
            .andExpect(jsonPath("$.amount").value(DEFAULT_AMOUNT.doubleValue()))
            .andExpect(jsonPath("$.discountRate").value(DEFAULT_DISCOUNT_RATE.doubleValue()))
            .andExpect(jsonPath("$.discountType").value(DEFAULT_DISCOUNT_TYPE.toString()))
            .andExpect(jsonPath("$.total").value(DEFAULT_TOTAL.doubleValue()))
            .andExpect(jsonPath("$.showOnlyTotal").value(DEFAULT_SHOW_ONLY_TOTAL.booleanValue()));
    }

    @Test
    @Transactional
    void getNonExistingDocumentPosition() throws Exception {
        // Get the documentPosition
        restDocumentPositionMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewDocumentPosition() throws Exception {
        // Initialize the database
        documentPositionRepository.saveAndFlush(documentPosition);

        int databaseSizeBeforeUpdate = documentPositionRepository.findAll().size();

        // Update the documentPosition
        DocumentPosition updatedDocumentPosition = documentPositionRepository.findById(documentPosition.getId()).get();
        // Disconnect from session so that the updates on updatedDocumentPosition are not directly saved in db
        em.detach(updatedDocumentPosition);
        updatedDocumentPosition
            .type(UPDATED_TYPE)
            .catalogType(UPDATED_CATALOG_TYPE)
            .number(UPDATED_NUMBER)
            .name(UPDATED_NAME)
            .description(UPDATED_DESCRIPTION)
            .price(UPDATED_PRICE)
            .vat(UPDATED_VAT)
            .amount(UPDATED_AMOUNT)
            .discountRate(UPDATED_DISCOUNT_RATE)
            .discountType(UPDATED_DISCOUNT_TYPE)
            .total(UPDATED_TOTAL)
            .showOnlyTotal(UPDATED_SHOW_ONLY_TOTAL);
        DocumentPositionDTO documentPositionDTO = documentPositionMapper.toDto(updatedDocumentPosition);

        restDocumentPositionMockMvc
            .perform(
                put(ENTITY_API_URL_ID, documentPositionDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(documentPositionDTO))
            )
            .andExpect(status().isOk());

        // Validate the DocumentPosition in the database
        List<DocumentPosition> documentPositionList = documentPositionRepository.findAll();
        assertThat(documentPositionList).hasSize(databaseSizeBeforeUpdate);
        DocumentPosition testDocumentPosition = documentPositionList.get(documentPositionList.size() - 1);
        assertThat(testDocumentPosition.getType()).isEqualTo(UPDATED_TYPE);
        assertThat(testDocumentPosition.getCatalogType()).isEqualTo(UPDATED_CATALOG_TYPE);
        assertThat(testDocumentPosition.getNumber()).isEqualTo(UPDATED_NUMBER);
        assertThat(testDocumentPosition.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testDocumentPosition.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
        assertThat(testDocumentPosition.getPrice()).isEqualTo(UPDATED_PRICE);
        assertThat(testDocumentPosition.getVat()).isEqualTo(UPDATED_VAT);
        assertThat(testDocumentPosition.getAmount()).isEqualTo(UPDATED_AMOUNT);
        assertThat(testDocumentPosition.getDiscountRate()).isEqualTo(UPDATED_DISCOUNT_RATE);
        assertThat(testDocumentPosition.getDiscountType()).isEqualTo(UPDATED_DISCOUNT_TYPE);
        assertThat(testDocumentPosition.getTotal()).isEqualTo(UPDATED_TOTAL);
        assertThat(testDocumentPosition.getShowOnlyTotal()).isEqualTo(UPDATED_SHOW_ONLY_TOTAL);
    }

    @Test
    @Transactional
    void putNonExistingDocumentPosition() throws Exception {
        int databaseSizeBeforeUpdate = documentPositionRepository.findAll().size();
        documentPosition.setId(count.incrementAndGet());

        // Create the DocumentPosition
        DocumentPositionDTO documentPositionDTO = documentPositionMapper.toDto(documentPosition);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restDocumentPositionMockMvc
            .perform(
                put(ENTITY_API_URL_ID, documentPositionDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(documentPositionDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the DocumentPosition in the database
        List<DocumentPosition> documentPositionList = documentPositionRepository.findAll();
        assertThat(documentPositionList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchDocumentPosition() throws Exception {
        int databaseSizeBeforeUpdate = documentPositionRepository.findAll().size();
        documentPosition.setId(count.incrementAndGet());

        // Create the DocumentPosition
        DocumentPositionDTO documentPositionDTO = documentPositionMapper.toDto(documentPosition);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restDocumentPositionMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(documentPositionDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the DocumentPosition in the database
        List<DocumentPosition> documentPositionList = documentPositionRepository.findAll();
        assertThat(documentPositionList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamDocumentPosition() throws Exception {
        int databaseSizeBeforeUpdate = documentPositionRepository.findAll().size();
        documentPosition.setId(count.incrementAndGet());

        // Create the DocumentPosition
        DocumentPositionDTO documentPositionDTO = documentPositionMapper.toDto(documentPosition);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restDocumentPositionMockMvc
            .perform(
                put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(documentPositionDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the DocumentPosition in the database
        List<DocumentPosition> documentPositionList = documentPositionRepository.findAll();
        assertThat(documentPositionList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateDocumentPositionWithPatch() throws Exception {
        // Initialize the database
        documentPositionRepository.saveAndFlush(documentPosition);

        int databaseSizeBeforeUpdate = documentPositionRepository.findAll().size();

        // Update the documentPosition using partial update
        DocumentPosition partialUpdatedDocumentPosition = new DocumentPosition();
        partialUpdatedDocumentPosition.setId(documentPosition.getId());

        partialUpdatedDocumentPosition
            .type(UPDATED_TYPE)
            .name(UPDATED_NAME)
            .price(UPDATED_PRICE)
            .vat(UPDATED_VAT)
            .amount(UPDATED_AMOUNT)
            .total(UPDATED_TOTAL);

        restDocumentPositionMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedDocumentPosition.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedDocumentPosition))
            )
            .andExpect(status().isOk());

        // Validate the DocumentPosition in the database
        List<DocumentPosition> documentPositionList = documentPositionRepository.findAll();
        assertThat(documentPositionList).hasSize(databaseSizeBeforeUpdate);
        DocumentPosition testDocumentPosition = documentPositionList.get(documentPositionList.size() - 1);
        assertThat(testDocumentPosition.getType()).isEqualTo(UPDATED_TYPE);
        assertThat(testDocumentPosition.getCatalogType()).isEqualTo(DEFAULT_CATALOG_TYPE);
        assertThat(testDocumentPosition.getNumber()).isEqualTo(DEFAULT_NUMBER);
        assertThat(testDocumentPosition.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testDocumentPosition.getDescription()).isEqualTo(DEFAULT_DESCRIPTION);
        assertThat(testDocumentPosition.getPrice()).isEqualTo(UPDATED_PRICE);
        assertThat(testDocumentPosition.getVat()).isEqualTo(UPDATED_VAT);
        assertThat(testDocumentPosition.getAmount()).isEqualTo(UPDATED_AMOUNT);
        assertThat(testDocumentPosition.getDiscountRate()).isEqualTo(DEFAULT_DISCOUNT_RATE);
        assertThat(testDocumentPosition.getDiscountType()).isEqualTo(DEFAULT_DISCOUNT_TYPE);
        assertThat(testDocumentPosition.getTotal()).isEqualTo(UPDATED_TOTAL);
        assertThat(testDocumentPosition.getShowOnlyTotal()).isEqualTo(DEFAULT_SHOW_ONLY_TOTAL);
    }

    @Test
    @Transactional
    void fullUpdateDocumentPositionWithPatch() throws Exception {
        // Initialize the database
        documentPositionRepository.saveAndFlush(documentPosition);

        int databaseSizeBeforeUpdate = documentPositionRepository.findAll().size();

        // Update the documentPosition using partial update
        DocumentPosition partialUpdatedDocumentPosition = new DocumentPosition();
        partialUpdatedDocumentPosition.setId(documentPosition.getId());

        partialUpdatedDocumentPosition
            .type(UPDATED_TYPE)
            .catalogType(UPDATED_CATALOG_TYPE)
            .number(UPDATED_NUMBER)
            .name(UPDATED_NAME)
            .description(UPDATED_DESCRIPTION)
            .price(UPDATED_PRICE)
            .vat(UPDATED_VAT)
            .amount(UPDATED_AMOUNT)
            .discountRate(UPDATED_DISCOUNT_RATE)
            .discountType(UPDATED_DISCOUNT_TYPE)
            .total(UPDATED_TOTAL)
            .showOnlyTotal(UPDATED_SHOW_ONLY_TOTAL);

        restDocumentPositionMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedDocumentPosition.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedDocumentPosition))
            )
            .andExpect(status().isOk());

        // Validate the DocumentPosition in the database
        List<DocumentPosition> documentPositionList = documentPositionRepository.findAll();
        assertThat(documentPositionList).hasSize(databaseSizeBeforeUpdate);
        DocumentPosition testDocumentPosition = documentPositionList.get(documentPositionList.size() - 1);
        assertThat(testDocumentPosition.getType()).isEqualTo(UPDATED_TYPE);
        assertThat(testDocumentPosition.getCatalogType()).isEqualTo(UPDATED_CATALOG_TYPE);
        assertThat(testDocumentPosition.getNumber()).isEqualTo(UPDATED_NUMBER);
        assertThat(testDocumentPosition.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testDocumentPosition.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
        assertThat(testDocumentPosition.getPrice()).isEqualTo(UPDATED_PRICE);
        assertThat(testDocumentPosition.getVat()).isEqualTo(UPDATED_VAT);
        assertThat(testDocumentPosition.getAmount()).isEqualTo(UPDATED_AMOUNT);
        assertThat(testDocumentPosition.getDiscountRate()).isEqualTo(UPDATED_DISCOUNT_RATE);
        assertThat(testDocumentPosition.getDiscountType()).isEqualTo(UPDATED_DISCOUNT_TYPE);
        assertThat(testDocumentPosition.getTotal()).isEqualTo(UPDATED_TOTAL);
        assertThat(testDocumentPosition.getShowOnlyTotal()).isEqualTo(UPDATED_SHOW_ONLY_TOTAL);
    }

    @Test
    @Transactional
    void patchNonExistingDocumentPosition() throws Exception {
        int databaseSizeBeforeUpdate = documentPositionRepository.findAll().size();
        documentPosition.setId(count.incrementAndGet());

        // Create the DocumentPosition
        DocumentPositionDTO documentPositionDTO = documentPositionMapper.toDto(documentPosition);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restDocumentPositionMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, documentPositionDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(documentPositionDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the DocumentPosition in the database
        List<DocumentPosition> documentPositionList = documentPositionRepository.findAll();
        assertThat(documentPositionList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchDocumentPosition() throws Exception {
        int databaseSizeBeforeUpdate = documentPositionRepository.findAll().size();
        documentPosition.setId(count.incrementAndGet());

        // Create the DocumentPosition
        DocumentPositionDTO documentPositionDTO = documentPositionMapper.toDto(documentPosition);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restDocumentPositionMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(documentPositionDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the DocumentPosition in the database
        List<DocumentPosition> documentPositionList = documentPositionRepository.findAll();
        assertThat(documentPositionList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamDocumentPosition() throws Exception {
        int databaseSizeBeforeUpdate = documentPositionRepository.findAll().size();
        documentPosition.setId(count.incrementAndGet());

        // Create the DocumentPosition
        DocumentPositionDTO documentPositionDTO = documentPositionMapper.toDto(documentPosition);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restDocumentPositionMockMvc
            .perform(
                patch(ENTITY_API_URL)
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(documentPositionDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the DocumentPosition in the database
        List<DocumentPosition> documentPositionList = documentPositionRepository.findAll();
        assertThat(documentPositionList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteDocumentPosition() throws Exception {
        // Initialize the database
        documentPositionRepository.saveAndFlush(documentPosition);

        int databaseSizeBeforeDelete = documentPositionRepository.findAll().size();

        // Delete the documentPosition
        restDocumentPositionMockMvc
            .perform(delete(ENTITY_API_URL_ID, documentPosition.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<DocumentPosition> documentPositionList = documentPositionRepository.findAll();
        assertThat(documentPositionList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
