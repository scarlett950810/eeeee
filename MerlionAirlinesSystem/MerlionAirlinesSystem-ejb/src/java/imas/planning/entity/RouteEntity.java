/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package imas.planning.entity;

import imas.inventory.entity.CostPairEntity;
import java.io.Serializable;
import java.util.List;
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

    @OneToOne
    private AirportEntity originAirport;

    @OneToOne
    private AirportEntity destinationAirport;
    private Double revenue;
    private Double cost;
    private Double distance;   //meter 
    private Double flightHours;
    @ManyToOne
    private RouteGroupEntity routeGroup;
    @OneToMany(mappedBy = "route")
    private List<FlightEntity> flights;
    @OneToOne
    private RouteEntity reverseRoute;
    private Double popularity;

    @OneToMany(mappedBy="route")
    private List<CostPairEntity> costPairs;

    public List<CostPairEntity> getCostPairs() {
        return costPairs;
    }

    public void setCostPairs(List<CostPairEntity> costPairs) {
        this.costPairs = costPairs;
    }

    public RouteEntity getReverseRoute() {
        return reverseRoute;
    }

    public void setReverseRoute(RouteEntity reverseRoute) {
        this.reverseRoute = reverseRoute;
    }

    public RouteEntity() {
    }

    public RouteEntity(AirportEntity originAirport, AirportEntity destinationAirport) {
        this.popularity = 0.5;
        this.originAirport = originAirport;
        this.destinationAirport = destinationAirport;
    }

    public RouteEntity(AirportEntity originAirport, AirportEntity destinationAirport,
            Double revenue, Double cost, Double distance, Double flightHours) {
        this.popularity = 0.5;
        this.originAirport = originAirport;
        this.destinationAirport = destinationAirport;
        this.revenue = revenue;
        this.cost = cost;
        this.distance = distance;
        this.flightHours = flightHours;
    }

    public String getRouteName() {
        return originAirport.getAirportName() + " to " + destinationAirport.getAirportName();
    }

    public String getReturnRoutesName() {
        System.out.println("enter function getReturnRoutesName" + originAirport.getAirportName() + destinationAirport.getAirportName());
        return originAirport.getAirportName() + " to " + destinationAirport.getAirportName() + " and its return route " + destinationAirport.getAirportName() + " to " + originAirport.getAirportName();

    }

    /**
     * Get the value of flightHours
     *
     * @return the value of flightHours
     */
    public Double getFlightHours() {
        return flightHours;
    }

    /**
     * Set the value of flightHours
     *
     * @param flightHours new value of flightHours
     */
    public void setFlightHours(Double flightHours) {
        this.flightHours = flightHours;
    }

    /**
     * Get the value of distance
     *
     * @return the value of distance
     */
    public Double getDistance() {
        return distance;
    }

    /**
     * Set the value of distance
     *
     * @param distance new value of distance
     */
    public void setDistance(Double distance) {
        this.distance = distance;
    }

//    @ManyToOne(cascade={CascadeType.ALL})
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public AirportEntity getOriginAirport() {
        //    System.out.println("Lalala");

        return originAirport;
    }

    public void setOriginAirport(AirportEntity originAirport) {

        this.originAirport = originAirport;
    }

    public AirportEntity getDestinationAirport() {
        //  System.out.println("Lalala1");

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

    public List<FlightEntity> getFlights() {
        return flights;
    }

    public void setFlights(List<FlightEntity> flights) {
        this.flights = flights;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (id != null ? id.hashCode() : 0);
        return hash;
    }

    public RouteGroupEntity getRouteGroup() {
        return routeGroup;
    }

    public void setRouteGroup(RouteGroupEntity routeGroup) {
        this.routeGroup = routeGroup;
    }

    public Double getPopularity() {
        return popularity;
    }

    public void setPopularity(Double popularity) {
        this.popularity = popularity;
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
        return "from " + originAirport.toString() + " to " + destinationAirport.toString();
    }

}
