package com.mgs.service;

import com.mgs.domain.UserHierarchy;
import com.mgs.repository.UserHierarchyRepository;
import com.mgs.service.dto.UserHierarchyDTO;
import com.mgs.service.mapper.UserHierarchyMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link com.mgs.domain.UserHierarchy}.
 */
@Service
@Transactional
public class UserHierarchyService {

    private static final Logger LOG = LoggerFactory.getLogger(UserHierarchyService.class);

    private final UserHierarchyRepository userHierarchyRepository;

    private final UserHierarchyMapper userHierarchyMapper;

    public UserHierarchyService(UserHierarchyRepository userHierarchyRepository, UserHierarchyMapper userHierarchyMapper) {
        this.userHierarchyRepository = userHierarchyRepository;
        this.userHierarchyMapper = userHierarchyMapper;
    }

    /**
     * Save a userHierarchy.
     *
     * @param userHierarchyDTO the entity to save.
     * @return the persisted entity.
     */
    public UserHierarchyDTO save(UserHierarchyDTO userHierarchyDTO) {
        LOG.debug("Request to save UserHierarchy : {}", userHierarchyDTO);
        UserHierarchy userHierarchy = userHierarchyMapper.toEntity(userHierarchyDTO);
        userHierarchy = userHierarchyRepository.save(userHierarchy);
        return userHierarchyMapper.toDto(userHierarchy);
    }

    /**
     * Update a userHierarchy.
     *
     * @param userHierarchyDTO the entity to save.
     * @return the persisted entity.
     */
    public UserHierarchyDTO update(UserHierarchyDTO userHierarchyDTO) {
        LOG.debug("Request to update UserHierarchy : {}", userHierarchyDTO);
        UserHierarchy userHierarchy = userHierarchyMapper.toEntity(userHierarchyDTO);
        userHierarchy = userHierarchyRepository.save(userHierarchy);
        return userHierarchyMapper.toDto(userHierarchy);
    }

    /**
     * Partially update a userHierarchy.
     *
     * @param userHierarchyDTO the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<UserHierarchyDTO> partialUpdate(UserHierarchyDTO userHierarchyDTO) {
        LOG.debug("Request to partially update UserHierarchy : {}", userHierarchyDTO);

        return userHierarchyRepository
            .findById(userHierarchyDTO.getId())
            .map(existingUserHierarchy -> {
                userHierarchyMapper.partialUpdate(existingUserHierarchy, userHierarchyDTO);

                return existingUserHierarchy;
            })
            .map(userHierarchyRepository::save)
            .map(userHierarchyMapper::toDto);
    }

    /**
     * Get one userHierarchy by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<UserHierarchyDTO> findOne(Long id) {
        LOG.debug("Request to get UserHierarchy : {}", id);
        return userHierarchyRepository.findById(id).map(userHierarchyMapper::toDto);
    }

    /**
     * Delete the userHierarchy by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        LOG.debug("Request to delete UserHierarchy : {}", id);
        userHierarchyRepository.deleteById(id);
    }
}
