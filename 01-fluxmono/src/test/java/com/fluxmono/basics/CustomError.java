package com.fluxmono.basics;

public class CustomError extends Throwable {
    private String message;

    public CustomError(Throwable cause) {
        super(cause);
        this.message = cause.getMessage();
    }

    @Override
    public String getMessage() {
        return message;
    }
}
