## Spark
- Composants Spark:
  - Spark Core
  - Spark SQL
  - Spark ML
  - Spark Streaming
  - Spark Graph

- Best practice: taille des partitions de spark < 128 mo

- An Executor holds nothing, it just does work.

- A Partition is processed by a Core that has been assigned to an Executor. An Executor typically has 1 core but can 
  have more than 1 such Core.
- An App has Actions that translate to 1 or more Jobs.

- A Job has Stages (based on Shuffle Boundaries).

- Stages have Tasks, the number of these depends on number of Partitions.

- Parallel processing of the Partitions depends on number of Cores allocated to Executors.

- Spark is scalable in terms of Cores, Memory and Disk. The latter two in  means that if the Partitions cannot all fit
  into Memory on the Worker for your Job, then that Partition or more will spill in its entirety to Disk.

- Major features in AQE:
  - Including coalescing post-shuffle partitions
    spark.sql.adaptive.coalescePartitions.enabled: When true and spark.sql.adaptive.enabled is true, Spark will coalesce
    contiguous shuffle partitions according to the target size (specified by spark.sql.adaptive.advisoryPartitionSizeInBytes),
    TO AVOID TOO MANY SMALL TASKS.
  - Converting sort-merge join to broadcast join
  - Skew join optimization


Links:
- https://www.linkedin.com/pulse/catalyst-tungsten-apache-sparks-speeding-engine-deepak-rajak/
- https://www.linkedin.com/pulse/apache-spark-rdd-vs-dataframe-dataset-chandan-prakash/
