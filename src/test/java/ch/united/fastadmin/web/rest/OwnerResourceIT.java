package ch.united.fastadmin.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.elasticsearch.index.query.QueryBuilders.queryStringQuery;
import static org.hamcrest.Matchers.hasItem;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import ch.united.fastadmin.IntegrationTest;
import ch.united.fastadmin.domain.Owner;
import ch.united.fastadmin.domain.enumeration.CurrencyType;
import ch.united.fastadmin.domain.enumeration.LanguageType;
import ch.united.fastadmin.repository.OwnerRepository;
import ch.united.fastadmin.repository.search.OwnerSearchRepository;
import ch.united.fastadmin.service.criteria.OwnerCriteria;
import ch.united.fastadmin.service.dto.OwnerDTO;
import ch.united.fastadmin.service.mapper.OwnerMapper;
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
 * Integration tests for the {@link OwnerResource} REST controller.
 */
@IntegrationTest
@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc
@WithMockUser
class OwnerResourceIT {

    private static final Integer DEFAULT_REMOTE_ID = 1;
    private static final Integer UPDATED_REMOTE_ID = 2;
    private static final Integer SMALLER_REMOTE_ID = 1 - 1;

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_SURNAME = "AAAAAAAAAA";
    private static final String UPDATED_SURNAME = "BBBBBBBBBB";

    private static final String DEFAULT_EMAIL = "AAAAAAAAAA";
    private static final String UPDATED_EMAIL = "BBBBBBBBBB";

    private static final LanguageType DEFAULT_LANGUAGE = LanguageType.FRENCH;
    private static final LanguageType UPDATED_LANGUAGE = LanguageType.ENGLISH;

    private static final String DEFAULT_COMPANY_NAME = "AAAAAAAAAA";
    private static final String UPDATED_COMPANY_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_COMPANY_ADDITION = "AAAAAAAAAA";
    private static final String UPDATED_COMPANY_ADDITION = "BBBBBBBBBB";

    private static final String DEFAULT_COMPANY_COUNTRY = "AAAAAAAAAA";
    private static final String UPDATED_COMPANY_COUNTRY = "BBBBBBBBBB";

    private static final String DEFAULT_COMPANY_STREET = "AAAAAAAAAA";
    private static final String UPDATED_COMPANY_STREET = "BBBBBBBBBB";

    private static final String DEFAULT_COMPANY_STREET_NO = "AAAAAAAAAA";
    private static final String UPDATED_COMPANY_STREET_NO = "BBBBBBBBBB";

    private static final String DEFAULT_COMPANY_STREET_2 = "AAAAAAAAAA";
    private static final String UPDATED_COMPANY_STREET_2 = "BBBBBBBBBB";

    private static final String DEFAULT_COMPANY_POSTCODE = "AAAAAAAAAA";
    private static final String UPDATED_COMPANY_POSTCODE = "BBBBBBBBBB";

    private static final String DEFAULT_COMPANY_CITY = "AAAAAAAAAA";
    private static final String UPDATED_COMPANY_CITY = "BBBBBBBBBB";

    private static final String DEFAULT_COMPANY_PHONE = "AAAAAAAAAA";
    private static final String UPDATED_COMPANY_PHONE = "BBBBBBBBBB";

    private static final String DEFAULT_COMPANY_FAX = "AAAAAAAAAA";
    private static final String UPDATED_COMPANY_FAX = "BBBBBBBBBB";

    private static final String DEFAULT_COMPANY_EMAIL = "AAAAAAAAAA";
    private static final String UPDATED_COMPANY_EMAIL = "BBBBBBBBBB";

    private static final String DEFAULT_COMPANY_WEBSITE = "AAAAAAAAAA";
    private static final String UPDATED_COMPANY_WEBSITE = "BBBBBBBBBB";

    private static final String DEFAULT_COMPANY_VAT_ID = "AAAAAAAAAA";
    private static final String UPDATED_COMPANY_VAT_ID = "BBBBBBBBBB";

    private static final CurrencyType DEFAULT_COMPANY_CURRENCY = CurrencyType.CHF;
    private static final CurrencyType UPDATED_COMPANY_CURRENCY = CurrencyType.EUR;

    private static final String ENTITY_API_URL = "/api/owners";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";
    private static final String ENTITY_SEARCH_API_URL = "/api/_search/owners";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private OwnerRepository ownerRepository;

    @Autowired
    private OwnerMapper ownerMapper;

    /**
     * This repository is mocked in the ch.united.fastadmin.repository.search test package.
     *
     * @see ch.united.fastadmin.repository.search.OwnerSearchRepositoryMockConfiguration
     */
    @Autowired
    private OwnerSearchRepository mockOwnerSearchRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restOwnerMockMvc;

    private Owner owner;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Owner createEntity(EntityManager em) {
        Owner owner = new Owner()
            .remoteId(DEFAULT_REMOTE_ID)
            .name(DEFAULT_NAME)
            .surname(DEFAULT_SURNAME)
            .email(DEFAULT_EMAIL)
            .language(DEFAULT_LANGUAGE)
            .companyName(DEFAULT_COMPANY_NAME)
            .companyAddition(DEFAULT_COMPANY_ADDITION)
            .companyCountry(DEFAULT_COMPANY_COUNTRY)
            .companyStreet(DEFAULT_COMPANY_STREET)
            .companyStreetNo(DEFAULT_COMPANY_STREET_NO)
            .companyStreet2(DEFAULT_COMPANY_STREET_2)
            .companyPostcode(DEFAULT_COMPANY_POSTCODE)
            .companyCity(DEFAULT_COMPANY_CITY)
            .companyPhone(DEFAULT_COMPANY_PHONE)
            .companyFax(DEFAULT_COMPANY_FAX)
            .companyEmail(DEFAULT_COMPANY_EMAIL)
            .companyWebsite(DEFAULT_COMPANY_WEBSITE)
            .companyVatId(DEFAULT_COMPANY_VAT_ID)
            .companyCurrency(DEFAULT_COMPANY_CURRENCY);
        return owner;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Owner createUpdatedEntity(EntityManager em) {
        Owner owner = new Owner()
            .remoteId(UPDATED_REMOTE_ID)
            .name(UPDATED_NAME)
            .surname(UPDATED_SURNAME)
            .email(UPDATED_EMAIL)
            .language(UPDATED_LANGUAGE)
            .companyName(UPDATED_COMPANY_NAME)
            .companyAddition(UPDATED_COMPANY_ADDITION)
            .companyCountry(UPDATED_COMPANY_COUNTRY)
            .companyStreet(UPDATED_COMPANY_STREET)
            .companyStreetNo(UPDATED_COMPANY_STREET_NO)
            .companyStreet2(UPDATED_COMPANY_STREET_2)
            .companyPostcode(UPDATED_COMPANY_POSTCODE)
            .companyCity(UPDATED_COMPANY_CITY)
            .companyPhone(UPDATED_COMPANY_PHONE)
            .companyFax(UPDATED_COMPANY_FAX)
            .companyEmail(UPDATED_COMPANY_EMAIL)
            .companyWebsite(UPDATED_COMPANY_WEBSITE)
            .companyVatId(UPDATED_COMPANY_VAT_ID)
            .companyCurrency(UPDATED_COMPANY_CURRENCY);
        return owner;
    }

    @BeforeEach
    public void initTest() {
        owner = createEntity(em);
    }

    @Test
    @Transactional
    void createOwner() throws Exception {
        int databaseSizeBeforeCreate = ownerRepository.findAll().size();
        // Create the Owner
        OwnerDTO ownerDTO = ownerMapper.toDto(owner);
        restOwnerMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(ownerDTO)))
            .andExpect(status().isCreated());

        // Validate the Owner in the database
        List<Owner> ownerList = ownerRepository.findAll();
        assertThat(ownerList).hasSize(databaseSizeBeforeCreate + 1);
        Owner testOwner = ownerList.get(ownerList.size() - 1);
        assertThat(testOwner.getRemoteId()).isEqualTo(DEFAULT_REMOTE_ID);
        assertThat(testOwner.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testOwner.getSurname()).isEqualTo(DEFAULT_SURNAME);
        assertThat(testOwner.getEmail()).isEqualTo(DEFAULT_EMAIL);
        assertThat(testOwner.getLanguage()).isEqualTo(DEFAULT_LANGUAGE);
        assertThat(testOwner.getCompanyName()).isEqualTo(DEFAULT_COMPANY_NAME);
        assertThat(testOwner.getCompanyAddition()).isEqualTo(DEFAULT_COMPANY_ADDITION);
        assertThat(testOwner.getCompanyCountry()).isEqualTo(DEFAULT_COMPANY_COUNTRY);
        assertThat(testOwner.getCompanyStreet()).isEqualTo(DEFAULT_COMPANY_STREET);
        assertThat(testOwner.getCompanyStreetNo()).isEqualTo(DEFAULT_COMPANY_STREET_NO);
        assertThat(testOwner.getCompanyStreet2()).isEqualTo(DEFAULT_COMPANY_STREET_2);
        assertThat(testOwner.getCompanyPostcode()).isEqualTo(DEFAULT_COMPANY_POSTCODE);
        assertThat(testOwner.getCompanyCity()).isEqualTo(DEFAULT_COMPANY_CITY);
        assertThat(testOwner.getCompanyPhone()).isEqualTo(DEFAULT_COMPANY_PHONE);
        assertThat(testOwner.getCompanyFax()).isEqualTo(DEFAULT_COMPANY_FAX);
        assertThat(testOwner.getCompanyEmail()).isEqualTo(DEFAULT_COMPANY_EMAIL);
        assertThat(testOwner.getCompanyWebsite()).isEqualTo(DEFAULT_COMPANY_WEBSITE);
        assertThat(testOwner.getCompanyVatId()).isEqualTo(DEFAULT_COMPANY_VAT_ID);
        assertThat(testOwner.getCompanyCurrency()).isEqualTo(DEFAULT_COMPANY_CURRENCY);

