package pt.ipleiria.estg.dei.ei.dae.project.ProjetoDae.dtos;

import io.smallrye.common.constraint.NotNull;
import io.smallrye.common.constraint.Nullable;
import pt.ipleiria.estg.dei.ei.dae.project.ProjetoDae.entities.HealthProfessional;
import pt.ipleiria.estg.dei.ei.dae.project.ProjetoDae.entities.Measurement;
import pt.ipleiria.estg.dei.ei.dae.project.ProjetoDae.entities.Patient;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

public class PrescriptionDTO {


    private int id;
    private String healthProfessional_username;
    private String patient_username;
    private List<Measurement> measurements;
    private List<String> treatment;
    private String description;

    public PrescriptionDTO() {
    }

    public PrescriptionDTO(int id, String healthProfessional_username, String patient_username, List<Measurement> measurements, List<String> treatment, String description) {
        this.id = id;
        this.healthProfessional_username = healthProfessional_username;
        this.patient_username = patient_username;
        this.measurements = new ArrayList<>();
        this.treatment = treatment;
        this.description = description;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getHealthProfessional_username() {
        return healthProfessional_username;
    }

    public void setHealthProfessional_username(String healthProfessional_username) {
        this.healthProfessional_username = healthProfessional_username;
    }

    public String getPatient_username() {
        return patient_username;
    }

    public void setPatient_username(String patient_username) {
        this.patient_username = patient_username;
    }

    public List<Measurement> getMeasurements() {
        return measurements;
    }

    public void setMeasurements(List<Measurement> measurements) {
        this.measurements = measurements;
    }

    public List<String> getTreatment() {
        return treatment;
    }

    public void setTreatment(List<String> treatment) {
        this.treatment = treatment;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
