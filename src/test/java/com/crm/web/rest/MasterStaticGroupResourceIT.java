package com.crm.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.crm.IntegrationTest;
import com.crm.domain.EntityType;
import com.crm.domain.MasterStaticGroup;
import com.crm.domain.enumeration.MasterStaticGroupEditable;
import com.crm.repository.MasterStaticGroupRepository;
import com.crm.service.dto.MasterStaticGroupDTO;
import com.crm.service.mapper.MasterStaticGroupMapper;
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
 * Integration tests for the {@link MasterStaticGroupResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class MasterStaticGroupResourceIT {

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_DESCRIPTION = "AAAAAAAAAA";
    private static final String UPDATED_DESCRIPTION = "BBBBBBBBBB";

    private static final String DEFAULT_UI_LABEL = "AAAAAAAAAA";
    private static final String UPDATED_UI_LABEL = "BBBBBBBBBB";

    private static final MasterStaticGroupEditable DEFAULT_EDITABLE = MasterStaticGroupEditable.YES;
    private static final MasterStaticGroupEditable UPDATED_EDITABLE = MasterStaticGroupEditable.NO;

    private static final String ENTITY_API_URL = "/api/master-static-groups";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private MasterStaticGroupRepository masterStaticGroupRepository;

    @Autowired
    private MasterStaticGroupMapper masterStaticGroupMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restMasterStaticGroupMockMvc;

    private MasterStaticGroup masterStaticGroup;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static MasterStaticGroup createEntity(EntityManager em) {
        MasterStaticGroup masterStaticGroup = new MasterStaticGroup()
            .name(DEFAULT_NAME)
            .description(DEFAULT_DESCRIPTION)
            .uiLabel(DEFAULT_UI_LABEL)
            .editable(DEFAULT_EDITABLE);
        return masterStaticGroup;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static MasterStaticGroup createUpdatedEntity(EntityManager em) {
        MasterStaticGroup masterStaticGroup = new MasterStaticGroup()
            .name(UPDATED_NAME)
            .description(UPDATED_DESCRIPTION)
            .uiLabel(UPDATED_UI_LABEL)
            .editable(UPDATED_EDITABLE);
        return masterStaticGroup;
    }

    @BeforeEach
    public void initTest() {
        masterStaticGroup = createEntity(em);
    }

    @Test
    @Transactional
    void createMasterStaticGroup() throws Exception {
        int databaseSizeBeforeCreate = masterStaticGroupRepository.findAll().size();
        // Create the MasterStaticGroup
        MasterStaticGroupDTO masterStaticGroupDTO = masterStaticGroupMapper.toDto(masterStaticGroup);
        restMasterStaticGroupMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(masterStaticGroupDTO))
            )
            .andExpect(status().isCreated());

        // Validate the MasterStaticGroup in the database
        List<MasterStaticGroup> masterStaticGroupList = masterStaticGroupRepository.findAll();
        assertThat(masterStaticGroupList).hasSize(databaseSizeBeforeCreate + 1);
        MasterStaticGroup testMasterStaticGroup = masterStaticGroupList.get(masterStaticGroupList.size() - 1);
        assertThat(testMasterStaticGroup.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testMasterStaticGroup.getDescription()).isEqualTo(DEFAULT_DESCRIPTION);
        assertThat(testMasterStaticGroup.getUiLabel()).isEqualTo(DEFAULT_UI_LABEL);
        assertThat(testMasterStaticGroup.getEditable()).isEqualTo(DEFAULT_EDITABLE);
    }

    @Test
    @Transactional
    void createMasterStaticGroupWithExistingId() throws Exception {
        // Create the MasterStaticGroup with an existing ID
        masterStaticGroup.setId(1L);
        MasterStaticGroupDTO masterStaticGroupDTO = masterStaticGroupMapper.toDto(masterStaticGroup);

        int databaseSizeBeforeCreate = masterStaticGroupRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restMasterStaticGroupMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(masterStaticGroupDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the MasterStaticGroup in the database
        List<MasterStaticGroup> masterStaticGroupList = masterStaticGroupRepository.findAll();
        assertThat(masterStaticGroupList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkNameIsRequired() throws Exception {
        int databaseSizeBeforeTest = masterStaticGroupRepository.findAll().size();
        // set the field null
        masterStaticGroup.setName(null);

        // Create the MasterStaticGroup, which fails.
        MasterStaticGroupDTO masterStaticGroupDTO = masterStaticGroupMapper.toDto(masterStaticGroup);

        restMasterStaticGroupMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(masterStaticGroupDTO))
            )
            .andExpect(status().isBadRequest());

        List<MasterStaticGroup> masterStaticGroupList = masterStaticGroupRepository.findAll();
        assertThat(masterStaticGroupList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllMasterStaticGroups() throws Exception {
        // Initialize the database
        masterStaticGroupRepository.saveAndFlush(masterStaticGroup);

        // Get all the masterStaticGroupList
        restMasterStaticGroupMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(masterStaticGroup.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION)))
            .andExpect(jsonPath("$.[*].uiLabel").value(hasItem(DEFAULT_UI_LABEL)))
            .andExpect(jsonPath("$.[*].editable").value(hasItem(DEFAULT_EDITABLE.toString())));
    }

    @Test
    @Transactional
    void getMasterStaticGroup() throws Exception {
        // Initialize the database
        masterStaticGroupRepository.saveAndFlush(masterStaticGroup);

        // Get the masterStaticGroup
        restMasterStaticGroupMockMvc
            .perform(get(ENTITY_API_URL_ID, masterStaticGroup.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(masterStaticGroup.getId().intValue()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME))
            .andExpect(jsonPath("$.description").value(DEFAULT_DESCRIPTION))
            .andExpect(jsonPath("$.uiLabel").value(DEFAULT_UI_LABEL))
            .andExpect(jsonPath("$.editable").value(DEFAULT_EDITABLE.toString()));
    }

    @Test
    @Transactional
    void getMasterStaticGroupsByIdFiltering() throws Exception {
        // Initialize the database
        masterStaticGroupRepository.saveAndFlush(masterStaticGroup);

        Long id = masterStaticGroup.getId();

        defaultMasterStaticGroupShouldBeFound("id.equals=" + id);
        defaultMasterStaticGroupShouldNotBeFound("id.notEquals=" + id);

        defaultMasterStaticGroupShouldBeFound("id.greaterThanOrEqual=" + id);
        defaultMasterStaticGroupShouldNotBeFound("id.greaterThan=" + id);

        defaultMasterStaticGroupShouldBeFound("id.lessThanOrEqual=" + id);
        defaultMasterStaticGroupShouldNotBeFound("id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllMasterStaticGroupsByNameIsEqualToSomething() throws Exception {
        // Initialize the database
        masterStaticGroupRepository.saveAndFlush(masterStaticGroup);

        // Get all the masterStaticGroupList where name equals to DEFAULT_NAME
        defaultMasterStaticGroupShouldBeFound("name.equals=" + DEFAULT_NAME);

        // Get all the masterStaticGroupList where name equals to UPDATED_NAME
        defaultMasterStaticGroupShouldNotBeFound("name.equals=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllMasterStaticGroupsByNameIsInShouldWork() throws Exception {
        // Initialize the database
        masterStaticGroupRepository.saveAndFlush(masterStaticGroup);

        // Get all the masterStaticGroupList where name in DEFAULT_NAME or UPDATED_NAME
        defaultMasterStaticGroupShouldBeFound("name.in=" + DEFAULT_NAME + "," + UPDATED_NAME);

        // Get all the masterStaticGroupList where name equals to UPDATED_NAME
        defaultMasterStaticGroupShouldNotBeFound("name.in=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllMasterStaticGroupsByNameIsNullOrNotNull() throws Exception {
        // Initialize the database
        masterStaticGroupRepository.saveAndFlush(masterStaticGroup);

        // Get all the masterStaticGroupList where name is not null
        defaultMasterStaticGroupShouldBeFound("name.specified=true");

        // Get all the masterStaticGroupList where name is null
        defaultMasterStaticGroupShouldNotBeFound("name.specified=false");
    }

    @Test
    @Transactional
    void getAllMasterStaticGroupsByNameContainsSomething() throws Exception {
        // Initialize the database
        masterStaticGroupRepository.saveAndFlush(masterStaticGroup);

        // Get all the masterStaticGroupList where name contains DEFAULT_NAME
        defaultMasterStaticGroupShouldBeFound("name.contains=" + DEFAULT_NAME);

        // Get all the masterStaticGroupList where name contains UPDATED_NAME
        defaultMasterStaticGroupShouldNotBeFound("name.contains=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllMasterStaticGroupsByNameNotContainsSomething() throws Exception {
        // Initialize the database
        masterStaticGroupRepository.saveAndFlush(masterStaticGroup);

        // Get all the masterStaticGroupList where name does not contain DEFAULT_NAME
        defaultMasterStaticGroupShouldNotBeFound("name.doesNotContain=" + DEFAULT_NAME);

        // Get all the masterStaticGroupList where name does not contain UPDATED_NAME
        defaultMasterStaticGroupShouldBeFound("name.doesNotContain=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllMasterStaticGroupsByDescriptionIsEqualToSomething() throws Exception {
        // Initialize the database
        masterStaticGroupRepository.saveAndFlush(masterStaticGroup);

        // Get all the masterStaticGroupList where description equals to DEFAULT_DESCRIPTION
        defaultMasterStaticGroupShouldBeFound("description.equals=" + DEFAULT_DESCRIPTION);

        // Get all the masterStaticGroupList where description equals to UPDATED_DESCRIPTION
        defaultMasterStaticGroupShouldNotBeFound("description.equals=" + UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    void getAllMasterStaticGroupsByDescriptionIsInShouldWork() throws Exception {
        // Initialize the database
        masterStaticGroupRepository.saveAndFlush(masterStaticGroup);

        // Get all the masterStaticGroupList where description in DEFAULT_DESCRIPTION or UPDATED_DESCRIPTION
        defaultMasterStaticGroupShouldBeFound("description.in=" + DEFAULT_DESCRIPTION + "," + UPDATED_DESCRIPTION);

        // Get all the masterStaticGroupList where description equals to UPDATED_DESCRIPTION
        defaultMasterStaticGroupShouldNotBeFound("description.in=" + UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    void getAllMasterStaticGroupsByDescriptionIsNullOrNotNull() throws Exception {
        // Initialize the database
        masterStaticGroupRepository.saveAndFlush(masterStaticGroup);

        // Get all the masterStaticGroupList where description is not null
        defaultMasterStaticGroupShouldBeFound("description.specified=true");

        // Get all the masterStaticGroupList where description is null
        defaultMasterStaticGroupShouldNotBeFound("description.specified=false");
    }

    @Test
    @Transactional
    void getAllMasterStaticGroupsByDescriptionContainsSomething() throws Exception {
        // Initialize the database
        masterStaticGroupRepository.saveAndFlush(masterStaticGroup);

        // Get all the masterStaticGroupList where description contains DEFAULT_DESCRIPTION
        defaultMasterStaticGroupShouldBeFound("description.contains=" + DEFAULT_DESCRIPTION);

        // Get all the masterStaticGroupList where description contains UPDATED_DESCRIPTION
        defaultMasterStaticGroupShouldNotBeFound("description.contains=" + UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    void getAllMasterStaticGroupsByDescriptionNotContainsSomething() throws Exception {
        // Initialize the database
        masterStaticGroupRepository.saveAndFlush(masterStaticGroup);

        // Get all the masterStaticGroupList where description does not contain DEFAULT_DESCRIPTION
        defaultMasterStaticGroupShouldNotBeFound("description.doesNotContain=" + DEFAULT_DESCRIPTION);

        // Get all the masterStaticGroupList where description does not contain UPDATED_DESCRIPTION
        defaultMasterStaticGroupShouldBeFound("description.doesNotContain=" + UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    void getAllMasterStaticGroupsByUiLabelIsEqualToSomething() throws Exception {
        // Initialize the database
        masterStaticGroupRepository.saveAndFlush(masterStaticGroup);

        // Get all the masterStaticGroupList where uiLabel equals to DEFAULT_UI_LABEL
        defaultMasterStaticGroupShouldBeFound("uiLabel.equals=" + DEFAULT_UI_LABEL);

        // Get all the masterStaticGroupList where uiLabel equals to UPDATED_UI_LABEL
        defaultMasterStaticGroupShouldNotBeFound("uiLabel.equals=" + UPDATED_UI_LABEL);
    }

    @Test
    @Transactional
    void getAllMasterStaticGroupsByUiLabelIsInShouldWork() throws Exception {
        // Initialize the database
        masterStaticGroupRepository.saveAndFlush(masterStaticGroup);

        // Get all the masterStaticGroupList where uiLabel in DEFAULT_UI_LABEL or UPDATED_UI_LABEL
        defaultMasterStaticGroupShouldBeFound("uiLabel.in=" + DEFAULT_UI_LABEL + "," + UPDATED_UI_LABEL);

        // Get all the masterStaticGroupList where uiLabel equals to UPDATED_UI_LABEL
        defaultMasterStaticGroupShouldNotBeFound("uiLabel.in=" + UPDATED_UI_LABEL);
    }

    @Test
    @Transactional
    void getAllMasterStaticGroupsByUiLabelIsNullOrNotNull() throws Exception {
        // Initialize the database
        masterStaticGroupRepository.saveAndFlush(masterStaticGroup);

        // Get all the masterStaticGroupList where uiLabel is not null
        defaultMasterStaticGroupShouldBeFound("uiLabel.specified=true");

        // Get all the masterStaticGroupList where uiLabel is null
        defaultMasterStaticGroupShouldNotBeFound("uiLabel.specified=false");
    }

    @Test
    @Transactional
    void getAllMasterStaticGroupsByUiLabelContainsSomething() throws Exception {
        // Initialize the database
        masterStaticGroupRepository.saveAndFlush(masterStaticGroup);

        // Get all the masterStaticGroupList where uiLabel contains DEFAULT_UI_LABEL
        defaultMasterStaticGroupShouldBeFound("uiLabel.contains=" + DEFAULT_UI_LABEL);

        // Get all the masterStaticGroupList where uiLabel contains UPDATED_UI_LABEL
        defaultMasterStaticGroupShouldNotBeFound("uiLabel.contains=" + UPDATED_UI_LABEL);
    }

    @Test
    @Transactional
    void getAllMasterStaticGroupsByUiLabelNotContainsSomething() throws Exception {
        // Initialize the database
        masterStaticGroupRepository.saveAndFlush(masterStaticGroup);

        // Get all the masterStaticGroupList where uiLabel does not contain DEFAULT_UI_LABEL
        defaultMasterStaticGroupShouldNotBeFound("uiLabel.doesNotContain=" + DEFAULT_UI_LABEL);

        // Get all the masterStaticGroupList where uiLabel does not contain UPDATED_UI_LABEL
        defaultMasterStaticGroupShouldBeFound("uiLabel.doesNotContain=" + UPDATED_UI_LABEL);
    }

    @Test
    @Transactional
    void getAllMasterStaticGroupsByEditableIsEqualToSomething() throws Exception {
        // Initialize the database
        masterStaticGroupRepository.saveAndFlush(masterStaticGroup);

        // Get all the masterStaticGroupList where editable equals to DEFAULT_EDITABLE
        defaultMasterStaticGroupShouldBeFound("editable.equals=" + DEFAULT_EDITABLE);

        // Get all the masterStaticGroupList where editable equals to UPDATED_EDITABLE
        defaultMasterStaticGroupShouldNotBeFound("editable.equals=" + UPDATED_EDITABLE);
    }

    @Test
    @Transactional
    void getAllMasterStaticGroupsByEditableIsInShouldWork() throws Exception {
        // Initialize the database
        masterStaticGroupRepository.saveAndFlush(masterStaticGroup);

        // Get all the masterStaticGroupList where editable in DEFAULT_EDITABLE or UPDATED_EDITABLE
        defaultMasterStaticGroupShouldBeFound("editable.in=" + DEFAULT_EDITABLE + "," + UPDATED_EDITABLE);

        // Get all the masterStaticGroupList where editable equals to UPDATED_EDITABLE
        defaultMasterStaticGroupShouldNotBeFound("editable.in=" + UPDATED_EDITABLE);
    }

    @Test
    @Transactional
    void getAllMasterStaticGroupsByEditableIsNullOrNotNull() throws Exception {
        // Initialize the database
        masterStaticGroupRepository.saveAndFlush(masterStaticGroup);

        // Get all the masterStaticGroupList where editable is not null
        defaultMasterStaticGroupShouldBeFound("editable.specified=true");

        // Get all the masterStaticGroupList where editable is null
        defaultMasterStaticGroupShouldNotBeFound("editable.specified=false");
    }

    @Test
    @Transactional
    void getAllMasterStaticGroupsByEntityTypeIsEqualToSomething() throws Exception {
        EntityType entityType;
        if (TestUtil.findAll(em, EntityType.class).isEmpty()) {
            masterStaticGroupRepository.saveAndFlush(masterStaticGroup);
            entityType = EntityTypeResourceIT.createEntity(em);
        } else {
            entityType = TestUtil.findAll(em, EntityType.class).get(0);
        }
        em.persist(entityType);
        em.flush();
        masterStaticGroup.setEntityType(entityType);
        masterStaticGroupRepository.saveAndFlush(masterStaticGroup);
        Long entityTypeId = entityType.getId();
        // Get all the masterStaticGroupList where entityType equals to entityTypeId
        defaultMasterStaticGroupShouldBeFound("entityTypeId.equals=" + entityTypeId);

        // Get all the masterStaticGroupList where entityType equals to (entityTypeId + 1)
        defaultMasterStaticGroupShouldNotBeFound("entityTypeId.equals=" + (entityTypeId + 1));
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultMasterStaticGroupShouldBeFound(String filter) throws Exception {
        restMasterStaticGroupMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(masterStaticGroup.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION)))
            .andExpect(jsonPath("$.[*].uiLabel").value(hasItem(DEFAULT_UI_LABEL)))
            .andExpect(jsonPath("$.[*].editable").value(hasItem(DEFAULT_EDITABLE.toString())));

        // Check, that the count call also returns 1
        restMasterStaticGroupMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultMasterStaticGroupShouldNotBeFound(String filter) throws Exception {
        restMasterStaticGroupMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restMasterStaticGroupMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingMasterStaticGroup() throws Exception {
        // Get the masterStaticGroup
        restMasterStaticGroupMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingMasterStaticGroup() throws Exception {
        // Initialize the database
        masterStaticGroupRepository.saveAndFlush(masterStaticGroup);

        int databaseSizeBeforeUpdate = masterStaticGroupRepository.findAll().size();

        // Update the masterStaticGroup
        MasterStaticGroup updatedMasterStaticGroup = masterStaticGroupRepository.findById(masterStaticGroup.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedMasterStaticGroup are not directly saved in db
        em.detach(updatedMasterStaticGroup);
        updatedMasterStaticGroup.name(UPDATED_NAME).description(UPDATED_DESCRIPTION).uiLabel(UPDATED_UI_LABEL).editable(UPDATED_EDITABLE);
        MasterStaticGroupDTO masterStaticGroupDTO = masterStaticGroupMapper.toDto(updatedMasterStaticGroup);

        restMasterStaticGroupMockMvc
            .perform(
                put(ENTITY_API_URL_ID, masterStaticGroupDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(masterStaticGroupDTO))
            )
            .andExpect(status().isOk());

        // Validate the MasterStaticGroup in the database
        List<MasterStaticGroup> masterStaticGroupList = masterStaticGroupRepository.findAll();
        assertThat(masterStaticGroupList).hasSize(databaseSizeBeforeUpdate);
        MasterStaticGroup testMasterStaticGroup = masterStaticGroupList.get(masterStaticGroupList.size() - 1);
        assertThat(testMasterStaticGroup.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testMasterStaticGroup.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
        assertThat(testMasterStaticGroup.getUiLabel()).isEqualTo(UPDATED_UI_LABEL);
        assertThat(testMasterStaticGroup.getEditable()).isEqualTo(UPDATED_EDITABLE);
    }

    @Test
    @Transactional
    void putNonExistingMasterStaticGroup() throws Exception {
        int databaseSizeBeforeUpdate = masterStaticGroupRepository.findAll().size();
        masterStaticGroup.setId(longCount.incrementAndGet());

        // Create the MasterStaticGroup
        MasterStaticGroupDTO masterStaticGroupDTO = masterStaticGroupMapper.toDto(masterStaticGroup);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restMasterStaticGroupMockMvc
            .perform(
                put(ENTITY_API_URL_ID, masterStaticGroupDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(masterStaticGroupDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the MasterStaticGroup in the database
        List<MasterStaticGroup> masterStaticGroupList = masterStaticGroupRepository.findAll();
        assertThat(masterStaticGroupList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchMasterStaticGroup() throws Exception {
        int databaseSizeBeforeUpdate = masterStaticGroupRepository.findAll().size();
        masterStaticGroup.setId(longCount.incrementAndGet());

        // Create the MasterStaticGroup
        MasterStaticGroupDTO masterStaticGroupDTO = masterStaticGroupMapper.toDto(masterStaticGroup);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restMasterStaticGroupMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(masterStaticGroupDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the MasterStaticGroup in the database
        List<MasterStaticGroup> masterStaticGroupList = masterStaticGroupRepository.findAll();
        assertThat(masterStaticGroupList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamMasterStaticGroup() throws Exception {
        int databaseSizeBeforeUpdate = masterStaticGroupRepository.findAll().size();
        masterStaticGroup.setId(longCount.incrementAndGet());

        // Create the MasterStaticGroup
        MasterStaticGroupDTO masterStaticGroupDTO = masterStaticGroupMapper.toDto(masterStaticGroup);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restMasterStaticGroupMockMvc
            .perform(
                put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(masterStaticGroupDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the MasterStaticGroup in the database
        List<MasterStaticGroup> masterStaticGroupList = masterStaticGroupRepository.findAll();
        assertThat(masterStaticGroupList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateMasterStaticGroupWithPatch() throws Exception {
        // Initialize the database
        masterStaticGroupRepository.saveAndFlush(masterStaticGroup);

        int databaseSizeBeforeUpdate = masterStaticGroupRepository.findAll().size();

        // Update the masterStaticGroup using partial update
        MasterStaticGroup partialUpdatedMasterStaticGroup = new MasterStaticGroup();
        partialUpdatedMasterStaticGroup.setId(masterStaticGroup.getId());

        partialUpdatedMasterStaticGroup.description(UPDATED_DESCRIPTION).uiLabel(UPDATED_UI_LABEL);

        restMasterStaticGroupMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedMasterStaticGroup.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedMasterStaticGroup))
            )
            .andExpect(status().isOk());

        // Validate the MasterStaticGroup in the database
        List<MasterStaticGroup> masterStaticGroupList = masterStaticGroupRepository.findAll();
        assertThat(masterStaticGroupList).hasSize(databaseSizeBeforeUpdate);
        MasterStaticGroup testMasterStaticGroup = masterStaticGroupList.get(masterStaticGroupList.size() - 1);
        assertThat(testMasterStaticGroup.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testMasterStaticGroup.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
        assertThat(testMasterStaticGroup.getUiLabel()).isEqualTo(UPDATED_UI_LABEL);
        assertThat(testMasterStaticGroup.getEditable()).isEqualTo(DEFAULT_EDITABLE);
    }

    @Test
    @Transactional
    void fullUpdateMasterStaticGroupWithPatch() throws Exception {
        // Initialize the database
        masterStaticGroupRepository.saveAndFlush(masterStaticGroup);

        int databaseSizeBeforeUpdate = masterStaticGroupRepository.findAll().size();

        // Update the masterStaticGroup using partial update
        MasterStaticGroup partialUpdatedMasterStaticGroup = new MasterStaticGroup();
        partialUpdatedMasterStaticGroup.setId(masterStaticGroup.getId());

        partialUpdatedMasterStaticGroup
            .name(UPDATED_NAME)
            .description(UPDATED_DESCRIPTION)
            .uiLabel(UPDATED_UI_LABEL)
            .editable(UPDATED_EDITABLE);

        restMasterStaticGroupMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedMasterStaticGroup.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedMasterStaticGroup))
            )
            .andExpect(status().isOk());

        // Validate the MasterStaticGroup in the database
        List<MasterStaticGroup> masterStaticGroupList = masterStaticGroupRepository.findAll();
        assertThat(masterStaticGroupList).hasSize(databaseSizeBeforeUpdate);
        MasterStaticGroup testMasterStaticGroup = masterStaticGroupList.get(masterStaticGroupList.size() - 1);
        assertThat(testMasterStaticGroup.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testMasterStaticGroup.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
        assertThat(testMasterStaticGroup.getUiLabel()).isEqualTo(UPDATED_UI_LABEL);
        assertThat(testMasterStaticGroup.getEditable()).isEqualTo(UPDATED_EDITABLE);
    }

    @Test
    @Transactional
    void patchNonExistingMasterStaticGroup() throws Exception {
        int databaseSizeBeforeUpdate = masterStaticGroupRepository.findAll().size();
        masterStaticGroup.setId(longCount.incrementAndGet());

        // Create the MasterStaticGroup
        MasterStaticGroupDTO masterStaticGroupDTO = masterStaticGroupMapper.toDto(masterStaticGroup);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restMasterStaticGroupMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, masterStaticGroupDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(masterStaticGroupDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the MasterStaticGroup in the database
        List<MasterStaticGroup> masterStaticGroupList = masterStaticGroupRepository.findAll();
        assertThat(masterStaticGroupList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchMasterStaticGroup() throws Exception {
        int databaseSizeBeforeUpdate = masterStaticGroupRepository.findAll().size();
        masterStaticGroup.setId(longCount.incrementAndGet());

        // Create the MasterStaticGroup
        MasterStaticGroupDTO masterStaticGroupDTO = masterStaticGroupMapper.toDto(masterStaticGroup);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restMasterStaticGroupMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(masterStaticGroupDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the MasterStaticGroup in the database
        List<MasterStaticGroup> masterStaticGroupList = masterStaticGroupRepository.findAll();
        assertThat(masterStaticGroupList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamMasterStaticGroup() throws Exception {
        int databaseSizeBeforeUpdate = masterStaticGroupRepository.findAll().size();
        masterStaticGroup.setId(longCount.incrementAndGet());

        // Create the MasterStaticGroup
        MasterStaticGroupDTO masterStaticGroupDTO = masterStaticGroupMapper.toDto(masterStaticGroup);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restMasterStaticGroupMockMvc
            .perform(
                patch(ENTITY_API_URL)
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(masterStaticGroupDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the MasterStaticGroup in the database
        List<MasterStaticGroup> masterStaticGroupList = masterStaticGroupRepository.findAll();
        assertThat(masterStaticGroupList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteMasterStaticGroup() throws Exception {
        // Initialize the database
        masterStaticGroupRepository.saveAndFlush(masterStaticGroup);

        int databaseSizeBeforeDelete = masterStaticGroupRepository.findAll().size();

        // Delete the masterStaticGroup
        restMasterStaticGroupMockMvc
            .perform(delete(ENTITY_API_URL_ID, masterStaticGroup.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<MasterStaticGroup> masterStaticGroupList = masterStaticGroupRepository.findAll();
        assertThat(masterStaticGroupList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
