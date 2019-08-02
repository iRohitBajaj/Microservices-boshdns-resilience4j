package com.example.personadetailsservice.repository;

import com.example.personadetailsservice.model.Person;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PersonRepository extends JpaRepository<Person, Long> {

    Person findByName(String name);

}
