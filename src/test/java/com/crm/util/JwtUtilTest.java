package com.crm.util;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for JwtUtil.
 */
class JwtUtilTest {

    // Example JWT token from the user
    private static final String EXAMPLE_TOKEN = "eyJhbGciOiJIUzUxMiJ9.eyJ0ZW5hbnRfaWQiOjEsInN1YiI6ImFkbWluIiwiZXhwIjoxNzYzOTEzMzk5LCJhdXRoIjoiUk9MRV9BRE1JTiBST0xFX1VTRVIiLCJpYXQiOjE3NjM4MjY5OTl9.W12itUjkUqqWc-J_SWHjtHGatQB7ZKIodWMEtZfiKL4MuowGwcPOjv4bEFoAO4D4V_1Q6LfxChlYk0DP_X64UQ";

    @Test
    void testGetTenantIdFromTokenString() {
        Long tenantId = JwtUtil.getTenantIdFromTokenString(EXAMPLE_TOKEN);
        
        assertNotNull(tenantId, "Tenant ID should not be null");
        assertEquals(1L, tenantId, "Tenant ID should be 1");
    }

    @Test
    void testGetSubjectFromTokenString() {
        String subject = JwtUtil.getSubjectFromTokenString(EXAMPLE_TOKEN);
        
        assertNotNull(subject, "Subject should not be null");
        assertEquals("admin", subject, "Subject should be 'admin'");
    }

    @Test
    void testGetAuthoritiesFromTokenString() {
        String authorities = JwtUtil.getAuthoritiesFromTokenString(EXAMPLE_TOKEN);
        
        assertNotNull(authorities, "Authorities should not be null");
        assertEquals("ROLE_ADMIN ROLE_USER", authorities, "Authorities should be 'ROLE_ADMIN ROLE_USER'");
    }

    @Test
    void testGetTenantIdFromInvalidToken() {
        String invalidToken = "invalid.token.here";
        
        Long tenantId = JwtUtil.getTenantIdFromTokenString(invalidToken);
        
        assertNull(tenantId, "Tenant ID should be null for invalid token");
    }

    @Test
    void testGetTenantIdFromNullToken() {
        Long tenantId = JwtUtil.getTenantIdFromTokenString(null);
        
        assertNull(tenantId, "Tenant ID should be null for null token");
    }

    @Test
    void testGetTenantIdFromEmptyToken() {
        Long tenantId = JwtUtil.getTenantIdFromTokenString("");
        
        assertNull(tenantId, "Tenant ID should be null for empty token");
    }

    @Test
    void testGetSubjectFromInvalidToken() {
        String invalidToken = "invalid.token.here";
        
        String subject = JwtUtil.getSubjectFromTokenString(invalidToken);
        
        assertNull(subject, "Subject should be null for invalid token");
    }

    @Test
    void testGetAuthoritiesFromInvalidToken() {
        String invalidToken = "invalid.token.here";
        
        String authorities = JwtUtil.getAuthoritiesFromTokenString(invalidToken);
        
        assertNull(authorities, "Authorities should be null for invalid token");
    }
}
