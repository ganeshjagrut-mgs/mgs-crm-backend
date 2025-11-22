package com.crm.util;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.jwt.Jwt;

import java.util.Base64;
import com.fasterxml.jackson.databind.JsonNode;
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
     * @return the tenant ID from the token, or null if not found
     */
    public static Long getTenantIdFromToken() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        
        if (authentication != null && authentication.getPrincipal() instanceof Jwt jwt) {
            Object tenantIdClaim = jwt.getClaim("tenant_id");
            if (tenantIdClaim != null) {
                if (tenantIdClaim instanceof Long) {
                    return (Long) tenantIdClaim;
                } else if (tenantIdClaim instanceof Integer) {
                    return ((Integer) tenantIdClaim).longValue();
                } else if (tenantIdClaim instanceof Number) {
                    return ((Number) tenantIdClaim).longValue();
                }
            }
        }
        
        return null;
    }

    /**
     * Extract tenant ID from a raw JWT token string.
     *
     * @param token the JWT token string (without "Bearer " prefix)
     * @return the tenant ID from the token, or null if not found or invalid
     */
    public static Long getTenantIdFromTokenString(String token) {
        try {
            // JWT has 3 parts separated by dots: header.payload.signature
            String[] parts = token.split("\\.");
            if (parts.length != 3) {
                return null;
            }

            // Decode the payload (second part)
            String payload = new String(Base64.getUrlDecoder().decode(parts[1]));
            
            // Parse JSON
            JsonNode jsonNode = objectMapper.readTree(payload);
            
            // Extract tenant_id
            if (jsonNode.has("tenant_id")) {
                return jsonNode.get("tenant_id").asLong();
            }
            
            return null;
        } catch (Exception e) {
            // Log the error if needed
            return null;
        }
    }

    /**
     * Extract subject (username) from a raw JWT token string.
     *
     * @param token the JWT token string (without "Bearer " prefix)
     * @return the subject from the token, or null if not found or invalid
     */
    public static String getSubjectFromTokenString(String token) {
        try {
            String[] parts = token.split("\\.");
            if (parts.length != 3) {
                return null;
            }

            String payload = new String(Base64.getUrlDecoder().decode(parts[1]));
            JsonNode jsonNode = objectMapper.readTree(payload);
            
            if (jsonNode.has("sub")) {
                return jsonNode.get("sub").asText();
            }
            
            return null;
        } catch (Exception e) {
            return null;
        }
    }

    /**
     * Extract authorities from a raw JWT token string.
     *
     * @param token the JWT token string (without "Bearer " prefix)
     * @return the authorities from the token, or null if not found or invalid
     */
    public static String getAuthoritiesFromTokenString(String token) {
        try {
            String[] parts = token.split("\\.");
            if (parts.length != 3) {
                return null;
            }

            String payload = new String(Base64.getUrlDecoder().decode(parts[1]));
            JsonNode jsonNode = objectMapper.readTree(payload);
            
            if (jsonNode.has("auth")) {
                return jsonNode.get("auth").asText();
            }
            
            return null;
        } catch (Exception e) {
            return null;
        }
    }
}
