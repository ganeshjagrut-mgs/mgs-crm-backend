package com.crm.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.crm.IntegrationTest;
import com.crm.domain.Customer;
import com.crm.domain.MasterStaticType;
import com.crm.domain.Pipeline;
import com.crm.domain.Task;
import com.crm.domain.User;
import com.crm.domain.enumeration.Status;
import com.crm.domain.enumeration.TaskType;
import com.crm.repository.TaskRepository;
import com.crm.service.dto.TaskDTO;
import com.crm.service.mapper.TaskMapper;
import jakarta.persistence.EntityManager;
import java.time.LocalDate;
import java.time.ZoneId;
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
 * Integration tests for the {@link TaskResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class TaskResourceIT {

    private static final TaskType DEFAULT_TASK_TYPE = TaskType.CALL;
    private static final TaskType UPDATED_TASK_TYPE = TaskType.MEETING;

    private static final LocalDate DEFAULT_DUE_DATE = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_DUE_DATE = LocalDate.now(ZoneId.systemDefault());
    private static final LocalDate SMALLER_DUE_DATE = LocalDate.ofEpochDay(-1L);

    private static final String DEFAULT_TASK_NAME = "AAAAAAAAAA";
    private static final String UPDATED_TASK_NAME = "BBBBBBBBBB";

    private static final Status DEFAULT_STATUS = Status.PENDING;
    private static final Status UPDATED_STATUS = Status.IN_PROGRESS;

    private static final LocalDate DEFAULT_TASK_COMPLETION_DATE = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_TASK_COMPLETION_DATE = LocalDate.now(ZoneId.systemDefault());
    private static final LocalDate SMALLER_TASK_COMPLETION_DATE = LocalDate.ofEpochDay(-1L);

    private static final UUID DEFAULT_CORRELATION_ID = UUID.randomUUID();
    private static final UUID UPDATED_CORRELATION_ID = UUID.randomUUID();

    private static final String ENTITY_API_URL = "/api/tasks";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private TaskRepository taskRepository;

    @Autowired
    private TaskMapper taskMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restTaskMockMvc;

    private Task task;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Task createEntity(EntityManager em) {
        Task task = new Task()
            .taskType(DEFAULT_TASK_TYPE)
            .dueDate(DEFAULT_DUE_DATE)
            .taskName(DEFAULT_TASK_NAME)
            .status(DEFAULT_STATUS)
            .taskCompletionDate(DEFAULT_TASK_COMPLETION_DATE)
            .correlationId(DEFAULT_CORRELATION_ID);
        return task;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Task createUpdatedEntity(EntityManager em) {
        Task task = new Task()
            .taskType(UPDATED_TASK_TYPE)
            .dueDate(UPDATED_DUE_DATE)
            .taskName(UPDATED_TASK_NAME)
            .status(UPDATED_STATUS)
            .taskCompletionDate(UPDATED_TASK_COMPLETION_DATE)
            .correlationId(UPDATED_CORRELATION_ID);
        return task;
    }

    @BeforeEach
    public void initTest() {
        task = createEntity(em);
    }

    @Test
    @Transactional
    void createTask() throws Exception {
        int databaseSizeBeforeCreate = taskRepository.findAll().size();
        // Create the Task
        TaskDTO taskDTO = taskMapper.toDto(task);
        restTaskMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(taskDTO)))
            .andExpect(status().isCreated());

        // Validate the Task in the database
        List<Task> taskList = taskRepository.findAll();
        assertThat(taskList).hasSize(databaseSizeBeforeCreate + 1);
        Task testTask = taskList.get(taskList.size() - 1);
        assertThat(testTask.getTaskType()).isEqualTo(DEFAULT_TASK_TYPE);
        assertThat(testTask.getDueDate()).isEqualTo(DEFAULT_DUE_DATE);
        assertThat(testTask.getTaskName()).isEqualTo(DEFAULT_TASK_NAME);
        assertThat(testTask.getStatus()).isEqualTo(DEFAULT_STATUS);
        assertThat(testTask.getTaskCompletionDate()).isEqualTo(DEFAULT_TASK_COMPLETION_DATE);
        assertThat(testTask.getCorrelationId()).isEqualTo(DEFAULT_CORRELATION_ID);
    }

    @Test
    @Transactional
    void createTaskWithExistingId() throws Exception {
        // Create the Task with an existing ID
        task.setId(1L);
        TaskDTO taskDTO = taskMapper.toDto(task);

        int databaseSizeBeforeCreate = taskRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restTaskMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(taskDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Task in the database
        List<Task> taskList = taskRepository.findAll();
        assertThat(taskList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkTaskTypeIsRequired() throws Exception {
        int databaseSizeBeforeTest = taskRepository.findAll().size();
        // set the field null
        task.setTaskType(null);

        // Create the Task, which fails.
        TaskDTO taskDTO = taskMapper.toDto(task);

        restTaskMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(taskDTO)))
            .andExpect(status().isBadRequest());

        List<Task> taskList = taskRepository.findAll();
        assertThat(taskList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkDueDateIsRequired() throws Exception {
        int databaseSizeBeforeTest = taskRepository.findAll().size();
        // set the field null
        task.setDueDate(null);

        // Create the Task, which fails.
        TaskDTO taskDTO = taskMapper.toDto(task);

        restTaskMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(taskDTO)))
            .andExpect(status().isBadRequest());

        List<Task> taskList = taskRepository.findAll();
        assertThat(taskList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkTaskNameIsRequired() throws Exception {
        int databaseSizeBeforeTest = taskRepository.findAll().size();
        // set the field null
        task.setTaskName(null);

        // Create the Task, which fails.
        TaskDTO taskDTO = taskMapper.toDto(task);

        restTaskMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(taskDTO)))
            .andExpect(status().isBadRequest());

        List<Task> taskList = taskRepository.findAll();
        assertThat(taskList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkStatusIsRequired() throws Exception {
        int databaseSizeBeforeTest = taskRepository.findAll().size();
        // set the field null
        task.setStatus(null);

        // Create the Task, which fails.
        TaskDTO taskDTO = taskMapper.toDto(task);

        restTaskMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(taskDTO)))
            .andExpect(status().isBadRequest());

        List<Task> taskList = taskRepository.findAll();
        assertThat(taskList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllTasks() throws Exception {
        // Initialize the database
        taskRepository.saveAndFlush(task);

        // Get all the taskList
        restTaskMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(task.getId().intValue())))
            .andExpect(jsonPath("$.[*].taskType").value(hasItem(DEFAULT_TASK_TYPE.toString())))
            .andExpect(jsonPath("$.[*].dueDate").value(hasItem(DEFAULT_DUE_DATE.toString())))
            .andExpect(jsonPath("$.[*].taskName").value(hasItem(DEFAULT_TASK_NAME)))
            .andExpect(jsonPath("$.[*].status").value(hasItem(DEFAULT_STATUS.toString())))
            .andExpect(jsonPath("$.[*].taskCompletionDate").value(hasItem(DEFAULT_TASK_COMPLETION_DATE.toString())))
            .andExpect(jsonPath("$.[*].correlationId").value(hasItem(DEFAULT_CORRELATION_ID.toString())));
    }

    @Test
    @Transactional
    void getTask() throws Exception {
        // Initialize the database
        taskRepository.saveAndFlush(task);

        // Get the task
        restTaskMockMvc
            .perform(get(ENTITY_API_URL_ID, task.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(task.getId().intValue()))
            .andExpect(jsonPath("$.taskType").value(DEFAULT_TASK_TYPE.toString()))
            .andExpect(jsonPath("$.dueDate").value(DEFAULT_DUE_DATE.toString()))
            .andExpect(jsonPath("$.taskName").value(DEFAULT_TASK_NAME))
            .andExpect(jsonPath("$.status").value(DEFAULT_STATUS.toString()))
            .andExpect(jsonPath("$.taskCompletionDate").value(DEFAULT_TASK_COMPLETION_DATE.toString()))
            .andExpect(jsonPath("$.correlationId").value(DEFAULT_CORRELATION_ID.toString()));
    }

    @Test
    @Transactional
    void getTasksByIdFiltering() throws Exception {
        // Initialize the database
        taskRepository.saveAndFlush(task);

        Long id = task.getId();

        defaultTaskShouldBeFound("id.equals=" + id);
        defaultTaskShouldNotBeFound("id.notEquals=" + id);

        defaultTaskShouldBeFound("id.greaterThanOrEqual=" + id);
        defaultTaskShouldNotBeFound("id.greaterThan=" + id);

        defaultTaskShouldBeFound("id.lessThanOrEqual=" + id);
        defaultTaskShouldNotBeFound("id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllTasksByTaskTypeIsEqualToSomething() throws Exception {
        // Initialize the database
        taskRepository.saveAndFlush(task);

        // Get all the taskList where taskType equals to DEFAULT_TASK_TYPE
        defaultTaskShouldBeFound("taskType.equals=" + DEFAULT_TASK_TYPE);

        // Get all the taskList where taskType equals to UPDATED_TASK_TYPE
        defaultTaskShouldNotBeFound("taskType.equals=" + UPDATED_TASK_TYPE);
    }

    @Test
    @Transactional
    void getAllTasksByTaskTypeIsInShouldWork() throws Exception {
        // Initialize the database
        taskRepository.saveAndFlush(task);

        // Get all the taskList where taskType in DEFAULT_TASK_TYPE or UPDATED_TASK_TYPE
        defaultTaskShouldBeFound("taskType.in=" + DEFAULT_TASK_TYPE + "," + UPDATED_TASK_TYPE);

        // Get all the taskList where taskType equals to UPDATED_TASK_TYPE
        defaultTaskShouldNotBeFound("taskType.in=" + UPDATED_TASK_TYPE);
    }

    @Test
    @Transactional
    void getAllTasksByTaskTypeIsNullOrNotNull() throws Exception {
        // Initialize the database
        taskRepository.saveAndFlush(task);

        // Get all the taskList where taskType is not null
        defaultTaskShouldBeFound("taskType.specified=true");

        // Get all the taskList where taskType is null
        defaultTaskShouldNotBeFound("taskType.specified=false");
    }

    @Test
    @Transactional
    void getAllTasksByDueDateIsEqualToSomething() throws Exception {
        // Initialize the database
        taskRepository.saveAndFlush(task);

        // Get all the taskList where dueDate equals to DEFAULT_DUE_DATE
        defaultTaskShouldBeFound("dueDate.equals=" + DEFAULT_DUE_DATE);

        // Get all the taskList where dueDate equals to UPDATED_DUE_DATE
        defaultTaskShouldNotBeFound("dueDate.equals=" + UPDATED_DUE_DATE);
    }

    @Test
    @Transactional
    void getAllTasksByDueDateIsInShouldWork() throws Exception {
        // Initialize the database
        taskRepository.saveAndFlush(task);

        // Get all the taskList where dueDate in DEFAULT_DUE_DATE or UPDATED_DUE_DATE
        defaultTaskShouldBeFound("dueDate.in=" + DEFAULT_DUE_DATE + "," + UPDATED_DUE_DATE);

        // Get all the taskList where dueDate equals to UPDATED_DUE_DATE
        defaultTaskShouldNotBeFound("dueDate.in=" + UPDATED_DUE_DATE);
    }

    @Test
    @Transactional
    void getAllTasksByDueDateIsNullOrNotNull() throws Exception {
        // Initialize the database
        taskRepository.saveAndFlush(task);

        // Get all the taskList where dueDate is not null
        defaultTaskShouldBeFound("dueDate.specified=true");

        // Get all the taskList where dueDate is null
        defaultTaskShouldNotBeFound("dueDate.specified=false");
    }

    @Test
    @Transactional
    void getAllTasksByDueDateIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        taskRepository.saveAndFlush(task);

        // Get all the taskList where dueDate is greater than or equal to DEFAULT_DUE_DATE
        defaultTaskShouldBeFound("dueDate.greaterThanOrEqual=" + DEFAULT_DUE_DATE);

        // Get all the taskList where dueDate is greater than or equal to UPDATED_DUE_DATE
        defaultTaskShouldNotBeFound("dueDate.greaterThanOrEqual=" + UPDATED_DUE_DATE);
    }

    @Test
    @Transactional
    void getAllTasksByDueDateIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        taskRepository.saveAndFlush(task);

        // Get all the taskList where dueDate is less than or equal to DEFAULT_DUE_DATE
        defaultTaskShouldBeFound("dueDate.lessThanOrEqual=" + DEFAULT_DUE_DATE);

        // Get all the taskList where dueDate is less than or equal to SMALLER_DUE_DATE
        defaultTaskShouldNotBeFound("dueDate.lessThanOrEqual=" + SMALLER_DUE_DATE);
    }

    @Test
    @Transactional
    void getAllTasksByDueDateIsLessThanSomething() throws Exception {
        // Initialize the database
        taskRepository.saveAndFlush(task);

        // Get all the taskList where dueDate is less than DEFAULT_DUE_DATE
        defaultTaskShouldNotBeFound("dueDate.lessThan=" + DEFAULT_DUE_DATE);

        // Get all the taskList where dueDate is less than UPDATED_DUE_DATE
        defaultTaskShouldBeFound("dueDate.lessThan=" + UPDATED_DUE_DATE);
    }

    @Test
    @Transactional
    void getAllTasksByDueDateIsGreaterThanSomething() throws Exception {
        // Initialize the database
        taskRepository.saveAndFlush(task);

        // Get all the taskList where dueDate is greater than DEFAULT_DUE_DATE
        defaultTaskShouldNotBeFound("dueDate.greaterThan=" + DEFAULT_DUE_DATE);

        // Get all the taskList where dueDate is greater than SMALLER_DUE_DATE
        defaultTaskShouldBeFound("dueDate.greaterThan=" + SMALLER_DUE_DATE);
    }

    @Test
    @Transactional
    void getAllTasksByTaskNameIsEqualToSomething() throws Exception {
        // Initialize the database
        taskRepository.saveAndFlush(task);

        // Get all the taskList where taskName equals to DEFAULT_TASK_NAME
        defaultTaskShouldBeFound("taskName.equals=" + DEFAULT_TASK_NAME);

        // Get all the taskList where taskName equals to UPDATED_TASK_NAME
        defaultTaskShouldNotBeFound("taskName.equals=" + UPDATED_TASK_NAME);
    }

    @Test
    @Transactional
    void getAllTasksByTaskNameIsInShouldWork() throws Exception {
        // Initialize the database
        taskRepository.saveAndFlush(task);

        // Get all the taskList where taskName in DEFAULT_TASK_NAME or UPDATED_TASK_NAME
        defaultTaskShouldBeFound("taskName.in=" + DEFAULT_TASK_NAME + "," + UPDATED_TASK_NAME);

        // Get all the taskList where taskName equals to UPDATED_TASK_NAME
        defaultTaskShouldNotBeFound("taskName.in=" + UPDATED_TASK_NAME);
    }

    @Test
    @Transactional
    void getAllTasksByTaskNameIsNullOrNotNull() throws Exception {
        // Initialize the database
        taskRepository.saveAndFlush(task);

        // Get all the taskList where taskName is not null
        defaultTaskShouldBeFound("taskName.specified=true");

        // Get all the taskList where taskName is null
        defaultTaskShouldNotBeFound("taskName.specified=false");
    }

    @Test
    @Transactional
    void getAllTasksByTaskNameContainsSomething() throws Exception {
        // Initialize the database
        taskRepository.saveAndFlush(task);

        // Get all the taskList where taskName contains DEFAULT_TASK_NAME
        defaultTaskShouldBeFound("taskName.contains=" + DEFAULT_TASK_NAME);

        // Get all the taskList where taskName contains UPDATED_TASK_NAME
        defaultTaskShouldNotBeFound("taskName.contains=" + UPDATED_TASK_NAME);
    }

    @Test
    @Transactional
    void getAllTasksByTaskNameNotContainsSomething() throws Exception {
        // Initialize the database
        taskRepository.saveAndFlush(task);

        // Get all the taskList where taskName does not contain DEFAULT_TASK_NAME
        defaultTaskShouldNotBeFound("taskName.doesNotContain=" + DEFAULT_TASK_NAME);

        // Get all the taskList where taskName does not contain UPDATED_TASK_NAME
        defaultTaskShouldBeFound("taskName.doesNotContain=" + UPDATED_TASK_NAME);
    }

    @Test
    @Transactional
    void getAllTasksByStatusIsEqualToSomething() throws Exception {
        // Initialize the database
        taskRepository.saveAndFlush(task);

        // Get all the taskList where status equals to DEFAULT_STATUS
        defaultTaskShouldBeFound("status.equals=" + DEFAULT_STATUS);

        // Get all the taskList where status equals to UPDATED_STATUS
        defaultTaskShouldNotBeFound("status.equals=" + UPDATED_STATUS);
    }

    @Test
    @Transactional
    void getAllTasksByStatusIsInShouldWork() throws Exception {
        // Initialize the database
        taskRepository.saveAndFlush(task);

        // Get all the taskList where status in DEFAULT_STATUS or UPDATED_STATUS
        defaultTaskShouldBeFound("status.in=" + DEFAULT_STATUS + "," + UPDATED_STATUS);

        // Get all the taskList where status equals to UPDATED_STATUS
        defaultTaskShouldNotBeFound("status.in=" + UPDATED_STATUS);
    }

    @Test
    @Transactional
    void getAllTasksByStatusIsNullOrNotNull() throws Exception {
        // Initialize the database
        taskRepository.saveAndFlush(task);

        // Get all the taskList where status is not null
        defaultTaskShouldBeFound("status.specified=true");

        // Get all the taskList where status is null
        defaultTaskShouldNotBeFound("status.specified=false");
    }

    @Test
    @Transactional
    void getAllTasksByTaskCompletionDateIsEqualToSomething() throws Exception {
        // Initialize the database
        taskRepository.saveAndFlush(task);

        // Get all the taskList where taskCompletionDate equals to DEFAULT_TASK_COMPLETION_DATE
        defaultTaskShouldBeFound("taskCompletionDate.equals=" + DEFAULT_TASK_COMPLETION_DATE);

        // Get all the taskList where taskCompletionDate equals to UPDATED_TASK_COMPLETION_DATE
        defaultTaskShouldNotBeFound("taskCompletionDate.equals=" + UPDATED_TASK_COMPLETION_DATE);
    }

    @Test
    @Transactional
    void getAllTasksByTaskCompletionDateIsInShouldWork() throws Exception {
        // Initialize the database
        taskRepository.saveAndFlush(task);

        // Get all the taskList where taskCompletionDate in DEFAULT_TASK_COMPLETION_DATE or UPDATED_TASK_COMPLETION_DATE
        defaultTaskShouldBeFound("taskCompletionDate.in=" + DEFAULT_TASK_COMPLETION_DATE + "," + UPDATED_TASK_COMPLETION_DATE);

        // Get all the taskList where taskCompletionDate equals to UPDATED_TASK_COMPLETION_DATE
        defaultTaskShouldNotBeFound("taskCompletionDate.in=" + UPDATED_TASK_COMPLETION_DATE);
    }

    @Test
    @Transactional
    void getAllTasksByTaskCompletionDateIsNullOrNotNull() throws Exception {
        // Initialize the database
        taskRepository.saveAndFlush(task);

        // Get all the taskList where taskCompletionDate is not null
        defaultTaskShouldBeFound("taskCompletionDate.specified=true");

        // Get all the taskList where taskCompletionDate is null
        defaultTaskShouldNotBeFound("taskCompletionDate.specified=false");
    }

    @Test
    @Transactional
    void getAllTasksByTaskCompletionDateIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        taskRepository.saveAndFlush(task);

        // Get all the taskList where taskCompletionDate is greater than or equal to DEFAULT_TASK_COMPLETION_DATE
        defaultTaskShouldBeFound("taskCompletionDate.greaterThanOrEqual=" + DEFAULT_TASK_COMPLETION_DATE);

        // Get all the taskList where taskCompletionDate is greater than or equal to UPDATED_TASK_COMPLETION_DATE
        defaultTaskShouldNotBeFound("taskCompletionDate.greaterThanOrEqual=" + UPDATED_TASK_COMPLETION_DATE);
    }

    @Test
    @Transactional
    void getAllTasksByTaskCompletionDateIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        taskRepository.saveAndFlush(task);

        // Get all the taskList where taskCompletionDate is less than or equal to DEFAULT_TASK_COMPLETION_DATE
        defaultTaskShouldBeFound("taskCompletionDate.lessThanOrEqual=" + DEFAULT_TASK_COMPLETION_DATE);

        // Get all the taskList where taskCompletionDate is less than or equal to SMALLER_TASK_COMPLETION_DATE
        defaultTaskShouldNotBeFound("taskCompletionDate.lessThanOrEqual=" + SMALLER_TASK_COMPLETION_DATE);
    }

    @Test
    @Transactional
    void getAllTasksByTaskCompletionDateIsLessThanSomething() throws Exception {
        // Initialize the database
        taskRepository.saveAndFlush(task);

        // Get all the taskList where taskCompletionDate is less than DEFAULT_TASK_COMPLETION_DATE
        defaultTaskShouldNotBeFound("taskCompletionDate.lessThan=" + DEFAULT_TASK_COMPLETION_DATE);

        // Get all the taskList where taskCompletionDate is less than UPDATED_TASK_COMPLETION_DATE
        defaultTaskShouldBeFound("taskCompletionDate.lessThan=" + UPDATED_TASK_COMPLETION_DATE);
    }

    @Test
    @Transactional
    void getAllTasksByTaskCompletionDateIsGreaterThanSomething() throws Exception {
        // Initialize the database
        taskRepository.saveAndFlush(task);

        // Get all the taskList where taskCompletionDate is greater than DEFAULT_TASK_COMPLETION_DATE
        defaultTaskShouldNotBeFound("taskCompletionDate.greaterThan=" + DEFAULT_TASK_COMPLETION_DATE);

        // Get all the taskList where taskCompletionDate is greater than SMALLER_TASK_COMPLETION_DATE
        defaultTaskShouldBeFound("taskCompletionDate.greaterThan=" + SMALLER_TASK_COMPLETION_DATE);
    }

    @Test
    @Transactional
    void getAllTasksByCorrelationIdIsEqualToSomething() throws Exception {
        // Initialize the database
        taskRepository.saveAndFlush(task);

        // Get all the taskList where correlationId equals to DEFAULT_CORRELATION_ID
        defaultTaskShouldBeFound("correlationId.equals=" + DEFAULT_CORRELATION_ID);

        // Get all the taskList where correlationId equals to UPDATED_CORRELATION_ID
        defaultTaskShouldNotBeFound("correlationId.equals=" + UPDATED_CORRELATION_ID);
    }

    @Test
    @Transactional
    void getAllTasksByCorrelationIdIsInShouldWork() throws Exception {
        // Initialize the database
        taskRepository.saveAndFlush(task);

        // Get all the taskList where correlationId in DEFAULT_CORRELATION_ID or UPDATED_CORRELATION_ID
        defaultTaskShouldBeFound("correlationId.in=" + DEFAULT_CORRELATION_ID + "," + UPDATED_CORRELATION_ID);

        // Get all the taskList where correlationId equals to UPDATED_CORRELATION_ID
        defaultTaskShouldNotBeFound("correlationId.in=" + UPDATED_CORRELATION_ID);
    }

    @Test
    @Transactional
    void getAllTasksByCorrelationIdIsNullOrNotNull() throws Exception {
        // Initialize the database
        taskRepository.saveAndFlush(task);

        // Get all the taskList where correlationId is not null
        defaultTaskShouldBeFound("correlationId.specified=true");

        // Get all the taskList where correlationId is null
        defaultTaskShouldNotBeFound("correlationId.specified=false");
    }

    @Test
    @Transactional
    void getAllTasksByTaskOwnerIsEqualToSomething() throws Exception {
        User taskOwner;
        if (TestUtil.findAll(em, User.class).isEmpty()) {
            taskRepository.saveAndFlush(task);
            taskOwner = UserResourceIT.createEntity(em);
        } else {
            taskOwner = TestUtil.findAll(em, User.class).get(0);
        }
        em.persist(taskOwner);
        em.flush();
        task.setTaskOwner(taskOwner);
        taskRepository.saveAndFlush(task);
        Long taskOwnerId = taskOwner.getId();
        // Get all the taskList where taskOwner equals to taskOwnerId
        defaultTaskShouldBeFound("taskOwnerId.equals=" + taskOwnerId);

        // Get all the taskList where taskOwner equals to (taskOwnerId + 1)
        defaultTaskShouldNotBeFound("taskOwnerId.equals=" + (taskOwnerId + 1));
    }

    @Test
    @Transactional
    void getAllTasksByCustomerIsEqualToSomething() throws Exception {
        Customer customer;
        if (TestUtil.findAll(em, Customer.class).isEmpty()) {
            taskRepository.saveAndFlush(task);
            customer = CustomerResourceIT.createEntity(em);
        } else {
            customer = TestUtil.findAll(em, Customer.class).get(0);
        }
        em.persist(customer);
        em.flush();
        task.setCustomer(customer);
        taskRepository.saveAndFlush(task);
        Long customerId = customer.getId();
        // Get all the taskList where customer equals to customerId
        defaultTaskShouldBeFound("customerId.equals=" + customerId);

        // Get all the taskList where customer equals to (customerId + 1)
        defaultTaskShouldNotBeFound("customerId.equals=" + (customerId + 1));
    }

    @Test
    @Transactional
    void getAllTasksByRelatedToIsEqualToSomething() throws Exception {
        MasterStaticType relatedTo;
        if (TestUtil.findAll(em, MasterStaticType.class).isEmpty()) {
            taskRepository.saveAndFlush(task);
            relatedTo = MasterStaticTypeResourceIT.createEntity(em);
        } else {
            relatedTo = TestUtil.findAll(em, MasterStaticType.class).get(0);
        }
        em.persist(relatedTo);
        em.flush();
        task.setRelatedTo(relatedTo);
        taskRepository.saveAndFlush(task);
        Long relatedToId = relatedTo.getId();
        // Get all the taskList where relatedTo equals to relatedToId
        defaultTaskShouldBeFound("relatedToId.equals=" + relatedToId);

        // Get all the taskList where relatedTo equals to (relatedToId + 1)
        defaultTaskShouldNotBeFound("relatedToId.equals=" + (relatedToId + 1));
    }

    @Test
    @Transactional
    void getAllTasksByPipelineIsEqualToSomething() throws Exception {
        Pipeline pipeline;
        if (TestUtil.findAll(em, Pipeline.class).isEmpty()) {
            taskRepository.saveAndFlush(task);
            pipeline = PipelineResourceIT.createEntity(em);
        } else {
            pipeline = TestUtil.findAll(em, Pipeline.class).get(0);
        }
        em.persist(pipeline);
        em.flush();
        task.setPipeline(pipeline);
        taskRepository.saveAndFlush(task);
        Long pipelineId = pipeline.getId();
        // Get all the taskList where pipeline equals to pipelineId
        defaultTaskShouldBeFound("pipelineId.equals=" + pipelineId);

        // Get all the taskList where pipeline equals to (pipelineId + 1)
        defaultTaskShouldNotBeFound("pipelineId.equals=" + (pipelineId + 1));
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultTaskShouldBeFound(String filter) throws Exception {
        restTaskMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(task.getId().intValue())))
            .andExpect(jsonPath("$.[*].taskType").value(hasItem(DEFAULT_TASK_TYPE.toString())))
            .andExpect(jsonPath("$.[*].dueDate").value(hasItem(DEFAULT_DUE_DATE.toString())))
            .andExpect(jsonPath("$.[*].taskName").value(hasItem(DEFAULT_TASK_NAME)))
            .andExpect(jsonPath("$.[*].status").value(hasItem(DEFAULT_STATUS.toString())))
            .andExpect(jsonPath("$.[*].taskCompletionDate").value(hasItem(DEFAULT_TASK_COMPLETION_DATE.toString())))
            .andExpect(jsonPath("$.[*].correlationId").value(hasItem(DEFAULT_CORRELATION_ID.toString())));

        // Check, that the count call also returns 1
        restTaskMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultTaskShouldNotBeFound(String filter) throws Exception {
        restTaskMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restTaskMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingTask() throws Exception {
        // Get the task
        restTaskMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingTask() throws Exception {
        // Initialize the database
        taskRepository.saveAndFlush(task);

        int databaseSizeBeforeUpdate = taskRepository.findAll().size();

        // Update the task
        Task updatedTask = taskRepository.findById(task.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedTask are not directly saved in db
        em.detach(updatedTask);
        updatedTask
            .taskType(UPDATED_TASK_TYPE)
            .dueDate(UPDATED_DUE_DATE)
            .taskName(UPDATED_TASK_NAME)
            .status(UPDATED_STATUS)
            .taskCompletionDate(UPDATED_TASK_COMPLETION_DATE)
            .correlationId(UPDATED_CORRELATION_ID);
        TaskDTO taskDTO = taskMapper.toDto(updatedTask);

        restTaskMockMvc
            .perform(
                put(ENTITY_API_URL_ID, taskDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(taskDTO))
            )
            .andExpect(status().isOk());

        // Validate the Task in the database
        List<Task> taskList = taskRepository.findAll();
        assertThat(taskList).hasSize(databaseSizeBeforeUpdate);
        Task testTask = taskList.get(taskList.size() - 1);
        assertThat(testTask.getTaskType()).isEqualTo(UPDATED_TASK_TYPE);
        assertThat(testTask.getDueDate()).isEqualTo(UPDATED_DUE_DATE);
        assertThat(testTask.getTaskName()).isEqualTo(UPDATED_TASK_NAME);
        assertThat(testTask.getStatus()).isEqualTo(UPDATED_STATUS);
        assertThat(testTask.getTaskCompletionDate()).isEqualTo(UPDATED_TASK_COMPLETION_DATE);
        assertThat(testTask.getCorrelationId()).isEqualTo(UPDATED_CORRELATION_ID);
    }

    @Test
    @Transactional
    void putNonExistingTask() throws Exception {
        int databaseSizeBeforeUpdate = taskRepository.findAll().size();
        task.setId(longCount.incrementAndGet());

        // Create the Task
        TaskDTO taskDTO = taskMapper.toDto(task);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restTaskMockMvc
            .perform(
                put(ENTITY_API_URL_ID, taskDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(taskDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Task in the database
        List<Task> taskList = taskRepository.findAll();
        assertThat(taskList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchTask() throws Exception {
        int databaseSizeBeforeUpdate = taskRepository.findAll().size();
        task.setId(longCount.incrementAndGet());

        // Create the Task
        TaskDTO taskDTO = taskMapper.toDto(task);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restTaskMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(taskDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Task in the database
        List<Task> taskList = taskRepository.findAll();
        assertThat(taskList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamTask() throws Exception {
        int databaseSizeBeforeUpdate = taskRepository.findAll().size();
        task.setId(longCount.incrementAndGet());

        // Create the Task
        TaskDTO taskDTO = taskMapper.toDto(task);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restTaskMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(taskDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Task in the database
        List<Task> taskList = taskRepository.findAll();
        assertThat(taskList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateTaskWithPatch() throws Exception {
        // Initialize the database
        taskRepository.saveAndFlush(task);

        int databaseSizeBeforeUpdate = taskRepository.findAll().size();

        // Update the task using partial update
        Task partialUpdatedTask = new Task();
        partialUpdatedTask.setId(task.getId());

        partialUpdatedTask.taskName(UPDATED_TASK_NAME);

        restTaskMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedTask.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedTask))
            )
            .andExpect(status().isOk());

        // Validate the Task in the database
        List<Task> taskList = taskRepository.findAll();
        assertThat(taskList).hasSize(databaseSizeBeforeUpdate);
        Task testTask = taskList.get(taskList.size() - 1);
        assertThat(testTask.getTaskType()).isEqualTo(DEFAULT_TASK_TYPE);
        assertThat(testTask.getDueDate()).isEqualTo(DEFAULT_DUE_DATE);
        assertThat(testTask.getTaskName()).isEqualTo(UPDATED_TASK_NAME);
        assertThat(testTask.getStatus()).isEqualTo(DEFAULT_STATUS);
        assertThat(testTask.getTaskCompletionDate()).isEqualTo(DEFAULT_TASK_COMPLETION_DATE);
        assertThat(testTask.getCorrelationId()).isEqualTo(DEFAULT_CORRELATION_ID);
    }

    @Test
    @Transactional
    void fullUpdateTaskWithPatch() throws Exception {
        // Initialize the database
        taskRepository.saveAndFlush(task);

        int databaseSizeBeforeUpdate = taskRepository.findAll().size();

        // Update the task using partial update
        Task partialUpdatedTask = new Task();
        partialUpdatedTask.setId(task.getId());

        partialUpdatedTask
            .taskType(UPDATED_TASK_TYPE)
            .dueDate(UPDATED_DUE_DATE)
            .taskName(UPDATED_TASK_NAME)
            .status(UPDATED_STATUS)
            .taskCompletionDate(UPDATED_TASK_COMPLETION_DATE)
            .correlationId(UPDATED_CORRELATION_ID);

        restTaskMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedTask.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedTask))
            )
            .andExpect(status().isOk());

        // Validate the Task in the database
        List<Task> taskList = taskRepository.findAll();
        assertThat(taskList).hasSize(databaseSizeBeforeUpdate);
        Task testTask = taskList.get(taskList.size() - 1);
        assertThat(testTask.getTaskType()).isEqualTo(UPDATED_TASK_TYPE);
        assertThat(testTask.getDueDate()).isEqualTo(UPDATED_DUE_DATE);
        assertThat(testTask.getTaskName()).isEqualTo(UPDATED_TASK_NAME);
        assertThat(testTask.getStatus()).isEqualTo(UPDATED_STATUS);
        assertThat(testTask.getTaskCompletionDate()).isEqualTo(UPDATED_TASK_COMPLETION_DATE);
        assertThat(testTask.getCorrelationId()).isEqualTo(UPDATED_CORRELATION_ID);
    }

    @Test
    @Transactional
    void patchNonExistingTask() throws Exception {
        int databaseSizeBeforeUpdate = taskRepository.findAll().size();
        task.setId(longCount.incrementAndGet());

        // Create the Task
        TaskDTO taskDTO = taskMapper.toDto(task);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restTaskMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, taskDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(taskDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Task in the database
        List<Task> taskList = taskRepository.findAll();
        assertThat(taskList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchTask() throws Exception {
        int databaseSizeBeforeUpdate = taskRepository.findAll().size();
        task.setId(longCount.incrementAndGet());

        // Create the Task
        TaskDTO taskDTO = taskMapper.toDto(task);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restTaskMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(taskDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Task in the database
        List<Task> taskList = taskRepository.findAll();
        assertThat(taskList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamTask() throws Exception {
        int databaseSizeBeforeUpdate = taskRepository.findAll().size();
        task.setId(longCount.incrementAndGet());

        // Create the Task
        TaskDTO taskDTO = taskMapper.toDto(task);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restTaskMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(taskDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Task in the database
        List<Task> taskList = taskRepository.findAll();
        assertThat(taskList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteTask() throws Exception {
        // Initialize the database
        taskRepository.saveAndFlush(task);

        int databaseSizeBeforeDelete = taskRepository.findAll().size();

        // Delete the task
        restTaskMockMvc
            .perform(delete(ENTITY_API_URL_ID, task.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Task> taskList = taskRepository.findAll();
        assertThat(taskList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
