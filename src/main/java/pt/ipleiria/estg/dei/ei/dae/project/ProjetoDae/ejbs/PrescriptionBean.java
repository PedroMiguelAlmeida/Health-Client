package pt.ipleiria.estg.dei.ei.dae.project.ProjetoDae.ejbs;

import pt.ipleiria.estg.dei.ei.dae.project.ProjetoDae.entities.*;
import pt.ipleiria.estg.dei.ei.dae.project.ProjetoDae.exceptions.MyConstraintViolationException;
import pt.ipleiria.estg.dei.ei.dae.project.ProjetoDae.exceptions.MyEntityExistsException;
import pt.ipleiria.estg.dei.ei.dae.project.ProjetoDae.exceptions.MyEntityNotFoundException;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.LockModeType;
import javax.persistence.PersistenceContext;
import javax.validation.ConstraintViolationException;
import java.util.List;

@Stateless
public class PrescriptionBean {
    @PersistenceContext
    EntityManager em;

    @EJB
    PatientBean patientBean;

    @EJB
    HealthProfessionalBean healthProfessionalBean;

    @EJB
    MeasurementBean measurementBean;

    public void create(String healthProfessional_username, String patient_username, List<Measurement> measurements, List<String> treatment, String description) throws MyEntityNotFoundException, MyEntityExistsException, MyConstraintViolationException {
        try {
            HealthProfessional healthProfessional = healthProfessionalBean.findHealthProfessional(healthProfessional_username);
            Patient patient = patientBean.findPatient(patient_username);

            for (Measurement measurement : measurements) {
                measurementBean.findMeasurement(measurement.getId());
            }

            Prescription prescription = new Prescription(healthProfessional, patient, measurements, treatment, description);
            em.persist(prescription);
        } catch (ConstraintViolationException e) {
            throw new MyConstraintViolationException(e);
        }
    }

    public List<Prescription> getAllPrescriptions() {
        return em.createNamedQuery("getAllPrescriptions").getResultList();
    }

    public Prescription findPrescription(int id) throws MyEntityNotFoundException {
        Prescription prescription = em.find(Prescription.class, id);
        if(prescription == null)
            throw new MyEntityNotFoundException("Prescription with id: " + id + " not found");
        return prescription;
    }

    public void updatePrescription(Prescription updatedPrescription) throws MyEntityNotFoundException, MyConstraintViolationException {
        try {
            Prescription prescription = findPrescription(updatedPrescription.getId());
            Patient patient = patientBean.findPatient(updatedPrescription.getPatient().getUsername());
            HealthProfessional healthProfessional = healthProfessionalBean.findHealthProfessional(updatedPrescription.getHealthProfessional().getUsername());

            em.lock(updatedPrescription, LockModeType.OPTIMISTIC);
            em.merge(updatedPrescription);
        }catch (ConstraintViolationException e) {
            throw new MyConstraintViolationException(e);
        }
    }

    public void removePrescription(int id) throws MyEntityNotFoundException {
        Prescription prescription = em.find(Prescription.class, id);
        if (prescription == null)
            throw new MyEntityNotFoundException("Prescription with id " + id + " not found.");

        this.em.remove(prescription);
    }

}