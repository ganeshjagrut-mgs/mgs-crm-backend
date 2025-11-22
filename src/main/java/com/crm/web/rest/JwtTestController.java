package com.crm.web.rest;

import com.crm.util.JwtUtil;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

/**
 * REST controller for testing JWT token operations.
 */
@RestController
@RequestMapping("/api/test")
public class JwtTestController {

    /**
     * Test endpoint to extract tenant ID from the current authenticated user's token.
     *
     * @return tenant ID and other token information
     */
    @GetMapping("/tenant-from-auth")
    public ResponseEntity<Map<String, Object>> getTenantIdFromAuth() {
        Map<String, Object> response = new HashMap<>();
        
        // Get tenant ID using utility method
        Long tenantId = JwtUtil.getTenantIdFromToken();
        response.put("tenant_id", tenantId);
        
        // Get additional information from authentication
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.getPrincipal() instanceof Jwt jwt) {
            response.put("subject", jwt.getSubject());
            response.put("authorities", jwt.getClaim("auth"));
            response.put("issued_at", jwt.getIssuedAt());
            response.put("expires_at", jwt.getExpiresAt());
        }
        
        return ResponseEntity.ok(response);
    }

    /**
     * Test endpoint to extract tenant ID from a provided JWT token string.
     *
     * @param token the JWT token string
     * @return tenant ID and other token information
     */
    @PostMapping("/tenant-from-token")
    public ResponseEntity<Map<String, Object>> getTenantIdFromTokenString(@RequestBody Map<String, String> request) {
        String token = request.get("token");
        
        if (token == null || token.isEmpty()) {
            return ResponseEntity.badRequest().body(Map.of("error", "Token is required"));
        }
        
        // Remove "Bearer " prefix if present
        if (token.startsWith("Bearer ")) {
            token = token.substring(7);
        }
        
        Map<String, Object> response = new HashMap<>();
        
        // Extract tenant ID
        Long tenantId = JwtUtil.getTenantIdFromTokenString(token);
        response.put("tenant_id", tenantId);
        
        // Extract other claims
        String subject = JwtUtil.getSubjectFromTokenString(token);
        response.put("subject", subject);
        
        String authorities = JwtUtil.getAuthoritiesFromTokenString(token);
        response.put("authorities", authorities);
        
        return ResponseEntity.ok(response);
    }

    /**
     * Test endpoint with the example token from the user.
     *
     * @return tenant ID and other information from the example token
     */
    @GetMapping("/test-example-token")
    public ResponseEntity<Map<String, Object>> testExampleToken() {
        // The example token provided by the user
        String exampleToken = "eyJhbGciOiJIUzUxMiJ9.eyJ0ZW5hbnRfaWQiOjEsInN1YiI6ImFkbWluIiwiZXhwIjoxNzYzOTEzMzk5LCJhdXRoIjoiUk9MRV9BRE1JTiBST0xFX1VTRVIiLCJpYXQiOjE3NjM4MjY5OTl9.W12itUjkUqqWc-J_SWHjtHGatQB7ZKIodWMEtZfiKL4MuowGwcPOjv4bEFoAO4D4V_1Q6LfxChlYk0DP_X64UQ";
        
        Map<String, Object> response = new HashMap<>();
        
        // Extract tenant ID
        Long tenantId = JwtUtil.getTenantIdFromTokenString(exampleToken);
        response.put("tenant_id", tenantId);
        
        // Extract other claims
        String subject = JwtUtil.getSubjectFromTokenString(exampleToken);
        response.put("subject", subject);
        
        String authorities = JwtUtil.getAuthoritiesFromTokenString(exampleToken);
        response.put("authorities", authorities);
        
        response.put("note", "This is the example token provided by the user");
        
        return ResponseEntity.ok(response);
    }
}
