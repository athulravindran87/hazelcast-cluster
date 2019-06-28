package com.athul.common.client;

import com.athul.common.BaseTest;
import com.hazelcast.client.HazelcastClient;
import com.hazelcast.client.config.ClientConfig;
import com.hazelcast.client.config.ClientNetworkConfig;
import com.hazelcast.config.DiscoveryConfig;
import com.hazelcast.core.HazelcastInstance;
import com.hazelcast.eureka.one.EurekaOneDiscoveryStrategyFactory;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentMatchers;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import static org.hamcrest.Matchers.*;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;

@RunWith(PowerMockRunner.class)
@PrepareForTest({ HazelcastClient.class, EurekaOneDiscoveryStrategyFactory.class })
public class ClientConfigurationTest extends BaseTest
{

    private ClientConfiguration testObj;

    @Before
    public void setUp() throws Exception
    {
        testObj = new ClientConfiguration();
        PowerMockito.mockStatic(HazelcastClient.class);
        PowerMockito.mockStatic(EurekaOneDiscoveryStrategyFactory.class);
    }

    @Test
    public void testHazelCastInstance() throws Exception
    {

        PowerMockito.when(HazelcastClient.newHazelcastClient(ArgumentMatchers.any())).thenReturn(mock(HazelcastInstance.class));

        HazelcastInstance result = testObj.hazelcastInstance(mock(ClientConfig.class));
        assertThat(result, instanceOf(HazelcastInstance.class));

        PowerMockito.verifyStatic(HazelcastClient.class);

        HazelcastClient.newHazelcastClient(ArgumentMatchers.any(ClientConfig.class));

    }

    @Test
    public void testClientConfig() throws Exception
    {
        ClientConfig result = testObj.hazelcastConfig();

        assertThat(result.getProperties(), allOf(
                hasEntry("hazelcast.logging.type", "slf4j"),
                hasEntry("hazelcast.jmx", "true"),
                hasEntry("hazelcast.discovery.enabled", "true"),
                hasEntry("hazelcast.socket.bind.any", "false"),
                hasEntry("hazelcast.socket.client.bind.any", "true")
        ));

        assertThat(result.getGroupConfig().getName(), equalTo("hazelcast-server"));

        assertThat(result.getNetworkConfig(), instanceOf(ClientNetworkConfig.class));
        assertTrue(result.getNetworkConfig().isRedoOperation());
        assertTrue(result.getNetworkConfig().isSmartRouting());
        assertThat(result.getNetworkConfig().getConnectionAttemptLimit(), equalTo(100_000));
        assertThat(result.getNetworkConfig().getConnectionAttemptPeriod(), equalTo(10_000));

    }

    @Test
    public void testNetworkConfig() throws Exception
    {
        ClientConfig result = testObj.hazelcastConfig();

        assertThat(result.getNetworkConfig(), instanceOf(ClientNetworkConfig.class));
        assertTrue(result.getNetworkConfig().isRedoOperation());
        assertTrue(result.getNetworkConfig().isSmartRouting());
        assertThat(result.getNetworkConfig().getConnectionAttemptLimit(), equalTo(100_000));
        assertThat(result.getNetworkConfig().getConnectionAttemptPeriod(), equalTo(10_000));

    }


    @Test
    public void testDiscoveryConfig() throws Exception
    {
        ClientConfig result = testObj.hazelcastConfig();

        assertThat(result.getNetworkConfig().getDiscoveryConfig(), instanceOf(DiscoveryConfig.class));

        PowerMockito.verifyStatic(EurekaOneDiscoveryStrategyFactory.class);
        EurekaOneDiscoveryStrategyFactory.setEurekaClient(null);

        assertThat(result.getNetworkConfig().getDiscoveryConfig().getDiscoveryStrategyConfigs().size(), equalTo(1));

        assertThat(result.getNetworkConfig().getDiscoveryConfig().getDiscoveryStrategyConfigs(), allOf(hasItem(allOf(

                hasProperty("discoveryStrategyFactory", instanceOf(EurekaOneDiscoveryStrategyFactory.class)),
                hasProperty("properties")
        ))));

    }
}