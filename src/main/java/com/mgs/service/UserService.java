package com.mgs.service;

import com.mgs.domain.User;
import com.mgs.domain.enumeration.Role;
import com.mgs.domain.enumeration.TenantStatus;
import com.mgs.repository.UserRepository;
import com.mgs.service.dto.*;
import com.mgs.service.mapper.UserMapper;
import java.util.Optional;

import com.mgs.util.EncryptionUtil;
import com.mgs.util.JwtUtil;
import com.mgs.web.rest.errors.EncryptionKeyNotFoundException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.security.MessageDigest;
import java.security.SecureRandom;
import java.nio.charset.StandardCharsets;
import java.util.Base64;

/**
 * Service Implementation for managing {@link com.mgs.domain.User}.
 */
@Service
@Transactional
public class UserService {

    private static final Logger LOG = LoggerFactory.getLogger(UserService.class);

    private final UserRepository userRepository;

    private final UserMapper userMapper;

    private final TenantService tenantService;

    private final PasswordEncoder passwordEncoder;

    private final RoleService roleService;

    private final UserRoleService userRoleService;

    private final EncryptionUtil encryptionUtil;

    private final TenantEncryptionKeyService tenantEncryptionKeyService;

    public UserService(UserRepository userRepository, UserMapper userMapper, TenantService tenantService,
                       PasswordEncoder passwordEncoder, RoleService roleService, UserRoleService userRoleService,
                       EncryptionUtil encryptionUtil, TenantEncryptionKeyService tenantEncryptionKeyService) {
        this.userRepository = userRepository;
        this.userMapper = userMapper;
        this.tenantService = tenantService;
        this.passwordEncoder = passwordEncoder;
        this.roleService = roleService;
        this.userRoleService = userRoleService;
        this.encryptionUtil = encryptionUtil;
        this.tenantEncryptionKeyService = tenantEncryptionKeyService;
    }

    /**
     * Save a user.
     *
     * @param userDTO the entity to save.
     * @return the persisted entity.
     */
    public UserDTO save(UserDTO userDTO) {
        LOG.debug("Request to save User : {}", userDTO);
        userDTO.setIsActive(true);
        User user = userMapper.toEntity(userDTO);
        user = userRepository.save(user);
        return userMapper.toDto(user);
    }

