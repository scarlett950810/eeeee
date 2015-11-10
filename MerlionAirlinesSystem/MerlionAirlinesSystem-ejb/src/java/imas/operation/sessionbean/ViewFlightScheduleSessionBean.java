/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package imas.operation.sessionbean;

import imas.planning.entity.AircraftEntity;
import imas.planning.entity.FlightEntity;
import imas.planning.entity.MaintenanceScheduleEntity;
import java.util.ArrayList;
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
public class ViewFlightScheduleSessionBean implements ViewFlightScheduleSessionBeanLocal {

    @PersistenceContext
    private EntityManager em;
    private Map<String, String> aircrafts;
    List<AircraftEntity> aircraftList;
    private ScheduleModel lazyEventModel;
    @Temporal(javax.persistence.TemporalType.TIMESTAMP)
    private Date startDate;
    @Temporal(javax.persistence.TemporalType.TIMESTAMP)
    private Date endDate;

    private String flightNo;
    private ScheduleEvent event = new DefaultScheduleEvent();

    @Override
    public Map<String, String> getAircrafts() {
        Query query;
        query = em.createQuery("SELECT f FROM AircraftEntity f ");
        List<AircraftEntity> list = query.getResultList();
        aircrafts = new HashMap<String, String>();
        if (!list.isEmpty()) {
            List<Long> aircraft = new ArrayList<Long>();
            Long tailId;
            for (int i = 0; i < list.size(); i++) {
                tailId = list.get(i).getId();
                if (!aircraft.contains(tailId)) {
                    aircraft.add(tailId);
                    aircrafts.put(list.get(i).getTailId(), list.get(i).getTailId());
                }
            }
        }
        return aircrafts;
    }

    @Override
    public ScheduleModel createEvent(String aircraft) {
        Query query;
        query = em.createQuery("SELECT f FROM AircraftEntity f WHERE f.tailId=:aircraft");
        query.setParameter("aircraft", aircraft);
        AircraftEntity selectedAircraft;
        selectedAircraft = (AircraftEntity) query.getResultList().get(0);
        query = em.createQuery("SELECT f FROM FlightEntity f WHERE f.aircraft=:aircraft");
        query.setParameter("aircraft", selectedAircraft);
        List<FlightEntity> list = query.getResultList();
        lazyEventModel = new DefaultScheduleModel();
        if (!list.isEmpty()) {
            for (int i = 0; i < list.size(); i++) {
                flightNo = list.get(i).getFlightNo();
                startDate = list.get(i).getDepartureDate();
                endDate = list.get(i).getArrivalDate();
                if (flightNo != null && startDate != null && endDate != null) {
                    event = new DefaultScheduleEvent(flightNo + " " + list.get(i).getRoute().getOriginAirport().getAirportCode() + " to " + list.get(i).getRoute().getDestinationAirport().getAirportCode(), startDate, endDate);
                    lazyEventModel.addEvent(event);
                }

            }
            return lazyEventModel;
        } else {
            return null;
        }
    }

    @Override
    public ScheduleModel createAllEvent(String aircraft) {
        Query query;
        query = em.createQuery("SELECT f FROM AircraftEntity f WHERE f.tailId=:aircraft");
        query.setParameter("aircraft", aircraft);
        AircraftEntity selectedAircraft;
        selectedAircraft = (AircraftEntity) query.getResultList().get(0);
        query = em.createQuery("SELECT f FROM FlightEntity f WHERE f.aircraft=:aircraft");
        query.setParameter("aircraft", selectedAircraft);
        List<FlightEntity> flights = query.getResultList();
        lazyEventModel = new DefaultScheduleModel();
        if (!flights.isEmpty()) {
            for (int i = 0; i < flights.size(); i++) {
                flightNo = flights.get(i).getFlightNo();
                startDate = flights.get(i).getDepartureDate();
                endDate = flights.get(i).getArrivalDate();
                if (flightNo != null && startDate != null && endDate != null) {
                    event = new DefaultScheduleEvent(flightNo + " " + flights.get(i).getRoute().getOriginAirport().getAirportCode() + " to " + flights.get(i).getRoute().getDestinationAirport().getAirportCode(), startDate, endDate);
                    lazyEventModel.addEvent(event);
                }
            }
        }
        query = em.createQuery("SELECT f FROM MaintenanceScheduleEntity f WHERE f.aircraft=:aircraft");
        query.setParameter("aircraft", selectedAircraft);
        List<MaintenanceScheduleEntity> list = query.getResultList();
        if (!list.isEmpty()) {
            for (int i = 0; i < list.size(); i++) {
                String maintenanceName = list.get(i).getMaintenanceType();
                startDate = list.get(i).getStartingTime();
                endDate = list.get(i).getEndingTime();
                if (maintenanceName != null & startDate != null & endDate != null) {
                    event = new DefaultScheduleEvent(maintenanceName, startDate, endDate);
                    lazyEventModel.addEvent(event);
                }

            }
            return lazyEventModel;
        } else {
            return null;
        }

    }

    public Date getStartDate() {
        return startDate;
    }

    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }

    public Date getEndDate() {
        return endDate;
    }

    public void setEndDate(Date endDate) {
        this.endDate = endDate;
    }

    public String getFlightNo() {
        return flightNo;
    }

    public void setFlightNo(String flightNo) {
        this.flightNo = flightNo;
    }

    public ScheduleEvent getEvent() {
        return event;
    }

    public void setEvent(ScheduleEvent event) {
        this.event = event;
    }

    public ScheduleModel getLazyEventModel() {
        return lazyEventModel;
    }

    public void setLazyEventModel(ScheduleModel lazyEventModel) {
        this.lazyEventModel = lazyEventModel;
    }

}
