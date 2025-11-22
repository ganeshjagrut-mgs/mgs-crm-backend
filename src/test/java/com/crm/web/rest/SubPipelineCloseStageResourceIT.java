package com.crm.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.crm.IntegrationTest;
import com.crm.domain.MasterStaticType;
import com.crm.domain.SubPipeline;
import com.crm.domain.SubPipelineCloseStage;
import com.crm.repository.SubPipelineCloseStageRepository;
import com.crm.service.dto.SubPipelineCloseStageDTO;
import com.crm.service.mapper.SubPipelineCloseStageMapper;
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
 * Integration tests for the {@link SubPipelineCloseStageResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class SubPipelineCloseStageResourceIT {

    private static final Integer DEFAULT_INDEX = 1;
    private static final Integer UPDATED_INDEX = 2;
    private static final Integer SMALLER_INDEX = 1 - 1;

    private static final String ENTITY_API_URL = "/api/sub-pipeline-close-stages";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private SubPipelineCloseStageRepository subPipelineCloseStageRepository;

    @Autowired
    private SubPipelineCloseStageMapper subPipelineCloseStageMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restSubPipelineCloseStageMockMvc;

    private SubPipelineCloseStage subPipelineCloseStage;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static SubPipelineCloseStage createEntity(EntityManager em) {
        SubPipelineCloseStage subPipelineCloseStage = new SubPipelineCloseStage().index(DEFAULT_INDEX);
        // Add required entity
        MasterStaticType masterStaticType;
        if (TestUtil.findAll(em, MasterStaticType.class).isEmpty()) {
            masterStaticType = MasterStaticTypeResourceIT.createEntity(em);
            em.persist(masterStaticType);
            em.flush();
        } else {
            masterStaticType = TestUtil.findAll(em, MasterStaticType.class).get(0);
        }
        subPipelineCloseStage.setStage(masterStaticType);
        // Add required entity
        SubPipeline subPipeline;
        if (TestUtil.findAll(em, SubPipeline.class).isEmpty()) {
            subPipeline = SubPipelineResourceIT.createEntity(em);
            em.persist(subPipeline);
            em.flush();
        } else {
            subPipeline = TestUtil.findAll(em, SubPipeline.class).get(0);
        }
        subPipelineCloseStage.setSubPipeline(subPipeline);
        return subPipelineCloseStage;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static SubPipelineCloseStage createUpdatedEntity(EntityManager em) {
        SubPipelineCloseStage subPipelineCloseStage = new SubPipelineCloseStage().index(UPDATED_INDEX);
        // Add required entity
        MasterStaticType masterStaticType;
        if (TestUtil.findAll(em, MasterStaticType.class).isEmpty()) {
            masterStaticType = MasterStaticTypeResourceIT.createUpdatedEntity(em);
            em.persist(masterStaticType);
            em.flush();
        } else {
            masterStaticType = TestUtil.findAll(em, MasterStaticType.class).get(0);
        }
        subPipelineCloseStage.setStage(masterStaticType);
        // Add required entity
        SubPipeline subPipeline;
        if (TestUtil.findAll(em, SubPipeline.class).isEmpty()) {
            subPipeline = SubPipelineResourceIT.createUpdatedEntity(em);
            em.persist(subPipeline);
            em.flush();
        } else {
            subPipeline = TestUtil.findAll(em, SubPipeline.class).get(0);
        }
        subPipelineCloseStage.setSubPipeline(subPipeline);
        return subPipelineCloseStage;
    }

    @BeforeEach
    public void initTest() {
        subPipelineCloseStage = createEntity(em);
    }

    @Test
    @Transactional
    void createSubPipelineCloseStage() throws Exception {
        int databaseSizeBeforeCreate = subPipelineCloseStageRepository.findAll().size();
        // Create the SubPipelineCloseStage
        SubPipelineCloseStageDTO subPipelineCloseStageDTO = subPipelineCloseStageMapper.toDto(subPipelineCloseStage);
        restSubPipelineCloseStageMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(subPipelineCloseStageDTO))
            )
            .andExpect(status().isCreated());

        // Validate the SubPipelineCloseStage in the database
        List<SubPipelineCloseStage> subPipelineCloseStageList = subPipelineCloseStageRepository.findAll();
        assertThat(subPipelineCloseStageList).hasSize(databaseSizeBeforeCreate + 1);
        SubPipelineCloseStage testSubPipelineCloseStage = subPipelineCloseStageList.get(subPipelineCloseStageList.size() - 1);
        assertThat(testSubPipelineCloseStage.getIndex()).isEqualTo(DEFAULT_INDEX);
    }

    @Test
    @Transactional
    void createSubPipelineCloseStageWithExistingId() throws Exception {
        // Create the SubPipelineCloseStage with an existing ID
        subPipelineCloseStage.setId(1L);
        SubPipelineCloseStageDTO subPipelineCloseStageDTO = subPipelineCloseStageMapper.toDto(subPipelineCloseStage);

        int databaseSizeBeforeCreate = subPipelineCloseStageRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restSubPipelineCloseStageMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(subPipelineCloseStageDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the SubPipelineCloseStage in the database
        List<SubPipelineCloseStage> subPipelineCloseStageList = subPipelineCloseStageRepository.findAll();
        assertThat(subPipelineCloseStageList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllSubPipelineCloseStages() throws Exception {
        // Initialize the database
        subPipelineCloseStageRepository.saveAndFlush(subPipelineCloseStage);

        // Get all the subPipelineCloseStageList
        restSubPipelineCloseStageMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(subPipelineCloseStage.getId().intValue())))
            .andExpect(jsonPath("$.[*].index").value(hasItem(DEFAULT_INDEX)));
    }

    @Test
    @Transactional
    void getSubPipelineCloseStage() throws Exception {
        // Initialize the database
        subPipelineCloseStageRepository.saveAndFlush(subPipelineCloseStage);

        // Get the subPipelineCloseStage
        restSubPipelineCloseStageMockMvc
            .perform(get(ENTITY_API_URL_ID, subPipelineCloseStage.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(subPipelineCloseStage.getId().intValue()))
            .andExpect(jsonPath("$.index").value(DEFAULT_INDEX));
    }

    @Test
    @Transactional
    void getSubPipelineCloseStagesByIdFiltering() throws Exception {
        // Initialize the database
        subPipelineCloseStageRepository.saveAndFlush(subPipelineCloseStage);

        Long id = subPipelineCloseStage.getId();

        defaultSubPipelineCloseStageShouldBeFound("id.equals=" + id);
        defaultSubPipelineCloseStageShouldNotBeFound("id.notEquals=" + id);

        defaultSubPipelineCloseStageShouldBeFound("id.greaterThanOrEqual=" + id);
        defaultSubPipelineCloseStageShouldNotBeFound("id.greaterThan=" + id);

        defaultSubPipelineCloseStageShouldBeFound("id.lessThanOrEqual=" + id);
        defaultSubPipelineCloseStageShouldNotBeFound("id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllSubPipelineCloseStagesByIndexIsEqualToSomething() throws Exception {
        // Initialize the database
        subPipelineCloseStageRepository.saveAndFlush(subPipelineCloseStage);

        // Get all the subPipelineCloseStageList where index equals to DEFAULT_INDEX
        defaultSubPipelineCloseStageShouldBeFound("index.equals=" + DEFAULT_INDEX);

        // Get all the subPipelineCloseStageList where index equals to UPDATED_INDEX
        defaultSubPipelineCloseStageShouldNotBeFound("index.equals=" + UPDATED_INDEX);
    }

    @Test
    @Transactional
    void getAllSubPipelineCloseStagesByIndexIsInShouldWork() throws Exception {
        // Initialize the database
        subPipelineCloseStageRepository.saveAndFlush(subPipelineCloseStage);

        // Get all the subPipelineCloseStageList where index in DEFAULT_INDEX or UPDATED_INDEX
        defaultSubPipelineCloseStageShouldBeFound("index.in=" + DEFAULT_INDEX + "," + UPDATED_INDEX);

        // Get all the subPipelineCloseStageList where index equals to UPDATED_INDEX
        defaultSubPipelineCloseStageShouldNotBeFound("index.in=" + UPDATED_INDEX);
    }

    @Test
    @Transactional
    void getAllSubPipelineCloseStagesByIndexIsNullOrNotNull() throws Exception {
        // Initialize the database
        subPipelineCloseStageRepository.saveAndFlush(subPipelineCloseStage);

        // Get all the subPipelineCloseStageList where index is not null
        defaultSubPipelineCloseStageShouldBeFound("index.specified=true");

        // Get all the subPipelineCloseStageList where index is null
        defaultSubPipelineCloseStageShouldNotBeFound("index.specified=false");
    }

    @Test
    @Transactional
    void getAllSubPipelineCloseStagesByIndexIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        subPipelineCloseStageRepository.saveAndFlush(subPipelineCloseStage);

        // Get all the subPipelineCloseStageList where index is greater than or equal to DEFAULT_INDEX
        defaultSubPipelineCloseStageShouldBeFound("index.greaterThanOrEqual=" + DEFAULT_INDEX);

        // Get all the subPipelineCloseStageList where index is greater than or equal to UPDATED_INDEX
        defaultSubPipelineCloseStageShouldNotBeFound("index.greaterThanOrEqual=" + UPDATED_INDEX);
    }

    @Test
    @Transactional
    void getAllSubPipelineCloseStagesByIndexIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        subPipelineCloseStageRepository.saveAndFlush(subPipelineCloseStage);

        // Get all the subPipelineCloseStageList where index is less than or equal to DEFAULT_INDEX
        defaultSubPipelineCloseStageShouldBeFound("index.lessThanOrEqual=" + DEFAULT_INDEX);

        // Get all the subPipelineCloseStageList where index is less than or equal to SMALLER_INDEX
        defaultSubPipelineCloseStageShouldNotBeFound("index.lessThanOrEqual=" + SMALLER_INDEX);
    }

    @Test
    @Transactional
    void getAllSubPipelineCloseStagesByIndexIsLessThanSomething() throws Exception {
        // Initialize the database
        subPipelineCloseStageRepository.saveAndFlush(subPipelineCloseStage);

        // Get all the subPipelineCloseStageList where index is less than DEFAULT_INDEX
        defaultSubPipelineCloseStageShouldNotBeFound("index.lessThan=" + DEFAULT_INDEX);

        // Get all the subPipelineCloseStageList where index is less than UPDATED_INDEX
        defaultSubPipelineCloseStageShouldBeFound("index.lessThan=" + UPDATED_INDEX);
    }

    @Test
    @Transactional
    void getAllSubPipelineCloseStagesByIndexIsGreaterThanSomething() throws Exception {
        // Initialize the database
        subPipelineCloseStageRepository.saveAndFlush(subPipelineCloseStage);

        // Get all the subPipelineCloseStageList where index is greater than DEFAULT_INDEX
        defaultSubPipelineCloseStageShouldNotBeFound("index.greaterThan=" + DEFAULT_INDEX);

        // Get all the subPipelineCloseStageList where index is greater than SMALLER_INDEX
        defaultSubPipelineCloseStageShouldBeFound("index.greaterThan=" + SMALLER_INDEX);
    }

    @Test
    @Transactional
    void getAllSubPipelineCloseStagesByStageIsEqualToSomething() throws Exception {
        MasterStaticType stage;
        if (TestUtil.findAll(em, MasterStaticType.class).isEmpty()) {
            subPipelineCloseStageRepository.saveAndFlush(subPipelineCloseStage);
            stage = MasterStaticTypeResourceIT.createEntity(em);
        } else {
            stage = TestUtil.findAll(em, MasterStaticType.class).get(0);
        }
        em.persist(stage);
        em.flush();
        subPipelineCloseStage.setStage(stage);
        subPipelineCloseStageRepository.saveAndFlush(subPipelineCloseStage);
        Long stageId = stage.getId();
        // Get all the subPipelineCloseStageList where stage equals to stageId
        defaultSubPipelineCloseStageShouldBeFound("stageId.equals=" + stageId);

        // Get all the subPipelineCloseStageList where stage equals to (stageId + 1)
        defaultSubPipelineCloseStageShouldNotBeFound("stageId.equals=" + (stageId + 1));
    }

    @Test
    @Transactional
    void getAllSubPipelineCloseStagesBySubPipelineIsEqualToSomething() throws Exception {
        SubPipeline subPipeline;
        if (TestUtil.findAll(em, SubPipeline.class).isEmpty()) {
            subPipelineCloseStageRepository.saveAndFlush(subPipelineCloseStage);
            subPipeline = SubPipelineResourceIT.createEntity(em);
        } else {
            subPipeline = TestUtil.findAll(em, SubPipeline.class).get(0);
        }
        em.persist(subPipeline);
        em.flush();
        subPipelineCloseStage.setSubPipeline(subPipeline);
        subPipelineCloseStageRepository.saveAndFlush(subPipelineCloseStage);
        Long subPipelineId = subPipeline.getId();
        // Get all the subPipelineCloseStageList where subPipeline equals to subPipelineId
        defaultSubPipelineCloseStageShouldBeFound("subPipelineId.equals=" + subPipelineId);

        // Get all the subPipelineCloseStageList where subPipeline equals to (subPipelineId + 1)
        defaultSubPipelineCloseStageShouldNotBeFound("subPipelineId.equals=" + (subPipelineId + 1));
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultSubPipelineCloseStageShouldBeFound(String filter) throws Exception {
        restSubPipelineCloseStageMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(subPipelineCloseStage.getId().intValue())))
            .andExpect(jsonPath("$.[*].index").value(hasItem(DEFAULT_INDEX)));

        // Check, that the count call also returns 1
        restSubPipelineCloseStageMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultSubPipelineCloseStageShouldNotBeFound(String filter) throws Exception {
        restSubPipelineCloseStageMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restSubPipelineCloseStageMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingSubPipelineCloseStage() throws Exception {
        // Get the subPipelineCloseStage
        restSubPipelineCloseStageMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingSubPipelineCloseStage() throws Exception {
        // Initialize the database
        subPipelineCloseStageRepository.saveAndFlush(subPipelineCloseStage);

        int databaseSizeBeforeUpdate = subPipelineCloseStageRepository.findAll().size();

        // Update the subPipelineCloseStage
        SubPipelineCloseStage updatedSubPipelineCloseStage = subPipelineCloseStageRepository
            .findById(subPipelineCloseStage.getId())
            .orElseThrow();
        // Disconnect from session so that the updates on updatedSubPipelineCloseStage are not directly saved in db
        em.detach(updatedSubPipelineCloseStage);
        updatedSubPipelineCloseStage.index(UPDATED_INDEX);
        SubPipelineCloseStageDTO subPipelineCloseStageDTO = subPipelineCloseStageMapper.toDto(updatedSubPipelineCloseStage);

        restSubPipelineCloseStageMockMvc
            .perform(
                put(ENTITY_API_URL_ID, subPipelineCloseStageDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(subPipelineCloseStageDTO))
            )
            .andExpect(status().isOk());

        // Validate the SubPipelineCloseStage in the database
        List<SubPipelineCloseStage> subPipelineCloseStageList = subPipelineCloseStageRepository.findAll();
        assertThat(subPipelineCloseStageList).hasSize(databaseSizeBeforeUpdate);
        SubPipelineCloseStage testSubPipelineCloseStage = subPipelineCloseStageList.get(subPipelineCloseStageList.size() - 1);
        assertThat(testSubPipelineCloseStage.getIndex()).isEqualTo(UPDATED_INDEX);
    }

    @Test
    @Transactional
    void putNonExistingSubPipelineCloseStage() throws Exception {
        int databaseSizeBeforeUpdate = subPipelineCloseStageRepository.findAll().size();
        subPipelineCloseStage.setId(longCount.incrementAndGet());

        // Create the SubPipelineCloseStage
        SubPipelineCloseStageDTO subPipelineCloseStageDTO = subPipelineCloseStageMapper.toDto(subPipelineCloseStage);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restSubPipelineCloseStageMockMvc
            .perform(
                put(ENTITY_API_URL_ID, subPipelineCloseStageDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(subPipelineCloseStageDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the SubPipelineCloseStage in the database
        List<SubPipelineCloseStage> subPipelineCloseStageList = subPipelineCloseStageRepository.findAll();
        assertThat(subPipelineCloseStageList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchSubPipelineCloseStage() throws Exception {
        int databaseSizeBeforeUpdate = subPipelineCloseStageRepository.findAll().size();
        subPipelineCloseStage.setId(longCount.incrementAndGet());

        // Create the SubPipelineCloseStage
        SubPipelineCloseStageDTO subPipelineCloseStageDTO = subPipelineCloseStageMapper.toDto(subPipelineCloseStage);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restSubPipelineCloseStageMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(subPipelineCloseStageDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the SubPipelineCloseStage in the database
        List<SubPipelineCloseStage> subPipelineCloseStageList = subPipelineCloseStageRepository.findAll();
        assertThat(subPipelineCloseStageList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamSubPipelineCloseStage() throws Exception {
        int databaseSizeBeforeUpdate = subPipelineCloseStageRepository.findAll().size();
        subPipelineCloseStage.setId(longCount.incrementAndGet());

        // Create the SubPipelineCloseStage
        SubPipelineCloseStageDTO subPipelineCloseStageDTO = subPipelineCloseStageMapper.toDto(subPipelineCloseStage);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restSubPipelineCloseStageMockMvc
            .perform(
                put(ENTITY_API_URL)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(subPipelineCloseStageDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the SubPipelineCloseStage in the database
        List<SubPipelineCloseStage> subPipelineCloseStageList = subPipelineCloseStageRepository.findAll();
        assertThat(subPipelineCloseStageList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateSubPipelineCloseStageWithPatch() throws Exception {
        // Initialize the database
        subPipelineCloseStageRepository.saveAndFlush(subPipelineCloseStage);

        int databaseSizeBeforeUpdate = subPipelineCloseStageRepository.findAll().size();

        // Update the subPipelineCloseStage using partial update
        SubPipelineCloseStage partialUpdatedSubPipelineCloseStage = new SubPipelineCloseStage();
        partialUpdatedSubPipelineCloseStage.setId(subPipelineCloseStage.getId());

        partialUpdatedSubPipelineCloseStage.index(UPDATED_INDEX);

        restSubPipelineCloseStageMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedSubPipelineCloseStage.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedSubPipelineCloseStage))
            )
            .andExpect(status().isOk());

        // Validate the SubPipelineCloseStage in the database
        List<SubPipelineCloseStage> subPipelineCloseStageList = subPipelineCloseStageRepository.findAll();
        assertThat(subPipelineCloseStageList).hasSize(databaseSizeBeforeUpdate);
        SubPipelineCloseStage testSubPipelineCloseStage = subPipelineCloseStageList.get(subPipelineCloseStageList.size() - 1);
        assertThat(testSubPipelineCloseStage.getIndex()).isEqualTo(UPDATED_INDEX);
    }

    @Test
    @Transactional
    void fullUpdateSubPipelineCloseStageWithPatch() throws Exception {
        // Initialize the database
        subPipelineCloseStageRepository.saveAndFlush(subPipelineCloseStage);

        int databaseSizeBeforeUpdate = subPipelineCloseStageRepository.findAll().size();

        // Update the subPipelineCloseStage using partial update
        SubPipelineCloseStage partialUpdatedSubPipelineCloseStage = new SubPipelineCloseStage();
        partialUpdatedSubPipelineCloseStage.setId(subPipelineCloseStage.getId());

        partialUpdatedSubPipelineCloseStage.index(UPDATED_INDEX);

        restSubPipelineCloseStageMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedSubPipelineCloseStage.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedSubPipelineCloseStage))
            )
            .andExpect(status().isOk());

        // Validate the SubPipelineCloseStage in the database
        List<SubPipelineCloseStage> subPipelineCloseStageList = subPipelineCloseStageRepository.findAll();
        assertThat(subPipelineCloseStageList).hasSize(databaseSizeBeforeUpdate);
        SubPipelineCloseStage testSubPipelineCloseStage = subPipelineCloseStageList.get(subPipelineCloseStageList.size() - 1);
        assertThat(testSubPipelineCloseStage.getIndex()).isEqualTo(UPDATED_INDEX);
    }

    @Test
    @Transactional
    void patchNonExistingSubPipelineCloseStage() throws Exception {
        int databaseSizeBeforeUpdate = subPipelineCloseStageRepository.findAll().size();
        subPipelineCloseStage.setId(longCount.incrementAndGet());

        // Create the SubPipelineCloseStage
        SubPipelineCloseStageDTO subPipelineCloseStageDTO = subPipelineCloseStageMapper.toDto(subPipelineCloseStage);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restSubPipelineCloseStageMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, subPipelineCloseStageDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(subPipelineCloseStageDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the SubPipelineCloseStage in the database
        List<SubPipelineCloseStage> subPipelineCloseStageList = subPipelineCloseStageRepository.findAll();
        assertThat(subPipelineCloseStageList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchSubPipelineCloseStage() throws Exception {
        int databaseSizeBeforeUpdate = subPipelineCloseStageRepository.findAll().size();
        subPipelineCloseStage.setId(longCount.incrementAndGet());

        // Create the SubPipelineCloseStage
        SubPipelineCloseStageDTO subPipelineCloseStageDTO = subPipelineCloseStageMapper.toDto(subPipelineCloseStage);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restSubPipelineCloseStageMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(subPipelineCloseStageDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the SubPipelineCloseStage in the database
        List<SubPipelineCloseStage> subPipelineCloseStageList = subPipelineCloseStageRepository.findAll();
        assertThat(subPipelineCloseStageList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamSubPipelineCloseStage() throws Exception {
        int databaseSizeBeforeUpdate = subPipelineCloseStageRepository.findAll().size();
        subPipelineCloseStage.setId(longCount.incrementAndGet());

        // Create the SubPipelineCloseStage
        SubPipelineCloseStageDTO subPipelineCloseStageDTO = subPipelineCloseStageMapper.toDto(subPipelineCloseStage);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restSubPipelineCloseStageMockMvc
            .perform(
                patch(ENTITY_API_URL)
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(subPipelineCloseStageDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the SubPipelineCloseStage in the database
        List<SubPipelineCloseStage> subPipelineCloseStageList = subPipelineCloseStageRepository.findAll();
        assertThat(subPipelineCloseStageList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteSubPipelineCloseStage() throws Exception {
        // Initialize the database
        subPipelineCloseStageRepository.saveAndFlush(subPipelineCloseStage);

        int databaseSizeBeforeDelete = subPipelineCloseStageRepository.findAll().size();

        // Delete the subPipelineCloseStage
        restSubPipelineCloseStageMockMvc
            .perform(delete(ENTITY_API_URL_ID, subPipelineCloseStage.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<SubPipelineCloseStage> subPipelineCloseStageList = subPipelineCloseStageRepository.findAll();
        assertThat(subPipelineCloseStageList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
