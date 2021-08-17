package ch.united.fastadmin.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import ch.united.fastadmin.IntegrationTest;
import ch.united.fastadmin.domain.ContactAccount;
import ch.united.fastadmin.domain.enumeration.AccountType;
import ch.united.fastadmin.repository.ContactAccountRepository;
import ch.united.fastadmin.service.dto.ContactAccountDTO;
import ch.united.fastadmin.service.mapper.ContactAccountMapper;
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
 * Integration tests for the {@link ContactAccountResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class ContactAccountResourceIT {

    private static final Integer DEFAULT_REMOTE_ID = 1;
    private static final Integer UPDATED_REMOTE_ID = 2;

    private static final Boolean DEFAULT_DEFAULT_ACCOUNT = false;
    private static final Boolean UPDATED_DEFAULT_ACCOUNT = true;

    private static final AccountType DEFAULT_TYPE = AccountType.IBAN;
    private static final AccountType UPDATED_TYPE = AccountType.ISR;

    private static final String DEFAULT_NUMBER = "AAAAAAAAAA";
    private static final String UPDATED_NUMBER = "BBBBBBBBBB";

    private static final String DEFAULT_BIC = "AAAAAAAAAA";
    private static final String UPDATED_BIC = "BBBBBBBBBB";

    private static final String DEFAULT_DESCRIPTION = "AAAAAAAAAA";
    private static final String UPDATED_DESCRIPTION = "BBBBBBBBBB";

    private static final Boolean DEFAULT_INACTIV = false;
    private static final Boolean UPDATED_INACTIV = true;

    private static final String ENTITY_API_URL = "/api/contact-accounts";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ContactAccountRepository contactAccountRepository;

    @Autowired
    private ContactAccountMapper contactAccountMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restContactAccountMockMvc;

    private ContactAccount contactAccount;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static ContactAccount createEntity(EntityManager em) {
        ContactAccount contactAccount = new ContactAccount()
            .remoteId(DEFAULT_REMOTE_ID)
            .defaultAccount(DEFAULT_DEFAULT_ACCOUNT)
            .type(DEFAULT_TYPE)
            .number(DEFAULT_NUMBER)
            .bic(DEFAULT_BIC)
            .description(DEFAULT_DESCRIPTION)
            .inactiv(DEFAULT_INACTIV);
        return contactAccount;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static ContactAccount createUpdatedEntity(EntityManager em) {
        ContactAccount contactAccount = new ContactAccount()
            .remoteId(UPDATED_REMOTE_ID)
            .defaultAccount(UPDATED_DEFAULT_ACCOUNT)
            .type(UPDATED_TYPE)
            .number(UPDATED_NUMBER)
            .bic(UPDATED_BIC)
            .description(UPDATED_DESCRIPTION)
            .inactiv(UPDATED_INACTIV);
        return contactAccount;
    }

    @BeforeEach
    public void initTest() {
        contactAccount = createEntity(em);
    }

    @Test
    @Transactional
    void createContactAccount() throws Exception {
        int databaseSizeBeforeCreate = contactAccountRepository.findAll().size();
        // Create the ContactAccount
        ContactAccountDTO contactAccountDTO = contactAccountMapper.toDto(contactAccount);
        restContactAccountMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(contactAccountDTO))
            )
            .andExpect(status().isCreated());

        // Validate the ContactAccount in the database
        List<ContactAccount> contactAccountList = contactAccountRepository.findAll();
        assertThat(contactAccountList).hasSize(databaseSizeBeforeCreate + 1);
        ContactAccount testContactAccount = contactAccountList.get(contactAccountList.size() - 1);
        assertThat(testContactAccount.getRemoteId()).isEqualTo(DEFAULT_REMOTE_ID);
        assertThat(testContactAccount.getDefaultAccount()).isEqualTo(DEFAULT_DEFAULT_ACCOUNT);
        assertThat(testContactAccount.getType()).isEqualTo(DEFAULT_TYPE);
        assertThat(testContactAccount.getNumber()).isEqualTo(DEFAULT_NUMBER);
        assertThat(testContactAccount.getBic()).isEqualTo(DEFAULT_BIC);
        assertThat(testContactAccount.getDescription()).isEqualTo(DEFAULT_DESCRIPTION);
        assertThat(testContactAccount.getInactiv()).isEqualTo(DEFAULT_INACTIV);
    }

    @Test
    @Transactional
    void createContactAccountWithExistingId() throws Exception {
        // Create the ContactAccount with an existing ID
        contactAccount.setId(1L);
        ContactAccountDTO contactAccountDTO = contactAccountMapper.toDto(contactAccount);

        int databaseSizeBeforeCreate = contactAccountRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restContactAccountMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(contactAccountDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the ContactAccount in the database
        List<ContactAccount> contactAccountList = contactAccountRepository.findAll();
        assertThat(contactAccountList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllContactAccounts() throws Exception {
        // Initialize the database
        contactAccountRepository.saveAndFlush(contactAccount);

        // Get all the contactAccountList
        restContactAccountMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(contactAccount.getId().intValue())))
            .andExpect(jsonPath("$.[*].remoteId").value(hasItem(DEFAULT_REMOTE_ID)))
            .andExpect(jsonPath("$.[*].defaultAccount").value(hasItem(DEFAULT_DEFAULT_ACCOUNT.booleanValue())))
            .andExpect(jsonPath("$.[*].type").value(hasItem(DEFAULT_TYPE.toString())))
            .andExpect(jsonPath("$.[*].number").value(hasItem(DEFAULT_NUMBER)))
            .andExpect(jsonPath("$.[*].bic").value(hasItem(DEFAULT_BIC)))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION)))
            .andExpect(jsonPath("$.[*].inactiv").value(hasItem(DEFAULT_INACTIV.booleanValue())));
    }

    @Test
    @Transactional
    void getContactAccount() throws Exception {
        // Initialize the database
        contactAccountRepository.saveAndFlush(contactAccount);

        // Get the contactAccount
        restContactAccountMockMvc
            .perform(get(ENTITY_API_URL_ID, contactAccount.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(contactAccount.getId().intValue()))
            .andExpect(jsonPath("$.remoteId").value(DEFAULT_REMOTE_ID))
            .andExpect(jsonPath("$.defaultAccount").value(DEFAULT_DEFAULT_ACCOUNT.booleanValue()))
            .andExpect(jsonPath("$.type").value(DEFAULT_TYPE.toString()))
            .andExpect(jsonPath("$.number").value(DEFAULT_NUMBER))
            .andExpect(jsonPath("$.bic").value(DEFAULT_BIC))
            .andExpect(jsonPath("$.description").value(DEFAULT_DESCRIPTION))
            .andExpect(jsonPath("$.inactiv").value(DEFAULT_INACTIV.booleanValue()));
    }

    @Test
    @Transactional
    void getNonExistingContactAccount() throws Exception {
        // Get the contactAccount
        restContactAccountMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewContactAccount() throws Exception {
        // Initialize the database
        contactAccountRepository.saveAndFlush(contactAccount);

        int databaseSizeBeforeUpdate = contactAccountRepository.findAll().size();

        // Update the contactAccount
        ContactAccount updatedContactAccount = contactAccountRepository.findById(contactAccount.getId()).get();
        // Disconnect from session so that the updates on updatedContactAccount are not directly saved in db
        em.detach(updatedContactAccount);
        updatedContactAccount
            .remoteId(UPDATED_REMOTE_ID)
            .defaultAccount(UPDATED_DEFAULT_ACCOUNT)
            .type(UPDATED_TYPE)
            .number(UPDATED_NUMBER)
            .bic(UPDATED_BIC)
            .description(UPDATED_DESCRIPTION)
            .inactiv(UPDATED_INACTIV);
        ContactAccountDTO contactAccountDTO = contactAccountMapper.toDto(updatedContactAccount);

        restContactAccountMockMvc
            .perform(
                put(ENTITY_API_URL_ID, contactAccountDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(contactAccountDTO))
            )
            .andExpect(status().isOk());

        // Validate the ContactAccount in the database
        List<ContactAccount> contactAccountList = contactAccountRepository.findAll();
        assertThat(contactAccountList).hasSize(databaseSizeBeforeUpdate);
        ContactAccount testContactAccount = contactAccountList.get(contactAccountList.size() - 1);
        assertThat(testContactAccount.getRemoteId()).isEqualTo(UPDATED_REMOTE_ID);
        assertThat(testContactAccount.getDefaultAccount()).isEqualTo(UPDATED_DEFAULT_ACCOUNT);
        assertThat(testContactAccount.getType()).isEqualTo(UPDATED_TYPE);
        assertThat(testContactAccount.getNumber()).isEqualTo(UPDATED_NUMBER);
        assertThat(testContactAccount.getBic()).isEqualTo(UPDATED_BIC);
        assertThat(testContactAccount.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
        assertThat(testContactAccount.getInactiv()).isEqualTo(UPDATED_INACTIV);
    }

    @Test
    @Transactional
    void putNonExistingContactAccount() throws Exception {
        int databaseSizeBeforeUpdate = contactAccountRepository.findAll().size();
        contactAccount.setId(count.incrementAndGet());

        // Create the ContactAccount
        ContactAccountDTO contactAccountDTO = contactAccountMapper.toDto(contactAccount);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restContactAccountMockMvc
            .perform(
                put(ENTITY_API_URL_ID, contactAccountDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(contactAccountDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the ContactAccount in the database
        List<ContactAccount> contactAccountList = contactAccountRepository.findAll();
        assertThat(contactAccountList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchContactAccount() throws Exception {
        int databaseSizeBeforeUpdate = contactAccountRepository.findAll().size();
        contactAccount.setId(count.incrementAndGet());

        // Create the ContactAccount
        ContactAccountDTO contactAccountDTO = contactAccountMapper.toDto(contactAccount);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restContactAccountMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(contactAccountDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the ContactAccount in the database
        List<ContactAccount> contactAccountList = contactAccountRepository.findAll();
        assertThat(contactAccountList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamContactAccount() throws Exception {
        int databaseSizeBeforeUpdate = contactAccountRepository.findAll().size();
        contactAccount.setId(count.incrementAndGet());

        // Create the ContactAccount
        ContactAccountDTO contactAccountDTO = contactAccountMapper.toDto(contactAccount);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restContactAccountMockMvc
            .perform(
                put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(contactAccountDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the ContactAccount in the database
        List<ContactAccount> contactAccountList = contactAccountRepository.findAll();
        assertThat(contactAccountList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateContactAccountWithPatch() throws Exception {
        // Initialize the database
        contactAccountRepository.saveAndFlush(contactAccount);

        int databaseSizeBeforeUpdate = contactAccountRepository.findAll().size();

        // Update the contactAccount using partial update
        ContactAccount partialUpdatedContactAccount = new ContactAccount();
        partialUpdatedContactAccount.setId(contactAccount.getId());

        partialUpdatedContactAccount.remoteId(UPDATED_REMOTE_ID).defaultAccount(UPDATED_DEFAULT_ACCOUNT);

        restContactAccountMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedContactAccount.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedContactAccount))
            )
            .andExpect(status().isOk());

        // Validate the ContactAccount in the database
        List<ContactAccount> contactAccountList = contactAccountRepository.findAll();
        assertThat(contactAccountList).hasSize(databaseSizeBeforeUpdate);
        ContactAccount testContactAccount = contactAccountList.get(contactAccountList.size() - 1);
        assertThat(testContactAccount.getRemoteId()).isEqualTo(UPDATED_REMOTE_ID);
        assertThat(testContactAccount.getDefaultAccount()).isEqualTo(UPDATED_DEFAULT_ACCOUNT);
        assertThat(testContactAccount.getType()).isEqualTo(DEFAULT_TYPE);
        assertThat(testContactAccount.getNumber()).isEqualTo(DEFAULT_NUMBER);
        assertThat(testContactAccount.getBic()).isEqualTo(DEFAULT_BIC);
        assertThat(testContactAccount.getDescription()).isEqualTo(DEFAULT_DESCRIPTION);
        assertThat(testContactAccount.getInactiv()).isEqualTo(DEFAULT_INACTIV);
    }

    @Test
    @Transactional
    void fullUpdateContactAccountWithPatch() throws Exception {
        // Initialize the database
        contactAccountRepository.saveAndFlush(contactAccount);

        int databaseSizeBeforeUpdate = contactAccountRepository.findAll().size();

        // Update the contactAccount using partial update
        ContactAccount partialUpdatedContactAccount = new ContactAccount();
        partialUpdatedContactAccount.setId(contactAccount.getId());

        partialUpdatedContactAccount
            .remoteId(UPDATED_REMOTE_ID)
            .defaultAccount(UPDATED_DEFAULT_ACCOUNT)
            .type(UPDATED_TYPE)
            .number(UPDATED_NUMBER)
            .bic(UPDATED_BIC)
            .description(UPDATED_DESCRIPTION)
            .inactiv(UPDATED_INACTIV);

        restContactAccountMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedContactAccount.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedContactAccount))
            )
            .andExpect(status().isOk());

        // Validate the ContactAccount in the database
        List<ContactAccount> contactAccountList = contactAccountRepository.findAll();
        assertThat(contactAccountList).hasSize(databaseSizeBeforeUpdate);
        ContactAccount testContactAccount = contactAccountList.get(contactAccountList.size() - 1);
        assertThat(testContactAccount.getRemoteId()).isEqualTo(UPDATED_REMOTE_ID);
        assertThat(testContactAccount.getDefaultAccount()).isEqualTo(UPDATED_DEFAULT_ACCOUNT);
        assertThat(testContactAccount.getType()).isEqualTo(UPDATED_TYPE);
        assertThat(testContactAccount.getNumber()).isEqualTo(UPDATED_NUMBER);
        assertThat(testContactAccount.getBic()).isEqualTo(UPDATED_BIC);
        assertThat(testContactAccount.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
        assertThat(testContactAccount.getInactiv()).isEqualTo(UPDATED_INACTIV);
    }

    @Test
    @Transactional
    void patchNonExistingContactAccount() throws Exception {
        int databaseSizeBeforeUpdate = contactAccountRepository.findAll().size();
        contactAccount.setId(count.incrementAndGet());

        // Create the ContactAccount
        ContactAccountDTO contactAccountDTO = contactAccountMapper.toDto(contactAccount);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restContactAccountMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, contactAccountDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(contactAccountDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the ContactAccount in the database
        List<ContactAccount> contactAccountList = contactAccountRepository.findAll();
        assertThat(contactAccountList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchContactAccount() throws Exception {
        int databaseSizeBeforeUpdate = contactAccountRepository.findAll().size();
        contactAccount.setId(count.incrementAndGet());

        // Create the ContactAccount
        ContactAccountDTO contactAccountDTO = contactAccountMapper.toDto(contactAccount);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restContactAccountMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(contactAccountDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the ContactAccount in the database
        List<ContactAccount> contactAccountList = contactAccountRepository.findAll();
        assertThat(contactAccountList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamContactAccount() throws Exception {
        int databaseSizeBeforeUpdate = contactAccountRepository.findAll().size();
        contactAccount.setId(count.incrementAndGet());

        // Create the ContactAccount
        ContactAccountDTO contactAccountDTO = contactAccountMapper.toDto(contactAccount);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restContactAccountMockMvc
            .perform(
                patch(ENTITY_API_URL)
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(contactAccountDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the ContactAccount in the database
        List<ContactAccount> contactAccountList = contactAccountRepository.findAll();
        assertThat(contactAccountList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteContactAccount() throws Exception {
        // Initialize the database
        contactAccountRepository.saveAndFlush(contactAccount);

        int databaseSizeBeforeDelete = contactAccountRepository.findAll().size();

        // Delete the contactAccount
        restContactAccountMockMvc
            .perform(delete(ENTITY_API_URL_ID, contactAccount.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<ContactAccount> contactAccountList = contactAccountRepository.findAll();
        assertThat(contactAccountList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
