package com.example.demo.repositories;

import com.example.demo.entities.Patient;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PatientRepository extends JpaRepository<Patient, Long> {
    // Spring génère automatiquement : save(), findAll(), findById(), delete()...

    // Ajout d'une méthode personnalisée pour chercher par nom
    Patient findByNom(String name);
}