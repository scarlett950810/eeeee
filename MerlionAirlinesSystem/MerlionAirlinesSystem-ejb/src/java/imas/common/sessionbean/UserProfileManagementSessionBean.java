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
import util.security.CryptographicHelper;

/**
 *
 * @author Howard
 */
@Stateless
public class UserProfileManagementSessionBean implements UserProfileManagementSessionBeanLocal {

    @PersistenceContext
    private EntityManager entityManager;
    CryptographicHelper cp = new CryptographicHelper();

    @Override
    public Boolean getOldPassword(String staffNo, String oldPassword) {

        Query query = entityManager.createQuery("SELECT s FROM StaffEntity s WHERE s.staffNo = :staffNumber");
        query.setParameter("staffNumber", staffNo);
        List<StaffEntity> staffs = (List<StaffEntity>) query.getResultList();

        if (staffs.isEmpty()) {
            return null;
        } else {
            String salt = staffs.get(0).getSalt();
            String newPassword = cp.doMD5Hashing(oldPassword + salt);
            if (newPassword.equals(staffs.get(0).getPassword())) {
                System.out.print("password correct");
                return true;
            } else {
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

        if (staffs.isEmpty()) {
            System.out.println("The staff does not exist");
        } else {
            StaffEntity staff = staffs.get(0);
            String salt = staff.getSalt();
            String actualPassword = cp.doMD5Hashing(newPassword + salt);
            staff.setPassword(actualPassword);
        }
    }

    @Override
    public void updateEmail(String staffNo, String newEmail) {

        Query query = entityManager.createQuery("SELECT s FROM StaffEntity s WHERE s.staffNo = :staffNumber");
        query.setParameter("staffNumber", staffNo);
        List<StaffEntity> staffs = (List<StaffEntity>) query.getResultList();

        if (staffs.isEmpty()) {
            System.out.println("The staff does not exist");
        } else {
            StaffEntity staff = staffs.get(0);
            staff.setEmail(newEmail);
        }
    }

    @Override
    public void updateContact(String staffNo, String contactNumber) {
        Query query = entityManager.createQuery("SELECT s FROM StaffEntity s WHERE s.staffNo = :staffNumber");
        query.setParameter("staffNumber", staffNo);
        List<StaffEntity> staffs = (List<StaffEntity>) query.getResultList();

        if (staffs.isEmpty()) {
            System.out.println("The staff does not exist");
        } else {
            StaffEntity staff = staffs.get(0);
            staff.setContactNumber(contactNumber);
        }
    }

    @Override
    public void updateWorking(String staffNo) {
        Query query = entityManager.createQuery("SELECT s FROM StaffEntity s WHERE s.staffNo = :staffNumber");
        query.setParameter("staffNumber", staffNo);
        StaffEntity staff = (StaffEntity) query.getSingleResult();
        //staff.setWorking(true);
        if (staff.getWorking()) //true: in working
        {
            staff.setWorking(false);
        } else {
            staff.setWorking(true);
        }

    }

    @Override
    public String getWorkingMessage(String staffNo) {
        System.err.println("Enter get working msg" + staffNo);
        Query query = entityManager.createQuery("SELECT s FROM StaffEntity s WHERE s.staffNo = :staffNumber");
        System.err.println("Enter get working msg" + staffNo);
        query.setParameter("staffNumber", staffNo);
        System.err.println("Before get query");
        List<StaffEntity> staffs = (List<StaffEntity>) query.getResultList();
        System.err.println("Finish query"+staffs.size());
        
        if (staffs.isEmpty()) {
            System.out.println("The staff does not exist");
            return "hahaha";
        } else {
            StaffEntity staff = staffs.get(0);
            //staff.setWorking(true);
            if (staff.getWorking()) {
                return "You are going to leave your work station. Please make confirmation";
            } else {
                return "You are going to start working here. Please make confirmation";
            }
        }

    }

}
