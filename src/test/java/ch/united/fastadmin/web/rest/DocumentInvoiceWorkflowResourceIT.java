package ch.united.fastadmin.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import ch.united.fastadmin.IntegrationTest;
import ch.united.fastadmin.domain.DocumentInvoiceWorkflow;
import ch.united.fastadmin.domain.enumeration.InvoiceWorkflowStatus;
import ch.united.fastadmin.domain.enumeration.PostSpeed;
import ch.united.fastadmin.domain.enumeration.WorkflowAction;
import ch.united.fastadmin.repository.DocumentInvoiceWorkflowRepository;
import ch.united.fastadmin.service.dto.DocumentInvoiceWorkflowDTO;
import ch.united.fastadmin.service.mapper.DocumentInvoiceWorkflowMapper;
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
 * Integration tests for the {@link DocumentInvoiceWorkflowResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class DocumentInvoiceWorkflowResourceIT {

    private static final Boolean DEFAULT_ACTIVE = false;
    private static final Boolean UPDATED_ACTIVE = true;

    private static final InvoiceWorkflowStatus DEFAULT_STATUS = InvoiceWorkflowStatus.PAYMENT_REMINDER;
    private static final InvoiceWorkflowStatus UPDATED_STATUS = InvoiceWorkflowStatus.FIRST_REMINDER;

    private static final Integer DEFAULT_OVERDUE_DAYS = 1;
    private static final Integer UPDATED_OVERDUE_DAYS = 2;

    private static final WorkflowAction DEFAULT_ACTION = WorkflowAction.REMIND_ME;
    private static final WorkflowAction UPDATED_ACTION = WorkflowAction.REMIND_CONTACT_BY_EMAIL;

    private static final String DEFAULT_CONTACT_EMAIL = "AAAAAAAAAA";
    private static final String UPDATED_CONTACT_EMAIL = "BBBBBBBBBB";

    private static final PostSpeed DEFAULT_SPEED = PostSpeed.PRIORIRY;
    private static final PostSpeed UPDATED_SPEED = PostSpeed.ECONOMY;

    private static final String ENTITY_API_URL = "/api/document-invoice-workflows";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private DocumentInvoiceWorkflowRepository documentInvoiceWorkflowRepository;

    @Autowired
    private DocumentInvoiceWorkflowMapper documentInvoiceWorkflowMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restDocumentInvoiceWorkflowMockMvc;

    private DocumentInvoiceWorkflow documentInvoiceWorkflow;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static DocumentInvoiceWorkflow createEntity(EntityManager em) {
        DocumentInvoiceWorkflow documentInvoiceWorkflow = new DocumentInvoiceWorkflow()
            .active(DEFAULT_ACTIVE)
            .status(DEFAULT_STATUS)
            .overdueDays(DEFAULT_OVERDUE_DAYS)
            .action(DEFAULT_ACTION)
            .contactEmail(DEFAULT_CONTACT_EMAIL)
            .speed(DEFAULT_SPEED);
        return documentInvoiceWorkflow;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static DocumentInvoiceWorkflow createUpdatedEntity(EntityManager em) {
        DocumentInvoiceWorkflow documentInvoiceWorkflow = new DocumentInvoiceWorkflow()
            .active(UPDATED_ACTIVE)
            .status(UPDATED_STATUS)
            .overdueDays(UPDATED_OVERDUE_DAYS)
            .action(UPDATED_ACTION)
            .contactEmail(UPDATED_CONTACT_EMAIL)
            .speed(UPDATED_SPEED);
        return documentInvoiceWorkflow;
    }

    @BeforeEach
    public void initTest() {
        documentInvoiceWorkflow = createEntity(em);
    }

    @Test
    @Transactional
    void createDocumentInvoiceWorkflow() throws Exception {
        int databaseSizeBeforeCreate = documentInvoiceWorkflowRepository.findAll().size();
        // Create the DocumentInvoiceWorkflow
        DocumentInvoiceWorkflowDTO documentInvoiceWorkflowDTO = documentInvoiceWorkflowMapper.toDto(documentInvoiceWorkflow);
        restDocumentInvoiceWorkflowMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(documentInvoiceWorkflowDTO))
            )
            .andExpect(status().isCreated());

        // Validate the DocumentInvoiceWorkflow in the database
        List<DocumentInvoiceWorkflow> documentInvoiceWorkflowList = documentInvoiceWorkflowRepository.findAll();
        assertThat(documentInvoiceWorkflowList).hasSize(databaseSizeBeforeCreate + 1);
        DocumentInvoiceWorkflow testDocumentInvoiceWorkflow = documentInvoiceWorkflowList.get(documentInvoiceWorkflowList.size() - 1);
        assertThat(testDocumentInvoiceWorkflow.getActive()).isEqualTo(DEFAULT_ACTIVE);
        assertThat(testDocumentInvoiceWorkflow.getStatus()).isEqualTo(DEFAULT_STATUS);
        assertThat(testDocumentInvoiceWorkflow.getOverdueDays()).isEqualTo(DEFAULT_OVERDUE_DAYS);
        assertThat(testDocumentInvoiceWorkflow.getAction()).isEqualTo(DEFAULT_ACTION);
        assertThat(testDocumentInvoiceWorkflow.getContactEmail()).isEqualTo(DEFAULT_CONTACT_EMAIL);
        assertThat(testDocumentInvoiceWorkflow.getSpeed()).isEqualTo(DEFAULT_SPEED);
    }

    @Test
    @Transactional
    void createDocumentInvoiceWorkflowWithExistingId() throws Exception {
        // Create the DocumentInvoiceWorkflow with an existing ID
        documentInvoiceWorkflow.setId(1L);
        DocumentInvoiceWorkflowDTO documentInvoiceWorkflowDTO = documentInvoiceWorkflowMapper.toDto(documentInvoiceWorkflow);

        int databaseSizeBeforeCreate = documentInvoiceWorkflowRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restDocumentInvoiceWorkflowMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(documentInvoiceWorkflowDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the DocumentInvoiceWorkflow in the database
        List<DocumentInvoiceWorkflow> documentInvoiceWorkflowList = documentInvoiceWorkflowRepository.findAll();
        assertThat(documentInvoiceWorkflowList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllDocumentInvoiceWorkflows() throws Exception {
        // Initialize the database
        documentInvoiceWorkflowRepository.saveAndFlush(documentInvoiceWorkflow);

        // Get all the documentInvoiceWorkflowList
        restDocumentInvoiceWorkflowMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(documentInvoiceWorkflow.getId().intValue())))
            .andExpect(jsonPath("$.[*].active").value(hasItem(DEFAULT_ACTIVE.booleanValue())))
            .andExpect(jsonPath("$.[*].status").value(hasItem(DEFAULT_STATUS.toString())))
            .andExpect(jsonPath("$.[*].overdueDays").value(hasItem(DEFAULT_OVERDUE_DAYS)))
            .andExpect(jsonPath("$.[*].action").value(hasItem(DEFAULT_ACTION.toString())))
            .andExpect(jsonPath("$.[*].contactEmail").value(hasItem(DEFAULT_CONTACT_EMAIL)))
            .andExpect(jsonPath("$.[*].speed").value(hasItem(DEFAULT_SPEED.toString())));
    }

    @Test
    @Transactional
    void getDocumentInvoiceWorkflow() throws Exception {
        // Initialize the database
        documentInvoiceWorkflowRepository.saveAndFlush(documentInvoiceWorkflow);

        // Get the documentInvoiceWorkflow
        restDocumentInvoiceWorkflowMockMvc
            .perform(get(ENTITY_API_URL_ID, documentInvoiceWorkflow.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(documentInvoiceWorkflow.getId().intValue()))
            .andExpect(jsonPath("$.active").value(DEFAULT_ACTIVE.booleanValue()))
            .andExpect(jsonPath("$.status").value(DEFAULT_STATUS.toString()))
            .andExpect(jsonPath("$.overdueDays").value(DEFAULT_OVERDUE_DAYS))
            .andExpect(jsonPath("$.action").value(DEFAULT_ACTION.toString()))
            .andExpect(jsonPath("$.contactEmail").value(DEFAULT_CONTACT_EMAIL))
            .andExpect(jsonPath("$.speed").value(DEFAULT_SPEED.toString()));
    }

    @Test
    @Transactional
    void getNonExistingDocumentInvoiceWorkflow() throws Exception {
        // Get the documentInvoiceWorkflow
        restDocumentInvoiceWorkflowMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewDocumentInvoiceWorkflow() throws Exception {
        // Initialize the database
        documentInvoiceWorkflowRepository.saveAndFlush(documentInvoiceWorkflow);

        int databaseSizeBeforeUpdate = documentInvoiceWorkflowRepository.findAll().size();

        // Update the documentInvoiceWorkflow
        DocumentInvoiceWorkflow updatedDocumentInvoiceWorkflow = documentInvoiceWorkflowRepository
            .findById(documentInvoiceWorkflow.getId())
            .get();
        // Disconnect from session so that the updates on updatedDocumentInvoiceWorkflow are not directly saved in db
        em.detach(updatedDocumentInvoiceWorkflow);
        updatedDocumentInvoiceWorkflow
            .active(UPDATED_ACTIVE)
            .status(UPDATED_STATUS)
            .overdueDays(UPDATED_OVERDUE_DAYS)
            .action(UPDATED_ACTION)
            .contactEmail(UPDATED_CONTACT_EMAIL)
            .speed(UPDATED_SPEED);
        DocumentInvoiceWorkflowDTO documentInvoiceWorkflowDTO = documentInvoiceWorkflowMapper.toDto(updatedDocumentInvoiceWorkflow);

        restDocumentInvoiceWorkflowMockMvc
            .perform(
                put(ENTITY_API_URL_ID, documentInvoiceWorkflowDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(documentInvoiceWorkflowDTO))
            )
            .andExpect(status().isOk());

        // Validate the DocumentInvoiceWorkflow in the database
        List<DocumentInvoiceWorkflow> documentInvoiceWorkflowList = documentInvoiceWorkflowRepository.findAll();
        assertThat(documentInvoiceWorkflowList).hasSize(databaseSizeBeforeUpdate);
        DocumentInvoiceWorkflow testDocumentInvoiceWorkflow = documentInvoiceWorkflowList.get(documentInvoiceWorkflowList.size() - 1);
        assertThat(testDocumentInvoiceWorkflow.getActive()).isEqualTo(UPDATED_ACTIVE);
        assertThat(testDocumentInvoiceWorkflow.getStatus()).isEqualTo(UPDATED_STATUS);
        assertThat(testDocumentInvoiceWorkflow.getOverdueDays()).isEqualTo(UPDATED_OVERDUE_DAYS);
        assertThat(testDocumentInvoiceWorkflow.getAction()).isEqualTo(UPDATED_ACTION);
        assertThat(testDocumentInvoiceWorkflow.getContactEmail()).isEqualTo(UPDATED_CONTACT_EMAIL);
        assertThat(testDocumentInvoiceWorkflow.getSpeed()).isEqualTo(UPDATED_SPEED);
    }

    @Test
    @Transactional
    void putNonExistingDocumentInvoiceWorkflow() throws Exception {
        int databaseSizeBeforeUpdate = documentInvoiceWorkflowRepository.findAll().size();
        documentInvoiceWorkflow.setId(count.incrementAndGet());

        // Create the DocumentInvoiceWorkflow
        DocumentInvoiceWorkflowDTO documentInvoiceWorkflowDTO = documentInvoiceWorkflowMapper.toDto(documentInvoiceWorkflow);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restDocumentInvoiceWorkflowMockMvc
            .perform(
                put(ENTITY_API_URL_ID, documentInvoiceWorkflowDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(documentInvoiceWorkflowDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the DocumentInvoiceWorkflow in the database
        List<DocumentInvoiceWorkflow> documentInvoiceWorkflowList = documentInvoiceWorkflowRepository.findAll();
        assertThat(documentInvoiceWorkflowList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchDocumentInvoiceWorkflow() throws Exception {
        int databaseSizeBeforeUpdate = documentInvoiceWorkflowRepository.findAll().size();
        documentInvoiceWorkflow.setId(count.incrementAndGet());

        // Create the DocumentInvoiceWorkflow
        DocumentInvoiceWorkflowDTO documentInvoiceWorkflowDTO = documentInvoiceWorkflowMapper.toDto(documentInvoiceWorkflow);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restDocumentInvoiceWorkflowMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(documentInvoiceWorkflowDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the DocumentInvoiceWorkflow in the database
        List<DocumentInvoiceWorkflow> documentInvoiceWorkflowList = documentInvoiceWorkflowRepository.findAll();
        assertThat(documentInvoiceWorkflowList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamDocumentInvoiceWorkflow() throws Exception {
        int databaseSizeBeforeUpdate = documentInvoiceWorkflowRepository.findAll().size();
        documentInvoiceWorkflow.setId(count.incrementAndGet());

        // Create the DocumentInvoiceWorkflow
        DocumentInvoiceWorkflowDTO documentInvoiceWorkflowDTO = documentInvoiceWorkflowMapper.toDto(documentInvoiceWorkflow);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restDocumentInvoiceWorkflowMockMvc
            .perform(
                put(ENTITY_API_URL)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(documentInvoiceWorkflowDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the DocumentInvoiceWorkflow in the database
        List<DocumentInvoiceWorkflow> documentInvoiceWorkflowList = documentInvoiceWorkflowRepository.findAll();
        assertThat(documentInvoiceWorkflowList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateDocumentInvoiceWorkflowWithPatch() throws Exception {
        // Initialize the database
        documentInvoiceWorkflowRepository.saveAndFlush(documentInvoiceWorkflow);

        int databaseSizeBeforeUpdate = documentInvoiceWorkflowRepository.findAll().size();

        // Update the documentInvoiceWorkflow using partial update
        DocumentInvoiceWorkflow partialUpdatedDocumentInvoiceWorkflow = new DocumentInvoiceWorkflow();
        partialUpdatedDocumentInvoiceWorkflow.setId(documentInvoiceWorkflow.getId());

        partialUpdatedDocumentInvoiceWorkflow
            .active(UPDATED_ACTIVE)
            .status(UPDATED_STATUS)
            .action(UPDATED_ACTION)
            .contactEmail(UPDATED_CONTACT_EMAIL);

        restDocumentInvoiceWorkflowMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedDocumentInvoiceWorkflow.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedDocumentInvoiceWorkflow))
            )
            .andExpect(status().isOk());

        // Validate the DocumentInvoiceWorkflow in the database
        List<DocumentInvoiceWorkflow> documentInvoiceWorkflowList = documentInvoiceWorkflowRepository.findAll();
        assertThat(documentInvoiceWorkflowList).hasSize(databaseSizeBeforeUpdate);
        DocumentInvoiceWorkflow testDocumentInvoiceWorkflow = documentInvoiceWorkflowList.get(documentInvoiceWorkflowList.size() - 1);
        assertThat(testDocumentInvoiceWorkflow.getActive()).isEqualTo(UPDATED_ACTIVE);
        assertThat(testDocumentInvoiceWorkflow.getStatus()).isEqualTo(UPDATED_STATUS);
        assertThat(testDocumentInvoiceWorkflow.getOverdueDays()).isEqualTo(DEFAULT_OVERDUE_DAYS);
        assertThat(testDocumentInvoiceWorkflow.getAction()).isEqualTo(UPDATED_ACTION);
        assertThat(testDocumentInvoiceWorkflow.getContactEmail()).isEqualTo(UPDATED_CONTACT_EMAIL);
        assertThat(testDocumentInvoiceWorkflow.getSpeed()).isEqualTo(DEFAULT_SPEED);
    }

    @Test
    @Transactional
    void fullUpdateDocumentInvoiceWorkflowWithPatch() throws Exception {
        // Initialize the database
        documentInvoiceWorkflowRepository.saveAndFlush(documentInvoiceWorkflow);

        int databaseSizeBeforeUpdate = documentInvoiceWorkflowRepository.findAll().size();

        // Update the documentInvoiceWorkflow using partial update
        DocumentInvoiceWorkflow partialUpdatedDocumentInvoiceWorkflow = new DocumentInvoiceWorkflow();
        partialUpdatedDocumentInvoiceWorkflow.setId(documentInvoiceWorkflow.getId());

        partialUpdatedDocumentInvoiceWorkflow
            .active(UPDATED_ACTIVE)
            .status(UPDATED_STATUS)
            .overdueDays(UPDATED_OVERDUE_DAYS)
            .action(UPDATED_ACTION)
            .contactEmail(UPDATED_CONTACT_EMAIL)
            .speed(UPDATED_SPEED);

        restDocumentInvoiceWorkflowMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedDocumentInvoiceWorkflow.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedDocumentInvoiceWorkflow))
            )
            .andExpect(status().isOk());

        // Validate the DocumentInvoiceWorkflow in the database
        List<DocumentInvoiceWorkflow> documentInvoiceWorkflowList = documentInvoiceWorkflowRepository.findAll();
        assertThat(documentInvoiceWorkflowList).hasSize(databaseSizeBeforeUpdate);
        DocumentInvoiceWorkflow testDocumentInvoiceWorkflow = documentInvoiceWorkflowList.get(documentInvoiceWorkflowList.size() - 1);
        assertThat(testDocumentInvoiceWorkflow.getActive()).isEqualTo(UPDATED_ACTIVE);
        assertThat(testDocumentInvoiceWorkflow.getStatus()).isEqualTo(UPDATED_STATUS);
        assertThat(testDocumentInvoiceWorkflow.getOverdueDays()).isEqualTo(UPDATED_OVERDUE_DAYS);
        assertThat(testDocumentInvoiceWorkflow.getAction()).isEqualTo(UPDATED_ACTION);
        assertThat(testDocumentInvoiceWorkflow.getContactEmail()).isEqualTo(UPDATED_CONTACT_EMAIL);
        assertThat(testDocumentInvoiceWorkflow.getSpeed()).isEqualTo(UPDATED_SPEED);
    }

    @Test
    @Transactional
    void patchNonExistingDocumentInvoiceWorkflow() throws Exception {
        int databaseSizeBeforeUpdate = documentInvoiceWorkflowRepository.findAll().size();
        documentInvoiceWorkflow.setId(count.incrementAndGet());

        // Create the DocumentInvoiceWorkflow
        DocumentInvoiceWorkflowDTO documentInvoiceWorkflowDTO = documentInvoiceWorkflowMapper.toDto(documentInvoiceWorkflow);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restDocumentInvoiceWorkflowMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, documentInvoiceWorkflowDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(documentInvoiceWorkflowDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the DocumentInvoiceWorkflow in the database
        List<DocumentInvoiceWorkflow> documentInvoiceWorkflowList = documentInvoiceWorkflowRepository.findAll();
        assertThat(documentInvoiceWorkflowList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchDocumentInvoiceWorkflow() throws Exception {
        int databaseSizeBeforeUpdate = documentInvoiceWorkflowRepository.findAll().size();
        documentInvoiceWorkflow.setId(count.incrementAndGet());

        // Create the DocumentInvoiceWorkflow
        DocumentInvoiceWorkflowDTO documentInvoiceWorkflowDTO = documentInvoiceWorkflowMapper.toDto(documentInvoiceWorkflow);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restDocumentInvoiceWorkflowMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(documentInvoiceWorkflowDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the DocumentInvoiceWorkflow in the database
        List<DocumentInvoiceWorkflow> documentInvoiceWorkflowList = documentInvoiceWorkflowRepository.findAll();
        assertThat(documentInvoiceWorkflowList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamDocumentInvoiceWorkflow() throws Exception {
        int databaseSizeBeforeUpdate = documentInvoiceWorkflowRepository.findAll().size();
        documentInvoiceWorkflow.setId(count.incrementAndGet());

        // Create the DocumentInvoiceWorkflow
        DocumentInvoiceWorkflowDTO documentInvoiceWorkflowDTO = documentInvoiceWorkflowMapper.toDto(documentInvoiceWorkflow);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restDocumentInvoiceWorkflowMockMvc
            .perform(
                patch(ENTITY_API_URL)
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(documentInvoiceWorkflowDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the DocumentInvoiceWorkflow in the database
        List<DocumentInvoiceWorkflow> documentInvoiceWorkflowList = documentInvoiceWorkflowRepository.findAll();
        assertThat(documentInvoiceWorkflowList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteDocumentInvoiceWorkflow() throws Exception {
        // Initialize the database
        documentInvoiceWorkflowRepository.saveAndFlush(documentInvoiceWorkflow);

        int databaseSizeBeforeDelete = documentInvoiceWorkflowRepository.findAll().size();

        // Delete the documentInvoiceWorkflow
        restDocumentInvoiceWorkflowMockMvc
            .perform(delete(ENTITY_API_URL_ID, documentInvoiceWorkflow.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<DocumentInvoiceWorkflow> documentInvoiceWorkflowList = documentInvoiceWorkflowRepository.findAll();
        assertThat(documentInvoiceWorkflowList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
