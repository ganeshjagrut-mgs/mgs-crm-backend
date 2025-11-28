package com.mgs.web.rest;

import static com.mgs.domain.SubPipelineAsserts.*;
import static com.mgs.web.rest.TestUtil.createUpdateProxyForBean;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mgs.IntegrationTest;
import com.mgs.domain.Pipeline;
import com.mgs.domain.SubPipeline;
import com.mgs.domain.Tenant;
import com.mgs.repository.SubPipelineRepository;
import com.mgs.service.dto.SubPipelineDTO;
import com.mgs.service.mapper.SubPipelineMapper;
import jakarta.persistence.EntityManager;
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
 * Integration tests for the {@link SubPipelineResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class SubPipelineResourceIT {

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_NAME_SEARCH = "AAAAAAAAAA";
    private static final String UPDATED_NAME_SEARCH = "BBBBBBBBBB";

    private static final Integer DEFAULT_SEQUENCE_ORDER = 1;
    private static final Integer UPDATED_SEQUENCE_ORDER = 2;
    private static final Integer SMALLER_SEQUENCE_ORDER = 1 - 1;

    private static final Integer DEFAULT_PROBABILITY = 1;
    private static final Integer UPDATED_PROBABILITY = 2;
    private static final Integer SMALLER_PROBABILITY = 1 - 1;

    private static final Boolean DEFAULT_IS_CLOSING_STAGE = false;
    private static final Boolean UPDATED_IS_CLOSING_STAGE = true;

    private static final String ENTITY_API_URL = "/api/sub-pipelines";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ObjectMapper om;

    @Autowired
    private SubPipelineRepository subPipelineRepository;

    @Autowired
    private SubPipelineMapper subPipelineMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restSubPipelineMockMvc;

    private SubPipeline subPipeline;

    private SubPipeline insertedSubPipeline;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static SubPipeline createEntity(EntityManager em) {
        SubPipeline subPipeline = new SubPipeline()
            .name(DEFAULT_NAME)
            .nameSearch(DEFAULT_NAME_SEARCH)
            .sequenceOrder(DEFAULT_SEQUENCE_ORDER)
            .probability(DEFAULT_PROBABILITY)
            .isClosingStage(DEFAULT_IS_CLOSING_STAGE);
        // Add required entity
        Tenant tenant;
        if (TestUtil.findAll(em, Tenant.class).isEmpty()) {
            tenant = TenantResourceIT.createEntity();
            em.persist(tenant);
            em.flush();
        } else {
            tenant = TestUtil.findAll(em, Tenant.class).get(0);
        }
        subPipeline.setTenant(tenant);
        // Add required entity
        Pipeline pipeline;
        if (TestUtil.findAll(em, Pipeline.class).isEmpty()) {
            pipeline = PipelineResourceIT.createEntity(em);
            em.persist(pipeline);
            em.flush();
        } else {
            pipeline = TestUtil.findAll(em, Pipeline.class).get(0);
        }
        subPipeline.setPipeline(pipeline);
        return subPipeline;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static SubPipeline createUpdatedEntity(EntityManager em) {
        SubPipeline updatedSubPipeline = new SubPipeline()
            .name(UPDATED_NAME)
            .nameSearch(UPDATED_NAME_SEARCH)
            .sequenceOrder(UPDATED_SEQUENCE_ORDER)
            .probability(UPDATED_PROBABILITY)
            .isClosingStage(UPDATED_IS_CLOSING_STAGE);
        // Add required entity
        Tenant tenant;
        if (TestUtil.findAll(em, Tenant.class).isEmpty()) {
            tenant = TenantResourceIT.createUpdatedEntity();
            em.persist(tenant);
            em.flush();
        } else {
            tenant = TestUtil.findAll(em, Tenant.class).get(0);
        }
        updatedSubPipeline.setTenant(tenant);
        // Add required entity
        Pipeline pipeline;
        if (TestUtil.findAll(em, Pipeline.class).isEmpty()) {
            pipeline = PipelineResourceIT.createUpdatedEntity(em);
            em.persist(pipeline);
            em.flush();
        } else {
            pipeline = TestUtil.findAll(em, Pipeline.class).get(0);
        }
        updatedSubPipeline.setPipeline(pipeline);
        return updatedSubPipeline;
    }

    @BeforeEach
    void initTest() {
        subPipeline = createEntity(em);
    }

    @AfterEach
    void cleanup() {
        if (insertedSubPipeline != null) {
            subPipelineRepository.delete(insertedSubPipeline);
            insertedSubPipeline = null;
        }
    }

    @Test
    @Transactional
    void createSubPipeline() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the SubPipeline
        SubPipelineDTO subPipelineDTO = subPipelineMapper.toDto(subPipeline);
        var returnedSubPipelineDTO = om.readValue(
            restSubPipelineMockMvc
                .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(subPipelineDTO)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            SubPipelineDTO.class
        );

        // Validate the SubPipeline in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        var returnedSubPipeline = subPipelineMapper.toEntity(returnedSubPipelineDTO);
        assertSubPipelineUpdatableFieldsEquals(returnedSubPipeline, getPersistedSubPipeline(returnedSubPipeline));

        insertedSubPipeline = returnedSubPipeline;
    }

    @Test
    @Transactional
    void createSubPipelineWithExistingId() throws Exception {
        // Create the SubPipeline with an existing ID
        subPipeline.setId(1L);
        SubPipelineDTO subPipelineDTO = subPipelineMapper.toDto(subPipeline);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restSubPipelineMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(subPipelineDTO)))
            .andExpect(status().isBadRequest());

        // Validate the SubPipeline in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkNameIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        subPipeline.setName(null);

        // Create the SubPipeline, which fails.
        SubPipelineDTO subPipelineDTO = subPipelineMapper.toDto(subPipeline);

        restSubPipelineMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(subPipelineDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkSequenceOrderIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        subPipeline.setSequenceOrder(null);

        // Create the SubPipeline, which fails.
        SubPipelineDTO subPipelineDTO = subPipelineMapper.toDto(subPipeline);

        restSubPipelineMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(subPipelineDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkIsClosingStageIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        subPipeline.setIsClosingStage(null);

        // Create the SubPipeline, which fails.
        SubPipelineDTO subPipelineDTO = subPipelineMapper.toDto(subPipeline);

        restSubPipelineMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(subPipelineDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllSubPipelines() throws Exception {
        // Initialize the database
        insertedSubPipeline = subPipelineRepository.saveAndFlush(subPipeline);

        // Get all the subPipelineList
        restSubPipelineMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(subPipeline.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)))
            .andExpect(jsonPath("$.[*].nameSearch").value(hasItem(DEFAULT_NAME_SEARCH)))
            .andExpect(jsonPath("$.[*].sequenceOrder").value(hasItem(DEFAULT_SEQUENCE_ORDER)))
            .andExpect(jsonPath("$.[*].probability").value(hasItem(DEFAULT_PROBABILITY)))
            .andExpect(jsonPath("$.[*].isClosingStage").value(hasItem(DEFAULT_IS_CLOSING_STAGE)));
    }

    @Test
    @Transactional
    void getSubPipeline() throws Exception {
        // Initialize the database
        insertedSubPipeline = subPipelineRepository.saveAndFlush(subPipeline);

        // Get the subPipeline
        restSubPipelineMockMvc
            .perform(get(ENTITY_API_URL_ID, subPipeline.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(subPipeline.getId().intValue()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME))
            .andExpect(jsonPath("$.nameSearch").value(DEFAULT_NAME_SEARCH))
            .andExpect(jsonPath("$.sequenceOrder").value(DEFAULT_SEQUENCE_ORDER))
            .andExpect(jsonPath("$.probability").value(DEFAULT_PROBABILITY))
            .andExpect(jsonPath("$.isClosingStage").value(DEFAULT_IS_CLOSING_STAGE));
    }

    @Test
    @Transactional
    void getSubPipelinesByIdFiltering() throws Exception {
        // Initialize the database
        insertedSubPipeline = subPipelineRepository.saveAndFlush(subPipeline);

        Long id = subPipeline.getId();

        defaultSubPipelineFiltering("id.equals=" + id, "id.notEquals=" + id);

        defaultSubPipelineFiltering("id.greaterThanOrEqual=" + id, "id.greaterThan=" + id);

        defaultSubPipelineFiltering("id.lessThanOrEqual=" + id, "id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllSubPipelinesByNameIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedSubPipeline = subPipelineRepository.saveAndFlush(subPipeline);

        // Get all the subPipelineList where name equals to
        defaultSubPipelineFiltering("name.equals=" + DEFAULT_NAME, "name.equals=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllSubPipelinesByNameIsInShouldWork() throws Exception {
        // Initialize the database
        insertedSubPipeline = subPipelineRepository.saveAndFlush(subPipeline);

        // Get all the subPipelineList where name in
        defaultSubPipelineFiltering("name.in=" + DEFAULT_NAME + "," + UPDATED_NAME, "name.in=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllSubPipelinesByNameIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedSubPipeline = subPipelineRepository.saveAndFlush(subPipeline);

        // Get all the subPipelineList where name is not null
        defaultSubPipelineFiltering("name.specified=true", "name.specified=false");
    }

    @Test
    @Transactional
    void getAllSubPipelinesByNameContainsSomething() throws Exception {
        // Initialize the database
        insertedSubPipeline = subPipelineRepository.saveAndFlush(subPipeline);

        // Get all the subPipelineList where name contains
        defaultSubPipelineFiltering("name.contains=" + DEFAULT_NAME, "name.contains=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllSubPipelinesByNameNotContainsSomething() throws Exception {
        // Initialize the database
        insertedSubPipeline = subPipelineRepository.saveAndFlush(subPipeline);

        // Get all the subPipelineList where name does not contain
        defaultSubPipelineFiltering("name.doesNotContain=" + UPDATED_NAME, "name.doesNotContain=" + DEFAULT_NAME);
    }

    @Test
    @Transactional
    void getAllSubPipelinesByNameSearchIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedSubPipeline = subPipelineRepository.saveAndFlush(subPipeline);

        // Get all the subPipelineList where nameSearch equals to
        defaultSubPipelineFiltering("nameSearch.equals=" + DEFAULT_NAME_SEARCH, "nameSearch.equals=" + UPDATED_NAME_SEARCH);
    }

    @Test
    @Transactional
    void getAllSubPipelinesByNameSearchIsInShouldWork() throws Exception {
        // Initialize the database
        insertedSubPipeline = subPipelineRepository.saveAndFlush(subPipeline);

        // Get all the subPipelineList where nameSearch in
        defaultSubPipelineFiltering(
            "nameSearch.in=" + DEFAULT_NAME_SEARCH + "," + UPDATED_NAME_SEARCH,
            "nameSearch.in=" + UPDATED_NAME_SEARCH
        );
    }

    @Test
    @Transactional
    void getAllSubPipelinesByNameSearchIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedSubPipeline = subPipelineRepository.saveAndFlush(subPipeline);

        // Get all the subPipelineList where nameSearch is not null
        defaultSubPipelineFiltering("nameSearch.specified=true", "nameSearch.specified=false");
    }

    @Test
    @Transactional
    void getAllSubPipelinesByNameSearchContainsSomething() throws Exception {
        // Initialize the database
        insertedSubPipeline = subPipelineRepository.saveAndFlush(subPipeline);

        // Get all the subPipelineList where nameSearch contains
        defaultSubPipelineFiltering("nameSearch.contains=" + DEFAULT_NAME_SEARCH, "nameSearch.contains=" + UPDATED_NAME_SEARCH);
    }

    @Test
    @Transactional
    void getAllSubPipelinesByNameSearchNotContainsSomething() throws Exception {
        // Initialize the database
        insertedSubPipeline = subPipelineRepository.saveAndFlush(subPipeline);

        // Get all the subPipelineList where nameSearch does not contain
        defaultSubPipelineFiltering("nameSearch.doesNotContain=" + UPDATED_NAME_SEARCH, "nameSearch.doesNotContain=" + DEFAULT_NAME_SEARCH);
    }

    @Test
    @Transactional
    void getAllSubPipelinesBySequenceOrderIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedSubPipeline = subPipelineRepository.saveAndFlush(subPipeline);

        // Get all the subPipelineList where sequenceOrder equals to
        defaultSubPipelineFiltering("sequenceOrder.equals=" + DEFAULT_SEQUENCE_ORDER, "sequenceOrder.equals=" + UPDATED_SEQUENCE_ORDER);
    }

    @Test
    @Transactional
    void getAllSubPipelinesBySequenceOrderIsInShouldWork() throws Exception {
        // Initialize the database
        insertedSubPipeline = subPipelineRepository.saveAndFlush(subPipeline);

        // Get all the subPipelineList where sequenceOrder in
        defaultSubPipelineFiltering(
            "sequenceOrder.in=" + DEFAULT_SEQUENCE_ORDER + "," + UPDATED_SEQUENCE_ORDER,
            "sequenceOrder.in=" + UPDATED_SEQUENCE_ORDER
        );
    }

    @Test
    @Transactional
    void getAllSubPipelinesBySequenceOrderIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedSubPipeline = subPipelineRepository.saveAndFlush(subPipeline);

        // Get all the subPipelineList where sequenceOrder is not null
        defaultSubPipelineFiltering("sequenceOrder.specified=true", "sequenceOrder.specified=false");
    }

    @Test
    @Transactional
    void getAllSubPipelinesBySequenceOrderIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedSubPipeline = subPipelineRepository.saveAndFlush(subPipeline);

        // Get all the subPipelineList where sequenceOrder is greater than or equal to
        defaultSubPipelineFiltering(
            "sequenceOrder.greaterThanOrEqual=" + DEFAULT_SEQUENCE_ORDER,
            "sequenceOrder.greaterThanOrEqual=" + UPDATED_SEQUENCE_ORDER
        );
    }

    @Test
    @Transactional
    void getAllSubPipelinesBySequenceOrderIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedSubPipeline = subPipelineRepository.saveAndFlush(subPipeline);

        // Get all the subPipelineList where sequenceOrder is less than or equal to
        defaultSubPipelineFiltering(
            "sequenceOrder.lessThanOrEqual=" + DEFAULT_SEQUENCE_ORDER,
            "sequenceOrder.lessThanOrEqual=" + SMALLER_SEQUENCE_ORDER
        );
    }

    @Test
    @Transactional
    void getAllSubPipelinesBySequenceOrderIsLessThanSomething() throws Exception {
        // Initialize the database
        insertedSubPipeline = subPipelineRepository.saveAndFlush(subPipeline);

        // Get all the subPipelineList where sequenceOrder is less than
        defaultSubPipelineFiltering("sequenceOrder.lessThan=" + UPDATED_SEQUENCE_ORDER, "sequenceOrder.lessThan=" + DEFAULT_SEQUENCE_ORDER);
    }

    @Test
    @Transactional
    void getAllSubPipelinesBySequenceOrderIsGreaterThanSomething() throws Exception {
        // Initialize the database
        insertedSubPipeline = subPipelineRepository.saveAndFlush(subPipeline);

        // Get all the subPipelineList where sequenceOrder is greater than
        defaultSubPipelineFiltering(
            "sequenceOrder.greaterThan=" + SMALLER_SEQUENCE_ORDER,
            "sequenceOrder.greaterThan=" + DEFAULT_SEQUENCE_ORDER
        );
    }

    @Test
    @Transactional
    void getAllSubPipelinesByProbabilityIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedSubPipeline = subPipelineRepository.saveAndFlush(subPipeline);

        // Get all the subPipelineList where probability equals to
        defaultSubPipelineFiltering("probability.equals=" + DEFAULT_PROBABILITY, "probability.equals=" + UPDATED_PROBABILITY);
    }

    @Test
    @Transactional
    void getAllSubPipelinesByProbabilityIsInShouldWork() throws Exception {
        // Initialize the database
        insertedSubPipeline = subPipelineRepository.saveAndFlush(subPipeline);

        // Get all the subPipelineList where probability in
        defaultSubPipelineFiltering(
            "probability.in=" + DEFAULT_PROBABILITY + "," + UPDATED_PROBABILITY,
            "probability.in=" + UPDATED_PROBABILITY
        );
    }

    @Test
    @Transactional
    void getAllSubPipelinesByProbabilityIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedSubPipeline = subPipelineRepository.saveAndFlush(subPipeline);

        // Get all the subPipelineList where probability is not null
        defaultSubPipelineFiltering("probability.specified=true", "probability.specified=false");
    }

    @Test
    @Transactional
    void getAllSubPipelinesByProbabilityIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedSubPipeline = subPipelineRepository.saveAndFlush(subPipeline);

        // Get all the subPipelineList where probability is greater than or equal to
        defaultSubPipelineFiltering(
            "probability.greaterThanOrEqual=" + DEFAULT_PROBABILITY,
            "probability.greaterThanOrEqual=" + UPDATED_PROBABILITY
        );
    }

    @Test
    @Transactional
    void getAllSubPipelinesByProbabilityIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedSubPipeline = subPipelineRepository.saveAndFlush(subPipeline);

        // Get all the subPipelineList where probability is less than or equal to
        defaultSubPipelineFiltering(
            "probability.lessThanOrEqual=" + DEFAULT_PROBABILITY,
            "probability.lessThanOrEqual=" + SMALLER_PROBABILITY
        );
    }

    @Test
    @Transactional
    void getAllSubPipelinesByProbabilityIsLessThanSomething() throws Exception {
        // Initialize the database
        insertedSubPipeline = subPipelineRepository.saveAndFlush(subPipeline);

        // Get all the subPipelineList where probability is less than
        defaultSubPipelineFiltering("probability.lessThan=" + UPDATED_PROBABILITY, "probability.lessThan=" + DEFAULT_PROBABILITY);
    }

    @Test
    @Transactional
    void getAllSubPipelinesByProbabilityIsGreaterThanSomething() throws Exception {
        // Initialize the database
        insertedSubPipeline = subPipelineRepository.saveAndFlush(subPipeline);

        // Get all the subPipelineList where probability is greater than
        defaultSubPipelineFiltering("probability.greaterThan=" + SMALLER_PROBABILITY, "probability.greaterThan=" + DEFAULT_PROBABILITY);
    }

    @Test
    @Transactional
    void getAllSubPipelinesByIsClosingStageIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedSubPipeline = subPipelineRepository.saveAndFlush(subPipeline);

        // Get all the subPipelineList where isClosingStage equals to
        defaultSubPipelineFiltering(
            "isClosingStage.equals=" + DEFAULT_IS_CLOSING_STAGE,
            "isClosingStage.equals=" + UPDATED_IS_CLOSING_STAGE
        );
    }

    @Test
    @Transactional
    void getAllSubPipelinesByIsClosingStageIsInShouldWork() throws Exception {
        // Initialize the database
        insertedSubPipeline = subPipelineRepository.saveAndFlush(subPipeline);

        // Get all the subPipelineList where isClosingStage in
        defaultSubPipelineFiltering(
            "isClosingStage.in=" + DEFAULT_IS_CLOSING_STAGE + "," + UPDATED_IS_CLOSING_STAGE,
            "isClosingStage.in=" + UPDATED_IS_CLOSING_STAGE
        );
    }

    @Test
    @Transactional
    void getAllSubPipelinesByIsClosingStageIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedSubPipeline = subPipelineRepository.saveAndFlush(subPipeline);

        // Get all the subPipelineList where isClosingStage is not null
        defaultSubPipelineFiltering("isClosingStage.specified=true", "isClosingStage.specified=false");
    }

    @Test
    @Transactional
    void getAllSubPipelinesByTenantIsEqualToSomething() throws Exception {
        Tenant tenant;
        if (TestUtil.findAll(em, Tenant.class).isEmpty()) {
            subPipelineRepository.saveAndFlush(subPipeline);
            tenant = TenantResourceIT.createEntity();
        } else {
            tenant = TestUtil.findAll(em, Tenant.class).get(0);
        }
        em.persist(tenant);
        em.flush();
        subPipeline.setTenant(tenant);
        subPipelineRepository.saveAndFlush(subPipeline);
        Long tenantId = tenant.getId();
        // Get all the subPipelineList where tenant equals to tenantId
        defaultSubPipelineShouldBeFound("tenantId.equals=" + tenantId);

        // Get all the subPipelineList where tenant equals to (tenantId + 1)
        defaultSubPipelineShouldNotBeFound("tenantId.equals=" + (tenantId + 1));
    }

    @Test
    @Transactional
    void getAllSubPipelinesByPipelineIsEqualToSomething() throws Exception {
        Pipeline pipeline;
        if (TestUtil.findAll(em, Pipeline.class).isEmpty()) {
            subPipelineRepository.saveAndFlush(subPipeline);
            pipeline = PipelineResourceIT.createEntity(em);
        } else {
            pipeline = TestUtil.findAll(em, Pipeline.class).get(0);
        }
        em.persist(pipeline);
        em.flush();
        subPipeline.setPipeline(pipeline);
        subPipelineRepository.saveAndFlush(subPipeline);
        Long pipelineId = pipeline.getId();
        // Get all the subPipelineList where pipeline equals to pipelineId
        defaultSubPipelineShouldBeFound("pipelineId.equals=" + pipelineId);

        // Get all the subPipelineList where pipeline equals to (pipelineId + 1)
        defaultSubPipelineShouldNotBeFound("pipelineId.equals=" + (pipelineId + 1));
    }

    private void defaultSubPipelineFiltering(String shouldBeFound, String shouldNotBeFound) throws Exception {
        defaultSubPipelineShouldBeFound(shouldBeFound);
        defaultSubPipelineShouldNotBeFound(shouldNotBeFound);
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultSubPipelineShouldBeFound(String filter) throws Exception {
        restSubPipelineMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(subPipeline.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)))
            .andExpect(jsonPath("$.[*].nameSearch").value(hasItem(DEFAULT_NAME_SEARCH)))
            .andExpect(jsonPath("$.[*].sequenceOrder").value(hasItem(DEFAULT_SEQUENCE_ORDER)))
            .andExpect(jsonPath("$.[*].probability").value(hasItem(DEFAULT_PROBABILITY)))
            .andExpect(jsonPath("$.[*].isClosingStage").value(hasItem(DEFAULT_IS_CLOSING_STAGE)));

        // Check, that the count call also returns 1
        restSubPipelineMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultSubPipelineShouldNotBeFound(String filter) throws Exception {
        restSubPipelineMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restSubPipelineMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingSubPipeline() throws Exception {
        // Get the subPipeline
        restSubPipelineMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingSubPipeline() throws Exception {
        // Initialize the database
        insertedSubPipeline = subPipelineRepository.saveAndFlush(subPipeline);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the subPipeline
        SubPipeline updatedSubPipeline = subPipelineRepository.findById(subPipeline.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedSubPipeline are not directly saved in db
        em.detach(updatedSubPipeline);
        updatedSubPipeline
            .name(UPDATED_NAME)
            .nameSearch(UPDATED_NAME_SEARCH)
            .sequenceOrder(UPDATED_SEQUENCE_ORDER)
            .probability(UPDATED_PROBABILITY)
            .isClosingStage(UPDATED_IS_CLOSING_STAGE);
        SubPipelineDTO subPipelineDTO = subPipelineMapper.toDto(updatedSubPipeline);

        restSubPipelineMockMvc
            .perform(
                put(ENTITY_API_URL_ID, subPipelineDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(subPipelineDTO))
            )
            .andExpect(status().isOk());

        // Validate the SubPipeline in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedSubPipelineToMatchAllProperties(updatedSubPipeline);
    }

    @Test
    @Transactional
    void putNonExistingSubPipeline() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        subPipeline.setId(longCount.incrementAndGet());

        // Create the SubPipeline
        SubPipelineDTO subPipelineDTO = subPipelineMapper.toDto(subPipeline);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restSubPipelineMockMvc
            .perform(
                put(ENTITY_API_URL_ID, subPipelineDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(subPipelineDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the SubPipeline in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchSubPipeline() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        subPipeline.setId(longCount.incrementAndGet());

        // Create the SubPipeline
        SubPipelineDTO subPipelineDTO = subPipelineMapper.toDto(subPipeline);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restSubPipelineMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(subPipelineDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the SubPipeline in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamSubPipeline() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        subPipeline.setId(longCount.incrementAndGet());

        // Create the SubPipeline
        SubPipelineDTO subPipelineDTO = subPipelineMapper.toDto(subPipeline);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restSubPipelineMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(subPipelineDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the SubPipeline in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateSubPipelineWithPatch() throws Exception {
        // Initialize the database
        insertedSubPipeline = subPipelineRepository.saveAndFlush(subPipeline);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the subPipeline using partial update
        SubPipeline partialUpdatedSubPipeline = new SubPipeline();
        partialUpdatedSubPipeline.setId(subPipeline.getId());

        partialUpdatedSubPipeline.name(UPDATED_NAME).nameSearch(UPDATED_NAME_SEARCH);

        restSubPipelineMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedSubPipeline.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedSubPipeline))
            )
            .andExpect(status().isOk());

        // Validate the SubPipeline in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertSubPipelineUpdatableFieldsEquals(
            createUpdateProxyForBean(partialUpdatedSubPipeline, subPipeline),
            getPersistedSubPipeline(subPipeline)
        );
    }

    @Test
    @Transactional
    void fullUpdateSubPipelineWithPatch() throws Exception {
        // Initialize the database
        insertedSubPipeline = subPipelineRepository.saveAndFlush(subPipeline);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the subPipeline using partial update
        SubPipeline partialUpdatedSubPipeline = new SubPipeline();
        partialUpdatedSubPipeline.setId(subPipeline.getId());

        partialUpdatedSubPipeline
            .name(UPDATED_NAME)
            .nameSearch(UPDATED_NAME_SEARCH)
            .sequenceOrder(UPDATED_SEQUENCE_ORDER)
            .probability(UPDATED_PROBABILITY)
            .isClosingStage(UPDATED_IS_CLOSING_STAGE);

        restSubPipelineMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedSubPipeline.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedSubPipeline))
            )
            .andExpect(status().isOk());

        // Validate the SubPipeline in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertSubPipelineUpdatableFieldsEquals(partialUpdatedSubPipeline, getPersistedSubPipeline(partialUpdatedSubPipeline));
    }

    @Test
    @Transactional
    void patchNonExistingSubPipeline() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        subPipeline.setId(longCount.incrementAndGet());

        // Create the SubPipeline
        SubPipelineDTO subPipelineDTO = subPipelineMapper.toDto(subPipeline);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restSubPipelineMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, subPipelineDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(subPipelineDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the SubPipeline in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchSubPipeline() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        subPipeline.setId(longCount.incrementAndGet());

        // Create the SubPipeline
        SubPipelineDTO subPipelineDTO = subPipelineMapper.toDto(subPipeline);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restSubPipelineMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(subPipelineDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the SubPipeline in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamSubPipeline() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        subPipeline.setId(longCount.incrementAndGet());

        // Create the SubPipeline
        SubPipelineDTO subPipelineDTO = subPipelineMapper.toDto(subPipeline);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restSubPipelineMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(om.writeValueAsBytes(subPipelineDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the SubPipeline in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteSubPipeline() throws Exception {
        // Initialize the database
        insertedSubPipeline = subPipelineRepository.saveAndFlush(subPipeline);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the subPipeline
        restSubPipelineMockMvc
            .perform(delete(ENTITY_API_URL_ID, subPipeline.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return subPipelineRepository.count();
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

    protected SubPipeline getPersistedSubPipeline(SubPipeline subPipeline) {
        return subPipelineRepository.findById(subPipeline.getId()).orElseThrow();
    }

    protected void assertPersistedSubPipelineToMatchAllProperties(SubPipeline expectedSubPipeline) {
        assertSubPipelineAllPropertiesEquals(expectedSubPipeline, getPersistedSubPipeline(expectedSubPipeline));
    }

    protected void assertPersistedSubPipelineToMatchUpdatableProperties(SubPipeline expectedSubPipeline) {
        assertSubPipelineAllUpdatablePropertiesEquals(expectedSubPipeline, getPersistedSubPipeline(expectedSubPipeline));
    }
}
