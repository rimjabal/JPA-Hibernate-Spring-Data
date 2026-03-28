package com.example.demo.repositories;

import com.example.demo.entities.Medecin;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MedecinRepository extends JpaRepository<Medecin, Long> {
    // Méthode pour chercher un médecin par son nom
    Medecin findByNom(String name);
}