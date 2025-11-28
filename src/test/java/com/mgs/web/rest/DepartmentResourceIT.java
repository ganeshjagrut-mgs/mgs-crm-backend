package com.mgs.web.rest;

import static com.mgs.domain.DepartmentAsserts.*;
import static com.mgs.web.rest.TestUtil.createUpdateProxyForBean;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mgs.IntegrationTest;
import com.mgs.domain.Department;
import com.mgs.domain.Tenant;
import com.mgs.domain.User;
import com.mgs.repository.DepartmentRepository;
import com.mgs.service.dto.DepartmentDTO;
import com.mgs.service.mapper.DepartmentMapper;
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
 * Integration tests for the {@link DepartmentResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class DepartmentResourceIT {

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_NAME_SEARCH = "AAAAAAAAAA";
    private static final String UPDATED_NAME_SEARCH = "BBBBBBBBBB";

    private static final String DEFAULT_TYPE = "AAAAAAAAAA";
    private static final String UPDATED_TYPE = "BBBBBBBBBB";

    private static final Boolean DEFAULT_IS_ACTIVE = false;
    private static final Boolean UPDATED_IS_ACTIVE = true;

    private static final String ENTITY_API_URL = "/api/departments";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ObjectMapper om;

    @Autowired
    private DepartmentRepository departmentRepository;

    @Autowired
    private DepartmentMapper departmentMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restDepartmentMockMvc;

    private Department department;

    private Department insertedDepartment;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Department createEntity(EntityManager em) {
        Department department = new Department()
            .name(DEFAULT_NAME)
            .nameSearch(DEFAULT_NAME_SEARCH)
            .type(DEFAULT_TYPE)
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
        department.setTenant(tenant);
        return department;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Department createUpdatedEntity(EntityManager em) {
        Department updatedDepartment = new Department()
            .name(UPDATED_NAME)
            .nameSearch(UPDATED_NAME_SEARCH)
            .type(UPDATED_TYPE)
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
        updatedDepartment.setTenant(tenant);
        return updatedDepartment;
    }

    @BeforeEach
    void initTest() {
        department = createEntity(em);
    }

    @AfterEach
    void cleanup() {
        if (insertedDepartment != null) {
            departmentRepository.delete(insertedDepartment);
            insertedDepartment = null;
        }
    }

    @Test
    @Transactional
    void createDepartment() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the Department
        DepartmentDTO departmentDTO = departmentMapper.toDto(department);
        var returnedDepartmentDTO = om.readValue(
            restDepartmentMockMvc
                .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(departmentDTO)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            DepartmentDTO.class
        );

        // Validate the Department in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        var returnedDepartment = departmentMapper.toEntity(returnedDepartmentDTO);
        assertDepartmentUpdatableFieldsEquals(returnedDepartment, getPersistedDepartment(returnedDepartment));

        insertedDepartment = returnedDepartment;
    }

    @Test
    @Transactional
    void createDepartmentWithExistingId() throws Exception {
        // Create the Department with an existing ID
        department.setId(1L);
        DepartmentDTO departmentDTO = departmentMapper.toDto(department);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restDepartmentMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(departmentDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Department in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkNameIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        department.setName(null);

        // Create the Department, which fails.
        DepartmentDTO departmentDTO = departmentMapper.toDto(department);

        restDepartmentMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(departmentDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllDepartments() throws Exception {
        // Initialize the database
        insertedDepartment = departmentRepository.saveAndFlush(department);

        // Get all the departmentList
        restDepartmentMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(department.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)))
            .andExpect(jsonPath("$.[*].nameSearch").value(hasItem(DEFAULT_NAME_SEARCH)))
            .andExpect(jsonPath("$.[*].type").value(hasItem(DEFAULT_TYPE)))
            .andExpect(jsonPath("$.[*].isActive").value(hasItem(DEFAULT_IS_ACTIVE)));
    }

    @Test
    @Transactional
    void getDepartment() throws Exception {
        // Initialize the database
        insertedDepartment = departmentRepository.saveAndFlush(department);

        // Get the department
        restDepartmentMockMvc
            .perform(get(ENTITY_API_URL_ID, department.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(department.getId().intValue()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME))
            .andExpect(jsonPath("$.nameSearch").value(DEFAULT_NAME_SEARCH))
            .andExpect(jsonPath("$.type").value(DEFAULT_TYPE))
            .andExpect(jsonPath("$.isActive").value(DEFAULT_IS_ACTIVE));
    }

    @Test
    @Transactional
    void getDepartmentsByIdFiltering() throws Exception {
        // Initialize the database
        insertedDepartment = departmentRepository.saveAndFlush(department);

        Long id = department.getId();

        defaultDepartmentFiltering("id.equals=" + id, "id.notEquals=" + id);

        defaultDepartmentFiltering("id.greaterThanOrEqual=" + id, "id.greaterThan=" + id);

        defaultDepartmentFiltering("id.lessThanOrEqual=" + id, "id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllDepartmentsByNameIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedDepartment = departmentRepository.saveAndFlush(department);

        // Get all the departmentList where name equals to
        defaultDepartmentFiltering("name.equals=" + DEFAULT_NAME, "name.equals=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllDepartmentsByNameIsInShouldWork() throws Exception {
        // Initialize the database
        insertedDepartment = departmentRepository.saveAndFlush(department);

        // Get all the departmentList where name in
        defaultDepartmentFiltering("name.in=" + DEFAULT_NAME + "," + UPDATED_NAME, "name.in=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllDepartmentsByNameIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedDepartment = departmentRepository.saveAndFlush(department);

        // Get all the departmentList where name is not null
        defaultDepartmentFiltering("name.specified=true", "name.specified=false");
    }

    @Test
    @Transactional
    void getAllDepartmentsByNameContainsSomething() throws Exception {
        // Initialize the database
        insertedDepartment = departmentRepository.saveAndFlush(department);

        // Get all the departmentList where name contains
        defaultDepartmentFiltering("name.contains=" + DEFAULT_NAME, "name.contains=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllDepartmentsByNameNotContainsSomething() throws Exception {
        // Initialize the database
        insertedDepartment = departmentRepository.saveAndFlush(department);

        // Get all the departmentList where name does not contain
        defaultDepartmentFiltering("name.doesNotContain=" + UPDATED_NAME, "name.doesNotContain=" + DEFAULT_NAME);
    }

    @Test
    @Transactional
    void getAllDepartmentsByNameSearchIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedDepartment = departmentRepository.saveAndFlush(department);

        // Get all the departmentList where nameSearch equals to
        defaultDepartmentFiltering("nameSearch.equals=" + DEFAULT_NAME_SEARCH, "nameSearch.equals=" + UPDATED_NAME_SEARCH);
    }

    @Test
    @Transactional
    void getAllDepartmentsByNameSearchIsInShouldWork() throws Exception {
        // Initialize the database
        insertedDepartment = departmentRepository.saveAndFlush(department);

        // Get all the departmentList where nameSearch in
        defaultDepartmentFiltering(
            "nameSearch.in=" + DEFAULT_NAME_SEARCH + "," + UPDATED_NAME_SEARCH,
            "nameSearch.in=" + UPDATED_NAME_SEARCH
        );
    }

    @Test
    @Transactional
    void getAllDepartmentsByNameSearchIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedDepartment = departmentRepository.saveAndFlush(department);

        // Get all the departmentList where nameSearch is not null
        defaultDepartmentFiltering("nameSearch.specified=true", "nameSearch.specified=false");
    }

    @Test
    @Transactional
    void getAllDepartmentsByNameSearchContainsSomething() throws Exception {
        // Initialize the database
        insertedDepartment = departmentRepository.saveAndFlush(department);

        // Get all the departmentList where nameSearch contains
        defaultDepartmentFiltering("nameSearch.contains=" + DEFAULT_NAME_SEARCH, "nameSearch.contains=" + UPDATED_NAME_SEARCH);
    }

    @Test
    @Transactional
    void getAllDepartmentsByNameSearchNotContainsSomething() throws Exception {
        // Initialize the database
        insertedDepartment = departmentRepository.saveAndFlush(department);

        // Get all the departmentList where nameSearch does not contain
        defaultDepartmentFiltering("nameSearch.doesNotContain=" + UPDATED_NAME_SEARCH, "nameSearch.doesNotContain=" + DEFAULT_NAME_SEARCH);
    }

    @Test
    @Transactional
    void getAllDepartmentsByTypeIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedDepartment = departmentRepository.saveAndFlush(department);

        // Get all the departmentList where type equals to
        defaultDepartmentFiltering("type.equals=" + DEFAULT_TYPE, "type.equals=" + UPDATED_TYPE);
    }

    @Test
    @Transactional
    void getAllDepartmentsByTypeIsInShouldWork() throws Exception {
        // Initialize the database
        insertedDepartment = departmentRepository.saveAndFlush(department);

        // Get all the departmentList where type in
        defaultDepartmentFiltering("type.in=" + DEFAULT_TYPE + "," + UPDATED_TYPE, "type.in=" + UPDATED_TYPE);
    }

    @Test
    @Transactional
    void getAllDepartmentsByTypeIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedDepartment = departmentRepository.saveAndFlush(department);

        // Get all the departmentList where type is not null
        defaultDepartmentFiltering("type.specified=true", "type.specified=false");
    }

    @Test
    @Transactional
    void getAllDepartmentsByTypeContainsSomething() throws Exception {
        // Initialize the database
        insertedDepartment = departmentRepository.saveAndFlush(department);

        // Get all the departmentList where type contains
        defaultDepartmentFiltering("type.contains=" + DEFAULT_TYPE, "type.contains=" + UPDATED_TYPE);
    }

    @Test
    @Transactional
    void getAllDepartmentsByTypeNotContainsSomething() throws Exception {
        // Initialize the database
        insertedDepartment = departmentRepository.saveAndFlush(department);

        // Get all the departmentList where type does not contain
        defaultDepartmentFiltering("type.doesNotContain=" + UPDATED_TYPE, "type.doesNotContain=" + DEFAULT_TYPE);
    }

    @Test
    @Transactional
    void getAllDepartmentsByIsActiveIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedDepartment = departmentRepository.saveAndFlush(department);

        // Get all the departmentList where isActive equals to
        defaultDepartmentFiltering("isActive.equals=" + DEFAULT_IS_ACTIVE, "isActive.equals=" + UPDATED_IS_ACTIVE);
    }

    @Test
    @Transactional
    void getAllDepartmentsByIsActiveIsInShouldWork() throws Exception {
        // Initialize the database
        insertedDepartment = departmentRepository.saveAndFlush(department);

        // Get all the departmentList where isActive in
        defaultDepartmentFiltering("isActive.in=" + DEFAULT_IS_ACTIVE + "," + UPDATED_IS_ACTIVE, "isActive.in=" + UPDATED_IS_ACTIVE);
    }

    @Test
    @Transactional
    void getAllDepartmentsByIsActiveIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedDepartment = departmentRepository.saveAndFlush(department);

        // Get all the departmentList where isActive is not null
        defaultDepartmentFiltering("isActive.specified=true", "isActive.specified=false");
    }

    @Test
    @Transactional
    void getAllDepartmentsByTenantIsEqualToSomething() throws Exception {
        Tenant tenant;
        if (TestUtil.findAll(em, Tenant.class).isEmpty()) {
            departmentRepository.saveAndFlush(department);
            tenant = TenantResourceIT.createEntity();
        } else {
            tenant = TestUtil.findAll(em, Tenant.class).get(0);
        }
        em.persist(tenant);
        em.flush();
        department.setTenant(tenant);
        departmentRepository.saveAndFlush(department);
        Long tenantId = tenant.getId();
        // Get all the departmentList where tenant equals to tenantId
        defaultDepartmentShouldBeFound("tenantId.equals=" + tenantId);

        // Get all the departmentList where tenant equals to (tenantId + 1)
        defaultDepartmentShouldNotBeFound("tenantId.equals=" + (tenantId + 1));
    }

    @Test
    @Transactional
    void getAllDepartmentsByHeadUserIsEqualToSomething() throws Exception {
        User headUser;
        if (TestUtil.findAll(em, User.class).isEmpty()) {
            departmentRepository.saveAndFlush(department);
            headUser = UserResourceIT.createEntity(em);
        } else {
            headUser = TestUtil.findAll(em, User.class).get(0);
        }
        em.persist(headUser);
        em.flush();
        department.setHeadUser(headUser);
        departmentRepository.saveAndFlush(department);
        Long headUserId = headUser.getId();
        // Get all the departmentList where headUser equals to headUserId
        defaultDepartmentShouldBeFound("headUserId.equals=" + headUserId);

        // Get all the departmentList where headUser equals to (headUserId + 1)
        defaultDepartmentShouldNotBeFound("headUserId.equals=" + (headUserId + 1));
    }

    private void defaultDepartmentFiltering(String shouldBeFound, String shouldNotBeFound) throws Exception {
        defaultDepartmentShouldBeFound(shouldBeFound);
        defaultDepartmentShouldNotBeFound(shouldNotBeFound);
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultDepartmentShouldBeFound(String filter) throws Exception {
        restDepartmentMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(department.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)))
            .andExpect(jsonPath("$.[*].nameSearch").value(hasItem(DEFAULT_NAME_SEARCH)))
            .andExpect(jsonPath("$.[*].type").value(hasItem(DEFAULT_TYPE)))
            .andExpect(jsonPath("$.[*].isActive").value(hasItem(DEFAULT_IS_ACTIVE)));

        // Check, that the count call also returns 1
        restDepartmentMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultDepartmentShouldNotBeFound(String filter) throws Exception {
        restDepartmentMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restDepartmentMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingDepartment() throws Exception {
        // Get the department
        restDepartmentMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingDepartment() throws Exception {
        // Initialize the database
        insertedDepartment = departmentRepository.saveAndFlush(department);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the department
        Department updatedDepartment = departmentRepository.findById(department.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedDepartment are not directly saved in db
        em.detach(updatedDepartment);
        updatedDepartment.name(UPDATED_NAME).nameSearch(UPDATED_NAME_SEARCH).type(UPDATED_TYPE).isActive(UPDATED_IS_ACTIVE);
        DepartmentDTO departmentDTO = departmentMapper.toDto(updatedDepartment);

        restDepartmentMockMvc
            .perform(
                put(ENTITY_API_URL_ID, departmentDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(departmentDTO))
            )
            .andExpect(status().isOk());

        // Validate the Department in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedDepartmentToMatchAllProperties(updatedDepartment);
    }

    @Test
    @Transactional
    void putNonExistingDepartment() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        department.setId(longCount.incrementAndGet());

        // Create the Department
        DepartmentDTO departmentDTO = departmentMapper.toDto(department);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restDepartmentMockMvc
            .perform(
                put(ENTITY_API_URL_ID, departmentDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(departmentDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Department in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchDepartment() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        department.setId(longCount.incrementAndGet());

        // Create the Department
        DepartmentDTO departmentDTO = departmentMapper.toDto(department);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restDepartmentMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(departmentDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Department in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamDepartment() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        department.setId(longCount.incrementAndGet());

        // Create the Department
        DepartmentDTO departmentDTO = departmentMapper.toDto(department);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restDepartmentMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(departmentDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Department in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateDepartmentWithPatch() throws Exception {
        // Initialize the database
        insertedDepartment = departmentRepository.saveAndFlush(department);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the department using partial update
        Department partialUpdatedDepartment = new Department();
        partialUpdatedDepartment.setId(department.getId());

        partialUpdatedDepartment.name(UPDATED_NAME).nameSearch(UPDATED_NAME_SEARCH).type(UPDATED_TYPE);

        restDepartmentMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedDepartment.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedDepartment))
            )
            .andExpect(status().isOk());

        // Validate the Department in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertDepartmentUpdatableFieldsEquals(
            createUpdateProxyForBean(partialUpdatedDepartment, department),
            getPersistedDepartment(department)
        );
    }

    @Test
    @Transactional
    void fullUpdateDepartmentWithPatch() throws Exception {
        // Initialize the database
        insertedDepartment = departmentRepository.saveAndFlush(department);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the department using partial update
        Department partialUpdatedDepartment = new Department();
        partialUpdatedDepartment.setId(department.getId());

        partialUpdatedDepartment.name(UPDATED_NAME).nameSearch(UPDATED_NAME_SEARCH).type(UPDATED_TYPE).isActive(UPDATED_IS_ACTIVE);

        restDepartmentMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedDepartment.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedDepartment))
            )
            .andExpect(status().isOk());

        // Validate the Department in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertDepartmentUpdatableFieldsEquals(partialUpdatedDepartment, getPersistedDepartment(partialUpdatedDepartment));
    }

    @Test
    @Transactional
    void patchNonExistingDepartment() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        department.setId(longCount.incrementAndGet());

        // Create the Department
        DepartmentDTO departmentDTO = departmentMapper.toDto(department);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restDepartmentMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, departmentDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(departmentDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Department in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchDepartment() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        department.setId(longCount.incrementAndGet());

        // Create the Department
        DepartmentDTO departmentDTO = departmentMapper.toDto(department);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restDepartmentMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(departmentDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Department in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamDepartment() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        department.setId(longCount.incrementAndGet());

        // Create the Department
        DepartmentDTO departmentDTO = departmentMapper.toDto(department);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restDepartmentMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(om.writeValueAsBytes(departmentDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Department in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteDepartment() throws Exception {
        // Initialize the database
        insertedDepartment = departmentRepository.saveAndFlush(department);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the department
        restDepartmentMockMvc
            .perform(delete(ENTITY_API_URL_ID, department.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return departmentRepository.count();
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

    protected Department getPersistedDepartment(Department department) {
        return departmentRepository.findById(department.getId()).orElseThrow();
    }

    protected void assertPersistedDepartmentToMatchAllProperties(Department expectedDepartment) {
        assertDepartmentAllPropertiesEquals(expectedDepartment, getPersistedDepartment(expectedDepartment));
    }

    protected void assertPersistedDepartmentToMatchUpdatableProperties(Department expectedDepartment) {
        assertDepartmentAllUpdatablePropertiesEquals(expectedDepartment, getPersistedDepartment(expectedDepartment));
    }
}
