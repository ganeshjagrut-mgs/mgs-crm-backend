package com.mgs.web.rest;

import static com.mgs.domain.LeadSourceAsserts.*;
import static com.mgs.web.rest.TestUtil.createUpdateProxyForBean;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mgs.IntegrationTest;
import com.mgs.domain.LeadSource;
import com.mgs.domain.Tenant;
import com.mgs.repository.LeadSourceRepository;
import com.mgs.service.dto.LeadSourceDTO;
import com.mgs.service.mapper.LeadSourceMapper;
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
 * Integration tests for the {@link LeadSourceResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class LeadSourceResourceIT {

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_NAME_SEARCH = "AAAAAAAAAA";
    private static final String UPDATED_NAME_SEARCH = "BBBBBBBBBB";

    private static final Boolean DEFAULT_IS_ACTIVE = false;
    private static final Boolean UPDATED_IS_ACTIVE = true;

    private static final String ENTITY_API_URL = "/api/lead-sources";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ObjectMapper om;

    @Autowired
    private LeadSourceRepository leadSourceRepository;

    @Autowired
    private LeadSourceMapper leadSourceMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restLeadSourceMockMvc;

    private LeadSource leadSource;

    private LeadSource insertedLeadSource;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static LeadSource createEntity(EntityManager em) {
        LeadSource leadSource = new LeadSource().name(DEFAULT_NAME).nameSearch(DEFAULT_NAME_SEARCH).isActive(DEFAULT_IS_ACTIVE);
        // Add required entity
        Tenant tenant;
        if (TestUtil.findAll(em, Tenant.class).isEmpty()) {
            tenant = TenantResourceIT.createEntity();
            em.persist(tenant);
            em.flush();
        } else {
            tenant = TestUtil.findAll(em, Tenant.class).get(0);
        }
        leadSource.setTenant(tenant);
        return leadSource;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static LeadSource createUpdatedEntity(EntityManager em) {
        LeadSource updatedLeadSource = new LeadSource().name(UPDATED_NAME).nameSearch(UPDATED_NAME_SEARCH).isActive(UPDATED_IS_ACTIVE);
        // Add required entity
        Tenant tenant;
        if (TestUtil.findAll(em, Tenant.class).isEmpty()) {
            tenant = TenantResourceIT.createUpdatedEntity();
            em.persist(tenant);
            em.flush();
        } else {
            tenant = TestUtil.findAll(em, Tenant.class).get(0);
        }
        updatedLeadSource.setTenant(tenant);
        return updatedLeadSource;
    }

    @BeforeEach
    void initTest() {
        leadSource = createEntity(em);
    }

    @AfterEach
    void cleanup() {
        if (insertedLeadSource != null) {
            leadSourceRepository.delete(insertedLeadSource);
            insertedLeadSource = null;
        }
    }

    @Test
    @Transactional
    void createLeadSource() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the LeadSource
        LeadSourceDTO leadSourceDTO = leadSourceMapper.toDto(leadSource);
        var returnedLeadSourceDTO = om.readValue(
            restLeadSourceMockMvc
                .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(leadSourceDTO)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            LeadSourceDTO.class
        );

        // Validate the LeadSource in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        var returnedLeadSource = leadSourceMapper.toEntity(returnedLeadSourceDTO);
        assertLeadSourceUpdatableFieldsEquals(returnedLeadSource, getPersistedLeadSource(returnedLeadSource));

        insertedLeadSource = returnedLeadSource;
    }

    @Test
    @Transactional
    void createLeadSourceWithExistingId() throws Exception {
        // Create the LeadSource with an existing ID
        leadSource.setId(1L);
        LeadSourceDTO leadSourceDTO = leadSourceMapper.toDto(leadSource);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restLeadSourceMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(leadSourceDTO)))
            .andExpect(status().isBadRequest());

        // Validate the LeadSource in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkNameIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        leadSource.setName(null);

        // Create the LeadSource, which fails.
        LeadSourceDTO leadSourceDTO = leadSourceMapper.toDto(leadSource);

        restLeadSourceMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(leadSourceDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllLeadSources() throws Exception {
        // Initialize the database
        insertedLeadSource = leadSourceRepository.saveAndFlush(leadSource);

        // Get all the leadSourceList
        restLeadSourceMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(leadSource.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)))
            .andExpect(jsonPath("$.[*].nameSearch").value(hasItem(DEFAULT_NAME_SEARCH)))
            .andExpect(jsonPath("$.[*].isActive").value(hasItem(DEFAULT_IS_ACTIVE)));
    }

    @Test
    @Transactional
    void getLeadSource() throws Exception {
        // Initialize the database
        insertedLeadSource = leadSourceRepository.saveAndFlush(leadSource);

        // Get the leadSource
        restLeadSourceMockMvc
            .perform(get(ENTITY_API_URL_ID, leadSource.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(leadSource.getId().intValue()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME))
            .andExpect(jsonPath("$.nameSearch").value(DEFAULT_NAME_SEARCH))
            .andExpect(jsonPath("$.isActive").value(DEFAULT_IS_ACTIVE));
    }

    @Test
    @Transactional
    void getLeadSourcesByIdFiltering() throws Exception {
        // Initialize the database
        insertedLeadSource = leadSourceRepository.saveAndFlush(leadSource);

        Long id = leadSource.getId();

        defaultLeadSourceFiltering("id.equals=" + id, "id.notEquals=" + id);

        defaultLeadSourceFiltering("id.greaterThanOrEqual=" + id, "id.greaterThan=" + id);

        defaultLeadSourceFiltering("id.lessThanOrEqual=" + id, "id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllLeadSourcesByNameIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedLeadSource = leadSourceRepository.saveAndFlush(leadSource);

        // Get all the leadSourceList where name equals to
        defaultLeadSourceFiltering("name.equals=" + DEFAULT_NAME, "name.equals=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllLeadSourcesByNameIsInShouldWork() throws Exception {
        // Initialize the database
        insertedLeadSource = leadSourceRepository.saveAndFlush(leadSource);

        // Get all the leadSourceList where name in
        defaultLeadSourceFiltering("name.in=" + DEFAULT_NAME + "," + UPDATED_NAME, "name.in=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllLeadSourcesByNameIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedLeadSource = leadSourceRepository.saveAndFlush(leadSource);

        // Get all the leadSourceList where name is not null
        defaultLeadSourceFiltering("name.specified=true", "name.specified=false");
    }

    @Test
    @Transactional
    void getAllLeadSourcesByNameContainsSomething() throws Exception {
        // Initialize the database
        insertedLeadSource = leadSourceRepository.saveAndFlush(leadSource);

        // Get all the leadSourceList where name contains
        defaultLeadSourceFiltering("name.contains=" + DEFAULT_NAME, "name.contains=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllLeadSourcesByNameNotContainsSomething() throws Exception {
        // Initialize the database
        insertedLeadSource = leadSourceRepository.saveAndFlush(leadSource);

        // Get all the leadSourceList where name does not contain
        defaultLeadSourceFiltering("name.doesNotContain=" + UPDATED_NAME, "name.doesNotContain=" + DEFAULT_NAME);
    }

    @Test
    @Transactional
    void getAllLeadSourcesByNameSearchIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedLeadSource = leadSourceRepository.saveAndFlush(leadSource);

        // Get all the leadSourceList where nameSearch equals to
        defaultLeadSourceFiltering("nameSearch.equals=" + DEFAULT_NAME_SEARCH, "nameSearch.equals=" + UPDATED_NAME_SEARCH);
    }

    @Test
    @Transactional
    void getAllLeadSourcesByNameSearchIsInShouldWork() throws Exception {
        // Initialize the database
        insertedLeadSource = leadSourceRepository.saveAndFlush(leadSource);

        // Get all the leadSourceList where nameSearch in
        defaultLeadSourceFiltering(
            "nameSearch.in=" + DEFAULT_NAME_SEARCH + "," + UPDATED_NAME_SEARCH,
            "nameSearch.in=" + UPDATED_NAME_SEARCH
        );
    }

    @Test
    @Transactional
    void getAllLeadSourcesByNameSearchIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedLeadSource = leadSourceRepository.saveAndFlush(leadSource);

        // Get all the leadSourceList where nameSearch is not null
        defaultLeadSourceFiltering("nameSearch.specified=true", "nameSearch.specified=false");
    }

    @Test
    @Transactional
    void getAllLeadSourcesByNameSearchContainsSomething() throws Exception {
        // Initialize the database
        insertedLeadSource = leadSourceRepository.saveAndFlush(leadSource);

        // Get all the leadSourceList where nameSearch contains
        defaultLeadSourceFiltering("nameSearch.contains=" + DEFAULT_NAME_SEARCH, "nameSearch.contains=" + UPDATED_NAME_SEARCH);
    }

    @Test
    @Transactional
    void getAllLeadSourcesByNameSearchNotContainsSomething() throws Exception {
        // Initialize the database
        insertedLeadSource = leadSourceRepository.saveAndFlush(leadSource);

        // Get all the leadSourceList where nameSearch does not contain
        defaultLeadSourceFiltering("nameSearch.doesNotContain=" + UPDATED_NAME_SEARCH, "nameSearch.doesNotContain=" + DEFAULT_NAME_SEARCH);
    }

    @Test
    @Transactional
    void getAllLeadSourcesByIsActiveIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedLeadSource = leadSourceRepository.saveAndFlush(leadSource);

        // Get all the leadSourceList where isActive equals to
        defaultLeadSourceFiltering("isActive.equals=" + DEFAULT_IS_ACTIVE, "isActive.equals=" + UPDATED_IS_ACTIVE);
    }

    @Test
    @Transactional
    void getAllLeadSourcesByIsActiveIsInShouldWork() throws Exception {
        // Initialize the database
        insertedLeadSource = leadSourceRepository.saveAndFlush(leadSource);

        // Get all the leadSourceList where isActive in
        defaultLeadSourceFiltering("isActive.in=" + DEFAULT_IS_ACTIVE + "," + UPDATED_IS_ACTIVE, "isActive.in=" + UPDATED_IS_ACTIVE);
    }

    @Test
    @Transactional
    void getAllLeadSourcesByIsActiveIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedLeadSource = leadSourceRepository.saveAndFlush(leadSource);

        // Get all the leadSourceList where isActive is not null
        defaultLeadSourceFiltering("isActive.specified=true", "isActive.specified=false");
    }

    @Test
    @Transactional
    void getAllLeadSourcesByTenantIsEqualToSomething() throws Exception {
        Tenant tenant;
        if (TestUtil.findAll(em, Tenant.class).isEmpty()) {
            leadSourceRepository.saveAndFlush(leadSource);
            tenant = TenantResourceIT.createEntity();
        } else {
            tenant = TestUtil.findAll(em, Tenant.class).get(0);
        }
        em.persist(tenant);
        em.flush();
        leadSource.setTenant(tenant);
        leadSourceRepository.saveAndFlush(leadSource);
        Long tenantId = tenant.getId();
        // Get all the leadSourceList where tenant equals to tenantId
        defaultLeadSourceShouldBeFound("tenantId.equals=" + tenantId);

        // Get all the leadSourceList where tenant equals to (tenantId + 1)
        defaultLeadSourceShouldNotBeFound("tenantId.equals=" + (tenantId + 1));
    }

    private void defaultLeadSourceFiltering(String shouldBeFound, String shouldNotBeFound) throws Exception {
        defaultLeadSourceShouldBeFound(shouldBeFound);
        defaultLeadSourceShouldNotBeFound(shouldNotBeFound);
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultLeadSourceShouldBeFound(String filter) throws Exception {
        restLeadSourceMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(leadSource.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)))
            .andExpect(jsonPath("$.[*].nameSearch").value(hasItem(DEFAULT_NAME_SEARCH)))
            .andExpect(jsonPath("$.[*].isActive").value(hasItem(DEFAULT_IS_ACTIVE)));

        // Check, that the count call also returns 1
        restLeadSourceMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultLeadSourceShouldNotBeFound(String filter) throws Exception {
        restLeadSourceMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restLeadSourceMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingLeadSource() throws Exception {
        // Get the leadSource
        restLeadSourceMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingLeadSource() throws Exception {
        // Initialize the database
        insertedLeadSource = leadSourceRepository.saveAndFlush(leadSource);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the leadSource
        LeadSource updatedLeadSource = leadSourceRepository.findById(leadSource.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedLeadSource are not directly saved in db
        em.detach(updatedLeadSource);
        updatedLeadSource.name(UPDATED_NAME).nameSearch(UPDATED_NAME_SEARCH).isActive(UPDATED_IS_ACTIVE);
        LeadSourceDTO leadSourceDTO = leadSourceMapper.toDto(updatedLeadSource);

        restLeadSourceMockMvc
            .perform(
                put(ENTITY_API_URL_ID, leadSourceDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(leadSourceDTO))
            )
            .andExpect(status().isOk());

        // Validate the LeadSource in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedLeadSourceToMatchAllProperties(updatedLeadSource);
    }

    @Test
    @Transactional
    void putNonExistingLeadSource() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        leadSource.setId(longCount.incrementAndGet());

        // Create the LeadSource
        LeadSourceDTO leadSourceDTO = leadSourceMapper.toDto(leadSource);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restLeadSourceMockMvc
            .perform(
                put(ENTITY_API_URL_ID, leadSourceDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(leadSourceDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the LeadSource in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchLeadSource() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        leadSource.setId(longCount.incrementAndGet());

        // Create the LeadSource
        LeadSourceDTO leadSourceDTO = leadSourceMapper.toDto(leadSource);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restLeadSourceMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(leadSourceDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the LeadSource in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamLeadSource() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        leadSource.setId(longCount.incrementAndGet());

        // Create the LeadSource
        LeadSourceDTO leadSourceDTO = leadSourceMapper.toDto(leadSource);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restLeadSourceMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(leadSourceDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the LeadSource in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateLeadSourceWithPatch() throws Exception {
        // Initialize the database
        insertedLeadSource = leadSourceRepository.saveAndFlush(leadSource);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the leadSource using partial update
        LeadSource partialUpdatedLeadSource = new LeadSource();
        partialUpdatedLeadSource.setId(leadSource.getId());

        restLeadSourceMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedLeadSource.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedLeadSource))
            )
            .andExpect(status().isOk());

        // Validate the LeadSource in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertLeadSourceUpdatableFieldsEquals(
            createUpdateProxyForBean(partialUpdatedLeadSource, leadSource),
            getPersistedLeadSource(leadSource)
        );
    }

    @Test
    @Transactional
    void fullUpdateLeadSourceWithPatch() throws Exception {
        // Initialize the database
        insertedLeadSource = leadSourceRepository.saveAndFlush(leadSource);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the leadSource using partial update
        LeadSource partialUpdatedLeadSource = new LeadSource();
        partialUpdatedLeadSource.setId(leadSource.getId());

        partialUpdatedLeadSource.name(UPDATED_NAME).nameSearch(UPDATED_NAME_SEARCH).isActive(UPDATED_IS_ACTIVE);

        restLeadSourceMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedLeadSource.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedLeadSource))
            )
            .andExpect(status().isOk());

        // Validate the LeadSource in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertLeadSourceUpdatableFieldsEquals(partialUpdatedLeadSource, getPersistedLeadSource(partialUpdatedLeadSource));
    }

    @Test
    @Transactional
    void patchNonExistingLeadSource() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        leadSource.setId(longCount.incrementAndGet());

        // Create the LeadSource
        LeadSourceDTO leadSourceDTO = leadSourceMapper.toDto(leadSource);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restLeadSourceMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, leadSourceDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(leadSourceDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the LeadSource in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchLeadSource() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        leadSource.setId(longCount.incrementAndGet());

        // Create the LeadSource
        LeadSourceDTO leadSourceDTO = leadSourceMapper.toDto(leadSource);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restLeadSourceMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(leadSourceDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the LeadSource in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamLeadSource() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        leadSource.setId(longCount.incrementAndGet());

        // Create the LeadSource
        LeadSourceDTO leadSourceDTO = leadSourceMapper.toDto(leadSource);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restLeadSourceMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(om.writeValueAsBytes(leadSourceDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the LeadSource in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteLeadSource() throws Exception {
        // Initialize the database
        insertedLeadSource = leadSourceRepository.saveAndFlush(leadSource);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the leadSource
        restLeadSourceMockMvc
            .perform(delete(ENTITY_API_URL_ID, leadSource.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return leadSourceRepository.count();
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

    protected LeadSource getPersistedLeadSource(LeadSource leadSource) {
        return leadSourceRepository.findById(leadSource.getId()).orElseThrow();
    }

    protected void assertPersistedLeadSourceToMatchAllProperties(LeadSource expectedLeadSource) {
        assertLeadSourceAllPropertiesEquals(expectedLeadSource, getPersistedLeadSource(expectedLeadSource));
    }

    protected void assertPersistedLeadSourceToMatchUpdatableProperties(LeadSource expectedLeadSource) {
        assertLeadSourceAllUpdatablePropertiesEquals(expectedLeadSource, getPersistedLeadSource(expectedLeadSource));
    }
}
