curl -X POST http://localhost:8083/connectors \
-H 'Accept: */*' \
-H 'Content-Type: application/json' \
-d '{
"name": "db_source_connector",
"config": {
    "connector.class": "io.confluent.connect.jdbc.JdbcSourceConnector",
    "connection.url": "jdbc:postgresql://10.0.1.102:5432/inventory",
    "connection.user": "kafka",
    "connection.password": "Kafka!",
    "topic.prefix": "postgres-",
    "mode":"timestamp",
    "timestamp.column.name": "update_ts"
  }
}'
