**265. You are designing a fault-tolerant architecture to store data in a regional BigQuery dataset.
You need to ensure that your application is able to recover from a corruption event in your tables that occurred within
the past seven days.
You want to adopt managed services with the lowest RPO and most cost-effective solution. What should you do?**

- A. Access historical data by using time travel in BigQuery.
- B. Export the data from BigQuery into a new table that excludes the corrupted data
- C. Create a BigQuery table snapshot on a daily basis.
- D. Migrate your data to multi-region BigQuery buckets

=> A

- _Lowest RPO: Time travel offers point-in-time recovery for the past seven days by default, providing the shortest
  possible recovery point objective (RPO) among the given options. You can recover data to any state within that
  window._
- _No Additional Costs: Time travel is a built-in feature of BigQuery, incurring no extra storage or operational costs._
- _Managed Service: BigQuery handles time travel automatically, eliminating manual backup and restore processes._

***
**266. You are building a streaming Dataflow pipeline that ingests noise level data from hundreds of sensors placed near
construction sites across a city. The sensors measure noise level every ten seconds, and send that data to the pipeline
when levels reach above 70 dBA. You need to detect the average noise level from a sensor when data is received for a
duration of more than 30 minutes, but the window ends when no data has been received for 15 minutes. What should you
do?**

- A. Use session windows with a 15-minute gap duration.
- B. Use session windows with a 30-minute gap duration.
- C. Use hopping windows with a 15-minute window, and a thirty-minute period.
- D. Use tumbling windows with a 15-minute window and a fifteen-minute .withAllowedLateness operator.

=> A

_To detect average noise levels from sensors, the best approach is to use session windows with a 15-minute gap
duration (
Option A). Session windows are ideal for cases like this where the events (sensor data) are sporadic. They group
events
that occur within a certain time interval (15 minutes in your case) and a new window is started if no data is received
for the duration of the gap. This matches your requirement to end the window when no data is received for 15 minutes,
ensuring that the average noise level is calculated over periods of continuous data._

***
**267. You are creating a data model in BigQuery that will hold retail transaction data. Your two largest tables,
sales_transaction_header and sales_transaction_line, have a tightly coupled immutable relationship. These tables are
rarely modified after load and are frequently joined when queried. You need to model the sales_transaction_header and
sales_transaction_line tables to improve the performance of data analytics queries. What should you do?**

- A. Create a sales_transaction table that holds the sales_transaction_header information as rows and the
  sales_transaction_line rows as nested and repeated fields.
- B. Create a sales_transaction table that holds the sales_transaction_header and sales_transaction_line information as
  rows, duplicating the sales_transaction_header data for each line.
- C. Create a sales_transaction table that stores the sales_transaction_header and sales_transaction_line data as a JSON
  data type.
- D. Create separate sales_transaction_header and sales_transaction_line tables and, when querying, specify the
  sales_transaction_line first in the WHERE clause

=> A

_In BigQuery, nested and repeated fields can significantly improve performance for certain types of queries,
especially joins, because the data is co-located and can be read efficiently. This approach is often used in data
warehousing scenarios where query performance is a priority, and the data relationships are immutable and rarely
modified._

***
**268. You created a new version of a Dataflow streaming data ingestion pipeline that reads from Pub/Sub and writes to
BigQuery. The previous version of the pipeline that runs in production uses a 5-minute window for processing. You need
to deploy the new version of the pipeline without losing any data, creating inconsistencies, or increasing the
processing latency by more than 10 minutes. What should you do?**

- A. Update the old pipeline with the new pipeline code.
- B. Snapshot the old pipeline, stop the old pipeline, and then start the new pipeline from the snapshot.
- C. Drain the old pipeline, then start the new pipeline.
- D. Cancel the old pipeline, then start the new pipeline.

=> C or B

- _Graceful Data Transition: Draining the old pipeline ensures it processes all existing data in its buffers and
  watermarks before shutting down, preventing data loss or inconsistencies._
