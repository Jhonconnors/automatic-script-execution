package com.example.exception;

public class InvalidScriptException extends Exception {
    public InvalidScriptException(String message) {
        super(message);
    }

    public InvalidScriptException(String message, Throwable cause) {
        super(message, cause);
    }
}
