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

        // Get tenant ID from JWT token and encrypt if available
        try {
            Long tenantId = JwtUtil.getTenantIdFromToken();
            String encryptionKey = encryptionUtil
                .getEncryptionKey(tenantId)
                .orElseThrow(() -> new EncryptionKeyNotFoundException(tenantId));
            encryptionUtil.encryptObject(user, encryptionKey);
        } catch (Exception e) {
            // No tenant context available (e.g., in tests or unauthenticated requests)
            // Skip encryption and continue
            LOG.debug("Skipping encryption - no tenant context available: {}", e.getMessage());
        }

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

        // Get tenant ID from JWT token and encrypt if available
        try {
            Long tenantId = JwtUtil.getTenantIdFromToken();
            String encryptionKey = encryptionUtil
                .getEncryptionKey(tenantId)
                .orElseThrow(() -> new EncryptionKeyNotFoundException(tenantId));
            encryptionUtil.encryptObject(user, encryptionKey);
        } catch (Exception e) {
            // No tenant context available (e.g., in tests or unauthenticated requests)
            // Skip encryption and continue
            LOG.debug("Skipping encryption - no tenant context available: {}", e.getMessage());
        }

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

        // Get tenant ID from JWT token
        Optional<String> encryptionKeyOpt = Optional.empty();
        try {
            Long tenantId = JwtUtil.getTenantIdFromToken();
            encryptionKeyOpt = encryptionUtil.getEncryptionKey(tenantId);
            if (encryptionKeyOpt.isEmpty()) {
                throw new EncryptionKeyNotFoundException(tenantId);
            }
        } catch (Exception e) {
            // No tenant context available (e.g., in tests or unauthenticated requests)
            // Skip encryption/decryption and continue
            LOG.debug("Skipping encryption - no tenant context available: {}", e.getMessage());
        }

        final Optional<String> finalEncryptionKey = encryptionKeyOpt;
        return userRepository
            .findById(userDTO.getId())
            .map(existingUser -> {
                // Decrypt if encryption key is available
                finalEncryptionKey.ifPresent(key -> encryptionUtil.decryptObject(existingUser, key));

                userMapper.partialUpdate(existingUser, userDTO);

                // Encrypt if encryption key is available
                finalEncryptionKey.ifPresent(key -> encryptionUtil.encryptObject(existingUser, key));

                return existingUser;
            })
            .map(userRepository::save)
            .map(savedUser -> {
                // Decrypt for returning if encryption key is available
                finalEncryptionKey.ifPresent(key -> encryptionUtil.decryptObject(savedUser, key));
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

        // Get tenant ID from JWT token
        Optional<String> encryptionKeyOpt = Optional.empty();
        try {
            Long tenantId = JwtUtil.getTenantIdFromToken();
            encryptionKeyOpt = encryptionUtil.getEncryptionKey(tenantId);
            if (encryptionKeyOpt.isEmpty()) {
                throw new EncryptionKeyNotFoundException(tenantId);
            }
        } catch (Exception e) {
            // No tenant context available (e.g., in tests or unauthenticated requests)
            // Skip decryption and continue
            LOG.debug("Skipping decryption - no tenant context available: {}", e.getMessage());
        }

        final Optional<String> finalEncryptionKey = encryptionKeyOpt;
        return userRepository.findById(id).map(user -> {
            // Decrypt if encryption key is available
            finalEncryptionKey.ifPresent(key -> encryptionUtil.decryptObject(user, key));
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
