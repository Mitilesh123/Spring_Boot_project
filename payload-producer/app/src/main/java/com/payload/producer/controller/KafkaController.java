package com.payload.producer.controller;

import java.time.Duration;
import java.time.LocalTime;
import java.util.concurrent.CompletableFuture;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.payload.producer.service.KafkaProducer;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
public class KafkaController {

	@Autowired
	private KafkaProducer kafkaProducer;

	@PostMapping(value = "/send")
	public CompletableFuture<ResponseEntity<String>> sendPayload(@RequestParam(defaultValue = "null") String key,
			@RequestParam(required = true) String topic, @RequestParam(defaultValue = "1") int count,
			@RequestBody String payload) {

		LocalTime startTime = LocalTime.now();

		log.info("Sending payload to topic: {}, key: {}, payload: {}", topic, key, payload);

		return kafkaProducer.sendPayload(topic, key, payload, count).thenApply(result -> {

			long millis = Duration.between(startTime, LocalTime.now()).toMillis();

			if (result) {
				log.info("Message sent to topic: {} in {} ms.", topic, millis);
				return ResponseEntity
						.ok(String.format("Message sent to topic \"%s\" %d times in %d ms.", topic, count, millis));
			} else {
				log.error("Failed to send message to topic: {}", topic);
				return ResponseEntity.status(HttpStatus.BAD_REQUEST)
						.body("Failed to send message to topic. Please verify that the topic exists and try again.");
			}
		});
	}
}
