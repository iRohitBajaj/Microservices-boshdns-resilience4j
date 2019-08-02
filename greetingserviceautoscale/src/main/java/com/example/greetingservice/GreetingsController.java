package com.example.greetingservice;

import com.example.greetingservice.model.Person;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import io.github.resilience4j.retry.annotation.Retry;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import java.net.SocketTimeoutException;
import java.util.Map;

@RestController
@Slf4j
public class GreetingsController {

    RestTemplate restTemplate;
    WeatherService weatherService;

    @Value("${persondetailsservice.url:http://persondetailsserviceautoscale.apps.internal:8080/person/}")
    private String pdsUrl;

    @Autowired
    public GreetingsController(@Qualifier("restTemplate") RestTemplate restTemplate, WeatherService weatherService) {
        this.restTemplate = restTemplate;
        this.weatherService = weatherService;
    }

    @GetMapping("/greeting/{name}")
    ResponseEntity<String> greeting(@PathVariable("name") String name){
        Person person = this.restTemplate.getForObject(pdsUrl+name, Person.class);
        Map<String, Double> data = getWeather(person.getZip());
        return ResponseEntity.ok("Hello "+name+ " from "+person.getCity()+", today's temperature in "+person.getCity()+" is "+data.get("temp")+" with humidity "+data.get("humidity"));
    }

    private Map<String, Double> getWeather(String zip){
        try {
            return weatherService.callWeatherEndpoint(zip);
        }catch(SocketTimeoutException ex){
            log.error(ex.getMessage());
        }
        return null;
    }
}
