/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package imas.crm.sessionbean;

import imas.crm.entity.MemberEntity;
import javax.ejb.Local;

/**
 *
 * @author Howard
 */
@Local
public interface MemberProfileManagementSessionBeanLocal {

    MemberEntity getMember(String memberID);

    void updateProfile(MemberEntity member);

    boolean resetPassword(String oldPassword, String newPassword, String memberID);
    
}
