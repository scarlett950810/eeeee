/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package GDS.sessionbean;

import GDS.entity.GDSFlightEntity;
import java.util.List;

/**
 *
 * @author Scarlett
 */
public class GDSTransferFlight {
    private String route1;
    private String route2;
    private List<GDSFlightEntity> flights1;
    private List<GDSFlightEntity> flights2;

    public String getRoute1() {
        return route1;
    }

    public void setRoute1(String route1) {
        this.route1 = route1;
    }

    public String getRoute2() {
        return route2;
    }

    public void setRoute2(String route2) {
        this.route2 = route2;
    }

    public List<GDSFlightEntity> getFlights1() {
        return flights1;
    }

    public void setFlights1(List<GDSFlightEntity> flights1) {
        this.flights1 = flights1;
    }

    public List<GDSFlightEntity> getFlights2() {
        return flights2;
    }

    public void setFlights2(List<GDSFlightEntity> flights2) {
        this.flights2 = flights2;
    }

    public GDSTransferFlight() {
    }

    public GDSTransferFlight(String route1, String route2, List<GDSFlightEntity> flights1, List<GDSFlightEntity> flights2) {
        this.route1 = route1;
        this.route2 = route2;
        this.flights1 = flights1;
        this.flights2 = flights2;
    }

}
