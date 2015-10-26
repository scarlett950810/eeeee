/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package imas.operation.sessionbean;

import imas.common.entity.CabinCrewEntity;
import imas.common.entity.PilotEntity;
import imas.planning.entity.AircraftEntity;
import imas.planning.entity.AircraftTypeEntity;
import imas.planning.entity.AirportEntity;
import imas.planning.entity.FlightEntity;
import imas.planning.entity.RouteEntity;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

/**
 *
 * @author wutong
 */
@Stateless
public class CrewToFlightSessionBean implements CrewToFlightSessionBeanLocal {

    @PersistenceContext
    private EntityManager em;

    @Override
    public List<PilotEntity> retreiveAvailablePilot(AircraftEntity aircraft, RouteEntity route, Date departureDate, Date arrivalDate) {
        Double dist = route.getDistance();
        String aircraftType = aircraft.getAircraftType().getIATACode();
        AirportEntity airport = route.getOriginAirport();
        List<PilotEntity> pilots = retrieveAllPilots();
        List<PilotEntity> pilotList = new ArrayList<>();
        for (PilotEntity p : pilots) {
            if (dist > 5000 && !p.getMileageLimit()) {
                System.err.println("first continue");
                continue;
            }
            if (!p.getBase().equals(airport)) {
                System.err.println("second continue");
                continue;
            }
            List<String> types = p.getAircraftTypeCapabilities();
            List<FlightEntity> flights = p.getPilotFlights();
            Boolean type = false;
            for (String t : types) {
                if (aircraftType.equals(t)) {
                    type = true;
                    System.err.println("find the type");
                    break;
                }
            }
            Boolean conflict = false;
            if (type) {
                for (FlightEntity f : flights) {
                    Date tempDeparture = f.getDepartureDate();
                    Date tempArrival = f.getArrivalDate();
                    if ((tempDeparture.after(departureDate) && tempDeparture.before(arrivalDate)) || (tempArrival.after(departureDate) && tempArrival.before(arrivalDate)) || (tempDeparture.before(departureDate) && tempArrival.after(arrivalDate))) {
                        System.err.println("");
                        conflict = true;
                        break;
                    }

                }
            }
            System.err.println(conflict);
            if (!conflict) {

                pilotList.add(p);
            }
        }
        return pilotList;
    }

    // Add business logic below. (Right-click in editor and choose
    // "Insert Code > Add Business Method")
    @Override
    public List<PilotEntity> retrieveAllPilots() {
        Query query = em.createQuery("SELECT a FROM PilotEntity a");
        List<PilotEntity> pilots = (List<PilotEntity>) query.getResultList();
        return pilots;
    }

    @Override
    public List<CabinCrewEntity> retrieveAllCabinCrew() {
        Query query = em.createQuery("SELECT a FROM CabinCrewEntity a");
        List<CabinCrewEntity> cabinCrewList = (List<CabinCrewEntity>) query.getResultList();
        return cabinCrewList;
    }

    @Override
    public List<CabinCrewEntity> retrieveAvailableCabinCrew(RouteEntity route, Date departureDate, Date arrivalDate) {
        AirportEntity airport = route.getOriginAirport();
        List<CabinCrewEntity> cabinCrewList = retrieveAllCabinCrew();
        System.err.println("Call retrieve all cabin crew");
        List<CabinCrewEntity> selectedCabinCrewList = new ArrayList<>();
        for (CabinCrewEntity c : cabinCrewList) {
            System.err.println("Print airport code"+airport.getAirportCode());
            System.err.println("Print airport base"+c.getBase().getAirportCode());
            if (c.getBase().equals(airport)) {
                System.err.println("Step 1");
                continue;
            }
            List<FlightEntity> flights = c.getCabinCrewFlights();
            Boolean conflict = false;
            System.err.println("Step 2");
            for (FlightEntity f : flights) {
                System.err.println("Step 3");
                Date tempDeparture = f.getDepartureDate();
                Date tempArrival = f.getArrivalDate();
                if ((tempDeparture.after(departureDate) && tempDeparture.before(arrivalDate)) || (tempArrival.after(departureDate) && tempArrival.before(arrivalDate)) || (tempDeparture.before(departureDate) && tempArrival.after(arrivalDate))) {
                    conflict = true;
                    break;
                }
                System.err.println("Step 4");
            }
            System.err.println("Step 5");
            if (!conflict) {
                selectedCabinCrewList.add(c);
            }
        }
        System.err.println("Finish get crew list");
        return selectedCabinCrewList;
    }

    @Override
    public void createFlight(FlightEntity flight, FlightEntity returnFlight) {
        em.persist(flight);
        em.persist(returnFlight);
        flight.setReverseFlight(returnFlight);
        returnFlight.setReverseFlight(flight);

    }

    @Override
    public List<FlightEntity> retrieveAllFlights() {
        Query query = em.createQuery("SELECT a FROM FlightEntity a");
        List<FlightEntity> flightList = (List<FlightEntity>) query.getResultList();
        return flightList;
    }

    @Override
    public void deleteFlight(FlightEntity flight) {
        Long Id = flight.getId();
        Query query = em.createQuery("SELECT a FROM FlightEntity a WHERE a.id = :ID");
        query.setParameter("ID", Id);
        FlightEntity f = (FlightEntity) query.getSingleResult();
        FlightEntity returnF = new FlightEntity();
        System.err.println("Enter delete flight" + f.getFlightNo());
        returnF = f.getReverseFlight();
        Long returnId = returnF.getId();
        Query query1 = em.createQuery("SELECT a FROM FlightEntity a WHERE a.id = :ID");
        query1.setParameter("ID", returnId);
        FlightEntity returnFlight = (FlightEntity) query1.getSingleResult();
        f.setReverseFlight(null);
        returnF.setReverseFlight(null);
        

//        f.setCabinCrews(null);
//        f.setPilots(null);
//        f.setRoute(null);
//        f.setAircraftFlight(null);
        System.err.println("prepare to reomve");
        em.remove(f);
        em.remove(returnFlight);
        System.err.println("remove successfully");
    }
}
