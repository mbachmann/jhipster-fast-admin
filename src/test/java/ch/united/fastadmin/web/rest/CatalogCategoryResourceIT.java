package ch.united.fastadmin.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import ch.united.fastadmin.IntegrationTest;
import ch.united.fastadmin.domain.CatalogCategory;
import ch.united.fastadmin.repository.CatalogCategoryRepository;
import ch.united.fastadmin.service.dto.CatalogCategoryDTO;
import ch.united.fastadmin.service.mapper.CatalogCategoryMapper;
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
 * Integration tests for the {@link CatalogCategoryResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class CatalogCategoryResourceIT {

    private static final Integer DEFAULT_REMOTE_ID = 1;
    private static final Integer UPDATED_REMOTE_ID = 2;

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_ACCOUNTING_ACCOUNT_NUMBER = "AAAAAAAAAA";
    private static final String UPDATED_ACCOUNTING_ACCOUNT_NUMBER = "BBBBBBBBBB";

    private static final Integer DEFAULT_USAGE = 1;
    private static final Integer UPDATED_USAGE = 2;

    private static final Boolean DEFAULT_INACTIV = false;
    private static final Boolean UPDATED_INACTIV = true;

    private static final String ENTITY_API_URL = "/api/catalog-categories";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private CatalogCategoryRepository catalogCategoryRepository;

    @Autowired
    private CatalogCategoryMapper catalogCategoryMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restCatalogCategoryMockMvc;

    private CatalogCategory catalogCategory;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static CatalogCategory createEntity(EntityManager em) {
        CatalogCategory catalogCategory = new CatalogCategory()
            .remoteId(DEFAULT_REMOTE_ID)
            .name(DEFAULT_NAME)
            .accountingAccountNumber(DEFAULT_ACCOUNTING_ACCOUNT_NUMBER)
            .usage(DEFAULT_USAGE)
            .inactiv(DEFAULT_INACTIV);
        return catalogCategory;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static CatalogCategory createUpdatedEntity(EntityManager em) {
        CatalogCategory catalogCategory = new CatalogCategory()
            .remoteId(UPDATED_REMOTE_ID)
            .name(UPDATED_NAME)
            .accountingAccountNumber(UPDATED_ACCOUNTING_ACCOUNT_NUMBER)
            .usage(UPDATED_USAGE)
            .inactiv(UPDATED_INACTIV);
        return catalogCategory;
    }

    @BeforeEach
    public void initTest() {
        catalogCategory = createEntity(em);
    }

    @Test
    @Transactional
    void createCatalogCategory() throws Exception {
        int databaseSizeBeforeCreate = catalogCategoryRepository.findAll().size();
        // Create the CatalogCategory
        CatalogCategoryDTO catalogCategoryDTO = catalogCategoryMapper.toDto(catalogCategory);
        restCatalogCategoryMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(catalogCategoryDTO))
            )
            .andExpect(status().isCreated());

        // Validate the CatalogCategory in the database
        List<CatalogCategory> catalogCategoryList = catalogCategoryRepository.findAll();
        assertThat(catalogCategoryList).hasSize(databaseSizeBeforeCreate + 1);
        CatalogCategory testCatalogCategory = catalogCategoryList.get(catalogCategoryList.size() - 1);
        assertThat(testCatalogCategory.getRemoteId()).isEqualTo(DEFAULT_REMOTE_ID);
        assertThat(testCatalogCategory.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testCatalogCategory.getAccountingAccountNumber()).isEqualTo(DEFAULT_ACCOUNTING_ACCOUNT_NUMBER);
        assertThat(testCatalogCategory.getUsage()).isEqualTo(DEFAULT_USAGE);
        assertThat(testCatalogCategory.getInactiv()).isEqualTo(DEFAULT_INACTIV);
    }

    @Test
    @Transactional
    void createCatalogCategoryWithExistingId() throws Exception {
        // Create the CatalogCategory with an existing ID
        catalogCategory.setId(1L);
        CatalogCategoryDTO catalogCategoryDTO = catalogCategoryMapper.toDto(catalogCategory);

        int databaseSizeBeforeCreate = catalogCategoryRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restCatalogCategoryMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(catalogCategoryDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the CatalogCategory in the database
        List<CatalogCategory> catalogCategoryList = catalogCategoryRepository.findAll();
        assertThat(catalogCategoryList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkNameIsRequired() throws Exception {
        int databaseSizeBeforeTest = catalogCategoryRepository.findAll().size();
        // set the field null
        catalogCategory.setName(null);

        // Create the CatalogCategory, which fails.
        CatalogCategoryDTO catalogCategoryDTO = catalogCategoryMapper.toDto(catalogCategory);

        restCatalogCategoryMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(catalogCategoryDTO))
            )
            .andExpect(status().isBadRequest());

        List<CatalogCategory> catalogCategoryList = catalogCategoryRepository.findAll();
        assertThat(catalogCategoryList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllCatalogCategories() throws Exception {
        // Initialize the database
        catalogCategoryRepository.saveAndFlush(catalogCategory);

        // Get all the catalogCategoryList
        restCatalogCategoryMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(catalogCategory.getId().intValue())))
            .andExpect(jsonPath("$.[*].remoteId").value(hasItem(DEFAULT_REMOTE_ID)))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)))
            .andExpect(jsonPath("$.[*].accountingAccountNumber").value(hasItem(DEFAULT_ACCOUNTING_ACCOUNT_NUMBER)))
            .andExpect(jsonPath("$.[*].usage").value(hasItem(DEFAULT_USAGE)))
            .andExpect(jsonPath("$.[*].inactiv").value(hasItem(DEFAULT_INACTIV.booleanValue())));
    }

    @Test
    @Transactional
    void getCatalogCategory() throws Exception {
        // Initialize the database
        catalogCategoryRepository.saveAndFlush(catalogCategory);

        // Get the catalogCategory
        restCatalogCategoryMockMvc
            .perform(get(ENTITY_API_URL_ID, catalogCategory.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(catalogCategory.getId().intValue()))
            .andExpect(jsonPath("$.remoteId").value(DEFAULT_REMOTE_ID))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME))
            .andExpect(jsonPath("$.accountingAccountNumber").value(DEFAULT_ACCOUNTING_ACCOUNT_NUMBER))
            .andExpect(jsonPath("$.usage").value(DEFAULT_USAGE))
            .andExpect(jsonPath("$.inactiv").value(DEFAULT_INACTIV.booleanValue()));
    }

    @Test
    @Transactional
    void getNonExistingCatalogCategory() throws Exception {
        // Get the catalogCategory
        restCatalogCategoryMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewCatalogCategory() throws Exception {
        // Initialize the database
        catalogCategoryRepository.saveAndFlush(catalogCategory);

        int databaseSizeBeforeUpdate = catalogCategoryRepository.findAll().size();

        // Update the catalogCategory
        CatalogCategory updatedCatalogCategory = catalogCategoryRepository.findById(catalogCategory.getId()).get();
        // Disconnect from session so that the updates on updatedCatalogCategory are not directly saved in db
        em.detach(updatedCatalogCategory);
        updatedCatalogCategory
            .remoteId(UPDATED_REMOTE_ID)
            .name(UPDATED_NAME)
            .accountingAccountNumber(UPDATED_ACCOUNTING_ACCOUNT_NUMBER)
            .usage(UPDATED_USAGE)
            .inactiv(UPDATED_INACTIV);
        CatalogCategoryDTO catalogCategoryDTO = catalogCategoryMapper.toDto(updatedCatalogCategory);

        restCatalogCategoryMockMvc
            .perform(
                put(ENTITY_API_URL_ID, catalogCategoryDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(catalogCategoryDTO))
            )
            .andExpect(status().isOk());

        // Validate the CatalogCategory in the database
        List<CatalogCategory> catalogCategoryList = catalogCategoryRepository.findAll();
        assertThat(catalogCategoryList).hasSize(databaseSizeBeforeUpdate);
        CatalogCategory testCatalogCategory = catalogCategoryList.get(catalogCategoryList.size() - 1);
        assertThat(testCatalogCategory.getRemoteId()).isEqualTo(UPDATED_REMOTE_ID);
        assertThat(testCatalogCategory.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testCatalogCategory.getAccountingAccountNumber()).isEqualTo(UPDATED_ACCOUNTING_ACCOUNT_NUMBER);
        assertThat(testCatalogCategory.getUsage()).isEqualTo(UPDATED_USAGE);
        assertThat(testCatalogCategory.getInactiv()).isEqualTo(UPDATED_INACTIV);
    }

    @Test
    @Transactional
    void putNonExistingCatalogCategory() throws Exception {
        int databaseSizeBeforeUpdate = catalogCategoryRepository.findAll().size();
        catalogCategory.setId(count.incrementAndGet());

        // Create the CatalogCategory
        CatalogCategoryDTO catalogCategoryDTO = catalogCategoryMapper.toDto(catalogCategory);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restCatalogCategoryMockMvc
            .perform(
                put(ENTITY_API_URL_ID, catalogCategoryDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(catalogCategoryDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the CatalogCategory in the database
        List<CatalogCategory> catalogCategoryList = catalogCategoryRepository.findAll();
        assertThat(catalogCategoryList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchCatalogCategory() throws Exception {
        int databaseSizeBeforeUpdate = catalogCategoryRepository.findAll().size();
        catalogCategory.setId(count.incrementAndGet());

        // Create the CatalogCategory
        CatalogCategoryDTO catalogCategoryDTO = catalogCategoryMapper.toDto(catalogCategory);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCatalogCategoryMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(catalogCategoryDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the CatalogCategory in the database
        List<CatalogCategory> catalogCategoryList = catalogCategoryRepository.findAll();
        assertThat(catalogCategoryList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamCatalogCategory() throws Exception {
        int databaseSizeBeforeUpdate = catalogCategoryRepository.findAll().size();
        catalogCategory.setId(count.incrementAndGet());

        // Create the CatalogCategory
        CatalogCategoryDTO catalogCategoryDTO = catalogCategoryMapper.toDto(catalogCategory);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCatalogCategoryMockMvc
            .perform(
                put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(catalogCategoryDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the CatalogCategory in the database
        List<CatalogCategory> catalogCategoryList = catalogCategoryRepository.findAll();
        assertThat(catalogCategoryList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateCatalogCategoryWithPatch() throws Exception {
        // Initialize the database
        catalogCategoryRepository.saveAndFlush(catalogCategory);

        int databaseSizeBeforeUpdate = catalogCategoryRepository.findAll().size();

        // Update the catalogCategory using partial update
        CatalogCategory partialUpdatedCatalogCategory = new CatalogCategory();
        partialUpdatedCatalogCategory.setId(catalogCategory.getId());

        partialUpdatedCatalogCategory.usage(UPDATED_USAGE);

        restCatalogCategoryMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedCatalogCategory.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedCatalogCategory))
            )
            .andExpect(status().isOk());

        // Validate the CatalogCategory in the database
        List<CatalogCategory> catalogCategoryList = catalogCategoryRepository.findAll();
        assertThat(catalogCategoryList).hasSize(databaseSizeBeforeUpdate);
        CatalogCategory testCatalogCategory = catalogCategoryList.get(catalogCategoryList.size() - 1);
        assertThat(testCatalogCategory.getRemoteId()).isEqualTo(DEFAULT_REMOTE_ID);
        assertThat(testCatalogCategory.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testCatalogCategory.getAccountingAccountNumber()).isEqualTo(DEFAULT_ACCOUNTING_ACCOUNT_NUMBER);
        assertThat(testCatalogCategory.getUsage()).isEqualTo(UPDATED_USAGE);
        assertThat(testCatalogCategory.getInactiv()).isEqualTo(DEFAULT_INACTIV);
    }

    @Test
    @Transactional
    void fullUpdateCatalogCategoryWithPatch() throws Exception {
        // Initialize the database
        catalogCategoryRepository.saveAndFlush(catalogCategory);

        int databaseSizeBeforeUpdate = catalogCategoryRepository.findAll().size();

        // Update the catalogCategory using partial update
        CatalogCategory partialUpdatedCatalogCategory = new CatalogCategory();
        partialUpdatedCatalogCategory.setId(catalogCategory.getId());

        partialUpdatedCatalogCategory
            .remoteId(UPDATED_REMOTE_ID)
            .name(UPDATED_NAME)
            .accountingAccountNumber(UPDATED_ACCOUNTING_ACCOUNT_NUMBER)
            .usage(UPDATED_USAGE)
            .inactiv(UPDATED_INACTIV);

        restCatalogCategoryMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedCatalogCategory.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedCatalogCategory))
            )
            .andExpect(status().isOk());

        // Validate the CatalogCategory in the database
        List<CatalogCategory> catalogCategoryList = catalogCategoryRepository.findAll();
        assertThat(catalogCategoryList).hasSize(databaseSizeBeforeUpdate);
        CatalogCategory testCatalogCategory = catalogCategoryList.get(catalogCategoryList.size() - 1);
        assertThat(testCatalogCategory.getRemoteId()).isEqualTo(UPDATED_REMOTE_ID);
        assertThat(testCatalogCategory.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testCatalogCategory.getAccountingAccountNumber()).isEqualTo(UPDATED_ACCOUNTING_ACCOUNT_NUMBER);
        assertThat(testCatalogCategory.getUsage()).isEqualTo(UPDATED_USAGE);
        assertThat(testCatalogCategory.getInactiv()).isEqualTo(UPDATED_INACTIV);
    }

    @Test
    @Transactional
    void patchNonExistingCatalogCategory() throws Exception {
        int databaseSizeBeforeUpdate = catalogCategoryRepository.findAll().size();
        catalogCategory.setId(count.incrementAndGet());

        // Create the CatalogCategory
        CatalogCategoryDTO catalogCategoryDTO = catalogCategoryMapper.toDto(catalogCategory);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restCatalogCategoryMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, catalogCategoryDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(catalogCategoryDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the CatalogCategory in the database
        List<CatalogCategory> catalogCategoryList = catalogCategoryRepository.findAll();
        assertThat(catalogCategoryList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchCatalogCategory() throws Exception {
        int databaseSizeBeforeUpdate = catalogCategoryRepository.findAll().size();
        catalogCategory.setId(count.incrementAndGet());

        // Create the CatalogCategory
        CatalogCategoryDTO catalogCategoryDTO = catalogCategoryMapper.toDto(catalogCategory);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCatalogCategoryMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(catalogCategoryDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the CatalogCategory in the database
        List<CatalogCategory> catalogCategoryList = catalogCategoryRepository.findAll();
        assertThat(catalogCategoryList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamCatalogCategory() throws Exception {
        int databaseSizeBeforeUpdate = catalogCategoryRepository.findAll().size();
        catalogCategory.setId(count.incrementAndGet());

        // Create the CatalogCategory
        CatalogCategoryDTO catalogCategoryDTO = catalogCategoryMapper.toDto(catalogCategory);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCatalogCategoryMockMvc
            .perform(
                patch(ENTITY_API_URL)
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(catalogCategoryDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the CatalogCategory in the database
        List<CatalogCategory> catalogCategoryList = catalogCategoryRepository.findAll();
        assertThat(catalogCategoryList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteCatalogCategory() throws Exception {
        // Initialize the database
        catalogCategoryRepository.saveAndFlush(catalogCategory);

        int databaseSizeBeforeDelete = catalogCategoryRepository.findAll().size();

        // Delete the catalogCategory
        restCatalogCategoryMockMvc
            .perform(delete(ENTITY_API_URL_ID, catalogCategory.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<CatalogCategory> catalogCategoryList = catalogCategoryRepository.findAll();
        assertThat(catalogCategoryList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
