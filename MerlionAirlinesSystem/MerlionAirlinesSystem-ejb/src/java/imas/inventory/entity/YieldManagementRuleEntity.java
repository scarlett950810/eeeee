/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package imas.inventory.entity;

import imas.planning.entity.FlightEntity;
import java.io.Serializable;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToOne;

/**
 *
 * @author Scarlett
 */
@Entity
public class YieldManagementRuleEntity implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    @OneToOne
    private FlightEntity flight;
    private Integer number;
    private Integer timeToDepartureInDaysParameter;
    private Double totalRevenueToTotalCostParameter;
    private Double percentageSoldParameter;
    private Integer economyClass1RemainingQuotaParameter;
    private Integer economyClass2RemainingQuotaParameter;
    private Double changeEconomyClass3and4and5To1Or2PercentageParameter;

    private boolean enabled;

    public YieldManagementRuleEntity() {
    }

    public YieldManagementRuleEntity YieldManagementRule1Entity(FlightEntity flight,
            Integer timeToDepartureInDaysParameter, Double totalRevenueToTotalCostParameter) {

        YieldManagementRuleEntity yieldManagementRuleEntity = new YieldManagementRuleEntity();
        yieldManagementRuleEntity.flight = flight;
        yieldManagementRuleEntity.number = 1;
        yieldManagementRuleEntity.timeToDepartureInDaysParameter = timeToDepartureInDaysParameter;
        yieldManagementRuleEntity.totalRevenueToTotalCostParameter = totalRevenueToTotalCostParameter;
        yieldManagementRuleEntity.enabled = true;
      
        return yieldManagementRuleEntity;
    }

    public YieldManagementRuleEntity YieldManagementRule2Entity(FlightEntity flight,
            Integer timeToDepartureInDaysParameter, Double totalRevenueToTotalCostParameter) {

        YieldManagementRuleEntity yieldManagementRuleEntity = new YieldManagementRuleEntity();
        yieldManagementRuleEntity.flight = flight;
        yieldManagementRuleEntity.number = 2;
        yieldManagementRuleEntity.timeToDepartureInDaysParameter = timeToDepartureInDaysParameter;
        yieldManagementRuleEntity.totalRevenueToTotalCostParameter = totalRevenueToTotalCostParameter;
        yieldManagementRuleEntity.enabled = true;
        
        return yieldManagementRuleEntity;
    }

    public YieldManagementRuleEntity YieldManagementRule3Entity(FlightEntity flight,
            Integer timeToDepartureInDaysParameter, Double percentageSoldParameter) {

        YieldManagementRuleEntity yieldManagementRuleEntity = new YieldManagementRuleEntity();
        yieldManagementRuleEntity.flight = flight;
        yieldManagementRuleEntity.number = 3;
        yieldManagementRuleEntity.timeToDepartureInDaysParameter = timeToDepartureInDaysParameter;
        yieldManagementRuleEntity.percentageSoldParameter = percentageSoldParameter;
        yieldManagementRuleEntity.enabled = true;
        
        return yieldManagementRuleEntity;
    }

    public YieldManagementRuleEntity YieldManagementRule4Entity(FlightEntity flight, 
            Integer timeToDepartureInDaysParameter,
            Integer economyClass1RemainingQuotaParameter, Integer economyClass2RemainingQuotaParameter, 
            Double changeEconomyClass3and4and5To1Or2PercentageParameter) {

        YieldManagementRuleEntity yieldManagementRuleEntity = new YieldManagementRuleEntity();
        yieldManagementRuleEntity.flight = flight;
        yieldManagementRuleEntity.number = 4;
        yieldManagementRuleEntity.timeToDepartureInDaysParameter = timeToDepartureInDaysParameter;
        yieldManagementRuleEntity.economyClass1RemainingQuotaParameter = economyClass1RemainingQuotaParameter;
        yieldManagementRuleEntity.economyClass2RemainingQuotaParameter = economyClass2RemainingQuotaParameter;
        yieldManagementRuleEntity.changeEconomyClass3and4and5To1Or2PercentageParameter = changeEconomyClass3and4and5To1Or2PercentageParameter;
        yieldManagementRuleEntity.enabled = true;

        return yieldManagementRuleEntity;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public FlightEntity getFlight() {
        return flight;
    }

    public void setFlight(FlightEntity flight) {
        this.flight = flight;
    }

    public Integer getNumber() {
        return number;
    }

    public void setNumber(Integer number) {
        this.number = number;
    }

    public Integer getTimeToDepartureInDaysParameter() {
        return timeToDepartureInDaysParameter;
    }

    public void setTimeToDepartureInDaysParameter(Integer timeToDepartureInDaysParameter) {
        this.timeToDepartureInDaysParameter = timeToDepartureInDaysParameter;
    }

    public Double getTotalRevenueToTotalCostParameter() {
        return totalRevenueToTotalCostParameter;
    }

    public void setTotalRevenueToTotalCostParameter(Double totalRevenueToTotalCostParameter) {
        this.totalRevenueToTotalCostParameter = totalRevenueToTotalCostParameter;
    }

    public Double getChangeEconomyClass3and4and5To1Or2PercentageParameter() {
        return changeEconomyClass3and4and5To1Or2PercentageParameter;
    }

    public void setChangeEconomyClass3and4and5To1Or2PercentageParameter(Double changeEconomyClass3and4and5To1Or2PercentageParameter) {
        this.changeEconomyClass3and4and5To1Or2PercentageParameter = changeEconomyClass3and4and5To1Or2PercentageParameter;
    }

    public Double getPercentageSoldParameter() {
        return percentageSoldParameter;
    }

    public void setPercentageSoldParameter(Double percentageSoldParameter) {
        this.percentageSoldParameter = percentageSoldParameter;
    }

    public Integer getEconomyClass1RemainingQuotaParameter() {
        return economyClass1RemainingQuotaParameter;
    }

    public void setEconomyClass1RemainingQuotaParameter(Integer economyClass1RemainingQuotaParameter) {
        this.economyClass1RemainingQuotaParameter = economyClass1RemainingQuotaParameter;
    }

    public Integer getEconomyClass2RemainingQuotaParameter() {
        return economyClass2RemainingQuotaParameter;
    }

    public void setEconomyClass2RemainingQuotaParameter(Integer economyClass2RemainingQuotaParameter) {
        this.economyClass2RemainingQuotaParameter = economyClass2RemainingQuotaParameter;
    }

    public boolean isEnabled() {
        return enabled;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }
    
    public boolean isRule1() {
        return number == 1;
    }
    
    public boolean isRule2() {
        return number == 2;
    }
    
    public boolean isRule3() {
        return number == 3;
    }
    
    public boolean isRule4() {
        return number == 4;
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
        if (!(object instanceof YieldManagementRuleEntity)) {
            return false;
        }
        YieldManagementRuleEntity other = (YieldManagementRuleEntity) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "imas.inventory.entity.YieldManagementRuleEntity[ id=" + id + " ]";
    }

}
