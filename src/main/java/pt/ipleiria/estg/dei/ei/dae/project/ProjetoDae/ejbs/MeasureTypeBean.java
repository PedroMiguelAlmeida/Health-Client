package pt.ipleiria.estg.dei.ei.dae.project.ProjetoDae.ejbs;

import pt.ipleiria.estg.dei.ei.dae.project.ProjetoDae.entities.MeasureType;
import pt.ipleiria.estg.dei.ei.dae.project.ProjetoDae.entities.MeasureType;
import pt.ipleiria.estg.dei.ei.dae.project.ProjetoDae.exceptions.MyConstraintViolationException;
import pt.ipleiria.estg.dei.ei.dae.project.ProjetoDae.exceptions.MyEntityExistsException;
import pt.ipleiria.estg.dei.ei.dae.project.ProjetoDae.exceptions.MyEntityNotFoundException;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.LockModeType;
import javax.persistence.PersistenceContext;
import java.util.List;

@Stateless
public class MeasureTypeBean {
    @PersistenceContext
    EntityManager em;

    public List<MeasureType> getAllMeasureTypes() {
        return em.createNamedQuery("getAllMeasureTypes").getResultList();
    }

    public MeasureType findMeasureType(int id) {
        return em.find(MeasureType.class, id);
    }
}
