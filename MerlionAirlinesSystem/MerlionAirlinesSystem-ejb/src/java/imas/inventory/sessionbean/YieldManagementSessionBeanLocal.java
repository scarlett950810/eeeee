/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package imas.inventory.sessionbean;

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

    public void runYieldManagementRule1(FlightEntity flight, Integer timeToDepartureInDaysParameter, Double totalRevenueToTotalCostParameter, Integer changeEconomyClass1Parameter, Integer changeEconomyClass2Parameter, Integer changeEconomyClass3Parameter, Integer changeEconomyClass4Parameter);

    public double getEconomyClassTotalCost(FlightEntity flight);

    public double getTotalEconomyClassRevenue(FlightEntity flight);

    public int getFromNowToDepartureInDay(FlightEntity flight);
    
}
