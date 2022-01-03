package pt.ipleiria.estg.dei.ei.dae.project.ProjetoDae.entities;

import io.smallrye.common.constraint.NotNull;
import io.smallrye.common.constraint.Nullable;

import javax.persistence.*;
import java.io.Serializable;
import java.util.List;


@Table(
        name = "PRESCRIPTION"
)
@NamedQueries({@NamedQuery(
        name = "getAllPrescriptions",
        query = "SELECT p FROM Prescription p ORDER BY p.id"
)})
@Entity
public class Prescription implements Serializable {

    @Id @GeneratedValue
    private int id;

    @NotNull @ManyToOne @JoinColumn(name = "HEALTHPROFESSIONAL_USERNAME")
    private HealthProfessional healthProfessional;

    @NotNull @ManyToOne @JoinColumn(name = "PATIENT_USERNAME")
    private Patient patient;

    @OneToMany(mappedBy = "prescription", cascade = CascadeType.REMOVE)
    private List<Measurement> measurements;

    @Nullable @ElementCollection
    private List<String> treatment;

    @NotNull
    private String description;

    public Prescription() {
    }

    public Prescription(HealthProfessional healthProfessional, Patient patient, List<Measurement> measurements, List<String> treatment, String description) {
        this.healthProfessional = healthProfessional;
        this.patient = patient;
        this.measurements = measurements;
        this.treatment = treatment;
        this.description = description;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public HealthProfessional getHealthProfessional() {
        return healthProfessional;
    }

    public void setHealthProfessional(HealthProfessional healthProfessional) {
        this.healthProfessional = healthProfessional;
    }

    public Patient getPatient() {
        return patient;
    }

    public void setPatient(Patient patient) {
        this.patient = patient;
    }

    public List<Measurement> getMeasurements() {
        return measurements;
    }

    public void setMeasurements(List<Measurement> measurements) {
        this.measurements = measurements;
    }

    public void addMeasurement(Measurement measurement) {
        this.measurements.add(measurement);
    }

    public void removeMeasurement(Measurement measurement) {
        this.measurements.remove(measurement);
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
