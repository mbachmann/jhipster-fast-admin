package ch.united.fastadmin.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.elasticsearch.index.query.QueryBuilders.queryStringQuery;
import static org.hamcrest.Matchers.hasItem;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import ch.united.fastadmin.IntegrationTest;
import ch.united.fastadmin.domain.ContactPerson;
import ch.united.fastadmin.domain.CustomField;
import ch.united.fastadmin.domain.enumeration.GenderType;
import ch.united.fastadmin.repository.ContactPersonRepository;
import ch.united.fastadmin.repository.search.ContactPersonSearchRepository;
import ch.united.fastadmin.service.criteria.ContactPersonCriteria;
import ch.united.fastadmin.service.dto.ContactPersonDTO;
import ch.united.fastadmin.service.mapper.ContactPersonMapper;
import java.util.Collections;
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
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

/**
 * Integration tests for the {@link ContactPersonResource} REST controller.
 */
@IntegrationTest
@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc
@WithMockUser
class ContactPersonResourceIT {

    private static final Integer DEFAULT_REMOTE_ID = 1;
    private static final Integer UPDATED_REMOTE_ID = 2;
    private static final Integer SMALLER_REMOTE_ID = 1 - 1;

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

    private static final String ENTITY_API_URL = "/api/contact-people";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";
    private static final String ENTITY_SEARCH_API_URL = "/api/_search/contact-people";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ContactPersonRepository contactPersonRepository;

    @Autowired
    private ContactPersonMapper contactPersonMapper;

    /**
     * This repository is mocked in the ch.united.fastadmin.repository.search test package.
     *
     * @see ch.united.fastadmin.repository.search.ContactPersonSearchRepositoryMockConfiguration
     */
    @Autowired
    private ContactPersonSearchRepository mockContactPersonSearchRepository;

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
            .wantsNewsletter(DEFAULT_WANTS_NEWSLETTER);
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
            .wantsNewsletter(UPDATED_WANTS_NEWSLETTER);
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

        // Validate the ContactPerson in Elasticsearch
        verify(mockContactPersonSearchRepository, times(1)).save(testContactPerson);
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

