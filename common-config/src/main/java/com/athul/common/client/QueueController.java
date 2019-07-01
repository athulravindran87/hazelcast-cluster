package com.athul.common.client;

import com.hazelcast.core.HazelcastInstance;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(value = "/queue")
@Slf4j
public class QueueController
{

    @Autowired
    @Qualifier("dsHzInstance")
    private HazelcastInstance data;


    @GetMapping(value = "/get")
    public String getData() throws InterruptedException
    {
        String result =  (String)this.data.getQueue("test-queue").take();
        log.info("Result from queue is {}",result);
        return result;
    }

    @PostMapping(value = "/put")
    public void putData(@RequestParam String value) throws InterruptedException
    {
        this.data.getQueue("test-queue").put(value);
    }
}
