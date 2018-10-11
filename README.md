# 微服务治理（服务发现、熔断、服务降级、路由、网关、客户端负载等）


一、Spring Cloud
________________________________________
1．1 服务治理




1．2 Maven构建项目
1）CentOS7提供私有库（centos-extras），默认是启动状态，如果是禁用状态，则需要启用，大都用户采用yum安装以后续更新。
2）部份用户下载RPM包安装及维护，主要原因无互联内网服务器。
3）少部份用户于开发测试，选择便利脚本安装。
二、Spring Config
________________________________________
2．1服务端
2．1．1 配置库


2．2客户端



三、Spring Eureka（服务发现）
________________________________________

3．1 POM&CODE
<?xml version="1.0"?>
<project xsi:schemaLocation="http://maven.apache.org/POM/4.0.0 http://maven.apache.org/xsd/maven-4.0.0.xsd" xmlns="http://maven.apache.org/POM/4.0.0"
    xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance">
  <modelVersion>4.0.0</modelVersion>
  <groupId>eureka-server</groupId>
  <artifactId>spring-eureka-server</artifactId>
  <version>1.0.1-SNAPSHOT</version>
  
  <parent>
    <groupId>cloud</groupId>
    <artifactId>spring-cloud-pro</artifactId>
    <version>0.0.1-SNAPSHOT</version>
  </parent>
  
  <name>spring-eureka-server</name>

  <properties>
    <project.build.sourceEncoding>UTF-8</project.build.sourceEncoding>
  </properties>
  
  <dependencies>
    <dependency>
       <groupId>org.springframework.cloud</groupId>
       <artifactId>spring-cloud-starter-eureka-server</artifactId>
</dependency>
<dependency>
		<groupId>org.springframework.cloud</groupId>
		<artifactId>spring-cloud-starter-config</artifactId>
</dependency>
<dependency>
		<groupId>org.springframework.cloud</groupId>
		<artifactId>spring-cloud-config-client</artifactId>
</dependency>
  </dependencies>
  <build>
      <plugins>
          <plugin>
              <groupId>org.springframework.boot</groupId>
              <artifactId>spring-boot-maven-plugin</artifactId>
          </plugin>
      </plugins>
  </build>
</project>

项目只有一个主类EurekaServerApplication，添加注解@EnableEurekaServer、@SpringBootApplication
package org.spring.cloud.eureka.server;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.server.EnableEurekaServer;

@SpringBootApplication
@EnableEurekaServer
public class EurekaServerApplication {
	
	public static void main(String[] args) {
		
		SpringApplication.run(EurekaServerApplication.class, args);
        
    }
}



3．2 服务注册中心
3．2．1 单机模式
Maven项目构建，参考POM。
Application.yml定义
spring:
  profiles: default
server:
  port: 9000  
eureka:
  instance:
    hostname: localhost
  client:
    registerWithEureka: false
    fetchRegistry: false
    serviceUrl:
      defaultZone: http://${eureka.instance.hostname}:${server.port}/eureka/
  server:
    enable-self-preservation: false
    eviction-interval-timer-in-ms: 5000

http://localhost:9000
 
【备注】
server:
waitTimeInMsWhenSyncEmpty: 0
提示：EMERGENCY! EUREKA MAY BE INCORRECTLY CLAIMING INSTANCES ARE UP WHEN THEY'RE NOT. RENEWALS ARE LESSER THAN THRESHOLD AND HENCE THE INSTANCES ARE NOT BEING EXPIRED JUST TO BE SAFE.
3．2．2 集群模式
Application.yml
【注意】1）文件内容中各个备份点定义之间，增加“---”
		 2）冒号后面跟上空格再填值
---
spring:
  profiles: default
server:
  port: 9000
eureka:
  instance:
    hostname: eureka-server
  client:
    registerWithEureka: false
    fetchRegistry: false
    serviceUrl:
      defaultZone: http://${eureka.instance.hostname}:${server.port}/eureka/
  server:
    enable-self-preservation: false
    eviction-interval-timer-in-ms: 5000
---      
spring:
  profiles: d1
  application:
    name: eureka-cluster
server:
  port: 9001
eureka:
  instance:
    hostname: eureka-server-c1
  client:
    registerWithEureka: false
    fetchRegistry: false
    serviceUrl:
      defaultZone: http://eureka-server-c2:9002/eureka/,http://eureka-server-c3:9003/eureka/
  server:
    enable-self-preservation: false
    eviction-interval-timer-in-ms: 5000
---      
spring:
  profiles: d2
  application:
    name: eureka-cluster
server:
  port: 9002
eureka:
  instance:
    hostname: eureka-server-c2
  client:
    registerWithEureka: false
    fetchRegistry: false
    serviceUrl:
      defaultZone: http://eureka-server-c1:9001/eureka/,http://eureka-server-c3:9003/eureka/
  server:
    enable-self-preservation: false
    eviction-interval-timer-in-ms: 5000
---      
spring:
  profiles: d3
  application:
    name: eureka-cluster
server:
  port: 9003
eureka:
  instance:
    hostname: eureka-server-c3
  client:
    registerWithEureka: false
    fetchRegistry: false
    serviceUrl:
      defaultZone: http://eureka-server-c1:9001/eureka/,http://eureka-server-c2:9002/eureka/
  server:
    enable-self-preservation: false
    eviction-interval-timer-in-ms: 5000
备注：集群模式说明
# 1)Defined profiles,aplication name,hostname
# 2)update registerWithEureka and fetchRegistry are equals true
# 3)update defaultZone value is cluster another node values
# 4)start service add environment:-Dspring.profiles.active=united-states. example:
#   java -jar -Dspring.profiles.active=united-states SpringCloudServiceRegistrationEurekaServer.jar
# 5)update os hosts file, adding ip and hostname mapping

