/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package imas.web.managedbean.inventory;

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
import javax.faces.context.FacesContext;
import javax.inject.Named;
import javax.faces.view.ViewScoped;
import javax.persistence.PostRemove;
import org.primefaces.event.SelectEvent;
import org.primefaces.model.chart.Axis;
import org.primefaces.model.chart.AxisType;
import org.primefaces.model.chart.BarChartModel;
import org.primefaces.model.chart.BarChartSeries;
import org.primefaces.model.chart.CartesianChartModel;
import org.primefaces.model.chart.LegendPlacement;
import org.primefaces.model.chart.LineChartSeries;
import org.primefaces.model.chart.LinearAxis;

/**
 *
 * @author Scarlett
 */
@Named(value = "inventoryRevenueSummaryManagedBean")
@ViewScoped
public class InventoryRevenueSummaryManagedBean implements Serializable {

    @EJB
    private CostManagementSessionBeanLocal costManagementSessionBean;

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
        this.routeList = routeSession.retrieveAllRoutes();
        FacesContext.getCurrentInstance().getExternalContext().getSessionMap().put("routeList", this.routeList);
        routeDetailsByTimelineCombinedModel = new BarChartModel();
        routeDetailsByBookingClassCombinedModel = new BarChartModel();

        selectedRoute = routeList.get(0);
        updateRouteGraphs();
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
        if (selectedRoute != null) {
            updateRouteGraphs();
        }
    }

    public void onRouteToDateSelect(SelectEvent event) {
        routeFromMaxDate = routeToDate;
        if (selectedRoute != null) {
            updateRouteGraphs();
        }
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

    private Date addMonths(Date original, int monthNo) {
        Calendar c = Calendar.getInstance();
        c.setTime(original);
        c.add(Calendar.MONTH, monthNo);

        return c.getTime();
    }

    public void onRouteChange() {
        updateRouteGraphs();
    }

    public void updateRouteGraphs() {
        routeDetailsByTimelineCombinedModel = new BarChartModel();
        routeDetailsByTimelineCombinedModel.setTitle("Revenue, Cost and Profit Margin by Month for" + selectedRoute.getRouteName());
        routeDetailsByTimelineCombinedModel.setLegendPlacement(LegendPlacement.OUTSIDEGRID);
        routeDetailsByTimelineCombinedModel.setLegendPosition("e");
        routeDetailsByTimelineCombinedModel.setShowPointLabels(true);
        routeDetailsByTimelineCombinedModel.setMouseoverHighlight(false);

        BarChartSeries revenueSeries = new BarChartSeries();
        revenueSeries.setLabel("Revenue");
        BarChartSeries costSeries = new BarChartSeries();
        costSeries.setLabel("Cost");
        LineChartSeries profitMarginSeries = new LineChartSeries();
        profitMarginSeries.setLabel("Profit Margin");

        Date start = routeFromDate;
        Date end;
        while (start.before(routeToDate)) {
            end = addMonths(start, 1);
            if (end.after(routeToDate)) {
                end = routeToDate;
            }

            double revenue = inventoryRevenueManagementSessionBean.getRouteTotalRevenueDuringDuration(selectedRoute, start, end);
            double cost = inventoryRevenueManagementSessionBean.getRouteTotalCostDuringDuration(selectedRoute, start, end);

            // fill in randon data
            // to be removed later
            Random random = new Random();
            double totalFlightNo = ((routeToDate.getTime() - routeFromDate.getTime()) / (60 * 60 * 1000 * 24 * (0.5 + random.nextDouble())));
            double totalSeatNo = 20 * (1.5 + 2 * random.nextDouble());
            cost = costManagementSessionBean.getCostPerSeatPerMile(selectedRoute) * selectedRoute.getDistance() * totalFlightNo * totalSeatNo;
            revenue = (0.5 + random.nextDouble()) * 3 * cost;
            // random numerb end

            double pm = (revenue - cost) / revenue;
            revenueSeries.set(start, revenue);
            costSeries.set(start, cost);
            profitMarginSeries.set(start, pm);

            start = end;
        }

        routeDetailsByTimelineCombinedModel.addSeries(revenueSeries);
        revenueSeries.setYaxis(AxisType.Y);
        routeDetailsByTimelineCombinedModel.addSeries(costSeries);
        costSeries.setYaxis(AxisType.Y3);
        routeDetailsByTimelineCombinedModel.addSeries(profitMarginSeries);
        profitMarginSeries.setYaxis(AxisType.Y2);

        Axis yAxis = routeDetailsByTimelineCombinedModel.getAxis(AxisType.Y);
        yAxis.setLabel("SG $");
        yAxis.setMin(0);
        Axis y2Axis = new LinearAxis("Ratio");
        y2Axis.setMin(0);
        routeDetailsByTimelineCombinedModel.getAxes().put(AxisType.Y2, y2Axis);
        routeDetailsByTimelineCombinedModel.getAxes().put(AxisType.Y3, y2Axis);
        
    }

}
