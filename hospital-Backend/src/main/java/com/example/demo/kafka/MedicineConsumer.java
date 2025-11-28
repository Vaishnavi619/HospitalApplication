package com.example.demo.kafka;

import com.example.demo.dto.MedicineEvent;
import com.example.demo.entity.Medicine;
import com.example.demo.repository.MedicineRepository;

import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
public class MedicineConsumer {

    private final MedicineRepository medicineRepository;

    public MedicineConsumer(MedicineRepository medicineRepository) {
        this.medicineRepository = medicineRepository;
    }

    @KafkaListener(
        topics = "bulk-medicine-upload",
        groupId = "medicine-group",
        containerFactory = "medicineKafkaListenerFactory"
    )
    public void consume(MedicineEvent event) {
        // ✅ Optional duplicate check by name
        if (medicineRepository.findByName(event.getName()).isPresent()) {
            System.out.println("⚠️ Duplicate medicine skipped: " + event.getName());
            return;
        }

        Medicine medicine = new Medicine();
        medicine.setName(event.getName());
        medicine.setPrice(event.getPrice());
        medicine.setDiscontinued(event.isDiscontinued());
        medicine.setManufacturerName(event.getManufacturerName());
        medicine.setType(event.getType());
        medicine.setPackSizeLabel(event.getPackSizeLabel());
        medicine.setShortComposition1(event.getShortComposition1());
        medicine.setShortComposition2(event.getShortComposition2());

        medicineRepository.save(medicine);
        System.out.println("✅ Saved medicine: " + medicine.getName());
    }
}

