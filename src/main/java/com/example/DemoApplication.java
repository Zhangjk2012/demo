package com.example;

import com.example.spring.TestSingleton;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class DemoApplication {

    @Bean
    public TestSingleton create() {
        TestSingleton t = new TestSingleton();
        return t;
    }


	public static void main(String[] args) {
        ApplicationContext context = SpringApplication.run(DemoApplication.class, args);

        context.getBean("testScope");
        context.getBean("testScope");
        context.getBean("testScope");

	}
}
