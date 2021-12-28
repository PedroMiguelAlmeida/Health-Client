package pt.ipleiria.estg.dei.ei.dae.project.ProjetoDae.ejbs;

import pt.ipleiria.estg.dei.ei.dae.project.ProjetoDae.MeasureTypeType;
import pt.ipleiria.estg.dei.ei.dae.project.ProjetoDae.entities.MeasureType;
import pt.ipleiria.estg.dei.ei.dae.project.ProjetoDae.entities.MeasureType;
import pt.ipleiria.estg.dei.ei.dae.project.ProjetoDae.entities.QuantitativeMeasureType;
import pt.ipleiria.estg.dei.ei.dae.project.ProjetoDae.entities.User;
import pt.ipleiria.estg.dei.ei.dae.project.ProjetoDae.exceptions.MyConstraintViolationException;
import pt.ipleiria.estg.dei.ei.dae.project.ProjetoDae.exceptions.MyEntityExistsException;
import pt.ipleiria.estg.dei.ei.dae.project.ProjetoDae.exceptions.MyEntityNotFoundException;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.LockModeType;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import java.util.List;

@Stateless
public class MeasureTypeBean {
    @PersistenceContext
    EntityManager em;

    public List<MeasureType> getAllMeasureTypes() {
        return em.createNamedQuery("getAllMeasureTypes").getResultList();
    }

    public MeasureType findMeasureType(int id) throws MyEntityNotFoundException {
        MeasureType measureType = em.find(MeasureType.class, id);
        if(measureType == null)
            throw new MyEntityNotFoundException("MeasureType with id: " + id + " not found");
        return measureType;
    }

    public MeasureType findMeasureTypeByNameAndType(String name, MeasureTypeType type) {
        try{
            return (QuantitativeMeasureType)this.em.createQuery("SELECT a FROM MeasureType a WHERE a.name = :name AND a.type = :type")
                    .setParameter("name", name)
                    .setParameter("type", type)
                    .getSingleResult();
        } catch(NoResultException e) {
            return null;
        }
    }
}
