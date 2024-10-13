package com.linuxacademy.ccdak.producer;

import java.util.Properties;

import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.Producer;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.clients.producer.RecordMetadata;

/**
 * @author will
 */
public class MemberSignupsProducer {

    Producer<Integer, String> producer;

    public MemberSignupsProducer() {
        Properties props = new Properties();
        props.put("bootstrap.servers", "zoo1:9092");
        props.put("key.serializer", "org.apache.kafka.common.serialization.IntegerSerializer");
        props.put("value.serializer", "org.apache.kafka.common.serialization.StringSerializer");

        // Recently, a Kafka broker failed and had to be restarted. Unfortunately, that broker was the leader for a partition of
        // the member_signups topic at the time, and a few records had been committed by the leader but had not yet been written
        // to the replicas. A small number of records were lost when the leader failed. Change the configuration so that
        // this does not happen again.
        props.put("acks", "all");

        // The producer is configured to retry the process of sending a record when it fails due to a transient error. However,
        // in a few instances this has caused records to be written to the topic log in a different order than the order
        // they were sent by the producer, because a record was retried while another record was sent ahead of it. A few
        // downstream consumers depend on certain ordering guarantees for this data. Ensure that retries by the producer do
        // not result in out-of-order records.
        props.put("max.in.flight.requests.per.connection", 1);

        // This producer sometimes experiences high throughput that could benefit from a greater degree of message batching.
        // Increase the batch size to 64 KB (65536 bytes).
        props.put("batch.size", 65536);

        producer = new KafkaProducer<>(props);
    }

    public void handleMemberSignup(Integer memberId, String name) {
        int partition;
        if (name.toUpperCase().charAt(0) <= 'M') {
            partition = 0;
        } else {
            partition = 1;
        }
        ProducerRecord record = new ProducerRecord<>("member_signups", partition, memberId, name.toUpperCase());
        producer.send(record, (RecordMetadata recordMetadata, Exception e) -> {
            if (e != null) {
                System.err.println(e.getMessage());
            } else {
                System.out.println("key=" + record.key() + ", value=" + record.value());
            }
        });
    }

    public void tearDown() {
        producer.close();
    }

}
