package com.athul.hazelcastclient1.health;

import com.hazelcast.core.HazelcastInstance;
import com.hazelcast.core.Member;
import lombok.extern.slf4j.Slf4j;
import org.eclipse.collections.impl.factory.Sets;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.actuate.health.Health;
import org.springframework.boot.actuate.health.HealthIndicator;
import org.springframework.stereotype.Component;

import java.util.Objects;

@Component
@Slf4j
public class HazelcastClientHealth implements HealthIndicator
{

    public static final String HAZELCAST_NODE = "Hazelcast-client-node";

    @Autowired
    @Qualifier("dsHzInstance")
    private HazelcastInstance hazelcastInstance;

    @Override
    public Health health()
    {
        try
        {

            if(Objects.isNull(hazelcastInstance))
            {
                return Health.down().withDetail(HAZELCAST_NODE, "No hazelcast server exists").build();
            }
            if(! hazelcastInstance.getLifecycleService().isRunning())
            {
                return Health.down().withDetail(HAZELCAST_NODE, "No hazelcast server instances are running").build();
            }

            return Health.up()
                    .withDetail("Cluster_Details ", getClusterDetails(hazelcastInstance))
                    .build();
        }
        catch(Exception e)
        {
            return Health.down().withDetail(HAZELCAST_NODE, "Error while checking health").build();
        }

    }

    protected String getClusterDetails(HazelcastInstance hazelcastInstance)
    {
        return Sets.adapt(hazelcastInstance.getCluster().getMembers()).collect(Member::getAddress).toString();
    }

}
