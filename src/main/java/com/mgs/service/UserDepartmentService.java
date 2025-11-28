package com.mgs.service;

import com.mgs.domain.UserDepartment;
import com.mgs.repository.UserDepartmentRepository;
import com.mgs.service.dto.UserDepartmentDTO;
import com.mgs.service.mapper.UserDepartmentMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link com.mgs.domain.UserDepartment}.
 */
@Service
@Transactional
public class UserDepartmentService {

    private static final Logger LOG = LoggerFactory.getLogger(UserDepartmentService.class);

    private final UserDepartmentRepository userDepartmentRepository;

    private final UserDepartmentMapper userDepartmentMapper;

    public UserDepartmentService(UserDepartmentRepository userDepartmentRepository, UserDepartmentMapper userDepartmentMapper) {
        this.userDepartmentRepository = userDepartmentRepository;
        this.userDepartmentMapper = userDepartmentMapper;
    }

    /**
     * Save a userDepartment.
     *
     * @param userDepartmentDTO the entity to save.
     * @return the persisted entity.
     */
    public UserDepartmentDTO save(UserDepartmentDTO userDepartmentDTO) {
        LOG.debug("Request to save UserDepartment : {}", userDepartmentDTO);
        UserDepartment userDepartment = userDepartmentMapper.toEntity(userDepartmentDTO);
        userDepartment = userDepartmentRepository.save(userDepartment);
        return userDepartmentMapper.toDto(userDepartment);
    }

    /**
     * Update a userDepartment.
     *
     * @param userDepartmentDTO the entity to save.
     * @return the persisted entity.
     */
    public UserDepartmentDTO update(UserDepartmentDTO userDepartmentDTO) {
        LOG.debug("Request to update UserDepartment : {}", userDepartmentDTO);
        UserDepartment userDepartment = userDepartmentMapper.toEntity(userDepartmentDTO);
        userDepartment = userDepartmentRepository.save(userDepartment);
        return userDepartmentMapper.toDto(userDepartment);
    }

    /**
     * Partially update a userDepartment.
     *
     * @param userDepartmentDTO the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<UserDepartmentDTO> partialUpdate(UserDepartmentDTO userDepartmentDTO) {
        LOG.debug("Request to partially update UserDepartment : {}", userDepartmentDTO);

        return userDepartmentRepository
            .findById(userDepartmentDTO.getId())
            .map(existingUserDepartment -> {
                userDepartmentMapper.partialUpdate(existingUserDepartment, userDepartmentDTO);

                return existingUserDepartment;
            })
            .map(userDepartmentRepository::save)
            .map(userDepartmentMapper::toDto);
    }

    /**
     * Get one userDepartment by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<UserDepartmentDTO> findOne(Long id) {
        LOG.debug("Request to get UserDepartment : {}", id);
        return userDepartmentRepository.findById(id).map(userDepartmentMapper::toDto);
    }

    /**
     * Delete the userDepartment by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        LOG.debug("Request to delete UserDepartment : {}", id);
        userDepartmentRepository.deleteById(id);
    }
}
