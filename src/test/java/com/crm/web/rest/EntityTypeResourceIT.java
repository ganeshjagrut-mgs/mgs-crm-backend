package com.crm.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.crm.IntegrationTest;
import com.crm.domain.EntityType;
import com.crm.repository.EntityTypeRepository;
import com.crm.service.dto.EntityTypeDTO;
import com.crm.service.mapper.EntityTypeMapper;
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
 * Integration tests for the {@link EntityTypeResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class EntityTypeResourceIT {

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_LABEL = "AAAAAAAAAA";
    private static final String UPDATED_LABEL = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/entity-types";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private EntityTypeRepository entityTypeRepository;

    @Autowired
    private EntityTypeMapper entityTypeMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restEntityTypeMockMvc;

    private EntityType entityType;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static EntityType createEntity(EntityManager em) {
        EntityType entityType = new EntityType().name(DEFAULT_NAME).label(DEFAULT_LABEL);
        return entityType;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static EntityType createUpdatedEntity(EntityManager em) {
        EntityType entityType = new EntityType().name(UPDATED_NAME).label(UPDATED_LABEL);
        return entityType;
    }

    @BeforeEach
    public void initTest() {
        entityType = createEntity(em);
    }

    @Test
    @Transactional
    void createEntityType() throws Exception {
        int databaseSizeBeforeCreate = entityTypeRepository.findAll().size();
        // Create the EntityType
        EntityTypeDTO entityTypeDTO = entityTypeMapper.toDto(entityType);
        restEntityTypeMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(entityTypeDTO)))
            .andExpect(status().isCreated());

        // Validate the EntityType in the database
        List<EntityType> entityTypeList = entityTypeRepository.findAll();
        assertThat(entityTypeList).hasSize(databaseSizeBeforeCreate + 1);
        EntityType testEntityType = entityTypeList.get(entityTypeList.size() - 1);
        assertThat(testEntityType.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testEntityType.getLabel()).isEqualTo(DEFAULT_LABEL);
    }

    @Test
    @Transactional
    void createEntityTypeWithExistingId() throws Exception {
        // Create the EntityType with an existing ID
        entityType.setId(1L);
        EntityTypeDTO entityTypeDTO = entityTypeMapper.toDto(entityType);

        int databaseSizeBeforeCreate = entityTypeRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restEntityTypeMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(entityTypeDTO)))
            .andExpect(status().isBadRequest());

        // Validate the EntityType in the database
        List<EntityType> entityTypeList = entityTypeRepository.findAll();
        assertThat(entityTypeList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkNameIsRequired() throws Exception {
        int databaseSizeBeforeTest = entityTypeRepository.findAll().size();
        // set the field null
        entityType.setName(null);

        // Create the EntityType, which fails.
        EntityTypeDTO entityTypeDTO = entityTypeMapper.toDto(entityType);

        restEntityTypeMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(entityTypeDTO)))
            .andExpect(status().isBadRequest());

        List<EntityType> entityTypeList = entityTypeRepository.findAll();
        assertThat(entityTypeList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllEntityTypes() throws Exception {
        // Initialize the database
        entityTypeRepository.saveAndFlush(entityType);

        // Get all the entityTypeList
        restEntityTypeMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(entityType.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)))
            .andExpect(jsonPath("$.[*].label").value(hasItem(DEFAULT_LABEL)));
    }

    @Test
    @Transactional
    void getEntityType() throws Exception {
        // Initialize the database
        entityTypeRepository.saveAndFlush(entityType);

        // Get the entityType
        restEntityTypeMockMvc
            .perform(get(ENTITY_API_URL_ID, entityType.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(entityType.getId().intValue()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME))
            .andExpect(jsonPath("$.label").value(DEFAULT_LABEL));
    }

    @Test
    @Transactional
    void getEntityTypesByIdFiltering() throws Exception {
        // Initialize the database
        entityTypeRepository.saveAndFlush(entityType);

        Long id = entityType.getId();

        defaultEntityTypeShouldBeFound("id.equals=" + id);
        defaultEntityTypeShouldNotBeFound("id.notEquals=" + id);

        defaultEntityTypeShouldBeFound("id.greaterThanOrEqual=" + id);
        defaultEntityTypeShouldNotBeFound("id.greaterThan=" + id);

        defaultEntityTypeShouldBeFound("id.lessThanOrEqual=" + id);
        defaultEntityTypeShouldNotBeFound("id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllEntityTypesByNameIsEqualToSomething() throws Exception {
        // Initialize the database
        entityTypeRepository.saveAndFlush(entityType);

        // Get all the entityTypeList where name equals to DEFAULT_NAME
        defaultEntityTypeShouldBeFound("name.equals=" + DEFAULT_NAME);

        // Get all the entityTypeList where name equals to UPDATED_NAME
        defaultEntityTypeShouldNotBeFound("name.equals=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllEntityTypesByNameIsInShouldWork() throws Exception {
        // Initialize the database
        entityTypeRepository.saveAndFlush(entityType);

        // Get all the entityTypeList where name in DEFAULT_NAME or UPDATED_NAME
        defaultEntityTypeShouldBeFound("name.in=" + DEFAULT_NAME + "," + UPDATED_NAME);

        // Get all the entityTypeList where name equals to UPDATED_NAME
        defaultEntityTypeShouldNotBeFound("name.in=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllEntityTypesByNameIsNullOrNotNull() throws Exception {
        // Initialize the database
        entityTypeRepository.saveAndFlush(entityType);

        // Get all the entityTypeList where name is not null
        defaultEntityTypeShouldBeFound("name.specified=true");

        // Get all the entityTypeList where name is null
        defaultEntityTypeShouldNotBeFound("name.specified=false");
    }

    @Test
    @Transactional
    void getAllEntityTypesByNameContainsSomething() throws Exception {
        // Initialize the database
        entityTypeRepository.saveAndFlush(entityType);

        // Get all the entityTypeList where name contains DEFAULT_NAME
        defaultEntityTypeShouldBeFound("name.contains=" + DEFAULT_NAME);

        // Get all the entityTypeList where name contains UPDATED_NAME
        defaultEntityTypeShouldNotBeFound("name.contains=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllEntityTypesByNameNotContainsSomething() throws Exception {
        // Initialize the database
        entityTypeRepository.saveAndFlush(entityType);

        // Get all the entityTypeList where name does not contain DEFAULT_NAME
        defaultEntityTypeShouldNotBeFound("name.doesNotContain=" + DEFAULT_NAME);

        // Get all the entityTypeList where name does not contain UPDATED_NAME
        defaultEntityTypeShouldBeFound("name.doesNotContain=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllEntityTypesByLabelIsEqualToSomething() throws Exception {
        // Initialize the database
        entityTypeRepository.saveAndFlush(entityType);

        // Get all the entityTypeList where label equals to DEFAULT_LABEL
        defaultEntityTypeShouldBeFound("label.equals=" + DEFAULT_LABEL);

        // Get all the entityTypeList where label equals to UPDATED_LABEL
        defaultEntityTypeShouldNotBeFound("label.equals=" + UPDATED_LABEL);
    }

    @Test
    @Transactional
    void getAllEntityTypesByLabelIsInShouldWork() throws Exception {
        // Initialize the database
        entityTypeRepository.saveAndFlush(entityType);

        // Get all the entityTypeList where label in DEFAULT_LABEL or UPDATED_LABEL
        defaultEntityTypeShouldBeFound("label.in=" + DEFAULT_LABEL + "," + UPDATED_LABEL);

        // Get all the entityTypeList where label equals to UPDATED_LABEL
        defaultEntityTypeShouldNotBeFound("label.in=" + UPDATED_LABEL);
    }

    @Test
    @Transactional
    void getAllEntityTypesByLabelIsNullOrNotNull() throws Exception {
        // Initialize the database
        entityTypeRepository.saveAndFlush(entityType);

        // Get all the entityTypeList where label is not null
        defaultEntityTypeShouldBeFound("label.specified=true");

        // Get all the entityTypeList where label is null
        defaultEntityTypeShouldNotBeFound("label.specified=false");
    }

    @Test
    @Transactional
    void getAllEntityTypesByLabelContainsSomething() throws Exception {
        // Initialize the database
        entityTypeRepository.saveAndFlush(entityType);

        // Get all the entityTypeList where label contains DEFAULT_LABEL
        defaultEntityTypeShouldBeFound("label.contains=" + DEFAULT_LABEL);

        // Get all the entityTypeList where label contains UPDATED_LABEL
        defaultEntityTypeShouldNotBeFound("label.contains=" + UPDATED_LABEL);
    }

    @Test
    @Transactional
    void getAllEntityTypesByLabelNotContainsSomething() throws Exception {
        // Initialize the database
        entityTypeRepository.saveAndFlush(entityType);

        // Get all the entityTypeList where label does not contain DEFAULT_LABEL
        defaultEntityTypeShouldNotBeFound("label.doesNotContain=" + DEFAULT_LABEL);

        // Get all the entityTypeList where label does not contain UPDATED_LABEL
        defaultEntityTypeShouldBeFound("label.doesNotContain=" + UPDATED_LABEL);
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultEntityTypeShouldBeFound(String filter) throws Exception {
        restEntityTypeMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(entityType.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)))
            .andExpect(jsonPath("$.[*].label").value(hasItem(DEFAULT_LABEL)));

        // Check, that the count call also returns 1
        restEntityTypeMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultEntityTypeShouldNotBeFound(String filter) throws Exception {
        restEntityTypeMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restEntityTypeMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingEntityType() throws Exception {
        // Get the entityType
        restEntityTypeMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingEntityType() throws Exception {
        // Initialize the database
        entityTypeRepository.saveAndFlush(entityType);

        int databaseSizeBeforeUpdate = entityTypeRepository.findAll().size();

        // Update the entityType
        EntityType updatedEntityType = entityTypeRepository.findById(entityType.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedEntityType are not directly saved in db
        em.detach(updatedEntityType);
        updatedEntityType.name(UPDATED_NAME).label(UPDATED_LABEL);
        EntityTypeDTO entityTypeDTO = entityTypeMapper.toDto(updatedEntityType);

        restEntityTypeMockMvc
            .perform(
                put(ENTITY_API_URL_ID, entityTypeDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(entityTypeDTO))
            )
            .andExpect(status().isOk());

        // Validate the EntityType in the database
        List<EntityType> entityTypeList = entityTypeRepository.findAll();
        assertThat(entityTypeList).hasSize(databaseSizeBeforeUpdate);
        EntityType testEntityType = entityTypeList.get(entityTypeList.size() - 1);
        assertThat(testEntityType.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testEntityType.getLabel()).isEqualTo(UPDATED_LABEL);
    }

    @Test
    @Transactional
    void putNonExistingEntityType() throws Exception {
        int databaseSizeBeforeUpdate = entityTypeRepository.findAll().size();
        entityType.setId(longCount.incrementAndGet());

        // Create the EntityType
        EntityTypeDTO entityTypeDTO = entityTypeMapper.toDto(entityType);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restEntityTypeMockMvc
            .perform(
                put(ENTITY_API_URL_ID, entityTypeDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(entityTypeDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the EntityType in the database
        List<EntityType> entityTypeList = entityTypeRepository.findAll();
        assertThat(entityTypeList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchEntityType() throws Exception {
        int databaseSizeBeforeUpdate = entityTypeRepository.findAll().size();
        entityType.setId(longCount.incrementAndGet());

        // Create the EntityType
        EntityTypeDTO entityTypeDTO = entityTypeMapper.toDto(entityType);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restEntityTypeMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(entityTypeDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the EntityType in the database
        List<EntityType> entityTypeList = entityTypeRepository.findAll();
        assertThat(entityTypeList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamEntityType() throws Exception {
        int databaseSizeBeforeUpdate = entityTypeRepository.findAll().size();
        entityType.setId(longCount.incrementAndGet());

        // Create the EntityType
        EntityTypeDTO entityTypeDTO = entityTypeMapper.toDto(entityType);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restEntityTypeMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(entityTypeDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the EntityType in the database
        List<EntityType> entityTypeList = entityTypeRepository.findAll();
        assertThat(entityTypeList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateEntityTypeWithPatch() throws Exception {
        // Initialize the database
        entityTypeRepository.saveAndFlush(entityType);

        int databaseSizeBeforeUpdate = entityTypeRepository.findAll().size();

        // Update the entityType using partial update
        EntityType partialUpdatedEntityType = new EntityType();
        partialUpdatedEntityType.setId(entityType.getId());

        partialUpdatedEntityType.name(UPDATED_NAME);

        restEntityTypeMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedEntityType.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedEntityType))
            )
            .andExpect(status().isOk());

        // Validate the EntityType in the database
        List<EntityType> entityTypeList = entityTypeRepository.findAll();
        assertThat(entityTypeList).hasSize(databaseSizeBeforeUpdate);
        EntityType testEntityType = entityTypeList.get(entityTypeList.size() - 1);
        assertThat(testEntityType.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testEntityType.getLabel()).isEqualTo(DEFAULT_LABEL);
    }

    @Test
    @Transactional
    void fullUpdateEntityTypeWithPatch() throws Exception {
        // Initialize the database
        entityTypeRepository.saveAndFlush(entityType);

        int databaseSizeBeforeUpdate = entityTypeRepository.findAll().size();

        // Update the entityType using partial update
        EntityType partialUpdatedEntityType = new EntityType();
        partialUpdatedEntityType.setId(entityType.getId());

        partialUpdatedEntityType.name(UPDATED_NAME).label(UPDATED_LABEL);

        restEntityTypeMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedEntityType.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedEntityType))
            )
            .andExpect(status().isOk());

        // Validate the EntityType in the database
        List<EntityType> entityTypeList = entityTypeRepository.findAll();
        assertThat(entityTypeList).hasSize(databaseSizeBeforeUpdate);
        EntityType testEntityType = entityTypeList.get(entityTypeList.size() - 1);
        assertThat(testEntityType.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testEntityType.getLabel()).isEqualTo(UPDATED_LABEL);
    }

    @Test
    @Transactional
    void patchNonExistingEntityType() throws Exception {
        int databaseSizeBeforeUpdate = entityTypeRepository.findAll().size();
        entityType.setId(longCount.incrementAndGet());

        // Create the EntityType
        EntityTypeDTO entityTypeDTO = entityTypeMapper.toDto(entityType);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restEntityTypeMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, entityTypeDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(entityTypeDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the EntityType in the database
        List<EntityType> entityTypeList = entityTypeRepository.findAll();
        assertThat(entityTypeList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchEntityType() throws Exception {
        int databaseSizeBeforeUpdate = entityTypeRepository.findAll().size();
        entityType.setId(longCount.incrementAndGet());

        // Create the EntityType
        EntityTypeDTO entityTypeDTO = entityTypeMapper.toDto(entityType);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restEntityTypeMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(entityTypeDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the EntityType in the database
        List<EntityType> entityTypeList = entityTypeRepository.findAll();
        assertThat(entityTypeList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamEntityType() throws Exception {
        int databaseSizeBeforeUpdate = entityTypeRepository.findAll().size();
        entityType.setId(longCount.incrementAndGet());

        // Create the EntityType
        EntityTypeDTO entityTypeDTO = entityTypeMapper.toDto(entityType);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restEntityTypeMockMvc
            .perform(
                patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(entityTypeDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the EntityType in the database
        List<EntityType> entityTypeList = entityTypeRepository.findAll();
        assertThat(entityTypeList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteEntityType() throws Exception {
        // Initialize the database
        entityTypeRepository.saveAndFlush(entityType);

        int databaseSizeBeforeDelete = entityTypeRepository.findAll().size();

        // Delete the entityType
        restEntityTypeMockMvc
            .perform(delete(ENTITY_API_URL_ID, entityType.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<EntityType> entityTypeList = entityTypeRepository.findAll();
        assertThat(entityTypeList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
