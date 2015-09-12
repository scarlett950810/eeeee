/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package imas.common.sessionbean;

import imas.common.entity.InternalMessageEntity;
import imas.common.entity.StaffEntity;
import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

/**
 *
 * @author Scarlett
 */
@Stateless
public class InternalMessageSessionBean implements InternalMessageSessionBeanLocal {
    
    @PersistenceContext
    private EntityManager entityManager;
    
    @Override
    public List<StaffEntity> getAllStaff() {        
        Query queryForAllStaff = entityManager.createQuery("SELECT s FROM StaffEntity s");
        List<StaffEntity> allStaff = queryForAllStaff.getResultList();
//        System.out.println(allStaff);
        return allStaff;
    }
    
    @Override
    public StaffEntity getStaffEntityByStaffNo(String staffNo) {
        Query queryForStaff = entityManager.createQuery("SELECT s FROM StaffEntity s WHERE s.staffNo = :staffNo");
        queryForStaff.setParameter("staffNo", staffNo);
        List result = queryForStaff.getResultList();
        if (result.isEmpty()) {
            return null;
        } else {
            StaffEntity staff = (StaffEntity) result.get(0);
            return staff;
            
        }
    }

    @Override
    public void sendMessage(StaffEntity loggedInStaff, StaffEntity receiver, String content) {
        InternalMessageEntity newMessage = new InternalMessageEntity(loggedInStaff, receiver, content);
        entityManager.persist(newMessage);
    }
    
    public List<InternalMessageEntity> getAllMessages(StaffEntity staff) {
        Query queryForAllMessages = entityManager.createQuery("SELECT m FROM InternalMessageEntity m WHERE m.receiver = :receiver");
        queryForAllMessages.setParameter("receiver", staff);
        return (List<InternalMessageEntity>) queryForAllMessages.getResultList();
    }

}
