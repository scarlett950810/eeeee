/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package imas.web.managedbean.planning;

import imas.planning.entity.AirportEntity;
import imas.planning.entity.RouteEntity;
import imas.planning.sessionbean.RouteSessionBeanLocal;
import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.application.FacesMessage;
import javax.faces.bean.SessionScoped;
import javax.faces.context.ExternalContext;
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
public class RouteManagedBean implements Serializable {

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
    private List<RouteEntity> filteredRoutes;
    private String routeDelete;
    private RouteEntity route;
    private Double totalPassengers;
    private Double marketShare;
    private Double additionalTraffic;
    private Double avgFare;
    private Double proratedFare;

    @PostConstruct
    public void init() {
        hubs = new HashMap<String, String>();
        spokes = new HashMap<String, String>();
        routesName = new ArrayList<String>();

        int i = 0;
        List<String> hubNames = new ArrayList();
        for (Object o : routeSession.retrieveHubs()) {
            AirportEntity hub = (AirportEntity) o;
            String hName = hub.getAirportName();
            hubNames.add(hName);
        }
        for (String hubName : hubNames) {
            hubs.put(hubName, hubName);
        }

        List<String> spokeNames = new ArrayList();
        for (Object o : routeSession.retrieveSpokes()) {
            AirportEntity spoke = (AirportEntity) o;
            String sName = spoke.getAirportName();
            spokeNames.add(sName);
        }
        for (String spokeName : spokeNames) {
            spokes.put(spokeName, spokeName);
        }
        routesName = routeSession.retrieveAllConnectionName();
        System.out.println("dadada1");
        routes = routeSession.retrieveAllRoutes();
        System.out.println("dadada");

    }

    public Double getTotalPassengers() {
        return totalPassengers;
    }

    public void setTotalPassengers(Double totalPassengers) {
        this.totalPassengers = totalPassengers;
    }

    public Double getMarketShare() {
        return marketShare;
    }

    public void setMarketShare(Double marketShare) {
        this.marketShare = marketShare;
    }

    public Double getAdditionalTraffic() {
        return additionalTraffic;
    }

    public void setAdditionalTraffic(Double additionalTraffic) {
        this.additionalTraffic = additionalTraffic;
    }

    public Double getAvgFare() {
        return avgFare;
    }

    public void setAvgFare(Double avgFare) {
        this.avgFare = avgFare;
    }

    public Double getProratedFare() {
        return proratedFare;
    }

    public void setProratedFare(Double proratedFare) {
        this.proratedFare = proratedFare;
    }

