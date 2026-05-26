package com.co.bancolombia.franchise.domain.exceptions;

public class BusinessExeption extends ApplicationException{

    public enum Type {
        ERROR_MONGO("Error with the operations");

        private final String message;

        public String getMessage() {
            return message;
        }

        Type(String message) {
            this.message = message;
        }

    }

    private final Type type;

    public BusinessExeption(Type type, String exceptionInfo) {
        super(type.message.concat(" ").concat(exceptionInfo));
        this.type=type;
    }

    public Type getType() {
        return type;
    }
}