        // Validate the ContactPerson in Elasticsearch
        verify(mockContactPersonSearchRepository, times(0)).save(contactPerson);
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
            .andExpect(jsonPath("$.[*].wantsNewsletter").value(hasItem(DEFAULT_WANTS_NEWSLETTER.booleanValue())));
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
            .andExpect(jsonPath("$.wantsNewsletter").value(DEFAULT_WANTS_NEWSLETTER.booleanValue()));
    }

    @Test
    @Transactional
    void getContactPeopleByIdFiltering() throws Exception {
        // Initialize the database
        contactPersonRepository.saveAndFlush(contactPerson);

        Long id = contactPerson.getId();

        defaultContactPersonShouldBeFound("id.equals=" + id);
        defaultContactPersonShouldNotBeFound("id.notEquals=" + id);

        defaultContactPersonShouldBeFound("id.greaterThanOrEqual=" + id);
        defaultContactPersonShouldNotBeFound("id.greaterThan=" + id);

        defaultContactPersonShouldBeFound("id.lessThanOrEqual=" + id);
        defaultContactPersonShouldNotBeFound("id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllContactPeopleByRemoteIdIsEqualToSomething() throws Exception {
        // Initialize the database
        contactPersonRepository.saveAndFlush(contactPerson);

        // Get all the contactPersonList where remoteId equals to DEFAULT_REMOTE_ID
        defaultContactPersonShouldBeFound("remoteId.equals=" + DEFAULT_REMOTE_ID);

        // Get all the contactPersonList where remoteId equals to UPDATED_REMOTE_ID
        defaultContactPersonShouldNotBeFound("remoteId.equals=" + UPDATED_REMOTE_ID);
    }

    @Test
    @Transactional
    void getAllContactPeopleByRemoteIdIsNotEqualToSomething() throws Exception {
        // Initialize the database
        contactPersonRepository.saveAndFlush(contactPerson);

        // Get all the contactPersonList where remoteId not equals to DEFAULT_REMOTE_ID
        defaultContactPersonShouldNotBeFound("remoteId.notEquals=" + DEFAULT_REMOTE_ID);

        // Get all the contactPersonList where remoteId not equals to UPDATED_REMOTE_ID
        defaultContactPersonShouldBeFound("remoteId.notEquals=" + UPDATED_REMOTE_ID);
    }

    @Test
    @Transactional
    void getAllContactPeopleByRemoteIdIsInShouldWork() throws Exception {
        // Initialize the database
        contactPersonRepository.saveAndFlush(contactPerson);

        // Get all the contactPersonList where remoteId in DEFAULT_REMOTE_ID or UPDATED_REMOTE_ID
        defaultContactPersonShouldBeFound("remoteId.in=" + DEFAULT_REMOTE_ID + "," + UPDATED_REMOTE_ID);

        // Get all the contactPersonList where remoteId equals to UPDATED_REMOTE_ID
        defaultContactPersonShouldNotBeFound("remoteId.in=" + UPDATED_REMOTE_ID);
    }

    @Test
    @Transactional
    void getAllContactPeopleByRemoteIdIsNullOrNotNull() throws Exception {
        // Initialize the database
        contactPersonRepository.saveAndFlush(contactPerson);

        // Get all the contactPersonList where remoteId is not null
        defaultContactPersonShouldBeFound("remoteId.specified=true");

        // Get all the contactPersonList where remoteId is null
        defaultContactPersonShouldNotBeFound("remoteId.specified=false");
    }

    @Test
    @Transactional
    void getAllContactPeopleByRemoteIdIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        contactPersonRepository.saveAndFlush(contactPerson);

        // Get all the contactPersonList where remoteId is greater than or equal to DEFAULT_REMOTE_ID
        defaultContactPersonShouldBeFound("remoteId.greaterThanOrEqual=" + DEFAULT_REMOTE_ID);

        // Get all the contactPersonList where remoteId is greater than or equal to UPDATED_REMOTE_ID
        defaultContactPersonShouldNotBeFound("remoteId.greaterThanOrEqual=" + UPDATED_REMOTE_ID);
    }

    @Test
    @Transactional
    void getAllContactPeopleByRemoteIdIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        contactPersonRepository.saveAndFlush(contactPerson);

        // Get all the contactPersonList where remoteId is less than or equal to DEFAULT_REMOTE_ID
        defaultContactPersonShouldBeFound("remoteId.lessThanOrEqual=" + DEFAULT_REMOTE_ID);

        // Get all the contactPersonList where remoteId is less than or equal to SMALLER_REMOTE_ID
        defaultContactPersonShouldNotBeFound("remoteId.lessThanOrEqual=" + SMALLER_REMOTE_ID);
    }

    @Test
    @Transactional
    void getAllContactPeopleByRemoteIdIsLessThanSomething() throws Exception {
        // Initialize the database
        contactPersonRepository.saveAndFlush(contactPerson);

        // Get all the contactPersonList where remoteId is less than DEFAULT_REMOTE_ID
        defaultContactPersonShouldNotBeFound("remoteId.lessThan=" + DEFAULT_REMOTE_ID);

        // Get all the contactPersonList where remoteId is less than UPDATED_REMOTE_ID
        defaultContactPersonShouldBeFound("remoteId.lessThan=" + UPDATED_REMOTE_ID);
    }

    @Test
    @Transactional
    void getAllContactPeopleByRemoteIdIsGreaterThanSomething() throws Exception {
        // Initialize the database
        contactPersonRepository.saveAndFlush(contactPerson);

        // Get all the contactPersonList where remoteId is greater than DEFAULT_REMOTE_ID
        defaultContactPersonShouldNotBeFound("remoteId.greaterThan=" + DEFAULT_REMOTE_ID);

        // Get all the contactPersonList where remoteId is greater than SMALLER_REMOTE_ID
        defaultContactPersonShouldBeFound("remoteId.greaterThan=" + SMALLER_REMOTE_ID);
    }

    @Test
    @Transactional
    void getAllContactPeopleByDefaultPersonIsEqualToSomething() throws Exception {
        // Initialize the database
        contactPersonRepository.saveAndFlush(contactPerson);

        // Get all the contactPersonList where defaultPerson equals to DEFAULT_DEFAULT_PERSON
        defaultContactPersonShouldBeFound("defaultPerson.equals=" + DEFAULT_DEFAULT_PERSON);

        // Get all the contactPersonList where defaultPerson equals to UPDATED_DEFAULT_PERSON
        defaultContactPersonShouldNotBeFound("defaultPerson.equals=" + UPDATED_DEFAULT_PERSON);
    }

    @Test
    @Transactional
    void getAllContactPeopleByDefaultPersonIsNotEqualToSomething() throws Exception {
        // Initialize the database
        contactPersonRepository.saveAndFlush(contactPerson);

        // Get all the contactPersonList where defaultPerson not equals to DEFAULT_DEFAULT_PERSON
        defaultContactPersonShouldNotBeFound("defaultPerson.notEquals=" + DEFAULT_DEFAULT_PERSON);

        // Get all the contactPersonList where defaultPerson not equals to UPDATED_DEFAULT_PERSON
        defaultContactPersonShouldBeFound("defaultPerson.notEquals=" + UPDATED_DEFAULT_PERSON);
    }

    @Test
    @Transactional
    void getAllContactPeopleByDefaultPersonIsInShouldWork() throws Exception {
        // Initialize the database
        contactPersonRepository.saveAndFlush(contactPerson);

        // Get all the contactPersonList where defaultPerson in DEFAULT_DEFAULT_PERSON or UPDATED_DEFAULT_PERSON
        defaultContactPersonShouldBeFound("defaultPerson.in=" + DEFAULT_DEFAULT_PERSON + "," + UPDATED_DEFAULT_PERSON);

        // Get all the contactPersonList where defaultPerson equals to UPDATED_DEFAULT_PERSON
        defaultContactPersonShouldNotBeFound("defaultPerson.in=" + UPDATED_DEFAULT_PERSON);
    }

    @Test
    @Transactional
    void getAllContactPeopleByDefaultPersonIsNullOrNotNull() throws Exception {
        // Initialize the database
        contactPersonRepository.saveAndFlush(contactPerson);

        // Get all the contactPersonList where defaultPerson is not null
        defaultContactPersonShouldBeFound("defaultPerson.specified=true");

        // Get all the contactPersonList where defaultPerson is null
        defaultContactPersonShouldNotBeFound("defaultPerson.specified=false");
    }

    @Test
    @Transactional
    void getAllContactPeopleByNameIsEqualToSomething() throws Exception {
        // Initialize the database
        contactPersonRepository.saveAndFlush(contactPerson);

        // Get all the contactPersonList where name equals to DEFAULT_NAME
        defaultContactPersonShouldBeFound("name.equals=" + DEFAULT_NAME);

        // Get all the contactPersonList where name equals to UPDATED_NAME
        defaultContactPersonShouldNotBeFound("name.equals=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllContactPeopleByNameIsNotEqualToSomething() throws Exception {
        // Initialize the database
        contactPersonRepository.saveAndFlush(contactPerson);

        // Get all the contactPersonList where name not equals to DEFAULT_NAME
        defaultContactPersonShouldNotBeFound("name.notEquals=" + DEFAULT_NAME);

        // Get all the contactPersonList where name not equals to UPDATED_NAME
        defaultContactPersonShouldBeFound("name.notEquals=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllContactPeopleByNameIsInShouldWork() throws Exception {
        // Initialize the database
        contactPersonRepository.saveAndFlush(contactPerson);

        // Get all the contactPersonList where name in DEFAULT_NAME or UPDATED_NAME
        defaultContactPersonShouldBeFound("name.in=" + DEFAULT_NAME + "," + UPDATED_NAME);

        // Get all the contactPersonList where name equals to UPDATED_NAME
        defaultContactPersonShouldNotBeFound("name.in=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllContactPeopleByNameIsNullOrNotNull() throws Exception {
        // Initialize the database
        contactPersonRepository.saveAndFlush(contactPerson);

        // Get all the contactPersonList where name is not null
        defaultContactPersonShouldBeFound("name.specified=true");

        // Get all the contactPersonList where name is null
        defaultContactPersonShouldNotBeFound("name.specified=false");
    }

    @Test
    @Transactional
    void getAllContactPeopleByNameContainsSomething() throws Exception {
        // Initialize the database
        contactPersonRepository.saveAndFlush(contactPerson);

        // Get all the contactPersonList where name contains DEFAULT_NAME
        defaultContactPersonShouldBeFound("name.contains=" + DEFAULT_NAME);

        // Get all the contactPersonList where name contains UPDATED_NAME
        defaultContactPersonShouldNotBeFound("name.contains=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllContactPeopleByNameNotContainsSomething() throws Exception {
        // Initialize the database
        contactPersonRepository.saveAndFlush(contactPerson);

        // Get all the contactPersonList where name does not contain DEFAULT_NAME
        defaultContactPersonShouldNotBeFound("name.doesNotContain=" + DEFAULT_NAME);

        // Get all the contactPersonList where name does not contain UPDATED_NAME
        defaultContactPersonShouldBeFound("name.doesNotContain=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllContactPeopleBySurnameIsEqualToSomething() throws Exception {
        // Initialize the database
        contactPersonRepository.saveAndFlush(contactPerson);

        // Get all the contactPersonList where surname equals to DEFAULT_SURNAME
        defaultContactPersonShouldBeFound("surname.equals=" + DEFAULT_SURNAME);

        // Get all the contactPersonList where surname equals to UPDATED_SURNAME
        defaultContactPersonShouldNotBeFound("surname.equals=" + UPDATED_SURNAME);
    }

    @Test
    @Transactional
    void getAllContactPeopleBySurnameIsNotEqualToSomething() throws Exception {
        // Initialize the database
        contactPersonRepository.saveAndFlush(contactPerson);

        // Get all the contactPersonList where surname not equals to DEFAULT_SURNAME
        defaultContactPersonShouldNotBeFound("surname.notEquals=" + DEFAULT_SURNAME);

        // Get all the contactPersonList where surname not equals to UPDATED_SURNAME
        defaultContactPersonShouldBeFound("surname.notEquals=" + UPDATED_SURNAME);
    }

    @Test
    @Transactional
    void getAllContactPeopleBySurnameIsInShouldWork() throws Exception {
        // Initialize the database
        contactPersonRepository.saveAndFlush(contactPerson);

        // Get all the contactPersonList where surname in DEFAULT_SURNAME or UPDATED_SURNAME
        defaultContactPersonShouldBeFound("surname.in=" + DEFAULT_SURNAME + "," + UPDATED_SURNAME);

        // Get all the contactPersonList where surname equals to UPDATED_SURNAME
        defaultContactPersonShouldNotBeFound("surname.in=" + UPDATED_SURNAME);
    }

    @Test
    @Transactional
    void getAllContactPeopleBySurnameIsNullOrNotNull() throws Exception {
        // Initialize the database
        contactPersonRepository.saveAndFlush(contactPerson);

        // Get all the contactPersonList where surname is not null
        defaultContactPersonShouldBeFound("surname.specified=true");

        // Get all the contactPersonList where surname is null
        defaultContactPersonShouldNotBeFound("surname.specified=false");
    }

    @Test
    @Transactional
    void getAllContactPeopleBySurnameContainsSomething() throws Exception {
        // Initialize the database
        contactPersonRepository.saveAndFlush(contactPerson);

        // Get all the contactPersonList where surname contains DEFAULT_SURNAME
        defaultContactPersonShouldBeFound("surname.contains=" + DEFAULT_SURNAME);

        // Get all the contactPersonList where surname contains UPDATED_SURNAME
        defaultContactPersonShouldNotBeFound("surname.contains=" + UPDATED_SURNAME);
    }

    @Test
    @Transactional
    void getAllContactPeopleBySurnameNotContainsSomething() throws Exception {
        // Initialize the database
        contactPersonRepository.saveAndFlush(contactPerson);

        // Get all the contactPersonList where surname does not contain DEFAULT_SURNAME
        defaultContactPersonShouldNotBeFound("surname.doesNotContain=" + DEFAULT_SURNAME);

        // Get all the contactPersonList where surname does not contain UPDATED_SURNAME
        defaultContactPersonShouldBeFound("surname.doesNotContain=" + UPDATED_SURNAME);
    }

    @Test
    @Transactional
    void getAllContactPeopleByGenderIsEqualToSomething() throws Exception {
        // Initialize the database
        contactPersonRepository.saveAndFlush(contactPerson);

        // Get all the contactPersonList where gender equals to DEFAULT_GENDER
        defaultContactPersonShouldBeFound("gender.equals=" + DEFAULT_GENDER);

        // Get all the contactPersonList where gender equals to UPDATED_GENDER
        defaultContactPersonShouldNotBeFound("gender.equals=" + UPDATED_GENDER);
    }

    @Test
    @Transactional
    void getAllContactPeopleByGenderIsNotEqualToSomething() throws Exception {
        // Initialize the database
        contactPersonRepository.saveAndFlush(contactPerson);

        // Get all the contactPersonList where gender not equals to DEFAULT_GENDER
        defaultContactPersonShouldNotBeFound("gender.notEquals=" + DEFAULT_GENDER);

        // Get all the contactPersonList where gender not equals to UPDATED_GENDER
        defaultContactPersonShouldBeFound("gender.notEquals=" + UPDATED_GENDER);
    }

    @Test
    @Transactional
    void getAllContactPeopleByGenderIsInShouldWork() throws Exception {
        // Initialize the database
        contactPersonRepository.saveAndFlush(contactPerson);

        // Get all the contactPersonList where gender in DEFAULT_GENDER or UPDATED_GENDER
        defaultContactPersonShouldBeFound("gender.in=" + DEFAULT_GENDER + "," + UPDATED_GENDER);

        // Get all the contactPersonList where gender equals to UPDATED_GENDER
        defaultContactPersonShouldNotBeFound("gender.in=" + UPDATED_GENDER);
    }

    @Test
    @Transactional
    void getAllContactPeopleByGenderIsNullOrNotNull() throws Exception {
        // Initialize the database
        contactPersonRepository.saveAndFlush(contactPerson);

        // Get all the contactPersonList where gender is not null
        defaultContactPersonShouldBeFound("gender.specified=true");

        // Get all the contactPersonList where gender is null
        defaultContactPersonShouldNotBeFound("gender.specified=false");
    }

    @Test
    @Transactional
    void getAllContactPeopleByEmailIsEqualToSomething() throws Exception {
        // Initialize the database
        contactPersonRepository.saveAndFlush(contactPerson);

        // Get all the contactPersonList where email equals to DEFAULT_EMAIL
        defaultContactPersonShouldBeFound("email.equals=" + DEFAULT_EMAIL);

        // Get all the contactPersonList where email equals to UPDATED_EMAIL
        defaultContactPersonShouldNotBeFound("email.equals=" + UPDATED_EMAIL);
    }

    @Test
    @Transactional
    void getAllContactPeopleByEmailIsNotEqualToSomething() throws Exception {
        // Initialize the database
        contactPersonRepository.saveAndFlush(contactPerson);

        // Get all the contactPersonList where email not equals to DEFAULT_EMAIL
        defaultContactPersonShouldNotBeFound("email.notEquals=" + DEFAULT_EMAIL);

        // Get all the contactPersonList where email not equals to UPDATED_EMAIL
        defaultContactPersonShouldBeFound("email.notEquals=" + UPDATED_EMAIL);
    }

    @Test
    @Transactional
    void getAllContactPeopleByEmailIsInShouldWork() throws Exception {
        // Initialize the database
        contactPersonRepository.saveAndFlush(contactPerson);

        // Get all the contactPersonList where email in DEFAULT_EMAIL or UPDATED_EMAIL
        defaultContactPersonShouldBeFound("email.in=" + DEFAULT_EMAIL + "," + UPDATED_EMAIL);

        // Get all the contactPersonList where email equals to UPDATED_EMAIL
        defaultContactPersonShouldNotBeFound("email.in=" + UPDATED_EMAIL);
    }

    @Test
    @Transactional
    void getAllContactPeopleByEmailIsNullOrNotNull() throws Exception {
        // Initialize the database
        contactPersonRepository.saveAndFlush(contactPerson);

        // Get all the contactPersonList where email is not null
        defaultContactPersonShouldBeFound("email.specified=true");

        // Get all the contactPersonList where email is null
        defaultContactPersonShouldNotBeFound("email.specified=false");
    }

    @Test
    @Transactional
    void getAllContactPeopleByEmailContainsSomething() throws Exception {
        // Initialize the database
        contactPersonRepository.saveAndFlush(contactPerson);

        // Get all the contactPersonList where email contains DEFAULT_EMAIL
        defaultContactPersonShouldBeFound("email.contains=" + DEFAULT_EMAIL);

        // Get all the contactPersonList where email contains UPDATED_EMAIL
        defaultContactPersonShouldNotBeFound("email.contains=" + UPDATED_EMAIL);
    }

    @Test
    @Transactional
    void getAllContactPeopleByEmailNotContainsSomething() throws Exception {
        // Initialize the database
        contactPersonRepository.saveAndFlush(contactPerson);

        // Get all the contactPersonList where email does not contain DEFAULT_EMAIL
        defaultContactPersonShouldNotBeFound("email.doesNotContain=" + DEFAULT_EMAIL);

        // Get all the contactPersonList where email does not contain UPDATED_EMAIL
        defaultContactPersonShouldBeFound("email.doesNotContain=" + UPDATED_EMAIL);
    }

    @Test
    @Transactional
    void getAllContactPeopleByPhoneIsEqualToSomething() throws Exception {
        // Initialize the database
        contactPersonRepository.saveAndFlush(contactPerson);

        // Get all the contactPersonList where phone equals to DEFAULT_PHONE
        defaultContactPersonShouldBeFound("phone.equals=" + DEFAULT_PHONE);

        // Get all the contactPersonList where phone equals to UPDATED_PHONE
        defaultContactPersonShouldNotBeFound("phone.equals=" + UPDATED_PHONE);
    }

    @Test
    @Transactional
    void getAllContactPeopleByPhoneIsNotEqualToSomething() throws Exception {
        // Initialize the database
        contactPersonRepository.saveAndFlush(contactPerson);

        // Get all the contactPersonList where phone not equals to DEFAULT_PHONE
        defaultContactPersonShouldNotBeFound("phone.notEquals=" + DEFAULT_PHONE);

        // Get all the contactPersonList where phone not equals to UPDATED_PHONE
        defaultContactPersonShouldBeFound("phone.notEquals=" + UPDATED_PHONE);
    }

    @Test
    @Transactional
    void getAllContactPeopleByPhoneIsInShouldWork() throws Exception {
        // Initialize the database
        contactPersonRepository.saveAndFlush(contactPerson);

        // Get all the contactPersonList where phone in DEFAULT_PHONE or UPDATED_PHONE
        defaultContactPersonShouldBeFound("phone.in=" + DEFAULT_PHONE + "," + UPDATED_PHONE);

        // Get all the contactPersonList where phone equals to UPDATED_PHONE
        defaultContactPersonShouldNotBeFound("phone.in=" + UPDATED_PHONE);
    }

    @Test
    @Transactional
    void getAllContactPeopleByPhoneIsNullOrNotNull() throws Exception {
        // Initialize the database
        contactPersonRepository.saveAndFlush(contactPerson);

        // Get all the contactPersonList where phone is not null
        defaultContactPersonShouldBeFound("phone.specified=true");

        // Get all the contactPersonList where phone is null
        defaultContactPersonShouldNotBeFound("phone.specified=false");
    }

    @Test
    @Transactional
    void getAllContactPeopleByPhoneContainsSomething() throws Exception {
        // Initialize the database
        contactPersonRepository.saveAndFlush(contactPerson);

        // Get all the contactPersonList where phone contains DEFAULT_PHONE
        defaultContactPersonShouldBeFound("phone.contains=" + DEFAULT_PHONE);

        // Get all the contactPersonList where phone contains UPDATED_PHONE
        defaultContactPersonShouldNotBeFound("phone.contains=" + UPDATED_PHONE);
    }

    @Test
    @Transactional
    void getAllContactPeopleByPhoneNotContainsSomething() throws Exception {
        // Initialize the database
        contactPersonRepository.saveAndFlush(contactPerson);

        // Get all the contactPersonList where phone does not contain DEFAULT_PHONE
        defaultContactPersonShouldNotBeFound("phone.doesNotContain=" + DEFAULT_PHONE);

        // Get all the contactPersonList where phone does not contain UPDATED_PHONE
        defaultContactPersonShouldBeFound("phone.doesNotContain=" + UPDATED_PHONE);
    }

    @Test
    @Transactional
    void getAllContactPeopleByDepartmentIsEqualToSomething() throws Exception {
        // Initialize the database
        contactPersonRepository.saveAndFlush(contactPerson);

        // Get all the contactPersonList where department equals to DEFAULT_DEPARTMENT
        defaultContactPersonShouldBeFound("department.equals=" + DEFAULT_DEPARTMENT);

        // Get all the contactPersonList where department equals to UPDATED_DEPARTMENT
        defaultContactPersonShouldNotBeFound("department.equals=" + UPDATED_DEPARTMENT);
    }

    @Test
    @Transactional
    void getAllContactPeopleByDepartmentIsNotEqualToSomething() throws Exception {
        // Initialize the database
        contactPersonRepository.saveAndFlush(contactPerson);

        // Get all the contactPersonList where department not equals to DEFAULT_DEPARTMENT
        defaultContactPersonShouldNotBeFound("department.notEquals=" + DEFAULT_DEPARTMENT);

        // Get all the contactPersonList where department not equals to UPDATED_DEPARTMENT
        defaultContactPersonShouldBeFound("department.notEquals=" + UPDATED_DEPARTMENT);
    }

    @Test
    @Transactional
    void getAllContactPeopleByDepartmentIsInShouldWork() throws Exception {
        // Initialize the database
        contactPersonRepository.saveAndFlush(contactPerson);

        // Get all the contactPersonList where department in DEFAULT_DEPARTMENT or UPDATED_DEPARTMENT
        defaultContactPersonShouldBeFound("department.in=" + DEFAULT_DEPARTMENT + "," + UPDATED_DEPARTMENT);

        // Get all the contactPersonList where department equals to UPDATED_DEPARTMENT
        defaultContactPersonShouldNotBeFound("department.in=" + UPDATED_DEPARTMENT);
    }

    @Test
    @Transactional
    void getAllContactPeopleByDepartmentIsNullOrNotNull() throws Exception {
        // Initialize the database
        contactPersonRepository.saveAndFlush(contactPerson);

        // Get all the contactPersonList where department is not null
        defaultContactPersonShouldBeFound("department.specified=true");

        // Get all the contactPersonList where department is null
        defaultContactPersonShouldNotBeFound("department.specified=false");
    }

    @Test
    @Transactional
    void getAllContactPeopleByDepartmentContainsSomething() throws Exception {
        // Initialize the database
        contactPersonRepository.saveAndFlush(contactPerson);

        // Get all the contactPersonList where department contains DEFAULT_DEPARTMENT
        defaultContactPersonShouldBeFound("department.contains=" + DEFAULT_DEPARTMENT);

        // Get all the contactPersonList where department contains UPDATED_DEPARTMENT
        defaultContactPersonShouldNotBeFound("department.contains=" + UPDATED_DEPARTMENT);
    }

    @Test
    @Transactional
    void getAllContactPeopleByDepartmentNotContainsSomething() throws Exception {
        // Initialize the database
        contactPersonRepository.saveAndFlush(contactPerson);

        // Get all the contactPersonList where department does not contain DEFAULT_DEPARTMENT
        defaultContactPersonShouldNotBeFound("department.doesNotContain=" + DEFAULT_DEPARTMENT);

        // Get all the contactPersonList where department does not contain UPDATED_DEPARTMENT
        defaultContactPersonShouldBeFound("department.doesNotContain=" + UPDATED_DEPARTMENT);
    }

    @Test
    @Transactional
    void getAllContactPeopleBySalutationIsEqualToSomething() throws Exception {
        // Initialize the database
        contactPersonRepository.saveAndFlush(contactPerson);

        // Get all the contactPersonList where salutation equals to DEFAULT_SALUTATION
        defaultContactPersonShouldBeFound("salutation.equals=" + DEFAULT_SALUTATION);

        // Get all the contactPersonList where salutation equals to UPDATED_SALUTATION
        defaultContactPersonShouldNotBeFound("salutation.equals=" + UPDATED_SALUTATION);
    }

    @Test
    @Transactional
    void getAllContactPeopleBySalutationIsNotEqualToSomething() throws Exception {
        // Initialize the database
        contactPersonRepository.saveAndFlush(contactPerson);

        // Get all the contactPersonList where salutation not equals to DEFAULT_SALUTATION
        defaultContactPersonShouldNotBeFound("salutation.notEquals=" + DEFAULT_SALUTATION);

        // Get all the contactPersonList where salutation not equals to UPDATED_SALUTATION
        defaultContactPersonShouldBeFound("salutation.notEquals=" + UPDATED_SALUTATION);
    }

    @Test
    @Transactional
    void getAllContactPeopleBySalutationIsInShouldWork() throws Exception {
        // Initialize the database
        contactPersonRepository.saveAndFlush(contactPerson);

        // Get all the contactPersonList where salutation in DEFAULT_SALUTATION or UPDATED_SALUTATION
        defaultContactPersonShouldBeFound("salutation.in=" + DEFAULT_SALUTATION + "," + UPDATED_SALUTATION);

        // Get all the contactPersonList where salutation equals to UPDATED_SALUTATION
        defaultContactPersonShouldNotBeFound("salutation.in=" + UPDATED_SALUTATION);
    }

    @Test
    @Transactional
    void getAllContactPeopleBySalutationIsNullOrNotNull() throws Exception {
        // Initialize the database
        contactPersonRepository.saveAndFlush(contactPerson);

        // Get all the contactPersonList where salutation is not null
        defaultContactPersonShouldBeFound("salutation.specified=true");

        // Get all the contactPersonList where salutation is null
        defaultContactPersonShouldNotBeFound("salutation.specified=false");
    }

    @Test
    @Transactional
    void getAllContactPeopleBySalutationContainsSomething() throws Exception {
        // Initialize the database
        contactPersonRepository.saveAndFlush(contactPerson);

        // Get all the contactPersonList where salutation contains DEFAULT_SALUTATION
        defaultContactPersonShouldBeFound("salutation.contains=" + DEFAULT_SALUTATION);

        // Get all the contactPersonList where salutation contains UPDATED_SALUTATION
        defaultContactPersonShouldNotBeFound("salutation.contains=" + UPDATED_SALUTATION);
    }

    @Test
    @Transactional
    void getAllContactPeopleBySalutationNotContainsSomething() throws Exception {
        // Initialize the database
        contactPersonRepository.saveAndFlush(contactPerson);

        // Get all the contactPersonList where salutation does not contain DEFAULT_SALUTATION
        defaultContactPersonShouldNotBeFound("salutation.doesNotContain=" + DEFAULT_SALUTATION);

        // Get all the contactPersonList where salutation does not contain UPDATED_SALUTATION
        defaultContactPersonShouldBeFound("salutation.doesNotContain=" + UPDATED_SALUTATION);
    }

    @Test
    @Transactional
    void getAllContactPeopleByShowTitleIsEqualToSomething() throws Exception {
        // Initialize the database
        contactPersonRepository.saveAndFlush(contactPerson);

        // Get all the contactPersonList where showTitle equals to DEFAULT_SHOW_TITLE
        defaultContactPersonShouldBeFound("showTitle.equals=" + DEFAULT_SHOW_TITLE);

        // Get all the contactPersonList where showTitle equals to UPDATED_SHOW_TITLE
        defaultContactPersonShouldNotBeFound("showTitle.equals=" + UPDATED_SHOW_TITLE);
    }

    @Test
    @Transactional
    void getAllContactPeopleByShowTitleIsNotEqualToSomething() throws Exception {
        // Initialize the database
        contactPersonRepository.saveAndFlush(contactPerson);

        // Get all the contactPersonList where showTitle not equals to DEFAULT_SHOW_TITLE
        defaultContactPersonShouldNotBeFound("showTitle.notEquals=" + DEFAULT_SHOW_TITLE);

        // Get all the contactPersonList where showTitle not equals to UPDATED_SHOW_TITLE
        defaultContactPersonShouldBeFound("showTitle.notEquals=" + UPDATED_SHOW_TITLE);
    }

    @Test
    @Transactional
    void getAllContactPeopleByShowTitleIsInShouldWork() throws Exception {
        // Initialize the database
        contactPersonRepository.saveAndFlush(contactPerson);

        // Get all the contactPersonList where showTitle in DEFAULT_SHOW_TITLE or UPDATED_SHOW_TITLE
        defaultContactPersonShouldBeFound("showTitle.in=" + DEFAULT_SHOW_TITLE + "," + UPDATED_SHOW_TITLE);

        // Get all the contactPersonList where showTitle equals to UPDATED_SHOW_TITLE
        defaultContactPersonShouldNotBeFound("showTitle.in=" + UPDATED_SHOW_TITLE);
    }

    @Test
    @Transactional
    void getAllContactPeopleByShowTitleIsNullOrNotNull() throws Exception {
        // Initialize the database
        contactPersonRepository.saveAndFlush(contactPerson);

        // Get all the contactPersonList where showTitle is not null
        defaultContactPersonShouldBeFound("showTitle.specified=true");

        // Get all the contactPersonList where showTitle is null
        defaultContactPersonShouldNotBeFound("showTitle.specified=false");
    }

    @Test
    @Transactional
    void getAllContactPeopleByShowDepartmentIsEqualToSomething() throws Exception {
        // Initialize the database
        contactPersonRepository.saveAndFlush(contactPerson);

        // Get all the contactPersonList where showDepartment equals to DEFAULT_SHOW_DEPARTMENT
        defaultContactPersonShouldBeFound("showDepartment.equals=" + DEFAULT_SHOW_DEPARTMENT);

        // Get all the contactPersonList where showDepartment equals to UPDATED_SHOW_DEPARTMENT
        defaultContactPersonShouldNotBeFound("showDepartment.equals=" + UPDATED_SHOW_DEPARTMENT);
    }

    @Test
    @Transactional
    void getAllContactPeopleByShowDepartmentIsNotEqualToSomething() throws Exception {
        // Initialize the database
        contactPersonRepository.saveAndFlush(contactPerson);

        // Get all the contactPersonList where showDepartment not equals to DEFAULT_SHOW_DEPARTMENT
        defaultContactPersonShouldNotBeFound("showDepartment.notEquals=" + DEFAULT_SHOW_DEPARTMENT);

        // Get all the contactPersonList where showDepartment not equals to UPDATED_SHOW_DEPARTMENT
        defaultContactPersonShouldBeFound("showDepartment.notEquals=" + UPDATED_SHOW_DEPARTMENT);
    }

    @Test
    @Transactional
    void getAllContactPeopleByShowDepartmentIsInShouldWork() throws Exception {
        // Initialize the database
        contactPersonRepository.saveAndFlush(contactPerson);

        // Get all the contactPersonList where showDepartment in DEFAULT_SHOW_DEPARTMENT or UPDATED_SHOW_DEPARTMENT
        defaultContactPersonShouldBeFound("showDepartment.in=" + DEFAULT_SHOW_DEPARTMENT + "," + UPDATED_SHOW_DEPARTMENT);

        // Get all the contactPersonList where showDepartment equals to UPDATED_SHOW_DEPARTMENT
        defaultContactPersonShouldNotBeFound("showDepartment.in=" + UPDATED_SHOW_DEPARTMENT);
    }

    @Test
    @Transactional
    void getAllContactPeopleByShowDepartmentIsNullOrNotNull() throws Exception {
        // Initialize the database
        contactPersonRepository.saveAndFlush(contactPerson);

        // Get all the contactPersonList where showDepartment is not null
        defaultContactPersonShouldBeFound("showDepartment.specified=true");

        // Get all the contactPersonList where showDepartment is null
        defaultContactPersonShouldNotBeFound("showDepartment.specified=false");
    }

    @Test
    @Transactional
    void getAllContactPeopleByWantsNewsletterIsEqualToSomething() throws Exception {
        // Initialize the database
        contactPersonRepository.saveAndFlush(contactPerson);

        // Get all the contactPersonList where wantsNewsletter equals to DEFAULT_WANTS_NEWSLETTER
        defaultContactPersonShouldBeFound("wantsNewsletter.equals=" + DEFAULT_WANTS_NEWSLETTER);

        // Get all the contactPersonList where wantsNewsletter equals to UPDATED_WANTS_NEWSLETTER
        defaultContactPersonShouldNotBeFound("wantsNewsletter.equals=" + UPDATED_WANTS_NEWSLETTER);
    }

    @Test
    @Transactional
    void getAllContactPeopleByWantsNewsletterIsNotEqualToSomething() throws Exception {
        // Initialize the database
        contactPersonRepository.saveAndFlush(contactPerson);

        // Get all the contactPersonList where wantsNewsletter not equals to DEFAULT_WANTS_NEWSLETTER
        defaultContactPersonShouldNotBeFound("wantsNewsletter.notEquals=" + DEFAULT_WANTS_NEWSLETTER);

        // Get all the contactPersonList where wantsNewsletter not equals to UPDATED_WANTS_NEWSLETTER
        defaultContactPersonShouldBeFound("wantsNewsletter.notEquals=" + UPDATED_WANTS_NEWSLETTER);
    }

    @Test
    @Transactional
    void getAllContactPeopleByWantsNewsletterIsInShouldWork() throws Exception {
        // Initialize the database
        contactPersonRepository.saveAndFlush(contactPerson);

        // Get all the contactPersonList where wantsNewsletter in DEFAULT_WANTS_NEWSLETTER or UPDATED_WANTS_NEWSLETTER
        defaultContactPersonShouldBeFound("wantsNewsletter.in=" + DEFAULT_WANTS_NEWSLETTER + "," + UPDATED_WANTS_NEWSLETTER);

        // Get all the contactPersonList where wantsNewsletter equals to UPDATED_WANTS_NEWSLETTER
        defaultContactPersonShouldNotBeFound("wantsNewsletter.in=" + UPDATED_WANTS_NEWSLETTER);
    }

    @Test
    @Transactional
    void getAllContactPeopleByWantsNewsletterIsNullOrNotNull() throws Exception {
        // Initialize the database
        contactPersonRepository.saveAndFlush(contactPerson);

        // Get all the contactPersonList where wantsNewsletter is not null
        defaultContactPersonShouldBeFound("wantsNewsletter.specified=true");

        // Get all the contactPersonList where wantsNewsletter is null
        defaultContactPersonShouldNotBeFound("wantsNewsletter.specified=false");
    }

    @Test
    @Transactional
    void getAllContactPeopleByCustomFieldsIsEqualToSomething() throws Exception {
        // Initialize the database
        contactPersonRepository.saveAndFlush(contactPerson);
        CustomField customFields = CustomFieldResourceIT.createEntity(em);
        em.persist(customFields);
        em.flush();
        contactPerson.addCustomFields(customFields);
        contactPersonRepository.saveAndFlush(contactPerson);
        Long customFieldsId = customFields.getId();

        // Get all the contactPersonList where customFields equals to customFieldsId
        defaultContactPersonShouldBeFound("customFieldsId.equals=" + customFieldsId);

        // Get all the contactPersonList where customFields equals to (customFieldsId + 1)
        defaultContactPersonShouldNotBeFound("customFieldsId.equals=" + (customFieldsId + 1));
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultContactPersonShouldBeFound(String filter) throws Exception {
        restContactPersonMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
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
            .andExpect(jsonPath("$.[*].wantsNewsletter").value(hasItem(DEFAULT_WANTS_NEWSLETTER.booleanValue())));

        // Check, that the count call also returns 1
        restContactPersonMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultContactPersonShouldNotBeFound(String filter) throws Exception {
        restContactPersonMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restContactPersonMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
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
            .wantsNewsletter(UPDATED_WANTS_NEWSLETTER);
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

        // Validate the ContactPerson in Elasticsearch
        verify(mockContactPersonSearchRepository).save(testContactPerson);
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

        // Validate the ContactPerson in Elasticsearch
        verify(mockContactPersonSearchRepository, times(0)).save(contactPerson);
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

        // Validate the ContactPerson in Elasticsearch
        verify(mockContactPersonSearchRepository, times(0)).save(contactPerson);
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

        // Validate the ContactPerson in Elasticsearch
        verify(mockContactPersonSearchRepository, times(0)).save(contactPerson);
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
            .wantsNewsletter(UPDATED_WANTS_NEWSLETTER);

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
            .wantsNewsletter(UPDATED_WANTS_NEWSLETTER);

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

        // Validate the ContactPerson in Elasticsearch
        verify(mockContactPersonSearchRepository, times(0)).save(contactPerson);
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

        // Validate the ContactPerson in Elasticsearch
        verify(mockContactPersonSearchRepository, times(0)).save(contactPerson);
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

        // Validate the ContactPerson in Elasticsearch
        verify(mockContactPersonSearchRepository, times(0)).save(contactPerson);
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

        // Validate the ContactPerson in Elasticsearch
        verify(mockContactPersonSearchRepository, times(1)).deleteById(contactPerson.getId());
    }

    @Test
    @Transactional
    void searchContactPerson() throws Exception {
        // Configure the mock search repository
        // Initialize the database
        contactPersonRepository.saveAndFlush(contactPerson);
        when(mockContactPersonSearchRepository.search(queryStringQuery("id:" + contactPerson.getId())))
            .thenReturn(Collections.singletonList(contactPerson));

        // Search the contactPerson
        restContactPersonMockMvc
            .perform(get(ENTITY_SEARCH_API_URL + "?query=id:" + contactPerson.getId()))
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
            .andExpect(jsonPath("$.[*].wantsNewsletter").value(hasItem(DEFAULT_WANTS_NEWSLETTER.booleanValue())));
    }
}
