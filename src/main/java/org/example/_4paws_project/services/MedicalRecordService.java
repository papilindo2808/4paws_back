package org.example._4paws_project.services;

import org.example._4paws_project.models.MedicalRecord;
import org.example._4paws_project.repositories.MedicalRecordRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class MedicalRecordService {

    @Autowired
    private MedicalRecordRepository medicalRecordRepository;

    public List<MedicalRecord> getAllMedicalRecords() {
        return medicalRecordRepository.findAll();
    }

    public Optional<MedicalRecord> getMedicalRecordById(Long id) {
        return medicalRecordRepository.findById(id);
    }

    public MedicalRecord addMedicalRecord(MedicalRecord medicalRecord) {
        return medicalRecordRepository.save(medicalRecord);
    }

    public Optional<MedicalRecord> updateMedicalRecord(Long id, MedicalRecord medicalRecordDetails) {
        return medicalRecordRepository.findById(id).map(medicalRecord -> {
            medicalRecord.setDescription(medicalRecordDetails.getDescription());
            medicalRecord.setDate(medicalRecordDetails.getDate());
            medicalRecord.setAnimal(medicalRecordDetails.getAnimal());
            return medicalRecordRepository.save(medicalRecord);
        });
    }

    public void deleteMedicalRecord(Long id) {
        medicalRecordRepository.deleteById(id);
    }
}