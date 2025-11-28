package com.mgs.service;

import com.mgs.domain.RolePermission;
import com.mgs.repository.RolePermissionRepository;
import com.mgs.service.dto.RolePermissionDTO;
import com.mgs.service.mapper.RolePermissionMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link com.mgs.domain.RolePermission}.
 */
@Service
@Transactional
public class RolePermissionService {

    private static final Logger LOG = LoggerFactory.getLogger(RolePermissionService.class);

    private final RolePermissionRepository rolePermissionRepository;

    private final RolePermissionMapper rolePermissionMapper;

    public RolePermissionService(RolePermissionRepository rolePermissionRepository, RolePermissionMapper rolePermissionMapper) {
        this.rolePermissionRepository = rolePermissionRepository;
        this.rolePermissionMapper = rolePermissionMapper;
    }

    /**
     * Save a rolePermission.
     *
     * @param rolePermissionDTO the entity to save.
     * @return the persisted entity.
     */
    public RolePermissionDTO save(RolePermissionDTO rolePermissionDTO) {
        LOG.debug("Request to save RolePermission : {}", rolePermissionDTO);
        RolePermission rolePermission = rolePermissionMapper.toEntity(rolePermissionDTO);
        rolePermission = rolePermissionRepository.save(rolePermission);
        return rolePermissionMapper.toDto(rolePermission);
    }

    /**
     * Update a rolePermission.
     *
     * @param rolePermissionDTO the entity to save.
     * @return the persisted entity.
     */
    public RolePermissionDTO update(RolePermissionDTO rolePermissionDTO) {
        LOG.debug("Request to update RolePermission : {}", rolePermissionDTO);
        RolePermission rolePermission = rolePermissionMapper.toEntity(rolePermissionDTO);
        rolePermission = rolePermissionRepository.save(rolePermission);
        return rolePermissionMapper.toDto(rolePermission);
    }

    /**
     * Partially update a rolePermission.
     *
     * @param rolePermissionDTO the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<RolePermissionDTO> partialUpdate(RolePermissionDTO rolePermissionDTO) {
        LOG.debug("Request to partially update RolePermission : {}", rolePermissionDTO);

        return rolePermissionRepository
            .findById(rolePermissionDTO.getId())
            .map(existingRolePermission -> {
                rolePermissionMapper.partialUpdate(existingRolePermission, rolePermissionDTO);

                return existingRolePermission;
            })
            .map(rolePermissionRepository::save)
            .map(rolePermissionMapper::toDto);
    }

    /**
     * Get one rolePermission by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<RolePermissionDTO> findOne(Long id) {
        LOG.debug("Request to get RolePermission : {}", id);
        return rolePermissionRepository.findById(id).map(rolePermissionMapper::toDto);
    }

    /**
     * Delete the rolePermission by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        LOG.debug("Request to delete RolePermission : {}", id);
        rolePermissionRepository.deleteById(id);
    }
}
