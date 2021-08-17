package ch.united.fastadmin.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import ch.united.fastadmin.IntegrationTest;
import ch.united.fastadmin.domain.DescriptiveDocumentText;
import ch.united.fastadmin.domain.enumeration.DocumentInvoiceTextStatus;
import ch.united.fastadmin.repository.DescriptiveDocumentTextRepository;
import ch.united.fastadmin.service.dto.DescriptiveDocumentTextDTO;
import ch.united.fastadmin.service.mapper.DescriptiveDocumentTextMapper;
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
 * Integration tests for the {@link DescriptiveDocumentTextResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class DescriptiveDocumentTextResourceIT {

    private static final String DEFAULT_TITLE = "AAAAAAAAAA";
    private static final String UPDATED_TITLE = "BBBBBBBBBB";

    private static final String DEFAULT_INTRODUCTION = "AAAAAAAAAA";
    private static final String UPDATED_INTRODUCTION = "BBBBBBBBBB";

    private static final String DEFAULT_CONDITIONS = "AAAAAAAAAA";
    private static final String UPDATED_CONDITIONS = "BBBBBBBBBB";

    private static final DocumentInvoiceTextStatus DEFAULT_STATUS = DocumentInvoiceTextStatus.DEFAULT;
    private static final DocumentInvoiceTextStatus UPDATED_STATUS = DocumentInvoiceTextStatus.PAYMENT_REMINDER;

    private static final String ENTITY_API_URL = "/api/descriptive-document-texts";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private DescriptiveDocumentTextRepository descriptiveDocumentTextRepository;

    @Autowired
    private DescriptiveDocumentTextMapper descriptiveDocumentTextMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restDescriptiveDocumentTextMockMvc;

    private DescriptiveDocumentText descriptiveDocumentText;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static DescriptiveDocumentText createEntity(EntityManager em) {
        DescriptiveDocumentText descriptiveDocumentText = new DescriptiveDocumentText()
            .title(DEFAULT_TITLE)
            .introduction(DEFAULT_INTRODUCTION)
            .conditions(DEFAULT_CONDITIONS)
            .status(DEFAULT_STATUS);
        return descriptiveDocumentText;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static DescriptiveDocumentText createUpdatedEntity(EntityManager em) {
        DescriptiveDocumentText descriptiveDocumentText = new DescriptiveDocumentText()
            .title(UPDATED_TITLE)
            .introduction(UPDATED_INTRODUCTION)
            .conditions(UPDATED_CONDITIONS)
            .status(UPDATED_STATUS);
        return descriptiveDocumentText;
    }

    @BeforeEach
    public void initTest() {
        descriptiveDocumentText = createEntity(em);
    }

    @Test
    @Transactional
    void createDescriptiveDocumentText() throws Exception {
        int databaseSizeBeforeCreate = descriptiveDocumentTextRepository.findAll().size();
        // Create the DescriptiveDocumentText
        DescriptiveDocumentTextDTO descriptiveDocumentTextDTO = descriptiveDocumentTextMapper.toDto(descriptiveDocumentText);
        restDescriptiveDocumentTextMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(descriptiveDocumentTextDTO))
            )
            .andExpect(status().isCreated());

        // Validate the DescriptiveDocumentText in the database
        List<DescriptiveDocumentText> descriptiveDocumentTextList = descriptiveDocumentTextRepository.findAll();
        assertThat(descriptiveDocumentTextList).hasSize(databaseSizeBeforeCreate + 1);
        DescriptiveDocumentText testDescriptiveDocumentText = descriptiveDocumentTextList.get(descriptiveDocumentTextList.size() - 1);
        assertThat(testDescriptiveDocumentText.getTitle()).isEqualTo(DEFAULT_TITLE);
        assertThat(testDescriptiveDocumentText.getIntroduction()).isEqualTo(DEFAULT_INTRODUCTION);
        assertThat(testDescriptiveDocumentText.getConditions()).isEqualTo(DEFAULT_CONDITIONS);
        assertThat(testDescriptiveDocumentText.getStatus()).isEqualTo(DEFAULT_STATUS);
    }

    @Test
    @Transactional
    void createDescriptiveDocumentTextWithExistingId() throws Exception {
        // Create the DescriptiveDocumentText with an existing ID
        descriptiveDocumentText.setId(1L);
        DescriptiveDocumentTextDTO descriptiveDocumentTextDTO = descriptiveDocumentTextMapper.toDto(descriptiveDocumentText);

        int databaseSizeBeforeCreate = descriptiveDocumentTextRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restDescriptiveDocumentTextMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(descriptiveDocumentTextDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the DescriptiveDocumentText in the database
        List<DescriptiveDocumentText> descriptiveDocumentTextList = descriptiveDocumentTextRepository.findAll();
        assertThat(descriptiveDocumentTextList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllDescriptiveDocumentTexts() throws Exception {
        // Initialize the database
        descriptiveDocumentTextRepository.saveAndFlush(descriptiveDocumentText);

        // Get all the descriptiveDocumentTextList
        restDescriptiveDocumentTextMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(descriptiveDocumentText.getId().intValue())))
            .andExpect(jsonPath("$.[*].title").value(hasItem(DEFAULT_TITLE)))
            .andExpect(jsonPath("$.[*].introduction").value(hasItem(DEFAULT_INTRODUCTION)))
            .andExpect(jsonPath("$.[*].conditions").value(hasItem(DEFAULT_CONDITIONS)))
            .andExpect(jsonPath("$.[*].status").value(hasItem(DEFAULT_STATUS.toString())));
    }

    @Test
    @Transactional
    void getDescriptiveDocumentText() throws Exception {
        // Initialize the database
        descriptiveDocumentTextRepository.saveAndFlush(descriptiveDocumentText);

        // Get the descriptiveDocumentText
        restDescriptiveDocumentTextMockMvc
            .perform(get(ENTITY_API_URL_ID, descriptiveDocumentText.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(descriptiveDocumentText.getId().intValue()))
            .andExpect(jsonPath("$.title").value(DEFAULT_TITLE))
            .andExpect(jsonPath("$.introduction").value(DEFAULT_INTRODUCTION))
            .andExpect(jsonPath("$.conditions").value(DEFAULT_CONDITIONS))
            .andExpect(jsonPath("$.status").value(DEFAULT_STATUS.toString()));
    }

    @Test
    @Transactional
    void getNonExistingDescriptiveDocumentText() throws Exception {
        // Get the descriptiveDocumentText
        restDescriptiveDocumentTextMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewDescriptiveDocumentText() throws Exception {
        // Initialize the database
        descriptiveDocumentTextRepository.saveAndFlush(descriptiveDocumentText);

        int databaseSizeBeforeUpdate = descriptiveDocumentTextRepository.findAll().size();

        // Update the descriptiveDocumentText
        DescriptiveDocumentText updatedDescriptiveDocumentText = descriptiveDocumentTextRepository
            .findById(descriptiveDocumentText.getId())
            .get();
        // Disconnect from session so that the updates on updatedDescriptiveDocumentText are not directly saved in db
        em.detach(updatedDescriptiveDocumentText);
        updatedDescriptiveDocumentText
            .title(UPDATED_TITLE)
            .introduction(UPDATED_INTRODUCTION)
            .conditions(UPDATED_CONDITIONS)
            .status(UPDATED_STATUS);
        DescriptiveDocumentTextDTO descriptiveDocumentTextDTO = descriptiveDocumentTextMapper.toDto(updatedDescriptiveDocumentText);

        restDescriptiveDocumentTextMockMvc
            .perform(
                put(ENTITY_API_URL_ID, descriptiveDocumentTextDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(descriptiveDocumentTextDTO))
            )
            .andExpect(status().isOk());

        // Validate the DescriptiveDocumentText in the database
        List<DescriptiveDocumentText> descriptiveDocumentTextList = descriptiveDocumentTextRepository.findAll();
        assertThat(descriptiveDocumentTextList).hasSize(databaseSizeBeforeUpdate);
        DescriptiveDocumentText testDescriptiveDocumentText = descriptiveDocumentTextList.get(descriptiveDocumentTextList.size() - 1);
        assertThat(testDescriptiveDocumentText.getTitle()).isEqualTo(UPDATED_TITLE);
        assertThat(testDescriptiveDocumentText.getIntroduction()).isEqualTo(UPDATED_INTRODUCTION);
        assertThat(testDescriptiveDocumentText.getConditions()).isEqualTo(UPDATED_CONDITIONS);
        assertThat(testDescriptiveDocumentText.getStatus()).isEqualTo(UPDATED_STATUS);
    }

    @Test
    @Transactional
    void putNonExistingDescriptiveDocumentText() throws Exception {
        int databaseSizeBeforeUpdate = descriptiveDocumentTextRepository.findAll().size();
        descriptiveDocumentText.setId(count.incrementAndGet());

        // Create the DescriptiveDocumentText
        DescriptiveDocumentTextDTO descriptiveDocumentTextDTO = descriptiveDocumentTextMapper.toDto(descriptiveDocumentText);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restDescriptiveDocumentTextMockMvc
            .perform(
                put(ENTITY_API_URL_ID, descriptiveDocumentTextDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(descriptiveDocumentTextDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the DescriptiveDocumentText in the database
        List<DescriptiveDocumentText> descriptiveDocumentTextList = descriptiveDocumentTextRepository.findAll();
        assertThat(descriptiveDocumentTextList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchDescriptiveDocumentText() throws Exception {
        int databaseSizeBeforeUpdate = descriptiveDocumentTextRepository.findAll().size();
        descriptiveDocumentText.setId(count.incrementAndGet());

        // Create the DescriptiveDocumentText
        DescriptiveDocumentTextDTO descriptiveDocumentTextDTO = descriptiveDocumentTextMapper.toDto(descriptiveDocumentText);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restDescriptiveDocumentTextMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(descriptiveDocumentTextDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the DescriptiveDocumentText in the database
        List<DescriptiveDocumentText> descriptiveDocumentTextList = descriptiveDocumentTextRepository.findAll();
        assertThat(descriptiveDocumentTextList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamDescriptiveDocumentText() throws Exception {
        int databaseSizeBeforeUpdate = descriptiveDocumentTextRepository.findAll().size();
        descriptiveDocumentText.setId(count.incrementAndGet());

        // Create the DescriptiveDocumentText
        DescriptiveDocumentTextDTO descriptiveDocumentTextDTO = descriptiveDocumentTextMapper.toDto(descriptiveDocumentText);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restDescriptiveDocumentTextMockMvc
            .perform(
                put(ENTITY_API_URL)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(descriptiveDocumentTextDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the DescriptiveDocumentText in the database
        List<DescriptiveDocumentText> descriptiveDocumentTextList = descriptiveDocumentTextRepository.findAll();
        assertThat(descriptiveDocumentTextList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateDescriptiveDocumentTextWithPatch() throws Exception {
        // Initialize the database
        descriptiveDocumentTextRepository.saveAndFlush(descriptiveDocumentText);

        int databaseSizeBeforeUpdate = descriptiveDocumentTextRepository.findAll().size();

        // Update the descriptiveDocumentText using partial update
        DescriptiveDocumentText partialUpdatedDescriptiveDocumentText = new DescriptiveDocumentText();
        partialUpdatedDescriptiveDocumentText.setId(descriptiveDocumentText.getId());

        partialUpdatedDescriptiveDocumentText.conditions(UPDATED_CONDITIONS).status(UPDATED_STATUS);

        restDescriptiveDocumentTextMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedDescriptiveDocumentText.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedDescriptiveDocumentText))
            )
            .andExpect(status().isOk());

        // Validate the DescriptiveDocumentText in the database
        List<DescriptiveDocumentText> descriptiveDocumentTextList = descriptiveDocumentTextRepository.findAll();
        assertThat(descriptiveDocumentTextList).hasSize(databaseSizeBeforeUpdate);
        DescriptiveDocumentText testDescriptiveDocumentText = descriptiveDocumentTextList.get(descriptiveDocumentTextList.size() - 1);
        assertThat(testDescriptiveDocumentText.getTitle()).isEqualTo(DEFAULT_TITLE);
        assertThat(testDescriptiveDocumentText.getIntroduction()).isEqualTo(DEFAULT_INTRODUCTION);
        assertThat(testDescriptiveDocumentText.getConditions()).isEqualTo(UPDATED_CONDITIONS);
        assertThat(testDescriptiveDocumentText.getStatus()).isEqualTo(UPDATED_STATUS);
    }

    @Test
    @Transactional
    void fullUpdateDescriptiveDocumentTextWithPatch() throws Exception {
        // Initialize the database
        descriptiveDocumentTextRepository.saveAndFlush(descriptiveDocumentText);

        int databaseSizeBeforeUpdate = descriptiveDocumentTextRepository.findAll().size();

        // Update the descriptiveDocumentText using partial update
        DescriptiveDocumentText partialUpdatedDescriptiveDocumentText = new DescriptiveDocumentText();
        partialUpdatedDescriptiveDocumentText.setId(descriptiveDocumentText.getId());

        partialUpdatedDescriptiveDocumentText
            .title(UPDATED_TITLE)
            .introduction(UPDATED_INTRODUCTION)
            .conditions(UPDATED_CONDITIONS)
            .status(UPDATED_STATUS);

        restDescriptiveDocumentTextMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedDescriptiveDocumentText.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedDescriptiveDocumentText))
            )
            .andExpect(status().isOk());

        // Validate the DescriptiveDocumentText in the database
        List<DescriptiveDocumentText> descriptiveDocumentTextList = descriptiveDocumentTextRepository.findAll();
        assertThat(descriptiveDocumentTextList).hasSize(databaseSizeBeforeUpdate);
        DescriptiveDocumentText testDescriptiveDocumentText = descriptiveDocumentTextList.get(descriptiveDocumentTextList.size() - 1);
        assertThat(testDescriptiveDocumentText.getTitle()).isEqualTo(UPDATED_TITLE);
        assertThat(testDescriptiveDocumentText.getIntroduction()).isEqualTo(UPDATED_INTRODUCTION);
        assertThat(testDescriptiveDocumentText.getConditions()).isEqualTo(UPDATED_CONDITIONS);
        assertThat(testDescriptiveDocumentText.getStatus()).isEqualTo(UPDATED_STATUS);
    }

    @Test
    @Transactional
    void patchNonExistingDescriptiveDocumentText() throws Exception {
        int databaseSizeBeforeUpdate = descriptiveDocumentTextRepository.findAll().size();
        descriptiveDocumentText.setId(count.incrementAndGet());

        // Create the DescriptiveDocumentText
        DescriptiveDocumentTextDTO descriptiveDocumentTextDTO = descriptiveDocumentTextMapper.toDto(descriptiveDocumentText);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restDescriptiveDocumentTextMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, descriptiveDocumentTextDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(descriptiveDocumentTextDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the DescriptiveDocumentText in the database
        List<DescriptiveDocumentText> descriptiveDocumentTextList = descriptiveDocumentTextRepository.findAll();
        assertThat(descriptiveDocumentTextList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchDescriptiveDocumentText() throws Exception {
        int databaseSizeBeforeUpdate = descriptiveDocumentTextRepository.findAll().size();
        descriptiveDocumentText.setId(count.incrementAndGet());

        // Create the DescriptiveDocumentText
        DescriptiveDocumentTextDTO descriptiveDocumentTextDTO = descriptiveDocumentTextMapper.toDto(descriptiveDocumentText);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restDescriptiveDocumentTextMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(descriptiveDocumentTextDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the DescriptiveDocumentText in the database
        List<DescriptiveDocumentText> descriptiveDocumentTextList = descriptiveDocumentTextRepository.findAll();
        assertThat(descriptiveDocumentTextList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamDescriptiveDocumentText() throws Exception {
        int databaseSizeBeforeUpdate = descriptiveDocumentTextRepository.findAll().size();
        descriptiveDocumentText.setId(count.incrementAndGet());

        // Create the DescriptiveDocumentText
        DescriptiveDocumentTextDTO descriptiveDocumentTextDTO = descriptiveDocumentTextMapper.toDto(descriptiveDocumentText);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restDescriptiveDocumentTextMockMvc
            .perform(
                patch(ENTITY_API_URL)
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(descriptiveDocumentTextDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the DescriptiveDocumentText in the database
        List<DescriptiveDocumentText> descriptiveDocumentTextList = descriptiveDocumentTextRepository.findAll();
        assertThat(descriptiveDocumentTextList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteDescriptiveDocumentText() throws Exception {
        // Initialize the database
        descriptiveDocumentTextRepository.saveAndFlush(descriptiveDocumentText);

        int databaseSizeBeforeDelete = descriptiveDocumentTextRepository.findAll().size();

        // Delete the descriptiveDocumentText
        restDescriptiveDocumentTextMockMvc
            .perform(delete(ENTITY_API_URL_ID, descriptiveDocumentText.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<DescriptiveDocumentText> descriptiveDocumentTextList = descriptiveDocumentTextRepository.findAll();
        assertThat(descriptiveDocumentTextList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
