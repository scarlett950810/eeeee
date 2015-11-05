/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package imas.web.managedbean.planning;

import java.io.Serializable;
import javax.annotation.PostConstruct;
import javax.faces.context.FacesContext;
import javax.inject.Named;
import javax.faces.view.ViewScoped;
import imas.planning.entity.RouteEntity;
import imas.planning.sessionbean.RouteSessionBeanLocal;
import java.io.IOException;
import java.math.BigDecimal;
import java.math.RoundingMode;
import javax.ejb.EJB;
import javax.faces.context.ExternalContext;

/**
 *
 * @author wutong
 */
@Named(value = "routeCreationManagedBean")
@ViewScoped
public class RouteCreationManagedBean implements Serializable {

    private String hub;
    private String spoke;
    private Double revenue;
    private Double distance;
    @EJB
    private RouteSessionBeanLocal routeSession;

    /**
     * Creates a new instance of RouteCreationManagedBean
     */
    public RouteCreationManagedBean() {
    }

    @PostConstruct
    public void init() {
        hub = (String) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("hubAdded");
        spoke = (String) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("spokeAdded");
        distance = (Double) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("distAdded");
        revenue = (Double) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("revenue");
        revenue = revenue / 1000000;
        revenue = round(revenue,2);
    }

    public double round(double value, int places) {
        if (places < 0) {
            throw new IllegalArgumentException();
        }

        BigDecimal bd = new BigDecimal(value);
        bd = bd.setScale(places, RoundingMode.HALF_UP);
        return bd.doubleValue();
    }

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

    public Double getRevenue() {
        return revenue;
    }

    public void setRevenue(Double revenue) {
        this.revenue = revenue;
    }

    public Double getDistance() {
        return distance;
    }

    public void setDistance(Double distance) {
        this.distance = distance;
    }

    public void confirmRoute() throws IOException {
        routeSession.connectHubSpoke(hub, spoke);
        routeSession.AddDistToRoute(hub, spoke, distance);
        FacesContext fc = FacesContext.getCurrentInstance();
        ExternalContext ec = fc.getExternalContext();
        ec.redirect("planningRoute.xhtml");
    }
}
