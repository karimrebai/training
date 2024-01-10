## Spark
- Composants Spark:
  - Spark Core
  - Spark SQL
  - Spark ML
  - Spark Streaming
  - Spark Graph

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


- Repartition
Returns a new DataFrame that has exactly n partitions.
Wide transformation
Pro: Evenly balances partition sizes
Con: Requires shuffling all data

- Coalesce
Returns a new DataFrame that has exactly n partitions, when fewer partitions are requested.
If a larger number of partitions is requested, it will stay at the current number of partitions.
Narrow transformation, some partitions are effectively concatenated
Pro: Requires no shuffling
Cons:
- Is not able to increase # partitions
- Can result in uneven partition sizes


- Best practices on partitioning:
  - Make the number of partitions a multiple of the number of cores
  - Target a partition size of ~200MB
  - Size default shuffle partitions by dividing largest shuffle stage input by the target partition size
    e.g.: 4TB / 200MB = 20,000 shuffle partition count 
  Note: When writing a DataFrame to storage, the number of DataFrame partitions determines the number of data files written.
  (This assumes that Hive partitioning is not used for the data in storage)


- Caching
  - It is more advantageous to store DataFrame df at the MEMORY_AND_DISK storage level rather than the MEMORY_ONLY
    storage level when it’s faster to read all the computed data in DataFrame df that cannot fit into memory from disk
    rather than recompute it based on its logical plan.

Links:
- https://www.linkedin.com/pulse/catalyst-tungsten-apache-sparks-speeding-engine-deepak-rajak/
- https://www.linkedin.com/pulse/apache-spark-rdd-vs-dataframe-dataset-chandan-prakash/
- https://towardsdatascience.com/strategies-of-spark-join-c0e7b4572bcf