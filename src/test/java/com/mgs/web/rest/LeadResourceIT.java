package com.mgs.web.rest;

import static com.mgs.domain.LeadAsserts.*;
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
import com.mgs.domain.Lead;
import com.mgs.domain.LeadSource;
import com.mgs.domain.Pipeline;
import com.mgs.domain.SubPipeline;
import com.mgs.domain.Tenant;
import com.mgs.domain.User;
import com.mgs.domain.enumeration.LeadStatus;
import com.mgs.repository.LeadRepository;
import com.mgs.service.dto.LeadDTO;
import com.mgs.service.mapper.LeadMapper;
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
 * Integration tests for the {@link LeadResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class LeadResourceIT {

    private static final String DEFAULT_TITLE = "AAAAAAAAAA";
    private static final String UPDATED_TITLE = "BBBBBBBBBB";

    private static final LeadStatus DEFAULT_STATUS = LeadStatus.NEW;
    private static final LeadStatus UPDATED_STATUS = LeadStatus.CONTACTED;

    private static final BigDecimal DEFAULT_ESTIMATED_VALUE = new BigDecimal(1);
    private static final BigDecimal UPDATED_ESTIMATED_VALUE = new BigDecimal(2);
    private static final BigDecimal SMALLER_ESTIMATED_VALUE = new BigDecimal(1 - 1);

    private static final String DEFAULT_CURRENCY = "AAAAAAAAAA";
    private static final String UPDATED_CURRENCY = "BBBBBBBBBB";

    private static final String DEFAULT_NOTES = "AAAAAAAAAA";
    private static final String UPDATED_NOTES = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/leads";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ObjectMapper om;

    @Autowired
    private LeadRepository leadRepository;

    @Autowired
    private LeadMapper leadMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restLeadMockMvc;

    private Lead lead;

    private Lead insertedLead;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Lead createEntity(EntityManager em) {
        Lead lead = new Lead()
            .title(DEFAULT_TITLE)
            .status(DEFAULT_STATUS)
            .estimatedValue(DEFAULT_ESTIMATED_VALUE)
            .currency(DEFAULT_CURRENCY)
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
        lead.setTenant(tenant);
        // Add required entity
        User user = UserResourceIT.createEntity(em);
        em.persist(user);
        em.flush();
        lead.setOwnerUser(user);
        return lead;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Lead createUpdatedEntity(EntityManager em) {
        Lead updatedLead = new Lead()
            .title(UPDATED_TITLE)
            .status(UPDATED_STATUS)
            .estimatedValue(UPDATED_ESTIMATED_VALUE)
            .currency(UPDATED_CURRENCY)
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
        updatedLead.setTenant(tenant);
        // Add required entity
        User user = UserResourceIT.createEntity(em);
        em.persist(user);
        em.flush();
        updatedLead.setOwnerUser(user);
        return updatedLead;
    }

    @BeforeEach
    void initTest() {
        lead = createEntity(em);
    }

    @AfterEach
    void cleanup() {
        if (insertedLead != null) {
            leadRepository.delete(insertedLead);
            insertedLead = null;
        }
    }

    @Test
    @Transactional
    void createLead() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the Lead
        LeadDTO leadDTO = leadMapper.toDto(lead);
        var returnedLeadDTO = om.readValue(
            restLeadMockMvc
                .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(leadDTO)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            LeadDTO.class
        );

        // Validate the Lead in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        var returnedLead = leadMapper.toEntity(returnedLeadDTO);
        assertLeadUpdatableFieldsEquals(returnedLead, getPersistedLead(returnedLead));

        insertedLead = returnedLead;
    }

    @Test
    @Transactional
    void createLeadWithExistingId() throws Exception {
        // Create the Lead with an existing ID
        lead.setId(1L);
        LeadDTO leadDTO = leadMapper.toDto(lead);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restLeadMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(leadDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Lead in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkTitleIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        lead.setTitle(null);

        // Create the Lead, which fails.
        LeadDTO leadDTO = leadMapper.toDto(lead);

        restLeadMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(leadDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkStatusIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        lead.setStatus(null);

        // Create the Lead, which fails.
        LeadDTO leadDTO = leadMapper.toDto(lead);

        restLeadMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(leadDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllLeads() throws Exception {
        // Initialize the database
        insertedLead = leadRepository.saveAndFlush(lead);

        // Get all the leadList
        restLeadMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(lead.getId().intValue())))
            .andExpect(jsonPath("$.[*].title").value(hasItem(DEFAULT_TITLE)))
            .andExpect(jsonPath("$.[*].status").value(hasItem(DEFAULT_STATUS.toString())))
            .andExpect(jsonPath("$.[*].estimatedValue").value(hasItem(sameNumber(DEFAULT_ESTIMATED_VALUE))))
            .andExpect(jsonPath("$.[*].currency").value(hasItem(DEFAULT_CURRENCY)))
            .andExpect(jsonPath("$.[*].notes").value(hasItem(DEFAULT_NOTES)));
    }

    @Test
    @Transactional
    void getLead() throws Exception {
        // Initialize the database
        insertedLead = leadRepository.saveAndFlush(lead);

        // Get the lead
        restLeadMockMvc
            .perform(get(ENTITY_API_URL_ID, lead.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(lead.getId().intValue()))
            .andExpect(jsonPath("$.title").value(DEFAULT_TITLE))
            .andExpect(jsonPath("$.status").value(DEFAULT_STATUS.toString()))
            .andExpect(jsonPath("$.estimatedValue").value(sameNumber(DEFAULT_ESTIMATED_VALUE)))
            .andExpect(jsonPath("$.currency").value(DEFAULT_CURRENCY))
            .andExpect(jsonPath("$.notes").value(DEFAULT_NOTES));
    }

    @Test
    @Transactional
    void getLeadsByIdFiltering() throws Exception {
        // Initialize the database
        insertedLead = leadRepository.saveAndFlush(lead);

        Long id = lead.getId();

        defaultLeadFiltering("id.equals=" + id, "id.notEquals=" + id);

        defaultLeadFiltering("id.greaterThanOrEqual=" + id, "id.greaterThan=" + id);

        defaultLeadFiltering("id.lessThanOrEqual=" + id, "id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllLeadsByTitleIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedLead = leadRepository.saveAndFlush(lead);

        // Get all the leadList where title equals to
        defaultLeadFiltering("title.equals=" + DEFAULT_TITLE, "title.equals=" + UPDATED_TITLE);
    }

    @Test
    @Transactional
    void getAllLeadsByTitleIsInShouldWork() throws Exception {
        // Initialize the database
        insertedLead = leadRepository.saveAndFlush(lead);

        // Get all the leadList where title in
        defaultLeadFiltering("title.in=" + DEFAULT_TITLE + "," + UPDATED_TITLE, "title.in=" + UPDATED_TITLE);
    }

    @Test
    @Transactional
    void getAllLeadsByTitleIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedLead = leadRepository.saveAndFlush(lead);

        // Get all the leadList where title is not null
        defaultLeadFiltering("title.specified=true", "title.specified=false");
    }

    @Test
    @Transactional
    void getAllLeadsByTitleContainsSomething() throws Exception {
        // Initialize the database
        insertedLead = leadRepository.saveAndFlush(lead);

        // Get all the leadList where title contains
        defaultLeadFiltering("title.contains=" + DEFAULT_TITLE, "title.contains=" + UPDATED_TITLE);
    }

    @Test
    @Transactional
    void getAllLeadsByTitleNotContainsSomething() throws Exception {
        // Initialize the database
        insertedLead = leadRepository.saveAndFlush(lead);

        // Get all the leadList where title does not contain
        defaultLeadFiltering("title.doesNotContain=" + UPDATED_TITLE, "title.doesNotContain=" + DEFAULT_TITLE);
    }

    @Test
    @Transactional
    void getAllLeadsByStatusIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedLead = leadRepository.saveAndFlush(lead);

        // Get all the leadList where status equals to
        defaultLeadFiltering("status.equals=" + DEFAULT_STATUS, "status.equals=" + UPDATED_STATUS);
    }

    @Test
    @Transactional
    void getAllLeadsByStatusIsInShouldWork() throws Exception {
        // Initialize the database
        insertedLead = leadRepository.saveAndFlush(lead);

        // Get all the leadList where status in
        defaultLeadFiltering("status.in=" + DEFAULT_STATUS + "," + UPDATED_STATUS, "status.in=" + UPDATED_STATUS);
    }

    @Test
    @Transactional
    void getAllLeadsByStatusIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedLead = leadRepository.saveAndFlush(lead);

        // Get all the leadList where status is not null
        defaultLeadFiltering("status.specified=true", "status.specified=false");
    }

    @Test
    @Transactional
    void getAllLeadsByEstimatedValueIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedLead = leadRepository.saveAndFlush(lead);

        // Get all the leadList where estimatedValue equals to
        defaultLeadFiltering("estimatedValue.equals=" + DEFAULT_ESTIMATED_VALUE, "estimatedValue.equals=" + UPDATED_ESTIMATED_VALUE);
    }

    @Test
    @Transactional
    void getAllLeadsByEstimatedValueIsInShouldWork() throws Exception {
        // Initialize the database
        insertedLead = leadRepository.saveAndFlush(lead);

        // Get all the leadList where estimatedValue in
        defaultLeadFiltering(
            "estimatedValue.in=" + DEFAULT_ESTIMATED_VALUE + "," + UPDATED_ESTIMATED_VALUE,
            "estimatedValue.in=" + UPDATED_ESTIMATED_VALUE
        );
    }

    @Test
    @Transactional
    void getAllLeadsByEstimatedValueIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedLead = leadRepository.saveAndFlush(lead);

        // Get all the leadList where estimatedValue is not null
        defaultLeadFiltering("estimatedValue.specified=true", "estimatedValue.specified=false");
    }

    @Test
    @Transactional
    void getAllLeadsByEstimatedValueIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedLead = leadRepository.saveAndFlush(lead);

        // Get all the leadList where estimatedValue is greater than or equal to
        defaultLeadFiltering(
            "estimatedValue.greaterThanOrEqual=" + DEFAULT_ESTIMATED_VALUE,
            "estimatedValue.greaterThanOrEqual=" + UPDATED_ESTIMATED_VALUE
        );
    }

    @Test
    @Transactional
    void getAllLeadsByEstimatedValueIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedLead = leadRepository.saveAndFlush(lead);

        // Get all the leadList where estimatedValue is less than or equal to
        defaultLeadFiltering(
            "estimatedValue.lessThanOrEqual=" + DEFAULT_ESTIMATED_VALUE,
            "estimatedValue.lessThanOrEqual=" + SMALLER_ESTIMATED_VALUE
        );
    }

    @Test
    @Transactional
    void getAllLeadsByEstimatedValueIsLessThanSomething() throws Exception {
        // Initialize the database
        insertedLead = leadRepository.saveAndFlush(lead);

        // Get all the leadList where estimatedValue is less than
        defaultLeadFiltering("estimatedValue.lessThan=" + UPDATED_ESTIMATED_VALUE, "estimatedValue.lessThan=" + DEFAULT_ESTIMATED_VALUE);
    }

    @Test
    @Transactional
    void getAllLeadsByEstimatedValueIsGreaterThanSomething() throws Exception {
        // Initialize the database
        insertedLead = leadRepository.saveAndFlush(lead);

        // Get all the leadList where estimatedValue is greater than
        defaultLeadFiltering(
            "estimatedValue.greaterThan=" + SMALLER_ESTIMATED_VALUE,
            "estimatedValue.greaterThan=" + DEFAULT_ESTIMATED_VALUE
        );
    }

    @Test
    @Transactional
    void getAllLeadsByCurrencyIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedLead = leadRepository.saveAndFlush(lead);

        // Get all the leadList where currency equals to
        defaultLeadFiltering("currency.equals=" + DEFAULT_CURRENCY, "currency.equals=" + UPDATED_CURRENCY);
    }

    @Test
    @Transactional
    void getAllLeadsByCurrencyIsInShouldWork() throws Exception {
        // Initialize the database
        insertedLead = leadRepository.saveAndFlush(lead);

        // Get all the leadList where currency in
        defaultLeadFiltering("currency.in=" + DEFAULT_CURRENCY + "," + UPDATED_CURRENCY, "currency.in=" + UPDATED_CURRENCY);
    }

    @Test
    @Transactional
    void getAllLeadsByCurrencyIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedLead = leadRepository.saveAndFlush(lead);

        // Get all the leadList where currency is not null
        defaultLeadFiltering("currency.specified=true", "currency.specified=false");
    }

    @Test
    @Transactional
    void getAllLeadsByCurrencyContainsSomething() throws Exception {
        // Initialize the database
        insertedLead = leadRepository.saveAndFlush(lead);

        // Get all the leadList where currency contains
        defaultLeadFiltering("currency.contains=" + DEFAULT_CURRENCY, "currency.contains=" + UPDATED_CURRENCY);
    }

    @Test
    @Transactional
    void getAllLeadsByCurrencyNotContainsSomething() throws Exception {
        // Initialize the database
        insertedLead = leadRepository.saveAndFlush(lead);

        // Get all the leadList where currency does not contain
        defaultLeadFiltering("currency.doesNotContain=" + UPDATED_CURRENCY, "currency.doesNotContain=" + DEFAULT_CURRENCY);
    }

    @Test
    @Transactional
    void getAllLeadsByNotesIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedLead = leadRepository.saveAndFlush(lead);

        // Get all the leadList where notes equals to
        defaultLeadFiltering("notes.equals=" + DEFAULT_NOTES, "notes.equals=" + UPDATED_NOTES);
    }

    @Test
    @Transactional
    void getAllLeadsByNotesIsInShouldWork() throws Exception {
        // Initialize the database
        insertedLead = leadRepository.saveAndFlush(lead);

        // Get all the leadList where notes in
        defaultLeadFiltering("notes.in=" + DEFAULT_NOTES + "," + UPDATED_NOTES, "notes.in=" + UPDATED_NOTES);
    }

    @Test
    @Transactional
    void getAllLeadsByNotesIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedLead = leadRepository.saveAndFlush(lead);

        // Get all the leadList where notes is not null
        defaultLeadFiltering("notes.specified=true", "notes.specified=false");
    }

    @Test
    @Transactional
    void getAllLeadsByNotesContainsSomething() throws Exception {
        // Initialize the database
        insertedLead = leadRepository.saveAndFlush(lead);

        // Get all the leadList where notes contains
        defaultLeadFiltering("notes.contains=" + DEFAULT_NOTES, "notes.contains=" + UPDATED_NOTES);
    }

    @Test
    @Transactional
    void getAllLeadsByNotesNotContainsSomething() throws Exception {
        // Initialize the database
        insertedLead = leadRepository.saveAndFlush(lead);

        // Get all the leadList where notes does not contain
        defaultLeadFiltering("notes.doesNotContain=" + UPDATED_NOTES, "notes.doesNotContain=" + DEFAULT_NOTES);
    }

    @Test
    @Transactional
    void getAllLeadsByTenantIsEqualToSomething() throws Exception {
        Tenant tenant;
        if (TestUtil.findAll(em, Tenant.class).isEmpty()) {
            leadRepository.saveAndFlush(lead);
            tenant = TenantResourceIT.createEntity();
        } else {
            tenant = TestUtil.findAll(em, Tenant.class).get(0);
        }
        em.persist(tenant);
        em.flush();
        lead.setTenant(tenant);
        leadRepository.saveAndFlush(lead);
        Long tenantId = tenant.getId();
        // Get all the leadList where tenant equals to tenantId
        defaultLeadShouldBeFound("tenantId.equals=" + tenantId);

        // Get all the leadList where tenant equals to (tenantId + 1)
        defaultLeadShouldNotBeFound("tenantId.equals=" + (tenantId + 1));
    }

    @Test
    @Transactional
    void getAllLeadsByCustomerIsEqualToSomething() throws Exception {
        Customer customer;
        if (TestUtil.findAll(em, Customer.class).isEmpty()) {
            leadRepository.saveAndFlush(lead);
            customer = CustomerResourceIT.createEntity(em);
        } else {
            customer = TestUtil.findAll(em, Customer.class).get(0);
        }
        em.persist(customer);
        em.flush();
        lead.setCustomer(customer);
        leadRepository.saveAndFlush(lead);
        Long customerId = customer.getId();
        // Get all the leadList where customer equals to customerId
        defaultLeadShouldBeFound("customerId.equals=" + customerId);

        // Get all the leadList where customer equals to (customerId + 1)
        defaultLeadShouldNotBeFound("customerId.equals=" + (customerId + 1));
    }

    @Test
    @Transactional
    void getAllLeadsByContactIsEqualToSomething() throws Exception {
        Contact contact;
        if (TestUtil.findAll(em, Contact.class).isEmpty()) {
            leadRepository.saveAndFlush(lead);
            contact = ContactResourceIT.createEntity(em);
        } else {
            contact = TestUtil.findAll(em, Contact.class).get(0);
        }
        em.persist(contact);
        em.flush();
        lead.setContact(contact);
        leadRepository.saveAndFlush(lead);
        Long contactId = contact.getId();
        // Get all the leadList where contact equals to contactId
        defaultLeadShouldBeFound("contactId.equals=" + contactId);

        // Get all the leadList where contact equals to (contactId + 1)
        defaultLeadShouldNotBeFound("contactId.equals=" + (contactId + 1));
    }

    @Test
    @Transactional
    void getAllLeadsBySourceIsEqualToSomething() throws Exception {
        LeadSource source;
        if (TestUtil.findAll(em, LeadSource.class).isEmpty()) {
            leadRepository.saveAndFlush(lead);
            source = LeadSourceResourceIT.createEntity(em);
        } else {
            source = TestUtil.findAll(em, LeadSource.class).get(0);
        }
        em.persist(source);
        em.flush();
        lead.setSource(source);
        leadRepository.saveAndFlush(lead);
        Long sourceId = source.getId();
        // Get all the leadList where source equals to sourceId
        defaultLeadShouldBeFound("sourceId.equals=" + sourceId);

        // Get all the leadList where source equals to (sourceId + 1)
        defaultLeadShouldNotBeFound("sourceId.equals=" + (sourceId + 1));
    }

    @Test
    @Transactional
    void getAllLeadsByPipelineIsEqualToSomething() throws Exception {
        Pipeline pipeline;
        if (TestUtil.findAll(em, Pipeline.class).isEmpty()) {
            leadRepository.saveAndFlush(lead);
            pipeline = PipelineResourceIT.createEntity(em);
        } else {
            pipeline = TestUtil.findAll(em, Pipeline.class).get(0);
        }
        em.persist(pipeline);
        em.flush();
        lead.setPipeline(pipeline);
        leadRepository.saveAndFlush(lead);
        Long pipelineId = pipeline.getId();
        // Get all the leadList where pipeline equals to pipelineId
        defaultLeadShouldBeFound("pipelineId.equals=" + pipelineId);

        // Get all the leadList where pipeline equals to (pipelineId + 1)
        defaultLeadShouldNotBeFound("pipelineId.equals=" + (pipelineId + 1));
    }

    @Test
    @Transactional
    void getAllLeadsByStageIsEqualToSomething() throws Exception {
        SubPipeline stage;
        if (TestUtil.findAll(em, SubPipeline.class).isEmpty()) {
            leadRepository.saveAndFlush(lead);
            stage = SubPipelineResourceIT.createEntity(em);
        } else {
            stage = TestUtil.findAll(em, SubPipeline.class).get(0);
        }
        em.persist(stage);
        em.flush();
        lead.setStage(stage);
        leadRepository.saveAndFlush(lead);
        Long stageId = stage.getId();
        // Get all the leadList where stage equals to stageId
        defaultLeadShouldBeFound("stageId.equals=" + stageId);

        // Get all the leadList where stage equals to (stageId + 1)
        defaultLeadShouldNotBeFound("stageId.equals=" + (stageId + 1));
    }

    @Test
    @Transactional
    void getAllLeadsByOwnerUserIsEqualToSomething() throws Exception {
        User ownerUser;
        if (TestUtil.findAll(em, User.class).isEmpty()) {
            leadRepository.saveAndFlush(lead);
            ownerUser = UserResourceIT.createEntity(em);
        } else {
            ownerUser = TestUtil.findAll(em, User.class).get(0);
        }
        em.persist(ownerUser);
        em.flush();
        lead.setOwnerUser(ownerUser);
        leadRepository.saveAndFlush(lead);
        Long ownerUserId = ownerUser.getId();
        // Get all the leadList where ownerUser equals to ownerUserId
        defaultLeadShouldBeFound("ownerUserId.equals=" + ownerUserId);

        // Get all the leadList where ownerUser equals to (ownerUserId + 1)
        defaultLeadShouldNotBeFound("ownerUserId.equals=" + (ownerUserId + 1));
    }

    private void defaultLeadFiltering(String shouldBeFound, String shouldNotBeFound) throws Exception {
        defaultLeadShouldBeFound(shouldBeFound);
        defaultLeadShouldNotBeFound(shouldNotBeFound);
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultLeadShouldBeFound(String filter) throws Exception {
        restLeadMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(lead.getId().intValue())))
            .andExpect(jsonPath("$.[*].title").value(hasItem(DEFAULT_TITLE)))
            .andExpect(jsonPath("$.[*].status").value(hasItem(DEFAULT_STATUS.toString())))
            .andExpect(jsonPath("$.[*].estimatedValue").value(hasItem(sameNumber(DEFAULT_ESTIMATED_VALUE))))
            .andExpect(jsonPath("$.[*].currency").value(hasItem(DEFAULT_CURRENCY)))
            .andExpect(jsonPath("$.[*].notes").value(hasItem(DEFAULT_NOTES)));

        // Check, that the count call also returns 1
        restLeadMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultLeadShouldNotBeFound(String filter) throws Exception {
        restLeadMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restLeadMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingLead() throws Exception {
        // Get the lead
        restLeadMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingLead() throws Exception {
        // Initialize the database
        insertedLead = leadRepository.saveAndFlush(lead);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the lead
        Lead updatedLead = leadRepository.findById(lead.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedLead are not directly saved in db
        em.detach(updatedLead);
        updatedLead
            .title(UPDATED_TITLE)
            .status(UPDATED_STATUS)
            .estimatedValue(UPDATED_ESTIMATED_VALUE)
            .currency(UPDATED_CURRENCY)
            .notes(UPDATED_NOTES);
        LeadDTO leadDTO = leadMapper.toDto(updatedLead);

        restLeadMockMvc
            .perform(put(ENTITY_API_URL_ID, leadDTO.getId()).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(leadDTO)))
            .andExpect(status().isOk());

        // Validate the Lead in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedLeadToMatchAllProperties(updatedLead);
    }

    @Test
    @Transactional
    void putNonExistingLead() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        lead.setId(longCount.incrementAndGet());

        // Create the Lead
        LeadDTO leadDTO = leadMapper.toDto(lead);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restLeadMockMvc
            .perform(put(ENTITY_API_URL_ID, leadDTO.getId()).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(leadDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Lead in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchLead() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        lead.setId(longCount.incrementAndGet());

        // Create the Lead
        LeadDTO leadDTO = leadMapper.toDto(lead);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restLeadMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(leadDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Lead in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamLead() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        lead.setId(longCount.incrementAndGet());

        // Create the Lead
        LeadDTO leadDTO = leadMapper.toDto(lead);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restLeadMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(leadDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Lead in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateLeadWithPatch() throws Exception {
        // Initialize the database
        insertedLead = leadRepository.saveAndFlush(lead);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the lead using partial update
        Lead partialUpdatedLead = new Lead();
        partialUpdatedLead.setId(lead.getId());

        partialUpdatedLead.status(UPDATED_STATUS).estimatedValue(UPDATED_ESTIMATED_VALUE);

        restLeadMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedLead.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedLead))
            )
            .andExpect(status().isOk());

        // Validate the Lead in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertLeadUpdatableFieldsEquals(createUpdateProxyForBean(partialUpdatedLead, lead), getPersistedLead(lead));
    }

    @Test
    @Transactional
    void fullUpdateLeadWithPatch() throws Exception {
        // Initialize the database
        insertedLead = leadRepository.saveAndFlush(lead);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the lead using partial update
        Lead partialUpdatedLead = new Lead();
        partialUpdatedLead.setId(lead.getId());

        partialUpdatedLead
            .title(UPDATED_TITLE)
            .status(UPDATED_STATUS)
            .estimatedValue(UPDATED_ESTIMATED_VALUE)
            .currency(UPDATED_CURRENCY)
            .notes(UPDATED_NOTES);

        restLeadMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedLead.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedLead))
            )
            .andExpect(status().isOk());

        // Validate the Lead in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertLeadUpdatableFieldsEquals(partialUpdatedLead, getPersistedLead(partialUpdatedLead));
    }

    @Test
    @Transactional
    void patchNonExistingLead() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        lead.setId(longCount.incrementAndGet());

        // Create the Lead
        LeadDTO leadDTO = leadMapper.toDto(lead);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restLeadMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, leadDTO.getId()).contentType("application/merge-patch+json").content(om.writeValueAsBytes(leadDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Lead in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchLead() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        lead.setId(longCount.incrementAndGet());

        // Create the Lead
        LeadDTO leadDTO = leadMapper.toDto(lead);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restLeadMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(leadDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Lead in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamLead() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        lead.setId(longCount.incrementAndGet());

        // Create the Lead
        LeadDTO leadDTO = leadMapper.toDto(lead);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restLeadMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(om.writeValueAsBytes(leadDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Lead in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteLead() throws Exception {
        // Initialize the database
        insertedLead = leadRepository.saveAndFlush(lead);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the lead
        restLeadMockMvc
            .perform(delete(ENTITY_API_URL_ID, lead.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return leadRepository.count();
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

    protected Lead getPersistedLead(Lead lead) {
        return leadRepository.findById(lead.getId()).orElseThrow();
    }

    protected void assertPersistedLeadToMatchAllProperties(Lead expectedLead) {
        assertLeadAllPropertiesEquals(expectedLead, getPersistedLead(expectedLead));
    }

    protected void assertPersistedLeadToMatchUpdatableProperties(Lead expectedLead) {
        assertLeadAllUpdatablePropertiesEquals(expectedLead, getPersistedLead(expectedLead));
    }
}
