package com.mgs.security;

import org.springframework.security.core.AuthenticationException;

/**
 * This exception is thrown when a user tries to authenticate with an inactive account.
 */
public class UserNotActivatedException extends AuthenticationException {

    private static final long serialVersionUID = 1L;

    public UserNotActivatedException(String message) {
        super(message);
    }

    public UserNotActivatedException(String message, Throwable cause) {
        super(message, cause);
    }
}
