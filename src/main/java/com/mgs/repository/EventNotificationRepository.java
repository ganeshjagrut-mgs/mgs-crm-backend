package com.mgs.repository;

import com.mgs.domain.EventNotification;
import java.util.List;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the EventNotification entity.
 */
@SuppressWarnings("unused")
@Repository
public interface EventNotificationRepository extends JpaRepository<EventNotification, Long>, JpaSpecificationExecutor<EventNotification> {
    @Query("select eventNotification from EventNotification eventNotification where eventNotification.user.email = ?#{authentication.name}")
    List<EventNotification> findByUserIsCurrentUser();
}
