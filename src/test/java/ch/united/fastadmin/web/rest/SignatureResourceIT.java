package ch.united.fastadmin.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import ch.united.fastadmin.IntegrationTest;
import ch.united.fastadmin.domain.Signature;
import ch.united.fastadmin.repository.SignatureRepository;
import ch.united.fastadmin.service.dto.SignatureDTO;
import ch.united.fastadmin.service.mapper.SignatureMapper;
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
import org.springframework.util.Base64Utils;

/**
 * Integration tests for the {@link SignatureResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class SignatureResourceIT {

    private static final Integer DEFAULT_REMOTE_ID = 1;
    private static final Integer UPDATED_REMOTE_ID = 2;

    private static final byte[] DEFAULT_SIGNATURE_IMAGE = TestUtil.createByteArray(1, "0");
    private static final byte[] UPDATED_SIGNATURE_IMAGE = TestUtil.createByteArray(1, "1");
    private static final String DEFAULT_SIGNATURE_IMAGE_CONTENT_TYPE = "image/jpg";
    private static final String UPDATED_SIGNATURE_IMAGE_CONTENT_TYPE = "image/png";

    private static final Integer DEFAULT_WIDTH = 1;
    private static final Integer UPDATED_WIDTH = 2;

    private static final Integer DEFAULT_HEIGTH = 1;
    private static final Integer UPDATED_HEIGTH = 2;

    private static final String DEFAULT_USER_NAME = "AAAAAAAAAA";
    private static final String UPDATED_USER_NAME = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/signatures";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private SignatureRepository signatureRepository;

    @Autowired
    private SignatureMapper signatureMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restSignatureMockMvc;

    private Signature signature;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Signature createEntity(EntityManager em) {
        Signature signature = new Signature()
            .remoteId(DEFAULT_REMOTE_ID)
            .signatureImage(DEFAULT_SIGNATURE_IMAGE)
            .signatureImageContentType(DEFAULT_SIGNATURE_IMAGE_CONTENT_TYPE)
            .width(DEFAULT_WIDTH)
            .heigth(DEFAULT_HEIGTH)
            .userName(DEFAULT_USER_NAME);
        return signature;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Signature createUpdatedEntity(EntityManager em) {
        Signature signature = new Signature()
            .remoteId(UPDATED_REMOTE_ID)
            .signatureImage(UPDATED_SIGNATURE_IMAGE)
            .signatureImageContentType(UPDATED_SIGNATURE_IMAGE_CONTENT_TYPE)
            .width(UPDATED_WIDTH)
            .heigth(UPDATED_HEIGTH)
            .userName(UPDATED_USER_NAME);
        return signature;
    }

    @BeforeEach
    public void initTest() {
        signature = createEntity(em);
    }

    @Test
    @Transactional
    void createSignature() throws Exception {
        int databaseSizeBeforeCreate = signatureRepository.findAll().size();
        // Create the Signature
        SignatureDTO signatureDTO = signatureMapper.toDto(signature);
        restSignatureMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(signatureDTO)))
            .andExpect(status().isCreated());

        // Validate the Signature in the database
        List<Signature> signatureList = signatureRepository.findAll();
        assertThat(signatureList).hasSize(databaseSizeBeforeCreate + 1);
        Signature testSignature = signatureList.get(signatureList.size() - 1);
        assertThat(testSignature.getRemoteId()).isEqualTo(DEFAULT_REMOTE_ID);
        assertThat(testSignature.getSignatureImage()).isEqualTo(DEFAULT_SIGNATURE_IMAGE);
        assertThat(testSignature.getSignatureImageContentType()).isEqualTo(DEFAULT_SIGNATURE_IMAGE_CONTENT_TYPE);
        assertThat(testSignature.getWidth()).isEqualTo(DEFAULT_WIDTH);
        assertThat(testSignature.getHeigth()).isEqualTo(DEFAULT_HEIGTH);
        assertThat(testSignature.getUserName()).isEqualTo(DEFAULT_USER_NAME);
    }

    @Test
    @Transactional
    void createSignatureWithExistingId() throws Exception {
        // Create the Signature with an existing ID
        signature.setId(1L);
        SignatureDTO signatureDTO = signatureMapper.toDto(signature);

        int databaseSizeBeforeCreate = signatureRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restSignatureMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(signatureDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Signature in the database
        List<Signature> signatureList = signatureRepository.findAll();
        assertThat(signatureList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllSignatures() throws Exception {
        // Initialize the database
        signatureRepository.saveAndFlush(signature);

        // Get all the signatureList
        restSignatureMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(signature.getId().intValue())))
            .andExpect(jsonPath("$.[*].remoteId").value(hasItem(DEFAULT_REMOTE_ID)))
            .andExpect(jsonPath("$.[*].signatureImageContentType").value(hasItem(DEFAULT_SIGNATURE_IMAGE_CONTENT_TYPE)))
            .andExpect(jsonPath("$.[*].signatureImage").value(hasItem(Base64Utils.encodeToString(DEFAULT_SIGNATURE_IMAGE))))
            .andExpect(jsonPath("$.[*].width").value(hasItem(DEFAULT_WIDTH)))
            .andExpect(jsonPath("$.[*].heigth").value(hasItem(DEFAULT_HEIGTH)))
            .andExpect(jsonPath("$.[*].userName").value(hasItem(DEFAULT_USER_NAME)));
    }

    @Test
    @Transactional
    void getSignature() throws Exception {
        // Initialize the database
        signatureRepository.saveAndFlush(signature);

        // Get the signature
        restSignatureMockMvc
            .perform(get(ENTITY_API_URL_ID, signature.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(signature.getId().intValue()))
            .andExpect(jsonPath("$.remoteId").value(DEFAULT_REMOTE_ID))
            .andExpect(jsonPath("$.signatureImageContentType").value(DEFAULT_SIGNATURE_IMAGE_CONTENT_TYPE))
            .andExpect(jsonPath("$.signatureImage").value(Base64Utils.encodeToString(DEFAULT_SIGNATURE_IMAGE)))
            .andExpect(jsonPath("$.width").value(DEFAULT_WIDTH))
            .andExpect(jsonPath("$.heigth").value(DEFAULT_HEIGTH))
            .andExpect(jsonPath("$.userName").value(DEFAULT_USER_NAME));
    }

    @Test
    @Transactional
    void getNonExistingSignature() throws Exception {
        // Get the signature
        restSignatureMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewSignature() throws Exception {
        // Initialize the database
        signatureRepository.saveAndFlush(signature);

        int databaseSizeBeforeUpdate = signatureRepository.findAll().size();

        // Update the signature
        Signature updatedSignature = signatureRepository.findById(signature.getId()).get();
        // Disconnect from session so that the updates on updatedSignature are not directly saved in db
        em.detach(updatedSignature);
        updatedSignature
            .remoteId(UPDATED_REMOTE_ID)
            .signatureImage(UPDATED_SIGNATURE_IMAGE)
            .signatureImageContentType(UPDATED_SIGNATURE_IMAGE_CONTENT_TYPE)
            .width(UPDATED_WIDTH)
            .heigth(UPDATED_HEIGTH)
            .userName(UPDATED_USER_NAME);
        SignatureDTO signatureDTO = signatureMapper.toDto(updatedSignature);

        restSignatureMockMvc
            .perform(
                put(ENTITY_API_URL_ID, signatureDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(signatureDTO))
            )
            .andExpect(status().isOk());

        // Validate the Signature in the database
        List<Signature> signatureList = signatureRepository.findAll();
        assertThat(signatureList).hasSize(databaseSizeBeforeUpdate);
        Signature testSignature = signatureList.get(signatureList.size() - 1);
        assertThat(testSignature.getRemoteId()).isEqualTo(UPDATED_REMOTE_ID);
        assertThat(testSignature.getSignatureImage()).isEqualTo(UPDATED_SIGNATURE_IMAGE);
        assertThat(testSignature.getSignatureImageContentType()).isEqualTo(UPDATED_SIGNATURE_IMAGE_CONTENT_TYPE);
        assertThat(testSignature.getWidth()).isEqualTo(UPDATED_WIDTH);
        assertThat(testSignature.getHeigth()).isEqualTo(UPDATED_HEIGTH);
        assertThat(testSignature.getUserName()).isEqualTo(UPDATED_USER_NAME);
    }

    @Test
    @Transactional
    void putNonExistingSignature() throws Exception {
        int databaseSizeBeforeUpdate = signatureRepository.findAll().size();
        signature.setId(count.incrementAndGet());

        // Create the Signature
        SignatureDTO signatureDTO = signatureMapper.toDto(signature);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restSignatureMockMvc
            .perform(
                put(ENTITY_API_URL_ID, signatureDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(signatureDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Signature in the database
        List<Signature> signatureList = signatureRepository.findAll();
        assertThat(signatureList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchSignature() throws Exception {
        int databaseSizeBeforeUpdate = signatureRepository.findAll().size();
        signature.setId(count.incrementAndGet());

        // Create the Signature
        SignatureDTO signatureDTO = signatureMapper.toDto(signature);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restSignatureMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(signatureDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Signature in the database
        List<Signature> signatureList = signatureRepository.findAll();
        assertThat(signatureList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamSignature() throws Exception {
        int databaseSizeBeforeUpdate = signatureRepository.findAll().size();
        signature.setId(count.incrementAndGet());

        // Create the Signature
        SignatureDTO signatureDTO = signatureMapper.toDto(signature);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restSignatureMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(signatureDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Signature in the database
        List<Signature> signatureList = signatureRepository.findAll();
        assertThat(signatureList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateSignatureWithPatch() throws Exception {
        // Initialize the database
        signatureRepository.saveAndFlush(signature);

        int databaseSizeBeforeUpdate = signatureRepository.findAll().size();

        // Update the signature using partial update
        Signature partialUpdatedSignature = new Signature();
        partialUpdatedSignature.setId(signature.getId());

        partialUpdatedSignature.remoteId(UPDATED_REMOTE_ID).width(UPDATED_WIDTH).heigth(UPDATED_HEIGTH);

        restSignatureMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedSignature.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedSignature))
            )
            .andExpect(status().isOk());

        // Validate the Signature in the database
        List<Signature> signatureList = signatureRepository.findAll();
        assertThat(signatureList).hasSize(databaseSizeBeforeUpdate);
        Signature testSignature = signatureList.get(signatureList.size() - 1);
        assertThat(testSignature.getRemoteId()).isEqualTo(UPDATED_REMOTE_ID);
        assertThat(testSignature.getSignatureImage()).isEqualTo(DEFAULT_SIGNATURE_IMAGE);
        assertThat(testSignature.getSignatureImageContentType()).isEqualTo(DEFAULT_SIGNATURE_IMAGE_CONTENT_TYPE);
        assertThat(testSignature.getWidth()).isEqualTo(UPDATED_WIDTH);
        assertThat(testSignature.getHeigth()).isEqualTo(UPDATED_HEIGTH);
        assertThat(testSignature.getUserName()).isEqualTo(DEFAULT_USER_NAME);
    }

    @Test
    @Transactional
    void fullUpdateSignatureWithPatch() throws Exception {
        // Initialize the database
        signatureRepository.saveAndFlush(signature);

        int databaseSizeBeforeUpdate = signatureRepository.findAll().size();

        // Update the signature using partial update
        Signature partialUpdatedSignature = new Signature();
        partialUpdatedSignature.setId(signature.getId());

        partialUpdatedSignature
            .remoteId(UPDATED_REMOTE_ID)
            .signatureImage(UPDATED_SIGNATURE_IMAGE)
            .signatureImageContentType(UPDATED_SIGNATURE_IMAGE_CONTENT_TYPE)
            .width(UPDATED_WIDTH)
            .heigth(UPDATED_HEIGTH)
            .userName(UPDATED_USER_NAME);

        restSignatureMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedSignature.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedSignature))
            )
            .andExpect(status().isOk());

        // Validate the Signature in the database
        List<Signature> signatureList = signatureRepository.findAll();
        assertThat(signatureList).hasSize(databaseSizeBeforeUpdate);
        Signature testSignature = signatureList.get(signatureList.size() - 1);
        assertThat(testSignature.getRemoteId()).isEqualTo(UPDATED_REMOTE_ID);
        assertThat(testSignature.getSignatureImage()).isEqualTo(UPDATED_SIGNATURE_IMAGE);
        assertThat(testSignature.getSignatureImageContentType()).isEqualTo(UPDATED_SIGNATURE_IMAGE_CONTENT_TYPE);
        assertThat(testSignature.getWidth()).isEqualTo(UPDATED_WIDTH);
        assertThat(testSignature.getHeigth()).isEqualTo(UPDATED_HEIGTH);
        assertThat(testSignature.getUserName()).isEqualTo(UPDATED_USER_NAME);
    }

    @Test
    @Transactional
    void patchNonExistingSignature() throws Exception {
        int databaseSizeBeforeUpdate = signatureRepository.findAll().size();
        signature.setId(count.incrementAndGet());

        // Create the Signature
        SignatureDTO signatureDTO = signatureMapper.toDto(signature);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restSignatureMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, signatureDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(signatureDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Signature in the database
        List<Signature> signatureList = signatureRepository.findAll();
        assertThat(signatureList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchSignature() throws Exception {
        int databaseSizeBeforeUpdate = signatureRepository.findAll().size();
        signature.setId(count.incrementAndGet());

        // Create the Signature
        SignatureDTO signatureDTO = signatureMapper.toDto(signature);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restSignatureMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(signatureDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Signature in the database
        List<Signature> signatureList = signatureRepository.findAll();
        assertThat(signatureList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamSignature() throws Exception {
        int databaseSizeBeforeUpdate = signatureRepository.findAll().size();
        signature.setId(count.incrementAndGet());

        // Create the Signature
        SignatureDTO signatureDTO = signatureMapper.toDto(signature);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restSignatureMockMvc
            .perform(
                patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(signatureDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the Signature in the database
        List<Signature> signatureList = signatureRepository.findAll();
        assertThat(signatureList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteSignature() throws Exception {
        // Initialize the database
        signatureRepository.saveAndFlush(signature);

        int databaseSizeBeforeDelete = signatureRepository.findAll().size();

        // Delete the signature
        restSignatureMockMvc
            .perform(delete(ENTITY_API_URL_ID, signature.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Signature> signatureList = signatureRepository.findAll();
        assertThat(signatureList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
