package com.mgs.service;

import com.mgs.domain.SystemUser;
import com.mgs.repository.SystemUserRepository;
import com.mgs.service.dto.SystemUserDTO;
import com.mgs.service.mapper.SystemUserMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link com.mgs.domain.SystemUser}.
 */
@Service
@Transactional
public class SystemUserService {

    private static final Logger LOG = LoggerFactory.getLogger(SystemUserService.class);

    private final SystemUserRepository systemUserRepository;

    private final SystemUserMapper systemUserMapper;

    public SystemUserService(SystemUserRepository systemUserRepository, SystemUserMapper systemUserMapper) {
        this.systemUserRepository = systemUserRepository;
        this.systemUserMapper = systemUserMapper;
    }

    /**
     * Save a systemUser.
     *
     * @param systemUserDTO the entity to save.
     * @return the persisted entity.
     */
    public SystemUserDTO save(SystemUserDTO systemUserDTO) {
        LOG.debug("Request to save SystemUser : {}", systemUserDTO);
        SystemUser systemUser = systemUserMapper.toEntity(systemUserDTO);
        systemUser = systemUserRepository.save(systemUser);
        return systemUserMapper.toDto(systemUser);
    }

    /**
     * Update a systemUser.
     *
     * @param systemUserDTO the entity to save.
     * @return the persisted entity.
     */
    public SystemUserDTO update(SystemUserDTO systemUserDTO) {
        LOG.debug("Request to update SystemUser : {}", systemUserDTO);
        SystemUser systemUser = systemUserMapper.toEntity(systemUserDTO);
        systemUser = systemUserRepository.save(systemUser);
        return systemUserMapper.toDto(systemUser);
    }

    /**
     * Partially update a systemUser.
     *
     * @param systemUserDTO the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<SystemUserDTO> partialUpdate(SystemUserDTO systemUserDTO) {
        LOG.debug("Request to partially update SystemUser : {}", systemUserDTO);

        return systemUserRepository
            .findById(systemUserDTO.getId())
            .map(existingSystemUser -> {
                systemUserMapper.partialUpdate(existingSystemUser, systemUserDTO);

                return existingSystemUser;
            })
            .map(systemUserRepository::save)
            .map(systemUserMapper::toDto);
    }

    /**
     * Get one systemUser by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<SystemUserDTO> findOne(Long id) {
        LOG.debug("Request to get SystemUser : {}", id);
        return systemUserRepository.findById(id).map(systemUserMapper::toDto);
    }

    /**
     * Delete the systemUser by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        LOG.debug("Request to delete SystemUser : {}", id);
        systemUserRepository.deleteById(id);
    }
}
