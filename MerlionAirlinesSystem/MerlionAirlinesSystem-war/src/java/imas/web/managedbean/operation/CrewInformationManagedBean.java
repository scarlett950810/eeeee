/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package imas.web.managedbean.operation;

import imas.common.entity.CabinCrewEntity;
import imas.common.entity.PilotEntity;
import imas.planning.entity.AircraftTypeEntity;
import imas.planning.sessionbean.CabinCrewSchedulingSessionBeanLocal;
import imas.planning.sessionbean.CrewSchedulingSessionBeanLocal;
import java.io.Serializable;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.context.FacesContext;
import javax.inject.Named;
import javax.faces.view.ViewScoped;

/**
 *
 * @author wutong
 */
@Named(value = "crewInformationManagedBean")
@ViewScoped
public class CrewInformationManagedBean implements Serializable{
    @EJB
    private CrewSchedulingSessionBeanLocal crewSchedulingSessionBean;
    @EJB
    private CabinCrewSchedulingSessionBeanLocal cabinCrewSchedulingSessionBean;
    private List<PilotEntity> pilots;
    private List<CabinCrewEntity> cabinCrewList;
    
    /**
     * Creates a new instance of CrewInformationManagedBean
     */
    public CrewInformationManagedBean() {
    }
    
    @PostConstruct
    public void init() {
        System.err.println("create new flight managed bean");
        pilots = crewSchedulingSessionBean.retriveAllPilots();        
        System.err.println("create new flight managed bean" + pilots.size());
        cabinCrewList = cabinCrewSchedulingSessionBean.retrieveAllCabinCrew();
    }

    public List<PilotEntity> getPilots() {
        return pilots;
    }

    public void setPilots(List<PilotEntity> pilots) {
        this.pilots = pilots;
    }
    
    public String getTypesToFly(PilotEntity pilot){
        List<String> types = pilot.getAircraftTypeCapabilities();
        String typesToFly = "";
        //System.err.println("Pilot"+pilot.getDisplayName());
        for(String t: types){
            //System.err.println("type haha"+t);
            typesToFly = typesToFly+t;
        }
        return typesToFly;
    }
    
    public String getMileageLimit(PilotEntity pilot){
        if(pilot.getMileageLimit())
            return "10,000";
        else
            return "5,000";
    }

    public List<CabinCrewEntity> getCabinCrewList() {
        return cabinCrewList;
    }

    public void setCabinCrewList(List<CabinCrewEntity> cabinCrewList) {
        this.cabinCrewList = cabinCrewList;
    }
}
