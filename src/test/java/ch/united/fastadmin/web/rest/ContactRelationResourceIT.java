package ch.united.fastadmin.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.elasticsearch.index.query.QueryBuilders.queryStringQuery;
import static org.hamcrest.Matchers.hasItem;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import ch.united.fastadmin.IntegrationTest;
import ch.united.fastadmin.domain.ContactRelation;
import ch.united.fastadmin.domain.enumeration.ContactRelationType;
import ch.united.fastadmin.repository.ContactRelationRepository;
import ch.united.fastadmin.repository.search.ContactRelationSearchRepository;
import ch.united.fastadmin.service.dto.ContactRelationDTO;
import ch.united.fastadmin.service.mapper.ContactRelationMapper;
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
 * Integration tests for the {@link ContactRelationResource} REST controller.
 */
@IntegrationTest
@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc
@WithMockUser
class ContactRelationResourceIT {

    private static final ContactRelationType DEFAULT_CONTACT_RELATION_TYPE = ContactRelationType.CUSTOMER;
    private static final ContactRelationType UPDATED_CONTACT_RELATION_TYPE = ContactRelationType.CREDITOR;

    private static final String ENTITY_API_URL = "/api/contact-relations";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";
    private static final String ENTITY_SEARCH_API_URL = "/api/_search/contact-relations";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ContactRelationRepository contactRelationRepository;

    @Autowired
    private ContactRelationMapper contactRelationMapper;

    /**
     * This repository is mocked in the ch.united.fastadmin.repository.search test package.
     *
     * @see ch.united.fastadmin.repository.search.ContactRelationSearchRepositoryMockConfiguration
     */
    @Autowired
    private ContactRelationSearchRepository mockContactRelationSearchRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restContactRelationMockMvc;

    private ContactRelation contactRelation;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static ContactRelation createEntity(EntityManager em) {
        ContactRelation contactRelation = new ContactRelation().contactRelationType(DEFAULT_CONTACT_RELATION_TYPE);
        return contactRelation;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static ContactRelation createUpdatedEntity(EntityManager em) {
        ContactRelation contactRelation = new ContactRelation().contactRelationType(UPDATED_CONTACT_RELATION_TYPE);
        return contactRelation;
    }

    @BeforeEach
    public void initTest() {
        contactRelation = createEntity(em);
    }

    @Test
    @Transactional
    void createContactRelation() throws Exception {
        int databaseSizeBeforeCreate = contactRelationRepository.findAll().size();
        // Create the ContactRelation
        ContactRelationDTO contactRelationDTO = contactRelationMapper.toDto(contactRelation);
        restContactRelationMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(contactRelationDTO))
            )
            .andExpect(status().isCreated());

        // Validate the ContactRelation in the database
        List<ContactRelation> contactRelationList = contactRelationRepository.findAll();
        assertThat(contactRelationList).hasSize(databaseSizeBeforeCreate + 1);
        ContactRelation testContactRelation = contactRelationList.get(contactRelationList.size() - 1);
        assertThat(testContactRelation.getContactRelationType()).isEqualTo(DEFAULT_CONTACT_RELATION_TYPE);

