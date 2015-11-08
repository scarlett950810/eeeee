/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package GDS.entity;

import java.io.Serializable;
import java.util.Date;
import java.util.List;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Temporal;

/**
 *
 * @author Scarlett
 */
@Entity
public class GDSFlightEntity implements Serializable {
    
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    
    @ManyToOne
    private GDSCompanyEntity GDSCompanyEntity;
    private String flightNo;
    @ManyToOne
    private GDSAirportEntity origin;
    @ManyToOne
    private GDSAirportEntity destination;    
    @Temporal(javax.persistence.TemporalType.DATE)
    private Date departureDate;
    @Temporal(javax.persistence.TemporalType.DATE)
    private Date arrivalDate;
    @OneToMany(mappedBy = "GDSflight")
    private List<GDSBookingClassEntity> GDSBookingClassEntitys;
    private String aircraftITATCode;

    public GDSFlightEntity() {
    }

    public GDSFlightEntity(GDSCompanyEntity GDSCompanyEntity, String flightNo, GDSAirportEntity origin, GDSAirportEntity destination, Date departureDate, Date arrivalDate, String aircraftITATCode) {
        this.GDSCompanyEntity = GDSCompanyEntity;
        this.flightNo = flightNo;
        this.origin = origin;
        this.destination = destination;
        this.departureDate = departureDate;
        this.arrivalDate = arrivalDate;
        this.aircraftITATCode = aircraftITATCode;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public List<GDSBookingClassEntity> getGDSBookingClassEntitys() {
        return GDSBookingClassEntitys;
    }

    public void setGDSBookingClassEntitys(List<GDSBookingClassEntity> GDSBookingClassEntitys) {
        this.GDSBookingClassEntitys = GDSBookingClassEntitys;
    }

    public GDSCompanyEntity getGDSCompanyEntity() {
        return GDSCompanyEntity;
    }

    public void setGDSCompanyEntity(GDSCompanyEntity GDSCompanyEntity) {
        this.GDSCompanyEntity = GDSCompanyEntity;
    }

    public GDSAirportEntity getOrigin() {
        return origin;
    }

    public void setOrigin(GDSAirportEntity origin) {
        this.origin = origin;
    }

    public GDSAirportEntity getDestination() {
        return destination;
    }

    public void setDestination(GDSAirportEntity destination) {
        this.destination = destination;
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

    public String getFlightNo() {
        return flightNo;
    }

    public void setFlightNo(String flightNo) {
        this.flightNo = flightNo;
    }

    public String getAircraftITATCode() {
        return aircraftITATCode;
    }

    public void setAircraftITATCode(String aircraftITATCode) {
        this.aircraftITATCode = aircraftITATCode;
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
        if (!(object instanceof GDSFlightEntity)) {
            return false;
        }
        GDSFlightEntity other = (GDSFlightEntity) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "From " + origin + " to " + destination + ", departured at " + departureDate;
    }
    
}
