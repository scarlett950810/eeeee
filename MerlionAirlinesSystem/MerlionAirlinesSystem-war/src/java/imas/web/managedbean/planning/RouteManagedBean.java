/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package imas.web.managedbean.planning;

import imas.planning.entity.AirportEntity;
import imas.planning.entity.RouteEntity;
import imas.planning.sessionbean.RouteSessionBeanLocal;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.inject.Named;
import javax.faces.view.ViewScoped;
import org.primefaces.event.RowEditEvent;


 

/**
 *
 * @author ruicai
 */
@Named(value = "routeManagedBean")
@ViewScoped
public class RouteManagedBean implements Serializable{

    /**
     * Creates a new instance of RouteManagedBean
     */
    public RouteManagedBean() {
    }
 //   private Map<String, Map<String, String>> data = new HashMap<String, Map<String, String>>();
    @EJB
    private RouteSessionBeanLocal routeSession;
    private String hub;
    private String spoke;
    private Double distance;
    private AirportEntity airport;
    private Map<String, String> hubs;
    private Map<String, String> spokes;
    private List<String> routesName;
    private List<RouteEntity> routes;
    private String routeDelete;
   
    @PostConstruct
    public void init() {
        hubs = new HashMap<String, String>();
        spokes = new HashMap<String, String>();
        routesName = new ArrayList<String>();

        
        int i = 0;
        List<String> hubNames = new ArrayList();
        for(Object o: routeSession.retrieveHubs()){
            AirportEntity hub = (AirportEntity)o;
            String hName = hub.getAirportName();
            hubNames.add(hName);
        }
        for(String hubName: hubNames){
            hubs.put(hubName, hubName);
        }

        List<String> spokeNames = new ArrayList();
        for(Object o: routeSession.retrieveSpokes()){
            AirportEntity spoke = (AirportEntity)o;
            String sName = spoke.getAirportName();
            spokeNames.add(sName);
        }
        for(String spokeName: spokeNames){
            spokes.put(spokeName, spokeName);
        }
        routesName = routeSession.retrieveAllConnectionName();
                System.out.println("dadada1");
        routes = routeSession.retrieveAllRoutes();
        System.out.println("dadada");
        
        
    }

    public List<RouteEntity> getRoutes() {
        return routes;
    }

    public void setRoutes(List<RouteEntity> routes) {
        this.routes = routes;
    }

    public Double getDistance() {
        return distance;
    }

    public void setDistance(Double distance) {
        this.distance = distance;
    }

   
    public void onRowEdit(RowEditEvent event) {
        routeSession.updateRouteInfo(((RouteEntity) event.getObject()));
        FacesMessage msg = new FacesMessage("Route Edited", ((RouteEntity) event.getObject()).getId().toString());
        FacesContext.getCurrentInstance().addMessage(null, msg);
    }
    
//    public void updateRoutesInfo(){
//        System.out.println("0 ROUTE distance"+routes.get(0).getDistance());
//        routeSession.updateRoutesInfo(routes);
//    }
        
    

    public String getRouteDelete() {
        return routeDelete;
    }

    public void setRouteDelete(String routeDelete) {
        this.routeDelete = routeDelete;
    }

    public List<String> getRoutesName() {
        return routesName;
    }

    public void setRoutesName(List<String> routesName) {
        this.routesName = routesName;
    }
    

  /*  public Map<String, Map<String, String>> getData() {
        return data;
    }*/

    public String getHub() {
        return hub;
    }

    public void setHub(String hub) {
        this.hub = hub;
    }

    public String getSpoke() {
        return spoke;
    }

    public void setSpoke(String spoke) {
        this.spoke = spoke;
    }

    public Map<String, String> getHubs() {
        return hubs;
    }

    public void setHubs(Map<String, String> hubs) {
        this.hubs = hubs;
    }

    public Map<String, String> getSpokes() {
        ArrayList spokesTemp = new ArrayList<String>();
        for(Object o: routeSession.retrieveHubs()){
            AirportEntity hub = (AirportEntity)o;
            String hName = hub.getAirportName();
            spokesTemp.add(hName);
        }
        for(Object hubName: spokesTemp){
            String hub1 = (String)hubName;
            spokes.put(hub1, hub1);
        }
        return spokes;
    }

    public void setSpokes(Map<String, String> spokes) {
        this.spokes = spokes;
    }
    public void generateRoutes(){
        FacesMessage msg;
        if(hub.equals(spoke))
            msg = new FacesMessage("Unsuccessful", "Connecting two same airports not allowed");
        else if(routeSession.availabilityCheck(distance)){//add availability check function 在session bean里implement
            if(!routeSession.connectHubSpoke(hub, spoke))
                msg = new FacesMessage("Unsuccessful", "This route has been added");
            else {
                routeSession.AddDistToRoute(hub, spoke, distance);
                msg = new FacesMessage("Successful", "Added the route " + hub + "-->" + spoke +" successfully");
                //System.err.println("hehe");
            }
        }
        else{
            if(!routeSession.connectHubSpoke(hub, spoke))
                msg = new FacesMessage("Unsuccessful", "This route has been added");
            else{
                msg = new FacesMessage("", "Route added has exceed the maximum range of current fleet");
                //System.err.println("haha");
                }
            }
        FacesContext.getCurrentInstance().addMessage(null, msg);  
    }
    public void deleteRoute(){
        String[] airportsName = routeDelete.split(" ");
        routeSession.deleteRoutesByName(airportsName[0], airportsName[2]);
    }

   


  /*  public void onCountryChange() {
        if (country != null && !country.equals("")) {
            cities = data.get(country);
        } else {
            cities = new HashMap<String, String>();
        }
    }
*/
  /*  public void displayLocation() {
        FacesMessage msg;
        if (spoke != null && hub != null) {
            msg = new FacesMessage("Selected", hub + "and " + spoke);
        } else {
            msg = new FacesMessage(FacesMessage.SEVERITY_ERROR, "Invalid", "Hub is not selected.");
        }

        FacesContext.getCurrentInstance().addMessage(null, msg);
    }*/


}
