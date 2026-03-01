package com.payload.producer.config;

import java.util.Properties;

import org.apache.kafka.clients.admin.AdminClient;
import org.apache.kafka.clients.admin.AdminClientConfig;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class AdminClientConfiguration {

	@Value("${spring.kafka.bootstrap-servers}")
	private String bootstrapServer;

	@Bean
	public AdminClient adminClient() {

		Properties prob = new Properties();
		prob.put(AdminClientConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapServer);
		return AdminClient.create(prob);
	}
}