配置：Eurake server


映射：Eurake server集群节点配置IP与主机名映射（启动前），修改hosts文件，hosts文件位置，win10：C:\Windows\System32\drivers\etc； linux：/etc/hosts
如：
192.168.1.203 eureka-server
192.168.1.200 eureka-server-c1
192.168.1.201 eureka-server-c2
192.168.1.202 eureka-server-c3

启动：集群模式下application.yml（全局配置）不能直接启动服务，启动时需要指定环境变量参数spring.profiles.active，参数值为application.yml中定义的名称（唯一），如启动节点1。
java -jar -Dspring.profiles.active=d1 SpringCloudServiceRegistrationEurekaServer.jar

 
默认情况以测试环境启动，可以设置生产环境启动，配置方式如下：
1）Classpath路径下定义eureka-server-{test|prod}.properties
2）无需定义属性方件，启动时配置JAVA环境变量eureka.environment
java -jar -Dspring.profiles.active=d1 -Deureka.environment=prod SpringCloudServiceRegistrationEurekaServer.jar

启动3个实例集群，如下图所示：
 

访问：
http://eureka-server-c1:9001
或
http://eureka-server-c2:9002 
或
http://eureka-server-c3:9003
 


3．3 服务提供
3．3．1 POM&CODE
Maven项目构建pom.xml
<?xml version="1.0"?>
<project xsi:schemaLocation="http://maven.apache.org/POM/4.0.0 http://maven.apache.org/xsd/maven-4.0.0.xsd" xmlns="http://maven.apache.org/POM/4.0.0"
    xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance">
  <modelVersion>4.0.0</modelVersion>
  <groupId>eureka-provider</groupId>
  <artifactId>spring-eureka-provider</artifactId>
  <version>1.0.1-SNAPSHOT</version>
  
  <parent>
    <groupId>cloud</groupId>
    <artifactId>spring-cloud-pro</artifactId>
    <version>0.0.1-SNAPSHOT</version>
  </parent>
  
  <name>spring-eureka-provider</name>

  <properties>
    <project.build.sourceEncoding>UTF-8</project.build.sourceEncoding>
  </properties>
  
  <dependencies>
    <!--  Spring eureka client -->
    <dependency>
       <groupId>org.springframework.cloud</groupId>
       <artifactId>spring-cloud-starter-eureka</artifactId>
</dependency>
<!--  Start EndPoint by default-->
<dependency>
       <groupId>org.springframework.boot</groupId>
       <artifactId>spring-boot-actuator</artifactId>
</dependency>
<!--  Spring cloud config client -->
   	<dependency>
		<groupId>org.springframework.cloud</groupId>
		<artifactId>spring-cloud-starter-config</artifactId>
	</dependency>
	<dependency>
		<groupId>org.springframework.cloud</groupId>
		<artifactId>spring-cloud-config-client</artifactId>
	</dependency>
  </dependencies>
  
  <build>
      <plugins>
          <!--  Spring boot package tools -->
          <plugin>
              <groupId>org.springframework.boot</groupId>
              <artifactId>spring-boot-maven-plugin</artifactId>
          </plugin>
      </plugins>
  </build>
</project>


Application.yml，定义服务名称（spring.appliction.name）、服务注注册中心（eureka.client.serviceUrl.defaultZone）

spring:
  application:
    name: consumer-service
eureka:
  client:
    serviceUrl:
      defaultZone: http://eureka-server-c1:9001/eureka
server:
  port: 8899

主类：EurekaClientApplication增加注解@EnableDiscoveryClient或@EnableEurekaClient用于服务提供注册、@SpringBootApplication

package org.spring.coud.eureka.client;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

@EnableDiscoveryClient
@SpringBootApplication
public class EurekaProviderApplication {
	
	public static void main(String[] args) {
		
		SpringApplication.run(EurekaProviderApplication.class, args);
        
    }
}

控制层数据接口类：Restful接口实例，获取注册服务实例列表，通过注入DiscoveryClient实例获取。

package org.spring.coud.eureka.provider;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ServiceInstanceRestController {
	
	@Autowired
    private DiscoveryClient discoveryClient;
 
    @RequestMapping(value = "/", method = RequestMethod.GET)
    public String home() {
        return "<a href='showAllServiceIds'>Show All Service Ids</a>";
    }
 
    @RequestMapping(value = "/showAllServiceIds", method = RequestMethod.GET)
    public String showAllServiceIds() {
 
        List<String> serviceIds = this.discoveryClient.getServices();
 
        if (serviceIds == null || serviceIds.isEmpty()) {
            return "No services found!";
        }
        String html = "<h3>Service Ids:</h3>";
        for (String serviceId : serviceIds) {
            html += "<br><a href='showService?serviceId=" + serviceId + "'>" + serviceId + "</a>";
        }
        return html;
    }
 
    @RequestMapping(value = "/showService", method = RequestMethod.GET)
    public String showFirstService(@RequestParam(defaultValue = "") String serviceId) {
 
        // (Need!!) eureka.client.fetchRegistry=true
        List<ServiceInstance> instances = this.discoveryClient.getInstances(serviceId);
 
        if (instances == null || instances.isEmpty()) {
            return "No instances for service: " + serviceId;
        }
        String html = "<h2>Instances for Service Id: " + serviceId + "</h2>";
 
        for (ServiceInstance serviceInstance : instances) {
            html += "<h3>Instance: " + serviceInstance.getUri() + "</h3>";
            html += "Host: " + serviceInstance.getHost() + "<br>";
            html += "Port: " + serviceInstance.getPort() + "<br>";
        }
 
        return html;
    }
 
    // A REST method, To call from another service.
    // See in Lesson "Load Balancing with Ribbon".
    @RequestMapping(value = "/hello", method = RequestMethod.GET)
    public String hello() {
 
        return "<html>Hello from ABC-SERVICE</html>";
    }
}

