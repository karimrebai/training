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