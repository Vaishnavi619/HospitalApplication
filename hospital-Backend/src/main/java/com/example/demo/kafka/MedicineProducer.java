package com.example.demo.kafka;

import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import com.example.demo.dto.MedicineEvent;

@Service
public class MedicineProducer {

    private static final String TOPIC = "bulk-medicine-upload";

    private final KafkaTemplate<String, MedicineEvent> kafkaTemplate;

    public MedicineProducer(KafkaTemplate<String, MedicineEvent> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }

    public void sendMedicine(MedicineEvent event) {
        kafkaTemplate.send(TOPIC, event);
    }
}
