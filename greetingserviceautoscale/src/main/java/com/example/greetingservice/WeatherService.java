package com.example.greetingservice;

import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import io.github.resilience4j.retry.annotation.Retry;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import java.net.SocketTimeoutException;
import java.util.Map;

@Component
@Slf4j
public class WeatherService {

    private final static String QUERY_FORMAT = "/data/2.5/weather?zip=%s,us&appid=";

    private final RestTemplate restTemplate;

    @Value("${com.intuit.external.weather.host}")
    private String host;

    @Value("${com.intuit.external.weather.port}")
    private String port;

    @Value("${com.intuit.external.weather.appId}")
    private String appId;

    public WeatherService(@Qualifier("timeBoundRestTemplate") RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    @Retry(name="getWeather")
    @CircuitBreaker(name="getWeather")
    public Map<String, Double> callWeatherEndpoint(String zip) throws SocketTimeoutException {
        String query = String.format(QUERY_FORMAT, zip);
        ResponseEntity<Map> response;
            String uri = "http://" + host + ":" + port;
            final long startTime = System.currentTimeMillis();
            log.error("current time in milliseconds is : "+startTime);
            response = restTemplate.getForEntity(uri + query + appId, Map.class);
            log.debug("Dependency response Duration = " + (System.currentTimeMillis() - startTime));

        return (Map<String, Double>) response.getBody().get("main");
    }
}