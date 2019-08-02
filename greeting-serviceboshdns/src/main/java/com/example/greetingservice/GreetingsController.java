package com.example.greetingservice;

import com.example.greetingservice.model.Person;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

@RestController
public class GreetingsController {

    RestTemplate restTemplate;



    @Autowired
    public GreetingsController(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    @GetMapping("/greeting/{name}")
    ResponseEntity<String> greeting(@PathVariable("name") String name){
        Person person = this.restTemplate.getForObject("http://persondetailsserviceboshdns.apps.internal:8080/person/"+name, Person.class);
        return ResponseEntity.ok("Hello "+name+ " from "+person.getCity());
    }
}
