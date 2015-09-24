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
import javax.persistence.ManyToOne;

/**
 *
 * @author Lei
 */
@Entity
public class SeatEntity implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    @ManyToOne
    private AircraftEntity aircraft;
    private String seatNo;
    private boolean goodCondition;
    private String seatClass; //First Class, Business Class, Premium Economy Class, Economy Class
    
    private boolean firstClass;
    private boolean businessClass;
    private boolean premiumEconomyClass;
    private boolean economyClass;
    

    public SeatEntity() {

    }

    public SeatEntity(AircraftEntity aircraft, String seatNo, String seatClass) {
        this.aircraft = aircraft;
        this.seatNo = seatNo;
        this.goodCondition = true;
        this.seatClass = seatClass;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getSeatNo() {
        return seatNo;
    }

    public void setSeatNo(String seatNo) {
        this.seatNo = seatNo;
    }

    public String getSeatClass() {
        return seatClass;
    }

    public void setSeatClass(String seatClass) {
        this.seatClass = seatClass;
    }
    
    public void setSeatClassToFirstClass() {
        this.seatClass = "First Class";
        this.firstClass = true;
    }
    
    public void setSeatClassToBusinessClass() {
        this.seatClass = "Business Class";
        this.businessClass = true;
    }
    
    public void setSeatClassToPremiumEconomyClass() {
        this.seatClass = "Premium Economy Class";
        this.premiumEconomyClass = true;
    }
    
    public void setSeatClassToEconomyClass() {
        this.seatClass = "Economy Class";
        this.economyClass = true;
    }

    public AircraftEntity getAircraft() {
        return aircraft;
    }

    public void setAircraft(AircraftEntity aircraft) {
        this.aircraft = aircraft;
    }

    public boolean isGoodCondition() {
        return goodCondition;
    }

    public void setGoodCondition(boolean goodCondition) {
        this.goodCondition = goodCondition;
    }

    public void setFirstClass(boolean FirstClass) {
        this.firstClass = FirstClass;
    }

    public void setBusinessClass(boolean BusinessClass) {
        this.businessClass = BusinessClass;
    }

    public void setPremiumEconomyClass(boolean PremiumEconomyClass) {
        this.premiumEconomyClass = PremiumEconomyClass;
    }

    public void setEconomyClass(boolean EconomyClass) {
        this.economyClass = EconomyClass;
    }
    
    public boolean isFirstClass() {
        return "First Class".equals(seatClass);
    }
    
    public boolean isBusinessClass() {
        return "First Class".equals(seatClass);
    }
    
    public boolean isPremiumEconomyClass() {
        return "Premium Economy Class".equals(seatClass);
    }
    
    public boolean isEconomyClass() {
        return "Economy Class".equals(seatClass);
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
        if (!(object instanceof SeatEntity)) {
            return false;
        }
        SeatEntity other = (SeatEntity) object;
        return !((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id)));
    }

    @Override
    public String toString() {
        return "imas.planning.entity.SeatEntity[ id=" + id + " ]";
    }

}
