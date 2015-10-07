/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package imas.web.managedbean.operation;

import imas.common.entity.CabinCrewEntity;
import imas.common.entity.PilotEntity;
import imas.operation.sessionbean.RetrieveDutySessionBeanLocal;
import imas.planning.entity.FlightEntity;
import imas.planning.entity.RouteEntity;
import java.io.Serializable;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import org.primefaces.context.RequestContext;
import org.primefaces.extensions.model.timeline.TimelineEvent;
import org.primefaces.extensions.model.timeline.TimelineGroup;
import org.primefaces.extensions.model.timeline.TimelineModel;
import org.primefaces.model.DefaultScheduleEvent;
import org.primefaces.model.ScheduleEvent;
import org.primefaces.model.ScheduleModel;

/**
 *
 * @author Lei
 */
@ManagedBean
@ViewScoped
public class RetrieveDutyManagedBean implements Serializable {

    private Map<String, Map<String, String>> data = new HashMap<String, Map<String, String>>();
    private ScheduleModel lazyEventModel;
    private ScheduleEvent event = new DefaultScheduleEvent();
    private String position;
    private String name;
    private Map<String, String> positions;
    private Map<String, String> names;
    private TimelineModel pilotDutyTimeline;    
    private TimelineModel cabinCrewDutyTimeline;
    
    @EJB
    RetrieveDutySessionBeanLocal retrieveDutySessionBean;

    @PostConstruct
    public void init() {
        // create calendar view
        positions = new HashMap<String, String>();
        positions.put("Pilot", "Pilot");
        positions.put("Cabin Crew", "Cabin Crew");

        Map<String, String> map = new HashMap<String, String>();
        map = retrieveDutySessionBean.getPilot();
        data.put("Pilot", map);

        map = new HashMap<String, String>();

        map = retrieveDutySessionBean.getCabinCrew();
        data.put("Cabin Crew", map);

        // create timeline view
        pilotDutyTimeline = new TimelineModel();
        List<PilotEntity> allPilots = retrieveDutySessionBean.getAllPilots();
        
        for (PilotEntity pilot: allPilots) {            
            // create group
            TimelineGroup group = new TimelineGroup(pilot.getId().toString(), pilot);
            pilotDutyTimeline.addGroup(group);
            
            List<FlightEntity> flights = pilot.getPilotFlights();
            
            for (FlightEntity flight: flights) {
                pilotDutyTimeline.add(new TimelineEvent(flight, flight.getDepartureDate(), flight.getArrivalDate(), false, group.getId()));
            }
            
        }
        
        cabinCrewDutyTimeline = new TimelineModel();
        List<CabinCrewEntity> allCabinCrew = retrieveDutySessionBean.getAllCabinCrew();
        
        for (CabinCrewEntity cabinCrew: allCabinCrew) {            
            // create group
            TimelineGroup group = new TimelineGroup(cabinCrew.getId().toString(), cabinCrew);
            cabinCrewDutyTimeline.addGroup(group);
            
            List<FlightEntity> flights = cabinCrew.getCabinCrewFlights();
            
            for (FlightEntity flight: flights) {
                cabinCrewDutyTimeline.add(new TimelineEvent(flight, flight.getDepartureDate(), flight.getArrivalDate(), false, group.getId()));
            }
            
        }
    }

    public void onPositionChange() {
        if (position != null && !position.equals("") || position.equals("Select Position")) {
            names = data.get(position);
        } else {
            names = new HashMap<String, String>();
        }
    }

    public void submit() {
        FacesMessage msg;
        if (position == null || name == null) {
            msg = new FacesMessage(FacesMessage.SEVERITY_ERROR, "Invalid", "Staff is not selected.");
        } else {
            
            if (position.equals("Pilot")) {
                lazyEventModel = retrieveDutySessionBean.createPilotEvent(name);
            } else {//cabin crew
                lazyEventModel = retrieveDutySessionBean.createCabinEvent(name);
            }
            
            if (lazyEventModel != null && lazyEventModel.getEventCount() != 0) {
               msg = new FacesMessage("Selected", position + " :  " + name);
                RequestContext requestContext = RequestContext.getCurrentInstance();
                requestContext.execute("PF('dlg').show()");
            } else {
                msg = new FacesMessage("Warning", "Staff:" + name + "  has no flight schedule");
            }
            

        }
        

        FacesContext.getCurrentInstance().addMessage(null, msg);
    }

    public Map<String, Map<String, String>> getData() {
        return data;
    }

    public void setData(Map<String, Map<String, String>> data) {
        this.data = data;
    }

    public ScheduleModel getLazyEventModel() {
        return lazyEventModel;
    }

    public void setLazyEventModel(ScheduleModel lazyEventModel) {
        this.lazyEventModel = lazyEventModel;
    }

    public ScheduleEvent getEvent() {
        return event;
    }

    public void setEvent(ScheduleEvent event) {
        this.event = event;
    }

    public String getPosition() {
        return position;
    }

    public void setPosition(String position) {
        this.position = position;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Map<String, String> getPositions() {
        return positions;
    }

    public void setPositions(Map<String, String> positions) {
        this.positions = positions;
    }

    public Map<String, String> getNames() {
        return names;
    }

    public void setNames(Map<String, String> names) {
        this.names = names;
    }

    public TimelineModel getPilotDutyTimeline() {
        return pilotDutyTimeline;
    }

    public void setPilotDutyTimeline(TimelineModel pilotDutyTimeline) {
        this.pilotDutyTimeline = pilotDutyTimeline;
    }

    public TimelineModel getCabinCrewDutyTimeline() {
        return cabinCrewDutyTimeline;
    }

    public void setCabinCrewDutyTimeline(TimelineModel cabinCrewDutyTimeline) {
        this.cabinCrewDutyTimeline = cabinCrewDutyTimeline;
    }
    
}
