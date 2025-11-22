package com.crm.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.crm.IntegrationTest;
import com.crm.domain.TaskAudit;
import com.crm.repository.TaskAuditRepository;
import com.crm.service.dto.TaskAuditDTO;
import com.crm.service.mapper.TaskAuditMapper;
import jakarta.persistence.EntityManager;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
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
 * Integration tests for the {@link TaskAuditResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class TaskAuditResourceIT {

    private static final Instant DEFAULT_EVENT_TIMESTAMP = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_EVENT_TIMESTAMP = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final String DEFAULT_ACTION = "AAAAAAAAAA";
    private static final String UPDATED_ACTION = "BBBBBBBBBB";

    private static final UUID DEFAULT_ROW_ID = UUID.randomUUID();
    private static final UUID UPDATED_ROW_ID = UUID.randomUUID();

    private static final String DEFAULT_CHANGES = "AAAAAAAAAA";
    private static final String UPDATED_CHANGES = "BBBBBBBBBB";

    private static final UUID DEFAULT_CORRELATION_ID = UUID.randomUUID();
    private static final UUID UPDATED_CORRELATION_ID = UUID.randomUUID();

    private static final String ENTITY_API_URL = "/api/task-audits";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private TaskAuditRepository taskAuditRepository;

    @Autowired
    private TaskAuditMapper taskAuditMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restTaskAuditMockMvc;

    private TaskAudit taskAudit;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static TaskAudit createEntity(EntityManager em) {
        TaskAudit taskAudit = new TaskAudit()
            .eventTimestamp(DEFAULT_EVENT_TIMESTAMP)
            .action(DEFAULT_ACTION)
            .rowId(DEFAULT_ROW_ID)
            .changes(DEFAULT_CHANGES)
            .correlationId(DEFAULT_CORRELATION_ID);
        return taskAudit;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static TaskAudit createUpdatedEntity(EntityManager em) {
        TaskAudit taskAudit = new TaskAudit()
            .eventTimestamp(UPDATED_EVENT_TIMESTAMP)
            .action(UPDATED_ACTION)
            .rowId(UPDATED_ROW_ID)
            .changes(UPDATED_CHANGES)
            .correlationId(UPDATED_CORRELATION_ID);
        return taskAudit;
    }

    @BeforeEach
    public void initTest() {
        taskAudit = createEntity(em);
    }

    @Test
    @Transactional
    void createTaskAudit() throws Exception {
        int databaseSizeBeforeCreate = taskAuditRepository.findAll().size();
        // Create the TaskAudit
        TaskAuditDTO taskAuditDTO = taskAuditMapper.toDto(taskAudit);
        restTaskAuditMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(taskAuditDTO)))
            .andExpect(status().isCreated());

        // Validate the TaskAudit in the database
        List<TaskAudit> taskAuditList = taskAuditRepository.findAll();
        assertThat(taskAuditList).hasSize(databaseSizeBeforeCreate + 1);
        TaskAudit testTaskAudit = taskAuditList.get(taskAuditList.size() - 1);
        assertThat(testTaskAudit.getEventTimestamp()).isEqualTo(DEFAULT_EVENT_TIMESTAMP);
        assertThat(testTaskAudit.getAction()).isEqualTo(DEFAULT_ACTION);
        assertThat(testTaskAudit.getRowId()).isEqualTo(DEFAULT_ROW_ID);
        assertThat(testTaskAudit.getChanges()).isEqualTo(DEFAULT_CHANGES);
        assertThat(testTaskAudit.getCorrelationId()).isEqualTo(DEFAULT_CORRELATION_ID);
    }

    @Test
    @Transactional
    void createTaskAuditWithExistingId() throws Exception {
        // Create the TaskAudit with an existing ID
        taskAudit.setId(1L);
        TaskAuditDTO taskAuditDTO = taskAuditMapper.toDto(taskAudit);

        int databaseSizeBeforeCreate = taskAuditRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restTaskAuditMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(taskAuditDTO)))
            .andExpect(status().isBadRequest());

        // Validate the TaskAudit in the database
        List<TaskAudit> taskAuditList = taskAuditRepository.findAll();
        assertThat(taskAuditList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllTaskAudits() throws Exception {
        // Initialize the database
        taskAuditRepository.saveAndFlush(taskAudit);

        // Get all the taskAuditList
        restTaskAuditMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(taskAudit.getId().intValue())))
            .andExpect(jsonPath("$.[*].eventTimestamp").value(hasItem(DEFAULT_EVENT_TIMESTAMP.toString())))
            .andExpect(jsonPath("$.[*].action").value(hasItem(DEFAULT_ACTION)))
            .andExpect(jsonPath("$.[*].rowId").value(hasItem(DEFAULT_ROW_ID.toString())))
            .andExpect(jsonPath("$.[*].changes").value(hasItem(DEFAULT_CHANGES.toString())))
            .andExpect(jsonPath("$.[*].correlationId").value(hasItem(DEFAULT_CORRELATION_ID.toString())));
    }

    @Test
    @Transactional
    void getTaskAudit() throws Exception {
        // Initialize the database
        taskAuditRepository.saveAndFlush(taskAudit);

        // Get the taskAudit
        restTaskAuditMockMvc
            .perform(get(ENTITY_API_URL_ID, taskAudit.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(taskAudit.getId().intValue()))
            .andExpect(jsonPath("$.eventTimestamp").value(DEFAULT_EVENT_TIMESTAMP.toString()))
            .andExpect(jsonPath("$.action").value(DEFAULT_ACTION))
            .andExpect(jsonPath("$.rowId").value(DEFAULT_ROW_ID.toString()))
            .andExpect(jsonPath("$.changes").value(DEFAULT_CHANGES.toString()))
            .andExpect(jsonPath("$.correlationId").value(DEFAULT_CORRELATION_ID.toString()));
    }

    @Test
    @Transactional
    void getTaskAuditsByIdFiltering() throws Exception {
        // Initialize the database
        taskAuditRepository.saveAndFlush(taskAudit);

        Long id = taskAudit.getId();

        defaultTaskAuditShouldBeFound("id.equals=" + id);
        defaultTaskAuditShouldNotBeFound("id.notEquals=" + id);

        defaultTaskAuditShouldBeFound("id.greaterThanOrEqual=" + id);
        defaultTaskAuditShouldNotBeFound("id.greaterThan=" + id);

        defaultTaskAuditShouldBeFound("id.lessThanOrEqual=" + id);
        defaultTaskAuditShouldNotBeFound("id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllTaskAuditsByEventTimestampIsEqualToSomething() throws Exception {
        // Initialize the database
        taskAuditRepository.saveAndFlush(taskAudit);

        // Get all the taskAuditList where eventTimestamp equals to DEFAULT_EVENT_TIMESTAMP
        defaultTaskAuditShouldBeFound("eventTimestamp.equals=" + DEFAULT_EVENT_TIMESTAMP);

        // Get all the taskAuditList where eventTimestamp equals to UPDATED_EVENT_TIMESTAMP
        defaultTaskAuditShouldNotBeFound("eventTimestamp.equals=" + UPDATED_EVENT_TIMESTAMP);
    }

    @Test
    @Transactional
    void getAllTaskAuditsByEventTimestampIsInShouldWork() throws Exception {
        // Initialize the database
        taskAuditRepository.saveAndFlush(taskAudit);

        // Get all the taskAuditList where eventTimestamp in DEFAULT_EVENT_TIMESTAMP or UPDATED_EVENT_TIMESTAMP
        defaultTaskAuditShouldBeFound("eventTimestamp.in=" + DEFAULT_EVENT_TIMESTAMP + "," + UPDATED_EVENT_TIMESTAMP);

        // Get all the taskAuditList where eventTimestamp equals to UPDATED_EVENT_TIMESTAMP
        defaultTaskAuditShouldNotBeFound("eventTimestamp.in=" + UPDATED_EVENT_TIMESTAMP);
    }

    @Test
    @Transactional
    void getAllTaskAuditsByEventTimestampIsNullOrNotNull() throws Exception {
        // Initialize the database
        taskAuditRepository.saveAndFlush(taskAudit);

        // Get all the taskAuditList where eventTimestamp is not null
        defaultTaskAuditShouldBeFound("eventTimestamp.specified=true");

        // Get all the taskAuditList where eventTimestamp is null
        defaultTaskAuditShouldNotBeFound("eventTimestamp.specified=false");
    }

    @Test
    @Transactional
    void getAllTaskAuditsByActionIsEqualToSomething() throws Exception {
        // Initialize the database
        taskAuditRepository.saveAndFlush(taskAudit);

        // Get all the taskAuditList where action equals to DEFAULT_ACTION
        defaultTaskAuditShouldBeFound("action.equals=" + DEFAULT_ACTION);

        // Get all the taskAuditList where action equals to UPDATED_ACTION
        defaultTaskAuditShouldNotBeFound("action.equals=" + UPDATED_ACTION);
    }

    @Test
    @Transactional
    void getAllTaskAuditsByActionIsInShouldWork() throws Exception {
        // Initialize the database
        taskAuditRepository.saveAndFlush(taskAudit);

        // Get all the taskAuditList where action in DEFAULT_ACTION or UPDATED_ACTION
        defaultTaskAuditShouldBeFound("action.in=" + DEFAULT_ACTION + "," + UPDATED_ACTION);

        // Get all the taskAuditList where action equals to UPDATED_ACTION
        defaultTaskAuditShouldNotBeFound("action.in=" + UPDATED_ACTION);
    }

    @Test
    @Transactional
    void getAllTaskAuditsByActionIsNullOrNotNull() throws Exception {
        // Initialize the database
        taskAuditRepository.saveAndFlush(taskAudit);

        // Get all the taskAuditList where action is not null
        defaultTaskAuditShouldBeFound("action.specified=true");

        // Get all the taskAuditList where action is null
        defaultTaskAuditShouldNotBeFound("action.specified=false");
    }

    @Test
    @Transactional
    void getAllTaskAuditsByActionContainsSomething() throws Exception {
        // Initialize the database
        taskAuditRepository.saveAndFlush(taskAudit);

        // Get all the taskAuditList where action contains DEFAULT_ACTION
        defaultTaskAuditShouldBeFound("action.contains=" + DEFAULT_ACTION);

        // Get all the taskAuditList where action contains UPDATED_ACTION
        defaultTaskAuditShouldNotBeFound("action.contains=" + UPDATED_ACTION);
    }

    @Test
    @Transactional
    void getAllTaskAuditsByActionNotContainsSomething() throws Exception {
        // Initialize the database
        taskAuditRepository.saveAndFlush(taskAudit);

        // Get all the taskAuditList where action does not contain DEFAULT_ACTION
        defaultTaskAuditShouldNotBeFound("action.doesNotContain=" + DEFAULT_ACTION);

        // Get all the taskAuditList where action does not contain UPDATED_ACTION
        defaultTaskAuditShouldBeFound("action.doesNotContain=" + UPDATED_ACTION);
    }

    @Test
    @Transactional
    void getAllTaskAuditsByRowIdIsEqualToSomething() throws Exception {
        // Initialize the database
        taskAuditRepository.saveAndFlush(taskAudit);

        // Get all the taskAuditList where rowId equals to DEFAULT_ROW_ID
        defaultTaskAuditShouldBeFound("rowId.equals=" + DEFAULT_ROW_ID);

        // Get all the taskAuditList where rowId equals to UPDATED_ROW_ID
        defaultTaskAuditShouldNotBeFound("rowId.equals=" + UPDATED_ROW_ID);
    }

    @Test
    @Transactional
    void getAllTaskAuditsByRowIdIsInShouldWork() throws Exception {
        // Initialize the database
        taskAuditRepository.saveAndFlush(taskAudit);

        // Get all the taskAuditList where rowId in DEFAULT_ROW_ID or UPDATED_ROW_ID
        defaultTaskAuditShouldBeFound("rowId.in=" + DEFAULT_ROW_ID + "," + UPDATED_ROW_ID);

        // Get all the taskAuditList where rowId equals to UPDATED_ROW_ID
        defaultTaskAuditShouldNotBeFound("rowId.in=" + UPDATED_ROW_ID);
    }

    @Test
    @Transactional
    void getAllTaskAuditsByRowIdIsNullOrNotNull() throws Exception {
        // Initialize the database
        taskAuditRepository.saveAndFlush(taskAudit);

        // Get all the taskAuditList where rowId is not null
        defaultTaskAuditShouldBeFound("rowId.specified=true");

        // Get all the taskAuditList where rowId is null
        defaultTaskAuditShouldNotBeFound("rowId.specified=false");
    }

    @Test
    @Transactional
    void getAllTaskAuditsByCorrelationIdIsEqualToSomething() throws Exception {
        // Initialize the database
        taskAuditRepository.saveAndFlush(taskAudit);

        // Get all the taskAuditList where correlationId equals to DEFAULT_CORRELATION_ID
        defaultTaskAuditShouldBeFound("correlationId.equals=" + DEFAULT_CORRELATION_ID);

        // Get all the taskAuditList where correlationId equals to UPDATED_CORRELATION_ID
        defaultTaskAuditShouldNotBeFound("correlationId.equals=" + UPDATED_CORRELATION_ID);
    }

    @Test
    @Transactional
    void getAllTaskAuditsByCorrelationIdIsInShouldWork() throws Exception {
        // Initialize the database
        taskAuditRepository.saveAndFlush(taskAudit);

        // Get all the taskAuditList where correlationId in DEFAULT_CORRELATION_ID or UPDATED_CORRELATION_ID
        defaultTaskAuditShouldBeFound("correlationId.in=" + DEFAULT_CORRELATION_ID + "," + UPDATED_CORRELATION_ID);

        // Get all the taskAuditList where correlationId equals to UPDATED_CORRELATION_ID
        defaultTaskAuditShouldNotBeFound("correlationId.in=" + UPDATED_CORRELATION_ID);
    }

    @Test
    @Transactional
    void getAllTaskAuditsByCorrelationIdIsNullOrNotNull() throws Exception {
        // Initialize the database
        taskAuditRepository.saveAndFlush(taskAudit);

        // Get all the taskAuditList where correlationId is not null
        defaultTaskAuditShouldBeFound("correlationId.specified=true");

        // Get all the taskAuditList where correlationId is null
        defaultTaskAuditShouldNotBeFound("correlationId.specified=false");
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultTaskAuditShouldBeFound(String filter) throws Exception {
        restTaskAuditMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(taskAudit.getId().intValue())))
            .andExpect(jsonPath("$.[*].eventTimestamp").value(hasItem(DEFAULT_EVENT_TIMESTAMP.toString())))
            .andExpect(jsonPath("$.[*].action").value(hasItem(DEFAULT_ACTION)))
            .andExpect(jsonPath("$.[*].rowId").value(hasItem(DEFAULT_ROW_ID.toString())))
            .andExpect(jsonPath("$.[*].changes").value(hasItem(DEFAULT_CHANGES.toString())))
            .andExpect(jsonPath("$.[*].correlationId").value(hasItem(DEFAULT_CORRELATION_ID.toString())));

        // Check, that the count call also returns 1
        restTaskAuditMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultTaskAuditShouldNotBeFound(String filter) throws Exception {
        restTaskAuditMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restTaskAuditMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingTaskAudit() throws Exception {
        // Get the taskAudit
        restTaskAuditMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingTaskAudit() throws Exception {
        // Initialize the database
        taskAuditRepository.saveAndFlush(taskAudit);

        int databaseSizeBeforeUpdate = taskAuditRepository.findAll().size();

        // Update the taskAudit
        TaskAudit updatedTaskAudit = taskAuditRepository.findById(taskAudit.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedTaskAudit are not directly saved in db
        em.detach(updatedTaskAudit);
        updatedTaskAudit
            .eventTimestamp(UPDATED_EVENT_TIMESTAMP)
            .action(UPDATED_ACTION)
            .rowId(UPDATED_ROW_ID)
            .changes(UPDATED_CHANGES)
            .correlationId(UPDATED_CORRELATION_ID);
        TaskAuditDTO taskAuditDTO = taskAuditMapper.toDto(updatedTaskAudit);

        restTaskAuditMockMvc
            .perform(
                put(ENTITY_API_URL_ID, taskAuditDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(taskAuditDTO))
            )
            .andExpect(status().isOk());

        // Validate the TaskAudit in the database
        List<TaskAudit> taskAuditList = taskAuditRepository.findAll();
        assertThat(taskAuditList).hasSize(databaseSizeBeforeUpdate);
        TaskAudit testTaskAudit = taskAuditList.get(taskAuditList.size() - 1);
        assertThat(testTaskAudit.getEventTimestamp()).isEqualTo(UPDATED_EVENT_TIMESTAMP);
        assertThat(testTaskAudit.getAction()).isEqualTo(UPDATED_ACTION);
        assertThat(testTaskAudit.getRowId()).isEqualTo(UPDATED_ROW_ID);
        assertThat(testTaskAudit.getChanges()).isEqualTo(UPDATED_CHANGES);
        assertThat(testTaskAudit.getCorrelationId()).isEqualTo(UPDATED_CORRELATION_ID);
    }

    @Test
    @Transactional
    void putNonExistingTaskAudit() throws Exception {
        int databaseSizeBeforeUpdate = taskAuditRepository.findAll().size();
        taskAudit.setId(longCount.incrementAndGet());

        // Create the TaskAudit
        TaskAuditDTO taskAuditDTO = taskAuditMapper.toDto(taskAudit);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restTaskAuditMockMvc
            .perform(
                put(ENTITY_API_URL_ID, taskAuditDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(taskAuditDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the TaskAudit in the database
        List<TaskAudit> taskAuditList = taskAuditRepository.findAll();
        assertThat(taskAuditList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchTaskAudit() throws Exception {
        int databaseSizeBeforeUpdate = taskAuditRepository.findAll().size();
        taskAudit.setId(longCount.incrementAndGet());

        // Create the TaskAudit
        TaskAuditDTO taskAuditDTO = taskAuditMapper.toDto(taskAudit);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restTaskAuditMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(taskAuditDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the TaskAudit in the database
        List<TaskAudit> taskAuditList = taskAuditRepository.findAll();
        assertThat(taskAuditList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamTaskAudit() throws Exception {
        int databaseSizeBeforeUpdate = taskAuditRepository.findAll().size();
        taskAudit.setId(longCount.incrementAndGet());

        // Create the TaskAudit
        TaskAuditDTO taskAuditDTO = taskAuditMapper.toDto(taskAudit);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restTaskAuditMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(taskAuditDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the TaskAudit in the database
        List<TaskAudit> taskAuditList = taskAuditRepository.findAll();
        assertThat(taskAuditList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateTaskAuditWithPatch() throws Exception {
        // Initialize the database
        taskAuditRepository.saveAndFlush(taskAudit);

        int databaseSizeBeforeUpdate = taskAuditRepository.findAll().size();

        // Update the taskAudit using partial update
        TaskAudit partialUpdatedTaskAudit = new TaskAudit();
        partialUpdatedTaskAudit.setId(taskAudit.getId());

        restTaskAuditMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedTaskAudit.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedTaskAudit))
            )
            .andExpect(status().isOk());

        // Validate the TaskAudit in the database
        List<TaskAudit> taskAuditList = taskAuditRepository.findAll();
        assertThat(taskAuditList).hasSize(databaseSizeBeforeUpdate);
        TaskAudit testTaskAudit = taskAuditList.get(taskAuditList.size() - 1);
        assertThat(testTaskAudit.getEventTimestamp()).isEqualTo(DEFAULT_EVENT_TIMESTAMP);
        assertThat(testTaskAudit.getAction()).isEqualTo(DEFAULT_ACTION);
        assertThat(testTaskAudit.getRowId()).isEqualTo(DEFAULT_ROW_ID);
        assertThat(testTaskAudit.getChanges()).isEqualTo(DEFAULT_CHANGES);
        assertThat(testTaskAudit.getCorrelationId()).isEqualTo(DEFAULT_CORRELATION_ID);
    }

    @Test
    @Transactional
    void fullUpdateTaskAuditWithPatch() throws Exception {
        // Initialize the database
        taskAuditRepository.saveAndFlush(taskAudit);

        int databaseSizeBeforeUpdate = taskAuditRepository.findAll().size();

        // Update the taskAudit using partial update
        TaskAudit partialUpdatedTaskAudit = new TaskAudit();
        partialUpdatedTaskAudit.setId(taskAudit.getId());

        partialUpdatedTaskAudit
            .eventTimestamp(UPDATED_EVENT_TIMESTAMP)
            .action(UPDATED_ACTION)
            .rowId(UPDATED_ROW_ID)
            .changes(UPDATED_CHANGES)
            .correlationId(UPDATED_CORRELATION_ID);

        restTaskAuditMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedTaskAudit.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedTaskAudit))
            )
            .andExpect(status().isOk());

        // Validate the TaskAudit in the database
        List<TaskAudit> taskAuditList = taskAuditRepository.findAll();
        assertThat(taskAuditList).hasSize(databaseSizeBeforeUpdate);
        TaskAudit testTaskAudit = taskAuditList.get(taskAuditList.size() - 1);
        assertThat(testTaskAudit.getEventTimestamp()).isEqualTo(UPDATED_EVENT_TIMESTAMP);
        assertThat(testTaskAudit.getAction()).isEqualTo(UPDATED_ACTION);
        assertThat(testTaskAudit.getRowId()).isEqualTo(UPDATED_ROW_ID);
        assertThat(testTaskAudit.getChanges()).isEqualTo(UPDATED_CHANGES);
        assertThat(testTaskAudit.getCorrelationId()).isEqualTo(UPDATED_CORRELATION_ID);
    }

    @Test
    @Transactional
    void patchNonExistingTaskAudit() throws Exception {
        int databaseSizeBeforeUpdate = taskAuditRepository.findAll().size();
        taskAudit.setId(longCount.incrementAndGet());

        // Create the TaskAudit
        TaskAuditDTO taskAuditDTO = taskAuditMapper.toDto(taskAudit);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restTaskAuditMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, taskAuditDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(taskAuditDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the TaskAudit in the database
        List<TaskAudit> taskAuditList = taskAuditRepository.findAll();
        assertThat(taskAuditList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchTaskAudit() throws Exception {
        int databaseSizeBeforeUpdate = taskAuditRepository.findAll().size();
        taskAudit.setId(longCount.incrementAndGet());

        // Create the TaskAudit
        TaskAuditDTO taskAuditDTO = taskAuditMapper.toDto(taskAudit);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restTaskAuditMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(taskAuditDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the TaskAudit in the database
        List<TaskAudit> taskAuditList = taskAuditRepository.findAll();
        assertThat(taskAuditList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamTaskAudit() throws Exception {
        int databaseSizeBeforeUpdate = taskAuditRepository.findAll().size();
        taskAudit.setId(longCount.incrementAndGet());

        // Create the TaskAudit
        TaskAuditDTO taskAuditDTO = taskAuditMapper.toDto(taskAudit);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restTaskAuditMockMvc
            .perform(
                patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(taskAuditDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the TaskAudit in the database
        List<TaskAudit> taskAuditList = taskAuditRepository.findAll();
        assertThat(taskAuditList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteTaskAudit() throws Exception {
        // Initialize the database
        taskAuditRepository.saveAndFlush(taskAudit);

        int databaseSizeBeforeDelete = taskAuditRepository.findAll().size();

        // Delete the taskAudit
        restTaskAuditMockMvc
            .perform(delete(ENTITY_API_URL_ID, taskAudit.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<TaskAudit> taskAuditList = taskAuditRepository.findAll();
        assertThat(taskAuditList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
