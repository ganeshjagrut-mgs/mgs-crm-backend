package com.mgs.security;

import com.mgs.domain.User;
import com.mgs.domain.UserRole;
import com.mgs.repository.UserRepository;
import com.mgs.repository.UserRoleRepository;
import com.mgs.util.EncryptionUtil;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

/**
 * Authenticate a user from the database.
 */
@Component("userDetailsService")
public class DomainUserDetailsService implements UserDetailsService {

    private static final Logger LOG = LoggerFactory.getLogger(DomainUserDetailsService.class);

    private final UserRepository userRepository;
    private final UserRoleRepository userRoleRepository;
    private final EncryptionUtil encryptionUtil;

    public DomainUserDetailsService(UserRepository userRepository, UserRoleRepository userRoleRepository, EncryptionUtil encryptionUtil) {
        this.userRepository = userRepository;
        this.userRoleRepository = userRoleRepository;
        this.encryptionUtil = encryptionUtil;
    }

    @Override
    @Transactional(readOnly = true)
    public UserDetails loadUserByUsername(final String email) {
        LOG.debug("Authenticating user '{}'", email);

        String emailHash = EncryptionUtil.hashEmail(email);
        LOG.debug("Generated email hash for lookup: {}", emailHash);

        return userRepository
            .findByEmailHash(emailHash)
            .map(user -> {
                LOG.debug("Found user with matching email hash. Tenant ID: {}", user.getTenant().getId());

                Long tenantId = user.getTenant().getId();
                String encryptionKey = encryptionUtil
                    .getEncryptionKey(tenantId)
                    .orElseThrow(() -> new RuntimeException("Encryption key not found for tenant: " + tenantId));

                encryptionUtil.decryptObject(user, encryptionKey);
                LOG.info("Successfully authenticated user: {}. IsActive: {}", email, user.getIsActive());

                return createSpringSecurityUser(email, user);
            })
            .orElseThrow(() -> {
                LOG.warn("User with email {} (hash: {}) was not found", email, emailHash);
                return new UsernameNotFoundException("User with email " + email + " was not found in the database");
            });
    }

    @Transactional(readOnly = true)
    public User getUserWithTenant(final String email) {
        LOG.debug("Loading user with tenant information for '{}'", email);

        String emailHash = EncryptionUtil.hashEmail(email);

        return userRepository
            .findByEmailHash(emailHash)
            .map(user -> {
                Long tenantId = user.getTenant().getId();
                String encryptionKey = encryptionUtil
                    .getEncryptionKey(tenantId)
                    .orElseThrow(() -> new RuntimeException("Encryption key not found for tenant: " + tenantId));

                encryptionUtil.decryptObject(user, encryptionKey);
                LOG.debug("Found and decrypted user with tenant information for email: {}", email);

                return user;
            })
            .orElseThrow(() -> new UsernameNotFoundException("User with email " + email + " was not found in the database"));
    }

    private org.springframework.security.core.userdetails.User createSpringSecurityUser(String lowercaseLogin, User user) {
        if (!user.getIsActive()) {
            throw new UserNotActivatedException("User " + lowercaseLogin + " is not activated");
        }
        List<UserRole> userRoles = userRoleRepository.findByUserId(user.getId());
        List<GrantedAuthority> grantedAuthorities = userRoles
            .stream()
            .map(userRole -> new SimpleGrantedAuthority(userRole.getRole().getName()))
            .collect(Collectors.toList());

        return new org.springframework.security.core.userdetails.User(user.getEmail(), user.getPasswordHash(), grantedAuthorities);
    }
}
