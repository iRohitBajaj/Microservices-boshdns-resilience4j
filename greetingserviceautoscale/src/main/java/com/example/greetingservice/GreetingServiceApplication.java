package com.example.greetingservice;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.http.client.SimpleClientHttpRequestFactory;
import org.springframework.web.client.RestTemplate;

import java.util.concurrent.TimeUnit;

@SpringBootApplication
public class GreetingServiceApplication {

    @Value("${restclient.timeout}")
    private int timeout;

	public static void main(String[] args) {
		SpringApplication.run(GreetingServiceApplication.class, args);
	}

    @Bean(name="restTemplate")
    RestTemplate restTemplate(){
        return new RestTemplate();
    }

    @Bean(name="timeBoundRestTemplate")
    RestTemplate timeBoundRestTemplate(){
        return new RestTemplate(getClientHttpRequestFactory());
    }

    private SimpleClientHttpRequestFactory getClientHttpRequestFactory()
    {
        SimpleClientHttpRequestFactory clientHttpRequestFactory
                = new SimpleClientHttpRequestFactory();
        //Connect timeout
        clientHttpRequestFactory.setConnectTimeout((int) TimeUnit.MILLISECONDS.toMillis(timeout));

        //Read timeout
        clientHttpRequestFactory.setReadTimeout((int) TimeUnit.MILLISECONDS.toMillis(timeout));
        return clientHttpRequestFactory;
    }


}
