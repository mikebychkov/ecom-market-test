package com.example.demo.controller;

import com.example.demo.service.RequestCountService;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;

import java.util.concurrent.ConcurrentHashMap;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class SomeControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private RequestCountService requestCountService;

    @AfterEach
    public void afterEach() {
        ReflectionTestUtils.setField(requestCountService, "store", new ConcurrentHashMap<>());
    }

    MockHttpServletRequestBuilder ip1() {
        return get("/some/endpoint").header("X-Forwarded-For", "111.11.11.111");
    }

    MockHttpServletRequestBuilder ip2() {
        return get("/some/endpoint").header("X-Forwarded-For", "222.22.22.222");
    }

    @Test
    public void singleRequest() throws Exception {

        this.mockMvc.perform(ip1())
                .andDo(print())
                .andExpect(status().isOk());
    }

    @Test
    public void sequentialRequests() throws Exception {

        this.mockMvc.perform(ip1())
                .andDo(print())
                .andExpect(status().isOk());
        this.mockMvc.perform(ip1())
                .andDo(print())
                .andExpect(status().isOk());

        this.mockMvc.perform(ip1())
                .andDo(print())
                .andExpect(status().is(502));
    }

    @Test
    public void sequentialRequestsDifferentIPs() throws Exception {

        this.mockMvc.perform(ip1())
                .andDo(print())
                .andExpect(status().isOk());
        this.mockMvc.perform(ip1())
                .andDo(print())
                .andExpect(status().isOk());

        this.mockMvc.perform(ip2())
                .andDo(print())
                .andExpect(status().isOk());
    }

    @Test
    public void sequentialRequestsNotExceedTimeLimit() throws Exception {

        this.mockMvc.perform(ip1())
                .andDo(print())
                .andExpect(status().isOk());
        this.mockMvc.perform(ip1())
                .andDo(print())
                .andExpect(status().isOk());

        Thread.sleep(5000);

        this.mockMvc.perform(ip1())
                .andDo(print())
                .andExpect(status().isOk());
    }
}