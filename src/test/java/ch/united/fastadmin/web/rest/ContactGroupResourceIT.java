package ch.united.fastadmin.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import ch.united.fastadmin.IntegrationTest;
import ch.united.fastadmin.domain.ContactGroup;
import ch.united.fastadmin.repository.ContactGroupRepository;
import ch.united.fastadmin.service.dto.ContactGroupDTO;
import ch.united.fastadmin.service.mapper.ContactGroupMapper;
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
 * Integration tests for the {@link ContactGroupResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class ContactGroupResourceIT {

    private static final Integer DEFAULT_REMOTE_ID = 1;
    private static final Integer UPDATED_REMOTE_ID = 2;

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    private static final Integer DEFAULT_USAGE = 1;
    private static final Integer UPDATED_USAGE = 2;

    private static final String ENTITY_API_URL = "/api/contact-groups";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ContactGroupRepository contactGroupRepository;

    @Autowired
    private ContactGroupMapper contactGroupMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restContactGroupMockMvc;

    private ContactGroup contactGroup;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static ContactGroup createEntity(EntityManager em) {
        ContactGroup contactGroup = new ContactGroup().remoteId(DEFAULT_REMOTE_ID).name(DEFAULT_NAME).usage(DEFAULT_USAGE);
        return contactGroup;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static ContactGroup createUpdatedEntity(EntityManager em) {
        ContactGroup contactGroup = new ContactGroup().remoteId(UPDATED_REMOTE_ID).name(UPDATED_NAME).usage(UPDATED_USAGE);
        return contactGroup;
    }

    @BeforeEach
    public void initTest() {
        contactGroup = createEntity(em);
    }

    @Test
    @Transactional
    void createContactGroup() throws Exception {
        int databaseSizeBeforeCreate = contactGroupRepository.findAll().size();
        // Create the ContactGroup
        ContactGroupDTO contactGroupDTO = contactGroupMapper.toDto(contactGroup);
        restContactGroupMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(contactGroupDTO))
            )
            .andExpect(status().isCreated());

        // Validate the ContactGroup in the database
        List<ContactGroup> contactGroupList = contactGroupRepository.findAll();
        assertThat(contactGroupList).hasSize(databaseSizeBeforeCreate + 1);
        ContactGroup testContactGroup = contactGroupList.get(contactGroupList.size() - 1);
        assertThat(testContactGroup.getRemoteId()).isEqualTo(DEFAULT_REMOTE_ID);
        assertThat(testContactGroup.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testContactGroup.getUsage()).isEqualTo(DEFAULT_USAGE);
    }

    @Test
    @Transactional
    void createContactGroupWithExistingId() throws Exception {
        // Create the ContactGroup with an existing ID
        contactGroup.setId(1L);
        ContactGroupDTO contactGroupDTO = contactGroupMapper.toDto(contactGroup);

        int databaseSizeBeforeCreate = contactGroupRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restContactGroupMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(contactGroupDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the ContactGroup in the database
        List<ContactGroup> contactGroupList = contactGroupRepository.findAll();
        assertThat(contactGroupList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkNameIsRequired() throws Exception {
        int databaseSizeBeforeTest = contactGroupRepository.findAll().size();
        // set the field null
        contactGroup.setName(null);

        // Create the ContactGroup, which fails.
        ContactGroupDTO contactGroupDTO = contactGroupMapper.toDto(contactGroup);

        restContactGroupMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(contactGroupDTO))
            )
            .andExpect(status().isBadRequest());

        List<ContactGroup> contactGroupList = contactGroupRepository.findAll();
        assertThat(contactGroupList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllContactGroups() throws Exception {
        // Initialize the database
        contactGroupRepository.saveAndFlush(contactGroup);

        // Get all the contactGroupList
        restContactGroupMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(contactGroup.getId().intValue())))
            .andExpect(jsonPath("$.[*].remoteId").value(hasItem(DEFAULT_REMOTE_ID)))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)))
            .andExpect(jsonPath("$.[*].usage").value(hasItem(DEFAULT_USAGE)));
    }

    @Test
    @Transactional
    void getContactGroup() throws Exception {
        // Initialize the database
        contactGroupRepository.saveAndFlush(contactGroup);

        // Get the contactGroup
        restContactGroupMockMvc
            .perform(get(ENTITY_API_URL_ID, contactGroup.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(contactGroup.getId().intValue()))
            .andExpect(jsonPath("$.remoteId").value(DEFAULT_REMOTE_ID))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME))
            .andExpect(jsonPath("$.usage").value(DEFAULT_USAGE));
    }

    @Test
    @Transactional
    void getNonExistingContactGroup() throws Exception {
        // Get the contactGroup
        restContactGroupMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewContactGroup() throws Exception {
        // Initialize the database
        contactGroupRepository.saveAndFlush(contactGroup);

        int databaseSizeBeforeUpdate = contactGroupRepository.findAll().size();

        // Update the contactGroup
        ContactGroup updatedContactGroup = contactGroupRepository.findById(contactGroup.getId()).get();
        // Disconnect from session so that the updates on updatedContactGroup are not directly saved in db
        em.detach(updatedContactGroup);
        updatedContactGroup.remoteId(UPDATED_REMOTE_ID).name(UPDATED_NAME).usage(UPDATED_USAGE);
        ContactGroupDTO contactGroupDTO = contactGroupMapper.toDto(updatedContactGroup);

        restContactGroupMockMvc
            .perform(
                put(ENTITY_API_URL_ID, contactGroupDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(contactGroupDTO))
            )
            .andExpect(status().isOk());

        // Validate the ContactGroup in the database
        List<ContactGroup> contactGroupList = contactGroupRepository.findAll();
        assertThat(contactGroupList).hasSize(databaseSizeBeforeUpdate);
        ContactGroup testContactGroup = contactGroupList.get(contactGroupList.size() - 1);
        assertThat(testContactGroup.getRemoteId()).isEqualTo(UPDATED_REMOTE_ID);
        assertThat(testContactGroup.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testContactGroup.getUsage()).isEqualTo(UPDATED_USAGE);
    }

    @Test
    @Transactional
    void putNonExistingContactGroup() throws Exception {
        int databaseSizeBeforeUpdate = contactGroupRepository.findAll().size();
        contactGroup.setId(count.incrementAndGet());

        // Create the ContactGroup
        ContactGroupDTO contactGroupDTO = contactGroupMapper.toDto(contactGroup);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restContactGroupMockMvc
            .perform(
                put(ENTITY_API_URL_ID, contactGroupDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(contactGroupDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the ContactGroup in the database
        List<ContactGroup> contactGroupList = contactGroupRepository.findAll();
        assertThat(contactGroupList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchContactGroup() throws Exception {
        int databaseSizeBeforeUpdate = contactGroupRepository.findAll().size();
        contactGroup.setId(count.incrementAndGet());

        // Create the ContactGroup
        ContactGroupDTO contactGroupDTO = contactGroupMapper.toDto(contactGroup);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restContactGroupMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(contactGroupDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the ContactGroup in the database
        List<ContactGroup> contactGroupList = contactGroupRepository.findAll();
        assertThat(contactGroupList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamContactGroup() throws Exception {
        int databaseSizeBeforeUpdate = contactGroupRepository.findAll().size();
        contactGroup.setId(count.incrementAndGet());

        // Create the ContactGroup
        ContactGroupDTO contactGroupDTO = contactGroupMapper.toDto(contactGroup);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restContactGroupMockMvc
            .perform(
                put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(contactGroupDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the ContactGroup in the database
        List<ContactGroup> contactGroupList = contactGroupRepository.findAll();
        assertThat(contactGroupList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateContactGroupWithPatch() throws Exception {
        // Initialize the database
        contactGroupRepository.saveAndFlush(contactGroup);

        int databaseSizeBeforeUpdate = contactGroupRepository.findAll().size();

        // Update the contactGroup using partial update
        ContactGroup partialUpdatedContactGroup = new ContactGroup();
        partialUpdatedContactGroup.setId(contactGroup.getId());

        partialUpdatedContactGroup.remoteId(UPDATED_REMOTE_ID).name(UPDATED_NAME);

        restContactGroupMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedContactGroup.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedContactGroup))
            )
            .andExpect(status().isOk());

        // Validate the ContactGroup in the database
        List<ContactGroup> contactGroupList = contactGroupRepository.findAll();
        assertThat(contactGroupList).hasSize(databaseSizeBeforeUpdate);
        ContactGroup testContactGroup = contactGroupList.get(contactGroupList.size() - 1);
        assertThat(testContactGroup.getRemoteId()).isEqualTo(UPDATED_REMOTE_ID);
        assertThat(testContactGroup.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testContactGroup.getUsage()).isEqualTo(DEFAULT_USAGE);
    }

    @Test
    @Transactional
    void fullUpdateContactGroupWithPatch() throws Exception {
        // Initialize the database
        contactGroupRepository.saveAndFlush(contactGroup);

        int databaseSizeBeforeUpdate = contactGroupRepository.findAll().size();

        // Update the contactGroup using partial update
        ContactGroup partialUpdatedContactGroup = new ContactGroup();
        partialUpdatedContactGroup.setId(contactGroup.getId());

        partialUpdatedContactGroup.remoteId(UPDATED_REMOTE_ID).name(UPDATED_NAME).usage(UPDATED_USAGE);

        restContactGroupMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedContactGroup.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedContactGroup))
            )
            .andExpect(status().isOk());

        // Validate the ContactGroup in the database
        List<ContactGroup> contactGroupList = contactGroupRepository.findAll();
        assertThat(contactGroupList).hasSize(databaseSizeBeforeUpdate);
        ContactGroup testContactGroup = contactGroupList.get(contactGroupList.size() - 1);
        assertThat(testContactGroup.getRemoteId()).isEqualTo(UPDATED_REMOTE_ID);
        assertThat(testContactGroup.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testContactGroup.getUsage()).isEqualTo(UPDATED_USAGE);
    }

    @Test
    @Transactional
    void patchNonExistingContactGroup() throws Exception {
        int databaseSizeBeforeUpdate = contactGroupRepository.findAll().size();
        contactGroup.setId(count.incrementAndGet());

        // Create the ContactGroup
        ContactGroupDTO contactGroupDTO = contactGroupMapper.toDto(contactGroup);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restContactGroupMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, contactGroupDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(contactGroupDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the ContactGroup in the database
        List<ContactGroup> contactGroupList = contactGroupRepository.findAll();
        assertThat(contactGroupList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchContactGroup() throws Exception {
        int databaseSizeBeforeUpdate = contactGroupRepository.findAll().size();
        contactGroup.setId(count.incrementAndGet());

        // Create the ContactGroup
        ContactGroupDTO contactGroupDTO = contactGroupMapper.toDto(contactGroup);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restContactGroupMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(contactGroupDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the ContactGroup in the database
        List<ContactGroup> contactGroupList = contactGroupRepository.findAll();
        assertThat(contactGroupList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamContactGroup() throws Exception {
        int databaseSizeBeforeUpdate = contactGroupRepository.findAll().size();
        contactGroup.setId(count.incrementAndGet());

        // Create the ContactGroup
        ContactGroupDTO contactGroupDTO = contactGroupMapper.toDto(contactGroup);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restContactGroupMockMvc
            .perform(
                patch(ENTITY_API_URL)
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(contactGroupDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the ContactGroup in the database
        List<ContactGroup> contactGroupList = contactGroupRepository.findAll();
        assertThat(contactGroupList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteContactGroup() throws Exception {
        // Initialize the database
        contactGroupRepository.saveAndFlush(contactGroup);

        int databaseSizeBeforeDelete = contactGroupRepository.findAll().size();

        // Delete the contactGroup
        restContactGroupMockMvc
            .perform(delete(ENTITY_API_URL_ID, contactGroup.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<ContactGroup> contactGroupList = contactGroupRepository.findAll();
        assertThat(contactGroupList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