提供服务消费者调用接口，接口以Restful风格开放。

package org.spring.coud.eureka.provider.restapi;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
/**
 * @author oyyl
 */
@RestController
@RequestMapping("/api/rest/provider/test")
public class ResourcesRestController {
	@RequestMapping("/response")
	public String readMsg(){
		
		return "Request restfull resources success!";
	}
}




3．3．2 Application.yml
同一服务可注册多个，原则上采取：一主多从
spring.application.name：定义服务名称（全局），注册成功后，自动发现服务端可以查看服务名称。
Eureka.instance.appname：定义服务里一个实例别名，同一类服务建议统一相同名称，如下述定义的“ABC-SERVICE“。
defaultZone：定义自动发现服务端Url地址（多个可随机选取）。
spring.profiles：定义从节点随机不重复别名。
Eureka. healthcheck. Enabled：true 启用健康检查
// 生命有效期
lease-renewal-interval-in-seconds : 10 租期有效期

lease-expiration-duration-in-seconds: 30 租期到期
---
# Items that apply to ALL profiles:  
spring:
  application:
    name: ABC-SERVICE # ==> This is Service-Id  
eureka:
  instance:
    appname: ABC-SERVICE  # ==> This is a instance of ABC-SERVICE
    lease-renewal-interval-in-seconds: 10
    lease-expiration-duration-in-seconds: 30
  client:
    fetchRegistry: true
    serviceUrl:
      defaultZone: http://eureka-server-c1:9001/eureka
    healthcheck:
      enabled: true
server:
  port: 8000
---
spring:
  profiles: abc-service-replica01
eureka:
  instance:
appname: ABC-SERVICE  # ==> This is a instance of ABC-SERVICE
    lease-renewal-interval-in-seconds: 10
    lease-expiration-duration-in-seconds: 30
  client:
    fetchRegistry: true
    serviceUrl:
      defaultZone: http://eureka-server-c1:9001/eureka
    healthcheck:
      enabled: true
server:
  port: 8001
---
spring:
  profiles: abc-service-replica02
eureka:
  instance:
appname: ABC-SERVICE  # ==> This is a instance of ABC-SERVICE
    lease-renewal-interval-in-seconds: 10
    lease-expiration-duration-in-seconds: 30
  client:
    fetchRegistry: true
    serviceUrl:
      defaultZone: http://eureka-server-c1:9001/eureka
    healthcheck:
      enabled: true
server:
  port: 8002
---
spring:
  profiles: abc-service-replica03
eureka:
  instance:
appname: ABC-SERVICE  # ==> This is a instance of ABC-SERVICE
    lease-renewal-interval-in-seconds: 10
    lease-expiration-duration-in-seconds: 30
  client:
    fetchRegistry: true
    serviceUrl:
      defaultZone: http://eureka-server-c1:9001/eureka
    healthcheck:
      enabled: true
server:
  port: 8003


3．3．3 服务启动
项目通过打成可执行Jar包以后，通过java -jar 命令启动。与服务注册中启动方式类似，不再具体描述。下图是启动后的效果：
 

3．4 服务消费
	针对Eureka的服务提供者与服务消费者注解配置都是一样，唯一的区别是application.yml配置，实际开发中服务提供与服务消费都是同一应用程序，本文档为描述两者区别，明显拆分两个应用程序。
RestTemplate 整合了 Eureka Client，为我们提供了很多便利，我们所需要做的就是在 Spring 中注册一个 RestTemplate，通过可以使用 RestTemplate调用服务；但是如果需要通过服务名称并进行负载，需要引用Ribbon，如调用“ABC-SERVICE”
 
请求：http://ABC-SERVICE/api/rest/provider/test/reponse，会出现以下错误，服务名无法解析到主机。需要Ribbon从注册中心根据服务名称解析主机并提供负载均衡。
I/O error on GET request for "http://ABC-SERVICE/api/rest/test/provider/response": ABC-SERVICE; nested exception is java.net.UnknownHostException: ABC-SERVICE

3．4．1 POM&CODE
消费者Maven引入ribbon，解决自动从注册中心解析服务名称及负载均衡。
<?xml version="1.0"?>
<project xsi:schemaLocation="http://maven.apache.org/POM/4.0.0 http://maven.apache.org/xsd/maven-4.0.0.xsd" xmlns="http://maven.apache.org/POM/4.0.0"
    xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance">
  <modelVersion>4.0.0</modelVersion>
  <groupId>eureka-consumer</groupId>
  <artifactId>spring-eureka-consumer</artifactId>
  <version>1.0.1-SNAPSHOT</version>
  
  <name>spring-eureka-consumer</name>
  
  <parent>
    <groupId>cloud</groupId>
    <artifactId>spring-cloud-pro</artifactId>
    <version>0.0.1-SNAPSHOT</version>
  </parent>

  <properties>
    <project.build.sourceEncoding>UTF-8</project.build.sourceEncoding>
  </properties>
  
  <dependencies>
    <dependency>
       <groupId>org.springframework.cloud</groupId>
       <artifactId>spring-cloud-starter-eureka</artifactId>
</dependency>
<!-- 客户端负载 -->
<dependency>
       <groupId>org.springframework.cloud</groupId>
       <artifactId>spring-cloud-starter-ribbon</artifactId>
    </dependency>
    <dependency>
		<groupId>org.springframework.boot</groupId>
		<artifactId>spring-boot-actuator</artifactId>
	</dependency>
    <dependency>
		<groupId>org.springframework.cloud</groupId>
		<artifactId>spring-cloud-starter-config</artifactId>
	</dependency>
	<dependency>
		<groupId>org.springframework.cloud</groupId>
		<artifactId>spring-cloud-config-client</artifactId>
	</dependency>
  </dependencies>
  <build>
      <plugins>
          <plugin>
              <groupId>org.springframework.boot</groupId>
              <artifactId>spring-boot-maven-plugin</artifactId>
          </plugin>
      </plugins>
  </build>
