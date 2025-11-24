package com.crm.web.rest.errors;

import org.springframework.http.HttpStatus;
import org.springframework.web.ErrorResponseException;
import tech.jhipster.web.rest.errors.ProblemDetailWithCause;

import java.net.URI;

/**
 * Exception thrown when a user is not found during authentication.
 */
public class UserNotFoundException extends ErrorResponseException {

    private static final long serialVersionUID = 1L;

    public UserNotFoundException() {
        this("User not found");
    }

    public UserNotFoundException(String message) {
        super(
            HttpStatus.UNAUTHORIZED,
            ProblemDetailWithCause.ProblemDetailWithCauseBuilder
                .instance()
                .withStatus(HttpStatus.UNAUTHORIZED.value())
                .withType(URI.create(ErrorConstants.DEFAULT_TYPE.toString()))
                .withTitle("User not found")
                .withDetail(message)
                .build(),
            null
        );
    }
}
