/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package imas.inventory.sessionbean;

import imas.inventory.entity.BookingClassRuleSetEntity;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

/**
 *
 * @author Scarlett
 */
@Stateless
public class TnCManagementSessionBean implements TnCManagementSessionBeanLocal {
    
    @PersistenceContext
    private EntityManager em;
    
    @Override
    public void updateTnC(BookingClassRuleSetEntity bookingClassRuleSet) {
        BookingClassRuleSetEntity bookingClassRuleSetToUpdate = em.find(BookingClassRuleSetEntity.class, bookingClassRuleSet.getId());
        bookingClassRuleSetToUpdate.setChangeFlightFeeForMoreThan60Days(bookingClassRuleSet.getChangeFlightFeeForMoreThan60Days());
        bookingClassRuleSetToUpdate.setChangeFlightFeeForLessThan60Days(bookingClassRuleSet.getChangeFlightFeeForLessThan60Days());
        bookingClassRuleSetToUpdate.setCancellationRefundForMoreThan60Days(bookingClassRuleSet.getCancellationRefundForMoreThan60Days());
        bookingClassRuleSetToUpdate.setCancellationRefundForLessThan60Days(bookingClassRuleSet.getCancellationRefundForLessThan60Days());
        bookingClassRuleSetToUpdate.setMileageAccrual(bookingClassRuleSet.getMileageAccrual());
    }

}
