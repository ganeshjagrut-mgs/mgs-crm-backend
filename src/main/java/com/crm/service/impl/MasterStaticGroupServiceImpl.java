package com.crm.service.impl;

import com.crm.domain.MasterStaticGroup;
import com.crm.repository.MasterStaticGroupRepository;
import com.crm.service.MasterStaticGroupService;
import com.crm.service.dto.MasterStaticGroupDTO;
import com.crm.service.mapper.MasterStaticGroupMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link com.crm.domain.MasterStaticGroup}.
 */
@Service
@Transactional
public class MasterStaticGroupServiceImpl implements MasterStaticGroupService {

    private final Logger log = LoggerFactory.getLogger(MasterStaticGroupServiceImpl.class);

    private final MasterStaticGroupRepository masterStaticGroupRepository;

    private final MasterStaticGroupMapper masterStaticGroupMapper;

    public MasterStaticGroupServiceImpl(
        MasterStaticGroupRepository masterStaticGroupRepository,
        MasterStaticGroupMapper masterStaticGroupMapper
    ) {
        this.masterStaticGroupRepository = masterStaticGroupRepository;
        this.masterStaticGroupMapper = masterStaticGroupMapper;
    }

    @Override
    public MasterStaticGroupDTO save(MasterStaticGroupDTO masterStaticGroupDTO) {
        log.debug("Request to save MasterStaticGroup : {}", masterStaticGroupDTO);
        MasterStaticGroup masterStaticGroup = masterStaticGroupMapper.toEntity(masterStaticGroupDTO);
        masterStaticGroup = masterStaticGroupRepository.save(masterStaticGroup);
        return masterStaticGroupMapper.toDto(masterStaticGroup);
    }

    @Override
    public MasterStaticGroupDTO update(MasterStaticGroupDTO masterStaticGroupDTO) {
        log.debug("Request to update MasterStaticGroup : {}", masterStaticGroupDTO);
        MasterStaticGroup masterStaticGroup = masterStaticGroupMapper.toEntity(masterStaticGroupDTO);
        masterStaticGroup = masterStaticGroupRepository.save(masterStaticGroup);
        return masterStaticGroupMapper.toDto(masterStaticGroup);
    }

    @Override
    public Optional<MasterStaticGroupDTO> partialUpdate(MasterStaticGroupDTO masterStaticGroupDTO) {
        log.debug("Request to partially update MasterStaticGroup : {}", masterStaticGroupDTO);

        return masterStaticGroupRepository
            .findById(masterStaticGroupDTO.getId())
            .map(existingMasterStaticGroup -> {
                masterStaticGroupMapper.partialUpdate(existingMasterStaticGroup, masterStaticGroupDTO);

                return existingMasterStaticGroup;
            })
            .map(masterStaticGroupRepository::save)
            .map(masterStaticGroupMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<MasterStaticGroupDTO> findAll(Pageable pageable) {
        log.debug("Request to get all MasterStaticGroups");
        return masterStaticGroupRepository.findAll(pageable).map(masterStaticGroupMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<MasterStaticGroupDTO> findOne(Long id) {
        log.debug("Request to get MasterStaticGroup : {}", id);
        return masterStaticGroupRepository.findById(id).map(masterStaticGroupMapper::toDto);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete MasterStaticGroup : {}", id);
        masterStaticGroupRepository.deleteById(id);
    }
}
