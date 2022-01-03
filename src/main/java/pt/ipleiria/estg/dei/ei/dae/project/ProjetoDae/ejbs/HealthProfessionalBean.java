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

//    public void update(HealthProfessional updateHealthProfessional) throws MyEntityNotFoundException {
//        em.merge(updateHealthProfessional);
//    }
    public void update(String username ,String name,String email,String profession, boolean active) throws MyEntityNotFoundException {
        HealthProfessional healthProfessional = em.find(HealthProfessional.class, username);

        if (healthProfessional != null) {
            em.lock(healthProfessional, LockModeType.OPTIMISTIC);
            healthProfessional.setProfession(profession);
            healthProfessional.setActive(active);
            //healthProfessional.setVersion(healthProfessional.getVersion()+1);
            healthProfessional.setEmail(email);
            healthProfessional.setName(name);
            healthProfessional.setUsername(username);
            healthProfessional.setPassword(healthProfessional.getPassword());
            //System.err.println("Username "+ healthProfessional.getUsername()+" name: "+healthProfessional.getName()+" profession: "+healthProfessional.getProfession()+" email: "+healthProfessional.getEmail());
            //System.err.println("setVersion "+ healthProfessional.getVersion()+" Role: "+healthProfessional.getRole()+" password: "+healthProfessional.getPassword());

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

}
