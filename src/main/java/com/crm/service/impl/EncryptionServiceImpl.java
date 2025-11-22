package com.crm.service.impl;

import com.crm.domain.Encryption;
import com.crm.repository.EncryptionRepository;
import com.crm.service.EncryptionService;
import com.crm.service.dto.EncryptionDTO;
import com.crm.service.mapper.EncryptionMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link com.crm.domain.Encryption}.
 */
@Service
@Transactional
public class EncryptionServiceImpl implements EncryptionService {

    private final Logger log = LoggerFactory.getLogger(EncryptionServiceImpl.class);

    private final EncryptionRepository encryptionRepository;

    private final EncryptionMapper encryptionMapper;

    public EncryptionServiceImpl(EncryptionRepository encryptionRepository, EncryptionMapper encryptionMapper) {
        this.encryptionRepository = encryptionRepository;
        this.encryptionMapper = encryptionMapper;
    }

    @Override
    public EncryptionDTO save(EncryptionDTO encryptionDTO) {
        log.debug("Request to save Encryption : {}", encryptionDTO);
        Encryption encryption = encryptionMapper.toEntity(encryptionDTO);
        encryption = encryptionRepository.save(encryption);
        return encryptionMapper.toDto(encryption);
    }

    @Override
    public EncryptionDTO update(EncryptionDTO encryptionDTO) {
        log.debug("Request to update Encryption : {}", encryptionDTO);
        Encryption encryption = encryptionMapper.toEntity(encryptionDTO);
        encryption = encryptionRepository.save(encryption);
        return encryptionMapper.toDto(encryption);
    }

    @Override
    public Optional<EncryptionDTO> partialUpdate(EncryptionDTO encryptionDTO) {
        log.debug("Request to partially update Encryption : {}", encryptionDTO);

        return encryptionRepository
                .findById(encryptionDTO.getId())
                .map(existingEncryption -> {
                    encryptionMapper.partialUpdate(existingEncryption, encryptionDTO);

                    return existingEncryption;
                })
                .map(encryptionRepository::save)
                .map(encryptionMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<EncryptionDTO> findAll(Pageable pageable) {
        log.debug("Request to get all Encryptions");
        return encryptionRepository.findAll(pageable).map(encryptionMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<EncryptionDTO> findOne(Long id) {
        log.debug("Request to get Encryption : {}", id);
        return encryptionRepository.findById(id).map(encryptionMapper::toDto);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete Encryption : {}", id);
        encryptionRepository.deleteById(id);
    }
}
