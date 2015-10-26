/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package imas.web.managedbean.inventory;

import imas.distribution.sessionbean.FlightLookupSessionBeanLocal;
import imas.inventory.entity.YieldManagementRuleEntity;
import imas.inventory.sessionbean.RulesManagementSessionBeanLocal;
import imas.inventory.sessionbean.YieldManagementSessionBeanLocal;
import imas.planning.entity.FlightEntity;
import java.io.Serializable;
import java.text.DecimalFormat;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.faces.view.ViewScoped;
import javax.inject.Named;
import javax.persistence.PostRemove;

/**
 *
 * @author Scarlett
 */
@Named
@ViewScoped
public class YieldManagementManagedBean implements Serializable {

    @EJB
    private YieldManagementSessionBeanLocal yieldManagementSessionBean;

    @EJB
    private RulesManagementSessionBeanLocal rulesManagementSessionBean;

    @EJB
    private FlightLookupSessionBeanLocal flightLookupSessionBean;

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
        this.flights = flightLookupSessionBean.getAllSellingFlights();
        FacesContext.getCurrentInstance().getExternalContext().getSessionMap().put("allFlights", this.flights);
        if (FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("selectedSellingFlightToManage") != null) {
            this.flight = (FlightEntity) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("selectedSellingFlightToManage");
            onFlightChange();
        }
    }

    @PostRemove
    public void destroy() {
        FacesContext.getCurrentInstance().getExternalContext().getSessionMap().remove("allFlights");
        FacesContext.getCurrentInstance().getExternalContext().getSessionMap().remove("selectedSellingFlightToManage");
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
            List<YieldManagementRuleEntity> rules = flight.getYieldManagementRules();
            for (YieldManagementRuleEntity r : rules) {
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
        if (rule1.getTimeToDepartureInDaysParameter() > 80) {
            FacesContext.getCurrentInstance().addMessage("rule1",
                    new FacesMessage(FacesMessage.SEVERITY_FATAL, "Update Failed:", "Time to departure parameter must be less than 80"));
        } else if (rule1.getTimeToDepartureInDaysParameter() < 40) {
            FacesContext.getCurrentInstance().addMessage("rule1",
                    new FacesMessage(FacesMessage.SEVERITY_FATAL, "Update Failed:", "Time to departure parameter must be more than 40"));
        } else if (rule1.getTotalRevenueToTotalCostParameter() > 2) {
            FacesContext.getCurrentInstance().addMessage("rule1",
                    new FacesMessage(FacesMessage.SEVERITY_FATAL, "Update Failed:", "Total revenue to total cost parameter must be less than 2.0"));
        } else if (rule1.getTotalRevenueToTotalCostParameter() < 1) {
            FacesContext.getCurrentInstance().addMessage("rule1",
                    new FacesMessage(FacesMessage.SEVERITY_FATAL, "Update Failed:", "Total revenue to total cost parameter must be more than 1.0"));
        } else {
            rulesManagementSessionBean.updateRule1(rule1);
            FacesContext.getCurrentInstance().addMessage("rule1",
                    new FacesMessage(FacesMessage.SEVERITY_INFO, "Successful:", "Rule 1 updated."));
        }

    }

    public void updateRule2() {
        if (rule2.getTimeToDepartureInDaysParameter() > 80) {
            FacesContext.getCurrentInstance().addMessage("rule2",
                    new FacesMessage(FacesMessage.SEVERITY_FATAL, "Update Failed:", "Time to departure parameter must be less than 80"));
        } else if (rule2.getTimeToDepartureInDaysParameter() < 40) {
            FacesContext.getCurrentInstance().addMessage("rule2",
                    new FacesMessage(FacesMessage.SEVERITY_FATAL, "Update Failed:", "Time to departure parameter must be more than 40"));
        } else if (rule2.getTotalRevenueToTotalCostParameter() > 1.2) {
            FacesContext.getCurrentInstance().addMessage("rule2",
                    new FacesMessage(FacesMessage.SEVERITY_FATAL, "Update Failed:", "Total revenue to total cost parameter must be less than 1.2"));
        } else if (rule1.getTotalRevenueToTotalCostParameter() < 0.5) {
            FacesContext.getCurrentInstance().addMessage("rule2",
                    new FacesMessage(FacesMessage.SEVERITY_FATAL, "Update Failed:", "Total revenue to total cost parameter must be more than 0.5"));
        } else {
            rulesManagementSessionBean.updateRule2(rule2);
            FacesContext.getCurrentInstance().addMessage("rule2",
                    new FacesMessage(FacesMessage.SEVERITY_INFO, "Successful:", "Rule 2 updated."));
        }
    }

    public void updateRule3() {
        if (rule3.getTimeToDepartureInDaysParameter() > 20) {
            FacesContext.getCurrentInstance().addMessage("rule3",
                    new FacesMessage(FacesMessage.SEVERITY_FATAL, "Update Failed:", "Time to departure parameter must be less than 20"));
        } else if (rule3.getTimeToDepartureInDaysParameter() < 10) {
            FacesContext.getCurrentInstance().addMessage("rule3",
                    new FacesMessage(FacesMessage.SEVERITY_FATAL, "Update Failed:", "Time to departure parameter must be more than 10"));
        } else if (rule3.getPercentageSoldParameter() > 0.6) {
            FacesContext.getCurrentInstance().addMessage("rule3",
                    new FacesMessage(FacesMessage.SEVERITY_FATAL, "Update Failed:", "Total percentage sold parameter must be less than 1.2"));
        } else if (rule3.getPercentageSoldParameter() < 0.6) {
            FacesContext.getCurrentInstance().addMessage("rule3",
                    new FacesMessage(FacesMessage.SEVERITY_FATAL, "Update Failed:", "Total percentage sold parameter must be more than 0.5"));
        } else {
            rulesManagementSessionBean.updateRule3(rule3);
            FacesContext.getCurrentInstance().addMessage("rule3",
                    new FacesMessage(FacesMessage.SEVERITY_INFO, "Successful:", "Rule 3 updated."));
        }
    }

    public void updateRule4() {
        if (rule4.getTimeToDepartureInDaysParameter() > 20) {
            FacesContext.getCurrentInstance().addMessage("rule4",
                    new FacesMessage(FacesMessage.SEVERITY_FATAL, "Update Failed:", "Time to departure parameter must be less than 20"));
        } else if (rule4.getTimeToDepartureInDaysParameter() < 10) {
            FacesContext.getCurrentInstance().addMessage("rule4",
                    new FacesMessage(FacesMessage.SEVERITY_FATAL, "Update Failed:", "Time to departure parameter must be more than 10"));
        } else if (rule4.getEconomyClass1RemainingQuotaParameter() > 10 || rule4.getEconomyClass1RemainingQuotaParameter() < 3) {
            FacesContext.getCurrentInstance().addMessage("rule4",
                    new FacesMessage(FacesMessage.SEVERITY_FATAL, "Update Failed:", "Economy class 1 remaining quota parameter must be between 3 to 10"));
        } else if (rule4.getEconomyClass2RemainingQuotaParameter() > 12 || rule4.getEconomyClass2RemainingQuotaParameter() < 5) {
            FacesContext.getCurrentInstance().addMessage("rule4",
                    new FacesMessage(FacesMessage.SEVERITY_FATAL, "Update Failed:", "Economy class 2 remaining quota parameter must be between 5 to 12"));
        } else {
            rulesManagementSessionBean.updateRule4(rule4);
            FacesContext.getCurrentInstance().addMessage("rule4",
                    new FacesMessage(FacesMessage.SEVERITY_INFO, "Successful:", "Rule 4 updated."));
        }
    }

    public String twoDecimalRound(double origin) {
        DecimalFormat f = new DecimalFormat("##.00");
        return f.format(origin);
    }

    public String getFlightPopularity() {
        if (flight != null) {
            double np = yieldManagementSessionBean.getRouteNormalizedPopularity(flight.getRoute());

            if (np > 0.8) {
                return "Very Popular";
            } else if (np > 0.6) {
                return "Popular";
            } else if (np > 0.4) {
                return "Normal";
            } else {
                return "Not Popular";
            }
        } else {
            return "";
        }

    }

}
