package com.example.ribbon;

import com.netflix.client.config.IClientConfig;
import com.netflix.loadbalancer.AvailabilityFilteringRule;
import com.netflix.loadbalancer.IPing;
import com.netflix.loadbalancer.IRule;
import com.netflix.loadbalancer.PingUrl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.netflix.ribbon.PropertiesFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/*
      The CustomConfiguration clas must be a @Configuration class, but take care that it is not in a @ComponentScan for the main
      application context. Otherwise, it is shared by all the @RibbonClients. If you use @ComponentScan (or @SpringBootApplication),
      you need to take steps to avoid it being included (for instance, you can put it in a separate, non-overlapping package
      or specify the packages to scan explicitly in the @ComponentScan).
 */
@Configuration
public class RibbonConfig {

    @Autowired
    IClientConfig ribbonClientConfig;

    @Autowired
    private PropertiesFactory propertiesFactory;

    @Bean
    public IPing ribbonPingLocal(IClientConfig config) {
        return new PingUrl(false, "/actuator/health");
    }

    @Bean
    public IRule ribbonRule(IClientConfig config) {
        return new AvailabilityFilteringRule();
    }

}
