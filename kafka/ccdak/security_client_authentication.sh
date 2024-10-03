# 1. Enable Client Authentication for the Broker:
ssl.client.auth=required
sudo systemctl restart confluent-kafka
sudo systemctl status confluent-kafka

# 2. Generate a client certificate:
keytool -keystore client.keystore.jks -alias kafkauser -validity 365 \
-genkey -keyalg RSA -dname "CN=kafkauser, OU=Unknown, O=Unknown, L=Unknown, ST=Unknown, C=Unknown"

# 3. Sign the key, then import the certificate authority and signed key into the keystore:
keytool -keystore client.keystore.jks -alias kafkauser -certreq -file client-cert-file

openssl x509 -req -CA ca-cert -CAkey ca-key -in client-cert-file -out client-cert-signed -days 365 -CAcreateserial

keytool -keystore client.keystore.jks -alias CARoot -import -file ca-cert

keytool -keystore client.keystore.jks -alias kafkauser -import -file client-cert-signed

# 4. Move the client keystore into an appropriate location:
sudo cp client.keystore.jks /var/private/ssl/
sudo chown root:root /var/private/ssl/client.keystore.jks

# 5. Add Client Authentication Settings to Your Client Config File:
ssl.keystore.location=/var/private/ssl/client.keystore.jks
ssl.keystore.password=<your client keystore password>
ssl.key.password=<your client key password>

# 6. Create a console consumer using client authentication:
kafka-console-consumer --bootstrap-server zoo1:9093 --topic inventory_purchases \
--from-beginning --consumer.config client-ssl.properties
