- Airflow is an orchestrator, not a processing framework, process your gigabytes of data outside of Airflow 
(i.e. You have a Spark cluster, you use an operator to execute a Spark job, the data is processed in Spark)

- A DAG is a data pipeline, an Operator is a task

- Airflow is composed by:
  - Scheduler
  - Executor
  - Workers
  - Webserver
  - Metadata Database

- An Executor defines how your tasks are executed whereas a worker is a process executing your task

- The scheduler schedules your tasks, the web server serves the UI, the database stores the metadata of Airflow

- How to install Airflow: `pip3 install apache-airflow==2.1.0 --constraint constraint.txt`

- `airflow db init` is the first command to execute to initialise Airflow

- If a task fails, check the logs by clicking on the task from the UI and "Logs"

- The Gantt view is useful to spot bottlenecks and tasks that are too long to execute

- Define ONLY ONE task per Operator

- 3 types of Operators:
  - Action Operators: Execute an action
  - Transfer Operators: Transfer data
  - Sensors: Wait for a condition to be met

- Test a task: `airflow tasks test dag_id task_id start_date`

- DAG scheduling:
  - start_date (2020-01-01 10am)
  - schedule_interval: how often the DAG should be run from the start_date + schedule_time (ex: 10min)
  - The pipeline will be triggered once start_date and schedule_interval are elapsed (triggered at 10:10)

- In a case of a pause of a DAG for example, by default airflow will trigger all non triggered Dag Runs since THE LAST
EXECUTION DATE after resuming.
This behaviour is enabled by the option: `catchup=True`
Ex:
Let's assume a DAG with a start_date to the 28/10/2021:10:00:00 PM UTC and the DAG is turned on at 10:30:00 PM UTC
with a schedule_interval of */10 * * * * (After every 10 minutes).
Then DAG will run twice, the first DAG Run will run at 10:10 then 10:20.

- By default, we can only execute one task at a time. This is because of those 2 options:
  - `airflow config get-value core sql_alchemy_conn` => sqlite:////home/krebai/airflow/airflow.db (It's not possible
  to make multiple writes at the same time with SQLite)
  - `airflow config get-value core executor` => SequentialExecutor
=> To run parallel tasks we should use another db (like Postgres) and we should use the LocalExecutor which will run
multiple tasks as subprocesses in parallel on the same machine.

- LocalExecutor allows running parallel tasks in a single machine so the resources are limited. If there is a need to
scale, we should use CeleryExecutor or KubernetesExecutor.

- To run tasks in the right order, airflow Executors use queues.

- The CeleryExecutor allows tasks to run on multiple machines with the help of Redis (in memory database used as 
message broker system) which is outside the Executor. Each time we execute a task, it's pushed into Redis and then a
worker will pull out the task from it.
Scheduler -> Redis -> Workers

- Important concurrency parameters:
  - `worker_concurrency` (in airflow.cfg): The concurrency that will be used when starting workers with the
  `airflow celery worker` command. This defines the number of task instances that a worker will take, so size up your
  workers based on the resources on your worker box and the nature of your tasks
  - `parallelism` (in airflow.cfg): This defines the maximum number of task instances that can run concurrently in
  Airflow regardless of scheduler count and worker count.
  - `dag_concurrency` (in airflow.cfg): The maximum number of task instances allowed to run concurrently in each DAG.
  - `concurrency` (DAG parameter): Same as `dag_concurrency` but applied only to a specific DAG.
  - `max_active_runs_per_dag`: The maximum number of active DAG runs per DAG. The scheduler will not create more DAG
  runs if it reaches the limit. This is configurable at the DAG level with ``max_active_runs``, which is defaulted as
  ``max_active_runs_per_dag``.

- Subdags
  - A subdag must have the same start_date and schedule_interval as its parent dag
  - It's not recommended to use subdags because of 3 reasons:
    - Deadlocks
    - Complexity
    - SequentialExecutor: they use sequential executor by default and it's not recommended to change it.

- TaskGroup: Use TaskGroups instead of Subdags

- XCom: Cross communication
  - Allows exchanging SMALL amount of data
  - XCom are limited in size depending on the database:
    - SQLite: 2 GB
    - Postgres: 1 GB
    - Mysql: 64 KB
  

