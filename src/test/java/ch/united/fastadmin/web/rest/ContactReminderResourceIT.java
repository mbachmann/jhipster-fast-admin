package ch.united.fastadmin.web.rest;

import static ch.united.fastadmin.web.rest.TestUtil.sameInstant;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import ch.united.fastadmin.IntegrationTest;
import ch.united.fastadmin.domain.Contact;
import ch.united.fastadmin.domain.ContactReminder;
import ch.united.fastadmin.domain.enumeration.IntervalType;
import ch.united.fastadmin.repository.ContactReminderRepository;
import ch.united.fastadmin.service.criteria.ContactReminderCriteria;
import ch.united.fastadmin.service.dto.ContactReminderDTO;
import ch.united.fastadmin.service.mapper.ContactReminderMapper;
import java.time.Instant;
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
 * Integration tests for the {@link ContactReminderResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class ContactReminderResourceIT {

    private static final Integer DEFAULT_REMOTE_ID = 1;
    private static final Integer UPDATED_REMOTE_ID = 2;
    private static final Integer SMALLER_REMOTE_ID = 1 - 1;

    private static final String DEFAULT_CONTACT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_CONTACT_NAME = "BBBBBBBBBB";

    private static final ZonedDateTime DEFAULT_DATE_TIME = ZonedDateTime.ofInstant(Instant.ofEpochMilli(0L), ZoneOffset.UTC);
    private static final ZonedDateTime UPDATED_DATE_TIME = ZonedDateTime.now(ZoneId.systemDefault()).withNano(0);
    private static final ZonedDateTime SMALLER_DATE_TIME = ZonedDateTime.ofInstant(Instant.ofEpochMilli(-1L), ZoneOffset.UTC);

    private static final String DEFAULT_TITLE = "AAAAAAAAAA";
    private static final String UPDATED_TITLE = "BBBBBBBBBB";

    private static final String DEFAULT_DESCRIPTION = "AAAAAAAAAA";
    private static final String UPDATED_DESCRIPTION = "BBBBBBBBBB";

    private static final Integer DEFAULT_INTERVAL_VALUE = 1;
    private static final Integer UPDATED_INTERVAL_VALUE = 2;
    private static final Integer SMALLER_INTERVAL_VALUE = 1 - 1;

    private static final IntervalType DEFAULT_INTERVAL_TYPE = IntervalType.HOUR;
    private static final IntervalType UPDATED_INTERVAL_TYPE = IntervalType.DAY;

    private static final Boolean DEFAULT_INACTIV = false;
    private static final Boolean UPDATED_INACTIV = true;

    private static final String ENTITY_API_URL = "/api/contact-reminders";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ContactReminderRepository contactReminderRepository;

    @Autowired
    private ContactReminderMapper contactReminderMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restContactReminderMockMvc;

    private ContactReminder contactReminder;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static ContactReminder createEntity(EntityManager em) {
        ContactReminder contactReminder = new ContactReminder()
            .remoteId(DEFAULT_REMOTE_ID)
            .contactName(DEFAULT_CONTACT_NAME)
            .dateTime(DEFAULT_DATE_TIME)
            .title(DEFAULT_TITLE)
            .description(DEFAULT_DESCRIPTION)
            .intervalValue(DEFAULT_INTERVAL_VALUE)
            .intervalType(DEFAULT_INTERVAL_TYPE)
            .inactiv(DEFAULT_INACTIV);
        return contactReminder;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static ContactReminder createUpdatedEntity(EntityManager em) {
        ContactReminder contactReminder = new ContactReminder()
            .remoteId(UPDATED_REMOTE_ID)
            .contactName(UPDATED_CONTACT_NAME)
            .dateTime(UPDATED_DATE_TIME)
            .title(UPDATED_TITLE)
            .description(UPDATED_DESCRIPTION)
            .intervalValue(UPDATED_INTERVAL_VALUE)
            .intervalType(UPDATED_INTERVAL_TYPE)
            .inactiv(UPDATED_INACTIV);
        return contactReminder;
    }

    @BeforeEach
    public void initTest() {
        contactReminder = createEntity(em);
    }

    @Test
    @Transactional
    void createContactReminder() throws Exception {
        int databaseSizeBeforeCreate = contactReminderRepository.findAll().size();
        // Create the ContactReminder
        ContactReminderDTO contactReminderDTO = contactReminderMapper.toDto(contactReminder);
        restContactReminderMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(contactReminderDTO))
            )
            .andExpect(status().isCreated());

        // Validate the ContactReminder in the database
        List<ContactReminder> contactReminderList = contactReminderRepository.findAll();
        assertThat(contactReminderList).hasSize(databaseSizeBeforeCreate + 1);
        ContactReminder testContactReminder = contactReminderList.get(contactReminderList.size() - 1);
        assertThat(testContactReminder.getRemoteId()).isEqualTo(DEFAULT_REMOTE_ID);
        assertThat(testContactReminder.getContactName()).isEqualTo(DEFAULT_CONTACT_NAME);
        assertThat(testContactReminder.getDateTime()).isEqualTo(DEFAULT_DATE_TIME);
        assertThat(testContactReminder.getTitle()).isEqualTo(DEFAULT_TITLE);
        assertThat(testContactReminder.getDescription()).isEqualTo(DEFAULT_DESCRIPTION);
        assertThat(testContactReminder.getIntervalValue()).isEqualTo(DEFAULT_INTERVAL_VALUE);
        assertThat(testContactReminder.getIntervalType()).isEqualTo(DEFAULT_INTERVAL_TYPE);
        assertThat(testContactReminder.getInactiv()).isEqualTo(DEFAULT_INACTIV);
    }

    @Test
    @Transactional
    void createContactReminderWithExistingId() throws Exception {
        // Create the ContactReminder with an existing ID
        contactReminder.setId(1L);
        ContactReminderDTO contactReminderDTO = contactReminderMapper.toDto(contactReminder);

        int databaseSizeBeforeCreate = contactReminderRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restContactReminderMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(contactReminderDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the ContactReminder in the database
        List<ContactReminder> contactReminderList = contactReminderRepository.findAll();
        assertThat(contactReminderList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllContactReminders() throws Exception {
        // Initialize the database
        contactReminderRepository.saveAndFlush(contactReminder);

        // Get all the contactReminderList
        restContactReminderMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(contactReminder.getId().intValue())))
            .andExpect(jsonPath("$.[*].remoteId").value(hasItem(DEFAULT_REMOTE_ID)))
            .andExpect(jsonPath("$.[*].contactName").value(hasItem(DEFAULT_CONTACT_NAME)))
            .andExpect(jsonPath("$.[*].dateTime").value(hasItem(sameInstant(DEFAULT_DATE_TIME))))
            .andExpect(jsonPath("$.[*].title").value(hasItem(DEFAULT_TITLE)))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION)))
            .andExpect(jsonPath("$.[*].intervalValue").value(hasItem(DEFAULT_INTERVAL_VALUE)))
            .andExpect(jsonPath("$.[*].intervalType").value(hasItem(DEFAULT_INTERVAL_TYPE.toString())))
            .andExpect(jsonPath("$.[*].inactiv").value(hasItem(DEFAULT_INACTIV.booleanValue())));
    }

    @Test
    @Transactional
    void getContactReminder() throws Exception {
        // Initialize the database
        contactReminderRepository.saveAndFlush(contactReminder);

        // Get the contactReminder
        restContactReminderMockMvc
            .perform(get(ENTITY_API_URL_ID, contactReminder.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(contactReminder.getId().intValue()))
            .andExpect(jsonPath("$.remoteId").value(DEFAULT_REMOTE_ID))
            .andExpect(jsonPath("$.contactName").value(DEFAULT_CONTACT_NAME))
            .andExpect(jsonPath("$.dateTime").value(sameInstant(DEFAULT_DATE_TIME)))
            .andExpect(jsonPath("$.title").value(DEFAULT_TITLE))
            .andExpect(jsonPath("$.description").value(DEFAULT_DESCRIPTION))
            .andExpect(jsonPath("$.intervalValue").value(DEFAULT_INTERVAL_VALUE))
            .andExpect(jsonPath("$.intervalType").value(DEFAULT_INTERVAL_TYPE.toString()))
            .andExpect(jsonPath("$.inactiv").value(DEFAULT_INACTIV.booleanValue()));
    }

    @Test
    @Transactional
    void getContactRemindersByIdFiltering() throws Exception {
        // Initialize the database
        contactReminderRepository.saveAndFlush(contactReminder);

        Long id = contactReminder.getId();

        defaultContactReminderShouldBeFound("id.equals=" + id);
        defaultContactReminderShouldNotBeFound("id.notEquals=" + id);

        defaultContactReminderShouldBeFound("id.greaterThanOrEqual=" + id);
        defaultContactReminderShouldNotBeFound("id.greaterThan=" + id);

        defaultContactReminderShouldBeFound("id.lessThanOrEqual=" + id);
        defaultContactReminderShouldNotBeFound("id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllContactRemindersByRemoteIdIsEqualToSomething() throws Exception {
        // Initialize the database
        contactReminderRepository.saveAndFlush(contactReminder);

        // Get all the contactReminderList where remoteId equals to DEFAULT_REMOTE_ID
        defaultContactReminderShouldBeFound("remoteId.equals=" + DEFAULT_REMOTE_ID);

        // Get all the contactReminderList where remoteId equals to UPDATED_REMOTE_ID
        defaultContactReminderShouldNotBeFound("remoteId.equals=" + UPDATED_REMOTE_ID);
    }

    @Test
    @Transactional
    void getAllContactRemindersByRemoteIdIsNotEqualToSomething() throws Exception {
        // Initialize the database
        contactReminderRepository.saveAndFlush(contactReminder);

        // Get all the contactReminderList where remoteId not equals to DEFAULT_REMOTE_ID
        defaultContactReminderShouldNotBeFound("remoteId.notEquals=" + DEFAULT_REMOTE_ID);

        // Get all the contactReminderList where remoteId not equals to UPDATED_REMOTE_ID
        defaultContactReminderShouldBeFound("remoteId.notEquals=" + UPDATED_REMOTE_ID);
    }

    @Test
    @Transactional
    void getAllContactRemindersByRemoteIdIsInShouldWork() throws Exception {
        // Initialize the database
        contactReminderRepository.saveAndFlush(contactReminder);

        // Get all the contactReminderList where remoteId in DEFAULT_REMOTE_ID or UPDATED_REMOTE_ID
        defaultContactReminderShouldBeFound("remoteId.in=" + DEFAULT_REMOTE_ID + "," + UPDATED_REMOTE_ID);

        // Get all the contactReminderList where remoteId equals to UPDATED_REMOTE_ID
        defaultContactReminderShouldNotBeFound("remoteId.in=" + UPDATED_REMOTE_ID);
    }

    @Test
    @Transactional
    void getAllContactRemindersByRemoteIdIsNullOrNotNull() throws Exception {
        // Initialize the database
        contactReminderRepository.saveAndFlush(contactReminder);

        // Get all the contactReminderList where remoteId is not null
        defaultContactReminderShouldBeFound("remoteId.specified=true");

        // Get all the contactReminderList where remoteId is null
        defaultContactReminderShouldNotBeFound("remoteId.specified=false");
    }

    @Test
    @Transactional
    void getAllContactRemindersByRemoteIdIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        contactReminderRepository.saveAndFlush(contactReminder);

        // Get all the contactReminderList where remoteId is greater than or equal to DEFAULT_REMOTE_ID
        defaultContactReminderShouldBeFound("remoteId.greaterThanOrEqual=" + DEFAULT_REMOTE_ID);

        // Get all the contactReminderList where remoteId is greater than or equal to UPDATED_REMOTE_ID
        defaultContactReminderShouldNotBeFound("remoteId.greaterThanOrEqual=" + UPDATED_REMOTE_ID);
    }

    @Test
    @Transactional
    void getAllContactRemindersByRemoteIdIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        contactReminderRepository.saveAndFlush(contactReminder);

        // Get all the contactReminderList where remoteId is less than or equal to DEFAULT_REMOTE_ID
        defaultContactReminderShouldBeFound("remoteId.lessThanOrEqual=" + DEFAULT_REMOTE_ID);

        // Get all the contactReminderList where remoteId is less than or equal to SMALLER_REMOTE_ID
        defaultContactReminderShouldNotBeFound("remoteId.lessThanOrEqual=" + SMALLER_REMOTE_ID);
    }

    @Test
    @Transactional
    void getAllContactRemindersByRemoteIdIsLessThanSomething() throws Exception {
        // Initialize the database
        contactReminderRepository.saveAndFlush(contactReminder);

        // Get all the contactReminderList where remoteId is less than DEFAULT_REMOTE_ID
        defaultContactReminderShouldNotBeFound("remoteId.lessThan=" + DEFAULT_REMOTE_ID);

        // Get all the contactReminderList where remoteId is less than UPDATED_REMOTE_ID
        defaultContactReminderShouldBeFound("remoteId.lessThan=" + UPDATED_REMOTE_ID);
    }

    @Test
    @Transactional
    void getAllContactRemindersByRemoteIdIsGreaterThanSomething() throws Exception {
        // Initialize the database
        contactReminderRepository.saveAndFlush(contactReminder);

        // Get all the contactReminderList where remoteId is greater than DEFAULT_REMOTE_ID
        defaultContactReminderShouldNotBeFound("remoteId.greaterThan=" + DEFAULT_REMOTE_ID);

        // Get all the contactReminderList where remoteId is greater than SMALLER_REMOTE_ID
        defaultContactReminderShouldBeFound("remoteId.greaterThan=" + SMALLER_REMOTE_ID);
    }

    @Test
    @Transactional
    void getAllContactRemindersByContactNameIsEqualToSomething() throws Exception {
        // Initialize the database
        contactReminderRepository.saveAndFlush(contactReminder);

        // Get all the contactReminderList where contactName equals to DEFAULT_CONTACT_NAME
        defaultContactReminderShouldBeFound("contactName.equals=" + DEFAULT_CONTACT_NAME);

        // Get all the contactReminderList where contactName equals to UPDATED_CONTACT_NAME
        defaultContactReminderShouldNotBeFound("contactName.equals=" + UPDATED_CONTACT_NAME);
    }

    @Test
    @Transactional
    void getAllContactRemindersByContactNameIsNotEqualToSomething() throws Exception {
        // Initialize the database
        contactReminderRepository.saveAndFlush(contactReminder);

        // Get all the contactReminderList where contactName not equals to DEFAULT_CONTACT_NAME
        defaultContactReminderShouldNotBeFound("contactName.notEquals=" + DEFAULT_CONTACT_NAME);

        // Get all the contactReminderList where contactName not equals to UPDATED_CONTACT_NAME
        defaultContactReminderShouldBeFound("contactName.notEquals=" + UPDATED_CONTACT_NAME);
    }

    @Test
    @Transactional
    void getAllContactRemindersByContactNameIsInShouldWork() throws Exception {
        // Initialize the database
        contactReminderRepository.saveAndFlush(contactReminder);

        // Get all the contactReminderList where contactName in DEFAULT_CONTACT_NAME or UPDATED_CONTACT_NAME
        defaultContactReminderShouldBeFound("contactName.in=" + DEFAULT_CONTACT_NAME + "," + UPDATED_CONTACT_NAME);

        // Get all the contactReminderList where contactName equals to UPDATED_CONTACT_NAME
        defaultContactReminderShouldNotBeFound("contactName.in=" + UPDATED_CONTACT_NAME);
    }

    @Test
    @Transactional
    void getAllContactRemindersByContactNameIsNullOrNotNull() throws Exception {
        // Initialize the database
        contactReminderRepository.saveAndFlush(contactReminder);

        // Get all the contactReminderList where contactName is not null
        defaultContactReminderShouldBeFound("contactName.specified=true");

        // Get all the contactReminderList where contactName is null
        defaultContactReminderShouldNotBeFound("contactName.specified=false");
    }

    @Test
    @Transactional
    void getAllContactRemindersByContactNameContainsSomething() throws Exception {
        // Initialize the database
        contactReminderRepository.saveAndFlush(contactReminder);

        // Get all the contactReminderList where contactName contains DEFAULT_CONTACT_NAME
        defaultContactReminderShouldBeFound("contactName.contains=" + DEFAULT_CONTACT_NAME);

        // Get all the contactReminderList where contactName contains UPDATED_CONTACT_NAME
        defaultContactReminderShouldNotBeFound("contactName.contains=" + UPDATED_CONTACT_NAME);
    }

    @Test
    @Transactional
    void getAllContactRemindersByContactNameNotContainsSomething() throws Exception {
        // Initialize the database
        contactReminderRepository.saveAndFlush(contactReminder);

        // Get all the contactReminderList where contactName does not contain DEFAULT_CONTACT_NAME
        defaultContactReminderShouldNotBeFound("contactName.doesNotContain=" + DEFAULT_CONTACT_NAME);

        // Get all the contactReminderList where contactName does not contain UPDATED_CONTACT_NAME
        defaultContactReminderShouldBeFound("contactName.doesNotContain=" + UPDATED_CONTACT_NAME);
    }

    @Test
    @Transactional
    void getAllContactRemindersByDateTimeIsEqualToSomething() throws Exception {
        // Initialize the database
        contactReminderRepository.saveAndFlush(contactReminder);

        // Get all the contactReminderList where dateTime equals to DEFAULT_DATE_TIME
        defaultContactReminderShouldBeFound("dateTime.equals=" + DEFAULT_DATE_TIME);

        // Get all the contactReminderList where dateTime equals to UPDATED_DATE_TIME
        defaultContactReminderShouldNotBeFound("dateTime.equals=" + UPDATED_DATE_TIME);
    }

    @Test
    @Transactional
    void getAllContactRemindersByDateTimeIsNotEqualToSomething() throws Exception {
        // Initialize the database
        contactReminderRepository.saveAndFlush(contactReminder);

        // Get all the contactReminderList where dateTime not equals to DEFAULT_DATE_TIME
        defaultContactReminderShouldNotBeFound("dateTime.notEquals=" + DEFAULT_DATE_TIME);

        // Get all the contactReminderList where dateTime not equals to UPDATED_DATE_TIME
        defaultContactReminderShouldBeFound("dateTime.notEquals=" + UPDATED_DATE_TIME);
    }

    @Test
    @Transactional
    void getAllContactRemindersByDateTimeIsInShouldWork() throws Exception {
        // Initialize the database
        contactReminderRepository.saveAndFlush(contactReminder);

        // Get all the contactReminderList where dateTime in DEFAULT_DATE_TIME or UPDATED_DATE_TIME
        defaultContactReminderShouldBeFound("dateTime.in=" + DEFAULT_DATE_TIME + "," + UPDATED_DATE_TIME);

        // Get all the contactReminderList where dateTime equals to UPDATED_DATE_TIME
        defaultContactReminderShouldNotBeFound("dateTime.in=" + UPDATED_DATE_TIME);
    }

    @Test
    @Transactional
    void getAllContactRemindersByDateTimeIsNullOrNotNull() throws Exception {
        // Initialize the database
        contactReminderRepository.saveAndFlush(contactReminder);

        // Get all the contactReminderList where dateTime is not null
        defaultContactReminderShouldBeFound("dateTime.specified=true");

        // Get all the contactReminderList where dateTime is null
        defaultContactReminderShouldNotBeFound("dateTime.specified=false");
    }

    @Test
    @Transactional
    void getAllContactRemindersByDateTimeIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        contactReminderRepository.saveAndFlush(contactReminder);

        // Get all the contactReminderList where dateTime is greater than or equal to DEFAULT_DATE_TIME
        defaultContactReminderShouldBeFound("dateTime.greaterThanOrEqual=" + DEFAULT_DATE_TIME);

        // Get all the contactReminderList where dateTime is greater than or equal to UPDATED_DATE_TIME
        defaultContactReminderShouldNotBeFound("dateTime.greaterThanOrEqual=" + UPDATED_DATE_TIME);
    }

    @Test
    @Transactional
    void getAllContactRemindersByDateTimeIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        contactReminderRepository.saveAndFlush(contactReminder);

        // Get all the contactReminderList where dateTime is less than or equal to DEFAULT_DATE_TIME
        defaultContactReminderShouldBeFound("dateTime.lessThanOrEqual=" + DEFAULT_DATE_TIME);

        // Get all the contactReminderList where dateTime is less than or equal to SMALLER_DATE_TIME
        defaultContactReminderShouldNotBeFound("dateTime.lessThanOrEqual=" + SMALLER_DATE_TIME);
    }

    @Test
    @Transactional
    void getAllContactRemindersByDateTimeIsLessThanSomething() throws Exception {
        // Initialize the database
        contactReminderRepository.saveAndFlush(contactReminder);

        // Get all the contactReminderList where dateTime is less than DEFAULT_DATE_TIME
        defaultContactReminderShouldNotBeFound("dateTime.lessThan=" + DEFAULT_DATE_TIME);

        // Get all the contactReminderList where dateTime is less than UPDATED_DATE_TIME
        defaultContactReminderShouldBeFound("dateTime.lessThan=" + UPDATED_DATE_TIME);
    }

    @Test
    @Transactional
    void getAllContactRemindersByDateTimeIsGreaterThanSomething() throws Exception {
        // Initialize the database
        contactReminderRepository.saveAndFlush(contactReminder);

        // Get all the contactReminderList where dateTime is greater than DEFAULT_DATE_TIME
        defaultContactReminderShouldNotBeFound("dateTime.greaterThan=" + DEFAULT_DATE_TIME);

        // Get all the contactReminderList where dateTime is greater than SMALLER_DATE_TIME
        defaultContactReminderShouldBeFound("dateTime.greaterThan=" + SMALLER_DATE_TIME);
    }

    @Test
    @Transactional
    void getAllContactRemindersByTitleIsEqualToSomething() throws Exception {
        // Initialize the database
        contactReminderRepository.saveAndFlush(contactReminder);

        // Get all the contactReminderList where title equals to DEFAULT_TITLE
        defaultContactReminderShouldBeFound("title.equals=" + DEFAULT_TITLE);

        // Get all the contactReminderList where title equals to UPDATED_TITLE
        defaultContactReminderShouldNotBeFound("title.equals=" + UPDATED_TITLE);
    }

    @Test
    @Transactional
    void getAllContactRemindersByTitleIsNotEqualToSomething() throws Exception {
        // Initialize the database
        contactReminderRepository.saveAndFlush(contactReminder);

        // Get all the contactReminderList where title not equals to DEFAULT_TITLE
        defaultContactReminderShouldNotBeFound("title.notEquals=" + DEFAULT_TITLE);

        // Get all the contactReminderList where title not equals to UPDATED_TITLE
        defaultContactReminderShouldBeFound("title.notEquals=" + UPDATED_TITLE);
    }

    @Test
    @Transactional
    void getAllContactRemindersByTitleIsInShouldWork() throws Exception {
        // Initialize the database
        contactReminderRepository.saveAndFlush(contactReminder);

        // Get all the contactReminderList where title in DEFAULT_TITLE or UPDATED_TITLE
        defaultContactReminderShouldBeFound("title.in=" + DEFAULT_TITLE + "," + UPDATED_TITLE);

        // Get all the contactReminderList where title equals to UPDATED_TITLE
        defaultContactReminderShouldNotBeFound("title.in=" + UPDATED_TITLE);
    }

    @Test
    @Transactional
    void getAllContactRemindersByTitleIsNullOrNotNull() throws Exception {
        // Initialize the database
        contactReminderRepository.saveAndFlush(contactReminder);

        // Get all the contactReminderList where title is not null
        defaultContactReminderShouldBeFound("title.specified=true");

        // Get all the contactReminderList where title is null
        defaultContactReminderShouldNotBeFound("title.specified=false");
    }

    @Test
    @Transactional
    void getAllContactRemindersByTitleContainsSomething() throws Exception {
        // Initialize the database
        contactReminderRepository.saveAndFlush(contactReminder);

        // Get all the contactReminderList where title contains DEFAULT_TITLE
        defaultContactReminderShouldBeFound("title.contains=" + DEFAULT_TITLE);

        // Get all the contactReminderList where title contains UPDATED_TITLE
        defaultContactReminderShouldNotBeFound("title.contains=" + UPDATED_TITLE);
    }

    @Test
    @Transactional
    void getAllContactRemindersByTitleNotContainsSomething() throws Exception {
        // Initialize the database
        contactReminderRepository.saveAndFlush(contactReminder);

        // Get all the contactReminderList where title does not contain DEFAULT_TITLE
        defaultContactReminderShouldNotBeFound("title.doesNotContain=" + DEFAULT_TITLE);

        // Get all the contactReminderList where title does not contain UPDATED_TITLE
        defaultContactReminderShouldBeFound("title.doesNotContain=" + UPDATED_TITLE);
    }

    @Test
    @Transactional
    void getAllContactRemindersByDescriptionIsEqualToSomething() throws Exception {
        // Initialize the database
        contactReminderRepository.saveAndFlush(contactReminder);

        // Get all the contactReminderList where description equals to DEFAULT_DESCRIPTION
        defaultContactReminderShouldBeFound("description.equals=" + DEFAULT_DESCRIPTION);

        // Get all the contactReminderList where description equals to UPDATED_DESCRIPTION
        defaultContactReminderShouldNotBeFound("description.equals=" + UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    void getAllContactRemindersByDescriptionIsNotEqualToSomething() throws Exception {
        // Initialize the database
        contactReminderRepository.saveAndFlush(contactReminder);

        // Get all the contactReminderList where description not equals to DEFAULT_DESCRIPTION
        defaultContactReminderShouldNotBeFound("description.notEquals=" + DEFAULT_DESCRIPTION);

        // Get all the contactReminderList where description not equals to UPDATED_DESCRIPTION
        defaultContactReminderShouldBeFound("description.notEquals=" + UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    void getAllContactRemindersByDescriptionIsInShouldWork() throws Exception {
        // Initialize the database
        contactReminderRepository.saveAndFlush(contactReminder);

        // Get all the contactReminderList where description in DEFAULT_DESCRIPTION or UPDATED_DESCRIPTION
        defaultContactReminderShouldBeFound("description.in=" + DEFAULT_DESCRIPTION + "," + UPDATED_DESCRIPTION);

        // Get all the contactReminderList where description equals to UPDATED_DESCRIPTION
        defaultContactReminderShouldNotBeFound("description.in=" + UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    void getAllContactRemindersByDescriptionIsNullOrNotNull() throws Exception {
        // Initialize the database
        contactReminderRepository.saveAndFlush(contactReminder);

        // Get all the contactReminderList where description is not null
        defaultContactReminderShouldBeFound("description.specified=true");

        // Get all the contactReminderList where description is null
        defaultContactReminderShouldNotBeFound("description.specified=false");
    }

    @Test
    @Transactional
    void getAllContactRemindersByDescriptionContainsSomething() throws Exception {
        // Initialize the database
        contactReminderRepository.saveAndFlush(contactReminder);

        // Get all the contactReminderList where description contains DEFAULT_DESCRIPTION
        defaultContactReminderShouldBeFound("description.contains=" + DEFAULT_DESCRIPTION);

        // Get all the contactReminderList where description contains UPDATED_DESCRIPTION
        defaultContactReminderShouldNotBeFound("description.contains=" + UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    void getAllContactRemindersByDescriptionNotContainsSomething() throws Exception {
        // Initialize the database
        contactReminderRepository.saveAndFlush(contactReminder);

        // Get all the contactReminderList where description does not contain DEFAULT_DESCRIPTION
        defaultContactReminderShouldNotBeFound("description.doesNotContain=" + DEFAULT_DESCRIPTION);

        // Get all the contactReminderList where description does not contain UPDATED_DESCRIPTION
        defaultContactReminderShouldBeFound("description.doesNotContain=" + UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    void getAllContactRemindersByIntervalValueIsEqualToSomething() throws Exception {
        // Initialize the database
        contactReminderRepository.saveAndFlush(contactReminder);

        // Get all the contactReminderList where intervalValue equals to DEFAULT_INTERVAL_VALUE
        defaultContactReminderShouldBeFound("intervalValue.equals=" + DEFAULT_INTERVAL_VALUE);

        // Get all the contactReminderList where intervalValue equals to UPDATED_INTERVAL_VALUE
        defaultContactReminderShouldNotBeFound("intervalValue.equals=" + UPDATED_INTERVAL_VALUE);
    }

    @Test
    @Transactional
    void getAllContactRemindersByIntervalValueIsNotEqualToSomething() throws Exception {
        // Initialize the database
        contactReminderRepository.saveAndFlush(contactReminder);

        // Get all the contactReminderList where intervalValue not equals to DEFAULT_INTERVAL_VALUE
        defaultContactReminderShouldNotBeFound("intervalValue.notEquals=" + DEFAULT_INTERVAL_VALUE);

        // Get all the contactReminderList where intervalValue not equals to UPDATED_INTERVAL_VALUE
        defaultContactReminderShouldBeFound("intervalValue.notEquals=" + UPDATED_INTERVAL_VALUE);
    }

    @Test
    @Transactional
    void getAllContactRemindersByIntervalValueIsInShouldWork() throws Exception {
        // Initialize the database
        contactReminderRepository.saveAndFlush(contactReminder);

        // Get all the contactReminderList where intervalValue in DEFAULT_INTERVAL_VALUE or UPDATED_INTERVAL_VALUE
        defaultContactReminderShouldBeFound("intervalValue.in=" + DEFAULT_INTERVAL_VALUE + "," + UPDATED_INTERVAL_VALUE);

        // Get all the contactReminderList where intervalValue equals to UPDATED_INTERVAL_VALUE
        defaultContactReminderShouldNotBeFound("intervalValue.in=" + UPDATED_INTERVAL_VALUE);
    }

    @Test
    @Transactional
    void getAllContactRemindersByIntervalValueIsNullOrNotNull() throws Exception {
        // Initialize the database
        contactReminderRepository.saveAndFlush(contactReminder);

        // Get all the contactReminderList where intervalValue is not null
        defaultContactReminderShouldBeFound("intervalValue.specified=true");

        // Get all the contactReminderList where intervalValue is null
        defaultContactReminderShouldNotBeFound("intervalValue.specified=false");
    }

    @Test
    @Transactional
    void getAllContactRemindersByIntervalValueIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        contactReminderRepository.saveAndFlush(contactReminder);

        // Get all the contactReminderList where intervalValue is greater than or equal to DEFAULT_INTERVAL_VALUE
        defaultContactReminderShouldBeFound("intervalValue.greaterThanOrEqual=" + DEFAULT_INTERVAL_VALUE);

        // Get all the contactReminderList where intervalValue is greater than or equal to UPDATED_INTERVAL_VALUE
        defaultContactReminderShouldNotBeFound("intervalValue.greaterThanOrEqual=" + UPDATED_INTERVAL_VALUE);
    }

    @Test
    @Transactional
    void getAllContactRemindersByIntervalValueIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        contactReminderRepository.saveAndFlush(contactReminder);

        // Get all the contactReminderList where intervalValue is less than or equal to DEFAULT_INTERVAL_VALUE
        defaultContactReminderShouldBeFound("intervalValue.lessThanOrEqual=" + DEFAULT_INTERVAL_VALUE);

        // Get all the contactReminderList where intervalValue is less than or equal to SMALLER_INTERVAL_VALUE
        defaultContactReminderShouldNotBeFound("intervalValue.lessThanOrEqual=" + SMALLER_INTERVAL_VALUE);
    }

    @Test
    @Transactional
    void getAllContactRemindersByIntervalValueIsLessThanSomething() throws Exception {
        // Initialize the database
        contactReminderRepository.saveAndFlush(contactReminder);

        // Get all the contactReminderList where intervalValue is less than DEFAULT_INTERVAL_VALUE
        defaultContactReminderShouldNotBeFound("intervalValue.lessThan=" + DEFAULT_INTERVAL_VALUE);

        // Get all the contactReminderList where intervalValue is less than UPDATED_INTERVAL_VALUE
        defaultContactReminderShouldBeFound("intervalValue.lessThan=" + UPDATED_INTERVAL_VALUE);
    }

    @Test
    @Transactional
    void getAllContactRemindersByIntervalValueIsGreaterThanSomething() throws Exception {
        // Initialize the database
        contactReminderRepository.saveAndFlush(contactReminder);

        // Get all the contactReminderList where intervalValue is greater than DEFAULT_INTERVAL_VALUE
        defaultContactReminderShouldNotBeFound("intervalValue.greaterThan=" + DEFAULT_INTERVAL_VALUE);

        // Get all the contactReminderList where intervalValue is greater than SMALLER_INTERVAL_VALUE
        defaultContactReminderShouldBeFound("intervalValue.greaterThan=" + SMALLER_INTERVAL_VALUE);
    }

    @Test
    @Transactional
    void getAllContactRemindersByIntervalTypeIsEqualToSomething() throws Exception {
        // Initialize the database
        contactReminderRepository.saveAndFlush(contactReminder);

        // Get all the contactReminderList where intervalType equals to DEFAULT_INTERVAL_TYPE
        defaultContactReminderShouldBeFound("intervalType.equals=" + DEFAULT_INTERVAL_TYPE);

        // Get all the contactReminderList where intervalType equals to UPDATED_INTERVAL_TYPE
        defaultContactReminderShouldNotBeFound("intervalType.equals=" + UPDATED_INTERVAL_TYPE);
    }

    @Test
    @Transactional
    void getAllContactRemindersByIntervalTypeIsNotEqualToSomething() throws Exception {
        // Initialize the database
        contactReminderRepository.saveAndFlush(contactReminder);

        // Get all the contactReminderList where intervalType not equals to DEFAULT_INTERVAL_TYPE
        defaultContactReminderShouldNotBeFound("intervalType.notEquals=" + DEFAULT_INTERVAL_TYPE);

        // Get all the contactReminderList where intervalType not equals to UPDATED_INTERVAL_TYPE
        defaultContactReminderShouldBeFound("intervalType.notEquals=" + UPDATED_INTERVAL_TYPE);
    }

    @Test
    @Transactional
    void getAllContactRemindersByIntervalTypeIsInShouldWork() throws Exception {
        // Initialize the database
        contactReminderRepository.saveAndFlush(contactReminder);

        // Get all the contactReminderList where intervalType in DEFAULT_INTERVAL_TYPE or UPDATED_INTERVAL_TYPE
        defaultContactReminderShouldBeFound("intervalType.in=" + DEFAULT_INTERVAL_TYPE + "," + UPDATED_INTERVAL_TYPE);

        // Get all the contactReminderList where intervalType equals to UPDATED_INTERVAL_TYPE
        defaultContactReminderShouldNotBeFound("intervalType.in=" + UPDATED_INTERVAL_TYPE);
    }

    @Test
    @Transactional
    void getAllContactRemindersByIntervalTypeIsNullOrNotNull() throws Exception {
        // Initialize the database
        contactReminderRepository.saveAndFlush(contactReminder);

        // Get all the contactReminderList where intervalType is not null
        defaultContactReminderShouldBeFound("intervalType.specified=true");

        // Get all the contactReminderList where intervalType is null
        defaultContactReminderShouldNotBeFound("intervalType.specified=false");
    }

    @Test
    @Transactional
    void getAllContactRemindersByInactivIsEqualToSomething() throws Exception {
        // Initialize the database
        contactReminderRepository.saveAndFlush(contactReminder);

        // Get all the contactReminderList where inactiv equals to DEFAULT_INACTIV
        defaultContactReminderShouldBeFound("inactiv.equals=" + DEFAULT_INACTIV);

        // Get all the contactReminderList where inactiv equals to UPDATED_INACTIV
        defaultContactReminderShouldNotBeFound("inactiv.equals=" + UPDATED_INACTIV);
    }

    @Test
    @Transactional
    void getAllContactRemindersByInactivIsNotEqualToSomething() throws Exception {
        // Initialize the database
        contactReminderRepository.saveAndFlush(contactReminder);

        // Get all the contactReminderList where inactiv not equals to DEFAULT_INACTIV
        defaultContactReminderShouldNotBeFound("inactiv.notEquals=" + DEFAULT_INACTIV);

        // Get all the contactReminderList where inactiv not equals to UPDATED_INACTIV
        defaultContactReminderShouldBeFound("inactiv.notEquals=" + UPDATED_INACTIV);
    }

    @Test
    @Transactional
    void getAllContactRemindersByInactivIsInShouldWork() throws Exception {
        // Initialize the database
        contactReminderRepository.saveAndFlush(contactReminder);

        // Get all the contactReminderList where inactiv in DEFAULT_INACTIV or UPDATED_INACTIV
        defaultContactReminderShouldBeFound("inactiv.in=" + DEFAULT_INACTIV + "," + UPDATED_INACTIV);

        // Get all the contactReminderList where inactiv equals to UPDATED_INACTIV
        defaultContactReminderShouldNotBeFound("inactiv.in=" + UPDATED_INACTIV);
    }

    @Test
    @Transactional
    void getAllContactRemindersByInactivIsNullOrNotNull() throws Exception {
        // Initialize the database
        contactReminderRepository.saveAndFlush(contactReminder);

        // Get all the contactReminderList where inactiv is not null
        defaultContactReminderShouldBeFound("inactiv.specified=true");

        // Get all the contactReminderList where inactiv is null
        defaultContactReminderShouldNotBeFound("inactiv.specified=false");
    }

    @Test
    @Transactional
    void getAllContactRemindersByContactIsEqualToSomething() throws Exception {
        // Initialize the database
        contactReminderRepository.saveAndFlush(contactReminder);
        Contact contact = ContactResourceIT.createEntity(em);
        em.persist(contact);
        em.flush();
        contactReminder.setContact(contact);
        contactReminderRepository.saveAndFlush(contactReminder);
        Long contactId = contact.getId();

        // Get all the contactReminderList where contact equals to contactId
        defaultContactReminderShouldBeFound("contactId.equals=" + contactId);

        // Get all the contactReminderList where contact equals to (contactId + 1)
        defaultContactReminderShouldNotBeFound("contactId.equals=" + (contactId + 1));
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultContactReminderShouldBeFound(String filter) throws Exception {
        restContactReminderMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(contactReminder.getId().intValue())))
            .andExpect(jsonPath("$.[*].remoteId").value(hasItem(DEFAULT_REMOTE_ID)))
            .andExpect(jsonPath("$.[*].contactName").value(hasItem(DEFAULT_CONTACT_NAME)))
            .andExpect(jsonPath("$.[*].dateTime").value(hasItem(sameInstant(DEFAULT_DATE_TIME))))
            .andExpect(jsonPath("$.[*].title").value(hasItem(DEFAULT_TITLE)))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION)))
            .andExpect(jsonPath("$.[*].intervalValue").value(hasItem(DEFAULT_INTERVAL_VALUE)))
            .andExpect(jsonPath("$.[*].intervalType").value(hasItem(DEFAULT_INTERVAL_TYPE.toString())))
            .andExpect(jsonPath("$.[*].inactiv").value(hasItem(DEFAULT_INACTIV.booleanValue())));

        // Check, that the count call also returns 1
        restContactReminderMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultContactReminderShouldNotBeFound(String filter) throws Exception {
        restContactReminderMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restContactReminderMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingContactReminder() throws Exception {
        // Get the contactReminder
        restContactReminderMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewContactReminder() throws Exception {
        // Initialize the database
        contactReminderRepository.saveAndFlush(contactReminder);

        int databaseSizeBeforeUpdate = contactReminderRepository.findAll().size();

        // Update the contactReminder
        ContactReminder updatedContactReminder = contactReminderRepository.findById(contactReminder.getId()).get();
        // Disconnect from session so that the updates on updatedContactReminder are not directly saved in db
        em.detach(updatedContactReminder);
        updatedContactReminder
            .remoteId(UPDATED_REMOTE_ID)
            .contactName(UPDATED_CONTACT_NAME)
            .dateTime(UPDATED_DATE_TIME)
            .title(UPDATED_TITLE)
            .description(UPDATED_DESCRIPTION)
            .intervalValue(UPDATED_INTERVAL_VALUE)
            .intervalType(UPDATED_INTERVAL_TYPE)
            .inactiv(UPDATED_INACTIV);
        ContactReminderDTO contactReminderDTO = contactReminderMapper.toDto(updatedContactReminder);

        restContactReminderMockMvc
            .perform(
                put(ENTITY_API_URL_ID, contactReminderDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(contactReminderDTO))
            )
            .andExpect(status().isOk());

        // Validate the ContactReminder in the database
        List<ContactReminder> contactReminderList = contactReminderRepository.findAll();
        assertThat(contactReminderList).hasSize(databaseSizeBeforeUpdate);
        ContactReminder testContactReminder = contactReminderList.get(contactReminderList.size() - 1);
        assertThat(testContactReminder.getRemoteId()).isEqualTo(UPDATED_REMOTE_ID);
        assertThat(testContactReminder.getContactName()).isEqualTo(UPDATED_CONTACT_NAME);
        assertThat(testContactReminder.getDateTime()).isEqualTo(UPDATED_DATE_TIME);
        assertThat(testContactReminder.getTitle()).isEqualTo(UPDATED_TITLE);
        assertThat(testContactReminder.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
        assertThat(testContactReminder.getIntervalValue()).isEqualTo(UPDATED_INTERVAL_VALUE);
        assertThat(testContactReminder.getIntervalType()).isEqualTo(UPDATED_INTERVAL_TYPE);
        assertThat(testContactReminder.getInactiv()).isEqualTo(UPDATED_INACTIV);
    }

    @Test
    @Transactional
    void putNonExistingContactReminder() throws Exception {
        int databaseSizeBeforeUpdate = contactReminderRepository.findAll().size();
        contactReminder.setId(count.incrementAndGet());

        // Create the ContactReminder
        ContactReminderDTO contactReminderDTO = contactReminderMapper.toDto(contactReminder);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restContactReminderMockMvc
            .perform(
                put(ENTITY_API_URL_ID, contactReminderDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(contactReminderDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the ContactReminder in the database
        List<ContactReminder> contactReminderList = contactReminderRepository.findAll();
        assertThat(contactReminderList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchContactReminder() throws Exception {
        int databaseSizeBeforeUpdate = contactReminderRepository.findAll().size();
        contactReminder.setId(count.incrementAndGet());

        // Create the ContactReminder
        ContactReminderDTO contactReminderDTO = contactReminderMapper.toDto(contactReminder);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restContactReminderMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(contactReminderDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the ContactReminder in the database
        List<ContactReminder> contactReminderList = contactReminderRepository.findAll();
        assertThat(contactReminderList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamContactReminder() throws Exception {
        int databaseSizeBeforeUpdate = contactReminderRepository.findAll().size();
        contactReminder.setId(count.incrementAndGet());

        // Create the ContactReminder
        ContactReminderDTO contactReminderDTO = contactReminderMapper.toDto(contactReminder);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restContactReminderMockMvc
            .perform(
                put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(contactReminderDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the ContactReminder in the database
        List<ContactReminder> contactReminderList = contactReminderRepository.findAll();
        assertThat(contactReminderList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateContactReminderWithPatch() throws Exception {
        // Initialize the database
        contactReminderRepository.saveAndFlush(contactReminder);

        int databaseSizeBeforeUpdate = contactReminderRepository.findAll().size();

        // Update the contactReminder using partial update
        ContactReminder partialUpdatedContactReminder = new ContactReminder();
        partialUpdatedContactReminder.setId(contactReminder.getId());

        partialUpdatedContactReminder.title(UPDATED_TITLE).intervalValue(UPDATED_INTERVAL_VALUE).intervalType(UPDATED_INTERVAL_TYPE);

        restContactReminderMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedContactReminder.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedContactReminder))
            )
            .andExpect(status().isOk());

        // Validate the ContactReminder in the database
        List<ContactReminder> contactReminderList = contactReminderRepository.findAll();
        assertThat(contactReminderList).hasSize(databaseSizeBeforeUpdate);
        ContactReminder testContactReminder = contactReminderList.get(contactReminderList.size() - 1);
        assertThat(testContactReminder.getRemoteId()).isEqualTo(DEFAULT_REMOTE_ID);
        assertThat(testContactReminder.getContactName()).isEqualTo(DEFAULT_CONTACT_NAME);
        assertThat(testContactReminder.getDateTime()).isEqualTo(DEFAULT_DATE_TIME);
        assertThat(testContactReminder.getTitle()).isEqualTo(UPDATED_TITLE);
        assertThat(testContactReminder.getDescription()).isEqualTo(DEFAULT_DESCRIPTION);
        assertThat(testContactReminder.getIntervalValue()).isEqualTo(UPDATED_INTERVAL_VALUE);
        assertThat(testContactReminder.getIntervalType()).isEqualTo(UPDATED_INTERVAL_TYPE);
        assertThat(testContactReminder.getInactiv()).isEqualTo(DEFAULT_INACTIV);
    }

    @Test
    @Transactional
    void fullUpdateContactReminderWithPatch() throws Exception {
        // Initialize the database
        contactReminderRepository.saveAndFlush(contactReminder);

        int databaseSizeBeforeUpdate = contactReminderRepository.findAll().size();

        // Update the contactReminder using partial update
        ContactReminder partialUpdatedContactReminder = new ContactReminder();
        partialUpdatedContactReminder.setId(contactReminder.getId());

        partialUpdatedContactReminder
            .remoteId(UPDATED_REMOTE_ID)
            .contactName(UPDATED_CONTACT_NAME)
            .dateTime(UPDATED_DATE_TIME)
            .title(UPDATED_TITLE)
            .description(UPDATED_DESCRIPTION)
            .intervalValue(UPDATED_INTERVAL_VALUE)
            .intervalType(UPDATED_INTERVAL_TYPE)
            .inactiv(UPDATED_INACTIV);

        restContactReminderMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedContactReminder.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedContactReminder))
            )
            .andExpect(status().isOk());

        // Validate the ContactReminder in the database
        List<ContactReminder> contactReminderList = contactReminderRepository.findAll();
        assertThat(contactReminderList).hasSize(databaseSizeBeforeUpdate);
        ContactReminder testContactReminder = contactReminderList.get(contactReminderList.size() - 1);
        assertThat(testContactReminder.getRemoteId()).isEqualTo(UPDATED_REMOTE_ID);
        assertThat(testContactReminder.getContactName()).isEqualTo(UPDATED_CONTACT_NAME);
        assertThat(testContactReminder.getDateTime()).isEqualTo(UPDATED_DATE_TIME);
        assertThat(testContactReminder.getTitle()).isEqualTo(UPDATED_TITLE);
        assertThat(testContactReminder.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
        assertThat(testContactReminder.getIntervalValue()).isEqualTo(UPDATED_INTERVAL_VALUE);
        assertThat(testContactReminder.getIntervalType()).isEqualTo(UPDATED_INTERVAL_TYPE);
        assertThat(testContactReminder.getInactiv()).isEqualTo(UPDATED_INACTIV);
    }

    @Test
    @Transactional
    void patchNonExistingContactReminder() throws Exception {
        int databaseSizeBeforeUpdate = contactReminderRepository.findAll().size();
        contactReminder.setId(count.incrementAndGet());

        // Create the ContactReminder
        ContactReminderDTO contactReminderDTO = contactReminderMapper.toDto(contactReminder);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restContactReminderMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, contactReminderDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(contactReminderDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the ContactReminder in the database
        List<ContactReminder> contactReminderList = contactReminderRepository.findAll();
        assertThat(contactReminderList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchContactReminder() throws Exception {
        int databaseSizeBeforeUpdate = contactReminderRepository.findAll().size();
        contactReminder.setId(count.incrementAndGet());

        // Create the ContactReminder
        ContactReminderDTO contactReminderDTO = contactReminderMapper.toDto(contactReminder);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restContactReminderMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(contactReminderDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the ContactReminder in the database
        List<ContactReminder> contactReminderList = contactReminderRepository.findAll();
        assertThat(contactReminderList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamContactReminder() throws Exception {
        int databaseSizeBeforeUpdate = contactReminderRepository.findAll().size();
        contactReminder.setId(count.incrementAndGet());

        // Create the ContactReminder
        ContactReminderDTO contactReminderDTO = contactReminderMapper.toDto(contactReminder);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restContactReminderMockMvc
            .perform(
                patch(ENTITY_API_URL)
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(contactReminderDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the ContactReminder in the database
        List<ContactReminder> contactReminderList = contactReminderRepository.findAll();
        assertThat(contactReminderList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteContactReminder() throws Exception {
        // Initialize the database
        contactReminderRepository.saveAndFlush(contactReminder);

        int databaseSizeBeforeDelete = contactReminderRepository.findAll().size();

        // Delete the contactReminder
        restContactReminderMockMvc
            .perform(delete(ENTITY_API_URL_ID, contactReminder.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<ContactReminder> contactReminderList = contactReminderRepository.findAll();
        assertThat(contactReminderList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
