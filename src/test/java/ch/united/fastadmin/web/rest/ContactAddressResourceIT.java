package ch.united.fastadmin.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.elasticsearch.index.query.QueryBuilders.queryStringQuery;
import static org.hamcrest.Matchers.hasItem;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import ch.united.fastadmin.IntegrationTest;
import ch.united.fastadmin.domain.ContactAddress;
import ch.united.fastadmin.repository.ContactAddressRepository;
import ch.united.fastadmin.repository.search.ContactAddressSearchRepository;
import ch.united.fastadmin.service.criteria.ContactAddressCriteria;
import ch.united.fastadmin.service.dto.ContactAddressDTO;
import ch.united.fastadmin.service.mapper.ContactAddressMapper;
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
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

/**
 * Integration tests for the {@link ContactAddressResource} REST controller.
 */
@IntegrationTest
@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc
@WithMockUser
class ContactAddressResourceIT {

    private static final Integer DEFAULT_REMOTE_ID = 1;
    private static final Integer UPDATED_REMOTE_ID = 2;
    private static final Integer SMALLER_REMOTE_ID = 1 - 1;

    private static final Boolean DEFAULT_DEFAULT_ADDRESS = false;
    private static final Boolean UPDATED_DEFAULT_ADDRESS = true;

    private static final String DEFAULT_COUNTRY = "AAAAAAAAAA";
    private static final String UPDATED_COUNTRY = "BBBBBBBBBB";

    private static final String DEFAULT_STREET = "AAAAAAAAAA";
    private static final String UPDATED_STREET = "BBBBBBBBBB";

    private static final String DEFAULT_STREET_NO = "AAAAAAAAAA";
    private static final String UPDATED_STREET_NO = "BBBBBBBBBB";

    private static final String DEFAULT_STREET_2 = "AAAAAAAAAA";
    private static final String UPDATED_STREET_2 = "BBBBBBBBBB";

    private static final String DEFAULT_POSTCODE = "AAAAAAAAAA";
    private static final String UPDATED_POSTCODE = "BBBBBBBBBB";

    private static final String DEFAULT_CITY = "AAAAAAAAAA";
    private static final String UPDATED_CITY = "BBBBBBBBBB";

    private static final Boolean DEFAULT_HIDE_ON_DOCUMENTS = false;
    private static final Boolean UPDATED_HIDE_ON_DOCUMENTS = true;

    private static final Boolean DEFAULT_DEFAULT_PREPAGE = false;
    private static final Boolean UPDATED_DEFAULT_PREPAGE = true;

    private static final String ENTITY_API_URL = "/api/contact-addresses";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";
    private static final String ENTITY_SEARCH_API_URL = "/api/_search/contact-addresses";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ContactAddressRepository contactAddressRepository;

    @Autowired
    private ContactAddressMapper contactAddressMapper;

    /**
     * This repository is mocked in the ch.united.fastadmin.repository.search test package.
     *
     * @see ch.united.fastadmin.repository.search.ContactAddressSearchRepositoryMockConfiguration
     */
    @Autowired
    private ContactAddressSearchRepository mockContactAddressSearchRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restContactAddressMockMvc;

    private ContactAddress contactAddress;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static ContactAddress createEntity(EntityManager em) {
        ContactAddress contactAddress = new ContactAddress()
            .remoteId(DEFAULT_REMOTE_ID)
            .defaultAddress(DEFAULT_DEFAULT_ADDRESS)
            .country(DEFAULT_COUNTRY)
            .street(DEFAULT_STREET)
            .streetNo(DEFAULT_STREET_NO)
            .street2(DEFAULT_STREET_2)
            .postcode(DEFAULT_POSTCODE)
            .city(DEFAULT_CITY)
            .hideOnDocuments(DEFAULT_HIDE_ON_DOCUMENTS)
            .defaultPrepage(DEFAULT_DEFAULT_PREPAGE);
        return contactAddress;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static ContactAddress createUpdatedEntity(EntityManager em) {
        ContactAddress contactAddress = new ContactAddress()
            .remoteId(UPDATED_REMOTE_ID)
            .defaultAddress(UPDATED_DEFAULT_ADDRESS)
            .country(UPDATED_COUNTRY)
            .street(UPDATED_STREET)
            .streetNo(UPDATED_STREET_NO)
            .street2(UPDATED_STREET_2)
            .postcode(UPDATED_POSTCODE)
            .city(UPDATED_CITY)
            .hideOnDocuments(UPDATED_HIDE_ON_DOCUMENTS)
            .defaultPrepage(UPDATED_DEFAULT_PREPAGE);
        return contactAddress;
    }

    @BeforeEach
    public void initTest() {
        contactAddress = createEntity(em);
    }

    @Test
    @Transactional
    void createContactAddress() throws Exception {
        int databaseSizeBeforeCreate = contactAddressRepository.findAll().size();
        // Create the ContactAddress
        ContactAddressDTO contactAddressDTO = contactAddressMapper.toDto(contactAddress);
        restContactAddressMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(contactAddressDTO))
            )
            .andExpect(status().isCreated());

        // Validate the ContactAddress in the database
        List<ContactAddress> contactAddressList = contactAddressRepository.findAll();
        assertThat(contactAddressList).hasSize(databaseSizeBeforeCreate + 1);
        ContactAddress testContactAddress = contactAddressList.get(contactAddressList.size() - 1);
        assertThat(testContactAddress.getRemoteId()).isEqualTo(DEFAULT_REMOTE_ID);
        assertThat(testContactAddress.getDefaultAddress()).isEqualTo(DEFAULT_DEFAULT_ADDRESS);
        assertThat(testContactAddress.getCountry()).isEqualTo(DEFAULT_COUNTRY);
        assertThat(testContactAddress.getStreet()).isEqualTo(DEFAULT_STREET);
        assertThat(testContactAddress.getStreetNo()).isEqualTo(DEFAULT_STREET_NO);
        assertThat(testContactAddress.getStreet2()).isEqualTo(DEFAULT_STREET_2);
        assertThat(testContactAddress.getPostcode()).isEqualTo(DEFAULT_POSTCODE);
        assertThat(testContactAddress.getCity()).isEqualTo(DEFAULT_CITY);
        assertThat(testContactAddress.getHideOnDocuments()).isEqualTo(DEFAULT_HIDE_ON_DOCUMENTS);
        assertThat(testContactAddress.getDefaultPrepage()).isEqualTo(DEFAULT_DEFAULT_PREPAGE);

