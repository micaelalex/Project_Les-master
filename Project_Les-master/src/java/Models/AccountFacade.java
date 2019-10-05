
/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Models;

import java.util.*;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import Classes.Account;

/**
 *
 * @author diogo
 */
@Stateless
public class AccountFacade extends AbstractFacade<Account> {

    @PersistenceContext(unitName = "LesPU")
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public AccountFacade() {
        super(Account.class);
    }
    
    public List<Account> findByEmail(String Email) {
        return em.createNamedQuery("Account.findByEmail").setParameter("email", Email).getResultList();
    }
    
    public List<Account> findByUserName(String Username) {
        return em.createNamedQuery("Account.findByUserName").setParameter("userName", Username).getResultList();
    }
    
    public  void teste(){
        
    }
    
}
