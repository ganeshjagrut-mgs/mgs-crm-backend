package com.crm.service.impl;

import com.crm.domain.EntityType;
import com.crm.repository.EntityTypeRepository;
import com.crm.service.EntityTypeService;
import com.crm.service.dto.EntityTypeDTO;
import com.crm.service.mapper.EntityTypeMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link com.crm.domain.EntityType}.
 */
@Service
@Transactional
public class EntityTypeServiceImpl implements EntityTypeService {

    private final Logger log = LoggerFactory.getLogger(EntityTypeServiceImpl.class);

    private final EntityTypeRepository entityTypeRepository;

    private final EntityTypeMapper entityTypeMapper;

    public EntityTypeServiceImpl(EntityTypeRepository entityTypeRepository, EntityTypeMapper entityTypeMapper) {
        this.entityTypeRepository = entityTypeRepository;
        this.entityTypeMapper = entityTypeMapper;
    }

    @Override
    public EntityTypeDTO save(EntityTypeDTO entityTypeDTO) {
        log.debug("Request to save EntityType : {}", entityTypeDTO);
        EntityType entityType = entityTypeMapper.toEntity(entityTypeDTO);
        entityType = entityTypeRepository.save(entityType);
        return entityTypeMapper.toDto(entityType);
    }

    @Override
    public EntityTypeDTO update(EntityTypeDTO entityTypeDTO) {
        log.debug("Request to update EntityType : {}", entityTypeDTO);
        EntityType entityType = entityTypeMapper.toEntity(entityTypeDTO);
        entityType = entityTypeRepository.save(entityType);
        return entityTypeMapper.toDto(entityType);
    }

    @Override
    public Optional<EntityTypeDTO> partialUpdate(EntityTypeDTO entityTypeDTO) {
        log.debug("Request to partially update EntityType : {}", entityTypeDTO);

        return entityTypeRepository
            .findById(entityTypeDTO.getId())
            .map(existingEntityType -> {
                entityTypeMapper.partialUpdate(existingEntityType, entityTypeDTO);

                return existingEntityType;
            })
            .map(entityTypeRepository::save)
            .map(entityTypeMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<EntityTypeDTO> findAll(Pageable pageable) {
        log.debug("Request to get all EntityTypes");
        return entityTypeRepository.findAll(pageable).map(entityTypeMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<EntityTypeDTO> findOne(Long id) {
        log.debug("Request to get EntityType : {}", id);
        return entityTypeRepository.findById(id).map(entityTypeMapper::toDto);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete EntityType : {}", id);
        entityTypeRepository.deleteById(id);
    }
}
