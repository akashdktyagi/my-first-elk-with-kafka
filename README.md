## My First ELK integration with Kafka

#### Some info to use this repo. These are note for my future self. But Mr Visitor you can also use the instructions.

* Start the ELK Stack with docker compose UP
```yml
version: '3.9'

services:
  elk:
    image: sebp/elk:latest
    ports:
      - "5601:5601"
      - "9200:9200"
      - "5044:5044"
    environment:
      - ELASTICSEARCH_USER=demo
      - ELASTICSEARCH_PASSWORD=demo
      - LOGSTASH_USER=demo
      - LOGSTASH_PASSWORD=demo
      - KIBANA_USER=demo
      - KIBANA_PASSWORD=demo

  ``` 
* Create a Confluent Kafka Cloud account and create a topic there. Also copy the client. properties from there user name and password etc and keep in  client.properties file
* Change the Topic name in the Client.properties. It should match the topic name what you have created in confluent kafka online
* Then Run Producer.java main class, it should start publishing the message to the COnfleunt Kafka
* Then Start running ConsumerElk.java. It should start reading the message from Kafka and start printing. Also, it will start injecting the meessage to ELK Stack
* Run localhost:5601 to see logs in kibana. Set a index from Stack Management and then go to discover.
