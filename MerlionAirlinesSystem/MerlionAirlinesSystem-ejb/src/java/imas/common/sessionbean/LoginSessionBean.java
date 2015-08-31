/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package imas.common.sessionbean;

import imas.common.entity.InternalAnnouncementEntity;
import imas.common.entity.StaffEntity;
import javax.ejb.Stateful;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.faces.context.FacesContext;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
/**
 *
 * @author Howard
 */
@Stateful
public class LoginSessionBean implements LoginSessionBeanLocal {                

    @PersistenceContext
    private EntityManager entityManager;
    
    /**
     *
     * @param staffNo
     * @param password
     * @return staffId
     */
    @Override
    public boolean doLogin(String staffNo, String password) {
//        insertData();
        
        Query query = entityManager.createQuery("SELECT s FROM StaffEntity s WHERE s.staffNo = :staffNumber AND s.password = :password");
        query.setParameter("staffNumber", staffNo);
        query.setParameter("password", password);
        
        List<StaffEntity> staff = (List<StaffEntity>)query.getResultList();
        if(!staff.isEmpty()){
            FacesContext.getCurrentInstance().getExternalContext().getSessionMap().put("staffEntity", (StaffEntity) staff.get(0));
            return true;
        }else{
            return false;
        }
    }

    private void insertData() {
        StaffEntity s = new StaffEntity("1", "DY", "1", "scarlett.dongyan@gmail.com", "84316002", "admin");
        entityManager.persist(s);
        
        InternalAnnouncementEntity i1 = new InternalAnnouncementEntity(s, "read message", "hello. This message is read.", new Date());
        i1.setIsRead(true);
        entityManager.persist(i1);
        
        InternalAnnouncementEntity i2 = new InternalAnnouncementEntity(s, "unread message", "An unread message.", new Date());
        entityManager.persist(i2);
    }
}
