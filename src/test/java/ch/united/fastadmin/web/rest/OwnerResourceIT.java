package ch.united.fastadmin.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import ch.united.fastadmin.IntegrationTest;
import ch.united.fastadmin.domain.Owner;
import ch.united.fastadmin.domain.enumeration.CompanyCurrency;
import ch.united.fastadmin.domain.enumeration.CompanyLanguage;
import ch.united.fastadmin.domain.enumeration.Country;
import ch.united.fastadmin.repository.OwnerRepository;
import ch.united.fastadmin.service.dto.OwnerDTO;
import ch.united.fastadmin.service.mapper.OwnerMapper;
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
 * Integration tests for the {@link OwnerResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class OwnerResourceIT {

    private static final Integer DEFAULT_REMOTE_ID = 1;
    private static final Integer UPDATED_REMOTE_ID = 2;

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_SURNAME = "AAAAAAAAAA";
    private static final String UPDATED_SURNAME = "BBBBBBBBBB";

    private static final String DEFAULT_EMAIL = "AAAAAAAAAA";
    private static final String UPDATED_EMAIL = "BBBBBBBBBB";

    private static final CompanyLanguage DEFAULT_LANGUAGE = CompanyLanguage.FRENCH;
    private static final CompanyLanguage UPDATED_LANGUAGE = CompanyLanguage.ENGLISH;

    private static final String DEFAULT_COMPANY_NAME = "AAAAAAAAAA";
    private static final String UPDATED_COMPANY_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_COMPANY_ADDITION = "AAAAAAAAAA";
    private static final String UPDATED_COMPANY_ADDITION = "BBBBBBBBBB";

    private static final Country DEFAULT_COMPANY_COUNTRY = Country.AD;
    private static final Country UPDATED_COMPANY_COUNTRY = Country.AE;

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

    private static final CompanyCurrency DEFAULT_COMPANY_CURRENCY = CompanyCurrency.CHF;
    private static final CompanyCurrency UPDATED_COMPANY_CURRENCY = CompanyCurrency.EUR;

    private static final String ENTITY_API_URL = "/api/owners";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private OwnerRepository ownerRepository;

    @Autowired
    private OwnerMapper ownerMapper;

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
            .andExpect(jsonPath("$.[*].companyCountry").value(hasItem(DEFAULT_COMPANY_COUNTRY.toString())))
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
            .andExpect(jsonPath("$.companyCountry").value(DEFAULT_COMPANY_COUNTRY.toString()))
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
    }
}
