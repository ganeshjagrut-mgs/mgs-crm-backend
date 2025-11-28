package com.mgs.web.rest;

import static com.mgs.domain.RoleAsserts.*;
import static com.mgs.web.rest.TestUtil.createUpdateProxyForBean;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mgs.IntegrationTest;
import com.mgs.domain.Role;
import com.mgs.domain.Tenant;
import com.mgs.repository.RoleRepository;
import com.mgs.service.dto.RoleDTO;
import com.mgs.service.mapper.RoleMapper;
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
 * Integration tests for the {@link RoleResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class RoleResourceIT {

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_CODE = "AAAAAAAAAA";
    private static final String UPDATED_CODE = "BBBBBBBBBB";

    private static final Boolean DEFAULT_IS_SYSTEM = false;
    private static final Boolean UPDATED_IS_SYSTEM = true;

    private static final Integer DEFAULT_ROLE_LEVEL = 1;
    private static final Integer UPDATED_ROLE_LEVEL = 2;
    private static final Integer SMALLER_ROLE_LEVEL = 1 - 1;

    private static final String ENTITY_API_URL = "/api/roles";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ObjectMapper om;

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private RoleMapper roleMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restRoleMockMvc;

    private Role role;

    private Role insertedRole;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Role createEntity(EntityManager em) {
        Role role = new Role().name(DEFAULT_NAME).code(DEFAULT_CODE).isSystem(DEFAULT_IS_SYSTEM).roleLevel(DEFAULT_ROLE_LEVEL);
        // Add required entity
        Tenant tenant;
        if (TestUtil.findAll(em, Tenant.class).isEmpty()) {
            tenant = TenantResourceIT.createEntity();
            em.persist(tenant);
            em.flush();
        } else {
            tenant = TestUtil.findAll(em, Tenant.class).get(0);
        }
        role.setTenant(tenant);
        return role;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Role createUpdatedEntity(EntityManager em) {
        Role updatedRole = new Role().name(UPDATED_NAME).code(UPDATED_CODE).isSystem(UPDATED_IS_SYSTEM).roleLevel(UPDATED_ROLE_LEVEL);
        // Add required entity
        Tenant tenant;
        if (TestUtil.findAll(em, Tenant.class).isEmpty()) {
            tenant = TenantResourceIT.createUpdatedEntity();
            em.persist(tenant);
            em.flush();
        } else {
            tenant = TestUtil.findAll(em, Tenant.class).get(0);
        }
        updatedRole.setTenant(tenant);
        return updatedRole;
    }

    @BeforeEach
    void initTest() {
        role = createEntity(em);
    }

    @AfterEach
    void cleanup() {
        if (insertedRole != null) {
            roleRepository.delete(insertedRole);
            insertedRole = null;
        }
    }

    @Test
    @Transactional
    void createRole() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the Role
        RoleDTO roleDTO = roleMapper.toDto(role);
        var returnedRoleDTO = om.readValue(
            restRoleMockMvc
                .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(roleDTO)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            RoleDTO.class
        );

        // Validate the Role in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        var returnedRole = roleMapper.toEntity(returnedRoleDTO);
        assertRoleUpdatableFieldsEquals(returnedRole, getPersistedRole(returnedRole));

        insertedRole = returnedRole;
    }

    @Test
    @Transactional
    void createRoleWithExistingId() throws Exception {
        // Create the Role with an existing ID
        role.setId(1L);
        RoleDTO roleDTO = roleMapper.toDto(role);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restRoleMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(roleDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Role in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkNameIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        role.setName(null);

        // Create the Role, which fails.
        RoleDTO roleDTO = roleMapper.toDto(role);

        restRoleMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(roleDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllRoles() throws Exception {
        // Initialize the database
        insertedRole = roleRepository.saveAndFlush(role);

        // Get all the roleList
        restRoleMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(role.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)))
            .andExpect(jsonPath("$.[*].code").value(hasItem(DEFAULT_CODE)))
            .andExpect(jsonPath("$.[*].isSystem").value(hasItem(DEFAULT_IS_SYSTEM)))
            .andExpect(jsonPath("$.[*].roleLevel").value(hasItem(DEFAULT_ROLE_LEVEL)));
    }

    @Test
    @Transactional
    void getRole() throws Exception {
        // Initialize the database
        insertedRole = roleRepository.saveAndFlush(role);

        // Get the role
        restRoleMockMvc
            .perform(get(ENTITY_API_URL_ID, role.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(role.getId().intValue()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME))
            .andExpect(jsonPath("$.code").value(DEFAULT_CODE))
            .andExpect(jsonPath("$.isSystem").value(DEFAULT_IS_SYSTEM))
            .andExpect(jsonPath("$.roleLevel").value(DEFAULT_ROLE_LEVEL));
    }

    @Test
    @Transactional
    void getRolesByIdFiltering() throws Exception {
        // Initialize the database
        insertedRole = roleRepository.saveAndFlush(role);

        Long id = role.getId();

        defaultRoleFiltering("id.equals=" + id, "id.notEquals=" + id);

        defaultRoleFiltering("id.greaterThanOrEqual=" + id, "id.greaterThan=" + id);

        defaultRoleFiltering("id.lessThanOrEqual=" + id, "id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllRolesByNameIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedRole = roleRepository.saveAndFlush(role);

        // Get all the roleList where name equals to
        defaultRoleFiltering("name.equals=" + DEFAULT_NAME, "name.equals=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllRolesByNameIsInShouldWork() throws Exception {
        // Initialize the database
        insertedRole = roleRepository.saveAndFlush(role);

        // Get all the roleList where name in
        defaultRoleFiltering("name.in=" + DEFAULT_NAME + "," + UPDATED_NAME, "name.in=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllRolesByNameIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedRole = roleRepository.saveAndFlush(role);

        // Get all the roleList where name is not null
        defaultRoleFiltering("name.specified=true", "name.specified=false");
    }

    @Test
    @Transactional
    void getAllRolesByNameContainsSomething() throws Exception {
        // Initialize the database
        insertedRole = roleRepository.saveAndFlush(role);

        // Get all the roleList where name contains
        defaultRoleFiltering("name.contains=" + DEFAULT_NAME, "name.contains=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllRolesByNameNotContainsSomething() throws Exception {
        // Initialize the database
        insertedRole = roleRepository.saveAndFlush(role);

        // Get all the roleList where name does not contain
        defaultRoleFiltering("name.doesNotContain=" + UPDATED_NAME, "name.doesNotContain=" + DEFAULT_NAME);
    }

    @Test
    @Transactional
    void getAllRolesByCodeIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedRole = roleRepository.saveAndFlush(role);

        // Get all the roleList where code equals to
        defaultRoleFiltering("code.equals=" + DEFAULT_CODE, "code.equals=" + UPDATED_CODE);
    }

    @Test
    @Transactional
    void getAllRolesByCodeIsInShouldWork() throws Exception {
        // Initialize the database
        insertedRole = roleRepository.saveAndFlush(role);

        // Get all the roleList where code in
        defaultRoleFiltering("code.in=" + DEFAULT_CODE + "," + UPDATED_CODE, "code.in=" + UPDATED_CODE);
    }

    @Test
    @Transactional
    void getAllRolesByCodeIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedRole = roleRepository.saveAndFlush(role);

        // Get all the roleList where code is not null
        defaultRoleFiltering("code.specified=true", "code.specified=false");
    }

    @Test
    @Transactional
    void getAllRolesByCodeContainsSomething() throws Exception {
        // Initialize the database
        insertedRole = roleRepository.saveAndFlush(role);

        // Get all the roleList where code contains
        defaultRoleFiltering("code.contains=" + DEFAULT_CODE, "code.contains=" + UPDATED_CODE);
    }

    @Test
    @Transactional
    void getAllRolesByCodeNotContainsSomething() throws Exception {
        // Initialize the database
        insertedRole = roleRepository.saveAndFlush(role);

        // Get all the roleList where code does not contain
        defaultRoleFiltering("code.doesNotContain=" + UPDATED_CODE, "code.doesNotContain=" + DEFAULT_CODE);
    }

    @Test
    @Transactional
    void getAllRolesByIsSystemIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedRole = roleRepository.saveAndFlush(role);

        // Get all the roleList where isSystem equals to
        defaultRoleFiltering("isSystem.equals=" + DEFAULT_IS_SYSTEM, "isSystem.equals=" + UPDATED_IS_SYSTEM);
    }

    @Test
    @Transactional
    void getAllRolesByIsSystemIsInShouldWork() throws Exception {
        // Initialize the database
        insertedRole = roleRepository.saveAndFlush(role);

        // Get all the roleList where isSystem in
        defaultRoleFiltering("isSystem.in=" + DEFAULT_IS_SYSTEM + "," + UPDATED_IS_SYSTEM, "isSystem.in=" + UPDATED_IS_SYSTEM);
    }

    @Test
    @Transactional
    void getAllRolesByIsSystemIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedRole = roleRepository.saveAndFlush(role);

        // Get all the roleList where isSystem is not null
        defaultRoleFiltering("isSystem.specified=true", "isSystem.specified=false");
    }

    @Test
    @Transactional
    void getAllRolesByRoleLevelIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedRole = roleRepository.saveAndFlush(role);

        // Get all the roleList where roleLevel equals to
        defaultRoleFiltering("roleLevel.equals=" + DEFAULT_ROLE_LEVEL, "roleLevel.equals=" + UPDATED_ROLE_LEVEL);
    }

    @Test
    @Transactional
    void getAllRolesByRoleLevelIsInShouldWork() throws Exception {
        // Initialize the database
        insertedRole = roleRepository.saveAndFlush(role);

        // Get all the roleList where roleLevel in
        defaultRoleFiltering("roleLevel.in=" + DEFAULT_ROLE_LEVEL + "," + UPDATED_ROLE_LEVEL, "roleLevel.in=" + UPDATED_ROLE_LEVEL);
    }

    @Test
    @Transactional
    void getAllRolesByRoleLevelIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedRole = roleRepository.saveAndFlush(role);

        // Get all the roleList where roleLevel is not null
        defaultRoleFiltering("roleLevel.specified=true", "roleLevel.specified=false");
    }

    @Test
    @Transactional
    void getAllRolesByRoleLevelIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedRole = roleRepository.saveAndFlush(role);

        // Get all the roleList where roleLevel is greater than or equal to
        defaultRoleFiltering("roleLevel.greaterThanOrEqual=" + DEFAULT_ROLE_LEVEL, "roleLevel.greaterThanOrEqual=" + UPDATED_ROLE_LEVEL);
    }

    @Test
    @Transactional
    void getAllRolesByRoleLevelIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedRole = roleRepository.saveAndFlush(role);

        // Get all the roleList where roleLevel is less than or equal to
        defaultRoleFiltering("roleLevel.lessThanOrEqual=" + DEFAULT_ROLE_LEVEL, "roleLevel.lessThanOrEqual=" + SMALLER_ROLE_LEVEL);
    }

    @Test
    @Transactional
    void getAllRolesByRoleLevelIsLessThanSomething() throws Exception {
        // Initialize the database
        insertedRole = roleRepository.saveAndFlush(role);

        // Get all the roleList where roleLevel is less than
        defaultRoleFiltering("roleLevel.lessThan=" + UPDATED_ROLE_LEVEL, "roleLevel.lessThan=" + DEFAULT_ROLE_LEVEL);
    }

    @Test
    @Transactional
    void getAllRolesByRoleLevelIsGreaterThanSomething() throws Exception {
        // Initialize the database
        insertedRole = roleRepository.saveAndFlush(role);

        // Get all the roleList where roleLevel is greater than
        defaultRoleFiltering("roleLevel.greaterThan=" + SMALLER_ROLE_LEVEL, "roleLevel.greaterThan=" + DEFAULT_ROLE_LEVEL);
    }

    @Test
    @Transactional
    void getAllRolesByTenantIsEqualToSomething() throws Exception {
        Tenant tenant;
        if (TestUtil.findAll(em, Tenant.class).isEmpty()) {
            roleRepository.saveAndFlush(role);
            tenant = TenantResourceIT.createEntity();
        } else {
            tenant = TestUtil.findAll(em, Tenant.class).get(0);
        }
        em.persist(tenant);
        em.flush();
        role.setTenant(tenant);
        roleRepository.saveAndFlush(role);
        Long tenantId = tenant.getId();
        // Get all the roleList where tenant equals to tenantId
        defaultRoleShouldBeFound("tenantId.equals=" + tenantId);

        // Get all the roleList where tenant equals to (tenantId + 1)
        defaultRoleShouldNotBeFound("tenantId.equals=" + (tenantId + 1));
    }

    private void defaultRoleFiltering(String shouldBeFound, String shouldNotBeFound) throws Exception {
        defaultRoleShouldBeFound(shouldBeFound);
        defaultRoleShouldNotBeFound(shouldNotBeFound);
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultRoleShouldBeFound(String filter) throws Exception {
        restRoleMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(role.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)))
            .andExpect(jsonPath("$.[*].code").value(hasItem(DEFAULT_CODE)))
            .andExpect(jsonPath("$.[*].isSystem").value(hasItem(DEFAULT_IS_SYSTEM)))
            .andExpect(jsonPath("$.[*].roleLevel").value(hasItem(DEFAULT_ROLE_LEVEL)));

        // Check, that the count call also returns 1
        restRoleMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultRoleShouldNotBeFound(String filter) throws Exception {
        restRoleMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restRoleMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingRole() throws Exception {
        // Get the role
        restRoleMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingRole() throws Exception {
        // Initialize the database
        insertedRole = roleRepository.saveAndFlush(role);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the role
        Role updatedRole = roleRepository.findById(role.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedRole are not directly saved in db
        em.detach(updatedRole);
        updatedRole.name(UPDATED_NAME).code(UPDATED_CODE).isSystem(UPDATED_IS_SYSTEM).roleLevel(UPDATED_ROLE_LEVEL);
        RoleDTO roleDTO = roleMapper.toDto(updatedRole);

        restRoleMockMvc
            .perform(put(ENTITY_API_URL_ID, roleDTO.getId()).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(roleDTO)))
            .andExpect(status().isOk());

        // Validate the Role in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedRoleToMatchAllProperties(updatedRole);
    }

    @Test
    @Transactional
    void putNonExistingRole() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        role.setId(longCount.incrementAndGet());

        // Create the Role
        RoleDTO roleDTO = roleMapper.toDto(role);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restRoleMockMvc
            .perform(put(ENTITY_API_URL_ID, roleDTO.getId()).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(roleDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Role in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchRole() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        role.setId(longCount.incrementAndGet());

        // Create the Role
        RoleDTO roleDTO = roleMapper.toDto(role);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restRoleMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(roleDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Role in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamRole() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        role.setId(longCount.incrementAndGet());

        // Create the Role
        RoleDTO roleDTO = roleMapper.toDto(role);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restRoleMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(roleDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Role in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateRoleWithPatch() throws Exception {
        // Initialize the database
        insertedRole = roleRepository.saveAndFlush(role);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the role using partial update
        Role partialUpdatedRole = new Role();
        partialUpdatedRole.setId(role.getId());

        partialUpdatedRole.code(UPDATED_CODE).isSystem(UPDATED_IS_SYSTEM);

        restRoleMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedRole.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedRole))
            )
            .andExpect(status().isOk());

        // Validate the Role in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertRoleUpdatableFieldsEquals(createUpdateProxyForBean(partialUpdatedRole, role), getPersistedRole(role));
    }

    @Test
    @Transactional
    void fullUpdateRoleWithPatch() throws Exception {
        // Initialize the database
        insertedRole = roleRepository.saveAndFlush(role);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the role using partial update
        Role partialUpdatedRole = new Role();
        partialUpdatedRole.setId(role.getId());

        partialUpdatedRole.name(UPDATED_NAME).code(UPDATED_CODE).isSystem(UPDATED_IS_SYSTEM).roleLevel(UPDATED_ROLE_LEVEL);

        restRoleMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedRole.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedRole))
            )
            .andExpect(status().isOk());

        // Validate the Role in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertRoleUpdatableFieldsEquals(partialUpdatedRole, getPersistedRole(partialUpdatedRole));
    }

    @Test
    @Transactional
    void patchNonExistingRole() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        role.setId(longCount.incrementAndGet());

        // Create the Role
        RoleDTO roleDTO = roleMapper.toDto(role);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restRoleMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, roleDTO.getId()).contentType("application/merge-patch+json").content(om.writeValueAsBytes(roleDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Role in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchRole() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        role.setId(longCount.incrementAndGet());

        // Create the Role
        RoleDTO roleDTO = roleMapper.toDto(role);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restRoleMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(roleDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Role in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamRole() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        role.setId(longCount.incrementAndGet());

        // Create the Role
        RoleDTO roleDTO = roleMapper.toDto(role);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restRoleMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(om.writeValueAsBytes(roleDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Role in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteRole() throws Exception {
        // Initialize the database
        insertedRole = roleRepository.saveAndFlush(role);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the role
        restRoleMockMvc
            .perform(delete(ENTITY_API_URL_ID, role.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return roleRepository.count();
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

    protected Role getPersistedRole(Role role) {
        return roleRepository.findById(role.getId()).orElseThrow();
    }

    protected void assertPersistedRoleToMatchAllProperties(Role expectedRole) {
        assertRoleAllPropertiesEquals(expectedRole, getPersistedRole(expectedRole));
    }

    protected void assertPersistedRoleToMatchUpdatableProperties(Role expectedRole) {
        assertRoleAllUpdatablePropertiesEquals(expectedRole, getPersistedRole(expectedRole));
    }
}
