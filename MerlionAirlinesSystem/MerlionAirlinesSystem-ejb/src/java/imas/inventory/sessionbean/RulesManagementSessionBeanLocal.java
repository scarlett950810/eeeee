/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package imas.inventory.sessionbean;

import imas.inventory.entity.YieldManagementRuleEntity;
import javax.ejb.Local;

/**
 *
 * @author Scarlett
 */
@Local
public interface RulesManagementSessionBeanLocal {

    public void updateRule1(YieldManagementRuleEntity yieldManagementRule1);

    public void updateRule2(YieldManagementRuleEntity yieldManagementRule2);

    public void updateRule3(YieldManagementRuleEntity yieldManagementRule3);

    public void updateRule4(YieldManagementRuleEntity yieldManagementRule4);
    
}
