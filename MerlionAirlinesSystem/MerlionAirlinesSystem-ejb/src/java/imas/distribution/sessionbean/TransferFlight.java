/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package imas.distribution.sessionbean;

import imas.planning.entity.FlightEntity;
import imas.planning.entity.RouteEntity;
import java.util.List;

/**
 *
 * @author Scarlett
 */
public class TransferFlight {
    private RouteEntity route1;
    private RouteEntity route2;
    private List<FlightEntity> flights1;
    private List<FlightEntity> flights2;

    public RouteEntity getRoute1() {
        return route1;
    }

    public void setRoute1(RouteEntity route1) {
        this.route1 = route1;
    }

    public RouteEntity getRoute2() {
        return route2;
    }

    public void setRoute2(RouteEntity route2) {
        this.route2 = route2;
    }

    public List<FlightEntity> getFlights1() {
        return flights1;
    }

    public void setFlights1(List<FlightEntity> flights1) {
        this.flights1 = flights1;
    }

    public List<FlightEntity> getFlights2() {
        return flights2;
    }

    public void setFlights2(List<FlightEntity> flights2) {
        this.flights2 = flights2;
    }

    public TransferFlight() {
    }

    public TransferFlight(RouteEntity route1, RouteEntity route2, List<FlightEntity> flights1, List<FlightEntity> flights2) {
        this.route1 = route1;
        this.route2 = route2;
        this.flights1 = flights1;
        this.flights2 = flights2;
    }
    
}
