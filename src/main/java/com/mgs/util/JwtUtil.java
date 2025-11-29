package com.mgs.util;

import com.mgs.web.rest.errors.BadRequestAlertException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.jwt.Jwt;
import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * Utility class for JWT token operations.
 */
public class JwtUtil {

    private static final ObjectMapper objectMapper = new ObjectMapper();

    private JwtUtil() {}

    /**
     * Extract tenant ID from the current authenticated user's JWT token.
     *
     * @return the tenant ID from the token
     * @throws BadRequestAlertException if tenant ID is not found in token or authentication is missing
     */
    public static Long getTenantIdFromToken() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication == null || !(authentication.getPrincipal() instanceof Jwt)) {
            throw new BadRequestAlertException("Authentication token not found", "jwt", "notauthenticated");
        }

        Jwt jwt = (Jwt) authentication.getPrincipal();

        // Try both tenantId and tenant_id claim names
        Object tenantIdClaim = jwt.getClaim("tenantId");
        if (tenantIdClaim == null) {
            tenantIdClaim = jwt.getClaim("tenant_id");
        }

        if (tenantIdClaim == null) {
            throw new BadRequestAlertException("Tenant ID not found in token", "jwt", "tenantidmissing");
        }

        if (tenantIdClaim instanceof Long) {
            return (Long) tenantIdClaim;
        } else if (tenantIdClaim instanceof Integer) {
            return ((Integer) tenantIdClaim).longValue();
        } else if (tenantIdClaim instanceof Number) {
            return ((Number) tenantIdClaim).longValue();
        }

        throw new BadRequestAlertException("Invalid tenant ID format in token", "jwt", "invalidtenantid");
    }
}