- _Minimal Latency Increase: The latency increase will be limited to the amount of time it takes to drain the old
  pipeline, typically within the acceptable 10-minute threshold._

***
**269. Your organization's data assets are stored in BigQuery, Pub/Sub, and a PostgreSQL instance running on Compute
Engine. Because there are multiple domains and diverse teams using the data, teams in your organization are unable to
discover existing data assets. You need to design a solution to improve data discoverability while keeping development
and configuration efforts to a minimum. What should you do?**

- A. Use Data Catalog to automatically catalog BigQuery datasets. Use Data Catalog APIs to manually catalog Pub/Sub
  topics and PostgreSQL tables.
- B. Use Data Catalog to automatically catalog BigQuery datasets and Pub/Sub topics. Use Data Catalog APIs to
  manually catalog PostgreSQL tables.
- C. Use Data Catalog to automatically catalog BigQuery datasets and Pub/Sub topics. Use custom connectors to
  manually catalog PostgreSQL tables.
- D. Use customer connectors to manually catalog BigQuery datasets, Pub/Sub topics, and PostgreSQL tables.

=> B

_It utilizes Data Catalog's native support for both BigQuery datasets and Pub/Sub topics. For PostgreSQL tables
running on a Compute Engine instance, you'd use Data Catalog APIs to create custom entries, as Data Catalog does not
automatically discover external databases like PostgreSQL._

***
**270. You need to create a SQL pipeline. The pipeline runs an aggregate SQL transformation on a BigQuery table every
two hours and appends the result to another existing BigQuery table. You need to configure the pipeline to retry if
errors occur. You want the pipeline to send an email notification after three consecutive failures.
What should you do?**

- A. Use the BigQueryUpsertTableOperator in Cloud Composer, set the retry parameter to three, and set the
  email_on_failure
  parameter to true.
- B. Use the BigQueryInsertJobOperator in Cloud Composer, set the retry parameter to three, and set the email_on_failure
  parameter to true.
- C. Create a BigQuery scheduled query to run the SQL transformation with schedule options that repeats every two hours,
  and enable email notifications.
- D. Create a BigQuery scheduled query to run the SQL transformation with schedule options that repeats every two hours,
  and enable notification to Pub/Sub topic. Use Pub/Sub and Cloud Functions to send an email after three failed
  executions.

=> B

- _It provides a direct and controlled way to manage the SQL pipeline using Cloud Composer (Apache Airflow)._
- _The BigQueryInsertJobOperator is well-suited for running SQL jobs in BigQuery, including aggregate
  transformations and handling of results._
- _The retry and email_on_failure parameters align with the requirements for error handling and notifications._
- _Cloud Composer requires more setup than using BigQuery's scheduled queries directly, but it offers robust workflow
  management, retry logic, and notification capabilities, making it suitable for more complex and controlled data
  pipeline requirements._

***
**271. You are monitoring your organization’s data lake hosted on BigQuery. The ingestion pipelines read data from
Pub/Sub and write the data into tables on BigQuery. After a new version of the ingestion pipelines is deployed, the
daily stored data increased by 50%. The volumes of data in Pub/Sub remained the same and only some tables had their
daily partition data size doubled. You need to investigate and fix the cause of the data increase. What should you do?**

- B.

1. Check for code errors in the deployed pipelines.2. Check for multiple writing to pipeline BigQuery sink.
3. Check for errors in Cloud Logging during the day of the release of the new pipelines.
4. If no errors, restore the BigQuery tables to their content before the last release by using time travel.

- C.

1. Check for duplicate rows in the BigQuery tables that have the daily partition data size doubled.
2. Check the BigQuery Audit logs to find job IDs.
3. Use Cloud Monitoring to determine when the identified Dataflow jobs started and the pipeline code version.
4. When more than one pipeline ingests data into a table, stop all versions except the latest one.

=> C

