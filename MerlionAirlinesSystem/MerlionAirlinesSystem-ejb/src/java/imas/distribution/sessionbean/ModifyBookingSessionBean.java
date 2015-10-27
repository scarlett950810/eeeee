/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package imas.distribution.sessionbean;

import imas.distribution.entity.PNREntity;
import imas.distribution.entity.TicketEntity;
import java.util.Date;
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
public class ModifyBookingSessionBean implements ModifyBookingSessionBeanLocal {

    @PersistenceContext
    private EntityManager entityManager;
    
    @Override
    public PNREntity retrievePNRRecord(String referenceNumber) {
        Query query = entityManager.createQuery("SELECT p FROM PNREntity p WHERE p.referenceNumber= :referenceNumber");
        query.setParameter("referenceNumber", referenceNumber);
        List<PNREntity> PNRList = (List<PNREntity>) query.getResultList();
        if(PNRList.isEmpty()){
            return null;
        }else{
            return PNRList.get(0);
        }
        
    }

    @Override
    public List<TicketEntity> getTicketList(String referenceNumber, String passportNumber) {
        Query query = entityManager.createQuery("SELECT t FROM TicketEntity t where t.passenger.passportNumber = :passportNumber AND t.referenceNumber = :referenceNumber");
        query.setParameter("passportNumber", passportNumber);
        query.setParameter("referenceNumber", referenceNumber);

        List<TicketEntity> tickets = (List<TicketEntity>) query.getResultList();
        Date currentDate = new Date();
        
        if (tickets.isEmpty()) {
            return null;
        } else {
            for(int i=0; i< tickets.size(); i++){
                if(tickets.get(i).getFlight().getDepartureDate().getTime() - currentDate.getTime() < 3600000){
                    tickets.remove(tickets.get(i));
                }
            }
            return tickets;
        }
    }

    @Override
    public void flushModification(TicketEntity ticket) {
        entityManager.merge(ticket);
    }
    
    
    

    
}
