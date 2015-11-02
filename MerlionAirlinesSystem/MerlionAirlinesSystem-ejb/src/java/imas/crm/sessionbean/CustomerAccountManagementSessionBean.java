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
import java.util.UUID;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

/**
 *
 * @author wutong
 */
@Stateless
public class CustomerAccountManagementSessionBean implements CustomerAccountManagementSessionBeanLocal {
    @PersistenceContext
    private EntityManager em;
    
    
    
    @Override
    public void createCustomer(MemberEntity newCustomer) {
        String token = UUID.randomUUID().toString();
        token = token.replaceAll("-", "").substring(0, 8);
        newCustomer.setToken(token);
        em.persist(newCustomer);
        EmailManager.runMemberActivationEmail(newCustomer.getEmail(), newCustomer.getTitle()+ " "+ newCustomer.getFirstName() + " " + 
                newCustomer.getLastName(), "https://localhost:8181/MerlionAirlinesExternalSystem/CRM/crmMemberActivation.xhtml?token="+token);
        EmailManager.run(newCustomer.getEmail(), "Welcome to MerFlion | Fly more with Merlion and get more benefits", "");
    }

    // Add business logic below. (Right-click in editor and choose
    // "Insert Code > Add Business Method")

    @Override
    public Boolean checkDuplicateAccount(String email) {
        return true;
    }

    @Override
    public Boolean checkValidity(String email, String pin) {
        System.err.println("ENTER"+email+pin);
        Query query = em.createQuery("SELECT a FROM MemberEntity a WHERE a.email = :email");
        query.setParameter("email", email);
        MemberEntity member = new MemberEntity();
        List<MemberEntity> memberList = new ArrayList<>();
        memberList = query.getResultList();
        
        
        System.err.println("Enter method"+memberList.size());
        if(memberList.isEmpty()) return false;
        member = memberList.get(0);
        
        System.err.println("email"+member.getEmail());
        System.err.println("password"+member.getPinNumber());
        String correctPin = member.getPinNumber();
        if(pin.equals(correctPin)){
            System.err.println("pin and email matches");
            return true;
        }
        return false;        
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
    
    
    
}
