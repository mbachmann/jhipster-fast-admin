package ch.united.fastadmin.web.rest;

import static ch.united.fastadmin.web.rest.TestUtil.sameInstant;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import ch.united.fastadmin.IntegrationTest;
import ch.united.fastadmin.domain.OrderConfirmation;
import ch.united.fastadmin.domain.enumeration.Currency;
import ch.united.fastadmin.domain.enumeration.DiscountType;
import ch.united.fastadmin.domain.enumeration.DocumentLanguage;
import ch.united.fastadmin.domain.enumeration.OrderConfirmationStatus;
import ch.united.fastadmin.repository.OrderConfirmationRepository;
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

    private static final String DEFAULT_NUMBER = "AAAAAAAAAA";
    private static final String UPDATED_NUMBER = "BBBBBBBBBB";

    private static final String DEFAULT_CONTACT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_CONTACT_NAME = "BBBBBBBBBB";

    private static final LocalDate DEFAULT_DATE = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_DATE = LocalDate.now(ZoneId.systemDefault());

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

    private static final DocumentLanguage DEFAULT_LANGUAGE = DocumentLanguage.FRENCH;
    private static final DocumentLanguage UPDATED_LANGUAGE = DocumentLanguage.ENGLISH;

    private static final Integer DEFAULT_PAGE_AMOUNT = 1;
    private static final Integer UPDATED_PAGE_AMOUNT = 2;

    private static final String DEFAULT_NOTES = "AAAAAAAAAA";
    private static final String UPDATED_NOTES = "BBBBBBBBBB";

    private static final OrderConfirmationStatus DEFAULT_STATUS = OrderConfirmationStatus.DRAFT;
    private static final OrderConfirmationStatus UPDATED_STATUS = OrderConfirmationStatus.SENT;

    private static final ZonedDateTime DEFAULT_CREATED = ZonedDateTime.ofInstant(Instant.ofEpochMilli(0L), ZoneOffset.UTC);
    private static final ZonedDateTime UPDATED_CREATED = ZonedDateTime.now(ZoneId.systemDefault()).withNano(0);

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
