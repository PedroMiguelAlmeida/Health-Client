package pt.ipleiria.estg.dei.ei.dae.project.ProjetoDae.ejbs;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import pt.ipleiria.estg.dei.ei.dae.project.ProjetoDae.entities.User;
import pt.ipleiria.estg.dei.ei.dae.project.ProjetoDae.entities.User;
import pt.ipleiria.estg.dei.ei.dae.project.ProjetoDae.exceptions.MyEntityNotFoundException;

import java.util.List;

@Stateless
public class UserBean {
    @PersistenceContext
    EntityManager em;

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
}
