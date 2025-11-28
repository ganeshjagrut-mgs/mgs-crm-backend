package com.mgs.web.rest;

import static com.mgs.domain.PipelineAsserts.*;
import static com.mgs.web.rest.TestUtil.createUpdateProxyForBean;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mgs.IntegrationTest;
import com.mgs.domain.Pipeline;
import com.mgs.domain.Tenant;
import com.mgs.domain.enumeration.PipelineModule;
import com.mgs.repository.PipelineRepository;
import com.mgs.service.dto.PipelineDTO;
import com.mgs.service.mapper.PipelineMapper;
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
 * Integration tests for the {@link PipelineResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class PipelineResourceIT {

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_NAME_SEARCH = "AAAAAAAAAA";
    private static final String UPDATED_NAME_SEARCH = "BBBBBBBBBB";

    private static final PipelineModule DEFAULT_MODULE = PipelineModule.LEAD;
    private static final PipelineModule UPDATED_MODULE = PipelineModule.DEAL;

    private static final Boolean DEFAULT_IS_DEFAULT = false;
    private static final Boolean UPDATED_IS_DEFAULT = true;

    private static final String ENTITY_API_URL = "/api/pipelines";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ObjectMapper om;

    @Autowired
    private PipelineRepository pipelineRepository;

    @Autowired
    private PipelineMapper pipelineMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restPipelineMockMvc;

    private Pipeline pipeline;

    private Pipeline insertedPipeline;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Pipeline createEntity(EntityManager em) {
        Pipeline pipeline = new Pipeline()
            .name(DEFAULT_NAME)
            .nameSearch(DEFAULT_NAME_SEARCH)
            .module(DEFAULT_MODULE)
            .isDefault(DEFAULT_IS_DEFAULT);
        // Add required entity
        Tenant tenant;
        if (TestUtil.findAll(em, Tenant.class).isEmpty()) {
            tenant = TenantResourceIT.createEntity();
            em.persist(tenant);
            em.flush();
        } else {
            tenant = TestUtil.findAll(em, Tenant.class).get(0);
        }
        pipeline.setTenant(tenant);
        return pipeline;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Pipeline createUpdatedEntity(EntityManager em) {
        Pipeline updatedPipeline = new Pipeline()
            .name(UPDATED_NAME)
            .nameSearch(UPDATED_NAME_SEARCH)
            .module(UPDATED_MODULE)
            .isDefault(UPDATED_IS_DEFAULT);
        // Add required entity
        Tenant tenant;
        if (TestUtil.findAll(em, Tenant.class).isEmpty()) {
            tenant = TenantResourceIT.createUpdatedEntity();
            em.persist(tenant);
            em.flush();
        } else {
            tenant = TestUtil.findAll(em, Tenant.class).get(0);
        }
        updatedPipeline.setTenant(tenant);
        return updatedPipeline;
    }

    @BeforeEach
    void initTest() {
        pipeline = createEntity(em);
    }

    @AfterEach
    void cleanup() {
        if (insertedPipeline != null) {
            pipelineRepository.delete(insertedPipeline);
            insertedPipeline = null;
        }
    }

    @Test
    @Transactional
    void createPipeline() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the Pipeline
        PipelineDTO pipelineDTO = pipelineMapper.toDto(pipeline);
        var returnedPipelineDTO = om.readValue(
            restPipelineMockMvc
                .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(pipelineDTO)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            PipelineDTO.class
        );

        // Validate the Pipeline in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        var returnedPipeline = pipelineMapper.toEntity(returnedPipelineDTO);
        assertPipelineUpdatableFieldsEquals(returnedPipeline, getPersistedPipeline(returnedPipeline));

        insertedPipeline = returnedPipeline;
    }

    @Test
    @Transactional
    void createPipelineWithExistingId() throws Exception {
        // Create the Pipeline with an existing ID
        pipeline.setId(1L);
        PipelineDTO pipelineDTO = pipelineMapper.toDto(pipeline);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restPipelineMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(pipelineDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Pipeline in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkNameIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        pipeline.setName(null);

        // Create the Pipeline, which fails.
        PipelineDTO pipelineDTO = pipelineMapper.toDto(pipeline);

        restPipelineMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(pipelineDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkModuleIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        pipeline.setModule(null);

        // Create the Pipeline, which fails.
        PipelineDTO pipelineDTO = pipelineMapper.toDto(pipeline);

        restPipelineMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(pipelineDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllPipelines() throws Exception {
        // Initialize the database
        insertedPipeline = pipelineRepository.saveAndFlush(pipeline);

        // Get all the pipelineList
        restPipelineMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(pipeline.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)))
            .andExpect(jsonPath("$.[*].nameSearch").value(hasItem(DEFAULT_NAME_SEARCH)))
            .andExpect(jsonPath("$.[*].module").value(hasItem(DEFAULT_MODULE.toString())))
            .andExpect(jsonPath("$.[*].isDefault").value(hasItem(DEFAULT_IS_DEFAULT)));
    }

    @Test
    @Transactional
    void getPipeline() throws Exception {
        // Initialize the database
        insertedPipeline = pipelineRepository.saveAndFlush(pipeline);

        // Get the pipeline
        restPipelineMockMvc
            .perform(get(ENTITY_API_URL_ID, pipeline.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(pipeline.getId().intValue()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME))
            .andExpect(jsonPath("$.nameSearch").value(DEFAULT_NAME_SEARCH))
            .andExpect(jsonPath("$.module").value(DEFAULT_MODULE.toString()))
            .andExpect(jsonPath("$.isDefault").value(DEFAULT_IS_DEFAULT));
    }

    @Test
    @Transactional
    void getPipelinesByIdFiltering() throws Exception {
        // Initialize the database
        insertedPipeline = pipelineRepository.saveAndFlush(pipeline);

        Long id = pipeline.getId();

        defaultPipelineFiltering("id.equals=" + id, "id.notEquals=" + id);

        defaultPipelineFiltering("id.greaterThanOrEqual=" + id, "id.greaterThan=" + id);

        defaultPipelineFiltering("id.lessThanOrEqual=" + id, "id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllPipelinesByNameIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedPipeline = pipelineRepository.saveAndFlush(pipeline);

        // Get all the pipelineList where name equals to
        defaultPipelineFiltering("name.equals=" + DEFAULT_NAME, "name.equals=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllPipelinesByNameIsInShouldWork() throws Exception {
        // Initialize the database
        insertedPipeline = pipelineRepository.saveAndFlush(pipeline);

        // Get all the pipelineList where name in
        defaultPipelineFiltering("name.in=" + DEFAULT_NAME + "," + UPDATED_NAME, "name.in=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllPipelinesByNameIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedPipeline = pipelineRepository.saveAndFlush(pipeline);

        // Get all the pipelineList where name is not null
        defaultPipelineFiltering("name.specified=true", "name.specified=false");
    }

    @Test
    @Transactional
    void getAllPipelinesByNameContainsSomething() throws Exception {
        // Initialize the database
        insertedPipeline = pipelineRepository.saveAndFlush(pipeline);

        // Get all the pipelineList where name contains
        defaultPipelineFiltering("name.contains=" + DEFAULT_NAME, "name.contains=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllPipelinesByNameNotContainsSomething() throws Exception {
        // Initialize the database
        insertedPipeline = pipelineRepository.saveAndFlush(pipeline);

        // Get all the pipelineList where name does not contain
        defaultPipelineFiltering("name.doesNotContain=" + UPDATED_NAME, "name.doesNotContain=" + DEFAULT_NAME);
    }

    @Test
    @Transactional
    void getAllPipelinesByNameSearchIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedPipeline = pipelineRepository.saveAndFlush(pipeline);

        // Get all the pipelineList where nameSearch equals to
        defaultPipelineFiltering("nameSearch.equals=" + DEFAULT_NAME_SEARCH, "nameSearch.equals=" + UPDATED_NAME_SEARCH);
    }

    @Test
    @Transactional
    void getAllPipelinesByNameSearchIsInShouldWork() throws Exception {
        // Initialize the database
        insertedPipeline = pipelineRepository.saveAndFlush(pipeline);

        // Get all the pipelineList where nameSearch in
        defaultPipelineFiltering(
            "nameSearch.in=" + DEFAULT_NAME_SEARCH + "," + UPDATED_NAME_SEARCH,
            "nameSearch.in=" + UPDATED_NAME_SEARCH
        );
    }

    @Test
    @Transactional
    void getAllPipelinesByNameSearchIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedPipeline = pipelineRepository.saveAndFlush(pipeline);

        // Get all the pipelineList where nameSearch is not null
        defaultPipelineFiltering("nameSearch.specified=true", "nameSearch.specified=false");
    }

    @Test
    @Transactional
    void getAllPipelinesByNameSearchContainsSomething() throws Exception {
        // Initialize the database
        insertedPipeline = pipelineRepository.saveAndFlush(pipeline);

        // Get all the pipelineList where nameSearch contains
        defaultPipelineFiltering("nameSearch.contains=" + DEFAULT_NAME_SEARCH, "nameSearch.contains=" + UPDATED_NAME_SEARCH);
    }

    @Test
    @Transactional
    void getAllPipelinesByNameSearchNotContainsSomething() throws Exception {
        // Initialize the database
        insertedPipeline = pipelineRepository.saveAndFlush(pipeline);

        // Get all the pipelineList where nameSearch does not contain
        defaultPipelineFiltering("nameSearch.doesNotContain=" + UPDATED_NAME_SEARCH, "nameSearch.doesNotContain=" + DEFAULT_NAME_SEARCH);
    }

    @Test
    @Transactional
    void getAllPipelinesByModuleIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedPipeline = pipelineRepository.saveAndFlush(pipeline);

        // Get all the pipelineList where module equals to
        defaultPipelineFiltering("module.equals=" + DEFAULT_MODULE, "module.equals=" + UPDATED_MODULE);
    }

    @Test
    @Transactional
    void getAllPipelinesByModuleIsInShouldWork() throws Exception {
        // Initialize the database
        insertedPipeline = pipelineRepository.saveAndFlush(pipeline);

        // Get all the pipelineList where module in
        defaultPipelineFiltering("module.in=" + DEFAULT_MODULE + "," + UPDATED_MODULE, "module.in=" + UPDATED_MODULE);
    }

    @Test
    @Transactional
    void getAllPipelinesByModuleIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedPipeline = pipelineRepository.saveAndFlush(pipeline);

        // Get all the pipelineList where module is not null
        defaultPipelineFiltering("module.specified=true", "module.specified=false");
    }

    @Test
    @Transactional
    void getAllPipelinesByIsDefaultIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedPipeline = pipelineRepository.saveAndFlush(pipeline);

        // Get all the pipelineList where isDefault equals to
        defaultPipelineFiltering("isDefault.equals=" + DEFAULT_IS_DEFAULT, "isDefault.equals=" + UPDATED_IS_DEFAULT);
    }

    @Test
    @Transactional
    void getAllPipelinesByIsDefaultIsInShouldWork() throws Exception {
        // Initialize the database
        insertedPipeline = pipelineRepository.saveAndFlush(pipeline);

        // Get all the pipelineList where isDefault in
        defaultPipelineFiltering("isDefault.in=" + DEFAULT_IS_DEFAULT + "," + UPDATED_IS_DEFAULT, "isDefault.in=" + UPDATED_IS_DEFAULT);
    }

    @Test
    @Transactional
    void getAllPipelinesByIsDefaultIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedPipeline = pipelineRepository.saveAndFlush(pipeline);

        // Get all the pipelineList where isDefault is not null
        defaultPipelineFiltering("isDefault.specified=true", "isDefault.specified=false");
    }

    @Test
    @Transactional
    void getAllPipelinesByTenantIsEqualToSomething() throws Exception {
        Tenant tenant;
        if (TestUtil.findAll(em, Tenant.class).isEmpty()) {
            pipelineRepository.saveAndFlush(pipeline);
            tenant = TenantResourceIT.createEntity();
        } else {
            tenant = TestUtil.findAll(em, Tenant.class).get(0);
        }
        em.persist(tenant);
        em.flush();
        pipeline.setTenant(tenant);
        pipelineRepository.saveAndFlush(pipeline);
        Long tenantId = tenant.getId();
        // Get all the pipelineList where tenant equals to tenantId
        defaultPipelineShouldBeFound("tenantId.equals=" + tenantId);

        // Get all the pipelineList where tenant equals to (tenantId + 1)
        defaultPipelineShouldNotBeFound("tenantId.equals=" + (tenantId + 1));
    }

    private void defaultPipelineFiltering(String shouldBeFound, String shouldNotBeFound) throws Exception {
        defaultPipelineShouldBeFound(shouldBeFound);
        defaultPipelineShouldNotBeFound(shouldNotBeFound);
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
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)))
            .andExpect(jsonPath("$.[*].nameSearch").value(hasItem(DEFAULT_NAME_SEARCH)))
            .andExpect(jsonPath("$.[*].module").value(hasItem(DEFAULT_MODULE.toString())))
            .andExpect(jsonPath("$.[*].isDefault").value(hasItem(DEFAULT_IS_DEFAULT)));

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
        insertedPipeline = pipelineRepository.saveAndFlush(pipeline);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the pipeline
        Pipeline updatedPipeline = pipelineRepository.findById(pipeline.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedPipeline are not directly saved in db
        em.detach(updatedPipeline);
        updatedPipeline.name(UPDATED_NAME).nameSearch(UPDATED_NAME_SEARCH).module(UPDATED_MODULE).isDefault(UPDATED_IS_DEFAULT);
        PipelineDTO pipelineDTO = pipelineMapper.toDto(updatedPipeline);

        restPipelineMockMvc
            .perform(
                put(ENTITY_API_URL_ID, pipelineDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(pipelineDTO))
            )
            .andExpect(status().isOk());

        // Validate the Pipeline in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedPipelineToMatchAllProperties(updatedPipeline);
    }

    @Test
    @Transactional
    void putNonExistingPipeline() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        pipeline.setId(longCount.incrementAndGet());

        // Create the Pipeline
        PipelineDTO pipelineDTO = pipelineMapper.toDto(pipeline);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restPipelineMockMvc
            .perform(
                put(ENTITY_API_URL_ID, pipelineDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(pipelineDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Pipeline in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchPipeline() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        pipeline.setId(longCount.incrementAndGet());

        // Create the Pipeline
        PipelineDTO pipelineDTO = pipelineMapper.toDto(pipeline);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restPipelineMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(pipelineDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Pipeline in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamPipeline() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        pipeline.setId(longCount.incrementAndGet());

        // Create the Pipeline
        PipelineDTO pipelineDTO = pipelineMapper.toDto(pipeline);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restPipelineMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(pipelineDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Pipeline in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdatePipelineWithPatch() throws Exception {
        // Initialize the database
        insertedPipeline = pipelineRepository.saveAndFlush(pipeline);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the pipeline using partial update
        Pipeline partialUpdatedPipeline = new Pipeline();
        partialUpdatedPipeline.setId(pipeline.getId());

        partialUpdatedPipeline.isDefault(UPDATED_IS_DEFAULT);

        restPipelineMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedPipeline.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedPipeline))
            )
            .andExpect(status().isOk());

        // Validate the Pipeline in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPipelineUpdatableFieldsEquals(createUpdateProxyForBean(partialUpdatedPipeline, pipeline), getPersistedPipeline(pipeline));
    }

    @Test
    @Transactional
    void fullUpdatePipelineWithPatch() throws Exception {
        // Initialize the database
        insertedPipeline = pipelineRepository.saveAndFlush(pipeline);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the pipeline using partial update
        Pipeline partialUpdatedPipeline = new Pipeline();
        partialUpdatedPipeline.setId(pipeline.getId());

        partialUpdatedPipeline.name(UPDATED_NAME).nameSearch(UPDATED_NAME_SEARCH).module(UPDATED_MODULE).isDefault(UPDATED_IS_DEFAULT);

        restPipelineMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedPipeline.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedPipeline))
            )
            .andExpect(status().isOk());

        // Validate the Pipeline in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPipelineUpdatableFieldsEquals(partialUpdatedPipeline, getPersistedPipeline(partialUpdatedPipeline));
    }

    @Test
    @Transactional
    void patchNonExistingPipeline() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        pipeline.setId(longCount.incrementAndGet());

        // Create the Pipeline
        PipelineDTO pipelineDTO = pipelineMapper.toDto(pipeline);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restPipelineMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, pipelineDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(pipelineDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Pipeline in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchPipeline() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        pipeline.setId(longCount.incrementAndGet());

        // Create the Pipeline
        PipelineDTO pipelineDTO = pipelineMapper.toDto(pipeline);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restPipelineMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(pipelineDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Pipeline in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamPipeline() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        pipeline.setId(longCount.incrementAndGet());

        // Create the Pipeline
        PipelineDTO pipelineDTO = pipelineMapper.toDto(pipeline);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restPipelineMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(om.writeValueAsBytes(pipelineDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Pipeline in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deletePipeline() throws Exception {
        // Initialize the database
        insertedPipeline = pipelineRepository.saveAndFlush(pipeline);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the pipeline
        restPipelineMockMvc
            .perform(delete(ENTITY_API_URL_ID, pipeline.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return pipelineRepository.count();
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

    protected Pipeline getPersistedPipeline(Pipeline pipeline) {
        return pipelineRepository.findById(pipeline.getId()).orElseThrow();
    }

    protected void assertPersistedPipelineToMatchAllProperties(Pipeline expectedPipeline) {
        assertPipelineAllPropertiesEquals(expectedPipeline, getPersistedPipeline(expectedPipeline));
    }

    protected void assertPersistedPipelineToMatchUpdatableProperties(Pipeline expectedPipeline) {
        assertPipelineAllUpdatablePropertiesEquals(expectedPipeline, getPersistedPipeline(expectedPipeline));
    }
}
