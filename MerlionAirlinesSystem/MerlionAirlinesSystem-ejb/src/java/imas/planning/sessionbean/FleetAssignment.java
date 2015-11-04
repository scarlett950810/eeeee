/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package imas.planning.sessionbean;

import imas.planning.entity.AircraftEntity;
import imas.planning.entity.AirportEntity;
import imas.planning.entity.FlightEntity;
import imas.planning.entity.MaintenanceScheduleEntity;
import imas.planning.entity.RouteEntity;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import javax.ejb.Stateful;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

/**
 *
 * @author ruicai
 */
@Stateful
public class FleetAssignment implements FleetAssignmentLocal {

    @PersistenceContext
    private EntityManager em;
    
    
    @Override
    public List<Date> getAllPlanningPeriodStartDateByYear() {
        Query q = em.createQuery("SELECT DISTINCT a.operatingYear FROM FlightEntity a");
        List<Integer> ops = (List<Integer>) q.getResultList();

        List<Date> result = new ArrayList<Date>();
        for (Integer i : ops) {
            //           System.err.println("operatingYear" + i);
            q = em.createQuery("SELECT a FROM FlightEntity a Where a.operatingYear = :year ORDER BY a.departureDate");
            q.setParameter("year", i);
            List<FlightEntity> fs = (List<FlightEntity>) q.getResultList();
            FlightEntity f = fs.get(0);
            result.add(f.getDepartureDate());
        }
        return result;
    }
    

    @Override
    public void deleteUnassginedFlights(List<FlightEntity> flightsUnassigned) {
        for (FlightEntity f : flightsUnassigned) {
            FlightEntity fD = em.find(FlightEntity.class, f.getId());
            if (fD != null) {
                FlightEntity fRD = em.find(FlightEntity.class, fD.getReverseFlight().getId());
                fD.setReverseFlight(null);
                fRD.setReverseFlight(null);
                em.remove(fD);
                em.remove(fRD);
            }

        }

    }

    // Add business logic below. (Right-click in editor and choose
    // "Insert Code > Add Business Method")

