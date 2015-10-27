/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package imas.inventory.sessionbean;

import imas.inventory.entity.YieldManagementRuleEntity;
import imas.planning.entity.FlightEntity;
import imas.planning.entity.RouteEntity;
import javax.ejb.Local;

/**
 *
 * @author Scarlett
 */
@Local
public interface YieldManagementSessionBeanLocal {

    public double getFlightEconomyClassTotalCost(FlightEntity flight);

    public double getFlightTotalEconomyClassRevenue(FlightEntity flight);

    public int getFlightFromNowToDepartureInDay(FlightEntity flight);

    public int getFlightTotalNumberOfSoldEconomyClassBookingClassTickets(FlightEntity flight, String bookingClassName);
    
    public int getFlightTotalNumberOfSoldEconomyClassesTicket(FlightEntity flight);

    public void runYieldManagementRule1(YieldManagementRuleEntity yieldManagementRuleEntity);

    public void runYieldManagementRule2(YieldManagementRuleEntity yieldManagementRuleEntity);

    public void runYieldManagementRule3(YieldManagementRuleEntity yieldManagementRuleEntity);

    public void runYieldManagementRule4(YieldManagementRuleEntity yieldManagementRuleEntity);

    public void updateAllRoutePopularity();

    public Double computeRoutePopularity(RouteEntity route);

    public double getRouteNormalizedPopularity(RouteEntity route);

    public void createYieldManagementRulesForFlight(FlightEntity flight);

    public String getFlightFromNowToDepartureString(FlightEntity flight);

}
