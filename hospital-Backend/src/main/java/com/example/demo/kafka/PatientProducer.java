package com.example.demo.kafka;



import com.example.demo.dto.PatientDto;

import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
public class PatientProducer {

    private static final String TOPIC = "patient-topic";

    private final KafkaTemplate<String, PatientDto> kafkaTemplate;

    public PatientProducer(KafkaTemplate<String, PatientDto> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }

    public void sendPatient(PatientDto dto) {
        kafkaTemplate.send(TOPIC, dto);
    }
}