        // Validate the ContactRelation in Elasticsearch
        verify(mockContactRelationSearchRepository, times(1)).save(testContactRelation);
    }

    @Test
    @Transactional
    void createContactRelationWithExistingId() throws Exception {
        // Create the ContactRelation with an existing ID
        contactRelation.setId(1L);
        ContactRelationDTO contactRelationDTO = contactRelationMapper.toDto(contactRelation);

        int databaseSizeBeforeCreate = contactRelationRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restContactRelationMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(contactRelationDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the ContactRelation in the database
        List<ContactRelation> contactRelationList = contactRelationRepository.findAll();
        assertThat(contactRelationList).hasSize(databaseSizeBeforeCreate);

        // Validate the ContactRelation in Elasticsearch
        verify(mockContactRelationSearchRepository, times(0)).save(contactRelation);
    }

    @Test
    @Transactional
    void getAllContactRelations() throws Exception {
        // Initialize the database
        contactRelationRepository.saveAndFlush(contactRelation);

        // Get all the contactRelationList
        restContactRelationMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(contactRelation.getId().intValue())))
            .andExpect(jsonPath("$.[*].contactRelationType").value(hasItem(DEFAULT_CONTACT_RELATION_TYPE.toString())));
    }

    @Test
    @Transactional
    void getContactRelation() throws Exception {
        // Initialize the database
        contactRelationRepository.saveAndFlush(contactRelation);

        // Get the contactRelation
        restContactRelationMockMvc
            .perform(get(ENTITY_API_URL_ID, contactRelation.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(contactRelation.getId().intValue()))
            .andExpect(jsonPath("$.contactRelationType").value(DEFAULT_CONTACT_RELATION_TYPE.toString()));
    }

    @Test
    @Transactional
    void getNonExistingContactRelation() throws Exception {
        // Get the contactRelation
        restContactRelationMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewContactRelation() throws Exception {
        // Initialize the database
        contactRelationRepository.saveAndFlush(contactRelation);

        int databaseSizeBeforeUpdate = contactRelationRepository.findAll().size();

        // Update the contactRelation
        ContactRelation updatedContactRelation = contactRelationRepository.findById(contactRelation.getId()).get();
        // Disconnect from session so that the updates on updatedContactRelation are not directly saved in db
        em.detach(updatedContactRelation);
        updatedContactRelation.contactRelationType(UPDATED_CONTACT_RELATION_TYPE);
        ContactRelationDTO contactRelationDTO = contactRelationMapper.toDto(updatedContactRelation);

        restContactRelationMockMvc
            .perform(
                put(ENTITY_API_URL_ID, contactRelationDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(contactRelationDTO))
            )
            .andExpect(status().isOk());

        // Validate the ContactRelation in the database
        List<ContactRelation> contactRelationList = contactRelationRepository.findAll();
        assertThat(contactRelationList).hasSize(databaseSizeBeforeUpdate);
        ContactRelation testContactRelation = contactRelationList.get(contactRelationList.size() - 1);
        assertThat(testContactRelation.getContactRelationType()).isEqualTo(UPDATED_CONTACT_RELATION_TYPE);

        // Validate the ContactRelation in Elasticsearch
        verify(mockContactRelationSearchRepository).save(testContactRelation);
    }

    @Test
    @Transactional
    void putNonExistingContactRelation() throws Exception {
        int databaseSizeBeforeUpdate = contactRelationRepository.findAll().size();
        contactRelation.setId(count.incrementAndGet());

        // Create the ContactRelation
        ContactRelationDTO contactRelationDTO = contactRelationMapper.toDto(contactRelation);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restContactRelationMockMvc
            .perform(
                put(ENTITY_API_URL_ID, contactRelationDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(contactRelationDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the ContactRelation in the database
        List<ContactRelation> contactRelationList = contactRelationRepository.findAll();
        assertThat(contactRelationList).hasSize(databaseSizeBeforeUpdate);

        // Validate the ContactRelation in Elasticsearch
        verify(mockContactRelationSearchRepository, times(0)).save(contactRelation);
    }

    @Test
    @Transactional
    void putWithIdMismatchContactRelation() throws Exception {
        int databaseSizeBeforeUpdate = contactRelationRepository.findAll().size();
        contactRelation.setId(count.incrementAndGet());

        // Create the ContactRelation
        ContactRelationDTO contactRelationDTO = contactRelationMapper.toDto(contactRelation);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restContactRelationMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(contactRelationDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the ContactRelation in the database
        List<ContactRelation> contactRelationList = contactRelationRepository.findAll();
        assertThat(contactRelationList).hasSize(databaseSizeBeforeUpdate);

        // Validate the ContactRelation in Elasticsearch
        verify(mockContactRelationSearchRepository, times(0)).save(contactRelation);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamContactRelation() throws Exception {
        int databaseSizeBeforeUpdate = contactRelationRepository.findAll().size();
        contactRelation.setId(count.incrementAndGet());

        // Create the ContactRelation
        ContactRelationDTO contactRelationDTO = contactRelationMapper.toDto(contactRelation);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restContactRelationMockMvc
            .perform(
                put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(contactRelationDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the ContactRelation in the database
        List<ContactRelation> contactRelationList = contactRelationRepository.findAll();
        assertThat(contactRelationList).hasSize(databaseSizeBeforeUpdate);

        // Validate the ContactRelation in Elasticsearch
        verify(mockContactRelationSearchRepository, times(0)).save(contactRelation);
    }

    @Test
    @Transactional
    void partialUpdateContactRelationWithPatch() throws Exception {
        // Initialize the database
        contactRelationRepository.saveAndFlush(contactRelation);

        int databaseSizeBeforeUpdate = contactRelationRepository.findAll().size();

        // Update the contactRelation using partial update
        ContactRelation partialUpdatedContactRelation = new ContactRelation();
        partialUpdatedContactRelation.setId(contactRelation.getId());

        partialUpdatedContactRelation.contactRelationType(UPDATED_CONTACT_RELATION_TYPE);

        restContactRelationMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedContactRelation.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedContactRelation))
            )
            .andExpect(status().isOk());

        // Validate the ContactRelation in the database
        List<ContactRelation> contactRelationList = contactRelationRepository.findAll();
        assertThat(contactRelationList).hasSize(databaseSizeBeforeUpdate);
        ContactRelation testContactRelation = contactRelationList.get(contactRelationList.size() - 1);
        assertThat(testContactRelation.getContactRelationType()).isEqualTo(UPDATED_CONTACT_RELATION_TYPE);
    }

    @Test
    @Transactional
    void fullUpdateContactRelationWithPatch() throws Exception {
        // Initialize the database
        contactRelationRepository.saveAndFlush(contactRelation);

        int databaseSizeBeforeUpdate = contactRelationRepository.findAll().size();

        // Update the contactRelation using partial update
        ContactRelation partialUpdatedContactRelation = new ContactRelation();
        partialUpdatedContactRelation.setId(contactRelation.getId());

        partialUpdatedContactRelation.contactRelationType(UPDATED_CONTACT_RELATION_TYPE);

        restContactRelationMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedContactRelation.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedContactRelation))
            )
            .andExpect(status().isOk());

        // Validate the ContactRelation in the database
        List<ContactRelation> contactRelationList = contactRelationRepository.findAll();
        assertThat(contactRelationList).hasSize(databaseSizeBeforeUpdate);
        ContactRelation testContactRelation = contactRelationList.get(contactRelationList.size() - 1);
        assertThat(testContactRelation.getContactRelationType()).isEqualTo(UPDATED_CONTACT_RELATION_TYPE);
    }

    @Test
    @Transactional
    void patchNonExistingContactRelation() throws Exception {
        int databaseSizeBeforeUpdate = contactRelationRepository.findAll().size();
        contactRelation.setId(count.incrementAndGet());

        // Create the ContactRelation
        ContactRelationDTO contactRelationDTO = contactRelationMapper.toDto(contactRelation);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restContactRelationMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, contactRelationDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(contactRelationDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the ContactRelation in the database
        List<ContactRelation> contactRelationList = contactRelationRepository.findAll();
        assertThat(contactRelationList).hasSize(databaseSizeBeforeUpdate);

        // Validate the ContactRelation in Elasticsearch
        verify(mockContactRelationSearchRepository, times(0)).save(contactRelation);
    }

    @Test
    @Transactional
    void patchWithIdMismatchContactRelation() throws Exception {
        int databaseSizeBeforeUpdate = contactRelationRepository.findAll().size();
        contactRelation.setId(count.incrementAndGet());

        // Create the ContactRelation
        ContactRelationDTO contactRelationDTO = contactRelationMapper.toDto(contactRelation);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restContactRelationMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(contactRelationDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the ContactRelation in the database
        List<ContactRelation> contactRelationList = contactRelationRepository.findAll();
        assertThat(contactRelationList).hasSize(databaseSizeBeforeUpdate);

        // Validate the ContactRelation in Elasticsearch
        verify(mockContactRelationSearchRepository, times(0)).save(contactRelation);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamContactRelation() throws Exception {
        int databaseSizeBeforeUpdate = contactRelationRepository.findAll().size();
        contactRelation.setId(count.incrementAndGet());

        // Create the ContactRelation
        ContactRelationDTO contactRelationDTO = contactRelationMapper.toDto(contactRelation);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restContactRelationMockMvc
            .perform(
                patch(ENTITY_API_URL)
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(contactRelationDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the ContactRelation in the database
        List<ContactRelation> contactRelationList = contactRelationRepository.findAll();
        assertThat(contactRelationList).hasSize(databaseSizeBeforeUpdate);

        // Validate the ContactRelation in Elasticsearch
        verify(mockContactRelationSearchRepository, times(0)).save(contactRelation);
    }

    @Test
    @Transactional
    void deleteContactRelation() throws Exception {
        // Initialize the database
        contactRelationRepository.saveAndFlush(contactRelation);

        int databaseSizeBeforeDelete = contactRelationRepository.findAll().size();

        // Delete the contactRelation
        restContactRelationMockMvc
            .perform(delete(ENTITY_API_URL_ID, contactRelation.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<ContactRelation> contactRelationList = contactRelationRepository.findAll();
        assertThat(contactRelationList).hasSize(databaseSizeBeforeDelete - 1);

        // Validate the ContactRelation in Elasticsearch
        verify(mockContactRelationSearchRepository, times(1)).deleteById(contactRelation.getId());
    }

    @Test
    @Transactional
    void searchContactRelation() throws Exception {
        // Configure the mock search repository
        // Initialize the database
        contactRelationRepository.saveAndFlush(contactRelation);
        when(mockContactRelationSearchRepository.search(queryStringQuery("id:" + contactRelation.getId())))
            .thenReturn(Collections.singletonList(contactRelation));

        // Search the contactRelation
        restContactRelationMockMvc
            .perform(get(ENTITY_SEARCH_API_URL + "?query=id:" + contactRelation.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(contactRelation.getId().intValue())))
            .andExpect(jsonPath("$.[*].contactRelationType").value(hasItem(DEFAULT_CONTACT_RELATION_TYPE.toString())));
    }
}
