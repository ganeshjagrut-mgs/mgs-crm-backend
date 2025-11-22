package com.crm.service.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Objects;
import java.util.UUID;

/**
 * A DTO for the {@link com.crm.domain.Pipeline} entity.
 */
@Schema(description = "Pipeline - Sales pipeline/opportunities")
@SuppressWarnings("common-java:DuplicatedBlocks")
public class PipelineDTO implements Serializable {

    private Long id;

    @NotNull
    private String pipelineName;

    private BigDecimal totalAmount;

    private Integer noOfSamples;

    private UUID correlationId;

    private UserDTO pipelineOwner;

    private CustomerDTO customer;

    private MasterStaticTypeDTO stageOfPipeline;

    private SubPipelineDTO subPipeline;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getPipelineName() {
        return pipelineName;
    }

    public void setPipelineName(String pipelineName) {
        this.pipelineName = pipelineName;
    }

    public BigDecimal getTotalAmount() {
        return totalAmount;
    }

    public void setTotalAmount(BigDecimal totalAmount) {
        this.totalAmount = totalAmount;
    }

    public Integer getNoOfSamples() {
        return noOfSamples;
    }

    public void setNoOfSamples(Integer noOfSamples) {
        this.noOfSamples = noOfSamples;
    }

    public UUID getCorrelationId() {
        return correlationId;
    }

    public void setCorrelationId(UUID correlationId) {
        this.correlationId = correlationId;
    }

    public UserDTO getPipelineOwner() {
        return pipelineOwner;
    }

    public void setPipelineOwner(UserDTO pipelineOwner) {
        this.pipelineOwner = pipelineOwner;
    }

    public CustomerDTO getCustomer() {
        return customer;
    }

    public void setCustomer(CustomerDTO customer) {
        this.customer = customer;
    }

    public MasterStaticTypeDTO getStageOfPipeline() {
        return stageOfPipeline;
    }

    public void setStageOfPipeline(MasterStaticTypeDTO stageOfPipeline) {
        this.stageOfPipeline = stageOfPipeline;
    }

    public SubPipelineDTO getSubPipeline() {
        return subPipeline;
    }

    public void setSubPipeline(SubPipelineDTO subPipeline) {
        this.subPipeline = subPipeline;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof PipelineDTO)) {
            return false;
        }

        PipelineDTO pipelineDTO = (PipelineDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, pipelineDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "PipelineDTO{" +
            "id=" + getId() +
            ", pipelineName='" + getPipelineName() + "'" +
            ", totalAmount=" + getTotalAmount() +
            ", noOfSamples=" + getNoOfSamples() +
            ", correlationId='" + getCorrelationId() + "'" +
            ", pipelineOwner=" + getPipelineOwner() +
            ", customer=" + getCustomer() +
            ", stageOfPipeline=" + getStageOfPipeline() +
            ", subPipeline=" + getSubPipeline() +
            "}";
    }
}
