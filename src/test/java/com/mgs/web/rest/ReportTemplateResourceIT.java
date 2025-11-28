package com.mgs.web.rest;

import static com.mgs.domain.ReportTemplateAsserts.*;
import static com.mgs.web.rest.TestUtil.createUpdateProxyForBean;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mgs.IntegrationTest;
import com.mgs.domain.ReportTemplate;
import com.mgs.domain.Tenant;
import com.mgs.repository.ReportTemplateRepository;
import com.mgs.service.dto.ReportTemplateDTO;
import com.mgs.service.mapper.ReportTemplateMapper;
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
 * Integration tests for the {@link ReportTemplateResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class ReportTemplateResourceIT {

    private static final String DEFAULT_CODE = "AAAAAAAAAA";
    private static final String UPDATED_CODE = "BBBBBBBBBB";

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_DESCRIPTION = "AAAAAAAAAA";
    private static final String UPDATED_DESCRIPTION = "BBBBBBBBBB";

    private static final String DEFAULT_CONFIG_JSON = "AAAAAAAAAA";
    private static final String UPDATED_CONFIG_JSON = "BBBBBBBBBB";

    private static final Boolean DEFAULT_IS_ACTIVE = false;
    private static final Boolean UPDATED_IS_ACTIVE = true;

    private static final String ENTITY_API_URL = "/api/report-templates";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ObjectMapper om;

    @Autowired
    private ReportTemplateRepository reportTemplateRepository;

    @Autowired
    private ReportTemplateMapper reportTemplateMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restReportTemplateMockMvc;

    private ReportTemplate reportTemplate;

    private ReportTemplate insertedReportTemplate;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static ReportTemplate createEntity(EntityManager em) {
        ReportTemplate reportTemplate = new ReportTemplate()
            .code(DEFAULT_CODE)
            .name(DEFAULT_NAME)
            .description(DEFAULT_DESCRIPTION)
            .configJson(DEFAULT_CONFIG_JSON)
            .isActive(DEFAULT_IS_ACTIVE);
        // Add required entity
        Tenant tenant;
        if (TestUtil.findAll(em, Tenant.class).isEmpty()) {
            tenant = TenantResourceIT.createEntity();
            em.persist(tenant);
            em.flush();
        } else {
            tenant = TestUtil.findAll(em, Tenant.class).get(0);
        }
        reportTemplate.setTenant(tenant);
        return reportTemplate;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static ReportTemplate createUpdatedEntity(EntityManager em) {
        ReportTemplate updatedReportTemplate = new ReportTemplate()
            .code(UPDATED_CODE)
            .name(UPDATED_NAME)
            .description(UPDATED_DESCRIPTION)
            .configJson(UPDATED_CONFIG_JSON)
            .isActive(UPDATED_IS_ACTIVE);
        // Add required entity
        Tenant tenant;
        if (TestUtil.findAll(em, Tenant.class).isEmpty()) {
            tenant = TenantResourceIT.createUpdatedEntity();
            em.persist(tenant);
            em.flush();
        } else {
            tenant = TestUtil.findAll(em, Tenant.class).get(0);
        }
        updatedReportTemplate.setTenant(tenant);
        return updatedReportTemplate;
    }

    @BeforeEach
    void initTest() {
        reportTemplate = createEntity(em);
    }

    @AfterEach
    void cleanup() {
        if (insertedReportTemplate != null) {
            reportTemplateRepository.delete(insertedReportTemplate);
            insertedReportTemplate = null;
        }
    }

    @Test
    @Transactional
    void createReportTemplate() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the ReportTemplate
        ReportTemplateDTO reportTemplateDTO = reportTemplateMapper.toDto(reportTemplate);
        var returnedReportTemplateDTO = om.readValue(
            restReportTemplateMockMvc
                .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(reportTemplateDTO)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            ReportTemplateDTO.class
        );

        // Validate the ReportTemplate in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        var returnedReportTemplate = reportTemplateMapper.toEntity(returnedReportTemplateDTO);
        assertReportTemplateUpdatableFieldsEquals(returnedReportTemplate, getPersistedReportTemplate(returnedReportTemplate));

        insertedReportTemplate = returnedReportTemplate;
    }

    @Test
    @Transactional
    void createReportTemplateWithExistingId() throws Exception {
        // Create the ReportTemplate with an existing ID
        reportTemplate.setId(1L);
        ReportTemplateDTO reportTemplateDTO = reportTemplateMapper.toDto(reportTemplate);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restReportTemplateMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(reportTemplateDTO)))
            .andExpect(status().isBadRequest());

        // Validate the ReportTemplate in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllReportTemplates() throws Exception {
        // Initialize the database
        insertedReportTemplate = reportTemplateRepository.saveAndFlush(reportTemplate);

        // Get all the reportTemplateList
        restReportTemplateMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(reportTemplate.getId().intValue())))
            .andExpect(jsonPath("$.[*].code").value(hasItem(DEFAULT_CODE)))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION)))
            .andExpect(jsonPath("$.[*].configJson").value(hasItem(DEFAULT_CONFIG_JSON)))
            .andExpect(jsonPath("$.[*].isActive").value(hasItem(DEFAULT_IS_ACTIVE)));
    }

    @Test
    @Transactional
    void getReportTemplate() throws Exception {
        // Initialize the database
        insertedReportTemplate = reportTemplateRepository.saveAndFlush(reportTemplate);

        // Get the reportTemplate
        restReportTemplateMockMvc
            .perform(get(ENTITY_API_URL_ID, reportTemplate.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(reportTemplate.getId().intValue()))
            .andExpect(jsonPath("$.code").value(DEFAULT_CODE))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME))
            .andExpect(jsonPath("$.description").value(DEFAULT_DESCRIPTION))
            .andExpect(jsonPath("$.configJson").value(DEFAULT_CONFIG_JSON))
            .andExpect(jsonPath("$.isActive").value(DEFAULT_IS_ACTIVE));
    }

    @Test
    @Transactional
    void getReportTemplatesByIdFiltering() throws Exception {
        // Initialize the database
        insertedReportTemplate = reportTemplateRepository.saveAndFlush(reportTemplate);

        Long id = reportTemplate.getId();

        defaultReportTemplateFiltering("id.equals=" + id, "id.notEquals=" + id);

        defaultReportTemplateFiltering("id.greaterThanOrEqual=" + id, "id.greaterThan=" + id);

        defaultReportTemplateFiltering("id.lessThanOrEqual=" + id, "id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllReportTemplatesByCodeIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedReportTemplate = reportTemplateRepository.saveAndFlush(reportTemplate);

        // Get all the reportTemplateList where code equals to
        defaultReportTemplateFiltering("code.equals=" + DEFAULT_CODE, "code.equals=" + UPDATED_CODE);
    }

    @Test
    @Transactional
    void getAllReportTemplatesByCodeIsInShouldWork() throws Exception {
        // Initialize the database
        insertedReportTemplate = reportTemplateRepository.saveAndFlush(reportTemplate);

        // Get all the reportTemplateList where code in
        defaultReportTemplateFiltering("code.in=" + DEFAULT_CODE + "," + UPDATED_CODE, "code.in=" + UPDATED_CODE);
    }

    @Test
    @Transactional
    void getAllReportTemplatesByCodeIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedReportTemplate = reportTemplateRepository.saveAndFlush(reportTemplate);

        // Get all the reportTemplateList where code is not null
        defaultReportTemplateFiltering("code.specified=true", "code.specified=false");
    }

    @Test
    @Transactional
    void getAllReportTemplatesByCodeContainsSomething() throws Exception {
        // Initialize the database
        insertedReportTemplate = reportTemplateRepository.saveAndFlush(reportTemplate);

        // Get all the reportTemplateList where code contains
        defaultReportTemplateFiltering("code.contains=" + DEFAULT_CODE, "code.contains=" + UPDATED_CODE);
    }

    @Test
    @Transactional
    void getAllReportTemplatesByCodeNotContainsSomething() throws Exception {
        // Initialize the database
        insertedReportTemplate = reportTemplateRepository.saveAndFlush(reportTemplate);

        // Get all the reportTemplateList where code does not contain
        defaultReportTemplateFiltering("code.doesNotContain=" + UPDATED_CODE, "code.doesNotContain=" + DEFAULT_CODE);
    }

    @Test
    @Transactional
    void getAllReportTemplatesByNameIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedReportTemplate = reportTemplateRepository.saveAndFlush(reportTemplate);

        // Get all the reportTemplateList where name equals to
        defaultReportTemplateFiltering("name.equals=" + DEFAULT_NAME, "name.equals=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllReportTemplatesByNameIsInShouldWork() throws Exception {
        // Initialize the database
        insertedReportTemplate = reportTemplateRepository.saveAndFlush(reportTemplate);

        // Get all the reportTemplateList where name in
        defaultReportTemplateFiltering("name.in=" + DEFAULT_NAME + "," + UPDATED_NAME, "name.in=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllReportTemplatesByNameIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedReportTemplate = reportTemplateRepository.saveAndFlush(reportTemplate);

        // Get all the reportTemplateList where name is not null
        defaultReportTemplateFiltering("name.specified=true", "name.specified=false");
    }

    @Test
    @Transactional
    void getAllReportTemplatesByNameContainsSomething() throws Exception {
        // Initialize the database
        insertedReportTemplate = reportTemplateRepository.saveAndFlush(reportTemplate);

        // Get all the reportTemplateList where name contains
        defaultReportTemplateFiltering("name.contains=" + DEFAULT_NAME, "name.contains=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllReportTemplatesByNameNotContainsSomething() throws Exception {
        // Initialize the database
        insertedReportTemplate = reportTemplateRepository.saveAndFlush(reportTemplate);

        // Get all the reportTemplateList where name does not contain
        defaultReportTemplateFiltering("name.doesNotContain=" + UPDATED_NAME, "name.doesNotContain=" + DEFAULT_NAME);
    }

    @Test
    @Transactional
    void getAllReportTemplatesByDescriptionIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedReportTemplate = reportTemplateRepository.saveAndFlush(reportTemplate);

        // Get all the reportTemplateList where description equals to
        defaultReportTemplateFiltering("description.equals=" + DEFAULT_DESCRIPTION, "description.equals=" + UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    void getAllReportTemplatesByDescriptionIsInShouldWork() throws Exception {
        // Initialize the database
        insertedReportTemplate = reportTemplateRepository.saveAndFlush(reportTemplate);

        // Get all the reportTemplateList where description in
        defaultReportTemplateFiltering(
            "description.in=" + DEFAULT_DESCRIPTION + "," + UPDATED_DESCRIPTION,
            "description.in=" + UPDATED_DESCRIPTION
        );
    }

    @Test
    @Transactional
    void getAllReportTemplatesByDescriptionIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedReportTemplate = reportTemplateRepository.saveAndFlush(reportTemplate);

        // Get all the reportTemplateList where description is not null
        defaultReportTemplateFiltering("description.specified=true", "description.specified=false");
    }

    @Test
    @Transactional
    void getAllReportTemplatesByDescriptionContainsSomething() throws Exception {
        // Initialize the database
        insertedReportTemplate = reportTemplateRepository.saveAndFlush(reportTemplate);

        // Get all the reportTemplateList where description contains
        defaultReportTemplateFiltering("description.contains=" + DEFAULT_DESCRIPTION, "description.contains=" + UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    void getAllReportTemplatesByDescriptionNotContainsSomething() throws Exception {
        // Initialize the database
        insertedReportTemplate = reportTemplateRepository.saveAndFlush(reportTemplate);

        // Get all the reportTemplateList where description does not contain
        defaultReportTemplateFiltering(
            "description.doesNotContain=" + UPDATED_DESCRIPTION,
            "description.doesNotContain=" + DEFAULT_DESCRIPTION
        );
    }

    @Test
    @Transactional
    void getAllReportTemplatesByConfigJsonIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedReportTemplate = reportTemplateRepository.saveAndFlush(reportTemplate);

        // Get all the reportTemplateList where configJson equals to
        defaultReportTemplateFiltering("configJson.equals=" + DEFAULT_CONFIG_JSON, "configJson.equals=" + UPDATED_CONFIG_JSON);
    }

    @Test
    @Transactional
    void getAllReportTemplatesByConfigJsonIsInShouldWork() throws Exception {
        // Initialize the database
        insertedReportTemplate = reportTemplateRepository.saveAndFlush(reportTemplate);

        // Get all the reportTemplateList where configJson in
        defaultReportTemplateFiltering(
            "configJson.in=" + DEFAULT_CONFIG_JSON + "," + UPDATED_CONFIG_JSON,
            "configJson.in=" + UPDATED_CONFIG_JSON
        );
    }

    @Test
    @Transactional
    void getAllReportTemplatesByConfigJsonIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedReportTemplate = reportTemplateRepository.saveAndFlush(reportTemplate);

        // Get all the reportTemplateList where configJson is not null
        defaultReportTemplateFiltering("configJson.specified=true", "configJson.specified=false");
    }

    @Test
    @Transactional
    void getAllReportTemplatesByConfigJsonContainsSomething() throws Exception {
        // Initialize the database
        insertedReportTemplate = reportTemplateRepository.saveAndFlush(reportTemplate);

        // Get all the reportTemplateList where configJson contains
        defaultReportTemplateFiltering("configJson.contains=" + DEFAULT_CONFIG_JSON, "configJson.contains=" + UPDATED_CONFIG_JSON);
    }

    @Test
    @Transactional
    void getAllReportTemplatesByConfigJsonNotContainsSomething() throws Exception {
        // Initialize the database
        insertedReportTemplate = reportTemplateRepository.saveAndFlush(reportTemplate);

        // Get all the reportTemplateList where configJson does not contain
        defaultReportTemplateFiltering(
            "configJson.doesNotContain=" + UPDATED_CONFIG_JSON,
            "configJson.doesNotContain=" + DEFAULT_CONFIG_JSON
        );
    }

    @Test
    @Transactional
    void getAllReportTemplatesByIsActiveIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedReportTemplate = reportTemplateRepository.saveAndFlush(reportTemplate);

        // Get all the reportTemplateList where isActive equals to
        defaultReportTemplateFiltering("isActive.equals=" + DEFAULT_IS_ACTIVE, "isActive.equals=" + UPDATED_IS_ACTIVE);
    }

    @Test
    @Transactional
    void getAllReportTemplatesByIsActiveIsInShouldWork() throws Exception {
        // Initialize the database
        insertedReportTemplate = reportTemplateRepository.saveAndFlush(reportTemplate);

        // Get all the reportTemplateList where isActive in
        defaultReportTemplateFiltering("isActive.in=" + DEFAULT_IS_ACTIVE + "," + UPDATED_IS_ACTIVE, "isActive.in=" + UPDATED_IS_ACTIVE);
    }

    @Test
    @Transactional
    void getAllReportTemplatesByIsActiveIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedReportTemplate = reportTemplateRepository.saveAndFlush(reportTemplate);

        // Get all the reportTemplateList where isActive is not null
        defaultReportTemplateFiltering("isActive.specified=true", "isActive.specified=false");
    }

    @Test
    @Transactional
    void getAllReportTemplatesByTenantIsEqualToSomething() throws Exception {
        Tenant tenant;
        if (TestUtil.findAll(em, Tenant.class).isEmpty()) {
            reportTemplateRepository.saveAndFlush(reportTemplate);
            tenant = TenantResourceIT.createEntity();
        } else {
            tenant = TestUtil.findAll(em, Tenant.class).get(0);
        }
        em.persist(tenant);
        em.flush();
        reportTemplate.setTenant(tenant);
        reportTemplateRepository.saveAndFlush(reportTemplate);
        Long tenantId = tenant.getId();
        // Get all the reportTemplateList where tenant equals to tenantId
        defaultReportTemplateShouldBeFound("tenantId.equals=" + tenantId);

        // Get all the reportTemplateList where tenant equals to (tenantId + 1)
        defaultReportTemplateShouldNotBeFound("tenantId.equals=" + (tenantId + 1));
    }

    private void defaultReportTemplateFiltering(String shouldBeFound, String shouldNotBeFound) throws Exception {
        defaultReportTemplateShouldBeFound(shouldBeFound);
        defaultReportTemplateShouldNotBeFound(shouldNotBeFound);
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultReportTemplateShouldBeFound(String filter) throws Exception {
        restReportTemplateMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(reportTemplate.getId().intValue())))
            .andExpect(jsonPath("$.[*].code").value(hasItem(DEFAULT_CODE)))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION)))
            .andExpect(jsonPath("$.[*].configJson").value(hasItem(DEFAULT_CONFIG_JSON)))
            .andExpect(jsonPath("$.[*].isActive").value(hasItem(DEFAULT_IS_ACTIVE)));

        // Check, that the count call also returns 1
        restReportTemplateMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultReportTemplateShouldNotBeFound(String filter) throws Exception {
        restReportTemplateMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restReportTemplateMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingReportTemplate() throws Exception {
        // Get the reportTemplate
        restReportTemplateMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingReportTemplate() throws Exception {
        // Initialize the database
        insertedReportTemplate = reportTemplateRepository.saveAndFlush(reportTemplate);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the reportTemplate
        ReportTemplate updatedReportTemplate = reportTemplateRepository.findById(reportTemplate.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedReportTemplate are not directly saved in db
        em.detach(updatedReportTemplate);
        updatedReportTemplate
            .code(UPDATED_CODE)
            .name(UPDATED_NAME)
            .description(UPDATED_DESCRIPTION)
            .configJson(UPDATED_CONFIG_JSON)
            .isActive(UPDATED_IS_ACTIVE);
        ReportTemplateDTO reportTemplateDTO = reportTemplateMapper.toDto(updatedReportTemplate);

        restReportTemplateMockMvc
            .perform(
                put(ENTITY_API_URL_ID, reportTemplateDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(reportTemplateDTO))
            )
            .andExpect(status().isOk());

        // Validate the ReportTemplate in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedReportTemplateToMatchAllProperties(updatedReportTemplate);
    }

    @Test
    @Transactional
    void putNonExistingReportTemplate() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        reportTemplate.setId(longCount.incrementAndGet());

        // Create the ReportTemplate
        ReportTemplateDTO reportTemplateDTO = reportTemplateMapper.toDto(reportTemplate);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restReportTemplateMockMvc
            .perform(
                put(ENTITY_API_URL_ID, reportTemplateDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(reportTemplateDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the ReportTemplate in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchReportTemplate() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        reportTemplate.setId(longCount.incrementAndGet());

        // Create the ReportTemplate
        ReportTemplateDTO reportTemplateDTO = reportTemplateMapper.toDto(reportTemplate);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restReportTemplateMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(reportTemplateDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the ReportTemplate in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamReportTemplate() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        reportTemplate.setId(longCount.incrementAndGet());

        // Create the ReportTemplate
        ReportTemplateDTO reportTemplateDTO = reportTemplateMapper.toDto(reportTemplate);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restReportTemplateMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(reportTemplateDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the ReportTemplate in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateReportTemplateWithPatch() throws Exception {
        // Initialize the database
        insertedReportTemplate = reportTemplateRepository.saveAndFlush(reportTemplate);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the reportTemplate using partial update
        ReportTemplate partialUpdatedReportTemplate = new ReportTemplate();
        partialUpdatedReportTemplate.setId(reportTemplate.getId());

        partialUpdatedReportTemplate.code(UPDATED_CODE);

        restReportTemplateMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedReportTemplate.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedReportTemplate))
            )
            .andExpect(status().isOk());

        // Validate the ReportTemplate in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertReportTemplateUpdatableFieldsEquals(
            createUpdateProxyForBean(partialUpdatedReportTemplate, reportTemplate),
            getPersistedReportTemplate(reportTemplate)
        );
    }

    @Test
    @Transactional
    void fullUpdateReportTemplateWithPatch() throws Exception {
        // Initialize the database
        insertedReportTemplate = reportTemplateRepository.saveAndFlush(reportTemplate);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the reportTemplate using partial update
        ReportTemplate partialUpdatedReportTemplate = new ReportTemplate();
        partialUpdatedReportTemplate.setId(reportTemplate.getId());

        partialUpdatedReportTemplate
            .code(UPDATED_CODE)
            .name(UPDATED_NAME)
            .description(UPDATED_DESCRIPTION)
            .configJson(UPDATED_CONFIG_JSON)
            .isActive(UPDATED_IS_ACTIVE);

        restReportTemplateMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedReportTemplate.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedReportTemplate))
            )
            .andExpect(status().isOk());

        // Validate the ReportTemplate in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertReportTemplateUpdatableFieldsEquals(partialUpdatedReportTemplate, getPersistedReportTemplate(partialUpdatedReportTemplate));
    }

    @Test
    @Transactional
    void patchNonExistingReportTemplate() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        reportTemplate.setId(longCount.incrementAndGet());

        // Create the ReportTemplate
        ReportTemplateDTO reportTemplateDTO = reportTemplateMapper.toDto(reportTemplate);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restReportTemplateMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, reportTemplateDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(reportTemplateDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the ReportTemplate in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchReportTemplate() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        reportTemplate.setId(longCount.incrementAndGet());

        // Create the ReportTemplate
        ReportTemplateDTO reportTemplateDTO = reportTemplateMapper.toDto(reportTemplate);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restReportTemplateMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(reportTemplateDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the ReportTemplate in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamReportTemplate() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        reportTemplate.setId(longCount.incrementAndGet());

        // Create the ReportTemplate
        ReportTemplateDTO reportTemplateDTO = reportTemplateMapper.toDto(reportTemplate);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restReportTemplateMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(om.writeValueAsBytes(reportTemplateDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the ReportTemplate in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteReportTemplate() throws Exception {
        // Initialize the database
        insertedReportTemplate = reportTemplateRepository.saveAndFlush(reportTemplate);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the reportTemplate
        restReportTemplateMockMvc
            .perform(delete(ENTITY_API_URL_ID, reportTemplate.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return reportTemplateRepository.count();
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

    protected ReportTemplate getPersistedReportTemplate(ReportTemplate reportTemplate) {
        return reportTemplateRepository.findById(reportTemplate.getId()).orElseThrow();
    }

    protected void assertPersistedReportTemplateToMatchAllProperties(ReportTemplate expectedReportTemplate) {
        assertReportTemplateAllPropertiesEquals(expectedReportTemplate, getPersistedReportTemplate(expectedReportTemplate));
    }

    protected void assertPersistedReportTemplateToMatchUpdatableProperties(ReportTemplate expectedReportTemplate) {
        assertReportTemplateAllUpdatablePropertiesEquals(expectedReportTemplate, getPersistedReportTemplate(expectedReportTemplate));
    }
}
