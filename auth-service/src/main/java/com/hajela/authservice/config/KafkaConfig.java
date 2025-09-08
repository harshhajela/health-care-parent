package com.hajela.authservice.config;

import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.admin.AdminClientConfig;
import org.apache.kafka.clients.admin.NewTopic;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.common.serialization.StringSerializer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.kafka.core.DefaultKafkaProducerFactory;
import org.springframework.kafka.core.KafkaAdmin;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.core.ProducerFactory;
import org.springframework.kafka.support.serializer.JsonSerializer;

import java.util.HashMap;
import java.util.Map;

import static com.hajela.authservice.messaging.KafkaMessageProducer.FORGOT_PASSWORD_TOPIC;
import static com.hajela.authservice.messaging.KafkaMessageProducer.USER_ACTIVATION_TOPIC;

@Slf4j
@Configuration
public class KafkaConfig {

    @Value("${kafka.host}")
    private String kafkaHost;

    private static final int NUMBER_OF_PARTITIONS = 2;
    private static final short REPLICATION_FACTOR = 1;

    @Bean
    public NewTopic userActivationTopic() {
        return new NewTopic(USER_ACTIVATION_TOPIC, NUMBER_OF_PARTITIONS, REPLICATION_FACTOR);
    }

    @Bean
    public NewTopic forgotPasswordTopic() {
        return new NewTopic(FORGOT_PASSWORD_TOPIC, NUMBER_OF_PARTITIONS, REPLICATION_FACTOR);
    }

    @Bean
    public KafkaAdmin kafkaAdmin() {
        log.info("KAFKA_HOST[bootstrap.servers]={}", kafkaHost);
        Map<String, Object> configs = new HashMap<>();
        configs.put(AdminClientConfig.BOOTSTRAP_SERVERS_CONFIG, kafkaHost);
        return new KafkaAdmin(configs);
    }

    public Map<String,Object> producerConfig(){
        log.info("KAFKA_HOST={}", kafkaHost);
        Map<String,Object> props=new HashMap<>();
        props.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG,
                kafkaHost);
        props.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG,
                StringSerializer.class);
        props.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG,
                JsonSerializer.class);
        return props;
    }

    @Bean
    public ProducerFactory<String,Object> producerFactory(){
        return new DefaultKafkaProducerFactory<>(producerConfig());
    }

    @Bean
    public KafkaTemplate<String,Object> kafkaTemplate(){
        return new KafkaTemplate<>(producerFactory());
    }
}
