package com.mgs.repository;

import com.mgs.domain.Ticket;
import java.util.List;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the Ticket entity.
 */
@SuppressWarnings("unused")
@Repository
public interface TicketRepository extends JpaRepository<Ticket, Long>, JpaSpecificationExecutor<Ticket> {
    @Query("select ticket from Ticket ticket where ticket.assignedTo.email = ?#{authentication.name}")
    List<Ticket> findByAssignedToIsCurrentUser();
}
