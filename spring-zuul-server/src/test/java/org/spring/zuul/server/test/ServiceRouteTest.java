package org.spring.zuul.server.test;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.spring.zuul.server.ZuulServerApplication;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.cloud.netflix.zuul.filters.discovery.PatternServiceRouteMapper;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes=ZuulServerApplication.class)
public class ServiceRouteTest {
	
	@Test
	public void testServicePattern(){
		PatternServiceRouteMapper ps = new PatternServiceRouteMapper(
		        "(?<name>^.+)-(?<version>v.+$)",
		        "${version}/${name}");
		System.out.println(ps.apply("rest-abc-v1"));
	}
	
}
