package com.mgs.web.rest;

import static com.mgs.domain.ComplaintCategoryAsserts.*;
import static com.mgs.web.rest.TestUtil.createUpdateProxyForBean;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mgs.IntegrationTest;
import com.mgs.domain.ComplaintCategory;
import com.mgs.domain.Tenant;
import com.mgs.repository.ComplaintCategoryRepository;
import com.mgs.service.dto.ComplaintCategoryDTO;
import com.mgs.service.mapper.ComplaintCategoryMapper;
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
 * Integration tests for the {@link ComplaintCategoryResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class ComplaintCategoryResourceIT {

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    private static final Boolean DEFAULT_IS_ACTIVE = false;
    private static final Boolean UPDATED_IS_ACTIVE = true;

    private static final String ENTITY_API_URL = "/api/complaint-categories";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ObjectMapper om;

    @Autowired
    private ComplaintCategoryRepository complaintCategoryRepository;

    @Autowired
    private ComplaintCategoryMapper complaintCategoryMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restComplaintCategoryMockMvc;

    private ComplaintCategory complaintCategory;

    private ComplaintCategory insertedComplaintCategory;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static ComplaintCategory createEntity(EntityManager em) {
        ComplaintCategory complaintCategory = new ComplaintCategory().name(DEFAULT_NAME).isActive(DEFAULT_IS_ACTIVE);
        // Add required entity
        Tenant tenant;
        if (TestUtil.findAll(em, Tenant.class).isEmpty()) {
            tenant = TenantResourceIT.createEntity();
            em.persist(tenant);
            em.flush();
        } else {
            tenant = TestUtil.findAll(em, Tenant.class).get(0);
        }
        complaintCategory.setTenant(tenant);
        return complaintCategory;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static ComplaintCategory createUpdatedEntity(EntityManager em) {
        ComplaintCategory updatedComplaintCategory = new ComplaintCategory().name(UPDATED_NAME).isActive(UPDATED_IS_ACTIVE);
        // Add required entity
        Tenant tenant;
        if (TestUtil.findAll(em, Tenant.class).isEmpty()) {
            tenant = TenantResourceIT.createUpdatedEntity();
            em.persist(tenant);
            em.flush();
        } else {
            tenant = TestUtil.findAll(em, Tenant.class).get(0);
        }
        updatedComplaintCategory.setTenant(tenant);
        return updatedComplaintCategory;
    }

    @BeforeEach
    void initTest() {
        complaintCategory = createEntity(em);
    }

    @AfterEach
    void cleanup() {
        if (insertedComplaintCategory != null) {
            complaintCategoryRepository.delete(insertedComplaintCategory);
            insertedComplaintCategory = null;
        }
    }

    @Test
    @Transactional
    void createComplaintCategory() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the ComplaintCategory
        ComplaintCategoryDTO complaintCategoryDTO = complaintCategoryMapper.toDto(complaintCategory);
        var returnedComplaintCategoryDTO = om.readValue(
            restComplaintCategoryMockMvc
                .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(complaintCategoryDTO)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            ComplaintCategoryDTO.class
        );

        // Validate the ComplaintCategory in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        var returnedComplaintCategory = complaintCategoryMapper.toEntity(returnedComplaintCategoryDTO);
        assertComplaintCategoryUpdatableFieldsEquals(returnedComplaintCategory, getPersistedComplaintCategory(returnedComplaintCategory));

        insertedComplaintCategory = returnedComplaintCategory;
    }

    @Test
    @Transactional
    void createComplaintCategoryWithExistingId() throws Exception {
        // Create the ComplaintCategory with an existing ID
        complaintCategory.setId(1L);
        ComplaintCategoryDTO complaintCategoryDTO = complaintCategoryMapper.toDto(complaintCategory);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restComplaintCategoryMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(complaintCategoryDTO)))
            .andExpect(status().isBadRequest());

        // Validate the ComplaintCategory in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkNameIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        complaintCategory.setName(null);

        // Create the ComplaintCategory, which fails.
        ComplaintCategoryDTO complaintCategoryDTO = complaintCategoryMapper.toDto(complaintCategory);

        restComplaintCategoryMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(complaintCategoryDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllComplaintCategories() throws Exception {
        // Initialize the database
        insertedComplaintCategory = complaintCategoryRepository.saveAndFlush(complaintCategory);

        // Get all the complaintCategoryList
        restComplaintCategoryMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(complaintCategory.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)))
            .andExpect(jsonPath("$.[*].isActive").value(hasItem(DEFAULT_IS_ACTIVE)));
    }

    @Test
    @Transactional
    void getComplaintCategory() throws Exception {
        // Initialize the database
        insertedComplaintCategory = complaintCategoryRepository.saveAndFlush(complaintCategory);

        // Get the complaintCategory
        restComplaintCategoryMockMvc
            .perform(get(ENTITY_API_URL_ID, complaintCategory.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(complaintCategory.getId().intValue()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME))
            .andExpect(jsonPath("$.isActive").value(DEFAULT_IS_ACTIVE));
    }

    @Test
    @Transactional
    void getComplaintCategoriesByIdFiltering() throws Exception {
        // Initialize the database
        insertedComplaintCategory = complaintCategoryRepository.saveAndFlush(complaintCategory);

        Long id = complaintCategory.getId();

        defaultComplaintCategoryFiltering("id.equals=" + id, "id.notEquals=" + id);

        defaultComplaintCategoryFiltering("id.greaterThanOrEqual=" + id, "id.greaterThan=" + id);

        defaultComplaintCategoryFiltering("id.lessThanOrEqual=" + id, "id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllComplaintCategoriesByNameIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedComplaintCategory = complaintCategoryRepository.saveAndFlush(complaintCategory);

        // Get all the complaintCategoryList where name equals to
        defaultComplaintCategoryFiltering("name.equals=" + DEFAULT_NAME, "name.equals=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllComplaintCategoriesByNameIsInShouldWork() throws Exception {
        // Initialize the database
        insertedComplaintCategory = complaintCategoryRepository.saveAndFlush(complaintCategory);

        // Get all the complaintCategoryList where name in
        defaultComplaintCategoryFiltering("name.in=" + DEFAULT_NAME + "," + UPDATED_NAME, "name.in=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllComplaintCategoriesByNameIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedComplaintCategory = complaintCategoryRepository.saveAndFlush(complaintCategory);

        // Get all the complaintCategoryList where name is not null
        defaultComplaintCategoryFiltering("name.specified=true", "name.specified=false");
    }

    @Test
    @Transactional
    void getAllComplaintCategoriesByNameContainsSomething() throws Exception {
        // Initialize the database
        insertedComplaintCategory = complaintCategoryRepository.saveAndFlush(complaintCategory);

        // Get all the complaintCategoryList where name contains
        defaultComplaintCategoryFiltering("name.contains=" + DEFAULT_NAME, "name.contains=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllComplaintCategoriesByNameNotContainsSomething() throws Exception {
        // Initialize the database
        insertedComplaintCategory = complaintCategoryRepository.saveAndFlush(complaintCategory);

        // Get all the complaintCategoryList where name does not contain
        defaultComplaintCategoryFiltering("name.doesNotContain=" + UPDATED_NAME, "name.doesNotContain=" + DEFAULT_NAME);
    }

    @Test
    @Transactional
    void getAllComplaintCategoriesByIsActiveIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedComplaintCategory = complaintCategoryRepository.saveAndFlush(complaintCategory);

        // Get all the complaintCategoryList where isActive equals to
        defaultComplaintCategoryFiltering("isActive.equals=" + DEFAULT_IS_ACTIVE, "isActive.equals=" + UPDATED_IS_ACTIVE);
    }

    @Test
    @Transactional
    void getAllComplaintCategoriesByIsActiveIsInShouldWork() throws Exception {
        // Initialize the database
        insertedComplaintCategory = complaintCategoryRepository.saveAndFlush(complaintCategory);

        // Get all the complaintCategoryList where isActive in
        defaultComplaintCategoryFiltering("isActive.in=" + DEFAULT_IS_ACTIVE + "," + UPDATED_IS_ACTIVE, "isActive.in=" + UPDATED_IS_ACTIVE);
    }

    @Test
    @Transactional
    void getAllComplaintCategoriesByIsActiveIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedComplaintCategory = complaintCategoryRepository.saveAndFlush(complaintCategory);

        // Get all the complaintCategoryList where isActive is not null
        defaultComplaintCategoryFiltering("isActive.specified=true", "isActive.specified=false");
    }

    @Test
    @Transactional
    void getAllComplaintCategoriesByTenantIsEqualToSomething() throws Exception {
        Tenant tenant;
        if (TestUtil.findAll(em, Tenant.class).isEmpty()) {
            complaintCategoryRepository.saveAndFlush(complaintCategory);
            tenant = TenantResourceIT.createEntity();
        } else {
            tenant = TestUtil.findAll(em, Tenant.class).get(0);
        }
        em.persist(tenant);
        em.flush();
        complaintCategory.setTenant(tenant);
        complaintCategoryRepository.saveAndFlush(complaintCategory);
        Long tenantId = tenant.getId();
        // Get all the complaintCategoryList where tenant equals to tenantId
        defaultComplaintCategoryShouldBeFound("tenantId.equals=" + tenantId);

        // Get all the complaintCategoryList where tenant equals to (tenantId + 1)
        defaultComplaintCategoryShouldNotBeFound("tenantId.equals=" + (tenantId + 1));
    }

    private void defaultComplaintCategoryFiltering(String shouldBeFound, String shouldNotBeFound) throws Exception {
        defaultComplaintCategoryShouldBeFound(shouldBeFound);
        defaultComplaintCategoryShouldNotBeFound(shouldNotBeFound);
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultComplaintCategoryShouldBeFound(String filter) throws Exception {
        restComplaintCategoryMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(complaintCategory.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)))
            .andExpect(jsonPath("$.[*].isActive").value(hasItem(DEFAULT_IS_ACTIVE)));

        // Check, that the count call also returns 1
        restComplaintCategoryMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultComplaintCategoryShouldNotBeFound(String filter) throws Exception {
        restComplaintCategoryMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restComplaintCategoryMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingComplaintCategory() throws Exception {
        // Get the complaintCategory
        restComplaintCategoryMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingComplaintCategory() throws Exception {
        // Initialize the database
        insertedComplaintCategory = complaintCategoryRepository.saveAndFlush(complaintCategory);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the complaintCategory
        ComplaintCategory updatedComplaintCategory = complaintCategoryRepository.findById(complaintCategory.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedComplaintCategory are not directly saved in db
        em.detach(updatedComplaintCategory);
        updatedComplaintCategory.name(UPDATED_NAME).isActive(UPDATED_IS_ACTIVE);
        ComplaintCategoryDTO complaintCategoryDTO = complaintCategoryMapper.toDto(updatedComplaintCategory);

        restComplaintCategoryMockMvc
            .perform(
                put(ENTITY_API_URL_ID, complaintCategoryDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(complaintCategoryDTO))
            )
            .andExpect(status().isOk());

        // Validate the ComplaintCategory in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedComplaintCategoryToMatchAllProperties(updatedComplaintCategory);
    }

    @Test
    @Transactional
    void putNonExistingComplaintCategory() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        complaintCategory.setId(longCount.incrementAndGet());

        // Create the ComplaintCategory
        ComplaintCategoryDTO complaintCategoryDTO = complaintCategoryMapper.toDto(complaintCategory);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restComplaintCategoryMockMvc
            .perform(
                put(ENTITY_API_URL_ID, complaintCategoryDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(complaintCategoryDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the ComplaintCategory in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchComplaintCategory() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        complaintCategory.setId(longCount.incrementAndGet());

        // Create the ComplaintCategory
        ComplaintCategoryDTO complaintCategoryDTO = complaintCategoryMapper.toDto(complaintCategory);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restComplaintCategoryMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(complaintCategoryDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the ComplaintCategory in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamComplaintCategory() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        complaintCategory.setId(longCount.incrementAndGet());

        // Create the ComplaintCategory
        ComplaintCategoryDTO complaintCategoryDTO = complaintCategoryMapper.toDto(complaintCategory);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restComplaintCategoryMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(complaintCategoryDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the ComplaintCategory in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateComplaintCategoryWithPatch() throws Exception {
        // Initialize the database
        insertedComplaintCategory = complaintCategoryRepository.saveAndFlush(complaintCategory);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the complaintCategory using partial update
        ComplaintCategory partialUpdatedComplaintCategory = new ComplaintCategory();
        partialUpdatedComplaintCategory.setId(complaintCategory.getId());

        partialUpdatedComplaintCategory.name(UPDATED_NAME).isActive(UPDATED_IS_ACTIVE);

        restComplaintCategoryMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedComplaintCategory.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedComplaintCategory))
            )
            .andExpect(status().isOk());

        // Validate the ComplaintCategory in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertComplaintCategoryUpdatableFieldsEquals(
            createUpdateProxyForBean(partialUpdatedComplaintCategory, complaintCategory),
            getPersistedComplaintCategory(complaintCategory)
        );
    }

    @Test
    @Transactional
    void fullUpdateComplaintCategoryWithPatch() throws Exception {
        // Initialize the database
        insertedComplaintCategory = complaintCategoryRepository.saveAndFlush(complaintCategory);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the complaintCategory using partial update
        ComplaintCategory partialUpdatedComplaintCategory = new ComplaintCategory();
        partialUpdatedComplaintCategory.setId(complaintCategory.getId());

        partialUpdatedComplaintCategory.name(UPDATED_NAME).isActive(UPDATED_IS_ACTIVE);

        restComplaintCategoryMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedComplaintCategory.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedComplaintCategory))
            )
            .andExpect(status().isOk());

        // Validate the ComplaintCategory in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertComplaintCategoryUpdatableFieldsEquals(
            partialUpdatedComplaintCategory,
            getPersistedComplaintCategory(partialUpdatedComplaintCategory)
        );
    }

    @Test
    @Transactional
    void patchNonExistingComplaintCategory() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        complaintCategory.setId(longCount.incrementAndGet());

        // Create the ComplaintCategory
        ComplaintCategoryDTO complaintCategoryDTO = complaintCategoryMapper.toDto(complaintCategory);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restComplaintCategoryMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, complaintCategoryDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(complaintCategoryDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the ComplaintCategory in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchComplaintCategory() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        complaintCategory.setId(longCount.incrementAndGet());

        // Create the ComplaintCategory
        ComplaintCategoryDTO complaintCategoryDTO = complaintCategoryMapper.toDto(complaintCategory);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restComplaintCategoryMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(complaintCategoryDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the ComplaintCategory in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamComplaintCategory() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        complaintCategory.setId(longCount.incrementAndGet());

        // Create the ComplaintCategory
        ComplaintCategoryDTO complaintCategoryDTO = complaintCategoryMapper.toDto(complaintCategory);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restComplaintCategoryMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(om.writeValueAsBytes(complaintCategoryDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the ComplaintCategory in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteComplaintCategory() throws Exception {
        // Initialize the database
        insertedComplaintCategory = complaintCategoryRepository.saveAndFlush(complaintCategory);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the complaintCategory
        restComplaintCategoryMockMvc
            .perform(delete(ENTITY_API_URL_ID, complaintCategory.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return complaintCategoryRepository.count();
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

    protected ComplaintCategory getPersistedComplaintCategory(ComplaintCategory complaintCategory) {
        return complaintCategoryRepository.findById(complaintCategory.getId()).orElseThrow();
    }

    protected void assertPersistedComplaintCategoryToMatchAllProperties(ComplaintCategory expectedComplaintCategory) {
        assertComplaintCategoryAllPropertiesEquals(expectedComplaintCategory, getPersistedComplaintCategory(expectedComplaintCategory));
    }

    protected void assertPersistedComplaintCategoryToMatchUpdatableProperties(ComplaintCategory expectedComplaintCategory) {
        assertComplaintCategoryAllUpdatablePropertiesEquals(
            expectedComplaintCategory,
            getPersistedComplaintCategory(expectedComplaintCategory)
        );
    }
}
