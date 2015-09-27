/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package imas.operation.sessionbean;

import imas.common.entity.CabinCrewEntity;
import imas.common.entity.PilotEntity;
import imas.planning.entity.FlightEntity;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.persistence.Temporal;
import org.primefaces.model.DefaultScheduleEvent;
import org.primefaces.model.DefaultScheduleModel;
import org.primefaces.model.ScheduleEvent;
import org.primefaces.model.ScheduleModel;

/**
 *
 * @author Lei
 */
@Stateless
public class RetrieveDutySessionBean implements RetrieveDutySessionBeanLocal {

    // Add business logic below. (Right-click in editor and choose
    // "Insert Code > Add Business Method")
    @PersistenceContext
    private EntityManager em;
    private String name;
    private String flightNo;
    private ScheduleModel lazyEventModel;
    private ScheduleEvent event = new DefaultScheduleEvent();
    @Temporal(javax.persistence.TemporalType.TIMESTAMP)
    private Date startDate;
    @Temporal(javax.persistence.TemporalType.TIMESTAMP)
    private Date endDate;

    @Override
    public Map<String, String> getPilot() {
        Map<String, String> map = new HashMap<String, String>();
        Query query;
        query = em.createQuery("SELECT f FROM PilotEntity f");     
        List<PilotEntity> list = query.getResultList();
        
        if (!list.isEmpty()) {
            for (int i = 0; i < list.size(); i++) {
                name = list.get(i).getDisplayName() + "-" + list.get(i).getStaffNo();
                map.put(name, name);
            }
        }
        return map;
    }

    @Override
    public Map<String, String> getCabinCrew() {
        Map<String, String> map = new HashMap<String, String>();
        Query query;
        query = em.createQuery("SELECT f FROM CabinCrewEntity f");
        List<CabinCrewEntity> list = query.getResultList();
        if (!list.isEmpty()) {
            for (int i = 0; i < list.size(); i++) {
                name = list.get(i).getDisplayName() + "-" + list.get(i).getStaffNo();
                map.put(name, name);
            }
        }
        return map;
    }

    @Override
    public ScheduleModel createPilotEvent(String name) {
        String[] parts = name.split("-");
        String staffNo = parts[1];
        Query query;
        query = em.createQuery("SELECT f FROM PilotEntity f WHERE f.staffNo =:staffNo");
        query.setParameter("staffNo", staffNo);
        List<PilotEntity> list = query.getResultList();
        lazyEventModel = new DefaultScheduleModel();
        if (!list.isEmpty()) {
            List<FlightEntity> flights = list.get(0).getPilotFlights();

            for (int i = 0; i < flights.size(); i++) {
                System.out.print("33333");
                flightNo = flights.get(i).getFlightNo();
                startDate = flights.get(i).getDepartureDate();
                endDate = flights.get(i).getArrivalDate();
                if (flightNo != null & startDate != null & endDate != null) {
                    event = new DefaultScheduleEvent(flightNo, startDate, endDate);
                    System.out.print(event);
                    lazyEventModel.addEvent(event);
                }
            }
            return lazyEventModel;
        } else {
            return null;
        }

    }

    @Override
    public ScheduleModel createCabinEvent(String name) {
        String[] parts = name.split("-");
        String staffNo = parts[1];
        Query query;
        query = em.createQuery("SELECT f FROM CabinCrewEntity f WHERE f.staffNo =:staffNo");
        query.setParameter("staffNo", staffNo);
        List<CabinCrewEntity> list = query.getResultList();
        lazyEventModel = new DefaultScheduleModel();
        if (!list.isEmpty()) {
            List<FlightEntity> flights = list.get(0).getCabinCrewFlights();

            for (int i = 0; i < flights.size(); i++) {
                System.out.print("33333");
                flightNo = flights.get(i).getFlightNo();
                startDate = flights.get(i).getDepartureDate();
                endDate = flights.get(i).getArrivalDate();
                if (flightNo != null & startDate != null & endDate != null) {
                    event = new DefaultScheduleEvent(flightNo, startDate, endDate);
                    System.out.print(event);
                    lazyEventModel.addEvent(event);
                }
            }
            return lazyEventModel;
        } else {
            return null;
        }
    }

}
