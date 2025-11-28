package com.mgs.service;

import com.mgs.domain.Complaint;
import com.mgs.repository.ComplaintRepository;
import com.mgs.service.dto.ComplaintDTO;
import com.mgs.service.mapper.ComplaintMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link com.mgs.domain.Complaint}.
 */
@Service
@Transactional
public class ComplaintService {

    private static final Logger LOG = LoggerFactory.getLogger(ComplaintService.class);

    private final ComplaintRepository complaintRepository;

    private final ComplaintMapper complaintMapper;

    public ComplaintService(ComplaintRepository complaintRepository, ComplaintMapper complaintMapper) {
        this.complaintRepository = complaintRepository;
        this.complaintMapper = complaintMapper;
    }

    /**
     * Save a complaint.
     *
     * @param complaintDTO the entity to save.
     * @return the persisted entity.
     */
    public ComplaintDTO save(ComplaintDTO complaintDTO) {
        LOG.debug("Request to save Complaint : {}", complaintDTO);
        Complaint complaint = complaintMapper.toEntity(complaintDTO);
        complaint = complaintRepository.save(complaint);
        return complaintMapper.toDto(complaint);
    }

    /**
     * Update a complaint.
     *
     * @param complaintDTO the entity to save.
     * @return the persisted entity.
     */
    public ComplaintDTO update(ComplaintDTO complaintDTO) {
        LOG.debug("Request to update Complaint : {}", complaintDTO);
        Complaint complaint = complaintMapper.toEntity(complaintDTO);
        complaint = complaintRepository.save(complaint);
        return complaintMapper.toDto(complaint);
    }

    /**
     * Partially update a complaint.
     *
     * @param complaintDTO the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<ComplaintDTO> partialUpdate(ComplaintDTO complaintDTO) {
        LOG.debug("Request to partially update Complaint : {}", complaintDTO);

        return complaintRepository
            .findById(complaintDTO.getId())
            .map(existingComplaint -> {
                complaintMapper.partialUpdate(existingComplaint, complaintDTO);

                return existingComplaint;
            })
            .map(complaintRepository::save)
            .map(complaintMapper::toDto);
    }

    /**
     * Get one complaint by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<ComplaintDTO> findOne(Long id) {
        LOG.debug("Request to get Complaint : {}", id);
        return complaintRepository.findById(id).map(complaintMapper::toDto);
    }

    /**
     * Delete the complaint by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        LOG.debug("Request to delete Complaint : {}", id);
        complaintRepository.deleteById(id);
    }
}
