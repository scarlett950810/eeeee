/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package imas.inventory.sessionbean;

import imas.inventory.entity.YieldManagementRuleEntity;
import imas.planning.entity.FlightEntity;
import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

/**
 *
 * @author Scarlett
 */
@Stateless
public class RulesManagementSessionBean implements RulesManagementSessionBeanLocal {

    @PersistenceContext
    private EntityManager entityManager;
    
    @Override
    public List<FlightEntity> getAllFlights() {
        Query queryForAllFlights = entityManager.createQuery("SELECT f FROM FlightEntity f");
        return queryForAllFlights.getResultList();        
    }

    @Override
    public List<YieldManagementRuleEntity> getAllFlightRules(FlightEntity flight) {
        Query queryForAllRulesUnderFlight = entityManager.createQuery("SELECT r FROM YieldManagementRuleEntity r "
                + "WHERE r.flight = :flight");
        queryForAllRulesUnderFlight.setParameter("flight", flight);
        
        return queryForAllRulesUnderFlight.getResultList();
    }
    
    @Override
    public void updateRule1(FlightEntity flight, YieldManagementRuleEntity yieldManagementRule1) {
        Query queryForRule = entityManager.createQuery("SELECT r FROM YieldManagementRuleEntity r"
                + " WHERE r.flight = :flight AND r.number = :number");
        queryForRule.setParameter("flight", flight);
        queryForRule.setParameter("number", 1);
        YieldManagementRuleEntity ruleToUpdate = (YieldManagementRuleEntity) queryForRule.getSingleResult();
        ruleToUpdate.setTimeToDepartureInDaysParameter(yieldManagementRule1.getTimeToDepartureInDaysParameter());
        ruleToUpdate.setTotalRevenueToTotalCostParameter(yieldManagementRule1.getTotalRevenueToTotalCostParameter());
        ruleToUpdate.setEnabled(yieldManagementRule1.isEnabled());
    }
    
    @Override
    public void updateRule2(FlightEntity flight, YieldManagementRuleEntity yieldManagementRule2) {
        Query queryForRule = entityManager.createQuery("SELECT r FROM YieldManagementRuleEntity r"
                + " WHERE r.flight = :flight AND r.number = :number");
        queryForRule.setParameter("flight", flight);
        queryForRule.setParameter("number", 2);
        YieldManagementRuleEntity ruleToUpdate = (YieldManagementRuleEntity) queryForRule.getSingleResult();
        ruleToUpdate.setTimeToDepartureInDaysParameter(yieldManagementRule2.getTimeToDepartureInDaysParameter());
        ruleToUpdate.setTotalRevenueToTotalCostParameter(yieldManagementRule2.getTotalRevenueToTotalCostParameter());
        ruleToUpdate.setEnabled(yieldManagementRule2.isEnabled());
    }
    
    @Override
    public void updateRule3(FlightEntity flight, YieldManagementRuleEntity yieldManagementRule3) {
        Query queryForRule = entityManager.createQuery("SELECT r FROM YieldManagementRuleEntity r"
                + " WHERE r.flight = :flight AND r.number = :number");
        queryForRule.setParameter("flight", flight);
        queryForRule.setParameter("number", 3);
        YieldManagementRuleEntity ruleToUpdate = (YieldManagementRuleEntity) queryForRule.getSingleResult();
        ruleToUpdate.setTimeToDepartureInDaysParameter(yieldManagementRule3.getTimeToDepartureInDaysParameter());
        ruleToUpdate.setPercentageSoldParameter(yieldManagementRule3.getPercentageSoldParameter());
        ruleToUpdate.setEnabled(yieldManagementRule3.isEnabled());
    }
    
    @Override
    public void updateRule4(FlightEntity flight, YieldManagementRuleEntity yieldManagementRule4) {
        Query queryForRule = entityManager.createQuery("SELECT r FROM YieldManagementRuleEntity r"
                + " WHERE r.flight = :flight AND r.number = :number");
        queryForRule.setParameter("flight", flight);
        queryForRule.setParameter("number", 4);
        YieldManagementRuleEntity ruleToUpdate = (YieldManagementRuleEntity) queryForRule.getSingleResult();
//        ruleToUpdate = yieldManagementRule4;
        ruleToUpdate.setTimeToDepartureInDaysParameter(yieldManagementRule4.getTimeToDepartureInDaysParameter());
        ruleToUpdate.setEconomyClass1RemainingQuotaParameter(yieldManagementRule4.getEconomyClass1RemainingQuotaParameter());
        ruleToUpdate.setEconomyClass2RemainingQuotaParameter(yieldManagementRule4.getEconomyClass2RemainingQuotaParameter());
        ruleToUpdate.setChangeEconomyClass3and4and5To1Or2PercentageParameter(yieldManagementRule4.getChangeEconomyClass3and4and5To1Or2PercentageParameter());
        ruleToUpdate.setEnabled(yieldManagementRule4.isEnabled());
        
    }
    
}
