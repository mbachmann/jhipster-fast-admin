package ch.united.fastadmin.web.rest;

import static ch.united.fastadmin.web.rest.TestUtil.sameInstant;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import ch.united.fastadmin.IntegrationTest;
import ch.united.fastadmin.domain.Contact;
import ch.united.fastadmin.domain.ContactAddress;
import ch.united.fastadmin.domain.ContactPerson;
import ch.united.fastadmin.domain.DescriptiveDocumentText;
import ch.united.fastadmin.domain.DocumentFreeText;
import ch.united.fastadmin.domain.DocumentPosition;
import ch.united.fastadmin.domain.Layout;
import ch.united.fastadmin.domain.Offer;
import ch.united.fastadmin.domain.Signature;
import ch.united.fastadmin.domain.enumeration.Currency;
import ch.united.fastadmin.domain.enumeration.DiscountType;
import ch.united.fastadmin.domain.enumeration.DocumentLanguage;
import ch.united.fastadmin.domain.enumeration.OfferAcceptOnlineStatus;
import ch.united.fastadmin.domain.enumeration.OfferStatus;
import ch.united.fastadmin.repository.OfferRepository;
import ch.united.fastadmin.service.criteria.OfferCriteria;
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
    private static final Integer SMALLER_REMOTE_ID = 1 - 1;

    private static final String DEFAULT_NUMBER = "AAAAAAAAAA";
    private static final String UPDATED_NUMBER = "BBBBBBBBBB";

    private static final String DEFAULT_CONTACT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_CONTACT_NAME = "BBBBBBBBBB";

    private static final LocalDate DEFAULT_DATE = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_DATE = LocalDate.now(ZoneId.systemDefault());
    private static final LocalDate SMALLER_DATE = LocalDate.ofEpochDay(-1L);

    private static final LocalDate DEFAULT_VALID_UNTIL = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_VALID_UNTIL = LocalDate.now(ZoneId.systemDefault());
    private static final LocalDate SMALLER_VALID_UNTIL = LocalDate.ofEpochDay(-1L);

    private static final String DEFAULT_PERIOD_TEXT = "AAAAAAAAAA";
    private static final String UPDATED_PERIOD_TEXT = "BBBBBBBBBB";

    private static final Currency DEFAULT_CURRENCY = Currency.AED;
    private static final Currency UPDATED_CURRENCY = Currency.AFN;

    private static final Double DEFAULT_TOTAL = 1D;
    private static final Double UPDATED_TOTAL = 2D;
    private static final Double SMALLER_TOTAL = 1D - 1D;

    private static final Boolean DEFAULT_VAT_INCLUDED = false;
    private static final Boolean UPDATED_VAT_INCLUDED = true;

    private static final Double DEFAULT_DISCOUNT_RATE = 1D;
    private static final Double UPDATED_DISCOUNT_RATE = 2D;
    private static final Double SMALLER_DISCOUNT_RATE = 1D - 1D;

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
    private static final Integer SMALLER_PAGE_AMOUNT = 1 - 1;

    private static final String DEFAULT_NOTES = "AAAAAAAAAA";
    private static final String UPDATED_NOTES = "BBBBBBBBBB";

    private static final OfferStatus DEFAULT_STATUS = OfferStatus.DRAFT;
    private static final OfferStatus UPDATED_STATUS = OfferStatus.SENT;

    private static final ZonedDateTime DEFAULT_CREATED = ZonedDateTime.ofInstant(Instant.ofEpochMilli(0L), ZoneOffset.UTC);
    private static final ZonedDateTime UPDATED_CREATED = ZonedDateTime.now(ZoneId.systemDefault()).withNano(0);
    private static final ZonedDateTime SMALLER_CREATED = ZonedDateTime.ofInstant(Instant.ofEpochMilli(-1L), ZoneOffset.UTC);

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
    void getOffersByIdFiltering() throws Exception {
        // Initialize the database
        offerRepository.saveAndFlush(offer);

        Long id = offer.getId();

        defaultOfferShouldBeFound("id.equals=" + id);
        defaultOfferShouldNotBeFound("id.notEquals=" + id);

        defaultOfferShouldBeFound("id.greaterThanOrEqual=" + id);
        defaultOfferShouldNotBeFound("id.greaterThan=" + id);

        defaultOfferShouldBeFound("id.lessThanOrEqual=" + id);
        defaultOfferShouldNotBeFound("id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllOffersByRemoteIdIsEqualToSomething() throws Exception {
        // Initialize the database
        offerRepository.saveAndFlush(offer);

        // Get all the offerList where remoteId equals to DEFAULT_REMOTE_ID
        defaultOfferShouldBeFound("remoteId.equals=" + DEFAULT_REMOTE_ID);

        // Get all the offerList where remoteId equals to UPDATED_REMOTE_ID
        defaultOfferShouldNotBeFound("remoteId.equals=" + UPDATED_REMOTE_ID);
    }

    @Test
    @Transactional
    void getAllOffersByRemoteIdIsNotEqualToSomething() throws Exception {
        // Initialize the database
        offerRepository.saveAndFlush(offer);

        // Get all the offerList where remoteId not equals to DEFAULT_REMOTE_ID
        defaultOfferShouldNotBeFound("remoteId.notEquals=" + DEFAULT_REMOTE_ID);

        // Get all the offerList where remoteId not equals to UPDATED_REMOTE_ID
        defaultOfferShouldBeFound("remoteId.notEquals=" + UPDATED_REMOTE_ID);
    }

    @Test
    @Transactional
    void getAllOffersByRemoteIdIsInShouldWork() throws Exception {
        // Initialize the database
        offerRepository.saveAndFlush(offer);

        // Get all the offerList where remoteId in DEFAULT_REMOTE_ID or UPDATED_REMOTE_ID
        defaultOfferShouldBeFound("remoteId.in=" + DEFAULT_REMOTE_ID + "," + UPDATED_REMOTE_ID);

        // Get all the offerList where remoteId equals to UPDATED_REMOTE_ID
        defaultOfferShouldNotBeFound("remoteId.in=" + UPDATED_REMOTE_ID);
    }

    @Test
    @Transactional
    void getAllOffersByRemoteIdIsNullOrNotNull() throws Exception {
        // Initialize the database
        offerRepository.saveAndFlush(offer);

        // Get all the offerList where remoteId is not null
        defaultOfferShouldBeFound("remoteId.specified=true");

        // Get all the offerList where remoteId is null
        defaultOfferShouldNotBeFound("remoteId.specified=false");
    }

    @Test
    @Transactional
    void getAllOffersByRemoteIdIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        offerRepository.saveAndFlush(offer);

        // Get all the offerList where remoteId is greater than or equal to DEFAULT_REMOTE_ID
        defaultOfferShouldBeFound("remoteId.greaterThanOrEqual=" + DEFAULT_REMOTE_ID);

        // Get all the offerList where remoteId is greater than or equal to UPDATED_REMOTE_ID
        defaultOfferShouldNotBeFound("remoteId.greaterThanOrEqual=" + UPDATED_REMOTE_ID);
    }

    @Test
    @Transactional
    void getAllOffersByRemoteIdIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        offerRepository.saveAndFlush(offer);

        // Get all the offerList where remoteId is less than or equal to DEFAULT_REMOTE_ID
        defaultOfferShouldBeFound("remoteId.lessThanOrEqual=" + DEFAULT_REMOTE_ID);

        // Get all the offerList where remoteId is less than or equal to SMALLER_REMOTE_ID
        defaultOfferShouldNotBeFound("remoteId.lessThanOrEqual=" + SMALLER_REMOTE_ID);
    }

    @Test
    @Transactional
    void getAllOffersByRemoteIdIsLessThanSomething() throws Exception {
        // Initialize the database
        offerRepository.saveAndFlush(offer);

        // Get all the offerList where remoteId is less than DEFAULT_REMOTE_ID
        defaultOfferShouldNotBeFound("remoteId.lessThan=" + DEFAULT_REMOTE_ID);

        // Get all the offerList where remoteId is less than UPDATED_REMOTE_ID
        defaultOfferShouldBeFound("remoteId.lessThan=" + UPDATED_REMOTE_ID);
    }

    @Test
    @Transactional
    void getAllOffersByRemoteIdIsGreaterThanSomething() throws Exception {
        // Initialize the database
        offerRepository.saveAndFlush(offer);

        // Get all the offerList where remoteId is greater than DEFAULT_REMOTE_ID
        defaultOfferShouldNotBeFound("remoteId.greaterThan=" + DEFAULT_REMOTE_ID);

        // Get all the offerList where remoteId is greater than SMALLER_REMOTE_ID
        defaultOfferShouldBeFound("remoteId.greaterThan=" + SMALLER_REMOTE_ID);
    }

    @Test
    @Transactional
    void getAllOffersByNumberIsEqualToSomething() throws Exception {
        // Initialize the database
        offerRepository.saveAndFlush(offer);

        // Get all the offerList where number equals to DEFAULT_NUMBER
        defaultOfferShouldBeFound("number.equals=" + DEFAULT_NUMBER);

        // Get all the offerList where number equals to UPDATED_NUMBER
        defaultOfferShouldNotBeFound("number.equals=" + UPDATED_NUMBER);
    }

    @Test
    @Transactional
    void getAllOffersByNumberIsNotEqualToSomething() throws Exception {
        // Initialize the database
        offerRepository.saveAndFlush(offer);

        // Get all the offerList where number not equals to DEFAULT_NUMBER
        defaultOfferShouldNotBeFound("number.notEquals=" + DEFAULT_NUMBER);

        // Get all the offerList where number not equals to UPDATED_NUMBER
        defaultOfferShouldBeFound("number.notEquals=" + UPDATED_NUMBER);
    }

    @Test
    @Transactional
    void getAllOffersByNumberIsInShouldWork() throws Exception {
        // Initialize the database
        offerRepository.saveAndFlush(offer);

        // Get all the offerList where number in DEFAULT_NUMBER or UPDATED_NUMBER
        defaultOfferShouldBeFound("number.in=" + DEFAULT_NUMBER + "," + UPDATED_NUMBER);

        // Get all the offerList where number equals to UPDATED_NUMBER
        defaultOfferShouldNotBeFound("number.in=" + UPDATED_NUMBER);
    }

    @Test
    @Transactional
    void getAllOffersByNumberIsNullOrNotNull() throws Exception {
        // Initialize the database
        offerRepository.saveAndFlush(offer);

        // Get all the offerList where number is not null
        defaultOfferShouldBeFound("number.specified=true");

        // Get all the offerList where number is null
        defaultOfferShouldNotBeFound("number.specified=false");
    }

    @Test
    @Transactional
    void getAllOffersByNumberContainsSomething() throws Exception {
        // Initialize the database
        offerRepository.saveAndFlush(offer);

        // Get all the offerList where number contains DEFAULT_NUMBER
        defaultOfferShouldBeFound("number.contains=" + DEFAULT_NUMBER);

        // Get all the offerList where number contains UPDATED_NUMBER
        defaultOfferShouldNotBeFound("number.contains=" + UPDATED_NUMBER);
    }

    @Test
    @Transactional
    void getAllOffersByNumberNotContainsSomething() throws Exception {
        // Initialize the database
        offerRepository.saveAndFlush(offer);

        // Get all the offerList where number does not contain DEFAULT_NUMBER
        defaultOfferShouldNotBeFound("number.doesNotContain=" + DEFAULT_NUMBER);

        // Get all the offerList where number does not contain UPDATED_NUMBER
        defaultOfferShouldBeFound("number.doesNotContain=" + UPDATED_NUMBER);
    }

    @Test
    @Transactional
    void getAllOffersByContactNameIsEqualToSomething() throws Exception {
        // Initialize the database
        offerRepository.saveAndFlush(offer);

        // Get all the offerList where contactName equals to DEFAULT_CONTACT_NAME
        defaultOfferShouldBeFound("contactName.equals=" + DEFAULT_CONTACT_NAME);

        // Get all the offerList where contactName equals to UPDATED_CONTACT_NAME
        defaultOfferShouldNotBeFound("contactName.equals=" + UPDATED_CONTACT_NAME);
    }

    @Test
    @Transactional
    void getAllOffersByContactNameIsNotEqualToSomething() throws Exception {
        // Initialize the database
        offerRepository.saveAndFlush(offer);

        // Get all the offerList where contactName not equals to DEFAULT_CONTACT_NAME
        defaultOfferShouldNotBeFound("contactName.notEquals=" + DEFAULT_CONTACT_NAME);

        // Get all the offerList where contactName not equals to UPDATED_CONTACT_NAME
        defaultOfferShouldBeFound("contactName.notEquals=" + UPDATED_CONTACT_NAME);
    }

    @Test
    @Transactional
    void getAllOffersByContactNameIsInShouldWork() throws Exception {
        // Initialize the database
        offerRepository.saveAndFlush(offer);

        // Get all the offerList where contactName in DEFAULT_CONTACT_NAME or UPDATED_CONTACT_NAME
        defaultOfferShouldBeFound("contactName.in=" + DEFAULT_CONTACT_NAME + "," + UPDATED_CONTACT_NAME);

        // Get all the offerList where contactName equals to UPDATED_CONTACT_NAME
        defaultOfferShouldNotBeFound("contactName.in=" + UPDATED_CONTACT_NAME);
    }

    @Test
    @Transactional
    void getAllOffersByContactNameIsNullOrNotNull() throws Exception {
        // Initialize the database
        offerRepository.saveAndFlush(offer);

        // Get all the offerList where contactName is not null
        defaultOfferShouldBeFound("contactName.specified=true");

        // Get all the offerList where contactName is null
        defaultOfferShouldNotBeFound("contactName.specified=false");
    }

    @Test
    @Transactional
    void getAllOffersByContactNameContainsSomething() throws Exception {
        // Initialize the database
        offerRepository.saveAndFlush(offer);

        // Get all the offerList where contactName contains DEFAULT_CONTACT_NAME
        defaultOfferShouldBeFound("contactName.contains=" + DEFAULT_CONTACT_NAME);

        // Get all the offerList where contactName contains UPDATED_CONTACT_NAME
        defaultOfferShouldNotBeFound("contactName.contains=" + UPDATED_CONTACT_NAME);
    }

    @Test
    @Transactional
    void getAllOffersByContactNameNotContainsSomething() throws Exception {
        // Initialize the database
        offerRepository.saveAndFlush(offer);

        // Get all the offerList where contactName does not contain DEFAULT_CONTACT_NAME
        defaultOfferShouldNotBeFound("contactName.doesNotContain=" + DEFAULT_CONTACT_NAME);

        // Get all the offerList where contactName does not contain UPDATED_CONTACT_NAME
        defaultOfferShouldBeFound("contactName.doesNotContain=" + UPDATED_CONTACT_NAME);
    }

    @Test
    @Transactional
    void getAllOffersByDateIsEqualToSomething() throws Exception {
        // Initialize the database
        offerRepository.saveAndFlush(offer);

        // Get all the offerList where date equals to DEFAULT_DATE
        defaultOfferShouldBeFound("date.equals=" + DEFAULT_DATE);

        // Get all the offerList where date equals to UPDATED_DATE
        defaultOfferShouldNotBeFound("date.equals=" + UPDATED_DATE);
    }

    @Test
    @Transactional
    void getAllOffersByDateIsNotEqualToSomething() throws Exception {
        // Initialize the database
        offerRepository.saveAndFlush(offer);

        // Get all the offerList where date not equals to DEFAULT_DATE
        defaultOfferShouldNotBeFound("date.notEquals=" + DEFAULT_DATE);

        // Get all the offerList where date not equals to UPDATED_DATE
        defaultOfferShouldBeFound("date.notEquals=" + UPDATED_DATE);
    }

    @Test
    @Transactional
    void getAllOffersByDateIsInShouldWork() throws Exception {
        // Initialize the database
        offerRepository.saveAndFlush(offer);

        // Get all the offerList where date in DEFAULT_DATE or UPDATED_DATE
        defaultOfferShouldBeFound("date.in=" + DEFAULT_DATE + "," + UPDATED_DATE);

        // Get all the offerList where date equals to UPDATED_DATE
        defaultOfferShouldNotBeFound("date.in=" + UPDATED_DATE);
    }

    @Test
    @Transactional
    void getAllOffersByDateIsNullOrNotNull() throws Exception {
        // Initialize the database
        offerRepository.saveAndFlush(offer);

        // Get all the offerList where date is not null
        defaultOfferShouldBeFound("date.specified=true");

        // Get all the offerList where date is null
        defaultOfferShouldNotBeFound("date.specified=false");
    }

    @Test
    @Transactional
    void getAllOffersByDateIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        offerRepository.saveAndFlush(offer);

        // Get all the offerList where date is greater than or equal to DEFAULT_DATE
        defaultOfferShouldBeFound("date.greaterThanOrEqual=" + DEFAULT_DATE);

        // Get all the offerList where date is greater than or equal to UPDATED_DATE
        defaultOfferShouldNotBeFound("date.greaterThanOrEqual=" + UPDATED_DATE);
    }

    @Test
    @Transactional
    void getAllOffersByDateIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        offerRepository.saveAndFlush(offer);

        // Get all the offerList where date is less than or equal to DEFAULT_DATE
        defaultOfferShouldBeFound("date.lessThanOrEqual=" + DEFAULT_DATE);

        // Get all the offerList where date is less than or equal to SMALLER_DATE
        defaultOfferShouldNotBeFound("date.lessThanOrEqual=" + SMALLER_DATE);
    }

    @Test
    @Transactional
    void getAllOffersByDateIsLessThanSomething() throws Exception {
        // Initialize the database
        offerRepository.saveAndFlush(offer);

        // Get all the offerList where date is less than DEFAULT_DATE
        defaultOfferShouldNotBeFound("date.lessThan=" + DEFAULT_DATE);

        // Get all the offerList where date is less than UPDATED_DATE
        defaultOfferShouldBeFound("date.lessThan=" + UPDATED_DATE);
    }

    @Test
    @Transactional
    void getAllOffersByDateIsGreaterThanSomething() throws Exception {
        // Initialize the database
        offerRepository.saveAndFlush(offer);

        // Get all the offerList where date is greater than DEFAULT_DATE
        defaultOfferShouldNotBeFound("date.greaterThan=" + DEFAULT_DATE);

        // Get all the offerList where date is greater than SMALLER_DATE
        defaultOfferShouldBeFound("date.greaterThan=" + SMALLER_DATE);
    }

    @Test
    @Transactional
    void getAllOffersByValidUntilIsEqualToSomething() throws Exception {
        // Initialize the database
        offerRepository.saveAndFlush(offer);

        // Get all the offerList where validUntil equals to DEFAULT_VALID_UNTIL
        defaultOfferShouldBeFound("validUntil.equals=" + DEFAULT_VALID_UNTIL);

        // Get all the offerList where validUntil equals to UPDATED_VALID_UNTIL
        defaultOfferShouldNotBeFound("validUntil.equals=" + UPDATED_VALID_UNTIL);
    }

    @Test
    @Transactional
    void getAllOffersByValidUntilIsNotEqualToSomething() throws Exception {
        // Initialize the database
        offerRepository.saveAndFlush(offer);

        // Get all the offerList where validUntil not equals to DEFAULT_VALID_UNTIL
        defaultOfferShouldNotBeFound("validUntil.notEquals=" + DEFAULT_VALID_UNTIL);

        // Get all the offerList where validUntil not equals to UPDATED_VALID_UNTIL
        defaultOfferShouldBeFound("validUntil.notEquals=" + UPDATED_VALID_UNTIL);
    }

    @Test
    @Transactional
    void getAllOffersByValidUntilIsInShouldWork() throws Exception {
        // Initialize the database
        offerRepository.saveAndFlush(offer);

        // Get all the offerList where validUntil in DEFAULT_VALID_UNTIL or UPDATED_VALID_UNTIL
        defaultOfferShouldBeFound("validUntil.in=" + DEFAULT_VALID_UNTIL + "," + UPDATED_VALID_UNTIL);

        // Get all the offerList where validUntil equals to UPDATED_VALID_UNTIL
        defaultOfferShouldNotBeFound("validUntil.in=" + UPDATED_VALID_UNTIL);
    }

    @Test
    @Transactional
    void getAllOffersByValidUntilIsNullOrNotNull() throws Exception {
        // Initialize the database
        offerRepository.saveAndFlush(offer);

        // Get all the offerList where validUntil is not null
        defaultOfferShouldBeFound("validUntil.specified=true");

        // Get all the offerList where validUntil is null
        defaultOfferShouldNotBeFound("validUntil.specified=false");
    }

    @Test
    @Transactional
    void getAllOffersByValidUntilIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        offerRepository.saveAndFlush(offer);

        // Get all the offerList where validUntil is greater than or equal to DEFAULT_VALID_UNTIL
        defaultOfferShouldBeFound("validUntil.greaterThanOrEqual=" + DEFAULT_VALID_UNTIL);

        // Get all the offerList where validUntil is greater than or equal to UPDATED_VALID_UNTIL
        defaultOfferShouldNotBeFound("validUntil.greaterThanOrEqual=" + UPDATED_VALID_UNTIL);
    }

    @Test
    @Transactional
    void getAllOffersByValidUntilIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        offerRepository.saveAndFlush(offer);

        // Get all the offerList where validUntil is less than or equal to DEFAULT_VALID_UNTIL
        defaultOfferShouldBeFound("validUntil.lessThanOrEqual=" + DEFAULT_VALID_UNTIL);

        // Get all the offerList where validUntil is less than or equal to SMALLER_VALID_UNTIL
        defaultOfferShouldNotBeFound("validUntil.lessThanOrEqual=" + SMALLER_VALID_UNTIL);
    }

    @Test
    @Transactional
    void getAllOffersByValidUntilIsLessThanSomething() throws Exception {
        // Initialize the database
        offerRepository.saveAndFlush(offer);

        // Get all the offerList where validUntil is less than DEFAULT_VALID_UNTIL
        defaultOfferShouldNotBeFound("validUntil.lessThan=" + DEFAULT_VALID_UNTIL);

        // Get all the offerList where validUntil is less than UPDATED_VALID_UNTIL
        defaultOfferShouldBeFound("validUntil.lessThan=" + UPDATED_VALID_UNTIL);
    }

    @Test
    @Transactional
    void getAllOffersByValidUntilIsGreaterThanSomething() throws Exception {
        // Initialize the database
        offerRepository.saveAndFlush(offer);

        // Get all the offerList where validUntil is greater than DEFAULT_VALID_UNTIL
        defaultOfferShouldNotBeFound("validUntil.greaterThan=" + DEFAULT_VALID_UNTIL);

        // Get all the offerList where validUntil is greater than SMALLER_VALID_UNTIL
        defaultOfferShouldBeFound("validUntil.greaterThan=" + SMALLER_VALID_UNTIL);
    }

    @Test
    @Transactional
    void getAllOffersByPeriodTextIsEqualToSomething() throws Exception {
        // Initialize the database
        offerRepository.saveAndFlush(offer);

        // Get all the offerList where periodText equals to DEFAULT_PERIOD_TEXT
        defaultOfferShouldBeFound("periodText.equals=" + DEFAULT_PERIOD_TEXT);

        // Get all the offerList where periodText equals to UPDATED_PERIOD_TEXT
        defaultOfferShouldNotBeFound("periodText.equals=" + UPDATED_PERIOD_TEXT);
    }

    @Test
    @Transactional
    void getAllOffersByPeriodTextIsNotEqualToSomething() throws Exception {
        // Initialize the database
        offerRepository.saveAndFlush(offer);

        // Get all the offerList where periodText not equals to DEFAULT_PERIOD_TEXT
        defaultOfferShouldNotBeFound("periodText.notEquals=" + DEFAULT_PERIOD_TEXT);

        // Get all the offerList where periodText not equals to UPDATED_PERIOD_TEXT
        defaultOfferShouldBeFound("periodText.notEquals=" + UPDATED_PERIOD_TEXT);
    }

    @Test
    @Transactional
    void getAllOffersByPeriodTextIsInShouldWork() throws Exception {
        // Initialize the database
        offerRepository.saveAndFlush(offer);

        // Get all the offerList where periodText in DEFAULT_PERIOD_TEXT or UPDATED_PERIOD_TEXT
        defaultOfferShouldBeFound("periodText.in=" + DEFAULT_PERIOD_TEXT + "," + UPDATED_PERIOD_TEXT);

        // Get all the offerList where periodText equals to UPDATED_PERIOD_TEXT
        defaultOfferShouldNotBeFound("periodText.in=" + UPDATED_PERIOD_TEXT);
    }

    @Test
    @Transactional
    void getAllOffersByPeriodTextIsNullOrNotNull() throws Exception {
        // Initialize the database
        offerRepository.saveAndFlush(offer);

        // Get all the offerList where periodText is not null
        defaultOfferShouldBeFound("periodText.specified=true");

        // Get all the offerList where periodText is null
        defaultOfferShouldNotBeFound("periodText.specified=false");
    }

    @Test
    @Transactional
    void getAllOffersByPeriodTextContainsSomething() throws Exception {
        // Initialize the database
        offerRepository.saveAndFlush(offer);

        // Get all the offerList where periodText contains DEFAULT_PERIOD_TEXT
        defaultOfferShouldBeFound("periodText.contains=" + DEFAULT_PERIOD_TEXT);

        // Get all the offerList where periodText contains UPDATED_PERIOD_TEXT
        defaultOfferShouldNotBeFound("periodText.contains=" + UPDATED_PERIOD_TEXT);
    }

    @Test
    @Transactional
    void getAllOffersByPeriodTextNotContainsSomething() throws Exception {
        // Initialize the database
        offerRepository.saveAndFlush(offer);

        // Get all the offerList where periodText does not contain DEFAULT_PERIOD_TEXT
        defaultOfferShouldNotBeFound("periodText.doesNotContain=" + DEFAULT_PERIOD_TEXT);

        // Get all the offerList where periodText does not contain UPDATED_PERIOD_TEXT
        defaultOfferShouldBeFound("periodText.doesNotContain=" + UPDATED_PERIOD_TEXT);
    }

    @Test
    @Transactional
    void getAllOffersByCurrencyIsEqualToSomething() throws Exception {
        // Initialize the database
        offerRepository.saveAndFlush(offer);

        // Get all the offerList where currency equals to DEFAULT_CURRENCY
        defaultOfferShouldBeFound("currency.equals=" + DEFAULT_CURRENCY);

        // Get all the offerList where currency equals to UPDATED_CURRENCY
        defaultOfferShouldNotBeFound("currency.equals=" + UPDATED_CURRENCY);
    }

    @Test
    @Transactional
    void getAllOffersByCurrencyIsNotEqualToSomething() throws Exception {
        // Initialize the database
        offerRepository.saveAndFlush(offer);

        // Get all the offerList where currency not equals to DEFAULT_CURRENCY
        defaultOfferShouldNotBeFound("currency.notEquals=" + DEFAULT_CURRENCY);

        // Get all the offerList where currency not equals to UPDATED_CURRENCY
        defaultOfferShouldBeFound("currency.notEquals=" + UPDATED_CURRENCY);
    }

    @Test
    @Transactional
    void getAllOffersByCurrencyIsInShouldWork() throws Exception {
        // Initialize the database
        offerRepository.saveAndFlush(offer);

        // Get all the offerList where currency in DEFAULT_CURRENCY or UPDATED_CURRENCY
        defaultOfferShouldBeFound("currency.in=" + DEFAULT_CURRENCY + "," + UPDATED_CURRENCY);

        // Get all the offerList where currency equals to UPDATED_CURRENCY
        defaultOfferShouldNotBeFound("currency.in=" + UPDATED_CURRENCY);
    }

    @Test
    @Transactional
    void getAllOffersByCurrencyIsNullOrNotNull() throws Exception {
        // Initialize the database
        offerRepository.saveAndFlush(offer);

        // Get all the offerList where currency is not null
        defaultOfferShouldBeFound("currency.specified=true");

        // Get all the offerList where currency is null
        defaultOfferShouldNotBeFound("currency.specified=false");
    }

    @Test
    @Transactional
    void getAllOffersByTotalIsEqualToSomething() throws Exception {
        // Initialize the database
        offerRepository.saveAndFlush(offer);

        // Get all the offerList where total equals to DEFAULT_TOTAL
        defaultOfferShouldBeFound("total.equals=" + DEFAULT_TOTAL);

        // Get all the offerList where total equals to UPDATED_TOTAL
        defaultOfferShouldNotBeFound("total.equals=" + UPDATED_TOTAL);
    }

    @Test
    @Transactional
    void getAllOffersByTotalIsNotEqualToSomething() throws Exception {
        // Initialize the database
        offerRepository.saveAndFlush(offer);

        // Get all the offerList where total not equals to DEFAULT_TOTAL
        defaultOfferShouldNotBeFound("total.notEquals=" + DEFAULT_TOTAL);

        // Get all the offerList where total not equals to UPDATED_TOTAL
        defaultOfferShouldBeFound("total.notEquals=" + UPDATED_TOTAL);
    }

    @Test
    @Transactional
    void getAllOffersByTotalIsInShouldWork() throws Exception {
        // Initialize the database
        offerRepository.saveAndFlush(offer);

        // Get all the offerList where total in DEFAULT_TOTAL or UPDATED_TOTAL
        defaultOfferShouldBeFound("total.in=" + DEFAULT_TOTAL + "," + UPDATED_TOTAL);

        // Get all the offerList where total equals to UPDATED_TOTAL
        defaultOfferShouldNotBeFound("total.in=" + UPDATED_TOTAL);
    }

    @Test
    @Transactional
    void getAllOffersByTotalIsNullOrNotNull() throws Exception {
        // Initialize the database
        offerRepository.saveAndFlush(offer);

        // Get all the offerList where total is not null
        defaultOfferShouldBeFound("total.specified=true");

        // Get all the offerList where total is null
        defaultOfferShouldNotBeFound("total.specified=false");
    }

    @Test
    @Transactional
    void getAllOffersByTotalIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        offerRepository.saveAndFlush(offer);

        // Get all the offerList where total is greater than or equal to DEFAULT_TOTAL
        defaultOfferShouldBeFound("total.greaterThanOrEqual=" + DEFAULT_TOTAL);

        // Get all the offerList where total is greater than or equal to UPDATED_TOTAL
        defaultOfferShouldNotBeFound("total.greaterThanOrEqual=" + UPDATED_TOTAL);
    }

    @Test
    @Transactional
    void getAllOffersByTotalIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        offerRepository.saveAndFlush(offer);

        // Get all the offerList where total is less than or equal to DEFAULT_TOTAL
        defaultOfferShouldBeFound("total.lessThanOrEqual=" + DEFAULT_TOTAL);

        // Get all the offerList where total is less than or equal to SMALLER_TOTAL
        defaultOfferShouldNotBeFound("total.lessThanOrEqual=" + SMALLER_TOTAL);
    }

    @Test
    @Transactional
    void getAllOffersByTotalIsLessThanSomething() throws Exception {
        // Initialize the database
        offerRepository.saveAndFlush(offer);

        // Get all the offerList where total is less than DEFAULT_TOTAL
        defaultOfferShouldNotBeFound("total.lessThan=" + DEFAULT_TOTAL);

        // Get all the offerList where total is less than UPDATED_TOTAL
        defaultOfferShouldBeFound("total.lessThan=" + UPDATED_TOTAL);
    }

    @Test
    @Transactional
    void getAllOffersByTotalIsGreaterThanSomething() throws Exception {
        // Initialize the database
        offerRepository.saveAndFlush(offer);

        // Get all the offerList where total is greater than DEFAULT_TOTAL
        defaultOfferShouldNotBeFound("total.greaterThan=" + DEFAULT_TOTAL);

        // Get all the offerList where total is greater than SMALLER_TOTAL
        defaultOfferShouldBeFound("total.greaterThan=" + SMALLER_TOTAL);
    }

    @Test
    @Transactional
    void getAllOffersByVatIncludedIsEqualToSomething() throws Exception {
        // Initialize the database
        offerRepository.saveAndFlush(offer);

        // Get all the offerList where vatIncluded equals to DEFAULT_VAT_INCLUDED
        defaultOfferShouldBeFound("vatIncluded.equals=" + DEFAULT_VAT_INCLUDED);

        // Get all the offerList where vatIncluded equals to UPDATED_VAT_INCLUDED
        defaultOfferShouldNotBeFound("vatIncluded.equals=" + UPDATED_VAT_INCLUDED);
    }

    @Test
    @Transactional
    void getAllOffersByVatIncludedIsNotEqualToSomething() throws Exception {
        // Initialize the database
        offerRepository.saveAndFlush(offer);

        // Get all the offerList where vatIncluded not equals to DEFAULT_VAT_INCLUDED
        defaultOfferShouldNotBeFound("vatIncluded.notEquals=" + DEFAULT_VAT_INCLUDED);

        // Get all the offerList where vatIncluded not equals to UPDATED_VAT_INCLUDED
        defaultOfferShouldBeFound("vatIncluded.notEquals=" + UPDATED_VAT_INCLUDED);
    }

    @Test
    @Transactional
    void getAllOffersByVatIncludedIsInShouldWork() throws Exception {
        // Initialize the database
        offerRepository.saveAndFlush(offer);

        // Get all the offerList where vatIncluded in DEFAULT_VAT_INCLUDED or UPDATED_VAT_INCLUDED
        defaultOfferShouldBeFound("vatIncluded.in=" + DEFAULT_VAT_INCLUDED + "," + UPDATED_VAT_INCLUDED);

        // Get all the offerList where vatIncluded equals to UPDATED_VAT_INCLUDED
        defaultOfferShouldNotBeFound("vatIncluded.in=" + UPDATED_VAT_INCLUDED);
    }

    @Test
    @Transactional
    void getAllOffersByVatIncludedIsNullOrNotNull() throws Exception {
        // Initialize the database
        offerRepository.saveAndFlush(offer);

        // Get all the offerList where vatIncluded is not null
        defaultOfferShouldBeFound("vatIncluded.specified=true");

        // Get all the offerList where vatIncluded is null
        defaultOfferShouldNotBeFound("vatIncluded.specified=false");
    }

    @Test
    @Transactional
    void getAllOffersByDiscountRateIsEqualToSomething() throws Exception {
        // Initialize the database
        offerRepository.saveAndFlush(offer);

        // Get all the offerList where discountRate equals to DEFAULT_DISCOUNT_RATE
        defaultOfferShouldBeFound("discountRate.equals=" + DEFAULT_DISCOUNT_RATE);

        // Get all the offerList where discountRate equals to UPDATED_DISCOUNT_RATE
        defaultOfferShouldNotBeFound("discountRate.equals=" + UPDATED_DISCOUNT_RATE);
    }

    @Test
    @Transactional
    void getAllOffersByDiscountRateIsNotEqualToSomething() throws Exception {
        // Initialize the database
        offerRepository.saveAndFlush(offer);

        // Get all the offerList where discountRate not equals to DEFAULT_DISCOUNT_RATE
        defaultOfferShouldNotBeFound("discountRate.notEquals=" + DEFAULT_DISCOUNT_RATE);

        // Get all the offerList where discountRate not equals to UPDATED_DISCOUNT_RATE
        defaultOfferShouldBeFound("discountRate.notEquals=" + UPDATED_DISCOUNT_RATE);
    }

    @Test
    @Transactional
    void getAllOffersByDiscountRateIsInShouldWork() throws Exception {
        // Initialize the database
        offerRepository.saveAndFlush(offer);

        // Get all the offerList where discountRate in DEFAULT_DISCOUNT_RATE or UPDATED_DISCOUNT_RATE
        defaultOfferShouldBeFound("discountRate.in=" + DEFAULT_DISCOUNT_RATE + "," + UPDATED_DISCOUNT_RATE);

        // Get all the offerList where discountRate equals to UPDATED_DISCOUNT_RATE
        defaultOfferShouldNotBeFound("discountRate.in=" + UPDATED_DISCOUNT_RATE);
    }

    @Test
    @Transactional
    void getAllOffersByDiscountRateIsNullOrNotNull() throws Exception {
        // Initialize the database
        offerRepository.saveAndFlush(offer);

        // Get all the offerList where discountRate is not null
        defaultOfferShouldBeFound("discountRate.specified=true");

        // Get all the offerList where discountRate is null
        defaultOfferShouldNotBeFound("discountRate.specified=false");
    }

    @Test
    @Transactional
    void getAllOffersByDiscountRateIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        offerRepository.saveAndFlush(offer);

        // Get all the offerList where discountRate is greater than or equal to DEFAULT_DISCOUNT_RATE
        defaultOfferShouldBeFound("discountRate.greaterThanOrEqual=" + DEFAULT_DISCOUNT_RATE);

        // Get all the offerList where discountRate is greater than or equal to UPDATED_DISCOUNT_RATE
        defaultOfferShouldNotBeFound("discountRate.greaterThanOrEqual=" + UPDATED_DISCOUNT_RATE);
    }

    @Test
    @Transactional
    void getAllOffersByDiscountRateIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        offerRepository.saveAndFlush(offer);

        // Get all the offerList where discountRate is less than or equal to DEFAULT_DISCOUNT_RATE
        defaultOfferShouldBeFound("discountRate.lessThanOrEqual=" + DEFAULT_DISCOUNT_RATE);

        // Get all the offerList where discountRate is less than or equal to SMALLER_DISCOUNT_RATE
        defaultOfferShouldNotBeFound("discountRate.lessThanOrEqual=" + SMALLER_DISCOUNT_RATE);
    }

    @Test
    @Transactional
    void getAllOffersByDiscountRateIsLessThanSomething() throws Exception {
        // Initialize the database
        offerRepository.saveAndFlush(offer);

        // Get all the offerList where discountRate is less than DEFAULT_DISCOUNT_RATE
        defaultOfferShouldNotBeFound("discountRate.lessThan=" + DEFAULT_DISCOUNT_RATE);

        // Get all the offerList where discountRate is less than UPDATED_DISCOUNT_RATE
        defaultOfferShouldBeFound("discountRate.lessThan=" + UPDATED_DISCOUNT_RATE);
    }

    @Test
    @Transactional
    void getAllOffersByDiscountRateIsGreaterThanSomething() throws Exception {
        // Initialize the database
        offerRepository.saveAndFlush(offer);

        // Get all the offerList where discountRate is greater than DEFAULT_DISCOUNT_RATE
        defaultOfferShouldNotBeFound("discountRate.greaterThan=" + DEFAULT_DISCOUNT_RATE);

        // Get all the offerList where discountRate is greater than SMALLER_DISCOUNT_RATE
        defaultOfferShouldBeFound("discountRate.greaterThan=" + SMALLER_DISCOUNT_RATE);
    }

    @Test
    @Transactional
    void getAllOffersByDiscountTypeIsEqualToSomething() throws Exception {
        // Initialize the database
        offerRepository.saveAndFlush(offer);

        // Get all the offerList where discountType equals to DEFAULT_DISCOUNT_TYPE
        defaultOfferShouldBeFound("discountType.equals=" + DEFAULT_DISCOUNT_TYPE);

        // Get all the offerList where discountType equals to UPDATED_DISCOUNT_TYPE
        defaultOfferShouldNotBeFound("discountType.equals=" + UPDATED_DISCOUNT_TYPE);
    }

    @Test
    @Transactional
    void getAllOffersByDiscountTypeIsNotEqualToSomething() throws Exception {
        // Initialize the database
        offerRepository.saveAndFlush(offer);

        // Get all the offerList where discountType not equals to DEFAULT_DISCOUNT_TYPE
        defaultOfferShouldNotBeFound("discountType.notEquals=" + DEFAULT_DISCOUNT_TYPE);

        // Get all the offerList where discountType not equals to UPDATED_DISCOUNT_TYPE
        defaultOfferShouldBeFound("discountType.notEquals=" + UPDATED_DISCOUNT_TYPE);
    }

    @Test
    @Transactional
    void getAllOffersByDiscountTypeIsInShouldWork() throws Exception {
        // Initialize the database
        offerRepository.saveAndFlush(offer);

        // Get all the offerList where discountType in DEFAULT_DISCOUNT_TYPE or UPDATED_DISCOUNT_TYPE
        defaultOfferShouldBeFound("discountType.in=" + DEFAULT_DISCOUNT_TYPE + "," + UPDATED_DISCOUNT_TYPE);

        // Get all the offerList where discountType equals to UPDATED_DISCOUNT_TYPE
        defaultOfferShouldNotBeFound("discountType.in=" + UPDATED_DISCOUNT_TYPE);
    }

    @Test
    @Transactional
    void getAllOffersByDiscountTypeIsNullOrNotNull() throws Exception {
        // Initialize the database
        offerRepository.saveAndFlush(offer);

        // Get all the offerList where discountType is not null
        defaultOfferShouldBeFound("discountType.specified=true");

        // Get all the offerList where discountType is null
        defaultOfferShouldNotBeFound("discountType.specified=false");
    }

    @Test
    @Transactional
    void getAllOffersByAcceptOnlineIsEqualToSomething() throws Exception {
        // Initialize the database
        offerRepository.saveAndFlush(offer);

        // Get all the offerList where acceptOnline equals to DEFAULT_ACCEPT_ONLINE
        defaultOfferShouldBeFound("acceptOnline.equals=" + DEFAULT_ACCEPT_ONLINE);

        // Get all the offerList where acceptOnline equals to UPDATED_ACCEPT_ONLINE
        defaultOfferShouldNotBeFound("acceptOnline.equals=" + UPDATED_ACCEPT_ONLINE);
    }

    @Test
    @Transactional
    void getAllOffersByAcceptOnlineIsNotEqualToSomething() throws Exception {
        // Initialize the database
        offerRepository.saveAndFlush(offer);

        // Get all the offerList where acceptOnline not equals to DEFAULT_ACCEPT_ONLINE
        defaultOfferShouldNotBeFound("acceptOnline.notEquals=" + DEFAULT_ACCEPT_ONLINE);

        // Get all the offerList where acceptOnline not equals to UPDATED_ACCEPT_ONLINE
        defaultOfferShouldBeFound("acceptOnline.notEquals=" + UPDATED_ACCEPT_ONLINE);
    }

    @Test
    @Transactional
    void getAllOffersByAcceptOnlineIsInShouldWork() throws Exception {
        // Initialize the database
        offerRepository.saveAndFlush(offer);

        // Get all the offerList where acceptOnline in DEFAULT_ACCEPT_ONLINE or UPDATED_ACCEPT_ONLINE
        defaultOfferShouldBeFound("acceptOnline.in=" + DEFAULT_ACCEPT_ONLINE + "," + UPDATED_ACCEPT_ONLINE);

        // Get all the offerList where acceptOnline equals to UPDATED_ACCEPT_ONLINE
        defaultOfferShouldNotBeFound("acceptOnline.in=" + UPDATED_ACCEPT_ONLINE);
    }

    @Test
    @Transactional
    void getAllOffersByAcceptOnlineIsNullOrNotNull() throws Exception {
        // Initialize the database
        offerRepository.saveAndFlush(offer);

        // Get all the offerList where acceptOnline is not null
        defaultOfferShouldBeFound("acceptOnline.specified=true");

        // Get all the offerList where acceptOnline is null
        defaultOfferShouldNotBeFound("acceptOnline.specified=false");
    }

    @Test
    @Transactional
    void getAllOffersByAcceptOnlineUrlIsEqualToSomething() throws Exception {
        // Initialize the database
        offerRepository.saveAndFlush(offer);

        // Get all the offerList where acceptOnlineUrl equals to DEFAULT_ACCEPT_ONLINE_URL
        defaultOfferShouldBeFound("acceptOnlineUrl.equals=" + DEFAULT_ACCEPT_ONLINE_URL);

        // Get all the offerList where acceptOnlineUrl equals to UPDATED_ACCEPT_ONLINE_URL
        defaultOfferShouldNotBeFound("acceptOnlineUrl.equals=" + UPDATED_ACCEPT_ONLINE_URL);
    }

    @Test
    @Transactional
    void getAllOffersByAcceptOnlineUrlIsNotEqualToSomething() throws Exception {
        // Initialize the database
        offerRepository.saveAndFlush(offer);

        // Get all the offerList where acceptOnlineUrl not equals to DEFAULT_ACCEPT_ONLINE_URL
        defaultOfferShouldNotBeFound("acceptOnlineUrl.notEquals=" + DEFAULT_ACCEPT_ONLINE_URL);

        // Get all the offerList where acceptOnlineUrl not equals to UPDATED_ACCEPT_ONLINE_URL
        defaultOfferShouldBeFound("acceptOnlineUrl.notEquals=" + UPDATED_ACCEPT_ONLINE_URL);
    }

    @Test
    @Transactional
    void getAllOffersByAcceptOnlineUrlIsInShouldWork() throws Exception {
        // Initialize the database
        offerRepository.saveAndFlush(offer);

        // Get all the offerList where acceptOnlineUrl in DEFAULT_ACCEPT_ONLINE_URL or UPDATED_ACCEPT_ONLINE_URL
        defaultOfferShouldBeFound("acceptOnlineUrl.in=" + DEFAULT_ACCEPT_ONLINE_URL + "," + UPDATED_ACCEPT_ONLINE_URL);

        // Get all the offerList where acceptOnlineUrl equals to UPDATED_ACCEPT_ONLINE_URL
        defaultOfferShouldNotBeFound("acceptOnlineUrl.in=" + UPDATED_ACCEPT_ONLINE_URL);
    }

    @Test
    @Transactional
    void getAllOffersByAcceptOnlineUrlIsNullOrNotNull() throws Exception {
        // Initialize the database
        offerRepository.saveAndFlush(offer);

        // Get all the offerList where acceptOnlineUrl is not null
        defaultOfferShouldBeFound("acceptOnlineUrl.specified=true");

        // Get all the offerList where acceptOnlineUrl is null
        defaultOfferShouldNotBeFound("acceptOnlineUrl.specified=false");
    }

    @Test
    @Transactional
    void getAllOffersByAcceptOnlineUrlContainsSomething() throws Exception {
        // Initialize the database
        offerRepository.saveAndFlush(offer);

        // Get all the offerList where acceptOnlineUrl contains DEFAULT_ACCEPT_ONLINE_URL
        defaultOfferShouldBeFound("acceptOnlineUrl.contains=" + DEFAULT_ACCEPT_ONLINE_URL);

        // Get all the offerList where acceptOnlineUrl contains UPDATED_ACCEPT_ONLINE_URL
        defaultOfferShouldNotBeFound("acceptOnlineUrl.contains=" + UPDATED_ACCEPT_ONLINE_URL);
    }

    @Test
    @Transactional
    void getAllOffersByAcceptOnlineUrlNotContainsSomething() throws Exception {
        // Initialize the database
        offerRepository.saveAndFlush(offer);

        // Get all the offerList where acceptOnlineUrl does not contain DEFAULT_ACCEPT_ONLINE_URL
        defaultOfferShouldNotBeFound("acceptOnlineUrl.doesNotContain=" + DEFAULT_ACCEPT_ONLINE_URL);

        // Get all the offerList where acceptOnlineUrl does not contain UPDATED_ACCEPT_ONLINE_URL
        defaultOfferShouldBeFound("acceptOnlineUrl.doesNotContain=" + UPDATED_ACCEPT_ONLINE_URL);
    }

    @Test
    @Transactional
    void getAllOffersByAcceptOnlineStatusIsEqualToSomething() throws Exception {
        // Initialize the database
        offerRepository.saveAndFlush(offer);

        // Get all the offerList where acceptOnlineStatus equals to DEFAULT_ACCEPT_ONLINE_STATUS
        defaultOfferShouldBeFound("acceptOnlineStatus.equals=" + DEFAULT_ACCEPT_ONLINE_STATUS);

        // Get all the offerList where acceptOnlineStatus equals to UPDATED_ACCEPT_ONLINE_STATUS
        defaultOfferShouldNotBeFound("acceptOnlineStatus.equals=" + UPDATED_ACCEPT_ONLINE_STATUS);
    }

    @Test
    @Transactional
    void getAllOffersByAcceptOnlineStatusIsNotEqualToSomething() throws Exception {
        // Initialize the database
        offerRepository.saveAndFlush(offer);

        // Get all the offerList where acceptOnlineStatus not equals to DEFAULT_ACCEPT_ONLINE_STATUS
        defaultOfferShouldNotBeFound("acceptOnlineStatus.notEquals=" + DEFAULT_ACCEPT_ONLINE_STATUS);

        // Get all the offerList where acceptOnlineStatus not equals to UPDATED_ACCEPT_ONLINE_STATUS
        defaultOfferShouldBeFound("acceptOnlineStatus.notEquals=" + UPDATED_ACCEPT_ONLINE_STATUS);
    }

    @Test
    @Transactional
    void getAllOffersByAcceptOnlineStatusIsInShouldWork() throws Exception {
        // Initialize the database
        offerRepository.saveAndFlush(offer);

        // Get all the offerList where acceptOnlineStatus in DEFAULT_ACCEPT_ONLINE_STATUS or UPDATED_ACCEPT_ONLINE_STATUS
        defaultOfferShouldBeFound("acceptOnlineStatus.in=" + DEFAULT_ACCEPT_ONLINE_STATUS + "," + UPDATED_ACCEPT_ONLINE_STATUS);

        // Get all the offerList where acceptOnlineStatus equals to UPDATED_ACCEPT_ONLINE_STATUS
        defaultOfferShouldNotBeFound("acceptOnlineStatus.in=" + UPDATED_ACCEPT_ONLINE_STATUS);
    }

    @Test
    @Transactional
    void getAllOffersByAcceptOnlineStatusIsNullOrNotNull() throws Exception {
        // Initialize the database
        offerRepository.saveAndFlush(offer);

        // Get all the offerList where acceptOnlineStatus is not null
        defaultOfferShouldBeFound("acceptOnlineStatus.specified=true");

        // Get all the offerList where acceptOnlineStatus is null
        defaultOfferShouldNotBeFound("acceptOnlineStatus.specified=false");
    }

    @Test
    @Transactional
    void getAllOffersByLanguageIsEqualToSomething() throws Exception {
        // Initialize the database
        offerRepository.saveAndFlush(offer);

        // Get all the offerList where language equals to DEFAULT_LANGUAGE
        defaultOfferShouldBeFound("language.equals=" + DEFAULT_LANGUAGE);

        // Get all the offerList where language equals to UPDATED_LANGUAGE
        defaultOfferShouldNotBeFound("language.equals=" + UPDATED_LANGUAGE);
    }

    @Test
    @Transactional
    void getAllOffersByLanguageIsNotEqualToSomething() throws Exception {
        // Initialize the database
        offerRepository.saveAndFlush(offer);

        // Get all the offerList where language not equals to DEFAULT_LANGUAGE
        defaultOfferShouldNotBeFound("language.notEquals=" + DEFAULT_LANGUAGE);

        // Get all the offerList where language not equals to UPDATED_LANGUAGE
        defaultOfferShouldBeFound("language.notEquals=" + UPDATED_LANGUAGE);
    }

    @Test
    @Transactional
    void getAllOffersByLanguageIsInShouldWork() throws Exception {
        // Initialize the database
        offerRepository.saveAndFlush(offer);

        // Get all the offerList where language in DEFAULT_LANGUAGE or UPDATED_LANGUAGE
        defaultOfferShouldBeFound("language.in=" + DEFAULT_LANGUAGE + "," + UPDATED_LANGUAGE);

        // Get all the offerList where language equals to UPDATED_LANGUAGE
        defaultOfferShouldNotBeFound("language.in=" + UPDATED_LANGUAGE);
    }

    @Test
    @Transactional
    void getAllOffersByLanguageIsNullOrNotNull() throws Exception {
        // Initialize the database
        offerRepository.saveAndFlush(offer);

        // Get all the offerList where language is not null
        defaultOfferShouldBeFound("language.specified=true");

        // Get all the offerList where language is null
        defaultOfferShouldNotBeFound("language.specified=false");
    }

    @Test
    @Transactional
    void getAllOffersByPageAmountIsEqualToSomething() throws Exception {
        // Initialize the database
        offerRepository.saveAndFlush(offer);

        // Get all the offerList where pageAmount equals to DEFAULT_PAGE_AMOUNT
        defaultOfferShouldBeFound("pageAmount.equals=" + DEFAULT_PAGE_AMOUNT);

        // Get all the offerList where pageAmount equals to UPDATED_PAGE_AMOUNT
        defaultOfferShouldNotBeFound("pageAmount.equals=" + UPDATED_PAGE_AMOUNT);
    }

    @Test
    @Transactional
    void getAllOffersByPageAmountIsNotEqualToSomething() throws Exception {
        // Initialize the database
        offerRepository.saveAndFlush(offer);

        // Get all the offerList where pageAmount not equals to DEFAULT_PAGE_AMOUNT
        defaultOfferShouldNotBeFound("pageAmount.notEquals=" + DEFAULT_PAGE_AMOUNT);

        // Get all the offerList where pageAmount not equals to UPDATED_PAGE_AMOUNT
        defaultOfferShouldBeFound("pageAmount.notEquals=" + UPDATED_PAGE_AMOUNT);
    }

    @Test
    @Transactional
    void getAllOffersByPageAmountIsInShouldWork() throws Exception {
        // Initialize the database
        offerRepository.saveAndFlush(offer);

        // Get all the offerList where pageAmount in DEFAULT_PAGE_AMOUNT or UPDATED_PAGE_AMOUNT
        defaultOfferShouldBeFound("pageAmount.in=" + DEFAULT_PAGE_AMOUNT + "," + UPDATED_PAGE_AMOUNT);

        // Get all the offerList where pageAmount equals to UPDATED_PAGE_AMOUNT
        defaultOfferShouldNotBeFound("pageAmount.in=" + UPDATED_PAGE_AMOUNT);
    }

    @Test
    @Transactional
    void getAllOffersByPageAmountIsNullOrNotNull() throws Exception {
        // Initialize the database
        offerRepository.saveAndFlush(offer);

        // Get all the offerList where pageAmount is not null
        defaultOfferShouldBeFound("pageAmount.specified=true");

        // Get all the offerList where pageAmount is null
        defaultOfferShouldNotBeFound("pageAmount.specified=false");
    }

    @Test
    @Transactional
    void getAllOffersByPageAmountIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        offerRepository.saveAndFlush(offer);

        // Get all the offerList where pageAmount is greater than or equal to DEFAULT_PAGE_AMOUNT
        defaultOfferShouldBeFound("pageAmount.greaterThanOrEqual=" + DEFAULT_PAGE_AMOUNT);

        // Get all the offerList where pageAmount is greater than or equal to UPDATED_PAGE_AMOUNT
        defaultOfferShouldNotBeFound("pageAmount.greaterThanOrEqual=" + UPDATED_PAGE_AMOUNT);
    }

    @Test
    @Transactional
    void getAllOffersByPageAmountIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        offerRepository.saveAndFlush(offer);

        // Get all the offerList where pageAmount is less than or equal to DEFAULT_PAGE_AMOUNT
        defaultOfferShouldBeFound("pageAmount.lessThanOrEqual=" + DEFAULT_PAGE_AMOUNT);

        // Get all the offerList where pageAmount is less than or equal to SMALLER_PAGE_AMOUNT
        defaultOfferShouldNotBeFound("pageAmount.lessThanOrEqual=" + SMALLER_PAGE_AMOUNT);
    }

    @Test
    @Transactional
    void getAllOffersByPageAmountIsLessThanSomething() throws Exception {
        // Initialize the database
        offerRepository.saveAndFlush(offer);

        // Get all the offerList where pageAmount is less than DEFAULT_PAGE_AMOUNT
        defaultOfferShouldNotBeFound("pageAmount.lessThan=" + DEFAULT_PAGE_AMOUNT);

        // Get all the offerList where pageAmount is less than UPDATED_PAGE_AMOUNT
        defaultOfferShouldBeFound("pageAmount.lessThan=" + UPDATED_PAGE_AMOUNT);
    }

    @Test
    @Transactional
    void getAllOffersByPageAmountIsGreaterThanSomething() throws Exception {
        // Initialize the database
        offerRepository.saveAndFlush(offer);

        // Get all the offerList where pageAmount is greater than DEFAULT_PAGE_AMOUNT
        defaultOfferShouldNotBeFound("pageAmount.greaterThan=" + DEFAULT_PAGE_AMOUNT);

        // Get all the offerList where pageAmount is greater than SMALLER_PAGE_AMOUNT
        defaultOfferShouldBeFound("pageAmount.greaterThan=" + SMALLER_PAGE_AMOUNT);
    }

    @Test
    @Transactional
    void getAllOffersByNotesIsEqualToSomething() throws Exception {
        // Initialize the database
        offerRepository.saveAndFlush(offer);

        // Get all the offerList where notes equals to DEFAULT_NOTES
        defaultOfferShouldBeFound("notes.equals=" + DEFAULT_NOTES);

        // Get all the offerList where notes equals to UPDATED_NOTES
        defaultOfferShouldNotBeFound("notes.equals=" + UPDATED_NOTES);
    }

    @Test
    @Transactional
    void getAllOffersByNotesIsNotEqualToSomething() throws Exception {
        // Initialize the database
        offerRepository.saveAndFlush(offer);

        // Get all the offerList where notes not equals to DEFAULT_NOTES
        defaultOfferShouldNotBeFound("notes.notEquals=" + DEFAULT_NOTES);

        // Get all the offerList where notes not equals to UPDATED_NOTES
        defaultOfferShouldBeFound("notes.notEquals=" + UPDATED_NOTES);
    }

    @Test
    @Transactional
    void getAllOffersByNotesIsInShouldWork() throws Exception {
        // Initialize the database
        offerRepository.saveAndFlush(offer);

        // Get all the offerList where notes in DEFAULT_NOTES or UPDATED_NOTES
        defaultOfferShouldBeFound("notes.in=" + DEFAULT_NOTES + "," + UPDATED_NOTES);

        // Get all the offerList where notes equals to UPDATED_NOTES
        defaultOfferShouldNotBeFound("notes.in=" + UPDATED_NOTES);
    }

    @Test
    @Transactional
    void getAllOffersByNotesIsNullOrNotNull() throws Exception {
        // Initialize the database
        offerRepository.saveAndFlush(offer);

        // Get all the offerList where notes is not null
        defaultOfferShouldBeFound("notes.specified=true");

        // Get all the offerList where notes is null
        defaultOfferShouldNotBeFound("notes.specified=false");
    }

    @Test
    @Transactional
    void getAllOffersByNotesContainsSomething() throws Exception {
        // Initialize the database
        offerRepository.saveAndFlush(offer);

        // Get all the offerList where notes contains DEFAULT_NOTES
        defaultOfferShouldBeFound("notes.contains=" + DEFAULT_NOTES);

        // Get all the offerList where notes contains UPDATED_NOTES
        defaultOfferShouldNotBeFound("notes.contains=" + UPDATED_NOTES);
    }

    @Test
    @Transactional
    void getAllOffersByNotesNotContainsSomething() throws Exception {
        // Initialize the database
        offerRepository.saveAndFlush(offer);

        // Get all the offerList where notes does not contain DEFAULT_NOTES
        defaultOfferShouldNotBeFound("notes.doesNotContain=" + DEFAULT_NOTES);

        // Get all the offerList where notes does not contain UPDATED_NOTES
        defaultOfferShouldBeFound("notes.doesNotContain=" + UPDATED_NOTES);
    }

    @Test
    @Transactional
    void getAllOffersByStatusIsEqualToSomething() throws Exception {
        // Initialize the database
        offerRepository.saveAndFlush(offer);

        // Get all the offerList where status equals to DEFAULT_STATUS
        defaultOfferShouldBeFound("status.equals=" + DEFAULT_STATUS);

        // Get all the offerList where status equals to UPDATED_STATUS
        defaultOfferShouldNotBeFound("status.equals=" + UPDATED_STATUS);
    }

    @Test
    @Transactional
    void getAllOffersByStatusIsNotEqualToSomething() throws Exception {
        // Initialize the database
        offerRepository.saveAndFlush(offer);

        // Get all the offerList where status not equals to DEFAULT_STATUS
        defaultOfferShouldNotBeFound("status.notEquals=" + DEFAULT_STATUS);

        // Get all the offerList where status not equals to UPDATED_STATUS
        defaultOfferShouldBeFound("status.notEquals=" + UPDATED_STATUS);
    }

    @Test
    @Transactional
    void getAllOffersByStatusIsInShouldWork() throws Exception {
        // Initialize the database
        offerRepository.saveAndFlush(offer);

        // Get all the offerList where status in DEFAULT_STATUS or UPDATED_STATUS
        defaultOfferShouldBeFound("status.in=" + DEFAULT_STATUS + "," + UPDATED_STATUS);

        // Get all the offerList where status equals to UPDATED_STATUS
        defaultOfferShouldNotBeFound("status.in=" + UPDATED_STATUS);
    }

    @Test
    @Transactional
    void getAllOffersByStatusIsNullOrNotNull() throws Exception {
        // Initialize the database
        offerRepository.saveAndFlush(offer);

        // Get all the offerList where status is not null
        defaultOfferShouldBeFound("status.specified=true");

        // Get all the offerList where status is null
        defaultOfferShouldNotBeFound("status.specified=false");
    }

    @Test
    @Transactional
    void getAllOffersByCreatedIsEqualToSomething() throws Exception {
        // Initialize the database
        offerRepository.saveAndFlush(offer);

        // Get all the offerList where created equals to DEFAULT_CREATED
        defaultOfferShouldBeFound("created.equals=" + DEFAULT_CREATED);

        // Get all the offerList where created equals to UPDATED_CREATED
        defaultOfferShouldNotBeFound("created.equals=" + UPDATED_CREATED);
    }

    @Test
    @Transactional
    void getAllOffersByCreatedIsNotEqualToSomething() throws Exception {
        // Initialize the database
        offerRepository.saveAndFlush(offer);

        // Get all the offerList where created not equals to DEFAULT_CREATED
        defaultOfferShouldNotBeFound("created.notEquals=" + DEFAULT_CREATED);

        // Get all the offerList where created not equals to UPDATED_CREATED
        defaultOfferShouldBeFound("created.notEquals=" + UPDATED_CREATED);
    }

    @Test
    @Transactional
    void getAllOffersByCreatedIsInShouldWork() throws Exception {
        // Initialize the database
        offerRepository.saveAndFlush(offer);

        // Get all the offerList where created in DEFAULT_CREATED or UPDATED_CREATED
        defaultOfferShouldBeFound("created.in=" + DEFAULT_CREATED + "," + UPDATED_CREATED);

        // Get all the offerList where created equals to UPDATED_CREATED
        defaultOfferShouldNotBeFound("created.in=" + UPDATED_CREATED);
    }

    @Test
    @Transactional
    void getAllOffersByCreatedIsNullOrNotNull() throws Exception {
        // Initialize the database
        offerRepository.saveAndFlush(offer);

        // Get all the offerList where created is not null
        defaultOfferShouldBeFound("created.specified=true");

        // Get all the offerList where created is null
        defaultOfferShouldNotBeFound("created.specified=false");
    }

    @Test
    @Transactional
    void getAllOffersByCreatedIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        offerRepository.saveAndFlush(offer);

        // Get all the offerList where created is greater than or equal to DEFAULT_CREATED
        defaultOfferShouldBeFound("created.greaterThanOrEqual=" + DEFAULT_CREATED);

        // Get all the offerList where created is greater than or equal to UPDATED_CREATED
        defaultOfferShouldNotBeFound("created.greaterThanOrEqual=" + UPDATED_CREATED);
    }

    @Test
    @Transactional
    void getAllOffersByCreatedIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        offerRepository.saveAndFlush(offer);

        // Get all the offerList where created is less than or equal to DEFAULT_CREATED
        defaultOfferShouldBeFound("created.lessThanOrEqual=" + DEFAULT_CREATED);

        // Get all the offerList where created is less than or equal to SMALLER_CREATED
        defaultOfferShouldNotBeFound("created.lessThanOrEqual=" + SMALLER_CREATED);
    }

    @Test
    @Transactional
    void getAllOffersByCreatedIsLessThanSomething() throws Exception {
        // Initialize the database
        offerRepository.saveAndFlush(offer);

        // Get all the offerList where created is less than DEFAULT_CREATED
        defaultOfferShouldNotBeFound("created.lessThan=" + DEFAULT_CREATED);

        // Get all the offerList where created is less than UPDATED_CREATED
        defaultOfferShouldBeFound("created.lessThan=" + UPDATED_CREATED);
    }

    @Test
    @Transactional
    void getAllOffersByCreatedIsGreaterThanSomething() throws Exception {
        // Initialize the database
        offerRepository.saveAndFlush(offer);

        // Get all the offerList where created is greater than DEFAULT_CREATED
        defaultOfferShouldNotBeFound("created.greaterThan=" + DEFAULT_CREATED);

        // Get all the offerList where created is greater than SMALLER_CREATED
        defaultOfferShouldBeFound("created.greaterThan=" + SMALLER_CREATED);
    }

    @Test
    @Transactional
    void getAllOffersByFreeTextsIsEqualToSomething() throws Exception {
        // Initialize the database
        offerRepository.saveAndFlush(offer);
        DocumentFreeText freeTexts = DocumentFreeTextResourceIT.createEntity(em);
        em.persist(freeTexts);
        em.flush();
        offer.addFreeTexts(freeTexts);
        offerRepository.saveAndFlush(offer);
        Long freeTextsId = freeTexts.getId();

        // Get all the offerList where freeTexts equals to freeTextsId
        defaultOfferShouldBeFound("freeTextsId.equals=" + freeTextsId);

        // Get all the offerList where freeTexts equals to (freeTextsId + 1)
        defaultOfferShouldNotBeFound("freeTextsId.equals=" + (freeTextsId + 1));
    }

    @Test
    @Transactional
    void getAllOffersByTextsIsEqualToSomething() throws Exception {
        // Initialize the database
        offerRepository.saveAndFlush(offer);
        DescriptiveDocumentText texts = DescriptiveDocumentTextResourceIT.createEntity(em);
        em.persist(texts);
        em.flush();
        offer.addTexts(texts);
        offerRepository.saveAndFlush(offer);
        Long textsId = texts.getId();

        // Get all the offerList where texts equals to textsId
        defaultOfferShouldBeFound("textsId.equals=" + textsId);

        // Get all the offerList where texts equals to (textsId + 1)
        defaultOfferShouldNotBeFound("textsId.equals=" + (textsId + 1));
    }

    @Test
    @Transactional
    void getAllOffersByPositionsIsEqualToSomething() throws Exception {
        // Initialize the database
        offerRepository.saveAndFlush(offer);
        DocumentPosition positions = DocumentPositionResourceIT.createEntity(em);
        em.persist(positions);
        em.flush();
        offer.addPositions(positions);
        offerRepository.saveAndFlush(offer);
        Long positionsId = positions.getId();

        // Get all the offerList where positions equals to positionsId
        defaultOfferShouldBeFound("positionsId.equals=" + positionsId);

        // Get all the offerList where positions equals to (positionsId + 1)
        defaultOfferShouldNotBeFound("positionsId.equals=" + (positionsId + 1));
    }

    @Test
    @Transactional
    void getAllOffersByContactIsEqualToSomething() throws Exception {
        // Initialize the database
        offerRepository.saveAndFlush(offer);
        Contact contact = ContactResourceIT.createEntity(em);
        em.persist(contact);
        em.flush();
        offer.setContact(contact);
        offerRepository.saveAndFlush(offer);
        Long contactId = contact.getId();

        // Get all the offerList where contact equals to contactId
        defaultOfferShouldBeFound("contactId.equals=" + contactId);

        // Get all the offerList where contact equals to (contactId + 1)
        defaultOfferShouldNotBeFound("contactId.equals=" + (contactId + 1));
    }

    @Test
    @Transactional
    void getAllOffersByContactAddressIsEqualToSomething() throws Exception {
        // Initialize the database
        offerRepository.saveAndFlush(offer);
        ContactAddress contactAddress = ContactAddressResourceIT.createEntity(em);
        em.persist(contactAddress);
        em.flush();
        offer.setContactAddress(contactAddress);
        offerRepository.saveAndFlush(offer);
        Long contactAddressId = contactAddress.getId();

        // Get all the offerList where contactAddress equals to contactAddressId
        defaultOfferShouldBeFound("contactAddressId.equals=" + contactAddressId);

        // Get all the offerList where contactAddress equals to (contactAddressId + 1)
        defaultOfferShouldNotBeFound("contactAddressId.equals=" + (contactAddressId + 1));
    }

    @Test
    @Transactional
    void getAllOffersByContactPersonIsEqualToSomething() throws Exception {
        // Initialize the database
        offerRepository.saveAndFlush(offer);
        ContactPerson contactPerson = ContactPersonResourceIT.createEntity(em);
        em.persist(contactPerson);
        em.flush();
        offer.setContactPerson(contactPerson);
        offerRepository.saveAndFlush(offer);
        Long contactPersonId = contactPerson.getId();

        // Get all the offerList where contactPerson equals to contactPersonId
        defaultOfferShouldBeFound("contactPersonId.equals=" + contactPersonId);

        // Get all the offerList where contactPerson equals to (contactPersonId + 1)
        defaultOfferShouldNotBeFound("contactPersonId.equals=" + (contactPersonId + 1));
    }

    @Test
    @Transactional
    void getAllOffersByContactPrePageAddressIsEqualToSomething() throws Exception {
        // Initialize the database
        offerRepository.saveAndFlush(offer);
        ContactAddress contactPrePageAddress = ContactAddressResourceIT.createEntity(em);
        em.persist(contactPrePageAddress);
        em.flush();
        offer.setContactPrePageAddress(contactPrePageAddress);
        offerRepository.saveAndFlush(offer);
        Long contactPrePageAddressId = contactPrePageAddress.getId();

        // Get all the offerList where contactPrePageAddress equals to contactPrePageAddressId
        defaultOfferShouldBeFound("contactPrePageAddressId.equals=" + contactPrePageAddressId);

        // Get all the offerList where contactPrePageAddress equals to (contactPrePageAddressId + 1)
        defaultOfferShouldNotBeFound("contactPrePageAddressId.equals=" + (contactPrePageAddressId + 1));
    }

    @Test
    @Transactional
    void getAllOffersByLayoutIsEqualToSomething() throws Exception {
        // Initialize the database
        offerRepository.saveAndFlush(offer);
        Layout layout = LayoutResourceIT.createEntity(em);
        em.persist(layout);
        em.flush();
        offer.setLayout(layout);
        offerRepository.saveAndFlush(offer);
        Long layoutId = layout.getId();

        // Get all the offerList where layout equals to layoutId
        defaultOfferShouldBeFound("layoutId.equals=" + layoutId);

        // Get all the offerList where layout equals to (layoutId + 1)
        defaultOfferShouldNotBeFound("layoutId.equals=" + (layoutId + 1));
    }

    @Test
    @Transactional
    void getAllOffersByLayoutIsEqualToSomething() throws Exception {
        // Initialize the database
        offerRepository.saveAndFlush(offer);
        Signature layout = SignatureResourceIT.createEntity(em);
        em.persist(layout);
        em.flush();
        offer.setLayout(layout);
        offerRepository.saveAndFlush(offer);
        Long layoutId = layout.getId();

        // Get all the offerList where layout equals to layoutId
        defaultOfferShouldBeFound("layoutId.equals=" + layoutId);

        // Get all the offerList where layout equals to (layoutId + 1)
        defaultOfferShouldNotBeFound("layoutId.equals=" + (layoutId + 1));
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultOfferShouldBeFound(String filter) throws Exception {
        restOfferMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
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

        // Check, that the count call also returns 1
        restOfferMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultOfferShouldNotBeFound(String filter) throws Exception {
        restOfferMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restOfferMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
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
