/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package imas.inventory.sessionbean;

import imas.inventory.entity.BookingClassRuleSetEntity;
import javax.ejb.Local;

/**
 *
 * @author Scarlett
 */
@Local
public interface TnCManagementSessionBeanLocal {
    
    public void updateTnC(BookingClassRuleSetEntity bookingClassRuleSet);

}
