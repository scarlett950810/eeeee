/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package imas.planning.entity;

import java.io.Serializable;
import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToOne;

/**
 *
 * @author Lei
 */
@Entity
public class RouteEntity  implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    
    @OneToOne(cascade={CascadeType.PERSIST})
    private AirportEntity originAirport ;
    
    @OneToOne(cascade={CascadeType.PERSIST})
    private AirportEntity destinationAirport;
    private Double revenue;
    private Double cost;
    private Integer distance;    
    private Double flightHours;

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
    public Integer getDistance() {
        return distance;
    }

    /**
     * Set the value of distance
     *
     * @param distance new value of distance
     */
    public void setDistance(Integer distance) {
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