/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package imas.inventory.sessionbean;

import javax.ejb.Local;

/**
 *
 * @author Scarlett
 */
@Local
public interface SetPriceTimerSessionBeanLocal {

    public void setMonthToDeparture(int monthToDeparture);
   
}
