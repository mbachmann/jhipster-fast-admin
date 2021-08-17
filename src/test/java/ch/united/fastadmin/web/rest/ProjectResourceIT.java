package ch.united.fastadmin.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import ch.united.fastadmin.IntegrationTest;
import ch.united.fastadmin.domain.Contact;
import ch.united.fastadmin.domain.CustomFieldValue;
import ch.united.fastadmin.domain.Project;
import ch.united.fastadmin.domain.enumeration.ProjectStatus;
import ch.united.fastadmin.repository.ProjectRepository;
import ch.united.fastadmin.service.criteria.ProjectCriteria;
import ch.united.fastadmin.service.dto.ProjectDTO;
import ch.united.fastadmin.service.mapper.ProjectMapper;
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

/**
 * Integration tests for the {@link ProjectResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class ProjectResourceIT {

    private static final Integer DEFAULT_REMOTE_ID = 1;
    private static final Integer UPDATED_REMOTE_ID = 2;
    private static final Integer SMALLER_REMOTE_ID = 1 - 1;

    private static final String DEFAULT_NUMBER = "AAAAAAAAAA";
    private static final String UPDATED_NUMBER = "BBBBBBBBBB";

    private static final String DEFAULT_CONTACT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_CONTACT_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_DESCRIPTION = "AAAAAAAAAA";
    private static final String UPDATED_DESCRIPTION = "BBBBBBBBBB";

    private static final LocalDate DEFAULT_START_DATE = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_START_DATE = LocalDate.now(ZoneId.systemDefault());
    private static final LocalDate SMALLER_START_DATE = LocalDate.ofEpochDay(-1L);

    private static final Double DEFAULT_HOURS_ESTIMATED = 1D;
    private static final Double UPDATED_HOURS_ESTIMATED = 2D;
    private static final Double SMALLER_HOURS_ESTIMATED = 1D - 1D;

    private static final Double DEFAULT_HOURLY_RATE = 1D;
    private static final Double UPDATED_HOURLY_RATE = 2D;
    private static final Double SMALLER_HOURLY_RATE = 1D - 1D;

    private static final ProjectStatus DEFAULT_STATUS = ProjectStatus.OPEN;
    private static final ProjectStatus UPDATED_STATUS = ProjectStatus.CLOSED;

    private static final String ENTITY_API_URL = "/api/projects";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ProjectRepository projectRepository;

    @Autowired
    private ProjectMapper projectMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restProjectMockMvc;

    private Project project;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Project createEntity(EntityManager em) {
        Project project = new Project()
            .remoteId(DEFAULT_REMOTE_ID)
            .number(DEFAULT_NUMBER)
            .contactName(DEFAULT_CONTACT_NAME)
            .name(DEFAULT_NAME)
            .description(DEFAULT_DESCRIPTION)
            .startDate(DEFAULT_START_DATE)
            .hoursEstimated(DEFAULT_HOURS_ESTIMATED)
            .hourlyRate(DEFAULT_HOURLY_RATE)
            .status(DEFAULT_STATUS);
        return project;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Project createUpdatedEntity(EntityManager em) {
        Project project = new Project()
            .remoteId(UPDATED_REMOTE_ID)
            .number(UPDATED_NUMBER)
            .contactName(UPDATED_CONTACT_NAME)
            .name(UPDATED_NAME)
            .description(UPDATED_DESCRIPTION)
            .startDate(UPDATED_START_DATE)
            .hoursEstimated(UPDATED_HOURS_ESTIMATED)
            .hourlyRate(UPDATED_HOURLY_RATE)
            .status(UPDATED_STATUS);
        return project;
    }

    @BeforeEach
    public void initTest() {
        project = createEntity(em);
    }

    @Test
    @Transactional
    void createProject() throws Exception {
        int databaseSizeBeforeCreate = projectRepository.findAll().size();
        // Create the Project
        ProjectDTO projectDTO = projectMapper.toDto(project);
        restProjectMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(projectDTO)))
            .andExpect(status().isCreated());

        // Validate the Project in the database
        List<Project> projectList = projectRepository.findAll();
        assertThat(projectList).hasSize(databaseSizeBeforeCreate + 1);
        Project testProject = projectList.get(projectList.size() - 1);
        assertThat(testProject.getRemoteId()).isEqualTo(DEFAULT_REMOTE_ID);
        assertThat(testProject.getNumber()).isEqualTo(DEFAULT_NUMBER);
        assertThat(testProject.getContactName()).isEqualTo(DEFAULT_CONTACT_NAME);
        assertThat(testProject.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testProject.getDescription()).isEqualTo(DEFAULT_DESCRIPTION);
        assertThat(testProject.getStartDate()).isEqualTo(DEFAULT_START_DATE);
        assertThat(testProject.getHoursEstimated()).isEqualTo(DEFAULT_HOURS_ESTIMATED);
        assertThat(testProject.getHourlyRate()).isEqualTo(DEFAULT_HOURLY_RATE);
        assertThat(testProject.getStatus()).isEqualTo(DEFAULT_STATUS);
    }

    @Test
    @Transactional
    void createProjectWithExistingId() throws Exception {
        // Create the Project with an existing ID
        project.setId(1L);
        ProjectDTO projectDTO = projectMapper.toDto(project);

        int databaseSizeBeforeCreate = projectRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restProjectMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(projectDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Project in the database
        List<Project> projectList = projectRepository.findAll();
        assertThat(projectList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkNameIsRequired() throws Exception {
        int databaseSizeBeforeTest = projectRepository.findAll().size();
        // set the field null
        project.setName(null);

        // Create the Project, which fails.
        ProjectDTO projectDTO = projectMapper.toDto(project);

        restProjectMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(projectDTO)))
            .andExpect(status().isBadRequest());

        List<Project> projectList = projectRepository.findAll();
        assertThat(projectList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkStartDateIsRequired() throws Exception {
        int databaseSizeBeforeTest = projectRepository.findAll().size();
        // set the field null
        project.setStartDate(null);

        // Create the Project, which fails.
        ProjectDTO projectDTO = projectMapper.toDto(project);

        restProjectMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(projectDTO)))
            .andExpect(status().isBadRequest());

        List<Project> projectList = projectRepository.findAll();
        assertThat(projectList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkStatusIsRequired() throws Exception {
        int databaseSizeBeforeTest = projectRepository.findAll().size();
        // set the field null
        project.setStatus(null);

        // Create the Project, which fails.
        ProjectDTO projectDTO = projectMapper.toDto(project);

        restProjectMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(projectDTO)))
            .andExpect(status().isBadRequest());

        List<Project> projectList = projectRepository.findAll();
        assertThat(projectList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllProjects() throws Exception {
        // Initialize the database
        projectRepository.saveAndFlush(project);

        // Get all the projectList
        restProjectMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(project.getId().intValue())))
            .andExpect(jsonPath("$.[*].remoteId").value(hasItem(DEFAULT_REMOTE_ID)))
            .andExpect(jsonPath("$.[*].number").value(hasItem(DEFAULT_NUMBER)))
            .andExpect(jsonPath("$.[*].contactName").value(hasItem(DEFAULT_CONTACT_NAME)))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION)))
            .andExpect(jsonPath("$.[*].startDate").value(hasItem(DEFAULT_START_DATE.toString())))
            .andExpect(jsonPath("$.[*].hoursEstimated").value(hasItem(DEFAULT_HOURS_ESTIMATED.doubleValue())))
            .andExpect(jsonPath("$.[*].hourlyRate").value(hasItem(DEFAULT_HOURLY_RATE.doubleValue())))
            .andExpect(jsonPath("$.[*].status").value(hasItem(DEFAULT_STATUS.toString())));
    }

    @Test
    @Transactional
    void getProject() throws Exception {
        // Initialize the database
        projectRepository.saveAndFlush(project);

        // Get the project
        restProjectMockMvc
            .perform(get(ENTITY_API_URL_ID, project.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(project.getId().intValue()))
            .andExpect(jsonPath("$.remoteId").value(DEFAULT_REMOTE_ID))
            .andExpect(jsonPath("$.number").value(DEFAULT_NUMBER))
            .andExpect(jsonPath("$.contactName").value(DEFAULT_CONTACT_NAME))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME))
            .andExpect(jsonPath("$.description").value(DEFAULT_DESCRIPTION))
            .andExpect(jsonPath("$.startDate").value(DEFAULT_START_DATE.toString()))
            .andExpect(jsonPath("$.hoursEstimated").value(DEFAULT_HOURS_ESTIMATED.doubleValue()))
            .andExpect(jsonPath("$.hourlyRate").value(DEFAULT_HOURLY_RATE.doubleValue()))
            .andExpect(jsonPath("$.status").value(DEFAULT_STATUS.toString()));
    }

    @Test
    @Transactional
    void getProjectsByIdFiltering() throws Exception {
        // Initialize the database
        projectRepository.saveAndFlush(project);

        Long id = project.getId();

        defaultProjectShouldBeFound("id.equals=" + id);
        defaultProjectShouldNotBeFound("id.notEquals=" + id);

        defaultProjectShouldBeFound("id.greaterThanOrEqual=" + id);
        defaultProjectShouldNotBeFound("id.greaterThan=" + id);

        defaultProjectShouldBeFound("id.lessThanOrEqual=" + id);
        defaultProjectShouldNotBeFound("id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllProjectsByRemoteIdIsEqualToSomething() throws Exception {
        // Initialize the database
        projectRepository.saveAndFlush(project);

        // Get all the projectList where remoteId equals to DEFAULT_REMOTE_ID
        defaultProjectShouldBeFound("remoteId.equals=" + DEFAULT_REMOTE_ID);

        // Get all the projectList where remoteId equals to UPDATED_REMOTE_ID
        defaultProjectShouldNotBeFound("remoteId.equals=" + UPDATED_REMOTE_ID);
    }

    @Test
    @Transactional
    void getAllProjectsByRemoteIdIsNotEqualToSomething() throws Exception {
        // Initialize the database
        projectRepository.saveAndFlush(project);

        // Get all the projectList where remoteId not equals to DEFAULT_REMOTE_ID
        defaultProjectShouldNotBeFound("remoteId.notEquals=" + DEFAULT_REMOTE_ID);

        // Get all the projectList where remoteId not equals to UPDATED_REMOTE_ID
        defaultProjectShouldBeFound("remoteId.notEquals=" + UPDATED_REMOTE_ID);
    }

    @Test
    @Transactional
    void getAllProjectsByRemoteIdIsInShouldWork() throws Exception {
        // Initialize the database
        projectRepository.saveAndFlush(project);

        // Get all the projectList where remoteId in DEFAULT_REMOTE_ID or UPDATED_REMOTE_ID
        defaultProjectShouldBeFound("remoteId.in=" + DEFAULT_REMOTE_ID + "," + UPDATED_REMOTE_ID);

        // Get all the projectList where remoteId equals to UPDATED_REMOTE_ID
        defaultProjectShouldNotBeFound("remoteId.in=" + UPDATED_REMOTE_ID);
    }

    @Test
    @Transactional
    void getAllProjectsByRemoteIdIsNullOrNotNull() throws Exception {
        // Initialize the database
        projectRepository.saveAndFlush(project);

        // Get all the projectList where remoteId is not null
        defaultProjectShouldBeFound("remoteId.specified=true");

        // Get all the projectList where remoteId is null
        defaultProjectShouldNotBeFound("remoteId.specified=false");
    }

    @Test
    @Transactional
    void getAllProjectsByRemoteIdIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        projectRepository.saveAndFlush(project);

        // Get all the projectList where remoteId is greater than or equal to DEFAULT_REMOTE_ID
        defaultProjectShouldBeFound("remoteId.greaterThanOrEqual=" + DEFAULT_REMOTE_ID);

        // Get all the projectList where remoteId is greater than or equal to UPDATED_REMOTE_ID
        defaultProjectShouldNotBeFound("remoteId.greaterThanOrEqual=" + UPDATED_REMOTE_ID);
    }

    @Test
    @Transactional
    void getAllProjectsByRemoteIdIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        projectRepository.saveAndFlush(project);

        // Get all the projectList where remoteId is less than or equal to DEFAULT_REMOTE_ID
        defaultProjectShouldBeFound("remoteId.lessThanOrEqual=" + DEFAULT_REMOTE_ID);

        // Get all the projectList where remoteId is less than or equal to SMALLER_REMOTE_ID
        defaultProjectShouldNotBeFound("remoteId.lessThanOrEqual=" + SMALLER_REMOTE_ID);
    }

    @Test
    @Transactional
    void getAllProjectsByRemoteIdIsLessThanSomething() throws Exception {
        // Initialize the database
        projectRepository.saveAndFlush(project);

        // Get all the projectList where remoteId is less than DEFAULT_REMOTE_ID
        defaultProjectShouldNotBeFound("remoteId.lessThan=" + DEFAULT_REMOTE_ID);

        // Get all the projectList where remoteId is less than UPDATED_REMOTE_ID
        defaultProjectShouldBeFound("remoteId.lessThan=" + UPDATED_REMOTE_ID);
    }

    @Test
    @Transactional
    void getAllProjectsByRemoteIdIsGreaterThanSomething() throws Exception {
        // Initialize the database
        projectRepository.saveAndFlush(project);

        // Get all the projectList where remoteId is greater than DEFAULT_REMOTE_ID
        defaultProjectShouldNotBeFound("remoteId.greaterThan=" + DEFAULT_REMOTE_ID);

        // Get all the projectList where remoteId is greater than SMALLER_REMOTE_ID
        defaultProjectShouldBeFound("remoteId.greaterThan=" + SMALLER_REMOTE_ID);
    }

    @Test
    @Transactional
    void getAllProjectsByNumberIsEqualToSomething() throws Exception {
        // Initialize the database
        projectRepository.saveAndFlush(project);

        // Get all the projectList where number equals to DEFAULT_NUMBER
        defaultProjectShouldBeFound("number.equals=" + DEFAULT_NUMBER);

        // Get all the projectList where number equals to UPDATED_NUMBER
        defaultProjectShouldNotBeFound("number.equals=" + UPDATED_NUMBER);
    }

    @Test
    @Transactional
    void getAllProjectsByNumberIsNotEqualToSomething() throws Exception {
        // Initialize the database
        projectRepository.saveAndFlush(project);

        // Get all the projectList where number not equals to DEFAULT_NUMBER
        defaultProjectShouldNotBeFound("number.notEquals=" + DEFAULT_NUMBER);

        // Get all the projectList where number not equals to UPDATED_NUMBER
        defaultProjectShouldBeFound("number.notEquals=" + UPDATED_NUMBER);
    }

    @Test
    @Transactional
    void getAllProjectsByNumberIsInShouldWork() throws Exception {
        // Initialize the database
        projectRepository.saveAndFlush(project);

        // Get all the projectList where number in DEFAULT_NUMBER or UPDATED_NUMBER
        defaultProjectShouldBeFound("number.in=" + DEFAULT_NUMBER + "," + UPDATED_NUMBER);

        // Get all the projectList where number equals to UPDATED_NUMBER
        defaultProjectShouldNotBeFound("number.in=" + UPDATED_NUMBER);
    }

    @Test
    @Transactional
    void getAllProjectsByNumberIsNullOrNotNull() throws Exception {
        // Initialize the database
        projectRepository.saveAndFlush(project);

        // Get all the projectList where number is not null
        defaultProjectShouldBeFound("number.specified=true");

        // Get all the projectList where number is null
        defaultProjectShouldNotBeFound("number.specified=false");
    }

    @Test
    @Transactional
    void getAllProjectsByNumberContainsSomething() throws Exception {
        // Initialize the database
        projectRepository.saveAndFlush(project);

        // Get all the projectList where number contains DEFAULT_NUMBER
        defaultProjectShouldBeFound("number.contains=" + DEFAULT_NUMBER);

        // Get all the projectList where number contains UPDATED_NUMBER
        defaultProjectShouldNotBeFound("number.contains=" + UPDATED_NUMBER);
    }

    @Test
    @Transactional
    void getAllProjectsByNumberNotContainsSomething() throws Exception {
        // Initialize the database
        projectRepository.saveAndFlush(project);

        // Get all the projectList where number does not contain DEFAULT_NUMBER
        defaultProjectShouldNotBeFound("number.doesNotContain=" + DEFAULT_NUMBER);

        // Get all the projectList where number does not contain UPDATED_NUMBER
        defaultProjectShouldBeFound("number.doesNotContain=" + UPDATED_NUMBER);
    }

    @Test
    @Transactional
    void getAllProjectsByContactNameIsEqualToSomething() throws Exception {
        // Initialize the database
        projectRepository.saveAndFlush(project);

        // Get all the projectList where contactName equals to DEFAULT_CONTACT_NAME
        defaultProjectShouldBeFound("contactName.equals=" + DEFAULT_CONTACT_NAME);

        // Get all the projectList where contactName equals to UPDATED_CONTACT_NAME
        defaultProjectShouldNotBeFound("contactName.equals=" + UPDATED_CONTACT_NAME);
    }

    @Test
    @Transactional
    void getAllProjectsByContactNameIsNotEqualToSomething() throws Exception {
        // Initialize the database
        projectRepository.saveAndFlush(project);

        // Get all the projectList where contactName not equals to DEFAULT_CONTACT_NAME
        defaultProjectShouldNotBeFound("contactName.notEquals=" + DEFAULT_CONTACT_NAME);

        // Get all the projectList where contactName not equals to UPDATED_CONTACT_NAME
        defaultProjectShouldBeFound("contactName.notEquals=" + UPDATED_CONTACT_NAME);
    }

    @Test
    @Transactional
    void getAllProjectsByContactNameIsInShouldWork() throws Exception {
        // Initialize the database
        projectRepository.saveAndFlush(project);

        // Get all the projectList where contactName in DEFAULT_CONTACT_NAME or UPDATED_CONTACT_NAME
        defaultProjectShouldBeFound("contactName.in=" + DEFAULT_CONTACT_NAME + "," + UPDATED_CONTACT_NAME);

        // Get all the projectList where contactName equals to UPDATED_CONTACT_NAME
        defaultProjectShouldNotBeFound("contactName.in=" + UPDATED_CONTACT_NAME);
    }

    @Test
    @Transactional
    void getAllProjectsByContactNameIsNullOrNotNull() throws Exception {
        // Initialize the database
        projectRepository.saveAndFlush(project);

        // Get all the projectList where contactName is not null
        defaultProjectShouldBeFound("contactName.specified=true");

        // Get all the projectList where contactName is null
        defaultProjectShouldNotBeFound("contactName.specified=false");
    }

    @Test
    @Transactional
    void getAllProjectsByContactNameContainsSomething() throws Exception {
        // Initialize the database
        projectRepository.saveAndFlush(project);

        // Get all the projectList where contactName contains DEFAULT_CONTACT_NAME
        defaultProjectShouldBeFound("contactName.contains=" + DEFAULT_CONTACT_NAME);

        // Get all the projectList where contactName contains UPDATED_CONTACT_NAME
        defaultProjectShouldNotBeFound("contactName.contains=" + UPDATED_CONTACT_NAME);
    }

    @Test
    @Transactional
    void getAllProjectsByContactNameNotContainsSomething() throws Exception {
        // Initialize the database
        projectRepository.saveAndFlush(project);

        // Get all the projectList where contactName does not contain DEFAULT_CONTACT_NAME
        defaultProjectShouldNotBeFound("contactName.doesNotContain=" + DEFAULT_CONTACT_NAME);

        // Get all the projectList where contactName does not contain UPDATED_CONTACT_NAME
        defaultProjectShouldBeFound("contactName.doesNotContain=" + UPDATED_CONTACT_NAME);
    }

    @Test
    @Transactional
    void getAllProjectsByNameIsEqualToSomething() throws Exception {
        // Initialize the database
        projectRepository.saveAndFlush(project);

        // Get all the projectList where name equals to DEFAULT_NAME
        defaultProjectShouldBeFound("name.equals=" + DEFAULT_NAME);

        // Get all the projectList where name equals to UPDATED_NAME
        defaultProjectShouldNotBeFound("name.equals=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllProjectsByNameIsNotEqualToSomething() throws Exception {
        // Initialize the database
        projectRepository.saveAndFlush(project);

        // Get all the projectList where name not equals to DEFAULT_NAME
        defaultProjectShouldNotBeFound("name.notEquals=" + DEFAULT_NAME);

        // Get all the projectList where name not equals to UPDATED_NAME
        defaultProjectShouldBeFound("name.notEquals=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllProjectsByNameIsInShouldWork() throws Exception {
        // Initialize the database
        projectRepository.saveAndFlush(project);

        // Get all the projectList where name in DEFAULT_NAME or UPDATED_NAME
        defaultProjectShouldBeFound("name.in=" + DEFAULT_NAME + "," + UPDATED_NAME);

        // Get all the projectList where name equals to UPDATED_NAME
        defaultProjectShouldNotBeFound("name.in=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllProjectsByNameIsNullOrNotNull() throws Exception {
        // Initialize the database
        projectRepository.saveAndFlush(project);

        // Get all the projectList where name is not null
        defaultProjectShouldBeFound("name.specified=true");

        // Get all the projectList where name is null
        defaultProjectShouldNotBeFound("name.specified=false");
    }

    @Test
    @Transactional
    void getAllProjectsByNameContainsSomething() throws Exception {
        // Initialize the database
        projectRepository.saveAndFlush(project);

        // Get all the projectList where name contains DEFAULT_NAME
        defaultProjectShouldBeFound("name.contains=" + DEFAULT_NAME);

        // Get all the projectList where name contains UPDATED_NAME
        defaultProjectShouldNotBeFound("name.contains=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllProjectsByNameNotContainsSomething() throws Exception {
        // Initialize the database
        projectRepository.saveAndFlush(project);

        // Get all the projectList where name does not contain DEFAULT_NAME
        defaultProjectShouldNotBeFound("name.doesNotContain=" + DEFAULT_NAME);

        // Get all the projectList where name does not contain UPDATED_NAME
        defaultProjectShouldBeFound("name.doesNotContain=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllProjectsByDescriptionIsEqualToSomething() throws Exception {
        // Initialize the database
        projectRepository.saveAndFlush(project);

        // Get all the projectList where description equals to DEFAULT_DESCRIPTION
        defaultProjectShouldBeFound("description.equals=" + DEFAULT_DESCRIPTION);

        // Get all the projectList where description equals to UPDATED_DESCRIPTION
        defaultProjectShouldNotBeFound("description.equals=" + UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    void getAllProjectsByDescriptionIsNotEqualToSomething() throws Exception {
        // Initialize the database
        projectRepository.saveAndFlush(project);

        // Get all the projectList where description not equals to DEFAULT_DESCRIPTION
        defaultProjectShouldNotBeFound("description.notEquals=" + DEFAULT_DESCRIPTION);

        // Get all the projectList where description not equals to UPDATED_DESCRIPTION
        defaultProjectShouldBeFound("description.notEquals=" + UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    void getAllProjectsByDescriptionIsInShouldWork() throws Exception {
        // Initialize the database
        projectRepository.saveAndFlush(project);

        // Get all the projectList where description in DEFAULT_DESCRIPTION or UPDATED_DESCRIPTION
        defaultProjectShouldBeFound("description.in=" + DEFAULT_DESCRIPTION + "," + UPDATED_DESCRIPTION);

        // Get all the projectList where description equals to UPDATED_DESCRIPTION
        defaultProjectShouldNotBeFound("description.in=" + UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    void getAllProjectsByDescriptionIsNullOrNotNull() throws Exception {
        // Initialize the database
        projectRepository.saveAndFlush(project);

        // Get all the projectList where description is not null
        defaultProjectShouldBeFound("description.specified=true");

        // Get all the projectList where description is null
        defaultProjectShouldNotBeFound("description.specified=false");
    }

    @Test
    @Transactional
    void getAllProjectsByDescriptionContainsSomething() throws Exception {
        // Initialize the database
        projectRepository.saveAndFlush(project);

        // Get all the projectList where description contains DEFAULT_DESCRIPTION
        defaultProjectShouldBeFound("description.contains=" + DEFAULT_DESCRIPTION);

        // Get all the projectList where description contains UPDATED_DESCRIPTION
        defaultProjectShouldNotBeFound("description.contains=" + UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    void getAllProjectsByDescriptionNotContainsSomething() throws Exception {
        // Initialize the database
        projectRepository.saveAndFlush(project);

        // Get all the projectList where description does not contain DEFAULT_DESCRIPTION
        defaultProjectShouldNotBeFound("description.doesNotContain=" + DEFAULT_DESCRIPTION);

        // Get all the projectList where description does not contain UPDATED_DESCRIPTION
        defaultProjectShouldBeFound("description.doesNotContain=" + UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    void getAllProjectsByStartDateIsEqualToSomething() throws Exception {
        // Initialize the database
        projectRepository.saveAndFlush(project);

        // Get all the projectList where startDate equals to DEFAULT_START_DATE
        defaultProjectShouldBeFound("startDate.equals=" + DEFAULT_START_DATE);

        // Get all the projectList where startDate equals to UPDATED_START_DATE
        defaultProjectShouldNotBeFound("startDate.equals=" + UPDATED_START_DATE);
    }

    @Test
    @Transactional
    void getAllProjectsByStartDateIsNotEqualToSomething() throws Exception {
        // Initialize the database
        projectRepository.saveAndFlush(project);

        // Get all the projectList where startDate not equals to DEFAULT_START_DATE
        defaultProjectShouldNotBeFound("startDate.notEquals=" + DEFAULT_START_DATE);

        // Get all the projectList where startDate not equals to UPDATED_START_DATE
        defaultProjectShouldBeFound("startDate.notEquals=" + UPDATED_START_DATE);
    }

    @Test
    @Transactional
    void getAllProjectsByStartDateIsInShouldWork() throws Exception {
        // Initialize the database
        projectRepository.saveAndFlush(project);

        // Get all the projectList where startDate in DEFAULT_START_DATE or UPDATED_START_DATE
        defaultProjectShouldBeFound("startDate.in=" + DEFAULT_START_DATE + "," + UPDATED_START_DATE);

        // Get all the projectList where startDate equals to UPDATED_START_DATE
        defaultProjectShouldNotBeFound("startDate.in=" + UPDATED_START_DATE);
    }

    @Test
    @Transactional
    void getAllProjectsByStartDateIsNullOrNotNull() throws Exception {
        // Initialize the database
        projectRepository.saveAndFlush(project);

        // Get all the projectList where startDate is not null
        defaultProjectShouldBeFound("startDate.specified=true");

        // Get all the projectList where startDate is null
        defaultProjectShouldNotBeFound("startDate.specified=false");
    }

    @Test
    @Transactional
    void getAllProjectsByStartDateIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        projectRepository.saveAndFlush(project);

        // Get all the projectList where startDate is greater than or equal to DEFAULT_START_DATE
        defaultProjectShouldBeFound("startDate.greaterThanOrEqual=" + DEFAULT_START_DATE);

        // Get all the projectList where startDate is greater than or equal to UPDATED_START_DATE
        defaultProjectShouldNotBeFound("startDate.greaterThanOrEqual=" + UPDATED_START_DATE);
    }

    @Test
    @Transactional
    void getAllProjectsByStartDateIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        projectRepository.saveAndFlush(project);

        // Get all the projectList where startDate is less than or equal to DEFAULT_START_DATE
        defaultProjectShouldBeFound("startDate.lessThanOrEqual=" + DEFAULT_START_DATE);

        // Get all the projectList where startDate is less than or equal to SMALLER_START_DATE
        defaultProjectShouldNotBeFound("startDate.lessThanOrEqual=" + SMALLER_START_DATE);
    }

    @Test
    @Transactional
    void getAllProjectsByStartDateIsLessThanSomething() throws Exception {
        // Initialize the database
        projectRepository.saveAndFlush(project);

        // Get all the projectList where startDate is less than DEFAULT_START_DATE
        defaultProjectShouldNotBeFound("startDate.lessThan=" + DEFAULT_START_DATE);

        // Get all the projectList where startDate is less than UPDATED_START_DATE
        defaultProjectShouldBeFound("startDate.lessThan=" + UPDATED_START_DATE);
    }

    @Test
    @Transactional
    void getAllProjectsByStartDateIsGreaterThanSomething() throws Exception {
        // Initialize the database
        projectRepository.saveAndFlush(project);

        // Get all the projectList where startDate is greater than DEFAULT_START_DATE
        defaultProjectShouldNotBeFound("startDate.greaterThan=" + DEFAULT_START_DATE);

        // Get all the projectList where startDate is greater than SMALLER_START_DATE
        defaultProjectShouldBeFound("startDate.greaterThan=" + SMALLER_START_DATE);
    }

    @Test
    @Transactional
    void getAllProjectsByHoursEstimatedIsEqualToSomething() throws Exception {
        // Initialize the database
        projectRepository.saveAndFlush(project);

        // Get all the projectList where hoursEstimated equals to DEFAULT_HOURS_ESTIMATED
        defaultProjectShouldBeFound("hoursEstimated.equals=" + DEFAULT_HOURS_ESTIMATED);

        // Get all the projectList where hoursEstimated equals to UPDATED_HOURS_ESTIMATED
        defaultProjectShouldNotBeFound("hoursEstimated.equals=" + UPDATED_HOURS_ESTIMATED);
    }

    @Test
    @Transactional
    void getAllProjectsByHoursEstimatedIsNotEqualToSomething() throws Exception {
        // Initialize the database
        projectRepository.saveAndFlush(project);

        // Get all the projectList where hoursEstimated not equals to DEFAULT_HOURS_ESTIMATED
        defaultProjectShouldNotBeFound("hoursEstimated.notEquals=" + DEFAULT_HOURS_ESTIMATED);

        // Get all the projectList where hoursEstimated not equals to UPDATED_HOURS_ESTIMATED
        defaultProjectShouldBeFound("hoursEstimated.notEquals=" + UPDATED_HOURS_ESTIMATED);
    }

    @Test
    @Transactional
    void getAllProjectsByHoursEstimatedIsInShouldWork() throws Exception {
        // Initialize the database
        projectRepository.saveAndFlush(project);

        // Get all the projectList where hoursEstimated in DEFAULT_HOURS_ESTIMATED or UPDATED_HOURS_ESTIMATED
        defaultProjectShouldBeFound("hoursEstimated.in=" + DEFAULT_HOURS_ESTIMATED + "," + UPDATED_HOURS_ESTIMATED);

        // Get all the projectList where hoursEstimated equals to UPDATED_HOURS_ESTIMATED
        defaultProjectShouldNotBeFound("hoursEstimated.in=" + UPDATED_HOURS_ESTIMATED);
    }

    @Test
    @Transactional
    void getAllProjectsByHoursEstimatedIsNullOrNotNull() throws Exception {
        // Initialize the database
        projectRepository.saveAndFlush(project);

        // Get all the projectList where hoursEstimated is not null
        defaultProjectShouldBeFound("hoursEstimated.specified=true");

        // Get all the projectList where hoursEstimated is null
        defaultProjectShouldNotBeFound("hoursEstimated.specified=false");
    }

    @Test
    @Transactional
    void getAllProjectsByHoursEstimatedIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        projectRepository.saveAndFlush(project);

        // Get all the projectList where hoursEstimated is greater than or equal to DEFAULT_HOURS_ESTIMATED
        defaultProjectShouldBeFound("hoursEstimated.greaterThanOrEqual=" + DEFAULT_HOURS_ESTIMATED);

        // Get all the projectList where hoursEstimated is greater than or equal to UPDATED_HOURS_ESTIMATED
        defaultProjectShouldNotBeFound("hoursEstimated.greaterThanOrEqual=" + UPDATED_HOURS_ESTIMATED);
    }

    @Test
    @Transactional
    void getAllProjectsByHoursEstimatedIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        projectRepository.saveAndFlush(project);

        // Get all the projectList where hoursEstimated is less than or equal to DEFAULT_HOURS_ESTIMATED
        defaultProjectShouldBeFound("hoursEstimated.lessThanOrEqual=" + DEFAULT_HOURS_ESTIMATED);

        // Get all the projectList where hoursEstimated is less than or equal to SMALLER_HOURS_ESTIMATED
        defaultProjectShouldNotBeFound("hoursEstimated.lessThanOrEqual=" + SMALLER_HOURS_ESTIMATED);
    }

    @Test
    @Transactional
    void getAllProjectsByHoursEstimatedIsLessThanSomething() throws Exception {
        // Initialize the database
        projectRepository.saveAndFlush(project);

        // Get all the projectList where hoursEstimated is less than DEFAULT_HOURS_ESTIMATED
        defaultProjectShouldNotBeFound("hoursEstimated.lessThan=" + DEFAULT_HOURS_ESTIMATED);

        // Get all the projectList where hoursEstimated is less than UPDATED_HOURS_ESTIMATED
        defaultProjectShouldBeFound("hoursEstimated.lessThan=" + UPDATED_HOURS_ESTIMATED);
    }

    @Test
    @Transactional
    void getAllProjectsByHoursEstimatedIsGreaterThanSomething() throws Exception {
        // Initialize the database
        projectRepository.saveAndFlush(project);

        // Get all the projectList where hoursEstimated is greater than DEFAULT_HOURS_ESTIMATED
        defaultProjectShouldNotBeFound("hoursEstimated.greaterThan=" + DEFAULT_HOURS_ESTIMATED);

        // Get all the projectList where hoursEstimated is greater than SMALLER_HOURS_ESTIMATED
        defaultProjectShouldBeFound("hoursEstimated.greaterThan=" + SMALLER_HOURS_ESTIMATED);
    }

    @Test
    @Transactional
    void getAllProjectsByHourlyRateIsEqualToSomething() throws Exception {
        // Initialize the database
        projectRepository.saveAndFlush(project);

        // Get all the projectList where hourlyRate equals to DEFAULT_HOURLY_RATE
        defaultProjectShouldBeFound("hourlyRate.equals=" + DEFAULT_HOURLY_RATE);

        // Get all the projectList where hourlyRate equals to UPDATED_HOURLY_RATE
        defaultProjectShouldNotBeFound("hourlyRate.equals=" + UPDATED_HOURLY_RATE);
    }

    @Test
    @Transactional
    void getAllProjectsByHourlyRateIsNotEqualToSomething() throws Exception {
        // Initialize the database
        projectRepository.saveAndFlush(project);

        // Get all the projectList where hourlyRate not equals to DEFAULT_HOURLY_RATE
        defaultProjectShouldNotBeFound("hourlyRate.notEquals=" + DEFAULT_HOURLY_RATE);

        // Get all the projectList where hourlyRate not equals to UPDATED_HOURLY_RATE
        defaultProjectShouldBeFound("hourlyRate.notEquals=" + UPDATED_HOURLY_RATE);
    }

    @Test
    @Transactional
    void getAllProjectsByHourlyRateIsInShouldWork() throws Exception {
        // Initialize the database
        projectRepository.saveAndFlush(project);

        // Get all the projectList where hourlyRate in DEFAULT_HOURLY_RATE or UPDATED_HOURLY_RATE
        defaultProjectShouldBeFound("hourlyRate.in=" + DEFAULT_HOURLY_RATE + "," + UPDATED_HOURLY_RATE);

        // Get all the projectList where hourlyRate equals to UPDATED_HOURLY_RATE
        defaultProjectShouldNotBeFound("hourlyRate.in=" + UPDATED_HOURLY_RATE);
    }

    @Test
    @Transactional
    void getAllProjectsByHourlyRateIsNullOrNotNull() throws Exception {
        // Initialize the database
        projectRepository.saveAndFlush(project);

        // Get all the projectList where hourlyRate is not null
        defaultProjectShouldBeFound("hourlyRate.specified=true");

        // Get all the projectList where hourlyRate is null
        defaultProjectShouldNotBeFound("hourlyRate.specified=false");
    }

    @Test
    @Transactional
    void getAllProjectsByHourlyRateIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        projectRepository.saveAndFlush(project);

        // Get all the projectList where hourlyRate is greater than or equal to DEFAULT_HOURLY_RATE
        defaultProjectShouldBeFound("hourlyRate.greaterThanOrEqual=" + DEFAULT_HOURLY_RATE);

        // Get all the projectList where hourlyRate is greater than or equal to UPDATED_HOURLY_RATE
        defaultProjectShouldNotBeFound("hourlyRate.greaterThanOrEqual=" + UPDATED_HOURLY_RATE);
    }

    @Test
    @Transactional
    void getAllProjectsByHourlyRateIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        projectRepository.saveAndFlush(project);

        // Get all the projectList where hourlyRate is less than or equal to DEFAULT_HOURLY_RATE
        defaultProjectShouldBeFound("hourlyRate.lessThanOrEqual=" + DEFAULT_HOURLY_RATE);

        // Get all the projectList where hourlyRate is less than or equal to SMALLER_HOURLY_RATE
        defaultProjectShouldNotBeFound("hourlyRate.lessThanOrEqual=" + SMALLER_HOURLY_RATE);
    }

    @Test
    @Transactional
    void getAllProjectsByHourlyRateIsLessThanSomething() throws Exception {
        // Initialize the database
        projectRepository.saveAndFlush(project);

        // Get all the projectList where hourlyRate is less than DEFAULT_HOURLY_RATE
        defaultProjectShouldNotBeFound("hourlyRate.lessThan=" + DEFAULT_HOURLY_RATE);

        // Get all the projectList where hourlyRate is less than UPDATED_HOURLY_RATE
        defaultProjectShouldBeFound("hourlyRate.lessThan=" + UPDATED_HOURLY_RATE);
    }

    @Test
    @Transactional
    void getAllProjectsByHourlyRateIsGreaterThanSomething() throws Exception {
        // Initialize the database
        projectRepository.saveAndFlush(project);

        // Get all the projectList where hourlyRate is greater than DEFAULT_HOURLY_RATE
        defaultProjectShouldNotBeFound("hourlyRate.greaterThan=" + DEFAULT_HOURLY_RATE);

        // Get all the projectList where hourlyRate is greater than SMALLER_HOURLY_RATE
        defaultProjectShouldBeFound("hourlyRate.greaterThan=" + SMALLER_HOURLY_RATE);
    }

    @Test
    @Transactional
    void getAllProjectsByStatusIsEqualToSomething() throws Exception {
        // Initialize the database
        projectRepository.saveAndFlush(project);

        // Get all the projectList where status equals to DEFAULT_STATUS
        defaultProjectShouldBeFound("status.equals=" + DEFAULT_STATUS);

        // Get all the projectList where status equals to UPDATED_STATUS
        defaultProjectShouldNotBeFound("status.equals=" + UPDATED_STATUS);
    }

    @Test
    @Transactional
    void getAllProjectsByStatusIsNotEqualToSomething() throws Exception {
        // Initialize the database
        projectRepository.saveAndFlush(project);

        // Get all the projectList where status not equals to DEFAULT_STATUS
        defaultProjectShouldNotBeFound("status.notEquals=" + DEFAULT_STATUS);

        // Get all the projectList where status not equals to UPDATED_STATUS
        defaultProjectShouldBeFound("status.notEquals=" + UPDATED_STATUS);
    }

    @Test
    @Transactional
    void getAllProjectsByStatusIsInShouldWork() throws Exception {
        // Initialize the database
        projectRepository.saveAndFlush(project);

        // Get all the projectList where status in DEFAULT_STATUS or UPDATED_STATUS
        defaultProjectShouldBeFound("status.in=" + DEFAULT_STATUS + "," + UPDATED_STATUS);

        // Get all the projectList where status equals to UPDATED_STATUS
        defaultProjectShouldNotBeFound("status.in=" + UPDATED_STATUS);
    }

    @Test
    @Transactional
    void getAllProjectsByStatusIsNullOrNotNull() throws Exception {
        // Initialize the database
        projectRepository.saveAndFlush(project);

        // Get all the projectList where status is not null
        defaultProjectShouldBeFound("status.specified=true");

        // Get all the projectList where status is null
        defaultProjectShouldNotBeFound("status.specified=false");
    }

    @Test
    @Transactional
    void getAllProjectsByCustomFieldsIsEqualToSomething() throws Exception {
        // Initialize the database
        projectRepository.saveAndFlush(project);
        CustomFieldValue customFields = CustomFieldValueResourceIT.createEntity(em);
        em.persist(customFields);
        em.flush();
        project.addCustomFields(customFields);
        projectRepository.saveAndFlush(project);
        Long customFieldsId = customFields.getId();

        // Get all the projectList where customFields equals to customFieldsId
        defaultProjectShouldBeFound("customFieldsId.equals=" + customFieldsId);

        // Get all the projectList where customFields equals to (customFieldsId + 1)
        defaultProjectShouldNotBeFound("customFieldsId.equals=" + (customFieldsId + 1));
    }

    @Test
    @Transactional
    void getAllProjectsByContactIsEqualToSomething() throws Exception {
        // Initialize the database
        projectRepository.saveAndFlush(project);
        Contact contact = ContactResourceIT.createEntity(em);
        em.persist(contact);
        em.flush();
        project.setContact(contact);
        projectRepository.saveAndFlush(project);
        Long contactId = contact.getId();

        // Get all the projectList where contact equals to contactId
        defaultProjectShouldBeFound("contactId.equals=" + contactId);

        // Get all the projectList where contact equals to (contactId + 1)
        defaultProjectShouldNotBeFound("contactId.equals=" + (contactId + 1));
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultProjectShouldBeFound(String filter) throws Exception {
        restProjectMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(project.getId().intValue())))
            .andExpect(jsonPath("$.[*].remoteId").value(hasItem(DEFAULT_REMOTE_ID)))
            .andExpect(jsonPath("$.[*].number").value(hasItem(DEFAULT_NUMBER)))
            .andExpect(jsonPath("$.[*].contactName").value(hasItem(DEFAULT_CONTACT_NAME)))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION)))
            .andExpect(jsonPath("$.[*].startDate").value(hasItem(DEFAULT_START_DATE.toString())))
            .andExpect(jsonPath("$.[*].hoursEstimated").value(hasItem(DEFAULT_HOURS_ESTIMATED.doubleValue())))
            .andExpect(jsonPath("$.[*].hourlyRate").value(hasItem(DEFAULT_HOURLY_RATE.doubleValue())))
            .andExpect(jsonPath("$.[*].status").value(hasItem(DEFAULT_STATUS.toString())));

        // Check, that the count call also returns 1
        restProjectMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultProjectShouldNotBeFound(String filter) throws Exception {
        restProjectMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restProjectMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingProject() throws Exception {
        // Get the project
        restProjectMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewProject() throws Exception {
        // Initialize the database
        projectRepository.saveAndFlush(project);

        int databaseSizeBeforeUpdate = projectRepository.findAll().size();

        // Update the project
        Project updatedProject = projectRepository.findById(project.getId()).get();
        // Disconnect from session so that the updates on updatedProject are not directly saved in db
        em.detach(updatedProject);
        updatedProject
            .remoteId(UPDATED_REMOTE_ID)
            .number(UPDATED_NUMBER)
            .contactName(UPDATED_CONTACT_NAME)
            .name(UPDATED_NAME)
            .description(UPDATED_DESCRIPTION)
            .startDate(UPDATED_START_DATE)
            .hoursEstimated(UPDATED_HOURS_ESTIMATED)
            .hourlyRate(UPDATED_HOURLY_RATE)
            .status(UPDATED_STATUS);
        ProjectDTO projectDTO = projectMapper.toDto(updatedProject);

        restProjectMockMvc
            .perform(
                put(ENTITY_API_URL_ID, projectDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(projectDTO))
            )
            .andExpect(status().isOk());

        // Validate the Project in the database
        List<Project> projectList = projectRepository.findAll();
        assertThat(projectList).hasSize(databaseSizeBeforeUpdate);
        Project testProject = projectList.get(projectList.size() - 1);
        assertThat(testProject.getRemoteId()).isEqualTo(UPDATED_REMOTE_ID);
        assertThat(testProject.getNumber()).isEqualTo(UPDATED_NUMBER);
        assertThat(testProject.getContactName()).isEqualTo(UPDATED_CONTACT_NAME);
        assertThat(testProject.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testProject.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
        assertThat(testProject.getStartDate()).isEqualTo(UPDATED_START_DATE);
        assertThat(testProject.getHoursEstimated()).isEqualTo(UPDATED_HOURS_ESTIMATED);
        assertThat(testProject.getHourlyRate()).isEqualTo(UPDATED_HOURLY_RATE);
        assertThat(testProject.getStatus()).isEqualTo(UPDATED_STATUS);
    }

    @Test
    @Transactional
    void putNonExistingProject() throws Exception {
        int databaseSizeBeforeUpdate = projectRepository.findAll().size();
        project.setId(count.incrementAndGet());

        // Create the Project
        ProjectDTO projectDTO = projectMapper.toDto(project);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restProjectMockMvc
            .perform(
                put(ENTITY_API_URL_ID, projectDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(projectDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Project in the database
        List<Project> projectList = projectRepository.findAll();
        assertThat(projectList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchProject() throws Exception {
        int databaseSizeBeforeUpdate = projectRepository.findAll().size();
        project.setId(count.incrementAndGet());

        // Create the Project
        ProjectDTO projectDTO = projectMapper.toDto(project);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restProjectMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(projectDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Project in the database
        List<Project> projectList = projectRepository.findAll();
        assertThat(projectList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamProject() throws Exception {
        int databaseSizeBeforeUpdate = projectRepository.findAll().size();
        project.setId(count.incrementAndGet());

        // Create the Project
        ProjectDTO projectDTO = projectMapper.toDto(project);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restProjectMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(projectDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Project in the database
        List<Project> projectList = projectRepository.findAll();
        assertThat(projectList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateProjectWithPatch() throws Exception {
        // Initialize the database
        projectRepository.saveAndFlush(project);

        int databaseSizeBeforeUpdate = projectRepository.findAll().size();

        // Update the project using partial update
        Project partialUpdatedProject = new Project();
        partialUpdatedProject.setId(project.getId());

        partialUpdatedProject
            .remoteId(UPDATED_REMOTE_ID)
            .name(UPDATED_NAME)
            .description(UPDATED_DESCRIPTION)
            .hoursEstimated(UPDATED_HOURS_ESTIMATED)
            .hourlyRate(UPDATED_HOURLY_RATE)
            .status(UPDATED_STATUS);

        restProjectMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedProject.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedProject))
            )
            .andExpect(status().isOk());

        // Validate the Project in the database
        List<Project> projectList = projectRepository.findAll();
        assertThat(projectList).hasSize(databaseSizeBeforeUpdate);
        Project testProject = projectList.get(projectList.size() - 1);
        assertThat(testProject.getRemoteId()).isEqualTo(UPDATED_REMOTE_ID);
        assertThat(testProject.getNumber()).isEqualTo(DEFAULT_NUMBER);
        assertThat(testProject.getContactName()).isEqualTo(DEFAULT_CONTACT_NAME);
        assertThat(testProject.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testProject.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
        assertThat(testProject.getStartDate()).isEqualTo(DEFAULT_START_DATE);
        assertThat(testProject.getHoursEstimated()).isEqualTo(UPDATED_HOURS_ESTIMATED);
        assertThat(testProject.getHourlyRate()).isEqualTo(UPDATED_HOURLY_RATE);
        assertThat(testProject.getStatus()).isEqualTo(UPDATED_STATUS);
    }

    @Test
    @Transactional
    void fullUpdateProjectWithPatch() throws Exception {
        // Initialize the database
        projectRepository.saveAndFlush(project);

        int databaseSizeBeforeUpdate = projectRepository.findAll().size();

        // Update the project using partial update
        Project partialUpdatedProject = new Project();
        partialUpdatedProject.setId(project.getId());

        partialUpdatedProject
            .remoteId(UPDATED_REMOTE_ID)
            .number(UPDATED_NUMBER)
            .contactName(UPDATED_CONTACT_NAME)
            .name(UPDATED_NAME)
            .description(UPDATED_DESCRIPTION)
            .startDate(UPDATED_START_DATE)
            .hoursEstimated(UPDATED_HOURS_ESTIMATED)
            .hourlyRate(UPDATED_HOURLY_RATE)
            .status(UPDATED_STATUS);

        restProjectMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedProject.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedProject))
            )
            .andExpect(status().isOk());

        // Validate the Project in the database
        List<Project> projectList = projectRepository.findAll();
        assertThat(projectList).hasSize(databaseSizeBeforeUpdate);
        Project testProject = projectList.get(projectList.size() - 1);
        assertThat(testProject.getRemoteId()).isEqualTo(UPDATED_REMOTE_ID);
        assertThat(testProject.getNumber()).isEqualTo(UPDATED_NUMBER);
        assertThat(testProject.getContactName()).isEqualTo(UPDATED_CONTACT_NAME);
        assertThat(testProject.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testProject.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
        assertThat(testProject.getStartDate()).isEqualTo(UPDATED_START_DATE);
        assertThat(testProject.getHoursEstimated()).isEqualTo(UPDATED_HOURS_ESTIMATED);
        assertThat(testProject.getHourlyRate()).isEqualTo(UPDATED_HOURLY_RATE);
        assertThat(testProject.getStatus()).isEqualTo(UPDATED_STATUS);
    }

    @Test
    @Transactional
    void patchNonExistingProject() throws Exception {
        int databaseSizeBeforeUpdate = projectRepository.findAll().size();
        project.setId(count.incrementAndGet());

        // Create the Project
        ProjectDTO projectDTO = projectMapper.toDto(project);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restProjectMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, projectDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(projectDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Project in the database
        List<Project> projectList = projectRepository.findAll();
        assertThat(projectList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchProject() throws Exception {
        int databaseSizeBeforeUpdate = projectRepository.findAll().size();
        project.setId(count.incrementAndGet());

        // Create the Project
        ProjectDTO projectDTO = projectMapper.toDto(project);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restProjectMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(projectDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Project in the database
        List<Project> projectList = projectRepository.findAll();
        assertThat(projectList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamProject() throws Exception {
        int databaseSizeBeforeUpdate = projectRepository.findAll().size();
        project.setId(count.incrementAndGet());

        // Create the Project
        ProjectDTO projectDTO = projectMapper.toDto(project);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restProjectMockMvc
            .perform(
                patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(projectDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the Project in the database
        List<Project> projectList = projectRepository.findAll();
        assertThat(projectList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteProject() throws Exception {
        // Initialize the database
        projectRepository.saveAndFlush(project);

        int databaseSizeBeforeDelete = projectRepository.findAll().size();

        // Delete the project
        restProjectMockMvc
            .perform(delete(ENTITY_API_URL_ID, project.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Project> projectList = projectRepository.findAll();
        assertThat(projectList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
