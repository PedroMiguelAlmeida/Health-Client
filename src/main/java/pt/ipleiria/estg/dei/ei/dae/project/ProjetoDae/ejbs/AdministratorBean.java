package pt.ipleiria.estg.dei.ei.dae.project.ProjetoDae.ejbs;


import pt.ipleiria.estg.dei.ei.dae.project.ProjetoDae.Roles;
import pt.ipleiria.estg.dei.ei.dae.project.ProjetoDae.entities.Administrator;
import pt.ipleiria.estg.dei.ei.dae.project.ProjetoDae.entities.HealthProfessional;
import pt.ipleiria.estg.dei.ei.dae.project.ProjetoDae.exceptions.MyEntityNotFoundException;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.List;

@Stateless
public class AdministratorBean {
    @PersistenceContext
    private EntityManager em;
    public void create(String username, String password, String name, String email, int version, Roles role,boolean active){
        Administrator administrator = new Administrator(username,password,name,email,version, role,active);
        em.persist(administrator);
    }

    public List<Administrator> getAllAdministrators(){
        return this.em.createNamedQuery("getAllAdministrators").getResultList();
    }

    public Administrator findAdministrator(String username){return (Administrator)this.em.find(Administrator.class,username);}

    public void update(Administrator updateAdministrator) throws MyEntityNotFoundException {
        em.merge(updateAdministrator);
    }
    public void removeAdministrator(String username){
        Administrator administrator = (Administrator)this.em.find(Administrator.class,username);
        if (administrator==null){
            System.out.println("Administrator with username" + username + "not found.");
        }
        this.em.remove(administrator);
    }

}
