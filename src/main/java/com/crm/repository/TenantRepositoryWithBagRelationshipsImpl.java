package com.crm.repository;

import com.crm.domain.Tenant;
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
public class TenantRepositoryWithBagRelationshipsImpl implements TenantRepositoryWithBagRelationships {

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public Optional<Tenant> fetchBagRelationships(Optional<Tenant> tenant) {
        return tenant.map(this::fetchUsers);
    }

    @Override
    public Page<Tenant> fetchBagRelationships(Page<Tenant> tenants) {
        return new PageImpl<>(fetchBagRelationships(tenants.getContent()), tenants.getPageable(), tenants.getTotalElements());
    }

    @Override
    public List<Tenant> fetchBagRelationships(List<Tenant> tenants) {
        return Optional.of(tenants).map(this::fetchUsers).orElse(Collections.emptyList());
    }

    Tenant fetchUsers(Tenant result) {
        return entityManager
            .createQuery("select tenant from Tenant tenant left join fetch tenant.users where tenant.id = :id", Tenant.class)
            .setParameter("id", result.getId())
            .getSingleResult();
    }

    List<Tenant> fetchUsers(List<Tenant> tenants) {
        HashMap<Object, Integer> order = new HashMap<>();
        IntStream.range(0, tenants.size()).forEach(index -> order.put(tenants.get(index).getId(), index));
        List<Tenant> result = entityManager
            .createQuery("select tenant from Tenant tenant left join fetch tenant.users where tenant in :tenants", Tenant.class)
            .setParameter("tenants", tenants)
            .getResultList();
        Collections.sort(result, (o1, o2) -> Integer.compare(order.get(o1.getId()), order.get(o2.getId())));
        return result;
    }
}
