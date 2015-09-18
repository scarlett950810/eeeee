/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package imas.inventory.entity;

import imas.planning.entity.FlightEntity;
import imas.planning.entity.RouteEntity;
import java.io.Serializable;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;

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
    @ManyToOne
    private FlightEntity flight;
    private Integer number;
//    private Integer timeToDepartureInDaysMoreThanParameter;
//    private Integer timeToDepartureInDaysLessThanParameter;
//    private Double totalRevenueMoreThanTotalCostParameter;
//    private Double totalRevenueLessThanTotalCostParameter;
//    private Integer increaseEconomyClass1Parameter;
//    private Integer decreaseEconomyClass1Parameter;
//    private Integer increaseEconomyClass2Parameter;
//    private Integer decreaseEconomyClass2Parameter;
//    private Integer increaseEconomyClass3Parameter;
//    private Integer decreaseEconomyClass3Parameter;
//    private Integer increaseEconomyClass4Parameter;
//    private Integer decreaseEconomyClass4Parameter;
//    private Integer increaseEconomyClass5Parameter;
//    private Integer decreaseEconomyClass5Parameter;
    private Integer timeToDepartureInDaysParameter;
    private Double totalRevenueToTotalCostParameter;
    private Integer changeEconomyClass1Parameter;
    private Integer changeEconomyClass2Parameter;
    private Integer changeEconomyClass3Parameter;
    private Integer changeEconomyClass4Parameter;
    private Integer changeEconomyClass5Parameter;
    private Integer percentageSoldParameter;
    private Integer EconomyClass1RemainingQuotaParameter;
    private Integer EconomyClass2RemainingQuotaParameter;
    private Integer EconomyClass3RemainingQuotaParameter;
    private Integer EconomyClass4RemainingQuotaParameter;
    private Integer EconomyClass5RemainingQuotaParameter;

    public YieldManagementRuleEntity() {
    }

    public YieldManagementRuleEntity YieldManagementRule1Entity(FlightEntity flight, 
            Integer timeToDepartureInDaysParameter, Double totalRevenueToTotalCostParameter, 
            Integer changeEconomyClass1Parameter, Integer changeEconomyClass2Parameter,
            Integer changeEconomyClass3Parameter, Integer changeEconomyClass4Parameter) {
        
        YieldManagementRuleEntity yieldManagementRuleEntity = new YieldManagementRuleEntity();        
        yieldManagementRuleEntity.flight = flight;
        yieldManagementRuleEntity.number = 1;
        yieldManagementRuleEntity.timeToDepartureInDaysParameter = timeToDepartureInDaysParameter;
        yieldManagementRuleEntity.totalRevenueToTotalCostParameter = totalRevenueToTotalCostParameter;
        yieldManagementRuleEntity.changeEconomyClass1Parameter = changeEconomyClass1Parameter;
        yieldManagementRuleEntity.changeEconomyClass2Parameter = changeEconomyClass2Parameter;
        yieldManagementRuleEntity.changeEconomyClass3Parameter = changeEconomyClass3Parameter;
        yieldManagementRuleEntity.changeEconomyClass4Parameter = changeEconomyClass4Parameter;
        
        return yieldManagementRuleEntity;
    }
    
    public YieldManagementRuleEntity YieldManagementRule2Entity(FlightEntity flight, 
            Integer timeToDepartureInDaysParameter, Double totalRevenueToTotalCostParameter, 
            Integer changeEconomyClass2Parameter, Integer changeEconomyClass3Parameter, 
            Integer changeEconomyClass4Parameter, Integer changeEconomyClass5Parameter) {
        
        YieldManagementRuleEntity yieldManagementRuleEntity = new YieldManagementRuleEntity();        
        yieldManagementRuleEntity.flight = flight;
        yieldManagementRuleEntity.number = 2;
        yieldManagementRuleEntity.timeToDepartureInDaysParameter = timeToDepartureInDaysParameter;
        yieldManagementRuleEntity.totalRevenueToTotalCostParameter = totalRevenueToTotalCostParameter;
        yieldManagementRuleEntity.changeEconomyClass2Parameter = changeEconomyClass2Parameter;
        yieldManagementRuleEntity.changeEconomyClass3Parameter = changeEconomyClass3Parameter;
        yieldManagementRuleEntity.changeEconomyClass4Parameter = changeEconomyClass4Parameter;
        yieldManagementRuleEntity.changeEconomyClass5Parameter = changeEconomyClass5Parameter;
        
        return yieldManagementRuleEntity;
    }

    public YieldManagementRuleEntity YieldManagementRule3Entity(FlightEntity flight, 
            Integer timeToDepartureInDaysParameter, Integer changeEconomyClass1Parameter, 
            Integer changeEconomyClass2Parameter, Integer changeEconomyClass3Parameter, 
            Integer changeEconomyClass4Parameter, Integer changeEconomyClass5Parameter, Integer percentageSoldParameter) {
        
        YieldManagementRuleEntity yieldManagementRuleEntity = new YieldManagementRuleEntity();        
        yieldManagementRuleEntity.flight = flight;
        yieldManagementRuleEntity.number = 3;
        yieldManagementRuleEntity.timeToDepartureInDaysParameter = timeToDepartureInDaysParameter;
        yieldManagementRuleEntity.changeEconomyClass1Parameter = changeEconomyClass1Parameter;
        yieldManagementRuleEntity.changeEconomyClass2Parameter = changeEconomyClass2Parameter;
        yieldManagementRuleEntity.changeEconomyClass3Parameter = changeEconomyClass3Parameter;
        yieldManagementRuleEntity.changeEconomyClass4Parameter = changeEconomyClass4Parameter;
        yieldManagementRuleEntity.changeEconomyClass5Parameter = changeEconomyClass5Parameter;
        yieldManagementRuleEntity.percentageSoldParameter = percentageSoldParameter;
        
        return yieldManagementRuleEntity;
    }

    public YieldManagementRuleEntity YieldManagementRule4Entity(FlightEntity flight, Integer timeToDepartureInDaysParameter, 
            Integer changeEconomyClass1Parameter, Integer changeEconomyClass2Parameter, Integer changeEconomyClass3Parameter, 
            Integer changeEconomyClass4Parameter, Integer changeEconomyClass5Parameter, Integer EconomyClass1RemainingQuotaParameter, 
            Integer EconomyClass2RemainingQuotaParameter, Integer EconomyClass3RemainingQuotaParameter, 
            Integer EconomyClass4RemainingQuotaParameter, Integer EconomyClass5RemainingQuotaParameter) {
        
        YieldManagementRuleEntity yieldManagementRuleEntity = new YieldManagementRuleEntity();        
        yieldManagementRuleEntity.flight = flight;
        yieldManagementRuleEntity.number = 4;
        yieldManagementRuleEntity.timeToDepartureInDaysParameter = timeToDepartureInDaysParameter;
        yieldManagementRuleEntity.changeEconomyClass1Parameter = changeEconomyClass1Parameter;
        yieldManagementRuleEntity.changeEconomyClass2Parameter = changeEconomyClass2Parameter;
        yieldManagementRuleEntity.changeEconomyClass3Parameter = changeEconomyClass3Parameter;
        yieldManagementRuleEntity.changeEconomyClass4Parameter = changeEconomyClass4Parameter;
        yieldManagementRuleEntity.changeEconomyClass5Parameter = changeEconomyClass5Parameter;
        yieldManagementRuleEntity.EconomyClass1RemainingQuotaParameter = EconomyClass1RemainingQuotaParameter;
        yieldManagementRuleEntity.EconomyClass2RemainingQuotaParameter = EconomyClass2RemainingQuotaParameter;
        yieldManagementRuleEntity.EconomyClass3RemainingQuotaParameter = EconomyClass3RemainingQuotaParameter;
        yieldManagementRuleEntity.EconomyClass4RemainingQuotaParameter = EconomyClass4RemainingQuotaParameter;
        yieldManagementRuleEntity.EconomyClass5RemainingQuotaParameter = EconomyClass5RemainingQuotaParameter;
        
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

    public Integer getChangeEconomyClass1Parameter() {
        return changeEconomyClass1Parameter;
    }

    public void setChangeEconomyClass1Parameter(Integer changeEconomyClass1Parameter) {
        this.changeEconomyClass1Parameter = changeEconomyClass1Parameter;
    }

    public Integer getChangeEconomyClass2Parameter() {
        return changeEconomyClass2Parameter;
    }

    public void setChangeEconomyClass2Parameter(Integer changeEconomyClass2Parameter) {
        this.changeEconomyClass2Parameter = changeEconomyClass2Parameter;
    }

    public Integer getChangeEconomyClass3Parameter() {
        return changeEconomyClass3Parameter;
    }

    public void setChangeEconomyClass3Parameter(Integer changeEconomyClass3Parameter) {
        this.changeEconomyClass3Parameter = changeEconomyClass3Parameter;
    }

    public Integer getChangeEconomyClass4Parameter() {
        return changeEconomyClass4Parameter;
    }

    public void setChangeEconomyClass4Parameter(Integer changeEconomyClass4Parameter) {
        this.changeEconomyClass4Parameter = changeEconomyClass4Parameter;
    }

    public Integer getChangeEconomyClass5Parameter() {
        return changeEconomyClass5Parameter;
    }

    public void setChangeEconomyClass5Parameter(Integer changeEconomyClass5Parameter) {
        this.changeEconomyClass5Parameter = changeEconomyClass5Parameter;
    }

    public Integer getPercentageSoldParameter() {
        return percentageSoldParameter;
    }

    public void setPercentageSoldParameter(Integer percentageSoldParameter) {
        this.percentageSoldParameter = percentageSoldParameter;
    }

    public Integer getEconomyClass1RemainingQuotaParameter() {
        return EconomyClass1RemainingQuotaParameter;
    }

    public void setEconomyClass1RemainingQuotaParameter(Integer EconomyClass1RemainingQuotaParameter) {
        this.EconomyClass1RemainingQuotaParameter = EconomyClass1RemainingQuotaParameter;
    }

    public Integer getEconomyClass2RemainingQuotaParameter() {
        return EconomyClass2RemainingQuotaParameter;
    }

    public void setEconomyClass2RemainingQuotaParameter(Integer EconomyClass2RemainingQuotaParameter) {
        this.EconomyClass2RemainingQuotaParameter = EconomyClass2RemainingQuotaParameter;
    }

    public Integer getEconomyClass3RemainingQuotaParameter() {
        return EconomyClass3RemainingQuotaParameter;
    }

    public void setEconomyClass3RemainingQuotaParameter(Integer EconomyClass3RemainingQuotaParameter) {
        this.EconomyClass3RemainingQuotaParameter = EconomyClass3RemainingQuotaParameter;
    }

    public Integer getEconomyClass4RemainingQuotaParameter() {
        return EconomyClass4RemainingQuotaParameter;
    }

    public void setEconomyClass4RemainingQuotaParameter(Integer EconomyClass4RemainingQuotaParameter) {
        this.EconomyClass4RemainingQuotaParameter = EconomyClass4RemainingQuotaParameter;
    }

    public Integer getEconomyClass5RemainingQuotaParameter() {
        return EconomyClass5RemainingQuotaParameter;
    }

    public void setEconomyClass5RemainingQuotaParameter(Integer EconomyClass5RemainingQuotaParameter) {
        this.EconomyClass5RemainingQuotaParameter = EconomyClass5RemainingQuotaParameter;
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
