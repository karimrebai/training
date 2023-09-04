## EDF
- on a choisi de stocker les fichiers sur HBase car petits, HBase fait le merging pour nous

- spark micro-batch de 10 sec, estimation de data reçues en 10s et on a divisé par 128 pr avoir le nombre de partitions

- par défaut HBase fait des partitions de 10go, mais on a forcé HBase a faire des partitions de 128Mo (hbase-site.xml)

## Hadoop
- Hadoop:
  - hdfs: namenode, secondary namenode, datanode: facteur de replication par défaut est de 3
  - mapreduce: traitement des données
  - yarn: gestion des ressources
  - zookeeper: coordinateur

## Spark
- Composants Spark:
  - Spark Core
  - Spark SQL
  - Spark ML
  - Spark Streaming
  - Spark Graph

- Quels sont les cluster manager qu'on peut utiliser avec Spark: Yarn, Mesos, Spark standalone

- 2 modes de soumission avec Yarn:
  - client
  - cluster

- le DAG (Directed Acyclic Graph)

- best practice: taille des partitions de spark < 128 mo

- An Executor holds nothing, it just does work.
  - A Partition is processed by a Core that has been assigned to an Executor. An Executor typically has 1 core but can have more than 1 such Core.
  - An App has Actions that translate to 1 or more Jobs.
  - A Job has Stages (based on Shuffle Boundaries).
  - Stages have Tasks, the number of these depends on number of Partitions.
  - Parallel processing of the Partitions depends on number of Cores allocated to Executors.
Spark is scalable in terms of Cores, Memory and Disk. The latter two in  means that if the Partitions cannot all fit
into Memory on the Worker for your Job, then that Partition or more will spill in its entirety to Disk.

