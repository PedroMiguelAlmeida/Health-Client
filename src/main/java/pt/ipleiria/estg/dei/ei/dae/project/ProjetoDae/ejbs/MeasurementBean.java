package pt.ipleiria.estg.dei.ei.dae.project.ProjetoDae.ejbs;

import pt.ipleiria.estg.dei.ei.dae.project.ProjetoDae.Roles;
import pt.ipleiria.estg.dei.ei.dae.project.ProjetoDae.entities.MeasureType;
import pt.ipleiria.estg.dei.ei.dae.project.ProjetoDae.entities.Measurement;
import pt.ipleiria.estg.dei.ei.dae.project.ProjetoDae.entities.Patient;
import pt.ipleiria.estg.dei.ei.dae.project.ProjetoDae.entities.User;
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
    UserBean userBean;

    @EJB
    MeasureTypeBean measureTypeBean;

    public void create(int measureTypeId, String value, String inputSource, String username) throws MyEntityNotFoundException, MyEntityExistsException, MyConstraintViolationException {
        try {
            User user = userBean.findUser(username);
            MeasureType measureType = measureTypeBean.findMeasureType(measureTypeId);

            Measurement measurement = new Measurement(measureType, value, inputSource, user);
            em.persist(measurement);
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

    public void updateMeasurement(Measurement updatedMeasurement) throws MyEntityNotFoundException, MyConstraintViolationException {
        try {
            Measurement measurement = findMeasurement(updatedMeasurement.getId());
            User user = userBean.findUser(updatedMeasurement.getUser().getUsername());
            MeasureType measureType = measureTypeBean.findMeasureType(updatedMeasurement.getMeasureType().getId());

            em.lock(updatedMeasurement, LockModeType.OPTIMISTIC);
            em.merge(updatedMeasurement);
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
}
