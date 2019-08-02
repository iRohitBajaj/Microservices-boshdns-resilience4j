package com.example.personadetailsservice;

import com.example.personadetailsservice.model.Person;
import com.example.personadetailsservice.repository.PersonRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;


@SpringBootApplication
@Slf4j
public class PersonadetailsserviceApplication implements CommandLineRunner
{

	public static void main(String[] args) {
		SpringApplication.run(PersonadetailsserviceApplication.class, args);
	}

	@Autowired
	PersonRepository repository;

	@Override
	public void run(String... args) throws Exception {
		log.info("StartApplication...");

		repository.save(new Person("John","New York"));
		repository.save(new Person("Jake","Chicago"));
		repository.save(new Person("Adam","Atlanta"));

		System.out.println("\nfindAll()");
		repository.findAll().forEach(x -> System.out.println(x));

		System.out.println("\nfindById(1L)");
		repository.findById(1l).ifPresent(x -> System.out.println(x));

		System.out.println("\nfindByName('John')");
		System.out.println(repository.findByName("John"));


	}
}
