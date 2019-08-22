package com.athul.hazelcastclient1.health;

import com.hazelcast.config.GroupConfig;
import com.hazelcast.config.MapConfig;
import com.hazelcast.core.Hazelcast;
import com.hazelcast.core.HazelcastInstance;
import com.hazelcast.core.IMap;
import com.hazelcast.core.Member;
import com.hazelcast.monitor.LocalMapStats;
import com.hazelcast.nio.Address;
import org.eclipse.collections.api.set.MutableSet;
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
import java.util.Collections;
import java.util.HashSet;

import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.hasEntry;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.when;

@RunWith(PowerMockRunner.class)
@PrepareForTest({ Hazelcast.class, GroupConfig.class, Address.class })
public class HazelcastClientHealthTest
{

    @Rule
    public final SystemOutRule systemOutRule = new SystemOutRule().muteForSuccessfulTests().enableLog();

    @Rule
    public final SystemErrRule systemErrRule = new SystemErrRule().muteForSuccessfulTests().enableLog();
    @InjectMocks
    private HazelcastClientHealth testObj;

    @Mock(answer = Answers.RETURNS_DEEP_STUBS)
    private HazelcastInstance hazelcastInstance;

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

    @Mock
    private MapConfig mapConfig;


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
        when(hazelcastInstance.getCluster().getMembers()).thenReturn(members);
        when(member.getAddress()).thenReturn(address);
        when(address.getInetAddress()).thenReturn(inetAddress);
        when(groupConfig.getName()).thenReturn("hz_cluster");

    }

    @Test
    public void healthWithNoInstance()
    {
        PowerMockito.when(Hazelcast.getAllHazelcastInstances()).thenReturn(Sets.mutable.empty());

        assertThat(testObj.health().getStatus(), equalTo(Status.DOWN));
        assertThat(testObj.health().getDetails(),
                hasEntry(HazelcastClientHealth.HAZELCAST_NODE,
                        "No hazelcast server instances are running"));
    }

    @Test
    public void healthWithException()
    {
        PowerMockito.when(Hazelcast.getAllHazelcastInstances()).thenReturn(Collections.singleton(hazelcastInstance));

        when(hazelcastInstance.getLifecycleService().isRunning()).thenThrow(RuntimeException.class);

        assertThat(testObj.health().getStatus(), equalTo(Status.DOWN));
        assertThat(testObj.health().getDetails(),
                hasEntry(HazelcastClientHealth.HAZELCAST_NODE,
                        "Error while checking health"));
    }

    @Test
    public void healthWithNoInstanceRunning()
    {
        PowerMockito.when(Hazelcast.getAllHazelcastInstances()).thenReturn(Collections.singleton(hazelcastInstance));

        when(hazelcastInstance.getLifecycleService().isRunning()).thenReturn(false);

        assertThat(testObj.health().getStatus(), equalTo(Status.DOWN));
        assertThat(testObj.health().getDetails(),
                hasEntry(HazelcastClientHealth.HAZELCAST_NODE,
                        "No hazelcast server instances are running"));
    }

    @Test
    public void healthHappyPath()
    {
        PowerMockito.when(Hazelcast.getAllHazelcastInstances()).thenReturn(Collections.singleton(hazelcastInstance));

        when(hazelcastInstance.getLifecycleService().isRunning()).thenReturn(true);

        when(map.getLocalMapStats()).thenReturn(localMapStats);


        assertThat(testObj.health().getStatus(), equalTo(Status.UP));
        assertThat(testObj.health().getDetails().size(), equalTo(1));

    }

    @Test
    public void testClusterDetails() throws Exception
    {
        assertThat(testObj.getClusterDetails(hazelcastInstance),
                equalTo( addresses.toString()));

    }

}