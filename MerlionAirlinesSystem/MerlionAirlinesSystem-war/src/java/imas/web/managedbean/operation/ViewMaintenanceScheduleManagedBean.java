/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package imas.web.managedbean.operation;

import imas.operation.sessionbean.ViewMaintenanceScheduleSessionBeanLocal;
import java.io.Serializable;
import java.util.Calendar;
import java.util.Date;
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
public class ViewMaintenanceScheduleManagedBean implements Serializable {

    /**
     * Creates a new instance of ViewMaintenanceScheduleManagedBean
     */
    private ScheduleModel lazyEventModel;
    private ScheduleEvent event = new DefaultScheduleEvent();
    private Map<String, String> aircrafts;
    private String aircraft = null;
    private Map<String, String> data = new HashMap<String, String>();

    @EJB
    private ViewMaintenanceScheduleSessionBeanLocal viewMaintenanceScheduleSessionBean;

    @PostConstruct
    public void init() {
        if (viewMaintenanceScheduleSessionBean.getAircrafts() == null || viewMaintenanceScheduleSessionBean.getAircrafts().isEmpty()) {
            viewMaintenanceScheduleSessionBean.create();
            System.out.print("we");
        }
        aircrafts = viewMaintenanceScheduleSessionBean.getAircrafts();

    }

    public void submit() {
        FacesMessage msg;
        if (aircraft == null || aircraft.equals("Select Aircraft")) {
            msg = new FacesMessage(FacesMessage.SEVERITY_ERROR, "Invalid", "aircraft is not selected.");

        } else {
            lazyEventModel = viewMaintenanceScheduleSessionBean.createEvent(aircraft);
            if (lazyEventModel != null&&lazyEventModel.getEventCount()!=0) {
                msg = new FacesMessage("Selected", "Tail No:" + aircraft);
                RequestContext requestContext = RequestContext.getCurrentInstance();
                requestContext.execute("PF('dlg').show()");
            } else {
                msg = new FacesMessage("Warning", "Tail No:" + aircraft + "  has no maintenance schedule");
            }

        }

        FacesContext.getCurrentInstance().addMessage(null, msg);

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

    public Map<String, String> getData() {
        return data;
    }

    public void setData(Map<String, String> data) {
        this.data = data;
    }

}
