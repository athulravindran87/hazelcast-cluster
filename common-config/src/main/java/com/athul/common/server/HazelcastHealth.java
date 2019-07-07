package com.athul.common.server;

import com.hazelcast.core.DistributedObject;
import com.hazelcast.core.Hazelcast;
import com.hazelcast.core.HazelcastInstance;
import com.hazelcast.core.Member;
import com.hazelcast.monitor.LocalMapStats;
import com.hazelcast.nio.Address;
import lombok.extern.slf4j.Slf4j;
import org.eclipse.collections.api.map.MutableMap;
import org.eclipse.collections.impl.collection.mutable.CollectionAdapter;
import org.eclipse.collections.impl.factory.Sets;
import org.springframework.boot.actuate.health.Health;
import org.springframework.boot.actuate.health.HealthIndicator;
import org.springframework.stereotype.Component;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

@Component
@Slf4j
public class HazelcastHealth implements HealthIndicator
{

    public static final String HAZELCAST_NODE = "Hazelcast-node";

    @Override
    public Health health()
    {
        try
        {
            HazelcastInstance hazelcastInstance = Hazelcast.getAllHazelcastInstances().stream().findAny().orElse(null);

            if(Objects.isNull(hazelcastInstance))
            {
                return Health.down().withDetail(HAZELCAST_NODE, "No hazelcast server exists").build();
            }
            if(! hazelcastInstance.getLifecycleService().isRunning())
            {
                return Health.down().withDetail(HAZELCAST_NODE, "No hazelcast server instances are running").build();
            }

            Collection <DistributedObject> distributedObjects = hazelcastInstance.getDistributedObjects();

            return Health.up()
                    .withDetail("Cluster_Details ", clusterDetails(hazelcastInstance))
                    .withDetail("All Objects ", distributedObjects.stream().map(DistributedObject::getName))
                    .withDetail("Caches",cacheStats(hazelcastInstance))
                    .build();
        }
        catch(Exception e)
        {
            return Health.down().withDetail(HAZELCAST_NODE, "Error while checking health").build();
        }

    }

    protected Object clusterDetails(HazelcastInstance instance)
    {
        Map <String, Object> clusters = new HashMap <>();

        clusters.put("Name", instance.getConfig().getGroupConfig().getName());
        clusters.put("State", instance.getCluster().getClusterState());
        clusters.put("Version", instance.getCluster().getClusterVersion());
        clusters.put("Cluster Members", Sets.adapt(instance.getCluster().getMembers()).collect(this::getInetAddress).toString());

        return clusters;
    }

    protected Address getInetAddress(Member member)
    {
        try
        {
            return member.getAddress();
        }
        catch(Exception e)
        {
            log.error("Unable to get InetAddress for host", e);
        }
        return null;
    }

    protected MutableMap <String, LocalMapStats>  cacheStats(HazelcastInstance hazelcastInstance)
    {
       return CollectionAdapter.adapt(
                hazelcastInstance.getDistributedObjects()).select(
                distributedObject->distributedObject.getServiceName().endsWith("mapService")).toMap(
                DistributedObject::getName,
                distributedObject->hazelcastInstance.getMap(distributedObject.getName()).getLocalMapStats());


    }
}
