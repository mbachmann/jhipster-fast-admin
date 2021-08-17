package ch.united.fastadmin.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import ch.united.fastadmin.IntegrationTest;
import ch.united.fastadmin.domain.ContactAddress;
import ch.united.fastadmin.domain.enumeration.Country;
import ch.united.fastadmin.repository.ContactAddressRepository;
import ch.united.fastadmin.service.dto.ContactAddressDTO;
import ch.united.fastadmin.service.mapper.ContactAddressMapper;
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
 * Integration tests for the {@link ContactAddressResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class ContactAddressResourceIT {

    private static final Integer DEFAULT_REMOTE_ID = 1;
    private static final Integer UPDATED_REMOTE_ID = 2;

    private static final Boolean DEFAULT_DEFAULT_ADDRESS = false;
    private static final Boolean UPDATED_DEFAULT_ADDRESS = true;

    private static final Country DEFAULT_COUNTRY = Country.AD;
    private static final Country UPDATED_COUNTRY = Country.AE;

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

    private static final Boolean DEFAULT_INACTIV = false;
    private static final Boolean UPDATED_INACTIV = true;

    private static final String ENTITY_API_URL = "/api/contact-addresses";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ContactAddressRepository contactAddressRepository;

    @Autowired
    private ContactAddressMapper contactAddressMapper;

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
            .defaultPrepage(DEFAULT_DEFAULT_PREPAGE)
            .inactiv(DEFAULT_INACTIV);
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
            .defaultPrepage(UPDATED_DEFAULT_PREPAGE)
            .inactiv(UPDATED_INACTIV);
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
        assertThat(testContactAddress.getInactiv()).isEqualTo(DEFAULT_INACTIV);
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
            .andExpect(jsonPath("$.[*].country").value(hasItem(DEFAULT_COUNTRY.toString())))
            .andExpect(jsonPath("$.[*].street").value(hasItem(DEFAULT_STREET)))
            .andExpect(jsonPath("$.[*].streetNo").value(hasItem(DEFAULT_STREET_NO)))
            .andExpect(jsonPath("$.[*].street2").value(hasItem(DEFAULT_STREET_2)))
            .andExpect(jsonPath("$.[*].postcode").value(hasItem(DEFAULT_POSTCODE)))
            .andExpect(jsonPath("$.[*].city").value(hasItem(DEFAULT_CITY)))
            .andExpect(jsonPath("$.[*].hideOnDocuments").value(hasItem(DEFAULT_HIDE_ON_DOCUMENTS.booleanValue())))
            .andExpect(jsonPath("$.[*].defaultPrepage").value(hasItem(DEFAULT_DEFAULT_PREPAGE.booleanValue())))
            .andExpect(jsonPath("$.[*].inactiv").value(hasItem(DEFAULT_INACTIV.booleanValue())));
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
            .andExpect(jsonPath("$.country").value(DEFAULT_COUNTRY.toString()))
            .andExpect(jsonPath("$.street").value(DEFAULT_STREET))
            .andExpect(jsonPath("$.streetNo").value(DEFAULT_STREET_NO))
            .andExpect(jsonPath("$.street2").value(DEFAULT_STREET_2))
            .andExpect(jsonPath("$.postcode").value(DEFAULT_POSTCODE))
            .andExpect(jsonPath("$.city").value(DEFAULT_CITY))
            .andExpect(jsonPath("$.hideOnDocuments").value(DEFAULT_HIDE_ON_DOCUMENTS.booleanValue()))
            .andExpect(jsonPath("$.defaultPrepage").value(DEFAULT_DEFAULT_PREPAGE.booleanValue()))
            .andExpect(jsonPath("$.inactiv").value(DEFAULT_INACTIV.booleanValue()));
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
            .defaultPrepage(UPDATED_DEFAULT_PREPAGE)
            .inactiv(UPDATED_INACTIV);
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
        assertThat(testContactAddress.getInactiv()).isEqualTo(UPDATED_INACTIV);
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
        assertThat(testContactAddress.getInactiv()).isEqualTo(DEFAULT_INACTIV);
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
            .defaultPrepage(UPDATED_DEFAULT_PREPAGE)
            .inactiv(UPDATED_INACTIV);

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
        assertThat(testContactAddress.getInactiv()).isEqualTo(UPDATED_INACTIV);
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
    }
}