</project>

启动类：EurekaConsumerApplication，服务提供与服务消费添加一样的注解：@EnableDiscoveryClient、@SpringBootApplication。
	实例化RestTemplate注入Spring容器，增加注解@Bean，其它服务层、控制层可直接注入使用；RestTemplate增加注解@LoadBalanced，提供服务名称解析与负载均衡。
package org.spring.eureka.consumer;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.context.annotation.Bean;
import org.springframework.web.client.RestTemplate;

/**
 * Service consumer
 * 服务消费
 * @author oyyl
 *
 */
@EnableDiscoveryClient
@SpringBootApplication
public class EurekaConsumerApplication {
	@Bean
	@LoadBalanced
	public RestTemplate rest(RestTemplateBuilder builder) {
		return builder.build();
	}
	public static void main(String[] args) {
		SpringApplication.run(EurekaConsumerApplication.class, args);
    }
}


控制层数据接口类：ConsumerRestController，提供Restful接口，调用服务层TestService类。
package org.spring.eureka.consumer.restapi;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/rest/consumer/test")
public class ConsumerRestController {
	
	@Autowired
	private TestService testService;
	
	@RequestMapping("/response")
	public String read(){
		return testService.read();
	}
}

服务层类：TestService，负责调用服务提供的接口，调用地址采用：服务名称 + 请求路径（RestTemplate + Ribbon解决服务名称与客户端负载均衡）。
ABC-SERVICE：为服务提供者在application.yml中的spring.application.name配置项的值（或者访问Eureka web ui）
package org.spring.eureka.consumer.restapi;
import java.net.URI;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class TestService {

	@Autowired
	private RestTemplate restTemplate;
	
	public String read() {
		URI uri = URI.create("http://ABC-SERVICE/api/rest/provider/test/response");
		return this.restTemplate.getForObject(uri, String.class);
	}
}

 
图1.服务名称
请求访问成功
 







3．4．2 Feign（申明式调用）
Feign是一种声明式、模板化的HTTP客户端，类似本地化调用。



四、Spring Hystrix（断路器）
________________________________________
Hystrix，是Netflix的一个开源熔断器，通过Hystrix我们可以很方便实现资源隔离、限流、超时设计、服务降级等服务容灾措施，并且还提供了强大的监控，较低级别的服务中的服务故障可能导致用户级联故障。当对特定服务的请求达到一定阈值时（Hystrix中的默认值为5秒内的20次故障），电路打开，不进行连接。在错误和开路的情况下，开发人员可以提供后备。
 
图2.Hystrix回退防止级联故障

4．1 POM&CODE
在3.4的基础上，增加熔断机制。
<?xml version="1.0"?>
<project xsi:schemaLocation="http://maven.apache.org/POM/4.0.0 http://maven.apache.org/xsd/maven-4.0.0.xsd" xmlns="http://maven.apache.org/POM/4.0.0"
    xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance">
  <modelVersion>4.0.0</modelVersion>
  <groupId>hystrix-client</groupId>
  <artifactId>spring-hystrix-client</artifactId>
  <version>1.0.1-SNAPSHOT</version>
  
  <parent>
    <groupId>cloud</groupId>
    <artifactId>spring-cloud-pro</artifactId>
    <version>0.0.1-SNAPSHOT</version>
  </parent>
  
  <name>spring-hystrix-client</name>
  
  <properties>
    <project.build.sourceEncoding>UTF-8</project.build.sourceEncoding>
  </properties>
  
  <dependencies>
   <!-- 熔断 -->
    <dependency>
	  <groupId>org.springframework.cloud</groupId>
	  <artifactId>spring-cloud-starter-hystrix</artifactId>
	</dependency>
   <!-- 服务发现客户端 -->
	<dependency>
       <groupId>org.springframework.cloud</groupId>
       <artifactId>spring-cloud-starter-eureka</artifactId>
    </dependency>
	<!-- 客户端负载均衡与服务名称解析 -->
    <dependency>
       <groupId>org.springframework.cloud</groupId>
       <artifactId>spring-cloud-starter-ribbon</artifactId>
    </dependency>
	<dependency>
        <groupId>org.springframework.boot</groupId>
        <artifactId>spring-boot-starter-actuator</artifactId>
    </dependency>
	<dependency>
		<groupId>org.springframework.cloud</groupId>
		<artifactId>spring-cloud-starter-config</artifactId>
	</dependency>
	<dependency>
		<groupId>org.springframework.cloud</groupId>
		<artifactId>spring-cloud-config-client</artifactId>
	</dependency>	
  </dependencies>
  <build>
      <plugins>
          <plugin>
              <groupId>org.springframework.boot</groupId>
              <artifactId>spring-boot-maven-plugin</artifactId>
          </plugin>
      </plugins>
  </build>
</project>

启动类：HystrixClientApplication
@EnableCircuitBreaker：启动熔断机制（服务降级）
@EnableDiscoveryClient：结全RestTemplate、ribbon服务发现、服务名解析、负载均衡。
package org.spring.hystrix.client;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.cloud.client.circuitbreaker.EnableCircuitBreaker;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.context.annotation.Bean;
import org.springframework.web.client.RestTemplate;
/**
 * 断路器
 * @author oyyl
 * @since 2018/09/26
 */
@SpringBootApplication
@EnableCircuitBreaker
@EnableDiscoveryClient
public class HystrixClientApplication {
	
