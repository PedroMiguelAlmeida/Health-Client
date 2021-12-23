package pt.ipleiria.estg.dei.ei.dae.project.ProjetoDae.ejbs;


import pt.ipleiria.estg.dei.ei.dae.project.ProjetoDae.entities.HealthProfessional;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.List;

@Stateless
public class HealthProfessionalBean {
    @PersistenceContext
    private EntityManager em;
    public void create(String username,String password,String name,String email,int version,String profession,boolean chefe){
        HealthProfessional healthProfessional = new HealthProfessional(username,password,name,email
        ,version,profession,chefe);
        em.persist(healthProfessional);
    }

    public List<HealthProfessional> getAllHealthProfessionals(){
        return this.em.createNamedQuery("getAllHealthProfessionals").getResultList();
    }

    public HealthProfessional findHealthProfessional(String username){return (HealthProfessional)this.em.find(HealthProfessional.class,username);}


}
