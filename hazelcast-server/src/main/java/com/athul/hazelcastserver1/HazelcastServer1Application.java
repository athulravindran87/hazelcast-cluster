package com.athul.hazelcastserver1;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;

@SpringBootApplication
@EnableEurekaClient
public class HazelcastServer1Application
{

    public static void main(String[] args)
    {
        SpringApplication.run(HazelcastServer1Application.class, args);
    }

}
