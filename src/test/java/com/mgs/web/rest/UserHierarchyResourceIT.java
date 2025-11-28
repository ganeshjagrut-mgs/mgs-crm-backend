package com.mgs.web.rest;

import static com.mgs.domain.UserHierarchyAsserts.*;
import static com.mgs.web.rest.TestUtil.createUpdateProxyForBean;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mgs.IntegrationTest;
import com.mgs.domain.Tenant;
import com.mgs.domain.User;
import com.mgs.domain.UserHierarchy;
import com.mgs.repository.UserHierarchyRepository;
import com.mgs.service.dto.UserHierarchyDTO;
import com.mgs.service.mapper.UserHierarchyMapper;
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
 * Integration tests for the {@link UserHierarchyResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class UserHierarchyResourceIT {

    private static final String DEFAULT_RELATIONSHIP_TYPE = "AAAAAAAAAA";
    private static final String UPDATED_RELATIONSHIP_TYPE = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/user-hierarchies";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ObjectMapper om;

    @Autowired
    private UserHierarchyRepository userHierarchyRepository;

    @Autowired
    private UserHierarchyMapper userHierarchyMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restUserHierarchyMockMvc;

    private UserHierarchy userHierarchy;

    private UserHierarchy insertedUserHierarchy;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static UserHierarchy createEntity(EntityManager em) {
        UserHierarchy userHierarchy = new UserHierarchy().relationshipType(DEFAULT_RELATIONSHIP_TYPE);
        // Add required entity
        Tenant tenant;
        if (TestUtil.findAll(em, Tenant.class).isEmpty()) {
            tenant = TenantResourceIT.createEntity();
            em.persist(tenant);
            em.flush();
        } else {
            tenant = TestUtil.findAll(em, Tenant.class).get(0);
        }
        userHierarchy.setTenant(tenant);
        // Add required entity
        User user = UserResourceIT.createEntity(em);
        em.persist(user);
        em.flush();
        userHierarchy.setParentUser(user);
        // Add required entity
        userHierarchy.setChildUser(user);
        return userHierarchy;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static UserHierarchy createUpdatedEntity(EntityManager em) {
        UserHierarchy updatedUserHierarchy = new UserHierarchy().relationshipType(UPDATED_RELATIONSHIP_TYPE);
        // Add required entity
        Tenant tenant;
        if (TestUtil.findAll(em, Tenant.class).isEmpty()) {
            tenant = TenantResourceIT.createUpdatedEntity();
            em.persist(tenant);
            em.flush();
        } else {
            tenant = TestUtil.findAll(em, Tenant.class).get(0);
        }
        updatedUserHierarchy.setTenant(tenant);
        // Add required entity
        User user = UserResourceIT.createEntity(em);
        em.persist(user);
        em.flush();
        updatedUserHierarchy.setParentUser(user);
        // Add required entity
        updatedUserHierarchy.setChildUser(user);
        return updatedUserHierarchy;
    }

    @BeforeEach
    void initTest() {
        userHierarchy = createEntity(em);
    }

    @AfterEach
    void cleanup() {
        if (insertedUserHierarchy != null) {
            userHierarchyRepository.delete(insertedUserHierarchy);
            insertedUserHierarchy = null;
        }
    }

    @Test
    @Transactional
    void createUserHierarchy() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the UserHierarchy
        UserHierarchyDTO userHierarchyDTO = userHierarchyMapper.toDto(userHierarchy);
        var returnedUserHierarchyDTO = om.readValue(
            restUserHierarchyMockMvc
                .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(userHierarchyDTO)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            UserHierarchyDTO.class
        );

        // Validate the UserHierarchy in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        var returnedUserHierarchy = userHierarchyMapper.toEntity(returnedUserHierarchyDTO);
        assertUserHierarchyUpdatableFieldsEquals(returnedUserHierarchy, getPersistedUserHierarchy(returnedUserHierarchy));

        insertedUserHierarchy = returnedUserHierarchy;
    }

    @Test
    @Transactional
    void createUserHierarchyWithExistingId() throws Exception {
        // Create the UserHierarchy with an existing ID
        userHierarchy.setId(1L);
        UserHierarchyDTO userHierarchyDTO = userHierarchyMapper.toDto(userHierarchy);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restUserHierarchyMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(userHierarchyDTO)))
            .andExpect(status().isBadRequest());

        // Validate the UserHierarchy in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllUserHierarchies() throws Exception {
        // Initialize the database
        insertedUserHierarchy = userHierarchyRepository.saveAndFlush(userHierarchy);

        // Get all the userHierarchyList
        restUserHierarchyMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(userHierarchy.getId().intValue())))
            .andExpect(jsonPath("$.[*].relationshipType").value(hasItem(DEFAULT_RELATIONSHIP_TYPE)));
    }

    @Test
    @Transactional
    void getUserHierarchy() throws Exception {
        // Initialize the database
        insertedUserHierarchy = userHierarchyRepository.saveAndFlush(userHierarchy);

        // Get the userHierarchy
        restUserHierarchyMockMvc
            .perform(get(ENTITY_API_URL_ID, userHierarchy.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(userHierarchy.getId().intValue()))
            .andExpect(jsonPath("$.relationshipType").value(DEFAULT_RELATIONSHIP_TYPE));
    }

    @Test
    @Transactional
    void getUserHierarchiesByIdFiltering() throws Exception {
        // Initialize the database
        insertedUserHierarchy = userHierarchyRepository.saveAndFlush(userHierarchy);

        Long id = userHierarchy.getId();

        defaultUserHierarchyFiltering("id.equals=" + id, "id.notEquals=" + id);

        defaultUserHierarchyFiltering("id.greaterThanOrEqual=" + id, "id.greaterThan=" + id);

        defaultUserHierarchyFiltering("id.lessThanOrEqual=" + id, "id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllUserHierarchiesByRelationshipTypeIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedUserHierarchy = userHierarchyRepository.saveAndFlush(userHierarchy);

        // Get all the userHierarchyList where relationshipType equals to
        defaultUserHierarchyFiltering(
            "relationshipType.equals=" + DEFAULT_RELATIONSHIP_TYPE,
            "relationshipType.equals=" + UPDATED_RELATIONSHIP_TYPE
        );
    }

    @Test
    @Transactional
    void getAllUserHierarchiesByRelationshipTypeIsInShouldWork() throws Exception {
        // Initialize the database
        insertedUserHierarchy = userHierarchyRepository.saveAndFlush(userHierarchy);

        // Get all the userHierarchyList where relationshipType in
        defaultUserHierarchyFiltering(
            "relationshipType.in=" + DEFAULT_RELATIONSHIP_TYPE + "," + UPDATED_RELATIONSHIP_TYPE,
            "relationshipType.in=" + UPDATED_RELATIONSHIP_TYPE
        );
    }

    @Test
    @Transactional
    void getAllUserHierarchiesByRelationshipTypeIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedUserHierarchy = userHierarchyRepository.saveAndFlush(userHierarchy);

        // Get all the userHierarchyList where relationshipType is not null
        defaultUserHierarchyFiltering("relationshipType.specified=true", "relationshipType.specified=false");
    }

    @Test
    @Transactional
    void getAllUserHierarchiesByRelationshipTypeContainsSomething() throws Exception {
        // Initialize the database
        insertedUserHierarchy = userHierarchyRepository.saveAndFlush(userHierarchy);

        // Get all the userHierarchyList where relationshipType contains
        defaultUserHierarchyFiltering(
            "relationshipType.contains=" + DEFAULT_RELATIONSHIP_TYPE,
            "relationshipType.contains=" + UPDATED_RELATIONSHIP_TYPE
        );
    }

    @Test
    @Transactional
    void getAllUserHierarchiesByRelationshipTypeNotContainsSomething() throws Exception {
        // Initialize the database
        insertedUserHierarchy = userHierarchyRepository.saveAndFlush(userHierarchy);

        // Get all the userHierarchyList where relationshipType does not contain
        defaultUserHierarchyFiltering(
            "relationshipType.doesNotContain=" + UPDATED_RELATIONSHIP_TYPE,
            "relationshipType.doesNotContain=" + DEFAULT_RELATIONSHIP_TYPE
        );
    }

    @Test
    @Transactional
    void getAllUserHierarchiesByTenantIsEqualToSomething() throws Exception {
        Tenant tenant;
        if (TestUtil.findAll(em, Tenant.class).isEmpty()) {
            userHierarchyRepository.saveAndFlush(userHierarchy);
            tenant = TenantResourceIT.createEntity();
        } else {
            tenant = TestUtil.findAll(em, Tenant.class).get(0);
        }
        em.persist(tenant);
        em.flush();
        userHierarchy.setTenant(tenant);
        userHierarchyRepository.saveAndFlush(userHierarchy);
        Long tenantId = tenant.getId();
        // Get all the userHierarchyList where tenant equals to tenantId
        defaultUserHierarchyShouldBeFound("tenantId.equals=" + tenantId);

        // Get all the userHierarchyList where tenant equals to (tenantId + 1)
        defaultUserHierarchyShouldNotBeFound("tenantId.equals=" + (tenantId + 1));
    }

    @Test
    @Transactional
    void getAllUserHierarchiesByParentUserIsEqualToSomething() throws Exception {
        User parentUser;
        if (TestUtil.findAll(em, User.class).isEmpty()) {
            userHierarchyRepository.saveAndFlush(userHierarchy);
            parentUser = UserResourceIT.createEntity(em);
        } else {
            parentUser = TestUtil.findAll(em, User.class).get(0);
        }
        em.persist(parentUser);
        em.flush();
        userHierarchy.setParentUser(parentUser);
        userHierarchyRepository.saveAndFlush(userHierarchy);
        Long parentUserId = parentUser.getId();
        // Get all the userHierarchyList where parentUser equals to parentUserId
        defaultUserHierarchyShouldBeFound("parentUserId.equals=" + parentUserId);

        // Get all the userHierarchyList where parentUser equals to (parentUserId + 1)
        defaultUserHierarchyShouldNotBeFound("parentUserId.equals=" + (parentUserId + 1));
    }

    @Test
    @Transactional
    void getAllUserHierarchiesByChildUserIsEqualToSomething() throws Exception {
        User childUser;
        if (TestUtil.findAll(em, User.class).isEmpty()) {
            userHierarchyRepository.saveAndFlush(userHierarchy);
            childUser = UserResourceIT.createEntity(em);
        } else {
            childUser = TestUtil.findAll(em, User.class).get(0);
        }
        em.persist(childUser);
        em.flush();
        userHierarchy.setChildUser(childUser);
        userHierarchyRepository.saveAndFlush(userHierarchy);
        Long childUserId = childUser.getId();
        // Get all the userHierarchyList where childUser equals to childUserId
        defaultUserHierarchyShouldBeFound("childUserId.equals=" + childUserId);

        // Get all the userHierarchyList where childUser equals to (childUserId + 1)
        defaultUserHierarchyShouldNotBeFound("childUserId.equals=" + (childUserId + 1));
    }

    private void defaultUserHierarchyFiltering(String shouldBeFound, String shouldNotBeFound) throws Exception {
        defaultUserHierarchyShouldBeFound(shouldBeFound);
        defaultUserHierarchyShouldNotBeFound(shouldNotBeFound);
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultUserHierarchyShouldBeFound(String filter) throws Exception {
        restUserHierarchyMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(userHierarchy.getId().intValue())))
            .andExpect(jsonPath("$.[*].relationshipType").value(hasItem(DEFAULT_RELATIONSHIP_TYPE)));

        // Check, that the count call also returns 1
        restUserHierarchyMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultUserHierarchyShouldNotBeFound(String filter) throws Exception {
        restUserHierarchyMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restUserHierarchyMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingUserHierarchy() throws Exception {
        // Get the userHierarchy
        restUserHierarchyMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingUserHierarchy() throws Exception {
        // Initialize the database
        insertedUserHierarchy = userHierarchyRepository.saveAndFlush(userHierarchy);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the userHierarchy
        UserHierarchy updatedUserHierarchy = userHierarchyRepository.findById(userHierarchy.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedUserHierarchy are not directly saved in db
        em.detach(updatedUserHierarchy);
        updatedUserHierarchy.relationshipType(UPDATED_RELATIONSHIP_TYPE);
        UserHierarchyDTO userHierarchyDTO = userHierarchyMapper.toDto(updatedUserHierarchy);

        restUserHierarchyMockMvc
            .perform(
                put(ENTITY_API_URL_ID, userHierarchyDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(userHierarchyDTO))
            )
            .andExpect(status().isOk());

        // Validate the UserHierarchy in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedUserHierarchyToMatchAllProperties(updatedUserHierarchy);
    }

    @Test
    @Transactional
    void putNonExistingUserHierarchy() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        userHierarchy.setId(longCount.incrementAndGet());

        // Create the UserHierarchy
        UserHierarchyDTO userHierarchyDTO = userHierarchyMapper.toDto(userHierarchy);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restUserHierarchyMockMvc
            .perform(
                put(ENTITY_API_URL_ID, userHierarchyDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(userHierarchyDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the UserHierarchy in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchUserHierarchy() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        userHierarchy.setId(longCount.incrementAndGet());

        // Create the UserHierarchy
        UserHierarchyDTO userHierarchyDTO = userHierarchyMapper.toDto(userHierarchy);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restUserHierarchyMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(userHierarchyDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the UserHierarchy in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamUserHierarchy() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        userHierarchy.setId(longCount.incrementAndGet());

        // Create the UserHierarchy
        UserHierarchyDTO userHierarchyDTO = userHierarchyMapper.toDto(userHierarchy);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restUserHierarchyMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(userHierarchyDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the UserHierarchy in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateUserHierarchyWithPatch() throws Exception {
        // Initialize the database
        insertedUserHierarchy = userHierarchyRepository.saveAndFlush(userHierarchy);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the userHierarchy using partial update
        UserHierarchy partialUpdatedUserHierarchy = new UserHierarchy();
        partialUpdatedUserHierarchy.setId(userHierarchy.getId());

        partialUpdatedUserHierarchy.relationshipType(UPDATED_RELATIONSHIP_TYPE);

        restUserHierarchyMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedUserHierarchy.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedUserHierarchy))
            )
            .andExpect(status().isOk());

        // Validate the UserHierarchy in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertUserHierarchyUpdatableFieldsEquals(
            createUpdateProxyForBean(partialUpdatedUserHierarchy, userHierarchy),
            getPersistedUserHierarchy(userHierarchy)
        );
    }

    @Test
    @Transactional
    void fullUpdateUserHierarchyWithPatch() throws Exception {
        // Initialize the database
        insertedUserHierarchy = userHierarchyRepository.saveAndFlush(userHierarchy);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the userHierarchy using partial update
        UserHierarchy partialUpdatedUserHierarchy = new UserHierarchy();
        partialUpdatedUserHierarchy.setId(userHierarchy.getId());

        partialUpdatedUserHierarchy.relationshipType(UPDATED_RELATIONSHIP_TYPE);

        restUserHierarchyMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedUserHierarchy.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedUserHierarchy))
            )
            .andExpect(status().isOk());

        // Validate the UserHierarchy in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertUserHierarchyUpdatableFieldsEquals(partialUpdatedUserHierarchy, getPersistedUserHierarchy(partialUpdatedUserHierarchy));
    }

    @Test
    @Transactional
    void patchNonExistingUserHierarchy() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        userHierarchy.setId(longCount.incrementAndGet());

        // Create the UserHierarchy
        UserHierarchyDTO userHierarchyDTO = userHierarchyMapper.toDto(userHierarchy);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restUserHierarchyMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, userHierarchyDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(userHierarchyDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the UserHierarchy in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchUserHierarchy() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        userHierarchy.setId(longCount.incrementAndGet());

        // Create the UserHierarchy
        UserHierarchyDTO userHierarchyDTO = userHierarchyMapper.toDto(userHierarchy);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restUserHierarchyMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(userHierarchyDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the UserHierarchy in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamUserHierarchy() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        userHierarchy.setId(longCount.incrementAndGet());

        // Create the UserHierarchy
        UserHierarchyDTO userHierarchyDTO = userHierarchyMapper.toDto(userHierarchy);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restUserHierarchyMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(om.writeValueAsBytes(userHierarchyDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the UserHierarchy in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteUserHierarchy() throws Exception {
        // Initialize the database
        insertedUserHierarchy = userHierarchyRepository.saveAndFlush(userHierarchy);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the userHierarchy
        restUserHierarchyMockMvc
            .perform(delete(ENTITY_API_URL_ID, userHierarchy.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return userHierarchyRepository.count();
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

    protected UserHierarchy getPersistedUserHierarchy(UserHierarchy userHierarchy) {
        return userHierarchyRepository.findById(userHierarchy.getId()).orElseThrow();
    }

    protected void assertPersistedUserHierarchyToMatchAllProperties(UserHierarchy expectedUserHierarchy) {
        assertUserHierarchyAllPropertiesEquals(expectedUserHierarchy, getPersistedUserHierarchy(expectedUserHierarchy));
    }

    protected void assertPersistedUserHierarchyToMatchUpdatableProperties(UserHierarchy expectedUserHierarchy) {
        assertUserHierarchyAllUpdatablePropertiesEquals(expectedUserHierarchy, getPersistedUserHierarchy(expectedUserHierarchy));
    }
}
