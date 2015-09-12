/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package imas.common.sessionbean;

import imas.common.entity.StaffEntity;
import imas.planning.entity.AirportEntity;
import imas.utility.sessionbean.EmailManager;
import java.util.List;
import java.util.UUID;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.ejb.Stateless;
import javax.mail.MessagingException;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

/**
 *
 * @author Howard
 */
@Stateless
public class AccountManagementSessionBean implements AccountManagementSessionBeanLocal {

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public void resetStaffPassword(String email) {
        String password = generatePassword();
        
        Query query = entityManager.createQuery("SELECT s FROM StaffEntity s WHERE s.email = :email");
        query.setParameter("email", email);
        try {
            StaffEntity staff = (StaffEntity) query.getSingleResult();
            staff.setPassword(password);            
        } catch (NoResultException exception) {
            System.out.println("No such staff");
        }
        
        try {
            sendResetEmail(email, password);
        } catch (MessagingException ex) {
            Logger.getLogger(AccountManagementSessionBean.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private void sendResetEmail(String email, String password) throws MessagingException {
        String subject = "Test Email Function";
        String content = "Hello world! Welcome to the Merlion Airline Internal System. You temporary password is: " + password;
        System.out.print(password);
        EmailManager.run(email, subject, content);
    }

    private void sendNewStaffEmail(String email, String password, String staffName) throws MessagingException {
        String subject = "Welcome to Merlion Airlines";
        String content = "Hi " + staffName + ", " + "<br>Welcome to Merlion Airlines and start your dream career here.<br>Please refer below for your initial password to access the internal system: <br>" + password + "<br><br>Thank you.<br><br>Merlion Airline HR Manager";
        EmailManager.run(email, subject, content);
    }
    
    @Override
    public Boolean addStaff(String staffNo, String name, String email, String contactNumber, String address, String gender, String base, String department) {
        String password = generatePassword();
        System.out.println(password);
        Query query = entityManager.createQuery("SELECT s FROM StaffEntity s WHERE s.staffNo = :staffNumber");
        query.setParameter("staffNumber", staffNo);
       
        List<StaffEntity> staffs = (List<StaffEntity>) query.getResultList();
        if(staffs.isEmpty()){ 
            StaffEntity staff = new StaffEntity(staffNo, name, password, email, contactNumber, department, address, gender, base);
            entityManager.persist(staff);
            try {
                sendNewStaffEmail(staff.getEmail(),password, staff.getDisplayName());
            } catch (MessagingException ex) {
                Logger.getLogger(AccountManagementSessionBean.class.getName()).log(Level.SEVERE, null, ex);
            }
            return true;
        }else{
            return false;
        }
    }

    private String generatePassword() {
        String password = UUID.randomUUID().toString();
        password = password.replaceAll("-", "").substring(0, 8);
        System.out.println("uuid = " + password);
        return password;
    }

    @Override
    public Boolean checkEmailExistence(String email) {
        Query query = entityManager.createQuery("SELECT s FROM StaffEntity s WHERE s.email = :email");
        query.setParameter("email", email);

        List<StaffEntity> staff = (List<StaffEntity>) query.getResultList();
        if (staff.isEmpty()) {
            return false;
        } else {
            return true;
        }
    }

    @Override
    public List<StaffEntity> fetchStaff() {
        Query query = entityManager.createQuery("SELECT s FROM StaffEntity s");

        List<StaffEntity> staff = (List<StaffEntity>) query.getResultList();
        return staff;
    }

    @Override
    public void deleteStaff(String staffNo) {
        Query query = entityManager.createQuery("DELETE FROM StaffEntity s WHERE s.staffNo = :staffNo");
        query.setParameter("staffNo", staffNo);
        query.executeUpdate();
        System.out.println("staff deleted");
    }

    @Override
    public void updateStaff(String staffNo, String email, String contactNumber, String address, String department, String base) {
        Query query = entityManager.createQuery("SELECT s FROM StaffEntity s WHERE s.staffNo = :staffNo");
        query.setParameter("staffNo", staffNo);

        List<StaffEntity> staffs = (List<StaffEntity>) query.getResultList();
        if (staffs.isEmpty()) {
            System.out.print("no such user");
        } else {
            StaffEntity staff = staffs.get(0);
            staff.setEmail(email);
            staff.setContactNumber(contactNumber);
            staff.setAddress(address);
            staff.setDepartment(department);
            staff.setBase(base);
        }
        
    }

    @Override
    public StaffEntity getStaff(String staffNo) {
        Query query = entityManager.createQuery("SELECT s FROM StaffEntity s WHERE s.staffNo = :staffNo");
        query.setParameter("staffNo", staffNo);

        List<StaffEntity> staffs = (List<StaffEntity>) query.getResultList();
        if (staffs.isEmpty()) {
            System.out.print("no such user");
            return null;
        } else {
            return staffs.get(0);
        } 
    }

    @Override
    public List<String> fetchBases() {
        Query query = entityManager.createQuery("SELECT a.airportCode FROM AirportEntity a");

        List<String> bases = (List<String>) query.getResultList();
        if (bases.isEmpty()) {
            System.out.print("no results");
            return null;
        } else {
            return bases;
        }
    }
    
    

}
