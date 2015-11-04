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
public interface MemberAccountActivationSessionBeanLocal {

    MemberEntity getMemberBasedOnToken(String token);

    void activateMemberAccount(MemberEntity member);
    
}
