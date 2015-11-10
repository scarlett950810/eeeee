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
    private Double totalPassengers;
    private Double marketShare;
    private Double additionalTraffic;
    private Double avgFare;
    private Double proratedFare;
    private Double cost;
    private Double profit;
    private Double avgRevenue;
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
        totalPassengers = (Double) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("totalPassengers");
        totalPassengers = round(totalPassengers,0);
        marketShare = (Double) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("marketShare");        
        additionalTraffic = (Double) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("additionalTraffic");
        avgFare = (Double) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("avgFare");
        proratedFare = (Double) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("proratedFare");
        revenue = (Double) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("revenue");
        revenue = revenue / 1000000;
        revenue = round(revenue,2);
        profit = totalPassengers * (marketShare/100) * avgFare + additionalTraffic * proratedFare;
        profit = profit/1000000;
        cost = 0.1398 * distance * (totalPassengers * (marketShare/100)+additionalTraffic);
        cost = cost/1000000;
        cost = round(cost,2);
        avgRevenue = routeSession.getAvgRevenue();
        avgRevenue = round(avgRevenue,2);
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

    public Double getCost() {
        return cost;
    }

    public void setCost(Double cost) {
        this.cost = cost;
    }

    public Double getProfit() {
        return profit;
    }

    public void setProfit(Double profit) {
        this.profit = profit;
    }

    public Double getAvgRevenue() {
        return avgRevenue;
    }

    public void setAvgRevenue(Double avgRevenue) {
        this.avgRevenue = avgRevenue;
    }

    public void confirmRoute() throws IOException {
        routeSession.connectHubSpoke(hub, spoke);
        routeSession.AddDistToRoute(hub, spoke, distance);
        routeSession.addRevenueForecast(hub, spoke, totalPassengers, marketShare, additionalTraffic, avgFare, proratedFare, revenue);
        FacesContext fc = FacesContext.getCurrentInstance();
        ExternalContext ec = fc.getExternalContext();
        ec.redirect("planningRoute.xhtml");
    }
}
