package com.example.personadetailsservice.controller;

import com.example.personadetailsservice.model.Person;
import com.example.personadetailsservice.service.PersonService;
import io.micrometer.core.instrument.MeterRegistry;
import io.micrometer.core.instrument.Timer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class PersonController {

    private PersonService personService;

    MeterRegistry meterRegistry;

    @Autowired
    public PersonController(PersonService personService, MeterRegistry meterRegistry)
    {
        this.personService = personService;
        this.meterRegistry = meterRegistry;
    }

    @GetMapping("/person/{name}")
    ResponseEntity<Person> personDetails(@PathVariable("name") String name){
        Timer timer = meterRegistry.timer("db.processing","processing time","in seconds");
        return timer.record( () -> {
            ResponseEntity<Person> response = new ResponseEntity<>(personService.getPersonDetails(name), HttpStatus.OK);
            return response;
        });
    }
    

}
