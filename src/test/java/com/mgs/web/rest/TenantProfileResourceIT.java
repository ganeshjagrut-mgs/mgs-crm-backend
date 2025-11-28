package com.mgs.web.rest;

import static com.mgs.domain.TenantProfileAsserts.*;
import static com.mgs.web.rest.TestUtil.createUpdateProxyForBean;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mgs.IntegrationTest;
import com.mgs.domain.Address;
import com.mgs.domain.Tenant;
import com.mgs.domain.TenantProfile;
import com.mgs.repository.TenantProfileRepository;
import com.mgs.service.dto.TenantProfileDTO;
import com.mgs.service.mapper.TenantProfileMapper;
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
 * Integration tests for the {@link TenantProfileResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class TenantProfileResourceIT {

    private static final String DEFAULT_SUBDOMAIN = "AAAAAAAAAA";
    private static final String UPDATED_SUBDOMAIN = "BBBBBBBBBB";

    private static final String DEFAULT_CUSTOM_DOMAIN = "AAAAAAAAAA";
    private static final String UPDATED_CUSTOM_DOMAIN = "BBBBBBBBBB";

    private static final Boolean DEFAULT_DOMAIN_VERIFIED = false;
    private static final Boolean UPDATED_DOMAIN_VERIFIED = true;

    private static final String DEFAULT_LEGAL_NAME = "AAAAAAAAAA";
    private static final String UPDATED_LEGAL_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_SHORT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_SHORT_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_REGISTRATION_NUMBER = "AAAAAAAAAA";
    private static final String UPDATED_REGISTRATION_NUMBER = "BBBBBBBBBB";

    private static final String DEFAULT_TAX_ID = "AAAAAAAAAA";
    private static final String UPDATED_TAX_ID = "BBBBBBBBBB";

    private static final String DEFAULT_CONTACT_PERSON = "AAAAAAAAAA";
    private static final String UPDATED_CONTACT_PERSON = "BBBBBBBBBB";

    private static final String DEFAULT_CONTACT_EMAIL = "AAAAAAAAAA";
    private static final String UPDATED_CONTACT_EMAIL = "BBBBBBBBBB";

    private static final String DEFAULT_CONTACT_PHONE = "AAAAAAAAAA";
    private static final String UPDATED_CONTACT_PHONE = "BBBBBBBBBB";

    private static final String DEFAULT_WEBSITE_URL = "AAAAAAAAAA";
    private static final String UPDATED_WEBSITE_URL = "BBBBBBBBBB";

    private static final String DEFAULT_DEFAULT_LOCALE = "AAAAAAAAAA";
    private static final String UPDATED_DEFAULT_LOCALE = "BBBBBBBBBB";

    private static final String DEFAULT_TIMEZONE = "AAAAAAAAAA";
    private static final String UPDATED_TIMEZONE = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/tenant-profiles";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ObjectMapper om;

    @Autowired
    private TenantProfileRepository tenantProfileRepository;

    @Autowired
    private TenantProfileMapper tenantProfileMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restTenantProfileMockMvc;

    private TenantProfile tenantProfile;

    private TenantProfile insertedTenantProfile;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static TenantProfile createEntity(EntityManager em) {
        TenantProfile tenantProfile = new TenantProfile()
            .subdomain(DEFAULT_SUBDOMAIN)
            .customDomain(DEFAULT_CUSTOM_DOMAIN)
            .domainVerified(DEFAULT_DOMAIN_VERIFIED)
            .legalName(DEFAULT_LEGAL_NAME)
            .shortName(DEFAULT_SHORT_NAME)
            .registrationNumber(DEFAULT_REGISTRATION_NUMBER)
            .taxId(DEFAULT_TAX_ID)
            .contactPerson(DEFAULT_CONTACT_PERSON)
            .contactEmail(DEFAULT_CONTACT_EMAIL)
            .contactPhone(DEFAULT_CONTACT_PHONE)
            .websiteUrl(DEFAULT_WEBSITE_URL)
            .defaultLocale(DEFAULT_DEFAULT_LOCALE)
            .timezone(DEFAULT_TIMEZONE);
        // Add required entity
        Tenant tenant;
        if (TestUtil.findAll(em, Tenant.class).isEmpty()) {
            tenant = TenantResourceIT.createEntity();
            em.persist(tenant);
            em.flush();
        } else {
            tenant = TestUtil.findAll(em, Tenant.class).get(0);
        }
        tenantProfile.setTenant(tenant);
        return tenantProfile;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static TenantProfile createUpdatedEntity(EntityManager em) {
        TenantProfile updatedTenantProfile = new TenantProfile()
            .subdomain(UPDATED_SUBDOMAIN)
            .customDomain(UPDATED_CUSTOM_DOMAIN)
            .domainVerified(UPDATED_DOMAIN_VERIFIED)
            .legalName(UPDATED_LEGAL_NAME)
            .shortName(UPDATED_SHORT_NAME)
            .registrationNumber(UPDATED_REGISTRATION_NUMBER)
            .taxId(UPDATED_TAX_ID)
            .contactPerson(UPDATED_CONTACT_PERSON)
            .contactEmail(UPDATED_CONTACT_EMAIL)
            .contactPhone(UPDATED_CONTACT_PHONE)
            .websiteUrl(UPDATED_WEBSITE_URL)
            .defaultLocale(UPDATED_DEFAULT_LOCALE)
            .timezone(UPDATED_TIMEZONE);
        // Add required entity
        Tenant tenant;
        if (TestUtil.findAll(em, Tenant.class).isEmpty()) {
            tenant = TenantResourceIT.createUpdatedEntity();
            em.persist(tenant);
            em.flush();
        } else {
            tenant = TestUtil.findAll(em, Tenant.class).get(0);
        }
        updatedTenantProfile.setTenant(tenant);
        return updatedTenantProfile;
    }

    @BeforeEach
    void initTest() {
        tenantProfile = createEntity(em);
    }

    @AfterEach
    void cleanup() {
        if (insertedTenantProfile != null) {
            tenantProfileRepository.delete(insertedTenantProfile);
            insertedTenantProfile = null;
        }
    }

    @Test
    @Transactional
    void createTenantProfile() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the TenantProfile
        TenantProfileDTO tenantProfileDTO = tenantProfileMapper.toDto(tenantProfile);
        var returnedTenantProfileDTO = om.readValue(
            restTenantProfileMockMvc
                .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(tenantProfileDTO)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            TenantProfileDTO.class
        );

        // Validate the TenantProfile in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        var returnedTenantProfile = tenantProfileMapper.toEntity(returnedTenantProfileDTO);
        assertTenantProfileUpdatableFieldsEquals(returnedTenantProfile, getPersistedTenantProfile(returnedTenantProfile));

        insertedTenantProfile = returnedTenantProfile;
    }

    @Test
    @Transactional
    void createTenantProfileWithExistingId() throws Exception {
        // Create the TenantProfile with an existing ID
        tenantProfile.setId(1L);
        TenantProfileDTO tenantProfileDTO = tenantProfileMapper.toDto(tenantProfile);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restTenantProfileMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(tenantProfileDTO)))
            .andExpect(status().isBadRequest());

        // Validate the TenantProfile in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkDomainVerifiedIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        tenantProfile.setDomainVerified(null);

        // Create the TenantProfile, which fails.
        TenantProfileDTO tenantProfileDTO = tenantProfileMapper.toDto(tenantProfile);

        restTenantProfileMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(tenantProfileDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllTenantProfiles() throws Exception {
        // Initialize the database
        insertedTenantProfile = tenantProfileRepository.saveAndFlush(tenantProfile);

        // Get all the tenantProfileList
        restTenantProfileMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(tenantProfile.getId().intValue())))
            .andExpect(jsonPath("$.[*].subdomain").value(hasItem(DEFAULT_SUBDOMAIN)))
            .andExpect(jsonPath("$.[*].customDomain").value(hasItem(DEFAULT_CUSTOM_DOMAIN)))
            .andExpect(jsonPath("$.[*].domainVerified").value(hasItem(DEFAULT_DOMAIN_VERIFIED)))
            .andExpect(jsonPath("$.[*].legalName").value(hasItem(DEFAULT_LEGAL_NAME)))
            .andExpect(jsonPath("$.[*].shortName").value(hasItem(DEFAULT_SHORT_NAME)))
            .andExpect(jsonPath("$.[*].registrationNumber").value(hasItem(DEFAULT_REGISTRATION_NUMBER)))
            .andExpect(jsonPath("$.[*].taxId").value(hasItem(DEFAULT_TAX_ID)))
            .andExpect(jsonPath("$.[*].contactPerson").value(hasItem(DEFAULT_CONTACT_PERSON)))
            .andExpect(jsonPath("$.[*].contactEmail").value(hasItem(DEFAULT_CONTACT_EMAIL)))
            .andExpect(jsonPath("$.[*].contactPhone").value(hasItem(DEFAULT_CONTACT_PHONE)))
            .andExpect(jsonPath("$.[*].websiteUrl").value(hasItem(DEFAULT_WEBSITE_URL)))
            .andExpect(jsonPath("$.[*].defaultLocale").value(hasItem(DEFAULT_DEFAULT_LOCALE)))
            .andExpect(jsonPath("$.[*].timezone").value(hasItem(DEFAULT_TIMEZONE)));
    }

    @Test
    @Transactional
    void getTenantProfile() throws Exception {
        // Initialize the database
        insertedTenantProfile = tenantProfileRepository.saveAndFlush(tenantProfile);

        // Get the tenantProfile
        restTenantProfileMockMvc
            .perform(get(ENTITY_API_URL_ID, tenantProfile.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(tenantProfile.getId().intValue()))
            .andExpect(jsonPath("$.subdomain").value(DEFAULT_SUBDOMAIN))
            .andExpect(jsonPath("$.customDomain").value(DEFAULT_CUSTOM_DOMAIN))
            .andExpect(jsonPath("$.domainVerified").value(DEFAULT_DOMAIN_VERIFIED))
            .andExpect(jsonPath("$.legalName").value(DEFAULT_LEGAL_NAME))
            .andExpect(jsonPath("$.shortName").value(DEFAULT_SHORT_NAME))
            .andExpect(jsonPath("$.registrationNumber").value(DEFAULT_REGISTRATION_NUMBER))
            .andExpect(jsonPath("$.taxId").value(DEFAULT_TAX_ID))
            .andExpect(jsonPath("$.contactPerson").value(DEFAULT_CONTACT_PERSON))
            .andExpect(jsonPath("$.contactEmail").value(DEFAULT_CONTACT_EMAIL))
            .andExpect(jsonPath("$.contactPhone").value(DEFAULT_CONTACT_PHONE))
            .andExpect(jsonPath("$.websiteUrl").value(DEFAULT_WEBSITE_URL))
            .andExpect(jsonPath("$.defaultLocale").value(DEFAULT_DEFAULT_LOCALE))
            .andExpect(jsonPath("$.timezone").value(DEFAULT_TIMEZONE));
    }

    @Test
    @Transactional
    void getTenantProfilesByIdFiltering() throws Exception {
        // Initialize the database
        insertedTenantProfile = tenantProfileRepository.saveAndFlush(tenantProfile);

        Long id = tenantProfile.getId();

        defaultTenantProfileFiltering("id.equals=" + id, "id.notEquals=" + id);

        defaultTenantProfileFiltering("id.greaterThanOrEqual=" + id, "id.greaterThan=" + id);

        defaultTenantProfileFiltering("id.lessThanOrEqual=" + id, "id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllTenantProfilesBySubdomainIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedTenantProfile = tenantProfileRepository.saveAndFlush(tenantProfile);

        // Get all the tenantProfileList where subdomain equals to
        defaultTenantProfileFiltering("subdomain.equals=" + DEFAULT_SUBDOMAIN, "subdomain.equals=" + UPDATED_SUBDOMAIN);
    }

    @Test
    @Transactional
    void getAllTenantProfilesBySubdomainIsInShouldWork() throws Exception {
        // Initialize the database
        insertedTenantProfile = tenantProfileRepository.saveAndFlush(tenantProfile);

        // Get all the tenantProfileList where subdomain in
        defaultTenantProfileFiltering("subdomain.in=" + DEFAULT_SUBDOMAIN + "," + UPDATED_SUBDOMAIN, "subdomain.in=" + UPDATED_SUBDOMAIN);
    }

    @Test
    @Transactional
    void getAllTenantProfilesBySubdomainIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedTenantProfile = tenantProfileRepository.saveAndFlush(tenantProfile);

        // Get all the tenantProfileList where subdomain is not null
        defaultTenantProfileFiltering("subdomain.specified=true", "subdomain.specified=false");
    }

    @Test
    @Transactional
    void getAllTenantProfilesBySubdomainContainsSomething() throws Exception {
        // Initialize the database
        insertedTenantProfile = tenantProfileRepository.saveAndFlush(tenantProfile);

        // Get all the tenantProfileList where subdomain contains
        defaultTenantProfileFiltering("subdomain.contains=" + DEFAULT_SUBDOMAIN, "subdomain.contains=" + UPDATED_SUBDOMAIN);
    }

    @Test
    @Transactional
    void getAllTenantProfilesBySubdomainNotContainsSomething() throws Exception {
        // Initialize the database
        insertedTenantProfile = tenantProfileRepository.saveAndFlush(tenantProfile);

        // Get all the tenantProfileList where subdomain does not contain
        defaultTenantProfileFiltering("subdomain.doesNotContain=" + UPDATED_SUBDOMAIN, "subdomain.doesNotContain=" + DEFAULT_SUBDOMAIN);
    }

    @Test
    @Transactional
    void getAllTenantProfilesByCustomDomainIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedTenantProfile = tenantProfileRepository.saveAndFlush(tenantProfile);

        // Get all the tenantProfileList where customDomain equals to
        defaultTenantProfileFiltering("customDomain.equals=" + DEFAULT_CUSTOM_DOMAIN, "customDomain.equals=" + UPDATED_CUSTOM_DOMAIN);
    }

    @Test
    @Transactional
    void getAllTenantProfilesByCustomDomainIsInShouldWork() throws Exception {
        // Initialize the database
        insertedTenantProfile = tenantProfileRepository.saveAndFlush(tenantProfile);

        // Get all the tenantProfileList where customDomain in
        defaultTenantProfileFiltering(
            "customDomain.in=" + DEFAULT_CUSTOM_DOMAIN + "," + UPDATED_CUSTOM_DOMAIN,
            "customDomain.in=" + UPDATED_CUSTOM_DOMAIN
        );
    }

    @Test
    @Transactional
    void getAllTenantProfilesByCustomDomainIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedTenantProfile = tenantProfileRepository.saveAndFlush(tenantProfile);

        // Get all the tenantProfileList where customDomain is not null
        defaultTenantProfileFiltering("customDomain.specified=true", "customDomain.specified=false");
    }

    @Test
    @Transactional
    void getAllTenantProfilesByCustomDomainContainsSomething() throws Exception {
        // Initialize the database
        insertedTenantProfile = tenantProfileRepository.saveAndFlush(tenantProfile);

        // Get all the tenantProfileList where customDomain contains
        defaultTenantProfileFiltering("customDomain.contains=" + DEFAULT_CUSTOM_DOMAIN, "customDomain.contains=" + UPDATED_CUSTOM_DOMAIN);
    }

    @Test
    @Transactional
    void getAllTenantProfilesByCustomDomainNotContainsSomething() throws Exception {
        // Initialize the database
        insertedTenantProfile = tenantProfileRepository.saveAndFlush(tenantProfile);

        // Get all the tenantProfileList where customDomain does not contain
        defaultTenantProfileFiltering(
            "customDomain.doesNotContain=" + UPDATED_CUSTOM_DOMAIN,
            "customDomain.doesNotContain=" + DEFAULT_CUSTOM_DOMAIN
        );
    }

    @Test
    @Transactional
    void getAllTenantProfilesByDomainVerifiedIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedTenantProfile = tenantProfileRepository.saveAndFlush(tenantProfile);

        // Get all the tenantProfileList where domainVerified equals to
        defaultTenantProfileFiltering(
            "domainVerified.equals=" + DEFAULT_DOMAIN_VERIFIED,
            "domainVerified.equals=" + UPDATED_DOMAIN_VERIFIED
        );
    }

    @Test
    @Transactional
    void getAllTenantProfilesByDomainVerifiedIsInShouldWork() throws Exception {
        // Initialize the database
        insertedTenantProfile = tenantProfileRepository.saveAndFlush(tenantProfile);

        // Get all the tenantProfileList where domainVerified in
        defaultTenantProfileFiltering(
            "domainVerified.in=" + DEFAULT_DOMAIN_VERIFIED + "," + UPDATED_DOMAIN_VERIFIED,
            "domainVerified.in=" + UPDATED_DOMAIN_VERIFIED
        );
    }

    @Test
    @Transactional
    void getAllTenantProfilesByDomainVerifiedIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedTenantProfile = tenantProfileRepository.saveAndFlush(tenantProfile);

        // Get all the tenantProfileList where domainVerified is not null
        defaultTenantProfileFiltering("domainVerified.specified=true", "domainVerified.specified=false");
    }

    @Test
    @Transactional
    void getAllTenantProfilesByDefaultLocaleIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedTenantProfile = tenantProfileRepository.saveAndFlush(tenantProfile);

        // Get all the tenantProfileList where defaultLocale equals to
        defaultTenantProfileFiltering("defaultLocale.equals=" + DEFAULT_DEFAULT_LOCALE, "defaultLocale.equals=" + UPDATED_DEFAULT_LOCALE);
    }

    @Test
    @Transactional
    void getAllTenantProfilesByDefaultLocaleIsInShouldWork() throws Exception {
        // Initialize the database
        insertedTenantProfile = tenantProfileRepository.saveAndFlush(tenantProfile);

        // Get all the tenantProfileList where defaultLocale in
        defaultTenantProfileFiltering(
            "defaultLocale.in=" + DEFAULT_DEFAULT_LOCALE + "," + UPDATED_DEFAULT_LOCALE,
            "defaultLocale.in=" + UPDATED_DEFAULT_LOCALE
        );
    }

    @Test
    @Transactional
    void getAllTenantProfilesByDefaultLocaleIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedTenantProfile = tenantProfileRepository.saveAndFlush(tenantProfile);

        // Get all the tenantProfileList where defaultLocale is not null
        defaultTenantProfileFiltering("defaultLocale.specified=true", "defaultLocale.specified=false");
    }

    @Test
    @Transactional
    void getAllTenantProfilesByDefaultLocaleContainsSomething() throws Exception {
        // Initialize the database
        insertedTenantProfile = tenantProfileRepository.saveAndFlush(tenantProfile);

        // Get all the tenantProfileList where defaultLocale contains
        defaultTenantProfileFiltering(
            "defaultLocale.contains=" + DEFAULT_DEFAULT_LOCALE,
            "defaultLocale.contains=" + UPDATED_DEFAULT_LOCALE
        );
    }

    @Test
    @Transactional
    void getAllTenantProfilesByDefaultLocaleNotContainsSomething() throws Exception {
        // Initialize the database
        insertedTenantProfile = tenantProfileRepository.saveAndFlush(tenantProfile);

        // Get all the tenantProfileList where defaultLocale does not contain
        defaultTenantProfileFiltering(
            "defaultLocale.doesNotContain=" + UPDATED_DEFAULT_LOCALE,
            "defaultLocale.doesNotContain=" + DEFAULT_DEFAULT_LOCALE
        );
    }

    @Test
    @Transactional
    void getAllTenantProfilesByTimezoneIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedTenantProfile = tenantProfileRepository.saveAndFlush(tenantProfile);

        // Get all the tenantProfileList where timezone equals to
        defaultTenantProfileFiltering("timezone.equals=" + DEFAULT_TIMEZONE, "timezone.equals=" + UPDATED_TIMEZONE);
    }

    @Test
    @Transactional
    void getAllTenantProfilesByTimezoneIsInShouldWork() throws Exception {
        // Initialize the database
        insertedTenantProfile = tenantProfileRepository.saveAndFlush(tenantProfile);

        // Get all the tenantProfileList where timezone in
        defaultTenantProfileFiltering("timezone.in=" + DEFAULT_TIMEZONE + "," + UPDATED_TIMEZONE, "timezone.in=" + UPDATED_TIMEZONE);
    }

    @Test
    @Transactional
    void getAllTenantProfilesByTimezoneIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedTenantProfile = tenantProfileRepository.saveAndFlush(tenantProfile);

        // Get all the tenantProfileList where timezone is not null
        defaultTenantProfileFiltering("timezone.specified=true", "timezone.specified=false");
    }

    @Test
    @Transactional
    void getAllTenantProfilesByTimezoneContainsSomething() throws Exception {
        // Initialize the database
        insertedTenantProfile = tenantProfileRepository.saveAndFlush(tenantProfile);

        // Get all the tenantProfileList where timezone contains
        defaultTenantProfileFiltering("timezone.contains=" + DEFAULT_TIMEZONE, "timezone.contains=" + UPDATED_TIMEZONE);
    }

    @Test
    @Transactional
    void getAllTenantProfilesByTimezoneNotContainsSomething() throws Exception {
        // Initialize the database
        insertedTenantProfile = tenantProfileRepository.saveAndFlush(tenantProfile);

        // Get all the tenantProfileList where timezone does not contain
        defaultTenantProfileFiltering("timezone.doesNotContain=" + UPDATED_TIMEZONE, "timezone.doesNotContain=" + DEFAULT_TIMEZONE);
    }

    @Test
    @Transactional
    void getAllTenantProfilesByTenantIsEqualToSomething() throws Exception {
        // Get already existing entity
        Tenant tenant = tenantProfile.getTenant();
        tenantProfileRepository.saveAndFlush(tenantProfile);
        Long tenantId = tenant.getId();
        // Get all the tenantProfileList where tenant equals to tenantId
        defaultTenantProfileShouldBeFound("tenantId.equals=" + tenantId);

        // Get all the tenantProfileList where tenant equals to (tenantId + 1)
        defaultTenantProfileShouldNotBeFound("tenantId.equals=" + (tenantId + 1));
    }

    @Test
    @Transactional
    void getAllTenantProfilesByAddressIsEqualToSomething() throws Exception {
        Address address;
        if (TestUtil.findAll(em, Address.class).isEmpty()) {
            tenantProfileRepository.saveAndFlush(tenantProfile);
            address = AddressResourceIT.createEntity(em);
        } else {
            address = TestUtil.findAll(em, Address.class).get(0);
        }
        em.persist(address);
        em.flush();
        tenantProfile.setAddress(address);
        tenantProfileRepository.saveAndFlush(tenantProfile);
        Long addressId = address.getId();
        // Get all the tenantProfileList where address equals to addressId
        defaultTenantProfileShouldBeFound("addressId.equals=" + addressId);

        // Get all the tenantProfileList where address equals to (addressId + 1)
        defaultTenantProfileShouldNotBeFound("addressId.equals=" + (addressId + 1));
    }

    private void defaultTenantProfileFiltering(String shouldBeFound, String shouldNotBeFound) throws Exception {
        defaultTenantProfileShouldBeFound(shouldBeFound);
        defaultTenantProfileShouldNotBeFound(shouldNotBeFound);
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultTenantProfileShouldBeFound(String filter) throws Exception {
        restTenantProfileMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(tenantProfile.getId().intValue())))
            .andExpect(jsonPath("$.[*].subdomain").value(hasItem(DEFAULT_SUBDOMAIN)))
            .andExpect(jsonPath("$.[*].customDomain").value(hasItem(DEFAULT_CUSTOM_DOMAIN)))
            .andExpect(jsonPath("$.[*].domainVerified").value(hasItem(DEFAULT_DOMAIN_VERIFIED)))
            .andExpect(jsonPath("$.[*].legalName").value(hasItem(DEFAULT_LEGAL_NAME)))
            .andExpect(jsonPath("$.[*].shortName").value(hasItem(DEFAULT_SHORT_NAME)))
            .andExpect(jsonPath("$.[*].registrationNumber").value(hasItem(DEFAULT_REGISTRATION_NUMBER)))
            .andExpect(jsonPath("$.[*].taxId").value(hasItem(DEFAULT_TAX_ID)))
            .andExpect(jsonPath("$.[*].contactPerson").value(hasItem(DEFAULT_CONTACT_PERSON)))
            .andExpect(jsonPath("$.[*].contactEmail").value(hasItem(DEFAULT_CONTACT_EMAIL)))
            .andExpect(jsonPath("$.[*].contactPhone").value(hasItem(DEFAULT_CONTACT_PHONE)))
            .andExpect(jsonPath("$.[*].websiteUrl").value(hasItem(DEFAULT_WEBSITE_URL)))
            .andExpect(jsonPath("$.[*].defaultLocale").value(hasItem(DEFAULT_DEFAULT_LOCALE)))
            .andExpect(jsonPath("$.[*].timezone").value(hasItem(DEFAULT_TIMEZONE)));

        // Check, that the count call also returns 1
        restTenantProfileMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultTenantProfileShouldNotBeFound(String filter) throws Exception {
        restTenantProfileMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restTenantProfileMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingTenantProfile() throws Exception {
        // Get the tenantProfile
        restTenantProfileMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingTenantProfile() throws Exception {
        // Initialize the database
        insertedTenantProfile = tenantProfileRepository.saveAndFlush(tenantProfile);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the tenantProfile
        TenantProfile updatedTenantProfile = tenantProfileRepository.findById(tenantProfile.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedTenantProfile are not directly saved in db
        em.detach(updatedTenantProfile);
        updatedTenantProfile
            .subdomain(UPDATED_SUBDOMAIN)
            .customDomain(UPDATED_CUSTOM_DOMAIN)
            .domainVerified(UPDATED_DOMAIN_VERIFIED)
            .legalName(UPDATED_LEGAL_NAME)
            .shortName(UPDATED_SHORT_NAME)
            .registrationNumber(UPDATED_REGISTRATION_NUMBER)
            .taxId(UPDATED_TAX_ID)
            .contactPerson(UPDATED_CONTACT_PERSON)
            .contactEmail(UPDATED_CONTACT_EMAIL)
            .contactPhone(UPDATED_CONTACT_PHONE)
            .websiteUrl(UPDATED_WEBSITE_URL)
            .defaultLocale(UPDATED_DEFAULT_LOCALE)
            .timezone(UPDATED_TIMEZONE);
        TenantProfileDTO tenantProfileDTO = tenantProfileMapper.toDto(updatedTenantProfile);

        restTenantProfileMockMvc
            .perform(
                put(ENTITY_API_URL_ID, tenantProfileDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(tenantProfileDTO))
            )
            .andExpect(status().isOk());

        // Validate the TenantProfile in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedTenantProfileToMatchAllProperties(updatedTenantProfile);
    }

    @Test
    @Transactional
    void putNonExistingTenantProfile() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        tenantProfile.setId(longCount.incrementAndGet());

        // Create the TenantProfile
        TenantProfileDTO tenantProfileDTO = tenantProfileMapper.toDto(tenantProfile);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restTenantProfileMockMvc
            .perform(
                put(ENTITY_API_URL_ID, tenantProfileDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(tenantProfileDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the TenantProfile in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchTenantProfile() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        tenantProfile.setId(longCount.incrementAndGet());

        // Create the TenantProfile
        TenantProfileDTO tenantProfileDTO = tenantProfileMapper.toDto(tenantProfile);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restTenantProfileMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(tenantProfileDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the TenantProfile in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamTenantProfile() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        tenantProfile.setId(longCount.incrementAndGet());

        // Create the TenantProfile
        TenantProfileDTO tenantProfileDTO = tenantProfileMapper.toDto(tenantProfile);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restTenantProfileMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(tenantProfileDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the TenantProfile in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateTenantProfileWithPatch() throws Exception {
        // Initialize the database
        insertedTenantProfile = tenantProfileRepository.saveAndFlush(tenantProfile);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the tenantProfile using partial update
        TenantProfile partialUpdatedTenantProfile = new TenantProfile();
        partialUpdatedTenantProfile.setId(tenantProfile.getId());

        partialUpdatedTenantProfile
            .customDomain(UPDATED_CUSTOM_DOMAIN)
            .legalName(UPDATED_LEGAL_NAME)
            .shortName(UPDATED_SHORT_NAME)
            .registrationNumber(UPDATED_REGISTRATION_NUMBER)
            .contactEmail(UPDATED_CONTACT_EMAIL)
            .contactPhone(UPDATED_CONTACT_PHONE)
            .defaultLocale(UPDATED_DEFAULT_LOCALE)
            .timezone(UPDATED_TIMEZONE);

        restTenantProfileMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedTenantProfile.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedTenantProfile))
            )
            .andExpect(status().isOk());

        // Validate the TenantProfile in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertTenantProfileUpdatableFieldsEquals(
            createUpdateProxyForBean(partialUpdatedTenantProfile, tenantProfile),
            getPersistedTenantProfile(tenantProfile)
        );
    }

    @Test
    @Transactional
    void fullUpdateTenantProfileWithPatch() throws Exception {
        // Initialize the database
        insertedTenantProfile = tenantProfileRepository.saveAndFlush(tenantProfile);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the tenantProfile using partial update
        TenantProfile partialUpdatedTenantProfile = new TenantProfile();
        partialUpdatedTenantProfile.setId(tenantProfile.getId());

        partialUpdatedTenantProfile
            .subdomain(UPDATED_SUBDOMAIN)
            .customDomain(UPDATED_CUSTOM_DOMAIN)
            .domainVerified(UPDATED_DOMAIN_VERIFIED)
            .legalName(UPDATED_LEGAL_NAME)
            .shortName(UPDATED_SHORT_NAME)
            .registrationNumber(UPDATED_REGISTRATION_NUMBER)
            .taxId(UPDATED_TAX_ID)
            .contactPerson(UPDATED_CONTACT_PERSON)
            .contactEmail(UPDATED_CONTACT_EMAIL)
            .contactPhone(UPDATED_CONTACT_PHONE)
            .websiteUrl(UPDATED_WEBSITE_URL)
            .defaultLocale(UPDATED_DEFAULT_LOCALE)
            .timezone(UPDATED_TIMEZONE);

        restTenantProfileMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedTenantProfile.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedTenantProfile))
            )
            .andExpect(status().isOk());

        // Validate the TenantProfile in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertTenantProfileUpdatableFieldsEquals(partialUpdatedTenantProfile, getPersistedTenantProfile(partialUpdatedTenantProfile));
    }

    @Test
    @Transactional
    void patchNonExistingTenantProfile() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        tenantProfile.setId(longCount.incrementAndGet());

        // Create the TenantProfile
        TenantProfileDTO tenantProfileDTO = tenantProfileMapper.toDto(tenantProfile);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restTenantProfileMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, tenantProfileDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(tenantProfileDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the TenantProfile in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchTenantProfile() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        tenantProfile.setId(longCount.incrementAndGet());

        // Create the TenantProfile
        TenantProfileDTO tenantProfileDTO = tenantProfileMapper.toDto(tenantProfile);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restTenantProfileMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(tenantProfileDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the TenantProfile in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamTenantProfile() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        tenantProfile.setId(longCount.incrementAndGet());

        // Create the TenantProfile
        TenantProfileDTO tenantProfileDTO = tenantProfileMapper.toDto(tenantProfile);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restTenantProfileMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(om.writeValueAsBytes(tenantProfileDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the TenantProfile in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteTenantProfile() throws Exception {
        // Initialize the database
        insertedTenantProfile = tenantProfileRepository.saveAndFlush(tenantProfile);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the tenantProfile
        restTenantProfileMockMvc
            .perform(delete(ENTITY_API_URL_ID, tenantProfile.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return tenantProfileRepository.count();
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

    protected TenantProfile getPersistedTenantProfile(TenantProfile tenantProfile) {
        return tenantProfileRepository.findById(tenantProfile.getId()).orElseThrow();
    }

    protected void assertPersistedTenantProfileToMatchAllProperties(TenantProfile expectedTenantProfile) {
        assertTenantProfileAllPropertiesEquals(expectedTenantProfile, getPersistedTenantProfile(expectedTenantProfile));
    }

    protected void assertPersistedTenantProfileToMatchUpdatableProperties(TenantProfile expectedTenantProfile) {
        assertTenantProfileAllUpdatablePropertiesEquals(expectedTenantProfile, getPersistedTenantProfile(expectedTenantProfile));
    }
}
