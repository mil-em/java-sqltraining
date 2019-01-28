package com.miloszmatejko.model;

public class DataSourceException extends Exception {
    public DataSourceException(String errorMessage, Throwable e) {
                super ( errorMessage, e);
    }
}
