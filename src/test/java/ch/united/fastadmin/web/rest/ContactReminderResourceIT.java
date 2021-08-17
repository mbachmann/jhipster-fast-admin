package ch.united.fastadmin.web.rest;

import static ch.united.fastadmin.web.rest.TestUtil.sameInstant;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import ch.united.fastadmin.IntegrationTest;
import ch.united.fastadmin.domain.ContactReminder;
import ch.united.fastadmin.domain.enumeration.IntervalType;
import ch.united.fastadmin.repository.ContactReminderRepository;
import ch.united.fastadmin.service.dto.ContactReminderDTO;
import ch.united.fastadmin.service.mapper.ContactReminderMapper;
import java.time.Instant;
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
 * Integration tests for the {@link ContactReminderResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class ContactReminderResourceIT {

    private static final Integer DEFAULT_REMOTE_ID = 1;
    private static final Integer UPDATED_REMOTE_ID = 2;

    private static final String DEFAULT_CONTACT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_CONTACT_NAME = "BBBBBBBBBB";

    private static final ZonedDateTime DEFAULT_DATE_TIME = ZonedDateTime.ofInstant(Instant.ofEpochMilli(0L), ZoneOffset.UTC);
    private static final ZonedDateTime UPDATED_DATE_TIME = ZonedDateTime.now(ZoneId.systemDefault()).withNano(0);

    private static final String DEFAULT_TITLE = "AAAAAAAAAA";
    private static final String UPDATED_TITLE = "BBBBBBBBBB";

    private static final String DEFAULT_DESCRIPTION = "AAAAAAAAAA";
    private static final String UPDATED_DESCRIPTION = "BBBBBBBBBB";

    private static final Integer DEFAULT_INTERVAL_VALUE = 1;
    private static final Integer UPDATED_INTERVAL_VALUE = 2;

    private static final IntervalType DEFAULT_INTERVAL_TYPE = IntervalType.HOUR;
    private static final IntervalType UPDATED_INTERVAL_TYPE = IntervalType.DAY;

    private static final Boolean DEFAULT_INACTIV = false;
    private static final Boolean UPDATED_INACTIV = true;

    private static final String ENTITY_API_URL = "/api/contact-reminders";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ContactReminderRepository contactReminderRepository;

    @Autowired
    private ContactReminderMapper contactReminderMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restContactReminderMockMvc;

    private ContactReminder contactReminder;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static ContactReminder createEntity(EntityManager em) {
        ContactReminder contactReminder = new ContactReminder()
            .remoteId(DEFAULT_REMOTE_ID)
            .contactName(DEFAULT_CONTACT_NAME)
            .dateTime(DEFAULT_DATE_TIME)
            .title(DEFAULT_TITLE)
            .description(DEFAULT_DESCRIPTION)
            .intervalValue(DEFAULT_INTERVAL_VALUE)
            .intervalType(DEFAULT_INTERVAL_TYPE)
            .inactiv(DEFAULT_INACTIV);
        return contactReminder;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static ContactReminder createUpdatedEntity(EntityManager em) {
        ContactReminder contactReminder = new ContactReminder()
            .remoteId(UPDATED_REMOTE_ID)
            .contactName(UPDATED_CONTACT_NAME)
            .dateTime(UPDATED_DATE_TIME)
            .title(UPDATED_TITLE)
            .description(UPDATED_DESCRIPTION)
            .intervalValue(UPDATED_INTERVAL_VALUE)
            .intervalType(UPDATED_INTERVAL_TYPE)
            .inactiv(UPDATED_INACTIV);
        return contactReminder;
    }

    @BeforeEach
    public void initTest() {
        contactReminder = createEntity(em);
    }

    @Test
    @Transactional
    void createContactReminder() throws Exception {
        int databaseSizeBeforeCreate = contactReminderRepository.findAll().size();
        // Create the ContactReminder
        ContactReminderDTO contactReminderDTO = contactReminderMapper.toDto(contactReminder);
        restContactReminderMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(contactReminderDTO))
            )
            .andExpect(status().isCreated());

        // Validate the ContactReminder in the database
        List<ContactReminder> contactReminderList = contactReminderRepository.findAll();
        assertThat(contactReminderList).hasSize(databaseSizeBeforeCreate + 1);
        ContactReminder testContactReminder = contactReminderList.get(contactReminderList.size() - 1);
        assertThat(testContactReminder.getRemoteId()).isEqualTo(DEFAULT_REMOTE_ID);
        assertThat(testContactReminder.getContactName()).isEqualTo(DEFAULT_CONTACT_NAME);
        assertThat(testContactReminder.getDateTime()).isEqualTo(DEFAULT_DATE_TIME);
        assertThat(testContactReminder.getTitle()).isEqualTo(DEFAULT_TITLE);
        assertThat(testContactReminder.getDescription()).isEqualTo(DEFAULT_DESCRIPTION);
        assertThat(testContactReminder.getIntervalValue()).isEqualTo(DEFAULT_INTERVAL_VALUE);
        assertThat(testContactReminder.getIntervalType()).isEqualTo(DEFAULT_INTERVAL_TYPE);
        assertThat(testContactReminder.getInactiv()).isEqualTo(DEFAULT_INACTIV);
    }

    @Test
    @Transactional
    void createContactReminderWithExistingId() throws Exception {
        // Create the ContactReminder with an existing ID
        contactReminder.setId(1L);
        ContactReminderDTO contactReminderDTO = contactReminderMapper.toDto(contactReminder);

        int databaseSizeBeforeCreate = contactReminderRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restContactReminderMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(contactReminderDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the ContactReminder in the database
        List<ContactReminder> contactReminderList = contactReminderRepository.findAll();
        assertThat(contactReminderList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllContactReminders() throws Exception {
        // Initialize the database
        contactReminderRepository.saveAndFlush(contactReminder);

        // Get all the contactReminderList
        restContactReminderMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(contactReminder.getId().intValue())))
            .andExpect(jsonPath("$.[*].remoteId").value(hasItem(DEFAULT_REMOTE_ID)))
            .andExpect(jsonPath("$.[*].contactName").value(hasItem(DEFAULT_CONTACT_NAME)))
            .andExpect(jsonPath("$.[*].dateTime").value(hasItem(sameInstant(DEFAULT_DATE_TIME))))
            .andExpect(jsonPath("$.[*].title").value(hasItem(DEFAULT_TITLE)))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION)))
            .andExpect(jsonPath("$.[*].intervalValue").value(hasItem(DEFAULT_INTERVAL_VALUE)))
            .andExpect(jsonPath("$.[*].intervalType").value(hasItem(DEFAULT_INTERVAL_TYPE.toString())))
            .andExpect(jsonPath("$.[*].inactiv").value(hasItem(DEFAULT_INACTIV.booleanValue())));
    }

    @Test
    @Transactional
    void getContactReminder() throws Exception {
        // Initialize the database
        contactReminderRepository.saveAndFlush(contactReminder);

        // Get the contactReminder
        restContactReminderMockMvc
            .perform(get(ENTITY_API_URL_ID, contactReminder.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(contactReminder.getId().intValue()))
            .andExpect(jsonPath("$.remoteId").value(DEFAULT_REMOTE_ID))
            .andExpect(jsonPath("$.contactName").value(DEFAULT_CONTACT_NAME))
            .andExpect(jsonPath("$.dateTime").value(sameInstant(DEFAULT_DATE_TIME)))
            .andExpect(jsonPath("$.title").value(DEFAULT_TITLE))
            .andExpect(jsonPath("$.description").value(DEFAULT_DESCRIPTION))
            .andExpect(jsonPath("$.intervalValue").value(DEFAULT_INTERVAL_VALUE))
            .andExpect(jsonPath("$.intervalType").value(DEFAULT_INTERVAL_TYPE.toString()))
            .andExpect(jsonPath("$.inactiv").value(DEFAULT_INACTIV.booleanValue()));
    }

    @Test
    @Transactional
    void getNonExistingContactReminder() throws Exception {
        // Get the contactReminder
        restContactReminderMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewContactReminder() throws Exception {
        // Initialize the database
        contactReminderRepository.saveAndFlush(contactReminder);

        int databaseSizeBeforeUpdate = contactReminderRepository.findAll().size();

        // Update the contactReminder
        ContactReminder updatedContactReminder = contactReminderRepository.findById(contactReminder.getId()).get();
        // Disconnect from session so that the updates on updatedContactReminder are not directly saved in db
        em.detach(updatedContactReminder);
        updatedContactReminder
            .remoteId(UPDATED_REMOTE_ID)
            .contactName(UPDATED_CONTACT_NAME)
            .dateTime(UPDATED_DATE_TIME)
            .title(UPDATED_TITLE)
            .description(UPDATED_DESCRIPTION)
            .intervalValue(UPDATED_INTERVAL_VALUE)
            .intervalType(UPDATED_INTERVAL_TYPE)
            .inactiv(UPDATED_INACTIV);
        ContactReminderDTO contactReminderDTO = contactReminderMapper.toDto(updatedContactReminder);

        restContactReminderMockMvc
            .perform(
                put(ENTITY_API_URL_ID, contactReminderDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(contactReminderDTO))
            )
            .andExpect(status().isOk());

        // Validate the ContactReminder in the database
        List<ContactReminder> contactReminderList = contactReminderRepository.findAll();
        assertThat(contactReminderList).hasSize(databaseSizeBeforeUpdate);
        ContactReminder testContactReminder = contactReminderList.get(contactReminderList.size() - 1);
        assertThat(testContactReminder.getRemoteId()).isEqualTo(UPDATED_REMOTE_ID);
        assertThat(testContactReminder.getContactName()).isEqualTo(UPDATED_CONTACT_NAME);
        assertThat(testContactReminder.getDateTime()).isEqualTo(UPDATED_DATE_TIME);
        assertThat(testContactReminder.getTitle()).isEqualTo(UPDATED_TITLE);
        assertThat(testContactReminder.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
        assertThat(testContactReminder.getIntervalValue()).isEqualTo(UPDATED_INTERVAL_VALUE);
        assertThat(testContactReminder.getIntervalType()).isEqualTo(UPDATED_INTERVAL_TYPE);
        assertThat(testContactReminder.getInactiv()).isEqualTo(UPDATED_INACTIV);
    }

    @Test
    @Transactional
    void putNonExistingContactReminder() throws Exception {
        int databaseSizeBeforeUpdate = contactReminderRepository.findAll().size();
        contactReminder.setId(count.incrementAndGet());

        // Create the ContactReminder
        ContactReminderDTO contactReminderDTO = contactReminderMapper.toDto(contactReminder);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restContactReminderMockMvc
            .perform(
                put(ENTITY_API_URL_ID, contactReminderDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(contactReminderDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the ContactReminder in the database
        List<ContactReminder> contactReminderList = contactReminderRepository.findAll();
        assertThat(contactReminderList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchContactReminder() throws Exception {
        int databaseSizeBeforeUpdate = contactReminderRepository.findAll().size();
        contactReminder.setId(count.incrementAndGet());

        // Create the ContactReminder
        ContactReminderDTO contactReminderDTO = contactReminderMapper.toDto(contactReminder);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restContactReminderMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(contactReminderDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the ContactReminder in the database
        List<ContactReminder> contactReminderList = contactReminderRepository.findAll();
        assertThat(contactReminderList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamContactReminder() throws Exception {
        int databaseSizeBeforeUpdate = contactReminderRepository.findAll().size();
        contactReminder.setId(count.incrementAndGet());

        // Create the ContactReminder
        ContactReminderDTO contactReminderDTO = contactReminderMapper.toDto(contactReminder);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restContactReminderMockMvc
            .perform(
                put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(contactReminderDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the ContactReminder in the database
        List<ContactReminder> contactReminderList = contactReminderRepository.findAll();
        assertThat(contactReminderList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateContactReminderWithPatch() throws Exception {
        // Initialize the database
        contactReminderRepository.saveAndFlush(contactReminder);

        int databaseSizeBeforeUpdate = contactReminderRepository.findAll().size();

        // Update the contactReminder using partial update
        ContactReminder partialUpdatedContactReminder = new ContactReminder();
        partialUpdatedContactReminder.setId(contactReminder.getId());

        partialUpdatedContactReminder.title(UPDATED_TITLE).intervalValue(UPDATED_INTERVAL_VALUE).intervalType(UPDATED_INTERVAL_TYPE);

        restContactReminderMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedContactReminder.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedContactReminder))
            )
            .andExpect(status().isOk());

        // Validate the ContactReminder in the database
        List<ContactReminder> contactReminderList = contactReminderRepository.findAll();
        assertThat(contactReminderList).hasSize(databaseSizeBeforeUpdate);
        ContactReminder testContactReminder = contactReminderList.get(contactReminderList.size() - 1);
        assertThat(testContactReminder.getRemoteId()).isEqualTo(DEFAULT_REMOTE_ID);
        assertThat(testContactReminder.getContactName()).isEqualTo(DEFAULT_CONTACT_NAME);
        assertThat(testContactReminder.getDateTime()).isEqualTo(DEFAULT_DATE_TIME);
        assertThat(testContactReminder.getTitle()).isEqualTo(UPDATED_TITLE);
        assertThat(testContactReminder.getDescription()).isEqualTo(DEFAULT_DESCRIPTION);
        assertThat(testContactReminder.getIntervalValue()).isEqualTo(UPDATED_INTERVAL_VALUE);
        assertThat(testContactReminder.getIntervalType()).isEqualTo(UPDATED_INTERVAL_TYPE);
        assertThat(testContactReminder.getInactiv()).isEqualTo(DEFAULT_INACTIV);
    }

    @Test
    @Transactional
    void fullUpdateContactReminderWithPatch() throws Exception {
        // Initialize the database
        contactReminderRepository.saveAndFlush(contactReminder);

        int databaseSizeBeforeUpdate = contactReminderRepository.findAll().size();

        // Update the contactReminder using partial update
        ContactReminder partialUpdatedContactReminder = new ContactReminder();
        partialUpdatedContactReminder.setId(contactReminder.getId());

        partialUpdatedContactReminder
            .remoteId(UPDATED_REMOTE_ID)
            .contactName(UPDATED_CONTACT_NAME)
            .dateTime(UPDATED_DATE_TIME)
            .title(UPDATED_TITLE)
            .description(UPDATED_DESCRIPTION)
            .intervalValue(UPDATED_INTERVAL_VALUE)
            .intervalType(UPDATED_INTERVAL_TYPE)
            .inactiv(UPDATED_INACTIV);

        restContactReminderMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedContactReminder.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedContactReminder))
            )
            .andExpect(status().isOk());

        // Validate the ContactReminder in the database
        List<ContactReminder> contactReminderList = contactReminderRepository.findAll();
        assertThat(contactReminderList).hasSize(databaseSizeBeforeUpdate);
        ContactReminder testContactReminder = contactReminderList.get(contactReminderList.size() - 1);
        assertThat(testContactReminder.getRemoteId()).isEqualTo(UPDATED_REMOTE_ID);
        assertThat(testContactReminder.getContactName()).isEqualTo(UPDATED_CONTACT_NAME);
        assertThat(testContactReminder.getDateTime()).isEqualTo(UPDATED_DATE_TIME);
        assertThat(testContactReminder.getTitle()).isEqualTo(UPDATED_TITLE);
        assertThat(testContactReminder.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
        assertThat(testContactReminder.getIntervalValue()).isEqualTo(UPDATED_INTERVAL_VALUE);
        assertThat(testContactReminder.getIntervalType()).isEqualTo(UPDATED_INTERVAL_TYPE);
        assertThat(testContactReminder.getInactiv()).isEqualTo(UPDATED_INACTIV);
    }

    @Test
    @Transactional
    void patchNonExistingContactReminder() throws Exception {
        int databaseSizeBeforeUpdate = contactReminderRepository.findAll().size();
        contactReminder.setId(count.incrementAndGet());

        // Create the ContactReminder
        ContactReminderDTO contactReminderDTO = contactReminderMapper.toDto(contactReminder);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restContactReminderMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, contactReminderDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(contactReminderDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the ContactReminder in the database
        List<ContactReminder> contactReminderList = contactReminderRepository.findAll();
        assertThat(contactReminderList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchContactReminder() throws Exception {
        int databaseSizeBeforeUpdate = contactReminderRepository.findAll().size();
        contactReminder.setId(count.incrementAndGet());

        // Create the ContactReminder
        ContactReminderDTO contactReminderDTO = contactReminderMapper.toDto(contactReminder);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restContactReminderMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(contactReminderDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the ContactReminder in the database
        List<ContactReminder> contactReminderList = contactReminderRepository.findAll();
        assertThat(contactReminderList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamContactReminder() throws Exception {
        int databaseSizeBeforeUpdate = contactReminderRepository.findAll().size();
        contactReminder.setId(count.incrementAndGet());

        // Create the ContactReminder
        ContactReminderDTO contactReminderDTO = contactReminderMapper.toDto(contactReminder);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restContactReminderMockMvc
            .perform(
                patch(ENTITY_API_URL)
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(contactReminderDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the ContactReminder in the database
        List<ContactReminder> contactReminderList = contactReminderRepository.findAll();
        assertThat(contactReminderList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteContactReminder() throws Exception {
        // Initialize the database
        contactReminderRepository.saveAndFlush(contactReminder);

        int databaseSizeBeforeDelete = contactReminderRepository.findAll().size();

        // Delete the contactReminder
        restContactReminderMockMvc
            .perform(delete(ENTITY_API_URL_ID, contactReminder.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<ContactReminder> contactReminderList = contactReminderRepository.findAll();
        assertThat(contactReminderList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
