/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package imas.planning.sessionbean;

import imas.common.entity.CabinCrewEntity;

import imas.planning.entity.AircraftEntity;
import imas.planning.entity.AirportEntity;
import imas.planning.entity.FlightEntity;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

/**
 *
 * @author wutong
 */
@Stateless
public class CabinCrewSchedulingSessionBean implements CabinCrewSchedulingSessionBeanLocal {

    @PersistenceContext
    private EntityManager em;

    // Add business logic below. (Right-click in editor and choose
    // "Insert Code > Add Business Method")

    // Add business logic below. (Right-click in editor and choose
    // "Insert Code > Add Business Method")

    @Override
    public List<FlightEntity> CabinCrewScheduling(List<FlightEntity> flights, List<CabinCrewEntity> cabinCrews) {
        System.err.println("enter pilotScheduling");

        List<FlightEntity> flightsLeft = CabinScheduling(flights, cabinCrews);

        return flightsLeft;
    }

    @Override
    public List<FlightEntity> CabinScheduling(List<FlightEntity> flights, List<CabinCrewEntity> cabinCrews) {
        System.err.println("enter for short");
        for (CabinCrewEntity c : cabinCrews) {
            if (!flights.isEmpty()) {
                flights = oneCabinCrewScheduling(flights, c);
            }
        }
        return flights;
    }

