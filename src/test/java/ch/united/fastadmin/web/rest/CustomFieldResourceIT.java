package ch.united.fastadmin.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import ch.united.fastadmin.IntegrationTest;
import ch.united.fastadmin.domain.CustomField;
import ch.united.fastadmin.domain.enumeration.DomainArea;
import ch.united.fastadmin.repository.CustomFieldRepository;
import ch.united.fastadmin.service.dto.CustomFieldDTO;
import ch.united.fastadmin.service.mapper.CustomFieldMapper;
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
 * Integration tests for the {@link CustomFieldResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class CustomFieldResourceIT {

    private static final DomainArea DEFAULT_DOMAIN_AREA = DomainArea.CONTACTS;
    private static final DomainArea UPDATED_DOMAIN_AREA = DomainArea.LETTERS;

    private static final String DEFAULT_KEY = "AAAAAAAAAA";
    private static final String UPDATED_KEY = "BBBBBBBBBB";

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_DEFAULT_VALUE = "AAAAAAAAAA";
    private static final String UPDATED_DEFAULT_VALUE = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/custom-fields";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private CustomFieldRepository customFieldRepository;

    @Autowired
    private CustomFieldMapper customFieldMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restCustomFieldMockMvc;

    private CustomField customField;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static CustomField createEntity(EntityManager em) {
        CustomField customField = new CustomField()
            .domainArea(DEFAULT_DOMAIN_AREA)
            .key(DEFAULT_KEY)
            .name(DEFAULT_NAME)
            .defaultValue(DEFAULT_DEFAULT_VALUE);
        return customField;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static CustomField createUpdatedEntity(EntityManager em) {
        CustomField customField = new CustomField()
            .domainArea(UPDATED_DOMAIN_AREA)
            .key(UPDATED_KEY)
            .name(UPDATED_NAME)
            .defaultValue(UPDATED_DEFAULT_VALUE);
        return customField;
    }

    @BeforeEach
    public void initTest() {
        customField = createEntity(em);
    }

    @Test
    @Transactional
    void createCustomField() throws Exception {
        int databaseSizeBeforeCreate = customFieldRepository.findAll().size();
        // Create the CustomField
        CustomFieldDTO customFieldDTO = customFieldMapper.toDto(customField);
        restCustomFieldMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(customFieldDTO))
            )
            .andExpect(status().isCreated());

        // Validate the CustomField in the database
        List<CustomField> customFieldList = customFieldRepository.findAll();
        assertThat(customFieldList).hasSize(databaseSizeBeforeCreate + 1);
        CustomField testCustomField = customFieldList.get(customFieldList.size() - 1);
        assertThat(testCustomField.getDomainArea()).isEqualTo(DEFAULT_DOMAIN_AREA);
        assertThat(testCustomField.getKey()).isEqualTo(DEFAULT_KEY);
        assertThat(testCustomField.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testCustomField.getDefaultValue()).isEqualTo(DEFAULT_DEFAULT_VALUE);
    }

    @Test
    @Transactional
    void createCustomFieldWithExistingId() throws Exception {
        // Create the CustomField with an existing ID
        customField.setId(1L);
        CustomFieldDTO customFieldDTO = customFieldMapper.toDto(customField);

        int databaseSizeBeforeCreate = customFieldRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restCustomFieldMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(customFieldDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the CustomField in the database
        List<CustomField> customFieldList = customFieldRepository.findAll();
        assertThat(customFieldList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkKeyIsRequired() throws Exception {
        int databaseSizeBeforeTest = customFieldRepository.findAll().size();
        // set the field null
        customField.setKey(null);

        // Create the CustomField, which fails.
        CustomFieldDTO customFieldDTO = customFieldMapper.toDto(customField);

        restCustomFieldMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(customFieldDTO))
            )
            .andExpect(status().isBadRequest());

        List<CustomField> customFieldList = customFieldRepository.findAll();
        assertThat(customFieldList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkNameIsRequired() throws Exception {
        int databaseSizeBeforeTest = customFieldRepository.findAll().size();
        // set the field null
        customField.setName(null);

        // Create the CustomField, which fails.
        CustomFieldDTO customFieldDTO = customFieldMapper.toDto(customField);

        restCustomFieldMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(customFieldDTO))
            )
            .andExpect(status().isBadRequest());

        List<CustomField> customFieldList = customFieldRepository.findAll();
        assertThat(customFieldList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllCustomFields() throws Exception {
        // Initialize the database
        customFieldRepository.saveAndFlush(customField);

        // Get all the customFieldList
        restCustomFieldMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(customField.getId().intValue())))
            .andExpect(jsonPath("$.[*].domainArea").value(hasItem(DEFAULT_DOMAIN_AREA.toString())))
            .andExpect(jsonPath("$.[*].key").value(hasItem(DEFAULT_KEY)))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)))
            .andExpect(jsonPath("$.[*].defaultValue").value(hasItem(DEFAULT_DEFAULT_VALUE)));
    }

    @Test
    @Transactional
    void getCustomField() throws Exception {
        // Initialize the database
        customFieldRepository.saveAndFlush(customField);

        // Get the customField
        restCustomFieldMockMvc
            .perform(get(ENTITY_API_URL_ID, customField.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(customField.getId().intValue()))
            .andExpect(jsonPath("$.domainArea").value(DEFAULT_DOMAIN_AREA.toString()))
            .andExpect(jsonPath("$.key").value(DEFAULT_KEY))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME))
            .andExpect(jsonPath("$.defaultValue").value(DEFAULT_DEFAULT_VALUE));
    }

    @Test
    @Transactional
    void getNonExistingCustomField() throws Exception {
        // Get the customField
        restCustomFieldMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewCustomField() throws Exception {
        // Initialize the database
        customFieldRepository.saveAndFlush(customField);

        int databaseSizeBeforeUpdate = customFieldRepository.findAll().size();

        // Update the customField
        CustomField updatedCustomField = customFieldRepository.findById(customField.getId()).get();
        // Disconnect from session so that the updates on updatedCustomField are not directly saved in db
        em.detach(updatedCustomField);
        updatedCustomField.domainArea(UPDATED_DOMAIN_AREA).key(UPDATED_KEY).name(UPDATED_NAME).defaultValue(UPDATED_DEFAULT_VALUE);
        CustomFieldDTO customFieldDTO = customFieldMapper.toDto(updatedCustomField);

        restCustomFieldMockMvc
            .perform(
                put(ENTITY_API_URL_ID, customFieldDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(customFieldDTO))
            )
            .andExpect(status().isOk());

        // Validate the CustomField in the database
        List<CustomField> customFieldList = customFieldRepository.findAll();
        assertThat(customFieldList).hasSize(databaseSizeBeforeUpdate);
        CustomField testCustomField = customFieldList.get(customFieldList.size() - 1);
        assertThat(testCustomField.getDomainArea()).isEqualTo(UPDATED_DOMAIN_AREA);
        assertThat(testCustomField.getKey()).isEqualTo(UPDATED_KEY);
        assertThat(testCustomField.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testCustomField.getDefaultValue()).isEqualTo(UPDATED_DEFAULT_VALUE);
    }

    @Test
    @Transactional
    void putNonExistingCustomField() throws Exception {
        int databaseSizeBeforeUpdate = customFieldRepository.findAll().size();
        customField.setId(count.incrementAndGet());

        // Create the CustomField
        CustomFieldDTO customFieldDTO = customFieldMapper.toDto(customField);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restCustomFieldMockMvc
            .perform(
                put(ENTITY_API_URL_ID, customFieldDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(customFieldDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the CustomField in the database
        List<CustomField> customFieldList = customFieldRepository.findAll();
        assertThat(customFieldList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchCustomField() throws Exception {
        int databaseSizeBeforeUpdate = customFieldRepository.findAll().size();
        customField.setId(count.incrementAndGet());

        // Create the CustomField
        CustomFieldDTO customFieldDTO = customFieldMapper.toDto(customField);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCustomFieldMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(customFieldDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the CustomField in the database
        List<CustomField> customFieldList = customFieldRepository.findAll();
        assertThat(customFieldList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamCustomField() throws Exception {
        int databaseSizeBeforeUpdate = customFieldRepository.findAll().size();
        customField.setId(count.incrementAndGet());

        // Create the CustomField
        CustomFieldDTO customFieldDTO = customFieldMapper.toDto(customField);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCustomFieldMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(customFieldDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the CustomField in the database
        List<CustomField> customFieldList = customFieldRepository.findAll();
        assertThat(customFieldList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateCustomFieldWithPatch() throws Exception {
        // Initialize the database
        customFieldRepository.saveAndFlush(customField);

        int databaseSizeBeforeUpdate = customFieldRepository.findAll().size();

        // Update the customField using partial update
        CustomField partialUpdatedCustomField = new CustomField();
        partialUpdatedCustomField.setId(customField.getId());

        partialUpdatedCustomField.domainArea(UPDATED_DOMAIN_AREA).key(UPDATED_KEY).defaultValue(UPDATED_DEFAULT_VALUE);

        restCustomFieldMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedCustomField.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedCustomField))
            )
            .andExpect(status().isOk());

        // Validate the CustomField in the database
        List<CustomField> customFieldList = customFieldRepository.findAll();
        assertThat(customFieldList).hasSize(databaseSizeBeforeUpdate);
        CustomField testCustomField = customFieldList.get(customFieldList.size() - 1);
        assertThat(testCustomField.getDomainArea()).isEqualTo(UPDATED_DOMAIN_AREA);
        assertThat(testCustomField.getKey()).isEqualTo(UPDATED_KEY);
        assertThat(testCustomField.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testCustomField.getDefaultValue()).isEqualTo(UPDATED_DEFAULT_VALUE);
    }

    @Test
    @Transactional
    void fullUpdateCustomFieldWithPatch() throws Exception {
        // Initialize the database
        customFieldRepository.saveAndFlush(customField);

        int databaseSizeBeforeUpdate = customFieldRepository.findAll().size();

        // Update the customField using partial update
        CustomField partialUpdatedCustomField = new CustomField();
        partialUpdatedCustomField.setId(customField.getId());

        partialUpdatedCustomField.domainArea(UPDATED_DOMAIN_AREA).key(UPDATED_KEY).name(UPDATED_NAME).defaultValue(UPDATED_DEFAULT_VALUE);

        restCustomFieldMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedCustomField.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedCustomField))
            )
            .andExpect(status().isOk());

        // Validate the CustomField in the database
        List<CustomField> customFieldList = customFieldRepository.findAll();
        assertThat(customFieldList).hasSize(databaseSizeBeforeUpdate);
        CustomField testCustomField = customFieldList.get(customFieldList.size() - 1);
        assertThat(testCustomField.getDomainArea()).isEqualTo(UPDATED_DOMAIN_AREA);
        assertThat(testCustomField.getKey()).isEqualTo(UPDATED_KEY);
        assertThat(testCustomField.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testCustomField.getDefaultValue()).isEqualTo(UPDATED_DEFAULT_VALUE);
    }

    @Test
    @Transactional
    void patchNonExistingCustomField() throws Exception {
        int databaseSizeBeforeUpdate = customFieldRepository.findAll().size();
        customField.setId(count.incrementAndGet());

        // Create the CustomField
        CustomFieldDTO customFieldDTO = customFieldMapper.toDto(customField);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restCustomFieldMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, customFieldDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(customFieldDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the CustomField in the database
        List<CustomField> customFieldList = customFieldRepository.findAll();
        assertThat(customFieldList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchCustomField() throws Exception {
        int databaseSizeBeforeUpdate = customFieldRepository.findAll().size();
        customField.setId(count.incrementAndGet());

        // Create the CustomField
        CustomFieldDTO customFieldDTO = customFieldMapper.toDto(customField);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCustomFieldMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(customFieldDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the CustomField in the database
        List<CustomField> customFieldList = customFieldRepository.findAll();
        assertThat(customFieldList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamCustomField() throws Exception {
        int databaseSizeBeforeUpdate = customFieldRepository.findAll().size();
        customField.setId(count.incrementAndGet());

        // Create the CustomField
        CustomFieldDTO customFieldDTO = customFieldMapper.toDto(customField);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCustomFieldMockMvc
            .perform(
                patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(customFieldDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the CustomField in the database
        List<CustomField> customFieldList = customFieldRepository.findAll();
        assertThat(customFieldList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteCustomField() throws Exception {
        // Initialize the database
        customFieldRepository.saveAndFlush(customField);

        int databaseSizeBeforeDelete = customFieldRepository.findAll().size();

        // Delete the customField
        restCustomFieldMockMvc
            .perform(delete(ENTITY_API_URL_ID, customField.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<CustomField> customFieldList = customFieldRepository.findAll();
        assertThat(customFieldList).hasSize(databaseSizeBeforeDelete - 1);
    }
}