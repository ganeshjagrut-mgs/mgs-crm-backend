package com.crm.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.crm.IntegrationTest;
import com.crm.domain.Customer;
import com.crm.domain.Lead;
import com.crm.domain.MasterStaticType;
import com.crm.domain.User;
import com.crm.repository.LeadRepository;
import com.crm.service.dto.LeadDTO;
import com.crm.service.mapper.LeadMapper;
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
 * Integration tests for the {@link LeadResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class LeadResourceIT {

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_LEAD_NUMBER = "AAAAAAAAAA";
    private static final String UPDATED_LEAD_NUMBER = "BBBBBBBBBB";

    private static final Integer DEFAULT_ANNUAL_REVENUE = 1;
    private static final Integer UPDATED_ANNUAL_REVENUE = 2;
    private static final Integer SMALLER_ANNUAL_REVENUE = 1 - 1;

    private static final String ENTITY_API_URL = "/api/leads";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private LeadRepository leadRepository;

    @Autowired
    private LeadMapper leadMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restLeadMockMvc;

    private Lead lead;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Lead createEntity(EntityManager em) {
        Lead lead = new Lead().name(DEFAULT_NAME).leadNumber(DEFAULT_LEAD_NUMBER).annualRevenue(DEFAULT_ANNUAL_REVENUE);
        return lead;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Lead createUpdatedEntity(EntityManager em) {
        Lead lead = new Lead().name(UPDATED_NAME).leadNumber(UPDATED_LEAD_NUMBER).annualRevenue(UPDATED_ANNUAL_REVENUE);
        return lead;
    }

    @BeforeEach
    public void initTest() {
        lead = createEntity(em);
    }

    @Test
    @Transactional
    void createLead() throws Exception {
        int databaseSizeBeforeCreate = leadRepository.findAll().size();
        // Create the Lead
        LeadDTO leadDTO = leadMapper.toDto(lead);
        restLeadMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(leadDTO)))
            .andExpect(status().isCreated());

        // Validate the Lead in the database
        List<Lead> leadList = leadRepository.findAll();
        assertThat(leadList).hasSize(databaseSizeBeforeCreate + 1);
        Lead testLead = leadList.get(leadList.size() - 1);
        assertThat(testLead.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testLead.getLeadNumber()).isEqualTo(DEFAULT_LEAD_NUMBER);
        assertThat(testLead.getAnnualRevenue()).isEqualTo(DEFAULT_ANNUAL_REVENUE);
    }

    @Test
    @Transactional
    void createLeadWithExistingId() throws Exception {
        // Create the Lead with an existing ID
        lead.setId(1L);
        LeadDTO leadDTO = leadMapper.toDto(lead);

        int databaseSizeBeforeCreate = leadRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restLeadMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(leadDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Lead in the database
        List<Lead> leadList = leadRepository.findAll();
        assertThat(leadList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkNameIsRequired() throws Exception {
        int databaseSizeBeforeTest = leadRepository.findAll().size();
        // set the field null
        lead.setName(null);

        // Create the Lead, which fails.
        LeadDTO leadDTO = leadMapper.toDto(lead);

        restLeadMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(leadDTO)))
            .andExpect(status().isBadRequest());

        List<Lead> leadList = leadRepository.findAll();
        assertThat(leadList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllLeads() throws Exception {
        // Initialize the database
        leadRepository.saveAndFlush(lead);

        // Get all the leadList
        restLeadMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(lead.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)))
            .andExpect(jsonPath("$.[*].leadNumber").value(hasItem(DEFAULT_LEAD_NUMBER)))
            .andExpect(jsonPath("$.[*].annualRevenue").value(hasItem(DEFAULT_ANNUAL_REVENUE)));
    }

    @Test
    @Transactional
    void getLead() throws Exception {
        // Initialize the database
        leadRepository.saveAndFlush(lead);

        // Get the lead
        restLeadMockMvc
            .perform(get(ENTITY_API_URL_ID, lead.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(lead.getId().intValue()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME))
            .andExpect(jsonPath("$.leadNumber").value(DEFAULT_LEAD_NUMBER))
            .andExpect(jsonPath("$.annualRevenue").value(DEFAULT_ANNUAL_REVENUE));
    }

    @Test
    @Transactional
    void getLeadsByIdFiltering() throws Exception {
        // Initialize the database
        leadRepository.saveAndFlush(lead);

        Long id = lead.getId();

        defaultLeadShouldBeFound("id.equals=" + id);
        defaultLeadShouldNotBeFound("id.notEquals=" + id);

        defaultLeadShouldBeFound("id.greaterThanOrEqual=" + id);
        defaultLeadShouldNotBeFound("id.greaterThan=" + id);

        defaultLeadShouldBeFound("id.lessThanOrEqual=" + id);
        defaultLeadShouldNotBeFound("id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllLeadsByNameIsEqualToSomething() throws Exception {
        // Initialize the database
        leadRepository.saveAndFlush(lead);

        // Get all the leadList where name equals to DEFAULT_NAME
        defaultLeadShouldBeFound("name.equals=" + DEFAULT_NAME);

        // Get all the leadList where name equals to UPDATED_NAME
        defaultLeadShouldNotBeFound("name.equals=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllLeadsByNameIsInShouldWork() throws Exception {
        // Initialize the database
        leadRepository.saveAndFlush(lead);

        // Get all the leadList where name in DEFAULT_NAME or UPDATED_NAME
        defaultLeadShouldBeFound("name.in=" + DEFAULT_NAME + "," + UPDATED_NAME);

        // Get all the leadList where name equals to UPDATED_NAME
        defaultLeadShouldNotBeFound("name.in=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllLeadsByNameIsNullOrNotNull() throws Exception {
        // Initialize the database
        leadRepository.saveAndFlush(lead);

        // Get all the leadList where name is not null
        defaultLeadShouldBeFound("name.specified=true");

        // Get all the leadList where name is null
        defaultLeadShouldNotBeFound("name.specified=false");
    }

    @Test
    @Transactional
    void getAllLeadsByNameContainsSomething() throws Exception {
        // Initialize the database
        leadRepository.saveAndFlush(lead);

        // Get all the leadList where name contains DEFAULT_NAME
        defaultLeadShouldBeFound("name.contains=" + DEFAULT_NAME);

        // Get all the leadList where name contains UPDATED_NAME
        defaultLeadShouldNotBeFound("name.contains=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllLeadsByNameNotContainsSomething() throws Exception {
        // Initialize the database
        leadRepository.saveAndFlush(lead);

        // Get all the leadList where name does not contain DEFAULT_NAME
        defaultLeadShouldNotBeFound("name.doesNotContain=" + DEFAULT_NAME);

        // Get all the leadList where name does not contain UPDATED_NAME
        defaultLeadShouldBeFound("name.doesNotContain=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllLeadsByLeadNumberIsEqualToSomething() throws Exception {
        // Initialize the database
        leadRepository.saveAndFlush(lead);

        // Get all the leadList where leadNumber equals to DEFAULT_LEAD_NUMBER
        defaultLeadShouldBeFound("leadNumber.equals=" + DEFAULT_LEAD_NUMBER);

        // Get all the leadList where leadNumber equals to UPDATED_LEAD_NUMBER
        defaultLeadShouldNotBeFound("leadNumber.equals=" + UPDATED_LEAD_NUMBER);
    }

    @Test
    @Transactional
    void getAllLeadsByLeadNumberIsInShouldWork() throws Exception {
        // Initialize the database
        leadRepository.saveAndFlush(lead);

        // Get all the leadList where leadNumber in DEFAULT_LEAD_NUMBER or UPDATED_LEAD_NUMBER
        defaultLeadShouldBeFound("leadNumber.in=" + DEFAULT_LEAD_NUMBER + "," + UPDATED_LEAD_NUMBER);

        // Get all the leadList where leadNumber equals to UPDATED_LEAD_NUMBER
        defaultLeadShouldNotBeFound("leadNumber.in=" + UPDATED_LEAD_NUMBER);
    }

    @Test
    @Transactional
    void getAllLeadsByLeadNumberIsNullOrNotNull() throws Exception {
        // Initialize the database
        leadRepository.saveAndFlush(lead);

        // Get all the leadList where leadNumber is not null
        defaultLeadShouldBeFound("leadNumber.specified=true");

        // Get all the leadList where leadNumber is null
        defaultLeadShouldNotBeFound("leadNumber.specified=false");
    }

    @Test
    @Transactional
    void getAllLeadsByLeadNumberContainsSomething() throws Exception {
        // Initialize the database
        leadRepository.saveAndFlush(lead);

        // Get all the leadList where leadNumber contains DEFAULT_LEAD_NUMBER
        defaultLeadShouldBeFound("leadNumber.contains=" + DEFAULT_LEAD_NUMBER);

        // Get all the leadList where leadNumber contains UPDATED_LEAD_NUMBER
        defaultLeadShouldNotBeFound("leadNumber.contains=" + UPDATED_LEAD_NUMBER);
    }

    @Test
    @Transactional
    void getAllLeadsByLeadNumberNotContainsSomething() throws Exception {
        // Initialize the database
        leadRepository.saveAndFlush(lead);

        // Get all the leadList where leadNumber does not contain DEFAULT_LEAD_NUMBER
        defaultLeadShouldNotBeFound("leadNumber.doesNotContain=" + DEFAULT_LEAD_NUMBER);

        // Get all the leadList where leadNumber does not contain UPDATED_LEAD_NUMBER
        defaultLeadShouldBeFound("leadNumber.doesNotContain=" + UPDATED_LEAD_NUMBER);
    }

    @Test
    @Transactional
    void getAllLeadsByAnnualRevenueIsEqualToSomething() throws Exception {
        // Initialize the database
        leadRepository.saveAndFlush(lead);

        // Get all the leadList where annualRevenue equals to DEFAULT_ANNUAL_REVENUE
        defaultLeadShouldBeFound("annualRevenue.equals=" + DEFAULT_ANNUAL_REVENUE);

        // Get all the leadList where annualRevenue equals to UPDATED_ANNUAL_REVENUE
        defaultLeadShouldNotBeFound("annualRevenue.equals=" + UPDATED_ANNUAL_REVENUE);
    }

    @Test
    @Transactional
    void getAllLeadsByAnnualRevenueIsInShouldWork() throws Exception {
        // Initialize the database
        leadRepository.saveAndFlush(lead);

        // Get all the leadList where annualRevenue in DEFAULT_ANNUAL_REVENUE or UPDATED_ANNUAL_REVENUE
        defaultLeadShouldBeFound("annualRevenue.in=" + DEFAULT_ANNUAL_REVENUE + "," + UPDATED_ANNUAL_REVENUE);

        // Get all the leadList where annualRevenue equals to UPDATED_ANNUAL_REVENUE
        defaultLeadShouldNotBeFound("annualRevenue.in=" + UPDATED_ANNUAL_REVENUE);
    }

    @Test
    @Transactional
    void getAllLeadsByAnnualRevenueIsNullOrNotNull() throws Exception {
        // Initialize the database
        leadRepository.saveAndFlush(lead);

        // Get all the leadList where annualRevenue is not null
        defaultLeadShouldBeFound("annualRevenue.specified=true");

        // Get all the leadList where annualRevenue is null
        defaultLeadShouldNotBeFound("annualRevenue.specified=false");
    }

    @Test
    @Transactional
    void getAllLeadsByAnnualRevenueIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        leadRepository.saveAndFlush(lead);

        // Get all the leadList where annualRevenue is greater than or equal to DEFAULT_ANNUAL_REVENUE
        defaultLeadShouldBeFound("annualRevenue.greaterThanOrEqual=" + DEFAULT_ANNUAL_REVENUE);

        // Get all the leadList where annualRevenue is greater than or equal to UPDATED_ANNUAL_REVENUE
        defaultLeadShouldNotBeFound("annualRevenue.greaterThanOrEqual=" + UPDATED_ANNUAL_REVENUE);
    }

    @Test
    @Transactional
    void getAllLeadsByAnnualRevenueIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        leadRepository.saveAndFlush(lead);

        // Get all the leadList where annualRevenue is less than or equal to DEFAULT_ANNUAL_REVENUE
        defaultLeadShouldBeFound("annualRevenue.lessThanOrEqual=" + DEFAULT_ANNUAL_REVENUE);

        // Get all the leadList where annualRevenue is less than or equal to SMALLER_ANNUAL_REVENUE
        defaultLeadShouldNotBeFound("annualRevenue.lessThanOrEqual=" + SMALLER_ANNUAL_REVENUE);
    }

    @Test
    @Transactional
    void getAllLeadsByAnnualRevenueIsLessThanSomething() throws Exception {
        // Initialize the database
        leadRepository.saveAndFlush(lead);

        // Get all the leadList where annualRevenue is less than DEFAULT_ANNUAL_REVENUE
        defaultLeadShouldNotBeFound("annualRevenue.lessThan=" + DEFAULT_ANNUAL_REVENUE);

        // Get all the leadList where annualRevenue is less than UPDATED_ANNUAL_REVENUE
        defaultLeadShouldBeFound("annualRevenue.lessThan=" + UPDATED_ANNUAL_REVENUE);
    }

    @Test
    @Transactional
    void getAllLeadsByAnnualRevenueIsGreaterThanSomething() throws Exception {
        // Initialize the database
        leadRepository.saveAndFlush(lead);

        // Get all the leadList where annualRevenue is greater than DEFAULT_ANNUAL_REVENUE
        defaultLeadShouldNotBeFound("annualRevenue.greaterThan=" + DEFAULT_ANNUAL_REVENUE);

        // Get all the leadList where annualRevenue is greater than SMALLER_ANNUAL_REVENUE
        defaultLeadShouldBeFound("annualRevenue.greaterThan=" + SMALLER_ANNUAL_REVENUE);
    }

    @Test
    @Transactional
    void getAllLeadsByUserIsEqualToSomething() throws Exception {
        User user;
        if (TestUtil.findAll(em, User.class).isEmpty()) {
            leadRepository.saveAndFlush(lead);
            user = UserResourceIT.createEntity(em);
        } else {
            user = TestUtil.findAll(em, User.class).get(0);
        }
        em.persist(user);
        em.flush();
        lead.setUser(user);
        leadRepository.saveAndFlush(lead);
        Long userId = user.getId();
        // Get all the leadList where user equals to userId
        defaultLeadShouldBeFound("userId.equals=" + userId);

        // Get all the leadList where user equals to (userId + 1)
        defaultLeadShouldNotBeFound("userId.equals=" + (userId + 1));
    }

    @Test
    @Transactional
    void getAllLeadsByCustomerIsEqualToSomething() throws Exception {
        Customer customer;
        if (TestUtil.findAll(em, Customer.class).isEmpty()) {
            leadRepository.saveAndFlush(lead);
            customer = CustomerResourceIT.createEntity(em);
        } else {
            customer = TestUtil.findAll(em, Customer.class).get(0);
        }
        em.persist(customer);
        em.flush();
        lead.setCustomer(customer);
        leadRepository.saveAndFlush(lead);
        Long customerId = customer.getId();
        // Get all the leadList where customer equals to customerId
        defaultLeadShouldBeFound("customerId.equals=" + customerId);

        // Get all the leadList where customer equals to (customerId + 1)
        defaultLeadShouldNotBeFound("customerId.equals=" + (customerId + 1));
    }

    @Test
    @Transactional
    void getAllLeadsByLeadSourceIsEqualToSomething() throws Exception {
        MasterStaticType leadSource;
        if (TestUtil.findAll(em, MasterStaticType.class).isEmpty()) {
            leadRepository.saveAndFlush(lead);
            leadSource = MasterStaticTypeResourceIT.createEntity(em);
        } else {
            leadSource = TestUtil.findAll(em, MasterStaticType.class).get(0);
        }
        em.persist(leadSource);
        em.flush();
        lead.setLeadSource(leadSource);
        leadRepository.saveAndFlush(lead);
        Long leadSourceId = leadSource.getId();
        // Get all the leadList where leadSource equals to leadSourceId
        defaultLeadShouldBeFound("leadSourceId.equals=" + leadSourceId);

        // Get all the leadList where leadSource equals to (leadSourceId + 1)
        defaultLeadShouldNotBeFound("leadSourceId.equals=" + (leadSourceId + 1));
    }

    @Test
    @Transactional
    void getAllLeadsByIndustryTypeIsEqualToSomething() throws Exception {
        MasterStaticType industryType;
        if (TestUtil.findAll(em, MasterStaticType.class).isEmpty()) {
            leadRepository.saveAndFlush(lead);
            industryType = MasterStaticTypeResourceIT.createEntity(em);
        } else {
            industryType = TestUtil.findAll(em, MasterStaticType.class).get(0);
        }
        em.persist(industryType);
        em.flush();
        lead.setIndustryType(industryType);
        leadRepository.saveAndFlush(lead);
        Long industryTypeId = industryType.getId();
        // Get all the leadList where industryType equals to industryTypeId
        defaultLeadShouldBeFound("industryTypeId.equals=" + industryTypeId);

        // Get all the leadList where industryType equals to (industryTypeId + 1)
        defaultLeadShouldNotBeFound("industryTypeId.equals=" + (industryTypeId + 1));
    }

    @Test
    @Transactional
    void getAllLeadsByLeadStatusIsEqualToSomething() throws Exception {
        MasterStaticType leadStatus;
        if (TestUtil.findAll(em, MasterStaticType.class).isEmpty()) {
            leadRepository.saveAndFlush(lead);
            leadStatus = MasterStaticTypeResourceIT.createEntity(em);
        } else {
            leadStatus = TestUtil.findAll(em, MasterStaticType.class).get(0);
        }
        em.persist(leadStatus);
        em.flush();
        lead.setLeadStatus(leadStatus);
        leadRepository.saveAndFlush(lead);
        Long leadStatusId = leadStatus.getId();
        // Get all the leadList where leadStatus equals to leadStatusId
        defaultLeadShouldBeFound("leadStatusId.equals=" + leadStatusId);

        // Get all the leadList where leadStatus equals to (leadStatusId + 1)
        defaultLeadShouldNotBeFound("leadStatusId.equals=" + (leadStatusId + 1));
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultLeadShouldBeFound(String filter) throws Exception {
        restLeadMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(lead.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)))
            .andExpect(jsonPath("$.[*].leadNumber").value(hasItem(DEFAULT_LEAD_NUMBER)))
            .andExpect(jsonPath("$.[*].annualRevenue").value(hasItem(DEFAULT_ANNUAL_REVENUE)));

        // Check, that the count call also returns 1
        restLeadMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultLeadShouldNotBeFound(String filter) throws Exception {
        restLeadMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restLeadMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingLead() throws Exception {
        // Get the lead
        restLeadMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingLead() throws Exception {
        // Initialize the database
        leadRepository.saveAndFlush(lead);

        int databaseSizeBeforeUpdate = leadRepository.findAll().size();

        // Update the lead
        Lead updatedLead = leadRepository.findById(lead.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedLead are not directly saved in db
        em.detach(updatedLead);
        updatedLead.name(UPDATED_NAME).leadNumber(UPDATED_LEAD_NUMBER).annualRevenue(UPDATED_ANNUAL_REVENUE);
        LeadDTO leadDTO = leadMapper.toDto(updatedLead);

        restLeadMockMvc
            .perform(
                put(ENTITY_API_URL_ID, leadDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(leadDTO))
            )
            .andExpect(status().isOk());

        // Validate the Lead in the database
        List<Lead> leadList = leadRepository.findAll();
        assertThat(leadList).hasSize(databaseSizeBeforeUpdate);
        Lead testLead = leadList.get(leadList.size() - 1);
        assertThat(testLead.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testLead.getLeadNumber()).isEqualTo(UPDATED_LEAD_NUMBER);
        assertThat(testLead.getAnnualRevenue()).isEqualTo(UPDATED_ANNUAL_REVENUE);
    }

    @Test
    @Transactional
    void putNonExistingLead() throws Exception {
        int databaseSizeBeforeUpdate = leadRepository.findAll().size();
        lead.setId(longCount.incrementAndGet());

        // Create the Lead
        LeadDTO leadDTO = leadMapper.toDto(lead);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restLeadMockMvc
            .perform(
                put(ENTITY_API_URL_ID, leadDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(leadDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Lead in the database
        List<Lead> leadList = leadRepository.findAll();
        assertThat(leadList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchLead() throws Exception {
        int databaseSizeBeforeUpdate = leadRepository.findAll().size();
        lead.setId(longCount.incrementAndGet());

        // Create the Lead
        LeadDTO leadDTO = leadMapper.toDto(lead);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restLeadMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(leadDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Lead in the database
        List<Lead> leadList = leadRepository.findAll();
        assertThat(leadList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamLead() throws Exception {
        int databaseSizeBeforeUpdate = leadRepository.findAll().size();
        lead.setId(longCount.incrementAndGet());

        // Create the Lead
        LeadDTO leadDTO = leadMapper.toDto(lead);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restLeadMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(leadDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Lead in the database
        List<Lead> leadList = leadRepository.findAll();
        assertThat(leadList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateLeadWithPatch() throws Exception {
        // Initialize the database
        leadRepository.saveAndFlush(lead);

        int databaseSizeBeforeUpdate = leadRepository.findAll().size();

        // Update the lead using partial update
        Lead partialUpdatedLead = new Lead();
        partialUpdatedLead.setId(lead.getId());

        partialUpdatedLead.name(UPDATED_NAME).leadNumber(UPDATED_LEAD_NUMBER).annualRevenue(UPDATED_ANNUAL_REVENUE);

        restLeadMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedLead.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedLead))
            )
            .andExpect(status().isOk());

        // Validate the Lead in the database
        List<Lead> leadList = leadRepository.findAll();
        assertThat(leadList).hasSize(databaseSizeBeforeUpdate);
        Lead testLead = leadList.get(leadList.size() - 1);
        assertThat(testLead.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testLead.getLeadNumber()).isEqualTo(UPDATED_LEAD_NUMBER);
        assertThat(testLead.getAnnualRevenue()).isEqualTo(UPDATED_ANNUAL_REVENUE);
    }

    @Test
    @Transactional
    void fullUpdateLeadWithPatch() throws Exception {
        // Initialize the database
        leadRepository.saveAndFlush(lead);

        int databaseSizeBeforeUpdate = leadRepository.findAll().size();

        // Update the lead using partial update
        Lead partialUpdatedLead = new Lead();
        partialUpdatedLead.setId(lead.getId());

        partialUpdatedLead.name(UPDATED_NAME).leadNumber(UPDATED_LEAD_NUMBER).annualRevenue(UPDATED_ANNUAL_REVENUE);

        restLeadMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedLead.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedLead))
            )
            .andExpect(status().isOk());

        // Validate the Lead in the database
        List<Lead> leadList = leadRepository.findAll();
        assertThat(leadList).hasSize(databaseSizeBeforeUpdate);
        Lead testLead = leadList.get(leadList.size() - 1);
        assertThat(testLead.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testLead.getLeadNumber()).isEqualTo(UPDATED_LEAD_NUMBER);
        assertThat(testLead.getAnnualRevenue()).isEqualTo(UPDATED_ANNUAL_REVENUE);
    }

    @Test
    @Transactional
    void patchNonExistingLead() throws Exception {
        int databaseSizeBeforeUpdate = leadRepository.findAll().size();
        lead.setId(longCount.incrementAndGet());

        // Create the Lead
        LeadDTO leadDTO = leadMapper.toDto(lead);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restLeadMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, leadDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(leadDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Lead in the database
        List<Lead> leadList = leadRepository.findAll();
        assertThat(leadList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchLead() throws Exception {
        int databaseSizeBeforeUpdate = leadRepository.findAll().size();
        lead.setId(longCount.incrementAndGet());

        // Create the Lead
        LeadDTO leadDTO = leadMapper.toDto(lead);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restLeadMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(leadDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Lead in the database
        List<Lead> leadList = leadRepository.findAll();
        assertThat(leadList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamLead() throws Exception {
        int databaseSizeBeforeUpdate = leadRepository.findAll().size();
        lead.setId(longCount.incrementAndGet());

        // Create the Lead
        LeadDTO leadDTO = leadMapper.toDto(lead);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restLeadMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(leadDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Lead in the database
        List<Lead> leadList = leadRepository.findAll();
        assertThat(leadList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteLead() throws Exception {
        // Initialize the database
        leadRepository.saveAndFlush(lead);

        int databaseSizeBeforeDelete = leadRepository.findAll().size();

        // Delete the lead
        restLeadMockMvc
            .perform(delete(ENTITY_API_URL_ID, lead.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Lead> leadList = leadRepository.findAll();
        assertThat(leadList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
