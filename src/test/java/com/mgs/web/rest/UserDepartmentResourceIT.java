package com.mgs.web.rest;

import static com.mgs.domain.UserDepartmentAsserts.*;
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
import com.mgs.domain.UserDepartment;
import com.mgs.repository.UserDepartmentRepository;
import com.mgs.service.dto.UserDepartmentDTO;
import com.mgs.service.mapper.UserDepartmentMapper;
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
 * Integration tests for the {@link UserDepartmentResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class UserDepartmentResourceIT {

    private static final String ENTITY_API_URL = "/api/user-departments";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ObjectMapper om;

    @Autowired
    private UserDepartmentRepository userDepartmentRepository;

    @Autowired
    private UserDepartmentMapper userDepartmentMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restUserDepartmentMockMvc;

    private UserDepartment userDepartment;

    private UserDepartment insertedUserDepartment;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static UserDepartment createEntity(EntityManager em) {
        UserDepartment userDepartment = new UserDepartment();
        // Add required entity
        Tenant tenant;
        if (TestUtil.findAll(em, Tenant.class).isEmpty()) {
            tenant = TenantResourceIT.createEntity();
            em.persist(tenant);
            em.flush();
        } else {
            tenant = TestUtil.findAll(em, Tenant.class).get(0);
        }
        userDepartment.setTenant(tenant);
        // Add required entity
        User user = UserResourceIT.createEntity(em);
        em.persist(user);
        em.flush();
        userDepartment.setUser(user);
        // Add required entity
        Department department;
        if (TestUtil.findAll(em, Department.class).isEmpty()) {
            department = DepartmentResourceIT.createEntity(em);
            em.persist(department);
            em.flush();
        } else {
            department = TestUtil.findAll(em, Department.class).get(0);
        }
        userDepartment.setDepartment(department);
        return userDepartment;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static UserDepartment createUpdatedEntity(EntityManager em) {
        UserDepartment updatedUserDepartment = new UserDepartment();
        // Add required entity
        Tenant tenant;
        if (TestUtil.findAll(em, Tenant.class).isEmpty()) {
            tenant = TenantResourceIT.createUpdatedEntity();
            em.persist(tenant);
            em.flush();
        } else {
            tenant = TestUtil.findAll(em, Tenant.class).get(0);
        }
        updatedUserDepartment.setTenant(tenant);
        // Add required entity
        User user = UserResourceIT.createEntity(em);
        em.persist(user);
        em.flush();
        updatedUserDepartment.setUser(user);
        // Add required entity
        Department department;
        if (TestUtil.findAll(em, Department.class).isEmpty()) {
            department = DepartmentResourceIT.createUpdatedEntity(em);
            em.persist(department);
            em.flush();
        } else {
            department = TestUtil.findAll(em, Department.class).get(0);
        }
        updatedUserDepartment.setDepartment(department);
        return updatedUserDepartment;
    }

    @BeforeEach
    void initTest() {
        userDepartment = createEntity(em);
    }

    @AfterEach
    void cleanup() {
        if (insertedUserDepartment != null) {
            userDepartmentRepository.delete(insertedUserDepartment);
            insertedUserDepartment = null;
        }
    }

    @Test
    @Transactional
    void createUserDepartment() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the UserDepartment
        UserDepartmentDTO userDepartmentDTO = userDepartmentMapper.toDto(userDepartment);
        var returnedUserDepartmentDTO = om.readValue(
            restUserDepartmentMockMvc
                .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(userDepartmentDTO)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            UserDepartmentDTO.class
        );

        // Validate the UserDepartment in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        var returnedUserDepartment = userDepartmentMapper.toEntity(returnedUserDepartmentDTO);
        assertUserDepartmentUpdatableFieldsEquals(returnedUserDepartment, getPersistedUserDepartment(returnedUserDepartment));

        insertedUserDepartment = returnedUserDepartment;
    }

    @Test
    @Transactional
    void createUserDepartmentWithExistingId() throws Exception {
        // Create the UserDepartment with an existing ID
        userDepartment.setId(1L);
        UserDepartmentDTO userDepartmentDTO = userDepartmentMapper.toDto(userDepartment);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restUserDepartmentMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(userDepartmentDTO)))
            .andExpect(status().isBadRequest());

        // Validate the UserDepartment in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllUserDepartments() throws Exception {
        // Initialize the database
        insertedUserDepartment = userDepartmentRepository.saveAndFlush(userDepartment);

        // Get all the userDepartmentList
        restUserDepartmentMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(userDepartment.getId().intValue())));
    }

    @Test
    @Transactional
    void getUserDepartment() throws Exception {
        // Initialize the database
        insertedUserDepartment = userDepartmentRepository.saveAndFlush(userDepartment);

        // Get the userDepartment
        restUserDepartmentMockMvc
            .perform(get(ENTITY_API_URL_ID, userDepartment.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(userDepartment.getId().intValue()));
    }

    @Test
    @Transactional
    void getUserDepartmentsByIdFiltering() throws Exception {
        // Initialize the database
        insertedUserDepartment = userDepartmentRepository.saveAndFlush(userDepartment);

        Long id = userDepartment.getId();

        defaultUserDepartmentFiltering("id.equals=" + id, "id.notEquals=" + id);

        defaultUserDepartmentFiltering("id.greaterThanOrEqual=" + id, "id.greaterThan=" + id);

        defaultUserDepartmentFiltering("id.lessThanOrEqual=" + id, "id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllUserDepartmentsByTenantIsEqualToSomething() throws Exception {
        Tenant tenant;
        if (TestUtil.findAll(em, Tenant.class).isEmpty()) {
            userDepartmentRepository.saveAndFlush(userDepartment);
            tenant = TenantResourceIT.createEntity();
        } else {
            tenant = TestUtil.findAll(em, Tenant.class).get(0);
        }
        em.persist(tenant);
        em.flush();
        userDepartment.setTenant(tenant);
        userDepartmentRepository.saveAndFlush(userDepartment);
        Long tenantId = tenant.getId();
        // Get all the userDepartmentList where tenant equals to tenantId
        defaultUserDepartmentShouldBeFound("tenantId.equals=" + tenantId);

        // Get all the userDepartmentList where tenant equals to (tenantId + 1)
        defaultUserDepartmentShouldNotBeFound("tenantId.equals=" + (tenantId + 1));
    }

    @Test
    @Transactional
    void getAllUserDepartmentsByUserIsEqualToSomething() throws Exception {
        User user;
        if (TestUtil.findAll(em, User.class).isEmpty()) {
            userDepartmentRepository.saveAndFlush(userDepartment);
            user = UserResourceIT.createEntity(em);
        } else {
            user = TestUtil.findAll(em, User.class).get(0);
        }
        em.persist(user);
        em.flush();
        userDepartment.setUser(user);
        userDepartmentRepository.saveAndFlush(userDepartment);
        Long userId = user.getId();
        // Get all the userDepartmentList where user equals to userId
        defaultUserDepartmentShouldBeFound("userId.equals=" + userId);

        // Get all the userDepartmentList where user equals to (userId + 1)
        defaultUserDepartmentShouldNotBeFound("userId.equals=" + (userId + 1));
    }

    @Test
    @Transactional
    void getAllUserDepartmentsByDepartmentIsEqualToSomething() throws Exception {
        Department department;
        if (TestUtil.findAll(em, Department.class).isEmpty()) {
            userDepartmentRepository.saveAndFlush(userDepartment);
            department = DepartmentResourceIT.createEntity(em);
        } else {
            department = TestUtil.findAll(em, Department.class).get(0);
        }
        em.persist(department);
        em.flush();
        userDepartment.setDepartment(department);
        userDepartmentRepository.saveAndFlush(userDepartment);
        Long departmentId = department.getId();
        // Get all the userDepartmentList where department equals to departmentId
        defaultUserDepartmentShouldBeFound("departmentId.equals=" + departmentId);

        // Get all the userDepartmentList where department equals to (departmentId + 1)
        defaultUserDepartmentShouldNotBeFound("departmentId.equals=" + (departmentId + 1));
    }

    private void defaultUserDepartmentFiltering(String shouldBeFound, String shouldNotBeFound) throws Exception {
        defaultUserDepartmentShouldBeFound(shouldBeFound);
        defaultUserDepartmentShouldNotBeFound(shouldNotBeFound);
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultUserDepartmentShouldBeFound(String filter) throws Exception {
        restUserDepartmentMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(userDepartment.getId().intValue())));

        // Check, that the count call also returns 1
        restUserDepartmentMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultUserDepartmentShouldNotBeFound(String filter) throws Exception {
        restUserDepartmentMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restUserDepartmentMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingUserDepartment() throws Exception {
        // Get the userDepartment
        restUserDepartmentMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingUserDepartment() throws Exception {
        // Initialize the database
        insertedUserDepartment = userDepartmentRepository.saveAndFlush(userDepartment);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the userDepartment
        UserDepartment updatedUserDepartment = userDepartmentRepository.findById(userDepartment.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedUserDepartment are not directly saved in db
        em.detach(updatedUserDepartment);
        UserDepartmentDTO userDepartmentDTO = userDepartmentMapper.toDto(updatedUserDepartment);

        restUserDepartmentMockMvc
            .perform(
                put(ENTITY_API_URL_ID, userDepartmentDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(userDepartmentDTO))
            )
            .andExpect(status().isOk());

        // Validate the UserDepartment in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedUserDepartmentToMatchAllProperties(updatedUserDepartment);
    }

    @Test
    @Transactional
    void putNonExistingUserDepartment() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        userDepartment.setId(longCount.incrementAndGet());

        // Create the UserDepartment
        UserDepartmentDTO userDepartmentDTO = userDepartmentMapper.toDto(userDepartment);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restUserDepartmentMockMvc
            .perform(
                put(ENTITY_API_URL_ID, userDepartmentDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(userDepartmentDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the UserDepartment in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchUserDepartment() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        userDepartment.setId(longCount.incrementAndGet());

        // Create the UserDepartment
        UserDepartmentDTO userDepartmentDTO = userDepartmentMapper.toDto(userDepartment);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restUserDepartmentMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(userDepartmentDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the UserDepartment in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamUserDepartment() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        userDepartment.setId(longCount.incrementAndGet());

        // Create the UserDepartment
        UserDepartmentDTO userDepartmentDTO = userDepartmentMapper.toDto(userDepartment);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restUserDepartmentMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(userDepartmentDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the UserDepartment in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateUserDepartmentWithPatch() throws Exception {
        // Initialize the database
        insertedUserDepartment = userDepartmentRepository.saveAndFlush(userDepartment);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the userDepartment using partial update
        UserDepartment partialUpdatedUserDepartment = new UserDepartment();
        partialUpdatedUserDepartment.setId(userDepartment.getId());

        restUserDepartmentMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedUserDepartment.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedUserDepartment))
            )
            .andExpect(status().isOk());

        // Validate the UserDepartment in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertUserDepartmentUpdatableFieldsEquals(
            createUpdateProxyForBean(partialUpdatedUserDepartment, userDepartment),
            getPersistedUserDepartment(userDepartment)
        );
    }

    @Test
    @Transactional
    void fullUpdateUserDepartmentWithPatch() throws Exception {
        // Initialize the database
        insertedUserDepartment = userDepartmentRepository.saveAndFlush(userDepartment);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the userDepartment using partial update
        UserDepartment partialUpdatedUserDepartment = new UserDepartment();
        partialUpdatedUserDepartment.setId(userDepartment.getId());

        restUserDepartmentMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedUserDepartment.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedUserDepartment))
            )
            .andExpect(status().isOk());

        // Validate the UserDepartment in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertUserDepartmentUpdatableFieldsEquals(partialUpdatedUserDepartment, getPersistedUserDepartment(partialUpdatedUserDepartment));
    }

    @Test
    @Transactional
    void patchNonExistingUserDepartment() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        userDepartment.setId(longCount.incrementAndGet());

        // Create the UserDepartment
        UserDepartmentDTO userDepartmentDTO = userDepartmentMapper.toDto(userDepartment);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restUserDepartmentMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, userDepartmentDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(userDepartmentDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the UserDepartment in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchUserDepartment() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        userDepartment.setId(longCount.incrementAndGet());

        // Create the UserDepartment
        UserDepartmentDTO userDepartmentDTO = userDepartmentMapper.toDto(userDepartment);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restUserDepartmentMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(userDepartmentDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the UserDepartment in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamUserDepartment() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        userDepartment.setId(longCount.incrementAndGet());

        // Create the UserDepartment
        UserDepartmentDTO userDepartmentDTO = userDepartmentMapper.toDto(userDepartment);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restUserDepartmentMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(om.writeValueAsBytes(userDepartmentDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the UserDepartment in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteUserDepartment() throws Exception {
        // Initialize the database
        insertedUserDepartment = userDepartmentRepository.saveAndFlush(userDepartment);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the userDepartment
        restUserDepartmentMockMvc
            .perform(delete(ENTITY_API_URL_ID, userDepartment.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return userDepartmentRepository.count();
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

    protected UserDepartment getPersistedUserDepartment(UserDepartment userDepartment) {
        return userDepartmentRepository.findById(userDepartment.getId()).orElseThrow();
    }

    protected void assertPersistedUserDepartmentToMatchAllProperties(UserDepartment expectedUserDepartment) {
        assertUserDepartmentAllPropertiesEquals(expectedUserDepartment, getPersistedUserDepartment(expectedUserDepartment));
    }

    protected void assertPersistedUserDepartmentToMatchUpdatableProperties(UserDepartment expectedUserDepartment) {
        assertUserDepartmentAllUpdatablePropertiesEquals(expectedUserDepartment, getPersistedUserDepartment(expectedUserDepartment));
    }
}
