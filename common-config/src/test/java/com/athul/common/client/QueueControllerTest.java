package com.athul.common.client;

import com.athul.common.BaseTest;
import com.hazelcast.core.HazelcastInstance;
import com.hazelcast.core.IQueue;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static org.hamcrest.Matchers.equalTo;
import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(MockitoJUnitRunner.class)
public class QueueControllerTest extends BaseTest
{

    @Mock
    private HazelcastInstance data;

    private MockMvc mockMvc;

    @InjectMocks
    private QueueController testObj;

    @Mock
    private IQueue <Object> dataQ;

    @Before
    public void setUp() throws Exception
    {
        this.mockMvc = MockMvcBuilders.standaloneSetup(this.testObj).build();
    }

    @Test
    public void getData() throws Exception
    {

        when(data.getQueue("test-queue")).thenReturn(dataQ);
        when(dataQ.take()).thenReturn("abc");

        assertNotNull(this.testObj);

        mockMvc.perform(get("/queue/get")).andExpect(status().isOk())
                .andExpect(content().string(equalTo("abc")));
    }

    @Test
    public void putData() throws Exception
    {

        when(data.getQueue("test-queue")).thenReturn(dataQ);

        assertNotNull(this.testObj);

        mockMvc.perform(post("/queue/put?value=abc")).andExpect(status().isOk());
        verify(dataQ).put("abc");
    }
}