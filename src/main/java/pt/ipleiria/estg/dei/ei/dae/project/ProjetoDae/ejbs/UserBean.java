package pt.ipleiria.estg.dei.ei.dae.project.ProjetoDae.ejbs;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.mail.MessagingException;
import javax.persistence.EntityManager;
import javax.persistence.LockModeType;
import javax.persistence.PersistenceContext;
import javax.ws.rs.NotAuthorizedException;

import pt.ipleiria.estg.dei.ei.dae.project.ProjetoDae.entities.Patient;
import pt.ipleiria.estg.dei.ei.dae.project.ProjetoDae.entities.Token;
import pt.ipleiria.estg.dei.ei.dae.project.ProjetoDae.entities.User;
import pt.ipleiria.estg.dei.ei.dae.project.ProjetoDae.entities.User;
import pt.ipleiria.estg.dei.ei.dae.project.ProjetoDae.exceptions.MyConstraintViolationException;
import pt.ipleiria.estg.dei.ei.dae.project.ProjetoDae.exceptions.MyEntityExistsException;
import pt.ipleiria.estg.dei.ei.dae.project.ProjetoDae.exceptions.MyEntityNotFoundException;

import java.util.List;
import java.util.Objects;

@Stateless
public class UserBean {
    @PersistenceContext
    EntityManager em;
    @EJB
    private EmailBean emailBean;
    @EJB
    private TokenBean tokenBean;

    public User authenticate(String username, String password) throws Exception {
        User user = (User)this.em.find(User.class, username);
        if (user != null && user.getPassword().equals(User.hashPassword(password))) {
            return user;
        } else {
            throw new Exception("Failed logging in with username '" + username + "': unknown username or wrong password");
        }
    }


    public List<User> getAllUsers() {
        return this.em.createNamedQuery("getAllUsers").getResultList();
    }

    public User findUser(String username) throws MyEntityNotFoundException {
        User user = em.find(User.class, username);
        if(user == null)
            throw new MyEntityNotFoundException("User with username: " + username + " not found");
        return user;
    }

    public void updatePassword(String username,String password,String tokenString) throws MyEntityNotFoundException {
        User user = em.find(User.class,username);
        if (user==null){
            throw new MyEntityNotFoundException("Patient not found");
        }
        Token token = tokenBean.findToken(user.getEmail());
        System.out.println("token1: "+token.getToken()+"token2: "+tokenString );
        if (!Objects.equals(token.getToken(), tokenString)){
            throw new NotAuthorizedException("Token was not found");
        }
        em.lock(user, LockModeType.OPTIMISTIC);
        user.setPassword(password);
    }

    public void deleteToken(String username,String tokenString) throws MyEntityNotFoundException {
        User user = em.find(User.class,username);
        if (user==null){
            throw new MyEntityNotFoundException("Patient not found");
        }
        Token token = tokenBean.findToken(user.getEmail());
        System.out.println("token1: "+token.getToken()+"token2: "+tokenString );
        if (!Objects.equals(token.getToken(), tokenString)){
            throw new NotAuthorizedException("Token was not found");
        }
        em.lock(token,LockModeType.PESSIMISTIC_WRITE);
        tokenBean.delete(user.getEmail());
    }


    public void sendEmailToChangePassword(String username) throws MyConstraintViolationException, MyEntityNotFoundException, MyEntityExistsException, MessagingException {
        User user = em.find(User.class,username);
        if (user==null){
            throw new MyEntityNotFoundException("Patient not found");
        }
        tokenBean.create(user.getEmail());
        Token token = tokenBean.findToken(user.getEmail());
        System.out.println("This is email: "+ user.getEmail());
        if (token == null){
            throw new MyEntityNotFoundException("token not found");
        }
        emailBean.send(user.getEmail(),"http://localhost:3000/auth/changePassword?token="+token.getToken()+"&username="+username,"http://localhost:3000/auth/changePassword?token="+token.getToken()+"&username="+username);

    }
}
