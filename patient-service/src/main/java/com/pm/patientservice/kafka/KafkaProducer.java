package com.pm.patientservice.kafka;

import com.pm.patientservice.model.Patient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
import patient.events.PatientEvent;

@Service
public class KafkaProducer {
    private static final Logger log = LoggerFactory.getLogger(KafkaProducer.class);
    private final KafkaTemplate<String,byte[]> kafkaTemplate;
    // IntelliJ false positive: Spring Boot 4 moved Kafka auto-configuration into the
    // spring-boot-kafka module, and the IDE's static analysis doesn't see the
    // auto-configured KafkaTemplate<?, ?> bean. It exists at runtime.
    @SuppressWarnings("SpringJavaInjectionPointsAutowiringInspection")
    public KafkaProducer(KafkaTemplate<String,byte[]> _kafkaTemplate){
        this.kafkaTemplate = _kafkaTemplate;
    }
    public void sendEvent(Patient patient){
        PatientEvent event = PatientEvent.newBuilder()
                .setPatientId(patient.getId().toString())
                .setName(patient.getName())
                .setEmail(patient.getEmail())
                .setEventType("PATIENT_CREATED")
                .build();
        try{
            kafkaTemplate.send("patient",event.toByteArray());
        } catch (Exception e) {
            log.error("Error sending PatientCreated event:{}", event);
        }

    }


}
