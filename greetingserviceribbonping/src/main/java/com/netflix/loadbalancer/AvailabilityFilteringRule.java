package com.netflix.loadbalancer;

import com.google.common.collect.Collections2;
import com.netflix.client.config.IClientConfig;
import com.netflix.loadbalancer.*;
import com.netflix.servo.annotations.DataSourceType;
import com.netflix.servo.annotations.Monitor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

public class AvailabilityFilteringRule extends PredicateBasedRule {
    private static final Logger LOGGER = LoggerFactory.getLogger(com.netflix.loadbalancer.AvailabilityFilteringRule.class);
    private AbstractServerPredicate predicate = CompositePredicate.withPredicate(new AvailabilityPredicate(this, (IClientConfig)null)).addFallbackPredicate(AbstractServerPredicate.alwaysTrue()).build();

    public AvailabilityFilteringRule() {
    }

    public void initWithNiwsConfig(IClientConfig clientConfig) {
        this.predicate = CompositePredicate.withPredicate(new AvailabilityPredicate(this, clientConfig)).addFallbackPredicate(AbstractServerPredicate.alwaysTrue()).build();
    }

    @Monitor(
            name = "AvailableServersCount",
            type = DataSourceType.GAUGE
    )
    public int getAvailableServersCount() {
        ILoadBalancer lb = this.getLoadBalancer();
        List<Server> servers = lb.getAllServers();
        return servers == null ? 0 : Collections2.filter(servers, this.predicate.getServerOnlyPredicate()).size();
    }

    public Server choose(Object key) {
        int count = 0;
        LOGGER.debug("checking server availability for key :"+key);
        for(Server server = this.roundRobinRule.choose(key); count++ <= 10; server = this.roundRobinRule.choose(key)) {
            if (this.predicate.apply(new PredicateKey(server))) {
                LOGGER.debug("available server is :"+server.getHostPort()+" with aliveness "+server.isAlive());
                return server;
            }
        }

        return super.choose(key);
    }

    public AbstractServerPredicate getPredicate() {
        return this.predicate;
    }
}
