package pt.ipleiria.estg.dei.ei.dae.project.ProjetoDae.ejbs;


import pt.ipleiria.estg.dei.ei.dae.project.ProjetoDae.MeasureTypeType;
import pt.ipleiria.estg.dei.ei.dae.project.ProjetoDae.entities.*;
import pt.ipleiria.estg.dei.ei.dae.project.ProjetoDae.exceptions.MyConstraintViolationException;
import pt.ipleiria.estg.dei.ei.dae.project.ProjetoDae.exceptions.MyEntityExistsException;
import pt.ipleiria.estg.dei.ei.dae.project.ProjetoDae.exceptions.MyEntityNotFoundException;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.LockModeType;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.validation.ConstraintViolationException;
import java.util.List;

@Stateless
public class QuantitativeMeasureTypeBean {

    @EJB
    private MeasureTypeBean measureTypeBean;

    @PersistenceContext
    EntityManager em;

    public void create(String name, boolean multiple, double min, double max, boolean decimal) throws MyEntityExistsException, MyConstraintViolationException {
        MeasureType measureType = measureTypeBean.findMeasureTypeByNameAndType(name, MeasureTypeType.Quantitative);
        if(measureType != null)
            throw new MyEntityExistsException("QuantitativeMeasureType with name: " + name + " already exists");

        try {
            QuantitativeMeasureType quantitativeMeasureType = new QuantitativeMeasureType(name, multiple, MeasureTypeType.Quantitative, 0, true, min, max, decimal);
            em.persist(quantitativeMeasureType);
        } catch (ConstraintViolationException e) {
            throw new MyConstraintViolationException(e);
        }
    }

    public List<QuantitativeMeasureType> getAllQuantitativeMeasureTypes() {
        return em.createNamedQuery("getAllQuantitativeMeasureTypes").getResultList();
    }

    public QuantitativeMeasureType findQuantitativeMeasureType(int id) throws MyEntityNotFoundException {
        QuantitativeMeasureType quantitativeMeasureType = em.find(QuantitativeMeasureType.class, id);
        if(quantitativeMeasureType == null)
            throw new MyEntityNotFoundException("QuantitativeMeasureType with id: " + id + " not found");
        return quantitativeMeasureType;
    }

    public Prescription findPrescription(int id) throws MyEntityNotFoundException {
        Prescription prescription = em.find(Prescription.class, id);
        if(prescription == null)
            throw new MyEntityNotFoundException("Prescription with id: " + id + " not found");
        return prescription;
    }

    public void updateQuantitativeMeasureType(int id, String name, Boolean multiple, double min, double max, boolean decimal) throws MyEntityNotFoundException, MyConstraintViolationException {
        try {
            QuantitativeMeasureType quantitativeMeasureType = findQuantitativeMeasureType(id);

            em.lock(quantitativeMeasureType, LockModeType.OPTIMISTIC);
            quantitativeMeasureType.setName(name);
            quantitativeMeasureType.setMultiple(multiple);
            quantitativeMeasureType.setMin(min);
            quantitativeMeasureType.setMax(max);
            quantitativeMeasureType.setDecimal(decimal);

        }catch (ConstraintViolationException e) {
            throw new MyConstraintViolationException(e);
        }
    }

    public void removeQuantitativeMeasureType(String name) throws MyEntityNotFoundException {
        QuantitativeMeasureType quantitativeMeasureType = em.find(QuantitativeMeasureType.class, name);
        if (quantitativeMeasureType == null)
            throw new MyEntityNotFoundException("QuantitativeMeasureType with name " + name + " not found.");

        this.em.remove(quantitativeMeasureType);
    }

    private class T {
    }
}
