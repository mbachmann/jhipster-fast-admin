package ch.united.fastadmin.web.rest;

import static ch.united.fastadmin.web.rest.TestUtil.sameInstant;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import ch.united.fastadmin.IntegrationTest;
import ch.united.fastadmin.domain.DeliveryNote;
import ch.united.fastadmin.domain.enumeration.Currency;
import ch.united.fastadmin.domain.enumeration.DeliveryNoteStatus;
import ch.united.fastadmin.domain.enumeration.DiscountType;
import ch.united.fastadmin.domain.enumeration.DocumentLanguage;
import ch.united.fastadmin.repository.DeliveryNoteRepository;
import ch.united.fastadmin.service.dto.DeliveryNoteDTO;
import ch.united.fastadmin.service.mapper.DeliveryNoteMapper;
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
 * Integration tests for the {@link DeliveryNoteResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class DeliveryNoteResourceIT {

    private static final Integer DEFAULT_REMOTE_ID = 1;
    private static final Integer UPDATED_REMOTE_ID = 2;

    private static final String DEFAULT_NUMBER = "AAAAAAAAAA";
    private static final String UPDATED_NUMBER = "BBBBBBBBBB";

    private static final String DEFAULT_CONTACT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_CONTACT_NAME = "BBBBBBBBBB";

    private static final LocalDate DEFAULT_DATE = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_DATE = LocalDate.now(ZoneId.systemDefault());

    private static final String DEFAULT_PERIOD_TEXT = "AAAAAAAAAA";
    private static final String UPDATED_PERIOD_TEXT = "BBBBBBBBBB";

    private static final Currency DEFAULT_CURRENCY = Currency.AED;
    private static final Currency UPDATED_CURRENCY = Currency.AFN;

    private static final Double DEFAULT_TOTAL = 1D;
    private static final Double UPDATED_TOTAL = 2D;

    private static final Boolean DEFAULT_VAT_INCLUDED = false;
    private static final Boolean UPDATED_VAT_INCLUDED = true;

    private static final Double DEFAULT_DISCOUNT_RATE = 1D;
    private static final Double UPDATED_DISCOUNT_RATE = 2D;

    private static final DiscountType DEFAULT_DISCOUNT_TYPE = DiscountType.IN_PERCENT;
    private static final DiscountType UPDATED_DISCOUNT_TYPE = DiscountType.AMOUNT;

    private static final DocumentLanguage DEFAULT_LANGUAGE = DocumentLanguage.FRENCH;
    private static final DocumentLanguage UPDATED_LANGUAGE = DocumentLanguage.ENGLISH;

    private static final Integer DEFAULT_PAGE_AMOUNT = 1;
    private static final Integer UPDATED_PAGE_AMOUNT = 2;

    private static final String DEFAULT_NOTES = "AAAAAAAAAA";
    private static final String UPDATED_NOTES = "BBBBBBBBBB";

    private static final DeliveryNoteStatus DEFAULT_STATUS = DeliveryNoteStatus.DRAFT;
    private static final DeliveryNoteStatus UPDATED_STATUS = DeliveryNoteStatus.SENT;

    private static final ZonedDateTime DEFAULT_CREATED = ZonedDateTime.ofInstant(Instant.ofEpochMilli(0L), ZoneOffset.UTC);
    private static final ZonedDateTime UPDATED_CREATED = ZonedDateTime.now(ZoneId.systemDefault()).withNano(0);

    private static final String ENTITY_API_URL = "/api/delivery-notes";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private DeliveryNoteRepository deliveryNoteRepository;

    @Autowired
    private DeliveryNoteMapper deliveryNoteMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restDeliveryNoteMockMvc;

    private DeliveryNote deliveryNote;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static DeliveryNote createEntity(EntityManager em) {
        DeliveryNote deliveryNote = new DeliveryNote()
            .remoteId(DEFAULT_REMOTE_ID)
            .number(DEFAULT_NUMBER)
            .contactName(DEFAULT_CONTACT_NAME)
            .date(DEFAULT_DATE)
            .periodText(DEFAULT_PERIOD_TEXT)
            .currency(DEFAULT_CURRENCY)
            .total(DEFAULT_TOTAL)
            .vatIncluded(DEFAULT_VAT_INCLUDED)
            .discountRate(DEFAULT_DISCOUNT_RATE)
            .discountType(DEFAULT_DISCOUNT_TYPE)
            .language(DEFAULT_LANGUAGE)
            .pageAmount(DEFAULT_PAGE_AMOUNT)
            .notes(DEFAULT_NOTES)
            .status(DEFAULT_STATUS)
            .created(DEFAULT_CREATED);
        return deliveryNote;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static DeliveryNote createUpdatedEntity(EntityManager em) {
        DeliveryNote deliveryNote = new DeliveryNote()
            .remoteId(UPDATED_REMOTE_ID)
            .number(UPDATED_NUMBER)
            .contactName(UPDATED_CONTACT_NAME)
            .date(UPDATED_DATE)
            .periodText(UPDATED_PERIOD_TEXT)
            .currency(UPDATED_CURRENCY)
            .total(UPDATED_TOTAL)
            .vatIncluded(UPDATED_VAT_INCLUDED)
            .discountRate(UPDATED_DISCOUNT_RATE)
            .discountType(UPDATED_DISCOUNT_TYPE)
            .language(UPDATED_LANGUAGE)
            .pageAmount(UPDATED_PAGE_AMOUNT)
            .notes(UPDATED_NOTES)
            .status(UPDATED_STATUS)
            .created(UPDATED_CREATED);
        return deliveryNote;
    }

    @BeforeEach
    public void initTest() {
        deliveryNote = createEntity(em);
    }

    @Test
    @Transactional
    void createDeliveryNote() throws Exception {
        int databaseSizeBeforeCreate = deliveryNoteRepository.findAll().size();
        // Create the DeliveryNote
        DeliveryNoteDTO deliveryNoteDTO = deliveryNoteMapper.toDto(deliveryNote);
        restDeliveryNoteMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(deliveryNoteDTO))
            )
            .andExpect(status().isCreated());

        // Validate the DeliveryNote in the database
        List<DeliveryNote> deliveryNoteList = deliveryNoteRepository.findAll();
        assertThat(deliveryNoteList).hasSize(databaseSizeBeforeCreate + 1);
        DeliveryNote testDeliveryNote = deliveryNoteList.get(deliveryNoteList.size() - 1);
        assertThat(testDeliveryNote.getRemoteId()).isEqualTo(DEFAULT_REMOTE_ID);
        assertThat(testDeliveryNote.getNumber()).isEqualTo(DEFAULT_NUMBER);
        assertThat(testDeliveryNote.getContactName()).isEqualTo(DEFAULT_CONTACT_NAME);
        assertThat(testDeliveryNote.getDate()).isEqualTo(DEFAULT_DATE);
        assertThat(testDeliveryNote.getPeriodText()).isEqualTo(DEFAULT_PERIOD_TEXT);
        assertThat(testDeliveryNote.getCurrency()).isEqualTo(DEFAULT_CURRENCY);
        assertThat(testDeliveryNote.getTotal()).isEqualTo(DEFAULT_TOTAL);
        assertThat(testDeliveryNote.getVatIncluded()).isEqualTo(DEFAULT_VAT_INCLUDED);
        assertThat(testDeliveryNote.getDiscountRate()).isEqualTo(DEFAULT_DISCOUNT_RATE);
        assertThat(testDeliveryNote.getDiscountType()).isEqualTo(DEFAULT_DISCOUNT_TYPE);
        assertThat(testDeliveryNote.getLanguage()).isEqualTo(DEFAULT_LANGUAGE);
        assertThat(testDeliveryNote.getPageAmount()).isEqualTo(DEFAULT_PAGE_AMOUNT);
        assertThat(testDeliveryNote.getNotes()).isEqualTo(DEFAULT_NOTES);
        assertThat(testDeliveryNote.getStatus()).isEqualTo(DEFAULT_STATUS);
        assertThat(testDeliveryNote.getCreated()).isEqualTo(DEFAULT_CREATED);
    }

    @Test
    @Transactional
    void createDeliveryNoteWithExistingId() throws Exception {
        // Create the DeliveryNote with an existing ID
        deliveryNote.setId(1L);
        DeliveryNoteDTO deliveryNoteDTO = deliveryNoteMapper.toDto(deliveryNote);

        int databaseSizeBeforeCreate = deliveryNoteRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restDeliveryNoteMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(deliveryNoteDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the DeliveryNote in the database
        List<DeliveryNote> deliveryNoteList = deliveryNoteRepository.findAll();
        assertThat(deliveryNoteList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllDeliveryNotes() throws Exception {
        // Initialize the database
        deliveryNoteRepository.saveAndFlush(deliveryNote);

        // Get all the deliveryNoteList
        restDeliveryNoteMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(deliveryNote.getId().intValue())))
            .andExpect(jsonPath("$.[*].remoteId").value(hasItem(DEFAULT_REMOTE_ID)))
            .andExpect(jsonPath("$.[*].number").value(hasItem(DEFAULT_NUMBER)))
            .andExpect(jsonPath("$.[*].contactName").value(hasItem(DEFAULT_CONTACT_NAME)))
            .andExpect(jsonPath("$.[*].date").value(hasItem(DEFAULT_DATE.toString())))
            .andExpect(jsonPath("$.[*].periodText").value(hasItem(DEFAULT_PERIOD_TEXT)))
            .andExpect(jsonPath("$.[*].currency").value(hasItem(DEFAULT_CURRENCY.toString())))
            .andExpect(jsonPath("$.[*].total").value(hasItem(DEFAULT_TOTAL.doubleValue())))
            .andExpect(jsonPath("$.[*].vatIncluded").value(hasItem(DEFAULT_VAT_INCLUDED.booleanValue())))
            .andExpect(jsonPath("$.[*].discountRate").value(hasItem(DEFAULT_DISCOUNT_RATE.doubleValue())))
            .andExpect(jsonPath("$.[*].discountType").value(hasItem(DEFAULT_DISCOUNT_TYPE.toString())))
            .andExpect(jsonPath("$.[*].language").value(hasItem(DEFAULT_LANGUAGE.toString())))
            .andExpect(jsonPath("$.[*].pageAmount").value(hasItem(DEFAULT_PAGE_AMOUNT)))
            .andExpect(jsonPath("$.[*].notes").value(hasItem(DEFAULT_NOTES)))
            .andExpect(jsonPath("$.[*].status").value(hasItem(DEFAULT_STATUS.toString())))
            .andExpect(jsonPath("$.[*].created").value(hasItem(sameInstant(DEFAULT_CREATED))));
    }

    @Test
    @Transactional
    void getDeliveryNote() throws Exception {
        // Initialize the database
        deliveryNoteRepository.saveAndFlush(deliveryNote);

        // Get the deliveryNote
        restDeliveryNoteMockMvc
            .perform(get(ENTITY_API_URL_ID, deliveryNote.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(deliveryNote.getId().intValue()))
            .andExpect(jsonPath("$.remoteId").value(DEFAULT_REMOTE_ID))
            .andExpect(jsonPath("$.number").value(DEFAULT_NUMBER))
            .andExpect(jsonPath("$.contactName").value(DEFAULT_CONTACT_NAME))
            .andExpect(jsonPath("$.date").value(DEFAULT_DATE.toString()))
            .andExpect(jsonPath("$.periodText").value(DEFAULT_PERIOD_TEXT))
            .andExpect(jsonPath("$.currency").value(DEFAULT_CURRENCY.toString()))
            .andExpect(jsonPath("$.total").value(DEFAULT_TOTAL.doubleValue()))
            .andExpect(jsonPath("$.vatIncluded").value(DEFAULT_VAT_INCLUDED.booleanValue()))
            .andExpect(jsonPath("$.discountRate").value(DEFAULT_DISCOUNT_RATE.doubleValue()))
            .andExpect(jsonPath("$.discountType").value(DEFAULT_DISCOUNT_TYPE.toString()))
            .andExpect(jsonPath("$.language").value(DEFAULT_LANGUAGE.toString()))
            .andExpect(jsonPath("$.pageAmount").value(DEFAULT_PAGE_AMOUNT))
            .andExpect(jsonPath("$.notes").value(DEFAULT_NOTES))
            .andExpect(jsonPath("$.status").value(DEFAULT_STATUS.toString()))
            .andExpect(jsonPath("$.created").value(sameInstant(DEFAULT_CREATED)));
    }

    @Test
    @Transactional
    void getNonExistingDeliveryNote() throws Exception {
        // Get the deliveryNote
        restDeliveryNoteMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewDeliveryNote() throws Exception {
        // Initialize the database
        deliveryNoteRepository.saveAndFlush(deliveryNote);

        int databaseSizeBeforeUpdate = deliveryNoteRepository.findAll().size();

        // Update the deliveryNote
        DeliveryNote updatedDeliveryNote = deliveryNoteRepository.findById(deliveryNote.getId()).get();
        // Disconnect from session so that the updates on updatedDeliveryNote are not directly saved in db
        em.detach(updatedDeliveryNote);
        updatedDeliveryNote
            .remoteId(UPDATED_REMOTE_ID)
            .number(UPDATED_NUMBER)
            .contactName(UPDATED_CONTACT_NAME)
            .date(UPDATED_DATE)
            .periodText(UPDATED_PERIOD_TEXT)
            .currency(UPDATED_CURRENCY)
            .total(UPDATED_TOTAL)
            .vatIncluded(UPDATED_VAT_INCLUDED)
            .discountRate(UPDATED_DISCOUNT_RATE)
            .discountType(UPDATED_DISCOUNT_TYPE)
            .language(UPDATED_LANGUAGE)
            .pageAmount(UPDATED_PAGE_AMOUNT)
            .notes(UPDATED_NOTES)
            .status(UPDATED_STATUS)
            .created(UPDATED_CREATED);
        DeliveryNoteDTO deliveryNoteDTO = deliveryNoteMapper.toDto(updatedDeliveryNote);

        restDeliveryNoteMockMvc
            .perform(
                put(ENTITY_API_URL_ID, deliveryNoteDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(deliveryNoteDTO))
            )
            .andExpect(status().isOk());

        // Validate the DeliveryNote in the database
        List<DeliveryNote> deliveryNoteList = deliveryNoteRepository.findAll();
        assertThat(deliveryNoteList).hasSize(databaseSizeBeforeUpdate);
        DeliveryNote testDeliveryNote = deliveryNoteList.get(deliveryNoteList.size() - 1);
        assertThat(testDeliveryNote.getRemoteId()).isEqualTo(UPDATED_REMOTE_ID);
        assertThat(testDeliveryNote.getNumber()).isEqualTo(UPDATED_NUMBER);
        assertThat(testDeliveryNote.getContactName()).isEqualTo(UPDATED_CONTACT_NAME);
        assertThat(testDeliveryNote.getDate()).isEqualTo(UPDATED_DATE);
        assertThat(testDeliveryNote.getPeriodText()).isEqualTo(UPDATED_PERIOD_TEXT);
        assertThat(testDeliveryNote.getCurrency()).isEqualTo(UPDATED_CURRENCY);
        assertThat(testDeliveryNote.getTotal()).isEqualTo(UPDATED_TOTAL);
        assertThat(testDeliveryNote.getVatIncluded()).isEqualTo(UPDATED_VAT_INCLUDED);
        assertThat(testDeliveryNote.getDiscountRate()).isEqualTo(UPDATED_DISCOUNT_RATE);
        assertThat(testDeliveryNote.getDiscountType()).isEqualTo(UPDATED_DISCOUNT_TYPE);
        assertThat(testDeliveryNote.getLanguage()).isEqualTo(UPDATED_LANGUAGE);
        assertThat(testDeliveryNote.getPageAmount()).isEqualTo(UPDATED_PAGE_AMOUNT);
        assertThat(testDeliveryNote.getNotes()).isEqualTo(UPDATED_NOTES);
        assertThat(testDeliveryNote.getStatus()).isEqualTo(UPDATED_STATUS);
        assertThat(testDeliveryNote.getCreated()).isEqualTo(UPDATED_CREATED);
    }

    @Test
    @Transactional
    void putNonExistingDeliveryNote() throws Exception {
        int databaseSizeBeforeUpdate = deliveryNoteRepository.findAll().size();
        deliveryNote.setId(count.incrementAndGet());

        // Create the DeliveryNote
        DeliveryNoteDTO deliveryNoteDTO = deliveryNoteMapper.toDto(deliveryNote);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restDeliveryNoteMockMvc
            .perform(
                put(ENTITY_API_URL_ID, deliveryNoteDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(deliveryNoteDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the DeliveryNote in the database
        List<DeliveryNote> deliveryNoteList = deliveryNoteRepository.findAll();
        assertThat(deliveryNoteList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchDeliveryNote() throws Exception {
        int databaseSizeBeforeUpdate = deliveryNoteRepository.findAll().size();
        deliveryNote.setId(count.incrementAndGet());

        // Create the DeliveryNote
        DeliveryNoteDTO deliveryNoteDTO = deliveryNoteMapper.toDto(deliveryNote);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restDeliveryNoteMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(deliveryNoteDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the DeliveryNote in the database
        List<DeliveryNote> deliveryNoteList = deliveryNoteRepository.findAll();
        assertThat(deliveryNoteList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamDeliveryNote() throws Exception {
        int databaseSizeBeforeUpdate = deliveryNoteRepository.findAll().size();
        deliveryNote.setId(count.incrementAndGet());

        // Create the DeliveryNote
        DeliveryNoteDTO deliveryNoteDTO = deliveryNoteMapper.toDto(deliveryNote);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restDeliveryNoteMockMvc
            .perform(
                put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(deliveryNoteDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the DeliveryNote in the database
        List<DeliveryNote> deliveryNoteList = deliveryNoteRepository.findAll();
        assertThat(deliveryNoteList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateDeliveryNoteWithPatch() throws Exception {
        // Initialize the database
        deliveryNoteRepository.saveAndFlush(deliveryNote);

        int databaseSizeBeforeUpdate = deliveryNoteRepository.findAll().size();

        // Update the deliveryNote using partial update
        DeliveryNote partialUpdatedDeliveryNote = new DeliveryNote();
        partialUpdatedDeliveryNote.setId(deliveryNote.getId());

        partialUpdatedDeliveryNote
            .number(UPDATED_NUMBER)
            .date(UPDATED_DATE)
            .discountType(UPDATED_DISCOUNT_TYPE)
            .language(UPDATED_LANGUAGE)
            .pageAmount(UPDATED_PAGE_AMOUNT)
            .created(UPDATED_CREATED);

        restDeliveryNoteMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedDeliveryNote.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedDeliveryNote))
            )
            .andExpect(status().isOk());

        // Validate the DeliveryNote in the database
        List<DeliveryNote> deliveryNoteList = deliveryNoteRepository.findAll();
        assertThat(deliveryNoteList).hasSize(databaseSizeBeforeUpdate);
        DeliveryNote testDeliveryNote = deliveryNoteList.get(deliveryNoteList.size() - 1);
        assertThat(testDeliveryNote.getRemoteId()).isEqualTo(DEFAULT_REMOTE_ID);
        assertThat(testDeliveryNote.getNumber()).isEqualTo(UPDATED_NUMBER);
        assertThat(testDeliveryNote.getContactName()).isEqualTo(DEFAULT_CONTACT_NAME);
        assertThat(testDeliveryNote.getDate()).isEqualTo(UPDATED_DATE);
        assertThat(testDeliveryNote.getPeriodText()).isEqualTo(DEFAULT_PERIOD_TEXT);
        assertThat(testDeliveryNote.getCurrency()).isEqualTo(DEFAULT_CURRENCY);
        assertThat(testDeliveryNote.getTotal()).isEqualTo(DEFAULT_TOTAL);
        assertThat(testDeliveryNote.getVatIncluded()).isEqualTo(DEFAULT_VAT_INCLUDED);
        assertThat(testDeliveryNote.getDiscountRate()).isEqualTo(DEFAULT_DISCOUNT_RATE);
        assertThat(testDeliveryNote.getDiscountType()).isEqualTo(UPDATED_DISCOUNT_TYPE);
        assertThat(testDeliveryNote.getLanguage()).isEqualTo(UPDATED_LANGUAGE);
        assertThat(testDeliveryNote.getPageAmount()).isEqualTo(UPDATED_PAGE_AMOUNT);
        assertThat(testDeliveryNote.getNotes()).isEqualTo(DEFAULT_NOTES);
        assertThat(testDeliveryNote.getStatus()).isEqualTo(DEFAULT_STATUS);
        assertThat(testDeliveryNote.getCreated()).isEqualTo(UPDATED_CREATED);
    }

    @Test
    @Transactional
    void fullUpdateDeliveryNoteWithPatch() throws Exception {
        // Initialize the database
        deliveryNoteRepository.saveAndFlush(deliveryNote);

        int databaseSizeBeforeUpdate = deliveryNoteRepository.findAll().size();

        // Update the deliveryNote using partial update
        DeliveryNote partialUpdatedDeliveryNote = new DeliveryNote();
        partialUpdatedDeliveryNote.setId(deliveryNote.getId());

        partialUpdatedDeliveryNote
            .remoteId(UPDATED_REMOTE_ID)
            .number(UPDATED_NUMBER)
            .contactName(UPDATED_CONTACT_NAME)
            .date(UPDATED_DATE)
            .periodText(UPDATED_PERIOD_TEXT)
            .currency(UPDATED_CURRENCY)
            .total(UPDATED_TOTAL)
            .vatIncluded(UPDATED_VAT_INCLUDED)
            .discountRate(UPDATED_DISCOUNT_RATE)
            .discountType(UPDATED_DISCOUNT_TYPE)
            .language(UPDATED_LANGUAGE)
            .pageAmount(UPDATED_PAGE_AMOUNT)
            .notes(UPDATED_NOTES)
            .status(UPDATED_STATUS)
            .created(UPDATED_CREATED);

        restDeliveryNoteMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedDeliveryNote.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedDeliveryNote))
            )
            .andExpect(status().isOk());

        // Validate the DeliveryNote in the database
        List<DeliveryNote> deliveryNoteList = deliveryNoteRepository.findAll();
        assertThat(deliveryNoteList).hasSize(databaseSizeBeforeUpdate);
        DeliveryNote testDeliveryNote = deliveryNoteList.get(deliveryNoteList.size() - 1);
        assertThat(testDeliveryNote.getRemoteId()).isEqualTo(UPDATED_REMOTE_ID);
        assertThat(testDeliveryNote.getNumber()).isEqualTo(UPDATED_NUMBER);
        assertThat(testDeliveryNote.getContactName()).isEqualTo(UPDATED_CONTACT_NAME);
        assertThat(testDeliveryNote.getDate()).isEqualTo(UPDATED_DATE);
        assertThat(testDeliveryNote.getPeriodText()).isEqualTo(UPDATED_PERIOD_TEXT);
        assertThat(testDeliveryNote.getCurrency()).isEqualTo(UPDATED_CURRENCY);
        assertThat(testDeliveryNote.getTotal()).isEqualTo(UPDATED_TOTAL);
        assertThat(testDeliveryNote.getVatIncluded()).isEqualTo(UPDATED_VAT_INCLUDED);
        assertThat(testDeliveryNote.getDiscountRate()).isEqualTo(UPDATED_DISCOUNT_RATE);
        assertThat(testDeliveryNote.getDiscountType()).isEqualTo(UPDATED_DISCOUNT_TYPE);
        assertThat(testDeliveryNote.getLanguage()).isEqualTo(UPDATED_LANGUAGE);
        assertThat(testDeliveryNote.getPageAmount()).isEqualTo(UPDATED_PAGE_AMOUNT);
        assertThat(testDeliveryNote.getNotes()).isEqualTo(UPDATED_NOTES);
        assertThat(testDeliveryNote.getStatus()).isEqualTo(UPDATED_STATUS);
        assertThat(testDeliveryNote.getCreated()).isEqualTo(UPDATED_CREATED);
    }

    @Test
    @Transactional
    void patchNonExistingDeliveryNote() throws Exception {
        int databaseSizeBeforeUpdate = deliveryNoteRepository.findAll().size();
        deliveryNote.setId(count.incrementAndGet());

        // Create the DeliveryNote
        DeliveryNoteDTO deliveryNoteDTO = deliveryNoteMapper.toDto(deliveryNote);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restDeliveryNoteMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, deliveryNoteDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(deliveryNoteDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the DeliveryNote in the database
        List<DeliveryNote> deliveryNoteList = deliveryNoteRepository.findAll();
        assertThat(deliveryNoteList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchDeliveryNote() throws Exception {
        int databaseSizeBeforeUpdate = deliveryNoteRepository.findAll().size();
        deliveryNote.setId(count.incrementAndGet());

        // Create the DeliveryNote
        DeliveryNoteDTO deliveryNoteDTO = deliveryNoteMapper.toDto(deliveryNote);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restDeliveryNoteMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(deliveryNoteDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the DeliveryNote in the database
        List<DeliveryNote> deliveryNoteList = deliveryNoteRepository.findAll();
        assertThat(deliveryNoteList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamDeliveryNote() throws Exception {
        int databaseSizeBeforeUpdate = deliveryNoteRepository.findAll().size();
        deliveryNote.setId(count.incrementAndGet());

        // Create the DeliveryNote
        DeliveryNoteDTO deliveryNoteDTO = deliveryNoteMapper.toDto(deliveryNote);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restDeliveryNoteMockMvc
            .perform(
                patch(ENTITY_API_URL)
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(deliveryNoteDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the DeliveryNote in the database
        List<DeliveryNote> deliveryNoteList = deliveryNoteRepository.findAll();
        assertThat(deliveryNoteList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteDeliveryNote() throws Exception {
        // Initialize the database
        deliveryNoteRepository.saveAndFlush(deliveryNote);

        int databaseSizeBeforeDelete = deliveryNoteRepository.findAll().size();

        // Delete the deliveryNote
        restDeliveryNoteMockMvc
            .perform(delete(ENTITY_API_URL_ID, deliveryNote.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<DeliveryNote> deliveryNoteList = deliveryNoteRepository.findAll();
        assertThat(deliveryNoteList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
