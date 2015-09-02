/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package imas.planning.entity;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;

/**
 *
 * @author Lei
 */
@Entity
public class RouteEntity implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private Double revenue;
    private Double cost;
    private Integer distance;
    private Double flightHours;

    //airport origin
    @OneToOne(cascade = {CascadeType.PERSIST})
    private AirportEntity originAirport;
    //airport destination
    @OneToOne(cascade = {CascadeType.PERSIST})
    private AirportEntity destinationAirport;

    @ManyToOne(cascade = {CascadeType.PERSIST})
    private RouteGroupEntity routeGroup;

    @OneToMany(cascade = {CascadeType.ALL}, mappedBy = "routeFlights")
    private List<FlightEntity> routeFlight = new ArrayList<FlightEntity>();

    public RouteEntity() {

    }

    public RouteEntity(Long id, Double revenue, Double cost, Integer distance, Double flightHours, AirportEntity originAirport, AirportEntity destinationAirport, RouteGroupEntity routeGroup) {
        this.id = id;
        this.revenue = revenue;
        this.cost = cost;
        this.distance = distance;
        this.flightHours = flightHours;
        this.originAirport = originAirport;
        this.destinationAirport = destinationAirport;
        this.routeGroup = routeGroup;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public AirportEntity getOriginAirport() {
        return originAirport;
    }

    public void setOriginAirport(AirportEntity originAirport) {
        this.originAirport = originAirport;
    }

    public AirportEntity getDestinationAirport() {
        return destinationAirport;
    }

    public void setDestinationAirport(AirportEntity destinationAirport) {
        this.destinationAirport = destinationAirport;
    }

    public Double getRevenue() {
        return revenue;
    }

    public void setRevenue(Double revenue) {
        this.revenue = revenue;
    }

    public Double getCost() {
        return cost;
    }

    public void setCost(Double cost) {
        this.cost = cost;
    }

    public Integer getDistance() {
        return distance;
    }

    public void setDistance(Integer distance) {
        this.distance = distance;
    }

    public Double getFlightHours() {
        return flightHours;
    }

    public void setFlightHours(Double flightHours) {
        this.flightHours = flightHours;
    }

    public RouteGroupEntity getRouteGroup() {
        return routeGroup;
    }

    public void setRouteGroup(RouteGroupEntity routeGroup) {
        this.routeGroup = routeGroup;
    }

    public List<FlightEntity> getRouteFlight() {
        return routeFlight;
    }

    public void setRouteFlight(List<FlightEntity> routeFlight) {
        this.routeFlight = routeFlight;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (id != null ? id.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof RouteEntity)) {
            return false;
        }
        RouteEntity other = (RouteEntity) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "imas.common.entity.RouteEntity[ id=" + id + " ]";
    }

}
