/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package imas.common.sessionbean;

import imas.common.entity.StaffEntity;
import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

/**
 *
 * @author Howard
 */
@Stateless
public class UserProfileManagementSessionBean implements UserProfileManagementSessionBeanLocal {

    @PersistenceContext
    private EntityManager entityManager;
    
    @Override
    public Boolean getOldPassword(String staffNo, String oldPassword) {
       
        Query query = entityManager.createQuery("SELECT s FROM StaffEntity s WHERE s.staffNo = :staffNumber");
        query.setParameter("staffNumber", staffNo);
        List<StaffEntity> staffs = (List<StaffEntity>) query.getResultList();
        
        if(staffs.isEmpty()){
            return null;
        }else{
            if(oldPassword.equals(staffs.get(0).getPassword())){
                System.out.print("password correct");
                return true;
            }else{
                System.out.print("password incorrect");
                return false;
            }
        }
    }

    @Override
    public void updatePassword(String newPassword, String staffNo) {
        
        Query query = entityManager.createQuery("SELECT s FROM StaffEntity s WHERE s.staffNo = :staffNumber");
        query.setParameter("staffNumber", staffNo);
        List<StaffEntity> staffs = (List<StaffEntity>) query.getResultList();
        
        if(staffs.isEmpty()){
            System.out.println("The staff does not exist");
        }else{
            StaffEntity staff = staffs.get(0);
            staff.setPassword(newPassword);
        }
    }

    @Override
    public void updateEmail(String staffNo, String newEmail) {
        
        Query query = entityManager.createQuery("SELECT s FROM StaffEntity s WHERE s.staffNo = :staffNumber");
        query.setParameter("staffNumber", staffNo);
        List<StaffEntity> staffs = (List<StaffEntity>) query.getResultList();
        
        if(staffs.isEmpty()){
            System.out.println("The staff does not exist");
        }else{
            StaffEntity staff = staffs.get(0);
            staff.setEmail(newEmail);
        }
    }

    @Override
    public void updateContact(String staffNo, String contactNumber) {
        Query query = entityManager.createQuery("SELECT s FROM StaffEntity s WHERE s.staffNo = :staffNumber");
        query.setParameter("staffNumber", staffNo);
        List<StaffEntity> staffs = (List<StaffEntity>) query.getResultList();
        
        if(staffs.isEmpty()){
            System.out.println("The staff does not exist");
        }else{
            StaffEntity staff = staffs.get(0);
            staff.setContactNumber(contactNumber);
        }
    }

    
    
}
