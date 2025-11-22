package com.crm.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.crm.IntegrationTest;
import com.crm.domain.Pipeline;
import com.crm.domain.PipelineTag;
import com.crm.repository.PipelineTagRepository;
import com.crm.service.dto.PipelineTagDTO;
import com.crm.service.mapper.PipelineTagMapper;
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
 * Integration tests for the {@link PipelineTagResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class PipelineTagResourceIT {

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/pipeline-tags";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private PipelineTagRepository pipelineTagRepository;

    @Autowired
    private PipelineTagMapper pipelineTagMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restPipelineTagMockMvc;

    private PipelineTag pipelineTag;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static PipelineTag createEntity(EntityManager em) {
        PipelineTag pipelineTag = new PipelineTag().name(DEFAULT_NAME);
        // Add required entity
        Pipeline pipeline;
        if (TestUtil.findAll(em, Pipeline.class).isEmpty()) {
            pipeline = PipelineResourceIT.createEntity(em);
            em.persist(pipeline);
            em.flush();
        } else {
            pipeline = TestUtil.findAll(em, Pipeline.class).get(0);
        }
        pipelineTag.setPipeline(pipeline);
        return pipelineTag;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static PipelineTag createUpdatedEntity(EntityManager em) {
        PipelineTag pipelineTag = new PipelineTag().name(UPDATED_NAME);
        // Add required entity
        Pipeline pipeline;
        if (TestUtil.findAll(em, Pipeline.class).isEmpty()) {
            pipeline = PipelineResourceIT.createUpdatedEntity(em);
            em.persist(pipeline);
            em.flush();
        } else {
            pipeline = TestUtil.findAll(em, Pipeline.class).get(0);
        }
        pipelineTag.setPipeline(pipeline);
        return pipelineTag;
    }

    @BeforeEach
    public void initTest() {
        pipelineTag = createEntity(em);
    }

    @Test
    @Transactional
    void createPipelineTag() throws Exception {
        int databaseSizeBeforeCreate = pipelineTagRepository.findAll().size();
        // Create the PipelineTag
        PipelineTagDTO pipelineTagDTO = pipelineTagMapper.toDto(pipelineTag);
        restPipelineTagMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(pipelineTagDTO))
            )
            .andExpect(status().isCreated());

        // Validate the PipelineTag in the database
        List<PipelineTag> pipelineTagList = pipelineTagRepository.findAll();
        assertThat(pipelineTagList).hasSize(databaseSizeBeforeCreate + 1);
        PipelineTag testPipelineTag = pipelineTagList.get(pipelineTagList.size() - 1);
        assertThat(testPipelineTag.getName()).isEqualTo(DEFAULT_NAME);
    }

    @Test
    @Transactional
    void createPipelineTagWithExistingId() throws Exception {
        // Create the PipelineTag with an existing ID
        pipelineTag.setId(1L);
        PipelineTagDTO pipelineTagDTO = pipelineTagMapper.toDto(pipelineTag);

        int databaseSizeBeforeCreate = pipelineTagRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restPipelineTagMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(pipelineTagDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the PipelineTag in the database
        List<PipelineTag> pipelineTagList = pipelineTagRepository.findAll();
        assertThat(pipelineTagList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkNameIsRequired() throws Exception {
        int databaseSizeBeforeTest = pipelineTagRepository.findAll().size();
        // set the field null
        pipelineTag.setName(null);

        // Create the PipelineTag, which fails.
        PipelineTagDTO pipelineTagDTO = pipelineTagMapper.toDto(pipelineTag);

        restPipelineTagMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(pipelineTagDTO))
            )
            .andExpect(status().isBadRequest());

        List<PipelineTag> pipelineTagList = pipelineTagRepository.findAll();
        assertThat(pipelineTagList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllPipelineTags() throws Exception {
        // Initialize the database
        pipelineTagRepository.saveAndFlush(pipelineTag);

        // Get all the pipelineTagList
        restPipelineTagMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(pipelineTag.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)));
    }

    @Test
    @Transactional
    void getPipelineTag() throws Exception {
        // Initialize the database
        pipelineTagRepository.saveAndFlush(pipelineTag);

        // Get the pipelineTag
        restPipelineTagMockMvc
            .perform(get(ENTITY_API_URL_ID, pipelineTag.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(pipelineTag.getId().intValue()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME));
    }

    @Test
    @Transactional
    void getPipelineTagsByIdFiltering() throws Exception {
        // Initialize the database
        pipelineTagRepository.saveAndFlush(pipelineTag);

        Long id = pipelineTag.getId();

        defaultPipelineTagShouldBeFound("id.equals=" + id);
        defaultPipelineTagShouldNotBeFound("id.notEquals=" + id);

        defaultPipelineTagShouldBeFound("id.greaterThanOrEqual=" + id);
        defaultPipelineTagShouldNotBeFound("id.greaterThan=" + id);

        defaultPipelineTagShouldBeFound("id.lessThanOrEqual=" + id);
        defaultPipelineTagShouldNotBeFound("id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllPipelineTagsByNameIsEqualToSomething() throws Exception {
        // Initialize the database
        pipelineTagRepository.saveAndFlush(pipelineTag);

        // Get all the pipelineTagList where name equals to DEFAULT_NAME
        defaultPipelineTagShouldBeFound("name.equals=" + DEFAULT_NAME);

        // Get all the pipelineTagList where name equals to UPDATED_NAME
        defaultPipelineTagShouldNotBeFound("name.equals=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllPipelineTagsByNameIsInShouldWork() throws Exception {
        // Initialize the database
        pipelineTagRepository.saveAndFlush(pipelineTag);

        // Get all the pipelineTagList where name in DEFAULT_NAME or UPDATED_NAME
        defaultPipelineTagShouldBeFound("name.in=" + DEFAULT_NAME + "," + UPDATED_NAME);

        // Get all the pipelineTagList where name equals to UPDATED_NAME
        defaultPipelineTagShouldNotBeFound("name.in=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllPipelineTagsByNameIsNullOrNotNull() throws Exception {
        // Initialize the database
        pipelineTagRepository.saveAndFlush(pipelineTag);

        // Get all the pipelineTagList where name is not null
        defaultPipelineTagShouldBeFound("name.specified=true");

        // Get all the pipelineTagList where name is null
        defaultPipelineTagShouldNotBeFound("name.specified=false");
    }

    @Test
    @Transactional
    void getAllPipelineTagsByNameContainsSomething() throws Exception {
        // Initialize the database
        pipelineTagRepository.saveAndFlush(pipelineTag);

        // Get all the pipelineTagList where name contains DEFAULT_NAME
        defaultPipelineTagShouldBeFound("name.contains=" + DEFAULT_NAME);

        // Get all the pipelineTagList where name contains UPDATED_NAME
        defaultPipelineTagShouldNotBeFound("name.contains=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllPipelineTagsByNameNotContainsSomething() throws Exception {
        // Initialize the database
        pipelineTagRepository.saveAndFlush(pipelineTag);

        // Get all the pipelineTagList where name does not contain DEFAULT_NAME
        defaultPipelineTagShouldNotBeFound("name.doesNotContain=" + DEFAULT_NAME);

        // Get all the pipelineTagList where name does not contain UPDATED_NAME
        defaultPipelineTagShouldBeFound("name.doesNotContain=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllPipelineTagsByPipelineIsEqualToSomething() throws Exception {
        Pipeline pipeline;
        if (TestUtil.findAll(em, Pipeline.class).isEmpty()) {
            pipelineTagRepository.saveAndFlush(pipelineTag);
            pipeline = PipelineResourceIT.createEntity(em);
        } else {
            pipeline = TestUtil.findAll(em, Pipeline.class).get(0);
        }
        em.persist(pipeline);
        em.flush();
        pipelineTag.setPipeline(pipeline);
        pipelineTagRepository.saveAndFlush(pipelineTag);
        Long pipelineId = pipeline.getId();
        // Get all the pipelineTagList where pipeline equals to pipelineId
        defaultPipelineTagShouldBeFound("pipelineId.equals=" + pipelineId);

        // Get all the pipelineTagList where pipeline equals to (pipelineId + 1)
        defaultPipelineTagShouldNotBeFound("pipelineId.equals=" + (pipelineId + 1));
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultPipelineTagShouldBeFound(String filter) throws Exception {
        restPipelineTagMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(pipelineTag.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)));

        // Check, that the count call also returns 1
        restPipelineTagMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultPipelineTagShouldNotBeFound(String filter) throws Exception {
        restPipelineTagMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restPipelineTagMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingPipelineTag() throws Exception {
        // Get the pipelineTag
        restPipelineTagMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingPipelineTag() throws Exception {
        // Initialize the database
        pipelineTagRepository.saveAndFlush(pipelineTag);

        int databaseSizeBeforeUpdate = pipelineTagRepository.findAll().size();

        // Update the pipelineTag
        PipelineTag updatedPipelineTag = pipelineTagRepository.findById(pipelineTag.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedPipelineTag are not directly saved in db
        em.detach(updatedPipelineTag);
        updatedPipelineTag.name(UPDATED_NAME);
        PipelineTagDTO pipelineTagDTO = pipelineTagMapper.toDto(updatedPipelineTag);

        restPipelineTagMockMvc
            .perform(
                put(ENTITY_API_URL_ID, pipelineTagDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(pipelineTagDTO))
            )
            .andExpect(status().isOk());

        // Validate the PipelineTag in the database
        List<PipelineTag> pipelineTagList = pipelineTagRepository.findAll();
        assertThat(pipelineTagList).hasSize(databaseSizeBeforeUpdate);
        PipelineTag testPipelineTag = pipelineTagList.get(pipelineTagList.size() - 1);
        assertThat(testPipelineTag.getName()).isEqualTo(UPDATED_NAME);
    }

    @Test
    @Transactional
    void putNonExistingPipelineTag() throws Exception {
        int databaseSizeBeforeUpdate = pipelineTagRepository.findAll().size();
        pipelineTag.setId(longCount.incrementAndGet());

        // Create the PipelineTag
        PipelineTagDTO pipelineTagDTO = pipelineTagMapper.toDto(pipelineTag);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restPipelineTagMockMvc
            .perform(
                put(ENTITY_API_URL_ID, pipelineTagDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(pipelineTagDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the PipelineTag in the database
        List<PipelineTag> pipelineTagList = pipelineTagRepository.findAll();
        assertThat(pipelineTagList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchPipelineTag() throws Exception {
        int databaseSizeBeforeUpdate = pipelineTagRepository.findAll().size();
        pipelineTag.setId(longCount.incrementAndGet());

        // Create the PipelineTag
        PipelineTagDTO pipelineTagDTO = pipelineTagMapper.toDto(pipelineTag);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restPipelineTagMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(pipelineTagDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the PipelineTag in the database
        List<PipelineTag> pipelineTagList = pipelineTagRepository.findAll();
        assertThat(pipelineTagList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamPipelineTag() throws Exception {
        int databaseSizeBeforeUpdate = pipelineTagRepository.findAll().size();
        pipelineTag.setId(longCount.incrementAndGet());

        // Create the PipelineTag
        PipelineTagDTO pipelineTagDTO = pipelineTagMapper.toDto(pipelineTag);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restPipelineTagMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(pipelineTagDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the PipelineTag in the database
        List<PipelineTag> pipelineTagList = pipelineTagRepository.findAll();
        assertThat(pipelineTagList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdatePipelineTagWithPatch() throws Exception {
        // Initialize the database
        pipelineTagRepository.saveAndFlush(pipelineTag);

        int databaseSizeBeforeUpdate = pipelineTagRepository.findAll().size();

        // Update the pipelineTag using partial update
        PipelineTag partialUpdatedPipelineTag = new PipelineTag();
        partialUpdatedPipelineTag.setId(pipelineTag.getId());

        partialUpdatedPipelineTag.name(UPDATED_NAME);

        restPipelineTagMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedPipelineTag.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedPipelineTag))
            )
            .andExpect(status().isOk());

        // Validate the PipelineTag in the database
        List<PipelineTag> pipelineTagList = pipelineTagRepository.findAll();
        assertThat(pipelineTagList).hasSize(databaseSizeBeforeUpdate);
        PipelineTag testPipelineTag = pipelineTagList.get(pipelineTagList.size() - 1);
        assertThat(testPipelineTag.getName()).isEqualTo(UPDATED_NAME);
    }

    @Test
    @Transactional
    void fullUpdatePipelineTagWithPatch() throws Exception {
        // Initialize the database
        pipelineTagRepository.saveAndFlush(pipelineTag);

        int databaseSizeBeforeUpdate = pipelineTagRepository.findAll().size();

        // Update the pipelineTag using partial update
        PipelineTag partialUpdatedPipelineTag = new PipelineTag();
        partialUpdatedPipelineTag.setId(pipelineTag.getId());

        partialUpdatedPipelineTag.name(UPDATED_NAME);

        restPipelineTagMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedPipelineTag.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedPipelineTag))
            )
            .andExpect(status().isOk());

        // Validate the PipelineTag in the database
        List<PipelineTag> pipelineTagList = pipelineTagRepository.findAll();
        assertThat(pipelineTagList).hasSize(databaseSizeBeforeUpdate);
        PipelineTag testPipelineTag = pipelineTagList.get(pipelineTagList.size() - 1);
        assertThat(testPipelineTag.getName()).isEqualTo(UPDATED_NAME);
    }

    @Test
    @Transactional
    void patchNonExistingPipelineTag() throws Exception {
        int databaseSizeBeforeUpdate = pipelineTagRepository.findAll().size();
        pipelineTag.setId(longCount.incrementAndGet());

        // Create the PipelineTag
        PipelineTagDTO pipelineTagDTO = pipelineTagMapper.toDto(pipelineTag);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restPipelineTagMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, pipelineTagDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(pipelineTagDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the PipelineTag in the database
        List<PipelineTag> pipelineTagList = pipelineTagRepository.findAll();
        assertThat(pipelineTagList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchPipelineTag() throws Exception {
        int databaseSizeBeforeUpdate = pipelineTagRepository.findAll().size();
        pipelineTag.setId(longCount.incrementAndGet());

        // Create the PipelineTag
        PipelineTagDTO pipelineTagDTO = pipelineTagMapper.toDto(pipelineTag);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restPipelineTagMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(pipelineTagDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the PipelineTag in the database
        List<PipelineTag> pipelineTagList = pipelineTagRepository.findAll();
        assertThat(pipelineTagList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamPipelineTag() throws Exception {
        int databaseSizeBeforeUpdate = pipelineTagRepository.findAll().size();
        pipelineTag.setId(longCount.incrementAndGet());

        // Create the PipelineTag
        PipelineTagDTO pipelineTagDTO = pipelineTagMapper.toDto(pipelineTag);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restPipelineTagMockMvc
            .perform(
                patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(pipelineTagDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the PipelineTag in the database
        List<PipelineTag> pipelineTagList = pipelineTagRepository.findAll();
        assertThat(pipelineTagList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deletePipelineTag() throws Exception {
        // Initialize the database
        pipelineTagRepository.saveAndFlush(pipelineTag);

        int databaseSizeBeforeDelete = pipelineTagRepository.findAll().size();

        // Delete the pipelineTag
        restPipelineTagMockMvc
            .perform(delete(ENTITY_API_URL_ID, pipelineTag.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<PipelineTag> pipelineTagList = pipelineTagRepository.findAll();
        assertThat(pipelineTagList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
