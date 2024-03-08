Best practices doc:

- Bigtable
https://cloud.google.com/bigtable/quotas#storage-per-node

- Pub/Sub
https://cloud.google.com/pubsub/docs/publish-best-practices
https://cloud.google.com/pubsub/docs/subscribe-best-practices






  
# Modernizing Data Lakes and Data Warehouses

## Intro to data engineering

- Key considerations when building a data lake:
  - Type of data ?
  - Scale to meet the demand ?
  - High throughput ingestion ?
  - Fine-grained access control to objects ?
  - Other tools can connect to it easily ?

- Key considerations when building a data warehouse:
  - Can it serve as a sink for both batch and streaming pipelines ?
  - Can it scale to meet my needs ?
  - How the data is organized, cataloged and access controlled ?
  - Is it design for performance ?
  - What level of maintenance is required by your engineering team ?

- Cloud SQL
  - Optimized for high-throughput writes

- Big Query
  - Optimized for high-read data

## Building a data lake

TODO

## Building a data warehouse

### Schema design

- Transactional databases often use normal form
=> Store the data efficiently
=> Query processing is clear and direct
=> Improves orderliness of the data, useful to save space

Example: Order -> Order items

- Data warehouses often denormalize
Denormalizing is the strategy of duplicate field values for a column in a table to gain performance
Data is repeated rather than being relational
Flattened data takes more space but makes queries more efficient because they can be processed in parallel using
columnar processing
Specifically, denormalizing enables BigQuery to more efficiently distribute processing amon slots

Normalizing data can be bad for performance => Grouping on a 1-to-many field in flattened data can cause shuffling
of data over the network.

Put all the info in a single table is not a good solution too, because data would be duplicated.

Fortunately, BigQuery supports a method to overcome this with Repeated and Nested fields.

### Nested and repeated fields

Nested ARRAY fields and STRUCT fields allow for differing data granularity in the same table.

STRUCT allows really wide schemas, major benefit is that the data is pre joined already, hence faster to query.

STRUCT fields => RECORD type
ARRAY fields => REPEATED mode / Can be of any type and can be part of regular fields or STRUCTS

Demo: https://github.com/GoogleCloudPlatform/training-data-analyst/blob/master/courses/data-engineering/demos/nested.md

### Recap

- Instead of joins, take advantage of nested and repeated fields in denormalized tables
  - Keep a dimension table smaller than 10 GB normalized, unless the table rarely goes through UPDATE and DELETE operations
- Denormalize a dimension table larger than 10 GB, unless data manipulation or costs outweigh benefits of optimal queries

Examples:
```sql
SELECT fullVisitorId, date,
    ARRAY_AGG(DISTINCT v2ProductName) AS products_viewed,
    ARRAY_LENGTH(ARRAY_AGG(DISTINCT v2ProductName)) AS distinct_products_viewed
FROM `data-to-insights.ecommerce.all_sessions`
WHERE visitId = 1501570398
GROUP BY fullVisitorId, date
ORDER BY date
```

```sql
SELECT DISTINCT visitId,
                h.page.pageTitle
FROM `bigquery-public-data.google_analytics_sample.ga_sessions_20170801`,
     UNNEST(hits) AS h
```

### Partitioning and Clustering

TODO

https://cloud.google.com/bigquery/docs/partitioned-tables

https://cloud.google.com/bigquery/docs/clustered-tables




# Building Batch Data Pipelines on Google Cloud

## Executing Spark on Dataproc

### Optimizing Dataproc Storage

- Local HDFS is a good solution if:
  - Your jobs require a lot of metadata operations: having thousands of files / partitions and each file is small
  - You modify the HDFS data frequently or you rename directories: Cloud Storage objects are immutable so renaming
    a directory is an expensive operation because it consists of copying all objects to a new key and deleting them
  - You heavily use the append operation on HDFS files
  - You have workloads that involve heavy I/O - spark.write.partitionBy(...).parquet("gs://...")
  - You have I/O workloads that are especially sensitive to latency

- Cloud Storage works well as the initial and final source of data in a big data pipeline

- Separating storage and compute helps reduce costs significantly

- Cluster Scheduled Deletion:
  - Minimum: 10 minutes
  - Maximum: 14 days

- With ephemeral clusters, you only pay for what you use

- If persistent cluster is required:
  - Create the smallest cluster you can, using preemptible VMs based on time budget
  - Scope your work to the smallest possible number of jobs
  - Scale the cluster to the minimum workable number of nodes, use auto-scaling.