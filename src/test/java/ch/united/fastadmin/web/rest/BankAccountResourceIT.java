package ch.united.fastadmin.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import ch.united.fastadmin.IntegrationTest;
import ch.united.fastadmin.domain.BankAccount;
import ch.united.fastadmin.domain.enumeration.AutoSynch;
import ch.united.fastadmin.domain.enumeration.AutoSynchDirection;
import ch.united.fastadmin.domain.enumeration.Currency;
import ch.united.fastadmin.repository.BankAccountRepository;
import ch.united.fastadmin.service.dto.BankAccountDTO;
import ch.united.fastadmin.service.mapper.BankAccountMapper;
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
 * Integration tests for the {@link BankAccountResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class BankAccountResourceIT {

    private static final Integer DEFAULT_REMOTE_ID = 1;
    private static final Integer UPDATED_REMOTE_ID = 2;

    private static final Boolean DEFAULT_DEFAULT_BANK_ACCOUNT = false;
    private static final Boolean UPDATED_DEFAULT_BANK_ACCOUNT = true;

    private static final String DEFAULT_DESCRIPTION = "AAAAAAAAAA";
    private static final String UPDATED_DESCRIPTION = "BBBBBBBBBB";

    private static final String DEFAULT_BANK_NAME = "AAAAAAAAAA";
    private static final String UPDATED_BANK_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_NUMBER = "AAAAAAAAAA";
    private static final String UPDATED_NUMBER = "BBBBBBBBBB";

    private static final String DEFAULT_IBAN = "AAAAAAAAAA";
    private static final String UPDATED_IBAN = "BBBBBBBBBB";

    private static final String DEFAULT_BIC = "AAAAAAAAAA";
    private static final String UPDATED_BIC = "BBBBBBBBBB";

    private static final String DEFAULT_POST_ACCOUNT = "AAAAAAAAAA";
    private static final String UPDATED_POST_ACCOUNT = "BBBBBBBBBB";

    private static final AutoSynch DEFAULT_AUTO_SYNC = AutoSynch.ACTIVE;
    private static final AutoSynch UPDATED_AUTO_SYNC = AutoSynch.INACTIVE;

    private static final AutoSynchDirection DEFAULT_AUTO_SYNC_DIRECTION = AutoSynchDirection.IN;
    private static final AutoSynchDirection UPDATED_AUTO_SYNC_DIRECTION = AutoSynchDirection.OUT;

    private static final Currency DEFAULT_CURRENCY = Currency.AED;
    private static final Currency UPDATED_CURRENCY = Currency.AFN;

    private static final Boolean DEFAULT_INACTIV = false;
    private static final Boolean UPDATED_INACTIV = true;

    private static final String ENTITY_API_URL = "/api/bank-accounts";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private BankAccountRepository bankAccountRepository;

    @Autowired
    private BankAccountMapper bankAccountMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restBankAccountMockMvc;

    private BankAccount bankAccount;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static BankAccount createEntity(EntityManager em) {
        BankAccount bankAccount = new BankAccount()
            .remoteId(DEFAULT_REMOTE_ID)
            .defaultBankAccount(DEFAULT_DEFAULT_BANK_ACCOUNT)
            .description(DEFAULT_DESCRIPTION)
            .bankName(DEFAULT_BANK_NAME)
            .number(DEFAULT_NUMBER)
            .iban(DEFAULT_IBAN)
            .bic(DEFAULT_BIC)
            .postAccount(DEFAULT_POST_ACCOUNT)
            .autoSync(DEFAULT_AUTO_SYNC)
            .autoSyncDirection(DEFAULT_AUTO_SYNC_DIRECTION)
            .currency(DEFAULT_CURRENCY)
            .inactiv(DEFAULT_INACTIV);
        return bankAccount;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static BankAccount createUpdatedEntity(EntityManager em) {
        BankAccount bankAccount = new BankAccount()
            .remoteId(UPDATED_REMOTE_ID)
            .defaultBankAccount(UPDATED_DEFAULT_BANK_ACCOUNT)
            .description(UPDATED_DESCRIPTION)
            .bankName(UPDATED_BANK_NAME)
            .number(UPDATED_NUMBER)
            .iban(UPDATED_IBAN)
            .bic(UPDATED_BIC)
            .postAccount(UPDATED_POST_ACCOUNT)
            .autoSync(UPDATED_AUTO_SYNC)
            .autoSyncDirection(UPDATED_AUTO_SYNC_DIRECTION)
            .currency(UPDATED_CURRENCY)
            .inactiv(UPDATED_INACTIV);
        return bankAccount;
    }

    @BeforeEach
    public void initTest() {
        bankAccount = createEntity(em);
    }

    @Test
    @Transactional
    void createBankAccount() throws Exception {
        int databaseSizeBeforeCreate = bankAccountRepository.findAll().size();
        // Create the BankAccount
        BankAccountDTO bankAccountDTO = bankAccountMapper.toDto(bankAccount);
        restBankAccountMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(bankAccountDTO))
            )
            .andExpect(status().isCreated());

        // Validate the BankAccount in the database
        List<BankAccount> bankAccountList = bankAccountRepository.findAll();
        assertThat(bankAccountList).hasSize(databaseSizeBeforeCreate + 1);
        BankAccount testBankAccount = bankAccountList.get(bankAccountList.size() - 1);
        assertThat(testBankAccount.getRemoteId()).isEqualTo(DEFAULT_REMOTE_ID);
        assertThat(testBankAccount.getDefaultBankAccount()).isEqualTo(DEFAULT_DEFAULT_BANK_ACCOUNT);
        assertThat(testBankAccount.getDescription()).isEqualTo(DEFAULT_DESCRIPTION);
        assertThat(testBankAccount.getBankName()).isEqualTo(DEFAULT_BANK_NAME);
        assertThat(testBankAccount.getNumber()).isEqualTo(DEFAULT_NUMBER);
        assertThat(testBankAccount.getIban()).isEqualTo(DEFAULT_IBAN);
        assertThat(testBankAccount.getBic()).isEqualTo(DEFAULT_BIC);
        assertThat(testBankAccount.getPostAccount()).isEqualTo(DEFAULT_POST_ACCOUNT);
        assertThat(testBankAccount.getAutoSync()).isEqualTo(DEFAULT_AUTO_SYNC);
        assertThat(testBankAccount.getAutoSyncDirection()).isEqualTo(DEFAULT_AUTO_SYNC_DIRECTION);
        assertThat(testBankAccount.getCurrency()).isEqualTo(DEFAULT_CURRENCY);
        assertThat(testBankAccount.getInactiv()).isEqualTo(DEFAULT_INACTIV);
    }

    @Test
    @Transactional
    void createBankAccountWithExistingId() throws Exception {
        // Create the BankAccount with an existing ID
        bankAccount.setId(1L);
        BankAccountDTO bankAccountDTO = bankAccountMapper.toDto(bankAccount);

        int databaseSizeBeforeCreate = bankAccountRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restBankAccountMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(bankAccountDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the BankAccount in the database
        List<BankAccount> bankAccountList = bankAccountRepository.findAll();
        assertThat(bankAccountList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllBankAccounts() throws Exception {
        // Initialize the database
        bankAccountRepository.saveAndFlush(bankAccount);

        // Get all the bankAccountList
        restBankAccountMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(bankAccount.getId().intValue())))
            .andExpect(jsonPath("$.[*].remoteId").value(hasItem(DEFAULT_REMOTE_ID)))
            .andExpect(jsonPath("$.[*].defaultBankAccount").value(hasItem(DEFAULT_DEFAULT_BANK_ACCOUNT.booleanValue())))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION)))
            .andExpect(jsonPath("$.[*].bankName").value(hasItem(DEFAULT_BANK_NAME)))
            .andExpect(jsonPath("$.[*].number").value(hasItem(DEFAULT_NUMBER)))
            .andExpect(jsonPath("$.[*].iban").value(hasItem(DEFAULT_IBAN)))
            .andExpect(jsonPath("$.[*].bic").value(hasItem(DEFAULT_BIC)))
            .andExpect(jsonPath("$.[*].postAccount").value(hasItem(DEFAULT_POST_ACCOUNT)))
            .andExpect(jsonPath("$.[*].autoSync").value(hasItem(DEFAULT_AUTO_SYNC.toString())))
            .andExpect(jsonPath("$.[*].autoSyncDirection").value(hasItem(DEFAULT_AUTO_SYNC_DIRECTION.toString())))
            .andExpect(jsonPath("$.[*].currency").value(hasItem(DEFAULT_CURRENCY.toString())))
            .andExpect(jsonPath("$.[*].inactiv").value(hasItem(DEFAULT_INACTIV.booleanValue())));
    }

    @Test
    @Transactional
    void getBankAccount() throws Exception {
        // Initialize the database
        bankAccountRepository.saveAndFlush(bankAccount);

        // Get the bankAccount
        restBankAccountMockMvc
            .perform(get(ENTITY_API_URL_ID, bankAccount.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(bankAccount.getId().intValue()))
            .andExpect(jsonPath("$.remoteId").value(DEFAULT_REMOTE_ID))
            .andExpect(jsonPath("$.defaultBankAccount").value(DEFAULT_DEFAULT_BANK_ACCOUNT.booleanValue()))
            .andExpect(jsonPath("$.description").value(DEFAULT_DESCRIPTION))
            .andExpect(jsonPath("$.bankName").value(DEFAULT_BANK_NAME))
            .andExpect(jsonPath("$.number").value(DEFAULT_NUMBER))
            .andExpect(jsonPath("$.iban").value(DEFAULT_IBAN))
            .andExpect(jsonPath("$.bic").value(DEFAULT_BIC))
            .andExpect(jsonPath("$.postAccount").value(DEFAULT_POST_ACCOUNT))
            .andExpect(jsonPath("$.autoSync").value(DEFAULT_AUTO_SYNC.toString()))
            .andExpect(jsonPath("$.autoSyncDirection").value(DEFAULT_AUTO_SYNC_DIRECTION.toString()))
            .andExpect(jsonPath("$.currency").value(DEFAULT_CURRENCY.toString()))
            .andExpect(jsonPath("$.inactiv").value(DEFAULT_INACTIV.booleanValue()));
    }

    @Test
    @Transactional
    void getNonExistingBankAccount() throws Exception {
        // Get the bankAccount
        restBankAccountMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewBankAccount() throws Exception {
        // Initialize the database
        bankAccountRepository.saveAndFlush(bankAccount);

        int databaseSizeBeforeUpdate = bankAccountRepository.findAll().size();

        // Update the bankAccount
        BankAccount updatedBankAccount = bankAccountRepository.findById(bankAccount.getId()).get();
        // Disconnect from session so that the updates on updatedBankAccount are not directly saved in db
        em.detach(updatedBankAccount);
        updatedBankAccount
            .remoteId(UPDATED_REMOTE_ID)
            .defaultBankAccount(UPDATED_DEFAULT_BANK_ACCOUNT)
            .description(UPDATED_DESCRIPTION)
            .bankName(UPDATED_BANK_NAME)
            .number(UPDATED_NUMBER)
            .iban(UPDATED_IBAN)
            .bic(UPDATED_BIC)
            .postAccount(UPDATED_POST_ACCOUNT)
            .autoSync(UPDATED_AUTO_SYNC)
            .autoSyncDirection(UPDATED_AUTO_SYNC_DIRECTION)
            .currency(UPDATED_CURRENCY)
            .inactiv(UPDATED_INACTIV);
        BankAccountDTO bankAccountDTO = bankAccountMapper.toDto(updatedBankAccount);

        restBankAccountMockMvc
            .perform(
                put(ENTITY_API_URL_ID, bankAccountDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(bankAccountDTO))
            )
            .andExpect(status().isOk());

        // Validate the BankAccount in the database
        List<BankAccount> bankAccountList = bankAccountRepository.findAll();
        assertThat(bankAccountList).hasSize(databaseSizeBeforeUpdate);
        BankAccount testBankAccount = bankAccountList.get(bankAccountList.size() - 1);
        assertThat(testBankAccount.getRemoteId()).isEqualTo(UPDATED_REMOTE_ID);
        assertThat(testBankAccount.getDefaultBankAccount()).isEqualTo(UPDATED_DEFAULT_BANK_ACCOUNT);
        assertThat(testBankAccount.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
        assertThat(testBankAccount.getBankName()).isEqualTo(UPDATED_BANK_NAME);
        assertThat(testBankAccount.getNumber()).isEqualTo(UPDATED_NUMBER);
        assertThat(testBankAccount.getIban()).isEqualTo(UPDATED_IBAN);
        assertThat(testBankAccount.getBic()).isEqualTo(UPDATED_BIC);
        assertThat(testBankAccount.getPostAccount()).isEqualTo(UPDATED_POST_ACCOUNT);
        assertThat(testBankAccount.getAutoSync()).isEqualTo(UPDATED_AUTO_SYNC);
        assertThat(testBankAccount.getAutoSyncDirection()).isEqualTo(UPDATED_AUTO_SYNC_DIRECTION);
        assertThat(testBankAccount.getCurrency()).isEqualTo(UPDATED_CURRENCY);
        assertThat(testBankAccount.getInactiv()).isEqualTo(UPDATED_INACTIV);
    }

    @Test
    @Transactional
    void putNonExistingBankAccount() throws Exception {
        int databaseSizeBeforeUpdate = bankAccountRepository.findAll().size();
        bankAccount.setId(count.incrementAndGet());

        // Create the BankAccount
        BankAccountDTO bankAccountDTO = bankAccountMapper.toDto(bankAccount);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restBankAccountMockMvc
            .perform(
                put(ENTITY_API_URL_ID, bankAccountDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(bankAccountDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the BankAccount in the database
        List<BankAccount> bankAccountList = bankAccountRepository.findAll();
        assertThat(bankAccountList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchBankAccount() throws Exception {
        int databaseSizeBeforeUpdate = bankAccountRepository.findAll().size();
        bankAccount.setId(count.incrementAndGet());

        // Create the BankAccount
        BankAccountDTO bankAccountDTO = bankAccountMapper.toDto(bankAccount);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restBankAccountMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(bankAccountDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the BankAccount in the database
        List<BankAccount> bankAccountList = bankAccountRepository.findAll();
        assertThat(bankAccountList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamBankAccount() throws Exception {
        int databaseSizeBeforeUpdate = bankAccountRepository.findAll().size();
        bankAccount.setId(count.incrementAndGet());

        // Create the BankAccount
        BankAccountDTO bankAccountDTO = bankAccountMapper.toDto(bankAccount);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restBankAccountMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(bankAccountDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the BankAccount in the database
        List<BankAccount> bankAccountList = bankAccountRepository.findAll();
        assertThat(bankAccountList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateBankAccountWithPatch() throws Exception {
        // Initialize the database
        bankAccountRepository.saveAndFlush(bankAccount);

        int databaseSizeBeforeUpdate = bankAccountRepository.findAll().size();

        // Update the bankAccount using partial update
        BankAccount partialUpdatedBankAccount = new BankAccount();
        partialUpdatedBankAccount.setId(bankAccount.getId());

        partialUpdatedBankAccount
            .remoteId(UPDATED_REMOTE_ID)
            .defaultBankAccount(UPDATED_DEFAULT_BANK_ACCOUNT)
            .bankName(UPDATED_BANK_NAME)
            .iban(UPDATED_IBAN)
            .postAccount(UPDATED_POST_ACCOUNT)
            .currency(UPDATED_CURRENCY);

        restBankAccountMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedBankAccount.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedBankAccount))
            )
            .andExpect(status().isOk());

        // Validate the BankAccount in the database
        List<BankAccount> bankAccountList = bankAccountRepository.findAll();
        assertThat(bankAccountList).hasSize(databaseSizeBeforeUpdate);
        BankAccount testBankAccount = bankAccountList.get(bankAccountList.size() - 1);
        assertThat(testBankAccount.getRemoteId()).isEqualTo(UPDATED_REMOTE_ID);
        assertThat(testBankAccount.getDefaultBankAccount()).isEqualTo(UPDATED_DEFAULT_BANK_ACCOUNT);
        assertThat(testBankAccount.getDescription()).isEqualTo(DEFAULT_DESCRIPTION);
        assertThat(testBankAccount.getBankName()).isEqualTo(UPDATED_BANK_NAME);
        assertThat(testBankAccount.getNumber()).isEqualTo(DEFAULT_NUMBER);
        assertThat(testBankAccount.getIban()).isEqualTo(UPDATED_IBAN);
        assertThat(testBankAccount.getBic()).isEqualTo(DEFAULT_BIC);
        assertThat(testBankAccount.getPostAccount()).isEqualTo(UPDATED_POST_ACCOUNT);
        assertThat(testBankAccount.getAutoSync()).isEqualTo(DEFAULT_AUTO_SYNC);
        assertThat(testBankAccount.getAutoSyncDirection()).isEqualTo(DEFAULT_AUTO_SYNC_DIRECTION);
        assertThat(testBankAccount.getCurrency()).isEqualTo(UPDATED_CURRENCY);
        assertThat(testBankAccount.getInactiv()).isEqualTo(DEFAULT_INACTIV);
    }

    @Test
    @Transactional
    void fullUpdateBankAccountWithPatch() throws Exception {
        // Initialize the database
        bankAccountRepository.saveAndFlush(bankAccount);

        int databaseSizeBeforeUpdate = bankAccountRepository.findAll().size();

        // Update the bankAccount using partial update
        BankAccount partialUpdatedBankAccount = new BankAccount();
        partialUpdatedBankAccount.setId(bankAccount.getId());

        partialUpdatedBankAccount
            .remoteId(UPDATED_REMOTE_ID)
            .defaultBankAccount(UPDATED_DEFAULT_BANK_ACCOUNT)
            .description(UPDATED_DESCRIPTION)
            .bankName(UPDATED_BANK_NAME)
            .number(UPDATED_NUMBER)
            .iban(UPDATED_IBAN)
            .bic(UPDATED_BIC)
            .postAccount(UPDATED_POST_ACCOUNT)
            .autoSync(UPDATED_AUTO_SYNC)
            .autoSyncDirection(UPDATED_AUTO_SYNC_DIRECTION)
            .currency(UPDATED_CURRENCY)
            .inactiv(UPDATED_INACTIV);

        restBankAccountMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedBankAccount.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedBankAccount))
            )
            .andExpect(status().isOk());

        // Validate the BankAccount in the database
        List<BankAccount> bankAccountList = bankAccountRepository.findAll();
        assertThat(bankAccountList).hasSize(databaseSizeBeforeUpdate);
        BankAccount testBankAccount = bankAccountList.get(bankAccountList.size() - 1);
        assertThat(testBankAccount.getRemoteId()).isEqualTo(UPDATED_REMOTE_ID);
        assertThat(testBankAccount.getDefaultBankAccount()).isEqualTo(UPDATED_DEFAULT_BANK_ACCOUNT);
        assertThat(testBankAccount.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
        assertThat(testBankAccount.getBankName()).isEqualTo(UPDATED_BANK_NAME);
        assertThat(testBankAccount.getNumber()).isEqualTo(UPDATED_NUMBER);
        assertThat(testBankAccount.getIban()).isEqualTo(UPDATED_IBAN);
        assertThat(testBankAccount.getBic()).isEqualTo(UPDATED_BIC);
        assertThat(testBankAccount.getPostAccount()).isEqualTo(UPDATED_POST_ACCOUNT);
        assertThat(testBankAccount.getAutoSync()).isEqualTo(UPDATED_AUTO_SYNC);
        assertThat(testBankAccount.getAutoSyncDirection()).isEqualTo(UPDATED_AUTO_SYNC_DIRECTION);
        assertThat(testBankAccount.getCurrency()).isEqualTo(UPDATED_CURRENCY);
        assertThat(testBankAccount.getInactiv()).isEqualTo(UPDATED_INACTIV);
    }

    @Test
    @Transactional
    void patchNonExistingBankAccount() throws Exception {
        int databaseSizeBeforeUpdate = bankAccountRepository.findAll().size();
        bankAccount.setId(count.incrementAndGet());

        // Create the BankAccount
        BankAccountDTO bankAccountDTO = bankAccountMapper.toDto(bankAccount);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restBankAccountMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, bankAccountDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(bankAccountDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the BankAccount in the database
        List<BankAccount> bankAccountList = bankAccountRepository.findAll();
        assertThat(bankAccountList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchBankAccount() throws Exception {
        int databaseSizeBeforeUpdate = bankAccountRepository.findAll().size();
        bankAccount.setId(count.incrementAndGet());

        // Create the BankAccount
        BankAccountDTO bankAccountDTO = bankAccountMapper.toDto(bankAccount);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restBankAccountMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(bankAccountDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the BankAccount in the database
        List<BankAccount> bankAccountList = bankAccountRepository.findAll();
        assertThat(bankAccountList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamBankAccount() throws Exception {
        int databaseSizeBeforeUpdate = bankAccountRepository.findAll().size();
        bankAccount.setId(count.incrementAndGet());

        // Create the BankAccount
        BankAccountDTO bankAccountDTO = bankAccountMapper.toDto(bankAccount);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restBankAccountMockMvc
            .perform(
                patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(bankAccountDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the BankAccount in the database
        List<BankAccount> bankAccountList = bankAccountRepository.findAll();
        assertThat(bankAccountList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteBankAccount() throws Exception {
        // Initialize the database
        bankAccountRepository.saveAndFlush(bankAccount);

        int databaseSizeBeforeDelete = bankAccountRepository.findAll().size();

        // Delete the bankAccount
        restBankAccountMockMvc
            .perform(delete(ENTITY_API_URL_ID, bankAccount.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<BankAccount> bankAccountList = bankAccountRepository.findAll();
        assertThat(bankAccountList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
