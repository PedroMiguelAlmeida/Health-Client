
package pt.ipleiria.estg.dei.ei.dae.project.ProjetoDae.ejbs;

import java.util.List;
import java.util.Objects;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.mail.MessagingException;
import javax.persistence.EntityManager;
import javax.persistence.LockModeType;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.validation.ConstraintViolationException;
import javax.ws.rs.NotAuthorizedException;

import pt.ipleiria.estg.dei.ei.dae.project.ProjetoDae.Roles;
import pt.ipleiria.estg.dei.ei.dae.project.ProjetoDae.entities.HealthProfessional;
import pt.ipleiria.estg.dei.ei.dae.project.ProjetoDae.entities.Patient;
import pt.ipleiria.estg.dei.ei.dae.project.ProjetoDae.entities.Token;
import pt.ipleiria.estg.dei.ei.dae.project.ProjetoDae.exceptions.MyConstraintViolationException;
import pt.ipleiria.estg.dei.ei.dae.project.ProjetoDae.exceptions.MyEntityExistsException;
import pt.ipleiria.estg.dei.ei.dae.project.ProjetoDae.exceptions.MyEntityNotFoundException;

import static pt.ipleiria.estg.dei.ei.dae.project.ProjetoDae.entities.User.hashPassword;

@Stateless
public class PatientBean {
    @PersistenceContext
    EntityManager em;
    @EJB
    private EmailBean emailBean;
    @EJB
    private TokenBean tokenBean;

    public void create(String username, String password,String name, String email, Roles role, boolean active) throws MyEntityNotFoundException, MyEntityExistsException, MyConstraintViolationException, MessagingException {
        Patient patient = em.find(Patient.class, username);
        if(patient != null)
            throw new MyEntityExistsException("Patient with username: " + username + " already exists");

        try {
            patient = new Patient(username, password, name, email, 0, role,active);
            em.persist(patient);
            tokenBean.create(email);
            Token token = tokenBean.findToken(email);
            System.out.println("THIS IS EMAIL: "+ email);
            if (token == null){
                throw new MyEntityNotFoundException("token not found");
            }

            emailBean.send(email,"localhost:3000/"+token.getToken(),token.getToken());
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

//    public void updatePassword(String username,String password,String tokenString) throws MyEntityNotFoundException {
//        Patient patient = em.find(Patient.class,username);
//        if (patient==null){
//            throw new MyEntityNotFoundException("Patient not found");
//        }
//        Token token = tokenBean.findToken(patient.getEmail());
//        System.out.println("token1: "+token.getToken()+"token2: "+tokenString );
//        if (!Objects.equals(token.getToken(), tokenString)){
//            throw new NotAuthorizedException("Token was not found");
//        }
//        em.lock(patient,LockModeType.OPTIMISTIC);
//        patient.setPassword(password);
//    }
//
//    public void deleteToken(String username,String tokenString) throws MyEntityNotFoundException {
//        Patient patient = em.find(Patient.class,username);
//        if (patient==null){
//            throw new MyEntityNotFoundException("Patient not found");
//        }
//        Token token = tokenBean.findToken(patient.getEmail());
//        System.out.println("token1: "+token.getToken()+"token2: "+tokenString );
//        if (!Objects.equals(token.getToken(), tokenString)){
//            throw new NotAuthorizedException("Token was not found");
//        }
//        em.lock(token,LockModeType.PESSIMISTIC_WRITE);
//        tokenBean.delete(patient.getEmail());
//    }
//
//    public void sendEmailToChangePassword(String username) throws MyConstraintViolationException, MyEntityNotFoundException, MyEntityExistsException, MessagingException {
//        Patient patient = em.find(Patient.class,username);
//        if (patient==null){
//            throw new MyEntityNotFoundException("Patient not found");
//        }
//        tokenBean.create(patient.getEmail());
//        Token token = tokenBean.findToken(patient.getEmail());
//        System.out.println("This is email: "+ patient.getEmail());
//        if (token == null){
//            throw new MyEntityNotFoundException("token not found");
//        }
//        emailBean.send(patient.getEmail(), "localhost:3000/"+token.getToken(),token.getToken());
//
//    }
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
