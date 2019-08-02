package com.example.greetingclient.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.netflix.ribbon.RibbonClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

@RestController
@RibbonClient(name="greetingserviceeurekatuned"
 )
public class GreetingService {

    private RestTemplate restTemplate;

    @Autowired
    public GreetingService(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    @GetMapping("/hi/{name}")
    public String callGreetingService(@PathVariable("name") String name) {
        String greeting = this.restTemplate.getForObject("http://greetingserviceeurekatuned/greeting/"+name, String.class);
        return greeting;
    }
}