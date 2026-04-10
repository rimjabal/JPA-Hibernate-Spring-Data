package com.example.demo;

import com.example.demo.entities.AppRole;
import com.example.demo.entities.AppUser;
import com.example.demo.entities.Consultation;
import com.example.demo.entities.Medecin;
import com.example.demo.entities.Patient;
import com.example.demo.entities.Product;
import com.example.demo.entities.RendezVous;
import com.example.demo.entities.StatusRDV;
import com.example.demo.repositories.MedecinRepository;
import com.example.demo.repositories.PatientRepository;
import com.example.demo.repositories.ProductRepository;
import com.example.demo.repositories.RendezVousRepository;
import com.example.demo.service.IHospitalService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

@SpringBootApplication
public class DemoApplication {

    public static void main(String[] args) {
        SpringApplication.run(DemoApplication.class, args);
    }

    @Bean
    CommandLineRunner productDemo(ProductRepository productRepository) {
        return args -> {
            if (productRepository.count() == 0) {
                productRepository.save(new Product(null, "Computer", 4300, 3));
                productRepository.save(new Product(null, "Printer", 1200, 7));
                productRepository.save(new Product(null, "Smartphone", 2500, 12));
                productRepository.save(new Product(null, "Tablet", 2100, 5));
            }

            System.out.println("========== Product demo ==========");
            productRepository.findAll().forEach(product ->
                    System.out.println(product.getId() + " " + product.getName() + " " + product.getPrice()));

            Optional<Product> productOptional = productRepository.findById(1L);
            productOptional.ifPresent(product ->
                    System.out.println("Product found: " + product.getName()));

            System.out.println("Search 't':");
            productRepository.findByNameContainsIgnoreCase("t").forEach(product ->
                    System.out.println(product.getName()));

            productOptional.ifPresent(product -> {
                product.setPrice(product.getPrice() + 100);
                productRepository.save(product);
                System.out.println("Updated product " + product.getId() + " price=" + product.getPrice());
            });

            if (productRepository.findByNameContainsIgnoreCase("Temporary").isEmpty()) {
                Product temporary = productRepository.save(new Product(null, "Temporary Product", 99, 1));
                productRepository.deleteById(temporary.getId());
                System.out.println("Temporary product deleted: " + temporary.getId());
            }
        };
    }

    @Bean
    CommandLineRunner hospitalDemo(IHospitalService hospitalService,
                                   PatientRepository patientRepository,
                                   MedecinRepository medecinRepository,
                                   RendezVousRepository rendezVousRepository) {
        return args -> {
            if (patientRepository.count() == 0 && medecinRepository.count() == 0 && rendezVousRepository.count() == 0) {
                List<Patient> patients = new ArrayList<>();
                Stream.of("Rim", "Hassan", "Yasmine").forEach(name -> {
                    Patient patient = new Patient();
                    patient.setNom(name);
                    patient.setDateNaissance(new Date());
                    patient.setMalade(false);
                    patients.add(hospitalService.savePatient(patient));
                });

                List<Medecin> medecins = new ArrayList<>();
                Stream.of("Dr Amine", "Dr Sara", "Dr Hanane").forEach(name -> {
                    Medecin medecin = new Medecin();
                    medecin.setNom(name);
                    medecin.setEmail(name.replace(" ", "") + "@gmail.com");
                    medecin.setSpecialite(Math.random() > 0.5 ? "Cardio" : "Dentiste");
                    medecins.add(hospitalService.saveMedecin(medecin));
                });

                RendezVous rendezVous = new RendezVous();
                rendezVous.setDate(new Date());
                rendezVous.setStatus(StatusRDV.PENDING);
                rendezVous.setPatient(patients.get(0));
                rendezVous.setMedecin(medecins.get(0));
                RendezVous savedRendezVous = hospitalService.saveRDV(rendezVous);

                Consultation consultation = new Consultation();
                consultation.setDateConsultation(new Date());
                consultation.setRapport("Rapport de la consultation medicale...");
                consultation.setRendezVous(savedRendezVous);
                hospitalService.saveConsultation(consultation);

                System.out.println("Hospital demo initialized.");
            }

            if (patientRepository.count() > 0 || medecinRepository.count() > 0) {
                System.out.println("Patients: " + patientRepository.count());
                System.out.println("Medecins: " + medecinRepository.count());
                System.out.println("RendezVous: " + rendezVousRepository.count());
            }
        };
    }

    @Bean
    CommandLineRunner securityDemo(IHospitalService hospitalService) {
        return args -> {
            trySaveRole(hospitalService, "USER");
            trySaveRole(hospitalService, "ADMIN");
            trySaveRole(hospitalService, "STUDENT");

            trySaveUser(hospitalService, "user1", "1234");
            trySaveUser(hospitalService, "admin", "1234");
            trySaveUser(hospitalService, "student", "1234");

            tryAddRole(hospitalService, "user1", "USER");
            tryAddRole(hospitalService, "admin", "USER");
            tryAddRole(hospitalService, "admin", "ADMIN");
            tryAddRole(hospitalService, "student", "STUDENT");
        };
    }

    private void trySaveRole(IHospitalService hospitalService, String roleName) {
        try {
            AppRole appRole = new AppRole();
            appRole.setRoleName(roleName);
            hospitalService.saveRole(appRole);
        } catch (Exception ignored) {
        }
    }

    private void trySaveUser(IHospitalService hospitalService, String username, String password) {
        try {
            AppUser appUser = new AppUser();
            appUser.setUsername(username);
            appUser.setPassword(password);
            hospitalService.saveUser(appUser);
        } catch (Exception ignored) {
        }
    }

    private void tryAddRole(IHospitalService hospitalService, String username, String roleName) {
        try {
            hospitalService.addRoleToUser(username, roleName);
        } catch (Exception ignored) {
        }
    }
}
