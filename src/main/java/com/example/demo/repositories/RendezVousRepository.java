package com.example.demo.repositories;

import com.example.demo.entities.RendezVous;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RendezVousRepository extends JpaRepository<RendezVous, String> {
    // Le type de l'ID est String ici !
}