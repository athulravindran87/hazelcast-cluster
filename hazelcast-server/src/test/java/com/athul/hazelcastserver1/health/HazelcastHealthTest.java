package com.athul.hazelcastserver1.health;

import com.hazelcast.cluster.ClusterState;
import com.hazelcast.config.GroupConfig;
import com.hazelcast.config.MapConfig;
import com.hazelcast.config.QueueConfig;
import com.hazelcast.core.*;
import com.hazelcast.monitor.LocalMapStats;
import com.hazelcast.monitor.LocalQueueStats;
import com.hazelcast.nio.Address;
import com.hazelcast.version.Version;
import org.eclipse.collections.api.map.MutableMap;
import org.eclipse.collections.api.set.MutableSet;
import org.eclipse.collections.impl.factory.Maps;
import org.eclipse.collections.impl.factory.Sets;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.contrib.java.lang.system.SystemErrRule;
import org.junit.contrib.java.lang.system.SystemOutRule;
import org.junit.runner.RunWith;
import org.mockito.Answers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;
import org.springframework.boot.actuate.health.Status;

import java.net.InetAddress;
import java.util.HashSet;
import java.util.Map;

import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.hasEntry;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@RunWith(PowerMockRunner.class)
@PrepareForTest({ Hazelcast.class, GroupConfig.class, Address.class })
public class HazelcastHealthTest
{
    @Rule
    public final SystemOutRule systemOutRule = new SystemOutRule().muteForSuccessfulTests().enableLog();

    @Rule
    public final SystemErrRule systemErrRule = new SystemErrRule().muteForSuccessfulTests().enableLog();
    @InjectMocks
    private HazelcastHealth testObj;

    @Mock(answer = Answers.RETURNS_DEEP_STUBS)
    private HazelcastInstance hazelcastInstance;

    @Mock
    private IMap map;

    @Mock
    private IQueue queue;

    @Mock
    private LocalMapStats localMapStats;

    @Mock
    private LocalQueueStats localQueueStats;


    @Mock(answer = Answers.RETURNS_DEEP_STUBS)
    private Member member;

    @Mock
    private Address address;

    @Mock
    private InetAddress inetAddress;

    @Mock
    private GroupConfig groupConfig;

    @Mock
    private MapConfig mapConfig;

    @Mock
    private QueueConfig queueConfig;


    private HashSet <Member> members = new HashSet <>();
    private MutableSet <Address> addresses = Sets.mutable.empty();

    @Before
    public void setUp() throws Exception
    {
        PowerMockito.mockStatic(Hazelcast.class);
        PowerMockito.mockStatic(GroupConfig.class);
        PowerMockito.mockStatic(Address.class);

        addresses.add(address);
        members.add(member);
        when(hazelcastInstance.getMap("somename")).thenReturn(map);
        when(hazelcastInstance.getConfig().getGroupConfig()).thenReturn(mock(GroupConfig.class));
        when(hazelcastInstance.getCluster().getClusterState()).thenReturn(ClusterState.ACTIVE);
        when(hazelcastInstance.getCluster().getClusterVersion()).thenReturn(Version.of("3.12"));
        when(hazelcastInstance.getCluster().getMembers()).thenReturn(members);
        when(member.getAddress()).thenReturn(address);
        when(address.getInetAddress()).thenReturn(inetAddress);
        when(hazelcastInstance.getConfig().getGroupConfig()).thenReturn(groupConfig);
        when(hazelcastInstance.getConfig().getMapConfigs()).thenReturn(Maps.mutable.of("test-map", mapConfig));
        when(hazelcastInstance.getConfig().getQueueConfigs()).thenReturn(Maps.mutable.of("test-queue", queueConfig));
        when(groupConfig.getName()).thenReturn("hz_cluster");

    }

    @Test
    public void healthWithException()
    {

        when(hazelcastInstance.getLifecycleService().isRunning()).thenThrow(RuntimeException.class);

        assertThat(testObj.health().getStatus(), equalTo(Status.DOWN));
        assertThat(testObj.health().getDetails(),
                hasEntry("Hazelcast-node",
                        "Error while checking health"));
    }

    @Test
    public void healthWithNoInstanceRunning()
    {

        when(hazelcastInstance.getLifecycleService().isRunning()).thenReturn(false);

        assertThat(testObj.health().getStatus(), equalTo(Status.DOWN));
        assertThat(testObj.health().getDetails(),
                hasEntry("Hazelcast-node",
                        "No hazelcast server instances are running"));
    }

    @Test
    public void healthHappyPath()
    {

        when(hazelcastInstance.getLifecycleService().isRunning()).thenReturn(true);

        when(map.getLocalMapStats()).thenReturn(localMapStats);


        assertThat(testObj.health().getStatus(), equalTo(Status.UP));
        assertThat(testObj.health().getDetails().size(), equalTo(5));

    }

    @Test
    public void testClusterDetails() throws Exception
    {

        Map <String,Object> result = (Map <String, Object>) testObj.clusterDetails(hazelcastInstance);

        assertThat(result.size(),equalTo(4));

        assertThat(result,
                hasEntry("Version", Version.of("3.12")));

        assertThat(result,
                hasEntry("State", ClusterState.ACTIVE));

        assertThat(result,
                hasEntry("Cluster Members", addresses.toString()));

        assertThat(result,
                hasEntry("Name", "hz_cluster"));

    }


    @Test
    public void testMapStats() throws Exception
    {

        when(member.getAddress()).thenReturn(address);
        when(hazelcastInstance.getMap("test-map")).thenReturn(map);
        when(map.getLocalMapStats()).thenReturn(localMapStats);

        MutableMap <String, LocalMapStats> result = testObj.mapStats(hazelcastInstance);

        assertThat(result, hasEntry("test-map", localMapStats));
    }


    @Test
    public void testQueueStats() throws Exception
    {

        when(member.getAddress()).thenReturn(address);
        when(hazelcastInstance.getQueue("test-queue")).thenReturn(queue);
        when(queue.getLocalQueueStats()).thenReturn(localQueueStats);

        MutableMap <String, LocalQueueStats> result = testObj.queueStats(hazelcastInstance);

        assertThat(result, hasEntry("test-queue", localQueueStats));
    }


}