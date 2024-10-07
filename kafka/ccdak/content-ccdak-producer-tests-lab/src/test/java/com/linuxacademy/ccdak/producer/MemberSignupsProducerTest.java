package com.linuxacademy.ccdak.producer;

import org.apache.kafka.clients.producer.MockProducer;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.common.serialization.IntegerSerializer;
import org.apache.kafka.common.serialization.StringSerializer;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.PrintStream;

public class MemberSignupsProducerTest {

    MockProducer<Integer, String> mockProducer;
    MemberSignupsProducer memberSignupsProducer;

    // Contains data sent so System.out during the test.
    private ByteArrayOutputStream systemOutContent;
    // Contains data sent so System.err during the test.
    private ByteArrayOutputStream systemErrContent;
    private final PrintStream originalSystemOut = System.out;
    private final PrintStream originalSystemErr = System.err;

    @Before
    public void setUp() {
        mockProducer = new MockProducer<>(false, new IntegerSerializer(), new StringSerializer());
        memberSignupsProducer = new MemberSignupsProducer();
        memberSignupsProducer.producer = mockProducer;
    }

    @Before
    public void setUpStreams() {
        systemOutContent = new ByteArrayOutputStream();
        systemErrContent = new ByteArrayOutputStream();
        System.setOut(new PrintStream(systemOutContent));
        System.setErr(new PrintStream(systemErrContent));
    }

    @After
    public void restoreStreams() {
        System.setOut(originalSystemOut);
        System.setErr(originalSystemErr);
    }

    @Test
    public void testHandleMemberSignup_sent_data() {
        // Perform a simple test to verify that the producer sends the correct data to the correct topic when handleMemberSignup is called.
        // Verify that the published record has the memberId as the key and the uppercased name as the value.
        // Verify that the records is sent to the member_signups topic.
        memberSignupsProducer.handleMemberSignup(42, "fada");

        mockProducer.completeNext();

        ProducerRecord<Integer, String> record = mockProducer.history().get(0);
        Assert.assertEquals(1, mockProducer.history().size());
        Assert.assertEquals(Integer.valueOf(42), record.key());
        Assert.assertEquals("FADA", record.value());
        Assert.assertEquals("member_signups", record.topic());
    }

    @Test
    public void testHandleMemberSignup_partitioning() {
        // Verify that records with a value starting with A-M are assigned to partition 0, and that others are assigned to partition 1.
        // You can send two records in this test, one with a value that begins with A-M and the other that begins with N-Z.
        memberSignupsProducer.handleMemberSignup(1, "bayern");
        memberSignupsProducer.handleMemberSignup(2, "real");

        mockProducer.completeNext();

        Assert.assertEquals(2, mockProducer.history().size());
        Assert.assertEquals(Integer.valueOf(0), mockProducer.history().get(0).partition());
        Assert.assertEquals(Integer.valueOf(1), mockProducer.history().get(1).partition());
    }

    @Test
    public void testHandleMemberSignup_output() throws IOException {
        // Verify that the producer logs the record data to System.out.
        // A text fixture called systemOutContent has already been set up in this class to capture System.out data.
        memberSignupsProducer.handleMemberSignup(1, "bayern");
        mockProducer.completeNext();

        Assert.assertEquals("key=1, value=BAYERN\n", systemOutContent.toString());
    }

    @Test
    public void testHandleMemberSignup_error() throws IOException {
        // Verify that the producer logs the error message to System.err if an error occurs when sending a record.
        // A text fixture called systemErrContent has already been set up in this class to capture System.err data.
        memberSignupsProducer.handleMemberSignup(1, "bayern");
        mockProducer.errorNext(new RuntimeException("oh fada"));

        Assert.assertEquals("oh fada\n", systemErrContent.toString());
    }

}
