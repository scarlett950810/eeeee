/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package imas.common.sessionbean;

import imas.common.entity.StaffEntity;
import imas.planning.entity.AirportEntity;
import imas.utility.sessionbean.EmailManager;
import java.security.SecureRandom;
import java.util.List;
import java.util.Random;
import java.util.UUID;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.ejb.Stateless;
import javax.mail.MessagingException;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import util.security.CryptographicHelper;

/**
 *
 * @author Howard
 */
@Stateless
public class AccountManagementSessionBean implements AccountManagementSessionBeanLocal {

    private static final Random RANDOM = new SecureRandom();
    public static final int SALT_LENGTH = 8;
    CryptographicHelper cp = new CryptographicHelper();
    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public void resetStaffPassword(String email) {
        String password = generatePassword();
        String tempPassword;
        
        Query query = entityManager.createQuery("SELECT s FROM StaffEntity s WHERE s.email = :email");
        query.setParameter("email", email);
        try {
            StaffEntity staff = (StaffEntity) query.getSingleResult();
            tempPassword=cp.doMD5Hashing(password+staff.getSalt());
            staff.setPassword(tempPassword);
        } catch (NoResultException exception) {
            System.out.println("No such staff");
        }

        try {
            SendResetEmail(email, password);
        } catch (MessagingException ex) {
            Logger.getLogger(AccountManagementSessionBean.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private void SendResetEmail(String email, String password) throws MessagingException {
        String subject = "Test Email Function";
        String content = "Hello world! Welcome to the Merlion Airline Internal System. You temporary password is: " + password;
        System.out.print(password);
        EmailManager.run(email, subject, content);
    }

    @Override
    public Boolean addStaff(String staffNo, String name, String email, String contactNumber, String address, String gender, String base, String department) {
        String password = generatePassword();
        System.out.println(password);
        String tempPassword;
        String salt = "";
        String letters = "0123456789abcdefghjkmnpqrstuvwxyzABCDEFGHJKMNPQRSTUVWXYZ23456789";
        for (int i = 0; i < SALT_LENGTH; i++) {
            int index = (int) (RANDOM.nextDouble() * letters.length());
            salt += letters.substring(index, index + 1);
        }

        tempPassword = cp.doMD5Hashing(password + salt);

        Query query = entityManager.createQuery("SELECT s FROM StaffEntity s WHERE s.staffNo = :staffNumber");
        query.setParameter("staffNumber", staffNo);

        List<StaffEntity> staffs = (List<StaffEntity>) query.getResultList();
        if (staffs.isEmpty()) {
            StaffEntity staff = new StaffEntity(staffNo, name, tempPassword, email, contactNumber, department, address, gender, base);
            staff.setSalt(salt);
            entityManager.persist(staff);
            return true;
        } else {
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

}
