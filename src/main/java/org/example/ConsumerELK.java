package org.example;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.common.serialization.StringDeserializer;

import java.io.IOException;
import java.io.InputStream;
import java.time.Duration;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;


public class ConsumerELK {

    static String _topicName;
    public static void main(String[] args) throws IOException {

        ConsumerELK consumerMain = new ConsumerELK();
        Properties props = consumerMain.loadConfig("client.properties");
        props.put(ConsumerConfig.GROUP_ID_CONFIG, "kafka-java-getting-started");
        props.put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, "earliest");
        props.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class.getName());
        props.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class.getName());
        _topicName  = props.getProperty("topicName");

        KafkaConsumer<String, String> consumer = new KafkaConsumer<>(props);
        consumer.subscribe(Arrays.asList(_topicName));
        while (true) {
            ConsumerRecords<String, String> records = consumer.poll(Duration.ofMillis(100));
            for (ConsumerRecord<String, String> record : records) {
                System.out.printf("key = %s, value = %s%n", record.key(), record.value());
                injectMessagesToELK(record.value());
            }
        }
    }

    public Properties loadConfig(final String configFile) throws IOException {
        InputStream is = this.getClass().getClassLoader().getResourceAsStream("client.properties");
        Properties properties = new Properties();
        properties.load(is);
        return properties;
    }


    public static void injectMessagesToELK(String msg){
        RestAssured
                .given()
                .contentType(ContentType.JSON)
                .and()
                .body(msg)
                .when()
                .post("http://localhost:9200/"+_topicName+"/cf")
                .then()
                .assertThat()
                .statusCode(201);
    }



}