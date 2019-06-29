package com.athul.common.server;

import com.athul.common.BaseTest;
import com.hazelcast.cluster.ClusterState;
import com.hazelcast.config.GroupConfig;
import com.hazelcast.core.*;
import com.hazelcast.monitor.LocalMapStats;
import com.hazelcast.nio.Address;
import com.hazelcast.version.Version;
import org.eclipse.collections.api.map.MutableMap;
import org.eclipse.collections.api.set.MutableSet;
import org.eclipse.collections.impl.factory.Sets;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Answers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;
import org.springframework.boot.actuate.health.Status;

import java.net.InetAddress;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.Map;

import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.hasEntry;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThat;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@RunWith(PowerMockRunner.class)
@PrepareForTest({ Hazelcast.class, GroupConfig.class, Address.class })
public class HazelcastHealthTest extends BaseTest
{
    @InjectMocks
    private HazelcastHealth testObj;

    @Mock(answer = Answers.RETURNS_DEEP_STUBS)
    private HazelcastInstance hazelcastInstance;

    @Mock
    private DistributedObject distributedObject;

    @Mock
    private IMap map;

    @Mock
    private LocalMapStats localMapStats;


    @Mock(answer = Answers.RETURNS_DEEP_STUBS)
    private Member member;

    @Mock
    private Address address;

    @Mock
    private InetAddress inetAddress;

    @Mock
    private GroupConfig groupConfig;


    private HashSet<Member> members = new HashSet <>();
    private MutableSet <InetAddress> addresses = Sets.mutable.empty();
    private ArrayList<LocalMapStats> localMapStatsArrayList = new ArrayList <>();

    @Before
    public void setUp() throws Exception
    {
        PowerMockito.mockStatic(Hazelcast.class);
        PowerMockito.mockStatic(GroupConfig.class);
        PowerMockito.mockStatic(Address.class);

        addresses.add(inetAddress);
        members.add(member);
        when(hazelcastInstance.getMap("somename")).thenReturn(map);
        when(hazelcastInstance.getDistributedObjects()).thenReturn(Collections.singleton(distributedObject));
        when(hazelcastInstance.getConfig().getGroupConfig()).thenReturn(mock(GroupConfig.class));
        when(hazelcastInstance.getCluster().getClusterState()).thenReturn(ClusterState.ACTIVE);
        when(hazelcastInstance.getCluster().getClusterVersion()).thenReturn(Version.of("3.12"));
        when(hazelcastInstance.getCluster().getMembers()).thenReturn(members);
        when(member.getAddress()).thenReturn(address);
        when(address.getInetAddress()).thenReturn(inetAddress);
        when(distributedObject.getServiceName()).thenReturn("testmapService");
        when(distributedObject.getName()).thenReturn("somename");
        when(hazelcastInstance.getConfig().getGroupConfig()).thenReturn(groupConfig);
        when(groupConfig.getName()).thenReturn("hz_cluster");

    }

    @After
    public void tearDown() throws Exception
    {
    }

    @Test
    public void healthWithNoInstance()
    {
        PowerMockito.when(Hazelcast.getAllHazelcastInstances()).thenReturn(null);

        assertThat(testObj.health().getStatus(), equalTo(Status.DOWN));
        assertThat(testObj.health().getDetails(),
                hasEntry("Hazelcast-node",
                        "Error while checking health"));
    }

    @Test
    public void healthWithNoInstanceRunning()
    {
        PowerMockito.when(Hazelcast.getAllHazelcastInstances()).thenReturn(Collections.singleton(hazelcastInstance));

        when(hazelcastInstance.getLifecycleService().isRunning()).thenReturn(false);

        assertThat(testObj.health().getStatus(), equalTo(Status.DOWN));
        assertThat(testObj.health().getDetails(),
                hasEntry("Hazelcast-node",
                        "No hazelcast server instances are running"));
    }

    @Test
    public void healthHappyPath()
    {
        PowerMockito.when(Hazelcast.getAllHazelcastInstances()).thenReturn(Collections.singleton(hazelcastInstance));

        when(hazelcastInstance.getLifecycleService().isRunning()).thenReturn(true);

        when(map.getLocalMapStats()).thenReturn(localMapStats);


        assertThat(testObj.health().getStatus(), equalTo(Status.UP));
        assertThat(testObj.health().getDetails().size(), equalTo(3));

    }

    @Test
    public void testClusterDetails() throws Exception
    {

        Map<String,Object> result = (Map <String, Object>) testObj.clusterDetails(hazelcastInstance);

        assertThat(result.size(),equalTo(4));

        assertThat(result,
                hasEntry("Version", Version.of("3.12")));

        assertThat(result,
                hasEntry("State", ClusterState.ACTIVE));

        assertThat(result,
                hasEntry("Cluster Members", addresses));

        assertThat(result,
                hasEntry("Name", "hz_cluster"));

    }


    @Test
    public void testCacheStats() throws Exception
    {

        when(member.getAddress()).thenReturn(address);
        when(hazelcastInstance.getMap(anyString())).thenReturn(map);
        when(map.getLocalMapStats()).thenReturn(localMapStats);

        MutableMap <String, LocalMapStats> result = testObj.cacheStats(hazelcastInstance);

        assertThat(result, hasEntry("somename", localMapStats));
    }


    @Test
    public void testgetAddressException() throws Exception
    {
        PowerMockito.when(member.getAddress()).thenReturn(null);

        assertNull(testObj.getInetAddress(member));
    }

}