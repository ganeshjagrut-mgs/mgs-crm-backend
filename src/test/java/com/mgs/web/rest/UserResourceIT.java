package com.mgs.web.rest;

import static com.mgs.domain.UserAsserts.*;
import static com.mgs.web.rest.TestUtil.createUpdateProxyForBean;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mgs.IntegrationTest;
import com.mgs.domain.Tenant;
import com.mgs.domain.User;
import com.mgs.repository.UserRepository;
import com.mgs.service.dto.UserDTO;
import com.mgs.service.mapper.UserMapper;
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
 * Integration tests for the {@link UserResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class UserResourceIT {

    private static final String DEFAULT_EMAIL = "AAAAAAAAAA";
    private static final String UPDATED_EMAIL = "BBBBBBBBBB";

    private static final String DEFAULT_PHONE = "AAAAAAAAAA";
    private static final String UPDATED_PHONE = "BBBBBBBBBB";

    private static final String DEFAULT_PASSWORD_HASH = "AAAAAAAAAA";
    private static final String UPDATED_PASSWORD_HASH = "BBBBBBBBBB";

    private static final String DEFAULT_FIRST_NAME = "AAAAAAAAAA";
    private static final String UPDATED_FIRST_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_LAST_NAME = "AAAAAAAAAA";
    private static final String UPDATED_LAST_NAME = "BBBBBBBBBB";

    private static final Boolean DEFAULT_IS_ACTIVE = false;
    private static final Boolean UPDATED_IS_ACTIVE = true;

    private static final String ENTITY_API_URL = "/api/users";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ObjectMapper om;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restUserMockMvc;

    private User user;

    private User insertedUser;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static User createEntity(EntityManager em) {
        User user = new User()
            .email(DEFAULT_EMAIL)
            .phone(DEFAULT_PHONE)
            .passwordHash(DEFAULT_PASSWORD_HASH)
            .firstName(DEFAULT_FIRST_NAME)
            .lastName(DEFAULT_LAST_NAME)
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
        user.setTenant(tenant);
        return user;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static User createUpdatedEntity(EntityManager em) {
        User updatedUser = new User()
            .email(UPDATED_EMAIL)
            .phone(UPDATED_PHONE)
            .passwordHash(UPDATED_PASSWORD_HASH)
            .firstName(UPDATED_FIRST_NAME)
            .lastName(UPDATED_LAST_NAME)
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
        updatedUser.setTenant(tenant);
        return updatedUser;
    }

    @BeforeEach
    void initTest() {
        user = createEntity(em);
    }

    @AfterEach
    void cleanup() {
        if (insertedUser != null) {
            userRepository.delete(insertedUser);
            insertedUser = null;
        }
    }

    @Test
    @Transactional
    void createUser() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the User
        UserDTO userDTO = userMapper.toDto(user);
        var returnedUserDTO = om.readValue(
            restUserMockMvc
                .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(userDTO)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            UserDTO.class
        );

        // Validate the User in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        var returnedUser = userMapper.toEntity(returnedUserDTO);
        assertUserUpdatableFieldsEquals(returnedUser, getPersistedUser(returnedUser));

        insertedUser = returnedUser;
    }

    @Test
    @Transactional
    void createUserWithExistingId() throws Exception {
        // Create the User with an existing ID
        user.setId(1L);
        UserDTO userDTO = userMapper.toDto(user);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restUserMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(userDTO)))
            .andExpect(status().isBadRequest());

        // Validate the User in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkPasswordHashIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        user.setPasswordHash(null);

        // Create the User, which fails.
        UserDTO userDTO = userMapper.toDto(user);

        restUserMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(userDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkIsActiveIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        user.setIsActive(null);

        // Create the User, which fails.
        UserDTO userDTO = userMapper.toDto(user);

        restUserMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(userDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllUsers() throws Exception {
        // Initialize the database
        insertedUser = userRepository.saveAndFlush(user);

        // Get all the userList
        restUserMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(user.getId().intValue())))
            .andExpect(jsonPath("$.[*].email").value(hasItem(DEFAULT_EMAIL)))
            .andExpect(jsonPath("$.[*].phone").value(hasItem(DEFAULT_PHONE)))
            .andExpect(jsonPath("$.[*].passwordHash").value(hasItem(DEFAULT_PASSWORD_HASH)))
            .andExpect(jsonPath("$.[*].firstName").value(hasItem(DEFAULT_FIRST_NAME)))
            .andExpect(jsonPath("$.[*].lastName").value(hasItem(DEFAULT_LAST_NAME)))
            .andExpect(jsonPath("$.[*].isActive").value(hasItem(DEFAULT_IS_ACTIVE)));
    }

    @Test
    @Transactional
    void getUser() throws Exception {
        // Initialize the database
        insertedUser = userRepository.saveAndFlush(user);

        // Get the user
        restUserMockMvc
            .perform(get(ENTITY_API_URL_ID, user.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(user.getId().intValue()))
            .andExpect(jsonPath("$.email").value(DEFAULT_EMAIL))
            .andExpect(jsonPath("$.phone").value(DEFAULT_PHONE))
            .andExpect(jsonPath("$.passwordHash").value(DEFAULT_PASSWORD_HASH))
            .andExpect(jsonPath("$.firstName").value(DEFAULT_FIRST_NAME))
            .andExpect(jsonPath("$.lastName").value(DEFAULT_LAST_NAME))
            .andExpect(jsonPath("$.isActive").value(DEFAULT_IS_ACTIVE));
    }

    @Test
    @Transactional
    void getUsersByIdFiltering() throws Exception {
        // Initialize the database
        insertedUser = userRepository.saveAndFlush(user);

        Long id = user.getId();

        defaultUserFiltering("id.equals=" + id, "id.notEquals=" + id);

        defaultUserFiltering("id.greaterThanOrEqual=" + id, "id.greaterThan=" + id);

        defaultUserFiltering("id.lessThanOrEqual=" + id, "id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllUsersByPasswordHashIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedUser = userRepository.saveAndFlush(user);

        // Get all the userList where passwordHash equals to
        defaultUserFiltering("passwordHash.equals=" + DEFAULT_PASSWORD_HASH, "passwordHash.equals=" + UPDATED_PASSWORD_HASH);
    }

    @Test
    @Transactional
    void getAllUsersByPasswordHashIsInShouldWork() throws Exception {
        // Initialize the database
        insertedUser = userRepository.saveAndFlush(user);

        // Get all the userList where passwordHash in
        defaultUserFiltering(
            "passwordHash.in=" + DEFAULT_PASSWORD_HASH + "," + UPDATED_PASSWORD_HASH,
            "passwordHash.in=" + UPDATED_PASSWORD_HASH
        );
    }

    @Test
    @Transactional
    void getAllUsersByPasswordHashIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedUser = userRepository.saveAndFlush(user);

        // Get all the userList where passwordHash is not null
        defaultUserFiltering("passwordHash.specified=true", "passwordHash.specified=false");
    }

    @Test
    @Transactional
    void getAllUsersByPasswordHashContainsSomething() throws Exception {
        // Initialize the database
        insertedUser = userRepository.saveAndFlush(user);

        // Get all the userList where passwordHash contains
        defaultUserFiltering("passwordHash.contains=" + DEFAULT_PASSWORD_HASH, "passwordHash.contains=" + UPDATED_PASSWORD_HASH);
    }

    @Test
    @Transactional
    void getAllUsersByPasswordHashNotContainsSomething() throws Exception {
        // Initialize the database
        insertedUser = userRepository.saveAndFlush(user);

        // Get all the userList where passwordHash does not contain
        defaultUserFiltering(
            "passwordHash.doesNotContain=" + UPDATED_PASSWORD_HASH,
            "passwordHash.doesNotContain=" + DEFAULT_PASSWORD_HASH
        );
    }

    @Test
    @Transactional
    void getAllUsersByIsActiveIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedUser = userRepository.saveAndFlush(user);

        // Get all the userList where isActive equals to
        defaultUserFiltering("isActive.equals=" + DEFAULT_IS_ACTIVE, "isActive.equals=" + UPDATED_IS_ACTIVE);
    }

    @Test
    @Transactional
    void getAllUsersByIsActiveIsInShouldWork() throws Exception {
        // Initialize the database
        insertedUser = userRepository.saveAndFlush(user);

        // Get all the userList where isActive in
        defaultUserFiltering("isActive.in=" + DEFAULT_IS_ACTIVE + "," + UPDATED_IS_ACTIVE, "isActive.in=" + UPDATED_IS_ACTIVE);
    }

    @Test
    @Transactional
    void getAllUsersByIsActiveIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedUser = userRepository.saveAndFlush(user);

        // Get all the userList where isActive is not null
        defaultUserFiltering("isActive.specified=true", "isActive.specified=false");
    }

    @Test
    @Transactional
    void getAllUsersByTenantIsEqualToSomething() throws Exception {
        Tenant tenant;
        if (TestUtil.findAll(em, Tenant.class).isEmpty()) {
            userRepository.saveAndFlush(user);
            tenant = TenantResourceIT.createEntity();
        } else {
            tenant = TestUtil.findAll(em, Tenant.class).get(0);
        }
        em.persist(tenant);
        em.flush();
        user.setTenant(tenant);
        userRepository.saveAndFlush(user);
        Long tenantId = tenant.getId();
        // Get all the userList where tenant equals to tenantId
        defaultUserShouldBeFound("tenantId.equals=" + tenantId);

        // Get all the userList where tenant equals to (tenantId + 1)
        defaultUserShouldNotBeFound("tenantId.equals=" + (tenantId + 1));
    }

    private void defaultUserFiltering(String shouldBeFound, String shouldNotBeFound) throws Exception {
        defaultUserShouldBeFound(shouldBeFound);
        defaultUserShouldNotBeFound(shouldNotBeFound);
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultUserShouldBeFound(String filter) throws Exception {
        restUserMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(user.getId().intValue())))
            .andExpect(jsonPath("$.[*].email").value(hasItem(DEFAULT_EMAIL)))
            .andExpect(jsonPath("$.[*].phone").value(hasItem(DEFAULT_PHONE)))
            .andExpect(jsonPath("$.[*].passwordHash").value(hasItem(DEFAULT_PASSWORD_HASH)))
            .andExpect(jsonPath("$.[*].firstName").value(hasItem(DEFAULT_FIRST_NAME)))
            .andExpect(jsonPath("$.[*].lastName").value(hasItem(DEFAULT_LAST_NAME)))
            .andExpect(jsonPath("$.[*].isActive").value(hasItem(DEFAULT_IS_ACTIVE)));

        // Check, that the count call also returns 1
        restUserMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultUserShouldNotBeFound(String filter) throws Exception {
        restUserMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restUserMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingUser() throws Exception {
        // Get the user
        restUserMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingUser() throws Exception {
        // Initialize the database
        insertedUser = userRepository.saveAndFlush(user);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the user
        User updatedUser = userRepository.findById(user.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedUser are not directly saved in db
        em.detach(updatedUser);
        updatedUser
            .email(UPDATED_EMAIL)
            .phone(UPDATED_PHONE)
            .passwordHash(UPDATED_PASSWORD_HASH)
            .firstName(UPDATED_FIRST_NAME)
            .lastName(UPDATED_LAST_NAME)
            .isActive(UPDATED_IS_ACTIVE);
        UserDTO userDTO = userMapper.toDto(updatedUser);

        restUserMockMvc
            .perform(put(ENTITY_API_URL_ID, userDTO.getId()).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(userDTO)))
            .andExpect(status().isOk());

        // Validate the User in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedUserToMatchAllProperties(updatedUser);
    }

    @Test
    @Transactional
    void putNonExistingUser() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        user.setId(longCount.incrementAndGet());

        // Create the User
        UserDTO userDTO = userMapper.toDto(user);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restUserMockMvc
            .perform(put(ENTITY_API_URL_ID, userDTO.getId()).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(userDTO)))
            .andExpect(status().isBadRequest());

        // Validate the User in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchUser() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        user.setId(longCount.incrementAndGet());

        // Create the User
        UserDTO userDTO = userMapper.toDto(user);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restUserMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(userDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the User in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamUser() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        user.setId(longCount.incrementAndGet());

        // Create the User
        UserDTO userDTO = userMapper.toDto(user);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restUserMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(userDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the User in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateUserWithPatch() throws Exception {
        // Initialize the database
        insertedUser = userRepository.saveAndFlush(user);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the user using partial update
        User partialUpdatedUser = new User();
        partialUpdatedUser.setId(user.getId());

        partialUpdatedUser.lastName(UPDATED_LAST_NAME);

        restUserMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedUser.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedUser))
            )
            .andExpect(status().isOk());

        // Validate the User in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertUserUpdatableFieldsEquals(createUpdateProxyForBean(partialUpdatedUser, user), getPersistedUser(user));
    }

    @Test
    @Transactional
    void fullUpdateUserWithPatch() throws Exception {
        // Initialize the database
        insertedUser = userRepository.saveAndFlush(user);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the user using partial update
        User partialUpdatedUser = new User();
        partialUpdatedUser.setId(user.getId());

        partialUpdatedUser
            .email(UPDATED_EMAIL)
            .phone(UPDATED_PHONE)
            .passwordHash(UPDATED_PASSWORD_HASH)
            .firstName(UPDATED_FIRST_NAME)
            .lastName(UPDATED_LAST_NAME)
            .isActive(UPDATED_IS_ACTIVE);

        restUserMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedUser.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedUser))
            )
            .andExpect(status().isOk());

        // Validate the User in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertUserUpdatableFieldsEquals(partialUpdatedUser, getPersistedUser(partialUpdatedUser));
    }

    @Test
    @Transactional
    void patchNonExistingUser() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        user.setId(longCount.incrementAndGet());

        // Create the User
        UserDTO userDTO = userMapper.toDto(user);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restUserMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, userDTO.getId()).contentType("application/merge-patch+json").content(om.writeValueAsBytes(userDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the User in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchUser() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        user.setId(longCount.incrementAndGet());

        // Create the User
        UserDTO userDTO = userMapper.toDto(user);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restUserMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(userDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the User in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamUser() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        user.setId(longCount.incrementAndGet());

        // Create the User
        UserDTO userDTO = userMapper.toDto(user);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restUserMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(om.writeValueAsBytes(userDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the User in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteUser() throws Exception {
        // Initialize the database
        insertedUser = userRepository.saveAndFlush(user);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the user
        restUserMockMvc
            .perform(delete(ENTITY_API_URL_ID, user.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return userRepository.count();
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

    protected User getPersistedUser(User user) {
        return userRepository.findById(user.getId()).orElseThrow();
    }

    protected void assertPersistedUserToMatchAllProperties(User expectedUser) {
        assertUserAllPropertiesEquals(expectedUser, getPersistedUser(expectedUser));
    }

    protected void assertPersistedUserToMatchUpdatableProperties(User expectedUser) {
        assertUserAllUpdatablePropertiesEquals(expectedUser, getPersistedUser(expectedUser));
    }
}
