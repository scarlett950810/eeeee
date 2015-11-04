/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package imas.web.managedbean.inventory;

import imas.inventory.sessionbean.SetPriceTimerSessionBeanLocal;
import java.io.Serializable;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.inject.Named;
import javax.faces.view.ViewScoped;

/**
 *
 * @author Scarlett
 */
@Named(value = "inventoryTimerManagedBean")
@ViewScoped
public class InventoryTimerManagedBean implements Serializable {
    
    @EJB
    private SetPriceTimerSessionBeanLocal setPriceTimerSessionBean;

    private int monthsToDeparture;

    public int getMonthsToDeparture() {
        return monthsToDeparture;
    }

    public void setMonthsToDeparture(int monthsToDeparture) {
        this.monthsToDeparture = monthsToDeparture;
    }
    
    public void update() {
        System.out.println("monthsToDeparture = " + monthsToDeparture);
        System.out.println("setPriceTimerSessionBean.getMonthsToDeparture = " + setPriceTimerSessionBean.getMonthsToDeparture());
        if (monthsToDeparture == setPriceTimerSessionBean.getMonthsToDeparture()) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Not Updated",
                "Parameter does not change."));
        } else {
            setPriceTimerSessionBean.setMonthsToDeparture(monthsToDeparture);
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Updated",
                "Now flights would be priced " + monthsToDeparture + " months to departure."));
        }
        
    }
    /**
     * Creates a new instance of InventoryTimerManagedBean
     */
    public InventoryTimerManagedBean() {
    }
    
    @PostConstruct
    public void init() {
        monthsToDeparture = setPriceTimerSessionBean.getMonthsToDeparture();
    }
    
}
