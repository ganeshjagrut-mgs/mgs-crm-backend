package com.crm.repository;

import com.crm.domain.Customer;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the Customer entity.
 *
 * When extending this class, extend CustomerRepositoryWithBagRelationships too.
 * For more information refer to https://github.com/jhipster/generator-jhipster/issues/17990.
 */
@Repository
public interface CustomerRepository
    extends CustomerRepositoryWithBagRelationships, JpaRepository<Customer, Long>, JpaSpecificationExecutor<Customer> {
    @Query("select customer from Customer customer where customer.user.login = ?#{authentication.name}")
    List<Customer> findByUserIsCurrentUser();

    @Query("select customer from Customer customer where customer.outstandingPerson.login = ?#{authentication.name}")
    List<Customer> findByOutstandingPersonIsCurrentUser();

    default Optional<Customer> findOneWithEagerRelationships(Long id) {
        return this.fetchBagRelationships(this.findById(id));
    }

    default List<Customer> findAllWithEagerRelationships() {
        return this.fetchBagRelationships(this.findAll());
    }

    default Page<Customer> findAllWithEagerRelationships(Pageable pageable) {
        return this.fetchBagRelationships(this.findAll(pageable));
    }
}