	/**
	 * 1)@Bean用于实例化对象RestTemplate,在服务层及控制层使用
	 * 2)@RestTemplate用于服务名映射及负载均衡
	 * @param builder
	 * @return
	 */
	@Bean
	@LoadBalanced
	public RestTemplate rest(RestTemplateBuilder builder) {
		return builder.build();
	}
	
    public static void main( String[] args ) {
    	
        SpringApplication.run(HystrixClientApplication.class, args);
        
    }
}

控制层接口类
package org.spring.hystrix.client.restapi;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/rest/consumer/test")
public class ConsumerRestController {
	
	@Autowired
	private TestService testService;
	
	@RequestMapping("/response")
	public String read(){
		
		return testService.read();
	}

}

服务层类：
方法上增加注解@HystrixCommand，启动熔断处理。
package org.spring.hystrix.client.restapi;
import java.net.URI;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;
@Service
public class TestService {
	@Autowired
	private RestTemplate restTemplate;
	/**
	 * 基于注解增加熔断机制并服务降级处理
	 * 远程请求调用存在问题时,则使用本地方法reliable处理并返回
	 * @return
	 */
	@HystrixCommand(fallbackMethod = "reliable")
	public String read() {
		URI uri = URI.create("http://ABC-SERVICE/api/rest/provider/test/response");
		return this.restTemplate.getForObject(uri, String.class);
	}

	public String reliable() {
		return "Cloud Native Java (O'Reilly)";
	}
}


4．2 配置文件
主要配置为application.yml
spring:
  application:
    name: consumer-hystrix-service
eureka:
  client:
    serviceUrl:
      defaultZone: http://eureka-server-c1:9001/eureka
server:
  port: 8898

4．3 熔断测试
1）启动服务注册中心
2）启动服务提供（一主一从）
3）启动带有熔断机的服务消费
启动后的效果如下：
ABC-SERVICE：服务提供
CONSUMER-HYSTIX-SERVICE：服务消费（带熔断）
EUREKA-SERVICE：服务自动发现集群
 
图1.服务启动状态
访问
http://localhost:8898/api/rest/consumer/test/response

 
图2.请求结果
停掉其中1个服务提供，再次访问，出现一次服务降级返回情况：
 
图2.请求

4．4 Hystrix dashboard（控制面板）
Hystrix dashboard：断路器控制面板
基于可视图表实时或定时监控及展示单实例Eureka 断路器请求流量、断路器开关、出错比例、服务降级等情况。

4．4．1 POM&CODE
项目中包含Hystrix仪表板，Pom.xml 增加spring-cloud-starter-hystrix-dashboard：
<?xml version="1.0"?>
<project xsi:schemaLocation="http://maven.apache.org/POM/4.0.0 http://maven.apache.org/xsd/maven-4.0.0.xsd" xmlns="http://maven.apache.org/POM/4.0.0"
    xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance">
  <modelVersion>4.0.0</modelVersion>
  <groupId>hystrix-client</groupId>
  <artifactId>spring-hystrix-client</artifactId>
  <version>1.0.1-SNAPSHOT</version>
  
  <parent>
    <groupId>cloud</groupId>
    <artifactId>spring-cloud-pro</artifactId>
    <version>0.0.1-SNAPSHOT</version>
  </parent>
  
  <name>spring-hystrix-client</name>
  
  <properties>
    <project.build.sourceEncoding>UTF-8</project.build.sourceEncoding>
  </properties>
  
  <dependencies>
    <!-- 断路器 -->
    <dependency>
	  <groupId>org.springframework.cloud</groupId>
	  <artifactId>spring-cloud-starter-hystrix</artifactId>
	</dependency>
	<!-- 断路面板 -->
	<dependency>
	  <groupId>org.springframework.cloud</groupId>
	  <artifactId>spring-cloud-starter-hystrix-dashboard</artifactId>
	</dependency>
	<!-- 自动发现客户端 -->
	<dependency>
       <groupId>org.springframework.cloud</groupId>
       <artifactId>spring-cloud-starter-eureka</artifactId>
    </dependency>
	<!-- 客户端负载均衡与服务名称解析 -->
    <dependency>
       <groupId>org.springframework.cloud</groupId>
       <artifactId>spring-cloud-starter-ribbon</artifactId>
    </dependency>
	<dependency>
        <groupId>org.springframework.boot</groupId>
        <artifactId>spring-boot-starter-actuator</artifactId>
    </dependency>
	<dependency>
		<groupId>org.springframework.cloud</groupId>
		<artifactId>spring-cloud-starter-config</artifactId>
	</dependency>
	<dependency>
		<groupId>org.springframework.cloud</groupId>
		<artifactId>spring-cloud-config-client</artifactId>
	</dependency>	
  </dependencies>
  <build>
      <plugins>
          <plugin>
              <groupId>org.springframework.boot</groupId>
              <artifactId>spring-boot-maven-plugin</artifactId>
          </plugin>
      </plugins>
  </build>
</project>

主类：
增加注解@EnableHystrixDashboard，启动面板
package org.spring.hystrix.client;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.cloud.client.circuitbreaker.EnableCircuitBreaker;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.cloud.netflix.hystrix.dashboard.EnableHystrixDashboard;
import org.springframework.context.annotation.Bean;
import org.springframework.web.client.RestTemplate;
/**
 * 断路器
 * @author oyyl
 * @since 2018/09/26
 */
@SpringBootApplication
@EnableCircuitBreaker
@EnableDiscoveryClient
@EnableHystrixDashboard
public class HystrixClientApplication {
	
	/**
	 * 1)@Bean用于实例化对象RestTemplate,在服务层及控制层使用
	 * 2)@LoadBalanced用于服务名映射及负载均衡
	 * @param builder
	 * @return
	 */
	@Bean
	@LoadBalanced
	public RestTemplate rest(RestTemplateBuilder builder) {
		return builder.build();
	}
	
