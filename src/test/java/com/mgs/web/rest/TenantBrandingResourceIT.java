package com.mgs.web.rest;

import static com.mgs.domain.TenantBrandingAsserts.*;
import static com.mgs.web.rest.TestUtil.createUpdateProxyForBean;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mgs.IntegrationTest;
import com.mgs.domain.Tenant;
import com.mgs.domain.TenantBranding;
import com.mgs.repository.TenantBrandingRepository;
import com.mgs.service.dto.TenantBrandingDTO;
import com.mgs.service.mapper.TenantBrandingMapper;
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
 * Integration tests for the {@link TenantBrandingResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class TenantBrandingResourceIT {

    private static final String DEFAULT_LOGO_PATH = "AAAAAAAAAA";
    private static final String UPDATED_LOGO_PATH = "BBBBBBBBBB";

    private static final String DEFAULT_LOGO_DARK_PATH = "AAAAAAAAAA";
    private static final String UPDATED_LOGO_DARK_PATH = "BBBBBBBBBB";

    private static final String DEFAULT_FAVICON_PATH = "AAAAAAAAAA";
    private static final String UPDATED_FAVICON_PATH = "BBBBBBBBBB";

    private static final String DEFAULT_PRIMARY_COLOR = "AAAAAAAAAA";
    private static final String UPDATED_PRIMARY_COLOR = "BBBBBBBBBB";

    private static final String DEFAULT_SECONDARY_COLOR = "AAAAAAAAAA";
    private static final String UPDATED_SECONDARY_COLOR = "BBBBBBBBBB";

    private static final String DEFAULT_ACCENT_COLOR = "AAAAAAAAAA";
    private static final String UPDATED_ACCENT_COLOR = "BBBBBBBBBB";

    private static final String DEFAULT_PDF_HEADER_LOGO_PATH = "AAAAAAAAAA";
    private static final String UPDATED_PDF_HEADER_LOGO_PATH = "BBBBBBBBBB";

    private static final String DEFAULT_PDF_FOOTER_TEXT = "AAAAAAAAAA";
    private static final String UPDATED_PDF_FOOTER_TEXT = "BBBBBBBBBB";

    private static final String DEFAULT_PDF_PRIMARY_COLOR = "AAAAAAAAAA";
    private static final String UPDATED_PDF_PRIMARY_COLOR = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/tenant-brandings";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ObjectMapper om;

    @Autowired
    private TenantBrandingRepository tenantBrandingRepository;

    @Autowired
    private TenantBrandingMapper tenantBrandingMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restTenantBrandingMockMvc;

    private TenantBranding tenantBranding;

    private TenantBranding insertedTenantBranding;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static TenantBranding createEntity(EntityManager em) {
        TenantBranding tenantBranding = new TenantBranding()
            .logoPath(DEFAULT_LOGO_PATH)
            .logoDarkPath(DEFAULT_LOGO_DARK_PATH)
            .faviconPath(DEFAULT_FAVICON_PATH)
            .primaryColor(DEFAULT_PRIMARY_COLOR)
            .secondaryColor(DEFAULT_SECONDARY_COLOR)
            .accentColor(DEFAULT_ACCENT_COLOR)
            .pdfHeaderLogoPath(DEFAULT_PDF_HEADER_LOGO_PATH)
            .pdfFooterText(DEFAULT_PDF_FOOTER_TEXT)
            .pdfPrimaryColor(DEFAULT_PDF_PRIMARY_COLOR);
        // Add required entity
        Tenant tenant;
        if (TestUtil.findAll(em, Tenant.class).isEmpty()) {
            tenant = TenantResourceIT.createEntity();
            em.persist(tenant);
            em.flush();
        } else {
            tenant = TestUtil.findAll(em, Tenant.class).get(0);
        }
        tenantBranding.setTenant(tenant);
        return tenantBranding;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static TenantBranding createUpdatedEntity(EntityManager em) {
        TenantBranding updatedTenantBranding = new TenantBranding()
            .logoPath(UPDATED_LOGO_PATH)
            .logoDarkPath(UPDATED_LOGO_DARK_PATH)
            .faviconPath(UPDATED_FAVICON_PATH)
            .primaryColor(UPDATED_PRIMARY_COLOR)
            .secondaryColor(UPDATED_SECONDARY_COLOR)
            .accentColor(UPDATED_ACCENT_COLOR)
            .pdfHeaderLogoPath(UPDATED_PDF_HEADER_LOGO_PATH)
            .pdfFooterText(UPDATED_PDF_FOOTER_TEXT)
            .pdfPrimaryColor(UPDATED_PDF_PRIMARY_COLOR);
        // Add required entity
        Tenant tenant;
        if (TestUtil.findAll(em, Tenant.class).isEmpty()) {
            tenant = TenantResourceIT.createUpdatedEntity();
            em.persist(tenant);
            em.flush();
        } else {
            tenant = TestUtil.findAll(em, Tenant.class).get(0);
        }
        updatedTenantBranding.setTenant(tenant);
        return updatedTenantBranding;
    }

    @BeforeEach
    void initTest() {
        tenantBranding = createEntity(em);
    }

    @AfterEach
    void cleanup() {
        if (insertedTenantBranding != null) {
            tenantBrandingRepository.delete(insertedTenantBranding);
            insertedTenantBranding = null;
        }
    }

    @Test
    @Transactional
    void createTenantBranding() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the TenantBranding
        TenantBrandingDTO tenantBrandingDTO = tenantBrandingMapper.toDto(tenantBranding);
        var returnedTenantBrandingDTO = om.readValue(
            restTenantBrandingMockMvc
                .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(tenantBrandingDTO)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            TenantBrandingDTO.class
        );

        // Validate the TenantBranding in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        var returnedTenantBranding = tenantBrandingMapper.toEntity(returnedTenantBrandingDTO);
        assertTenantBrandingUpdatableFieldsEquals(returnedTenantBranding, getPersistedTenantBranding(returnedTenantBranding));

        insertedTenantBranding = returnedTenantBranding;
    }

    @Test
    @Transactional
    void createTenantBrandingWithExistingId() throws Exception {
        // Create the TenantBranding with an existing ID
        tenantBranding.setId(1L);
        TenantBrandingDTO tenantBrandingDTO = tenantBrandingMapper.toDto(tenantBranding);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restTenantBrandingMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(tenantBrandingDTO)))
            .andExpect(status().isBadRequest());

        // Validate the TenantBranding in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllTenantBrandings() throws Exception {
        // Initialize the database
        insertedTenantBranding = tenantBrandingRepository.saveAndFlush(tenantBranding);

        // Get all the tenantBrandingList
        restTenantBrandingMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(tenantBranding.getId().intValue())))
            .andExpect(jsonPath("$.[*].logoPath").value(hasItem(DEFAULT_LOGO_PATH)))
            .andExpect(jsonPath("$.[*].logoDarkPath").value(hasItem(DEFAULT_LOGO_DARK_PATH)))
            .andExpect(jsonPath("$.[*].faviconPath").value(hasItem(DEFAULT_FAVICON_PATH)))
            .andExpect(jsonPath("$.[*].primaryColor").value(hasItem(DEFAULT_PRIMARY_COLOR)))
            .andExpect(jsonPath("$.[*].secondaryColor").value(hasItem(DEFAULT_SECONDARY_COLOR)))
            .andExpect(jsonPath("$.[*].accentColor").value(hasItem(DEFAULT_ACCENT_COLOR)))
            .andExpect(jsonPath("$.[*].pdfHeaderLogoPath").value(hasItem(DEFAULT_PDF_HEADER_LOGO_PATH)))
            .andExpect(jsonPath("$.[*].pdfFooterText").value(hasItem(DEFAULT_PDF_FOOTER_TEXT)))
            .andExpect(jsonPath("$.[*].pdfPrimaryColor").value(hasItem(DEFAULT_PDF_PRIMARY_COLOR)));
    }

    @Test
    @Transactional
    void getTenantBranding() throws Exception {
        // Initialize the database
        insertedTenantBranding = tenantBrandingRepository.saveAndFlush(tenantBranding);

        // Get the tenantBranding
        restTenantBrandingMockMvc
            .perform(get(ENTITY_API_URL_ID, tenantBranding.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(tenantBranding.getId().intValue()))
            .andExpect(jsonPath("$.logoPath").value(DEFAULT_LOGO_PATH))
            .andExpect(jsonPath("$.logoDarkPath").value(DEFAULT_LOGO_DARK_PATH))
            .andExpect(jsonPath("$.faviconPath").value(DEFAULT_FAVICON_PATH))
            .andExpect(jsonPath("$.primaryColor").value(DEFAULT_PRIMARY_COLOR))
            .andExpect(jsonPath("$.secondaryColor").value(DEFAULT_SECONDARY_COLOR))
            .andExpect(jsonPath("$.accentColor").value(DEFAULT_ACCENT_COLOR))
            .andExpect(jsonPath("$.pdfHeaderLogoPath").value(DEFAULT_PDF_HEADER_LOGO_PATH))
            .andExpect(jsonPath("$.pdfFooterText").value(DEFAULT_PDF_FOOTER_TEXT))
            .andExpect(jsonPath("$.pdfPrimaryColor").value(DEFAULT_PDF_PRIMARY_COLOR));
    }

    @Test
    @Transactional
    void getTenantBrandingsByIdFiltering() throws Exception {
        // Initialize the database
        insertedTenantBranding = tenantBrandingRepository.saveAndFlush(tenantBranding);

        Long id = tenantBranding.getId();

        defaultTenantBrandingFiltering("id.equals=" + id, "id.notEquals=" + id);

        defaultTenantBrandingFiltering("id.greaterThanOrEqual=" + id, "id.greaterThan=" + id);

        defaultTenantBrandingFiltering("id.lessThanOrEqual=" + id, "id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllTenantBrandingsByLogoPathIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedTenantBranding = tenantBrandingRepository.saveAndFlush(tenantBranding);

        // Get all the tenantBrandingList where logoPath equals to
        defaultTenantBrandingFiltering("logoPath.equals=" + DEFAULT_LOGO_PATH, "logoPath.equals=" + UPDATED_LOGO_PATH);
    }

    @Test
    @Transactional
    void getAllTenantBrandingsByLogoPathIsInShouldWork() throws Exception {
        // Initialize the database
        insertedTenantBranding = tenantBrandingRepository.saveAndFlush(tenantBranding);

        // Get all the tenantBrandingList where logoPath in
        defaultTenantBrandingFiltering("logoPath.in=" + DEFAULT_LOGO_PATH + "," + UPDATED_LOGO_PATH, "logoPath.in=" + UPDATED_LOGO_PATH);
    }

    @Test
    @Transactional
    void getAllTenantBrandingsByLogoPathIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedTenantBranding = tenantBrandingRepository.saveAndFlush(tenantBranding);

        // Get all the tenantBrandingList where logoPath is not null
        defaultTenantBrandingFiltering("logoPath.specified=true", "logoPath.specified=false");
    }

    @Test
    @Transactional
    void getAllTenantBrandingsByLogoPathContainsSomething() throws Exception {
        // Initialize the database
        insertedTenantBranding = tenantBrandingRepository.saveAndFlush(tenantBranding);

        // Get all the tenantBrandingList where logoPath contains
        defaultTenantBrandingFiltering("logoPath.contains=" + DEFAULT_LOGO_PATH, "logoPath.contains=" + UPDATED_LOGO_PATH);
    }

    @Test
    @Transactional
    void getAllTenantBrandingsByLogoPathNotContainsSomething() throws Exception {
        // Initialize the database
        insertedTenantBranding = tenantBrandingRepository.saveAndFlush(tenantBranding);

        // Get all the tenantBrandingList where logoPath does not contain
        defaultTenantBrandingFiltering("logoPath.doesNotContain=" + UPDATED_LOGO_PATH, "logoPath.doesNotContain=" + DEFAULT_LOGO_PATH);
    }

    @Test
    @Transactional
    void getAllTenantBrandingsByLogoDarkPathIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedTenantBranding = tenantBrandingRepository.saveAndFlush(tenantBranding);

        // Get all the tenantBrandingList where logoDarkPath equals to
        defaultTenantBrandingFiltering("logoDarkPath.equals=" + DEFAULT_LOGO_DARK_PATH, "logoDarkPath.equals=" + UPDATED_LOGO_DARK_PATH);
    }

    @Test
    @Transactional
    void getAllTenantBrandingsByLogoDarkPathIsInShouldWork() throws Exception {
        // Initialize the database
        insertedTenantBranding = tenantBrandingRepository.saveAndFlush(tenantBranding);

        // Get all the tenantBrandingList where logoDarkPath in
        defaultTenantBrandingFiltering(
            "logoDarkPath.in=" + DEFAULT_LOGO_DARK_PATH + "," + UPDATED_LOGO_DARK_PATH,
            "logoDarkPath.in=" + UPDATED_LOGO_DARK_PATH
        );
    }

    @Test
    @Transactional
    void getAllTenantBrandingsByLogoDarkPathIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedTenantBranding = tenantBrandingRepository.saveAndFlush(tenantBranding);

        // Get all the tenantBrandingList where logoDarkPath is not null
        defaultTenantBrandingFiltering("logoDarkPath.specified=true", "logoDarkPath.specified=false");
    }

    @Test
    @Transactional
    void getAllTenantBrandingsByLogoDarkPathContainsSomething() throws Exception {
        // Initialize the database
        insertedTenantBranding = tenantBrandingRepository.saveAndFlush(tenantBranding);

        // Get all the tenantBrandingList where logoDarkPath contains
        defaultTenantBrandingFiltering(
            "logoDarkPath.contains=" + DEFAULT_LOGO_DARK_PATH,
            "logoDarkPath.contains=" + UPDATED_LOGO_DARK_PATH
        );
    }

    @Test
    @Transactional
    void getAllTenantBrandingsByLogoDarkPathNotContainsSomething() throws Exception {
        // Initialize the database
        insertedTenantBranding = tenantBrandingRepository.saveAndFlush(tenantBranding);

        // Get all the tenantBrandingList where logoDarkPath does not contain
        defaultTenantBrandingFiltering(
            "logoDarkPath.doesNotContain=" + UPDATED_LOGO_DARK_PATH,
            "logoDarkPath.doesNotContain=" + DEFAULT_LOGO_DARK_PATH
        );
    }

    @Test
    @Transactional
    void getAllTenantBrandingsByFaviconPathIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedTenantBranding = tenantBrandingRepository.saveAndFlush(tenantBranding);

        // Get all the tenantBrandingList where faviconPath equals to
        defaultTenantBrandingFiltering("faviconPath.equals=" + DEFAULT_FAVICON_PATH, "faviconPath.equals=" + UPDATED_FAVICON_PATH);
    }

    @Test
    @Transactional
    void getAllTenantBrandingsByFaviconPathIsInShouldWork() throws Exception {
        // Initialize the database
        insertedTenantBranding = tenantBrandingRepository.saveAndFlush(tenantBranding);

        // Get all the tenantBrandingList where faviconPath in
        defaultTenantBrandingFiltering(
            "faviconPath.in=" + DEFAULT_FAVICON_PATH + "," + UPDATED_FAVICON_PATH,
            "faviconPath.in=" + UPDATED_FAVICON_PATH
        );
    }

    @Test
    @Transactional
    void getAllTenantBrandingsByFaviconPathIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedTenantBranding = tenantBrandingRepository.saveAndFlush(tenantBranding);

        // Get all the tenantBrandingList where faviconPath is not null
        defaultTenantBrandingFiltering("faviconPath.specified=true", "faviconPath.specified=false");
    }

    @Test
    @Transactional
    void getAllTenantBrandingsByFaviconPathContainsSomething() throws Exception {
        // Initialize the database
        insertedTenantBranding = tenantBrandingRepository.saveAndFlush(tenantBranding);

        // Get all the tenantBrandingList where faviconPath contains
        defaultTenantBrandingFiltering("faviconPath.contains=" + DEFAULT_FAVICON_PATH, "faviconPath.contains=" + UPDATED_FAVICON_PATH);
    }

    @Test
    @Transactional
    void getAllTenantBrandingsByFaviconPathNotContainsSomething() throws Exception {
        // Initialize the database
        insertedTenantBranding = tenantBrandingRepository.saveAndFlush(tenantBranding);

        // Get all the tenantBrandingList where faviconPath does not contain
        defaultTenantBrandingFiltering(
            "faviconPath.doesNotContain=" + UPDATED_FAVICON_PATH,
            "faviconPath.doesNotContain=" + DEFAULT_FAVICON_PATH
        );
    }

    @Test
    @Transactional
    void getAllTenantBrandingsByPrimaryColorIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedTenantBranding = tenantBrandingRepository.saveAndFlush(tenantBranding);

        // Get all the tenantBrandingList where primaryColor equals to
        defaultTenantBrandingFiltering("primaryColor.equals=" + DEFAULT_PRIMARY_COLOR, "primaryColor.equals=" + UPDATED_PRIMARY_COLOR);
    }

    @Test
    @Transactional
    void getAllTenantBrandingsByPrimaryColorIsInShouldWork() throws Exception {
        // Initialize the database
        insertedTenantBranding = tenantBrandingRepository.saveAndFlush(tenantBranding);

        // Get all the tenantBrandingList where primaryColor in
        defaultTenantBrandingFiltering(
            "primaryColor.in=" + DEFAULT_PRIMARY_COLOR + "," + UPDATED_PRIMARY_COLOR,
            "primaryColor.in=" + UPDATED_PRIMARY_COLOR
        );
    }

    @Test
    @Transactional
    void getAllTenantBrandingsByPrimaryColorIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedTenantBranding = tenantBrandingRepository.saveAndFlush(tenantBranding);

        // Get all the tenantBrandingList where primaryColor is not null
        defaultTenantBrandingFiltering("primaryColor.specified=true", "primaryColor.specified=false");
    }

    @Test
    @Transactional
    void getAllTenantBrandingsByPrimaryColorContainsSomething() throws Exception {
        // Initialize the database
        insertedTenantBranding = tenantBrandingRepository.saveAndFlush(tenantBranding);

        // Get all the tenantBrandingList where primaryColor contains
        defaultTenantBrandingFiltering("primaryColor.contains=" + DEFAULT_PRIMARY_COLOR, "primaryColor.contains=" + UPDATED_PRIMARY_COLOR);
    }

    @Test
    @Transactional
    void getAllTenantBrandingsByPrimaryColorNotContainsSomething() throws Exception {
        // Initialize the database
        insertedTenantBranding = tenantBrandingRepository.saveAndFlush(tenantBranding);

        // Get all the tenantBrandingList where primaryColor does not contain
        defaultTenantBrandingFiltering(
            "primaryColor.doesNotContain=" + UPDATED_PRIMARY_COLOR,
            "primaryColor.doesNotContain=" + DEFAULT_PRIMARY_COLOR
        );
    }

    @Test
    @Transactional
    void getAllTenantBrandingsBySecondaryColorIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedTenantBranding = tenantBrandingRepository.saveAndFlush(tenantBranding);

        // Get all the tenantBrandingList where secondaryColor equals to
        defaultTenantBrandingFiltering(
            "secondaryColor.equals=" + DEFAULT_SECONDARY_COLOR,
            "secondaryColor.equals=" + UPDATED_SECONDARY_COLOR
        );
    }

    @Test
    @Transactional
    void getAllTenantBrandingsBySecondaryColorIsInShouldWork() throws Exception {
        // Initialize the database
        insertedTenantBranding = tenantBrandingRepository.saveAndFlush(tenantBranding);

        // Get all the tenantBrandingList where secondaryColor in
        defaultTenantBrandingFiltering(
            "secondaryColor.in=" + DEFAULT_SECONDARY_COLOR + "," + UPDATED_SECONDARY_COLOR,
            "secondaryColor.in=" + UPDATED_SECONDARY_COLOR
        );
    }

    @Test
    @Transactional
    void getAllTenantBrandingsBySecondaryColorIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedTenantBranding = tenantBrandingRepository.saveAndFlush(tenantBranding);

        // Get all the tenantBrandingList where secondaryColor is not null
        defaultTenantBrandingFiltering("secondaryColor.specified=true", "secondaryColor.specified=false");
    }

    @Test
    @Transactional
    void getAllTenantBrandingsBySecondaryColorContainsSomething() throws Exception {
        // Initialize the database
        insertedTenantBranding = tenantBrandingRepository.saveAndFlush(tenantBranding);

        // Get all the tenantBrandingList where secondaryColor contains
        defaultTenantBrandingFiltering(
            "secondaryColor.contains=" + DEFAULT_SECONDARY_COLOR,
            "secondaryColor.contains=" + UPDATED_SECONDARY_COLOR
        );
    }

    @Test
    @Transactional
    void getAllTenantBrandingsBySecondaryColorNotContainsSomething() throws Exception {
        // Initialize the database
        insertedTenantBranding = tenantBrandingRepository.saveAndFlush(tenantBranding);

        // Get all the tenantBrandingList where secondaryColor does not contain
        defaultTenantBrandingFiltering(
            "secondaryColor.doesNotContain=" + UPDATED_SECONDARY_COLOR,
            "secondaryColor.doesNotContain=" + DEFAULT_SECONDARY_COLOR
        );
    }

    @Test
    @Transactional
    void getAllTenantBrandingsByAccentColorIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedTenantBranding = tenantBrandingRepository.saveAndFlush(tenantBranding);

        // Get all the tenantBrandingList where accentColor equals to
        defaultTenantBrandingFiltering("accentColor.equals=" + DEFAULT_ACCENT_COLOR, "accentColor.equals=" + UPDATED_ACCENT_COLOR);
    }

    @Test
    @Transactional
    void getAllTenantBrandingsByAccentColorIsInShouldWork() throws Exception {
        // Initialize the database
        insertedTenantBranding = tenantBrandingRepository.saveAndFlush(tenantBranding);

        // Get all the tenantBrandingList where accentColor in
        defaultTenantBrandingFiltering(
            "accentColor.in=" + DEFAULT_ACCENT_COLOR + "," + UPDATED_ACCENT_COLOR,
            "accentColor.in=" + UPDATED_ACCENT_COLOR
        );
    }

    @Test
    @Transactional
    void getAllTenantBrandingsByAccentColorIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedTenantBranding = tenantBrandingRepository.saveAndFlush(tenantBranding);

        // Get all the tenantBrandingList where accentColor is not null
        defaultTenantBrandingFiltering("accentColor.specified=true", "accentColor.specified=false");
    }

    @Test
    @Transactional
    void getAllTenantBrandingsByAccentColorContainsSomething() throws Exception {
        // Initialize the database
        insertedTenantBranding = tenantBrandingRepository.saveAndFlush(tenantBranding);

        // Get all the tenantBrandingList where accentColor contains
        defaultTenantBrandingFiltering("accentColor.contains=" + DEFAULT_ACCENT_COLOR, "accentColor.contains=" + UPDATED_ACCENT_COLOR);
    }

    @Test
    @Transactional
    void getAllTenantBrandingsByAccentColorNotContainsSomething() throws Exception {
        // Initialize the database
        insertedTenantBranding = tenantBrandingRepository.saveAndFlush(tenantBranding);

        // Get all the tenantBrandingList where accentColor does not contain
        defaultTenantBrandingFiltering(
            "accentColor.doesNotContain=" + UPDATED_ACCENT_COLOR,
            "accentColor.doesNotContain=" + DEFAULT_ACCENT_COLOR
        );
    }

    @Test
    @Transactional
    void getAllTenantBrandingsByPdfHeaderLogoPathIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedTenantBranding = tenantBrandingRepository.saveAndFlush(tenantBranding);

        // Get all the tenantBrandingList where pdfHeaderLogoPath equals to
        defaultTenantBrandingFiltering(
            "pdfHeaderLogoPath.equals=" + DEFAULT_PDF_HEADER_LOGO_PATH,
            "pdfHeaderLogoPath.equals=" + UPDATED_PDF_HEADER_LOGO_PATH
        );
    }

    @Test
    @Transactional
    void getAllTenantBrandingsByPdfHeaderLogoPathIsInShouldWork() throws Exception {
        // Initialize the database
        insertedTenantBranding = tenantBrandingRepository.saveAndFlush(tenantBranding);

        // Get all the tenantBrandingList where pdfHeaderLogoPath in
        defaultTenantBrandingFiltering(
            "pdfHeaderLogoPath.in=" + DEFAULT_PDF_HEADER_LOGO_PATH + "," + UPDATED_PDF_HEADER_LOGO_PATH,
            "pdfHeaderLogoPath.in=" + UPDATED_PDF_HEADER_LOGO_PATH
        );
    }

    @Test
    @Transactional
    void getAllTenantBrandingsByPdfHeaderLogoPathIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedTenantBranding = tenantBrandingRepository.saveAndFlush(tenantBranding);

        // Get all the tenantBrandingList where pdfHeaderLogoPath is not null
        defaultTenantBrandingFiltering("pdfHeaderLogoPath.specified=true", "pdfHeaderLogoPath.specified=false");
    }

    @Test
    @Transactional
    void getAllTenantBrandingsByPdfHeaderLogoPathContainsSomething() throws Exception {
        // Initialize the database
        insertedTenantBranding = tenantBrandingRepository.saveAndFlush(tenantBranding);

        // Get all the tenantBrandingList where pdfHeaderLogoPath contains
        defaultTenantBrandingFiltering(
            "pdfHeaderLogoPath.contains=" + DEFAULT_PDF_HEADER_LOGO_PATH,
            "pdfHeaderLogoPath.contains=" + UPDATED_PDF_HEADER_LOGO_PATH
        );
    }

    @Test
    @Transactional
    void getAllTenantBrandingsByPdfHeaderLogoPathNotContainsSomething() throws Exception {
        // Initialize the database
        insertedTenantBranding = tenantBrandingRepository.saveAndFlush(tenantBranding);

        // Get all the tenantBrandingList where pdfHeaderLogoPath does not contain
        defaultTenantBrandingFiltering(
            "pdfHeaderLogoPath.doesNotContain=" + UPDATED_PDF_HEADER_LOGO_PATH,
            "pdfHeaderLogoPath.doesNotContain=" + DEFAULT_PDF_HEADER_LOGO_PATH
        );
    }

    @Test
    @Transactional
    void getAllTenantBrandingsByPdfFooterTextIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedTenantBranding = tenantBrandingRepository.saveAndFlush(tenantBranding);

        // Get all the tenantBrandingList where pdfFooterText equals to
        defaultTenantBrandingFiltering(
            "pdfFooterText.equals=" + DEFAULT_PDF_FOOTER_TEXT,
            "pdfFooterText.equals=" + UPDATED_PDF_FOOTER_TEXT
        );
    }

    @Test
    @Transactional
    void getAllTenantBrandingsByPdfFooterTextIsInShouldWork() throws Exception {
        // Initialize the database
        insertedTenantBranding = tenantBrandingRepository.saveAndFlush(tenantBranding);

        // Get all the tenantBrandingList where pdfFooterText in
        defaultTenantBrandingFiltering(
            "pdfFooterText.in=" + DEFAULT_PDF_FOOTER_TEXT + "," + UPDATED_PDF_FOOTER_TEXT,
            "pdfFooterText.in=" + UPDATED_PDF_FOOTER_TEXT
        );
    }

    @Test
    @Transactional
    void getAllTenantBrandingsByPdfFooterTextIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedTenantBranding = tenantBrandingRepository.saveAndFlush(tenantBranding);

        // Get all the tenantBrandingList where pdfFooterText is not null
        defaultTenantBrandingFiltering("pdfFooterText.specified=true", "pdfFooterText.specified=false");
    }

    @Test
    @Transactional
    void getAllTenantBrandingsByPdfFooterTextContainsSomething() throws Exception {
        // Initialize the database
        insertedTenantBranding = tenantBrandingRepository.saveAndFlush(tenantBranding);

        // Get all the tenantBrandingList where pdfFooterText contains
        defaultTenantBrandingFiltering(
            "pdfFooterText.contains=" + DEFAULT_PDF_FOOTER_TEXT,
            "pdfFooterText.contains=" + UPDATED_PDF_FOOTER_TEXT
        );
    }

    @Test
    @Transactional
    void getAllTenantBrandingsByPdfFooterTextNotContainsSomething() throws Exception {
        // Initialize the database
        insertedTenantBranding = tenantBrandingRepository.saveAndFlush(tenantBranding);

        // Get all the tenantBrandingList where pdfFooterText does not contain
        defaultTenantBrandingFiltering(
            "pdfFooterText.doesNotContain=" + UPDATED_PDF_FOOTER_TEXT,
            "pdfFooterText.doesNotContain=" + DEFAULT_PDF_FOOTER_TEXT
        );
    }

    @Test
    @Transactional
    void getAllTenantBrandingsByPdfPrimaryColorIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedTenantBranding = tenantBrandingRepository.saveAndFlush(tenantBranding);

        // Get all the tenantBrandingList where pdfPrimaryColor equals to
        defaultTenantBrandingFiltering(
            "pdfPrimaryColor.equals=" + DEFAULT_PDF_PRIMARY_COLOR,
            "pdfPrimaryColor.equals=" + UPDATED_PDF_PRIMARY_COLOR
        );
    }

    @Test
    @Transactional
    void getAllTenantBrandingsByPdfPrimaryColorIsInShouldWork() throws Exception {
        // Initialize the database
        insertedTenantBranding = tenantBrandingRepository.saveAndFlush(tenantBranding);

        // Get all the tenantBrandingList where pdfPrimaryColor in
        defaultTenantBrandingFiltering(
            "pdfPrimaryColor.in=" + DEFAULT_PDF_PRIMARY_COLOR + "," + UPDATED_PDF_PRIMARY_COLOR,
            "pdfPrimaryColor.in=" + UPDATED_PDF_PRIMARY_COLOR
        );
    }

    @Test
    @Transactional
    void getAllTenantBrandingsByPdfPrimaryColorIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedTenantBranding = tenantBrandingRepository.saveAndFlush(tenantBranding);

        // Get all the tenantBrandingList where pdfPrimaryColor is not null
        defaultTenantBrandingFiltering("pdfPrimaryColor.specified=true", "pdfPrimaryColor.specified=false");
    }

    @Test
    @Transactional
    void getAllTenantBrandingsByPdfPrimaryColorContainsSomething() throws Exception {
        // Initialize the database
        insertedTenantBranding = tenantBrandingRepository.saveAndFlush(tenantBranding);

        // Get all the tenantBrandingList where pdfPrimaryColor contains
        defaultTenantBrandingFiltering(
            "pdfPrimaryColor.contains=" + DEFAULT_PDF_PRIMARY_COLOR,
            "pdfPrimaryColor.contains=" + UPDATED_PDF_PRIMARY_COLOR
        );
    }

    @Test
    @Transactional
    void getAllTenantBrandingsByPdfPrimaryColorNotContainsSomething() throws Exception {
        // Initialize the database
        insertedTenantBranding = tenantBrandingRepository.saveAndFlush(tenantBranding);

        // Get all the tenantBrandingList where pdfPrimaryColor does not contain
        defaultTenantBrandingFiltering(
            "pdfPrimaryColor.doesNotContain=" + UPDATED_PDF_PRIMARY_COLOR,
            "pdfPrimaryColor.doesNotContain=" + DEFAULT_PDF_PRIMARY_COLOR
        );
    }

    @Test
    @Transactional
    void getAllTenantBrandingsByTenantIsEqualToSomething() throws Exception {
        // Get already existing entity
        Tenant tenant = tenantBranding.getTenant();
        tenantBrandingRepository.saveAndFlush(tenantBranding);
        Long tenantId = tenant.getId();
        // Get all the tenantBrandingList where tenant equals to tenantId
        defaultTenantBrandingShouldBeFound("tenantId.equals=" + tenantId);

        // Get all the tenantBrandingList where tenant equals to (tenantId + 1)
        defaultTenantBrandingShouldNotBeFound("tenantId.equals=" + (tenantId + 1));
    }

    private void defaultTenantBrandingFiltering(String shouldBeFound, String shouldNotBeFound) throws Exception {
        defaultTenantBrandingShouldBeFound(shouldBeFound);
        defaultTenantBrandingShouldNotBeFound(shouldNotBeFound);
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultTenantBrandingShouldBeFound(String filter) throws Exception {
        restTenantBrandingMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(tenantBranding.getId().intValue())))
            .andExpect(jsonPath("$.[*].logoPath").value(hasItem(DEFAULT_LOGO_PATH)))
            .andExpect(jsonPath("$.[*].logoDarkPath").value(hasItem(DEFAULT_LOGO_DARK_PATH)))
            .andExpect(jsonPath("$.[*].faviconPath").value(hasItem(DEFAULT_FAVICON_PATH)))
            .andExpect(jsonPath("$.[*].primaryColor").value(hasItem(DEFAULT_PRIMARY_COLOR)))
            .andExpect(jsonPath("$.[*].secondaryColor").value(hasItem(DEFAULT_SECONDARY_COLOR)))
            .andExpect(jsonPath("$.[*].accentColor").value(hasItem(DEFAULT_ACCENT_COLOR)))
            .andExpect(jsonPath("$.[*].pdfHeaderLogoPath").value(hasItem(DEFAULT_PDF_HEADER_LOGO_PATH)))
            .andExpect(jsonPath("$.[*].pdfFooterText").value(hasItem(DEFAULT_PDF_FOOTER_TEXT)))
            .andExpect(jsonPath("$.[*].pdfPrimaryColor").value(hasItem(DEFAULT_PDF_PRIMARY_COLOR)));

        // Check, that the count call also returns 1
        restTenantBrandingMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultTenantBrandingShouldNotBeFound(String filter) throws Exception {
        restTenantBrandingMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restTenantBrandingMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingTenantBranding() throws Exception {
        // Get the tenantBranding
        restTenantBrandingMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingTenantBranding() throws Exception {
        // Initialize the database
        insertedTenantBranding = tenantBrandingRepository.saveAndFlush(tenantBranding);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the tenantBranding
        TenantBranding updatedTenantBranding = tenantBrandingRepository.findById(tenantBranding.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedTenantBranding are not directly saved in db
        em.detach(updatedTenantBranding);
        updatedTenantBranding
            .logoPath(UPDATED_LOGO_PATH)
            .logoDarkPath(UPDATED_LOGO_DARK_PATH)
            .faviconPath(UPDATED_FAVICON_PATH)
            .primaryColor(UPDATED_PRIMARY_COLOR)
            .secondaryColor(UPDATED_SECONDARY_COLOR)
            .accentColor(UPDATED_ACCENT_COLOR)
            .pdfHeaderLogoPath(UPDATED_PDF_HEADER_LOGO_PATH)
            .pdfFooterText(UPDATED_PDF_FOOTER_TEXT)
            .pdfPrimaryColor(UPDATED_PDF_PRIMARY_COLOR);
        TenantBrandingDTO tenantBrandingDTO = tenantBrandingMapper.toDto(updatedTenantBranding);

        restTenantBrandingMockMvc
            .perform(
                put(ENTITY_API_URL_ID, tenantBrandingDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(tenantBrandingDTO))
            )
            .andExpect(status().isOk());

        // Validate the TenantBranding in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedTenantBrandingToMatchAllProperties(updatedTenantBranding);
    }

    @Test
    @Transactional
    void putNonExistingTenantBranding() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        tenantBranding.setId(longCount.incrementAndGet());

        // Create the TenantBranding
        TenantBrandingDTO tenantBrandingDTO = tenantBrandingMapper.toDto(tenantBranding);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restTenantBrandingMockMvc
            .perform(
                put(ENTITY_API_URL_ID, tenantBrandingDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(tenantBrandingDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the TenantBranding in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchTenantBranding() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        tenantBranding.setId(longCount.incrementAndGet());

        // Create the TenantBranding
        TenantBrandingDTO tenantBrandingDTO = tenantBrandingMapper.toDto(tenantBranding);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restTenantBrandingMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(tenantBrandingDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the TenantBranding in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamTenantBranding() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        tenantBranding.setId(longCount.incrementAndGet());

        // Create the TenantBranding
        TenantBrandingDTO tenantBrandingDTO = tenantBrandingMapper.toDto(tenantBranding);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restTenantBrandingMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(tenantBrandingDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the TenantBranding in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateTenantBrandingWithPatch() throws Exception {
        // Initialize the database
        insertedTenantBranding = tenantBrandingRepository.saveAndFlush(tenantBranding);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the tenantBranding using partial update
        TenantBranding partialUpdatedTenantBranding = new TenantBranding();
        partialUpdatedTenantBranding.setId(tenantBranding.getId());

        partialUpdatedTenantBranding.logoPath(UPDATED_LOGO_PATH).faviconPath(UPDATED_FAVICON_PATH).accentColor(UPDATED_ACCENT_COLOR);

        restTenantBrandingMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedTenantBranding.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedTenantBranding))
            )
            .andExpect(status().isOk());

        // Validate the TenantBranding in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertTenantBrandingUpdatableFieldsEquals(
            createUpdateProxyForBean(partialUpdatedTenantBranding, tenantBranding),
            getPersistedTenantBranding(tenantBranding)
        );
    }

    @Test
    @Transactional
    void fullUpdateTenantBrandingWithPatch() throws Exception {
        // Initialize the database
        insertedTenantBranding = tenantBrandingRepository.saveAndFlush(tenantBranding);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the tenantBranding using partial update
        TenantBranding partialUpdatedTenantBranding = new TenantBranding();
        partialUpdatedTenantBranding.setId(tenantBranding.getId());

        partialUpdatedTenantBranding
            .logoPath(UPDATED_LOGO_PATH)
            .logoDarkPath(UPDATED_LOGO_DARK_PATH)
            .faviconPath(UPDATED_FAVICON_PATH)
            .primaryColor(UPDATED_PRIMARY_COLOR)
            .secondaryColor(UPDATED_SECONDARY_COLOR)
            .accentColor(UPDATED_ACCENT_COLOR)
            .pdfHeaderLogoPath(UPDATED_PDF_HEADER_LOGO_PATH)
            .pdfFooterText(UPDATED_PDF_FOOTER_TEXT)
            .pdfPrimaryColor(UPDATED_PDF_PRIMARY_COLOR);

        restTenantBrandingMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedTenantBranding.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedTenantBranding))
            )
            .andExpect(status().isOk());

        // Validate the TenantBranding in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertTenantBrandingUpdatableFieldsEquals(partialUpdatedTenantBranding, getPersistedTenantBranding(partialUpdatedTenantBranding));
    }

    @Test
    @Transactional
    void patchNonExistingTenantBranding() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        tenantBranding.setId(longCount.incrementAndGet());

        // Create the TenantBranding
        TenantBrandingDTO tenantBrandingDTO = tenantBrandingMapper.toDto(tenantBranding);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restTenantBrandingMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, tenantBrandingDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(tenantBrandingDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the TenantBranding in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchTenantBranding() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        tenantBranding.setId(longCount.incrementAndGet());

        // Create the TenantBranding
        TenantBrandingDTO tenantBrandingDTO = tenantBrandingMapper.toDto(tenantBranding);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restTenantBrandingMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(tenantBrandingDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the TenantBranding in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamTenantBranding() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        tenantBranding.setId(longCount.incrementAndGet());

        // Create the TenantBranding
        TenantBrandingDTO tenantBrandingDTO = tenantBrandingMapper.toDto(tenantBranding);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restTenantBrandingMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(om.writeValueAsBytes(tenantBrandingDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the TenantBranding in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteTenantBranding() throws Exception {
        // Initialize the database
        insertedTenantBranding = tenantBrandingRepository.saveAndFlush(tenantBranding);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the tenantBranding
        restTenantBrandingMockMvc
            .perform(delete(ENTITY_API_URL_ID, tenantBranding.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return tenantBrandingRepository.count();
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

    protected TenantBranding getPersistedTenantBranding(TenantBranding tenantBranding) {
        return tenantBrandingRepository.findById(tenantBranding.getId()).orElseThrow();
    }

    protected void assertPersistedTenantBrandingToMatchAllProperties(TenantBranding expectedTenantBranding) {
        assertTenantBrandingAllPropertiesEquals(expectedTenantBranding, getPersistedTenantBranding(expectedTenantBranding));
    }

    protected void assertPersistedTenantBrandingToMatchUpdatableProperties(TenantBranding expectedTenantBranding) {
        assertTenantBrandingAllUpdatablePropertiesEquals(expectedTenantBranding, getPersistedTenantBranding(expectedTenantBranding));
    }
}
