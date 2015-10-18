/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package imas.inventory.sessionbean;

import imas.inventory.entity.BookingClassEntity;
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

    public Double getRoutePopularity(RouteEntity route);

    public double getEconomyClassTotalCost(FlightEntity flight);

    public double getTotalEconomyClassRevenue(FlightEntity flight);

    public int getFromNowToDepartureInDay(FlightEntity flight);

    public int getTotalNumberOfSoldEconomyClass1Ticket(FlightEntity flight);

    public int getTotalNumberOfSoldEconomyClass2Ticket(FlightEntity flight);

    public int getTotalNumberOfSoldEconomyClass3Ticket(FlightEntity flight);

    public int getTotalNumberOfSoldEconomyClass4Ticket(FlightEntity flight);

    public int getTotalNumberOfSoldEconomyClass5Ticket(FlightEntity flight);

    public int getTotalNumberOfSoldEconomyClassesTicket(FlightEntity flight);

    public BookingClassEntity getEconomyClass1(FlightEntity flight);

    public BookingClassEntity getEconomyClass2(FlightEntity flight);

    public BookingClassEntity getEconomyClass3(FlightEntity flight);

    public BookingClassEntity getEconomyClass4(FlightEntity flight);

    public BookingClassEntity getEconomyClass5(FlightEntity flight);

    public void runYieldManagementRule1(YieldManagementRuleEntity yieldManagementRuleEntity);

    public void runYieldManagementRule2(YieldManagementRuleEntity yieldManagementRuleEntity);

    public void runYieldManagementRule3(YieldManagementRuleEntity yieldManagementRuleEntity);

    public void runYieldManagementRule4(YieldManagementRuleEntity yieldManagementRuleEntity);

    public void insertRules();

    public void autoCreateRulesForFlight(FlightEntity flight);

    public void updateAllRoutePopularity();

    public void disableYieldManagementRule(YieldManagementRuleEntity yieldManagementRuleEntity);

    public double getNormalizedPopularity(RouteEntity route);

}
