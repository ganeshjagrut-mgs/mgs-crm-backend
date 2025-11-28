package com.mgs.web.rest;

import static com.mgs.domain.UserRoleAsserts.*;
import static com.mgs.web.rest.TestUtil.createUpdateProxyForBean;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mgs.IntegrationTest;
import com.mgs.domain.Role;
import com.mgs.domain.Tenant;
import com.mgs.domain.User;
import com.mgs.domain.UserRole;
import com.mgs.repository.UserRoleRepository;
import com.mgs.service.dto.UserRoleDTO;
import com.mgs.service.mapper.UserRoleMapper;
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
 * Integration tests for the {@link UserRoleResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class UserRoleResourceIT {

    private static final String ENTITY_API_URL = "/api/user-roles";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ObjectMapper om;

    @Autowired
    private UserRoleRepository userRoleRepository;

    @Autowired
    private UserRoleMapper userRoleMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restUserRoleMockMvc;

    private UserRole userRole;

    private UserRole insertedUserRole;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static UserRole createEntity(EntityManager em) {
        UserRole userRole = new UserRole();
        // Add required entity
        Tenant tenant;
        if (TestUtil.findAll(em, Tenant.class).isEmpty()) {
            tenant = TenantResourceIT.createEntity();
            em.persist(tenant);
            em.flush();
        } else {
            tenant = TestUtil.findAll(em, Tenant.class).get(0);
        }
        userRole.setTenant(tenant);
        // Add required entity
        User user = UserResourceIT.createEntity(em);
        em.persist(user);
        em.flush();
        userRole.setUser(user);
        // Add required entity
        Role role;
        if (TestUtil.findAll(em, Role.class).isEmpty()) {
            role = RoleResourceIT.createEntity(em);
            em.persist(role);
            em.flush();
        } else {
            role = TestUtil.findAll(em, Role.class).get(0);
        }
        userRole.setRole(role);
        return userRole;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static UserRole createUpdatedEntity(EntityManager em) {
        UserRole updatedUserRole = new UserRole();
        // Add required entity
        Tenant tenant;
        if (TestUtil.findAll(em, Tenant.class).isEmpty()) {
            tenant = TenantResourceIT.createUpdatedEntity();
            em.persist(tenant);
            em.flush();
        } else {
            tenant = TestUtil.findAll(em, Tenant.class).get(0);
        }
        updatedUserRole.setTenant(tenant);
        // Add required entity
        User user = UserResourceIT.createEntity(em);
        em.persist(user);
        em.flush();
        updatedUserRole.setUser(user);
        // Add required entity
        Role role;
        if (TestUtil.findAll(em, Role.class).isEmpty()) {
            role = RoleResourceIT.createUpdatedEntity(em);
            em.persist(role);
            em.flush();
        } else {
            role = TestUtil.findAll(em, Role.class).get(0);
        }
        updatedUserRole.setRole(role);
        return updatedUserRole;
    }

    @BeforeEach
    void initTest() {
        userRole = createEntity(em);
    }

    @AfterEach
    void cleanup() {
        if (insertedUserRole != null) {
            userRoleRepository.delete(insertedUserRole);
            insertedUserRole = null;
        }
    }

    @Test
    @Transactional
    void createUserRole() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the UserRole
        UserRoleDTO userRoleDTO = userRoleMapper.toDto(userRole);
        var returnedUserRoleDTO = om.readValue(
            restUserRoleMockMvc
                .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(userRoleDTO)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            UserRoleDTO.class
        );

        // Validate the UserRole in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        var returnedUserRole = userRoleMapper.toEntity(returnedUserRoleDTO);
        assertUserRoleUpdatableFieldsEquals(returnedUserRole, getPersistedUserRole(returnedUserRole));

        insertedUserRole = returnedUserRole;
    }

    @Test
    @Transactional
    void createUserRoleWithExistingId() throws Exception {
        // Create the UserRole with an existing ID
        userRole.setId(1L);
        UserRoleDTO userRoleDTO = userRoleMapper.toDto(userRole);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restUserRoleMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(userRoleDTO)))
            .andExpect(status().isBadRequest());

        // Validate the UserRole in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllUserRoles() throws Exception {
        // Initialize the database
        insertedUserRole = userRoleRepository.saveAndFlush(userRole);

        // Get all the userRoleList
        restUserRoleMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(userRole.getId().intValue())));
    }

    @Test
    @Transactional
    void getUserRole() throws Exception {
        // Initialize the database
        insertedUserRole = userRoleRepository.saveAndFlush(userRole);

        // Get the userRole
        restUserRoleMockMvc
            .perform(get(ENTITY_API_URL_ID, userRole.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(userRole.getId().intValue()));
    }

    @Test
    @Transactional
    void getUserRolesByIdFiltering() throws Exception {
        // Initialize the database
        insertedUserRole = userRoleRepository.saveAndFlush(userRole);

        Long id = userRole.getId();

        defaultUserRoleFiltering("id.equals=" + id, "id.notEquals=" + id);

        defaultUserRoleFiltering("id.greaterThanOrEqual=" + id, "id.greaterThan=" + id);

        defaultUserRoleFiltering("id.lessThanOrEqual=" + id, "id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllUserRolesByTenantIsEqualToSomething() throws Exception {
        Tenant tenant;
        if (TestUtil.findAll(em, Tenant.class).isEmpty()) {
            userRoleRepository.saveAndFlush(userRole);
            tenant = TenantResourceIT.createEntity();
        } else {
            tenant = TestUtil.findAll(em, Tenant.class).get(0);
        }
        em.persist(tenant);
        em.flush();
        userRole.setTenant(tenant);
        userRoleRepository.saveAndFlush(userRole);
        Long tenantId = tenant.getId();
        // Get all the userRoleList where tenant equals to tenantId
        defaultUserRoleShouldBeFound("tenantId.equals=" + tenantId);

        // Get all the userRoleList where tenant equals to (tenantId + 1)
        defaultUserRoleShouldNotBeFound("tenantId.equals=" + (tenantId + 1));
    }

    @Test
    @Transactional
    void getAllUserRolesByUserIsEqualToSomething() throws Exception {
        User user;
        if (TestUtil.findAll(em, User.class).isEmpty()) {
            userRoleRepository.saveAndFlush(userRole);
            user = UserResourceIT.createEntity(em);
        } else {
            user = TestUtil.findAll(em, User.class).get(0);
        }
        em.persist(user);
        em.flush();
        userRole.setUser(user);
        userRoleRepository.saveAndFlush(userRole);
        Long userId = user.getId();
        // Get all the userRoleList where user equals to userId
        defaultUserRoleShouldBeFound("userId.equals=" + userId);

        // Get all the userRoleList where user equals to (userId + 1)
        defaultUserRoleShouldNotBeFound("userId.equals=" + (userId + 1));
    }

    @Test
    @Transactional
    void getAllUserRolesByRoleIsEqualToSomething() throws Exception {
        Role role;
        if (TestUtil.findAll(em, Role.class).isEmpty()) {
            userRoleRepository.saveAndFlush(userRole);
            role = RoleResourceIT.createEntity(em);
        } else {
            role = TestUtil.findAll(em, Role.class).get(0);
        }
        em.persist(role);
        em.flush();
        userRole.setRole(role);
        userRoleRepository.saveAndFlush(userRole);
        Long roleId = role.getId();
        // Get all the userRoleList where role equals to roleId
        defaultUserRoleShouldBeFound("roleId.equals=" + roleId);

        // Get all the userRoleList where role equals to (roleId + 1)
        defaultUserRoleShouldNotBeFound("roleId.equals=" + (roleId + 1));
    }

    private void defaultUserRoleFiltering(String shouldBeFound, String shouldNotBeFound) throws Exception {
        defaultUserRoleShouldBeFound(shouldBeFound);
        defaultUserRoleShouldNotBeFound(shouldNotBeFound);
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultUserRoleShouldBeFound(String filter) throws Exception {
        restUserRoleMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(userRole.getId().intValue())));

        // Check, that the count call also returns 1
        restUserRoleMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultUserRoleShouldNotBeFound(String filter) throws Exception {
        restUserRoleMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restUserRoleMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingUserRole() throws Exception {
        // Get the userRole
        restUserRoleMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingUserRole() throws Exception {
        // Initialize the database
        insertedUserRole = userRoleRepository.saveAndFlush(userRole);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the userRole
        UserRole updatedUserRole = userRoleRepository.findById(userRole.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedUserRole are not directly saved in db
        em.detach(updatedUserRole);
        UserRoleDTO userRoleDTO = userRoleMapper.toDto(updatedUserRole);

        restUserRoleMockMvc
            .perform(
                put(ENTITY_API_URL_ID, userRoleDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(userRoleDTO))
            )
            .andExpect(status().isOk());

        // Validate the UserRole in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedUserRoleToMatchAllProperties(updatedUserRole);
    }

    @Test
    @Transactional
    void putNonExistingUserRole() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        userRole.setId(longCount.incrementAndGet());

        // Create the UserRole
        UserRoleDTO userRoleDTO = userRoleMapper.toDto(userRole);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restUserRoleMockMvc
            .perform(
                put(ENTITY_API_URL_ID, userRoleDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(userRoleDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the UserRole in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchUserRole() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        userRole.setId(longCount.incrementAndGet());

        // Create the UserRole
        UserRoleDTO userRoleDTO = userRoleMapper.toDto(userRole);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restUserRoleMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(userRoleDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the UserRole in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamUserRole() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        userRole.setId(longCount.incrementAndGet());

        // Create the UserRole
        UserRoleDTO userRoleDTO = userRoleMapper.toDto(userRole);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restUserRoleMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(userRoleDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the UserRole in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateUserRoleWithPatch() throws Exception {
        // Initialize the database
        insertedUserRole = userRoleRepository.saveAndFlush(userRole);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the userRole using partial update
        UserRole partialUpdatedUserRole = new UserRole();
        partialUpdatedUserRole.setId(userRole.getId());

        restUserRoleMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedUserRole.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedUserRole))
            )
            .andExpect(status().isOk());

        // Validate the UserRole in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertUserRoleUpdatableFieldsEquals(createUpdateProxyForBean(partialUpdatedUserRole, userRole), getPersistedUserRole(userRole));
    }

    @Test
    @Transactional
    void fullUpdateUserRoleWithPatch() throws Exception {
        // Initialize the database
        insertedUserRole = userRoleRepository.saveAndFlush(userRole);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the userRole using partial update
        UserRole partialUpdatedUserRole = new UserRole();
        partialUpdatedUserRole.setId(userRole.getId());

        restUserRoleMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedUserRole.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedUserRole))
            )
            .andExpect(status().isOk());

        // Validate the UserRole in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertUserRoleUpdatableFieldsEquals(partialUpdatedUserRole, getPersistedUserRole(partialUpdatedUserRole));
    }

    @Test
    @Transactional
    void patchNonExistingUserRole() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        userRole.setId(longCount.incrementAndGet());

        // Create the UserRole
        UserRoleDTO userRoleDTO = userRoleMapper.toDto(userRole);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restUserRoleMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, userRoleDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(userRoleDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the UserRole in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchUserRole() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        userRole.setId(longCount.incrementAndGet());

        // Create the UserRole
        UserRoleDTO userRoleDTO = userRoleMapper.toDto(userRole);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restUserRoleMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(userRoleDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the UserRole in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamUserRole() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        userRole.setId(longCount.incrementAndGet());

        // Create the UserRole
        UserRoleDTO userRoleDTO = userRoleMapper.toDto(userRole);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restUserRoleMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(om.writeValueAsBytes(userRoleDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the UserRole in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteUserRole() throws Exception {
        // Initialize the database
        insertedUserRole = userRoleRepository.saveAndFlush(userRole);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the userRole
        restUserRoleMockMvc
            .perform(delete(ENTITY_API_URL_ID, userRole.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return userRoleRepository.count();
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

    protected UserRole getPersistedUserRole(UserRole userRole) {
        return userRoleRepository.findById(userRole.getId()).orElseThrow();
    }

    protected void assertPersistedUserRoleToMatchAllProperties(UserRole expectedUserRole) {
        assertUserRoleAllPropertiesEquals(expectedUserRole, getPersistedUserRole(expectedUserRole));
    }

    protected void assertPersistedUserRoleToMatchUpdatableProperties(UserRole expectedUserRole) {
        assertUserRoleAllUpdatablePropertiesEquals(expectedUserRole, getPersistedUserRole(expectedUserRole));
    }
}
