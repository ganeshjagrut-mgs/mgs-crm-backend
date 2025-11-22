package com.crm.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.crm.IntegrationTest;
import com.crm.domain.Encryption;
import com.crm.domain.Tenant;
import com.crm.repository.EncryptionRepository;
import com.crm.service.dto.EncryptionDTO;
import com.crm.service.mapper.EncryptionMapper;
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
 * Integration tests for the {@link EncryptionResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class EncryptionResourceIT {

    private static final String DEFAULT_KEY = "AAAAAAAAAA";
    private static final String UPDATED_KEY = "BBBBBBBBBB";

    private static final String DEFAULT_PIN = "AAAAAAAAAA";
    private static final String UPDATED_PIN = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/encryptions";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private EncryptionRepository encryptionRepository;

    @Autowired
    private EncryptionMapper encryptionMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restEncryptionMockMvc;

    private Encryption encryption;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Encryption createEntity(EntityManager em) {
        Encryption encryption = new Encryption().key(DEFAULT_KEY).pin(DEFAULT_PIN);
        return encryption;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Encryption createUpdatedEntity(EntityManager em) {
        Encryption encryption = new Encryption().key(UPDATED_KEY).pin(UPDATED_PIN);
        return encryption;
    }

    @BeforeEach
    public void initTest() {
        encryption = createEntity(em);
    }

    @Test
    @Transactional
    void createEncryption() throws Exception {
        int databaseSizeBeforeCreate = encryptionRepository.findAll().size();
        // Create the Encryption
        EncryptionDTO encryptionDTO = encryptionMapper.toDto(encryption);
        restEncryptionMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(encryptionDTO)))
            .andExpect(status().isCreated());

        // Validate the Encryption in the database
        List<Encryption> encryptionList = encryptionRepository.findAll();
        assertThat(encryptionList).hasSize(databaseSizeBeforeCreate + 1);
        Encryption testEncryption = encryptionList.get(encryptionList.size() - 1);
        assertThat(testEncryption.getKey()).isEqualTo(DEFAULT_KEY);
        assertThat(testEncryption.getPin()).isEqualTo(DEFAULT_PIN);
    }

    @Test
    @Transactional
    void createEncryptionWithExistingId() throws Exception {
        // Create the Encryption with an existing ID
        encryption.setId(1L);
        EncryptionDTO encryptionDTO = encryptionMapper.toDto(encryption);

        int databaseSizeBeforeCreate = encryptionRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restEncryptionMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(encryptionDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Encryption in the database
        List<Encryption> encryptionList = encryptionRepository.findAll();
        assertThat(encryptionList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkKeyIsRequired() throws Exception {
        int databaseSizeBeforeTest = encryptionRepository.findAll().size();
        // set the field null
        encryption.setKey(null);

        // Create the Encryption, which fails.
        EncryptionDTO encryptionDTO = encryptionMapper.toDto(encryption);

        restEncryptionMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(encryptionDTO)))
            .andExpect(status().isBadRequest());

        List<Encryption> encryptionList = encryptionRepository.findAll();
        assertThat(encryptionList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkPinIsRequired() throws Exception {
        int databaseSizeBeforeTest = encryptionRepository.findAll().size();
        // set the field null
        encryption.setPin(null);

        // Create the Encryption, which fails.
        EncryptionDTO encryptionDTO = encryptionMapper.toDto(encryption);

        restEncryptionMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(encryptionDTO)))
            .andExpect(status().isBadRequest());

        List<Encryption> encryptionList = encryptionRepository.findAll();
        assertThat(encryptionList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllEncryptions() throws Exception {
        // Initialize the database
        encryptionRepository.saveAndFlush(encryption);

        // Get all the encryptionList
        restEncryptionMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(encryption.getId().intValue())))
            .andExpect(jsonPath("$.[*].key").value(hasItem(DEFAULT_KEY)))
            .andExpect(jsonPath("$.[*].pin").value(hasItem(DEFAULT_PIN)));
    }

    @Test
    @Transactional
    void getEncryption() throws Exception {
        // Initialize the database
        encryptionRepository.saveAndFlush(encryption);

        // Get the encryption
        restEncryptionMockMvc
            .perform(get(ENTITY_API_URL_ID, encryption.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(encryption.getId().intValue()))
            .andExpect(jsonPath("$.key").value(DEFAULT_KEY))
            .andExpect(jsonPath("$.pin").value(DEFAULT_PIN));
    }

    @Test
    @Transactional
    void getEncryptionsByIdFiltering() throws Exception {
        // Initialize the database
        encryptionRepository.saveAndFlush(encryption);

        Long id = encryption.getId();

        defaultEncryptionShouldBeFound("id.equals=" + id);
        defaultEncryptionShouldNotBeFound("id.notEquals=" + id);

        defaultEncryptionShouldBeFound("id.greaterThanOrEqual=" + id);
        defaultEncryptionShouldNotBeFound("id.greaterThan=" + id);

        defaultEncryptionShouldBeFound("id.lessThanOrEqual=" + id);
        defaultEncryptionShouldNotBeFound("id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllEncryptionsByKeyIsEqualToSomething() throws Exception {
        // Initialize the database
        encryptionRepository.saveAndFlush(encryption);

        // Get all the encryptionList where key equals to DEFAULT_KEY
        defaultEncryptionShouldBeFound("key.equals=" + DEFAULT_KEY);

        // Get all the encryptionList where key equals to UPDATED_KEY
        defaultEncryptionShouldNotBeFound("key.equals=" + UPDATED_KEY);
    }

    @Test
    @Transactional
    void getAllEncryptionsByKeyIsInShouldWork() throws Exception {
        // Initialize the database
        encryptionRepository.saveAndFlush(encryption);

        // Get all the encryptionList where key in DEFAULT_KEY or UPDATED_KEY
        defaultEncryptionShouldBeFound("key.in=" + DEFAULT_KEY + "," + UPDATED_KEY);

        // Get all the encryptionList where key equals to UPDATED_KEY
        defaultEncryptionShouldNotBeFound("key.in=" + UPDATED_KEY);
    }

    @Test
    @Transactional
    void getAllEncryptionsByKeyIsNullOrNotNull() throws Exception {
        // Initialize the database
        encryptionRepository.saveAndFlush(encryption);

        // Get all the encryptionList where key is not null
        defaultEncryptionShouldBeFound("key.specified=true");

        // Get all the encryptionList where key is null
        defaultEncryptionShouldNotBeFound("key.specified=false");
    }

    @Test
    @Transactional
    void getAllEncryptionsByKeyContainsSomething() throws Exception {
        // Initialize the database
        encryptionRepository.saveAndFlush(encryption);

        // Get all the encryptionList where key contains DEFAULT_KEY
        defaultEncryptionShouldBeFound("key.contains=" + DEFAULT_KEY);

        // Get all the encryptionList where key contains UPDATED_KEY
        defaultEncryptionShouldNotBeFound("key.contains=" + UPDATED_KEY);
    }

    @Test
    @Transactional
    void getAllEncryptionsByKeyNotContainsSomething() throws Exception {
        // Initialize the database
        encryptionRepository.saveAndFlush(encryption);

        // Get all the encryptionList where key does not contain DEFAULT_KEY
        defaultEncryptionShouldNotBeFound("key.doesNotContain=" + DEFAULT_KEY);

        // Get all the encryptionList where key does not contain UPDATED_KEY
        defaultEncryptionShouldBeFound("key.doesNotContain=" + UPDATED_KEY);
    }

    @Test
    @Transactional
    void getAllEncryptionsByPinIsEqualToSomething() throws Exception {
        // Initialize the database
        encryptionRepository.saveAndFlush(encryption);

        // Get all the encryptionList where pin equals to DEFAULT_PIN
        defaultEncryptionShouldBeFound("pin.equals=" + DEFAULT_PIN);

        // Get all the encryptionList where pin equals to UPDATED_PIN
        defaultEncryptionShouldNotBeFound("pin.equals=" + UPDATED_PIN);
    }

    @Test
    @Transactional
    void getAllEncryptionsByPinIsInShouldWork() throws Exception {
        // Initialize the database
        encryptionRepository.saveAndFlush(encryption);

        // Get all the encryptionList where pin in DEFAULT_PIN or UPDATED_PIN
        defaultEncryptionShouldBeFound("pin.in=" + DEFAULT_PIN + "," + UPDATED_PIN);

        // Get all the encryptionList where pin equals to UPDATED_PIN
        defaultEncryptionShouldNotBeFound("pin.in=" + UPDATED_PIN);
    }

    @Test
    @Transactional
    void getAllEncryptionsByPinIsNullOrNotNull() throws Exception {
        // Initialize the database
        encryptionRepository.saveAndFlush(encryption);

        // Get all the encryptionList where pin is not null
        defaultEncryptionShouldBeFound("pin.specified=true");

        // Get all the encryptionList where pin is null
        defaultEncryptionShouldNotBeFound("pin.specified=false");
    }

    @Test
    @Transactional
    void getAllEncryptionsByPinContainsSomething() throws Exception {
        // Initialize the database
        encryptionRepository.saveAndFlush(encryption);

        // Get all the encryptionList where pin contains DEFAULT_PIN
        defaultEncryptionShouldBeFound("pin.contains=" + DEFAULT_PIN);

        // Get all the encryptionList where pin contains UPDATED_PIN
        defaultEncryptionShouldNotBeFound("pin.contains=" + UPDATED_PIN);
    }

    @Test
    @Transactional
    void getAllEncryptionsByPinNotContainsSomething() throws Exception {
        // Initialize the database
        encryptionRepository.saveAndFlush(encryption);

        // Get all the encryptionList where pin does not contain DEFAULT_PIN
        defaultEncryptionShouldNotBeFound("pin.doesNotContain=" + DEFAULT_PIN);

        // Get all the encryptionList where pin does not contain UPDATED_PIN
        defaultEncryptionShouldBeFound("pin.doesNotContain=" + UPDATED_PIN);
    }

    @Test
    @Transactional
    void getAllEncryptionsByTenantIsEqualToSomething() throws Exception {
        Tenant tenant;
        if (TestUtil.findAll(em, Tenant.class).isEmpty()) {
            encryptionRepository.saveAndFlush(encryption);
            tenant = TenantResourceIT.createEntity(em);
        } else {
            tenant = TestUtil.findAll(em, Tenant.class).get(0);
        }
        em.persist(tenant);
        em.flush();
        encryption.setTenant(tenant);
        encryptionRepository.saveAndFlush(encryption);
        Long tenantId = tenant.getId();
        // Get all the encryptionList where tenant equals to tenantId
        defaultEncryptionShouldBeFound("tenantId.equals=" + tenantId);

        // Get all the encryptionList where tenant equals to (tenantId + 1)
        defaultEncryptionShouldNotBeFound("tenantId.equals=" + (tenantId + 1));
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultEncryptionShouldBeFound(String filter) throws Exception {
        restEncryptionMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(encryption.getId().intValue())))
            .andExpect(jsonPath("$.[*].key").value(hasItem(DEFAULT_KEY)))
            .andExpect(jsonPath("$.[*].pin").value(hasItem(DEFAULT_PIN)));

        // Check, that the count call also returns 1
        restEncryptionMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultEncryptionShouldNotBeFound(String filter) throws Exception {
        restEncryptionMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restEncryptionMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingEncryption() throws Exception {
        // Get the encryption
        restEncryptionMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingEncryption() throws Exception {
        // Initialize the database
        encryptionRepository.saveAndFlush(encryption);

        int databaseSizeBeforeUpdate = encryptionRepository.findAll().size();

        // Update the encryption
        Encryption updatedEncryption = encryptionRepository.findById(encryption.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedEncryption are not directly saved in db
        em.detach(updatedEncryption);
        updatedEncryption.key(UPDATED_KEY).pin(UPDATED_PIN);
        EncryptionDTO encryptionDTO = encryptionMapper.toDto(updatedEncryption);

        restEncryptionMockMvc
            .perform(
                put(ENTITY_API_URL_ID, encryptionDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(encryptionDTO))
            )
            .andExpect(status().isOk());

        // Validate the Encryption in the database
        List<Encryption> encryptionList = encryptionRepository.findAll();
        assertThat(encryptionList).hasSize(databaseSizeBeforeUpdate);
        Encryption testEncryption = encryptionList.get(encryptionList.size() - 1);
        assertThat(testEncryption.getKey()).isEqualTo(UPDATED_KEY);
        assertThat(testEncryption.getPin()).isEqualTo(UPDATED_PIN);
    }

    @Test
    @Transactional
    void putNonExistingEncryption() throws Exception {
        int databaseSizeBeforeUpdate = encryptionRepository.findAll().size();
        encryption.setId(longCount.incrementAndGet());

        // Create the Encryption
        EncryptionDTO encryptionDTO = encryptionMapper.toDto(encryption);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restEncryptionMockMvc
            .perform(
                put(ENTITY_API_URL_ID, encryptionDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(encryptionDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Encryption in the database
        List<Encryption> encryptionList = encryptionRepository.findAll();
        assertThat(encryptionList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchEncryption() throws Exception {
        int databaseSizeBeforeUpdate = encryptionRepository.findAll().size();
        encryption.setId(longCount.incrementAndGet());

        // Create the Encryption
        EncryptionDTO encryptionDTO = encryptionMapper.toDto(encryption);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restEncryptionMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(encryptionDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Encryption in the database
        List<Encryption> encryptionList = encryptionRepository.findAll();
        assertThat(encryptionList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamEncryption() throws Exception {
        int databaseSizeBeforeUpdate = encryptionRepository.findAll().size();
        encryption.setId(longCount.incrementAndGet());

        // Create the Encryption
        EncryptionDTO encryptionDTO = encryptionMapper.toDto(encryption);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restEncryptionMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(encryptionDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Encryption in the database
        List<Encryption> encryptionList = encryptionRepository.findAll();
        assertThat(encryptionList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateEncryptionWithPatch() throws Exception {
        // Initialize the database
        encryptionRepository.saveAndFlush(encryption);

        int databaseSizeBeforeUpdate = encryptionRepository.findAll().size();

        // Update the encryption using partial update
        Encryption partialUpdatedEncryption = new Encryption();
        partialUpdatedEncryption.setId(encryption.getId());

        partialUpdatedEncryption.key(UPDATED_KEY);

        restEncryptionMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedEncryption.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedEncryption))
            )
            .andExpect(status().isOk());

        // Validate the Encryption in the database
        List<Encryption> encryptionList = encryptionRepository.findAll();
        assertThat(encryptionList).hasSize(databaseSizeBeforeUpdate);
        Encryption testEncryption = encryptionList.get(encryptionList.size() - 1);
        assertThat(testEncryption.getKey()).isEqualTo(UPDATED_KEY);
        assertThat(testEncryption.getPin()).isEqualTo(DEFAULT_PIN);
    }

    @Test
    @Transactional
    void fullUpdateEncryptionWithPatch() throws Exception {
        // Initialize the database
        encryptionRepository.saveAndFlush(encryption);

        int databaseSizeBeforeUpdate = encryptionRepository.findAll().size();

        // Update the encryption using partial update
        Encryption partialUpdatedEncryption = new Encryption();
        partialUpdatedEncryption.setId(encryption.getId());

        partialUpdatedEncryption.key(UPDATED_KEY).pin(UPDATED_PIN);

        restEncryptionMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedEncryption.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedEncryption))
            )
            .andExpect(status().isOk());

        // Validate the Encryption in the database
        List<Encryption> encryptionList = encryptionRepository.findAll();
        assertThat(encryptionList).hasSize(databaseSizeBeforeUpdate);
        Encryption testEncryption = encryptionList.get(encryptionList.size() - 1);
        assertThat(testEncryption.getKey()).isEqualTo(UPDATED_KEY);
        assertThat(testEncryption.getPin()).isEqualTo(UPDATED_PIN);
    }

    @Test
    @Transactional
    void patchNonExistingEncryption() throws Exception {
        int databaseSizeBeforeUpdate = encryptionRepository.findAll().size();
        encryption.setId(longCount.incrementAndGet());

        // Create the Encryption
        EncryptionDTO encryptionDTO = encryptionMapper.toDto(encryption);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restEncryptionMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, encryptionDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(encryptionDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Encryption in the database
        List<Encryption> encryptionList = encryptionRepository.findAll();
        assertThat(encryptionList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchEncryption() throws Exception {
        int databaseSizeBeforeUpdate = encryptionRepository.findAll().size();
        encryption.setId(longCount.incrementAndGet());

        // Create the Encryption
        EncryptionDTO encryptionDTO = encryptionMapper.toDto(encryption);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restEncryptionMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(encryptionDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Encryption in the database
        List<Encryption> encryptionList = encryptionRepository.findAll();
        assertThat(encryptionList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamEncryption() throws Exception {
        int databaseSizeBeforeUpdate = encryptionRepository.findAll().size();
        encryption.setId(longCount.incrementAndGet());

        // Create the Encryption
        EncryptionDTO encryptionDTO = encryptionMapper.toDto(encryption);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restEncryptionMockMvc
            .perform(
                patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(encryptionDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the Encryption in the database
        List<Encryption> encryptionList = encryptionRepository.findAll();
        assertThat(encryptionList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteEncryption() throws Exception {
        // Initialize the database
        encryptionRepository.saveAndFlush(encryption);

        int databaseSizeBeforeDelete = encryptionRepository.findAll().size();

        // Delete the encryption
        restEncryptionMockMvc
            .perform(delete(ENTITY_API_URL_ID, encryption.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Encryption> encryptionList = encryptionRepository.findAll();
        assertThat(encryptionList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
