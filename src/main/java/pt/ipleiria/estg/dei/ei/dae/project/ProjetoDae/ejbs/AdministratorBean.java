package pt.ipleiria.estg.dei.ei.dae.project.ProjetoDae.ejbs;


import pt.ipleiria.estg.dei.ei.dae.project.ProjetoDae.Roles;
import pt.ipleiria.estg.dei.ei.dae.project.ProjetoDae.dtos.UpdatePasswordDTO;
import pt.ipleiria.estg.dei.ei.dae.project.ProjetoDae.entities.Administrator;
import pt.ipleiria.estg.dei.ei.dae.project.ProjetoDae.entities.HealthProfessional;
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
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.core.Response;
import java.util.List;
import java.util.Objects;

@Stateless
public class AdministratorBean {
    @PersistenceContext
    private EntityManager em;
    @EJB
    private EmailBean emailBean;
    @EJB
    private TokenBean tokenBean;

    public void create(String username, String password, String name, String email, int version, Roles role,boolean active) throws MyConstraintViolationException, MyEntityNotFoundException, MyEntityExistsException, MessagingException {

        Administrator administrator = this.em.find(Administrator.class,username);
        if (administrator!=null){
            System.out.println("Administrator with username: "+ username + "already exists");
            System.exit(0);
        }
        try{
        administrator = new Administrator(username,password,name,email,version, role,active);
        em.persist(administrator);
        tokenBean.create(email);
        Token token = tokenBean.findToken(email);
        System.out.println("This is email:"+email);
        if (token==null){
            throw new MyEntityNotFoundException("token not found");
        }
            emailBean.send(email,"http://localhost:3000/auth/changePassword?token="+token.getToken()+"&username="+username,"http://localhost:3000/auth/changePassword?token="+token.getToken()+"&username="+username);
        }
        catch (ConstraintViolationException e){
            throw new MyConstraintViolationException(e);
        }
    }

    public List<Administrator> getAllAdministrators(){
        return this.em.createNamedQuery("getAllAdministrators").getResultList();
    }

    public Administrator findAdministrator(String username){return (Administrator)this.em.find(Administrator.class,username);}


    public void update(String username ,String name,String email, boolean active) throws MyEntityNotFoundException {
        Administrator administrator = em.find(Administrator.class, username);

        if (administrator != null) {
            em.lock(administrator, LockModeType.OPTIMISTIC);
            administrator.setActive(active);
            administrator.setEmail(email);
            administrator.setName(name);
            administrator.setUsername(username);
            administrator.setPassword(administrator.getPassword());
            //System.err.println("Username "+ administrator.getUsername()+" name: "+administrator.getName()+" profession: "+administrator.getProfession()+" email: "+administrator.getEmail());
            //System.err.println("setVersion "+ administrator.getVersion()+" Role: "+administrator.getRole()+" password: "+administrator.getPassword());

        }else{
            System.err.println("ERROR_FINDING_ADMIN");
        }

    }




//    public void update(Administrator updateAdministrator) throws MyEntityNotFoundException {
//        em.merge(updateAdministrator);
//    }
//
    public void removeAdministrator(String username){
        Administrator administrator = (Administrator)this.em.find(Administrator.class,username);
        if (administrator==null){
            System.out.println("Administrator with username" + username + "not found.");
        }
        this.em.remove(administrator);
    }

    public void delete(String username) {
        Administrator administrator = em.find(Administrator.class, username);
        if (administrator != null) {
            em.lock(administrator,LockModeType.OPTIMISTIC);
            administrator.setActive(false);
        }else {
            System.err.println("ERROR_DELETING_ADMINISTRATOR");
        }
    }

//    public void updatePassword(String username,String password,String tokenString) throws MyEntityNotFoundException {
//        Administrator administrator = em.find(Administrator.class,username);
//        if (administrator==null){
//            throw new MyEntityNotFoundException("Administrator not found");
//        }
//        Token token = tokenBean.findToken(administrator.getEmail());
//        System.out.println("token1: "+token.getToken()+"token2: "+tokenString );
//        if (!Objects.equals(token.getToken(), tokenString)){
//            throw new NotAuthorizedException("Token was not found");
//        }
//        em.lock(administrator,LockModeType.OPTIMISTIC);
//        administrator.setPassword(password);
//    }
//
//    public void deleteToken(String username,String tokenString) throws MyEntityNotFoundException {
//        Administrator administrator = em.find(Administrator.class,username);
//        if (administrator==null){
//            throw new MyEntityNotFoundException("Administrator not found");
//        }
//        Token token = tokenBean.findToken(administrator.getEmail());
//        System.out.println("token1: "+token.getToken()+"token2: "+tokenString );
//        if (!Objects.equals(token.getToken(), tokenString)){
//            throw new NotAuthorizedException("Token was not found");
//        }
//        em.lock(token,LockModeType.PESSIMISTIC_WRITE);
//        tokenBean.delete(administrator.getEmail());
//    }
//
//    public void sendEmailToChangePassword(String username) throws MyConstraintViolationException, MyEntityNotFoundException, MyEntityExistsException, MessagingException {
//        Administrator administrator = em.find(Administrator.class,username);
//        if (administrator==null){
//            throw new MyEntityNotFoundException("Administrator not found");
//        }
//        tokenBean.create(administrator.getEmail());
//        Token token = tokenBean.findToken(administrator.getEmail());
//        System.out.println("This is email: "+ administrator.getEmail());
//        if (token == null){
//            throw new MyEntityNotFoundException("token not found");
//        }
//        emailBean.send(administrator.getEmail(), "localhost:3000/"+token.getToken(),token.getToken());
//
//    }
}