    public static void main( String[] args ) {
    	
        SpringApplication.run(HystrixClientApplication.class, args);
        
    }
}

Web UI访问： http://localhost:8898/hystrix
 
数据接口访问：http://localhost:8898/hystrix.stream
 

4．4．1 可视化面板
http://localhost:8898/hystrix.stream
 
图1.主控制面板
查看单点单实例：
 
图2.监控指标

4．5 Turbine
监控多实例Eureka熔段情况
























































六、Ribbon & Feign
________________________________________
6．1 Ribbon
客户端负载
6．1．1 POM
<dependency>
       <groupId>org.springframework.cloud</groupId>
       <artifactId>spring-cloud-starter-ribbon</artifactId>
    </dependency>

Spring Cloud使用RibbonClientConfiguration为每个“命名的客户端“根据需要创建一个新的ApplicationContext。这包含（除其他外）ILoadBalancer，RestClient和ServerListFilter。
6．1．2 Ribbon客户端定义
Spring Boot配置文件，定义外部属性来配置Ribbon客户端，格式如下：
<client>.ribbon.* 

假设定义hello的client，则application.yml定义如下，Ribbon一般情况通过从服务注册中心获取服务（提供服务）列表，此例ribbon.eureka.enabled=false禁用Eureka，而是通过listOfServers（静态服务列表） 、ServerListRefreshInterval属性值，间隔获取服务列表。hello为client名称。
hello:
  ribbon:
    eureka:
      enabled: false
listOfServers: localhost:8090,localhost:9092,localhost:9999
ServerListRefreshInterval: 15000
可选项属性key配置参考CommonClientConfigKey，默认配置定义在DefaultClientConfigImpl（RibbonClientConfiguration引用）。

Spring Cloud还允许您通过使用@RibbonClient声明其他配置（位于RibbonClientConfiguration之上）来完全控制客户端
@Configuration
@RibbonClient(name = "hello", configuration = HelloConfiguration.class)
public class TestConfiguration {
}
HelloConfiguration类定义：
@Configuration
public class HelloConfiguration {
    @Bean
    public IPing ribbonPing(IClientConfig config) {
        return new PingUrl();
    }
}
在这种情况下，客户端由RibbonClientConfiguration中已经存在的配置与HelloConfiguration中的定义的配置组成（后者通常会覆盖前者）。


Ribbon根据client名称创建ApplicationContext及一系列组件Bean，组件Bean如下所示：
1）IClientConfig, which stores client configuration for a client or load balancer,
2）an ILoadBalancer, which represents a software load balancer,
3）a ServerList, which defines how to get a list of servers to choose from,
4）an IRule, which describes a load balancing strategy, and
5）an IPing, which says how periodic pings of a server are performed.

Spring Cloud Netflix默认情况下为Ribbon（BeanType beanName：ClassName）提供以下bean：
1）IClientConfig ribbonClientConfig：DefaultClientConfigImpl
2）IRule ribbonRule：ZoneAvoidanceRule
3）IPing ribbonPing：NoOpPing
4）ServerList<Server> ribbonServerList：ConfigurationBasedServerList
5）ServerListFilter<Server> ribbonServerListFilter：ZonePreferenceServerListFilter
6）ILoadBalancer ribbonLoadBalancer：ZoneAwareLoadBalancer
7）ServerListUpdater ribbonServerListUpdater：PollingServerListUpdater



6．2 Feign
Feign是一个声明式的Web服务客户端。这使得Web服务客户端的写入更加方便 要使用Feign创建一个界面并对其进行注释，它具有可插入注释支持，包括Feign注释和JAX-RS注释。Feign还支持可插拔编码器和解码器。Spring Cloud增加了对Spring MVC注释的支持，并使用Spring Web中默认使用的HttpMessageConverters。Spring Cloud集成Ribbon和Eureka以在使用Feign时提供负载均衡的http客户端。Feign用添加@FeignClient已经使用Ribbon。
引用：
<dependency>
       <groupId>org.springframework.cloud</groupId>
       <artifactId>spring-cloud-starter-feign</artifactId>
    </dependency>

主类启用feign：增加注解@EnableFeignClients
@Configuration
@ComponentScan
@EnableAutoConfiguration
@EnableEurekaClient
@EnableFeignClients
public class Application {
    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }
}

调用用申请类：
@FeignClient("hello")
public interface StoreClient {
    @RequestMapping(method = RequestMethod.GET, value = "/stores")
    List<Store> getStores();
    @RequestMapping(method = RequestMethod.POST, value = "/stores/{storeId}", consumes = "application/json")
    Store update(@PathVariable("storeId") Long storeId, Store store);
}









七、Zuuls
________________________________________
Zuul是Netflix的基于JVM的路由器和服务器端负载均衡器，作为微服务体系结构统一路由网关。Zuul的规则引擎允许基本上写任何JVM语言编写规则和过滤器，内置Java和Groovy。
【注意】下述Spring Cloud版本测试简单的URL路由时，报错“MIME IS NOT NULL”。
<dependency>
	<groupId>org.springframework.cloud</groupId>
	<artifactId>spring-cloud-dependencies</artifactId>
	<version> Brixton.RELEASE</version>
	<type>pom</type>
	<scope>import</scope>
</dependency>
解决办法：升级Spring Cloud为Dalston.RELEASE（spring-cloud-netflix-dependencies版本需要1.2.6及以上）。
<dependency>
	<groupId>org.springframework.cloud</groupId>
	<artifactId>spring-cloud-dependencies</artifactId>
	<version>Dalston.RELEASE</version>
	<type>pom</type>
	<scope>import</scope>
</dependency>



