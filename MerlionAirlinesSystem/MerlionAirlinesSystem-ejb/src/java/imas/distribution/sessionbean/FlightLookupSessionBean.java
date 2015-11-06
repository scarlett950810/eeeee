/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package imas.distribution.sessionbean;

import imas.crm.entity.MemberEntity;
import imas.distribution.entity.TicketEntity;
import imas.inventory.entity.BookingClassEntity;
import imas.inventory.entity.YieldManagementRuleEntity;
import imas.inventory.sessionbean.YieldManagementSessionBeanLocal;
import imas.planning.entity.AirportEntity;
import imas.planning.entity.FlightEntity;
import imas.planning.entity.RouteEntity;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Random;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

/**
 *
 * @author Scarlett
 */

@Stateless
public class FlightLookupSessionBean implements FlightLookupSessionBeanLocal {

    @EJB
    private YieldManagementSessionBeanLocal yieldManagementSessionBean;

    @PersistenceContext
    private EntityManager entityManager;

    /*
     @Override
     public void makeBooking(BookingClassEntity bookingClass, int number) {
        
     for (int i = 0; i < number; i++) {
     TicketEntity ticketEntity = new TicketEntity(bookingClass.getFlight(), bookingClass.getName(), bookingClass.getPrice());
     entityManager.persist(ticketEntity);
     }
        
     Query queryForRules = entityManager.createQuery("SELECT r FROM YieldManagementRuleEntity r WHERE r.flight = :flight");
     queryForRules.setParameter("flight", bookingClass.getFlight());
        
     List<YieldManagementRuleEntity> rules = queryForRules.getResultList();
        
     for (YieldManagementRuleEntity rule: rules) {
     if (rule.isEnabled()) {
     if (rule.isRule1()) {
     yieldManagementSessionBean.runYieldManagementRule1(rule);
     } else if (rule.isRule2()) {
     yieldManagementSessionBean.runYieldManagementRule2(rule);
     } else if (rule.isRule3()) {
     yieldManagementSessionBean.runYieldManagementRule3(rule);
     } else if (rule.isRule4()) {
     yieldManagementSessionBean.runYieldManagementRule4(rule);
     }
     }                
     }
     }
     */
    @Override
    public List<TicketEntity> createTicketEntitiesWithoutPersisting(BookingClassEntity bookingClass, int number) {
        List<TicketEntity> tickets = new ArrayList<>();

        for (int i = 0; i < number; i++) {
            TicketEntity ticketEntity = new TicketEntity(bookingClass.getFlight(), bookingClass.getName(), bookingClass.getPrice());
            tickets.add(ticketEntity);
        }

        return tickets;
    }

    @Override
    public void runYieldManagementsRulesOnFlight(FlightEntity flight) {
        Query queryForRules = entityManager.createQuery("SELECT r FROM YieldManagementRuleEntity r WHERE r.flight = :flight");
        queryForRules.setParameter("flight", flight);

        List<YieldManagementRuleEntity> rules = queryForRules.getResultList();

        for (YieldManagementRuleEntity rule : rules) {
            if (rule.isEnabled()) {
                if (rule.isRule1()) {
                    yieldManagementSessionBean.runYieldManagementRule1(rule);
                } else if (rule.isRule2()) {
                    yieldManagementSessionBean.runYieldManagementRule2(rule);
                } else if (rule.isRule3()) {
                    yieldManagementSessionBean.runYieldManagementRule3(rule);
                } else if (rule.isRule4()) {
                    yieldManagementSessionBean.runYieldManagementRule4(rule);
                }
            }
        }
    }

    @Override
    public int getQuotaLeft(BookingClassEntity bookingClassEntity) {
        Query queryForAllCurrentTicketsUnderBookingClass = entityManager.createQuery("SELECT t FROM TicketEntity t "
                + "WHERE t.flight = :flight AND t.bookingClassName = :bookingClassName");
        queryForAllCurrentTicketsUnderBookingClass.setParameter("flight", bookingClassEntity.getFlight());
        queryForAllCurrentTicketsUnderBookingClass.setParameter("bookingClassName", bookingClassEntity.getName());
        List<TicketEntity> tickets = queryForAllCurrentTicketsUnderBookingClass.getResultList();
        int numberOfTicketsSold = tickets.size();
        return bookingClassEntity.getQuota() - numberOfTicketsSold;
    }

