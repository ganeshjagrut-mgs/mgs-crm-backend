package com.crm.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.crm.IntegrationTest;
import com.crm.domain.PipelineAudit;
import com.crm.repository.PipelineAuditRepository;
import com.crm.service.dto.PipelineAuditDTO;
import com.crm.service.mapper.PipelineAuditMapper;
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
 * Integration tests for the {@link PipelineAuditResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class PipelineAuditResourceIT {

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

    private static final String ENTITY_API_URL = "/api/pipeline-audits";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private PipelineAuditRepository pipelineAuditRepository;

    @Autowired
    private PipelineAuditMapper pipelineAuditMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restPipelineAuditMockMvc;

    private PipelineAudit pipelineAudit;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static PipelineAudit createEntity(EntityManager em) {
        PipelineAudit pipelineAudit = new PipelineAudit()
            .eventTimestamp(DEFAULT_EVENT_TIMESTAMP)
            .action(DEFAULT_ACTION)
            .rowId(DEFAULT_ROW_ID)
            .changes(DEFAULT_CHANGES)
            .correlationId(DEFAULT_CORRELATION_ID);
        return pipelineAudit;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static PipelineAudit createUpdatedEntity(EntityManager em) {
        PipelineAudit pipelineAudit = new PipelineAudit()
            .eventTimestamp(UPDATED_EVENT_TIMESTAMP)
            .action(UPDATED_ACTION)
            .rowId(UPDATED_ROW_ID)
            .changes(UPDATED_CHANGES)
            .correlationId(UPDATED_CORRELATION_ID);
        return pipelineAudit;
    }

    @BeforeEach
    public void initTest() {
        pipelineAudit = createEntity(em);
    }

    @Test
    @Transactional
    void createPipelineAudit() throws Exception {
        int databaseSizeBeforeCreate = pipelineAuditRepository.findAll().size();
        // Create the PipelineAudit
        PipelineAuditDTO pipelineAuditDTO = pipelineAuditMapper.toDto(pipelineAudit);
        restPipelineAuditMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(pipelineAuditDTO))
            )
            .andExpect(status().isCreated());

        // Validate the PipelineAudit in the database
        List<PipelineAudit> pipelineAuditList = pipelineAuditRepository.findAll();
        assertThat(pipelineAuditList).hasSize(databaseSizeBeforeCreate + 1);
        PipelineAudit testPipelineAudit = pipelineAuditList.get(pipelineAuditList.size() - 1);
        assertThat(testPipelineAudit.getEventTimestamp()).isEqualTo(DEFAULT_EVENT_TIMESTAMP);
        assertThat(testPipelineAudit.getAction()).isEqualTo(DEFAULT_ACTION);
        assertThat(testPipelineAudit.getRowId()).isEqualTo(DEFAULT_ROW_ID);
        assertThat(testPipelineAudit.getChanges()).isEqualTo(DEFAULT_CHANGES);
        assertThat(testPipelineAudit.getCorrelationId()).isEqualTo(DEFAULT_CORRELATION_ID);
    }

    @Test
    @Transactional
    void createPipelineAuditWithExistingId() throws Exception {
        // Create the PipelineAudit with an existing ID
        pipelineAudit.setId(1L);
        PipelineAuditDTO pipelineAuditDTO = pipelineAuditMapper.toDto(pipelineAudit);

        int databaseSizeBeforeCreate = pipelineAuditRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restPipelineAuditMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(pipelineAuditDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the PipelineAudit in the database
        List<PipelineAudit> pipelineAuditList = pipelineAuditRepository.findAll();
        assertThat(pipelineAuditList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllPipelineAudits() throws Exception {
        // Initialize the database
        pipelineAuditRepository.saveAndFlush(pipelineAudit);

        // Get all the pipelineAuditList
        restPipelineAuditMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(pipelineAudit.getId().intValue())))
            .andExpect(jsonPath("$.[*].eventTimestamp").value(hasItem(DEFAULT_EVENT_TIMESTAMP.toString())))
            .andExpect(jsonPath("$.[*].action").value(hasItem(DEFAULT_ACTION)))
            .andExpect(jsonPath("$.[*].rowId").value(hasItem(DEFAULT_ROW_ID.toString())))
            .andExpect(jsonPath("$.[*].changes").value(hasItem(DEFAULT_CHANGES.toString())))
            .andExpect(jsonPath("$.[*].correlationId").value(hasItem(DEFAULT_CORRELATION_ID.toString())));
    }

    @Test
    @Transactional
    void getPipelineAudit() throws Exception {
        // Initialize the database
        pipelineAuditRepository.saveAndFlush(pipelineAudit);

        // Get the pipelineAudit
        restPipelineAuditMockMvc
            .perform(get(ENTITY_API_URL_ID, pipelineAudit.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(pipelineAudit.getId().intValue()))
            .andExpect(jsonPath("$.eventTimestamp").value(DEFAULT_EVENT_TIMESTAMP.toString()))
            .andExpect(jsonPath("$.action").value(DEFAULT_ACTION))
            .andExpect(jsonPath("$.rowId").value(DEFAULT_ROW_ID.toString()))
            .andExpect(jsonPath("$.changes").value(DEFAULT_CHANGES.toString()))
            .andExpect(jsonPath("$.correlationId").value(DEFAULT_CORRELATION_ID.toString()));
    }

    @Test
    @Transactional
    void getPipelineAuditsByIdFiltering() throws Exception {
        // Initialize the database
        pipelineAuditRepository.saveAndFlush(pipelineAudit);

        Long id = pipelineAudit.getId();

        defaultPipelineAuditShouldBeFound("id.equals=" + id);
        defaultPipelineAuditShouldNotBeFound("id.notEquals=" + id);

        defaultPipelineAuditShouldBeFound("id.greaterThanOrEqual=" + id);
        defaultPipelineAuditShouldNotBeFound("id.greaterThan=" + id);

        defaultPipelineAuditShouldBeFound("id.lessThanOrEqual=" + id);
        defaultPipelineAuditShouldNotBeFound("id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllPipelineAuditsByEventTimestampIsEqualToSomething() throws Exception {
        // Initialize the database
        pipelineAuditRepository.saveAndFlush(pipelineAudit);

        // Get all the pipelineAuditList where eventTimestamp equals to DEFAULT_EVENT_TIMESTAMP
        defaultPipelineAuditShouldBeFound("eventTimestamp.equals=" + DEFAULT_EVENT_TIMESTAMP);

        // Get all the pipelineAuditList where eventTimestamp equals to UPDATED_EVENT_TIMESTAMP
        defaultPipelineAuditShouldNotBeFound("eventTimestamp.equals=" + UPDATED_EVENT_TIMESTAMP);
    }

    @Test
    @Transactional
    void getAllPipelineAuditsByEventTimestampIsInShouldWork() throws Exception {
        // Initialize the database
        pipelineAuditRepository.saveAndFlush(pipelineAudit);

        // Get all the pipelineAuditList where eventTimestamp in DEFAULT_EVENT_TIMESTAMP or UPDATED_EVENT_TIMESTAMP
        defaultPipelineAuditShouldBeFound("eventTimestamp.in=" + DEFAULT_EVENT_TIMESTAMP + "," + UPDATED_EVENT_TIMESTAMP);

        // Get all the pipelineAuditList where eventTimestamp equals to UPDATED_EVENT_TIMESTAMP
        defaultPipelineAuditShouldNotBeFound("eventTimestamp.in=" + UPDATED_EVENT_TIMESTAMP);
    }

    @Test
    @Transactional
    void getAllPipelineAuditsByEventTimestampIsNullOrNotNull() throws Exception {
        // Initialize the database
        pipelineAuditRepository.saveAndFlush(pipelineAudit);

        // Get all the pipelineAuditList where eventTimestamp is not null
        defaultPipelineAuditShouldBeFound("eventTimestamp.specified=true");

        // Get all the pipelineAuditList where eventTimestamp is null
        defaultPipelineAuditShouldNotBeFound("eventTimestamp.specified=false");
    }

    @Test
    @Transactional
    void getAllPipelineAuditsByActionIsEqualToSomething() throws Exception {
        // Initialize the database
        pipelineAuditRepository.saveAndFlush(pipelineAudit);

        // Get all the pipelineAuditList where action equals to DEFAULT_ACTION
        defaultPipelineAuditShouldBeFound("action.equals=" + DEFAULT_ACTION);

        // Get all the pipelineAuditList where action equals to UPDATED_ACTION
        defaultPipelineAuditShouldNotBeFound("action.equals=" + UPDATED_ACTION);
    }

    @Test
    @Transactional
    void getAllPipelineAuditsByActionIsInShouldWork() throws Exception {
        // Initialize the database
        pipelineAuditRepository.saveAndFlush(pipelineAudit);

        // Get all the pipelineAuditList where action in DEFAULT_ACTION or UPDATED_ACTION
        defaultPipelineAuditShouldBeFound("action.in=" + DEFAULT_ACTION + "," + UPDATED_ACTION);

        // Get all the pipelineAuditList where action equals to UPDATED_ACTION
        defaultPipelineAuditShouldNotBeFound("action.in=" + UPDATED_ACTION);
    }

    @Test
    @Transactional
    void getAllPipelineAuditsByActionIsNullOrNotNull() throws Exception {
        // Initialize the database
        pipelineAuditRepository.saveAndFlush(pipelineAudit);

        // Get all the pipelineAuditList where action is not null
        defaultPipelineAuditShouldBeFound("action.specified=true");

        // Get all the pipelineAuditList where action is null
        defaultPipelineAuditShouldNotBeFound("action.specified=false");
    }

    @Test
    @Transactional
    void getAllPipelineAuditsByActionContainsSomething() throws Exception {
        // Initialize the database
        pipelineAuditRepository.saveAndFlush(pipelineAudit);

        // Get all the pipelineAuditList where action contains DEFAULT_ACTION
        defaultPipelineAuditShouldBeFound("action.contains=" + DEFAULT_ACTION);

        // Get all the pipelineAuditList where action contains UPDATED_ACTION
        defaultPipelineAuditShouldNotBeFound("action.contains=" + UPDATED_ACTION);
    }

    @Test
    @Transactional
    void getAllPipelineAuditsByActionNotContainsSomething() throws Exception {
        // Initialize the database
        pipelineAuditRepository.saveAndFlush(pipelineAudit);

        // Get all the pipelineAuditList where action does not contain DEFAULT_ACTION
        defaultPipelineAuditShouldNotBeFound("action.doesNotContain=" + DEFAULT_ACTION);

        // Get all the pipelineAuditList where action does not contain UPDATED_ACTION
        defaultPipelineAuditShouldBeFound("action.doesNotContain=" + UPDATED_ACTION);
    }

    @Test
    @Transactional
    void getAllPipelineAuditsByRowIdIsEqualToSomething() throws Exception {
        // Initialize the database
        pipelineAuditRepository.saveAndFlush(pipelineAudit);

        // Get all the pipelineAuditList where rowId equals to DEFAULT_ROW_ID
        defaultPipelineAuditShouldBeFound("rowId.equals=" + DEFAULT_ROW_ID);

        // Get all the pipelineAuditList where rowId equals to UPDATED_ROW_ID
        defaultPipelineAuditShouldNotBeFound("rowId.equals=" + UPDATED_ROW_ID);
    }

    @Test
    @Transactional
    void getAllPipelineAuditsByRowIdIsInShouldWork() throws Exception {
        // Initialize the database
        pipelineAuditRepository.saveAndFlush(pipelineAudit);

        // Get all the pipelineAuditList where rowId in DEFAULT_ROW_ID or UPDATED_ROW_ID
        defaultPipelineAuditShouldBeFound("rowId.in=" + DEFAULT_ROW_ID + "," + UPDATED_ROW_ID);

        // Get all the pipelineAuditList where rowId equals to UPDATED_ROW_ID
        defaultPipelineAuditShouldNotBeFound("rowId.in=" + UPDATED_ROW_ID);
    }

    @Test
    @Transactional
    void getAllPipelineAuditsByRowIdIsNullOrNotNull() throws Exception {
        // Initialize the database
        pipelineAuditRepository.saveAndFlush(pipelineAudit);

        // Get all the pipelineAuditList where rowId is not null
        defaultPipelineAuditShouldBeFound("rowId.specified=true");

        // Get all the pipelineAuditList where rowId is null
        defaultPipelineAuditShouldNotBeFound("rowId.specified=false");
    }

    @Test
    @Transactional
    void getAllPipelineAuditsByCorrelationIdIsEqualToSomething() throws Exception {
        // Initialize the database
        pipelineAuditRepository.saveAndFlush(pipelineAudit);

        // Get all the pipelineAuditList where correlationId equals to DEFAULT_CORRELATION_ID
        defaultPipelineAuditShouldBeFound("correlationId.equals=" + DEFAULT_CORRELATION_ID);

        // Get all the pipelineAuditList where correlationId equals to UPDATED_CORRELATION_ID
        defaultPipelineAuditShouldNotBeFound("correlationId.equals=" + UPDATED_CORRELATION_ID);
    }

    @Test
    @Transactional
    void getAllPipelineAuditsByCorrelationIdIsInShouldWork() throws Exception {
        // Initialize the database
        pipelineAuditRepository.saveAndFlush(pipelineAudit);

        // Get all the pipelineAuditList where correlationId in DEFAULT_CORRELATION_ID or UPDATED_CORRELATION_ID
        defaultPipelineAuditShouldBeFound("correlationId.in=" + DEFAULT_CORRELATION_ID + "," + UPDATED_CORRELATION_ID);

        // Get all the pipelineAuditList where correlationId equals to UPDATED_CORRELATION_ID
        defaultPipelineAuditShouldNotBeFound("correlationId.in=" + UPDATED_CORRELATION_ID);
    }

    @Test
    @Transactional
    void getAllPipelineAuditsByCorrelationIdIsNullOrNotNull() throws Exception {
        // Initialize the database
        pipelineAuditRepository.saveAndFlush(pipelineAudit);

        // Get all the pipelineAuditList where correlationId is not null
        defaultPipelineAuditShouldBeFound("correlationId.specified=true");

        // Get all the pipelineAuditList where correlationId is null
        defaultPipelineAuditShouldNotBeFound("correlationId.specified=false");
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultPipelineAuditShouldBeFound(String filter) throws Exception {
        restPipelineAuditMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(pipelineAudit.getId().intValue())))
            .andExpect(jsonPath("$.[*].eventTimestamp").value(hasItem(DEFAULT_EVENT_TIMESTAMP.toString())))
            .andExpect(jsonPath("$.[*].action").value(hasItem(DEFAULT_ACTION)))
            .andExpect(jsonPath("$.[*].rowId").value(hasItem(DEFAULT_ROW_ID.toString())))
            .andExpect(jsonPath("$.[*].changes").value(hasItem(DEFAULT_CHANGES.toString())))
            .andExpect(jsonPath("$.[*].correlationId").value(hasItem(DEFAULT_CORRELATION_ID.toString())));

        // Check, that the count call also returns 1
        restPipelineAuditMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultPipelineAuditShouldNotBeFound(String filter) throws Exception {
        restPipelineAuditMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restPipelineAuditMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingPipelineAudit() throws Exception {
        // Get the pipelineAudit
        restPipelineAuditMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingPipelineAudit() throws Exception {
        // Initialize the database
        pipelineAuditRepository.saveAndFlush(pipelineAudit);

        int databaseSizeBeforeUpdate = pipelineAuditRepository.findAll().size();

        // Update the pipelineAudit
        PipelineAudit updatedPipelineAudit = pipelineAuditRepository.findById(pipelineAudit.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedPipelineAudit are not directly saved in db
        em.detach(updatedPipelineAudit);
        updatedPipelineAudit
            .eventTimestamp(UPDATED_EVENT_TIMESTAMP)
            .action(UPDATED_ACTION)
            .rowId(UPDATED_ROW_ID)
            .changes(UPDATED_CHANGES)
            .correlationId(UPDATED_CORRELATION_ID);
        PipelineAuditDTO pipelineAuditDTO = pipelineAuditMapper.toDto(updatedPipelineAudit);

        restPipelineAuditMockMvc
            .perform(
                put(ENTITY_API_URL_ID, pipelineAuditDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(pipelineAuditDTO))
            )
            .andExpect(status().isOk());

        // Validate the PipelineAudit in the database
        List<PipelineAudit> pipelineAuditList = pipelineAuditRepository.findAll();
        assertThat(pipelineAuditList).hasSize(databaseSizeBeforeUpdate);
        PipelineAudit testPipelineAudit = pipelineAuditList.get(pipelineAuditList.size() - 1);
        assertThat(testPipelineAudit.getEventTimestamp()).isEqualTo(UPDATED_EVENT_TIMESTAMP);
        assertThat(testPipelineAudit.getAction()).isEqualTo(UPDATED_ACTION);
        assertThat(testPipelineAudit.getRowId()).isEqualTo(UPDATED_ROW_ID);
        assertThat(testPipelineAudit.getChanges()).isEqualTo(UPDATED_CHANGES);
        assertThat(testPipelineAudit.getCorrelationId()).isEqualTo(UPDATED_CORRELATION_ID);
    }

    @Test
    @Transactional
    void putNonExistingPipelineAudit() throws Exception {
        int databaseSizeBeforeUpdate = pipelineAuditRepository.findAll().size();
        pipelineAudit.setId(longCount.incrementAndGet());

        // Create the PipelineAudit
        PipelineAuditDTO pipelineAuditDTO = pipelineAuditMapper.toDto(pipelineAudit);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restPipelineAuditMockMvc
            .perform(
                put(ENTITY_API_URL_ID, pipelineAuditDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(pipelineAuditDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the PipelineAudit in the database
        List<PipelineAudit> pipelineAuditList = pipelineAuditRepository.findAll();
        assertThat(pipelineAuditList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchPipelineAudit() throws Exception {
        int databaseSizeBeforeUpdate = pipelineAuditRepository.findAll().size();
        pipelineAudit.setId(longCount.incrementAndGet());

        // Create the PipelineAudit
        PipelineAuditDTO pipelineAuditDTO = pipelineAuditMapper.toDto(pipelineAudit);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restPipelineAuditMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(pipelineAuditDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the PipelineAudit in the database
        List<PipelineAudit> pipelineAuditList = pipelineAuditRepository.findAll();
        assertThat(pipelineAuditList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamPipelineAudit() throws Exception {
        int databaseSizeBeforeUpdate = pipelineAuditRepository.findAll().size();
        pipelineAudit.setId(longCount.incrementAndGet());

        // Create the PipelineAudit
        PipelineAuditDTO pipelineAuditDTO = pipelineAuditMapper.toDto(pipelineAudit);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restPipelineAuditMockMvc
            .perform(
                put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(pipelineAuditDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the PipelineAudit in the database
        List<PipelineAudit> pipelineAuditList = pipelineAuditRepository.findAll();
        assertThat(pipelineAuditList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdatePipelineAuditWithPatch() throws Exception {
        // Initialize the database
        pipelineAuditRepository.saveAndFlush(pipelineAudit);

        int databaseSizeBeforeUpdate = pipelineAuditRepository.findAll().size();

        // Update the pipelineAudit using partial update
        PipelineAudit partialUpdatedPipelineAudit = new PipelineAudit();
        partialUpdatedPipelineAudit.setId(pipelineAudit.getId());

        partialUpdatedPipelineAudit.action(UPDATED_ACTION).rowId(UPDATED_ROW_ID).changes(UPDATED_CHANGES);

        restPipelineAuditMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedPipelineAudit.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedPipelineAudit))
            )
            .andExpect(status().isOk());

        // Validate the PipelineAudit in the database
        List<PipelineAudit> pipelineAuditList = pipelineAuditRepository.findAll();
        assertThat(pipelineAuditList).hasSize(databaseSizeBeforeUpdate);
        PipelineAudit testPipelineAudit = pipelineAuditList.get(pipelineAuditList.size() - 1);
        assertThat(testPipelineAudit.getEventTimestamp()).isEqualTo(DEFAULT_EVENT_TIMESTAMP);
        assertThat(testPipelineAudit.getAction()).isEqualTo(UPDATED_ACTION);
        assertThat(testPipelineAudit.getRowId()).isEqualTo(UPDATED_ROW_ID);
        assertThat(testPipelineAudit.getChanges()).isEqualTo(UPDATED_CHANGES);
        assertThat(testPipelineAudit.getCorrelationId()).isEqualTo(DEFAULT_CORRELATION_ID);
    }

    @Test
    @Transactional
    void fullUpdatePipelineAuditWithPatch() throws Exception {
        // Initialize the database
        pipelineAuditRepository.saveAndFlush(pipelineAudit);

        int databaseSizeBeforeUpdate = pipelineAuditRepository.findAll().size();

        // Update the pipelineAudit using partial update
        PipelineAudit partialUpdatedPipelineAudit = new PipelineAudit();
        partialUpdatedPipelineAudit.setId(pipelineAudit.getId());

        partialUpdatedPipelineAudit
            .eventTimestamp(UPDATED_EVENT_TIMESTAMP)
            .action(UPDATED_ACTION)
            .rowId(UPDATED_ROW_ID)
            .changes(UPDATED_CHANGES)
            .correlationId(UPDATED_CORRELATION_ID);

        restPipelineAuditMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedPipelineAudit.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedPipelineAudit))
            )
            .andExpect(status().isOk());

        // Validate the PipelineAudit in the database
        List<PipelineAudit> pipelineAuditList = pipelineAuditRepository.findAll();
        assertThat(pipelineAuditList).hasSize(databaseSizeBeforeUpdate);
        PipelineAudit testPipelineAudit = pipelineAuditList.get(pipelineAuditList.size() - 1);
        assertThat(testPipelineAudit.getEventTimestamp()).isEqualTo(UPDATED_EVENT_TIMESTAMP);
        assertThat(testPipelineAudit.getAction()).isEqualTo(UPDATED_ACTION);
        assertThat(testPipelineAudit.getRowId()).isEqualTo(UPDATED_ROW_ID);
        assertThat(testPipelineAudit.getChanges()).isEqualTo(UPDATED_CHANGES);
        assertThat(testPipelineAudit.getCorrelationId()).isEqualTo(UPDATED_CORRELATION_ID);
    }

    @Test
    @Transactional
    void patchNonExistingPipelineAudit() throws Exception {
        int databaseSizeBeforeUpdate = pipelineAuditRepository.findAll().size();
        pipelineAudit.setId(longCount.incrementAndGet());

        // Create the PipelineAudit
        PipelineAuditDTO pipelineAuditDTO = pipelineAuditMapper.toDto(pipelineAudit);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restPipelineAuditMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, pipelineAuditDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(pipelineAuditDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the PipelineAudit in the database
        List<PipelineAudit> pipelineAuditList = pipelineAuditRepository.findAll();
        assertThat(pipelineAuditList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchPipelineAudit() throws Exception {
        int databaseSizeBeforeUpdate = pipelineAuditRepository.findAll().size();
        pipelineAudit.setId(longCount.incrementAndGet());

        // Create the PipelineAudit
        PipelineAuditDTO pipelineAuditDTO = pipelineAuditMapper.toDto(pipelineAudit);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restPipelineAuditMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(pipelineAuditDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the PipelineAudit in the database
        List<PipelineAudit> pipelineAuditList = pipelineAuditRepository.findAll();
        assertThat(pipelineAuditList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamPipelineAudit() throws Exception {
        int databaseSizeBeforeUpdate = pipelineAuditRepository.findAll().size();
        pipelineAudit.setId(longCount.incrementAndGet());

        // Create the PipelineAudit
        PipelineAuditDTO pipelineAuditDTO = pipelineAuditMapper.toDto(pipelineAudit);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restPipelineAuditMockMvc
            .perform(
                patch(ENTITY_API_URL)
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(pipelineAuditDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the PipelineAudit in the database
        List<PipelineAudit> pipelineAuditList = pipelineAuditRepository.findAll();
        assertThat(pipelineAuditList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deletePipelineAudit() throws Exception {
        // Initialize the database
        pipelineAuditRepository.saveAndFlush(pipelineAudit);

        int databaseSizeBeforeDelete = pipelineAuditRepository.findAll().size();

        // Delete the pipelineAudit
        restPipelineAuditMockMvc
            .perform(delete(ENTITY_API_URL_ID, pipelineAudit.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<PipelineAudit> pipelineAuditList = pipelineAuditRepository.findAll();
        assertThat(pipelineAuditList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
