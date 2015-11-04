/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package imas.web.managedbean.systemAdmin;

import imas.inventory.sessionbean.CostManagementSessionBeanLocal;
import imas.inventory.sessionbean.InventoryRevenueManagementSessionBeanLocal;
import imas.planning.entity.RouteEntity;
import imas.planning.sessionbean.RouteSessionBeanLocal;
import java.io.Serializable;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Random;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.inject.Named;
import javax.faces.view.ViewScoped;
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

    public SystemAdminDashBoardManagedBean() {
    }

    @PostConstruct
    public void init() {

        allToMinDate = this.getToday();
        allToDate = this.getToday();
        allFromDate = addDays(allToDate, -365);
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
        allDetailsByRouteCombinedModel.setShowPointLabels(true);
        allDetailsByRouteCombinedModel.setMouseoverHighlight(false);
        
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

            
            // fill in randon data
            // to be removed later
            /*
            Random random = new Random();
            double totalFlightNo = ((allToDate.getTime() - allFromDate.getTime()) / (60 * 60 * 1000 * 24 * (0.9 + 0.2 * random.nextDouble())));
            double totalSeatNo = 5 * (1.5 + 0.5 * random.nextDouble()) + 15;
            cost = costManagementSessionBean.getCostPerSeatPerMile(route) * route.getDistance() * totalFlightNo * totalSeatNo;
            revenue = (0.5 + random.nextDouble()) * 3 * cost;
                    */
            // random numerb end

            
            double pm = (revenue - cost) / revenue;
            allByRoutePieModel.set(count + " " + routeName, revenue);
            revenueSeries.set(count, revenue);
            costSeries.set(count, cost);
            profitMarginSeries.set(count, pm);
            
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
        Axis y2Axis = new LinearAxis("Ratio");
        y2Axis.setMin(0);
        allDetailsByRouteCombinedModel.getAxes().put(AxisType.Y2, y2Axis);
        allDetailsByRouteCombinedModel.getAxes().put(AxisType.Y3, y2Axis);
        
        // TODO:
        // show only top X routes.
        // set revenue, cost to route entity, and rank again and return top routes.
    }

}
