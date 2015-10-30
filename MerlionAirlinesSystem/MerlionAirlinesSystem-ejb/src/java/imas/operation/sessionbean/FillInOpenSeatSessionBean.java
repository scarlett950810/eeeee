/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package imas.operation.sessionbean;

import imas.common.entity.CabinCrewEntity;
import imas.common.entity.PilotEntity;
import imas.planning.entity.FlightEntity;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

/**
 *
 * @author Lei
 */
@Stateless
public class FillInOpenSeatSessionBean implements FillInOpenSeatSessionBeanLocal {

    @PersistenceContext
    private EntityManager em;

    private String name;
    private Map<String, String> map;
    private List<PilotEntity> allPilots;
    private List<CabinCrewEntity> allCrews;

    @Override
    public List<FlightEntity> fetchComingFlights(String base) {
        Query query = em.createQuery("SELECT f FROM FlightEntity f WHERE f.route.originAirport.airportCode = :base");
        query.setParameter("base", base);

        List<FlightEntity> flights = (List<FlightEntity>) query.getResultList();
        List<FlightEntity> comingFlights = new ArrayList<FlightEntity>();

        Calendar cal = Calendar.getInstance();
        Date currentDate = new Date();
        currentDate = new Date(currentDate.getTime() - (1000 * 60 * 60));
        cal.add(Calendar.YEAR, 3);
//        cal.add(Calendar.HOUR, 24);
        Date limitDate = cal.getTime();
        if (flights.isEmpty()) {
            return null;
        } else {
            for (int i = 0; i < flights.size(); i++) {
                if (flights.get(i).getDepartureDate().after(currentDate) && flights.get(i).getDepartureDate().before(limitDate)
                        && (flights.get(i).getActualDepartureDate() == null || flights.get(i).getActualArrivalDate() == null)) {
                    comingFlights.add(flights.get(i));
                }
            }
            Collections.sort(comingFlights);
            return comingFlights;
        }
    }

    @Override
    public Map<String, String> getMissingCabinCrew(FlightEntity selectedFlight) {
        map = new HashMap<String, String>();
        allCrews = selectedFlight.getCabinCrews();
        for (int i = 0; i < allCrews.size(); i++) {
            if (Objects.equals(allCrews.get(i).getWorking(), Boolean.FALSE) && allCrews.get(i).getWorkingStatus().equals("available")) {
                name = allCrews.get(i).getDisplayName() + "-" + allCrews.get(i).getStaffNo();
                map.put(name, name);

            }
        }
        System.out.print(map);
        return map;
    }

    @Override
    public Map<String, String> getMissingPilot(FlightEntity selectedFlight) {
        map = new HashMap<String, String>();
        allPilots = selectedFlight.getPilots();

        for (int i = 0; i < allPilots.size(); i++) {
            System.out.println(allPilots.get(i).getWorking());
            if (Objects.equals(allPilots.get(i).getWorking(), Boolean.FALSE) && allPilots.get(i).getWorkingStatus().equals("available")) {

                name = allPilots.get(i).getDisplayName() + "-" + allPilots.get(i).getStaffNo();
                map.put(name, name);
            }
            allPilots.get(i).setWorking(Boolean.FALSE);
        }

        return map;
    }

    public Date getReturnDate(FlightEntity selectedFlight) {
        Date start = selectedFlight.getDepartureDate();
//        System.out.print(start);
        Date end = selectedFlight.getReverseFlight().getArrivalDate();
//        System.out.print(end);
        if (end.after(start)) {
            return end;
        } else {
            Date temp;
            Query query = em.createQuery("SELECT f FROM FlightEntity f WHERE f.route.originAirport=:origin AND f.route.destinationAirport=:destination ");
            query.setParameter("origin", selectedFlight.getReverseFlight().getRoute().getOriginAirport());
            query.setParameter("destination", selectedFlight.getReverseFlight().getRoute().getDestinationAirport());
            List<FlightEntity> flights = (List<FlightEntity>) query.getResultList();
            if (!flights.isEmpty()) {
                Collections.sort(flights);
                for (int i = 0; i < flights.size(); i++) {
                    if (flights.get(i).equals(selectedFlight.getReverseFlight())) {
                        end = flights.get(i + 1).getArrivalDate();
                        System.out.print(end);
                        break;
                    }
                }
            }
            return end;
        }

    }

