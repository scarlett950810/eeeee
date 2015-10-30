/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package imas.web.managedbean.inventory;

import imas.inventory.entity.CostPairEntity;
import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import java.io.Serializable;
import javax.ejb.EJB;
import imas.inventory.sessionbean.CostManagementSessionBeanLocal;
import imas.planning.entity.RouteEntity;
import imas.planning.sessionbean.RouteSessionBeanLocal;
import java.util.ArrayList;
import java.util.List;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;
import javax.inject.Named;
import javax.persistence.PostRemove;
import org.primefaces.context.RequestContext;
import org.primefaces.model.TreeNode;

/**
 *
 * @author Lei
 */
@Named(value = "inventoryCostManagedBean")
@ManagedBean
@ViewScoped
public class InventoryCostManagedBean implements Serializable {

    @EJB
    private CostManagementSessionBeanLocal costSession;

    @EJB
    private RouteSessionBeanLocal routeSession;

    private RouteEntity selectedRoute;
    private List<RouteEntity> routes;
    private List<CostPairEntity> costTable;
    private TreeNode root;
    private CostPairEntity selectedCost;
    private Double newCost;
    private boolean display = false;

    @PostConstruct
    public void init() {
        List<RouteEntity> routesAll = routeSession.retrieveAllRoutes();
        routes = routeSession.filterRoutesToConnections(routesAll);
        FacesContext.getCurrentInstance().getExternalContext().getSessionMap().put("routesRangeList", routes);

//        costTable = costSession.createTable();
        List<CostPairEntity> list = new ArrayList<CostPairEntity>();
        list.add(new CostPairEntity("Cost per seat per mile", 0.0, 0));//0*
        list.add(new CostPairEntity("Fixed cost per seat per mile", 0.0, 1));//1*
        list.add(new CostPairEntity("Flight distance per seat per day", 0.0, 2));//2
        list.add(new CostPairEntity("Fixed cost per seat", 0.0, 3));//3*
        list.add(new CostPairEntity("Operating cost per seat", 0.0, 4));//4
        list.add(new CostPairEntity("Other cost per seat", 0.0, 5));//5
        list.add(new CostPairEntity("Flight variable cost per seat per mile", 0.0, 6));//6*
        list.add(new CostPairEntity("Oil cost per seat per mile", 0.0, 7));//7
        list.add(new CostPairEntity("Crew cost per seat per mile", 0.0, 8));//8
        list.add(new CostPairEntity("Maintainence cost per seat per mile", 0.0, 9));//9
        list.add(new CostPairEntity("Tolls per seat per mile for take-off/landing", 0.0, 10));//10
        list.add(new CostPairEntity("Other variable cost per seat per mile", 0.0, 11));//11
        list.add(new CostPairEntity("Passenger cost per seat per mile", 0.0, 12));//12*
        list.add(new CostPairEntity("Show rate", 1.0, 13));//13
        list.add(new CostPairEntity("Average flight distance per passenger", 0.0, 14));//14
        list.add(new CostPairEntity("Average cost per passenger", 0.0, 15));//15
        list.add(new CostPairEntity("Sales cost per passenger", 0.0, 16));//16
        list.add(new CostPairEntity("Airport fee per passenger", 0.0, 17));//17
        list.add(new CostPairEntity("Check-in cost per passenger", 0.0, 18));//18
        list.add(new CostPairEntity("Meal cost per passenger", 0.0, 19));//19
        list.add(new CostPairEntity("Service cost per passenger", 0.0, 20));//20
        list.add(new CostPairEntity("First class service cost per passenger", 0.0, 21));//21
        list.add(new CostPairEntity("Delay cost per passenger", 0.0, 22));//22
        root = costSession.createRoot(list);
    }

    @PostRemove
    public void destroy() {
        FacesContext.getCurrentInstance().getExternalContext().getSessionMap().remove("routesRangeList");
    }

    public RouteEntity getSelectedRoute() {
        return selectedRoute;
    }

    public boolean isDisplay() {
        return display;
    }

    public void setDisplay(boolean display) {
        this.display = display;
    }

    public void setSelectedRoute(RouteEntity selectedRoute) {
        this.selectedRoute = selectedRoute;
    }

    public List<RouteEntity> getRoutes() {
        return routes;
    }

    public void setRoutes(List<RouteEntity> routes) {
        this.routes = routes;
    }

    public void onRouteChange() {
        display = true;
        if (selectedRoute != null) {
            System.out.print(selectedRoute);

            if (selectedRoute.getCostPairs().isEmpty() || selectedRoute.getCostPairs() == null || selectedRoute.getCostPairs().size() < 23) {
                costSession.initCostTable(selectedRoute);
            }
            costTable = costSession.getCostPairList(selectedRoute);
            root = costSession.createRoot(costTable);
//            System.out.print("111111111111111111");

        }
    }

    public void updateCostActionListener(ActionEvent event) {
        selectedCost = (CostPairEntity) event.getComponent().getAttributes().get("costPair");

        if (selectedCost.getCostType().equals("Fixed cost per seat per mile") || selectedCost.getCostType().equals("Fixed cost per seat")
                || selectedCost.getCostType().equals("Flight variable cost per seat per mile") || selectedCost.getCostType().equals("Passenger cost per seat per mile")
                || selectedCost.getCostType().equals("Show rate") || selectedCost.getCostType().equals("Average cost per passenger")
                || selectedCost.getCostType().equals("Cost per seat per mile")) {
            FacesMessage msg = new FacesMessage("Sorry", "Please change those editable costs");
            FacesContext.getCurrentInstance().addMessage(null, msg);
        } else {
            RequestContext requestContext = RequestContext.getCurrentInstance();
            newCost = null;
            requestContext.execute("PF('costDialog').show()");
        }

    }

    public void updateCost() {
        if (newCost == null) {
            FacesMessage msg = new FacesMessage("Sorry", "Please Enter the new cost");
            FacesContext.getCurrentInstance().addMessage(null, msg);
        } else {
            costSession.updateCost(selectedRoute, selectedCost.getCostType(), newCost);
            root = costSession.createRoot(selectedRoute.getCostPairs());
            FacesMessage msg = new FacesMessage("Reminder", selectedCost.getCostType() + " has been changed to " + Double.toString(newCost));
            FacesContext.getCurrentInstance().addMessage(null, msg);
        }

//        }
//        System.out.println(costName);
//        System.out.println(costFigure);
    }

    public List<CostPairEntity> getCostTable() {
        return costTable;
    }

    public Double getNewCost() {
        return newCost;
    }

    public void setNewCost(Double newCost) {
        this.newCost = newCost;
    }

    public void setCostTable(List<CostPairEntity> costTable) {
        this.costTable = costTable;
    }

    public TreeNode getRoot() {
        return root;
    }

    public CostPairEntity getSelectedCost() {
        return selectedCost;
    }

    public void setSelectedCost(CostPairEntity selectedCost) {
        this.selectedCost = selectedCost;
    }

}
