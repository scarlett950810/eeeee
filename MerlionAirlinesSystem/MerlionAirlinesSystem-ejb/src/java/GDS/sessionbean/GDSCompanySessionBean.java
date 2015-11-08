/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package GDS.sessionbean;

import GDS.entity.GDSCompanyEntity;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

/**
 *
 * @author Scarlett
 */
@Stateless
public class GDSCompanySessionBean implements GDSCompanySessionBeanLocal {

    @PersistenceContext
    private EntityManager em;

    @Override
    public boolean accountNotExist(String account) {
        Query querForAccount = em.createQuery("SELECT c FROM GDSCompanyEntity c WHERE c.account = :account");
        querForAccount.setParameter("account", account);
        return querForAccount.getResultList().isEmpty();
    }

    @Override
    public void register(String account, String password, String name, String HQCountry,
            String mainPage, String email, String contactNo) {
        GDSCompanyEntity company = new GDSCompanyEntity(account, password, name, HQCountry, mainPage, email, contactNo);
        em.persist(company);
    }

    @Override
    public boolean logIn(String account, String password) {
        Query q = em.createQuery("SELECT c FROM GDSCompanyEntity c WHERE c.account = :account AND c.password = :password");
        q.setParameter("account", account);
        q.setParameter("password", password);
        return !q.getResultList().isEmpty();
    }

    @Override
    public GDSCompanyEntity getCompany(String account) {
        Query q = em.createQuery("SELECT c FROM GDSCompanyEntity c WHERE c.account = :account");
        q.setParameter("account", account);
        if (q.getResultList().isEmpty()) {
            return null;
        } else {
            return (GDSCompanyEntity) q.getResultList().get(0);
        }
    }
}
