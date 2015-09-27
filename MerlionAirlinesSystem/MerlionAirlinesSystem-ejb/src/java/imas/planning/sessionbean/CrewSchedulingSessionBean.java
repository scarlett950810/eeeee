/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package imas.planning.sessionbean;

import imas.common.entity.PilotEntity;
import imas.planning.entity.AircraftEntity;
import imas.planning.entity.AirportEntity;
import imas.planning.entity.FlightEntity;
import imas.planning.entity.MaintenanceScheduleEntity;
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
 * @author ruicai
 */
@Stateless
public class CrewSchedulingSessionBean implements CrewSchedulingSessionBeanLocal {

    @PersistenceContext
    private EntityManager em;
    // Add business logic below. (Right-click in editor and choose
    // "Insert Code > Add Business Method")

    @Override
    public List<FlightEntity> pilotScheduling(List<FlightEntity> flights, List<PilotEntity> pilots) {
        System.err.println("enter pilotScheduling");
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
        List<PilotEntity> pilotsLongDis = new ArrayList<PilotEntity>();
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
            cal.add(Calendar.YEAR, 1);
            earliestDep = cal.getTime();

            for (FlightEntity f : flightsAvai) {
                Date depTemp = f.getDepartureDate();
                if (depTemp.compareTo(earliestDep) < 0 && pilot.getBase().getAirportCode().equals(f.getRoute().getOriginAirport().getAirportCode())) {
                    //Find the earliest flight which departs at the aircraft's hub
                    earliestFlight = em.find(FlightEntity.class, f.getId());
                    earliestDep = f.getDepartureDate();
                    hasHubOrNot = true;

                }

            }
            System.err.println("1st assign job");
        } else {
            earliestDep = pilot.getPilotFlights().get(0).getArrivalDate();
            Date latestDate = null;
            for (FlightEntity f : pilot.getPilotFlights()) {
                if (f.getArrivalDate().compareTo(earliestDep) > 0) {
                    earliestDep = f.getArrivalDate();
                    latestDate = f.getArrivalDate();
                }
            }

            for (FlightEntity f : flightsAvai) {
                Date depTemp = f.getDepartureDate();
                if (depTemp.compareTo(latestDate) > 0 && pilot.getBase().getAirportCode().equals(f.getRoute().getOriginAirport().getAirportCode())) {
                    //Find the earliest flight which departs at the aircraft's hub
                    earliestFlight = em.find(FlightEntity.class, f.getId());
                    earliestDep = f.getDepartureDate();
                    hasHubOrNot = true;

                }

            }

            if (hasHubOrNot) {
                for (FlightEntity f : flightsAvai) {
                    if (f.getDepartureDate().compareTo(latestDate) > 0 && f.getDepartureDate().compareTo(earliestDep) < 0 && f.getDepartureDate().compareTo(earliestDep) < 0 && pilot.getBase().getAirportCode().equals(f.getRoute().getOriginAirport().getAirportCode())) {
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
                        flightAssigned = em.find(FlightEntity.class, f.getId());
                    }
                } else if (f.getRoute().getDestinationAirport().getAirportCode().equals(pilot.getBase().getAirportCode()) && f.getRoute().getOriginAirport().getAirportCode().equals(currentLoc.getAirportCode()) && f.getDepartureDate().compareTo(earliestDep) > 0) {
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

                    if (currentLoc.equals(pilot.getBase())) {
                        if (f.getRoute().getOriginAirport().getAirportCode().equals(currentLoc.getAirportCode()) && f.getDepartureDate().compareTo(earliestDep) > 0 && f.getDepartureDate().compareTo(findSoonest) < 0) {
                            flightAssigned = em.find(FlightEntity.class, f.getId());
                            findSoonest = f.getDepartureDate();
                            findNextFlight = true;
                        }
                    } else if (f.getRoute().getDestinationAirport().getAirportCode().equals(pilot.getBase().getAirportCode()) && f.getRoute().getOriginAirport().getAirportCode().equals(currentLoc.getAirportCode()) && f.getDepartureDate().compareTo(earliestDep) > 0 && f.getDepartureDate().compareTo(findSoonest) < 0) {
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

                pilot.getPilotFlights().add(flightAssigned);
                flightAssigned.getPilots().add(pilot);
                if (flightAssigned.getPilots().size() >= 2) {
                    flightsAvai.remove(flightAssigned);
                }
                em.merge(pilot);

                earliestDep = flightAssigned.getArrivalDate(); // later can change to calculate 
                currentLoc = flightAssigned.getRoute().getDestinationAirport();
                //                      System.err.println("7.2.1");
                long diffInHours = TimeUnit.MILLISECONDS.toHours(flightAssigned.getDepartureDate().getTime() - mtAcc.getTime());
                System.err.println("findNextFlight is tru and diffHours = " + diffInHours);
                if (diffInHours > 96 && currentLoc.getAirportCode().equals(pilot.getBase().getAirportCode())) {
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

        em.merge(pilot);
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
            cal.add(Calendar.YEAR, 1);
            earliestDep = cal.getTime();
            System.err.println("before getBase");
            for (FlightEntity f : flightsAvai) {
                Date depTemp = f.getDepartureDate();
                if (depTemp.compareTo(earliestDep) < 0 && pilot.getBase().getAirportCode().equals(f.getRoute().getOriginAirport().getAirportCode())) {
                    //Find the earliest flight which departs at the aircraft's hub
                    earliestFlight = em.find(FlightEntity.class, f.getId());
                    earliestDep = f.getDepartureDate();
                    hasHubOrNot = true;

                }

            }
            System.err.println("after getBase");
        } else {
            System.err.println("already go some flights before getbase");
            earliestDep = pilot.getPilotFlights().get(0).getArrivalDate();
            Date latestDate = null;
            for (FlightEntity f : pilot.getPilotFlights()) {
                if (f.getArrivalDate().compareTo(earliestDep) > 0) {
                    earliestDep = f.getArrivalDate();
                    latestDate = f.getArrivalDate();
                }
            }
            System.err.println("test problem");
            for (FlightEntity f : flightsAvai) {
                Date depTemp = f.getDepartureDate();
                System.err.println("test1 problem" + pilot.getBase().getAirportCode());
                System.err.println("..." + f.getRoute().getOriginAirport().getAirportCode());
                if (depTemp.compareTo(latestDate) > 0 && pilot.getBase().getAirportCode().equals(f.getRoute().getOriginAirport().getAirportCode())) {
                    //Find the earliest flight which departs at the aircraft's hub
                    System.err.println("test2 problem");
                    earliestFlight = em.find(FlightEntity.class, f.getId());
                    earliestDep = f.getDepartureDate();
                    hasHubOrNot = true;

                }

            }
            System.err.println("already go some flights after getbase");

            if (hasHubOrNot) {
                for (FlightEntity f : flightsAvai) {
                    if (f.getDepartureDate().compareTo(latestDate) > 0 && f.getDepartureDate().compareTo(earliestDep) < 0 && f.getDepartureDate().compareTo(earliestDep) < 0 && pilot.getBase().getAirportCode().equals(f.getRoute().getOriginAirport().getAirportCode())) {
                        //Find the earliest flight which departs at the aircraft's hub
                        earliestFlight = em.find(FlightEntity.class, f.getId());
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
                        flightAssigned = em.find(FlightEntity.class, f.getId());
                    }
                } else if (f.getRoute().getDestinationAirport().getAirportCode().equals(pilot.getBase().getAirportCode()) && f.getRoute().getOriginAirport().getAirportCode().equals(currentLoc.getAirportCode()) && f.getDepartureDate().compareTo(earliestDep) > 0) {
                    findSoonest = f.getDepartureDate();
                    flightAssigned = em.find(FlightEntity.class, f.getId());
                }

            }
            //find a suitable flight
            if (findSoonest == null) {
                findNextFlight = false;
            } else {

                for (FlightEntity f : flightsAvai) {

                    if (currentLoc.equals(pilot.getBase())) {
                        if (f.getRoute().getOriginAirport().getAirportCode().equals(currentLoc.getAirportCode()) && f.getDepartureDate().compareTo(earliestDep) > 0 && f.getDepartureDate().compareTo(findSoonest) < 0) {
                            flightAssigned = em.find(FlightEntity.class, f.getId());
                            findSoonest = f.getDepartureDate();
                            findNextFlight = true;
                        }
                    } else if (f.getRoute().getDestinationAirport().getAirportCode().equals(pilot.getBase().getAirportCode()) && f.getRoute().getOriginAirport().getAirportCode().equals(currentLoc.getAirportCode()) && f.getDepartureDate().compareTo(earliestDep) > 0 && f.getDepartureDate().compareTo(findSoonest) < 0) {
                        flightAssigned = em.find(FlightEntity.class, f.getId());
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
                em.merge(pilot);

                earliestDep = flightAssigned.getArrivalDate(); // later can change to calculate 
                currentLoc = flightAssigned.getRoute().getDestinationAirport();
                //                      System.err.println("7.2.1");

                long diffInHours = TimeUnit.MILLISECONDS.toHours(flightAssigned.getDepartureDate().getTime() - mtAcc.getTime());
                System.err.println("findNextFlight is tru and diffHours = " + diffInHours);
                if (diffInHours > 96 && currentLoc.getAirportCode().equals(pilot.getBase().getAirportCode())) {
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

        em.merge(pilot);
        return flightsAvai;
        //route with higher demand operate with larger aircraft
        //longer distance larger aircraft
        //maintenance time 
        //remember to destroy the flights list after one year planning
        //B Check
        //before executing the function need to have maintenance hours required
    }

    @Override
    public Date calculateMaintenanceHours(PilotEntity pilot, Date mtAcc
    ) {
        //       System.err.println("date mtacc"+ mtAcc);
        Double flyingHours = 0.0;
        if (pilot.getPilotFlights().isEmpty()) {
            return mtAcc;
        }

        List<FlightEntity> fL = pilot.getPilotFlights();
        Date earliestT = fL.get(0).getDepartureDate();
        for (FlightEntity f : fL) {
            if (f.getDepartureDate().compareTo(mtAcc) >= 0 && f.getDepartureDate().compareTo(earliestT) < 0) {
                earliestT = f.getDepartureDate();
            }
        }
        System.err.println("Caluate Hours mtAcc" + mtAcc);
        System.err.println("calcuate hours earliestT" + earliestT);
        return earliestT;
    }

    @Override
    public List<PilotEntity> retriveAllPilots() {
        Query q = em.createQuery("SELECT a FROM PilotEntity a");

        return (List<PilotEntity>) q.getResultList();
    }

}
