package com.crm.service.impl;

import com.crm.domain.Complaint;
import com.crm.repository.ComplaintRepository;
import com.crm.service.ComplaintService;
import com.crm.service.dto.ComplaintDTO;
import com.crm.service.mapper.ComplaintMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link com.crm.domain.Complaint}.
 */
@Service
@Transactional
public class ComplaintServiceImpl implements ComplaintService {

    private final Logger log = LoggerFactory.getLogger(ComplaintServiceImpl.class);

    private final ComplaintRepository complaintRepository;

    private final ComplaintMapper complaintMapper;

    public ComplaintServiceImpl(ComplaintRepository complaintRepository, ComplaintMapper complaintMapper) {
        this.complaintRepository = complaintRepository;
        this.complaintMapper = complaintMapper;
    }

    @Override
    public ComplaintDTO save(ComplaintDTO complaintDTO) {
        log.debug("Request to save Complaint : {}", complaintDTO);
        Complaint complaint = complaintMapper.toEntity(complaintDTO);
        complaint = complaintRepository.save(complaint);
        return complaintMapper.toDto(complaint);
    }

    @Override
    public ComplaintDTO update(ComplaintDTO complaintDTO) {
        log.debug("Request to update Complaint : {}", complaintDTO);
        Complaint complaint = complaintMapper.toEntity(complaintDTO);
        complaint = complaintRepository.save(complaint);
        return complaintMapper.toDto(complaint);
    }

    @Override
    public Optional<ComplaintDTO> partialUpdate(ComplaintDTO complaintDTO) {
        log.debug("Request to partially update Complaint : {}", complaintDTO);

        return complaintRepository
            .findById(complaintDTO.getId())
            .map(existingComplaint -> {
                complaintMapper.partialUpdate(existingComplaint, complaintDTO);

                return existingComplaint;
            })
            .map(complaintRepository::save)
            .map(complaintMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<ComplaintDTO> findAll(Pageable pageable) {
        log.debug("Request to get all Complaints");
        return complaintRepository.findAll(pageable).map(complaintMapper::toDto);
    }

    public Page<ComplaintDTO> findAllWithEagerRelationships(Pageable pageable) {
        return complaintRepository.findAllWithEagerRelationships(pageable).map(complaintMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<ComplaintDTO> findOne(Long id) {
        log.debug("Request to get Complaint : {}", id);
        return complaintRepository.findOneWithEagerRelationships(id).map(complaintMapper::toDto);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete Complaint : {}", id);
        complaintRepository.deleteById(id);
    }
}
