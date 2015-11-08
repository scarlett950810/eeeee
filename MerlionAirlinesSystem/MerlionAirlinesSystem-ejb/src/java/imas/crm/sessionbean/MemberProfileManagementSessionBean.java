/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package imas.crm.sessionbean;

import imas.crm.entity.MemberEntity;
import imas.distribution.entity.TicketEntity;
import imas.planning.entity.FlightEntity;
import java.util.ArrayList;
import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

/**
 *
 * @author Howard
 */
@Stateless
public class MemberProfileManagementSessionBean implements MemberProfileManagementSessionBeanLocal {

    @PersistenceContext
    private EntityManager em;

    @Override
    public MemberEntity getMember(String memberID) {
        
        Query query = em.createQuery("SELECT m FROM MemberEntity m WHERE m.memberID = :memberID");
        query.setParameter("memberID", memberID);

        List<MemberEntity> members = (List<MemberEntity>) query.getResultList();
        if (members.isEmpty()) {
            return null;
        } else {
            System.out.println(members.get(0));
            return members.get(0);
        }
    }

    @Override
    public void updateProfile(MemberEntity member) {

        em.merge(member);
        System.out.println(member.getContactNumber());
    }

    @Override
    public boolean resetPassword(String oldPassword, String newPassword, String memberID) {
        
        Query query = em.createQuery("SELECT m FROM MemberEntity m WHERE m.memberID = :memberID");
        query.setParameter("memberID", memberID);

        List<MemberEntity> members = (List<MemberEntity>) query.getResultList();
        System.out.println(members.isEmpty());
        if (members.isEmpty()) {
            return false;
        } else {
            MemberEntity member = members.get(0);
            if (member.getPinNumber().equals(oldPassword)) {
                member.setPinNumber(newPassword);
                System.out.print(member.getPinNumber());
                return true;
            } else {
                return false;
            }

        }

    }

    @Override //To get the tickets associated with a specific member
    public List<TicketEntity> getMemberTickets(String memberID) {
        
        Query query = em.createQuery("SELECT m FROM MemberEntity m WHERE m.memberID = :memberID");
        query.setParameter("memberID", memberID);
        List<MemberEntity> members = (List<MemberEntity>) query.getResultList();
        MemberEntity member = members.get(0);
        //List<FlightEntity> flights = new ArrayList<>();
        List<TicketEntity> tickets = member.getTicketList();
        return tickets;
    }

    @Override
    public void redeemMileage(double usedMileage, String memberID) {
        Query query = em.createQuery("SELECT m FROM MemberEntity m WHERE m.memberID = :memberID");
        query.setParameter("memberID", memberID);
        List<MemberEntity> members = (List<MemberEntity>) query.getResultList();

        if (!members.isEmpty()) {
            MemberEntity member = members.get(0);
            //List<FlightEntity> flights = new ArrayList<>();
            member.setMileage(member.getMileage() - usedMileage);
            System.out.println("redeemed " + member.getMileage());
        }
    }

    @Override
    public List<TicketEntity> getHistoricalTickets(String memberID) {
        List<TicketEntity> tickets = getMemberTickets(memberID);
        for(int i=0; i< tickets.size(); i++){
            TicketEntity ticket = tickets.get(i);
            if(ticket.getIssued() == false){
                tickets.remove(ticket);
            }
        }
        return tickets;
    }

    @Override
    public List<TicketEntity> getFutureTicket(String memberID) {
        List<TicketEntity> tickets = getMemberTickets(memberID);
        for(int i=0; i< tickets.size(); i++){
            TicketEntity ticket = tickets.get(i);
            if(ticket.getIssued() == true){
                tickets.remove(ticket);
            }
        }
        return tickets;
    }

    @Override
    public void accumulateMileage(String memberID, double mileage) {
        Query query = em.createQuery("SELECT m FROM MemberEntity m WHERE m.memberID = :memberID");
        query.setParameter("memberID", memberID);
        List<MemberEntity> members = (List<MemberEntity>) query.getResultList();

        if (!members.isEmpty()) {
            MemberEntity member = members.get(0);
            member.setMileage(member.getMileage() + mileage);
            System.out.print("new Mileage = " + member.getMileage());
        }
    }

    @Override
    public boolean deductMileage(String memberID, double deductAmount) {
        Query query = em.createQuery("SELECT m FROM MemberEntity m WHERE m.memberID = :memberID");
        query.setParameter("memberID", memberID);
        List<MemberEntity> members = (List<MemberEntity>) query.getResultList();

        if (!members.isEmpty()) {
            MemberEntity member = members.get(0);
            if(member.getMileage() - deductAmount >= 0){
                member.setMileage(member.getMileage() - deductAmount);
                System.out.print("new Mileage = " + member.getMileage());
                return true;
            }else{
                return false;
            }
            
        }
        return false;
    }

    @Override
    public void upgradeSeatClassWithMileage(TicketEntity ticket) {
        em.merge(ticket);
    }
    
    

}
