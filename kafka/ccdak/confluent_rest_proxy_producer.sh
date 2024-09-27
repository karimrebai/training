curl -X POST \
-H "Content-Type: application/vnd.kafka.json.v2+json" \
-H "Accept: application/vnd.kafka.v2+json" \
--data '{ "records": [{"key": "apples", "value": "23"}, {"key": "grapes", "value": "160"}] }' \
http://localhost:8082/topics/inventory_purchases
