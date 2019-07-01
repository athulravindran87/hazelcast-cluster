package com.athul.common.client;

import com.hazelcast.core.HazelcastInstance;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(value = "/map")
@Slf4j
public class CacheController
{

    @Autowired
    @Qualifier("dsHzInstance")
    private HazelcastInstance data;

    @GetMapping(value = "/get")
    public String getData(@RequestParam String key)
    {
        String result =  this.data.<Object,String>getMap("test-map").get(key);
        log.info("Result from cache is {}",result);
        return result;
    }

    @PostMapping(value = "/put")
    public void putData(@RequestParam String key, @RequestParam String value)
    {
        this.data.getMap("test-map").put(key,value);
    }


}
