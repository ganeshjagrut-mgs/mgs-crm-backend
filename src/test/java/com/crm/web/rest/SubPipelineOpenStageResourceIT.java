package com.crm.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.crm.IntegrationTest;
import com.crm.domain.MasterStaticType;
import com.crm.domain.SubPipeline;
import com.crm.domain.SubPipelineOpenStage;
import com.crm.repository.SubPipelineOpenStageRepository;
import com.crm.service.dto.SubPipelineOpenStageDTO;
import com.crm.service.mapper.SubPipelineOpenStageMapper;
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
 * Integration tests for the {@link SubPipelineOpenStageResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class SubPipelineOpenStageResourceIT {

    private static final Integer DEFAULT_INDEX = 1;
    private static final Integer UPDATED_INDEX = 2;
    private static final Integer SMALLER_INDEX = 1 - 1;

    private static final String ENTITY_API_URL = "/api/sub-pipeline-open-stages";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private SubPipelineOpenStageRepository subPipelineOpenStageRepository;

    @Autowired
    private SubPipelineOpenStageMapper subPipelineOpenStageMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restSubPipelineOpenStageMockMvc;

    private SubPipelineOpenStage subPipelineOpenStage;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static SubPipelineOpenStage createEntity(EntityManager em) {
        SubPipelineOpenStage subPipelineOpenStage = new SubPipelineOpenStage().index(DEFAULT_INDEX);
        // Add required entity
        MasterStaticType masterStaticType;
        if (TestUtil.findAll(em, MasterStaticType.class).isEmpty()) {
            masterStaticType = MasterStaticTypeResourceIT.createEntity(em);
            em.persist(masterStaticType);
            em.flush();
        } else {
            masterStaticType = TestUtil.findAll(em, MasterStaticType.class).get(0);
        }
        subPipelineOpenStage.setStage(masterStaticType);
        // Add required entity
        SubPipeline subPipeline;
        if (TestUtil.findAll(em, SubPipeline.class).isEmpty()) {
            subPipeline = SubPipelineResourceIT.createEntity(em);
            em.persist(subPipeline);
            em.flush();
        } else {
            subPipeline = TestUtil.findAll(em, SubPipeline.class).get(0);
        }
        subPipelineOpenStage.setSubPipeline(subPipeline);
        return subPipelineOpenStage;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static SubPipelineOpenStage createUpdatedEntity(EntityManager em) {
        SubPipelineOpenStage subPipelineOpenStage = new SubPipelineOpenStage().index(UPDATED_INDEX);
        // Add required entity
        MasterStaticType masterStaticType;
        if (TestUtil.findAll(em, MasterStaticType.class).isEmpty()) {
            masterStaticType = MasterStaticTypeResourceIT.createUpdatedEntity(em);
            em.persist(masterStaticType);
            em.flush();
        } else {
            masterStaticType = TestUtil.findAll(em, MasterStaticType.class).get(0);
        }
        subPipelineOpenStage.setStage(masterStaticType);
        // Add required entity
        SubPipeline subPipeline;
        if (TestUtil.findAll(em, SubPipeline.class).isEmpty()) {
            subPipeline = SubPipelineResourceIT.createUpdatedEntity(em);
            em.persist(subPipeline);
            em.flush();
        } else {
            subPipeline = TestUtil.findAll(em, SubPipeline.class).get(0);
        }
        subPipelineOpenStage.setSubPipeline(subPipeline);
        return subPipelineOpenStage;
    }

    @BeforeEach
    public void initTest() {
        subPipelineOpenStage = createEntity(em);
    }

    @Test
    @Transactional
    void createSubPipelineOpenStage() throws Exception {
        int databaseSizeBeforeCreate = subPipelineOpenStageRepository.findAll().size();
        // Create the SubPipelineOpenStage
        SubPipelineOpenStageDTO subPipelineOpenStageDTO = subPipelineOpenStageMapper.toDto(subPipelineOpenStage);
        restSubPipelineOpenStageMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(subPipelineOpenStageDTO))
            )
            .andExpect(status().isCreated());

        // Validate the SubPipelineOpenStage in the database
        List<SubPipelineOpenStage> subPipelineOpenStageList = subPipelineOpenStageRepository.findAll();
        assertThat(subPipelineOpenStageList).hasSize(databaseSizeBeforeCreate + 1);
        SubPipelineOpenStage testSubPipelineOpenStage = subPipelineOpenStageList.get(subPipelineOpenStageList.size() - 1);
        assertThat(testSubPipelineOpenStage.getIndex()).isEqualTo(DEFAULT_INDEX);
    }

    @Test
    @Transactional
    void createSubPipelineOpenStageWithExistingId() throws Exception {
        // Create the SubPipelineOpenStage with an existing ID
        subPipelineOpenStage.setId(1L);
        SubPipelineOpenStageDTO subPipelineOpenStageDTO = subPipelineOpenStageMapper.toDto(subPipelineOpenStage);

        int databaseSizeBeforeCreate = subPipelineOpenStageRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restSubPipelineOpenStageMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(subPipelineOpenStageDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the SubPipelineOpenStage in the database
        List<SubPipelineOpenStage> subPipelineOpenStageList = subPipelineOpenStageRepository.findAll();
        assertThat(subPipelineOpenStageList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllSubPipelineOpenStages() throws Exception {
        // Initialize the database
        subPipelineOpenStageRepository.saveAndFlush(subPipelineOpenStage);

        // Get all the subPipelineOpenStageList
        restSubPipelineOpenStageMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(subPipelineOpenStage.getId().intValue())))
            .andExpect(jsonPath("$.[*].index").value(hasItem(DEFAULT_INDEX)));
    }

    @Test
    @Transactional
    void getSubPipelineOpenStage() throws Exception {
        // Initialize the database
        subPipelineOpenStageRepository.saveAndFlush(subPipelineOpenStage);

        // Get the subPipelineOpenStage
        restSubPipelineOpenStageMockMvc
            .perform(get(ENTITY_API_URL_ID, subPipelineOpenStage.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(subPipelineOpenStage.getId().intValue()))
            .andExpect(jsonPath("$.index").value(DEFAULT_INDEX));
    }

    @Test
    @Transactional
    void getSubPipelineOpenStagesByIdFiltering() throws Exception {
        // Initialize the database
        subPipelineOpenStageRepository.saveAndFlush(subPipelineOpenStage);

        Long id = subPipelineOpenStage.getId();

        defaultSubPipelineOpenStageShouldBeFound("id.equals=" + id);
        defaultSubPipelineOpenStageShouldNotBeFound("id.notEquals=" + id);

        defaultSubPipelineOpenStageShouldBeFound("id.greaterThanOrEqual=" + id);
        defaultSubPipelineOpenStageShouldNotBeFound("id.greaterThan=" + id);

        defaultSubPipelineOpenStageShouldBeFound("id.lessThanOrEqual=" + id);
        defaultSubPipelineOpenStageShouldNotBeFound("id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllSubPipelineOpenStagesByIndexIsEqualToSomething() throws Exception {
        // Initialize the database
        subPipelineOpenStageRepository.saveAndFlush(subPipelineOpenStage);

        // Get all the subPipelineOpenStageList where index equals to DEFAULT_INDEX
        defaultSubPipelineOpenStageShouldBeFound("index.equals=" + DEFAULT_INDEX);

        // Get all the subPipelineOpenStageList where index equals to UPDATED_INDEX
        defaultSubPipelineOpenStageShouldNotBeFound("index.equals=" + UPDATED_INDEX);
    }

    @Test
    @Transactional
    void getAllSubPipelineOpenStagesByIndexIsInShouldWork() throws Exception {
        // Initialize the database
        subPipelineOpenStageRepository.saveAndFlush(subPipelineOpenStage);

        // Get all the subPipelineOpenStageList where index in DEFAULT_INDEX or UPDATED_INDEX
        defaultSubPipelineOpenStageShouldBeFound("index.in=" + DEFAULT_INDEX + "," + UPDATED_INDEX);

        // Get all the subPipelineOpenStageList where index equals to UPDATED_INDEX
        defaultSubPipelineOpenStageShouldNotBeFound("index.in=" + UPDATED_INDEX);
    }

    @Test
    @Transactional
    void getAllSubPipelineOpenStagesByIndexIsNullOrNotNull() throws Exception {
        // Initialize the database
        subPipelineOpenStageRepository.saveAndFlush(subPipelineOpenStage);

        // Get all the subPipelineOpenStageList where index is not null
        defaultSubPipelineOpenStageShouldBeFound("index.specified=true");

        // Get all the subPipelineOpenStageList where index is null
        defaultSubPipelineOpenStageShouldNotBeFound("index.specified=false");
    }

    @Test
    @Transactional
    void getAllSubPipelineOpenStagesByIndexIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        subPipelineOpenStageRepository.saveAndFlush(subPipelineOpenStage);

        // Get all the subPipelineOpenStageList where index is greater than or equal to DEFAULT_INDEX
        defaultSubPipelineOpenStageShouldBeFound("index.greaterThanOrEqual=" + DEFAULT_INDEX);

        // Get all the subPipelineOpenStageList where index is greater than or equal to UPDATED_INDEX
        defaultSubPipelineOpenStageShouldNotBeFound("index.greaterThanOrEqual=" + UPDATED_INDEX);
    }

    @Test
    @Transactional
    void getAllSubPipelineOpenStagesByIndexIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        subPipelineOpenStageRepository.saveAndFlush(subPipelineOpenStage);

        // Get all the subPipelineOpenStageList where index is less than or equal to DEFAULT_INDEX
        defaultSubPipelineOpenStageShouldBeFound("index.lessThanOrEqual=" + DEFAULT_INDEX);

        // Get all the subPipelineOpenStageList where index is less than or equal to SMALLER_INDEX
        defaultSubPipelineOpenStageShouldNotBeFound("index.lessThanOrEqual=" + SMALLER_INDEX);
    }

    @Test
    @Transactional
    void getAllSubPipelineOpenStagesByIndexIsLessThanSomething() throws Exception {
        // Initialize the database
        subPipelineOpenStageRepository.saveAndFlush(subPipelineOpenStage);

        // Get all the subPipelineOpenStageList where index is less than DEFAULT_INDEX
        defaultSubPipelineOpenStageShouldNotBeFound("index.lessThan=" + DEFAULT_INDEX);

        // Get all the subPipelineOpenStageList where index is less than UPDATED_INDEX
        defaultSubPipelineOpenStageShouldBeFound("index.lessThan=" + UPDATED_INDEX);
    }

    @Test
    @Transactional
    void getAllSubPipelineOpenStagesByIndexIsGreaterThanSomething() throws Exception {
        // Initialize the database
        subPipelineOpenStageRepository.saveAndFlush(subPipelineOpenStage);

        // Get all the subPipelineOpenStageList where index is greater than DEFAULT_INDEX
        defaultSubPipelineOpenStageShouldNotBeFound("index.greaterThan=" + DEFAULT_INDEX);

        // Get all the subPipelineOpenStageList where index is greater than SMALLER_INDEX
        defaultSubPipelineOpenStageShouldBeFound("index.greaterThan=" + SMALLER_INDEX);
    }

    @Test
    @Transactional
    void getAllSubPipelineOpenStagesByStageIsEqualToSomething() throws Exception {
        MasterStaticType stage;
        if (TestUtil.findAll(em, MasterStaticType.class).isEmpty()) {
            subPipelineOpenStageRepository.saveAndFlush(subPipelineOpenStage);
            stage = MasterStaticTypeResourceIT.createEntity(em);
        } else {
            stage = TestUtil.findAll(em, MasterStaticType.class).get(0);
        }
        em.persist(stage);
        em.flush();
        subPipelineOpenStage.setStage(stage);
        subPipelineOpenStageRepository.saveAndFlush(subPipelineOpenStage);
        Long stageId = stage.getId();
        // Get all the subPipelineOpenStageList where stage equals to stageId
        defaultSubPipelineOpenStageShouldBeFound("stageId.equals=" + stageId);

        // Get all the subPipelineOpenStageList where stage equals to (stageId + 1)
        defaultSubPipelineOpenStageShouldNotBeFound("stageId.equals=" + (stageId + 1));
    }

    @Test
    @Transactional
    void getAllSubPipelineOpenStagesBySubPipelineIsEqualToSomething() throws Exception {
        SubPipeline subPipeline;
        if (TestUtil.findAll(em, SubPipeline.class).isEmpty()) {
            subPipelineOpenStageRepository.saveAndFlush(subPipelineOpenStage);
            subPipeline = SubPipelineResourceIT.createEntity(em);
        } else {
            subPipeline = TestUtil.findAll(em, SubPipeline.class).get(0);
        }
        em.persist(subPipeline);
        em.flush();
        subPipelineOpenStage.setSubPipeline(subPipeline);
        subPipelineOpenStageRepository.saveAndFlush(subPipelineOpenStage);
        Long subPipelineId = subPipeline.getId();
        // Get all the subPipelineOpenStageList where subPipeline equals to subPipelineId
        defaultSubPipelineOpenStageShouldBeFound("subPipelineId.equals=" + subPipelineId);

        // Get all the subPipelineOpenStageList where subPipeline equals to (subPipelineId + 1)
        defaultSubPipelineOpenStageShouldNotBeFound("subPipelineId.equals=" + (subPipelineId + 1));
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultSubPipelineOpenStageShouldBeFound(String filter) throws Exception {
        restSubPipelineOpenStageMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(subPipelineOpenStage.getId().intValue())))
            .andExpect(jsonPath("$.[*].index").value(hasItem(DEFAULT_INDEX)));

        // Check, that the count call also returns 1
        restSubPipelineOpenStageMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultSubPipelineOpenStageShouldNotBeFound(String filter) throws Exception {
        restSubPipelineOpenStageMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restSubPipelineOpenStageMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingSubPipelineOpenStage() throws Exception {
        // Get the subPipelineOpenStage
        restSubPipelineOpenStageMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingSubPipelineOpenStage() throws Exception {
        // Initialize the database
        subPipelineOpenStageRepository.saveAndFlush(subPipelineOpenStage);

        int databaseSizeBeforeUpdate = subPipelineOpenStageRepository.findAll().size();

        // Update the subPipelineOpenStage
        SubPipelineOpenStage updatedSubPipelineOpenStage = subPipelineOpenStageRepository
            .findById(subPipelineOpenStage.getId())
            .orElseThrow();
        // Disconnect from session so that the updates on updatedSubPipelineOpenStage are not directly saved in db
        em.detach(updatedSubPipelineOpenStage);
        updatedSubPipelineOpenStage.index(UPDATED_INDEX);
        SubPipelineOpenStageDTO subPipelineOpenStageDTO = subPipelineOpenStageMapper.toDto(updatedSubPipelineOpenStage);

        restSubPipelineOpenStageMockMvc
            .perform(
                put(ENTITY_API_URL_ID, subPipelineOpenStageDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(subPipelineOpenStageDTO))
            )
            .andExpect(status().isOk());

        // Validate the SubPipelineOpenStage in the database
        List<SubPipelineOpenStage> subPipelineOpenStageList = subPipelineOpenStageRepository.findAll();
        assertThat(subPipelineOpenStageList).hasSize(databaseSizeBeforeUpdate);
        SubPipelineOpenStage testSubPipelineOpenStage = subPipelineOpenStageList.get(subPipelineOpenStageList.size() - 1);
        assertThat(testSubPipelineOpenStage.getIndex()).isEqualTo(UPDATED_INDEX);
    }

    @Test
    @Transactional
    void putNonExistingSubPipelineOpenStage() throws Exception {
        int databaseSizeBeforeUpdate = subPipelineOpenStageRepository.findAll().size();
        subPipelineOpenStage.setId(longCount.incrementAndGet());

        // Create the SubPipelineOpenStage
        SubPipelineOpenStageDTO subPipelineOpenStageDTO = subPipelineOpenStageMapper.toDto(subPipelineOpenStage);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restSubPipelineOpenStageMockMvc
            .perform(
                put(ENTITY_API_URL_ID, subPipelineOpenStageDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(subPipelineOpenStageDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the SubPipelineOpenStage in the database
        List<SubPipelineOpenStage> subPipelineOpenStageList = subPipelineOpenStageRepository.findAll();
        assertThat(subPipelineOpenStageList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchSubPipelineOpenStage() throws Exception {
        int databaseSizeBeforeUpdate = subPipelineOpenStageRepository.findAll().size();
        subPipelineOpenStage.setId(longCount.incrementAndGet());

        // Create the SubPipelineOpenStage
        SubPipelineOpenStageDTO subPipelineOpenStageDTO = subPipelineOpenStageMapper.toDto(subPipelineOpenStage);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restSubPipelineOpenStageMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(subPipelineOpenStageDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the SubPipelineOpenStage in the database
        List<SubPipelineOpenStage> subPipelineOpenStageList = subPipelineOpenStageRepository.findAll();
        assertThat(subPipelineOpenStageList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamSubPipelineOpenStage() throws Exception {
        int databaseSizeBeforeUpdate = subPipelineOpenStageRepository.findAll().size();
        subPipelineOpenStage.setId(longCount.incrementAndGet());

        // Create the SubPipelineOpenStage
        SubPipelineOpenStageDTO subPipelineOpenStageDTO = subPipelineOpenStageMapper.toDto(subPipelineOpenStage);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restSubPipelineOpenStageMockMvc
            .perform(
                put(ENTITY_API_URL)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(subPipelineOpenStageDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the SubPipelineOpenStage in the database
        List<SubPipelineOpenStage> subPipelineOpenStageList = subPipelineOpenStageRepository.findAll();
        assertThat(subPipelineOpenStageList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateSubPipelineOpenStageWithPatch() throws Exception {
        // Initialize the database
        subPipelineOpenStageRepository.saveAndFlush(subPipelineOpenStage);

        int databaseSizeBeforeUpdate = subPipelineOpenStageRepository.findAll().size();

        // Update the subPipelineOpenStage using partial update
        SubPipelineOpenStage partialUpdatedSubPipelineOpenStage = new SubPipelineOpenStage();
        partialUpdatedSubPipelineOpenStage.setId(subPipelineOpenStage.getId());

        restSubPipelineOpenStageMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedSubPipelineOpenStage.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedSubPipelineOpenStage))
            )
            .andExpect(status().isOk());

        // Validate the SubPipelineOpenStage in the database
        List<SubPipelineOpenStage> subPipelineOpenStageList = subPipelineOpenStageRepository.findAll();
        assertThat(subPipelineOpenStageList).hasSize(databaseSizeBeforeUpdate);
        SubPipelineOpenStage testSubPipelineOpenStage = subPipelineOpenStageList.get(subPipelineOpenStageList.size() - 1);
        assertThat(testSubPipelineOpenStage.getIndex()).isEqualTo(DEFAULT_INDEX);
    }

    @Test
    @Transactional
    void fullUpdateSubPipelineOpenStageWithPatch() throws Exception {
        // Initialize the database
        subPipelineOpenStageRepository.saveAndFlush(subPipelineOpenStage);

        int databaseSizeBeforeUpdate = subPipelineOpenStageRepository.findAll().size();

        // Update the subPipelineOpenStage using partial update
        SubPipelineOpenStage partialUpdatedSubPipelineOpenStage = new SubPipelineOpenStage();
        partialUpdatedSubPipelineOpenStage.setId(subPipelineOpenStage.getId());

        partialUpdatedSubPipelineOpenStage.index(UPDATED_INDEX);

        restSubPipelineOpenStageMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedSubPipelineOpenStage.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedSubPipelineOpenStage))
            )
            .andExpect(status().isOk());

        // Validate the SubPipelineOpenStage in the database
        List<SubPipelineOpenStage> subPipelineOpenStageList = subPipelineOpenStageRepository.findAll();
        assertThat(subPipelineOpenStageList).hasSize(databaseSizeBeforeUpdate);
        SubPipelineOpenStage testSubPipelineOpenStage = subPipelineOpenStageList.get(subPipelineOpenStageList.size() - 1);
        assertThat(testSubPipelineOpenStage.getIndex()).isEqualTo(UPDATED_INDEX);
    }

    @Test
    @Transactional
    void patchNonExistingSubPipelineOpenStage() throws Exception {
        int databaseSizeBeforeUpdate = subPipelineOpenStageRepository.findAll().size();
        subPipelineOpenStage.setId(longCount.incrementAndGet());

        // Create the SubPipelineOpenStage
        SubPipelineOpenStageDTO subPipelineOpenStageDTO = subPipelineOpenStageMapper.toDto(subPipelineOpenStage);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restSubPipelineOpenStageMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, subPipelineOpenStageDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(subPipelineOpenStageDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the SubPipelineOpenStage in the database
        List<SubPipelineOpenStage> subPipelineOpenStageList = subPipelineOpenStageRepository.findAll();
        assertThat(subPipelineOpenStageList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchSubPipelineOpenStage() throws Exception {
        int databaseSizeBeforeUpdate = subPipelineOpenStageRepository.findAll().size();
        subPipelineOpenStage.setId(longCount.incrementAndGet());

        // Create the SubPipelineOpenStage
        SubPipelineOpenStageDTO subPipelineOpenStageDTO = subPipelineOpenStageMapper.toDto(subPipelineOpenStage);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restSubPipelineOpenStageMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(subPipelineOpenStageDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the SubPipelineOpenStage in the database
        List<SubPipelineOpenStage> subPipelineOpenStageList = subPipelineOpenStageRepository.findAll();
        assertThat(subPipelineOpenStageList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamSubPipelineOpenStage() throws Exception {
        int databaseSizeBeforeUpdate = subPipelineOpenStageRepository.findAll().size();
        subPipelineOpenStage.setId(longCount.incrementAndGet());

        // Create the SubPipelineOpenStage
        SubPipelineOpenStageDTO subPipelineOpenStageDTO = subPipelineOpenStageMapper.toDto(subPipelineOpenStage);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restSubPipelineOpenStageMockMvc
            .perform(
                patch(ENTITY_API_URL)
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(subPipelineOpenStageDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the SubPipelineOpenStage in the database
        List<SubPipelineOpenStage> subPipelineOpenStageList = subPipelineOpenStageRepository.findAll();
        assertThat(subPipelineOpenStageList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteSubPipelineOpenStage() throws Exception {
        // Initialize the database
        subPipelineOpenStageRepository.saveAndFlush(subPipelineOpenStage);

        int databaseSizeBeforeDelete = subPipelineOpenStageRepository.findAll().size();

        // Delete the subPipelineOpenStage
        restSubPipelineOpenStageMockMvc
            .perform(delete(ENTITY_API_URL_ID, subPipelineOpenStage.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<SubPipelineOpenStage> subPipelineOpenStageList = subPipelineOpenStageRepository.findAll();
        assertThat(subPipelineOpenStageList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
