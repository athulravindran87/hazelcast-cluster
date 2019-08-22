package com.athul.common.server;

import com.athul.common.BaseTest;
import com.hazelcast.config.*;
import com.hazelcast.core.Hazelcast;
import com.hazelcast.core.HazelcastInstance;
import com.hazelcast.eureka.one.EurekaOneDiscoveryStrategyFactory;
import com.netflix.discovery.EurekaClient;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.contrib.java.lang.system.SystemErrRule;
import org.junit.contrib.java.lang.system.SystemOutRule;
import org.junit.runner.RunWith;
import org.mockito.ArgumentMatchers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.Map;

import static org.hamcrest.Matchers.*;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@RunWith(PowerMockRunner.class)
@PrepareForTest({ Hazelcast.class, EurekaOneDiscoveryStrategyFactory.class })
public class ServerConfigurationTest extends BaseTest
{

    @Rule
    public final SystemOutRule systemOutRule = new SystemOutRule().muteForSuccessfulTests().enableLog();

    @Rule
    public final SystemErrRule systemErrRule = new SystemErrRule().muteForSuccessfulTests().enableLog();

    @Mock
    private EurekaClient eurekaClient;

    @Mock
    private Config config;

    @InjectMocks
    private ServerConfiguration testObj;

    @Before
    public void setUp() throws Exception
    {
        ReflectionTestUtils.setField(testObj, "portNumber", 5701);
        ReflectionTestUtils.setField(testObj, "hostName", "192.168.2.0");
        PowerMockito.mockStatic(Hazelcast.class);
        PowerMockito.mockStatic(EurekaOneDiscoveryStrategyFactory.class);

    }

    @Test
    public void hazelcastInstance()
    {
        when(config.getInstanceName()).thenReturn("somename");
        PowerMockito.when(Hazelcast.getOrCreateHazelcastInstance(config)).thenReturn(mock(HazelcastInstance.class));


        assertThat(testObj.hazelcastInstance(config), instanceOf(HazelcastInstance.class));


        PowerMockito.verifyStatic(Hazelcast.class);
        Hazelcast.getOrCreateHazelcastInstance(ArgumentMatchers.any(Config.class));

    }

    @Test
    public void hazelcastConfig()
    {
        Config result = testObj.hazelcastConfig(eurekaClient);
        PowerMockito.verifyStatic(EurekaOneDiscoveryStrategyFactory.class);
        EurekaOneDiscoveryStrategyFactory.setEurekaClient(eurekaClient);

        assertThat(result.getInstanceName(),equalTo("hazelcast-datastore-instance"));

        assertThat(result.getProperties(), allOf(
                hasEntry("hazelcast.logging.type", "slf4j"),
                hasEntry("hazelcast.jmx", "false"),
                hasEntry("hazelcast.discovery.enabled", "true"),
                hasEntry("hazelcast.rest.enabled", "true"),
                hasEntry("hazelcast.discovery.public.ip.enabled", "true"),
                hasEntry("hazelcast.http.healthcheck.enabled", "true")
        ));


    }

    @Test
    public void testNetworkConfig()
    {
        Config result = testObj.hazelcastConfig(eurekaClient);

        assertThat(result.getNetworkConfig(), instanceOf(NetworkConfig.class));

        assertThat(result.getNetworkConfig().getPublicAddress(), equalTo("192.168.2.0:5701"));
        assertThat(result.getNetworkConfig().getPort(), equalTo(5701));
        assertThat(result.getNetworkConfig().isReuseAddress(), equalTo(true));
        assertThat(result.getNetworkConfig().isPortAutoIncrement(), equalTo(true));
        assertThat(result.getNetworkConfig().getOutboundPorts(), hasItem(0));

    }

    @Test
    public void testJoin()
    {
        JoinConfig result = testObj.hazelcastConfig(eurekaClient).getNetworkConfig().getJoin();

        assertThat(result.getEurekaConfig().isEnabled(), equalTo(true));

        assertThat(result.getEurekaConfig().getProperties(),
                allOf(  hasEntry("self-registration", "true"),
                        hasEntry("namespace", "hazelcast"),
                        hasEntry("skip-eureka-registration-verification", "true"),
                        hasEntry("use-metadata-for-host-and-port", "true")

                        ));
        assertThat(result.getMulticastConfig().isEnabled(), equalTo(false));
        assertThat(result.getTcpIpConfig().isEnabled(), equalTo(false));
        assertThat(result.getAwsConfig().isEnabled(), equalTo(false));

    }

    @Test
    public void testInterfaces()
    {
        InterfacesConfig result = testObj.hazelcastConfig(eurekaClient).getNetworkConfig().getInterfaces();

        assertThat(result.isEnabled(), equalTo(false));
        assertThat(result.getInterfaces(), hasItem("10.10.1.*"));
    }

    @Test
    public void testGroupConfig()
    {
        GroupConfig result = testObj.hazelcastConfig(eurekaClient).getGroupConfig();

        assertThat(result.getName(), equalTo("hazelcast-server"));
    }

    @Test
    public void testMapConfig()
    {
        Map <String, MapConfig> result = testObj.hazelcastConfig(eurekaClient).getMapConfigs();

        MapConfig mapConfig = result.get("test-map");

        assertThat(result.size(), equalTo(1));
        assertThat(mapConfig.getName(), equalTo("test-map"));
        assertThat(mapConfig.getEvictionPolicy(), equalTo(EvictionPolicy.LRU));
        assertThat(mapConfig.getTimeToLiveSeconds(), equalTo(28_000));
        assertThat(mapConfig.getMaxSizeConfig().getSize(), equalTo(200));
        assertThat(mapConfig.getMaxSizeConfig().getMaxSizePolicy(), equalTo(MaxSizeConfig.MaxSizePolicy.FREE_HEAP_SIZE));
    }

    @Test
    public void testQueueConfig()
    {
        Map<String, QueueConfig> result = testObj.hazelcastConfig(eurekaClient).getQueueConfigs();

        assertThat(result.size(), equalTo(1));

        QueueConfig queueConfig = result.get("test-queue");
        assertThat(queueConfig.getName(), equalTo("test-queue"));
        assertThat(queueConfig.getMaxSize(), equalTo(200));
    }
}