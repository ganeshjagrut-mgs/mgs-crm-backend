package com.crm.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.crm.IntegrationTest;
import com.crm.domain.Address;
import com.crm.domain.Encryption;
import com.crm.domain.Tenant;
import com.crm.domain.User;
import com.crm.repository.TenantRepository;
import com.crm.service.TenantService;
import com.crm.service.dto.TenantDTO;
import com.crm.service.mapper.TenantMapper;
import jakarta.persistence.EntityManager;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.atomic.AtomicLong;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

/**
 * Integration tests for the {@link TenantResource} REST controller.
 */
@IntegrationTest
@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc
@WithMockUser
class TenantResourceIT {

    private static final String DEFAULT_COMPANY_NAME = "AAAAAAAAAA";
    private static final String UPDATED_COMPANY_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_CONTACT_PERSON = "AAAAAAAAAA";
    private static final String UPDATED_CONTACT_PERSON = "BBBBBBBBBB";

    private static final String DEFAULT_LOGO = "AAAAAAAAAA";
    private static final String UPDATED_LOGO = "BBBBBBBBBB";

    private static final String DEFAULT_WEBSITE = "AAAAAAAAAA";
    private static final String UPDATED_WEBSITE = "BBBBBBBBBB";

    private static final String DEFAULT_REGISTRATION_NUMBER = "AAAAAAAAAA";
    private static final String UPDATED_REGISTRATION_NUMBER = "BBBBBBBBBB";

    private static final Integer DEFAULT_SUB_ID = 1;
    private static final Integer UPDATED_SUB_ID = 2;
    private static final Integer SMALLER_SUB_ID = 1 - 1;

    private static final String ENTITY_API_URL = "/api/tenants";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private TenantRepository tenantRepository;

    @Mock
    private TenantRepository tenantRepositoryMock;

    @Autowired
    private TenantMapper tenantMapper;

    @Mock
    private TenantService tenantServiceMock;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restTenantMockMvc;

    private Tenant tenant;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Tenant createEntity(EntityManager em) {
        Tenant tenant = new Tenant()
            .companyName(DEFAULT_COMPANY_NAME)
            .contactPerson(DEFAULT_CONTACT_PERSON)
            .logo(DEFAULT_LOGO)
            .website(DEFAULT_WEBSITE)
            .registrationNumber(DEFAULT_REGISTRATION_NUMBER)
            .subId(DEFAULT_SUB_ID);
        return tenant;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Tenant createUpdatedEntity(EntityManager em) {
        Tenant tenant = new Tenant()
            .companyName(UPDATED_COMPANY_NAME)
            .contactPerson(UPDATED_CONTACT_PERSON)
            .logo(UPDATED_LOGO)
            .website(UPDATED_WEBSITE)
            .registrationNumber(UPDATED_REGISTRATION_NUMBER)
            .subId(UPDATED_SUB_ID);
        return tenant;
    }

    @BeforeEach
    public void initTest() {
        tenant = createEntity(em);
    }

    @Test
    @Transactional
    void createTenant() throws Exception {
        int databaseSizeBeforeCreate = tenantRepository.findAll().size();
        // Create the Tenant
        TenantDTO tenantDTO = tenantMapper.toDto(tenant);
        restTenantMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(tenantDTO)))
            .andExpect(status().isCreated());