    @Override
    public List<FlightEntity> getAllFlightsWithinPlanningPeriod(Date startingDate) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(startingDate);
        cal.add(Calendar.YEAR, 1);
        Date endingDate = cal.getTime();
        Query q = em.createQuery("SELECT a FROM FlightEntity a WHERE a.departureDate >= :startingDate AND a.arrivalDate <= :endingDate");
        q.setParameter("startingDate", startingDate);
        q.setParameter("endingDate", endingDate);
        return (List<FlightEntity>) q.getResultList();
    }

    @Override
    public List<FlightEntity> fleetAssignment(List<FlightEntity> flights, List<AircraftEntity> aircrafts) {
        //       System.err.println("enter fleetAssignment");
        for (AircraftEntity a : aircrafts) {
            if (!flights.isEmpty()) {
                flights = oneAircraftAssignment(a, flights);
                //               System.err.println("finsh one fleetassignment");
            } 
        }
        //       System.err.println("finish fleetA"+flights.size());
        return flights;

    }

    @Override
    public List<FlightEntity> oneAircraftAssignment(AircraftEntity aircraft, List<FlightEntity> flightsAll) {
        List<FlightEntity> flightsAvai = new ArrayList<FlightEntity>();
        //        System.err.println("enter one aircraftAssignment");
        System.err.println("flight Size"+flightsAll.size());
        for (FlightEntity f : flightsAll) {
            if (aircraft.getAircraftType().getAircraftRange() > f.getRoute().getDistance() && f.getAircraft() == null) {
                flightsAvai.add(f);
            }
        }
        //find the available flights based on aircraf capability  
        if (flightsAvai.isEmpty()) {
            return flightsAll;
        }
        //       System.err.println("1");
        Calendar cal = Calendar.getInstance();
        Date earliestDep = flightsAvai.get(0).getDepartureDate();
        boolean hasHubOrNot = false;
        FlightEntity earliestFlight = new FlightEntity();
        if (aircraft.getFlights() == null || aircraft.getFlights().isEmpty()) {
            System.err.println("1st assign job start");
            cal.setTime(earliestDep);
            cal.add(Calendar.YEAR, 50);
            earliestDep = cal.getTime();

            for (FlightEntity f : flightsAvai) {
                Date depTemp = f.getDepartureDate();
                if (depTemp.compareTo(earliestDep) < 0 && aircraft.getAirportHub().getAirportCode().equals(f.getRoute().getOriginAirport().getAirportCode())) {
                    //Find the earliest flight which departs at the aircraft's hub
                    earliestFlight = em.find(FlightEntity.class, f.getId());
                    earliestDep = f.getDepartureDate();
                    hasHubOrNot = true;

                }

            }
            System.err.println("1st assign job");
        } else {
            earliestDep = aircraft.getFlights().get(0).getArrivalDate();
            Date latestDate = aircraft.getFlights().get(0).getArrivalDate();
            FlightEntity lastFlight = aircraft.getFlights().get(0);
            for (FlightEntity f : aircraft.getFlights()) {
                if (f.getArrivalDate().compareTo(earliestDep) > 0) {
                    earliestDep = f.getArrivalDate();
                    earliestFlight = f;
                    latestDate = f.getArrivalDate();
                    lastFlight = f;
                }
            }

            for (FlightEntity f : flightsAvai) {
                Date depTemp = f.getDepartureDate();
                if (depTemp.compareTo(latestDate) > 0 && lastFlight.getRoute().getDestinationAirport().getAirportCode().equals(f.getRoute().getOriginAirport().getAirportCode())) {
                    //Find the earliest flight which departs at the aircraft's hub
                    earliestFlight = em.find(FlightEntity.class, f.getId());
                    earliestDep = f.getDepartureDate();
                    hasHubOrNot = true;

                }

            }

            if (hasHubOrNot) {
                for (FlightEntity f : flightsAvai) {
                    if (f.getDepartureDate().compareTo(latestDate) > 0 && f.getDepartureDate().compareTo(earliestDep) < 0 && f.getDepartureDate().compareTo(earliestDep) < 0 && lastFlight.getRoute().getDestinationAirport().getAirportCode().equals(f.getRoute().getOriginAirport().getAirportCode())) {
                        //Find the earliest flight which departs at the aircraft's hub
                        earliestFlight = em.find(FlightEntity.class, f.getId());
                        earliestDep = f.getDepartureDate();
                        hasHubOrNot = true;

                    }

                }
            }
            System.err.println("2nd assigned");
        }

        System.err.println("3 + earliest flight"+earliestFlight.getDepartureDate());
        if (!hasHubOrNot) {
                System.err.println("all flights do not departure at the aircraft's hub");
            return flightsAll;

        }

        if (aircraft.getFlights() != null) {
            aircraft.getFlights().add(earliestFlight);

        } else {
            aircraft.setFlights(new ArrayList<FlightEntity>());
            aircraft.getFlights().add(earliestFlight);
        }
        earliestFlight.setAircraft(aircraft);
        earliestDep = earliestFlight.getArrivalDate();
        //       System.err.println("4");

        flightsAvai.remove(earliestFlight);
        flightsAll.remove(earliestFlight);
        //assign the first flight
        FlightEntity flightAssigned = earliestFlight;
        boolean findNextFlight = true;
        AirportEntity currentLoc = flightAssigned.getRoute().getDestinationAirport();
        //      System.err.println("5");
        Date mtAcc = flightAssigned.getDepartureDate();
        //      System.err.println("mtacc before while"+mtAcc);
        while (findNextFlight) {
//        System.err.println("5.1");

            cal.setTime(earliestDep);
            cal.add(Calendar.MINUTE, (int) (aircraft.getTurnAroundTime() + 0.5d));

            earliestDep = cal.getTime();
//        System.err.println("6");
//            System.err.println("earliestDep"+earliestDep);
            findNextFlight = false;
            Date findSoonest = null;
            Double flyingHoursAC = calculateMaintenanceHours(aircraft, mtAcc);
             boolean findHub = false;
                if(flyingHoursAC >= 70){
                    
                    
                for (FlightEntity f : flightsAvai) {

                    if (currentLoc.equals(aircraft.getAirportHub())) {
                        if (f.getRoute().getOriginAirport().getAirportCode().equals(currentLoc.getAirportCode()) && f.getDepartureDate().compareTo(earliestDep) > 0 ) {
                            flightAssigned = em.find(FlightEntity.class, f.getId());
                            findSoonest = f.getDepartureDate();
                            findHub = true;
                             findNextFlight = true;
                        }
                    } else if (f.getRoute().getDestinationAirport().getAirportCode().equals(aircraft.getAirportHub().getAirportCode()) && f.getRoute().getOriginAirport().getAirportCode().equals(currentLoc.getAirportCode()) && f.getDepartureDate().compareTo(earliestDep) > 0 ) {
                        flightAssigned = em.find(FlightEntity.class, f.getId());
                        findSoonest = f.getDepartureDate();
                        findHub = true;
                         findNextFlight = true;
                    }

                }
                    if(!findHub)
                    {
                         for (FlightEntity f : flightsAvai) {

                    
                        if (f.getRoute().getOriginAirport().getAirportCode().equals(currentLoc.getAirportCode()) && f.getDepartureDate().compareTo(earliestDep) > 0 ) {
                            flightAssigned = em.find(FlightEntity.class, f.getId());
                            findSoonest = f.getDepartureDate();
                          
                             findNextFlight = true;
                        }
                        }
                        
                    }                
                }else{
                    
                         for (FlightEntity f : flightsAvai) {

                    
                        if (f.getRoute().getOriginAirport().getAirportCode().equals(currentLoc.getAirportCode()) && f.getDepartureDate().compareTo(earliestDep) >0) {
                            flightAssigned = em.find(FlightEntity.class, f.getId());
                            findSoonest = f.getDepartureDate();
                             findNextFlight = true;
                           
                        }
                        }
                        
                }
            
             
            //find a suitable flight
            if (findSoonest == null) {
                findNextFlight = false;
            } else {
                
                
                if(flyingHoursAC >= 70){
                    
                   if(findHub){
                for (FlightEntity f : flightsAvai) {

                    if (currentLoc.equals(aircraft.getAirportHub())) {
                        if (f.getRoute().getOriginAirport().getAirportCode().equals(currentLoc.getAirportCode()) && f.getDepartureDate().compareTo(earliestDep) > 0 && f.getDepartureDate().compareTo(findSoonest) < 0) {
                            flightAssigned = em.find(FlightEntity.class, f.getId());
                            findSoonest = f.getDepartureDate();
                            
                        }
                    } else if (f.getRoute().getDestinationAirport().getAirportCode().equals(aircraft.getAirportHub().getAirportCode()) && f.getRoute().getOriginAirport().getAirportCode().equals(currentLoc.getAirportCode()) && f.getDepartureDate().compareTo(earliestDep) > 0 && f.getDepartureDate().compareTo(findSoonest) < 0) {
                        flightAssigned = em.find(FlightEntity.class, f.getId());
                        findSoonest = f.getDepartureDate();
                        
                    }

                }
                   }else
                   {
                    
                         for (FlightEntity f : flightsAvai) {

                    
                        if (f.getRoute().getOriginAirport().getAirportCode().equals(currentLoc.getAirportCode()) && f.getDepartureDate().compareTo(earliestDep) > 0 && f.getDepartureDate().compareTo(findSoonest) < 0) {
                            flightAssigned = em.find(FlightEntity.class, f.getId());
                            findSoonest = f.getDepartureDate();
                           
                        }
                        }
                        
                    }                
                }else{
                    
                         for (FlightEntity f : flightsAvai) {

                    
                        if (f.getRoute().getOriginAirport().getAirportCode().equals(currentLoc.getAirportCode()) && f.getDepartureDate().compareTo(earliestDep) > 0 && f.getDepartureDate().compareTo(findSoonest) < 0) {
                            flightAssigned = em.find(FlightEntity.class, f.getId());
                            findSoonest = f.getDepartureDate();
                           
                        }
                        }
                        
                }
            }
            //find the nearest flight
//         System.err.println("7");

           
            //         System.err.println("7.1 flyingHours"+flyingHoursAC);

            if (findNextFlight) {
                //               System.err.println("findNextFlight is true");
                if (flyingHoursAC + flightAssigned.getRoute().getFlightHours() >= 125.0 && currentLoc.getAirportCode().equals(aircraft.getAirportHub().getAirportCode())) {
                    //               System.err.println("flightAssigned FLIGHT hours"+flightAssigned.getRoute().getFlightHours());
                    MaintenanceScheduleEntity maintenanceSchedule = new MaintenanceScheduleEntity();
                    maintenanceSchedule.setMaintenanceType("A");
                    maintenanceSchedule.setStartingTime(earliestDep);
                    cal.setTime(earliestDep);
                    cal.add(Calendar.HOUR, (int) (aircraft.getAircraftType().getMaintenanceHoursRequiredACheck() + 0.5d));
                    //              System.err.println("maintenance hours requireed:"+aircraft.getAircraftType().getMaintenanceHoursRequiredACheck()+" sishewuru"+(int) (aircraft.getAircraftType().getMaintenanceHoursRequiredACheck() + 0.5d));
                    earliestDep = cal.getTime();
                    maintenanceSchedule.setEndingTime(earliestDep);
                    if (aircraft.getMaintenances() != null) {
                        aircraft.getMaintenances().add(maintenanceSchedule);
                    } else {
                        aircraft.setMaintenances(new ArrayList<MaintenanceScheduleEntity>());
                        aircraft.getMaintenances().add(maintenanceSchedule);
                    }
                    maintenanceSchedule.setAircraft(aircraft);
                    em.merge(maintenanceSchedule);
                    em.merge(aircraft);

                    currentLoc = aircraft.getAirportHub();
                    mtAcc = earliestDep;
//                       System.err.println("7.2");

                } else {
                    aircraft.getFlights().add(flightAssigned);
                    flightAssigned.setAircraft(aircraft);
                    flightsAvai.remove(flightAssigned);
                    flightsAll.remove(flightAssigned);
                    em.merge(aircraft);

                    earliestDep = flightAssigned.getArrivalDate(); // later can change to calculate 
                    currentLoc = flightAssigned.getRoute().getDestinationAirport();
                    //                      System.err.println("7.2.1");

                }
            }
            //            System.err.println("8");

        }

        em.merge(aircraft);
        return flightsAll;
        //route with higher demand operate with larger aircraft
        //longer distance larger aircraft
        //maintenance time 
        //remember to destroy the flights list after one year planning
        //B Check
        //before executing the function need to have maintenance hours required
    }

    @Override
    public Double calculateMaintenanceHours(AircraftEntity aircraft, Date mtAcc) {
        //       System.err.println("date mtacc"+ mtAcc);
        Double flyingHours = 0.0;
        List<FlightEntity> fL = aircraft.getFlights();
        for (FlightEntity f : fL) {
            if (f.getDepartureDate().compareTo(mtAcc) > 0) {
                flyingHours = flyingHours + f.getRoute().getFlightHours();
            }
        }
        return flyingHours;
    }

    @Override
    public List<FlightEntity> retreiveDBrecords(RouteEntity route) {
        RouteEntity r = em.find(RouteEntity.class, route.getId());

        return r.getFlights();
    }

    @Override
    public List<FlightEntity> getAllFlights() {
        System.err.println("开始getAll Flights method ()");
        Query q = em.createQuery("SELECT a FROM FlightEntity a WHERE a.aircraft IS NULL");
        System.err.println("结束 getAll Flights method ()");
        System.err.println("...."+(List<FlightEntity>)q.getResultList());
    
        
        System.err.println("");
        return (List<FlightEntity>)q.getResultList();
        
    }

    @Override
    public List<FlightEntity> findUnassinedFlights() {
        Query q = em.createQuery("SELECT a FROM FlightEntity a WHERE a.aircraft IS NULL");
        return (List<FlightEntity>)q.getResultList();
    }

}
