package ch.united.fastadmin.web.rest;

import static ch.united.fastadmin.web.rest.TestUtil.sameInstant;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import ch.united.fastadmin.IntegrationTest;
import ch.united.fastadmin.domain.Contact;
import ch.united.fastadmin.domain.enumeration.CommunicationChannel;
import ch.united.fastadmin.domain.enumeration.CommunicationNewsletter;
import ch.united.fastadmin.domain.enumeration.ContactType;
import ch.united.fastadmin.domain.enumeration.Currency;
import ch.united.fastadmin.domain.enumeration.DiscountType;
import ch.united.fastadmin.domain.enumeration.GenderType;
import ch.united.fastadmin.repository.ContactRepository;
import ch.united.fastadmin.service.ContactService;
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

    private static final Double DEFAULT_DISCOUNT_RATE = 1D;
    private static final Double UPDATED_DISCOUNT_RATE = 2D;

    private static final DiscountType DEFAULT_DISCOUNT_TYPE = DiscountType.IN_PERCENT;
    private static final DiscountType UPDATED_DISCOUNT_TYPE = DiscountType.AMOUNT;

    private static final Integer DEFAULT_PAYMENT_GRACE = 1;
    private static final Integer UPDATED_PAYMENT_GRACE = 2;

    private static final Double DEFAULT_HOURLY_RATE = 1D;
    private static final Double UPDATED_HOURLY_RATE = 2D;

    private static final ZonedDateTime DEFAULT_CREATED = ZonedDateTime.ofInstant(Instant.ofEpochMilli(0L), ZoneOffset.UTC);
    private static final ZonedDateTime UPDATED_CREATED = ZonedDateTime.now(ZoneId.systemDefault()).withNano(0);

    private static final Integer DEFAULT_MAIN_ADDRESS_ID = 1;
    private static final Integer UPDATED_MAIN_ADDRESS_ID = 2;

    private static final LocalDate DEFAULT_BIRTH_DATE = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_BIRTH_DATE = LocalDate.now(ZoneId.systemDefault());

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
