package ch.united.fastadmin.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.elasticsearch.index.query.QueryBuilders.queryStringQuery;
import static org.hamcrest.Matchers.hasItem;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import ch.united.fastadmin.IntegrationTest;
import ch.united.fastadmin.domain.Contact;
import ch.united.fastadmin.domain.ContactGroup;
import ch.united.fastadmin.repository.ContactGroupRepository;
import ch.united.fastadmin.repository.search.ContactGroupSearchRepository;
import ch.united.fastadmin.service.criteria.ContactGroupCriteria;
import ch.united.fastadmin.service.dto.ContactGroupDTO;
import ch.united.fastadmin.service.mapper.ContactGroupMapper;
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
 * Integration tests for the {@link ContactGroupResource} REST controller.
 */
@IntegrationTest
@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc
@WithMockUser
class ContactGroupResourceIT {

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/contact-groups";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";
    private static final String ENTITY_SEARCH_API_URL = "/api/_search/contact-groups";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ContactGroupRepository contactGroupRepository;

    @Autowired
    private ContactGroupMapper contactGroupMapper;

    /**
     * This repository is mocked in the ch.united.fastadmin.repository.search test package.
     *
     * @see ch.united.fastadmin.repository.search.ContactGroupSearchRepositoryMockConfiguration
     */
    @Autowired
    private ContactGroupSearchRepository mockContactGroupSearchRepository;

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
        ContactGroup contactGroup = new ContactGroup().name(DEFAULT_NAME);
        return contactGroup;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static ContactGroup createUpdatedEntity(EntityManager em) {
        ContactGroup contactGroup = new ContactGroup().name(UPDATED_NAME);
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
        assertThat(testContactGroup.getName()).isEqualTo(DEFAULT_NAME);

        // Validate the ContactGroup in Elasticsearch
        verify(mockContactGroupSearchRepository, times(1)).save(testContactGroup);
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

        // Validate the ContactGroup in Elasticsearch
        verify(mockContactGroupSearchRepository, times(0)).save(contactGroup);
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
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)));
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
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME));
    }

    @Test
    @Transactional
    void getContactGroupsByIdFiltering() throws Exception {
        // Initialize the database
        contactGroupRepository.saveAndFlush(contactGroup);

        Long id = contactGroup.getId();

        defaultContactGroupShouldBeFound("id.equals=" + id);
        defaultContactGroupShouldNotBeFound("id.notEquals=" + id);

        defaultContactGroupShouldBeFound("id.greaterThanOrEqual=" + id);
        defaultContactGroupShouldNotBeFound("id.greaterThan=" + id);

        defaultContactGroupShouldBeFound("id.lessThanOrEqual=" + id);
        defaultContactGroupShouldNotBeFound("id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllContactGroupsByNameIsEqualToSomething() throws Exception {
        // Initialize the database
        contactGroupRepository.saveAndFlush(contactGroup);

        // Get all the contactGroupList where name equals to DEFAULT_NAME
        defaultContactGroupShouldBeFound("name.equals=" + DEFAULT_NAME);

        // Get all the contactGroupList where name equals to UPDATED_NAME
        defaultContactGroupShouldNotBeFound("name.equals=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllContactGroupsByNameIsNotEqualToSomething() throws Exception {
        // Initialize the database
        contactGroupRepository.saveAndFlush(contactGroup);

        // Get all the contactGroupList where name not equals to DEFAULT_NAME
        defaultContactGroupShouldNotBeFound("name.notEquals=" + DEFAULT_NAME);

        // Get all the contactGroupList where name not equals to UPDATED_NAME
        defaultContactGroupShouldBeFound("name.notEquals=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllContactGroupsByNameIsInShouldWork() throws Exception {
        // Initialize the database
        contactGroupRepository.saveAndFlush(contactGroup);

        // Get all the contactGroupList where name in DEFAULT_NAME or UPDATED_NAME
        defaultContactGroupShouldBeFound("name.in=" + DEFAULT_NAME + "," + UPDATED_NAME);

        // Get all the contactGroupList where name equals to UPDATED_NAME
        defaultContactGroupShouldNotBeFound("name.in=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllContactGroupsByNameIsNullOrNotNull() throws Exception {
        // Initialize the database
        contactGroupRepository.saveAndFlush(contactGroup);

        // Get all the contactGroupList where name is not null
        defaultContactGroupShouldBeFound("name.specified=true");

        // Get all the contactGroupList where name is null
        defaultContactGroupShouldNotBeFound("name.specified=false");
    }

    @Test
    @Transactional
    void getAllContactGroupsByNameContainsSomething() throws Exception {
        // Initialize the database
        contactGroupRepository.saveAndFlush(contactGroup);

        // Get all the contactGroupList where name contains DEFAULT_NAME
        defaultContactGroupShouldBeFound("name.contains=" + DEFAULT_NAME);

        // Get all the contactGroupList where name contains UPDATED_NAME
        defaultContactGroupShouldNotBeFound("name.contains=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllContactGroupsByNameNotContainsSomething() throws Exception {
        // Initialize the database
        contactGroupRepository.saveAndFlush(contactGroup);

        // Get all the contactGroupList where name does not contain DEFAULT_NAME
        defaultContactGroupShouldNotBeFound("name.doesNotContain=" + DEFAULT_NAME);

        // Get all the contactGroupList where name does not contain UPDATED_NAME
        defaultContactGroupShouldBeFound("name.doesNotContain=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllContactGroupsByContactIsEqualToSomething() throws Exception {
        // Initialize the database
        contactGroupRepository.saveAndFlush(contactGroup);
        Contact contact = ContactResourceIT.createEntity(em);
        em.persist(contact);
        em.flush();
        contactGroup.setContact(contact);
        contactGroupRepository.saveAndFlush(contactGroup);
        Long contactId = contact.getId();

        // Get all the contactGroupList where contact equals to contactId
        defaultContactGroupShouldBeFound("contactId.equals=" + contactId);

        // Get all the contactGroupList where contact equals to (contactId + 1)
        defaultContactGroupShouldNotBeFound("contactId.equals=" + (contactId + 1));
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultContactGroupShouldBeFound(String filter) throws Exception {
        restContactGroupMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(contactGroup.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)));

        // Check, that the count call also returns 1
        restContactGroupMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultContactGroupShouldNotBeFound(String filter) throws Exception {
        restContactGroupMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restContactGroupMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
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
        updatedContactGroup.name(UPDATED_NAME);
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
        assertThat(testContactGroup.getName()).isEqualTo(UPDATED_NAME);

        // Validate the ContactGroup in Elasticsearch
        verify(mockContactGroupSearchRepository).save(testContactGroup);
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

        // Validate the ContactGroup in Elasticsearch
        verify(mockContactGroupSearchRepository, times(0)).save(contactGroup);
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

        // Validate the ContactGroup in Elasticsearch
        verify(mockContactGroupSearchRepository, times(0)).save(contactGroup);
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

        // Validate the ContactGroup in Elasticsearch
        verify(mockContactGroupSearchRepository, times(0)).save(contactGroup);
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

        partialUpdatedContactGroup.name(UPDATED_NAME);

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
        assertThat(testContactGroup.getName()).isEqualTo(UPDATED_NAME);
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

        partialUpdatedContactGroup.name(UPDATED_NAME);

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
        assertThat(testContactGroup.getName()).isEqualTo(UPDATED_NAME);
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

        // Validate the ContactGroup in Elasticsearch
        verify(mockContactGroupSearchRepository, times(0)).save(contactGroup);
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

        // Validate the ContactGroup in Elasticsearch
        verify(mockContactGroupSearchRepository, times(0)).save(contactGroup);
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

        // Validate the ContactGroup in Elasticsearch
        verify(mockContactGroupSearchRepository, times(0)).save(contactGroup);
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

        // Validate the ContactGroup in Elasticsearch
        verify(mockContactGroupSearchRepository, times(1)).deleteById(contactGroup.getId());
    }

    @Test
    @Transactional
    void searchContactGroup() throws Exception {
        // Configure the mock search repository
        // Initialize the database
        contactGroupRepository.saveAndFlush(contactGroup);
        when(mockContactGroupSearchRepository.search(queryStringQuery("id:" + contactGroup.getId())))
            .thenReturn(Collections.singletonList(contactGroup));

        // Search the contactGroup
        restContactGroupMockMvc
            .perform(get(ENTITY_SEARCH_API_URL + "?query=id:" + contactGroup.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(contactGroup.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)));
    }
}
