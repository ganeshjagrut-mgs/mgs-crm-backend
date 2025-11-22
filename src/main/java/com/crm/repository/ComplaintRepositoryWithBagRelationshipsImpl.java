package com.crm.repository;

import com.crm.domain.Complaint;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;
import java.util.stream.IntStream;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;

/**
 * Utility repository to load bag relationships based on https://vladmihalcea.com/hibernate-multiplebagfetchexception/
 */
public class ComplaintRepositoryWithBagRelationshipsImpl implements ComplaintRepositoryWithBagRelationships {

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public Optional<Complaint> fetchBagRelationships(Optional<Complaint> complaint) {
        return complaint.map(this::fetchComplaintRelatedPersons);
    }

    @Override
    public Page<Complaint> fetchBagRelationships(Page<Complaint> complaints) {
        return new PageImpl<>(fetchBagRelationships(complaints.getContent()), complaints.getPageable(), complaints.getTotalElements());
    }

    @Override
    public List<Complaint> fetchBagRelationships(List<Complaint> complaints) {
        return Optional.of(complaints).map(this::fetchComplaintRelatedPersons).orElse(Collections.emptyList());
    }

    Complaint fetchComplaintRelatedPersons(Complaint result) {
        return entityManager
            .createQuery(
                "select complaint from Complaint complaint left join fetch complaint.complaintRelatedPersons where complaint.id = :id",
                Complaint.class
            )
            .setParameter("id", result.getId())
            .getSingleResult();
    }

    List<Complaint> fetchComplaintRelatedPersons(List<Complaint> complaints) {
        HashMap<Object, Integer> order = new HashMap<>();
        IntStream.range(0, complaints.size()).forEach(index -> order.put(complaints.get(index).getId(), index));
        List<Complaint> result = entityManager
            .createQuery(
                "select complaint from Complaint complaint left join fetch complaint.complaintRelatedPersons where complaint in :complaints",
                Complaint.class
            )
            .setParameter("complaints", complaints)
            .getResultList();
        Collections.sort(result, (o1, o2) -> Integer.compare(order.get(o1.getId()), order.get(o2.getId())));
        return result;
    }
}
