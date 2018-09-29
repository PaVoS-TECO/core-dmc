package controller;




import static org.apache.kafka.clients.consumer.ConsumerConfig.AUTO_COMMIT_INTERVAL_MS_CONFIG;
import static org.apache.kafka.clients.consumer.ConsumerConfig.ENABLE_AUTO_COMMIT_CONFIG;
import static org.apache.kafka.clients.consumer.ConsumerConfig.GROUP_ID_CONFIG;
import static org.apache.kafka.clients.consumer.ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG;
import static org.apache.kafka.clients.consumer.ConsumerConfig.SESSION_TIMEOUT_MS_CONFIG;
import static org.apache.kafka.streams.StreamsConfig.BOOTSTRAP_SERVERS_CONFIG;

import java.util.Arrays;
import java.util.Properties;
import java.util.UUID;

import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

public class Exporttester {

public static  int counter = 0;

    public static void main(String[] args) throws InterruptedException {

        //Client Props
        Properties props = new Properties();
        props.put(BOOTSTRAP_SERVERS_CONFIG, "pavos.oliver.pw:9092");
        props.put(GROUP_ID_CONFIG, "i");
        props.put(ENABLE_AUTO_COMMIT_CONFIG, "true");
        props.put(AUTO_COMMIT_INTERVAL_MS_CONFIG, "1000");
        props.put(SESSION_TIMEOUT_MS_CONFIG, "30000");
        props.put(KEY_DESERIALIZER_CLASS_CONFIG, "org.apache.kafka.common.serialization.StringDeserializer");
        props.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG,
                "org.apache.kafka.common.serialization.StringDeserializer");

        //props.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG,
        //        KafkaAvroDeserializer.class.getName());
        props.put(ConsumerConfig.GROUP_ID_CONFIG, UUID.randomUUID().toString());
        props.put(ConsumerConfig.CLIENT_ID_CONFIG, "your_client_id");
        props.put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, "earliest");
        props.put("schema.registry.url", "http://pavos.oliver.pw:8081");

        @SuppressWarnings("resource")
		KafkaConsumer<String, String> consumer = new KafkaConsumer<>(props);

        consumer.subscribe(Arrays.asList("AvroExport"));

        System.out.println("Consumer A gestartet!");

        while (true) {

            final ConsumerRecords<String, String> foi =
                    consumer.poll(100);
            foi.forEach(record1 -> {

            	try {
					JSONObject obs = (JSONObject) new JSONParser().parse(record1.value());
					new JSONParser().parse((String) obs.get("FeatureOfInterest"));
                    JSONObject dataS =  (JSONObject) new JSONParser().parse((String) obs.get("Datastream"));
                    new JSONParser().parse((String) dataS.get("Thing"));
                    new JSONParser().parse((String) dataS.get("Sensor"));
                    new JSONParser().parse((String) dataS.get("ObservedProperty"));
                   System.out.println(obs.get("iotId"));
				} catch (ParseException e) {
					e.printStackTrace();
				}
            });
    }
}}