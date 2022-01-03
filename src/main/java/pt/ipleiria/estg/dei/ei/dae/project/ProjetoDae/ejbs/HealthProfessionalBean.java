package pt.ipleiria.estg.dei.ei.dae.project.ProjetoDae.ejbs;


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

    public void update(String username,String name,String email,String profession, boolean active) throws MyEntityNotFoundException {
        HealthProfessional healthProfessional = em.find(HealthProfessional.class, username);
        if (healthProfessional != null) {
            em.lock(healthProfessional, LockModeType.OPTIMISTIC);
            healthProfessional.setProfession(profession);
            healthProfessional.setActive(active);
            healthProfessional.setVersion(healthProfessional.getVersion()+1);
            healthProfessional.setEmail(email);
            healthProfessional.setName(name);
            healthProfessional.setUsername(username);
        }

    }

    public List<HealthProfessional> getAllHealthProfessionals(){
        return (List<HealthProfessional>) em.createNamedQuery("getAllHealthProfessionals").getResultList();
    }

    public HealthProfessional findHealthProfessional(String username){return (HealthProfessional)this.em.find(HealthProfessional.class,username);}
    public void delete(HealthProfessional deleteHealthProfessional) {
        if (!em.contains(deleteHealthProfessional)){
            deleteHealthProfessional=em.merge(deleteHealthProfessional);
        }
        em.remove(deleteHealthProfessional);
    }

    public void signPatients(HealthProfessional healthProfessional, Patient patientToSign,String patientUsername) throws MyEntityNotFoundException {
        if (patientToSign.getName() == "none"){return;}
        if (findHealthProfessional(healthProfessional.getUsername())==null){
            System.out.println("The health professional doesn't exist");
            return;
        }
        if (patientToSign.getUsername()!=patientUsername){
            System.out.println("The Patient doesn't exist");
            return;
        }
        patientToSign.addHealthProfessional(getAllHealthProfessionals(),healthProfessional);
    }

    public void unsignPatients(HealthProfessional healthProfessional,Patient patientToUnsign,String patientUsername) throws MyEntityNotFoundException {
        if (findHealthProfessional(healthProfessional.getUsername())==null){
            System.out.println("The health professional you are trying to unroll doesn't exist");
            return;
        }
        if (patientToUnsign.getUsername()!=patientUsername){
            System.out.println("The patient  you are trying to unroll doesn't exist");
            return;
        }
        for (Patient patients: healthProfessional.getPatients()){patients.removeHealthProfessionals(healthProfessional);}
    }


}
