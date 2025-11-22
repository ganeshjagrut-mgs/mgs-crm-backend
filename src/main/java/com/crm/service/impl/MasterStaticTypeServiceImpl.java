package com.crm.service.impl;

import com.crm.domain.MasterStaticType;
import com.crm.repository.MasterStaticTypeRepository;
import com.crm.service.MasterStaticTypeService;
import com.crm.service.dto.MasterStaticTypeDTO;
import com.crm.service.mapper.MasterStaticTypeMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link com.crm.domain.MasterStaticType}.
 */
@Service
@Transactional
public class MasterStaticTypeServiceImpl implements MasterStaticTypeService {

    private final Logger log = LoggerFactory.getLogger(MasterStaticTypeServiceImpl.class);

    private final MasterStaticTypeRepository masterStaticTypeRepository;

    private final MasterStaticTypeMapper masterStaticTypeMapper;

    public MasterStaticTypeServiceImpl(
        MasterStaticTypeRepository masterStaticTypeRepository,
        MasterStaticTypeMapper masterStaticTypeMapper
    ) {
        this.masterStaticTypeRepository = masterStaticTypeRepository;
        this.masterStaticTypeMapper = masterStaticTypeMapper;
    }

    @Override
    public MasterStaticTypeDTO save(MasterStaticTypeDTO masterStaticTypeDTO) {
        log.debug("Request to save MasterStaticType : {}", masterStaticTypeDTO);
        MasterStaticType masterStaticType = masterStaticTypeMapper.toEntity(masterStaticTypeDTO);
        masterStaticType = masterStaticTypeRepository.save(masterStaticType);
        return masterStaticTypeMapper.toDto(masterStaticType);
    }

    @Override
    public MasterStaticTypeDTO update(MasterStaticTypeDTO masterStaticTypeDTO) {
        log.debug("Request to update MasterStaticType : {}", masterStaticTypeDTO);
        MasterStaticType masterStaticType = masterStaticTypeMapper.toEntity(masterStaticTypeDTO);
        masterStaticType = masterStaticTypeRepository.save(masterStaticType);
        return masterStaticTypeMapper.toDto(masterStaticType);
    }

    @Override
    public Optional<MasterStaticTypeDTO> partialUpdate(MasterStaticTypeDTO masterStaticTypeDTO) {
        log.debug("Request to partially update MasterStaticType : {}", masterStaticTypeDTO);

        return masterStaticTypeRepository
            .findById(masterStaticTypeDTO.getId())
            .map(existingMasterStaticType -> {
                masterStaticTypeMapper.partialUpdate(existingMasterStaticType, masterStaticTypeDTO);

                return existingMasterStaticType;
            })
            .map(masterStaticTypeRepository::save)
            .map(masterStaticTypeMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<MasterStaticTypeDTO> findAll(Pageable pageable) {
        log.debug("Request to get all MasterStaticTypes");
        return masterStaticTypeRepository.findAll(pageable).map(masterStaticTypeMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<MasterStaticTypeDTO> findOne(Long id) {
        log.debug("Request to get MasterStaticType : {}", id);
        return masterStaticTypeRepository.findById(id).map(masterStaticTypeMapper::toDto);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete MasterStaticType : {}", id);
        masterStaticTypeRepository.deleteById(id);
    }
}
