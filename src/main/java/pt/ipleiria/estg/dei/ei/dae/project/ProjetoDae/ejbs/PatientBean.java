
package pt.ipleiria.estg.dei.ei.dae.project.ProjetoDae.ejbs;

import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.LockModeType;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
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
            throw new MyEntityExistsException("Patient with username: " + username + " already exists");

        try {
            patient = new Patient(username, password, name, email, 0, role, active);
            em.persist(patient);
        } catch (ConstraintViolationException e) {
            throw new MyConstraintViolationException(e);
        }
    }

    public List<Patient> getAllPatients() {
        return em.createNamedQuery("getAllPatients").getResultList();
    }

    public Patient findPatient(String username) throws MyEntityNotFoundException{
        Patient patient = em.find(Patient.class, username);
        if(patient == null)
            throw new MyEntityNotFoundException("User with username: " + username + " not found");
        return patient;
    }
    public List<Patient> findActivePatient(String username) throws MyEntityNotFoundException{
        Query query = em.createQuery("SELECT c FROM User c" + " WHERE c.username = :username and c.active=true");
        query.setParameter("username",username);
        List<Patient> patients= query.getResultList();
        System.err.println("Patient query: " +patients);

        return patients;
//        if(patient == null)
//            throw new MyEntityNotFoundException("User with username: " + username + " not found");
//        return patient;
    }


    public void update(String username ,String name,String email, boolean active) throws MyEntityNotFoundException {
        Patient patient = em.find(Patient.class, username);
        if (patient != null) {
            em.lock(patient, LockModeType.OPTIMISTIC);
            patient.setActive(active);
            patient.setEmail(email);
            patient.setName(name);
            patient.setUsername(username);
            patient.setPassword(patient.getPassword());
        }else{
            System.err.println("ERROR_FINDING_PATIENT");
        }

    }
//    public void updatePatient(Patient updatePatient) throws MyEntityNotFoundException {
//        em.lock(updatePatient,LockModeType.OPTIMISTIC);
//        em.merge(updatePatient);
//    }


    public void delete(String username) {
        Patient deletePatient = em.find(Patient.class, username);
        if (deletePatient != null) {
            em.lock(deletePatient,LockModeType.OPTIMISTIC);
            deletePatient.setActive(false);
        }else {
            System.err.println("ERROR_DELETING_PATIENT");
        }
    }
}