7．1 路由配置（嵌入反向代理）
测试需要启动Eureka Server、Eureka Provider
7．1．1 静态路由—URL
path: 匹配url中带有t1的路径，可以自定义名称
url: path匹配中后转发至http://localhost:8000/ 并忽略path中定义的前缀，如请求：http://localhost:8888/t1/api/test，后端请求路由将转发至http://localhost:8000/api/test
application.yml片断如下：
spring:
  application:
    name: gateway-service
server:
  port: 8896
zuul:
  routes:
    abcserver:
      path: /t1/**
      url: http://localhost:8000/
简单的URL路由不会被执行为HystrixCommand（熔断），也不能使用Ribbon对多个URL进行负载平衡。为此，需要在服务提供接口端为serviceId配置Ribbon客户端。

Maven引入spring-cloud-starter-zuul依赖
<?xml version="1.0"?>
<project xsi:schemaLocation="http://maven.apache.org/POM/4.0.0 http://maven.apache.org/xsd/maven-4.0.0.xsd" xmlns="http://maven.apache.org/POM/4.0.0"
    xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance">
  <modelVersion>4.0.0</modelVersion>
  <groupId>zuul-server</groupId>
  <artifactId>spring-zuul-server</artifactId>
  <version>1.0.1-SNAPSHOT</version>
  
  <parent>
    <groupId>cloud</groupId>
    <artifactId>spring-cloud-pro</artifactId>
    <version>0.0.1-SNAPSHOT</version>
  </parent>
  
  <name>spring-zuul-server</name>
  
  <properties>
    <project.build.sourceEncoding>UTF-8</project.build.sourceEncoding>
  </properties>
  
  <dependencies>
	<dependency>
	  <groupId>org.springframework.cloud</groupId>
	  <artifactId>spring-cloud-starter-zuul</artifactId>
	</dependency>
	<dependency>
      <groupId>org.springframework.boot</groupId>
      <artifactId>spring-boot-starter-actuator</artifactId>
    </dependency>
    <dependency>
	    <groupId>ch.qos.logback</groupId>
	    <artifactId>logback-classic</artifactId>
	</dependency>	
  </dependencies>
  <build>
      <plugins>
          <plugin>
              <groupId>org.springframework.boot</groupId>
              <artifactId>spring-boot-maven-plugin</artifactId>
          </plugin>
      </plugins>
  </build>
</project>

启动类添加注解@EnableZuulProxy
package org.spring.zuul.server;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.zuul.EnableZuulProxy;

/**
 * 网关
 * @author oyyl
 * @since 2018/09/26
 */
@SpringBootApplication
@EnableZuulProxy
public class ZuulServerApplication {
	
    public static void main(String[] args) {
    	
        SpringApplication.run(ZuulServerApplication.class, args);
        
    }
}
7．1．2 静态路由—服务ID静态配置
Maven引入spring-cloud-starter-zuul依赖
Zuul启动器不包括发现客户端，因此对于基于“服务”的路由，则需POM引入Eureka自动发现客户端依赖。
<?xml version="1.0"?>
<project xsi:schemaLocation="http://maven.apache.org/POM/4.0.0 http://maven.apache.org/xsd/maven-4.0.0.xsd" xmlns="http://maven.apache.org/POM/4.0.0"
    xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance">
  <modelVersion>4.0.0</modelVersion>
  <groupId>zuul-server</groupId>
  <artifactId>spring-zuul-server</artifactId>
  <version>1.0.1-SNAPSHOT</version>
  
  <parent>
    <groupId>cloud</groupId>
    <artifactId>spring-cloud-pro</artifactId>
    <version>0.0.1-SNAPSHOT</version>
  </parent>
  
  <name>spring-zuul-server</name>
  
  <properties>
    <project.build.sourceEncoding>UTF-8</project.build.sourceEncoding>
  </properties>
  
  <dependencies>
	<dependency>
	  <groupId>org.springframework.cloud</groupId>
	  <artifactId>spring-cloud-starter-zuul</artifactId>
	</dependency>
	<!-- 自动发现客户端 -->
	<dependency>
       <groupId>org.springframework.cloud</groupId>
       <artifactId>spring-cloud-starter-eureka</artifactId>
    </dependency>
    <!-- 熔断 -->
    <dependency>
	  <groupId>org.springframework.cloud</groupId>
	  <artifactId>spring-cloud-starter-hystrix</artifactId>
	</dependency>
	<!-- 熔断监控面板 -->
	<dependency>
	  <groupId>org.springframework.cloud</groupId>
	  <artifactId>spring-cloud-starter-hystrix-dashboard</artifactId>
	</dependency>    
	<dependency>
      <groupId>org.springframework.boot</groupId>
      <artifactId>spring-boot-starter-actuator</artifactId>
    </dependency>
    <dependency>
	    <groupId>ch.qos.logback</groupId>
	    <artifactId>logback-classic</artifactId>
	</dependency>	
  </dependencies>
  <build>
      <plugins>
          <plugin>
              <groupId>org.springframework.boot</groupId>
              <artifactId>spring-boot-maven-plugin</artifactId>
          </plugin>
      </plugins>
  </build>
</project>

启动类添加注解@EnableZuulProxy
package org.spring.zuul.server;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.zuul.EnableZuulProxy;

/**
 * 网关
 * @author oyyl
 * @since 2018/09/26
 */
@SpringBootApplication
@EnableZuulProxy
public class ZuulServerApplication {
	
    public static void main(String[] args) {
    	
        SpringApplication.run(ZuulServerApplication.class, args);
        
    }
}

因为EnableZuulProxy注解类已经增加自动发现以及熔断注解，EnableZuulProxy源码如下：
 




Application.yml配置如下：
abc-service为服务名称；值为路径匹配前缀，可以自定义。
spring:
  application:
    name: gateway-service
server:
  port: 8896
eureka:
  client:
    serviceUrl:
      defaultZone: http://eureka-server-c1:9002/eureka
    healthcheck:
      enabled: true
