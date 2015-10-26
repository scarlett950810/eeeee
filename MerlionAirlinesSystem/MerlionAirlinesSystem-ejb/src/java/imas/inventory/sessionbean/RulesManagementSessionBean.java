/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package imas.inventory.sessionbean;

import imas.inventory.entity.YieldManagementRuleEntity;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

/**
 *
 * @author Scarlett
 */
@Stateless
public class RulesManagementSessionBean implements RulesManagementSessionBeanLocal {

    @EJB
    private YieldManagementSessionBeanLocal yieldManagementSessionBean;

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public void updateRule1(YieldManagementRuleEntity yieldManagementRule1) {

        YieldManagementRuleEntity ruleToUpdate = (YieldManagementRuleEntity) entityManager.find(YieldManagementRuleEntity.class, yieldManagementRule1.getId());
        ruleToUpdate.setTimeToDepartureInDaysParameter(yieldManagementRule1.getTimeToDepartureInDaysParameter());
        ruleToUpdate.setTotalRevenueToTotalCostParameter(yieldManagementRule1.getTotalRevenueToTotalCostParameter());
        ruleToUpdate.setEnabled(yieldManagementRule1.isEnabled());

        if (yieldManagementRule1.isEnabled()) {
            yieldManagementSessionBean.runYieldManagementRule1(ruleToUpdate);
        }

    }

    @Override
    public void updateRule2(YieldManagementRuleEntity yieldManagementRule2) {

        YieldManagementRuleEntity ruleToUpdate = (YieldManagementRuleEntity) entityManager.find(YieldManagementRuleEntity.class, yieldManagementRule2.getId());
        ruleToUpdate.setTimeToDepartureInDaysParameter(yieldManagementRule2.getTimeToDepartureInDaysParameter());
        ruleToUpdate.setTotalRevenueToTotalCostParameter(yieldManagementRule2.getTotalRevenueToTotalCostParameter());
        ruleToUpdate.setEnabled(yieldManagementRule2.isEnabled());

        if (yieldManagementRule2.isEnabled()) {
            yieldManagementSessionBean.runYieldManagementRule2(ruleToUpdate);
        }
    }

    @Override
    public void updateRule3(YieldManagementRuleEntity yieldManagementRule3) {

        YieldManagementRuleEntity ruleToUpdate = (YieldManagementRuleEntity) entityManager.find(YieldManagementRuleEntity.class, yieldManagementRule3.getId());
        ruleToUpdate.setTimeToDepartureInDaysParameter(yieldManagementRule3.getTimeToDepartureInDaysParameter());
        ruleToUpdate.setPercentageSoldParameter(yieldManagementRule3.getPercentageSoldParameter());
        ruleToUpdate.setEnabled(yieldManagementRule3.isEnabled());

        if (yieldManagementRule3.isEnabled()) {
            yieldManagementSessionBean.runYieldManagementRule3(ruleToUpdate);
        }
    }

    @Override
    public void updateRule4(YieldManagementRuleEntity yieldManagementRule4) {

        YieldManagementRuleEntity ruleToUpdate = (YieldManagementRuleEntity) entityManager.find(YieldManagementRuleEntity.class, yieldManagementRule4);
        ruleToUpdate.setTimeToDepartureInDaysParameter(yieldManagementRule4.getTimeToDepartureInDaysParameter());
        ruleToUpdate.setEconomyClass1RemainingQuotaParameter(yieldManagementRule4.getEconomyClass1RemainingQuotaParameter());
        ruleToUpdate.setEconomyClass2RemainingQuotaParameter(yieldManagementRule4.getEconomyClass2RemainingQuotaParameter());
        ruleToUpdate.setChangeEconomyClass3and4and5To1Or2PercentageParameter(yieldManagementRule4.getChangeEconomyClass3and4and5To1Or2PercentageParameter());
        ruleToUpdate.setEnabled(yieldManagementRule4.isEnabled());

        if (yieldManagementRule4.isEnabled()) {
            yieldManagementSessionBean.runYieldManagementRule4(ruleToUpdate);
        }

    }

}
