package com.mgs.web.rest;

import static com.mgs.domain.RolePermissionAsserts.*;
import static com.mgs.web.rest.TestUtil.createUpdateProxyForBean;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mgs.IntegrationTest;
import com.mgs.domain.PermissionModule;
import com.mgs.domain.Role;
import com.mgs.domain.RolePermission;
import com.mgs.repository.RolePermissionRepository;
import com.mgs.service.dto.RolePermissionDTO;
import com.mgs.service.mapper.RolePermissionMapper;
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
 * Integration tests for the {@link RolePermissionResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class RolePermissionResourceIT {

    private static final Boolean DEFAULT_CAN_READ = false;
    private static final Boolean UPDATED_CAN_READ = true;

    private static final Boolean DEFAULT_CAN_CREATE = false;
    private static final Boolean UPDATED_CAN_CREATE = true;

    private static final Boolean DEFAULT_CAN_UPDATE = false;
    private static final Boolean UPDATED_CAN_UPDATE = true;

    private static final Boolean DEFAULT_CAN_DELETE = false;
    private static final Boolean UPDATED_CAN_DELETE = true;

    private static final String ENTITY_API_URL = "/api/role-permissions";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ObjectMapper om;

    @Autowired
    private RolePermissionRepository rolePermissionRepository;

    @Autowired
    private RolePermissionMapper rolePermissionMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restRolePermissionMockMvc;

    private RolePermission rolePermission;

    private RolePermission insertedRolePermission;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static RolePermission createEntity(EntityManager em) {
        RolePermission rolePermission = new RolePermission()
            .canRead(DEFAULT_CAN_READ)
            .canCreate(DEFAULT_CAN_CREATE)
            .canUpdate(DEFAULT_CAN_UPDATE)
            .canDelete(DEFAULT_CAN_DELETE);
        // Add required entity
        Role role;
        if (TestUtil.findAll(em, Role.class).isEmpty()) {
            role = RoleResourceIT.createEntity(em);
            em.persist(role);
            em.flush();
        } else {
            role = TestUtil.findAll(em, Role.class).get(0);
        }
        rolePermission.setRole(role);
        // Add required entity
        PermissionModule permissionModule;
        if (TestUtil.findAll(em, PermissionModule.class).isEmpty()) {
            permissionModule = PermissionModuleResourceIT.createEntity();
            em.persist(permissionModule);
            em.flush();
        } else {
            permissionModule = TestUtil.findAll(em, PermissionModule.class).get(0);
        }
        rolePermission.setModule(permissionModule);
        return rolePermission;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static RolePermission createUpdatedEntity(EntityManager em) {
        RolePermission updatedRolePermission = new RolePermission()
            .canRead(UPDATED_CAN_READ)
            .canCreate(UPDATED_CAN_CREATE)
            .canUpdate(UPDATED_CAN_UPDATE)
            .canDelete(UPDATED_CAN_DELETE);
        // Add required entity
        Role role;
        if (TestUtil.findAll(em, Role.class).isEmpty()) {
            role = RoleResourceIT.createUpdatedEntity(em);
            em.persist(role);
            em.flush();
        } else {
            role = TestUtil.findAll(em, Role.class).get(0);
        }
        updatedRolePermission.setRole(role);
        // Add required entity
        PermissionModule permissionModule;
        if (TestUtil.findAll(em, PermissionModule.class).isEmpty()) {
            permissionModule = PermissionModuleResourceIT.createUpdatedEntity();
            em.persist(permissionModule);
            em.flush();
        } else {
            permissionModule = TestUtil.findAll(em, PermissionModule.class).get(0);
        }
        updatedRolePermission.setModule(permissionModule);
        return updatedRolePermission;
    }

    @BeforeEach
    void initTest() {
        rolePermission = createEntity(em);
    }

    @AfterEach
    void cleanup() {
        if (insertedRolePermission != null) {
            rolePermissionRepository.delete(insertedRolePermission);
            insertedRolePermission = null;
        }
    }

    @Test
    @Transactional
    void createRolePermission() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the RolePermission
        RolePermissionDTO rolePermissionDTO = rolePermissionMapper.toDto(rolePermission);
        var returnedRolePermissionDTO = om.readValue(
            restRolePermissionMockMvc
                .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(rolePermissionDTO)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            RolePermissionDTO.class
        );

        // Validate the RolePermission in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        var returnedRolePermission = rolePermissionMapper.toEntity(returnedRolePermissionDTO);
        assertRolePermissionUpdatableFieldsEquals(returnedRolePermission, getPersistedRolePermission(returnedRolePermission));

        insertedRolePermission = returnedRolePermission;
    }

    @Test
    @Transactional
    void createRolePermissionWithExistingId() throws Exception {
        // Create the RolePermission with an existing ID
        rolePermission.setId(1L);
        RolePermissionDTO rolePermissionDTO = rolePermissionMapper.toDto(rolePermission);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restRolePermissionMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(rolePermissionDTO)))
            .andExpect(status().isBadRequest());

        // Validate the RolePermission in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkCanReadIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        rolePermission.setCanRead(null);

        // Create the RolePermission, which fails.
        RolePermissionDTO rolePermissionDTO = rolePermissionMapper.toDto(rolePermission);

        restRolePermissionMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(rolePermissionDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkCanCreateIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        rolePermission.setCanCreate(null);

        // Create the RolePermission, which fails.
        RolePermissionDTO rolePermissionDTO = rolePermissionMapper.toDto(rolePermission);

        restRolePermissionMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(rolePermissionDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkCanUpdateIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        rolePermission.setCanUpdate(null);

        // Create the RolePermission, which fails.
        RolePermissionDTO rolePermissionDTO = rolePermissionMapper.toDto(rolePermission);

        restRolePermissionMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(rolePermissionDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkCanDeleteIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        rolePermission.setCanDelete(null);

        // Create the RolePermission, which fails.
        RolePermissionDTO rolePermissionDTO = rolePermissionMapper.toDto(rolePermission);

        restRolePermissionMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(rolePermissionDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllRolePermissions() throws Exception {
        // Initialize the database
        insertedRolePermission = rolePermissionRepository.saveAndFlush(rolePermission);

        // Get all the rolePermissionList
        restRolePermissionMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(rolePermission.getId().intValue())))
            .andExpect(jsonPath("$.[*].canRead").value(hasItem(DEFAULT_CAN_READ)))
            .andExpect(jsonPath("$.[*].canCreate").value(hasItem(DEFAULT_CAN_CREATE)))
            .andExpect(jsonPath("$.[*].canUpdate").value(hasItem(DEFAULT_CAN_UPDATE)))
            .andExpect(jsonPath("$.[*].canDelete").value(hasItem(DEFAULT_CAN_DELETE)));
    }

    @Test
    @Transactional
    void getRolePermission() throws Exception {
        // Initialize the database
        insertedRolePermission = rolePermissionRepository.saveAndFlush(rolePermission);

        // Get the rolePermission
        restRolePermissionMockMvc
            .perform(get(ENTITY_API_URL_ID, rolePermission.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(rolePermission.getId().intValue()))
            .andExpect(jsonPath("$.canRead").value(DEFAULT_CAN_READ))
            .andExpect(jsonPath("$.canCreate").value(DEFAULT_CAN_CREATE))
            .andExpect(jsonPath("$.canUpdate").value(DEFAULT_CAN_UPDATE))
            .andExpect(jsonPath("$.canDelete").value(DEFAULT_CAN_DELETE));
    }

    @Test
    @Transactional
    void getRolePermissionsByIdFiltering() throws Exception {
        // Initialize the database
        insertedRolePermission = rolePermissionRepository.saveAndFlush(rolePermission);

        Long id = rolePermission.getId();

        defaultRolePermissionFiltering("id.equals=" + id, "id.notEquals=" + id);

        defaultRolePermissionFiltering("id.greaterThanOrEqual=" + id, "id.greaterThan=" + id);

        defaultRolePermissionFiltering("id.lessThanOrEqual=" + id, "id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllRolePermissionsByCanReadIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedRolePermission = rolePermissionRepository.saveAndFlush(rolePermission);

        // Get all the rolePermissionList where canRead equals to
        defaultRolePermissionFiltering("canRead.equals=" + DEFAULT_CAN_READ, "canRead.equals=" + UPDATED_CAN_READ);
    }

    @Test
    @Transactional
    void getAllRolePermissionsByCanReadIsInShouldWork() throws Exception {
        // Initialize the database
        insertedRolePermission = rolePermissionRepository.saveAndFlush(rolePermission);

        // Get all the rolePermissionList where canRead in
        defaultRolePermissionFiltering("canRead.in=" + DEFAULT_CAN_READ + "," + UPDATED_CAN_READ, "canRead.in=" + UPDATED_CAN_READ);
    }

    @Test
    @Transactional
    void getAllRolePermissionsByCanReadIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedRolePermission = rolePermissionRepository.saveAndFlush(rolePermission);

        // Get all the rolePermissionList where canRead is not null
        defaultRolePermissionFiltering("canRead.specified=true", "canRead.specified=false");
    }

    @Test
    @Transactional
    void getAllRolePermissionsByCanCreateIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedRolePermission = rolePermissionRepository.saveAndFlush(rolePermission);

        // Get all the rolePermissionList where canCreate equals to
        defaultRolePermissionFiltering("canCreate.equals=" + DEFAULT_CAN_CREATE, "canCreate.equals=" + UPDATED_CAN_CREATE);
    }

    @Test
    @Transactional
    void getAllRolePermissionsByCanCreateIsInShouldWork() throws Exception {
        // Initialize the database
        insertedRolePermission = rolePermissionRepository.saveAndFlush(rolePermission);

        // Get all the rolePermissionList where canCreate in
        defaultRolePermissionFiltering(
            "canCreate.in=" + DEFAULT_CAN_CREATE + "," + UPDATED_CAN_CREATE,
            "canCreate.in=" + UPDATED_CAN_CREATE
        );
    }

    @Test
    @Transactional
    void getAllRolePermissionsByCanCreateIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedRolePermission = rolePermissionRepository.saveAndFlush(rolePermission);

        // Get all the rolePermissionList where canCreate is not null
        defaultRolePermissionFiltering("canCreate.specified=true", "canCreate.specified=false");
    }

    @Test
    @Transactional
    void getAllRolePermissionsByCanUpdateIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedRolePermission = rolePermissionRepository.saveAndFlush(rolePermission);

        // Get all the rolePermissionList where canUpdate equals to
        defaultRolePermissionFiltering("canUpdate.equals=" + DEFAULT_CAN_UPDATE, "canUpdate.equals=" + UPDATED_CAN_UPDATE);
    }

    @Test
    @Transactional
    void getAllRolePermissionsByCanUpdateIsInShouldWork() throws Exception {
        // Initialize the database
        insertedRolePermission = rolePermissionRepository.saveAndFlush(rolePermission);

        // Get all the rolePermissionList where canUpdate in
        defaultRolePermissionFiltering(
            "canUpdate.in=" + DEFAULT_CAN_UPDATE + "," + UPDATED_CAN_UPDATE,
            "canUpdate.in=" + UPDATED_CAN_UPDATE
        );
    }

    @Test
    @Transactional
    void getAllRolePermissionsByCanUpdateIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedRolePermission = rolePermissionRepository.saveAndFlush(rolePermission);

        // Get all the rolePermissionList where canUpdate is not null
        defaultRolePermissionFiltering("canUpdate.specified=true", "canUpdate.specified=false");
    }

    @Test
    @Transactional
    void getAllRolePermissionsByCanDeleteIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedRolePermission = rolePermissionRepository.saveAndFlush(rolePermission);

        // Get all the rolePermissionList where canDelete equals to
        defaultRolePermissionFiltering("canDelete.equals=" + DEFAULT_CAN_DELETE, "canDelete.equals=" + UPDATED_CAN_DELETE);
    }

    @Test
    @Transactional
    void getAllRolePermissionsByCanDeleteIsInShouldWork() throws Exception {
        // Initialize the database
        insertedRolePermission = rolePermissionRepository.saveAndFlush(rolePermission);

        // Get all the rolePermissionList where canDelete in
        defaultRolePermissionFiltering(
            "canDelete.in=" + DEFAULT_CAN_DELETE + "," + UPDATED_CAN_DELETE,
            "canDelete.in=" + UPDATED_CAN_DELETE
        );
    }

    @Test
    @Transactional
    void getAllRolePermissionsByCanDeleteIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedRolePermission = rolePermissionRepository.saveAndFlush(rolePermission);

        // Get all the rolePermissionList where canDelete is not null
        defaultRolePermissionFiltering("canDelete.specified=true", "canDelete.specified=false");
    }

    @Test
    @Transactional
    void getAllRolePermissionsByRoleIsEqualToSomething() throws Exception {
        Role role;
        if (TestUtil.findAll(em, Role.class).isEmpty()) {
            rolePermissionRepository.saveAndFlush(rolePermission);
            role = RoleResourceIT.createEntity(em);
        } else {
            role = TestUtil.findAll(em, Role.class).get(0);
        }
        em.persist(role);
        em.flush();
        rolePermission.setRole(role);
        rolePermissionRepository.saveAndFlush(rolePermission);
        Long roleId = role.getId();
        // Get all the rolePermissionList where role equals to roleId
        defaultRolePermissionShouldBeFound("roleId.equals=" + roleId);

        // Get all the rolePermissionList where role equals to (roleId + 1)
        defaultRolePermissionShouldNotBeFound("roleId.equals=" + (roleId + 1));
    }

    @Test
    @Transactional
    void getAllRolePermissionsByModuleIsEqualToSomething() throws Exception {
        PermissionModule module;
        if (TestUtil.findAll(em, PermissionModule.class).isEmpty()) {
            rolePermissionRepository.saveAndFlush(rolePermission);
            module = PermissionModuleResourceIT.createEntity();
        } else {
            module = TestUtil.findAll(em, PermissionModule.class).get(0);
        }
        em.persist(module);
        em.flush();
        rolePermission.setModule(module);
        rolePermissionRepository.saveAndFlush(rolePermission);
        Long moduleId = module.getId();
        // Get all the rolePermissionList where module equals to moduleId
        defaultRolePermissionShouldBeFound("moduleId.equals=" + moduleId);

        // Get all the rolePermissionList where module equals to (moduleId + 1)
        defaultRolePermissionShouldNotBeFound("moduleId.equals=" + (moduleId + 1));
    }

    private void defaultRolePermissionFiltering(String shouldBeFound, String shouldNotBeFound) throws Exception {
        defaultRolePermissionShouldBeFound(shouldBeFound);
        defaultRolePermissionShouldNotBeFound(shouldNotBeFound);
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultRolePermissionShouldBeFound(String filter) throws Exception {
        restRolePermissionMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(rolePermission.getId().intValue())))
            .andExpect(jsonPath("$.[*].canRead").value(hasItem(DEFAULT_CAN_READ)))
            .andExpect(jsonPath("$.[*].canCreate").value(hasItem(DEFAULT_CAN_CREATE)))
            .andExpect(jsonPath("$.[*].canUpdate").value(hasItem(DEFAULT_CAN_UPDATE)))
            .andExpect(jsonPath("$.[*].canDelete").value(hasItem(DEFAULT_CAN_DELETE)));

        // Check, that the count call also returns 1
        restRolePermissionMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultRolePermissionShouldNotBeFound(String filter) throws Exception {
        restRolePermissionMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restRolePermissionMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingRolePermission() throws Exception {
        // Get the rolePermission
        restRolePermissionMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingRolePermission() throws Exception {
        // Initialize the database
        insertedRolePermission = rolePermissionRepository.saveAndFlush(rolePermission);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the rolePermission
        RolePermission updatedRolePermission = rolePermissionRepository.findById(rolePermission.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedRolePermission are not directly saved in db
        em.detach(updatedRolePermission);
        updatedRolePermission
            .canRead(UPDATED_CAN_READ)
            .canCreate(UPDATED_CAN_CREATE)
            .canUpdate(UPDATED_CAN_UPDATE)
            .canDelete(UPDATED_CAN_DELETE);
        RolePermissionDTO rolePermissionDTO = rolePermissionMapper.toDto(updatedRolePermission);

        restRolePermissionMockMvc
            .perform(
                put(ENTITY_API_URL_ID, rolePermissionDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(rolePermissionDTO))
            )
            .andExpect(status().isOk());

        // Validate the RolePermission in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedRolePermissionToMatchAllProperties(updatedRolePermission);
    }

    @Test
    @Transactional
    void putNonExistingRolePermission() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        rolePermission.setId(longCount.incrementAndGet());

        // Create the RolePermission
        RolePermissionDTO rolePermissionDTO = rolePermissionMapper.toDto(rolePermission);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restRolePermissionMockMvc
            .perform(
                put(ENTITY_API_URL_ID, rolePermissionDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(rolePermissionDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the RolePermission in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchRolePermission() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        rolePermission.setId(longCount.incrementAndGet());

        // Create the RolePermission
        RolePermissionDTO rolePermissionDTO = rolePermissionMapper.toDto(rolePermission);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restRolePermissionMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(rolePermissionDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the RolePermission in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamRolePermission() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        rolePermission.setId(longCount.incrementAndGet());

        // Create the RolePermission
        RolePermissionDTO rolePermissionDTO = rolePermissionMapper.toDto(rolePermission);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restRolePermissionMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(rolePermissionDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the RolePermission in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateRolePermissionWithPatch() throws Exception {
        // Initialize the database
        insertedRolePermission = rolePermissionRepository.saveAndFlush(rolePermission);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the rolePermission using partial update
        RolePermission partialUpdatedRolePermission = new RolePermission();
        partialUpdatedRolePermission.setId(rolePermission.getId());

        partialUpdatedRolePermission.canRead(UPDATED_CAN_READ).canDelete(UPDATED_CAN_DELETE);

        restRolePermissionMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedRolePermission.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedRolePermission))
            )
            .andExpect(status().isOk());

        // Validate the RolePermission in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertRolePermissionUpdatableFieldsEquals(
            createUpdateProxyForBean(partialUpdatedRolePermission, rolePermission),
            getPersistedRolePermission(rolePermission)
        );
    }

    @Test
    @Transactional
    void fullUpdateRolePermissionWithPatch() throws Exception {
        // Initialize the database
        insertedRolePermission = rolePermissionRepository.saveAndFlush(rolePermission);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the rolePermission using partial update
        RolePermission partialUpdatedRolePermission = new RolePermission();
        partialUpdatedRolePermission.setId(rolePermission.getId());

        partialUpdatedRolePermission
            .canRead(UPDATED_CAN_READ)
            .canCreate(UPDATED_CAN_CREATE)
            .canUpdate(UPDATED_CAN_UPDATE)
            .canDelete(UPDATED_CAN_DELETE);

        restRolePermissionMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedRolePermission.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedRolePermission))
            )
            .andExpect(status().isOk());

        // Validate the RolePermission in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertRolePermissionUpdatableFieldsEquals(partialUpdatedRolePermission, getPersistedRolePermission(partialUpdatedRolePermission));
    }

    @Test
    @Transactional
    void patchNonExistingRolePermission() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        rolePermission.setId(longCount.incrementAndGet());

        // Create the RolePermission
        RolePermissionDTO rolePermissionDTO = rolePermissionMapper.toDto(rolePermission);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restRolePermissionMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, rolePermissionDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(rolePermissionDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the RolePermission in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchRolePermission() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        rolePermission.setId(longCount.incrementAndGet());

        // Create the RolePermission
        RolePermissionDTO rolePermissionDTO = rolePermissionMapper.toDto(rolePermission);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restRolePermissionMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(rolePermissionDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the RolePermission in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamRolePermission() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        rolePermission.setId(longCount.incrementAndGet());

        // Create the RolePermission
        RolePermissionDTO rolePermissionDTO = rolePermissionMapper.toDto(rolePermission);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restRolePermissionMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(om.writeValueAsBytes(rolePermissionDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the RolePermission in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteRolePermission() throws Exception {
        // Initialize the database
        insertedRolePermission = rolePermissionRepository.saveAndFlush(rolePermission);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the rolePermission
        restRolePermissionMockMvc
            .perform(delete(ENTITY_API_URL_ID, rolePermission.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return rolePermissionRepository.count();
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

    protected RolePermission getPersistedRolePermission(RolePermission rolePermission) {
        return rolePermissionRepository.findById(rolePermission.getId()).orElseThrow();
    }

    protected void assertPersistedRolePermissionToMatchAllProperties(RolePermission expectedRolePermission) {
        assertRolePermissionAllPropertiesEquals(expectedRolePermission, getPersistedRolePermission(expectedRolePermission));
    }

    protected void assertPersistedRolePermissionToMatchUpdatableProperties(RolePermission expectedRolePermission) {
        assertRolePermissionAllUpdatablePropertiesEquals(expectedRolePermission, getPersistedRolePermission(expectedRolePermission));
    }
}
