package ch.united.fastadmin.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import ch.united.fastadmin.IntegrationTest;
import ch.united.fastadmin.domain.CustomFieldValue;
import ch.united.fastadmin.repository.CustomFieldValueRepository;
import ch.united.fastadmin.service.dto.CustomFieldValueDTO;
import ch.united.fastadmin.service.mapper.CustomFieldValueMapper;
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
 * Integration tests for the {@link CustomFieldValueResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class CustomFieldValueResourceIT {

    private static final String DEFAULT_KEY = "AAAAAAAAAA";
    private static final String UPDATED_KEY = "BBBBBBBBBB";

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_VALUE = "AAAAAAAAAA";
    private static final String UPDATED_VALUE = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/custom-field-values";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private CustomFieldValueRepository customFieldValueRepository;

    @Autowired
    private CustomFieldValueMapper customFieldValueMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restCustomFieldValueMockMvc;

    private CustomFieldValue customFieldValue;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static CustomFieldValue createEntity(EntityManager em) {
        CustomFieldValue customFieldValue = new CustomFieldValue().key(DEFAULT_KEY).name(DEFAULT_NAME).value(DEFAULT_VALUE);
        return customFieldValue;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static CustomFieldValue createUpdatedEntity(EntityManager em) {
        CustomFieldValue customFieldValue = new CustomFieldValue().key(UPDATED_KEY).name(UPDATED_NAME).value(UPDATED_VALUE);
        return customFieldValue;
    }

    @BeforeEach
    public void initTest() {
        customFieldValue = createEntity(em);
    }

    @Test
    @Transactional
    void createCustomFieldValue() throws Exception {
        int databaseSizeBeforeCreate = customFieldValueRepository.findAll().size();
        // Create the CustomFieldValue
        CustomFieldValueDTO customFieldValueDTO = customFieldValueMapper.toDto(customFieldValue);
        restCustomFieldValueMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(customFieldValueDTO))
            )
            .andExpect(status().isCreated());

        // Validate the CustomFieldValue in the database
        List<CustomFieldValue> customFieldValueList = customFieldValueRepository.findAll();
        assertThat(customFieldValueList).hasSize(databaseSizeBeforeCreate + 1);
        CustomFieldValue testCustomFieldValue = customFieldValueList.get(customFieldValueList.size() - 1);
        assertThat(testCustomFieldValue.getKey()).isEqualTo(DEFAULT_KEY);
        assertThat(testCustomFieldValue.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testCustomFieldValue.getValue()).isEqualTo(DEFAULT_VALUE);
    }

    @Test
    @Transactional
    void createCustomFieldValueWithExistingId() throws Exception {
        // Create the CustomFieldValue with an existing ID
        customFieldValue.setId(1L);
        CustomFieldValueDTO customFieldValueDTO = customFieldValueMapper.toDto(customFieldValue);

        int databaseSizeBeforeCreate = customFieldValueRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restCustomFieldValueMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(customFieldValueDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the CustomFieldValue in the database
        List<CustomFieldValue> customFieldValueList = customFieldValueRepository.findAll();
        assertThat(customFieldValueList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkKeyIsRequired() throws Exception {
        int databaseSizeBeforeTest = customFieldValueRepository.findAll().size();
        // set the field null
        customFieldValue.setKey(null);

        // Create the CustomFieldValue, which fails.
        CustomFieldValueDTO customFieldValueDTO = customFieldValueMapper.toDto(customFieldValue);

        restCustomFieldValueMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(customFieldValueDTO))
            )
            .andExpect(status().isBadRequest());

        List<CustomFieldValue> customFieldValueList = customFieldValueRepository.findAll();
        assertThat(customFieldValueList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkNameIsRequired() throws Exception {
        int databaseSizeBeforeTest = customFieldValueRepository.findAll().size();
        // set the field null
        customFieldValue.setName(null);

        // Create the CustomFieldValue, which fails.
        CustomFieldValueDTO customFieldValueDTO = customFieldValueMapper.toDto(customFieldValue);

        restCustomFieldValueMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(customFieldValueDTO))
            )
            .andExpect(status().isBadRequest());

        List<CustomFieldValue> customFieldValueList = customFieldValueRepository.findAll();
        assertThat(customFieldValueList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllCustomFieldValues() throws Exception {
        // Initialize the database
        customFieldValueRepository.saveAndFlush(customFieldValue);

        // Get all the customFieldValueList
        restCustomFieldValueMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(customFieldValue.getId().intValue())))
            .andExpect(jsonPath("$.[*].key").value(hasItem(DEFAULT_KEY)))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)))
            .andExpect(jsonPath("$.[*].value").value(hasItem(DEFAULT_VALUE)));
    }

    @Test
    @Transactional
    void getCustomFieldValue() throws Exception {
        // Initialize the database
        customFieldValueRepository.saveAndFlush(customFieldValue);

        // Get the customFieldValue
        restCustomFieldValueMockMvc
            .perform(get(ENTITY_API_URL_ID, customFieldValue.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(customFieldValue.getId().intValue()))
            .andExpect(jsonPath("$.key").value(DEFAULT_KEY))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME))
            .andExpect(jsonPath("$.value").value(DEFAULT_VALUE));
    }

    @Test
    @Transactional
    void getNonExistingCustomFieldValue() throws Exception {
        // Get the customFieldValue
        restCustomFieldValueMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewCustomFieldValue() throws Exception {
        // Initialize the database
        customFieldValueRepository.saveAndFlush(customFieldValue);

        int databaseSizeBeforeUpdate = customFieldValueRepository.findAll().size();

        // Update the customFieldValue
        CustomFieldValue updatedCustomFieldValue = customFieldValueRepository.findById(customFieldValue.getId()).get();
        // Disconnect from session so that the updates on updatedCustomFieldValue are not directly saved in db
        em.detach(updatedCustomFieldValue);
        updatedCustomFieldValue.key(UPDATED_KEY).name(UPDATED_NAME).value(UPDATED_VALUE);
        CustomFieldValueDTO customFieldValueDTO = customFieldValueMapper.toDto(updatedCustomFieldValue);

        restCustomFieldValueMockMvc
            .perform(
                put(ENTITY_API_URL_ID, customFieldValueDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(customFieldValueDTO))
            )
            .andExpect(status().isOk());

        // Validate the CustomFieldValue in the database
        List<CustomFieldValue> customFieldValueList = customFieldValueRepository.findAll();
        assertThat(customFieldValueList).hasSize(databaseSizeBeforeUpdate);
        CustomFieldValue testCustomFieldValue = customFieldValueList.get(customFieldValueList.size() - 1);
        assertThat(testCustomFieldValue.getKey()).isEqualTo(UPDATED_KEY);
        assertThat(testCustomFieldValue.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testCustomFieldValue.getValue()).isEqualTo(UPDATED_VALUE);
    }

    @Test
    @Transactional
    void putNonExistingCustomFieldValue() throws Exception {
        int databaseSizeBeforeUpdate = customFieldValueRepository.findAll().size();
        customFieldValue.setId(count.incrementAndGet());

        // Create the CustomFieldValue
        CustomFieldValueDTO customFieldValueDTO = customFieldValueMapper.toDto(customFieldValue);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restCustomFieldValueMockMvc
            .perform(
                put(ENTITY_API_URL_ID, customFieldValueDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(customFieldValueDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the CustomFieldValue in the database
        List<CustomFieldValue> customFieldValueList = customFieldValueRepository.findAll();
        assertThat(customFieldValueList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchCustomFieldValue() throws Exception {
        int databaseSizeBeforeUpdate = customFieldValueRepository.findAll().size();
        customFieldValue.setId(count.incrementAndGet());

        // Create the CustomFieldValue
        CustomFieldValueDTO customFieldValueDTO = customFieldValueMapper.toDto(customFieldValue);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCustomFieldValueMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(customFieldValueDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the CustomFieldValue in the database
        List<CustomFieldValue> customFieldValueList = customFieldValueRepository.findAll();
        assertThat(customFieldValueList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamCustomFieldValue() throws Exception {
        int databaseSizeBeforeUpdate = customFieldValueRepository.findAll().size();
        customFieldValue.setId(count.incrementAndGet());

        // Create the CustomFieldValue
        CustomFieldValueDTO customFieldValueDTO = customFieldValueMapper.toDto(customFieldValue);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCustomFieldValueMockMvc
            .perform(
                put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(customFieldValueDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the CustomFieldValue in the database
        List<CustomFieldValue> customFieldValueList = customFieldValueRepository.findAll();
        assertThat(customFieldValueList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateCustomFieldValueWithPatch() throws Exception {
        // Initialize the database
        customFieldValueRepository.saveAndFlush(customFieldValue);

        int databaseSizeBeforeUpdate = customFieldValueRepository.findAll().size();

        // Update the customFieldValue using partial update
        CustomFieldValue partialUpdatedCustomFieldValue = new CustomFieldValue();
        partialUpdatedCustomFieldValue.setId(customFieldValue.getId());

        partialUpdatedCustomFieldValue.key(UPDATED_KEY).value(UPDATED_VALUE);

        restCustomFieldValueMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedCustomFieldValue.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedCustomFieldValue))
            )
            .andExpect(status().isOk());

        // Validate the CustomFieldValue in the database
        List<CustomFieldValue> customFieldValueList = customFieldValueRepository.findAll();
        assertThat(customFieldValueList).hasSize(databaseSizeBeforeUpdate);
        CustomFieldValue testCustomFieldValue = customFieldValueList.get(customFieldValueList.size() - 1);
        assertThat(testCustomFieldValue.getKey()).isEqualTo(UPDATED_KEY);
        assertThat(testCustomFieldValue.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testCustomFieldValue.getValue()).isEqualTo(UPDATED_VALUE);
    }

    @Test
    @Transactional
    void fullUpdateCustomFieldValueWithPatch() throws Exception {
        // Initialize the database
        customFieldValueRepository.saveAndFlush(customFieldValue);

        int databaseSizeBeforeUpdate = customFieldValueRepository.findAll().size();

        // Update the customFieldValue using partial update
        CustomFieldValue partialUpdatedCustomFieldValue = new CustomFieldValue();
        partialUpdatedCustomFieldValue.setId(customFieldValue.getId());

        partialUpdatedCustomFieldValue.key(UPDATED_KEY).name(UPDATED_NAME).value(UPDATED_VALUE);

        restCustomFieldValueMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedCustomFieldValue.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedCustomFieldValue))
            )
            .andExpect(status().isOk());

        // Validate the CustomFieldValue in the database
        List<CustomFieldValue> customFieldValueList = customFieldValueRepository.findAll();
        assertThat(customFieldValueList).hasSize(databaseSizeBeforeUpdate);
        CustomFieldValue testCustomFieldValue = customFieldValueList.get(customFieldValueList.size() - 1);
        assertThat(testCustomFieldValue.getKey()).isEqualTo(UPDATED_KEY);
        assertThat(testCustomFieldValue.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testCustomFieldValue.getValue()).isEqualTo(UPDATED_VALUE);
    }

    @Test
    @Transactional
    void patchNonExistingCustomFieldValue() throws Exception {
        int databaseSizeBeforeUpdate = customFieldValueRepository.findAll().size();
        customFieldValue.setId(count.incrementAndGet());

        // Create the CustomFieldValue
        CustomFieldValueDTO customFieldValueDTO = customFieldValueMapper.toDto(customFieldValue);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restCustomFieldValueMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, customFieldValueDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(customFieldValueDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the CustomFieldValue in the database
        List<CustomFieldValue> customFieldValueList = customFieldValueRepository.findAll();
        assertThat(customFieldValueList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchCustomFieldValue() throws Exception {
        int databaseSizeBeforeUpdate = customFieldValueRepository.findAll().size();
        customFieldValue.setId(count.incrementAndGet());

        // Create the CustomFieldValue
        CustomFieldValueDTO customFieldValueDTO = customFieldValueMapper.toDto(customFieldValue);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCustomFieldValueMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(customFieldValueDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the CustomFieldValue in the database
        List<CustomFieldValue> customFieldValueList = customFieldValueRepository.findAll();
        assertThat(customFieldValueList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamCustomFieldValue() throws Exception {
        int databaseSizeBeforeUpdate = customFieldValueRepository.findAll().size();
        customFieldValue.setId(count.incrementAndGet());

        // Create the CustomFieldValue
        CustomFieldValueDTO customFieldValueDTO = customFieldValueMapper.toDto(customFieldValue);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCustomFieldValueMockMvc
            .perform(
                patch(ENTITY_API_URL)
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(customFieldValueDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the CustomFieldValue in the database
        List<CustomFieldValue> customFieldValueList = customFieldValueRepository.findAll();
        assertThat(customFieldValueList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteCustomFieldValue() throws Exception {
        // Initialize the database
        customFieldValueRepository.saveAndFlush(customFieldValue);

        int databaseSizeBeforeDelete = customFieldValueRepository.findAll().size();

        // Delete the customFieldValue
        restCustomFieldValueMockMvc
            .perform(delete(ENTITY_API_URL_ID, customFieldValue.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<CustomFieldValue> customFieldValueList = customFieldValueRepository.findAll();
        assertThat(customFieldValueList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
