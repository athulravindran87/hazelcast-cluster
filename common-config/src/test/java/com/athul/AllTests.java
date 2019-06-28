package com.athul;

import com.athul.common.client.CacheControllerTest;
import com.athul.common.client.ClientConfigurationTest;
import com.athul.common.client.QueueControllerTest;
import com.athul.common.server.HazelcastHealthTest;
import com.athul.common.server.ServerConfigurationTest;
import org.junit.runner.RunWith;
import org.junit.runners.Suite;

@RunWith(Suite.class)
@Suite.SuiteClasses({ServerConfigurationTest.class, ClientConfigurationTest.class, HazelcastHealthTest.class,
        QueueControllerTest.class, CacheControllerTest.class })
public class AllTests
{
}
