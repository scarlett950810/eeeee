/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package imas.web.managedbean.planning;

import imas.planning.entity.FlightEntity;
import imas.planning.entity.RouteEntity;
import imas.planning.sessionbean.RouteSessionBeanLocal;
import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.inject.Named;
import javax.faces.view.ViewScoped;

/**
 *
 * @author ruicai
 */
@Named(value = "flightsDisplayManagedBean")
@ViewScoped
public class FlightsDisplayManagedBean implements Serializable {

    private List<FlightEntity> flightsGenerated;
    private List<FlightEntity> flightsFiltered;
    private RouteEntity routeSelected;
    private Integer yearSelected;
    @EJB
    private RouteSessionBeanLocal routeSession;

    /**
     * Creates a new instance of FlightsDisplayManagedBean
     */
    public FlightsDisplayManagedBean() {

    }

    public List<FlightEntity> getFlightsFiltered() {
        return flightsFiltered;
    }

    public void setFlightsFiltered(List<FlightEntity> flightsFiltered) {
        this.flightsFiltered = flightsFiltered;
    }

    public Integer getYearSelected() {
        return yearSelected;
    }

    public void setYearSelected(Integer yearSelected) {
        this.yearSelected = yearSelected;
    }

    public void goBack() throws IOException {
        FacesContext fc = FacesContext.getCurrentInstance();
        ExternalContext ec = fc.getExternalContext();
        ec.redirect("planningSetFrequency.xhtml");
    }

    public List<String> getRoutesName() {
        System.err.println("dfdsf");
        List a = new ArrayList<String>();
        System.err.println("ddsadda" + routeSelected);
        a.add(routeSelected.getRouteName());
        System.err.println("daf" + routeSelected);
        System.err.println("routename" + routeSelected.getRouteName());
        a.add(routeSelected.getReverseRoute().getRouteName());
        System.err.println("routename" + routeSelected.getReverseRoute().getRouteName());

        return a;
    }

    public List<FlightEntity> getFlightsGenerated() {
        return flightsGenerated;
    }

    public void setFlightsGenerated(List<FlightEntity> flightsGenerated) {
        this.flightsGenerated = flightsGenerated;
    }

    @PostConstruct
    public void init() {
        yearSelected = (Integer) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("yearSelected");
        routeSelected = (RouteEntity) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("routeSelected");
        flightsGenerated = routeSession.retrieveAllFlightsGenerated(yearSelected, routeSelected);

    }

}
