/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package imas.operation.sessionbean;

import imas.planning.entity.AircraftEntity;
import imas.planning.entity.AircraftGroupEntity;
import imas.planning.entity.AircraftTypeEntity;
import imas.planning.entity.AirportEntity;
import imas.planning.entity.MaintenanceScheduleEntity;
import imas.planning.sessionbean.AircraftSessionBeanLocal;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.persistence.Temporal;
import org.primefaces.model.DefaultScheduleEvent;
import org.primefaces.model.DefaultScheduleModel;
import org.primefaces.model.LazyScheduleModel;
import org.primefaces.model.ScheduleEvent;
import org.primefaces.model.ScheduleModel;

/**
 *
 * @author Lei
 */
@Stateless
public class ViewMaintenanceScheduleSessionBean implements ViewMaintenanceScheduleSessionBeanLocal {

    @PersistenceContext
    private EntityManager em;
    private Map<String, String> aircrafts;
    List<AircraftEntity> aircraftList;
    private ScheduleModel lazyEventModel;
    @Temporal(javax.persistence.TemporalType.TIMESTAMP)
    private Date startDate;
    @Temporal(javax.persistence.TemporalType.TIMESTAMP)
    private Date endDate;

    private String maintenanceName;
    private ScheduleEvent event = new DefaultScheduleEvent();

    @EJB
    private AircraftSessionBeanLocal aircraftSessionBean;

    @Override
    public void create() {
        AircraftTypeEntity at1 = new AircraftTypeEntity("A980", (double) 10,  (double) 100000, (double) 200, (double) 3000, (double) 4400, (double) 20, "Gas", (double) 20);
        em.persist(at1);

        AirportEntity a1 = new AirportEntity(false, "Shijiazhuang", "ZD Airport", "SJZ", "China");
        AirportEntity a2 = new AirportEntity(true, "Guangzhou", "Baiyun Airport", "CAN", "China");
        AirportEntity a3 = new AirportEntity(true, "Beijing", "BJ International Airport", "BJIA", "China");

        em.persist(a1);
        em.persist(a2);
        em.persist(a3);

        AircraftGroupEntity ag1 = new AircraftGroupEntity("A980");
        em.persist(ag1);
         aircraftSessionBean.addAircraft("001", at1, (double) 20000000, (double) 1000000, (double) 19000000, (double) 20, (double) 0, "All is well", a2, a1, 4, 5, 4, 6, 6, 10, 7, 50, (double) 30);
        aircraftSessionBean.addAircraft("002", at1, (double) 20000000, (double) 1000000, (double) 19000000, (double) 18, (double) 0, "All is well", a3, a2, 4, 5, 4, 6, 6, 30, 7, 30, (double) 35);
        aircraftSessionBean.addAircraft("003", at1, (double) 20000000, (double) 1000000, (double) 19000000, (double) 20, (double) 0, "All is well", a3, a2, 0, 0, 4, 6, 6, 30, 7, 50, (double) 30);

        Query queryForAircraft = em.createQuery("select at from AircraftEntity at");
        AircraftEntity ac1 = (AircraftEntity) queryForAircraft.getResultList().get(0);
        AircraftEntity ac2 = (AircraftEntity) queryForAircraft.getResultList().get(1);

        MaintenanceScheduleEntity ms1 = new MaintenanceScheduleEntity();
        ms1.setAircraft(ac1);
        Date currentDate = new Date();
        Date pastDate = new Date(currentDate.getTime() + (1000 * 60 * 60 * 24));
        ms1.setStartingTime(currentDate);
        ms1.setEndingTime(pastDate);
        ms1.setMaintenanceType("repair");
        MaintenanceScheduleEntity ms2 = new MaintenanceScheduleEntity();
        ms2.setAircraft(ac2);
        em.persist(ms1);
        em.persist(ms2);
    }

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
//        query = em.createQuery("SELECT f FROM MaintenanceScheduleEntity f ");
//        List<MaintenanceScheduleEntity> list = query.getResultList();
//        aircrafts = new HashMap<String, String>();
//        if (!list.isEmpty()) {
//            List<Long> aircraft = new ArrayList<Long>();
//            Long tailId;
//            for (int i = 0; i < list.size(); i++) {
//                tailId = list.get(i).getAircraft().getId();
//                if (!aircraft.contains(tailId)) {
//                    aircraft.add(tailId);
//                    aircrafts.put(list.get(i).getAircraft().getTailId(), list.get(i).getAircraft().getTailId());
//                }
//            }
//
//        }
        return aircrafts;
    }

    @Override
    public ScheduleModel createEvent(String aircraft) {
        Query query;
        query = em.createQuery("SELECT f FROM AircraftEntity f WHERE f.tailId=:aircraft");
        query.setParameter("aircraft", aircraft);
        AircraftEntity selectedAircraft;
        selectedAircraft = (AircraftEntity) query.getResultList().get(0);
        query = em.createQuery("SELECT f FROM MaintenanceScheduleEntity f WHERE f.aircraft=:aircraft");
        query.setParameter("aircraft", selectedAircraft);
        List<MaintenanceScheduleEntity> list = query.getResultList();
        lazyEventModel = new DefaultScheduleModel();
        if (!list.isEmpty()) {
            for (int i = 0; i < list.size(); i++) {
                maintenanceName = list.get(i).getMaintenanceType();
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

    public List<AircraftEntity> getAircraftList() {
        return aircraftList;
    }

    public void setAircraftList(List<AircraftEntity> aircraftList) {
        this.aircraftList = aircraftList;
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

    public String getMaintenanceName() {
        return maintenanceName;
    }

    public void setMaintenanceName(String maintenanceName) {
        this.maintenanceName = maintenanceName;
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
