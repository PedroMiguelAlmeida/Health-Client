
package pt.ipleiria.estg.dei.ei.dae.project.ProjetoDae.ejbs;

import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.LockModeType;
import javax.persistence.PersistenceContext;
import pt.ipleiria.estg.dei.ei.dae.project.ProjetoDae.entities.Patient;

@Stateless
public class PatientBean {
    @PersistenceContext
    EntityManager em;

    public PatientBean() {
    }

    public void create(String username, String password, String name, String email) {
        Patient patient = (Patient)this.em.find(Patient.class, username);
        if (patient != null) {
            System.out.println("Patient with username: " + username + " already exists");
            System.exit(0);
        }

        patient = new Patient(username, password, name, email, 0);
        this.em.persist(patient);
        if (patient == null) {
            System.out.println("ERROR! creating patient");
        }

    }

    public List<Patient> getAllPatients() {
        return this.em.createNamedQuery("getAllPatients").getResultList();
    }

    public Patient findPatient(String username) {
        return (Patient)this.em.find(Patient.class, username);
    }

    public void updatePatient(String username, String password, String name, String email) {
        Patient patient = (Patient)this.em.find(Patient.class, username);
        if (patient == null) {
            System.out.println("Patient with username " + username + " not found.");
            System.exit(0);
        }

        this.em.lock(patient, LockModeType.OPTIMISTIC);
        patient.setName(name);
        patient.setEmail(email);
        patient.setPassword(password);
    }

    public void removePatient(String username) {
        Patient patient = (Patient)this.em.find(Patient.class, username);
        if (patient == null) {
            System.out.println("Patient with username " + username + " not found.");
        }

        this.em.remove(patient);
    }
}
