package com.mgs.web.rest.errors;

import java.net.URI;
import org.springframework.http.HttpStatus;
import org.springframework.web.ErrorResponseException;
import tech.jhipster.web.rest.errors.ProblemDetailWithCause;
import tech.jhipster.web.rest.errors.ProblemDetailWithCause.ProblemDetailWithCauseBuilder;

@SuppressWarnings("java:S110") // Inheritance tree of classes should not be too deep
public class EncryptionKeyNotFoundException extends ErrorResponseException {

    private static final long serialVersionUID = 1L;

    private final Long tenantId;

    public EncryptionKeyNotFoundException(Long tenantId) {
        this(ErrorConstants.DEFAULT_TYPE, "Encryption key not found for tenant", tenantId);
    }

    public EncryptionKeyNotFoundException(String defaultMessage, Long tenantId) {
        this(ErrorConstants.DEFAULT_TYPE, defaultMessage, tenantId);
    }

    public EncryptionKeyNotFoundException(URI type, String defaultMessage, Long tenantId) {
        super(
            HttpStatus.INTERNAL_SERVER_ERROR,
            ProblemDetailWithCauseBuilder
                .instance()
                .withStatus(HttpStatus.INTERNAL_SERVER_ERROR.value())
                .withType(type)
                .withTitle(defaultMessage)
                .withProperty("message", ErrorConstants.ERR_ENCRYPTION_KEY_NOT_FOUND)
                .withProperty("params", tenantId != null ? tenantId.toString() : "unknown")
                .build(),
            null
        );
        this.tenantId = tenantId;
    }

    public Long getTenantId() {
        return tenantId;
    }

    public ProblemDetailWithCause getProblemDetailWithCause() {
        return (ProblemDetailWithCause) this.getBody();
    }
}
