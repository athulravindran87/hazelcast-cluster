package com.athul;

import com.athul.hazelcastclient1.HazelcastClient1ApplicationTests;
import com.athul.hazelcastclient1.health.HazelcastClientHealthTest;
import org.junit.runner.RunWith;
import org.junit.runners.Suite;

@RunWith(Suite.class)
@Suite.SuiteClasses({HazelcastClient1ApplicationTests.class, HazelcastClientHealthTest.class })
public class AllTests
{
}
