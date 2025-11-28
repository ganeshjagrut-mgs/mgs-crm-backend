package com.mgs.web.rest;

import static com.mgs.domain.TaskAsserts.*;
import static com.mgs.web.rest.TestUtil.createUpdateProxyForBean;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mgs.IntegrationTest;
import com.mgs.domain.Task;
import com.mgs.domain.TaskType;
import com.mgs.domain.Tenant;
import com.mgs.domain.User;
import com.mgs.domain.enumeration.TaskPriority;
import com.mgs.domain.enumeration.TaskStatus;
import com.mgs.repository.TaskRepository;
import com.mgs.service.dto.TaskDTO;
import com.mgs.service.mapper.TaskMapper;
import jakarta.persistence.EntityManager;
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
 * Integration tests for the {@link TaskResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class TaskResourceIT {

    private static final String DEFAULT_TITLE = "AAAAAAAAAA";
    private static final String UPDATED_TITLE = "BBBBBBBBBB";

    private static final String DEFAULT_DESCRIPTION = "AAAAAAAAAA";
    private static final String UPDATED_DESCRIPTION = "BBBBBBBBBB";

    private static final String DEFAULT_RELATED_TYPE = "AAAAAAAAAA";
    private static final String UPDATED_RELATED_TYPE = "BBBBBBBBBB";

    private static final TaskStatus DEFAULT_STATUS = TaskStatus.TODO;
    private static final TaskStatus UPDATED_STATUS = TaskStatus.IN_PROGRESS;

    private static final TaskPriority DEFAULT_PRIORITY = TaskPriority.LOW;
    private static final TaskPriority UPDATED_PRIORITY = TaskPriority.MEDIUM;

    private static final Instant DEFAULT_DUE_AT = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_DUE_AT = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final Instant DEFAULT_COMPLETED_AT = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_COMPLETED_AT = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final String ENTITY_API_URL = "/api/tasks";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ObjectMapper om;

    @Autowired
    private TaskRepository taskRepository;

    @Autowired
    private TaskMapper taskMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restTaskMockMvc;

    private Task task;

    private Task insertedTask;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Task createEntity(EntityManager em) {
        Task task = new Task()
            .title(DEFAULT_TITLE)
            .description(DEFAULT_DESCRIPTION)
            .relatedType(DEFAULT_RELATED_TYPE)
            .status(DEFAULT_STATUS)
            .priority(DEFAULT_PRIORITY)
            .dueAt(DEFAULT_DUE_AT)
            .completedAt(DEFAULT_COMPLETED_AT);
        // Add required entity
        Tenant tenant;
        if (TestUtil.findAll(em, Tenant.class).isEmpty()) {
            tenant = TenantResourceIT.createEntity();
            em.persist(tenant);
            em.flush();
        } else {
            tenant = TestUtil.findAll(em, Tenant.class).get(0);
        }
        task.setTenant(tenant);
        // Add required entity
        User user = UserResourceIT.createEntity(em);
        em.persist(user);
        em.flush();
        task.setAssignedUser(user);
        // Add required entity
        task.setCreatedByUser(user);
        return task;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Task createUpdatedEntity(EntityManager em) {
        Task updatedTask = new Task()
            .title(UPDATED_TITLE)
            .description(UPDATED_DESCRIPTION)
            .relatedType(UPDATED_RELATED_TYPE)
            .status(UPDATED_STATUS)
            .priority(UPDATED_PRIORITY)
            .dueAt(UPDATED_DUE_AT)
            .completedAt(UPDATED_COMPLETED_AT);
        // Add required entity
        Tenant tenant;
        if (TestUtil.findAll(em, Tenant.class).isEmpty()) {
            tenant = TenantResourceIT.createUpdatedEntity();
            em.persist(tenant);
            em.flush();
        } else {
            tenant = TestUtil.findAll(em, Tenant.class).get(0);
        }
        updatedTask.setTenant(tenant);
        // Add required entity
        User user = UserResourceIT.createEntity(em);
        em.persist(user);
        em.flush();
        updatedTask.setAssignedUser(user);
        // Add required entity
        updatedTask.setCreatedByUser(user);
        return updatedTask;
    }

    @BeforeEach
    void initTest() {
        task = createEntity(em);
    }

    @AfterEach
    void cleanup() {
        if (insertedTask != null) {
            taskRepository.delete(insertedTask);
            insertedTask = null;
        }
    }

    @Test
    @Transactional
    void createTask() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the Task
        TaskDTO taskDTO = taskMapper.toDto(task);
        var returnedTaskDTO = om.readValue(
            restTaskMockMvc
                .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(taskDTO)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            TaskDTO.class
        );

        // Validate the Task in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        var returnedTask = taskMapper.toEntity(returnedTaskDTO);
        assertTaskUpdatableFieldsEquals(returnedTask, getPersistedTask(returnedTask));

        insertedTask = returnedTask;
    }

    @Test
    @Transactional
    void createTaskWithExistingId() throws Exception {
        // Create the Task with an existing ID
        task.setId(1L);
        TaskDTO taskDTO = taskMapper.toDto(task);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restTaskMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(taskDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Task in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkTitleIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        task.setTitle(null);

        // Create the Task, which fails.
        TaskDTO taskDTO = taskMapper.toDto(task);

        restTaskMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(taskDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkStatusIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        task.setStatus(null);

        // Create the Task, which fails.
        TaskDTO taskDTO = taskMapper.toDto(task);

        restTaskMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(taskDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkPriorityIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        task.setPriority(null);

        // Create the Task, which fails.
        TaskDTO taskDTO = taskMapper.toDto(task);

        restTaskMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(taskDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllTasks() throws Exception {
        // Initialize the database
        insertedTask = taskRepository.saveAndFlush(task);

        // Get all the taskList
        restTaskMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(task.getId().intValue())))
            .andExpect(jsonPath("$.[*].title").value(hasItem(DEFAULT_TITLE)))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION)))
            .andExpect(jsonPath("$.[*].relatedType").value(hasItem(DEFAULT_RELATED_TYPE)))
            .andExpect(jsonPath("$.[*].status").value(hasItem(DEFAULT_STATUS.toString())))
            .andExpect(jsonPath("$.[*].priority").value(hasItem(DEFAULT_PRIORITY.toString())))
            .andExpect(jsonPath("$.[*].dueAt").value(hasItem(DEFAULT_DUE_AT.toString())))
            .andExpect(jsonPath("$.[*].completedAt").value(hasItem(DEFAULT_COMPLETED_AT.toString())));
    }

    @Test
    @Transactional
    void getTask() throws Exception {
        // Initialize the database
        insertedTask = taskRepository.saveAndFlush(task);

        // Get the task
        restTaskMockMvc
            .perform(get(ENTITY_API_URL_ID, task.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(task.getId().intValue()))
            .andExpect(jsonPath("$.title").value(DEFAULT_TITLE))
            .andExpect(jsonPath("$.description").value(DEFAULT_DESCRIPTION))
            .andExpect(jsonPath("$.relatedType").value(DEFAULT_RELATED_TYPE))
            .andExpect(jsonPath("$.status").value(DEFAULT_STATUS.toString()))
            .andExpect(jsonPath("$.priority").value(DEFAULT_PRIORITY.toString()))
            .andExpect(jsonPath("$.dueAt").value(DEFAULT_DUE_AT.toString()))
            .andExpect(jsonPath("$.completedAt").value(DEFAULT_COMPLETED_AT.toString()));
    }

    @Test
    @Transactional
    void getTasksByIdFiltering() throws Exception {
        // Initialize the database
        insertedTask = taskRepository.saveAndFlush(task);

        Long id = task.getId();

        defaultTaskFiltering("id.equals=" + id, "id.notEquals=" + id);

        defaultTaskFiltering("id.greaterThanOrEqual=" + id, "id.greaterThan=" + id);

        defaultTaskFiltering("id.lessThanOrEqual=" + id, "id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllTasksByTitleIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedTask = taskRepository.saveAndFlush(task);

        // Get all the taskList where title equals to
        defaultTaskFiltering("title.equals=" + DEFAULT_TITLE, "title.equals=" + UPDATED_TITLE);
    }

    @Test
    @Transactional
    void getAllTasksByTitleIsInShouldWork() throws Exception {
        // Initialize the database
        insertedTask = taskRepository.saveAndFlush(task);

        // Get all the taskList where title in
        defaultTaskFiltering("title.in=" + DEFAULT_TITLE + "," + UPDATED_TITLE, "title.in=" + UPDATED_TITLE);
    }

    @Test
    @Transactional
    void getAllTasksByTitleIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedTask = taskRepository.saveAndFlush(task);

        // Get all the taskList where title is not null
        defaultTaskFiltering("title.specified=true", "title.specified=false");
    }

    @Test
    @Transactional
    void getAllTasksByTitleContainsSomething() throws Exception {
        // Initialize the database
        insertedTask = taskRepository.saveAndFlush(task);

        // Get all the taskList where title contains
        defaultTaskFiltering("title.contains=" + DEFAULT_TITLE, "title.contains=" + UPDATED_TITLE);
    }

    @Test
    @Transactional
    void getAllTasksByTitleNotContainsSomething() throws Exception {
        // Initialize the database
        insertedTask = taskRepository.saveAndFlush(task);

        // Get all the taskList where title does not contain
        defaultTaskFiltering("title.doesNotContain=" + UPDATED_TITLE, "title.doesNotContain=" + DEFAULT_TITLE);
    }

    @Test
    @Transactional
    void getAllTasksByDescriptionIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedTask = taskRepository.saveAndFlush(task);

        // Get all the taskList where description equals to
        defaultTaskFiltering("description.equals=" + DEFAULT_DESCRIPTION, "description.equals=" + UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    void getAllTasksByDescriptionIsInShouldWork() throws Exception {
        // Initialize the database
        insertedTask = taskRepository.saveAndFlush(task);

        // Get all the taskList where description in
        defaultTaskFiltering("description.in=" + DEFAULT_DESCRIPTION + "," + UPDATED_DESCRIPTION, "description.in=" + UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    void getAllTasksByDescriptionIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedTask = taskRepository.saveAndFlush(task);

        // Get all the taskList where description is not null
        defaultTaskFiltering("description.specified=true", "description.specified=false");
    }

    @Test
    @Transactional
    void getAllTasksByDescriptionContainsSomething() throws Exception {
        // Initialize the database
        insertedTask = taskRepository.saveAndFlush(task);

        // Get all the taskList where description contains
        defaultTaskFiltering("description.contains=" + DEFAULT_DESCRIPTION, "description.contains=" + UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    void getAllTasksByDescriptionNotContainsSomething() throws Exception {
        // Initialize the database
        insertedTask = taskRepository.saveAndFlush(task);

        // Get all the taskList where description does not contain
        defaultTaskFiltering("description.doesNotContain=" + UPDATED_DESCRIPTION, "description.doesNotContain=" + DEFAULT_DESCRIPTION);
    }

    @Test
    @Transactional
    void getAllTasksByRelatedTypeIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedTask = taskRepository.saveAndFlush(task);

        // Get all the taskList where relatedType equals to
        defaultTaskFiltering("relatedType.equals=" + DEFAULT_RELATED_TYPE, "relatedType.equals=" + UPDATED_RELATED_TYPE);
    }

    @Test
    @Transactional
    void getAllTasksByRelatedTypeIsInShouldWork() throws Exception {
        // Initialize the database
        insertedTask = taskRepository.saveAndFlush(task);

        // Get all the taskList where relatedType in
        defaultTaskFiltering(
            "relatedType.in=" + DEFAULT_RELATED_TYPE + "," + UPDATED_RELATED_TYPE,
            "relatedType.in=" + UPDATED_RELATED_TYPE
        );
    }

    @Test
    @Transactional
    void getAllTasksByRelatedTypeIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedTask = taskRepository.saveAndFlush(task);

        // Get all the taskList where relatedType is not null
        defaultTaskFiltering("relatedType.specified=true", "relatedType.specified=false");
    }

    @Test
    @Transactional
    void getAllTasksByRelatedTypeContainsSomething() throws Exception {
        // Initialize the database
        insertedTask = taskRepository.saveAndFlush(task);

        // Get all the taskList where relatedType contains
        defaultTaskFiltering("relatedType.contains=" + DEFAULT_RELATED_TYPE, "relatedType.contains=" + UPDATED_RELATED_TYPE);
    }

    @Test
    @Transactional
    void getAllTasksByRelatedTypeNotContainsSomething() throws Exception {
        // Initialize the database
        insertedTask = taskRepository.saveAndFlush(task);

        // Get all the taskList where relatedType does not contain
        defaultTaskFiltering("relatedType.doesNotContain=" + UPDATED_RELATED_TYPE, "relatedType.doesNotContain=" + DEFAULT_RELATED_TYPE);
    }

    @Test
    @Transactional
    void getAllTasksByStatusIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedTask = taskRepository.saveAndFlush(task);

        // Get all the taskList where status equals to
        defaultTaskFiltering("status.equals=" + DEFAULT_STATUS, "status.equals=" + UPDATED_STATUS);
    }

    @Test
    @Transactional
    void getAllTasksByStatusIsInShouldWork() throws Exception {
        // Initialize the database
        insertedTask = taskRepository.saveAndFlush(task);

        // Get all the taskList where status in
        defaultTaskFiltering("status.in=" + DEFAULT_STATUS + "," + UPDATED_STATUS, "status.in=" + UPDATED_STATUS);
    }

    @Test
    @Transactional
    void getAllTasksByStatusIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedTask = taskRepository.saveAndFlush(task);

        // Get all the taskList where status is not null
        defaultTaskFiltering("status.specified=true", "status.specified=false");
    }

    @Test
    @Transactional
    void getAllTasksByPriorityIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedTask = taskRepository.saveAndFlush(task);

        // Get all the taskList where priority equals to
        defaultTaskFiltering("priority.equals=" + DEFAULT_PRIORITY, "priority.equals=" + UPDATED_PRIORITY);
    }

    @Test
    @Transactional
    void getAllTasksByPriorityIsInShouldWork() throws Exception {
        // Initialize the database
        insertedTask = taskRepository.saveAndFlush(task);

        // Get all the taskList where priority in
        defaultTaskFiltering("priority.in=" + DEFAULT_PRIORITY + "," + UPDATED_PRIORITY, "priority.in=" + UPDATED_PRIORITY);
    }

    @Test
    @Transactional
    void getAllTasksByPriorityIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedTask = taskRepository.saveAndFlush(task);

        // Get all the taskList where priority is not null
        defaultTaskFiltering("priority.specified=true", "priority.specified=false");
    }

    @Test
    @Transactional
    void getAllTasksByDueAtIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedTask = taskRepository.saveAndFlush(task);

        // Get all the taskList where dueAt equals to
        defaultTaskFiltering("dueAt.equals=" + DEFAULT_DUE_AT, "dueAt.equals=" + UPDATED_DUE_AT);
    }

    @Test
    @Transactional
    void getAllTasksByDueAtIsInShouldWork() throws Exception {
        // Initialize the database
        insertedTask = taskRepository.saveAndFlush(task);

        // Get all the taskList where dueAt in
        defaultTaskFiltering("dueAt.in=" + DEFAULT_DUE_AT + "," + UPDATED_DUE_AT, "dueAt.in=" + UPDATED_DUE_AT);
    }

    @Test
    @Transactional
    void getAllTasksByDueAtIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedTask = taskRepository.saveAndFlush(task);

        // Get all the taskList where dueAt is not null
        defaultTaskFiltering("dueAt.specified=true", "dueAt.specified=false");
    }

    @Test
    @Transactional
    void getAllTasksByCompletedAtIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedTask = taskRepository.saveAndFlush(task);

        // Get all the taskList where completedAt equals to
        defaultTaskFiltering("completedAt.equals=" + DEFAULT_COMPLETED_AT, "completedAt.equals=" + UPDATED_COMPLETED_AT);
    }

    @Test
    @Transactional
    void getAllTasksByCompletedAtIsInShouldWork() throws Exception {
        // Initialize the database
        insertedTask = taskRepository.saveAndFlush(task);

        // Get all the taskList where completedAt in
        defaultTaskFiltering(
            "completedAt.in=" + DEFAULT_COMPLETED_AT + "," + UPDATED_COMPLETED_AT,
            "completedAt.in=" + UPDATED_COMPLETED_AT
        );
    }

    @Test
    @Transactional
    void getAllTasksByCompletedAtIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedTask = taskRepository.saveAndFlush(task);

        // Get all the taskList where completedAt is not null
        defaultTaskFiltering("completedAt.specified=true", "completedAt.specified=false");
    }

    @Test
    @Transactional
    void getAllTasksByTenantIsEqualToSomething() throws Exception {
        Tenant tenant;
        if (TestUtil.findAll(em, Tenant.class).isEmpty()) {
            taskRepository.saveAndFlush(task);
            tenant = TenantResourceIT.createEntity();
        } else {
            tenant = TestUtil.findAll(em, Tenant.class).get(0);
        }
        em.persist(tenant);
        em.flush();
        task.setTenant(tenant);
        taskRepository.saveAndFlush(task);
        Long tenantId = tenant.getId();
        // Get all the taskList where tenant equals to tenantId
        defaultTaskShouldBeFound("tenantId.equals=" + tenantId);

        // Get all the taskList where tenant equals to (tenantId + 1)
        defaultTaskShouldNotBeFound("tenantId.equals=" + (tenantId + 1));
    }

    @Test
    @Transactional
    void getAllTasksByTaskTypeIsEqualToSomething() throws Exception {
        TaskType taskType;
        if (TestUtil.findAll(em, TaskType.class).isEmpty()) {
            taskRepository.saveAndFlush(task);
            taskType = TaskTypeResourceIT.createEntity(em);
        } else {
            taskType = TestUtil.findAll(em, TaskType.class).get(0);
        }
        em.persist(taskType);
        em.flush();
        task.setTaskType(taskType);
        taskRepository.saveAndFlush(task);
        Long taskTypeId = taskType.getId();
        // Get all the taskList where taskType equals to taskTypeId
        defaultTaskShouldBeFound("taskTypeId.equals=" + taskTypeId);

        // Get all the taskList where taskType equals to (taskTypeId + 1)
        defaultTaskShouldNotBeFound("taskTypeId.equals=" + (taskTypeId + 1));
    }

    @Test
    @Transactional
    void getAllTasksByAssignedUserIsEqualToSomething() throws Exception {
        User assignedUser;
        if (TestUtil.findAll(em, User.class).isEmpty()) {
            taskRepository.saveAndFlush(task);
            assignedUser = UserResourceIT.createEntity(em);
        } else {
            assignedUser = TestUtil.findAll(em, User.class).get(0);
        }
        em.persist(assignedUser);
        em.flush();
        task.setAssignedUser(assignedUser);
        taskRepository.saveAndFlush(task);
        Long assignedUserId = assignedUser.getId();
        // Get all the taskList where assignedUser equals to assignedUserId
        defaultTaskShouldBeFound("assignedUserId.equals=" + assignedUserId);

        // Get all the taskList where assignedUser equals to (assignedUserId + 1)
        defaultTaskShouldNotBeFound("assignedUserId.equals=" + (assignedUserId + 1));
    }

    @Test
    @Transactional
    void getAllTasksByCreatedByUserIsEqualToSomething() throws Exception {
        User createdByUser;
        if (TestUtil.findAll(em, User.class).isEmpty()) {
            taskRepository.saveAndFlush(task);
            createdByUser = UserResourceIT.createEntity(em);
        } else {
            createdByUser = TestUtil.findAll(em, User.class).get(0);
        }
        em.persist(createdByUser);
        em.flush();
        task.setCreatedByUser(createdByUser);
        taskRepository.saveAndFlush(task);
        Long createdByUserId = createdByUser.getId();
        // Get all the taskList where createdByUser equals to createdByUserId
        defaultTaskShouldBeFound("createdByUserId.equals=" + createdByUserId);

        // Get all the taskList where createdByUser equals to (createdByUserId + 1)
        defaultTaskShouldNotBeFound("createdByUserId.equals=" + (createdByUserId + 1));
    }

    private void defaultTaskFiltering(String shouldBeFound, String shouldNotBeFound) throws Exception {
        defaultTaskShouldBeFound(shouldBeFound);
        defaultTaskShouldNotBeFound(shouldNotBeFound);
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
            .andExpect(jsonPath("$.[*].title").value(hasItem(DEFAULT_TITLE)))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION)))
            .andExpect(jsonPath("$.[*].relatedType").value(hasItem(DEFAULT_RELATED_TYPE)))
            .andExpect(jsonPath("$.[*].status").value(hasItem(DEFAULT_STATUS.toString())))
            .andExpect(jsonPath("$.[*].priority").value(hasItem(DEFAULT_PRIORITY.toString())))
            .andExpect(jsonPath("$.[*].dueAt").value(hasItem(DEFAULT_DUE_AT.toString())))
            .andExpect(jsonPath("$.[*].completedAt").value(hasItem(DEFAULT_COMPLETED_AT.toString())));

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
        insertedTask = taskRepository.saveAndFlush(task);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the task
        Task updatedTask = taskRepository.findById(task.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedTask are not directly saved in db
        em.detach(updatedTask);
        updatedTask
            .title(UPDATED_TITLE)
            .description(UPDATED_DESCRIPTION)
            .relatedType(UPDATED_RELATED_TYPE)
            .status(UPDATED_STATUS)
            .priority(UPDATED_PRIORITY)
            .dueAt(UPDATED_DUE_AT)
            .completedAt(UPDATED_COMPLETED_AT);
        TaskDTO taskDTO = taskMapper.toDto(updatedTask);

        restTaskMockMvc
            .perform(put(ENTITY_API_URL_ID, taskDTO.getId()).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(taskDTO)))
            .andExpect(status().isOk());

        // Validate the Task in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedTaskToMatchAllProperties(updatedTask);
    }

    @Test
    @Transactional
    void putNonExistingTask() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        task.setId(longCount.incrementAndGet());

        // Create the Task
        TaskDTO taskDTO = taskMapper.toDto(task);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restTaskMockMvc
            .perform(put(ENTITY_API_URL_ID, taskDTO.getId()).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(taskDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Task in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchTask() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        task.setId(longCount.incrementAndGet());

        // Create the Task
        TaskDTO taskDTO = taskMapper.toDto(task);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restTaskMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(taskDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Task in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamTask() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        task.setId(longCount.incrementAndGet());

        // Create the Task
        TaskDTO taskDTO = taskMapper.toDto(task);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restTaskMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(taskDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Task in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateTaskWithPatch() throws Exception {
        // Initialize the database
        insertedTask = taskRepository.saveAndFlush(task);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the task using partial update
        Task partialUpdatedTask = new Task();
        partialUpdatedTask.setId(task.getId());

        partialUpdatedTask
            .description(UPDATED_DESCRIPTION)
            .relatedType(UPDATED_RELATED_TYPE)
            .status(UPDATED_STATUS)
            .priority(UPDATED_PRIORITY)
            .dueAt(UPDATED_DUE_AT)
            .completedAt(UPDATED_COMPLETED_AT);

        restTaskMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedTask.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedTask))
            )
            .andExpect(status().isOk());

        // Validate the Task in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertTaskUpdatableFieldsEquals(createUpdateProxyForBean(partialUpdatedTask, task), getPersistedTask(task));
    }

    @Test
    @Transactional
    void fullUpdateTaskWithPatch() throws Exception {
        // Initialize the database
        insertedTask = taskRepository.saveAndFlush(task);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the task using partial update
        Task partialUpdatedTask = new Task();
        partialUpdatedTask.setId(task.getId());

        partialUpdatedTask
            .title(UPDATED_TITLE)
            .description(UPDATED_DESCRIPTION)
            .relatedType(UPDATED_RELATED_TYPE)
            .status(UPDATED_STATUS)
            .priority(UPDATED_PRIORITY)
            .dueAt(UPDATED_DUE_AT)
            .completedAt(UPDATED_COMPLETED_AT);

        restTaskMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedTask.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedTask))
            )
            .andExpect(status().isOk());

        // Validate the Task in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertTaskUpdatableFieldsEquals(partialUpdatedTask, getPersistedTask(partialUpdatedTask));
    }

    @Test
    @Transactional
    void patchNonExistingTask() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        task.setId(longCount.incrementAndGet());

        // Create the Task
        TaskDTO taskDTO = taskMapper.toDto(task);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restTaskMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, taskDTO.getId()).contentType("application/merge-patch+json").content(om.writeValueAsBytes(taskDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Task in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchTask() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        task.setId(longCount.incrementAndGet());

        // Create the Task
        TaskDTO taskDTO = taskMapper.toDto(task);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restTaskMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(taskDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Task in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamTask() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        task.setId(longCount.incrementAndGet());

        // Create the Task
        TaskDTO taskDTO = taskMapper.toDto(task);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restTaskMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(om.writeValueAsBytes(taskDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Task in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteTask() throws Exception {
        // Initialize the database
        insertedTask = taskRepository.saveAndFlush(task);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the task
        restTaskMockMvc
            .perform(delete(ENTITY_API_URL_ID, task.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return taskRepository.count();
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

    protected Task getPersistedTask(Task task) {
        return taskRepository.findById(task.getId()).orElseThrow();
    }

    protected void assertPersistedTaskToMatchAllProperties(Task expectedTask) {
        assertTaskAllPropertiesEquals(expectedTask, getPersistedTask(expectedTask));
    }

    protected void assertPersistedTaskToMatchUpdatableProperties(Task expectedTask) {
        assertTaskAllUpdatablePropertiesEquals(expectedTask, getPersistedTask(expectedTask));
    }
}
