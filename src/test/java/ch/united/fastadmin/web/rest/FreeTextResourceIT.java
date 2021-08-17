package ch.united.fastadmin.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import ch.united.fastadmin.IntegrationTest;
import ch.united.fastadmin.domain.FreeText;
import ch.united.fastadmin.domain.enumeration.DocumentLanguage;
import ch.united.fastadmin.repository.FreeTextRepository;
import ch.united.fastadmin.service.dto.FreeTextDTO;
import ch.united.fastadmin.service.mapper.FreeTextMapper;
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
 * Integration tests for the {@link FreeTextResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class FreeTextResourceIT {

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

    private static final DocumentLanguage DEFAULT_LANGUAGE = DocumentLanguage.FRENCH;
    private static final DocumentLanguage UPDATED_LANGUAGE = DocumentLanguage.ENGLISH;

    private static final String ENTITY_API_URL = "/api/free-texts";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private FreeTextRepository freeTextRepository;

    @Autowired
    private FreeTextMapper freeTextMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restFreeTextMockMvc;

    private FreeText freeText;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static FreeText createEntity(EntityManager em) {
        FreeText freeText = new FreeText()
            .text(DEFAULT_TEXT)
            .fontSize(DEFAULT_FONT_SIZE)
            .positionX(DEFAULT_POSITION_X)
            .positionY(DEFAULT_POSITION_Y)
            .pageNo(DEFAULT_PAGE_NO)
            .language(DEFAULT_LANGUAGE);
        return freeText;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static FreeText createUpdatedEntity(EntityManager em) {
        FreeText freeText = new FreeText()
            .text(UPDATED_TEXT)
            .fontSize(UPDATED_FONT_SIZE)
            .positionX(UPDATED_POSITION_X)
            .positionY(UPDATED_POSITION_Y)
            .pageNo(UPDATED_PAGE_NO)
            .language(UPDATED_LANGUAGE);
        return freeText;
    }

    @BeforeEach
    public void initTest() {
        freeText = createEntity(em);
    }

    @Test
    @Transactional
    void createFreeText() throws Exception {
        int databaseSizeBeforeCreate = freeTextRepository.findAll().size();
        // Create the FreeText
        FreeTextDTO freeTextDTO = freeTextMapper.toDto(freeText);
        restFreeTextMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(freeTextDTO)))
            .andExpect(status().isCreated());

        // Validate the FreeText in the database
        List<FreeText> freeTextList = freeTextRepository.findAll();
        assertThat(freeTextList).hasSize(databaseSizeBeforeCreate + 1);
        FreeText testFreeText = freeTextList.get(freeTextList.size() - 1);
        assertThat(testFreeText.getText()).isEqualTo(DEFAULT_TEXT);
        assertThat(testFreeText.getFontSize()).isEqualTo(DEFAULT_FONT_SIZE);
        assertThat(testFreeText.getPositionX()).isEqualTo(DEFAULT_POSITION_X);
        assertThat(testFreeText.getPositionY()).isEqualTo(DEFAULT_POSITION_Y);
        assertThat(testFreeText.getPageNo()).isEqualTo(DEFAULT_PAGE_NO);
        assertThat(testFreeText.getLanguage()).isEqualTo(DEFAULT_LANGUAGE);
    }

    @Test
    @Transactional
    void createFreeTextWithExistingId() throws Exception {
        // Create the FreeText with an existing ID
        freeText.setId(1L);
        FreeTextDTO freeTextDTO = freeTextMapper.toDto(freeText);

        int databaseSizeBeforeCreate = freeTextRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restFreeTextMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(freeTextDTO)))
            .andExpect(status().isBadRequest());

        // Validate the FreeText in the database
        List<FreeText> freeTextList = freeTextRepository.findAll();
        assertThat(freeTextList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllFreeTexts() throws Exception {
        // Initialize the database
        freeTextRepository.saveAndFlush(freeText);

        // Get all the freeTextList
        restFreeTextMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(freeText.getId().intValue())))
            .andExpect(jsonPath("$.[*].text").value(hasItem(DEFAULT_TEXT)))
            .andExpect(jsonPath("$.[*].fontSize").value(hasItem(DEFAULT_FONT_SIZE)))
            .andExpect(jsonPath("$.[*].positionX").value(hasItem(DEFAULT_POSITION_X.doubleValue())))
            .andExpect(jsonPath("$.[*].positionY").value(hasItem(DEFAULT_POSITION_Y.doubleValue())))
            .andExpect(jsonPath("$.[*].pageNo").value(hasItem(DEFAULT_PAGE_NO)))
            .andExpect(jsonPath("$.[*].language").value(hasItem(DEFAULT_LANGUAGE.toString())));
    }

    @Test
    @Transactional
    void getFreeText() throws Exception {
        // Initialize the database
        freeTextRepository.saveAndFlush(freeText);

        // Get the freeText
        restFreeTextMockMvc
            .perform(get(ENTITY_API_URL_ID, freeText.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(freeText.getId().intValue()))
            .andExpect(jsonPath("$.text").value(DEFAULT_TEXT))
            .andExpect(jsonPath("$.fontSize").value(DEFAULT_FONT_SIZE))
            .andExpect(jsonPath("$.positionX").value(DEFAULT_POSITION_X.doubleValue()))
            .andExpect(jsonPath("$.positionY").value(DEFAULT_POSITION_Y.doubleValue()))
            .andExpect(jsonPath("$.pageNo").value(DEFAULT_PAGE_NO))
            .andExpect(jsonPath("$.language").value(DEFAULT_LANGUAGE.toString()));
    }

    @Test
    @Transactional
    void getNonExistingFreeText() throws Exception {
        // Get the freeText
        restFreeTextMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewFreeText() throws Exception {
        // Initialize the database
        freeTextRepository.saveAndFlush(freeText);

        int databaseSizeBeforeUpdate = freeTextRepository.findAll().size();

        // Update the freeText
        FreeText updatedFreeText = freeTextRepository.findById(freeText.getId()).get();
        // Disconnect from session so that the updates on updatedFreeText are not directly saved in db
        em.detach(updatedFreeText);
        updatedFreeText
            .text(UPDATED_TEXT)
            .fontSize(UPDATED_FONT_SIZE)
            .positionX(UPDATED_POSITION_X)
            .positionY(UPDATED_POSITION_Y)
            .pageNo(UPDATED_PAGE_NO)
            .language(UPDATED_LANGUAGE);
        FreeTextDTO freeTextDTO = freeTextMapper.toDto(updatedFreeText);

        restFreeTextMockMvc
            .perform(
                put(ENTITY_API_URL_ID, freeTextDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(freeTextDTO))
            )
            .andExpect(status().isOk());

        // Validate the FreeText in the database
        List<FreeText> freeTextList = freeTextRepository.findAll();
        assertThat(freeTextList).hasSize(databaseSizeBeforeUpdate);
        FreeText testFreeText = freeTextList.get(freeTextList.size() - 1);
        assertThat(testFreeText.getText()).isEqualTo(UPDATED_TEXT);
        assertThat(testFreeText.getFontSize()).isEqualTo(UPDATED_FONT_SIZE);
        assertThat(testFreeText.getPositionX()).isEqualTo(UPDATED_POSITION_X);
        assertThat(testFreeText.getPositionY()).isEqualTo(UPDATED_POSITION_Y);
        assertThat(testFreeText.getPageNo()).isEqualTo(UPDATED_PAGE_NO);
        assertThat(testFreeText.getLanguage()).isEqualTo(UPDATED_LANGUAGE);
    }

    @Test
    @Transactional
    void putNonExistingFreeText() throws Exception {
        int databaseSizeBeforeUpdate = freeTextRepository.findAll().size();
        freeText.setId(count.incrementAndGet());

        // Create the FreeText
        FreeTextDTO freeTextDTO = freeTextMapper.toDto(freeText);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restFreeTextMockMvc
            .perform(
                put(ENTITY_API_URL_ID, freeTextDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(freeTextDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the FreeText in the database
        List<FreeText> freeTextList = freeTextRepository.findAll();
        assertThat(freeTextList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchFreeText() throws Exception {
        int databaseSizeBeforeUpdate = freeTextRepository.findAll().size();
        freeText.setId(count.incrementAndGet());

        // Create the FreeText
        FreeTextDTO freeTextDTO = freeTextMapper.toDto(freeText);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restFreeTextMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(freeTextDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the FreeText in the database
        List<FreeText> freeTextList = freeTextRepository.findAll();
        assertThat(freeTextList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamFreeText() throws Exception {
        int databaseSizeBeforeUpdate = freeTextRepository.findAll().size();
        freeText.setId(count.incrementAndGet());

        // Create the FreeText
        FreeTextDTO freeTextDTO = freeTextMapper.toDto(freeText);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restFreeTextMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(freeTextDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the FreeText in the database
        List<FreeText> freeTextList = freeTextRepository.findAll();
        assertThat(freeTextList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateFreeTextWithPatch() throws Exception {
        // Initialize the database
        freeTextRepository.saveAndFlush(freeText);

        int databaseSizeBeforeUpdate = freeTextRepository.findAll().size();

        // Update the freeText using partial update
        FreeText partialUpdatedFreeText = new FreeText();
        partialUpdatedFreeText.setId(freeText.getId());

        partialUpdatedFreeText.text(UPDATED_TEXT).fontSize(UPDATED_FONT_SIZE).positionX(UPDATED_POSITION_X).positionY(UPDATED_POSITION_Y);

        restFreeTextMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedFreeText.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedFreeText))
            )
            .andExpect(status().isOk());

        // Validate the FreeText in the database
        List<FreeText> freeTextList = freeTextRepository.findAll();
        assertThat(freeTextList).hasSize(databaseSizeBeforeUpdate);
        FreeText testFreeText = freeTextList.get(freeTextList.size() - 1);
        assertThat(testFreeText.getText()).isEqualTo(UPDATED_TEXT);
        assertThat(testFreeText.getFontSize()).isEqualTo(UPDATED_FONT_SIZE);
        assertThat(testFreeText.getPositionX()).isEqualTo(UPDATED_POSITION_X);
        assertThat(testFreeText.getPositionY()).isEqualTo(UPDATED_POSITION_Y);
        assertThat(testFreeText.getPageNo()).isEqualTo(DEFAULT_PAGE_NO);
        assertThat(testFreeText.getLanguage()).isEqualTo(DEFAULT_LANGUAGE);
    }

    @Test
    @Transactional
    void fullUpdateFreeTextWithPatch() throws Exception {
        // Initialize the database
        freeTextRepository.saveAndFlush(freeText);

        int databaseSizeBeforeUpdate = freeTextRepository.findAll().size();

        // Update the freeText using partial update
        FreeText partialUpdatedFreeText = new FreeText();
        partialUpdatedFreeText.setId(freeText.getId());

        partialUpdatedFreeText
            .text(UPDATED_TEXT)
            .fontSize(UPDATED_FONT_SIZE)
            .positionX(UPDATED_POSITION_X)
            .positionY(UPDATED_POSITION_Y)
            .pageNo(UPDATED_PAGE_NO)
            .language(UPDATED_LANGUAGE);

        restFreeTextMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedFreeText.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedFreeText))
            )
            .andExpect(status().isOk());

        // Validate the FreeText in the database
        List<FreeText> freeTextList = freeTextRepository.findAll();
        assertThat(freeTextList).hasSize(databaseSizeBeforeUpdate);
        FreeText testFreeText = freeTextList.get(freeTextList.size() - 1);
        assertThat(testFreeText.getText()).isEqualTo(UPDATED_TEXT);
        assertThat(testFreeText.getFontSize()).isEqualTo(UPDATED_FONT_SIZE);
        assertThat(testFreeText.getPositionX()).isEqualTo(UPDATED_POSITION_X);
        assertThat(testFreeText.getPositionY()).isEqualTo(UPDATED_POSITION_Y);
        assertThat(testFreeText.getPageNo()).isEqualTo(UPDATED_PAGE_NO);
        assertThat(testFreeText.getLanguage()).isEqualTo(UPDATED_LANGUAGE);
    }

    @Test
    @Transactional
    void patchNonExistingFreeText() throws Exception {
        int databaseSizeBeforeUpdate = freeTextRepository.findAll().size();
        freeText.setId(count.incrementAndGet());

        // Create the FreeText
        FreeTextDTO freeTextDTO = freeTextMapper.toDto(freeText);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restFreeTextMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, freeTextDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(freeTextDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the FreeText in the database
        List<FreeText> freeTextList = freeTextRepository.findAll();
        assertThat(freeTextList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchFreeText() throws Exception {
        int databaseSizeBeforeUpdate = freeTextRepository.findAll().size();
        freeText.setId(count.incrementAndGet());

        // Create the FreeText
        FreeTextDTO freeTextDTO = freeTextMapper.toDto(freeText);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restFreeTextMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(freeTextDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the FreeText in the database
        List<FreeText> freeTextList = freeTextRepository.findAll();
        assertThat(freeTextList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamFreeText() throws Exception {
        int databaseSizeBeforeUpdate = freeTextRepository.findAll().size();
        freeText.setId(count.incrementAndGet());

        // Create the FreeText
        FreeTextDTO freeTextDTO = freeTextMapper.toDto(freeText);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restFreeTextMockMvc
            .perform(
                patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(freeTextDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the FreeText in the database
        List<FreeText> freeTextList = freeTextRepository.findAll();
        assertThat(freeTextList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteFreeText() throws Exception {
        // Initialize the database
        freeTextRepository.saveAndFlush(freeText);

        int databaseSizeBeforeDelete = freeTextRepository.findAll().size();

        // Delete the freeText
        restFreeTextMockMvc
            .perform(delete(ENTITY_API_URL_ID, freeText.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<FreeText> freeTextList = freeTextRepository.findAll();
        assertThat(freeTextList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
