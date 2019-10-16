package kafka.tutorial;

import org.apache.kafka.clients.producer.*;
import org.apache.kafka.common.serialization.StringSerializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Properties;

public class ProducerWithCallBack {
    public static void main(String[] args) {
        //System.out.println("hello world!");

        final Logger logger = LoggerFactory.getLogger(ProducerWithCallBack.class);

        //Set Producers proerties
        String server = "127.0.0.1:9092";
        Properties properties = new Properties();
        properties.setProperty(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG,server);
        properties.setProperty(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class.getName());
        properties.setProperty(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG,StringSerializer.class.getName());

        //create the producer
        KafkaProducer<String,String> producer = new KafkaProducer<String, String>(properties);

        ProducerRecord<String,String> record =
                new ProducerRecord<String, String>("first_topic", "hello world");

        //send message

        producer.send(record, new Callback() {
            public void onCompletion(RecordMetadata recordMetadata, Exception e) {
                if (e==null){
                    logger.info("Receiver new matadata: \n"+
                            "Topic: "+recordMetadata.topic()+"\n"+
                            "Parition: "+ recordMetadata.partition()+"\n");

                } else {
                    logger.error("Error:" ,e);
                }
            }
        });

        producer.flush();
        producer.close();







    }

}
