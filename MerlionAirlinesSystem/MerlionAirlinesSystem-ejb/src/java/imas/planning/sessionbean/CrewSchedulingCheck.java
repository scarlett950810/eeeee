/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package imas.planning.sessionbean;

import imas.common.entity.CabinCrewEntity;
import imas.common.entity.PilotEntity;
import imas.planning.entity.AircraftEntity;
import imas.planning.entity.AirportEntity;
import imas.planning.entity.FlightEntity;
import imas.planning.entity.MaintenanceScheduleEntity;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.TimeUnit;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.ejb.TransactionManagement;
import javax.ejb.TransactionManagementType;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

/**
 *
 * @author ruicai
 */
@Stateless
@TransactionManagement(TransactionManagementType.BEAN)
public class CrewSchedulingCheck implements CrewSchedulingCheckLocal {

    @EJB
    private AircraftSessionBeanLocal aircraftSession;

    @PersistenceContext
    private EntityManager em;
    // Add business logic below. (Right-click in editor and choose
    // "Insert Code > Add Business Method")

    
    @Override
    public List<FlightEntity> pilotSchedulingCapacityCheck(List<FlightEntity> flights, List<PilotEntity> pilots) {
        System.err.println("enter pilotScheduling");

        flights = fleetAssignmentCheck(flights, aircraftSession.getAircrafts());

        System.err.println("after fleet check");
        List<FlightEntity> shortFlights = new ArrayList<FlightEntity>();
        List<FlightEntity> longFlights = new ArrayList<FlightEntity>();
        for (FlightEntity f : flights) {
            if (f.getRoute().getDistance() < 5000) {
                shortFlights.add(f);
            } else {
                longFlights.add(f);
            }
        }
        shortFlights = pilotSchedulingForShortDistance(shortFlights, pilots);
        List<PilotEntity> pilotsLongDis = new ArrayList<>();
        for (PilotEntity p : pilots) {
            if (p.getMileageLimit()) {
                pilotsLongDis.add(p);
            }
        }
        System.err.println("enter for loop for shortFlights");
        for (FlightEntity f : shortFlights) {
            longFlights.add(f);
        }
        System.err.println("enter long Flights");
        longFlights = pilotSchedulingForLongDistance(longFlights, pilotsLongDis);
        System.err.println("finish pilotshceduling");
        if(longFlights == null){
            System.err.println("longFlighs is NULL");
        }
        else
            System.err.println("size of the longFlights:"+longFlights.size());
        
        
        System.err.println("checkcheck");
        return longFlights;
        
    }

    
    @Override
    public List<FlightEntity> pilotSchedulingForShortDistance(List<FlightEntity> shortFlights, List<PilotEntity> pilots) {
        System.err.println("enter for short");
        List<FlightEntity> flights = shortFlights;
        for (PilotEntity p : pilots) {
            if (!flights.isEmpty()) {
                flights = onePilotSchedulingForShort(flights, p);
            }
        }
        return flights;
    }

    
    @Override
    public List<FlightEntity> onePilotSchedulingForShort(List<FlightEntity> flights, PilotEntity pilot) {
        System.err.println("enter one for short");
        List<FlightEntity> flightsAvai = new ArrayList<FlightEntity>();
        for (FlightEntity f : flights) {
            boolean b = true;
            for (String type : pilot.getAircraftTypeCapabilities()) {
                if (f.getPilots() == null) {
                    f.setPilots(new ArrayList<PilotEntity>());
                }
                if (f.getAircraft() != null) {
                    if (type.equals(f.getAircraft().getAircraftType().getIATACode()) && b == true && f.getPilots().size() < 2) {
                        b = false;
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

        if (pilot.getPilotFlights() == null || pilot.getPilotFlights().isEmpty()) {
            System.err.println("1st assign job start");
            Calendar cal = Calendar.getInstance();
            cal.setTime(earliestDep);
            cal.add(Calendar.YEAR, 50);
            earliestDep = cal.getTime();

            for (FlightEntity f : flightsAvai) {
                Date depTemp = f.getDepartureDate();
                if (depTemp.compareTo(earliestDep) < 0 && pilot.getBase().getAirportCode().equals(f.getRoute().getOriginAirport().getAirportCode())) {
                    //Find the earliest flight which departs at the aircraft's hub
                    earliestFlight = f;
                    earliestDep = f.getDepartureDate();
                    hasHubOrNot = true;

                }

            }
            System.err.println("1st assign job");
        } else {
            earliestDep = pilot.getPilotFlights().get(0).getArrivalDate();
            Date latestDate = pilot.getPilotFlights().get(0).getArrivalDate();
            FlightEntity lastFlight = pilot.getPilotFlights().get(0);
            for (FlightEntity f : pilot.getPilotFlights()) {
                if (f.getArrivalDate().compareTo(earliestDep) > 0) {
                    earliestDep = f.getArrivalDate();
                    latestDate = f.getArrivalDate();
                    lastFlight = f;
                    earliestFlight = f;
                    
                }
            }

            for (FlightEntity f : flightsAvai) {
                Date depTemp = f.getDepartureDate();
                if (depTemp.compareTo(latestDate) > 0 && lastFlight.getRoute().getDestinationAirport().getAirportCode().equals(f.getRoute().getOriginAirport().getAirportCode())) {
                    //Find the earliest flight which departs at the aircraft's hub
                    earliestFlight = f;                   
                    earliestDep = f.getDepartureDate();
                    hasHubOrNot = true;

                }

            }

            if (hasHubOrNot) {
                for (FlightEntity f : flightsAvai) {
                    if (f.getDepartureDate().compareTo(latestDate) > 0 && f.getDepartureDate().compareTo(earliestDep) < 0 && f.getDepartureDate().compareTo(earliestDep) < 0 && lastFlight.getRoute().getDestinationAirport().getAirportCode().equals(f.getRoute().getOriginAirport().getAirportCode())) {
                        //Find the earliest flight which departs at the aircraft's hub
                        earliestFlight = f;
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
        if (pilot.getPilotFlights() != null) {
            pilot.getPilotFlights().add(earliestFlight);

        } else {
            pilot.setPilotFlights(new ArrayList<FlightEntity>());
            pilot.getPilotFlights().add(earliestFlight);
        }

        if (earliestFlight.getPilots() != null) {
            earliestFlight.getPilots().add(pilot);
        } else {
            earliestFlight.setPilots(new ArrayList<PilotEntity>());
            earliestFlight.getPilots().add(pilot);

        }

        earliestDep = earliestFlight.getArrivalDate();
        if (earliestFlight.getPilots().size() >= 2) {
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

                if (currentLoc.equals(pilot.getBase())) {
                    if (f.getRoute().getOriginAirport().getAirportCode().equals(currentLoc.getAirportCode()) && f.getDepartureDate().compareTo(earliestDep) > 0) {
                        findSoonest = f.getDepartureDate();
                        flightAssigned = f;
                        findNextFlight = true;
                    }
                } else if (f.getRoute().getDestinationAirport().getAirportCode().equals(pilot.getBase().getAirportCode()) && f.getRoute().getOriginAirport().getAirportCode().equals(currentLoc.getAirportCode()) && f.getDepartureDate().compareTo(earliestDep) > 0) {
                    findSoonest = f.getDepartureDate();
                    flightAssigned = f;
                    findNextFlight = true;
                }

            }
            System.err.println("find a suitable flight");
            //find a suitable flight
            if (findSoonest == null) {
                findNextFlight = false;
            } else {

                for (FlightEntity f : flightsAvai) {

                    if (currentLoc.equals(pilot.getBase())) {
                        if (f.getRoute().getOriginAirport().getAirportCode().equals(currentLoc.getAirportCode()) && f.getDepartureDate().compareTo(earliestDep) > 0 && f.getDepartureDate().compareTo(findSoonest) < 0) {
                            flightAssigned = f;
                            findSoonest = f.getDepartureDate();
                            findNextFlight = true;
                        }
                    } else if (f.getRoute().getDestinationAirport().getAirportCode().equals(pilot.getBase().getAirportCode()) && f.getRoute().getOriginAirport().getAirportCode().equals(currentLoc.getAirportCode()) && f.getDepartureDate().compareTo(earliestDep) > 0 && f.getDepartureDate().compareTo(findSoonest) < 0) {
                        flightAssigned = f;
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

                pilot.getPilotFlights().add(flightAssigned);
                flightAssigned.getPilots().add(pilot);
                if (flightAssigned.getPilots().size() >= 2) {
                    flightsAvai.remove(flightAssigned);
                }

                earliestDep = flightAssigned.getArrivalDate(); // later can change to calculate 
                currentLoc = flightAssigned.getRoute().getDestinationAirport();
                //                      System.err.println("7.2.1");
                long diffInHours = TimeUnit.MILLISECONDS.toHours(flightAssigned.getArrivalDate().getTime() - mtAcc.getTime());
                System.err.println("findNextFlight is tru and diffHours = " + diffInHours);
                if (diffInHours >= 96 && currentLoc.getAirportCode().equals(pilot.getBase().getAirportCode())) {
                    //               System.err.println("flightAssigned FLIGHT hours"+flightAssigned.getRoute().getFlightHours());

                    cal.setTime(earliestDep);
                    cal.add(Calendar.HOUR, 72);
                    //              System.err.println("maintenance hours requireed:"+aircraft.getAircraftType().getMaintenanceHoursRequiredACheck()+" sishewuru"+(int) (aircraft.getAircraftType().getMaintenanceHoursRequiredACheck() + 0.5d));
                    earliestDep = cal.getTime();

                    currentLoc = pilot.getBase();
                    mtAcc = earliestDep;
                    System.err.println("mtAcc = " + mtAcc);
//                       System.err.println("7.2");

                }
                //            System.err.println("8");

            }
        }

        return flightsAvai;
        //route with higher demand operate with larger aircraft
        //longer distance larger aircraft
        //maintenance time 
        //remember to destroy the flights list after one year planning
        //B Check
        //before executing the function need to have maintenance hours required
    }

    
    @Override
    public List<FlightEntity> pilotSchedulingForLongDistance(List<FlightEntity> shortFlights, List<PilotEntity> pilots
    ) {
        List<FlightEntity> flights = shortFlights;
        System.err.println("number of pilots :" + pilots.size());
        for (PilotEntity p : pilots) {
            if (!flights.isEmpty()) {
                System.err.println("starting one for long" + p + "flightsnum" + flights.size());
                flights = onePilotSchedulingForLong(flights, p);
                System.err.println("finish one fo long" + p);
            }
        }
        System.err.println("sucess return for long");
        return flights;
    }

    
    @Override
    public List<FlightEntity> onePilotSchedulingForLong(List<FlightEntity> flights, PilotEntity pilot
    ) {
        List<FlightEntity> flightsAvai = new ArrayList<FlightEntity>();
        System.err.println("enter one for long");
        for (FlightEntity f : flights) {
            System.err.println("got things in flights");
            boolean b = true;

            System.err.println("flight" + f);
            System.err.println("flight distance" + f.getRoute().getDistance());
            System.err.println("list" + pilot.getAircraftTypeCapabilities());
            for (String type : pilot.getAircraftTypeCapabilities()) {
                System.err.println("enter gettype");
                System.err.println("type" + type);
                if (f.getPilots() == null) {
                    f.setPilots(new ArrayList<>());
                }
                System.err.println("size excepton");
                System.err.println("size exception" + f.getPilots().size());
                if (f.getAircraft() != null) {
                    if (type.equals(f.getAircraft().getAircraftType().getIATACode()) && b == true && ((f.getPilots().size() < 3 && f.getRoute().getDistance() >= 5000) || (f.getPilots().size() < 2 && f.getRoute().getDistance() < 5000))) {
                        b = false;
                        flightsAvai.add(f);
                    }
                }
            }
        }
        System.err.println("before ttest empty");
        if (flightsAvai.isEmpty()) {
            System.err.println("empty");
            return flights;
        }
        System.err.println("after enter one for long");
        Date earliestDep = flightsAvai.get(0).getDepartureDate();
        boolean hasHubOrNot = false;
        FlightEntity earliestFlight = new FlightEntity();

        if (pilot.getPilotFlights() == null || pilot.getPilotFlights().isEmpty()) {
            Calendar cal = Calendar.getInstance();
            cal.setTime(earliestDep);
            cal.add(Calendar.YEAR, 50);
            earliestDep = cal.getTime();
            System.err.println("before getBase");
            for (FlightEntity f : flightsAvai) {
                Date depTemp = f.getDepartureDate();
                if (depTemp.compareTo(earliestDep) < 0 && pilot.getBase().getAirportCode().equals(f.getRoute().getOriginAirport().getAirportCode())) {
                    //Find the earliest flight which departs at the aircraft's hub
                    earliestFlight = f;
                    earliestDep = f.getDepartureDate();
                    hasHubOrNot = true;

                }

            }
            System.err.println("after getBase");
        } else {
            System.err.println("already go some flights before getbase");
            earliestDep = pilot.getPilotFlights().get(0).getArrivalDate();
            Date latestDate = pilot.getPilotFlights().get(0).getArrivalDate();
            FlightEntity lastFlight = pilot.getPilotFlights().get(0);
            for (FlightEntity f : pilot.getPilotFlights()) {
                if (f.getArrivalDate().compareTo(earliestDep) > 0) {
                    earliestDep = f.getArrivalDate();
                    latestDate = f.getArrivalDate();
                    lastFlight = f;
                    earliestFlight = f;
                    
                }
            }

            for (FlightEntity f : flightsAvai) {
                Date depTemp = f.getDepartureDate();
                if (depTemp.compareTo(latestDate) > 0 && lastFlight.getRoute().getDestinationAirport().getAirportCode().equals(f.getRoute().getOriginAirport().getAirportCode())) {
                    //Find the earliest flight which departs at the aircraft's hub
                    earliestFlight = f;                   
                    earliestDep = f.getDepartureDate();
                    hasHubOrNot = true;

                }

            }
            System.err.println("already go some flights after getbase");

            if (hasHubOrNot) {
                for (FlightEntity f : flightsAvai) {
                    if (f.getDepartureDate().compareTo(latestDate) > 0 && f.getDepartureDate().compareTo(earliestDep) < 0 && f.getDepartureDate().compareTo(earliestDep) < 0 && pilot.getBase().getAirportCode().equals(f.getRoute().getOriginAirport().getAirportCode())) {
                        //Find the earliest flight which departs at the aircraft's hub
                        earliestFlight = f;
                        earliestDep = f.getDepartureDate();
                        hasHubOrNot = true;

                    }

                }
            }
            System.err.println("already got some flights ");

        }
        //      System.err.println("2 earliest Dep"+earliestDep);

//        System.err.println("3 + earliest flight"+earliestFlight.getDepartureDate());
        if (!hasHubOrNot) {
            //           System.err.println("all flights do not departure at the aircraft's hub");
            return flightsAvai;

        }
        if (pilot.getPilotFlights() != null) {
            pilot.getPilotFlights().add(earliestFlight);

        } else {
            pilot.setPilotFlights(new ArrayList<FlightEntity>());
            pilot.getPilotFlights().add(earliestFlight);
        }

        if (earliestFlight.getPilots() != null) {
            earliestFlight.getPilots().add(pilot);
        } else {
            earliestFlight.setPilots(new ArrayList<PilotEntity>());
            earliestFlight.getPilots().add(pilot);

        }

        earliestDep = earliestFlight.getArrivalDate();
        if ((earliestFlight.getPilots().size() >= 2 && earliestFlight.getRoute().getDistance() < 5000) || (earliestFlight.getPilots().size() >= 3 && earliestFlight.getRoute().getDistance() > 5000)) {
            flightsAvai.remove(earliestFlight);
        }

        FlightEntity flightAssigned = earliestFlight;
        boolean findNextFlight = true;
        AirportEntity currentLoc = flightAssigned.getRoute().getDestinationAirport();
        //      System.err.println("5");
        Date mtAcc = flightAssigned.getDepartureDate();
        //      System.err.println("mtacc before while"+mtAcc);
        System.err.println("before while");
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

                if (currentLoc.equals(pilot.getBase())) {
                    if (f.getRoute().getOriginAirport().getAirportCode().equals(currentLoc.getAirportCode()) && f.getDepartureDate().compareTo(earliestDep) > 0) {
                        findSoonest = f.getDepartureDate();
                        findNextFlight = true;
                        flightAssigned = f;
                    }
                } else if (f.getRoute().getDestinationAirport().getAirportCode().equals(pilot.getBase().getAirportCode()) && f.getRoute().getOriginAirport().getAirportCode().equals(currentLoc.getAirportCode()) && f.getDepartureDate().compareTo(earliestDep) > 0) {
                    findSoonest = f.getDepartureDate();
                    flightAssigned = f;
                    findNextFlight = true;
                }

            }
            //find a suitable flight
            if (findSoonest == null) {
                findNextFlight = false;
            } else {

                for (FlightEntity f : flightsAvai) {

                    if (currentLoc.equals(pilot.getBase())) {
                        if (f.getRoute().getOriginAirport().getAirportCode().equals(currentLoc.getAirportCode()) && f.getDepartureDate().compareTo(earliestDep) > 0 && f.getDepartureDate().compareTo(findSoonest) < 0) {
                            flightAssigned = f;
                            findSoonest = f.getDepartureDate();
                            findNextFlight = true;
                        }
                    } else if (f.getRoute().getDestinationAirport().getAirportCode().equals(pilot.getBase().getAirportCode()) && f.getRoute().getOriginAirport().getAirportCode().equals(currentLoc.getAirportCode()) && f.getDepartureDate().compareTo(earliestDep) > 0 && f.getDepartureDate().compareTo(findSoonest) < 0) {
                        flightAssigned = f;
                        findSoonest = f.getDepartureDate();
                        findNextFlight = true;
                    }

                }
            }
            //find the nearest flight
//         System.err.println("7");

            //           Date earliestT = calculateMaintenanceHours(pilot, mtAcc);
            //         System.err.println("7.1 flyingHours"+flyingHoursAC);
            if (findNextFlight) {

//                       System.err.println("7.2");
                pilot.getPilotFlights().add(flightAssigned);
                flightAssigned.getPilots().add(pilot);
                if ((flightAssigned.getPilots().size() >= 2 && flightAssigned.getRoute().getDistance() < 5000) || (flightAssigned.getPilots().size() >= 3 && flightAssigned.getRoute().getDistance() > 5000)) {
                    flightsAvai.remove(flightAssigned);
                }

                earliestDep = flightAssigned.getArrivalDate(); // later can change to calculate 
                currentLoc = flightAssigned.getRoute().getDestinationAirport();
                //                      System.err.println("7.2.1");

                long diffInHours = TimeUnit.MILLISECONDS.toHours(flightAssigned.getArrivalDate().getTime() - mtAcc.getTime());
                System.err.println("findNextFlight is tru and diffHours = " + diffInHours);
                if (diffInHours >= 96 && currentLoc.getAirportCode().equals(pilot.getBase().getAirportCode())) {
                    //               System.err.println("flightAssigned FLIGHT hours"+flightAssigned.getRoute().getFlightHours());

                    cal.setTime(earliestDep);
                    cal.add(Calendar.HOUR, 72);
                    //              System.err.println("maintenance hours requireed:"+aircraft.getAircraftType().getMaintenanceHoursRequiredACheck()+" sishewuru"+(int) (aircraft.getAircraftType().getMaintenanceHoursRequiredACheck() + 0.5d));
                    earliestDep = cal.getTime();

                    currentLoc = pilot.getBase();
                    mtAcc = earliestDep;
                    System.err.println("mtAcc" + mtAcc);
                }
            }
            //            System.err.println("8");

        }

        return flightsAvai;
        //route with higher demand operate with larger aircraft
        //longer distance larger aircraft
        //maintenance time 
        //remember to destroy the flights list after one year planning
        //B Check
        //before executing the function need to have maintenance hours required
    }

//    @Override
//    public Date calculateMaintenanceHours(PilotEntity pilot, Date mtAcc
//    ) {
//        //       System.err.println("date mtacc"+ mtAcc);
//        Double flyingHours = 0.0;
//        if (pilot.getPilotFlights().isEmpty()) {
//            return mtAcc;
//        }
//
//        List<FlightEntity> fL = pilot.getPilotFlights();
//        Date earliestT = fL.get(0).getDepartureDate();
//        for (FlightEntity f : fL) {
//            if (f.getDepartureDate().compareTo(mtAcc) >= 0 && f.getDepartureDate().compareTo(earliestT) < 0) {
//                earliestT = f.getDepartureDate();
//            }
//        }
//        System.err.println("Caluate Hours mtAcc" + mtAcc);
//        System.err.println("calcuate hours earliestT" + earliestT);
//        return earliestT;
//    }
    @Override
    public List<PilotEntity> retriveAllPilots() {
        Query q = em.createQuery("SELECT a FROM PilotEntity a");

        return (List<PilotEntity>) q.getResultList();
    }

    private int counter = 0;

    @Override
    public List<FlightEntity> CabinCrewSchedulingCapacityCheck(List<FlightEntity> flights, List<CabinCrewEntity> cabinCrews) {
        System.err.println("enter cabinCrewSchduling");
        flights = fleetAssignmentCheck(flights, aircraftSession.getAircrafts());

        System.err.println("enter cabin Scheduling");

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
        counter = 0;

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

            for (FlightEntity f : flightsAvai) {
                Date depTemp = f.getDepartureDate();
                if (depTemp.compareTo(earliestDep) < 0 && cabinCrew.getBase().getAirportCode().equals(f.getRoute().getOriginAirport().getAirportCode())) {
                    //Find the earliest flight which departs at the aircraft's hub
                    earliestFlight = f;
                    earliestDep = f.getDepartureDate();
                    hasHubOrNot = true;

                }

            }
            System.err.println("1st assign job");
        } else {
            earliestDep = cabinCrew.getCabinCrewFlights().get(0).getArrivalDate();
            Date latestDate = cabinCrew.getCabinCrewFlights().get(0).getArrivalDate();
            FlightEntity lastFlight = cabinCrew.getCabinCrewFlights().get(0);
            for (FlightEntity f : cabinCrew.getCabinCrewFlights()) {
                if (f.getArrivalDate().compareTo(earliestDep) > 0) {
                    earliestDep = f.getArrivalDate();
                    latestDate = f.getArrivalDate();
                    earliestFlight = f;
                    lastFlight = f;
                   
                }
            }

           
            for (FlightEntity f : flightsAvai) {
                Date depTemp = f.getDepartureDate();
                if (depTemp.compareTo(latestDate) > 0 && lastFlight.getRoute().getDestinationAirport().getAirportCode().equals(f.getRoute().getOriginAirport().getAirportCode())) {
                    //Find the earliest flight which departs at the aircraft's hub
                    earliestFlight = f;
                    earliestDep = f.getDepartureDate();
                    hasHubOrNot = true;

                }

            }
            

            if (hasHubOrNot) {
                for (FlightEntity f : flightsAvai) {
                    if (f.getDepartureDate().compareTo(latestDate) > 0 && f.getDepartureDate().compareTo(earliestDep) < 0 && f.getDepartureDate().compareTo(earliestDep) < 0 && lastFlight.getRoute().getDestinationAirport().getAirportCode().equals(f.getRoute().getOriginAirport().getAirportCode())) {
                        //Find the earliest flight which departs at the aircraft's hub
                        earliestFlight = f;
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
                        flightAssigned = f;
                        findNextFlight = true;
                    }
                } else if (f.getRoute().getDestinationAirport().getAirportCode().equals(cabinCrew.getBase().getAirportCode()) && f.getRoute().getOriginAirport().getAirportCode().equals(currentLoc.getAirportCode()) && f.getDepartureDate().compareTo(earliestDep) > 0) {
                    findSoonest = f.getDepartureDate();
                    flightAssigned = f;
                    findNextFlight = true;
                }

            }
            System.err.println("find a suitable flight cabin");
            //find a suitable flight
            if (findSoonest == null) {
                findNextFlight = false;
            } else {

                for (FlightEntity f : flightsAvai) {

                    if (currentLoc.equals(cabinCrew.getBase())) {
                        if (f.getRoute().getOriginAirport().getAirportCode().equals(currentLoc.getAirportCode()) && f.getDepartureDate().compareTo(earliestDep) > 0 && f.getDepartureDate().compareTo(findSoonest) < 0) {
                            flightAssigned = f;
                            findSoonest = f.getDepartureDate();
                            findNextFlight = true;
                        }
                    } else if (f.getRoute().getDestinationAirport().getAirportCode().equals(cabinCrew.getBase().getAirportCode()) && f.getRoute().getOriginAirport().getAirportCode().equals(currentLoc.getAirportCode()) && f.getDepartureDate().compareTo(earliestDep) > 0 && f.getDepartureDate().compareTo(findSoonest) < 0) {
                        flightAssigned = f;
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

                    currentLoc = cabinCrew.getBase();
                    mtAcc = earliestDep;
                    System.err.println("cabin mtAcc = " + mtAcc);
//                       System.err.println("7.2");

                }
                //            System.err.println("8");

            }
        }

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

//        Integer sum = 0;
//        
//        System.err.println("getFlightCapacity before query");
//        Query q1 = em.createQuery("SELECT s FROM SeatEntity s WHERE s.aircraft = :aircraft AND s.seatClass = :seatClass");
//        q1.setParameter("aircraft", a);
//        q1.setParameter("seatClass", "First Class");
//        Integer n1 = q1.getResultList().size();
//                System.err.println("getFlightCapacity after query");
//
//        int a1 = n1 / 20;
//        if (n1 % 20 > 0) {
//            a1++;
//        }
//        sum = sum + a1;
//
//        Query q2 = em.createQuery("SELECT s FROM SeatEntity s WHERE s.aircraft = :aircraft AND s.seatClass = :seatClass");
//        q2.setParameter("aircraft", a);
//        q2.setParameter("seatClass", "Premium Economy Class");
//        Integer n2 = q2.getResultList().size();
//        int a2 = n2 / 35;
//        if (n2 % 35 > 0) {
//            a2++;
//        }
//        sum = sum + a2;
//
//        Query q3 = em.createQuery("SELECT s FROM SeatEntity s WHERE s.aircraft = :aircraft AND s.seatClass = :seatClass");
//        q3.setParameter("aircraft", a);
//        q3.setParameter("seatClass", "Economy Class");
//        Integer n3 = q3.getResultList().size();
//        int a3 = n3 / 50;
//        if (n3 % 50 > 0) {
//            a3++;
//        }
//        sum = sum + a3;
//
//        Query q4 = em.createQuery("SELECT s FROM SeatEntity s WHERE s.aircraft = :aircraft AND s.seatClass = :seatClass");
//        q4.setParameter("aircraft", a);
//        q4.setParameter("seatClass", "Business Class");
//        Integer n4 = q4.getResultList().size();
//        int a4 = n4 / 30;
//        if (n4 % 30 > 0) {
//            a4++;
//        }
//        sum = sum + a4;
    }

    @Override
    public List<CabinCrewEntity> retrieveAllCabinCrew() {
        Query q = em.createQuery("SELECT a FROM CabinCrewEntity a");
        return (List<CabinCrewEntity>) q.getResultList();

    }

    
    @Override
    public List<FlightEntity> fleetAssignmentCheck(List<FlightEntity> flights, List<AircraftEntity> aircrafts) {
        HashMap<String, List<FlightEntity>> flights2 = new HashMap<>();
        if(flights == null){
            System.err.println("flights is NULL");
        }
        flights2.put("Unassigned", flights);
        flights2.put("Assigned", new ArrayList<FlightEntity>());
        System.err.println("enter fleet check");
        
        int i = 0;
        
        
        for (AircraftEntity a : aircrafts) {
            
            i++;
            
            if(flights2 == null)
            {
                System.err.println(i + ": flights2 is NULL");
            }
            else
            {
                System.err.println(i + ": flights2 is NOT NULL");
                
                if(flights2.get("Unassigned") != null)
                {
                    System.err.println(i + ": flights2.get(Unassigned) is NOT NULL");
                }
                else
                {
                    System.err.println(i + ": flights2.get(Unassigned) is NULL");
                }
            }
            
            if (!flights2.get("Unassigned").isEmpty()) {
                flights2 = oneAircraftAssignment(a, flights2);
                //               System.err.println("finsh one fleetassignment");
            }
        }
        //       System.err.println("finish fleetA"+flights.size());
        return (List<FlightEntity>) flights2.get("Assigned");
    }

    // Add business logic below. (Right-click in editor and choose
    // "Insert Code > Add Business Method")
    @Override
    public HashMap oneAircraftAssignment(AircraftEntity aircraft, HashMap<String, List<FlightEntity>> flights2) {
        System.err.println("enter one aircraftAssignment");
        List<FlightEntity> flightsAll = (List<FlightEntity>) flights2.get("Unassigned");
        System.err.println("dadadad");
        List<FlightEntity> flightsAssigned = (List<FlightEntity>) flights2.get("Assigned");
        System.err.println("lalalal");
        List<FlightEntity> flightsAvai = new ArrayList<>();
        //        System.err.println("enter one aircraftAssignment");
        System.err.println("flight Size");
        System.err.println("dadada");
        
        if(flightsAll != null)
        {
            System.err.println("flightsAll is NOT NULL: " + flightsAll.size());
        }
        else
        {
            System.err.println("flightsAll is NULL");
            flightsAll = new ArrayList<>();
        }
        
        for (FlightEntity f : flightsAll) {
            System.err.println("enter f");
            if (aircraft.getAircraftType().getAircraftRange() > f.getRoute().getDistance() && f.getAircraft() == null) {
                System.err.println("evry if");
                flightsAvai.add(f);
            }

        }
        System.err.println("out of loop");
        System.err.println("qq" + flightsAll.size());
        //find the available flights based on aircraf capability  
        if (flightsAvai.isEmpty()) {
            return flights2;
        }
        System.err.println("1");
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
                    earliestFlight = f;
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
                    latestDate = f.getArrivalDate();
                    lastFlight = f;
                    earliestFlight = f;
                    
                }
            }

            for (FlightEntity f : flightsAvai) {
                Date depTemp = f.getDepartureDate();
                if (depTemp.compareTo(latestDate) > 0 && lastFlight.getRoute().getDestinationAirport().getAirportCode().equals(f.getRoute().getOriginAirport().getAirportCode())) {
                    //Find the earliest flight which departs at the aircraft's hub
                    earliestFlight = f;
                    earliestDep = f.getDepartureDate();
                    hasHubOrNot = true;

                }

            }

            if (hasHubOrNot) {
                for (FlightEntity f : flightsAvai) {
                    if (f.getDepartureDate().compareTo(latestDate) > 0 && f.getDepartureDate().compareTo(earliestDep) < 0 && f.getDepartureDate().compareTo(earliestDep) < 0 && lastFlight.getRoute().getDestinationAirport().getAirportCode().equals(f.getRoute().getOriginAirport().getAirportCode())) {
                        //Find the earliest flight which departs at the aircraft's hub
                        earliestFlight = f;
                        earliestDep = f.getDepartureDate();
                        hasHubOrNot = true;

                    }

                }
            }
            System.err.println("2nd assigned");
        }

        System.err.println("3 + earliest flight" + earliestFlight.getDepartureDate());
        if (!hasHubOrNot) {
            System.err.println("all flights do not departure at the aircraft's hub");
            return flights2;

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
        flightsAssigned.add(earliestFlight);
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
                            flightAssigned = f;
                            findSoonest = f.getDepartureDate();
                            findHub = true;
                             findNextFlight = true;
                        }
                    } else if (f.getRoute().getDestinationAirport().getAirportCode().equals(aircraft.getAirportHub().getAirportCode()) && f.getRoute().getOriginAirport().getAirportCode().equals(currentLoc.getAirportCode()) && f.getDepartureDate().compareTo(earliestDep) > 0 ) {
                        flightAssigned = f;
                        findSoonest = f.getDepartureDate();
                        findHub = true;
                         findNextFlight = true;
                    }

                }
                    if(!findHub)
                    {
                         for (FlightEntity f : flightsAvai) {

                    
                        if (f.getRoute().getOriginAirport().getAirportCode().equals(currentLoc.getAirportCode()) && f.getDepartureDate().compareTo(earliestDep) > 0 ) {
                            flightAssigned = f;
                            findSoonest = f.getDepartureDate();
                          
                             findNextFlight = true;
                        }
                        }
                        
                    }                
                }else{
                    
                         for (FlightEntity f : flightsAvai) {

                    
                        if (f.getRoute().getOriginAirport().getAirportCode().equals(currentLoc.getAirportCode()) && f.getDepartureDate().compareTo(earliestDep) >0) {
                            flightAssigned = f;
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
                            flightAssigned = f;
                            findSoonest = f.getDepartureDate();
                            
                        }
                    } else if (f.getRoute().getDestinationAirport().getAirportCode().equals(aircraft.getAirportHub().getAirportCode()) && f.getRoute().getOriginAirport().getAirportCode().equals(currentLoc.getAirportCode()) && f.getDepartureDate().compareTo(earliestDep) > 0 && f.getDepartureDate().compareTo(findSoonest) < 0) {
                        flightAssigned = f;
                        findSoonest = f.getDepartureDate();
                        
                    }

                }
                   }else
                   {
                    
                         for (FlightEntity f : flightsAvai) {

                    
                        if (f.getRoute().getOriginAirport().getAirportCode().equals(currentLoc.getAirportCode()) && f.getDepartureDate().compareTo(earliestDep) > 0 && f.getDepartureDate().compareTo(findSoonest) < 0) {
                            flightAssigned = f;
                            findSoonest = f.getDepartureDate();
                           
                        }
                        }
                        
                    }                
                }else{
                    
                         for (FlightEntity f : flightsAvai) {

                    
                        if (f.getRoute().getOriginAirport().getAirportCode().equals(currentLoc.getAirportCode()) && f.getDepartureDate().compareTo(earliestDep) > 0 && f.getDepartureDate().compareTo(findSoonest) < 0) {
                            flightAssigned = f;
                            findSoonest = f.getDepartureDate();
                           
                        }
                        }
                        
                }
            }
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

                    currentLoc = aircraft.getAirportHub();
                    mtAcc = earliestDep;
//                       System.err.println("7.2");

                } else {
                    aircraft.getFlights().add(flightAssigned);
                    flightAssigned.setAircraft(aircraft);
                    flightsAvai.remove(flightAssigned);
                    flightsAssigned.add(flightAssigned);
                    flightsAll.remove(flightAssigned);
                    earliestDep = flightAssigned.getArrivalDate(); // later can change to calculate 
                    currentLoc = flightAssigned.getRoute().getDestinationAirport();
                    //                      System.err.println("7.2.1");

                }
            }
            //            System.err.println("8");

        }
        
        flights2.put("Unassigned", flightsAll);
        flights2.put("Assigned", flightsAssigned);
        return flights2;
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

}
