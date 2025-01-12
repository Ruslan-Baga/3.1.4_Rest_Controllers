package ru.kata.spring.boot_security.demo.exception;

public class EmailRuntimeException extends RuntimeException {
    public EmailRuntimeException(String message) {
        super(message);
    }
}
