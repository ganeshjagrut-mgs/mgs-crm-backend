package com.mgs.web.rest;

import static com.mgs.domain.PermissionModuleAsserts.*;
import static com.mgs.web.rest.TestUtil.createUpdateProxyForBean;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mgs.IntegrationTest;
import com.mgs.domain.PermissionModule;
import com.mgs.repository.PermissionModuleRepository;
import com.mgs.service.dto.PermissionModuleDTO;
import com.mgs.service.mapper.PermissionModuleMapper;
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
 * Integration tests for the {@link PermissionModuleResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class PermissionModuleResourceIT {

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_DESCRIPTION = "AAAAAAAAAA";
    private static final String UPDATED_DESCRIPTION = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/permission-modules";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ObjectMapper om;

    @Autowired
    private PermissionModuleRepository permissionModuleRepository;

    @Autowired
    private PermissionModuleMapper permissionModuleMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restPermissionModuleMockMvc;

    private PermissionModule permissionModule;

    private PermissionModule insertedPermissionModule;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static PermissionModule createEntity() {
        return new PermissionModule().name(DEFAULT_NAME).description(DEFAULT_DESCRIPTION);
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static PermissionModule createUpdatedEntity() {
        return new PermissionModule().name(UPDATED_NAME).description(UPDATED_DESCRIPTION);
    }

    @BeforeEach
    void initTest() {
        permissionModule = createEntity();
    }

    @AfterEach
    void cleanup() {
        if (insertedPermissionModule != null) {
            permissionModuleRepository.delete(insertedPermissionModule);
            insertedPermissionModule = null;
        }
    }

    @Test
    @Transactional
    void createPermissionModule() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the PermissionModule
        PermissionModuleDTO permissionModuleDTO = permissionModuleMapper.toDto(permissionModule);
        var returnedPermissionModuleDTO = om.readValue(
            restPermissionModuleMockMvc
                .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(permissionModuleDTO)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            PermissionModuleDTO.class
        );

        // Validate the PermissionModule in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        var returnedPermissionModule = permissionModuleMapper.toEntity(returnedPermissionModuleDTO);
        assertPermissionModuleUpdatableFieldsEquals(returnedPermissionModule, getPersistedPermissionModule(returnedPermissionModule));

        insertedPermissionModule = returnedPermissionModule;
    }

    @Test
    @Transactional
    void createPermissionModuleWithExistingId() throws Exception {
        // Create the PermissionModule with an existing ID
        permissionModule.setId(1L);
        PermissionModuleDTO permissionModuleDTO = permissionModuleMapper.toDto(permissionModule);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restPermissionModuleMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(permissionModuleDTO)))
            .andExpect(status().isBadRequest());

        // Validate the PermissionModule in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkNameIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        permissionModule.setName(null);

        // Create the PermissionModule, which fails.
        PermissionModuleDTO permissionModuleDTO = permissionModuleMapper.toDto(permissionModule);

        restPermissionModuleMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(permissionModuleDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllPermissionModules() throws Exception {
        // Initialize the database
        insertedPermissionModule = permissionModuleRepository.saveAndFlush(permissionModule);

        // Get all the permissionModuleList
        restPermissionModuleMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(permissionModule.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION)));
    }

    @Test
    @Transactional
    void getPermissionModule() throws Exception {
        // Initialize the database
        insertedPermissionModule = permissionModuleRepository.saveAndFlush(permissionModule);

        // Get the permissionModule
        restPermissionModuleMockMvc
            .perform(get(ENTITY_API_URL_ID, permissionModule.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(permissionModule.getId().intValue()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME))
            .andExpect(jsonPath("$.description").value(DEFAULT_DESCRIPTION));
    }

    @Test
    @Transactional
    void getPermissionModulesByIdFiltering() throws Exception {
        // Initialize the database
        insertedPermissionModule = permissionModuleRepository.saveAndFlush(permissionModule);

        Long id = permissionModule.getId();

        defaultPermissionModuleFiltering("id.equals=" + id, "id.notEquals=" + id);

        defaultPermissionModuleFiltering("id.greaterThanOrEqual=" + id, "id.greaterThan=" + id);

        defaultPermissionModuleFiltering("id.lessThanOrEqual=" + id, "id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllPermissionModulesByNameIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedPermissionModule = permissionModuleRepository.saveAndFlush(permissionModule);

        // Get all the permissionModuleList where name equals to
        defaultPermissionModuleFiltering("name.equals=" + DEFAULT_NAME, "name.equals=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllPermissionModulesByNameIsInShouldWork() throws Exception {
        // Initialize the database
        insertedPermissionModule = permissionModuleRepository.saveAndFlush(permissionModule);

        // Get all the permissionModuleList where name in
        defaultPermissionModuleFiltering("name.in=" + DEFAULT_NAME + "," + UPDATED_NAME, "name.in=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllPermissionModulesByNameIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedPermissionModule = permissionModuleRepository.saveAndFlush(permissionModule);

        // Get all the permissionModuleList where name is not null
        defaultPermissionModuleFiltering("name.specified=true", "name.specified=false");
    }

    @Test
    @Transactional
    void getAllPermissionModulesByNameContainsSomething() throws Exception {
        // Initialize the database
        insertedPermissionModule = permissionModuleRepository.saveAndFlush(permissionModule);

        // Get all the permissionModuleList where name contains
        defaultPermissionModuleFiltering("name.contains=" + DEFAULT_NAME, "name.contains=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllPermissionModulesByNameNotContainsSomething() throws Exception {
        // Initialize the database
        insertedPermissionModule = permissionModuleRepository.saveAndFlush(permissionModule);

        // Get all the permissionModuleList where name does not contain
        defaultPermissionModuleFiltering("name.doesNotContain=" + UPDATED_NAME, "name.doesNotContain=" + DEFAULT_NAME);
    }

    @Test
    @Transactional
    void getAllPermissionModulesByDescriptionIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedPermissionModule = permissionModuleRepository.saveAndFlush(permissionModule);

        // Get all the permissionModuleList where description equals to
        defaultPermissionModuleFiltering("description.equals=" + DEFAULT_DESCRIPTION, "description.equals=" + UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    void getAllPermissionModulesByDescriptionIsInShouldWork() throws Exception {
        // Initialize the database
        insertedPermissionModule = permissionModuleRepository.saveAndFlush(permissionModule);

        // Get all the permissionModuleList where description in
        defaultPermissionModuleFiltering(
            "description.in=" + DEFAULT_DESCRIPTION + "," + UPDATED_DESCRIPTION,
            "description.in=" + UPDATED_DESCRIPTION
        );
    }

    @Test
    @Transactional
    void getAllPermissionModulesByDescriptionIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedPermissionModule = permissionModuleRepository.saveAndFlush(permissionModule);

        // Get all the permissionModuleList where description is not null
        defaultPermissionModuleFiltering("description.specified=true", "description.specified=false");
    }

    @Test
    @Transactional
    void getAllPermissionModulesByDescriptionContainsSomething() throws Exception {
        // Initialize the database
        insertedPermissionModule = permissionModuleRepository.saveAndFlush(permissionModule);

        // Get all the permissionModuleList where description contains
        defaultPermissionModuleFiltering("description.contains=" + DEFAULT_DESCRIPTION, "description.contains=" + UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    void getAllPermissionModulesByDescriptionNotContainsSomething() throws Exception {
        // Initialize the database
        insertedPermissionModule = permissionModuleRepository.saveAndFlush(permissionModule);

        // Get all the permissionModuleList where description does not contain
        defaultPermissionModuleFiltering(
            "description.doesNotContain=" + UPDATED_DESCRIPTION,
            "description.doesNotContain=" + DEFAULT_DESCRIPTION
        );
    }

    private void defaultPermissionModuleFiltering(String shouldBeFound, String shouldNotBeFound) throws Exception {
        defaultPermissionModuleShouldBeFound(shouldBeFound);
        defaultPermissionModuleShouldNotBeFound(shouldNotBeFound);
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultPermissionModuleShouldBeFound(String filter) throws Exception {
        restPermissionModuleMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(permissionModule.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION)));

        // Check, that the count call also returns 1
        restPermissionModuleMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultPermissionModuleShouldNotBeFound(String filter) throws Exception {
        restPermissionModuleMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restPermissionModuleMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingPermissionModule() throws Exception {
        // Get the permissionModule
        restPermissionModuleMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingPermissionModule() throws Exception {
        // Initialize the database
        insertedPermissionModule = permissionModuleRepository.saveAndFlush(permissionModule);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the permissionModule
        PermissionModule updatedPermissionModule = permissionModuleRepository.findById(permissionModule.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedPermissionModule are not directly saved in db
        em.detach(updatedPermissionModule);
        updatedPermissionModule.name(UPDATED_NAME).description(UPDATED_DESCRIPTION);
        PermissionModuleDTO permissionModuleDTO = permissionModuleMapper.toDto(updatedPermissionModule);

        restPermissionModuleMockMvc
            .perform(
                put(ENTITY_API_URL_ID, permissionModuleDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(permissionModuleDTO))
            )
            .andExpect(status().isOk());

        // Validate the PermissionModule in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedPermissionModuleToMatchAllProperties(updatedPermissionModule);
    }

    @Test
    @Transactional
    void putNonExistingPermissionModule() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        permissionModule.setId(longCount.incrementAndGet());

        // Create the PermissionModule
        PermissionModuleDTO permissionModuleDTO = permissionModuleMapper.toDto(permissionModule);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restPermissionModuleMockMvc
            .perform(
                put(ENTITY_API_URL_ID, permissionModuleDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(permissionModuleDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the PermissionModule in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchPermissionModule() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        permissionModule.setId(longCount.incrementAndGet());

        // Create the PermissionModule
        PermissionModuleDTO permissionModuleDTO = permissionModuleMapper.toDto(permissionModule);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restPermissionModuleMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(permissionModuleDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the PermissionModule in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamPermissionModule() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        permissionModule.setId(longCount.incrementAndGet());

        // Create the PermissionModule
        PermissionModuleDTO permissionModuleDTO = permissionModuleMapper.toDto(permissionModule);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restPermissionModuleMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(permissionModuleDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the PermissionModule in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdatePermissionModuleWithPatch() throws Exception {
        // Initialize the database
        insertedPermissionModule = permissionModuleRepository.saveAndFlush(permissionModule);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the permissionModule using partial update
        PermissionModule partialUpdatedPermissionModule = new PermissionModule();
        partialUpdatedPermissionModule.setId(permissionModule.getId());

        restPermissionModuleMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedPermissionModule.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedPermissionModule))
            )
            .andExpect(status().isOk());

        // Validate the PermissionModule in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPermissionModuleUpdatableFieldsEquals(
            createUpdateProxyForBean(partialUpdatedPermissionModule, permissionModule),
            getPersistedPermissionModule(permissionModule)
        );
    }

    @Test
    @Transactional
    void fullUpdatePermissionModuleWithPatch() throws Exception {
        // Initialize the database
        insertedPermissionModule = permissionModuleRepository.saveAndFlush(permissionModule);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the permissionModule using partial update
        PermissionModule partialUpdatedPermissionModule = new PermissionModule();
        partialUpdatedPermissionModule.setId(permissionModule.getId());

        partialUpdatedPermissionModule.name(UPDATED_NAME).description(UPDATED_DESCRIPTION);

        restPermissionModuleMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedPermissionModule.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedPermissionModule))
            )
            .andExpect(status().isOk());

        // Validate the PermissionModule in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPermissionModuleUpdatableFieldsEquals(
            partialUpdatedPermissionModule,
            getPersistedPermissionModule(partialUpdatedPermissionModule)
        );
    }

    @Test
    @Transactional
    void patchNonExistingPermissionModule() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        permissionModule.setId(longCount.incrementAndGet());

        // Create the PermissionModule
        PermissionModuleDTO permissionModuleDTO = permissionModuleMapper.toDto(permissionModule);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restPermissionModuleMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, permissionModuleDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(permissionModuleDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the PermissionModule in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchPermissionModule() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        permissionModule.setId(longCount.incrementAndGet());

        // Create the PermissionModule
        PermissionModuleDTO permissionModuleDTO = permissionModuleMapper.toDto(permissionModule);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restPermissionModuleMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(permissionModuleDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the PermissionModule in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamPermissionModule() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        permissionModule.setId(longCount.incrementAndGet());

        // Create the PermissionModule
        PermissionModuleDTO permissionModuleDTO = permissionModuleMapper.toDto(permissionModule);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restPermissionModuleMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(om.writeValueAsBytes(permissionModuleDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the PermissionModule in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deletePermissionModule() throws Exception {
        // Initialize the database
        insertedPermissionModule = permissionModuleRepository.saveAndFlush(permissionModule);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the permissionModule
        restPermissionModuleMockMvc
            .perform(delete(ENTITY_API_URL_ID, permissionModule.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return permissionModuleRepository.count();
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

    protected PermissionModule getPersistedPermissionModule(PermissionModule permissionModule) {
        return permissionModuleRepository.findById(permissionModule.getId()).orElseThrow();
    }

    protected void assertPersistedPermissionModuleToMatchAllProperties(PermissionModule expectedPermissionModule) {
        assertPermissionModuleAllPropertiesEquals(expectedPermissionModule, getPersistedPermissionModule(expectedPermissionModule));
    }

    protected void assertPersistedPermissionModuleToMatchUpdatableProperties(PermissionModule expectedPermissionModule) {
        assertPermissionModuleAllUpdatablePropertiesEquals(
            expectedPermissionModule,
            getPersistedPermissionModule(expectedPermissionModule)
        );
    }
}
