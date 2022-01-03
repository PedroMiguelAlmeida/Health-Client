package pt.ipleiria.estg.dei.ei.dae.project.ProjetoDae.ejbs;


import pt.ipleiria.estg.dei.ei.dae.project.ProjetoDae.entities.Patient;
import pt.ipleiria.estg.dei.ei.dae.project.ProjetoDae.entities.Token;
import pt.ipleiria.estg.dei.ei.dae.project.ProjetoDae.exceptions.MyConstraintViolationException;
import pt.ipleiria.estg.dei.ei.dae.project.ProjetoDae.exceptions.MyEntityExistsException;
import pt.ipleiria.estg.dei.ei.dae.project.ProjetoDae.exceptions.MyEntityNotFoundException;

import javax.ejb.Stateless;

import javax.persistence.EntityManager;
import javax.persistence.LockModeType;
import javax.persistence.PersistenceContext;
import javax.validation.ConstraintViolationException;

@Stateless

public class TokenBean {
    @PersistenceContext
    EntityManager em;

    public void create(String tokenString,String email) throws MyEntityNotFoundException, MyEntityExistsException, MyConstraintViolationException {
        Token token = em.find(Token.class,tokenString);
        if (token !=null){
            throw new MyEntityExistsException("This token already exists");
        }
        try {
            token = new Token(tokenString,email);
            em.persist(token);
        } catch (ConstraintViolationException e) {
            throw new MyConstraintViolationException(e);
        }
    }

    public Token findToken(String tokenString) throws MyEntityNotFoundException{
        Token token = em.find(Token.class,tokenString);
        if(token == null)
            throw new MyEntityNotFoundException("Token  " + tokenString + " not found");
        return token;
    }

    public void delete(String tokenString){
        Token deleteToken = em.find(Token.class,tokenString);
        if (deleteToken!=null){
            em.remove(deleteToken);
        }
    }
}
