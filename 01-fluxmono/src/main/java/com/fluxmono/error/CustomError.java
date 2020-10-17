package com.fluxmono.error;

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