    @Override
    public List<FlightEntity> oneCabinCrewScheduling(List<FlightEntity> flights, CabinCrewEntity cabinCrew) {
        System.err.println("enter one for short");
        List<FlightEntity> flightsAvai = new ArrayList<FlightEntity>();
        for (FlightEntity f : flights) {

            if (f.getAircraft() != null) {
                if (f.getCabinCrews()==null){
                    f.setCabinCrews(new ArrayList<CabinCrewEntity>());   
                    flightsAvai.add(f);
                }
                else{
                    if(f.getCabinCrews().size()<getFlightCapacity(f)){
                        flightsAvai.add(f);
                    }
                }
                    
                   
  
            }
        }
        System.err.println("after filter available flights according to pilot");
        if (flightsAvai.isEmpty()) {
            return flights;
        }
        Date earliestDep = flightsAvai.get(0).getDepartureDate();
        boolean hasHubOrNot = false;
        FlightEntity earliestFlight = new FlightEntity();

        if (cabinCrew.getCabinCrewFlights() == null || cabinCrew.getCabinCrewFlights().isEmpty()) {
            System.err.println("1st assign job start");
            Calendar cal = Calendar.getInstance();
            cal.setTime(earliestDep);
            cal.add(Calendar.YEAR, 1);
            earliestDep = cal.getTime();

            for (FlightEntity f : flightsAvai) {
                Date depTemp = f.getDepartureDate();
                if (depTemp.compareTo(earliestDep) < 0 && cabinCrew.getBase().getAirportCode().equals(f.getRoute().getOriginAirport().getAirportCode())) {
                    //Find the earliest flight which departs at the aircraft's hub
                    earliestFlight = em.find(FlightEntity.class, f.getId());
                    earliestDep = f.getDepartureDate();
                    hasHubOrNot = true;

                }

            }
            System.err.println("1st assign job");
        } else {
            earliestDep = cabinCrew.getCabinCrewFlights().get(0).getArrivalDate();
            Date latestDate = null;
            for (FlightEntity f : cabinCrew.getCabinCrewFlights()) {
                if (f.getArrivalDate().compareTo(earliestDep) > 0) {
                    earliestDep = f.getArrivalDate();
                    latestDate = f.getArrivalDate();
                }
            }

            for (FlightEntity f : flightsAvai) {
                Date depTemp = f.getDepartureDate();
                if (depTemp.compareTo(latestDate) > 0 && cabinCrew.getBase().getAirportCode().equals(f.getRoute().getOriginAirport().getAirportCode())) {
                    //Find the earliest flight which departs at the aircraft's hub
                    earliestFlight = em.find(FlightEntity.class, f.getId());
                    earliestDep = f.getDepartureDate();
                    hasHubOrNot = true;

                }

            }

            if (hasHubOrNot) {
                for (FlightEntity f : flightsAvai) {
                    if (f.getDepartureDate().compareTo(latestDate) > 0 && f.getDepartureDate().compareTo(earliestDep) < 0 && f.getDepartureDate().compareTo(earliestDep) < 0 && cabinCrew.getBase().getAirportCode().equals(f.getRoute().getOriginAirport().getAirportCode())) {
                        //Find the earliest flight which departs at the aircraft's hub
                        earliestFlight = em.find(FlightEntity.class, f.getId());
                        earliestDep = f.getDepartureDate();
                        hasHubOrNot = true;

                    }

                }
            }
            System.err.println("2nd assigned");
        }
        //      System.err.println("2 earliest Dep"+earliestDep);

//        System.err.println("3 + earliest flight"+earliestFlight.getDepartureDate());
        if (!hasHubOrNot) {
            //           System.err.println("all flights do not departure at the aircraft's hub");
            return flightsAvai;

        }
        if (cabinCrew.getCabinCrewFlights() != null) {
            cabinCrew.getCabinCrewFlights().add(earliestFlight);

        } else {
            cabinCrew.setCabinCrewFlights(new ArrayList<FlightEntity>());
            cabinCrew.getCabinCrewFlights().add(earliestFlight);
        }

        if (earliestFlight.getCabinCrews()!= null) {
            earliestFlight.getCabinCrews().add(cabinCrew);
        } else {
            earliestFlight.setCabinCrews(new ArrayList<CabinCrewEntity>());
            earliestFlight.getCabinCrews().add(cabinCrew);

        }

        earliestDep = earliestFlight.getArrivalDate();
        if (earliestFlight.getCabinCrews().size() >= getFlightCapacity(earliestFlight)) {
            flightsAvai.remove(earliestFlight);
        }

        FlightEntity flightAssigned = earliestFlight;
        boolean findNextFlight = true;
        AirportEntity currentLoc = flightAssigned.getRoute().getDestinationAirport();
        //      System.err.println("5");
        Date mtAcc = flightAssigned.getDepartureDate();
        //      System.err.println("mtacc before while"+mtAcc);
        while (findNextFlight) {
//        System.err.println("5.1");
            Calendar cal = Calendar.getInstance();
            if (flightAssigned.getRoute().getFlightHours() > 8) {
                cal.setTime(earliestDep);
                cal.add(Calendar.HOUR, 2);
                earliestDep = cal.getTime();
            }
//        System.err.println("6");
//            System.err.println("earliestDep"+earliestDep);
            findNextFlight = false;
            Date findSoonest = null;
            for (FlightEntity f : flightsAvai) {

                if (currentLoc.equals(cabinCrew.getBase())) {
                    if (f.getRoute().getOriginAirport().getAirportCode().equals(currentLoc.getAirportCode()) && f.getDepartureDate().compareTo(earliestDep) > 0) {
                        findSoonest = f.getDepartureDate();
                        flightAssigned = em.find(FlightEntity.class, f.getId());
                    }
                } else if (f.getRoute().getDestinationAirport().getAirportCode().equals(cabinCrew.getBase().getAirportCode()) && f.getRoute().getOriginAirport().getAirportCode().equals(currentLoc.getAirportCode()) && f.getDepartureDate().compareTo(earliestDep) > 0) {
                    findSoonest = f.getDepartureDate();
                    flightAssigned = em.find(FlightEntity.class, f.getId());
                }

            }
            System.err.println("find a suitable flight");
            //find a suitable flight
            if (findSoonest == null) {
                findNextFlight = false;
            } else {

                for (FlightEntity f : flightsAvai) {

                    if (currentLoc.equals(cabinCrew.getBase())) {
                        if (f.getRoute().getOriginAirport().getAirportCode().equals(currentLoc.getAirportCode()) && f.getDepartureDate().compareTo(earliestDep) > 0 && f.getDepartureDate().compareTo(findSoonest) < 0) {
                            flightAssigned = em.find(FlightEntity.class, f.getId());
                            findSoonest = f.getDepartureDate();
                            findNextFlight = true;
                        }
                    } else if (f.getRoute().getDestinationAirport().getAirportCode().equals(cabinCrew.getBase().getAirportCode()) && f.getRoute().getOriginAirport().getAirportCode().equals(currentLoc.getAirportCode()) && f.getDepartureDate().compareTo(earliestDep) > 0 && f.getDepartureDate().compareTo(findSoonest) < 0) {
                        flightAssigned = em.find(FlightEntity.class, f.getId());
                        findSoonest = f.getDepartureDate();
                        findNextFlight = true;
                    }

                }
            }
            System.err.println("find nearest flight");
            //find the nearest flight
//         System.err.println("7");

            //         System.err.println("7.1 flyingHours"+flyingHoursAC);
            if (findNextFlight) {

                cabinCrew.getCabinCrewFlights().add(flightAssigned);
                flightAssigned.getCabinCrews().add(cabinCrew);
                if (flightAssigned.getCabinCrews().size() >= getFlightCapacity(flightAssigned)) {
                    flightsAvai.remove(flightAssigned);
                }
                em.merge(cabinCrew);

                earliestDep = flightAssigned.getArrivalDate(); // later can change to calculate 
                currentLoc = flightAssigned.getRoute().getDestinationAirport();
                //                      System.err.println("7.2.1");
                long diffInHours = TimeUnit.MILLISECONDS.toHours(flightAssigned.getArrivalDate().getTime() - mtAcc.getTime());
                System.err.println("findNextFlight is tru and diffHours = " + diffInHours);
                if (diffInHours > 96 && currentLoc.getAirportCode().equals(cabinCrew.getBase().getAirportCode())) {
                    //               System.err.println("flightAssigned FLIGHT hours"+flightAssigned.getRoute().getFlightHours());

                    cal.setTime(earliestDep);
                    cal.add(Calendar.HOUR, 72);
                    //              System.err.println("maintenance hours requireed:"+aircraft.getAircraftType().getMaintenanceHoursRequiredACheck()+" sishewuru"+(int) (aircraft.getAircraftType().getMaintenanceHoursRequiredACheck() + 0.5d));
                    earliestDep = cal.getTime();

                    currentLoc = cabinCrew.getBase();
                    mtAcc = earliestDep;
                    System.err.println("mtAcc = " + mtAcc);
//                       System.err.println("7.2");

                }
                //            System.err.println("8");

            }
        }

        em.merge(cabinCrew);
        return flightsAvai;
        //route with higher demand operate with larger aircraft
        //longer distance larger aircraft
        //maintenance time 
        //remember to destroy the flights list after one year planning
        //B Check
        //before executing the function need to have maintenance hours required
    }

