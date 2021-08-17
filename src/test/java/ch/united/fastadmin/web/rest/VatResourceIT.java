package ch.united.fastadmin.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import ch.united.fastadmin.IntegrationTest;
import ch.united.fastadmin.domain.Vat;
import ch.united.fastadmin.domain.enumeration.VatType;
import ch.united.fastadmin.repository.VatRepository;
import ch.united.fastadmin.service.dto.VatDTO;
import ch.united.fastadmin.service.mapper.VatMapper;
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
 * Integration tests for the {@link VatResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class VatResourceIT {

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

    private static final String ENTITY_API_URL = "/api/vats";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private VatRepository vatRepository;

    @Autowired
    private VatMapper vatMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restVatMockMvc;

    private Vat vat;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Vat createEntity(EntityManager em) {
        Vat vat = new Vat()
            .name(DEFAULT_NAME)
            .vatType(DEFAULT_VAT_TYPE)
            .validFrom(DEFAULT_VALID_FROM)
            .validUntil(DEFAULT_VALID_UNTIL)
            .vatPercent(DEFAULT_VAT_PERCENT)
            .inactiv(DEFAULT_INACTIV)
            .newVatId(DEFAULT_NEW_VAT_ID);
        return vat;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Vat createUpdatedEntity(EntityManager em) {
        Vat vat = new Vat()
            .name(UPDATED_NAME)
            .vatType(UPDATED_VAT_TYPE)
            .validFrom(UPDATED_VALID_FROM)
            .validUntil(UPDATED_VALID_UNTIL)
            .vatPercent(UPDATED_VAT_PERCENT)
            .inactiv(UPDATED_INACTIV)
            .newVatId(UPDATED_NEW_VAT_ID);
        return vat;
    }

    @BeforeEach
    public void initTest() {
        vat = createEntity(em);
    }

    @Test
    @Transactional
    void createVat() throws Exception {
        int databaseSizeBeforeCreate = vatRepository.findAll().size();
        // Create the Vat
        VatDTO vatDTO = vatMapper.toDto(vat);
        restVatMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(vatDTO)))
            .andExpect(status().isCreated());

        // Validate the Vat in the database
        List<Vat> vatList = vatRepository.findAll();
        assertThat(vatList).hasSize(databaseSizeBeforeCreate + 1);
        Vat testVat = vatList.get(vatList.size() - 1);
        assertThat(testVat.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testVat.getVatType()).isEqualTo(DEFAULT_VAT_TYPE);
        assertThat(testVat.getValidFrom()).isEqualTo(DEFAULT_VALID_FROM);
        assertThat(testVat.getValidUntil()).isEqualTo(DEFAULT_VALID_UNTIL);
        assertThat(testVat.getVatPercent()).isEqualTo(DEFAULT_VAT_PERCENT);
        assertThat(testVat.getInactiv()).isEqualTo(DEFAULT_INACTIV);
        assertThat(testVat.getNewVatId()).isEqualTo(DEFAULT_NEW_VAT_ID);
    }

    @Test
    @Transactional
    void createVatWithExistingId() throws Exception {
        // Create the Vat with an existing ID
        vat.setId(1L);
        VatDTO vatDTO = vatMapper.toDto(vat);

        int databaseSizeBeforeCreate = vatRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restVatMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(vatDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Vat in the database
        List<Vat> vatList = vatRepository.findAll();
        assertThat(vatList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllVats() throws Exception {
        // Initialize the database
        vatRepository.saveAndFlush(vat);

        // Get all the vatList
        restVatMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(vat.getId().intValue())))
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
    void getVat() throws Exception {
        // Initialize the database
        vatRepository.saveAndFlush(vat);

        // Get the vat
        restVatMockMvc
            .perform(get(ENTITY_API_URL_ID, vat.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(vat.getId().intValue()))
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
    void getNonExistingVat() throws Exception {
        // Get the vat
        restVatMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewVat() throws Exception {
        // Initialize the database
        vatRepository.saveAndFlush(vat);

        int databaseSizeBeforeUpdate = vatRepository.findAll().size();

        // Update the vat
        Vat updatedVat = vatRepository.findById(vat.getId()).get();
        // Disconnect from session so that the updates on updatedVat are not directly saved in db
        em.detach(updatedVat);
        updatedVat
            .name(UPDATED_NAME)
            .vatType(UPDATED_VAT_TYPE)
            .validFrom(UPDATED_VALID_FROM)
            .validUntil(UPDATED_VALID_UNTIL)
            .vatPercent(UPDATED_VAT_PERCENT)
            .inactiv(UPDATED_INACTIV)
            .newVatId(UPDATED_NEW_VAT_ID);
        VatDTO vatDTO = vatMapper.toDto(updatedVat);

        restVatMockMvc
            .perform(
                put(ENTITY_API_URL_ID, vatDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(vatDTO))
            )
            .andExpect(status().isOk());

        // Validate the Vat in the database
        List<Vat> vatList = vatRepository.findAll();
        assertThat(vatList).hasSize(databaseSizeBeforeUpdate);
        Vat testVat = vatList.get(vatList.size() - 1);
        assertThat(testVat.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testVat.getVatType()).isEqualTo(UPDATED_VAT_TYPE);
        assertThat(testVat.getValidFrom()).isEqualTo(UPDATED_VALID_FROM);
        assertThat(testVat.getValidUntil()).isEqualTo(UPDATED_VALID_UNTIL);
        assertThat(testVat.getVatPercent()).isEqualTo(UPDATED_VAT_PERCENT);
        assertThat(testVat.getInactiv()).isEqualTo(UPDATED_INACTIV);
        assertThat(testVat.getNewVatId()).isEqualTo(UPDATED_NEW_VAT_ID);
    }

    @Test
    @Transactional
    void putNonExistingVat() throws Exception {
        int databaseSizeBeforeUpdate = vatRepository.findAll().size();
        vat.setId(count.incrementAndGet());

        // Create the Vat
        VatDTO vatDTO = vatMapper.toDto(vat);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restVatMockMvc
            .perform(
                put(ENTITY_API_URL_ID, vatDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(vatDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Vat in the database
        List<Vat> vatList = vatRepository.findAll();
        assertThat(vatList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchVat() throws Exception {
        int databaseSizeBeforeUpdate = vatRepository.findAll().size();
        vat.setId(count.incrementAndGet());

        // Create the Vat
        VatDTO vatDTO = vatMapper.toDto(vat);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restVatMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(vatDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Vat in the database
        List<Vat> vatList = vatRepository.findAll();
        assertThat(vatList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamVat() throws Exception {
        int databaseSizeBeforeUpdate = vatRepository.findAll().size();
        vat.setId(count.incrementAndGet());

        // Create the Vat
        VatDTO vatDTO = vatMapper.toDto(vat);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restVatMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(vatDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Vat in the database
        List<Vat> vatList = vatRepository.findAll();
        assertThat(vatList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateVatWithPatch() throws Exception {
        // Initialize the database
        vatRepository.saveAndFlush(vat);

        int databaseSizeBeforeUpdate = vatRepository.findAll().size();

        // Update the vat using partial update
        Vat partialUpdatedVat = new Vat();
        partialUpdatedVat.setId(vat.getId());

        partialUpdatedVat.vatType(UPDATED_VAT_TYPE).validUntil(UPDATED_VALID_UNTIL).inactiv(UPDATED_INACTIV);

        restVatMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedVat.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedVat))
            )
            .andExpect(status().isOk());

        // Validate the Vat in the database
        List<Vat> vatList = vatRepository.findAll();
        assertThat(vatList).hasSize(databaseSizeBeforeUpdate);
        Vat testVat = vatList.get(vatList.size() - 1);
        assertThat(testVat.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testVat.getVatType()).isEqualTo(UPDATED_VAT_TYPE);
        assertThat(testVat.getValidFrom()).isEqualTo(DEFAULT_VALID_FROM);
        assertThat(testVat.getValidUntil()).isEqualTo(UPDATED_VALID_UNTIL);
        assertThat(testVat.getVatPercent()).isEqualTo(DEFAULT_VAT_PERCENT);
        assertThat(testVat.getInactiv()).isEqualTo(UPDATED_INACTIV);
        assertThat(testVat.getNewVatId()).isEqualTo(DEFAULT_NEW_VAT_ID);
    }

    @Test
    @Transactional
    void fullUpdateVatWithPatch() throws Exception {
        // Initialize the database
        vatRepository.saveAndFlush(vat);

        int databaseSizeBeforeUpdate = vatRepository.findAll().size();

        // Update the vat using partial update
        Vat partialUpdatedVat = new Vat();
        partialUpdatedVat.setId(vat.getId());

        partialUpdatedVat
            .name(UPDATED_NAME)
            .vatType(UPDATED_VAT_TYPE)
            .validFrom(UPDATED_VALID_FROM)
            .validUntil(UPDATED_VALID_UNTIL)
            .vatPercent(UPDATED_VAT_PERCENT)
            .inactiv(UPDATED_INACTIV)
            .newVatId(UPDATED_NEW_VAT_ID);

        restVatMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedVat.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedVat))
            )
            .andExpect(status().isOk());

        // Validate the Vat in the database
        List<Vat> vatList = vatRepository.findAll();
        assertThat(vatList).hasSize(databaseSizeBeforeUpdate);
        Vat testVat = vatList.get(vatList.size() - 1);
        assertThat(testVat.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testVat.getVatType()).isEqualTo(UPDATED_VAT_TYPE);
        assertThat(testVat.getValidFrom()).isEqualTo(UPDATED_VALID_FROM);
        assertThat(testVat.getValidUntil()).isEqualTo(UPDATED_VALID_UNTIL);
        assertThat(testVat.getVatPercent()).isEqualTo(UPDATED_VAT_PERCENT);
        assertThat(testVat.getInactiv()).isEqualTo(UPDATED_INACTIV);
        assertThat(testVat.getNewVatId()).isEqualTo(UPDATED_NEW_VAT_ID);
    }

    @Test
    @Transactional
    void patchNonExistingVat() throws Exception {
        int databaseSizeBeforeUpdate = vatRepository.findAll().size();
        vat.setId(count.incrementAndGet());

        // Create the Vat
        VatDTO vatDTO = vatMapper.toDto(vat);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restVatMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, vatDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(vatDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Vat in the database
        List<Vat> vatList = vatRepository.findAll();
        assertThat(vatList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchVat() throws Exception {
        int databaseSizeBeforeUpdate = vatRepository.findAll().size();
        vat.setId(count.incrementAndGet());

        // Create the Vat
        VatDTO vatDTO = vatMapper.toDto(vat);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restVatMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(vatDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Vat in the database
        List<Vat> vatList = vatRepository.findAll();
        assertThat(vatList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamVat() throws Exception {
        int databaseSizeBeforeUpdate = vatRepository.findAll().size();
        vat.setId(count.incrementAndGet());

        // Create the Vat
        VatDTO vatDTO = vatMapper.toDto(vat);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restVatMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(vatDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Vat in the database
        List<Vat> vatList = vatRepository.findAll();
        assertThat(vatList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteVat() throws Exception {
        // Initialize the database
        vatRepository.saveAndFlush(vat);

        int databaseSizeBeforeDelete = vatRepository.findAll().size();

        // Delete the vat
        restVatMockMvc.perform(delete(ENTITY_API_URL_ID, vat.getId()).accept(MediaType.APPLICATION_JSON)).andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Vat> vatList = vatRepository.findAll();
        assertThat(vatList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
