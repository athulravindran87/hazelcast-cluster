package com.athul.hazelcastserver1.config;

import com.hazelcast.config.Config;
import com.hazelcast.config.EvictionPolicy;
import com.hazelcast.config.GroupConfig;
import com.hazelcast.config.InterfacesConfig;
import com.hazelcast.config.JoinConfig;
import com.hazelcast.config.MapConfig;
import com.hazelcast.config.MaxSizeConfig;
import com.hazelcast.config.NetworkConfig;
import com.hazelcast.config.QueueConfig;
import com.hazelcast.core.Hazelcast;
import com.hazelcast.core.HazelcastInstance;
import com.hazelcast.eureka.one.EurekaOneDiscoveryStrategyFactory;
import com.netflix.discovery.EurekaClient;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.HashMap;
import java.util.Map;

@Configuration
@Slf4j
public class ServerConfiguration
{

    @Value("${hazelcast.port:5701}")
    private int portNumber;

    @Value("${spring.cloud.client.ip-address}")
    private String hostName;

    @Bean("hzServerInstance")
    public HazelcastInstance hazelcastInstance(@Autowired Config hazelcastConfig)
    {
        log.info("Hazelcast instance {}", hazelcastConfig.getInstanceName());
        return Hazelcast.getOrCreateHazelcastInstance(hazelcastConfig);
    }

    @Bean
    public Config hazelcastConfig(@Autowired EurekaClient eurekaClient)
    {
        EurekaOneDiscoveryStrategyFactory.setEurekaClient(eurekaClient);
        Config config = new Config();
        config.setInstanceName("hazelcast-datastore-instance");
        config.setProperty("hazelcast.jmx", "false");
        config.setProperty("hazelcast.logging.type", "slf4j");
        config.setProperty("hazelcast.rest.enabled", "true");
        config.setProperty("hazelcast.http.healthcheck.enabled", "true");
        config.setProperty("hazelcast.discovery.enabled", "true");
        config.setProperty("hazelcast.discovery.public.ip.enabled", "true");
        config.setNetworkConfig(
                new NetworkConfig().setPublicAddress(hostName+":" + portNumber).setPort(portNumber).setReuseAddress(
                        true).addOutboundPort(0).setPortAutoIncrement(true).setJoin(createJoin()).setInterfaces(
                        new InterfacesConfig().setEnabled(false).addInterface("10.10.1.*")));

        config.setGroupConfig(new GroupConfig("hazelcast-server"));
        config.setMapConfigs(createMapConfig());
        config.setQueueConfigs(createQueueConfig());
        return config;
    }


    private JoinConfig createJoin()
    {
        JoinConfig joinConfig = new JoinConfig();
        joinConfig.getEurekaConfig()
                .setEnabled(true)
                .setProperty("self-registration", "true")
                .setProperty("namespace", "hazelcast")
                .setProperty("skip-eureka-registration-verification", "true")
                .setProperty("use-metadata-for-host-and-port", "true");
        joinConfig.getMulticastConfig().setEnabled(false);
        joinConfig.getTcpIpConfig().setEnabled(false);
        joinConfig.getAwsConfig().setEnabled(false);
        return joinConfig;
    }

    private Map <String, MapConfig> createMapConfig()
    {
        Map <String, MapConfig> mapConfigs = new HashMap <>();

        mapConfigs.put("test-map", new MapConfig().setName("test-map").setMaxSizeConfig(
                new MaxSizeConfig(200, MaxSizeConfig.MaxSizePolicy.FREE_HEAP_SIZE)).setEvictionPolicy(
                EvictionPolicy.LRU).setTimeToLiveSeconds(28000));

        return mapConfigs;

    }

    private Map <String, QueueConfig> createQueueConfig()
    {

        Map <String, QueueConfig> qConfigs = new HashMap <>();
        qConfigs.put("test-queue", new QueueConfig().setName("test-queue").setMaxSize(200));
        return qConfigs;
    }

}
