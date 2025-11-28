package com.mgs.web.rest;

import static com.mgs.domain.CountryAsserts.*;
import static com.mgs.web.rest.TestUtil.createUpdateProxyForBean;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mgs.IntegrationTest;
import com.mgs.domain.Country;
import com.mgs.repository.CountryRepository;
import com.mgs.service.dto.CountryDTO;
import com.mgs.service.mapper.CountryMapper;
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
 * Integration tests for the {@link CountryResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class CountryResourceIT {

    private static final String DEFAULT_ISO_CODE_2 = "AAAAAAAAAA";
    private static final String UPDATED_ISO_CODE_2 = "BBBBBBBBBB";

    private static final String DEFAULT_ISO_CODE_3 = "AAAAAAAAAA";
    private static final String UPDATED_ISO_CODE_3 = "BBBBBBBBBB";

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/countries";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ObjectMapper om;

    @Autowired
    private CountryRepository countryRepository;

    @Autowired
    private CountryMapper countryMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restCountryMockMvc;

    private Country country;

    private Country insertedCountry;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Country createEntity() {
        return new Country().isoCode2(DEFAULT_ISO_CODE_2).isoCode3(DEFAULT_ISO_CODE_3).name(DEFAULT_NAME);
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Country createUpdatedEntity() {
        return new Country().isoCode2(UPDATED_ISO_CODE_2).isoCode3(UPDATED_ISO_CODE_3).name(UPDATED_NAME);
    }

    @BeforeEach
    void initTest() {
        country = createEntity();
    }

    @AfterEach
    void cleanup() {
        if (insertedCountry != null) {
            countryRepository.delete(insertedCountry);
            insertedCountry = null;
        }
    }

    @Test
    @Transactional
    void createCountry() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the Country
        CountryDTO countryDTO = countryMapper.toDto(country);
        var returnedCountryDTO = om.readValue(
            restCountryMockMvc
                .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(countryDTO)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            CountryDTO.class
        );

        // Validate the Country in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        var returnedCountry = countryMapper.toEntity(returnedCountryDTO);
        assertCountryUpdatableFieldsEquals(returnedCountry, getPersistedCountry(returnedCountry));

        insertedCountry = returnedCountry;
    }

    @Test
    @Transactional
    void createCountryWithExistingId() throws Exception {
        // Create the Country with an existing ID
        country.setId(1L);
        CountryDTO countryDTO = countryMapper.toDto(country);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restCountryMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(countryDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Country in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkNameIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        country.setName(null);

        // Create the Country, which fails.
        CountryDTO countryDTO = countryMapper.toDto(country);

        restCountryMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(countryDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllCountries() throws Exception {
        // Initialize the database
        insertedCountry = countryRepository.saveAndFlush(country);

        // Get all the countryList
        restCountryMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(country.getId().intValue())))
            .andExpect(jsonPath("$.[*].isoCode2").value(hasItem(DEFAULT_ISO_CODE_2)))
            .andExpect(jsonPath("$.[*].isoCode3").value(hasItem(DEFAULT_ISO_CODE_3)))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)));
    }

    @Test
    @Transactional
    void getCountry() throws Exception {
        // Initialize the database
        insertedCountry = countryRepository.saveAndFlush(country);

        // Get the country
        restCountryMockMvc
            .perform(get(ENTITY_API_URL_ID, country.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(country.getId().intValue()))
            .andExpect(jsonPath("$.isoCode2").value(DEFAULT_ISO_CODE_2))
            .andExpect(jsonPath("$.isoCode3").value(DEFAULT_ISO_CODE_3))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME));
    }

    @Test
    @Transactional
    void getCountriesByIdFiltering() throws Exception {
        // Initialize the database
        insertedCountry = countryRepository.saveAndFlush(country);

        Long id = country.getId();

        defaultCountryFiltering("id.equals=" + id, "id.notEquals=" + id);

        defaultCountryFiltering("id.greaterThanOrEqual=" + id, "id.greaterThan=" + id);

        defaultCountryFiltering("id.lessThanOrEqual=" + id, "id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllCountriesByIsoCode2IsEqualToSomething() throws Exception {
        // Initialize the database
        insertedCountry = countryRepository.saveAndFlush(country);

        // Get all the countryList where isoCode2 equals to
        defaultCountryFiltering("isoCode2.equals=" + DEFAULT_ISO_CODE_2, "isoCode2.equals=" + UPDATED_ISO_CODE_2);
    }

    @Test
    @Transactional
    void getAllCountriesByIsoCode2IsInShouldWork() throws Exception {
        // Initialize the database
        insertedCountry = countryRepository.saveAndFlush(country);

        // Get all the countryList where isoCode2 in
        defaultCountryFiltering("isoCode2.in=" + DEFAULT_ISO_CODE_2 + "," + UPDATED_ISO_CODE_2, "isoCode2.in=" + UPDATED_ISO_CODE_2);
    }

    @Test
    @Transactional
    void getAllCountriesByIsoCode2IsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedCountry = countryRepository.saveAndFlush(country);

        // Get all the countryList where isoCode2 is not null
        defaultCountryFiltering("isoCode2.specified=true", "isoCode2.specified=false");
    }

    @Test
    @Transactional
    void getAllCountriesByIsoCode2ContainsSomething() throws Exception {
        // Initialize the database
        insertedCountry = countryRepository.saveAndFlush(country);

        // Get all the countryList where isoCode2 contains
        defaultCountryFiltering("isoCode2.contains=" + DEFAULT_ISO_CODE_2, "isoCode2.contains=" + UPDATED_ISO_CODE_2);
    }

    @Test
    @Transactional
    void getAllCountriesByIsoCode2NotContainsSomething() throws Exception {
        // Initialize the database
        insertedCountry = countryRepository.saveAndFlush(country);

        // Get all the countryList where isoCode2 does not contain
        defaultCountryFiltering("isoCode2.doesNotContain=" + UPDATED_ISO_CODE_2, "isoCode2.doesNotContain=" + DEFAULT_ISO_CODE_2);
    }

    @Test
    @Transactional
    void getAllCountriesByIsoCode3IsEqualToSomething() throws Exception {
        // Initialize the database
        insertedCountry = countryRepository.saveAndFlush(country);

        // Get all the countryList where isoCode3 equals to
        defaultCountryFiltering("isoCode3.equals=" + DEFAULT_ISO_CODE_3, "isoCode3.equals=" + UPDATED_ISO_CODE_3);
    }

    @Test
    @Transactional
    void getAllCountriesByIsoCode3IsInShouldWork() throws Exception {
        // Initialize the database
        insertedCountry = countryRepository.saveAndFlush(country);

        // Get all the countryList where isoCode3 in
        defaultCountryFiltering("isoCode3.in=" + DEFAULT_ISO_CODE_3 + "," + UPDATED_ISO_CODE_3, "isoCode3.in=" + UPDATED_ISO_CODE_3);
    }

    @Test
    @Transactional
    void getAllCountriesByIsoCode3IsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedCountry = countryRepository.saveAndFlush(country);

        // Get all the countryList where isoCode3 is not null
        defaultCountryFiltering("isoCode3.specified=true", "isoCode3.specified=false");
    }

    @Test
    @Transactional
    void getAllCountriesByIsoCode3ContainsSomething() throws Exception {
        // Initialize the database
        insertedCountry = countryRepository.saveAndFlush(country);

        // Get all the countryList where isoCode3 contains
        defaultCountryFiltering("isoCode3.contains=" + DEFAULT_ISO_CODE_3, "isoCode3.contains=" + UPDATED_ISO_CODE_3);
    }

    @Test
    @Transactional
    void getAllCountriesByIsoCode3NotContainsSomething() throws Exception {
        // Initialize the database
        insertedCountry = countryRepository.saveAndFlush(country);

        // Get all the countryList where isoCode3 does not contain
        defaultCountryFiltering("isoCode3.doesNotContain=" + UPDATED_ISO_CODE_3, "isoCode3.doesNotContain=" + DEFAULT_ISO_CODE_3);
    }

    @Test
    @Transactional
    void getAllCountriesByNameIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedCountry = countryRepository.saveAndFlush(country);

        // Get all the countryList where name equals to
        defaultCountryFiltering("name.equals=" + DEFAULT_NAME, "name.equals=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllCountriesByNameIsInShouldWork() throws Exception {
        // Initialize the database
        insertedCountry = countryRepository.saveAndFlush(country);

        // Get all the countryList where name in
        defaultCountryFiltering("name.in=" + DEFAULT_NAME + "," + UPDATED_NAME, "name.in=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllCountriesByNameIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedCountry = countryRepository.saveAndFlush(country);

        // Get all the countryList where name is not null
        defaultCountryFiltering("name.specified=true", "name.specified=false");
    }

    @Test
    @Transactional
    void getAllCountriesByNameContainsSomething() throws Exception {
        // Initialize the database
        insertedCountry = countryRepository.saveAndFlush(country);

        // Get all the countryList where name contains
        defaultCountryFiltering("name.contains=" + DEFAULT_NAME, "name.contains=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllCountriesByNameNotContainsSomething() throws Exception {
        // Initialize the database
        insertedCountry = countryRepository.saveAndFlush(country);

        // Get all the countryList where name does not contain
        defaultCountryFiltering("name.doesNotContain=" + UPDATED_NAME, "name.doesNotContain=" + DEFAULT_NAME);
    }

    private void defaultCountryFiltering(String shouldBeFound, String shouldNotBeFound) throws Exception {
        defaultCountryShouldBeFound(shouldBeFound);
        defaultCountryShouldNotBeFound(shouldNotBeFound);
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultCountryShouldBeFound(String filter) throws Exception {
        restCountryMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(country.getId().intValue())))
            .andExpect(jsonPath("$.[*].isoCode2").value(hasItem(DEFAULT_ISO_CODE_2)))
            .andExpect(jsonPath("$.[*].isoCode3").value(hasItem(DEFAULT_ISO_CODE_3)))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)));

        // Check, that the count call also returns 1
        restCountryMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultCountryShouldNotBeFound(String filter) throws Exception {
        restCountryMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restCountryMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingCountry() throws Exception {
        // Get the country
        restCountryMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingCountry() throws Exception {
        // Initialize the database
        insertedCountry = countryRepository.saveAndFlush(country);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the country
        Country updatedCountry = countryRepository.findById(country.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedCountry are not directly saved in db
        em.detach(updatedCountry);
        updatedCountry.isoCode2(UPDATED_ISO_CODE_2).isoCode3(UPDATED_ISO_CODE_3).name(UPDATED_NAME);
        CountryDTO countryDTO = countryMapper.toDto(updatedCountry);

        restCountryMockMvc
            .perform(
                put(ENTITY_API_URL_ID, countryDTO.getId()).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(countryDTO))
            )
            .andExpect(status().isOk());

        // Validate the Country in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedCountryToMatchAllProperties(updatedCountry);
    }

    @Test
    @Transactional
    void putNonExistingCountry() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        country.setId(longCount.incrementAndGet());

        // Create the Country
        CountryDTO countryDTO = countryMapper.toDto(country);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restCountryMockMvc
            .perform(
                put(ENTITY_API_URL_ID, countryDTO.getId()).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(countryDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Country in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchCountry() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        country.setId(longCount.incrementAndGet());

        // Create the Country
        CountryDTO countryDTO = countryMapper.toDto(country);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCountryMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(countryDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Country in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamCountry() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        country.setId(longCount.incrementAndGet());

        // Create the Country
        CountryDTO countryDTO = countryMapper.toDto(country);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCountryMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(countryDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Country in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateCountryWithPatch() throws Exception {
        // Initialize the database
        insertedCountry = countryRepository.saveAndFlush(country);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the country using partial update
        Country partialUpdatedCountry = new Country();
        partialUpdatedCountry.setId(country.getId());

        partialUpdatedCountry.isoCode3(UPDATED_ISO_CODE_3);

        restCountryMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedCountry.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedCountry))
            )
            .andExpect(status().isOk());

        // Validate the Country in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertCountryUpdatableFieldsEquals(createUpdateProxyForBean(partialUpdatedCountry, country), getPersistedCountry(country));
    }

    @Test
    @Transactional
    void fullUpdateCountryWithPatch() throws Exception {
        // Initialize the database
        insertedCountry = countryRepository.saveAndFlush(country);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the country using partial update
        Country partialUpdatedCountry = new Country();
        partialUpdatedCountry.setId(country.getId());

        partialUpdatedCountry.isoCode2(UPDATED_ISO_CODE_2).isoCode3(UPDATED_ISO_CODE_3).name(UPDATED_NAME);

        restCountryMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedCountry.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedCountry))
            )
            .andExpect(status().isOk());

        // Validate the Country in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertCountryUpdatableFieldsEquals(partialUpdatedCountry, getPersistedCountry(partialUpdatedCountry));
    }

    @Test
    @Transactional
    void patchNonExistingCountry() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        country.setId(longCount.incrementAndGet());

        // Create the Country
        CountryDTO countryDTO = countryMapper.toDto(country);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restCountryMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, countryDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(countryDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Country in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchCountry() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        country.setId(longCount.incrementAndGet());

        // Create the Country
        CountryDTO countryDTO = countryMapper.toDto(country);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCountryMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(countryDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Country in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamCountry() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        country.setId(longCount.incrementAndGet());

        // Create the Country
        CountryDTO countryDTO = countryMapper.toDto(country);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCountryMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(om.writeValueAsBytes(countryDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Country in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteCountry() throws Exception {
        // Initialize the database
        insertedCountry = countryRepository.saveAndFlush(country);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the country
        restCountryMockMvc
            .perform(delete(ENTITY_API_URL_ID, country.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return countryRepository.count();
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

    protected Country getPersistedCountry(Country country) {
        return countryRepository.findById(country.getId()).orElseThrow();
    }

    protected void assertPersistedCountryToMatchAllProperties(Country expectedCountry) {
        assertCountryAllPropertiesEquals(expectedCountry, getPersistedCountry(expectedCountry));
    }

    protected void assertPersistedCountryToMatchUpdatableProperties(Country expectedCountry) {
        assertCountryAllUpdatablePropertiesEquals(expectedCountry, getPersistedCountry(expectedCountry));
    }
}
