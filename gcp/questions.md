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
**What is the difference between a deep and wide neural network? What would you use a deep AND wide neural network for?**

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