package com.athul.common.client;

import com.hazelcast.core.HazelcastInstance;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "/queue")
@Slf4j
public class QueueController
{

    @Autowired
    @Qualifier("dsHzInstance")
    private HazelcastInstance data;


    @RequestMapping(value = "/get", method = RequestMethod.GET)
    public String getData() throws Exception
    {
        String result =  (String)this.data.getQueue("test-queue").take();
        log.info("Result from queue is {}",result);
        return result;
    }

    @RequestMapping(value = "/put", method = RequestMethod.POST)
    public void putData(@RequestParam String value) throws Exception
    {
        this.data.getQueue("test-queue").put(value);
    }
}
