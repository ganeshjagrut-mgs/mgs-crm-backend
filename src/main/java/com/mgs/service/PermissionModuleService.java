package com.mgs.service;

import com.mgs.domain.PermissionModule;
import com.mgs.repository.PermissionModuleRepository;
import com.mgs.service.dto.PermissionModuleDTO;
import com.mgs.service.mapper.PermissionModuleMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link com.mgs.domain.PermissionModule}.
 */
@Service
@Transactional
public class PermissionModuleService {

    private static final Logger LOG = LoggerFactory.getLogger(PermissionModuleService.class);

    private final PermissionModuleRepository permissionModuleRepository;

    private final PermissionModuleMapper permissionModuleMapper;

    public PermissionModuleService(PermissionModuleRepository permissionModuleRepository, PermissionModuleMapper permissionModuleMapper) {
        this.permissionModuleRepository = permissionModuleRepository;
        this.permissionModuleMapper = permissionModuleMapper;
    }

    /**
     * Save a permissionModule.
     *
     * @param permissionModuleDTO the entity to save.
     * @return the persisted entity.
     */
    public PermissionModuleDTO save(PermissionModuleDTO permissionModuleDTO) {
        LOG.debug("Request to save PermissionModule : {}", permissionModuleDTO);
        PermissionModule permissionModule = permissionModuleMapper.toEntity(permissionModuleDTO);
        permissionModule = permissionModuleRepository.save(permissionModule);
        return permissionModuleMapper.toDto(permissionModule);
    }

    /**
     * Update a permissionModule.
     *
     * @param permissionModuleDTO the entity to save.
     * @return the persisted entity.
     */
    public PermissionModuleDTO update(PermissionModuleDTO permissionModuleDTO) {
        LOG.debug("Request to update PermissionModule : {}", permissionModuleDTO);
        PermissionModule permissionModule = permissionModuleMapper.toEntity(permissionModuleDTO);
        permissionModule = permissionModuleRepository.save(permissionModule);
        return permissionModuleMapper.toDto(permissionModule);
    }

    /**
     * Partially update a permissionModule.
     *
     * @param permissionModuleDTO the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<PermissionModuleDTO> partialUpdate(PermissionModuleDTO permissionModuleDTO) {
        LOG.debug("Request to partially update PermissionModule : {}", permissionModuleDTO);

        return permissionModuleRepository
            .findById(permissionModuleDTO.getId())
            .map(existingPermissionModule -> {
                permissionModuleMapper.partialUpdate(existingPermissionModule, permissionModuleDTO);

                return existingPermissionModule;
            })
            .map(permissionModuleRepository::save)
            .map(permissionModuleMapper::toDto);
    }

    /**
     * Get one permissionModule by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<PermissionModuleDTO> findOne(Long id) {
        LOG.debug("Request to get PermissionModule : {}", id);
        return permissionModuleRepository.findById(id).map(permissionModuleMapper::toDto);
    }

    /**
     * Delete the permissionModule by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        LOG.debug("Request to delete PermissionModule : {}", id);
        permissionModuleRepository.deleteById(id);
    }
}
