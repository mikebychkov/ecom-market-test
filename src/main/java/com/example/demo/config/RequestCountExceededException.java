package com.example.demo.config;

public class RequestCountExceededException extends RuntimeException {

    public RequestCountExceededException() {
        super("Request count exceeded limit");
    }
}
