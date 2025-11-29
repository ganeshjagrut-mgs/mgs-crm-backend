package com.mgs.service;

import com.mgs.domain.User;
import com.mgs.repository.UserRepository;
import com.mgs.service.dto.UserDTO;
import com.mgs.service.mapper.UserMapper;
import java.util.Optional;

import com.mgs.util.EncryptionUtil;
import com.mgs.util.JwtUtil;
import com.mgs.web.rest.errors.EncryptionKeyNotFoundException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link com.mgs.domain.User}.
 */
@Service
@Transactional
public class UserService {

    private static final Logger LOG = LoggerFactory.getLogger(UserService.class);

    private final UserRepository userRepository;

    private final UserMapper userMapper;

    private final EncryptionUtil encryptionUtil;

    public UserService(UserRepository userRepository, UserMapper userMapper, EncryptionUtil encryptionUtil) {
        this.userRepository = userRepository;
        this.userMapper = userMapper;
        this.encryptionUtil = encryptionUtil;
    }

    /**
     * Save a user.
     *
     * @param userDTO the entity to save.
     * @return the persisted entity.
     */
    public UserDTO save(UserDTO userDTO) {
        LOG.debug("Request to save User : {}", userDTO);
        User user = userMapper.toEntity(userDTO);

        Long tenantId = JwtUtil.getTenantIdFromToken();
        String encryptionKey = encryptionUtil
            .getEncryptionKey(tenantId)
            .orElseThrow(() -> new EncryptionKeyNotFoundException(tenantId));
        encryptionUtil.encryptObject(user, encryptionKey);

        user = userRepository.save(user);
        return userMapper.toDto(user);
    }

    /**
     * Update a user.
     *
     * @param userDTO the entity to save.
     * @return the persisted entity.
     */
    public UserDTO update(UserDTO userDTO) {
        LOG.debug("Request to update User : {}", userDTO);
        User user = userMapper.toEntity(userDTO);

        Long tenantId = JwtUtil.getTenantIdFromToken();
        String encryptionKey = encryptionUtil
            .getEncryptionKey(tenantId)
            .orElseThrow(() -> new EncryptionKeyNotFoundException(tenantId));
        encryptionUtil.encryptObject(user, encryptionKey);

        user = userRepository.save(user);
        return userMapper.toDto(user);
    }

    /**
     * Partially update a user.
     *
     * @param userDTO the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<UserDTO> partialUpdate(UserDTO userDTO) {
        LOG.debug("Request to partially update User : {}", userDTO);

        Long tenantId = JwtUtil.getTenantIdFromToken();
        String encryptionKey = encryptionUtil
            .getEncryptionKey(tenantId)
            .orElseThrow(() -> new EncryptionKeyNotFoundException(tenantId));

        return userRepository
            .findById(userDTO.getId())
            .map(existingUser -> {
                encryptionUtil.decryptObject(existingUser, encryptionKey);
                userMapper.partialUpdate(existingUser, userDTO);

                encryptionUtil.encryptObject(existingUser, encryptionKey);
                return existingUser;
            })
            .map(userRepository::save)
            .map(savedUser -> {
                encryptionUtil.decryptObject(savedUser, encryptionKey);
                return userMapper.toDto(savedUser);
            });
    }

    /**
     * Get one user by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<UserDTO> findOne(Long id) {
        LOG.debug("Request to get User : {}", id);

        Long tenantId = JwtUtil.getTenantIdFromToken();
        String encryptionKey = encryptionUtil
            .getEncryptionKey(tenantId)
            .orElseThrow(() -> new EncryptionKeyNotFoundException(tenantId));

        return userRepository.findById(id).map(user -> {
            encryptionUtil.decryptObject(user, encryptionKey);
            return userMapper.toDto(user);
        });
    }

    /**
     * Delete the user by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        LOG.debug("Request to delete User : {}", id);
        userRepository.deleteById(id);
    }
}
