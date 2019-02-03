package com.miloszmatejko.controller;

public class ControllerException extends Exception {
    public ControllerException(String message, Throwable cause) {
        super ( message, cause );
    }
}
