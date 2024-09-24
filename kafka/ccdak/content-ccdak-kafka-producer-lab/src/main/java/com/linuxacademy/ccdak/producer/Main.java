package com.linuxacademy.ccdak.producer;

import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.Producer;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.common.serialization.StringSerializer;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Properties;
import java.util.stream.Stream;

public class Main {

    public static void main(String[] args) {
        Properties props = new Properties();
        props.put("bootstrap.servers", "localhost:9092");
        props.put("key.serializer", StringSerializer.class);
        props.put("value.serializer", StringSerializer.class);
        props.put("acks", "all");

        try (Producer<String, String> producer = new KafkaProducer<>(props)) {
            try (Stream<String> lines = Files.lines(Paths.get("src/main/resources/sample_transaction_log.txt"))) {
                lines
                        .map(line -> line.split(":"))
                        .forEach((line) -> {
                            String key = line[0];
                            String value = line[1];
                            producer.send(new ProducerRecord<>("inventory_purchases", key, value));
                            if (key.equals("apples")) {
                                producer.send(new ProducerRecord<>("apples", key, value));
                            }
                        });
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }
}
