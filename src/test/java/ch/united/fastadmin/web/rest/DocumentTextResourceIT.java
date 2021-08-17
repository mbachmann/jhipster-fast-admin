package ch.united.fastadmin.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import ch.united.fastadmin.IntegrationTest;
import ch.united.fastadmin.domain.DocumentText;
import ch.united.fastadmin.domain.enumeration.DocumentInvoiceTextStatus;
import ch.united.fastadmin.domain.enumeration.DocumentLanguage;
import ch.united.fastadmin.domain.enumeration.DocumentTextDocumentType;
import ch.united.fastadmin.domain.enumeration.DocumentTextType;
import ch.united.fastadmin.domain.enumeration.DocumentTextUsage;
import ch.united.fastadmin.repository.DocumentTextRepository;
import ch.united.fastadmin.service.dto.DocumentTextDTO;
import ch.united.fastadmin.service.mapper.DocumentTextMapper;
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
 * Integration tests for the {@link DocumentTextResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class DocumentTextResourceIT {

    private static final Boolean DEFAULT_DEFAULT_TEXT = false;
    private static final Boolean UPDATED_DEFAULT_TEXT = true;

    private static final String DEFAULT_TEXT = "AAAAAAAAAA";
    private static final String UPDATED_TEXT = "BBBBBBBBBB";

    private static final DocumentLanguage DEFAULT_LANGUAGE = DocumentLanguage.FRENCH;
    private static final DocumentLanguage UPDATED_LANGUAGE = DocumentLanguage.ENGLISH;

    private static final DocumentTextUsage DEFAULT_USAGE = DocumentTextUsage.TITLE;
    private static final DocumentTextUsage UPDATED_USAGE = DocumentTextUsage.INTRODUCTION;

    private static final DocumentInvoiceTextStatus DEFAULT_STATUS = DocumentInvoiceTextStatus.DEFAULT;
    private static final DocumentInvoiceTextStatus UPDATED_STATUS = DocumentInvoiceTextStatus.PAYMENT_REMINDER;

    private static final DocumentTextType DEFAULT_TYPE = DocumentTextType.DOCUMENT;
    private static final DocumentTextType UPDATED_TYPE = DocumentTextType.EMAIL;

    private static final DocumentTextDocumentType DEFAULT_DOCUMENT_TYPE = DocumentTextDocumentType.OFFER;
    private static final DocumentTextDocumentType UPDATED_DOCUMENT_TYPE = DocumentTextDocumentType.ORDER_CONFIRMATION;

    private static final String ENTITY_API_URL = "/api/document-texts";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private DocumentTextRepository documentTextRepository;

    @Autowired
    private DocumentTextMapper documentTextMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restDocumentTextMockMvc;

    private DocumentText documentText;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static DocumentText createEntity(EntityManager em) {
        DocumentText documentText = new DocumentText()
            .defaultText(DEFAULT_DEFAULT_TEXT)
            .text(DEFAULT_TEXT)
            .language(DEFAULT_LANGUAGE)
            .usage(DEFAULT_USAGE)
            .status(DEFAULT_STATUS)
            .type(DEFAULT_TYPE)
            .documentType(DEFAULT_DOCUMENT_TYPE);
        return documentText;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static DocumentText createUpdatedEntity(EntityManager em) {
        DocumentText documentText = new DocumentText()
            .defaultText(UPDATED_DEFAULT_TEXT)
            .text(UPDATED_TEXT)
            .language(UPDATED_LANGUAGE)
            .usage(UPDATED_USAGE)
            .status(UPDATED_STATUS)
            .type(UPDATED_TYPE)
            .documentType(UPDATED_DOCUMENT_TYPE);
        return documentText;
    }

    @BeforeEach
    public void initTest() {
        documentText = createEntity(em);
    }

    @Test
    @Transactional
    void createDocumentText() throws Exception {
        int databaseSizeBeforeCreate = documentTextRepository.findAll().size();
        // Create the DocumentText
        DocumentTextDTO documentTextDTO = documentTextMapper.toDto(documentText);
        restDocumentTextMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(documentTextDTO))
            )
            .andExpect(status().isCreated());

        // Validate the DocumentText in the database
        List<DocumentText> documentTextList = documentTextRepository.findAll();
        assertThat(documentTextList).hasSize(databaseSizeBeforeCreate + 1);
        DocumentText testDocumentText = documentTextList.get(documentTextList.size() - 1);
        assertThat(testDocumentText.getDefaultText()).isEqualTo(DEFAULT_DEFAULT_TEXT);
        assertThat(testDocumentText.getText()).isEqualTo(DEFAULT_TEXT);
        assertThat(testDocumentText.getLanguage()).isEqualTo(DEFAULT_LANGUAGE);
        assertThat(testDocumentText.getUsage()).isEqualTo(DEFAULT_USAGE);
        assertThat(testDocumentText.getStatus()).isEqualTo(DEFAULT_STATUS);
        assertThat(testDocumentText.getType()).isEqualTo(DEFAULT_TYPE);
        assertThat(testDocumentText.getDocumentType()).isEqualTo(DEFAULT_DOCUMENT_TYPE);
    }

    @Test
    @Transactional
    void createDocumentTextWithExistingId() throws Exception {
        // Create the DocumentText with an existing ID
        documentText.setId(1L);
        DocumentTextDTO documentTextDTO = documentTextMapper.toDto(documentText);

        int databaseSizeBeforeCreate = documentTextRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restDocumentTextMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(documentTextDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the DocumentText in the database
        List<DocumentText> documentTextList = documentTextRepository.findAll();
        assertThat(documentTextList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllDocumentTexts() throws Exception {
        // Initialize the database
        documentTextRepository.saveAndFlush(documentText);

        // Get all the documentTextList
        restDocumentTextMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(documentText.getId().intValue())))
            .andExpect(jsonPath("$.[*].defaultText").value(hasItem(DEFAULT_DEFAULT_TEXT.booleanValue())))
            .andExpect(jsonPath("$.[*].text").value(hasItem(DEFAULT_TEXT)))
            .andExpect(jsonPath("$.[*].language").value(hasItem(DEFAULT_LANGUAGE.toString())))
            .andExpect(jsonPath("$.[*].usage").value(hasItem(DEFAULT_USAGE.toString())))
            .andExpect(jsonPath("$.[*].status").value(hasItem(DEFAULT_STATUS.toString())))
            .andExpect(jsonPath("$.[*].type").value(hasItem(DEFAULT_TYPE.toString())))
            .andExpect(jsonPath("$.[*].documentType").value(hasItem(DEFAULT_DOCUMENT_TYPE.toString())));
    }

    @Test
    @Transactional
    void getDocumentText() throws Exception {
        // Initialize the database
        documentTextRepository.saveAndFlush(documentText);

        // Get the documentText
        restDocumentTextMockMvc
            .perform(get(ENTITY_API_URL_ID, documentText.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(documentText.getId().intValue()))
            .andExpect(jsonPath("$.defaultText").value(DEFAULT_DEFAULT_TEXT.booleanValue()))
            .andExpect(jsonPath("$.text").value(DEFAULT_TEXT))
            .andExpect(jsonPath("$.language").value(DEFAULT_LANGUAGE.toString()))
            .andExpect(jsonPath("$.usage").value(DEFAULT_USAGE.toString()))
            .andExpect(jsonPath("$.status").value(DEFAULT_STATUS.toString()))
            .andExpect(jsonPath("$.type").value(DEFAULT_TYPE.toString()))
            .andExpect(jsonPath("$.documentType").value(DEFAULT_DOCUMENT_TYPE.toString()));
    }

    @Test
    @Transactional
    void getNonExistingDocumentText() throws Exception {
        // Get the documentText
        restDocumentTextMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewDocumentText() throws Exception {
        // Initialize the database
        documentTextRepository.saveAndFlush(documentText);

        int databaseSizeBeforeUpdate = documentTextRepository.findAll().size();

        // Update the documentText
        DocumentText updatedDocumentText = documentTextRepository.findById(documentText.getId()).get();
        // Disconnect from session so that the updates on updatedDocumentText are not directly saved in db
        em.detach(updatedDocumentText);
        updatedDocumentText
            .defaultText(UPDATED_DEFAULT_TEXT)
            .text(UPDATED_TEXT)
            .language(UPDATED_LANGUAGE)
            .usage(UPDATED_USAGE)
            .status(UPDATED_STATUS)
            .type(UPDATED_TYPE)
            .documentType(UPDATED_DOCUMENT_TYPE);
        DocumentTextDTO documentTextDTO = documentTextMapper.toDto(updatedDocumentText);

        restDocumentTextMockMvc
            .perform(
                put(ENTITY_API_URL_ID, documentTextDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(documentTextDTO))
            )
            .andExpect(status().isOk());

        // Validate the DocumentText in the database
        List<DocumentText> documentTextList = documentTextRepository.findAll();
        assertThat(documentTextList).hasSize(databaseSizeBeforeUpdate);
        DocumentText testDocumentText = documentTextList.get(documentTextList.size() - 1);
        assertThat(testDocumentText.getDefaultText()).isEqualTo(UPDATED_DEFAULT_TEXT);
        assertThat(testDocumentText.getText()).isEqualTo(UPDATED_TEXT);
        assertThat(testDocumentText.getLanguage()).isEqualTo(UPDATED_LANGUAGE);
        assertThat(testDocumentText.getUsage()).isEqualTo(UPDATED_USAGE);
        assertThat(testDocumentText.getStatus()).isEqualTo(UPDATED_STATUS);
        assertThat(testDocumentText.getType()).isEqualTo(UPDATED_TYPE);
        assertThat(testDocumentText.getDocumentType()).isEqualTo(UPDATED_DOCUMENT_TYPE);
    }

    @Test
    @Transactional
    void putNonExistingDocumentText() throws Exception {
        int databaseSizeBeforeUpdate = documentTextRepository.findAll().size();
        documentText.setId(count.incrementAndGet());

        // Create the DocumentText
        DocumentTextDTO documentTextDTO = documentTextMapper.toDto(documentText);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restDocumentTextMockMvc
            .perform(
                put(ENTITY_API_URL_ID, documentTextDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(documentTextDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the DocumentText in the database
        List<DocumentText> documentTextList = documentTextRepository.findAll();
        assertThat(documentTextList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchDocumentText() throws Exception {
        int databaseSizeBeforeUpdate = documentTextRepository.findAll().size();
        documentText.setId(count.incrementAndGet());

        // Create the DocumentText
        DocumentTextDTO documentTextDTO = documentTextMapper.toDto(documentText);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restDocumentTextMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(documentTextDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the DocumentText in the database
        List<DocumentText> documentTextList = documentTextRepository.findAll();
        assertThat(documentTextList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamDocumentText() throws Exception {
        int databaseSizeBeforeUpdate = documentTextRepository.findAll().size();
        documentText.setId(count.incrementAndGet());

        // Create the DocumentText
        DocumentTextDTO documentTextDTO = documentTextMapper.toDto(documentText);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restDocumentTextMockMvc
            .perform(
                put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(documentTextDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the DocumentText in the database
        List<DocumentText> documentTextList = documentTextRepository.findAll();
        assertThat(documentTextList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateDocumentTextWithPatch() throws Exception {
        // Initialize the database
        documentTextRepository.saveAndFlush(documentText);

        int databaseSizeBeforeUpdate = documentTextRepository.findAll().size();

        // Update the documentText using partial update
        DocumentText partialUpdatedDocumentText = new DocumentText();
        partialUpdatedDocumentText.setId(documentText.getId());

        partialUpdatedDocumentText.defaultText(UPDATED_DEFAULT_TEXT).text(UPDATED_TEXT).language(UPDATED_LANGUAGE).status(UPDATED_STATUS);

        restDocumentTextMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedDocumentText.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedDocumentText))
            )
            .andExpect(status().isOk());

        // Validate the DocumentText in the database
        List<DocumentText> documentTextList = documentTextRepository.findAll();
        assertThat(documentTextList).hasSize(databaseSizeBeforeUpdate);
        DocumentText testDocumentText = documentTextList.get(documentTextList.size() - 1);
        assertThat(testDocumentText.getDefaultText()).isEqualTo(UPDATED_DEFAULT_TEXT);
        assertThat(testDocumentText.getText()).isEqualTo(UPDATED_TEXT);
        assertThat(testDocumentText.getLanguage()).isEqualTo(UPDATED_LANGUAGE);
        assertThat(testDocumentText.getUsage()).isEqualTo(DEFAULT_USAGE);
        assertThat(testDocumentText.getStatus()).isEqualTo(UPDATED_STATUS);
        assertThat(testDocumentText.getType()).isEqualTo(DEFAULT_TYPE);
        assertThat(testDocumentText.getDocumentType()).isEqualTo(DEFAULT_DOCUMENT_TYPE);
    }

    @Test
    @Transactional
    void fullUpdateDocumentTextWithPatch() throws Exception {
        // Initialize the database
        documentTextRepository.saveAndFlush(documentText);

        int databaseSizeBeforeUpdate = documentTextRepository.findAll().size();

        // Update the documentText using partial update
        DocumentText partialUpdatedDocumentText = new DocumentText();
        partialUpdatedDocumentText.setId(documentText.getId());

        partialUpdatedDocumentText
            .defaultText(UPDATED_DEFAULT_TEXT)
            .text(UPDATED_TEXT)
            .language(UPDATED_LANGUAGE)
            .usage(UPDATED_USAGE)
            .status(UPDATED_STATUS)
            .type(UPDATED_TYPE)
            .documentType(UPDATED_DOCUMENT_TYPE);

        restDocumentTextMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedDocumentText.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedDocumentText))
            )
            .andExpect(status().isOk());

        // Validate the DocumentText in the database
        List<DocumentText> documentTextList = documentTextRepository.findAll();
        assertThat(documentTextList).hasSize(databaseSizeBeforeUpdate);
        DocumentText testDocumentText = documentTextList.get(documentTextList.size() - 1);
        assertThat(testDocumentText.getDefaultText()).isEqualTo(UPDATED_DEFAULT_TEXT);
        assertThat(testDocumentText.getText()).isEqualTo(UPDATED_TEXT);
        assertThat(testDocumentText.getLanguage()).isEqualTo(UPDATED_LANGUAGE);
        assertThat(testDocumentText.getUsage()).isEqualTo(UPDATED_USAGE);
        assertThat(testDocumentText.getStatus()).isEqualTo(UPDATED_STATUS);
        assertThat(testDocumentText.getType()).isEqualTo(UPDATED_TYPE);
        assertThat(testDocumentText.getDocumentType()).isEqualTo(UPDATED_DOCUMENT_TYPE);
    }

    @Test
    @Transactional
    void patchNonExistingDocumentText() throws Exception {
        int databaseSizeBeforeUpdate = documentTextRepository.findAll().size();
        documentText.setId(count.incrementAndGet());

        // Create the DocumentText
        DocumentTextDTO documentTextDTO = documentTextMapper.toDto(documentText);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restDocumentTextMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, documentTextDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(documentTextDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the DocumentText in the database
        List<DocumentText> documentTextList = documentTextRepository.findAll();
        assertThat(documentTextList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchDocumentText() throws Exception {
        int databaseSizeBeforeUpdate = documentTextRepository.findAll().size();
        documentText.setId(count.incrementAndGet());

        // Create the DocumentText
        DocumentTextDTO documentTextDTO = documentTextMapper.toDto(documentText);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restDocumentTextMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(documentTextDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the DocumentText in the database
        List<DocumentText> documentTextList = documentTextRepository.findAll();
        assertThat(documentTextList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamDocumentText() throws Exception {
        int databaseSizeBeforeUpdate = documentTextRepository.findAll().size();
        documentText.setId(count.incrementAndGet());

        // Create the DocumentText
        DocumentTextDTO documentTextDTO = documentTextMapper.toDto(documentText);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restDocumentTextMockMvc
            .perform(
                patch(ENTITY_API_URL)
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(documentTextDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the DocumentText in the database
        List<DocumentText> documentTextList = documentTextRepository.findAll();
        assertThat(documentTextList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteDocumentText() throws Exception {
        // Initialize the database
        documentTextRepository.saveAndFlush(documentText);

        int databaseSizeBeforeDelete = documentTextRepository.findAll().size();

        // Delete the documentText
        restDocumentTextMockMvc
            .perform(delete(ENTITY_API_URL_ID, documentText.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<DocumentText> documentTextList = documentTextRepository.findAll();
        assertThat(documentTextList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
