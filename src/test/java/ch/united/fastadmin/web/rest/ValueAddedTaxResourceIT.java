package ch.united.fastadmin.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import ch.united.fastadmin.IntegrationTest;
import ch.united.fastadmin.domain.ValueAddedTax;
import ch.united.fastadmin.domain.enumeration.VatType;
import ch.united.fastadmin.repository.ValueAddedTaxRepository;
import ch.united.fastadmin.service.dto.ValueAddedTaxDTO;
import ch.united.fastadmin.service.mapper.ValueAddedTaxMapper;
import java.time.LocalDate;
import java.time.ZoneId;
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
 * Integration tests for the {@link ValueAddedTaxResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class ValueAddedTaxResourceIT {

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    private static final VatType DEFAULT_VAT_TYPE = VatType.PERCENT;
    private static final VatType UPDATED_VAT_TYPE = VatType.NO_VAT;

    private static final LocalDate DEFAULT_VALID_FROM = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_VALID_FROM = LocalDate.now(ZoneId.systemDefault());

    private static final LocalDate DEFAULT_VALID_UNTIL = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_VALID_UNTIL = LocalDate.now(ZoneId.systemDefault());

    private static final Double DEFAULT_VAT_PERCENT = 1D;
    private static final Double UPDATED_VAT_PERCENT = 2D;

    private static final Boolean DEFAULT_INACTIV = false;
    private static final Boolean UPDATED_INACTIV = true;

    private static final Integer DEFAULT_NEW_VAT_ID = 1;
    private static final Integer UPDATED_NEW_VAT_ID = 2;

    private static final String ENTITY_API_URL = "/api/value-added-taxes";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ValueAddedTaxRepository valueAddedTaxRepository;

    @Autowired
    private ValueAddedTaxMapper valueAddedTaxMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restValueAddedTaxMockMvc;

    private ValueAddedTax valueAddedTax;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static ValueAddedTax createEntity(EntityManager em) {
        ValueAddedTax valueAddedTax = new ValueAddedTax()
            .name(DEFAULT_NAME)
            .vatType(DEFAULT_VAT_TYPE)
            .validFrom(DEFAULT_VALID_FROM)
            .validUntil(DEFAULT_VALID_UNTIL)
            .vatPercent(DEFAULT_VAT_PERCENT)
            .inactiv(DEFAULT_INACTIV)
            .newVatId(DEFAULT_NEW_VAT_ID);
        return valueAddedTax;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static ValueAddedTax createUpdatedEntity(EntityManager em) {
        ValueAddedTax valueAddedTax = new ValueAddedTax()
            .name(UPDATED_NAME)
            .vatType(UPDATED_VAT_TYPE)
            .validFrom(UPDATED_VALID_FROM)
            .validUntil(UPDATED_VALID_UNTIL)
            .vatPercent(UPDATED_VAT_PERCENT)
            .inactiv(UPDATED_INACTIV)
            .newVatId(UPDATED_NEW_VAT_ID);
        return valueAddedTax;
    }

    @BeforeEach
    public void initTest() {
        valueAddedTax = createEntity(em);
    }

    @Test
    @Transactional
    void createValueAddedTax() throws Exception {
        int databaseSizeBeforeCreate = valueAddedTaxRepository.findAll().size();
        // Create the ValueAddedTax
        ValueAddedTaxDTO valueAddedTaxDTO = valueAddedTaxMapper.toDto(valueAddedTax);
        restValueAddedTaxMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(valueAddedTaxDTO))
            )
            .andExpect(status().isCreated());

        // Validate the ValueAddedTax in the database
        List<ValueAddedTax> valueAddedTaxList = valueAddedTaxRepository.findAll();
        assertThat(valueAddedTaxList).hasSize(databaseSizeBeforeCreate + 1);
        ValueAddedTax testValueAddedTax = valueAddedTaxList.get(valueAddedTaxList.size() - 1);
        assertThat(testValueAddedTax.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testValueAddedTax.getVatType()).isEqualTo(DEFAULT_VAT_TYPE);
        assertThat(testValueAddedTax.getValidFrom()).isEqualTo(DEFAULT_VALID_FROM);
        assertThat(testValueAddedTax.getValidUntil()).isEqualTo(DEFAULT_VALID_UNTIL);
        assertThat(testValueAddedTax.getVatPercent()).isEqualTo(DEFAULT_VAT_PERCENT);
        assertThat(testValueAddedTax.getInactiv()).isEqualTo(DEFAULT_INACTIV);
        assertThat(testValueAddedTax.getNewVatId()).isEqualTo(DEFAULT_NEW_VAT_ID);
    }

    @Test
    @Transactional
    void createValueAddedTaxWithExistingId() throws Exception {
        // Create the ValueAddedTax with an existing ID
        valueAddedTax.setId(1L);
        ValueAddedTaxDTO valueAddedTaxDTO = valueAddedTaxMapper.toDto(valueAddedTax);

        int databaseSizeBeforeCreate = valueAddedTaxRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restValueAddedTaxMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(valueAddedTaxDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the ValueAddedTax in the database
        List<ValueAddedTax> valueAddedTaxList = valueAddedTaxRepository.findAll();
        assertThat(valueAddedTaxList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllValueAddedTaxes() throws Exception {
        // Initialize the database
        valueAddedTaxRepository.saveAndFlush(valueAddedTax);

        // Get all the valueAddedTaxList
        restValueAddedTaxMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(valueAddedTax.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)))
            .andExpect(jsonPath("$.[*].vatType").value(hasItem(DEFAULT_VAT_TYPE.toString())))
            .andExpect(jsonPath("$.[*].validFrom").value(hasItem(DEFAULT_VALID_FROM.toString())))
            .andExpect(jsonPath("$.[*].validUntil").value(hasItem(DEFAULT_VALID_UNTIL.toString())))
            .andExpect(jsonPath("$.[*].vatPercent").value(hasItem(DEFAULT_VAT_PERCENT.doubleValue())))
            .andExpect(jsonPath("$.[*].inactiv").value(hasItem(DEFAULT_INACTIV.booleanValue())))
            .andExpect(jsonPath("$.[*].newVatId").value(hasItem(DEFAULT_NEW_VAT_ID)));
    }

    @Test
    @Transactional
    void getValueAddedTax() throws Exception {
        // Initialize the database
        valueAddedTaxRepository.saveAndFlush(valueAddedTax);

        // Get the valueAddedTax
        restValueAddedTaxMockMvc
            .perform(get(ENTITY_API_URL_ID, valueAddedTax.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(valueAddedTax.getId().intValue()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME))
            .andExpect(jsonPath("$.vatType").value(DEFAULT_VAT_TYPE.toString()))
            .andExpect(jsonPath("$.validFrom").value(DEFAULT_VALID_FROM.toString()))
            .andExpect(jsonPath("$.validUntil").value(DEFAULT_VALID_UNTIL.toString()))
            .andExpect(jsonPath("$.vatPercent").value(DEFAULT_VAT_PERCENT.doubleValue()))
            .andExpect(jsonPath("$.inactiv").value(DEFAULT_INACTIV.booleanValue()))
            .andExpect(jsonPath("$.newVatId").value(DEFAULT_NEW_VAT_ID));
    }

    @Test
    @Transactional
    void getNonExistingValueAddedTax() throws Exception {
        // Get the valueAddedTax
        restValueAddedTaxMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewValueAddedTax() throws Exception {
        // Initialize the database
        valueAddedTaxRepository.saveAndFlush(valueAddedTax);

        int databaseSizeBeforeUpdate = valueAddedTaxRepository.findAll().size();

        // Update the valueAddedTax
        ValueAddedTax updatedValueAddedTax = valueAddedTaxRepository.findById(valueAddedTax.getId()).get();
        // Disconnect from session so that the updates on updatedValueAddedTax are not directly saved in db
        em.detach(updatedValueAddedTax);
        updatedValueAddedTax
            .name(UPDATED_NAME)
            .vatType(UPDATED_VAT_TYPE)
            .validFrom(UPDATED_VALID_FROM)
            .validUntil(UPDATED_VALID_UNTIL)
            .vatPercent(UPDATED_VAT_PERCENT)
            .inactiv(UPDATED_INACTIV)
            .newVatId(UPDATED_NEW_VAT_ID);
        ValueAddedTaxDTO valueAddedTaxDTO = valueAddedTaxMapper.toDto(updatedValueAddedTax);

        restValueAddedTaxMockMvc
            .perform(
                put(ENTITY_API_URL_ID, valueAddedTaxDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(valueAddedTaxDTO))
            )
            .andExpect(status().isOk());

        // Validate the ValueAddedTax in the database
        List<ValueAddedTax> valueAddedTaxList = valueAddedTaxRepository.findAll();
        assertThat(valueAddedTaxList).hasSize(databaseSizeBeforeUpdate);
        ValueAddedTax testValueAddedTax = valueAddedTaxList.get(valueAddedTaxList.size() - 1);
        assertThat(testValueAddedTax.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testValueAddedTax.getVatType()).isEqualTo(UPDATED_VAT_TYPE);
        assertThat(testValueAddedTax.getValidFrom()).isEqualTo(UPDATED_VALID_FROM);
        assertThat(testValueAddedTax.getValidUntil()).isEqualTo(UPDATED_VALID_UNTIL);
        assertThat(testValueAddedTax.getVatPercent()).isEqualTo(UPDATED_VAT_PERCENT);
        assertThat(testValueAddedTax.getInactiv()).isEqualTo(UPDATED_INACTIV);
        assertThat(testValueAddedTax.getNewVatId()).isEqualTo(UPDATED_NEW_VAT_ID);
    }

    @Test
    @Transactional
    void putNonExistingValueAddedTax() throws Exception {
        int databaseSizeBeforeUpdate = valueAddedTaxRepository.findAll().size();
        valueAddedTax.setId(count.incrementAndGet());

        // Create the ValueAddedTax
        ValueAddedTaxDTO valueAddedTaxDTO = valueAddedTaxMapper.toDto(valueAddedTax);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restValueAddedTaxMockMvc
            .perform(
                put(ENTITY_API_URL_ID, valueAddedTaxDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(valueAddedTaxDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the ValueAddedTax in the database
        List<ValueAddedTax> valueAddedTaxList = valueAddedTaxRepository.findAll();
        assertThat(valueAddedTaxList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchValueAddedTax() throws Exception {
        int databaseSizeBeforeUpdate = valueAddedTaxRepository.findAll().size();
        valueAddedTax.setId(count.incrementAndGet());

        // Create the ValueAddedTax
        ValueAddedTaxDTO valueAddedTaxDTO = valueAddedTaxMapper.toDto(valueAddedTax);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restValueAddedTaxMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(valueAddedTaxDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the ValueAddedTax in the database
        List<ValueAddedTax> valueAddedTaxList = valueAddedTaxRepository.findAll();
        assertThat(valueAddedTaxList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamValueAddedTax() throws Exception {
        int databaseSizeBeforeUpdate = valueAddedTaxRepository.findAll().size();
        valueAddedTax.setId(count.incrementAndGet());

        // Create the ValueAddedTax
        ValueAddedTaxDTO valueAddedTaxDTO = valueAddedTaxMapper.toDto(valueAddedTax);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restValueAddedTaxMockMvc
            .perform(
                put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(valueAddedTaxDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the ValueAddedTax in the database
        List<ValueAddedTax> valueAddedTaxList = valueAddedTaxRepository.findAll();
        assertThat(valueAddedTaxList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateValueAddedTaxWithPatch() throws Exception {
        // Initialize the database
        valueAddedTaxRepository.saveAndFlush(valueAddedTax);

        int databaseSizeBeforeUpdate = valueAddedTaxRepository.findAll().size();

        // Update the valueAddedTax using partial update
        ValueAddedTax partialUpdatedValueAddedTax = new ValueAddedTax();
        partialUpdatedValueAddedTax.setId(valueAddedTax.getId());

        partialUpdatedValueAddedTax.name(UPDATED_NAME).newVatId(UPDATED_NEW_VAT_ID);

        restValueAddedTaxMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedValueAddedTax.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedValueAddedTax))
            )
            .andExpect(status().isOk());

        // Validate the ValueAddedTax in the database
        List<ValueAddedTax> valueAddedTaxList = valueAddedTaxRepository.findAll();
        assertThat(valueAddedTaxList).hasSize(databaseSizeBeforeUpdate);
        ValueAddedTax testValueAddedTax = valueAddedTaxList.get(valueAddedTaxList.size() - 1);
        assertThat(testValueAddedTax.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testValueAddedTax.getVatType()).isEqualTo(DEFAULT_VAT_TYPE);
        assertThat(testValueAddedTax.getValidFrom()).isEqualTo(DEFAULT_VALID_FROM);
        assertThat(testValueAddedTax.getValidUntil()).isEqualTo(DEFAULT_VALID_UNTIL);
        assertThat(testValueAddedTax.getVatPercent()).isEqualTo(DEFAULT_VAT_PERCENT);
        assertThat(testValueAddedTax.getInactiv()).isEqualTo(DEFAULT_INACTIV);
        assertThat(testValueAddedTax.getNewVatId()).isEqualTo(UPDATED_NEW_VAT_ID);
    }

    @Test
    @Transactional
    void fullUpdateValueAddedTaxWithPatch() throws Exception {
        // Initialize the database
        valueAddedTaxRepository.saveAndFlush(valueAddedTax);

        int databaseSizeBeforeUpdate = valueAddedTaxRepository.findAll().size();

        // Update the valueAddedTax using partial update
        ValueAddedTax partialUpdatedValueAddedTax = new ValueAddedTax();
        partialUpdatedValueAddedTax.setId(valueAddedTax.getId());

        partialUpdatedValueAddedTax
            .name(UPDATED_NAME)
            .vatType(UPDATED_VAT_TYPE)
            .validFrom(UPDATED_VALID_FROM)
            .validUntil(UPDATED_VALID_UNTIL)
            .vatPercent(UPDATED_VAT_PERCENT)
            .inactiv(UPDATED_INACTIV)
            .newVatId(UPDATED_NEW_VAT_ID);

        restValueAddedTaxMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedValueAddedTax.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedValueAddedTax))
            )
            .andExpect(status().isOk());

        // Validate the ValueAddedTax in the database
        List<ValueAddedTax> valueAddedTaxList = valueAddedTaxRepository.findAll();
        assertThat(valueAddedTaxList).hasSize(databaseSizeBeforeUpdate);
        ValueAddedTax testValueAddedTax = valueAddedTaxList.get(valueAddedTaxList.size() - 1);
        assertThat(testValueAddedTax.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testValueAddedTax.getVatType()).isEqualTo(UPDATED_VAT_TYPE);
        assertThat(testValueAddedTax.getValidFrom()).isEqualTo(UPDATED_VALID_FROM);
        assertThat(testValueAddedTax.getValidUntil()).isEqualTo(UPDATED_VALID_UNTIL);
        assertThat(testValueAddedTax.getVatPercent()).isEqualTo(UPDATED_VAT_PERCENT);
        assertThat(testValueAddedTax.getInactiv()).isEqualTo(UPDATED_INACTIV);
        assertThat(testValueAddedTax.getNewVatId()).isEqualTo(UPDATED_NEW_VAT_ID);
    }

    @Test
    @Transactional
    void patchNonExistingValueAddedTax() throws Exception {
        int databaseSizeBeforeUpdate = valueAddedTaxRepository.findAll().size();
        valueAddedTax.setId(count.incrementAndGet());

        // Create the ValueAddedTax
        ValueAddedTaxDTO valueAddedTaxDTO = valueAddedTaxMapper.toDto(valueAddedTax);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restValueAddedTaxMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, valueAddedTaxDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(valueAddedTaxDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the ValueAddedTax in the database
        List<ValueAddedTax> valueAddedTaxList = valueAddedTaxRepository.findAll();
        assertThat(valueAddedTaxList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchValueAddedTax() throws Exception {
        int databaseSizeBeforeUpdate = valueAddedTaxRepository.findAll().size();
        valueAddedTax.setId(count.incrementAndGet());

        // Create the ValueAddedTax
        ValueAddedTaxDTO valueAddedTaxDTO = valueAddedTaxMapper.toDto(valueAddedTax);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restValueAddedTaxMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(valueAddedTaxDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the ValueAddedTax in the database
        List<ValueAddedTax> valueAddedTaxList = valueAddedTaxRepository.findAll();
        assertThat(valueAddedTaxList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamValueAddedTax() throws Exception {
        int databaseSizeBeforeUpdate = valueAddedTaxRepository.findAll().size();
        valueAddedTax.setId(count.incrementAndGet());

        // Create the ValueAddedTax
        ValueAddedTaxDTO valueAddedTaxDTO = valueAddedTaxMapper.toDto(valueAddedTax);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restValueAddedTaxMockMvc
            .perform(
                patch(ENTITY_API_URL)
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(valueAddedTaxDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the ValueAddedTax in the database
        List<ValueAddedTax> valueAddedTaxList = valueAddedTaxRepository.findAll();
        assertThat(valueAddedTaxList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteValueAddedTax() throws Exception {
        // Initialize the database
        valueAddedTaxRepository.saveAndFlush(valueAddedTax);

        int databaseSizeBeforeDelete = valueAddedTaxRepository.findAll().size();

        // Delete the valueAddedTax
        restValueAddedTaxMockMvc
            .perform(delete(ENTITY_API_URL_ID, valueAddedTax.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<ValueAddedTax> valueAddedTaxList = valueAddedTaxRepository.findAll();
        assertThat(valueAddedTaxList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
