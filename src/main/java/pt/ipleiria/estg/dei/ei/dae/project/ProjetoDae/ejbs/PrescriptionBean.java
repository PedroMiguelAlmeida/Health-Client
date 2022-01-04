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
import javax.persistence.Query;
import javax.validation.ConstraintViolationException;
import java.util.ArrayList;
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

            Prescription prescription = new Prescription(healthProfessional, patient, treatment, description,0, true);
            em.persist(prescription);

            for (Measurement measurement : measurements) {
                addMeasurementToPrescription(prescription, measurement.getId());
            }

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

    public List<Prescription> findActivePrescription(int id) throws MyEntityNotFoundException{
        Query query = em.createQuery("SELECT c FROM Prescription c WHERE c.id = :id and c.active=true");
        query.setParameter("id", id);
        List<Prescription> prescriptions = query.getResultList();

        if(prescriptions == null)
            throw new MyEntityNotFoundException("Prescription with id: " + id + " not found");
        return prescriptions;
    }

    public void updatePrescription(int id, String healthProfessional_username, String patient_username, List<Measurement> measurements, List<String> treatment, String description) throws MyEntityNotFoundException, MyEntityExistsException {

            Prescription prescription = findPrescription(id);
            HealthProfessional healthProfessional = healthProfessionalBean.findHealthProfessional(healthProfessional_username);
            Patient patient = patientBean.findPatient(patient_username);

            em.lock(prescription, LockModeType.OPTIMISTIC);
            prescription.setHealthProfessional(healthProfessional);
            prescription.setPatient(patient);
            prescription.setTreatment(treatment);
            prescription.setDescription(description);


//            for (Measurement measurement : prescription.getMeasurements())
//                if (measurement.getPrescriptions().contains(prescription))
//                    measurement.removePrescription(prescription);
//
//            prescription.setMeasurements(new ArrayList<>());

            removeMeasurementsFromPrescription(prescription, prescription.getMeasurements());

            for (Measurement measurement : measurements)
                addMeasurementToPrescription(prescription, measurement.getId());
    }

    public void removePrescription(int id) throws MyEntityNotFoundException {
        Prescription prescription = em.find(Prescription.class, id);
        if (prescription == null)
            throw new MyEntityNotFoundException("Prescription with id " + id + " not found.");

        this.em.remove(prescription);
    }

    public void deletePrescription(int id) throws MyEntityNotFoundException {
        Prescription prescription = findPrescription(id);
        em.lock(prescription,LockModeType.OPTIMISTIC);
        prescription.setActive(false);

    }

    public void addMeasurementToPrescription(Prescription prescription, int measurement_id) throws MyEntityNotFoundException {
            Measurement measurement = measurementBean.findMeasurement(measurement_id);

            if (!measurement.getPrescriptions().contains(prescription))
                measurement.addPrescription(prescription);

            if (!prescription.getMeasurements().contains(measurement))
                prescription.addMeasurement(measurement);
    }

    public void removeMeasurementsFromPrescription(Prescription prescription, List<Measurement> measurements) throws MyEntityNotFoundException {
        for (Measurement measurement : measurements)
            if (measurement.getPrescriptions().contains(prescription))
                measurement.removePrescription(prescription);

        prescription.setMeasurements(new ArrayList<>());
    }
}
