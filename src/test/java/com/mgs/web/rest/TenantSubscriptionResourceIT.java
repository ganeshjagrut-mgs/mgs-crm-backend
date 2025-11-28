package com.mgs.web.rest;

import static com.mgs.domain.TenantSubscriptionAsserts.*;
import static com.mgs.web.rest.TestUtil.createUpdateProxyForBean;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mgs.IntegrationTest;
import com.mgs.domain.Plan;
import com.mgs.domain.Tenant;
import com.mgs.domain.TenantSubscription;
import com.mgs.domain.enumeration.SubscriptionStatus;
import com.mgs.repository.TenantSubscriptionRepository;
import com.mgs.service.dto.TenantSubscriptionDTO;
import com.mgs.service.mapper.TenantSubscriptionMapper;
import jakarta.persistence.EntityManager;
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.temporal.ChronoUnit;
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
 * Integration tests for the {@link TenantSubscriptionResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class TenantSubscriptionResourceIT {

    private static final SubscriptionStatus DEFAULT_STATUS = SubscriptionStatus.TRIAL;
    private static final SubscriptionStatus UPDATED_STATUS = SubscriptionStatus.ACTIVE;

    private static final LocalDate DEFAULT_START_DATE = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_START_DATE = LocalDate.now(ZoneId.systemDefault());
    private static final LocalDate SMALLER_START_DATE = LocalDate.ofEpochDay(-1L);

    private static final LocalDate DEFAULT_END_DATE = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_END_DATE = LocalDate.now(ZoneId.systemDefault());
    private static final LocalDate SMALLER_END_DATE = LocalDate.ofEpochDay(-1L);

    private static final LocalDate DEFAULT_TRIAL_END_DATE = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_TRIAL_END_DATE = LocalDate.now(ZoneId.systemDefault());
    private static final LocalDate SMALLER_TRIAL_END_DATE = LocalDate.ofEpochDay(-1L);

    private static final Instant DEFAULT_LAST_RENEWED_AT = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_LAST_RENEWED_AT = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final Instant DEFAULT_NEXT_BILLING_AT = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_NEXT_BILLING_AT = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final String ENTITY_API_URL = "/api/tenant-subscriptions";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ObjectMapper om;

    @Autowired
    private TenantSubscriptionRepository tenantSubscriptionRepository;

    @Autowired
    private TenantSubscriptionMapper tenantSubscriptionMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restTenantSubscriptionMockMvc;

    private TenantSubscription tenantSubscription;

    private TenantSubscription insertedTenantSubscription;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static TenantSubscription createEntity(EntityManager em) {
        TenantSubscription tenantSubscription = new TenantSubscription()
            .status(DEFAULT_STATUS)
            .startDate(DEFAULT_START_DATE)
            .endDate(DEFAULT_END_DATE)
            .trialEndDate(DEFAULT_TRIAL_END_DATE)
            .lastRenewedAt(DEFAULT_LAST_RENEWED_AT)
            .nextBillingAt(DEFAULT_NEXT_BILLING_AT);
        // Add required entity
        Tenant tenant;
        if (TestUtil.findAll(em, Tenant.class).isEmpty()) {
            tenant = TenantResourceIT.createEntity();
            em.persist(tenant);
            em.flush();
        } else {
            tenant = TestUtil.findAll(em, Tenant.class).get(0);
        }
        tenantSubscription.setTenant(tenant);
        // Add required entity
        Plan plan;
        if (TestUtil.findAll(em, Plan.class).isEmpty()) {
            plan = PlanResourceIT.createEntity();
            em.persist(plan);
            em.flush();
        } else {
            plan = TestUtil.findAll(em, Plan.class).get(0);
        }
        tenantSubscription.setPlan(plan);
        return tenantSubscription;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static TenantSubscription createUpdatedEntity(EntityManager em) {
        TenantSubscription updatedTenantSubscription = new TenantSubscription()
            .status(UPDATED_STATUS)
            .startDate(UPDATED_START_DATE)
            .endDate(UPDATED_END_DATE)
            .trialEndDate(UPDATED_TRIAL_END_DATE)
            .lastRenewedAt(UPDATED_LAST_RENEWED_AT)
            .nextBillingAt(UPDATED_NEXT_BILLING_AT);
        // Add required entity
        Tenant tenant;
        if (TestUtil.findAll(em, Tenant.class).isEmpty()) {
            tenant = TenantResourceIT.createUpdatedEntity();
            em.persist(tenant);
            em.flush();
        } else {
            tenant = TestUtil.findAll(em, Tenant.class).get(0);
        }
        updatedTenantSubscription.setTenant(tenant);
        // Add required entity
        Plan plan;
        if (TestUtil.findAll(em, Plan.class).isEmpty()) {
            plan = PlanResourceIT.createUpdatedEntity();
            em.persist(plan);
            em.flush();
        } else {
            plan = TestUtil.findAll(em, Plan.class).get(0);
        }
        updatedTenantSubscription.setPlan(plan);
        return updatedTenantSubscription;
    }

    @BeforeEach
    void initTest() {
        tenantSubscription = createEntity(em);
    }

    @AfterEach
    void cleanup() {
        if (insertedTenantSubscription != null) {
            tenantSubscriptionRepository.delete(insertedTenantSubscription);
            insertedTenantSubscription = null;
        }
    }

    @Test
    @Transactional
    void createTenantSubscription() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the TenantSubscription
        TenantSubscriptionDTO tenantSubscriptionDTO = tenantSubscriptionMapper.toDto(tenantSubscription);
        var returnedTenantSubscriptionDTO = om.readValue(
            restTenantSubscriptionMockMvc
                .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(tenantSubscriptionDTO)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            TenantSubscriptionDTO.class
        );

        // Validate the TenantSubscription in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        var returnedTenantSubscription = tenantSubscriptionMapper.toEntity(returnedTenantSubscriptionDTO);
        assertTenantSubscriptionUpdatableFieldsEquals(
            returnedTenantSubscription,
            getPersistedTenantSubscription(returnedTenantSubscription)
        );

        insertedTenantSubscription = returnedTenantSubscription;
    }

    @Test
    @Transactional
    void createTenantSubscriptionWithExistingId() throws Exception {
        // Create the TenantSubscription with an existing ID
        tenantSubscription.setId(1L);
        TenantSubscriptionDTO tenantSubscriptionDTO = tenantSubscriptionMapper.toDto(tenantSubscription);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restTenantSubscriptionMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(tenantSubscriptionDTO)))
            .andExpect(status().isBadRequest());

        // Validate the TenantSubscription in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkStatusIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        tenantSubscription.setStatus(null);

        // Create the TenantSubscription, which fails.
        TenantSubscriptionDTO tenantSubscriptionDTO = tenantSubscriptionMapper.toDto(tenantSubscription);

        restTenantSubscriptionMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(tenantSubscriptionDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkStartDateIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        tenantSubscription.setStartDate(null);

        // Create the TenantSubscription, which fails.
        TenantSubscriptionDTO tenantSubscriptionDTO = tenantSubscriptionMapper.toDto(tenantSubscription);

        restTenantSubscriptionMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(tenantSubscriptionDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllTenantSubscriptions() throws Exception {
        // Initialize the database
        insertedTenantSubscription = tenantSubscriptionRepository.saveAndFlush(tenantSubscription);

        // Get all the tenantSubscriptionList
        restTenantSubscriptionMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(tenantSubscription.getId().intValue())))
            .andExpect(jsonPath("$.[*].status").value(hasItem(DEFAULT_STATUS.toString())))
            .andExpect(jsonPath("$.[*].startDate").value(hasItem(DEFAULT_START_DATE.toString())))
            .andExpect(jsonPath("$.[*].endDate").value(hasItem(DEFAULT_END_DATE.toString())))
            .andExpect(jsonPath("$.[*].trialEndDate").value(hasItem(DEFAULT_TRIAL_END_DATE.toString())))
            .andExpect(jsonPath("$.[*].lastRenewedAt").value(hasItem(DEFAULT_LAST_RENEWED_AT.toString())))
            .andExpect(jsonPath("$.[*].nextBillingAt").value(hasItem(DEFAULT_NEXT_BILLING_AT.toString())));
    }

    @Test
    @Transactional
    void getTenantSubscription() throws Exception {
        // Initialize the database
        insertedTenantSubscription = tenantSubscriptionRepository.saveAndFlush(tenantSubscription);

        // Get the tenantSubscription
        restTenantSubscriptionMockMvc
            .perform(get(ENTITY_API_URL_ID, tenantSubscription.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(tenantSubscription.getId().intValue()))
            .andExpect(jsonPath("$.status").value(DEFAULT_STATUS.toString()))
            .andExpect(jsonPath("$.startDate").value(DEFAULT_START_DATE.toString()))
            .andExpect(jsonPath("$.endDate").value(DEFAULT_END_DATE.toString()))
            .andExpect(jsonPath("$.trialEndDate").value(DEFAULT_TRIAL_END_DATE.toString()))
            .andExpect(jsonPath("$.lastRenewedAt").value(DEFAULT_LAST_RENEWED_AT.toString()))
            .andExpect(jsonPath("$.nextBillingAt").value(DEFAULT_NEXT_BILLING_AT.toString()));
    }

    @Test
    @Transactional
    void getTenantSubscriptionsByIdFiltering() throws Exception {
        // Initialize the database
        insertedTenantSubscription = tenantSubscriptionRepository.saveAndFlush(tenantSubscription);

        Long id = tenantSubscription.getId();

        defaultTenantSubscriptionFiltering("id.equals=" + id, "id.notEquals=" + id);

        defaultTenantSubscriptionFiltering("id.greaterThanOrEqual=" + id, "id.greaterThan=" + id);

        defaultTenantSubscriptionFiltering("id.lessThanOrEqual=" + id, "id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllTenantSubscriptionsByStatusIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedTenantSubscription = tenantSubscriptionRepository.saveAndFlush(tenantSubscription);

        // Get all the tenantSubscriptionList where status equals to
        defaultTenantSubscriptionFiltering("status.equals=" + DEFAULT_STATUS, "status.equals=" + UPDATED_STATUS);
    }

    @Test
    @Transactional
    void getAllTenantSubscriptionsByStatusIsInShouldWork() throws Exception {
        // Initialize the database
        insertedTenantSubscription = tenantSubscriptionRepository.saveAndFlush(tenantSubscription);

        // Get all the tenantSubscriptionList where status in
        defaultTenantSubscriptionFiltering("status.in=" + DEFAULT_STATUS + "," + UPDATED_STATUS, "status.in=" + UPDATED_STATUS);
    }

    @Test
    @Transactional
    void getAllTenantSubscriptionsByStatusIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedTenantSubscription = tenantSubscriptionRepository.saveAndFlush(tenantSubscription);

        // Get all the tenantSubscriptionList where status is not null
        defaultTenantSubscriptionFiltering("status.specified=true", "status.specified=false");
    }

    @Test
    @Transactional
    void getAllTenantSubscriptionsByStartDateIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedTenantSubscription = tenantSubscriptionRepository.saveAndFlush(tenantSubscription);

        // Get all the tenantSubscriptionList where startDate equals to
        defaultTenantSubscriptionFiltering("startDate.equals=" + DEFAULT_START_DATE, "startDate.equals=" + UPDATED_START_DATE);
    }

    @Test
    @Transactional
    void getAllTenantSubscriptionsByStartDateIsInShouldWork() throws Exception {
        // Initialize the database
        insertedTenantSubscription = tenantSubscriptionRepository.saveAndFlush(tenantSubscription);

        // Get all the tenantSubscriptionList where startDate in
        defaultTenantSubscriptionFiltering(
            "startDate.in=" + DEFAULT_START_DATE + "," + UPDATED_START_DATE,
            "startDate.in=" + UPDATED_START_DATE
        );
    }

    @Test
    @Transactional
    void getAllTenantSubscriptionsByStartDateIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedTenantSubscription = tenantSubscriptionRepository.saveAndFlush(tenantSubscription);

        // Get all the tenantSubscriptionList where startDate is not null
        defaultTenantSubscriptionFiltering("startDate.specified=true", "startDate.specified=false");
    }

    @Test
    @Transactional
    void getAllTenantSubscriptionsByStartDateIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedTenantSubscription = tenantSubscriptionRepository.saveAndFlush(tenantSubscription);

        // Get all the tenantSubscriptionList where startDate is greater than or equal to
        defaultTenantSubscriptionFiltering(
            "startDate.greaterThanOrEqual=" + DEFAULT_START_DATE,
            "startDate.greaterThanOrEqual=" + UPDATED_START_DATE
        );
    }

    @Test
    @Transactional
    void getAllTenantSubscriptionsByStartDateIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedTenantSubscription = tenantSubscriptionRepository.saveAndFlush(tenantSubscription);

        // Get all the tenantSubscriptionList where startDate is less than or equal to
        defaultTenantSubscriptionFiltering(
            "startDate.lessThanOrEqual=" + DEFAULT_START_DATE,
            "startDate.lessThanOrEqual=" + SMALLER_START_DATE
        );
    }

    @Test
    @Transactional
    void getAllTenantSubscriptionsByStartDateIsLessThanSomething() throws Exception {
        // Initialize the database
        insertedTenantSubscription = tenantSubscriptionRepository.saveAndFlush(tenantSubscription);

        // Get all the tenantSubscriptionList where startDate is less than
        defaultTenantSubscriptionFiltering("startDate.lessThan=" + UPDATED_START_DATE, "startDate.lessThan=" + DEFAULT_START_DATE);
    }

    @Test
    @Transactional
    void getAllTenantSubscriptionsByStartDateIsGreaterThanSomething() throws Exception {
        // Initialize the database
        insertedTenantSubscription = tenantSubscriptionRepository.saveAndFlush(tenantSubscription);

        // Get all the tenantSubscriptionList where startDate is greater than
        defaultTenantSubscriptionFiltering("startDate.greaterThan=" + SMALLER_START_DATE, "startDate.greaterThan=" + DEFAULT_START_DATE);
    }

    @Test
    @Transactional
    void getAllTenantSubscriptionsByEndDateIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedTenantSubscription = tenantSubscriptionRepository.saveAndFlush(tenantSubscription);

        // Get all the tenantSubscriptionList where endDate equals to
        defaultTenantSubscriptionFiltering("endDate.equals=" + DEFAULT_END_DATE, "endDate.equals=" + UPDATED_END_DATE);
    }

    @Test
    @Transactional
    void getAllTenantSubscriptionsByEndDateIsInShouldWork() throws Exception {
        // Initialize the database
        insertedTenantSubscription = tenantSubscriptionRepository.saveAndFlush(tenantSubscription);

        // Get all the tenantSubscriptionList where endDate in
        defaultTenantSubscriptionFiltering("endDate.in=" + DEFAULT_END_DATE + "," + UPDATED_END_DATE, "endDate.in=" + UPDATED_END_DATE);
    }

    @Test
    @Transactional
    void getAllTenantSubscriptionsByEndDateIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedTenantSubscription = tenantSubscriptionRepository.saveAndFlush(tenantSubscription);

        // Get all the tenantSubscriptionList where endDate is not null
        defaultTenantSubscriptionFiltering("endDate.specified=true", "endDate.specified=false");
    }

    @Test
    @Transactional
    void getAllTenantSubscriptionsByEndDateIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedTenantSubscription = tenantSubscriptionRepository.saveAndFlush(tenantSubscription);

        // Get all the tenantSubscriptionList where endDate is greater than or equal to
        defaultTenantSubscriptionFiltering(
            "endDate.greaterThanOrEqual=" + DEFAULT_END_DATE,
            "endDate.greaterThanOrEqual=" + UPDATED_END_DATE
        );
    }

    @Test
    @Transactional
    void getAllTenantSubscriptionsByEndDateIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedTenantSubscription = tenantSubscriptionRepository.saveAndFlush(tenantSubscription);

        // Get all the tenantSubscriptionList where endDate is less than or equal to
        defaultTenantSubscriptionFiltering("endDate.lessThanOrEqual=" + DEFAULT_END_DATE, "endDate.lessThanOrEqual=" + SMALLER_END_DATE);
    }

    @Test
    @Transactional
    void getAllTenantSubscriptionsByEndDateIsLessThanSomething() throws Exception {
        // Initialize the database
        insertedTenantSubscription = tenantSubscriptionRepository.saveAndFlush(tenantSubscription);

        // Get all the tenantSubscriptionList where endDate is less than
        defaultTenantSubscriptionFiltering("endDate.lessThan=" + UPDATED_END_DATE, "endDate.lessThan=" + DEFAULT_END_DATE);
    }

    @Test
    @Transactional
    void getAllTenantSubscriptionsByEndDateIsGreaterThanSomething() throws Exception {
        // Initialize the database
        insertedTenantSubscription = tenantSubscriptionRepository.saveAndFlush(tenantSubscription);

        // Get all the tenantSubscriptionList where endDate is greater than
        defaultTenantSubscriptionFiltering("endDate.greaterThan=" + SMALLER_END_DATE, "endDate.greaterThan=" + DEFAULT_END_DATE);
    }

    @Test
    @Transactional
    void getAllTenantSubscriptionsByTrialEndDateIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedTenantSubscription = tenantSubscriptionRepository.saveAndFlush(tenantSubscription);

        // Get all the tenantSubscriptionList where trialEndDate equals to
        defaultTenantSubscriptionFiltering(
            "trialEndDate.equals=" + DEFAULT_TRIAL_END_DATE,
            "trialEndDate.equals=" + UPDATED_TRIAL_END_DATE
        );
    }

    @Test
    @Transactional
    void getAllTenantSubscriptionsByTrialEndDateIsInShouldWork() throws Exception {
        // Initialize the database
        insertedTenantSubscription = tenantSubscriptionRepository.saveAndFlush(tenantSubscription);

        // Get all the tenantSubscriptionList where trialEndDate in
        defaultTenantSubscriptionFiltering(
            "trialEndDate.in=" + DEFAULT_TRIAL_END_DATE + "," + UPDATED_TRIAL_END_DATE,
            "trialEndDate.in=" + UPDATED_TRIAL_END_DATE
        );
    }

    @Test
    @Transactional
    void getAllTenantSubscriptionsByTrialEndDateIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedTenantSubscription = tenantSubscriptionRepository.saveAndFlush(tenantSubscription);

        // Get all the tenantSubscriptionList where trialEndDate is not null
        defaultTenantSubscriptionFiltering("trialEndDate.specified=true", "trialEndDate.specified=false");
    }

    @Test
    @Transactional
    void getAllTenantSubscriptionsByTrialEndDateIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedTenantSubscription = tenantSubscriptionRepository.saveAndFlush(tenantSubscription);

        // Get all the tenantSubscriptionList where trialEndDate is greater than or equal to
        defaultTenantSubscriptionFiltering(
            "trialEndDate.greaterThanOrEqual=" + DEFAULT_TRIAL_END_DATE,
            "trialEndDate.greaterThanOrEqual=" + UPDATED_TRIAL_END_DATE
        );
    }

    @Test
    @Transactional
    void getAllTenantSubscriptionsByTrialEndDateIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedTenantSubscription = tenantSubscriptionRepository.saveAndFlush(tenantSubscription);

        // Get all the tenantSubscriptionList where trialEndDate is less than or equal to
        defaultTenantSubscriptionFiltering(
            "trialEndDate.lessThanOrEqual=" + DEFAULT_TRIAL_END_DATE,
            "trialEndDate.lessThanOrEqual=" + SMALLER_TRIAL_END_DATE
        );
    }

    @Test
    @Transactional
    void getAllTenantSubscriptionsByTrialEndDateIsLessThanSomething() throws Exception {
        // Initialize the database
        insertedTenantSubscription = tenantSubscriptionRepository.saveAndFlush(tenantSubscription);

        // Get all the tenantSubscriptionList where trialEndDate is less than
        defaultTenantSubscriptionFiltering(
            "trialEndDate.lessThan=" + UPDATED_TRIAL_END_DATE,
            "trialEndDate.lessThan=" + DEFAULT_TRIAL_END_DATE
        );
    }

    @Test
    @Transactional
    void getAllTenantSubscriptionsByTrialEndDateIsGreaterThanSomething() throws Exception {
        // Initialize the database
        insertedTenantSubscription = tenantSubscriptionRepository.saveAndFlush(tenantSubscription);

        // Get all the tenantSubscriptionList where trialEndDate is greater than
        defaultTenantSubscriptionFiltering(
            "trialEndDate.greaterThan=" + SMALLER_TRIAL_END_DATE,
            "trialEndDate.greaterThan=" + DEFAULT_TRIAL_END_DATE
        );
    }

    @Test
    @Transactional
    void getAllTenantSubscriptionsByLastRenewedAtIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedTenantSubscription = tenantSubscriptionRepository.saveAndFlush(tenantSubscription);

        // Get all the tenantSubscriptionList where lastRenewedAt equals to
        defaultTenantSubscriptionFiltering(
            "lastRenewedAt.equals=" + DEFAULT_LAST_RENEWED_AT,
            "lastRenewedAt.equals=" + UPDATED_LAST_RENEWED_AT
        );
    }

    @Test
    @Transactional
    void getAllTenantSubscriptionsByLastRenewedAtIsInShouldWork() throws Exception {
        // Initialize the database
        insertedTenantSubscription = tenantSubscriptionRepository.saveAndFlush(tenantSubscription);

        // Get all the tenantSubscriptionList where lastRenewedAt in
        defaultTenantSubscriptionFiltering(
            "lastRenewedAt.in=" + DEFAULT_LAST_RENEWED_AT + "," + UPDATED_LAST_RENEWED_AT,
            "lastRenewedAt.in=" + UPDATED_LAST_RENEWED_AT
        );
    }

    @Test
    @Transactional
    void getAllTenantSubscriptionsByLastRenewedAtIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedTenantSubscription = tenantSubscriptionRepository.saveAndFlush(tenantSubscription);

        // Get all the tenantSubscriptionList where lastRenewedAt is not null
        defaultTenantSubscriptionFiltering("lastRenewedAt.specified=true", "lastRenewedAt.specified=false");
    }

    @Test
    @Transactional
    void getAllTenantSubscriptionsByNextBillingAtIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedTenantSubscription = tenantSubscriptionRepository.saveAndFlush(tenantSubscription);

        // Get all the tenantSubscriptionList where nextBillingAt equals to
        defaultTenantSubscriptionFiltering(
            "nextBillingAt.equals=" + DEFAULT_NEXT_BILLING_AT,
            "nextBillingAt.equals=" + UPDATED_NEXT_BILLING_AT
        );
    }

    @Test
    @Transactional
    void getAllTenantSubscriptionsByNextBillingAtIsInShouldWork() throws Exception {
        // Initialize the database
        insertedTenantSubscription = tenantSubscriptionRepository.saveAndFlush(tenantSubscription);

        // Get all the tenantSubscriptionList where nextBillingAt in
        defaultTenantSubscriptionFiltering(
            "nextBillingAt.in=" + DEFAULT_NEXT_BILLING_AT + "," + UPDATED_NEXT_BILLING_AT,
            "nextBillingAt.in=" + UPDATED_NEXT_BILLING_AT
        );
    }

    @Test
    @Transactional
    void getAllTenantSubscriptionsByNextBillingAtIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedTenantSubscription = tenantSubscriptionRepository.saveAndFlush(tenantSubscription);

        // Get all the tenantSubscriptionList where nextBillingAt is not null
        defaultTenantSubscriptionFiltering("nextBillingAt.specified=true", "nextBillingAt.specified=false");
    }

    @Test
    @Transactional
    void getAllTenantSubscriptionsByTenantIsEqualToSomething() throws Exception {
        Tenant tenant;
        if (TestUtil.findAll(em, Tenant.class).isEmpty()) {
            tenantSubscriptionRepository.saveAndFlush(tenantSubscription);
            tenant = TenantResourceIT.createEntity();
        } else {
            tenant = TestUtil.findAll(em, Tenant.class).get(0);
        }
        em.persist(tenant);
        em.flush();
        tenantSubscription.setTenant(tenant);
        tenantSubscriptionRepository.saveAndFlush(tenantSubscription);
        Long tenantId = tenant.getId();
        // Get all the tenantSubscriptionList where tenant equals to tenantId
        defaultTenantSubscriptionShouldBeFound("tenantId.equals=" + tenantId);

        // Get all the tenantSubscriptionList where tenant equals to (tenantId + 1)
        defaultTenantSubscriptionShouldNotBeFound("tenantId.equals=" + (tenantId + 1));
    }

    @Test
    @Transactional
    void getAllTenantSubscriptionsByPlanIsEqualToSomething() throws Exception {
        Plan plan;
        if (TestUtil.findAll(em, Plan.class).isEmpty()) {
            tenantSubscriptionRepository.saveAndFlush(tenantSubscription);
            plan = PlanResourceIT.createEntity();
        } else {
            plan = TestUtil.findAll(em, Plan.class).get(0);
        }
        em.persist(plan);
        em.flush();
        tenantSubscription.setPlan(plan);
        tenantSubscriptionRepository.saveAndFlush(tenantSubscription);
        Long planId = plan.getId();
        // Get all the tenantSubscriptionList where plan equals to planId
        defaultTenantSubscriptionShouldBeFound("planId.equals=" + planId);

        // Get all the tenantSubscriptionList where plan equals to (planId + 1)
        defaultTenantSubscriptionShouldNotBeFound("planId.equals=" + (planId + 1));
    }

    private void defaultTenantSubscriptionFiltering(String shouldBeFound, String shouldNotBeFound) throws Exception {
        defaultTenantSubscriptionShouldBeFound(shouldBeFound);
        defaultTenantSubscriptionShouldNotBeFound(shouldNotBeFound);
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultTenantSubscriptionShouldBeFound(String filter) throws Exception {
        restTenantSubscriptionMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(tenantSubscription.getId().intValue())))
            .andExpect(jsonPath("$.[*].status").value(hasItem(DEFAULT_STATUS.toString())))
            .andExpect(jsonPath("$.[*].startDate").value(hasItem(DEFAULT_START_DATE.toString())))
            .andExpect(jsonPath("$.[*].endDate").value(hasItem(DEFAULT_END_DATE.toString())))
            .andExpect(jsonPath("$.[*].trialEndDate").value(hasItem(DEFAULT_TRIAL_END_DATE.toString())))
            .andExpect(jsonPath("$.[*].lastRenewedAt").value(hasItem(DEFAULT_LAST_RENEWED_AT.toString())))
            .andExpect(jsonPath("$.[*].nextBillingAt").value(hasItem(DEFAULT_NEXT_BILLING_AT.toString())));

        // Check, that the count call also returns 1
        restTenantSubscriptionMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultTenantSubscriptionShouldNotBeFound(String filter) throws Exception {
        restTenantSubscriptionMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restTenantSubscriptionMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingTenantSubscription() throws Exception {
        // Get the tenantSubscription
        restTenantSubscriptionMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingTenantSubscription() throws Exception {
        // Initialize the database
        insertedTenantSubscription = tenantSubscriptionRepository.saveAndFlush(tenantSubscription);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the tenantSubscription
        TenantSubscription updatedTenantSubscription = tenantSubscriptionRepository.findById(tenantSubscription.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedTenantSubscription are not directly saved in db
        em.detach(updatedTenantSubscription);
        updatedTenantSubscription
            .status(UPDATED_STATUS)
            .startDate(UPDATED_START_DATE)
            .endDate(UPDATED_END_DATE)
            .trialEndDate(UPDATED_TRIAL_END_DATE)
            .lastRenewedAt(UPDATED_LAST_RENEWED_AT)
            .nextBillingAt(UPDATED_NEXT_BILLING_AT);
        TenantSubscriptionDTO tenantSubscriptionDTO = tenantSubscriptionMapper.toDto(updatedTenantSubscription);

        restTenantSubscriptionMockMvc
            .perform(
                put(ENTITY_API_URL_ID, tenantSubscriptionDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(tenantSubscriptionDTO))
            )
            .andExpect(status().isOk());

        // Validate the TenantSubscription in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedTenantSubscriptionToMatchAllProperties(updatedTenantSubscription);
    }

    @Test
    @Transactional
    void putNonExistingTenantSubscription() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        tenantSubscription.setId(longCount.incrementAndGet());

        // Create the TenantSubscription
        TenantSubscriptionDTO tenantSubscriptionDTO = tenantSubscriptionMapper.toDto(tenantSubscription);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restTenantSubscriptionMockMvc
            .perform(
                put(ENTITY_API_URL_ID, tenantSubscriptionDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(tenantSubscriptionDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the TenantSubscription in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchTenantSubscription() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        tenantSubscription.setId(longCount.incrementAndGet());

        // Create the TenantSubscription
        TenantSubscriptionDTO tenantSubscriptionDTO = tenantSubscriptionMapper.toDto(tenantSubscription);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restTenantSubscriptionMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(tenantSubscriptionDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the TenantSubscription in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamTenantSubscription() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        tenantSubscription.setId(longCount.incrementAndGet());

        // Create the TenantSubscription
        TenantSubscriptionDTO tenantSubscriptionDTO = tenantSubscriptionMapper.toDto(tenantSubscription);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restTenantSubscriptionMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(tenantSubscriptionDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the TenantSubscription in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateTenantSubscriptionWithPatch() throws Exception {
        // Initialize the database
        insertedTenantSubscription = tenantSubscriptionRepository.saveAndFlush(tenantSubscription);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the tenantSubscription using partial update
        TenantSubscription partialUpdatedTenantSubscription = new TenantSubscription();
        partialUpdatedTenantSubscription.setId(tenantSubscription.getId());

        partialUpdatedTenantSubscription.lastRenewedAt(UPDATED_LAST_RENEWED_AT);

        restTenantSubscriptionMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedTenantSubscription.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedTenantSubscription))
            )
            .andExpect(status().isOk());

        // Validate the TenantSubscription in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertTenantSubscriptionUpdatableFieldsEquals(
            createUpdateProxyForBean(partialUpdatedTenantSubscription, tenantSubscription),
            getPersistedTenantSubscription(tenantSubscription)
        );
    }

    @Test
    @Transactional
    void fullUpdateTenantSubscriptionWithPatch() throws Exception {
        // Initialize the database
        insertedTenantSubscription = tenantSubscriptionRepository.saveAndFlush(tenantSubscription);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the tenantSubscription using partial update
        TenantSubscription partialUpdatedTenantSubscription = new TenantSubscription();
        partialUpdatedTenantSubscription.setId(tenantSubscription.getId());

        partialUpdatedTenantSubscription
            .status(UPDATED_STATUS)
            .startDate(UPDATED_START_DATE)
            .endDate(UPDATED_END_DATE)
            .trialEndDate(UPDATED_TRIAL_END_DATE)
            .lastRenewedAt(UPDATED_LAST_RENEWED_AT)
            .nextBillingAt(UPDATED_NEXT_BILLING_AT);

        restTenantSubscriptionMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedTenantSubscription.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedTenantSubscription))
            )
            .andExpect(status().isOk());

        // Validate the TenantSubscription in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertTenantSubscriptionUpdatableFieldsEquals(
            partialUpdatedTenantSubscription,
            getPersistedTenantSubscription(partialUpdatedTenantSubscription)
        );
    }

    @Test
    @Transactional
    void patchNonExistingTenantSubscription() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        tenantSubscription.setId(longCount.incrementAndGet());

        // Create the TenantSubscription
        TenantSubscriptionDTO tenantSubscriptionDTO = tenantSubscriptionMapper.toDto(tenantSubscription);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restTenantSubscriptionMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, tenantSubscriptionDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(tenantSubscriptionDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the TenantSubscription in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchTenantSubscription() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        tenantSubscription.setId(longCount.incrementAndGet());

        // Create the TenantSubscription
        TenantSubscriptionDTO tenantSubscriptionDTO = tenantSubscriptionMapper.toDto(tenantSubscription);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restTenantSubscriptionMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(tenantSubscriptionDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the TenantSubscription in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamTenantSubscription() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        tenantSubscription.setId(longCount.incrementAndGet());

        // Create the TenantSubscription
        TenantSubscriptionDTO tenantSubscriptionDTO = tenantSubscriptionMapper.toDto(tenantSubscription);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restTenantSubscriptionMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(om.writeValueAsBytes(tenantSubscriptionDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the TenantSubscription in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteTenantSubscription() throws Exception {
        // Initialize the database
        insertedTenantSubscription = tenantSubscriptionRepository.saveAndFlush(tenantSubscription);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the tenantSubscription
        restTenantSubscriptionMockMvc
            .perform(delete(ENTITY_API_URL_ID, tenantSubscription.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return tenantSubscriptionRepository.count();
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

    protected TenantSubscription getPersistedTenantSubscription(TenantSubscription tenantSubscription) {
        return tenantSubscriptionRepository.findById(tenantSubscription.getId()).orElseThrow();
    }

    protected void assertPersistedTenantSubscriptionToMatchAllProperties(TenantSubscription expectedTenantSubscription) {
        assertTenantSubscriptionAllPropertiesEquals(expectedTenantSubscription, getPersistedTenantSubscription(expectedTenantSubscription));
    }

    protected void assertPersistedTenantSubscriptionToMatchUpdatableProperties(TenantSubscription expectedTenantSubscription) {
        assertTenantSubscriptionAllUpdatablePropertiesEquals(
            expectedTenantSubscription,
            getPersistedTenantSubscription(expectedTenantSubscription)
        );
    }
}
