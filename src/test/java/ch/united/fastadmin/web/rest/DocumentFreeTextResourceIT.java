package ch.united.fastadmin.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import ch.united.fastadmin.IntegrationTest;
import ch.united.fastadmin.domain.DocumentFreeText;
import ch.united.fastadmin.repository.DocumentFreeTextRepository;
import ch.united.fastadmin.service.dto.DocumentFreeTextDTO;
import ch.united.fastadmin.service.mapper.DocumentFreeTextMapper;
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
 * Integration tests for the {@link DocumentFreeTextResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class DocumentFreeTextResourceIT {

    private static final String DEFAULT_TEXT = "AAAAAAAAAA";
    private static final String UPDATED_TEXT = "BBBBBBBBBB";

    private static final Integer DEFAULT_FONT_SIZE = 1;
    private static final Integer UPDATED_FONT_SIZE = 2;

    private static final Double DEFAULT_POSITION_X = 1D;
    private static final Double UPDATED_POSITION_X = 2D;

    private static final Double DEFAULT_POSITION_Y = 1D;
    private static final Double UPDATED_POSITION_Y = 2D;

    private static final Integer DEFAULT_PAGE_NO = 1;
    private static final Integer UPDATED_PAGE_NO = 2;

    private static final String ENTITY_API_URL = "/api/document-free-texts";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private DocumentFreeTextRepository documentFreeTextRepository;

    @Autowired
    private DocumentFreeTextMapper documentFreeTextMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restDocumentFreeTextMockMvc;

    private DocumentFreeText documentFreeText;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static DocumentFreeText createEntity(EntityManager em) {
        DocumentFreeText documentFreeText = new DocumentFreeText()
            .text(DEFAULT_TEXT)
            .fontSize(DEFAULT_FONT_SIZE)
            .positionX(DEFAULT_POSITION_X)
            .positionY(DEFAULT_POSITION_Y)
            .pageNo(DEFAULT_PAGE_NO);
        return documentFreeText;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static DocumentFreeText createUpdatedEntity(EntityManager em) {
        DocumentFreeText documentFreeText = new DocumentFreeText()
            .text(UPDATED_TEXT)
            .fontSize(UPDATED_FONT_SIZE)
            .positionX(UPDATED_POSITION_X)
            .positionY(UPDATED_POSITION_Y)
            .pageNo(UPDATED_PAGE_NO);
        return documentFreeText;
    }

    @BeforeEach
    public void initTest() {
        documentFreeText = createEntity(em);
    }

    @Test
    @Transactional
    void createDocumentFreeText() throws Exception {
        int databaseSizeBeforeCreate = documentFreeTextRepository.findAll().size();
        // Create the DocumentFreeText
        DocumentFreeTextDTO documentFreeTextDTO = documentFreeTextMapper.toDto(documentFreeText);
        restDocumentFreeTextMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(documentFreeTextDTO))
            )
            .andExpect(status().isCreated());

        // Validate the DocumentFreeText in the database
        List<DocumentFreeText> documentFreeTextList = documentFreeTextRepository.findAll();
        assertThat(documentFreeTextList).hasSize(databaseSizeBeforeCreate + 1);
        DocumentFreeText testDocumentFreeText = documentFreeTextList.get(documentFreeTextList.size() - 1);
        assertThat(testDocumentFreeText.getText()).isEqualTo(DEFAULT_TEXT);
        assertThat(testDocumentFreeText.getFontSize()).isEqualTo(DEFAULT_FONT_SIZE);
        assertThat(testDocumentFreeText.getPositionX()).isEqualTo(DEFAULT_POSITION_X);
        assertThat(testDocumentFreeText.getPositionY()).isEqualTo(DEFAULT_POSITION_Y);
        assertThat(testDocumentFreeText.getPageNo()).isEqualTo(DEFAULT_PAGE_NO);
    }

    @Test
    @Transactional
    void createDocumentFreeTextWithExistingId() throws Exception {
        // Create the DocumentFreeText with an existing ID
        documentFreeText.setId(1L);
        DocumentFreeTextDTO documentFreeTextDTO = documentFreeTextMapper.toDto(documentFreeText);

        int databaseSizeBeforeCreate = documentFreeTextRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restDocumentFreeTextMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(documentFreeTextDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the DocumentFreeText in the database
        List<DocumentFreeText> documentFreeTextList = documentFreeTextRepository.findAll();
        assertThat(documentFreeTextList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllDocumentFreeTexts() throws Exception {
        // Initialize the database
        documentFreeTextRepository.saveAndFlush(documentFreeText);

        // Get all the documentFreeTextList
        restDocumentFreeTextMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(documentFreeText.getId().intValue())))
            .andExpect(jsonPath("$.[*].text").value(hasItem(DEFAULT_TEXT)))
            .andExpect(jsonPath("$.[*].fontSize").value(hasItem(DEFAULT_FONT_SIZE)))
            .andExpect(jsonPath("$.[*].positionX").value(hasItem(DEFAULT_POSITION_X.doubleValue())))
            .andExpect(jsonPath("$.[*].positionY").value(hasItem(DEFAULT_POSITION_Y.doubleValue())))
            .andExpect(jsonPath("$.[*].pageNo").value(hasItem(DEFAULT_PAGE_NO)));
    }

    @Test
    @Transactional
    void getDocumentFreeText() throws Exception {
        // Initialize the database
        documentFreeTextRepository.saveAndFlush(documentFreeText);

        // Get the documentFreeText
        restDocumentFreeTextMockMvc
            .perform(get(ENTITY_API_URL_ID, documentFreeText.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(documentFreeText.getId().intValue()))
            .andExpect(jsonPath("$.text").value(DEFAULT_TEXT))
            .andExpect(jsonPath("$.fontSize").value(DEFAULT_FONT_SIZE))
            .andExpect(jsonPath("$.positionX").value(DEFAULT_POSITION_X.doubleValue()))
            .andExpect(jsonPath("$.positionY").value(DEFAULT_POSITION_Y.doubleValue()))
            .andExpect(jsonPath("$.pageNo").value(DEFAULT_PAGE_NO));
    }

    @Test
    @Transactional
    void getNonExistingDocumentFreeText() throws Exception {
        // Get the documentFreeText
        restDocumentFreeTextMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewDocumentFreeText() throws Exception {
        // Initialize the database
        documentFreeTextRepository.saveAndFlush(documentFreeText);

        int databaseSizeBeforeUpdate = documentFreeTextRepository.findAll().size();

        // Update the documentFreeText
        DocumentFreeText updatedDocumentFreeText = documentFreeTextRepository.findById(documentFreeText.getId()).get();
        // Disconnect from session so that the updates on updatedDocumentFreeText are not directly saved in db
        em.detach(updatedDocumentFreeText);
        updatedDocumentFreeText
            .text(UPDATED_TEXT)
            .fontSize(UPDATED_FONT_SIZE)
            .positionX(UPDATED_POSITION_X)
            .positionY(UPDATED_POSITION_Y)
            .pageNo(UPDATED_PAGE_NO);
        DocumentFreeTextDTO documentFreeTextDTO = documentFreeTextMapper.toDto(updatedDocumentFreeText);

        restDocumentFreeTextMockMvc
            .perform(
                put(ENTITY_API_URL_ID, documentFreeTextDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(documentFreeTextDTO))
            )
            .andExpect(status().isOk());

        // Validate the DocumentFreeText in the database
        List<DocumentFreeText> documentFreeTextList = documentFreeTextRepository.findAll();
        assertThat(documentFreeTextList).hasSize(databaseSizeBeforeUpdate);
        DocumentFreeText testDocumentFreeText = documentFreeTextList.get(documentFreeTextList.size() - 1);
        assertThat(testDocumentFreeText.getText()).isEqualTo(UPDATED_TEXT);
        assertThat(testDocumentFreeText.getFontSize()).isEqualTo(UPDATED_FONT_SIZE);
        assertThat(testDocumentFreeText.getPositionX()).isEqualTo(UPDATED_POSITION_X);
        assertThat(testDocumentFreeText.getPositionY()).isEqualTo(UPDATED_POSITION_Y);
        assertThat(testDocumentFreeText.getPageNo()).isEqualTo(UPDATED_PAGE_NO);
    }

    @Test
    @Transactional
    void putNonExistingDocumentFreeText() throws Exception {
        int databaseSizeBeforeUpdate = documentFreeTextRepository.findAll().size();
        documentFreeText.setId(count.incrementAndGet());

        // Create the DocumentFreeText
        DocumentFreeTextDTO documentFreeTextDTO = documentFreeTextMapper.toDto(documentFreeText);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restDocumentFreeTextMockMvc
            .perform(
                put(ENTITY_API_URL_ID, documentFreeTextDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(documentFreeTextDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the DocumentFreeText in the database
        List<DocumentFreeText> documentFreeTextList = documentFreeTextRepository.findAll();
        assertThat(documentFreeTextList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchDocumentFreeText() throws Exception {
        int databaseSizeBeforeUpdate = documentFreeTextRepository.findAll().size();
        documentFreeText.setId(count.incrementAndGet());

        // Create the DocumentFreeText
        DocumentFreeTextDTO documentFreeTextDTO = documentFreeTextMapper.toDto(documentFreeText);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restDocumentFreeTextMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(documentFreeTextDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the DocumentFreeText in the database
        List<DocumentFreeText> documentFreeTextList = documentFreeTextRepository.findAll();
        assertThat(documentFreeTextList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamDocumentFreeText() throws Exception {
        int databaseSizeBeforeUpdate = documentFreeTextRepository.findAll().size();
        documentFreeText.setId(count.incrementAndGet());

        // Create the DocumentFreeText
        DocumentFreeTextDTO documentFreeTextDTO = documentFreeTextMapper.toDto(documentFreeText);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restDocumentFreeTextMockMvc
            .perform(
                put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(documentFreeTextDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the DocumentFreeText in the database
        List<DocumentFreeText> documentFreeTextList = documentFreeTextRepository.findAll();
        assertThat(documentFreeTextList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateDocumentFreeTextWithPatch() throws Exception {
        // Initialize the database
        documentFreeTextRepository.saveAndFlush(documentFreeText);

        int databaseSizeBeforeUpdate = documentFreeTextRepository.findAll().size();

        // Update the documentFreeText using partial update
        DocumentFreeText partialUpdatedDocumentFreeText = new DocumentFreeText();
        partialUpdatedDocumentFreeText.setId(documentFreeText.getId());

        partialUpdatedDocumentFreeText.positionY(UPDATED_POSITION_Y).pageNo(UPDATED_PAGE_NO);

        restDocumentFreeTextMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedDocumentFreeText.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedDocumentFreeText))
            )
            .andExpect(status().isOk());

        // Validate the DocumentFreeText in the database
        List<DocumentFreeText> documentFreeTextList = documentFreeTextRepository.findAll();
        assertThat(documentFreeTextList).hasSize(databaseSizeBeforeUpdate);
        DocumentFreeText testDocumentFreeText = documentFreeTextList.get(documentFreeTextList.size() - 1);
        assertThat(testDocumentFreeText.getText()).isEqualTo(DEFAULT_TEXT);
        assertThat(testDocumentFreeText.getFontSize()).isEqualTo(DEFAULT_FONT_SIZE);
        assertThat(testDocumentFreeText.getPositionX()).isEqualTo(DEFAULT_POSITION_X);
        assertThat(testDocumentFreeText.getPositionY()).isEqualTo(UPDATED_POSITION_Y);
        assertThat(testDocumentFreeText.getPageNo()).isEqualTo(UPDATED_PAGE_NO);
    }

    @Test
    @Transactional
    void fullUpdateDocumentFreeTextWithPatch() throws Exception {
        // Initialize the database
        documentFreeTextRepository.saveAndFlush(documentFreeText);

        int databaseSizeBeforeUpdate = documentFreeTextRepository.findAll().size();

        // Update the documentFreeText using partial update
        DocumentFreeText partialUpdatedDocumentFreeText = new DocumentFreeText();
        partialUpdatedDocumentFreeText.setId(documentFreeText.getId());

        partialUpdatedDocumentFreeText
            .text(UPDATED_TEXT)
            .fontSize(UPDATED_FONT_SIZE)
            .positionX(UPDATED_POSITION_X)
            .positionY(UPDATED_POSITION_Y)
            .pageNo(UPDATED_PAGE_NO);

        restDocumentFreeTextMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedDocumentFreeText.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedDocumentFreeText))
            )
            .andExpect(status().isOk());

        // Validate the DocumentFreeText in the database
        List<DocumentFreeText> documentFreeTextList = documentFreeTextRepository.findAll();
        assertThat(documentFreeTextList).hasSize(databaseSizeBeforeUpdate);
        DocumentFreeText testDocumentFreeText = documentFreeTextList.get(documentFreeTextList.size() - 1);
        assertThat(testDocumentFreeText.getText()).isEqualTo(UPDATED_TEXT);
        assertThat(testDocumentFreeText.getFontSize()).isEqualTo(UPDATED_FONT_SIZE);
        assertThat(testDocumentFreeText.getPositionX()).isEqualTo(UPDATED_POSITION_X);
        assertThat(testDocumentFreeText.getPositionY()).isEqualTo(UPDATED_POSITION_Y);
        assertThat(testDocumentFreeText.getPageNo()).isEqualTo(UPDATED_PAGE_NO);
    }

    @Test
    @Transactional
    void patchNonExistingDocumentFreeText() throws Exception {
        int databaseSizeBeforeUpdate = documentFreeTextRepository.findAll().size();
        documentFreeText.setId(count.incrementAndGet());

        // Create the DocumentFreeText
        DocumentFreeTextDTO documentFreeTextDTO = documentFreeTextMapper.toDto(documentFreeText);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restDocumentFreeTextMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, documentFreeTextDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(documentFreeTextDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the DocumentFreeText in the database
        List<DocumentFreeText> documentFreeTextList = documentFreeTextRepository.findAll();
        assertThat(documentFreeTextList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchDocumentFreeText() throws Exception {
        int databaseSizeBeforeUpdate = documentFreeTextRepository.findAll().size();
        documentFreeText.setId(count.incrementAndGet());

        // Create the DocumentFreeText
        DocumentFreeTextDTO documentFreeTextDTO = documentFreeTextMapper.toDto(documentFreeText);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restDocumentFreeTextMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(documentFreeTextDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the DocumentFreeText in the database
        List<DocumentFreeText> documentFreeTextList = documentFreeTextRepository.findAll();
        assertThat(documentFreeTextList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamDocumentFreeText() throws Exception {
        int databaseSizeBeforeUpdate = documentFreeTextRepository.findAll().size();
        documentFreeText.setId(count.incrementAndGet());

        // Create the DocumentFreeText
        DocumentFreeTextDTO documentFreeTextDTO = documentFreeTextMapper.toDto(documentFreeText);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restDocumentFreeTextMockMvc
            .perform(
                patch(ENTITY_API_URL)
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(documentFreeTextDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the DocumentFreeText in the database
        List<DocumentFreeText> documentFreeTextList = documentFreeTextRepository.findAll();
        assertThat(documentFreeTextList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteDocumentFreeText() throws Exception {
        // Initialize the database
        documentFreeTextRepository.saveAndFlush(documentFreeText);

        int databaseSizeBeforeDelete = documentFreeTextRepository.findAll().size();

        // Delete the documentFreeText
        restDocumentFreeTextMockMvc
            .perform(delete(ENTITY_API_URL_ID, documentFreeText.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<DocumentFreeText> documentFreeTextList = documentFreeTextRepository.findAll();
        assertThat(documentFreeTextList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
