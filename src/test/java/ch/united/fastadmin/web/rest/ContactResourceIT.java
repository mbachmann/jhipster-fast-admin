package ch.united.fastadmin.web.rest;

import static ch.united.fastadmin.web.rest.TestUtil.sameInstant;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import ch.united.fastadmin.IntegrationTest;
import ch.united.fastadmin.domain.Contact;
import ch.united.fastadmin.domain.ContactGroup;
import ch.united.fastadmin.domain.ContactRelation;
import ch.united.fastadmin.domain.CustomFieldValue;
import ch.united.fastadmin.domain.enumeration.CommunicationChannel;
import ch.united.fastadmin.domain.enumeration.CommunicationNewsletter;
import ch.united.fastadmin.domain.enumeration.ContactType;
import ch.united.fastadmin.domain.enumeration.Currency;
import ch.united.fastadmin.domain.enumeration.DiscountType;
import ch.united.fastadmin.domain.enumeration.GenderType;
import ch.united.fastadmin.repository.ContactRepository;
import ch.united.fastadmin.service.ContactService;
import ch.united.fastadmin.service.criteria.ContactCriteria;
import ch.united.fastadmin.service.dto.ContactDTO;
import ch.united.fastadmin.service.mapper.ContactMapper;
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.atomic.AtomicLong;
import javax.persistence.EntityManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Base64Utils;

/**
 * Integration tests for the {@link ContactResource} REST controller.
 */
@IntegrationTest
@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc
@WithMockUser
class ContactResourceIT {

    private static final Integer DEFAULT_REMOTE_ID = 1;
    private static final Integer UPDATED_REMOTE_ID = 2;
    private static final Integer SMALLER_REMOTE_ID = 1 - 1;

    private static final String DEFAULT_NUMBER = "AAAAAAAAAA";
    private static final String UPDATED_NUMBER = "BBBBBBBBBB";

    private static final ContactType DEFAULT_TYPE = ContactType.COMPANY;
    private static final ContactType UPDATED_TYPE = ContactType.PRIVATE;

    private static final GenderType DEFAULT_GENDER = GenderType.MALE;
    private static final GenderType UPDATED_GENDER = GenderType.FEMALE;

    private static final Boolean DEFAULT_GENDER_SALUTATION_ACTIVE = false;
    private static final Boolean UPDATED_GENDER_SALUTATION_ACTIVE = true;

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_NAME_ADDITION = "AAAAAAAAAA";
    private static final String UPDATED_NAME_ADDITION = "BBBBBBBBBB";

    private static final String DEFAULT_SALUTATION = "AAAAAAAAAA";
    private static final String UPDATED_SALUTATION = "BBBBBBBBBB";

    private static final String DEFAULT_PHONE = "AAAAAAAAAA";
    private static final String UPDATED_PHONE = "BBBBBBBBBB";

    private static final String DEFAULT_FAX = "AAAAAAAAAA";
    private static final String UPDATED_FAX = "BBBBBBBBBB";

    private static final String DEFAULT_EMAIL = "AAAAAAAAAA";
    private static final String UPDATED_EMAIL = "BBBBBBBBBB";

    private static final String DEFAULT_WEBSITE = "AAAAAAAAAA";
    private static final String UPDATED_WEBSITE = "BBBBBBBBBB";

    private static final String DEFAULT_NOTES = "AAAAAAAAAA";
    private static final String UPDATED_NOTES = "BBBBBBBBBB";

    private static final String DEFAULT_COMMUNICATION_LANGUAGE = "AAAAAAAAAA";
    private static final String UPDATED_COMMUNICATION_LANGUAGE = "BBBBBBBBBB";

    private static final CommunicationChannel DEFAULT_COMMUNICATION_CHANNEL = CommunicationChannel.NO_PREFERENCE;
    private static final CommunicationChannel UPDATED_COMMUNICATION_CHANNEL = CommunicationChannel.BY_EMAIL;

    private static final CommunicationNewsletter DEFAULT_COMMUNICATION_NEWSLETTER = CommunicationNewsletter.SEND_ADDRESS_AND_CONTACTS;
    private static final CommunicationNewsletter UPDATED_COMMUNICATION_NEWSLETTER = CommunicationNewsletter.SEND_TO_MAIN_CONTACT_ONLY;

    private static final Currency DEFAULT_CURRENCY = Currency.AED;
    private static final Currency UPDATED_CURRENCY = Currency.AFN;

    private static final String DEFAULT_EBILL_ACCOUNT_ID = "AAAAAAAAAA";
    private static final String UPDATED_EBILL_ACCOUNT_ID = "BBBBBBBBBB";

    private static final String DEFAULT_VAT_IDENTIFICATION = "AAAAAAAAAA";
    private static final String UPDATED_VAT_IDENTIFICATION = "BBBBBBBBBB";

    private static final Double DEFAULT_VAT_RATE = 1D;
    private static final Double UPDATED_VAT_RATE = 2D;
    private static final Double SMALLER_VAT_RATE = 1D - 1D;

    private static final Double DEFAULT_DISCOUNT_RATE = 1D;
    private static final Double UPDATED_DISCOUNT_RATE = 2D;
    private static final Double SMALLER_DISCOUNT_RATE = 1D - 1D;

    private static final DiscountType DEFAULT_DISCOUNT_TYPE = DiscountType.IN_PERCENT;
    private static final DiscountType UPDATED_DISCOUNT_TYPE = DiscountType.AMOUNT;

    private static final Integer DEFAULT_PAYMENT_GRACE = 1;
    private static final Integer UPDATED_PAYMENT_GRACE = 2;
    private static final Integer SMALLER_PAYMENT_GRACE = 1 - 1;

    private static final Double DEFAULT_HOURLY_RATE = 1D;
    private static final Double UPDATED_HOURLY_RATE = 2D;
    private static final Double SMALLER_HOURLY_RATE = 1D - 1D;

    private static final ZonedDateTime DEFAULT_CREATED = ZonedDateTime.ofInstant(Instant.ofEpochMilli(0L), ZoneOffset.UTC);
    private static final ZonedDateTime UPDATED_CREATED = ZonedDateTime.now(ZoneId.systemDefault()).withNano(0);
    private static final ZonedDateTime SMALLER_CREATED = ZonedDateTime.ofInstant(Instant.ofEpochMilli(-1L), ZoneOffset.UTC);

    private static final Integer DEFAULT_MAIN_ADDRESS_ID = 1;
    private static final Integer UPDATED_MAIN_ADDRESS_ID = 2;
    private static final Integer SMALLER_MAIN_ADDRESS_ID = 1 - 1;

    private static final LocalDate DEFAULT_BIRTH_DATE = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_BIRTH_DATE = LocalDate.now(ZoneId.systemDefault());
    private static final LocalDate SMALLER_BIRTH_DATE = LocalDate.ofEpochDay(-1L);

    private static final String DEFAULT_BIRTH_PLACE = "AAAAAAAAAA";
    private static final String UPDATED_BIRTH_PLACE = "BBBBBBBBBB";

    private static final String DEFAULT_PLACE_OF_ORIGIN = "AAAAAAAAAA";
    private static final String UPDATED_PLACE_OF_ORIGIN = "BBBBBBBBBB";

    private static final String DEFAULT_CITIZEN_COUNTRY_1 = "AAAAAAAAAA";
    private static final String UPDATED_CITIZEN_COUNTRY_1 = "BBBBBBBBBB";

    private static final String DEFAULT_CITIZEN_COUNTRY_2 = "AAAAAAAAAA";
    private static final String UPDATED_CITIZEN_COUNTRY_2 = "BBBBBBBBBB";

    private static final String DEFAULT_SOCIAL_SECURITY_NUMBER = "AAAAAAAAAA";
    private static final String UPDATED_SOCIAL_SECURITY_NUMBER = "BBBBBBBBBB";

    private static final String DEFAULT_HOBBIES = "AAAAAAAAAA";
    private static final String UPDATED_HOBBIES = "BBBBBBBBBB";

    private static final String DEFAULT_DAILY_WORK = "AAAAAAAAAA";
    private static final String UPDATED_DAILY_WORK = "BBBBBBBBBB";

    private static final String DEFAULT_CONTACT_ATTRIBUTE_01 = "AAAAAAAAAA";
    private static final String UPDATED_CONTACT_ATTRIBUTE_01 = "BBBBBBBBBB";

    private static final byte[] DEFAULT_AVATAR = TestUtil.createByteArray(1, "0");
    private static final byte[] UPDATED_AVATAR = TestUtil.createByteArray(1, "1");
    private static final String DEFAULT_AVATAR_CONTENT_TYPE = "image/jpg";
    private static final String UPDATED_AVATAR_CONTENT_TYPE = "image/png";

    private static final String DEFAULT_IMAGE_TYPE = "AAAAAAAAAA";
    private static final String UPDATED_IMAGE_TYPE = "BBBBBBBBBB";

    private static final Boolean DEFAULT_INACTIV = false;
    private static final Boolean UPDATED_INACTIV = true;

    private static final String ENTITY_API_URL = "/api/contacts";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ContactRepository contactRepository;

    @Mock
    private ContactRepository contactRepositoryMock;

    @Autowired
    private ContactMapper contactMapper;

    @Mock
    private ContactService contactServiceMock;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restContactMockMvc;

    private Contact contact;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Contact createEntity(EntityManager em) {
        Contact contact = new Contact()
            .remoteId(DEFAULT_REMOTE_ID)
            .number(DEFAULT_NUMBER)
            .type(DEFAULT_TYPE)
            .gender(DEFAULT_GENDER)
            .genderSalutationActive(DEFAULT_GENDER_SALUTATION_ACTIVE)
            .name(DEFAULT_NAME)
            .nameAddition(DEFAULT_NAME_ADDITION)
            .salutation(DEFAULT_SALUTATION)
            .phone(DEFAULT_PHONE)
            .fax(DEFAULT_FAX)
            .email(DEFAULT_EMAIL)
            .website(DEFAULT_WEBSITE)
            .notes(DEFAULT_NOTES)
            .communicationLanguage(DEFAULT_COMMUNICATION_LANGUAGE)
            .communicationChannel(DEFAULT_COMMUNICATION_CHANNEL)
            .communicationNewsletter(DEFAULT_COMMUNICATION_NEWSLETTER)
            .currency(DEFAULT_CURRENCY)
            .ebillAccountId(DEFAULT_EBILL_ACCOUNT_ID)
            .vatIdentification(DEFAULT_VAT_IDENTIFICATION)
            .vatRate(DEFAULT_VAT_RATE)
            .discountRate(DEFAULT_DISCOUNT_RATE)
            .discountType(DEFAULT_DISCOUNT_TYPE)
            .paymentGrace(DEFAULT_PAYMENT_GRACE)
            .hourlyRate(DEFAULT_HOURLY_RATE)
            .created(DEFAULT_CREATED)
            .mainAddressId(DEFAULT_MAIN_ADDRESS_ID)
            .birthDate(DEFAULT_BIRTH_DATE)
            .birthPlace(DEFAULT_BIRTH_PLACE)
            .placeOfOrigin(DEFAULT_PLACE_OF_ORIGIN)
            .citizenCountry1(DEFAULT_CITIZEN_COUNTRY_1)
            .citizenCountry2(DEFAULT_CITIZEN_COUNTRY_2)
            .socialSecurityNumber(DEFAULT_SOCIAL_SECURITY_NUMBER)
            .hobbies(DEFAULT_HOBBIES)
            .dailyWork(DEFAULT_DAILY_WORK)
            .contactAttribute01(DEFAULT_CONTACT_ATTRIBUTE_01)
            .avatar(DEFAULT_AVATAR)
            .avatarContentType(DEFAULT_AVATAR_CONTENT_TYPE)
            .imageType(DEFAULT_IMAGE_TYPE)
            .inactiv(DEFAULT_INACTIV);
        return contact;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Contact createUpdatedEntity(EntityManager em) {
        Contact contact = new Contact()
            .remoteId(UPDATED_REMOTE_ID)
            .number(UPDATED_NUMBER)
            .type(UPDATED_TYPE)
            .gender(UPDATED_GENDER)
            .genderSalutationActive(UPDATED_GENDER_SALUTATION_ACTIVE)
            .name(UPDATED_NAME)
            .nameAddition(UPDATED_NAME_ADDITION)
            .salutation(UPDATED_SALUTATION)
            .phone(UPDATED_PHONE)
            .fax(UPDATED_FAX)
            .email(UPDATED_EMAIL)
            .website(UPDATED_WEBSITE)
            .notes(UPDATED_NOTES)
            .communicationLanguage(UPDATED_COMMUNICATION_LANGUAGE)
            .communicationChannel(UPDATED_COMMUNICATION_CHANNEL)
            .communicationNewsletter(UPDATED_COMMUNICATION_NEWSLETTER)
            .currency(UPDATED_CURRENCY)
            .ebillAccountId(UPDATED_EBILL_ACCOUNT_ID)
            .vatIdentification(UPDATED_VAT_IDENTIFICATION)
            .vatRate(UPDATED_VAT_RATE)
            .discountRate(UPDATED_DISCOUNT_RATE)
            .discountType(UPDATED_DISCOUNT_TYPE)
            .paymentGrace(UPDATED_PAYMENT_GRACE)
            .hourlyRate(UPDATED_HOURLY_RATE)
            .created(UPDATED_CREATED)
            .mainAddressId(UPDATED_MAIN_ADDRESS_ID)
            .birthDate(UPDATED_BIRTH_DATE)
            .birthPlace(UPDATED_BIRTH_PLACE)
            .placeOfOrigin(UPDATED_PLACE_OF_ORIGIN)
            .citizenCountry1(UPDATED_CITIZEN_COUNTRY_1)
            .citizenCountry2(UPDATED_CITIZEN_COUNTRY_2)
            .socialSecurityNumber(UPDATED_SOCIAL_SECURITY_NUMBER)
            .hobbies(UPDATED_HOBBIES)
            .dailyWork(UPDATED_DAILY_WORK)
            .contactAttribute01(UPDATED_CONTACT_ATTRIBUTE_01)
            .avatar(UPDATED_AVATAR)
            .avatarContentType(UPDATED_AVATAR_CONTENT_TYPE)
            .imageType(UPDATED_IMAGE_TYPE)
            .inactiv(UPDATED_INACTIV);
        return contact;
    }

    @BeforeEach
    public void initTest() {
        contact = createEntity(em);
    }

    @Test
    @Transactional
    void createContact() throws Exception {
        int databaseSizeBeforeCreate = contactRepository.findAll().size();
        // Create the Contact
        ContactDTO contactDTO = contactMapper.toDto(contact);
        restContactMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(contactDTO)))
            .andExpect(status().isCreated());

