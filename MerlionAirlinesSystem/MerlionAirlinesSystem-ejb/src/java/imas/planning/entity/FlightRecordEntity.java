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
import javax.persistence.ManyToOne;

/**
 *
 * @author Lei
 */
@Entity
public class FlightRecordEntity implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private String EORAviolation;
    private String significantMechnicalFailures;//default 
    private String fuelDumping;
    private String illness;
    private String overWeightLanding;
    private String extraRemark;

    @ManyToOne(cascade = {CascadeType.PERSIST})
    private FlightEntity flightRecords;

    public FlightRecordEntity() {

    }

    public FlightRecordEntity(Long id, String EORAviolation, String significantMechnicalFailures, String fuelDumping, String illness, String overWeightLanding, String extraRemark, FlightEntity flightRecords) {
        this.id = id;
        this.EORAviolation = EORAviolation;
        this.significantMechnicalFailures = significantMechnicalFailures;
        this.fuelDumping = fuelDumping;
        this.illness = illness;
        this.overWeightLanding = overWeightLanding;
        this.extraRemark = extraRemark;
        this.flightRecords = flightRecords;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getEORAviolation() {
        return EORAviolation;
    }

    public void setEORAviolation(String EORAviolation) {
        this.EORAviolation = EORAviolation;
    }

    public String getSignificantMechnicalFailures() {
        return significantMechnicalFailures;
    }

    public void setSignificantMechnicalFailures(String significantMechnicalFailures) {
        this.significantMechnicalFailures = significantMechnicalFailures;
    }

    public String getFuelDumping() {
        return fuelDumping;
    }

    public void setFuelDumping(String fuelDumping) {
        this.fuelDumping = fuelDumping;
    }

    public String getIllness() {
        return illness;
    }

    public void setIllness(String illness) {
        this.illness = illness;
    }

    public String getOverWeightLanding() {
        return overWeightLanding;
    }

    public void setOverWeightLanding(String overWeightLanding) {
        this.overWeightLanding = overWeightLanding;
    }

    public String getExtraRemark() {
        return extraRemark;
    }

    public void setExtraRemark(String extraRemark) {
        this.extraRemark = extraRemark;
    }

    public FlightEntity getFlightRecords() {
        return flightRecords;
    }

    public void setFlightRecords(FlightEntity flightRecords) {
        this.flightRecords = flightRecords;
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
        if (!(object instanceof FlightRecordEntity)) {
            return false;
        }
        FlightRecordEntity other = (FlightRecordEntity) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "imas.planning.entity.FlightRecordEntity[ id=" + id + " ]";
    }

}
