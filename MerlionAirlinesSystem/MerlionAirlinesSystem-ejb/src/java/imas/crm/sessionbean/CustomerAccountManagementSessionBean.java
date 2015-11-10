/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package imas.crm.sessionbean;

import imas.crm.entity.MemberEntity;
import imas.utility.sessionbean.EmailManager;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Random;
import java.util.UUID;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import util.security.CryptographicHelper;

/**
 *
 * @author wutong
 */
@Stateless
public class CustomerAccountManagementSessionBean implements CustomerAccountManagementSessionBeanLocal {

    @PersistenceContext
    private EntityManager em;
    CryptographicHelper cp = new CryptographicHelper();

    @Override
    public void createCustomer(MemberEntity newCustomer) {
        String token = UUID.randomUUID().toString();
        token = token.replaceAll("-", "").substring(0, 8);
        newCustomer.setToken(token);
        Date currentDate = new Date();
        Random randomno = new Random();
        String memberID = currentDate.getTime() + "" + randomno.nextInt(100);
        newCustomer.setMemberID(memberID);
        em.persist(newCustomer);
        EmailManager.runMemberActivationEmail(newCustomer.getEmail(), newCustomer.getTitle() + " " + newCustomer.getFirstName() + " "
                + newCustomer.getLastName(), "https://localhost:8181/MerlionAirlinesExternalSystem/CRM/crmMemberActivation.xhtml?token=" + token, newCustomer.getMemberID());
        EmailManager.run(newCustomer.getEmail(), "Welcome to MerFlion | Fly more with Merlion and get more benefits", "");
    }

    // Add business logic below. (Right-click in editor and choose
    // "Insert Code > Add Business Method")
    @Override
    public Boolean checkDuplicateAccount(String email) {
        return true;
    }

    @Override
    public MemberEntity checkValidity(String memberID, String pin) {

        Query query = em.createQuery("SELECT a FROM MemberEntity a WHERE a.memberID = :memberID");
        query.setParameter("memberID", memberID);
        MemberEntity member = new MemberEntity();
        List<MemberEntity> memberList = new ArrayList<>();
        memberList = query.getResultList();

        if (memberList.isEmpty()) {
            return null;
        } else {
            member = memberList.get(0);
            String salt = member.getSalt();
            pin = cp.doMD5Hashing(pin + salt);
            String correctPin = member.getPinNumber();
            if (pin.equals(correctPin)) {
                return member;
            } else {
                return null;
            }
        }
    }

    @Override
    public void updateCustomer(String title, String lastName, String firstName, String email, String gender, String nationality, int securityQuestionIndex, String answer, Date birthdate, String contactNumber) {
        Query query = em.createQuery("SELECT a FROM MemberEntity a WHERE a.email = :email");
        query.setParameter("email", email);
        MemberEntity member = new MemberEntity();
        List<MemberEntity> memberList = new ArrayList<>();
        memberList = query.getResultList();
        member = memberList.get(0);
        member.setTitle(title);
        member.setLastName(lastName);
        member.setFirstName(firstName);

        member.setGender(gender);
        member.setNationality(nationality);
        member.setSequrityQuestionIndex(securityQuestionIndex);
        member.setSequrityQuestionanswer(answer);
        member.setBirthDate(birthdate);

        member.setContactNumber(contactNumber);

    }

    @Override
    public boolean checkSequrityAnswer(int questionIndex, String answer, String memberID
    ) {
        Query query = em.createQuery("SELECT m FROM MemberEntity m WHERE m.memberID = :memberID");
        query.setParameter("memberID", memberID);

        List<MemberEntity> members = (List<MemberEntity>) query.getResultList();
        if (members.isEmpty()) {
            return false;
        } else {
            MemberEntity member = members.get(0);
            if (member.getSequrityQuestionIndex() == questionIndex && member.getSequrityQuestionanswer().toLowerCase().equals(answer.toLowerCase())) {
                return true;
            } else {
                return false;
            }
        }
    }

    @Override
    public void resetPIN(String memberID, String newPIN
    ) {
        Query query = em.createQuery("SELECT m FROM MemberEntity m WHERE m.memberID = :memberID");
        query.setParameter("memberID", memberID);

        List<MemberEntity> members = (List<MemberEntity>) query.getResultList();
        if (members.isEmpty()) {
            System.out.print("This member does not exist");
        } else {
            MemberEntity member = members.get(0);
            String salt = member.getSalt();
            newPIN = cp.doMD5Hashing(newPIN + salt);
            member.setPinNumber(newPIN);
            System.out.print(newPIN);
        }
    }

}
