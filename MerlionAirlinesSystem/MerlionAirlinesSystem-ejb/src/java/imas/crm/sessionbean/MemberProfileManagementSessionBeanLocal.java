/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package imas.crm.sessionbean;

import imas.crm.entity.MemberEntity;
import imas.distribution.entity.TicketEntity;
import imas.planning.entity.FlightEntity;
import java.util.List;
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

    List<TicketEntity> getMemberTickets(String memberID);
    
}
