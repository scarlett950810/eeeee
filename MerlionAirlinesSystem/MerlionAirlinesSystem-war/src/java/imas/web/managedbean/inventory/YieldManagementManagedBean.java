/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package imas.web.managedbean.inventory;

import imas.inventory.entity.YieldManagementRuleEntity;
import imas.inventory.sessionbean.RulesManagementSessionBeanLocal;
import imas.inventory.sessionbean.YieldManagementSessionBeanLocal;
import imas.planning.entity.FlightEntity;
import java.io.Serializable;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.inject.Named;
import javax.faces.view.ViewScoped;
import javax.persistence.PostRemove;

/**
 *
 * @author Scarlett
 */
@Named(value = "yieldManagementManagedBean")
@ViewScoped
public class YieldManagementManagedBean implements Serializable  {
    
    @EJB
    private RulesManagementSessionBeanLocal rulesManagementSessionBean;
    
    private List<FlightEntity> flights;
    private YieldManagementRuleEntity rule1;
    private YieldManagementRuleEntity rule2;
    private YieldManagementRuleEntity rule3;
    private YieldManagementRuleEntity rule4;
    
    private FlightEntity flight;    
    
    /**
     * Creates a new instance of YieldManagementManagedBean
     */
    public YieldManagementManagedBean() {
    }

    @PostConstruct
    public void init() {
        this.flights = rulesManagementSessionBean.getAllFlights();
        FacesContext.getCurrentInstance().getExternalContext().getSessionMap().put("allFlights", this.flights);
//        yieldManagementSessionBean.insertRules();
    }

    @PostRemove
    public void destroy() {
        FacesContext.getCurrentInstance().getExternalContext().getSessionMap().remove("allFlights");
    }
    
    public FlightEntity getFlight() {
        return flight;
    }

    public void setFlight(FlightEntity flight) {
        this.flight = flight;
    }

    public List<FlightEntity> getFlights() {
        return flights;
    }

    public void setFlights(List<FlightEntity> flights) {
        this.flights = flights;
    }

    public YieldManagementRuleEntity getRule1() {
        return rule1;
    }

    public void setRule1(YieldManagementRuleEntity rule1) {
        this.rule1 = rule1;
    }

    public YieldManagementRuleEntity getRule2() {
        return rule2;
    }

    public void setRule2(YieldManagementRuleEntity rule2) {
        this.rule2 = rule2;
    }

    public YieldManagementRuleEntity getRule3() {
        return rule3;
    }

    public void setRule3(YieldManagementRuleEntity rule3) {
        this.rule3 = rule3;
    }

    public YieldManagementRuleEntity getRule4() {
        return rule4;
    }

    public void setRule4(YieldManagementRuleEntity rule4) {
        this.rule4 = rule4;
    }
    
    public void onFlightChange() {
        if (flight != null) {
            List<YieldManagementRuleEntity> rules = rulesManagementSessionBean.getAllFlightRules(flight);
            for (YieldManagementRuleEntity r: rules) {
//                System.out.println(r);
                switch (r.getNumber()) {
                    case 1:
                        rule1 = r;
                        break;
                    case 2:
                        rule2 = r;
                        break;
                    case 3:
                        rule3 = r;
                        break;
                    case 4:
                        rule4 = r;
                        break;
                    default:
                        System.out.println("!!!!!!!!!!!!!!!!!!!!!!!!!!!!");
                        System.out.println("YieldManagementManageBean.onFlightChange got rules numbered not 1 - 4!!!!");
                        break;
                }
            
            }
        }
    }
    
    public void updateRule1() {
        rulesManagementSessionBean.updateRule1(flight, rule1);
    }
    
    public void updateRule2() {
        rulesManagementSessionBean.updateRule2(flight, rule2);
    }
    
    public void updateRule3() {
        rulesManagementSessionBean.updateRule3(flight, rule3);
    }
    
    public void updateRule4() {
        rulesManagementSessionBean.updateRule4(flight, rule4);
    }
    
}
