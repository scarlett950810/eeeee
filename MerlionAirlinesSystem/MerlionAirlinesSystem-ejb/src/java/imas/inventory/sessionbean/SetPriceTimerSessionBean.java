/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package imas.inventory.sessionbean;

import javax.ejb.EJB;
import javax.ejb.Schedule;
import javax.ejb.Stateless;

/**
 *
 * @author Scarlett
 */
@Stateless
public class SetPriceTimerSessionBean {
    
    @EJB
    private BookingClassesManagementSessionBeanLocal bookingClassesManagementSessionBean;

    @Schedule(minute="36",hour="*", persistent=false)
    public void detectFlightsToSetPrice(){
        System.out.println("Set price timer run every 1 hour:");
        /*
        set price timer run very one hour
        to detect flights within 13 months to departure but price not set yet,
        booking classes would be create automatically for those flights.
        
        flights become available for booking 12 months before depature, 
        thus staff from inventory have 1 month to manually adjust the price.
        */
        bookingClassesManagementSessionBean.autoPriceFlightsNeedToBePriced(13);
    }
    
}
