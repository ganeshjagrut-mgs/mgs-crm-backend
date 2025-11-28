package com.mgs.service;

import com.mgs.domain.Country;
import com.mgs.repository.CountryRepository;
import com.mgs.service.dto.CountryDTO;
import com.mgs.service.mapper.CountryMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link com.mgs.domain.Country}.
 */
@Service
@Transactional
public class CountryService {

    private static final Logger LOG = LoggerFactory.getLogger(CountryService.class);

    private final CountryRepository countryRepository;

    private final CountryMapper countryMapper;

    public CountryService(CountryRepository countryRepository, CountryMapper countryMapper) {
        this.countryRepository = countryRepository;
        this.countryMapper = countryMapper;
    }

    /**
     * Save a country.
     *
     * @param countryDTO the entity to save.
     * @return the persisted entity.
     */
    public CountryDTO save(CountryDTO countryDTO) {
        LOG.debug("Request to save Country : {}", countryDTO);
        Country country = countryMapper.toEntity(countryDTO);
        country = countryRepository.save(country);
        return countryMapper.toDto(country);
    }

    /**
     * Update a country.
     *
     * @param countryDTO the entity to save.
     * @return the persisted entity.
     */
    public CountryDTO update(CountryDTO countryDTO) {
        LOG.debug("Request to update Country : {}", countryDTO);
        Country country = countryMapper.toEntity(countryDTO);
        country = countryRepository.save(country);
        return countryMapper.toDto(country);
    }

    /**
     * Partially update a country.
     *
     * @param countryDTO the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<CountryDTO> partialUpdate(CountryDTO countryDTO) {
        LOG.debug("Request to partially update Country : {}", countryDTO);

        return countryRepository
            .findById(countryDTO.getId())
            .map(existingCountry -> {
                countryMapper.partialUpdate(existingCountry, countryDTO);

                return existingCountry;
            })
            .map(countryRepository::save)
            .map(countryMapper::toDto);
    }

    /**
     * Get one country by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<CountryDTO> findOne(Long id) {
        LOG.debug("Request to get Country : {}", id);
        return countryRepository.findById(id).map(countryMapper::toDto);
    }

    /**
     * Delete the country by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        LOG.debug("Request to delete Country : {}", id);
        countryRepository.deleteById(id);
    }
}
