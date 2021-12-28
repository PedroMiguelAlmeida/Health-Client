package pt.ipleiria.estg.dei.ei.dae.project.ProjetoDae.ejbs;

import pt.ipleiria.estg.dei.ei.dae.project.ProjetoDae.MeasureTypeType;
import pt.ipleiria.estg.dei.ei.dae.project.ProjetoDae.entities.MeasureType;
import pt.ipleiria.estg.dei.ei.dae.project.ProjetoDae.entities.Measurement;
import pt.ipleiria.estg.dei.ei.dae.project.ProjetoDae.entities.QualitativeMeasureType;
import pt.ipleiria.estg.dei.ei.dae.project.ProjetoDae.entities.QuantitativeMeasureType;
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
public class QualitativeMeasureTypeBean {
    @EJB
    private MeasureTypeBean measureTypeBean;

    @PersistenceContext
    EntityManager em;

    public void create(String name, boolean multiple, List<String> values) throws MyEntityExistsException, MyConstraintViolationException {
        MeasureType measureType = measureTypeBean.findMeasureTypeByNameAndType(name, MeasureTypeType.Qualitative);
        if(measureType != null)
            throw new MyEntityExistsException("QuantitativeMeasureType with name: " + name + " already exists");

        try {
            QualitativeMeasureType qualitativeMeasureType = new QualitativeMeasureType(name, multiple, MeasureTypeType.Qualitative, values);
            em.persist(qualitativeMeasureType);
        } catch (ConstraintViolationException e) {
            throw new MyConstraintViolationException(e);
        }
    }

    public List<QualitativeMeasureType> getAllQualitativeMeasureTypes() {
        return em.createNamedQuery("getAllQualitativeMeasureTypes").getResultList();
    }

    public QualitativeMeasureType findQualitativeMeasureType(int id) {
        return em.find(QualitativeMeasureType.class, id);
    }

    public QualitativeMeasureType findQualitativeMeasureTypeByName(String name) {
        try {
            return (QualitativeMeasureType)em.createQuery("SELECT a FROM QualitativeMeasureType a where a.name = :name").setParameter("name", name).getSingleResult();
        } catch(NoResultException e) {
            return null;
        }
    }

    public void updateQualitativeMeasureType(String name, Boolean multiple, List<String> values) throws MyEntityNotFoundException {
        QualitativeMeasureType qualitativeMeasureType = em.find(QualitativeMeasureType.class, name);
        if (qualitativeMeasureType != null) {
            this.em.lock(qualitativeMeasureType, LockModeType.OPTIMISTIC);
            qualitativeMeasureType.setName(name);
            qualitativeMeasureType.setMultiple(multiple);
            qualitativeMeasureType.setValues(values);
        }
        throw new MyEntityNotFoundException("QualitativeMeasureType with name " + name + " not found.");
    }

    public void removeQualitativeMeasureType(String name) throws MyEntityNotFoundException {
        QualitativeMeasureType qualitativeMeasureType = em.find(QualitativeMeasureType.class, name);
        if (qualitativeMeasureType == null)
            throw new MyEntityNotFoundException("QualitativeMeasureType with name " + name + " not found.");

        this.em.remove(qualitativeMeasureType);
    }
}