        // Validate the Tenant in the database
        List<Tenant> tenantList = tenantRepository.findAll();
        assertThat(tenantList).hasSize(databaseSizeBeforeCreate + 1);
        Tenant testTenant = tenantList.get(tenantList.size() - 1);
        assertThat(testTenant.getCompanyName()).isEqualTo(DEFAULT_COMPANY_NAME);
        assertThat(testTenant.getContactPerson()).isEqualTo(DEFAULT_CONTACT_PERSON);
        assertThat(testTenant.getLogo()).isEqualTo(DEFAULT_LOGO);
        assertThat(testTenant.getWebsite()).isEqualTo(DEFAULT_WEBSITE);
        assertThat(testTenant.getRegistrationNumber()).isEqualTo(DEFAULT_REGISTRATION_NUMBER);
        assertThat(testTenant.getSubId()).isEqualTo(DEFAULT_SUB_ID);
    }

    @Test
    @Transactional
    void createTenantWithExistingId() throws Exception {
        // Create the Tenant with an existing ID
        tenant.setId(1L);
        TenantDTO tenantDTO = tenantMapper.toDto(tenant);

        int databaseSizeBeforeCreate = tenantRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restTenantMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(tenantDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Tenant in the database
        List<Tenant> tenantList = tenantRepository.findAll();
        assertThat(tenantList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkCompanyNameIsRequired() throws Exception {
        int databaseSizeBeforeTest = tenantRepository.findAll().size();
        // set the field null
        tenant.setCompanyName(null);

        // Create the Tenant, which fails.
        TenantDTO tenantDTO = tenantMapper.toDto(tenant);

        restTenantMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(tenantDTO)))
            .andExpect(status().isBadRequest());

        List<Tenant> tenantList = tenantRepository.findAll();
        assertThat(tenantList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllTenants() throws Exception {
        // Initialize the database
        tenantRepository.saveAndFlush(tenant);

        // Get all the tenantList
        restTenantMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(tenant.getId().intValue())))
            .andExpect(jsonPath("$.[*].companyName").value(hasItem(DEFAULT_COMPANY_NAME)))
            .andExpect(jsonPath("$.[*].contactPerson").value(hasItem(DEFAULT_CONTACT_PERSON)))
            .andExpect(jsonPath("$.[*].logo").value(hasItem(DEFAULT_LOGO)))
            .andExpect(jsonPath("$.[*].website").value(hasItem(DEFAULT_WEBSITE)))
            .andExpect(jsonPath("$.[*].registrationNumber").value(hasItem(DEFAULT_REGISTRATION_NUMBER)))
            .andExpect(jsonPath("$.[*].subId").value(hasItem(DEFAULT_SUB_ID)));
    }

    @SuppressWarnings({ "unchecked" })
    void getAllTenantsWithEagerRelationshipsIsEnabled() throws Exception {
        when(tenantServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restTenantMockMvc.perform(get(ENTITY_API_URL + "?eagerload=true")).andExpect(status().isOk());

        verify(tenantServiceMock, times(1)).findAllWithEagerRelationships(any());
    }

    @SuppressWarnings({ "unchecked" })
    void getAllTenantsWithEagerRelationshipsIsNotEnabled() throws Exception {
        when(tenantServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restTenantMockMvc.perform(get(ENTITY_API_URL + "?eagerload=false")).andExpect(status().isOk());
        verify(tenantRepositoryMock, times(1)).findAll(any(Pageable.class));
    }

    @Test
    @Transactional
    void getTenant() throws Exception {
        // Initialize the database
        tenantRepository.saveAndFlush(tenant);

        // Get the tenant
        restTenantMockMvc
            .perform(get(ENTITY_API_URL_ID, tenant.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(tenant.getId().intValue()))
            .andExpect(jsonPath("$.companyName").value(DEFAULT_COMPANY_NAME))
            .andExpect(jsonPath("$.contactPerson").value(DEFAULT_CONTACT_PERSON))
            .andExpect(jsonPath("$.logo").value(DEFAULT_LOGO))
            .andExpect(jsonPath("$.website").value(DEFAULT_WEBSITE))
            .andExpect(jsonPath("$.registrationNumber").value(DEFAULT_REGISTRATION_NUMBER))
            .andExpect(jsonPath("$.subId").value(DEFAULT_SUB_ID));
    }

    @Test
    @Transactional
    void getTenantsByIdFiltering() throws Exception {
        // Initialize the database
        tenantRepository.saveAndFlush(tenant);

        Long id = tenant.getId();

        defaultTenantShouldBeFound("id.equals=" + id);
        defaultTenantShouldNotBeFound("id.notEquals=" + id);

        defaultTenantShouldBeFound("id.greaterThanOrEqual=" + id);
        defaultTenantShouldNotBeFound("id.greaterThan=" + id);

        defaultTenantShouldBeFound("id.lessThanOrEqual=" + id);
        defaultTenantShouldNotBeFound("id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllTenantsByCompanyNameIsEqualToSomething() throws Exception {
        // Initialize the database
        tenantRepository.saveAndFlush(tenant);

        // Get all the tenantList where companyName equals to DEFAULT_COMPANY_NAME
        defaultTenantShouldBeFound("companyName.equals=" + DEFAULT_COMPANY_NAME);

        // Get all the tenantList where companyName equals to UPDATED_COMPANY_NAME
        defaultTenantShouldNotBeFound("companyName.equals=" + UPDATED_COMPANY_NAME);
    }

    @Test
    @Transactional
    void getAllTenantsByCompanyNameIsInShouldWork() throws Exception {
        // Initialize the database
        tenantRepository.saveAndFlush(tenant);

        // Get all the tenantList where companyName in DEFAULT_COMPANY_NAME or UPDATED_COMPANY_NAME
        defaultTenantShouldBeFound("companyName.in=" + DEFAULT_COMPANY_NAME + "," + UPDATED_COMPANY_NAME);

        // Get all the tenantList where companyName equals to UPDATED_COMPANY_NAME
        defaultTenantShouldNotBeFound("companyName.in=" + UPDATED_COMPANY_NAME);
    }

    @Test
    @Transactional
    void getAllTenantsByCompanyNameIsNullOrNotNull() throws Exception {
        // Initialize the database
        tenantRepository.saveAndFlush(tenant);

        // Get all the tenantList where companyName is not null
        defaultTenantShouldBeFound("companyName.specified=true");

        // Get all the tenantList where companyName is null
        defaultTenantShouldNotBeFound("companyName.specified=false");
    }

    @Test
    @Transactional
    void getAllTenantsByCompanyNameContainsSomething() throws Exception {
        // Initialize the database
        tenantRepository.saveAndFlush(tenant);

        // Get all the tenantList where companyName contains DEFAULT_COMPANY_NAME
        defaultTenantShouldBeFound("companyName.contains=" + DEFAULT_COMPANY_NAME);

        // Get all the tenantList where companyName contains UPDATED_COMPANY_NAME
        defaultTenantShouldNotBeFound("companyName.contains=" + UPDATED_COMPANY_NAME);
    }

    @Test
    @Transactional
    void getAllTenantsByCompanyNameNotContainsSomething() throws Exception {
        // Initialize the database
        tenantRepository.saveAndFlush(tenant);

        // Get all the tenantList where companyName does not contain DEFAULT_COMPANY_NAME
        defaultTenantShouldNotBeFound("companyName.doesNotContain=" + DEFAULT_COMPANY_NAME);

        // Get all the tenantList where companyName does not contain UPDATED_COMPANY_NAME
        defaultTenantShouldBeFound("companyName.doesNotContain=" + UPDATED_COMPANY_NAME);
    }

    @Test
    @Transactional
    void getAllTenantsByContactPersonIsEqualToSomething() throws Exception {
        // Initialize the database
        tenantRepository.saveAndFlush(tenant);

        // Get all the tenantList where contactPerson equals to DEFAULT_CONTACT_PERSON
        defaultTenantShouldBeFound("contactPerson.equals=" + DEFAULT_CONTACT_PERSON);

        // Get all the tenantList where contactPerson equals to UPDATED_CONTACT_PERSON
        defaultTenantShouldNotBeFound("contactPerson.equals=" + UPDATED_CONTACT_PERSON);
    }

    @Test
    @Transactional
    void getAllTenantsByContactPersonIsInShouldWork() throws Exception {
        // Initialize the database
        tenantRepository.saveAndFlush(tenant);

        // Get all the tenantList where contactPerson in DEFAULT_CONTACT_PERSON or UPDATED_CONTACT_PERSON
        defaultTenantShouldBeFound("contactPerson.in=" + DEFAULT_CONTACT_PERSON + "," + UPDATED_CONTACT_PERSON);

        // Get all the tenantList where contactPerson equals to UPDATED_CONTACT_PERSON
        defaultTenantShouldNotBeFound("contactPerson.in=" + UPDATED_CONTACT_PERSON);
    }

    @Test
    @Transactional
    void getAllTenantsByContactPersonIsNullOrNotNull() throws Exception {
        // Initialize the database
        tenantRepository.saveAndFlush(tenant);

        // Get all the tenantList where contactPerson is not null
        defaultTenantShouldBeFound("contactPerson.specified=true");

        // Get all the tenantList where contactPerson is null
        defaultTenantShouldNotBeFound("contactPerson.specified=false");
    }

    @Test
    @Transactional
    void getAllTenantsByContactPersonContainsSomething() throws Exception {
        // Initialize the database
        tenantRepository.saveAndFlush(tenant);

        // Get all the tenantList where contactPerson contains DEFAULT_CONTACT_PERSON
        defaultTenantShouldBeFound("contactPerson.contains=" + DEFAULT_CONTACT_PERSON);

        // Get all the tenantList where contactPerson contains UPDATED_CONTACT_PERSON
        defaultTenantShouldNotBeFound("contactPerson.contains=" + UPDATED_CONTACT_PERSON);
    }

    @Test
    @Transactional
    void getAllTenantsByContactPersonNotContainsSomething() throws Exception {
        // Initialize the database
        tenantRepository.saveAndFlush(tenant);

        // Get all the tenantList where contactPerson does not contain DEFAULT_CONTACT_PERSON
        defaultTenantShouldNotBeFound("contactPerson.doesNotContain=" + DEFAULT_CONTACT_PERSON);

        // Get all the tenantList where contactPerson does not contain UPDATED_CONTACT_PERSON
        defaultTenantShouldBeFound("contactPerson.doesNotContain=" + UPDATED_CONTACT_PERSON);
    }

    @Test
    @Transactional
    void getAllTenantsByLogoIsEqualToSomething() throws Exception {
        // Initialize the database
        tenantRepository.saveAndFlush(tenant);

        // Get all the tenantList where logo equals to DEFAULT_LOGO
        defaultTenantShouldBeFound("logo.equals=" + DEFAULT_LOGO);

        // Get all the tenantList where logo equals to UPDATED_LOGO
        defaultTenantShouldNotBeFound("logo.equals=" + UPDATED_LOGO);
    }

    @Test
    @Transactional
    void getAllTenantsByLogoIsInShouldWork() throws Exception {
        // Initialize the database
        tenantRepository.saveAndFlush(tenant);

        // Get all the tenantList where logo in DEFAULT_LOGO or UPDATED_LOGO
        defaultTenantShouldBeFound("logo.in=" + DEFAULT_LOGO + "," + UPDATED_LOGO);

        // Get all the tenantList where logo equals to UPDATED_LOGO
        defaultTenantShouldNotBeFound("logo.in=" + UPDATED_LOGO);
    }

    @Test
    @Transactional
    void getAllTenantsByLogoIsNullOrNotNull() throws Exception {
        // Initialize the database
        tenantRepository.saveAndFlush(tenant);

        // Get all the tenantList where logo is not null
        defaultTenantShouldBeFound("logo.specified=true");

        // Get all the tenantList where logo is null
        defaultTenantShouldNotBeFound("logo.specified=false");
    }

    @Test
    @Transactional
    void getAllTenantsByLogoContainsSomething() throws Exception {
        // Initialize the database
        tenantRepository.saveAndFlush(tenant);

        // Get all the tenantList where logo contains DEFAULT_LOGO
        defaultTenantShouldBeFound("logo.contains=" + DEFAULT_LOGO);

        // Get all the tenantList where logo contains UPDATED_LOGO
        defaultTenantShouldNotBeFound("logo.contains=" + UPDATED_LOGO);
    }

    @Test
    @Transactional
    void getAllTenantsByLogoNotContainsSomething() throws Exception {
        // Initialize the database
        tenantRepository.saveAndFlush(tenant);

        // Get all the tenantList where logo does not contain DEFAULT_LOGO
        defaultTenantShouldNotBeFound("logo.doesNotContain=" + DEFAULT_LOGO);

        // Get all the tenantList where logo does not contain UPDATED_LOGO
        defaultTenantShouldBeFound("logo.doesNotContain=" + UPDATED_LOGO);
    }

    @Test
    @Transactional
    void getAllTenantsByWebsiteIsEqualToSomething() throws Exception {
        // Initialize the database
        tenantRepository.saveAndFlush(tenant);

        // Get all the tenantList where website equals to DEFAULT_WEBSITE
        defaultTenantShouldBeFound("website.equals=" + DEFAULT_WEBSITE);

        // Get all the tenantList where website equals to UPDATED_WEBSITE
        defaultTenantShouldNotBeFound("website.equals=" + UPDATED_WEBSITE);
    }

    @Test
    @Transactional
    void getAllTenantsByWebsiteIsInShouldWork() throws Exception {
        // Initialize the database
        tenantRepository.saveAndFlush(tenant);

        // Get all the tenantList where website in DEFAULT_WEBSITE or UPDATED_WEBSITE
        defaultTenantShouldBeFound("website.in=" + DEFAULT_WEBSITE + "," + UPDATED_WEBSITE);

        // Get all the tenantList where website equals to UPDATED_WEBSITE
        defaultTenantShouldNotBeFound("website.in=" + UPDATED_WEBSITE);
    }

    @Test
    @Transactional
    void getAllTenantsByWebsiteIsNullOrNotNull() throws Exception {
        // Initialize the database
        tenantRepository.saveAndFlush(tenant);

        // Get all the tenantList where website is not null
        defaultTenantShouldBeFound("website.specified=true");

        // Get all the tenantList where website is null
        defaultTenantShouldNotBeFound("website.specified=false");
    }

    @Test
    @Transactional
    void getAllTenantsByWebsiteContainsSomething() throws Exception {
        // Initialize the database
        tenantRepository.saveAndFlush(tenant);

        // Get all the tenantList where website contains DEFAULT_WEBSITE
        defaultTenantShouldBeFound("website.contains=" + DEFAULT_WEBSITE);

        // Get all the tenantList where website contains UPDATED_WEBSITE
        defaultTenantShouldNotBeFound("website.contains=" + UPDATED_WEBSITE);
    }

    @Test
    @Transactional
    void getAllTenantsByWebsiteNotContainsSomething() throws Exception {
        // Initialize the database
        tenantRepository.saveAndFlush(tenant);

        // Get all the tenantList where website does not contain DEFAULT_WEBSITE
        defaultTenantShouldNotBeFound("website.doesNotContain=" + DEFAULT_WEBSITE);

        // Get all the tenantList where website does not contain UPDATED_WEBSITE
        defaultTenantShouldBeFound("website.doesNotContain=" + UPDATED_WEBSITE);
    }

    @Test
    @Transactional
    void getAllTenantsByRegistrationNumberIsEqualToSomething() throws Exception {
        // Initialize the database
        tenantRepository.saveAndFlush(tenant);

        // Get all the tenantList where registrationNumber equals to DEFAULT_REGISTRATION_NUMBER
        defaultTenantShouldBeFound("registrationNumber.equals=" + DEFAULT_REGISTRATION_NUMBER);

        // Get all the tenantList where registrationNumber equals to UPDATED_REGISTRATION_NUMBER
        defaultTenantShouldNotBeFound("registrationNumber.equals=" + UPDATED_REGISTRATION_NUMBER);
    }

    @Test
    @Transactional
    void getAllTenantsByRegistrationNumberIsInShouldWork() throws Exception {
        // Initialize the database
        tenantRepository.saveAndFlush(tenant);

        // Get all the tenantList where registrationNumber in DEFAULT_REGISTRATION_NUMBER or UPDATED_REGISTRATION_NUMBER
        defaultTenantShouldBeFound("registrationNumber.in=" + DEFAULT_REGISTRATION_NUMBER + "," + UPDATED_REGISTRATION_NUMBER);

        // Get all the tenantList where registrationNumber equals to UPDATED_REGISTRATION_NUMBER
        defaultTenantShouldNotBeFound("registrationNumber.in=" + UPDATED_REGISTRATION_NUMBER);
    }

    @Test
    @Transactional
    void getAllTenantsByRegistrationNumberIsNullOrNotNull() throws Exception {
        // Initialize the database
        tenantRepository.saveAndFlush(tenant);

        // Get all the tenantList where registrationNumber is not null
        defaultTenantShouldBeFound("registrationNumber.specified=true");

        // Get all the tenantList where registrationNumber is null
        defaultTenantShouldNotBeFound("registrationNumber.specified=false");
    }

    @Test
    @Transactional
    void getAllTenantsByRegistrationNumberContainsSomething() throws Exception {
        // Initialize the database
        tenantRepository.saveAndFlush(tenant);

        // Get all the tenantList where registrationNumber contains DEFAULT_REGISTRATION_NUMBER
        defaultTenantShouldBeFound("registrationNumber.contains=" + DEFAULT_REGISTRATION_NUMBER);

        // Get all the tenantList where registrationNumber contains UPDATED_REGISTRATION_NUMBER
        defaultTenantShouldNotBeFound("registrationNumber.contains=" + UPDATED_REGISTRATION_NUMBER);
    }

    @Test
    @Transactional
    void getAllTenantsByRegistrationNumberNotContainsSomething() throws Exception {
        // Initialize the database
        tenantRepository.saveAndFlush(tenant);

        // Get all the tenantList where registrationNumber does not contain DEFAULT_REGISTRATION_NUMBER
        defaultTenantShouldNotBeFound("registrationNumber.doesNotContain=" + DEFAULT_REGISTRATION_NUMBER);

        // Get all the tenantList where registrationNumber does not contain UPDATED_REGISTRATION_NUMBER
        defaultTenantShouldBeFound("registrationNumber.doesNotContain=" + UPDATED_REGISTRATION_NUMBER);
    }

    @Test
    @Transactional
    void getAllTenantsBySubIdIsEqualToSomething() throws Exception {
        // Initialize the database
        tenantRepository.saveAndFlush(tenant);

        // Get all the tenantList where subId equals to DEFAULT_SUB_ID
        defaultTenantShouldBeFound("subId.equals=" + DEFAULT_SUB_ID);

        // Get all the tenantList where subId equals to UPDATED_SUB_ID
        defaultTenantShouldNotBeFound("subId.equals=" + UPDATED_SUB_ID);
    }

    @Test
    @Transactional
    void getAllTenantsBySubIdIsInShouldWork() throws Exception {
        // Initialize the database
        tenantRepository.saveAndFlush(tenant);

        // Get all the tenantList where subId in DEFAULT_SUB_ID or UPDATED_SUB_ID
        defaultTenantShouldBeFound("subId.in=" + DEFAULT_SUB_ID + "," + UPDATED_SUB_ID);

        // Get all the tenantList where subId equals to UPDATED_SUB_ID
        defaultTenantShouldNotBeFound("subId.in=" + UPDATED_SUB_ID);
    }

    @Test
    @Transactional
    void getAllTenantsBySubIdIsNullOrNotNull() throws Exception {
        // Initialize the database
        tenantRepository.saveAndFlush(tenant);

        // Get all the tenantList where subId is not null
        defaultTenantShouldBeFound("subId.specified=true");

        // Get all the tenantList where subId is null
        defaultTenantShouldNotBeFound("subId.specified=false");
    }

    @Test
    @Transactional
    void getAllTenantsBySubIdIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        tenantRepository.saveAndFlush(tenant);

        // Get all the tenantList where subId is greater than or equal to DEFAULT_SUB_ID
        defaultTenantShouldBeFound("subId.greaterThanOrEqual=" + DEFAULT_SUB_ID);

        // Get all the tenantList where subId is greater than or equal to UPDATED_SUB_ID
        defaultTenantShouldNotBeFound("subId.greaterThanOrEqual=" + UPDATED_SUB_ID);
    }

    @Test
    @Transactional
    void getAllTenantsBySubIdIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        tenantRepository.saveAndFlush(tenant);

        // Get all the tenantList where subId is less than or equal to DEFAULT_SUB_ID
        defaultTenantShouldBeFound("subId.lessThanOrEqual=" + DEFAULT_SUB_ID);

        // Get all the tenantList where subId is less than or equal to SMALLER_SUB_ID
        defaultTenantShouldNotBeFound("subId.lessThanOrEqual=" + SMALLER_SUB_ID);
    }

    @Test
    @Transactional
    void getAllTenantsBySubIdIsLessThanSomething() throws Exception {
        // Initialize the database
        tenantRepository.saveAndFlush(tenant);

        // Get all the tenantList where subId is less than DEFAULT_SUB_ID
        defaultTenantShouldNotBeFound("subId.lessThan=" + DEFAULT_SUB_ID);

        // Get all the tenantList where subId is less than UPDATED_SUB_ID
        defaultTenantShouldBeFound("subId.lessThan=" + UPDATED_SUB_ID);
    }

    @Test
    @Transactional
    void getAllTenantsBySubIdIsGreaterThanSomething() throws Exception {
        // Initialize the database
        tenantRepository.saveAndFlush(tenant);

        // Get all the tenantList where subId is greater than DEFAULT_SUB_ID
        defaultTenantShouldNotBeFound("subId.greaterThan=" + DEFAULT_SUB_ID);

        // Get all the tenantList where subId is greater than SMALLER_SUB_ID
        defaultTenantShouldBeFound("subId.greaterThan=" + SMALLER_SUB_ID);
    }

    @Test
    @Transactional
    void getAllTenantsByAddressesIsEqualToSomething() throws Exception {
        Address addresses;
        if (TestUtil.findAll(em, Address.class).isEmpty()) {
            tenantRepository.saveAndFlush(tenant);
            addresses = AddressResourceIT.createEntity(em);
        } else {
            addresses = TestUtil.findAll(em, Address.class).get(0);
        }
        em.persist(addresses);
        em.flush();
        tenant.addAddresses(addresses);
        tenantRepository.saveAndFlush(tenant);
        Long addressesId = addresses.getId();
        // Get all the tenantList where addresses equals to addressesId
        defaultTenantShouldBeFound("addressesId.equals=" + addressesId);

        // Get all the tenantList where addresses equals to (addressesId + 1)
        defaultTenantShouldNotBeFound("addressesId.equals=" + (addressesId + 1));
    }

    @Test
    @Transactional
    void getAllTenantsByUsersIsEqualToSomething() throws Exception {
        User users;
        if (TestUtil.findAll(em, User.class).isEmpty()) {
            tenantRepository.saveAndFlush(tenant);
            users = UserResourceIT.createEntity(em);
        } else {
            users = TestUtil.findAll(em, User.class).get(0);
        }
        em.persist(users);
        em.flush();
        tenant.addUsers(users);
        tenantRepository.saveAndFlush(tenant);
        Long usersId = users.getId();
        // Get all the tenantList where users equals to usersId
        defaultTenantShouldBeFound("usersId.equals=" + usersId);

        // Get all the tenantList where users equals to (usersId + 1)
        defaultTenantShouldNotBeFound("usersId.equals=" + (usersId + 1));
    }

    @Test
    @Transactional
    void getAllTenantsByEncryptionIsEqualToSomething() throws Exception {
        Encryption encryption;
        if (TestUtil.findAll(em, Encryption.class).isEmpty()) {
            tenantRepository.saveAndFlush(tenant);
            encryption = EncryptionResourceIT.createEntity(em);
        } else {
            encryption = TestUtil.findAll(em, Encryption.class).get(0);
        }
        em.persist(encryption);
        em.flush();
        tenant.setEncryption(encryption);
        encryption.setTenant(tenant);
        tenantRepository.saveAndFlush(tenant);
        Long encryptionId = encryption.getId();
        // Get all the tenantList where encryption equals to encryptionId
        defaultTenantShouldBeFound("encryptionId.equals=" + encryptionId);

        // Get all the tenantList where encryption equals to (encryptionId + 1)
        defaultTenantShouldNotBeFound("encryptionId.equals=" + (encryptionId + 1));
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultTenantShouldBeFound(String filter) throws Exception {
        restTenantMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(tenant.getId().intValue())))
            .andExpect(jsonPath("$.[*].companyName").value(hasItem(DEFAULT_COMPANY_NAME)))
            .andExpect(jsonPath("$.[*].contactPerson").value(hasItem(DEFAULT_CONTACT_PERSON)))
            .andExpect(jsonPath("$.[*].logo").value(hasItem(DEFAULT_LOGO)))
            .andExpect(jsonPath("$.[*].website").value(hasItem(DEFAULT_WEBSITE)))
            .andExpect(jsonPath("$.[*].registrationNumber").value(hasItem(DEFAULT_REGISTRATION_NUMBER)))
            .andExpect(jsonPath("$.[*].subId").value(hasItem(DEFAULT_SUB_ID)));

        // Check, that the count call also returns 1
        restTenantMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultTenantShouldNotBeFound(String filter) throws Exception {
        restTenantMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restTenantMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingTenant() throws Exception {
        // Get the tenant
        restTenantMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingTenant() throws Exception {
        // Initialize the database
        tenantRepository.saveAndFlush(tenant);

        int databaseSizeBeforeUpdate = tenantRepository.findAll().size();

        // Update the tenant
        Tenant updatedTenant = tenantRepository.findById(tenant.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedTenant are not directly saved in db
        em.detach(updatedTenant);
        updatedTenant
            .companyName(UPDATED_COMPANY_NAME)
            .contactPerson(UPDATED_CONTACT_PERSON)
            .logo(UPDATED_LOGO)
            .website(UPDATED_WEBSITE)
            .registrationNumber(UPDATED_REGISTRATION_NUMBER)
            .subId(UPDATED_SUB_ID);
        TenantDTO tenantDTO = tenantMapper.toDto(updatedTenant);

        restTenantMockMvc
            .perform(
                put(ENTITY_API_URL_ID, tenantDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(tenantDTO))
            )
            .andExpect(status().isOk());

        // Validate the Tenant in the database
        List<Tenant> tenantList = tenantRepository.findAll();
        assertThat(tenantList).hasSize(databaseSizeBeforeUpdate);
        Tenant testTenant = tenantList.get(tenantList.size() - 1);
        assertThat(testTenant.getCompanyName()).isEqualTo(UPDATED_COMPANY_NAME);
        assertThat(testTenant.getContactPerson()).isEqualTo(UPDATED_CONTACT_PERSON);
        assertThat(testTenant.getLogo()).isEqualTo(UPDATED_LOGO);
        assertThat(testTenant.getWebsite()).isEqualTo(UPDATED_WEBSITE);
        assertThat(testTenant.getRegistrationNumber()).isEqualTo(UPDATED_REGISTRATION_NUMBER);
        assertThat(testTenant.getSubId()).isEqualTo(UPDATED_SUB_ID);
    }

    @Test
    @Transactional
    void putNonExistingTenant() throws Exception {
        int databaseSizeBeforeUpdate = tenantRepository.findAll().size();
        tenant.setId(longCount.incrementAndGet());

        // Create the Tenant
        TenantDTO tenantDTO = tenantMapper.toDto(tenant);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restTenantMockMvc
            .perform(
                put(ENTITY_API_URL_ID, tenantDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(tenantDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Tenant in the database
        List<Tenant> tenantList = tenantRepository.findAll();
        assertThat(tenantList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchTenant() throws Exception {
        int databaseSizeBeforeUpdate = tenantRepository.findAll().size();
        tenant.setId(longCount.incrementAndGet());

        // Create the Tenant
        TenantDTO tenantDTO = tenantMapper.toDto(tenant);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restTenantMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(tenantDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Tenant in the database
        List<Tenant> tenantList = tenantRepository.findAll();
        assertThat(tenantList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamTenant() throws Exception {
        int databaseSizeBeforeUpdate = tenantRepository.findAll().size();
        tenant.setId(longCount.incrementAndGet());

        // Create the Tenant
        TenantDTO tenantDTO = tenantMapper.toDto(tenant);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restTenantMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(tenantDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Tenant in the database
        List<Tenant> tenantList = tenantRepository.findAll();
        assertThat(tenantList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateTenantWithPatch() throws Exception {
        // Initialize the database
        tenantRepository.saveAndFlush(tenant);

        int databaseSizeBeforeUpdate = tenantRepository.findAll().size();

        // Update the tenant using partial update
        Tenant partialUpdatedTenant = new Tenant();
        partialUpdatedTenant.setId(tenant.getId());

        partialUpdatedTenant
            .companyName(UPDATED_COMPANY_NAME)
            .contactPerson(UPDATED_CONTACT_PERSON)
            .logo(UPDATED_LOGO)
            .website(UPDATED_WEBSITE);

        restTenantMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedTenant.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedTenant))
            )
            .andExpect(status().isOk());

        // Validate the Tenant in the database
        List<Tenant> tenantList = tenantRepository.findAll();
        assertThat(tenantList).hasSize(databaseSizeBeforeUpdate);
        Tenant testTenant = tenantList.get(tenantList.size() - 1);
        assertThat(testTenant.getCompanyName()).isEqualTo(UPDATED_COMPANY_NAME);
        assertThat(testTenant.getContactPerson()).isEqualTo(UPDATED_CONTACT_PERSON);
        assertThat(testTenant.getLogo()).isEqualTo(UPDATED_LOGO);
        assertThat(testTenant.getWebsite()).isEqualTo(UPDATED_WEBSITE);
        assertThat(testTenant.getRegistrationNumber()).isEqualTo(DEFAULT_REGISTRATION_NUMBER);
        assertThat(testTenant.getSubId()).isEqualTo(DEFAULT_SUB_ID);
    }

    @Test
    @Transactional
    void fullUpdateTenantWithPatch() throws Exception {
        // Initialize the database
        tenantRepository.saveAndFlush(tenant);

        int databaseSizeBeforeUpdate = tenantRepository.findAll().size();

        // Update the tenant using partial update
        Tenant partialUpdatedTenant = new Tenant();
        partialUpdatedTenant.setId(tenant.getId());

        partialUpdatedTenant
            .companyName(UPDATED_COMPANY_NAME)
            .contactPerson(UPDATED_CONTACT_PERSON)
            .logo(UPDATED_LOGO)
            .website(UPDATED_WEBSITE)
            .registrationNumber(UPDATED_REGISTRATION_NUMBER)
            .subId(UPDATED_SUB_ID);

        restTenantMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedTenant.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedTenant))
            )
            .andExpect(status().isOk());

        // Validate the Tenant in the database
        List<Tenant> tenantList = tenantRepository.findAll();
        assertThat(tenantList).hasSize(databaseSizeBeforeUpdate);
        Tenant testTenant = tenantList.get(tenantList.size() - 1);
        assertThat(testTenant.getCompanyName()).isEqualTo(UPDATED_COMPANY_NAME);
        assertThat(testTenant.getContactPerson()).isEqualTo(UPDATED_CONTACT_PERSON);
        assertThat(testTenant.getLogo()).isEqualTo(UPDATED_LOGO);
        assertThat(testTenant.getWebsite()).isEqualTo(UPDATED_WEBSITE);
        assertThat(testTenant.getRegistrationNumber()).isEqualTo(UPDATED_REGISTRATION_NUMBER);
        assertThat(testTenant.getSubId()).isEqualTo(UPDATED_SUB_ID);
    }

    @Test
    @Transactional
    void patchNonExistingTenant() throws Exception {
        int databaseSizeBeforeUpdate = tenantRepository.findAll().size();
        tenant.setId(longCount.incrementAndGet());

        // Create the Tenant
        TenantDTO tenantDTO = tenantMapper.toDto(tenant);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restTenantMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, tenantDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(tenantDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Tenant in the database
        List<Tenant> tenantList = tenantRepository.findAll();
        assertThat(tenantList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchTenant() throws Exception {
        int databaseSizeBeforeUpdate = tenantRepository.findAll().size();
        tenant.setId(longCount.incrementAndGet());

        // Create the Tenant
        TenantDTO tenantDTO = tenantMapper.toDto(tenant);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restTenantMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(tenantDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Tenant in the database
        List<Tenant> tenantList = tenantRepository.findAll();
        assertThat(tenantList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamTenant() throws Exception {
        int databaseSizeBeforeUpdate = tenantRepository.findAll().size();
        tenant.setId(longCount.incrementAndGet());

        // Create the Tenant
        TenantDTO tenantDTO = tenantMapper.toDto(tenant);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restTenantMockMvc
            .perform(
                patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(tenantDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the Tenant in the database
        List<Tenant> tenantList = tenantRepository.findAll();
        assertThat(tenantList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteTenant() throws Exception {
        // Initialize the database
        tenantRepository.saveAndFlush(tenant);

        int databaseSizeBeforeDelete = tenantRepository.findAll().size();

        // Delete the tenant
        restTenantMockMvc
            .perform(delete(ENTITY_API_URL_ID, tenant.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Tenant> tenantList = tenantRepository.findAll();
        assertThat(tenantList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
