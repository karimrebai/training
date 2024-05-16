**You are developing an application that will only recognize and tag specific business to business product logos in
images.
You do not have an extensive background working with machine learning models, but need to get your application
working.
What is the current best method to accomplish this task?**

- Use the AutoML Vision service to train a custom model using the Vision API.<br>
  _Cloud Vision API can recognize common logos, but would struggle to find specific business logos on its own. The
  best option is AutoML, which allows you to take the pre-trained Vision API and apply it to custom images. Creating
  a custom ML model from scratch would be time-consuming and is not necessary when you can build on existing models._

***
**You regularly use prefetch caching with a Data Studio report to visualize the results of BigQuery queries. You want to
minimize service costs. What should you do?**

- Set up the report to use the Owner's credentials to access the underlying data in BigQuery, and verify that the
  Enable cache checkbox is selected for the report.<br>
  _You must set Owner credentials to use the enable cache option in BigQuery. It is also a Google best practice to
  use the enable cache option when the business scenario calls for using prefetch caching.Cache auto-expires after
  12 hours. 24-hour cache is not a valid option. Prefetch cache is only for data sources that use the Owner's
  credentials (not the Viewer's credentials)._

***
**In order to protect live customer data, your organization needs to maintain separate operating environments
development/test, staging, and production to meet the needs of running experiments, deploying new features, and serving
production customers. What is the best practice for isolating these environments while at the same time maintaining
operability?**

- Create a separate project for dev/test, staging, and production. Migrate relevant data between projects when ready for
  the next stage.<br>
  _Explanation_

***
**You have a long-running, streaming Dataflow pipeline that you need to shut down. You do not need to preserve data
currently in the processing pipeline and need it shut down as soon as possible. Which shutdown option should you use to
complete the shutdown process?**

- Cancel<br>
  _Graceful Shutdown is not a valid option. Cancel will shut down the pipeline without allowing buffered jobs to
  complete. Stop is not a valid option. Drain will stop new data from flowing in but will leave the processing pipeline
  running to process buffered data, which is not what we want._

***
**What is the difference between a deep and wide neural network?
What would you use a deep AND wide neural network for?**

- Deep and wide models are ideal for a recommendation application.<br>
- Wide models are used for memorization. Deep models are for generalization.<br>
  _A deep neural network is simply a feed-forward network with many hidden layers. Wider networks can approximate more
  interactions between input variables._

***
**The data team in your organization has created a Cloud Dataflow job that processes CSV files from Cloud Storage using
a user-managed controller service account. The job creates a worker, but then fails. What could be the problem?**

- The user-managed service account does not have the necessary storage IAM role for the Cloud Storage bucket.<br>
  _If you are using user-managed controller service accounts for Cloud Dataflow, you need to make sure that those
  service accounts have all the necessary roles and permissions, including access to Cloud Storage in this scenario.
  With that in mind you should use the principle of least privilege, and not grant broad roles such as Project Editor.
  This problem is not a provisioning issue._

***
**Your company's Kafka server cluster has been unable to scale to the demands of their data ingest needs. Streaming data
ingest comes from locations all around the world. How can they migrate this functionality to Google Cloud to be able to
scale for future growth?**

- Create a single Pub/Sub topic. Configure endpoints to publish to the Pub/Sub topic, and configure Cloud Dataflow to
  subscribe to the same topic to process messages as they come in.<br>
  _Cloud Pub/Sub is an ideal managed replacement for Kafka. It is globally available so topics do not need to be created
  on the basis of region._

***
**You are designing storage for event data as part of building a data pipeline on Google Cloud. Your input data is in
CSV format. You want to minimize the cost of querying individual values over time windows. Which storage service and
schema design should you use?**

- Use Cloud Bigtable for storage. Design tall and narrow tables, and use a new row for each single event version.<br>
  _You will want to use Bigtable for this 'values over time' scenario. Using tall and narrow tables is the best practice
  for this use case, using a new row, not a new column for each event. You should not use Cloud Storage or BigQuery for
  this scenario._

***
**You want to update a Cloud Dataflow pipeline to use the same PTransform on 2 PCollections, which are both of the same
type of CSV row data. What is the best way to achieve this?**

- Use a Flatten transform on the PCollections to merge them prior to the PTransform<br>
  _The Flatten transformation merges multiple PCollection objects into a single logical PCollection, whereas Join
  transforms like CoGroupByKey attempt to merge data where there are related keys in the two datasets._

***
**You need to deploy a TensorFlow machine-learning model to Google Cloud. You want to maximize the speed and minimize
the cost of model prediction and deployment. What should you do?**

- Export your trained model to a SavedModel format. Deploy and run your model on Cloud ML Engine.<br>
  _You do not need to run the model on Kubernetes Engine. Cloud ML Engine / AI Platform is the preferred service to
  fulfill the requirement to minimize costs. You would not run your ML model on Cloud Storage. There are reasons for
  exporting 2 copies, however, this does not save on costs, which is a requirement._

***
**Your team is storing data in Cloud Storage that will only be accessed roughly once a month after it is 90 days old.
Your business continuity policy states that all data must be replicated in more than one region. How do you set this up
efficiently?**

- Use a Lifecycle configuration to change the Storage Class to Near-line once an object reaches 90 days of age. Use
  dual-region buckets.<br>
  _The Near-line storage class provides the most efficient pricing for data that will only be accessed once per month,
  and a lifecycle configuration can update the storage class of objects once they reach 90 days of age. A dual-region
  bucket is sufficient to satisfy the other requirement of this scenario._

***
**Your security team have decided that your Dataproc cluster must be isolated from the public internet and not have any
public IP addresses. How can you achieve this?**

- Use the --no-address flag and make sure that Private Google Access is enabled for the subnet.<br>
  _Using the --no-address flag will prevent public IPs from being assigned to nodes in a Cloud Dataproc cluster.
  However, Private Google Access is still required for the subnet to access certain GCP APIs._

***
**You need to design a data pipeline that will allow you to ingest TBs data for later analysis as both large-scale SQL
aggregations and small range-scan lookups. What would be a good approach to this?**

- Ingest data through Cloud Dataflow. Use multiple transformations to output to BigQuery for SQL aggregate queries, and
  Bigtable for range-scan queries.<br>
  _Large scale aggregated SQL queries are best run on BigQuery, whereas small range-scan lookups across TBs of data work
  best on Cloud Bigtable. By using a single Cloud Dataflow pipeline you can ingest data into both systems at the same
  time and have your choice of query method. Datastore and Spanner would not support both types of query._

***
**You want to display aggregate view counts for your YouTube channel data in Data Studio. You want to see the video
titles and view counts summarized over the last 30 days. You also want to divide the data by the Country Code using the
fewest possible steps. What should you do?**

- Set up a YouTube data source for your channel data for Data Studio. Set Views as the metric and set Video Title and
  Country Code as report dimensions.<br>
  _Using Views as the metric and setting Video Title and Country Code as the report dimensions is the better option.
  Country Code is a dimension because it's a string and should be displayed as such, that is, showing all countries,
  instead of filtering._

***
**You currently have a Bigtable instance you've been using for development running a development instance type, using
HDDs for storage. You are ready to upgrade your development instance to a production instance for increased performance.
You also want to upgrade your storage to SSDs as you need maximum performance for your instance. What should you do?**

- Export your Bigtable data into a new instance, and configure the new instance type as production with SSDs<br>
  _Since you cannot change the disk type on an existing Bigtable instance, you will need to export/import your Bigtable
  data into a new instance with the different storage type. You will need to export to Cloud Storage then back to
  Bigtable again._

***
**Which of these is NOT a type of trigger that applies to Dataflow?**

- Element size in bytes<br>
  _Element size is not a type of trigger; therefore, it is the correct answer. Dataflow is basically an Apache
  Beam-managed service. The triggers that apply to Dataflow are: event time triggers, processing time triggers,
  data-driven triggers, composite triggers, Pub/Sub topics, and Cloud Functions._

***
**You have deployed some custom data processing code on Compute Engine which receives data from a push subscription to a
Cloud Pub/Sub topic. You have noticed that in Stackdriver the num_outstanding_messages metric has occasional peaks and
is never less than 100. What could be causing this?**

- There is insufficient processing power in the Compute Engine deployment to process the amount of Pub/Sub traffic.
  Increase the size of the Compute Engine instance.<br>
  _If the num_outstanding_messages metric on a push subscription has occasional peaks, it is likely that the subscriber
  is sometimes struggling to deal with the load of the messages being sent. In this case it would be logical to assume
  that the Compute Engine resource is underpowered. Changing the subscription model is not applicable, and there is no
  need to switch to Kubernetes Engine._

***
**You need to choose a structure storage option for storing very large amounts of data with the following properties and
requirements: 1) The data has a single key 2) You need very low latency. Which solution should you choose?**

