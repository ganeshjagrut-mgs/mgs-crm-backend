package com.mgs.web.rest;

import com.mgs.domain.enumeration.*;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * REST controller for retrieving all enumerations in the application.
 */
@RestController
@RequestMapping("/api")
public class EnumResource {

    private static final Logger LOG = LoggerFactory.getLogger(EnumResource.class);

    /**
     * {@code GET  /enums} : get all enumerations.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body containing all enums.
     */
    @GetMapping("/enums")
    public ResponseEntity<Map<String, List<String>>> getAllEnums() {
        LOG.debug("REST request to get all Enums");

        Map<String, List<String>> enums = new HashMap<>();

        // Add all enumerations
        enums.put("AddressType", getEnumValues(AddressType.class));
        enums.put("ComplaintPriority", getEnumValues(ComplaintPriority.class));
        enums.put("ComplaintSource", getEnumValues(ComplaintSource.class));
        enums.put("ComplaintStatus", getEnumValues(ComplaintStatus.class));
        enums.put("CustomerStatus", getEnumValues(CustomerStatus.class));
        enums.put("CustomerType", getEnumValues(CustomerType.class));
        enums.put("DealStatus", getEnumValues(DealStatus.class));
        enums.put("DiscountType", getEnumValues(DiscountType.class));
        enums.put("LeadStatus", getEnumValues(LeadStatus.class));
        enums.put("PipelineModule", getEnumValues(PipelineModule.class));
        enums.put("QuotationStatus", getEnumValues(QuotationStatus.class));
        enums.put("SubscriptionStatus", getEnumValues(SubscriptionStatus.class));
        enums.put("TaskPriority", getEnumValues(TaskPriority.class));
        enums.put("TaskStatus", getEnumValues(TaskStatus.class));
        enums.put("TenantStatus", getEnumValues(TenantStatus.class));
        enums.put("TicketPriority", getEnumValues(TicketPriority.class));
        enums.put("TicketStatus", getEnumValues(TicketStatus.class));

        return ResponseEntity.ok().body(enums);
    }

    /**
     * Helper method to get enum values as a list of strings.
     *
     * @param enumClass the enum class
     * @return list of enum values as strings
     */
    private <E extends Enum<E>> List<String> getEnumValues(Class<E> enumClass) {
        return Arrays.stream(enumClass.getEnumConstants()).map(Enum::name).collect(Collectors.toList());
    }
}