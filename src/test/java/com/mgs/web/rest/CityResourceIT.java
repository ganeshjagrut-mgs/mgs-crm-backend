package com.mgs.web.rest;

import static com.mgs.domain.CityAsserts.*;
import static com.mgs.web.rest.TestUtil.createUpdateProxyForBean;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mgs.IntegrationTest;
import com.mgs.domain.City;
import com.mgs.domain.State;
import com.mgs.repository.CityRepository;
import com.mgs.service.dto.CityDTO;
import com.mgs.service.mapper.CityMapper;
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
 * Integration tests for the {@link CityResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class CityResourceIT {

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_CODE = "AAAAAAAAAA";
    private static final String UPDATED_CODE = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/cities";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ObjectMapper om;

    @Autowired
    private CityRepository cityRepository;

    @Autowired
    private CityMapper cityMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restCityMockMvc;

    private City city;

    private City insertedCity;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static City createEntity(EntityManager em) {
        City city = new City().name(DEFAULT_NAME).code(DEFAULT_CODE);
        // Add required entity
        State state;
        if (TestUtil.findAll(em, State.class).isEmpty()) {
            state = StateResourceIT.createEntity(em);
            em.persist(state);
            em.flush();
        } else {
            state = TestUtil.findAll(em, State.class).get(0);
        }
        city.setState(state);
        return city;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static City createUpdatedEntity(EntityManager em) {
        City updatedCity = new City().name(UPDATED_NAME).code(UPDATED_CODE);
        // Add required entity
        State state;
        if (TestUtil.findAll(em, State.class).isEmpty()) {
            state = StateResourceIT.createUpdatedEntity(em);
            em.persist(state);
            em.flush();
        } else {
            state = TestUtil.findAll(em, State.class).get(0);
        }
        updatedCity.setState(state);
        return updatedCity;
    }

    @BeforeEach
    void initTest() {
        city = createEntity(em);
    }

    @AfterEach
    void cleanup() {
        if (insertedCity != null) {
            cityRepository.delete(insertedCity);
            insertedCity = null;
        }
    }

    @Test
    @Transactional
    void createCity() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the City
        CityDTO cityDTO = cityMapper.toDto(city);
        var returnedCityDTO = om.readValue(
            restCityMockMvc
                .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(cityDTO)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            CityDTO.class
        );

        // Validate the City in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        var returnedCity = cityMapper.toEntity(returnedCityDTO);
        assertCityUpdatableFieldsEquals(returnedCity, getPersistedCity(returnedCity));

        insertedCity = returnedCity;
    }

    @Test
    @Transactional
    void createCityWithExistingId() throws Exception {
        // Create the City with an existing ID
        city.setId(1L);
        CityDTO cityDTO = cityMapper.toDto(city);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restCityMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(cityDTO)))
            .andExpect(status().isBadRequest());

        // Validate the City in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkNameIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        city.setName(null);

        // Create the City, which fails.
        CityDTO cityDTO = cityMapper.toDto(city);

        restCityMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(cityDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllCities() throws Exception {
        // Initialize the database
        insertedCity = cityRepository.saveAndFlush(city);

        // Get all the cityList
        restCityMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(city.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)))
            .andExpect(jsonPath("$.[*].code").value(hasItem(DEFAULT_CODE)));
    }

    @Test
    @Transactional
    void getCity() throws Exception {
        // Initialize the database
        insertedCity = cityRepository.saveAndFlush(city);

        // Get the city
        restCityMockMvc
            .perform(get(ENTITY_API_URL_ID, city.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(city.getId().intValue()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME))
            .andExpect(jsonPath("$.code").value(DEFAULT_CODE));
    }

    @Test
    @Transactional
    void getCitiesByIdFiltering() throws Exception {
        // Initialize the database
        insertedCity = cityRepository.saveAndFlush(city);

        Long id = city.getId();

        defaultCityFiltering("id.equals=" + id, "id.notEquals=" + id);

        defaultCityFiltering("id.greaterThanOrEqual=" + id, "id.greaterThan=" + id);

        defaultCityFiltering("id.lessThanOrEqual=" + id, "id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllCitiesByNameIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedCity = cityRepository.saveAndFlush(city);

        // Get all the cityList where name equals to
        defaultCityFiltering("name.equals=" + DEFAULT_NAME, "name.equals=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllCitiesByNameIsInShouldWork() throws Exception {
        // Initialize the database
        insertedCity = cityRepository.saveAndFlush(city);

        // Get all the cityList where name in
        defaultCityFiltering("name.in=" + DEFAULT_NAME + "," + UPDATED_NAME, "name.in=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllCitiesByNameIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedCity = cityRepository.saveAndFlush(city);

        // Get all the cityList where name is not null
        defaultCityFiltering("name.specified=true", "name.specified=false");
    }

    @Test
    @Transactional
    void getAllCitiesByNameContainsSomething() throws Exception {
        // Initialize the database
        insertedCity = cityRepository.saveAndFlush(city);

        // Get all the cityList where name contains
        defaultCityFiltering("name.contains=" + DEFAULT_NAME, "name.contains=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllCitiesByNameNotContainsSomething() throws Exception {
        // Initialize the database
        insertedCity = cityRepository.saveAndFlush(city);

        // Get all the cityList where name does not contain
        defaultCityFiltering("name.doesNotContain=" + UPDATED_NAME, "name.doesNotContain=" + DEFAULT_NAME);
    }

    @Test
    @Transactional
    void getAllCitiesByCodeIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedCity = cityRepository.saveAndFlush(city);

        // Get all the cityList where code equals to
        defaultCityFiltering("code.equals=" + DEFAULT_CODE, "code.equals=" + UPDATED_CODE);
    }

    @Test
    @Transactional
    void getAllCitiesByCodeIsInShouldWork() throws Exception {
        // Initialize the database
        insertedCity = cityRepository.saveAndFlush(city);

        // Get all the cityList where code in
        defaultCityFiltering("code.in=" + DEFAULT_CODE + "," + UPDATED_CODE, "code.in=" + UPDATED_CODE);
    }

    @Test
    @Transactional
    void getAllCitiesByCodeIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedCity = cityRepository.saveAndFlush(city);

        // Get all the cityList where code is not null
        defaultCityFiltering("code.specified=true", "code.specified=false");
    }

    @Test
    @Transactional
    void getAllCitiesByCodeContainsSomething() throws Exception {
        // Initialize the database
        insertedCity = cityRepository.saveAndFlush(city);

        // Get all the cityList where code contains
        defaultCityFiltering("code.contains=" + DEFAULT_CODE, "code.contains=" + UPDATED_CODE);
    }

    @Test
    @Transactional
    void getAllCitiesByCodeNotContainsSomething() throws Exception {
        // Initialize the database
        insertedCity = cityRepository.saveAndFlush(city);

        // Get all the cityList where code does not contain
        defaultCityFiltering("code.doesNotContain=" + UPDATED_CODE, "code.doesNotContain=" + DEFAULT_CODE);
    }

    @Test
    @Transactional
    void getAllCitiesByStateIsEqualToSomething() throws Exception {
        State state;
        if (TestUtil.findAll(em, State.class).isEmpty()) {
            cityRepository.saveAndFlush(city);
            state = StateResourceIT.createEntity(em);
        } else {
            state = TestUtil.findAll(em, State.class).get(0);
        }
        em.persist(state);
        em.flush();
        city.setState(state);
        cityRepository.saveAndFlush(city);
        Long stateId = state.getId();
        // Get all the cityList where state equals to stateId
        defaultCityShouldBeFound("stateId.equals=" + stateId);

        // Get all the cityList where state equals to (stateId + 1)
        defaultCityShouldNotBeFound("stateId.equals=" + (stateId + 1));
    }

    private void defaultCityFiltering(String shouldBeFound, String shouldNotBeFound) throws Exception {
        defaultCityShouldBeFound(shouldBeFound);
        defaultCityShouldNotBeFound(shouldNotBeFound);
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultCityShouldBeFound(String filter) throws Exception {
        restCityMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(city.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)))
            .andExpect(jsonPath("$.[*].code").value(hasItem(DEFAULT_CODE)));

        // Check, that the count call also returns 1
        restCityMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultCityShouldNotBeFound(String filter) throws Exception {
        restCityMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restCityMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingCity() throws Exception {
        // Get the city
        restCityMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingCity() throws Exception {
        // Initialize the database
        insertedCity = cityRepository.saveAndFlush(city);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the city
        City updatedCity = cityRepository.findById(city.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedCity are not directly saved in db
        em.detach(updatedCity);
        updatedCity.name(UPDATED_NAME).code(UPDATED_CODE);
        CityDTO cityDTO = cityMapper.toDto(updatedCity);

        restCityMockMvc
            .perform(put(ENTITY_API_URL_ID, cityDTO.getId()).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(cityDTO)))
            .andExpect(status().isOk());

        // Validate the City in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedCityToMatchAllProperties(updatedCity);
    }

    @Test
    @Transactional
    void putNonExistingCity() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        city.setId(longCount.incrementAndGet());

        // Create the City
        CityDTO cityDTO = cityMapper.toDto(city);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restCityMockMvc
            .perform(put(ENTITY_API_URL_ID, cityDTO.getId()).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(cityDTO)))
            .andExpect(status().isBadRequest());

        // Validate the City in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchCity() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        city.setId(longCount.incrementAndGet());

        // Create the City
        CityDTO cityDTO = cityMapper.toDto(city);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCityMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(cityDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the City in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamCity() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        city.setId(longCount.incrementAndGet());

        // Create the City
        CityDTO cityDTO = cityMapper.toDto(city);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCityMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(cityDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the City in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateCityWithPatch() throws Exception {
        // Initialize the database
        insertedCity = cityRepository.saveAndFlush(city);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the city using partial update
        City partialUpdatedCity = new City();
        partialUpdatedCity.setId(city.getId());

        partialUpdatedCity.name(UPDATED_NAME);

        restCityMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedCity.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedCity))
            )
            .andExpect(status().isOk());

        // Validate the City in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertCityUpdatableFieldsEquals(createUpdateProxyForBean(partialUpdatedCity, city), getPersistedCity(city));
    }

    @Test
    @Transactional
    void fullUpdateCityWithPatch() throws Exception {
        // Initialize the database
        insertedCity = cityRepository.saveAndFlush(city);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the city using partial update
        City partialUpdatedCity = new City();
        partialUpdatedCity.setId(city.getId());

        partialUpdatedCity.name(UPDATED_NAME).code(UPDATED_CODE);

        restCityMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedCity.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedCity))
            )
            .andExpect(status().isOk());

        // Validate the City in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertCityUpdatableFieldsEquals(partialUpdatedCity, getPersistedCity(partialUpdatedCity));
    }

    @Test
    @Transactional
    void patchNonExistingCity() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        city.setId(longCount.incrementAndGet());

        // Create the City
        CityDTO cityDTO = cityMapper.toDto(city);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restCityMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, cityDTO.getId()).contentType("application/merge-patch+json").content(om.writeValueAsBytes(cityDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the City in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchCity() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        city.setId(longCount.incrementAndGet());

        // Create the City
        CityDTO cityDTO = cityMapper.toDto(city);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCityMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(cityDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the City in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamCity() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        city.setId(longCount.incrementAndGet());

        // Create the City
        CityDTO cityDTO = cityMapper.toDto(city);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCityMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(om.writeValueAsBytes(cityDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the City in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteCity() throws Exception {
        // Initialize the database
        insertedCity = cityRepository.saveAndFlush(city);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the city
        restCityMockMvc
            .perform(delete(ENTITY_API_URL_ID, city.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return cityRepository.count();
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

    protected City getPersistedCity(City city) {
        return cityRepository.findById(city.getId()).orElseThrow();
    }

    protected void assertPersistedCityToMatchAllProperties(City expectedCity) {
        assertCityAllPropertiesEquals(expectedCity, getPersistedCity(expectedCity));
    }

    protected void assertPersistedCityToMatchUpdatableProperties(City expectedCity) {
        assertCityAllUpdatablePropertiesEquals(expectedCity, getPersistedCity(expectedCity));
    }
}
