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
import ch.united.fastadmin.domain.DocumentFreeText;
import ch.united.fastadmin.domain.DocumentLetter;
import ch.united.fastadmin.domain.Layout;
import ch.united.fastadmin.domain.Signature;
import ch.united.fastadmin.domain.enumeration.DocumentLanguage;
import ch.united.fastadmin.domain.enumeration.LetterStatus;
import ch.united.fastadmin.repository.DocumentLetterRepository;
import ch.united.fastadmin.service.criteria.DocumentLetterCriteria;
import ch.united.fastadmin.service.dto.DocumentLetterDTO;
import ch.united.fastadmin.service.mapper.DocumentLetterMapper;
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
 * Integration tests for the {@link DocumentLetterResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class DocumentLetterResourceIT {

    private static final Integer DEFAULT_REMOTE_ID = 1;
    private static final Integer UPDATED_REMOTE_ID = 2;
    private static final Integer SMALLER_REMOTE_ID = 1 - 1;

    private static final String DEFAULT_CONTACT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_CONTACT_NAME = "BBBBBBBBBB";

    private static final LocalDate DEFAULT_DATE = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_DATE = LocalDate.now(ZoneId.systemDefault());
    private static final LocalDate SMALLER_DATE = LocalDate.ofEpochDay(-1L);

    private static final String DEFAULT_TITLE = "AAAAAAAAAA";
    private static final String UPDATED_TITLE = "BBBBBBBBBB";

    private static final String DEFAULT_CONTENT = "AAAAAAAAAA";
    private static final String UPDATED_CONTENT = "BBBBBBBBBB";

    private static final DocumentLanguage DEFAULT_LANGUAGE = DocumentLanguage.FRENCH;
    private static final DocumentLanguage UPDATED_LANGUAGE = DocumentLanguage.ENGLISH;

    private static final Integer DEFAULT_PAGE_AMOUNT = 1;
    private static final Integer UPDATED_PAGE_AMOUNT = 2;
    private static final Integer SMALLER_PAGE_AMOUNT = 1 - 1;

    private static final String DEFAULT_NOTES = "AAAAAAAAAA";
    private static final String UPDATED_NOTES = "BBBBBBBBBB";

    private static final LetterStatus DEFAULT_STATUS = LetterStatus.DRAFT;
    private static final LetterStatus UPDATED_STATUS = LetterStatus.SENT;

    private static final ZonedDateTime DEFAULT_CREATED = ZonedDateTime.ofInstant(Instant.ofEpochMilli(0L), ZoneOffset.UTC);
    private static final ZonedDateTime UPDATED_CREATED = ZonedDateTime.now(ZoneId.systemDefault()).withNano(0);
    private static final ZonedDateTime SMALLER_CREATED = ZonedDateTime.ofInstant(Instant.ofEpochMilli(-1L), ZoneOffset.UTC);

    private static final String ENTITY_API_URL = "/api/document-letters";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private DocumentLetterRepository documentLetterRepository;

    @Autowired
    private DocumentLetterMapper documentLetterMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restDocumentLetterMockMvc;

    private DocumentLetter documentLetter;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static DocumentLetter createEntity(EntityManager em) {
        DocumentLetter documentLetter = new DocumentLetter()
            .remoteId(DEFAULT_REMOTE_ID)
            .contactName(DEFAULT_CONTACT_NAME)
            .date(DEFAULT_DATE)
            .title(DEFAULT_TITLE)
            .content(DEFAULT_CONTENT)
            .language(DEFAULT_LANGUAGE)
            .pageAmount(DEFAULT_PAGE_AMOUNT)
            .notes(DEFAULT_NOTES)
            .status(DEFAULT_STATUS)
            .created(DEFAULT_CREATED);
        return documentLetter;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static DocumentLetter createUpdatedEntity(EntityManager em) {
        DocumentLetter documentLetter = new DocumentLetter()
            .remoteId(UPDATED_REMOTE_ID)
            .contactName(UPDATED_CONTACT_NAME)
            .date(UPDATED_DATE)
            .title(UPDATED_TITLE)
            .content(UPDATED_CONTENT)
            .language(UPDATED_LANGUAGE)
            .pageAmount(UPDATED_PAGE_AMOUNT)
            .notes(UPDATED_NOTES)
            .status(UPDATED_STATUS)
            .created(UPDATED_CREATED);
        return documentLetter;
    }

    @BeforeEach
    public void initTest() {
        documentLetter = createEntity(em);
    }

    @Test
    @Transactional
    void createDocumentLetter() throws Exception {
        int databaseSizeBeforeCreate = documentLetterRepository.findAll().size();
        // Create the DocumentLetter
        DocumentLetterDTO documentLetterDTO = documentLetterMapper.toDto(documentLetter);
        restDocumentLetterMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(documentLetterDTO))
            )
            .andExpect(status().isCreated());

        // Validate the DocumentLetter in the database
        List<DocumentLetter> documentLetterList = documentLetterRepository.findAll();
        assertThat(documentLetterList).hasSize(databaseSizeBeforeCreate + 1);
        DocumentLetter testDocumentLetter = documentLetterList.get(documentLetterList.size() - 1);
        assertThat(testDocumentLetter.getRemoteId()).isEqualTo(DEFAULT_REMOTE_ID);
        assertThat(testDocumentLetter.getContactName()).isEqualTo(DEFAULT_CONTACT_NAME);
        assertThat(testDocumentLetter.getDate()).isEqualTo(DEFAULT_DATE);
        assertThat(testDocumentLetter.getTitle()).isEqualTo(DEFAULT_TITLE);
        assertThat(testDocumentLetter.getContent()).isEqualTo(DEFAULT_CONTENT);
        assertThat(testDocumentLetter.getLanguage()).isEqualTo(DEFAULT_LANGUAGE);
        assertThat(testDocumentLetter.getPageAmount()).isEqualTo(DEFAULT_PAGE_AMOUNT);
        assertThat(testDocumentLetter.getNotes()).isEqualTo(DEFAULT_NOTES);
        assertThat(testDocumentLetter.getStatus()).isEqualTo(DEFAULT_STATUS);
        assertThat(testDocumentLetter.getCreated()).isEqualTo(DEFAULT_CREATED);
    }

    @Test
    @Transactional
    void createDocumentLetterWithExistingId() throws Exception {
        // Create the DocumentLetter with an existing ID
        documentLetter.setId(1L);
        DocumentLetterDTO documentLetterDTO = documentLetterMapper.toDto(documentLetter);

        int databaseSizeBeforeCreate = documentLetterRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restDocumentLetterMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(documentLetterDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the DocumentLetter in the database
        List<DocumentLetter> documentLetterList = documentLetterRepository.findAll();
        assertThat(documentLetterList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllDocumentLetters() throws Exception {
        // Initialize the database
        documentLetterRepository.saveAndFlush(documentLetter);

        // Get all the documentLetterList
        restDocumentLetterMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(documentLetter.getId().intValue())))
            .andExpect(jsonPath("$.[*].remoteId").value(hasItem(DEFAULT_REMOTE_ID)))
            .andExpect(jsonPath("$.[*].contactName").value(hasItem(DEFAULT_CONTACT_NAME)))
            .andExpect(jsonPath("$.[*].date").value(hasItem(DEFAULT_DATE.toString())))
            .andExpect(jsonPath("$.[*].title").value(hasItem(DEFAULT_TITLE)))
            .andExpect(jsonPath("$.[*].content").value(hasItem(DEFAULT_CONTENT)))
            .andExpect(jsonPath("$.[*].language").value(hasItem(DEFAULT_LANGUAGE.toString())))
            .andExpect(jsonPath("$.[*].pageAmount").value(hasItem(DEFAULT_PAGE_AMOUNT)))
            .andExpect(jsonPath("$.[*].notes").value(hasItem(DEFAULT_NOTES)))
            .andExpect(jsonPath("$.[*].status").value(hasItem(DEFAULT_STATUS.toString())))
            .andExpect(jsonPath("$.[*].created").value(hasItem(sameInstant(DEFAULT_CREATED))));
    }

    @Test
    @Transactional
    void getDocumentLetter() throws Exception {
        // Initialize the database
        documentLetterRepository.saveAndFlush(documentLetter);

        // Get the documentLetter
        restDocumentLetterMockMvc
            .perform(get(ENTITY_API_URL_ID, documentLetter.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(documentLetter.getId().intValue()))
            .andExpect(jsonPath("$.remoteId").value(DEFAULT_REMOTE_ID))
            .andExpect(jsonPath("$.contactName").value(DEFAULT_CONTACT_NAME))
            .andExpect(jsonPath("$.date").value(DEFAULT_DATE.toString()))
            .andExpect(jsonPath("$.title").value(DEFAULT_TITLE))
            .andExpect(jsonPath("$.content").value(DEFAULT_CONTENT))
            .andExpect(jsonPath("$.language").value(DEFAULT_LANGUAGE.toString()))
            .andExpect(jsonPath("$.pageAmount").value(DEFAULT_PAGE_AMOUNT))
            .andExpect(jsonPath("$.notes").value(DEFAULT_NOTES))
            .andExpect(jsonPath("$.status").value(DEFAULT_STATUS.toString()))
            .andExpect(jsonPath("$.created").value(sameInstant(DEFAULT_CREATED)));
    }

    @Test
    @Transactional
    void getDocumentLettersByIdFiltering() throws Exception {
        // Initialize the database
        documentLetterRepository.saveAndFlush(documentLetter);

        Long id = documentLetter.getId();

        defaultDocumentLetterShouldBeFound("id.equals=" + id);
        defaultDocumentLetterShouldNotBeFound("id.notEquals=" + id);

        defaultDocumentLetterShouldBeFound("id.greaterThanOrEqual=" + id);
        defaultDocumentLetterShouldNotBeFound("id.greaterThan=" + id);

        defaultDocumentLetterShouldBeFound("id.lessThanOrEqual=" + id);
        defaultDocumentLetterShouldNotBeFound("id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllDocumentLettersByRemoteIdIsEqualToSomething() throws Exception {
        // Initialize the database
        documentLetterRepository.saveAndFlush(documentLetter);

        // Get all the documentLetterList where remoteId equals to DEFAULT_REMOTE_ID
        defaultDocumentLetterShouldBeFound("remoteId.equals=" + DEFAULT_REMOTE_ID);

        // Get all the documentLetterList where remoteId equals to UPDATED_REMOTE_ID
        defaultDocumentLetterShouldNotBeFound("remoteId.equals=" + UPDATED_REMOTE_ID);
    }

    @Test
    @Transactional
    void getAllDocumentLettersByRemoteIdIsNotEqualToSomething() throws Exception {
        // Initialize the database
        documentLetterRepository.saveAndFlush(documentLetter);

        // Get all the documentLetterList where remoteId not equals to DEFAULT_REMOTE_ID
        defaultDocumentLetterShouldNotBeFound("remoteId.notEquals=" + DEFAULT_REMOTE_ID);

        // Get all the documentLetterList where remoteId not equals to UPDATED_REMOTE_ID
        defaultDocumentLetterShouldBeFound("remoteId.notEquals=" + UPDATED_REMOTE_ID);
    }

    @Test
    @Transactional
    void getAllDocumentLettersByRemoteIdIsInShouldWork() throws Exception {
        // Initialize the database
        documentLetterRepository.saveAndFlush(documentLetter);

        // Get all the documentLetterList where remoteId in DEFAULT_REMOTE_ID or UPDATED_REMOTE_ID
        defaultDocumentLetterShouldBeFound("remoteId.in=" + DEFAULT_REMOTE_ID + "," + UPDATED_REMOTE_ID);

        // Get all the documentLetterList where remoteId equals to UPDATED_REMOTE_ID
        defaultDocumentLetterShouldNotBeFound("remoteId.in=" + UPDATED_REMOTE_ID);
    }

    @Test
    @Transactional
    void getAllDocumentLettersByRemoteIdIsNullOrNotNull() throws Exception {
        // Initialize the database
        documentLetterRepository.saveAndFlush(documentLetter);

        // Get all the documentLetterList where remoteId is not null
        defaultDocumentLetterShouldBeFound("remoteId.specified=true");

        // Get all the documentLetterList where remoteId is null
        defaultDocumentLetterShouldNotBeFound("remoteId.specified=false");
    }

    @Test
    @Transactional
    void getAllDocumentLettersByRemoteIdIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        documentLetterRepository.saveAndFlush(documentLetter);

        // Get all the documentLetterList where remoteId is greater than or equal to DEFAULT_REMOTE_ID
        defaultDocumentLetterShouldBeFound("remoteId.greaterThanOrEqual=" + DEFAULT_REMOTE_ID);

        // Get all the documentLetterList where remoteId is greater than or equal to UPDATED_REMOTE_ID
        defaultDocumentLetterShouldNotBeFound("remoteId.greaterThanOrEqual=" + UPDATED_REMOTE_ID);
    }

    @Test
    @Transactional
    void getAllDocumentLettersByRemoteIdIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        documentLetterRepository.saveAndFlush(documentLetter);

        // Get all the documentLetterList where remoteId is less than or equal to DEFAULT_REMOTE_ID
        defaultDocumentLetterShouldBeFound("remoteId.lessThanOrEqual=" + DEFAULT_REMOTE_ID);

        // Get all the documentLetterList where remoteId is less than or equal to SMALLER_REMOTE_ID
        defaultDocumentLetterShouldNotBeFound("remoteId.lessThanOrEqual=" + SMALLER_REMOTE_ID);
    }

    @Test
    @Transactional
    void getAllDocumentLettersByRemoteIdIsLessThanSomething() throws Exception {
        // Initialize the database
        documentLetterRepository.saveAndFlush(documentLetter);

        // Get all the documentLetterList where remoteId is less than DEFAULT_REMOTE_ID
        defaultDocumentLetterShouldNotBeFound("remoteId.lessThan=" + DEFAULT_REMOTE_ID);

        // Get all the documentLetterList where remoteId is less than UPDATED_REMOTE_ID
        defaultDocumentLetterShouldBeFound("remoteId.lessThan=" + UPDATED_REMOTE_ID);
    }

    @Test
    @Transactional
    void getAllDocumentLettersByRemoteIdIsGreaterThanSomething() throws Exception {
        // Initialize the database
        documentLetterRepository.saveAndFlush(documentLetter);

        // Get all the documentLetterList where remoteId is greater than DEFAULT_REMOTE_ID
        defaultDocumentLetterShouldNotBeFound("remoteId.greaterThan=" + DEFAULT_REMOTE_ID);

        // Get all the documentLetterList where remoteId is greater than SMALLER_REMOTE_ID
        defaultDocumentLetterShouldBeFound("remoteId.greaterThan=" + SMALLER_REMOTE_ID);
    }

    @Test
    @Transactional
    void getAllDocumentLettersByContactNameIsEqualToSomething() throws Exception {
        // Initialize the database
        documentLetterRepository.saveAndFlush(documentLetter);

        // Get all the documentLetterList where contactName equals to DEFAULT_CONTACT_NAME
        defaultDocumentLetterShouldBeFound("contactName.equals=" + DEFAULT_CONTACT_NAME);

        // Get all the documentLetterList where contactName equals to UPDATED_CONTACT_NAME
        defaultDocumentLetterShouldNotBeFound("contactName.equals=" + UPDATED_CONTACT_NAME);
    }

    @Test
    @Transactional
    void getAllDocumentLettersByContactNameIsNotEqualToSomething() throws Exception {
        // Initialize the database
        documentLetterRepository.saveAndFlush(documentLetter);

        // Get all the documentLetterList where contactName not equals to DEFAULT_CONTACT_NAME
        defaultDocumentLetterShouldNotBeFound("contactName.notEquals=" + DEFAULT_CONTACT_NAME);

        // Get all the documentLetterList where contactName not equals to UPDATED_CONTACT_NAME
        defaultDocumentLetterShouldBeFound("contactName.notEquals=" + UPDATED_CONTACT_NAME);
    }

    @Test
    @Transactional
    void getAllDocumentLettersByContactNameIsInShouldWork() throws Exception {
        // Initialize the database
        documentLetterRepository.saveAndFlush(documentLetter);

        // Get all the documentLetterList where contactName in DEFAULT_CONTACT_NAME or UPDATED_CONTACT_NAME
        defaultDocumentLetterShouldBeFound("contactName.in=" + DEFAULT_CONTACT_NAME + "," + UPDATED_CONTACT_NAME);

        // Get all the documentLetterList where contactName equals to UPDATED_CONTACT_NAME
        defaultDocumentLetterShouldNotBeFound("contactName.in=" + UPDATED_CONTACT_NAME);
    }

    @Test
    @Transactional
    void getAllDocumentLettersByContactNameIsNullOrNotNull() throws Exception {
        // Initialize the database
        documentLetterRepository.saveAndFlush(documentLetter);

        // Get all the documentLetterList where contactName is not null
        defaultDocumentLetterShouldBeFound("contactName.specified=true");

        // Get all the documentLetterList where contactName is null
        defaultDocumentLetterShouldNotBeFound("contactName.specified=false");
    }

    @Test
    @Transactional
    void getAllDocumentLettersByContactNameContainsSomething() throws Exception {
        // Initialize the database
        documentLetterRepository.saveAndFlush(documentLetter);

        // Get all the documentLetterList where contactName contains DEFAULT_CONTACT_NAME
        defaultDocumentLetterShouldBeFound("contactName.contains=" + DEFAULT_CONTACT_NAME);

        // Get all the documentLetterList where contactName contains UPDATED_CONTACT_NAME
        defaultDocumentLetterShouldNotBeFound("contactName.contains=" + UPDATED_CONTACT_NAME);
    }

    @Test
    @Transactional
    void getAllDocumentLettersByContactNameNotContainsSomething() throws Exception {
        // Initialize the database
        documentLetterRepository.saveAndFlush(documentLetter);

        // Get all the documentLetterList where contactName does not contain DEFAULT_CONTACT_NAME
        defaultDocumentLetterShouldNotBeFound("contactName.doesNotContain=" + DEFAULT_CONTACT_NAME);

        // Get all the documentLetterList where contactName does not contain UPDATED_CONTACT_NAME
        defaultDocumentLetterShouldBeFound("contactName.doesNotContain=" + UPDATED_CONTACT_NAME);
    }

    @Test
    @Transactional
    void getAllDocumentLettersByDateIsEqualToSomething() throws Exception {
        // Initialize the database
        documentLetterRepository.saveAndFlush(documentLetter);

        // Get all the documentLetterList where date equals to DEFAULT_DATE
        defaultDocumentLetterShouldBeFound("date.equals=" + DEFAULT_DATE);

        // Get all the documentLetterList where date equals to UPDATED_DATE
        defaultDocumentLetterShouldNotBeFound("date.equals=" + UPDATED_DATE);
    }

    @Test
    @Transactional
    void getAllDocumentLettersByDateIsNotEqualToSomething() throws Exception {
        // Initialize the database
        documentLetterRepository.saveAndFlush(documentLetter);

        // Get all the documentLetterList where date not equals to DEFAULT_DATE
        defaultDocumentLetterShouldNotBeFound("date.notEquals=" + DEFAULT_DATE);

        // Get all the documentLetterList where date not equals to UPDATED_DATE
        defaultDocumentLetterShouldBeFound("date.notEquals=" + UPDATED_DATE);
    }

    @Test
    @Transactional
    void getAllDocumentLettersByDateIsInShouldWork() throws Exception {
        // Initialize the database
        documentLetterRepository.saveAndFlush(documentLetter);

        // Get all the documentLetterList where date in DEFAULT_DATE or UPDATED_DATE
        defaultDocumentLetterShouldBeFound("date.in=" + DEFAULT_DATE + "," + UPDATED_DATE);

        // Get all the documentLetterList where date equals to UPDATED_DATE
        defaultDocumentLetterShouldNotBeFound("date.in=" + UPDATED_DATE);
    }

    @Test
    @Transactional
    void getAllDocumentLettersByDateIsNullOrNotNull() throws Exception {
        // Initialize the database
        documentLetterRepository.saveAndFlush(documentLetter);

        // Get all the documentLetterList where date is not null
        defaultDocumentLetterShouldBeFound("date.specified=true");

        // Get all the documentLetterList where date is null
        defaultDocumentLetterShouldNotBeFound("date.specified=false");
    }

    @Test
    @Transactional
    void getAllDocumentLettersByDateIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        documentLetterRepository.saveAndFlush(documentLetter);

        // Get all the documentLetterList where date is greater than or equal to DEFAULT_DATE
        defaultDocumentLetterShouldBeFound("date.greaterThanOrEqual=" + DEFAULT_DATE);

        // Get all the documentLetterList where date is greater than or equal to UPDATED_DATE
        defaultDocumentLetterShouldNotBeFound("date.greaterThanOrEqual=" + UPDATED_DATE);
    }

    @Test
    @Transactional
    void getAllDocumentLettersByDateIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        documentLetterRepository.saveAndFlush(documentLetter);

        // Get all the documentLetterList where date is less than or equal to DEFAULT_DATE
        defaultDocumentLetterShouldBeFound("date.lessThanOrEqual=" + DEFAULT_DATE);

        // Get all the documentLetterList where date is less than or equal to SMALLER_DATE
        defaultDocumentLetterShouldNotBeFound("date.lessThanOrEqual=" + SMALLER_DATE);
    }

    @Test
    @Transactional
    void getAllDocumentLettersByDateIsLessThanSomething() throws Exception {
        // Initialize the database
        documentLetterRepository.saveAndFlush(documentLetter);

        // Get all the documentLetterList where date is less than DEFAULT_DATE
        defaultDocumentLetterShouldNotBeFound("date.lessThan=" + DEFAULT_DATE);

        // Get all the documentLetterList where date is less than UPDATED_DATE
        defaultDocumentLetterShouldBeFound("date.lessThan=" + UPDATED_DATE);
    }

    @Test
    @Transactional
    void getAllDocumentLettersByDateIsGreaterThanSomething() throws Exception {
        // Initialize the database
        documentLetterRepository.saveAndFlush(documentLetter);

        // Get all the documentLetterList where date is greater than DEFAULT_DATE
        defaultDocumentLetterShouldNotBeFound("date.greaterThan=" + DEFAULT_DATE);

        // Get all the documentLetterList where date is greater than SMALLER_DATE
        defaultDocumentLetterShouldBeFound("date.greaterThan=" + SMALLER_DATE);
    }

    @Test
    @Transactional
    void getAllDocumentLettersByTitleIsEqualToSomething() throws Exception {
        // Initialize the database
        documentLetterRepository.saveAndFlush(documentLetter);

        // Get all the documentLetterList where title equals to DEFAULT_TITLE
        defaultDocumentLetterShouldBeFound("title.equals=" + DEFAULT_TITLE);

        // Get all the documentLetterList where title equals to UPDATED_TITLE
        defaultDocumentLetterShouldNotBeFound("title.equals=" + UPDATED_TITLE);
    }

    @Test
    @Transactional
    void getAllDocumentLettersByTitleIsNotEqualToSomething() throws Exception {
        // Initialize the database
        documentLetterRepository.saveAndFlush(documentLetter);

        // Get all the documentLetterList where title not equals to DEFAULT_TITLE
        defaultDocumentLetterShouldNotBeFound("title.notEquals=" + DEFAULT_TITLE);

        // Get all the documentLetterList where title not equals to UPDATED_TITLE
        defaultDocumentLetterShouldBeFound("title.notEquals=" + UPDATED_TITLE);
    }

    @Test
    @Transactional
    void getAllDocumentLettersByTitleIsInShouldWork() throws Exception {
        // Initialize the database
        documentLetterRepository.saveAndFlush(documentLetter);

        // Get all the documentLetterList where title in DEFAULT_TITLE or UPDATED_TITLE
        defaultDocumentLetterShouldBeFound("title.in=" + DEFAULT_TITLE + "," + UPDATED_TITLE);

        // Get all the documentLetterList where title equals to UPDATED_TITLE
        defaultDocumentLetterShouldNotBeFound("title.in=" + UPDATED_TITLE);
    }

    @Test
    @Transactional
    void getAllDocumentLettersByTitleIsNullOrNotNull() throws Exception {
        // Initialize the database
        documentLetterRepository.saveAndFlush(documentLetter);

        // Get all the documentLetterList where title is not null
        defaultDocumentLetterShouldBeFound("title.specified=true");

        // Get all the documentLetterList where title is null
        defaultDocumentLetterShouldNotBeFound("title.specified=false");
    }

    @Test
    @Transactional
    void getAllDocumentLettersByTitleContainsSomething() throws Exception {
        // Initialize the database
        documentLetterRepository.saveAndFlush(documentLetter);

        // Get all the documentLetterList where title contains DEFAULT_TITLE
        defaultDocumentLetterShouldBeFound("title.contains=" + DEFAULT_TITLE);

        // Get all the documentLetterList where title contains UPDATED_TITLE
        defaultDocumentLetterShouldNotBeFound("title.contains=" + UPDATED_TITLE);
    }

    @Test
    @Transactional
    void getAllDocumentLettersByTitleNotContainsSomething() throws Exception {
        // Initialize the database
        documentLetterRepository.saveAndFlush(documentLetter);

        // Get all the documentLetterList where title does not contain DEFAULT_TITLE
        defaultDocumentLetterShouldNotBeFound("title.doesNotContain=" + DEFAULT_TITLE);

        // Get all the documentLetterList where title does not contain UPDATED_TITLE
        defaultDocumentLetterShouldBeFound("title.doesNotContain=" + UPDATED_TITLE);
    }

    @Test
    @Transactional
    void getAllDocumentLettersByContentIsEqualToSomething() throws Exception {
        // Initialize the database
        documentLetterRepository.saveAndFlush(documentLetter);

        // Get all the documentLetterList where content equals to DEFAULT_CONTENT
        defaultDocumentLetterShouldBeFound("content.equals=" + DEFAULT_CONTENT);

        // Get all the documentLetterList where content equals to UPDATED_CONTENT
        defaultDocumentLetterShouldNotBeFound("content.equals=" + UPDATED_CONTENT);
    }

    @Test
    @Transactional
    void getAllDocumentLettersByContentIsNotEqualToSomething() throws Exception {
        // Initialize the database
        documentLetterRepository.saveAndFlush(documentLetter);

        // Get all the documentLetterList where content not equals to DEFAULT_CONTENT
        defaultDocumentLetterShouldNotBeFound("content.notEquals=" + DEFAULT_CONTENT);

        // Get all the documentLetterList where content not equals to UPDATED_CONTENT
        defaultDocumentLetterShouldBeFound("content.notEquals=" + UPDATED_CONTENT);
    }

    @Test
    @Transactional
    void getAllDocumentLettersByContentIsInShouldWork() throws Exception {
        // Initialize the database
        documentLetterRepository.saveAndFlush(documentLetter);

        // Get all the documentLetterList where content in DEFAULT_CONTENT or UPDATED_CONTENT
        defaultDocumentLetterShouldBeFound("content.in=" + DEFAULT_CONTENT + "," + UPDATED_CONTENT);

        // Get all the documentLetterList where content equals to UPDATED_CONTENT
        defaultDocumentLetterShouldNotBeFound("content.in=" + UPDATED_CONTENT);
    }

    @Test
    @Transactional
    void getAllDocumentLettersByContentIsNullOrNotNull() throws Exception {
        // Initialize the database
        documentLetterRepository.saveAndFlush(documentLetter);

        // Get all the documentLetterList where content is not null
        defaultDocumentLetterShouldBeFound("content.specified=true");

        // Get all the documentLetterList where content is null
        defaultDocumentLetterShouldNotBeFound("content.specified=false");
    }

    @Test
    @Transactional
    void getAllDocumentLettersByContentContainsSomething() throws Exception {
        // Initialize the database
        documentLetterRepository.saveAndFlush(documentLetter);

        // Get all the documentLetterList where content contains DEFAULT_CONTENT
        defaultDocumentLetterShouldBeFound("content.contains=" + DEFAULT_CONTENT);

        // Get all the documentLetterList where content contains UPDATED_CONTENT
        defaultDocumentLetterShouldNotBeFound("content.contains=" + UPDATED_CONTENT);
    }

    @Test
    @Transactional
    void getAllDocumentLettersByContentNotContainsSomething() throws Exception {
        // Initialize the database
        documentLetterRepository.saveAndFlush(documentLetter);

        // Get all the documentLetterList where content does not contain DEFAULT_CONTENT
        defaultDocumentLetterShouldNotBeFound("content.doesNotContain=" + DEFAULT_CONTENT);

        // Get all the documentLetterList where content does not contain UPDATED_CONTENT
        defaultDocumentLetterShouldBeFound("content.doesNotContain=" + UPDATED_CONTENT);
    }

    @Test
    @Transactional
    void getAllDocumentLettersByLanguageIsEqualToSomething() throws Exception {
        // Initialize the database
        documentLetterRepository.saveAndFlush(documentLetter);

        // Get all the documentLetterList where language equals to DEFAULT_LANGUAGE
        defaultDocumentLetterShouldBeFound("language.equals=" + DEFAULT_LANGUAGE);

        // Get all the documentLetterList where language equals to UPDATED_LANGUAGE
        defaultDocumentLetterShouldNotBeFound("language.equals=" + UPDATED_LANGUAGE);
    }

    @Test
    @Transactional
    void getAllDocumentLettersByLanguageIsNotEqualToSomething() throws Exception {
        // Initialize the database
        documentLetterRepository.saveAndFlush(documentLetter);

        // Get all the documentLetterList where language not equals to DEFAULT_LANGUAGE
        defaultDocumentLetterShouldNotBeFound("language.notEquals=" + DEFAULT_LANGUAGE);

        // Get all the documentLetterList where language not equals to UPDATED_LANGUAGE
        defaultDocumentLetterShouldBeFound("language.notEquals=" + UPDATED_LANGUAGE);
    }

    @Test
    @Transactional
    void getAllDocumentLettersByLanguageIsInShouldWork() throws Exception {
        // Initialize the database
        documentLetterRepository.saveAndFlush(documentLetter);

        // Get all the documentLetterList where language in DEFAULT_LANGUAGE or UPDATED_LANGUAGE
        defaultDocumentLetterShouldBeFound("language.in=" + DEFAULT_LANGUAGE + "," + UPDATED_LANGUAGE);

        // Get all the documentLetterList where language equals to UPDATED_LANGUAGE
        defaultDocumentLetterShouldNotBeFound("language.in=" + UPDATED_LANGUAGE);
    }

    @Test
    @Transactional
    void getAllDocumentLettersByLanguageIsNullOrNotNull() throws Exception {
        // Initialize the database
        documentLetterRepository.saveAndFlush(documentLetter);

        // Get all the documentLetterList where language is not null
        defaultDocumentLetterShouldBeFound("language.specified=true");

        // Get all the documentLetterList where language is null
        defaultDocumentLetterShouldNotBeFound("language.specified=false");
    }

    @Test
    @Transactional
    void getAllDocumentLettersByPageAmountIsEqualToSomething() throws Exception {
        // Initialize the database
        documentLetterRepository.saveAndFlush(documentLetter);

        // Get all the documentLetterList where pageAmount equals to DEFAULT_PAGE_AMOUNT
        defaultDocumentLetterShouldBeFound("pageAmount.equals=" + DEFAULT_PAGE_AMOUNT);

        // Get all the documentLetterList where pageAmount equals to UPDATED_PAGE_AMOUNT
        defaultDocumentLetterShouldNotBeFound("pageAmount.equals=" + UPDATED_PAGE_AMOUNT);
    }

    @Test
    @Transactional
    void getAllDocumentLettersByPageAmountIsNotEqualToSomething() throws Exception {
        // Initialize the database
        documentLetterRepository.saveAndFlush(documentLetter);

        // Get all the documentLetterList where pageAmount not equals to DEFAULT_PAGE_AMOUNT
        defaultDocumentLetterShouldNotBeFound("pageAmount.notEquals=" + DEFAULT_PAGE_AMOUNT);

        // Get all the documentLetterList where pageAmount not equals to UPDATED_PAGE_AMOUNT
        defaultDocumentLetterShouldBeFound("pageAmount.notEquals=" + UPDATED_PAGE_AMOUNT);
    }

    @Test
    @Transactional
    void getAllDocumentLettersByPageAmountIsInShouldWork() throws Exception {
        // Initialize the database
        documentLetterRepository.saveAndFlush(documentLetter);

        // Get all the documentLetterList where pageAmount in DEFAULT_PAGE_AMOUNT or UPDATED_PAGE_AMOUNT
        defaultDocumentLetterShouldBeFound("pageAmount.in=" + DEFAULT_PAGE_AMOUNT + "," + UPDATED_PAGE_AMOUNT);

        // Get all the documentLetterList where pageAmount equals to UPDATED_PAGE_AMOUNT
        defaultDocumentLetterShouldNotBeFound("pageAmount.in=" + UPDATED_PAGE_AMOUNT);
    }

    @Test
    @Transactional
    void getAllDocumentLettersByPageAmountIsNullOrNotNull() throws Exception {
        // Initialize the database
        documentLetterRepository.saveAndFlush(documentLetter);

        // Get all the documentLetterList where pageAmount is not null
        defaultDocumentLetterShouldBeFound("pageAmount.specified=true");

        // Get all the documentLetterList where pageAmount is null
        defaultDocumentLetterShouldNotBeFound("pageAmount.specified=false");
    }

    @Test
    @Transactional
    void getAllDocumentLettersByPageAmountIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        documentLetterRepository.saveAndFlush(documentLetter);

        // Get all the documentLetterList where pageAmount is greater than or equal to DEFAULT_PAGE_AMOUNT
        defaultDocumentLetterShouldBeFound("pageAmount.greaterThanOrEqual=" + DEFAULT_PAGE_AMOUNT);

        // Get all the documentLetterList where pageAmount is greater than or equal to UPDATED_PAGE_AMOUNT
        defaultDocumentLetterShouldNotBeFound("pageAmount.greaterThanOrEqual=" + UPDATED_PAGE_AMOUNT);
    }

    @Test
    @Transactional
    void getAllDocumentLettersByPageAmountIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        documentLetterRepository.saveAndFlush(documentLetter);

        // Get all the documentLetterList where pageAmount is less than or equal to DEFAULT_PAGE_AMOUNT
        defaultDocumentLetterShouldBeFound("pageAmount.lessThanOrEqual=" + DEFAULT_PAGE_AMOUNT);

        // Get all the documentLetterList where pageAmount is less than or equal to SMALLER_PAGE_AMOUNT
        defaultDocumentLetterShouldNotBeFound("pageAmount.lessThanOrEqual=" + SMALLER_PAGE_AMOUNT);
    }

    @Test
    @Transactional
    void getAllDocumentLettersByPageAmountIsLessThanSomething() throws Exception {
        // Initialize the database
        documentLetterRepository.saveAndFlush(documentLetter);

        // Get all the documentLetterList where pageAmount is less than DEFAULT_PAGE_AMOUNT
        defaultDocumentLetterShouldNotBeFound("pageAmount.lessThan=" + DEFAULT_PAGE_AMOUNT);

        // Get all the documentLetterList where pageAmount is less than UPDATED_PAGE_AMOUNT
        defaultDocumentLetterShouldBeFound("pageAmount.lessThan=" + UPDATED_PAGE_AMOUNT);
    }

    @Test
    @Transactional
    void getAllDocumentLettersByPageAmountIsGreaterThanSomething() throws Exception {
        // Initialize the database
        documentLetterRepository.saveAndFlush(documentLetter);

        // Get all the documentLetterList where pageAmount is greater than DEFAULT_PAGE_AMOUNT
        defaultDocumentLetterShouldNotBeFound("pageAmount.greaterThan=" + DEFAULT_PAGE_AMOUNT);

        // Get all the documentLetterList where pageAmount is greater than SMALLER_PAGE_AMOUNT
        defaultDocumentLetterShouldBeFound("pageAmount.greaterThan=" + SMALLER_PAGE_AMOUNT);
    }

    @Test
    @Transactional
    void getAllDocumentLettersByNotesIsEqualToSomething() throws Exception {
        // Initialize the database
        documentLetterRepository.saveAndFlush(documentLetter);

        // Get all the documentLetterList where notes equals to DEFAULT_NOTES
        defaultDocumentLetterShouldBeFound("notes.equals=" + DEFAULT_NOTES);

        // Get all the documentLetterList where notes equals to UPDATED_NOTES
        defaultDocumentLetterShouldNotBeFound("notes.equals=" + UPDATED_NOTES);
    }

    @Test
    @Transactional
    void getAllDocumentLettersByNotesIsNotEqualToSomething() throws Exception {
        // Initialize the database
        documentLetterRepository.saveAndFlush(documentLetter);

        // Get all the documentLetterList where notes not equals to DEFAULT_NOTES
        defaultDocumentLetterShouldNotBeFound("notes.notEquals=" + DEFAULT_NOTES);

        // Get all the documentLetterList where notes not equals to UPDATED_NOTES
        defaultDocumentLetterShouldBeFound("notes.notEquals=" + UPDATED_NOTES);
    }

    @Test
    @Transactional
    void getAllDocumentLettersByNotesIsInShouldWork() throws Exception {
        // Initialize the database
        documentLetterRepository.saveAndFlush(documentLetter);

        // Get all the documentLetterList where notes in DEFAULT_NOTES or UPDATED_NOTES
        defaultDocumentLetterShouldBeFound("notes.in=" + DEFAULT_NOTES + "," + UPDATED_NOTES);

        // Get all the documentLetterList where notes equals to UPDATED_NOTES
        defaultDocumentLetterShouldNotBeFound("notes.in=" + UPDATED_NOTES);
    }

    @Test
    @Transactional
    void getAllDocumentLettersByNotesIsNullOrNotNull() throws Exception {
        // Initialize the database
        documentLetterRepository.saveAndFlush(documentLetter);

        // Get all the documentLetterList where notes is not null
        defaultDocumentLetterShouldBeFound("notes.specified=true");

        // Get all the documentLetterList where notes is null
        defaultDocumentLetterShouldNotBeFound("notes.specified=false");
    }

    @Test
    @Transactional
    void getAllDocumentLettersByNotesContainsSomething() throws Exception {
        // Initialize the database
        documentLetterRepository.saveAndFlush(documentLetter);

        // Get all the documentLetterList where notes contains DEFAULT_NOTES
        defaultDocumentLetterShouldBeFound("notes.contains=" + DEFAULT_NOTES);

        // Get all the documentLetterList where notes contains UPDATED_NOTES
        defaultDocumentLetterShouldNotBeFound("notes.contains=" + UPDATED_NOTES);
    }

    @Test
    @Transactional
    void getAllDocumentLettersByNotesNotContainsSomething() throws Exception {
        // Initialize the database
        documentLetterRepository.saveAndFlush(documentLetter);

        // Get all the documentLetterList where notes does not contain DEFAULT_NOTES
        defaultDocumentLetterShouldNotBeFound("notes.doesNotContain=" + DEFAULT_NOTES);

        // Get all the documentLetterList where notes does not contain UPDATED_NOTES
        defaultDocumentLetterShouldBeFound("notes.doesNotContain=" + UPDATED_NOTES);
    }

    @Test
    @Transactional
    void getAllDocumentLettersByStatusIsEqualToSomething() throws Exception {
        // Initialize the database
        documentLetterRepository.saveAndFlush(documentLetter);

        // Get all the documentLetterList where status equals to DEFAULT_STATUS
        defaultDocumentLetterShouldBeFound("status.equals=" + DEFAULT_STATUS);

        // Get all the documentLetterList where status equals to UPDATED_STATUS
        defaultDocumentLetterShouldNotBeFound("status.equals=" + UPDATED_STATUS);
    }

    @Test
    @Transactional
    void getAllDocumentLettersByStatusIsNotEqualToSomething() throws Exception {
        // Initialize the database
        documentLetterRepository.saveAndFlush(documentLetter);

        // Get all the documentLetterList where status not equals to DEFAULT_STATUS
        defaultDocumentLetterShouldNotBeFound("status.notEquals=" + DEFAULT_STATUS);

        // Get all the documentLetterList where status not equals to UPDATED_STATUS
        defaultDocumentLetterShouldBeFound("status.notEquals=" + UPDATED_STATUS);
    }

    @Test
    @Transactional
    void getAllDocumentLettersByStatusIsInShouldWork() throws Exception {
        // Initialize the database
        documentLetterRepository.saveAndFlush(documentLetter);

        // Get all the documentLetterList where status in DEFAULT_STATUS or UPDATED_STATUS
        defaultDocumentLetterShouldBeFound("status.in=" + DEFAULT_STATUS + "," + UPDATED_STATUS);

        // Get all the documentLetterList where status equals to UPDATED_STATUS
        defaultDocumentLetterShouldNotBeFound("status.in=" + UPDATED_STATUS);
    }

    @Test
    @Transactional
    void getAllDocumentLettersByStatusIsNullOrNotNull() throws Exception {
        // Initialize the database
        documentLetterRepository.saveAndFlush(documentLetter);

        // Get all the documentLetterList where status is not null
        defaultDocumentLetterShouldBeFound("status.specified=true");

        // Get all the documentLetterList where status is null
        defaultDocumentLetterShouldNotBeFound("status.specified=false");
    }

    @Test
    @Transactional
    void getAllDocumentLettersByCreatedIsEqualToSomething() throws Exception {
        // Initialize the database
        documentLetterRepository.saveAndFlush(documentLetter);

        // Get all the documentLetterList where created equals to DEFAULT_CREATED
        defaultDocumentLetterShouldBeFound("created.equals=" + DEFAULT_CREATED);

        // Get all the documentLetterList where created equals to UPDATED_CREATED
        defaultDocumentLetterShouldNotBeFound("created.equals=" + UPDATED_CREATED);
    }

    @Test
    @Transactional
    void getAllDocumentLettersByCreatedIsNotEqualToSomething() throws Exception {
        // Initialize the database
        documentLetterRepository.saveAndFlush(documentLetter);

        // Get all the documentLetterList where created not equals to DEFAULT_CREATED
        defaultDocumentLetterShouldNotBeFound("created.notEquals=" + DEFAULT_CREATED);

        // Get all the documentLetterList where created not equals to UPDATED_CREATED
        defaultDocumentLetterShouldBeFound("created.notEquals=" + UPDATED_CREATED);
    }

    @Test
    @Transactional
    void getAllDocumentLettersByCreatedIsInShouldWork() throws Exception {
        // Initialize the database
        documentLetterRepository.saveAndFlush(documentLetter);

        // Get all the documentLetterList where created in DEFAULT_CREATED or UPDATED_CREATED
        defaultDocumentLetterShouldBeFound("created.in=" + DEFAULT_CREATED + "," + UPDATED_CREATED);

        // Get all the documentLetterList where created equals to UPDATED_CREATED
        defaultDocumentLetterShouldNotBeFound("created.in=" + UPDATED_CREATED);
    }

    @Test
    @Transactional
    void getAllDocumentLettersByCreatedIsNullOrNotNull() throws Exception {
        // Initialize the database
        documentLetterRepository.saveAndFlush(documentLetter);

        // Get all the documentLetterList where created is not null
        defaultDocumentLetterShouldBeFound("created.specified=true");

        // Get all the documentLetterList where created is null
        defaultDocumentLetterShouldNotBeFound("created.specified=false");
    }

    @Test
    @Transactional
    void getAllDocumentLettersByCreatedIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        documentLetterRepository.saveAndFlush(documentLetter);

        // Get all the documentLetterList where created is greater than or equal to DEFAULT_CREATED
        defaultDocumentLetterShouldBeFound("created.greaterThanOrEqual=" + DEFAULT_CREATED);

        // Get all the documentLetterList where created is greater than or equal to UPDATED_CREATED
        defaultDocumentLetterShouldNotBeFound("created.greaterThanOrEqual=" + UPDATED_CREATED);
    }

    @Test
    @Transactional
    void getAllDocumentLettersByCreatedIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        documentLetterRepository.saveAndFlush(documentLetter);

        // Get all the documentLetterList where created is less than or equal to DEFAULT_CREATED
        defaultDocumentLetterShouldBeFound("created.lessThanOrEqual=" + DEFAULT_CREATED);

        // Get all the documentLetterList where created is less than or equal to SMALLER_CREATED
        defaultDocumentLetterShouldNotBeFound("created.lessThanOrEqual=" + SMALLER_CREATED);
    }

    @Test
    @Transactional
    void getAllDocumentLettersByCreatedIsLessThanSomething() throws Exception {
        // Initialize the database
        documentLetterRepository.saveAndFlush(documentLetter);

        // Get all the documentLetterList where created is less than DEFAULT_CREATED
        defaultDocumentLetterShouldNotBeFound("created.lessThan=" + DEFAULT_CREATED);

        // Get all the documentLetterList where created is less than UPDATED_CREATED
        defaultDocumentLetterShouldBeFound("created.lessThan=" + UPDATED_CREATED);
    }

    @Test
    @Transactional
    void getAllDocumentLettersByCreatedIsGreaterThanSomething() throws Exception {
        // Initialize the database
        documentLetterRepository.saveAndFlush(documentLetter);

        // Get all the documentLetterList where created is greater than DEFAULT_CREATED
        defaultDocumentLetterShouldNotBeFound("created.greaterThan=" + DEFAULT_CREATED);

        // Get all the documentLetterList where created is greater than SMALLER_CREATED
        defaultDocumentLetterShouldBeFound("created.greaterThan=" + SMALLER_CREATED);
    }

    @Test
    @Transactional
    void getAllDocumentLettersByCustomFieldsIsEqualToSomething() throws Exception {
        // Initialize the database
        documentLetterRepository.saveAndFlush(documentLetter);
        CustomFieldValue customFields = CustomFieldValueResourceIT.createEntity(em);
        em.persist(customFields);
        em.flush();
        documentLetter.addCustomFields(customFields);
        documentLetterRepository.saveAndFlush(documentLetter);
        Long customFieldsId = customFields.getId();

        // Get all the documentLetterList where customFields equals to customFieldsId
        defaultDocumentLetterShouldBeFound("customFieldsId.equals=" + customFieldsId);

        // Get all the documentLetterList where customFields equals to (customFieldsId + 1)
        defaultDocumentLetterShouldNotBeFound("customFieldsId.equals=" + (customFieldsId + 1));
    }

    @Test
    @Transactional
    void getAllDocumentLettersByFreeTextsIsEqualToSomething() throws Exception {
        // Initialize the database
        documentLetterRepository.saveAndFlush(documentLetter);
        DocumentFreeText freeTexts = DocumentFreeTextResourceIT.createEntity(em);
        em.persist(freeTexts);
        em.flush();
        documentLetter.addFreeTexts(freeTexts);
        documentLetterRepository.saveAndFlush(documentLetter);
        Long freeTextsId = freeTexts.getId();

        // Get all the documentLetterList where freeTexts equals to freeTextsId
        defaultDocumentLetterShouldBeFound("freeTextsId.equals=" + freeTextsId);

        // Get all the documentLetterList where freeTexts equals to (freeTextsId + 1)
        defaultDocumentLetterShouldNotBeFound("freeTextsId.equals=" + (freeTextsId + 1));
    }

    @Test
    @Transactional
    void getAllDocumentLettersByContactIsEqualToSomething() throws Exception {
        // Initialize the database
        documentLetterRepository.saveAndFlush(documentLetter);
        Contact contact = ContactResourceIT.createEntity(em);
        em.persist(contact);
        em.flush();
        documentLetter.setContact(contact);
        documentLetterRepository.saveAndFlush(documentLetter);
        Long contactId = contact.getId();

        // Get all the documentLetterList where contact equals to contactId
        defaultDocumentLetterShouldBeFound("contactId.equals=" + contactId);

        // Get all the documentLetterList where contact equals to (contactId + 1)
        defaultDocumentLetterShouldNotBeFound("contactId.equals=" + (contactId + 1));
    }

    @Test
    @Transactional
    void getAllDocumentLettersByContactAddressIsEqualToSomething() throws Exception {
        // Initialize the database
        documentLetterRepository.saveAndFlush(documentLetter);
        ContactAddress contactAddress = ContactAddressResourceIT.createEntity(em);
        em.persist(contactAddress);
        em.flush();
        documentLetter.setContactAddress(contactAddress);
        documentLetterRepository.saveAndFlush(documentLetter);
        Long contactAddressId = contactAddress.getId();

        // Get all the documentLetterList where contactAddress equals to contactAddressId
        defaultDocumentLetterShouldBeFound("contactAddressId.equals=" + contactAddressId);

        // Get all the documentLetterList where contactAddress equals to (contactAddressId + 1)
        defaultDocumentLetterShouldNotBeFound("contactAddressId.equals=" + (contactAddressId + 1));
    }

    @Test
    @Transactional
    void getAllDocumentLettersByContactPersonIsEqualToSomething() throws Exception {
        // Initialize the database
        documentLetterRepository.saveAndFlush(documentLetter);
        ContactPerson contactPerson = ContactPersonResourceIT.createEntity(em);
        em.persist(contactPerson);
        em.flush();
        documentLetter.setContactPerson(contactPerson);
        documentLetterRepository.saveAndFlush(documentLetter);
        Long contactPersonId = contactPerson.getId();

        // Get all the documentLetterList where contactPerson equals to contactPersonId
        defaultDocumentLetterShouldBeFound("contactPersonId.equals=" + contactPersonId);

        // Get all the documentLetterList where contactPerson equals to (contactPersonId + 1)
        defaultDocumentLetterShouldNotBeFound("contactPersonId.equals=" + (contactPersonId + 1));
    }

    @Test
    @Transactional
    void getAllDocumentLettersByContactPrePageAddressIsEqualToSomething() throws Exception {
        // Initialize the database
        documentLetterRepository.saveAndFlush(documentLetter);
        ContactAddress contactPrePageAddress = ContactAddressResourceIT.createEntity(em);
        em.persist(contactPrePageAddress);
        em.flush();
        documentLetter.setContactPrePageAddress(contactPrePageAddress);
        documentLetterRepository.saveAndFlush(documentLetter);
        Long contactPrePageAddressId = contactPrePageAddress.getId();

        // Get all the documentLetterList where contactPrePageAddress equals to contactPrePageAddressId
        defaultDocumentLetterShouldBeFound("contactPrePageAddressId.equals=" + contactPrePageAddressId);

        // Get all the documentLetterList where contactPrePageAddress equals to (contactPrePageAddressId + 1)
        defaultDocumentLetterShouldNotBeFound("contactPrePageAddressId.equals=" + (contactPrePageAddressId + 1));
    }

    @Test
    @Transactional
    void getAllDocumentLettersByLayoutIsEqualToSomething() throws Exception {
        // Initialize the database
        documentLetterRepository.saveAndFlush(documentLetter);
        Layout layout = LayoutResourceIT.createEntity(em);
        em.persist(layout);
        em.flush();
        documentLetter.setLayout(layout);
        documentLetterRepository.saveAndFlush(documentLetter);
        Long layoutId = layout.getId();

        // Get all the documentLetterList where layout equals to layoutId
        defaultDocumentLetterShouldBeFound("layoutId.equals=" + layoutId);

        // Get all the documentLetterList where layout equals to (layoutId + 1)
        defaultDocumentLetterShouldNotBeFound("layoutId.equals=" + (layoutId + 1));
    }

    @Test
    @Transactional
    void getAllDocumentLettersBySignatureIsEqualToSomething() throws Exception {
        // Initialize the database
        documentLetterRepository.saveAndFlush(documentLetter);
        Signature signature = SignatureResourceIT.createEntity(em);
        em.persist(signature);
        em.flush();
        documentLetter.setSignature(signature);
        documentLetterRepository.saveAndFlush(documentLetter);
        Long signatureId = signature.getId();

        // Get all the documentLetterList where signature equals to signatureId
        defaultDocumentLetterShouldBeFound("signatureId.equals=" + signatureId);

        // Get all the documentLetterList where signature equals to (signatureId + 1)
        defaultDocumentLetterShouldNotBeFound("signatureId.equals=" + (signatureId + 1));
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultDocumentLetterShouldBeFound(String filter) throws Exception {
        restDocumentLetterMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(documentLetter.getId().intValue())))
            .andExpect(jsonPath("$.[*].remoteId").value(hasItem(DEFAULT_REMOTE_ID)))
            .andExpect(jsonPath("$.[*].contactName").value(hasItem(DEFAULT_CONTACT_NAME)))
            .andExpect(jsonPath("$.[*].date").value(hasItem(DEFAULT_DATE.toString())))
            .andExpect(jsonPath("$.[*].title").value(hasItem(DEFAULT_TITLE)))
            .andExpect(jsonPath("$.[*].content").value(hasItem(DEFAULT_CONTENT)))
            .andExpect(jsonPath("$.[*].language").value(hasItem(DEFAULT_LANGUAGE.toString())))
            .andExpect(jsonPath("$.[*].pageAmount").value(hasItem(DEFAULT_PAGE_AMOUNT)))
            .andExpect(jsonPath("$.[*].notes").value(hasItem(DEFAULT_NOTES)))
            .andExpect(jsonPath("$.[*].status").value(hasItem(DEFAULT_STATUS.toString())))
            .andExpect(jsonPath("$.[*].created").value(hasItem(sameInstant(DEFAULT_CREATED))));

        // Check, that the count call also returns 1
        restDocumentLetterMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultDocumentLetterShouldNotBeFound(String filter) throws Exception {
        restDocumentLetterMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restDocumentLetterMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingDocumentLetter() throws Exception {
        // Get the documentLetter
        restDocumentLetterMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewDocumentLetter() throws Exception {
        // Initialize the database
        documentLetterRepository.saveAndFlush(documentLetter);

        int databaseSizeBeforeUpdate = documentLetterRepository.findAll().size();

        // Update the documentLetter
        DocumentLetter updatedDocumentLetter = documentLetterRepository.findById(documentLetter.getId()).get();
        // Disconnect from session so that the updates on updatedDocumentLetter are not directly saved in db
        em.detach(updatedDocumentLetter);
        updatedDocumentLetter
            .remoteId(UPDATED_REMOTE_ID)
            .contactName(UPDATED_CONTACT_NAME)
            .date(UPDATED_DATE)
            .title(UPDATED_TITLE)
            .content(UPDATED_CONTENT)
            .language(UPDATED_LANGUAGE)
            .pageAmount(UPDATED_PAGE_AMOUNT)
            .notes(UPDATED_NOTES)
            .status(UPDATED_STATUS)
            .created(UPDATED_CREATED);
        DocumentLetterDTO documentLetterDTO = documentLetterMapper.toDto(updatedDocumentLetter);

        restDocumentLetterMockMvc
            .perform(
                put(ENTITY_API_URL_ID, documentLetterDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(documentLetterDTO))
            )
            .andExpect(status().isOk());

        // Validate the DocumentLetter in the database
        List<DocumentLetter> documentLetterList = documentLetterRepository.findAll();
        assertThat(documentLetterList).hasSize(databaseSizeBeforeUpdate);
        DocumentLetter testDocumentLetter = documentLetterList.get(documentLetterList.size() - 1);
        assertThat(testDocumentLetter.getRemoteId()).isEqualTo(UPDATED_REMOTE_ID);
        assertThat(testDocumentLetter.getContactName()).isEqualTo(UPDATED_CONTACT_NAME);
        assertThat(testDocumentLetter.getDate()).isEqualTo(UPDATED_DATE);
        assertThat(testDocumentLetter.getTitle()).isEqualTo(UPDATED_TITLE);
        assertThat(testDocumentLetter.getContent()).isEqualTo(UPDATED_CONTENT);
        assertThat(testDocumentLetter.getLanguage()).isEqualTo(UPDATED_LANGUAGE);
        assertThat(testDocumentLetter.getPageAmount()).isEqualTo(UPDATED_PAGE_AMOUNT);
        assertThat(testDocumentLetter.getNotes()).isEqualTo(UPDATED_NOTES);
        assertThat(testDocumentLetter.getStatus()).isEqualTo(UPDATED_STATUS);
        assertThat(testDocumentLetter.getCreated()).isEqualTo(UPDATED_CREATED);
    }

    @Test
    @Transactional
    void putNonExistingDocumentLetter() throws Exception {
        int databaseSizeBeforeUpdate = documentLetterRepository.findAll().size();
        documentLetter.setId(count.incrementAndGet());

        // Create the DocumentLetter
        DocumentLetterDTO documentLetterDTO = documentLetterMapper.toDto(documentLetter);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restDocumentLetterMockMvc
            .perform(
                put(ENTITY_API_URL_ID, documentLetterDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(documentLetterDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the DocumentLetter in the database
        List<DocumentLetter> documentLetterList = documentLetterRepository.findAll();
        assertThat(documentLetterList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchDocumentLetter() throws Exception {
        int databaseSizeBeforeUpdate = documentLetterRepository.findAll().size();
        documentLetter.setId(count.incrementAndGet());

        // Create the DocumentLetter
        DocumentLetterDTO documentLetterDTO = documentLetterMapper.toDto(documentLetter);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restDocumentLetterMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(documentLetterDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the DocumentLetter in the database
        List<DocumentLetter> documentLetterList = documentLetterRepository.findAll();
        assertThat(documentLetterList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamDocumentLetter() throws Exception {
        int databaseSizeBeforeUpdate = documentLetterRepository.findAll().size();
        documentLetter.setId(count.incrementAndGet());

        // Create the DocumentLetter
        DocumentLetterDTO documentLetterDTO = documentLetterMapper.toDto(documentLetter);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restDocumentLetterMockMvc
            .perform(
                put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(documentLetterDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the DocumentLetter in the database
        List<DocumentLetter> documentLetterList = documentLetterRepository.findAll();
        assertThat(documentLetterList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateDocumentLetterWithPatch() throws Exception {
        // Initialize the database
        documentLetterRepository.saveAndFlush(documentLetter);

        int databaseSizeBeforeUpdate = documentLetterRepository.findAll().size();

        // Update the documentLetter using partial update
        DocumentLetter partialUpdatedDocumentLetter = new DocumentLetter();
        partialUpdatedDocumentLetter.setId(documentLetter.getId());

        partialUpdatedDocumentLetter
            .title(UPDATED_TITLE)
            .language(UPDATED_LANGUAGE)
            .notes(UPDATED_NOTES)
            .status(UPDATED_STATUS)
            .created(UPDATED_CREATED);

        restDocumentLetterMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedDocumentLetter.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedDocumentLetter))
            )
            .andExpect(status().isOk());

        // Validate the DocumentLetter in the database
        List<DocumentLetter> documentLetterList = documentLetterRepository.findAll();
        assertThat(documentLetterList).hasSize(databaseSizeBeforeUpdate);
        DocumentLetter testDocumentLetter = documentLetterList.get(documentLetterList.size() - 1);
        assertThat(testDocumentLetter.getRemoteId()).isEqualTo(DEFAULT_REMOTE_ID);
        assertThat(testDocumentLetter.getContactName()).isEqualTo(DEFAULT_CONTACT_NAME);
        assertThat(testDocumentLetter.getDate()).isEqualTo(DEFAULT_DATE);
        assertThat(testDocumentLetter.getTitle()).isEqualTo(UPDATED_TITLE);
        assertThat(testDocumentLetter.getContent()).isEqualTo(DEFAULT_CONTENT);
        assertThat(testDocumentLetter.getLanguage()).isEqualTo(UPDATED_LANGUAGE);
        assertThat(testDocumentLetter.getPageAmount()).isEqualTo(DEFAULT_PAGE_AMOUNT);
        assertThat(testDocumentLetter.getNotes()).isEqualTo(UPDATED_NOTES);
        assertThat(testDocumentLetter.getStatus()).isEqualTo(UPDATED_STATUS);
        assertThat(testDocumentLetter.getCreated()).isEqualTo(UPDATED_CREATED);
    }

    @Test
    @Transactional
    void fullUpdateDocumentLetterWithPatch() throws Exception {
        // Initialize the database
        documentLetterRepository.saveAndFlush(documentLetter);

        int databaseSizeBeforeUpdate = documentLetterRepository.findAll().size();

        // Update the documentLetter using partial update
        DocumentLetter partialUpdatedDocumentLetter = new DocumentLetter();
        partialUpdatedDocumentLetter.setId(documentLetter.getId());

        partialUpdatedDocumentLetter
            .remoteId(UPDATED_REMOTE_ID)
            .contactName(UPDATED_CONTACT_NAME)
            .date(UPDATED_DATE)
            .title(UPDATED_TITLE)
            .content(UPDATED_CONTENT)
            .language(UPDATED_LANGUAGE)
            .pageAmount(UPDATED_PAGE_AMOUNT)
            .notes(UPDATED_NOTES)
            .status(UPDATED_STATUS)
            .created(UPDATED_CREATED);

        restDocumentLetterMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedDocumentLetter.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedDocumentLetter))
            )
            .andExpect(status().isOk());

        // Validate the DocumentLetter in the database
        List<DocumentLetter> documentLetterList = documentLetterRepository.findAll();
        assertThat(documentLetterList).hasSize(databaseSizeBeforeUpdate);
        DocumentLetter testDocumentLetter = documentLetterList.get(documentLetterList.size() - 1);
        assertThat(testDocumentLetter.getRemoteId()).isEqualTo(UPDATED_REMOTE_ID);
        assertThat(testDocumentLetter.getContactName()).isEqualTo(UPDATED_CONTACT_NAME);
        assertThat(testDocumentLetter.getDate()).isEqualTo(UPDATED_DATE);
        assertThat(testDocumentLetter.getTitle()).isEqualTo(UPDATED_TITLE);
        assertThat(testDocumentLetter.getContent()).isEqualTo(UPDATED_CONTENT);
        assertThat(testDocumentLetter.getLanguage()).isEqualTo(UPDATED_LANGUAGE);
        assertThat(testDocumentLetter.getPageAmount()).isEqualTo(UPDATED_PAGE_AMOUNT);
        assertThat(testDocumentLetter.getNotes()).isEqualTo(UPDATED_NOTES);
        assertThat(testDocumentLetter.getStatus()).isEqualTo(UPDATED_STATUS);
        assertThat(testDocumentLetter.getCreated()).isEqualTo(UPDATED_CREATED);
    }

    @Test
    @Transactional
    void patchNonExistingDocumentLetter() throws Exception {
        int databaseSizeBeforeUpdate = documentLetterRepository.findAll().size();
        documentLetter.setId(count.incrementAndGet());

        // Create the DocumentLetter
        DocumentLetterDTO documentLetterDTO = documentLetterMapper.toDto(documentLetter);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restDocumentLetterMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, documentLetterDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(documentLetterDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the DocumentLetter in the database
        List<DocumentLetter> documentLetterList = documentLetterRepository.findAll();
        assertThat(documentLetterList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchDocumentLetter() throws Exception {
        int databaseSizeBeforeUpdate = documentLetterRepository.findAll().size();
        documentLetter.setId(count.incrementAndGet());

        // Create the DocumentLetter
        DocumentLetterDTO documentLetterDTO = documentLetterMapper.toDto(documentLetter);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restDocumentLetterMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(documentLetterDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the DocumentLetter in the database
        List<DocumentLetter> documentLetterList = documentLetterRepository.findAll();
        assertThat(documentLetterList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamDocumentLetter() throws Exception {
        int databaseSizeBeforeUpdate = documentLetterRepository.findAll().size();
        documentLetter.setId(count.incrementAndGet());

        // Create the DocumentLetter
        DocumentLetterDTO documentLetterDTO = documentLetterMapper.toDto(documentLetter);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restDocumentLetterMockMvc
            .perform(
                patch(ENTITY_API_URL)
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(documentLetterDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the DocumentLetter in the database
        List<DocumentLetter> documentLetterList = documentLetterRepository.findAll();
        assertThat(documentLetterList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteDocumentLetter() throws Exception {
        // Initialize the database
        documentLetterRepository.saveAndFlush(documentLetter);

        int databaseSizeBeforeDelete = documentLetterRepository.findAll().size();

        // Delete the documentLetter
        restDocumentLetterMockMvc
            .perform(delete(ENTITY_API_URL_ID, documentLetter.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<DocumentLetter> documentLetterList = documentLetterRepository.findAll();
        assertThat(documentLetterList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
