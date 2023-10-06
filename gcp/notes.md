## Big Data and ML on GCP

### Compute

- Compute Engine: IaaS offering. Compute, Network, Storage, Max flexibility
- GKE (Google Kubernetes Engine): Runs containerized apps in a Cloud environment. A container represents packaged up 
  with all its dependencies.
- App Engine: PaaS. Bind code to libraries that provide access to the infra application needs.
- Cloud Functions: FaaS. Executes code in response to events. Serverless execution environment.
- Cloud Runs: Automatically scales up/down. Charges only for resources you use.
    
### Storage

- Unstructured Data -> Cloud Storage or BigTable
  In Cloud Storage, we store objects (images, files...) in buckets which belong to a project.
- Structured Data
  - OLTP (fast inserts and updates)
    - SQL -> Cloud SQL / Cloud Spanner
    - NoSQL -> Firestore
  - OLAP (analytics, querying)
    - SQL -> BigQuery
    - NoSQL -> BigTable

### Product Categories

#### Ingestion & process
- Pub/Sub
- Dataflow
- Dataproc
- Cloud Data fusion

#### Storage
- Cloud Storage
- Cloud SQL
- Cloud Spanner
- Cloud BigTable
- Firestore

#### Analytics
- BigQuery
- Looker
- Looker Studio

#### Machine Learning & AI
- Vertex AI
- AutoML
- Vertex AI Workbench
- TensorFlow
- Document AI
- Contact Center AI
- Retail Product Discovery
- Healthcare Data Engine

## Data Engineering For Streaming Data

### Big Data Challenges

- 4Vs:
  - Variety
  - Volume
  - Velocity
  - Veracity

### Designing streaming pipelines with Apache Beam

- Apache Beam has the following characteristics:
  - Unified: Uses a single programming model for both batch and streaming data
  - Portable: Can work on multiple execution environments, like Dataflow or Spark
  - Extensible: Allows to write and share your own connectors and transformation libraries

### Implementing streaming pipelines on Cloud Dataflow

- Dataflow is designed to be low maintenance:
  - NoOps
  - Serverless

- Job stage[Google Certified Professional Data Engineer PDF.pdf](..%2F..%2FDownloads%2FGoogle%20Certified%20Professional%20Data%20Engineer%20PDF.pdf)s:
  - Graph optimization
  - Work scheduler
  - Auto-scaler
  - Auto-healing
  - Work re-balancing
  - Compute & Storage

- Dataflow templates:
  - Streaming: Pub/Sub to BigQuery, Pub/Sub to Cloud Storage, Datastream to BigQuery...
  - Batch: BigQuery to Cloud Storage...
  - Utility: Bulk Compression of Cloud Storage files, Firestore bulk delete, File format conversion...

## Big Data withb BigQuery

- Predicting on: 
  - Numeric value -> Consider Linear Regression for forecasting
  - Discrete class (like high, low...) -> Consider Logistic Regression for classification