- Cloud Bigtable<br>
  _Bigtable uses a single key and has very low latency (in milliseconds), so it is the best choice. Datastore stores
  less data than Bigtable, and operates on multiple keys. Cloud SQL holds at most 15TB of data and is not a high
  performance single-key database. BigQuery does not use single key values and has latency measured in seconds, not
  milliseconds._

***
**Your company's aging Hadoop servers are nearing end of life. Instead of replacing your hardware, your CIO has decided
to migrate the cluster to Google Cloud Dataproc. A direct lift and shift migration of the cluster would require 30 TB of
disk space per individual node. There are cost concerns about using that much storage. How can you best minimize the
cost of the migration?**

- Decouple storage from computer by placing the data in Cloud Storage<br>
  _Placing all input and output data in Cloud Storage allows you to 1. Treat clusters as ephemeral and 2. Use a much
  cheaper storage location compared to persistent disks without a noticeable impact on performance. Preemptible VM's
  only save costs on compute (CPU/memory) usage, and has no effect on storage costs._

***
**Your organization will be deploying a new fleet of IoT devices, and writes to your Bigtable instance are expected to
peak at 50,000 queries per second. You have optimized your row key design and need to design a cluster that can meet
this demand. What do you do?**

- Scale the instance to 5 SSD nodes.<br>
  _An optimized Bigtable instance with a well-designed row key schema can theoretically support up to 10,000 write
  queries per second per node, so 5 nodes are required._

