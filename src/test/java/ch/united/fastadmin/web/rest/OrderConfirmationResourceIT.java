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
import ch.united.fastadmin.domain.OrderConfirmation;
import ch.united.fastadmin.domain.Signature;
import ch.united.fastadmin.domain.enumeration.Currency;
import ch.united.fastadmin.domain.enumeration.DiscountType;
import ch.united.fastadmin.domain.enumeration.DocumentLanguage;
import ch.united.fastadmin.domain.enumeration.OrderConfirmationStatus;
import ch.united.fastadmin.repository.OrderConfirmationRepository;
import ch.united.fastadmin.service.criteria.OrderConfirmationCriteria;
import ch.united.fastadmin.service.dto.OrderConfirmationDTO;
import ch.united.fastadmin.service.mapper.OrderConfirmationMapper;
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
 * Integration tests for the {@link OrderConfirmationResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class OrderConfirmationResourceIT {

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

    private static final OrderConfirmationStatus DEFAULT_STATUS = OrderConfirmationStatus.DRAFT;
    private static final OrderConfirmationStatus UPDATED_STATUS = OrderConfirmationStatus.SENT;

    private static final ZonedDateTime DEFAULT_CREATED = ZonedDateTime.ofInstant(Instant.ofEpochMilli(0L), ZoneOffset.UTC);
    private static final ZonedDateTime UPDATED_CREATED = ZonedDateTime.now(ZoneId.systemDefault()).withNano(0);
    private static final ZonedDateTime SMALLER_CREATED = ZonedDateTime.ofInstant(Instant.ofEpochMilli(-1L), ZoneOffset.UTC);

    private static final String ENTITY_API_URL = "/api/order-confirmations";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private OrderConfirmationRepository orderConfirmationRepository;

    @Autowired
    private OrderConfirmationMapper orderConfirmationMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restOrderConfirmationMockMvc;

    private OrderConfirmation orderConfirmation;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static OrderConfirmation createEntity(EntityManager em) {
        OrderConfirmation orderConfirmation = new OrderConfirmation()
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
        return orderConfirmation;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static OrderConfirmation createUpdatedEntity(EntityManager em) {
        OrderConfirmation orderConfirmation = new OrderConfirmation()
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
        return orderConfirmation;
    }

    @BeforeEach
    public void initTest() {
        orderConfirmation = createEntity(em);
    }

    @Test
    @Transactional
    void createOrderConfirmation() throws Exception {
        int databaseSizeBeforeCreate = orderConfirmationRepository.findAll().size();
        // Create the OrderConfirmation
        OrderConfirmationDTO orderConfirmationDTO = orderConfirmationMapper.toDto(orderConfirmation);
        restOrderConfirmationMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(orderConfirmationDTO))
            )
            .andExpect(status().isCreated());

        // Validate the OrderConfirmation in the database
        List<OrderConfirmation> orderConfirmationList = orderConfirmationRepository.findAll();
        assertThat(orderConfirmationList).hasSize(databaseSizeBeforeCreate + 1);
        OrderConfirmation testOrderConfirmation = orderConfirmationList.get(orderConfirmationList.size() - 1);
        assertThat(testOrderConfirmation.getRemoteId()).isEqualTo(DEFAULT_REMOTE_ID);
        assertThat(testOrderConfirmation.getNumber()).isEqualTo(DEFAULT_NUMBER);
        assertThat(testOrderConfirmation.getContactName()).isEqualTo(DEFAULT_CONTACT_NAME);
        assertThat(testOrderConfirmation.getDate()).isEqualTo(DEFAULT_DATE);
        assertThat(testOrderConfirmation.getPeriodText()).isEqualTo(DEFAULT_PERIOD_TEXT);
        assertThat(testOrderConfirmation.getCurrency()).isEqualTo(DEFAULT_CURRENCY);
        assertThat(testOrderConfirmation.getTotal()).isEqualTo(DEFAULT_TOTAL);
        assertThat(testOrderConfirmation.getVatIncluded()).isEqualTo(DEFAULT_VAT_INCLUDED);
        assertThat(testOrderConfirmation.getDiscountRate()).isEqualTo(DEFAULT_DISCOUNT_RATE);
        assertThat(testOrderConfirmation.getDiscountType()).isEqualTo(DEFAULT_DISCOUNT_TYPE);
        assertThat(testOrderConfirmation.getLanguage()).isEqualTo(DEFAULT_LANGUAGE);
        assertThat(testOrderConfirmation.getPageAmount()).isEqualTo(DEFAULT_PAGE_AMOUNT);
        assertThat(testOrderConfirmation.getNotes()).isEqualTo(DEFAULT_NOTES);
        assertThat(testOrderConfirmation.getStatus()).isEqualTo(DEFAULT_STATUS);
        assertThat(testOrderConfirmation.getCreated()).isEqualTo(DEFAULT_CREATED);
    }

    @Test
    @Transactional
    void createOrderConfirmationWithExistingId() throws Exception {
        // Create the OrderConfirmation with an existing ID
        orderConfirmation.setId(1L);
        OrderConfirmationDTO orderConfirmationDTO = orderConfirmationMapper.toDto(orderConfirmation);

        int databaseSizeBeforeCreate = orderConfirmationRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restOrderConfirmationMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(orderConfirmationDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the OrderConfirmation in the database
        List<OrderConfirmation> orderConfirmationList = orderConfirmationRepository.findAll();
        assertThat(orderConfirmationList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllOrderConfirmations() throws Exception {
        // Initialize the database
        orderConfirmationRepository.saveAndFlush(orderConfirmation);

        // Get all the orderConfirmationList
        restOrderConfirmationMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(orderConfirmation.getId().intValue())))
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
    void getOrderConfirmation() throws Exception {
        // Initialize the database
        orderConfirmationRepository.saveAndFlush(orderConfirmation);

        // Get the orderConfirmation
        restOrderConfirmationMockMvc
            .perform(get(ENTITY_API_URL_ID, orderConfirmation.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(orderConfirmation.getId().intValue()))
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
    void getOrderConfirmationsByIdFiltering() throws Exception {
        // Initialize the database
        orderConfirmationRepository.saveAndFlush(orderConfirmation);

        Long id = orderConfirmation.getId();

        defaultOrderConfirmationShouldBeFound("id.equals=" + id);
        defaultOrderConfirmationShouldNotBeFound("id.notEquals=" + id);

        defaultOrderConfirmationShouldBeFound("id.greaterThanOrEqual=" + id);
        defaultOrderConfirmationShouldNotBeFound("id.greaterThan=" + id);

        defaultOrderConfirmationShouldBeFound("id.lessThanOrEqual=" + id);
        defaultOrderConfirmationShouldNotBeFound("id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllOrderConfirmationsByRemoteIdIsEqualToSomething() throws Exception {
        // Initialize the database
        orderConfirmationRepository.saveAndFlush(orderConfirmation);

        // Get all the orderConfirmationList where remoteId equals to DEFAULT_REMOTE_ID
        defaultOrderConfirmationShouldBeFound("remoteId.equals=" + DEFAULT_REMOTE_ID);

        // Get all the orderConfirmationList where remoteId equals to UPDATED_REMOTE_ID
        defaultOrderConfirmationShouldNotBeFound("remoteId.equals=" + UPDATED_REMOTE_ID);
    }

    @Test
    @Transactional
    void getAllOrderConfirmationsByRemoteIdIsNotEqualToSomething() throws Exception {
        // Initialize the database
        orderConfirmationRepository.saveAndFlush(orderConfirmation);

        // Get all the orderConfirmationList where remoteId not equals to DEFAULT_REMOTE_ID
        defaultOrderConfirmationShouldNotBeFound("remoteId.notEquals=" + DEFAULT_REMOTE_ID);

        // Get all the orderConfirmationList where remoteId not equals to UPDATED_REMOTE_ID
        defaultOrderConfirmationShouldBeFound("remoteId.notEquals=" + UPDATED_REMOTE_ID);
    }

    @Test
    @Transactional
    void getAllOrderConfirmationsByRemoteIdIsInShouldWork() throws Exception {
        // Initialize the database
        orderConfirmationRepository.saveAndFlush(orderConfirmation);

        // Get all the orderConfirmationList where remoteId in DEFAULT_REMOTE_ID or UPDATED_REMOTE_ID
        defaultOrderConfirmationShouldBeFound("remoteId.in=" + DEFAULT_REMOTE_ID + "," + UPDATED_REMOTE_ID);

        // Get all the orderConfirmationList where remoteId equals to UPDATED_REMOTE_ID
        defaultOrderConfirmationShouldNotBeFound("remoteId.in=" + UPDATED_REMOTE_ID);
    }

    @Test
    @Transactional
    void getAllOrderConfirmationsByRemoteIdIsNullOrNotNull() throws Exception {
        // Initialize the database
        orderConfirmationRepository.saveAndFlush(orderConfirmation);

        // Get all the orderConfirmationList where remoteId is not null
        defaultOrderConfirmationShouldBeFound("remoteId.specified=true");

        // Get all the orderConfirmationList where remoteId is null
        defaultOrderConfirmationShouldNotBeFound("remoteId.specified=false");
    }

    @Test
    @Transactional
    void getAllOrderConfirmationsByRemoteIdIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        orderConfirmationRepository.saveAndFlush(orderConfirmation);

        // Get all the orderConfirmationList where remoteId is greater than or equal to DEFAULT_REMOTE_ID
        defaultOrderConfirmationShouldBeFound("remoteId.greaterThanOrEqual=" + DEFAULT_REMOTE_ID);

        // Get all the orderConfirmationList where remoteId is greater than or equal to UPDATED_REMOTE_ID
        defaultOrderConfirmationShouldNotBeFound("remoteId.greaterThanOrEqual=" + UPDATED_REMOTE_ID);
    }

    @Test
    @Transactional
    void getAllOrderConfirmationsByRemoteIdIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        orderConfirmationRepository.saveAndFlush(orderConfirmation);

        // Get all the orderConfirmationList where remoteId is less than or equal to DEFAULT_REMOTE_ID
        defaultOrderConfirmationShouldBeFound("remoteId.lessThanOrEqual=" + DEFAULT_REMOTE_ID);

        // Get all the orderConfirmationList where remoteId is less than or equal to SMALLER_REMOTE_ID
        defaultOrderConfirmationShouldNotBeFound("remoteId.lessThanOrEqual=" + SMALLER_REMOTE_ID);
    }

    @Test
    @Transactional
    void getAllOrderConfirmationsByRemoteIdIsLessThanSomething() throws Exception {
        // Initialize the database
        orderConfirmationRepository.saveAndFlush(orderConfirmation);

        // Get all the orderConfirmationList where remoteId is less than DEFAULT_REMOTE_ID
        defaultOrderConfirmationShouldNotBeFound("remoteId.lessThan=" + DEFAULT_REMOTE_ID);

        // Get all the orderConfirmationList where remoteId is less than UPDATED_REMOTE_ID
        defaultOrderConfirmationShouldBeFound("remoteId.lessThan=" + UPDATED_REMOTE_ID);
    }

    @Test
    @Transactional
    void getAllOrderConfirmationsByRemoteIdIsGreaterThanSomething() throws Exception {
        // Initialize the database
        orderConfirmationRepository.saveAndFlush(orderConfirmation);

        // Get all the orderConfirmationList where remoteId is greater than DEFAULT_REMOTE_ID
        defaultOrderConfirmationShouldNotBeFound("remoteId.greaterThan=" + DEFAULT_REMOTE_ID);

        // Get all the orderConfirmationList where remoteId is greater than SMALLER_REMOTE_ID
        defaultOrderConfirmationShouldBeFound("remoteId.greaterThan=" + SMALLER_REMOTE_ID);
    }

    @Test
    @Transactional
    void getAllOrderConfirmationsByNumberIsEqualToSomething() throws Exception {
        // Initialize the database
        orderConfirmationRepository.saveAndFlush(orderConfirmation);

        // Get all the orderConfirmationList where number equals to DEFAULT_NUMBER
        defaultOrderConfirmationShouldBeFound("number.equals=" + DEFAULT_NUMBER);

        // Get all the orderConfirmationList where number equals to UPDATED_NUMBER
        defaultOrderConfirmationShouldNotBeFound("number.equals=" + UPDATED_NUMBER);
    }

    @Test
    @Transactional
    void getAllOrderConfirmationsByNumberIsNotEqualToSomething() throws Exception {
        // Initialize the database
        orderConfirmationRepository.saveAndFlush(orderConfirmation);

        // Get all the orderConfirmationList where number not equals to DEFAULT_NUMBER
        defaultOrderConfirmationShouldNotBeFound("number.notEquals=" + DEFAULT_NUMBER);

        // Get all the orderConfirmationList where number not equals to UPDATED_NUMBER
        defaultOrderConfirmationShouldBeFound("number.notEquals=" + UPDATED_NUMBER);
    }

    @Test
    @Transactional
    void getAllOrderConfirmationsByNumberIsInShouldWork() throws Exception {
        // Initialize the database
        orderConfirmationRepository.saveAndFlush(orderConfirmation);

        // Get all the orderConfirmationList where number in DEFAULT_NUMBER or UPDATED_NUMBER
        defaultOrderConfirmationShouldBeFound("number.in=" + DEFAULT_NUMBER + "," + UPDATED_NUMBER);

        // Get all the orderConfirmationList where number equals to UPDATED_NUMBER
        defaultOrderConfirmationShouldNotBeFound("number.in=" + UPDATED_NUMBER);
    }

    @Test
    @Transactional
    void getAllOrderConfirmationsByNumberIsNullOrNotNull() throws Exception {
        // Initialize the database
        orderConfirmationRepository.saveAndFlush(orderConfirmation);

        // Get all the orderConfirmationList where number is not null
        defaultOrderConfirmationShouldBeFound("number.specified=true");

        // Get all the orderConfirmationList where number is null
        defaultOrderConfirmationShouldNotBeFound("number.specified=false");
    }

    @Test
    @Transactional
    void getAllOrderConfirmationsByNumberContainsSomething() throws Exception {
        // Initialize the database
        orderConfirmationRepository.saveAndFlush(orderConfirmation);

        // Get all the orderConfirmationList where number contains DEFAULT_NUMBER
        defaultOrderConfirmationShouldBeFound("number.contains=" + DEFAULT_NUMBER);

        // Get all the orderConfirmationList where number contains UPDATED_NUMBER
        defaultOrderConfirmationShouldNotBeFound("number.contains=" + UPDATED_NUMBER);
    }

    @Test
    @Transactional
    void getAllOrderConfirmationsByNumberNotContainsSomething() throws Exception {
        // Initialize the database
        orderConfirmationRepository.saveAndFlush(orderConfirmation);

        // Get all the orderConfirmationList where number does not contain DEFAULT_NUMBER
        defaultOrderConfirmationShouldNotBeFound("number.doesNotContain=" + DEFAULT_NUMBER);

        // Get all the orderConfirmationList where number does not contain UPDATED_NUMBER
        defaultOrderConfirmationShouldBeFound("number.doesNotContain=" + UPDATED_NUMBER);
    }

    @Test
    @Transactional
    void getAllOrderConfirmationsByContactNameIsEqualToSomething() throws Exception {
        // Initialize the database
        orderConfirmationRepository.saveAndFlush(orderConfirmation);

        // Get all the orderConfirmationList where contactName equals to DEFAULT_CONTACT_NAME
        defaultOrderConfirmationShouldBeFound("contactName.equals=" + DEFAULT_CONTACT_NAME);

        // Get all the orderConfirmationList where contactName equals to UPDATED_CONTACT_NAME
        defaultOrderConfirmationShouldNotBeFound("contactName.equals=" + UPDATED_CONTACT_NAME);
    }

    @Test
    @Transactional
    void getAllOrderConfirmationsByContactNameIsNotEqualToSomething() throws Exception {
        // Initialize the database
        orderConfirmationRepository.saveAndFlush(orderConfirmation);

        // Get all the orderConfirmationList where contactName not equals to DEFAULT_CONTACT_NAME
        defaultOrderConfirmationShouldNotBeFound("contactName.notEquals=" + DEFAULT_CONTACT_NAME);

        // Get all the orderConfirmationList where contactName not equals to UPDATED_CONTACT_NAME
        defaultOrderConfirmationShouldBeFound("contactName.notEquals=" + UPDATED_CONTACT_NAME);
    }

    @Test
    @Transactional
    void getAllOrderConfirmationsByContactNameIsInShouldWork() throws Exception {
        // Initialize the database
        orderConfirmationRepository.saveAndFlush(orderConfirmation);

        // Get all the orderConfirmationList where contactName in DEFAULT_CONTACT_NAME or UPDATED_CONTACT_NAME
        defaultOrderConfirmationShouldBeFound("contactName.in=" + DEFAULT_CONTACT_NAME + "," + UPDATED_CONTACT_NAME);

        // Get all the orderConfirmationList where contactName equals to UPDATED_CONTACT_NAME
        defaultOrderConfirmationShouldNotBeFound("contactName.in=" + UPDATED_CONTACT_NAME);
    }

    @Test
    @Transactional
    void getAllOrderConfirmationsByContactNameIsNullOrNotNull() throws Exception {
        // Initialize the database
        orderConfirmationRepository.saveAndFlush(orderConfirmation);

        // Get all the orderConfirmationList where contactName is not null
        defaultOrderConfirmationShouldBeFound("contactName.specified=true");

        // Get all the orderConfirmationList where contactName is null
        defaultOrderConfirmationShouldNotBeFound("contactName.specified=false");
    }

    @Test
    @Transactional
    void getAllOrderConfirmationsByContactNameContainsSomething() throws Exception {
        // Initialize the database
        orderConfirmationRepository.saveAndFlush(orderConfirmation);

        // Get all the orderConfirmationList where contactName contains DEFAULT_CONTACT_NAME
        defaultOrderConfirmationShouldBeFound("contactName.contains=" + DEFAULT_CONTACT_NAME);

        // Get all the orderConfirmationList where contactName contains UPDATED_CONTACT_NAME
        defaultOrderConfirmationShouldNotBeFound("contactName.contains=" + UPDATED_CONTACT_NAME);
    }

    @Test
    @Transactional
    void getAllOrderConfirmationsByContactNameNotContainsSomething() throws Exception {
        // Initialize the database
        orderConfirmationRepository.saveAndFlush(orderConfirmation);

        // Get all the orderConfirmationList where contactName does not contain DEFAULT_CONTACT_NAME
        defaultOrderConfirmationShouldNotBeFound("contactName.doesNotContain=" + DEFAULT_CONTACT_NAME);

        // Get all the orderConfirmationList where contactName does not contain UPDATED_CONTACT_NAME
        defaultOrderConfirmationShouldBeFound("contactName.doesNotContain=" + UPDATED_CONTACT_NAME);
    }

    @Test
    @Transactional
    void getAllOrderConfirmationsByDateIsEqualToSomething() throws Exception {
        // Initialize the database
        orderConfirmationRepository.saveAndFlush(orderConfirmation);

        // Get all the orderConfirmationList where date equals to DEFAULT_DATE
        defaultOrderConfirmationShouldBeFound("date.equals=" + DEFAULT_DATE);

        // Get all the orderConfirmationList where date equals to UPDATED_DATE
        defaultOrderConfirmationShouldNotBeFound("date.equals=" + UPDATED_DATE);
    }

    @Test
    @Transactional
    void getAllOrderConfirmationsByDateIsNotEqualToSomething() throws Exception {
        // Initialize the database
        orderConfirmationRepository.saveAndFlush(orderConfirmation);

        // Get all the orderConfirmationList where date not equals to DEFAULT_DATE
        defaultOrderConfirmationShouldNotBeFound("date.notEquals=" + DEFAULT_DATE);

        // Get all the orderConfirmationList where date not equals to UPDATED_DATE
        defaultOrderConfirmationShouldBeFound("date.notEquals=" + UPDATED_DATE);
    }

    @Test
    @Transactional
    void getAllOrderConfirmationsByDateIsInShouldWork() throws Exception {
        // Initialize the database
        orderConfirmationRepository.saveAndFlush(orderConfirmation);

        // Get all the orderConfirmationList where date in DEFAULT_DATE or UPDATED_DATE
        defaultOrderConfirmationShouldBeFound("date.in=" + DEFAULT_DATE + "," + UPDATED_DATE);

        // Get all the orderConfirmationList where date equals to UPDATED_DATE
        defaultOrderConfirmationShouldNotBeFound("date.in=" + UPDATED_DATE);
    }

    @Test
    @Transactional
    void getAllOrderConfirmationsByDateIsNullOrNotNull() throws Exception {
        // Initialize the database
        orderConfirmationRepository.saveAndFlush(orderConfirmation);

        // Get all the orderConfirmationList where date is not null
        defaultOrderConfirmationShouldBeFound("date.specified=true");

        // Get all the orderConfirmationList where date is null
        defaultOrderConfirmationShouldNotBeFound("date.specified=false");
    }

    @Test
    @Transactional
    void getAllOrderConfirmationsByDateIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        orderConfirmationRepository.saveAndFlush(orderConfirmation);

        // Get all the orderConfirmationList where date is greater than or equal to DEFAULT_DATE
        defaultOrderConfirmationShouldBeFound("date.greaterThanOrEqual=" + DEFAULT_DATE);

        // Get all the orderConfirmationList where date is greater than or equal to UPDATED_DATE
        defaultOrderConfirmationShouldNotBeFound("date.greaterThanOrEqual=" + UPDATED_DATE);
    }

    @Test
    @Transactional
    void getAllOrderConfirmationsByDateIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        orderConfirmationRepository.saveAndFlush(orderConfirmation);

        // Get all the orderConfirmationList where date is less than or equal to DEFAULT_DATE
        defaultOrderConfirmationShouldBeFound("date.lessThanOrEqual=" + DEFAULT_DATE);

        // Get all the orderConfirmationList where date is less than or equal to SMALLER_DATE
        defaultOrderConfirmationShouldNotBeFound("date.lessThanOrEqual=" + SMALLER_DATE);
    }

    @Test
    @Transactional
    void getAllOrderConfirmationsByDateIsLessThanSomething() throws Exception {
        // Initialize the database
        orderConfirmationRepository.saveAndFlush(orderConfirmation);

        // Get all the orderConfirmationList where date is less than DEFAULT_DATE
        defaultOrderConfirmationShouldNotBeFound("date.lessThan=" + DEFAULT_DATE);

        // Get all the orderConfirmationList where date is less than UPDATED_DATE
        defaultOrderConfirmationShouldBeFound("date.lessThan=" + UPDATED_DATE);
    }

    @Test
    @Transactional
    void getAllOrderConfirmationsByDateIsGreaterThanSomething() throws Exception {
        // Initialize the database
        orderConfirmationRepository.saveAndFlush(orderConfirmation);

        // Get all the orderConfirmationList where date is greater than DEFAULT_DATE
        defaultOrderConfirmationShouldNotBeFound("date.greaterThan=" + DEFAULT_DATE);

        // Get all the orderConfirmationList where date is greater than SMALLER_DATE
        defaultOrderConfirmationShouldBeFound("date.greaterThan=" + SMALLER_DATE);
    }

    @Test
    @Transactional
    void getAllOrderConfirmationsByPeriodTextIsEqualToSomething() throws Exception {
        // Initialize the database
        orderConfirmationRepository.saveAndFlush(orderConfirmation);

        // Get all the orderConfirmationList where periodText equals to DEFAULT_PERIOD_TEXT
        defaultOrderConfirmationShouldBeFound("periodText.equals=" + DEFAULT_PERIOD_TEXT);

        // Get all the orderConfirmationList where periodText equals to UPDATED_PERIOD_TEXT
        defaultOrderConfirmationShouldNotBeFound("periodText.equals=" + UPDATED_PERIOD_TEXT);
    }

    @Test
    @Transactional
    void getAllOrderConfirmationsByPeriodTextIsNotEqualToSomething() throws Exception {
        // Initialize the database
        orderConfirmationRepository.saveAndFlush(orderConfirmation);

        // Get all the orderConfirmationList where periodText not equals to DEFAULT_PERIOD_TEXT
        defaultOrderConfirmationShouldNotBeFound("periodText.notEquals=" + DEFAULT_PERIOD_TEXT);

        // Get all the orderConfirmationList where periodText not equals to UPDATED_PERIOD_TEXT
        defaultOrderConfirmationShouldBeFound("periodText.notEquals=" + UPDATED_PERIOD_TEXT);
    }

    @Test
    @Transactional
    void getAllOrderConfirmationsByPeriodTextIsInShouldWork() throws Exception {
        // Initialize the database
        orderConfirmationRepository.saveAndFlush(orderConfirmation);

        // Get all the orderConfirmationList where periodText in DEFAULT_PERIOD_TEXT or UPDATED_PERIOD_TEXT
        defaultOrderConfirmationShouldBeFound("periodText.in=" + DEFAULT_PERIOD_TEXT + "," + UPDATED_PERIOD_TEXT);

        // Get all the orderConfirmationList where periodText equals to UPDATED_PERIOD_TEXT
        defaultOrderConfirmationShouldNotBeFound("periodText.in=" + UPDATED_PERIOD_TEXT);
    }

    @Test
    @Transactional
    void getAllOrderConfirmationsByPeriodTextIsNullOrNotNull() throws Exception {
        // Initialize the database
        orderConfirmationRepository.saveAndFlush(orderConfirmation);

        // Get all the orderConfirmationList where periodText is not null
        defaultOrderConfirmationShouldBeFound("periodText.specified=true");

        // Get all the orderConfirmationList where periodText is null
        defaultOrderConfirmationShouldNotBeFound("periodText.specified=false");
    }

    @Test
    @Transactional
    void getAllOrderConfirmationsByPeriodTextContainsSomething() throws Exception {
        // Initialize the database
        orderConfirmationRepository.saveAndFlush(orderConfirmation);

        // Get all the orderConfirmationList where periodText contains DEFAULT_PERIOD_TEXT
        defaultOrderConfirmationShouldBeFound("periodText.contains=" + DEFAULT_PERIOD_TEXT);

        // Get all the orderConfirmationList where periodText contains UPDATED_PERIOD_TEXT
        defaultOrderConfirmationShouldNotBeFound("periodText.contains=" + UPDATED_PERIOD_TEXT);
    }

    @Test
    @Transactional
    void getAllOrderConfirmationsByPeriodTextNotContainsSomething() throws Exception {
        // Initialize the database
        orderConfirmationRepository.saveAndFlush(orderConfirmation);

        // Get all the orderConfirmationList where periodText does not contain DEFAULT_PERIOD_TEXT
        defaultOrderConfirmationShouldNotBeFound("periodText.doesNotContain=" + DEFAULT_PERIOD_TEXT);

        // Get all the orderConfirmationList where periodText does not contain UPDATED_PERIOD_TEXT
        defaultOrderConfirmationShouldBeFound("periodText.doesNotContain=" + UPDATED_PERIOD_TEXT);
    }

    @Test
    @Transactional
    void getAllOrderConfirmationsByCurrencyIsEqualToSomething() throws Exception {
        // Initialize the database
        orderConfirmationRepository.saveAndFlush(orderConfirmation);

        // Get all the orderConfirmationList where currency equals to DEFAULT_CURRENCY
        defaultOrderConfirmationShouldBeFound("currency.equals=" + DEFAULT_CURRENCY);

        // Get all the orderConfirmationList where currency equals to UPDATED_CURRENCY
        defaultOrderConfirmationShouldNotBeFound("currency.equals=" + UPDATED_CURRENCY);
    }

    @Test
    @Transactional
    void getAllOrderConfirmationsByCurrencyIsNotEqualToSomething() throws Exception {
        // Initialize the database
        orderConfirmationRepository.saveAndFlush(orderConfirmation);

        // Get all the orderConfirmationList where currency not equals to DEFAULT_CURRENCY
        defaultOrderConfirmationShouldNotBeFound("currency.notEquals=" + DEFAULT_CURRENCY);

        // Get all the orderConfirmationList where currency not equals to UPDATED_CURRENCY
        defaultOrderConfirmationShouldBeFound("currency.notEquals=" + UPDATED_CURRENCY);
    }

    @Test
    @Transactional
    void getAllOrderConfirmationsByCurrencyIsInShouldWork() throws Exception {
        // Initialize the database
        orderConfirmationRepository.saveAndFlush(orderConfirmation);

        // Get all the orderConfirmationList where currency in DEFAULT_CURRENCY or UPDATED_CURRENCY
        defaultOrderConfirmationShouldBeFound("currency.in=" + DEFAULT_CURRENCY + "," + UPDATED_CURRENCY);

        // Get all the orderConfirmationList where currency equals to UPDATED_CURRENCY
        defaultOrderConfirmationShouldNotBeFound("currency.in=" + UPDATED_CURRENCY);
    }

    @Test
    @Transactional
    void getAllOrderConfirmationsByCurrencyIsNullOrNotNull() throws Exception {
        // Initialize the database
        orderConfirmationRepository.saveAndFlush(orderConfirmation);

        // Get all the orderConfirmationList where currency is not null
        defaultOrderConfirmationShouldBeFound("currency.specified=true");

        // Get all the orderConfirmationList where currency is null
        defaultOrderConfirmationShouldNotBeFound("currency.specified=false");
    }

    @Test
    @Transactional
    void getAllOrderConfirmationsByTotalIsEqualToSomething() throws Exception {
        // Initialize the database
        orderConfirmationRepository.saveAndFlush(orderConfirmation);

        // Get all the orderConfirmationList where total equals to DEFAULT_TOTAL
        defaultOrderConfirmationShouldBeFound("total.equals=" + DEFAULT_TOTAL);

        // Get all the orderConfirmationList where total equals to UPDATED_TOTAL
        defaultOrderConfirmationShouldNotBeFound("total.equals=" + UPDATED_TOTAL);
    }

    @Test
    @Transactional
    void getAllOrderConfirmationsByTotalIsNotEqualToSomething() throws Exception {
        // Initialize the database
        orderConfirmationRepository.saveAndFlush(orderConfirmation);

        // Get all the orderConfirmationList where total not equals to DEFAULT_TOTAL
        defaultOrderConfirmationShouldNotBeFound("total.notEquals=" + DEFAULT_TOTAL);

        // Get all the orderConfirmationList where total not equals to UPDATED_TOTAL
        defaultOrderConfirmationShouldBeFound("total.notEquals=" + UPDATED_TOTAL);
    }

    @Test
    @Transactional
    void getAllOrderConfirmationsByTotalIsInShouldWork() throws Exception {
        // Initialize the database
        orderConfirmationRepository.saveAndFlush(orderConfirmation);

        // Get all the orderConfirmationList where total in DEFAULT_TOTAL or UPDATED_TOTAL
        defaultOrderConfirmationShouldBeFound("total.in=" + DEFAULT_TOTAL + "," + UPDATED_TOTAL);

        // Get all the orderConfirmationList where total equals to UPDATED_TOTAL
        defaultOrderConfirmationShouldNotBeFound("total.in=" + UPDATED_TOTAL);
    }

    @Test
    @Transactional
    void getAllOrderConfirmationsByTotalIsNullOrNotNull() throws Exception {
        // Initialize the database
        orderConfirmationRepository.saveAndFlush(orderConfirmation);

        // Get all the orderConfirmationList where total is not null
        defaultOrderConfirmationShouldBeFound("total.specified=true");

        // Get all the orderConfirmationList where total is null
        defaultOrderConfirmationShouldNotBeFound("total.specified=false");
    }

    @Test
    @Transactional
    void getAllOrderConfirmationsByTotalIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        orderConfirmationRepository.saveAndFlush(orderConfirmation);

        // Get all the orderConfirmationList where total is greater than or equal to DEFAULT_TOTAL
        defaultOrderConfirmationShouldBeFound("total.greaterThanOrEqual=" + DEFAULT_TOTAL);

        // Get all the orderConfirmationList where total is greater than or equal to UPDATED_TOTAL
        defaultOrderConfirmationShouldNotBeFound("total.greaterThanOrEqual=" + UPDATED_TOTAL);
    }

    @Test
    @Transactional
    void getAllOrderConfirmationsByTotalIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        orderConfirmationRepository.saveAndFlush(orderConfirmation);

        // Get all the orderConfirmationList where total is less than or equal to DEFAULT_TOTAL
        defaultOrderConfirmationShouldBeFound("total.lessThanOrEqual=" + DEFAULT_TOTAL);

        // Get all the orderConfirmationList where total is less than or equal to SMALLER_TOTAL
        defaultOrderConfirmationShouldNotBeFound("total.lessThanOrEqual=" + SMALLER_TOTAL);
    }

    @Test
    @Transactional
    void getAllOrderConfirmationsByTotalIsLessThanSomething() throws Exception {
        // Initialize the database
        orderConfirmationRepository.saveAndFlush(orderConfirmation);

        // Get all the orderConfirmationList where total is less than DEFAULT_TOTAL
        defaultOrderConfirmationShouldNotBeFound("total.lessThan=" + DEFAULT_TOTAL);

        // Get all the orderConfirmationList where total is less than UPDATED_TOTAL
        defaultOrderConfirmationShouldBeFound("total.lessThan=" + UPDATED_TOTAL);
    }

    @Test
    @Transactional
    void getAllOrderConfirmationsByTotalIsGreaterThanSomething() throws Exception {
        // Initialize the database
        orderConfirmationRepository.saveAndFlush(orderConfirmation);

        // Get all the orderConfirmationList where total is greater than DEFAULT_TOTAL
        defaultOrderConfirmationShouldNotBeFound("total.greaterThan=" + DEFAULT_TOTAL);

        // Get all the orderConfirmationList where total is greater than SMALLER_TOTAL
        defaultOrderConfirmationShouldBeFound("total.greaterThan=" + SMALLER_TOTAL);
    }

    @Test
    @Transactional
    void getAllOrderConfirmationsByVatIncludedIsEqualToSomething() throws Exception {
        // Initialize the database
        orderConfirmationRepository.saveAndFlush(orderConfirmation);

        // Get all the orderConfirmationList where vatIncluded equals to DEFAULT_VAT_INCLUDED
        defaultOrderConfirmationShouldBeFound("vatIncluded.equals=" + DEFAULT_VAT_INCLUDED);

        // Get all the orderConfirmationList where vatIncluded equals to UPDATED_VAT_INCLUDED
        defaultOrderConfirmationShouldNotBeFound("vatIncluded.equals=" + UPDATED_VAT_INCLUDED);
    }

    @Test
    @Transactional
    void getAllOrderConfirmationsByVatIncludedIsNotEqualToSomething() throws Exception {
        // Initialize the database
        orderConfirmationRepository.saveAndFlush(orderConfirmation);

        // Get all the orderConfirmationList where vatIncluded not equals to DEFAULT_VAT_INCLUDED
        defaultOrderConfirmationShouldNotBeFound("vatIncluded.notEquals=" + DEFAULT_VAT_INCLUDED);

        // Get all the orderConfirmationList where vatIncluded not equals to UPDATED_VAT_INCLUDED
        defaultOrderConfirmationShouldBeFound("vatIncluded.notEquals=" + UPDATED_VAT_INCLUDED);
    }

    @Test
    @Transactional
    void getAllOrderConfirmationsByVatIncludedIsInShouldWork() throws Exception {
        // Initialize the database
        orderConfirmationRepository.saveAndFlush(orderConfirmation);

        // Get all the orderConfirmationList where vatIncluded in DEFAULT_VAT_INCLUDED or UPDATED_VAT_INCLUDED
        defaultOrderConfirmationShouldBeFound("vatIncluded.in=" + DEFAULT_VAT_INCLUDED + "," + UPDATED_VAT_INCLUDED);

        // Get all the orderConfirmationList where vatIncluded equals to UPDATED_VAT_INCLUDED
        defaultOrderConfirmationShouldNotBeFound("vatIncluded.in=" + UPDATED_VAT_INCLUDED);
    }

    @Test
    @Transactional
    void getAllOrderConfirmationsByVatIncludedIsNullOrNotNull() throws Exception {
        // Initialize the database
        orderConfirmationRepository.saveAndFlush(orderConfirmation);

        // Get all the orderConfirmationList where vatIncluded is not null
        defaultOrderConfirmationShouldBeFound("vatIncluded.specified=true");

        // Get all the orderConfirmationList where vatIncluded is null
        defaultOrderConfirmationShouldNotBeFound("vatIncluded.specified=false");
    }

    @Test
    @Transactional
    void getAllOrderConfirmationsByDiscountRateIsEqualToSomething() throws Exception {
        // Initialize the database
        orderConfirmationRepository.saveAndFlush(orderConfirmation);

        // Get all the orderConfirmationList where discountRate equals to DEFAULT_DISCOUNT_RATE
        defaultOrderConfirmationShouldBeFound("discountRate.equals=" + DEFAULT_DISCOUNT_RATE);

        // Get all the orderConfirmationList where discountRate equals to UPDATED_DISCOUNT_RATE
        defaultOrderConfirmationShouldNotBeFound("discountRate.equals=" + UPDATED_DISCOUNT_RATE);
    }

    @Test
    @Transactional
    void getAllOrderConfirmationsByDiscountRateIsNotEqualToSomething() throws Exception {
        // Initialize the database
        orderConfirmationRepository.saveAndFlush(orderConfirmation);

        // Get all the orderConfirmationList where discountRate not equals to DEFAULT_DISCOUNT_RATE
        defaultOrderConfirmationShouldNotBeFound("discountRate.notEquals=" + DEFAULT_DISCOUNT_RATE);

        // Get all the orderConfirmationList where discountRate not equals to UPDATED_DISCOUNT_RATE
        defaultOrderConfirmationShouldBeFound("discountRate.notEquals=" + UPDATED_DISCOUNT_RATE);
    }

    @Test
    @Transactional
    void getAllOrderConfirmationsByDiscountRateIsInShouldWork() throws Exception {
        // Initialize the database
        orderConfirmationRepository.saveAndFlush(orderConfirmation);

        // Get all the orderConfirmationList where discountRate in DEFAULT_DISCOUNT_RATE or UPDATED_DISCOUNT_RATE
        defaultOrderConfirmationShouldBeFound("discountRate.in=" + DEFAULT_DISCOUNT_RATE + "," + UPDATED_DISCOUNT_RATE);

        // Get all the orderConfirmationList where discountRate equals to UPDATED_DISCOUNT_RATE
        defaultOrderConfirmationShouldNotBeFound("discountRate.in=" + UPDATED_DISCOUNT_RATE);
    }

    @Test
    @Transactional
    void getAllOrderConfirmationsByDiscountRateIsNullOrNotNull() throws Exception {
        // Initialize the database
        orderConfirmationRepository.saveAndFlush(orderConfirmation);

        // Get all the orderConfirmationList where discountRate is not null
        defaultOrderConfirmationShouldBeFound("discountRate.specified=true");

        // Get all the orderConfirmationList where discountRate is null
        defaultOrderConfirmationShouldNotBeFound("discountRate.specified=false");
    }

    @Test
    @Transactional
    void getAllOrderConfirmationsByDiscountRateIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        orderConfirmationRepository.saveAndFlush(orderConfirmation);

        // Get all the orderConfirmationList where discountRate is greater than or equal to DEFAULT_DISCOUNT_RATE
        defaultOrderConfirmationShouldBeFound("discountRate.greaterThanOrEqual=" + DEFAULT_DISCOUNT_RATE);

        // Get all the orderConfirmationList where discountRate is greater than or equal to UPDATED_DISCOUNT_RATE
        defaultOrderConfirmationShouldNotBeFound("discountRate.greaterThanOrEqual=" + UPDATED_DISCOUNT_RATE);
    }

    @Test
    @Transactional
    void getAllOrderConfirmationsByDiscountRateIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        orderConfirmationRepository.saveAndFlush(orderConfirmation);

        // Get all the orderConfirmationList where discountRate is less than or equal to DEFAULT_DISCOUNT_RATE
        defaultOrderConfirmationShouldBeFound("discountRate.lessThanOrEqual=" + DEFAULT_DISCOUNT_RATE);

        // Get all the orderConfirmationList where discountRate is less than or equal to SMALLER_DISCOUNT_RATE
        defaultOrderConfirmationShouldNotBeFound("discountRate.lessThanOrEqual=" + SMALLER_DISCOUNT_RATE);
    }

    @Test
    @Transactional
    void getAllOrderConfirmationsByDiscountRateIsLessThanSomething() throws Exception {
        // Initialize the database
        orderConfirmationRepository.saveAndFlush(orderConfirmation);

        // Get all the orderConfirmationList where discountRate is less than DEFAULT_DISCOUNT_RATE
        defaultOrderConfirmationShouldNotBeFound("discountRate.lessThan=" + DEFAULT_DISCOUNT_RATE);

        // Get all the orderConfirmationList where discountRate is less than UPDATED_DISCOUNT_RATE
        defaultOrderConfirmationShouldBeFound("discountRate.lessThan=" + UPDATED_DISCOUNT_RATE);
    }

    @Test
    @Transactional
    void getAllOrderConfirmationsByDiscountRateIsGreaterThanSomething() throws Exception {
        // Initialize the database
        orderConfirmationRepository.saveAndFlush(orderConfirmation);

        // Get all the orderConfirmationList where discountRate is greater than DEFAULT_DISCOUNT_RATE
        defaultOrderConfirmationShouldNotBeFound("discountRate.greaterThan=" + DEFAULT_DISCOUNT_RATE);

        // Get all the orderConfirmationList where discountRate is greater than SMALLER_DISCOUNT_RATE
        defaultOrderConfirmationShouldBeFound("discountRate.greaterThan=" + SMALLER_DISCOUNT_RATE);
    }

    @Test
    @Transactional
    void getAllOrderConfirmationsByDiscountTypeIsEqualToSomething() throws Exception {
        // Initialize the database
        orderConfirmationRepository.saveAndFlush(orderConfirmation);

        // Get all the orderConfirmationList where discountType equals to DEFAULT_DISCOUNT_TYPE
        defaultOrderConfirmationShouldBeFound("discountType.equals=" + DEFAULT_DISCOUNT_TYPE);

        // Get all the orderConfirmationList where discountType equals to UPDATED_DISCOUNT_TYPE
        defaultOrderConfirmationShouldNotBeFound("discountType.equals=" + UPDATED_DISCOUNT_TYPE);
    }

    @Test
    @Transactional
    void getAllOrderConfirmationsByDiscountTypeIsNotEqualToSomething() throws Exception {
        // Initialize the database
        orderConfirmationRepository.saveAndFlush(orderConfirmation);

        // Get all the orderConfirmationList where discountType not equals to DEFAULT_DISCOUNT_TYPE
        defaultOrderConfirmationShouldNotBeFound("discountType.notEquals=" + DEFAULT_DISCOUNT_TYPE);

        // Get all the orderConfirmationList where discountType not equals to UPDATED_DISCOUNT_TYPE
        defaultOrderConfirmationShouldBeFound("discountType.notEquals=" + UPDATED_DISCOUNT_TYPE);
    }

    @Test
    @Transactional
    void getAllOrderConfirmationsByDiscountTypeIsInShouldWork() throws Exception {
        // Initialize the database
        orderConfirmationRepository.saveAndFlush(orderConfirmation);

        // Get all the orderConfirmationList where discountType in DEFAULT_DISCOUNT_TYPE or UPDATED_DISCOUNT_TYPE
        defaultOrderConfirmationShouldBeFound("discountType.in=" + DEFAULT_DISCOUNT_TYPE + "," + UPDATED_DISCOUNT_TYPE);

        // Get all the orderConfirmationList where discountType equals to UPDATED_DISCOUNT_TYPE
        defaultOrderConfirmationShouldNotBeFound("discountType.in=" + UPDATED_DISCOUNT_TYPE);
    }

    @Test
    @Transactional
    void getAllOrderConfirmationsByDiscountTypeIsNullOrNotNull() throws Exception {
        // Initialize the database
        orderConfirmationRepository.saveAndFlush(orderConfirmation);

        // Get all the orderConfirmationList where discountType is not null
        defaultOrderConfirmationShouldBeFound("discountType.specified=true");

        // Get all the orderConfirmationList where discountType is null
        defaultOrderConfirmationShouldNotBeFound("discountType.specified=false");
    }

    @Test
    @Transactional
    void getAllOrderConfirmationsByLanguageIsEqualToSomething() throws Exception {
        // Initialize the database
        orderConfirmationRepository.saveAndFlush(orderConfirmation);

        // Get all the orderConfirmationList where language equals to DEFAULT_LANGUAGE
        defaultOrderConfirmationShouldBeFound("language.equals=" + DEFAULT_LANGUAGE);

        // Get all the orderConfirmationList where language equals to UPDATED_LANGUAGE
        defaultOrderConfirmationShouldNotBeFound("language.equals=" + UPDATED_LANGUAGE);
    }

    @Test
    @Transactional
    void getAllOrderConfirmationsByLanguageIsNotEqualToSomething() throws Exception {
        // Initialize the database
        orderConfirmationRepository.saveAndFlush(orderConfirmation);

        // Get all the orderConfirmationList where language not equals to DEFAULT_LANGUAGE
        defaultOrderConfirmationShouldNotBeFound("language.notEquals=" + DEFAULT_LANGUAGE);

        // Get all the orderConfirmationList where language not equals to UPDATED_LANGUAGE
        defaultOrderConfirmationShouldBeFound("language.notEquals=" + UPDATED_LANGUAGE);
    }

    @Test
    @Transactional
    void getAllOrderConfirmationsByLanguageIsInShouldWork() throws Exception {
        // Initialize the database
        orderConfirmationRepository.saveAndFlush(orderConfirmation);

        // Get all the orderConfirmationList where language in DEFAULT_LANGUAGE or UPDATED_LANGUAGE
        defaultOrderConfirmationShouldBeFound("language.in=" + DEFAULT_LANGUAGE + "," + UPDATED_LANGUAGE);

        // Get all the orderConfirmationList where language equals to UPDATED_LANGUAGE
        defaultOrderConfirmationShouldNotBeFound("language.in=" + UPDATED_LANGUAGE);
    }

    @Test
    @Transactional
    void getAllOrderConfirmationsByLanguageIsNullOrNotNull() throws Exception {
        // Initialize the database
        orderConfirmationRepository.saveAndFlush(orderConfirmation);

        // Get all the orderConfirmationList where language is not null
        defaultOrderConfirmationShouldBeFound("language.specified=true");

        // Get all the orderConfirmationList where language is null
        defaultOrderConfirmationShouldNotBeFound("language.specified=false");
    }

    @Test
    @Transactional
    void getAllOrderConfirmationsByPageAmountIsEqualToSomething() throws Exception {
        // Initialize the database
        orderConfirmationRepository.saveAndFlush(orderConfirmation);

        // Get all the orderConfirmationList where pageAmount equals to DEFAULT_PAGE_AMOUNT
        defaultOrderConfirmationShouldBeFound("pageAmount.equals=" + DEFAULT_PAGE_AMOUNT);

        // Get all the orderConfirmationList where pageAmount equals to UPDATED_PAGE_AMOUNT
        defaultOrderConfirmationShouldNotBeFound("pageAmount.equals=" + UPDATED_PAGE_AMOUNT);
    }

    @Test
    @Transactional
    void getAllOrderConfirmationsByPageAmountIsNotEqualToSomething() throws Exception {
        // Initialize the database
        orderConfirmationRepository.saveAndFlush(orderConfirmation);

        // Get all the orderConfirmationList where pageAmount not equals to DEFAULT_PAGE_AMOUNT
        defaultOrderConfirmationShouldNotBeFound("pageAmount.notEquals=" + DEFAULT_PAGE_AMOUNT);

        // Get all the orderConfirmationList where pageAmount not equals to UPDATED_PAGE_AMOUNT
        defaultOrderConfirmationShouldBeFound("pageAmount.notEquals=" + UPDATED_PAGE_AMOUNT);
    }

    @Test
    @Transactional
    void getAllOrderConfirmationsByPageAmountIsInShouldWork() throws Exception {
        // Initialize the database
        orderConfirmationRepository.saveAndFlush(orderConfirmation);

        // Get all the orderConfirmationList where pageAmount in DEFAULT_PAGE_AMOUNT or UPDATED_PAGE_AMOUNT
        defaultOrderConfirmationShouldBeFound("pageAmount.in=" + DEFAULT_PAGE_AMOUNT + "," + UPDATED_PAGE_AMOUNT);

        // Get all the orderConfirmationList where pageAmount equals to UPDATED_PAGE_AMOUNT
        defaultOrderConfirmationShouldNotBeFound("pageAmount.in=" + UPDATED_PAGE_AMOUNT);
    }

    @Test
    @Transactional
    void getAllOrderConfirmationsByPageAmountIsNullOrNotNull() throws Exception {
        // Initialize the database
        orderConfirmationRepository.saveAndFlush(orderConfirmation);

        // Get all the orderConfirmationList where pageAmount is not null
        defaultOrderConfirmationShouldBeFound("pageAmount.specified=true");

        // Get all the orderConfirmationList where pageAmount is null
        defaultOrderConfirmationShouldNotBeFound("pageAmount.specified=false");
    }

    @Test
    @Transactional
    void getAllOrderConfirmationsByPageAmountIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        orderConfirmationRepository.saveAndFlush(orderConfirmation);

        // Get all the orderConfirmationList where pageAmount is greater than or equal to DEFAULT_PAGE_AMOUNT
        defaultOrderConfirmationShouldBeFound("pageAmount.greaterThanOrEqual=" + DEFAULT_PAGE_AMOUNT);

        // Get all the orderConfirmationList where pageAmount is greater than or equal to UPDATED_PAGE_AMOUNT
        defaultOrderConfirmationShouldNotBeFound("pageAmount.greaterThanOrEqual=" + UPDATED_PAGE_AMOUNT);
    }

    @Test
    @Transactional
    void getAllOrderConfirmationsByPageAmountIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        orderConfirmationRepository.saveAndFlush(orderConfirmation);

        // Get all the orderConfirmationList where pageAmount is less than or equal to DEFAULT_PAGE_AMOUNT
        defaultOrderConfirmationShouldBeFound("pageAmount.lessThanOrEqual=" + DEFAULT_PAGE_AMOUNT);

        // Get all the orderConfirmationList where pageAmount is less than or equal to SMALLER_PAGE_AMOUNT
        defaultOrderConfirmationShouldNotBeFound("pageAmount.lessThanOrEqual=" + SMALLER_PAGE_AMOUNT);
    }

    @Test
    @Transactional
    void getAllOrderConfirmationsByPageAmountIsLessThanSomething() throws Exception {
        // Initialize the database
        orderConfirmationRepository.saveAndFlush(orderConfirmation);

        // Get all the orderConfirmationList where pageAmount is less than DEFAULT_PAGE_AMOUNT
        defaultOrderConfirmationShouldNotBeFound("pageAmount.lessThan=" + DEFAULT_PAGE_AMOUNT);

        // Get all the orderConfirmationList where pageAmount is less than UPDATED_PAGE_AMOUNT
        defaultOrderConfirmationShouldBeFound("pageAmount.lessThan=" + UPDATED_PAGE_AMOUNT);
    }

    @Test
    @Transactional
    void getAllOrderConfirmationsByPageAmountIsGreaterThanSomething() throws Exception {
        // Initialize the database
        orderConfirmationRepository.saveAndFlush(orderConfirmation);

        // Get all the orderConfirmationList where pageAmount is greater than DEFAULT_PAGE_AMOUNT
        defaultOrderConfirmationShouldNotBeFound("pageAmount.greaterThan=" + DEFAULT_PAGE_AMOUNT);

        // Get all the orderConfirmationList where pageAmount is greater than SMALLER_PAGE_AMOUNT
        defaultOrderConfirmationShouldBeFound("pageAmount.greaterThan=" + SMALLER_PAGE_AMOUNT);
    }

    @Test
    @Transactional
    void getAllOrderConfirmationsByNotesIsEqualToSomething() throws Exception {
        // Initialize the database
        orderConfirmationRepository.saveAndFlush(orderConfirmation);

        // Get all the orderConfirmationList where notes equals to DEFAULT_NOTES
        defaultOrderConfirmationShouldBeFound("notes.equals=" + DEFAULT_NOTES);

        // Get all the orderConfirmationList where notes equals to UPDATED_NOTES
        defaultOrderConfirmationShouldNotBeFound("notes.equals=" + UPDATED_NOTES);
    }

    @Test
    @Transactional
    void getAllOrderConfirmationsByNotesIsNotEqualToSomething() throws Exception {
        // Initialize the database
        orderConfirmationRepository.saveAndFlush(orderConfirmation);

        // Get all the orderConfirmationList where notes not equals to DEFAULT_NOTES
        defaultOrderConfirmationShouldNotBeFound("notes.notEquals=" + DEFAULT_NOTES);

        // Get all the orderConfirmationList where notes not equals to UPDATED_NOTES
        defaultOrderConfirmationShouldBeFound("notes.notEquals=" + UPDATED_NOTES);
    }

    @Test
    @Transactional
    void getAllOrderConfirmationsByNotesIsInShouldWork() throws Exception {
        // Initialize the database
        orderConfirmationRepository.saveAndFlush(orderConfirmation);

        // Get all the orderConfirmationList where notes in DEFAULT_NOTES or UPDATED_NOTES
        defaultOrderConfirmationShouldBeFound("notes.in=" + DEFAULT_NOTES + "," + UPDATED_NOTES);

        // Get all the orderConfirmationList where notes equals to UPDATED_NOTES
        defaultOrderConfirmationShouldNotBeFound("notes.in=" + UPDATED_NOTES);
    }

    @Test
    @Transactional
    void getAllOrderConfirmationsByNotesIsNullOrNotNull() throws Exception {
        // Initialize the database
        orderConfirmationRepository.saveAndFlush(orderConfirmation);

        // Get all the orderConfirmationList where notes is not null
        defaultOrderConfirmationShouldBeFound("notes.specified=true");

        // Get all the orderConfirmationList where notes is null
        defaultOrderConfirmationShouldNotBeFound("notes.specified=false");
    }

    @Test
    @Transactional
    void getAllOrderConfirmationsByNotesContainsSomething() throws Exception {
        // Initialize the database
        orderConfirmationRepository.saveAndFlush(orderConfirmation);

        // Get all the orderConfirmationList where notes contains DEFAULT_NOTES
        defaultOrderConfirmationShouldBeFound("notes.contains=" + DEFAULT_NOTES);

        // Get all the orderConfirmationList where notes contains UPDATED_NOTES
        defaultOrderConfirmationShouldNotBeFound("notes.contains=" + UPDATED_NOTES);
    }

    @Test
    @Transactional
    void getAllOrderConfirmationsByNotesNotContainsSomething() throws Exception {
        // Initialize the database
        orderConfirmationRepository.saveAndFlush(orderConfirmation);

        // Get all the orderConfirmationList where notes does not contain DEFAULT_NOTES
        defaultOrderConfirmationShouldNotBeFound("notes.doesNotContain=" + DEFAULT_NOTES);

        // Get all the orderConfirmationList where notes does not contain UPDATED_NOTES
        defaultOrderConfirmationShouldBeFound("notes.doesNotContain=" + UPDATED_NOTES);
    }

    @Test
    @Transactional
    void getAllOrderConfirmationsByStatusIsEqualToSomething() throws Exception {
        // Initialize the database
        orderConfirmationRepository.saveAndFlush(orderConfirmation);

        // Get all the orderConfirmationList where status equals to DEFAULT_STATUS
        defaultOrderConfirmationShouldBeFound("status.equals=" + DEFAULT_STATUS);

        // Get all the orderConfirmationList where status equals to UPDATED_STATUS
        defaultOrderConfirmationShouldNotBeFound("status.equals=" + UPDATED_STATUS);
    }

    @Test
    @Transactional
    void getAllOrderConfirmationsByStatusIsNotEqualToSomething() throws Exception {
        // Initialize the database
        orderConfirmationRepository.saveAndFlush(orderConfirmation);

        // Get all the orderConfirmationList where status not equals to DEFAULT_STATUS
        defaultOrderConfirmationShouldNotBeFound("status.notEquals=" + DEFAULT_STATUS);

        // Get all the orderConfirmationList where status not equals to UPDATED_STATUS
        defaultOrderConfirmationShouldBeFound("status.notEquals=" + UPDATED_STATUS);
    }

    @Test
    @Transactional
    void getAllOrderConfirmationsByStatusIsInShouldWork() throws Exception {
        // Initialize the database
        orderConfirmationRepository.saveAndFlush(orderConfirmation);

        // Get all the orderConfirmationList where status in DEFAULT_STATUS or UPDATED_STATUS
        defaultOrderConfirmationShouldBeFound("status.in=" + DEFAULT_STATUS + "," + UPDATED_STATUS);

        // Get all the orderConfirmationList where status equals to UPDATED_STATUS
        defaultOrderConfirmationShouldNotBeFound("status.in=" + UPDATED_STATUS);
    }

    @Test
    @Transactional
    void getAllOrderConfirmationsByStatusIsNullOrNotNull() throws Exception {
        // Initialize the database
        orderConfirmationRepository.saveAndFlush(orderConfirmation);

        // Get all the orderConfirmationList where status is not null
        defaultOrderConfirmationShouldBeFound("status.specified=true");

        // Get all the orderConfirmationList where status is null
        defaultOrderConfirmationShouldNotBeFound("status.specified=false");
    }

    @Test
    @Transactional
    void getAllOrderConfirmationsByCreatedIsEqualToSomething() throws Exception {
        // Initialize the database
        orderConfirmationRepository.saveAndFlush(orderConfirmation);

        // Get all the orderConfirmationList where created equals to DEFAULT_CREATED
        defaultOrderConfirmationShouldBeFound("created.equals=" + DEFAULT_CREATED);

        // Get all the orderConfirmationList where created equals to UPDATED_CREATED
        defaultOrderConfirmationShouldNotBeFound("created.equals=" + UPDATED_CREATED);
    }

    @Test
    @Transactional
    void getAllOrderConfirmationsByCreatedIsNotEqualToSomething() throws Exception {
        // Initialize the database
        orderConfirmationRepository.saveAndFlush(orderConfirmation);

        // Get all the orderConfirmationList where created not equals to DEFAULT_CREATED
        defaultOrderConfirmationShouldNotBeFound("created.notEquals=" + DEFAULT_CREATED);

        // Get all the orderConfirmationList where created not equals to UPDATED_CREATED
        defaultOrderConfirmationShouldBeFound("created.notEquals=" + UPDATED_CREATED);
    }

    @Test
    @Transactional
    void getAllOrderConfirmationsByCreatedIsInShouldWork() throws Exception {
        // Initialize the database
        orderConfirmationRepository.saveAndFlush(orderConfirmation);

        // Get all the orderConfirmationList where created in DEFAULT_CREATED or UPDATED_CREATED
        defaultOrderConfirmationShouldBeFound("created.in=" + DEFAULT_CREATED + "," + UPDATED_CREATED);

        // Get all the orderConfirmationList where created equals to UPDATED_CREATED
        defaultOrderConfirmationShouldNotBeFound("created.in=" + UPDATED_CREATED);
    }

    @Test
    @Transactional
    void getAllOrderConfirmationsByCreatedIsNullOrNotNull() throws Exception {
        // Initialize the database
        orderConfirmationRepository.saveAndFlush(orderConfirmation);

        // Get all the orderConfirmationList where created is not null
        defaultOrderConfirmationShouldBeFound("created.specified=true");

        // Get all the orderConfirmationList where created is null
        defaultOrderConfirmationShouldNotBeFound("created.specified=false");
    }

    @Test
    @Transactional
    void getAllOrderConfirmationsByCreatedIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        orderConfirmationRepository.saveAndFlush(orderConfirmation);

        // Get all the orderConfirmationList where created is greater than or equal to DEFAULT_CREATED
        defaultOrderConfirmationShouldBeFound("created.greaterThanOrEqual=" + DEFAULT_CREATED);

        // Get all the orderConfirmationList where created is greater than or equal to UPDATED_CREATED
        defaultOrderConfirmationShouldNotBeFound("created.greaterThanOrEqual=" + UPDATED_CREATED);
    }

    @Test
    @Transactional
    void getAllOrderConfirmationsByCreatedIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        orderConfirmationRepository.saveAndFlush(orderConfirmation);

        // Get all the orderConfirmationList where created is less than or equal to DEFAULT_CREATED
        defaultOrderConfirmationShouldBeFound("created.lessThanOrEqual=" + DEFAULT_CREATED);

        // Get all the orderConfirmationList where created is less than or equal to SMALLER_CREATED
        defaultOrderConfirmationShouldNotBeFound("created.lessThanOrEqual=" + SMALLER_CREATED);
    }

    @Test
    @Transactional
    void getAllOrderConfirmationsByCreatedIsLessThanSomething() throws Exception {
        // Initialize the database
        orderConfirmationRepository.saveAndFlush(orderConfirmation);

        // Get all the orderConfirmationList where created is less than DEFAULT_CREATED
        defaultOrderConfirmationShouldNotBeFound("created.lessThan=" + DEFAULT_CREATED);

        // Get all the orderConfirmationList where created is less than UPDATED_CREATED
        defaultOrderConfirmationShouldBeFound("created.lessThan=" + UPDATED_CREATED);
    }

    @Test
    @Transactional
    void getAllOrderConfirmationsByCreatedIsGreaterThanSomething() throws Exception {
        // Initialize the database
        orderConfirmationRepository.saveAndFlush(orderConfirmation);

        // Get all the orderConfirmationList where created is greater than DEFAULT_CREATED
        defaultOrderConfirmationShouldNotBeFound("created.greaterThan=" + DEFAULT_CREATED);

        // Get all the orderConfirmationList where created is greater than SMALLER_CREATED
        defaultOrderConfirmationShouldBeFound("created.greaterThan=" + SMALLER_CREATED);
    }

    @Test
    @Transactional
    void getAllOrderConfirmationsByFreeTextsIsEqualToSomething() throws Exception {
        // Initialize the database
        orderConfirmationRepository.saveAndFlush(orderConfirmation);
        DocumentFreeText freeTexts = DocumentFreeTextResourceIT.createEntity(em);
        em.persist(freeTexts);
        em.flush();
        orderConfirmation.addFreeTexts(freeTexts);
        orderConfirmationRepository.saveAndFlush(orderConfirmation);
        Long freeTextsId = freeTexts.getId();

        // Get all the orderConfirmationList where freeTexts equals to freeTextsId
        defaultOrderConfirmationShouldBeFound("freeTextsId.equals=" + freeTextsId);

        // Get all the orderConfirmationList where freeTexts equals to (freeTextsId + 1)
        defaultOrderConfirmationShouldNotBeFound("freeTextsId.equals=" + (freeTextsId + 1));
    }

    @Test
    @Transactional
    void getAllOrderConfirmationsByTextsIsEqualToSomething() throws Exception {
        // Initialize the database
        orderConfirmationRepository.saveAndFlush(orderConfirmation);
        DescriptiveDocumentText texts = DescriptiveDocumentTextResourceIT.createEntity(em);
        em.persist(texts);
        em.flush();
        orderConfirmation.addTexts(texts);
        orderConfirmationRepository.saveAndFlush(orderConfirmation);
        Long textsId = texts.getId();

        // Get all the orderConfirmationList where texts equals to textsId
        defaultOrderConfirmationShouldBeFound("textsId.equals=" + textsId);

        // Get all the orderConfirmationList where texts equals to (textsId + 1)
        defaultOrderConfirmationShouldNotBeFound("textsId.equals=" + (textsId + 1));
    }

    @Test
    @Transactional
    void getAllOrderConfirmationsByPositionsIsEqualToSomething() throws Exception {
        // Initialize the database
        orderConfirmationRepository.saveAndFlush(orderConfirmation);
        DocumentPosition positions = DocumentPositionResourceIT.createEntity(em);
        em.persist(positions);
        em.flush();
        orderConfirmation.addPositions(positions);
        orderConfirmationRepository.saveAndFlush(orderConfirmation);
        Long positionsId = positions.getId();

        // Get all the orderConfirmationList where positions equals to positionsId
        defaultOrderConfirmationShouldBeFound("positionsId.equals=" + positionsId);

        // Get all the orderConfirmationList where positions equals to (positionsId + 1)
        defaultOrderConfirmationShouldNotBeFound("positionsId.equals=" + (positionsId + 1));
    }

    @Test
    @Transactional
    void getAllOrderConfirmationsByContactIsEqualToSomething() throws Exception {
        // Initialize the database
        orderConfirmationRepository.saveAndFlush(orderConfirmation);
        Contact contact = ContactResourceIT.createEntity(em);
        em.persist(contact);
        em.flush();
        orderConfirmation.setContact(contact);
        orderConfirmationRepository.saveAndFlush(orderConfirmation);
        Long contactId = contact.getId();

        // Get all the orderConfirmationList where contact equals to contactId
        defaultOrderConfirmationShouldBeFound("contactId.equals=" + contactId);

        // Get all the orderConfirmationList where contact equals to (contactId + 1)
        defaultOrderConfirmationShouldNotBeFound("contactId.equals=" + (contactId + 1));
    }

    @Test
    @Transactional
    void getAllOrderConfirmationsByContactAddressIsEqualToSomething() throws Exception {
        // Initialize the database
        orderConfirmationRepository.saveAndFlush(orderConfirmation);
        ContactAddress contactAddress = ContactAddressResourceIT.createEntity(em);
        em.persist(contactAddress);
        em.flush();
        orderConfirmation.setContactAddress(contactAddress);
        orderConfirmationRepository.saveAndFlush(orderConfirmation);
        Long contactAddressId = contactAddress.getId();

        // Get all the orderConfirmationList where contactAddress equals to contactAddressId
        defaultOrderConfirmationShouldBeFound("contactAddressId.equals=" + contactAddressId);

        // Get all the orderConfirmationList where contactAddress equals to (contactAddressId + 1)
        defaultOrderConfirmationShouldNotBeFound("contactAddressId.equals=" + (contactAddressId + 1));
    }

    @Test
    @Transactional
    void getAllOrderConfirmationsByContactPersonIsEqualToSomething() throws Exception {
        // Initialize the database
        orderConfirmationRepository.saveAndFlush(orderConfirmation);
        ContactPerson contactPerson = ContactPersonResourceIT.createEntity(em);
        em.persist(contactPerson);
        em.flush();
        orderConfirmation.setContactPerson(contactPerson);
        orderConfirmationRepository.saveAndFlush(orderConfirmation);
        Long contactPersonId = contactPerson.getId();

        // Get all the orderConfirmationList where contactPerson equals to contactPersonId
        defaultOrderConfirmationShouldBeFound("contactPersonId.equals=" + contactPersonId);

        // Get all the orderConfirmationList where contactPerson equals to (contactPersonId + 1)
        defaultOrderConfirmationShouldNotBeFound("contactPersonId.equals=" + (contactPersonId + 1));
    }

    @Test
    @Transactional
    void getAllOrderConfirmationsByContactPrePageAddressIsEqualToSomething() throws Exception {
        // Initialize the database
        orderConfirmationRepository.saveAndFlush(orderConfirmation);
        ContactAddress contactPrePageAddress = ContactAddressResourceIT.createEntity(em);
        em.persist(contactPrePageAddress);
        em.flush();
        orderConfirmation.setContactPrePageAddress(contactPrePageAddress);
        orderConfirmationRepository.saveAndFlush(orderConfirmation);
        Long contactPrePageAddressId = contactPrePageAddress.getId();

        // Get all the orderConfirmationList where contactPrePageAddress equals to contactPrePageAddressId
        defaultOrderConfirmationShouldBeFound("contactPrePageAddressId.equals=" + contactPrePageAddressId);

        // Get all the orderConfirmationList where contactPrePageAddress equals to (contactPrePageAddressId + 1)
        defaultOrderConfirmationShouldNotBeFound("contactPrePageAddressId.equals=" + (contactPrePageAddressId + 1));
    }

    @Test
    @Transactional
    void getAllOrderConfirmationsByLayoutIsEqualToSomething() throws Exception {
        // Initialize the database
        orderConfirmationRepository.saveAndFlush(orderConfirmation);
        Layout layout = LayoutResourceIT.createEntity(em);
        em.persist(layout);
        em.flush();
        orderConfirmation.setLayout(layout);
        orderConfirmationRepository.saveAndFlush(orderConfirmation);
        Long layoutId = layout.getId();

        // Get all the orderConfirmationList where layout equals to layoutId
        defaultOrderConfirmationShouldBeFound("layoutId.equals=" + layoutId);

        // Get all the orderConfirmationList where layout equals to (layoutId + 1)
        defaultOrderConfirmationShouldNotBeFound("layoutId.equals=" + (layoutId + 1));
    }

    @Test
    @Transactional
    void getAllOrderConfirmationsBySignatureIsEqualToSomething() throws Exception {
        // Initialize the database
        orderConfirmationRepository.saveAndFlush(orderConfirmation);
        Signature signature = SignatureResourceIT.createEntity(em);
        em.persist(signature);
        em.flush();
        orderConfirmation.setSignature(signature);
        orderConfirmationRepository.saveAndFlush(orderConfirmation);
        Long signatureId = signature.getId();

        // Get all the orderConfirmationList where signature equals to signatureId
        defaultOrderConfirmationShouldBeFound("signatureId.equals=" + signatureId);

        // Get all the orderConfirmationList where signature equals to (signatureId + 1)
        defaultOrderConfirmationShouldNotBeFound("signatureId.equals=" + (signatureId + 1));
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultOrderConfirmationShouldBeFound(String filter) throws Exception {
        restOrderConfirmationMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(orderConfirmation.getId().intValue())))
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
        restOrderConfirmationMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultOrderConfirmationShouldNotBeFound(String filter) throws Exception {
        restOrderConfirmationMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restOrderConfirmationMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingOrderConfirmation() throws Exception {
        // Get the orderConfirmation
        restOrderConfirmationMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewOrderConfirmation() throws Exception {
        // Initialize the database
        orderConfirmationRepository.saveAndFlush(orderConfirmation);

        int databaseSizeBeforeUpdate = orderConfirmationRepository.findAll().size();

        // Update the orderConfirmation
        OrderConfirmation updatedOrderConfirmation = orderConfirmationRepository.findById(orderConfirmation.getId()).get();
        // Disconnect from session so that the updates on updatedOrderConfirmation are not directly saved in db
        em.detach(updatedOrderConfirmation);
        updatedOrderConfirmation
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
        OrderConfirmationDTO orderConfirmationDTO = orderConfirmationMapper.toDto(updatedOrderConfirmation);

        restOrderConfirmationMockMvc
            .perform(
                put(ENTITY_API_URL_ID, orderConfirmationDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(orderConfirmationDTO))
            )
            .andExpect(status().isOk());

        // Validate the OrderConfirmation in the database
        List<OrderConfirmation> orderConfirmationList = orderConfirmationRepository.findAll();
        assertThat(orderConfirmationList).hasSize(databaseSizeBeforeUpdate);
        OrderConfirmation testOrderConfirmation = orderConfirmationList.get(orderConfirmationList.size() - 1);
        assertThat(testOrderConfirmation.getRemoteId()).isEqualTo(UPDATED_REMOTE_ID);
        assertThat(testOrderConfirmation.getNumber()).isEqualTo(UPDATED_NUMBER);
        assertThat(testOrderConfirmation.getContactName()).isEqualTo(UPDATED_CONTACT_NAME);
        assertThat(testOrderConfirmation.getDate()).isEqualTo(UPDATED_DATE);
        assertThat(testOrderConfirmation.getPeriodText()).isEqualTo(UPDATED_PERIOD_TEXT);
        assertThat(testOrderConfirmation.getCurrency()).isEqualTo(UPDATED_CURRENCY);
        assertThat(testOrderConfirmation.getTotal()).isEqualTo(UPDATED_TOTAL);
        assertThat(testOrderConfirmation.getVatIncluded()).isEqualTo(UPDATED_VAT_INCLUDED);
        assertThat(testOrderConfirmation.getDiscountRate()).isEqualTo(UPDATED_DISCOUNT_RATE);
        assertThat(testOrderConfirmation.getDiscountType()).isEqualTo(UPDATED_DISCOUNT_TYPE);
        assertThat(testOrderConfirmation.getLanguage()).isEqualTo(UPDATED_LANGUAGE);
        assertThat(testOrderConfirmation.getPageAmount()).isEqualTo(UPDATED_PAGE_AMOUNT);
        assertThat(testOrderConfirmation.getNotes()).isEqualTo(UPDATED_NOTES);
        assertThat(testOrderConfirmation.getStatus()).isEqualTo(UPDATED_STATUS);
        assertThat(testOrderConfirmation.getCreated()).isEqualTo(UPDATED_CREATED);
    }

    @Test
    @Transactional
    void putNonExistingOrderConfirmation() throws Exception {
        int databaseSizeBeforeUpdate = orderConfirmationRepository.findAll().size();
        orderConfirmation.setId(count.incrementAndGet());

        // Create the OrderConfirmation
        OrderConfirmationDTO orderConfirmationDTO = orderConfirmationMapper.toDto(orderConfirmation);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restOrderConfirmationMockMvc
            .perform(
                put(ENTITY_API_URL_ID, orderConfirmationDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(orderConfirmationDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the OrderConfirmation in the database
        List<OrderConfirmation> orderConfirmationList = orderConfirmationRepository.findAll();
        assertThat(orderConfirmationList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchOrderConfirmation() throws Exception {
        int databaseSizeBeforeUpdate = orderConfirmationRepository.findAll().size();
        orderConfirmation.setId(count.incrementAndGet());

        // Create the OrderConfirmation
        OrderConfirmationDTO orderConfirmationDTO = orderConfirmationMapper.toDto(orderConfirmation);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restOrderConfirmationMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(orderConfirmationDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the OrderConfirmation in the database
        List<OrderConfirmation> orderConfirmationList = orderConfirmationRepository.findAll();
        assertThat(orderConfirmationList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamOrderConfirmation() throws Exception {
        int databaseSizeBeforeUpdate = orderConfirmationRepository.findAll().size();
        orderConfirmation.setId(count.incrementAndGet());

        // Create the OrderConfirmation
        OrderConfirmationDTO orderConfirmationDTO = orderConfirmationMapper.toDto(orderConfirmation);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restOrderConfirmationMockMvc
            .perform(
                put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(orderConfirmationDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the OrderConfirmation in the database
        List<OrderConfirmation> orderConfirmationList = orderConfirmationRepository.findAll();
        assertThat(orderConfirmationList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateOrderConfirmationWithPatch() throws Exception {
        // Initialize the database
        orderConfirmationRepository.saveAndFlush(orderConfirmation);

        int databaseSizeBeforeUpdate = orderConfirmationRepository.findAll().size();

        // Update the orderConfirmation using partial update
        OrderConfirmation partialUpdatedOrderConfirmation = new OrderConfirmation();
        partialUpdatedOrderConfirmation.setId(orderConfirmation.getId());

        partialUpdatedOrderConfirmation
            .remoteId(UPDATED_REMOTE_ID)
            .number(UPDATED_NUMBER)
            .contactName(UPDATED_CONTACT_NAME)
            .total(UPDATED_TOTAL)
            .discountType(UPDATED_DISCOUNT_TYPE)
            .language(UPDATED_LANGUAGE)
            .pageAmount(UPDATED_PAGE_AMOUNT)
            .notes(UPDATED_NOTES)
            .status(UPDATED_STATUS)
            .created(UPDATED_CREATED);

        restOrderConfirmationMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedOrderConfirmation.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedOrderConfirmation))
            )
            .andExpect(status().isOk());

        // Validate the OrderConfirmation in the database
        List<OrderConfirmation> orderConfirmationList = orderConfirmationRepository.findAll();
        assertThat(orderConfirmationList).hasSize(databaseSizeBeforeUpdate);
        OrderConfirmation testOrderConfirmation = orderConfirmationList.get(orderConfirmationList.size() - 1);
        assertThat(testOrderConfirmation.getRemoteId()).isEqualTo(UPDATED_REMOTE_ID);
        assertThat(testOrderConfirmation.getNumber()).isEqualTo(UPDATED_NUMBER);
        assertThat(testOrderConfirmation.getContactName()).isEqualTo(UPDATED_CONTACT_NAME);
        assertThat(testOrderConfirmation.getDate()).isEqualTo(DEFAULT_DATE);
        assertThat(testOrderConfirmation.getPeriodText()).isEqualTo(DEFAULT_PERIOD_TEXT);
        assertThat(testOrderConfirmation.getCurrency()).isEqualTo(DEFAULT_CURRENCY);
        assertThat(testOrderConfirmation.getTotal()).isEqualTo(UPDATED_TOTAL);
        assertThat(testOrderConfirmation.getVatIncluded()).isEqualTo(DEFAULT_VAT_INCLUDED);
        assertThat(testOrderConfirmation.getDiscountRate()).isEqualTo(DEFAULT_DISCOUNT_RATE);
        assertThat(testOrderConfirmation.getDiscountType()).isEqualTo(UPDATED_DISCOUNT_TYPE);
        assertThat(testOrderConfirmation.getLanguage()).isEqualTo(UPDATED_LANGUAGE);
        assertThat(testOrderConfirmation.getPageAmount()).isEqualTo(UPDATED_PAGE_AMOUNT);
        assertThat(testOrderConfirmation.getNotes()).isEqualTo(UPDATED_NOTES);
        assertThat(testOrderConfirmation.getStatus()).isEqualTo(UPDATED_STATUS);
        assertThat(testOrderConfirmation.getCreated()).isEqualTo(UPDATED_CREATED);
    }

    @Test
    @Transactional
    void fullUpdateOrderConfirmationWithPatch() throws Exception {
        // Initialize the database
        orderConfirmationRepository.saveAndFlush(orderConfirmation);

        int databaseSizeBeforeUpdate = orderConfirmationRepository.findAll().size();

        // Update the orderConfirmation using partial update
        OrderConfirmation partialUpdatedOrderConfirmation = new OrderConfirmation();
        partialUpdatedOrderConfirmation.setId(orderConfirmation.getId());

        partialUpdatedOrderConfirmation
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

        restOrderConfirmationMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedOrderConfirmation.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedOrderConfirmation))
            )
            .andExpect(status().isOk());

        // Validate the OrderConfirmation in the database
        List<OrderConfirmation> orderConfirmationList = orderConfirmationRepository.findAll();
        assertThat(orderConfirmationList).hasSize(databaseSizeBeforeUpdate);
        OrderConfirmation testOrderConfirmation = orderConfirmationList.get(orderConfirmationList.size() - 1);
        assertThat(testOrderConfirmation.getRemoteId()).isEqualTo(UPDATED_REMOTE_ID);
        assertThat(testOrderConfirmation.getNumber()).isEqualTo(UPDATED_NUMBER);
        assertThat(testOrderConfirmation.getContactName()).isEqualTo(UPDATED_CONTACT_NAME);
        assertThat(testOrderConfirmation.getDate()).isEqualTo(UPDATED_DATE);
        assertThat(testOrderConfirmation.getPeriodText()).isEqualTo(UPDATED_PERIOD_TEXT);
        assertThat(testOrderConfirmation.getCurrency()).isEqualTo(UPDATED_CURRENCY);
        assertThat(testOrderConfirmation.getTotal()).isEqualTo(UPDATED_TOTAL);
        assertThat(testOrderConfirmation.getVatIncluded()).isEqualTo(UPDATED_VAT_INCLUDED);
        assertThat(testOrderConfirmation.getDiscountRate()).isEqualTo(UPDATED_DISCOUNT_RATE);
        assertThat(testOrderConfirmation.getDiscountType()).isEqualTo(UPDATED_DISCOUNT_TYPE);
        assertThat(testOrderConfirmation.getLanguage()).isEqualTo(UPDATED_LANGUAGE);
        assertThat(testOrderConfirmation.getPageAmount()).isEqualTo(UPDATED_PAGE_AMOUNT);
        assertThat(testOrderConfirmation.getNotes()).isEqualTo(UPDATED_NOTES);
        assertThat(testOrderConfirmation.getStatus()).isEqualTo(UPDATED_STATUS);
        assertThat(testOrderConfirmation.getCreated()).isEqualTo(UPDATED_CREATED);
    }

    @Test
    @Transactional
    void patchNonExistingOrderConfirmation() throws Exception {
        int databaseSizeBeforeUpdate = orderConfirmationRepository.findAll().size();
        orderConfirmation.setId(count.incrementAndGet());

        // Create the OrderConfirmation
        OrderConfirmationDTO orderConfirmationDTO = orderConfirmationMapper.toDto(orderConfirmation);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restOrderConfirmationMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, orderConfirmationDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(orderConfirmationDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the OrderConfirmation in the database
        List<OrderConfirmation> orderConfirmationList = orderConfirmationRepository.findAll();
        assertThat(orderConfirmationList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchOrderConfirmation() throws Exception {
        int databaseSizeBeforeUpdate = orderConfirmationRepository.findAll().size();
        orderConfirmation.setId(count.incrementAndGet());

        // Create the OrderConfirmation
        OrderConfirmationDTO orderConfirmationDTO = orderConfirmationMapper.toDto(orderConfirmation);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restOrderConfirmationMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(orderConfirmationDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the OrderConfirmation in the database
        List<OrderConfirmation> orderConfirmationList = orderConfirmationRepository.findAll();
        assertThat(orderConfirmationList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamOrderConfirmation() throws Exception {
        int databaseSizeBeforeUpdate = orderConfirmationRepository.findAll().size();
        orderConfirmation.setId(count.incrementAndGet());

        // Create the OrderConfirmation
        OrderConfirmationDTO orderConfirmationDTO = orderConfirmationMapper.toDto(orderConfirmation);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restOrderConfirmationMockMvc
            .perform(
                patch(ENTITY_API_URL)
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(orderConfirmationDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the OrderConfirmation in the database
        List<OrderConfirmation> orderConfirmationList = orderConfirmationRepository.findAll();
        assertThat(orderConfirmationList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteOrderConfirmation() throws Exception {
        // Initialize the database
        orderConfirmationRepository.saveAndFlush(orderConfirmation);

        int databaseSizeBeforeDelete = orderConfirmationRepository.findAll().size();

        // Delete the orderConfirmation
        restOrderConfirmationMockMvc
            .perform(delete(ENTITY_API_URL_ID, orderConfirmation.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<OrderConfirmation> orderConfirmationList = orderConfirmationRepository.findAll();
        assertThat(orderConfirmationList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