        // Validate the Contact in the database
        List<Contact> contactList = contactRepository.findAll();
        assertThat(contactList).hasSize(databaseSizeBeforeCreate + 1);
        Contact testContact = contactList.get(contactList.size() - 1);
        assertThat(testContact.getRemoteId()).isEqualTo(DEFAULT_REMOTE_ID);
        assertThat(testContact.getNumber()).isEqualTo(DEFAULT_NUMBER);
        assertThat(testContact.getType()).isEqualTo(DEFAULT_TYPE);
        assertThat(testContact.getGender()).isEqualTo(DEFAULT_GENDER);
        assertThat(testContact.getGenderSalutationActive()).isEqualTo(DEFAULT_GENDER_SALUTATION_ACTIVE);
        assertThat(testContact.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testContact.getNameAddition()).isEqualTo(DEFAULT_NAME_ADDITION);
        assertThat(testContact.getSalutation()).isEqualTo(DEFAULT_SALUTATION);
        assertThat(testContact.getPhone()).isEqualTo(DEFAULT_PHONE);
        assertThat(testContact.getFax()).isEqualTo(DEFAULT_FAX);
        assertThat(testContact.getEmail()).isEqualTo(DEFAULT_EMAIL);
        assertThat(testContact.getWebsite()).isEqualTo(DEFAULT_WEBSITE);
        assertThat(testContact.getNotes()).isEqualTo(DEFAULT_NOTES);
        assertThat(testContact.getCommunicationLanguage()).isEqualTo(DEFAULT_COMMUNICATION_LANGUAGE);
        assertThat(testContact.getCommunicationChannel()).isEqualTo(DEFAULT_COMMUNICATION_CHANNEL);
        assertThat(testContact.getCommunicationNewsletter()).isEqualTo(DEFAULT_COMMUNICATION_NEWSLETTER);
        assertThat(testContact.getCurrency()).isEqualTo(DEFAULT_CURRENCY);
        assertThat(testContact.getEbillAccountId()).isEqualTo(DEFAULT_EBILL_ACCOUNT_ID);
        assertThat(testContact.getVatIdentification()).isEqualTo(DEFAULT_VAT_IDENTIFICATION);
        assertThat(testContact.getVatRate()).isEqualTo(DEFAULT_VAT_RATE);
        assertThat(testContact.getDiscountRate()).isEqualTo(DEFAULT_DISCOUNT_RATE);
        assertThat(testContact.getDiscountType()).isEqualTo(DEFAULT_DISCOUNT_TYPE);
        assertThat(testContact.getPaymentGrace()).isEqualTo(DEFAULT_PAYMENT_GRACE);
        assertThat(testContact.getHourlyRate()).isEqualTo(DEFAULT_HOURLY_RATE);
        assertThat(testContact.getCreated()).isEqualTo(DEFAULT_CREATED);
        assertThat(testContact.getMainAddressId()).isEqualTo(DEFAULT_MAIN_ADDRESS_ID);
        assertThat(testContact.getBirthDate()).isEqualTo(DEFAULT_BIRTH_DATE);
        assertThat(testContact.getBirthPlace()).isEqualTo(DEFAULT_BIRTH_PLACE);
        assertThat(testContact.getPlaceOfOrigin()).isEqualTo(DEFAULT_PLACE_OF_ORIGIN);
        assertThat(testContact.getCitizenCountry1()).isEqualTo(DEFAULT_CITIZEN_COUNTRY_1);
        assertThat(testContact.getCitizenCountry2()).isEqualTo(DEFAULT_CITIZEN_COUNTRY_2);
        assertThat(testContact.getSocialSecurityNumber()).isEqualTo(DEFAULT_SOCIAL_SECURITY_NUMBER);
        assertThat(testContact.getHobbies()).isEqualTo(DEFAULT_HOBBIES);
        assertThat(testContact.getDailyWork()).isEqualTo(DEFAULT_DAILY_WORK);
        assertThat(testContact.getContactAttribute01()).isEqualTo(DEFAULT_CONTACT_ATTRIBUTE_01);
        assertThat(testContact.getAvatar()).isEqualTo(DEFAULT_AVATAR);
        assertThat(testContact.getAvatarContentType()).isEqualTo(DEFAULT_AVATAR_CONTENT_TYPE);
        assertThat(testContact.getImageType()).isEqualTo(DEFAULT_IMAGE_TYPE);
        assertThat(testContact.getInactiv()).isEqualTo(DEFAULT_INACTIV);
    }

    @Test
    @Transactional
    void createContactWithExistingId() throws Exception {
        // Create the Contact with an existing ID
        contact.setId(1L);
        ContactDTO contactDTO = contactMapper.toDto(contact);

        int databaseSizeBeforeCreate = contactRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restContactMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(contactDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Contact in the database
        List<Contact> contactList = contactRepository.findAll();
        assertThat(contactList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkTypeIsRequired() throws Exception {
        int databaseSizeBeforeTest = contactRepository.findAll().size();
        // set the field null
        contact.setType(null);

        // Create the Contact, which fails.
        ContactDTO contactDTO = contactMapper.toDto(contact);

        restContactMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(contactDTO)))
            .andExpect(status().isBadRequest());

        List<Contact> contactList = contactRepository.findAll();
        assertThat(contactList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkGenderIsRequired() throws Exception {
        int databaseSizeBeforeTest = contactRepository.findAll().size();
        // set the field null
        contact.setGender(null);

        // Create the Contact, which fails.
        ContactDTO contactDTO = contactMapper.toDto(contact);

        restContactMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(contactDTO)))
            .andExpect(status().isBadRequest());

        List<Contact> contactList = contactRepository.findAll();
        assertThat(contactList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkGenderSalutationActiveIsRequired() throws Exception {
        int databaseSizeBeforeTest = contactRepository.findAll().size();
        // set the field null
        contact.setGenderSalutationActive(null);

        // Create the Contact, which fails.
        ContactDTO contactDTO = contactMapper.toDto(contact);

        restContactMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(contactDTO)))
            .andExpect(status().isBadRequest());

        List<Contact> contactList = contactRepository.findAll();
        assertThat(contactList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkNameIsRequired() throws Exception {
        int databaseSizeBeforeTest = contactRepository.findAll().size();
        // set the field null
        contact.setName(null);

        // Create the Contact, which fails.
        ContactDTO contactDTO = contactMapper.toDto(contact);

        restContactMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(contactDTO)))
            .andExpect(status().isBadRequest());

        List<Contact> contactList = contactRepository.findAll();
        assertThat(contactList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllContacts() throws Exception {
        // Initialize the database
        contactRepository.saveAndFlush(contact);

        // Get all the contactList
        restContactMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(contact.getId().intValue())))
            .andExpect(jsonPath("$.[*].remoteId").value(hasItem(DEFAULT_REMOTE_ID)))
            .andExpect(jsonPath("$.[*].number").value(hasItem(DEFAULT_NUMBER)))
            .andExpect(jsonPath("$.[*].type").value(hasItem(DEFAULT_TYPE.toString())))
            .andExpect(jsonPath("$.[*].gender").value(hasItem(DEFAULT_GENDER.toString())))
            .andExpect(jsonPath("$.[*].genderSalutationActive").value(hasItem(DEFAULT_GENDER_SALUTATION_ACTIVE.booleanValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)))
            .andExpect(jsonPath("$.[*].nameAddition").value(hasItem(DEFAULT_NAME_ADDITION)))
            .andExpect(jsonPath("$.[*].salutation").value(hasItem(DEFAULT_SALUTATION)))
            .andExpect(jsonPath("$.[*].phone").value(hasItem(DEFAULT_PHONE)))
            .andExpect(jsonPath("$.[*].fax").value(hasItem(DEFAULT_FAX)))
            .andExpect(jsonPath("$.[*].email").value(hasItem(DEFAULT_EMAIL)))
            .andExpect(jsonPath("$.[*].website").value(hasItem(DEFAULT_WEBSITE)))
            .andExpect(jsonPath("$.[*].notes").value(hasItem(DEFAULT_NOTES)))
            .andExpect(jsonPath("$.[*].communicationLanguage").value(hasItem(DEFAULT_COMMUNICATION_LANGUAGE)))
            .andExpect(jsonPath("$.[*].communicationChannel").value(hasItem(DEFAULT_COMMUNICATION_CHANNEL.toString())))
            .andExpect(jsonPath("$.[*].communicationNewsletter").value(hasItem(DEFAULT_COMMUNICATION_NEWSLETTER.toString())))
            .andExpect(jsonPath("$.[*].currency").value(hasItem(DEFAULT_CURRENCY.toString())))
            .andExpect(jsonPath("$.[*].ebillAccountId").value(hasItem(DEFAULT_EBILL_ACCOUNT_ID)))
            .andExpect(jsonPath("$.[*].vatIdentification").value(hasItem(DEFAULT_VAT_IDENTIFICATION)))
            .andExpect(jsonPath("$.[*].vatRate").value(hasItem(DEFAULT_VAT_RATE.doubleValue())))
            .andExpect(jsonPath("$.[*].discountRate").value(hasItem(DEFAULT_DISCOUNT_RATE.doubleValue())))
            .andExpect(jsonPath("$.[*].discountType").value(hasItem(DEFAULT_DISCOUNT_TYPE.toString())))
            .andExpect(jsonPath("$.[*].paymentGrace").value(hasItem(DEFAULT_PAYMENT_GRACE)))
            .andExpect(jsonPath("$.[*].hourlyRate").value(hasItem(DEFAULT_HOURLY_RATE.doubleValue())))
            .andExpect(jsonPath("$.[*].created").value(hasItem(sameInstant(DEFAULT_CREATED))))
            .andExpect(jsonPath("$.[*].mainAddressId").value(hasItem(DEFAULT_MAIN_ADDRESS_ID)))
            .andExpect(jsonPath("$.[*].birthDate").value(hasItem(DEFAULT_BIRTH_DATE.toString())))
            .andExpect(jsonPath("$.[*].birthPlace").value(hasItem(DEFAULT_BIRTH_PLACE)))
            .andExpect(jsonPath("$.[*].placeOfOrigin").value(hasItem(DEFAULT_PLACE_OF_ORIGIN)))
            .andExpect(jsonPath("$.[*].citizenCountry1").value(hasItem(DEFAULT_CITIZEN_COUNTRY_1)))
            .andExpect(jsonPath("$.[*].citizenCountry2").value(hasItem(DEFAULT_CITIZEN_COUNTRY_2)))
            .andExpect(jsonPath("$.[*].socialSecurityNumber").value(hasItem(DEFAULT_SOCIAL_SECURITY_NUMBER)))
            .andExpect(jsonPath("$.[*].hobbies").value(hasItem(DEFAULT_HOBBIES)))
            .andExpect(jsonPath("$.[*].dailyWork").value(hasItem(DEFAULT_DAILY_WORK)))
            .andExpect(jsonPath("$.[*].contactAttribute01").value(hasItem(DEFAULT_CONTACT_ATTRIBUTE_01)))
            .andExpect(jsonPath("$.[*].avatarContentType").value(hasItem(DEFAULT_AVATAR_CONTENT_TYPE)))
            .andExpect(jsonPath("$.[*].avatar").value(hasItem(Base64Utils.encodeToString(DEFAULT_AVATAR))))
            .andExpect(jsonPath("$.[*].imageType").value(hasItem(DEFAULT_IMAGE_TYPE)))
            .andExpect(jsonPath("$.[*].inactiv").value(hasItem(DEFAULT_INACTIV.booleanValue())));
    }

    @SuppressWarnings({ "unchecked" })
    void getAllContactsWithEagerRelationshipsIsEnabled() throws Exception {
        when(contactServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restContactMockMvc.perform(get(ENTITY_API_URL + "?eagerload=true")).andExpect(status().isOk());

        verify(contactServiceMock, times(1)).findAllWithEagerRelationships(any());
    }

    @SuppressWarnings({ "unchecked" })
    void getAllContactsWithEagerRelationshipsIsNotEnabled() throws Exception {
        when(contactServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restContactMockMvc.perform(get(ENTITY_API_URL + "?eagerload=true")).andExpect(status().isOk());

        verify(contactServiceMock, times(1)).findAllWithEagerRelationships(any());
    }

    @Test
    @Transactional
    void getContact() throws Exception {
        // Initialize the database
        contactRepository.saveAndFlush(contact);

        // Get the contact
        restContactMockMvc
            .perform(get(ENTITY_API_URL_ID, contact.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(contact.getId().intValue()))
            .andExpect(jsonPath("$.remoteId").value(DEFAULT_REMOTE_ID))
            .andExpect(jsonPath("$.number").value(DEFAULT_NUMBER))
            .andExpect(jsonPath("$.type").value(DEFAULT_TYPE.toString()))
            .andExpect(jsonPath("$.gender").value(DEFAULT_GENDER.toString()))
            .andExpect(jsonPath("$.genderSalutationActive").value(DEFAULT_GENDER_SALUTATION_ACTIVE.booleanValue()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME))
            .andExpect(jsonPath("$.nameAddition").value(DEFAULT_NAME_ADDITION))
            .andExpect(jsonPath("$.salutation").value(DEFAULT_SALUTATION))
            .andExpect(jsonPath("$.phone").value(DEFAULT_PHONE))
            .andExpect(jsonPath("$.fax").value(DEFAULT_FAX))
            .andExpect(jsonPath("$.email").value(DEFAULT_EMAIL))
            .andExpect(jsonPath("$.website").value(DEFAULT_WEBSITE))
            .andExpect(jsonPath("$.notes").value(DEFAULT_NOTES))
            .andExpect(jsonPath("$.communicationLanguage").value(DEFAULT_COMMUNICATION_LANGUAGE))
            .andExpect(jsonPath("$.communicationChannel").value(DEFAULT_COMMUNICATION_CHANNEL.toString()))
            .andExpect(jsonPath("$.communicationNewsletter").value(DEFAULT_COMMUNICATION_NEWSLETTER.toString()))
            .andExpect(jsonPath("$.currency").value(DEFAULT_CURRENCY.toString()))
            .andExpect(jsonPath("$.ebillAccountId").value(DEFAULT_EBILL_ACCOUNT_ID))
            .andExpect(jsonPath("$.vatIdentification").value(DEFAULT_VAT_IDENTIFICATION))
            .andExpect(jsonPath("$.vatRate").value(DEFAULT_VAT_RATE.doubleValue()))
            .andExpect(jsonPath("$.discountRate").value(DEFAULT_DISCOUNT_RATE.doubleValue()))
            .andExpect(jsonPath("$.discountType").value(DEFAULT_DISCOUNT_TYPE.toString()))
            .andExpect(jsonPath("$.paymentGrace").value(DEFAULT_PAYMENT_GRACE))
            .andExpect(jsonPath("$.hourlyRate").value(DEFAULT_HOURLY_RATE.doubleValue()))
            .andExpect(jsonPath("$.created").value(sameInstant(DEFAULT_CREATED)))
            .andExpect(jsonPath("$.mainAddressId").value(DEFAULT_MAIN_ADDRESS_ID))
            .andExpect(jsonPath("$.birthDate").value(DEFAULT_BIRTH_DATE.toString()))
            .andExpect(jsonPath("$.birthPlace").value(DEFAULT_BIRTH_PLACE))
            .andExpect(jsonPath("$.placeOfOrigin").value(DEFAULT_PLACE_OF_ORIGIN))
            .andExpect(jsonPath("$.citizenCountry1").value(DEFAULT_CITIZEN_COUNTRY_1))
            .andExpect(jsonPath("$.citizenCountry2").value(DEFAULT_CITIZEN_COUNTRY_2))
            .andExpect(jsonPath("$.socialSecurityNumber").value(DEFAULT_SOCIAL_SECURITY_NUMBER))
            .andExpect(jsonPath("$.hobbies").value(DEFAULT_HOBBIES))
            .andExpect(jsonPath("$.dailyWork").value(DEFAULT_DAILY_WORK))
            .andExpect(jsonPath("$.contactAttribute01").value(DEFAULT_CONTACT_ATTRIBUTE_01))
            .andExpect(jsonPath("$.avatarContentType").value(DEFAULT_AVATAR_CONTENT_TYPE))
            .andExpect(jsonPath("$.avatar").value(Base64Utils.encodeToString(DEFAULT_AVATAR)))
            .andExpect(jsonPath("$.imageType").value(DEFAULT_IMAGE_TYPE))
            .andExpect(jsonPath("$.inactiv").value(DEFAULT_INACTIV.booleanValue()));
    }

    @Test
    @Transactional
    void getContactsByIdFiltering() throws Exception {
        // Initialize the database
        contactRepository.saveAndFlush(contact);

        Long id = contact.getId();

        defaultContactShouldBeFound("id.equals=" + id);
        defaultContactShouldNotBeFound("id.notEquals=" + id);

        defaultContactShouldBeFound("id.greaterThanOrEqual=" + id);
        defaultContactShouldNotBeFound("id.greaterThan=" + id);

        defaultContactShouldBeFound("id.lessThanOrEqual=" + id);
        defaultContactShouldNotBeFound("id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllContactsByRemoteIdIsEqualToSomething() throws Exception {
        // Initialize the database
        contactRepository.saveAndFlush(contact);

        // Get all the contactList where remoteId equals to DEFAULT_REMOTE_ID
        defaultContactShouldBeFound("remoteId.equals=" + DEFAULT_REMOTE_ID);

        // Get all the contactList where remoteId equals to UPDATED_REMOTE_ID
        defaultContactShouldNotBeFound("remoteId.equals=" + UPDATED_REMOTE_ID);
    }

    @Test
    @Transactional
    void getAllContactsByRemoteIdIsNotEqualToSomething() throws Exception {
        // Initialize the database
        contactRepository.saveAndFlush(contact);

        // Get all the contactList where remoteId not equals to DEFAULT_REMOTE_ID
        defaultContactShouldNotBeFound("remoteId.notEquals=" + DEFAULT_REMOTE_ID);

        // Get all the contactList where remoteId not equals to UPDATED_REMOTE_ID
        defaultContactShouldBeFound("remoteId.notEquals=" + UPDATED_REMOTE_ID);
    }

    @Test
    @Transactional
    void getAllContactsByRemoteIdIsInShouldWork() throws Exception {
        // Initialize the database
        contactRepository.saveAndFlush(contact);

        // Get all the contactList where remoteId in DEFAULT_REMOTE_ID or UPDATED_REMOTE_ID
        defaultContactShouldBeFound("remoteId.in=" + DEFAULT_REMOTE_ID + "," + UPDATED_REMOTE_ID);

        // Get all the contactList where remoteId equals to UPDATED_REMOTE_ID
        defaultContactShouldNotBeFound("remoteId.in=" + UPDATED_REMOTE_ID);
    }

    @Test
    @Transactional
    void getAllContactsByRemoteIdIsNullOrNotNull() throws Exception {
        // Initialize the database
        contactRepository.saveAndFlush(contact);

        // Get all the contactList where remoteId is not null
        defaultContactShouldBeFound("remoteId.specified=true");

        // Get all the contactList where remoteId is null
        defaultContactShouldNotBeFound("remoteId.specified=false");
    }

    @Test
    @Transactional
    void getAllContactsByRemoteIdIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        contactRepository.saveAndFlush(contact);

        // Get all the contactList where remoteId is greater than or equal to DEFAULT_REMOTE_ID
        defaultContactShouldBeFound("remoteId.greaterThanOrEqual=" + DEFAULT_REMOTE_ID);

        // Get all the contactList where remoteId is greater than or equal to UPDATED_REMOTE_ID
        defaultContactShouldNotBeFound("remoteId.greaterThanOrEqual=" + UPDATED_REMOTE_ID);
    }

    @Test
    @Transactional
    void getAllContactsByRemoteIdIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        contactRepository.saveAndFlush(contact);

        // Get all the contactList where remoteId is less than or equal to DEFAULT_REMOTE_ID
        defaultContactShouldBeFound("remoteId.lessThanOrEqual=" + DEFAULT_REMOTE_ID);

        // Get all the contactList where remoteId is less than or equal to SMALLER_REMOTE_ID
        defaultContactShouldNotBeFound("remoteId.lessThanOrEqual=" + SMALLER_REMOTE_ID);
    }

    @Test
    @Transactional
    void getAllContactsByRemoteIdIsLessThanSomething() throws Exception {
        // Initialize the database
        contactRepository.saveAndFlush(contact);

        // Get all the contactList where remoteId is less than DEFAULT_REMOTE_ID
        defaultContactShouldNotBeFound("remoteId.lessThan=" + DEFAULT_REMOTE_ID);

        // Get all the contactList where remoteId is less than UPDATED_REMOTE_ID
        defaultContactShouldBeFound("remoteId.lessThan=" + UPDATED_REMOTE_ID);
    }

    @Test
    @Transactional
    void getAllContactsByRemoteIdIsGreaterThanSomething() throws Exception {
        // Initialize the database
        contactRepository.saveAndFlush(contact);

        // Get all the contactList where remoteId is greater than DEFAULT_REMOTE_ID
        defaultContactShouldNotBeFound("remoteId.greaterThan=" + DEFAULT_REMOTE_ID);

        // Get all the contactList where remoteId is greater than SMALLER_REMOTE_ID
        defaultContactShouldBeFound("remoteId.greaterThan=" + SMALLER_REMOTE_ID);
    }

    @Test
    @Transactional
    void getAllContactsByNumberIsEqualToSomething() throws Exception {
        // Initialize the database
        contactRepository.saveAndFlush(contact);

        // Get all the contactList where number equals to DEFAULT_NUMBER
        defaultContactShouldBeFound("number.equals=" + DEFAULT_NUMBER);

        // Get all the contactList where number equals to UPDATED_NUMBER
        defaultContactShouldNotBeFound("number.equals=" + UPDATED_NUMBER);
    }

    @Test
    @Transactional
    void getAllContactsByNumberIsNotEqualToSomething() throws Exception {
        // Initialize the database
        contactRepository.saveAndFlush(contact);

        // Get all the contactList where number not equals to DEFAULT_NUMBER
        defaultContactShouldNotBeFound("number.notEquals=" + DEFAULT_NUMBER);

        // Get all the contactList where number not equals to UPDATED_NUMBER
        defaultContactShouldBeFound("number.notEquals=" + UPDATED_NUMBER);
    }

    @Test
    @Transactional
    void getAllContactsByNumberIsInShouldWork() throws Exception {
        // Initialize the database
        contactRepository.saveAndFlush(contact);

        // Get all the contactList where number in DEFAULT_NUMBER or UPDATED_NUMBER
        defaultContactShouldBeFound("number.in=" + DEFAULT_NUMBER + "," + UPDATED_NUMBER);

        // Get all the contactList where number equals to UPDATED_NUMBER
        defaultContactShouldNotBeFound("number.in=" + UPDATED_NUMBER);
    }

    @Test
    @Transactional
    void getAllContactsByNumberIsNullOrNotNull() throws Exception {
        // Initialize the database
        contactRepository.saveAndFlush(contact);

        // Get all the contactList where number is not null
        defaultContactShouldBeFound("number.specified=true");

        // Get all the contactList where number is null
        defaultContactShouldNotBeFound("number.specified=false");
    }

    @Test
    @Transactional
    void getAllContactsByNumberContainsSomething() throws Exception {
        // Initialize the database
        contactRepository.saveAndFlush(contact);

        // Get all the contactList where number contains DEFAULT_NUMBER
        defaultContactShouldBeFound("number.contains=" + DEFAULT_NUMBER);

        // Get all the contactList where number contains UPDATED_NUMBER
        defaultContactShouldNotBeFound("number.contains=" + UPDATED_NUMBER);
    }

    @Test
    @Transactional
    void getAllContactsByNumberNotContainsSomething() throws Exception {
        // Initialize the database
        contactRepository.saveAndFlush(contact);

        // Get all the contactList where number does not contain DEFAULT_NUMBER
        defaultContactShouldNotBeFound("number.doesNotContain=" + DEFAULT_NUMBER);

        // Get all the contactList where number does not contain UPDATED_NUMBER
        defaultContactShouldBeFound("number.doesNotContain=" + UPDATED_NUMBER);
    }

    @Test
    @Transactional
    void getAllContactsByTypeIsEqualToSomething() throws Exception {
        // Initialize the database
        contactRepository.saveAndFlush(contact);

        // Get all the contactList where type equals to DEFAULT_TYPE
        defaultContactShouldBeFound("type.equals=" + DEFAULT_TYPE);

        // Get all the contactList where type equals to UPDATED_TYPE
        defaultContactShouldNotBeFound("type.equals=" + UPDATED_TYPE);
    }

    @Test
    @Transactional
    void getAllContactsByTypeIsNotEqualToSomething() throws Exception {
        // Initialize the database
        contactRepository.saveAndFlush(contact);

        // Get all the contactList where type not equals to DEFAULT_TYPE
        defaultContactShouldNotBeFound("type.notEquals=" + DEFAULT_TYPE);

        // Get all the contactList where type not equals to UPDATED_TYPE
        defaultContactShouldBeFound("type.notEquals=" + UPDATED_TYPE);
    }

    @Test
    @Transactional
    void getAllContactsByTypeIsInShouldWork() throws Exception {
        // Initialize the database
        contactRepository.saveAndFlush(contact);

        // Get all the contactList where type in DEFAULT_TYPE or UPDATED_TYPE
        defaultContactShouldBeFound("type.in=" + DEFAULT_TYPE + "," + UPDATED_TYPE);

        // Get all the contactList where type equals to UPDATED_TYPE
        defaultContactShouldNotBeFound("type.in=" + UPDATED_TYPE);
    }

    @Test
    @Transactional
    void getAllContactsByTypeIsNullOrNotNull() throws Exception {
        // Initialize the database
        contactRepository.saveAndFlush(contact);

        // Get all the contactList where type is not null
        defaultContactShouldBeFound("type.specified=true");

        // Get all the contactList where type is null
        defaultContactShouldNotBeFound("type.specified=false");
    }

    @Test
    @Transactional
    void getAllContactsByGenderIsEqualToSomething() throws Exception {
        // Initialize the database
        contactRepository.saveAndFlush(contact);

        // Get all the contactList where gender equals to DEFAULT_GENDER
        defaultContactShouldBeFound("gender.equals=" + DEFAULT_GENDER);

        // Get all the contactList where gender equals to UPDATED_GENDER
        defaultContactShouldNotBeFound("gender.equals=" + UPDATED_GENDER);
    }

    @Test
    @Transactional
    void getAllContactsByGenderIsNotEqualToSomething() throws Exception {
        // Initialize the database
        contactRepository.saveAndFlush(contact);

        // Get all the contactList where gender not equals to DEFAULT_GENDER
        defaultContactShouldNotBeFound("gender.notEquals=" + DEFAULT_GENDER);

        // Get all the contactList where gender not equals to UPDATED_GENDER
        defaultContactShouldBeFound("gender.notEquals=" + UPDATED_GENDER);
    }

    @Test
    @Transactional
    void getAllContactsByGenderIsInShouldWork() throws Exception {
        // Initialize the database
        contactRepository.saveAndFlush(contact);

        // Get all the contactList where gender in DEFAULT_GENDER or UPDATED_GENDER
        defaultContactShouldBeFound("gender.in=" + DEFAULT_GENDER + "," + UPDATED_GENDER);

        // Get all the contactList where gender equals to UPDATED_GENDER
        defaultContactShouldNotBeFound("gender.in=" + UPDATED_GENDER);
    }

    @Test
    @Transactional
    void getAllContactsByGenderIsNullOrNotNull() throws Exception {
        // Initialize the database
        contactRepository.saveAndFlush(contact);

        // Get all the contactList where gender is not null
        defaultContactShouldBeFound("gender.specified=true");

        // Get all the contactList where gender is null
        defaultContactShouldNotBeFound("gender.specified=false");
    }

    @Test
    @Transactional
    void getAllContactsByGenderSalutationActiveIsEqualToSomething() throws Exception {
        // Initialize the database
        contactRepository.saveAndFlush(contact);

        // Get all the contactList where genderSalutationActive equals to DEFAULT_GENDER_SALUTATION_ACTIVE
        defaultContactShouldBeFound("genderSalutationActive.equals=" + DEFAULT_GENDER_SALUTATION_ACTIVE);

        // Get all the contactList where genderSalutationActive equals to UPDATED_GENDER_SALUTATION_ACTIVE
        defaultContactShouldNotBeFound("genderSalutationActive.equals=" + UPDATED_GENDER_SALUTATION_ACTIVE);
    }

    @Test
    @Transactional
    void getAllContactsByGenderSalutationActiveIsNotEqualToSomething() throws Exception {
        // Initialize the database
        contactRepository.saveAndFlush(contact);

        // Get all the contactList where genderSalutationActive not equals to DEFAULT_GENDER_SALUTATION_ACTIVE
        defaultContactShouldNotBeFound("genderSalutationActive.notEquals=" + DEFAULT_GENDER_SALUTATION_ACTIVE);

        // Get all the contactList where genderSalutationActive not equals to UPDATED_GENDER_SALUTATION_ACTIVE
        defaultContactShouldBeFound("genderSalutationActive.notEquals=" + UPDATED_GENDER_SALUTATION_ACTIVE);
    }

    @Test
    @Transactional
    void getAllContactsByGenderSalutationActiveIsInShouldWork() throws Exception {
        // Initialize the database
        contactRepository.saveAndFlush(contact);

        // Get all the contactList where genderSalutationActive in DEFAULT_GENDER_SALUTATION_ACTIVE or UPDATED_GENDER_SALUTATION_ACTIVE
        defaultContactShouldBeFound(
            "genderSalutationActive.in=" + DEFAULT_GENDER_SALUTATION_ACTIVE + "," + UPDATED_GENDER_SALUTATION_ACTIVE
        );

        // Get all the contactList where genderSalutationActive equals to UPDATED_GENDER_SALUTATION_ACTIVE
        defaultContactShouldNotBeFound("genderSalutationActive.in=" + UPDATED_GENDER_SALUTATION_ACTIVE);
    }

    @Test
    @Transactional
    void getAllContactsByGenderSalutationActiveIsNullOrNotNull() throws Exception {
        // Initialize the database
        contactRepository.saveAndFlush(contact);

        // Get all the contactList where genderSalutationActive is not null
        defaultContactShouldBeFound("genderSalutationActive.specified=true");

        // Get all the contactList where genderSalutationActive is null
        defaultContactShouldNotBeFound("genderSalutationActive.specified=false");
    }

    @Test
    @Transactional
    void getAllContactsByNameIsEqualToSomething() throws Exception {
        // Initialize the database
        contactRepository.saveAndFlush(contact);

        // Get all the contactList where name equals to DEFAULT_NAME
        defaultContactShouldBeFound("name.equals=" + DEFAULT_NAME);

        // Get all the contactList where name equals to UPDATED_NAME
        defaultContactShouldNotBeFound("name.equals=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllContactsByNameIsNotEqualToSomething() throws Exception {
        // Initialize the database
        contactRepository.saveAndFlush(contact);

        // Get all the contactList where name not equals to DEFAULT_NAME
        defaultContactShouldNotBeFound("name.notEquals=" + DEFAULT_NAME);

        // Get all the contactList where name not equals to UPDATED_NAME
        defaultContactShouldBeFound("name.notEquals=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllContactsByNameIsInShouldWork() throws Exception {
        // Initialize the database
        contactRepository.saveAndFlush(contact);

        // Get all the contactList where name in DEFAULT_NAME or UPDATED_NAME
        defaultContactShouldBeFound("name.in=" + DEFAULT_NAME + "," + UPDATED_NAME);

        // Get all the contactList where name equals to UPDATED_NAME
        defaultContactShouldNotBeFound("name.in=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllContactsByNameIsNullOrNotNull() throws Exception {
        // Initialize the database
        contactRepository.saveAndFlush(contact);

        // Get all the contactList where name is not null
        defaultContactShouldBeFound("name.specified=true");

        // Get all the contactList where name is null
        defaultContactShouldNotBeFound("name.specified=false");
    }

    @Test
    @Transactional
    void getAllContactsByNameContainsSomething() throws Exception {
        // Initialize the database
        contactRepository.saveAndFlush(contact);

        // Get all the contactList where name contains DEFAULT_NAME
        defaultContactShouldBeFound("name.contains=" + DEFAULT_NAME);

        // Get all the contactList where name contains UPDATED_NAME
        defaultContactShouldNotBeFound("name.contains=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllContactsByNameNotContainsSomething() throws Exception {
        // Initialize the database
        contactRepository.saveAndFlush(contact);

        // Get all the contactList where name does not contain DEFAULT_NAME
        defaultContactShouldNotBeFound("name.doesNotContain=" + DEFAULT_NAME);

        // Get all the contactList where name does not contain UPDATED_NAME
        defaultContactShouldBeFound("name.doesNotContain=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllContactsByNameAdditionIsEqualToSomething() throws Exception {
        // Initialize the database
        contactRepository.saveAndFlush(contact);

        // Get all the contactList where nameAddition equals to DEFAULT_NAME_ADDITION
        defaultContactShouldBeFound("nameAddition.equals=" + DEFAULT_NAME_ADDITION);

        // Get all the contactList where nameAddition equals to UPDATED_NAME_ADDITION
        defaultContactShouldNotBeFound("nameAddition.equals=" + UPDATED_NAME_ADDITION);
    }

    @Test
    @Transactional
    void getAllContactsByNameAdditionIsNotEqualToSomething() throws Exception {
        // Initialize the database
        contactRepository.saveAndFlush(contact);

        // Get all the contactList where nameAddition not equals to DEFAULT_NAME_ADDITION
        defaultContactShouldNotBeFound("nameAddition.notEquals=" + DEFAULT_NAME_ADDITION);

        // Get all the contactList where nameAddition not equals to UPDATED_NAME_ADDITION
        defaultContactShouldBeFound("nameAddition.notEquals=" + UPDATED_NAME_ADDITION);
    }

    @Test
    @Transactional
    void getAllContactsByNameAdditionIsInShouldWork() throws Exception {
        // Initialize the database
        contactRepository.saveAndFlush(contact);

        // Get all the contactList where nameAddition in DEFAULT_NAME_ADDITION or UPDATED_NAME_ADDITION
        defaultContactShouldBeFound("nameAddition.in=" + DEFAULT_NAME_ADDITION + "," + UPDATED_NAME_ADDITION);

        // Get all the contactList where nameAddition equals to UPDATED_NAME_ADDITION
        defaultContactShouldNotBeFound("nameAddition.in=" + UPDATED_NAME_ADDITION);
    }

    @Test
    @Transactional
    void getAllContactsByNameAdditionIsNullOrNotNull() throws Exception {
        // Initialize the database
        contactRepository.saveAndFlush(contact);

        // Get all the contactList where nameAddition is not null
        defaultContactShouldBeFound("nameAddition.specified=true");

        // Get all the contactList where nameAddition is null
        defaultContactShouldNotBeFound("nameAddition.specified=false");
    }

    @Test
    @Transactional
    void getAllContactsByNameAdditionContainsSomething() throws Exception {
        // Initialize the database
        contactRepository.saveAndFlush(contact);

        // Get all the contactList where nameAddition contains DEFAULT_NAME_ADDITION
        defaultContactShouldBeFound("nameAddition.contains=" + DEFAULT_NAME_ADDITION);

        // Get all the contactList where nameAddition contains UPDATED_NAME_ADDITION
        defaultContactShouldNotBeFound("nameAddition.contains=" + UPDATED_NAME_ADDITION);
    }

    @Test
    @Transactional
    void getAllContactsByNameAdditionNotContainsSomething() throws Exception {
        // Initialize the database
        contactRepository.saveAndFlush(contact);

        // Get all the contactList where nameAddition does not contain DEFAULT_NAME_ADDITION
        defaultContactShouldNotBeFound("nameAddition.doesNotContain=" + DEFAULT_NAME_ADDITION);

        // Get all the contactList where nameAddition does not contain UPDATED_NAME_ADDITION
        defaultContactShouldBeFound("nameAddition.doesNotContain=" + UPDATED_NAME_ADDITION);
    }

    @Test
    @Transactional
    void getAllContactsBySalutationIsEqualToSomething() throws Exception {
        // Initialize the database
        contactRepository.saveAndFlush(contact);

        // Get all the contactList where salutation equals to DEFAULT_SALUTATION
        defaultContactShouldBeFound("salutation.equals=" + DEFAULT_SALUTATION);

        // Get all the contactList where salutation equals to UPDATED_SALUTATION
        defaultContactShouldNotBeFound("salutation.equals=" + UPDATED_SALUTATION);
    }

    @Test
    @Transactional
    void getAllContactsBySalutationIsNotEqualToSomething() throws Exception {
        // Initialize the database
        contactRepository.saveAndFlush(contact);

        // Get all the contactList where salutation not equals to DEFAULT_SALUTATION
        defaultContactShouldNotBeFound("salutation.notEquals=" + DEFAULT_SALUTATION);

        // Get all the contactList where salutation not equals to UPDATED_SALUTATION
        defaultContactShouldBeFound("salutation.notEquals=" + UPDATED_SALUTATION);
    }

    @Test
    @Transactional
    void getAllContactsBySalutationIsInShouldWork() throws Exception {
        // Initialize the database
        contactRepository.saveAndFlush(contact);

        // Get all the contactList where salutation in DEFAULT_SALUTATION or UPDATED_SALUTATION
        defaultContactShouldBeFound("salutation.in=" + DEFAULT_SALUTATION + "," + UPDATED_SALUTATION);

        // Get all the contactList where salutation equals to UPDATED_SALUTATION
        defaultContactShouldNotBeFound("salutation.in=" + UPDATED_SALUTATION);
    }

    @Test
    @Transactional
    void getAllContactsBySalutationIsNullOrNotNull() throws Exception {
        // Initialize the database
        contactRepository.saveAndFlush(contact);

        // Get all the contactList where salutation is not null
        defaultContactShouldBeFound("salutation.specified=true");

        // Get all the contactList where salutation is null
        defaultContactShouldNotBeFound("salutation.specified=false");
    }

    @Test
    @Transactional
    void getAllContactsBySalutationContainsSomething() throws Exception {
        // Initialize the database
        contactRepository.saveAndFlush(contact);

        // Get all the contactList where salutation contains DEFAULT_SALUTATION
        defaultContactShouldBeFound("salutation.contains=" + DEFAULT_SALUTATION);

        // Get all the contactList where salutation contains UPDATED_SALUTATION
        defaultContactShouldNotBeFound("salutation.contains=" + UPDATED_SALUTATION);
    }

    @Test
    @Transactional
    void getAllContactsBySalutationNotContainsSomething() throws Exception {
        // Initialize the database
        contactRepository.saveAndFlush(contact);

        // Get all the contactList where salutation does not contain DEFAULT_SALUTATION
        defaultContactShouldNotBeFound("salutation.doesNotContain=" + DEFAULT_SALUTATION);

        // Get all the contactList where salutation does not contain UPDATED_SALUTATION
        defaultContactShouldBeFound("salutation.doesNotContain=" + UPDATED_SALUTATION);
    }

    @Test
    @Transactional
    void getAllContactsByPhoneIsEqualToSomething() throws Exception {
        // Initialize the database
        contactRepository.saveAndFlush(contact);

        // Get all the contactList where phone equals to DEFAULT_PHONE
        defaultContactShouldBeFound("phone.equals=" + DEFAULT_PHONE);

        // Get all the contactList where phone equals to UPDATED_PHONE
        defaultContactShouldNotBeFound("phone.equals=" + UPDATED_PHONE);
    }

    @Test
    @Transactional
    void getAllContactsByPhoneIsNotEqualToSomething() throws Exception {
        // Initialize the database
        contactRepository.saveAndFlush(contact);

        // Get all the contactList where phone not equals to DEFAULT_PHONE
        defaultContactShouldNotBeFound("phone.notEquals=" + DEFAULT_PHONE);

        // Get all the contactList where phone not equals to UPDATED_PHONE
        defaultContactShouldBeFound("phone.notEquals=" + UPDATED_PHONE);
    }

    @Test
    @Transactional
    void getAllContactsByPhoneIsInShouldWork() throws Exception {
        // Initialize the database
        contactRepository.saveAndFlush(contact);

        // Get all the contactList where phone in DEFAULT_PHONE or UPDATED_PHONE
        defaultContactShouldBeFound("phone.in=" + DEFAULT_PHONE + "," + UPDATED_PHONE);

        // Get all the contactList where phone equals to UPDATED_PHONE
        defaultContactShouldNotBeFound("phone.in=" + UPDATED_PHONE);
    }

    @Test
    @Transactional
    void getAllContactsByPhoneIsNullOrNotNull() throws Exception {
        // Initialize the database
        contactRepository.saveAndFlush(contact);

        // Get all the contactList where phone is not null
        defaultContactShouldBeFound("phone.specified=true");

        // Get all the contactList where phone is null
        defaultContactShouldNotBeFound("phone.specified=false");
    }

    @Test
    @Transactional
    void getAllContactsByPhoneContainsSomething() throws Exception {
        // Initialize the database
        contactRepository.saveAndFlush(contact);

        // Get all the contactList where phone contains DEFAULT_PHONE
        defaultContactShouldBeFound("phone.contains=" + DEFAULT_PHONE);

        // Get all the contactList where phone contains UPDATED_PHONE
        defaultContactShouldNotBeFound("phone.contains=" + UPDATED_PHONE);
    }

    @Test
    @Transactional
    void getAllContactsByPhoneNotContainsSomething() throws Exception {
        // Initialize the database
        contactRepository.saveAndFlush(contact);

        // Get all the contactList where phone does not contain DEFAULT_PHONE
        defaultContactShouldNotBeFound("phone.doesNotContain=" + DEFAULT_PHONE);

        // Get all the contactList where phone does not contain UPDATED_PHONE
        defaultContactShouldBeFound("phone.doesNotContain=" + UPDATED_PHONE);
    }

    @Test
    @Transactional
    void getAllContactsByFaxIsEqualToSomething() throws Exception {
        // Initialize the database
        contactRepository.saveAndFlush(contact);

        // Get all the contactList where fax equals to DEFAULT_FAX
        defaultContactShouldBeFound("fax.equals=" + DEFAULT_FAX);

        // Get all the contactList where fax equals to UPDATED_FAX
        defaultContactShouldNotBeFound("fax.equals=" + UPDATED_FAX);
    }

    @Test
    @Transactional
    void getAllContactsByFaxIsNotEqualToSomething() throws Exception {
        // Initialize the database
        contactRepository.saveAndFlush(contact);

        // Get all the contactList where fax not equals to DEFAULT_FAX
        defaultContactShouldNotBeFound("fax.notEquals=" + DEFAULT_FAX);

        // Get all the contactList where fax not equals to UPDATED_FAX
        defaultContactShouldBeFound("fax.notEquals=" + UPDATED_FAX);
    }

    @Test
    @Transactional
    void getAllContactsByFaxIsInShouldWork() throws Exception {
        // Initialize the database
        contactRepository.saveAndFlush(contact);

        // Get all the contactList where fax in DEFAULT_FAX or UPDATED_FAX
        defaultContactShouldBeFound("fax.in=" + DEFAULT_FAX + "," + UPDATED_FAX);

        // Get all the contactList where fax equals to UPDATED_FAX
        defaultContactShouldNotBeFound("fax.in=" + UPDATED_FAX);
    }

    @Test
    @Transactional
    void getAllContactsByFaxIsNullOrNotNull() throws Exception {
        // Initialize the database
        contactRepository.saveAndFlush(contact);

        // Get all the contactList where fax is not null
        defaultContactShouldBeFound("fax.specified=true");

        // Get all the contactList where fax is null
        defaultContactShouldNotBeFound("fax.specified=false");
    }

    @Test
    @Transactional
    void getAllContactsByFaxContainsSomething() throws Exception {
        // Initialize the database
        contactRepository.saveAndFlush(contact);

        // Get all the contactList where fax contains DEFAULT_FAX
        defaultContactShouldBeFound("fax.contains=" + DEFAULT_FAX);

        // Get all the contactList where fax contains UPDATED_FAX
        defaultContactShouldNotBeFound("fax.contains=" + UPDATED_FAX);
    }

    @Test
    @Transactional
    void getAllContactsByFaxNotContainsSomething() throws Exception {
        // Initialize the database
        contactRepository.saveAndFlush(contact);

        // Get all the contactList where fax does not contain DEFAULT_FAX
        defaultContactShouldNotBeFound("fax.doesNotContain=" + DEFAULT_FAX);

        // Get all the contactList where fax does not contain UPDATED_FAX
        defaultContactShouldBeFound("fax.doesNotContain=" + UPDATED_FAX);
    }

    @Test
    @Transactional
    void getAllContactsByEmailIsEqualToSomething() throws Exception {
        // Initialize the database
        contactRepository.saveAndFlush(contact);

        // Get all the contactList where email equals to DEFAULT_EMAIL
        defaultContactShouldBeFound("email.equals=" + DEFAULT_EMAIL);

        // Get all the contactList where email equals to UPDATED_EMAIL
        defaultContactShouldNotBeFound("email.equals=" + UPDATED_EMAIL);
    }

    @Test
    @Transactional
    void getAllContactsByEmailIsNotEqualToSomething() throws Exception {
        // Initialize the database
        contactRepository.saveAndFlush(contact);

        // Get all the contactList where email not equals to DEFAULT_EMAIL
        defaultContactShouldNotBeFound("email.notEquals=" + DEFAULT_EMAIL);

        // Get all the contactList where email not equals to UPDATED_EMAIL
        defaultContactShouldBeFound("email.notEquals=" + UPDATED_EMAIL);
    }

    @Test
    @Transactional
    void getAllContactsByEmailIsInShouldWork() throws Exception {
        // Initialize the database
        contactRepository.saveAndFlush(contact);

        // Get all the contactList where email in DEFAULT_EMAIL or UPDATED_EMAIL
        defaultContactShouldBeFound("email.in=" + DEFAULT_EMAIL + "," + UPDATED_EMAIL);

        // Get all the contactList where email equals to UPDATED_EMAIL
        defaultContactShouldNotBeFound("email.in=" + UPDATED_EMAIL);
    }

    @Test
    @Transactional
    void getAllContactsByEmailIsNullOrNotNull() throws Exception {
        // Initialize the database
        contactRepository.saveAndFlush(contact);

        // Get all the contactList where email is not null
        defaultContactShouldBeFound("email.specified=true");

        // Get all the contactList where email is null
        defaultContactShouldNotBeFound("email.specified=false");
    }

    @Test
    @Transactional
    void getAllContactsByEmailContainsSomething() throws Exception {
        // Initialize the database
        contactRepository.saveAndFlush(contact);

        // Get all the contactList where email contains DEFAULT_EMAIL
        defaultContactShouldBeFound("email.contains=" + DEFAULT_EMAIL);

        // Get all the contactList where email contains UPDATED_EMAIL
        defaultContactShouldNotBeFound("email.contains=" + UPDATED_EMAIL);
    }

    @Test
    @Transactional
    void getAllContactsByEmailNotContainsSomething() throws Exception {
        // Initialize the database
        contactRepository.saveAndFlush(contact);

        // Get all the contactList where email does not contain DEFAULT_EMAIL
        defaultContactShouldNotBeFound("email.doesNotContain=" + DEFAULT_EMAIL);

        // Get all the contactList where email does not contain UPDATED_EMAIL
        defaultContactShouldBeFound("email.doesNotContain=" + UPDATED_EMAIL);
    }

    @Test
    @Transactional
    void getAllContactsByWebsiteIsEqualToSomething() throws Exception {
        // Initialize the database
        contactRepository.saveAndFlush(contact);

        // Get all the contactList where website equals to DEFAULT_WEBSITE
        defaultContactShouldBeFound("website.equals=" + DEFAULT_WEBSITE);

        // Get all the contactList where website equals to UPDATED_WEBSITE
        defaultContactShouldNotBeFound("website.equals=" + UPDATED_WEBSITE);
    }

    @Test
    @Transactional
    void getAllContactsByWebsiteIsNotEqualToSomething() throws Exception {
        // Initialize the database
        contactRepository.saveAndFlush(contact);

        // Get all the contactList where website not equals to DEFAULT_WEBSITE
        defaultContactShouldNotBeFound("website.notEquals=" + DEFAULT_WEBSITE);

        // Get all the contactList where website not equals to UPDATED_WEBSITE
        defaultContactShouldBeFound("website.notEquals=" + UPDATED_WEBSITE);
    }

    @Test
    @Transactional
    void getAllContactsByWebsiteIsInShouldWork() throws Exception {
        // Initialize the database
        contactRepository.saveAndFlush(contact);

        // Get all the contactList where website in DEFAULT_WEBSITE or UPDATED_WEBSITE
        defaultContactShouldBeFound("website.in=" + DEFAULT_WEBSITE + "," + UPDATED_WEBSITE);

        // Get all the contactList where website equals to UPDATED_WEBSITE
        defaultContactShouldNotBeFound("website.in=" + UPDATED_WEBSITE);
    }

    @Test
    @Transactional
    void getAllContactsByWebsiteIsNullOrNotNull() throws Exception {
        // Initialize the database
        contactRepository.saveAndFlush(contact);

        // Get all the contactList where website is not null
        defaultContactShouldBeFound("website.specified=true");

        // Get all the contactList where website is null
        defaultContactShouldNotBeFound("website.specified=false");
    }

    @Test
    @Transactional
    void getAllContactsByWebsiteContainsSomething() throws Exception {
        // Initialize the database
        contactRepository.saveAndFlush(contact);

        // Get all the contactList where website contains DEFAULT_WEBSITE
        defaultContactShouldBeFound("website.contains=" + DEFAULT_WEBSITE);

        // Get all the contactList where website contains UPDATED_WEBSITE
        defaultContactShouldNotBeFound("website.contains=" + UPDATED_WEBSITE);
    }

    @Test
    @Transactional
    void getAllContactsByWebsiteNotContainsSomething() throws Exception {
        // Initialize the database
        contactRepository.saveAndFlush(contact);

        // Get all the contactList where website does not contain DEFAULT_WEBSITE
        defaultContactShouldNotBeFound("website.doesNotContain=" + DEFAULT_WEBSITE);

        // Get all the contactList where website does not contain UPDATED_WEBSITE
        defaultContactShouldBeFound("website.doesNotContain=" + UPDATED_WEBSITE);
    }

    @Test
    @Transactional
    void getAllContactsByNotesIsEqualToSomething() throws Exception {
        // Initialize the database
        contactRepository.saveAndFlush(contact);

        // Get all the contactList where notes equals to DEFAULT_NOTES
        defaultContactShouldBeFound("notes.equals=" + DEFAULT_NOTES);

        // Get all the contactList where notes equals to UPDATED_NOTES
        defaultContactShouldNotBeFound("notes.equals=" + UPDATED_NOTES);
    }

    @Test
    @Transactional
    void getAllContactsByNotesIsNotEqualToSomething() throws Exception {
        // Initialize the database
        contactRepository.saveAndFlush(contact);

        // Get all the contactList where notes not equals to DEFAULT_NOTES
        defaultContactShouldNotBeFound("notes.notEquals=" + DEFAULT_NOTES);

        // Get all the contactList where notes not equals to UPDATED_NOTES
        defaultContactShouldBeFound("notes.notEquals=" + UPDATED_NOTES);
    }

    @Test
    @Transactional
    void getAllContactsByNotesIsInShouldWork() throws Exception {
        // Initialize the database
        contactRepository.saveAndFlush(contact);

        // Get all the contactList where notes in DEFAULT_NOTES or UPDATED_NOTES
        defaultContactShouldBeFound("notes.in=" + DEFAULT_NOTES + "," + UPDATED_NOTES);

        // Get all the contactList where notes equals to UPDATED_NOTES
        defaultContactShouldNotBeFound("notes.in=" + UPDATED_NOTES);
    }

    @Test
    @Transactional
    void getAllContactsByNotesIsNullOrNotNull() throws Exception {
        // Initialize the database
        contactRepository.saveAndFlush(contact);

        // Get all the contactList where notes is not null
        defaultContactShouldBeFound("notes.specified=true");

        // Get all the contactList where notes is null
        defaultContactShouldNotBeFound("notes.specified=false");
    }

    @Test
    @Transactional
    void getAllContactsByNotesContainsSomething() throws Exception {
        // Initialize the database
        contactRepository.saveAndFlush(contact);

        // Get all the contactList where notes contains DEFAULT_NOTES
        defaultContactShouldBeFound("notes.contains=" + DEFAULT_NOTES);

        // Get all the contactList where notes contains UPDATED_NOTES
        defaultContactShouldNotBeFound("notes.contains=" + UPDATED_NOTES);
    }

    @Test
    @Transactional
    void getAllContactsByNotesNotContainsSomething() throws Exception {
        // Initialize the database
        contactRepository.saveAndFlush(contact);

        // Get all the contactList where notes does not contain DEFAULT_NOTES
        defaultContactShouldNotBeFound("notes.doesNotContain=" + DEFAULT_NOTES);

        // Get all the contactList where notes does not contain UPDATED_NOTES
        defaultContactShouldBeFound("notes.doesNotContain=" + UPDATED_NOTES);
    }

    @Test
    @Transactional
    void getAllContactsByCommunicationLanguageIsEqualToSomething() throws Exception {
        // Initialize the database
        contactRepository.saveAndFlush(contact);

        // Get all the contactList where communicationLanguage equals to DEFAULT_COMMUNICATION_LANGUAGE
        defaultContactShouldBeFound("communicationLanguage.equals=" + DEFAULT_COMMUNICATION_LANGUAGE);

        // Get all the contactList where communicationLanguage equals to UPDATED_COMMUNICATION_LANGUAGE
        defaultContactShouldNotBeFound("communicationLanguage.equals=" + UPDATED_COMMUNICATION_LANGUAGE);
    }

    @Test
    @Transactional
    void getAllContactsByCommunicationLanguageIsNotEqualToSomething() throws Exception {
        // Initialize the database
        contactRepository.saveAndFlush(contact);

        // Get all the contactList where communicationLanguage not equals to DEFAULT_COMMUNICATION_LANGUAGE
        defaultContactShouldNotBeFound("communicationLanguage.notEquals=" + DEFAULT_COMMUNICATION_LANGUAGE);

        // Get all the contactList where communicationLanguage not equals to UPDATED_COMMUNICATION_LANGUAGE
        defaultContactShouldBeFound("communicationLanguage.notEquals=" + UPDATED_COMMUNICATION_LANGUAGE);
    }

    @Test
    @Transactional
    void getAllContactsByCommunicationLanguageIsInShouldWork() throws Exception {
        // Initialize the database
        contactRepository.saveAndFlush(contact);

        // Get all the contactList where communicationLanguage in DEFAULT_COMMUNICATION_LANGUAGE or UPDATED_COMMUNICATION_LANGUAGE
        defaultContactShouldBeFound("communicationLanguage.in=" + DEFAULT_COMMUNICATION_LANGUAGE + "," + UPDATED_COMMUNICATION_LANGUAGE);

        // Get all the contactList where communicationLanguage equals to UPDATED_COMMUNICATION_LANGUAGE
        defaultContactShouldNotBeFound("communicationLanguage.in=" + UPDATED_COMMUNICATION_LANGUAGE);
    }

    @Test
    @Transactional
    void getAllContactsByCommunicationLanguageIsNullOrNotNull() throws Exception {
        // Initialize the database
        contactRepository.saveAndFlush(contact);

        // Get all the contactList where communicationLanguage is not null
        defaultContactShouldBeFound("communicationLanguage.specified=true");

        // Get all the contactList where communicationLanguage is null
        defaultContactShouldNotBeFound("communicationLanguage.specified=false");
    }

    @Test
    @Transactional
    void getAllContactsByCommunicationLanguageContainsSomething() throws Exception {
        // Initialize the database
        contactRepository.saveAndFlush(contact);

        // Get all the contactList where communicationLanguage contains DEFAULT_COMMUNICATION_LANGUAGE
        defaultContactShouldBeFound("communicationLanguage.contains=" + DEFAULT_COMMUNICATION_LANGUAGE);

        // Get all the contactList where communicationLanguage contains UPDATED_COMMUNICATION_LANGUAGE
        defaultContactShouldNotBeFound("communicationLanguage.contains=" + UPDATED_COMMUNICATION_LANGUAGE);
    }

    @Test
    @Transactional
    void getAllContactsByCommunicationLanguageNotContainsSomething() throws Exception {
        // Initialize the database
        contactRepository.saveAndFlush(contact);

        // Get all the contactList where communicationLanguage does not contain DEFAULT_COMMUNICATION_LANGUAGE
        defaultContactShouldNotBeFound("communicationLanguage.doesNotContain=" + DEFAULT_COMMUNICATION_LANGUAGE);

        // Get all the contactList where communicationLanguage does not contain UPDATED_COMMUNICATION_LANGUAGE
        defaultContactShouldBeFound("communicationLanguage.doesNotContain=" + UPDATED_COMMUNICATION_LANGUAGE);
    }

    @Test
    @Transactional
    void getAllContactsByCommunicationChannelIsEqualToSomething() throws Exception {
        // Initialize the database
        contactRepository.saveAndFlush(contact);

        // Get all the contactList where communicationChannel equals to DEFAULT_COMMUNICATION_CHANNEL
        defaultContactShouldBeFound("communicationChannel.equals=" + DEFAULT_COMMUNICATION_CHANNEL);

        // Get all the contactList where communicationChannel equals to UPDATED_COMMUNICATION_CHANNEL
        defaultContactShouldNotBeFound("communicationChannel.equals=" + UPDATED_COMMUNICATION_CHANNEL);
    }

    @Test
    @Transactional
    void getAllContactsByCommunicationChannelIsNotEqualToSomething() throws Exception {
        // Initialize the database
        contactRepository.saveAndFlush(contact);

        // Get all the contactList where communicationChannel not equals to DEFAULT_COMMUNICATION_CHANNEL
        defaultContactShouldNotBeFound("communicationChannel.notEquals=" + DEFAULT_COMMUNICATION_CHANNEL);

        // Get all the contactList where communicationChannel not equals to UPDATED_COMMUNICATION_CHANNEL
        defaultContactShouldBeFound("communicationChannel.notEquals=" + UPDATED_COMMUNICATION_CHANNEL);
    }

    @Test
    @Transactional
    void getAllContactsByCommunicationChannelIsInShouldWork() throws Exception {
        // Initialize the database
        contactRepository.saveAndFlush(contact);

        // Get all the contactList where communicationChannel in DEFAULT_COMMUNICATION_CHANNEL or UPDATED_COMMUNICATION_CHANNEL
        defaultContactShouldBeFound("communicationChannel.in=" + DEFAULT_COMMUNICATION_CHANNEL + "," + UPDATED_COMMUNICATION_CHANNEL);

        // Get all the contactList where communicationChannel equals to UPDATED_COMMUNICATION_CHANNEL
        defaultContactShouldNotBeFound("communicationChannel.in=" + UPDATED_COMMUNICATION_CHANNEL);
    }

    @Test
    @Transactional
    void getAllContactsByCommunicationChannelIsNullOrNotNull() throws Exception {
        // Initialize the database
        contactRepository.saveAndFlush(contact);

        // Get all the contactList where communicationChannel is not null
        defaultContactShouldBeFound("communicationChannel.specified=true");

        // Get all the contactList where communicationChannel is null
        defaultContactShouldNotBeFound("communicationChannel.specified=false");
    }

    @Test
    @Transactional
    void getAllContactsByCommunicationNewsletterIsEqualToSomething() throws Exception {
        // Initialize the database
        contactRepository.saveAndFlush(contact);

        // Get all the contactList where communicationNewsletter equals to DEFAULT_COMMUNICATION_NEWSLETTER
        defaultContactShouldBeFound("communicationNewsletter.equals=" + DEFAULT_COMMUNICATION_NEWSLETTER);

        // Get all the contactList where communicationNewsletter equals to UPDATED_COMMUNICATION_NEWSLETTER
        defaultContactShouldNotBeFound("communicationNewsletter.equals=" + UPDATED_COMMUNICATION_NEWSLETTER);
    }

    @Test
    @Transactional
    void getAllContactsByCommunicationNewsletterIsNotEqualToSomething() throws Exception {
        // Initialize the database
        contactRepository.saveAndFlush(contact);

        // Get all the contactList where communicationNewsletter not equals to DEFAULT_COMMUNICATION_NEWSLETTER
        defaultContactShouldNotBeFound("communicationNewsletter.notEquals=" + DEFAULT_COMMUNICATION_NEWSLETTER);

        // Get all the contactList where communicationNewsletter not equals to UPDATED_COMMUNICATION_NEWSLETTER
        defaultContactShouldBeFound("communicationNewsletter.notEquals=" + UPDATED_COMMUNICATION_NEWSLETTER);
    }

    @Test
    @Transactional
    void getAllContactsByCommunicationNewsletterIsInShouldWork() throws Exception {
        // Initialize the database
        contactRepository.saveAndFlush(contact);

        // Get all the contactList where communicationNewsletter in DEFAULT_COMMUNICATION_NEWSLETTER or UPDATED_COMMUNICATION_NEWSLETTER
        defaultContactShouldBeFound(
            "communicationNewsletter.in=" + DEFAULT_COMMUNICATION_NEWSLETTER + "," + UPDATED_COMMUNICATION_NEWSLETTER
        );

        // Get all the contactList where communicationNewsletter equals to UPDATED_COMMUNICATION_NEWSLETTER
        defaultContactShouldNotBeFound("communicationNewsletter.in=" + UPDATED_COMMUNICATION_NEWSLETTER);
    }

    @Test
    @Transactional
    void getAllContactsByCommunicationNewsletterIsNullOrNotNull() throws Exception {
        // Initialize the database
        contactRepository.saveAndFlush(contact);

        // Get all the contactList where communicationNewsletter is not null
        defaultContactShouldBeFound("communicationNewsletter.specified=true");

        // Get all the contactList where communicationNewsletter is null
        defaultContactShouldNotBeFound("communicationNewsletter.specified=false");
    }

    @Test
    @Transactional
    void getAllContactsByCurrencyIsEqualToSomething() throws Exception {
        // Initialize the database
        contactRepository.saveAndFlush(contact);

        // Get all the contactList where currency equals to DEFAULT_CURRENCY
        defaultContactShouldBeFound("currency.equals=" + DEFAULT_CURRENCY);

        // Get all the contactList where currency equals to UPDATED_CURRENCY
        defaultContactShouldNotBeFound("currency.equals=" + UPDATED_CURRENCY);
    }

    @Test
    @Transactional
    void getAllContactsByCurrencyIsNotEqualToSomething() throws Exception {
        // Initialize the database
        contactRepository.saveAndFlush(contact);

        // Get all the contactList where currency not equals to DEFAULT_CURRENCY
        defaultContactShouldNotBeFound("currency.notEquals=" + DEFAULT_CURRENCY);

        // Get all the contactList where currency not equals to UPDATED_CURRENCY
        defaultContactShouldBeFound("currency.notEquals=" + UPDATED_CURRENCY);
    }

    @Test
    @Transactional
    void getAllContactsByCurrencyIsInShouldWork() throws Exception {
        // Initialize the database
        contactRepository.saveAndFlush(contact);

        // Get all the contactList where currency in DEFAULT_CURRENCY or UPDATED_CURRENCY
        defaultContactShouldBeFound("currency.in=" + DEFAULT_CURRENCY + "," + UPDATED_CURRENCY);

        // Get all the contactList where currency equals to UPDATED_CURRENCY
        defaultContactShouldNotBeFound("currency.in=" + UPDATED_CURRENCY);
    }

    @Test
    @Transactional
    void getAllContactsByCurrencyIsNullOrNotNull() throws Exception {
        // Initialize the database
        contactRepository.saveAndFlush(contact);

        // Get all the contactList where currency is not null
        defaultContactShouldBeFound("currency.specified=true");

        // Get all the contactList where currency is null
        defaultContactShouldNotBeFound("currency.specified=false");
    }

    @Test
    @Transactional
    void getAllContactsByEbillAccountIdIsEqualToSomething() throws Exception {
        // Initialize the database
        contactRepository.saveAndFlush(contact);

        // Get all the contactList where ebillAccountId equals to DEFAULT_EBILL_ACCOUNT_ID
        defaultContactShouldBeFound("ebillAccountId.equals=" + DEFAULT_EBILL_ACCOUNT_ID);

        // Get all the contactList where ebillAccountId equals to UPDATED_EBILL_ACCOUNT_ID
        defaultContactShouldNotBeFound("ebillAccountId.equals=" + UPDATED_EBILL_ACCOUNT_ID);
    }

    @Test
    @Transactional
    void getAllContactsByEbillAccountIdIsNotEqualToSomething() throws Exception {
        // Initialize the database
        contactRepository.saveAndFlush(contact);

        // Get all the contactList where ebillAccountId not equals to DEFAULT_EBILL_ACCOUNT_ID
        defaultContactShouldNotBeFound("ebillAccountId.notEquals=" + DEFAULT_EBILL_ACCOUNT_ID);

        // Get all the contactList where ebillAccountId not equals to UPDATED_EBILL_ACCOUNT_ID
        defaultContactShouldBeFound("ebillAccountId.notEquals=" + UPDATED_EBILL_ACCOUNT_ID);
    }

    @Test
    @Transactional
    void getAllContactsByEbillAccountIdIsInShouldWork() throws Exception {
        // Initialize the database
        contactRepository.saveAndFlush(contact);

        // Get all the contactList where ebillAccountId in DEFAULT_EBILL_ACCOUNT_ID or UPDATED_EBILL_ACCOUNT_ID
        defaultContactShouldBeFound("ebillAccountId.in=" + DEFAULT_EBILL_ACCOUNT_ID + "," + UPDATED_EBILL_ACCOUNT_ID);

        // Get all the contactList where ebillAccountId equals to UPDATED_EBILL_ACCOUNT_ID
        defaultContactShouldNotBeFound("ebillAccountId.in=" + UPDATED_EBILL_ACCOUNT_ID);
    }

    @Test
    @Transactional
    void getAllContactsByEbillAccountIdIsNullOrNotNull() throws Exception {
        // Initialize the database
        contactRepository.saveAndFlush(contact);

        // Get all the contactList where ebillAccountId is not null
        defaultContactShouldBeFound("ebillAccountId.specified=true");

        // Get all the contactList where ebillAccountId is null
        defaultContactShouldNotBeFound("ebillAccountId.specified=false");
    }

    @Test
    @Transactional
    void getAllContactsByEbillAccountIdContainsSomething() throws Exception {
        // Initialize the database
        contactRepository.saveAndFlush(contact);

        // Get all the contactList where ebillAccountId contains DEFAULT_EBILL_ACCOUNT_ID
        defaultContactShouldBeFound("ebillAccountId.contains=" + DEFAULT_EBILL_ACCOUNT_ID);

        // Get all the contactList where ebillAccountId contains UPDATED_EBILL_ACCOUNT_ID
        defaultContactShouldNotBeFound("ebillAccountId.contains=" + UPDATED_EBILL_ACCOUNT_ID);
    }

    @Test
    @Transactional
    void getAllContactsByEbillAccountIdNotContainsSomething() throws Exception {
        // Initialize the database
        contactRepository.saveAndFlush(contact);

        // Get all the contactList where ebillAccountId does not contain DEFAULT_EBILL_ACCOUNT_ID
        defaultContactShouldNotBeFound("ebillAccountId.doesNotContain=" + DEFAULT_EBILL_ACCOUNT_ID);

        // Get all the contactList where ebillAccountId does not contain UPDATED_EBILL_ACCOUNT_ID
        defaultContactShouldBeFound("ebillAccountId.doesNotContain=" + UPDATED_EBILL_ACCOUNT_ID);
    }

    @Test
    @Transactional
    void getAllContactsByVatIdentificationIsEqualToSomething() throws Exception {
        // Initialize the database
        contactRepository.saveAndFlush(contact);

        // Get all the contactList where vatIdentification equals to DEFAULT_VAT_IDENTIFICATION
        defaultContactShouldBeFound("vatIdentification.equals=" + DEFAULT_VAT_IDENTIFICATION);

        // Get all the contactList where vatIdentification equals to UPDATED_VAT_IDENTIFICATION
        defaultContactShouldNotBeFound("vatIdentification.equals=" + UPDATED_VAT_IDENTIFICATION);
    }

    @Test
    @Transactional
    void getAllContactsByVatIdentificationIsNotEqualToSomething() throws Exception {
        // Initialize the database
        contactRepository.saveAndFlush(contact);

        // Get all the contactList where vatIdentification not equals to DEFAULT_VAT_IDENTIFICATION
        defaultContactShouldNotBeFound("vatIdentification.notEquals=" + DEFAULT_VAT_IDENTIFICATION);

        // Get all the contactList where vatIdentification not equals to UPDATED_VAT_IDENTIFICATION
        defaultContactShouldBeFound("vatIdentification.notEquals=" + UPDATED_VAT_IDENTIFICATION);
    }

    @Test
    @Transactional
    void getAllContactsByVatIdentificationIsInShouldWork() throws Exception {
        // Initialize the database
        contactRepository.saveAndFlush(contact);

        // Get all the contactList where vatIdentification in DEFAULT_VAT_IDENTIFICATION or UPDATED_VAT_IDENTIFICATION
        defaultContactShouldBeFound("vatIdentification.in=" + DEFAULT_VAT_IDENTIFICATION + "," + UPDATED_VAT_IDENTIFICATION);

        // Get all the contactList where vatIdentification equals to UPDATED_VAT_IDENTIFICATION
        defaultContactShouldNotBeFound("vatIdentification.in=" + UPDATED_VAT_IDENTIFICATION);
    }

    @Test
    @Transactional
    void getAllContactsByVatIdentificationIsNullOrNotNull() throws Exception {
        // Initialize the database
        contactRepository.saveAndFlush(contact);

        // Get all the contactList where vatIdentification is not null
        defaultContactShouldBeFound("vatIdentification.specified=true");

        // Get all the contactList where vatIdentification is null
        defaultContactShouldNotBeFound("vatIdentification.specified=false");
    }

    @Test
    @Transactional
    void getAllContactsByVatIdentificationContainsSomething() throws Exception {
        // Initialize the database
        contactRepository.saveAndFlush(contact);

        // Get all the contactList where vatIdentification contains DEFAULT_VAT_IDENTIFICATION
        defaultContactShouldBeFound("vatIdentification.contains=" + DEFAULT_VAT_IDENTIFICATION);

        // Get all the contactList where vatIdentification contains UPDATED_VAT_IDENTIFICATION
        defaultContactShouldNotBeFound("vatIdentification.contains=" + UPDATED_VAT_IDENTIFICATION);
    }

    @Test
    @Transactional
    void getAllContactsByVatIdentificationNotContainsSomething() throws Exception {
        // Initialize the database
        contactRepository.saveAndFlush(contact);

        // Get all the contactList where vatIdentification does not contain DEFAULT_VAT_IDENTIFICATION
        defaultContactShouldNotBeFound("vatIdentification.doesNotContain=" + DEFAULT_VAT_IDENTIFICATION);

        // Get all the contactList where vatIdentification does not contain UPDATED_VAT_IDENTIFICATION
        defaultContactShouldBeFound("vatIdentification.doesNotContain=" + UPDATED_VAT_IDENTIFICATION);
    }

    @Test
    @Transactional
    void getAllContactsByVatRateIsEqualToSomething() throws Exception {
        // Initialize the database
        contactRepository.saveAndFlush(contact);

        // Get all the contactList where vatRate equals to DEFAULT_VAT_RATE
        defaultContactShouldBeFound("vatRate.equals=" + DEFAULT_VAT_RATE);

        // Get all the contactList where vatRate equals to UPDATED_VAT_RATE
        defaultContactShouldNotBeFound("vatRate.equals=" + UPDATED_VAT_RATE);
    }

    @Test
    @Transactional
    void getAllContactsByVatRateIsNotEqualToSomething() throws Exception {
        // Initialize the database
        contactRepository.saveAndFlush(contact);

        // Get all the contactList where vatRate not equals to DEFAULT_VAT_RATE
        defaultContactShouldNotBeFound("vatRate.notEquals=" + DEFAULT_VAT_RATE);

        // Get all the contactList where vatRate not equals to UPDATED_VAT_RATE
        defaultContactShouldBeFound("vatRate.notEquals=" + UPDATED_VAT_RATE);
    }

    @Test
    @Transactional
    void getAllContactsByVatRateIsInShouldWork() throws Exception {
        // Initialize the database
        contactRepository.saveAndFlush(contact);

        // Get all the contactList where vatRate in DEFAULT_VAT_RATE or UPDATED_VAT_RATE
        defaultContactShouldBeFound("vatRate.in=" + DEFAULT_VAT_RATE + "," + UPDATED_VAT_RATE);

        // Get all the contactList where vatRate equals to UPDATED_VAT_RATE
        defaultContactShouldNotBeFound("vatRate.in=" + UPDATED_VAT_RATE);
    }

    @Test
    @Transactional
    void getAllContactsByVatRateIsNullOrNotNull() throws Exception {
        // Initialize the database
        contactRepository.saveAndFlush(contact);

        // Get all the contactList where vatRate is not null
        defaultContactShouldBeFound("vatRate.specified=true");

        // Get all the contactList where vatRate is null
        defaultContactShouldNotBeFound("vatRate.specified=false");
    }

    @Test
    @Transactional
    void getAllContactsByVatRateIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        contactRepository.saveAndFlush(contact);

        // Get all the contactList where vatRate is greater than or equal to DEFAULT_VAT_RATE
        defaultContactShouldBeFound("vatRate.greaterThanOrEqual=" + DEFAULT_VAT_RATE);

        // Get all the contactList where vatRate is greater than or equal to UPDATED_VAT_RATE
        defaultContactShouldNotBeFound("vatRate.greaterThanOrEqual=" + UPDATED_VAT_RATE);
    }

    @Test
    @Transactional
    void getAllContactsByVatRateIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        contactRepository.saveAndFlush(contact);

        // Get all the contactList where vatRate is less than or equal to DEFAULT_VAT_RATE
        defaultContactShouldBeFound("vatRate.lessThanOrEqual=" + DEFAULT_VAT_RATE);

        // Get all the contactList where vatRate is less than or equal to SMALLER_VAT_RATE
        defaultContactShouldNotBeFound("vatRate.lessThanOrEqual=" + SMALLER_VAT_RATE);
    }

    @Test
    @Transactional
    void getAllContactsByVatRateIsLessThanSomething() throws Exception {
        // Initialize the database
        contactRepository.saveAndFlush(contact);

        // Get all the contactList where vatRate is less than DEFAULT_VAT_RATE
        defaultContactShouldNotBeFound("vatRate.lessThan=" + DEFAULT_VAT_RATE);

        // Get all the contactList where vatRate is less than UPDATED_VAT_RATE
        defaultContactShouldBeFound("vatRate.lessThan=" + UPDATED_VAT_RATE);
    }

    @Test
    @Transactional
    void getAllContactsByVatRateIsGreaterThanSomething() throws Exception {
        // Initialize the database
        contactRepository.saveAndFlush(contact);

        // Get all the contactList where vatRate is greater than DEFAULT_VAT_RATE
        defaultContactShouldNotBeFound("vatRate.greaterThan=" + DEFAULT_VAT_RATE);

        // Get all the contactList where vatRate is greater than SMALLER_VAT_RATE
        defaultContactShouldBeFound("vatRate.greaterThan=" + SMALLER_VAT_RATE);
    }

    @Test
    @Transactional
    void getAllContactsByDiscountRateIsEqualToSomething() throws Exception {
        // Initialize the database
        contactRepository.saveAndFlush(contact);

        // Get all the contactList where discountRate equals to DEFAULT_DISCOUNT_RATE
        defaultContactShouldBeFound("discountRate.equals=" + DEFAULT_DISCOUNT_RATE);

        // Get all the contactList where discountRate equals to UPDATED_DISCOUNT_RATE
        defaultContactShouldNotBeFound("discountRate.equals=" + UPDATED_DISCOUNT_RATE);
    }

    @Test
    @Transactional
    void getAllContactsByDiscountRateIsNotEqualToSomething() throws Exception {
        // Initialize the database
        contactRepository.saveAndFlush(contact);

        // Get all the contactList where discountRate not equals to DEFAULT_DISCOUNT_RATE
        defaultContactShouldNotBeFound("discountRate.notEquals=" + DEFAULT_DISCOUNT_RATE);

        // Get all the contactList where discountRate not equals to UPDATED_DISCOUNT_RATE
        defaultContactShouldBeFound("discountRate.notEquals=" + UPDATED_DISCOUNT_RATE);
    }

    @Test
    @Transactional
    void getAllContactsByDiscountRateIsInShouldWork() throws Exception {
        // Initialize the database
        contactRepository.saveAndFlush(contact);

        // Get all the contactList where discountRate in DEFAULT_DISCOUNT_RATE or UPDATED_DISCOUNT_RATE
        defaultContactShouldBeFound("discountRate.in=" + DEFAULT_DISCOUNT_RATE + "," + UPDATED_DISCOUNT_RATE);

        // Get all the contactList where discountRate equals to UPDATED_DISCOUNT_RATE
        defaultContactShouldNotBeFound("discountRate.in=" + UPDATED_DISCOUNT_RATE);
    }

    @Test
    @Transactional
    void getAllContactsByDiscountRateIsNullOrNotNull() throws Exception {
        // Initialize the database
        contactRepository.saveAndFlush(contact);

        // Get all the contactList where discountRate is not null
        defaultContactShouldBeFound("discountRate.specified=true");

        // Get all the contactList where discountRate is null
        defaultContactShouldNotBeFound("discountRate.specified=false");
    }

    @Test
    @Transactional
    void getAllContactsByDiscountRateIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        contactRepository.saveAndFlush(contact);

        // Get all the contactList where discountRate is greater than or equal to DEFAULT_DISCOUNT_RATE
        defaultContactShouldBeFound("discountRate.greaterThanOrEqual=" + DEFAULT_DISCOUNT_RATE);

        // Get all the contactList where discountRate is greater than or equal to UPDATED_DISCOUNT_RATE
        defaultContactShouldNotBeFound("discountRate.greaterThanOrEqual=" + UPDATED_DISCOUNT_RATE);
    }

    @Test
    @Transactional
    void getAllContactsByDiscountRateIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        contactRepository.saveAndFlush(contact);

        // Get all the contactList where discountRate is less than or equal to DEFAULT_DISCOUNT_RATE
        defaultContactShouldBeFound("discountRate.lessThanOrEqual=" + DEFAULT_DISCOUNT_RATE);

        // Get all the contactList where discountRate is less than or equal to SMALLER_DISCOUNT_RATE
        defaultContactShouldNotBeFound("discountRate.lessThanOrEqual=" + SMALLER_DISCOUNT_RATE);
    }

    @Test
    @Transactional
    void getAllContactsByDiscountRateIsLessThanSomething() throws Exception {
        // Initialize the database
        contactRepository.saveAndFlush(contact);

        // Get all the contactList where discountRate is less than DEFAULT_DISCOUNT_RATE
        defaultContactShouldNotBeFound("discountRate.lessThan=" + DEFAULT_DISCOUNT_RATE);

        // Get all the contactList where discountRate is less than UPDATED_DISCOUNT_RATE
        defaultContactShouldBeFound("discountRate.lessThan=" + UPDATED_DISCOUNT_RATE);
    }

    @Test
    @Transactional
    void getAllContactsByDiscountRateIsGreaterThanSomething() throws Exception {
        // Initialize the database
        contactRepository.saveAndFlush(contact);

        // Get all the contactList where discountRate is greater than DEFAULT_DISCOUNT_RATE
        defaultContactShouldNotBeFound("discountRate.greaterThan=" + DEFAULT_DISCOUNT_RATE);

        // Get all the contactList where discountRate is greater than SMALLER_DISCOUNT_RATE
        defaultContactShouldBeFound("discountRate.greaterThan=" + SMALLER_DISCOUNT_RATE);
    }

    @Test
    @Transactional
    void getAllContactsByDiscountTypeIsEqualToSomething() throws Exception {
        // Initialize the database
        contactRepository.saveAndFlush(contact);

        // Get all the contactList where discountType equals to DEFAULT_DISCOUNT_TYPE
        defaultContactShouldBeFound("discountType.equals=" + DEFAULT_DISCOUNT_TYPE);

        // Get all the contactList where discountType equals to UPDATED_DISCOUNT_TYPE
        defaultContactShouldNotBeFound("discountType.equals=" + UPDATED_DISCOUNT_TYPE);
    }

    @Test
    @Transactional
    void getAllContactsByDiscountTypeIsNotEqualToSomething() throws Exception {
        // Initialize the database
        contactRepository.saveAndFlush(contact);

        // Get all the contactList where discountType not equals to DEFAULT_DISCOUNT_TYPE
        defaultContactShouldNotBeFound("discountType.notEquals=" + DEFAULT_DISCOUNT_TYPE);

        // Get all the contactList where discountType not equals to UPDATED_DISCOUNT_TYPE
        defaultContactShouldBeFound("discountType.notEquals=" + UPDATED_DISCOUNT_TYPE);
    }

    @Test
    @Transactional
    void getAllContactsByDiscountTypeIsInShouldWork() throws Exception {
        // Initialize the database
        contactRepository.saveAndFlush(contact);

        // Get all the contactList where discountType in DEFAULT_DISCOUNT_TYPE or UPDATED_DISCOUNT_TYPE
        defaultContactShouldBeFound("discountType.in=" + DEFAULT_DISCOUNT_TYPE + "," + UPDATED_DISCOUNT_TYPE);

        // Get all the contactList where discountType equals to UPDATED_DISCOUNT_TYPE
        defaultContactShouldNotBeFound("discountType.in=" + UPDATED_DISCOUNT_TYPE);
    }

    @Test
    @Transactional
    void getAllContactsByDiscountTypeIsNullOrNotNull() throws Exception {
        // Initialize the database
        contactRepository.saveAndFlush(contact);

        // Get all the contactList where discountType is not null
        defaultContactShouldBeFound("discountType.specified=true");

        // Get all the contactList where discountType is null
        defaultContactShouldNotBeFound("discountType.specified=false");
    }

    @Test
    @Transactional
    void getAllContactsByPaymentGraceIsEqualToSomething() throws Exception {
        // Initialize the database
        contactRepository.saveAndFlush(contact);

        // Get all the contactList where paymentGrace equals to DEFAULT_PAYMENT_GRACE
        defaultContactShouldBeFound("paymentGrace.equals=" + DEFAULT_PAYMENT_GRACE);

        // Get all the contactList where paymentGrace equals to UPDATED_PAYMENT_GRACE
        defaultContactShouldNotBeFound("paymentGrace.equals=" + UPDATED_PAYMENT_GRACE);
    }

    @Test
    @Transactional
    void getAllContactsByPaymentGraceIsNotEqualToSomething() throws Exception {
        // Initialize the database
        contactRepository.saveAndFlush(contact);

        // Get all the contactList where paymentGrace not equals to DEFAULT_PAYMENT_GRACE
        defaultContactShouldNotBeFound("paymentGrace.notEquals=" + DEFAULT_PAYMENT_GRACE);

        // Get all the contactList where paymentGrace not equals to UPDATED_PAYMENT_GRACE
        defaultContactShouldBeFound("paymentGrace.notEquals=" + UPDATED_PAYMENT_GRACE);
    }

    @Test
    @Transactional
    void getAllContactsByPaymentGraceIsInShouldWork() throws Exception {
        // Initialize the database
        contactRepository.saveAndFlush(contact);

        // Get all the contactList where paymentGrace in DEFAULT_PAYMENT_GRACE or UPDATED_PAYMENT_GRACE
        defaultContactShouldBeFound("paymentGrace.in=" + DEFAULT_PAYMENT_GRACE + "," + UPDATED_PAYMENT_GRACE);

        // Get all the contactList where paymentGrace equals to UPDATED_PAYMENT_GRACE
        defaultContactShouldNotBeFound("paymentGrace.in=" + UPDATED_PAYMENT_GRACE);
    }

    @Test
    @Transactional
    void getAllContactsByPaymentGraceIsNullOrNotNull() throws Exception {
        // Initialize the database
        contactRepository.saveAndFlush(contact);

        // Get all the contactList where paymentGrace is not null
        defaultContactShouldBeFound("paymentGrace.specified=true");

        // Get all the contactList where paymentGrace is null
        defaultContactShouldNotBeFound("paymentGrace.specified=false");
    }

    @Test
    @Transactional
    void getAllContactsByPaymentGraceIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        contactRepository.saveAndFlush(contact);

        // Get all the contactList where paymentGrace is greater than or equal to DEFAULT_PAYMENT_GRACE
        defaultContactShouldBeFound("paymentGrace.greaterThanOrEqual=" + DEFAULT_PAYMENT_GRACE);

        // Get all the contactList where paymentGrace is greater than or equal to UPDATED_PAYMENT_GRACE
        defaultContactShouldNotBeFound("paymentGrace.greaterThanOrEqual=" + UPDATED_PAYMENT_GRACE);
    }

    @Test
    @Transactional
    void getAllContactsByPaymentGraceIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        contactRepository.saveAndFlush(contact);

        // Get all the contactList where paymentGrace is less than or equal to DEFAULT_PAYMENT_GRACE
        defaultContactShouldBeFound("paymentGrace.lessThanOrEqual=" + DEFAULT_PAYMENT_GRACE);

        // Get all the contactList where paymentGrace is less than or equal to SMALLER_PAYMENT_GRACE
        defaultContactShouldNotBeFound("paymentGrace.lessThanOrEqual=" + SMALLER_PAYMENT_GRACE);
    }

    @Test
    @Transactional
    void getAllContactsByPaymentGraceIsLessThanSomething() throws Exception {
        // Initialize the database
        contactRepository.saveAndFlush(contact);

        // Get all the contactList where paymentGrace is less than DEFAULT_PAYMENT_GRACE
        defaultContactShouldNotBeFound("paymentGrace.lessThan=" + DEFAULT_PAYMENT_GRACE);

        // Get all the contactList where paymentGrace is less than UPDATED_PAYMENT_GRACE
        defaultContactShouldBeFound("paymentGrace.lessThan=" + UPDATED_PAYMENT_GRACE);
    }

    @Test
    @Transactional
    void getAllContactsByPaymentGraceIsGreaterThanSomething() throws Exception {
        // Initialize the database
        contactRepository.saveAndFlush(contact);

        // Get all the contactList where paymentGrace is greater than DEFAULT_PAYMENT_GRACE
        defaultContactShouldNotBeFound("paymentGrace.greaterThan=" + DEFAULT_PAYMENT_GRACE);

        // Get all the contactList where paymentGrace is greater than SMALLER_PAYMENT_GRACE
        defaultContactShouldBeFound("paymentGrace.greaterThan=" + SMALLER_PAYMENT_GRACE);
    }

    @Test
    @Transactional
    void getAllContactsByHourlyRateIsEqualToSomething() throws Exception {
        // Initialize the database
        contactRepository.saveAndFlush(contact);

        // Get all the contactList where hourlyRate equals to DEFAULT_HOURLY_RATE
        defaultContactShouldBeFound("hourlyRate.equals=" + DEFAULT_HOURLY_RATE);

        // Get all the contactList where hourlyRate equals to UPDATED_HOURLY_RATE
        defaultContactShouldNotBeFound("hourlyRate.equals=" + UPDATED_HOURLY_RATE);
    }

    @Test
    @Transactional
    void getAllContactsByHourlyRateIsNotEqualToSomething() throws Exception {
        // Initialize the database
        contactRepository.saveAndFlush(contact);

        // Get all the contactList where hourlyRate not equals to DEFAULT_HOURLY_RATE
        defaultContactShouldNotBeFound("hourlyRate.notEquals=" + DEFAULT_HOURLY_RATE);

        // Get all the contactList where hourlyRate not equals to UPDATED_HOURLY_RATE
        defaultContactShouldBeFound("hourlyRate.notEquals=" + UPDATED_HOURLY_RATE);
    }

    @Test
    @Transactional
    void getAllContactsByHourlyRateIsInShouldWork() throws Exception {
        // Initialize the database
        contactRepository.saveAndFlush(contact);

        // Get all the contactList where hourlyRate in DEFAULT_HOURLY_RATE or UPDATED_HOURLY_RATE
        defaultContactShouldBeFound("hourlyRate.in=" + DEFAULT_HOURLY_RATE + "," + UPDATED_HOURLY_RATE);

        // Get all the contactList where hourlyRate equals to UPDATED_HOURLY_RATE
        defaultContactShouldNotBeFound("hourlyRate.in=" + UPDATED_HOURLY_RATE);
    }

    @Test
    @Transactional
    void getAllContactsByHourlyRateIsNullOrNotNull() throws Exception {
        // Initialize the database
        contactRepository.saveAndFlush(contact);

        // Get all the contactList where hourlyRate is not null
        defaultContactShouldBeFound("hourlyRate.specified=true");

        // Get all the contactList where hourlyRate is null
        defaultContactShouldNotBeFound("hourlyRate.specified=false");
    }

    @Test
    @Transactional
    void getAllContactsByHourlyRateIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        contactRepository.saveAndFlush(contact);

        // Get all the contactList where hourlyRate is greater than or equal to DEFAULT_HOURLY_RATE
        defaultContactShouldBeFound("hourlyRate.greaterThanOrEqual=" + DEFAULT_HOURLY_RATE);

        // Get all the contactList where hourlyRate is greater than or equal to UPDATED_HOURLY_RATE
        defaultContactShouldNotBeFound("hourlyRate.greaterThanOrEqual=" + UPDATED_HOURLY_RATE);
    }

    @Test
    @Transactional
    void getAllContactsByHourlyRateIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        contactRepository.saveAndFlush(contact);

        // Get all the contactList where hourlyRate is less than or equal to DEFAULT_HOURLY_RATE
        defaultContactShouldBeFound("hourlyRate.lessThanOrEqual=" + DEFAULT_HOURLY_RATE);

        // Get all the contactList where hourlyRate is less than or equal to SMALLER_HOURLY_RATE
        defaultContactShouldNotBeFound("hourlyRate.lessThanOrEqual=" + SMALLER_HOURLY_RATE);
    }

    @Test
    @Transactional
    void getAllContactsByHourlyRateIsLessThanSomething() throws Exception {
        // Initialize the database
        contactRepository.saveAndFlush(contact);

        // Get all the contactList where hourlyRate is less than DEFAULT_HOURLY_RATE
        defaultContactShouldNotBeFound("hourlyRate.lessThan=" + DEFAULT_HOURLY_RATE);

        // Get all the contactList where hourlyRate is less than UPDATED_HOURLY_RATE
        defaultContactShouldBeFound("hourlyRate.lessThan=" + UPDATED_HOURLY_RATE);
    }

    @Test
    @Transactional
    void getAllContactsByHourlyRateIsGreaterThanSomething() throws Exception {
        // Initialize the database
        contactRepository.saveAndFlush(contact);

        // Get all the contactList where hourlyRate is greater than DEFAULT_HOURLY_RATE
        defaultContactShouldNotBeFound("hourlyRate.greaterThan=" + DEFAULT_HOURLY_RATE);

        // Get all the contactList where hourlyRate is greater than SMALLER_HOURLY_RATE
        defaultContactShouldBeFound("hourlyRate.greaterThan=" + SMALLER_HOURLY_RATE);
    }

    @Test
    @Transactional
    void getAllContactsByCreatedIsEqualToSomething() throws Exception {
        // Initialize the database
        contactRepository.saveAndFlush(contact);

        // Get all the contactList where created equals to DEFAULT_CREATED
        defaultContactShouldBeFound("created.equals=" + DEFAULT_CREATED);

        // Get all the contactList where created equals to UPDATED_CREATED
        defaultContactShouldNotBeFound("created.equals=" + UPDATED_CREATED);
    }

    @Test
    @Transactional
    void getAllContactsByCreatedIsNotEqualToSomething() throws Exception {
        // Initialize the database
        contactRepository.saveAndFlush(contact);

        // Get all the contactList where created not equals to DEFAULT_CREATED
        defaultContactShouldNotBeFound("created.notEquals=" + DEFAULT_CREATED);

        // Get all the contactList where created not equals to UPDATED_CREATED
        defaultContactShouldBeFound("created.notEquals=" + UPDATED_CREATED);
    }

    @Test
    @Transactional
    void getAllContactsByCreatedIsInShouldWork() throws Exception {
        // Initialize the database
        contactRepository.saveAndFlush(contact);

        // Get all the contactList where created in DEFAULT_CREATED or UPDATED_CREATED
        defaultContactShouldBeFound("created.in=" + DEFAULT_CREATED + "," + UPDATED_CREATED);

        // Get all the contactList where created equals to UPDATED_CREATED
        defaultContactShouldNotBeFound("created.in=" + UPDATED_CREATED);
    }

    @Test
    @Transactional
    void getAllContactsByCreatedIsNullOrNotNull() throws Exception {
        // Initialize the database
        contactRepository.saveAndFlush(contact);

        // Get all the contactList where created is not null
        defaultContactShouldBeFound("created.specified=true");

        // Get all the contactList where created is null
        defaultContactShouldNotBeFound("created.specified=false");
    }

    @Test
    @Transactional
    void getAllContactsByCreatedIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        contactRepository.saveAndFlush(contact);

        // Get all the contactList where created is greater than or equal to DEFAULT_CREATED
        defaultContactShouldBeFound("created.greaterThanOrEqual=" + DEFAULT_CREATED);

        // Get all the contactList where created is greater than or equal to UPDATED_CREATED
        defaultContactShouldNotBeFound("created.greaterThanOrEqual=" + UPDATED_CREATED);
    }

    @Test
    @Transactional
    void getAllContactsByCreatedIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        contactRepository.saveAndFlush(contact);

        // Get all the contactList where created is less than or equal to DEFAULT_CREATED
        defaultContactShouldBeFound("created.lessThanOrEqual=" + DEFAULT_CREATED);

        // Get all the contactList where created is less than or equal to SMALLER_CREATED
        defaultContactShouldNotBeFound("created.lessThanOrEqual=" + SMALLER_CREATED);
    }

    @Test
    @Transactional
    void getAllContactsByCreatedIsLessThanSomething() throws Exception {
        // Initialize the database
        contactRepository.saveAndFlush(contact);

        // Get all the contactList where created is less than DEFAULT_CREATED
        defaultContactShouldNotBeFound("created.lessThan=" + DEFAULT_CREATED);

        // Get all the contactList where created is less than UPDATED_CREATED
        defaultContactShouldBeFound("created.lessThan=" + UPDATED_CREATED);
    }

    @Test
    @Transactional
    void getAllContactsByCreatedIsGreaterThanSomething() throws Exception {
        // Initialize the database
        contactRepository.saveAndFlush(contact);

        // Get all the contactList where created is greater than DEFAULT_CREATED
        defaultContactShouldNotBeFound("created.greaterThan=" + DEFAULT_CREATED);

        // Get all the contactList where created is greater than SMALLER_CREATED
        defaultContactShouldBeFound("created.greaterThan=" + SMALLER_CREATED);
    }

    @Test
    @Transactional
    void getAllContactsByMainAddressIdIsEqualToSomething() throws Exception {
        // Initialize the database
        contactRepository.saveAndFlush(contact);

        // Get all the contactList where mainAddressId equals to DEFAULT_MAIN_ADDRESS_ID
        defaultContactShouldBeFound("mainAddressId.equals=" + DEFAULT_MAIN_ADDRESS_ID);

        // Get all the contactList where mainAddressId equals to UPDATED_MAIN_ADDRESS_ID
        defaultContactShouldNotBeFound("mainAddressId.equals=" + UPDATED_MAIN_ADDRESS_ID);
    }

    @Test
    @Transactional
    void getAllContactsByMainAddressIdIsNotEqualToSomething() throws Exception {
        // Initialize the database
        contactRepository.saveAndFlush(contact);

        // Get all the contactList where mainAddressId not equals to DEFAULT_MAIN_ADDRESS_ID
        defaultContactShouldNotBeFound("mainAddressId.notEquals=" + DEFAULT_MAIN_ADDRESS_ID);

        // Get all the contactList where mainAddressId not equals to UPDATED_MAIN_ADDRESS_ID
        defaultContactShouldBeFound("mainAddressId.notEquals=" + UPDATED_MAIN_ADDRESS_ID);
    }

    @Test
    @Transactional
    void getAllContactsByMainAddressIdIsInShouldWork() throws Exception {
        // Initialize the database
        contactRepository.saveAndFlush(contact);

        // Get all the contactList where mainAddressId in DEFAULT_MAIN_ADDRESS_ID or UPDATED_MAIN_ADDRESS_ID
        defaultContactShouldBeFound("mainAddressId.in=" + DEFAULT_MAIN_ADDRESS_ID + "," + UPDATED_MAIN_ADDRESS_ID);

        // Get all the contactList where mainAddressId equals to UPDATED_MAIN_ADDRESS_ID
        defaultContactShouldNotBeFound("mainAddressId.in=" + UPDATED_MAIN_ADDRESS_ID);
    }

    @Test
    @Transactional
    void getAllContactsByMainAddressIdIsNullOrNotNull() throws Exception {
        // Initialize the database
        contactRepository.saveAndFlush(contact);

        // Get all the contactList where mainAddressId is not null
        defaultContactShouldBeFound("mainAddressId.specified=true");

        // Get all the contactList where mainAddressId is null
        defaultContactShouldNotBeFound("mainAddressId.specified=false");
    }

    @Test
    @Transactional
    void getAllContactsByMainAddressIdIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        contactRepository.saveAndFlush(contact);

        // Get all the contactList where mainAddressId is greater than or equal to DEFAULT_MAIN_ADDRESS_ID
        defaultContactShouldBeFound("mainAddressId.greaterThanOrEqual=" + DEFAULT_MAIN_ADDRESS_ID);

        // Get all the contactList where mainAddressId is greater than or equal to UPDATED_MAIN_ADDRESS_ID
        defaultContactShouldNotBeFound("mainAddressId.greaterThanOrEqual=" + UPDATED_MAIN_ADDRESS_ID);
    }

    @Test
    @Transactional
    void getAllContactsByMainAddressIdIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        contactRepository.saveAndFlush(contact);

        // Get all the contactList where mainAddressId is less than or equal to DEFAULT_MAIN_ADDRESS_ID
        defaultContactShouldBeFound("mainAddressId.lessThanOrEqual=" + DEFAULT_MAIN_ADDRESS_ID);

        // Get all the contactList where mainAddressId is less than or equal to SMALLER_MAIN_ADDRESS_ID
        defaultContactShouldNotBeFound("mainAddressId.lessThanOrEqual=" + SMALLER_MAIN_ADDRESS_ID);
    }

    @Test
    @Transactional
    void getAllContactsByMainAddressIdIsLessThanSomething() throws Exception {
        // Initialize the database
        contactRepository.saveAndFlush(contact);

        // Get all the contactList where mainAddressId is less than DEFAULT_MAIN_ADDRESS_ID
        defaultContactShouldNotBeFound("mainAddressId.lessThan=" + DEFAULT_MAIN_ADDRESS_ID);

        // Get all the contactList where mainAddressId is less than UPDATED_MAIN_ADDRESS_ID
        defaultContactShouldBeFound("mainAddressId.lessThan=" + UPDATED_MAIN_ADDRESS_ID);
    }

    @Test
    @Transactional
    void getAllContactsByMainAddressIdIsGreaterThanSomething() throws Exception {
        // Initialize the database
        contactRepository.saveAndFlush(contact);

        // Get all the contactList where mainAddressId is greater than DEFAULT_MAIN_ADDRESS_ID
        defaultContactShouldNotBeFound("mainAddressId.greaterThan=" + DEFAULT_MAIN_ADDRESS_ID);

        // Get all the contactList where mainAddressId is greater than SMALLER_MAIN_ADDRESS_ID
        defaultContactShouldBeFound("mainAddressId.greaterThan=" + SMALLER_MAIN_ADDRESS_ID);
    }

    @Test
    @Transactional
    void getAllContactsByBirthDateIsEqualToSomething() throws Exception {
        // Initialize the database
        contactRepository.saveAndFlush(contact);

        // Get all the contactList where birthDate equals to DEFAULT_BIRTH_DATE
        defaultContactShouldBeFound("birthDate.equals=" + DEFAULT_BIRTH_DATE);

        // Get all the contactList where birthDate equals to UPDATED_BIRTH_DATE
        defaultContactShouldNotBeFound("birthDate.equals=" + UPDATED_BIRTH_DATE);
    }

    @Test
    @Transactional
    void getAllContactsByBirthDateIsNotEqualToSomething() throws Exception {
        // Initialize the database
        contactRepository.saveAndFlush(contact);

        // Get all the contactList where birthDate not equals to DEFAULT_BIRTH_DATE
        defaultContactShouldNotBeFound("birthDate.notEquals=" + DEFAULT_BIRTH_DATE);

        // Get all the contactList where birthDate not equals to UPDATED_BIRTH_DATE
        defaultContactShouldBeFound("birthDate.notEquals=" + UPDATED_BIRTH_DATE);
    }

    @Test
    @Transactional
    void getAllContactsByBirthDateIsInShouldWork() throws Exception {
        // Initialize the database
        contactRepository.saveAndFlush(contact);

        // Get all the contactList where birthDate in DEFAULT_BIRTH_DATE or UPDATED_BIRTH_DATE
        defaultContactShouldBeFound("birthDate.in=" + DEFAULT_BIRTH_DATE + "," + UPDATED_BIRTH_DATE);

        // Get all the contactList where birthDate equals to UPDATED_BIRTH_DATE
        defaultContactShouldNotBeFound("birthDate.in=" + UPDATED_BIRTH_DATE);
    }

    @Test
    @Transactional
    void getAllContactsByBirthDateIsNullOrNotNull() throws Exception {
        // Initialize the database
        contactRepository.saveAndFlush(contact);

        // Get all the contactList where birthDate is not null
        defaultContactShouldBeFound("birthDate.specified=true");

        // Get all the contactList where birthDate is null
        defaultContactShouldNotBeFound("birthDate.specified=false");
    }

    @Test
    @Transactional
    void getAllContactsByBirthDateIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        contactRepository.saveAndFlush(contact);

        // Get all the contactList where birthDate is greater than or equal to DEFAULT_BIRTH_DATE
        defaultContactShouldBeFound("birthDate.greaterThanOrEqual=" + DEFAULT_BIRTH_DATE);

        // Get all the contactList where birthDate is greater than or equal to UPDATED_BIRTH_DATE
        defaultContactShouldNotBeFound("birthDate.greaterThanOrEqual=" + UPDATED_BIRTH_DATE);
    }

    @Test
    @Transactional
    void getAllContactsByBirthDateIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        contactRepository.saveAndFlush(contact);

        // Get all the contactList where birthDate is less than or equal to DEFAULT_BIRTH_DATE
        defaultContactShouldBeFound("birthDate.lessThanOrEqual=" + DEFAULT_BIRTH_DATE);

        // Get all the contactList where birthDate is less than or equal to SMALLER_BIRTH_DATE
        defaultContactShouldNotBeFound("birthDate.lessThanOrEqual=" + SMALLER_BIRTH_DATE);
    }

    @Test
    @Transactional
    void getAllContactsByBirthDateIsLessThanSomething() throws Exception {
        // Initialize the database
        contactRepository.saveAndFlush(contact);

        // Get all the contactList where birthDate is less than DEFAULT_BIRTH_DATE
        defaultContactShouldNotBeFound("birthDate.lessThan=" + DEFAULT_BIRTH_DATE);

        // Get all the contactList where birthDate is less than UPDATED_BIRTH_DATE
        defaultContactShouldBeFound("birthDate.lessThan=" + UPDATED_BIRTH_DATE);
    }

    @Test
    @Transactional
    void getAllContactsByBirthDateIsGreaterThanSomething() throws Exception {
        // Initialize the database
        contactRepository.saveAndFlush(contact);

        // Get all the contactList where birthDate is greater than DEFAULT_BIRTH_DATE
        defaultContactShouldNotBeFound("birthDate.greaterThan=" + DEFAULT_BIRTH_DATE);

        // Get all the contactList where birthDate is greater than SMALLER_BIRTH_DATE
        defaultContactShouldBeFound("birthDate.greaterThan=" + SMALLER_BIRTH_DATE);
    }

    @Test
    @Transactional
    void getAllContactsByBirthPlaceIsEqualToSomething() throws Exception {
        // Initialize the database
        contactRepository.saveAndFlush(contact);

        // Get all the contactList where birthPlace equals to DEFAULT_BIRTH_PLACE
        defaultContactShouldBeFound("birthPlace.equals=" + DEFAULT_BIRTH_PLACE);

        // Get all the contactList where birthPlace equals to UPDATED_BIRTH_PLACE
        defaultContactShouldNotBeFound("birthPlace.equals=" + UPDATED_BIRTH_PLACE);
    }

    @Test
    @Transactional
    void getAllContactsByBirthPlaceIsNotEqualToSomething() throws Exception {
        // Initialize the database
        contactRepository.saveAndFlush(contact);

        // Get all the contactList where birthPlace not equals to DEFAULT_BIRTH_PLACE
        defaultContactShouldNotBeFound("birthPlace.notEquals=" + DEFAULT_BIRTH_PLACE);

        // Get all the contactList where birthPlace not equals to UPDATED_BIRTH_PLACE
        defaultContactShouldBeFound("birthPlace.notEquals=" + UPDATED_BIRTH_PLACE);
    }

    @Test
    @Transactional
    void getAllContactsByBirthPlaceIsInShouldWork() throws Exception {
        // Initialize the database
        contactRepository.saveAndFlush(contact);

        // Get all the contactList where birthPlace in DEFAULT_BIRTH_PLACE or UPDATED_BIRTH_PLACE
        defaultContactShouldBeFound("birthPlace.in=" + DEFAULT_BIRTH_PLACE + "," + UPDATED_BIRTH_PLACE);

        // Get all the contactList where birthPlace equals to UPDATED_BIRTH_PLACE
        defaultContactShouldNotBeFound("birthPlace.in=" + UPDATED_BIRTH_PLACE);
    }

    @Test
    @Transactional
    void getAllContactsByBirthPlaceIsNullOrNotNull() throws Exception {
        // Initialize the database
        contactRepository.saveAndFlush(contact);

        // Get all the contactList where birthPlace is not null
        defaultContactShouldBeFound("birthPlace.specified=true");

        // Get all the contactList where birthPlace is null
        defaultContactShouldNotBeFound("birthPlace.specified=false");
    }

    @Test
    @Transactional
    void getAllContactsByBirthPlaceContainsSomething() throws Exception {
        // Initialize the database
        contactRepository.saveAndFlush(contact);

        // Get all the contactList where birthPlace contains DEFAULT_BIRTH_PLACE
        defaultContactShouldBeFound("birthPlace.contains=" + DEFAULT_BIRTH_PLACE);

        // Get all the contactList where birthPlace contains UPDATED_BIRTH_PLACE
        defaultContactShouldNotBeFound("birthPlace.contains=" + UPDATED_BIRTH_PLACE);
    }

    @Test
    @Transactional
    void getAllContactsByBirthPlaceNotContainsSomething() throws Exception {
        // Initialize the database
        contactRepository.saveAndFlush(contact);

        // Get all the contactList where birthPlace does not contain DEFAULT_BIRTH_PLACE
        defaultContactShouldNotBeFound("birthPlace.doesNotContain=" + DEFAULT_BIRTH_PLACE);

        // Get all the contactList where birthPlace does not contain UPDATED_BIRTH_PLACE
        defaultContactShouldBeFound("birthPlace.doesNotContain=" + UPDATED_BIRTH_PLACE);
    }

    @Test
    @Transactional
    void getAllContactsByPlaceOfOriginIsEqualToSomething() throws Exception {
        // Initialize the database
        contactRepository.saveAndFlush(contact);

        // Get all the contactList where placeOfOrigin equals to DEFAULT_PLACE_OF_ORIGIN
        defaultContactShouldBeFound("placeOfOrigin.equals=" + DEFAULT_PLACE_OF_ORIGIN);

        // Get all the contactList where placeOfOrigin equals to UPDATED_PLACE_OF_ORIGIN
        defaultContactShouldNotBeFound("placeOfOrigin.equals=" + UPDATED_PLACE_OF_ORIGIN);
    }

    @Test
    @Transactional
    void getAllContactsByPlaceOfOriginIsNotEqualToSomething() throws Exception {
        // Initialize the database
        contactRepository.saveAndFlush(contact);

        // Get all the contactList where placeOfOrigin not equals to DEFAULT_PLACE_OF_ORIGIN
        defaultContactShouldNotBeFound("placeOfOrigin.notEquals=" + DEFAULT_PLACE_OF_ORIGIN);

        // Get all the contactList where placeOfOrigin not equals to UPDATED_PLACE_OF_ORIGIN
        defaultContactShouldBeFound("placeOfOrigin.notEquals=" + UPDATED_PLACE_OF_ORIGIN);
    }

    @Test
    @Transactional
    void getAllContactsByPlaceOfOriginIsInShouldWork() throws Exception {
        // Initialize the database
        contactRepository.saveAndFlush(contact);

        // Get all the contactList where placeOfOrigin in DEFAULT_PLACE_OF_ORIGIN or UPDATED_PLACE_OF_ORIGIN
        defaultContactShouldBeFound("placeOfOrigin.in=" + DEFAULT_PLACE_OF_ORIGIN + "," + UPDATED_PLACE_OF_ORIGIN);

        // Get all the contactList where placeOfOrigin equals to UPDATED_PLACE_OF_ORIGIN
        defaultContactShouldNotBeFound("placeOfOrigin.in=" + UPDATED_PLACE_OF_ORIGIN);
    }

    @Test
    @Transactional
    void getAllContactsByPlaceOfOriginIsNullOrNotNull() throws Exception {
        // Initialize the database
        contactRepository.saveAndFlush(contact);

        // Get all the contactList where placeOfOrigin is not null
        defaultContactShouldBeFound("placeOfOrigin.specified=true");

        // Get all the contactList where placeOfOrigin is null
        defaultContactShouldNotBeFound("placeOfOrigin.specified=false");
    }

    @Test
    @Transactional
    void getAllContactsByPlaceOfOriginContainsSomething() throws Exception {
        // Initialize the database
        contactRepository.saveAndFlush(contact);

        // Get all the contactList where placeOfOrigin contains DEFAULT_PLACE_OF_ORIGIN
        defaultContactShouldBeFound("placeOfOrigin.contains=" + DEFAULT_PLACE_OF_ORIGIN);

        // Get all the contactList where placeOfOrigin contains UPDATED_PLACE_OF_ORIGIN
        defaultContactShouldNotBeFound("placeOfOrigin.contains=" + UPDATED_PLACE_OF_ORIGIN);
    }

    @Test
    @Transactional
    void getAllContactsByPlaceOfOriginNotContainsSomething() throws Exception {
        // Initialize the database
        contactRepository.saveAndFlush(contact);

        // Get all the contactList where placeOfOrigin does not contain DEFAULT_PLACE_OF_ORIGIN
        defaultContactShouldNotBeFound("placeOfOrigin.doesNotContain=" + DEFAULT_PLACE_OF_ORIGIN);

        // Get all the contactList where placeOfOrigin does not contain UPDATED_PLACE_OF_ORIGIN
        defaultContactShouldBeFound("placeOfOrigin.doesNotContain=" + UPDATED_PLACE_OF_ORIGIN);
    }

    @Test
    @Transactional
    void getAllContactsByCitizenCountry1IsEqualToSomething() throws Exception {
        // Initialize the database
        contactRepository.saveAndFlush(contact);

        // Get all the contactList where citizenCountry1 equals to DEFAULT_CITIZEN_COUNTRY_1
        defaultContactShouldBeFound("citizenCountry1.equals=" + DEFAULT_CITIZEN_COUNTRY_1);

        // Get all the contactList where citizenCountry1 equals to UPDATED_CITIZEN_COUNTRY_1
        defaultContactShouldNotBeFound("citizenCountry1.equals=" + UPDATED_CITIZEN_COUNTRY_1);
    }

    @Test
    @Transactional
    void getAllContactsByCitizenCountry1IsNotEqualToSomething() throws Exception {
        // Initialize the database
        contactRepository.saveAndFlush(contact);

        // Get all the contactList where citizenCountry1 not equals to DEFAULT_CITIZEN_COUNTRY_1
        defaultContactShouldNotBeFound("citizenCountry1.notEquals=" + DEFAULT_CITIZEN_COUNTRY_1);

        // Get all the contactList where citizenCountry1 not equals to UPDATED_CITIZEN_COUNTRY_1
        defaultContactShouldBeFound("citizenCountry1.notEquals=" + UPDATED_CITIZEN_COUNTRY_1);
    }

    @Test
    @Transactional
    void getAllContactsByCitizenCountry1IsInShouldWork() throws Exception {
        // Initialize the database
        contactRepository.saveAndFlush(contact);

        // Get all the contactList where citizenCountry1 in DEFAULT_CITIZEN_COUNTRY_1 or UPDATED_CITIZEN_COUNTRY_1
        defaultContactShouldBeFound("citizenCountry1.in=" + DEFAULT_CITIZEN_COUNTRY_1 + "," + UPDATED_CITIZEN_COUNTRY_1);

        // Get all the contactList where citizenCountry1 equals to UPDATED_CITIZEN_COUNTRY_1
        defaultContactShouldNotBeFound("citizenCountry1.in=" + UPDATED_CITIZEN_COUNTRY_1);
    }

    @Test
    @Transactional
    void getAllContactsByCitizenCountry1IsNullOrNotNull() throws Exception {
        // Initialize the database
        contactRepository.saveAndFlush(contact);

        // Get all the contactList where citizenCountry1 is not null
        defaultContactShouldBeFound("citizenCountry1.specified=true");

        // Get all the contactList where citizenCountry1 is null
        defaultContactShouldNotBeFound("citizenCountry1.specified=false");
    }

    @Test
    @Transactional
    void getAllContactsByCitizenCountry1ContainsSomething() throws Exception {
        // Initialize the database
        contactRepository.saveAndFlush(contact);

        // Get all the contactList where citizenCountry1 contains DEFAULT_CITIZEN_COUNTRY_1
        defaultContactShouldBeFound("citizenCountry1.contains=" + DEFAULT_CITIZEN_COUNTRY_1);

        // Get all the contactList where citizenCountry1 contains UPDATED_CITIZEN_COUNTRY_1
        defaultContactShouldNotBeFound("citizenCountry1.contains=" + UPDATED_CITIZEN_COUNTRY_1);
    }

    @Test
    @Transactional
    void getAllContactsByCitizenCountry1NotContainsSomething() throws Exception {
        // Initialize the database
        contactRepository.saveAndFlush(contact);

        // Get all the contactList where citizenCountry1 does not contain DEFAULT_CITIZEN_COUNTRY_1
        defaultContactShouldNotBeFound("citizenCountry1.doesNotContain=" + DEFAULT_CITIZEN_COUNTRY_1);

        // Get all the contactList where citizenCountry1 does not contain UPDATED_CITIZEN_COUNTRY_1
        defaultContactShouldBeFound("citizenCountry1.doesNotContain=" + UPDATED_CITIZEN_COUNTRY_1);
    }

    @Test
    @Transactional
    void getAllContactsByCitizenCountry2IsEqualToSomething() throws Exception {
        // Initialize the database
        contactRepository.saveAndFlush(contact);

        // Get all the contactList where citizenCountry2 equals to DEFAULT_CITIZEN_COUNTRY_2
        defaultContactShouldBeFound("citizenCountry2.equals=" + DEFAULT_CITIZEN_COUNTRY_2);

        // Get all the contactList where citizenCountry2 equals to UPDATED_CITIZEN_COUNTRY_2
        defaultContactShouldNotBeFound("citizenCountry2.equals=" + UPDATED_CITIZEN_COUNTRY_2);
    }

    @Test
    @Transactional
    void getAllContactsByCitizenCountry2IsNotEqualToSomething() throws Exception {
        // Initialize the database
        contactRepository.saveAndFlush(contact);

        // Get all the contactList where citizenCountry2 not equals to DEFAULT_CITIZEN_COUNTRY_2
        defaultContactShouldNotBeFound("citizenCountry2.notEquals=" + DEFAULT_CITIZEN_COUNTRY_2);

        // Get all the contactList where citizenCountry2 not equals to UPDATED_CITIZEN_COUNTRY_2
        defaultContactShouldBeFound("citizenCountry2.notEquals=" + UPDATED_CITIZEN_COUNTRY_2);
    }

    @Test
    @Transactional
    void getAllContactsByCitizenCountry2IsInShouldWork() throws Exception {
        // Initialize the database
        contactRepository.saveAndFlush(contact);

        // Get all the contactList where citizenCountry2 in DEFAULT_CITIZEN_COUNTRY_2 or UPDATED_CITIZEN_COUNTRY_2
        defaultContactShouldBeFound("citizenCountry2.in=" + DEFAULT_CITIZEN_COUNTRY_2 + "," + UPDATED_CITIZEN_COUNTRY_2);

        // Get all the contactList where citizenCountry2 equals to UPDATED_CITIZEN_COUNTRY_2
        defaultContactShouldNotBeFound("citizenCountry2.in=" + UPDATED_CITIZEN_COUNTRY_2);
    }

    @Test
    @Transactional
    void getAllContactsByCitizenCountry2IsNullOrNotNull() throws Exception {
        // Initialize the database
        contactRepository.saveAndFlush(contact);

        // Get all the contactList where citizenCountry2 is not null
        defaultContactShouldBeFound("citizenCountry2.specified=true");

        // Get all the contactList where citizenCountry2 is null
        defaultContactShouldNotBeFound("citizenCountry2.specified=false");
    }

    @Test
    @Transactional
    void getAllContactsByCitizenCountry2ContainsSomething() throws Exception {
        // Initialize the database
        contactRepository.saveAndFlush(contact);

        // Get all the contactList where citizenCountry2 contains DEFAULT_CITIZEN_COUNTRY_2
        defaultContactShouldBeFound("citizenCountry2.contains=" + DEFAULT_CITIZEN_COUNTRY_2);

        // Get all the contactList where citizenCountry2 contains UPDATED_CITIZEN_COUNTRY_2
        defaultContactShouldNotBeFound("citizenCountry2.contains=" + UPDATED_CITIZEN_COUNTRY_2);
    }

    @Test
    @Transactional
    void getAllContactsByCitizenCountry2NotContainsSomething() throws Exception {
        // Initialize the database
        contactRepository.saveAndFlush(contact);

        // Get all the contactList where citizenCountry2 does not contain DEFAULT_CITIZEN_COUNTRY_2
        defaultContactShouldNotBeFound("citizenCountry2.doesNotContain=" + DEFAULT_CITIZEN_COUNTRY_2);

        // Get all the contactList where citizenCountry2 does not contain UPDATED_CITIZEN_COUNTRY_2
        defaultContactShouldBeFound("citizenCountry2.doesNotContain=" + UPDATED_CITIZEN_COUNTRY_2);
    }

    @Test
    @Transactional
    void getAllContactsBySocialSecurityNumberIsEqualToSomething() throws Exception {
        // Initialize the database
        contactRepository.saveAndFlush(contact);

        // Get all the contactList where socialSecurityNumber equals to DEFAULT_SOCIAL_SECURITY_NUMBER
        defaultContactShouldBeFound("socialSecurityNumber.equals=" + DEFAULT_SOCIAL_SECURITY_NUMBER);

        // Get all the contactList where socialSecurityNumber equals to UPDATED_SOCIAL_SECURITY_NUMBER
        defaultContactShouldNotBeFound("socialSecurityNumber.equals=" + UPDATED_SOCIAL_SECURITY_NUMBER);
    }

    @Test
    @Transactional
    void getAllContactsBySocialSecurityNumberIsNotEqualToSomething() throws Exception {
        // Initialize the database
        contactRepository.saveAndFlush(contact);

        // Get all the contactList where socialSecurityNumber not equals to DEFAULT_SOCIAL_SECURITY_NUMBER
        defaultContactShouldNotBeFound("socialSecurityNumber.notEquals=" + DEFAULT_SOCIAL_SECURITY_NUMBER);

        // Get all the contactList where socialSecurityNumber not equals to UPDATED_SOCIAL_SECURITY_NUMBER
        defaultContactShouldBeFound("socialSecurityNumber.notEquals=" + UPDATED_SOCIAL_SECURITY_NUMBER);
    }

    @Test
    @Transactional
    void getAllContactsBySocialSecurityNumberIsInShouldWork() throws Exception {
        // Initialize the database
        contactRepository.saveAndFlush(contact);

        // Get all the contactList where socialSecurityNumber in DEFAULT_SOCIAL_SECURITY_NUMBER or UPDATED_SOCIAL_SECURITY_NUMBER
        defaultContactShouldBeFound("socialSecurityNumber.in=" + DEFAULT_SOCIAL_SECURITY_NUMBER + "," + UPDATED_SOCIAL_SECURITY_NUMBER);

        // Get all the contactList where socialSecurityNumber equals to UPDATED_SOCIAL_SECURITY_NUMBER
        defaultContactShouldNotBeFound("socialSecurityNumber.in=" + UPDATED_SOCIAL_SECURITY_NUMBER);
    }

    @Test
    @Transactional
    void getAllContactsBySocialSecurityNumberIsNullOrNotNull() throws Exception {
        // Initialize the database
        contactRepository.saveAndFlush(contact);

        // Get all the contactList where socialSecurityNumber is not null
        defaultContactShouldBeFound("socialSecurityNumber.specified=true");

        // Get all the contactList where socialSecurityNumber is null
        defaultContactShouldNotBeFound("socialSecurityNumber.specified=false");
    }

    @Test
    @Transactional
    void getAllContactsBySocialSecurityNumberContainsSomething() throws Exception {
        // Initialize the database
        contactRepository.saveAndFlush(contact);

        // Get all the contactList where socialSecurityNumber contains DEFAULT_SOCIAL_SECURITY_NUMBER
        defaultContactShouldBeFound("socialSecurityNumber.contains=" + DEFAULT_SOCIAL_SECURITY_NUMBER);

        // Get all the contactList where socialSecurityNumber contains UPDATED_SOCIAL_SECURITY_NUMBER
        defaultContactShouldNotBeFound("socialSecurityNumber.contains=" + UPDATED_SOCIAL_SECURITY_NUMBER);
    }

    @Test
    @Transactional
    void getAllContactsBySocialSecurityNumberNotContainsSomething() throws Exception {
        // Initialize the database
        contactRepository.saveAndFlush(contact);

        // Get all the contactList where socialSecurityNumber does not contain DEFAULT_SOCIAL_SECURITY_NUMBER
        defaultContactShouldNotBeFound("socialSecurityNumber.doesNotContain=" + DEFAULT_SOCIAL_SECURITY_NUMBER);

        // Get all the contactList where socialSecurityNumber does not contain UPDATED_SOCIAL_SECURITY_NUMBER
        defaultContactShouldBeFound("socialSecurityNumber.doesNotContain=" + UPDATED_SOCIAL_SECURITY_NUMBER);
    }

    @Test
    @Transactional
    void getAllContactsByHobbiesIsEqualToSomething() throws Exception {
        // Initialize the database
        contactRepository.saveAndFlush(contact);

        // Get all the contactList where hobbies equals to DEFAULT_HOBBIES
        defaultContactShouldBeFound("hobbies.equals=" + DEFAULT_HOBBIES);

        // Get all the contactList where hobbies equals to UPDATED_HOBBIES
        defaultContactShouldNotBeFound("hobbies.equals=" + UPDATED_HOBBIES);
    }

    @Test
    @Transactional
    void getAllContactsByHobbiesIsNotEqualToSomething() throws Exception {
        // Initialize the database
        contactRepository.saveAndFlush(contact);

        // Get all the contactList where hobbies not equals to DEFAULT_HOBBIES
        defaultContactShouldNotBeFound("hobbies.notEquals=" + DEFAULT_HOBBIES);

        // Get all the contactList where hobbies not equals to UPDATED_HOBBIES
        defaultContactShouldBeFound("hobbies.notEquals=" + UPDATED_HOBBIES);
    }

    @Test
    @Transactional
    void getAllContactsByHobbiesIsInShouldWork() throws Exception {
        // Initialize the database
        contactRepository.saveAndFlush(contact);

        // Get all the contactList where hobbies in DEFAULT_HOBBIES or UPDATED_HOBBIES
        defaultContactShouldBeFound("hobbies.in=" + DEFAULT_HOBBIES + "," + UPDATED_HOBBIES);

        // Get all the contactList where hobbies equals to UPDATED_HOBBIES
        defaultContactShouldNotBeFound("hobbies.in=" + UPDATED_HOBBIES);
    }

    @Test
    @Transactional
    void getAllContactsByHobbiesIsNullOrNotNull() throws Exception {
        // Initialize the database
        contactRepository.saveAndFlush(contact);

        // Get all the contactList where hobbies is not null
        defaultContactShouldBeFound("hobbies.specified=true");

        // Get all the contactList where hobbies is null
        defaultContactShouldNotBeFound("hobbies.specified=false");
    }

    @Test
    @Transactional
    void getAllContactsByHobbiesContainsSomething() throws Exception {
        // Initialize the database
        contactRepository.saveAndFlush(contact);

        // Get all the contactList where hobbies contains DEFAULT_HOBBIES
        defaultContactShouldBeFound("hobbies.contains=" + DEFAULT_HOBBIES);

        // Get all the contactList where hobbies contains UPDATED_HOBBIES
        defaultContactShouldNotBeFound("hobbies.contains=" + UPDATED_HOBBIES);
    }

    @Test
    @Transactional
    void getAllContactsByHobbiesNotContainsSomething() throws Exception {
        // Initialize the database
        contactRepository.saveAndFlush(contact);

        // Get all the contactList where hobbies does not contain DEFAULT_HOBBIES
        defaultContactShouldNotBeFound("hobbies.doesNotContain=" + DEFAULT_HOBBIES);

        // Get all the contactList where hobbies does not contain UPDATED_HOBBIES
        defaultContactShouldBeFound("hobbies.doesNotContain=" + UPDATED_HOBBIES);
    }

    @Test
    @Transactional
    void getAllContactsByDailyWorkIsEqualToSomething() throws Exception {
        // Initialize the database
        contactRepository.saveAndFlush(contact);

        // Get all the contactList where dailyWork equals to DEFAULT_DAILY_WORK
        defaultContactShouldBeFound("dailyWork.equals=" + DEFAULT_DAILY_WORK);

        // Get all the contactList where dailyWork equals to UPDATED_DAILY_WORK
        defaultContactShouldNotBeFound("dailyWork.equals=" + UPDATED_DAILY_WORK);
    }

    @Test
    @Transactional
    void getAllContactsByDailyWorkIsNotEqualToSomething() throws Exception {
        // Initialize the database
        contactRepository.saveAndFlush(contact);

        // Get all the contactList where dailyWork not equals to DEFAULT_DAILY_WORK
        defaultContactShouldNotBeFound("dailyWork.notEquals=" + DEFAULT_DAILY_WORK);

        // Get all the contactList where dailyWork not equals to UPDATED_DAILY_WORK
        defaultContactShouldBeFound("dailyWork.notEquals=" + UPDATED_DAILY_WORK);
    }

    @Test
    @Transactional
    void getAllContactsByDailyWorkIsInShouldWork() throws Exception {
        // Initialize the database
        contactRepository.saveAndFlush(contact);

        // Get all the contactList where dailyWork in DEFAULT_DAILY_WORK or UPDATED_DAILY_WORK
        defaultContactShouldBeFound("dailyWork.in=" + DEFAULT_DAILY_WORK + "," + UPDATED_DAILY_WORK);

        // Get all the contactList where dailyWork equals to UPDATED_DAILY_WORK
        defaultContactShouldNotBeFound("dailyWork.in=" + UPDATED_DAILY_WORK);
    }

    @Test
    @Transactional
    void getAllContactsByDailyWorkIsNullOrNotNull() throws Exception {
        // Initialize the database
        contactRepository.saveAndFlush(contact);

        // Get all the contactList where dailyWork is not null
        defaultContactShouldBeFound("dailyWork.specified=true");

        // Get all the contactList where dailyWork is null
        defaultContactShouldNotBeFound("dailyWork.specified=false");
    }

    @Test
    @Transactional
    void getAllContactsByDailyWorkContainsSomething() throws Exception {
        // Initialize the database
        contactRepository.saveAndFlush(contact);

        // Get all the contactList where dailyWork contains DEFAULT_DAILY_WORK
        defaultContactShouldBeFound("dailyWork.contains=" + DEFAULT_DAILY_WORK);

        // Get all the contactList where dailyWork contains UPDATED_DAILY_WORK
        defaultContactShouldNotBeFound("dailyWork.contains=" + UPDATED_DAILY_WORK);
    }

    @Test
    @Transactional
    void getAllContactsByDailyWorkNotContainsSomething() throws Exception {
        // Initialize the database
        contactRepository.saveAndFlush(contact);

        // Get all the contactList where dailyWork does not contain DEFAULT_DAILY_WORK
        defaultContactShouldNotBeFound("dailyWork.doesNotContain=" + DEFAULT_DAILY_WORK);

        // Get all the contactList where dailyWork does not contain UPDATED_DAILY_WORK
        defaultContactShouldBeFound("dailyWork.doesNotContain=" + UPDATED_DAILY_WORK);
    }

    @Test
    @Transactional
    void getAllContactsByContactAttribute01IsEqualToSomething() throws Exception {
        // Initialize the database
        contactRepository.saveAndFlush(contact);

        // Get all the contactList where contactAttribute01 equals to DEFAULT_CONTACT_ATTRIBUTE_01
        defaultContactShouldBeFound("contactAttribute01.equals=" + DEFAULT_CONTACT_ATTRIBUTE_01);

        // Get all the contactList where contactAttribute01 equals to UPDATED_CONTACT_ATTRIBUTE_01
        defaultContactShouldNotBeFound("contactAttribute01.equals=" + UPDATED_CONTACT_ATTRIBUTE_01);
    }

    @Test
    @Transactional
    void getAllContactsByContactAttribute01IsNotEqualToSomething() throws Exception {
        // Initialize the database
        contactRepository.saveAndFlush(contact);

        // Get all the contactList where contactAttribute01 not equals to DEFAULT_CONTACT_ATTRIBUTE_01
        defaultContactShouldNotBeFound("contactAttribute01.notEquals=" + DEFAULT_CONTACT_ATTRIBUTE_01);

        // Get all the contactList where contactAttribute01 not equals to UPDATED_CONTACT_ATTRIBUTE_01
        defaultContactShouldBeFound("contactAttribute01.notEquals=" + UPDATED_CONTACT_ATTRIBUTE_01);
    }

    @Test
    @Transactional
    void getAllContactsByContactAttribute01IsInShouldWork() throws Exception {
        // Initialize the database
        contactRepository.saveAndFlush(contact);

        // Get all the contactList where contactAttribute01 in DEFAULT_CONTACT_ATTRIBUTE_01 or UPDATED_CONTACT_ATTRIBUTE_01
        defaultContactShouldBeFound("contactAttribute01.in=" + DEFAULT_CONTACT_ATTRIBUTE_01 + "," + UPDATED_CONTACT_ATTRIBUTE_01);

        // Get all the contactList where contactAttribute01 equals to UPDATED_CONTACT_ATTRIBUTE_01
        defaultContactShouldNotBeFound("contactAttribute01.in=" + UPDATED_CONTACT_ATTRIBUTE_01);
    }

    @Test
    @Transactional
    void getAllContactsByContactAttribute01IsNullOrNotNull() throws Exception {
        // Initialize the database
        contactRepository.saveAndFlush(contact);

        // Get all the contactList where contactAttribute01 is not null
        defaultContactShouldBeFound("contactAttribute01.specified=true");

        // Get all the contactList where contactAttribute01 is null
        defaultContactShouldNotBeFound("contactAttribute01.specified=false");
    }

    @Test
    @Transactional
    void getAllContactsByContactAttribute01ContainsSomething() throws Exception {
        // Initialize the database
        contactRepository.saveAndFlush(contact);

        // Get all the contactList where contactAttribute01 contains DEFAULT_CONTACT_ATTRIBUTE_01
        defaultContactShouldBeFound("contactAttribute01.contains=" + DEFAULT_CONTACT_ATTRIBUTE_01);

        // Get all the contactList where contactAttribute01 contains UPDATED_CONTACT_ATTRIBUTE_01
        defaultContactShouldNotBeFound("contactAttribute01.contains=" + UPDATED_CONTACT_ATTRIBUTE_01);
    }

    @Test
    @Transactional
    void getAllContactsByContactAttribute01NotContainsSomething() throws Exception {
        // Initialize the database
        contactRepository.saveAndFlush(contact);

        // Get all the contactList where contactAttribute01 does not contain DEFAULT_CONTACT_ATTRIBUTE_01
        defaultContactShouldNotBeFound("contactAttribute01.doesNotContain=" + DEFAULT_CONTACT_ATTRIBUTE_01);

        // Get all the contactList where contactAttribute01 does not contain UPDATED_CONTACT_ATTRIBUTE_01
        defaultContactShouldBeFound("contactAttribute01.doesNotContain=" + UPDATED_CONTACT_ATTRIBUTE_01);
    }

    @Test
    @Transactional
    void getAllContactsByImageTypeIsEqualToSomething() throws Exception {
        // Initialize the database
        contactRepository.saveAndFlush(contact);

        // Get all the contactList where imageType equals to DEFAULT_IMAGE_TYPE
        defaultContactShouldBeFound("imageType.equals=" + DEFAULT_IMAGE_TYPE);

        // Get all the contactList where imageType equals to UPDATED_IMAGE_TYPE
        defaultContactShouldNotBeFound("imageType.equals=" + UPDATED_IMAGE_TYPE);
    }

    @Test
    @Transactional
    void getAllContactsByImageTypeIsNotEqualToSomething() throws Exception {
        // Initialize the database
        contactRepository.saveAndFlush(contact);

        // Get all the contactList where imageType not equals to DEFAULT_IMAGE_TYPE
        defaultContactShouldNotBeFound("imageType.notEquals=" + DEFAULT_IMAGE_TYPE);

        // Get all the contactList where imageType not equals to UPDATED_IMAGE_TYPE
        defaultContactShouldBeFound("imageType.notEquals=" + UPDATED_IMAGE_TYPE);
    }

    @Test
    @Transactional
    void getAllContactsByImageTypeIsInShouldWork() throws Exception {
        // Initialize the database
        contactRepository.saveAndFlush(contact);

        // Get all the contactList where imageType in DEFAULT_IMAGE_TYPE or UPDATED_IMAGE_TYPE
        defaultContactShouldBeFound("imageType.in=" + DEFAULT_IMAGE_TYPE + "," + UPDATED_IMAGE_TYPE);

        // Get all the contactList where imageType equals to UPDATED_IMAGE_TYPE
        defaultContactShouldNotBeFound("imageType.in=" + UPDATED_IMAGE_TYPE);
    }

    @Test
    @Transactional
    void getAllContactsByImageTypeIsNullOrNotNull() throws Exception {
        // Initialize the database
        contactRepository.saveAndFlush(contact);

        // Get all the contactList where imageType is not null
        defaultContactShouldBeFound("imageType.specified=true");

        // Get all the contactList where imageType is null
        defaultContactShouldNotBeFound("imageType.specified=false");
    }

    @Test
    @Transactional
    void getAllContactsByImageTypeContainsSomething() throws Exception {
        // Initialize the database
        contactRepository.saveAndFlush(contact);

        // Get all the contactList where imageType contains DEFAULT_IMAGE_TYPE
        defaultContactShouldBeFound("imageType.contains=" + DEFAULT_IMAGE_TYPE);

        // Get all the contactList where imageType contains UPDATED_IMAGE_TYPE
        defaultContactShouldNotBeFound("imageType.contains=" + UPDATED_IMAGE_TYPE);
    }

    @Test
    @Transactional
    void getAllContactsByImageTypeNotContainsSomething() throws Exception {
        // Initialize the database
        contactRepository.saveAndFlush(contact);

        // Get all the contactList where imageType does not contain DEFAULT_IMAGE_TYPE
        defaultContactShouldNotBeFound("imageType.doesNotContain=" + DEFAULT_IMAGE_TYPE);

        // Get all the contactList where imageType does not contain UPDATED_IMAGE_TYPE
        defaultContactShouldBeFound("imageType.doesNotContain=" + UPDATED_IMAGE_TYPE);
    }

    @Test
    @Transactional
    void getAllContactsByInactivIsEqualToSomething() throws Exception {
        // Initialize the database
        contactRepository.saveAndFlush(contact);

        // Get all the contactList where inactiv equals to DEFAULT_INACTIV
        defaultContactShouldBeFound("inactiv.equals=" + DEFAULT_INACTIV);

        // Get all the contactList where inactiv equals to UPDATED_INACTIV
        defaultContactShouldNotBeFound("inactiv.equals=" + UPDATED_INACTIV);
    }

    @Test
    @Transactional
    void getAllContactsByInactivIsNotEqualToSomething() throws Exception {
        // Initialize the database
        contactRepository.saveAndFlush(contact);

        // Get all the contactList where inactiv not equals to DEFAULT_INACTIV
        defaultContactShouldNotBeFound("inactiv.notEquals=" + DEFAULT_INACTIV);

        // Get all the contactList where inactiv not equals to UPDATED_INACTIV
        defaultContactShouldBeFound("inactiv.notEquals=" + UPDATED_INACTIV);
    }

    @Test
    @Transactional
    void getAllContactsByInactivIsInShouldWork() throws Exception {
        // Initialize the database
        contactRepository.saveAndFlush(contact);

        // Get all the contactList where inactiv in DEFAULT_INACTIV or UPDATED_INACTIV
        defaultContactShouldBeFound("inactiv.in=" + DEFAULT_INACTIV + "," + UPDATED_INACTIV);

        // Get all the contactList where inactiv equals to UPDATED_INACTIV
        defaultContactShouldNotBeFound("inactiv.in=" + UPDATED_INACTIV);
    }

    @Test
    @Transactional
    void getAllContactsByInactivIsNullOrNotNull() throws Exception {
        // Initialize the database
        contactRepository.saveAndFlush(contact);

        // Get all the contactList where inactiv is not null
        defaultContactShouldBeFound("inactiv.specified=true");

        // Get all the contactList where inactiv is null
        defaultContactShouldNotBeFound("inactiv.specified=false");
    }

    @Test
    @Transactional
    void getAllContactsByCustomFieldsIsEqualToSomething() throws Exception {
        // Initialize the database
        contactRepository.saveAndFlush(contact);
        CustomFieldValue customFields = CustomFieldValueResourceIT.createEntity(em);
        em.persist(customFields);
        em.flush();
        contact.addCustomFields(customFields);
        contactRepository.saveAndFlush(contact);
        Long customFieldsId = customFields.getId();

        // Get all the contactList where customFields equals to customFieldsId
        defaultContactShouldBeFound("customFieldsId.equals=" + customFieldsId);

        // Get all the contactList where customFields equals to (customFieldsId + 1)
        defaultContactShouldNotBeFound("customFieldsId.equals=" + (customFieldsId + 1));
    }

    @Test
    @Transactional
    void getAllContactsByRelationsIsEqualToSomething() throws Exception {
        // Initialize the database
        contactRepository.saveAndFlush(contact);
        ContactRelation relations = ContactRelationResourceIT.createEntity(em);
        em.persist(relations);
        em.flush();
        contact.addRelations(relations);
        contactRepository.saveAndFlush(contact);
        Long relationsId = relations.getId();

        // Get all the contactList where relations equals to relationsId
        defaultContactShouldBeFound("relationsId.equals=" + relationsId);

        // Get all the contactList where relations equals to (relationsId + 1)
        defaultContactShouldNotBeFound("relationsId.equals=" + (relationsId + 1));
    }

    @Test
    @Transactional
    void getAllContactsByGroupsIsEqualToSomething() throws Exception {
        // Initialize the database
        contactRepository.saveAndFlush(contact);
        ContactGroup groups = ContactGroupResourceIT.createEntity(em);
        em.persist(groups);
        em.flush();
        contact.addGroups(groups);
        contactRepository.saveAndFlush(contact);
        Long groupsId = groups.getId();

        // Get all the contactList where groups equals to groupsId
        defaultContactShouldBeFound("groupsId.equals=" + groupsId);

        // Get all the contactList where groups equals to (groupsId + 1)
        defaultContactShouldNotBeFound("groupsId.equals=" + (groupsId + 1));
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultContactShouldBeFound(String filter) throws Exception {
        restContactMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(contact.getId().intValue())))
            .andExpect(jsonPath("$.[*].remoteId").value(hasItem(DEFAULT_REMOTE_ID)))
            .andExpect(jsonPath("$.[*].number").value(hasItem(DEFAULT_NUMBER)))
            .andExpect(jsonPath("$.[*].type").value(hasItem(DEFAULT_TYPE.toString())))
            .andExpect(jsonPath("$.[*].gender").value(hasItem(DEFAULT_GENDER.toString())))
            .andExpect(jsonPath("$.[*].genderSalutationActive").value(hasItem(DEFAULT_GENDER_SALUTATION_ACTIVE.booleanValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)))
            .andExpect(jsonPath("$.[*].nameAddition").value(hasItem(DEFAULT_NAME_ADDITION)))
            .andExpect(jsonPath("$.[*].salutation").value(hasItem(DEFAULT_SALUTATION)))
            .andExpect(jsonPath("$.[*].phone").value(hasItem(DEFAULT_PHONE)))
            .andExpect(jsonPath("$.[*].fax").value(hasItem(DEFAULT_FAX)))
            .andExpect(jsonPath("$.[*].email").value(hasItem(DEFAULT_EMAIL)))
            .andExpect(jsonPath("$.[*].website").value(hasItem(DEFAULT_WEBSITE)))
            .andExpect(jsonPath("$.[*].notes").value(hasItem(DEFAULT_NOTES)))
            .andExpect(jsonPath("$.[*].communicationLanguage").value(hasItem(DEFAULT_COMMUNICATION_LANGUAGE)))
            .andExpect(jsonPath("$.[*].communicationChannel").value(hasItem(DEFAULT_COMMUNICATION_CHANNEL.toString())))
            .andExpect(jsonPath("$.[*].communicationNewsletter").value(hasItem(DEFAULT_COMMUNICATION_NEWSLETTER.toString())))
            .andExpect(jsonPath("$.[*].currency").value(hasItem(DEFAULT_CURRENCY.toString())))
            .andExpect(jsonPath("$.[*].ebillAccountId").value(hasItem(DEFAULT_EBILL_ACCOUNT_ID)))
            .andExpect(jsonPath("$.[*].vatIdentification").value(hasItem(DEFAULT_VAT_IDENTIFICATION)))
            .andExpect(jsonPath("$.[*].vatRate").value(hasItem(DEFAULT_VAT_RATE.doubleValue())))
            .andExpect(jsonPath("$.[*].discountRate").value(hasItem(DEFAULT_DISCOUNT_RATE.doubleValue())))
            .andExpect(jsonPath("$.[*].discountType").value(hasItem(DEFAULT_DISCOUNT_TYPE.toString())))
            .andExpect(jsonPath("$.[*].paymentGrace").value(hasItem(DEFAULT_PAYMENT_GRACE)))
            .andExpect(jsonPath("$.[*].hourlyRate").value(hasItem(DEFAULT_HOURLY_RATE.doubleValue())))
            .andExpect(jsonPath("$.[*].created").value(hasItem(sameInstant(DEFAULT_CREATED))))
            .andExpect(jsonPath("$.[*].mainAddressId").value(hasItem(DEFAULT_MAIN_ADDRESS_ID)))
            .andExpect(jsonPath("$.[*].birthDate").value(hasItem(DEFAULT_BIRTH_DATE.toString())))
            .andExpect(jsonPath("$.[*].birthPlace").value(hasItem(DEFAULT_BIRTH_PLACE)))
            .andExpect(jsonPath("$.[*].placeOfOrigin").value(hasItem(DEFAULT_PLACE_OF_ORIGIN)))
            .andExpect(jsonPath("$.[*].citizenCountry1").value(hasItem(DEFAULT_CITIZEN_COUNTRY_1)))
            .andExpect(jsonPath("$.[*].citizenCountry2").value(hasItem(DEFAULT_CITIZEN_COUNTRY_2)))
            .andExpect(jsonPath("$.[*].socialSecurityNumber").value(hasItem(DEFAULT_SOCIAL_SECURITY_NUMBER)))
            .andExpect(jsonPath("$.[*].hobbies").value(hasItem(DEFAULT_HOBBIES)))
            .andExpect(jsonPath("$.[*].dailyWork").value(hasItem(DEFAULT_DAILY_WORK)))
            .andExpect(jsonPath("$.[*].contactAttribute01").value(hasItem(DEFAULT_CONTACT_ATTRIBUTE_01)))
            .andExpect(jsonPath("$.[*].avatarContentType").value(hasItem(DEFAULT_AVATAR_CONTENT_TYPE)))
            .andExpect(jsonPath("$.[*].avatar").value(hasItem(Base64Utils.encodeToString(DEFAULT_AVATAR))))
            .andExpect(jsonPath("$.[*].imageType").value(hasItem(DEFAULT_IMAGE_TYPE)))
            .andExpect(jsonPath("$.[*].inactiv").value(hasItem(DEFAULT_INACTIV.booleanValue())));

        // Check, that the count call also returns 1
        restContactMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultContactShouldNotBeFound(String filter) throws Exception {
        restContactMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restContactMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingContact() throws Exception {
        // Get the contact
        restContactMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewContact() throws Exception {
        // Initialize the database
        contactRepository.saveAndFlush(contact);

        int databaseSizeBeforeUpdate = contactRepository.findAll().size();

        // Update the contact
        Contact updatedContact = contactRepository.findById(contact.getId()).get();
        // Disconnect from session so that the updates on updatedContact are not directly saved in db
        em.detach(updatedContact);
        updatedContact
            .remoteId(UPDATED_REMOTE_ID)
            .number(UPDATED_NUMBER)
            .type(UPDATED_TYPE)
            .gender(UPDATED_GENDER)
            .genderSalutationActive(UPDATED_GENDER_SALUTATION_ACTIVE)
            .name(UPDATED_NAME)
            .nameAddition(UPDATED_NAME_ADDITION)
            .salutation(UPDATED_SALUTATION)
            .phone(UPDATED_PHONE)
            .fax(UPDATED_FAX)
            .email(UPDATED_EMAIL)
            .website(UPDATED_WEBSITE)
            .notes(UPDATED_NOTES)
            .communicationLanguage(UPDATED_COMMUNICATION_LANGUAGE)
            .communicationChannel(UPDATED_COMMUNICATION_CHANNEL)
            .communicationNewsletter(UPDATED_COMMUNICATION_NEWSLETTER)
            .currency(UPDATED_CURRENCY)
            .ebillAccountId(UPDATED_EBILL_ACCOUNT_ID)
            .vatIdentification(UPDATED_VAT_IDENTIFICATION)
            .vatRate(UPDATED_VAT_RATE)
            .discountRate(UPDATED_DISCOUNT_RATE)
            .discountType(UPDATED_DISCOUNT_TYPE)
            .paymentGrace(UPDATED_PAYMENT_GRACE)
            .hourlyRate(UPDATED_HOURLY_RATE)
            .created(UPDATED_CREATED)
            .mainAddressId(UPDATED_MAIN_ADDRESS_ID)
            .birthDate(UPDATED_BIRTH_DATE)
            .birthPlace(UPDATED_BIRTH_PLACE)
            .placeOfOrigin(UPDATED_PLACE_OF_ORIGIN)
            .citizenCountry1(UPDATED_CITIZEN_COUNTRY_1)
            .citizenCountry2(UPDATED_CITIZEN_COUNTRY_2)
            .socialSecurityNumber(UPDATED_SOCIAL_SECURITY_NUMBER)
            .hobbies(UPDATED_HOBBIES)
            .dailyWork(UPDATED_DAILY_WORK)
            .contactAttribute01(UPDATED_CONTACT_ATTRIBUTE_01)
            .avatar(UPDATED_AVATAR)
            .avatarContentType(UPDATED_AVATAR_CONTENT_TYPE)
            .imageType(UPDATED_IMAGE_TYPE)
            .inactiv(UPDATED_INACTIV);
        ContactDTO contactDTO = contactMapper.toDto(updatedContact);

        restContactMockMvc
            .perform(
                put(ENTITY_API_URL_ID, contactDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(contactDTO))
            )
            .andExpect(status().isOk());

        // Validate the Contact in the database
        List<Contact> contactList = contactRepository.findAll();
        assertThat(contactList).hasSize(databaseSizeBeforeUpdate);
        Contact testContact = contactList.get(contactList.size() - 1);
        assertThat(testContact.getRemoteId()).isEqualTo(UPDATED_REMOTE_ID);
        assertThat(testContact.getNumber()).isEqualTo(UPDATED_NUMBER);
        assertThat(testContact.getType()).isEqualTo(UPDATED_TYPE);
        assertThat(testContact.getGender()).isEqualTo(UPDATED_GENDER);
        assertThat(testContact.getGenderSalutationActive()).isEqualTo(UPDATED_GENDER_SALUTATION_ACTIVE);
        assertThat(testContact.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testContact.getNameAddition()).isEqualTo(UPDATED_NAME_ADDITION);
        assertThat(testContact.getSalutation()).isEqualTo(UPDATED_SALUTATION);
        assertThat(testContact.getPhone()).isEqualTo(UPDATED_PHONE);
        assertThat(testContact.getFax()).isEqualTo(UPDATED_FAX);
        assertThat(testContact.getEmail()).isEqualTo(UPDATED_EMAIL);
        assertThat(testContact.getWebsite()).isEqualTo(UPDATED_WEBSITE);
        assertThat(testContact.getNotes()).isEqualTo(UPDATED_NOTES);
        assertThat(testContact.getCommunicationLanguage()).isEqualTo(UPDATED_COMMUNICATION_LANGUAGE);
        assertThat(testContact.getCommunicationChannel()).isEqualTo(UPDATED_COMMUNICATION_CHANNEL);
        assertThat(testContact.getCommunicationNewsletter()).isEqualTo(UPDATED_COMMUNICATION_NEWSLETTER);
        assertThat(testContact.getCurrency()).isEqualTo(UPDATED_CURRENCY);
        assertThat(testContact.getEbillAccountId()).isEqualTo(UPDATED_EBILL_ACCOUNT_ID);
        assertThat(testContact.getVatIdentification()).isEqualTo(UPDATED_VAT_IDENTIFICATION);
        assertThat(testContact.getVatRate()).isEqualTo(UPDATED_VAT_RATE);
        assertThat(testContact.getDiscountRate()).isEqualTo(UPDATED_DISCOUNT_RATE);
        assertThat(testContact.getDiscountType()).isEqualTo(UPDATED_DISCOUNT_TYPE);
        assertThat(testContact.getPaymentGrace()).isEqualTo(UPDATED_PAYMENT_GRACE);
        assertThat(testContact.getHourlyRate()).isEqualTo(UPDATED_HOURLY_RATE);
        assertThat(testContact.getCreated()).isEqualTo(UPDATED_CREATED);
        assertThat(testContact.getMainAddressId()).isEqualTo(UPDATED_MAIN_ADDRESS_ID);
        assertThat(testContact.getBirthDate()).isEqualTo(UPDATED_BIRTH_DATE);
        assertThat(testContact.getBirthPlace()).isEqualTo(UPDATED_BIRTH_PLACE);
        assertThat(testContact.getPlaceOfOrigin()).isEqualTo(UPDATED_PLACE_OF_ORIGIN);
        assertThat(testContact.getCitizenCountry1()).isEqualTo(UPDATED_CITIZEN_COUNTRY_1);
        assertThat(testContact.getCitizenCountry2()).isEqualTo(UPDATED_CITIZEN_COUNTRY_2);
        assertThat(testContact.getSocialSecurityNumber()).isEqualTo(UPDATED_SOCIAL_SECURITY_NUMBER);
        assertThat(testContact.getHobbies()).isEqualTo(UPDATED_HOBBIES);
        assertThat(testContact.getDailyWork()).isEqualTo(UPDATED_DAILY_WORK);
        assertThat(testContact.getContactAttribute01()).isEqualTo(UPDATED_CONTACT_ATTRIBUTE_01);
        assertThat(testContact.getAvatar()).isEqualTo(UPDATED_AVATAR);
        assertThat(testContact.getAvatarContentType()).isEqualTo(UPDATED_AVATAR_CONTENT_TYPE);
        assertThat(testContact.getImageType()).isEqualTo(UPDATED_IMAGE_TYPE);
        assertThat(testContact.getInactiv()).isEqualTo(UPDATED_INACTIV);
    }

    @Test
    @Transactional
    void putNonExistingContact() throws Exception {
        int databaseSizeBeforeUpdate = contactRepository.findAll().size();
        contact.setId(count.incrementAndGet());

        // Create the Contact
        ContactDTO contactDTO = contactMapper.toDto(contact);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restContactMockMvc
            .perform(
                put(ENTITY_API_URL_ID, contactDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(contactDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Contact in the database
        List<Contact> contactList = contactRepository.findAll();
        assertThat(contactList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchContact() throws Exception {
        int databaseSizeBeforeUpdate = contactRepository.findAll().size();
        contact.setId(count.incrementAndGet());

        // Create the Contact
        ContactDTO contactDTO = contactMapper.toDto(contact);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restContactMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(contactDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Contact in the database
        List<Contact> contactList = contactRepository.findAll();
        assertThat(contactList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamContact() throws Exception {
        int databaseSizeBeforeUpdate = contactRepository.findAll().size();
        contact.setId(count.incrementAndGet());

        // Create the Contact
        ContactDTO contactDTO = contactMapper.toDto(contact);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restContactMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(contactDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Contact in the database
        List<Contact> contactList = contactRepository.findAll();
        assertThat(contactList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateContactWithPatch() throws Exception {
        // Initialize the database
        contactRepository.saveAndFlush(contact);

        int databaseSizeBeforeUpdate = contactRepository.findAll().size();

        // Update the contact using partial update
        Contact partialUpdatedContact = new Contact();
        partialUpdatedContact.setId(contact.getId());

        partialUpdatedContact
            .remoteId(UPDATED_REMOTE_ID)
            .type(UPDATED_TYPE)
            .gender(UPDATED_GENDER)
            .nameAddition(UPDATED_NAME_ADDITION)
            .salutation(UPDATED_SALUTATION)
            .phone(UPDATED_PHONE)
            .fax(UPDATED_FAX)
            .communicationChannel(UPDATED_COMMUNICATION_CHANNEL)
            .vatIdentification(UPDATED_VAT_IDENTIFICATION)
            .birthDate(UPDATED_BIRTH_DATE)
            .citizenCountry1(UPDATED_CITIZEN_COUNTRY_1)
            .citizenCountry2(UPDATED_CITIZEN_COUNTRY_2)
            .socialSecurityNumber(UPDATED_SOCIAL_SECURITY_NUMBER)
            .hobbies(UPDATED_HOBBIES)
            .dailyWork(UPDATED_DAILY_WORK);

        restContactMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedContact.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedContact))
            )
            .andExpect(status().isOk());

        // Validate the Contact in the database
        List<Contact> contactList = contactRepository.findAll();
        assertThat(contactList).hasSize(databaseSizeBeforeUpdate);
        Contact testContact = contactList.get(contactList.size() - 1);
        assertThat(testContact.getRemoteId()).isEqualTo(UPDATED_REMOTE_ID);
        assertThat(testContact.getNumber()).isEqualTo(DEFAULT_NUMBER);
        assertThat(testContact.getType()).isEqualTo(UPDATED_TYPE);
        assertThat(testContact.getGender()).isEqualTo(UPDATED_GENDER);
        assertThat(testContact.getGenderSalutationActive()).isEqualTo(DEFAULT_GENDER_SALUTATION_ACTIVE);
        assertThat(testContact.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testContact.getNameAddition()).isEqualTo(UPDATED_NAME_ADDITION);
        assertThat(testContact.getSalutation()).isEqualTo(UPDATED_SALUTATION);
        assertThat(testContact.getPhone()).isEqualTo(UPDATED_PHONE);
        assertThat(testContact.getFax()).isEqualTo(UPDATED_FAX);
        assertThat(testContact.getEmail()).isEqualTo(DEFAULT_EMAIL);
        assertThat(testContact.getWebsite()).isEqualTo(DEFAULT_WEBSITE);
        assertThat(testContact.getNotes()).isEqualTo(DEFAULT_NOTES);
        assertThat(testContact.getCommunicationLanguage()).isEqualTo(DEFAULT_COMMUNICATION_LANGUAGE);
        assertThat(testContact.getCommunicationChannel()).isEqualTo(UPDATED_COMMUNICATION_CHANNEL);
        assertThat(testContact.getCommunicationNewsletter()).isEqualTo(DEFAULT_COMMUNICATION_NEWSLETTER);
        assertThat(testContact.getCurrency()).isEqualTo(DEFAULT_CURRENCY);
        assertThat(testContact.getEbillAccountId()).isEqualTo(DEFAULT_EBILL_ACCOUNT_ID);
        assertThat(testContact.getVatIdentification()).isEqualTo(UPDATED_VAT_IDENTIFICATION);
        assertThat(testContact.getVatRate()).isEqualTo(DEFAULT_VAT_RATE);
        assertThat(testContact.getDiscountRate()).isEqualTo(DEFAULT_DISCOUNT_RATE);
        assertThat(testContact.getDiscountType()).isEqualTo(DEFAULT_DISCOUNT_TYPE);
        assertThat(testContact.getPaymentGrace()).isEqualTo(DEFAULT_PAYMENT_GRACE);
        assertThat(testContact.getHourlyRate()).isEqualTo(DEFAULT_HOURLY_RATE);
        assertThat(testContact.getCreated()).isEqualTo(DEFAULT_CREATED);
        assertThat(testContact.getMainAddressId()).isEqualTo(DEFAULT_MAIN_ADDRESS_ID);
        assertThat(testContact.getBirthDate()).isEqualTo(UPDATED_BIRTH_DATE);
        assertThat(testContact.getBirthPlace()).isEqualTo(DEFAULT_BIRTH_PLACE);
        assertThat(testContact.getPlaceOfOrigin()).isEqualTo(DEFAULT_PLACE_OF_ORIGIN);
        assertThat(testContact.getCitizenCountry1()).isEqualTo(UPDATED_CITIZEN_COUNTRY_1);
        assertThat(testContact.getCitizenCountry2()).isEqualTo(UPDATED_CITIZEN_COUNTRY_2);
        assertThat(testContact.getSocialSecurityNumber()).isEqualTo(UPDATED_SOCIAL_SECURITY_NUMBER);
        assertThat(testContact.getHobbies()).isEqualTo(UPDATED_HOBBIES);
        assertThat(testContact.getDailyWork()).isEqualTo(UPDATED_DAILY_WORK);
        assertThat(testContact.getContactAttribute01()).isEqualTo(DEFAULT_CONTACT_ATTRIBUTE_01);
        assertThat(testContact.getAvatar()).isEqualTo(DEFAULT_AVATAR);
        assertThat(testContact.getAvatarContentType()).isEqualTo(DEFAULT_AVATAR_CONTENT_TYPE);
        assertThat(testContact.getImageType()).isEqualTo(DEFAULT_IMAGE_TYPE);
        assertThat(testContact.getInactiv()).isEqualTo(DEFAULT_INACTIV);
    }

    @Test
    @Transactional
    void fullUpdateContactWithPatch() throws Exception {
        // Initialize the database
        contactRepository.saveAndFlush(contact);

        int databaseSizeBeforeUpdate = contactRepository.findAll().size();

        // Update the contact using partial update
        Contact partialUpdatedContact = new Contact();
        partialUpdatedContact.setId(contact.getId());

        partialUpdatedContact
            .remoteId(UPDATED_REMOTE_ID)
            .number(UPDATED_NUMBER)
            .type(UPDATED_TYPE)
            .gender(UPDATED_GENDER)
            .genderSalutationActive(UPDATED_GENDER_SALUTATION_ACTIVE)
            .name(UPDATED_NAME)
            .nameAddition(UPDATED_NAME_ADDITION)
            .salutation(UPDATED_SALUTATION)
            .phone(UPDATED_PHONE)
            .fax(UPDATED_FAX)
            .email(UPDATED_EMAIL)
            .website(UPDATED_WEBSITE)
            .notes(UPDATED_NOTES)
            .communicationLanguage(UPDATED_COMMUNICATION_LANGUAGE)
            .communicationChannel(UPDATED_COMMUNICATION_CHANNEL)
            .communicationNewsletter(UPDATED_COMMUNICATION_NEWSLETTER)
            .currency(UPDATED_CURRENCY)
            .ebillAccountId(UPDATED_EBILL_ACCOUNT_ID)
            .vatIdentification(UPDATED_VAT_IDENTIFICATION)
            .vatRate(UPDATED_VAT_RATE)
            .discountRate(UPDATED_DISCOUNT_RATE)
            .discountType(UPDATED_DISCOUNT_TYPE)
            .paymentGrace(UPDATED_PAYMENT_GRACE)
            .hourlyRate(UPDATED_HOURLY_RATE)
            .created(UPDATED_CREATED)
            .mainAddressId(UPDATED_MAIN_ADDRESS_ID)
            .birthDate(UPDATED_BIRTH_DATE)
            .birthPlace(UPDATED_BIRTH_PLACE)
            .placeOfOrigin(UPDATED_PLACE_OF_ORIGIN)
            .citizenCountry1(UPDATED_CITIZEN_COUNTRY_1)
            .citizenCountry2(UPDATED_CITIZEN_COUNTRY_2)
            .socialSecurityNumber(UPDATED_SOCIAL_SECURITY_NUMBER)
            .hobbies(UPDATED_HOBBIES)
            .dailyWork(UPDATED_DAILY_WORK)
            .contactAttribute01(UPDATED_CONTACT_ATTRIBUTE_01)
            .avatar(UPDATED_AVATAR)
            .avatarContentType(UPDATED_AVATAR_CONTENT_TYPE)
            .imageType(UPDATED_IMAGE_TYPE)
            .inactiv(UPDATED_INACTIV);

        restContactMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedContact.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedContact))
            )
            .andExpect(status().isOk());

        // Validate the Contact in the database
        List<Contact> contactList = contactRepository.findAll();
        assertThat(contactList).hasSize(databaseSizeBeforeUpdate);
        Contact testContact = contactList.get(contactList.size() - 1);
        assertThat(testContact.getRemoteId()).isEqualTo(UPDATED_REMOTE_ID);
        assertThat(testContact.getNumber()).isEqualTo(UPDATED_NUMBER);
        assertThat(testContact.getType()).isEqualTo(UPDATED_TYPE);
        assertThat(testContact.getGender()).isEqualTo(UPDATED_GENDER);
        assertThat(testContact.getGenderSalutationActive()).isEqualTo(UPDATED_GENDER_SALUTATION_ACTIVE);
        assertThat(testContact.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testContact.getNameAddition()).isEqualTo(UPDATED_NAME_ADDITION);
        assertThat(testContact.getSalutation()).isEqualTo(UPDATED_SALUTATION);
        assertThat(testContact.getPhone()).isEqualTo(UPDATED_PHONE);
        assertThat(testContact.getFax()).isEqualTo(UPDATED_FAX);
        assertThat(testContact.getEmail()).isEqualTo(UPDATED_EMAIL);
        assertThat(testContact.getWebsite()).isEqualTo(UPDATED_WEBSITE);
        assertThat(testContact.getNotes()).isEqualTo(UPDATED_NOTES);
        assertThat(testContact.getCommunicationLanguage()).isEqualTo(UPDATED_COMMUNICATION_LANGUAGE);
        assertThat(testContact.getCommunicationChannel()).isEqualTo(UPDATED_COMMUNICATION_CHANNEL);
        assertThat(testContact.getCommunicationNewsletter()).isEqualTo(UPDATED_COMMUNICATION_NEWSLETTER);
        assertThat(testContact.getCurrency()).isEqualTo(UPDATED_CURRENCY);
        assertThat(testContact.getEbillAccountId()).isEqualTo(UPDATED_EBILL_ACCOUNT_ID);
        assertThat(testContact.getVatIdentification()).isEqualTo(UPDATED_VAT_IDENTIFICATION);
        assertThat(testContact.getVatRate()).isEqualTo(UPDATED_VAT_RATE);
        assertThat(testContact.getDiscountRate()).isEqualTo(UPDATED_DISCOUNT_RATE);
        assertThat(testContact.getDiscountType()).isEqualTo(UPDATED_DISCOUNT_TYPE);
        assertThat(testContact.getPaymentGrace()).isEqualTo(UPDATED_PAYMENT_GRACE);
        assertThat(testContact.getHourlyRate()).isEqualTo(UPDATED_HOURLY_RATE);
        assertThat(testContact.getCreated()).isEqualTo(UPDATED_CREATED);
        assertThat(testContact.getMainAddressId()).isEqualTo(UPDATED_MAIN_ADDRESS_ID);
        assertThat(testContact.getBirthDate()).isEqualTo(UPDATED_BIRTH_DATE);
        assertThat(testContact.getBirthPlace()).isEqualTo(UPDATED_BIRTH_PLACE);
        assertThat(testContact.getPlaceOfOrigin()).isEqualTo(UPDATED_PLACE_OF_ORIGIN);
        assertThat(testContact.getCitizenCountry1()).isEqualTo(UPDATED_CITIZEN_COUNTRY_1);
        assertThat(testContact.getCitizenCountry2()).isEqualTo(UPDATED_CITIZEN_COUNTRY_2);
        assertThat(testContact.getSocialSecurityNumber()).isEqualTo(UPDATED_SOCIAL_SECURITY_NUMBER);
        assertThat(testContact.getHobbies()).isEqualTo(UPDATED_HOBBIES);
        assertThat(testContact.getDailyWork()).isEqualTo(UPDATED_DAILY_WORK);
        assertThat(testContact.getContactAttribute01()).isEqualTo(UPDATED_CONTACT_ATTRIBUTE_01);
        assertThat(testContact.getAvatar()).isEqualTo(UPDATED_AVATAR);
        assertThat(testContact.getAvatarContentType()).isEqualTo(UPDATED_AVATAR_CONTENT_TYPE);
        assertThat(testContact.getImageType()).isEqualTo(UPDATED_IMAGE_TYPE);
        assertThat(testContact.getInactiv()).isEqualTo(UPDATED_INACTIV);
    }

    @Test
    @Transactional
    void patchNonExistingContact() throws Exception {
        int databaseSizeBeforeUpdate = contactRepository.findAll().size();
        contact.setId(count.incrementAndGet());

        // Create the Contact
        ContactDTO contactDTO = contactMapper.toDto(contact);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restContactMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, contactDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(contactDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Contact in the database
        List<Contact> contactList = contactRepository.findAll();
        assertThat(contactList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchContact() throws Exception {
        int databaseSizeBeforeUpdate = contactRepository.findAll().size();
        contact.setId(count.incrementAndGet());

        // Create the Contact
        ContactDTO contactDTO = contactMapper.toDto(contact);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restContactMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(contactDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Contact in the database
        List<Contact> contactList = contactRepository.findAll();
        assertThat(contactList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamContact() throws Exception {
        int databaseSizeBeforeUpdate = contactRepository.findAll().size();
        contact.setId(count.incrementAndGet());

        // Create the Contact
        ContactDTO contactDTO = contactMapper.toDto(contact);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restContactMockMvc
            .perform(
                patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(contactDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the Contact in the database
        List<Contact> contactList = contactRepository.findAll();
        assertThat(contactList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteContact() throws Exception {
        // Initialize the database
        contactRepository.saveAndFlush(contact);

        int databaseSizeBeforeDelete = contactRepository.findAll().size();

        // Delete the contact
        restContactMockMvc
            .perform(delete(ENTITY_API_URL_ID, contact.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Contact> contactList = contactRepository.findAll();
        assertThat(contactList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
