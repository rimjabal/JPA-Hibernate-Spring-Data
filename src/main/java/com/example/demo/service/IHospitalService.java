package com.example.demo.service;

import com.example.demo.entities.*;

public interface IHospitalService {
    Patient savePatient(Patient patient);
    Medecin saveMedecin(Medecin medecin);
    RendezVous saveRDV(RendezVous rdv);
    Consultation saveConsultation(Consultation consultation);
}