        // Validate the Owner in Elasticsearch
        verify(mockOwnerSearchRepository, times(1)).save(testOwner);
    }

    @Test
    @Transactional
    void createOwnerWithExistingId() throws Exception {
        // Create the Owner with an existing ID
        owner.setId(1L);
        OwnerDTO ownerDTO = ownerMapper.toDto(owner);

        int databaseSizeBeforeCreate = ownerRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restOwnerMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(ownerDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Owner in the database
        List<Owner> ownerList = ownerRepository.findAll();
        assertThat(ownerList).hasSize(databaseSizeBeforeCreate);

        // Validate the Owner in Elasticsearch
        verify(mockOwnerSearchRepository, times(0)).save(owner);
    }

    @Test
    @Transactional
    void getAllOwners() throws Exception {
        // Initialize the database
        ownerRepository.saveAndFlush(owner);

        // Get all the ownerList
        restOwnerMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(owner.getId().intValue())))
            .andExpect(jsonPath("$.[*].remoteId").value(hasItem(DEFAULT_REMOTE_ID)))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)))
            .andExpect(jsonPath("$.[*].surname").value(hasItem(DEFAULT_SURNAME)))
            .andExpect(jsonPath("$.[*].email").value(hasItem(DEFAULT_EMAIL)))
            .andExpect(jsonPath("$.[*].language").value(hasItem(DEFAULT_LANGUAGE.toString())))
            .andExpect(jsonPath("$.[*].companyName").value(hasItem(DEFAULT_COMPANY_NAME)))
            .andExpect(jsonPath("$.[*].companyAddition").value(hasItem(DEFAULT_COMPANY_ADDITION)))
            .andExpect(jsonPath("$.[*].companyCountry").value(hasItem(DEFAULT_COMPANY_COUNTRY)))
            .andExpect(jsonPath("$.[*].companyStreet").value(hasItem(DEFAULT_COMPANY_STREET)))
            .andExpect(jsonPath("$.[*].companyStreetNo").value(hasItem(DEFAULT_COMPANY_STREET_NO)))
            .andExpect(jsonPath("$.[*].companyStreet2").value(hasItem(DEFAULT_COMPANY_STREET_2)))
            .andExpect(jsonPath("$.[*].companyPostcode").value(hasItem(DEFAULT_COMPANY_POSTCODE)))
            .andExpect(jsonPath("$.[*].companyCity").value(hasItem(DEFAULT_COMPANY_CITY)))
            .andExpect(jsonPath("$.[*].companyPhone").value(hasItem(DEFAULT_COMPANY_PHONE)))
            .andExpect(jsonPath("$.[*].companyFax").value(hasItem(DEFAULT_COMPANY_FAX)))
            .andExpect(jsonPath("$.[*].companyEmail").value(hasItem(DEFAULT_COMPANY_EMAIL)))
            .andExpect(jsonPath("$.[*].companyWebsite").value(hasItem(DEFAULT_COMPANY_WEBSITE)))
            .andExpect(jsonPath("$.[*].companyVatId").value(hasItem(DEFAULT_COMPANY_VAT_ID)))
            .andExpect(jsonPath("$.[*].companyCurrency").value(hasItem(DEFAULT_COMPANY_CURRENCY.toString())));
    }

    @Test
    @Transactional
    void getOwner() throws Exception {
        // Initialize the database
        ownerRepository.saveAndFlush(owner);

        // Get the owner
        restOwnerMockMvc
            .perform(get(ENTITY_API_URL_ID, owner.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(owner.getId().intValue()))
            .andExpect(jsonPath("$.remoteId").value(DEFAULT_REMOTE_ID))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME))
            .andExpect(jsonPath("$.surname").value(DEFAULT_SURNAME))
            .andExpect(jsonPath("$.email").value(DEFAULT_EMAIL))
            .andExpect(jsonPath("$.language").value(DEFAULT_LANGUAGE.toString()))
            .andExpect(jsonPath("$.companyName").value(DEFAULT_COMPANY_NAME))
            .andExpect(jsonPath("$.companyAddition").value(DEFAULT_COMPANY_ADDITION))
            .andExpect(jsonPath("$.companyCountry").value(DEFAULT_COMPANY_COUNTRY))
            .andExpect(jsonPath("$.companyStreet").value(DEFAULT_COMPANY_STREET))
            .andExpect(jsonPath("$.companyStreetNo").value(DEFAULT_COMPANY_STREET_NO))
            .andExpect(jsonPath("$.companyStreet2").value(DEFAULT_COMPANY_STREET_2))
            .andExpect(jsonPath("$.companyPostcode").value(DEFAULT_COMPANY_POSTCODE))
            .andExpect(jsonPath("$.companyCity").value(DEFAULT_COMPANY_CITY))
            .andExpect(jsonPath("$.companyPhone").value(DEFAULT_COMPANY_PHONE))
            .andExpect(jsonPath("$.companyFax").value(DEFAULT_COMPANY_FAX))
            .andExpect(jsonPath("$.companyEmail").value(DEFAULT_COMPANY_EMAIL))
            .andExpect(jsonPath("$.companyWebsite").value(DEFAULT_COMPANY_WEBSITE))
            .andExpect(jsonPath("$.companyVatId").value(DEFAULT_COMPANY_VAT_ID))
            .andExpect(jsonPath("$.companyCurrency").value(DEFAULT_COMPANY_CURRENCY.toString()));
    }

    @Test
    @Transactional
    void getOwnersByIdFiltering() throws Exception {
        // Initialize the database
        ownerRepository.saveAndFlush(owner);

        Long id = owner.getId();

        defaultOwnerShouldBeFound("id.equals=" + id);
        defaultOwnerShouldNotBeFound("id.notEquals=" + id);

        defaultOwnerShouldBeFound("id.greaterThanOrEqual=" + id);
        defaultOwnerShouldNotBeFound("id.greaterThan=" + id);

        defaultOwnerShouldBeFound("id.lessThanOrEqual=" + id);
        defaultOwnerShouldNotBeFound("id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllOwnersByRemoteIdIsEqualToSomething() throws Exception {
        // Initialize the database
        ownerRepository.saveAndFlush(owner);

        // Get all the ownerList where remoteId equals to DEFAULT_REMOTE_ID
        defaultOwnerShouldBeFound("remoteId.equals=" + DEFAULT_REMOTE_ID);

        // Get all the ownerList where remoteId equals to UPDATED_REMOTE_ID
        defaultOwnerShouldNotBeFound("remoteId.equals=" + UPDATED_REMOTE_ID);
    }

    @Test
    @Transactional
    void getAllOwnersByRemoteIdIsNotEqualToSomething() throws Exception {
        // Initialize the database
        ownerRepository.saveAndFlush(owner);

        // Get all the ownerList where remoteId not equals to DEFAULT_REMOTE_ID
        defaultOwnerShouldNotBeFound("remoteId.notEquals=" + DEFAULT_REMOTE_ID);

        // Get all the ownerList where remoteId not equals to UPDATED_REMOTE_ID
        defaultOwnerShouldBeFound("remoteId.notEquals=" + UPDATED_REMOTE_ID);
    }

    @Test
    @Transactional
    void getAllOwnersByRemoteIdIsInShouldWork() throws Exception {
        // Initialize the database
        ownerRepository.saveAndFlush(owner);

        // Get all the ownerList where remoteId in DEFAULT_REMOTE_ID or UPDATED_REMOTE_ID
        defaultOwnerShouldBeFound("remoteId.in=" + DEFAULT_REMOTE_ID + "," + UPDATED_REMOTE_ID);

        // Get all the ownerList where remoteId equals to UPDATED_REMOTE_ID
        defaultOwnerShouldNotBeFound("remoteId.in=" + UPDATED_REMOTE_ID);
    }

    @Test
    @Transactional
    void getAllOwnersByRemoteIdIsNullOrNotNull() throws Exception {
        // Initialize the database
        ownerRepository.saveAndFlush(owner);

        // Get all the ownerList where remoteId is not null
        defaultOwnerShouldBeFound("remoteId.specified=true");

        // Get all the ownerList where remoteId is null
        defaultOwnerShouldNotBeFound("remoteId.specified=false");
    }

    @Test
    @Transactional
    void getAllOwnersByRemoteIdIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        ownerRepository.saveAndFlush(owner);

        // Get all the ownerList where remoteId is greater than or equal to DEFAULT_REMOTE_ID
        defaultOwnerShouldBeFound("remoteId.greaterThanOrEqual=" + DEFAULT_REMOTE_ID);

        // Get all the ownerList where remoteId is greater than or equal to UPDATED_REMOTE_ID
        defaultOwnerShouldNotBeFound("remoteId.greaterThanOrEqual=" + UPDATED_REMOTE_ID);
    }

    @Test
    @Transactional
    void getAllOwnersByRemoteIdIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        ownerRepository.saveAndFlush(owner);

        // Get all the ownerList where remoteId is less than or equal to DEFAULT_REMOTE_ID
        defaultOwnerShouldBeFound("remoteId.lessThanOrEqual=" + DEFAULT_REMOTE_ID);

        // Get all the ownerList where remoteId is less than or equal to SMALLER_REMOTE_ID
        defaultOwnerShouldNotBeFound("remoteId.lessThanOrEqual=" + SMALLER_REMOTE_ID);
    }

    @Test
    @Transactional
    void getAllOwnersByRemoteIdIsLessThanSomething() throws Exception {
        // Initialize the database
        ownerRepository.saveAndFlush(owner);

        // Get all the ownerList where remoteId is less than DEFAULT_REMOTE_ID
        defaultOwnerShouldNotBeFound("remoteId.lessThan=" + DEFAULT_REMOTE_ID);

        // Get all the ownerList where remoteId is less than UPDATED_REMOTE_ID
        defaultOwnerShouldBeFound("remoteId.lessThan=" + UPDATED_REMOTE_ID);
    }

    @Test
    @Transactional
    void getAllOwnersByRemoteIdIsGreaterThanSomething() throws Exception {
        // Initialize the database
        ownerRepository.saveAndFlush(owner);

        // Get all the ownerList where remoteId is greater than DEFAULT_REMOTE_ID
        defaultOwnerShouldNotBeFound("remoteId.greaterThan=" + DEFAULT_REMOTE_ID);

        // Get all the ownerList where remoteId is greater than SMALLER_REMOTE_ID
        defaultOwnerShouldBeFound("remoteId.greaterThan=" + SMALLER_REMOTE_ID);
    }

    @Test
    @Transactional
    void getAllOwnersByNameIsEqualToSomething() throws Exception {
        // Initialize the database
        ownerRepository.saveAndFlush(owner);

        // Get all the ownerList where name equals to DEFAULT_NAME
        defaultOwnerShouldBeFound("name.equals=" + DEFAULT_NAME);

        // Get all the ownerList where name equals to UPDATED_NAME
        defaultOwnerShouldNotBeFound("name.equals=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllOwnersByNameIsNotEqualToSomething() throws Exception {
        // Initialize the database
        ownerRepository.saveAndFlush(owner);

        // Get all the ownerList where name not equals to DEFAULT_NAME
        defaultOwnerShouldNotBeFound("name.notEquals=" + DEFAULT_NAME);

        // Get all the ownerList where name not equals to UPDATED_NAME
        defaultOwnerShouldBeFound("name.notEquals=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllOwnersByNameIsInShouldWork() throws Exception {
        // Initialize the database
        ownerRepository.saveAndFlush(owner);

        // Get all the ownerList where name in DEFAULT_NAME or UPDATED_NAME
        defaultOwnerShouldBeFound("name.in=" + DEFAULT_NAME + "," + UPDATED_NAME);

        // Get all the ownerList where name equals to UPDATED_NAME
        defaultOwnerShouldNotBeFound("name.in=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllOwnersByNameIsNullOrNotNull() throws Exception {
        // Initialize the database
        ownerRepository.saveAndFlush(owner);

        // Get all the ownerList where name is not null
        defaultOwnerShouldBeFound("name.specified=true");

        // Get all the ownerList where name is null
        defaultOwnerShouldNotBeFound("name.specified=false");
    }

    @Test
    @Transactional
    void getAllOwnersByNameContainsSomething() throws Exception {
        // Initialize the database
        ownerRepository.saveAndFlush(owner);

        // Get all the ownerList where name contains DEFAULT_NAME
        defaultOwnerShouldBeFound("name.contains=" + DEFAULT_NAME);

        // Get all the ownerList where name contains UPDATED_NAME
        defaultOwnerShouldNotBeFound("name.contains=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllOwnersByNameNotContainsSomething() throws Exception {
        // Initialize the database
        ownerRepository.saveAndFlush(owner);

        // Get all the ownerList where name does not contain DEFAULT_NAME
        defaultOwnerShouldNotBeFound("name.doesNotContain=" + DEFAULT_NAME);

        // Get all the ownerList where name does not contain UPDATED_NAME
        defaultOwnerShouldBeFound("name.doesNotContain=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllOwnersBySurnameIsEqualToSomething() throws Exception {
        // Initialize the database
        ownerRepository.saveAndFlush(owner);

        // Get all the ownerList where surname equals to DEFAULT_SURNAME
        defaultOwnerShouldBeFound("surname.equals=" + DEFAULT_SURNAME);

        // Get all the ownerList where surname equals to UPDATED_SURNAME
        defaultOwnerShouldNotBeFound("surname.equals=" + UPDATED_SURNAME);
    }

    @Test
    @Transactional
    void getAllOwnersBySurnameIsNotEqualToSomething() throws Exception {
        // Initialize the database
        ownerRepository.saveAndFlush(owner);

        // Get all the ownerList where surname not equals to DEFAULT_SURNAME
        defaultOwnerShouldNotBeFound("surname.notEquals=" + DEFAULT_SURNAME);

        // Get all the ownerList where surname not equals to UPDATED_SURNAME
        defaultOwnerShouldBeFound("surname.notEquals=" + UPDATED_SURNAME);
    }

    @Test
    @Transactional
    void getAllOwnersBySurnameIsInShouldWork() throws Exception {
        // Initialize the database
        ownerRepository.saveAndFlush(owner);

        // Get all the ownerList where surname in DEFAULT_SURNAME or UPDATED_SURNAME
        defaultOwnerShouldBeFound("surname.in=" + DEFAULT_SURNAME + "," + UPDATED_SURNAME);

        // Get all the ownerList where surname equals to UPDATED_SURNAME
        defaultOwnerShouldNotBeFound("surname.in=" + UPDATED_SURNAME);
    }

    @Test
    @Transactional
    void getAllOwnersBySurnameIsNullOrNotNull() throws Exception {
        // Initialize the database
        ownerRepository.saveAndFlush(owner);

        // Get all the ownerList where surname is not null
        defaultOwnerShouldBeFound("surname.specified=true");

        // Get all the ownerList where surname is null
        defaultOwnerShouldNotBeFound("surname.specified=false");
    }

    @Test
    @Transactional
    void getAllOwnersBySurnameContainsSomething() throws Exception {
        // Initialize the database
        ownerRepository.saveAndFlush(owner);

        // Get all the ownerList where surname contains DEFAULT_SURNAME
        defaultOwnerShouldBeFound("surname.contains=" + DEFAULT_SURNAME);

        // Get all the ownerList where surname contains UPDATED_SURNAME
        defaultOwnerShouldNotBeFound("surname.contains=" + UPDATED_SURNAME);
    }

    @Test
    @Transactional
    void getAllOwnersBySurnameNotContainsSomething() throws Exception {
        // Initialize the database
        ownerRepository.saveAndFlush(owner);

        // Get all the ownerList where surname does not contain DEFAULT_SURNAME
        defaultOwnerShouldNotBeFound("surname.doesNotContain=" + DEFAULT_SURNAME);

        // Get all the ownerList where surname does not contain UPDATED_SURNAME
        defaultOwnerShouldBeFound("surname.doesNotContain=" + UPDATED_SURNAME);
    }

    @Test
    @Transactional
    void getAllOwnersByEmailIsEqualToSomething() throws Exception {
        // Initialize the database
        ownerRepository.saveAndFlush(owner);

        // Get all the ownerList where email equals to DEFAULT_EMAIL
        defaultOwnerShouldBeFound("email.equals=" + DEFAULT_EMAIL);

        // Get all the ownerList where email equals to UPDATED_EMAIL
        defaultOwnerShouldNotBeFound("email.equals=" + UPDATED_EMAIL);
    }

    @Test
    @Transactional
    void getAllOwnersByEmailIsNotEqualToSomething() throws Exception {
        // Initialize the database
        ownerRepository.saveAndFlush(owner);

        // Get all the ownerList where email not equals to DEFAULT_EMAIL
        defaultOwnerShouldNotBeFound("email.notEquals=" + DEFAULT_EMAIL);

        // Get all the ownerList where email not equals to UPDATED_EMAIL
        defaultOwnerShouldBeFound("email.notEquals=" + UPDATED_EMAIL);
    }

    @Test
    @Transactional
    void getAllOwnersByEmailIsInShouldWork() throws Exception {
        // Initialize the database
        ownerRepository.saveAndFlush(owner);

        // Get all the ownerList where email in DEFAULT_EMAIL or UPDATED_EMAIL
        defaultOwnerShouldBeFound("email.in=" + DEFAULT_EMAIL + "," + UPDATED_EMAIL);

        // Get all the ownerList where email equals to UPDATED_EMAIL
        defaultOwnerShouldNotBeFound("email.in=" + UPDATED_EMAIL);
    }

    @Test
    @Transactional
    void getAllOwnersByEmailIsNullOrNotNull() throws Exception {
        // Initialize the database
        ownerRepository.saveAndFlush(owner);

        // Get all the ownerList where email is not null
        defaultOwnerShouldBeFound("email.specified=true");

        // Get all the ownerList where email is null
        defaultOwnerShouldNotBeFound("email.specified=false");
    }

    @Test
    @Transactional
    void getAllOwnersByEmailContainsSomething() throws Exception {
        // Initialize the database
        ownerRepository.saveAndFlush(owner);

        // Get all the ownerList where email contains DEFAULT_EMAIL
        defaultOwnerShouldBeFound("email.contains=" + DEFAULT_EMAIL);

        // Get all the ownerList where email contains UPDATED_EMAIL
        defaultOwnerShouldNotBeFound("email.contains=" + UPDATED_EMAIL);
    }

    @Test
    @Transactional
    void getAllOwnersByEmailNotContainsSomething() throws Exception {
        // Initialize the database
        ownerRepository.saveAndFlush(owner);

        // Get all the ownerList where email does not contain DEFAULT_EMAIL
        defaultOwnerShouldNotBeFound("email.doesNotContain=" + DEFAULT_EMAIL);

        // Get all the ownerList where email does not contain UPDATED_EMAIL
        defaultOwnerShouldBeFound("email.doesNotContain=" + UPDATED_EMAIL);
    }

    @Test
    @Transactional
    void getAllOwnersByLanguageIsEqualToSomething() throws Exception {
        // Initialize the database
        ownerRepository.saveAndFlush(owner);

        // Get all the ownerList where language equals to DEFAULT_LANGUAGE
        defaultOwnerShouldBeFound("language.equals=" + DEFAULT_LANGUAGE);

        // Get all the ownerList where language equals to UPDATED_LANGUAGE
        defaultOwnerShouldNotBeFound("language.equals=" + UPDATED_LANGUAGE);
    }

    @Test
    @Transactional
    void getAllOwnersByLanguageIsNotEqualToSomething() throws Exception {
        // Initialize the database
        ownerRepository.saveAndFlush(owner);

        // Get all the ownerList where language not equals to DEFAULT_LANGUAGE
        defaultOwnerShouldNotBeFound("language.notEquals=" + DEFAULT_LANGUAGE);

        // Get all the ownerList where language not equals to UPDATED_LANGUAGE
        defaultOwnerShouldBeFound("language.notEquals=" + UPDATED_LANGUAGE);
    }

    @Test
    @Transactional
    void getAllOwnersByLanguageIsInShouldWork() throws Exception {
        // Initialize the database
        ownerRepository.saveAndFlush(owner);

        // Get all the ownerList where language in DEFAULT_LANGUAGE or UPDATED_LANGUAGE
        defaultOwnerShouldBeFound("language.in=" + DEFAULT_LANGUAGE + "," + UPDATED_LANGUAGE);

        // Get all the ownerList where language equals to UPDATED_LANGUAGE
        defaultOwnerShouldNotBeFound("language.in=" + UPDATED_LANGUAGE);
    }

    @Test
    @Transactional
    void getAllOwnersByLanguageIsNullOrNotNull() throws Exception {
        // Initialize the database
        ownerRepository.saveAndFlush(owner);

        // Get all the ownerList where language is not null
        defaultOwnerShouldBeFound("language.specified=true");

        // Get all the ownerList where language is null
        defaultOwnerShouldNotBeFound("language.specified=false");
    }

    @Test
    @Transactional
    void getAllOwnersByCompanyNameIsEqualToSomething() throws Exception {
        // Initialize the database
        ownerRepository.saveAndFlush(owner);

        // Get all the ownerList where companyName equals to DEFAULT_COMPANY_NAME
        defaultOwnerShouldBeFound("companyName.equals=" + DEFAULT_COMPANY_NAME);

        // Get all the ownerList where companyName equals to UPDATED_COMPANY_NAME
        defaultOwnerShouldNotBeFound("companyName.equals=" + UPDATED_COMPANY_NAME);
    }

    @Test
    @Transactional
    void getAllOwnersByCompanyNameIsNotEqualToSomething() throws Exception {
        // Initialize the database
        ownerRepository.saveAndFlush(owner);

        // Get all the ownerList where companyName not equals to DEFAULT_COMPANY_NAME
        defaultOwnerShouldNotBeFound("companyName.notEquals=" + DEFAULT_COMPANY_NAME);

        // Get all the ownerList where companyName not equals to UPDATED_COMPANY_NAME
        defaultOwnerShouldBeFound("companyName.notEquals=" + UPDATED_COMPANY_NAME);
    }

    @Test
    @Transactional
    void getAllOwnersByCompanyNameIsInShouldWork() throws Exception {
        // Initialize the database
        ownerRepository.saveAndFlush(owner);

        // Get all the ownerList where companyName in DEFAULT_COMPANY_NAME or UPDATED_COMPANY_NAME
        defaultOwnerShouldBeFound("companyName.in=" + DEFAULT_COMPANY_NAME + "," + UPDATED_COMPANY_NAME);

        // Get all the ownerList where companyName equals to UPDATED_COMPANY_NAME
        defaultOwnerShouldNotBeFound("companyName.in=" + UPDATED_COMPANY_NAME);
    }

    @Test
    @Transactional
    void getAllOwnersByCompanyNameIsNullOrNotNull() throws Exception {
        // Initialize the database
        ownerRepository.saveAndFlush(owner);

        // Get all the ownerList where companyName is not null
        defaultOwnerShouldBeFound("companyName.specified=true");

        // Get all the ownerList where companyName is null
        defaultOwnerShouldNotBeFound("companyName.specified=false");
    }

    @Test
    @Transactional
    void getAllOwnersByCompanyNameContainsSomething() throws Exception {
        // Initialize the database
        ownerRepository.saveAndFlush(owner);

        // Get all the ownerList where companyName contains DEFAULT_COMPANY_NAME
        defaultOwnerShouldBeFound("companyName.contains=" + DEFAULT_COMPANY_NAME);

        // Get all the ownerList where companyName contains UPDATED_COMPANY_NAME
        defaultOwnerShouldNotBeFound("companyName.contains=" + UPDATED_COMPANY_NAME);
    }

    @Test
    @Transactional
    void getAllOwnersByCompanyNameNotContainsSomething() throws Exception {
        // Initialize the database
        ownerRepository.saveAndFlush(owner);

        // Get all the ownerList where companyName does not contain DEFAULT_COMPANY_NAME
        defaultOwnerShouldNotBeFound("companyName.doesNotContain=" + DEFAULT_COMPANY_NAME);

        // Get all the ownerList where companyName does not contain UPDATED_COMPANY_NAME
        defaultOwnerShouldBeFound("companyName.doesNotContain=" + UPDATED_COMPANY_NAME);
    }

    @Test
    @Transactional
    void getAllOwnersByCompanyAdditionIsEqualToSomething() throws Exception {
        // Initialize the database
        ownerRepository.saveAndFlush(owner);

        // Get all the ownerList where companyAddition equals to DEFAULT_COMPANY_ADDITION
        defaultOwnerShouldBeFound("companyAddition.equals=" + DEFAULT_COMPANY_ADDITION);

        // Get all the ownerList where companyAddition equals to UPDATED_COMPANY_ADDITION
        defaultOwnerShouldNotBeFound("companyAddition.equals=" + UPDATED_COMPANY_ADDITION);
    }

    @Test
    @Transactional
    void getAllOwnersByCompanyAdditionIsNotEqualToSomething() throws Exception {
        // Initialize the database
        ownerRepository.saveAndFlush(owner);

        // Get all the ownerList where companyAddition not equals to DEFAULT_COMPANY_ADDITION
        defaultOwnerShouldNotBeFound("companyAddition.notEquals=" + DEFAULT_COMPANY_ADDITION);

        // Get all the ownerList where companyAddition not equals to UPDATED_COMPANY_ADDITION
        defaultOwnerShouldBeFound("companyAddition.notEquals=" + UPDATED_COMPANY_ADDITION);
    }

    @Test
    @Transactional
    void getAllOwnersByCompanyAdditionIsInShouldWork() throws Exception {
        // Initialize the database
        ownerRepository.saveAndFlush(owner);

        // Get all the ownerList where companyAddition in DEFAULT_COMPANY_ADDITION or UPDATED_COMPANY_ADDITION
        defaultOwnerShouldBeFound("companyAddition.in=" + DEFAULT_COMPANY_ADDITION + "," + UPDATED_COMPANY_ADDITION);

        // Get all the ownerList where companyAddition equals to UPDATED_COMPANY_ADDITION
        defaultOwnerShouldNotBeFound("companyAddition.in=" + UPDATED_COMPANY_ADDITION);
    }

    @Test
    @Transactional
    void getAllOwnersByCompanyAdditionIsNullOrNotNull() throws Exception {
        // Initialize the database
        ownerRepository.saveAndFlush(owner);

        // Get all the ownerList where companyAddition is not null
        defaultOwnerShouldBeFound("companyAddition.specified=true");

        // Get all the ownerList where companyAddition is null
        defaultOwnerShouldNotBeFound("companyAddition.specified=false");
    }

    @Test
    @Transactional
    void getAllOwnersByCompanyAdditionContainsSomething() throws Exception {
        // Initialize the database
        ownerRepository.saveAndFlush(owner);

        // Get all the ownerList where companyAddition contains DEFAULT_COMPANY_ADDITION
        defaultOwnerShouldBeFound("companyAddition.contains=" + DEFAULT_COMPANY_ADDITION);

        // Get all the ownerList where companyAddition contains UPDATED_COMPANY_ADDITION
        defaultOwnerShouldNotBeFound("companyAddition.contains=" + UPDATED_COMPANY_ADDITION);
    }

    @Test
    @Transactional
    void getAllOwnersByCompanyAdditionNotContainsSomething() throws Exception {
        // Initialize the database
        ownerRepository.saveAndFlush(owner);

        // Get all the ownerList where companyAddition does not contain DEFAULT_COMPANY_ADDITION
        defaultOwnerShouldNotBeFound("companyAddition.doesNotContain=" + DEFAULT_COMPANY_ADDITION);

        // Get all the ownerList where companyAddition does not contain UPDATED_COMPANY_ADDITION
        defaultOwnerShouldBeFound("companyAddition.doesNotContain=" + UPDATED_COMPANY_ADDITION);
    }

    @Test
    @Transactional
    void getAllOwnersByCompanyCountryIsEqualToSomething() throws Exception {
        // Initialize the database
        ownerRepository.saveAndFlush(owner);

        // Get all the ownerList where companyCountry equals to DEFAULT_COMPANY_COUNTRY
        defaultOwnerShouldBeFound("companyCountry.equals=" + DEFAULT_COMPANY_COUNTRY);

        // Get all the ownerList where companyCountry equals to UPDATED_COMPANY_COUNTRY
        defaultOwnerShouldNotBeFound("companyCountry.equals=" + UPDATED_COMPANY_COUNTRY);
    }

    @Test
    @Transactional
    void getAllOwnersByCompanyCountryIsNotEqualToSomething() throws Exception {
        // Initialize the database
        ownerRepository.saveAndFlush(owner);

        // Get all the ownerList where companyCountry not equals to DEFAULT_COMPANY_COUNTRY
        defaultOwnerShouldNotBeFound("companyCountry.notEquals=" + DEFAULT_COMPANY_COUNTRY);

        // Get all the ownerList where companyCountry not equals to UPDATED_COMPANY_COUNTRY
        defaultOwnerShouldBeFound("companyCountry.notEquals=" + UPDATED_COMPANY_COUNTRY);
    }

    @Test
    @Transactional
    void getAllOwnersByCompanyCountryIsInShouldWork() throws Exception {
        // Initialize the database
        ownerRepository.saveAndFlush(owner);

        // Get all the ownerList where companyCountry in DEFAULT_COMPANY_COUNTRY or UPDATED_COMPANY_COUNTRY
        defaultOwnerShouldBeFound("companyCountry.in=" + DEFAULT_COMPANY_COUNTRY + "," + UPDATED_COMPANY_COUNTRY);

        // Get all the ownerList where companyCountry equals to UPDATED_COMPANY_COUNTRY
        defaultOwnerShouldNotBeFound("companyCountry.in=" + UPDATED_COMPANY_COUNTRY);
    }

    @Test
    @Transactional
    void getAllOwnersByCompanyCountryIsNullOrNotNull() throws Exception {
        // Initialize the database
        ownerRepository.saveAndFlush(owner);

        // Get all the ownerList where companyCountry is not null
        defaultOwnerShouldBeFound("companyCountry.specified=true");

        // Get all the ownerList where companyCountry is null
        defaultOwnerShouldNotBeFound("companyCountry.specified=false");
    }

    @Test
    @Transactional
    void getAllOwnersByCompanyCountryContainsSomething() throws Exception {
        // Initialize the database
        ownerRepository.saveAndFlush(owner);

        // Get all the ownerList where companyCountry contains DEFAULT_COMPANY_COUNTRY
        defaultOwnerShouldBeFound("companyCountry.contains=" + DEFAULT_COMPANY_COUNTRY);

        // Get all the ownerList where companyCountry contains UPDATED_COMPANY_COUNTRY
        defaultOwnerShouldNotBeFound("companyCountry.contains=" + UPDATED_COMPANY_COUNTRY);
    }

    @Test
    @Transactional
    void getAllOwnersByCompanyCountryNotContainsSomething() throws Exception {
        // Initialize the database
        ownerRepository.saveAndFlush(owner);

        // Get all the ownerList where companyCountry does not contain DEFAULT_COMPANY_COUNTRY
        defaultOwnerShouldNotBeFound("companyCountry.doesNotContain=" + DEFAULT_COMPANY_COUNTRY);

        // Get all the ownerList where companyCountry does not contain UPDATED_COMPANY_COUNTRY
        defaultOwnerShouldBeFound("companyCountry.doesNotContain=" + UPDATED_COMPANY_COUNTRY);
    }

    @Test
    @Transactional
    void getAllOwnersByCompanyStreetIsEqualToSomething() throws Exception {
        // Initialize the database
        ownerRepository.saveAndFlush(owner);

        // Get all the ownerList where companyStreet equals to DEFAULT_COMPANY_STREET
        defaultOwnerShouldBeFound("companyStreet.equals=" + DEFAULT_COMPANY_STREET);

        // Get all the ownerList where companyStreet equals to UPDATED_COMPANY_STREET
        defaultOwnerShouldNotBeFound("companyStreet.equals=" + UPDATED_COMPANY_STREET);
    }

    @Test
    @Transactional
    void getAllOwnersByCompanyStreetIsNotEqualToSomething() throws Exception {
        // Initialize the database
        ownerRepository.saveAndFlush(owner);

        // Get all the ownerList where companyStreet not equals to DEFAULT_COMPANY_STREET
        defaultOwnerShouldNotBeFound("companyStreet.notEquals=" + DEFAULT_COMPANY_STREET);

        // Get all the ownerList where companyStreet not equals to UPDATED_COMPANY_STREET
        defaultOwnerShouldBeFound("companyStreet.notEquals=" + UPDATED_COMPANY_STREET);
    }

    @Test
    @Transactional
    void getAllOwnersByCompanyStreetIsInShouldWork() throws Exception {
        // Initialize the database
        ownerRepository.saveAndFlush(owner);

        // Get all the ownerList where companyStreet in DEFAULT_COMPANY_STREET or UPDATED_COMPANY_STREET
        defaultOwnerShouldBeFound("companyStreet.in=" + DEFAULT_COMPANY_STREET + "," + UPDATED_COMPANY_STREET);

        // Get all the ownerList where companyStreet equals to UPDATED_COMPANY_STREET
        defaultOwnerShouldNotBeFound("companyStreet.in=" + UPDATED_COMPANY_STREET);
    }

    @Test
    @Transactional
    void getAllOwnersByCompanyStreetIsNullOrNotNull() throws Exception {
        // Initialize the database
        ownerRepository.saveAndFlush(owner);

        // Get all the ownerList where companyStreet is not null
        defaultOwnerShouldBeFound("companyStreet.specified=true");

        // Get all the ownerList where companyStreet is null
        defaultOwnerShouldNotBeFound("companyStreet.specified=false");
    }

    @Test
    @Transactional
    void getAllOwnersByCompanyStreetContainsSomething() throws Exception {
        // Initialize the database
        ownerRepository.saveAndFlush(owner);

        // Get all the ownerList where companyStreet contains DEFAULT_COMPANY_STREET
        defaultOwnerShouldBeFound("companyStreet.contains=" + DEFAULT_COMPANY_STREET);

        // Get all the ownerList where companyStreet contains UPDATED_COMPANY_STREET
        defaultOwnerShouldNotBeFound("companyStreet.contains=" + UPDATED_COMPANY_STREET);
    }

    @Test
    @Transactional
    void getAllOwnersByCompanyStreetNotContainsSomething() throws Exception {
        // Initialize the database
        ownerRepository.saveAndFlush(owner);

        // Get all the ownerList where companyStreet does not contain DEFAULT_COMPANY_STREET
        defaultOwnerShouldNotBeFound("companyStreet.doesNotContain=" + DEFAULT_COMPANY_STREET);

        // Get all the ownerList where companyStreet does not contain UPDATED_COMPANY_STREET
        defaultOwnerShouldBeFound("companyStreet.doesNotContain=" + UPDATED_COMPANY_STREET);
    }

    @Test
    @Transactional
    void getAllOwnersByCompanyStreetNoIsEqualToSomething() throws Exception {
        // Initialize the database
        ownerRepository.saveAndFlush(owner);

        // Get all the ownerList where companyStreetNo equals to DEFAULT_COMPANY_STREET_NO
        defaultOwnerShouldBeFound("companyStreetNo.equals=" + DEFAULT_COMPANY_STREET_NO);

        // Get all the ownerList where companyStreetNo equals to UPDATED_COMPANY_STREET_NO
        defaultOwnerShouldNotBeFound("companyStreetNo.equals=" + UPDATED_COMPANY_STREET_NO);
    }

    @Test
    @Transactional
    void getAllOwnersByCompanyStreetNoIsNotEqualToSomething() throws Exception {
        // Initialize the database
        ownerRepository.saveAndFlush(owner);

        // Get all the ownerList where companyStreetNo not equals to DEFAULT_COMPANY_STREET_NO
        defaultOwnerShouldNotBeFound("companyStreetNo.notEquals=" + DEFAULT_COMPANY_STREET_NO);

        // Get all the ownerList where companyStreetNo not equals to UPDATED_COMPANY_STREET_NO
        defaultOwnerShouldBeFound("companyStreetNo.notEquals=" + UPDATED_COMPANY_STREET_NO);
    }

    @Test
    @Transactional
    void getAllOwnersByCompanyStreetNoIsInShouldWork() throws Exception {
        // Initialize the database
        ownerRepository.saveAndFlush(owner);

        // Get all the ownerList where companyStreetNo in DEFAULT_COMPANY_STREET_NO or UPDATED_COMPANY_STREET_NO
        defaultOwnerShouldBeFound("companyStreetNo.in=" + DEFAULT_COMPANY_STREET_NO + "," + UPDATED_COMPANY_STREET_NO);

        // Get all the ownerList where companyStreetNo equals to UPDATED_COMPANY_STREET_NO
        defaultOwnerShouldNotBeFound("companyStreetNo.in=" + UPDATED_COMPANY_STREET_NO);
    }

    @Test
    @Transactional
    void getAllOwnersByCompanyStreetNoIsNullOrNotNull() throws Exception {
        // Initialize the database
        ownerRepository.saveAndFlush(owner);

        // Get all the ownerList where companyStreetNo is not null
        defaultOwnerShouldBeFound("companyStreetNo.specified=true");

        // Get all the ownerList where companyStreetNo is null
        defaultOwnerShouldNotBeFound("companyStreetNo.specified=false");
    }

    @Test
    @Transactional
    void getAllOwnersByCompanyStreetNoContainsSomething() throws Exception {
        // Initialize the database
        ownerRepository.saveAndFlush(owner);

        // Get all the ownerList where companyStreetNo contains DEFAULT_COMPANY_STREET_NO
        defaultOwnerShouldBeFound("companyStreetNo.contains=" + DEFAULT_COMPANY_STREET_NO);

        // Get all the ownerList where companyStreetNo contains UPDATED_COMPANY_STREET_NO
        defaultOwnerShouldNotBeFound("companyStreetNo.contains=" + UPDATED_COMPANY_STREET_NO);
    }

    @Test
    @Transactional
    void getAllOwnersByCompanyStreetNoNotContainsSomething() throws Exception {
        // Initialize the database
        ownerRepository.saveAndFlush(owner);

        // Get all the ownerList where companyStreetNo does not contain DEFAULT_COMPANY_STREET_NO
        defaultOwnerShouldNotBeFound("companyStreetNo.doesNotContain=" + DEFAULT_COMPANY_STREET_NO);

        // Get all the ownerList where companyStreetNo does not contain UPDATED_COMPANY_STREET_NO
        defaultOwnerShouldBeFound("companyStreetNo.doesNotContain=" + UPDATED_COMPANY_STREET_NO);
    }

    @Test
    @Transactional
    void getAllOwnersByCompanyStreet2IsEqualToSomething() throws Exception {
        // Initialize the database
        ownerRepository.saveAndFlush(owner);

        // Get all the ownerList where companyStreet2 equals to DEFAULT_COMPANY_STREET_2
        defaultOwnerShouldBeFound("companyStreet2.equals=" + DEFAULT_COMPANY_STREET_2);

        // Get all the ownerList where companyStreet2 equals to UPDATED_COMPANY_STREET_2
        defaultOwnerShouldNotBeFound("companyStreet2.equals=" + UPDATED_COMPANY_STREET_2);
    }

    @Test
    @Transactional
    void getAllOwnersByCompanyStreet2IsNotEqualToSomething() throws Exception {
        // Initialize the database
        ownerRepository.saveAndFlush(owner);

        // Get all the ownerList where companyStreet2 not equals to DEFAULT_COMPANY_STREET_2
        defaultOwnerShouldNotBeFound("companyStreet2.notEquals=" + DEFAULT_COMPANY_STREET_2);

        // Get all the ownerList where companyStreet2 not equals to UPDATED_COMPANY_STREET_2
        defaultOwnerShouldBeFound("companyStreet2.notEquals=" + UPDATED_COMPANY_STREET_2);
    }

    @Test
    @Transactional
    void getAllOwnersByCompanyStreet2IsInShouldWork() throws Exception {
        // Initialize the database
        ownerRepository.saveAndFlush(owner);

        // Get all the ownerList where companyStreet2 in DEFAULT_COMPANY_STREET_2 or UPDATED_COMPANY_STREET_2
        defaultOwnerShouldBeFound("companyStreet2.in=" + DEFAULT_COMPANY_STREET_2 + "," + UPDATED_COMPANY_STREET_2);

        // Get all the ownerList where companyStreet2 equals to UPDATED_COMPANY_STREET_2
        defaultOwnerShouldNotBeFound("companyStreet2.in=" + UPDATED_COMPANY_STREET_2);
    }

    @Test
    @Transactional
    void getAllOwnersByCompanyStreet2IsNullOrNotNull() throws Exception {
        // Initialize the database
        ownerRepository.saveAndFlush(owner);

        // Get all the ownerList where companyStreet2 is not null
        defaultOwnerShouldBeFound("companyStreet2.specified=true");

        // Get all the ownerList where companyStreet2 is null
        defaultOwnerShouldNotBeFound("companyStreet2.specified=false");
    }

    @Test
    @Transactional
    void getAllOwnersByCompanyStreet2ContainsSomething() throws Exception {
        // Initialize the database
        ownerRepository.saveAndFlush(owner);

        // Get all the ownerList where companyStreet2 contains DEFAULT_COMPANY_STREET_2
        defaultOwnerShouldBeFound("companyStreet2.contains=" + DEFAULT_COMPANY_STREET_2);

        // Get all the ownerList where companyStreet2 contains UPDATED_COMPANY_STREET_2
        defaultOwnerShouldNotBeFound("companyStreet2.contains=" + UPDATED_COMPANY_STREET_2);
    }

    @Test
    @Transactional
    void getAllOwnersByCompanyStreet2NotContainsSomething() throws Exception {
        // Initialize the database
        ownerRepository.saveAndFlush(owner);

        // Get all the ownerList where companyStreet2 does not contain DEFAULT_COMPANY_STREET_2
        defaultOwnerShouldNotBeFound("companyStreet2.doesNotContain=" + DEFAULT_COMPANY_STREET_2);

        // Get all the ownerList where companyStreet2 does not contain UPDATED_COMPANY_STREET_2
        defaultOwnerShouldBeFound("companyStreet2.doesNotContain=" + UPDATED_COMPANY_STREET_2);
    }

    @Test
    @Transactional
    void getAllOwnersByCompanyPostcodeIsEqualToSomething() throws Exception {
        // Initialize the database
        ownerRepository.saveAndFlush(owner);

        // Get all the ownerList where companyPostcode equals to DEFAULT_COMPANY_POSTCODE
        defaultOwnerShouldBeFound("companyPostcode.equals=" + DEFAULT_COMPANY_POSTCODE);

        // Get all the ownerList where companyPostcode equals to UPDATED_COMPANY_POSTCODE
        defaultOwnerShouldNotBeFound("companyPostcode.equals=" + UPDATED_COMPANY_POSTCODE);
    }

    @Test
    @Transactional
    void getAllOwnersByCompanyPostcodeIsNotEqualToSomething() throws Exception {
        // Initialize the database
        ownerRepository.saveAndFlush(owner);

        // Get all the ownerList where companyPostcode not equals to DEFAULT_COMPANY_POSTCODE
        defaultOwnerShouldNotBeFound("companyPostcode.notEquals=" + DEFAULT_COMPANY_POSTCODE);

        // Get all the ownerList where companyPostcode not equals to UPDATED_COMPANY_POSTCODE
        defaultOwnerShouldBeFound("companyPostcode.notEquals=" + UPDATED_COMPANY_POSTCODE);
    }

    @Test
    @Transactional
    void getAllOwnersByCompanyPostcodeIsInShouldWork() throws Exception {
        // Initialize the database
        ownerRepository.saveAndFlush(owner);

        // Get all the ownerList where companyPostcode in DEFAULT_COMPANY_POSTCODE or UPDATED_COMPANY_POSTCODE
        defaultOwnerShouldBeFound("companyPostcode.in=" + DEFAULT_COMPANY_POSTCODE + "," + UPDATED_COMPANY_POSTCODE);

        // Get all the ownerList where companyPostcode equals to UPDATED_COMPANY_POSTCODE
        defaultOwnerShouldNotBeFound("companyPostcode.in=" + UPDATED_COMPANY_POSTCODE);
    }

    @Test
    @Transactional
    void getAllOwnersByCompanyPostcodeIsNullOrNotNull() throws Exception {
        // Initialize the database
        ownerRepository.saveAndFlush(owner);

        // Get all the ownerList where companyPostcode is not null
        defaultOwnerShouldBeFound("companyPostcode.specified=true");

        // Get all the ownerList where companyPostcode is null
        defaultOwnerShouldNotBeFound("companyPostcode.specified=false");
    }

    @Test
    @Transactional
    void getAllOwnersByCompanyPostcodeContainsSomething() throws Exception {
        // Initialize the database
        ownerRepository.saveAndFlush(owner);

        // Get all the ownerList where companyPostcode contains DEFAULT_COMPANY_POSTCODE
        defaultOwnerShouldBeFound("companyPostcode.contains=" + DEFAULT_COMPANY_POSTCODE);

        // Get all the ownerList where companyPostcode contains UPDATED_COMPANY_POSTCODE
        defaultOwnerShouldNotBeFound("companyPostcode.contains=" + UPDATED_COMPANY_POSTCODE);
    }

    @Test
    @Transactional
    void getAllOwnersByCompanyPostcodeNotContainsSomething() throws Exception {
        // Initialize the database
        ownerRepository.saveAndFlush(owner);

        // Get all the ownerList where companyPostcode does not contain DEFAULT_COMPANY_POSTCODE
        defaultOwnerShouldNotBeFound("companyPostcode.doesNotContain=" + DEFAULT_COMPANY_POSTCODE);

        // Get all the ownerList where companyPostcode does not contain UPDATED_COMPANY_POSTCODE
        defaultOwnerShouldBeFound("companyPostcode.doesNotContain=" + UPDATED_COMPANY_POSTCODE);
    }

    @Test
    @Transactional
    void getAllOwnersByCompanyCityIsEqualToSomething() throws Exception {
        // Initialize the database
        ownerRepository.saveAndFlush(owner);

        // Get all the ownerList where companyCity equals to DEFAULT_COMPANY_CITY
        defaultOwnerShouldBeFound("companyCity.equals=" + DEFAULT_COMPANY_CITY);

        // Get all the ownerList where companyCity equals to UPDATED_COMPANY_CITY
        defaultOwnerShouldNotBeFound("companyCity.equals=" + UPDATED_COMPANY_CITY);
    }

    @Test
    @Transactional
    void getAllOwnersByCompanyCityIsNotEqualToSomething() throws Exception {
        // Initialize the database
        ownerRepository.saveAndFlush(owner);

        // Get all the ownerList where companyCity not equals to DEFAULT_COMPANY_CITY
        defaultOwnerShouldNotBeFound("companyCity.notEquals=" + DEFAULT_COMPANY_CITY);

        // Get all the ownerList where companyCity not equals to UPDATED_COMPANY_CITY
        defaultOwnerShouldBeFound("companyCity.notEquals=" + UPDATED_COMPANY_CITY);
    }

    @Test
    @Transactional
    void getAllOwnersByCompanyCityIsInShouldWork() throws Exception {
        // Initialize the database
        ownerRepository.saveAndFlush(owner);

        // Get all the ownerList where companyCity in DEFAULT_COMPANY_CITY or UPDATED_COMPANY_CITY
        defaultOwnerShouldBeFound("companyCity.in=" + DEFAULT_COMPANY_CITY + "," + UPDATED_COMPANY_CITY);

        // Get all the ownerList where companyCity equals to UPDATED_COMPANY_CITY
        defaultOwnerShouldNotBeFound("companyCity.in=" + UPDATED_COMPANY_CITY);
    }

    @Test
    @Transactional
    void getAllOwnersByCompanyCityIsNullOrNotNull() throws Exception {
        // Initialize the database
        ownerRepository.saveAndFlush(owner);

        // Get all the ownerList where companyCity is not null
        defaultOwnerShouldBeFound("companyCity.specified=true");

        // Get all the ownerList where companyCity is null
        defaultOwnerShouldNotBeFound("companyCity.specified=false");
    }

    @Test
    @Transactional
    void getAllOwnersByCompanyCityContainsSomething() throws Exception {
        // Initialize the database
        ownerRepository.saveAndFlush(owner);

        // Get all the ownerList where companyCity contains DEFAULT_COMPANY_CITY
        defaultOwnerShouldBeFound("companyCity.contains=" + DEFAULT_COMPANY_CITY);

        // Get all the ownerList where companyCity contains UPDATED_COMPANY_CITY
        defaultOwnerShouldNotBeFound("companyCity.contains=" + UPDATED_COMPANY_CITY);
    }

    @Test
    @Transactional
    void getAllOwnersByCompanyCityNotContainsSomething() throws Exception {
        // Initialize the database
        ownerRepository.saveAndFlush(owner);

        // Get all the ownerList where companyCity does not contain DEFAULT_COMPANY_CITY
        defaultOwnerShouldNotBeFound("companyCity.doesNotContain=" + DEFAULT_COMPANY_CITY);

        // Get all the ownerList where companyCity does not contain UPDATED_COMPANY_CITY
        defaultOwnerShouldBeFound("companyCity.doesNotContain=" + UPDATED_COMPANY_CITY);
    }

    @Test
    @Transactional
    void getAllOwnersByCompanyPhoneIsEqualToSomething() throws Exception {
        // Initialize the database
        ownerRepository.saveAndFlush(owner);

        // Get all the ownerList where companyPhone equals to DEFAULT_COMPANY_PHONE
        defaultOwnerShouldBeFound("companyPhone.equals=" + DEFAULT_COMPANY_PHONE);

        // Get all the ownerList where companyPhone equals to UPDATED_COMPANY_PHONE
        defaultOwnerShouldNotBeFound("companyPhone.equals=" + UPDATED_COMPANY_PHONE);
    }

    @Test
    @Transactional
    void getAllOwnersByCompanyPhoneIsNotEqualToSomething() throws Exception {
        // Initialize the database
        ownerRepository.saveAndFlush(owner);

        // Get all the ownerList where companyPhone not equals to DEFAULT_COMPANY_PHONE
        defaultOwnerShouldNotBeFound("companyPhone.notEquals=" + DEFAULT_COMPANY_PHONE);

        // Get all the ownerList where companyPhone not equals to UPDATED_COMPANY_PHONE
        defaultOwnerShouldBeFound("companyPhone.notEquals=" + UPDATED_COMPANY_PHONE);
    }

    @Test
    @Transactional
    void getAllOwnersByCompanyPhoneIsInShouldWork() throws Exception {
        // Initialize the database
        ownerRepository.saveAndFlush(owner);

        // Get all the ownerList where companyPhone in DEFAULT_COMPANY_PHONE or UPDATED_COMPANY_PHONE
        defaultOwnerShouldBeFound("companyPhone.in=" + DEFAULT_COMPANY_PHONE + "," + UPDATED_COMPANY_PHONE);

        // Get all the ownerList where companyPhone equals to UPDATED_COMPANY_PHONE
        defaultOwnerShouldNotBeFound("companyPhone.in=" + UPDATED_COMPANY_PHONE);
    }

    @Test
    @Transactional
    void getAllOwnersByCompanyPhoneIsNullOrNotNull() throws Exception {
        // Initialize the database
        ownerRepository.saveAndFlush(owner);

        // Get all the ownerList where companyPhone is not null
        defaultOwnerShouldBeFound("companyPhone.specified=true");

        // Get all the ownerList where companyPhone is null
        defaultOwnerShouldNotBeFound("companyPhone.specified=false");
    }

    @Test
    @Transactional
    void getAllOwnersByCompanyPhoneContainsSomething() throws Exception {
        // Initialize the database
        ownerRepository.saveAndFlush(owner);

        // Get all the ownerList where companyPhone contains DEFAULT_COMPANY_PHONE
        defaultOwnerShouldBeFound("companyPhone.contains=" + DEFAULT_COMPANY_PHONE);

        // Get all the ownerList where companyPhone contains UPDATED_COMPANY_PHONE
        defaultOwnerShouldNotBeFound("companyPhone.contains=" + UPDATED_COMPANY_PHONE);
    }

    @Test
    @Transactional
    void getAllOwnersByCompanyPhoneNotContainsSomething() throws Exception {
        // Initialize the database
        ownerRepository.saveAndFlush(owner);

        // Get all the ownerList where companyPhone does not contain DEFAULT_COMPANY_PHONE
        defaultOwnerShouldNotBeFound("companyPhone.doesNotContain=" + DEFAULT_COMPANY_PHONE);

        // Get all the ownerList where companyPhone does not contain UPDATED_COMPANY_PHONE
        defaultOwnerShouldBeFound("companyPhone.doesNotContain=" + UPDATED_COMPANY_PHONE);
    }

    @Test
    @Transactional
    void getAllOwnersByCompanyFaxIsEqualToSomething() throws Exception {
        // Initialize the database
        ownerRepository.saveAndFlush(owner);

        // Get all the ownerList where companyFax equals to DEFAULT_COMPANY_FAX
        defaultOwnerShouldBeFound("companyFax.equals=" + DEFAULT_COMPANY_FAX);

        // Get all the ownerList where companyFax equals to UPDATED_COMPANY_FAX
        defaultOwnerShouldNotBeFound("companyFax.equals=" + UPDATED_COMPANY_FAX);
    }

    @Test
    @Transactional
    void getAllOwnersByCompanyFaxIsNotEqualToSomething() throws Exception {
        // Initialize the database
        ownerRepository.saveAndFlush(owner);

        // Get all the ownerList where companyFax not equals to DEFAULT_COMPANY_FAX
        defaultOwnerShouldNotBeFound("companyFax.notEquals=" + DEFAULT_COMPANY_FAX);

        // Get all the ownerList where companyFax not equals to UPDATED_COMPANY_FAX
        defaultOwnerShouldBeFound("companyFax.notEquals=" + UPDATED_COMPANY_FAX);
    }

    @Test
    @Transactional
    void getAllOwnersByCompanyFaxIsInShouldWork() throws Exception {
        // Initialize the database
        ownerRepository.saveAndFlush(owner);

        // Get all the ownerList where companyFax in DEFAULT_COMPANY_FAX or UPDATED_COMPANY_FAX
        defaultOwnerShouldBeFound("companyFax.in=" + DEFAULT_COMPANY_FAX + "," + UPDATED_COMPANY_FAX);

        // Get all the ownerList where companyFax equals to UPDATED_COMPANY_FAX
        defaultOwnerShouldNotBeFound("companyFax.in=" + UPDATED_COMPANY_FAX);
    }

    @Test
    @Transactional
    void getAllOwnersByCompanyFaxIsNullOrNotNull() throws Exception {
        // Initialize the database
        ownerRepository.saveAndFlush(owner);

        // Get all the ownerList where companyFax is not null
        defaultOwnerShouldBeFound("companyFax.specified=true");

        // Get all the ownerList where companyFax is null
        defaultOwnerShouldNotBeFound("companyFax.specified=false");
    }

    @Test
    @Transactional
    void getAllOwnersByCompanyFaxContainsSomething() throws Exception {
        // Initialize the database
        ownerRepository.saveAndFlush(owner);

        // Get all the ownerList where companyFax contains DEFAULT_COMPANY_FAX
        defaultOwnerShouldBeFound("companyFax.contains=" + DEFAULT_COMPANY_FAX);

        // Get all the ownerList where companyFax contains UPDATED_COMPANY_FAX
        defaultOwnerShouldNotBeFound("companyFax.contains=" + UPDATED_COMPANY_FAX);
    }

    @Test
    @Transactional
    void getAllOwnersByCompanyFaxNotContainsSomething() throws Exception {
        // Initialize the database
        ownerRepository.saveAndFlush(owner);

        // Get all the ownerList where companyFax does not contain DEFAULT_COMPANY_FAX
        defaultOwnerShouldNotBeFound("companyFax.doesNotContain=" + DEFAULT_COMPANY_FAX);

        // Get all the ownerList where companyFax does not contain UPDATED_COMPANY_FAX
        defaultOwnerShouldBeFound("companyFax.doesNotContain=" + UPDATED_COMPANY_FAX);
    }

    @Test
    @Transactional
    void getAllOwnersByCompanyEmailIsEqualToSomething() throws Exception {
        // Initialize the database
        ownerRepository.saveAndFlush(owner);

        // Get all the ownerList where companyEmail equals to DEFAULT_COMPANY_EMAIL
        defaultOwnerShouldBeFound("companyEmail.equals=" + DEFAULT_COMPANY_EMAIL);

        // Get all the ownerList where companyEmail equals to UPDATED_COMPANY_EMAIL
        defaultOwnerShouldNotBeFound("companyEmail.equals=" + UPDATED_COMPANY_EMAIL);
    }

    @Test
    @Transactional
    void getAllOwnersByCompanyEmailIsNotEqualToSomething() throws Exception {
        // Initialize the database
        ownerRepository.saveAndFlush(owner);

        // Get all the ownerList where companyEmail not equals to DEFAULT_COMPANY_EMAIL
        defaultOwnerShouldNotBeFound("companyEmail.notEquals=" + DEFAULT_COMPANY_EMAIL);

        // Get all the ownerList where companyEmail not equals to UPDATED_COMPANY_EMAIL
        defaultOwnerShouldBeFound("companyEmail.notEquals=" + UPDATED_COMPANY_EMAIL);
    }

    @Test
    @Transactional
    void getAllOwnersByCompanyEmailIsInShouldWork() throws Exception {
        // Initialize the database
        ownerRepository.saveAndFlush(owner);

        // Get all the ownerList where companyEmail in DEFAULT_COMPANY_EMAIL or UPDATED_COMPANY_EMAIL
        defaultOwnerShouldBeFound("companyEmail.in=" + DEFAULT_COMPANY_EMAIL + "," + UPDATED_COMPANY_EMAIL);

        // Get all the ownerList where companyEmail equals to UPDATED_COMPANY_EMAIL
        defaultOwnerShouldNotBeFound("companyEmail.in=" + UPDATED_COMPANY_EMAIL);
    }

    @Test
    @Transactional
    void getAllOwnersByCompanyEmailIsNullOrNotNull() throws Exception {
        // Initialize the database
        ownerRepository.saveAndFlush(owner);

        // Get all the ownerList where companyEmail is not null
        defaultOwnerShouldBeFound("companyEmail.specified=true");

        // Get all the ownerList where companyEmail is null
        defaultOwnerShouldNotBeFound("companyEmail.specified=false");
    }

    @Test
    @Transactional
    void getAllOwnersByCompanyEmailContainsSomething() throws Exception {
        // Initialize the database
        ownerRepository.saveAndFlush(owner);

        // Get all the ownerList where companyEmail contains DEFAULT_COMPANY_EMAIL
        defaultOwnerShouldBeFound("companyEmail.contains=" + DEFAULT_COMPANY_EMAIL);

        // Get all the ownerList where companyEmail contains UPDATED_COMPANY_EMAIL
        defaultOwnerShouldNotBeFound("companyEmail.contains=" + UPDATED_COMPANY_EMAIL);
    }

    @Test
    @Transactional
    void getAllOwnersByCompanyEmailNotContainsSomething() throws Exception {
        // Initialize the database
        ownerRepository.saveAndFlush(owner);

        // Get all the ownerList where companyEmail does not contain DEFAULT_COMPANY_EMAIL
        defaultOwnerShouldNotBeFound("companyEmail.doesNotContain=" + DEFAULT_COMPANY_EMAIL);

        // Get all the ownerList where companyEmail does not contain UPDATED_COMPANY_EMAIL
        defaultOwnerShouldBeFound("companyEmail.doesNotContain=" + UPDATED_COMPANY_EMAIL);
    }

    @Test
    @Transactional
    void getAllOwnersByCompanyWebsiteIsEqualToSomething() throws Exception {
        // Initialize the database
        ownerRepository.saveAndFlush(owner);

        // Get all the ownerList where companyWebsite equals to DEFAULT_COMPANY_WEBSITE
        defaultOwnerShouldBeFound("companyWebsite.equals=" + DEFAULT_COMPANY_WEBSITE);

        // Get all the ownerList where companyWebsite equals to UPDATED_COMPANY_WEBSITE
        defaultOwnerShouldNotBeFound("companyWebsite.equals=" + UPDATED_COMPANY_WEBSITE);
    }

    @Test
    @Transactional
    void getAllOwnersByCompanyWebsiteIsNotEqualToSomething() throws Exception {
        // Initialize the database
        ownerRepository.saveAndFlush(owner);

        // Get all the ownerList where companyWebsite not equals to DEFAULT_COMPANY_WEBSITE
        defaultOwnerShouldNotBeFound("companyWebsite.notEquals=" + DEFAULT_COMPANY_WEBSITE);

        // Get all the ownerList where companyWebsite not equals to UPDATED_COMPANY_WEBSITE
        defaultOwnerShouldBeFound("companyWebsite.notEquals=" + UPDATED_COMPANY_WEBSITE);
    }

    @Test
    @Transactional
    void getAllOwnersByCompanyWebsiteIsInShouldWork() throws Exception {
        // Initialize the database
        ownerRepository.saveAndFlush(owner);

        // Get all the ownerList where companyWebsite in DEFAULT_COMPANY_WEBSITE or UPDATED_COMPANY_WEBSITE
        defaultOwnerShouldBeFound("companyWebsite.in=" + DEFAULT_COMPANY_WEBSITE + "," + UPDATED_COMPANY_WEBSITE);

        // Get all the ownerList where companyWebsite equals to UPDATED_COMPANY_WEBSITE
        defaultOwnerShouldNotBeFound("companyWebsite.in=" + UPDATED_COMPANY_WEBSITE);
    }

    @Test
    @Transactional
    void getAllOwnersByCompanyWebsiteIsNullOrNotNull() throws Exception {
        // Initialize the database
        ownerRepository.saveAndFlush(owner);

        // Get all the ownerList where companyWebsite is not null
        defaultOwnerShouldBeFound("companyWebsite.specified=true");

        // Get all the ownerList where companyWebsite is null
        defaultOwnerShouldNotBeFound("companyWebsite.specified=false");
    }

    @Test
    @Transactional
    void getAllOwnersByCompanyWebsiteContainsSomething() throws Exception {
        // Initialize the database
        ownerRepository.saveAndFlush(owner);

        // Get all the ownerList where companyWebsite contains DEFAULT_COMPANY_WEBSITE
        defaultOwnerShouldBeFound("companyWebsite.contains=" + DEFAULT_COMPANY_WEBSITE);

        // Get all the ownerList where companyWebsite contains UPDATED_COMPANY_WEBSITE
        defaultOwnerShouldNotBeFound("companyWebsite.contains=" + UPDATED_COMPANY_WEBSITE);
    }

    @Test
    @Transactional
    void getAllOwnersByCompanyWebsiteNotContainsSomething() throws Exception {
        // Initialize the database
        ownerRepository.saveAndFlush(owner);

        // Get all the ownerList where companyWebsite does not contain DEFAULT_COMPANY_WEBSITE
        defaultOwnerShouldNotBeFound("companyWebsite.doesNotContain=" + DEFAULT_COMPANY_WEBSITE);

        // Get all the ownerList where companyWebsite does not contain UPDATED_COMPANY_WEBSITE
        defaultOwnerShouldBeFound("companyWebsite.doesNotContain=" + UPDATED_COMPANY_WEBSITE);
    }

    @Test
    @Transactional
    void getAllOwnersByCompanyVatIdIsEqualToSomething() throws Exception {
        // Initialize the database
        ownerRepository.saveAndFlush(owner);

        // Get all the ownerList where companyVatId equals to DEFAULT_COMPANY_VAT_ID
        defaultOwnerShouldBeFound("companyVatId.equals=" + DEFAULT_COMPANY_VAT_ID);

        // Get all the ownerList where companyVatId equals to UPDATED_COMPANY_VAT_ID
        defaultOwnerShouldNotBeFound("companyVatId.equals=" + UPDATED_COMPANY_VAT_ID);
    }

    @Test
    @Transactional
    void getAllOwnersByCompanyVatIdIsNotEqualToSomething() throws Exception {
        // Initialize the database
        ownerRepository.saveAndFlush(owner);

        // Get all the ownerList where companyVatId not equals to DEFAULT_COMPANY_VAT_ID
        defaultOwnerShouldNotBeFound("companyVatId.notEquals=" + DEFAULT_COMPANY_VAT_ID);

        // Get all the ownerList where companyVatId not equals to UPDATED_COMPANY_VAT_ID
        defaultOwnerShouldBeFound("companyVatId.notEquals=" + UPDATED_COMPANY_VAT_ID);
    }

    @Test
    @Transactional
    void getAllOwnersByCompanyVatIdIsInShouldWork() throws Exception {
        // Initialize the database
        ownerRepository.saveAndFlush(owner);

        // Get all the ownerList where companyVatId in DEFAULT_COMPANY_VAT_ID or UPDATED_COMPANY_VAT_ID
        defaultOwnerShouldBeFound("companyVatId.in=" + DEFAULT_COMPANY_VAT_ID + "," + UPDATED_COMPANY_VAT_ID);

        // Get all the ownerList where companyVatId equals to UPDATED_COMPANY_VAT_ID
        defaultOwnerShouldNotBeFound("companyVatId.in=" + UPDATED_COMPANY_VAT_ID);
    }

    @Test
    @Transactional
    void getAllOwnersByCompanyVatIdIsNullOrNotNull() throws Exception {
        // Initialize the database
        ownerRepository.saveAndFlush(owner);

        // Get all the ownerList where companyVatId is not null
        defaultOwnerShouldBeFound("companyVatId.specified=true");

        // Get all the ownerList where companyVatId is null
        defaultOwnerShouldNotBeFound("companyVatId.specified=false");
    }

    @Test
    @Transactional
    void getAllOwnersByCompanyVatIdContainsSomething() throws Exception {
        // Initialize the database
        ownerRepository.saveAndFlush(owner);

        // Get all the ownerList where companyVatId contains DEFAULT_COMPANY_VAT_ID
        defaultOwnerShouldBeFound("companyVatId.contains=" + DEFAULT_COMPANY_VAT_ID);

        // Get all the ownerList where companyVatId contains UPDATED_COMPANY_VAT_ID
        defaultOwnerShouldNotBeFound("companyVatId.contains=" + UPDATED_COMPANY_VAT_ID);
    }

    @Test
    @Transactional
    void getAllOwnersByCompanyVatIdNotContainsSomething() throws Exception {
        // Initialize the database
        ownerRepository.saveAndFlush(owner);

        // Get all the ownerList where companyVatId does not contain DEFAULT_COMPANY_VAT_ID
        defaultOwnerShouldNotBeFound("companyVatId.doesNotContain=" + DEFAULT_COMPANY_VAT_ID);

        // Get all the ownerList where companyVatId does not contain UPDATED_COMPANY_VAT_ID
        defaultOwnerShouldBeFound("companyVatId.doesNotContain=" + UPDATED_COMPANY_VAT_ID);
    }

    @Test
    @Transactional
    void getAllOwnersByCompanyCurrencyIsEqualToSomething() throws Exception {
        // Initialize the database
        ownerRepository.saveAndFlush(owner);

        // Get all the ownerList where companyCurrency equals to DEFAULT_COMPANY_CURRENCY
        defaultOwnerShouldBeFound("companyCurrency.equals=" + DEFAULT_COMPANY_CURRENCY);

        // Get all the ownerList where companyCurrency equals to UPDATED_COMPANY_CURRENCY
        defaultOwnerShouldNotBeFound("companyCurrency.equals=" + UPDATED_COMPANY_CURRENCY);
    }

    @Test
    @Transactional
    void getAllOwnersByCompanyCurrencyIsNotEqualToSomething() throws Exception {
        // Initialize the database
        ownerRepository.saveAndFlush(owner);

        // Get all the ownerList where companyCurrency not equals to DEFAULT_COMPANY_CURRENCY
        defaultOwnerShouldNotBeFound("companyCurrency.notEquals=" + DEFAULT_COMPANY_CURRENCY);

        // Get all the ownerList where companyCurrency not equals to UPDATED_COMPANY_CURRENCY
        defaultOwnerShouldBeFound("companyCurrency.notEquals=" + UPDATED_COMPANY_CURRENCY);
    }

    @Test
    @Transactional
    void getAllOwnersByCompanyCurrencyIsInShouldWork() throws Exception {
        // Initialize the database
        ownerRepository.saveAndFlush(owner);

        // Get all the ownerList where companyCurrency in DEFAULT_COMPANY_CURRENCY or UPDATED_COMPANY_CURRENCY
        defaultOwnerShouldBeFound("companyCurrency.in=" + DEFAULT_COMPANY_CURRENCY + "," + UPDATED_COMPANY_CURRENCY);

        // Get all the ownerList where companyCurrency equals to UPDATED_COMPANY_CURRENCY
        defaultOwnerShouldNotBeFound("companyCurrency.in=" + UPDATED_COMPANY_CURRENCY);
    }

    @Test
    @Transactional
    void getAllOwnersByCompanyCurrencyIsNullOrNotNull() throws Exception {
        // Initialize the database
        ownerRepository.saveAndFlush(owner);

        // Get all the ownerList where companyCurrency is not null
        defaultOwnerShouldBeFound("companyCurrency.specified=true");

        // Get all the ownerList where companyCurrency is null
        defaultOwnerShouldNotBeFound("companyCurrency.specified=false");
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultOwnerShouldBeFound(String filter) throws Exception {
        restOwnerMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(owner.getId().intValue())))
            .andExpect(jsonPath("$.[*].remoteId").value(hasItem(DEFAULT_REMOTE_ID)))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)))
            .andExpect(jsonPath("$.[*].surname").value(hasItem(DEFAULT_SURNAME)))
            .andExpect(jsonPath("$.[*].email").value(hasItem(DEFAULT_EMAIL)))
            .andExpect(jsonPath("$.[*].language").value(hasItem(DEFAULT_LANGUAGE.toString())))
            .andExpect(jsonPath("$.[*].companyName").value(hasItem(DEFAULT_COMPANY_NAME)))
            .andExpect(jsonPath("$.[*].companyAddition").value(hasItem(DEFAULT_COMPANY_ADDITION)))
            .andExpect(jsonPath("$.[*].companyCountry").value(hasItem(DEFAULT_COMPANY_COUNTRY)))
            .andExpect(jsonPath("$.[*].companyStreet").value(hasItem(DEFAULT_COMPANY_STREET)))
            .andExpect(jsonPath("$.[*].companyStreetNo").value(hasItem(DEFAULT_COMPANY_STREET_NO)))
            .andExpect(jsonPath("$.[*].companyStreet2").value(hasItem(DEFAULT_COMPANY_STREET_2)))
            .andExpect(jsonPath("$.[*].companyPostcode").value(hasItem(DEFAULT_COMPANY_POSTCODE)))
            .andExpect(jsonPath("$.[*].companyCity").value(hasItem(DEFAULT_COMPANY_CITY)))
            .andExpect(jsonPath("$.[*].companyPhone").value(hasItem(DEFAULT_COMPANY_PHONE)))
            .andExpect(jsonPath("$.[*].companyFax").value(hasItem(DEFAULT_COMPANY_FAX)))
            .andExpect(jsonPath("$.[*].companyEmail").value(hasItem(DEFAULT_COMPANY_EMAIL)))
            .andExpect(jsonPath("$.[*].companyWebsite").value(hasItem(DEFAULT_COMPANY_WEBSITE)))
            .andExpect(jsonPath("$.[*].companyVatId").value(hasItem(DEFAULT_COMPANY_VAT_ID)))
            .andExpect(jsonPath("$.[*].companyCurrency").value(hasItem(DEFAULT_COMPANY_CURRENCY.toString())));

        // Check, that the count call also returns 1
        restOwnerMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultOwnerShouldNotBeFound(String filter) throws Exception {
        restOwnerMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restOwnerMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingOwner() throws Exception {
        // Get the owner
        restOwnerMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewOwner() throws Exception {
        // Initialize the database
        ownerRepository.saveAndFlush(owner);

        int databaseSizeBeforeUpdate = ownerRepository.findAll().size();

        // Update the owner
        Owner updatedOwner = ownerRepository.findById(owner.getId()).get();
        // Disconnect from session so that the updates on updatedOwner are not directly saved in db
        em.detach(updatedOwner);
        updatedOwner
            .remoteId(UPDATED_REMOTE_ID)
            .name(UPDATED_NAME)
            .surname(UPDATED_SURNAME)
            .email(UPDATED_EMAIL)
            .language(UPDATED_LANGUAGE)
            .companyName(UPDATED_COMPANY_NAME)
            .companyAddition(UPDATED_COMPANY_ADDITION)
            .companyCountry(UPDATED_COMPANY_COUNTRY)
            .companyStreet(UPDATED_COMPANY_STREET)
            .companyStreetNo(UPDATED_COMPANY_STREET_NO)
            .companyStreet2(UPDATED_COMPANY_STREET_2)
            .companyPostcode(UPDATED_COMPANY_POSTCODE)
            .companyCity(UPDATED_COMPANY_CITY)
            .companyPhone(UPDATED_COMPANY_PHONE)
            .companyFax(UPDATED_COMPANY_FAX)
            .companyEmail(UPDATED_COMPANY_EMAIL)
            .companyWebsite(UPDATED_COMPANY_WEBSITE)
            .companyVatId(UPDATED_COMPANY_VAT_ID)
            .companyCurrency(UPDATED_COMPANY_CURRENCY);
        OwnerDTO ownerDTO = ownerMapper.toDto(updatedOwner);

        restOwnerMockMvc
            .perform(
                put(ENTITY_API_URL_ID, ownerDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(ownerDTO))
            )
            .andExpect(status().isOk());

        // Validate the Owner in the database
        List<Owner> ownerList = ownerRepository.findAll();
        assertThat(ownerList).hasSize(databaseSizeBeforeUpdate);
        Owner testOwner = ownerList.get(ownerList.size() - 1);
        assertThat(testOwner.getRemoteId()).isEqualTo(UPDATED_REMOTE_ID);
        assertThat(testOwner.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testOwner.getSurname()).isEqualTo(UPDATED_SURNAME);
        assertThat(testOwner.getEmail()).isEqualTo(UPDATED_EMAIL);
        assertThat(testOwner.getLanguage()).isEqualTo(UPDATED_LANGUAGE);
        assertThat(testOwner.getCompanyName()).isEqualTo(UPDATED_COMPANY_NAME);
        assertThat(testOwner.getCompanyAddition()).isEqualTo(UPDATED_COMPANY_ADDITION);
        assertThat(testOwner.getCompanyCountry()).isEqualTo(UPDATED_COMPANY_COUNTRY);
        assertThat(testOwner.getCompanyStreet()).isEqualTo(UPDATED_COMPANY_STREET);
        assertThat(testOwner.getCompanyStreetNo()).isEqualTo(UPDATED_COMPANY_STREET_NO);
        assertThat(testOwner.getCompanyStreet2()).isEqualTo(UPDATED_COMPANY_STREET_2);
        assertThat(testOwner.getCompanyPostcode()).isEqualTo(UPDATED_COMPANY_POSTCODE);
        assertThat(testOwner.getCompanyCity()).isEqualTo(UPDATED_COMPANY_CITY);
        assertThat(testOwner.getCompanyPhone()).isEqualTo(UPDATED_COMPANY_PHONE);
        assertThat(testOwner.getCompanyFax()).isEqualTo(UPDATED_COMPANY_FAX);
        assertThat(testOwner.getCompanyEmail()).isEqualTo(UPDATED_COMPANY_EMAIL);
        assertThat(testOwner.getCompanyWebsite()).isEqualTo(UPDATED_COMPANY_WEBSITE);
        assertThat(testOwner.getCompanyVatId()).isEqualTo(UPDATED_COMPANY_VAT_ID);
        assertThat(testOwner.getCompanyCurrency()).isEqualTo(UPDATED_COMPANY_CURRENCY);

        // Validate the Owner in Elasticsearch
        verify(mockOwnerSearchRepository).save(testOwner);
    }

    @Test
    @Transactional
    void putNonExistingOwner() throws Exception {
        int databaseSizeBeforeUpdate = ownerRepository.findAll().size();
        owner.setId(count.incrementAndGet());

        // Create the Owner
        OwnerDTO ownerDTO = ownerMapper.toDto(owner);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restOwnerMockMvc
            .perform(
                put(ENTITY_API_URL_ID, ownerDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(ownerDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Owner in the database
        List<Owner> ownerList = ownerRepository.findAll();
        assertThat(ownerList).hasSize(databaseSizeBeforeUpdate);

        // Validate the Owner in Elasticsearch
        verify(mockOwnerSearchRepository, times(0)).save(owner);
    }

    @Test
    @Transactional
    void putWithIdMismatchOwner() throws Exception {
        int databaseSizeBeforeUpdate = ownerRepository.findAll().size();
        owner.setId(count.incrementAndGet());

        // Create the Owner
        OwnerDTO ownerDTO = ownerMapper.toDto(owner);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restOwnerMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(ownerDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Owner in the database
        List<Owner> ownerList = ownerRepository.findAll();
        assertThat(ownerList).hasSize(databaseSizeBeforeUpdate);

        // Validate the Owner in Elasticsearch
        verify(mockOwnerSearchRepository, times(0)).save(owner);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamOwner() throws Exception {
        int databaseSizeBeforeUpdate = ownerRepository.findAll().size();
        owner.setId(count.incrementAndGet());

        // Create the Owner
        OwnerDTO ownerDTO = ownerMapper.toDto(owner);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restOwnerMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(ownerDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Owner in the database
        List<Owner> ownerList = ownerRepository.findAll();
        assertThat(ownerList).hasSize(databaseSizeBeforeUpdate);

        // Validate the Owner in Elasticsearch
        verify(mockOwnerSearchRepository, times(0)).save(owner);
    }

    @Test
    @Transactional
    void partialUpdateOwnerWithPatch() throws Exception {
        // Initialize the database
        ownerRepository.saveAndFlush(owner);

        int databaseSizeBeforeUpdate = ownerRepository.findAll().size();

        // Update the owner using partial update
        Owner partialUpdatedOwner = new Owner();
        partialUpdatedOwner.setId(owner.getId());

        partialUpdatedOwner
            .name(UPDATED_NAME)
            .surname(UPDATED_SURNAME)
            .email(UPDATED_EMAIL)
            .language(UPDATED_LANGUAGE)
            .companyStreet(UPDATED_COMPANY_STREET)
            .companyFax(UPDATED_COMPANY_FAX)
            .companyWebsite(UPDATED_COMPANY_WEBSITE)
            .companyCurrency(UPDATED_COMPANY_CURRENCY);

        restOwnerMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedOwner.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedOwner))
            )
            .andExpect(status().isOk());

        // Validate the Owner in the database
        List<Owner> ownerList = ownerRepository.findAll();
        assertThat(ownerList).hasSize(databaseSizeBeforeUpdate);
        Owner testOwner = ownerList.get(ownerList.size() - 1);
        assertThat(testOwner.getRemoteId()).isEqualTo(DEFAULT_REMOTE_ID);
        assertThat(testOwner.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testOwner.getSurname()).isEqualTo(UPDATED_SURNAME);
        assertThat(testOwner.getEmail()).isEqualTo(UPDATED_EMAIL);
        assertThat(testOwner.getLanguage()).isEqualTo(UPDATED_LANGUAGE);
        assertThat(testOwner.getCompanyName()).isEqualTo(DEFAULT_COMPANY_NAME);
        assertThat(testOwner.getCompanyAddition()).isEqualTo(DEFAULT_COMPANY_ADDITION);
        assertThat(testOwner.getCompanyCountry()).isEqualTo(DEFAULT_COMPANY_COUNTRY);
        assertThat(testOwner.getCompanyStreet()).isEqualTo(UPDATED_COMPANY_STREET);
        assertThat(testOwner.getCompanyStreetNo()).isEqualTo(DEFAULT_COMPANY_STREET_NO);
        assertThat(testOwner.getCompanyStreet2()).isEqualTo(DEFAULT_COMPANY_STREET_2);
        assertThat(testOwner.getCompanyPostcode()).isEqualTo(DEFAULT_COMPANY_POSTCODE);
        assertThat(testOwner.getCompanyCity()).isEqualTo(DEFAULT_COMPANY_CITY);
        assertThat(testOwner.getCompanyPhone()).isEqualTo(DEFAULT_COMPANY_PHONE);
        assertThat(testOwner.getCompanyFax()).isEqualTo(UPDATED_COMPANY_FAX);
        assertThat(testOwner.getCompanyEmail()).isEqualTo(DEFAULT_COMPANY_EMAIL);
        assertThat(testOwner.getCompanyWebsite()).isEqualTo(UPDATED_COMPANY_WEBSITE);
        assertThat(testOwner.getCompanyVatId()).isEqualTo(DEFAULT_COMPANY_VAT_ID);
        assertThat(testOwner.getCompanyCurrency()).isEqualTo(UPDATED_COMPANY_CURRENCY);
    }

    @Test
    @Transactional
    void fullUpdateOwnerWithPatch() throws Exception {
        // Initialize the database
        ownerRepository.saveAndFlush(owner);

        int databaseSizeBeforeUpdate = ownerRepository.findAll().size();

        // Update the owner using partial update
        Owner partialUpdatedOwner = new Owner();
        partialUpdatedOwner.setId(owner.getId());

        partialUpdatedOwner
            .remoteId(UPDATED_REMOTE_ID)
            .name(UPDATED_NAME)
            .surname(UPDATED_SURNAME)
            .email(UPDATED_EMAIL)
            .language(UPDATED_LANGUAGE)
            .companyName(UPDATED_COMPANY_NAME)
            .companyAddition(UPDATED_COMPANY_ADDITION)
            .companyCountry(UPDATED_COMPANY_COUNTRY)
            .companyStreet(UPDATED_COMPANY_STREET)
            .companyStreetNo(UPDATED_COMPANY_STREET_NO)
            .companyStreet2(UPDATED_COMPANY_STREET_2)
            .companyPostcode(UPDATED_COMPANY_POSTCODE)
            .companyCity(UPDATED_COMPANY_CITY)
            .companyPhone(UPDATED_COMPANY_PHONE)
            .companyFax(UPDATED_COMPANY_FAX)
            .companyEmail(UPDATED_COMPANY_EMAIL)
            .companyWebsite(UPDATED_COMPANY_WEBSITE)
            .companyVatId(UPDATED_COMPANY_VAT_ID)
            .companyCurrency(UPDATED_COMPANY_CURRENCY);

        restOwnerMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedOwner.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedOwner))
            )
            .andExpect(status().isOk());

        // Validate the Owner in the database
        List<Owner> ownerList = ownerRepository.findAll();
        assertThat(ownerList).hasSize(databaseSizeBeforeUpdate);
        Owner testOwner = ownerList.get(ownerList.size() - 1);
        assertThat(testOwner.getRemoteId()).isEqualTo(UPDATED_REMOTE_ID);
        assertThat(testOwner.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testOwner.getSurname()).isEqualTo(UPDATED_SURNAME);
        assertThat(testOwner.getEmail()).isEqualTo(UPDATED_EMAIL);
        assertThat(testOwner.getLanguage()).isEqualTo(UPDATED_LANGUAGE);
        assertThat(testOwner.getCompanyName()).isEqualTo(UPDATED_COMPANY_NAME);
        assertThat(testOwner.getCompanyAddition()).isEqualTo(UPDATED_COMPANY_ADDITION);
        assertThat(testOwner.getCompanyCountry()).isEqualTo(UPDATED_COMPANY_COUNTRY);
        assertThat(testOwner.getCompanyStreet()).isEqualTo(UPDATED_COMPANY_STREET);
        assertThat(testOwner.getCompanyStreetNo()).isEqualTo(UPDATED_COMPANY_STREET_NO);
        assertThat(testOwner.getCompanyStreet2()).isEqualTo(UPDATED_COMPANY_STREET_2);
        assertThat(testOwner.getCompanyPostcode()).isEqualTo(UPDATED_COMPANY_POSTCODE);
        assertThat(testOwner.getCompanyCity()).isEqualTo(UPDATED_COMPANY_CITY);
        assertThat(testOwner.getCompanyPhone()).isEqualTo(UPDATED_COMPANY_PHONE);
        assertThat(testOwner.getCompanyFax()).isEqualTo(UPDATED_COMPANY_FAX);
        assertThat(testOwner.getCompanyEmail()).isEqualTo(UPDATED_COMPANY_EMAIL);
        assertThat(testOwner.getCompanyWebsite()).isEqualTo(UPDATED_COMPANY_WEBSITE);
        assertThat(testOwner.getCompanyVatId()).isEqualTo(UPDATED_COMPANY_VAT_ID);
        assertThat(testOwner.getCompanyCurrency()).isEqualTo(UPDATED_COMPANY_CURRENCY);
    }

    @Test
    @Transactional
    void patchNonExistingOwner() throws Exception {
        int databaseSizeBeforeUpdate = ownerRepository.findAll().size();
        owner.setId(count.incrementAndGet());

        // Create the Owner
        OwnerDTO ownerDTO = ownerMapper.toDto(owner);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restOwnerMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, ownerDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(ownerDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Owner in the database
        List<Owner> ownerList = ownerRepository.findAll();
        assertThat(ownerList).hasSize(databaseSizeBeforeUpdate);

        // Validate the Owner in Elasticsearch
        verify(mockOwnerSearchRepository, times(0)).save(owner);
    }

    @Test
    @Transactional
    void patchWithIdMismatchOwner() throws Exception {
        int databaseSizeBeforeUpdate = ownerRepository.findAll().size();
        owner.setId(count.incrementAndGet());

        // Create the Owner
        OwnerDTO ownerDTO = ownerMapper.toDto(owner);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restOwnerMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(ownerDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Owner in the database
        List<Owner> ownerList = ownerRepository.findAll();
        assertThat(ownerList).hasSize(databaseSizeBeforeUpdate);

        // Validate the Owner in Elasticsearch
        verify(mockOwnerSearchRepository, times(0)).save(owner);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamOwner() throws Exception {
        int databaseSizeBeforeUpdate = ownerRepository.findAll().size();
        owner.setId(count.incrementAndGet());

        // Create the Owner
        OwnerDTO ownerDTO = ownerMapper.toDto(owner);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restOwnerMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(ownerDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Owner in the database
        List<Owner> ownerList = ownerRepository.findAll();
        assertThat(ownerList).hasSize(databaseSizeBeforeUpdate);

        // Validate the Owner in Elasticsearch
        verify(mockOwnerSearchRepository, times(0)).save(owner);
    }

    @Test
    @Transactional
    void deleteOwner() throws Exception {
        // Initialize the database
        ownerRepository.saveAndFlush(owner);

        int databaseSizeBeforeDelete = ownerRepository.findAll().size();

        // Delete the owner
        restOwnerMockMvc
            .perform(delete(ENTITY_API_URL_ID, owner.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Owner> ownerList = ownerRepository.findAll();
        assertThat(ownerList).hasSize(databaseSizeBeforeDelete - 1);

        // Validate the Owner in Elasticsearch
        verify(mockOwnerSearchRepository, times(1)).deleteById(owner.getId());
    }

    @Test
    @Transactional
    void searchOwner() throws Exception {
        // Configure the mock search repository
        // Initialize the database
        ownerRepository.saveAndFlush(owner);
        when(mockOwnerSearchRepository.search(queryStringQuery("id:" + owner.getId()))).thenReturn(Collections.singletonList(owner));

        // Search the owner
        restOwnerMockMvc
            .perform(get(ENTITY_SEARCH_API_URL + "?query=id:" + owner.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(owner.getId().intValue())))
            .andExpect(jsonPath("$.[*].remoteId").value(hasItem(DEFAULT_REMOTE_ID)))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)))
            .andExpect(jsonPath("$.[*].surname").value(hasItem(DEFAULT_SURNAME)))
            .andExpect(jsonPath("$.[*].email").value(hasItem(DEFAULT_EMAIL)))
            .andExpect(jsonPath("$.[*].language").value(hasItem(DEFAULT_LANGUAGE.toString())))
            .andExpect(jsonPath("$.[*].companyName").value(hasItem(DEFAULT_COMPANY_NAME)))
            .andExpect(jsonPath("$.[*].companyAddition").value(hasItem(DEFAULT_COMPANY_ADDITION)))
            .andExpect(jsonPath("$.[*].companyCountry").value(hasItem(DEFAULT_COMPANY_COUNTRY)))
            .andExpect(jsonPath("$.[*].companyStreet").value(hasItem(DEFAULT_COMPANY_STREET)))
            .andExpect(jsonPath("$.[*].companyStreetNo").value(hasItem(DEFAULT_COMPANY_STREET_NO)))
            .andExpect(jsonPath("$.[*].companyStreet2").value(hasItem(DEFAULT_COMPANY_STREET_2)))
            .andExpect(jsonPath("$.[*].companyPostcode").value(hasItem(DEFAULT_COMPANY_POSTCODE)))
            .andExpect(jsonPath("$.[*].companyCity").value(hasItem(DEFAULT_COMPANY_CITY)))
            .andExpect(jsonPath("$.[*].companyPhone").value(hasItem(DEFAULT_COMPANY_PHONE)))
            .andExpect(jsonPath("$.[*].companyFax").value(hasItem(DEFAULT_COMPANY_FAX)))
            .andExpect(jsonPath("$.[*].companyEmail").value(hasItem(DEFAULT_COMPANY_EMAIL)))
            .andExpect(jsonPath("$.[*].companyWebsite").value(hasItem(DEFAULT_COMPANY_WEBSITE)))
            .andExpect(jsonPath("$.[*].companyVatId").value(hasItem(DEFAULT_COMPANY_VAT_ID)))
            .andExpect(jsonPath("$.[*].companyCurrency").value(hasItem(DEFAULT_COMPANY_CURRENCY.toString())));
    }
}
