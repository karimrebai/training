# Add read / write permission to specific users
kafka-acls --authorizer-properties zookeeper.connect=localhost:2181 \
  --add \
  --allow-principal User:kafkauser \
  --operation read \
  --operation write \
  --topic inventory_purchases

kafka-console-consumer --bootstrap-server zoo1:9093 \
  --topic inventory_purchases \
  --from-beginning \
  --consumer.config client-ssl.properties

# List permissions
kafka-acls --authorizer-properties zookeeper.connect=localhost:2181 \
  --topic member_signups \
  --list

# Remove permission
# If allow.everyone.if.no.acl.found is set to "true", everyone can access to the topic after removal of all ACLs
kafka-acls --authorizer-properties zookeeper.connect=localhost:2181 \
  --topic member_signups \
  --remove
