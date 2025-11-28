package com.mgs.web.rest;

import static com.mgs.domain.ReportRunAsserts.*;
import static com.mgs.web.rest.TestUtil.createUpdateProxyForBean;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mgs.IntegrationTest;
import com.mgs.domain.ReportRun;
import com.mgs.domain.ReportTemplate;
import com.mgs.domain.Tenant;
import com.mgs.domain.User;
import com.mgs.repository.ReportRunRepository;
import com.mgs.service.dto.ReportRunDTO;
import com.mgs.service.mapper.ReportRunMapper;
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
 * Integration tests for the {@link ReportRunResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class ReportRunResourceIT {

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_FILTER_JSON = "AAAAAAAAAA";
    private static final String UPDATED_FILTER_JSON = "BBBBBBBBBB";

    private static final String DEFAULT_FORMAT = "AAAAAAAAAA";
    private static final String UPDATED_FORMAT = "BBBBBBBBBB";

    private static final String DEFAULT_FILE_PATH = "AAAAAAAAAA";
    private static final String UPDATED_FILE_PATH = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/report-runs";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ObjectMapper om;

    @Autowired
    private ReportRunRepository reportRunRepository;

    @Autowired
    private ReportRunMapper reportRunMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restReportRunMockMvc;

    private ReportRun reportRun;

    private ReportRun insertedReportRun;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static ReportRun createEntity(EntityManager em) {
        ReportRun reportRun = new ReportRun()
            .name(DEFAULT_NAME)
            .filterJson(DEFAULT_FILTER_JSON)
            .format(DEFAULT_FORMAT)
            .filePath(DEFAULT_FILE_PATH);
        // Add required entity
        Tenant tenant;
        if (TestUtil.findAll(em, Tenant.class).isEmpty()) {
            tenant = TenantResourceIT.createEntity();
            em.persist(tenant);
            em.flush();
        } else {
            tenant = TestUtil.findAll(em, Tenant.class).get(0);
        }
        reportRun.setTenant(tenant);
        // Add required entity
        User user = UserResourceIT.createEntity(em);
        em.persist(user);
        em.flush();
        reportRun.setGeneratedByUser(user);
        return reportRun;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static ReportRun createUpdatedEntity(EntityManager em) {
        ReportRun updatedReportRun = new ReportRun()
            .name(UPDATED_NAME)
            .filterJson(UPDATED_FILTER_JSON)
            .format(UPDATED_FORMAT)
            .filePath(UPDATED_FILE_PATH);
        // Add required entity
        Tenant tenant;
        if (TestUtil.findAll(em, Tenant.class).isEmpty()) {
            tenant = TenantResourceIT.createUpdatedEntity();
            em.persist(tenant);
            em.flush();
        } else {
            tenant = TestUtil.findAll(em, Tenant.class).get(0);
        }
        updatedReportRun.setTenant(tenant);
        // Add required entity
        User user = UserResourceIT.createEntity(em);
        em.persist(user);
        em.flush();
        updatedReportRun.setGeneratedByUser(user);
        return updatedReportRun;
    }

    @BeforeEach
    void initTest() {
        reportRun = createEntity(em);
    }

    @AfterEach
    void cleanup() {
        if (insertedReportRun != null) {
            reportRunRepository.delete(insertedReportRun);
            insertedReportRun = null;
        }
    }

    @Test
    @Transactional
    void createReportRun() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the ReportRun
        ReportRunDTO reportRunDTO = reportRunMapper.toDto(reportRun);
        var returnedReportRunDTO = om.readValue(
            restReportRunMockMvc
                .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(reportRunDTO)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            ReportRunDTO.class
        );

        // Validate the ReportRun in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        var returnedReportRun = reportRunMapper.toEntity(returnedReportRunDTO);
        assertReportRunUpdatableFieldsEquals(returnedReportRun, getPersistedReportRun(returnedReportRun));

        insertedReportRun = returnedReportRun;
    }

    @Test
    @Transactional
    void createReportRunWithExistingId() throws Exception {
        // Create the ReportRun with an existing ID
        reportRun.setId(1L);
        ReportRunDTO reportRunDTO = reportRunMapper.toDto(reportRun);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restReportRunMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(reportRunDTO)))
            .andExpect(status().isBadRequest());

        // Validate the ReportRun in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkNameIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        reportRun.setName(null);

        // Create the ReportRun, which fails.
        ReportRunDTO reportRunDTO = reportRunMapper.toDto(reportRun);

        restReportRunMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(reportRunDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllReportRuns() throws Exception {
        // Initialize the database
        insertedReportRun = reportRunRepository.saveAndFlush(reportRun);

        // Get all the reportRunList
        restReportRunMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(reportRun.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)))
            .andExpect(jsonPath("$.[*].filterJson").value(hasItem(DEFAULT_FILTER_JSON)))
            .andExpect(jsonPath("$.[*].format").value(hasItem(DEFAULT_FORMAT)))
            .andExpect(jsonPath("$.[*].filePath").value(hasItem(DEFAULT_FILE_PATH)));
    }

    @Test
    @Transactional
    void getReportRun() throws Exception {
        // Initialize the database
        insertedReportRun = reportRunRepository.saveAndFlush(reportRun);

        // Get the reportRun
        restReportRunMockMvc
            .perform(get(ENTITY_API_URL_ID, reportRun.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(reportRun.getId().intValue()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME))
            .andExpect(jsonPath("$.filterJson").value(DEFAULT_FILTER_JSON))
            .andExpect(jsonPath("$.format").value(DEFAULT_FORMAT))
            .andExpect(jsonPath("$.filePath").value(DEFAULT_FILE_PATH));
    }

    @Test
    @Transactional
    void getReportRunsByIdFiltering() throws Exception {
        // Initialize the database
        insertedReportRun = reportRunRepository.saveAndFlush(reportRun);

        Long id = reportRun.getId();

        defaultReportRunFiltering("id.equals=" + id, "id.notEquals=" + id);

        defaultReportRunFiltering("id.greaterThanOrEqual=" + id, "id.greaterThan=" + id);

        defaultReportRunFiltering("id.lessThanOrEqual=" + id, "id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllReportRunsByNameIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedReportRun = reportRunRepository.saveAndFlush(reportRun);

        // Get all the reportRunList where name equals to
        defaultReportRunFiltering("name.equals=" + DEFAULT_NAME, "name.equals=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllReportRunsByNameIsInShouldWork() throws Exception {
        // Initialize the database
        insertedReportRun = reportRunRepository.saveAndFlush(reportRun);

        // Get all the reportRunList where name in
        defaultReportRunFiltering("name.in=" + DEFAULT_NAME + "," + UPDATED_NAME, "name.in=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllReportRunsByNameIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedReportRun = reportRunRepository.saveAndFlush(reportRun);

        // Get all the reportRunList where name is not null
        defaultReportRunFiltering("name.specified=true", "name.specified=false");
    }

    @Test
    @Transactional
    void getAllReportRunsByNameContainsSomething() throws Exception {
        // Initialize the database
        insertedReportRun = reportRunRepository.saveAndFlush(reportRun);

        // Get all the reportRunList where name contains
        defaultReportRunFiltering("name.contains=" + DEFAULT_NAME, "name.contains=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllReportRunsByNameNotContainsSomething() throws Exception {
        // Initialize the database
        insertedReportRun = reportRunRepository.saveAndFlush(reportRun);

        // Get all the reportRunList where name does not contain
        defaultReportRunFiltering("name.doesNotContain=" + UPDATED_NAME, "name.doesNotContain=" + DEFAULT_NAME);
    }

    @Test
    @Transactional
    void getAllReportRunsByFilterJsonIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedReportRun = reportRunRepository.saveAndFlush(reportRun);

        // Get all the reportRunList where filterJson equals to
        defaultReportRunFiltering("filterJson.equals=" + DEFAULT_FILTER_JSON, "filterJson.equals=" + UPDATED_FILTER_JSON);
    }

    @Test
    @Transactional
    void getAllReportRunsByFilterJsonIsInShouldWork() throws Exception {
        // Initialize the database
        insertedReportRun = reportRunRepository.saveAndFlush(reportRun);

        // Get all the reportRunList where filterJson in
        defaultReportRunFiltering(
            "filterJson.in=" + DEFAULT_FILTER_JSON + "," + UPDATED_FILTER_JSON,
            "filterJson.in=" + UPDATED_FILTER_JSON
        );
    }

    @Test
    @Transactional
    void getAllReportRunsByFilterJsonIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedReportRun = reportRunRepository.saveAndFlush(reportRun);

        // Get all the reportRunList where filterJson is not null
        defaultReportRunFiltering("filterJson.specified=true", "filterJson.specified=false");
    }

    @Test
    @Transactional
    void getAllReportRunsByFilterJsonContainsSomething() throws Exception {
        // Initialize the database
        insertedReportRun = reportRunRepository.saveAndFlush(reportRun);

        // Get all the reportRunList where filterJson contains
        defaultReportRunFiltering("filterJson.contains=" + DEFAULT_FILTER_JSON, "filterJson.contains=" + UPDATED_FILTER_JSON);
    }

    @Test
    @Transactional
    void getAllReportRunsByFilterJsonNotContainsSomething() throws Exception {
        // Initialize the database
        insertedReportRun = reportRunRepository.saveAndFlush(reportRun);

        // Get all the reportRunList where filterJson does not contain
        defaultReportRunFiltering("filterJson.doesNotContain=" + UPDATED_FILTER_JSON, "filterJson.doesNotContain=" + DEFAULT_FILTER_JSON);
    }

    @Test
    @Transactional
    void getAllReportRunsByFormatIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedReportRun = reportRunRepository.saveAndFlush(reportRun);

        // Get all the reportRunList where format equals to
        defaultReportRunFiltering("format.equals=" + DEFAULT_FORMAT, "format.equals=" + UPDATED_FORMAT);
    }

    @Test
    @Transactional
    void getAllReportRunsByFormatIsInShouldWork() throws Exception {
        // Initialize the database
        insertedReportRun = reportRunRepository.saveAndFlush(reportRun);

        // Get all the reportRunList where format in
        defaultReportRunFiltering("format.in=" + DEFAULT_FORMAT + "," + UPDATED_FORMAT, "format.in=" + UPDATED_FORMAT);
    }

    @Test
    @Transactional
    void getAllReportRunsByFormatIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedReportRun = reportRunRepository.saveAndFlush(reportRun);

        // Get all the reportRunList where format is not null
        defaultReportRunFiltering("format.specified=true", "format.specified=false");
    }

    @Test
    @Transactional
    void getAllReportRunsByFormatContainsSomething() throws Exception {
        // Initialize the database
        insertedReportRun = reportRunRepository.saveAndFlush(reportRun);

        // Get all the reportRunList where format contains
        defaultReportRunFiltering("format.contains=" + DEFAULT_FORMAT, "format.contains=" + UPDATED_FORMAT);
    }

    @Test
    @Transactional
    void getAllReportRunsByFormatNotContainsSomething() throws Exception {
        // Initialize the database
        insertedReportRun = reportRunRepository.saveAndFlush(reportRun);

        // Get all the reportRunList where format does not contain
        defaultReportRunFiltering("format.doesNotContain=" + UPDATED_FORMAT, "format.doesNotContain=" + DEFAULT_FORMAT);
    }

    @Test
    @Transactional
    void getAllReportRunsByFilePathIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedReportRun = reportRunRepository.saveAndFlush(reportRun);

        // Get all the reportRunList where filePath equals to
        defaultReportRunFiltering("filePath.equals=" + DEFAULT_FILE_PATH, "filePath.equals=" + UPDATED_FILE_PATH);
    }

    @Test
    @Transactional
    void getAllReportRunsByFilePathIsInShouldWork() throws Exception {
        // Initialize the database
        insertedReportRun = reportRunRepository.saveAndFlush(reportRun);

        // Get all the reportRunList where filePath in
        defaultReportRunFiltering("filePath.in=" + DEFAULT_FILE_PATH + "," + UPDATED_FILE_PATH, "filePath.in=" + UPDATED_FILE_PATH);
    }

    @Test
    @Transactional
    void getAllReportRunsByFilePathIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedReportRun = reportRunRepository.saveAndFlush(reportRun);

        // Get all the reportRunList where filePath is not null
        defaultReportRunFiltering("filePath.specified=true", "filePath.specified=false");
    }

    @Test
    @Transactional
    void getAllReportRunsByFilePathContainsSomething() throws Exception {
        // Initialize the database
        insertedReportRun = reportRunRepository.saveAndFlush(reportRun);

        // Get all the reportRunList where filePath contains
        defaultReportRunFiltering("filePath.contains=" + DEFAULT_FILE_PATH, "filePath.contains=" + UPDATED_FILE_PATH);
    }

    @Test
    @Transactional
    void getAllReportRunsByFilePathNotContainsSomething() throws Exception {
        // Initialize the database
        insertedReportRun = reportRunRepository.saveAndFlush(reportRun);

        // Get all the reportRunList where filePath does not contain
        defaultReportRunFiltering("filePath.doesNotContain=" + UPDATED_FILE_PATH, "filePath.doesNotContain=" + DEFAULT_FILE_PATH);
    }

    @Test
    @Transactional
    void getAllReportRunsByTenantIsEqualToSomething() throws Exception {
        Tenant tenant;
        if (TestUtil.findAll(em, Tenant.class).isEmpty()) {
            reportRunRepository.saveAndFlush(reportRun);
            tenant = TenantResourceIT.createEntity();
        } else {
            tenant = TestUtil.findAll(em, Tenant.class).get(0);
        }
        em.persist(tenant);
        em.flush();
        reportRun.setTenant(tenant);
        reportRunRepository.saveAndFlush(reportRun);
        Long tenantId = tenant.getId();
        // Get all the reportRunList where tenant equals to tenantId
        defaultReportRunShouldBeFound("tenantId.equals=" + tenantId);

        // Get all the reportRunList where tenant equals to (tenantId + 1)
        defaultReportRunShouldNotBeFound("tenantId.equals=" + (tenantId + 1));
    }

    @Test
    @Transactional
    void getAllReportRunsByTemplateIsEqualToSomething() throws Exception {
        ReportTemplate template;
        if (TestUtil.findAll(em, ReportTemplate.class).isEmpty()) {
            reportRunRepository.saveAndFlush(reportRun);
            template = ReportTemplateResourceIT.createEntity(em);
        } else {
            template = TestUtil.findAll(em, ReportTemplate.class).get(0);
        }
        em.persist(template);
        em.flush();
        reportRun.setTemplate(template);
        reportRunRepository.saveAndFlush(reportRun);
        Long templateId = template.getId();
        // Get all the reportRunList where template equals to templateId
        defaultReportRunShouldBeFound("templateId.equals=" + templateId);

        // Get all the reportRunList where template equals to (templateId + 1)
        defaultReportRunShouldNotBeFound("templateId.equals=" + (templateId + 1));
    }

    @Test
    @Transactional
    void getAllReportRunsByGeneratedByUserIsEqualToSomething() throws Exception {
        User generatedByUser;
        if (TestUtil.findAll(em, User.class).isEmpty()) {
            reportRunRepository.saveAndFlush(reportRun);
            generatedByUser = UserResourceIT.createEntity(em);
        } else {
            generatedByUser = TestUtil.findAll(em, User.class).get(0);
        }
        em.persist(generatedByUser);
        em.flush();
        reportRun.setGeneratedByUser(generatedByUser);
        reportRunRepository.saveAndFlush(reportRun);
        Long generatedByUserId = generatedByUser.getId();
        // Get all the reportRunList where generatedByUser equals to generatedByUserId
        defaultReportRunShouldBeFound("generatedByUserId.equals=" + generatedByUserId);

        // Get all the reportRunList where generatedByUser equals to (generatedByUserId + 1)
        defaultReportRunShouldNotBeFound("generatedByUserId.equals=" + (generatedByUserId + 1));
    }

    private void defaultReportRunFiltering(String shouldBeFound, String shouldNotBeFound) throws Exception {
        defaultReportRunShouldBeFound(shouldBeFound);
        defaultReportRunShouldNotBeFound(shouldNotBeFound);
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultReportRunShouldBeFound(String filter) throws Exception {
        restReportRunMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(reportRun.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)))
            .andExpect(jsonPath("$.[*].filterJson").value(hasItem(DEFAULT_FILTER_JSON)))
            .andExpect(jsonPath("$.[*].format").value(hasItem(DEFAULT_FORMAT)))
            .andExpect(jsonPath("$.[*].filePath").value(hasItem(DEFAULT_FILE_PATH)));

        // Check, that the count call also returns 1
        restReportRunMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultReportRunShouldNotBeFound(String filter) throws Exception {
        restReportRunMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restReportRunMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingReportRun() throws Exception {
        // Get the reportRun
        restReportRunMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingReportRun() throws Exception {
        // Initialize the database
        insertedReportRun = reportRunRepository.saveAndFlush(reportRun);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the reportRun
        ReportRun updatedReportRun = reportRunRepository.findById(reportRun.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedReportRun are not directly saved in db
        em.detach(updatedReportRun);
        updatedReportRun.name(UPDATED_NAME).filterJson(UPDATED_FILTER_JSON).format(UPDATED_FORMAT).filePath(UPDATED_FILE_PATH);
        ReportRunDTO reportRunDTO = reportRunMapper.toDto(updatedReportRun);

        restReportRunMockMvc
            .perform(
                put(ENTITY_API_URL_ID, reportRunDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(reportRunDTO))
            )
            .andExpect(status().isOk());

        // Validate the ReportRun in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedReportRunToMatchAllProperties(updatedReportRun);
    }

    @Test
    @Transactional
    void putNonExistingReportRun() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        reportRun.setId(longCount.incrementAndGet());

        // Create the ReportRun
        ReportRunDTO reportRunDTO = reportRunMapper.toDto(reportRun);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restReportRunMockMvc
            .perform(
                put(ENTITY_API_URL_ID, reportRunDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(reportRunDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the ReportRun in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchReportRun() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        reportRun.setId(longCount.incrementAndGet());

        // Create the ReportRun
        ReportRunDTO reportRunDTO = reportRunMapper.toDto(reportRun);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restReportRunMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(reportRunDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the ReportRun in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamReportRun() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        reportRun.setId(longCount.incrementAndGet());

        // Create the ReportRun
        ReportRunDTO reportRunDTO = reportRunMapper.toDto(reportRun);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restReportRunMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(reportRunDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the ReportRun in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateReportRunWithPatch() throws Exception {
        // Initialize the database
        insertedReportRun = reportRunRepository.saveAndFlush(reportRun);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the reportRun using partial update
        ReportRun partialUpdatedReportRun = new ReportRun();
        partialUpdatedReportRun.setId(reportRun.getId());

        partialUpdatedReportRun.format(UPDATED_FORMAT);

        restReportRunMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedReportRun.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedReportRun))
            )
            .andExpect(status().isOk());

        // Validate the ReportRun in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertReportRunUpdatableFieldsEquals(
            createUpdateProxyForBean(partialUpdatedReportRun, reportRun),
            getPersistedReportRun(reportRun)
        );
    }

    @Test
    @Transactional
    void fullUpdateReportRunWithPatch() throws Exception {
        // Initialize the database
        insertedReportRun = reportRunRepository.saveAndFlush(reportRun);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the reportRun using partial update
        ReportRun partialUpdatedReportRun = new ReportRun();
        partialUpdatedReportRun.setId(reportRun.getId());

        partialUpdatedReportRun.name(UPDATED_NAME).filterJson(UPDATED_FILTER_JSON).format(UPDATED_FORMAT).filePath(UPDATED_FILE_PATH);

        restReportRunMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedReportRun.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedReportRun))
            )
            .andExpect(status().isOk());

        // Validate the ReportRun in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertReportRunUpdatableFieldsEquals(partialUpdatedReportRun, getPersistedReportRun(partialUpdatedReportRun));
    }

    @Test
    @Transactional
    void patchNonExistingReportRun() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        reportRun.setId(longCount.incrementAndGet());

        // Create the ReportRun
        ReportRunDTO reportRunDTO = reportRunMapper.toDto(reportRun);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restReportRunMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, reportRunDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(reportRunDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the ReportRun in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchReportRun() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        reportRun.setId(longCount.incrementAndGet());

        // Create the ReportRun
        ReportRunDTO reportRunDTO = reportRunMapper.toDto(reportRun);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restReportRunMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(reportRunDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the ReportRun in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamReportRun() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        reportRun.setId(longCount.incrementAndGet());

        // Create the ReportRun
        ReportRunDTO reportRunDTO = reportRunMapper.toDto(reportRun);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restReportRunMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(om.writeValueAsBytes(reportRunDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the ReportRun in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteReportRun() throws Exception {
        // Initialize the database
        insertedReportRun = reportRunRepository.saveAndFlush(reportRun);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the reportRun
        restReportRunMockMvc
            .perform(delete(ENTITY_API_URL_ID, reportRun.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return reportRunRepository.count();
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

    protected ReportRun getPersistedReportRun(ReportRun reportRun) {
        return reportRunRepository.findById(reportRun.getId()).orElseThrow();
    }

    protected void assertPersistedReportRunToMatchAllProperties(ReportRun expectedReportRun) {
        assertReportRunAllPropertiesEquals(expectedReportRun, getPersistedReportRun(expectedReportRun));
    }

    protected void assertPersistedReportRunToMatchUpdatableProperties(ReportRun expectedReportRun) {
        assertReportRunAllUpdatablePropertiesEquals(expectedReportRun, getPersistedReportRun(expectedReportRun));
    }
}
