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
import imas.planning.entity.RouteEntity;
import java.text.SimpleDateFormat;

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
    private int counter = 0;

    @Override
    public List<FlightEntity> CabinCrewScheduling(List<FlightEntity> flights, List<CabinCrewEntity> cabinCrews) {
        System.err.println("enter cabin Scheduling");

        List<FlightEntity> flightsLeft = CabinScheduling(flights, cabinCrews);

        return flightsLeft;
    }

    @Override
    public List<FlightEntity> CabinScheduling(List<FlightEntity> flights, List<CabinCrewEntity> cabinCrews) {
        System.err.println("enter for short");
        for (CabinCrewEntity c : cabinCrews) {
            if(c.getSeniority()!=null){
            if (!flights.isEmpty()&&c.getSeniority().equals("A")) {
                flights = oneCabinCrewScheduling(flights, c);
            }
           }
        }
        for (CabinCrewEntity c : cabinCrews) {
            if(c.getSeniority()!=null){
            if (!flights.isEmpty()&&c.getSeniority().equals("B")) {
                flights = oneCabinCrewScheduling(flights, c);
            }
           }
        }
        for (CabinCrewEntity c : cabinCrews) {
            if(c.getSeniority()!=null){
            if (!flights.isEmpty()&&c.getSeniority().equals("C")) {
                flights = oneCabinCrewScheduling(flights, c);
            }
           }
        }
        for (CabinCrewEntity c : cabinCrews) {
            if(c.getSeniority()!=null){
            if (!flights.isEmpty()&&c.getSeniority().equals("D")) {
                flights = oneCabinCrewScheduling(flights, c);
            }
           }
        }
        for (CabinCrewEntity c : cabinCrews) {
            if(c.getSeniority()!=null){
            if (!flights.isEmpty()&&c.getSeniority().equals("E")) {
                flights = oneCabinCrewScheduling(flights, c);
            }
           }
        }
        for (CabinCrewEntity c : cabinCrews) {
            
            if (!flights.isEmpty()&&c.getSeniority()==null) {
                flights = oneCabinCrewScheduling(flights, c);
            }
           
        }
        
        return flights;
    }

    @Override
    public List<FlightEntity> oneCabinCrewScheduling(List<FlightEntity> flights, CabinCrewEntity cabinCrew) {
        counter = 0;
        SimpleDateFormat formatter = new SimpleDateFormat("EEEE");
        System.err.println("enter one for short");
        List<FlightEntity> flightsAvai = new ArrayList<FlightEntity>();
        for (FlightEntity f : flights) {

            if (f.getAircraft() != null) {
                if (f.getCabinCrews() == null) {
                    f.setCabinCrews(new ArrayList<CabinCrewEntity>());
                    flightsAvai.add(f);
                } else {

                    if (f.getCabinCrews().size() < getFlightCapacity(f)) {
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
            cal.add(Calendar.YEAR, 50);
            earliestDep = cal.getTime();

            
            if (cabinCrew.getPreferredDay()!=null&&cabinCrew.getPreferredRoutes()!=null) {
                    

            for (FlightEntity f : flightsAvai) {
                Date depTemp = f.getDepartureDate();

                if (formatter.format(depTemp).equals(cabinCrew.getPreferredDay())
                        && depTemp.compareTo(earliestDep) < 0
                        && cabinCrew.getBase().getAirportCode().equals(f.getRoute().getOriginAirport().getAirportCode())) {
                    //Find the earliest flight which departs at the aircraft's hub
                    boolean findPreferedRoute = false;
                    for (RouteEntity r : cabinCrew.getPreferredRoutes()) {
                        if (f.getRoute().getOriginAirport().getAirportName().equals(r.getOriginAirport().getAirportName())
                                && f.getRoute().getDestinationAirport().getAirportName().equals(r.getDestinationAirport().getAirportName())) {
                            findPreferedRoute = true;
                        }

                        if (f.getRoute().getOriginAirport().getAirportName().equals(r.getDestinationAirport().getAirportName())
                                && f.getRoute().getDestinationAirport().getAirportName().equals(r.getDestinationAirport().getAirportName())) {
                            findPreferedRoute = true;
                        }

                    }
                    if (findPreferedRoute) {
                        earliestFlight = em.find(FlightEntity.class, f.getId());
                        earliestDep = f.getDepartureDate();
                        hasHubOrNot = true;
                    }

                }

            }
        }  
        if (cabinCrew.getPreferredDay()!=null) {
                    
            if (!hasHubOrNot) {
                for (FlightEntity f : flightsAvai) {
                    Date depTemp = f.getDepartureDate();

                    if (formatter.format(depTemp).equals(cabinCrew.getPreferredDay())
                            && depTemp.compareTo(earliestDep) < 0
                            && cabinCrew.getBase().getAirportCode().equals(f.getRoute().getOriginAirport().getAirportCode())) {
                        //Find the earliest flight which departs at the aircraft's hub

                        earliestFlight = em.find(FlightEntity.class, f.getId());
                        earliestDep = f.getDepartureDate();
                        hasHubOrNot = true;

                    }

                }
            }
        }
            if (!hasHubOrNot) {
                for (FlightEntity f : flightsAvai) {
                    Date depTemp = f.getDepartureDate();

                    if (depTemp.compareTo(earliestDep) < 0 && cabinCrew.getBase().getAirportCode().equals(f.getRoute().getOriginAirport().getAirportCode())) {
                        //Find the earliest flight which departs at the aircraft's hub
                        earliestFlight = em.find(FlightEntity.class, f.getId());
                        earliestDep = f.getDepartureDate();
                        hasHubOrNot = true;

                    }

                }
            }

            System.err.println("1st assign job");
        } else {
              Calendar cal = Calendar.getInstance();
            cal.setTime(earliestDep);
            cal.add(Calendar.YEAR, 50);
            earliestDep = cal.getTime();

            Date latestDate = cabinCrew.getCabinCrewFlights().get(0).getArrivalDate();
            FlightEntity lastFlight = cabinCrew.getCabinCrewFlights().get(0);
            for (FlightEntity f : cabinCrew.getCabinCrewFlights()) {
                if (f.getArrivalDate().compareTo(latestDate) > 0) {
                    latestDate = f.getArrivalDate();
                    lastFlight = f;
                    earliestFlight = f;

                }
            }
            if (cabinCrew.getPreferredDay()!=null&&cabinCrew.getPreferredRoutes()!=null) {
                    
            for (FlightEntity f : flightsAvai) {
                if (formatter.format(f.getDepartureDate()).equals(cabinCrew.getPreferredDay())
                        && f.getDepartureDate().compareTo(latestDate) > 0
                        && f.getDepartureDate().compareTo(earliestDep) < 0
                        && cabinCrew.getBase().getAirportCode().equals(f.getRoute().getOriginAirport().getAirportCode())) {
                    //Find the earliest flight which departs at the aircraft's hub
                    boolean findPreferedRoute = false;
                    for (RouteEntity r : cabinCrew.getPreferredRoutes()) {
                        if (f.getRoute().getOriginAirport().getAirportName().equals(r.getOriginAirport().getAirportName())
                                && f.getRoute().getDestinationAirport().getAirportName().equals(r.getDestinationAirport().getAirportName())) {
                            findPreferedRoute = true;
                        }

                        if (f.getRoute().getOriginAirport().getAirportName().equals(r.getDestinationAirport().getAirportName())
                                && f.getRoute().getDestinationAirport().getAirportName().equals(r.getDestinationAirport().getAirportName())) {
                            findPreferedRoute = true;
                        }

                    }
                    if (findPreferedRoute) {
                        earliestFlight = em.find(FlightEntity.class, f.getId());
                        earliestDep = f.getDepartureDate();
                        hasHubOrNot = true;
                    }

                }

            }
        }
        if (cabinCrew.getPreferredDay()!=null) {
                    
            
            if (!hasHubOrNot) {
                for (FlightEntity f : flightsAvai) {
                    if (formatter.format(f.getDepartureDate()).equals(cabinCrew.getPreferredDay())
                            && f.getDepartureDate().compareTo(latestDate) > 0
                            && f.getDepartureDate().compareTo(earliestDep) < 0
                            && cabinCrew.getBase().getAirportCode().equals(f.getRoute().getOriginAirport().getAirportCode())) {
                        //Find the earliest flight which departs at the aircraft's hub

                        earliestFlight = em.find(FlightEntity.class, f.getId());
                        earliestDep = f.getDepartureDate();
                        hasHubOrNot = true;

                    }

                }

            }
        }
            if (!hasHubOrNot) {
                for (FlightEntity f : flightsAvai) {
                    if (f.getDepartureDate().compareTo(latestDate) > 0
                            && f.getDepartureDate().compareTo(earliestDep) < 0
                            && cabinCrew.getBase().getAirportCode().equals(f.getRoute().getOriginAirport().getAirportCode())) {
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

        if (earliestFlight.getCabinCrews() != null) {
            earliestFlight.getCabinCrews().add(cabinCrew);
        } else {
            earliestFlight.setCabinCrews(new ArrayList<CabinCrewEntity>());
            earliestFlight.getCabinCrews().add(cabinCrew);

        }

        earliestDep = earliestFlight.getArrivalDate();
        if (earliestFlight.getCabinCrews().size() >= getFlightCapacity(earliestFlight)) {
            flightsAvai.remove(earliestFlight);
            flights.remove(earliestFlight);
           
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
            if (cabinCrew.getPreferredRoutes()!=null) {
                    
            for (FlightEntity f : flightsAvai) {

                if (currentLoc.equals(cabinCrew.getBase())) {
                    if (f.getRoute().getOriginAirport().getAirportCode().equals(currentLoc.getAirportCode()) && f.getDepartureDate().compareTo(earliestDep) > 0) {
                        boolean findPreferedRoute = false;
                        for (RouteEntity r : cabinCrew.getPreferredRoutes()) {
                            if (f.getRoute().getOriginAirport().getAirportName().equals(r.getOriginAirport().getAirportName())
                                    && f.getRoute().getDestinationAirport().getAirportName().equals(r.getDestinationAirport().getAirportName())) {
                                findPreferedRoute = true;
                            }

                            if (f.getRoute().getOriginAirport().getAirportName().equals(r.getDestinationAirport().getAirportName())
                                    && f.getRoute().getDestinationAirport().getAirportName().equals(r.getDestinationAirport().getAirportName())) {
                                findPreferedRoute = true;
                            }

                        }
                        if (findPreferedRoute) {
                            findSoonest = f.getDepartureDate();
                            flightAssigned = em.find(FlightEntity.class, f.getId());
                            findNextFlight = true;
                        }
                    }
                } else if (f.getRoute().getDestinationAirport().getAirportCode().equals(cabinCrew.getBase().getAirportCode()) && f.getRoute().getOriginAirport().getAirportCode().equals(currentLoc.getAirportCode()) && f.getDepartureDate().compareTo(earliestDep) > 0) {
                    boolean findPreferedRoute = false;
                    for (RouteEntity r : cabinCrew.getPreferredRoutes()) {
                        if (f.getRoute().getOriginAirport().getAirportName().equals(r.getOriginAirport().getAirportName())
                                && f.getRoute().getDestinationAirport().getAirportName().equals(r.getDestinationAirport().getAirportName())) {
                            findPreferedRoute = true;
                        }

                        if (f.getRoute().getOriginAirport().getAirportName().equals(r.getDestinationAirport().getAirportName())
                                && f.getRoute().getDestinationAirport().getAirportName().equals(r.getDestinationAirport().getAirportName())) {
                            findPreferedRoute = true;
                        }

                    }
                    if (findPreferedRoute) {
                        findSoonest = f.getDepartureDate();
                        flightAssigned = em.find(FlightEntity.class, f.getId());
                        findNextFlight = true;
                    }
                }

            }
        }
            if (!findNextFlight) {
                for (FlightEntity f : flightsAvai) {

                    if (currentLoc.equals(cabinCrew.getBase())) {
                        if (f.getRoute().getOriginAirport().getAirportCode().equals(currentLoc.getAirportCode()) && f.getDepartureDate().compareTo(earliestDep) > 0) {

                            findSoonest = f.getDepartureDate();
                            flightAssigned = em.find(FlightEntity.class, f.getId());
                            findNextFlight = true;

                        }
                    } else if (f.getRoute().getDestinationAirport().getAirportCode().equals(cabinCrew.getBase().getAirportCode()) && f.getRoute().getOriginAirport().getAirportCode().equals(currentLoc.getAirportCode()) && f.getDepartureDate().compareTo(earliestDep) > 0) {

                        findSoonest = f.getDepartureDate();
                        flightAssigned = em.find(FlightEntity.class, f.getId());
                        findNextFlight = true;

                    }

                }
            }
            System.err.println("find a suitable flight");
            //find a suitable flight
            if (findNextFlight) {
                
            boolean findPreferedRoute = false;
                if (cabinCrew.getPreferredRoutes()!=null) {
                
                for (FlightEntity f : flightsAvai) {

                    if (currentLoc.equals(cabinCrew.getBase())) {
                        if (f.getRoute().getOriginAirport().getAirportCode().equals(currentLoc.getAirportCode())
                                && f.getDepartureDate().compareTo(earliestDep) > 0
                                && f.getDepartureDate().compareTo(findSoonest) < 0) {

                            for (RouteEntity r : cabinCrew.getPreferredRoutes()) {
                                if (f.getRoute().getOriginAirport().getAirportName().equals(r.getOriginAirport().getAirportName())
                                        && f.getRoute().getDestinationAirport().getAirportName().equals(r.getDestinationAirport().getAirportName())) {
                                    findPreferedRoute = true;
                                }

                                if (f.getRoute().getOriginAirport().getAirportName().equals(r.getDestinationAirport().getAirportName())
                                        && f.getRoute().getDestinationAirport().getAirportName().equals(r.getDestinationAirport().getAirportName())) {
                                    findPreferedRoute = true;
                                }

                            }
                            if (findPreferedRoute) {

                                flightAssigned = em.find(FlightEntity.class, f.getId());
                                findSoonest = f.getDepartureDate();
                                findNextFlight = true;
                            }
                        }
                    } else if (f.getRoute().getDestinationAirport().getAirportCode().equals(cabinCrew.getBase().getAirportCode()) && f.getRoute().getOriginAirport().getAirportCode().equals(currentLoc.getAirportCode()) && f.getDepartureDate().compareTo(earliestDep) > 0 && f.getDepartureDate().compareTo(findSoonest) < 0) {
                        for (RouteEntity r : cabinCrew.getPreferredRoutes()) {
                            if (f.getRoute().getOriginAirport().getAirportName().equals(r.getOriginAirport().getAirportName())
                                    && f.getRoute().getDestinationAirport().getAirportName().equals(r.getDestinationAirport().getAirportName())) {
                                findPreferedRoute = true;
                            }

                            if (f.getRoute().getOriginAirport().getAirportName().equals(r.getDestinationAirport().getAirportName())
                                    && f.getRoute().getDestinationAirport().getAirportName().equals(r.getDestinationAirport().getAirportName())) {
                                findPreferedRoute = true;
                            }

                        }
                        if (findPreferedRoute) {
                            flightAssigned = em.find(FlightEntity.class, f.getId());
                            findSoonest = f.getDepartureDate();
                            findNextFlight = true;
                        }
                    }

                }
            }
                if (!findPreferedRoute) {
                    for (FlightEntity f : flightsAvai) {

                    if (currentLoc.equals(cabinCrew.getBase())) {
                        if (f.getRoute().getOriginAirport().getAirportCode().equals(currentLoc.getAirportCode())
                                && f.getDepartureDate().compareTo(earliestDep) > 0
                                && f.getDepartureDate().compareTo(findSoonest) < 0) {

                            

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
                    flights.remove(flightAssigned);
                }
                em.merge(cabinCrew);

                earliestDep = flightAssigned.getArrivalDate(); // later can change to calculate 
                currentLoc = flightAssigned.getRoute().getDestinationAirport();
                //                      System.err.println("7.2.1");
                long diffInHours = TimeUnit.MILLISECONDS.toHours(flightAssigned.getArrivalDate().getTime() - mtAcc.getTime());
                System.err.println("cabin findNextFlight is tru and diffHours = " + diffInHours);
                if (diffInHours >= 96 && currentLoc.getAirportCode().equals(cabinCrew.getBase().getAirportCode())) {
                    //               System.err.println("flightAssigned FLIGHT hours"+flightAssigned.getRoute().getFlightHours());

                    cal.setTime(earliestDep);
                    cal.add(Calendar.HOUR, 72);
                    //              System.err.println("maintenance hours requireed:"+aircraft.getAircraftType().getMaintenanceHoursRequiredACheck()+" sishewuru"+(int) (aircraft.getAircraftType().getMaintenanceHoursRequiredACheck() + 0.5d));
                    earliestDep = cal.getTime();

                    currentLoc = cabinCrew.getBase();
                    mtAcc = earliestDep;
                    System.err.println("cabin mtAcc = " + mtAcc);
//                       System.err.println("7.2");

                }
                //            System.err.println("8");

            }
        }

        em.merge(cabinCrew);
        return flights;
        //route with higher demand operate with larger aircraft
        //longer distance larger aircraft
        //maintenance time 
        //remember to destroy the flights list after one year planning
        //B Check
        //before executing the function need to have maintenance hours required
    }

    @Override
    public Integer getFlightCapacity(FlightEntity flight) {

        counter++;
        System.err.println("counter: " + counter);

        if (flight.getAircraft() != null) {
            AircraftEntity a = flight.getAircraft();
            int size = flight.getAircraft().getSeats().size();
            int numOfCrew = size / 50;
            if (size % 50 > 0) {
                numOfCrew++;
            }

            return numOfCrew;
        } else {
            return 0;
        }
    
    }

    @Override
    public List<CabinCrewEntity> retrieveAllCabinCrew() {
        Query q = em.createQuery("SELECT a FROM CabinCrewEntity a");
        return (List<CabinCrewEntity>) q.getResultList();

    }

}