- _Detailed Investigation of Logs and Jobs Checking for duplicate rows targets the potential immediate cause of the
  issue._
- _Checking the BigQuery Audit logs helps identify which jobs might be contributing to the increased data volume._
- _Using Cloud Monitoring to correlate job starts with pipeline versions helps identify if a specific version of the
  pipeline is responsible._
- _Managing multiple versions of pipelines ensures that only the intended version is active, addressing any
  versioning errors that might have occurred during deployment._

***
**272. You have a BigQuery dataset named “customers”. All tables will be tagged by using a Data Catalog tag template
named “gdpr”. The template contains one mandatory field, “has_sensitive_data”, with a boolean value. All employees must
be able to do a simple search and find tables in the dataset that have either true or false in the “has_sensitive_data’
field. However, only the Human Resources (HR) group should be able to see the data inside the tables for which
“has_sensitive data” is true. You give the all employees group the bigquery.metadataViewer and bigquery.connectionUser
roles on the dataset. You want to minimize configuration overhead. What should you do next?**

- A. Create the “gdpr” tag template with private visibility. Assign the bigquery.dataViewer role to the HR group on
  the tables that contain sensitive data.
- B. Create the “gdpr” tag template with private visibility. Assign the datacatalog.tagTemplateViewer role on this
  tag to the all employees group, and assign the bigquery.dataViewer role to the HR group on the tables that contain
  sensitive data.
- C. Create the “gdpr” tag template with public visibility. Assign the bigquery.dataViewer role to the HR group on
  the tables that contain sensitive data.
- D. Create the “gdpr” tag template with public visibility. Assign the datacatalog.tagTemplateViewer role on this tag
  to the all employees group, and assign the bigquery.dataViewer role to the HR group on the tables that contain
  sensitive data.

=> C

- _The most straightforward solution with minimal configuration overhead._
- _By creating the "gdpr" tag template with public visibility, you ensure that all employees can search and find tables
  based on the "has_sensitive_data" field._
- _Assigning the bigquery.dataViewer role to the HR group on tables with sensitive data ensures that only they can view
  the actual data in these tables._

***
**273. You are creating the CI/CD cycle for the code of the directed acyclic graphs (DAGs) running in Cloud Composer.
Your team has two Cloud Composer instances: one instance for development and another instance for production. Your team
is using a Git repository to maintain and develop the code of the DAGs. You want to deploy the DAGs automatically to
Cloud Composer when a certain tag is pushed to the Git repository. What should you do?**

- A.

1. Use Cloud Build to copy the code of the DAG to the Cloud Storage bucket of the development instance for DAG
   testing.
2. If the tests pass, use Cloud Build to copy the code to the bucket of the production instance.

- B.

1. Use Cloud Build to build a container with the code of the DAG and the KubernetesPodOperator to deploy the
   code to the Google Kubernetes Engine (GKE) cluster of the development instance for testing.
2. If the tests pass, use the KubernetesPodOperator to deploy the container to the GKE cluster of the production
   instance.

- C.

1. Use Cloud Build to build a container and the KubernetesPodOperator to deploy the code of the DAG to the
   Google Kubernetes Engine (GKE) cluster of the development instance for testing.
2. If the tests pass, copy the code to the Cloud Storage bucket of the production instance.

- D.

1. Use Cloud Build to copy the code of the DAG to the Cloud Storage bucket of the development instance for DAG
   testing.
2. If the tests pass, use Cloud Build to build a container with the code of the DAG and the KubernetesPodOperator to
   deploy the container to the Google Kubernetes Engine (GKE) cluster of the production instance.

=> A

_This approach is straightforward and leverages Cloud Build to automate the deployment process. It doesn't require
containerization, making it simpler and possibly quicker._

***
**274. This approach is straightforward and leverages Cloud Build to automate the deployment process. It doesn't require
containerization, making it simpler and possibly quicker.**

