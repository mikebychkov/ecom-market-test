package com.example.demo.service;

import com.example.demo.annotations.ControlRequestCount;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;

@Service
@Log4j2
public class SomeServiceImpl implements SomeService {

    @Override
    @ControlRequestCount("doSomething")
    public void doSomething() {

        log.info("DO SOMETHING");
    }
}
