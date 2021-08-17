package ch.united.fastadmin.web.rest;

import static ch.united.fastadmin.web.rest.TestUtil.sameInstant;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import ch.united.fastadmin.IntegrationTest;
import ch.united.fastadmin.domain.DocumentLetter;
import ch.united.fastadmin.domain.enumeration.DocumentLanguage;
import ch.united.fastadmin.domain.enumeration.LetterStatus;
import ch.united.fastadmin.repository.DocumentLetterRepository;
import ch.united.fastadmin.service.dto.DocumentLetterDTO;
import ch.united.fastadmin.service.mapper.DocumentLetterMapper;
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
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
 * Integration tests for the {@link DocumentLetterResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class DocumentLetterResourceIT {

    private static final Integer DEFAULT_REMOTE_ID = 1;
    private static final Integer UPDATED_REMOTE_ID = 2;

    private static final String DEFAULT_CONTACT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_CONTACT_NAME = "BBBBBBBBBB";

    private static final LocalDate DEFAULT_DATE = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_DATE = LocalDate.now(ZoneId.systemDefault());

    private static final String DEFAULT_TITLE = "AAAAAAAAAA";
    private static final String UPDATED_TITLE = "BBBBBBBBBB";

    private static final String DEFAULT_CONTENT = "AAAAAAAAAA";
    private static final String UPDATED_CONTENT = "BBBBBBBBBB";

    private static final DocumentLanguage DEFAULT_LANGUAGE = DocumentLanguage.FRENCH;
    private static final DocumentLanguage UPDATED_LANGUAGE = DocumentLanguage.ENGLISH;

    private static final Integer DEFAULT_PAGE_AMOUNT = 1;
    private static final Integer UPDATED_PAGE_AMOUNT = 2;

    private static final String DEFAULT_NOTES = "AAAAAAAAAA";
    private static final String UPDATED_NOTES = "BBBBBBBBBB";

    private static final LetterStatus DEFAULT_STATUS = LetterStatus.DRAFT;
    private static final LetterStatus UPDATED_STATUS = LetterStatus.SENT;

    private static final ZonedDateTime DEFAULT_CREATED = ZonedDateTime.ofInstant(Instant.ofEpochMilli(0L), ZoneOffset.UTC);
    private static final ZonedDateTime UPDATED_CREATED = ZonedDateTime.now(ZoneId.systemDefault()).withNano(0);

    private static final String ENTITY_API_URL = "/api/document-letters";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private DocumentLetterRepository documentLetterRepository;

    @Autowired
    private DocumentLetterMapper documentLetterMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restDocumentLetterMockMvc;

    private DocumentLetter documentLetter;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static DocumentLetter createEntity(EntityManager em) {
        DocumentLetter documentLetter = new DocumentLetter()
            .remoteId(DEFAULT_REMOTE_ID)
            .contactName(DEFAULT_CONTACT_NAME)
            .date(DEFAULT_DATE)
            .title(DEFAULT_TITLE)
            .content(DEFAULT_CONTENT)
            .language(DEFAULT_LANGUAGE)
            .pageAmount(DEFAULT_PAGE_AMOUNT)
            .notes(DEFAULT_NOTES)
            .status(DEFAULT_STATUS)
            .created(DEFAULT_CREATED);
        return documentLetter;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static DocumentLetter createUpdatedEntity(EntityManager em) {
        DocumentLetter documentLetter = new DocumentLetter()
            .remoteId(UPDATED_REMOTE_ID)
            .contactName(UPDATED_CONTACT_NAME)
            .date(UPDATED_DATE)
            .title(UPDATED_TITLE)
            .content(UPDATED_CONTENT)
            .language(UPDATED_LANGUAGE)
            .pageAmount(UPDATED_PAGE_AMOUNT)
            .notes(UPDATED_NOTES)
            .status(UPDATED_STATUS)
            .created(UPDATED_CREATED);
        return documentLetter;
    }

    @BeforeEach
    public void initTest() {
        documentLetter = createEntity(em);
    }

    @Test
    @Transactional
    void createDocumentLetter() throws Exception {
        int databaseSizeBeforeCreate = documentLetterRepository.findAll().size();
        // Create the DocumentLetter
        DocumentLetterDTO documentLetterDTO = documentLetterMapper.toDto(documentLetter);
        restDocumentLetterMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(documentLetterDTO))
            )
            .andExpect(status().isCreated());

        // Validate the DocumentLetter in the database
        List<DocumentLetter> documentLetterList = documentLetterRepository.findAll();
        assertThat(documentLetterList).hasSize(databaseSizeBeforeCreate + 1);
        DocumentLetter testDocumentLetter = documentLetterList.get(documentLetterList.size() - 1);
        assertThat(testDocumentLetter.getRemoteId()).isEqualTo(DEFAULT_REMOTE_ID);
        assertThat(testDocumentLetter.getContactName()).isEqualTo(DEFAULT_CONTACT_NAME);
        assertThat(testDocumentLetter.getDate()).isEqualTo(DEFAULT_DATE);
        assertThat(testDocumentLetter.getTitle()).isEqualTo(DEFAULT_TITLE);
        assertThat(testDocumentLetter.getContent()).isEqualTo(DEFAULT_CONTENT);
        assertThat(testDocumentLetter.getLanguage()).isEqualTo(DEFAULT_LANGUAGE);
        assertThat(testDocumentLetter.getPageAmount()).isEqualTo(DEFAULT_PAGE_AMOUNT);
        assertThat(testDocumentLetter.getNotes()).isEqualTo(DEFAULT_NOTES);
        assertThat(testDocumentLetter.getStatus()).isEqualTo(DEFAULT_STATUS);
        assertThat(testDocumentLetter.getCreated()).isEqualTo(DEFAULT_CREATED);
    }

    @Test
    @Transactional
    void createDocumentLetterWithExistingId() throws Exception {
        // Create the DocumentLetter with an existing ID
        documentLetter.setId(1L);
        DocumentLetterDTO documentLetterDTO = documentLetterMapper.toDto(documentLetter);

        int databaseSizeBeforeCreate = documentLetterRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restDocumentLetterMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(documentLetterDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the DocumentLetter in the database
        List<DocumentLetter> documentLetterList = documentLetterRepository.findAll();
        assertThat(documentLetterList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllDocumentLetters() throws Exception {
        // Initialize the database
        documentLetterRepository.saveAndFlush(documentLetter);

        // Get all the documentLetterList
        restDocumentLetterMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(documentLetter.getId().intValue())))
            .andExpect(jsonPath("$.[*].remoteId").value(hasItem(DEFAULT_REMOTE_ID)))
            .andExpect(jsonPath("$.[*].contactName").value(hasItem(DEFAULT_CONTACT_NAME)))
            .andExpect(jsonPath("$.[*].date").value(hasItem(DEFAULT_DATE.toString())))
            .andExpect(jsonPath("$.[*].title").value(hasItem(DEFAULT_TITLE)))
            .andExpect(jsonPath("$.[*].content").value(hasItem(DEFAULT_CONTENT)))
            .andExpect(jsonPath("$.[*].language").value(hasItem(DEFAULT_LANGUAGE.toString())))
            .andExpect(jsonPath("$.[*].pageAmount").value(hasItem(DEFAULT_PAGE_AMOUNT)))
            .andExpect(jsonPath("$.[*].notes").value(hasItem(DEFAULT_NOTES)))
            .andExpect(jsonPath("$.[*].status").value(hasItem(DEFAULT_STATUS.toString())))
            .andExpect(jsonPath("$.[*].created").value(hasItem(sameInstant(DEFAULT_CREATED))));
    }

    @Test
    @Transactional
    void getDocumentLetter() throws Exception {
        // Initialize the database
        documentLetterRepository.saveAndFlush(documentLetter);

        // Get the documentLetter
        restDocumentLetterMockMvc
            .perform(get(ENTITY_API_URL_ID, documentLetter.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(documentLetter.getId().intValue()))
            .andExpect(jsonPath("$.remoteId").value(DEFAULT_REMOTE_ID))
            .andExpect(jsonPath("$.contactName").value(DEFAULT_CONTACT_NAME))
            .andExpect(jsonPath("$.date").value(DEFAULT_DATE.toString()))
            .andExpect(jsonPath("$.title").value(DEFAULT_TITLE))
            .andExpect(jsonPath("$.content").value(DEFAULT_CONTENT))
            .andExpect(jsonPath("$.language").value(DEFAULT_LANGUAGE.toString()))
            .andExpect(jsonPath("$.pageAmount").value(DEFAULT_PAGE_AMOUNT))
            .andExpect(jsonPath("$.notes").value(DEFAULT_NOTES))
            .andExpect(jsonPath("$.status").value(DEFAULT_STATUS.toString()))
            .andExpect(jsonPath("$.created").value(sameInstant(DEFAULT_CREATED)));
    }

    @Test
    @Transactional
    void getNonExistingDocumentLetter() throws Exception {
        // Get the documentLetter
        restDocumentLetterMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewDocumentLetter() throws Exception {
        // Initialize the database
        documentLetterRepository.saveAndFlush(documentLetter);

        int databaseSizeBeforeUpdate = documentLetterRepository.findAll().size();

        // Update the documentLetter
        DocumentLetter updatedDocumentLetter = documentLetterRepository.findById(documentLetter.getId()).get();
        // Disconnect from session so that the updates on updatedDocumentLetter are not directly saved in db
        em.detach(updatedDocumentLetter);
        updatedDocumentLetter
            .remoteId(UPDATED_REMOTE_ID)
            .contactName(UPDATED_CONTACT_NAME)
            .date(UPDATED_DATE)
            .title(UPDATED_TITLE)
            .content(UPDATED_CONTENT)
            .language(UPDATED_LANGUAGE)
            .pageAmount(UPDATED_PAGE_AMOUNT)
            .notes(UPDATED_NOTES)
            .status(UPDATED_STATUS)
            .created(UPDATED_CREATED);
        DocumentLetterDTO documentLetterDTO = documentLetterMapper.toDto(updatedDocumentLetter);

        restDocumentLetterMockMvc
            .perform(
                put(ENTITY_API_URL_ID, documentLetterDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(documentLetterDTO))
            )
            .andExpect(status().isOk());

        // Validate the DocumentLetter in the database
        List<DocumentLetter> documentLetterList = documentLetterRepository.findAll();
        assertThat(documentLetterList).hasSize(databaseSizeBeforeUpdate);
        DocumentLetter testDocumentLetter = documentLetterList.get(documentLetterList.size() - 1);
        assertThat(testDocumentLetter.getRemoteId()).isEqualTo(UPDATED_REMOTE_ID);
        assertThat(testDocumentLetter.getContactName()).isEqualTo(UPDATED_CONTACT_NAME);
        assertThat(testDocumentLetter.getDate()).isEqualTo(UPDATED_DATE);
        assertThat(testDocumentLetter.getTitle()).isEqualTo(UPDATED_TITLE);
        assertThat(testDocumentLetter.getContent()).isEqualTo(UPDATED_CONTENT);
        assertThat(testDocumentLetter.getLanguage()).isEqualTo(UPDATED_LANGUAGE);
        assertThat(testDocumentLetter.getPageAmount()).isEqualTo(UPDATED_PAGE_AMOUNT);
        assertThat(testDocumentLetter.getNotes()).isEqualTo(UPDATED_NOTES);
        assertThat(testDocumentLetter.getStatus()).isEqualTo(UPDATED_STATUS);
        assertThat(testDocumentLetter.getCreated()).isEqualTo(UPDATED_CREATED);
    }

    @Test
    @Transactional
    void putNonExistingDocumentLetter() throws Exception {
        int databaseSizeBeforeUpdate = documentLetterRepository.findAll().size();
        documentLetter.setId(count.incrementAndGet());

        // Create the DocumentLetter
        DocumentLetterDTO documentLetterDTO = documentLetterMapper.toDto(documentLetter);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restDocumentLetterMockMvc
            .perform(
                put(ENTITY_API_URL_ID, documentLetterDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(documentLetterDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the DocumentLetter in the database
        List<DocumentLetter> documentLetterList = documentLetterRepository.findAll();
        assertThat(documentLetterList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchDocumentLetter() throws Exception {
        int databaseSizeBeforeUpdate = documentLetterRepository.findAll().size();
        documentLetter.setId(count.incrementAndGet());

        // Create the DocumentLetter
        DocumentLetterDTO documentLetterDTO = documentLetterMapper.toDto(documentLetter);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restDocumentLetterMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(documentLetterDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the DocumentLetter in the database
        List<DocumentLetter> documentLetterList = documentLetterRepository.findAll();
        assertThat(documentLetterList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamDocumentLetter() throws Exception {
        int databaseSizeBeforeUpdate = documentLetterRepository.findAll().size();
        documentLetter.setId(count.incrementAndGet());

        // Create the DocumentLetter
        DocumentLetterDTO documentLetterDTO = documentLetterMapper.toDto(documentLetter);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restDocumentLetterMockMvc
            .perform(
                put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(documentLetterDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the DocumentLetter in the database
        List<DocumentLetter> documentLetterList = documentLetterRepository.findAll();
        assertThat(documentLetterList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateDocumentLetterWithPatch() throws Exception {
        // Initialize the database
        documentLetterRepository.saveAndFlush(documentLetter);

        int databaseSizeBeforeUpdate = documentLetterRepository.findAll().size();

        // Update the documentLetter using partial update
        DocumentLetter partialUpdatedDocumentLetter = new DocumentLetter();
        partialUpdatedDocumentLetter.setId(documentLetter.getId());

        partialUpdatedDocumentLetter
            .title(UPDATED_TITLE)
            .language(UPDATED_LANGUAGE)
            .notes(UPDATED_NOTES)
            .status(UPDATED_STATUS)
            .created(UPDATED_CREATED);

        restDocumentLetterMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedDocumentLetter.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedDocumentLetter))
            )
            .andExpect(status().isOk());

        // Validate the DocumentLetter in the database
        List<DocumentLetter> documentLetterList = documentLetterRepository.findAll();
        assertThat(documentLetterList).hasSize(databaseSizeBeforeUpdate);
        DocumentLetter testDocumentLetter = documentLetterList.get(documentLetterList.size() - 1);
        assertThat(testDocumentLetter.getRemoteId()).isEqualTo(DEFAULT_REMOTE_ID);
        assertThat(testDocumentLetter.getContactName()).isEqualTo(DEFAULT_CONTACT_NAME);
        assertThat(testDocumentLetter.getDate()).isEqualTo(DEFAULT_DATE);
        assertThat(testDocumentLetter.getTitle()).isEqualTo(UPDATED_TITLE);
        assertThat(testDocumentLetter.getContent()).isEqualTo(DEFAULT_CONTENT);
        assertThat(testDocumentLetter.getLanguage()).isEqualTo(UPDATED_LANGUAGE);
        assertThat(testDocumentLetter.getPageAmount()).isEqualTo(DEFAULT_PAGE_AMOUNT);
        assertThat(testDocumentLetter.getNotes()).isEqualTo(UPDATED_NOTES);
        assertThat(testDocumentLetter.getStatus()).isEqualTo(UPDATED_STATUS);
        assertThat(testDocumentLetter.getCreated()).isEqualTo(UPDATED_CREATED);
    }

    @Test
    @Transactional
    void fullUpdateDocumentLetterWithPatch() throws Exception {
        // Initialize the database
        documentLetterRepository.saveAndFlush(documentLetter);

        int databaseSizeBeforeUpdate = documentLetterRepository.findAll().size();

        // Update the documentLetter using partial update
        DocumentLetter partialUpdatedDocumentLetter = new DocumentLetter();
        partialUpdatedDocumentLetter.setId(documentLetter.getId());

        partialUpdatedDocumentLetter
            .remoteId(UPDATED_REMOTE_ID)
            .contactName(UPDATED_CONTACT_NAME)
            .date(UPDATED_DATE)
            .title(UPDATED_TITLE)
            .content(UPDATED_CONTENT)
            .language(UPDATED_LANGUAGE)
            .pageAmount(UPDATED_PAGE_AMOUNT)
            .notes(UPDATED_NOTES)
            .status(UPDATED_STATUS)
            .created(UPDATED_CREATED);

        restDocumentLetterMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedDocumentLetter.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedDocumentLetter))
            )
            .andExpect(status().isOk());

        // Validate the DocumentLetter in the database
        List<DocumentLetter> documentLetterList = documentLetterRepository.findAll();
        assertThat(documentLetterList).hasSize(databaseSizeBeforeUpdate);
        DocumentLetter testDocumentLetter = documentLetterList.get(documentLetterList.size() - 1);
        assertThat(testDocumentLetter.getRemoteId()).isEqualTo(UPDATED_REMOTE_ID);
        assertThat(testDocumentLetter.getContactName()).isEqualTo(UPDATED_CONTACT_NAME);
        assertThat(testDocumentLetter.getDate()).isEqualTo(UPDATED_DATE);
        assertThat(testDocumentLetter.getTitle()).isEqualTo(UPDATED_TITLE);
        assertThat(testDocumentLetter.getContent()).isEqualTo(UPDATED_CONTENT);
        assertThat(testDocumentLetter.getLanguage()).isEqualTo(UPDATED_LANGUAGE);
        assertThat(testDocumentLetter.getPageAmount()).isEqualTo(UPDATED_PAGE_AMOUNT);
        assertThat(testDocumentLetter.getNotes()).isEqualTo(UPDATED_NOTES);
        assertThat(testDocumentLetter.getStatus()).isEqualTo(UPDATED_STATUS);
        assertThat(testDocumentLetter.getCreated()).isEqualTo(UPDATED_CREATED);
    }

    @Test
    @Transactional
    void patchNonExistingDocumentLetter() throws Exception {
        int databaseSizeBeforeUpdate = documentLetterRepository.findAll().size();
        documentLetter.setId(count.incrementAndGet());

        // Create the DocumentLetter
        DocumentLetterDTO documentLetterDTO = documentLetterMapper.toDto(documentLetter);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restDocumentLetterMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, documentLetterDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(documentLetterDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the DocumentLetter in the database
        List<DocumentLetter> documentLetterList = documentLetterRepository.findAll();
        assertThat(documentLetterList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchDocumentLetter() throws Exception {
        int databaseSizeBeforeUpdate = documentLetterRepository.findAll().size();
        documentLetter.setId(count.incrementAndGet());

        // Create the DocumentLetter
        DocumentLetterDTO documentLetterDTO = documentLetterMapper.toDto(documentLetter);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restDocumentLetterMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(documentLetterDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the DocumentLetter in the database
        List<DocumentLetter> documentLetterList = documentLetterRepository.findAll();
        assertThat(documentLetterList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamDocumentLetter() throws Exception {
        int databaseSizeBeforeUpdate = documentLetterRepository.findAll().size();
        documentLetter.setId(count.incrementAndGet());

        // Create the DocumentLetter
        DocumentLetterDTO documentLetterDTO = documentLetterMapper.toDto(documentLetter);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restDocumentLetterMockMvc
            .perform(
                patch(ENTITY_API_URL)
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(documentLetterDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the DocumentLetter in the database
        List<DocumentLetter> documentLetterList = documentLetterRepository.findAll();
        assertThat(documentLetterList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteDocumentLetter() throws Exception {
        // Initialize the database
        documentLetterRepository.saveAndFlush(documentLetter);

        int databaseSizeBeforeDelete = documentLetterRepository.findAll().size();

        // Delete the documentLetter
        restDocumentLetterMockMvc
            .perform(delete(ENTITY_API_URL_ID, documentLetter.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<DocumentLetter> documentLetterList = documentLetterRepository.findAll();
        assertThat(documentLetterList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
