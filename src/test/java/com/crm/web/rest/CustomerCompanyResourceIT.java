package com.crm.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.crm.IntegrationTest;
import com.crm.domain.CustomerCompany;
import com.crm.repository.CustomerCompanyRepository;
import com.crm.service.dto.CustomerCompanyDTO;
import com.crm.service.mapper.CustomerCompanyMapper;
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
 * Integration tests for the {@link CustomerCompanyResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class CustomerCompanyResourceIT {

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_CODE = "AAAAAAAAAA";
    private static final String UPDATED_CODE = "BBBBBBBBBB";

    private static final String DEFAULT_DESCRIPTION = "AAAAAAAAAA";
    private static final String UPDATED_DESCRIPTION = "BBBBBBBBBB";

    private static final String DEFAULT_WEBSITE = "AAAAAAAAAA";
    private static final String UPDATED_WEBSITE = "BBBBBBBBBB";

    private static final String DEFAULT_REGISTRATION_NUMBER = "AAAAAAAAAA";
    private static final String UPDATED_REGISTRATION_NUMBER = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/customer-companies";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private CustomerCompanyRepository customerCompanyRepository;

    @Autowired
    private CustomerCompanyMapper customerCompanyMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restCustomerCompanyMockMvc;

    private CustomerCompany customerCompany;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static CustomerCompany createEntity(EntityManager em) {
        CustomerCompany customerCompany = new CustomerCompany()
            .name(DEFAULT_NAME)
            .code(DEFAULT_CODE)
            .description(DEFAULT_DESCRIPTION)
            .website(DEFAULT_WEBSITE)
            .registrationNumber(DEFAULT_REGISTRATION_NUMBER);
        return customerCompany;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static CustomerCompany createUpdatedEntity(EntityManager em) {
        CustomerCompany customerCompany = new CustomerCompany()
            .name(UPDATED_NAME)
            .code(UPDATED_CODE)
            .description(UPDATED_DESCRIPTION)
            .website(UPDATED_WEBSITE)
            .registrationNumber(UPDATED_REGISTRATION_NUMBER);
        return customerCompany;
    }

    @BeforeEach
    public void initTest() {
        customerCompany = createEntity(em);
    }

    @Test
    @Transactional
    void createCustomerCompany() throws Exception {
        int databaseSizeBeforeCreate = customerCompanyRepository.findAll().size();
        // Create the CustomerCompany
        CustomerCompanyDTO customerCompanyDTO = customerCompanyMapper.toDto(customerCompany);
        restCustomerCompanyMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(customerCompanyDTO))
            )
            .andExpect(status().isCreated());

        // Validate the CustomerCompany in the database
        List<CustomerCompany> customerCompanyList = customerCompanyRepository.findAll();
        assertThat(customerCompanyList).hasSize(databaseSizeBeforeCreate + 1);
        CustomerCompany testCustomerCompany = customerCompanyList.get(customerCompanyList.size() - 1);
        assertThat(testCustomerCompany.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testCustomerCompany.getCode()).isEqualTo(DEFAULT_CODE);
        assertThat(testCustomerCompany.getDescription()).isEqualTo(DEFAULT_DESCRIPTION);
        assertThat(testCustomerCompany.getWebsite()).isEqualTo(DEFAULT_WEBSITE);
        assertThat(testCustomerCompany.getRegistrationNumber()).isEqualTo(DEFAULT_REGISTRATION_NUMBER);
    }

    @Test
    @Transactional
    void createCustomerCompanyWithExistingId() throws Exception {
        // Create the CustomerCompany with an existing ID
        customerCompany.setId(1L);
        CustomerCompanyDTO customerCompanyDTO = customerCompanyMapper.toDto(customerCompany);

        int databaseSizeBeforeCreate = customerCompanyRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restCustomerCompanyMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(customerCompanyDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the CustomerCompany in the database
        List<CustomerCompany> customerCompanyList = customerCompanyRepository.findAll();
        assertThat(customerCompanyList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkNameIsRequired() throws Exception {
        int databaseSizeBeforeTest = customerCompanyRepository.findAll().size();
        // set the field null
        customerCompany.setName(null);

        // Create the CustomerCompany, which fails.
        CustomerCompanyDTO customerCompanyDTO = customerCompanyMapper.toDto(customerCompany);

        restCustomerCompanyMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(customerCompanyDTO))
            )
            .andExpect(status().isBadRequest());

        List<CustomerCompany> customerCompanyList = customerCompanyRepository.findAll();
        assertThat(customerCompanyList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllCustomerCompanies() throws Exception {
        // Initialize the database
        customerCompanyRepository.saveAndFlush(customerCompany);

        // Get all the customerCompanyList
        restCustomerCompanyMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(customerCompany.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)))
            .andExpect(jsonPath("$.[*].code").value(hasItem(DEFAULT_CODE)))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION)))
            .andExpect(jsonPath("$.[*].website").value(hasItem(DEFAULT_WEBSITE)))
            .andExpect(jsonPath("$.[*].registrationNumber").value(hasItem(DEFAULT_REGISTRATION_NUMBER)));
    }

    @Test
    @Transactional
    void getCustomerCompany() throws Exception {
        // Initialize the database
        customerCompanyRepository.saveAndFlush(customerCompany);

        // Get the customerCompany
        restCustomerCompanyMockMvc
            .perform(get(ENTITY_API_URL_ID, customerCompany.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(customerCompany.getId().intValue()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME))
            .andExpect(jsonPath("$.code").value(DEFAULT_CODE))
            .andExpect(jsonPath("$.description").value(DEFAULT_DESCRIPTION))
            .andExpect(jsonPath("$.website").value(DEFAULT_WEBSITE))
            .andExpect(jsonPath("$.registrationNumber").value(DEFAULT_REGISTRATION_NUMBER));
    }

    @Test
    @Transactional
    void getCustomerCompaniesByIdFiltering() throws Exception {
        // Initialize the database
        customerCompanyRepository.saveAndFlush(customerCompany);

        Long id = customerCompany.getId();

        defaultCustomerCompanyShouldBeFound("id.equals=" + id);
        defaultCustomerCompanyShouldNotBeFound("id.notEquals=" + id);

        defaultCustomerCompanyShouldBeFound("id.greaterThanOrEqual=" + id);
        defaultCustomerCompanyShouldNotBeFound("id.greaterThan=" + id);

        defaultCustomerCompanyShouldBeFound("id.lessThanOrEqual=" + id);
        defaultCustomerCompanyShouldNotBeFound("id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllCustomerCompaniesByNameIsEqualToSomething() throws Exception {
        // Initialize the database
        customerCompanyRepository.saveAndFlush(customerCompany);

        // Get all the customerCompanyList where name equals to DEFAULT_NAME
        defaultCustomerCompanyShouldBeFound("name.equals=" + DEFAULT_NAME);

        // Get all the customerCompanyList where name equals to UPDATED_NAME
        defaultCustomerCompanyShouldNotBeFound("name.equals=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllCustomerCompaniesByNameIsInShouldWork() throws Exception {
        // Initialize the database
        customerCompanyRepository.saveAndFlush(customerCompany);

        // Get all the customerCompanyList where name in DEFAULT_NAME or UPDATED_NAME
        defaultCustomerCompanyShouldBeFound("name.in=" + DEFAULT_NAME + "," + UPDATED_NAME);

        // Get all the customerCompanyList where name equals to UPDATED_NAME
        defaultCustomerCompanyShouldNotBeFound("name.in=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllCustomerCompaniesByNameIsNullOrNotNull() throws Exception {
        // Initialize the database
        customerCompanyRepository.saveAndFlush(customerCompany);

        // Get all the customerCompanyList where name is not null
        defaultCustomerCompanyShouldBeFound("name.specified=true");

        // Get all the customerCompanyList where name is null
        defaultCustomerCompanyShouldNotBeFound("name.specified=false");
    }

    @Test
    @Transactional
    void getAllCustomerCompaniesByNameContainsSomething() throws Exception {
        // Initialize the database
        customerCompanyRepository.saveAndFlush(customerCompany);

        // Get all the customerCompanyList where name contains DEFAULT_NAME
        defaultCustomerCompanyShouldBeFound("name.contains=" + DEFAULT_NAME);

        // Get all the customerCompanyList where name contains UPDATED_NAME
        defaultCustomerCompanyShouldNotBeFound("name.contains=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllCustomerCompaniesByNameNotContainsSomething() throws Exception {
        // Initialize the database
        customerCompanyRepository.saveAndFlush(customerCompany);

        // Get all the customerCompanyList where name does not contain DEFAULT_NAME
        defaultCustomerCompanyShouldNotBeFound("name.doesNotContain=" + DEFAULT_NAME);

        // Get all the customerCompanyList where name does not contain UPDATED_NAME
        defaultCustomerCompanyShouldBeFound("name.doesNotContain=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllCustomerCompaniesByCodeIsEqualToSomething() throws Exception {
        // Initialize the database
        customerCompanyRepository.saveAndFlush(customerCompany);

        // Get all the customerCompanyList where code equals to DEFAULT_CODE
        defaultCustomerCompanyShouldBeFound("code.equals=" + DEFAULT_CODE);

        // Get all the customerCompanyList where code equals to UPDATED_CODE
        defaultCustomerCompanyShouldNotBeFound("code.equals=" + UPDATED_CODE);
    }

    @Test
    @Transactional
    void getAllCustomerCompaniesByCodeIsInShouldWork() throws Exception {
        // Initialize the database
        customerCompanyRepository.saveAndFlush(customerCompany);

        // Get all the customerCompanyList where code in DEFAULT_CODE or UPDATED_CODE
        defaultCustomerCompanyShouldBeFound("code.in=" + DEFAULT_CODE + "," + UPDATED_CODE);

        // Get all the customerCompanyList where code equals to UPDATED_CODE
        defaultCustomerCompanyShouldNotBeFound("code.in=" + UPDATED_CODE);
    }

    @Test
    @Transactional
    void getAllCustomerCompaniesByCodeIsNullOrNotNull() throws Exception {
        // Initialize the database
        customerCompanyRepository.saveAndFlush(customerCompany);

        // Get all the customerCompanyList where code is not null
        defaultCustomerCompanyShouldBeFound("code.specified=true");

        // Get all the customerCompanyList where code is null
        defaultCustomerCompanyShouldNotBeFound("code.specified=false");
    }

    @Test
    @Transactional
    void getAllCustomerCompaniesByCodeContainsSomething() throws Exception {
        // Initialize the database
        customerCompanyRepository.saveAndFlush(customerCompany);

        // Get all the customerCompanyList where code contains DEFAULT_CODE
        defaultCustomerCompanyShouldBeFound("code.contains=" + DEFAULT_CODE);

        // Get all the customerCompanyList where code contains UPDATED_CODE
        defaultCustomerCompanyShouldNotBeFound("code.contains=" + UPDATED_CODE);
    }

    @Test
    @Transactional
    void getAllCustomerCompaniesByCodeNotContainsSomething() throws Exception {
        // Initialize the database
        customerCompanyRepository.saveAndFlush(customerCompany);

        // Get all the customerCompanyList where code does not contain DEFAULT_CODE
        defaultCustomerCompanyShouldNotBeFound("code.doesNotContain=" + DEFAULT_CODE);

        // Get all the customerCompanyList where code does not contain UPDATED_CODE
        defaultCustomerCompanyShouldBeFound("code.doesNotContain=" + UPDATED_CODE);
    }

    @Test
    @Transactional
    void getAllCustomerCompaniesByDescriptionIsEqualToSomething() throws Exception {
        // Initialize the database
        customerCompanyRepository.saveAndFlush(customerCompany);

        // Get all the customerCompanyList where description equals to DEFAULT_DESCRIPTION
        defaultCustomerCompanyShouldBeFound("description.equals=" + DEFAULT_DESCRIPTION);

        // Get all the customerCompanyList where description equals to UPDATED_DESCRIPTION
        defaultCustomerCompanyShouldNotBeFound("description.equals=" + UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    void getAllCustomerCompaniesByDescriptionIsInShouldWork() throws Exception {
        // Initialize the database
        customerCompanyRepository.saveAndFlush(customerCompany);

        // Get all the customerCompanyList where description in DEFAULT_DESCRIPTION or UPDATED_DESCRIPTION
        defaultCustomerCompanyShouldBeFound("description.in=" + DEFAULT_DESCRIPTION + "," + UPDATED_DESCRIPTION);

        // Get all the customerCompanyList where description equals to UPDATED_DESCRIPTION
        defaultCustomerCompanyShouldNotBeFound("description.in=" + UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    void getAllCustomerCompaniesByDescriptionIsNullOrNotNull() throws Exception {
        // Initialize the database
        customerCompanyRepository.saveAndFlush(customerCompany);

        // Get all the customerCompanyList where description is not null
        defaultCustomerCompanyShouldBeFound("description.specified=true");

        // Get all the customerCompanyList where description is null
        defaultCustomerCompanyShouldNotBeFound("description.specified=false");
    }

    @Test
    @Transactional
    void getAllCustomerCompaniesByDescriptionContainsSomething() throws Exception {
        // Initialize the database
        customerCompanyRepository.saveAndFlush(customerCompany);

        // Get all the customerCompanyList where description contains DEFAULT_DESCRIPTION
        defaultCustomerCompanyShouldBeFound("description.contains=" + DEFAULT_DESCRIPTION);

        // Get all the customerCompanyList where description contains UPDATED_DESCRIPTION
        defaultCustomerCompanyShouldNotBeFound("description.contains=" + UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    void getAllCustomerCompaniesByDescriptionNotContainsSomething() throws Exception {
        // Initialize the database
        customerCompanyRepository.saveAndFlush(customerCompany);

        // Get all the customerCompanyList where description does not contain DEFAULT_DESCRIPTION
        defaultCustomerCompanyShouldNotBeFound("description.doesNotContain=" + DEFAULT_DESCRIPTION);

        // Get all the customerCompanyList where description does not contain UPDATED_DESCRIPTION
        defaultCustomerCompanyShouldBeFound("description.doesNotContain=" + UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    void getAllCustomerCompaniesByWebsiteIsEqualToSomething() throws Exception {
        // Initialize the database
        customerCompanyRepository.saveAndFlush(customerCompany);

        // Get all the customerCompanyList where website equals to DEFAULT_WEBSITE
        defaultCustomerCompanyShouldBeFound("website.equals=" + DEFAULT_WEBSITE);

        // Get all the customerCompanyList where website equals to UPDATED_WEBSITE
        defaultCustomerCompanyShouldNotBeFound("website.equals=" + UPDATED_WEBSITE);
    }

    @Test
    @Transactional
    void getAllCustomerCompaniesByWebsiteIsInShouldWork() throws Exception {
        // Initialize the database
        customerCompanyRepository.saveAndFlush(customerCompany);

        // Get all the customerCompanyList where website in DEFAULT_WEBSITE or UPDATED_WEBSITE
        defaultCustomerCompanyShouldBeFound("website.in=" + DEFAULT_WEBSITE + "," + UPDATED_WEBSITE);

        // Get all the customerCompanyList where website equals to UPDATED_WEBSITE
        defaultCustomerCompanyShouldNotBeFound("website.in=" + UPDATED_WEBSITE);
    }

    @Test
    @Transactional
    void getAllCustomerCompaniesByWebsiteIsNullOrNotNull() throws Exception {
        // Initialize the database
        customerCompanyRepository.saveAndFlush(customerCompany);

        // Get all the customerCompanyList where website is not null
        defaultCustomerCompanyShouldBeFound("website.specified=true");

        // Get all the customerCompanyList where website is null
        defaultCustomerCompanyShouldNotBeFound("website.specified=false");
    }

    @Test
    @Transactional
    void getAllCustomerCompaniesByWebsiteContainsSomething() throws Exception {
        // Initialize the database
        customerCompanyRepository.saveAndFlush(customerCompany);

        // Get all the customerCompanyList where website contains DEFAULT_WEBSITE
        defaultCustomerCompanyShouldBeFound("website.contains=" + DEFAULT_WEBSITE);

        // Get all the customerCompanyList where website contains UPDATED_WEBSITE
        defaultCustomerCompanyShouldNotBeFound("website.contains=" + UPDATED_WEBSITE);
    }

    @Test
    @Transactional
    void getAllCustomerCompaniesByWebsiteNotContainsSomething() throws Exception {
        // Initialize the database
        customerCompanyRepository.saveAndFlush(customerCompany);

        // Get all the customerCompanyList where website does not contain DEFAULT_WEBSITE
        defaultCustomerCompanyShouldNotBeFound("website.doesNotContain=" + DEFAULT_WEBSITE);

        // Get all the customerCompanyList where website does not contain UPDATED_WEBSITE
        defaultCustomerCompanyShouldBeFound("website.doesNotContain=" + UPDATED_WEBSITE);
    }

    @Test
    @Transactional
    void getAllCustomerCompaniesByRegistrationNumberIsEqualToSomething() throws Exception {
        // Initialize the database
        customerCompanyRepository.saveAndFlush(customerCompany);

        // Get all the customerCompanyList where registrationNumber equals to DEFAULT_REGISTRATION_NUMBER
        defaultCustomerCompanyShouldBeFound("registrationNumber.equals=" + DEFAULT_REGISTRATION_NUMBER);

        // Get all the customerCompanyList where registrationNumber equals to UPDATED_REGISTRATION_NUMBER
        defaultCustomerCompanyShouldNotBeFound("registrationNumber.equals=" + UPDATED_REGISTRATION_NUMBER);
    }

    @Test
    @Transactional
    void getAllCustomerCompaniesByRegistrationNumberIsInShouldWork() throws Exception {
        // Initialize the database
        customerCompanyRepository.saveAndFlush(customerCompany);

        // Get all the customerCompanyList where registrationNumber in DEFAULT_REGISTRATION_NUMBER or UPDATED_REGISTRATION_NUMBER
        defaultCustomerCompanyShouldBeFound("registrationNumber.in=" + DEFAULT_REGISTRATION_NUMBER + "," + UPDATED_REGISTRATION_NUMBER);

        // Get all the customerCompanyList where registrationNumber equals to UPDATED_REGISTRATION_NUMBER
        defaultCustomerCompanyShouldNotBeFound("registrationNumber.in=" + UPDATED_REGISTRATION_NUMBER);
    }

    @Test
    @Transactional
    void getAllCustomerCompaniesByRegistrationNumberIsNullOrNotNull() throws Exception {
        // Initialize the database
        customerCompanyRepository.saveAndFlush(customerCompany);

        // Get all the customerCompanyList where registrationNumber is not null
        defaultCustomerCompanyShouldBeFound("registrationNumber.specified=true");

        // Get all the customerCompanyList where registrationNumber is null
        defaultCustomerCompanyShouldNotBeFound("registrationNumber.specified=false");
    }

    @Test
    @Transactional
    void getAllCustomerCompaniesByRegistrationNumberContainsSomething() throws Exception {
        // Initialize the database
        customerCompanyRepository.saveAndFlush(customerCompany);

        // Get all the customerCompanyList where registrationNumber contains DEFAULT_REGISTRATION_NUMBER
        defaultCustomerCompanyShouldBeFound("registrationNumber.contains=" + DEFAULT_REGISTRATION_NUMBER);

        // Get all the customerCompanyList where registrationNumber contains UPDATED_REGISTRATION_NUMBER
        defaultCustomerCompanyShouldNotBeFound("registrationNumber.contains=" + UPDATED_REGISTRATION_NUMBER);
    }

    @Test
    @Transactional
    void getAllCustomerCompaniesByRegistrationNumberNotContainsSomething() throws Exception {
        // Initialize the database
        customerCompanyRepository.saveAndFlush(customerCompany);

        // Get all the customerCompanyList where registrationNumber does not contain DEFAULT_REGISTRATION_NUMBER
        defaultCustomerCompanyShouldNotBeFound("registrationNumber.doesNotContain=" + DEFAULT_REGISTRATION_NUMBER);

        // Get all the customerCompanyList where registrationNumber does not contain UPDATED_REGISTRATION_NUMBER
        defaultCustomerCompanyShouldBeFound("registrationNumber.doesNotContain=" + UPDATED_REGISTRATION_NUMBER);
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultCustomerCompanyShouldBeFound(String filter) throws Exception {
        restCustomerCompanyMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(customerCompany.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)))
            .andExpect(jsonPath("$.[*].code").value(hasItem(DEFAULT_CODE)))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION)))
            .andExpect(jsonPath("$.[*].website").value(hasItem(DEFAULT_WEBSITE)))
            .andExpect(jsonPath("$.[*].registrationNumber").value(hasItem(DEFAULT_REGISTRATION_NUMBER)));

        // Check, that the count call also returns 1
        restCustomerCompanyMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultCustomerCompanyShouldNotBeFound(String filter) throws Exception {
        restCustomerCompanyMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restCustomerCompanyMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingCustomerCompany() throws Exception {
        // Get the customerCompany
        restCustomerCompanyMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingCustomerCompany() throws Exception {
        // Initialize the database
        customerCompanyRepository.saveAndFlush(customerCompany);

        int databaseSizeBeforeUpdate = customerCompanyRepository.findAll().size();

        // Update the customerCompany
        CustomerCompany updatedCustomerCompany = customerCompanyRepository.findById(customerCompany.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedCustomerCompany are not directly saved in db
        em.detach(updatedCustomerCompany);
        updatedCustomerCompany
            .name(UPDATED_NAME)
            .code(UPDATED_CODE)
            .description(UPDATED_DESCRIPTION)
            .website(UPDATED_WEBSITE)
            .registrationNumber(UPDATED_REGISTRATION_NUMBER);
        CustomerCompanyDTO customerCompanyDTO = customerCompanyMapper.toDto(updatedCustomerCompany);

        restCustomerCompanyMockMvc
            .perform(
                put(ENTITY_API_URL_ID, customerCompanyDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(customerCompanyDTO))
            )
            .andExpect(status().isOk());

        // Validate the CustomerCompany in the database
        List<CustomerCompany> customerCompanyList = customerCompanyRepository.findAll();
        assertThat(customerCompanyList).hasSize(databaseSizeBeforeUpdate);
        CustomerCompany testCustomerCompany = customerCompanyList.get(customerCompanyList.size() - 1);
        assertThat(testCustomerCompany.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testCustomerCompany.getCode()).isEqualTo(UPDATED_CODE);
        assertThat(testCustomerCompany.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
        assertThat(testCustomerCompany.getWebsite()).isEqualTo(UPDATED_WEBSITE);
        assertThat(testCustomerCompany.getRegistrationNumber()).isEqualTo(UPDATED_REGISTRATION_NUMBER);
    }

    @Test
    @Transactional
    void putNonExistingCustomerCompany() throws Exception {
        int databaseSizeBeforeUpdate = customerCompanyRepository.findAll().size();
        customerCompany.setId(longCount.incrementAndGet());

        // Create the CustomerCompany
        CustomerCompanyDTO customerCompanyDTO = customerCompanyMapper.toDto(customerCompany);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restCustomerCompanyMockMvc
            .perform(
                put(ENTITY_API_URL_ID, customerCompanyDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(customerCompanyDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the CustomerCompany in the database
        List<CustomerCompany> customerCompanyList = customerCompanyRepository.findAll();
        assertThat(customerCompanyList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchCustomerCompany() throws Exception {
        int databaseSizeBeforeUpdate = customerCompanyRepository.findAll().size();
        customerCompany.setId(longCount.incrementAndGet());

        // Create the CustomerCompany
        CustomerCompanyDTO customerCompanyDTO = customerCompanyMapper.toDto(customerCompany);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCustomerCompanyMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(customerCompanyDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the CustomerCompany in the database
        List<CustomerCompany> customerCompanyList = customerCompanyRepository.findAll();
        assertThat(customerCompanyList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamCustomerCompany() throws Exception {
        int databaseSizeBeforeUpdate = customerCompanyRepository.findAll().size();
        customerCompany.setId(longCount.incrementAndGet());

        // Create the CustomerCompany
        CustomerCompanyDTO customerCompanyDTO = customerCompanyMapper.toDto(customerCompany);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCustomerCompanyMockMvc
            .perform(
                put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(customerCompanyDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the CustomerCompany in the database
        List<CustomerCompany> customerCompanyList = customerCompanyRepository.findAll();
        assertThat(customerCompanyList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateCustomerCompanyWithPatch() throws Exception {
        // Initialize the database
        customerCompanyRepository.saveAndFlush(customerCompany);

        int databaseSizeBeforeUpdate = customerCompanyRepository.findAll().size();

        // Update the customerCompany using partial update
        CustomerCompany partialUpdatedCustomerCompany = new CustomerCompany();
        partialUpdatedCustomerCompany.setId(customerCompany.getId());

        partialUpdatedCustomerCompany.name(UPDATED_NAME).code(UPDATED_CODE).website(UPDATED_WEBSITE);

        restCustomerCompanyMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedCustomerCompany.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedCustomerCompany))
            )
            .andExpect(status().isOk());

        // Validate the CustomerCompany in the database
        List<CustomerCompany> customerCompanyList = customerCompanyRepository.findAll();
        assertThat(customerCompanyList).hasSize(databaseSizeBeforeUpdate);
        CustomerCompany testCustomerCompany = customerCompanyList.get(customerCompanyList.size() - 1);
        assertThat(testCustomerCompany.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testCustomerCompany.getCode()).isEqualTo(UPDATED_CODE);
        assertThat(testCustomerCompany.getDescription()).isEqualTo(DEFAULT_DESCRIPTION);
        assertThat(testCustomerCompany.getWebsite()).isEqualTo(UPDATED_WEBSITE);
        assertThat(testCustomerCompany.getRegistrationNumber()).isEqualTo(DEFAULT_REGISTRATION_NUMBER);
    }

    @Test
    @Transactional
    void fullUpdateCustomerCompanyWithPatch() throws Exception {
        // Initialize the database
        customerCompanyRepository.saveAndFlush(customerCompany);

        int databaseSizeBeforeUpdate = customerCompanyRepository.findAll().size();

        // Update the customerCompany using partial update
        CustomerCompany partialUpdatedCustomerCompany = new CustomerCompany();
        partialUpdatedCustomerCompany.setId(customerCompany.getId());

        partialUpdatedCustomerCompany
            .name(UPDATED_NAME)
            .code(UPDATED_CODE)
            .description(UPDATED_DESCRIPTION)
            .website(UPDATED_WEBSITE)
            .registrationNumber(UPDATED_REGISTRATION_NUMBER);

        restCustomerCompanyMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedCustomerCompany.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedCustomerCompany))
            )
            .andExpect(status().isOk());

        // Validate the CustomerCompany in the database
        List<CustomerCompany> customerCompanyList = customerCompanyRepository.findAll();
        assertThat(customerCompanyList).hasSize(databaseSizeBeforeUpdate);
        CustomerCompany testCustomerCompany = customerCompanyList.get(customerCompanyList.size() - 1);
        assertThat(testCustomerCompany.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testCustomerCompany.getCode()).isEqualTo(UPDATED_CODE);
        assertThat(testCustomerCompany.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
        assertThat(testCustomerCompany.getWebsite()).isEqualTo(UPDATED_WEBSITE);
        assertThat(testCustomerCompany.getRegistrationNumber()).isEqualTo(UPDATED_REGISTRATION_NUMBER);
    }

    @Test
    @Transactional
    void patchNonExistingCustomerCompany() throws Exception {
        int databaseSizeBeforeUpdate = customerCompanyRepository.findAll().size();
        customerCompany.setId(longCount.incrementAndGet());

        // Create the CustomerCompany
        CustomerCompanyDTO customerCompanyDTO = customerCompanyMapper.toDto(customerCompany);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restCustomerCompanyMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, customerCompanyDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(customerCompanyDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the CustomerCompany in the database
        List<CustomerCompany> customerCompanyList = customerCompanyRepository.findAll();
        assertThat(customerCompanyList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchCustomerCompany() throws Exception {
        int databaseSizeBeforeUpdate = customerCompanyRepository.findAll().size();
        customerCompany.setId(longCount.incrementAndGet());

        // Create the CustomerCompany
        CustomerCompanyDTO customerCompanyDTO = customerCompanyMapper.toDto(customerCompany);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCustomerCompanyMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(customerCompanyDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the CustomerCompany in the database
        List<CustomerCompany> customerCompanyList = customerCompanyRepository.findAll();
        assertThat(customerCompanyList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamCustomerCompany() throws Exception {
        int databaseSizeBeforeUpdate = customerCompanyRepository.findAll().size();
        customerCompany.setId(longCount.incrementAndGet());

        // Create the CustomerCompany
        CustomerCompanyDTO customerCompanyDTO = customerCompanyMapper.toDto(customerCompany);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCustomerCompanyMockMvc
            .perform(
                patch(ENTITY_API_URL)
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(customerCompanyDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the CustomerCompany in the database
        List<CustomerCompany> customerCompanyList = customerCompanyRepository.findAll();
        assertThat(customerCompanyList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteCustomerCompany() throws Exception {
        // Initialize the database
        customerCompanyRepository.saveAndFlush(customerCompany);

        int databaseSizeBeforeDelete = customerCompanyRepository.findAll().size();

        // Delete the customerCompany
        restCustomerCompanyMockMvc
            .perform(delete(ENTITY_API_URL_ID, customerCompany.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<CustomerCompany> customerCompanyList = customerCompanyRepository.findAll();
        assertThat(customerCompanyList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
