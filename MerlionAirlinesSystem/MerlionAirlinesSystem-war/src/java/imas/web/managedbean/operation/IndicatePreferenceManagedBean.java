/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package imas.web.managedbean.operation;

import imas.planning.entity.RouteEntity;
import imas.planning.sessionbean.RouteSessionBeanLocal;
import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import javax.ejb.EJB;
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
    private RouteEntity route;
    @EJB
    RouteSessionBeanLocal routeSessionBean;

    /**
     * Creates a new instance of IndicatePreferenceManagedBean
     */
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
        ec.redirect("operationIndicatePreferenceUpdate.xhtml");
    }

}