    @Override
    public List<FlightEntity> getAllSellingFlights() {
        List<FlightEntity> allAvailableFlights = new ArrayList();

        Query queryForAllAvailableFlights = entityManager.createQuery("SELECT f FROM FlightEntity f WHERE f.departureDate > :departureDate AND f.counterCheckInClosed = :counterCheckInClosed");
        queryForAllAvailableFlights.setParameter("departureDate", new Date());
        queryForAllAvailableFlights.setParameter("counterCheckInClosed", false);
        List<FlightEntity> allUndeparturedFlights = queryForAllAvailableFlights.getResultList();

        for (FlightEntity flight : allUndeparturedFlights) {

            Query queryForBookingClasses = entityManager.createQuery("SELECT bc FROM BookingClassEntity bc WHERE bc.flight = :flight");
            queryForBookingClasses.setParameter("flight", flight);
            if (queryForBookingClasses.getResultList().size() > 0) {
                allAvailableFlights.add(flight);
            }
        }

        return allAvailableFlights;
    }

    @Override
    public List<String> getAllCountryNames() {

        Query queryForAllCountryNames = entityManager.createQuery("SELECT DISTINCT a.nationName FROM AirportEntity a ORDER BY a.nationName ASC");
        List<String> allCountryNames = queryForAllCountryNames.getResultList();

        return allCountryNames;

    }

    @Override
    public List<AirportEntity> getAllAirportsInCountry(String countryName) {

        Query queryForAllAirportsInCountry = entityManager.createQuery("SELECT a FROM AirportEntity a where a.nationName = :nationName");
        queryForAllAirportsInCountry.setParameter("nationName", countryName);
        List<AirportEntity> allAirportsInCountry = queryForAllAirportsInCountry.getResultList();

        return allAirportsInCountry;

    }

    @Override
    public List<AirportEntity> getAllDestinationAirports(AirportEntity airport) {

        Query queryForAllDestinationAirports = entityManager.createQuery("SELECT r.destinationAirport FROM RouteEntity r where r.originAirport = :originAirport GROUP BY r.destinationAirport.nationName ORDER BY r.destinationAirport.nationName ASC");
        queryForAllDestinationAirports.setParameter("originAirport", airport);
        List<AirportEntity> allDestinationAirportsByCountry = queryForAllDestinationAirports.getResultList();

        return allDestinationAirportsByCountry;

    }

    @Override
    public RouteEntity getDirectRoute(AirportEntity origin, AirportEntity destination) {

        // direct routes
        Query queryForRoute = entityManager.createQuery("SELECT r FROM RouteEntity r where r.originAirport = :originAirport and r.destinationAirport = :destinationAirport");
        queryForRoute.setParameter("originAirport", origin);
        queryForRoute.setParameter("destinationAirport", destination);

        if (queryForRoute.getResultList().isEmpty()) {
            RouteEntity directFlightRoute = null;
            return null;
        } else {
            return (RouteEntity) queryForRoute.getResultList().get(0);
        }

    }

    @Override
    public List<FlightEntity> getAvailableFlights(RouteEntity route, Date lowerBound, Date upperBound) {

        Query queryForAvailableFlights = entityManager.createQuery("SELECT f FROM FlightEntity f where f.route = :route and f.departureDate > :lowerBound AND f.departureDate < :upperBound ORDER BY f.departureDate ASC");
        queryForAvailableFlights.setParameter("route", route);
        queryForAvailableFlights.setParameter("lowerBound", lowerBound);
        queryForAvailableFlights.setParameter("upperBound", upperBound);

        List<FlightEntity> availableFlights = queryForAvailableFlights.getResultList();
        return availableFlights;

    }

