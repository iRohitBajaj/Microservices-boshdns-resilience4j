package com.example.greetingservice.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@AllArgsConstructor
@NoArgsConstructor
public class Person {

    private Long id;
    private String name;
    private String city;

    public Person(String name, String city) {
        this.name = name;
        this.city =city;
    }
}