    /**
     * Save a user with explicit tenant ID (used during signup when no JWT token exists).
     *
     * @param userDTO the entity to save.
     * @param tenantId the tenant ID.
     * @return the persisted entity.
     */
    private UserDTO saveWithTenant(UserDTO userDTO, Long tenantId) {
        LOG.debug("Request to save User with tenant ID: {}", tenantId);
        User user = userMapper.toEntity(userDTO);

        String emailHash = EncryptionUtil.hashEmail(user.getEmail());
        user.setEmailHash(emailHash);
        LOG.debug("Generated email hash for user: {}", emailHash);

        String encryptionKey = encryptionUtil
            .getEncryptionKey(tenantId)
            .orElseThrow(() -> new EncryptionKeyNotFoundException("User could not be saved. Please contact administrator.", tenantId));
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

        try {
            Long tenantId = JwtUtil.getTenantIdFromToken();
            String encryptionKey = encryptionUtil
                .getEncryptionKey(tenantId)
                .orElseThrow(() -> new EncryptionKeyNotFoundException("User could not be updated. Please contact administrator.", tenantId));
            encryptionUtil.encryptObject(user, encryptionKey);
        } catch (Exception e) {
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

        Optional<String> encryptionKeyOpt = Optional.empty();
        try {
            Long tenantId = JwtUtil.getTenantIdFromToken();
            encryptionKeyOpt = encryptionUtil.getEncryptionKey(tenantId);
            if (encryptionKeyOpt.isEmpty()) {
                throw new EncryptionKeyNotFoundException("User could not be updated. Please contact administrator.", tenantId);
            }
        } catch (EncryptionKeyNotFoundException e) {
            throw e;
        } catch (Exception e) {
            LOG.debug("Skipping encryption - no tenant context available: {}", e.getMessage());
        }

        final Optional<String> finalEncryptionKey = encryptionKeyOpt;
        return userRepository
            .findById(userDTO.getId())
            .map(existingUser -> {
                finalEncryptionKey.ifPresent(key -> encryptionUtil.decryptObject(existingUser, key));
                userMapper.partialUpdate(existingUser, userDTO);
                finalEncryptionKey.ifPresent(key -> encryptionUtil.encryptObject(existingUser, key));
                return existingUser;
            })
            .map(userRepository::save)
            .map(savedUser -> {
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

        Optional<String> encryptionKeyOpt = Optional.empty();
        try {
            Long tenantId = JwtUtil.getTenantIdFromToken();
            encryptionKeyOpt = encryptionUtil.getEncryptionKey(tenantId);
            if (encryptionKeyOpt.isEmpty()) {
                throw new EncryptionKeyNotFoundException("User data could not be retrieved. Please contact administrator.", tenantId);
            }
        } catch (EncryptionKeyNotFoundException e) {
            throw e;  // Re-throw encryption key not found exception
        } catch (Exception e) {
            // No tenant context (e.g., in tests) - skip decryption
            LOG.debug("Skipping decryption - no tenant context available: {}", e.getMessage());
        }

        final Optional<String> finalEncryptionKey = encryptionKeyOpt;
        return userRepository.findById(id).map(user -> {
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

    public UserDTO signup(UserDTO userDTO) {
        LOG.debug("Request to signup: {}", userDTO);

        TenantDTO tenant = createTenant(userDTO.getTenant());
        createTenantEncryptionKey(tenant, userDTO.getPin());
        userDTO.setPasswordHash(passwordEncoder.encode(userDTO.getPasswordHash()));
        userDTO.setTenant(tenant);
        userDTO.setIsActive(true);
        UserDTO savedUser = saveWithTenant(userDTO, tenant.getId());

        RoleDTO roleDTO = new RoleDTO();
        roleDTO.setName(Role.COMPANY_OWNER.toString());
        roleDTO.setTenant(tenant);
        roleDTO = roleService.save(roleDTO);

        UserRoleDTO userRoleDTO = new UserRoleDTO();
        userRoleDTO.setRole(roleDTO);
        userRoleDTO.setUser(savedUser);
        userRoleDTO.setTenant(tenant);

        userRoleService.save(userRoleDTO);

        LOG.debug("Signup completed successfully for user: {}", savedUser.getId());
        return savedUser;
    }

    private TenantDTO createTenant(TenantDTO tenantDTO) {
        LOG.debug("Creating new tenant: {}", tenantDTO.getCode());
        String code = "TEN" + System.currentTimeMillis();
        System.out.println("code: " + code);
        tenantDTO.setStatus(TenantStatus.ACTIVE);
        tenantDTO.setCode(code);

        return tenantService.save(tenantDTO);
    }

    private void createTenantEncryptionKey(TenantDTO tenant, String pin) {
        LOG.debug("Creating encryption key for tenant: {} with provided PIN", tenant.getId());

        try {
            String encryptedDataKey = EncryptionUtil.generateKey();

            SecureRandom random = new SecureRandom();
            byte[] saltBytes = new byte[16];
            random.nextBytes(saltBytes);
            String pinSalt = Base64.getEncoder().encodeToString(saltBytes);

            // Hash PIN with salt
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            String saltedPin = pin + pinSalt;
            byte[] hashBytes = digest.digest(saltedPin.getBytes(StandardCharsets.UTF_8));
            String pinHash = Base64.getEncoder().encodeToString(hashBytes);

            // Create TenantEncryptionKey DTO
            TenantEncryptionKeyDTO encryptionKeyDTO = new TenantEncryptionKeyDTO();
            encryptionKeyDTO.setKeyVersion(1);
            encryptionKeyDTO.setEncryptedDataKey(encryptedDataKey);
            encryptionKeyDTO.setPinHash(pinHash);
            encryptionKeyDTO.setPinSalt(pinSalt);
            encryptionKeyDTO.setIsActive(true);
            encryptionKeyDTO.setTenant(tenant);
            tenantEncryptionKeyService.save(encryptionKeyDTO);

            LOG.debug("Encryption key created successfully for tenant: {}", tenant.getId());
        } catch (Exception e) {
            LOG.error("Error creating encryption key for tenant: {}", tenant.getId(), e);
            throw new RuntimeException("Failed to create encryption key for tenant", e);
        }
    }
}
