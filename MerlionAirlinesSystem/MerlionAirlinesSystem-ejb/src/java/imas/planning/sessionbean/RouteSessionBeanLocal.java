/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package imas.planning.sessionbean;

import imas.planning.entity.AirportEntity;
import imas.planning.entity.FlightEntity;
import imas.planning.entity.RouteEntity;
import java.util.ArrayList;
import java.util.List;
import javax.ejb.Local;

/**
 *
 * @author ruicai
 */
@Local
public interface RouteSessionBeanLocal {

    Boolean checkRoute(AirportEntity origin, AirportEntity destination);
    Boolean checkRouteByStringName(String hub, String spoke);

    void addRoute(AirportEntity origin, AirportEntity destination);

    Boolean connectHubSpoke(String hub, String spoke);

    List<AirportEntity> retrieveHubs();

    List<AirportEntity> retrieveSpokes();

    Boolean deleteRoute(AirportEntity hub, AirportEntity spoke);

    List<RouteEntity> retrieveAllRoutes();

    List<String> retrieveAllConnectionName();

    Boolean deleteRoutesByName(String origin, String destination);

    void updateRoutesInfo(List<RouteEntity> routes);

    void updateRouteInfo(RouteEntity route);

    List<RouteEntity> retrieveRoutesWithinRange(Double max, Double min);

    List<RouteEntity> filterRoutesToConnections(List<RouteEntity> routes);

    void createRouteGroup(String groupCode, Double maxRange, Double minRange, ArrayList<RouteEntity> routesGrouped);

    Boolean availabilityCheck(Double range);

    void AddDistToRoute(String hub, String spoke, Double distance);

    void saveReturnFlights(FlightEntity f);

    List<FlightEntity> retrieveAllFlightsGenerated(Integer year, RouteEntity route);

    void updateRouteTime(RouteEntity route, Double time);

    Double getSpeed(Double range);

    RouteEntity getRouteForPreference();

    void addRevenueForecast(String hub, String spoke, Double totalPassengers, Double marketShare, Double additionalTraffic, Double avgFare, Double proratedFare, Double revenue);

    Double getAvgRevenue();

    
    
}
