package com.example.demo.service;

public interface RequestCountService {

    void registerRequest(String ip, String aValue) throws InterruptedException;
}
