package com.linuxacademy.ccdak.consumer;

import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.MockConsumer;
import org.apache.kafka.clients.consumer.OffsetResetStrategy;
import org.apache.kafka.common.TopicPartition;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MemberSignupsConsumerTest {

    MockConsumer<Integer, String> mockConsumer;
    MemberSignupsConsumer memberSignupsConsumer;

    // Contains data sent so System.out during the test.
    private ByteArrayOutputStream systemOutContent;
    private final PrintStream originalSystemOut = System.out;

    @Before
    public void setUp() {
        mockConsumer = new MockConsumer<>(OffsetResetStrategy.EARLIEST);
        memberSignupsConsumer = new MemberSignupsConsumer();
        memberSignupsConsumer.consumer = mockConsumer;
    }

    @Before
    public void setUpStreams() {
        systemOutContent = new ByteArrayOutputStream();
        System.setOut(new PrintStream(systemOutContent));
    }

    @After
    public void restoreStreams() {
        System.setOut(originalSystemOut);
    }

    @Test
    public void testHandleRecords_output() {
        // Verify that the testHandleRecords writes the correct data to System.out
        // A text fixture called systemOutContent has already been set up in this class to capture System.out data.
        List<ConsumerRecord<Integer, String>> records = new ArrayList<>();
        records.add(new ConsumerRecord<>("test", 0, 0, 42, "toto"));
        Map<TopicPartition, List<ConsumerRecord<Integer, String>>> recordsByTopicPartition = new HashMap<>();
        recordsByTopicPartition.put(new TopicPartition("test", 0), records);

        memberSignupsConsumer.handleRecords(new ConsumerRecords<>(recordsByTopicPartition));

        Assert.assertEquals("key=42, value=toto, topic=test, partition=0, offset=0\n",
                systemOutContent.toString());
    }

    @Test
    public void testHandleRecords_none() {
        // Verify that testHandleRecords behaves correctly when processing no records.
        // A text fixture called systemOutContent has already been set up in this class to capture System.out data.
        Map<TopicPartition, List<ConsumerRecord<Integer, String>>> recordsByTopicPartition = new HashMap<>();
        memberSignupsConsumer.handleRecords(new ConsumerRecords<>(recordsByTopicPartition));

        Assert.assertEquals("", systemOutContent.toString());
    }

    @Test
    public void testHandleRecords_multiple() {
        // Verify that testHandleRecords behaves correctly when processing multiple records.
        // A text fixture called systemOutContent has already been set up in this class to capture System.out data.
        List<ConsumerRecord<Integer, String>> records = new ArrayList<>();
        records.add(new ConsumerRecord<>("test", 0, 0, 42, "toto"));
        records.add(new ConsumerRecord<>("test", 0, 1, 51, "pastis"));
        Map<TopicPartition, List<ConsumerRecord<Integer, String>>> recordsByTopicPartition = new HashMap<>();
        recordsByTopicPartition.put(new TopicPartition("test", 0), records);

        memberSignupsConsumer.handleRecords(new ConsumerRecords<>(recordsByTopicPartition));

        Assert.assertEquals("key=42, value=toto, topic=test, partition=0, offset=0\n" +
                        "key=51, value=pastis, topic=test, partition=0, offset=1\n",
                systemOutContent.toString());
    }

}
