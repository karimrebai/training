## Make configuration changes to address the following issues:

1) Recently, a Kafka broker failed and had to be restarted. Unfortunately, that broker was the leader for a partition of
   the member_signups topic at the time, and a few records had been committed by the leader but had not yet been written
   to the replicas. A small number of records were lost when the leader failed. Change the configuration so that 
   this does not happen again.

=> acks = all

2) The producer is configured to retry the process of sending a record when it fails due to a transient error. However,
   in a few instances this has caused records to be written to the topic log in a different order than the order 
   they were sent by the producer, because a record was retried while another record was sent ahead of it. A few 
   downstream consumers depend on certain ordering guarantees for this data. Ensure that retries by the producer do 
   not result in out-of-order records.

=> max.in.flight.requests.per.connection = 1

3) This producer sometimes experiences high throughput that could benefit from a greater degree of message batching.
   Increase the batch size to 64 KB (65536 bytes).

=> batch.size = 65536
