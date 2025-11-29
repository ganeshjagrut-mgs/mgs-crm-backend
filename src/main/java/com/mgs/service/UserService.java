package com.mgs.service;

import com.mgs.domain.User;
import com.mgs.domain.enumeration.Role;
import com.mgs.domain.enumeration.TenantStatus;
import com.mgs.repository.UserRepository;
import com.mgs.service.dto.*;
import com.mgs.service.mapper.UserMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.crypto.password.PasswordEncoder;
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

    private final TenantService tenantService;

    private final PasswordEncoder passwordEncoder;

    private final RoleService roleService;

    private final UserRoleService userRoleService;

    public UserService(UserRepository userRepository, UserMapper userMapper, TenantService tenantService, PasswordEncoder passwordEncoder, RoleService roleService, UserRoleService userRoleService) {
        this.userRepository = userRepository;
        this.userMapper = userMapper;
        this.tenantService = tenantService;
        this.passwordEncoder = passwordEncoder;
        this.roleService = roleService;
        this.userRoleService = userRoleService;
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
     * Update a user.
     *
     * @param userDTO the entity to save.
     * @return the persisted entity.
     */
    public UserDTO update(UserDTO userDTO) {
        LOG.debug("Request to update User : {}", userDTO);
        User user = userMapper.toEntity(userDTO);
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

        return userRepository
            .findById(userDTO.getId())
            .map(existingUser -> {
                userMapper.partialUpdate(existingUser, userDTO);

                return existingUser;
            })
            .map(userRepository::save)
            .map(userMapper::toDto);
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
        return userRepository.findById(id).map(userMapper::toDto);
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

        userDTO.setPasswordHash(passwordEncoder.encode(userDTO.getPasswordHash()));
        userDTO.setTenant(tenant);
        userDTO.setIsActive(false);
        UserDTO savedUser = save(userDTO);

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

        TenantDTO savedTenant = tenantService.save(tenantDTO);
        return savedTenant;
    }
}
