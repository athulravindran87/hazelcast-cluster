# hazelcast-cluster
Hazelcast Cluster using EurekaOneDiscovery 

[![Build Status](http://34.68.205.106/jenkins/buildStatus/icon?job=hazelcast-cluster-master-build&subject=Master%20Build)](http://34.68.205.106/jenkins/job/hazelcast-cluster-master-build/)       [![Build Status](http://34.68.205.106/jenkins/buildStatus/icon?job=hazelcast-cluster-mutation-test&subject=Mutation%20Test)](http://34.68.205.106/jenkins/job/hazelcast-cluster-mutation-test/)    [![Codacy Badge](https://api.codacy.com/project/badge/Grade/e9e89cc98f5d4b0f9fd80d18c9935981)](https://www.codacy.com?utm_source=github.com&amp;utm_medium=referral&amp;utm_content=athulravindran87/hazelcast-cluster&amp;utm_campaign=Badge_Grade)     [![Quality Gate Status](http://34.67.51.46/api/project_badges/measure?project=com.athul%3Ahazelcast-cluster&metric=alert_status)](http://34.67.51.46/dashboard?id=com.athul%3Ahazelcast-cluster)       [![Bugs](http://34.67.51.46/api/project_badges/measure?project=com.athul%3Ahazelcast-cluster&metric=bugs)](http://34.67.51.46/dashboard?id=com.athul%3Ahazelcast-cluster)    [![Coverage](http://34.67.51.46/api/project_badges/measure?project=com.athul%3Ahazelcast-cluster&metric=coverage)](http://34.67.51.46/dashboard?id=com.athul%3Ahazelcast-cluster)    [![Technical Debt](http://34.67.51.46/api/project_badges/measure?project=com.athul%3Ahazelcast-cluster&metric=sqale_index)](http://34.67.51.46/dashboard?id=com.athul%3Ahazelcast-cluster)   [![Maintainability Rating](http://34.67.51.46/api/project_badges/measure?project=com.athul%3Ahazelcast-cluster&metric=sqale_rating)](http://34.67.51.46/dashboard?id=com.athul%3Ahazelcast-cluster)


### This project is an experiment to set up Hazelcast clustering using Eureka as Discovery service. 

## **Technical Stack**:                   	         
1) Eureka Discovery Service.	         	             
2) EurekaOneDiscovery api                            
3) Hazelcast                                         
4) Spring Boot 2 

![hazelcast-server (2)](https://user-images.githubusercontent.com/5833938/60470478-90022880-9c2e-11e9-9c0f-cd30afbcd607.jpg)

| Service Name        | port | Comments                       |  
| ------------------- | -----| -------------------------------|
| discover-server     | 8761 | Eureka discovery server        |
| hazelcast-server-1  | 8762 | hazelcast server instance 1.   |
| hazelcast-server-2  | 8763 | hazelcast server instance 2.   |
| hazelcast-client-1  | 8764 | hazelcast client instance 1.   |
| hazelcast-client-2  | 8765 | hazelcast client instance 1.   |




