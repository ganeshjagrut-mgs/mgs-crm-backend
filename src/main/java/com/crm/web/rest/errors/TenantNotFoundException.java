package com.crm.web.rest.errors;

import org.springframework.http.HttpStatus;
import org.springframework.web.ErrorResponseException;
import tech.jhipster.web.rest.errors.ProblemDetailWithCause;

import java.net.URI;

/**
 * Exception thrown when a user does not have an associated tenant.
 */
public class TenantNotFoundException extends ErrorResponseException {

    private static final long serialVersionUID = 1L;

    public TenantNotFoundException() {
        this("User does not have an associated tenant");
    }

    public TenantNotFoundException(String message) {
        super(
            HttpStatus.UNAUTHORIZED,
            ProblemDetailWithCause.ProblemDetailWithCauseBuilder
                .instance()
                .withStatus(HttpStatus.UNAUTHORIZED.value())
                .withType(URI.create(ErrorConstants.DEFAULT_TYPE.toString()))
                .withTitle("Tenant not found")
                .withDetail(message)
                .build(),
            null
        );
    }
}
