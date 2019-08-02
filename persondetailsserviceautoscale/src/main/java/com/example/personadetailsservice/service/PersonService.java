package com.example.personadetailsservice.service;

import com.example.personadetailsservice.model.Person;
import com.example.personadetailsservice.repository.PersonRepository;
import io.micrometer.core.instrument.MeterRegistry;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class PersonService {

    private PersonRepository repository;

    MeterRegistry meterRegistry;

    @Autowired
    public PersonService(PersonRepository repository, MeterRegistry meterRegistry) {
        this.repository = repository;
        this.meterRegistry = meterRegistry;
    }

    public Optional<Person> getPersonDetails(String name){
        return Optional.ofNullable(repository.findByName(name));
    }
}