zuul:
  routes:
    abc-service: /t1/**


对路由的更细粒度的控制可以采用下述配置（等同上述），可以指定路径和serviceId并配置eureka server url用于获取服务发现, application.yml配置如下：
spring:
  application:
    name: gateway-service
server:
  port: 8896
eureka:
  client:
    serviceUrl:
      defaultZone: http://eureka-server-c1:9002/eureka
    healthcheck:
      enabled: true
zuul:
  routes:
    abcserver:
      path: /t1/**
      #url: http://localhost:8000/
      serviceId: abc-service
对“/t1”的http请求转发到“abc-service”服务。路由必须有一个“路径”，可以指定为蚂蚁样式模式，所以“/t1/ *”只匹配一个级别，但“/t1/ **”分层匹配。



7．1．3 动态路由—自定类
PatternServiceRouteMapper源码如下,创建PatternServiceRouteMapper对象，根据正则表达式命名捕获组建立模板提取“服务名称”（服务与路由即路径映射关系）
package org.springframework.cloud.netflix.zuul.filters.discovery;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.springframework.util.StringUtils;

/**
 * @author Stéphane Leroy
 *
 * This service route mapper use Java 7 RegEx named group feature to rewrite a discovered
 * service Id into a route.
 *
 * Ex : If we want to map service Id <code>[rest-service-v1]</code> to
 * <code>/v1/rest-service/**</code> route service pattern :
 * <code>"(?<name>.*)-(?<version>v.*$)"</code> route pattern :
 * <code>"${version}/${name}"</code>
 *
 * This implementation uses <code>Matcher.replaceFirst</code> so only one match will be
 * replaced.
 */
public class PatternServiceRouteMapper implements ServiceRouteMapper {

	/**
	 * A RegExp Pattern that extract needed information from a service ID. Ex :
	 * "(?<name>.*)-(?<version>v.*$)"
	 */
	private Pattern servicePattern;
	/**
	 * A RegExp that refer to named groups define in servicePattern. Ex :
	 * "${version}/${name}"
	 */
	private String routePattern;

	public PatternServiceRouteMapper(String servicePattern, String routePattern) {
		this.servicePattern = Pattern.compile(servicePattern);
		this.routePattern = routePattern;
	}

	/**
	 * Use servicePattern to extract groups and routePattern to construct the route.
	 *
	 * If there is no matches, the serviceId is returned.
	 *
	 * @param serviceId service discovered name
	 * @return route path
	 */
	@Override
	public String apply(String serviceId) {
		Matcher matcher = this.servicePattern.matcher(serviceId);
		String route = matcher.replaceFirst(this.routePattern);
		route = cleanRoute(route);
		return (StringUtils.hasText(route) ? route : serviceId);
	}

	/**
	 * Route with regex and replace can be a bit messy when used with conditional named
	 * group. We clean here first and trailing '/' and remove multiple consecutive '/'
	 * @param route
	 * @return
	 */
	private String cleanRoute(final String route) {
		String routeToClean = route.replaceAll("/{2,}", "/");
		if (routeToClean.startsWith("/")) {
			routeToClean = routeToClean.substring(1);
		}
		if (routeToClean.endsWith("/")) {
			routeToClean = routeToClean.substring(0, routeToClean.length() - 1);
		}
		return routeToClean;
	}
}

自定义服务与路由射映如下：
@Bean
public PatternServiceRouteMapper serviceRouteMapper() {
    return new PatternServiceRouteMapper(
        "(?<name>^.+)-(?<version>v.+$)",
        "${version}/${name}");
}








7．2 路由配置（普通式）
如果运行不带代理的Zuul服务器，或者有选择地切换代理平台的部分，使用@EnableZuulServer（不是@ EnableZuulProxy）。任何ZuulFilter类型的bean应用程序的都将会自动安装。与@EnableZuulProxy一样，但不会自动添加任何代理过滤器。
@EnableZuulServer - 普通Zuul Server,只支持基本的route与filter功能.
@EnableZuulProxy - 普通Zuul Server+服务发现与熔断等功能的增强版,具有反向代理功能.
在这种情况下，仍然通过配置“zuul.routes.*”来指定进入Zuul服务器的路由，但没有服务发现和代理，所以“serviceId”和“url”设置将被忽略。例如：
zuul:
  routes:
    abc-service: /t1/**



























八、问题
________________________________________
http://itmuch.com/spring-cloud-sum-eureka/
6．1 Eureka进入了保护模式
EMERGENCY! EUREKA MAY BE INCORRECTLY CLAIMING INSTANCES ARE UP WHEN THEY'RE NOT. RENEWALS ARE LESSER THAN THRESHOLD AND HENCE THE INSTANCES ARE NOT BEING EXPIRED JUST TO BE SAFE.
Eureka Server能够迅速有效地踢出已关停的节点，但是新手由于Eureka自我保护模式，以及心跳周期长的原因，常常会遇到Eureka Server不踢出已关停的节点的问题。
6．2 Eureka registration failed Cannot execute request on any known server
服务注册中心、服务提供或服务消费出现服务无法注册警告或读超时，存在以下错误：
DiscoveryClient  registration failed Cannot execute request on any known server
检查服务注册中心中配置文件application.yml，修改为以下配置：
Eureka.client.registerWithEureka: false




























九、资源连接
________________________________________
Spring eureka
https://o7planning.org/en/11733/understanding-spring-cloud-eureka-server-with-example#a15504641
Spring hystrix:
https://spring.io/guides/gs/circuit-breaker/
Spring zuul
https://www.jianshu.com/p/24a3b67bbab9
https://cloud.spring.io/spring-cloud-netflix/multi/multi__router_and_filter_zuul.html
http://blog.didispace.com/springcloud5/


