package ch.united.fastadmin.web.rest;

import static ch.united.fastadmin.web.rest.TestUtil.sameInstant;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import ch.united.fastadmin.IntegrationTest;
import ch.united.fastadmin.domain.ExchangeRate;
import ch.united.fastadmin.domain.enumeration.Currency;
import ch.united.fastadmin.domain.enumeration.Currency;
import ch.united.fastadmin.repository.ExchangeRateRepository;
import ch.united.fastadmin.service.dto.ExchangeRateDTO;
import ch.united.fastadmin.service.mapper.ExchangeRateMapper;
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
 * Integration tests for the {@link ExchangeRateResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class ExchangeRateResourceIT {

    private static final Integer DEFAULT_REMOTE_ID = 1;
    private static final Integer UPDATED_REMOTE_ID = 2;

    private static final Currency DEFAULT_CURRENCY_FROM = Currency.AED;
    private static final Currency UPDATED_CURRENCY_FROM = Currency.AFN;

    private static final Currency DEFAULT_CURRENCY_TO = Currency.AED;
    private static final Currency UPDATED_CURRENCY_TO = Currency.AFN;

    private static final Double DEFAULT_RATE = 1D;
    private static final Double UPDATED_RATE = 2D;

    private static final ZonedDateTime DEFAULT_CREATED = ZonedDateTime.ofInstant(Instant.ofEpochMilli(0L), ZoneOffset.UTC);
    private static final ZonedDateTime UPDATED_CREATED = ZonedDateTime.now(ZoneId.systemDefault()).withNano(0);

    private static final Boolean DEFAULT_INACTIV = false;
    private static final Boolean UPDATED_INACTIV = true;

    private static final String ENTITY_API_URL = "/api/exchange-rates";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ExchangeRateRepository exchangeRateRepository;

    @Autowired
    private ExchangeRateMapper exchangeRateMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restExchangeRateMockMvc;

    private ExchangeRate exchangeRate;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static ExchangeRate createEntity(EntityManager em) {
        ExchangeRate exchangeRate = new ExchangeRate()
            .remoteId(DEFAULT_REMOTE_ID)
            .currencyFrom(DEFAULT_CURRENCY_FROM)
            .currencyTo(DEFAULT_CURRENCY_TO)
            .rate(DEFAULT_RATE)
            .created(DEFAULT_CREATED)
            .inactiv(DEFAULT_INACTIV);
        return exchangeRate;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static ExchangeRate createUpdatedEntity(EntityManager em) {
        ExchangeRate exchangeRate = new ExchangeRate()
            .remoteId(UPDATED_REMOTE_ID)
            .currencyFrom(UPDATED_CURRENCY_FROM)
            .currencyTo(UPDATED_CURRENCY_TO)
            .rate(UPDATED_RATE)
            .created(UPDATED_CREATED)
            .inactiv(UPDATED_INACTIV);
        return exchangeRate;
    }

    @BeforeEach
    public void initTest() {
        exchangeRate = createEntity(em);
    }

    @Test
    @Transactional
    void createExchangeRate() throws Exception {
        int databaseSizeBeforeCreate = exchangeRateRepository.findAll().size();
        // Create the ExchangeRate
        ExchangeRateDTO exchangeRateDTO = exchangeRateMapper.toDto(exchangeRate);
        restExchangeRateMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(exchangeRateDTO))
            )
            .andExpect(status().isCreated());

        // Validate the ExchangeRate in the database
        List<ExchangeRate> exchangeRateList = exchangeRateRepository.findAll();
        assertThat(exchangeRateList).hasSize(databaseSizeBeforeCreate + 1);
        ExchangeRate testExchangeRate = exchangeRateList.get(exchangeRateList.size() - 1);
        assertThat(testExchangeRate.getRemoteId()).isEqualTo(DEFAULT_REMOTE_ID);
        assertThat(testExchangeRate.getCurrencyFrom()).isEqualTo(DEFAULT_CURRENCY_FROM);
        assertThat(testExchangeRate.getCurrencyTo()).isEqualTo(DEFAULT_CURRENCY_TO);
        assertThat(testExchangeRate.getRate()).isEqualTo(DEFAULT_RATE);
        assertThat(testExchangeRate.getCreated()).isEqualTo(DEFAULT_CREATED);
        assertThat(testExchangeRate.getInactiv()).isEqualTo(DEFAULT_INACTIV);
    }

    @Test
    @Transactional
    void createExchangeRateWithExistingId() throws Exception {
        // Create the ExchangeRate with an existing ID
        exchangeRate.setId(1L);
        ExchangeRateDTO exchangeRateDTO = exchangeRateMapper.toDto(exchangeRate);

        int databaseSizeBeforeCreate = exchangeRateRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restExchangeRateMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(exchangeRateDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the ExchangeRate in the database
        List<ExchangeRate> exchangeRateList = exchangeRateRepository.findAll();
        assertThat(exchangeRateList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllExchangeRates() throws Exception {
        // Initialize the database
        exchangeRateRepository.saveAndFlush(exchangeRate);

        // Get all the exchangeRateList
        restExchangeRateMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(exchangeRate.getId().intValue())))
            .andExpect(jsonPath("$.[*].remoteId").value(hasItem(DEFAULT_REMOTE_ID)))
            .andExpect(jsonPath("$.[*].currencyFrom").value(hasItem(DEFAULT_CURRENCY_FROM.toString())))
            .andExpect(jsonPath("$.[*].currencyTo").value(hasItem(DEFAULT_CURRENCY_TO.toString())))
            .andExpect(jsonPath("$.[*].rate").value(hasItem(DEFAULT_RATE.doubleValue())))
            .andExpect(jsonPath("$.[*].created").value(hasItem(sameInstant(DEFAULT_CREATED))))
            .andExpect(jsonPath("$.[*].inactiv").value(hasItem(DEFAULT_INACTIV.booleanValue())));
    }

    @Test
    @Transactional
    void getExchangeRate() throws Exception {
        // Initialize the database
        exchangeRateRepository.saveAndFlush(exchangeRate);

        // Get the exchangeRate
        restExchangeRateMockMvc
            .perform(get(ENTITY_API_URL_ID, exchangeRate.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(exchangeRate.getId().intValue()))
            .andExpect(jsonPath("$.remoteId").value(DEFAULT_REMOTE_ID))
            .andExpect(jsonPath("$.currencyFrom").value(DEFAULT_CURRENCY_FROM.toString()))
            .andExpect(jsonPath("$.currencyTo").value(DEFAULT_CURRENCY_TO.toString()))
            .andExpect(jsonPath("$.rate").value(DEFAULT_RATE.doubleValue()))
            .andExpect(jsonPath("$.created").value(sameInstant(DEFAULT_CREATED)))
            .andExpect(jsonPath("$.inactiv").value(DEFAULT_INACTIV.booleanValue()));
    }

    @Test
    @Transactional
    void getNonExistingExchangeRate() throws Exception {
        // Get the exchangeRate
        restExchangeRateMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewExchangeRate() throws Exception {
        // Initialize the database
        exchangeRateRepository.saveAndFlush(exchangeRate);

        int databaseSizeBeforeUpdate = exchangeRateRepository.findAll().size();

        // Update the exchangeRate
        ExchangeRate updatedExchangeRate = exchangeRateRepository.findById(exchangeRate.getId()).get();
        // Disconnect from session so that the updates on updatedExchangeRate are not directly saved in db
        em.detach(updatedExchangeRate);
        updatedExchangeRate
            .remoteId(UPDATED_REMOTE_ID)
            .currencyFrom(UPDATED_CURRENCY_FROM)
            .currencyTo(UPDATED_CURRENCY_TO)
            .rate(UPDATED_RATE)
            .created(UPDATED_CREATED)
            .inactiv(UPDATED_INACTIV);
        ExchangeRateDTO exchangeRateDTO = exchangeRateMapper.toDto(updatedExchangeRate);

        restExchangeRateMockMvc
            .perform(
                put(ENTITY_API_URL_ID, exchangeRateDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(exchangeRateDTO))
            )
            .andExpect(status().isOk());

        // Validate the ExchangeRate in the database
        List<ExchangeRate> exchangeRateList = exchangeRateRepository.findAll();
        assertThat(exchangeRateList).hasSize(databaseSizeBeforeUpdate);
        ExchangeRate testExchangeRate = exchangeRateList.get(exchangeRateList.size() - 1);
        assertThat(testExchangeRate.getRemoteId()).isEqualTo(UPDATED_REMOTE_ID);
        assertThat(testExchangeRate.getCurrencyFrom()).isEqualTo(UPDATED_CURRENCY_FROM);
        assertThat(testExchangeRate.getCurrencyTo()).isEqualTo(UPDATED_CURRENCY_TO);
        assertThat(testExchangeRate.getRate()).isEqualTo(UPDATED_RATE);
        assertThat(testExchangeRate.getCreated()).isEqualTo(UPDATED_CREATED);
        assertThat(testExchangeRate.getInactiv()).isEqualTo(UPDATED_INACTIV);
    }

    @Test
    @Transactional
    void putNonExistingExchangeRate() throws Exception {
        int databaseSizeBeforeUpdate = exchangeRateRepository.findAll().size();
        exchangeRate.setId(count.incrementAndGet());

        // Create the ExchangeRate
        ExchangeRateDTO exchangeRateDTO = exchangeRateMapper.toDto(exchangeRate);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restExchangeRateMockMvc
            .perform(
                put(ENTITY_API_URL_ID, exchangeRateDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(exchangeRateDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the ExchangeRate in the database
        List<ExchangeRate> exchangeRateList = exchangeRateRepository.findAll();
        assertThat(exchangeRateList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchExchangeRate() throws Exception {
        int databaseSizeBeforeUpdate = exchangeRateRepository.findAll().size();
        exchangeRate.setId(count.incrementAndGet());

        // Create the ExchangeRate
        ExchangeRateDTO exchangeRateDTO = exchangeRateMapper.toDto(exchangeRate);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restExchangeRateMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(exchangeRateDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the ExchangeRate in the database
        List<ExchangeRate> exchangeRateList = exchangeRateRepository.findAll();
        assertThat(exchangeRateList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamExchangeRate() throws Exception {
        int databaseSizeBeforeUpdate = exchangeRateRepository.findAll().size();
        exchangeRate.setId(count.incrementAndGet());

        // Create the ExchangeRate
        ExchangeRateDTO exchangeRateDTO = exchangeRateMapper.toDto(exchangeRate);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restExchangeRateMockMvc
            .perform(
                put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(exchangeRateDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the ExchangeRate in the database
        List<ExchangeRate> exchangeRateList = exchangeRateRepository.findAll();
        assertThat(exchangeRateList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateExchangeRateWithPatch() throws Exception {
        // Initialize the database
        exchangeRateRepository.saveAndFlush(exchangeRate);

        int databaseSizeBeforeUpdate = exchangeRateRepository.findAll().size();

        // Update the exchangeRate using partial update
        ExchangeRate partialUpdatedExchangeRate = new ExchangeRate();
        partialUpdatedExchangeRate.setId(exchangeRate.getId());

        partialUpdatedExchangeRate.remoteId(UPDATED_REMOTE_ID).created(UPDATED_CREATED).inactiv(UPDATED_INACTIV);

        restExchangeRateMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedExchangeRate.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedExchangeRate))
            )
            .andExpect(status().isOk());

        // Validate the ExchangeRate in the database
        List<ExchangeRate> exchangeRateList = exchangeRateRepository.findAll();
        assertThat(exchangeRateList).hasSize(databaseSizeBeforeUpdate);
        ExchangeRate testExchangeRate = exchangeRateList.get(exchangeRateList.size() - 1);
        assertThat(testExchangeRate.getRemoteId()).isEqualTo(UPDATED_REMOTE_ID);
        assertThat(testExchangeRate.getCurrencyFrom()).isEqualTo(DEFAULT_CURRENCY_FROM);
        assertThat(testExchangeRate.getCurrencyTo()).isEqualTo(DEFAULT_CURRENCY_TO);
        assertThat(testExchangeRate.getRate()).isEqualTo(DEFAULT_RATE);
        assertThat(testExchangeRate.getCreated()).isEqualTo(UPDATED_CREATED);
        assertThat(testExchangeRate.getInactiv()).isEqualTo(UPDATED_INACTIV);
    }

    @Test
    @Transactional
    void fullUpdateExchangeRateWithPatch() throws Exception {
        // Initialize the database
        exchangeRateRepository.saveAndFlush(exchangeRate);

        int databaseSizeBeforeUpdate = exchangeRateRepository.findAll().size();

        // Update the exchangeRate using partial update
        ExchangeRate partialUpdatedExchangeRate = new ExchangeRate();
        partialUpdatedExchangeRate.setId(exchangeRate.getId());

        partialUpdatedExchangeRate
            .remoteId(UPDATED_REMOTE_ID)
            .currencyFrom(UPDATED_CURRENCY_FROM)
            .currencyTo(UPDATED_CURRENCY_TO)
            .rate(UPDATED_RATE)
            .created(UPDATED_CREATED)
            .inactiv(UPDATED_INACTIV);

        restExchangeRateMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedExchangeRate.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedExchangeRate))
            )
            .andExpect(status().isOk());

        // Validate the ExchangeRate in the database
        List<ExchangeRate> exchangeRateList = exchangeRateRepository.findAll();
        assertThat(exchangeRateList).hasSize(databaseSizeBeforeUpdate);
        ExchangeRate testExchangeRate = exchangeRateList.get(exchangeRateList.size() - 1);
        assertThat(testExchangeRate.getRemoteId()).isEqualTo(UPDATED_REMOTE_ID);
        assertThat(testExchangeRate.getCurrencyFrom()).isEqualTo(UPDATED_CURRENCY_FROM);
        assertThat(testExchangeRate.getCurrencyTo()).isEqualTo(UPDATED_CURRENCY_TO);
        assertThat(testExchangeRate.getRate()).isEqualTo(UPDATED_RATE);
        assertThat(testExchangeRate.getCreated()).isEqualTo(UPDATED_CREATED);
        assertThat(testExchangeRate.getInactiv()).isEqualTo(UPDATED_INACTIV);
    }

    @Test
    @Transactional
    void patchNonExistingExchangeRate() throws Exception {
        int databaseSizeBeforeUpdate = exchangeRateRepository.findAll().size();
        exchangeRate.setId(count.incrementAndGet());

        // Create the ExchangeRate
        ExchangeRateDTO exchangeRateDTO = exchangeRateMapper.toDto(exchangeRate);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restExchangeRateMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, exchangeRateDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(exchangeRateDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the ExchangeRate in the database
        List<ExchangeRate> exchangeRateList = exchangeRateRepository.findAll();
        assertThat(exchangeRateList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchExchangeRate() throws Exception {
        int databaseSizeBeforeUpdate = exchangeRateRepository.findAll().size();
        exchangeRate.setId(count.incrementAndGet());

        // Create the ExchangeRate
        ExchangeRateDTO exchangeRateDTO = exchangeRateMapper.toDto(exchangeRate);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restExchangeRateMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(exchangeRateDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the ExchangeRate in the database
        List<ExchangeRate> exchangeRateList = exchangeRateRepository.findAll();
        assertThat(exchangeRateList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamExchangeRate() throws Exception {
        int databaseSizeBeforeUpdate = exchangeRateRepository.findAll().size();
        exchangeRate.setId(count.incrementAndGet());

        // Create the ExchangeRate
        ExchangeRateDTO exchangeRateDTO = exchangeRateMapper.toDto(exchangeRate);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restExchangeRateMockMvc
            .perform(
                patch(ENTITY_API_URL)
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(exchangeRateDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the ExchangeRate in the database
        List<ExchangeRate> exchangeRateList = exchangeRateRepository.findAll();
        assertThat(exchangeRateList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteExchangeRate() throws Exception {
        // Initialize the database
        exchangeRateRepository.saveAndFlush(exchangeRate);

        int databaseSizeBeforeDelete = exchangeRateRepository.findAll().size();

        // Delete the exchangeRate
        restExchangeRateMockMvc
            .perform(delete(ENTITY_API_URL_ID, exchangeRate.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<ExchangeRate> exchangeRateList = exchangeRateRepository.findAll();
        assertThat(exchangeRateList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
