package com.crm.service.impl;

import com.crm.domain.MasterCategory;
import com.crm.repository.MasterCategoryRepository;
import com.crm.service.MasterCategoryService;
import com.crm.service.dto.MasterCategoryDTO;
import com.crm.service.mapper.MasterCategoryMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link com.crm.domain.MasterCategory}.
 */
@Service
@Transactional
public class MasterCategoryServiceImpl implements MasterCategoryService {

    private final Logger log = LoggerFactory.getLogger(MasterCategoryServiceImpl.class);

    private final MasterCategoryRepository masterCategoryRepository;

    private final MasterCategoryMapper masterCategoryMapper;

    public MasterCategoryServiceImpl(MasterCategoryRepository masterCategoryRepository, MasterCategoryMapper masterCategoryMapper) {
        this.masterCategoryRepository = masterCategoryRepository;
        this.masterCategoryMapper = masterCategoryMapper;
    }

    @Override
    public MasterCategoryDTO save(MasterCategoryDTO masterCategoryDTO) {
        log.debug("Request to save MasterCategory : {}", masterCategoryDTO);
        MasterCategory masterCategory = masterCategoryMapper.toEntity(masterCategoryDTO);
        masterCategory = masterCategoryRepository.save(masterCategory);
        return masterCategoryMapper.toDto(masterCategory);
    }

    @Override
    public MasterCategoryDTO update(MasterCategoryDTO masterCategoryDTO) {
        log.debug("Request to update MasterCategory : {}", masterCategoryDTO);
        MasterCategory masterCategory = masterCategoryMapper.toEntity(masterCategoryDTO);
        masterCategory = masterCategoryRepository.save(masterCategory);
        return masterCategoryMapper.toDto(masterCategory);
    }

    @Override
    public Optional<MasterCategoryDTO> partialUpdate(MasterCategoryDTO masterCategoryDTO) {
        log.debug("Request to partially update MasterCategory : {}", masterCategoryDTO);

        return masterCategoryRepository
            .findById(masterCategoryDTO.getId())
            .map(existingMasterCategory -> {
                masterCategoryMapper.partialUpdate(existingMasterCategory, masterCategoryDTO);

                return existingMasterCategory;
            })
            .map(masterCategoryRepository::save)
            .map(masterCategoryMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<MasterCategoryDTO> findAll(Pageable pageable) {
        log.debug("Request to get all MasterCategories");
        return masterCategoryRepository.findAll(pageable).map(masterCategoryMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<MasterCategoryDTO> findOne(Long id) {
        log.debug("Request to get MasterCategory : {}", id);
        return masterCategoryRepository.findById(id).map(masterCategoryMapper::toDto);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete MasterCategory : {}", id);
        masterCategoryRepository.deleteById(id);
    }
}
