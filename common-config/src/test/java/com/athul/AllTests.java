package com.athul;

import com.athul.common.client.CacheControllerTest;
import com.athul.common.client.QueueControllerTest;
import org.junit.runner.RunWith;
import org.junit.runners.Suite;

@RunWith(Suite.class)
@Suite.SuiteClasses({ QueueControllerTest.class, CacheControllerTest.class })
public class AllTests
{
}
