package pt.ipleiria.estg.dei.ei.dae.project.ProjetoDae.ejbs;


import org.jboss.resteasy.core.PathParamInjector;
import pt.ipleiria.estg.dei.ei.dae.project.ProjetoDae.Roles;
import pt.ipleiria.estg.dei.ei.dae.project.ProjetoDae.entities.Administrator;
import pt.ipleiria.estg.dei.ei.dae.project.ProjetoDae.entities.HealthProfessional;
import pt.ipleiria.estg.dei.ei.dae.project.ProjetoDae.entities.Patient;
import pt.ipleiria.estg.dei.ei.dae.project.ProjetoDae.entities.Token;
import pt.ipleiria.estg.dei.ei.dae.project.ProjetoDae.exceptions.MyConstraintViolationException;
import pt.ipleiria.estg.dei.ei.dae.project.ProjetoDae.exceptions.MyEntityExistsException;
import pt.ipleiria.estg.dei.ei.dae.project.ProjetoDae.exceptions.MyEntityNotFoundException;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.mail.MessagingException;
import javax.persistence.EntityManager;
import javax.persistence.LockModeType;
import javax.persistence.PersistenceContext;
import javax.validation.ConstraintViolationException;
import javax.ws.rs.NotAuthorizedException;
import java.util.List;
import java.util.Objects;

@Stateless
public class HealthProfessionalBean {
    @PersistenceContext
    private EntityManager em;
    @EJB
    private EmailBean emailBean;
    @EJB
    private TokenBean tokenBean;


    public void create(String username, String password, String name, String email, int version, String profession, boolean chefe, Roles role,boolean active) throws MyConstraintViolationException, MyEntityNotFoundException, MyEntityExistsException, MessagingException {

        HealthProfessional professional = (HealthProfessional)this.em.find(HealthProfessional.class, username);
        if (professional != null) {
            System.out.println("HealthProfessional with username: " + username + " already exists");
            System.exit(0);
        }
        try {
            professional = new HealthProfessional(username, password, name, email, version, profession, chefe, role, active);
            em.persist(professional);
            tokenBean.create(email);
            Token token = tokenBean.findToken(email);
            System.out.println("This is email: "+ email);
            if (token == null){
                throw new MyEntityNotFoundException("token not found");
            }
            emailBean.send(email,"localhost:3000/"+token.getToken(),token.getToken());
        }
        catch (ConstraintViolationException e){
            throw new MyConstraintViolationException(e);
        }
    }

    public void update(String username ,String name,String email,String profession, boolean active) throws MyEntityNotFoundException {
        HealthProfessional healthProfessional = em.find(HealthProfessional.class, username);

        if (healthProfessional != null) {
            em.lock(healthProfessional, LockModeType.OPTIMISTIC);
            healthProfessional.setProfession(profession);
            healthProfessional.setActive(active);
            healthProfessional.setEmail(email);
            healthProfessional.setName(name);
            healthProfessional.setUsername(username);
            healthProfessional.setPassword(healthProfessional.getPassword());

        }else{
            System.err.println("ERROR_FINDING_HEALTHPROFESSIONAL");
        }
    }


    public List<HealthProfessional> getAllHealthProfessionals(){
        return (List<HealthProfessional>) em.createNamedQuery("getAllHealthProfessionals").getResultList();
    }

    public void updatePassword(String username,String password,String tokenString) throws MyEntityNotFoundException {
        HealthProfessional healthProfessional = em.find(HealthProfessional.class,username);
        if (healthProfessional==null){
            throw new MyEntityNotFoundException("HealthProfessional not found");
        }
        Token token = tokenBean.findToken(healthProfessional.getEmail());
        System.out.println("token1: "+token.getToken()+"token2: "+tokenString );
        if (!Objects.equals(token.getToken(), tokenString)){
            throw new NotAuthorizedException("Token was not found");
        }
        em.lock(healthProfessional,LockModeType.OPTIMISTIC);
        healthProfessional.setPassword(password);
    }

    public void deleteToken(String username,String tokenString) throws MyEntityNotFoundException {
        HealthProfessional healthProfessional = em.find(HealthProfessional.class,username);
        if (healthProfessional==null){
            throw new MyEntityNotFoundException("Health Professional not found");
        }
        Token token = tokenBean.findToken(healthProfessional.getEmail());
        System.out.println("token1: "+token.getToken()+"token2: "+tokenString );
        if (!Objects.equals(token.getToken(), tokenString)){
            throw new NotAuthorizedException("Token was not found");
        }
        em.lock(token,LockModeType.PESSIMISTIC_WRITE);
        tokenBean.delete(healthProfessional.getEmail());
    }

    public void sendEmailToChangePassword(String username) throws MyConstraintViolationException, MyEntityNotFoundException, MyEntityExistsException, MessagingException {
        HealthProfessional healthProfessional = em.find(HealthProfessional.class,username);
        if (healthProfessional==null){
            throw new MyEntityNotFoundException("HealthProfessional not found");
        }
        tokenBean.create(healthProfessional.getEmail());
        Token token = tokenBean.findToken(healthProfessional.getEmail());
        System.out.println("This is email: "+ healthProfessional.getEmail());
        if (token == null){
            throw new MyEntityNotFoundException("token not found");
        }
        emailBean.send(healthProfessional.getEmail(), "localhost:3000/"+token.getToken(),token.getToken());

    }
    public HealthProfessional findHealthProfessional(String username) throws MyEntityNotFoundException{
        HealthProfessional healthProfessional = this.em.find(HealthProfessional.class,username);
        if(healthProfessional == null)
            throw new MyEntityNotFoundException("User with username: " + username + " not found");
        return healthProfessional;
    }

    public void delete(String username) {
        HealthProfessional healthProfessional = em.find(HealthProfessional.class, username);
        if (healthProfessional != null) {
            em.lock(healthProfessional,LockModeType.OPTIMISTIC);
            healthProfessional.setActive(false);
        }else {
            System.err.println("ERROR_DELETING_PROFESSIONAL");
        }
    }

    public void signPatients(String healthProfessionalUsername, String patientUsername) throws MyEntityNotFoundException {
        HealthProfessional healthProfessional = findHealthProfessional(healthProfessionalUsername);
        if (healthProfessional == null){
            System.out.println("The health professional doesn't exist");
            return;
        }
        Patient patient = em.find(Patient.class,patientUsername);
        if (patient == null){
            System.out.println("The Patient doesn't exist");
            return;
        }
        if(!healthProfessional.getPatients().contains(patient)) {
            healthProfessional.addPatient(patient);
        }
        if (!patient.getHealthProfessionals().contains(healthProfessional)){
            patient.addHealthProfessional(healthProfessional);
        }
    }

    public void unsignPatients(String healthProfessionalUsername,String patientUsername) throws MyEntityNotFoundException {
        HealthProfessional healthProfessional = findHealthProfessional(healthProfessionalUsername);
        if (healthProfessional == null){
            System.out.println("The health professional you are trying to unsign doesn't exist");
            return;
        }
        Patient patient = em.find(Patient.class,patientUsername);
        if (patient == null){
            System.out.println("The patient  you are trying to unsign doesn't exist");
            return;
        }

        if(healthProfessional.getPatients().contains(patient)) {
            healthProfessional.removePatient(patient);
        }
        if (patient.getHealthProfessionals().contains(healthProfessional)){
            patient.removeHealthProfessionals(healthProfessional);
        }
    }


}
