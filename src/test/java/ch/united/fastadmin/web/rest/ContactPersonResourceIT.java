package ch.united.fastadmin.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import ch.united.fastadmin.IntegrationTest;
import ch.united.fastadmin.domain.ContactPerson;
import ch.united.fastadmin.domain.enumeration.GenderType;
import ch.united.fastadmin.repository.ContactPersonRepository;
import ch.united.fastadmin.service.dto.ContactPersonDTO;
import ch.united.fastadmin.service.mapper.ContactPersonMapper;
import java.time.LocalDate;
import java.time.ZoneId;
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
import org.springframework.util.Base64Utils;

/**
 * Integration tests for the {@link ContactPersonResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class ContactPersonResourceIT {

    private static final Integer DEFAULT_REMOTE_ID = 1;
    private static final Integer UPDATED_REMOTE_ID = 2;

    private static final Boolean DEFAULT_DEFAULT_PERSON = false;
    private static final Boolean UPDATED_DEFAULT_PERSON = true;

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_SURNAME = "AAAAAAAAAA";
    private static final String UPDATED_SURNAME = "BBBBBBBBBB";

    private static final GenderType DEFAULT_GENDER = GenderType.MALE;
    private static final GenderType UPDATED_GENDER = GenderType.FEMALE;

    private static final String DEFAULT_EMAIL = "AAAAAAAAAA";
    private static final String UPDATED_EMAIL = "BBBBBBBBBB";

    private static final String DEFAULT_PHONE = "AAAAAAAAAA";
    private static final String UPDATED_PHONE = "BBBBBBBBBB";

    private static final String DEFAULT_DEPARTMENT = "AAAAAAAAAA";
    private static final String UPDATED_DEPARTMENT = "BBBBBBBBBB";

    private static final String DEFAULT_SALUTATION = "AAAAAAAAAA";
    private static final String UPDATED_SALUTATION = "BBBBBBBBBB";

    private static final Boolean DEFAULT_SHOW_TITLE = false;
    private static final Boolean UPDATED_SHOW_TITLE = true;

    private static final Boolean DEFAULT_SHOW_DEPARTMENT = false;
    private static final Boolean UPDATED_SHOW_DEPARTMENT = true;

    private static final Boolean DEFAULT_WANTS_NEWSLETTER = false;
    private static final Boolean UPDATED_WANTS_NEWSLETTER = true;

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

    private static final String ENTITY_API_URL = "/api/contact-people";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ContactPersonRepository contactPersonRepository;

    @Autowired
    private ContactPersonMapper contactPersonMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restContactPersonMockMvc;

    private ContactPerson contactPerson;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static ContactPerson createEntity(EntityManager em) {
        ContactPerson contactPerson = new ContactPerson()
            .remoteId(DEFAULT_REMOTE_ID)
            .defaultPerson(DEFAULT_DEFAULT_PERSON)
            .name(DEFAULT_NAME)
            .surname(DEFAULT_SURNAME)
            .gender(DEFAULT_GENDER)
            .email(DEFAULT_EMAIL)
            .phone(DEFAULT_PHONE)
            .department(DEFAULT_DEPARTMENT)
            .salutation(DEFAULT_SALUTATION)
            .showTitle(DEFAULT_SHOW_TITLE)
            .showDepartment(DEFAULT_SHOW_DEPARTMENT)
            .wantsNewsletter(DEFAULT_WANTS_NEWSLETTER)
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
        return contactPerson;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static ContactPerson createUpdatedEntity(EntityManager em) {
        ContactPerson contactPerson = new ContactPerson()
            .remoteId(UPDATED_REMOTE_ID)
            .defaultPerson(UPDATED_DEFAULT_PERSON)
            .name(UPDATED_NAME)
            .surname(UPDATED_SURNAME)
            .gender(UPDATED_GENDER)
            .email(UPDATED_EMAIL)
            .phone(UPDATED_PHONE)
            .department(UPDATED_DEPARTMENT)
            .salutation(UPDATED_SALUTATION)
            .showTitle(UPDATED_SHOW_TITLE)
            .showDepartment(UPDATED_SHOW_DEPARTMENT)
            .wantsNewsletter(UPDATED_WANTS_NEWSLETTER)
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
        return contactPerson;
    }

    @BeforeEach
    public void initTest() {
        contactPerson = createEntity(em);
    }

    @Test
    @Transactional
    void createContactPerson() throws Exception {
        int databaseSizeBeforeCreate = contactPersonRepository.findAll().size();
        // Create the ContactPerson
        ContactPersonDTO contactPersonDTO = contactPersonMapper.toDto(contactPerson);
        restContactPersonMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(contactPersonDTO))
            )
            .andExpect(status().isCreated());

        // Validate the ContactPerson in the database
        List<ContactPerson> contactPersonList = contactPersonRepository.findAll();
        assertThat(contactPersonList).hasSize(databaseSizeBeforeCreate + 1);
        ContactPerson testContactPerson = contactPersonList.get(contactPersonList.size() - 1);
        assertThat(testContactPerson.getRemoteId()).isEqualTo(DEFAULT_REMOTE_ID);
        assertThat(testContactPerson.getDefaultPerson()).isEqualTo(DEFAULT_DEFAULT_PERSON);
        assertThat(testContactPerson.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testContactPerson.getSurname()).isEqualTo(DEFAULT_SURNAME);
        assertThat(testContactPerson.getGender()).isEqualTo(DEFAULT_GENDER);
        assertThat(testContactPerson.getEmail()).isEqualTo(DEFAULT_EMAIL);
        assertThat(testContactPerson.getPhone()).isEqualTo(DEFAULT_PHONE);
        assertThat(testContactPerson.getDepartment()).isEqualTo(DEFAULT_DEPARTMENT);
        assertThat(testContactPerson.getSalutation()).isEqualTo(DEFAULT_SALUTATION);
        assertThat(testContactPerson.getShowTitle()).isEqualTo(DEFAULT_SHOW_TITLE);
        assertThat(testContactPerson.getShowDepartment()).isEqualTo(DEFAULT_SHOW_DEPARTMENT);
        assertThat(testContactPerson.getWantsNewsletter()).isEqualTo(DEFAULT_WANTS_NEWSLETTER);
        assertThat(testContactPerson.getBirthDate()).isEqualTo(DEFAULT_BIRTH_DATE);
        assertThat(testContactPerson.getBirthPlace()).isEqualTo(DEFAULT_BIRTH_PLACE);
        assertThat(testContactPerson.getPlaceOfOrigin()).isEqualTo(DEFAULT_PLACE_OF_ORIGIN);
        assertThat(testContactPerson.getCitizenCountry1()).isEqualTo(DEFAULT_CITIZEN_COUNTRY_1);
        assertThat(testContactPerson.getCitizenCountry2()).isEqualTo(DEFAULT_CITIZEN_COUNTRY_2);
        assertThat(testContactPerson.getSocialSecurityNumber()).isEqualTo(DEFAULT_SOCIAL_SECURITY_NUMBER);
        assertThat(testContactPerson.getHobbies()).isEqualTo(DEFAULT_HOBBIES);
        assertThat(testContactPerson.getDailyWork()).isEqualTo(DEFAULT_DAILY_WORK);
        assertThat(testContactPerson.getContactAttribute01()).isEqualTo(DEFAULT_CONTACT_ATTRIBUTE_01);
        assertThat(testContactPerson.getAvatar()).isEqualTo(DEFAULT_AVATAR);
        assertThat(testContactPerson.getAvatarContentType()).isEqualTo(DEFAULT_AVATAR_CONTENT_TYPE);
        assertThat(testContactPerson.getImageType()).isEqualTo(DEFAULT_IMAGE_TYPE);
        assertThat(testContactPerson.getInactiv()).isEqualTo(DEFAULT_INACTIV);
    }

    @Test
    @Transactional
    void createContactPersonWithExistingId() throws Exception {
        // Create the ContactPerson with an existing ID
        contactPerson.setId(1L);
        ContactPersonDTO contactPersonDTO = contactPersonMapper.toDto(contactPerson);

        int databaseSizeBeforeCreate = contactPersonRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restContactPersonMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(contactPersonDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the ContactPerson in the database
        List<ContactPerson> contactPersonList = contactPersonRepository.findAll();
        assertThat(contactPersonList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllContactPeople() throws Exception {
        // Initialize the database
        contactPersonRepository.saveAndFlush(contactPerson);

        // Get all the contactPersonList
        restContactPersonMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(contactPerson.getId().intValue())))
            .andExpect(jsonPath("$.[*].remoteId").value(hasItem(DEFAULT_REMOTE_ID)))
            .andExpect(jsonPath("$.[*].defaultPerson").value(hasItem(DEFAULT_DEFAULT_PERSON.booleanValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)))
            .andExpect(jsonPath("$.[*].surname").value(hasItem(DEFAULT_SURNAME)))
            .andExpect(jsonPath("$.[*].gender").value(hasItem(DEFAULT_GENDER.toString())))
            .andExpect(jsonPath("$.[*].email").value(hasItem(DEFAULT_EMAIL)))
            .andExpect(jsonPath("$.[*].phone").value(hasItem(DEFAULT_PHONE)))
            .andExpect(jsonPath("$.[*].department").value(hasItem(DEFAULT_DEPARTMENT)))
            .andExpect(jsonPath("$.[*].salutation").value(hasItem(DEFAULT_SALUTATION)))
            .andExpect(jsonPath("$.[*].showTitle").value(hasItem(DEFAULT_SHOW_TITLE.booleanValue())))
            .andExpect(jsonPath("$.[*].showDepartment").value(hasItem(DEFAULT_SHOW_DEPARTMENT.booleanValue())))
            .andExpect(jsonPath("$.[*].wantsNewsletter").value(hasItem(DEFAULT_WANTS_NEWSLETTER.booleanValue())))
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

    @Test
    @Transactional
    void getContactPerson() throws Exception {
        // Initialize the database
        contactPersonRepository.saveAndFlush(contactPerson);

        // Get the contactPerson
        restContactPersonMockMvc
            .perform(get(ENTITY_API_URL_ID, contactPerson.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(contactPerson.getId().intValue()))
            .andExpect(jsonPath("$.remoteId").value(DEFAULT_REMOTE_ID))
            .andExpect(jsonPath("$.defaultPerson").value(DEFAULT_DEFAULT_PERSON.booleanValue()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME))
            .andExpect(jsonPath("$.surname").value(DEFAULT_SURNAME))
            .andExpect(jsonPath("$.gender").value(DEFAULT_GENDER.toString()))
            .andExpect(jsonPath("$.email").value(DEFAULT_EMAIL))
            .andExpect(jsonPath("$.phone").value(DEFAULT_PHONE))
            .andExpect(jsonPath("$.department").value(DEFAULT_DEPARTMENT))
            .andExpect(jsonPath("$.salutation").value(DEFAULT_SALUTATION))
            .andExpect(jsonPath("$.showTitle").value(DEFAULT_SHOW_TITLE.booleanValue()))
            .andExpect(jsonPath("$.showDepartment").value(DEFAULT_SHOW_DEPARTMENT.booleanValue()))
            .andExpect(jsonPath("$.wantsNewsletter").value(DEFAULT_WANTS_NEWSLETTER.booleanValue()))
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
    void getNonExistingContactPerson() throws Exception {
        // Get the contactPerson
        restContactPersonMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewContactPerson() throws Exception {
        // Initialize the database
        contactPersonRepository.saveAndFlush(contactPerson);

        int databaseSizeBeforeUpdate = contactPersonRepository.findAll().size();

        // Update the contactPerson
        ContactPerson updatedContactPerson = contactPersonRepository.findById(contactPerson.getId()).get();
        // Disconnect from session so that the updates on updatedContactPerson are not directly saved in db
        em.detach(updatedContactPerson);
        updatedContactPerson
            .remoteId(UPDATED_REMOTE_ID)
            .defaultPerson(UPDATED_DEFAULT_PERSON)
            .name(UPDATED_NAME)
            .surname(UPDATED_SURNAME)
            .gender(UPDATED_GENDER)
            .email(UPDATED_EMAIL)
            .phone(UPDATED_PHONE)
            .department(UPDATED_DEPARTMENT)
            .salutation(UPDATED_SALUTATION)
            .showTitle(UPDATED_SHOW_TITLE)
            .showDepartment(UPDATED_SHOW_DEPARTMENT)
            .wantsNewsletter(UPDATED_WANTS_NEWSLETTER)
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
        ContactPersonDTO contactPersonDTO = contactPersonMapper.toDto(updatedContactPerson);

        restContactPersonMockMvc
            .perform(
                put(ENTITY_API_URL_ID, contactPersonDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(contactPersonDTO))
            )
            .andExpect(status().isOk());

        // Validate the ContactPerson in the database
        List<ContactPerson> contactPersonList = contactPersonRepository.findAll();
        assertThat(contactPersonList).hasSize(databaseSizeBeforeUpdate);
        ContactPerson testContactPerson = contactPersonList.get(contactPersonList.size() - 1);
        assertThat(testContactPerson.getRemoteId()).isEqualTo(UPDATED_REMOTE_ID);
        assertThat(testContactPerson.getDefaultPerson()).isEqualTo(UPDATED_DEFAULT_PERSON);
        assertThat(testContactPerson.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testContactPerson.getSurname()).isEqualTo(UPDATED_SURNAME);
        assertThat(testContactPerson.getGender()).isEqualTo(UPDATED_GENDER);
        assertThat(testContactPerson.getEmail()).isEqualTo(UPDATED_EMAIL);
        assertThat(testContactPerson.getPhone()).isEqualTo(UPDATED_PHONE);
        assertThat(testContactPerson.getDepartment()).isEqualTo(UPDATED_DEPARTMENT);
        assertThat(testContactPerson.getSalutation()).isEqualTo(UPDATED_SALUTATION);
        assertThat(testContactPerson.getShowTitle()).isEqualTo(UPDATED_SHOW_TITLE);
        assertThat(testContactPerson.getShowDepartment()).isEqualTo(UPDATED_SHOW_DEPARTMENT);
        assertThat(testContactPerson.getWantsNewsletter()).isEqualTo(UPDATED_WANTS_NEWSLETTER);
        assertThat(testContactPerson.getBirthDate()).isEqualTo(UPDATED_BIRTH_DATE);
        assertThat(testContactPerson.getBirthPlace()).isEqualTo(UPDATED_BIRTH_PLACE);
        assertThat(testContactPerson.getPlaceOfOrigin()).isEqualTo(UPDATED_PLACE_OF_ORIGIN);
        assertThat(testContactPerson.getCitizenCountry1()).isEqualTo(UPDATED_CITIZEN_COUNTRY_1);
        assertThat(testContactPerson.getCitizenCountry2()).isEqualTo(UPDATED_CITIZEN_COUNTRY_2);
        assertThat(testContactPerson.getSocialSecurityNumber()).isEqualTo(UPDATED_SOCIAL_SECURITY_NUMBER);
        assertThat(testContactPerson.getHobbies()).isEqualTo(UPDATED_HOBBIES);
        assertThat(testContactPerson.getDailyWork()).isEqualTo(UPDATED_DAILY_WORK);
        assertThat(testContactPerson.getContactAttribute01()).isEqualTo(UPDATED_CONTACT_ATTRIBUTE_01);
        assertThat(testContactPerson.getAvatar()).isEqualTo(UPDATED_AVATAR);
        assertThat(testContactPerson.getAvatarContentType()).isEqualTo(UPDATED_AVATAR_CONTENT_TYPE);
        assertThat(testContactPerson.getImageType()).isEqualTo(UPDATED_IMAGE_TYPE);
        assertThat(testContactPerson.getInactiv()).isEqualTo(UPDATED_INACTIV);
    }

    @Test
    @Transactional
    void putNonExistingContactPerson() throws Exception {
        int databaseSizeBeforeUpdate = contactPersonRepository.findAll().size();
        contactPerson.setId(count.incrementAndGet());

        // Create the ContactPerson
        ContactPersonDTO contactPersonDTO = contactPersonMapper.toDto(contactPerson);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restContactPersonMockMvc
            .perform(
                put(ENTITY_API_URL_ID, contactPersonDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(contactPersonDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the ContactPerson in the database
        List<ContactPerson> contactPersonList = contactPersonRepository.findAll();
        assertThat(contactPersonList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchContactPerson() throws Exception {
        int databaseSizeBeforeUpdate = contactPersonRepository.findAll().size();
        contactPerson.setId(count.incrementAndGet());

        // Create the ContactPerson
        ContactPersonDTO contactPersonDTO = contactPersonMapper.toDto(contactPerson);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restContactPersonMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(contactPersonDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the ContactPerson in the database
        List<ContactPerson> contactPersonList = contactPersonRepository.findAll();
        assertThat(contactPersonList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamContactPerson() throws Exception {
        int databaseSizeBeforeUpdate = contactPersonRepository.findAll().size();
        contactPerson.setId(count.incrementAndGet());

        // Create the ContactPerson
        ContactPersonDTO contactPersonDTO = contactPersonMapper.toDto(contactPerson);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restContactPersonMockMvc
            .perform(
                put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(contactPersonDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the ContactPerson in the database
        List<ContactPerson> contactPersonList = contactPersonRepository.findAll();
        assertThat(contactPersonList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateContactPersonWithPatch() throws Exception {
        // Initialize the database
        contactPersonRepository.saveAndFlush(contactPerson);

        int databaseSizeBeforeUpdate = contactPersonRepository.findAll().size();

        // Update the contactPerson using partial update
        ContactPerson partialUpdatedContactPerson = new ContactPerson();
        partialUpdatedContactPerson.setId(contactPerson.getId());

        partialUpdatedContactPerson
            .defaultPerson(UPDATED_DEFAULT_PERSON)
            .surname(UPDATED_SURNAME)
            .gender(UPDATED_GENDER)
            .email(UPDATED_EMAIL)
            .phone(UPDATED_PHONE)
            .salutation(UPDATED_SALUTATION)
            .showDepartment(UPDATED_SHOW_DEPARTMENT)
            .wantsNewsletter(UPDATED_WANTS_NEWSLETTER)
            .birthDate(UPDATED_BIRTH_DATE)
            .citizenCountry1(UPDATED_CITIZEN_COUNTRY_1)
            .hobbies(UPDATED_HOBBIES)
            .contactAttribute01(UPDATED_CONTACT_ATTRIBUTE_01);

        restContactPersonMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedContactPerson.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedContactPerson))
            )
            .andExpect(status().isOk());

        // Validate the ContactPerson in the database
        List<ContactPerson> contactPersonList = contactPersonRepository.findAll();
        assertThat(contactPersonList).hasSize(databaseSizeBeforeUpdate);
        ContactPerson testContactPerson = contactPersonList.get(contactPersonList.size() - 1);
        assertThat(testContactPerson.getRemoteId()).isEqualTo(DEFAULT_REMOTE_ID);
        assertThat(testContactPerson.getDefaultPerson()).isEqualTo(UPDATED_DEFAULT_PERSON);
        assertThat(testContactPerson.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testContactPerson.getSurname()).isEqualTo(UPDATED_SURNAME);
        assertThat(testContactPerson.getGender()).isEqualTo(UPDATED_GENDER);
        assertThat(testContactPerson.getEmail()).isEqualTo(UPDATED_EMAIL);
        assertThat(testContactPerson.getPhone()).isEqualTo(UPDATED_PHONE);
        assertThat(testContactPerson.getDepartment()).isEqualTo(DEFAULT_DEPARTMENT);
        assertThat(testContactPerson.getSalutation()).isEqualTo(UPDATED_SALUTATION);
        assertThat(testContactPerson.getShowTitle()).isEqualTo(DEFAULT_SHOW_TITLE);
        assertThat(testContactPerson.getShowDepartment()).isEqualTo(UPDATED_SHOW_DEPARTMENT);
        assertThat(testContactPerson.getWantsNewsletter()).isEqualTo(UPDATED_WANTS_NEWSLETTER);
        assertThat(testContactPerson.getBirthDate()).isEqualTo(UPDATED_BIRTH_DATE);
        assertThat(testContactPerson.getBirthPlace()).isEqualTo(DEFAULT_BIRTH_PLACE);
        assertThat(testContactPerson.getPlaceOfOrigin()).isEqualTo(DEFAULT_PLACE_OF_ORIGIN);
        assertThat(testContactPerson.getCitizenCountry1()).isEqualTo(UPDATED_CITIZEN_COUNTRY_1);
        assertThat(testContactPerson.getCitizenCountry2()).isEqualTo(DEFAULT_CITIZEN_COUNTRY_2);
        assertThat(testContactPerson.getSocialSecurityNumber()).isEqualTo(DEFAULT_SOCIAL_SECURITY_NUMBER);
        assertThat(testContactPerson.getHobbies()).isEqualTo(UPDATED_HOBBIES);
        assertThat(testContactPerson.getDailyWork()).isEqualTo(DEFAULT_DAILY_WORK);
        assertThat(testContactPerson.getContactAttribute01()).isEqualTo(UPDATED_CONTACT_ATTRIBUTE_01);
        assertThat(testContactPerson.getAvatar()).isEqualTo(DEFAULT_AVATAR);
        assertThat(testContactPerson.getAvatarContentType()).isEqualTo(DEFAULT_AVATAR_CONTENT_TYPE);
        assertThat(testContactPerson.getImageType()).isEqualTo(DEFAULT_IMAGE_TYPE);
        assertThat(testContactPerson.getInactiv()).isEqualTo(DEFAULT_INACTIV);
    }

    @Test
    @Transactional
    void fullUpdateContactPersonWithPatch() throws Exception {
        // Initialize the database
        contactPersonRepository.saveAndFlush(contactPerson);

        int databaseSizeBeforeUpdate = contactPersonRepository.findAll().size();

        // Update the contactPerson using partial update
        ContactPerson partialUpdatedContactPerson = new ContactPerson();
        partialUpdatedContactPerson.setId(contactPerson.getId());

        partialUpdatedContactPerson
            .remoteId(UPDATED_REMOTE_ID)
            .defaultPerson(UPDATED_DEFAULT_PERSON)
            .name(UPDATED_NAME)
            .surname(UPDATED_SURNAME)
            .gender(UPDATED_GENDER)
            .email(UPDATED_EMAIL)
            .phone(UPDATED_PHONE)
            .department(UPDATED_DEPARTMENT)
            .salutation(UPDATED_SALUTATION)
            .showTitle(UPDATED_SHOW_TITLE)
            .showDepartment(UPDATED_SHOW_DEPARTMENT)
            .wantsNewsletter(UPDATED_WANTS_NEWSLETTER)
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

        restContactPersonMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedContactPerson.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedContactPerson))
            )
            .andExpect(status().isOk());

        // Validate the ContactPerson in the database
        List<ContactPerson> contactPersonList = contactPersonRepository.findAll();
        assertThat(contactPersonList).hasSize(databaseSizeBeforeUpdate);
        ContactPerson testContactPerson = contactPersonList.get(contactPersonList.size() - 1);
        assertThat(testContactPerson.getRemoteId()).isEqualTo(UPDATED_REMOTE_ID);
        assertThat(testContactPerson.getDefaultPerson()).isEqualTo(UPDATED_DEFAULT_PERSON);
        assertThat(testContactPerson.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testContactPerson.getSurname()).isEqualTo(UPDATED_SURNAME);
        assertThat(testContactPerson.getGender()).isEqualTo(UPDATED_GENDER);
        assertThat(testContactPerson.getEmail()).isEqualTo(UPDATED_EMAIL);
        assertThat(testContactPerson.getPhone()).isEqualTo(UPDATED_PHONE);
        assertThat(testContactPerson.getDepartment()).isEqualTo(UPDATED_DEPARTMENT);
        assertThat(testContactPerson.getSalutation()).isEqualTo(UPDATED_SALUTATION);
        assertThat(testContactPerson.getShowTitle()).isEqualTo(UPDATED_SHOW_TITLE);
        assertThat(testContactPerson.getShowDepartment()).isEqualTo(UPDATED_SHOW_DEPARTMENT);
        assertThat(testContactPerson.getWantsNewsletter()).isEqualTo(UPDATED_WANTS_NEWSLETTER);
        assertThat(testContactPerson.getBirthDate()).isEqualTo(UPDATED_BIRTH_DATE);
        assertThat(testContactPerson.getBirthPlace()).isEqualTo(UPDATED_BIRTH_PLACE);
        assertThat(testContactPerson.getPlaceOfOrigin()).isEqualTo(UPDATED_PLACE_OF_ORIGIN);
        assertThat(testContactPerson.getCitizenCountry1()).isEqualTo(UPDATED_CITIZEN_COUNTRY_1);
        assertThat(testContactPerson.getCitizenCountry2()).isEqualTo(UPDATED_CITIZEN_COUNTRY_2);
        assertThat(testContactPerson.getSocialSecurityNumber()).isEqualTo(UPDATED_SOCIAL_SECURITY_NUMBER);
        assertThat(testContactPerson.getHobbies()).isEqualTo(UPDATED_HOBBIES);
        assertThat(testContactPerson.getDailyWork()).isEqualTo(UPDATED_DAILY_WORK);
        assertThat(testContactPerson.getContactAttribute01()).isEqualTo(UPDATED_CONTACT_ATTRIBUTE_01);
        assertThat(testContactPerson.getAvatar()).isEqualTo(UPDATED_AVATAR);
        assertThat(testContactPerson.getAvatarContentType()).isEqualTo(UPDATED_AVATAR_CONTENT_TYPE);
        assertThat(testContactPerson.getImageType()).isEqualTo(UPDATED_IMAGE_TYPE);
        assertThat(testContactPerson.getInactiv()).isEqualTo(UPDATED_INACTIV);
    }

    @Test
    @Transactional
    void patchNonExistingContactPerson() throws Exception {
        int databaseSizeBeforeUpdate = contactPersonRepository.findAll().size();
        contactPerson.setId(count.incrementAndGet());

        // Create the ContactPerson
        ContactPersonDTO contactPersonDTO = contactPersonMapper.toDto(contactPerson);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restContactPersonMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, contactPersonDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(contactPersonDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the ContactPerson in the database
        List<ContactPerson> contactPersonList = contactPersonRepository.findAll();
        assertThat(contactPersonList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchContactPerson() throws Exception {
        int databaseSizeBeforeUpdate = contactPersonRepository.findAll().size();
        contactPerson.setId(count.incrementAndGet());

        // Create the ContactPerson
        ContactPersonDTO contactPersonDTO = contactPersonMapper.toDto(contactPerson);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restContactPersonMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(contactPersonDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the ContactPerson in the database
        List<ContactPerson> contactPersonList = contactPersonRepository.findAll();
        assertThat(contactPersonList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamContactPerson() throws Exception {
        int databaseSizeBeforeUpdate = contactPersonRepository.findAll().size();
        contactPerson.setId(count.incrementAndGet());

        // Create the ContactPerson
        ContactPersonDTO contactPersonDTO = contactPersonMapper.toDto(contactPerson);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restContactPersonMockMvc
            .perform(
                patch(ENTITY_API_URL)
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(contactPersonDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the ContactPerson in the database
        List<ContactPerson> contactPersonList = contactPersonRepository.findAll();
        assertThat(contactPersonList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteContactPerson() throws Exception {
        // Initialize the database
        contactPersonRepository.saveAndFlush(contactPerson);

        int databaseSizeBeforeDelete = contactPersonRepository.findAll().size();

        // Delete the contactPerson
        restContactPersonMockMvc
            .perform(delete(ENTITY_API_URL_ID, contactPerson.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<ContactPerson> contactPersonList = contactPersonRepository.findAll();
        assertThat(contactPersonList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
