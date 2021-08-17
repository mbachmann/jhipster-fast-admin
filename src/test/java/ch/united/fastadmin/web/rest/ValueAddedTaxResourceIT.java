package ch.united.fastadmin.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import ch.united.fastadmin.IntegrationTest;
import ch.united.fastadmin.domain.ValueAddedTax;
import ch.united.fastadmin.domain.enumeration.VatType;
import ch.united.fastadmin.repository.ValueAddedTaxRepository;
import ch.united.fastadmin.service.criteria.ValueAddedTaxCriteria;
import ch.united.fastadmin.service.dto.ValueAddedTaxDTO;
import ch.united.fastadmin.service.mapper.ValueAddedTaxMapper;
import java.time.LocalDate;
import java.time.ZoneId;
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
 * Integration tests for the {@link ValueAddedTaxResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class ValueAddedTaxResourceIT {

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    private static final VatType DEFAULT_VAT_TYPE = VatType.PERCENT;
    private static final VatType UPDATED_VAT_TYPE = VatType.NO_VAT;

    private static final LocalDate DEFAULT_VALID_FROM = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_VALID_FROM = LocalDate.now(ZoneId.systemDefault());
    private static final LocalDate SMALLER_VALID_FROM = LocalDate.ofEpochDay(-1L);

    private static final LocalDate DEFAULT_VALID_UNTIL = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_VALID_UNTIL = LocalDate.now(ZoneId.systemDefault());
    private static final LocalDate SMALLER_VALID_UNTIL = LocalDate.ofEpochDay(-1L);

    private static final Double DEFAULT_VAT_PERCENT = 1D;
    private static final Double UPDATED_VAT_PERCENT = 2D;
    private static final Double SMALLER_VAT_PERCENT = 1D - 1D;

    private static final Boolean DEFAULT_INACTIV = false;
    private static final Boolean UPDATED_INACTIV = true;

    private static final Integer DEFAULT_NEW_VAT_ID = 1;
    private static final Integer UPDATED_NEW_VAT_ID = 2;
    private static final Integer SMALLER_NEW_VAT_ID = 1 - 1;

    private static final String ENTITY_API_URL = "/api/value-added-taxes";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ValueAddedTaxRepository valueAddedTaxRepository;

    @Autowired
    private ValueAddedTaxMapper valueAddedTaxMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restValueAddedTaxMockMvc;

    private ValueAddedTax valueAddedTax;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static ValueAddedTax createEntity(EntityManager em) {
        ValueAddedTax valueAddedTax = new ValueAddedTax()
            .name(DEFAULT_NAME)
            .vatType(DEFAULT_VAT_TYPE)
            .validFrom(DEFAULT_VALID_FROM)
            .validUntil(DEFAULT_VALID_UNTIL)
            .vatPercent(DEFAULT_VAT_PERCENT)
            .inactiv(DEFAULT_INACTIV)
            .newVatId(DEFAULT_NEW_VAT_ID);
        return valueAddedTax;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static ValueAddedTax createUpdatedEntity(EntityManager em) {
        ValueAddedTax valueAddedTax = new ValueAddedTax()
            .name(UPDATED_NAME)
            .vatType(UPDATED_VAT_TYPE)
            .validFrom(UPDATED_VALID_FROM)
            .validUntil(UPDATED_VALID_UNTIL)
            .vatPercent(UPDATED_VAT_PERCENT)
            .inactiv(UPDATED_INACTIV)
            .newVatId(UPDATED_NEW_VAT_ID);
        return valueAddedTax;
    }

    @BeforeEach
    public void initTest() {
        valueAddedTax = createEntity(em);
    }

    @Test
    @Transactional
    void createValueAddedTax() throws Exception {
        int databaseSizeBeforeCreate = valueAddedTaxRepository.findAll().size();
        // Create the ValueAddedTax
        ValueAddedTaxDTO valueAddedTaxDTO = valueAddedTaxMapper.toDto(valueAddedTax);
        restValueAddedTaxMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(valueAddedTaxDTO))
            )
            .andExpect(status().isCreated());

        // Validate the ValueAddedTax in the database
        List<ValueAddedTax> valueAddedTaxList = valueAddedTaxRepository.findAll();
        assertThat(valueAddedTaxList).hasSize(databaseSizeBeforeCreate + 1);
        ValueAddedTax testValueAddedTax = valueAddedTaxList.get(valueAddedTaxList.size() - 1);
        assertThat(testValueAddedTax.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testValueAddedTax.getVatType()).isEqualTo(DEFAULT_VAT_TYPE);
        assertThat(testValueAddedTax.getValidFrom()).isEqualTo(DEFAULT_VALID_FROM);
        assertThat(testValueAddedTax.getValidUntil()).isEqualTo(DEFAULT_VALID_UNTIL);
        assertThat(testValueAddedTax.getVatPercent()).isEqualTo(DEFAULT_VAT_PERCENT);
        assertThat(testValueAddedTax.getInactiv()).isEqualTo(DEFAULT_INACTIV);
        assertThat(testValueAddedTax.getNewVatId()).isEqualTo(DEFAULT_NEW_VAT_ID);
    }

    @Test
    @Transactional
    void createValueAddedTaxWithExistingId() throws Exception {
        // Create the ValueAddedTax with an existing ID
        valueAddedTax.setId(1L);
        ValueAddedTaxDTO valueAddedTaxDTO = valueAddedTaxMapper.toDto(valueAddedTax);

        int databaseSizeBeforeCreate = valueAddedTaxRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restValueAddedTaxMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(valueAddedTaxDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the ValueAddedTax in the database
        List<ValueAddedTax> valueAddedTaxList = valueAddedTaxRepository.findAll();
        assertThat(valueAddedTaxList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllValueAddedTaxes() throws Exception {
        // Initialize the database
        valueAddedTaxRepository.saveAndFlush(valueAddedTax);

        // Get all the valueAddedTaxList
        restValueAddedTaxMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(valueAddedTax.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)))
            .andExpect(jsonPath("$.[*].vatType").value(hasItem(DEFAULT_VAT_TYPE.toString())))
            .andExpect(jsonPath("$.[*].validFrom").value(hasItem(DEFAULT_VALID_FROM.toString())))
            .andExpect(jsonPath("$.[*].validUntil").value(hasItem(DEFAULT_VALID_UNTIL.toString())))
            .andExpect(jsonPath("$.[*].vatPercent").value(hasItem(DEFAULT_VAT_PERCENT.doubleValue())))
            .andExpect(jsonPath("$.[*].inactiv").value(hasItem(DEFAULT_INACTIV.booleanValue())))
            .andExpect(jsonPath("$.[*].newVatId").value(hasItem(DEFAULT_NEW_VAT_ID)));
    }

    @Test
    @Transactional
    void getValueAddedTax() throws Exception {
        // Initialize the database
        valueAddedTaxRepository.saveAndFlush(valueAddedTax);

        // Get the valueAddedTax
        restValueAddedTaxMockMvc
            .perform(get(ENTITY_API_URL_ID, valueAddedTax.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(valueAddedTax.getId().intValue()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME))
            .andExpect(jsonPath("$.vatType").value(DEFAULT_VAT_TYPE.toString()))
            .andExpect(jsonPath("$.validFrom").value(DEFAULT_VALID_FROM.toString()))
            .andExpect(jsonPath("$.validUntil").value(DEFAULT_VALID_UNTIL.toString()))
            .andExpect(jsonPath("$.vatPercent").value(DEFAULT_VAT_PERCENT.doubleValue()))
            .andExpect(jsonPath("$.inactiv").value(DEFAULT_INACTIV.booleanValue()))
            .andExpect(jsonPath("$.newVatId").value(DEFAULT_NEW_VAT_ID));
    }

    @Test
    @Transactional
    void getValueAddedTaxesByIdFiltering() throws Exception {
        // Initialize the database
        valueAddedTaxRepository.saveAndFlush(valueAddedTax);

        Long id = valueAddedTax.getId();

        defaultValueAddedTaxShouldBeFound("id.equals=" + id);
        defaultValueAddedTaxShouldNotBeFound("id.notEquals=" + id);

        defaultValueAddedTaxShouldBeFound("id.greaterThanOrEqual=" + id);
        defaultValueAddedTaxShouldNotBeFound("id.greaterThan=" + id);

        defaultValueAddedTaxShouldBeFound("id.lessThanOrEqual=" + id);
        defaultValueAddedTaxShouldNotBeFound("id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllValueAddedTaxesByNameIsEqualToSomething() throws Exception {
        // Initialize the database
        valueAddedTaxRepository.saveAndFlush(valueAddedTax);

        // Get all the valueAddedTaxList where name equals to DEFAULT_NAME
        defaultValueAddedTaxShouldBeFound("name.equals=" + DEFAULT_NAME);

        // Get all the valueAddedTaxList where name equals to UPDATED_NAME
        defaultValueAddedTaxShouldNotBeFound("name.equals=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllValueAddedTaxesByNameIsNotEqualToSomething() throws Exception {
        // Initialize the database
        valueAddedTaxRepository.saveAndFlush(valueAddedTax);

        // Get all the valueAddedTaxList where name not equals to DEFAULT_NAME
        defaultValueAddedTaxShouldNotBeFound("name.notEquals=" + DEFAULT_NAME);

        // Get all the valueAddedTaxList where name not equals to UPDATED_NAME
        defaultValueAddedTaxShouldBeFound("name.notEquals=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllValueAddedTaxesByNameIsInShouldWork() throws Exception {
        // Initialize the database
        valueAddedTaxRepository.saveAndFlush(valueAddedTax);

        // Get all the valueAddedTaxList where name in DEFAULT_NAME or UPDATED_NAME
        defaultValueAddedTaxShouldBeFound("name.in=" + DEFAULT_NAME + "," + UPDATED_NAME);

        // Get all the valueAddedTaxList where name equals to UPDATED_NAME
        defaultValueAddedTaxShouldNotBeFound("name.in=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllValueAddedTaxesByNameIsNullOrNotNull() throws Exception {
        // Initialize the database
        valueAddedTaxRepository.saveAndFlush(valueAddedTax);

        // Get all the valueAddedTaxList where name is not null
        defaultValueAddedTaxShouldBeFound("name.specified=true");

        // Get all the valueAddedTaxList where name is null
        defaultValueAddedTaxShouldNotBeFound("name.specified=false");
    }

    @Test
    @Transactional
    void getAllValueAddedTaxesByNameContainsSomething() throws Exception {
        // Initialize the database
        valueAddedTaxRepository.saveAndFlush(valueAddedTax);

        // Get all the valueAddedTaxList where name contains DEFAULT_NAME
        defaultValueAddedTaxShouldBeFound("name.contains=" + DEFAULT_NAME);

        // Get all the valueAddedTaxList where name contains UPDATED_NAME
        defaultValueAddedTaxShouldNotBeFound("name.contains=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllValueAddedTaxesByNameNotContainsSomething() throws Exception {
        // Initialize the database
        valueAddedTaxRepository.saveAndFlush(valueAddedTax);

        // Get all the valueAddedTaxList where name does not contain DEFAULT_NAME
        defaultValueAddedTaxShouldNotBeFound("name.doesNotContain=" + DEFAULT_NAME);

        // Get all the valueAddedTaxList where name does not contain UPDATED_NAME
        defaultValueAddedTaxShouldBeFound("name.doesNotContain=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllValueAddedTaxesByVatTypeIsEqualToSomething() throws Exception {
        // Initialize the database
        valueAddedTaxRepository.saveAndFlush(valueAddedTax);

        // Get all the valueAddedTaxList where vatType equals to DEFAULT_VAT_TYPE
        defaultValueAddedTaxShouldBeFound("vatType.equals=" + DEFAULT_VAT_TYPE);

        // Get all the valueAddedTaxList where vatType equals to UPDATED_VAT_TYPE
        defaultValueAddedTaxShouldNotBeFound("vatType.equals=" + UPDATED_VAT_TYPE);
    }

    @Test
    @Transactional
    void getAllValueAddedTaxesByVatTypeIsNotEqualToSomething() throws Exception {
        // Initialize the database
        valueAddedTaxRepository.saveAndFlush(valueAddedTax);

        // Get all the valueAddedTaxList where vatType not equals to DEFAULT_VAT_TYPE
        defaultValueAddedTaxShouldNotBeFound("vatType.notEquals=" + DEFAULT_VAT_TYPE);

        // Get all the valueAddedTaxList where vatType not equals to UPDATED_VAT_TYPE
        defaultValueAddedTaxShouldBeFound("vatType.notEquals=" + UPDATED_VAT_TYPE);
    }

    @Test
    @Transactional
    void getAllValueAddedTaxesByVatTypeIsInShouldWork() throws Exception {
        // Initialize the database
        valueAddedTaxRepository.saveAndFlush(valueAddedTax);

        // Get all the valueAddedTaxList where vatType in DEFAULT_VAT_TYPE or UPDATED_VAT_TYPE
        defaultValueAddedTaxShouldBeFound("vatType.in=" + DEFAULT_VAT_TYPE + "," + UPDATED_VAT_TYPE);

        // Get all the valueAddedTaxList where vatType equals to UPDATED_VAT_TYPE
        defaultValueAddedTaxShouldNotBeFound("vatType.in=" + UPDATED_VAT_TYPE);
    }

    @Test
    @Transactional
    void getAllValueAddedTaxesByVatTypeIsNullOrNotNull() throws Exception {
        // Initialize the database
        valueAddedTaxRepository.saveAndFlush(valueAddedTax);

        // Get all the valueAddedTaxList where vatType is not null
        defaultValueAddedTaxShouldBeFound("vatType.specified=true");

        // Get all the valueAddedTaxList where vatType is null
        defaultValueAddedTaxShouldNotBeFound("vatType.specified=false");
    }

    @Test
    @Transactional
    void getAllValueAddedTaxesByValidFromIsEqualToSomething() throws Exception {
        // Initialize the database
        valueAddedTaxRepository.saveAndFlush(valueAddedTax);

        // Get all the valueAddedTaxList where validFrom equals to DEFAULT_VALID_FROM
        defaultValueAddedTaxShouldBeFound("validFrom.equals=" + DEFAULT_VALID_FROM);

        // Get all the valueAddedTaxList where validFrom equals to UPDATED_VALID_FROM
        defaultValueAddedTaxShouldNotBeFound("validFrom.equals=" + UPDATED_VALID_FROM);
    }

    @Test
    @Transactional
    void getAllValueAddedTaxesByValidFromIsNotEqualToSomething() throws Exception {
        // Initialize the database
        valueAddedTaxRepository.saveAndFlush(valueAddedTax);

        // Get all the valueAddedTaxList where validFrom not equals to DEFAULT_VALID_FROM
        defaultValueAddedTaxShouldNotBeFound("validFrom.notEquals=" + DEFAULT_VALID_FROM);

        // Get all the valueAddedTaxList where validFrom not equals to UPDATED_VALID_FROM
        defaultValueAddedTaxShouldBeFound("validFrom.notEquals=" + UPDATED_VALID_FROM);
    }

    @Test
    @Transactional
    void getAllValueAddedTaxesByValidFromIsInShouldWork() throws Exception {
        // Initialize the database
        valueAddedTaxRepository.saveAndFlush(valueAddedTax);

        // Get all the valueAddedTaxList where validFrom in DEFAULT_VALID_FROM or UPDATED_VALID_FROM
        defaultValueAddedTaxShouldBeFound("validFrom.in=" + DEFAULT_VALID_FROM + "," + UPDATED_VALID_FROM);

        // Get all the valueAddedTaxList where validFrom equals to UPDATED_VALID_FROM
        defaultValueAddedTaxShouldNotBeFound("validFrom.in=" + UPDATED_VALID_FROM);
    }

    @Test
    @Transactional
    void getAllValueAddedTaxesByValidFromIsNullOrNotNull() throws Exception {
        // Initialize the database
        valueAddedTaxRepository.saveAndFlush(valueAddedTax);

        // Get all the valueAddedTaxList where validFrom is not null
        defaultValueAddedTaxShouldBeFound("validFrom.specified=true");

        // Get all the valueAddedTaxList where validFrom is null
        defaultValueAddedTaxShouldNotBeFound("validFrom.specified=false");
    }

    @Test
    @Transactional
    void getAllValueAddedTaxesByValidFromIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        valueAddedTaxRepository.saveAndFlush(valueAddedTax);

        // Get all the valueAddedTaxList where validFrom is greater than or equal to DEFAULT_VALID_FROM
        defaultValueAddedTaxShouldBeFound("validFrom.greaterThanOrEqual=" + DEFAULT_VALID_FROM);

        // Get all the valueAddedTaxList where validFrom is greater than or equal to UPDATED_VALID_FROM
        defaultValueAddedTaxShouldNotBeFound("validFrom.greaterThanOrEqual=" + UPDATED_VALID_FROM);
    }

    @Test
    @Transactional
    void getAllValueAddedTaxesByValidFromIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        valueAddedTaxRepository.saveAndFlush(valueAddedTax);

        // Get all the valueAddedTaxList where validFrom is less than or equal to DEFAULT_VALID_FROM
        defaultValueAddedTaxShouldBeFound("validFrom.lessThanOrEqual=" + DEFAULT_VALID_FROM);

        // Get all the valueAddedTaxList where validFrom is less than or equal to SMALLER_VALID_FROM
        defaultValueAddedTaxShouldNotBeFound("validFrom.lessThanOrEqual=" + SMALLER_VALID_FROM);
    }

    @Test
    @Transactional
    void getAllValueAddedTaxesByValidFromIsLessThanSomething() throws Exception {
        // Initialize the database
        valueAddedTaxRepository.saveAndFlush(valueAddedTax);

        // Get all the valueAddedTaxList where validFrom is less than DEFAULT_VALID_FROM
        defaultValueAddedTaxShouldNotBeFound("validFrom.lessThan=" + DEFAULT_VALID_FROM);

        // Get all the valueAddedTaxList where validFrom is less than UPDATED_VALID_FROM
        defaultValueAddedTaxShouldBeFound("validFrom.lessThan=" + UPDATED_VALID_FROM);
    }

    @Test
    @Transactional
    void getAllValueAddedTaxesByValidFromIsGreaterThanSomething() throws Exception {
        // Initialize the database
        valueAddedTaxRepository.saveAndFlush(valueAddedTax);

        // Get all the valueAddedTaxList where validFrom is greater than DEFAULT_VALID_FROM
        defaultValueAddedTaxShouldNotBeFound("validFrom.greaterThan=" + DEFAULT_VALID_FROM);

        // Get all the valueAddedTaxList where validFrom is greater than SMALLER_VALID_FROM
        defaultValueAddedTaxShouldBeFound("validFrom.greaterThan=" + SMALLER_VALID_FROM);
    }

    @Test
    @Transactional
    void getAllValueAddedTaxesByValidUntilIsEqualToSomething() throws Exception {
        // Initialize the database
        valueAddedTaxRepository.saveAndFlush(valueAddedTax);

        // Get all the valueAddedTaxList where validUntil equals to DEFAULT_VALID_UNTIL
        defaultValueAddedTaxShouldBeFound("validUntil.equals=" + DEFAULT_VALID_UNTIL);

        // Get all the valueAddedTaxList where validUntil equals to UPDATED_VALID_UNTIL
        defaultValueAddedTaxShouldNotBeFound("validUntil.equals=" + UPDATED_VALID_UNTIL);
    }

    @Test
    @Transactional
    void getAllValueAddedTaxesByValidUntilIsNotEqualToSomething() throws Exception {
        // Initialize the database
        valueAddedTaxRepository.saveAndFlush(valueAddedTax);

        // Get all the valueAddedTaxList where validUntil not equals to DEFAULT_VALID_UNTIL
        defaultValueAddedTaxShouldNotBeFound("validUntil.notEquals=" + DEFAULT_VALID_UNTIL);

        // Get all the valueAddedTaxList where validUntil not equals to UPDATED_VALID_UNTIL
        defaultValueAddedTaxShouldBeFound("validUntil.notEquals=" + UPDATED_VALID_UNTIL);
    }

    @Test
    @Transactional
    void getAllValueAddedTaxesByValidUntilIsInShouldWork() throws Exception {
        // Initialize the database
        valueAddedTaxRepository.saveAndFlush(valueAddedTax);

        // Get all the valueAddedTaxList where validUntil in DEFAULT_VALID_UNTIL or UPDATED_VALID_UNTIL
        defaultValueAddedTaxShouldBeFound("validUntil.in=" + DEFAULT_VALID_UNTIL + "," + UPDATED_VALID_UNTIL);

        // Get all the valueAddedTaxList where validUntil equals to UPDATED_VALID_UNTIL
        defaultValueAddedTaxShouldNotBeFound("validUntil.in=" + UPDATED_VALID_UNTIL);
    }

    @Test
    @Transactional
    void getAllValueAddedTaxesByValidUntilIsNullOrNotNull() throws Exception {
        // Initialize the database
        valueAddedTaxRepository.saveAndFlush(valueAddedTax);

        // Get all the valueAddedTaxList where validUntil is not null
        defaultValueAddedTaxShouldBeFound("validUntil.specified=true");

        // Get all the valueAddedTaxList where validUntil is null
        defaultValueAddedTaxShouldNotBeFound("validUntil.specified=false");
    }

    @Test
    @Transactional
    void getAllValueAddedTaxesByValidUntilIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        valueAddedTaxRepository.saveAndFlush(valueAddedTax);

        // Get all the valueAddedTaxList where validUntil is greater than or equal to DEFAULT_VALID_UNTIL
        defaultValueAddedTaxShouldBeFound("validUntil.greaterThanOrEqual=" + DEFAULT_VALID_UNTIL);

        // Get all the valueAddedTaxList where validUntil is greater than or equal to UPDATED_VALID_UNTIL
        defaultValueAddedTaxShouldNotBeFound("validUntil.greaterThanOrEqual=" + UPDATED_VALID_UNTIL);
    }

    @Test
    @Transactional
    void getAllValueAddedTaxesByValidUntilIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        valueAddedTaxRepository.saveAndFlush(valueAddedTax);

        // Get all the valueAddedTaxList where validUntil is less than or equal to DEFAULT_VALID_UNTIL
        defaultValueAddedTaxShouldBeFound("validUntil.lessThanOrEqual=" + DEFAULT_VALID_UNTIL);

        // Get all the valueAddedTaxList where validUntil is less than or equal to SMALLER_VALID_UNTIL
        defaultValueAddedTaxShouldNotBeFound("validUntil.lessThanOrEqual=" + SMALLER_VALID_UNTIL);
    }

    @Test
    @Transactional
    void getAllValueAddedTaxesByValidUntilIsLessThanSomething() throws Exception {
        // Initialize the database
        valueAddedTaxRepository.saveAndFlush(valueAddedTax);

        // Get all the valueAddedTaxList where validUntil is less than DEFAULT_VALID_UNTIL
        defaultValueAddedTaxShouldNotBeFound("validUntil.lessThan=" + DEFAULT_VALID_UNTIL);

        // Get all the valueAddedTaxList where validUntil is less than UPDATED_VALID_UNTIL
        defaultValueAddedTaxShouldBeFound("validUntil.lessThan=" + UPDATED_VALID_UNTIL);
    }

    @Test
    @Transactional
    void getAllValueAddedTaxesByValidUntilIsGreaterThanSomething() throws Exception {
        // Initialize the database
        valueAddedTaxRepository.saveAndFlush(valueAddedTax);

        // Get all the valueAddedTaxList where validUntil is greater than DEFAULT_VALID_UNTIL
        defaultValueAddedTaxShouldNotBeFound("validUntil.greaterThan=" + DEFAULT_VALID_UNTIL);

        // Get all the valueAddedTaxList where validUntil is greater than SMALLER_VALID_UNTIL
        defaultValueAddedTaxShouldBeFound("validUntil.greaterThan=" + SMALLER_VALID_UNTIL);
    }

    @Test
    @Transactional
    void getAllValueAddedTaxesByVatPercentIsEqualToSomething() throws Exception {
        // Initialize the database
        valueAddedTaxRepository.saveAndFlush(valueAddedTax);

        // Get all the valueAddedTaxList where vatPercent equals to DEFAULT_VAT_PERCENT
        defaultValueAddedTaxShouldBeFound("vatPercent.equals=" + DEFAULT_VAT_PERCENT);

        // Get all the valueAddedTaxList where vatPercent equals to UPDATED_VAT_PERCENT
        defaultValueAddedTaxShouldNotBeFound("vatPercent.equals=" + UPDATED_VAT_PERCENT);
    }

    @Test
    @Transactional
    void getAllValueAddedTaxesByVatPercentIsNotEqualToSomething() throws Exception {
        // Initialize the database
        valueAddedTaxRepository.saveAndFlush(valueAddedTax);

        // Get all the valueAddedTaxList where vatPercent not equals to DEFAULT_VAT_PERCENT
        defaultValueAddedTaxShouldNotBeFound("vatPercent.notEquals=" + DEFAULT_VAT_PERCENT);

        // Get all the valueAddedTaxList where vatPercent not equals to UPDATED_VAT_PERCENT
        defaultValueAddedTaxShouldBeFound("vatPercent.notEquals=" + UPDATED_VAT_PERCENT);
    }

    @Test
    @Transactional
    void getAllValueAddedTaxesByVatPercentIsInShouldWork() throws Exception {
        // Initialize the database
        valueAddedTaxRepository.saveAndFlush(valueAddedTax);

        // Get all the valueAddedTaxList where vatPercent in DEFAULT_VAT_PERCENT or UPDATED_VAT_PERCENT
        defaultValueAddedTaxShouldBeFound("vatPercent.in=" + DEFAULT_VAT_PERCENT + "," + UPDATED_VAT_PERCENT);

        // Get all the valueAddedTaxList where vatPercent equals to UPDATED_VAT_PERCENT
        defaultValueAddedTaxShouldNotBeFound("vatPercent.in=" + UPDATED_VAT_PERCENT);
    }

    @Test
    @Transactional
    void getAllValueAddedTaxesByVatPercentIsNullOrNotNull() throws Exception {
        // Initialize the database
        valueAddedTaxRepository.saveAndFlush(valueAddedTax);

        // Get all the valueAddedTaxList where vatPercent is not null
        defaultValueAddedTaxShouldBeFound("vatPercent.specified=true");

        // Get all the valueAddedTaxList where vatPercent is null
        defaultValueAddedTaxShouldNotBeFound("vatPercent.specified=false");
    }

    @Test
    @Transactional
    void getAllValueAddedTaxesByVatPercentIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        valueAddedTaxRepository.saveAndFlush(valueAddedTax);

        // Get all the valueAddedTaxList where vatPercent is greater than or equal to DEFAULT_VAT_PERCENT
        defaultValueAddedTaxShouldBeFound("vatPercent.greaterThanOrEqual=" + DEFAULT_VAT_PERCENT);

        // Get all the valueAddedTaxList where vatPercent is greater than or equal to UPDATED_VAT_PERCENT
        defaultValueAddedTaxShouldNotBeFound("vatPercent.greaterThanOrEqual=" + UPDATED_VAT_PERCENT);
    }

    @Test
    @Transactional
    void getAllValueAddedTaxesByVatPercentIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        valueAddedTaxRepository.saveAndFlush(valueAddedTax);

        // Get all the valueAddedTaxList where vatPercent is less than or equal to DEFAULT_VAT_PERCENT
        defaultValueAddedTaxShouldBeFound("vatPercent.lessThanOrEqual=" + DEFAULT_VAT_PERCENT);

        // Get all the valueAddedTaxList where vatPercent is less than or equal to SMALLER_VAT_PERCENT
        defaultValueAddedTaxShouldNotBeFound("vatPercent.lessThanOrEqual=" + SMALLER_VAT_PERCENT);
    }

    @Test
    @Transactional
    void getAllValueAddedTaxesByVatPercentIsLessThanSomething() throws Exception {
        // Initialize the database
        valueAddedTaxRepository.saveAndFlush(valueAddedTax);

        // Get all the valueAddedTaxList where vatPercent is less than DEFAULT_VAT_PERCENT
        defaultValueAddedTaxShouldNotBeFound("vatPercent.lessThan=" + DEFAULT_VAT_PERCENT);

        // Get all the valueAddedTaxList where vatPercent is less than UPDATED_VAT_PERCENT
        defaultValueAddedTaxShouldBeFound("vatPercent.lessThan=" + UPDATED_VAT_PERCENT);
    }

    @Test
    @Transactional
    void getAllValueAddedTaxesByVatPercentIsGreaterThanSomething() throws Exception {
        // Initialize the database
        valueAddedTaxRepository.saveAndFlush(valueAddedTax);

        // Get all the valueAddedTaxList where vatPercent is greater than DEFAULT_VAT_PERCENT
        defaultValueAddedTaxShouldNotBeFound("vatPercent.greaterThan=" + DEFAULT_VAT_PERCENT);

        // Get all the valueAddedTaxList where vatPercent is greater than SMALLER_VAT_PERCENT
        defaultValueAddedTaxShouldBeFound("vatPercent.greaterThan=" + SMALLER_VAT_PERCENT);
    }

    @Test
    @Transactional
    void getAllValueAddedTaxesByInactivIsEqualToSomething() throws Exception {
        // Initialize the database
        valueAddedTaxRepository.saveAndFlush(valueAddedTax);

        // Get all the valueAddedTaxList where inactiv equals to DEFAULT_INACTIV
        defaultValueAddedTaxShouldBeFound("inactiv.equals=" + DEFAULT_INACTIV);

        // Get all the valueAddedTaxList where inactiv equals to UPDATED_INACTIV
        defaultValueAddedTaxShouldNotBeFound("inactiv.equals=" + UPDATED_INACTIV);
    }

    @Test
    @Transactional
    void getAllValueAddedTaxesByInactivIsNotEqualToSomething() throws Exception {
        // Initialize the database
        valueAddedTaxRepository.saveAndFlush(valueAddedTax);

        // Get all the valueAddedTaxList where inactiv not equals to DEFAULT_INACTIV
        defaultValueAddedTaxShouldNotBeFound("inactiv.notEquals=" + DEFAULT_INACTIV);

        // Get all the valueAddedTaxList where inactiv not equals to UPDATED_INACTIV
        defaultValueAddedTaxShouldBeFound("inactiv.notEquals=" + UPDATED_INACTIV);
    }

    @Test
    @Transactional
    void getAllValueAddedTaxesByInactivIsInShouldWork() throws Exception {
        // Initialize the database
        valueAddedTaxRepository.saveAndFlush(valueAddedTax);

        // Get all the valueAddedTaxList where inactiv in DEFAULT_INACTIV or UPDATED_INACTIV
        defaultValueAddedTaxShouldBeFound("inactiv.in=" + DEFAULT_INACTIV + "," + UPDATED_INACTIV);

        // Get all the valueAddedTaxList where inactiv equals to UPDATED_INACTIV
        defaultValueAddedTaxShouldNotBeFound("inactiv.in=" + UPDATED_INACTIV);
    }

    @Test
    @Transactional
    void getAllValueAddedTaxesByInactivIsNullOrNotNull() throws Exception {
        // Initialize the database
        valueAddedTaxRepository.saveAndFlush(valueAddedTax);

        // Get all the valueAddedTaxList where inactiv is not null
        defaultValueAddedTaxShouldBeFound("inactiv.specified=true");

        // Get all the valueAddedTaxList where inactiv is null
        defaultValueAddedTaxShouldNotBeFound("inactiv.specified=false");
    }

    @Test
    @Transactional
    void getAllValueAddedTaxesByNewVatIdIsEqualToSomething() throws Exception {
        // Initialize the database
        valueAddedTaxRepository.saveAndFlush(valueAddedTax);

        // Get all the valueAddedTaxList where newVatId equals to DEFAULT_NEW_VAT_ID
        defaultValueAddedTaxShouldBeFound("newVatId.equals=" + DEFAULT_NEW_VAT_ID);

        // Get all the valueAddedTaxList where newVatId equals to UPDATED_NEW_VAT_ID
        defaultValueAddedTaxShouldNotBeFound("newVatId.equals=" + UPDATED_NEW_VAT_ID);
    }

    @Test
    @Transactional
    void getAllValueAddedTaxesByNewVatIdIsNotEqualToSomething() throws Exception {
        // Initialize the database
        valueAddedTaxRepository.saveAndFlush(valueAddedTax);

        // Get all the valueAddedTaxList where newVatId not equals to DEFAULT_NEW_VAT_ID
        defaultValueAddedTaxShouldNotBeFound("newVatId.notEquals=" + DEFAULT_NEW_VAT_ID);

        // Get all the valueAddedTaxList where newVatId not equals to UPDATED_NEW_VAT_ID
        defaultValueAddedTaxShouldBeFound("newVatId.notEquals=" + UPDATED_NEW_VAT_ID);
    }

    @Test
    @Transactional
    void getAllValueAddedTaxesByNewVatIdIsInShouldWork() throws Exception {
        // Initialize the database
        valueAddedTaxRepository.saveAndFlush(valueAddedTax);

        // Get all the valueAddedTaxList where newVatId in DEFAULT_NEW_VAT_ID or UPDATED_NEW_VAT_ID
        defaultValueAddedTaxShouldBeFound("newVatId.in=" + DEFAULT_NEW_VAT_ID + "," + UPDATED_NEW_VAT_ID);

        // Get all the valueAddedTaxList where newVatId equals to UPDATED_NEW_VAT_ID
        defaultValueAddedTaxShouldNotBeFound("newVatId.in=" + UPDATED_NEW_VAT_ID);
    }

    @Test
    @Transactional
    void getAllValueAddedTaxesByNewVatIdIsNullOrNotNull() throws Exception {
        // Initialize the database
        valueAddedTaxRepository.saveAndFlush(valueAddedTax);

        // Get all the valueAddedTaxList where newVatId is not null
        defaultValueAddedTaxShouldBeFound("newVatId.specified=true");

        // Get all the valueAddedTaxList where newVatId is null
        defaultValueAddedTaxShouldNotBeFound("newVatId.specified=false");
    }

    @Test
    @Transactional
    void getAllValueAddedTaxesByNewVatIdIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        valueAddedTaxRepository.saveAndFlush(valueAddedTax);

        // Get all the valueAddedTaxList where newVatId is greater than or equal to DEFAULT_NEW_VAT_ID
        defaultValueAddedTaxShouldBeFound("newVatId.greaterThanOrEqual=" + DEFAULT_NEW_VAT_ID);

        // Get all the valueAddedTaxList where newVatId is greater than or equal to UPDATED_NEW_VAT_ID
        defaultValueAddedTaxShouldNotBeFound("newVatId.greaterThanOrEqual=" + UPDATED_NEW_VAT_ID);
    }

    @Test
    @Transactional
    void getAllValueAddedTaxesByNewVatIdIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        valueAddedTaxRepository.saveAndFlush(valueAddedTax);

        // Get all the valueAddedTaxList where newVatId is less than or equal to DEFAULT_NEW_VAT_ID
        defaultValueAddedTaxShouldBeFound("newVatId.lessThanOrEqual=" + DEFAULT_NEW_VAT_ID);

        // Get all the valueAddedTaxList where newVatId is less than or equal to SMALLER_NEW_VAT_ID
        defaultValueAddedTaxShouldNotBeFound("newVatId.lessThanOrEqual=" + SMALLER_NEW_VAT_ID);
    }

    @Test
    @Transactional
    void getAllValueAddedTaxesByNewVatIdIsLessThanSomething() throws Exception {
        // Initialize the database
        valueAddedTaxRepository.saveAndFlush(valueAddedTax);

        // Get all the valueAddedTaxList where newVatId is less than DEFAULT_NEW_VAT_ID
        defaultValueAddedTaxShouldNotBeFound("newVatId.lessThan=" + DEFAULT_NEW_VAT_ID);

        // Get all the valueAddedTaxList where newVatId is less than UPDATED_NEW_VAT_ID
        defaultValueAddedTaxShouldBeFound("newVatId.lessThan=" + UPDATED_NEW_VAT_ID);
    }

    @Test
    @Transactional
    void getAllValueAddedTaxesByNewVatIdIsGreaterThanSomething() throws Exception {
        // Initialize the database
        valueAddedTaxRepository.saveAndFlush(valueAddedTax);

        // Get all the valueAddedTaxList where newVatId is greater than DEFAULT_NEW_VAT_ID
        defaultValueAddedTaxShouldNotBeFound("newVatId.greaterThan=" + DEFAULT_NEW_VAT_ID);

        // Get all the valueAddedTaxList where newVatId is greater than SMALLER_NEW_VAT_ID
        defaultValueAddedTaxShouldBeFound("newVatId.greaterThan=" + SMALLER_NEW_VAT_ID);
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultValueAddedTaxShouldBeFound(String filter) throws Exception {
        restValueAddedTaxMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(valueAddedTax.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)))
            .andExpect(jsonPath("$.[*].vatType").value(hasItem(DEFAULT_VAT_TYPE.toString())))
            .andExpect(jsonPath("$.[*].validFrom").value(hasItem(DEFAULT_VALID_FROM.toString())))
            .andExpect(jsonPath("$.[*].validUntil").value(hasItem(DEFAULT_VALID_UNTIL.toString())))
            .andExpect(jsonPath("$.[*].vatPercent").value(hasItem(DEFAULT_VAT_PERCENT.doubleValue())))
            .andExpect(jsonPath("$.[*].inactiv").value(hasItem(DEFAULT_INACTIV.booleanValue())))
            .andExpect(jsonPath("$.[*].newVatId").value(hasItem(DEFAULT_NEW_VAT_ID)));

        // Check, that the count call also returns 1
        restValueAddedTaxMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultValueAddedTaxShouldNotBeFound(String filter) throws Exception {
        restValueAddedTaxMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restValueAddedTaxMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingValueAddedTax() throws Exception {
        // Get the valueAddedTax
        restValueAddedTaxMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewValueAddedTax() throws Exception {
        // Initialize the database
        valueAddedTaxRepository.saveAndFlush(valueAddedTax);

        int databaseSizeBeforeUpdate = valueAddedTaxRepository.findAll().size();

        // Update the valueAddedTax
        ValueAddedTax updatedValueAddedTax = valueAddedTaxRepository.findById(valueAddedTax.getId()).get();
        // Disconnect from session so that the updates on updatedValueAddedTax are not directly saved in db
        em.detach(updatedValueAddedTax);
        updatedValueAddedTax
            .name(UPDATED_NAME)
            .vatType(UPDATED_VAT_TYPE)
            .validFrom(UPDATED_VALID_FROM)
            .validUntil(UPDATED_VALID_UNTIL)
            .vatPercent(UPDATED_VAT_PERCENT)
            .inactiv(UPDATED_INACTIV)
            .newVatId(UPDATED_NEW_VAT_ID);
        ValueAddedTaxDTO valueAddedTaxDTO = valueAddedTaxMapper.toDto(updatedValueAddedTax);

        restValueAddedTaxMockMvc
            .perform(
                put(ENTITY_API_URL_ID, valueAddedTaxDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(valueAddedTaxDTO))
            )
            .andExpect(status().isOk());

        // Validate the ValueAddedTax in the database
        List<ValueAddedTax> valueAddedTaxList = valueAddedTaxRepository.findAll();
        assertThat(valueAddedTaxList).hasSize(databaseSizeBeforeUpdate);
        ValueAddedTax testValueAddedTax = valueAddedTaxList.get(valueAddedTaxList.size() - 1);
        assertThat(testValueAddedTax.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testValueAddedTax.getVatType()).isEqualTo(UPDATED_VAT_TYPE);
        assertThat(testValueAddedTax.getValidFrom()).isEqualTo(UPDATED_VALID_FROM);
        assertThat(testValueAddedTax.getValidUntil()).isEqualTo(UPDATED_VALID_UNTIL);
        assertThat(testValueAddedTax.getVatPercent()).isEqualTo(UPDATED_VAT_PERCENT);
        assertThat(testValueAddedTax.getInactiv()).isEqualTo(UPDATED_INACTIV);
        assertThat(testValueAddedTax.getNewVatId()).isEqualTo(UPDATED_NEW_VAT_ID);
    }

    @Test
    @Transactional
    void putNonExistingValueAddedTax() throws Exception {
        int databaseSizeBeforeUpdate = valueAddedTaxRepository.findAll().size();
        valueAddedTax.setId(count.incrementAndGet());

        // Create the ValueAddedTax
        ValueAddedTaxDTO valueAddedTaxDTO = valueAddedTaxMapper.toDto(valueAddedTax);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restValueAddedTaxMockMvc
            .perform(
                put(ENTITY_API_URL_ID, valueAddedTaxDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(valueAddedTaxDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the ValueAddedTax in the database
        List<ValueAddedTax> valueAddedTaxList = valueAddedTaxRepository.findAll();
        assertThat(valueAddedTaxList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchValueAddedTax() throws Exception {
        int databaseSizeBeforeUpdate = valueAddedTaxRepository.findAll().size();
        valueAddedTax.setId(count.incrementAndGet());

        // Create the ValueAddedTax
        ValueAddedTaxDTO valueAddedTaxDTO = valueAddedTaxMapper.toDto(valueAddedTax);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restValueAddedTaxMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(valueAddedTaxDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the ValueAddedTax in the database
        List<ValueAddedTax> valueAddedTaxList = valueAddedTaxRepository.findAll();
        assertThat(valueAddedTaxList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamValueAddedTax() throws Exception {
        int databaseSizeBeforeUpdate = valueAddedTaxRepository.findAll().size();
        valueAddedTax.setId(count.incrementAndGet());

        // Create the ValueAddedTax
        ValueAddedTaxDTO valueAddedTaxDTO = valueAddedTaxMapper.toDto(valueAddedTax);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restValueAddedTaxMockMvc
            .perform(
                put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(valueAddedTaxDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the ValueAddedTax in the database
        List<ValueAddedTax> valueAddedTaxList = valueAddedTaxRepository.findAll();
        assertThat(valueAddedTaxList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateValueAddedTaxWithPatch() throws Exception {
        // Initialize the database
        valueAddedTaxRepository.saveAndFlush(valueAddedTax);

        int databaseSizeBeforeUpdate = valueAddedTaxRepository.findAll().size();

        // Update the valueAddedTax using partial update
        ValueAddedTax partialUpdatedValueAddedTax = new ValueAddedTax();
        partialUpdatedValueAddedTax.setId(valueAddedTax.getId());

        partialUpdatedValueAddedTax.name(UPDATED_NAME).newVatId(UPDATED_NEW_VAT_ID);

        restValueAddedTaxMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedValueAddedTax.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedValueAddedTax))
            )
            .andExpect(status().isOk());

        // Validate the ValueAddedTax in the database
        List<ValueAddedTax> valueAddedTaxList = valueAddedTaxRepository.findAll();
        assertThat(valueAddedTaxList).hasSize(databaseSizeBeforeUpdate);
        ValueAddedTax testValueAddedTax = valueAddedTaxList.get(valueAddedTaxList.size() - 1);
        assertThat(testValueAddedTax.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testValueAddedTax.getVatType()).isEqualTo(DEFAULT_VAT_TYPE);
        assertThat(testValueAddedTax.getValidFrom()).isEqualTo(DEFAULT_VALID_FROM);
        assertThat(testValueAddedTax.getValidUntil()).isEqualTo(DEFAULT_VALID_UNTIL);
        assertThat(testValueAddedTax.getVatPercent()).isEqualTo(DEFAULT_VAT_PERCENT);
        assertThat(testValueAddedTax.getInactiv()).isEqualTo(DEFAULT_INACTIV);
        assertThat(testValueAddedTax.getNewVatId()).isEqualTo(UPDATED_NEW_VAT_ID);
    }

    @Test
    @Transactional
    void fullUpdateValueAddedTaxWithPatch() throws Exception {
        // Initialize the database
        valueAddedTaxRepository.saveAndFlush(valueAddedTax);

        int databaseSizeBeforeUpdate = valueAddedTaxRepository.findAll().size();

        // Update the valueAddedTax using partial update
        ValueAddedTax partialUpdatedValueAddedTax = new ValueAddedTax();
        partialUpdatedValueAddedTax.setId(valueAddedTax.getId());

        partialUpdatedValueAddedTax
            .name(UPDATED_NAME)
            .vatType(UPDATED_VAT_TYPE)
            .validFrom(UPDATED_VALID_FROM)
            .validUntil(UPDATED_VALID_UNTIL)
            .vatPercent(UPDATED_VAT_PERCENT)
            .inactiv(UPDATED_INACTIV)
            .newVatId(UPDATED_NEW_VAT_ID);

        restValueAddedTaxMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedValueAddedTax.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedValueAddedTax))
            )
            .andExpect(status().isOk());

        // Validate the ValueAddedTax in the database
        List<ValueAddedTax> valueAddedTaxList = valueAddedTaxRepository.findAll();
        assertThat(valueAddedTaxList).hasSize(databaseSizeBeforeUpdate);
        ValueAddedTax testValueAddedTax = valueAddedTaxList.get(valueAddedTaxList.size() - 1);
        assertThat(testValueAddedTax.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testValueAddedTax.getVatType()).isEqualTo(UPDATED_VAT_TYPE);
        assertThat(testValueAddedTax.getValidFrom()).isEqualTo(UPDATED_VALID_FROM);
        assertThat(testValueAddedTax.getValidUntil()).isEqualTo(UPDATED_VALID_UNTIL);
        assertThat(testValueAddedTax.getVatPercent()).isEqualTo(UPDATED_VAT_PERCENT);
        assertThat(testValueAddedTax.getInactiv()).isEqualTo(UPDATED_INACTIV);
        assertThat(testValueAddedTax.getNewVatId()).isEqualTo(UPDATED_NEW_VAT_ID);
    }

    @Test
    @Transactional
    void patchNonExistingValueAddedTax() throws Exception {
        int databaseSizeBeforeUpdate = valueAddedTaxRepository.findAll().size();
        valueAddedTax.setId(count.incrementAndGet());

        // Create the ValueAddedTax
        ValueAddedTaxDTO valueAddedTaxDTO = valueAddedTaxMapper.toDto(valueAddedTax);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restValueAddedTaxMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, valueAddedTaxDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(valueAddedTaxDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the ValueAddedTax in the database
        List<ValueAddedTax> valueAddedTaxList = valueAddedTaxRepository.findAll();
        assertThat(valueAddedTaxList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchValueAddedTax() throws Exception {
        int databaseSizeBeforeUpdate = valueAddedTaxRepository.findAll().size();
        valueAddedTax.setId(count.incrementAndGet());

        // Create the ValueAddedTax
        ValueAddedTaxDTO valueAddedTaxDTO = valueAddedTaxMapper.toDto(valueAddedTax);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restValueAddedTaxMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(valueAddedTaxDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the ValueAddedTax in the database
        List<ValueAddedTax> valueAddedTaxList = valueAddedTaxRepository.findAll();
        assertThat(valueAddedTaxList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamValueAddedTax() throws Exception {
        int databaseSizeBeforeUpdate = valueAddedTaxRepository.findAll().size();
        valueAddedTax.setId(count.incrementAndGet());

        // Create the ValueAddedTax
        ValueAddedTaxDTO valueAddedTaxDTO = valueAddedTaxMapper.toDto(valueAddedTax);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restValueAddedTaxMockMvc
            .perform(
                patch(ENTITY_API_URL)
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(valueAddedTaxDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the ValueAddedTax in the database
        List<ValueAddedTax> valueAddedTaxList = valueAddedTaxRepository.findAll();
        assertThat(valueAddedTaxList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteValueAddedTax() throws Exception {
        // Initialize the database
        valueAddedTaxRepository.saveAndFlush(valueAddedTax);

        int databaseSizeBeforeDelete = valueAddedTaxRepository.findAll().size();

        // Delete the valueAddedTax
        restValueAddedTaxMockMvc
            .perform(delete(ENTITY_API_URL_ID, valueAddedTax.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<ValueAddedTax> valueAddedTaxList = valueAddedTaxRepository.findAll();
        assertThat(valueAddedTaxList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
