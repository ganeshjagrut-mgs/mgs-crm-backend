package com.mgs.web.rest;

import static com.mgs.domain.DealAsserts.*;
import static com.mgs.web.rest.TestUtil.createUpdateProxyForBean;
import static com.mgs.web.rest.TestUtil.sameNumber;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mgs.IntegrationTest;
import com.mgs.domain.Contact;
import com.mgs.domain.Customer;
import com.mgs.domain.Deal;
import com.mgs.domain.Lead;
import com.mgs.domain.Tenant;
import com.mgs.domain.enumeration.DealStatus;
import com.mgs.repository.DealRepository;
import com.mgs.service.dto.DealDTO;
import com.mgs.service.mapper.DealMapper;
import jakarta.persistence.EntityManager;
import java.math.BigDecimal;
import java.time.Instant;
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
 * Integration tests for the {@link DealResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class DealResourceIT {

    private static final String DEFAULT_DEAL_NUMBER = "AAAAAAAAAA";
    private static final String UPDATED_DEAL_NUMBER = "BBBBBBBBBB";

    private static final BigDecimal DEFAULT_DEAL_VALUE = new BigDecimal(1);
    private static final BigDecimal UPDATED_DEAL_VALUE = new BigDecimal(2);
    private static final BigDecimal SMALLER_DEAL_VALUE = new BigDecimal(1 - 1);

    private static final DealStatus DEFAULT_STATUS = DealStatus.NEW;
    private static final DealStatus UPDATED_STATUS = DealStatus.WON;

    private static final String DEFAULT_CURRENCY = "AAAAAAAAAA";
    private static final String UPDATED_CURRENCY = "BBBBBBBBBB";

    private static final Instant DEFAULT_START_DATE = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_START_DATE = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final Instant DEFAULT_CLOSE_DATE = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_CLOSE_DATE = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final String DEFAULT_NOTES = "AAAAAAAAAA";
    private static final String UPDATED_NOTES = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/deals";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ObjectMapper om;

    @Autowired
    private DealRepository dealRepository;

    @Autowired
    private DealMapper dealMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restDealMockMvc;

    private Deal deal;

    private Deal insertedDeal;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Deal createEntity(EntityManager em) {
        Deal deal = new Deal()
            .dealNumber(DEFAULT_DEAL_NUMBER)
            .dealValue(DEFAULT_DEAL_VALUE)
            .status(DEFAULT_STATUS)
            .currency(DEFAULT_CURRENCY)
            .startDate(DEFAULT_START_DATE)
            .closeDate(DEFAULT_CLOSE_DATE)
            .notes(DEFAULT_NOTES);
        // Add required entity
        Tenant tenant;
        if (TestUtil.findAll(em, Tenant.class).isEmpty()) {
            tenant = TenantResourceIT.createEntity();
            em.persist(tenant);
            em.flush();
        } else {
            tenant = TestUtil.findAll(em, Tenant.class).get(0);
        }
        deal.setTenant(tenant);
        // Add required entity
        Customer customer;
        if (TestUtil.findAll(em, Customer.class).isEmpty()) {
            customer = CustomerResourceIT.createEntity(em);
            em.persist(customer);
            em.flush();
        } else {
            customer = TestUtil.findAll(em, Customer.class).get(0);
        }
        deal.setCustomer(customer);
        return deal;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Deal createUpdatedEntity(EntityManager em) {
        Deal updatedDeal = new Deal()
            .dealNumber(UPDATED_DEAL_NUMBER)
            .dealValue(UPDATED_DEAL_VALUE)
            .status(UPDATED_STATUS)
            .currency(UPDATED_CURRENCY)
            .startDate(UPDATED_START_DATE)
            .closeDate(UPDATED_CLOSE_DATE)
            .notes(UPDATED_NOTES);
        // Add required entity
        Tenant tenant;
        if (TestUtil.findAll(em, Tenant.class).isEmpty()) {
            tenant = TenantResourceIT.createUpdatedEntity();
            em.persist(tenant);
            em.flush();
        } else {
            tenant = TestUtil.findAll(em, Tenant.class).get(0);
        }
        updatedDeal.setTenant(tenant);
        // Add required entity
        Customer customer;
        if (TestUtil.findAll(em, Customer.class).isEmpty()) {
            customer = CustomerResourceIT.createUpdatedEntity(em);
            em.persist(customer);
            em.flush();
        } else {
            customer = TestUtil.findAll(em, Customer.class).get(0);
        }
        updatedDeal.setCustomer(customer);
        return updatedDeal;
    }

    @BeforeEach
    void initTest() {
        deal = createEntity(em);
    }

    @AfterEach
    void cleanup() {
        if (insertedDeal != null) {
            dealRepository.delete(insertedDeal);
            insertedDeal = null;
        }
    }

    @Test
    @Transactional
    void createDeal() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the Deal
        DealDTO dealDTO = dealMapper.toDto(deal);
        var returnedDealDTO = om.readValue(
            restDealMockMvc
                .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(dealDTO)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            DealDTO.class
        );

        // Validate the Deal in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        var returnedDeal = dealMapper.toEntity(returnedDealDTO);
        assertDealUpdatableFieldsEquals(returnedDeal, getPersistedDeal(returnedDeal));

        insertedDeal = returnedDeal;
    }

    @Test
    @Transactional
    void createDealWithExistingId() throws Exception {
        // Create the Deal with an existing ID
        deal.setId(1L);
        DealDTO dealDTO = dealMapper.toDto(deal);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restDealMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(dealDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Deal in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkDealNumberIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        deal.setDealNumber(null);

        // Create the Deal, which fails.
        DealDTO dealDTO = dealMapper.toDto(deal);

        restDealMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(dealDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkStatusIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        deal.setStatus(null);

        // Create the Deal, which fails.
        DealDTO dealDTO = dealMapper.toDto(deal);

        restDealMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(dealDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllDeals() throws Exception {
        // Initialize the database
        insertedDeal = dealRepository.saveAndFlush(deal);

        // Get all the dealList
        restDealMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(deal.getId().intValue())))
            .andExpect(jsonPath("$.[*].dealNumber").value(hasItem(DEFAULT_DEAL_NUMBER)))
            .andExpect(jsonPath("$.[*].dealValue").value(hasItem(sameNumber(DEFAULT_DEAL_VALUE))))
            .andExpect(jsonPath("$.[*].status").value(hasItem(DEFAULT_STATUS.toString())))
            .andExpect(jsonPath("$.[*].currency").value(hasItem(DEFAULT_CURRENCY)))
            .andExpect(jsonPath("$.[*].startDate").value(hasItem(DEFAULT_START_DATE.toString())))
            .andExpect(jsonPath("$.[*].closeDate").value(hasItem(DEFAULT_CLOSE_DATE.toString())))
            .andExpect(jsonPath("$.[*].notes").value(hasItem(DEFAULT_NOTES)));
    }

    @Test
    @Transactional
    void getDeal() throws Exception {
        // Initialize the database
        insertedDeal = dealRepository.saveAndFlush(deal);

        // Get the deal
        restDealMockMvc
            .perform(get(ENTITY_API_URL_ID, deal.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(deal.getId().intValue()))
            .andExpect(jsonPath("$.dealNumber").value(DEFAULT_DEAL_NUMBER))
            .andExpect(jsonPath("$.dealValue").value(sameNumber(DEFAULT_DEAL_VALUE)))
            .andExpect(jsonPath("$.status").value(DEFAULT_STATUS.toString()))
            .andExpect(jsonPath("$.currency").value(DEFAULT_CURRENCY))
            .andExpect(jsonPath("$.startDate").value(DEFAULT_START_DATE.toString()))
            .andExpect(jsonPath("$.closeDate").value(DEFAULT_CLOSE_DATE.toString()))
            .andExpect(jsonPath("$.notes").value(DEFAULT_NOTES));
    }

    @Test
    @Transactional
    void getDealsByIdFiltering() throws Exception {
        // Initialize the database
        insertedDeal = dealRepository.saveAndFlush(deal);

        Long id = deal.getId();

        defaultDealFiltering("id.equals=" + id, "id.notEquals=" + id);

        defaultDealFiltering("id.greaterThanOrEqual=" + id, "id.greaterThan=" + id);

        defaultDealFiltering("id.lessThanOrEqual=" + id, "id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllDealsByDealNumberIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedDeal = dealRepository.saveAndFlush(deal);

        // Get all the dealList where dealNumber equals to
        defaultDealFiltering("dealNumber.equals=" + DEFAULT_DEAL_NUMBER, "dealNumber.equals=" + UPDATED_DEAL_NUMBER);
    }

    @Test
    @Transactional
    void getAllDealsByDealNumberIsInShouldWork() throws Exception {
        // Initialize the database
        insertedDeal = dealRepository.saveAndFlush(deal);

        // Get all the dealList where dealNumber in
        defaultDealFiltering("dealNumber.in=" + DEFAULT_DEAL_NUMBER + "," + UPDATED_DEAL_NUMBER, "dealNumber.in=" + UPDATED_DEAL_NUMBER);
    }

    @Test
    @Transactional
    void getAllDealsByDealNumberIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedDeal = dealRepository.saveAndFlush(deal);

        // Get all the dealList where dealNumber is not null
        defaultDealFiltering("dealNumber.specified=true", "dealNumber.specified=false");
    }

    @Test
    @Transactional
    void getAllDealsByDealNumberContainsSomething() throws Exception {
        // Initialize the database
        insertedDeal = dealRepository.saveAndFlush(deal);

        // Get all the dealList where dealNumber contains
        defaultDealFiltering("dealNumber.contains=" + DEFAULT_DEAL_NUMBER, "dealNumber.contains=" + UPDATED_DEAL_NUMBER);
    }

    @Test
    @Transactional
    void getAllDealsByDealNumberNotContainsSomething() throws Exception {
        // Initialize the database
        insertedDeal = dealRepository.saveAndFlush(deal);

        // Get all the dealList where dealNumber does not contain
        defaultDealFiltering("dealNumber.doesNotContain=" + UPDATED_DEAL_NUMBER, "dealNumber.doesNotContain=" + DEFAULT_DEAL_NUMBER);
    }

    @Test
    @Transactional
    void getAllDealsByDealValueIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedDeal = dealRepository.saveAndFlush(deal);

        // Get all the dealList where dealValue equals to
        defaultDealFiltering("dealValue.equals=" + DEFAULT_DEAL_VALUE, "dealValue.equals=" + UPDATED_DEAL_VALUE);
    }

    @Test
    @Transactional
    void getAllDealsByDealValueIsInShouldWork() throws Exception {
        // Initialize the database
        insertedDeal = dealRepository.saveAndFlush(deal);

        // Get all the dealList where dealValue in
        defaultDealFiltering("dealValue.in=" + DEFAULT_DEAL_VALUE + "," + UPDATED_DEAL_VALUE, "dealValue.in=" + UPDATED_DEAL_VALUE);
    }

    @Test
    @Transactional
    void getAllDealsByDealValueIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedDeal = dealRepository.saveAndFlush(deal);

        // Get all the dealList where dealValue is not null
        defaultDealFiltering("dealValue.specified=true", "dealValue.specified=false");
    }

    @Test
    @Transactional
    void getAllDealsByDealValueIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedDeal = dealRepository.saveAndFlush(deal);

        // Get all the dealList where dealValue is greater than or equal to
        defaultDealFiltering("dealValue.greaterThanOrEqual=" + DEFAULT_DEAL_VALUE, "dealValue.greaterThanOrEqual=" + UPDATED_DEAL_VALUE);
    }

    @Test
    @Transactional
    void getAllDealsByDealValueIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedDeal = dealRepository.saveAndFlush(deal);

        // Get all the dealList where dealValue is less than or equal to
        defaultDealFiltering("dealValue.lessThanOrEqual=" + DEFAULT_DEAL_VALUE, "dealValue.lessThanOrEqual=" + SMALLER_DEAL_VALUE);
    }

    @Test
    @Transactional
    void getAllDealsByDealValueIsLessThanSomething() throws Exception {
        // Initialize the database
        insertedDeal = dealRepository.saveAndFlush(deal);

        // Get all the dealList where dealValue is less than
        defaultDealFiltering("dealValue.lessThan=" + UPDATED_DEAL_VALUE, "dealValue.lessThan=" + DEFAULT_DEAL_VALUE);
    }

    @Test
    @Transactional
    void getAllDealsByDealValueIsGreaterThanSomething() throws Exception {
        // Initialize the database
        insertedDeal = dealRepository.saveAndFlush(deal);

        // Get all the dealList where dealValue is greater than
        defaultDealFiltering("dealValue.greaterThan=" + SMALLER_DEAL_VALUE, "dealValue.greaterThan=" + DEFAULT_DEAL_VALUE);
    }

    @Test
    @Transactional
    void getAllDealsByStatusIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedDeal = dealRepository.saveAndFlush(deal);

        // Get all the dealList where status equals to
        defaultDealFiltering("status.equals=" + DEFAULT_STATUS, "status.equals=" + UPDATED_STATUS);
    }

    @Test
    @Transactional
    void getAllDealsByStatusIsInShouldWork() throws Exception {
        // Initialize the database
        insertedDeal = dealRepository.saveAndFlush(deal);

        // Get all the dealList where status in
        defaultDealFiltering("status.in=" + DEFAULT_STATUS + "," + UPDATED_STATUS, "status.in=" + UPDATED_STATUS);
    }

    @Test
    @Transactional
    void getAllDealsByStatusIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedDeal = dealRepository.saveAndFlush(deal);

        // Get all the dealList where status is not null
        defaultDealFiltering("status.specified=true", "status.specified=false");
    }

    @Test
    @Transactional
    void getAllDealsByCurrencyIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedDeal = dealRepository.saveAndFlush(deal);

        // Get all the dealList where currency equals to
        defaultDealFiltering("currency.equals=" + DEFAULT_CURRENCY, "currency.equals=" + UPDATED_CURRENCY);
    }

    @Test
    @Transactional
    void getAllDealsByCurrencyIsInShouldWork() throws Exception {
        // Initialize the database
        insertedDeal = dealRepository.saveAndFlush(deal);

        // Get all the dealList where currency in
        defaultDealFiltering("currency.in=" + DEFAULT_CURRENCY + "," + UPDATED_CURRENCY, "currency.in=" + UPDATED_CURRENCY);
    }

    @Test
    @Transactional
    void getAllDealsByCurrencyIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedDeal = dealRepository.saveAndFlush(deal);

        // Get all the dealList where currency is not null
        defaultDealFiltering("currency.specified=true", "currency.specified=false");
    }

    @Test
    @Transactional
    void getAllDealsByCurrencyContainsSomething() throws Exception {
        // Initialize the database
        insertedDeal = dealRepository.saveAndFlush(deal);

        // Get all the dealList where currency contains
        defaultDealFiltering("currency.contains=" + DEFAULT_CURRENCY, "currency.contains=" + UPDATED_CURRENCY);
    }

    @Test
    @Transactional
    void getAllDealsByCurrencyNotContainsSomething() throws Exception {
        // Initialize the database
        insertedDeal = dealRepository.saveAndFlush(deal);

        // Get all the dealList where currency does not contain
        defaultDealFiltering("currency.doesNotContain=" + UPDATED_CURRENCY, "currency.doesNotContain=" + DEFAULT_CURRENCY);
    }

    @Test
    @Transactional
    void getAllDealsByStartDateIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedDeal = dealRepository.saveAndFlush(deal);

        // Get all the dealList where startDate equals to
        defaultDealFiltering("startDate.equals=" + DEFAULT_START_DATE, "startDate.equals=" + UPDATED_START_DATE);
    }

    @Test
    @Transactional
    void getAllDealsByStartDateIsInShouldWork() throws Exception {
        // Initialize the database
        insertedDeal = dealRepository.saveAndFlush(deal);

        // Get all the dealList where startDate in
        defaultDealFiltering("startDate.in=" + DEFAULT_START_DATE + "," + UPDATED_START_DATE, "startDate.in=" + UPDATED_START_DATE);
    }

    @Test
    @Transactional
    void getAllDealsByStartDateIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedDeal = dealRepository.saveAndFlush(deal);

        // Get all the dealList where startDate is not null
        defaultDealFiltering("startDate.specified=true", "startDate.specified=false");
    }

    @Test
    @Transactional
    void getAllDealsByCloseDateIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedDeal = dealRepository.saveAndFlush(deal);

        // Get all the dealList where closeDate equals to
        defaultDealFiltering("closeDate.equals=" + DEFAULT_CLOSE_DATE, "closeDate.equals=" + UPDATED_CLOSE_DATE);
    }

    @Test
    @Transactional
    void getAllDealsByCloseDateIsInShouldWork() throws Exception {
        // Initialize the database
        insertedDeal = dealRepository.saveAndFlush(deal);

        // Get all the dealList where closeDate in
        defaultDealFiltering("closeDate.in=" + DEFAULT_CLOSE_DATE + "," + UPDATED_CLOSE_DATE, "closeDate.in=" + UPDATED_CLOSE_DATE);
    }

    @Test
    @Transactional
    void getAllDealsByCloseDateIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedDeal = dealRepository.saveAndFlush(deal);

        // Get all the dealList where closeDate is not null
        defaultDealFiltering("closeDate.specified=true", "closeDate.specified=false");
    }

    @Test
    @Transactional
    void getAllDealsByNotesIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedDeal = dealRepository.saveAndFlush(deal);

        // Get all the dealList where notes equals to
        defaultDealFiltering("notes.equals=" + DEFAULT_NOTES, "notes.equals=" + UPDATED_NOTES);
    }

    @Test
    @Transactional
    void getAllDealsByNotesIsInShouldWork() throws Exception {
        // Initialize the database
        insertedDeal = dealRepository.saveAndFlush(deal);

        // Get all the dealList where notes in
        defaultDealFiltering("notes.in=" + DEFAULT_NOTES + "," + UPDATED_NOTES, "notes.in=" + UPDATED_NOTES);
    }

    @Test
    @Transactional
    void getAllDealsByNotesIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedDeal = dealRepository.saveAndFlush(deal);

        // Get all the dealList where notes is not null
        defaultDealFiltering("notes.specified=true", "notes.specified=false");
    }

    @Test
    @Transactional
    void getAllDealsByNotesContainsSomething() throws Exception {
        // Initialize the database
        insertedDeal = dealRepository.saveAndFlush(deal);

        // Get all the dealList where notes contains
        defaultDealFiltering("notes.contains=" + DEFAULT_NOTES, "notes.contains=" + UPDATED_NOTES);
    }

    @Test
    @Transactional
    void getAllDealsByNotesNotContainsSomething() throws Exception {
        // Initialize the database
        insertedDeal = dealRepository.saveAndFlush(deal);

        // Get all the dealList where notes does not contain
        defaultDealFiltering("notes.doesNotContain=" + UPDATED_NOTES, "notes.doesNotContain=" + DEFAULT_NOTES);
    }

    @Test
    @Transactional
    void getAllDealsByTenantIsEqualToSomething() throws Exception {
        Tenant tenant;
        if (TestUtil.findAll(em, Tenant.class).isEmpty()) {
            dealRepository.saveAndFlush(deal);
            tenant = TenantResourceIT.createEntity();
        } else {
            tenant = TestUtil.findAll(em, Tenant.class).get(0);
        }
        em.persist(tenant);
        em.flush();
        deal.setTenant(tenant);
        dealRepository.saveAndFlush(deal);
        Long tenantId = tenant.getId();
        // Get all the dealList where tenant equals to tenantId
        defaultDealShouldBeFound("tenantId.equals=" + tenantId);

        // Get all the dealList where tenant equals to (tenantId + 1)
        defaultDealShouldNotBeFound("tenantId.equals=" + (tenantId + 1));
    }

    @Test
    @Transactional
    void getAllDealsByCustomerIsEqualToSomething() throws Exception {
        Customer customer;
        if (TestUtil.findAll(em, Customer.class).isEmpty()) {
            dealRepository.saveAndFlush(deal);
            customer = CustomerResourceIT.createEntity(em);
        } else {
            customer = TestUtil.findAll(em, Customer.class).get(0);
        }
        em.persist(customer);
        em.flush();
        deal.setCustomer(customer);
        dealRepository.saveAndFlush(deal);
        Long customerId = customer.getId();
        // Get all the dealList where customer equals to customerId
        defaultDealShouldBeFound("customerId.equals=" + customerId);

        // Get all the dealList where customer equals to (customerId + 1)
        defaultDealShouldNotBeFound("customerId.equals=" + (customerId + 1));
    }

    @Test
    @Transactional
    void getAllDealsByContactIsEqualToSomething() throws Exception {
        Contact contact;
        if (TestUtil.findAll(em, Contact.class).isEmpty()) {
            dealRepository.saveAndFlush(deal);
            contact = ContactResourceIT.createEntity(em);
        } else {
            contact = TestUtil.findAll(em, Contact.class).get(0);
        }
        em.persist(contact);
        em.flush();
        deal.setContact(contact);
        dealRepository.saveAndFlush(deal);
        Long contactId = contact.getId();
        // Get all the dealList where contact equals to contactId
        defaultDealShouldBeFound("contactId.equals=" + contactId);

        // Get all the dealList where contact equals to (contactId + 1)
        defaultDealShouldNotBeFound("contactId.equals=" + (contactId + 1));
    }

    @Test
    @Transactional
    void getAllDealsByLeadIsEqualToSomething() throws Exception {
        Lead lead;
        if (TestUtil.findAll(em, Lead.class).isEmpty()) {
            dealRepository.saveAndFlush(deal);
            lead = LeadResourceIT.createEntity(em);
        } else {
            lead = TestUtil.findAll(em, Lead.class).get(0);
        }
        em.persist(lead);
        em.flush();
        deal.setLead(lead);
        dealRepository.saveAndFlush(deal);
        Long leadId = lead.getId();
        // Get all the dealList where lead equals to leadId
        defaultDealShouldBeFound("leadId.equals=" + leadId);

        // Get all the dealList where lead equals to (leadId + 1)
        defaultDealShouldNotBeFound("leadId.equals=" + (leadId + 1));
    }

    private void defaultDealFiltering(String shouldBeFound, String shouldNotBeFound) throws Exception {
        defaultDealShouldBeFound(shouldBeFound);
        defaultDealShouldNotBeFound(shouldNotBeFound);
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultDealShouldBeFound(String filter) throws Exception {
        restDealMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(deal.getId().intValue())))
            .andExpect(jsonPath("$.[*].dealNumber").value(hasItem(DEFAULT_DEAL_NUMBER)))
            .andExpect(jsonPath("$.[*].dealValue").value(hasItem(sameNumber(DEFAULT_DEAL_VALUE))))
            .andExpect(jsonPath("$.[*].status").value(hasItem(DEFAULT_STATUS.toString())))
            .andExpect(jsonPath("$.[*].currency").value(hasItem(DEFAULT_CURRENCY)))
            .andExpect(jsonPath("$.[*].startDate").value(hasItem(DEFAULT_START_DATE.toString())))
            .andExpect(jsonPath("$.[*].closeDate").value(hasItem(DEFAULT_CLOSE_DATE.toString())))
            .andExpect(jsonPath("$.[*].notes").value(hasItem(DEFAULT_NOTES)));

        // Check, that the count call also returns 1
        restDealMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultDealShouldNotBeFound(String filter) throws Exception {
        restDealMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restDealMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingDeal() throws Exception {
        // Get the deal
        restDealMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingDeal() throws Exception {
        // Initialize the database
        insertedDeal = dealRepository.saveAndFlush(deal);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the deal
        Deal updatedDeal = dealRepository.findById(deal.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedDeal are not directly saved in db
        em.detach(updatedDeal);
        updatedDeal
            .dealNumber(UPDATED_DEAL_NUMBER)
            .dealValue(UPDATED_DEAL_VALUE)
            .status(UPDATED_STATUS)
            .currency(UPDATED_CURRENCY)
            .startDate(UPDATED_START_DATE)
            .closeDate(UPDATED_CLOSE_DATE)
            .notes(UPDATED_NOTES);
        DealDTO dealDTO = dealMapper.toDto(updatedDeal);

        restDealMockMvc
            .perform(put(ENTITY_API_URL_ID, dealDTO.getId()).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(dealDTO)))
            .andExpect(status().isOk());

        // Validate the Deal in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedDealToMatchAllProperties(updatedDeal);
    }

    @Test
    @Transactional
    void putNonExistingDeal() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        deal.setId(longCount.incrementAndGet());

        // Create the Deal
        DealDTO dealDTO = dealMapper.toDto(deal);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restDealMockMvc
            .perform(put(ENTITY_API_URL_ID, dealDTO.getId()).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(dealDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Deal in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchDeal() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        deal.setId(longCount.incrementAndGet());

        // Create the Deal
        DealDTO dealDTO = dealMapper.toDto(deal);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restDealMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(dealDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Deal in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamDeal() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        deal.setId(longCount.incrementAndGet());

        // Create the Deal
        DealDTO dealDTO = dealMapper.toDto(deal);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restDealMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(dealDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Deal in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateDealWithPatch() throws Exception {
        // Initialize the database
        insertedDeal = dealRepository.saveAndFlush(deal);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the deal using partial update
        Deal partialUpdatedDeal = new Deal();
        partialUpdatedDeal.setId(deal.getId());

        partialUpdatedDeal.startDate(UPDATED_START_DATE).notes(UPDATED_NOTES);

        restDealMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedDeal.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedDeal))
            )
            .andExpect(status().isOk());

        // Validate the Deal in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertDealUpdatableFieldsEquals(createUpdateProxyForBean(partialUpdatedDeal, deal), getPersistedDeal(deal));
    }

    @Test
    @Transactional
    void fullUpdateDealWithPatch() throws Exception {
        // Initialize the database
        insertedDeal = dealRepository.saveAndFlush(deal);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the deal using partial update
        Deal partialUpdatedDeal = new Deal();
        partialUpdatedDeal.setId(deal.getId());

        partialUpdatedDeal
            .dealNumber(UPDATED_DEAL_NUMBER)
            .dealValue(UPDATED_DEAL_VALUE)
            .status(UPDATED_STATUS)
            .currency(UPDATED_CURRENCY)
            .startDate(UPDATED_START_DATE)
            .closeDate(UPDATED_CLOSE_DATE)
            .notes(UPDATED_NOTES);

        restDealMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedDeal.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedDeal))
            )
            .andExpect(status().isOk());

        // Validate the Deal in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertDealUpdatableFieldsEquals(partialUpdatedDeal, getPersistedDeal(partialUpdatedDeal));
    }

    @Test
    @Transactional
    void patchNonExistingDeal() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        deal.setId(longCount.incrementAndGet());

        // Create the Deal
        DealDTO dealDTO = dealMapper.toDto(deal);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restDealMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, dealDTO.getId()).contentType("application/merge-patch+json").content(om.writeValueAsBytes(dealDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Deal in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchDeal() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        deal.setId(longCount.incrementAndGet());

        // Create the Deal
        DealDTO dealDTO = dealMapper.toDto(deal);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restDealMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(dealDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Deal in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamDeal() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        deal.setId(longCount.incrementAndGet());

        // Create the Deal
        DealDTO dealDTO = dealMapper.toDto(deal);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restDealMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(om.writeValueAsBytes(dealDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Deal in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteDeal() throws Exception {
        // Initialize the database
        insertedDeal = dealRepository.saveAndFlush(deal);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the deal
        restDealMockMvc
            .perform(delete(ENTITY_API_URL_ID, deal.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return dealRepository.count();
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

    protected Deal getPersistedDeal(Deal deal) {
        return dealRepository.findById(deal.getId()).orElseThrow();
    }

    protected void assertPersistedDealToMatchAllProperties(Deal expectedDeal) {
        assertDealAllPropertiesEquals(expectedDeal, getPersistedDeal(expectedDeal));
    }

    protected void assertPersistedDealToMatchUpdatableProperties(Deal expectedDeal) {
        assertDealAllUpdatablePropertiesEquals(expectedDeal, getPersistedDeal(expectedDeal));
    }
}
