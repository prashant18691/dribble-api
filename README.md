# Dribble API with Kafka Integration

  **Steps to follow**
  
  **Create DB tables**
  
  Create tables using the script available under resource folder(resources/templates/createTableScripts.txt).
  
1. Start **Zookeeper** using **bin/zookeeper-server-start.sh config/zookeeper.properties**.
2. Start **Kafka** server using **bin/kafka-server-start.sh config/server.properties**.
3. Create Kafka topic using **bin/kafka-topics.sh --create --zookeeper localhost:2181 --replication-factor 1 --partitions 1 --topic            kafka_dribble_topic**.
4. Start the server which is available at http://localhost:8081/dribble/. You can also access the api using swagger link at http://localhost:8081/swagger-ui.html#!/Dribble_API_Rest_Controller/.
5. DB design, Json Data and data excel file are available under \src\main\resources\templates\(dbDesign.JPG,jsonData.json, data.xlsx).
 

