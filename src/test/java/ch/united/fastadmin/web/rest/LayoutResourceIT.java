package ch.united.fastadmin.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import ch.united.fastadmin.IntegrationTest;
import ch.united.fastadmin.domain.Layout;
import ch.united.fastadmin.repository.LayoutRepository;
import ch.united.fastadmin.service.dto.LayoutDTO;
import ch.united.fastadmin.service.mapper.LayoutMapper;
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
 * Integration tests for the {@link LayoutResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class LayoutResourceIT {

    private static final Integer DEFAULT_REMOTE_ID = 1;
    private static final Integer UPDATED_REMOTE_ID = 2;

    private static final String ENTITY_API_URL = "/api/layouts";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private LayoutRepository layoutRepository;

    @Autowired
    private LayoutMapper layoutMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restLayoutMockMvc;

    private Layout layout;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Layout createEntity(EntityManager em) {
        Layout layout = new Layout().remoteId(DEFAULT_REMOTE_ID);
        return layout;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Layout createUpdatedEntity(EntityManager em) {
        Layout layout = new Layout().remoteId(UPDATED_REMOTE_ID);
        return layout;
    }

    @BeforeEach
    public void initTest() {
        layout = createEntity(em);
    }

    @Test
    @Transactional
    void createLayout() throws Exception {
        int databaseSizeBeforeCreate = layoutRepository.findAll().size();
        // Create the Layout
        LayoutDTO layoutDTO = layoutMapper.toDto(layout);
        restLayoutMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(layoutDTO)))
            .andExpect(status().isCreated());

        // Validate the Layout in the database
        List<Layout> layoutList = layoutRepository.findAll();
        assertThat(layoutList).hasSize(databaseSizeBeforeCreate + 1);
        Layout testLayout = layoutList.get(layoutList.size() - 1);
        assertThat(testLayout.getRemoteId()).isEqualTo(DEFAULT_REMOTE_ID);
    }

    @Test
    @Transactional
    void createLayoutWithExistingId() throws Exception {
        // Create the Layout with an existing ID
        layout.setId(1L);
        LayoutDTO layoutDTO = layoutMapper.toDto(layout);

        int databaseSizeBeforeCreate = layoutRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restLayoutMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(layoutDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Layout in the database
        List<Layout> layoutList = layoutRepository.findAll();
        assertThat(layoutList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllLayouts() throws Exception {
        // Initialize the database
        layoutRepository.saveAndFlush(layout);

        // Get all the layoutList
        restLayoutMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(layout.getId().intValue())))
            .andExpect(jsonPath("$.[*].remoteId").value(hasItem(DEFAULT_REMOTE_ID)));
    }

    @Test
    @Transactional
    void getLayout() throws Exception {
        // Initialize the database
        layoutRepository.saveAndFlush(layout);

        // Get the layout
        restLayoutMockMvc
            .perform(get(ENTITY_API_URL_ID, layout.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(layout.getId().intValue()))
            .andExpect(jsonPath("$.remoteId").value(DEFAULT_REMOTE_ID));
    }

    @Test
    @Transactional
    void getNonExistingLayout() throws Exception {
        // Get the layout
        restLayoutMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewLayout() throws Exception {
        // Initialize the database
        layoutRepository.saveAndFlush(layout);

        int databaseSizeBeforeUpdate = layoutRepository.findAll().size();

        // Update the layout
        Layout updatedLayout = layoutRepository.findById(layout.getId()).get();
        // Disconnect from session so that the updates on updatedLayout are not directly saved in db
        em.detach(updatedLayout);
        updatedLayout.remoteId(UPDATED_REMOTE_ID);
        LayoutDTO layoutDTO = layoutMapper.toDto(updatedLayout);

        restLayoutMockMvc
            .perform(
                put(ENTITY_API_URL_ID, layoutDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(layoutDTO))
            )
            .andExpect(status().isOk());

        // Validate the Layout in the database
        List<Layout> layoutList = layoutRepository.findAll();
        assertThat(layoutList).hasSize(databaseSizeBeforeUpdate);
        Layout testLayout = layoutList.get(layoutList.size() - 1);
        assertThat(testLayout.getRemoteId()).isEqualTo(UPDATED_REMOTE_ID);
    }

    @Test
    @Transactional
    void putNonExistingLayout() throws Exception {
        int databaseSizeBeforeUpdate = layoutRepository.findAll().size();
        layout.setId(count.incrementAndGet());

        // Create the Layout
        LayoutDTO layoutDTO = layoutMapper.toDto(layout);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restLayoutMockMvc
            .perform(
                put(ENTITY_API_URL_ID, layoutDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(layoutDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Layout in the database
        List<Layout> layoutList = layoutRepository.findAll();
        assertThat(layoutList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchLayout() throws Exception {
        int databaseSizeBeforeUpdate = layoutRepository.findAll().size();
        layout.setId(count.incrementAndGet());

        // Create the Layout
        LayoutDTO layoutDTO = layoutMapper.toDto(layout);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restLayoutMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(layoutDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Layout in the database
        List<Layout> layoutList = layoutRepository.findAll();
        assertThat(layoutList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamLayout() throws Exception {
        int databaseSizeBeforeUpdate = layoutRepository.findAll().size();
        layout.setId(count.incrementAndGet());

        // Create the Layout
        LayoutDTO layoutDTO = layoutMapper.toDto(layout);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restLayoutMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(layoutDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Layout in the database
        List<Layout> layoutList = layoutRepository.findAll();
        assertThat(layoutList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateLayoutWithPatch() throws Exception {
        // Initialize the database
        layoutRepository.saveAndFlush(layout);

        int databaseSizeBeforeUpdate = layoutRepository.findAll().size();

        // Update the layout using partial update
        Layout partialUpdatedLayout = new Layout();
        partialUpdatedLayout.setId(layout.getId());

        restLayoutMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedLayout.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedLayout))
            )
            .andExpect(status().isOk());

        // Validate the Layout in the database
        List<Layout> layoutList = layoutRepository.findAll();
        assertThat(layoutList).hasSize(databaseSizeBeforeUpdate);
        Layout testLayout = layoutList.get(layoutList.size() - 1);
        assertThat(testLayout.getRemoteId()).isEqualTo(DEFAULT_REMOTE_ID);
    }

    @Test
    @Transactional
    void fullUpdateLayoutWithPatch() throws Exception {
        // Initialize the database
        layoutRepository.saveAndFlush(layout);

        int databaseSizeBeforeUpdate = layoutRepository.findAll().size();

        // Update the layout using partial update
        Layout partialUpdatedLayout = new Layout();
        partialUpdatedLayout.setId(layout.getId());

        partialUpdatedLayout.remoteId(UPDATED_REMOTE_ID);

        restLayoutMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedLayout.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedLayout))
            )
            .andExpect(status().isOk());

        // Validate the Layout in the database
        List<Layout> layoutList = layoutRepository.findAll();
        assertThat(layoutList).hasSize(databaseSizeBeforeUpdate);
        Layout testLayout = layoutList.get(layoutList.size() - 1);
        assertThat(testLayout.getRemoteId()).isEqualTo(UPDATED_REMOTE_ID);
    }

    @Test
    @Transactional
    void patchNonExistingLayout() throws Exception {
        int databaseSizeBeforeUpdate = layoutRepository.findAll().size();
        layout.setId(count.incrementAndGet());

        // Create the Layout
        LayoutDTO layoutDTO = layoutMapper.toDto(layout);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restLayoutMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, layoutDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(layoutDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Layout in the database
        List<Layout> layoutList = layoutRepository.findAll();
        assertThat(layoutList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchLayout() throws Exception {
        int databaseSizeBeforeUpdate = layoutRepository.findAll().size();
        layout.setId(count.incrementAndGet());

        // Create the Layout
        LayoutDTO layoutDTO = layoutMapper.toDto(layout);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restLayoutMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(layoutDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Layout in the database
        List<Layout> layoutList = layoutRepository.findAll();
        assertThat(layoutList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamLayout() throws Exception {
        int databaseSizeBeforeUpdate = layoutRepository.findAll().size();
        layout.setId(count.incrementAndGet());

        // Create the Layout
        LayoutDTO layoutDTO = layoutMapper.toDto(layout);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restLayoutMockMvc
            .perform(
                patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(layoutDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the Layout in the database
        List<Layout> layoutList = layoutRepository.findAll();
        assertThat(layoutList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteLayout() throws Exception {
        // Initialize the database
        layoutRepository.saveAndFlush(layout);

        int databaseSizeBeforeDelete = layoutRepository.findAll().size();

        // Delete the layout
        restLayoutMockMvc
            .perform(delete(ENTITY_API_URL_ID, layout.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Layout> layoutList = layoutRepository.findAll();
        assertThat(layoutList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
