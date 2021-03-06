package com.athul.hazelcastserver1;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;
import org.springframework.boot.SpringApplication;

import static org.junit.Assert.assertNotNull;
import static org.mockito.ArgumentMatchers.eq;

@RunWith(PowerMockRunner.class)
@PrepareForTest(SpringApplication.class)
public class HazelcastServer1ApplicationTests
{
    private HazelcastServer1Application testObj;

    @Before
    public void setUp()
    {
        this.testObj = new HazelcastServer1Application();
        PowerMockito.mockStatic(SpringApplication.class);
    }

    @Test
    public void contextLoads() throws Exception
    {
        assertNotNull(this.testObj);
    }

    @Test
    public void testApplicationRuns() throws Exception
    {
        String[] args = {"Some arg", "Some arg"};

        assertNotNull(testObj);

        HazelcastServer1Application.main(args);

        PowerMockito.verifyStatic(SpringApplication.class);

        SpringApplication.run(eq(HazelcastServer1Application.class), eq("Some arg"), eq("Some arg"));
    }

}
