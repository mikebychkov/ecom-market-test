package com.example.demo.controller;

import com.example.demo.annotations.ControlRequestCount;
import com.example.demo.service.SomeService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/some")
@RequiredArgsConstructor
public class SomeController {

    private final SomeService someService;

    @GetMapping("/endpoint")
    @ControlRequestCount("someEndpoint")
    public ResponseEntity<Void> someEndpoint() {

        someService.doSomething();

        return ResponseEntity.ok().build();
    }
}
