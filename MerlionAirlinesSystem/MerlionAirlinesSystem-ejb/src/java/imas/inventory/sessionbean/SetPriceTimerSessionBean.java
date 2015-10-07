/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package imas.inventory.sessionbean;

import javax.ejb.EJB;
import javax.ejb.Schedule;
import javax.ejb.Singleton;

/**
 *
 * @author Scarlett
 */
@Singleton
public class SetPriceTimerSessionBean {
    
    private int monthToDeparture;

    public int getMonthToDeparture() {
        return monthToDeparture;
    }

    public void setMonthToDeparture(int monthToDeparture) {
        this.monthToDeparture = monthToDeparture;
    }

    @EJB
    private SeatsManagementSessionBeanLocal seatsManagementSessionBean;

    @Schedule(hour="*/1", persistent=false)
    public void doWork(){
        System.out.println("Set price timer run every 1 hour:");
        monthToDeparture = 12;
        seatsManagementSessionBean.autoPriceToDepartureAndUnpricedFlights(monthToDeparture);
    }
    
}
