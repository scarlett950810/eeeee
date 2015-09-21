/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package imas.inventory.sessionbean;

import imas.inventory.entity.YieldManagementRuleEntity;
import imas.planning.entity.FlightEntity;
import java.util.List;
import javax.ejb.Local;

/**
 *
 * @author Scarlett
 */
@Local
public interface RulesManagementSessionBeanLocal {

    public List<FlightEntity> getAllFlights();

    public List<YieldManagementRuleEntity> getAllFlightRules(FlightEntity flight);

    public void updateRule1(FlightEntity flight, YieldManagementRuleEntity yieldManagementRule1);

    public void updateRule2(FlightEntity flight, YieldManagementRuleEntity yieldManagementRule2);

    public void updateRule3(FlightEntity flight, YieldManagementRuleEntity yieldManagementRule3);

    public void updateRule4(FlightEntity flight, YieldManagementRuleEntity yieldManagementRule4);
    
}
