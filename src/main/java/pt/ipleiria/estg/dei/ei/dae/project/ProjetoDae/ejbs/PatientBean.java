
package pt.ipleiria.estg.dei.ei.dae.project.ProjetoDae.ejbs;

import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.LockModeType;
import javax.persistence.PersistenceContext;
import javax.validation.ConstraintViolationException;
import pt.ipleiria.estg.dei.ei.dae.project.ProjetoDae.Roles;
import pt.ipleiria.estg.dei.ei.dae.project.ProjetoDae.entities.Administrator;
import pt.ipleiria.estg.dei.ei.dae.project.ProjetoDae.entities.Patient;
import pt.ipleiria.estg.dei.ei.dae.project.ProjetoDae.exceptions.MyConstraintViolationException;
import pt.ipleiria.estg.dei.ei.dae.project.ProjetoDae.exceptions.MyEntityExistsException;
import pt.ipleiria.estg.dei.ei.dae.project.ProjetoDae.exceptions.MyEntityNotFoundException;

@Stateless
public class PatientBean {
    @PersistenceContext
    EntityManager em;

    public void create(String username, String password, String name, String email, Roles role,boolean active) throws MyEntityNotFoundException, MyEntityExistsException, MyConstraintViolationException {
        Patient patient = em.find(Patient.class, username);
        if(patient != null)
            throw new MyEntityExistsException("Student with username: " + username + " already exists");

        try {
            patient = new Patient(username, password, name, email, 0, role,active);
            em.persist(patient);
        } catch (ConstraintViolationException e) {
            throw new MyConstraintViolationException(e);
        }
    }

    public List<Patient> getAllPatients() {
        return em.createNamedQuery("getAllPatients").getResultList();
    }

    public Patient findPatient(String username) {
        return em.find(Patient.class, username);
    }

    public void updatePatient(Patient updatePatient) throws MyEntityNotFoundException {
        em.merge(updatePatient);
    }


    public void delete(Patient deletePatient) {
        if (!em.contains(deletePatient)){
            deletePatient=em.merge(deletePatient);
        }
        em.remove(deletePatient);
    }
}
