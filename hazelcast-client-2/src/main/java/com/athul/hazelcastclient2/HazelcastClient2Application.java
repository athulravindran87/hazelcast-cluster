package com.athul.hazelcastclient2;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;

@SpringBootApplication
@EnableEurekaClient
public class HazelcastClient2Application
{

    public static void main(String[] args)
    {
        SpringApplication.run(HazelcastClient2Application.class, args);
    }

}
