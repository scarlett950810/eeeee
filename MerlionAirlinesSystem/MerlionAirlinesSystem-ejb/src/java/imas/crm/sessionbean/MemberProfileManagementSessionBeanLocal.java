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

    void redeemMileage(double usedMileage, String memberID);

    List<TicketEntity> getHistoricalTickets(String memberID);

    List<TicketEntity> getFutureTicket(String memberID);

    void accumulateMileage(String memberID, double mileage);

    boolean deductMileage(String memberID, double deductAmount);

    void upgradeSeatClassWithMileage(TicketEntity ticket);

    void setTicketToMember(MemberEntity member, List<TicketEntity> newTickets);

    boolean upgradeToFirstClass(TicketEntity ticket, MemberEntity member, double deductMileage);

    boolean upgradeToBusinessClass(TicketEntity ticket, MemberEntity member, double deductMileage);

    boolean upgradeToPremiumEconomyClass(TicketEntity ticket, MemberEntity member, double deductMileage);

    TicketEntity retrieveTicket(Long ticketID);
    
}