    // the staying time of transfer would not exceed 24 hours.
    @Override
    public List<TransferFlight> getTransferFlights(AirportEntity origin, AirportEntity destination, Date departureDate) {
        List<TransferFlight> transferFlights = new ArrayList<>();
        List<AirportEntity> directDestinations = getAllDestinationAirports(origin);
        for (AirportEntity directDestination : directDestinations) {
            List<AirportEntity> transferDestinations = getAllDestinationAirports(directDestination);
            if (transferDestinations.contains(destination)) {
                // found transfer route
                RouteEntity route1 = getDirectRoute(origin, directDestination);
                if (route1 != null) {
                    List<FlightEntity> flights1 = getAvailableFlights(route1, departureDate, getDateAfterDays(departureDate, 1));
                    if (flights1.size() > 0) {
                        RouteEntity route2 = getDirectRoute(directDestination, destination);
                        if (route2 != null) {
                            Date lowerBound = flights1.get(0).getDepartureDate();
                            Date upperBound = getDateAfterDays(lowerBound, 1);

                            List<FlightEntity> flights2 = getAvailableFlights(route2, lowerBound, upperBound);

                            if (flights2.size() > 0) {
                                TransferFlight transferFlight = new TransferFlight(route1, route2, flights1, flights2);
                                transferFlights.add(transferFlight);
                            }
                        }
                    }
                }
            }
        }

        return transferFlights;
    }

    @Override
    public Date getDateAfterDays(Date origin, int daysToadd) {
        Calendar after = Calendar.getInstance();
        after.setTime(origin);
        after.add(Calendar.DATE, daysToadd);
        return after.getTime();
    }

    // for a given flight and seatClass, return all bookingclass under that seat class which has available quota
    @Override
    public List<BookingClassEntity> getAvailableBookingClassUnderFlightUnderSeatClass(FlightEntity flight, String seatClass, int totalPurchaseNo) {

        List<BookingClassEntity> allBookingClassesUnderFlight = flight.getBookingClasses();
        List<BookingClassEntity> allBookingClassUnderFlightUnderSeatClass = new ArrayList<>();

        for (BookingClassEntity bookingClassEntity : allBookingClassesUnderFlight) {
            if (bookingClassEntity.getSeatClass().equals(seatClass) && getQuotaLeft(bookingClassEntity) > totalPurchaseNo) {
                allBookingClassUnderFlightUnderSeatClass.add(bookingClassEntity);
            }
        }

        return allBookingClassUnderFlightUnderSeatClass;
    }

    // return whether two airports are reachable within 1 stop of transfer.
    @Override
    public boolean reachable(AirportEntity origin, AirportEntity destination) {
        Query queryForDirectRoute = entityManager.createQuery("select r from RouteEntity r where r.originAirport = :originAirport and r.destinationAirport = :destinationAirport");
        queryForDirectRoute.setParameter("originAirport", origin);
        queryForDirectRoute.setParameter("destinationAirport", destination);
        if (queryForDirectRoute.getResultList().size() > 0) {
            return true;
        } else {
            Query queryFor1Destination = entityManager.createQuery("select r.destinationAirport from RouteEntity r where r.originAirport = :originAirport");
            queryFor1Destination.setParameter("originAirport", origin);
            List<AirportEntity> firstDestinationAirports = (List<AirportEntity>) queryFor1Destination.getResultList();

            Query queryFor1Origin = entityManager.createQuery("select r.originAirport from RouteEntity r where r.destinationAirport = :destinationAirport");
            queryFor1Origin.setParameter("destinationAirport", destination);
            List<AirportEntity> firstOriginAirports = (List<AirportEntity>) queryFor1Origin.getResultList();

            for (AirportEntity a : firstOriginAirports) {
                if (firstDestinationAirports.contains(a)) {
                    return true;
                }
            }
            return false;
        }

    }

