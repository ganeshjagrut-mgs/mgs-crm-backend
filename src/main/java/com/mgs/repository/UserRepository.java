package com.mgs.repository;

import com.mgs.domain.User;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the User entity.
 */
@SuppressWarnings("unused")
@Repository
public interface UserRepository extends JpaRepository<User, Long>, JpaSpecificationExecutor<User> {
    Optional<User> findByEmail(String email);

    Optional<User> findByEmailAndTenantId(String email, Long tenantId);

    List<User> findByTenantId(Long tenantId);

    @Query("select u from User u where u.email = :email and u.tenant.id = :tenantId and u.isActive = true")
    Optional<User> findActiveUserByEmailAndTenant(@Param("email") String email, @Param("tenantId") Long tenantId);
}
