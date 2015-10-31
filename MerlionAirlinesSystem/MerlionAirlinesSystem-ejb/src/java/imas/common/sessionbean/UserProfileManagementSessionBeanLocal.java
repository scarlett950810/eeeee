/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package imas.common.sessionbean;

import javax.ejb.Local;

/**
 *
 * @author Howard
 */
@Local
public interface UserProfileManagementSessionBeanLocal {

    Boolean getOldPassword(String staffNo, String oldPassword);

    void updatePassword(String newPassword, String staffNo);

    void updateEmail(String staffNo, String newEmail);

    void updateContact(String staffNo, String contactNumber);

    void updateWorking(String staffNo);

    String getWorkingMessage(String staffNo);

    void setSequrityQuestion(String staffNo, String answer);
    
}