- A. Use Cloud KMS encryption key with Dataflow to ingest the existing Pub/Sub subscription to the existing BigQuery
  table.
- B. Create a new BigQuery table by using customer-managed encryption keys (CMEK), and migrate the data from the old
  BigQuery table.
- C. Create a new Pub/Sub topic with CMEK and use the existing BigQuery table by using Google-managed encryption key.
- D. Create a new BigQuery table and Pub/Sub topic by using customer-managed encryption keys (CMEK), and migrate the
  data from the old BigQuery table.

=> B

- *New BigQuery Table with CMEK: This option involves creating a new BigQuery table configured to use a CMEK from Cloud
  KMS. It directly addresses the need to use a CMEK for data at rest in BigQuery.*
- *Migrate Data: Migrating data from the old table (encrypted with a Google-managed key) to the new table (encrypted
  with CMEK) ensures that all existing data complies with the new policy.*

***
**275. You have a web application that publishes messages to Pub/Sub. You plan to build new versions of the application
locally and need to quickly test Pub/Sub integration for each new build. How should you configure local testing?**

- A. In the Google Cloud console, navigate to the API Library, and enable the Pub/Sub API. When developing locally
  configure your application to call pubsub.googleapis.com.
- B. Install the Pub/Sub emulator using gcloud, and start the emulator with a valid Google Project ID. When developing
  locally, configure your applicat.cn to use the local emulator by exporting the PUBSUB_EMULATOR_HOST variable.
- C. Run the gcloud config set api_endpoint_overrides/pubsub https://pubsubemulator.googleapis.com.com/ command to
  change the Pub/Sub endpoint prior to starting the application.
- D. Install Cloud Code on the integrated development environment (IDE). Navigate to Cloud APIs, and enable Pub/Sub
  against a valid Google Project IWhen developing locally, configure your application to call pubsub.googleapis.com.

=> B

_For local testing of Pub/Sub integration in a web application, installing and using the Pub/Sub emulator is the most
efficient approach. The emulator can be installed via gcloud and started with a valid Google Project ID. Configuring
your application to use the emulator locally is done by setting the PUBSUB_EMULATOR_HOST environment variable._

***
**276. You are designing a Dataflow pipeline for a batch processing job. You want to mitigate multiple zonal failures at
job submission time. What should you do?**

- A. Submit duplicate pipelines in two different zones by using the --zone flag.
- B. Set the pipeline staging location as a regional Cloud Storage bucket.
- C. Specify a worker region by using the --region flag.
- D. Create an Eventarc trigger to resubmit the job in case of zonal failure when submitting the job.

=> C

_Specifying a worker region (instead of a specific zone) allows Google Cloud's Dataflow service to manage the
distribution of resources across multiple zones within that region_

***
**277. You are designing a real-time system for a ride hailing app that identifies areas with high demand for rides to
effectively reroute available drivers to meet the demand. The system ingests data from multiple sources to Pub/Sub,
processes the data, and stores the results for visualization and analysis in real-time dashboards. The data sources
include driver location updates every 5 seconds and app-based booking events from riders. The data processing involves
real-time aggregation of supply and demand data for the last 30 seconds, every 2 seconds, and storing the results in a
low-latency system for visualization. What should you do?**

- A. Group the data by using a tumbling window in a Dataflow pipeline, and write the aggregated data to Memorystore.
- B. Group the data by using a hopping window in a Dataflow pipeline, and write the aggregated data to Memorystore.
- C. Group the data by using a session window in a Dataflow pipeline, and write the aggregated data to BigQuery.
- D. Group the data by using a hopping window in a Dataflow pipeline, and write the aggregated data to BigQuery.

=> B

- _Hopping Window: Hopping windows are fixed-sized, overlapping intervals._
- _Aggregate data over the last 30 seconds, every 2 seconds, as hopping windows allow for overlapping data analysis._
- _Memorystore: Ideal for low-latency access required for real-time visualization and analysis._

