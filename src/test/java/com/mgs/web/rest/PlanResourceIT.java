package com.mgs.web.rest;

import static com.mgs.domain.PlanAsserts.*;
import static com.mgs.web.rest.TestUtil.createUpdateProxyForBean;
import static com.mgs.web.rest.TestUtil.sameNumber;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mgs.IntegrationTest;
import com.mgs.domain.Plan;
import com.mgs.repository.PlanRepository;
import com.mgs.service.dto.PlanDTO;
import com.mgs.service.mapper.PlanMapper;
import jakarta.persistence.EntityManager;
import java.math.BigDecimal;
import java.util.Random;
import java.util.concurrent.atomic.AtomicLong;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

/**
 * Integration tests for the {@link PlanResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class PlanResourceIT {

    private static final String DEFAULT_CODE = "AAAAAAAAAA";
    private static final String UPDATED_CODE = "BBBBBBBBBB";

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_DESCRIPTION = "AAAAAAAAAA";
    private static final String UPDATED_DESCRIPTION = "BBBBBBBBBB";

    private static final Integer DEFAULT_MAX_USERS = 1;
    private static final Integer UPDATED_MAX_USERS = 2;
    private static final Integer SMALLER_MAX_USERS = 1 - 1;

    private static final Integer DEFAULT_MAX_STORAGE_MB = 1;
    private static final Integer UPDATED_MAX_STORAGE_MB = 2;
    private static final Integer SMALLER_MAX_STORAGE_MB = 1 - 1;

    private static final Integer DEFAULT_MAX_CUSTOMERS = 1;
    private static final Integer UPDATED_MAX_CUSTOMERS = 2;
    private static final Integer SMALLER_MAX_CUSTOMERS = 1 - 1;

    private static final Integer DEFAULT_MAX_CONTACTS = 1;
    private static final Integer UPDATED_MAX_CONTACTS = 2;
    private static final Integer SMALLER_MAX_CONTACTS = 1 - 1;

    private static final Integer DEFAULT_MAX_QUOTATIONS = 1;
    private static final Integer UPDATED_MAX_QUOTATIONS = 2;
    private static final Integer SMALLER_MAX_QUOTATIONS = 1 - 1;

    private static final Integer DEFAULT_MAX_COMPLAINTS = 1;
    private static final Integer UPDATED_MAX_COMPLAINTS = 2;
    private static final Integer SMALLER_MAX_COMPLAINTS = 1 - 1;

    private static final BigDecimal DEFAULT_PRICE_PER_MONTH = new BigDecimal(1);
    private static final BigDecimal UPDATED_PRICE_PER_MONTH = new BigDecimal(2);
    private static final BigDecimal SMALLER_PRICE_PER_MONTH = new BigDecimal(1 - 1);

    private static final String DEFAULT_CURRENCY = "AAAAAAAAAA";
    private static final String UPDATED_CURRENCY = "BBBBBBBBBB";

    private static final Boolean DEFAULT_IS_ACTIVE = false;
    private static final Boolean UPDATED_IS_ACTIVE = true;

    private static final String ENTITY_API_URL = "/api/plans";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ObjectMapper om;

    @Autowired
    private PlanRepository planRepository;

    @Autowired
    private PlanMapper planMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restPlanMockMvc;

    private Plan plan;

    private Plan insertedPlan;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Plan createEntity() {
        return new Plan()
            .code(DEFAULT_CODE)
            .name(DEFAULT_NAME)
            .description(DEFAULT_DESCRIPTION)
            .maxUsers(DEFAULT_MAX_USERS)
            .maxStorageMb(DEFAULT_MAX_STORAGE_MB)
            .maxCustomers(DEFAULT_MAX_CUSTOMERS)
            .maxContacts(DEFAULT_MAX_CONTACTS)
            .maxQuotations(DEFAULT_MAX_QUOTATIONS)
            .maxComplaints(DEFAULT_MAX_COMPLAINTS)
            .pricePerMonth(DEFAULT_PRICE_PER_MONTH)
            .currency(DEFAULT_CURRENCY)
            .isActive(DEFAULT_IS_ACTIVE);
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Plan createUpdatedEntity() {
        return new Plan()
            .code(UPDATED_CODE)
            .name(UPDATED_NAME)
            .description(UPDATED_DESCRIPTION)
            .maxUsers(UPDATED_MAX_USERS)
            .maxStorageMb(UPDATED_MAX_STORAGE_MB)
            .maxCustomers(UPDATED_MAX_CUSTOMERS)
            .maxContacts(UPDATED_MAX_CONTACTS)
            .maxQuotations(UPDATED_MAX_QUOTATIONS)
            .maxComplaints(UPDATED_MAX_COMPLAINTS)
            .pricePerMonth(UPDATED_PRICE_PER_MONTH)
            .currency(UPDATED_CURRENCY)
            .isActive(UPDATED_IS_ACTIVE);
    }

    @BeforeEach
    void initTest() {
        plan = createEntity();
    }

    @AfterEach
    void cleanup() {
        if (insertedPlan != null) {
            planRepository.delete(insertedPlan);
            insertedPlan = null;
        }
    }

    @Test
    @Transactional
    void createPlan() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the Plan
        PlanDTO planDTO = planMapper.toDto(plan);
        var returnedPlanDTO = om.readValue(
            restPlanMockMvc
                .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(planDTO)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            PlanDTO.class
        );

        // Validate the Plan in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        var returnedPlan = planMapper.toEntity(returnedPlanDTO);
        assertPlanUpdatableFieldsEquals(returnedPlan, getPersistedPlan(returnedPlan));

        insertedPlan = returnedPlan;
    }

    @Test
    @Transactional
    void createPlanWithExistingId() throws Exception {
        // Create the Plan with an existing ID
        plan.setId(1L);
        PlanDTO planDTO = planMapper.toDto(plan);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restPlanMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(planDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Plan in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkCodeIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        plan.setCode(null);

        // Create the Plan, which fails.
        PlanDTO planDTO = planMapper.toDto(plan);

        restPlanMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(planDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkNameIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        plan.setName(null);

        // Create the Plan, which fails.
        PlanDTO planDTO = planMapper.toDto(plan);

        restPlanMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(planDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkPricePerMonthIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        plan.setPricePerMonth(null);

        // Create the Plan, which fails.
        PlanDTO planDTO = planMapper.toDto(plan);

        restPlanMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(planDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkCurrencyIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        plan.setCurrency(null);

        // Create the Plan, which fails.
        PlanDTO planDTO = planMapper.toDto(plan);

        restPlanMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(planDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkIsActiveIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        plan.setIsActive(null);

        // Create the Plan, which fails.
        PlanDTO planDTO = planMapper.toDto(plan);

        restPlanMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(planDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllPlans() throws Exception {
        // Initialize the database
        insertedPlan = planRepository.saveAndFlush(plan);

        // Get all the planList
        restPlanMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(plan.getId().intValue())))
            .andExpect(jsonPath("$.[*].code").value(hasItem(DEFAULT_CODE)))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION)))
            .andExpect(jsonPath("$.[*].maxUsers").value(hasItem(DEFAULT_MAX_USERS)))
            .andExpect(jsonPath("$.[*].maxStorageMb").value(hasItem(DEFAULT_MAX_STORAGE_MB)))
            .andExpect(jsonPath("$.[*].maxCustomers").value(hasItem(DEFAULT_MAX_CUSTOMERS)))
            .andExpect(jsonPath("$.[*].maxContacts").value(hasItem(DEFAULT_MAX_CONTACTS)))
            .andExpect(jsonPath("$.[*].maxQuotations").value(hasItem(DEFAULT_MAX_QUOTATIONS)))
            .andExpect(jsonPath("$.[*].maxComplaints").value(hasItem(DEFAULT_MAX_COMPLAINTS)))
            .andExpect(jsonPath("$.[*].pricePerMonth").value(hasItem(sameNumber(DEFAULT_PRICE_PER_MONTH))))
            .andExpect(jsonPath("$.[*].currency").value(hasItem(DEFAULT_CURRENCY)))
            .andExpect(jsonPath("$.[*].isActive").value(hasItem(DEFAULT_IS_ACTIVE)));
    }

    @Test
    @Transactional
    void getPlan() throws Exception {
        // Initialize the database
        insertedPlan = planRepository.saveAndFlush(plan);

        // Get the plan
        restPlanMockMvc
            .perform(get(ENTITY_API_URL_ID, plan.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(plan.getId().intValue()))
            .andExpect(jsonPath("$.code").value(DEFAULT_CODE))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME))
            .andExpect(jsonPath("$.description").value(DEFAULT_DESCRIPTION))
            .andExpect(jsonPath("$.maxUsers").value(DEFAULT_MAX_USERS))
            .andExpect(jsonPath("$.maxStorageMb").value(DEFAULT_MAX_STORAGE_MB))
            .andExpect(jsonPath("$.maxCustomers").value(DEFAULT_MAX_CUSTOMERS))
            .andExpect(jsonPath("$.maxContacts").value(DEFAULT_MAX_CONTACTS))
            .andExpect(jsonPath("$.maxQuotations").value(DEFAULT_MAX_QUOTATIONS))
            .andExpect(jsonPath("$.maxComplaints").value(DEFAULT_MAX_COMPLAINTS))
            .andExpect(jsonPath("$.pricePerMonth").value(sameNumber(DEFAULT_PRICE_PER_MONTH)))
            .andExpect(jsonPath("$.currency").value(DEFAULT_CURRENCY))
            .andExpect(jsonPath("$.isActive").value(DEFAULT_IS_ACTIVE));
    }

    @Test
    @Transactional
    void getPlansByIdFiltering() throws Exception {
        // Initialize the database
        insertedPlan = planRepository.saveAndFlush(plan);

        Long id = plan.getId();

        defaultPlanFiltering("id.equals=" + id, "id.notEquals=" + id);

        defaultPlanFiltering("id.greaterThanOrEqual=" + id, "id.greaterThan=" + id);

        defaultPlanFiltering("id.lessThanOrEqual=" + id, "id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllPlansByCodeIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedPlan = planRepository.saveAndFlush(plan);

        // Get all the planList where code equals to
        defaultPlanFiltering("code.equals=" + DEFAULT_CODE, "code.equals=" + UPDATED_CODE);
    }

    @Test
    @Transactional
    void getAllPlansByCodeIsInShouldWork() throws Exception {
        // Initialize the database
        insertedPlan = planRepository.saveAndFlush(plan);

        // Get all the planList where code in
        defaultPlanFiltering("code.in=" + DEFAULT_CODE + "," + UPDATED_CODE, "code.in=" + UPDATED_CODE);
    }

    @Test
    @Transactional
    void getAllPlansByCodeIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedPlan = planRepository.saveAndFlush(plan);

        // Get all the planList where code is not null
        defaultPlanFiltering("code.specified=true", "code.specified=false");
    }

    @Test
    @Transactional
    void getAllPlansByCodeContainsSomething() throws Exception {
        // Initialize the database
        insertedPlan = planRepository.saveAndFlush(plan);

        // Get all the planList where code contains
        defaultPlanFiltering("code.contains=" + DEFAULT_CODE, "code.contains=" + UPDATED_CODE);
    }

    @Test
    @Transactional
    void getAllPlansByCodeNotContainsSomething() throws Exception {
        // Initialize the database
        insertedPlan = planRepository.saveAndFlush(plan);

        // Get all the planList where code does not contain
        defaultPlanFiltering("code.doesNotContain=" + UPDATED_CODE, "code.doesNotContain=" + DEFAULT_CODE);
    }

    @Test
    @Transactional
    void getAllPlansByNameIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedPlan = planRepository.saveAndFlush(plan);

        // Get all the planList where name equals to
        defaultPlanFiltering("name.equals=" + DEFAULT_NAME, "name.equals=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllPlansByNameIsInShouldWork() throws Exception {
        // Initialize the database
        insertedPlan = planRepository.saveAndFlush(plan);

        // Get all the planList where name in
        defaultPlanFiltering("name.in=" + DEFAULT_NAME + "," + UPDATED_NAME, "name.in=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllPlansByNameIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedPlan = planRepository.saveAndFlush(plan);

        // Get all the planList where name is not null
        defaultPlanFiltering("name.specified=true", "name.specified=false");
    }

    @Test
    @Transactional
    void getAllPlansByNameContainsSomething() throws Exception {
        // Initialize the database
        insertedPlan = planRepository.saveAndFlush(plan);

        // Get all the planList where name contains
        defaultPlanFiltering("name.contains=" + DEFAULT_NAME, "name.contains=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllPlansByNameNotContainsSomething() throws Exception {
        // Initialize the database
        insertedPlan = planRepository.saveAndFlush(plan);

        // Get all the planList where name does not contain
        defaultPlanFiltering("name.doesNotContain=" + UPDATED_NAME, "name.doesNotContain=" + DEFAULT_NAME);
    }

    @Test
    @Transactional
    void getAllPlansByDescriptionIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedPlan = planRepository.saveAndFlush(plan);

        // Get all the planList where description equals to
        defaultPlanFiltering("description.equals=" + DEFAULT_DESCRIPTION, "description.equals=" + UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    void getAllPlansByDescriptionIsInShouldWork() throws Exception {
        // Initialize the database
        insertedPlan = planRepository.saveAndFlush(plan);

        // Get all the planList where description in
        defaultPlanFiltering("description.in=" + DEFAULT_DESCRIPTION + "," + UPDATED_DESCRIPTION, "description.in=" + UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    void getAllPlansByDescriptionIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedPlan = planRepository.saveAndFlush(plan);

        // Get all the planList where description is not null
        defaultPlanFiltering("description.specified=true", "description.specified=false");
    }

    @Test
    @Transactional
    void getAllPlansByDescriptionContainsSomething() throws Exception {
        // Initialize the database
        insertedPlan = planRepository.saveAndFlush(plan);

        // Get all the planList where description contains
        defaultPlanFiltering("description.contains=" + DEFAULT_DESCRIPTION, "description.contains=" + UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    void getAllPlansByDescriptionNotContainsSomething() throws Exception {
        // Initialize the database
        insertedPlan = planRepository.saveAndFlush(plan);

        // Get all the planList where description does not contain
        defaultPlanFiltering("description.doesNotContain=" + UPDATED_DESCRIPTION, "description.doesNotContain=" + DEFAULT_DESCRIPTION);
    }

    @Test
    @Transactional
    void getAllPlansByMaxUsersIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedPlan = planRepository.saveAndFlush(plan);

        // Get all the planList where maxUsers equals to
        defaultPlanFiltering("maxUsers.equals=" + DEFAULT_MAX_USERS, "maxUsers.equals=" + UPDATED_MAX_USERS);
    }

    @Test
    @Transactional
    void getAllPlansByMaxUsersIsInShouldWork() throws Exception {
        // Initialize the database
        insertedPlan = planRepository.saveAndFlush(plan);

        // Get all the planList where maxUsers in
        defaultPlanFiltering("maxUsers.in=" + DEFAULT_MAX_USERS + "," + UPDATED_MAX_USERS, "maxUsers.in=" + UPDATED_MAX_USERS);
    }

    @Test
    @Transactional
    void getAllPlansByMaxUsersIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedPlan = planRepository.saveAndFlush(plan);

        // Get all the planList where maxUsers is not null
        defaultPlanFiltering("maxUsers.specified=true", "maxUsers.specified=false");
    }

    @Test
    @Transactional
    void getAllPlansByMaxUsersIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedPlan = planRepository.saveAndFlush(plan);

        // Get all the planList where maxUsers is greater than or equal to
        defaultPlanFiltering("maxUsers.greaterThanOrEqual=" + DEFAULT_MAX_USERS, "maxUsers.greaterThanOrEqual=" + UPDATED_MAX_USERS);
    }

    @Test
    @Transactional
    void getAllPlansByMaxUsersIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedPlan = planRepository.saveAndFlush(plan);

        // Get all the planList where maxUsers is less than or equal to
        defaultPlanFiltering("maxUsers.lessThanOrEqual=" + DEFAULT_MAX_USERS, "maxUsers.lessThanOrEqual=" + SMALLER_MAX_USERS);
    }

    @Test
    @Transactional
    void getAllPlansByMaxUsersIsLessThanSomething() throws Exception {
        // Initialize the database
        insertedPlan = planRepository.saveAndFlush(plan);

        // Get all the planList where maxUsers is less than
        defaultPlanFiltering("maxUsers.lessThan=" + UPDATED_MAX_USERS, "maxUsers.lessThan=" + DEFAULT_MAX_USERS);
    }

    @Test
    @Transactional
    void getAllPlansByMaxUsersIsGreaterThanSomething() throws Exception {
        // Initialize the database
        insertedPlan = planRepository.saveAndFlush(plan);

        // Get all the planList where maxUsers is greater than
        defaultPlanFiltering("maxUsers.greaterThan=" + SMALLER_MAX_USERS, "maxUsers.greaterThan=" + DEFAULT_MAX_USERS);
    }

    @Test
    @Transactional
    void getAllPlansByMaxStorageMbIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedPlan = planRepository.saveAndFlush(plan);

        // Get all the planList where maxStorageMb equals to
        defaultPlanFiltering("maxStorageMb.equals=" + DEFAULT_MAX_STORAGE_MB, "maxStorageMb.equals=" + UPDATED_MAX_STORAGE_MB);
    }

    @Test
    @Transactional
    void getAllPlansByMaxStorageMbIsInShouldWork() throws Exception {
        // Initialize the database
        insertedPlan = planRepository.saveAndFlush(plan);

        // Get all the planList where maxStorageMb in
        defaultPlanFiltering(
            "maxStorageMb.in=" + DEFAULT_MAX_STORAGE_MB + "," + UPDATED_MAX_STORAGE_MB,
            "maxStorageMb.in=" + UPDATED_MAX_STORAGE_MB
        );
    }

    @Test
    @Transactional
    void getAllPlansByMaxStorageMbIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedPlan = planRepository.saveAndFlush(plan);

        // Get all the planList where maxStorageMb is not null
        defaultPlanFiltering("maxStorageMb.specified=true", "maxStorageMb.specified=false");
    }

    @Test
    @Transactional
    void getAllPlansByMaxStorageMbIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedPlan = planRepository.saveAndFlush(plan);

        // Get all the planList where maxStorageMb is greater than or equal to
        defaultPlanFiltering(
            "maxStorageMb.greaterThanOrEqual=" + DEFAULT_MAX_STORAGE_MB,
            "maxStorageMb.greaterThanOrEqual=" + UPDATED_MAX_STORAGE_MB
        );
    }

    @Test
    @Transactional
    void getAllPlansByMaxStorageMbIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedPlan = planRepository.saveAndFlush(plan);

        // Get all the planList where maxStorageMb is less than or equal to
        defaultPlanFiltering(
            "maxStorageMb.lessThanOrEqual=" + DEFAULT_MAX_STORAGE_MB,
            "maxStorageMb.lessThanOrEqual=" + SMALLER_MAX_STORAGE_MB
        );
    }

    @Test
    @Transactional
    void getAllPlansByMaxStorageMbIsLessThanSomething() throws Exception {
        // Initialize the database
        insertedPlan = planRepository.saveAndFlush(plan);

        // Get all the planList where maxStorageMb is less than
        defaultPlanFiltering("maxStorageMb.lessThan=" + UPDATED_MAX_STORAGE_MB, "maxStorageMb.lessThan=" + DEFAULT_MAX_STORAGE_MB);
    }

    @Test
    @Transactional
    void getAllPlansByMaxStorageMbIsGreaterThanSomething() throws Exception {
        // Initialize the database
        insertedPlan = planRepository.saveAndFlush(plan);

        // Get all the planList where maxStorageMb is greater than
        defaultPlanFiltering("maxStorageMb.greaterThan=" + SMALLER_MAX_STORAGE_MB, "maxStorageMb.greaterThan=" + DEFAULT_MAX_STORAGE_MB);
    }

    @Test
    @Transactional
    void getAllPlansByMaxCustomersIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedPlan = planRepository.saveAndFlush(plan);

        // Get all the planList where maxCustomers equals to
        defaultPlanFiltering("maxCustomers.equals=" + DEFAULT_MAX_CUSTOMERS, "maxCustomers.equals=" + UPDATED_MAX_CUSTOMERS);
    }

    @Test
    @Transactional
    void getAllPlansByMaxCustomersIsInShouldWork() throws Exception {
        // Initialize the database
        insertedPlan = planRepository.saveAndFlush(plan);

        // Get all the planList where maxCustomers in
        defaultPlanFiltering(
            "maxCustomers.in=" + DEFAULT_MAX_CUSTOMERS + "," + UPDATED_MAX_CUSTOMERS,
            "maxCustomers.in=" + UPDATED_MAX_CUSTOMERS
        );
    }

    @Test
    @Transactional
    void getAllPlansByMaxCustomersIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedPlan = planRepository.saveAndFlush(plan);

        // Get all the planList where maxCustomers is not null
        defaultPlanFiltering("maxCustomers.specified=true", "maxCustomers.specified=false");
    }

    @Test
    @Transactional
    void getAllPlansByMaxCustomersIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedPlan = planRepository.saveAndFlush(plan);

        // Get all the planList where maxCustomers is greater than or equal to
        defaultPlanFiltering(
            "maxCustomers.greaterThanOrEqual=" + DEFAULT_MAX_CUSTOMERS,
            "maxCustomers.greaterThanOrEqual=" + UPDATED_MAX_CUSTOMERS
        );
    }

    @Test
    @Transactional
    void getAllPlansByMaxCustomersIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedPlan = planRepository.saveAndFlush(plan);

        // Get all the planList where maxCustomers is less than or equal to
        defaultPlanFiltering(
            "maxCustomers.lessThanOrEqual=" + DEFAULT_MAX_CUSTOMERS,
            "maxCustomers.lessThanOrEqual=" + SMALLER_MAX_CUSTOMERS
        );
    }

    @Test
    @Transactional
    void getAllPlansByMaxCustomersIsLessThanSomething() throws Exception {
        // Initialize the database
        insertedPlan = planRepository.saveAndFlush(plan);

        // Get all the planList where maxCustomers is less than
        defaultPlanFiltering("maxCustomers.lessThan=" + UPDATED_MAX_CUSTOMERS, "maxCustomers.lessThan=" + DEFAULT_MAX_CUSTOMERS);
    }

    @Test
    @Transactional
    void getAllPlansByMaxCustomersIsGreaterThanSomething() throws Exception {
        // Initialize the database
        insertedPlan = planRepository.saveAndFlush(plan);

        // Get all the planList where maxCustomers is greater than
        defaultPlanFiltering("maxCustomers.greaterThan=" + SMALLER_MAX_CUSTOMERS, "maxCustomers.greaterThan=" + DEFAULT_MAX_CUSTOMERS);
    }

    @Test
    @Transactional
    void getAllPlansByMaxContactsIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedPlan = planRepository.saveAndFlush(plan);

        // Get all the planList where maxContacts equals to
        defaultPlanFiltering("maxContacts.equals=" + DEFAULT_MAX_CONTACTS, "maxContacts.equals=" + UPDATED_MAX_CONTACTS);
    }

    @Test
    @Transactional
    void getAllPlansByMaxContactsIsInShouldWork() throws Exception {
        // Initialize the database
        insertedPlan = planRepository.saveAndFlush(plan);

        // Get all the planList where maxContacts in
        defaultPlanFiltering(
            "maxContacts.in=" + DEFAULT_MAX_CONTACTS + "," + UPDATED_MAX_CONTACTS,
            "maxContacts.in=" + UPDATED_MAX_CONTACTS
        );
    }

    @Test
    @Transactional
    void getAllPlansByMaxContactsIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedPlan = planRepository.saveAndFlush(plan);

        // Get all the planList where maxContacts is not null
        defaultPlanFiltering("maxContacts.specified=true", "maxContacts.specified=false");
    }

    @Test
    @Transactional
    void getAllPlansByMaxContactsIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedPlan = planRepository.saveAndFlush(plan);

        // Get all the planList where maxContacts is greater than or equal to
        defaultPlanFiltering(
            "maxContacts.greaterThanOrEqual=" + DEFAULT_MAX_CONTACTS,
            "maxContacts.greaterThanOrEqual=" + UPDATED_MAX_CONTACTS
        );
    }

    @Test
    @Transactional
    void getAllPlansByMaxContactsIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedPlan = planRepository.saveAndFlush(plan);

        // Get all the planList where maxContacts is less than or equal to
        defaultPlanFiltering("maxContacts.lessThanOrEqual=" + DEFAULT_MAX_CONTACTS, "maxContacts.lessThanOrEqual=" + SMALLER_MAX_CONTACTS);
    }

    @Test
    @Transactional
    void getAllPlansByMaxContactsIsLessThanSomething() throws Exception {
        // Initialize the database
        insertedPlan = planRepository.saveAndFlush(plan);

        // Get all the planList where maxContacts is less than
        defaultPlanFiltering("maxContacts.lessThan=" + UPDATED_MAX_CONTACTS, "maxContacts.lessThan=" + DEFAULT_MAX_CONTACTS);
    }

    @Test
    @Transactional
    void getAllPlansByMaxContactsIsGreaterThanSomething() throws Exception {
        // Initialize the database
        insertedPlan = planRepository.saveAndFlush(plan);

        // Get all the planList where maxContacts is greater than
        defaultPlanFiltering("maxContacts.greaterThan=" + SMALLER_MAX_CONTACTS, "maxContacts.greaterThan=" + DEFAULT_MAX_CONTACTS);
    }

    @Test
    @Transactional
    void getAllPlansByMaxQuotationsIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedPlan = planRepository.saveAndFlush(plan);

        // Get all the planList where maxQuotations equals to
        defaultPlanFiltering("maxQuotations.equals=" + DEFAULT_MAX_QUOTATIONS, "maxQuotations.equals=" + UPDATED_MAX_QUOTATIONS);
    }

    @Test
    @Transactional
    void getAllPlansByMaxQuotationsIsInShouldWork() throws Exception {
        // Initialize the database
        insertedPlan = planRepository.saveAndFlush(plan);

        // Get all the planList where maxQuotations in
        defaultPlanFiltering(
            "maxQuotations.in=" + DEFAULT_MAX_QUOTATIONS + "," + UPDATED_MAX_QUOTATIONS,
            "maxQuotations.in=" + UPDATED_MAX_QUOTATIONS
        );
    }

    @Test
    @Transactional
    void getAllPlansByMaxQuotationsIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedPlan = planRepository.saveAndFlush(plan);

        // Get all the planList where maxQuotations is not null
        defaultPlanFiltering("maxQuotations.specified=true", "maxQuotations.specified=false");
    }

    @Test
    @Transactional
    void getAllPlansByMaxQuotationsIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedPlan = planRepository.saveAndFlush(plan);

        // Get all the planList where maxQuotations is greater than or equal to
        defaultPlanFiltering(
            "maxQuotations.greaterThanOrEqual=" + DEFAULT_MAX_QUOTATIONS,
            "maxQuotations.greaterThanOrEqual=" + UPDATED_MAX_QUOTATIONS
        );
    }

    @Test
    @Transactional
    void getAllPlansByMaxQuotationsIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedPlan = planRepository.saveAndFlush(plan);

        // Get all the planList where maxQuotations is less than or equal to
        defaultPlanFiltering(
            "maxQuotations.lessThanOrEqual=" + DEFAULT_MAX_QUOTATIONS,
            "maxQuotations.lessThanOrEqual=" + SMALLER_MAX_QUOTATIONS
        );
    }

    @Test
    @Transactional
    void getAllPlansByMaxQuotationsIsLessThanSomething() throws Exception {
        // Initialize the database
        insertedPlan = planRepository.saveAndFlush(plan);

        // Get all the planList where maxQuotations is less than
        defaultPlanFiltering("maxQuotations.lessThan=" + UPDATED_MAX_QUOTATIONS, "maxQuotations.lessThan=" + DEFAULT_MAX_QUOTATIONS);
    }

    @Test
    @Transactional
    void getAllPlansByMaxQuotationsIsGreaterThanSomething() throws Exception {
        // Initialize the database
        insertedPlan = planRepository.saveAndFlush(plan);

        // Get all the planList where maxQuotations is greater than
        defaultPlanFiltering("maxQuotations.greaterThan=" + SMALLER_MAX_QUOTATIONS, "maxQuotations.greaterThan=" + DEFAULT_MAX_QUOTATIONS);
    }

    @Test
    @Transactional
    void getAllPlansByMaxComplaintsIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedPlan = planRepository.saveAndFlush(plan);

        // Get all the planList where maxComplaints equals to
        defaultPlanFiltering("maxComplaints.equals=" + DEFAULT_MAX_COMPLAINTS, "maxComplaints.equals=" + UPDATED_MAX_COMPLAINTS);
    }

    @Test
    @Transactional
    void getAllPlansByMaxComplaintsIsInShouldWork() throws Exception {
        // Initialize the database
        insertedPlan = planRepository.saveAndFlush(plan);

        // Get all the planList where maxComplaints in
        defaultPlanFiltering(
            "maxComplaints.in=" + DEFAULT_MAX_COMPLAINTS + "," + UPDATED_MAX_COMPLAINTS,
            "maxComplaints.in=" + UPDATED_MAX_COMPLAINTS
        );
    }

    @Test
    @Transactional
    void getAllPlansByMaxComplaintsIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedPlan = planRepository.saveAndFlush(plan);

        // Get all the planList where maxComplaints is not null
        defaultPlanFiltering("maxComplaints.specified=true", "maxComplaints.specified=false");
    }

    @Test
    @Transactional
    void getAllPlansByMaxComplaintsIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedPlan = planRepository.saveAndFlush(plan);

        // Get all the planList where maxComplaints is greater than or equal to
        defaultPlanFiltering(
            "maxComplaints.greaterThanOrEqual=" + DEFAULT_MAX_COMPLAINTS,
            "maxComplaints.greaterThanOrEqual=" + UPDATED_MAX_COMPLAINTS
        );
    }

    @Test
    @Transactional
    void getAllPlansByMaxComplaintsIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedPlan = planRepository.saveAndFlush(plan);

        // Get all the planList where maxComplaints is less than or equal to
        defaultPlanFiltering(
            "maxComplaints.lessThanOrEqual=" + DEFAULT_MAX_COMPLAINTS,
            "maxComplaints.lessThanOrEqual=" + SMALLER_MAX_COMPLAINTS
        );
    }

    @Test
    @Transactional
    void getAllPlansByMaxComplaintsIsLessThanSomething() throws Exception {
        // Initialize the database
        insertedPlan = planRepository.saveAndFlush(plan);

        // Get all the planList where maxComplaints is less than
        defaultPlanFiltering("maxComplaints.lessThan=" + UPDATED_MAX_COMPLAINTS, "maxComplaints.lessThan=" + DEFAULT_MAX_COMPLAINTS);
    }

    @Test
    @Transactional
    void getAllPlansByMaxComplaintsIsGreaterThanSomething() throws Exception {
        // Initialize the database
        insertedPlan = planRepository.saveAndFlush(plan);

        // Get all the planList where maxComplaints is greater than
        defaultPlanFiltering("maxComplaints.greaterThan=" + SMALLER_MAX_COMPLAINTS, "maxComplaints.greaterThan=" + DEFAULT_MAX_COMPLAINTS);
    }

    @Test
    @Transactional
    void getAllPlansByPricePerMonthIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedPlan = planRepository.saveAndFlush(plan);

        // Get all the planList where pricePerMonth equals to
        defaultPlanFiltering("pricePerMonth.equals=" + DEFAULT_PRICE_PER_MONTH, "pricePerMonth.equals=" + UPDATED_PRICE_PER_MONTH);
    }

    @Test
    @Transactional
    void getAllPlansByPricePerMonthIsInShouldWork() throws Exception {
        // Initialize the database
        insertedPlan = planRepository.saveAndFlush(plan);

        // Get all the planList where pricePerMonth in
        defaultPlanFiltering(
            "pricePerMonth.in=" + DEFAULT_PRICE_PER_MONTH + "," + UPDATED_PRICE_PER_MONTH,
            "pricePerMonth.in=" + UPDATED_PRICE_PER_MONTH
        );
    }

    @Test
    @Transactional
    void getAllPlansByPricePerMonthIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedPlan = planRepository.saveAndFlush(plan);

        // Get all the planList where pricePerMonth is not null
        defaultPlanFiltering("pricePerMonth.specified=true", "pricePerMonth.specified=false");
    }

    @Test
    @Transactional
    void getAllPlansByPricePerMonthIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedPlan = planRepository.saveAndFlush(plan);

        // Get all the planList where pricePerMonth is greater than or equal to
        defaultPlanFiltering(
            "pricePerMonth.greaterThanOrEqual=" + DEFAULT_PRICE_PER_MONTH,
            "pricePerMonth.greaterThanOrEqual=" + UPDATED_PRICE_PER_MONTH
        );
    }

    @Test
    @Transactional
    void getAllPlansByPricePerMonthIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedPlan = planRepository.saveAndFlush(plan);

        // Get all the planList where pricePerMonth is less than or equal to
        defaultPlanFiltering(
            "pricePerMonth.lessThanOrEqual=" + DEFAULT_PRICE_PER_MONTH,
            "pricePerMonth.lessThanOrEqual=" + SMALLER_PRICE_PER_MONTH
        );
    }

    @Test
    @Transactional
    void getAllPlansByPricePerMonthIsLessThanSomething() throws Exception {
        // Initialize the database
        insertedPlan = planRepository.saveAndFlush(plan);

        // Get all the planList where pricePerMonth is less than
        defaultPlanFiltering("pricePerMonth.lessThan=" + UPDATED_PRICE_PER_MONTH, "pricePerMonth.lessThan=" + DEFAULT_PRICE_PER_MONTH);
    }

    @Test
    @Transactional
    void getAllPlansByPricePerMonthIsGreaterThanSomething() throws Exception {
        // Initialize the database
        insertedPlan = planRepository.saveAndFlush(plan);

        // Get all the planList where pricePerMonth is greater than
        defaultPlanFiltering(
            "pricePerMonth.greaterThan=" + SMALLER_PRICE_PER_MONTH,
            "pricePerMonth.greaterThan=" + DEFAULT_PRICE_PER_MONTH
        );
    }

    @Test
    @Transactional
    void getAllPlansByCurrencyIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedPlan = planRepository.saveAndFlush(plan);

        // Get all the planList where currency equals to
        defaultPlanFiltering("currency.equals=" + DEFAULT_CURRENCY, "currency.equals=" + UPDATED_CURRENCY);
    }

    @Test
    @Transactional
    void getAllPlansByCurrencyIsInShouldWork() throws Exception {
        // Initialize the database
        insertedPlan = planRepository.saveAndFlush(plan);

        // Get all the planList where currency in
        defaultPlanFiltering("currency.in=" + DEFAULT_CURRENCY + "," + UPDATED_CURRENCY, "currency.in=" + UPDATED_CURRENCY);
    }

    @Test
    @Transactional
    void getAllPlansByCurrencyIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedPlan = planRepository.saveAndFlush(plan);

        // Get all the planList where currency is not null
        defaultPlanFiltering("currency.specified=true", "currency.specified=false");
    }

    @Test
    @Transactional
    void getAllPlansByCurrencyContainsSomething() throws Exception {
        // Initialize the database
        insertedPlan = planRepository.saveAndFlush(plan);

        // Get all the planList where currency contains
        defaultPlanFiltering("currency.contains=" + DEFAULT_CURRENCY, "currency.contains=" + UPDATED_CURRENCY);
    }

    @Test
    @Transactional
    void getAllPlansByCurrencyNotContainsSomething() throws Exception {
        // Initialize the database
        insertedPlan = planRepository.saveAndFlush(plan);

        // Get all the planList where currency does not contain
        defaultPlanFiltering("currency.doesNotContain=" + UPDATED_CURRENCY, "currency.doesNotContain=" + DEFAULT_CURRENCY);
    }

    @Test
    @Transactional
    void getAllPlansByIsActiveIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedPlan = planRepository.saveAndFlush(plan);

        // Get all the planList where isActive equals to
        defaultPlanFiltering("isActive.equals=" + DEFAULT_IS_ACTIVE, "isActive.equals=" + UPDATED_IS_ACTIVE);
    }

    @Test
    @Transactional
    void getAllPlansByIsActiveIsInShouldWork() throws Exception {
        // Initialize the database
        insertedPlan = planRepository.saveAndFlush(plan);

        // Get all the planList where isActive in
        defaultPlanFiltering("isActive.in=" + DEFAULT_IS_ACTIVE + "," + UPDATED_IS_ACTIVE, "isActive.in=" + UPDATED_IS_ACTIVE);
    }

    @Test
    @Transactional
    void getAllPlansByIsActiveIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedPlan = planRepository.saveAndFlush(plan);

        // Get all the planList where isActive is not null
        defaultPlanFiltering("isActive.specified=true", "isActive.specified=false");
    }

    private void defaultPlanFiltering(String shouldBeFound, String shouldNotBeFound) throws Exception {
        defaultPlanShouldBeFound(shouldBeFound);
        defaultPlanShouldNotBeFound(shouldNotBeFound);
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultPlanShouldBeFound(String filter) throws Exception {
        restPlanMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(plan.getId().intValue())))
            .andExpect(jsonPath("$.[*].code").value(hasItem(DEFAULT_CODE)))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION)))
            .andExpect(jsonPath("$.[*].maxUsers").value(hasItem(DEFAULT_MAX_USERS)))
            .andExpect(jsonPath("$.[*].maxStorageMb").value(hasItem(DEFAULT_MAX_STORAGE_MB)))
            .andExpect(jsonPath("$.[*].maxCustomers").value(hasItem(DEFAULT_MAX_CUSTOMERS)))
            .andExpect(jsonPath("$.[*].maxContacts").value(hasItem(DEFAULT_MAX_CONTACTS)))
            .andExpect(jsonPath("$.[*].maxQuotations").value(hasItem(DEFAULT_MAX_QUOTATIONS)))
            .andExpect(jsonPath("$.[*].maxComplaints").value(hasItem(DEFAULT_MAX_COMPLAINTS)))
            .andExpect(jsonPath("$.[*].pricePerMonth").value(hasItem(sameNumber(DEFAULT_PRICE_PER_MONTH))))
            .andExpect(jsonPath("$.[*].currency").value(hasItem(DEFAULT_CURRENCY)))
            .andExpect(jsonPath("$.[*].isActive").value(hasItem(DEFAULT_IS_ACTIVE)));

        // Check, that the count call also returns 1
        restPlanMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultPlanShouldNotBeFound(String filter) throws Exception {
        restPlanMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restPlanMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingPlan() throws Exception {
        // Get the plan
        restPlanMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingPlan() throws Exception {
        // Initialize the database
        insertedPlan = planRepository.saveAndFlush(plan);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the plan
        Plan updatedPlan = planRepository.findById(plan.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedPlan are not directly saved in db
        em.detach(updatedPlan);
        updatedPlan
            .code(UPDATED_CODE)
            .name(UPDATED_NAME)
            .description(UPDATED_DESCRIPTION)
            .maxUsers(UPDATED_MAX_USERS)
            .maxStorageMb(UPDATED_MAX_STORAGE_MB)
            .maxCustomers(UPDATED_MAX_CUSTOMERS)
            .maxContacts(UPDATED_MAX_CONTACTS)
            .maxQuotations(UPDATED_MAX_QUOTATIONS)
            .maxComplaints(UPDATED_MAX_COMPLAINTS)
            .pricePerMonth(UPDATED_PRICE_PER_MONTH)
            .currency(UPDATED_CURRENCY)
            .isActive(UPDATED_IS_ACTIVE);
        PlanDTO planDTO = planMapper.toDto(updatedPlan);

        restPlanMockMvc
            .perform(put(ENTITY_API_URL_ID, planDTO.getId()).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(planDTO)))
            .andExpect(status().isOk());

        // Validate the Plan in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedPlanToMatchAllProperties(updatedPlan);
    }

    @Test
    @Transactional
    void putNonExistingPlan() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        plan.setId(longCount.incrementAndGet());

        // Create the Plan
        PlanDTO planDTO = planMapper.toDto(plan);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restPlanMockMvc
            .perform(put(ENTITY_API_URL_ID, planDTO.getId()).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(planDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Plan in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchPlan() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        plan.setId(longCount.incrementAndGet());

        // Create the Plan
        PlanDTO planDTO = planMapper.toDto(plan);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restPlanMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(planDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Plan in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamPlan() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        plan.setId(longCount.incrementAndGet());

        // Create the Plan
        PlanDTO planDTO = planMapper.toDto(plan);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restPlanMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(planDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Plan in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdatePlanWithPatch() throws Exception {
        // Initialize the database
        insertedPlan = planRepository.saveAndFlush(plan);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the plan using partial update
        Plan partialUpdatedPlan = new Plan();
        partialUpdatedPlan.setId(plan.getId());

        partialUpdatedPlan
            .code(UPDATED_CODE)
            .description(UPDATED_DESCRIPTION)
            .maxStorageMb(UPDATED_MAX_STORAGE_MB)
            .maxCustomers(UPDATED_MAX_CUSTOMERS)
            .currency(UPDATED_CURRENCY)
            .isActive(UPDATED_IS_ACTIVE);

        restPlanMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedPlan.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedPlan))
            )
            .andExpect(status().isOk());

        // Validate the Plan in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPlanUpdatableFieldsEquals(createUpdateProxyForBean(partialUpdatedPlan, plan), getPersistedPlan(plan));
    }

    @Test
    @Transactional
    void fullUpdatePlanWithPatch() throws Exception {
        // Initialize the database
        insertedPlan = planRepository.saveAndFlush(plan);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the plan using partial update
        Plan partialUpdatedPlan = new Plan();
        partialUpdatedPlan.setId(plan.getId());

        partialUpdatedPlan
            .code(UPDATED_CODE)
            .name(UPDATED_NAME)
            .description(UPDATED_DESCRIPTION)
            .maxUsers(UPDATED_MAX_USERS)
            .maxStorageMb(UPDATED_MAX_STORAGE_MB)
            .maxCustomers(UPDATED_MAX_CUSTOMERS)
            .maxContacts(UPDATED_MAX_CONTACTS)
            .maxQuotations(UPDATED_MAX_QUOTATIONS)
            .maxComplaints(UPDATED_MAX_COMPLAINTS)
            .pricePerMonth(UPDATED_PRICE_PER_MONTH)
            .currency(UPDATED_CURRENCY)
            .isActive(UPDATED_IS_ACTIVE);

        restPlanMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedPlan.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedPlan))
            )
            .andExpect(status().isOk());

        // Validate the Plan in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPlanUpdatableFieldsEquals(partialUpdatedPlan, getPersistedPlan(partialUpdatedPlan));
    }

    @Test
    @Transactional
    void patchNonExistingPlan() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        plan.setId(longCount.incrementAndGet());

        // Create the Plan
        PlanDTO planDTO = planMapper.toDto(plan);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restPlanMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, planDTO.getId()).contentType("application/merge-patch+json").content(om.writeValueAsBytes(planDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Plan in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchPlan() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        plan.setId(longCount.incrementAndGet());

        // Create the Plan
        PlanDTO planDTO = planMapper.toDto(plan);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restPlanMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(planDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Plan in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamPlan() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        plan.setId(longCount.incrementAndGet());

        // Create the Plan
        PlanDTO planDTO = planMapper.toDto(plan);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restPlanMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(om.writeValueAsBytes(planDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Plan in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deletePlan() throws Exception {
        // Initialize the database
        insertedPlan = planRepository.saveAndFlush(plan);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the plan
        restPlanMockMvc
            .perform(delete(ENTITY_API_URL_ID, plan.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return planRepository.count();
    }

    protected void assertIncrementedRepositoryCount(long countBefore) {
        assertThat(countBefore + 1).isEqualTo(getRepositoryCount());
    }

    protected void assertDecrementedRepositoryCount(long countBefore) {
        assertThat(countBefore - 1).isEqualTo(getRepositoryCount());
    }

    protected void assertSameRepositoryCount(long countBefore) {
        assertThat(countBefore).isEqualTo(getRepositoryCount());
    }

    protected Plan getPersistedPlan(Plan plan) {
        return planRepository.findById(plan.getId()).orElseThrow();
    }

    protected void assertPersistedPlanToMatchAllProperties(Plan expectedPlan) {
        assertPlanAllPropertiesEquals(expectedPlan, getPersistedPlan(expectedPlan));
    }

    protected void assertPersistedPlanToMatchUpdatableProperties(Plan expectedPlan) {
        assertPlanAllUpdatablePropertiesEquals(expectedPlan, getPersistedPlan(expectedPlan));
    }
}
