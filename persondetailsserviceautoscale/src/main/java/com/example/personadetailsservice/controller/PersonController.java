package com.example.personadetailsservice.controller;

import com.example.personadetailsservice.model.Person;
import com.example.personadetailsservice.service.PersonService;
import io.github.resilience4j.bulkhead.annotation.Bulkhead;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import io.github.resilience4j.retry.annotation.Retry;
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
    @CircuitBreaker(name="getpersoncall"
           , fallbackMethod = "getAnonymous"
    )
    //@Retry(name="getpersoncall")
    @Bulkhead(name="getpersoncall")
    ResponseEntity<Person> personDetails(@PathVariable("name") String name) throws IllegalStateException{
        Timer timer = meterRegistry.timer("db.processing","processing time","in seconds");
        return timer.record( () -> personService.getPersonDetails(name)
                .map(ResponseEntity::ok)
                .orElseThrow(() -> new IllegalStateException("person not found")));
    }

    ResponseEntity<Person> getAnonymous(Throwable ex){
        return new ResponseEntity<>(Person.builder()
                .name("Anonymous")
                .city("Honolulu")
                .zip("10005")
                .build(), HttpStatus.OK);
    }
    

}
