package com.marlabs.assessment.exception;

public class UnknownCallerException extends RuntimeException {

    public UnknownCallerException(String callerId) {
        super("Unknown caller: " + callerId);
    }
}