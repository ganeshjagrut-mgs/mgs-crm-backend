package com.crm.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.crm.IntegrationTest;
import com.crm.domain.SubPipeline;
import com.crm.domain.SubPipelineCloseStage;
import com.crm.domain.SubPipelineOpenStage;
import com.crm.repository.SubPipelineRepository;
import com.crm.service.dto.SubPipelineDTO;
import com.crm.service.mapper.SubPipelineMapper;
import jakarta.persistence.EntityManager;
import java.util.List;
import java.util.Random;
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
 * Integration tests for the {@link SubPipelineResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class SubPipelineResourceIT {

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    private static final Integer DEFAULT_INDEX = 1;
    private static final Integer UPDATED_INDEX = 2;
    private static final Integer SMALLER_INDEX = 1 - 1;

    private static final String ENTITY_API_URL = "/api/sub-pipelines";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private SubPipelineRepository subPipelineRepository;

    @Autowired
    private SubPipelineMapper subPipelineMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restSubPipelineMockMvc;

    private SubPipeline subPipeline;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static SubPipeline createEntity(EntityManager em) {
        SubPipeline subPipeline = new SubPipeline().name(DEFAULT_NAME).index(DEFAULT_INDEX);
        return subPipeline;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static SubPipeline createUpdatedEntity(EntityManager em) {
        SubPipeline subPipeline = new SubPipeline().name(UPDATED_NAME).index(UPDATED_INDEX);
        return subPipeline;
    }

    @BeforeEach
    public void initTest() {
        subPipeline = createEntity(em);
    }

    @Test
    @Transactional
    void createSubPipeline() throws Exception {
        int databaseSizeBeforeCreate = subPipelineRepository.findAll().size();
        // Create the SubPipeline
        SubPipelineDTO subPipelineDTO = subPipelineMapper.toDto(subPipeline);
        restSubPipelineMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(subPipelineDTO))
            )
            .andExpect(status().isCreated());

        // Validate the SubPipeline in the database
        List<SubPipeline> subPipelineList = subPipelineRepository.findAll();
        assertThat(subPipelineList).hasSize(databaseSizeBeforeCreate + 1);
        SubPipeline testSubPipeline = subPipelineList.get(subPipelineList.size() - 1);
        assertThat(testSubPipeline.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testSubPipeline.getIndex()).isEqualTo(DEFAULT_INDEX);
    }

    @Test
    @Transactional
    void createSubPipelineWithExistingId() throws Exception {
        // Create the SubPipeline with an existing ID
        subPipeline.setId(1L);
        SubPipelineDTO subPipelineDTO = subPipelineMapper.toDto(subPipeline);

        int databaseSizeBeforeCreate = subPipelineRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restSubPipelineMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(subPipelineDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the SubPipeline in the database
        List<SubPipeline> subPipelineList = subPipelineRepository.findAll();
        assertThat(subPipelineList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkNameIsRequired() throws Exception {
        int databaseSizeBeforeTest = subPipelineRepository.findAll().size();
        // set the field null
        subPipeline.setName(null);

        // Create the SubPipeline, which fails.
        SubPipelineDTO subPipelineDTO = subPipelineMapper.toDto(subPipeline);

        restSubPipelineMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(subPipelineDTO))
            )
            .andExpect(status().isBadRequest());

        List<SubPipeline> subPipelineList = subPipelineRepository.findAll();
        assertThat(subPipelineList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllSubPipelines() throws Exception {
        // Initialize the database
        subPipelineRepository.saveAndFlush(subPipeline);

        // Get all the subPipelineList
        restSubPipelineMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(subPipeline.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)))
            .andExpect(jsonPath("$.[*].index").value(hasItem(DEFAULT_INDEX)));
    }

    @Test
    @Transactional
    void getSubPipeline() throws Exception {
        // Initialize the database
        subPipelineRepository.saveAndFlush(subPipeline);

        // Get the subPipeline
        restSubPipelineMockMvc
            .perform(get(ENTITY_API_URL_ID, subPipeline.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(subPipeline.getId().intValue()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME))
            .andExpect(jsonPath("$.index").value(DEFAULT_INDEX));
    }

    @Test
    @Transactional
    void getSubPipelinesByIdFiltering() throws Exception {
        // Initialize the database
        subPipelineRepository.saveAndFlush(subPipeline);

        Long id = subPipeline.getId();

        defaultSubPipelineShouldBeFound("id.equals=" + id);
        defaultSubPipelineShouldNotBeFound("id.notEquals=" + id);

        defaultSubPipelineShouldBeFound("id.greaterThanOrEqual=" + id);
        defaultSubPipelineShouldNotBeFound("id.greaterThan=" + id);

        defaultSubPipelineShouldBeFound("id.lessThanOrEqual=" + id);
        defaultSubPipelineShouldNotBeFound("id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllSubPipelinesByNameIsEqualToSomething() throws Exception {
        // Initialize the database
        subPipelineRepository.saveAndFlush(subPipeline);

        // Get all the subPipelineList where name equals to DEFAULT_NAME
        defaultSubPipelineShouldBeFound("name.equals=" + DEFAULT_NAME);

        // Get all the subPipelineList where name equals to UPDATED_NAME
        defaultSubPipelineShouldNotBeFound("name.equals=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllSubPipelinesByNameIsInShouldWork() throws Exception {
        // Initialize the database
        subPipelineRepository.saveAndFlush(subPipeline);

        // Get all the subPipelineList where name in DEFAULT_NAME or UPDATED_NAME
        defaultSubPipelineShouldBeFound("name.in=" + DEFAULT_NAME + "," + UPDATED_NAME);

        // Get all the subPipelineList where name equals to UPDATED_NAME
        defaultSubPipelineShouldNotBeFound("name.in=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllSubPipelinesByNameIsNullOrNotNull() throws Exception {
        // Initialize the database
        subPipelineRepository.saveAndFlush(subPipeline);

        // Get all the subPipelineList where name is not null
        defaultSubPipelineShouldBeFound("name.specified=true");

        // Get all the subPipelineList where name is null
        defaultSubPipelineShouldNotBeFound("name.specified=false");
    }

    @Test
    @Transactional
    void getAllSubPipelinesByNameContainsSomething() throws Exception {
        // Initialize the database
        subPipelineRepository.saveAndFlush(subPipeline);

        // Get all the subPipelineList where name contains DEFAULT_NAME
        defaultSubPipelineShouldBeFound("name.contains=" + DEFAULT_NAME);

        // Get all the subPipelineList where name contains UPDATED_NAME
        defaultSubPipelineShouldNotBeFound("name.contains=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllSubPipelinesByNameNotContainsSomething() throws Exception {
        // Initialize the database
        subPipelineRepository.saveAndFlush(subPipeline);

        // Get all the subPipelineList where name does not contain DEFAULT_NAME
        defaultSubPipelineShouldNotBeFound("name.doesNotContain=" + DEFAULT_NAME);

        // Get all the subPipelineList where name does not contain UPDATED_NAME
        defaultSubPipelineShouldBeFound("name.doesNotContain=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllSubPipelinesByIndexIsEqualToSomething() throws Exception {
        // Initialize the database
        subPipelineRepository.saveAndFlush(subPipeline);

        // Get all the subPipelineList where index equals to DEFAULT_INDEX
        defaultSubPipelineShouldBeFound("index.equals=" + DEFAULT_INDEX);

        // Get all the subPipelineList where index equals to UPDATED_INDEX
        defaultSubPipelineShouldNotBeFound("index.equals=" + UPDATED_INDEX);
    }

    @Test
    @Transactional
    void getAllSubPipelinesByIndexIsInShouldWork() throws Exception {
        // Initialize the database
        subPipelineRepository.saveAndFlush(subPipeline);

        // Get all the subPipelineList where index in DEFAULT_INDEX or UPDATED_INDEX
        defaultSubPipelineShouldBeFound("index.in=" + DEFAULT_INDEX + "," + UPDATED_INDEX);

        // Get all the subPipelineList where index equals to UPDATED_INDEX
        defaultSubPipelineShouldNotBeFound("index.in=" + UPDATED_INDEX);
    }

    @Test
    @Transactional
    void getAllSubPipelinesByIndexIsNullOrNotNull() throws Exception {
        // Initialize the database
        subPipelineRepository.saveAndFlush(subPipeline);

        // Get all the subPipelineList where index is not null
        defaultSubPipelineShouldBeFound("index.specified=true");

        // Get all the subPipelineList where index is null
        defaultSubPipelineShouldNotBeFound("index.specified=false");
    }

    @Test
    @Transactional
    void getAllSubPipelinesByIndexIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        subPipelineRepository.saveAndFlush(subPipeline);

        // Get all the subPipelineList where index is greater than or equal to DEFAULT_INDEX
        defaultSubPipelineShouldBeFound("index.greaterThanOrEqual=" + DEFAULT_INDEX);

        // Get all the subPipelineList where index is greater than or equal to UPDATED_INDEX
        defaultSubPipelineShouldNotBeFound("index.greaterThanOrEqual=" + UPDATED_INDEX);
    }

    @Test
    @Transactional
    void getAllSubPipelinesByIndexIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        subPipelineRepository.saveAndFlush(subPipeline);

        // Get all the subPipelineList where index is less than or equal to DEFAULT_INDEX
        defaultSubPipelineShouldBeFound("index.lessThanOrEqual=" + DEFAULT_INDEX);

        // Get all the subPipelineList where index is less than or equal to SMALLER_INDEX
        defaultSubPipelineShouldNotBeFound("index.lessThanOrEqual=" + SMALLER_INDEX);
    }

    @Test
    @Transactional
    void getAllSubPipelinesByIndexIsLessThanSomething() throws Exception {
        // Initialize the database
        subPipelineRepository.saveAndFlush(subPipeline);

        // Get all the subPipelineList where index is less than DEFAULT_INDEX
        defaultSubPipelineShouldNotBeFound("index.lessThan=" + DEFAULT_INDEX);

        // Get all the subPipelineList where index is less than UPDATED_INDEX
        defaultSubPipelineShouldBeFound("index.lessThan=" + UPDATED_INDEX);
    }

    @Test
    @Transactional
    void getAllSubPipelinesByIndexIsGreaterThanSomething() throws Exception {
        // Initialize the database
        subPipelineRepository.saveAndFlush(subPipeline);

        // Get all the subPipelineList where index is greater than DEFAULT_INDEX
        defaultSubPipelineShouldNotBeFound("index.greaterThan=" + DEFAULT_INDEX);

        // Get all the subPipelineList where index is greater than SMALLER_INDEX
        defaultSubPipelineShouldBeFound("index.greaterThan=" + SMALLER_INDEX);
    }

    @Test
    @Transactional
    void getAllSubPipelinesByOpenStagesIsEqualToSomething() throws Exception {
        SubPipelineOpenStage openStages;
        if (TestUtil.findAll(em, SubPipelineOpenStage.class).isEmpty()) {
            subPipelineRepository.saveAndFlush(subPipeline);
            openStages = SubPipelineOpenStageResourceIT.createEntity(em);
        } else {
            openStages = TestUtil.findAll(em, SubPipelineOpenStage.class).get(0);
        }
        em.persist(openStages);
        em.flush();
        subPipeline.addOpenStages(openStages);
        subPipelineRepository.saveAndFlush(subPipeline);
        Long openStagesId = openStages.getId();
        // Get all the subPipelineList where openStages equals to openStagesId
        defaultSubPipelineShouldBeFound("openStagesId.equals=" + openStagesId);

        // Get all the subPipelineList where openStages equals to (openStagesId + 1)
        defaultSubPipelineShouldNotBeFound("openStagesId.equals=" + (openStagesId + 1));
    }

    @Test
    @Transactional
    void getAllSubPipelinesByCloseStagesIsEqualToSomething() throws Exception {
        SubPipelineCloseStage closeStages;
        if (TestUtil.findAll(em, SubPipelineCloseStage.class).isEmpty()) {
            subPipelineRepository.saveAndFlush(subPipeline);
            closeStages = SubPipelineCloseStageResourceIT.createEntity(em);
        } else {
            closeStages = TestUtil.findAll(em, SubPipelineCloseStage.class).get(0);
        }
        em.persist(closeStages);
        em.flush();
        subPipeline.addCloseStages(closeStages);
        subPipelineRepository.saveAndFlush(subPipeline);
        Long closeStagesId = closeStages.getId();
        // Get all the subPipelineList where closeStages equals to closeStagesId
        defaultSubPipelineShouldBeFound("closeStagesId.equals=" + closeStagesId);

        // Get all the subPipelineList where closeStages equals to (closeStagesId + 1)
        defaultSubPipelineShouldNotBeFound("closeStagesId.equals=" + (closeStagesId + 1));
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
            .andExpect(jsonPath("$.[*].index").value(hasItem(DEFAULT_INDEX)));

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
        subPipelineRepository.saveAndFlush(subPipeline);

        int databaseSizeBeforeUpdate = subPipelineRepository.findAll().size();

        // Update the subPipeline
        SubPipeline updatedSubPipeline = subPipelineRepository.findById(subPipeline.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedSubPipeline are not directly saved in db
        em.detach(updatedSubPipeline);
        updatedSubPipeline.name(UPDATED_NAME).index(UPDATED_INDEX);
        SubPipelineDTO subPipelineDTO = subPipelineMapper.toDto(updatedSubPipeline);

        restSubPipelineMockMvc
            .perform(
                put(ENTITY_API_URL_ID, subPipelineDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(subPipelineDTO))
            )
            .andExpect(status().isOk());

        // Validate the SubPipeline in the database
        List<SubPipeline> subPipelineList = subPipelineRepository.findAll();
        assertThat(subPipelineList).hasSize(databaseSizeBeforeUpdate);
        SubPipeline testSubPipeline = subPipelineList.get(subPipelineList.size() - 1);
        assertThat(testSubPipeline.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testSubPipeline.getIndex()).isEqualTo(UPDATED_INDEX);
    }

    @Test
    @Transactional
    void putNonExistingSubPipeline() throws Exception {
        int databaseSizeBeforeUpdate = subPipelineRepository.findAll().size();
        subPipeline.setId(longCount.incrementAndGet());

        // Create the SubPipeline
        SubPipelineDTO subPipelineDTO = subPipelineMapper.toDto(subPipeline);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restSubPipelineMockMvc
            .perform(
                put(ENTITY_API_URL_ID, subPipelineDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(subPipelineDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the SubPipeline in the database
        List<SubPipeline> subPipelineList = subPipelineRepository.findAll();
        assertThat(subPipelineList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchSubPipeline() throws Exception {
        int databaseSizeBeforeUpdate = subPipelineRepository.findAll().size();
        subPipeline.setId(longCount.incrementAndGet());

        // Create the SubPipeline
        SubPipelineDTO subPipelineDTO = subPipelineMapper.toDto(subPipeline);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restSubPipelineMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(subPipelineDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the SubPipeline in the database
        List<SubPipeline> subPipelineList = subPipelineRepository.findAll();
        assertThat(subPipelineList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamSubPipeline() throws Exception {
        int databaseSizeBeforeUpdate = subPipelineRepository.findAll().size();
        subPipeline.setId(longCount.incrementAndGet());

        // Create the SubPipeline
        SubPipelineDTO subPipelineDTO = subPipelineMapper.toDto(subPipeline);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restSubPipelineMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(subPipelineDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the SubPipeline in the database
        List<SubPipeline> subPipelineList = subPipelineRepository.findAll();
        assertThat(subPipelineList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateSubPipelineWithPatch() throws Exception {
        // Initialize the database
        subPipelineRepository.saveAndFlush(subPipeline);

        int databaseSizeBeforeUpdate = subPipelineRepository.findAll().size();

        // Update the subPipeline using partial update
        SubPipeline partialUpdatedSubPipeline = new SubPipeline();
        partialUpdatedSubPipeline.setId(subPipeline.getId());

        partialUpdatedSubPipeline.name(UPDATED_NAME).index(UPDATED_INDEX);

        restSubPipelineMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedSubPipeline.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedSubPipeline))
            )
            .andExpect(status().isOk());

        // Validate the SubPipeline in the database
        List<SubPipeline> subPipelineList = subPipelineRepository.findAll();
        assertThat(subPipelineList).hasSize(databaseSizeBeforeUpdate);
        SubPipeline testSubPipeline = subPipelineList.get(subPipelineList.size() - 1);
        assertThat(testSubPipeline.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testSubPipeline.getIndex()).isEqualTo(UPDATED_INDEX);
    }

    @Test
    @Transactional
    void fullUpdateSubPipelineWithPatch() throws Exception {
        // Initialize the database
        subPipelineRepository.saveAndFlush(subPipeline);

        int databaseSizeBeforeUpdate = subPipelineRepository.findAll().size();

        // Update the subPipeline using partial update
        SubPipeline partialUpdatedSubPipeline = new SubPipeline();
        partialUpdatedSubPipeline.setId(subPipeline.getId());

        partialUpdatedSubPipeline.name(UPDATED_NAME).index(UPDATED_INDEX);

        restSubPipelineMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedSubPipeline.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedSubPipeline))
            )
            .andExpect(status().isOk());

        // Validate the SubPipeline in the database
        List<SubPipeline> subPipelineList = subPipelineRepository.findAll();
        assertThat(subPipelineList).hasSize(databaseSizeBeforeUpdate);
        SubPipeline testSubPipeline = subPipelineList.get(subPipelineList.size() - 1);
        assertThat(testSubPipeline.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testSubPipeline.getIndex()).isEqualTo(UPDATED_INDEX);
    }

    @Test
    @Transactional
    void patchNonExistingSubPipeline() throws Exception {
        int databaseSizeBeforeUpdate = subPipelineRepository.findAll().size();
        subPipeline.setId(longCount.incrementAndGet());

        // Create the SubPipeline
        SubPipelineDTO subPipelineDTO = subPipelineMapper.toDto(subPipeline);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restSubPipelineMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, subPipelineDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(subPipelineDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the SubPipeline in the database
        List<SubPipeline> subPipelineList = subPipelineRepository.findAll();
        assertThat(subPipelineList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchSubPipeline() throws Exception {
        int databaseSizeBeforeUpdate = subPipelineRepository.findAll().size();
        subPipeline.setId(longCount.incrementAndGet());

        // Create the SubPipeline
        SubPipelineDTO subPipelineDTO = subPipelineMapper.toDto(subPipeline);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restSubPipelineMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(subPipelineDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the SubPipeline in the database
        List<SubPipeline> subPipelineList = subPipelineRepository.findAll();
        assertThat(subPipelineList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamSubPipeline() throws Exception {
        int databaseSizeBeforeUpdate = subPipelineRepository.findAll().size();
        subPipeline.setId(longCount.incrementAndGet());

        // Create the SubPipeline
        SubPipelineDTO subPipelineDTO = subPipelineMapper.toDto(subPipeline);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restSubPipelineMockMvc
            .perform(
                patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(subPipelineDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the SubPipeline in the database
        List<SubPipeline> subPipelineList = subPipelineRepository.findAll();
        assertThat(subPipelineList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteSubPipeline() throws Exception {
        // Initialize the database
        subPipelineRepository.saveAndFlush(subPipeline);

        int databaseSizeBeforeDelete = subPipelineRepository.findAll().size();

        // Delete the subPipeline
        restSubPipelineMockMvc
            .perform(delete(ENTITY_API_URL_ID, subPipeline.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<SubPipeline> subPipelineList = subPipelineRepository.findAll();
        assertThat(subPipelineList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
