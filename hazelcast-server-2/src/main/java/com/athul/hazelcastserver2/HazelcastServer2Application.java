package com.athul.hazelcastserver2;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;

@SpringBootApplication(scanBasePackages = "com.athul.common.server")
@EnableEurekaClient
public class HazelcastServer2Application
{

    public static void main(String[] args)
    {
        SpringApplication.run(HazelcastServer2Application.class, args);
    }

}