    @Override
    public PilotEntity getPilot(String name) {

        String[] parts = name.split("-");
        String staffNo = parts[1];
        Query query;
        query = em.createQuery("SELECT f FROM PilotEntity f WHERE f.staffNo =:staffNo");
        query.setParameter("staffNo", staffNo);
        List<PilotEntity> list = query.getResultList();

        if (!list.isEmpty()) {
//            System.out.print(list.get(0));
            return list.get(0);
        }
        return null;

    }

    @Override
    public CabinCrewEntity getCrew(String name) {

        String[] parts = name.split("-");
        String staffNo = parts[1];
        Query query;
        query = em.createQuery("SELECT f FROM CabinCrewEntity f WHERE f.staffNo =:staffNo");
        query.setParameter("staffNo", staffNo);
        List<CabinCrewEntity> list = query.getResultList();

        if (!list.isEmpty()) {
//            System.out.print(list.get(0));
            return list.get(0);
        }
        return null;

    }

    @Override
    public void replacePilot(PilotEntity oldPilot, PilotEntity newPilot, FlightEntity selectedFlight) {
        List<PilotEntity> pilots = selectedFlight.getPilots();
        System.out.print(oldPilot);
        System.out.print(newPilot);
        System.out.print(selectedFlight);
        System.out.print(pilots);
        if (!pilots.isEmpty()) {
            for (int i = 0; i < pilots.size(); i++) {
                if (pilots.get(i).equals(oldPilot)) {
                    pilots.set(i, newPilot);
                    break;
                }
            }
        }
        selectedFlight.setPilots(pilots);
        em.merge(selectedFlight);
        oldPilot.getPilotFlights().remove(selectedFlight);
        newPilot.getPilotFlights().add(selectedFlight);
        em.merge(oldPilot);
        em.merge(newPilot);

    }

    @Override
    public void replaceCrew(CabinCrewEntity oldCrew, CabinCrewEntity newCrew, FlightEntity selectedFlight) {
        List<CabinCrewEntity> crews = selectedFlight.getCabinCrews();

        if (!crews.isEmpty()) {
            for (int i = 0; i < crews.size(); i++) {
                if (crews.get(i).equals(oldCrew)) {
                    crews.set(i, newCrew);
                    break;
                }
            }
        }
        selectedFlight.setCabinCrews(crews);
        em.merge(selectedFlight);
        oldCrew.getCabinCrewFlights().remove(selectedFlight);
        newCrew.getCabinCrewFlights().add(selectedFlight);
        em.merge(oldCrew);
        em.merge(newCrew);

    }

