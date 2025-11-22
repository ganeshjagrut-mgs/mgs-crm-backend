package com.crm.web.rest;

import static com.crm.web.rest.TestUtil.sameNumber;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.crm.IntegrationTest;
import com.crm.domain.Customer;
import com.crm.domain.MasterStaticType;
import com.crm.domain.Pipeline;
import com.crm.domain.PipelineTag;
import com.crm.domain.SubPipeline;
import com.crm.domain.Task;
import com.crm.domain.User;
import com.crm.repository.PipelineRepository;
import com.crm.service.dto.PipelineDTO;
import com.crm.service.mapper.PipelineMapper;
import jakarta.persistence.EntityManager;
import java.math.BigDecimal;
import java.util.List;
import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicLong;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

/**
 * Integration tests for the {@link PipelineResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class PipelineResourceIT {

    private static final String DEFAULT_PIPELINE_NAME = "AAAAAAAAAA";
    private static final String UPDATED_PIPELINE_NAME = "BBBBBBBBBB";

    private static final BigDecimal DEFAULT_TOTAL_AMOUNT = new BigDecimal(1);
    private static final BigDecimal UPDATED_TOTAL_AMOUNT = new BigDecimal(2);
    private static final BigDecimal SMALLER_TOTAL_AMOUNT = new BigDecimal(1 - 1);

    private static final Integer DEFAULT_NO_OF_SAMPLES = 1;
    private static final Integer UPDATED_NO_OF_SAMPLES = 2;
    private static final Integer SMALLER_NO_OF_SAMPLES = 1 - 1;

    private static final UUID DEFAULT_CORRELATION_ID = UUID.randomUUID();
    private static final UUID UPDATED_CORRELATION_ID = UUID.randomUUID();

    private static final String ENTITY_API_URL = "/api/pipelines";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private PipelineRepository pipelineRepository;

    @Autowired
    private PipelineMapper pipelineMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restPipelineMockMvc;

    private Pipeline pipeline;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Pipeline createEntity(EntityManager em) {
        Pipeline pipeline = new Pipeline()
            .pipelineName(DEFAULT_PIPELINE_NAME)
            .totalAmount(DEFAULT_TOTAL_AMOUNT)
            .noOfSamples(DEFAULT_NO_OF_SAMPLES)
            .correlationId(DEFAULT_CORRELATION_ID);
        return pipeline;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Pipeline createUpdatedEntity(EntityManager em) {
        Pipeline pipeline = new Pipeline()
            .pipelineName(UPDATED_PIPELINE_NAME)
            .totalAmount(UPDATED_TOTAL_AMOUNT)
            .noOfSamples(UPDATED_NO_OF_SAMPLES)
            .correlationId(UPDATED_CORRELATION_ID);
        return pipeline;
    }

    @BeforeEach
    public void initTest() {
        pipeline = createEntity(em);
    }

    @Test
    @Transactional
    void createPipeline() throws Exception {
        int databaseSizeBeforeCreate = pipelineRepository.findAll().size();
        // Create the Pipeline
        PipelineDTO pipelineDTO = pipelineMapper.toDto(pipeline);
        restPipelineMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(pipelineDTO)))
            .andExpect(status().isCreated());

        // Validate the Pipeline in the database
        List<Pipeline> pipelineList = pipelineRepository.findAll();
        assertThat(pipelineList).hasSize(databaseSizeBeforeCreate + 1);
        Pipeline testPipeline = pipelineList.get(pipelineList.size() - 1);
        assertThat(testPipeline.getPipelineName()).isEqualTo(DEFAULT_PIPELINE_NAME);
        assertThat(testPipeline.getTotalAmount()).isEqualByComparingTo(DEFAULT_TOTAL_AMOUNT);
        assertThat(testPipeline.getNoOfSamples()).isEqualTo(DEFAULT_NO_OF_SAMPLES);
        assertThat(testPipeline.getCorrelationId()).isEqualTo(DEFAULT_CORRELATION_ID);
    }

    @Test
    @Transactional
    void createPipelineWithExistingId() throws Exception {
        // Create the Pipeline with an existing ID
        pipeline.setId(1L);
        PipelineDTO pipelineDTO = pipelineMapper.toDto(pipeline);

        int databaseSizeBeforeCreate = pipelineRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restPipelineMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(pipelineDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Pipeline in the database
        List<Pipeline> pipelineList = pipelineRepository.findAll();
        assertThat(pipelineList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkPipelineNameIsRequired() throws Exception {
        int databaseSizeBeforeTest = pipelineRepository.findAll().size();
        // set the field null
        pipeline.setPipelineName(null);

        // Create the Pipeline, which fails.
        PipelineDTO pipelineDTO = pipelineMapper.toDto(pipeline);

        restPipelineMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(pipelineDTO)))
            .andExpect(status().isBadRequest());

        List<Pipeline> pipelineList = pipelineRepository.findAll();
        assertThat(pipelineList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllPipelines() throws Exception {
        // Initialize the database
        pipelineRepository.saveAndFlush(pipeline);

        // Get all the pipelineList
        restPipelineMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(pipeline.getId().intValue())))
            .andExpect(jsonPath("$.[*].pipelineName").value(hasItem(DEFAULT_PIPELINE_NAME)))
            .andExpect(jsonPath("$.[*].totalAmount").value(hasItem(sameNumber(DEFAULT_TOTAL_AMOUNT))))
            .andExpect(jsonPath("$.[*].noOfSamples").value(hasItem(DEFAULT_NO_OF_SAMPLES)))
            .andExpect(jsonPath("$.[*].correlationId").value(hasItem(DEFAULT_CORRELATION_ID.toString())));
    }

    @Test
    @Transactional
    void getPipeline() throws Exception {
        // Initialize the database
        pipelineRepository.saveAndFlush(pipeline);

        // Get the pipeline
        restPipelineMockMvc
            .perform(get(ENTITY_API_URL_ID, pipeline.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(pipeline.getId().intValue()))
            .andExpect(jsonPath("$.pipelineName").value(DEFAULT_PIPELINE_NAME))
            .andExpect(jsonPath("$.totalAmount").value(sameNumber(DEFAULT_TOTAL_AMOUNT)))
            .andExpect(jsonPath("$.noOfSamples").value(DEFAULT_NO_OF_SAMPLES))
            .andExpect(jsonPath("$.correlationId").value(DEFAULT_CORRELATION_ID.toString()));
    }

    @Test
    @Transactional
    void getPipelinesByIdFiltering() throws Exception {
        // Initialize the database
        pipelineRepository.saveAndFlush(pipeline);

        Long id = pipeline.getId();

        defaultPipelineShouldBeFound("id.equals=" + id);
        defaultPipelineShouldNotBeFound("id.notEquals=" + id);

        defaultPipelineShouldBeFound("id.greaterThanOrEqual=" + id);
        defaultPipelineShouldNotBeFound("id.greaterThan=" + id);

        defaultPipelineShouldBeFound("id.lessThanOrEqual=" + id);
        defaultPipelineShouldNotBeFound("id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllPipelinesByPipelineNameIsEqualToSomething() throws Exception {
        // Initialize the database
        pipelineRepository.saveAndFlush(pipeline);

        // Get all the pipelineList where pipelineName equals to DEFAULT_PIPELINE_NAME
        defaultPipelineShouldBeFound("pipelineName.equals=" + DEFAULT_PIPELINE_NAME);

        // Get all the pipelineList where pipelineName equals to UPDATED_PIPELINE_NAME
        defaultPipelineShouldNotBeFound("pipelineName.equals=" + UPDATED_PIPELINE_NAME);
    }

    @Test
    @Transactional
    void getAllPipelinesByPipelineNameIsInShouldWork() throws Exception {
        // Initialize the database
        pipelineRepository.saveAndFlush(pipeline);

        // Get all the pipelineList where pipelineName in DEFAULT_PIPELINE_NAME or UPDATED_PIPELINE_NAME
        defaultPipelineShouldBeFound("pipelineName.in=" + DEFAULT_PIPELINE_NAME + "," + UPDATED_PIPELINE_NAME);

        // Get all the pipelineList where pipelineName equals to UPDATED_PIPELINE_NAME
        defaultPipelineShouldNotBeFound("pipelineName.in=" + UPDATED_PIPELINE_NAME);
    }

    @Test
    @Transactional
    void getAllPipelinesByPipelineNameIsNullOrNotNull() throws Exception {
        // Initialize the database
        pipelineRepository.saveAndFlush(pipeline);

        // Get all the pipelineList where pipelineName is not null
        defaultPipelineShouldBeFound("pipelineName.specified=true");

        // Get all the pipelineList where pipelineName is null
        defaultPipelineShouldNotBeFound("pipelineName.specified=false");
    }

    @Test
    @Transactional
    void getAllPipelinesByPipelineNameContainsSomething() throws Exception {
        // Initialize the database
        pipelineRepository.saveAndFlush(pipeline);

        // Get all the pipelineList where pipelineName contains DEFAULT_PIPELINE_NAME
        defaultPipelineShouldBeFound("pipelineName.contains=" + DEFAULT_PIPELINE_NAME);

        // Get all the pipelineList where pipelineName contains UPDATED_PIPELINE_NAME
        defaultPipelineShouldNotBeFound("pipelineName.contains=" + UPDATED_PIPELINE_NAME);
    }

    @Test
    @Transactional
    void getAllPipelinesByPipelineNameNotContainsSomething() throws Exception {
        // Initialize the database
        pipelineRepository.saveAndFlush(pipeline);

        // Get all the pipelineList where pipelineName does not contain DEFAULT_PIPELINE_NAME
        defaultPipelineShouldNotBeFound("pipelineName.doesNotContain=" + DEFAULT_PIPELINE_NAME);

        // Get all the pipelineList where pipelineName does not contain UPDATED_PIPELINE_NAME
        defaultPipelineShouldBeFound("pipelineName.doesNotContain=" + UPDATED_PIPELINE_NAME);
    }

    @Test
    @Transactional
    void getAllPipelinesByTotalAmountIsEqualToSomething() throws Exception {
        // Initialize the database
        pipelineRepository.saveAndFlush(pipeline);

        // Get all the pipelineList where totalAmount equals to DEFAULT_TOTAL_AMOUNT
        defaultPipelineShouldBeFound("totalAmount.equals=" + DEFAULT_TOTAL_AMOUNT);

        // Get all the pipelineList where totalAmount equals to UPDATED_TOTAL_AMOUNT
        defaultPipelineShouldNotBeFound("totalAmount.equals=" + UPDATED_TOTAL_AMOUNT);
    }

    @Test
    @Transactional
    void getAllPipelinesByTotalAmountIsInShouldWork() throws Exception {
        // Initialize the database
        pipelineRepository.saveAndFlush(pipeline);

        // Get all the pipelineList where totalAmount in DEFAULT_TOTAL_AMOUNT or UPDATED_TOTAL_AMOUNT
        defaultPipelineShouldBeFound("totalAmount.in=" + DEFAULT_TOTAL_AMOUNT + "," + UPDATED_TOTAL_AMOUNT);

        // Get all the pipelineList where totalAmount equals to UPDATED_TOTAL_AMOUNT
        defaultPipelineShouldNotBeFound("totalAmount.in=" + UPDATED_TOTAL_AMOUNT);
    }

    @Test
    @Transactional
    void getAllPipelinesByTotalAmountIsNullOrNotNull() throws Exception {
        // Initialize the database
        pipelineRepository.saveAndFlush(pipeline);

        // Get all the pipelineList where totalAmount is not null
        defaultPipelineShouldBeFound("totalAmount.specified=true");

        // Get all the pipelineList where totalAmount is null
        defaultPipelineShouldNotBeFound("totalAmount.specified=false");
    }

    @Test
    @Transactional
    void getAllPipelinesByTotalAmountIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        pipelineRepository.saveAndFlush(pipeline);

        // Get all the pipelineList where totalAmount is greater than or equal to DEFAULT_TOTAL_AMOUNT
        defaultPipelineShouldBeFound("totalAmount.greaterThanOrEqual=" + DEFAULT_TOTAL_AMOUNT);

        // Get all the pipelineList where totalAmount is greater than or equal to UPDATED_TOTAL_AMOUNT
        defaultPipelineShouldNotBeFound("totalAmount.greaterThanOrEqual=" + UPDATED_TOTAL_AMOUNT);
    }

    @Test
    @Transactional
    void getAllPipelinesByTotalAmountIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        pipelineRepository.saveAndFlush(pipeline);

        // Get all the pipelineList where totalAmount is less than or equal to DEFAULT_TOTAL_AMOUNT
        defaultPipelineShouldBeFound("totalAmount.lessThanOrEqual=" + DEFAULT_TOTAL_AMOUNT);

        // Get all the pipelineList where totalAmount is less than or equal to SMALLER_TOTAL_AMOUNT
        defaultPipelineShouldNotBeFound("totalAmount.lessThanOrEqual=" + SMALLER_TOTAL_AMOUNT);
    }

    @Test
    @Transactional
    void getAllPipelinesByTotalAmountIsLessThanSomething() throws Exception {
        // Initialize the database
        pipelineRepository.saveAndFlush(pipeline);

        // Get all the pipelineList where totalAmount is less than DEFAULT_TOTAL_AMOUNT
        defaultPipelineShouldNotBeFound("totalAmount.lessThan=" + DEFAULT_TOTAL_AMOUNT);

        // Get all the pipelineList where totalAmount is less than UPDATED_TOTAL_AMOUNT
        defaultPipelineShouldBeFound("totalAmount.lessThan=" + UPDATED_TOTAL_AMOUNT);
    }

    @Test
    @Transactional
    void getAllPipelinesByTotalAmountIsGreaterThanSomething() throws Exception {
        // Initialize the database
        pipelineRepository.saveAndFlush(pipeline);

        // Get all the pipelineList where totalAmount is greater than DEFAULT_TOTAL_AMOUNT
        defaultPipelineShouldNotBeFound("totalAmount.greaterThan=" + DEFAULT_TOTAL_AMOUNT);

        // Get all the pipelineList where totalAmount is greater than SMALLER_TOTAL_AMOUNT
        defaultPipelineShouldBeFound("totalAmount.greaterThan=" + SMALLER_TOTAL_AMOUNT);
    }

    @Test
    @Transactional
    void getAllPipelinesByNoOfSamplesIsEqualToSomething() throws Exception {
        // Initialize the database
        pipelineRepository.saveAndFlush(pipeline);

        // Get all the pipelineList where noOfSamples equals to DEFAULT_NO_OF_SAMPLES
        defaultPipelineShouldBeFound("noOfSamples.equals=" + DEFAULT_NO_OF_SAMPLES);

        // Get all the pipelineList where noOfSamples equals to UPDATED_NO_OF_SAMPLES
        defaultPipelineShouldNotBeFound("noOfSamples.equals=" + UPDATED_NO_OF_SAMPLES);
    }

    @Test
    @Transactional
    void getAllPipelinesByNoOfSamplesIsInShouldWork() throws Exception {
        // Initialize the database
        pipelineRepository.saveAndFlush(pipeline);

        // Get all the pipelineList where noOfSamples in DEFAULT_NO_OF_SAMPLES or UPDATED_NO_OF_SAMPLES
        defaultPipelineShouldBeFound("noOfSamples.in=" + DEFAULT_NO_OF_SAMPLES + "," + UPDATED_NO_OF_SAMPLES);

        // Get all the pipelineList where noOfSamples equals to UPDATED_NO_OF_SAMPLES
        defaultPipelineShouldNotBeFound("noOfSamples.in=" + UPDATED_NO_OF_SAMPLES);
    }

    @Test
    @Transactional
    void getAllPipelinesByNoOfSamplesIsNullOrNotNull() throws Exception {
        // Initialize the database
        pipelineRepository.saveAndFlush(pipeline);

        // Get all the pipelineList where noOfSamples is not null
        defaultPipelineShouldBeFound("noOfSamples.specified=true");

        // Get all the pipelineList where noOfSamples is null
        defaultPipelineShouldNotBeFound("noOfSamples.specified=false");
    }

    @Test
    @Transactional
    void getAllPipelinesByNoOfSamplesIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        pipelineRepository.saveAndFlush(pipeline);

        // Get all the pipelineList where noOfSamples is greater than or equal to DEFAULT_NO_OF_SAMPLES
        defaultPipelineShouldBeFound("noOfSamples.greaterThanOrEqual=" + DEFAULT_NO_OF_SAMPLES);

        // Get all the pipelineList where noOfSamples is greater than or equal to UPDATED_NO_OF_SAMPLES
        defaultPipelineShouldNotBeFound("noOfSamples.greaterThanOrEqual=" + UPDATED_NO_OF_SAMPLES);
    }

    @Test
    @Transactional
    void getAllPipelinesByNoOfSamplesIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        pipelineRepository.saveAndFlush(pipeline);

        // Get all the pipelineList where noOfSamples is less than or equal to DEFAULT_NO_OF_SAMPLES
        defaultPipelineShouldBeFound("noOfSamples.lessThanOrEqual=" + DEFAULT_NO_OF_SAMPLES);

        // Get all the pipelineList where noOfSamples is less than or equal to SMALLER_NO_OF_SAMPLES
        defaultPipelineShouldNotBeFound("noOfSamples.lessThanOrEqual=" + SMALLER_NO_OF_SAMPLES);
    }

    @Test
    @Transactional
    void getAllPipelinesByNoOfSamplesIsLessThanSomething() throws Exception {
        // Initialize the database
        pipelineRepository.saveAndFlush(pipeline);

        // Get all the pipelineList where noOfSamples is less than DEFAULT_NO_OF_SAMPLES
        defaultPipelineShouldNotBeFound("noOfSamples.lessThan=" + DEFAULT_NO_OF_SAMPLES);

        // Get all the pipelineList where noOfSamples is less than UPDATED_NO_OF_SAMPLES
        defaultPipelineShouldBeFound("noOfSamples.lessThan=" + UPDATED_NO_OF_SAMPLES);
    }

    @Test
    @Transactional
    void getAllPipelinesByNoOfSamplesIsGreaterThanSomething() throws Exception {
        // Initialize the database
        pipelineRepository.saveAndFlush(pipeline);

        // Get all the pipelineList where noOfSamples is greater than DEFAULT_NO_OF_SAMPLES
        defaultPipelineShouldNotBeFound("noOfSamples.greaterThan=" + DEFAULT_NO_OF_SAMPLES);

        // Get all the pipelineList where noOfSamples is greater than SMALLER_NO_OF_SAMPLES
        defaultPipelineShouldBeFound("noOfSamples.greaterThan=" + SMALLER_NO_OF_SAMPLES);
    }

    @Test
    @Transactional
    void getAllPipelinesByCorrelationIdIsEqualToSomething() throws Exception {
        // Initialize the database
        pipelineRepository.saveAndFlush(pipeline);

        // Get all the pipelineList where correlationId equals to DEFAULT_CORRELATION_ID
        defaultPipelineShouldBeFound("correlationId.equals=" + DEFAULT_CORRELATION_ID);

        // Get all the pipelineList where correlationId equals to UPDATED_CORRELATION_ID
        defaultPipelineShouldNotBeFound("correlationId.equals=" + UPDATED_CORRELATION_ID);
    }

    @Test
    @Transactional
    void getAllPipelinesByCorrelationIdIsInShouldWork() throws Exception {
        // Initialize the database
        pipelineRepository.saveAndFlush(pipeline);

        // Get all the pipelineList where correlationId in DEFAULT_CORRELATION_ID or UPDATED_CORRELATION_ID
        defaultPipelineShouldBeFound("correlationId.in=" + DEFAULT_CORRELATION_ID + "," + UPDATED_CORRELATION_ID);

        // Get all the pipelineList where correlationId equals to UPDATED_CORRELATION_ID
        defaultPipelineShouldNotBeFound("correlationId.in=" + UPDATED_CORRELATION_ID);
    }

    @Test
    @Transactional
    void getAllPipelinesByCorrelationIdIsNullOrNotNull() throws Exception {
        // Initialize the database
        pipelineRepository.saveAndFlush(pipeline);

        // Get all the pipelineList where correlationId is not null
        defaultPipelineShouldBeFound("correlationId.specified=true");

        // Get all the pipelineList where correlationId is null
        defaultPipelineShouldNotBeFound("correlationId.specified=false");
    }

    @Test
    @Transactional
    void getAllPipelinesByPipelineTagsIsEqualToSomething() throws Exception {
        PipelineTag pipelineTags;
        if (TestUtil.findAll(em, PipelineTag.class).isEmpty()) {
            pipelineRepository.saveAndFlush(pipeline);
            pipelineTags = PipelineTagResourceIT.createEntity(em);
        } else {
            pipelineTags = TestUtil.findAll(em, PipelineTag.class).get(0);
        }
        em.persist(pipelineTags);
        em.flush();
        pipeline.addPipelineTags(pipelineTags);
        pipelineRepository.saveAndFlush(pipeline);
        Long pipelineTagsId = pipelineTags.getId();
        // Get all the pipelineList where pipelineTags equals to pipelineTagsId
        defaultPipelineShouldBeFound("pipelineTagsId.equals=" + pipelineTagsId);

        // Get all the pipelineList where pipelineTags equals to (pipelineTagsId + 1)
        defaultPipelineShouldNotBeFound("pipelineTagsId.equals=" + (pipelineTagsId + 1));
    }

    @Test
    @Transactional
    void getAllPipelinesByTasksIsEqualToSomething() throws Exception {
        Task tasks;
        if (TestUtil.findAll(em, Task.class).isEmpty()) {
            pipelineRepository.saveAndFlush(pipeline);
            tasks = TaskResourceIT.createEntity(em);
        } else {
            tasks = TestUtil.findAll(em, Task.class).get(0);
        }
        em.persist(tasks);
        em.flush();
        pipeline.addTasks(tasks);
        pipelineRepository.saveAndFlush(pipeline);
        Long tasksId = tasks.getId();
        // Get all the pipelineList where tasks equals to tasksId
        defaultPipelineShouldBeFound("tasksId.equals=" + tasksId);

        // Get all the pipelineList where tasks equals to (tasksId + 1)
        defaultPipelineShouldNotBeFound("tasksId.equals=" + (tasksId + 1));
    }

    @Test
    @Transactional
    void getAllPipelinesByPipelineOwnerIsEqualToSomething() throws Exception {
        User pipelineOwner;
        if (TestUtil.findAll(em, User.class).isEmpty()) {
            pipelineRepository.saveAndFlush(pipeline);
            pipelineOwner = UserResourceIT.createEntity(em);
        } else {
            pipelineOwner = TestUtil.findAll(em, User.class).get(0);
        }
        em.persist(pipelineOwner);
        em.flush();
        pipeline.setPipelineOwner(pipelineOwner);
        pipelineRepository.saveAndFlush(pipeline);
        Long pipelineOwnerId = pipelineOwner.getId();
        // Get all the pipelineList where pipelineOwner equals to pipelineOwnerId
        defaultPipelineShouldBeFound("pipelineOwnerId.equals=" + pipelineOwnerId);

        // Get all the pipelineList where pipelineOwner equals to (pipelineOwnerId + 1)
        defaultPipelineShouldNotBeFound("pipelineOwnerId.equals=" + (pipelineOwnerId + 1));
    }

    @Test
    @Transactional
    void getAllPipelinesByCustomerIsEqualToSomething() throws Exception {
        Customer customer;
        if (TestUtil.findAll(em, Customer.class).isEmpty()) {
            pipelineRepository.saveAndFlush(pipeline);
            customer = CustomerResourceIT.createEntity(em);
        } else {
            customer = TestUtil.findAll(em, Customer.class).get(0);
        }
        em.persist(customer);
        em.flush();
        pipeline.setCustomer(customer);
        pipelineRepository.saveAndFlush(pipeline);
        Long customerId = customer.getId();
        // Get all the pipelineList where customer equals to customerId
        defaultPipelineShouldBeFound("customerId.equals=" + customerId);

        // Get all the pipelineList where customer equals to (customerId + 1)
        defaultPipelineShouldNotBeFound("customerId.equals=" + (customerId + 1));
    }

    @Test
    @Transactional
    void getAllPipelinesByStageOfPipelineIsEqualToSomething() throws Exception {
        MasterStaticType stageOfPipeline;
        if (TestUtil.findAll(em, MasterStaticType.class).isEmpty()) {
            pipelineRepository.saveAndFlush(pipeline);
            stageOfPipeline = MasterStaticTypeResourceIT.createEntity(em);
        } else {
            stageOfPipeline = TestUtil.findAll(em, MasterStaticType.class).get(0);
        }
        em.persist(stageOfPipeline);
        em.flush();
        pipeline.setStageOfPipeline(stageOfPipeline);
        pipelineRepository.saveAndFlush(pipeline);
        Long stageOfPipelineId = stageOfPipeline.getId();
        // Get all the pipelineList where stageOfPipeline equals to stageOfPipelineId
        defaultPipelineShouldBeFound("stageOfPipelineId.equals=" + stageOfPipelineId);

        // Get all the pipelineList where stageOfPipeline equals to (stageOfPipelineId + 1)
        defaultPipelineShouldNotBeFound("stageOfPipelineId.equals=" + (stageOfPipelineId + 1));
    }

    @Test
    @Transactional
    void getAllPipelinesBySubPipelineIsEqualToSomething() throws Exception {
        SubPipeline subPipeline;
        if (TestUtil.findAll(em, SubPipeline.class).isEmpty()) {
            pipelineRepository.saveAndFlush(pipeline);
            subPipeline = SubPipelineResourceIT.createEntity(em);
        } else {
            subPipeline = TestUtil.findAll(em, SubPipeline.class).get(0);
        }
        em.persist(subPipeline);
        em.flush();
        pipeline.setSubPipeline(subPipeline);
        pipelineRepository.saveAndFlush(pipeline);
        Long subPipelineId = subPipeline.getId();
        // Get all the pipelineList where subPipeline equals to subPipelineId
        defaultPipelineShouldBeFound("subPipelineId.equals=" + subPipelineId);

        // Get all the pipelineList where subPipeline equals to (subPipelineId + 1)
        defaultPipelineShouldNotBeFound("subPipelineId.equals=" + (subPipelineId + 1));
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultPipelineShouldBeFound(String filter) throws Exception {
        restPipelineMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(pipeline.getId().intValue())))
            .andExpect(jsonPath("$.[*].pipelineName").value(hasItem(DEFAULT_PIPELINE_NAME)))
            .andExpect(jsonPath("$.[*].totalAmount").value(hasItem(sameNumber(DEFAULT_TOTAL_AMOUNT))))
            .andExpect(jsonPath("$.[*].noOfSamples").value(hasItem(DEFAULT_NO_OF_SAMPLES)))
            .andExpect(jsonPath("$.[*].correlationId").value(hasItem(DEFAULT_CORRELATION_ID.toString())));

        // Check, that the count call also returns 1
        restPipelineMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultPipelineShouldNotBeFound(String filter) throws Exception {
        restPipelineMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restPipelineMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingPipeline() throws Exception {
        // Get the pipeline
        restPipelineMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingPipeline() throws Exception {
        // Initialize the database
        pipelineRepository.saveAndFlush(pipeline);

        int databaseSizeBeforeUpdate = pipelineRepository.findAll().size();

        // Update the pipeline
        Pipeline updatedPipeline = pipelineRepository.findById(pipeline.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedPipeline are not directly saved in db
        em.detach(updatedPipeline);
        updatedPipeline
            .pipelineName(UPDATED_PIPELINE_NAME)
            .totalAmount(UPDATED_TOTAL_AMOUNT)
            .noOfSamples(UPDATED_NO_OF_SAMPLES)
            .correlationId(UPDATED_CORRELATION_ID);
        PipelineDTO pipelineDTO = pipelineMapper.toDto(updatedPipeline);

        restPipelineMockMvc
            .perform(
                put(ENTITY_API_URL_ID, pipelineDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(pipelineDTO))
            )
            .andExpect(status().isOk());

        // Validate the Pipeline in the database
        List<Pipeline> pipelineList = pipelineRepository.findAll();
        assertThat(pipelineList).hasSize(databaseSizeBeforeUpdate);
        Pipeline testPipeline = pipelineList.get(pipelineList.size() - 1);
        assertThat(testPipeline.getPipelineName()).isEqualTo(UPDATED_PIPELINE_NAME);
        assertThat(testPipeline.getTotalAmount()).isEqualByComparingTo(UPDATED_TOTAL_AMOUNT);
        assertThat(testPipeline.getNoOfSamples()).isEqualTo(UPDATED_NO_OF_SAMPLES);
        assertThat(testPipeline.getCorrelationId()).isEqualTo(UPDATED_CORRELATION_ID);
    }

    @Test
    @Transactional
    void putNonExistingPipeline() throws Exception {
        int databaseSizeBeforeUpdate = pipelineRepository.findAll().size();
        pipeline.setId(longCount.incrementAndGet());

        // Create the Pipeline
        PipelineDTO pipelineDTO = pipelineMapper.toDto(pipeline);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restPipelineMockMvc
            .perform(
                put(ENTITY_API_URL_ID, pipelineDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(pipelineDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Pipeline in the database
        List<Pipeline> pipelineList = pipelineRepository.findAll();
        assertThat(pipelineList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchPipeline() throws Exception {
        int databaseSizeBeforeUpdate = pipelineRepository.findAll().size();
        pipeline.setId(longCount.incrementAndGet());

        // Create the Pipeline
        PipelineDTO pipelineDTO = pipelineMapper.toDto(pipeline);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restPipelineMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(pipelineDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Pipeline in the database
        List<Pipeline> pipelineList = pipelineRepository.findAll();
        assertThat(pipelineList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamPipeline() throws Exception {
        int databaseSizeBeforeUpdate = pipelineRepository.findAll().size();
        pipeline.setId(longCount.incrementAndGet());

        // Create the Pipeline
        PipelineDTO pipelineDTO = pipelineMapper.toDto(pipeline);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restPipelineMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(pipelineDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Pipeline in the database
        List<Pipeline> pipelineList = pipelineRepository.findAll();
        assertThat(pipelineList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdatePipelineWithPatch() throws Exception {
        // Initialize the database
        pipelineRepository.saveAndFlush(pipeline);

        int databaseSizeBeforeUpdate = pipelineRepository.findAll().size();

        // Update the pipeline using partial update
        Pipeline partialUpdatedPipeline = new Pipeline();
        partialUpdatedPipeline.setId(pipeline.getId());

        partialUpdatedPipeline.pipelineName(UPDATED_PIPELINE_NAME).correlationId(UPDATED_CORRELATION_ID);

        restPipelineMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedPipeline.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedPipeline))
            )
            .andExpect(status().isOk());

        // Validate the Pipeline in the database
        List<Pipeline> pipelineList = pipelineRepository.findAll();
        assertThat(pipelineList).hasSize(databaseSizeBeforeUpdate);
        Pipeline testPipeline = pipelineList.get(pipelineList.size() - 1);
        assertThat(testPipeline.getPipelineName()).isEqualTo(UPDATED_PIPELINE_NAME);
        assertThat(testPipeline.getTotalAmount()).isEqualByComparingTo(DEFAULT_TOTAL_AMOUNT);
        assertThat(testPipeline.getNoOfSamples()).isEqualTo(DEFAULT_NO_OF_SAMPLES);
        assertThat(testPipeline.getCorrelationId()).isEqualTo(UPDATED_CORRELATION_ID);
    }

    @Test
    @Transactional
    void fullUpdatePipelineWithPatch() throws Exception {
        // Initialize the database
        pipelineRepository.saveAndFlush(pipeline);

        int databaseSizeBeforeUpdate = pipelineRepository.findAll().size();

        // Update the pipeline using partial update
        Pipeline partialUpdatedPipeline = new Pipeline();
        partialUpdatedPipeline.setId(pipeline.getId());

        partialUpdatedPipeline
            .pipelineName(UPDATED_PIPELINE_NAME)
            .totalAmount(UPDATED_TOTAL_AMOUNT)
            .noOfSamples(UPDATED_NO_OF_SAMPLES)
            .correlationId(UPDATED_CORRELATION_ID);

        restPipelineMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedPipeline.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedPipeline))
            )
            .andExpect(status().isOk());

        // Validate the Pipeline in the database
        List<Pipeline> pipelineList = pipelineRepository.findAll();
        assertThat(pipelineList).hasSize(databaseSizeBeforeUpdate);
        Pipeline testPipeline = pipelineList.get(pipelineList.size() - 1);
        assertThat(testPipeline.getPipelineName()).isEqualTo(UPDATED_PIPELINE_NAME);
        assertThat(testPipeline.getTotalAmount()).isEqualByComparingTo(UPDATED_TOTAL_AMOUNT);
        assertThat(testPipeline.getNoOfSamples()).isEqualTo(UPDATED_NO_OF_SAMPLES);
        assertThat(testPipeline.getCorrelationId()).isEqualTo(UPDATED_CORRELATION_ID);
    }

    @Test
    @Transactional
    void patchNonExistingPipeline() throws Exception {
        int databaseSizeBeforeUpdate = pipelineRepository.findAll().size();
        pipeline.setId(longCount.incrementAndGet());

        // Create the Pipeline
        PipelineDTO pipelineDTO = pipelineMapper.toDto(pipeline);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restPipelineMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, pipelineDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(pipelineDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Pipeline in the database
        List<Pipeline> pipelineList = pipelineRepository.findAll();
        assertThat(pipelineList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchPipeline() throws Exception {
        int databaseSizeBeforeUpdate = pipelineRepository.findAll().size();
        pipeline.setId(longCount.incrementAndGet());

        // Create the Pipeline
        PipelineDTO pipelineDTO = pipelineMapper.toDto(pipeline);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restPipelineMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(pipelineDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Pipeline in the database
        List<Pipeline> pipelineList = pipelineRepository.findAll();
        assertThat(pipelineList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamPipeline() throws Exception {
        int databaseSizeBeforeUpdate = pipelineRepository.findAll().size();
        pipeline.setId(longCount.incrementAndGet());

        // Create the Pipeline
        PipelineDTO pipelineDTO = pipelineMapper.toDto(pipeline);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restPipelineMockMvc
            .perform(
                patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(pipelineDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the Pipeline in the database
        List<Pipeline> pipelineList = pipelineRepository.findAll();
        assertThat(pipelineList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deletePipeline() throws Exception {
        // Initialize the database
        pipelineRepository.saveAndFlush(pipeline);

        int databaseSizeBeforeDelete = pipelineRepository.findAll().size();

        // Delete the pipeline
        restPipelineMockMvc
            .perform(delete(ENTITY_API_URL_ID, pipeline.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Pipeline> pipelineList = pipelineRepository.findAll();
        assertThat(pipelineList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