        // Validate the ContactAddress in Elasticsearch
        verify(mockContactAddressSearchRepository, times(1)).save(testContactAddress);
    }

    @Test
    @Transactional
    void createContactAddressWithExistingId() throws Exception {
        // Create the ContactAddress with an existing ID
        contactAddress.setId(1L);
        ContactAddressDTO contactAddressDTO = contactAddressMapper.toDto(contactAddress);

        int databaseSizeBeforeCreate = contactAddressRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restContactAddressMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(contactAddressDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the ContactAddress in the database
        List<ContactAddress> contactAddressList = contactAddressRepository.findAll();
        assertThat(contactAddressList).hasSize(databaseSizeBeforeCreate);

        // Validate the ContactAddress in Elasticsearch
        verify(mockContactAddressSearchRepository, times(0)).save(contactAddress);
    }

    @Test
    @Transactional
    void checkDefaultAddressIsRequired() throws Exception {
        int databaseSizeBeforeTest = contactAddressRepository.findAll().size();
        // set the field null
        contactAddress.setDefaultAddress(null);

        // Create the ContactAddress, which fails.
        ContactAddressDTO contactAddressDTO = contactAddressMapper.toDto(contactAddress);

        restContactAddressMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(contactAddressDTO))
            )
            .andExpect(status().isBadRequest());

        List<ContactAddress> contactAddressList = contactAddressRepository.findAll();
        assertThat(contactAddressList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkCountryIsRequired() throws Exception {
        int databaseSizeBeforeTest = contactAddressRepository.findAll().size();
        // set the field null
        contactAddress.setCountry(null);

        // Create the ContactAddress, which fails.
        ContactAddressDTO contactAddressDTO = contactAddressMapper.toDto(contactAddress);

        restContactAddressMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(contactAddressDTO))
            )
            .andExpect(status().isBadRequest());

        List<ContactAddress> contactAddressList = contactAddressRepository.findAll();
        assertThat(contactAddressList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkHideOnDocumentsIsRequired() throws Exception {
        int databaseSizeBeforeTest = contactAddressRepository.findAll().size();
        // set the field null
        contactAddress.setHideOnDocuments(null);

        // Create the ContactAddress, which fails.
        ContactAddressDTO contactAddressDTO = contactAddressMapper.toDto(contactAddress);

        restContactAddressMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(contactAddressDTO))
            )
            .andExpect(status().isBadRequest());

        List<ContactAddress> contactAddressList = contactAddressRepository.findAll();
        assertThat(contactAddressList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkDefaultPrepageIsRequired() throws Exception {
        int databaseSizeBeforeTest = contactAddressRepository.findAll().size();
        // set the field null
        contactAddress.setDefaultPrepage(null);

        // Create the ContactAddress, which fails.
        ContactAddressDTO contactAddressDTO = contactAddressMapper.toDto(contactAddress);

        restContactAddressMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(contactAddressDTO))
            )
            .andExpect(status().isBadRequest());

        List<ContactAddress> contactAddressList = contactAddressRepository.findAll();
        assertThat(contactAddressList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllContactAddresses() throws Exception {
        // Initialize the database
        contactAddressRepository.saveAndFlush(contactAddress);

        // Get all the contactAddressList
        restContactAddressMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(contactAddress.getId().intValue())))
            .andExpect(jsonPath("$.[*].remoteId").value(hasItem(DEFAULT_REMOTE_ID)))
            .andExpect(jsonPath("$.[*].defaultAddress").value(hasItem(DEFAULT_DEFAULT_ADDRESS.booleanValue())))
            .andExpect(jsonPath("$.[*].country").value(hasItem(DEFAULT_COUNTRY)))
            .andExpect(jsonPath("$.[*].street").value(hasItem(DEFAULT_STREET)))
            .andExpect(jsonPath("$.[*].streetNo").value(hasItem(DEFAULT_STREET_NO)))
            .andExpect(jsonPath("$.[*].street2").value(hasItem(DEFAULT_STREET_2)))
            .andExpect(jsonPath("$.[*].postcode").value(hasItem(DEFAULT_POSTCODE)))
            .andExpect(jsonPath("$.[*].city").value(hasItem(DEFAULT_CITY)))
            .andExpect(jsonPath("$.[*].hideOnDocuments").value(hasItem(DEFAULT_HIDE_ON_DOCUMENTS.booleanValue())))
            .andExpect(jsonPath("$.[*].defaultPrepage").value(hasItem(DEFAULT_DEFAULT_PREPAGE.booleanValue())));
    }

    @Test
    @Transactional
    void getContactAddress() throws Exception {
        // Initialize the database
        contactAddressRepository.saveAndFlush(contactAddress);

        // Get the contactAddress
        restContactAddressMockMvc
            .perform(get(ENTITY_API_URL_ID, contactAddress.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(contactAddress.getId().intValue()))
            .andExpect(jsonPath("$.remoteId").value(DEFAULT_REMOTE_ID))
            .andExpect(jsonPath("$.defaultAddress").value(DEFAULT_DEFAULT_ADDRESS.booleanValue()))
            .andExpect(jsonPath("$.country").value(DEFAULT_COUNTRY))
            .andExpect(jsonPath("$.street").value(DEFAULT_STREET))
            .andExpect(jsonPath("$.streetNo").value(DEFAULT_STREET_NO))
            .andExpect(jsonPath("$.street2").value(DEFAULT_STREET_2))
            .andExpect(jsonPath("$.postcode").value(DEFAULT_POSTCODE))
            .andExpect(jsonPath("$.city").value(DEFAULT_CITY))
            .andExpect(jsonPath("$.hideOnDocuments").value(DEFAULT_HIDE_ON_DOCUMENTS.booleanValue()))
            .andExpect(jsonPath("$.defaultPrepage").value(DEFAULT_DEFAULT_PREPAGE.booleanValue()));
    }

    @Test
    @Transactional
    void getContactAddressesByIdFiltering() throws Exception {
        // Initialize the database
        contactAddressRepository.saveAndFlush(contactAddress);

        Long id = contactAddress.getId();

        defaultContactAddressShouldBeFound("id.equals=" + id);
        defaultContactAddressShouldNotBeFound("id.notEquals=" + id);

        defaultContactAddressShouldBeFound("id.greaterThanOrEqual=" + id);
        defaultContactAddressShouldNotBeFound("id.greaterThan=" + id);

        defaultContactAddressShouldBeFound("id.lessThanOrEqual=" + id);
        defaultContactAddressShouldNotBeFound("id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllContactAddressesByRemoteIdIsEqualToSomething() throws Exception {
        // Initialize the database
        contactAddressRepository.saveAndFlush(contactAddress);

        // Get all the contactAddressList where remoteId equals to DEFAULT_REMOTE_ID
        defaultContactAddressShouldBeFound("remoteId.equals=" + DEFAULT_REMOTE_ID);

        // Get all the contactAddressList where remoteId equals to UPDATED_REMOTE_ID
        defaultContactAddressShouldNotBeFound("remoteId.equals=" + UPDATED_REMOTE_ID);
    }

    @Test
    @Transactional
    void getAllContactAddressesByRemoteIdIsNotEqualToSomething() throws Exception {
        // Initialize the database
        contactAddressRepository.saveAndFlush(contactAddress);

        // Get all the contactAddressList where remoteId not equals to DEFAULT_REMOTE_ID
        defaultContactAddressShouldNotBeFound("remoteId.notEquals=" + DEFAULT_REMOTE_ID);

        // Get all the contactAddressList where remoteId not equals to UPDATED_REMOTE_ID
        defaultContactAddressShouldBeFound("remoteId.notEquals=" + UPDATED_REMOTE_ID);
    }

    @Test
    @Transactional
    void getAllContactAddressesByRemoteIdIsInShouldWork() throws Exception {
        // Initialize the database
        contactAddressRepository.saveAndFlush(contactAddress);

        // Get all the contactAddressList where remoteId in DEFAULT_REMOTE_ID or UPDATED_REMOTE_ID
        defaultContactAddressShouldBeFound("remoteId.in=" + DEFAULT_REMOTE_ID + "," + UPDATED_REMOTE_ID);

        // Get all the contactAddressList where remoteId equals to UPDATED_REMOTE_ID
        defaultContactAddressShouldNotBeFound("remoteId.in=" + UPDATED_REMOTE_ID);
    }

    @Test
    @Transactional
    void getAllContactAddressesByRemoteIdIsNullOrNotNull() throws Exception {
        // Initialize the database
        contactAddressRepository.saveAndFlush(contactAddress);

        // Get all the contactAddressList where remoteId is not null
        defaultContactAddressShouldBeFound("remoteId.specified=true");

        // Get all the contactAddressList where remoteId is null
        defaultContactAddressShouldNotBeFound("remoteId.specified=false");
    }

    @Test
    @Transactional
    void getAllContactAddressesByRemoteIdIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        contactAddressRepository.saveAndFlush(contactAddress);

        // Get all the contactAddressList where remoteId is greater than or equal to DEFAULT_REMOTE_ID
        defaultContactAddressShouldBeFound("remoteId.greaterThanOrEqual=" + DEFAULT_REMOTE_ID);

        // Get all the contactAddressList where remoteId is greater than or equal to UPDATED_REMOTE_ID
        defaultContactAddressShouldNotBeFound("remoteId.greaterThanOrEqual=" + UPDATED_REMOTE_ID);
    }

    @Test
    @Transactional
    void getAllContactAddressesByRemoteIdIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        contactAddressRepository.saveAndFlush(contactAddress);

        // Get all the contactAddressList where remoteId is less than or equal to DEFAULT_REMOTE_ID
        defaultContactAddressShouldBeFound("remoteId.lessThanOrEqual=" + DEFAULT_REMOTE_ID);

        // Get all the contactAddressList where remoteId is less than or equal to SMALLER_REMOTE_ID
        defaultContactAddressShouldNotBeFound("remoteId.lessThanOrEqual=" + SMALLER_REMOTE_ID);
    }

    @Test
    @Transactional
    void getAllContactAddressesByRemoteIdIsLessThanSomething() throws Exception {
        // Initialize the database
        contactAddressRepository.saveAndFlush(contactAddress);

        // Get all the contactAddressList where remoteId is less than DEFAULT_REMOTE_ID
        defaultContactAddressShouldNotBeFound("remoteId.lessThan=" + DEFAULT_REMOTE_ID);

        // Get all the contactAddressList where remoteId is less than UPDATED_REMOTE_ID
        defaultContactAddressShouldBeFound("remoteId.lessThan=" + UPDATED_REMOTE_ID);
    }

    @Test
    @Transactional
    void getAllContactAddressesByRemoteIdIsGreaterThanSomething() throws Exception {
        // Initialize the database
        contactAddressRepository.saveAndFlush(contactAddress);

        // Get all the contactAddressList where remoteId is greater than DEFAULT_REMOTE_ID
        defaultContactAddressShouldNotBeFound("remoteId.greaterThan=" + DEFAULT_REMOTE_ID);

        // Get all the contactAddressList where remoteId is greater than SMALLER_REMOTE_ID
        defaultContactAddressShouldBeFound("remoteId.greaterThan=" + SMALLER_REMOTE_ID);
    }

    @Test
    @Transactional
    void getAllContactAddressesByDefaultAddressIsEqualToSomething() throws Exception {
        // Initialize the database
        contactAddressRepository.saveAndFlush(contactAddress);

        // Get all the contactAddressList where defaultAddress equals to DEFAULT_DEFAULT_ADDRESS
        defaultContactAddressShouldBeFound("defaultAddress.equals=" + DEFAULT_DEFAULT_ADDRESS);

        // Get all the contactAddressList where defaultAddress equals to UPDATED_DEFAULT_ADDRESS
        defaultContactAddressShouldNotBeFound("defaultAddress.equals=" + UPDATED_DEFAULT_ADDRESS);
    }

    @Test
    @Transactional
    void getAllContactAddressesByDefaultAddressIsNotEqualToSomething() throws Exception {
        // Initialize the database
        contactAddressRepository.saveAndFlush(contactAddress);

        // Get all the contactAddressList where defaultAddress not equals to DEFAULT_DEFAULT_ADDRESS
        defaultContactAddressShouldNotBeFound("defaultAddress.notEquals=" + DEFAULT_DEFAULT_ADDRESS);

        // Get all the contactAddressList where defaultAddress not equals to UPDATED_DEFAULT_ADDRESS
        defaultContactAddressShouldBeFound("defaultAddress.notEquals=" + UPDATED_DEFAULT_ADDRESS);
    }

    @Test
    @Transactional
    void getAllContactAddressesByDefaultAddressIsInShouldWork() throws Exception {
        // Initialize the database
        contactAddressRepository.saveAndFlush(contactAddress);

        // Get all the contactAddressList where defaultAddress in DEFAULT_DEFAULT_ADDRESS or UPDATED_DEFAULT_ADDRESS
        defaultContactAddressShouldBeFound("defaultAddress.in=" + DEFAULT_DEFAULT_ADDRESS + "," + UPDATED_DEFAULT_ADDRESS);

        // Get all the contactAddressList where defaultAddress equals to UPDATED_DEFAULT_ADDRESS
        defaultContactAddressShouldNotBeFound("defaultAddress.in=" + UPDATED_DEFAULT_ADDRESS);
    }

    @Test
    @Transactional
    void getAllContactAddressesByDefaultAddressIsNullOrNotNull() throws Exception {
        // Initialize the database
        contactAddressRepository.saveAndFlush(contactAddress);

        // Get all the contactAddressList where defaultAddress is not null
        defaultContactAddressShouldBeFound("defaultAddress.specified=true");

        // Get all the contactAddressList where defaultAddress is null
        defaultContactAddressShouldNotBeFound("defaultAddress.specified=false");
    }

    @Test
    @Transactional
    void getAllContactAddressesByCountryIsEqualToSomething() throws Exception {
        // Initialize the database
        contactAddressRepository.saveAndFlush(contactAddress);

        // Get all the contactAddressList where country equals to DEFAULT_COUNTRY
        defaultContactAddressShouldBeFound("country.equals=" + DEFAULT_COUNTRY);

        // Get all the contactAddressList where country equals to UPDATED_COUNTRY
        defaultContactAddressShouldNotBeFound("country.equals=" + UPDATED_COUNTRY);
    }

    @Test
    @Transactional
    void getAllContactAddressesByCountryIsNotEqualToSomething() throws Exception {
        // Initialize the database
        contactAddressRepository.saveAndFlush(contactAddress);

        // Get all the contactAddressList where country not equals to DEFAULT_COUNTRY
        defaultContactAddressShouldNotBeFound("country.notEquals=" + DEFAULT_COUNTRY);

        // Get all the contactAddressList where country not equals to UPDATED_COUNTRY
        defaultContactAddressShouldBeFound("country.notEquals=" + UPDATED_COUNTRY);
    }

    @Test
    @Transactional
    void getAllContactAddressesByCountryIsInShouldWork() throws Exception {
        // Initialize the database
        contactAddressRepository.saveAndFlush(contactAddress);

        // Get all the contactAddressList where country in DEFAULT_COUNTRY or UPDATED_COUNTRY
        defaultContactAddressShouldBeFound("country.in=" + DEFAULT_COUNTRY + "," + UPDATED_COUNTRY);

        // Get all the contactAddressList where country equals to UPDATED_COUNTRY
        defaultContactAddressShouldNotBeFound("country.in=" + UPDATED_COUNTRY);
    }

    @Test
    @Transactional
    void getAllContactAddressesByCountryIsNullOrNotNull() throws Exception {
        // Initialize the database
        contactAddressRepository.saveAndFlush(contactAddress);

        // Get all the contactAddressList where country is not null
        defaultContactAddressShouldBeFound("country.specified=true");

        // Get all the contactAddressList where country is null
        defaultContactAddressShouldNotBeFound("country.specified=false");
    }

    @Test
    @Transactional
    void getAllContactAddressesByCountryContainsSomething() throws Exception {
        // Initialize the database
        contactAddressRepository.saveAndFlush(contactAddress);

        // Get all the contactAddressList where country contains DEFAULT_COUNTRY
        defaultContactAddressShouldBeFound("country.contains=" + DEFAULT_COUNTRY);

        // Get all the contactAddressList where country contains UPDATED_COUNTRY
        defaultContactAddressShouldNotBeFound("country.contains=" + UPDATED_COUNTRY);
    }

    @Test
    @Transactional
    void getAllContactAddressesByCountryNotContainsSomething() throws Exception {
        // Initialize the database
        contactAddressRepository.saveAndFlush(contactAddress);

        // Get all the contactAddressList where country does not contain DEFAULT_COUNTRY
        defaultContactAddressShouldNotBeFound("country.doesNotContain=" + DEFAULT_COUNTRY);

        // Get all the contactAddressList where country does not contain UPDATED_COUNTRY
        defaultContactAddressShouldBeFound("country.doesNotContain=" + UPDATED_COUNTRY);
    }

    @Test
    @Transactional
    void getAllContactAddressesByStreetIsEqualToSomething() throws Exception {
        // Initialize the database
        contactAddressRepository.saveAndFlush(contactAddress);

        // Get all the contactAddressList where street equals to DEFAULT_STREET
        defaultContactAddressShouldBeFound("street.equals=" + DEFAULT_STREET);

        // Get all the contactAddressList where street equals to UPDATED_STREET
        defaultContactAddressShouldNotBeFound("street.equals=" + UPDATED_STREET);
    }

    @Test
    @Transactional
    void getAllContactAddressesByStreetIsNotEqualToSomething() throws Exception {
        // Initialize the database
        contactAddressRepository.saveAndFlush(contactAddress);

        // Get all the contactAddressList where street not equals to DEFAULT_STREET
        defaultContactAddressShouldNotBeFound("street.notEquals=" + DEFAULT_STREET);

        // Get all the contactAddressList where street not equals to UPDATED_STREET
        defaultContactAddressShouldBeFound("street.notEquals=" + UPDATED_STREET);
    }

    @Test
    @Transactional
    void getAllContactAddressesByStreetIsInShouldWork() throws Exception {
        // Initialize the database
        contactAddressRepository.saveAndFlush(contactAddress);

        // Get all the contactAddressList where street in DEFAULT_STREET or UPDATED_STREET
        defaultContactAddressShouldBeFound("street.in=" + DEFAULT_STREET + "," + UPDATED_STREET);

        // Get all the contactAddressList where street equals to UPDATED_STREET
        defaultContactAddressShouldNotBeFound("street.in=" + UPDATED_STREET);
    }

    @Test
    @Transactional
    void getAllContactAddressesByStreetIsNullOrNotNull() throws Exception {
        // Initialize the database
        contactAddressRepository.saveAndFlush(contactAddress);

        // Get all the contactAddressList where street is not null
        defaultContactAddressShouldBeFound("street.specified=true");

        // Get all the contactAddressList where street is null
        defaultContactAddressShouldNotBeFound("street.specified=false");
    }

    @Test
    @Transactional
    void getAllContactAddressesByStreetContainsSomething() throws Exception {
        // Initialize the database
        contactAddressRepository.saveAndFlush(contactAddress);

        // Get all the contactAddressList where street contains DEFAULT_STREET
        defaultContactAddressShouldBeFound("street.contains=" + DEFAULT_STREET);

        // Get all the contactAddressList where street contains UPDATED_STREET
        defaultContactAddressShouldNotBeFound("street.contains=" + UPDATED_STREET);
    }

    @Test
    @Transactional
    void getAllContactAddressesByStreetNotContainsSomething() throws Exception {
        // Initialize the database
        contactAddressRepository.saveAndFlush(contactAddress);

        // Get all the contactAddressList where street does not contain DEFAULT_STREET
        defaultContactAddressShouldNotBeFound("street.doesNotContain=" + DEFAULT_STREET);

        // Get all the contactAddressList where street does not contain UPDATED_STREET
        defaultContactAddressShouldBeFound("street.doesNotContain=" + UPDATED_STREET);
    }

    @Test
    @Transactional
    void getAllContactAddressesByStreetNoIsEqualToSomething() throws Exception {
        // Initialize the database
        contactAddressRepository.saveAndFlush(contactAddress);

        // Get all the contactAddressList where streetNo equals to DEFAULT_STREET_NO
        defaultContactAddressShouldBeFound("streetNo.equals=" + DEFAULT_STREET_NO);

        // Get all the contactAddressList where streetNo equals to UPDATED_STREET_NO
        defaultContactAddressShouldNotBeFound("streetNo.equals=" + UPDATED_STREET_NO);
    }

    @Test
    @Transactional
    void getAllContactAddressesByStreetNoIsNotEqualToSomething() throws Exception {
        // Initialize the database
        contactAddressRepository.saveAndFlush(contactAddress);

        // Get all the contactAddressList where streetNo not equals to DEFAULT_STREET_NO
        defaultContactAddressShouldNotBeFound("streetNo.notEquals=" + DEFAULT_STREET_NO);

        // Get all the contactAddressList where streetNo not equals to UPDATED_STREET_NO
        defaultContactAddressShouldBeFound("streetNo.notEquals=" + UPDATED_STREET_NO);
    }

    @Test
    @Transactional
    void getAllContactAddressesByStreetNoIsInShouldWork() throws Exception {
        // Initialize the database
        contactAddressRepository.saveAndFlush(contactAddress);

        // Get all the contactAddressList where streetNo in DEFAULT_STREET_NO or UPDATED_STREET_NO
        defaultContactAddressShouldBeFound("streetNo.in=" + DEFAULT_STREET_NO + "," + UPDATED_STREET_NO);

        // Get all the contactAddressList where streetNo equals to UPDATED_STREET_NO
        defaultContactAddressShouldNotBeFound("streetNo.in=" + UPDATED_STREET_NO);
    }

    @Test
    @Transactional
    void getAllContactAddressesByStreetNoIsNullOrNotNull() throws Exception {
        // Initialize the database
        contactAddressRepository.saveAndFlush(contactAddress);

        // Get all the contactAddressList where streetNo is not null
        defaultContactAddressShouldBeFound("streetNo.specified=true");

        // Get all the contactAddressList where streetNo is null
        defaultContactAddressShouldNotBeFound("streetNo.specified=false");
    }

    @Test
    @Transactional
    void getAllContactAddressesByStreetNoContainsSomething() throws Exception {
        // Initialize the database
        contactAddressRepository.saveAndFlush(contactAddress);

        // Get all the contactAddressList where streetNo contains DEFAULT_STREET_NO
        defaultContactAddressShouldBeFound("streetNo.contains=" + DEFAULT_STREET_NO);

        // Get all the contactAddressList where streetNo contains UPDATED_STREET_NO
        defaultContactAddressShouldNotBeFound("streetNo.contains=" + UPDATED_STREET_NO);
    }

    @Test
    @Transactional
    void getAllContactAddressesByStreetNoNotContainsSomething() throws Exception {
        // Initialize the database
        contactAddressRepository.saveAndFlush(contactAddress);

        // Get all the contactAddressList where streetNo does not contain DEFAULT_STREET_NO
        defaultContactAddressShouldNotBeFound("streetNo.doesNotContain=" + DEFAULT_STREET_NO);

        // Get all the contactAddressList where streetNo does not contain UPDATED_STREET_NO
        defaultContactAddressShouldBeFound("streetNo.doesNotContain=" + UPDATED_STREET_NO);
    }

    @Test
    @Transactional
    void getAllContactAddressesByStreet2IsEqualToSomething() throws Exception {
        // Initialize the database
        contactAddressRepository.saveAndFlush(contactAddress);

        // Get all the contactAddressList where street2 equals to DEFAULT_STREET_2
        defaultContactAddressShouldBeFound("street2.equals=" + DEFAULT_STREET_2);

        // Get all the contactAddressList where street2 equals to UPDATED_STREET_2
        defaultContactAddressShouldNotBeFound("street2.equals=" + UPDATED_STREET_2);
    }

    @Test
    @Transactional
    void getAllContactAddressesByStreet2IsNotEqualToSomething() throws Exception {
        // Initialize the database
        contactAddressRepository.saveAndFlush(contactAddress);

        // Get all the contactAddressList where street2 not equals to DEFAULT_STREET_2
        defaultContactAddressShouldNotBeFound("street2.notEquals=" + DEFAULT_STREET_2);

        // Get all the contactAddressList where street2 not equals to UPDATED_STREET_2
        defaultContactAddressShouldBeFound("street2.notEquals=" + UPDATED_STREET_2);
    }

    @Test
    @Transactional
    void getAllContactAddressesByStreet2IsInShouldWork() throws Exception {
        // Initialize the database
        contactAddressRepository.saveAndFlush(contactAddress);

        // Get all the contactAddressList where street2 in DEFAULT_STREET_2 or UPDATED_STREET_2
        defaultContactAddressShouldBeFound("street2.in=" + DEFAULT_STREET_2 + "," + UPDATED_STREET_2);

        // Get all the contactAddressList where street2 equals to UPDATED_STREET_2
        defaultContactAddressShouldNotBeFound("street2.in=" + UPDATED_STREET_2);
    }

    @Test
    @Transactional
    void getAllContactAddressesByStreet2IsNullOrNotNull() throws Exception {
        // Initialize the database
        contactAddressRepository.saveAndFlush(contactAddress);

        // Get all the contactAddressList where street2 is not null
        defaultContactAddressShouldBeFound("street2.specified=true");

        // Get all the contactAddressList where street2 is null
        defaultContactAddressShouldNotBeFound("street2.specified=false");
    }

    @Test
    @Transactional
    void getAllContactAddressesByStreet2ContainsSomething() throws Exception {
        // Initialize the database
        contactAddressRepository.saveAndFlush(contactAddress);

        // Get all the contactAddressList where street2 contains DEFAULT_STREET_2
        defaultContactAddressShouldBeFound("street2.contains=" + DEFAULT_STREET_2);

        // Get all the contactAddressList where street2 contains UPDATED_STREET_2
        defaultContactAddressShouldNotBeFound("street2.contains=" + UPDATED_STREET_2);
    }

    @Test
    @Transactional
    void getAllContactAddressesByStreet2NotContainsSomething() throws Exception {
        // Initialize the database
        contactAddressRepository.saveAndFlush(contactAddress);

        // Get all the contactAddressList where street2 does not contain DEFAULT_STREET_2
        defaultContactAddressShouldNotBeFound("street2.doesNotContain=" + DEFAULT_STREET_2);

        // Get all the contactAddressList where street2 does not contain UPDATED_STREET_2
        defaultContactAddressShouldBeFound("street2.doesNotContain=" + UPDATED_STREET_2);
    }

    @Test
    @Transactional
    void getAllContactAddressesByPostcodeIsEqualToSomething() throws Exception {
        // Initialize the database
        contactAddressRepository.saveAndFlush(contactAddress);

        // Get all the contactAddressList where postcode equals to DEFAULT_POSTCODE
        defaultContactAddressShouldBeFound("postcode.equals=" + DEFAULT_POSTCODE);

        // Get all the contactAddressList where postcode equals to UPDATED_POSTCODE
        defaultContactAddressShouldNotBeFound("postcode.equals=" + UPDATED_POSTCODE);
    }

    @Test
    @Transactional
    void getAllContactAddressesByPostcodeIsNotEqualToSomething() throws Exception {
        // Initialize the database
        contactAddressRepository.saveAndFlush(contactAddress);

        // Get all the contactAddressList where postcode not equals to DEFAULT_POSTCODE
        defaultContactAddressShouldNotBeFound("postcode.notEquals=" + DEFAULT_POSTCODE);

        // Get all the contactAddressList where postcode not equals to UPDATED_POSTCODE
        defaultContactAddressShouldBeFound("postcode.notEquals=" + UPDATED_POSTCODE);
    }

    @Test
    @Transactional
    void getAllContactAddressesByPostcodeIsInShouldWork() throws Exception {
        // Initialize the database
        contactAddressRepository.saveAndFlush(contactAddress);

        // Get all the contactAddressList where postcode in DEFAULT_POSTCODE or UPDATED_POSTCODE
        defaultContactAddressShouldBeFound("postcode.in=" + DEFAULT_POSTCODE + "," + UPDATED_POSTCODE);

        // Get all the contactAddressList where postcode equals to UPDATED_POSTCODE
        defaultContactAddressShouldNotBeFound("postcode.in=" + UPDATED_POSTCODE);
    }

    @Test
    @Transactional
    void getAllContactAddressesByPostcodeIsNullOrNotNull() throws Exception {
        // Initialize the database
        contactAddressRepository.saveAndFlush(contactAddress);

        // Get all the contactAddressList where postcode is not null
        defaultContactAddressShouldBeFound("postcode.specified=true");

        // Get all the contactAddressList where postcode is null
        defaultContactAddressShouldNotBeFound("postcode.specified=false");
    }

    @Test
    @Transactional
    void getAllContactAddressesByPostcodeContainsSomething() throws Exception {
        // Initialize the database
        contactAddressRepository.saveAndFlush(contactAddress);

        // Get all the contactAddressList where postcode contains DEFAULT_POSTCODE
        defaultContactAddressShouldBeFound("postcode.contains=" + DEFAULT_POSTCODE);

        // Get all the contactAddressList where postcode contains UPDATED_POSTCODE
        defaultContactAddressShouldNotBeFound("postcode.contains=" + UPDATED_POSTCODE);
    }

    @Test
    @Transactional
    void getAllContactAddressesByPostcodeNotContainsSomething() throws Exception {
        // Initialize the database
        contactAddressRepository.saveAndFlush(contactAddress);

        // Get all the contactAddressList where postcode does not contain DEFAULT_POSTCODE
        defaultContactAddressShouldNotBeFound("postcode.doesNotContain=" + DEFAULT_POSTCODE);

        // Get all the contactAddressList where postcode does not contain UPDATED_POSTCODE
        defaultContactAddressShouldBeFound("postcode.doesNotContain=" + UPDATED_POSTCODE);
    }

    @Test
    @Transactional
    void getAllContactAddressesByCityIsEqualToSomething() throws Exception {
        // Initialize the database
        contactAddressRepository.saveAndFlush(contactAddress);

        // Get all the contactAddressList where city equals to DEFAULT_CITY
        defaultContactAddressShouldBeFound("city.equals=" + DEFAULT_CITY);

        // Get all the contactAddressList where city equals to UPDATED_CITY
        defaultContactAddressShouldNotBeFound("city.equals=" + UPDATED_CITY);
    }

    @Test
    @Transactional
    void getAllContactAddressesByCityIsNotEqualToSomething() throws Exception {
        // Initialize the database
        contactAddressRepository.saveAndFlush(contactAddress);

        // Get all the contactAddressList where city not equals to DEFAULT_CITY
        defaultContactAddressShouldNotBeFound("city.notEquals=" + DEFAULT_CITY);

        // Get all the contactAddressList where city not equals to UPDATED_CITY
        defaultContactAddressShouldBeFound("city.notEquals=" + UPDATED_CITY);
    }

    @Test
    @Transactional
    void getAllContactAddressesByCityIsInShouldWork() throws Exception {
        // Initialize the database
        contactAddressRepository.saveAndFlush(contactAddress);

        // Get all the contactAddressList where city in DEFAULT_CITY or UPDATED_CITY
        defaultContactAddressShouldBeFound("city.in=" + DEFAULT_CITY + "," + UPDATED_CITY);

        // Get all the contactAddressList where city equals to UPDATED_CITY
        defaultContactAddressShouldNotBeFound("city.in=" + UPDATED_CITY);
    }

    @Test
    @Transactional
    void getAllContactAddressesByCityIsNullOrNotNull() throws Exception {
        // Initialize the database
        contactAddressRepository.saveAndFlush(contactAddress);

        // Get all the contactAddressList where city is not null
        defaultContactAddressShouldBeFound("city.specified=true");

        // Get all the contactAddressList where city is null
        defaultContactAddressShouldNotBeFound("city.specified=false");
    }

    @Test
    @Transactional
    void getAllContactAddressesByCityContainsSomething() throws Exception {
        // Initialize the database
        contactAddressRepository.saveAndFlush(contactAddress);

        // Get all the contactAddressList where city contains DEFAULT_CITY
        defaultContactAddressShouldBeFound("city.contains=" + DEFAULT_CITY);

        // Get all the contactAddressList where city contains UPDATED_CITY
        defaultContactAddressShouldNotBeFound("city.contains=" + UPDATED_CITY);
    }

    @Test
    @Transactional
    void getAllContactAddressesByCityNotContainsSomething() throws Exception {
        // Initialize the database
        contactAddressRepository.saveAndFlush(contactAddress);

        // Get all the contactAddressList where city does not contain DEFAULT_CITY
        defaultContactAddressShouldNotBeFound("city.doesNotContain=" + DEFAULT_CITY);

        // Get all the contactAddressList where city does not contain UPDATED_CITY
        defaultContactAddressShouldBeFound("city.doesNotContain=" + UPDATED_CITY);
    }

    @Test
    @Transactional
    void getAllContactAddressesByHideOnDocumentsIsEqualToSomething() throws Exception {
        // Initialize the database
        contactAddressRepository.saveAndFlush(contactAddress);

        // Get all the contactAddressList where hideOnDocuments equals to DEFAULT_HIDE_ON_DOCUMENTS
        defaultContactAddressShouldBeFound("hideOnDocuments.equals=" + DEFAULT_HIDE_ON_DOCUMENTS);

        // Get all the contactAddressList where hideOnDocuments equals to UPDATED_HIDE_ON_DOCUMENTS
        defaultContactAddressShouldNotBeFound("hideOnDocuments.equals=" + UPDATED_HIDE_ON_DOCUMENTS);
    }

    @Test
    @Transactional
    void getAllContactAddressesByHideOnDocumentsIsNotEqualToSomething() throws Exception {
        // Initialize the database
        contactAddressRepository.saveAndFlush(contactAddress);

        // Get all the contactAddressList where hideOnDocuments not equals to DEFAULT_HIDE_ON_DOCUMENTS
        defaultContactAddressShouldNotBeFound("hideOnDocuments.notEquals=" + DEFAULT_HIDE_ON_DOCUMENTS);

        // Get all the contactAddressList where hideOnDocuments not equals to UPDATED_HIDE_ON_DOCUMENTS
        defaultContactAddressShouldBeFound("hideOnDocuments.notEquals=" + UPDATED_HIDE_ON_DOCUMENTS);
    }

    @Test
    @Transactional
    void getAllContactAddressesByHideOnDocumentsIsInShouldWork() throws Exception {
        // Initialize the database
        contactAddressRepository.saveAndFlush(contactAddress);

        // Get all the contactAddressList where hideOnDocuments in DEFAULT_HIDE_ON_DOCUMENTS or UPDATED_HIDE_ON_DOCUMENTS
        defaultContactAddressShouldBeFound("hideOnDocuments.in=" + DEFAULT_HIDE_ON_DOCUMENTS + "," + UPDATED_HIDE_ON_DOCUMENTS);

        // Get all the contactAddressList where hideOnDocuments equals to UPDATED_HIDE_ON_DOCUMENTS
        defaultContactAddressShouldNotBeFound("hideOnDocuments.in=" + UPDATED_HIDE_ON_DOCUMENTS);
    }

    @Test
    @Transactional
    void getAllContactAddressesByHideOnDocumentsIsNullOrNotNull() throws Exception {
        // Initialize the database
        contactAddressRepository.saveAndFlush(contactAddress);

        // Get all the contactAddressList where hideOnDocuments is not null
        defaultContactAddressShouldBeFound("hideOnDocuments.specified=true");

        // Get all the contactAddressList where hideOnDocuments is null
        defaultContactAddressShouldNotBeFound("hideOnDocuments.specified=false");
    }

    @Test
    @Transactional
    void getAllContactAddressesByDefaultPrepageIsEqualToSomething() throws Exception {
        // Initialize the database
        contactAddressRepository.saveAndFlush(contactAddress);

        // Get all the contactAddressList where defaultPrepage equals to DEFAULT_DEFAULT_PREPAGE
        defaultContactAddressShouldBeFound("defaultPrepage.equals=" + DEFAULT_DEFAULT_PREPAGE);

        // Get all the contactAddressList where defaultPrepage equals to UPDATED_DEFAULT_PREPAGE
        defaultContactAddressShouldNotBeFound("defaultPrepage.equals=" + UPDATED_DEFAULT_PREPAGE);
    }

    @Test
    @Transactional
    void getAllContactAddressesByDefaultPrepageIsNotEqualToSomething() throws Exception {
        // Initialize the database
        contactAddressRepository.saveAndFlush(contactAddress);

        // Get all the contactAddressList where defaultPrepage not equals to DEFAULT_DEFAULT_PREPAGE
        defaultContactAddressShouldNotBeFound("defaultPrepage.notEquals=" + DEFAULT_DEFAULT_PREPAGE);

        // Get all the contactAddressList where defaultPrepage not equals to UPDATED_DEFAULT_PREPAGE
        defaultContactAddressShouldBeFound("defaultPrepage.notEquals=" + UPDATED_DEFAULT_PREPAGE);
    }

    @Test
    @Transactional
    void getAllContactAddressesByDefaultPrepageIsInShouldWork() throws Exception {
        // Initialize the database
        contactAddressRepository.saveAndFlush(contactAddress);

        // Get all the contactAddressList where defaultPrepage in DEFAULT_DEFAULT_PREPAGE or UPDATED_DEFAULT_PREPAGE
        defaultContactAddressShouldBeFound("defaultPrepage.in=" + DEFAULT_DEFAULT_PREPAGE + "," + UPDATED_DEFAULT_PREPAGE);

        // Get all the contactAddressList where defaultPrepage equals to UPDATED_DEFAULT_PREPAGE
        defaultContactAddressShouldNotBeFound("defaultPrepage.in=" + UPDATED_DEFAULT_PREPAGE);
    }

    @Test
    @Transactional
    void getAllContactAddressesByDefaultPrepageIsNullOrNotNull() throws Exception {
        // Initialize the database
        contactAddressRepository.saveAndFlush(contactAddress);

        // Get all the contactAddressList where defaultPrepage is not null
        defaultContactAddressShouldBeFound("defaultPrepage.specified=true");

        // Get all the contactAddressList where defaultPrepage is null
        defaultContactAddressShouldNotBeFound("defaultPrepage.specified=false");
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultContactAddressShouldBeFound(String filter) throws Exception {
        restContactAddressMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(contactAddress.getId().intValue())))
            .andExpect(jsonPath("$.[*].remoteId").value(hasItem(DEFAULT_REMOTE_ID)))
            .andExpect(jsonPath("$.[*].defaultAddress").value(hasItem(DEFAULT_DEFAULT_ADDRESS.booleanValue())))
            .andExpect(jsonPath("$.[*].country").value(hasItem(DEFAULT_COUNTRY)))
            .andExpect(jsonPath("$.[*].street").value(hasItem(DEFAULT_STREET)))
            .andExpect(jsonPath("$.[*].streetNo").value(hasItem(DEFAULT_STREET_NO)))
            .andExpect(jsonPath("$.[*].street2").value(hasItem(DEFAULT_STREET_2)))
            .andExpect(jsonPath("$.[*].postcode").value(hasItem(DEFAULT_POSTCODE)))
            .andExpect(jsonPath("$.[*].city").value(hasItem(DEFAULT_CITY)))
            .andExpect(jsonPath("$.[*].hideOnDocuments").value(hasItem(DEFAULT_HIDE_ON_DOCUMENTS.booleanValue())))
            .andExpect(jsonPath("$.[*].defaultPrepage").value(hasItem(DEFAULT_DEFAULT_PREPAGE.booleanValue())));

        // Check, that the count call also returns 1
        restContactAddressMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultContactAddressShouldNotBeFound(String filter) throws Exception {
        restContactAddressMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restContactAddressMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingContactAddress() throws Exception {
        // Get the contactAddress
        restContactAddressMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewContactAddress() throws Exception {
        // Initialize the database
        contactAddressRepository.saveAndFlush(contactAddress);

        int databaseSizeBeforeUpdate = contactAddressRepository.findAll().size();

        // Update the contactAddress
        ContactAddress updatedContactAddress = contactAddressRepository.findById(contactAddress.getId()).get();
        // Disconnect from session so that the updates on updatedContactAddress are not directly saved in db
        em.detach(updatedContactAddress);
        updatedContactAddress
            .remoteId(UPDATED_REMOTE_ID)
            .defaultAddress(UPDATED_DEFAULT_ADDRESS)
            .country(UPDATED_COUNTRY)
            .street(UPDATED_STREET)
            .streetNo(UPDATED_STREET_NO)
            .street2(UPDATED_STREET_2)
            .postcode(UPDATED_POSTCODE)
            .city(UPDATED_CITY)
            .hideOnDocuments(UPDATED_HIDE_ON_DOCUMENTS)
            .defaultPrepage(UPDATED_DEFAULT_PREPAGE);
        ContactAddressDTO contactAddressDTO = contactAddressMapper.toDto(updatedContactAddress);

        restContactAddressMockMvc
            .perform(
                put(ENTITY_API_URL_ID, contactAddressDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(contactAddressDTO))
            )
            .andExpect(status().isOk());

        // Validate the ContactAddress in the database
        List<ContactAddress> contactAddressList = contactAddressRepository.findAll();
        assertThat(contactAddressList).hasSize(databaseSizeBeforeUpdate);
        ContactAddress testContactAddress = contactAddressList.get(contactAddressList.size() - 1);
        assertThat(testContactAddress.getRemoteId()).isEqualTo(UPDATED_REMOTE_ID);
        assertThat(testContactAddress.getDefaultAddress()).isEqualTo(UPDATED_DEFAULT_ADDRESS);
        assertThat(testContactAddress.getCountry()).isEqualTo(UPDATED_COUNTRY);
        assertThat(testContactAddress.getStreet()).isEqualTo(UPDATED_STREET);
        assertThat(testContactAddress.getStreetNo()).isEqualTo(UPDATED_STREET_NO);
        assertThat(testContactAddress.getStreet2()).isEqualTo(UPDATED_STREET_2);
        assertThat(testContactAddress.getPostcode()).isEqualTo(UPDATED_POSTCODE);
        assertThat(testContactAddress.getCity()).isEqualTo(UPDATED_CITY);
        assertThat(testContactAddress.getHideOnDocuments()).isEqualTo(UPDATED_HIDE_ON_DOCUMENTS);
        assertThat(testContactAddress.getDefaultPrepage()).isEqualTo(UPDATED_DEFAULT_PREPAGE);

        // Validate the ContactAddress in Elasticsearch
        verify(mockContactAddressSearchRepository).save(testContactAddress);
    }

    @Test
    @Transactional
    void putNonExistingContactAddress() throws Exception {
        int databaseSizeBeforeUpdate = contactAddressRepository.findAll().size();
        contactAddress.setId(count.incrementAndGet());

        // Create the ContactAddress
        ContactAddressDTO contactAddressDTO = contactAddressMapper.toDto(contactAddress);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restContactAddressMockMvc
            .perform(
                put(ENTITY_API_URL_ID, contactAddressDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(contactAddressDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the ContactAddress in the database
        List<ContactAddress> contactAddressList = contactAddressRepository.findAll();
        assertThat(contactAddressList).hasSize(databaseSizeBeforeUpdate);

        // Validate the ContactAddress in Elasticsearch
        verify(mockContactAddressSearchRepository, times(0)).save(contactAddress);
    }

    @Test
    @Transactional
    void putWithIdMismatchContactAddress() throws Exception {
        int databaseSizeBeforeUpdate = contactAddressRepository.findAll().size();
        contactAddress.setId(count.incrementAndGet());

        // Create the ContactAddress
        ContactAddressDTO contactAddressDTO = contactAddressMapper.toDto(contactAddress);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restContactAddressMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(contactAddressDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the ContactAddress in the database
        List<ContactAddress> contactAddressList = contactAddressRepository.findAll();
        assertThat(contactAddressList).hasSize(databaseSizeBeforeUpdate);

        // Validate the ContactAddress in Elasticsearch
        verify(mockContactAddressSearchRepository, times(0)).save(contactAddress);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamContactAddress() throws Exception {
        int databaseSizeBeforeUpdate = contactAddressRepository.findAll().size();
        contactAddress.setId(count.incrementAndGet());

        // Create the ContactAddress
        ContactAddressDTO contactAddressDTO = contactAddressMapper.toDto(contactAddress);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restContactAddressMockMvc
            .perform(
                put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(contactAddressDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the ContactAddress in the database
        List<ContactAddress> contactAddressList = contactAddressRepository.findAll();
        assertThat(contactAddressList).hasSize(databaseSizeBeforeUpdate);

        // Validate the ContactAddress in Elasticsearch
        verify(mockContactAddressSearchRepository, times(0)).save(contactAddress);
    }

    @Test
    @Transactional
    void partialUpdateContactAddressWithPatch() throws Exception {
        // Initialize the database
        contactAddressRepository.saveAndFlush(contactAddress);

        int databaseSizeBeforeUpdate = contactAddressRepository.findAll().size();

        // Update the contactAddress using partial update
        ContactAddress partialUpdatedContactAddress = new ContactAddress();
        partialUpdatedContactAddress.setId(contactAddress.getId());

        partialUpdatedContactAddress
            .remoteId(UPDATED_REMOTE_ID)
            .street(UPDATED_STREET)
            .street2(UPDATED_STREET_2)
            .city(UPDATED_CITY)
            .hideOnDocuments(UPDATED_HIDE_ON_DOCUMENTS)
            .defaultPrepage(UPDATED_DEFAULT_PREPAGE);

        restContactAddressMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedContactAddress.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedContactAddress))
            )
            .andExpect(status().isOk());

        // Validate the ContactAddress in the database
        List<ContactAddress> contactAddressList = contactAddressRepository.findAll();
        assertThat(contactAddressList).hasSize(databaseSizeBeforeUpdate);
        ContactAddress testContactAddress = contactAddressList.get(contactAddressList.size() - 1);
        assertThat(testContactAddress.getRemoteId()).isEqualTo(UPDATED_REMOTE_ID);
        assertThat(testContactAddress.getDefaultAddress()).isEqualTo(DEFAULT_DEFAULT_ADDRESS);
        assertThat(testContactAddress.getCountry()).isEqualTo(DEFAULT_COUNTRY);
        assertThat(testContactAddress.getStreet()).isEqualTo(UPDATED_STREET);
        assertThat(testContactAddress.getStreetNo()).isEqualTo(DEFAULT_STREET_NO);
        assertThat(testContactAddress.getStreet2()).isEqualTo(UPDATED_STREET_2);
        assertThat(testContactAddress.getPostcode()).isEqualTo(DEFAULT_POSTCODE);
        assertThat(testContactAddress.getCity()).isEqualTo(UPDATED_CITY);
        assertThat(testContactAddress.getHideOnDocuments()).isEqualTo(UPDATED_HIDE_ON_DOCUMENTS);
        assertThat(testContactAddress.getDefaultPrepage()).isEqualTo(UPDATED_DEFAULT_PREPAGE);
    }

    @Test
    @Transactional
    void fullUpdateContactAddressWithPatch() throws Exception {
        // Initialize the database
        contactAddressRepository.saveAndFlush(contactAddress);

        int databaseSizeBeforeUpdate = contactAddressRepository.findAll().size();

        // Update the contactAddress using partial update
        ContactAddress partialUpdatedContactAddress = new ContactAddress();
        partialUpdatedContactAddress.setId(contactAddress.getId());

        partialUpdatedContactAddress
            .remoteId(UPDATED_REMOTE_ID)
            .defaultAddress(UPDATED_DEFAULT_ADDRESS)
            .country(UPDATED_COUNTRY)
            .street(UPDATED_STREET)
            .streetNo(UPDATED_STREET_NO)
            .street2(UPDATED_STREET_2)
            .postcode(UPDATED_POSTCODE)
            .city(UPDATED_CITY)
            .hideOnDocuments(UPDATED_HIDE_ON_DOCUMENTS)
            .defaultPrepage(UPDATED_DEFAULT_PREPAGE);

        restContactAddressMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedContactAddress.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedContactAddress))
            )
            .andExpect(status().isOk());

        // Validate the ContactAddress in the database
        List<ContactAddress> contactAddressList = contactAddressRepository.findAll();
        assertThat(contactAddressList).hasSize(databaseSizeBeforeUpdate);
        ContactAddress testContactAddress = contactAddressList.get(contactAddressList.size() - 1);
        assertThat(testContactAddress.getRemoteId()).isEqualTo(UPDATED_REMOTE_ID);
        assertThat(testContactAddress.getDefaultAddress()).isEqualTo(UPDATED_DEFAULT_ADDRESS);
        assertThat(testContactAddress.getCountry()).isEqualTo(UPDATED_COUNTRY);
        assertThat(testContactAddress.getStreet()).isEqualTo(UPDATED_STREET);
        assertThat(testContactAddress.getStreetNo()).isEqualTo(UPDATED_STREET_NO);
        assertThat(testContactAddress.getStreet2()).isEqualTo(UPDATED_STREET_2);
        assertThat(testContactAddress.getPostcode()).isEqualTo(UPDATED_POSTCODE);
        assertThat(testContactAddress.getCity()).isEqualTo(UPDATED_CITY);
        assertThat(testContactAddress.getHideOnDocuments()).isEqualTo(UPDATED_HIDE_ON_DOCUMENTS);
        assertThat(testContactAddress.getDefaultPrepage()).isEqualTo(UPDATED_DEFAULT_PREPAGE);
    }

    @Test
    @Transactional
    void patchNonExistingContactAddress() throws Exception {
        int databaseSizeBeforeUpdate = contactAddressRepository.findAll().size();
        contactAddress.setId(count.incrementAndGet());

        // Create the ContactAddress
        ContactAddressDTO contactAddressDTO = contactAddressMapper.toDto(contactAddress);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restContactAddressMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, contactAddressDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(contactAddressDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the ContactAddress in the database
        List<ContactAddress> contactAddressList = contactAddressRepository.findAll();
        assertThat(contactAddressList).hasSize(databaseSizeBeforeUpdate);

        // Validate the ContactAddress in Elasticsearch
        verify(mockContactAddressSearchRepository, times(0)).save(contactAddress);
    }

    @Test
    @Transactional
    void patchWithIdMismatchContactAddress() throws Exception {
        int databaseSizeBeforeUpdate = contactAddressRepository.findAll().size();
        contactAddress.setId(count.incrementAndGet());

        // Create the ContactAddress
        ContactAddressDTO contactAddressDTO = contactAddressMapper.toDto(contactAddress);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restContactAddressMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(contactAddressDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the ContactAddress in the database
        List<ContactAddress> contactAddressList = contactAddressRepository.findAll();
        assertThat(contactAddressList).hasSize(databaseSizeBeforeUpdate);

        // Validate the ContactAddress in Elasticsearch
        verify(mockContactAddressSearchRepository, times(0)).save(contactAddress);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamContactAddress() throws Exception {
        int databaseSizeBeforeUpdate = contactAddressRepository.findAll().size();
        contactAddress.setId(count.incrementAndGet());

        // Create the ContactAddress
        ContactAddressDTO contactAddressDTO = contactAddressMapper.toDto(contactAddress);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restContactAddressMockMvc
            .perform(
                patch(ENTITY_API_URL)
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(contactAddressDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the ContactAddress in the database
        List<ContactAddress> contactAddressList = contactAddressRepository.findAll();
        assertThat(contactAddressList).hasSize(databaseSizeBeforeUpdate);

        // Validate the ContactAddress in Elasticsearch
        verify(mockContactAddressSearchRepository, times(0)).save(contactAddress);
    }

    @Test
    @Transactional
    void deleteContactAddress() throws Exception {
        // Initialize the database
        contactAddressRepository.saveAndFlush(contactAddress);

        int databaseSizeBeforeDelete = contactAddressRepository.findAll().size();

        // Delete the contactAddress
        restContactAddressMockMvc
            .perform(delete(ENTITY_API_URL_ID, contactAddress.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<ContactAddress> contactAddressList = contactAddressRepository.findAll();
        assertThat(contactAddressList).hasSize(databaseSizeBeforeDelete - 1);

        // Validate the ContactAddress in Elasticsearch
        verify(mockContactAddressSearchRepository, times(1)).deleteById(contactAddress.getId());
    }

    @Test
    @Transactional
    void searchContactAddress() throws Exception {
        // Configure the mock search repository
        // Initialize the database
        contactAddressRepository.saveAndFlush(contactAddress);
        when(mockContactAddressSearchRepository.search(queryStringQuery("id:" + contactAddress.getId()), PageRequest.of(0, 20)))
            .thenReturn(new PageImpl<>(Collections.singletonList(contactAddress), PageRequest.of(0, 1), 1));

        // Search the contactAddress
        restContactAddressMockMvc
            .perform(get(ENTITY_SEARCH_API_URL + "?query=id:" + contactAddress.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(contactAddress.getId().intValue())))
            .andExpect(jsonPath("$.[*].remoteId").value(hasItem(DEFAULT_REMOTE_ID)))
            .andExpect(jsonPath("$.[*].defaultAddress").value(hasItem(DEFAULT_DEFAULT_ADDRESS.booleanValue())))
            .andExpect(jsonPath("$.[*].country").value(hasItem(DEFAULT_COUNTRY)))
            .andExpect(jsonPath("$.[*].street").value(hasItem(DEFAULT_STREET)))
            .andExpect(jsonPath("$.[*].streetNo").value(hasItem(DEFAULT_STREET_NO)))
            .andExpect(jsonPath("$.[*].street2").value(hasItem(DEFAULT_STREET_2)))
            .andExpect(jsonPath("$.[*].postcode").value(hasItem(DEFAULT_POSTCODE)))
            .andExpect(jsonPath("$.[*].city").value(hasItem(DEFAULT_CITY)))
            .andExpect(jsonPath("$.[*].hideOnDocuments").value(hasItem(DEFAULT_HIDE_ON_DOCUMENTS.booleanValue())))
            .andExpect(jsonPath("$.[*].defaultPrepage").value(hasItem(DEFAULT_DEFAULT_PREPAGE.booleanValue())));
    }
}
