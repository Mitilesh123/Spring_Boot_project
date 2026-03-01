package com.payload.producer;

import java.util.Arrays;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.scheduling.annotation.EnableAsync;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@EnableAsync
@SpringBootApplication
public class PayloadProducerApplication {

	public static void main(String[] args) {
		ApplicationContext context = SpringApplication.run(PayloadProducerApplication.class, args);
		String[] beanNames = context.getBeanDefinitionNames();
		Arrays.sort(beanNames);
		for (String beanName : beanNames) {
			log.info(beanName.toString());
		}
	}
}
