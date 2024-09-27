# Create a consumer for JSON data in "my_json_consumer_group" consumer group, starting at the beginning of the topic's
# log and subscribe to a topic. Then consume some data using the base URL in the first response.
# Finally, close the consumer with a DELETE to make it leave the group and clean up
# its resources.
curl -X POST -H "Content-Type: application/vnd.kafka.v2+json" \
--data '{"name": "my_consumer_instance", "format": "json", "auto.offset.reset": "earliest"}' \
http://localhost:8082/consumers/my_json_consumer_group

curl -X POST -H "Content-Type: application/vnd.kafka.v2+json" \
--data '{"topics":["inventory_purchases"]}' \
http://localhost:8082/consumers/my_json_consumer_group/instances/my_consumer_instance/subscription

curl -X GET -H "Accept: application/vnd.kafka.json.v2+json" \
http://localhost:8082/consumers/my_json_consumer_group/instances/my_consumer_instance/records

curl -X DELETE -H "Content-Type: application/vnd.kafka.v2+json" \
http://localhost:8082/consumers/my_json_consumer_group/instances/my_consumer_instance