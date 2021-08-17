package ch.united.fastadmin.web.rest;

import static ch.united.fastadmin.web.rest.TestUtil.sameInstant;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import ch.united.fastadmin.IntegrationTest;
import ch.united.fastadmin.domain.Offer;
import ch.united.fastadmin.domain.enumeration.Currency;
import ch.united.fastadmin.domain.enumeration.DiscountType;
import ch.united.fastadmin.domain.enumeration.DocumentLanguage;
import ch.united.fastadmin.domain.enumeration.OfferAcceptOnlineStatus;
import ch.united.fastadmin.domain.enumeration.OfferStatus;
import ch.united.fastadmin.repository.OfferRepository;
import ch.united.fastadmin.service.dto.OfferDTO;
import ch.united.fastadmin.service.mapper.OfferMapper;
import java.time.Instant;
import java.time.LocalDate;
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
 * Integration tests for the {@link OfferResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class OfferResourceIT {

    private static final Integer DEFAULT_REMOTE_ID = 1;
    private static final Integer UPDATED_REMOTE_ID = 2;

    private static final String DEFAULT_NUMBER = "AAAAAAAAAA";
    private static final String UPDATED_NUMBER = "BBBBBBBBBB";

    private static final String DEFAULT_CONTACT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_CONTACT_NAME = "BBBBBBBBBB";

    private static final LocalDate DEFAULT_DATE = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_DATE = LocalDate.now(ZoneId.systemDefault());

    private static final LocalDate DEFAULT_VALID_UNTIL = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_VALID_UNTIL = LocalDate.now(ZoneId.systemDefault());

    private static final String DEFAULT_PERIOD_TEXT = "AAAAAAAAAA";
    private static final String UPDATED_PERIOD_TEXT = "BBBBBBBBBB";

    private static final Currency DEFAULT_CURRENCY = Currency.AED;
    private static final Currency UPDATED_CURRENCY = Currency.AFN;

    private static final Double DEFAULT_TOTAL = 1D;
    private static final Double UPDATED_TOTAL = 2D;

    private static final Boolean DEFAULT_VAT_INCLUDED = false;
    private static final Boolean UPDATED_VAT_INCLUDED = true;

    private static final Double DEFAULT_DISCOUNT_RATE = 1D;
    private static final Double UPDATED_DISCOUNT_RATE = 2D;

    private static final DiscountType DEFAULT_DISCOUNT_TYPE = DiscountType.IN_PERCENT;
    private static final DiscountType UPDATED_DISCOUNT_TYPE = DiscountType.AMOUNT;

    private static final Boolean DEFAULT_ACCEPT_ONLINE = false;
    private static final Boolean UPDATED_ACCEPT_ONLINE = true;

    private static final String DEFAULT_ACCEPT_ONLINE_URL = "AAAAAAAAAA";
    private static final String UPDATED_ACCEPT_ONLINE_URL = "BBBBBBBBBB";

    private static final OfferAcceptOnlineStatus DEFAULT_ACCEPT_ONLINE_STATUS = OfferAcceptOnlineStatus.ACCEPTED;
    private static final OfferAcceptOnlineStatus UPDATED_ACCEPT_ONLINE_STATUS = OfferAcceptOnlineStatus.DECLINED;

    private static final DocumentLanguage DEFAULT_LANGUAGE = DocumentLanguage.FRENCH;
    private static final DocumentLanguage UPDATED_LANGUAGE = DocumentLanguage.ENGLISH;

    private static final Integer DEFAULT_PAGE_AMOUNT = 1;
    private static final Integer UPDATED_PAGE_AMOUNT = 2;

    private static final String DEFAULT_NOTES = "AAAAAAAAAA";
    private static final String UPDATED_NOTES = "BBBBBBBBBB";

    private static final OfferStatus DEFAULT_STATUS = OfferStatus.DRAFT;
    private static final OfferStatus UPDATED_STATUS = OfferStatus.SENT;

    private static final ZonedDateTime DEFAULT_CREATED = ZonedDateTime.ofInstant(Instant.ofEpochMilli(0L), ZoneOffset.UTC);
    private static final ZonedDateTime UPDATED_CREATED = ZonedDateTime.now(ZoneId.systemDefault()).withNano(0);

    private static final String ENTITY_API_URL = "/api/offers";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private OfferRepository offerRepository;

    @Autowired
    private OfferMapper offerMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restOfferMockMvc;

    private Offer offer;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Offer createEntity(EntityManager em) {
        Offer offer = new Offer()
            .remoteId(DEFAULT_REMOTE_ID)
            .number(DEFAULT_NUMBER)
            .contactName(DEFAULT_CONTACT_NAME)
            .date(DEFAULT_DATE)
            .validUntil(DEFAULT_VALID_UNTIL)
            .periodText(DEFAULT_PERIOD_TEXT)
            .currency(DEFAULT_CURRENCY)
            .total(DEFAULT_TOTAL)
            .vatIncluded(DEFAULT_VAT_INCLUDED)
            .discountRate(DEFAULT_DISCOUNT_RATE)
            .discountType(DEFAULT_DISCOUNT_TYPE)
            .acceptOnline(DEFAULT_ACCEPT_ONLINE)
            .acceptOnlineUrl(DEFAULT_ACCEPT_ONLINE_URL)
            .acceptOnlineStatus(DEFAULT_ACCEPT_ONLINE_STATUS)
            .language(DEFAULT_LANGUAGE)
            .pageAmount(DEFAULT_PAGE_AMOUNT)
            .notes(DEFAULT_NOTES)
            .status(DEFAULT_STATUS)
            .created(DEFAULT_CREATED);
        return offer;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Offer createUpdatedEntity(EntityManager em) {
        Offer offer = new Offer()
            .remoteId(UPDATED_REMOTE_ID)
            .number(UPDATED_NUMBER)
            .contactName(UPDATED_CONTACT_NAME)
            .date(UPDATED_DATE)
            .validUntil(UPDATED_VALID_UNTIL)
            .periodText(UPDATED_PERIOD_TEXT)
            .currency(UPDATED_CURRENCY)
            .total(UPDATED_TOTAL)
            .vatIncluded(UPDATED_VAT_INCLUDED)
            .discountRate(UPDATED_DISCOUNT_RATE)
            .discountType(UPDATED_DISCOUNT_TYPE)
            .acceptOnline(UPDATED_ACCEPT_ONLINE)
            .acceptOnlineUrl(UPDATED_ACCEPT_ONLINE_URL)
            .acceptOnlineStatus(UPDATED_ACCEPT_ONLINE_STATUS)
            .language(UPDATED_LANGUAGE)
            .pageAmount(UPDATED_PAGE_AMOUNT)
            .notes(UPDATED_NOTES)
            .status(UPDATED_STATUS)
            .created(UPDATED_CREATED);
        return offer;
    }

    @BeforeEach
    public void initTest() {
        offer = createEntity(em);
    }

    @Test
    @Transactional
    void createOffer() throws Exception {
        int databaseSizeBeforeCreate = offerRepository.findAll().size();
        // Create the Offer
        OfferDTO offerDTO = offerMapper.toDto(offer);
        restOfferMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(offerDTO)))
            .andExpect(status().isCreated());

        // Validate the Offer in the database
        List<Offer> offerList = offerRepository.findAll();
        assertThat(offerList).hasSize(databaseSizeBeforeCreate + 1);
        Offer testOffer = offerList.get(offerList.size() - 1);
        assertThat(testOffer.getRemoteId()).isEqualTo(DEFAULT_REMOTE_ID);
        assertThat(testOffer.getNumber()).isEqualTo(DEFAULT_NUMBER);
        assertThat(testOffer.getContactName()).isEqualTo(DEFAULT_CONTACT_NAME);
        assertThat(testOffer.getDate()).isEqualTo(DEFAULT_DATE);
        assertThat(testOffer.getValidUntil()).isEqualTo(DEFAULT_VALID_UNTIL);
        assertThat(testOffer.getPeriodText()).isEqualTo(DEFAULT_PERIOD_TEXT);
        assertThat(testOffer.getCurrency()).isEqualTo(DEFAULT_CURRENCY);
        assertThat(testOffer.getTotal()).isEqualTo(DEFAULT_TOTAL);
        assertThat(testOffer.getVatIncluded()).isEqualTo(DEFAULT_VAT_INCLUDED);
        assertThat(testOffer.getDiscountRate()).isEqualTo(DEFAULT_DISCOUNT_RATE);
        assertThat(testOffer.getDiscountType()).isEqualTo(DEFAULT_DISCOUNT_TYPE);
        assertThat(testOffer.getAcceptOnline()).isEqualTo(DEFAULT_ACCEPT_ONLINE);
        assertThat(testOffer.getAcceptOnlineUrl()).isEqualTo(DEFAULT_ACCEPT_ONLINE_URL);
        assertThat(testOffer.getAcceptOnlineStatus()).isEqualTo(DEFAULT_ACCEPT_ONLINE_STATUS);
        assertThat(testOffer.getLanguage()).isEqualTo(DEFAULT_LANGUAGE);
        assertThat(testOffer.getPageAmount()).isEqualTo(DEFAULT_PAGE_AMOUNT);
        assertThat(testOffer.getNotes()).isEqualTo(DEFAULT_NOTES);
        assertThat(testOffer.getStatus()).isEqualTo(DEFAULT_STATUS);
        assertThat(testOffer.getCreated()).isEqualTo(DEFAULT_CREATED);
    }

    @Test
    @Transactional
    void createOfferWithExistingId() throws Exception {
        // Create the Offer with an existing ID
        offer.setId(1L);
        OfferDTO offerDTO = offerMapper.toDto(offer);

        int databaseSizeBeforeCreate = offerRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restOfferMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(offerDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Offer in the database
        List<Offer> offerList = offerRepository.findAll();
        assertThat(offerList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllOffers() throws Exception {
        // Initialize the database
        offerRepository.saveAndFlush(offer);

        // Get all the offerList
        restOfferMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(offer.getId().intValue())))
            .andExpect(jsonPath("$.[*].remoteId").value(hasItem(DEFAULT_REMOTE_ID)))
            .andExpect(jsonPath("$.[*].number").value(hasItem(DEFAULT_NUMBER)))
            .andExpect(jsonPath("$.[*].contactName").value(hasItem(DEFAULT_CONTACT_NAME)))
            .andExpect(jsonPath("$.[*].date").value(hasItem(DEFAULT_DATE.toString())))
            .andExpect(jsonPath("$.[*].validUntil").value(hasItem(DEFAULT_VALID_UNTIL.toString())))
            .andExpect(jsonPath("$.[*].periodText").value(hasItem(DEFAULT_PERIOD_TEXT)))
            .andExpect(jsonPath("$.[*].currency").value(hasItem(DEFAULT_CURRENCY.toString())))
            .andExpect(jsonPath("$.[*].total").value(hasItem(DEFAULT_TOTAL.doubleValue())))
            .andExpect(jsonPath("$.[*].vatIncluded").value(hasItem(DEFAULT_VAT_INCLUDED.booleanValue())))
            .andExpect(jsonPath("$.[*].discountRate").value(hasItem(DEFAULT_DISCOUNT_RATE.doubleValue())))
            .andExpect(jsonPath("$.[*].discountType").value(hasItem(DEFAULT_DISCOUNT_TYPE.toString())))
            .andExpect(jsonPath("$.[*].acceptOnline").value(hasItem(DEFAULT_ACCEPT_ONLINE.booleanValue())))
            .andExpect(jsonPath("$.[*].acceptOnlineUrl").value(hasItem(DEFAULT_ACCEPT_ONLINE_URL)))
            .andExpect(jsonPath("$.[*].acceptOnlineStatus").value(hasItem(DEFAULT_ACCEPT_ONLINE_STATUS.toString())))
            .andExpect(jsonPath("$.[*].language").value(hasItem(DEFAULT_LANGUAGE.toString())))
            .andExpect(jsonPath("$.[*].pageAmount").value(hasItem(DEFAULT_PAGE_AMOUNT)))
            .andExpect(jsonPath("$.[*].notes").value(hasItem(DEFAULT_NOTES)))
            .andExpect(jsonPath("$.[*].status").value(hasItem(DEFAULT_STATUS.toString())))
            .andExpect(jsonPath("$.[*].created").value(hasItem(sameInstant(DEFAULT_CREATED))));
    }

    @Test
    @Transactional
    void getOffer() throws Exception {
        // Initialize the database
        offerRepository.saveAndFlush(offer);

        // Get the offer
        restOfferMockMvc
            .perform(get(ENTITY_API_URL_ID, offer.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(offer.getId().intValue()))
            .andExpect(jsonPath("$.remoteId").value(DEFAULT_REMOTE_ID))
            .andExpect(jsonPath("$.number").value(DEFAULT_NUMBER))
            .andExpect(jsonPath("$.contactName").value(DEFAULT_CONTACT_NAME))
            .andExpect(jsonPath("$.date").value(DEFAULT_DATE.toString()))
            .andExpect(jsonPath("$.validUntil").value(DEFAULT_VALID_UNTIL.toString()))
            .andExpect(jsonPath("$.periodText").value(DEFAULT_PERIOD_TEXT))
            .andExpect(jsonPath("$.currency").value(DEFAULT_CURRENCY.toString()))
            .andExpect(jsonPath("$.total").value(DEFAULT_TOTAL.doubleValue()))
            .andExpect(jsonPath("$.vatIncluded").value(DEFAULT_VAT_INCLUDED.booleanValue()))
            .andExpect(jsonPath("$.discountRate").value(DEFAULT_DISCOUNT_RATE.doubleValue()))
            .andExpect(jsonPath("$.discountType").value(DEFAULT_DISCOUNT_TYPE.toString()))
            .andExpect(jsonPath("$.acceptOnline").value(DEFAULT_ACCEPT_ONLINE.booleanValue()))
            .andExpect(jsonPath("$.acceptOnlineUrl").value(DEFAULT_ACCEPT_ONLINE_URL))
            .andExpect(jsonPath("$.acceptOnlineStatus").value(DEFAULT_ACCEPT_ONLINE_STATUS.toString()))
            .andExpect(jsonPath("$.language").value(DEFAULT_LANGUAGE.toString()))
            .andExpect(jsonPath("$.pageAmount").value(DEFAULT_PAGE_AMOUNT))
            .andExpect(jsonPath("$.notes").value(DEFAULT_NOTES))
            .andExpect(jsonPath("$.status").value(DEFAULT_STATUS.toString()))
            .andExpect(jsonPath("$.created").value(sameInstant(DEFAULT_CREATED)));
    }

    @Test
    @Transactional
    void getNonExistingOffer() throws Exception {
        // Get the offer
        restOfferMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewOffer() throws Exception {
        // Initialize the database
        offerRepository.saveAndFlush(offer);

        int databaseSizeBeforeUpdate = offerRepository.findAll().size();

        // Update the offer
        Offer updatedOffer = offerRepository.findById(offer.getId()).get();
        // Disconnect from session so that the updates on updatedOffer are not directly saved in db
        em.detach(updatedOffer);
        updatedOffer
            .remoteId(UPDATED_REMOTE_ID)
            .number(UPDATED_NUMBER)
            .contactName(UPDATED_CONTACT_NAME)
            .date(UPDATED_DATE)
            .validUntil(UPDATED_VALID_UNTIL)
            .periodText(UPDATED_PERIOD_TEXT)
            .currency(UPDATED_CURRENCY)
            .total(UPDATED_TOTAL)
            .vatIncluded(UPDATED_VAT_INCLUDED)
            .discountRate(UPDATED_DISCOUNT_RATE)
            .discountType(UPDATED_DISCOUNT_TYPE)
            .acceptOnline(UPDATED_ACCEPT_ONLINE)
            .acceptOnlineUrl(UPDATED_ACCEPT_ONLINE_URL)
            .acceptOnlineStatus(UPDATED_ACCEPT_ONLINE_STATUS)
            .language(UPDATED_LANGUAGE)
            .pageAmount(UPDATED_PAGE_AMOUNT)
            .notes(UPDATED_NOTES)
            .status(UPDATED_STATUS)
            .created(UPDATED_CREATED);
        OfferDTO offerDTO = offerMapper.toDto(updatedOffer);

        restOfferMockMvc
            .perform(
                put(ENTITY_API_URL_ID, offerDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(offerDTO))
            )
            .andExpect(status().isOk());

        // Validate the Offer in the database
        List<Offer> offerList = offerRepository.findAll();
        assertThat(offerList).hasSize(databaseSizeBeforeUpdate);
        Offer testOffer = offerList.get(offerList.size() - 1);
        assertThat(testOffer.getRemoteId()).isEqualTo(UPDATED_REMOTE_ID);
        assertThat(testOffer.getNumber()).isEqualTo(UPDATED_NUMBER);
        assertThat(testOffer.getContactName()).isEqualTo(UPDATED_CONTACT_NAME);
        assertThat(testOffer.getDate()).isEqualTo(UPDATED_DATE);
        assertThat(testOffer.getValidUntil()).isEqualTo(UPDATED_VALID_UNTIL);
        assertThat(testOffer.getPeriodText()).isEqualTo(UPDATED_PERIOD_TEXT);
        assertThat(testOffer.getCurrency()).isEqualTo(UPDATED_CURRENCY);
        assertThat(testOffer.getTotal()).isEqualTo(UPDATED_TOTAL);
        assertThat(testOffer.getVatIncluded()).isEqualTo(UPDATED_VAT_INCLUDED);
        assertThat(testOffer.getDiscountRate()).isEqualTo(UPDATED_DISCOUNT_RATE);
        assertThat(testOffer.getDiscountType()).isEqualTo(UPDATED_DISCOUNT_TYPE);
        assertThat(testOffer.getAcceptOnline()).isEqualTo(UPDATED_ACCEPT_ONLINE);
        assertThat(testOffer.getAcceptOnlineUrl()).isEqualTo(UPDATED_ACCEPT_ONLINE_URL);
        assertThat(testOffer.getAcceptOnlineStatus()).isEqualTo(UPDATED_ACCEPT_ONLINE_STATUS);
        assertThat(testOffer.getLanguage()).isEqualTo(UPDATED_LANGUAGE);
        assertThat(testOffer.getPageAmount()).isEqualTo(UPDATED_PAGE_AMOUNT);
        assertThat(testOffer.getNotes()).isEqualTo(UPDATED_NOTES);
        assertThat(testOffer.getStatus()).isEqualTo(UPDATED_STATUS);
        assertThat(testOffer.getCreated()).isEqualTo(UPDATED_CREATED);
    }

    @Test
    @Transactional
    void putNonExistingOffer() throws Exception {
        int databaseSizeBeforeUpdate = offerRepository.findAll().size();
        offer.setId(count.incrementAndGet());

        // Create the Offer
        OfferDTO offerDTO = offerMapper.toDto(offer);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restOfferMockMvc
            .perform(
                put(ENTITY_API_URL_ID, offerDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(offerDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Offer in the database
        List<Offer> offerList = offerRepository.findAll();
        assertThat(offerList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchOffer() throws Exception {
        int databaseSizeBeforeUpdate = offerRepository.findAll().size();
        offer.setId(count.incrementAndGet());

        // Create the Offer
        OfferDTO offerDTO = offerMapper.toDto(offer);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restOfferMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(offerDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Offer in the database
        List<Offer> offerList = offerRepository.findAll();
        assertThat(offerList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamOffer() throws Exception {
        int databaseSizeBeforeUpdate = offerRepository.findAll().size();
        offer.setId(count.incrementAndGet());

        // Create the Offer
        OfferDTO offerDTO = offerMapper.toDto(offer);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restOfferMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(offerDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Offer in the database
        List<Offer> offerList = offerRepository.findAll();
        assertThat(offerList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateOfferWithPatch() throws Exception {
        // Initialize the database
        offerRepository.saveAndFlush(offer);

        int databaseSizeBeforeUpdate = offerRepository.findAll().size();

        // Update the offer using partial update
        Offer partialUpdatedOffer = new Offer();
        partialUpdatedOffer.setId(offer.getId());

        partialUpdatedOffer
            .total(UPDATED_TOTAL)
            .vatIncluded(UPDATED_VAT_INCLUDED)
            .discountRate(UPDATED_DISCOUNT_RATE)
            .discountType(UPDATED_DISCOUNT_TYPE)
            .acceptOnline(UPDATED_ACCEPT_ONLINE)
            .acceptOnlineUrl(UPDATED_ACCEPT_ONLINE_URL)
            .language(UPDATED_LANGUAGE)
            .pageAmount(UPDATED_PAGE_AMOUNT)
            .notes(UPDATED_NOTES);

        restOfferMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedOffer.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedOffer))
            )
            .andExpect(status().isOk());

        // Validate the Offer in the database
        List<Offer> offerList = offerRepository.findAll();
        assertThat(offerList).hasSize(databaseSizeBeforeUpdate);
        Offer testOffer = offerList.get(offerList.size() - 1);
        assertThat(testOffer.getRemoteId()).isEqualTo(DEFAULT_REMOTE_ID);
        assertThat(testOffer.getNumber()).isEqualTo(DEFAULT_NUMBER);
        assertThat(testOffer.getContactName()).isEqualTo(DEFAULT_CONTACT_NAME);
        assertThat(testOffer.getDate()).isEqualTo(DEFAULT_DATE);
        assertThat(testOffer.getValidUntil()).isEqualTo(DEFAULT_VALID_UNTIL);
        assertThat(testOffer.getPeriodText()).isEqualTo(DEFAULT_PERIOD_TEXT);
        assertThat(testOffer.getCurrency()).isEqualTo(DEFAULT_CURRENCY);
        assertThat(testOffer.getTotal()).isEqualTo(UPDATED_TOTAL);
        assertThat(testOffer.getVatIncluded()).isEqualTo(UPDATED_VAT_INCLUDED);
        assertThat(testOffer.getDiscountRate()).isEqualTo(UPDATED_DISCOUNT_RATE);
        assertThat(testOffer.getDiscountType()).isEqualTo(UPDATED_DISCOUNT_TYPE);
        assertThat(testOffer.getAcceptOnline()).isEqualTo(UPDATED_ACCEPT_ONLINE);
        assertThat(testOffer.getAcceptOnlineUrl()).isEqualTo(UPDATED_ACCEPT_ONLINE_URL);
        assertThat(testOffer.getAcceptOnlineStatus()).isEqualTo(DEFAULT_ACCEPT_ONLINE_STATUS);
        assertThat(testOffer.getLanguage()).isEqualTo(UPDATED_LANGUAGE);
        assertThat(testOffer.getPageAmount()).isEqualTo(UPDATED_PAGE_AMOUNT);
        assertThat(testOffer.getNotes()).isEqualTo(UPDATED_NOTES);
        assertThat(testOffer.getStatus()).isEqualTo(DEFAULT_STATUS);
        assertThat(testOffer.getCreated()).isEqualTo(DEFAULT_CREATED);
    }

    @Test
    @Transactional
    void fullUpdateOfferWithPatch() throws Exception {
        // Initialize the database
        offerRepository.saveAndFlush(offer);

        int databaseSizeBeforeUpdate = offerRepository.findAll().size();

        // Update the offer using partial update
        Offer partialUpdatedOffer = new Offer();
        partialUpdatedOffer.setId(offer.getId());

        partialUpdatedOffer
            .remoteId(UPDATED_REMOTE_ID)
            .number(UPDATED_NUMBER)
            .contactName(UPDATED_CONTACT_NAME)
            .date(UPDATED_DATE)
            .validUntil(UPDATED_VALID_UNTIL)
            .periodText(UPDATED_PERIOD_TEXT)
            .currency(UPDATED_CURRENCY)
            .total(UPDATED_TOTAL)
            .vatIncluded(UPDATED_VAT_INCLUDED)
            .discountRate(UPDATED_DISCOUNT_RATE)
            .discountType(UPDATED_DISCOUNT_TYPE)
            .acceptOnline(UPDATED_ACCEPT_ONLINE)
            .acceptOnlineUrl(UPDATED_ACCEPT_ONLINE_URL)
            .acceptOnlineStatus(UPDATED_ACCEPT_ONLINE_STATUS)
            .language(UPDATED_LANGUAGE)
            .pageAmount(UPDATED_PAGE_AMOUNT)
            .notes(UPDATED_NOTES)
            .status(UPDATED_STATUS)
            .created(UPDATED_CREATED);

        restOfferMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedOffer.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedOffer))
            )
            .andExpect(status().isOk());

        // Validate the Offer in the database
        List<Offer> offerList = offerRepository.findAll();
        assertThat(offerList).hasSize(databaseSizeBeforeUpdate);
        Offer testOffer = offerList.get(offerList.size() - 1);
        assertThat(testOffer.getRemoteId()).isEqualTo(UPDATED_REMOTE_ID);
        assertThat(testOffer.getNumber()).isEqualTo(UPDATED_NUMBER);
        assertThat(testOffer.getContactName()).isEqualTo(UPDATED_CONTACT_NAME);
        assertThat(testOffer.getDate()).isEqualTo(UPDATED_DATE);
        assertThat(testOffer.getValidUntil()).isEqualTo(UPDATED_VALID_UNTIL);
        assertThat(testOffer.getPeriodText()).isEqualTo(UPDATED_PERIOD_TEXT);
        assertThat(testOffer.getCurrency()).isEqualTo(UPDATED_CURRENCY);
        assertThat(testOffer.getTotal()).isEqualTo(UPDATED_TOTAL);
        assertThat(testOffer.getVatIncluded()).isEqualTo(UPDATED_VAT_INCLUDED);
        assertThat(testOffer.getDiscountRate()).isEqualTo(UPDATED_DISCOUNT_RATE);
        assertThat(testOffer.getDiscountType()).isEqualTo(UPDATED_DISCOUNT_TYPE);
        assertThat(testOffer.getAcceptOnline()).isEqualTo(UPDATED_ACCEPT_ONLINE);
        assertThat(testOffer.getAcceptOnlineUrl()).isEqualTo(UPDATED_ACCEPT_ONLINE_URL);
        assertThat(testOffer.getAcceptOnlineStatus()).isEqualTo(UPDATED_ACCEPT_ONLINE_STATUS);
        assertThat(testOffer.getLanguage()).isEqualTo(UPDATED_LANGUAGE);
        assertThat(testOffer.getPageAmount()).isEqualTo(UPDATED_PAGE_AMOUNT);
        assertThat(testOffer.getNotes()).isEqualTo(UPDATED_NOTES);
        assertThat(testOffer.getStatus()).isEqualTo(UPDATED_STATUS);
        assertThat(testOffer.getCreated()).isEqualTo(UPDATED_CREATED);
    }

    @Test
    @Transactional
    void patchNonExistingOffer() throws Exception {
        int databaseSizeBeforeUpdate = offerRepository.findAll().size();
        offer.setId(count.incrementAndGet());

        // Create the Offer
        OfferDTO offerDTO = offerMapper.toDto(offer);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restOfferMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, offerDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(offerDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Offer in the database
        List<Offer> offerList = offerRepository.findAll();
        assertThat(offerList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchOffer() throws Exception {
        int databaseSizeBeforeUpdate = offerRepository.findAll().size();
        offer.setId(count.incrementAndGet());

        // Create the Offer
        OfferDTO offerDTO = offerMapper.toDto(offer);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restOfferMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(offerDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Offer in the database
        List<Offer> offerList = offerRepository.findAll();
        assertThat(offerList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamOffer() throws Exception {
        int databaseSizeBeforeUpdate = offerRepository.findAll().size();
        offer.setId(count.incrementAndGet());

        // Create the Offer
        OfferDTO offerDTO = offerMapper.toDto(offer);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restOfferMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(offerDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Offer in the database
        List<Offer> offerList = offerRepository.findAll();
        assertThat(offerList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteOffer() throws Exception {
        // Initialize the database
        offerRepository.saveAndFlush(offer);

        int databaseSizeBeforeDelete = offerRepository.findAll().size();

        // Delete the offer
        restOfferMockMvc
            .perform(delete(ENTITY_API_URL_ID, offer.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Offer> offerList = offerRepository.findAll();
        assertThat(offerList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
