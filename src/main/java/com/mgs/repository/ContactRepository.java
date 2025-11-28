package com.mgs.repository;

import com.mgs.domain.Contact;
import java.util.List;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the Contact entity.
 */
@SuppressWarnings("unused")
@Repository
public interface ContactRepository extends JpaRepository<Contact, Long>, JpaSpecificationExecutor<Contact> {
    @Query("select contact from Contact contact where contact.ownerUser.email = ?#{authentication.name}")
    List<Contact> findByOwnerUserIsCurrentUser();
}
