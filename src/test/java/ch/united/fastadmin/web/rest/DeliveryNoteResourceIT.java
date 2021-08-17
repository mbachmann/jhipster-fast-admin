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
import ch.united.fastadmin.domain.CustomFieldValue;
import ch.united.fastadmin.domain.DeliveryNote;
import ch.united.fastadmin.domain.DescriptiveDocumentText;
import ch.united.fastadmin.domain.DocumentFreeText;
import ch.united.fastadmin.domain.DocumentPosition;
import ch.united.fastadmin.domain.Layout;
import ch.united.fastadmin.domain.Signature;
import ch.united.fastadmin.domain.enumeration.Currency;
import ch.united.fastadmin.domain.enumeration.DeliveryNoteStatus;
import ch.united.fastadmin.domain.enumeration.DiscountType;
import ch.united.fastadmin.domain.enumeration.DocumentLanguage;
import ch.united.fastadmin.repository.DeliveryNoteRepository;
import ch.united.fastadmin.service.criteria.DeliveryNoteCriteria;
import ch.united.fastadmin.service.dto.DeliveryNoteDTO;
import ch.united.fastadmin.service.mapper.DeliveryNoteMapper;
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
 * Integration tests for the {@link DeliveryNoteResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class DeliveryNoteResourceIT {

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

    private static final DocumentLanguage DEFAULT_LANGUAGE = DocumentLanguage.FRENCH;
    private static final DocumentLanguage UPDATED_LANGUAGE = DocumentLanguage.ENGLISH;

    private static final Integer DEFAULT_PAGE_AMOUNT = 1;
    private static final Integer UPDATED_PAGE_AMOUNT = 2;
    private static final Integer SMALLER_PAGE_AMOUNT = 1 - 1;

    private static final String DEFAULT_NOTES = "AAAAAAAAAA";
    private static final String UPDATED_NOTES = "BBBBBBBBBB";

    private static final DeliveryNoteStatus DEFAULT_STATUS = DeliveryNoteStatus.DRAFT;
    private static final DeliveryNoteStatus UPDATED_STATUS = DeliveryNoteStatus.SENT;

    private static final ZonedDateTime DEFAULT_CREATED = ZonedDateTime.ofInstant(Instant.ofEpochMilli(0L), ZoneOffset.UTC);
    private static final ZonedDateTime UPDATED_CREATED = ZonedDateTime.now(ZoneId.systemDefault()).withNano(0);
    private static final ZonedDateTime SMALLER_CREATED = ZonedDateTime.ofInstant(Instant.ofEpochMilli(-1L), ZoneOffset.UTC);

    private static final String ENTITY_API_URL = "/api/delivery-notes";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private DeliveryNoteRepository deliveryNoteRepository;

    @Autowired
    private DeliveryNoteMapper deliveryNoteMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restDeliveryNoteMockMvc;

    private DeliveryNote deliveryNote;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static DeliveryNote createEntity(EntityManager em) {
        DeliveryNote deliveryNote = new DeliveryNote()
            .remoteId(DEFAULT_REMOTE_ID)
            .number(DEFAULT_NUMBER)
            .contactName(DEFAULT_CONTACT_NAME)
            .date(DEFAULT_DATE)
            .periodText(DEFAULT_PERIOD_TEXT)
            .currency(DEFAULT_CURRENCY)
            .total(DEFAULT_TOTAL)
            .vatIncluded(DEFAULT_VAT_INCLUDED)
            .discountRate(DEFAULT_DISCOUNT_RATE)
            .discountType(DEFAULT_DISCOUNT_TYPE)
            .language(DEFAULT_LANGUAGE)
            .pageAmount(DEFAULT_PAGE_AMOUNT)
            .notes(DEFAULT_NOTES)
            .status(DEFAULT_STATUS)
            .created(DEFAULT_CREATED);
        return deliveryNote;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static DeliveryNote createUpdatedEntity(EntityManager em) {
        DeliveryNote deliveryNote = new DeliveryNote()
            .remoteId(UPDATED_REMOTE_ID)
            .number(UPDATED_NUMBER)
            .contactName(UPDATED_CONTACT_NAME)
            .date(UPDATED_DATE)
            .periodText(UPDATED_PERIOD_TEXT)
            .currency(UPDATED_CURRENCY)
            .total(UPDATED_TOTAL)
            .vatIncluded(UPDATED_VAT_INCLUDED)
            .discountRate(UPDATED_DISCOUNT_RATE)
            .discountType(UPDATED_DISCOUNT_TYPE)
            .language(UPDATED_LANGUAGE)
            .pageAmount(UPDATED_PAGE_AMOUNT)
            .notes(UPDATED_NOTES)
            .status(UPDATED_STATUS)
            .created(UPDATED_CREATED);
        return deliveryNote;
    }

    @BeforeEach
    public void initTest() {
        deliveryNote = createEntity(em);
    }

    @Test
    @Transactional
    void createDeliveryNote() throws Exception {
        int databaseSizeBeforeCreate = deliveryNoteRepository.findAll().size();
        // Create the DeliveryNote
        DeliveryNoteDTO deliveryNoteDTO = deliveryNoteMapper.toDto(deliveryNote);
        restDeliveryNoteMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(deliveryNoteDTO))
            )
            .andExpect(status().isCreated());

        // Validate the DeliveryNote in the database
        List<DeliveryNote> deliveryNoteList = deliveryNoteRepository.findAll();
        assertThat(deliveryNoteList).hasSize(databaseSizeBeforeCreate + 1);
        DeliveryNote testDeliveryNote = deliveryNoteList.get(deliveryNoteList.size() - 1);
        assertThat(testDeliveryNote.getRemoteId()).isEqualTo(DEFAULT_REMOTE_ID);
        assertThat(testDeliveryNote.getNumber()).isEqualTo(DEFAULT_NUMBER);
        assertThat(testDeliveryNote.getContactName()).isEqualTo(DEFAULT_CONTACT_NAME);
        assertThat(testDeliveryNote.getDate()).isEqualTo(DEFAULT_DATE);
        assertThat(testDeliveryNote.getPeriodText()).isEqualTo(DEFAULT_PERIOD_TEXT);
        assertThat(testDeliveryNote.getCurrency()).isEqualTo(DEFAULT_CURRENCY);
        assertThat(testDeliveryNote.getTotal()).isEqualTo(DEFAULT_TOTAL);
        assertThat(testDeliveryNote.getVatIncluded()).isEqualTo(DEFAULT_VAT_INCLUDED);
        assertThat(testDeliveryNote.getDiscountRate()).isEqualTo(DEFAULT_DISCOUNT_RATE);
        assertThat(testDeliveryNote.getDiscountType()).isEqualTo(DEFAULT_DISCOUNT_TYPE);
        assertThat(testDeliveryNote.getLanguage()).isEqualTo(DEFAULT_LANGUAGE);
        assertThat(testDeliveryNote.getPageAmount()).isEqualTo(DEFAULT_PAGE_AMOUNT);
        assertThat(testDeliveryNote.getNotes()).isEqualTo(DEFAULT_NOTES);
        assertThat(testDeliveryNote.getStatus()).isEqualTo(DEFAULT_STATUS);
        assertThat(testDeliveryNote.getCreated()).isEqualTo(DEFAULT_CREATED);
    }

    @Test
    @Transactional
    void createDeliveryNoteWithExistingId() throws Exception {
        // Create the DeliveryNote with an existing ID
        deliveryNote.setId(1L);
        DeliveryNoteDTO deliveryNoteDTO = deliveryNoteMapper.toDto(deliveryNote);

        int databaseSizeBeforeCreate = deliveryNoteRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restDeliveryNoteMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(deliveryNoteDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the DeliveryNote in the database
        List<DeliveryNote> deliveryNoteList = deliveryNoteRepository.findAll();
        assertThat(deliveryNoteList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllDeliveryNotes() throws Exception {
        // Initialize the database
        deliveryNoteRepository.saveAndFlush(deliveryNote);

        // Get all the deliveryNoteList
        restDeliveryNoteMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(deliveryNote.getId().intValue())))
            .andExpect(jsonPath("$.[*].remoteId").value(hasItem(DEFAULT_REMOTE_ID)))
            .andExpect(jsonPath("$.[*].number").value(hasItem(DEFAULT_NUMBER)))
            .andExpect(jsonPath("$.[*].contactName").value(hasItem(DEFAULT_CONTACT_NAME)))
            .andExpect(jsonPath("$.[*].date").value(hasItem(DEFAULT_DATE.toString())))
            .andExpect(jsonPath("$.[*].periodText").value(hasItem(DEFAULT_PERIOD_TEXT)))
            .andExpect(jsonPath("$.[*].currency").value(hasItem(DEFAULT_CURRENCY.toString())))
            .andExpect(jsonPath("$.[*].total").value(hasItem(DEFAULT_TOTAL.doubleValue())))
            .andExpect(jsonPath("$.[*].vatIncluded").value(hasItem(DEFAULT_VAT_INCLUDED.booleanValue())))
            .andExpect(jsonPath("$.[*].discountRate").value(hasItem(DEFAULT_DISCOUNT_RATE.doubleValue())))
            .andExpect(jsonPath("$.[*].discountType").value(hasItem(DEFAULT_DISCOUNT_TYPE.toString())))
            .andExpect(jsonPath("$.[*].language").value(hasItem(DEFAULT_LANGUAGE.toString())))
            .andExpect(jsonPath("$.[*].pageAmount").value(hasItem(DEFAULT_PAGE_AMOUNT)))
            .andExpect(jsonPath("$.[*].notes").value(hasItem(DEFAULT_NOTES)))
            .andExpect(jsonPath("$.[*].status").value(hasItem(DEFAULT_STATUS.toString())))
            .andExpect(jsonPath("$.[*].created").value(hasItem(sameInstant(DEFAULT_CREATED))));
    }

    @Test
    @Transactional
    void getDeliveryNote() throws Exception {
        // Initialize the database
        deliveryNoteRepository.saveAndFlush(deliveryNote);

        // Get the deliveryNote
        restDeliveryNoteMockMvc
            .perform(get(ENTITY_API_URL_ID, deliveryNote.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(deliveryNote.getId().intValue()))
            .andExpect(jsonPath("$.remoteId").value(DEFAULT_REMOTE_ID))
            .andExpect(jsonPath("$.number").value(DEFAULT_NUMBER))
            .andExpect(jsonPath("$.contactName").value(DEFAULT_CONTACT_NAME))
            .andExpect(jsonPath("$.date").value(DEFAULT_DATE.toString()))
            .andExpect(jsonPath("$.periodText").value(DEFAULT_PERIOD_TEXT))
            .andExpect(jsonPath("$.currency").value(DEFAULT_CURRENCY.toString()))
            .andExpect(jsonPath("$.total").value(DEFAULT_TOTAL.doubleValue()))
            .andExpect(jsonPath("$.vatIncluded").value(DEFAULT_VAT_INCLUDED.booleanValue()))
            .andExpect(jsonPath("$.discountRate").value(DEFAULT_DISCOUNT_RATE.doubleValue()))
            .andExpect(jsonPath("$.discountType").value(DEFAULT_DISCOUNT_TYPE.toString()))
            .andExpect(jsonPath("$.language").value(DEFAULT_LANGUAGE.toString()))
            .andExpect(jsonPath("$.pageAmount").value(DEFAULT_PAGE_AMOUNT))
            .andExpect(jsonPath("$.notes").value(DEFAULT_NOTES))
            .andExpect(jsonPath("$.status").value(DEFAULT_STATUS.toString()))
            .andExpect(jsonPath("$.created").value(sameInstant(DEFAULT_CREATED)));
    }

    @Test
    @Transactional
    void getDeliveryNotesByIdFiltering() throws Exception {
        // Initialize the database
        deliveryNoteRepository.saveAndFlush(deliveryNote);

        Long id = deliveryNote.getId();

        defaultDeliveryNoteShouldBeFound("id.equals=" + id);
        defaultDeliveryNoteShouldNotBeFound("id.notEquals=" + id);

        defaultDeliveryNoteShouldBeFound("id.greaterThanOrEqual=" + id);
        defaultDeliveryNoteShouldNotBeFound("id.greaterThan=" + id);

        defaultDeliveryNoteShouldBeFound("id.lessThanOrEqual=" + id);
        defaultDeliveryNoteShouldNotBeFound("id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllDeliveryNotesByRemoteIdIsEqualToSomething() throws Exception {
        // Initialize the database
        deliveryNoteRepository.saveAndFlush(deliveryNote);

        // Get all the deliveryNoteList where remoteId equals to DEFAULT_REMOTE_ID
        defaultDeliveryNoteShouldBeFound("remoteId.equals=" + DEFAULT_REMOTE_ID);

        // Get all the deliveryNoteList where remoteId equals to UPDATED_REMOTE_ID
        defaultDeliveryNoteShouldNotBeFound("remoteId.equals=" + UPDATED_REMOTE_ID);
    }

    @Test
    @Transactional
    void getAllDeliveryNotesByRemoteIdIsNotEqualToSomething() throws Exception {
        // Initialize the database
        deliveryNoteRepository.saveAndFlush(deliveryNote);

        // Get all the deliveryNoteList where remoteId not equals to DEFAULT_REMOTE_ID
        defaultDeliveryNoteShouldNotBeFound("remoteId.notEquals=" + DEFAULT_REMOTE_ID);

        // Get all the deliveryNoteList where remoteId not equals to UPDATED_REMOTE_ID
        defaultDeliveryNoteShouldBeFound("remoteId.notEquals=" + UPDATED_REMOTE_ID);
    }

    @Test
    @Transactional
    void getAllDeliveryNotesByRemoteIdIsInShouldWork() throws Exception {
        // Initialize the database
        deliveryNoteRepository.saveAndFlush(deliveryNote);

        // Get all the deliveryNoteList where remoteId in DEFAULT_REMOTE_ID or UPDATED_REMOTE_ID
        defaultDeliveryNoteShouldBeFound("remoteId.in=" + DEFAULT_REMOTE_ID + "," + UPDATED_REMOTE_ID);

        // Get all the deliveryNoteList where remoteId equals to UPDATED_REMOTE_ID
        defaultDeliveryNoteShouldNotBeFound("remoteId.in=" + UPDATED_REMOTE_ID);
    }

    @Test
    @Transactional
    void getAllDeliveryNotesByRemoteIdIsNullOrNotNull() throws Exception {
        // Initialize the database
        deliveryNoteRepository.saveAndFlush(deliveryNote);

        // Get all the deliveryNoteList where remoteId is not null
        defaultDeliveryNoteShouldBeFound("remoteId.specified=true");

        // Get all the deliveryNoteList where remoteId is null
        defaultDeliveryNoteShouldNotBeFound("remoteId.specified=false");
    }

    @Test
    @Transactional
    void getAllDeliveryNotesByRemoteIdIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        deliveryNoteRepository.saveAndFlush(deliveryNote);

        // Get all the deliveryNoteList where remoteId is greater than or equal to DEFAULT_REMOTE_ID
        defaultDeliveryNoteShouldBeFound("remoteId.greaterThanOrEqual=" + DEFAULT_REMOTE_ID);

        // Get all the deliveryNoteList where remoteId is greater than or equal to UPDATED_REMOTE_ID
        defaultDeliveryNoteShouldNotBeFound("remoteId.greaterThanOrEqual=" + UPDATED_REMOTE_ID);
    }

    @Test
    @Transactional
    void getAllDeliveryNotesByRemoteIdIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        deliveryNoteRepository.saveAndFlush(deliveryNote);

        // Get all the deliveryNoteList where remoteId is less than or equal to DEFAULT_REMOTE_ID
        defaultDeliveryNoteShouldBeFound("remoteId.lessThanOrEqual=" + DEFAULT_REMOTE_ID);

        // Get all the deliveryNoteList where remoteId is less than or equal to SMALLER_REMOTE_ID
        defaultDeliveryNoteShouldNotBeFound("remoteId.lessThanOrEqual=" + SMALLER_REMOTE_ID);
    }

    @Test
    @Transactional
    void getAllDeliveryNotesByRemoteIdIsLessThanSomething() throws Exception {
        // Initialize the database
        deliveryNoteRepository.saveAndFlush(deliveryNote);

        // Get all the deliveryNoteList where remoteId is less than DEFAULT_REMOTE_ID
        defaultDeliveryNoteShouldNotBeFound("remoteId.lessThan=" + DEFAULT_REMOTE_ID);

        // Get all the deliveryNoteList where remoteId is less than UPDATED_REMOTE_ID
        defaultDeliveryNoteShouldBeFound("remoteId.lessThan=" + UPDATED_REMOTE_ID);
    }

    @Test
    @Transactional
    void getAllDeliveryNotesByRemoteIdIsGreaterThanSomething() throws Exception {
        // Initialize the database
        deliveryNoteRepository.saveAndFlush(deliveryNote);

        // Get all the deliveryNoteList where remoteId is greater than DEFAULT_REMOTE_ID
        defaultDeliveryNoteShouldNotBeFound("remoteId.greaterThan=" + DEFAULT_REMOTE_ID);

        // Get all the deliveryNoteList where remoteId is greater than SMALLER_REMOTE_ID
        defaultDeliveryNoteShouldBeFound("remoteId.greaterThan=" + SMALLER_REMOTE_ID);
    }

    @Test
    @Transactional
    void getAllDeliveryNotesByNumberIsEqualToSomething() throws Exception {
        // Initialize the database
        deliveryNoteRepository.saveAndFlush(deliveryNote);

        // Get all the deliveryNoteList where number equals to DEFAULT_NUMBER
        defaultDeliveryNoteShouldBeFound("number.equals=" + DEFAULT_NUMBER);

        // Get all the deliveryNoteList where number equals to UPDATED_NUMBER
        defaultDeliveryNoteShouldNotBeFound("number.equals=" + UPDATED_NUMBER);
    }

    @Test
    @Transactional
    void getAllDeliveryNotesByNumberIsNotEqualToSomething() throws Exception {
        // Initialize the database
        deliveryNoteRepository.saveAndFlush(deliveryNote);

        // Get all the deliveryNoteList where number not equals to DEFAULT_NUMBER
        defaultDeliveryNoteShouldNotBeFound("number.notEquals=" + DEFAULT_NUMBER);

        // Get all the deliveryNoteList where number not equals to UPDATED_NUMBER
        defaultDeliveryNoteShouldBeFound("number.notEquals=" + UPDATED_NUMBER);
    }

    @Test
    @Transactional
    void getAllDeliveryNotesByNumberIsInShouldWork() throws Exception {
        // Initialize the database
        deliveryNoteRepository.saveAndFlush(deliveryNote);

        // Get all the deliveryNoteList where number in DEFAULT_NUMBER or UPDATED_NUMBER
        defaultDeliveryNoteShouldBeFound("number.in=" + DEFAULT_NUMBER + "," + UPDATED_NUMBER);

        // Get all the deliveryNoteList where number equals to UPDATED_NUMBER
        defaultDeliveryNoteShouldNotBeFound("number.in=" + UPDATED_NUMBER);
    }

    @Test
    @Transactional
    void getAllDeliveryNotesByNumberIsNullOrNotNull() throws Exception {
        // Initialize the database
        deliveryNoteRepository.saveAndFlush(deliveryNote);

        // Get all the deliveryNoteList where number is not null
        defaultDeliveryNoteShouldBeFound("number.specified=true");

        // Get all the deliveryNoteList where number is null
        defaultDeliveryNoteShouldNotBeFound("number.specified=false");
    }

    @Test
    @Transactional
    void getAllDeliveryNotesByNumberContainsSomething() throws Exception {
        // Initialize the database
        deliveryNoteRepository.saveAndFlush(deliveryNote);

        // Get all the deliveryNoteList where number contains DEFAULT_NUMBER
        defaultDeliveryNoteShouldBeFound("number.contains=" + DEFAULT_NUMBER);

        // Get all the deliveryNoteList where number contains UPDATED_NUMBER
        defaultDeliveryNoteShouldNotBeFound("number.contains=" + UPDATED_NUMBER);
    }

    @Test
    @Transactional
    void getAllDeliveryNotesByNumberNotContainsSomething() throws Exception {
        // Initialize the database
        deliveryNoteRepository.saveAndFlush(deliveryNote);

        // Get all the deliveryNoteList where number does not contain DEFAULT_NUMBER
        defaultDeliveryNoteShouldNotBeFound("number.doesNotContain=" + DEFAULT_NUMBER);

        // Get all the deliveryNoteList where number does not contain UPDATED_NUMBER
        defaultDeliveryNoteShouldBeFound("number.doesNotContain=" + UPDATED_NUMBER);
    }

    @Test
    @Transactional
    void getAllDeliveryNotesByContactNameIsEqualToSomething() throws Exception {
        // Initialize the database
        deliveryNoteRepository.saveAndFlush(deliveryNote);

        // Get all the deliveryNoteList where contactName equals to DEFAULT_CONTACT_NAME
        defaultDeliveryNoteShouldBeFound("contactName.equals=" + DEFAULT_CONTACT_NAME);

        // Get all the deliveryNoteList where contactName equals to UPDATED_CONTACT_NAME
        defaultDeliveryNoteShouldNotBeFound("contactName.equals=" + UPDATED_CONTACT_NAME);
    }

    @Test
    @Transactional
    void getAllDeliveryNotesByContactNameIsNotEqualToSomething() throws Exception {
        // Initialize the database
        deliveryNoteRepository.saveAndFlush(deliveryNote);

        // Get all the deliveryNoteList where contactName not equals to DEFAULT_CONTACT_NAME
        defaultDeliveryNoteShouldNotBeFound("contactName.notEquals=" + DEFAULT_CONTACT_NAME);

        // Get all the deliveryNoteList where contactName not equals to UPDATED_CONTACT_NAME
        defaultDeliveryNoteShouldBeFound("contactName.notEquals=" + UPDATED_CONTACT_NAME);
    }

    @Test
    @Transactional
    void getAllDeliveryNotesByContactNameIsInShouldWork() throws Exception {
        // Initialize the database
        deliveryNoteRepository.saveAndFlush(deliveryNote);

        // Get all the deliveryNoteList where contactName in DEFAULT_CONTACT_NAME or UPDATED_CONTACT_NAME
        defaultDeliveryNoteShouldBeFound("contactName.in=" + DEFAULT_CONTACT_NAME + "," + UPDATED_CONTACT_NAME);

        // Get all the deliveryNoteList where contactName equals to UPDATED_CONTACT_NAME
        defaultDeliveryNoteShouldNotBeFound("contactName.in=" + UPDATED_CONTACT_NAME);
    }

    @Test
    @Transactional
    void getAllDeliveryNotesByContactNameIsNullOrNotNull() throws Exception {
        // Initialize the database
        deliveryNoteRepository.saveAndFlush(deliveryNote);

        // Get all the deliveryNoteList where contactName is not null
        defaultDeliveryNoteShouldBeFound("contactName.specified=true");

        // Get all the deliveryNoteList where contactName is null
        defaultDeliveryNoteShouldNotBeFound("contactName.specified=false");
    }

    @Test
    @Transactional
    void getAllDeliveryNotesByContactNameContainsSomething() throws Exception {
        // Initialize the database
        deliveryNoteRepository.saveAndFlush(deliveryNote);

        // Get all the deliveryNoteList where contactName contains DEFAULT_CONTACT_NAME
        defaultDeliveryNoteShouldBeFound("contactName.contains=" + DEFAULT_CONTACT_NAME);

        // Get all the deliveryNoteList where contactName contains UPDATED_CONTACT_NAME
        defaultDeliveryNoteShouldNotBeFound("contactName.contains=" + UPDATED_CONTACT_NAME);
    }

    @Test
    @Transactional
    void getAllDeliveryNotesByContactNameNotContainsSomething() throws Exception {
        // Initialize the database
        deliveryNoteRepository.saveAndFlush(deliveryNote);

        // Get all the deliveryNoteList where contactName does not contain DEFAULT_CONTACT_NAME
        defaultDeliveryNoteShouldNotBeFound("contactName.doesNotContain=" + DEFAULT_CONTACT_NAME);

        // Get all the deliveryNoteList where contactName does not contain UPDATED_CONTACT_NAME
        defaultDeliveryNoteShouldBeFound("contactName.doesNotContain=" + UPDATED_CONTACT_NAME);
    }

    @Test
    @Transactional
    void getAllDeliveryNotesByDateIsEqualToSomething() throws Exception {
        // Initialize the database
        deliveryNoteRepository.saveAndFlush(deliveryNote);

        // Get all the deliveryNoteList where date equals to DEFAULT_DATE
        defaultDeliveryNoteShouldBeFound("date.equals=" + DEFAULT_DATE);

        // Get all the deliveryNoteList where date equals to UPDATED_DATE
        defaultDeliveryNoteShouldNotBeFound("date.equals=" + UPDATED_DATE);
    }

    @Test
    @Transactional
    void getAllDeliveryNotesByDateIsNotEqualToSomething() throws Exception {
        // Initialize the database
        deliveryNoteRepository.saveAndFlush(deliveryNote);

        // Get all the deliveryNoteList where date not equals to DEFAULT_DATE
        defaultDeliveryNoteShouldNotBeFound("date.notEquals=" + DEFAULT_DATE);

        // Get all the deliveryNoteList where date not equals to UPDATED_DATE
        defaultDeliveryNoteShouldBeFound("date.notEquals=" + UPDATED_DATE);
    }

    @Test
    @Transactional
    void getAllDeliveryNotesByDateIsInShouldWork() throws Exception {
        // Initialize the database
        deliveryNoteRepository.saveAndFlush(deliveryNote);

        // Get all the deliveryNoteList where date in DEFAULT_DATE or UPDATED_DATE
        defaultDeliveryNoteShouldBeFound("date.in=" + DEFAULT_DATE + "," + UPDATED_DATE);

        // Get all the deliveryNoteList where date equals to UPDATED_DATE
        defaultDeliveryNoteShouldNotBeFound("date.in=" + UPDATED_DATE);
    }

    @Test
    @Transactional
    void getAllDeliveryNotesByDateIsNullOrNotNull() throws Exception {
        // Initialize the database
        deliveryNoteRepository.saveAndFlush(deliveryNote);

        // Get all the deliveryNoteList where date is not null
        defaultDeliveryNoteShouldBeFound("date.specified=true");

        // Get all the deliveryNoteList where date is null
        defaultDeliveryNoteShouldNotBeFound("date.specified=false");
    }

    @Test
    @Transactional
    void getAllDeliveryNotesByDateIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        deliveryNoteRepository.saveAndFlush(deliveryNote);

        // Get all the deliveryNoteList where date is greater than or equal to DEFAULT_DATE
        defaultDeliveryNoteShouldBeFound("date.greaterThanOrEqual=" + DEFAULT_DATE);

        // Get all the deliveryNoteList where date is greater than or equal to UPDATED_DATE
        defaultDeliveryNoteShouldNotBeFound("date.greaterThanOrEqual=" + UPDATED_DATE);
    }

    @Test
    @Transactional
    void getAllDeliveryNotesByDateIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        deliveryNoteRepository.saveAndFlush(deliveryNote);

        // Get all the deliveryNoteList where date is less than or equal to DEFAULT_DATE
        defaultDeliveryNoteShouldBeFound("date.lessThanOrEqual=" + DEFAULT_DATE);

        // Get all the deliveryNoteList where date is less than or equal to SMALLER_DATE
        defaultDeliveryNoteShouldNotBeFound("date.lessThanOrEqual=" + SMALLER_DATE);
    }

    @Test
    @Transactional
    void getAllDeliveryNotesByDateIsLessThanSomething() throws Exception {
        // Initialize the database
        deliveryNoteRepository.saveAndFlush(deliveryNote);

        // Get all the deliveryNoteList where date is less than DEFAULT_DATE
        defaultDeliveryNoteShouldNotBeFound("date.lessThan=" + DEFAULT_DATE);

        // Get all the deliveryNoteList where date is less than UPDATED_DATE
        defaultDeliveryNoteShouldBeFound("date.lessThan=" + UPDATED_DATE);
    }

    @Test
    @Transactional
    void getAllDeliveryNotesByDateIsGreaterThanSomething() throws Exception {
        // Initialize the database
        deliveryNoteRepository.saveAndFlush(deliveryNote);

        // Get all the deliveryNoteList where date is greater than DEFAULT_DATE
        defaultDeliveryNoteShouldNotBeFound("date.greaterThan=" + DEFAULT_DATE);

        // Get all the deliveryNoteList where date is greater than SMALLER_DATE
        defaultDeliveryNoteShouldBeFound("date.greaterThan=" + SMALLER_DATE);
    }

    @Test
    @Transactional
    void getAllDeliveryNotesByPeriodTextIsEqualToSomething() throws Exception {
        // Initialize the database
        deliveryNoteRepository.saveAndFlush(deliveryNote);

        // Get all the deliveryNoteList where periodText equals to DEFAULT_PERIOD_TEXT
        defaultDeliveryNoteShouldBeFound("periodText.equals=" + DEFAULT_PERIOD_TEXT);

        // Get all the deliveryNoteList where periodText equals to UPDATED_PERIOD_TEXT
        defaultDeliveryNoteShouldNotBeFound("periodText.equals=" + UPDATED_PERIOD_TEXT);
    }

    @Test
    @Transactional
    void getAllDeliveryNotesByPeriodTextIsNotEqualToSomething() throws Exception {
        // Initialize the database
        deliveryNoteRepository.saveAndFlush(deliveryNote);

        // Get all the deliveryNoteList where periodText not equals to DEFAULT_PERIOD_TEXT
        defaultDeliveryNoteShouldNotBeFound("periodText.notEquals=" + DEFAULT_PERIOD_TEXT);

        // Get all the deliveryNoteList where periodText not equals to UPDATED_PERIOD_TEXT
        defaultDeliveryNoteShouldBeFound("periodText.notEquals=" + UPDATED_PERIOD_TEXT);
    }

    @Test
    @Transactional
    void getAllDeliveryNotesByPeriodTextIsInShouldWork() throws Exception {
        // Initialize the database
        deliveryNoteRepository.saveAndFlush(deliveryNote);

        // Get all the deliveryNoteList where periodText in DEFAULT_PERIOD_TEXT or UPDATED_PERIOD_TEXT
        defaultDeliveryNoteShouldBeFound("periodText.in=" + DEFAULT_PERIOD_TEXT + "," + UPDATED_PERIOD_TEXT);

        // Get all the deliveryNoteList where periodText equals to UPDATED_PERIOD_TEXT
        defaultDeliveryNoteShouldNotBeFound("periodText.in=" + UPDATED_PERIOD_TEXT);
    }

    @Test
    @Transactional
    void getAllDeliveryNotesByPeriodTextIsNullOrNotNull() throws Exception {
        // Initialize the database
        deliveryNoteRepository.saveAndFlush(deliveryNote);

        // Get all the deliveryNoteList where periodText is not null
        defaultDeliveryNoteShouldBeFound("periodText.specified=true");

        // Get all the deliveryNoteList where periodText is null
        defaultDeliveryNoteShouldNotBeFound("periodText.specified=false");
    }

    @Test
    @Transactional
    void getAllDeliveryNotesByPeriodTextContainsSomething() throws Exception {
        // Initialize the database
        deliveryNoteRepository.saveAndFlush(deliveryNote);

        // Get all the deliveryNoteList where periodText contains DEFAULT_PERIOD_TEXT
        defaultDeliveryNoteShouldBeFound("periodText.contains=" + DEFAULT_PERIOD_TEXT);

        // Get all the deliveryNoteList where periodText contains UPDATED_PERIOD_TEXT
        defaultDeliveryNoteShouldNotBeFound("periodText.contains=" + UPDATED_PERIOD_TEXT);
    }

    @Test
    @Transactional
    void getAllDeliveryNotesByPeriodTextNotContainsSomething() throws Exception {
        // Initialize the database
        deliveryNoteRepository.saveAndFlush(deliveryNote);

        // Get all the deliveryNoteList where periodText does not contain DEFAULT_PERIOD_TEXT
        defaultDeliveryNoteShouldNotBeFound("periodText.doesNotContain=" + DEFAULT_PERIOD_TEXT);

        // Get all the deliveryNoteList where periodText does not contain UPDATED_PERIOD_TEXT
        defaultDeliveryNoteShouldBeFound("periodText.doesNotContain=" + UPDATED_PERIOD_TEXT);
    }

    @Test
    @Transactional
    void getAllDeliveryNotesByCurrencyIsEqualToSomething() throws Exception {
        // Initialize the database
        deliveryNoteRepository.saveAndFlush(deliveryNote);

        // Get all the deliveryNoteList where currency equals to DEFAULT_CURRENCY
        defaultDeliveryNoteShouldBeFound("currency.equals=" + DEFAULT_CURRENCY);

        // Get all the deliveryNoteList where currency equals to UPDATED_CURRENCY
        defaultDeliveryNoteShouldNotBeFound("currency.equals=" + UPDATED_CURRENCY);
    }

    @Test
    @Transactional
    void getAllDeliveryNotesByCurrencyIsNotEqualToSomething() throws Exception {
        // Initialize the database
        deliveryNoteRepository.saveAndFlush(deliveryNote);

        // Get all the deliveryNoteList where currency not equals to DEFAULT_CURRENCY
        defaultDeliveryNoteShouldNotBeFound("currency.notEquals=" + DEFAULT_CURRENCY);

        // Get all the deliveryNoteList where currency not equals to UPDATED_CURRENCY
        defaultDeliveryNoteShouldBeFound("currency.notEquals=" + UPDATED_CURRENCY);
    }

    @Test
    @Transactional
    void getAllDeliveryNotesByCurrencyIsInShouldWork() throws Exception {
        // Initialize the database
        deliveryNoteRepository.saveAndFlush(deliveryNote);

        // Get all the deliveryNoteList where currency in DEFAULT_CURRENCY or UPDATED_CURRENCY
        defaultDeliveryNoteShouldBeFound("currency.in=" + DEFAULT_CURRENCY + "," + UPDATED_CURRENCY);

        // Get all the deliveryNoteList where currency equals to UPDATED_CURRENCY
        defaultDeliveryNoteShouldNotBeFound("currency.in=" + UPDATED_CURRENCY);
    }

    @Test
    @Transactional
    void getAllDeliveryNotesByCurrencyIsNullOrNotNull() throws Exception {
        // Initialize the database
        deliveryNoteRepository.saveAndFlush(deliveryNote);

        // Get all the deliveryNoteList where currency is not null
        defaultDeliveryNoteShouldBeFound("currency.specified=true");

        // Get all the deliveryNoteList where currency is null
        defaultDeliveryNoteShouldNotBeFound("currency.specified=false");
    }

    @Test
    @Transactional
    void getAllDeliveryNotesByTotalIsEqualToSomething() throws Exception {
        // Initialize the database
        deliveryNoteRepository.saveAndFlush(deliveryNote);

        // Get all the deliveryNoteList where total equals to DEFAULT_TOTAL
        defaultDeliveryNoteShouldBeFound("total.equals=" + DEFAULT_TOTAL);

        // Get all the deliveryNoteList where total equals to UPDATED_TOTAL
        defaultDeliveryNoteShouldNotBeFound("total.equals=" + UPDATED_TOTAL);
    }

    @Test
    @Transactional
    void getAllDeliveryNotesByTotalIsNotEqualToSomething() throws Exception {
        // Initialize the database
        deliveryNoteRepository.saveAndFlush(deliveryNote);

        // Get all the deliveryNoteList where total not equals to DEFAULT_TOTAL
        defaultDeliveryNoteShouldNotBeFound("total.notEquals=" + DEFAULT_TOTAL);

        // Get all the deliveryNoteList where total not equals to UPDATED_TOTAL
        defaultDeliveryNoteShouldBeFound("total.notEquals=" + UPDATED_TOTAL);
    }

    @Test
    @Transactional
    void getAllDeliveryNotesByTotalIsInShouldWork() throws Exception {
        // Initialize the database
        deliveryNoteRepository.saveAndFlush(deliveryNote);

        // Get all the deliveryNoteList where total in DEFAULT_TOTAL or UPDATED_TOTAL
        defaultDeliveryNoteShouldBeFound("total.in=" + DEFAULT_TOTAL + "," + UPDATED_TOTAL);

        // Get all the deliveryNoteList where total equals to UPDATED_TOTAL
        defaultDeliveryNoteShouldNotBeFound("total.in=" + UPDATED_TOTAL);
    }

    @Test
    @Transactional
    void getAllDeliveryNotesByTotalIsNullOrNotNull() throws Exception {
        // Initialize the database
        deliveryNoteRepository.saveAndFlush(deliveryNote);

        // Get all the deliveryNoteList where total is not null
        defaultDeliveryNoteShouldBeFound("total.specified=true");

        // Get all the deliveryNoteList where total is null
        defaultDeliveryNoteShouldNotBeFound("total.specified=false");
    }

    @Test
    @Transactional
    void getAllDeliveryNotesByTotalIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        deliveryNoteRepository.saveAndFlush(deliveryNote);

        // Get all the deliveryNoteList where total is greater than or equal to DEFAULT_TOTAL
        defaultDeliveryNoteShouldBeFound("total.greaterThanOrEqual=" + DEFAULT_TOTAL);

        // Get all the deliveryNoteList where total is greater than or equal to UPDATED_TOTAL
        defaultDeliveryNoteShouldNotBeFound("total.greaterThanOrEqual=" + UPDATED_TOTAL);
    }

    @Test
    @Transactional
    void getAllDeliveryNotesByTotalIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        deliveryNoteRepository.saveAndFlush(deliveryNote);

        // Get all the deliveryNoteList where total is less than or equal to DEFAULT_TOTAL
        defaultDeliveryNoteShouldBeFound("total.lessThanOrEqual=" + DEFAULT_TOTAL);

        // Get all the deliveryNoteList where total is less than or equal to SMALLER_TOTAL
        defaultDeliveryNoteShouldNotBeFound("total.lessThanOrEqual=" + SMALLER_TOTAL);
    }

    @Test
    @Transactional
    void getAllDeliveryNotesByTotalIsLessThanSomething() throws Exception {
        // Initialize the database
        deliveryNoteRepository.saveAndFlush(deliveryNote);

        // Get all the deliveryNoteList where total is less than DEFAULT_TOTAL
        defaultDeliveryNoteShouldNotBeFound("total.lessThan=" + DEFAULT_TOTAL);

        // Get all the deliveryNoteList where total is less than UPDATED_TOTAL
        defaultDeliveryNoteShouldBeFound("total.lessThan=" + UPDATED_TOTAL);
    }

    @Test
    @Transactional
    void getAllDeliveryNotesByTotalIsGreaterThanSomething() throws Exception {
        // Initialize the database
        deliveryNoteRepository.saveAndFlush(deliveryNote);

        // Get all the deliveryNoteList where total is greater than DEFAULT_TOTAL
        defaultDeliveryNoteShouldNotBeFound("total.greaterThan=" + DEFAULT_TOTAL);

        // Get all the deliveryNoteList where total is greater than SMALLER_TOTAL
        defaultDeliveryNoteShouldBeFound("total.greaterThan=" + SMALLER_TOTAL);
    }

    @Test
    @Transactional
    void getAllDeliveryNotesByVatIncludedIsEqualToSomething() throws Exception {
        // Initialize the database
        deliveryNoteRepository.saveAndFlush(deliveryNote);

        // Get all the deliveryNoteList where vatIncluded equals to DEFAULT_VAT_INCLUDED
        defaultDeliveryNoteShouldBeFound("vatIncluded.equals=" + DEFAULT_VAT_INCLUDED);

        // Get all the deliveryNoteList where vatIncluded equals to UPDATED_VAT_INCLUDED
        defaultDeliveryNoteShouldNotBeFound("vatIncluded.equals=" + UPDATED_VAT_INCLUDED);
    }

    @Test
    @Transactional
    void getAllDeliveryNotesByVatIncludedIsNotEqualToSomething() throws Exception {
        // Initialize the database
        deliveryNoteRepository.saveAndFlush(deliveryNote);

        // Get all the deliveryNoteList where vatIncluded not equals to DEFAULT_VAT_INCLUDED
        defaultDeliveryNoteShouldNotBeFound("vatIncluded.notEquals=" + DEFAULT_VAT_INCLUDED);

        // Get all the deliveryNoteList where vatIncluded not equals to UPDATED_VAT_INCLUDED
        defaultDeliveryNoteShouldBeFound("vatIncluded.notEquals=" + UPDATED_VAT_INCLUDED);
    }

    @Test
    @Transactional
    void getAllDeliveryNotesByVatIncludedIsInShouldWork() throws Exception {
        // Initialize the database
        deliveryNoteRepository.saveAndFlush(deliveryNote);

        // Get all the deliveryNoteList where vatIncluded in DEFAULT_VAT_INCLUDED or UPDATED_VAT_INCLUDED
        defaultDeliveryNoteShouldBeFound("vatIncluded.in=" + DEFAULT_VAT_INCLUDED + "," + UPDATED_VAT_INCLUDED);

        // Get all the deliveryNoteList where vatIncluded equals to UPDATED_VAT_INCLUDED
        defaultDeliveryNoteShouldNotBeFound("vatIncluded.in=" + UPDATED_VAT_INCLUDED);
    }

    @Test
    @Transactional
    void getAllDeliveryNotesByVatIncludedIsNullOrNotNull() throws Exception {
        // Initialize the database
        deliveryNoteRepository.saveAndFlush(deliveryNote);

        // Get all the deliveryNoteList where vatIncluded is not null
        defaultDeliveryNoteShouldBeFound("vatIncluded.specified=true");

        // Get all the deliveryNoteList where vatIncluded is null
        defaultDeliveryNoteShouldNotBeFound("vatIncluded.specified=false");
    }

    @Test
    @Transactional
    void getAllDeliveryNotesByDiscountRateIsEqualToSomething() throws Exception {
        // Initialize the database
        deliveryNoteRepository.saveAndFlush(deliveryNote);

        // Get all the deliveryNoteList where discountRate equals to DEFAULT_DISCOUNT_RATE
        defaultDeliveryNoteShouldBeFound("discountRate.equals=" + DEFAULT_DISCOUNT_RATE);

        // Get all the deliveryNoteList where discountRate equals to UPDATED_DISCOUNT_RATE
        defaultDeliveryNoteShouldNotBeFound("discountRate.equals=" + UPDATED_DISCOUNT_RATE);
    }

    @Test
    @Transactional
    void getAllDeliveryNotesByDiscountRateIsNotEqualToSomething() throws Exception {
        // Initialize the database
        deliveryNoteRepository.saveAndFlush(deliveryNote);

        // Get all the deliveryNoteList where discountRate not equals to DEFAULT_DISCOUNT_RATE
        defaultDeliveryNoteShouldNotBeFound("discountRate.notEquals=" + DEFAULT_DISCOUNT_RATE);

        // Get all the deliveryNoteList where discountRate not equals to UPDATED_DISCOUNT_RATE
        defaultDeliveryNoteShouldBeFound("discountRate.notEquals=" + UPDATED_DISCOUNT_RATE);
    }

    @Test
    @Transactional
    void getAllDeliveryNotesByDiscountRateIsInShouldWork() throws Exception {
        // Initialize the database
        deliveryNoteRepository.saveAndFlush(deliveryNote);

        // Get all the deliveryNoteList where discountRate in DEFAULT_DISCOUNT_RATE or UPDATED_DISCOUNT_RATE
        defaultDeliveryNoteShouldBeFound("discountRate.in=" + DEFAULT_DISCOUNT_RATE + "," + UPDATED_DISCOUNT_RATE);

        // Get all the deliveryNoteList where discountRate equals to UPDATED_DISCOUNT_RATE
        defaultDeliveryNoteShouldNotBeFound("discountRate.in=" + UPDATED_DISCOUNT_RATE);
    }

    @Test
    @Transactional
    void getAllDeliveryNotesByDiscountRateIsNullOrNotNull() throws Exception {
        // Initialize the database
        deliveryNoteRepository.saveAndFlush(deliveryNote);

        // Get all the deliveryNoteList where discountRate is not null
        defaultDeliveryNoteShouldBeFound("discountRate.specified=true");

        // Get all the deliveryNoteList where discountRate is null
        defaultDeliveryNoteShouldNotBeFound("discountRate.specified=false");
    }

    @Test
    @Transactional
    void getAllDeliveryNotesByDiscountRateIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        deliveryNoteRepository.saveAndFlush(deliveryNote);

        // Get all the deliveryNoteList where discountRate is greater than or equal to DEFAULT_DISCOUNT_RATE
        defaultDeliveryNoteShouldBeFound("discountRate.greaterThanOrEqual=" + DEFAULT_DISCOUNT_RATE);

        // Get all the deliveryNoteList where discountRate is greater than or equal to UPDATED_DISCOUNT_RATE
        defaultDeliveryNoteShouldNotBeFound("discountRate.greaterThanOrEqual=" + UPDATED_DISCOUNT_RATE);
    }

    @Test
    @Transactional
    void getAllDeliveryNotesByDiscountRateIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        deliveryNoteRepository.saveAndFlush(deliveryNote);

        // Get all the deliveryNoteList where discountRate is less than or equal to DEFAULT_DISCOUNT_RATE
        defaultDeliveryNoteShouldBeFound("discountRate.lessThanOrEqual=" + DEFAULT_DISCOUNT_RATE);

        // Get all the deliveryNoteList where discountRate is less than or equal to SMALLER_DISCOUNT_RATE
        defaultDeliveryNoteShouldNotBeFound("discountRate.lessThanOrEqual=" + SMALLER_DISCOUNT_RATE);
    }

    @Test
    @Transactional
    void getAllDeliveryNotesByDiscountRateIsLessThanSomething() throws Exception {
        // Initialize the database
        deliveryNoteRepository.saveAndFlush(deliveryNote);

        // Get all the deliveryNoteList where discountRate is less than DEFAULT_DISCOUNT_RATE
        defaultDeliveryNoteShouldNotBeFound("discountRate.lessThan=" + DEFAULT_DISCOUNT_RATE);

        // Get all the deliveryNoteList where discountRate is less than UPDATED_DISCOUNT_RATE
        defaultDeliveryNoteShouldBeFound("discountRate.lessThan=" + UPDATED_DISCOUNT_RATE);
    }

    @Test
    @Transactional
    void getAllDeliveryNotesByDiscountRateIsGreaterThanSomething() throws Exception {
        // Initialize the database
        deliveryNoteRepository.saveAndFlush(deliveryNote);

        // Get all the deliveryNoteList where discountRate is greater than DEFAULT_DISCOUNT_RATE
        defaultDeliveryNoteShouldNotBeFound("discountRate.greaterThan=" + DEFAULT_DISCOUNT_RATE);

        // Get all the deliveryNoteList where discountRate is greater than SMALLER_DISCOUNT_RATE
        defaultDeliveryNoteShouldBeFound("discountRate.greaterThan=" + SMALLER_DISCOUNT_RATE);
    }

    @Test
    @Transactional
    void getAllDeliveryNotesByDiscountTypeIsEqualToSomething() throws Exception {
        // Initialize the database
        deliveryNoteRepository.saveAndFlush(deliveryNote);

        // Get all the deliveryNoteList where discountType equals to DEFAULT_DISCOUNT_TYPE
        defaultDeliveryNoteShouldBeFound("discountType.equals=" + DEFAULT_DISCOUNT_TYPE);

        // Get all the deliveryNoteList where discountType equals to UPDATED_DISCOUNT_TYPE
        defaultDeliveryNoteShouldNotBeFound("discountType.equals=" + UPDATED_DISCOUNT_TYPE);
    }

    @Test
    @Transactional
    void getAllDeliveryNotesByDiscountTypeIsNotEqualToSomething() throws Exception {
        // Initialize the database
        deliveryNoteRepository.saveAndFlush(deliveryNote);

        // Get all the deliveryNoteList where discountType not equals to DEFAULT_DISCOUNT_TYPE
        defaultDeliveryNoteShouldNotBeFound("discountType.notEquals=" + DEFAULT_DISCOUNT_TYPE);

        // Get all the deliveryNoteList where discountType not equals to UPDATED_DISCOUNT_TYPE
        defaultDeliveryNoteShouldBeFound("discountType.notEquals=" + UPDATED_DISCOUNT_TYPE);
    }

    @Test
    @Transactional
    void getAllDeliveryNotesByDiscountTypeIsInShouldWork() throws Exception {
        // Initialize the database
        deliveryNoteRepository.saveAndFlush(deliveryNote);

        // Get all the deliveryNoteList where discountType in DEFAULT_DISCOUNT_TYPE or UPDATED_DISCOUNT_TYPE
        defaultDeliveryNoteShouldBeFound("discountType.in=" + DEFAULT_DISCOUNT_TYPE + "," + UPDATED_DISCOUNT_TYPE);

        // Get all the deliveryNoteList where discountType equals to UPDATED_DISCOUNT_TYPE
        defaultDeliveryNoteShouldNotBeFound("discountType.in=" + UPDATED_DISCOUNT_TYPE);
    }

    @Test
    @Transactional
    void getAllDeliveryNotesByDiscountTypeIsNullOrNotNull() throws Exception {
        // Initialize the database
        deliveryNoteRepository.saveAndFlush(deliveryNote);

        // Get all the deliveryNoteList where discountType is not null
        defaultDeliveryNoteShouldBeFound("discountType.specified=true");

        // Get all the deliveryNoteList where discountType is null
        defaultDeliveryNoteShouldNotBeFound("discountType.specified=false");
    }

    @Test
    @Transactional
    void getAllDeliveryNotesByLanguageIsEqualToSomething() throws Exception {
        // Initialize the database
        deliveryNoteRepository.saveAndFlush(deliveryNote);

        // Get all the deliveryNoteList where language equals to DEFAULT_LANGUAGE
        defaultDeliveryNoteShouldBeFound("language.equals=" + DEFAULT_LANGUAGE);

        // Get all the deliveryNoteList where language equals to UPDATED_LANGUAGE
        defaultDeliveryNoteShouldNotBeFound("language.equals=" + UPDATED_LANGUAGE);
    }

    @Test
    @Transactional
    void getAllDeliveryNotesByLanguageIsNotEqualToSomething() throws Exception {
        // Initialize the database
        deliveryNoteRepository.saveAndFlush(deliveryNote);

        // Get all the deliveryNoteList where language not equals to DEFAULT_LANGUAGE
        defaultDeliveryNoteShouldNotBeFound("language.notEquals=" + DEFAULT_LANGUAGE);

        // Get all the deliveryNoteList where language not equals to UPDATED_LANGUAGE
        defaultDeliveryNoteShouldBeFound("language.notEquals=" + UPDATED_LANGUAGE);
    }

    @Test
    @Transactional
    void getAllDeliveryNotesByLanguageIsInShouldWork() throws Exception {
        // Initialize the database
        deliveryNoteRepository.saveAndFlush(deliveryNote);

        // Get all the deliveryNoteList where language in DEFAULT_LANGUAGE or UPDATED_LANGUAGE
        defaultDeliveryNoteShouldBeFound("language.in=" + DEFAULT_LANGUAGE + "," + UPDATED_LANGUAGE);

        // Get all the deliveryNoteList where language equals to UPDATED_LANGUAGE
        defaultDeliveryNoteShouldNotBeFound("language.in=" + UPDATED_LANGUAGE);
    }

    @Test
    @Transactional
    void getAllDeliveryNotesByLanguageIsNullOrNotNull() throws Exception {
        // Initialize the database
        deliveryNoteRepository.saveAndFlush(deliveryNote);

        // Get all the deliveryNoteList where language is not null
        defaultDeliveryNoteShouldBeFound("language.specified=true");

        // Get all the deliveryNoteList where language is null
        defaultDeliveryNoteShouldNotBeFound("language.specified=false");
    }

    @Test
    @Transactional
    void getAllDeliveryNotesByPageAmountIsEqualToSomething() throws Exception {
        // Initialize the database
        deliveryNoteRepository.saveAndFlush(deliveryNote);

        // Get all the deliveryNoteList where pageAmount equals to DEFAULT_PAGE_AMOUNT
        defaultDeliveryNoteShouldBeFound("pageAmount.equals=" + DEFAULT_PAGE_AMOUNT);

        // Get all the deliveryNoteList where pageAmount equals to UPDATED_PAGE_AMOUNT
        defaultDeliveryNoteShouldNotBeFound("pageAmount.equals=" + UPDATED_PAGE_AMOUNT);
    }

    @Test
    @Transactional
    void getAllDeliveryNotesByPageAmountIsNotEqualToSomething() throws Exception {
        // Initialize the database
        deliveryNoteRepository.saveAndFlush(deliveryNote);

        // Get all the deliveryNoteList where pageAmount not equals to DEFAULT_PAGE_AMOUNT
        defaultDeliveryNoteShouldNotBeFound("pageAmount.notEquals=" + DEFAULT_PAGE_AMOUNT);

        // Get all the deliveryNoteList where pageAmount not equals to UPDATED_PAGE_AMOUNT
        defaultDeliveryNoteShouldBeFound("pageAmount.notEquals=" + UPDATED_PAGE_AMOUNT);
    }

    @Test
    @Transactional
    void getAllDeliveryNotesByPageAmountIsInShouldWork() throws Exception {
        // Initialize the database
        deliveryNoteRepository.saveAndFlush(deliveryNote);

        // Get all the deliveryNoteList where pageAmount in DEFAULT_PAGE_AMOUNT or UPDATED_PAGE_AMOUNT
        defaultDeliveryNoteShouldBeFound("pageAmount.in=" + DEFAULT_PAGE_AMOUNT + "," + UPDATED_PAGE_AMOUNT);

        // Get all the deliveryNoteList where pageAmount equals to UPDATED_PAGE_AMOUNT
        defaultDeliveryNoteShouldNotBeFound("pageAmount.in=" + UPDATED_PAGE_AMOUNT);
    }

    @Test
    @Transactional
    void getAllDeliveryNotesByPageAmountIsNullOrNotNull() throws Exception {
        // Initialize the database
        deliveryNoteRepository.saveAndFlush(deliveryNote);

        // Get all the deliveryNoteList where pageAmount is not null
        defaultDeliveryNoteShouldBeFound("pageAmount.specified=true");

        // Get all the deliveryNoteList where pageAmount is null
        defaultDeliveryNoteShouldNotBeFound("pageAmount.specified=false");
    }

    @Test
    @Transactional
    void getAllDeliveryNotesByPageAmountIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        deliveryNoteRepository.saveAndFlush(deliveryNote);

        // Get all the deliveryNoteList where pageAmount is greater than or equal to DEFAULT_PAGE_AMOUNT
        defaultDeliveryNoteShouldBeFound("pageAmount.greaterThanOrEqual=" + DEFAULT_PAGE_AMOUNT);

        // Get all the deliveryNoteList where pageAmount is greater than or equal to UPDATED_PAGE_AMOUNT
        defaultDeliveryNoteShouldNotBeFound("pageAmount.greaterThanOrEqual=" + UPDATED_PAGE_AMOUNT);
    }

    @Test
    @Transactional
    void getAllDeliveryNotesByPageAmountIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        deliveryNoteRepository.saveAndFlush(deliveryNote);

        // Get all the deliveryNoteList where pageAmount is less than or equal to DEFAULT_PAGE_AMOUNT
        defaultDeliveryNoteShouldBeFound("pageAmount.lessThanOrEqual=" + DEFAULT_PAGE_AMOUNT);

        // Get all the deliveryNoteList where pageAmount is less than or equal to SMALLER_PAGE_AMOUNT
        defaultDeliveryNoteShouldNotBeFound("pageAmount.lessThanOrEqual=" + SMALLER_PAGE_AMOUNT);
    }

    @Test
    @Transactional
    void getAllDeliveryNotesByPageAmountIsLessThanSomething() throws Exception {
        // Initialize the database
        deliveryNoteRepository.saveAndFlush(deliveryNote);

        // Get all the deliveryNoteList where pageAmount is less than DEFAULT_PAGE_AMOUNT
        defaultDeliveryNoteShouldNotBeFound("pageAmount.lessThan=" + DEFAULT_PAGE_AMOUNT);

        // Get all the deliveryNoteList where pageAmount is less than UPDATED_PAGE_AMOUNT
        defaultDeliveryNoteShouldBeFound("pageAmount.lessThan=" + UPDATED_PAGE_AMOUNT);
    }

    @Test
    @Transactional
    void getAllDeliveryNotesByPageAmountIsGreaterThanSomething() throws Exception {
        // Initialize the database
        deliveryNoteRepository.saveAndFlush(deliveryNote);

        // Get all the deliveryNoteList where pageAmount is greater than DEFAULT_PAGE_AMOUNT
        defaultDeliveryNoteShouldNotBeFound("pageAmount.greaterThan=" + DEFAULT_PAGE_AMOUNT);

        // Get all the deliveryNoteList where pageAmount is greater than SMALLER_PAGE_AMOUNT
        defaultDeliveryNoteShouldBeFound("pageAmount.greaterThan=" + SMALLER_PAGE_AMOUNT);
    }

    @Test
    @Transactional
    void getAllDeliveryNotesByNotesIsEqualToSomething() throws Exception {
        // Initialize the database
        deliveryNoteRepository.saveAndFlush(deliveryNote);

        // Get all the deliveryNoteList where notes equals to DEFAULT_NOTES
        defaultDeliveryNoteShouldBeFound("notes.equals=" + DEFAULT_NOTES);

        // Get all the deliveryNoteList where notes equals to UPDATED_NOTES
        defaultDeliveryNoteShouldNotBeFound("notes.equals=" + UPDATED_NOTES);
    }

    @Test
    @Transactional
    void getAllDeliveryNotesByNotesIsNotEqualToSomething() throws Exception {
        // Initialize the database
        deliveryNoteRepository.saveAndFlush(deliveryNote);

        // Get all the deliveryNoteList where notes not equals to DEFAULT_NOTES
        defaultDeliveryNoteShouldNotBeFound("notes.notEquals=" + DEFAULT_NOTES);

        // Get all the deliveryNoteList where notes not equals to UPDATED_NOTES
        defaultDeliveryNoteShouldBeFound("notes.notEquals=" + UPDATED_NOTES);
    }

    @Test
    @Transactional
    void getAllDeliveryNotesByNotesIsInShouldWork() throws Exception {
        // Initialize the database
        deliveryNoteRepository.saveAndFlush(deliveryNote);

        // Get all the deliveryNoteList where notes in DEFAULT_NOTES or UPDATED_NOTES
        defaultDeliveryNoteShouldBeFound("notes.in=" + DEFAULT_NOTES + "," + UPDATED_NOTES);

        // Get all the deliveryNoteList where notes equals to UPDATED_NOTES
        defaultDeliveryNoteShouldNotBeFound("notes.in=" + UPDATED_NOTES);
    }

    @Test
    @Transactional
    void getAllDeliveryNotesByNotesIsNullOrNotNull() throws Exception {
        // Initialize the database
        deliveryNoteRepository.saveAndFlush(deliveryNote);

        // Get all the deliveryNoteList where notes is not null
        defaultDeliveryNoteShouldBeFound("notes.specified=true");

        // Get all the deliveryNoteList where notes is null
        defaultDeliveryNoteShouldNotBeFound("notes.specified=false");
    }

    @Test
    @Transactional
    void getAllDeliveryNotesByNotesContainsSomething() throws Exception {
        // Initialize the database
        deliveryNoteRepository.saveAndFlush(deliveryNote);

        // Get all the deliveryNoteList where notes contains DEFAULT_NOTES
        defaultDeliveryNoteShouldBeFound("notes.contains=" + DEFAULT_NOTES);

        // Get all the deliveryNoteList where notes contains UPDATED_NOTES
        defaultDeliveryNoteShouldNotBeFound("notes.contains=" + UPDATED_NOTES);
    }

    @Test
    @Transactional
    void getAllDeliveryNotesByNotesNotContainsSomething() throws Exception {
        // Initialize the database
        deliveryNoteRepository.saveAndFlush(deliveryNote);

        // Get all the deliveryNoteList where notes does not contain DEFAULT_NOTES
        defaultDeliveryNoteShouldNotBeFound("notes.doesNotContain=" + DEFAULT_NOTES);

        // Get all the deliveryNoteList where notes does not contain UPDATED_NOTES
        defaultDeliveryNoteShouldBeFound("notes.doesNotContain=" + UPDATED_NOTES);
    }

    @Test
    @Transactional
    void getAllDeliveryNotesByStatusIsEqualToSomething() throws Exception {
        // Initialize the database
        deliveryNoteRepository.saveAndFlush(deliveryNote);

        // Get all the deliveryNoteList where status equals to DEFAULT_STATUS
        defaultDeliveryNoteShouldBeFound("status.equals=" + DEFAULT_STATUS);

        // Get all the deliveryNoteList where status equals to UPDATED_STATUS
        defaultDeliveryNoteShouldNotBeFound("status.equals=" + UPDATED_STATUS);
    }

    @Test
    @Transactional
    void getAllDeliveryNotesByStatusIsNotEqualToSomething() throws Exception {
        // Initialize the database
        deliveryNoteRepository.saveAndFlush(deliveryNote);

        // Get all the deliveryNoteList where status not equals to DEFAULT_STATUS
        defaultDeliveryNoteShouldNotBeFound("status.notEquals=" + DEFAULT_STATUS);

        // Get all the deliveryNoteList where status not equals to UPDATED_STATUS
        defaultDeliveryNoteShouldBeFound("status.notEquals=" + UPDATED_STATUS);
    }

    @Test
    @Transactional
    void getAllDeliveryNotesByStatusIsInShouldWork() throws Exception {
        // Initialize the database
        deliveryNoteRepository.saveAndFlush(deliveryNote);

        // Get all the deliveryNoteList where status in DEFAULT_STATUS or UPDATED_STATUS
        defaultDeliveryNoteShouldBeFound("status.in=" + DEFAULT_STATUS + "," + UPDATED_STATUS);

        // Get all the deliveryNoteList where status equals to UPDATED_STATUS
        defaultDeliveryNoteShouldNotBeFound("status.in=" + UPDATED_STATUS);
    }

    @Test
    @Transactional
    void getAllDeliveryNotesByStatusIsNullOrNotNull() throws Exception {
        // Initialize the database
        deliveryNoteRepository.saveAndFlush(deliveryNote);

        // Get all the deliveryNoteList where status is not null
        defaultDeliveryNoteShouldBeFound("status.specified=true");

        // Get all the deliveryNoteList where status is null
        defaultDeliveryNoteShouldNotBeFound("status.specified=false");
    }

    @Test
    @Transactional
    void getAllDeliveryNotesByCreatedIsEqualToSomething() throws Exception {
        // Initialize the database
        deliveryNoteRepository.saveAndFlush(deliveryNote);

        // Get all the deliveryNoteList where created equals to DEFAULT_CREATED
        defaultDeliveryNoteShouldBeFound("created.equals=" + DEFAULT_CREATED);

        // Get all the deliveryNoteList where created equals to UPDATED_CREATED
        defaultDeliveryNoteShouldNotBeFound("created.equals=" + UPDATED_CREATED);
    }

    @Test
    @Transactional
    void getAllDeliveryNotesByCreatedIsNotEqualToSomething() throws Exception {
        // Initialize the database
        deliveryNoteRepository.saveAndFlush(deliveryNote);

        // Get all the deliveryNoteList where created not equals to DEFAULT_CREATED
        defaultDeliveryNoteShouldNotBeFound("created.notEquals=" + DEFAULT_CREATED);

        // Get all the deliveryNoteList where created not equals to UPDATED_CREATED
        defaultDeliveryNoteShouldBeFound("created.notEquals=" + UPDATED_CREATED);
    }

    @Test
    @Transactional
    void getAllDeliveryNotesByCreatedIsInShouldWork() throws Exception {
        // Initialize the database
        deliveryNoteRepository.saveAndFlush(deliveryNote);

        // Get all the deliveryNoteList where created in DEFAULT_CREATED or UPDATED_CREATED
        defaultDeliveryNoteShouldBeFound("created.in=" + DEFAULT_CREATED + "," + UPDATED_CREATED);

        // Get all the deliveryNoteList where created equals to UPDATED_CREATED
        defaultDeliveryNoteShouldNotBeFound("created.in=" + UPDATED_CREATED);
    }

    @Test
    @Transactional
    void getAllDeliveryNotesByCreatedIsNullOrNotNull() throws Exception {
        // Initialize the database
        deliveryNoteRepository.saveAndFlush(deliveryNote);

        // Get all the deliveryNoteList where created is not null
        defaultDeliveryNoteShouldBeFound("created.specified=true");

        // Get all the deliveryNoteList where created is null
        defaultDeliveryNoteShouldNotBeFound("created.specified=false");
    }

    @Test
    @Transactional
    void getAllDeliveryNotesByCreatedIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        deliveryNoteRepository.saveAndFlush(deliveryNote);

        // Get all the deliveryNoteList where created is greater than or equal to DEFAULT_CREATED
        defaultDeliveryNoteShouldBeFound("created.greaterThanOrEqual=" + DEFAULT_CREATED);

        // Get all the deliveryNoteList where created is greater than or equal to UPDATED_CREATED
        defaultDeliveryNoteShouldNotBeFound("created.greaterThanOrEqual=" + UPDATED_CREATED);
    }

    @Test
    @Transactional
    void getAllDeliveryNotesByCreatedIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        deliveryNoteRepository.saveAndFlush(deliveryNote);

        // Get all the deliveryNoteList where created is less than or equal to DEFAULT_CREATED
        defaultDeliveryNoteShouldBeFound("created.lessThanOrEqual=" + DEFAULT_CREATED);

        // Get all the deliveryNoteList where created is less than or equal to SMALLER_CREATED
        defaultDeliveryNoteShouldNotBeFound("created.lessThanOrEqual=" + SMALLER_CREATED);
    }

    @Test
    @Transactional
    void getAllDeliveryNotesByCreatedIsLessThanSomething() throws Exception {
        // Initialize the database
        deliveryNoteRepository.saveAndFlush(deliveryNote);

        // Get all the deliveryNoteList where created is less than DEFAULT_CREATED
        defaultDeliveryNoteShouldNotBeFound("created.lessThan=" + DEFAULT_CREATED);

        // Get all the deliveryNoteList where created is less than UPDATED_CREATED
        defaultDeliveryNoteShouldBeFound("created.lessThan=" + UPDATED_CREATED);
    }

    @Test
    @Transactional
    void getAllDeliveryNotesByCreatedIsGreaterThanSomething() throws Exception {
        // Initialize the database
        deliveryNoteRepository.saveAndFlush(deliveryNote);

        // Get all the deliveryNoteList where created is greater than DEFAULT_CREATED
        defaultDeliveryNoteShouldNotBeFound("created.greaterThan=" + DEFAULT_CREATED);

        // Get all the deliveryNoteList where created is greater than SMALLER_CREATED
        defaultDeliveryNoteShouldBeFound("created.greaterThan=" + SMALLER_CREATED);
    }

    @Test
    @Transactional
    void getAllDeliveryNotesByCustomFieldsIsEqualToSomething() throws Exception {
        // Initialize the database
        deliveryNoteRepository.saveAndFlush(deliveryNote);
        CustomFieldValue customFields = CustomFieldValueResourceIT.createEntity(em);
        em.persist(customFields);
        em.flush();
        deliveryNote.addCustomFields(customFields);
        deliveryNoteRepository.saveAndFlush(deliveryNote);
        Long customFieldsId = customFields.getId();

        // Get all the deliveryNoteList where customFields equals to customFieldsId
        defaultDeliveryNoteShouldBeFound("customFieldsId.equals=" + customFieldsId);

        // Get all the deliveryNoteList where customFields equals to (customFieldsId + 1)
        defaultDeliveryNoteShouldNotBeFound("customFieldsId.equals=" + (customFieldsId + 1));
    }

    @Test
    @Transactional
    void getAllDeliveryNotesByFreeTextsIsEqualToSomething() throws Exception {
        // Initialize the database
        deliveryNoteRepository.saveAndFlush(deliveryNote);
        DocumentFreeText freeTexts = DocumentFreeTextResourceIT.createEntity(em);
        em.persist(freeTexts);
        em.flush();
        deliveryNote.addFreeTexts(freeTexts);
        deliveryNoteRepository.saveAndFlush(deliveryNote);
        Long freeTextsId = freeTexts.getId();

        // Get all the deliveryNoteList where freeTexts equals to freeTextsId
        defaultDeliveryNoteShouldBeFound("freeTextsId.equals=" + freeTextsId);

        // Get all the deliveryNoteList where freeTexts equals to (freeTextsId + 1)
        defaultDeliveryNoteShouldNotBeFound("freeTextsId.equals=" + (freeTextsId + 1));
    }

    @Test
    @Transactional
    void getAllDeliveryNotesByTextsIsEqualToSomething() throws Exception {
        // Initialize the database
        deliveryNoteRepository.saveAndFlush(deliveryNote);
        DescriptiveDocumentText texts = DescriptiveDocumentTextResourceIT.createEntity(em);
        em.persist(texts);
        em.flush();
        deliveryNote.addTexts(texts);
        deliveryNoteRepository.saveAndFlush(deliveryNote);
        Long textsId = texts.getId();

        // Get all the deliveryNoteList where texts equals to textsId
        defaultDeliveryNoteShouldBeFound("textsId.equals=" + textsId);

        // Get all the deliveryNoteList where texts equals to (textsId + 1)
        defaultDeliveryNoteShouldNotBeFound("textsId.equals=" + (textsId + 1));
    }

    @Test
    @Transactional
    void getAllDeliveryNotesByPositionsIsEqualToSomething() throws Exception {
        // Initialize the database
        deliveryNoteRepository.saveAndFlush(deliveryNote);
        DocumentPosition positions = DocumentPositionResourceIT.createEntity(em);
        em.persist(positions);
        em.flush();
        deliveryNote.addPositions(positions);
        deliveryNoteRepository.saveAndFlush(deliveryNote);
        Long positionsId = positions.getId();

        // Get all the deliveryNoteList where positions equals to positionsId
        defaultDeliveryNoteShouldBeFound("positionsId.equals=" + positionsId);

        // Get all the deliveryNoteList where positions equals to (positionsId + 1)
        defaultDeliveryNoteShouldNotBeFound("positionsId.equals=" + (positionsId + 1));
    }

    @Test
    @Transactional
    void getAllDeliveryNotesByContactIsEqualToSomething() throws Exception {
        // Initialize the database
        deliveryNoteRepository.saveAndFlush(deliveryNote);
        Contact contact = ContactResourceIT.createEntity(em);
        em.persist(contact);
        em.flush();
        deliveryNote.setContact(contact);
        deliveryNoteRepository.saveAndFlush(deliveryNote);
        Long contactId = contact.getId();

        // Get all the deliveryNoteList where contact equals to contactId
        defaultDeliveryNoteShouldBeFound("contactId.equals=" + contactId);

        // Get all the deliveryNoteList where contact equals to (contactId + 1)
        defaultDeliveryNoteShouldNotBeFound("contactId.equals=" + (contactId + 1));
    }

    @Test
    @Transactional
    void getAllDeliveryNotesByContactAddressIsEqualToSomething() throws Exception {
        // Initialize the database
        deliveryNoteRepository.saveAndFlush(deliveryNote);
        ContactAddress contactAddress = ContactAddressResourceIT.createEntity(em);
        em.persist(contactAddress);
        em.flush();
        deliveryNote.setContactAddress(contactAddress);
        deliveryNoteRepository.saveAndFlush(deliveryNote);
        Long contactAddressId = contactAddress.getId();

        // Get all the deliveryNoteList where contactAddress equals to contactAddressId
        defaultDeliveryNoteShouldBeFound("contactAddressId.equals=" + contactAddressId);

        // Get all the deliveryNoteList where contactAddress equals to (contactAddressId + 1)
        defaultDeliveryNoteShouldNotBeFound("contactAddressId.equals=" + (contactAddressId + 1));
    }

    @Test
    @Transactional
    void getAllDeliveryNotesByContactPersonIsEqualToSomething() throws Exception {
        // Initialize the database
        deliveryNoteRepository.saveAndFlush(deliveryNote);
        ContactPerson contactPerson = ContactPersonResourceIT.createEntity(em);
        em.persist(contactPerson);
        em.flush();
        deliveryNote.setContactPerson(contactPerson);
        deliveryNoteRepository.saveAndFlush(deliveryNote);
        Long contactPersonId = contactPerson.getId();

        // Get all the deliveryNoteList where contactPerson equals to contactPersonId
        defaultDeliveryNoteShouldBeFound("contactPersonId.equals=" + contactPersonId);

        // Get all the deliveryNoteList where contactPerson equals to (contactPersonId + 1)
        defaultDeliveryNoteShouldNotBeFound("contactPersonId.equals=" + (contactPersonId + 1));
    }

    @Test
    @Transactional
    void getAllDeliveryNotesByContactPrePageAddressIsEqualToSomething() throws Exception {
        // Initialize the database
        deliveryNoteRepository.saveAndFlush(deliveryNote);
        ContactAddress contactPrePageAddress = ContactAddressResourceIT.createEntity(em);
        em.persist(contactPrePageAddress);
        em.flush();
        deliveryNote.setContactPrePageAddress(contactPrePageAddress);
        deliveryNoteRepository.saveAndFlush(deliveryNote);
        Long contactPrePageAddressId = contactPrePageAddress.getId();

        // Get all the deliveryNoteList where contactPrePageAddress equals to contactPrePageAddressId
        defaultDeliveryNoteShouldBeFound("contactPrePageAddressId.equals=" + contactPrePageAddressId);

        // Get all the deliveryNoteList where contactPrePageAddress equals to (contactPrePageAddressId + 1)
        defaultDeliveryNoteShouldNotBeFound("contactPrePageAddressId.equals=" + (contactPrePageAddressId + 1));
    }

    @Test
    @Transactional
    void getAllDeliveryNotesByLayoutIsEqualToSomething() throws Exception {
        // Initialize the database
        deliveryNoteRepository.saveAndFlush(deliveryNote);
        Layout layout = LayoutResourceIT.createEntity(em);
        em.persist(layout);
        em.flush();
        deliveryNote.setLayout(layout);
        deliveryNoteRepository.saveAndFlush(deliveryNote);
        Long layoutId = layout.getId();

        // Get all the deliveryNoteList where layout equals to layoutId
        defaultDeliveryNoteShouldBeFound("layoutId.equals=" + layoutId);

        // Get all the deliveryNoteList where layout equals to (layoutId + 1)
        defaultDeliveryNoteShouldNotBeFound("layoutId.equals=" + (layoutId + 1));
    }

    @Test
    @Transactional
    void getAllDeliveryNotesByLayoutIsEqualToSomething() throws Exception {
        // Initialize the database
        deliveryNoteRepository.saveAndFlush(deliveryNote);
        Signature layout = SignatureResourceIT.createEntity(em);
        em.persist(layout);
        em.flush();
        deliveryNote.setLayout(layout);
        deliveryNoteRepository.saveAndFlush(deliveryNote);
        Long layoutId = layout.getId();

        // Get all the deliveryNoteList where layout equals to layoutId
        defaultDeliveryNoteShouldBeFound("layoutId.equals=" + layoutId);

        // Get all the deliveryNoteList where layout equals to (layoutId + 1)
        defaultDeliveryNoteShouldNotBeFound("layoutId.equals=" + (layoutId + 1));
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultDeliveryNoteShouldBeFound(String filter) throws Exception {
        restDeliveryNoteMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(deliveryNote.getId().intValue())))
            .andExpect(jsonPath("$.[*].remoteId").value(hasItem(DEFAULT_REMOTE_ID)))
            .andExpect(jsonPath("$.[*].number").value(hasItem(DEFAULT_NUMBER)))
            .andExpect(jsonPath("$.[*].contactName").value(hasItem(DEFAULT_CONTACT_NAME)))
            .andExpect(jsonPath("$.[*].date").value(hasItem(DEFAULT_DATE.toString())))
            .andExpect(jsonPath("$.[*].periodText").value(hasItem(DEFAULT_PERIOD_TEXT)))
            .andExpect(jsonPath("$.[*].currency").value(hasItem(DEFAULT_CURRENCY.toString())))
            .andExpect(jsonPath("$.[*].total").value(hasItem(DEFAULT_TOTAL.doubleValue())))
            .andExpect(jsonPath("$.[*].vatIncluded").value(hasItem(DEFAULT_VAT_INCLUDED.booleanValue())))
            .andExpect(jsonPath("$.[*].discountRate").value(hasItem(DEFAULT_DISCOUNT_RATE.doubleValue())))
            .andExpect(jsonPath("$.[*].discountType").value(hasItem(DEFAULT_DISCOUNT_TYPE.toString())))
            .andExpect(jsonPath("$.[*].language").value(hasItem(DEFAULT_LANGUAGE.toString())))
            .andExpect(jsonPath("$.[*].pageAmount").value(hasItem(DEFAULT_PAGE_AMOUNT)))
            .andExpect(jsonPath("$.[*].notes").value(hasItem(DEFAULT_NOTES)))
            .andExpect(jsonPath("$.[*].status").value(hasItem(DEFAULT_STATUS.toString())))
            .andExpect(jsonPath("$.[*].created").value(hasItem(sameInstant(DEFAULT_CREATED))));

        // Check, that the count call also returns 1
        restDeliveryNoteMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultDeliveryNoteShouldNotBeFound(String filter) throws Exception {
        restDeliveryNoteMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restDeliveryNoteMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingDeliveryNote() throws Exception {
        // Get the deliveryNote
        restDeliveryNoteMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewDeliveryNote() throws Exception {
        // Initialize the database
        deliveryNoteRepository.saveAndFlush(deliveryNote);

        int databaseSizeBeforeUpdate = deliveryNoteRepository.findAll().size();

        // Update the deliveryNote
        DeliveryNote updatedDeliveryNote = deliveryNoteRepository.findById(deliveryNote.getId()).get();
        // Disconnect from session so that the updates on updatedDeliveryNote are not directly saved in db
        em.detach(updatedDeliveryNote);
        updatedDeliveryNote
            .remoteId(UPDATED_REMOTE_ID)
            .number(UPDATED_NUMBER)
            .contactName(UPDATED_CONTACT_NAME)
            .date(UPDATED_DATE)
            .periodText(UPDATED_PERIOD_TEXT)
            .currency(UPDATED_CURRENCY)
            .total(UPDATED_TOTAL)
            .vatIncluded(UPDATED_VAT_INCLUDED)
            .discountRate(UPDATED_DISCOUNT_RATE)
            .discountType(UPDATED_DISCOUNT_TYPE)
            .language(UPDATED_LANGUAGE)
            .pageAmount(UPDATED_PAGE_AMOUNT)
            .notes(UPDATED_NOTES)
            .status(UPDATED_STATUS)
            .created(UPDATED_CREATED);
        DeliveryNoteDTO deliveryNoteDTO = deliveryNoteMapper.toDto(updatedDeliveryNote);

        restDeliveryNoteMockMvc
            .perform(
                put(ENTITY_API_URL_ID, deliveryNoteDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(deliveryNoteDTO))
            )
            .andExpect(status().isOk());

        // Validate the DeliveryNote in the database
        List<DeliveryNote> deliveryNoteList = deliveryNoteRepository.findAll();
        assertThat(deliveryNoteList).hasSize(databaseSizeBeforeUpdate);
        DeliveryNote testDeliveryNote = deliveryNoteList.get(deliveryNoteList.size() - 1);
        assertThat(testDeliveryNote.getRemoteId()).isEqualTo(UPDATED_REMOTE_ID);
        assertThat(testDeliveryNote.getNumber()).isEqualTo(UPDATED_NUMBER);
        assertThat(testDeliveryNote.getContactName()).isEqualTo(UPDATED_CONTACT_NAME);
        assertThat(testDeliveryNote.getDate()).isEqualTo(UPDATED_DATE);
        assertThat(testDeliveryNote.getPeriodText()).isEqualTo(UPDATED_PERIOD_TEXT);
        assertThat(testDeliveryNote.getCurrency()).isEqualTo(UPDATED_CURRENCY);
        assertThat(testDeliveryNote.getTotal()).isEqualTo(UPDATED_TOTAL);
        assertThat(testDeliveryNote.getVatIncluded()).isEqualTo(UPDATED_VAT_INCLUDED);
        assertThat(testDeliveryNote.getDiscountRate()).isEqualTo(UPDATED_DISCOUNT_RATE);
        assertThat(testDeliveryNote.getDiscountType()).isEqualTo(UPDATED_DISCOUNT_TYPE);
        assertThat(testDeliveryNote.getLanguage()).isEqualTo(UPDATED_LANGUAGE);
        assertThat(testDeliveryNote.getPageAmount()).isEqualTo(UPDATED_PAGE_AMOUNT);
        assertThat(testDeliveryNote.getNotes()).isEqualTo(UPDATED_NOTES);
        assertThat(testDeliveryNote.getStatus()).isEqualTo(UPDATED_STATUS);
        assertThat(testDeliveryNote.getCreated()).isEqualTo(UPDATED_CREATED);
    }

    @Test
    @Transactional
    void putNonExistingDeliveryNote() throws Exception {
        int databaseSizeBeforeUpdate = deliveryNoteRepository.findAll().size();
        deliveryNote.setId(count.incrementAndGet());

        // Create the DeliveryNote
        DeliveryNoteDTO deliveryNoteDTO = deliveryNoteMapper.toDto(deliveryNote);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restDeliveryNoteMockMvc
            .perform(
                put(ENTITY_API_URL_ID, deliveryNoteDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(deliveryNoteDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the DeliveryNote in the database
        List<DeliveryNote> deliveryNoteList = deliveryNoteRepository.findAll();
        assertThat(deliveryNoteList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchDeliveryNote() throws Exception {
        int databaseSizeBeforeUpdate = deliveryNoteRepository.findAll().size();
        deliveryNote.setId(count.incrementAndGet());

        // Create the DeliveryNote
        DeliveryNoteDTO deliveryNoteDTO = deliveryNoteMapper.toDto(deliveryNote);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restDeliveryNoteMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(deliveryNoteDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the DeliveryNote in the database
        List<DeliveryNote> deliveryNoteList = deliveryNoteRepository.findAll();
        assertThat(deliveryNoteList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamDeliveryNote() throws Exception {
        int databaseSizeBeforeUpdate = deliveryNoteRepository.findAll().size();
        deliveryNote.setId(count.incrementAndGet());

        // Create the DeliveryNote
        DeliveryNoteDTO deliveryNoteDTO = deliveryNoteMapper.toDto(deliveryNote);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restDeliveryNoteMockMvc
            .perform(
                put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(deliveryNoteDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the DeliveryNote in the database
        List<DeliveryNote> deliveryNoteList = deliveryNoteRepository.findAll();
        assertThat(deliveryNoteList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateDeliveryNoteWithPatch() throws Exception {
        // Initialize the database
        deliveryNoteRepository.saveAndFlush(deliveryNote);

        int databaseSizeBeforeUpdate = deliveryNoteRepository.findAll().size();

        // Update the deliveryNote using partial update
        DeliveryNote partialUpdatedDeliveryNote = new DeliveryNote();
        partialUpdatedDeliveryNote.setId(deliveryNote.getId());

        partialUpdatedDeliveryNote
            .number(UPDATED_NUMBER)
            .date(UPDATED_DATE)
            .discountType(UPDATED_DISCOUNT_TYPE)
            .language(UPDATED_LANGUAGE)
            .pageAmount(UPDATED_PAGE_AMOUNT)
            .created(UPDATED_CREATED);

        restDeliveryNoteMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedDeliveryNote.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedDeliveryNote))
            )
            .andExpect(status().isOk());

        // Validate the DeliveryNote in the database
        List<DeliveryNote> deliveryNoteList = deliveryNoteRepository.findAll();
        assertThat(deliveryNoteList).hasSize(databaseSizeBeforeUpdate);
        DeliveryNote testDeliveryNote = deliveryNoteList.get(deliveryNoteList.size() - 1);
        assertThat(testDeliveryNote.getRemoteId()).isEqualTo(DEFAULT_REMOTE_ID);
        assertThat(testDeliveryNote.getNumber()).isEqualTo(UPDATED_NUMBER);
        assertThat(testDeliveryNote.getContactName()).isEqualTo(DEFAULT_CONTACT_NAME);
        assertThat(testDeliveryNote.getDate()).isEqualTo(UPDATED_DATE);
        assertThat(testDeliveryNote.getPeriodText()).isEqualTo(DEFAULT_PERIOD_TEXT);
        assertThat(testDeliveryNote.getCurrency()).isEqualTo(DEFAULT_CURRENCY);
        assertThat(testDeliveryNote.getTotal()).isEqualTo(DEFAULT_TOTAL);
        assertThat(testDeliveryNote.getVatIncluded()).isEqualTo(DEFAULT_VAT_INCLUDED);
        assertThat(testDeliveryNote.getDiscountRate()).isEqualTo(DEFAULT_DISCOUNT_RATE);
        assertThat(testDeliveryNote.getDiscountType()).isEqualTo(UPDATED_DISCOUNT_TYPE);
        assertThat(testDeliveryNote.getLanguage()).isEqualTo(UPDATED_LANGUAGE);
        assertThat(testDeliveryNote.getPageAmount()).isEqualTo(UPDATED_PAGE_AMOUNT);
        assertThat(testDeliveryNote.getNotes()).isEqualTo(DEFAULT_NOTES);
        assertThat(testDeliveryNote.getStatus()).isEqualTo(DEFAULT_STATUS);
        assertThat(testDeliveryNote.getCreated()).isEqualTo(UPDATED_CREATED);
    }

    @Test
    @Transactional
    void fullUpdateDeliveryNoteWithPatch() throws Exception {
        // Initialize the database
        deliveryNoteRepository.saveAndFlush(deliveryNote);

        int databaseSizeBeforeUpdate = deliveryNoteRepository.findAll().size();

        // Update the deliveryNote using partial update
        DeliveryNote partialUpdatedDeliveryNote = new DeliveryNote();
        partialUpdatedDeliveryNote.setId(deliveryNote.getId());

        partialUpdatedDeliveryNote
            .remoteId(UPDATED_REMOTE_ID)
            .number(UPDATED_NUMBER)
            .contactName(UPDATED_CONTACT_NAME)
            .date(UPDATED_DATE)
            .periodText(UPDATED_PERIOD_TEXT)
            .currency(UPDATED_CURRENCY)
            .total(UPDATED_TOTAL)
            .vatIncluded(UPDATED_VAT_INCLUDED)
            .discountRate(UPDATED_DISCOUNT_RATE)
            .discountType(UPDATED_DISCOUNT_TYPE)
            .language(UPDATED_LANGUAGE)
            .pageAmount(UPDATED_PAGE_AMOUNT)
            .notes(UPDATED_NOTES)
            .status(UPDATED_STATUS)
            .created(UPDATED_CREATED);

        restDeliveryNoteMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedDeliveryNote.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedDeliveryNote))
            )
            .andExpect(status().isOk());

        // Validate the DeliveryNote in the database
        List<DeliveryNote> deliveryNoteList = deliveryNoteRepository.findAll();
        assertThat(deliveryNoteList).hasSize(databaseSizeBeforeUpdate);
        DeliveryNote testDeliveryNote = deliveryNoteList.get(deliveryNoteList.size() - 1);
        assertThat(testDeliveryNote.getRemoteId()).isEqualTo(UPDATED_REMOTE_ID);
        assertThat(testDeliveryNote.getNumber()).isEqualTo(UPDATED_NUMBER);
        assertThat(testDeliveryNote.getContactName()).isEqualTo(UPDATED_CONTACT_NAME);
        assertThat(testDeliveryNote.getDate()).isEqualTo(UPDATED_DATE);
        assertThat(testDeliveryNote.getPeriodText()).isEqualTo(UPDATED_PERIOD_TEXT);
        assertThat(testDeliveryNote.getCurrency()).isEqualTo(UPDATED_CURRENCY);
        assertThat(testDeliveryNote.getTotal()).isEqualTo(UPDATED_TOTAL);
        assertThat(testDeliveryNote.getVatIncluded()).isEqualTo(UPDATED_VAT_INCLUDED);
        assertThat(testDeliveryNote.getDiscountRate()).isEqualTo(UPDATED_DISCOUNT_RATE);
        assertThat(testDeliveryNote.getDiscountType()).isEqualTo(UPDATED_DISCOUNT_TYPE);
        assertThat(testDeliveryNote.getLanguage()).isEqualTo(UPDATED_LANGUAGE);
        assertThat(testDeliveryNote.getPageAmount()).isEqualTo(UPDATED_PAGE_AMOUNT);
        assertThat(testDeliveryNote.getNotes()).isEqualTo(UPDATED_NOTES);
        assertThat(testDeliveryNote.getStatus()).isEqualTo(UPDATED_STATUS);
        assertThat(testDeliveryNote.getCreated()).isEqualTo(UPDATED_CREATED);
    }

    @Test
    @Transactional
    void patchNonExistingDeliveryNote() throws Exception {
        int databaseSizeBeforeUpdate = deliveryNoteRepository.findAll().size();
        deliveryNote.setId(count.incrementAndGet());

        // Create the DeliveryNote
        DeliveryNoteDTO deliveryNoteDTO = deliveryNoteMapper.toDto(deliveryNote);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restDeliveryNoteMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, deliveryNoteDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(deliveryNoteDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the DeliveryNote in the database
        List<DeliveryNote> deliveryNoteList = deliveryNoteRepository.findAll();
        assertThat(deliveryNoteList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchDeliveryNote() throws Exception {
        int databaseSizeBeforeUpdate = deliveryNoteRepository.findAll().size();
        deliveryNote.setId(count.incrementAndGet());

        // Create the DeliveryNote
        DeliveryNoteDTO deliveryNoteDTO = deliveryNoteMapper.toDto(deliveryNote);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restDeliveryNoteMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(deliveryNoteDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the DeliveryNote in the database
        List<DeliveryNote> deliveryNoteList = deliveryNoteRepository.findAll();
        assertThat(deliveryNoteList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamDeliveryNote() throws Exception {
        int databaseSizeBeforeUpdate = deliveryNoteRepository.findAll().size();
        deliveryNote.setId(count.incrementAndGet());

        // Create the DeliveryNote
        DeliveryNoteDTO deliveryNoteDTO = deliveryNoteMapper.toDto(deliveryNote);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restDeliveryNoteMockMvc
            .perform(
                patch(ENTITY_API_URL)
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(deliveryNoteDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the DeliveryNote in the database
        List<DeliveryNote> deliveryNoteList = deliveryNoteRepository.findAll();
        assertThat(deliveryNoteList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteDeliveryNote() throws Exception {
        // Initialize the database
        deliveryNoteRepository.saveAndFlush(deliveryNote);

        int databaseSizeBeforeDelete = deliveryNoteRepository.findAll().size();

        // Delete the deliveryNote
        restDeliveryNoteMockMvc
            .perform(delete(ENTITY_API_URL_ID, deliveryNote.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<DeliveryNote> deliveryNoteList = deliveryNoteRepository.findAll();
        assertThat(deliveryNoteList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
