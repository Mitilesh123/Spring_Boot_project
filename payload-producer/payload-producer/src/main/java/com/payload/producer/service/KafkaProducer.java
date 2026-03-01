package com.payload.producer.service;

import java.util.Set;
import java.util.concurrent.CompletableFuture;

import org.apache.kafka.clients.admin.AdminClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

@Service
public class KafkaProducer {

	@Autowired
	private KafkaTemplate<String, String> kafkaTemplate;

	@Autowired
	private AdminClient adminClient;

	@Async
	public CompletableFuture<Boolean> topicExists(String topicName) {
		return CompletableFuture.supplyAsync(() -> {
			try {
				Set<String> topics = adminClient.listTopics().names().get();
				return topics.contains(topicName);
			} catch (Exception e) {
				return false;
			}
		});
	}

	@Async
	public CompletableFuture<Boolean> sendPayload(String topic, String key, String payload, int count) {
		return topicExists(topic).thenCompose(exists -> {
			if (exists) {
				for (int i = 0; i < count; i++) {
					kafkaTemplate.send(topic, key, payload);
				}
				return CompletableFuture.completedFuture(true);
			} else {
				return CompletableFuture.completedFuture(false);
			}
		});
	}
}
