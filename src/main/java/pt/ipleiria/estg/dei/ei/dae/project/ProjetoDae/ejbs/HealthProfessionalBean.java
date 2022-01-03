package pt.ipleiria.estg.dei.ei.dae.project.ProjetoDae.ejbs;


import org.jboss.resteasy.core.PathParamInjector;
import pt.ipleiria.estg.dei.ei.dae.project.ProjetoDae.Roles;
import pt.ipleiria.estg.dei.ei.dae.project.ProjetoDae.entities.Administrator;
import pt.ipleiria.estg.dei.ei.dae.project.ProjetoDae.entities.HealthProfessional;
import pt.ipleiria.estg.dei.ei.dae.project.ProjetoDae.entities.Patient;
import pt.ipleiria.estg.dei.ei.dae.project.ProjetoDae.exceptions.MyEntityNotFoundException;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.LockModeType;
import javax.persistence.PersistenceContext;
import java.util.List;

@Stateless
public class HealthProfessionalBean {
    @PersistenceContext
    private EntityManager em;
    public void create(String username, String password, String name, String email, int version, String profession, boolean chefe, Roles role,boolean active){

        HealthProfessional professional = (HealthProfessional)this.em.find(HealthProfessional.class, username);
        if (professional != null) {
            System.out.println("HealthProfessional with username: " + username + " already exists");
            System.exit(0);
        }
         professional = new HealthProfessional(username,password,name,email,version,profession,chefe,role,active);
        em.persist(professional);
        if(professional == null){
            System.out.println("ERROR! creating Professional");
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

    public void signPatients(String healthProfessionalUsername, String patientUsername) {
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
