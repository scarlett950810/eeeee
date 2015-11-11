/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package DDS.sessionbean;

import DDS.entity.AgencyEntity;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

/**
 *
 * @author Scarlett
 */
@Stateless
public class AgencySessionBean implements AgencySessionBeanLocal {
    
    @PersistenceContext
    private EntityManager em;

    @Override
    public boolean accountExist(String account) {
        Query queryForAgencyAccount = em.createQuery("SELECT a FROM AgencyEntity a WHERE a.account = :account");
        queryForAgencyAccount.setParameter("account", account);
        return (queryForAgencyAccount.getResultList().size() > 0);
    }

    @Override
    public boolean checkLogin(String account, String pin) {
        Query queryForAgencyAccount = em.createQuery("SELECT a FROM AgencyEntity a WHERE a.account = :account AND a.pin = :pin");
        queryForAgencyAccount.setParameter("account", account);
        queryForAgencyAccount.setParameter("pin", pin);
        return (queryForAgencyAccount.getResultList().size() > 0);
    }
    
    @Override
    public void createAgency(String account, String pin, String name, String contactNumber, String email) {
        AgencyEntity agency = new AgencyEntity(account, pin, name, contactNumber, email);
        em.persist(agency);
    }
    
    @Override
    public AgencyEntity getAgency(String account) {
        Query queryForAgencyAccount = em.createQuery("SELECT a FROM AgencyEntity a WHERE a.account = :account");
        queryForAgencyAccount.setParameter("account", account);
        if (queryForAgencyAccount.getResultList().size() > 0) {
            return (AgencyEntity) queryForAgencyAccount.getResultList().get(0);
        } else {
            return null;
        }
    }


}
