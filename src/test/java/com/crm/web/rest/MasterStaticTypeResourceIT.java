package com.crm.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.crm.IntegrationTest;
import com.crm.domain.MasterStaticType;
import com.crm.repository.MasterStaticTypeRepository;
import com.crm.service.dto.MasterStaticTypeDTO;
import com.crm.service.mapper.MasterStaticTypeMapper;
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
 * Integration tests for the {@link MasterStaticTypeResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class MasterStaticTypeResourceIT {

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_DESCRIPTION = "AAAAAAAAAA";
    private static final String UPDATED_DESCRIPTION = "BBBBBBBBBB";

    private static final Integer DEFAULT_DISPLAY_ORDER = 1;
    private static final Integer UPDATED_DISPLAY_ORDER = 2;
    private static final Integer SMALLER_DISPLAY_ORDER = 1 - 1;

    private static final Boolean DEFAULT_IS_ACTIVE = false;
    private static final Boolean UPDATED_IS_ACTIVE = true;

    private static final String ENTITY_API_URL = "/api/master-static-types";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private MasterStaticTypeRepository masterStaticTypeRepository;

    @Autowired
    private MasterStaticTypeMapper masterStaticTypeMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restMasterStaticTypeMockMvc;

    private MasterStaticType masterStaticType;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static MasterStaticType createEntity(EntityManager em) {
        MasterStaticType masterStaticType = new MasterStaticType()
            .name(DEFAULT_NAME)
            .description(DEFAULT_DESCRIPTION)
            .displayOrder(DEFAULT_DISPLAY_ORDER)
            .isActive(DEFAULT_IS_ACTIVE);
        return masterStaticType;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static MasterStaticType createUpdatedEntity(EntityManager em) {
        MasterStaticType masterStaticType = new MasterStaticType()
            .name(UPDATED_NAME)
            .description(UPDATED_DESCRIPTION)
            .displayOrder(UPDATED_DISPLAY_ORDER)
            .isActive(UPDATED_IS_ACTIVE);
        return masterStaticType;
    }

    @BeforeEach
    public void initTest() {
        masterStaticType = createEntity(em);
    }

    @Test
    @Transactional
    void createMasterStaticType() throws Exception {
        int databaseSizeBeforeCreate = masterStaticTypeRepository.findAll().size();
        // Create the MasterStaticType
        MasterStaticTypeDTO masterStaticTypeDTO = masterStaticTypeMapper.toDto(masterStaticType);
        restMasterStaticTypeMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(masterStaticTypeDTO))
            )
            .andExpect(status().isCreated());

        // Validate the MasterStaticType in the database
        List<MasterStaticType> masterStaticTypeList = masterStaticTypeRepository.findAll();
        assertThat(masterStaticTypeList).hasSize(databaseSizeBeforeCreate + 1);
        MasterStaticType testMasterStaticType = masterStaticTypeList.get(masterStaticTypeList.size() - 1);
        assertThat(testMasterStaticType.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testMasterStaticType.getDescription()).isEqualTo(DEFAULT_DESCRIPTION);
        assertThat(testMasterStaticType.getDisplayOrder()).isEqualTo(DEFAULT_DISPLAY_ORDER);
        assertThat(testMasterStaticType.getIsActive()).isEqualTo(DEFAULT_IS_ACTIVE);
    }

    @Test
    @Transactional
    void createMasterStaticTypeWithExistingId() throws Exception {
        // Create the MasterStaticType with an existing ID
        masterStaticType.setId(1L);
        MasterStaticTypeDTO masterStaticTypeDTO = masterStaticTypeMapper.toDto(masterStaticType);

        int databaseSizeBeforeCreate = masterStaticTypeRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restMasterStaticTypeMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(masterStaticTypeDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the MasterStaticType in the database
        List<MasterStaticType> masterStaticTypeList = masterStaticTypeRepository.findAll();
        assertThat(masterStaticTypeList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkNameIsRequired() throws Exception {
        int databaseSizeBeforeTest = masterStaticTypeRepository.findAll().size();
        // set the field null
        masterStaticType.setName(null);

        // Create the MasterStaticType, which fails.
        MasterStaticTypeDTO masterStaticTypeDTO = masterStaticTypeMapper.toDto(masterStaticType);

        restMasterStaticTypeMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(masterStaticTypeDTO))
            )
            .andExpect(status().isBadRequest());

        List<MasterStaticType> masterStaticTypeList = masterStaticTypeRepository.findAll();
        assertThat(masterStaticTypeList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllMasterStaticTypes() throws Exception {
        // Initialize the database
        masterStaticTypeRepository.saveAndFlush(masterStaticType);

        // Get all the masterStaticTypeList
        restMasterStaticTypeMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(masterStaticType.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION)))
            .andExpect(jsonPath("$.[*].displayOrder").value(hasItem(DEFAULT_DISPLAY_ORDER)))
            .andExpect(jsonPath("$.[*].isActive").value(hasItem(DEFAULT_IS_ACTIVE.booleanValue())));
    }

    @Test
    @Transactional
    void getMasterStaticType() throws Exception {
        // Initialize the database
        masterStaticTypeRepository.saveAndFlush(masterStaticType);

        // Get the masterStaticType
        restMasterStaticTypeMockMvc
            .perform(get(ENTITY_API_URL_ID, masterStaticType.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(masterStaticType.getId().intValue()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME))
            .andExpect(jsonPath("$.description").value(DEFAULT_DESCRIPTION))
            .andExpect(jsonPath("$.displayOrder").value(DEFAULT_DISPLAY_ORDER))
            .andExpect(jsonPath("$.isActive").value(DEFAULT_IS_ACTIVE.booleanValue()));
    }

    @Test
    @Transactional
    void getMasterStaticTypesByIdFiltering() throws Exception {
        // Initialize the database
        masterStaticTypeRepository.saveAndFlush(masterStaticType);

        Long id = masterStaticType.getId();

        defaultMasterStaticTypeShouldBeFound("id.equals=" + id);
        defaultMasterStaticTypeShouldNotBeFound("id.notEquals=" + id);

        defaultMasterStaticTypeShouldBeFound("id.greaterThanOrEqual=" + id);
        defaultMasterStaticTypeShouldNotBeFound("id.greaterThan=" + id);

        defaultMasterStaticTypeShouldBeFound("id.lessThanOrEqual=" + id);
        defaultMasterStaticTypeShouldNotBeFound("id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllMasterStaticTypesByNameIsEqualToSomething() throws Exception {
        // Initialize the database
        masterStaticTypeRepository.saveAndFlush(masterStaticType);

        // Get all the masterStaticTypeList where name equals to DEFAULT_NAME
        defaultMasterStaticTypeShouldBeFound("name.equals=" + DEFAULT_NAME);

        // Get all the masterStaticTypeList where name equals to UPDATED_NAME
        defaultMasterStaticTypeShouldNotBeFound("name.equals=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllMasterStaticTypesByNameIsInShouldWork() throws Exception {
        // Initialize the database
        masterStaticTypeRepository.saveAndFlush(masterStaticType);

        // Get all the masterStaticTypeList where name in DEFAULT_NAME or UPDATED_NAME
        defaultMasterStaticTypeShouldBeFound("name.in=" + DEFAULT_NAME + "," + UPDATED_NAME);

        // Get all the masterStaticTypeList where name equals to UPDATED_NAME
        defaultMasterStaticTypeShouldNotBeFound("name.in=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllMasterStaticTypesByNameIsNullOrNotNull() throws Exception {
        // Initialize the database
        masterStaticTypeRepository.saveAndFlush(masterStaticType);

        // Get all the masterStaticTypeList where name is not null
        defaultMasterStaticTypeShouldBeFound("name.specified=true");

        // Get all the masterStaticTypeList where name is null
        defaultMasterStaticTypeShouldNotBeFound("name.specified=false");
    }

    @Test
    @Transactional
    void getAllMasterStaticTypesByNameContainsSomething() throws Exception {
        // Initialize the database
        masterStaticTypeRepository.saveAndFlush(masterStaticType);

        // Get all the masterStaticTypeList where name contains DEFAULT_NAME
        defaultMasterStaticTypeShouldBeFound("name.contains=" + DEFAULT_NAME);

        // Get all the masterStaticTypeList where name contains UPDATED_NAME
        defaultMasterStaticTypeShouldNotBeFound("name.contains=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllMasterStaticTypesByNameNotContainsSomething() throws Exception {
        // Initialize the database
        masterStaticTypeRepository.saveAndFlush(masterStaticType);

        // Get all the masterStaticTypeList where name does not contain DEFAULT_NAME
        defaultMasterStaticTypeShouldNotBeFound("name.doesNotContain=" + DEFAULT_NAME);

        // Get all the masterStaticTypeList where name does not contain UPDATED_NAME
        defaultMasterStaticTypeShouldBeFound("name.doesNotContain=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllMasterStaticTypesByDescriptionIsEqualToSomething() throws Exception {
        // Initialize the database
        masterStaticTypeRepository.saveAndFlush(masterStaticType);

        // Get all the masterStaticTypeList where description equals to DEFAULT_DESCRIPTION
        defaultMasterStaticTypeShouldBeFound("description.equals=" + DEFAULT_DESCRIPTION);

        // Get all the masterStaticTypeList where description equals to UPDATED_DESCRIPTION
        defaultMasterStaticTypeShouldNotBeFound("description.equals=" + UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    void getAllMasterStaticTypesByDescriptionIsInShouldWork() throws Exception {
        // Initialize the database
        masterStaticTypeRepository.saveAndFlush(masterStaticType);

        // Get all the masterStaticTypeList where description in DEFAULT_DESCRIPTION or UPDATED_DESCRIPTION
        defaultMasterStaticTypeShouldBeFound("description.in=" + DEFAULT_DESCRIPTION + "," + UPDATED_DESCRIPTION);

        // Get all the masterStaticTypeList where description equals to UPDATED_DESCRIPTION
        defaultMasterStaticTypeShouldNotBeFound("description.in=" + UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    void getAllMasterStaticTypesByDescriptionIsNullOrNotNull() throws Exception {
        // Initialize the database
        masterStaticTypeRepository.saveAndFlush(masterStaticType);

        // Get all the masterStaticTypeList where description is not null
        defaultMasterStaticTypeShouldBeFound("description.specified=true");

        // Get all the masterStaticTypeList where description is null
        defaultMasterStaticTypeShouldNotBeFound("description.specified=false");
    }

    @Test
    @Transactional
    void getAllMasterStaticTypesByDescriptionContainsSomething() throws Exception {
        // Initialize the database
        masterStaticTypeRepository.saveAndFlush(masterStaticType);

        // Get all the masterStaticTypeList where description contains DEFAULT_DESCRIPTION
        defaultMasterStaticTypeShouldBeFound("description.contains=" + DEFAULT_DESCRIPTION);

        // Get all the masterStaticTypeList where description contains UPDATED_DESCRIPTION
        defaultMasterStaticTypeShouldNotBeFound("description.contains=" + UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    void getAllMasterStaticTypesByDescriptionNotContainsSomething() throws Exception {
        // Initialize the database
        masterStaticTypeRepository.saveAndFlush(masterStaticType);

        // Get all the masterStaticTypeList where description does not contain DEFAULT_DESCRIPTION
        defaultMasterStaticTypeShouldNotBeFound("description.doesNotContain=" + DEFAULT_DESCRIPTION);

        // Get all the masterStaticTypeList where description does not contain UPDATED_DESCRIPTION
        defaultMasterStaticTypeShouldBeFound("description.doesNotContain=" + UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    void getAllMasterStaticTypesByDisplayOrderIsEqualToSomething() throws Exception {
        // Initialize the database
        masterStaticTypeRepository.saveAndFlush(masterStaticType);

        // Get all the masterStaticTypeList where displayOrder equals to DEFAULT_DISPLAY_ORDER
        defaultMasterStaticTypeShouldBeFound("displayOrder.equals=" + DEFAULT_DISPLAY_ORDER);

        // Get all the masterStaticTypeList where displayOrder equals to UPDATED_DISPLAY_ORDER
        defaultMasterStaticTypeShouldNotBeFound("displayOrder.equals=" + UPDATED_DISPLAY_ORDER);
    }

    @Test
    @Transactional
    void getAllMasterStaticTypesByDisplayOrderIsInShouldWork() throws Exception {
        // Initialize the database
        masterStaticTypeRepository.saveAndFlush(masterStaticType);

        // Get all the masterStaticTypeList where displayOrder in DEFAULT_DISPLAY_ORDER or UPDATED_DISPLAY_ORDER
        defaultMasterStaticTypeShouldBeFound("displayOrder.in=" + DEFAULT_DISPLAY_ORDER + "," + UPDATED_DISPLAY_ORDER);

        // Get all the masterStaticTypeList where displayOrder equals to UPDATED_DISPLAY_ORDER
        defaultMasterStaticTypeShouldNotBeFound("displayOrder.in=" + UPDATED_DISPLAY_ORDER);
    }

    @Test
    @Transactional
    void getAllMasterStaticTypesByDisplayOrderIsNullOrNotNull() throws Exception {
        // Initialize the database
        masterStaticTypeRepository.saveAndFlush(masterStaticType);

        // Get all the masterStaticTypeList where displayOrder is not null
        defaultMasterStaticTypeShouldBeFound("displayOrder.specified=true");

        // Get all the masterStaticTypeList where displayOrder is null
        defaultMasterStaticTypeShouldNotBeFound("displayOrder.specified=false");
    }

    @Test
    @Transactional
    void getAllMasterStaticTypesByDisplayOrderIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        masterStaticTypeRepository.saveAndFlush(masterStaticType);

        // Get all the masterStaticTypeList where displayOrder is greater than or equal to DEFAULT_DISPLAY_ORDER
        defaultMasterStaticTypeShouldBeFound("displayOrder.greaterThanOrEqual=" + DEFAULT_DISPLAY_ORDER);

        // Get all the masterStaticTypeList where displayOrder is greater than or equal to UPDATED_DISPLAY_ORDER
        defaultMasterStaticTypeShouldNotBeFound("displayOrder.greaterThanOrEqual=" + UPDATED_DISPLAY_ORDER);
    }

    @Test
    @Transactional
    void getAllMasterStaticTypesByDisplayOrderIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        masterStaticTypeRepository.saveAndFlush(masterStaticType);

        // Get all the masterStaticTypeList where displayOrder is less than or equal to DEFAULT_DISPLAY_ORDER
        defaultMasterStaticTypeShouldBeFound("displayOrder.lessThanOrEqual=" + DEFAULT_DISPLAY_ORDER);

        // Get all the masterStaticTypeList where displayOrder is less than or equal to SMALLER_DISPLAY_ORDER
        defaultMasterStaticTypeShouldNotBeFound("displayOrder.lessThanOrEqual=" + SMALLER_DISPLAY_ORDER);
    }

    @Test
    @Transactional
    void getAllMasterStaticTypesByDisplayOrderIsLessThanSomething() throws Exception {
        // Initialize the database
        masterStaticTypeRepository.saveAndFlush(masterStaticType);

        // Get all the masterStaticTypeList where displayOrder is less than DEFAULT_DISPLAY_ORDER
        defaultMasterStaticTypeShouldNotBeFound("displayOrder.lessThan=" + DEFAULT_DISPLAY_ORDER);

        // Get all the masterStaticTypeList where displayOrder is less than UPDATED_DISPLAY_ORDER
        defaultMasterStaticTypeShouldBeFound("displayOrder.lessThan=" + UPDATED_DISPLAY_ORDER);
    }

    @Test
    @Transactional
    void getAllMasterStaticTypesByDisplayOrderIsGreaterThanSomething() throws Exception {
        // Initialize the database
        masterStaticTypeRepository.saveAndFlush(masterStaticType);

        // Get all the masterStaticTypeList where displayOrder is greater than DEFAULT_DISPLAY_ORDER
        defaultMasterStaticTypeShouldNotBeFound("displayOrder.greaterThan=" + DEFAULT_DISPLAY_ORDER);

        // Get all the masterStaticTypeList where displayOrder is greater than SMALLER_DISPLAY_ORDER
        defaultMasterStaticTypeShouldBeFound("displayOrder.greaterThan=" + SMALLER_DISPLAY_ORDER);
    }

    @Test
    @Transactional
    void getAllMasterStaticTypesByIsActiveIsEqualToSomething() throws Exception {
        // Initialize the database
        masterStaticTypeRepository.saveAndFlush(masterStaticType);

        // Get all the masterStaticTypeList where isActive equals to DEFAULT_IS_ACTIVE
        defaultMasterStaticTypeShouldBeFound("isActive.equals=" + DEFAULT_IS_ACTIVE);

        // Get all the masterStaticTypeList where isActive equals to UPDATED_IS_ACTIVE
        defaultMasterStaticTypeShouldNotBeFound("isActive.equals=" + UPDATED_IS_ACTIVE);
    }

    @Test
    @Transactional
    void getAllMasterStaticTypesByIsActiveIsInShouldWork() throws Exception {
        // Initialize the database
        masterStaticTypeRepository.saveAndFlush(masterStaticType);

        // Get all the masterStaticTypeList where isActive in DEFAULT_IS_ACTIVE or UPDATED_IS_ACTIVE
        defaultMasterStaticTypeShouldBeFound("isActive.in=" + DEFAULT_IS_ACTIVE + "," + UPDATED_IS_ACTIVE);

        // Get all the masterStaticTypeList where isActive equals to UPDATED_IS_ACTIVE
        defaultMasterStaticTypeShouldNotBeFound("isActive.in=" + UPDATED_IS_ACTIVE);
    }

    @Test
    @Transactional
    void getAllMasterStaticTypesByIsActiveIsNullOrNotNull() throws Exception {
        // Initialize the database
        masterStaticTypeRepository.saveAndFlush(masterStaticType);

        // Get all the masterStaticTypeList where isActive is not null
        defaultMasterStaticTypeShouldBeFound("isActive.specified=true");

        // Get all the masterStaticTypeList where isActive is null
        defaultMasterStaticTypeShouldNotBeFound("isActive.specified=false");
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultMasterStaticTypeShouldBeFound(String filter) throws Exception {
        restMasterStaticTypeMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(masterStaticType.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION)))
            .andExpect(jsonPath("$.[*].displayOrder").value(hasItem(DEFAULT_DISPLAY_ORDER)))
            .andExpect(jsonPath("$.[*].isActive").value(hasItem(DEFAULT_IS_ACTIVE.booleanValue())));

        // Check, that the count call also returns 1
        restMasterStaticTypeMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultMasterStaticTypeShouldNotBeFound(String filter) throws Exception {
        restMasterStaticTypeMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restMasterStaticTypeMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingMasterStaticType() throws Exception {
        // Get the masterStaticType
        restMasterStaticTypeMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingMasterStaticType() throws Exception {
        // Initialize the database
        masterStaticTypeRepository.saveAndFlush(masterStaticType);

        int databaseSizeBeforeUpdate = masterStaticTypeRepository.findAll().size();

        // Update the masterStaticType
        MasterStaticType updatedMasterStaticType = masterStaticTypeRepository.findById(masterStaticType.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedMasterStaticType are not directly saved in db
        em.detach(updatedMasterStaticType);
        updatedMasterStaticType
            .name(UPDATED_NAME)
            .description(UPDATED_DESCRIPTION)
            .displayOrder(UPDATED_DISPLAY_ORDER)
            .isActive(UPDATED_IS_ACTIVE);
        MasterStaticTypeDTO masterStaticTypeDTO = masterStaticTypeMapper.toDto(updatedMasterStaticType);

        restMasterStaticTypeMockMvc
            .perform(
                put(ENTITY_API_URL_ID, masterStaticTypeDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(masterStaticTypeDTO))
            )
            .andExpect(status().isOk());

        // Validate the MasterStaticType in the database
        List<MasterStaticType> masterStaticTypeList = masterStaticTypeRepository.findAll();
        assertThat(masterStaticTypeList).hasSize(databaseSizeBeforeUpdate);
        MasterStaticType testMasterStaticType = masterStaticTypeList.get(masterStaticTypeList.size() - 1);
        assertThat(testMasterStaticType.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testMasterStaticType.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
        assertThat(testMasterStaticType.getDisplayOrder()).isEqualTo(UPDATED_DISPLAY_ORDER);
        assertThat(testMasterStaticType.getIsActive()).isEqualTo(UPDATED_IS_ACTIVE);
    }

    @Test
    @Transactional
    void putNonExistingMasterStaticType() throws Exception {
        int databaseSizeBeforeUpdate = masterStaticTypeRepository.findAll().size();
        masterStaticType.setId(longCount.incrementAndGet());

        // Create the MasterStaticType
        MasterStaticTypeDTO masterStaticTypeDTO = masterStaticTypeMapper.toDto(masterStaticType);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restMasterStaticTypeMockMvc
            .perform(
                put(ENTITY_API_URL_ID, masterStaticTypeDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(masterStaticTypeDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the MasterStaticType in the database
        List<MasterStaticType> masterStaticTypeList = masterStaticTypeRepository.findAll();
        assertThat(masterStaticTypeList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchMasterStaticType() throws Exception {
        int databaseSizeBeforeUpdate = masterStaticTypeRepository.findAll().size();
        masterStaticType.setId(longCount.incrementAndGet());

        // Create the MasterStaticType
        MasterStaticTypeDTO masterStaticTypeDTO = masterStaticTypeMapper.toDto(masterStaticType);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restMasterStaticTypeMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(masterStaticTypeDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the MasterStaticType in the database
        List<MasterStaticType> masterStaticTypeList = masterStaticTypeRepository.findAll();
        assertThat(masterStaticTypeList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamMasterStaticType() throws Exception {
        int databaseSizeBeforeUpdate = masterStaticTypeRepository.findAll().size();
        masterStaticType.setId(longCount.incrementAndGet());

        // Create the MasterStaticType
        MasterStaticTypeDTO masterStaticTypeDTO = masterStaticTypeMapper.toDto(masterStaticType);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restMasterStaticTypeMockMvc
            .perform(
                put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(masterStaticTypeDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the MasterStaticType in the database
        List<MasterStaticType> masterStaticTypeList = masterStaticTypeRepository.findAll();
        assertThat(masterStaticTypeList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateMasterStaticTypeWithPatch() throws Exception {
        // Initialize the database
        masterStaticTypeRepository.saveAndFlush(masterStaticType);

        int databaseSizeBeforeUpdate = masterStaticTypeRepository.findAll().size();

        // Update the masterStaticType using partial update
        MasterStaticType partialUpdatedMasterStaticType = new MasterStaticType();
        partialUpdatedMasterStaticType.setId(masterStaticType.getId());

        partialUpdatedMasterStaticType.description(UPDATED_DESCRIPTION).displayOrder(UPDATED_DISPLAY_ORDER).isActive(UPDATED_IS_ACTIVE);

        restMasterStaticTypeMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedMasterStaticType.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedMasterStaticType))
            )
            .andExpect(status().isOk());

        // Validate the MasterStaticType in the database
        List<MasterStaticType> masterStaticTypeList = masterStaticTypeRepository.findAll();
        assertThat(masterStaticTypeList).hasSize(databaseSizeBeforeUpdate);
        MasterStaticType testMasterStaticType = masterStaticTypeList.get(masterStaticTypeList.size() - 1);
        assertThat(testMasterStaticType.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testMasterStaticType.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
        assertThat(testMasterStaticType.getDisplayOrder()).isEqualTo(UPDATED_DISPLAY_ORDER);
        assertThat(testMasterStaticType.getIsActive()).isEqualTo(UPDATED_IS_ACTIVE);
    }

    @Test
    @Transactional
    void fullUpdateMasterStaticTypeWithPatch() throws Exception {
        // Initialize the database
        masterStaticTypeRepository.saveAndFlush(masterStaticType);

        int databaseSizeBeforeUpdate = masterStaticTypeRepository.findAll().size();

        // Update the masterStaticType using partial update
        MasterStaticType partialUpdatedMasterStaticType = new MasterStaticType();
        partialUpdatedMasterStaticType.setId(masterStaticType.getId());

        partialUpdatedMasterStaticType
            .name(UPDATED_NAME)
            .description(UPDATED_DESCRIPTION)
            .displayOrder(UPDATED_DISPLAY_ORDER)
            .isActive(UPDATED_IS_ACTIVE);

        restMasterStaticTypeMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedMasterStaticType.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedMasterStaticType))
            )
            .andExpect(status().isOk());

        // Validate the MasterStaticType in the database
        List<MasterStaticType> masterStaticTypeList = masterStaticTypeRepository.findAll();
        assertThat(masterStaticTypeList).hasSize(databaseSizeBeforeUpdate);
        MasterStaticType testMasterStaticType = masterStaticTypeList.get(masterStaticTypeList.size() - 1);
        assertThat(testMasterStaticType.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testMasterStaticType.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
        assertThat(testMasterStaticType.getDisplayOrder()).isEqualTo(UPDATED_DISPLAY_ORDER);
        assertThat(testMasterStaticType.getIsActive()).isEqualTo(UPDATED_IS_ACTIVE);
    }

    @Test
    @Transactional
    void patchNonExistingMasterStaticType() throws Exception {
        int databaseSizeBeforeUpdate = masterStaticTypeRepository.findAll().size();
        masterStaticType.setId(longCount.incrementAndGet());

        // Create the MasterStaticType
        MasterStaticTypeDTO masterStaticTypeDTO = masterStaticTypeMapper.toDto(masterStaticType);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restMasterStaticTypeMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, masterStaticTypeDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(masterStaticTypeDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the MasterStaticType in the database
        List<MasterStaticType> masterStaticTypeList = masterStaticTypeRepository.findAll();
        assertThat(masterStaticTypeList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchMasterStaticType() throws Exception {
        int databaseSizeBeforeUpdate = masterStaticTypeRepository.findAll().size();
        masterStaticType.setId(longCount.incrementAndGet());

        // Create the MasterStaticType
        MasterStaticTypeDTO masterStaticTypeDTO = masterStaticTypeMapper.toDto(masterStaticType);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restMasterStaticTypeMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(masterStaticTypeDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the MasterStaticType in the database
        List<MasterStaticType> masterStaticTypeList = masterStaticTypeRepository.findAll();
        assertThat(masterStaticTypeList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamMasterStaticType() throws Exception {
        int databaseSizeBeforeUpdate = masterStaticTypeRepository.findAll().size();
        masterStaticType.setId(longCount.incrementAndGet());

        // Create the MasterStaticType
        MasterStaticTypeDTO masterStaticTypeDTO = masterStaticTypeMapper.toDto(masterStaticType);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restMasterStaticTypeMockMvc
            .perform(
                patch(ENTITY_API_URL)
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(masterStaticTypeDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the MasterStaticType in the database
        List<MasterStaticType> masterStaticTypeList = masterStaticTypeRepository.findAll();
        assertThat(masterStaticTypeList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteMasterStaticType() throws Exception {
        // Initialize the database
        masterStaticTypeRepository.saveAndFlush(masterStaticType);

        int databaseSizeBeforeDelete = masterStaticTypeRepository.findAll().size();

        // Delete the masterStaticType
        restMasterStaticTypeMockMvc
            .perform(delete(ENTITY_API_URL_ID, masterStaticType.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<MasterStaticType> masterStaticTypeList = masterStaticTypeRepository.findAll();
        assertThat(masterStaticTypeList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
