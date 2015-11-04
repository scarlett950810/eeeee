/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package imas.departure.sessionbean;

import imas.distribution.entity.TicketEntity;
import imas.planning.entity.SeatEntity;
import static java.lang.Boolean.FALSE;
import static java.lang.Boolean.TRUE;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
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
public class WebCheckInSessionBean implements WebCheckInSessionBeanLocal {

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public List<TicketEntity> getCheckInTicket(String passportNumber, String referenceNumber) {
        Query query = entityManager.createQuery("SELECT t FROM TicketEntity t where t.passenger.passportNumber = :passportNumber AND t.referenceNumber = :referenceNumber");
        query.setParameter("passportNumber", passportNumber);
        query.setParameter("referenceNumber", referenceNumber);

        List<TicketEntity> tickets = (List<TicketEntity>) query.getResultList();

        if (tickets.isEmpty()) {
            return null;
        } else {
            for(int i=0; i<tickets.size(); i++){
                TicketEntity ticket = tickets.get(i);
                if(ticket.getIssued()==true){
                    tickets.remove(ticket);
                }
            }
            return tickets;
        }
    }

    @Override
    public List<List<SeatHelperClass>> fetchAllSeats(Long aircraftID, Long flightID, String bookingClass) {

        Query query1 = entityManager.createQuery("SELECT s FROM SeatEntity s where s.aircraft.id = :aircraftID");
        query1.setParameter("aircraftID", aircraftID);

        List<SeatEntity> seats = (List<SeatEntity>) query1.getResultList();

        Query query2 = entityManager.createQuery("SELECT t.seat FROM TicketEntity t where t.flight.id = :flightID");
        query2.setParameter("flightID", flightID);

        List<SeatEntity> occupiedSeats = (List<SeatEntity>) query2.getResultList();
        
        String seatClass;

        // Input bookingClass can be one of the following
        // First Class, Business Class, Premium Economy Class,
        // Full Service Economy, Economy Plus, Standard Economy, Economy Save, Economy Super Save
        // Economy Class Agency
        //Seat Class can be one of the following
        //First Class, Business Class, Premium Economy Class, Economy Class
        if (bookingClass.equals("First Class")) {
            seatClass = "First Class";
        } else if (bookingClass.equals("Business Class")) {
            seatClass = "Business Class";
        } else if (bookingClass.equals("Premium Economy Class")) {
            seatClass = "Premium Economy Class";
        } else {
            seatClass = "Economy Class";
        }

        if (seats.isEmpty() == false) {
            List<List<SeatHelperClass>> allSeats = new ArrayList<>();
            List<SeatHelperClass> seathelper = null;

            for (Iterator<SeatEntity> iterator = seats.iterator(); iterator.hasNext();) {
                SeatEntity next = iterator.next();
                SeatHelperClass temp = new SeatHelperClass();
                if (next.getSeatNo().indexOf("A") != -1) {
                    if (seathelper != null) {
                        allSeats.add(seathelper);
                    }
                    seathelper = new ArrayList<>();
                    temp.setSeatNumber(next.getSeatNo());
                    temp.setEligible(next.getSeatClass().equals(seatClass));
                    temp.setOccupied(occupiedSeats.contains(next));
                    seathelper.add(temp);
                } else {
                    temp.setSeatNumber(next.getSeatNo());
                    temp.setEligible(next.getSeatClass().equals(seatClass));
                    temp.setOccupied(occupiedSeats.contains(next));
                    seathelper.add(temp);
                }

            }

            return allSeats;

        } else {
            return null;
        }
    }

    @Override
    public void completeWebCheckIn(List<List<SeatHelperClass>> seatHelper, TicketEntity ticket) {
        SeatHelperClass seat;

        for (int i = 0; i < seatHelper.size(); i++) {
            for (int j = 0; j < seatHelper.get(i).size(); j++) {
                seat = seatHelper.get(i).get(j);
                if (seat.isSelected() == true) {
                    Query query = entityManager.createQuery("SELECT s FROM SeatEntity s where s.aircraft.id = :aircraftID AND s.seatNo = :seatNo");
                    query.setParameter("aircraftID", ticket.getFlight().getAircraft().getId());
                    query.setParameter("seatNo", seat.getSeatNumber());
                    
                    List<SeatEntity> seats = (List<SeatEntity>)query.getResultList();
                    ticket.setSeat(seats.get(0));
                    ticket.setIssued(TRUE);
                    
                }
            }
        }

        ticket.setIssued(TRUE);
        entityManager.merge(ticket);

        System.out.print(ticket.getSeat().getSeatNo());
    }

}
