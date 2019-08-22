package com.athul;

import com.athul.hazelcastserver1.HazelcastServer1ApplicationTests;
import com.athul.hazelcastserver1.config.ServerConfigurationTest;
import com.athul.hazelcastserver1.health.HazelcastHealthTest;
import org.junit.runner.RunWith;
import org.junit.runners.Suite;

@RunWith(Suite.class)
@Suite.SuiteClasses({HazelcastServer1ApplicationTests.class, HazelcastHealthTest.class, ServerConfigurationTest.class })
public class AllTests
{
}
