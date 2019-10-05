/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Models;

import Classes.Process;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

/**
 *
 * @author kraker
 */
@Stateless
public class ProcessFacade extends AbstractFacade<Process> {

    @PersistenceContext(unitName = "LesPU")
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public ProcessFacade() {
        super(Process.class);
    }
    
}