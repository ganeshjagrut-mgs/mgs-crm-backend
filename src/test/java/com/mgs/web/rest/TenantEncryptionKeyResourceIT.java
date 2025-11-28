package com.mgs.web.rest;

import static com.mgs.domain.TenantEncryptionKeyAsserts.*;
import static com.mgs.web.rest.TestUtil.createUpdateProxyForBean;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mgs.IntegrationTest;
import com.mgs.domain.Tenant;
import com.mgs.domain.TenantEncryptionKey;
import com.mgs.repository.TenantEncryptionKeyRepository;
import com.mgs.service.dto.TenantEncryptionKeyDTO;
import com.mgs.service.mapper.TenantEncryptionKeyMapper;
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
 * Integration tests for the {@link TenantEncryptionKeyResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class TenantEncryptionKeyResourceIT {

    private static final Integer DEFAULT_KEY_VERSION = 1;
    private static final Integer UPDATED_KEY_VERSION = 2;
    private static final Integer SMALLER_KEY_VERSION = 1 - 1;

    private static final String DEFAULT_ENCRYPTED_DATA_KEY = "AAAAAAAAAA";
    private static final String UPDATED_ENCRYPTED_DATA_KEY = "BBBBBBBBBB";

    private static final String DEFAULT_PIN_HASH = "AAAAAAAAAA";
    private static final String UPDATED_PIN_HASH = "BBBBBBBBBB";

    private static final String DEFAULT_PIN_SALT = "AAAAAAAAAA";
    private static final String UPDATED_PIN_SALT = "BBBBBBBBBB";

    private static final Boolean DEFAULT_IS_ACTIVE = false;
    private static final Boolean UPDATED_IS_ACTIVE = true;

    private static final String ENTITY_API_URL = "/api/tenant-encryption-keys";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ObjectMapper om;

    @Autowired
    private TenantEncryptionKeyRepository tenantEncryptionKeyRepository;

    @Autowired
    private TenantEncryptionKeyMapper tenantEncryptionKeyMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restTenantEncryptionKeyMockMvc;

    private TenantEncryptionKey tenantEncryptionKey;

    private TenantEncryptionKey insertedTenantEncryptionKey;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static TenantEncryptionKey createEntity(EntityManager em) {
        TenantEncryptionKey tenantEncryptionKey = new TenantEncryptionKey()
            .keyVersion(DEFAULT_KEY_VERSION)
            .encryptedDataKey(DEFAULT_ENCRYPTED_DATA_KEY)
            .pinHash(DEFAULT_PIN_HASH)
            .pinSalt(DEFAULT_PIN_SALT)
            .isActive(DEFAULT_IS_ACTIVE);
        // Add required entity
        Tenant tenant;
        if (TestUtil.findAll(em, Tenant.class).isEmpty()) {
            tenant = TenantResourceIT.createEntity();
            em.persist(tenant);
            em.flush();
        } else {
            tenant = TestUtil.findAll(em, Tenant.class).get(0);
        }
        tenantEncryptionKey.setTenant(tenant);
        return tenantEncryptionKey;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static TenantEncryptionKey createUpdatedEntity(EntityManager em) {
        TenantEncryptionKey updatedTenantEncryptionKey = new TenantEncryptionKey()
            .keyVersion(UPDATED_KEY_VERSION)
            .encryptedDataKey(UPDATED_ENCRYPTED_DATA_KEY)
            .pinHash(UPDATED_PIN_HASH)
            .pinSalt(UPDATED_PIN_SALT)
            .isActive(UPDATED_IS_ACTIVE);
        // Add required entity
        Tenant tenant;
        if (TestUtil.findAll(em, Tenant.class).isEmpty()) {
            tenant = TenantResourceIT.createUpdatedEntity();
            em.persist(tenant);
            em.flush();
        } else {
            tenant = TestUtil.findAll(em, Tenant.class).get(0);
        }
        updatedTenantEncryptionKey.setTenant(tenant);
        return updatedTenantEncryptionKey;
    }

    @BeforeEach
    void initTest() {
        tenantEncryptionKey = createEntity(em);
    }

    @AfterEach
    void cleanup() {
        if (insertedTenantEncryptionKey != null) {
            tenantEncryptionKeyRepository.delete(insertedTenantEncryptionKey);
            insertedTenantEncryptionKey = null;
        }
    }

    @Test
    @Transactional
    void createTenantEncryptionKey() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the TenantEncryptionKey
        TenantEncryptionKeyDTO tenantEncryptionKeyDTO = tenantEncryptionKeyMapper.toDto(tenantEncryptionKey);
        var returnedTenantEncryptionKeyDTO = om.readValue(
            restTenantEncryptionKeyMockMvc
                .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(tenantEncryptionKeyDTO)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            TenantEncryptionKeyDTO.class
        );

        // Validate the TenantEncryptionKey in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        var returnedTenantEncryptionKey = tenantEncryptionKeyMapper.toEntity(returnedTenantEncryptionKeyDTO);
        assertTenantEncryptionKeyUpdatableFieldsEquals(
            returnedTenantEncryptionKey,
            getPersistedTenantEncryptionKey(returnedTenantEncryptionKey)
        );

        insertedTenantEncryptionKey = returnedTenantEncryptionKey;
    }

    @Test
    @Transactional
    void createTenantEncryptionKeyWithExistingId() throws Exception {
        // Create the TenantEncryptionKey with an existing ID
        tenantEncryptionKey.setId(1L);
        TenantEncryptionKeyDTO tenantEncryptionKeyDTO = tenantEncryptionKeyMapper.toDto(tenantEncryptionKey);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restTenantEncryptionKeyMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(tenantEncryptionKeyDTO)))
            .andExpect(status().isBadRequest());

        // Validate the TenantEncryptionKey in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkKeyVersionIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        tenantEncryptionKey.setKeyVersion(null);

        // Create the TenantEncryptionKey, which fails.
        TenantEncryptionKeyDTO tenantEncryptionKeyDTO = tenantEncryptionKeyMapper.toDto(tenantEncryptionKey);

        restTenantEncryptionKeyMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(tenantEncryptionKeyDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkEncryptedDataKeyIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        tenantEncryptionKey.setEncryptedDataKey(null);

        // Create the TenantEncryptionKey, which fails.
        TenantEncryptionKeyDTO tenantEncryptionKeyDTO = tenantEncryptionKeyMapper.toDto(tenantEncryptionKey);

        restTenantEncryptionKeyMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(tenantEncryptionKeyDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkIsActiveIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        tenantEncryptionKey.setIsActive(null);

        // Create the TenantEncryptionKey, which fails.
        TenantEncryptionKeyDTO tenantEncryptionKeyDTO = tenantEncryptionKeyMapper.toDto(tenantEncryptionKey);

        restTenantEncryptionKeyMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(tenantEncryptionKeyDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllTenantEncryptionKeys() throws Exception {
        // Initialize the database
        insertedTenantEncryptionKey = tenantEncryptionKeyRepository.saveAndFlush(tenantEncryptionKey);

        // Get all the tenantEncryptionKeyList
        restTenantEncryptionKeyMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(tenantEncryptionKey.getId().intValue())))
            .andExpect(jsonPath("$.[*].keyVersion").value(hasItem(DEFAULT_KEY_VERSION)))
            .andExpect(jsonPath("$.[*].encryptedDataKey").value(hasItem(DEFAULT_ENCRYPTED_DATA_KEY)))
            .andExpect(jsonPath("$.[*].pinHash").value(hasItem(DEFAULT_PIN_HASH)))
            .andExpect(jsonPath("$.[*].pinSalt").value(hasItem(DEFAULT_PIN_SALT)))
            .andExpect(jsonPath("$.[*].isActive").value(hasItem(DEFAULT_IS_ACTIVE)));
    }

    @Test
    @Transactional
    void getTenantEncryptionKey() throws Exception {
        // Initialize the database
        insertedTenantEncryptionKey = tenantEncryptionKeyRepository.saveAndFlush(tenantEncryptionKey);

        // Get the tenantEncryptionKey
        restTenantEncryptionKeyMockMvc
            .perform(get(ENTITY_API_URL_ID, tenantEncryptionKey.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(tenantEncryptionKey.getId().intValue()))
            .andExpect(jsonPath("$.keyVersion").value(DEFAULT_KEY_VERSION))
            .andExpect(jsonPath("$.encryptedDataKey").value(DEFAULT_ENCRYPTED_DATA_KEY))
            .andExpect(jsonPath("$.pinHash").value(DEFAULT_PIN_HASH))
            .andExpect(jsonPath("$.pinSalt").value(DEFAULT_PIN_SALT))
            .andExpect(jsonPath("$.isActive").value(DEFAULT_IS_ACTIVE));
    }

    @Test
    @Transactional
    void getTenantEncryptionKeysByIdFiltering() throws Exception {
        // Initialize the database
        insertedTenantEncryptionKey = tenantEncryptionKeyRepository.saveAndFlush(tenantEncryptionKey);

        Long id = tenantEncryptionKey.getId();

        defaultTenantEncryptionKeyFiltering("id.equals=" + id, "id.notEquals=" + id);

        defaultTenantEncryptionKeyFiltering("id.greaterThanOrEqual=" + id, "id.greaterThan=" + id);

        defaultTenantEncryptionKeyFiltering("id.lessThanOrEqual=" + id, "id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllTenantEncryptionKeysByKeyVersionIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedTenantEncryptionKey = tenantEncryptionKeyRepository.saveAndFlush(tenantEncryptionKey);

        // Get all the tenantEncryptionKeyList where keyVersion equals to
        defaultTenantEncryptionKeyFiltering("keyVersion.equals=" + DEFAULT_KEY_VERSION, "keyVersion.equals=" + UPDATED_KEY_VERSION);
    }

    @Test
    @Transactional
    void getAllTenantEncryptionKeysByKeyVersionIsInShouldWork() throws Exception {
        // Initialize the database
        insertedTenantEncryptionKey = tenantEncryptionKeyRepository.saveAndFlush(tenantEncryptionKey);

        // Get all the tenantEncryptionKeyList where keyVersion in
        defaultTenantEncryptionKeyFiltering(
            "keyVersion.in=" + DEFAULT_KEY_VERSION + "," + UPDATED_KEY_VERSION,
            "keyVersion.in=" + UPDATED_KEY_VERSION
        );
    }

    @Test
    @Transactional
    void getAllTenantEncryptionKeysByKeyVersionIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedTenantEncryptionKey = tenantEncryptionKeyRepository.saveAndFlush(tenantEncryptionKey);

        // Get all the tenantEncryptionKeyList where keyVersion is not null
        defaultTenantEncryptionKeyFiltering("keyVersion.specified=true", "keyVersion.specified=false");
    }

    @Test
    @Transactional
    void getAllTenantEncryptionKeysByKeyVersionIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedTenantEncryptionKey = tenantEncryptionKeyRepository.saveAndFlush(tenantEncryptionKey);

        // Get all the tenantEncryptionKeyList where keyVersion is greater than or equal to
        defaultTenantEncryptionKeyFiltering(
            "keyVersion.greaterThanOrEqual=" + DEFAULT_KEY_VERSION,
            "keyVersion.greaterThanOrEqual=" + UPDATED_KEY_VERSION
        );
    }

    @Test
    @Transactional
    void getAllTenantEncryptionKeysByKeyVersionIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedTenantEncryptionKey = tenantEncryptionKeyRepository.saveAndFlush(tenantEncryptionKey);

        // Get all the tenantEncryptionKeyList where keyVersion is less than or equal to
        defaultTenantEncryptionKeyFiltering(
            "keyVersion.lessThanOrEqual=" + DEFAULT_KEY_VERSION,
            "keyVersion.lessThanOrEqual=" + SMALLER_KEY_VERSION
        );
    }

    @Test
    @Transactional
    void getAllTenantEncryptionKeysByKeyVersionIsLessThanSomething() throws Exception {
        // Initialize the database
        insertedTenantEncryptionKey = tenantEncryptionKeyRepository.saveAndFlush(tenantEncryptionKey);

        // Get all the tenantEncryptionKeyList where keyVersion is less than
        defaultTenantEncryptionKeyFiltering("keyVersion.lessThan=" + UPDATED_KEY_VERSION, "keyVersion.lessThan=" + DEFAULT_KEY_VERSION);
    }

    @Test
    @Transactional
    void getAllTenantEncryptionKeysByKeyVersionIsGreaterThanSomething() throws Exception {
        // Initialize the database
        insertedTenantEncryptionKey = tenantEncryptionKeyRepository.saveAndFlush(tenantEncryptionKey);

        // Get all the tenantEncryptionKeyList where keyVersion is greater than
        defaultTenantEncryptionKeyFiltering(
            "keyVersion.greaterThan=" + SMALLER_KEY_VERSION,
            "keyVersion.greaterThan=" + DEFAULT_KEY_VERSION
        );
    }

    @Test
    @Transactional
    void getAllTenantEncryptionKeysByEncryptedDataKeyIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedTenantEncryptionKey = tenantEncryptionKeyRepository.saveAndFlush(tenantEncryptionKey);

        // Get all the tenantEncryptionKeyList where encryptedDataKey equals to
        defaultTenantEncryptionKeyFiltering(
            "encryptedDataKey.equals=" + DEFAULT_ENCRYPTED_DATA_KEY,
            "encryptedDataKey.equals=" + UPDATED_ENCRYPTED_DATA_KEY
        );
    }

    @Test
    @Transactional
    void getAllTenantEncryptionKeysByEncryptedDataKeyIsInShouldWork() throws Exception {
        // Initialize the database
        insertedTenantEncryptionKey = tenantEncryptionKeyRepository.saveAndFlush(tenantEncryptionKey);

        // Get all the tenantEncryptionKeyList where encryptedDataKey in
        defaultTenantEncryptionKeyFiltering(
            "encryptedDataKey.in=" + DEFAULT_ENCRYPTED_DATA_KEY + "," + UPDATED_ENCRYPTED_DATA_KEY,
            "encryptedDataKey.in=" + UPDATED_ENCRYPTED_DATA_KEY
        );
    }

    @Test
    @Transactional
    void getAllTenantEncryptionKeysByEncryptedDataKeyIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedTenantEncryptionKey = tenantEncryptionKeyRepository.saveAndFlush(tenantEncryptionKey);

        // Get all the tenantEncryptionKeyList where encryptedDataKey is not null
        defaultTenantEncryptionKeyFiltering("encryptedDataKey.specified=true", "encryptedDataKey.specified=false");
    }

    @Test
    @Transactional
    void getAllTenantEncryptionKeysByEncryptedDataKeyContainsSomething() throws Exception {
        // Initialize the database
        insertedTenantEncryptionKey = tenantEncryptionKeyRepository.saveAndFlush(tenantEncryptionKey);

        // Get all the tenantEncryptionKeyList where encryptedDataKey contains
        defaultTenantEncryptionKeyFiltering(
            "encryptedDataKey.contains=" + DEFAULT_ENCRYPTED_DATA_KEY,
            "encryptedDataKey.contains=" + UPDATED_ENCRYPTED_DATA_KEY
        );
    }

    @Test
    @Transactional
    void getAllTenantEncryptionKeysByEncryptedDataKeyNotContainsSomething() throws Exception {
        // Initialize the database
        insertedTenantEncryptionKey = tenantEncryptionKeyRepository.saveAndFlush(tenantEncryptionKey);

        // Get all the tenantEncryptionKeyList where encryptedDataKey does not contain
        defaultTenantEncryptionKeyFiltering(
            "encryptedDataKey.doesNotContain=" + UPDATED_ENCRYPTED_DATA_KEY,
            "encryptedDataKey.doesNotContain=" + DEFAULT_ENCRYPTED_DATA_KEY
        );
    }

    @Test
    @Transactional
    void getAllTenantEncryptionKeysByPinHashIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedTenantEncryptionKey = tenantEncryptionKeyRepository.saveAndFlush(tenantEncryptionKey);

        // Get all the tenantEncryptionKeyList where pinHash equals to
        defaultTenantEncryptionKeyFiltering("pinHash.equals=" + DEFAULT_PIN_HASH, "pinHash.equals=" + UPDATED_PIN_HASH);
    }

    @Test
    @Transactional
    void getAllTenantEncryptionKeysByPinHashIsInShouldWork() throws Exception {
        // Initialize the database
        insertedTenantEncryptionKey = tenantEncryptionKeyRepository.saveAndFlush(tenantEncryptionKey);

        // Get all the tenantEncryptionKeyList where pinHash in
        defaultTenantEncryptionKeyFiltering("pinHash.in=" + DEFAULT_PIN_HASH + "," + UPDATED_PIN_HASH, "pinHash.in=" + UPDATED_PIN_HASH);
    }

    @Test
    @Transactional
    void getAllTenantEncryptionKeysByPinHashIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedTenantEncryptionKey = tenantEncryptionKeyRepository.saveAndFlush(tenantEncryptionKey);

        // Get all the tenantEncryptionKeyList where pinHash is not null
        defaultTenantEncryptionKeyFiltering("pinHash.specified=true", "pinHash.specified=false");
    }

    @Test
    @Transactional
    void getAllTenantEncryptionKeysByPinHashContainsSomething() throws Exception {
        // Initialize the database
        insertedTenantEncryptionKey = tenantEncryptionKeyRepository.saveAndFlush(tenantEncryptionKey);

        // Get all the tenantEncryptionKeyList where pinHash contains
        defaultTenantEncryptionKeyFiltering("pinHash.contains=" + DEFAULT_PIN_HASH, "pinHash.contains=" + UPDATED_PIN_HASH);
    }

    @Test
    @Transactional
    void getAllTenantEncryptionKeysByPinHashNotContainsSomething() throws Exception {
        // Initialize the database
        insertedTenantEncryptionKey = tenantEncryptionKeyRepository.saveAndFlush(tenantEncryptionKey);

        // Get all the tenantEncryptionKeyList where pinHash does not contain
        defaultTenantEncryptionKeyFiltering("pinHash.doesNotContain=" + UPDATED_PIN_HASH, "pinHash.doesNotContain=" + DEFAULT_PIN_HASH);
    }

    @Test
    @Transactional
    void getAllTenantEncryptionKeysByPinSaltIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedTenantEncryptionKey = tenantEncryptionKeyRepository.saveAndFlush(tenantEncryptionKey);

        // Get all the tenantEncryptionKeyList where pinSalt equals to
        defaultTenantEncryptionKeyFiltering("pinSalt.equals=" + DEFAULT_PIN_SALT, "pinSalt.equals=" + UPDATED_PIN_SALT);
    }

    @Test
    @Transactional
    void getAllTenantEncryptionKeysByPinSaltIsInShouldWork() throws Exception {
        // Initialize the database
        insertedTenantEncryptionKey = tenantEncryptionKeyRepository.saveAndFlush(tenantEncryptionKey);

        // Get all the tenantEncryptionKeyList where pinSalt in
        defaultTenantEncryptionKeyFiltering("pinSalt.in=" + DEFAULT_PIN_SALT + "," + UPDATED_PIN_SALT, "pinSalt.in=" + UPDATED_PIN_SALT);
    }

    @Test
    @Transactional
    void getAllTenantEncryptionKeysByPinSaltIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedTenantEncryptionKey = tenantEncryptionKeyRepository.saveAndFlush(tenantEncryptionKey);

        // Get all the tenantEncryptionKeyList where pinSalt is not null
        defaultTenantEncryptionKeyFiltering("pinSalt.specified=true", "pinSalt.specified=false");
    }

    @Test
    @Transactional
    void getAllTenantEncryptionKeysByPinSaltContainsSomething() throws Exception {
        // Initialize the database
        insertedTenantEncryptionKey = tenantEncryptionKeyRepository.saveAndFlush(tenantEncryptionKey);

        // Get all the tenantEncryptionKeyList where pinSalt contains
        defaultTenantEncryptionKeyFiltering("pinSalt.contains=" + DEFAULT_PIN_SALT, "pinSalt.contains=" + UPDATED_PIN_SALT);
    }

    @Test
    @Transactional
    void getAllTenantEncryptionKeysByPinSaltNotContainsSomething() throws Exception {
        // Initialize the database
        insertedTenantEncryptionKey = tenantEncryptionKeyRepository.saveAndFlush(tenantEncryptionKey);

        // Get all the tenantEncryptionKeyList where pinSalt does not contain
        defaultTenantEncryptionKeyFiltering("pinSalt.doesNotContain=" + UPDATED_PIN_SALT, "pinSalt.doesNotContain=" + DEFAULT_PIN_SALT);
    }

    @Test
    @Transactional
    void getAllTenantEncryptionKeysByIsActiveIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedTenantEncryptionKey = tenantEncryptionKeyRepository.saveAndFlush(tenantEncryptionKey);

        // Get all the tenantEncryptionKeyList where isActive equals to
        defaultTenantEncryptionKeyFiltering("isActive.equals=" + DEFAULT_IS_ACTIVE, "isActive.equals=" + UPDATED_IS_ACTIVE);
    }

    @Test
    @Transactional
    void getAllTenantEncryptionKeysByIsActiveIsInShouldWork() throws Exception {
        // Initialize the database
        insertedTenantEncryptionKey = tenantEncryptionKeyRepository.saveAndFlush(tenantEncryptionKey);

        // Get all the tenantEncryptionKeyList where isActive in
        defaultTenantEncryptionKeyFiltering(
            "isActive.in=" + DEFAULT_IS_ACTIVE + "," + UPDATED_IS_ACTIVE,
            "isActive.in=" + UPDATED_IS_ACTIVE
        );
    }

    @Test
    @Transactional
    void getAllTenantEncryptionKeysByIsActiveIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedTenantEncryptionKey = tenantEncryptionKeyRepository.saveAndFlush(tenantEncryptionKey);

        // Get all the tenantEncryptionKeyList where isActive is not null
        defaultTenantEncryptionKeyFiltering("isActive.specified=true", "isActive.specified=false");
    }

    @Test
    @Transactional
    void getAllTenantEncryptionKeysByTenantIsEqualToSomething() throws Exception {
        Tenant tenant;
        if (TestUtil.findAll(em, Tenant.class).isEmpty()) {
            tenantEncryptionKeyRepository.saveAndFlush(tenantEncryptionKey);
            tenant = TenantResourceIT.createEntity();
        } else {
            tenant = TestUtil.findAll(em, Tenant.class).get(0);
        }
        em.persist(tenant);
        em.flush();
        tenantEncryptionKey.setTenant(tenant);
        tenantEncryptionKeyRepository.saveAndFlush(tenantEncryptionKey);
        Long tenantId = tenant.getId();
        // Get all the tenantEncryptionKeyList where tenant equals to tenantId
        defaultTenantEncryptionKeyShouldBeFound("tenantId.equals=" + tenantId);

        // Get all the tenantEncryptionKeyList where tenant equals to (tenantId + 1)
        defaultTenantEncryptionKeyShouldNotBeFound("tenantId.equals=" + (tenantId + 1));
    }

    private void defaultTenantEncryptionKeyFiltering(String shouldBeFound, String shouldNotBeFound) throws Exception {
        defaultTenantEncryptionKeyShouldBeFound(shouldBeFound);
        defaultTenantEncryptionKeyShouldNotBeFound(shouldNotBeFound);
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultTenantEncryptionKeyShouldBeFound(String filter) throws Exception {
        restTenantEncryptionKeyMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(tenantEncryptionKey.getId().intValue())))
            .andExpect(jsonPath("$.[*].keyVersion").value(hasItem(DEFAULT_KEY_VERSION)))
            .andExpect(jsonPath("$.[*].encryptedDataKey").value(hasItem(DEFAULT_ENCRYPTED_DATA_KEY)))
            .andExpect(jsonPath("$.[*].pinHash").value(hasItem(DEFAULT_PIN_HASH)))
            .andExpect(jsonPath("$.[*].pinSalt").value(hasItem(DEFAULT_PIN_SALT)))
            .andExpect(jsonPath("$.[*].isActive").value(hasItem(DEFAULT_IS_ACTIVE)));

        // Check, that the count call also returns 1
        restTenantEncryptionKeyMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultTenantEncryptionKeyShouldNotBeFound(String filter) throws Exception {
        restTenantEncryptionKeyMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restTenantEncryptionKeyMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingTenantEncryptionKey() throws Exception {
        // Get the tenantEncryptionKey
        restTenantEncryptionKeyMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingTenantEncryptionKey() throws Exception {
        // Initialize the database
        insertedTenantEncryptionKey = tenantEncryptionKeyRepository.saveAndFlush(tenantEncryptionKey);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the tenantEncryptionKey
        TenantEncryptionKey updatedTenantEncryptionKey = tenantEncryptionKeyRepository.findById(tenantEncryptionKey.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedTenantEncryptionKey are not directly saved in db
        em.detach(updatedTenantEncryptionKey);
        updatedTenantEncryptionKey
            .keyVersion(UPDATED_KEY_VERSION)
            .encryptedDataKey(UPDATED_ENCRYPTED_DATA_KEY)
            .pinHash(UPDATED_PIN_HASH)
            .pinSalt(UPDATED_PIN_SALT)
            .isActive(UPDATED_IS_ACTIVE);
        TenantEncryptionKeyDTO tenantEncryptionKeyDTO = tenantEncryptionKeyMapper.toDto(updatedTenantEncryptionKey);

        restTenantEncryptionKeyMockMvc
            .perform(
                put(ENTITY_API_URL_ID, tenantEncryptionKeyDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(tenantEncryptionKeyDTO))
            )
            .andExpect(status().isOk());

        // Validate the TenantEncryptionKey in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedTenantEncryptionKeyToMatchAllProperties(updatedTenantEncryptionKey);
    }

    @Test
    @Transactional
    void putNonExistingTenantEncryptionKey() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        tenantEncryptionKey.setId(longCount.incrementAndGet());

        // Create the TenantEncryptionKey
        TenantEncryptionKeyDTO tenantEncryptionKeyDTO = tenantEncryptionKeyMapper.toDto(tenantEncryptionKey);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restTenantEncryptionKeyMockMvc
            .perform(
                put(ENTITY_API_URL_ID, tenantEncryptionKeyDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(tenantEncryptionKeyDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the TenantEncryptionKey in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchTenantEncryptionKey() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        tenantEncryptionKey.setId(longCount.incrementAndGet());

        // Create the TenantEncryptionKey
        TenantEncryptionKeyDTO tenantEncryptionKeyDTO = tenantEncryptionKeyMapper.toDto(tenantEncryptionKey);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restTenantEncryptionKeyMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(tenantEncryptionKeyDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the TenantEncryptionKey in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamTenantEncryptionKey() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        tenantEncryptionKey.setId(longCount.incrementAndGet());

        // Create the TenantEncryptionKey
        TenantEncryptionKeyDTO tenantEncryptionKeyDTO = tenantEncryptionKeyMapper.toDto(tenantEncryptionKey);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restTenantEncryptionKeyMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(tenantEncryptionKeyDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the TenantEncryptionKey in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateTenantEncryptionKeyWithPatch() throws Exception {
        // Initialize the database
        insertedTenantEncryptionKey = tenantEncryptionKeyRepository.saveAndFlush(tenantEncryptionKey);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the tenantEncryptionKey using partial update
        TenantEncryptionKey partialUpdatedTenantEncryptionKey = new TenantEncryptionKey();
        partialUpdatedTenantEncryptionKey.setId(tenantEncryptionKey.getId());

        partialUpdatedTenantEncryptionKey.pinHash(UPDATED_PIN_HASH);

        restTenantEncryptionKeyMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedTenantEncryptionKey.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedTenantEncryptionKey))
            )
            .andExpect(status().isOk());

        // Validate the TenantEncryptionKey in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertTenantEncryptionKeyUpdatableFieldsEquals(
            createUpdateProxyForBean(partialUpdatedTenantEncryptionKey, tenantEncryptionKey),
            getPersistedTenantEncryptionKey(tenantEncryptionKey)
        );
    }

    @Test
    @Transactional
    void fullUpdateTenantEncryptionKeyWithPatch() throws Exception {
        // Initialize the database
        insertedTenantEncryptionKey = tenantEncryptionKeyRepository.saveAndFlush(tenantEncryptionKey);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the tenantEncryptionKey using partial update
        TenantEncryptionKey partialUpdatedTenantEncryptionKey = new TenantEncryptionKey();
        partialUpdatedTenantEncryptionKey.setId(tenantEncryptionKey.getId());

        partialUpdatedTenantEncryptionKey
            .keyVersion(UPDATED_KEY_VERSION)
            .encryptedDataKey(UPDATED_ENCRYPTED_DATA_KEY)
            .pinHash(UPDATED_PIN_HASH)
            .pinSalt(UPDATED_PIN_SALT)
            .isActive(UPDATED_IS_ACTIVE);

        restTenantEncryptionKeyMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedTenantEncryptionKey.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedTenantEncryptionKey))
            )
            .andExpect(status().isOk());

        // Validate the TenantEncryptionKey in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertTenantEncryptionKeyUpdatableFieldsEquals(
            partialUpdatedTenantEncryptionKey,
            getPersistedTenantEncryptionKey(partialUpdatedTenantEncryptionKey)
        );
    }

    @Test
    @Transactional
    void patchNonExistingTenantEncryptionKey() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        tenantEncryptionKey.setId(longCount.incrementAndGet());

        // Create the TenantEncryptionKey
        TenantEncryptionKeyDTO tenantEncryptionKeyDTO = tenantEncryptionKeyMapper.toDto(tenantEncryptionKey);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restTenantEncryptionKeyMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, tenantEncryptionKeyDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(tenantEncryptionKeyDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the TenantEncryptionKey in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchTenantEncryptionKey() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        tenantEncryptionKey.setId(longCount.incrementAndGet());

        // Create the TenantEncryptionKey
        TenantEncryptionKeyDTO tenantEncryptionKeyDTO = tenantEncryptionKeyMapper.toDto(tenantEncryptionKey);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restTenantEncryptionKeyMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(tenantEncryptionKeyDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the TenantEncryptionKey in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamTenantEncryptionKey() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        tenantEncryptionKey.setId(longCount.incrementAndGet());

        // Create the TenantEncryptionKey
        TenantEncryptionKeyDTO tenantEncryptionKeyDTO = tenantEncryptionKeyMapper.toDto(tenantEncryptionKey);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restTenantEncryptionKeyMockMvc
            .perform(
                patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(om.writeValueAsBytes(tenantEncryptionKeyDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the TenantEncryptionKey in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteTenantEncryptionKey() throws Exception {
        // Initialize the database
        insertedTenantEncryptionKey = tenantEncryptionKeyRepository.saveAndFlush(tenantEncryptionKey);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the tenantEncryptionKey
        restTenantEncryptionKeyMockMvc
            .perform(delete(ENTITY_API_URL_ID, tenantEncryptionKey.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return tenantEncryptionKeyRepository.count();
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

    protected TenantEncryptionKey getPersistedTenantEncryptionKey(TenantEncryptionKey tenantEncryptionKey) {
        return tenantEncryptionKeyRepository.findById(tenantEncryptionKey.getId()).orElseThrow();
    }

    protected void assertPersistedTenantEncryptionKeyToMatchAllProperties(TenantEncryptionKey expectedTenantEncryptionKey) {
        assertTenantEncryptionKeyAllPropertiesEquals(
            expectedTenantEncryptionKey,
            getPersistedTenantEncryptionKey(expectedTenantEncryptionKey)
        );
    }

    protected void assertPersistedTenantEncryptionKeyToMatchUpdatableProperties(TenantEncryptionKey expectedTenantEncryptionKey) {
        assertTenantEncryptionKeyAllUpdatablePropertiesEquals(
            expectedTenantEncryptionKey,
            getPersistedTenantEncryptionKey(expectedTenantEncryptionKey)
        );
    }
}
