/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package imas.web.managedbean.operation;

import imas.common.entity.StaffEntity;
import imas.common.sessionbean.AccountManagementSessionBeanLocal;
import imas.common.sessionbean.LoginSessionBeanLocal;
import imas.planning.entity.RouteEntity;
import imas.planning.sessionbean.RouteSessionBeanLocal;
import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.application.FacesMessage;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.inject.Named;
import javax.faces.view.ViewScoped;

/**
 *
 * @author wutong
 */
@Named(value = "indicatePreferenceManagedBean")
@ViewScoped
public class IndicatePreferenceManagedBean implements Serializable {

    private String preferredDay;
    private String currentDay;
    private RouteEntity route;
    @EJB
    RouteSessionBeanLocal routeSessionBean;
    @EJB
    AccountManagementSessionBeanLocal accountManagementSessionBean;
    @EJB
    LoginSessionBeanLocal loginSessionBean;
    private List<RouteEntity> selectedRoutes;
    private List<RouteEntity> currentRoutes;
    private String staffNo;
    private StaffEntity staff;

    /**
     * Creates a new instance of IndicatePreferenceManagedBean
     */
    @PostConstruct
    public void init() {
        staffNo = (String) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("staffNo");
        System.out.print("staff No inside managed bean init:" + staffNo);
        if (staffNo != null) {
            staff = loginSessionBean.fetchStaff(staffNo);
        }
    }

    public IndicatePreferenceManagedBean() {
    }

    public List<String> getDayList() {
        List<String> dayList = new ArrayList<>();
        dayList.add("Monday");
        dayList.add("Tuesday");
        dayList.add("Wednesday");
        dayList.add("Thusday");
        dayList.add("Friday");
        dayList.add("Saturday");
        dayList.add("Sunday");
        return dayList;

    }

    public String getPreferredDay() {
        return preferredDay;
    }

    public void setPreferredDay(String preferredDay) {
        this.preferredDay = preferredDay;
    }

    public RouteEntity getRoute() {
        if (routeSessionBean.getRouteForPreference() == null) {
            return null;
        }
        return routeSessionBean.getRouteForPreference();
    }

    public void setRoute(RouteEntity route) {
        this.route = route;
    }

    public void update() throws IOException {
        FacesContext fc = FacesContext.getCurrentInstance();
        ExternalContext ec = fc.getExternalContext();
        if (selectedRoutes.size() > 3) {
            fc.addMessage(null, new FacesMessage("", "Please only select 3 routes maximum"));
        } else {
            accountManagementSessionBean.updatePreference(selectedRoutes, preferredDay, staffNo);
            ec.redirect("operationIndicatePreference.xhtml");
        }

    }

    public List<RouteEntity> getSelectedRoutes() {
        return selectedRoutes;
    }

    public void setSelectedRoutes(List<RouteEntity> selectedRoutes) {
        this.selectedRoutes = selectedRoutes;
    }

    public String getCurrentDay() {
        System.err.println("Enter get current day" + staff.getId());
        if (staff.getPreferredDay() == null) {
            return "";
        }
        return staff.getPreferredDay();
    }

    public void setCurrentDay(String currentDay) {
        this.currentDay = currentDay;
    }

    public List<RouteEntity> getCurrentRoutes() {
        if (staff.getPreferredRoutes().isEmpty()) {
            return null;
        }
        return staff.getPreferredRoutes();
    }

    public String getRouteString() {
        String routeString = "";
        List<RouteEntity> routeList = new ArrayList<>();
        routeList = getCurrentRoutes();
        if (routeList==null) {
            return routeString;
        }
        for (RouteEntity r : routeList) {
            routeString = routeString + "<br/>" + r;
        }
        return routeString;
    }

    public void setCurrentRoutes(List<RouteEntity> currentRoutes) {
        this.currentRoutes = currentRoutes;
    }

    public String getStaffNo() {
        return staffNo;
    }

    public void setStaffNo(String staffNo) {
        this.staffNo = staffNo;
    }

    public StaffEntity getStaff() {
        return staff;
    }

    public void setStaff(StaffEntity staff) {
        this.staff = staff;
    }

}