    @Override
    public Integer getFlightCapacity(FlightEntity flight) {
        AircraftEntity a = flight.getAircraft();
        Integer sum = 0;
        Query q1 = em.createQuery("SELECT s FROM SeatEntity s WHERE s.aircraft = :aircraft AND s.seatClass = :seatClass");
        q1.setParameter("aircraft", a);
        q1.setParameter("seatClass", "First Class");
        Integer n1 = q1.getResultList().size();
        int a1 = n1 / 10;
        if (n1 % 10 > 0) {
            a1++;
        }
        sum = sum + a1;

        Query q2 = em.createQuery("SELECT s FROM SeatEntity s WHERE s.aircraft = :aircraft AND s.seatClass = :seatClass");
        q2.setParameter("aircraft", a);
        q2.setParameter("seatClass", "Premium Economy Class");
        Integer n2 = q2.getResultList().size();
        int a2 = n2 / 20;
        if (n2 % 20 > 0) {
            a2++;
        }
        sum = sum + a2;

        Query q3 = em.createQuery("SELECT s FROM SeatEntity s WHERE s.aircraft = :aircraft AND s.seatClass = :seatClass");
        q3.setParameter("aircraft", a);
        q3.setParameter("seatClass", "Economy Class");
        Integer n3 = q3.getResultList().size();
        int a3 = n3 / 30;
        if (n3 % 30 > 0) {
            a3++;
        }
        sum = sum + a3;

        Query q4 = em.createQuery("SELECT s FROM SeatEntity s WHERE s.aircraft = :aircraft AND s.seatClass = :seatClass");
        q4.setParameter("aircraft", a);
        q4.setParameter("seatClass", "Business Class");
        Integer n4 = q4.getResultList().size();
        int a4 = n4 / 15;
        if (n4 % 15 > 0) {
            a4++;
        }
        sum = sum + a4;

        return sum;
    }

    @Override
    public List<CabinCrewEntity> retrieveAllCabinCrew() {
        Query q = em.createQuery("SELECT a FROM CabinCrewEntity a");
        return (List<CabinCrewEntity>) q.getResultList();
        
    }
    
    
}
