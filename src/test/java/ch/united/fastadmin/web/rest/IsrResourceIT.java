package ch.united.fastadmin.web.rest;

import static ch.united.fastadmin.web.rest.TestUtil.sameInstant;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import ch.united.fastadmin.IntegrationTest;
import ch.united.fastadmin.domain.Isr;
import ch.united.fastadmin.domain.enumeration.IsrPosition;
import ch.united.fastadmin.domain.enumeration.IsrType;
import ch.united.fastadmin.repository.IsrRepository;
import ch.united.fastadmin.service.dto.IsrDTO;
import ch.united.fastadmin.service.mapper.IsrMapper;
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
 * Integration tests for the {@link IsrResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class IsrResourceIT {

    private static final Boolean DEFAULT_DEFAULT_ISR = false;
    private static final Boolean UPDATED_DEFAULT_ISR = true;

    private static final IsrType DEFAULT_TYPE = IsrType.RED_INPAYMENT;
    private static final IsrType UPDATED_TYPE = IsrType.IBAN_NUMBER;

    private static final IsrPosition DEFAULT_POSITION = IsrPosition.ADDITIONAL_PAGE;
    private static final IsrPosition UPDATED_POSITION = IsrPosition.FIRST_PAGE;

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_BANK_NAME = "AAAAAAAAAA";
    private static final String UPDATED_BANK_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_BANK_ADDRESS = "AAAAAAAAAA";
    private static final String UPDATED_BANK_ADDRESS = "BBBBBBBBBB";

    private static final String DEFAULT_RECIPIENT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_RECIPIENT_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_RECIPIENT_ADDITION = "AAAAAAAAAA";
    private static final String UPDATED_RECIPIENT_ADDITION = "BBBBBBBBBB";

    private static final String DEFAULT_RECIPIENT_STREET = "AAAAAAAAAA";
    private static final String UPDATED_RECIPIENT_STREET = "BBBBBBBBBB";

    private static final String DEFAULT_RECIPIENT_CITY = "AAAAAAAAAA";
    private static final String UPDATED_RECIPIENT_CITY = "BBBBBBBBBB";

    private static final String DEFAULT_DELIVERY_NUMBER = "AAAAAAAAAA";
    private static final String UPDATED_DELIVERY_NUMBER = "BBBBBBBBBB";

    private static final String DEFAULT_IBAN = "AAAAAAAAAA";
    private static final String UPDATED_IBAN = "BBBBBBBBBB";

    private static final String DEFAULT_SUBSCRIBER_NUMBER = "AAAAAAAAAA";
    private static final String UPDATED_SUBSCRIBER_NUMBER = "BBBBBBBBBB";

    private static final Double DEFAULT_LEFT_PRINT_ADJUST = 1D;
    private static final Double UPDATED_LEFT_PRINT_ADJUST = 2D;

    private static final Double DEFAULT_TOP_PRINT_ADJUST = 1D;
    private static final Double UPDATED_TOP_PRINT_ADJUST = 2D;

    private static final ZonedDateTime DEFAULT_CREATED = ZonedDateTime.ofInstant(Instant.ofEpochMilli(0L), ZoneOffset.UTC);
    private static final ZonedDateTime UPDATED_CREATED = ZonedDateTime.now(ZoneId.systemDefault()).withNano(0);

    private static final Boolean DEFAULT_INACTIV = false;
    private static final Boolean UPDATED_INACTIV = true;

    private static final String ENTITY_API_URL = "/api/isrs";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private IsrRepository isrRepository;

    @Autowired
    private IsrMapper isrMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restIsrMockMvc;

    private Isr isr;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Isr createEntity(EntityManager em) {
        Isr isr = new Isr()
            .defaultIsr(DEFAULT_DEFAULT_ISR)
            .type(DEFAULT_TYPE)
            .position(DEFAULT_POSITION)
            .name(DEFAULT_NAME)
            .bankName(DEFAULT_BANK_NAME)
            .bankAddress(DEFAULT_BANK_ADDRESS)
            .recipientName(DEFAULT_RECIPIENT_NAME)
            .recipientAddition(DEFAULT_RECIPIENT_ADDITION)
            .recipientStreet(DEFAULT_RECIPIENT_STREET)
            .recipientCity(DEFAULT_RECIPIENT_CITY)
            .deliveryNumber(DEFAULT_DELIVERY_NUMBER)
            .iban(DEFAULT_IBAN)
            .subscriberNumber(DEFAULT_SUBSCRIBER_NUMBER)
            .leftPrintAdjust(DEFAULT_LEFT_PRINT_ADJUST)
            .topPrintAdjust(DEFAULT_TOP_PRINT_ADJUST)
            .created(DEFAULT_CREATED)
            .inactiv(DEFAULT_INACTIV);
        return isr;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Isr createUpdatedEntity(EntityManager em) {
        Isr isr = new Isr()
            .defaultIsr(UPDATED_DEFAULT_ISR)
            .type(UPDATED_TYPE)
            .position(UPDATED_POSITION)
            .name(UPDATED_NAME)
            .bankName(UPDATED_BANK_NAME)
            .bankAddress(UPDATED_BANK_ADDRESS)
            .recipientName(UPDATED_RECIPIENT_NAME)
            .recipientAddition(UPDATED_RECIPIENT_ADDITION)
            .recipientStreet(UPDATED_RECIPIENT_STREET)
            .recipientCity(UPDATED_RECIPIENT_CITY)
            .deliveryNumber(UPDATED_DELIVERY_NUMBER)
            .iban(UPDATED_IBAN)
            .subscriberNumber(UPDATED_SUBSCRIBER_NUMBER)
            .leftPrintAdjust(UPDATED_LEFT_PRINT_ADJUST)
            .topPrintAdjust(UPDATED_TOP_PRINT_ADJUST)
            .created(UPDATED_CREATED)
            .inactiv(UPDATED_INACTIV);
        return isr;
    }

    @BeforeEach
    public void initTest() {
        isr = createEntity(em);
    }

    @Test
    @Transactional
    void createIsr() throws Exception {
        int databaseSizeBeforeCreate = isrRepository.findAll().size();
        // Create the Isr
        IsrDTO isrDTO = isrMapper.toDto(isr);
        restIsrMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(isrDTO)))
            .andExpect(status().isCreated());

        // Validate the Isr in the database
        List<Isr> isrList = isrRepository.findAll();
        assertThat(isrList).hasSize(databaseSizeBeforeCreate + 1);
        Isr testIsr = isrList.get(isrList.size() - 1);
        assertThat(testIsr.getDefaultIsr()).isEqualTo(DEFAULT_DEFAULT_ISR);
        assertThat(testIsr.getType()).isEqualTo(DEFAULT_TYPE);
        assertThat(testIsr.getPosition()).isEqualTo(DEFAULT_POSITION);
        assertThat(testIsr.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testIsr.getBankName()).isEqualTo(DEFAULT_BANK_NAME);
        assertThat(testIsr.getBankAddress()).isEqualTo(DEFAULT_BANK_ADDRESS);
        assertThat(testIsr.getRecipientName()).isEqualTo(DEFAULT_RECIPIENT_NAME);
        assertThat(testIsr.getRecipientAddition()).isEqualTo(DEFAULT_RECIPIENT_ADDITION);
        assertThat(testIsr.getRecipientStreet()).isEqualTo(DEFAULT_RECIPIENT_STREET);
        assertThat(testIsr.getRecipientCity()).isEqualTo(DEFAULT_RECIPIENT_CITY);
        assertThat(testIsr.getDeliveryNumber()).isEqualTo(DEFAULT_DELIVERY_NUMBER);
        assertThat(testIsr.getIban()).isEqualTo(DEFAULT_IBAN);
        assertThat(testIsr.getSubscriberNumber()).isEqualTo(DEFAULT_SUBSCRIBER_NUMBER);
        assertThat(testIsr.getLeftPrintAdjust()).isEqualTo(DEFAULT_LEFT_PRINT_ADJUST);
        assertThat(testIsr.getTopPrintAdjust()).isEqualTo(DEFAULT_TOP_PRINT_ADJUST);
        assertThat(testIsr.getCreated()).isEqualTo(DEFAULT_CREATED);
        assertThat(testIsr.getInactiv()).isEqualTo(DEFAULT_INACTIV);
    }

    @Test
    @Transactional
    void createIsrWithExistingId() throws Exception {
        // Create the Isr with an existing ID
        isr.setId(1L);
        IsrDTO isrDTO = isrMapper.toDto(isr);

        int databaseSizeBeforeCreate = isrRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restIsrMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(isrDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Isr in the database
        List<Isr> isrList = isrRepository.findAll();
        assertThat(isrList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllIsrs() throws Exception {
        // Initialize the database
        isrRepository.saveAndFlush(isr);

        // Get all the isrList
        restIsrMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(isr.getId().intValue())))
            .andExpect(jsonPath("$.[*].defaultIsr").value(hasItem(DEFAULT_DEFAULT_ISR.booleanValue())))
            .andExpect(jsonPath("$.[*].type").value(hasItem(DEFAULT_TYPE.toString())))
            .andExpect(jsonPath("$.[*].position").value(hasItem(DEFAULT_POSITION.toString())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)))
            .andExpect(jsonPath("$.[*].bankName").value(hasItem(DEFAULT_BANK_NAME)))
            .andExpect(jsonPath("$.[*].bankAddress").value(hasItem(DEFAULT_BANK_ADDRESS)))
            .andExpect(jsonPath("$.[*].recipientName").value(hasItem(DEFAULT_RECIPIENT_NAME)))
            .andExpect(jsonPath("$.[*].recipientAddition").value(hasItem(DEFAULT_RECIPIENT_ADDITION)))
            .andExpect(jsonPath("$.[*].recipientStreet").value(hasItem(DEFAULT_RECIPIENT_STREET)))
            .andExpect(jsonPath("$.[*].recipientCity").value(hasItem(DEFAULT_RECIPIENT_CITY)))
            .andExpect(jsonPath("$.[*].deliveryNumber").value(hasItem(DEFAULT_DELIVERY_NUMBER)))
            .andExpect(jsonPath("$.[*].iban").value(hasItem(DEFAULT_IBAN)))
            .andExpect(jsonPath("$.[*].subscriberNumber").value(hasItem(DEFAULT_SUBSCRIBER_NUMBER)))
            .andExpect(jsonPath("$.[*].leftPrintAdjust").value(hasItem(DEFAULT_LEFT_PRINT_ADJUST.doubleValue())))
            .andExpect(jsonPath("$.[*].topPrintAdjust").value(hasItem(DEFAULT_TOP_PRINT_ADJUST.doubleValue())))
            .andExpect(jsonPath("$.[*].created").value(hasItem(sameInstant(DEFAULT_CREATED))))
            .andExpect(jsonPath("$.[*].inactiv").value(hasItem(DEFAULT_INACTIV.booleanValue())));
    }

    @Test
    @Transactional
    void getIsr() throws Exception {
        // Initialize the database
        isrRepository.saveAndFlush(isr);

        // Get the isr
        restIsrMockMvc
            .perform(get(ENTITY_API_URL_ID, isr.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(isr.getId().intValue()))
            .andExpect(jsonPath("$.defaultIsr").value(DEFAULT_DEFAULT_ISR.booleanValue()))
            .andExpect(jsonPath("$.type").value(DEFAULT_TYPE.toString()))
            .andExpect(jsonPath("$.position").value(DEFAULT_POSITION.toString()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME))
            .andExpect(jsonPath("$.bankName").value(DEFAULT_BANK_NAME))
            .andExpect(jsonPath("$.bankAddress").value(DEFAULT_BANK_ADDRESS))
            .andExpect(jsonPath("$.recipientName").value(DEFAULT_RECIPIENT_NAME))
            .andExpect(jsonPath("$.recipientAddition").value(DEFAULT_RECIPIENT_ADDITION))
            .andExpect(jsonPath("$.recipientStreet").value(DEFAULT_RECIPIENT_STREET))
            .andExpect(jsonPath("$.recipientCity").value(DEFAULT_RECIPIENT_CITY))
            .andExpect(jsonPath("$.deliveryNumber").value(DEFAULT_DELIVERY_NUMBER))
            .andExpect(jsonPath("$.iban").value(DEFAULT_IBAN))
            .andExpect(jsonPath("$.subscriberNumber").value(DEFAULT_SUBSCRIBER_NUMBER))
            .andExpect(jsonPath("$.leftPrintAdjust").value(DEFAULT_LEFT_PRINT_ADJUST.doubleValue()))
            .andExpect(jsonPath("$.topPrintAdjust").value(DEFAULT_TOP_PRINT_ADJUST.doubleValue()))
            .andExpect(jsonPath("$.created").value(sameInstant(DEFAULT_CREATED)))
            .andExpect(jsonPath("$.inactiv").value(DEFAULT_INACTIV.booleanValue()));
    }

    @Test
    @Transactional
    void getNonExistingIsr() throws Exception {
        // Get the isr
        restIsrMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewIsr() throws Exception {
        // Initialize the database
        isrRepository.saveAndFlush(isr);

        int databaseSizeBeforeUpdate = isrRepository.findAll().size();

        // Update the isr
        Isr updatedIsr = isrRepository.findById(isr.getId()).get();
        // Disconnect from session so that the updates on updatedIsr are not directly saved in db
        em.detach(updatedIsr);
        updatedIsr
            .defaultIsr(UPDATED_DEFAULT_ISR)
            .type(UPDATED_TYPE)
            .position(UPDATED_POSITION)
            .name(UPDATED_NAME)
            .bankName(UPDATED_BANK_NAME)
            .bankAddress(UPDATED_BANK_ADDRESS)
            .recipientName(UPDATED_RECIPIENT_NAME)
            .recipientAddition(UPDATED_RECIPIENT_ADDITION)
            .recipientStreet(UPDATED_RECIPIENT_STREET)
            .recipientCity(UPDATED_RECIPIENT_CITY)
            .deliveryNumber(UPDATED_DELIVERY_NUMBER)
            .iban(UPDATED_IBAN)
            .subscriberNumber(UPDATED_SUBSCRIBER_NUMBER)
            .leftPrintAdjust(UPDATED_LEFT_PRINT_ADJUST)
            .topPrintAdjust(UPDATED_TOP_PRINT_ADJUST)
            .created(UPDATED_CREATED)
            .inactiv(UPDATED_INACTIV);
        IsrDTO isrDTO = isrMapper.toDto(updatedIsr);

        restIsrMockMvc
            .perform(
                put(ENTITY_API_URL_ID, isrDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(isrDTO))
            )
            .andExpect(status().isOk());

        // Validate the Isr in the database
        List<Isr> isrList = isrRepository.findAll();
        assertThat(isrList).hasSize(databaseSizeBeforeUpdate);
        Isr testIsr = isrList.get(isrList.size() - 1);
        assertThat(testIsr.getDefaultIsr()).isEqualTo(UPDATED_DEFAULT_ISR);
        assertThat(testIsr.getType()).isEqualTo(UPDATED_TYPE);
        assertThat(testIsr.getPosition()).isEqualTo(UPDATED_POSITION);
        assertThat(testIsr.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testIsr.getBankName()).isEqualTo(UPDATED_BANK_NAME);
        assertThat(testIsr.getBankAddress()).isEqualTo(UPDATED_BANK_ADDRESS);
        assertThat(testIsr.getRecipientName()).isEqualTo(UPDATED_RECIPIENT_NAME);
        assertThat(testIsr.getRecipientAddition()).isEqualTo(UPDATED_RECIPIENT_ADDITION);
        assertThat(testIsr.getRecipientStreet()).isEqualTo(UPDATED_RECIPIENT_STREET);
        assertThat(testIsr.getRecipientCity()).isEqualTo(UPDATED_RECIPIENT_CITY);
        assertThat(testIsr.getDeliveryNumber()).isEqualTo(UPDATED_DELIVERY_NUMBER);
        assertThat(testIsr.getIban()).isEqualTo(UPDATED_IBAN);
        assertThat(testIsr.getSubscriberNumber()).isEqualTo(UPDATED_SUBSCRIBER_NUMBER);
        assertThat(testIsr.getLeftPrintAdjust()).isEqualTo(UPDATED_LEFT_PRINT_ADJUST);
        assertThat(testIsr.getTopPrintAdjust()).isEqualTo(UPDATED_TOP_PRINT_ADJUST);
        assertThat(testIsr.getCreated()).isEqualTo(UPDATED_CREATED);
        assertThat(testIsr.getInactiv()).isEqualTo(UPDATED_INACTIV);
    }

    @Test
    @Transactional
    void putNonExistingIsr() throws Exception {
        int databaseSizeBeforeUpdate = isrRepository.findAll().size();
        isr.setId(count.incrementAndGet());

        // Create the Isr
        IsrDTO isrDTO = isrMapper.toDto(isr);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restIsrMockMvc
            .perform(
                put(ENTITY_API_URL_ID, isrDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(isrDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Isr in the database
        List<Isr> isrList = isrRepository.findAll();
        assertThat(isrList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchIsr() throws Exception {
        int databaseSizeBeforeUpdate = isrRepository.findAll().size();
        isr.setId(count.incrementAndGet());

        // Create the Isr
        IsrDTO isrDTO = isrMapper.toDto(isr);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restIsrMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(isrDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Isr in the database
        List<Isr> isrList = isrRepository.findAll();
        assertThat(isrList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamIsr() throws Exception {
        int databaseSizeBeforeUpdate = isrRepository.findAll().size();
        isr.setId(count.incrementAndGet());

        // Create the Isr
        IsrDTO isrDTO = isrMapper.toDto(isr);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restIsrMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(isrDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Isr in the database
        List<Isr> isrList = isrRepository.findAll();
        assertThat(isrList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateIsrWithPatch() throws Exception {
        // Initialize the database
        isrRepository.saveAndFlush(isr);

        int databaseSizeBeforeUpdate = isrRepository.findAll().size();

        // Update the isr using partial update
        Isr partialUpdatedIsr = new Isr();
        partialUpdatedIsr.setId(isr.getId());

        partialUpdatedIsr
            .defaultIsr(UPDATED_DEFAULT_ISR)
            .bankName(UPDATED_BANK_NAME)
            .bankAddress(UPDATED_BANK_ADDRESS)
            .recipientStreet(UPDATED_RECIPIENT_STREET)
            .recipientCity(UPDATED_RECIPIENT_CITY)
            .iban(UPDATED_IBAN)
            .subscriberNumber(UPDATED_SUBSCRIBER_NUMBER)
            .topPrintAdjust(UPDATED_TOP_PRINT_ADJUST)
            .inactiv(UPDATED_INACTIV);

        restIsrMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedIsr.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedIsr))
            )
            .andExpect(status().isOk());

        // Validate the Isr in the database
        List<Isr> isrList = isrRepository.findAll();
        assertThat(isrList).hasSize(databaseSizeBeforeUpdate);
        Isr testIsr = isrList.get(isrList.size() - 1);
        assertThat(testIsr.getDefaultIsr()).isEqualTo(UPDATED_DEFAULT_ISR);
        assertThat(testIsr.getType()).isEqualTo(DEFAULT_TYPE);
        assertThat(testIsr.getPosition()).isEqualTo(DEFAULT_POSITION);
        assertThat(testIsr.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testIsr.getBankName()).isEqualTo(UPDATED_BANK_NAME);
        assertThat(testIsr.getBankAddress()).isEqualTo(UPDATED_BANK_ADDRESS);
        assertThat(testIsr.getRecipientName()).isEqualTo(DEFAULT_RECIPIENT_NAME);
        assertThat(testIsr.getRecipientAddition()).isEqualTo(DEFAULT_RECIPIENT_ADDITION);
        assertThat(testIsr.getRecipientStreet()).isEqualTo(UPDATED_RECIPIENT_STREET);
        assertThat(testIsr.getRecipientCity()).isEqualTo(UPDATED_RECIPIENT_CITY);
        assertThat(testIsr.getDeliveryNumber()).isEqualTo(DEFAULT_DELIVERY_NUMBER);
        assertThat(testIsr.getIban()).isEqualTo(UPDATED_IBAN);
        assertThat(testIsr.getSubscriberNumber()).isEqualTo(UPDATED_SUBSCRIBER_NUMBER);
        assertThat(testIsr.getLeftPrintAdjust()).isEqualTo(DEFAULT_LEFT_PRINT_ADJUST);
        assertThat(testIsr.getTopPrintAdjust()).isEqualTo(UPDATED_TOP_PRINT_ADJUST);
        assertThat(testIsr.getCreated()).isEqualTo(DEFAULT_CREATED);
        assertThat(testIsr.getInactiv()).isEqualTo(UPDATED_INACTIV);
    }

    @Test
    @Transactional
    void fullUpdateIsrWithPatch() throws Exception {
        // Initialize the database
        isrRepository.saveAndFlush(isr);

        int databaseSizeBeforeUpdate = isrRepository.findAll().size();

        // Update the isr using partial update
        Isr partialUpdatedIsr = new Isr();
        partialUpdatedIsr.setId(isr.getId());

        partialUpdatedIsr
            .defaultIsr(UPDATED_DEFAULT_ISR)
            .type(UPDATED_TYPE)
            .position(UPDATED_POSITION)
            .name(UPDATED_NAME)
            .bankName(UPDATED_BANK_NAME)
            .bankAddress(UPDATED_BANK_ADDRESS)
            .recipientName(UPDATED_RECIPIENT_NAME)
            .recipientAddition(UPDATED_RECIPIENT_ADDITION)
            .recipientStreet(UPDATED_RECIPIENT_STREET)
            .recipientCity(UPDATED_RECIPIENT_CITY)
            .deliveryNumber(UPDATED_DELIVERY_NUMBER)
            .iban(UPDATED_IBAN)
            .subscriberNumber(UPDATED_SUBSCRIBER_NUMBER)
            .leftPrintAdjust(UPDATED_LEFT_PRINT_ADJUST)
            .topPrintAdjust(UPDATED_TOP_PRINT_ADJUST)
            .created(UPDATED_CREATED)
            .inactiv(UPDATED_INACTIV);

        restIsrMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedIsr.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedIsr))
            )
            .andExpect(status().isOk());

        // Validate the Isr in the database
        List<Isr> isrList = isrRepository.findAll();
        assertThat(isrList).hasSize(databaseSizeBeforeUpdate);
        Isr testIsr = isrList.get(isrList.size() - 1);
        assertThat(testIsr.getDefaultIsr()).isEqualTo(UPDATED_DEFAULT_ISR);
        assertThat(testIsr.getType()).isEqualTo(UPDATED_TYPE);
        assertThat(testIsr.getPosition()).isEqualTo(UPDATED_POSITION);
        assertThat(testIsr.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testIsr.getBankName()).isEqualTo(UPDATED_BANK_NAME);
        assertThat(testIsr.getBankAddress()).isEqualTo(UPDATED_BANK_ADDRESS);
        assertThat(testIsr.getRecipientName()).isEqualTo(UPDATED_RECIPIENT_NAME);
        assertThat(testIsr.getRecipientAddition()).isEqualTo(UPDATED_RECIPIENT_ADDITION);
        assertThat(testIsr.getRecipientStreet()).isEqualTo(UPDATED_RECIPIENT_STREET);
        assertThat(testIsr.getRecipientCity()).isEqualTo(UPDATED_RECIPIENT_CITY);
        assertThat(testIsr.getDeliveryNumber()).isEqualTo(UPDATED_DELIVERY_NUMBER);
        assertThat(testIsr.getIban()).isEqualTo(UPDATED_IBAN);
        assertThat(testIsr.getSubscriberNumber()).isEqualTo(UPDATED_SUBSCRIBER_NUMBER);
        assertThat(testIsr.getLeftPrintAdjust()).isEqualTo(UPDATED_LEFT_PRINT_ADJUST);
        assertThat(testIsr.getTopPrintAdjust()).isEqualTo(UPDATED_TOP_PRINT_ADJUST);
        assertThat(testIsr.getCreated()).isEqualTo(UPDATED_CREATED);
        assertThat(testIsr.getInactiv()).isEqualTo(UPDATED_INACTIV);
    }

    @Test
    @Transactional
    void patchNonExistingIsr() throws Exception {
        int databaseSizeBeforeUpdate = isrRepository.findAll().size();
        isr.setId(count.incrementAndGet());

        // Create the Isr
        IsrDTO isrDTO = isrMapper.toDto(isr);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restIsrMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, isrDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(isrDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Isr in the database
        List<Isr> isrList = isrRepository.findAll();
        assertThat(isrList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchIsr() throws Exception {
        int databaseSizeBeforeUpdate = isrRepository.findAll().size();
        isr.setId(count.incrementAndGet());

        // Create the Isr
        IsrDTO isrDTO = isrMapper.toDto(isr);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restIsrMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(isrDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Isr in the database
        List<Isr> isrList = isrRepository.findAll();
        assertThat(isrList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamIsr() throws Exception {
        int databaseSizeBeforeUpdate = isrRepository.findAll().size();
        isr.setId(count.incrementAndGet());

        // Create the Isr
        IsrDTO isrDTO = isrMapper.toDto(isr);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restIsrMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(isrDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Isr in the database
        List<Isr> isrList = isrRepository.findAll();
        assertThat(isrList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteIsr() throws Exception {
        // Initialize the database
        isrRepository.saveAndFlush(isr);

        int databaseSizeBeforeDelete = isrRepository.findAll().size();

        // Delete the isr
        restIsrMockMvc.perform(delete(ENTITY_API_URL_ID, isr.getId()).accept(MediaType.APPLICATION_JSON)).andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Isr> isrList = isrRepository.findAll();
        assertThat(isrList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
