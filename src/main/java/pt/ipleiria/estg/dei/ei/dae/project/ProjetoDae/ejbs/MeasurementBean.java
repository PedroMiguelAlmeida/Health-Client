package pt.ipleiria.estg.dei.ei.dae.project.ProjetoDae.ejbs;

import pt.ipleiria.estg.dei.ei.dae.project.ProjetoDae.Roles;
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
public class MeasurementBean {
    @PersistenceContext
    EntityManager em;

    @EJB
    PatientBean patientBean;

    @EJB
    MeasureTypeBean measureTypeBean;


    public void create(int measureTypeId, String value, String inputSource, String username) throws MyEntityNotFoundException, MyEntityExistsException, MyConstraintViolationException {
        try {
            Patient patient = patientBean.findPatient(username);
            MeasureType measureType = measureTypeBean.findMeasureType(measureTypeId);

            Measurement measurement = new Measurement(measureType, value, inputSource, patient, 0, true);
            em.persist(measurement);

            if (!measureType.getMeasurements().contains(measurement))
                measureType.addMeasurement(measurement);

        } catch (ConstraintViolationException e) {
            throw new MyConstraintViolationException(e);
        }
    }

    public List<Measurement> getAllMeasurements() {
        return em.createNamedQuery("getAllMeasurements").getResultList();
    }

    public Measurement findMeasurement(int id) throws MyEntityNotFoundException {
        Measurement measurement = em.find(Measurement.class, id);
        if(measurement == null)
            throw new MyEntityNotFoundException("Measurement with id: " + id + " not found");
        return measurement;
    }
    public List<Measurement> findMeasurementByPatient(String username) throws MyEntityNotFoundException {
        List<Measurement> measurements = em.createQuery("SELECT p FROM Measurement p WHERE p.patient.username = :username")
                .setParameter("username",username).getResultList();

        if(measurements == null)
            throw new MyEntityNotFoundException("Measurement with username: " + username + " not found");
        return measurements;
    }

    public void updateMeasurement(int id, int measureTypeId, String value, String inputSource, String username) throws MyEntityNotFoundException, MyConstraintViolationException {
        try {
            Measurement measurement = findMeasurement(id);
            MeasureType measureType = measureTypeBean.findMeasureType(measureTypeId);
            Patient patient = patientBean.findPatient(username);

            if (measurement.getMeasureType().getMeasurements().contains(measurement))
                measureType.removeMeasurement(measurement);

            em.lock(measurement, LockModeType.OPTIMISTIC);
            measurement.setMeasureType(measureType);
            measurement.setValue(value);
            measurement.setInputSource(inputSource);
            measurement.setPatient(patient);

            if (!measureType.getMeasurements().contains(measurement))
                measureType.addMeasurement(measurement);

        }catch (ConstraintViolationException e) {
            throw new MyConstraintViolationException(e);
        }
    }

    public void removeMeasurement(int id) throws MyEntityNotFoundException {
        Measurement measurement = em.find(Measurement.class, id);
        if (measurement == null)
            throw new MyEntityNotFoundException("Measurement with id " + id + " not found.");

        this.em.remove(measurement);
    }

    public void deleteMeasurement(int id) throws MyEntityNotFoundException {
        Measurement measurement = findMeasurement(id);
        em.lock(measurement,LockModeType.OPTIMISTIC);
        measurement.setActive(false);

    }
}
