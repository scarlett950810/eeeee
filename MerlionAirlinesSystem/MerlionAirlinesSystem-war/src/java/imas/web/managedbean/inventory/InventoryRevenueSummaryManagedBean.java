/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package imas.web.managedbean.inventory;

import imas.inventory.sessionbean.InventoryRevenueManagementSessionBeanLocal;
import imas.planning.entity.RouteEntity;
import imas.planning.sessionbean.RouteSessionBeanLocal;
import java.io.Serializable;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.context.FacesContext;
import javax.inject.Named;
import javax.faces.view.ViewScoped;
import javax.persistence.PostRemove;
import org.primefaces.event.SelectEvent;
import org.primefaces.model.chart.CartesianChartModel;
import org.primefaces.model.chart.PieChartModel;

/**
 *
 * @author Scarlett
 */
@Named(value = "inventoryRevenueSummaryManagedBean")
@ViewScoped
public class InventoryRevenueSummaryManagedBean implements Serializable {
    
    @EJB
    private InventoryRevenueManagementSessionBeanLocal inventoryRevenueManagementSessionBean;

    @EJB
    private RouteSessionBeanLocal routeSession;

    private int topXOnly;
    private List<RouteEntity> routeList;
    private RouteEntity selectedRoute;
    private Date routeFromDate;
    private Date routeFromMaxDate;
    private Date routeToDate;
    private Date routeToMinDate;
    private Date today;
    private CartesianChartModel routeDetailsByTimelineCombinedModel;
    private CartesianChartModel routeDetailsByBookingClassCombinedModel;

    public InventoryRevenueSummaryManagedBean() {
    }

    @PostConstruct
    public void init() {
        
        routeToMinDate = this.getToday();
        routeToDate = this.getToday();
        routeFromDate = addDays(routeToDate, -365);
        topXOnly = 10;
        routeList = routeSession.retrieveAllRoutes();
        FacesContext.getCurrentInstance().getExternalContext().getSessionMap().put("routeList", routeList);
        routeDetailsByTimelineCombinedModel = new CartesianChartModel();
        routeDetailsByBookingClassCombinedModel = new CartesianChartModel();
    }

    @PostRemove
    public void destroy() {
        FacesContext.getCurrentInstance().getExternalContext().getSessionMap().remove("routeList");
    }
    
    public int getTopXOnly() {
        return topXOnly;
    }

    public void setTopXOnly(int topXOnly) {
        this.topXOnly = topXOnly;
    }

    public RouteEntity getSelectedRoute() {
        return selectedRoute;
    }

    public void setSelectedRoute(RouteEntity selectedRoute) {
        this.selectedRoute = selectedRoute;
    }

    public List<RouteEntity> getRouteList() {
        return routeList;
    }

    public void setRouteList(List<RouteEntity> routeList) {
        this.routeList = routeList;
    }

    public Date getRouteFromDate() {
        return routeFromDate;
    }

    public void setRouteFromDate(Date routeFromDate) {
        this.routeFromDate = routeFromDate;
    }

    public Date getRouteToDate() {
        return routeToDate;
    }

    public void setRouteToDate(Date routeToDate) {
        this.routeToDate = routeToDate;
    }

    public Date getRouteFromMaxDate() {
        return routeFromMaxDate;
    }

    public void setRouteFromMaxDate(Date routeFromMaxDate) {
        this.routeFromMaxDate = routeFromMaxDate;
    }

    public Date getRouteToMinDate() {
        return routeToMinDate;
    }

    public void setRouteToMinDate(Date routeToMinDate) {
        this.routeToMinDate = routeToMinDate;
    }

    public void onRouteFromDateSelect(SelectEvent event) {
        routeToMinDate = routeFromDate;
        updateRouteGraphs();
    }

    public void onRouteToDateSelect(SelectEvent event) {
        routeFromMaxDate = routeToDate;
        updateRouteGraphs();
    }

    public Date getToday() {
        return new Date();
    }

    public void setToday(Date today) {
        this.today = today;
    }

    public CartesianChartModel getRouteDetailsByTimelineCombinedModel() {
        return routeDetailsByTimelineCombinedModel;
    }

    public void setRouteDetailsByTimelineCombinedModel(CartesianChartModel routeDetailsByTimelineCombinedModel) {
        this.routeDetailsByTimelineCombinedModel = routeDetailsByTimelineCombinedModel;
    }

    public CartesianChartModel getRouteDetailsByBookingClassCombinedModel() {
        return routeDetailsByBookingClassCombinedModel;
    }

    public void setRouteDetailsByBookingClassCombinedModel(CartesianChartModel routeDetailsByBookingClassCombinedModel) {
        this.routeDetailsByBookingClassCombinedModel = routeDetailsByBookingClassCombinedModel;
    }

    private Date addDays(Date original, int dayNo) {
        Calendar c = Calendar.getInstance();
        c.setTime(original);
        c.add(Calendar.DATE, dayNo);

        return c.getTime();
    }

    public void onRouteChange() {
        updateRouteGraphs();
    }

    public void updateRouteGraphs() {

    }
}
