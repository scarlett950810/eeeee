/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package imas.web.managedbean.inventory;

import imas.inventory.sessionbean.SeatsManagementSessionBeanLocal;
import imas.planning.entity.FlightEntity;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.inject.Named;
import javax.enterprise.context.Dependent;
import javax.faces.context.FacesContext;
import javax.persistence.PostRemove;

/**
 *
 * @author Scarlett
 */
@Named(value = "seatsManagementManagedBean")
@Dependent
public class SeatsManagementManagedBean {
    @EJB
    private SeatsManagementSessionBeanLocal seatsManagementSessionBean;
    
    private List<FlightEntity> pendingFlights;
    
    private int ecnomyClassComputedOverbookingLevel;
    
    private int ecnomyClassOverbookingLevel;

    /**
     * Creates a new instance of SeatsManagementManagedBean
     */
    public SeatsManagementManagedBean() {
    }
    @PostConstruct
    public void init() {
        this.pendingFlights = seatsManagementSessionBean.getFlightsWithoutBookingClass();
        FacesContext.getCurrentInstance().getExternalContext().getSessionMap().put("pendingFlights", this.pendingFlights);
    }

    @PostRemove
    public void destroy() {
        FacesContext.getCurrentInstance().getExternalContext().getSessionMap().remove("pendingFlights");
    }
    
}
