package com.co.bancolombia.franchise.domain.exceptions;

import org.springframework.http.HttpStatus;

public class ValidationException extends ApplicationException {

    public enum Type {
        INVALID_INPUT("Invalid input data.", HttpStatus.BAD_REQUEST),
        INVALID_FORMAT("Invalid data format.", HttpStatus.BAD_REQUEST),
        MISSING_FIELD("Required field is missing.", HttpStatus.BAD_REQUEST),
        AUTHENTICATION_FAILED("Authentication failed.", HttpStatus.UNAUTHORIZED),
        ACCESS_DENIED("Access denied.", HttpStatus.FORBIDDEN),
        NOT_FOUND("Resource not found.", HttpStatus.NOT_FOUND),
        CONFLICT("Resource already exists.", HttpStatus.CONFLICT);

        private final String message;
        private final HttpStatus httpStatus;

        public String getMessage() {
            return message;
        }

        public HttpStatus getHttpStatus() {
            return httpStatus;
        }

        Type(String message, HttpStatus httpStatus) {
            this.message = message;
            this.httpStatus = httpStatus;
        }
    }

    private final Type type;
    private final HttpStatus customHttpStatus;

    public ValidationException(String message) {
        this(message, Type.INVALID_INPUT, null);
    }

    public ValidationException(String message, HttpStatus httpStatus) {
        this(message, Type.INVALID_INPUT, httpStatus);
    }

    public ValidationException(String message, Type type) {
        this(message, type, null);
    }

    public ValidationException(String message, Type type, HttpStatus customHttpStatus) {
        super(type.message.concat(" ").concat(message));
        this.type = type;
        this.customHttpStatus = customHttpStatus;
    }

    public Type getType() {
        return type;
    }

    public HttpStatus getHttpStatus() {
        return customHttpStatus != null ? customHttpStatus : type.getHttpStatus();
    }
}



