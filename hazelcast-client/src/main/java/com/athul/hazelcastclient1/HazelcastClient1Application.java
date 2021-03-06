package com.athul.hazelcastclient1;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;

@SpringBootApplication
@EnableEurekaClient
public class HazelcastClient1Application
{

    public static void main(String[] args)
    {
        SpringApplication.run(HazelcastClient1Application.class, args);
    }

}
