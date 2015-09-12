/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package imas.planning.entity;

import java.io.Serializable;
import java.util.List;
import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;

/**
 *
 * @author Lei
 */
@Entity
public class FlightEntity implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private String flightNo;
    private Double distance;
    private Long duration;
    private boolean departured;
    //booking
    //ticket

    //private AircraftEntity aircraft;
    @ManyToOne(cascade = {CascadeType.PERSIST})
    private AircraftEntity aircraft;
    
    //route
    @ManyToOne(cascade = {CascadeType.PERSIST})
    private RouteEntity route;

    // private FlightRecordEntity flightRecord;
    @OneToMany(cascade = {CascadeType.PERSIST}, mappedBy = "flightRecords")
    private List<FlightRecordEntity> flightRecords;

    public FlightEntity() {

    }

    public FlightEntity(Long id, String flightNo, Double distance, Long duration, AircraftEntity aircraft, RouteEntity route) {
        this.id = id;
        this.flightNo = flightNo;
        this.distance = distance;
        this.duration = duration;
        this.aircraft = aircraft;
        this.route = route;
        this.departured = false;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getFlightNo() {
        return flightNo;
    }

    public void setFlightNo(String flightNo) {
        this.flightNo = flightNo;
    }

    public RouteEntity getRoute() {
        return route;
    }

    public void setRoute(RouteEntity route) {
        this.route = route;
    }

    public Double getDistance() {
        return distance;
    }

    public void setDistance(Double distance) {
        this.distance = distance;
    }

    public Long getDuration() {
        return duration;
    }

    public void setDuration(Long duration) {
        this.duration = duration;
    }

    public List<FlightRecordEntity> getFlightRecords() {
        return flightRecords;
    }

    public void setFlightRecords(List<FlightRecordEntity> flightRecords) {
        this.flightRecords = flightRecords;
    }

    public AircraftEntity getAircraft() {
        return aircraft;
    }

    public void setAircraftFlight(AircraftEntity aircraft) {
        this.aircraft = aircraft;
    }

    public boolean isDepartured() {
        return departured;
    }

    public void setDepartured(boolean departured) {
        this.departured = departured;
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
        if (!(object instanceof FlightEntity)) {
            return false;
        }
        FlightEntity other = (FlightEntity) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "imas.planning.entity.FlightEntity[ id=" + id + " ]";
    }

}
