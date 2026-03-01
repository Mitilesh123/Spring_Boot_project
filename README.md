# Payload Producer

A Spring Boot application for producing and sending payloads to Apache Kafka topics. This service provides REST endpoints to publish messages to Kafka with support for batching, compression, and large payload handling.

## Table of Contents

- [Features](#features)
- [Prerequisites](#prerequisites)
- [Project Structure](#project-structure)
- [Setup & Installation](#setup--installation)
- [Running the Application](#running-the-application)
- [API Endpoints](#api-endpoints)
- [Configuration](#configuration)
- [Technologies Used](#technologies-used)

## Features

- **REST API for Kafka Production**: Send messages to Kafka topics via HTTP endpoints
- **Batch Message Support**: Send multiple copies of the same message in one request
- **Large Payload Support**: Configured to handle payloads up to 200 MB
- **Compression**: GZIP compression enabled for efficient message transmission
- **Asynchronous Processing**: Non-blocking message sending with `CompletableFuture`
- **Admin Client Configuration**: Preconfigured Kafka admin capabilities
- **Comprehensive Logging**: SLF4J logging with execution timing metrics

## Prerequisites

- **Java 17** or higher
- **Apache Kafka** (running and accessible)
- **Maven 3.6+** (or use the included Maven wrapper)
- **Kafka Bootstrap Server**: Default is `localhost:9092` (configurable)

## Project Structure

```
payload-producer/
├── src/
│   ├── main/
│   │   ├── java/com/payload/producer/
│   │   │   ├── PayloadProducerApplication.java     # Main Spring Boot application
│   │   │   ├── config/
│   │   │   │   └── AdminClientConfiguration.java   # Kafka admin client config
│   │   │   ├── controller/
│   │   │   │   └── KafkaController.java            # REST API endpoints
│   │   │   └── service/
│   │   │       └── KafkaProducer.java              # Kafka producer logic
│   │   └── resources/
│   │       └── application.yaml                     # Application 
├── pom.xml                                           # Maven project configuration
└── README.md                                         # This file
```

## Setup & Installation

### 1. Start Kafka

Ensure Kafka is running on `localhost:9092` (or update the configuration accordingly):

```bash
# Start Zookeeper (if needed)
bin/zookeeper-server-start.sh config/zookeeper.properties

# Start Kafka
bin/kafka-server-start.sh config/server.properties
```

### 2. Clone and Navigate to Project

```bash
cd payload-producer
```

### 3. Build the Project

Using Maven wrapper:

```bash
./mvnw clean install
```

Or with Maven:

```bash
mvn clean install
```

## Running the Application

### Using Maven

```bash
./mvnw spring-boot:run
```

Or:

```bash
mvn spring-boot:run
```

### Using Java

After building:

```bash
java -jar target/payload-producer-0.0.1.jar
```

The application will start and listen on the default Spring Boot port (`8080`).

## API Endpoints

### POST `/send`

**Description**: Send a payload to a Kafka topic.

**Parameters**:
| Name | Type | Required | Default | Description |
|------|------|----------|---------|-------------|
| `key` | String | No | `"null"` | Message key (for partitioning) |
| `topic` | String | Yes | - | Kafka topic name |
| `count` | Integer | No | `1` | Number of times to send the message |
| `payload` | String (Body) | Yes | - | Message content (up to 200 MB) |

**Request Example**:

```bash
curl -X POST http://localhost:8080/send \
  -H "Content-Type: text/plain" \
  -d '{"data": "your payload here"}' \
  --data-urlencode "key=myKey" \
  --data-urlencode "topic=my-topic" \
  --data-urlencode "count=1"
```

**Response** (Success - 200 OK):

```json
Message sent to topic "my-topic" 1 times in 125 ms.
```

**Response** (Failure - 400 Bad Request):

```json
Failed to send message to topic. Please verify that the topic exists and try again.
```

## Configuration

Configuration is managed via [application.yaml](payload-producer/payload-producer/src/main/resources/application.yaml):

```yaml
spring:
  application:
    name: payload-producer

  mvc:
    async:
      request-timeout: 1800000

  kafka:
    bootstrap-servers: localhost:9092 # Kafka broker address

    producer:
      key-serializer: org.apache.kafka.common.serialization.StringSerializer
      value-serializer: org.apache.kafka.common.serialization.StringSerializer
      compression-type: gzip # Enable compression
      buffer-memory: 262144000 # 250 MB buffer

      properties:
        max.request.size: 209715200 # Max message size: 200 MB
```

### Key Configuration Options

- **`bootstrap-servers`**: Kafka broker address (change if Kafka is on a different host/port)
- **`request-timeout`**: HTTP request timeout in milliseconds (30 minutes)
- **`compression-type`**: Message compression (gzip, none, etc.)
- **`buffer-memory`**: Producer buffer size (250 MB)
- **`max.request.size`**: Maximum message size (200 MB)

## Technologies Used

- **Spring Boot**: 4.0.3
- **Java**: 17
- **Apache Kafka**: For message streaming
- **Lombok**: To reduce boilerplate code
- **Maven**: Build and dependency management
- **SLF4J**: Logging framework