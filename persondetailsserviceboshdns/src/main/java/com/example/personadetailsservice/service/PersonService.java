package com.example.personadetailsservice.service;

import com.example.personadetailsservice.model.Person;
import com.example.personadetailsservice.repository.PersonRepository;
import io.micrometer.core.instrument.MeterRegistry;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class PersonService {

    private PersonRepository repository;

    MeterRegistry meterRegistry;

    @Autowired
    public PersonService(PersonRepository repository, MeterRegistry meterRegistry) {
        this.repository = repository;
        this.meterRegistry = meterRegistry;
    }

    public Person getPersonDetails(String name){
        return repository.findByName(name);
    }
}
