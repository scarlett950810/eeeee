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
        memberID = "M1e571164";
        Query query = em.createQuery("SELECT m FROM MemberEntity m WHERE m.memberID = :memberID");
        query.setParameter("memberID", memberID);
        
        List<MemberEntity> members = (List<MemberEntity>)query.getResultList();
        if(members.isEmpty()){
            return null;
        }else{
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
        memberID = "M1e571164";
        Query query = em.createQuery("SELECT m FROM MemberEntity m WHERE m.memberID = :memberID");
        query.setParameter("memberID", memberID);
        
        List<MemberEntity> members = (List<MemberEntity>)query.getResultList();
        System.out.println(members.isEmpty());
        if(members.isEmpty()){
            return false;
        }else{
            MemberEntity member = members.get(0);
            if(member.getPinNumber().equals(oldPassword)){
                member.setPinNumber(newPassword);
                System.out.print(member.getPinNumber());
                return true;
            }else{
                return false;
            }
            
        }
        
    }

    @Override
    public List<TicketEntity> getMemberTickets(String memberID) {
        memberID = "M1e571164";
        Query query = em.createQuery("SELECT m FROM MemberEntity m WHERE m.memberID = :memberID");
        query.setParameter("memberID", memberID);
        List<MemberEntity> members = (List<MemberEntity>)query.getResultList();
        MemberEntity member = members.get(0);
        //List<FlightEntity> flights = new ArrayList<>();
        List<TicketEntity> tickets = member.getTicketList();
        return tickets;
    }

    
    
}
