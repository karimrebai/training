package com.linuxacademy.ccdak.consumer;

import java.time.Duration;
import java.util.Arrays;
import java.util.Properties;

import org.apache.kafka.clients.consumer.Consumer;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;

/**
 * @author will
 */
public class MemberSignupsConsumer {

    Consumer<String, String> consumer;

    public MemberSignupsConsumer() {
        Properties props = new Properties();
        props.setProperty("bootstrap.servers", "zoo1:9092");
        props.setProperty("group.id", "group1");
        props.setProperty("enable.auto.commit", "false");
        props.setProperty("key.deserializer", "org.apache.kafka.common.serialization.StringDeserializer");
        props.setProperty("value.deserializer", "org.apache.kafka.common.serialization.StringDeserializer");

        // This consumer does not have a high need for real-time data since it is merely a logging utility that provides
        // data for later analysis. Increase the minimum fetch size to 1 K (1024 bytes) to allow the consumer to fetch
        // more data in a single request.
        props.setProperty("fetch.min.bytes", "1024");

        // Changes in consumer status (such as consumers joining or leaving the group) are not being detected quickly
        // enough. Configure the consumer to send a heartbeat every two seconds (2000ms).
        props.setProperty("heartbeat.interval.ms", "2000");

        // Last week, someone tried to run this consumer against a new cluster. The consumer failed with the following
        // error message:
        //   Exception in thread "main" org.apache.kafka.clients.consumer.NoOffsetForPartitionException:
        //   Undefined offset with no reset policy for partitions: [member_signups-0]
        // Ensure the consumer has an offset reset policy that will allow the consumer to read from the beginning of the
        // log when reading from a partition for the first time.
        props.setProperty("auto.offset.reset", "earliest");

        consumer = new KafkaConsumer<>(props);
        consumer.subscribe(Arrays.asList("member_signups"));
    }

    public void run() {

        while (true) {
            ConsumerRecords<String, String> records = consumer.poll(Duration.ofMillis(100));
            handleRecords(records);
        }

    }

    public void handleRecords(ConsumerRecords<String, String> records) {
        for (ConsumerRecord<String, String> record : records) {
            System.out.println("key=" + record.key() + ", value=" + record.value() + ", topic=" + record.topic()
                    + ", partition=" + record.partition() + ", offset=" + record.offset());
        }
        consumer.commitSync();
    }

}
