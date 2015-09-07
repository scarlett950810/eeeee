/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package imas.web.managedbean.planning;

import imas.planning.entity.AircraftEntity;
import imas.planning.entity.AircraftGroupEntity;
import imas.planning.sessionbean.AircraftGroupSessionBeanLocal;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.faces.view.ViewScoped;
import javax.inject.Named;
import javax.persistence.PostRemove;

/**
 *
 * @author Scarlett
 */
@Named(value = "aircraftGroupManagedBean")
@ViewScoped
public class AircraftGroupManagedBean implements Serializable {

    private String type;
    private List<AircraftGroupEntity> aircraftGroups;
    private AircraftGroupEntity aircraftGroup;
    private AircraftGroupEntity aircraftGroupToUpdate;
    private List<AircraftEntity> aircrafts;
    private AircraftEntity aircraft;

    @EJB
    private AircraftGroupSessionBeanLocal aircraftGroupSessionBean;

    @PostConstruct
    public void init() {
        FacesContext.getCurrentInstance().getExternalContext().getSessionMap().put("aircraftGroups", this.getAircraftGroups());
    }

    @PostRemove
    public void destroy() {
        FacesContext.getCurrentInstance().getExternalContext().getSessionMap().remove("aircraftGroups");
    }

    public AircraftGroupManagedBean() {
        this.aircraftGroups = new ArrayList();
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public AircraftEntity getAircraft() {
        return aircraft;
    }

    public void setAircraft(AircraftEntity aircraft) {
        this.aircraft = aircraft;
    }

    public List<AircraftEntity> getAircrafts() {
        return aircrafts;
    }

    public void setAircrafts(List<AircraftEntity> aircrafts) {
        this.aircrafts = aircrafts;
    }

    public List<AircraftGroupEntity> getAircraftGroups() {
        return aircraftGroupSessionBean.getAircraftGroups();
    }

    public void setAircraftGroups(List<AircraftGroupEntity> aircraftGroups) {
        this.aircraftGroups = aircraftGroups;
    }

    public AircraftGroupEntity getAircraftGroupToUpdate() {
        return aircraftGroupToUpdate;
    }

    public void setAircraftGroupToUpdate(AircraftGroupEntity aircraftGroupToUpdate) {
        this.aircraftGroupToUpdate = aircraftGroupToUpdate;
    }

    public void addAircraftGroup() {
        if (aircraftGroupSessionBean.checkAircraftGroupExistense(this.type)) {
            FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_ERROR, "Add group failed. A group of this type existed.", "");
            FacesContext.getCurrentInstance().addMessage(null, msg);
        } else {
            aircraftGroupSessionBean.addAircraftGroup(this.type);
            FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_INFO, "New aircraft group added", "");
            FacesContext.getCurrentInstance().addMessage(null, msg);
        }
    }

    public AircraftGroupEntity getAircraftGroup() {
        return aircraftGroup;
    }

    public void setAircraftGroup(AircraftGroupEntity aircraftGroup) {
        this.aircraftGroup = aircraftGroup;
    }

    public void onAircraftGroupChange() {
        if (aircraftGroup != null) {
            aircrafts = aircraftGroupSessionBean.getAircraftsFromGroup(aircraftGroup);
        }
    }

    public void onAircraftDelete(AircraftEntity aircraft) {
        aircraftGroupSessionBean.aircraftDetachGroup(aircraft);
        FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_INFO, "Aircraft is detached from the group.", "");
        FacesContext.getCurrentInstance().addMessage(null, msg);
    }

    public void onAircraftUpdate(AircraftEntity aircraft) {
        if (aircraftGroupToUpdate == aircraftGroup) {
            FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_ERROR, "New group is the same.", "");
            FacesContext.getCurrentInstance().addMessage(null, msg);
        } else {
            aircraftGroupSessionBean.aircraftChangeGroup(aircraft, aircraftGroupToUpdate);
            FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_INFO, "Aircraft group is changed.", "");
            FacesContext.getCurrentInstance().addMessage(null, msg);
        }

    }

}
