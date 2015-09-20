/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package imas.planning.entity;

import java.io.Serializable;
import java.util.Date;
import java.util.List;
import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Temporal;

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
    @Temporal(javax.persistence.TemporalType.DATE)
    private Date departureDate;
    @Temporal(javax.persistence.TemporalType.DATE)
    private Date arrivalDate;
    private Double duration;
    @OneToOne(cascade = CascadeType.PERSIST)
    private FlightEntity reverseFlight;
    private Integer operatingYear;
    private String weekDay;
    //booking
    //ticket

    //private AircraftEntity aircraft;
    @ManyToOne
    private AircraftEntity aircraft;
    
    //route
    @ManyToOne
    private RouteEntity route;

    // private FlightRecordEntity flightRecord;
    @OneToMany(mappedBy = "flightRecords")
    private List<FlightRecordEntity> flightRecords;

    public FlightEntity() {

    }
    public FlightEntity(Integer yearSelected) {
        this.operatingYear = yearSelected;
        
    }
    public Integer getOperatingYear() {
        return operatingYear;
    }

    public void setOperatingYear(Integer operatingYear) {
        this.operatingYear = operatingYear;
    }

    public FlightEntity(String flightNo, Double distance, Double duration, AircraftEntity aircraft, RouteEntity route) {
        this.flightNo = flightNo;
        this.distance = distance;
        this.duration = duration;
        this.aircraft = aircraft;
        this.route = route;
    }

    public Long getId() {
        return id;
    }

    public String getWeekDay() {
        return weekDay;
    }

    public void setWeekDay(String weekDay) {
        this.weekDay = weekDay;
    }

    public FlightEntity getReverseFlight() {
        return reverseFlight;
    }

    public void setReverseFlight(FlightEntity reverseFlight) {
        this.reverseFlight = reverseFlight;
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

    public Date getDepartureDate() {
        return departureDate;
    }

    public void setDepartureDate(Date departureDate) {
        this.departureDate = departureDate;
    }

    public Date getArrivalDate() {
        return arrivalDate;
    }

    public void setArrivalDate(Date arrivalDate) {
        this.arrivalDate = arrivalDate;
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

    public Double getDuration() {
        return duration;
    }

    public void setDuration(Double duration) {
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
