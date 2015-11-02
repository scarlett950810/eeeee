/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package imas.crm.sessionbean;

import imas.crm.entity.MemberEntity;
import java.util.Date;
import javax.ejb.Local;

/**
 *
 * @author wutong
 */
@Local
public interface CustomerAccountManagementSessionBeanLocal {

    void createCustomer(MemberEntity newCustomer);

    Boolean checkDuplicateAccount(String email);

    Boolean checkValidity(String email, String pin);

    void updateCustomer(String title, String lastName, String firstName, String email, String gender, String nationality, int securityQuestion, String answer, Date birthdate, String contactNumber);
    
}
