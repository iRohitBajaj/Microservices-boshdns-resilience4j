package com.example.personadetailsservice.controller;

import com.example.personadetailsservice.model.Person;
import com.example.personadetailsservice.repository.PersonRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class PersonController {

    private PersonRepository repository;

    @Autowired
    public PersonController(PersonRepository repository) {
        this.repository = repository;
    }

    @GetMapping("/person/{name}")
    ResponseEntity<Person> personDetails(@PathVariable("name") String name){
        return new ResponseEntity<>(repository.findByName(name), HttpStatus.OK);
    }

}
