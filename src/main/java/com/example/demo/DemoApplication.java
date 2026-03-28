package com.example.demo;

import com.example.demo.entities.*;
import com.example.demo.repositories.MedecinRepository;
import com.example.demo.repositories.PatientRepository;
import com.example.demo.repositories.RendezVousRepository;
import com.example.demo.service.IHospitalService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import java.util.Date;
import java.util.stream.Stream;

@SpringBootApplication
public class DemoApplication {

    public static void main(String[] args) {
        SpringApplication.run(DemoApplication.class, args);
    }

    @Bean
    CommandLineRunner start(IHospitalService hospitalService,
                            PatientRepository patientRepository,
                            MedecinRepository medecinRepository,
                            RendezVousRepository rendezVousRepository) {
        return args -> {
            // 1. Création de Patients
            Stream.of("Rim", "Hassan", "Yasmine").forEach(name -> {
                Patient p = new Patient();
                p.setNom(name);
                p.setDateNaissance(new Date());
                p.setMalade(false);
                hospitalService.savePatient(p);
            });

            // 2. Création de Médecins
            Stream.of("Dr Amine", "Dr Sara", "Dr Hanane").forEach(name -> {
                Medecin m = new Medecin();
                m.setNom(name);
                m.setEmail(name.replace(" ", "") + "@gmail.com");
                m.setSpecialite(Math.random() > 0.5 ? "Cardio" : "Dentiste");
                hospitalService.saveMedecin(m);
            });

            // 3. Création d'un Rendez-vous (Liaison Patient + Médecin)
            // On récupère un patient et un médecin existants en base
            Patient patient = patientRepository.findById(1L).orElse(null);
            Medecin medecin = medecinRepository.findByNom("Dr Amine");

            RendezVous rdv = new RendezVous();
            rdv.setDate(new Date());
            rdv.setStatus(StatusRDV.PENDING);
            rdv.setPatient(patient);
            rdv.setMedecin(medecin);
            hospitalService.saveRDV(rdv); // Le service va générer l'UUID ici

            // 4. Création d'une Consultation pour ce RDV
            // On récupère le RDV qu'on vient de créer (le premier de la liste)
            RendezVous rdvFromDB = rendezVousRepository.findAll().get(0);

            Consultation consultation = new Consultation();
            consultation.setDateConsultation(new Date());
            consultation.setRapport("Rapport de la consultation médicale...");
            consultation.setRendezVous(rdvFromDB);
            hospitalService.saveConsultation(consultation);

            System.out.println("Base de données Hospitalière initialisée avec succès !");
        };
    }
}