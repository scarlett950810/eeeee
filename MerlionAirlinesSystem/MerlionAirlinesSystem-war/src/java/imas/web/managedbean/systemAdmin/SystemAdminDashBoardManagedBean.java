/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package imas.web.managedbean.systemAdmin;

import imas.inventory.sessionbean.InventoryRevenueManagementSessionBeanLocal;
import imas.planning.entity.RouteEntity;
import imas.planning.sessionbean.RouteSessionBeanLocal;
import java.io.Serializable;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.inject.Named;
import javax.faces.view.ViewScoped;
import org.primefaces.model.chart.CartesianChartModel;
import org.primefaces.model.chart.PieChartModel;

/**
 *
 * @author Scarlett
 */
@Named(value = "systemAdminDashBoardManagedBean")
@ViewScoped
public class SystemAdminDashBoardManagedBean implements Serializable {

    @EJB
    private InventoryRevenueManagementSessionBeanLocal inventoryRevenueManagementSessionBean;

    @EJB
    private RouteSessionBeanLocal routeSession;
    
    private Date allFromDate;
    private Date allFromMaxDate;
    private Date allToDate;
    private Date allToMinDate;
    private int topXOnly;
    private Date today;
    
    private PieChartModel allByRoutePieModel;
    private CartesianChartModel allDetailsByRouteCombinedModel;
    
    public SystemAdminDashBoardManagedBean() {
    }
    
    @PostConstruct
    public void init() {
        
        allToMinDate = this.getToday();
        allToDate = this.getToday();
        allFromDate = addDays(allToDate, -365);
        topXOnly = 10;
        allByRoutePieModel = new PieChartModel();
        allDetailsByRouteCombinedModel = new CartesianChartModel();
        updateAllGraphs();
    }

    public Date getAllFromDate() {
        return allFromDate;
    }

    public void setAllFromDate(Date allFromDate) {
        this.allFromDate = allFromDate;
    }

    public Date getAllFromMaxDate() {
        return allFromMaxDate;
    }

    public void setAllFromMaxDate(Date allFromMaxDate) {
        this.allFromMaxDate = allFromMaxDate;
    }

    public Date getAllToDate() {
        return allToDate;
    }

    public void setAllToDate(Date allToDate) {
        this.allToDate = allToDate;
    }

    public Date getAllToMinDate() {
        return allToMinDate;
    }

    public void setAllToMinDate(Date allToMinDate) {
        this.allToMinDate = allToMinDate;
    }

    public int getTopXOnly() {
        return topXOnly;
    }

    public void setTopXOnly(int topXOnly) {
        this.topXOnly = topXOnly;
    }

    public Date getToday() {
        return new Date();
    }

    public void setToday(Date today) {
        this.today = today;
    }

    public PieChartModel getAllByRoutePieModel() {
        return allByRoutePieModel;
    }

    public void setAllByRoutePieModel(PieChartModel allByRoutePieModel) {
        this.allByRoutePieModel = allByRoutePieModel;
    }

    public CartesianChartModel getAllDetailsByRouteCombinedModel() {
        return allDetailsByRouteCombinedModel;
    }

    public void setAllDetailsByRouteCombinedModel(CartesianChartModel allDetailsByRouteCombinedModel) {
        this.allDetailsByRouteCombinedModel = allDetailsByRouteCombinedModel;
    }
    
    private Date addDays(Date original, int dayNo) {
        Calendar c = Calendar.getInstance();
        c.setTime(original);
        c.add(Calendar.DATE, dayNo);

        return c.getTime();
    }
    
    public void updateAllGraphs() {
        List<RouteEntity> routeList = routeSession.retrieveAllRoutes();
        for (RouteEntity route: routeList) {
            System.out.println("in");
            allByRoutePieModel.set(route.getRouteName(), 
                    inventoryRevenueManagementSessionBean.getRouteTotalRevenueDuringDuration(route, allFromDate, allToDate));
        }
        allByRoutePieModel.setTitle("Revenue by Route");
        allByRoutePieModel.setLegendPosition("s");
        
        
    }
    
}
