package com.crm.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.crm.IntegrationTest;
import com.crm.domain.Customer;
import com.crm.domain.MasterCategory;
import com.crm.repository.MasterCategoryRepository;
import com.crm.service.dto.MasterCategoryDTO;
import com.crm.service.mapper.MasterCategoryMapper;
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
 * Integration tests for the {@link MasterCategoryResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class MasterCategoryResourceIT {

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_DESCRIPTION = "AAAAAAAAAA";
    private static final String UPDATED_DESCRIPTION = "BBBBBBBBBB";

    private static final String DEFAULT_CODE = "AAAAAAAAAA";
    private static final String UPDATED_CODE = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/master-categories";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private MasterCategoryRepository masterCategoryRepository;

    @Autowired
    private MasterCategoryMapper masterCategoryMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restMasterCategoryMockMvc;

    private MasterCategory masterCategory;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static MasterCategory createEntity(EntityManager em) {
        MasterCategory masterCategory = new MasterCategory().name(DEFAULT_NAME).description(DEFAULT_DESCRIPTION).code(DEFAULT_CODE);
        return masterCategory;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static MasterCategory createUpdatedEntity(EntityManager em) {
        MasterCategory masterCategory = new MasterCategory().name(UPDATED_NAME).description(UPDATED_DESCRIPTION).code(UPDATED_CODE);
        return masterCategory;
    }

    @BeforeEach
    public void initTest() {
        masterCategory = createEntity(em);
    }

    @Test
    @Transactional
    void createMasterCategory() throws Exception {
        int databaseSizeBeforeCreate = masterCategoryRepository.findAll().size();
        // Create the MasterCategory
        MasterCategoryDTO masterCategoryDTO = masterCategoryMapper.toDto(masterCategory);
        restMasterCategoryMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(masterCategoryDTO))
            )
            .andExpect(status().isCreated());

        // Validate the MasterCategory in the database
        List<MasterCategory> masterCategoryList = masterCategoryRepository.findAll();
        assertThat(masterCategoryList).hasSize(databaseSizeBeforeCreate + 1);
        MasterCategory testMasterCategory = masterCategoryList.get(masterCategoryList.size() - 1);
        assertThat(testMasterCategory.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testMasterCategory.getDescription()).isEqualTo(DEFAULT_DESCRIPTION);
        assertThat(testMasterCategory.getCode()).isEqualTo(DEFAULT_CODE);
    }

    @Test
    @Transactional
    void createMasterCategoryWithExistingId() throws Exception {
        // Create the MasterCategory with an existing ID
        masterCategory.setId(1L);
        MasterCategoryDTO masterCategoryDTO = masterCategoryMapper.toDto(masterCategory);

        int databaseSizeBeforeCreate = masterCategoryRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restMasterCategoryMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(masterCategoryDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the MasterCategory in the database
        List<MasterCategory> masterCategoryList = masterCategoryRepository.findAll();
        assertThat(masterCategoryList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkNameIsRequired() throws Exception {
        int databaseSizeBeforeTest = masterCategoryRepository.findAll().size();
        // set the field null
        masterCategory.setName(null);

        // Create the MasterCategory, which fails.
        MasterCategoryDTO masterCategoryDTO = masterCategoryMapper.toDto(masterCategory);

        restMasterCategoryMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(masterCategoryDTO))
            )
            .andExpect(status().isBadRequest());

        List<MasterCategory> masterCategoryList = masterCategoryRepository.findAll();
        assertThat(masterCategoryList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllMasterCategories() throws Exception {
        // Initialize the database
        masterCategoryRepository.saveAndFlush(masterCategory);

        // Get all the masterCategoryList
        restMasterCategoryMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(masterCategory.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION)))
            .andExpect(jsonPath("$.[*].code").value(hasItem(DEFAULT_CODE)));
    }

    @Test
    @Transactional
    void getMasterCategory() throws Exception {
        // Initialize the database
        masterCategoryRepository.saveAndFlush(masterCategory);

        // Get the masterCategory
        restMasterCategoryMockMvc
            .perform(get(ENTITY_API_URL_ID, masterCategory.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(masterCategory.getId().intValue()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME))
            .andExpect(jsonPath("$.description").value(DEFAULT_DESCRIPTION))
            .andExpect(jsonPath("$.code").value(DEFAULT_CODE));
    }

    @Test
    @Transactional
    void getMasterCategoriesByIdFiltering() throws Exception {
        // Initialize the database
        masterCategoryRepository.saveAndFlush(masterCategory);

        Long id = masterCategory.getId();

        defaultMasterCategoryShouldBeFound("id.equals=" + id);
        defaultMasterCategoryShouldNotBeFound("id.notEquals=" + id);

        defaultMasterCategoryShouldBeFound("id.greaterThanOrEqual=" + id);
        defaultMasterCategoryShouldNotBeFound("id.greaterThan=" + id);

        defaultMasterCategoryShouldBeFound("id.lessThanOrEqual=" + id);
        defaultMasterCategoryShouldNotBeFound("id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllMasterCategoriesByNameIsEqualToSomething() throws Exception {
        // Initialize the database
        masterCategoryRepository.saveAndFlush(masterCategory);

        // Get all the masterCategoryList where name equals to DEFAULT_NAME
        defaultMasterCategoryShouldBeFound("name.equals=" + DEFAULT_NAME);

        // Get all the masterCategoryList where name equals to UPDATED_NAME
        defaultMasterCategoryShouldNotBeFound("name.equals=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllMasterCategoriesByNameIsInShouldWork() throws Exception {
        // Initialize the database
        masterCategoryRepository.saveAndFlush(masterCategory);

        // Get all the masterCategoryList where name in DEFAULT_NAME or UPDATED_NAME
        defaultMasterCategoryShouldBeFound("name.in=" + DEFAULT_NAME + "," + UPDATED_NAME);

        // Get all the masterCategoryList where name equals to UPDATED_NAME
        defaultMasterCategoryShouldNotBeFound("name.in=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllMasterCategoriesByNameIsNullOrNotNull() throws Exception {
        // Initialize the database
        masterCategoryRepository.saveAndFlush(masterCategory);

        // Get all the masterCategoryList where name is not null
        defaultMasterCategoryShouldBeFound("name.specified=true");

        // Get all the masterCategoryList where name is null
        defaultMasterCategoryShouldNotBeFound("name.specified=false");
    }

    @Test
    @Transactional
    void getAllMasterCategoriesByNameContainsSomething() throws Exception {
        // Initialize the database
        masterCategoryRepository.saveAndFlush(masterCategory);

        // Get all the masterCategoryList where name contains DEFAULT_NAME
        defaultMasterCategoryShouldBeFound("name.contains=" + DEFAULT_NAME);

        // Get all the masterCategoryList where name contains UPDATED_NAME
        defaultMasterCategoryShouldNotBeFound("name.contains=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllMasterCategoriesByNameNotContainsSomething() throws Exception {
        // Initialize the database
        masterCategoryRepository.saveAndFlush(masterCategory);

        // Get all the masterCategoryList where name does not contain DEFAULT_NAME
        defaultMasterCategoryShouldNotBeFound("name.doesNotContain=" + DEFAULT_NAME);

        // Get all the masterCategoryList where name does not contain UPDATED_NAME
        defaultMasterCategoryShouldBeFound("name.doesNotContain=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllMasterCategoriesByDescriptionIsEqualToSomething() throws Exception {
        // Initialize the database
        masterCategoryRepository.saveAndFlush(masterCategory);

        // Get all the masterCategoryList where description equals to DEFAULT_DESCRIPTION
        defaultMasterCategoryShouldBeFound("description.equals=" + DEFAULT_DESCRIPTION);

        // Get all the masterCategoryList where description equals to UPDATED_DESCRIPTION
        defaultMasterCategoryShouldNotBeFound("description.equals=" + UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    void getAllMasterCategoriesByDescriptionIsInShouldWork() throws Exception {
        // Initialize the database
        masterCategoryRepository.saveAndFlush(masterCategory);

        // Get all the masterCategoryList where description in DEFAULT_DESCRIPTION or UPDATED_DESCRIPTION
        defaultMasterCategoryShouldBeFound("description.in=" + DEFAULT_DESCRIPTION + "," + UPDATED_DESCRIPTION);

        // Get all the masterCategoryList where description equals to UPDATED_DESCRIPTION
        defaultMasterCategoryShouldNotBeFound("description.in=" + UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    void getAllMasterCategoriesByDescriptionIsNullOrNotNull() throws Exception {
        // Initialize the database
        masterCategoryRepository.saveAndFlush(masterCategory);

        // Get all the masterCategoryList where description is not null
        defaultMasterCategoryShouldBeFound("description.specified=true");

        // Get all the masterCategoryList where description is null
        defaultMasterCategoryShouldNotBeFound("description.specified=false");
    }

    @Test
    @Transactional
    void getAllMasterCategoriesByDescriptionContainsSomething() throws Exception {
        // Initialize the database
        masterCategoryRepository.saveAndFlush(masterCategory);

        // Get all the masterCategoryList where description contains DEFAULT_DESCRIPTION
        defaultMasterCategoryShouldBeFound("description.contains=" + DEFAULT_DESCRIPTION);

        // Get all the masterCategoryList where description contains UPDATED_DESCRIPTION
        defaultMasterCategoryShouldNotBeFound("description.contains=" + UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    void getAllMasterCategoriesByDescriptionNotContainsSomething() throws Exception {
        // Initialize the database
        masterCategoryRepository.saveAndFlush(masterCategory);

        // Get all the masterCategoryList where description does not contain DEFAULT_DESCRIPTION
        defaultMasterCategoryShouldNotBeFound("description.doesNotContain=" + DEFAULT_DESCRIPTION);

        // Get all the masterCategoryList where description does not contain UPDATED_DESCRIPTION
        defaultMasterCategoryShouldBeFound("description.doesNotContain=" + UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    void getAllMasterCategoriesByCodeIsEqualToSomething() throws Exception {
        // Initialize the database
        masterCategoryRepository.saveAndFlush(masterCategory);

        // Get all the masterCategoryList where code equals to DEFAULT_CODE
        defaultMasterCategoryShouldBeFound("code.equals=" + DEFAULT_CODE);

        // Get all the masterCategoryList where code equals to UPDATED_CODE
        defaultMasterCategoryShouldNotBeFound("code.equals=" + UPDATED_CODE);
    }

    @Test
    @Transactional
    void getAllMasterCategoriesByCodeIsInShouldWork() throws Exception {
        // Initialize the database
        masterCategoryRepository.saveAndFlush(masterCategory);

        // Get all the masterCategoryList where code in DEFAULT_CODE or UPDATED_CODE
        defaultMasterCategoryShouldBeFound("code.in=" + DEFAULT_CODE + "," + UPDATED_CODE);

        // Get all the masterCategoryList where code equals to UPDATED_CODE
        defaultMasterCategoryShouldNotBeFound("code.in=" + UPDATED_CODE);
    }

    @Test
    @Transactional
    void getAllMasterCategoriesByCodeIsNullOrNotNull() throws Exception {
        // Initialize the database
        masterCategoryRepository.saveAndFlush(masterCategory);

        // Get all the masterCategoryList where code is not null
        defaultMasterCategoryShouldBeFound("code.specified=true");

        // Get all the masterCategoryList where code is null
        defaultMasterCategoryShouldNotBeFound("code.specified=false");
    }

    @Test
    @Transactional
    void getAllMasterCategoriesByCodeContainsSomething() throws Exception {
        // Initialize the database
        masterCategoryRepository.saveAndFlush(masterCategory);

        // Get all the masterCategoryList where code contains DEFAULT_CODE
        defaultMasterCategoryShouldBeFound("code.contains=" + DEFAULT_CODE);

        // Get all the masterCategoryList where code contains UPDATED_CODE
        defaultMasterCategoryShouldNotBeFound("code.contains=" + UPDATED_CODE);
    }

    @Test
    @Transactional
    void getAllMasterCategoriesByCodeNotContainsSomething() throws Exception {
        // Initialize the database
        masterCategoryRepository.saveAndFlush(masterCategory);

        // Get all the masterCategoryList where code does not contain DEFAULT_CODE
        defaultMasterCategoryShouldNotBeFound("code.doesNotContain=" + DEFAULT_CODE);

        // Get all the masterCategoryList where code does not contain UPDATED_CODE
        defaultMasterCategoryShouldBeFound("code.doesNotContain=" + UPDATED_CODE);
    }

    @Test
    @Transactional
    void getAllMasterCategoriesByCustomersIsEqualToSomething() throws Exception {
        Customer customers;
        if (TestUtil.findAll(em, Customer.class).isEmpty()) {
            masterCategoryRepository.saveAndFlush(masterCategory);
            customers = CustomerResourceIT.createEntity(em);
        } else {
            customers = TestUtil.findAll(em, Customer.class).get(0);
        }
        em.persist(customers);
        em.flush();
        masterCategory.addCustomers(customers);
        masterCategoryRepository.saveAndFlush(masterCategory);
        Long customersId = customers.getId();
        // Get all the masterCategoryList where customers equals to customersId
        defaultMasterCategoryShouldBeFound("customersId.equals=" + customersId);

        // Get all the masterCategoryList where customers equals to (customersId + 1)
        defaultMasterCategoryShouldNotBeFound("customersId.equals=" + (customersId + 1));
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultMasterCategoryShouldBeFound(String filter) throws Exception {
        restMasterCategoryMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(masterCategory.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION)))
            .andExpect(jsonPath("$.[*].code").value(hasItem(DEFAULT_CODE)));

        // Check, that the count call also returns 1
        restMasterCategoryMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultMasterCategoryShouldNotBeFound(String filter) throws Exception {
        restMasterCategoryMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restMasterCategoryMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingMasterCategory() throws Exception {
        // Get the masterCategory
        restMasterCategoryMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingMasterCategory() throws Exception {
        // Initialize the database
        masterCategoryRepository.saveAndFlush(masterCategory);

        int databaseSizeBeforeUpdate = masterCategoryRepository.findAll().size();

        // Update the masterCategory
        MasterCategory updatedMasterCategory = masterCategoryRepository.findById(masterCategory.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedMasterCategory are not directly saved in db
        em.detach(updatedMasterCategory);
        updatedMasterCategory.name(UPDATED_NAME).description(UPDATED_DESCRIPTION).code(UPDATED_CODE);
        MasterCategoryDTO masterCategoryDTO = masterCategoryMapper.toDto(updatedMasterCategory);

        restMasterCategoryMockMvc
            .perform(
                put(ENTITY_API_URL_ID, masterCategoryDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(masterCategoryDTO))
            )
            .andExpect(status().isOk());

        // Validate the MasterCategory in the database
        List<MasterCategory> masterCategoryList = masterCategoryRepository.findAll();
        assertThat(masterCategoryList).hasSize(databaseSizeBeforeUpdate);
        MasterCategory testMasterCategory = masterCategoryList.get(masterCategoryList.size() - 1);
        assertThat(testMasterCategory.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testMasterCategory.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
        assertThat(testMasterCategory.getCode()).isEqualTo(UPDATED_CODE);
    }

    @Test
    @Transactional
    void putNonExistingMasterCategory() throws Exception {
        int databaseSizeBeforeUpdate = masterCategoryRepository.findAll().size();
        masterCategory.setId(longCount.incrementAndGet());

        // Create the MasterCategory
        MasterCategoryDTO masterCategoryDTO = masterCategoryMapper.toDto(masterCategory);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restMasterCategoryMockMvc
            .perform(
                put(ENTITY_API_URL_ID, masterCategoryDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(masterCategoryDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the MasterCategory in the database
        List<MasterCategory> masterCategoryList = masterCategoryRepository.findAll();
        assertThat(masterCategoryList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchMasterCategory() throws Exception {
        int databaseSizeBeforeUpdate = masterCategoryRepository.findAll().size();
        masterCategory.setId(longCount.incrementAndGet());

        // Create the MasterCategory
        MasterCategoryDTO masterCategoryDTO = masterCategoryMapper.toDto(masterCategory);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restMasterCategoryMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(masterCategoryDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the MasterCategory in the database
        List<MasterCategory> masterCategoryList = masterCategoryRepository.findAll();
        assertThat(masterCategoryList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamMasterCategory() throws Exception {
        int databaseSizeBeforeUpdate = masterCategoryRepository.findAll().size();
        masterCategory.setId(longCount.incrementAndGet());

        // Create the MasterCategory
        MasterCategoryDTO masterCategoryDTO = masterCategoryMapper.toDto(masterCategory);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restMasterCategoryMockMvc
            .perform(
                put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(masterCategoryDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the MasterCategory in the database
        List<MasterCategory> masterCategoryList = masterCategoryRepository.findAll();
        assertThat(masterCategoryList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateMasterCategoryWithPatch() throws Exception {
        // Initialize the database
        masterCategoryRepository.saveAndFlush(masterCategory);

        int databaseSizeBeforeUpdate = masterCategoryRepository.findAll().size();

        // Update the masterCategory using partial update
        MasterCategory partialUpdatedMasterCategory = new MasterCategory();
        partialUpdatedMasterCategory.setId(masterCategory.getId());

        partialUpdatedMasterCategory.name(UPDATED_NAME).code(UPDATED_CODE);

        restMasterCategoryMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedMasterCategory.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedMasterCategory))
            )
            .andExpect(status().isOk());

        // Validate the MasterCategory in the database
        List<MasterCategory> masterCategoryList = masterCategoryRepository.findAll();
        assertThat(masterCategoryList).hasSize(databaseSizeBeforeUpdate);
        MasterCategory testMasterCategory = masterCategoryList.get(masterCategoryList.size() - 1);
        assertThat(testMasterCategory.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testMasterCategory.getDescription()).isEqualTo(DEFAULT_DESCRIPTION);
        assertThat(testMasterCategory.getCode()).isEqualTo(UPDATED_CODE);
    }

    @Test
    @Transactional
    void fullUpdateMasterCategoryWithPatch() throws Exception {
        // Initialize the database
        masterCategoryRepository.saveAndFlush(masterCategory);

        int databaseSizeBeforeUpdate = masterCategoryRepository.findAll().size();

        // Update the masterCategory using partial update
        MasterCategory partialUpdatedMasterCategory = new MasterCategory();
        partialUpdatedMasterCategory.setId(masterCategory.getId());

        partialUpdatedMasterCategory.name(UPDATED_NAME).description(UPDATED_DESCRIPTION).code(UPDATED_CODE);

        restMasterCategoryMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedMasterCategory.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedMasterCategory))
            )
            .andExpect(status().isOk());

        // Validate the MasterCategory in the database
        List<MasterCategory> masterCategoryList = masterCategoryRepository.findAll();
        assertThat(masterCategoryList).hasSize(databaseSizeBeforeUpdate);
        MasterCategory testMasterCategory = masterCategoryList.get(masterCategoryList.size() - 1);
        assertThat(testMasterCategory.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testMasterCategory.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
        assertThat(testMasterCategory.getCode()).isEqualTo(UPDATED_CODE);
    }

    @Test
    @Transactional
    void patchNonExistingMasterCategory() throws Exception {
        int databaseSizeBeforeUpdate = masterCategoryRepository.findAll().size();
        masterCategory.setId(longCount.incrementAndGet());

        // Create the MasterCategory
        MasterCategoryDTO masterCategoryDTO = masterCategoryMapper.toDto(masterCategory);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restMasterCategoryMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, masterCategoryDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(masterCategoryDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the MasterCategory in the database
        List<MasterCategory> masterCategoryList = masterCategoryRepository.findAll();
        assertThat(masterCategoryList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchMasterCategory() throws Exception {
        int databaseSizeBeforeUpdate = masterCategoryRepository.findAll().size();
        masterCategory.setId(longCount.incrementAndGet());

        // Create the MasterCategory
        MasterCategoryDTO masterCategoryDTO = masterCategoryMapper.toDto(masterCategory);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restMasterCategoryMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(masterCategoryDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the MasterCategory in the database
        List<MasterCategory> masterCategoryList = masterCategoryRepository.findAll();
        assertThat(masterCategoryList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamMasterCategory() throws Exception {
        int databaseSizeBeforeUpdate = masterCategoryRepository.findAll().size();
        masterCategory.setId(longCount.incrementAndGet());

        // Create the MasterCategory
        MasterCategoryDTO masterCategoryDTO = masterCategoryMapper.toDto(masterCategory);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restMasterCategoryMockMvc
            .perform(
                patch(ENTITY_API_URL)
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(masterCategoryDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the MasterCategory in the database
        List<MasterCategory> masterCategoryList = masterCategoryRepository.findAll();
        assertThat(masterCategoryList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteMasterCategory() throws Exception {
        // Initialize the database
        masterCategoryRepository.saveAndFlush(masterCategory);

        int databaseSizeBeforeDelete = masterCategoryRepository.findAll().size();

        // Delete the masterCategory
        restMasterCategoryMockMvc
            .perform(delete(ENTITY_API_URL_ID, masterCategory.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<MasterCategory> masterCategoryList = masterCategoryRepository.findAll();
        assertThat(masterCategoryList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
