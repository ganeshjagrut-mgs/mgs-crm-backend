package com.crm.repository;

import com.crm.domain.Pipeline;
import java.util.List;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the Pipeline entity.
 */
@SuppressWarnings("unused")
@Repository
public interface PipelineRepository extends JpaRepository<Pipeline, Long>, JpaSpecificationExecutor<Pipeline> {
    @Query("select pipeline from Pipeline pipeline where pipeline.pipelineOwner.login = ?#{authentication.name}")
    List<Pipeline> findByPipelineOwnerIsCurrentUser();
}
