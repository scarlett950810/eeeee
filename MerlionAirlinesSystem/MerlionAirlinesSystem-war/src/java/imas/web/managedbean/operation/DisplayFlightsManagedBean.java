/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package imas.web.managedbean.operation;

import imas.operation.sessionbean.DisplayFlightsSessionBeanLocal;
import imas.planning.entity.AircraftEntity;
import imas.planning.entity.FlightEntity;
import imas.planning.entity.RouteEntity;
import java.io.Serializable;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.inject.Named;
import javax.faces.view.ViewScoped;
import org.primefaces.extensions.model.timeline.TimelineEvent;
import org.primefaces.extensions.model.timeline.TimelineGroup;
import org.primefaces.extensions.model.timeline.TimelineModel;

/**
 *
 * @author Scarlett
 */
@Named(value = "displayFlightsManagedBean")
@ViewScoped
public class DisplayFlightsManagedBean implements Serializable {
    
    @EJB
    private DisplayFlightsSessionBeanLocal displayFlightsSessionBean;

    private TimelineModel modelByRoute;
    
    private TimelineModel modelByAircraft;

    @PostConstruct
    protected void initialize() {
        // create timeline modelByRoute
        modelByRoute = new TimelineModel();
        
        List<RouteEntity> allRoutes = displayFlightsSessionBean.getAllRoutes();
        
        for (RouteEntity route: allRoutes) {            
            // create group
            TimelineGroup group = new TimelineGroup(route.getId().toString(), route);
            modelByRoute.addGroup(group);
            
            List<FlightEntity> flights = displayFlightsSessionBean.getFlightsUnderRoute(route);
            
            for (FlightEntity flight: flights) {
                modelByRoute.add(new TimelineEvent(flight, flight.getDepartureDate(), flight.getArrivalDate(), false, group.getId()));
            }
            
        }
        
        // create timeline modelByRoute
        modelByAircraft = new TimelineModel();
        
        List<AircraftEntity> allAircrafts = displayFlightsSessionBean.getAllAircrafts();
        
        for (AircraftEntity aircraft: allAircrafts) {
            // create group
            TimelineGroup group = new TimelineGroup(aircraft.getId().toString(), aircraft);
            modelByAircraft.addGroup(group);
            
            List<FlightEntity> flights = displayFlightsSessionBean.getFlightsUnderAircraft(aircraft);
            
            for (FlightEntity flight: flights) {
                modelByRoute.add(new TimelineEvent(flight, flight.getDepartureDate(), flight.getArrivalDate(), false, group.getId()));
            }
            
        }

    }

    public TimelineModel getModelByRoute() {
        return modelByRoute;
    }

    public void setModelByRoute(TimelineModel modelByRoute) {
        this.modelByRoute = modelByRoute;
    }

    public TimelineModel getModelByAircraft() {
        return modelByAircraft;
    }

    public void setModelByAircraft(TimelineModel modelByAircraft) {
        this.modelByAircraft = modelByAircraft;
    }
    
    /**
     * Creates a new instance of DisplayFlightsManagedBean
     */
    public DisplayFlightsManagedBean() {
    }
    
}
