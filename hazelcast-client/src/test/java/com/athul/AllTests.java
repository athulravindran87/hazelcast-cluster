package com.athul;

import com.athul.hazelcastclient1.HazelcastClient1ApplicationTests;
import com.athul.hazelcastclient1.config.ClientConfigurationTest;
import org.junit.runner.RunWith;
import org.junit.runners.Suite;

@RunWith(Suite.class)
@Suite.SuiteClasses({HazelcastClient1ApplicationTests.class, ClientConfigurationTest.class })
public class AllTests
{
}
