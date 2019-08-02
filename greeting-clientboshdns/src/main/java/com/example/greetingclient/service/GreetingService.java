package com.example.greetingclient.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;


@RestController
@Slf4j
public class GreetingService {

    private RestTemplate restTemplate;
    @Autowired
    public GreetingService(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    @GetMapping("/hi/{name}")
    public String callGreetingService(@PathVariable("name") String name) {
        return this.restTemplate.getForObject("http://greetingserviceboshdns.apps.internal:8080/greeting/"+name, String.class);
    }
}
