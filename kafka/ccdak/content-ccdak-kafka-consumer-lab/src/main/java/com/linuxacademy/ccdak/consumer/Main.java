package com.linuxacademy.ccdak.consumer;

import org.apache.kafka.clients.consumer.Consumer;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.common.serialization.StringDeserializer;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.time.Duration;
import java.util.Collections;
import java.util.Properties;

public class Main {

    public static void main(String[] args) {
        Properties props = new Properties();
        props.put("bootstrap.servers", "localhost:9092");
        props.put("group.id", "yo");
        props.put("enable.auto.commit", "false");
        props.put("key.deserializer", StringDeserializer.class);
        props.put("value.deserializer", StringDeserializer.class);

        try (Consumer<String, String> consumer = new KafkaConsumer<>(props)) {
            consumer.subscribe(Collections.singletonList("inventory_purchases"));
            try (BufferedWriter writer = new BufferedWriter(new FileWriter("output/output.dat", true))) {
                while (true) {
                    for (ConsumerRecord<String, String> record : consumer.poll(Duration.ofMillis(100))) {
                        writer.write("key=" + record.key() + ", value=" + record.value()
                                + ", topic=" + record.topic() + ", partition=" + record.partition()
                                + ", offset=" + record.offset());
                        writer.newLine();
                    }
                    writer.flush();
                    consumer.commitSync();
                }
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }
    }

}