***
**What types of Bigtable row keys can lead to hot-spotting?**

- Leading with a non-reversed timestamp.<br>
- Standard domain names (non-reversed).<br>
  _Like sequential IDs, timestamps will read and write from the same node, causing increased load. Non-reversed domain
  names at the start of a row key can lead to hot-spotting. If you need to use domain names, reverse it. Reverse
  timestamps will spread the load for reads and writes between nodes, making this an incorrect answer for this question.
  Randomized IDs will spread the load for reads and writes between nodes, making this an incorrect answer for this
  question._

***
**Your company needs to run analytics on their incoming inventory data stream. They need to use their existing Hadoop
workloads to perform this task. What two steps must be performed for storing and analyzing incoming inventory data
streams using the existing Hadoop workloads?**

- Stream from Cloud Pub/Sub into Cloud Dataproc, which can then place relevant data in the appropriate storage location.
- Connect Cloud Dataproc to Bigtable and Cloud Storage, running analytics on the data in both services.<br>
  _They need to reuse their existing Hadoop/Spark code, so Dataflow would be the wrong solution. Dataproc can connect to
  Pub/Sub for ingesting data. We can then process the data and then natively connect to Cloud Storage or Bigtable for
  storage and analytics._

***
**You want to train your machine learning model on AI Platform while saving costs. Which scaling tier would you choose?
**

- BASIC<br>
  _BASIC tier uses a single instance and is the lowest cost. CUSTOM can use different configurations, but BASIC is still
  cheaper with a single instance. STANDARD_1 and PREMIUM_1 all use multiple worker and parameter servers are more
  expensive._

***
**You work at a very large organizations that has a very large analyst team. You use the default pricing model for
BigQuery. During heavy usage, your analyst group occasionally runs out of the 2000 slots available for the BigQuery
jobs. You do not want to create additional projects for the sole purpose of increasing slot count. What can you do to
resolve this?**

- Switch to flat rate pricing to enable a higher total slot quota for your project.<br>
  _Creating another project is not necessary. Switching to flat rate pricing for the single project will allow you to
  increase your slot count._

***
**Question**

- Answer<br>
  _Explanation_

***
**Question**

- Answer<br>
  _Explanation_

***
**Question**

- Answer<br>
  _Explanation_

***
**Question**

- Answer<br>
  _Explanation_

***
**Question**

- Answer<br>
  _Explanation_

***
**Question**

- Answer<br>
  _Explanation_

***
**Question**

- Answer<br>
  _Explanation_

***
**Question**

- Answer<br>
  _Explanation_

***
**Question**

- Answer<br>
  _Explanation_

***
**Question**

- Answer<br>
  _Explanation_

***
**Question**

- Answer<br>
  _Explanation_

***
**Question**

- Answer<br>
  _Explanation_

***
**Question**

- Answer<br>
  _Explanation_

***
**Question**

- Answer<br>
  _Explanation_

***
**Question**

- Answer<br>
  _Explanation_

***
**Question**

- Answer<br>
  _Explanation_

***
**Question**

- Answer<br>
  _Explanation_

***
**Question**

- Answer<br>
  _Explanation_

***
**Question**

- Answer<br>
  _Explanation_

***
**Question**

- Answer<br>
  _Explanation_

***
**Question**

- Answer<br>
  _Explanation_

***
**Question**

- Answer<br>
  _Explanation_

***
**Question**

- Answer<br>
  _Explanation_

***
**Question**

- Answer<br>
  _Explanation_

***
**Question**

- Answer<br>
  _Explanation_

***
**Question**

- Answer<br>
  _Explanation_

***
**Question**

- Answer<br>
  _Explanation_

***
**Question**

- Answer<br>
  _Explanation_

***
**Question**

- Answer<br>
  _Explanation_

***
**Question**

- Answer<br>
  _Explanation_

***
**Question**

- Answer<br>
  _Explanation_

***
**Question**

- Answer<br>
  _Explanation_