/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package imas.web.managedbean.systemAdmin;

import imas.inventory.sessionbean.CostManagementSessionBean;
import imas.inventory.sessionbean.CostManagementSessionBeanLocal;
import imas.inventory.sessionbean.InventoryRevenueManagementSessionBeanLocal;
import imas.planning.entity.RouteEntity;
import imas.planning.sessionbean.RouteSessionBeanLocal;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.bean.SessionScoped;
import javax.faces.view.ViewScoped;
import javax.inject.Named;
import org.primefaces.event.SelectEvent;
import org.primefaces.model.chart.Axis;
import org.primefaces.model.chart.AxisType;
import org.primefaces.model.chart.BarChartModel;
import org.primefaces.model.chart.BarChartSeries;
import org.primefaces.model.chart.CartesianChartModel;
import org.primefaces.model.chart.LegendPlacement;
import org.primefaces.model.chart.LineChartSeries;
import org.primefaces.model.chart.LinearAxis;
import org.primefaces.model.chart.PieChartModel;

/**
 *
 * @author Scarlett
 */
@Named(value = "systemAdminDashBoardManagedBean")
@ViewScoped
public class SystemAdminDashBoardManagedBean implements Serializable {

    @EJB
    private CostManagementSessionBeanLocal costManagementSessionBean;

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
    
    private List<String[]> byRouteTable;

    public SystemAdminDashBoardManagedBean() {
    }

    @PostConstruct
    public void init() {

        allToMinDate = this.getToday();
        allToDate = this.getToday();
        allFromDate = addDays(allToDate, -30);
        topXOnly = 10;
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

    public List<String[]> getByRouteTable() {
        return byRouteTable;
    }

    public void setByRouteTable(List<String[]> byRouteTable) {
        this.byRouteTable = byRouteTable;
    }

    private Date addDays(Date original, int dayNo) {
        Calendar c = Calendar.getInstance();
        c.setTime(original);
        c.add(Calendar.DATE, dayNo);

        return c.getTime();
    }

    public void onAllFromDateSelect(SelectEvent event) {
        allToMinDate = allFromDate;
        updateAllGraphs();
    }

    public void onAllToDateSelect(SelectEvent event) {
        allFromMaxDate = allToDate;
        updateAllGraphs();
    }

    public void updateAllGraphs() {
        
        byRouteTable = new ArrayList<>();
        
        allByRoutePieModel = new PieChartModel();
        allByRoutePieModel.setMouseoverHighlight(true);
        allByRoutePieModel.setTitle("Revenue by Route");
        allByRoutePieModel.setLegendPlacement(LegendPlacement.OUTSIDEGRID);
        allByRoutePieModel.setShowDataLabels(true);
        allByRoutePieModel.setLegendPosition("e");
        allDetailsByRouteCombinedModel = new BarChartModel();
        allDetailsByRouteCombinedModel.setTitle("Revenue, Cost and Profit Margin by Route");
        allDetailsByRouteCombinedModel.setLegendPlacement(LegendPlacement.OUTSIDEGRID);
        allDetailsByRouteCombinedModel.setLegendPosition("e");
        allDetailsByRouteCombinedModel.setShowPointLabels(false);
        allDetailsByRouteCombinedModel.setMouseoverHighlight(true);
        allDetailsByRouteCombinedModel.setAnimate(true);
        
        BarChartSeries revenueSeries = new BarChartSeries();
        revenueSeries.setLabel("Revenue");
        BarChartSeries costSeries = new BarChartSeries();
        costSeries.setLabel("Cost");
        LineChartSeries profitMarginSeries = new LineChartSeries();
        profitMarginSeries.setLabel("Profit Margin");

        List<RouteEntity> routeList = routeSession.retrieveAllRoutes();
        int count = 1;
        for (RouteEntity route : routeList) {

            String routeName = route.getRouteName();
            double revenue = inventoryRevenueManagementSessionBean.getRouteTotalRevenueDuringDuration(route, allFromDate, allToDate);
            double cost = inventoryRevenueManagementSessionBean.getRouteTotalCostDuringDuration(route, allFromDate, allToDate);

            double pm = 0.0;
            if (cost > 0) {
                pm = (revenue - cost) / revenue;
            }
            
            System.out.println("revenue = " + revenue);
            System.out.println("cost = " + cost);
            
            String[] rowData = new String[5];
            rowData[0] = routeName;
            rowData[1] = Double.toString(CostManagementSessionBean.round(cost, 2));
            rowData[2] = Double.toString(CostManagementSessionBean.round(revenue, 2));
            rowData[3] = Double.toString(CostManagementSessionBean.round(pm * 100,2)) + "%";
            byRouteTable.add(rowData);
            
            allByRoutePieModel.set(count + " " + routeName, revenue);
            revenueSeries.set(count, revenue);
            costSeries.set(count, cost);
            profitMarginSeries.set(count, pm * 100);
            
            count++;
        }
        
        allDetailsByRouteCombinedModel.addSeries(revenueSeries);
        revenueSeries.setYaxis(AxisType.Y);
        allDetailsByRouteCombinedModel.addSeries(costSeries);
        costSeries.setYaxis(AxisType.Y3);
        allDetailsByRouteCombinedModel.addSeries(profitMarginSeries);
        profitMarginSeries.setYaxis(AxisType.Y2);
        
        Axis yAxis = allDetailsByRouteCombinedModel.getAxis(AxisType.Y);
        yAxis.setLabel("SG $");
        yAxis.setMin(0);
        Axis y2Axis = new LinearAxis("%");
        y2Axis.setMin(0);
        allDetailsByRouteCombinedModel.getAxes().put(AxisType.Y2, y2Axis);
        allDetailsByRouteCombinedModel.getAxes().put(AxisType.Y3, y2Axis);
        
        // TODO:
        // show only top X routes.
        // set revenue, cost to route entity, and rank again and return top routes.
    }

}
