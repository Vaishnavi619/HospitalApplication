package com.example.demo.jwt.config;

import com.example.demo.dto.MedicineEvent;
import com.example.demo.dto.PatientDto;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.apache.kafka.common.serialization.StringSerializer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.annotation.EnableKafka;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.core.*;
import org.springframework.kafka.support.serializer.JsonDeserializer;
import org.springframework.kafka.support.serializer.JsonSerializer;

import java.util.HashMap;
import java.util.Map;

@EnableKafka
@Configuration
public class KafkaConfig {

    // ===================== MEDICINE =======================

    @Bean
    public ConsumerFactory<String, MedicineEvent> medicineConsumerFactory() {
        JsonDeserializer<MedicineEvent> deserializer = new JsonDeserializer<>(MedicineEvent.class);
        deserializer.addTrustedPackages("*");

        Map<String, Object> props = new HashMap<>();
        props.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, "localhost:9092");
        props.put(ConsumerConfig.GROUP_ID_CONFIG, "medicine-group");

        return new DefaultKafkaConsumerFactory<>(props, new StringDeserializer(), deserializer);
    }

    @Bean
    public ConcurrentKafkaListenerContainerFactory<String, MedicineEvent> medicineKafkaListenerFactory() {
        ConcurrentKafkaListenerContainerFactory<String, MedicineEvent> factory = new ConcurrentKafkaListenerContainerFactory<>();
        factory.setConsumerFactory(medicineConsumerFactory());
        return factory;
    }

    @Bean
    public ProducerFactory<String, MedicineEvent> medicineProducerFactory() {
        Map<String, Object> config = new HashMap<>();
        config.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, "localhost:9092");
        config.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
        config.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, JsonSerializer.class);
        return new DefaultKafkaProducerFactory<>(config);
    }

    @Bean
    public KafkaTemplate<String, MedicineEvent> medicineKafkaTemplate() {
        return new KafkaTemplate<>(medicineProducerFactory());
    }

    // ===================== PATIENT =======================

    @Bean
    public ConsumerFactory<String, PatientDto> patientConsumerFactory() {
        JsonDeserializer<PatientDto> deserializer = new JsonDeserializer<>(PatientDto.class);
        deserializer.addTrustedPackages("*");

        Map<String, Object> props = new HashMap<>();
        props.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, "localhost:9092");
        props.put(ConsumerConfig.GROUP_ID_CONFIG, "patient-group");

        return new DefaultKafkaConsumerFactory<>(props, new StringDeserializer(), deserializer);
    }

    @Bean
    public ConcurrentKafkaListenerContainerFactory<String, PatientDto> patientKafkaListenerFactory() {
        ConcurrentKafkaListenerContainerFactory<String, PatientDto> factory = new ConcurrentKafkaListenerContainerFactory<>();
        factory.setConsumerFactory(patientConsumerFactory());
        return factory;
    }

    @Bean
    public ProducerFactory<String, PatientDto> patientProducerFactory() {
        Map<String, Object> config = new HashMap<>();
        config.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, "localhost:9092");
        config.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
        config.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, JsonSerializer.class);
        return new DefaultKafkaProducerFactory<>(config);
    }

    @Bean
    public KafkaTemplate<String, PatientDto> patientKafkaTemplate() {
        return new KafkaTemplate<>(patientProducerFactory());
    }
}
