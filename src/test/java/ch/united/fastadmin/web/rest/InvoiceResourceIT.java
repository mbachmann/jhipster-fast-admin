package ch.united.fastadmin.web.rest;

import static ch.united.fastadmin.web.rest.TestUtil.sameInstant;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import ch.united.fastadmin.IntegrationTest;
import ch.united.fastadmin.domain.Invoice;
import ch.united.fastadmin.domain.enumeration.Currency;
import ch.united.fastadmin.domain.enumeration.DiscountType;
import ch.united.fastadmin.domain.enumeration.DocumentLanguage;
import ch.united.fastadmin.domain.enumeration.InvoiceStatus;
import ch.united.fastadmin.domain.enumeration.IsrPosition;
import ch.united.fastadmin.repository.InvoiceRepository;
import ch.united.fastadmin.service.dto.InvoiceDTO;
import ch.united.fastadmin.service.mapper.InvoiceMapper;
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
 * Integration tests for the {@link InvoiceResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class InvoiceResourceIT {

    private static final Integer DEFAULT_REMOTE_ID = 1;
    private static final Integer UPDATED_REMOTE_ID = 2;

    private static final String DEFAULT_NUMBER = "AAAAAAAAAA";
    private static final String UPDATED_NUMBER = "BBBBBBBBBB";

    private static final String DEFAULT_CONTACT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_CONTACT_NAME = "BBBBBBBBBB";

    private static final LocalDate DEFAULT_DATE = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_DATE = LocalDate.now(ZoneId.systemDefault());

    private static final LocalDate DEFAULT_DUE = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_DUE = LocalDate.now(ZoneId.systemDefault());

    private static final LocalDate DEFAULT_PERIOD_FROM = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_PERIOD_FROM = LocalDate.now(ZoneId.systemDefault());

    private static final LocalDate DEFAULT_PERIOD_TO = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_PERIOD_TO = LocalDate.now(ZoneId.systemDefault());

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

    private static final Integer DEFAULT_CASH_DISCOUNT_RATE = 1;
    private static final Integer UPDATED_CASH_DISCOUNT_RATE = 2;

    private static final LocalDate DEFAULT_CASH_DISCOUNT_DATE = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_CASH_DISCOUNT_DATE = LocalDate.now(ZoneId.systemDefault());

    private static final Double DEFAULT_TOTAL_PAID = 1D;
    private static final Double UPDATED_TOTAL_PAID = 2D;

    private static final String DEFAULT_PAID_DATE = "AAAAAAAAAA";
    private static final String UPDATED_PAID_DATE = "BBBBBBBBBB";

    private static final IsrPosition DEFAULT_ISR_POSITION = IsrPosition.ADDITIONAL_PAGE;
    private static final IsrPosition UPDATED_ISR_POSITION = IsrPosition.FIRST_PAGE;

    private static final String DEFAULT_ISR_REFERENCE_NUMBER = "AAAAAAAAAA";
    private static final String UPDATED_ISR_REFERENCE_NUMBER = "BBBBBBBBBB";

    private static final Boolean DEFAULT_PAYMENT_LINK_PAYPAL = false;
    private static final Boolean UPDATED_PAYMENT_LINK_PAYPAL = true;

    private static final String DEFAULT_PAYMENT_LINK_PAYPAL_URL = "AAAAAAAAAA";
    private static final String UPDATED_PAYMENT_LINK_PAYPAL_URL = "BBBBBBBBBB";

    private static final Boolean DEFAULT_PAYMENT_LINK_POSTFINANCE = false;
    private static final Boolean UPDATED_PAYMENT_LINK_POSTFINANCE = true;

    private static final String DEFAULT_PAYMENT_LINK_POSTFINANCE_URL = "AAAAAAAAAA";
    private static final String UPDATED_PAYMENT_LINK_POSTFINANCE_URL = "BBBBBBBBBB";

    private static final Boolean DEFAULT_PAYMENT_LINK_PAYREXX = false;
    private static final Boolean UPDATED_PAYMENT_LINK_PAYREXX = true;

    private static final String DEFAULT_PAYMENT_LINK_PAYREXX_URL = "AAAAAAAAAA";
    private static final String UPDATED_PAYMENT_LINK_PAYREXX_URL = "BBBBBBBBBB";

    private static final Boolean DEFAULT_PAYMENT_LINK_SMARTCOMMERCE = false;
    private static final Boolean UPDATED_PAYMENT_LINK_SMARTCOMMERCE = true;

    private static final String DEFAULT_PAYMENT_LINK_SMARTCOMMERCE_URL = "AAAAAAAAAA";
    private static final String UPDATED_PAYMENT_LINK_SMARTCOMMERCE_URL = "BBBBBBBBBB";

    private static final DocumentLanguage DEFAULT_LANGUAGE = DocumentLanguage.FRENCH;
    private static final DocumentLanguage UPDATED_LANGUAGE = DocumentLanguage.ENGLISH;

    private static final Integer DEFAULT_PAGE_AMOUNT = 1;
    private static final Integer UPDATED_PAGE_AMOUNT = 2;

    private static final String DEFAULT_NOTES = "AAAAAAAAAA";
    private static final String UPDATED_NOTES = "BBBBBBBBBB";

    private static final InvoiceStatus DEFAULT_STATUS = InvoiceStatus.DRAFT;
    private static final InvoiceStatus UPDATED_STATUS = InvoiceStatus.SENT;

    private static final ZonedDateTime DEFAULT_CREATED = ZonedDateTime.ofInstant(Instant.ofEpochMilli(0L), ZoneOffset.UTC);
    private static final ZonedDateTime UPDATED_CREATED = ZonedDateTime.now(ZoneId.systemDefault()).withNano(0);

    private static final String ENTITY_API_URL = "/api/invoices";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private InvoiceRepository invoiceRepository;

    @Autowired
    private InvoiceMapper invoiceMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restInvoiceMockMvc;

    private Invoice invoice;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Invoice createEntity(EntityManager em) {
        Invoice invoice = new Invoice()
            .remoteId(DEFAULT_REMOTE_ID)
            .number(DEFAULT_NUMBER)
            .contactName(DEFAULT_CONTACT_NAME)
            .date(DEFAULT_DATE)
            .due(DEFAULT_DUE)
            .periodFrom(DEFAULT_PERIOD_FROM)
            .periodTo(DEFAULT_PERIOD_TO)
            .periodText(DEFAULT_PERIOD_TEXT)
            .currency(DEFAULT_CURRENCY)
            .total(DEFAULT_TOTAL)
            .vatIncluded(DEFAULT_VAT_INCLUDED)
            .discountRate(DEFAULT_DISCOUNT_RATE)
            .discountType(DEFAULT_DISCOUNT_TYPE)
            .cashDiscountRate(DEFAULT_CASH_DISCOUNT_RATE)
            .cashDiscountDate(DEFAULT_CASH_DISCOUNT_DATE)
            .totalPaid(DEFAULT_TOTAL_PAID)
            .paidDate(DEFAULT_PAID_DATE)
            .isrPosition(DEFAULT_ISR_POSITION)
            .isrReferenceNumber(DEFAULT_ISR_REFERENCE_NUMBER)
            .paymentLinkPaypal(DEFAULT_PAYMENT_LINK_PAYPAL)
            .paymentLinkPaypalUrl(DEFAULT_PAYMENT_LINK_PAYPAL_URL)
            .paymentLinkPostfinance(DEFAULT_PAYMENT_LINK_POSTFINANCE)
            .paymentLinkPostfinanceUrl(DEFAULT_PAYMENT_LINK_POSTFINANCE_URL)
            .paymentLinkPayrexx(DEFAULT_PAYMENT_LINK_PAYREXX)
            .paymentLinkPayrexxUrl(DEFAULT_PAYMENT_LINK_PAYREXX_URL)
            .paymentLinkSmartcommerce(DEFAULT_PAYMENT_LINK_SMARTCOMMERCE)
            .paymentLinkSmartcommerceUrl(DEFAULT_PAYMENT_LINK_SMARTCOMMERCE_URL)
            .language(DEFAULT_LANGUAGE)
            .pageAmount(DEFAULT_PAGE_AMOUNT)
            .notes(DEFAULT_NOTES)
            .status(DEFAULT_STATUS)
            .created(DEFAULT_CREATED);
        return invoice;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Invoice createUpdatedEntity(EntityManager em) {
        Invoice invoice = new Invoice()
            .remoteId(UPDATED_REMOTE_ID)
            .number(UPDATED_NUMBER)
            .contactName(UPDATED_CONTACT_NAME)
            .date(UPDATED_DATE)
            .due(UPDATED_DUE)
            .periodFrom(UPDATED_PERIOD_FROM)
            .periodTo(UPDATED_PERIOD_TO)
            .periodText(UPDATED_PERIOD_TEXT)
            .currency(UPDATED_CURRENCY)
            .total(UPDATED_TOTAL)
            .vatIncluded(UPDATED_VAT_INCLUDED)
            .discountRate(UPDATED_DISCOUNT_RATE)
            .discountType(UPDATED_DISCOUNT_TYPE)
            .cashDiscountRate(UPDATED_CASH_DISCOUNT_RATE)
            .cashDiscountDate(UPDATED_CASH_DISCOUNT_DATE)
            .totalPaid(UPDATED_TOTAL_PAID)
            .paidDate(UPDATED_PAID_DATE)
            .isrPosition(UPDATED_ISR_POSITION)
            .isrReferenceNumber(UPDATED_ISR_REFERENCE_NUMBER)
            .paymentLinkPaypal(UPDATED_PAYMENT_LINK_PAYPAL)
            .paymentLinkPaypalUrl(UPDATED_PAYMENT_LINK_PAYPAL_URL)
            .paymentLinkPostfinance(UPDATED_PAYMENT_LINK_POSTFINANCE)
            .paymentLinkPostfinanceUrl(UPDATED_PAYMENT_LINK_POSTFINANCE_URL)
            .paymentLinkPayrexx(UPDATED_PAYMENT_LINK_PAYREXX)
            .paymentLinkPayrexxUrl(UPDATED_PAYMENT_LINK_PAYREXX_URL)
            .paymentLinkSmartcommerce(UPDATED_PAYMENT_LINK_SMARTCOMMERCE)
            .paymentLinkSmartcommerceUrl(UPDATED_PAYMENT_LINK_SMARTCOMMERCE_URL)
            .language(UPDATED_LANGUAGE)
            .pageAmount(UPDATED_PAGE_AMOUNT)
            .notes(UPDATED_NOTES)
            .status(UPDATED_STATUS)
            .created(UPDATED_CREATED);
        return invoice;
    }

    @BeforeEach
    public void initTest() {
        invoice = createEntity(em);
    }

    @Test
    @Transactional
    void createInvoice() throws Exception {
        int databaseSizeBeforeCreate = invoiceRepository.findAll().size();
        // Create the Invoice
        InvoiceDTO invoiceDTO = invoiceMapper.toDto(invoice);
        restInvoiceMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(invoiceDTO)))
            .andExpect(status().isCreated());

        // Validate the Invoice in the database
        List<Invoice> invoiceList = invoiceRepository.findAll();
        assertThat(invoiceList).hasSize(databaseSizeBeforeCreate + 1);
        Invoice testInvoice = invoiceList.get(invoiceList.size() - 1);
        assertThat(testInvoice.getRemoteId()).isEqualTo(DEFAULT_REMOTE_ID);
        assertThat(testInvoice.getNumber()).isEqualTo(DEFAULT_NUMBER);
        assertThat(testInvoice.getContactName()).isEqualTo(DEFAULT_CONTACT_NAME);
        assertThat(testInvoice.getDate()).isEqualTo(DEFAULT_DATE);
        assertThat(testInvoice.getDue()).isEqualTo(DEFAULT_DUE);
        assertThat(testInvoice.getPeriodFrom()).isEqualTo(DEFAULT_PERIOD_FROM);
        assertThat(testInvoice.getPeriodTo()).isEqualTo(DEFAULT_PERIOD_TO);
        assertThat(testInvoice.getPeriodText()).isEqualTo(DEFAULT_PERIOD_TEXT);
        assertThat(testInvoice.getCurrency()).isEqualTo(DEFAULT_CURRENCY);
        assertThat(testInvoice.getTotal()).isEqualTo(DEFAULT_TOTAL);
        assertThat(testInvoice.getVatIncluded()).isEqualTo(DEFAULT_VAT_INCLUDED);
        assertThat(testInvoice.getDiscountRate()).isEqualTo(DEFAULT_DISCOUNT_RATE);
        assertThat(testInvoice.getDiscountType()).isEqualTo(DEFAULT_DISCOUNT_TYPE);
        assertThat(testInvoice.getCashDiscountRate()).isEqualTo(DEFAULT_CASH_DISCOUNT_RATE);
        assertThat(testInvoice.getCashDiscountDate()).isEqualTo(DEFAULT_CASH_DISCOUNT_DATE);
        assertThat(testInvoice.getTotalPaid()).isEqualTo(DEFAULT_TOTAL_PAID);
        assertThat(testInvoice.getPaidDate()).isEqualTo(DEFAULT_PAID_DATE);
        assertThat(testInvoice.getIsrPosition()).isEqualTo(DEFAULT_ISR_POSITION);
        assertThat(testInvoice.getIsrReferenceNumber()).isEqualTo(DEFAULT_ISR_REFERENCE_NUMBER);
        assertThat(testInvoice.getPaymentLinkPaypal()).isEqualTo(DEFAULT_PAYMENT_LINK_PAYPAL);
        assertThat(testInvoice.getPaymentLinkPaypalUrl()).isEqualTo(DEFAULT_PAYMENT_LINK_PAYPAL_URL);
        assertThat(testInvoice.getPaymentLinkPostfinance()).isEqualTo(DEFAULT_PAYMENT_LINK_POSTFINANCE);
        assertThat(testInvoice.getPaymentLinkPostfinanceUrl()).isEqualTo(DEFAULT_PAYMENT_LINK_POSTFINANCE_URL);
        assertThat(testInvoice.getPaymentLinkPayrexx()).isEqualTo(DEFAULT_PAYMENT_LINK_PAYREXX);
        assertThat(testInvoice.getPaymentLinkPayrexxUrl()).isEqualTo(DEFAULT_PAYMENT_LINK_PAYREXX_URL);
        assertThat(testInvoice.getPaymentLinkSmartcommerce()).isEqualTo(DEFAULT_PAYMENT_LINK_SMARTCOMMERCE);
        assertThat(testInvoice.getPaymentLinkSmartcommerceUrl()).isEqualTo(DEFAULT_PAYMENT_LINK_SMARTCOMMERCE_URL);
        assertThat(testInvoice.getLanguage()).isEqualTo(DEFAULT_LANGUAGE);
        assertThat(testInvoice.getPageAmount()).isEqualTo(DEFAULT_PAGE_AMOUNT);
        assertThat(testInvoice.getNotes()).isEqualTo(DEFAULT_NOTES);
        assertThat(testInvoice.getStatus()).isEqualTo(DEFAULT_STATUS);
        assertThat(testInvoice.getCreated()).isEqualTo(DEFAULT_CREATED);
    }

    @Test
    @Transactional
    void createInvoiceWithExistingId() throws Exception {
        // Create the Invoice with an existing ID
        invoice.setId(1L);
        InvoiceDTO invoiceDTO = invoiceMapper.toDto(invoice);

        int databaseSizeBeforeCreate = invoiceRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restInvoiceMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(invoiceDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Invoice in the database
        List<Invoice> invoiceList = invoiceRepository.findAll();
        assertThat(invoiceList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllInvoices() throws Exception {
        // Initialize the database
        invoiceRepository.saveAndFlush(invoice);

        // Get all the invoiceList
        restInvoiceMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(invoice.getId().intValue())))
            .andExpect(jsonPath("$.[*].remoteId").value(hasItem(DEFAULT_REMOTE_ID)))
            .andExpect(jsonPath("$.[*].number").value(hasItem(DEFAULT_NUMBER)))
            .andExpect(jsonPath("$.[*].contactName").value(hasItem(DEFAULT_CONTACT_NAME)))
            .andExpect(jsonPath("$.[*].date").value(hasItem(DEFAULT_DATE.toString())))
            .andExpect(jsonPath("$.[*].due").value(hasItem(DEFAULT_DUE.toString())))
            .andExpect(jsonPath("$.[*].periodFrom").value(hasItem(DEFAULT_PERIOD_FROM.toString())))
            .andExpect(jsonPath("$.[*].periodTo").value(hasItem(DEFAULT_PERIOD_TO.toString())))
            .andExpect(jsonPath("$.[*].periodText").value(hasItem(DEFAULT_PERIOD_TEXT)))
            .andExpect(jsonPath("$.[*].currency").value(hasItem(DEFAULT_CURRENCY.toString())))
            .andExpect(jsonPath("$.[*].total").value(hasItem(DEFAULT_TOTAL.doubleValue())))
            .andExpect(jsonPath("$.[*].vatIncluded").value(hasItem(DEFAULT_VAT_INCLUDED.booleanValue())))
            .andExpect(jsonPath("$.[*].discountRate").value(hasItem(DEFAULT_DISCOUNT_RATE.doubleValue())))
            .andExpect(jsonPath("$.[*].discountType").value(hasItem(DEFAULT_DISCOUNT_TYPE.toString())))
            .andExpect(jsonPath("$.[*].cashDiscountRate").value(hasItem(DEFAULT_CASH_DISCOUNT_RATE)))
            .andExpect(jsonPath("$.[*].cashDiscountDate").value(hasItem(DEFAULT_CASH_DISCOUNT_DATE.toString())))
            .andExpect(jsonPath("$.[*].totalPaid").value(hasItem(DEFAULT_TOTAL_PAID.doubleValue())))
            .andExpect(jsonPath("$.[*].paidDate").value(hasItem(DEFAULT_PAID_DATE)))
            .andExpect(jsonPath("$.[*].isrPosition").value(hasItem(DEFAULT_ISR_POSITION.toString())))
            .andExpect(jsonPath("$.[*].isrReferenceNumber").value(hasItem(DEFAULT_ISR_REFERENCE_NUMBER)))
            .andExpect(jsonPath("$.[*].paymentLinkPaypal").value(hasItem(DEFAULT_PAYMENT_LINK_PAYPAL.booleanValue())))
            .andExpect(jsonPath("$.[*].paymentLinkPaypalUrl").value(hasItem(DEFAULT_PAYMENT_LINK_PAYPAL_URL)))
            .andExpect(jsonPath("$.[*].paymentLinkPostfinance").value(hasItem(DEFAULT_PAYMENT_LINK_POSTFINANCE.booleanValue())))
            .andExpect(jsonPath("$.[*].paymentLinkPostfinanceUrl").value(hasItem(DEFAULT_PAYMENT_LINK_POSTFINANCE_URL)))
            .andExpect(jsonPath("$.[*].paymentLinkPayrexx").value(hasItem(DEFAULT_PAYMENT_LINK_PAYREXX.booleanValue())))
            .andExpect(jsonPath("$.[*].paymentLinkPayrexxUrl").value(hasItem(DEFAULT_PAYMENT_LINK_PAYREXX_URL)))
            .andExpect(jsonPath("$.[*].paymentLinkSmartcommerce").value(hasItem(DEFAULT_PAYMENT_LINK_SMARTCOMMERCE.booleanValue())))
            .andExpect(jsonPath("$.[*].paymentLinkSmartcommerceUrl").value(hasItem(DEFAULT_PAYMENT_LINK_SMARTCOMMERCE_URL)))
            .andExpect(jsonPath("$.[*].language").value(hasItem(DEFAULT_LANGUAGE.toString())))
            .andExpect(jsonPath("$.[*].pageAmount").value(hasItem(DEFAULT_PAGE_AMOUNT)))
            .andExpect(jsonPath("$.[*].notes").value(hasItem(DEFAULT_NOTES)))
            .andExpect(jsonPath("$.[*].status").value(hasItem(DEFAULT_STATUS.toString())))
            .andExpect(jsonPath("$.[*].created").value(hasItem(sameInstant(DEFAULT_CREATED))));
    }

    @Test
    @Transactional
    void getInvoice() throws Exception {
        // Initialize the database
        invoiceRepository.saveAndFlush(invoice);

        // Get the invoice
        restInvoiceMockMvc
            .perform(get(ENTITY_API_URL_ID, invoice.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(invoice.getId().intValue()))
            .andExpect(jsonPath("$.remoteId").value(DEFAULT_REMOTE_ID))
            .andExpect(jsonPath("$.number").value(DEFAULT_NUMBER))
            .andExpect(jsonPath("$.contactName").value(DEFAULT_CONTACT_NAME))
            .andExpect(jsonPath("$.date").value(DEFAULT_DATE.toString()))
            .andExpect(jsonPath("$.due").value(DEFAULT_DUE.toString()))
            .andExpect(jsonPath("$.periodFrom").value(DEFAULT_PERIOD_FROM.toString()))
            .andExpect(jsonPath("$.periodTo").value(DEFAULT_PERIOD_TO.toString()))
            .andExpect(jsonPath("$.periodText").value(DEFAULT_PERIOD_TEXT))
            .andExpect(jsonPath("$.currency").value(DEFAULT_CURRENCY.toString()))
            .andExpect(jsonPath("$.total").value(DEFAULT_TOTAL.doubleValue()))
            .andExpect(jsonPath("$.vatIncluded").value(DEFAULT_VAT_INCLUDED.booleanValue()))
            .andExpect(jsonPath("$.discountRate").value(DEFAULT_DISCOUNT_RATE.doubleValue()))
            .andExpect(jsonPath("$.discountType").value(DEFAULT_DISCOUNT_TYPE.toString()))
            .andExpect(jsonPath("$.cashDiscountRate").value(DEFAULT_CASH_DISCOUNT_RATE))
            .andExpect(jsonPath("$.cashDiscountDate").value(DEFAULT_CASH_DISCOUNT_DATE.toString()))
            .andExpect(jsonPath("$.totalPaid").value(DEFAULT_TOTAL_PAID.doubleValue()))
            .andExpect(jsonPath("$.paidDate").value(DEFAULT_PAID_DATE))
            .andExpect(jsonPath("$.isrPosition").value(DEFAULT_ISR_POSITION.toString()))
            .andExpect(jsonPath("$.isrReferenceNumber").value(DEFAULT_ISR_REFERENCE_NUMBER))
            .andExpect(jsonPath("$.paymentLinkPaypal").value(DEFAULT_PAYMENT_LINK_PAYPAL.booleanValue()))
            .andExpect(jsonPath("$.paymentLinkPaypalUrl").value(DEFAULT_PAYMENT_LINK_PAYPAL_URL))
            .andExpect(jsonPath("$.paymentLinkPostfinance").value(DEFAULT_PAYMENT_LINK_POSTFINANCE.booleanValue()))
            .andExpect(jsonPath("$.paymentLinkPostfinanceUrl").value(DEFAULT_PAYMENT_LINK_POSTFINANCE_URL))
            .andExpect(jsonPath("$.paymentLinkPayrexx").value(DEFAULT_PAYMENT_LINK_PAYREXX.booleanValue()))
            .andExpect(jsonPath("$.paymentLinkPayrexxUrl").value(DEFAULT_PAYMENT_LINK_PAYREXX_URL))
            .andExpect(jsonPath("$.paymentLinkSmartcommerce").value(DEFAULT_PAYMENT_LINK_SMARTCOMMERCE.booleanValue()))
            .andExpect(jsonPath("$.paymentLinkSmartcommerceUrl").value(DEFAULT_PAYMENT_LINK_SMARTCOMMERCE_URL))
            .andExpect(jsonPath("$.language").value(DEFAULT_LANGUAGE.toString()))
            .andExpect(jsonPath("$.pageAmount").value(DEFAULT_PAGE_AMOUNT))
            .andExpect(jsonPath("$.notes").value(DEFAULT_NOTES))
            .andExpect(jsonPath("$.status").value(DEFAULT_STATUS.toString()))
            .andExpect(jsonPath("$.created").value(sameInstant(DEFAULT_CREATED)));
    }

    @Test
    @Transactional
    void getNonExistingInvoice() throws Exception {
        // Get the invoice
        restInvoiceMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewInvoice() throws Exception {
        // Initialize the database
        invoiceRepository.saveAndFlush(invoice);

        int databaseSizeBeforeUpdate = invoiceRepository.findAll().size();

        // Update the invoice
        Invoice updatedInvoice = invoiceRepository.findById(invoice.getId()).get();
        // Disconnect from session so that the updates on updatedInvoice are not directly saved in db
        em.detach(updatedInvoice);
        updatedInvoice
            .remoteId(UPDATED_REMOTE_ID)
            .number(UPDATED_NUMBER)
            .contactName(UPDATED_CONTACT_NAME)
            .date(UPDATED_DATE)
            .due(UPDATED_DUE)
            .periodFrom(UPDATED_PERIOD_FROM)
            .periodTo(UPDATED_PERIOD_TO)
            .periodText(UPDATED_PERIOD_TEXT)
            .currency(UPDATED_CURRENCY)
            .total(UPDATED_TOTAL)
            .vatIncluded(UPDATED_VAT_INCLUDED)
            .discountRate(UPDATED_DISCOUNT_RATE)
            .discountType(UPDATED_DISCOUNT_TYPE)
            .cashDiscountRate(UPDATED_CASH_DISCOUNT_RATE)
            .cashDiscountDate(UPDATED_CASH_DISCOUNT_DATE)
            .totalPaid(UPDATED_TOTAL_PAID)
            .paidDate(UPDATED_PAID_DATE)
            .isrPosition(UPDATED_ISR_POSITION)
            .isrReferenceNumber(UPDATED_ISR_REFERENCE_NUMBER)
            .paymentLinkPaypal(UPDATED_PAYMENT_LINK_PAYPAL)
            .paymentLinkPaypalUrl(UPDATED_PAYMENT_LINK_PAYPAL_URL)
            .paymentLinkPostfinance(UPDATED_PAYMENT_LINK_POSTFINANCE)
            .paymentLinkPostfinanceUrl(UPDATED_PAYMENT_LINK_POSTFINANCE_URL)
            .paymentLinkPayrexx(UPDATED_PAYMENT_LINK_PAYREXX)
            .paymentLinkPayrexxUrl(UPDATED_PAYMENT_LINK_PAYREXX_URL)
            .paymentLinkSmartcommerce(UPDATED_PAYMENT_LINK_SMARTCOMMERCE)
            .paymentLinkSmartcommerceUrl(UPDATED_PAYMENT_LINK_SMARTCOMMERCE_URL)
            .language(UPDATED_LANGUAGE)
            .pageAmount(UPDATED_PAGE_AMOUNT)
            .notes(UPDATED_NOTES)
            .status(UPDATED_STATUS)
            .created(UPDATED_CREATED);
        InvoiceDTO invoiceDTO = invoiceMapper.toDto(updatedInvoice);

        restInvoiceMockMvc
            .perform(
                put(ENTITY_API_URL_ID, invoiceDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(invoiceDTO))
            )
            .andExpect(status().isOk());

        // Validate the Invoice in the database
        List<Invoice> invoiceList = invoiceRepository.findAll();
        assertThat(invoiceList).hasSize(databaseSizeBeforeUpdate);
        Invoice testInvoice = invoiceList.get(invoiceList.size() - 1);
        assertThat(testInvoice.getRemoteId()).isEqualTo(UPDATED_REMOTE_ID);
        assertThat(testInvoice.getNumber()).isEqualTo(UPDATED_NUMBER);
        assertThat(testInvoice.getContactName()).isEqualTo(UPDATED_CONTACT_NAME);
        assertThat(testInvoice.getDate()).isEqualTo(UPDATED_DATE);
        assertThat(testInvoice.getDue()).isEqualTo(UPDATED_DUE);
        assertThat(testInvoice.getPeriodFrom()).isEqualTo(UPDATED_PERIOD_FROM);
        assertThat(testInvoice.getPeriodTo()).isEqualTo(UPDATED_PERIOD_TO);
        assertThat(testInvoice.getPeriodText()).isEqualTo(UPDATED_PERIOD_TEXT);
        assertThat(testInvoice.getCurrency()).isEqualTo(UPDATED_CURRENCY);
        assertThat(testInvoice.getTotal()).isEqualTo(UPDATED_TOTAL);
        assertThat(testInvoice.getVatIncluded()).isEqualTo(UPDATED_VAT_INCLUDED);
        assertThat(testInvoice.getDiscountRate()).isEqualTo(UPDATED_DISCOUNT_RATE);
        assertThat(testInvoice.getDiscountType()).isEqualTo(UPDATED_DISCOUNT_TYPE);
        assertThat(testInvoice.getCashDiscountRate()).isEqualTo(UPDATED_CASH_DISCOUNT_RATE);
        assertThat(testInvoice.getCashDiscountDate()).isEqualTo(UPDATED_CASH_DISCOUNT_DATE);
        assertThat(testInvoice.getTotalPaid()).isEqualTo(UPDATED_TOTAL_PAID);
        assertThat(testInvoice.getPaidDate()).isEqualTo(UPDATED_PAID_DATE);
        assertThat(testInvoice.getIsrPosition()).isEqualTo(UPDATED_ISR_POSITION);
        assertThat(testInvoice.getIsrReferenceNumber()).isEqualTo(UPDATED_ISR_REFERENCE_NUMBER);
        assertThat(testInvoice.getPaymentLinkPaypal()).isEqualTo(UPDATED_PAYMENT_LINK_PAYPAL);
        assertThat(testInvoice.getPaymentLinkPaypalUrl()).isEqualTo(UPDATED_PAYMENT_LINK_PAYPAL_URL);
        assertThat(testInvoice.getPaymentLinkPostfinance()).isEqualTo(UPDATED_PAYMENT_LINK_POSTFINANCE);
        assertThat(testInvoice.getPaymentLinkPostfinanceUrl()).isEqualTo(UPDATED_PAYMENT_LINK_POSTFINANCE_URL);
        assertThat(testInvoice.getPaymentLinkPayrexx()).isEqualTo(UPDATED_PAYMENT_LINK_PAYREXX);
        assertThat(testInvoice.getPaymentLinkPayrexxUrl()).isEqualTo(UPDATED_PAYMENT_LINK_PAYREXX_URL);
        assertThat(testInvoice.getPaymentLinkSmartcommerce()).isEqualTo(UPDATED_PAYMENT_LINK_SMARTCOMMERCE);
        assertThat(testInvoice.getPaymentLinkSmartcommerceUrl()).isEqualTo(UPDATED_PAYMENT_LINK_SMARTCOMMERCE_URL);
        assertThat(testInvoice.getLanguage()).isEqualTo(UPDATED_LANGUAGE);
        assertThat(testInvoice.getPageAmount()).isEqualTo(UPDATED_PAGE_AMOUNT);
        assertThat(testInvoice.getNotes()).isEqualTo(UPDATED_NOTES);
        assertThat(testInvoice.getStatus()).isEqualTo(UPDATED_STATUS);
        assertThat(testInvoice.getCreated()).isEqualTo(UPDATED_CREATED);
    }

    @Test
    @Transactional
    void putNonExistingInvoice() throws Exception {
        int databaseSizeBeforeUpdate = invoiceRepository.findAll().size();
        invoice.setId(count.incrementAndGet());

        // Create the Invoice
        InvoiceDTO invoiceDTO = invoiceMapper.toDto(invoice);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restInvoiceMockMvc
            .perform(
                put(ENTITY_API_URL_ID, invoiceDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(invoiceDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Invoice in the database
        List<Invoice> invoiceList = invoiceRepository.findAll();
        assertThat(invoiceList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchInvoice() throws Exception {
        int databaseSizeBeforeUpdate = invoiceRepository.findAll().size();
        invoice.setId(count.incrementAndGet());

        // Create the Invoice
        InvoiceDTO invoiceDTO = invoiceMapper.toDto(invoice);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restInvoiceMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(invoiceDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Invoice in the database
        List<Invoice> invoiceList = invoiceRepository.findAll();
        assertThat(invoiceList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamInvoice() throws Exception {
        int databaseSizeBeforeUpdate = invoiceRepository.findAll().size();
        invoice.setId(count.incrementAndGet());

        // Create the Invoice
        InvoiceDTO invoiceDTO = invoiceMapper.toDto(invoice);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restInvoiceMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(invoiceDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Invoice in the database
        List<Invoice> invoiceList = invoiceRepository.findAll();
        assertThat(invoiceList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateInvoiceWithPatch() throws Exception {
        // Initialize the database
        invoiceRepository.saveAndFlush(invoice);

        int databaseSizeBeforeUpdate = invoiceRepository.findAll().size();

        // Update the invoice using partial update
        Invoice partialUpdatedInvoice = new Invoice();
        partialUpdatedInvoice.setId(invoice.getId());

        partialUpdatedInvoice
            .contactName(UPDATED_CONTACT_NAME)
            .date(UPDATED_DATE)
            .due(UPDATED_DUE)
            .currency(UPDATED_CURRENCY)
            .total(UPDATED_TOTAL)
            .vatIncluded(UPDATED_VAT_INCLUDED)
            .cashDiscountRate(UPDATED_CASH_DISCOUNT_RATE)
            .paidDate(UPDATED_PAID_DATE)
            .paymentLinkPaypal(UPDATED_PAYMENT_LINK_PAYPAL)
            .paymentLinkPaypalUrl(UPDATED_PAYMENT_LINK_PAYPAL_URL)
            .paymentLinkPostfinanceUrl(UPDATED_PAYMENT_LINK_POSTFINANCE_URL)
            .paymentLinkPayrexxUrl(UPDATED_PAYMENT_LINK_PAYREXX_URL)
            .paymentLinkSmartcommerce(UPDATED_PAYMENT_LINK_SMARTCOMMERCE)
            .paymentLinkSmartcommerceUrl(UPDATED_PAYMENT_LINK_SMARTCOMMERCE_URL)
            .language(UPDATED_LANGUAGE);

        restInvoiceMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedInvoice.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedInvoice))
            )
            .andExpect(status().isOk());

        // Validate the Invoice in the database
        List<Invoice> invoiceList = invoiceRepository.findAll();
        assertThat(invoiceList).hasSize(databaseSizeBeforeUpdate);
        Invoice testInvoice = invoiceList.get(invoiceList.size() - 1);
        assertThat(testInvoice.getRemoteId()).isEqualTo(DEFAULT_REMOTE_ID);
        assertThat(testInvoice.getNumber()).isEqualTo(DEFAULT_NUMBER);
        assertThat(testInvoice.getContactName()).isEqualTo(UPDATED_CONTACT_NAME);
        assertThat(testInvoice.getDate()).isEqualTo(UPDATED_DATE);
        assertThat(testInvoice.getDue()).isEqualTo(UPDATED_DUE);
        assertThat(testInvoice.getPeriodFrom()).isEqualTo(DEFAULT_PERIOD_FROM);
        assertThat(testInvoice.getPeriodTo()).isEqualTo(DEFAULT_PERIOD_TO);
        assertThat(testInvoice.getPeriodText()).isEqualTo(DEFAULT_PERIOD_TEXT);
        assertThat(testInvoice.getCurrency()).isEqualTo(UPDATED_CURRENCY);
        assertThat(testInvoice.getTotal()).isEqualTo(UPDATED_TOTAL);
        assertThat(testInvoice.getVatIncluded()).isEqualTo(UPDATED_VAT_INCLUDED);
        assertThat(testInvoice.getDiscountRate()).isEqualTo(DEFAULT_DISCOUNT_RATE);
        assertThat(testInvoice.getDiscountType()).isEqualTo(DEFAULT_DISCOUNT_TYPE);
        assertThat(testInvoice.getCashDiscountRate()).isEqualTo(UPDATED_CASH_DISCOUNT_RATE);
        assertThat(testInvoice.getCashDiscountDate()).isEqualTo(DEFAULT_CASH_DISCOUNT_DATE);
        assertThat(testInvoice.getTotalPaid()).isEqualTo(DEFAULT_TOTAL_PAID);
        assertThat(testInvoice.getPaidDate()).isEqualTo(UPDATED_PAID_DATE);
        assertThat(testInvoice.getIsrPosition()).isEqualTo(DEFAULT_ISR_POSITION);
        assertThat(testInvoice.getIsrReferenceNumber()).isEqualTo(DEFAULT_ISR_REFERENCE_NUMBER);
        assertThat(testInvoice.getPaymentLinkPaypal()).isEqualTo(UPDATED_PAYMENT_LINK_PAYPAL);
        assertThat(testInvoice.getPaymentLinkPaypalUrl()).isEqualTo(UPDATED_PAYMENT_LINK_PAYPAL_URL);
        assertThat(testInvoice.getPaymentLinkPostfinance()).isEqualTo(DEFAULT_PAYMENT_LINK_POSTFINANCE);
        assertThat(testInvoice.getPaymentLinkPostfinanceUrl()).isEqualTo(UPDATED_PAYMENT_LINK_POSTFINANCE_URL);
        assertThat(testInvoice.getPaymentLinkPayrexx()).isEqualTo(DEFAULT_PAYMENT_LINK_PAYREXX);
        assertThat(testInvoice.getPaymentLinkPayrexxUrl()).isEqualTo(UPDATED_PAYMENT_LINK_PAYREXX_URL);
        assertThat(testInvoice.getPaymentLinkSmartcommerce()).isEqualTo(UPDATED_PAYMENT_LINK_SMARTCOMMERCE);
        assertThat(testInvoice.getPaymentLinkSmartcommerceUrl()).isEqualTo(UPDATED_PAYMENT_LINK_SMARTCOMMERCE_URL);
        assertThat(testInvoice.getLanguage()).isEqualTo(UPDATED_LANGUAGE);
        assertThat(testInvoice.getPageAmount()).isEqualTo(DEFAULT_PAGE_AMOUNT);
        assertThat(testInvoice.getNotes()).isEqualTo(DEFAULT_NOTES);
        assertThat(testInvoice.getStatus()).isEqualTo(DEFAULT_STATUS);
        assertThat(testInvoice.getCreated()).isEqualTo(DEFAULT_CREATED);
    }

    @Test
    @Transactional
    void fullUpdateInvoiceWithPatch() throws Exception {
        // Initialize the database
        invoiceRepository.saveAndFlush(invoice);

        int databaseSizeBeforeUpdate = invoiceRepository.findAll().size();

        // Update the invoice using partial update
        Invoice partialUpdatedInvoice = new Invoice();
        partialUpdatedInvoice.setId(invoice.getId());

        partialUpdatedInvoice
            .remoteId(UPDATED_REMOTE_ID)
            .number(UPDATED_NUMBER)
            .contactName(UPDATED_CONTACT_NAME)
            .date(UPDATED_DATE)
            .due(UPDATED_DUE)
            .periodFrom(UPDATED_PERIOD_FROM)
            .periodTo(UPDATED_PERIOD_TO)
            .periodText(UPDATED_PERIOD_TEXT)
            .currency(UPDATED_CURRENCY)
            .total(UPDATED_TOTAL)
            .vatIncluded(UPDATED_VAT_INCLUDED)
            .discountRate(UPDATED_DISCOUNT_RATE)
            .discountType(UPDATED_DISCOUNT_TYPE)
            .cashDiscountRate(UPDATED_CASH_DISCOUNT_RATE)
            .cashDiscountDate(UPDATED_CASH_DISCOUNT_DATE)
            .totalPaid(UPDATED_TOTAL_PAID)
            .paidDate(UPDATED_PAID_DATE)
            .isrPosition(UPDATED_ISR_POSITION)
            .isrReferenceNumber(UPDATED_ISR_REFERENCE_NUMBER)
            .paymentLinkPaypal(UPDATED_PAYMENT_LINK_PAYPAL)
            .paymentLinkPaypalUrl(UPDATED_PAYMENT_LINK_PAYPAL_URL)
            .paymentLinkPostfinance(UPDATED_PAYMENT_LINK_POSTFINANCE)
            .paymentLinkPostfinanceUrl(UPDATED_PAYMENT_LINK_POSTFINANCE_URL)
            .paymentLinkPayrexx(UPDATED_PAYMENT_LINK_PAYREXX)
            .paymentLinkPayrexxUrl(UPDATED_PAYMENT_LINK_PAYREXX_URL)
            .paymentLinkSmartcommerce(UPDATED_PAYMENT_LINK_SMARTCOMMERCE)
            .paymentLinkSmartcommerceUrl(UPDATED_PAYMENT_LINK_SMARTCOMMERCE_URL)
            .language(UPDATED_LANGUAGE)
            .pageAmount(UPDATED_PAGE_AMOUNT)
            .notes(UPDATED_NOTES)
            .status(UPDATED_STATUS)
            .created(UPDATED_CREATED);

        restInvoiceMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedInvoice.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedInvoice))
            )
            .andExpect(status().isOk());

        // Validate the Invoice in the database
        List<Invoice> invoiceList = invoiceRepository.findAll();
        assertThat(invoiceList).hasSize(databaseSizeBeforeUpdate);
        Invoice testInvoice = invoiceList.get(invoiceList.size() - 1);
        assertThat(testInvoice.getRemoteId()).isEqualTo(UPDATED_REMOTE_ID);
        assertThat(testInvoice.getNumber()).isEqualTo(UPDATED_NUMBER);
        assertThat(testInvoice.getContactName()).isEqualTo(UPDATED_CONTACT_NAME);
        assertThat(testInvoice.getDate()).isEqualTo(UPDATED_DATE);
        assertThat(testInvoice.getDue()).isEqualTo(UPDATED_DUE);
        assertThat(testInvoice.getPeriodFrom()).isEqualTo(UPDATED_PERIOD_FROM);
        assertThat(testInvoice.getPeriodTo()).isEqualTo(UPDATED_PERIOD_TO);
        assertThat(testInvoice.getPeriodText()).isEqualTo(UPDATED_PERIOD_TEXT);
        assertThat(testInvoice.getCurrency()).isEqualTo(UPDATED_CURRENCY);
        assertThat(testInvoice.getTotal()).isEqualTo(UPDATED_TOTAL);
        assertThat(testInvoice.getVatIncluded()).isEqualTo(UPDATED_VAT_INCLUDED);
        assertThat(testInvoice.getDiscountRate()).isEqualTo(UPDATED_DISCOUNT_RATE);
        assertThat(testInvoice.getDiscountType()).isEqualTo(UPDATED_DISCOUNT_TYPE);
        assertThat(testInvoice.getCashDiscountRate()).isEqualTo(UPDATED_CASH_DISCOUNT_RATE);
        assertThat(testInvoice.getCashDiscountDate()).isEqualTo(UPDATED_CASH_DISCOUNT_DATE);
        assertThat(testInvoice.getTotalPaid()).isEqualTo(UPDATED_TOTAL_PAID);
        assertThat(testInvoice.getPaidDate()).isEqualTo(UPDATED_PAID_DATE);
        assertThat(testInvoice.getIsrPosition()).isEqualTo(UPDATED_ISR_POSITION);
        assertThat(testInvoice.getIsrReferenceNumber()).isEqualTo(UPDATED_ISR_REFERENCE_NUMBER);
        assertThat(testInvoice.getPaymentLinkPaypal()).isEqualTo(UPDATED_PAYMENT_LINK_PAYPAL);
        assertThat(testInvoice.getPaymentLinkPaypalUrl()).isEqualTo(UPDATED_PAYMENT_LINK_PAYPAL_URL);
        assertThat(testInvoice.getPaymentLinkPostfinance()).isEqualTo(UPDATED_PAYMENT_LINK_POSTFINANCE);
        assertThat(testInvoice.getPaymentLinkPostfinanceUrl()).isEqualTo(UPDATED_PAYMENT_LINK_POSTFINANCE_URL);
        assertThat(testInvoice.getPaymentLinkPayrexx()).isEqualTo(UPDATED_PAYMENT_LINK_PAYREXX);
        assertThat(testInvoice.getPaymentLinkPayrexxUrl()).isEqualTo(UPDATED_PAYMENT_LINK_PAYREXX_URL);
        assertThat(testInvoice.getPaymentLinkSmartcommerce()).isEqualTo(UPDATED_PAYMENT_LINK_SMARTCOMMERCE);
        assertThat(testInvoice.getPaymentLinkSmartcommerceUrl()).isEqualTo(UPDATED_PAYMENT_LINK_SMARTCOMMERCE_URL);
        assertThat(testInvoice.getLanguage()).isEqualTo(UPDATED_LANGUAGE);
        assertThat(testInvoice.getPageAmount()).isEqualTo(UPDATED_PAGE_AMOUNT);
        assertThat(testInvoice.getNotes()).isEqualTo(UPDATED_NOTES);
        assertThat(testInvoice.getStatus()).isEqualTo(UPDATED_STATUS);
        assertThat(testInvoice.getCreated()).isEqualTo(UPDATED_CREATED);
    }

    @Test
    @Transactional
    void patchNonExistingInvoice() throws Exception {
        int databaseSizeBeforeUpdate = invoiceRepository.findAll().size();
        invoice.setId(count.incrementAndGet());

        // Create the Invoice
        InvoiceDTO invoiceDTO = invoiceMapper.toDto(invoice);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restInvoiceMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, invoiceDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(invoiceDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Invoice in the database
        List<Invoice> invoiceList = invoiceRepository.findAll();
        assertThat(invoiceList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchInvoice() throws Exception {
        int databaseSizeBeforeUpdate = invoiceRepository.findAll().size();
        invoice.setId(count.incrementAndGet());

        // Create the Invoice
        InvoiceDTO invoiceDTO = invoiceMapper.toDto(invoice);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restInvoiceMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(invoiceDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Invoice in the database
        List<Invoice> invoiceList = invoiceRepository.findAll();
        assertThat(invoiceList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamInvoice() throws Exception {
        int databaseSizeBeforeUpdate = invoiceRepository.findAll().size();
        invoice.setId(count.incrementAndGet());

        // Create the Invoice
        InvoiceDTO invoiceDTO = invoiceMapper.toDto(invoice);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restInvoiceMockMvc
            .perform(
                patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(invoiceDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the Invoice in the database
        List<Invoice> invoiceList = invoiceRepository.findAll();
        assertThat(invoiceList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteInvoice() throws Exception {
        // Initialize the database
        invoiceRepository.saveAndFlush(invoice);

        int databaseSizeBeforeDelete = invoiceRepository.findAll().size();

        // Delete the invoice
        restInvoiceMockMvc
            .perform(delete(ENTITY_API_URL_ID, invoice.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Invoice> invoiceList = invoiceRepository.findAll();
        assertThat(invoiceList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