    public List<RouteEntity> getRoutes() {
        //     routes = getUpdatedRoutes();
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

    public RouteEntity getRoute() {
        return route;
    }

    public void setRoute(RouteEntity route) {
        this.route = route;
    }

    public void onRowEdit(RowEditEvent event) throws IOException {
        routeSession.updateRouteInfo(((RouteEntity) event.getObject()));
        FacesMessage msg = new FacesMessage("Route Edited", ((RouteEntity) event.getObject()).getId().toString());
        FacesContext.getCurrentInstance().addMessage(null, msg);
        FacesContext fc = FacesContext.getCurrentInstance();
        ExternalContext ec = fc.getExternalContext();
        ec.redirect("planningRouteCreation.xhtml");

    }

    public void onRowCancel(RowEditEvent event) {
        FacesMessage msg = new FacesMessage("Edit Cancelled", ((RouteEntity) event.getObject()).getRouteName());
        FacesContext.getCurrentInstance().addMessage(null, msg);
    }

    public List<RouteEntity> getUpdatedRoutes() {
        routes = routeSession.retrieveAllRoutes();
        return routes;

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
        for (Object o : routeSession.retrieveHubs()) {
            AirportEntity hub = (AirportEntity) o;
            String hName = hub.getAirportName();
            spokesTemp.add(hName);
        }
        for (Object hubName : spokesTemp) {
            String hub1 = (String) hubName;
            spokes.put(hub1, hub1);
        }
        return spokes;
    }

    public void setSpokes(Map<String, String> spokes) {
        this.spokes = spokes;
    }

    public List<RouteEntity> getFilteredRoutes() {
        return filteredRoutes;
    }

    public void setFilteredRoutes(List<RouteEntity> filteredRoutes) {
        this.filteredRoutes = filteredRoutes;
    }

    public Double getExpectedProfit() {
        if (totalPassengers == null || additionalTraffic == null || marketShare == null || avgFare == null || proratedFare == null) {
            //System.err.println(totalPassengers+additionalTraffic+marketShare+avgFare+proratedFare);
            System.err.println("Some variables are null");
            return 0.0;
        }
        return calculateRevenue();
    }

    public void generateRoutes() throws IOException {
        FacesMessage msg;
        if (hub.equals(spoke)) {
            msg = new FacesMessage("Failed", "Connecting two same airports not allowed");
        } else if (!routeSession.checkRouteByStringName(hub, spoke)) {
            msg = new FacesMessage("Failed", "This route has been added");
        } else if (!routeSession.availabilityCheck(distance)) {
            msg = new FacesMessage("Failed", "After checking the aircraft capability, route added has exceeded the maximum range of current fleet");
        } else {
            FacesContext fc = FacesContext.getCurrentInstance();
            Double revenue = calculateRevenue();
            ExternalContext ec = fc.getExternalContext();
            System.err.println("Inputs already: " + hub + spoke + distance + revenue);
            FacesContext.getCurrentInstance().getExternalContext().getSessionMap().put("hubAdded", hub);
            FacesContext.getCurrentInstance().getExternalContext().getSessionMap().put("spokeAdded", spoke);
            FacesContext.getCurrentInstance().getExternalContext().getSessionMap().put("distAdded", distance);
            FacesContext.getCurrentInstance().getExternalContext().getSessionMap().put("totalPassengers", totalPassengers);
            FacesContext.getCurrentInstance().getExternalContext().getSessionMap().put("additionalTraffic", additionalTraffic);
            FacesContext.getCurrentInstance().getExternalContext().getSessionMap().put("marketShare", marketShare);
            FacesContext.getCurrentInstance().getExternalContext().getSessionMap().put("avgFare", avgFare);
            FacesContext.getCurrentInstance().getExternalContext().getSessionMap().put("proratedFare", proratedFare);
            FacesContext.getCurrentInstance().getExternalContext().getSessionMap().put("revenue", revenue);
            ec.redirect("planningRouteCreation.xhtml");
            msg = new FacesMessage("","");
        }
        

        FacesContext.getCurrentInstance().addMessage(null, msg);
    }

    public void deleteRoute() throws IOException {
        String[] airportsName = routeDelete.split("-");
        FacesMessage msg;
        if (routeSession.deleteRoutesByName(airportsName[0], airportsName[1])) {
            FacesContext fc = FacesContext.getCurrentInstance();
            ExternalContext ec = fc.getExternalContext();
            ec.redirect("planningRoute.xhtml");
        } else {
            msg = new FacesMessage(FacesMessage.SEVERITY_ERROR, "Failed", "Please delete associated flights first");
            FacesContext.getCurrentInstance().addMessage(null, msg);
        }

    }

    public String calculateDist(RouteEntity route) {
        distance = route.getDistance();
        Double speed = routeSession.getSpeed(distance);
        Double time = distance / speed;
        routeSession.updateRouteTime(route, time);
        Integer hours = time.intValue();
        Double i = 60 * (time - time.intValue());
        Integer minutes = i.intValue();
        String s = hours.toString() + "hours " + minutes.toString() + "minutes";

        return s;

    }

    public void goDeleteRoute() throws IOException {
        FacesContext fc = FacesContext.getCurrentInstance();
        ExternalContext ec = fc.getExternalContext();
        ec.redirect("planningDeleteRoute.xhtml");
    }

    public void goAddRoute() throws IOException {
        FacesContext fc = FacesContext.getCurrentInstance();
        ExternalContext ec = fc.getExternalContext();
        ec.redirect("planningAddRoute.xhtml");
    }

    public Double calculateRevenue() {
        Double revenue = totalPassengers * (marketShare/100) * avgFare + additionalTraffic * proratedFare;
        revenue = revenue - 0.1398 * distance * (totalPassengers * (marketShare/100)+additionalTraffic);
        return revenue;
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