    @Override
    public void setAllFlightYearsBack(boolean departured, int year) {
        Query queryForAllFlights = entityManager.createQuery("SELECT f FROM FlightEntity f");
        List<FlightEntity> allFlights = queryForAllFlights.getResultList();
        for (FlightEntity f : allFlights) {

            f.setDepartured(departured);

            Date originalDate = f.getDepartureDate();
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(originalDate);
            calendar.add(Calendar.YEAR, -year);
            Date newDate = calendar.getTime();
            f.setDepartureDate(newDate);

            Date originalArrival = f.getArrivalDate();
            Calendar calendarArrival = Calendar.getInstance();
            calendarArrival.setTime(originalArrival);
            calendarArrival.add(Calendar.YEAR, -year);
            Date newArrival = calendarArrival.getTime();
            f.setArrivalDate(newArrival);
        }
    }

    @Override
    public void addTicketsToAllDeparturedFlights() {
        Query queryForAllDeparturedFlights = entityManager.createQuery("SELECT f FROM FlightEntity f WHERE f.departured = :departured");
        queryForAllDeparturedFlights.setParameter("departured", true);
        List<FlightEntity> allFlights = queryForAllDeparturedFlights.getResultList();

        int count = 0;
        for (FlightEntity f : allFlights) {
            if (f.getTickets().isEmpty()) {
                if (count < 1000) {
                    count++;
                } else {
                    break;
                }

                List<BookingClassEntity> bcs = f.getBookingClasses();
                for (BookingClassEntity bc : bcs) {
                    int quota = bc.getQuota();
                    Random r = new Random();
                    for (int i = 0; i < quota * (0.2 + 0.8 * r.nextDouble()); i++) {
                        TicketEntity t = new TicketEntity(f, bc.getName(), bc.getPrice());
                        entityManager.persist(t);
                    }
                }

                System.out.println(count + " tickets generated for " + f);
            }
        }
        entityManager.flush();
    }

    @Override
    public void deleteAllFlights() {
        Query queryForAllFlights = entityManager.createQuery("SELECT f FROM FlightEntity f");
        List<FlightEntity> allFlights = queryForAllFlights.getResultList();
        for (FlightEntity f : allFlights) {
            f.setReverseFlight(null);
//            f.getReverseFlight().setReverseFlight(null);
            entityManager.remove(f);
        }
    }

    @Override
    public void randomSetTicketsIssued() {
        Random r = new Random();

        Query queryForAllTickets = entityManager.createQuery("SELECT t FROM TicketEntity t WHERE t.id >= 1480000");
        List<TicketEntity> allTickets = queryForAllTickets.getResultList();

        int count = 0;
        for (TicketEntity t : allTickets) {
//            if (count < 1000) {
                    count++;
//                } else {
//                    break;
//                }
            System.out.println(count);
            if (r.nextDouble() < 0.94) {
                t.setIssued(true);
            } else {
                t.setIssued(false);
            }
        }
        entityManager.flush();
    }

//    public void randomAssignMembersToTickets() {
//        Random r = new Random();
//
//        Query queryForAllMembers = entityManager.createQuery("SELECT m FROM MemberEntity m");
//        List<MemberEntity> allMembers = queryForAllMembers.getResultList();
//        int totalMember = allMembers.size();
//
//        Query queryForAllTickets = entityManager.createQuery("SELECT t FROM TicketEntity t");
//        List<TicketEntity> allTickets = queryForAllTickets.getResultList();
//
//        for (TicketEntity t : allTickets) {
//            if (r.nextDouble() < 0.65) {
//                int memberNo = (int) r.nextDouble() * totalMember;
//                MemberEntity m = allMembers.get(memberNo);
//                entityManager.refresh(m);
//                t.setMember(m);
//                entityManager.merge(t);
//
//                List<TicketEntity> originalTicketsOfMembers = m.getTickets();
//                originalTicketsOfMembers.add(t);
//                m.setTickets(originalTicketsOfMembers);
//                entityManager.merge(m);
//            }
//        }
//
//    }
}