    @Override
    public List<PilotEntity> doPilotFillIn(PilotEntity selectedPilot, FlightEntity selectedFlight) {
        // same base
        System.out.print(selectedPilot.getBase());

//        System.out.print("dsffjlaaljsfasfds");
        Query query = em.createQuery("SELECT p FROM PilotEntity p WHERE p.base = :base");
        query.setParameter("base", selectedPilot.getBase());
        List<PilotEntity> pilots = (List<PilotEntity>) query.getResultList();
        List<PilotEntity> availablePilots = new ArrayList<PilotEntity>();
        List<FlightEntity> flights;

        Date startDate;
        Date endDate;
        Boolean result = Boolean.TRUE;

        System.out.print("lalalalalalalala");
        startDate = selectedFlight.getDepartureDate();
        System.out.print(startDate);
        endDate = getReturnDate(selectedFlight);
        System.out.print(endDate);
        //aircraft type,working,  current working status
        if (!pilots.isEmpty()) {
            System.out.print("hahahahahahahah");
            System.out.print(pilots.size());

            pilots.remove(selectedPilot);

            for (int i = 0; i < pilots.size(); i++) {
//                System.out.print(pilots.get(i).getAircraftTypeCapabilities());
//                System.out.print(selectedFlight.getAircraft().getAircraftType().getIATACode());
//                System.out.print(pilots.get(i).getWorking());
//                System.out.print(pilots.get(i).getWorkingStatus());

                pilots.get(i).setWorking(Boolean.TRUE);

                if (pilots.get(i).getAircraftTypeCapabilities().contains(selectedFlight.getAircraft().getAircraftType().getIATACode())
                        && pilots.get(i).getWorking() && pilots.get(i).getWorkingStatus().equals("available")) {

                    flights = pilots.get(i).getPilotFlights();
                    if (flights.isEmpty()) {
                        availablePilots.add(pilots.get(i));
                    } else {
                        for (int j = 0; j < flights.size(); j++) {
                            System.out.print("yi ge yi ge");
                            System.out.print(flights.get(j).getDepartureDate());
                            System.out.print(flights.get(j).getArrivalDate());

                            if (flights.get(j).getDepartureDate().before(endDate) && flights.get(j).getArrivalDate().after(startDate)) {
                                System.out.print("no no no");
                                result = Boolean.FALSE;
                                break;
                            }
                        }
                        if (result) {
                            availablePilots.add(pilots.get(i));
                        }
                    }
                }
            }
        }
        System.out.print(availablePilots);
        return availablePilots;
    }

    @Override
    public List<CabinCrewEntity> doCrewFillIn(CabinCrewEntity selectedCrew, FlightEntity selectedFlight) {
        System.out.print(selectedCrew.getBase());

//        System.out.print("dsffjlaaljsfasfds");
        Query query = em.createQuery("SELECT p FROM CabinCrewEntity p WHERE p.base = :base");
        query.setParameter("base", selectedCrew.getBase());
        List<CabinCrewEntity> crews = (List<CabinCrewEntity>) query.getResultList();
        List<CabinCrewEntity> availableCrews = new ArrayList<CabinCrewEntity>();
        List<FlightEntity> flights;

        Date startDate;
        Date endDate;
        Boolean result = Boolean.TRUE;

        System.out.print("lalalalalalalala");
        startDate = selectedFlight.getDepartureDate();
        System.out.print(startDate);
        endDate = getReturnDate(selectedFlight);
        System.out.print(endDate);
        //aircraft type,working,  current working status
        if (!crews.isEmpty()) {
            System.out.print("hahahahahahahah");
            System.out.print(crews.size());

            crews.remove(selectedCrew);

            for (int i = 0; i < crews.size(); i++) {
                if (i == 11 || i == 12 || i == 13 || i == 14 || i == 21 || i == 22 || i == 23 || i == 28) {
                    crews.get(i).setWorking(Boolean.TRUE);
                }

                if (crews.get(i).getWorking() && crews.get(i).getWorkingStatus().equals("available")) {

                    flights = crews.get(i).getCabinCrewFlights();
                    if (flights.isEmpty()) {
                        availableCrews.add(crews.get(i));
                    } else {
                        for (int j = 0; j < flights.size(); j++) {
                            System.out.print("yi ge yi ge");
                            System.out.print(flights.get(j).getDepartureDate());
                            System.out.print(flights.get(j).getArrivalDate());

                            if (flights.get(j).getDepartureDate().before(endDate) && flights.get(j).getArrivalDate().after(startDate)) {
                                System.out.print("no no no");
                                result = Boolean.FALSE;
                                break;
                            }
                        }
                        if (result) {
                            availableCrews.add(crews.get(i));
                        }
                    }
                }
            }
        }
        System.out.print(availableCrews);
        return availableCrews;
    }

}