***
**278. You need to train an XGBoost model on a small dataset. Your training code requires custom dependencies. You want
to minimize the startup time of your training job. How should you set up your Vertex AI custom training job?**

- A. Store the data in a Cloud Storage bucket, and create a custom container with your training application. In your
  training application, read the data from Cloud Storage and train the model.
- B. Use the XGBoost prebuilt custom container. Create a Python source distribution that includes the data and
  installs the dependencies at runtime. In your training application, load the data into a pandas DataFrame and train
  the model.
- C. Create a custom container that includes the data. In your training application, load the data into a pandas
  DataFrame and train the model.
- D. Store the data in a Cloud Storage bucket, and use the XGBoost prebuilt custom container to run your training
  application. Create a Python source distribution that installs the dependencies at runtime. In your training
  application, read the data from Cloud Storage and train the model.

=> A?

_Given the focus on minimizing startup time, and based on the information about XGBoost prebuilt container dependencies
available here https://cloud.google.com/vertex-ai/docs/training/pre-built-containers#xgboost_

***
**279. You want to store your team’s shared tables in a single dataset to make data easily accessible to various
analysts. You want to make this data readable but unmodifiable by analysts. At the same time, you want to provide the
analysts with individual workspaces in the same project, where they can create and store tables for their own use,
without the tables being accessible by other analysts. What should you do?**

- A. Give analysts the BigQuery Data Viewer role at the project level. Create one other dataset, and give the
  analysts the BigQuery Data Editor role on that dataset.
- B. Give analysts the BigQuery Data Viewer role at the project level. Create a dataset for each analyst, and give
  each analyst the BigQuery Data Editor role at the project level.
- C. Give analysts the BigQuery Data Viewer role on the shared dataset. Create a dataset for each analyst, and give
  each analyst the BigQuery Data Editor role at the dataset level for their assigned dataset.
- D. Give analysts the BigQuery Data Viewer role on the shared dataset. Create one other dataset and give the
  analysts the BigQuery Data Editor role on that dataset.

=> C

- _Data Viewer on Shared Dataset: Grants read-only access to the shared dataset._
- _Data Editor on Individual Datasets: Giving each analyst Data Editor role on their respective dataset creates private
  workspaces where they can create and store personal tables without exposing them to other analysts._

***
**280. You are running a streaming pipeline with Dataflow and are using hopping windows to group the data as the data
arrives. You noticed that some data is arriving late but is not being marked as late data, which is resulting in
inaccurate aggregations downstream. You need to find a solution that allows you to capture the late data in the
appropriate window. What should you do?**

- A. Use watermarks to define the expected data arrival window. Allow late data as it arrives.
- B. Change your windowing function to tumbling windows to avoid overlapping window periods.
- C. Change your windowing function to session windows to define your windows based on certain activity.
- D. Expand your hopping window so that the late data has more time to arrive within the grouping.

=> A

- _Watermarks: Watermarks in a streaming pipeline are used to specify the point in time when Dataflow expects all data up to that point to have arrived._
- _Allow Late Data: configure the pipeline to accept and correctly process data that arrives after the watermark, ensuring it's captured in the appropriate window._

***
**Question**

=> Answer

- _Explanation_

***
**Question**

=> Answer

- _Explanation_

***
**Question**

=> Answer

- _Explanation_

***
**Question**

=> Answer

- _Explanation_

***
**Question**

=> Answer

- _Explanation_

***
**Question**

=> Answer

- _Explanation_

***
**Question**

=> Answer

- _Explanation_

***
**Question**

=> Answer

- _Explanation_

***
**Question**

=> Answer

- _Explanation_

***
**Question**

=> Answer

- _Explanation_

***
**Question**

=> Answer

- _Explanation_

***
**Question**

=> Answer

- _Explanation_

***
**Question**

=> Answer

- _Explanation_

***
**Question**

=> Answer

- _Explanation_