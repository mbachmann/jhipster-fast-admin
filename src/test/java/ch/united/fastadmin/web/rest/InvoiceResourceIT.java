package ch.united.fastadmin.web.rest;

import static ch.united.fastadmin.web.rest.TestUtil.sameInstant;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import ch.united.fastadmin.IntegrationTest;
import ch.united.fastadmin.domain.BankAccount;
import ch.united.fastadmin.domain.Contact;
import ch.united.fastadmin.domain.ContactAddress;
import ch.united.fastadmin.domain.ContactPerson;
import ch.united.fastadmin.domain.DescriptiveDocumentText;
import ch.united.fastadmin.domain.DocumentFreeText;
import ch.united.fastadmin.domain.DocumentPosition;
import ch.united.fastadmin.domain.Invoice;
import ch.united.fastadmin.domain.Isr;
import ch.united.fastadmin.domain.Layout;
import ch.united.fastadmin.domain.Signature;
import ch.united.fastadmin.domain.enumeration.Currency;
import ch.united.fastadmin.domain.enumeration.DiscountType;
import ch.united.fastadmin.domain.enumeration.DocumentLanguage;
import ch.united.fastadmin.domain.enumeration.InvoiceStatus;
import ch.united.fastadmin.domain.enumeration.IsrPosition;
import ch.united.fastadmin.repository.InvoiceRepository;
import ch.united.fastadmin.service.criteria.InvoiceCriteria;
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
    private static final Integer SMALLER_REMOTE_ID = 1 - 1;

    private static final String DEFAULT_NUMBER = "AAAAAAAAAA";
    private static final String UPDATED_NUMBER = "BBBBBBBBBB";

    private static final String DEFAULT_CONTACT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_CONTACT_NAME = "BBBBBBBBBB";

    private static final LocalDate DEFAULT_DATE = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_DATE = LocalDate.now(ZoneId.systemDefault());
    private static final LocalDate SMALLER_DATE = LocalDate.ofEpochDay(-1L);

    private static final LocalDate DEFAULT_DUE = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_DUE = LocalDate.now(ZoneId.systemDefault());
    private static final LocalDate SMALLER_DUE = LocalDate.ofEpochDay(-1L);

    private static final LocalDate DEFAULT_PERIOD_FROM = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_PERIOD_FROM = LocalDate.now(ZoneId.systemDefault());
    private static final LocalDate SMALLER_PERIOD_FROM = LocalDate.ofEpochDay(-1L);

    private static final LocalDate DEFAULT_PERIOD_TO = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_PERIOD_TO = LocalDate.now(ZoneId.systemDefault());
    private static final LocalDate SMALLER_PERIOD_TO = LocalDate.ofEpochDay(-1L);

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

    private static final Integer DEFAULT_CASH_DISCOUNT_RATE = 1;
    private static final Integer UPDATED_CASH_DISCOUNT_RATE = 2;
    private static final Integer SMALLER_CASH_DISCOUNT_RATE = 1 - 1;

    private static final LocalDate DEFAULT_CASH_DISCOUNT_DATE = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_CASH_DISCOUNT_DATE = LocalDate.now(ZoneId.systemDefault());
    private static final LocalDate SMALLER_CASH_DISCOUNT_DATE = LocalDate.ofEpochDay(-1L);

    private static final Double DEFAULT_TOTAL_PAID = 1D;
    private static final Double UPDATED_TOTAL_PAID = 2D;
    private static final Double SMALLER_TOTAL_PAID = 1D - 1D;

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
    private static final Integer SMALLER_PAGE_AMOUNT = 1 - 1;

    private static final String DEFAULT_NOTES = "AAAAAAAAAA";
    private static final String UPDATED_NOTES = "BBBBBBBBBB";

    private static final InvoiceStatus DEFAULT_STATUS = InvoiceStatus.DRAFT;
    private static final InvoiceStatus UPDATED_STATUS = InvoiceStatus.SENT;

    private static final ZonedDateTime DEFAULT_CREATED = ZonedDateTime.ofInstant(Instant.ofEpochMilli(0L), ZoneOffset.UTC);
    private static final ZonedDateTime UPDATED_CREATED = ZonedDateTime.now(ZoneId.systemDefault()).withNano(0);
    private static final ZonedDateTime SMALLER_CREATED = ZonedDateTime.ofInstant(Instant.ofEpochMilli(-1L), ZoneOffset.UTC);

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
    void getInvoicesByIdFiltering() throws Exception {
        // Initialize the database
        invoiceRepository.saveAndFlush(invoice);

        Long id = invoice.getId();

        defaultInvoiceShouldBeFound("id.equals=" + id);
        defaultInvoiceShouldNotBeFound("id.notEquals=" + id);

        defaultInvoiceShouldBeFound("id.greaterThanOrEqual=" + id);
        defaultInvoiceShouldNotBeFound("id.greaterThan=" + id);

        defaultInvoiceShouldBeFound("id.lessThanOrEqual=" + id);
        defaultInvoiceShouldNotBeFound("id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllInvoicesByRemoteIdIsEqualToSomething() throws Exception {
        // Initialize the database
        invoiceRepository.saveAndFlush(invoice);

        // Get all the invoiceList where remoteId equals to DEFAULT_REMOTE_ID
        defaultInvoiceShouldBeFound("remoteId.equals=" + DEFAULT_REMOTE_ID);

        // Get all the invoiceList where remoteId equals to UPDATED_REMOTE_ID
        defaultInvoiceShouldNotBeFound("remoteId.equals=" + UPDATED_REMOTE_ID);
    }

    @Test
    @Transactional
    void getAllInvoicesByRemoteIdIsNotEqualToSomething() throws Exception {
        // Initialize the database
        invoiceRepository.saveAndFlush(invoice);

        // Get all the invoiceList where remoteId not equals to DEFAULT_REMOTE_ID
        defaultInvoiceShouldNotBeFound("remoteId.notEquals=" + DEFAULT_REMOTE_ID);

        // Get all the invoiceList where remoteId not equals to UPDATED_REMOTE_ID
        defaultInvoiceShouldBeFound("remoteId.notEquals=" + UPDATED_REMOTE_ID);
    }

    @Test
    @Transactional
    void getAllInvoicesByRemoteIdIsInShouldWork() throws Exception {
        // Initialize the database
        invoiceRepository.saveAndFlush(invoice);

        // Get all the invoiceList where remoteId in DEFAULT_REMOTE_ID or UPDATED_REMOTE_ID
        defaultInvoiceShouldBeFound("remoteId.in=" + DEFAULT_REMOTE_ID + "," + UPDATED_REMOTE_ID);

        // Get all the invoiceList where remoteId equals to UPDATED_REMOTE_ID
        defaultInvoiceShouldNotBeFound("remoteId.in=" + UPDATED_REMOTE_ID);
    }

    @Test
    @Transactional
    void getAllInvoicesByRemoteIdIsNullOrNotNull() throws Exception {
        // Initialize the database
        invoiceRepository.saveAndFlush(invoice);

        // Get all the invoiceList where remoteId is not null
        defaultInvoiceShouldBeFound("remoteId.specified=true");

        // Get all the invoiceList where remoteId is null
        defaultInvoiceShouldNotBeFound("remoteId.specified=false");
    }

    @Test
    @Transactional
    void getAllInvoicesByRemoteIdIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        invoiceRepository.saveAndFlush(invoice);

        // Get all the invoiceList where remoteId is greater than or equal to DEFAULT_REMOTE_ID
        defaultInvoiceShouldBeFound("remoteId.greaterThanOrEqual=" + DEFAULT_REMOTE_ID);

        // Get all the invoiceList where remoteId is greater than or equal to UPDATED_REMOTE_ID
        defaultInvoiceShouldNotBeFound("remoteId.greaterThanOrEqual=" + UPDATED_REMOTE_ID);
    }

    @Test
    @Transactional
    void getAllInvoicesByRemoteIdIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        invoiceRepository.saveAndFlush(invoice);

        // Get all the invoiceList where remoteId is less than or equal to DEFAULT_REMOTE_ID
        defaultInvoiceShouldBeFound("remoteId.lessThanOrEqual=" + DEFAULT_REMOTE_ID);

        // Get all the invoiceList where remoteId is less than or equal to SMALLER_REMOTE_ID
        defaultInvoiceShouldNotBeFound("remoteId.lessThanOrEqual=" + SMALLER_REMOTE_ID);
    }

    @Test
    @Transactional
    void getAllInvoicesByRemoteIdIsLessThanSomething() throws Exception {
        // Initialize the database
        invoiceRepository.saveAndFlush(invoice);

        // Get all the invoiceList where remoteId is less than DEFAULT_REMOTE_ID
        defaultInvoiceShouldNotBeFound("remoteId.lessThan=" + DEFAULT_REMOTE_ID);

        // Get all the invoiceList where remoteId is less than UPDATED_REMOTE_ID
        defaultInvoiceShouldBeFound("remoteId.lessThan=" + UPDATED_REMOTE_ID);
    }

    @Test
    @Transactional
    void getAllInvoicesByRemoteIdIsGreaterThanSomething() throws Exception {
        // Initialize the database
        invoiceRepository.saveAndFlush(invoice);

        // Get all the invoiceList where remoteId is greater than DEFAULT_REMOTE_ID
        defaultInvoiceShouldNotBeFound("remoteId.greaterThan=" + DEFAULT_REMOTE_ID);

        // Get all the invoiceList where remoteId is greater than SMALLER_REMOTE_ID
        defaultInvoiceShouldBeFound("remoteId.greaterThan=" + SMALLER_REMOTE_ID);
    }

    @Test
    @Transactional
    void getAllInvoicesByNumberIsEqualToSomething() throws Exception {
        // Initialize the database
        invoiceRepository.saveAndFlush(invoice);

        // Get all the invoiceList where number equals to DEFAULT_NUMBER
        defaultInvoiceShouldBeFound("number.equals=" + DEFAULT_NUMBER);

        // Get all the invoiceList where number equals to UPDATED_NUMBER
        defaultInvoiceShouldNotBeFound("number.equals=" + UPDATED_NUMBER);
    }

    @Test
    @Transactional
    void getAllInvoicesByNumberIsNotEqualToSomething() throws Exception {
        // Initialize the database
        invoiceRepository.saveAndFlush(invoice);

        // Get all the invoiceList where number not equals to DEFAULT_NUMBER
        defaultInvoiceShouldNotBeFound("number.notEquals=" + DEFAULT_NUMBER);

        // Get all the invoiceList where number not equals to UPDATED_NUMBER
        defaultInvoiceShouldBeFound("number.notEquals=" + UPDATED_NUMBER);
    }

    @Test
    @Transactional
    void getAllInvoicesByNumberIsInShouldWork() throws Exception {
        // Initialize the database
        invoiceRepository.saveAndFlush(invoice);

        // Get all the invoiceList where number in DEFAULT_NUMBER or UPDATED_NUMBER
        defaultInvoiceShouldBeFound("number.in=" + DEFAULT_NUMBER + "," + UPDATED_NUMBER);

        // Get all the invoiceList where number equals to UPDATED_NUMBER
        defaultInvoiceShouldNotBeFound("number.in=" + UPDATED_NUMBER);
    }

    @Test
    @Transactional
    void getAllInvoicesByNumberIsNullOrNotNull() throws Exception {
        // Initialize the database
        invoiceRepository.saveAndFlush(invoice);

        // Get all the invoiceList where number is not null
        defaultInvoiceShouldBeFound("number.specified=true");

        // Get all the invoiceList where number is null
        defaultInvoiceShouldNotBeFound("number.specified=false");
    }

    @Test
    @Transactional
    void getAllInvoicesByNumberContainsSomething() throws Exception {
        // Initialize the database
        invoiceRepository.saveAndFlush(invoice);

        // Get all the invoiceList where number contains DEFAULT_NUMBER
        defaultInvoiceShouldBeFound("number.contains=" + DEFAULT_NUMBER);

        // Get all the invoiceList where number contains UPDATED_NUMBER
        defaultInvoiceShouldNotBeFound("number.contains=" + UPDATED_NUMBER);
    }

    @Test
    @Transactional
    void getAllInvoicesByNumberNotContainsSomething() throws Exception {
        // Initialize the database
        invoiceRepository.saveAndFlush(invoice);

        // Get all the invoiceList where number does not contain DEFAULT_NUMBER
        defaultInvoiceShouldNotBeFound("number.doesNotContain=" + DEFAULT_NUMBER);

        // Get all the invoiceList where number does not contain UPDATED_NUMBER
        defaultInvoiceShouldBeFound("number.doesNotContain=" + UPDATED_NUMBER);
    }

    @Test
    @Transactional
    void getAllInvoicesByContactNameIsEqualToSomething() throws Exception {
        // Initialize the database
        invoiceRepository.saveAndFlush(invoice);

        // Get all the invoiceList where contactName equals to DEFAULT_CONTACT_NAME
        defaultInvoiceShouldBeFound("contactName.equals=" + DEFAULT_CONTACT_NAME);

        // Get all the invoiceList where contactName equals to UPDATED_CONTACT_NAME
        defaultInvoiceShouldNotBeFound("contactName.equals=" + UPDATED_CONTACT_NAME);
    }

    @Test
    @Transactional
    void getAllInvoicesByContactNameIsNotEqualToSomething() throws Exception {
        // Initialize the database
        invoiceRepository.saveAndFlush(invoice);

        // Get all the invoiceList where contactName not equals to DEFAULT_CONTACT_NAME
        defaultInvoiceShouldNotBeFound("contactName.notEquals=" + DEFAULT_CONTACT_NAME);

        // Get all the invoiceList where contactName not equals to UPDATED_CONTACT_NAME
        defaultInvoiceShouldBeFound("contactName.notEquals=" + UPDATED_CONTACT_NAME);
    }

    @Test
    @Transactional
    void getAllInvoicesByContactNameIsInShouldWork() throws Exception {
        // Initialize the database
        invoiceRepository.saveAndFlush(invoice);

        // Get all the invoiceList where contactName in DEFAULT_CONTACT_NAME or UPDATED_CONTACT_NAME
        defaultInvoiceShouldBeFound("contactName.in=" + DEFAULT_CONTACT_NAME + "," + UPDATED_CONTACT_NAME);

        // Get all the invoiceList where contactName equals to UPDATED_CONTACT_NAME
        defaultInvoiceShouldNotBeFound("contactName.in=" + UPDATED_CONTACT_NAME);
    }

    @Test
    @Transactional
    void getAllInvoicesByContactNameIsNullOrNotNull() throws Exception {
        // Initialize the database
        invoiceRepository.saveAndFlush(invoice);

        // Get all the invoiceList where contactName is not null
        defaultInvoiceShouldBeFound("contactName.specified=true");

        // Get all the invoiceList where contactName is null
        defaultInvoiceShouldNotBeFound("contactName.specified=false");
    }

    @Test
    @Transactional
    void getAllInvoicesByContactNameContainsSomething() throws Exception {
        // Initialize the database
        invoiceRepository.saveAndFlush(invoice);

        // Get all the invoiceList where contactName contains DEFAULT_CONTACT_NAME
        defaultInvoiceShouldBeFound("contactName.contains=" + DEFAULT_CONTACT_NAME);

        // Get all the invoiceList where contactName contains UPDATED_CONTACT_NAME
        defaultInvoiceShouldNotBeFound("contactName.contains=" + UPDATED_CONTACT_NAME);
    }

    @Test
    @Transactional
    void getAllInvoicesByContactNameNotContainsSomething() throws Exception {
        // Initialize the database
        invoiceRepository.saveAndFlush(invoice);

        // Get all the invoiceList where contactName does not contain DEFAULT_CONTACT_NAME
        defaultInvoiceShouldNotBeFound("contactName.doesNotContain=" + DEFAULT_CONTACT_NAME);

        // Get all the invoiceList where contactName does not contain UPDATED_CONTACT_NAME
        defaultInvoiceShouldBeFound("contactName.doesNotContain=" + UPDATED_CONTACT_NAME);
    }

    @Test
    @Transactional
    void getAllInvoicesByDateIsEqualToSomething() throws Exception {
        // Initialize the database
        invoiceRepository.saveAndFlush(invoice);

        // Get all the invoiceList where date equals to DEFAULT_DATE
        defaultInvoiceShouldBeFound("date.equals=" + DEFAULT_DATE);

        // Get all the invoiceList where date equals to UPDATED_DATE
        defaultInvoiceShouldNotBeFound("date.equals=" + UPDATED_DATE);
    }

    @Test
    @Transactional
    void getAllInvoicesByDateIsNotEqualToSomething() throws Exception {
        // Initialize the database
        invoiceRepository.saveAndFlush(invoice);

        // Get all the invoiceList where date not equals to DEFAULT_DATE
        defaultInvoiceShouldNotBeFound("date.notEquals=" + DEFAULT_DATE);

        // Get all the invoiceList where date not equals to UPDATED_DATE
        defaultInvoiceShouldBeFound("date.notEquals=" + UPDATED_DATE);
    }

    @Test
    @Transactional
    void getAllInvoicesByDateIsInShouldWork() throws Exception {
        // Initialize the database
        invoiceRepository.saveAndFlush(invoice);

        // Get all the invoiceList where date in DEFAULT_DATE or UPDATED_DATE
        defaultInvoiceShouldBeFound("date.in=" + DEFAULT_DATE + "," + UPDATED_DATE);

        // Get all the invoiceList where date equals to UPDATED_DATE
        defaultInvoiceShouldNotBeFound("date.in=" + UPDATED_DATE);
    }

    @Test
    @Transactional
    void getAllInvoicesByDateIsNullOrNotNull() throws Exception {
        // Initialize the database
        invoiceRepository.saveAndFlush(invoice);

        // Get all the invoiceList where date is not null
        defaultInvoiceShouldBeFound("date.specified=true");

        // Get all the invoiceList where date is null
        defaultInvoiceShouldNotBeFound("date.specified=false");
    }

    @Test
    @Transactional
    void getAllInvoicesByDateIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        invoiceRepository.saveAndFlush(invoice);

        // Get all the invoiceList where date is greater than or equal to DEFAULT_DATE
        defaultInvoiceShouldBeFound("date.greaterThanOrEqual=" + DEFAULT_DATE);

        // Get all the invoiceList where date is greater than or equal to UPDATED_DATE
        defaultInvoiceShouldNotBeFound("date.greaterThanOrEqual=" + UPDATED_DATE);
    }

    @Test
    @Transactional
    void getAllInvoicesByDateIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        invoiceRepository.saveAndFlush(invoice);

        // Get all the invoiceList where date is less than or equal to DEFAULT_DATE
        defaultInvoiceShouldBeFound("date.lessThanOrEqual=" + DEFAULT_DATE);

        // Get all the invoiceList where date is less than or equal to SMALLER_DATE
        defaultInvoiceShouldNotBeFound("date.lessThanOrEqual=" + SMALLER_DATE);
    }

    @Test
    @Transactional
    void getAllInvoicesByDateIsLessThanSomething() throws Exception {
        // Initialize the database
        invoiceRepository.saveAndFlush(invoice);

        // Get all the invoiceList where date is less than DEFAULT_DATE
        defaultInvoiceShouldNotBeFound("date.lessThan=" + DEFAULT_DATE);

        // Get all the invoiceList where date is less than UPDATED_DATE
        defaultInvoiceShouldBeFound("date.lessThan=" + UPDATED_DATE);
    }

    @Test
    @Transactional
    void getAllInvoicesByDateIsGreaterThanSomething() throws Exception {
        // Initialize the database
        invoiceRepository.saveAndFlush(invoice);

        // Get all the invoiceList where date is greater than DEFAULT_DATE
        defaultInvoiceShouldNotBeFound("date.greaterThan=" + DEFAULT_DATE);

        // Get all the invoiceList where date is greater than SMALLER_DATE
        defaultInvoiceShouldBeFound("date.greaterThan=" + SMALLER_DATE);
    }

    @Test
    @Transactional
    void getAllInvoicesByDueIsEqualToSomething() throws Exception {
        // Initialize the database
        invoiceRepository.saveAndFlush(invoice);

        // Get all the invoiceList where due equals to DEFAULT_DUE
        defaultInvoiceShouldBeFound("due.equals=" + DEFAULT_DUE);

        // Get all the invoiceList where due equals to UPDATED_DUE
        defaultInvoiceShouldNotBeFound("due.equals=" + UPDATED_DUE);
    }

    @Test
    @Transactional
    void getAllInvoicesByDueIsNotEqualToSomething() throws Exception {
        // Initialize the database
        invoiceRepository.saveAndFlush(invoice);

        // Get all the invoiceList where due not equals to DEFAULT_DUE
        defaultInvoiceShouldNotBeFound("due.notEquals=" + DEFAULT_DUE);

        // Get all the invoiceList where due not equals to UPDATED_DUE
        defaultInvoiceShouldBeFound("due.notEquals=" + UPDATED_DUE);
    }

    @Test
    @Transactional
    void getAllInvoicesByDueIsInShouldWork() throws Exception {
        // Initialize the database
        invoiceRepository.saveAndFlush(invoice);

        // Get all the invoiceList where due in DEFAULT_DUE or UPDATED_DUE
        defaultInvoiceShouldBeFound("due.in=" + DEFAULT_DUE + "," + UPDATED_DUE);

        // Get all the invoiceList where due equals to UPDATED_DUE
        defaultInvoiceShouldNotBeFound("due.in=" + UPDATED_DUE);
    }

    @Test
    @Transactional
    void getAllInvoicesByDueIsNullOrNotNull() throws Exception {
        // Initialize the database
        invoiceRepository.saveAndFlush(invoice);

        // Get all the invoiceList where due is not null
        defaultInvoiceShouldBeFound("due.specified=true");

        // Get all the invoiceList where due is null
        defaultInvoiceShouldNotBeFound("due.specified=false");
    }

    @Test
    @Transactional
    void getAllInvoicesByDueIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        invoiceRepository.saveAndFlush(invoice);

        // Get all the invoiceList where due is greater than or equal to DEFAULT_DUE
        defaultInvoiceShouldBeFound("due.greaterThanOrEqual=" + DEFAULT_DUE);

        // Get all the invoiceList where due is greater than or equal to UPDATED_DUE
        defaultInvoiceShouldNotBeFound("due.greaterThanOrEqual=" + UPDATED_DUE);
    }

    @Test
    @Transactional
    void getAllInvoicesByDueIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        invoiceRepository.saveAndFlush(invoice);

        // Get all the invoiceList where due is less than or equal to DEFAULT_DUE
        defaultInvoiceShouldBeFound("due.lessThanOrEqual=" + DEFAULT_DUE);

        // Get all the invoiceList where due is less than or equal to SMALLER_DUE
        defaultInvoiceShouldNotBeFound("due.lessThanOrEqual=" + SMALLER_DUE);
    }

    @Test
    @Transactional
    void getAllInvoicesByDueIsLessThanSomething() throws Exception {
        // Initialize the database
        invoiceRepository.saveAndFlush(invoice);

        // Get all the invoiceList where due is less than DEFAULT_DUE
        defaultInvoiceShouldNotBeFound("due.lessThan=" + DEFAULT_DUE);

        // Get all the invoiceList where due is less than UPDATED_DUE
        defaultInvoiceShouldBeFound("due.lessThan=" + UPDATED_DUE);
    }

    @Test
    @Transactional
    void getAllInvoicesByDueIsGreaterThanSomething() throws Exception {
        // Initialize the database
        invoiceRepository.saveAndFlush(invoice);

        // Get all the invoiceList where due is greater than DEFAULT_DUE
        defaultInvoiceShouldNotBeFound("due.greaterThan=" + DEFAULT_DUE);

        // Get all the invoiceList where due is greater than SMALLER_DUE
        defaultInvoiceShouldBeFound("due.greaterThan=" + SMALLER_DUE);
    }

    @Test
    @Transactional
    void getAllInvoicesByPeriodFromIsEqualToSomething() throws Exception {
        // Initialize the database
        invoiceRepository.saveAndFlush(invoice);

        // Get all the invoiceList where periodFrom equals to DEFAULT_PERIOD_FROM
        defaultInvoiceShouldBeFound("periodFrom.equals=" + DEFAULT_PERIOD_FROM);

        // Get all the invoiceList where periodFrom equals to UPDATED_PERIOD_FROM
        defaultInvoiceShouldNotBeFound("periodFrom.equals=" + UPDATED_PERIOD_FROM);
    }

    @Test
    @Transactional
    void getAllInvoicesByPeriodFromIsNotEqualToSomething() throws Exception {
        // Initialize the database
        invoiceRepository.saveAndFlush(invoice);

        // Get all the invoiceList where periodFrom not equals to DEFAULT_PERIOD_FROM
        defaultInvoiceShouldNotBeFound("periodFrom.notEquals=" + DEFAULT_PERIOD_FROM);

        // Get all the invoiceList where periodFrom not equals to UPDATED_PERIOD_FROM
        defaultInvoiceShouldBeFound("periodFrom.notEquals=" + UPDATED_PERIOD_FROM);
    }

    @Test
    @Transactional
    void getAllInvoicesByPeriodFromIsInShouldWork() throws Exception {
        // Initialize the database
        invoiceRepository.saveAndFlush(invoice);

        // Get all the invoiceList where periodFrom in DEFAULT_PERIOD_FROM or UPDATED_PERIOD_FROM
        defaultInvoiceShouldBeFound("periodFrom.in=" + DEFAULT_PERIOD_FROM + "," + UPDATED_PERIOD_FROM);

        // Get all the invoiceList where periodFrom equals to UPDATED_PERIOD_FROM
        defaultInvoiceShouldNotBeFound("periodFrom.in=" + UPDATED_PERIOD_FROM);
    }

    @Test
    @Transactional
    void getAllInvoicesByPeriodFromIsNullOrNotNull() throws Exception {
        // Initialize the database
        invoiceRepository.saveAndFlush(invoice);

        // Get all the invoiceList where periodFrom is not null
        defaultInvoiceShouldBeFound("periodFrom.specified=true");

        // Get all the invoiceList where periodFrom is null
        defaultInvoiceShouldNotBeFound("periodFrom.specified=false");
    }

    @Test
    @Transactional
    void getAllInvoicesByPeriodFromIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        invoiceRepository.saveAndFlush(invoice);

        // Get all the invoiceList where periodFrom is greater than or equal to DEFAULT_PERIOD_FROM
        defaultInvoiceShouldBeFound("periodFrom.greaterThanOrEqual=" + DEFAULT_PERIOD_FROM);

        // Get all the invoiceList where periodFrom is greater than or equal to UPDATED_PERIOD_FROM
        defaultInvoiceShouldNotBeFound("periodFrom.greaterThanOrEqual=" + UPDATED_PERIOD_FROM);
    }

    @Test
    @Transactional
    void getAllInvoicesByPeriodFromIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        invoiceRepository.saveAndFlush(invoice);

        // Get all the invoiceList where periodFrom is less than or equal to DEFAULT_PERIOD_FROM
        defaultInvoiceShouldBeFound("periodFrom.lessThanOrEqual=" + DEFAULT_PERIOD_FROM);

        // Get all the invoiceList where periodFrom is less than or equal to SMALLER_PERIOD_FROM
        defaultInvoiceShouldNotBeFound("periodFrom.lessThanOrEqual=" + SMALLER_PERIOD_FROM);
    }

    @Test
    @Transactional
    void getAllInvoicesByPeriodFromIsLessThanSomething() throws Exception {
        // Initialize the database
        invoiceRepository.saveAndFlush(invoice);

        // Get all the invoiceList where periodFrom is less than DEFAULT_PERIOD_FROM
        defaultInvoiceShouldNotBeFound("periodFrom.lessThan=" + DEFAULT_PERIOD_FROM);

        // Get all the invoiceList where periodFrom is less than UPDATED_PERIOD_FROM
        defaultInvoiceShouldBeFound("periodFrom.lessThan=" + UPDATED_PERIOD_FROM);
    }

    @Test
    @Transactional
    void getAllInvoicesByPeriodFromIsGreaterThanSomething() throws Exception {
        // Initialize the database
        invoiceRepository.saveAndFlush(invoice);

        // Get all the invoiceList where periodFrom is greater than DEFAULT_PERIOD_FROM
        defaultInvoiceShouldNotBeFound("periodFrom.greaterThan=" + DEFAULT_PERIOD_FROM);

        // Get all the invoiceList where periodFrom is greater than SMALLER_PERIOD_FROM
        defaultInvoiceShouldBeFound("periodFrom.greaterThan=" + SMALLER_PERIOD_FROM);
    }

    @Test
    @Transactional
    void getAllInvoicesByPeriodToIsEqualToSomething() throws Exception {
        // Initialize the database
        invoiceRepository.saveAndFlush(invoice);

        // Get all the invoiceList where periodTo equals to DEFAULT_PERIOD_TO
        defaultInvoiceShouldBeFound("periodTo.equals=" + DEFAULT_PERIOD_TO);

        // Get all the invoiceList where periodTo equals to UPDATED_PERIOD_TO
        defaultInvoiceShouldNotBeFound("periodTo.equals=" + UPDATED_PERIOD_TO);
    }

    @Test
    @Transactional
    void getAllInvoicesByPeriodToIsNotEqualToSomething() throws Exception {
        // Initialize the database
        invoiceRepository.saveAndFlush(invoice);

        // Get all the invoiceList where periodTo not equals to DEFAULT_PERIOD_TO
        defaultInvoiceShouldNotBeFound("periodTo.notEquals=" + DEFAULT_PERIOD_TO);

        // Get all the invoiceList where periodTo not equals to UPDATED_PERIOD_TO
        defaultInvoiceShouldBeFound("periodTo.notEquals=" + UPDATED_PERIOD_TO);
    }

    @Test
    @Transactional
    void getAllInvoicesByPeriodToIsInShouldWork() throws Exception {
        // Initialize the database
        invoiceRepository.saveAndFlush(invoice);

        // Get all the invoiceList where periodTo in DEFAULT_PERIOD_TO or UPDATED_PERIOD_TO
        defaultInvoiceShouldBeFound("periodTo.in=" + DEFAULT_PERIOD_TO + "," + UPDATED_PERIOD_TO);

        // Get all the invoiceList where periodTo equals to UPDATED_PERIOD_TO
        defaultInvoiceShouldNotBeFound("periodTo.in=" + UPDATED_PERIOD_TO);
    }

    @Test
    @Transactional
    void getAllInvoicesByPeriodToIsNullOrNotNull() throws Exception {
        // Initialize the database
        invoiceRepository.saveAndFlush(invoice);

        // Get all the invoiceList where periodTo is not null
        defaultInvoiceShouldBeFound("periodTo.specified=true");

        // Get all the invoiceList where periodTo is null
        defaultInvoiceShouldNotBeFound("periodTo.specified=false");
    }

    @Test
    @Transactional
    void getAllInvoicesByPeriodToIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        invoiceRepository.saveAndFlush(invoice);

        // Get all the invoiceList where periodTo is greater than or equal to DEFAULT_PERIOD_TO
        defaultInvoiceShouldBeFound("periodTo.greaterThanOrEqual=" + DEFAULT_PERIOD_TO);

        // Get all the invoiceList where periodTo is greater than or equal to UPDATED_PERIOD_TO
        defaultInvoiceShouldNotBeFound("periodTo.greaterThanOrEqual=" + UPDATED_PERIOD_TO);
    }

    @Test
    @Transactional
    void getAllInvoicesByPeriodToIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        invoiceRepository.saveAndFlush(invoice);

        // Get all the invoiceList where periodTo is less than or equal to DEFAULT_PERIOD_TO
        defaultInvoiceShouldBeFound("periodTo.lessThanOrEqual=" + DEFAULT_PERIOD_TO);

        // Get all the invoiceList where periodTo is less than or equal to SMALLER_PERIOD_TO
        defaultInvoiceShouldNotBeFound("periodTo.lessThanOrEqual=" + SMALLER_PERIOD_TO);
    }

    @Test
    @Transactional
    void getAllInvoicesByPeriodToIsLessThanSomething() throws Exception {
        // Initialize the database
        invoiceRepository.saveAndFlush(invoice);

        // Get all the invoiceList where periodTo is less than DEFAULT_PERIOD_TO
        defaultInvoiceShouldNotBeFound("periodTo.lessThan=" + DEFAULT_PERIOD_TO);

        // Get all the invoiceList where periodTo is less than UPDATED_PERIOD_TO
        defaultInvoiceShouldBeFound("periodTo.lessThan=" + UPDATED_PERIOD_TO);
    }

    @Test
    @Transactional
    void getAllInvoicesByPeriodToIsGreaterThanSomething() throws Exception {
        // Initialize the database
        invoiceRepository.saveAndFlush(invoice);

        // Get all the invoiceList where periodTo is greater than DEFAULT_PERIOD_TO
        defaultInvoiceShouldNotBeFound("periodTo.greaterThan=" + DEFAULT_PERIOD_TO);

        // Get all the invoiceList where periodTo is greater than SMALLER_PERIOD_TO
        defaultInvoiceShouldBeFound("periodTo.greaterThan=" + SMALLER_PERIOD_TO);
    }

    @Test
    @Transactional
    void getAllInvoicesByPeriodTextIsEqualToSomething() throws Exception {
        // Initialize the database
        invoiceRepository.saveAndFlush(invoice);

        // Get all the invoiceList where periodText equals to DEFAULT_PERIOD_TEXT
        defaultInvoiceShouldBeFound("periodText.equals=" + DEFAULT_PERIOD_TEXT);

        // Get all the invoiceList where periodText equals to UPDATED_PERIOD_TEXT
        defaultInvoiceShouldNotBeFound("periodText.equals=" + UPDATED_PERIOD_TEXT);
    }

    @Test
    @Transactional
    void getAllInvoicesByPeriodTextIsNotEqualToSomething() throws Exception {
        // Initialize the database
        invoiceRepository.saveAndFlush(invoice);

        // Get all the invoiceList where periodText not equals to DEFAULT_PERIOD_TEXT
        defaultInvoiceShouldNotBeFound("periodText.notEquals=" + DEFAULT_PERIOD_TEXT);

        // Get all the invoiceList where periodText not equals to UPDATED_PERIOD_TEXT
        defaultInvoiceShouldBeFound("periodText.notEquals=" + UPDATED_PERIOD_TEXT);
    }

    @Test
    @Transactional
    void getAllInvoicesByPeriodTextIsInShouldWork() throws Exception {
        // Initialize the database
        invoiceRepository.saveAndFlush(invoice);

        // Get all the invoiceList where periodText in DEFAULT_PERIOD_TEXT or UPDATED_PERIOD_TEXT
        defaultInvoiceShouldBeFound("periodText.in=" + DEFAULT_PERIOD_TEXT + "," + UPDATED_PERIOD_TEXT);

        // Get all the invoiceList where periodText equals to UPDATED_PERIOD_TEXT
        defaultInvoiceShouldNotBeFound("periodText.in=" + UPDATED_PERIOD_TEXT);
    }

    @Test
    @Transactional
    void getAllInvoicesByPeriodTextIsNullOrNotNull() throws Exception {
        // Initialize the database
        invoiceRepository.saveAndFlush(invoice);

        // Get all the invoiceList where periodText is not null
        defaultInvoiceShouldBeFound("periodText.specified=true");

        // Get all the invoiceList where periodText is null
        defaultInvoiceShouldNotBeFound("periodText.specified=false");
    }

    @Test
    @Transactional
    void getAllInvoicesByPeriodTextContainsSomething() throws Exception {
        // Initialize the database
        invoiceRepository.saveAndFlush(invoice);

        // Get all the invoiceList where periodText contains DEFAULT_PERIOD_TEXT
        defaultInvoiceShouldBeFound("periodText.contains=" + DEFAULT_PERIOD_TEXT);

        // Get all the invoiceList where periodText contains UPDATED_PERIOD_TEXT
        defaultInvoiceShouldNotBeFound("periodText.contains=" + UPDATED_PERIOD_TEXT);
    }

    @Test
    @Transactional
    void getAllInvoicesByPeriodTextNotContainsSomething() throws Exception {
        // Initialize the database
        invoiceRepository.saveAndFlush(invoice);

        // Get all the invoiceList where periodText does not contain DEFAULT_PERIOD_TEXT
        defaultInvoiceShouldNotBeFound("periodText.doesNotContain=" + DEFAULT_PERIOD_TEXT);

        // Get all the invoiceList where periodText does not contain UPDATED_PERIOD_TEXT
        defaultInvoiceShouldBeFound("periodText.doesNotContain=" + UPDATED_PERIOD_TEXT);
    }

    @Test
    @Transactional
    void getAllInvoicesByCurrencyIsEqualToSomething() throws Exception {
        // Initialize the database
        invoiceRepository.saveAndFlush(invoice);

        // Get all the invoiceList where currency equals to DEFAULT_CURRENCY
        defaultInvoiceShouldBeFound("currency.equals=" + DEFAULT_CURRENCY);

        // Get all the invoiceList where currency equals to UPDATED_CURRENCY
        defaultInvoiceShouldNotBeFound("currency.equals=" + UPDATED_CURRENCY);
    }

    @Test
    @Transactional
    void getAllInvoicesByCurrencyIsNotEqualToSomething() throws Exception {
        // Initialize the database
        invoiceRepository.saveAndFlush(invoice);

        // Get all the invoiceList where currency not equals to DEFAULT_CURRENCY
        defaultInvoiceShouldNotBeFound("currency.notEquals=" + DEFAULT_CURRENCY);

        // Get all the invoiceList where currency not equals to UPDATED_CURRENCY
        defaultInvoiceShouldBeFound("currency.notEquals=" + UPDATED_CURRENCY);
    }

    @Test
    @Transactional
    void getAllInvoicesByCurrencyIsInShouldWork() throws Exception {
        // Initialize the database
        invoiceRepository.saveAndFlush(invoice);

        // Get all the invoiceList where currency in DEFAULT_CURRENCY or UPDATED_CURRENCY
        defaultInvoiceShouldBeFound("currency.in=" + DEFAULT_CURRENCY + "," + UPDATED_CURRENCY);

        // Get all the invoiceList where currency equals to UPDATED_CURRENCY
        defaultInvoiceShouldNotBeFound("currency.in=" + UPDATED_CURRENCY);
    }

    @Test
    @Transactional
    void getAllInvoicesByCurrencyIsNullOrNotNull() throws Exception {
        // Initialize the database
        invoiceRepository.saveAndFlush(invoice);

        // Get all the invoiceList where currency is not null
        defaultInvoiceShouldBeFound("currency.specified=true");

        // Get all the invoiceList where currency is null
        defaultInvoiceShouldNotBeFound("currency.specified=false");
    }

    @Test
    @Transactional
    void getAllInvoicesByTotalIsEqualToSomething() throws Exception {
        // Initialize the database
        invoiceRepository.saveAndFlush(invoice);

        // Get all the invoiceList where total equals to DEFAULT_TOTAL
        defaultInvoiceShouldBeFound("total.equals=" + DEFAULT_TOTAL);

        // Get all the invoiceList where total equals to UPDATED_TOTAL
        defaultInvoiceShouldNotBeFound("total.equals=" + UPDATED_TOTAL);
    }

    @Test
    @Transactional
    void getAllInvoicesByTotalIsNotEqualToSomething() throws Exception {
        // Initialize the database
        invoiceRepository.saveAndFlush(invoice);

        // Get all the invoiceList where total not equals to DEFAULT_TOTAL
        defaultInvoiceShouldNotBeFound("total.notEquals=" + DEFAULT_TOTAL);

        // Get all the invoiceList where total not equals to UPDATED_TOTAL
        defaultInvoiceShouldBeFound("total.notEquals=" + UPDATED_TOTAL);
    }

    @Test
    @Transactional
    void getAllInvoicesByTotalIsInShouldWork() throws Exception {
        // Initialize the database
        invoiceRepository.saveAndFlush(invoice);

        // Get all the invoiceList where total in DEFAULT_TOTAL or UPDATED_TOTAL
        defaultInvoiceShouldBeFound("total.in=" + DEFAULT_TOTAL + "," + UPDATED_TOTAL);

        // Get all the invoiceList where total equals to UPDATED_TOTAL
        defaultInvoiceShouldNotBeFound("total.in=" + UPDATED_TOTAL);
    }

    @Test
    @Transactional
    void getAllInvoicesByTotalIsNullOrNotNull() throws Exception {
        // Initialize the database
        invoiceRepository.saveAndFlush(invoice);

        // Get all the invoiceList where total is not null
        defaultInvoiceShouldBeFound("total.specified=true");

        // Get all the invoiceList where total is null
        defaultInvoiceShouldNotBeFound("total.specified=false");
    }

    @Test
    @Transactional
    void getAllInvoicesByTotalIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        invoiceRepository.saveAndFlush(invoice);

        // Get all the invoiceList where total is greater than or equal to DEFAULT_TOTAL
        defaultInvoiceShouldBeFound("total.greaterThanOrEqual=" + DEFAULT_TOTAL);

        // Get all the invoiceList where total is greater than or equal to UPDATED_TOTAL
        defaultInvoiceShouldNotBeFound("total.greaterThanOrEqual=" + UPDATED_TOTAL);
    }

    @Test
    @Transactional
    void getAllInvoicesByTotalIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        invoiceRepository.saveAndFlush(invoice);

        // Get all the invoiceList where total is less than or equal to DEFAULT_TOTAL
        defaultInvoiceShouldBeFound("total.lessThanOrEqual=" + DEFAULT_TOTAL);

        // Get all the invoiceList where total is less than or equal to SMALLER_TOTAL
        defaultInvoiceShouldNotBeFound("total.lessThanOrEqual=" + SMALLER_TOTAL);
    }

    @Test
    @Transactional
    void getAllInvoicesByTotalIsLessThanSomething() throws Exception {
        // Initialize the database
        invoiceRepository.saveAndFlush(invoice);

        // Get all the invoiceList where total is less than DEFAULT_TOTAL
        defaultInvoiceShouldNotBeFound("total.lessThan=" + DEFAULT_TOTAL);

        // Get all the invoiceList where total is less than UPDATED_TOTAL
        defaultInvoiceShouldBeFound("total.lessThan=" + UPDATED_TOTAL);
    }

    @Test
    @Transactional
    void getAllInvoicesByTotalIsGreaterThanSomething() throws Exception {
        // Initialize the database
        invoiceRepository.saveAndFlush(invoice);

        // Get all the invoiceList where total is greater than DEFAULT_TOTAL
        defaultInvoiceShouldNotBeFound("total.greaterThan=" + DEFAULT_TOTAL);

        // Get all the invoiceList where total is greater than SMALLER_TOTAL
        defaultInvoiceShouldBeFound("total.greaterThan=" + SMALLER_TOTAL);
    }

    @Test
    @Transactional
    void getAllInvoicesByVatIncludedIsEqualToSomething() throws Exception {
        // Initialize the database
        invoiceRepository.saveAndFlush(invoice);

        // Get all the invoiceList where vatIncluded equals to DEFAULT_VAT_INCLUDED
        defaultInvoiceShouldBeFound("vatIncluded.equals=" + DEFAULT_VAT_INCLUDED);

        // Get all the invoiceList where vatIncluded equals to UPDATED_VAT_INCLUDED
        defaultInvoiceShouldNotBeFound("vatIncluded.equals=" + UPDATED_VAT_INCLUDED);
    }

    @Test
    @Transactional
    void getAllInvoicesByVatIncludedIsNotEqualToSomething() throws Exception {
        // Initialize the database
        invoiceRepository.saveAndFlush(invoice);

        // Get all the invoiceList where vatIncluded not equals to DEFAULT_VAT_INCLUDED
        defaultInvoiceShouldNotBeFound("vatIncluded.notEquals=" + DEFAULT_VAT_INCLUDED);

        // Get all the invoiceList where vatIncluded not equals to UPDATED_VAT_INCLUDED
        defaultInvoiceShouldBeFound("vatIncluded.notEquals=" + UPDATED_VAT_INCLUDED);
    }

    @Test
    @Transactional
    void getAllInvoicesByVatIncludedIsInShouldWork() throws Exception {
        // Initialize the database
        invoiceRepository.saveAndFlush(invoice);

        // Get all the invoiceList where vatIncluded in DEFAULT_VAT_INCLUDED or UPDATED_VAT_INCLUDED
        defaultInvoiceShouldBeFound("vatIncluded.in=" + DEFAULT_VAT_INCLUDED + "," + UPDATED_VAT_INCLUDED);

        // Get all the invoiceList where vatIncluded equals to UPDATED_VAT_INCLUDED
        defaultInvoiceShouldNotBeFound("vatIncluded.in=" + UPDATED_VAT_INCLUDED);
    }

    @Test
    @Transactional
    void getAllInvoicesByVatIncludedIsNullOrNotNull() throws Exception {
        // Initialize the database
        invoiceRepository.saveAndFlush(invoice);

        // Get all the invoiceList where vatIncluded is not null
        defaultInvoiceShouldBeFound("vatIncluded.specified=true");

        // Get all the invoiceList where vatIncluded is null
        defaultInvoiceShouldNotBeFound("vatIncluded.specified=false");
    }

    @Test
    @Transactional
    void getAllInvoicesByDiscountRateIsEqualToSomething() throws Exception {
        // Initialize the database
        invoiceRepository.saveAndFlush(invoice);

        // Get all the invoiceList where discountRate equals to DEFAULT_DISCOUNT_RATE
        defaultInvoiceShouldBeFound("discountRate.equals=" + DEFAULT_DISCOUNT_RATE);

        // Get all the invoiceList where discountRate equals to UPDATED_DISCOUNT_RATE
        defaultInvoiceShouldNotBeFound("discountRate.equals=" + UPDATED_DISCOUNT_RATE);
    }

    @Test
    @Transactional
    void getAllInvoicesByDiscountRateIsNotEqualToSomething() throws Exception {
        // Initialize the database
        invoiceRepository.saveAndFlush(invoice);

        // Get all the invoiceList where discountRate not equals to DEFAULT_DISCOUNT_RATE
        defaultInvoiceShouldNotBeFound("discountRate.notEquals=" + DEFAULT_DISCOUNT_RATE);

        // Get all the invoiceList where discountRate not equals to UPDATED_DISCOUNT_RATE
        defaultInvoiceShouldBeFound("discountRate.notEquals=" + UPDATED_DISCOUNT_RATE);
    }

    @Test
    @Transactional
    void getAllInvoicesByDiscountRateIsInShouldWork() throws Exception {
        // Initialize the database
        invoiceRepository.saveAndFlush(invoice);

        // Get all the invoiceList where discountRate in DEFAULT_DISCOUNT_RATE or UPDATED_DISCOUNT_RATE
        defaultInvoiceShouldBeFound("discountRate.in=" + DEFAULT_DISCOUNT_RATE + "," + UPDATED_DISCOUNT_RATE);

        // Get all the invoiceList where discountRate equals to UPDATED_DISCOUNT_RATE
        defaultInvoiceShouldNotBeFound("discountRate.in=" + UPDATED_DISCOUNT_RATE);
    }

    @Test
    @Transactional
    void getAllInvoicesByDiscountRateIsNullOrNotNull() throws Exception {
        // Initialize the database
        invoiceRepository.saveAndFlush(invoice);

        // Get all the invoiceList where discountRate is not null
        defaultInvoiceShouldBeFound("discountRate.specified=true");

        // Get all the invoiceList where discountRate is null
        defaultInvoiceShouldNotBeFound("discountRate.specified=false");
    }

    @Test
    @Transactional
    void getAllInvoicesByDiscountRateIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        invoiceRepository.saveAndFlush(invoice);

        // Get all the invoiceList where discountRate is greater than or equal to DEFAULT_DISCOUNT_RATE
        defaultInvoiceShouldBeFound("discountRate.greaterThanOrEqual=" + DEFAULT_DISCOUNT_RATE);

        // Get all the invoiceList where discountRate is greater than or equal to UPDATED_DISCOUNT_RATE
        defaultInvoiceShouldNotBeFound("discountRate.greaterThanOrEqual=" + UPDATED_DISCOUNT_RATE);
    }

    @Test
    @Transactional
    void getAllInvoicesByDiscountRateIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        invoiceRepository.saveAndFlush(invoice);

        // Get all the invoiceList where discountRate is less than or equal to DEFAULT_DISCOUNT_RATE
        defaultInvoiceShouldBeFound("discountRate.lessThanOrEqual=" + DEFAULT_DISCOUNT_RATE);

        // Get all the invoiceList where discountRate is less than or equal to SMALLER_DISCOUNT_RATE
        defaultInvoiceShouldNotBeFound("discountRate.lessThanOrEqual=" + SMALLER_DISCOUNT_RATE);
    }

    @Test
    @Transactional
    void getAllInvoicesByDiscountRateIsLessThanSomething() throws Exception {
        // Initialize the database
        invoiceRepository.saveAndFlush(invoice);

        // Get all the invoiceList where discountRate is less than DEFAULT_DISCOUNT_RATE
        defaultInvoiceShouldNotBeFound("discountRate.lessThan=" + DEFAULT_DISCOUNT_RATE);

        // Get all the invoiceList where discountRate is less than UPDATED_DISCOUNT_RATE
        defaultInvoiceShouldBeFound("discountRate.lessThan=" + UPDATED_DISCOUNT_RATE);
    }

    @Test
    @Transactional
    void getAllInvoicesByDiscountRateIsGreaterThanSomething() throws Exception {
        // Initialize the database
        invoiceRepository.saveAndFlush(invoice);

        // Get all the invoiceList where discountRate is greater than DEFAULT_DISCOUNT_RATE
        defaultInvoiceShouldNotBeFound("discountRate.greaterThan=" + DEFAULT_DISCOUNT_RATE);

        // Get all the invoiceList where discountRate is greater than SMALLER_DISCOUNT_RATE
        defaultInvoiceShouldBeFound("discountRate.greaterThan=" + SMALLER_DISCOUNT_RATE);
    }

    @Test
    @Transactional
    void getAllInvoicesByDiscountTypeIsEqualToSomething() throws Exception {
        // Initialize the database
        invoiceRepository.saveAndFlush(invoice);

        // Get all the invoiceList where discountType equals to DEFAULT_DISCOUNT_TYPE
        defaultInvoiceShouldBeFound("discountType.equals=" + DEFAULT_DISCOUNT_TYPE);

        // Get all the invoiceList where discountType equals to UPDATED_DISCOUNT_TYPE
        defaultInvoiceShouldNotBeFound("discountType.equals=" + UPDATED_DISCOUNT_TYPE);
    }

    @Test
    @Transactional
    void getAllInvoicesByDiscountTypeIsNotEqualToSomething() throws Exception {
        // Initialize the database
        invoiceRepository.saveAndFlush(invoice);

        // Get all the invoiceList where discountType not equals to DEFAULT_DISCOUNT_TYPE
        defaultInvoiceShouldNotBeFound("discountType.notEquals=" + DEFAULT_DISCOUNT_TYPE);

        // Get all the invoiceList where discountType not equals to UPDATED_DISCOUNT_TYPE
        defaultInvoiceShouldBeFound("discountType.notEquals=" + UPDATED_DISCOUNT_TYPE);
    }

    @Test
    @Transactional
    void getAllInvoicesByDiscountTypeIsInShouldWork() throws Exception {
        // Initialize the database
        invoiceRepository.saveAndFlush(invoice);

        // Get all the invoiceList where discountType in DEFAULT_DISCOUNT_TYPE or UPDATED_DISCOUNT_TYPE
        defaultInvoiceShouldBeFound("discountType.in=" + DEFAULT_DISCOUNT_TYPE + "," + UPDATED_DISCOUNT_TYPE);

        // Get all the invoiceList where discountType equals to UPDATED_DISCOUNT_TYPE
        defaultInvoiceShouldNotBeFound("discountType.in=" + UPDATED_DISCOUNT_TYPE);
    }

    @Test
    @Transactional
    void getAllInvoicesByDiscountTypeIsNullOrNotNull() throws Exception {
        // Initialize the database
        invoiceRepository.saveAndFlush(invoice);

        // Get all the invoiceList where discountType is not null
        defaultInvoiceShouldBeFound("discountType.specified=true");

        // Get all the invoiceList where discountType is null
        defaultInvoiceShouldNotBeFound("discountType.specified=false");
    }

    @Test
    @Transactional
    void getAllInvoicesByCashDiscountRateIsEqualToSomething() throws Exception {
        // Initialize the database
        invoiceRepository.saveAndFlush(invoice);

        // Get all the invoiceList where cashDiscountRate equals to DEFAULT_CASH_DISCOUNT_RATE
        defaultInvoiceShouldBeFound("cashDiscountRate.equals=" + DEFAULT_CASH_DISCOUNT_RATE);

        // Get all the invoiceList where cashDiscountRate equals to UPDATED_CASH_DISCOUNT_RATE
        defaultInvoiceShouldNotBeFound("cashDiscountRate.equals=" + UPDATED_CASH_DISCOUNT_RATE);
    }

    @Test
    @Transactional
    void getAllInvoicesByCashDiscountRateIsNotEqualToSomething() throws Exception {
        // Initialize the database
        invoiceRepository.saveAndFlush(invoice);

        // Get all the invoiceList where cashDiscountRate not equals to DEFAULT_CASH_DISCOUNT_RATE
        defaultInvoiceShouldNotBeFound("cashDiscountRate.notEquals=" + DEFAULT_CASH_DISCOUNT_RATE);

        // Get all the invoiceList where cashDiscountRate not equals to UPDATED_CASH_DISCOUNT_RATE
        defaultInvoiceShouldBeFound("cashDiscountRate.notEquals=" + UPDATED_CASH_DISCOUNT_RATE);
    }

    @Test
    @Transactional
    void getAllInvoicesByCashDiscountRateIsInShouldWork() throws Exception {
        // Initialize the database
        invoiceRepository.saveAndFlush(invoice);

        // Get all the invoiceList where cashDiscountRate in DEFAULT_CASH_DISCOUNT_RATE or UPDATED_CASH_DISCOUNT_RATE
        defaultInvoiceShouldBeFound("cashDiscountRate.in=" + DEFAULT_CASH_DISCOUNT_RATE + "," + UPDATED_CASH_DISCOUNT_RATE);

        // Get all the invoiceList where cashDiscountRate equals to UPDATED_CASH_DISCOUNT_RATE
        defaultInvoiceShouldNotBeFound("cashDiscountRate.in=" + UPDATED_CASH_DISCOUNT_RATE);
    }

    @Test
    @Transactional
    void getAllInvoicesByCashDiscountRateIsNullOrNotNull() throws Exception {
        // Initialize the database
        invoiceRepository.saveAndFlush(invoice);

        // Get all the invoiceList where cashDiscountRate is not null
        defaultInvoiceShouldBeFound("cashDiscountRate.specified=true");

        // Get all the invoiceList where cashDiscountRate is null
        defaultInvoiceShouldNotBeFound("cashDiscountRate.specified=false");
    }

    @Test
    @Transactional
    void getAllInvoicesByCashDiscountRateIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        invoiceRepository.saveAndFlush(invoice);

        // Get all the invoiceList where cashDiscountRate is greater than or equal to DEFAULT_CASH_DISCOUNT_RATE
        defaultInvoiceShouldBeFound("cashDiscountRate.greaterThanOrEqual=" + DEFAULT_CASH_DISCOUNT_RATE);

        // Get all the invoiceList where cashDiscountRate is greater than or equal to UPDATED_CASH_DISCOUNT_RATE
        defaultInvoiceShouldNotBeFound("cashDiscountRate.greaterThanOrEqual=" + UPDATED_CASH_DISCOUNT_RATE);
    }

    @Test
    @Transactional
    void getAllInvoicesByCashDiscountRateIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        invoiceRepository.saveAndFlush(invoice);

        // Get all the invoiceList where cashDiscountRate is less than or equal to DEFAULT_CASH_DISCOUNT_RATE
        defaultInvoiceShouldBeFound("cashDiscountRate.lessThanOrEqual=" + DEFAULT_CASH_DISCOUNT_RATE);

        // Get all the invoiceList where cashDiscountRate is less than or equal to SMALLER_CASH_DISCOUNT_RATE
        defaultInvoiceShouldNotBeFound("cashDiscountRate.lessThanOrEqual=" + SMALLER_CASH_DISCOUNT_RATE);
    }

    @Test
    @Transactional
    void getAllInvoicesByCashDiscountRateIsLessThanSomething() throws Exception {
        // Initialize the database
        invoiceRepository.saveAndFlush(invoice);

        // Get all the invoiceList where cashDiscountRate is less than DEFAULT_CASH_DISCOUNT_RATE
        defaultInvoiceShouldNotBeFound("cashDiscountRate.lessThan=" + DEFAULT_CASH_DISCOUNT_RATE);

        // Get all the invoiceList where cashDiscountRate is less than UPDATED_CASH_DISCOUNT_RATE
        defaultInvoiceShouldBeFound("cashDiscountRate.lessThan=" + UPDATED_CASH_DISCOUNT_RATE);
    }

    @Test
    @Transactional
    void getAllInvoicesByCashDiscountRateIsGreaterThanSomething() throws Exception {
        // Initialize the database
        invoiceRepository.saveAndFlush(invoice);

        // Get all the invoiceList where cashDiscountRate is greater than DEFAULT_CASH_DISCOUNT_RATE
        defaultInvoiceShouldNotBeFound("cashDiscountRate.greaterThan=" + DEFAULT_CASH_DISCOUNT_RATE);

        // Get all the invoiceList where cashDiscountRate is greater than SMALLER_CASH_DISCOUNT_RATE
        defaultInvoiceShouldBeFound("cashDiscountRate.greaterThan=" + SMALLER_CASH_DISCOUNT_RATE);
    }

    @Test
    @Transactional
    void getAllInvoicesByCashDiscountDateIsEqualToSomething() throws Exception {
        // Initialize the database
        invoiceRepository.saveAndFlush(invoice);

        // Get all the invoiceList where cashDiscountDate equals to DEFAULT_CASH_DISCOUNT_DATE
        defaultInvoiceShouldBeFound("cashDiscountDate.equals=" + DEFAULT_CASH_DISCOUNT_DATE);

        // Get all the invoiceList where cashDiscountDate equals to UPDATED_CASH_DISCOUNT_DATE
        defaultInvoiceShouldNotBeFound("cashDiscountDate.equals=" + UPDATED_CASH_DISCOUNT_DATE);
    }

    @Test
    @Transactional
    void getAllInvoicesByCashDiscountDateIsNotEqualToSomething() throws Exception {
        // Initialize the database
        invoiceRepository.saveAndFlush(invoice);

        // Get all the invoiceList where cashDiscountDate not equals to DEFAULT_CASH_DISCOUNT_DATE
        defaultInvoiceShouldNotBeFound("cashDiscountDate.notEquals=" + DEFAULT_CASH_DISCOUNT_DATE);

        // Get all the invoiceList where cashDiscountDate not equals to UPDATED_CASH_DISCOUNT_DATE
        defaultInvoiceShouldBeFound("cashDiscountDate.notEquals=" + UPDATED_CASH_DISCOUNT_DATE);
    }

    @Test
    @Transactional
    void getAllInvoicesByCashDiscountDateIsInShouldWork() throws Exception {
        // Initialize the database
        invoiceRepository.saveAndFlush(invoice);

        // Get all the invoiceList where cashDiscountDate in DEFAULT_CASH_DISCOUNT_DATE or UPDATED_CASH_DISCOUNT_DATE
        defaultInvoiceShouldBeFound("cashDiscountDate.in=" + DEFAULT_CASH_DISCOUNT_DATE + "," + UPDATED_CASH_DISCOUNT_DATE);

        // Get all the invoiceList where cashDiscountDate equals to UPDATED_CASH_DISCOUNT_DATE
        defaultInvoiceShouldNotBeFound("cashDiscountDate.in=" + UPDATED_CASH_DISCOUNT_DATE);
    }

    @Test
    @Transactional
    void getAllInvoicesByCashDiscountDateIsNullOrNotNull() throws Exception {
        // Initialize the database
        invoiceRepository.saveAndFlush(invoice);

        // Get all the invoiceList where cashDiscountDate is not null
        defaultInvoiceShouldBeFound("cashDiscountDate.specified=true");

        // Get all the invoiceList where cashDiscountDate is null
        defaultInvoiceShouldNotBeFound("cashDiscountDate.specified=false");
    }

    @Test
    @Transactional
    void getAllInvoicesByCashDiscountDateIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        invoiceRepository.saveAndFlush(invoice);

        // Get all the invoiceList where cashDiscountDate is greater than or equal to DEFAULT_CASH_DISCOUNT_DATE
        defaultInvoiceShouldBeFound("cashDiscountDate.greaterThanOrEqual=" + DEFAULT_CASH_DISCOUNT_DATE);

        // Get all the invoiceList where cashDiscountDate is greater than or equal to UPDATED_CASH_DISCOUNT_DATE
        defaultInvoiceShouldNotBeFound("cashDiscountDate.greaterThanOrEqual=" + UPDATED_CASH_DISCOUNT_DATE);
    }

    @Test
    @Transactional
    void getAllInvoicesByCashDiscountDateIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        invoiceRepository.saveAndFlush(invoice);

        // Get all the invoiceList where cashDiscountDate is less than or equal to DEFAULT_CASH_DISCOUNT_DATE
        defaultInvoiceShouldBeFound("cashDiscountDate.lessThanOrEqual=" + DEFAULT_CASH_DISCOUNT_DATE);

        // Get all the invoiceList where cashDiscountDate is less than or equal to SMALLER_CASH_DISCOUNT_DATE
        defaultInvoiceShouldNotBeFound("cashDiscountDate.lessThanOrEqual=" + SMALLER_CASH_DISCOUNT_DATE);
    }

    @Test
    @Transactional
    void getAllInvoicesByCashDiscountDateIsLessThanSomething() throws Exception {
        // Initialize the database
        invoiceRepository.saveAndFlush(invoice);

        // Get all the invoiceList where cashDiscountDate is less than DEFAULT_CASH_DISCOUNT_DATE
        defaultInvoiceShouldNotBeFound("cashDiscountDate.lessThan=" + DEFAULT_CASH_DISCOUNT_DATE);

        // Get all the invoiceList where cashDiscountDate is less than UPDATED_CASH_DISCOUNT_DATE
        defaultInvoiceShouldBeFound("cashDiscountDate.lessThan=" + UPDATED_CASH_DISCOUNT_DATE);
    }

    @Test
    @Transactional
    void getAllInvoicesByCashDiscountDateIsGreaterThanSomething() throws Exception {
        // Initialize the database
        invoiceRepository.saveAndFlush(invoice);

        // Get all the invoiceList where cashDiscountDate is greater than DEFAULT_CASH_DISCOUNT_DATE
        defaultInvoiceShouldNotBeFound("cashDiscountDate.greaterThan=" + DEFAULT_CASH_DISCOUNT_DATE);

        // Get all the invoiceList where cashDiscountDate is greater than SMALLER_CASH_DISCOUNT_DATE
        defaultInvoiceShouldBeFound("cashDiscountDate.greaterThan=" + SMALLER_CASH_DISCOUNT_DATE);
    }

    @Test
    @Transactional
    void getAllInvoicesByTotalPaidIsEqualToSomething() throws Exception {
        // Initialize the database
        invoiceRepository.saveAndFlush(invoice);

        // Get all the invoiceList where totalPaid equals to DEFAULT_TOTAL_PAID
        defaultInvoiceShouldBeFound("totalPaid.equals=" + DEFAULT_TOTAL_PAID);

        // Get all the invoiceList where totalPaid equals to UPDATED_TOTAL_PAID
        defaultInvoiceShouldNotBeFound("totalPaid.equals=" + UPDATED_TOTAL_PAID);
    }

    @Test
    @Transactional
    void getAllInvoicesByTotalPaidIsNotEqualToSomething() throws Exception {
        // Initialize the database
        invoiceRepository.saveAndFlush(invoice);

        // Get all the invoiceList where totalPaid not equals to DEFAULT_TOTAL_PAID
        defaultInvoiceShouldNotBeFound("totalPaid.notEquals=" + DEFAULT_TOTAL_PAID);

        // Get all the invoiceList where totalPaid not equals to UPDATED_TOTAL_PAID
        defaultInvoiceShouldBeFound("totalPaid.notEquals=" + UPDATED_TOTAL_PAID);
    }

    @Test
    @Transactional
    void getAllInvoicesByTotalPaidIsInShouldWork() throws Exception {
        // Initialize the database
        invoiceRepository.saveAndFlush(invoice);

        // Get all the invoiceList where totalPaid in DEFAULT_TOTAL_PAID or UPDATED_TOTAL_PAID
        defaultInvoiceShouldBeFound("totalPaid.in=" + DEFAULT_TOTAL_PAID + "," + UPDATED_TOTAL_PAID);

        // Get all the invoiceList where totalPaid equals to UPDATED_TOTAL_PAID
        defaultInvoiceShouldNotBeFound("totalPaid.in=" + UPDATED_TOTAL_PAID);
    }

    @Test
    @Transactional
    void getAllInvoicesByTotalPaidIsNullOrNotNull() throws Exception {
        // Initialize the database
        invoiceRepository.saveAndFlush(invoice);

        // Get all the invoiceList where totalPaid is not null
        defaultInvoiceShouldBeFound("totalPaid.specified=true");

        // Get all the invoiceList where totalPaid is null
        defaultInvoiceShouldNotBeFound("totalPaid.specified=false");
    }

    @Test
    @Transactional
    void getAllInvoicesByTotalPaidIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        invoiceRepository.saveAndFlush(invoice);

        // Get all the invoiceList where totalPaid is greater than or equal to DEFAULT_TOTAL_PAID
        defaultInvoiceShouldBeFound("totalPaid.greaterThanOrEqual=" + DEFAULT_TOTAL_PAID);

        // Get all the invoiceList where totalPaid is greater than or equal to UPDATED_TOTAL_PAID
        defaultInvoiceShouldNotBeFound("totalPaid.greaterThanOrEqual=" + UPDATED_TOTAL_PAID);
    }

    @Test
    @Transactional
    void getAllInvoicesByTotalPaidIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        invoiceRepository.saveAndFlush(invoice);

        // Get all the invoiceList where totalPaid is less than or equal to DEFAULT_TOTAL_PAID
        defaultInvoiceShouldBeFound("totalPaid.lessThanOrEqual=" + DEFAULT_TOTAL_PAID);

        // Get all the invoiceList where totalPaid is less than or equal to SMALLER_TOTAL_PAID
        defaultInvoiceShouldNotBeFound("totalPaid.lessThanOrEqual=" + SMALLER_TOTAL_PAID);
    }

    @Test
    @Transactional
    void getAllInvoicesByTotalPaidIsLessThanSomething() throws Exception {
        // Initialize the database
        invoiceRepository.saveAndFlush(invoice);

        // Get all the invoiceList where totalPaid is less than DEFAULT_TOTAL_PAID
        defaultInvoiceShouldNotBeFound("totalPaid.lessThan=" + DEFAULT_TOTAL_PAID);

        // Get all the invoiceList where totalPaid is less than UPDATED_TOTAL_PAID
        defaultInvoiceShouldBeFound("totalPaid.lessThan=" + UPDATED_TOTAL_PAID);
    }

    @Test
    @Transactional
    void getAllInvoicesByTotalPaidIsGreaterThanSomething() throws Exception {
        // Initialize the database
        invoiceRepository.saveAndFlush(invoice);

        // Get all the invoiceList where totalPaid is greater than DEFAULT_TOTAL_PAID
        defaultInvoiceShouldNotBeFound("totalPaid.greaterThan=" + DEFAULT_TOTAL_PAID);

        // Get all the invoiceList where totalPaid is greater than SMALLER_TOTAL_PAID
        defaultInvoiceShouldBeFound("totalPaid.greaterThan=" + SMALLER_TOTAL_PAID);
    }

    @Test
    @Transactional
    void getAllInvoicesByPaidDateIsEqualToSomething() throws Exception {
        // Initialize the database
        invoiceRepository.saveAndFlush(invoice);

        // Get all the invoiceList where paidDate equals to DEFAULT_PAID_DATE
        defaultInvoiceShouldBeFound("paidDate.equals=" + DEFAULT_PAID_DATE);

        // Get all the invoiceList where paidDate equals to UPDATED_PAID_DATE
        defaultInvoiceShouldNotBeFound("paidDate.equals=" + UPDATED_PAID_DATE);
    }

    @Test
    @Transactional
    void getAllInvoicesByPaidDateIsNotEqualToSomething() throws Exception {
        // Initialize the database
        invoiceRepository.saveAndFlush(invoice);

        // Get all the invoiceList where paidDate not equals to DEFAULT_PAID_DATE
        defaultInvoiceShouldNotBeFound("paidDate.notEquals=" + DEFAULT_PAID_DATE);

        // Get all the invoiceList where paidDate not equals to UPDATED_PAID_DATE
        defaultInvoiceShouldBeFound("paidDate.notEquals=" + UPDATED_PAID_DATE);
    }

    @Test
    @Transactional
    void getAllInvoicesByPaidDateIsInShouldWork() throws Exception {
        // Initialize the database
        invoiceRepository.saveAndFlush(invoice);

        // Get all the invoiceList where paidDate in DEFAULT_PAID_DATE or UPDATED_PAID_DATE
        defaultInvoiceShouldBeFound("paidDate.in=" + DEFAULT_PAID_DATE + "," + UPDATED_PAID_DATE);

        // Get all the invoiceList where paidDate equals to UPDATED_PAID_DATE
        defaultInvoiceShouldNotBeFound("paidDate.in=" + UPDATED_PAID_DATE);
    }

    @Test
    @Transactional
    void getAllInvoicesByPaidDateIsNullOrNotNull() throws Exception {
        // Initialize the database
        invoiceRepository.saveAndFlush(invoice);

        // Get all the invoiceList where paidDate is not null
        defaultInvoiceShouldBeFound("paidDate.specified=true");

        // Get all the invoiceList where paidDate is null
        defaultInvoiceShouldNotBeFound("paidDate.specified=false");
    }

    @Test
    @Transactional
    void getAllInvoicesByPaidDateContainsSomething() throws Exception {
        // Initialize the database
        invoiceRepository.saveAndFlush(invoice);

        // Get all the invoiceList where paidDate contains DEFAULT_PAID_DATE
        defaultInvoiceShouldBeFound("paidDate.contains=" + DEFAULT_PAID_DATE);

        // Get all the invoiceList where paidDate contains UPDATED_PAID_DATE
        defaultInvoiceShouldNotBeFound("paidDate.contains=" + UPDATED_PAID_DATE);
    }

    @Test
    @Transactional
    void getAllInvoicesByPaidDateNotContainsSomething() throws Exception {
        // Initialize the database
        invoiceRepository.saveAndFlush(invoice);

        // Get all the invoiceList where paidDate does not contain DEFAULT_PAID_DATE
        defaultInvoiceShouldNotBeFound("paidDate.doesNotContain=" + DEFAULT_PAID_DATE);

        // Get all the invoiceList where paidDate does not contain UPDATED_PAID_DATE
        defaultInvoiceShouldBeFound("paidDate.doesNotContain=" + UPDATED_PAID_DATE);
    }

    @Test
    @Transactional
    void getAllInvoicesByIsrPositionIsEqualToSomething() throws Exception {
        // Initialize the database
        invoiceRepository.saveAndFlush(invoice);

        // Get all the invoiceList where isrPosition equals to DEFAULT_ISR_POSITION
        defaultInvoiceShouldBeFound("isrPosition.equals=" + DEFAULT_ISR_POSITION);

        // Get all the invoiceList where isrPosition equals to UPDATED_ISR_POSITION
        defaultInvoiceShouldNotBeFound("isrPosition.equals=" + UPDATED_ISR_POSITION);
    }

    @Test
    @Transactional
    void getAllInvoicesByIsrPositionIsNotEqualToSomething() throws Exception {
        // Initialize the database
        invoiceRepository.saveAndFlush(invoice);

        // Get all the invoiceList where isrPosition not equals to DEFAULT_ISR_POSITION
        defaultInvoiceShouldNotBeFound("isrPosition.notEquals=" + DEFAULT_ISR_POSITION);

        // Get all the invoiceList where isrPosition not equals to UPDATED_ISR_POSITION
        defaultInvoiceShouldBeFound("isrPosition.notEquals=" + UPDATED_ISR_POSITION);
    }

    @Test
    @Transactional
    void getAllInvoicesByIsrPositionIsInShouldWork() throws Exception {
        // Initialize the database
        invoiceRepository.saveAndFlush(invoice);

        // Get all the invoiceList where isrPosition in DEFAULT_ISR_POSITION or UPDATED_ISR_POSITION
        defaultInvoiceShouldBeFound("isrPosition.in=" + DEFAULT_ISR_POSITION + "," + UPDATED_ISR_POSITION);

        // Get all the invoiceList where isrPosition equals to UPDATED_ISR_POSITION
        defaultInvoiceShouldNotBeFound("isrPosition.in=" + UPDATED_ISR_POSITION);
    }

    @Test
    @Transactional
    void getAllInvoicesByIsrPositionIsNullOrNotNull() throws Exception {
        // Initialize the database
        invoiceRepository.saveAndFlush(invoice);

        // Get all the invoiceList where isrPosition is not null
        defaultInvoiceShouldBeFound("isrPosition.specified=true");

        // Get all the invoiceList where isrPosition is null
        defaultInvoiceShouldNotBeFound("isrPosition.specified=false");
    }

    @Test
    @Transactional
    void getAllInvoicesByIsrReferenceNumberIsEqualToSomething() throws Exception {
        // Initialize the database
        invoiceRepository.saveAndFlush(invoice);

        // Get all the invoiceList where isrReferenceNumber equals to DEFAULT_ISR_REFERENCE_NUMBER
        defaultInvoiceShouldBeFound("isrReferenceNumber.equals=" + DEFAULT_ISR_REFERENCE_NUMBER);

        // Get all the invoiceList where isrReferenceNumber equals to UPDATED_ISR_REFERENCE_NUMBER
        defaultInvoiceShouldNotBeFound("isrReferenceNumber.equals=" + UPDATED_ISR_REFERENCE_NUMBER);
    }

    @Test
    @Transactional
    void getAllInvoicesByIsrReferenceNumberIsNotEqualToSomething() throws Exception {
        // Initialize the database
        invoiceRepository.saveAndFlush(invoice);

        // Get all the invoiceList where isrReferenceNumber not equals to DEFAULT_ISR_REFERENCE_NUMBER
        defaultInvoiceShouldNotBeFound("isrReferenceNumber.notEquals=" + DEFAULT_ISR_REFERENCE_NUMBER);

        // Get all the invoiceList where isrReferenceNumber not equals to UPDATED_ISR_REFERENCE_NUMBER
        defaultInvoiceShouldBeFound("isrReferenceNumber.notEquals=" + UPDATED_ISR_REFERENCE_NUMBER);
    }

    @Test
    @Transactional
    void getAllInvoicesByIsrReferenceNumberIsInShouldWork() throws Exception {
        // Initialize the database
        invoiceRepository.saveAndFlush(invoice);

        // Get all the invoiceList where isrReferenceNumber in DEFAULT_ISR_REFERENCE_NUMBER or UPDATED_ISR_REFERENCE_NUMBER
        defaultInvoiceShouldBeFound("isrReferenceNumber.in=" + DEFAULT_ISR_REFERENCE_NUMBER + "," + UPDATED_ISR_REFERENCE_NUMBER);

        // Get all the invoiceList where isrReferenceNumber equals to UPDATED_ISR_REFERENCE_NUMBER
        defaultInvoiceShouldNotBeFound("isrReferenceNumber.in=" + UPDATED_ISR_REFERENCE_NUMBER);
    }

    @Test
    @Transactional
    void getAllInvoicesByIsrReferenceNumberIsNullOrNotNull() throws Exception {
        // Initialize the database
        invoiceRepository.saveAndFlush(invoice);

        // Get all the invoiceList where isrReferenceNumber is not null
        defaultInvoiceShouldBeFound("isrReferenceNumber.specified=true");

        // Get all the invoiceList where isrReferenceNumber is null
        defaultInvoiceShouldNotBeFound("isrReferenceNumber.specified=false");
    }

    @Test
    @Transactional
    void getAllInvoicesByIsrReferenceNumberContainsSomething() throws Exception {
        // Initialize the database
        invoiceRepository.saveAndFlush(invoice);

        // Get all the invoiceList where isrReferenceNumber contains DEFAULT_ISR_REFERENCE_NUMBER
        defaultInvoiceShouldBeFound("isrReferenceNumber.contains=" + DEFAULT_ISR_REFERENCE_NUMBER);

        // Get all the invoiceList where isrReferenceNumber contains UPDATED_ISR_REFERENCE_NUMBER
        defaultInvoiceShouldNotBeFound("isrReferenceNumber.contains=" + UPDATED_ISR_REFERENCE_NUMBER);
    }

    @Test
    @Transactional
    void getAllInvoicesByIsrReferenceNumberNotContainsSomething() throws Exception {
        // Initialize the database
        invoiceRepository.saveAndFlush(invoice);

        // Get all the invoiceList where isrReferenceNumber does not contain DEFAULT_ISR_REFERENCE_NUMBER
        defaultInvoiceShouldNotBeFound("isrReferenceNumber.doesNotContain=" + DEFAULT_ISR_REFERENCE_NUMBER);

        // Get all the invoiceList where isrReferenceNumber does not contain UPDATED_ISR_REFERENCE_NUMBER
        defaultInvoiceShouldBeFound("isrReferenceNumber.doesNotContain=" + UPDATED_ISR_REFERENCE_NUMBER);
    }

    @Test
    @Transactional
    void getAllInvoicesByPaymentLinkPaypalIsEqualToSomething() throws Exception {
        // Initialize the database
        invoiceRepository.saveAndFlush(invoice);

        // Get all the invoiceList where paymentLinkPaypal equals to DEFAULT_PAYMENT_LINK_PAYPAL
        defaultInvoiceShouldBeFound("paymentLinkPaypal.equals=" + DEFAULT_PAYMENT_LINK_PAYPAL);

        // Get all the invoiceList where paymentLinkPaypal equals to UPDATED_PAYMENT_LINK_PAYPAL
        defaultInvoiceShouldNotBeFound("paymentLinkPaypal.equals=" + UPDATED_PAYMENT_LINK_PAYPAL);
    }

    @Test
    @Transactional
    void getAllInvoicesByPaymentLinkPaypalIsNotEqualToSomething() throws Exception {
        // Initialize the database
        invoiceRepository.saveAndFlush(invoice);

        // Get all the invoiceList where paymentLinkPaypal not equals to DEFAULT_PAYMENT_LINK_PAYPAL
        defaultInvoiceShouldNotBeFound("paymentLinkPaypal.notEquals=" + DEFAULT_PAYMENT_LINK_PAYPAL);

        // Get all the invoiceList where paymentLinkPaypal not equals to UPDATED_PAYMENT_LINK_PAYPAL
        defaultInvoiceShouldBeFound("paymentLinkPaypal.notEquals=" + UPDATED_PAYMENT_LINK_PAYPAL);
    }

    @Test
    @Transactional
    void getAllInvoicesByPaymentLinkPaypalIsInShouldWork() throws Exception {
        // Initialize the database
        invoiceRepository.saveAndFlush(invoice);

        // Get all the invoiceList where paymentLinkPaypal in DEFAULT_PAYMENT_LINK_PAYPAL or UPDATED_PAYMENT_LINK_PAYPAL
        defaultInvoiceShouldBeFound("paymentLinkPaypal.in=" + DEFAULT_PAYMENT_LINK_PAYPAL + "," + UPDATED_PAYMENT_LINK_PAYPAL);

        // Get all the invoiceList where paymentLinkPaypal equals to UPDATED_PAYMENT_LINK_PAYPAL
        defaultInvoiceShouldNotBeFound("paymentLinkPaypal.in=" + UPDATED_PAYMENT_LINK_PAYPAL);
    }

    @Test
    @Transactional
    void getAllInvoicesByPaymentLinkPaypalIsNullOrNotNull() throws Exception {
        // Initialize the database
        invoiceRepository.saveAndFlush(invoice);

        // Get all the invoiceList where paymentLinkPaypal is not null
        defaultInvoiceShouldBeFound("paymentLinkPaypal.specified=true");

        // Get all the invoiceList where paymentLinkPaypal is null
        defaultInvoiceShouldNotBeFound("paymentLinkPaypal.specified=false");
    }

    @Test
    @Transactional
    void getAllInvoicesByPaymentLinkPaypalUrlIsEqualToSomething() throws Exception {
        // Initialize the database
        invoiceRepository.saveAndFlush(invoice);

        // Get all the invoiceList where paymentLinkPaypalUrl equals to DEFAULT_PAYMENT_LINK_PAYPAL_URL
        defaultInvoiceShouldBeFound("paymentLinkPaypalUrl.equals=" + DEFAULT_PAYMENT_LINK_PAYPAL_URL);

        // Get all the invoiceList where paymentLinkPaypalUrl equals to UPDATED_PAYMENT_LINK_PAYPAL_URL
        defaultInvoiceShouldNotBeFound("paymentLinkPaypalUrl.equals=" + UPDATED_PAYMENT_LINK_PAYPAL_URL);
    }

    @Test
    @Transactional
    void getAllInvoicesByPaymentLinkPaypalUrlIsNotEqualToSomething() throws Exception {
        // Initialize the database
        invoiceRepository.saveAndFlush(invoice);

        // Get all the invoiceList where paymentLinkPaypalUrl not equals to DEFAULT_PAYMENT_LINK_PAYPAL_URL
        defaultInvoiceShouldNotBeFound("paymentLinkPaypalUrl.notEquals=" + DEFAULT_PAYMENT_LINK_PAYPAL_URL);

        // Get all the invoiceList where paymentLinkPaypalUrl not equals to UPDATED_PAYMENT_LINK_PAYPAL_URL
        defaultInvoiceShouldBeFound("paymentLinkPaypalUrl.notEquals=" + UPDATED_PAYMENT_LINK_PAYPAL_URL);
    }

    @Test
    @Transactional
    void getAllInvoicesByPaymentLinkPaypalUrlIsInShouldWork() throws Exception {
        // Initialize the database
        invoiceRepository.saveAndFlush(invoice);

        // Get all the invoiceList where paymentLinkPaypalUrl in DEFAULT_PAYMENT_LINK_PAYPAL_URL or UPDATED_PAYMENT_LINK_PAYPAL_URL
        defaultInvoiceShouldBeFound("paymentLinkPaypalUrl.in=" + DEFAULT_PAYMENT_LINK_PAYPAL_URL + "," + UPDATED_PAYMENT_LINK_PAYPAL_URL);

        // Get all the invoiceList where paymentLinkPaypalUrl equals to UPDATED_PAYMENT_LINK_PAYPAL_URL
        defaultInvoiceShouldNotBeFound("paymentLinkPaypalUrl.in=" + UPDATED_PAYMENT_LINK_PAYPAL_URL);
    }

    @Test
    @Transactional
    void getAllInvoicesByPaymentLinkPaypalUrlIsNullOrNotNull() throws Exception {
        // Initialize the database
        invoiceRepository.saveAndFlush(invoice);

        // Get all the invoiceList where paymentLinkPaypalUrl is not null
        defaultInvoiceShouldBeFound("paymentLinkPaypalUrl.specified=true");

        // Get all the invoiceList where paymentLinkPaypalUrl is null
        defaultInvoiceShouldNotBeFound("paymentLinkPaypalUrl.specified=false");
    }

    @Test
    @Transactional
    void getAllInvoicesByPaymentLinkPaypalUrlContainsSomething() throws Exception {
        // Initialize the database
        invoiceRepository.saveAndFlush(invoice);

        // Get all the invoiceList where paymentLinkPaypalUrl contains DEFAULT_PAYMENT_LINK_PAYPAL_URL
        defaultInvoiceShouldBeFound("paymentLinkPaypalUrl.contains=" + DEFAULT_PAYMENT_LINK_PAYPAL_URL);

        // Get all the invoiceList where paymentLinkPaypalUrl contains UPDATED_PAYMENT_LINK_PAYPAL_URL
        defaultInvoiceShouldNotBeFound("paymentLinkPaypalUrl.contains=" + UPDATED_PAYMENT_LINK_PAYPAL_URL);
    }

    @Test
    @Transactional
    void getAllInvoicesByPaymentLinkPaypalUrlNotContainsSomething() throws Exception {
        // Initialize the database
        invoiceRepository.saveAndFlush(invoice);

        // Get all the invoiceList where paymentLinkPaypalUrl does not contain DEFAULT_PAYMENT_LINK_PAYPAL_URL
        defaultInvoiceShouldNotBeFound("paymentLinkPaypalUrl.doesNotContain=" + DEFAULT_PAYMENT_LINK_PAYPAL_URL);

        // Get all the invoiceList where paymentLinkPaypalUrl does not contain UPDATED_PAYMENT_LINK_PAYPAL_URL
        defaultInvoiceShouldBeFound("paymentLinkPaypalUrl.doesNotContain=" + UPDATED_PAYMENT_LINK_PAYPAL_URL);
    }

    @Test
    @Transactional
    void getAllInvoicesByPaymentLinkPostfinanceIsEqualToSomething() throws Exception {
        // Initialize the database
        invoiceRepository.saveAndFlush(invoice);

        // Get all the invoiceList where paymentLinkPostfinance equals to DEFAULT_PAYMENT_LINK_POSTFINANCE
        defaultInvoiceShouldBeFound("paymentLinkPostfinance.equals=" + DEFAULT_PAYMENT_LINK_POSTFINANCE);

        // Get all the invoiceList where paymentLinkPostfinance equals to UPDATED_PAYMENT_LINK_POSTFINANCE
        defaultInvoiceShouldNotBeFound("paymentLinkPostfinance.equals=" + UPDATED_PAYMENT_LINK_POSTFINANCE);
    }

    @Test
    @Transactional
    void getAllInvoicesByPaymentLinkPostfinanceIsNotEqualToSomething() throws Exception {
        // Initialize the database
        invoiceRepository.saveAndFlush(invoice);

        // Get all the invoiceList where paymentLinkPostfinance not equals to DEFAULT_PAYMENT_LINK_POSTFINANCE
        defaultInvoiceShouldNotBeFound("paymentLinkPostfinance.notEquals=" + DEFAULT_PAYMENT_LINK_POSTFINANCE);

        // Get all the invoiceList where paymentLinkPostfinance not equals to UPDATED_PAYMENT_LINK_POSTFINANCE
        defaultInvoiceShouldBeFound("paymentLinkPostfinance.notEquals=" + UPDATED_PAYMENT_LINK_POSTFINANCE);
    }

    @Test
    @Transactional
    void getAllInvoicesByPaymentLinkPostfinanceIsInShouldWork() throws Exception {
        // Initialize the database
        invoiceRepository.saveAndFlush(invoice);

        // Get all the invoiceList where paymentLinkPostfinance in DEFAULT_PAYMENT_LINK_POSTFINANCE or UPDATED_PAYMENT_LINK_POSTFINANCE
        defaultInvoiceShouldBeFound(
            "paymentLinkPostfinance.in=" + DEFAULT_PAYMENT_LINK_POSTFINANCE + "," + UPDATED_PAYMENT_LINK_POSTFINANCE
        );

        // Get all the invoiceList where paymentLinkPostfinance equals to UPDATED_PAYMENT_LINK_POSTFINANCE
        defaultInvoiceShouldNotBeFound("paymentLinkPostfinance.in=" + UPDATED_PAYMENT_LINK_POSTFINANCE);
    }

    @Test
    @Transactional
    void getAllInvoicesByPaymentLinkPostfinanceIsNullOrNotNull() throws Exception {
        // Initialize the database
        invoiceRepository.saveAndFlush(invoice);

        // Get all the invoiceList where paymentLinkPostfinance is not null
        defaultInvoiceShouldBeFound("paymentLinkPostfinance.specified=true");

        // Get all the invoiceList where paymentLinkPostfinance is null
        defaultInvoiceShouldNotBeFound("paymentLinkPostfinance.specified=false");
    }

    @Test
    @Transactional
    void getAllInvoicesByPaymentLinkPostfinanceUrlIsEqualToSomething() throws Exception {
        // Initialize the database
        invoiceRepository.saveAndFlush(invoice);

        // Get all the invoiceList where paymentLinkPostfinanceUrl equals to DEFAULT_PAYMENT_LINK_POSTFINANCE_URL
        defaultInvoiceShouldBeFound("paymentLinkPostfinanceUrl.equals=" + DEFAULT_PAYMENT_LINK_POSTFINANCE_URL);

        // Get all the invoiceList where paymentLinkPostfinanceUrl equals to UPDATED_PAYMENT_LINK_POSTFINANCE_URL
        defaultInvoiceShouldNotBeFound("paymentLinkPostfinanceUrl.equals=" + UPDATED_PAYMENT_LINK_POSTFINANCE_URL);
    }

    @Test
    @Transactional
    void getAllInvoicesByPaymentLinkPostfinanceUrlIsNotEqualToSomething() throws Exception {
        // Initialize the database
        invoiceRepository.saveAndFlush(invoice);

        // Get all the invoiceList where paymentLinkPostfinanceUrl not equals to DEFAULT_PAYMENT_LINK_POSTFINANCE_URL
        defaultInvoiceShouldNotBeFound("paymentLinkPostfinanceUrl.notEquals=" + DEFAULT_PAYMENT_LINK_POSTFINANCE_URL);

        // Get all the invoiceList where paymentLinkPostfinanceUrl not equals to UPDATED_PAYMENT_LINK_POSTFINANCE_URL
        defaultInvoiceShouldBeFound("paymentLinkPostfinanceUrl.notEquals=" + UPDATED_PAYMENT_LINK_POSTFINANCE_URL);
    }

    @Test
    @Transactional
    void getAllInvoicesByPaymentLinkPostfinanceUrlIsInShouldWork() throws Exception {
        // Initialize the database
        invoiceRepository.saveAndFlush(invoice);

        // Get all the invoiceList where paymentLinkPostfinanceUrl in DEFAULT_PAYMENT_LINK_POSTFINANCE_URL or UPDATED_PAYMENT_LINK_POSTFINANCE_URL
        defaultInvoiceShouldBeFound(
            "paymentLinkPostfinanceUrl.in=" + DEFAULT_PAYMENT_LINK_POSTFINANCE_URL + "," + UPDATED_PAYMENT_LINK_POSTFINANCE_URL
        );

        // Get all the invoiceList where paymentLinkPostfinanceUrl equals to UPDATED_PAYMENT_LINK_POSTFINANCE_URL
        defaultInvoiceShouldNotBeFound("paymentLinkPostfinanceUrl.in=" + UPDATED_PAYMENT_LINK_POSTFINANCE_URL);
    }

    @Test
    @Transactional
    void getAllInvoicesByPaymentLinkPostfinanceUrlIsNullOrNotNull() throws Exception {
        // Initialize the database
        invoiceRepository.saveAndFlush(invoice);

        // Get all the invoiceList where paymentLinkPostfinanceUrl is not null
        defaultInvoiceShouldBeFound("paymentLinkPostfinanceUrl.specified=true");

        // Get all the invoiceList where paymentLinkPostfinanceUrl is null
        defaultInvoiceShouldNotBeFound("paymentLinkPostfinanceUrl.specified=false");
    }

    @Test
    @Transactional
    void getAllInvoicesByPaymentLinkPostfinanceUrlContainsSomething() throws Exception {
        // Initialize the database
        invoiceRepository.saveAndFlush(invoice);

        // Get all the invoiceList where paymentLinkPostfinanceUrl contains DEFAULT_PAYMENT_LINK_POSTFINANCE_URL
        defaultInvoiceShouldBeFound("paymentLinkPostfinanceUrl.contains=" + DEFAULT_PAYMENT_LINK_POSTFINANCE_URL);

        // Get all the invoiceList where paymentLinkPostfinanceUrl contains UPDATED_PAYMENT_LINK_POSTFINANCE_URL
        defaultInvoiceShouldNotBeFound("paymentLinkPostfinanceUrl.contains=" + UPDATED_PAYMENT_LINK_POSTFINANCE_URL);
    }

    @Test
    @Transactional
    void getAllInvoicesByPaymentLinkPostfinanceUrlNotContainsSomething() throws Exception {
        // Initialize the database
        invoiceRepository.saveAndFlush(invoice);

        // Get all the invoiceList where paymentLinkPostfinanceUrl does not contain DEFAULT_PAYMENT_LINK_POSTFINANCE_URL
        defaultInvoiceShouldNotBeFound("paymentLinkPostfinanceUrl.doesNotContain=" + DEFAULT_PAYMENT_LINK_POSTFINANCE_URL);

        // Get all the invoiceList where paymentLinkPostfinanceUrl does not contain UPDATED_PAYMENT_LINK_POSTFINANCE_URL
        defaultInvoiceShouldBeFound("paymentLinkPostfinanceUrl.doesNotContain=" + UPDATED_PAYMENT_LINK_POSTFINANCE_URL);
    }

    @Test
    @Transactional
    void getAllInvoicesByPaymentLinkPayrexxIsEqualToSomething() throws Exception {
        // Initialize the database
        invoiceRepository.saveAndFlush(invoice);

        // Get all the invoiceList where paymentLinkPayrexx equals to DEFAULT_PAYMENT_LINK_PAYREXX
        defaultInvoiceShouldBeFound("paymentLinkPayrexx.equals=" + DEFAULT_PAYMENT_LINK_PAYREXX);

        // Get all the invoiceList where paymentLinkPayrexx equals to UPDATED_PAYMENT_LINK_PAYREXX
        defaultInvoiceShouldNotBeFound("paymentLinkPayrexx.equals=" + UPDATED_PAYMENT_LINK_PAYREXX);
    }

    @Test
    @Transactional
    void getAllInvoicesByPaymentLinkPayrexxIsNotEqualToSomething() throws Exception {
        // Initialize the database
        invoiceRepository.saveAndFlush(invoice);

        // Get all the invoiceList where paymentLinkPayrexx not equals to DEFAULT_PAYMENT_LINK_PAYREXX
        defaultInvoiceShouldNotBeFound("paymentLinkPayrexx.notEquals=" + DEFAULT_PAYMENT_LINK_PAYREXX);

        // Get all the invoiceList where paymentLinkPayrexx not equals to UPDATED_PAYMENT_LINK_PAYREXX
        defaultInvoiceShouldBeFound("paymentLinkPayrexx.notEquals=" + UPDATED_PAYMENT_LINK_PAYREXX);
    }

    @Test
    @Transactional
    void getAllInvoicesByPaymentLinkPayrexxIsInShouldWork() throws Exception {
        // Initialize the database
        invoiceRepository.saveAndFlush(invoice);

        // Get all the invoiceList where paymentLinkPayrexx in DEFAULT_PAYMENT_LINK_PAYREXX or UPDATED_PAYMENT_LINK_PAYREXX
        defaultInvoiceShouldBeFound("paymentLinkPayrexx.in=" + DEFAULT_PAYMENT_LINK_PAYREXX + "," + UPDATED_PAYMENT_LINK_PAYREXX);

        // Get all the invoiceList where paymentLinkPayrexx equals to UPDATED_PAYMENT_LINK_PAYREXX
        defaultInvoiceShouldNotBeFound("paymentLinkPayrexx.in=" + UPDATED_PAYMENT_LINK_PAYREXX);
    }

    @Test
    @Transactional
    void getAllInvoicesByPaymentLinkPayrexxIsNullOrNotNull() throws Exception {
        // Initialize the database
        invoiceRepository.saveAndFlush(invoice);

        // Get all the invoiceList where paymentLinkPayrexx is not null
        defaultInvoiceShouldBeFound("paymentLinkPayrexx.specified=true");

        // Get all the invoiceList where paymentLinkPayrexx is null
        defaultInvoiceShouldNotBeFound("paymentLinkPayrexx.specified=false");
    }

    @Test
    @Transactional
    void getAllInvoicesByPaymentLinkPayrexxUrlIsEqualToSomething() throws Exception {
        // Initialize the database
        invoiceRepository.saveAndFlush(invoice);

        // Get all the invoiceList where paymentLinkPayrexxUrl equals to DEFAULT_PAYMENT_LINK_PAYREXX_URL
        defaultInvoiceShouldBeFound("paymentLinkPayrexxUrl.equals=" + DEFAULT_PAYMENT_LINK_PAYREXX_URL);

        // Get all the invoiceList where paymentLinkPayrexxUrl equals to UPDATED_PAYMENT_LINK_PAYREXX_URL
        defaultInvoiceShouldNotBeFound("paymentLinkPayrexxUrl.equals=" + UPDATED_PAYMENT_LINK_PAYREXX_URL);
    }

    @Test
    @Transactional
    void getAllInvoicesByPaymentLinkPayrexxUrlIsNotEqualToSomething() throws Exception {
        // Initialize the database
        invoiceRepository.saveAndFlush(invoice);

        // Get all the invoiceList where paymentLinkPayrexxUrl not equals to DEFAULT_PAYMENT_LINK_PAYREXX_URL
        defaultInvoiceShouldNotBeFound("paymentLinkPayrexxUrl.notEquals=" + DEFAULT_PAYMENT_LINK_PAYREXX_URL);

        // Get all the invoiceList where paymentLinkPayrexxUrl not equals to UPDATED_PAYMENT_LINK_PAYREXX_URL
        defaultInvoiceShouldBeFound("paymentLinkPayrexxUrl.notEquals=" + UPDATED_PAYMENT_LINK_PAYREXX_URL);
    }

    @Test
    @Transactional
    void getAllInvoicesByPaymentLinkPayrexxUrlIsInShouldWork() throws Exception {
        // Initialize the database
        invoiceRepository.saveAndFlush(invoice);

        // Get all the invoiceList where paymentLinkPayrexxUrl in DEFAULT_PAYMENT_LINK_PAYREXX_URL or UPDATED_PAYMENT_LINK_PAYREXX_URL
        defaultInvoiceShouldBeFound(
            "paymentLinkPayrexxUrl.in=" + DEFAULT_PAYMENT_LINK_PAYREXX_URL + "," + UPDATED_PAYMENT_LINK_PAYREXX_URL
        );

        // Get all the invoiceList where paymentLinkPayrexxUrl equals to UPDATED_PAYMENT_LINK_PAYREXX_URL
        defaultInvoiceShouldNotBeFound("paymentLinkPayrexxUrl.in=" + UPDATED_PAYMENT_LINK_PAYREXX_URL);
    }

    @Test
    @Transactional
    void getAllInvoicesByPaymentLinkPayrexxUrlIsNullOrNotNull() throws Exception {
        // Initialize the database
        invoiceRepository.saveAndFlush(invoice);

        // Get all the invoiceList where paymentLinkPayrexxUrl is not null
        defaultInvoiceShouldBeFound("paymentLinkPayrexxUrl.specified=true");

        // Get all the invoiceList where paymentLinkPayrexxUrl is null
        defaultInvoiceShouldNotBeFound("paymentLinkPayrexxUrl.specified=false");
    }

    @Test
    @Transactional
    void getAllInvoicesByPaymentLinkPayrexxUrlContainsSomething() throws Exception {
        // Initialize the database
        invoiceRepository.saveAndFlush(invoice);

        // Get all the invoiceList where paymentLinkPayrexxUrl contains DEFAULT_PAYMENT_LINK_PAYREXX_URL
        defaultInvoiceShouldBeFound("paymentLinkPayrexxUrl.contains=" + DEFAULT_PAYMENT_LINK_PAYREXX_URL);

        // Get all the invoiceList where paymentLinkPayrexxUrl contains UPDATED_PAYMENT_LINK_PAYREXX_URL
        defaultInvoiceShouldNotBeFound("paymentLinkPayrexxUrl.contains=" + UPDATED_PAYMENT_LINK_PAYREXX_URL);
    }

    @Test
    @Transactional
    void getAllInvoicesByPaymentLinkPayrexxUrlNotContainsSomething() throws Exception {
        // Initialize the database
        invoiceRepository.saveAndFlush(invoice);

        // Get all the invoiceList where paymentLinkPayrexxUrl does not contain DEFAULT_PAYMENT_LINK_PAYREXX_URL
        defaultInvoiceShouldNotBeFound("paymentLinkPayrexxUrl.doesNotContain=" + DEFAULT_PAYMENT_LINK_PAYREXX_URL);

        // Get all the invoiceList where paymentLinkPayrexxUrl does not contain UPDATED_PAYMENT_LINK_PAYREXX_URL
        defaultInvoiceShouldBeFound("paymentLinkPayrexxUrl.doesNotContain=" + UPDATED_PAYMENT_LINK_PAYREXX_URL);
    }

    @Test
    @Transactional
    void getAllInvoicesByPaymentLinkSmartcommerceIsEqualToSomething() throws Exception {
        // Initialize the database
        invoiceRepository.saveAndFlush(invoice);

        // Get all the invoiceList where paymentLinkSmartcommerce equals to DEFAULT_PAYMENT_LINK_SMARTCOMMERCE
        defaultInvoiceShouldBeFound("paymentLinkSmartcommerce.equals=" + DEFAULT_PAYMENT_LINK_SMARTCOMMERCE);

        // Get all the invoiceList where paymentLinkSmartcommerce equals to UPDATED_PAYMENT_LINK_SMARTCOMMERCE
        defaultInvoiceShouldNotBeFound("paymentLinkSmartcommerce.equals=" + UPDATED_PAYMENT_LINK_SMARTCOMMERCE);
    }

    @Test
    @Transactional
    void getAllInvoicesByPaymentLinkSmartcommerceIsNotEqualToSomething() throws Exception {
        // Initialize the database
        invoiceRepository.saveAndFlush(invoice);

        // Get all the invoiceList where paymentLinkSmartcommerce not equals to DEFAULT_PAYMENT_LINK_SMARTCOMMERCE
        defaultInvoiceShouldNotBeFound("paymentLinkSmartcommerce.notEquals=" + DEFAULT_PAYMENT_LINK_SMARTCOMMERCE);

        // Get all the invoiceList where paymentLinkSmartcommerce not equals to UPDATED_PAYMENT_LINK_SMARTCOMMERCE
        defaultInvoiceShouldBeFound("paymentLinkSmartcommerce.notEquals=" + UPDATED_PAYMENT_LINK_SMARTCOMMERCE);
    }

    @Test
    @Transactional
    void getAllInvoicesByPaymentLinkSmartcommerceIsInShouldWork() throws Exception {
        // Initialize the database
        invoiceRepository.saveAndFlush(invoice);

        // Get all the invoiceList where paymentLinkSmartcommerce in DEFAULT_PAYMENT_LINK_SMARTCOMMERCE or UPDATED_PAYMENT_LINK_SMARTCOMMERCE
        defaultInvoiceShouldBeFound(
            "paymentLinkSmartcommerce.in=" + DEFAULT_PAYMENT_LINK_SMARTCOMMERCE + "," + UPDATED_PAYMENT_LINK_SMARTCOMMERCE
        );

        // Get all the invoiceList where paymentLinkSmartcommerce equals to UPDATED_PAYMENT_LINK_SMARTCOMMERCE
        defaultInvoiceShouldNotBeFound("paymentLinkSmartcommerce.in=" + UPDATED_PAYMENT_LINK_SMARTCOMMERCE);
    }

    @Test
    @Transactional
    void getAllInvoicesByPaymentLinkSmartcommerceIsNullOrNotNull() throws Exception {
        // Initialize the database
        invoiceRepository.saveAndFlush(invoice);

        // Get all the invoiceList where paymentLinkSmartcommerce is not null
        defaultInvoiceShouldBeFound("paymentLinkSmartcommerce.specified=true");

        // Get all the invoiceList where paymentLinkSmartcommerce is null
        defaultInvoiceShouldNotBeFound("paymentLinkSmartcommerce.specified=false");
    }

    @Test
    @Transactional
    void getAllInvoicesByPaymentLinkSmartcommerceUrlIsEqualToSomething() throws Exception {
        // Initialize the database
        invoiceRepository.saveAndFlush(invoice);

        // Get all the invoiceList where paymentLinkSmartcommerceUrl equals to DEFAULT_PAYMENT_LINK_SMARTCOMMERCE_URL
        defaultInvoiceShouldBeFound("paymentLinkSmartcommerceUrl.equals=" + DEFAULT_PAYMENT_LINK_SMARTCOMMERCE_URL);

        // Get all the invoiceList where paymentLinkSmartcommerceUrl equals to UPDATED_PAYMENT_LINK_SMARTCOMMERCE_URL
        defaultInvoiceShouldNotBeFound("paymentLinkSmartcommerceUrl.equals=" + UPDATED_PAYMENT_LINK_SMARTCOMMERCE_URL);
    }

    @Test
    @Transactional
    void getAllInvoicesByPaymentLinkSmartcommerceUrlIsNotEqualToSomething() throws Exception {
        // Initialize the database
        invoiceRepository.saveAndFlush(invoice);

        // Get all the invoiceList where paymentLinkSmartcommerceUrl not equals to DEFAULT_PAYMENT_LINK_SMARTCOMMERCE_URL
        defaultInvoiceShouldNotBeFound("paymentLinkSmartcommerceUrl.notEquals=" + DEFAULT_PAYMENT_LINK_SMARTCOMMERCE_URL);

        // Get all the invoiceList where paymentLinkSmartcommerceUrl not equals to UPDATED_PAYMENT_LINK_SMARTCOMMERCE_URL
        defaultInvoiceShouldBeFound("paymentLinkSmartcommerceUrl.notEquals=" + UPDATED_PAYMENT_LINK_SMARTCOMMERCE_URL);
    }

    @Test
    @Transactional
    void getAllInvoicesByPaymentLinkSmartcommerceUrlIsInShouldWork() throws Exception {
        // Initialize the database
        invoiceRepository.saveAndFlush(invoice);

        // Get all the invoiceList where paymentLinkSmartcommerceUrl in DEFAULT_PAYMENT_LINK_SMARTCOMMERCE_URL or UPDATED_PAYMENT_LINK_SMARTCOMMERCE_URL
        defaultInvoiceShouldBeFound(
            "paymentLinkSmartcommerceUrl.in=" + DEFAULT_PAYMENT_LINK_SMARTCOMMERCE_URL + "," + UPDATED_PAYMENT_LINK_SMARTCOMMERCE_URL
        );

        // Get all the invoiceList where paymentLinkSmartcommerceUrl equals to UPDATED_PAYMENT_LINK_SMARTCOMMERCE_URL
        defaultInvoiceShouldNotBeFound("paymentLinkSmartcommerceUrl.in=" + UPDATED_PAYMENT_LINK_SMARTCOMMERCE_URL);
    }

    @Test
    @Transactional
    void getAllInvoicesByPaymentLinkSmartcommerceUrlIsNullOrNotNull() throws Exception {
        // Initialize the database
        invoiceRepository.saveAndFlush(invoice);

        // Get all the invoiceList where paymentLinkSmartcommerceUrl is not null
        defaultInvoiceShouldBeFound("paymentLinkSmartcommerceUrl.specified=true");

        // Get all the invoiceList where paymentLinkSmartcommerceUrl is null
        defaultInvoiceShouldNotBeFound("paymentLinkSmartcommerceUrl.specified=false");
    }

    @Test
    @Transactional
    void getAllInvoicesByPaymentLinkSmartcommerceUrlContainsSomething() throws Exception {
        // Initialize the database
        invoiceRepository.saveAndFlush(invoice);

        // Get all the invoiceList where paymentLinkSmartcommerceUrl contains DEFAULT_PAYMENT_LINK_SMARTCOMMERCE_URL
        defaultInvoiceShouldBeFound("paymentLinkSmartcommerceUrl.contains=" + DEFAULT_PAYMENT_LINK_SMARTCOMMERCE_URL);

        // Get all the invoiceList where paymentLinkSmartcommerceUrl contains UPDATED_PAYMENT_LINK_SMARTCOMMERCE_URL
        defaultInvoiceShouldNotBeFound("paymentLinkSmartcommerceUrl.contains=" + UPDATED_PAYMENT_LINK_SMARTCOMMERCE_URL);
    }

    @Test
    @Transactional
    void getAllInvoicesByPaymentLinkSmartcommerceUrlNotContainsSomething() throws Exception {
        // Initialize the database
        invoiceRepository.saveAndFlush(invoice);

        // Get all the invoiceList where paymentLinkSmartcommerceUrl does not contain DEFAULT_PAYMENT_LINK_SMARTCOMMERCE_URL
        defaultInvoiceShouldNotBeFound("paymentLinkSmartcommerceUrl.doesNotContain=" + DEFAULT_PAYMENT_LINK_SMARTCOMMERCE_URL);

        // Get all the invoiceList where paymentLinkSmartcommerceUrl does not contain UPDATED_PAYMENT_LINK_SMARTCOMMERCE_URL
        defaultInvoiceShouldBeFound("paymentLinkSmartcommerceUrl.doesNotContain=" + UPDATED_PAYMENT_LINK_SMARTCOMMERCE_URL);
    }

    @Test
    @Transactional
    void getAllInvoicesByLanguageIsEqualToSomething() throws Exception {
        // Initialize the database
        invoiceRepository.saveAndFlush(invoice);

        // Get all the invoiceList where language equals to DEFAULT_LANGUAGE
        defaultInvoiceShouldBeFound("language.equals=" + DEFAULT_LANGUAGE);

        // Get all the invoiceList where language equals to UPDATED_LANGUAGE
        defaultInvoiceShouldNotBeFound("language.equals=" + UPDATED_LANGUAGE);
    }

    @Test
    @Transactional
    void getAllInvoicesByLanguageIsNotEqualToSomething() throws Exception {
        // Initialize the database
        invoiceRepository.saveAndFlush(invoice);

        // Get all the invoiceList where language not equals to DEFAULT_LANGUAGE
        defaultInvoiceShouldNotBeFound("language.notEquals=" + DEFAULT_LANGUAGE);

        // Get all the invoiceList where language not equals to UPDATED_LANGUAGE
        defaultInvoiceShouldBeFound("language.notEquals=" + UPDATED_LANGUAGE);
    }

    @Test
    @Transactional
    void getAllInvoicesByLanguageIsInShouldWork() throws Exception {
        // Initialize the database
        invoiceRepository.saveAndFlush(invoice);

        // Get all the invoiceList where language in DEFAULT_LANGUAGE or UPDATED_LANGUAGE
        defaultInvoiceShouldBeFound("language.in=" + DEFAULT_LANGUAGE + "," + UPDATED_LANGUAGE);

        // Get all the invoiceList where language equals to UPDATED_LANGUAGE
        defaultInvoiceShouldNotBeFound("language.in=" + UPDATED_LANGUAGE);
    }

    @Test
    @Transactional
    void getAllInvoicesByLanguageIsNullOrNotNull() throws Exception {
        // Initialize the database
        invoiceRepository.saveAndFlush(invoice);

        // Get all the invoiceList where language is not null
        defaultInvoiceShouldBeFound("language.specified=true");

        // Get all the invoiceList where language is null
        defaultInvoiceShouldNotBeFound("language.specified=false");
    }

    @Test
    @Transactional
    void getAllInvoicesByPageAmountIsEqualToSomething() throws Exception {
        // Initialize the database
        invoiceRepository.saveAndFlush(invoice);

        // Get all the invoiceList where pageAmount equals to DEFAULT_PAGE_AMOUNT
        defaultInvoiceShouldBeFound("pageAmount.equals=" + DEFAULT_PAGE_AMOUNT);

        // Get all the invoiceList where pageAmount equals to UPDATED_PAGE_AMOUNT
        defaultInvoiceShouldNotBeFound("pageAmount.equals=" + UPDATED_PAGE_AMOUNT);
    }

    @Test
    @Transactional
    void getAllInvoicesByPageAmountIsNotEqualToSomething() throws Exception {
        // Initialize the database
        invoiceRepository.saveAndFlush(invoice);

        // Get all the invoiceList where pageAmount not equals to DEFAULT_PAGE_AMOUNT
        defaultInvoiceShouldNotBeFound("pageAmount.notEquals=" + DEFAULT_PAGE_AMOUNT);

        // Get all the invoiceList where pageAmount not equals to UPDATED_PAGE_AMOUNT
        defaultInvoiceShouldBeFound("pageAmount.notEquals=" + UPDATED_PAGE_AMOUNT);
    }

    @Test
    @Transactional
    void getAllInvoicesByPageAmountIsInShouldWork() throws Exception {
        // Initialize the database
        invoiceRepository.saveAndFlush(invoice);

        // Get all the invoiceList where pageAmount in DEFAULT_PAGE_AMOUNT or UPDATED_PAGE_AMOUNT
        defaultInvoiceShouldBeFound("pageAmount.in=" + DEFAULT_PAGE_AMOUNT + "," + UPDATED_PAGE_AMOUNT);

        // Get all the invoiceList where pageAmount equals to UPDATED_PAGE_AMOUNT
        defaultInvoiceShouldNotBeFound("pageAmount.in=" + UPDATED_PAGE_AMOUNT);
    }

    @Test
    @Transactional
    void getAllInvoicesByPageAmountIsNullOrNotNull() throws Exception {
        // Initialize the database
        invoiceRepository.saveAndFlush(invoice);

        // Get all the invoiceList where pageAmount is not null
        defaultInvoiceShouldBeFound("pageAmount.specified=true");

        // Get all the invoiceList where pageAmount is null
        defaultInvoiceShouldNotBeFound("pageAmount.specified=false");
    }

    @Test
    @Transactional
    void getAllInvoicesByPageAmountIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        invoiceRepository.saveAndFlush(invoice);

        // Get all the invoiceList where pageAmount is greater than or equal to DEFAULT_PAGE_AMOUNT
        defaultInvoiceShouldBeFound("pageAmount.greaterThanOrEqual=" + DEFAULT_PAGE_AMOUNT);

        // Get all the invoiceList where pageAmount is greater than or equal to UPDATED_PAGE_AMOUNT
        defaultInvoiceShouldNotBeFound("pageAmount.greaterThanOrEqual=" + UPDATED_PAGE_AMOUNT);
    }

    @Test
    @Transactional
    void getAllInvoicesByPageAmountIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        invoiceRepository.saveAndFlush(invoice);

        // Get all the invoiceList where pageAmount is less than or equal to DEFAULT_PAGE_AMOUNT
        defaultInvoiceShouldBeFound("pageAmount.lessThanOrEqual=" + DEFAULT_PAGE_AMOUNT);

        // Get all the invoiceList where pageAmount is less than or equal to SMALLER_PAGE_AMOUNT
        defaultInvoiceShouldNotBeFound("pageAmount.lessThanOrEqual=" + SMALLER_PAGE_AMOUNT);
    }

    @Test
    @Transactional
    void getAllInvoicesByPageAmountIsLessThanSomething() throws Exception {
        // Initialize the database
        invoiceRepository.saveAndFlush(invoice);

        // Get all the invoiceList where pageAmount is less than DEFAULT_PAGE_AMOUNT
        defaultInvoiceShouldNotBeFound("pageAmount.lessThan=" + DEFAULT_PAGE_AMOUNT);

        // Get all the invoiceList where pageAmount is less than UPDATED_PAGE_AMOUNT
        defaultInvoiceShouldBeFound("pageAmount.lessThan=" + UPDATED_PAGE_AMOUNT);
    }

    @Test
    @Transactional
    void getAllInvoicesByPageAmountIsGreaterThanSomething() throws Exception {
        // Initialize the database
        invoiceRepository.saveAndFlush(invoice);

        // Get all the invoiceList where pageAmount is greater than DEFAULT_PAGE_AMOUNT
        defaultInvoiceShouldNotBeFound("pageAmount.greaterThan=" + DEFAULT_PAGE_AMOUNT);

        // Get all the invoiceList where pageAmount is greater than SMALLER_PAGE_AMOUNT
        defaultInvoiceShouldBeFound("pageAmount.greaterThan=" + SMALLER_PAGE_AMOUNT);
    }

    @Test
    @Transactional
    void getAllInvoicesByNotesIsEqualToSomething() throws Exception {
        // Initialize the database
        invoiceRepository.saveAndFlush(invoice);

        // Get all the invoiceList where notes equals to DEFAULT_NOTES
        defaultInvoiceShouldBeFound("notes.equals=" + DEFAULT_NOTES);

        // Get all the invoiceList where notes equals to UPDATED_NOTES
        defaultInvoiceShouldNotBeFound("notes.equals=" + UPDATED_NOTES);
    }

    @Test
    @Transactional
    void getAllInvoicesByNotesIsNotEqualToSomething() throws Exception {
        // Initialize the database
        invoiceRepository.saveAndFlush(invoice);

        // Get all the invoiceList where notes not equals to DEFAULT_NOTES
        defaultInvoiceShouldNotBeFound("notes.notEquals=" + DEFAULT_NOTES);

        // Get all the invoiceList where notes not equals to UPDATED_NOTES
        defaultInvoiceShouldBeFound("notes.notEquals=" + UPDATED_NOTES);
    }

    @Test
    @Transactional
    void getAllInvoicesByNotesIsInShouldWork() throws Exception {
        // Initialize the database
        invoiceRepository.saveAndFlush(invoice);

        // Get all the invoiceList where notes in DEFAULT_NOTES or UPDATED_NOTES
        defaultInvoiceShouldBeFound("notes.in=" + DEFAULT_NOTES + "," + UPDATED_NOTES);

        // Get all the invoiceList where notes equals to UPDATED_NOTES
        defaultInvoiceShouldNotBeFound("notes.in=" + UPDATED_NOTES);
    }

    @Test
    @Transactional
    void getAllInvoicesByNotesIsNullOrNotNull() throws Exception {
        // Initialize the database
        invoiceRepository.saveAndFlush(invoice);

        // Get all the invoiceList where notes is not null
        defaultInvoiceShouldBeFound("notes.specified=true");

        // Get all the invoiceList where notes is null
        defaultInvoiceShouldNotBeFound("notes.specified=false");
    }

    @Test
    @Transactional
    void getAllInvoicesByNotesContainsSomething() throws Exception {
        // Initialize the database
        invoiceRepository.saveAndFlush(invoice);

        // Get all the invoiceList where notes contains DEFAULT_NOTES
        defaultInvoiceShouldBeFound("notes.contains=" + DEFAULT_NOTES);

        // Get all the invoiceList where notes contains UPDATED_NOTES
        defaultInvoiceShouldNotBeFound("notes.contains=" + UPDATED_NOTES);
    }

    @Test
    @Transactional
    void getAllInvoicesByNotesNotContainsSomething() throws Exception {
        // Initialize the database
        invoiceRepository.saveAndFlush(invoice);

        // Get all the invoiceList where notes does not contain DEFAULT_NOTES
        defaultInvoiceShouldNotBeFound("notes.doesNotContain=" + DEFAULT_NOTES);

        // Get all the invoiceList where notes does not contain UPDATED_NOTES
        defaultInvoiceShouldBeFound("notes.doesNotContain=" + UPDATED_NOTES);
    }

    @Test
    @Transactional
    void getAllInvoicesByStatusIsEqualToSomething() throws Exception {
        // Initialize the database
        invoiceRepository.saveAndFlush(invoice);

        // Get all the invoiceList where status equals to DEFAULT_STATUS
        defaultInvoiceShouldBeFound("status.equals=" + DEFAULT_STATUS);

        // Get all the invoiceList where status equals to UPDATED_STATUS
        defaultInvoiceShouldNotBeFound("status.equals=" + UPDATED_STATUS);
    }

    @Test
    @Transactional
    void getAllInvoicesByStatusIsNotEqualToSomething() throws Exception {
        // Initialize the database
        invoiceRepository.saveAndFlush(invoice);

        // Get all the invoiceList where status not equals to DEFAULT_STATUS
        defaultInvoiceShouldNotBeFound("status.notEquals=" + DEFAULT_STATUS);

        // Get all the invoiceList where status not equals to UPDATED_STATUS
        defaultInvoiceShouldBeFound("status.notEquals=" + UPDATED_STATUS);
    }

    @Test
    @Transactional
    void getAllInvoicesByStatusIsInShouldWork() throws Exception {
        // Initialize the database
        invoiceRepository.saveAndFlush(invoice);

        // Get all the invoiceList where status in DEFAULT_STATUS or UPDATED_STATUS
        defaultInvoiceShouldBeFound("status.in=" + DEFAULT_STATUS + "," + UPDATED_STATUS);

        // Get all the invoiceList where status equals to UPDATED_STATUS
        defaultInvoiceShouldNotBeFound("status.in=" + UPDATED_STATUS);
    }

    @Test
    @Transactional
    void getAllInvoicesByStatusIsNullOrNotNull() throws Exception {
        // Initialize the database
        invoiceRepository.saveAndFlush(invoice);

        // Get all the invoiceList where status is not null
        defaultInvoiceShouldBeFound("status.specified=true");

        // Get all the invoiceList where status is null
        defaultInvoiceShouldNotBeFound("status.specified=false");
    }

    @Test
    @Transactional
    void getAllInvoicesByCreatedIsEqualToSomething() throws Exception {
        // Initialize the database
        invoiceRepository.saveAndFlush(invoice);

        // Get all the invoiceList where created equals to DEFAULT_CREATED
        defaultInvoiceShouldBeFound("created.equals=" + DEFAULT_CREATED);

        // Get all the invoiceList where created equals to UPDATED_CREATED
        defaultInvoiceShouldNotBeFound("created.equals=" + UPDATED_CREATED);
    }

    @Test
    @Transactional
    void getAllInvoicesByCreatedIsNotEqualToSomething() throws Exception {
        // Initialize the database
        invoiceRepository.saveAndFlush(invoice);

        // Get all the invoiceList where created not equals to DEFAULT_CREATED
        defaultInvoiceShouldNotBeFound("created.notEquals=" + DEFAULT_CREATED);

        // Get all the invoiceList where created not equals to UPDATED_CREATED
        defaultInvoiceShouldBeFound("created.notEquals=" + UPDATED_CREATED);
    }

    @Test
    @Transactional
    void getAllInvoicesByCreatedIsInShouldWork() throws Exception {
        // Initialize the database
        invoiceRepository.saveAndFlush(invoice);

        // Get all the invoiceList where created in DEFAULT_CREATED or UPDATED_CREATED
        defaultInvoiceShouldBeFound("created.in=" + DEFAULT_CREATED + "," + UPDATED_CREATED);

        // Get all the invoiceList where created equals to UPDATED_CREATED
        defaultInvoiceShouldNotBeFound("created.in=" + UPDATED_CREATED);
    }

    @Test
    @Transactional
    void getAllInvoicesByCreatedIsNullOrNotNull() throws Exception {
        // Initialize the database
        invoiceRepository.saveAndFlush(invoice);

        // Get all the invoiceList where created is not null
        defaultInvoiceShouldBeFound("created.specified=true");

        // Get all the invoiceList where created is null
        defaultInvoiceShouldNotBeFound("created.specified=false");
    }

    @Test
    @Transactional
    void getAllInvoicesByCreatedIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        invoiceRepository.saveAndFlush(invoice);

        // Get all the invoiceList where created is greater than or equal to DEFAULT_CREATED
        defaultInvoiceShouldBeFound("created.greaterThanOrEqual=" + DEFAULT_CREATED);

        // Get all the invoiceList where created is greater than or equal to UPDATED_CREATED
        defaultInvoiceShouldNotBeFound("created.greaterThanOrEqual=" + UPDATED_CREATED);
    }

    @Test
    @Transactional
    void getAllInvoicesByCreatedIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        invoiceRepository.saveAndFlush(invoice);

        // Get all the invoiceList where created is less than or equal to DEFAULT_CREATED
        defaultInvoiceShouldBeFound("created.lessThanOrEqual=" + DEFAULT_CREATED);

        // Get all the invoiceList where created is less than or equal to SMALLER_CREATED
        defaultInvoiceShouldNotBeFound("created.lessThanOrEqual=" + SMALLER_CREATED);
    }

    @Test
    @Transactional
    void getAllInvoicesByCreatedIsLessThanSomething() throws Exception {
        // Initialize the database
        invoiceRepository.saveAndFlush(invoice);

        // Get all the invoiceList where created is less than DEFAULT_CREATED
        defaultInvoiceShouldNotBeFound("created.lessThan=" + DEFAULT_CREATED);

        // Get all the invoiceList where created is less than UPDATED_CREATED
        defaultInvoiceShouldBeFound("created.lessThan=" + UPDATED_CREATED);
    }

    @Test
    @Transactional
    void getAllInvoicesByCreatedIsGreaterThanSomething() throws Exception {
        // Initialize the database
        invoiceRepository.saveAndFlush(invoice);

        // Get all the invoiceList where created is greater than DEFAULT_CREATED
        defaultInvoiceShouldNotBeFound("created.greaterThan=" + DEFAULT_CREATED);

        // Get all the invoiceList where created is greater than SMALLER_CREATED
        defaultInvoiceShouldBeFound("created.greaterThan=" + SMALLER_CREATED);
    }

    @Test
    @Transactional
    void getAllInvoicesByFreeTextsIsEqualToSomething() throws Exception {
        // Initialize the database
        invoiceRepository.saveAndFlush(invoice);
        DocumentFreeText freeTexts = DocumentFreeTextResourceIT.createEntity(em);
        em.persist(freeTexts);
        em.flush();
        invoice.addFreeTexts(freeTexts);
        invoiceRepository.saveAndFlush(invoice);
        Long freeTextsId = freeTexts.getId();

        // Get all the invoiceList where freeTexts equals to freeTextsId
        defaultInvoiceShouldBeFound("freeTextsId.equals=" + freeTextsId);

        // Get all the invoiceList where freeTexts equals to (freeTextsId + 1)
        defaultInvoiceShouldNotBeFound("freeTextsId.equals=" + (freeTextsId + 1));
    }

    @Test
    @Transactional
    void getAllInvoicesByTextsIsEqualToSomething() throws Exception {
        // Initialize the database
        invoiceRepository.saveAndFlush(invoice);
        DescriptiveDocumentText texts = DescriptiveDocumentTextResourceIT.createEntity(em);
        em.persist(texts);
        em.flush();
        invoice.addTexts(texts);
        invoiceRepository.saveAndFlush(invoice);
        Long textsId = texts.getId();

        // Get all the invoiceList where texts equals to textsId
        defaultInvoiceShouldBeFound("textsId.equals=" + textsId);

        // Get all the invoiceList where texts equals to (textsId + 1)
        defaultInvoiceShouldNotBeFound("textsId.equals=" + (textsId + 1));
    }

    @Test
    @Transactional
    void getAllInvoicesByPositionsIsEqualToSomething() throws Exception {
        // Initialize the database
        invoiceRepository.saveAndFlush(invoice);
        DocumentPosition positions = DocumentPositionResourceIT.createEntity(em);
        em.persist(positions);
        em.flush();
        invoice.addPositions(positions);
        invoiceRepository.saveAndFlush(invoice);
        Long positionsId = positions.getId();

        // Get all the invoiceList where positions equals to positionsId
        defaultInvoiceShouldBeFound("positionsId.equals=" + positionsId);

        // Get all the invoiceList where positions equals to (positionsId + 1)
        defaultInvoiceShouldNotBeFound("positionsId.equals=" + (positionsId + 1));
    }

    @Test
    @Transactional
    void getAllInvoicesByContactIsEqualToSomething() throws Exception {
        // Initialize the database
        invoiceRepository.saveAndFlush(invoice);
        Contact contact = ContactResourceIT.createEntity(em);
        em.persist(contact);
        em.flush();
        invoice.setContact(contact);
        invoiceRepository.saveAndFlush(invoice);
        Long contactId = contact.getId();

        // Get all the invoiceList where contact equals to contactId
        defaultInvoiceShouldBeFound("contactId.equals=" + contactId);

        // Get all the invoiceList where contact equals to (contactId + 1)
        defaultInvoiceShouldNotBeFound("contactId.equals=" + (contactId + 1));
    }

    @Test
    @Transactional
    void getAllInvoicesByContactAddressIsEqualToSomething() throws Exception {
        // Initialize the database
        invoiceRepository.saveAndFlush(invoice);
        ContactAddress contactAddress = ContactAddressResourceIT.createEntity(em);
        em.persist(contactAddress);
        em.flush();
        invoice.setContactAddress(contactAddress);
        invoiceRepository.saveAndFlush(invoice);
        Long contactAddressId = contactAddress.getId();

        // Get all the invoiceList where contactAddress equals to contactAddressId
        defaultInvoiceShouldBeFound("contactAddressId.equals=" + contactAddressId);

        // Get all the invoiceList where contactAddress equals to (contactAddressId + 1)
        defaultInvoiceShouldNotBeFound("contactAddressId.equals=" + (contactAddressId + 1));
    }

    @Test
    @Transactional
    void getAllInvoicesByContactPersonIsEqualToSomething() throws Exception {
        // Initialize the database
        invoiceRepository.saveAndFlush(invoice);
        ContactPerson contactPerson = ContactPersonResourceIT.createEntity(em);
        em.persist(contactPerson);
        em.flush();
        invoice.setContactPerson(contactPerson);
        invoiceRepository.saveAndFlush(invoice);
        Long contactPersonId = contactPerson.getId();

        // Get all the invoiceList where contactPerson equals to contactPersonId
        defaultInvoiceShouldBeFound("contactPersonId.equals=" + contactPersonId);

        // Get all the invoiceList where contactPerson equals to (contactPersonId + 1)
        defaultInvoiceShouldNotBeFound("contactPersonId.equals=" + (contactPersonId + 1));
    }

    @Test
    @Transactional
    void getAllInvoicesByContactPrePageAddressIsEqualToSomething() throws Exception {
        // Initialize the database
        invoiceRepository.saveAndFlush(invoice);
        ContactAddress contactPrePageAddress = ContactAddressResourceIT.createEntity(em);
        em.persist(contactPrePageAddress);
        em.flush();
        invoice.setContactPrePageAddress(contactPrePageAddress);
        invoiceRepository.saveAndFlush(invoice);
        Long contactPrePageAddressId = contactPrePageAddress.getId();

        // Get all the invoiceList where contactPrePageAddress equals to contactPrePageAddressId
        defaultInvoiceShouldBeFound("contactPrePageAddressId.equals=" + contactPrePageAddressId);

        // Get all the invoiceList where contactPrePageAddress equals to (contactPrePageAddressId + 1)
        defaultInvoiceShouldNotBeFound("contactPrePageAddressId.equals=" + (contactPrePageAddressId + 1));
    }

    @Test
    @Transactional
    void getAllInvoicesByLayoutIsEqualToSomething() throws Exception {
        // Initialize the database
        invoiceRepository.saveAndFlush(invoice);
        Layout layout = LayoutResourceIT.createEntity(em);
        em.persist(layout);
        em.flush();
        invoice.setLayout(layout);
        invoiceRepository.saveAndFlush(invoice);
        Long layoutId = layout.getId();

        // Get all the invoiceList where layout equals to layoutId
        defaultInvoiceShouldBeFound("layoutId.equals=" + layoutId);

        // Get all the invoiceList where layout equals to (layoutId + 1)
        defaultInvoiceShouldNotBeFound("layoutId.equals=" + (layoutId + 1));
    }

    @Test
    @Transactional
    void getAllInvoicesByLayoutIsEqualToSomething() throws Exception {
        // Initialize the database
        invoiceRepository.saveAndFlush(invoice);
        Signature layout = SignatureResourceIT.createEntity(em);
        em.persist(layout);
        em.flush();
        invoice.setLayout(layout);
        invoiceRepository.saveAndFlush(invoice);
        Long layoutId = layout.getId();

        // Get all the invoiceList where layout equals to layoutId
        defaultInvoiceShouldBeFound("layoutId.equals=" + layoutId);

        // Get all the invoiceList where layout equals to (layoutId + 1)
        defaultInvoiceShouldNotBeFound("layoutId.equals=" + (layoutId + 1));
    }

    @Test
    @Transactional
    void getAllInvoicesByBankAccountIsEqualToSomething() throws Exception {
        // Initialize the database
        invoiceRepository.saveAndFlush(invoice);
        BankAccount bankAccount = BankAccountResourceIT.createEntity(em);
        em.persist(bankAccount);
        em.flush();
        invoice.setBankAccount(bankAccount);
        invoiceRepository.saveAndFlush(invoice);
        Long bankAccountId = bankAccount.getId();

        // Get all the invoiceList where bankAccount equals to bankAccountId
        defaultInvoiceShouldBeFound("bankAccountId.equals=" + bankAccountId);

        // Get all the invoiceList where bankAccount equals to (bankAccountId + 1)
        defaultInvoiceShouldNotBeFound("bankAccountId.equals=" + (bankAccountId + 1));
    }

    @Test
    @Transactional
    void getAllInvoicesByIsrIsEqualToSomething() throws Exception {
        // Initialize the database
        invoiceRepository.saveAndFlush(invoice);
        Isr isr = IsrResourceIT.createEntity(em);
        em.persist(isr);
        em.flush();
        invoice.setIsr(isr);
        invoiceRepository.saveAndFlush(invoice);
        Long isrId = isr.getId();

        // Get all the invoiceList where isr equals to isrId
        defaultInvoiceShouldBeFound("isrId.equals=" + isrId);

        // Get all the invoiceList where isr equals to (isrId + 1)
        defaultInvoiceShouldNotBeFound("isrId.equals=" + (isrId + 1));
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultInvoiceShouldBeFound(String filter) throws Exception {
        restInvoiceMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
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

        // Check, that the count call also returns 1
        restInvoiceMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultInvoiceShouldNotBeFound(String filter) throws Exception {
        restInvoiceMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restInvoiceMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
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
