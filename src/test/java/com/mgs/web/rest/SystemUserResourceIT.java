package com.mgs.web.rest;

import static com.mgs.domain.SystemUserAsserts.*;
import static com.mgs.web.rest.TestUtil.createUpdateProxyForBean;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mgs.IntegrationTest;
import com.mgs.domain.SystemUser;
import com.mgs.repository.SystemUserRepository;
import com.mgs.service.dto.SystemUserDTO;
import com.mgs.service.mapper.SystemUserMapper;
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
 * Integration tests for the {@link SystemUserResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class SystemUserResourceIT {

    private static final String DEFAULT_EMAIL = "AAAAAAAAAA";
    private static final String UPDATED_EMAIL = "BBBBBBBBBB";

    private static final String DEFAULT_PASSWORD_HASH = "AAAAAAAAAA";
    private static final String UPDATED_PASSWORD_HASH = "BBBBBBBBBB";

    private static final Boolean DEFAULT_IS_SUPER_ADMIN = false;
    private static final Boolean UPDATED_IS_SUPER_ADMIN = true;

    private static final String ENTITY_API_URL = "/api/system-users";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ObjectMapper om;

    @Autowired
    private SystemUserRepository systemUserRepository;

    @Autowired
    private SystemUserMapper systemUserMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restSystemUserMockMvc;

    private SystemUser systemUser;

    private SystemUser insertedSystemUser;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static SystemUser createEntity() {
        return new SystemUser().email(DEFAULT_EMAIL).passwordHash(DEFAULT_PASSWORD_HASH).isSuperAdmin(DEFAULT_IS_SUPER_ADMIN);
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static SystemUser createUpdatedEntity() {
        return new SystemUser().email(UPDATED_EMAIL).passwordHash(UPDATED_PASSWORD_HASH).isSuperAdmin(UPDATED_IS_SUPER_ADMIN);
    }

    @BeforeEach
    void initTest() {
        systemUser = createEntity();
    }

    @AfterEach
    void cleanup() {
        if (insertedSystemUser != null) {
            systemUserRepository.delete(insertedSystemUser);
            insertedSystemUser = null;
        }
    }

    @Test
    @Transactional
    void createSystemUser() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the SystemUser
        SystemUserDTO systemUserDTO = systemUserMapper.toDto(systemUser);
        var returnedSystemUserDTO = om.readValue(
            restSystemUserMockMvc
                .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(systemUserDTO)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            SystemUserDTO.class
        );

        // Validate the SystemUser in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        var returnedSystemUser = systemUserMapper.toEntity(returnedSystemUserDTO);
        assertSystemUserUpdatableFieldsEquals(returnedSystemUser, getPersistedSystemUser(returnedSystemUser));

        insertedSystemUser = returnedSystemUser;
    }

    @Test
    @Transactional
    void createSystemUserWithExistingId() throws Exception {
        // Create the SystemUser with an existing ID
        systemUser.setId(1L);
        SystemUserDTO systemUserDTO = systemUserMapper.toDto(systemUser);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restSystemUserMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(systemUserDTO)))
            .andExpect(status().isBadRequest());

        // Validate the SystemUser in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkEmailIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        systemUser.setEmail(null);

        // Create the SystemUser, which fails.
        SystemUserDTO systemUserDTO = systemUserMapper.toDto(systemUser);

        restSystemUserMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(systemUserDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkPasswordHashIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        systemUser.setPasswordHash(null);

        // Create the SystemUser, which fails.
        SystemUserDTO systemUserDTO = systemUserMapper.toDto(systemUser);

        restSystemUserMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(systemUserDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkIsSuperAdminIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        systemUser.setIsSuperAdmin(null);

        // Create the SystemUser, which fails.
        SystemUserDTO systemUserDTO = systemUserMapper.toDto(systemUser);

        restSystemUserMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(systemUserDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllSystemUsers() throws Exception {
        // Initialize the database
        insertedSystemUser = systemUserRepository.saveAndFlush(systemUser);

        // Get all the systemUserList
        restSystemUserMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(systemUser.getId().intValue())))
            .andExpect(jsonPath("$.[*].email").value(hasItem(DEFAULT_EMAIL)))
            .andExpect(jsonPath("$.[*].passwordHash").value(hasItem(DEFAULT_PASSWORD_HASH)))
            .andExpect(jsonPath("$.[*].isSuperAdmin").value(hasItem(DEFAULT_IS_SUPER_ADMIN)));
    }

    @Test
    @Transactional
    void getSystemUser() throws Exception {
        // Initialize the database
        insertedSystemUser = systemUserRepository.saveAndFlush(systemUser);

        // Get the systemUser
        restSystemUserMockMvc
            .perform(get(ENTITY_API_URL_ID, systemUser.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(systemUser.getId().intValue()))
            .andExpect(jsonPath("$.email").value(DEFAULT_EMAIL))
            .andExpect(jsonPath("$.passwordHash").value(DEFAULT_PASSWORD_HASH))
            .andExpect(jsonPath("$.isSuperAdmin").value(DEFAULT_IS_SUPER_ADMIN));
    }

    @Test
    @Transactional
    void getSystemUsersByIdFiltering() throws Exception {
        // Initialize the database
        insertedSystemUser = systemUserRepository.saveAndFlush(systemUser);

        Long id = systemUser.getId();

        defaultSystemUserFiltering("id.equals=" + id, "id.notEquals=" + id);

        defaultSystemUserFiltering("id.greaterThanOrEqual=" + id, "id.greaterThan=" + id);

        defaultSystemUserFiltering("id.lessThanOrEqual=" + id, "id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllSystemUsersByEmailIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedSystemUser = systemUserRepository.saveAndFlush(systemUser);

        // Get all the systemUserList where email equals to
        defaultSystemUserFiltering("email.equals=" + DEFAULT_EMAIL, "email.equals=" + UPDATED_EMAIL);
    }

    @Test
    @Transactional
    void getAllSystemUsersByEmailIsInShouldWork() throws Exception {
        // Initialize the database
        insertedSystemUser = systemUserRepository.saveAndFlush(systemUser);

        // Get all the systemUserList where email in
        defaultSystemUserFiltering("email.in=" + DEFAULT_EMAIL + "," + UPDATED_EMAIL, "email.in=" + UPDATED_EMAIL);
    }

    @Test
    @Transactional
    void getAllSystemUsersByEmailIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedSystemUser = systemUserRepository.saveAndFlush(systemUser);

        // Get all the systemUserList where email is not null
        defaultSystemUserFiltering("email.specified=true", "email.specified=false");
    }

    @Test
    @Transactional
    void getAllSystemUsersByEmailContainsSomething() throws Exception {
        // Initialize the database
        insertedSystemUser = systemUserRepository.saveAndFlush(systemUser);

        // Get all the systemUserList where email contains
        defaultSystemUserFiltering("email.contains=" + DEFAULT_EMAIL, "email.contains=" + UPDATED_EMAIL);
    }

    @Test
    @Transactional
    void getAllSystemUsersByEmailNotContainsSomething() throws Exception {
        // Initialize the database
        insertedSystemUser = systemUserRepository.saveAndFlush(systemUser);

        // Get all the systemUserList where email does not contain
        defaultSystemUserFiltering("email.doesNotContain=" + UPDATED_EMAIL, "email.doesNotContain=" + DEFAULT_EMAIL);
    }

    @Test
    @Transactional
    void getAllSystemUsersByPasswordHashIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedSystemUser = systemUserRepository.saveAndFlush(systemUser);

        // Get all the systemUserList where passwordHash equals to
        defaultSystemUserFiltering("passwordHash.equals=" + DEFAULT_PASSWORD_HASH, "passwordHash.equals=" + UPDATED_PASSWORD_HASH);
    }

    @Test
    @Transactional
    void getAllSystemUsersByPasswordHashIsInShouldWork() throws Exception {
        // Initialize the database
        insertedSystemUser = systemUserRepository.saveAndFlush(systemUser);

        // Get all the systemUserList where passwordHash in
        defaultSystemUserFiltering(
            "passwordHash.in=" + DEFAULT_PASSWORD_HASH + "," + UPDATED_PASSWORD_HASH,
            "passwordHash.in=" + UPDATED_PASSWORD_HASH
        );
    }

    @Test
    @Transactional
    void getAllSystemUsersByPasswordHashIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedSystemUser = systemUserRepository.saveAndFlush(systemUser);

        // Get all the systemUserList where passwordHash is not null
        defaultSystemUserFiltering("passwordHash.specified=true", "passwordHash.specified=false");
    }

    @Test
    @Transactional
    void getAllSystemUsersByPasswordHashContainsSomething() throws Exception {
        // Initialize the database
        insertedSystemUser = systemUserRepository.saveAndFlush(systemUser);

        // Get all the systemUserList where passwordHash contains
        defaultSystemUserFiltering("passwordHash.contains=" + DEFAULT_PASSWORD_HASH, "passwordHash.contains=" + UPDATED_PASSWORD_HASH);
    }

    @Test
    @Transactional
    void getAllSystemUsersByPasswordHashNotContainsSomething() throws Exception {
        // Initialize the database
        insertedSystemUser = systemUserRepository.saveAndFlush(systemUser);

        // Get all the systemUserList where passwordHash does not contain
        defaultSystemUserFiltering(
            "passwordHash.doesNotContain=" + UPDATED_PASSWORD_HASH,
            "passwordHash.doesNotContain=" + DEFAULT_PASSWORD_HASH
        );
    }

    @Test
    @Transactional
    void getAllSystemUsersByIsSuperAdminIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedSystemUser = systemUserRepository.saveAndFlush(systemUser);

        // Get all the systemUserList where isSuperAdmin equals to
        defaultSystemUserFiltering("isSuperAdmin.equals=" + DEFAULT_IS_SUPER_ADMIN, "isSuperAdmin.equals=" + UPDATED_IS_SUPER_ADMIN);
    }

    @Test
    @Transactional
    void getAllSystemUsersByIsSuperAdminIsInShouldWork() throws Exception {
        // Initialize the database
        insertedSystemUser = systemUserRepository.saveAndFlush(systemUser);

        // Get all the systemUserList where isSuperAdmin in
        defaultSystemUserFiltering(
            "isSuperAdmin.in=" + DEFAULT_IS_SUPER_ADMIN + "," + UPDATED_IS_SUPER_ADMIN,
            "isSuperAdmin.in=" + UPDATED_IS_SUPER_ADMIN
        );
    }

    @Test
    @Transactional
    void getAllSystemUsersByIsSuperAdminIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedSystemUser = systemUserRepository.saveAndFlush(systemUser);

        // Get all the systemUserList where isSuperAdmin is not null
        defaultSystemUserFiltering("isSuperAdmin.specified=true", "isSuperAdmin.specified=false");
    }

    private void defaultSystemUserFiltering(String shouldBeFound, String shouldNotBeFound) throws Exception {
        defaultSystemUserShouldBeFound(shouldBeFound);
        defaultSystemUserShouldNotBeFound(shouldNotBeFound);
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultSystemUserShouldBeFound(String filter) throws Exception {
        restSystemUserMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(systemUser.getId().intValue())))
            .andExpect(jsonPath("$.[*].email").value(hasItem(DEFAULT_EMAIL)))
            .andExpect(jsonPath("$.[*].passwordHash").value(hasItem(DEFAULT_PASSWORD_HASH)))
            .andExpect(jsonPath("$.[*].isSuperAdmin").value(hasItem(DEFAULT_IS_SUPER_ADMIN)));

        // Check, that the count call also returns 1
        restSystemUserMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultSystemUserShouldNotBeFound(String filter) throws Exception {
        restSystemUserMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restSystemUserMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingSystemUser() throws Exception {
        // Get the systemUser
        restSystemUserMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingSystemUser() throws Exception {
        // Initialize the database
        insertedSystemUser = systemUserRepository.saveAndFlush(systemUser);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the systemUser
        SystemUser updatedSystemUser = systemUserRepository.findById(systemUser.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedSystemUser are not directly saved in db
        em.detach(updatedSystemUser);
        updatedSystemUser.email(UPDATED_EMAIL).passwordHash(UPDATED_PASSWORD_HASH).isSuperAdmin(UPDATED_IS_SUPER_ADMIN);
        SystemUserDTO systemUserDTO = systemUserMapper.toDto(updatedSystemUser);

        restSystemUserMockMvc
            .perform(
                put(ENTITY_API_URL_ID, systemUserDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(systemUserDTO))
            )
            .andExpect(status().isOk());

        // Validate the SystemUser in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedSystemUserToMatchAllProperties(updatedSystemUser);
    }

    @Test
    @Transactional
    void putNonExistingSystemUser() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        systemUser.setId(longCount.incrementAndGet());

        // Create the SystemUser
        SystemUserDTO systemUserDTO = systemUserMapper.toDto(systemUser);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restSystemUserMockMvc
            .perform(
                put(ENTITY_API_URL_ID, systemUserDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(systemUserDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the SystemUser in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchSystemUser() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        systemUser.setId(longCount.incrementAndGet());

        // Create the SystemUser
        SystemUserDTO systemUserDTO = systemUserMapper.toDto(systemUser);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restSystemUserMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(systemUserDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the SystemUser in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamSystemUser() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        systemUser.setId(longCount.incrementAndGet());

        // Create the SystemUser
        SystemUserDTO systemUserDTO = systemUserMapper.toDto(systemUser);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restSystemUserMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(systemUserDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the SystemUser in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateSystemUserWithPatch() throws Exception {
        // Initialize the database
        insertedSystemUser = systemUserRepository.saveAndFlush(systemUser);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the systemUser using partial update
        SystemUser partialUpdatedSystemUser = new SystemUser();
        partialUpdatedSystemUser.setId(systemUser.getId());

        partialUpdatedSystemUser.email(UPDATED_EMAIL);

        restSystemUserMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedSystemUser.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedSystemUser))
            )
            .andExpect(status().isOk());

        // Validate the SystemUser in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertSystemUserUpdatableFieldsEquals(
            createUpdateProxyForBean(partialUpdatedSystemUser, systemUser),
            getPersistedSystemUser(systemUser)
        );
    }

    @Test
    @Transactional
    void fullUpdateSystemUserWithPatch() throws Exception {
        // Initialize the database
        insertedSystemUser = systemUserRepository.saveAndFlush(systemUser);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the systemUser using partial update
        SystemUser partialUpdatedSystemUser = new SystemUser();
        partialUpdatedSystemUser.setId(systemUser.getId());

        partialUpdatedSystemUser.email(UPDATED_EMAIL).passwordHash(UPDATED_PASSWORD_HASH).isSuperAdmin(UPDATED_IS_SUPER_ADMIN);

        restSystemUserMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedSystemUser.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedSystemUser))
            )
            .andExpect(status().isOk());

        // Validate the SystemUser in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertSystemUserUpdatableFieldsEquals(partialUpdatedSystemUser, getPersistedSystemUser(partialUpdatedSystemUser));
    }

    @Test
    @Transactional
    void patchNonExistingSystemUser() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        systemUser.setId(longCount.incrementAndGet());

        // Create the SystemUser
        SystemUserDTO systemUserDTO = systemUserMapper.toDto(systemUser);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restSystemUserMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, systemUserDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(systemUserDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the SystemUser in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchSystemUser() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        systemUser.setId(longCount.incrementAndGet());

        // Create the SystemUser
        SystemUserDTO systemUserDTO = systemUserMapper.toDto(systemUser);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restSystemUserMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(systemUserDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the SystemUser in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamSystemUser() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        systemUser.setId(longCount.incrementAndGet());

        // Create the SystemUser
        SystemUserDTO systemUserDTO = systemUserMapper.toDto(systemUser);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restSystemUserMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(om.writeValueAsBytes(systemUserDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the SystemUser in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteSystemUser() throws Exception {
        // Initialize the database
        insertedSystemUser = systemUserRepository.saveAndFlush(systemUser);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the systemUser
        restSystemUserMockMvc
            .perform(delete(ENTITY_API_URL_ID, systemUser.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return systemUserRepository.count();
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

    protected SystemUser getPersistedSystemUser(SystemUser systemUser) {
        return systemUserRepository.findById(systemUser.getId()).orElseThrow();
    }

    protected void assertPersistedSystemUserToMatchAllProperties(SystemUser expectedSystemUser) {
        assertSystemUserAllPropertiesEquals(expectedSystemUser, getPersistedSystemUser(expectedSystemUser));
    }

    protected void assertPersistedSystemUserToMatchUpdatableProperties(SystemUser expectedSystemUser) {
        assertSystemUserAllUpdatablePropertiesEquals(expectedSystemUser, getPersistedSystemUser(expectedSystemUser));
    }
}
