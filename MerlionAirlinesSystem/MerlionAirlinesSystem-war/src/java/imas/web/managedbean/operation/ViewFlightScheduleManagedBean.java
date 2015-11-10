/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package imas.web.managedbean.operation;

import imas.operation.sessionbean.ViewFlightScheduleSessionBeanLocal;
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
public class ViewFlightScheduleManagedBean implements Serializable {

    private ScheduleModel lazyEventModel;
    private ScheduleModel eventModel;
    private ScheduleEvent event = new DefaultScheduleEvent();
    private Map<String, String> aircrafts;
    private String aircraft = null;
    private Map<String, String> data = new HashMap<String, String>();

    @EJB
    private ViewFlightScheduleSessionBeanLocal viewFlightScheduleSessionBean;

    @PostConstruct
    public void init() {
        aircrafts = viewFlightScheduleSessionBean.getAircrafts();
    }

    public void submit() {
        FacesMessage msg;
        if (aircraft == null || aircraft.equals("Select Aircraft")) {
            msg = new FacesMessage(FacesMessage.SEVERITY_ERROR, "Invalid", "aircraft is not selected.");

        } else {
            lazyEventModel = viewFlightScheduleSessionBean.createEvent(aircraft);
            if (lazyEventModel != null && lazyEventModel.getEventCount() != 0) {
                msg = new FacesMessage("Selected", "Tail No:" + aircraft);
                RequestContext requestContext = RequestContext.getCurrentInstance();
                requestContext.execute("PF('dlg').show()");
            } else {
                msg = new FacesMessage("Warning", "Tail No:" + aircraft + "  has no flight schedule");
            }

        }

        FacesContext.getCurrentInstance().addMessage(null, msg);

    }

    public void displayAll() {
        FacesMessage msg;
        if (aircraft == null || aircraft.equals("Select Aircraft")) {
            msg = new FacesMessage(FacesMessage.SEVERITY_ERROR, "Invalid", "aircraft is not selected.");

        } else {
            eventModel = viewFlightScheduleSessionBean.createAllEvent(aircraft);;
            if (eventModel != null && eventModel.getEventCount() != 0) {
                msg = new FacesMessage("Selected", "Tail No:" + aircraft);
                RequestContext requestContext = RequestContext.getCurrentInstance();
                requestContext.execute("PF('newDialog').show()");
            } else {
                msg = new FacesMessage("Warning", "Tail No:" + aircraft + "  has no schedule");
            }

        }

        FacesContext.getCurrentInstance().addMessage(null, msg);

    }

    public ScheduleModel getLazyEventModel() {
        return lazyEventModel;
    }

    public void setLazyEventModel(ScheduleModel lazyEventModel) {
        this.lazyEventModel = lazyEventModel;
    }

    public ScheduleModel getEventModel() {
        return eventModel;
    }

    public void setEventModel(ScheduleModel eventModel) {
        this.eventModel = eventModel;
    }

    public ScheduleEvent getEvent() {
        return event;
    }

    public void setEvent(ScheduleEvent event) {
        this.event = event;
    }

    public Map<String, String> getAircrafts() {
        return aircrafts;
    }

    public void setAircrafts(Map<String, String> aircrafts) {
        this.aircrafts = aircrafts;
    }

    public String getAircraft() {
        return aircraft;
    }

    public void setAircraft(String aircraft) {
        this.aircraft = aircraft;
    }

    public Map<String, String> getData() {
        return data;
    }

    public void setData(Map<String, String> data) {
        this.data = data;
    }

}
