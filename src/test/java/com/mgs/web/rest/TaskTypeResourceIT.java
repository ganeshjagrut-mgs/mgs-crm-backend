package com.mgs.web.rest;

import static com.mgs.domain.TaskTypeAsserts.*;
import static com.mgs.web.rest.TestUtil.createUpdateProxyForBean;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mgs.IntegrationTest;
import com.mgs.domain.TaskType;
import com.mgs.domain.Tenant;
import com.mgs.repository.TaskTypeRepository;
import com.mgs.service.dto.TaskTypeDTO;
import com.mgs.service.mapper.TaskTypeMapper;
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
 * Integration tests for the {@link TaskTypeResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class TaskTypeResourceIT {

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    private static final Boolean DEFAULT_IS_ACTIVE = false;
    private static final Boolean UPDATED_IS_ACTIVE = true;

    private static final String ENTITY_API_URL = "/api/task-types";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ObjectMapper om;

    @Autowired
    private TaskTypeRepository taskTypeRepository;

    @Autowired
    private TaskTypeMapper taskTypeMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restTaskTypeMockMvc;

    private TaskType taskType;

    private TaskType insertedTaskType;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static TaskType createEntity(EntityManager em) {
        TaskType taskType = new TaskType().name(DEFAULT_NAME).isActive(DEFAULT_IS_ACTIVE);
        // Add required entity
        Tenant tenant;
        if (TestUtil.findAll(em, Tenant.class).isEmpty()) {
            tenant = TenantResourceIT.createEntity();
            em.persist(tenant);
            em.flush();
        } else {
            tenant = TestUtil.findAll(em, Tenant.class).get(0);
        }
        taskType.setTenant(tenant);
        return taskType;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static TaskType createUpdatedEntity(EntityManager em) {
        TaskType updatedTaskType = new TaskType().name(UPDATED_NAME).isActive(UPDATED_IS_ACTIVE);
        // Add required entity
        Tenant tenant;
        if (TestUtil.findAll(em, Tenant.class).isEmpty()) {
            tenant = TenantResourceIT.createUpdatedEntity();
            em.persist(tenant);
            em.flush();
        } else {
            tenant = TestUtil.findAll(em, Tenant.class).get(0);
        }
        updatedTaskType.setTenant(tenant);
        return updatedTaskType;
    }

    @BeforeEach
    void initTest() {
        taskType = createEntity(em);
    }

    @AfterEach
    void cleanup() {
        if (insertedTaskType != null) {
            taskTypeRepository.delete(insertedTaskType);
            insertedTaskType = null;
        }
    }

    @Test
    @Transactional
    void createTaskType() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the TaskType
        TaskTypeDTO taskTypeDTO = taskTypeMapper.toDto(taskType);
        var returnedTaskTypeDTO = om.readValue(
            restTaskTypeMockMvc
                .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(taskTypeDTO)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            TaskTypeDTO.class
        );

        // Validate the TaskType in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        var returnedTaskType = taskTypeMapper.toEntity(returnedTaskTypeDTO);
        assertTaskTypeUpdatableFieldsEquals(returnedTaskType, getPersistedTaskType(returnedTaskType));

        insertedTaskType = returnedTaskType;
    }

    @Test
    @Transactional
    void createTaskTypeWithExistingId() throws Exception {
        // Create the TaskType with an existing ID
        taskType.setId(1L);
        TaskTypeDTO taskTypeDTO = taskTypeMapper.toDto(taskType);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restTaskTypeMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(taskTypeDTO)))
            .andExpect(status().isBadRequest());

        // Validate the TaskType in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkNameIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        taskType.setName(null);

        // Create the TaskType, which fails.
        TaskTypeDTO taskTypeDTO = taskTypeMapper.toDto(taskType);

        restTaskTypeMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(taskTypeDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllTaskTypes() throws Exception {
        // Initialize the database
        insertedTaskType = taskTypeRepository.saveAndFlush(taskType);

        // Get all the taskTypeList
        restTaskTypeMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(taskType.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)))
            .andExpect(jsonPath("$.[*].isActive").value(hasItem(DEFAULT_IS_ACTIVE)));
    }

    @Test
    @Transactional
    void getTaskType() throws Exception {
        // Initialize the database
        insertedTaskType = taskTypeRepository.saveAndFlush(taskType);

        // Get the taskType
        restTaskTypeMockMvc
            .perform(get(ENTITY_API_URL_ID, taskType.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(taskType.getId().intValue()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME))
            .andExpect(jsonPath("$.isActive").value(DEFAULT_IS_ACTIVE));
    }

    @Test
    @Transactional
    void getTaskTypesByIdFiltering() throws Exception {
        // Initialize the database
        insertedTaskType = taskTypeRepository.saveAndFlush(taskType);

        Long id = taskType.getId();

        defaultTaskTypeFiltering("id.equals=" + id, "id.notEquals=" + id);

        defaultTaskTypeFiltering("id.greaterThanOrEqual=" + id, "id.greaterThan=" + id);

        defaultTaskTypeFiltering("id.lessThanOrEqual=" + id, "id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllTaskTypesByNameIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedTaskType = taskTypeRepository.saveAndFlush(taskType);

        // Get all the taskTypeList where name equals to
        defaultTaskTypeFiltering("name.equals=" + DEFAULT_NAME, "name.equals=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllTaskTypesByNameIsInShouldWork() throws Exception {
        // Initialize the database
        insertedTaskType = taskTypeRepository.saveAndFlush(taskType);

        // Get all the taskTypeList where name in
        defaultTaskTypeFiltering("name.in=" + DEFAULT_NAME + "," + UPDATED_NAME, "name.in=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllTaskTypesByNameIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedTaskType = taskTypeRepository.saveAndFlush(taskType);

        // Get all the taskTypeList where name is not null
        defaultTaskTypeFiltering("name.specified=true", "name.specified=false");
    }

    @Test
    @Transactional
    void getAllTaskTypesByNameContainsSomething() throws Exception {
        // Initialize the database
        insertedTaskType = taskTypeRepository.saveAndFlush(taskType);

        // Get all the taskTypeList where name contains
        defaultTaskTypeFiltering("name.contains=" + DEFAULT_NAME, "name.contains=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllTaskTypesByNameNotContainsSomething() throws Exception {
        // Initialize the database
        insertedTaskType = taskTypeRepository.saveAndFlush(taskType);

        // Get all the taskTypeList where name does not contain
        defaultTaskTypeFiltering("name.doesNotContain=" + UPDATED_NAME, "name.doesNotContain=" + DEFAULT_NAME);
    }

    @Test
    @Transactional
    void getAllTaskTypesByIsActiveIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedTaskType = taskTypeRepository.saveAndFlush(taskType);

        // Get all the taskTypeList where isActive equals to
        defaultTaskTypeFiltering("isActive.equals=" + DEFAULT_IS_ACTIVE, "isActive.equals=" + UPDATED_IS_ACTIVE);
    }

    @Test
    @Transactional
    void getAllTaskTypesByIsActiveIsInShouldWork() throws Exception {
        // Initialize the database
        insertedTaskType = taskTypeRepository.saveAndFlush(taskType);

        // Get all the taskTypeList where isActive in
        defaultTaskTypeFiltering("isActive.in=" + DEFAULT_IS_ACTIVE + "," + UPDATED_IS_ACTIVE, "isActive.in=" + UPDATED_IS_ACTIVE);
    }

    @Test
    @Transactional
    void getAllTaskTypesByIsActiveIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedTaskType = taskTypeRepository.saveAndFlush(taskType);

        // Get all the taskTypeList where isActive is not null
        defaultTaskTypeFiltering("isActive.specified=true", "isActive.specified=false");
    }

    @Test
    @Transactional
    void getAllTaskTypesByTenantIsEqualToSomething() throws Exception {
        Tenant tenant;
        if (TestUtil.findAll(em, Tenant.class).isEmpty()) {
            taskTypeRepository.saveAndFlush(taskType);
            tenant = TenantResourceIT.createEntity();
        } else {
            tenant = TestUtil.findAll(em, Tenant.class).get(0);
        }
        em.persist(tenant);
        em.flush();
        taskType.setTenant(tenant);
        taskTypeRepository.saveAndFlush(taskType);
        Long tenantId = tenant.getId();
        // Get all the taskTypeList where tenant equals to tenantId
        defaultTaskTypeShouldBeFound("tenantId.equals=" + tenantId);

        // Get all the taskTypeList where tenant equals to (tenantId + 1)
        defaultTaskTypeShouldNotBeFound("tenantId.equals=" + (tenantId + 1));
    }

    private void defaultTaskTypeFiltering(String shouldBeFound, String shouldNotBeFound) throws Exception {
        defaultTaskTypeShouldBeFound(shouldBeFound);
        defaultTaskTypeShouldNotBeFound(shouldNotBeFound);
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultTaskTypeShouldBeFound(String filter) throws Exception {
        restTaskTypeMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(taskType.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)))
            .andExpect(jsonPath("$.[*].isActive").value(hasItem(DEFAULT_IS_ACTIVE)));

        // Check, that the count call also returns 1
        restTaskTypeMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultTaskTypeShouldNotBeFound(String filter) throws Exception {
        restTaskTypeMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restTaskTypeMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingTaskType() throws Exception {
        // Get the taskType
        restTaskTypeMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingTaskType() throws Exception {
        // Initialize the database
        insertedTaskType = taskTypeRepository.saveAndFlush(taskType);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the taskType
        TaskType updatedTaskType = taskTypeRepository.findById(taskType.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedTaskType are not directly saved in db
        em.detach(updatedTaskType);
        updatedTaskType.name(UPDATED_NAME).isActive(UPDATED_IS_ACTIVE);
        TaskTypeDTO taskTypeDTO = taskTypeMapper.toDto(updatedTaskType);

        restTaskTypeMockMvc
            .perform(
                put(ENTITY_API_URL_ID, taskTypeDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(taskTypeDTO))
            )
            .andExpect(status().isOk());

        // Validate the TaskType in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedTaskTypeToMatchAllProperties(updatedTaskType);
    }

    @Test
    @Transactional
    void putNonExistingTaskType() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        taskType.setId(longCount.incrementAndGet());

        // Create the TaskType
        TaskTypeDTO taskTypeDTO = taskTypeMapper.toDto(taskType);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restTaskTypeMockMvc
            .perform(
                put(ENTITY_API_URL_ID, taskTypeDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(taskTypeDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the TaskType in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchTaskType() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        taskType.setId(longCount.incrementAndGet());

        // Create the TaskType
        TaskTypeDTO taskTypeDTO = taskTypeMapper.toDto(taskType);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restTaskTypeMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(taskTypeDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the TaskType in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamTaskType() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        taskType.setId(longCount.incrementAndGet());

        // Create the TaskType
        TaskTypeDTO taskTypeDTO = taskTypeMapper.toDto(taskType);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restTaskTypeMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(taskTypeDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the TaskType in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateTaskTypeWithPatch() throws Exception {
        // Initialize the database
        insertedTaskType = taskTypeRepository.saveAndFlush(taskType);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the taskType using partial update
        TaskType partialUpdatedTaskType = new TaskType();
        partialUpdatedTaskType.setId(taskType.getId());

        restTaskTypeMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedTaskType.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedTaskType))
            )
            .andExpect(status().isOk());

        // Validate the TaskType in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertTaskTypeUpdatableFieldsEquals(createUpdateProxyForBean(partialUpdatedTaskType, taskType), getPersistedTaskType(taskType));
    }

    @Test
    @Transactional
    void fullUpdateTaskTypeWithPatch() throws Exception {
        // Initialize the database
        insertedTaskType = taskTypeRepository.saveAndFlush(taskType);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the taskType using partial update
        TaskType partialUpdatedTaskType = new TaskType();
        partialUpdatedTaskType.setId(taskType.getId());

        partialUpdatedTaskType.name(UPDATED_NAME).isActive(UPDATED_IS_ACTIVE);

        restTaskTypeMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedTaskType.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedTaskType))
            )
            .andExpect(status().isOk());

        // Validate the TaskType in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertTaskTypeUpdatableFieldsEquals(partialUpdatedTaskType, getPersistedTaskType(partialUpdatedTaskType));
    }

    @Test
    @Transactional
    void patchNonExistingTaskType() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        taskType.setId(longCount.incrementAndGet());

        // Create the TaskType
        TaskTypeDTO taskTypeDTO = taskTypeMapper.toDto(taskType);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restTaskTypeMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, taskTypeDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(taskTypeDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the TaskType in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchTaskType() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        taskType.setId(longCount.incrementAndGet());

        // Create the TaskType
        TaskTypeDTO taskTypeDTO = taskTypeMapper.toDto(taskType);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restTaskTypeMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(taskTypeDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the TaskType in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamTaskType() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        taskType.setId(longCount.incrementAndGet());

        // Create the TaskType
        TaskTypeDTO taskTypeDTO = taskTypeMapper.toDto(taskType);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restTaskTypeMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(om.writeValueAsBytes(taskTypeDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the TaskType in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteTaskType() throws Exception {
        // Initialize the database
        insertedTaskType = taskTypeRepository.saveAndFlush(taskType);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the taskType
        restTaskTypeMockMvc
            .perform(delete(ENTITY_API_URL_ID, taskType.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return taskTypeRepository.count();
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

    protected TaskType getPersistedTaskType(TaskType taskType) {
        return taskTypeRepository.findById(taskType.getId()).orElseThrow();
    }

    protected void assertPersistedTaskTypeToMatchAllProperties(TaskType expectedTaskType) {
        assertTaskTypeAllPropertiesEquals(expectedTaskType, getPersistedTaskType(expectedTaskType));
    }

    protected void assertPersistedTaskTypeToMatchUpdatableProperties(TaskType expectedTaskType) {
        assertTaskTypeAllUpdatablePropertiesEquals(expectedTaskType, getPersistedTaskType(expectedTaskType));
    }
}
