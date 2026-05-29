package com.co.bancolombia.franchise.domain.exceptions;

import org.springframework.http.HttpStatus;

public class BusinessException extends ApplicationException{

    public enum Type {
        ERROR_MONGO("Error with the DB operations.", HttpStatus.CONFLICT),
        ERROR_MONGO_CONNECTION("Error connecting to the DB.", HttpStatus.SERVICE_UNAVAILABLE),
        ERROR_MONGO_AUTHENTICATION("DB authentication failed.", HttpStatus.UNAUTHORIZED),
        ERROR_MONGO_TIMEOUT("DB operation timeout.", HttpStatus.GATEWAY_TIMEOUT),
        ERROR_MONGO_PERMISSION("Insufficient DB permissions.", HttpStatus.FORBIDDEN),
        ERROR_FIELDS("Error with the fields of the request.", HttpStatus.BAD_REQUEST);

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

    public BusinessException(Type type, String exceptionInfo) {
        this(type, exceptionInfo, null);
    }

    public BusinessException(Type type, String exceptionInfo, HttpStatus customHttpStatus) {
        super(type.message.concat(" ").concat(exceptionInfo));
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
