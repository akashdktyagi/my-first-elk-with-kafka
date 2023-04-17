package org.example;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.javafaker.service.FakeValuesService;
import com.github.javafaker.service.RandomService;
import org.apache.kafka.clients.producer.*;

import java.io.IOException;
import java.io.InputStream;
import java.util.Locale;
import java.util.Properties;
import java.util.UUID;

public class ProducerMain {

    static String _topicName;
    public static void main(String[] args) throws IOException, InterruptedException {


        ProducerMain producerMain = new ProducerMain();
        Properties props = producerMain.loadConfig("client.properties");
        _topicName  = props.getProperty("topicName");
                //This is important, i these are not there , then it throws error
        props.put("key.serializer", "org.apache.kafka.common.serialization.StringSerializer");
        props.put("value.serializer", "org.apache.kafka.common.serialization.StringSerializer");

        Producer<String, String> producer = new KafkaProducer<>(props);
        int counter=0;
        while (counter<100000){
            producer.send(new ProducerRecord<>(_topicName, UUID.randomUUID().toString(), createRandomDataString()));
            counter = counter + 1;
            Thread.sleep(1000);
        }

        producer.close();
    }

    public Properties loadConfig(final String configFile) throws IOException {
        InputStream is = this.getClass().getClassLoader().getResourceAsStream("client.properties");
        Properties properties = new Properties();
        properties.load(is);
        return properties;
    }


    public static String createRandomDataString() throws JsonProcessingException {
        FakeValuesService fake = new FakeValuesService(
                new Locale("en-GB"), new RandomService());

        DataModel dataModel = DataModel.builder()
                .projectID(fake.regexify("[1-9]{1}"))
                .sha(fake.regexify("[a-z1-9]{10}"))
                .coverage(Integer.parseInt(fake.regexify("[1-9]{2}")))
                .sastCritical(Integer.parseInt(fake.regexify("[1-9]{2}")))
                .sastLow(Integer.parseInt(fake.regexify("[1-9]{3}")))
                .sastMedium(Integer.parseInt(fake.regexify("[1-9]{3}")))
                .testCount(Integer.parseInt(fake.regexify("[1-9]{3}")))
                .vulnerabilities(Integer.parseInt(fake.regexify("[1-9]{3}")))
                .testFailed(Integer.parseInt(fake.regexify("[1-9]{2}")))
                .build();

        ObjectMapper objectMapper = new ObjectMapper();
        return objectMapper.writeValueAsString(dataModel);


    }










}