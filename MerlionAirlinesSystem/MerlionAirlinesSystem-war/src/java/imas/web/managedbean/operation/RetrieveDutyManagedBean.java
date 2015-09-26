/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package imas.web.managedbean.operation;

import imas.operation.sessionbean.RetrieveDutySessionBeanLocal;
import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import org.primefaces.context.RequestContext;
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
    @EJB
    RetrieveDutySessionBeanLocal retrieveDutySessionBean;

    @PostConstruct
    public void init() {
        positions = new HashMap<String, String>();
        positions.put("Pilot", "Pilot");
        positions.put("Cabin Crew", "Cabin Crew");

        Map<String, String> map = new HashMap<String, String>();
        map = retrieveDutySessionBean.getPilot();
//        map.put("YL", "YL");
//        map.put("CR", "CR");
        data.put("Pilot", map);

        map = new HashMap<String, String>();
//        map.put("WT", "WT");
//        map.put("DY", "DY");
//        map.put("LH", "LH");
        
        map = retrieveDutySessionBean.getCabinCrew();
        data.put("Cabin Crew", map);

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
        if (position != null && name != null) {
            msg = new FacesMessage("Selected", position + " :  " + name);
            if (position.equals("Pilot")) {
                lazyEventModel = retrieveDutySessionBean.createPilotEvent(name);
            } else {//cabin crew
                lazyEventModel = retrieveDutySessionBean.createCabinEvent(name);
            }
            RequestContext requestContext = RequestContext.getCurrentInstance();
            requestContext.execute("PF('scheduleDialog').show()");
        } else {

            msg = new FacesMessage(FacesMessage.SEVERITY_ERROR, "Invalid", "Staff is not selected.");
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

}
