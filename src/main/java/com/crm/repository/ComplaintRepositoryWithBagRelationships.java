package com.crm.repository;

import com.crm.domain.Complaint;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;

public interface ComplaintRepositoryWithBagRelationships {
    Optional<Complaint> fetchBagRelationships(Optional<Complaint> complaint);

    List<Complaint> fetchBagRelationships(List<Complaint> complaints);

    Page<Complaint> fetchBagRelationships(Page<Complaint> complaints);
}
