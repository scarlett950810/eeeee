/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package imas.web.managedbean.planning;

import imas.planning.entity.RouteEntity;
import imas.planning.sessionbean.RouteSessionBeanLocal;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.context.FacesContext;
import javax.inject.Named;
import javax.faces.view.ViewScoped;

/**
 *
 * @author ruicai
 */
@Named(value = "routeGroupManagedBean")
@ViewScoped
public class RouteGroupManagedBean implements Serializable{
    
    private String groupCode;
    private Double maxRange;
    private Double minRange;
    private List<RouteEntity> routesRange;
    private List<RouteEntity> selectedRoutes;
    private String selectedRoute;
    private List<String> routesRangeString;

    @EJB
    private RouteSessionBeanLocal routeSession;
    
    
    @PostConstruct
    public void init() {
        
        routesRangeString = new ArrayList();
    }
    public void retrieveRoutesWithinRange(){
        List<RouteEntity> routesRangeInitial = routeSession.retrieveRoutesWithinRange(maxRange, minRange);
        routesRange = routeSession.filterRoutesToConnections(routesRangeInitial);
//        for(Object o:routesRange){
//            RouteEntity r = (RouteEntity)o;
//            routesRangeString.add(r.getReturnRoutesName());
//            System.out.println("return route is: " + r.getReturnRoutesName());
//        }
        System.out.println("Leave retrieveRoutesWithinRange"+routesRange.get(0).getReturnRoutesName()+routesRange.size());
        FacesContext.getCurrentInstance().getExternalContext().getSessionMap().put("routesRangeList", routesRange);

    }
    
    public void createRouteGroup(){
        System.out.println("maxrnage"+ maxRange);
        routeSession.createRouteGroup(groupCode, maxRange, minRange, (ArrayList<RouteEntity>) selectedRoutes);
    }

    public List<String> getRoutesRangeString() {
        return routesRangeString;
    }

    public void setRoutesRangeString(List<String> routesRangeString) {
        this.routesRangeString = routesRangeString;
    }

    public String getSelectedRoute() {
        return selectedRoute;
    }

    public void setSelectedRoute(String selectedRoute) {
        this.selectedRoute = selectedRoute;
    }
    public String getGroupCode() {
        return groupCode;
    }

    public void setGroupCode(String groupCode) {
        this.groupCode = groupCode;
    }

    public List<RouteEntity> getRoutesRange() {
        return routesRange;
    }

    public void setRoutesRange(List<RouteEntity> routesRange) {
        this.routesRange = routesRange;
    }

    public List<RouteEntity> getSelectedRoutes() {
        return selectedRoutes;
    }

    public void setSelectedRoutes(List<RouteEntity> selectedRoutes) {
        this.selectedRoutes = selectedRoutes;
    }

    public RouteSessionBeanLocal getRouteSession() {
        return routeSession;
    }

    public void setRouteSession(RouteSessionBeanLocal routeSession) {
        this.routeSession = routeSession;
    }

    public Double getMaxRange() {
        return maxRange;
    }

    public void setMaxRange(Double maxRange) {
        this.maxRange = maxRange;
    }

    public Double getMinRange() {
        return minRange;
    }

    public void setMinRange(Double minRange) {
        this.minRange = minRange;
    }


    /**
     * Creates a new instance of RouteGroupManagedBean
     */
    public RouteGroupManagedBean() {
    }
    
}
