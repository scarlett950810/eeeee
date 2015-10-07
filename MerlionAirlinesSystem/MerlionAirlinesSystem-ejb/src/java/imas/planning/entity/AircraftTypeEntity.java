/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package imas.planning.entity;

import java.io.Serializable;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

/**
 *
 * @author Scarlett
 */
@Entity
public class AircraftTypeEntity implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private String IATACode;
    private Double aircraftRange;//km
    private Integer aircraftSpace;//
    private Double cruisingSpeed;//miles/
    private Double wingSpan;//ft
    private Double aircraftWeight;//tonnes
    private Double aircraftLength;//ft
    private Double aircraftHeight;//ft
    private String powerPlant;
    private Double MaintenanceHoursRequiredACheck;
    //typical cruising speed

    public AircraftTypeEntity() {
    }

    public AircraftTypeEntity(String IATACode, Double aircraftRange, Integer aircraftSpace, 
            Double cruisingSpeed, Double wingSpan, Double aircraftWeight, Double aircraftLength, 
            Double aircraftHeight, String powerPlant, Double MaintenanceHoursRequiredACheck) {
        this.IATACode = IATACode;
        this.aircraftRange = aircraftRange;
        this.aircraftSpace = aircraftSpace;
        this.cruisingSpeed = cruisingSpeed;
        this.wingSpan = wingSpan;
        this.aircraftWeight = aircraftWeight;
        this.aircraftLength = aircraftLength;
        this.aircraftHeight = aircraftHeight;
        this.powerPlant = powerPlant;
        this.MaintenanceHoursRequiredACheck = MaintenanceHoursRequiredACheck;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Double getMaintenanceHoursRequiredACheck() {
        return MaintenanceHoursRequiredACheck;
    }

    public void setMaintenanceHoursRequiredACheck(Double MaintenanceHoursRequiredACheck) {
        this.MaintenanceHoursRequiredACheck = MaintenanceHoursRequiredACheck;
    }

    public String getIATACode() {
        return IATACode;
    }

    public void setIATACode(String IATACode) {
        this.IATACode = IATACode;
    }

    public Double getAircraftRange() {
        return aircraftRange;
    }

    public void setAircraftRange(Double aircraftRange) {
        this.aircraftRange = aircraftRange;
    }

    public Integer getAircraftSpace() {
        return aircraftSpace;
    }

    public void setAircraftSpace(Integer aircraftSpace) {
        this.aircraftSpace = aircraftSpace;
    }

    public Double getCruisingSpeed() {
        return cruisingSpeed;
    }

    public void setCruisingSpeed(Double cruisingSpeed) {
        this.cruisingSpeed = cruisingSpeed;
    }

    public Double getWingSpan() {
        return wingSpan;
    }

    public void setWingSpan(Double wingSpan) {
        this.wingSpan = wingSpan;
    }

    public Double getAircraftWeight() {
        return aircraftWeight;
    }

    public void setAircraftWeight(Double aircraftWeight) {
        this.aircraftWeight = aircraftWeight;
    }

    public Double getAircraftLength() {
        return aircraftLength;
    }

    public void setAircraftLength(Double aircraftLength) {
        this.aircraftLength = aircraftLength;
    }

    public Double getAircraftHeight() {
        return aircraftHeight;
    }

    public void setAircraftHeight(Double aircraftHeight) {
        this.aircraftHeight = aircraftHeight;
    }

    public String getPowerPlant() {
        return powerPlant;
    }

    public void setPowerPlant(String powerPlant) {
        this.powerPlant = powerPlant;
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
        if (!(object instanceof AircraftTypeEntity)) {
            return false;
        }
        AircraftTypeEntity other = (AircraftTypeEntity) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return IATACode;
    }

}
