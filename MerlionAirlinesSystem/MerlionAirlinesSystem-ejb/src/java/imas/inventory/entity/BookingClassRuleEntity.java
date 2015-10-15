/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package imas.inventory.entity;

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
public class BookingClassRuleEntity implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    
    private String bookingClassName;
    private boolean isAgencyBookingClass;
    
    private Double changeFlightFeeForMoreThan60Days;    // percentage of origin price
    private Double changeFlightFeeForLessThan60Days;    // percentage of origin price
    private Double cancellationRefundForMoreThan60Days; // percentage of origin price
    private Double cancellationRefundForLessThan60Days; // percentage of origin price
    private Double mileageAccrual;                      // times

    public BookingClassRuleEntity() {
    }

    public BookingClassRuleEntity(String bookingClassName, boolean isAgencyBookingClass, Double changeFlightFeeForMoreThan60Days, Double changeFlightFeeForLessThan60Days, 
            Double cancellationRefundForMoreThan60Days, Double cancellationRefundForLessThan60Days, Double mileageAccrual) {
        this.bookingClassName = bookingClassName;
        this.isAgencyBookingClass = isAgencyBookingClass;
        this.changeFlightFeeForMoreThan60Days = changeFlightFeeForMoreThan60Days;
        this.changeFlightFeeForLessThan60Days = changeFlightFeeForLessThan60Days;
        this.cancellationRefundForMoreThan60Days = cancellationRefundForMoreThan60Days;
        this.cancellationRefundForLessThan60Days = cancellationRefundForLessThan60Days;
        this.mileageAccrual = mileageAccrual;
    }
    
    public BookingClassRuleEntity createFirstClassBookingClassRule() {
        return new BookingClassRuleEntity("First Class", false, 0.0, 0.05, 1.0, 0.8, 1.5);
    }
    
    public BookingClassRuleEntity createFirstClassAgencyBookingClassRule() {
        return new BookingClassRuleEntity("First Class Agency", true, 0.0, 0.05, 1.0, 0.8, 1.5);
    }
    
    public BookingClassRuleEntity createBusinessClassBookingClassRule() {
        return new BookingClassRuleEntity("Business Class", false, 0.0, 0.05, 1.0, 0.8, 1.0);
    }
    
    public BookingClassRuleEntity createBusinessClassAgencyBookingClassRule() {
        return new BookingClassRuleEntity("Business Class Agency", true, 0.0, 0.05, 1.0, 0.8, 1.0);
    }
    
    public BookingClassRuleEntity createPremiumEconomyClassBookingClassRule() {
        return new BookingClassRuleEntity("Premium Economy Class", false, 0.05, 0.10, 0.8, 0.6, 0.5);
    }
    
    public BookingClassRuleEntity createPremiumEconomyClassAgencyBookingClassRule() {
        return new BookingClassRuleEntity("Premium Economy Class Agency", true, 0.05, 0.10, 0.8, 0.6, 0.5);
    }
    
    public BookingClassRuleEntity createEconomyClassBookingClass1Rule() {
        return new BookingClassRuleEntity("Economy Class", false, 0.1, 0.2, 0.6, 0.4, 0.0);
    }
    
    public BookingClassRuleEntity createEconomyClassBookingClass2Rule() {
        return new BookingClassRuleEntity("Economy Class", false, 0.2, 0.4, 0.2, 0.2, 0.0);
    }
    
    public BookingClassRuleEntity createEconomyClassBookingClass3Rule() {
        return new BookingClassRuleEntity("Economy Class", false, 0.4, 0.8, 0.0, 0.0, 0.0);
    }
    
    public BookingClassRuleEntity createEconomyClassBookingClass4Rule() {
        return new BookingClassRuleEntity("Economy Class", false, 0.6, 1.0, 0.0, 0.0, 0.0);
    }
    
    public BookingClassRuleEntity createEconomyClassBookingClass5Rule() {
        return new BookingClassRuleEntity("Economy Class", false, 1.0, 0.0, 0.0, 0.0, 0.0);
    }
    
    public BookingClassRuleEntity createEconomyClassAgencyBookingClassRule() {
        return new BookingClassRuleEntity("Economy Class Agency", true, 1.0, 1.0, 0.0, 0.0, 0.0);
    }
    
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getBookingClassName() {
        return bookingClassName;
    }

    public void setBookingClassName(String bookingClassName) {
        this.bookingClassName = bookingClassName;
    }

    public boolean isIsAgencyBookingClass() {
        return isAgencyBookingClass;
    }

    public void setIsAgencyBookingClass(boolean isAgencyBookingClass) {
        this.isAgencyBookingClass = isAgencyBookingClass;
    }

    public Double getChangeFlightFeeForMoreThan60Days() {
        return changeFlightFeeForMoreThan60Days;
    }

    public void setChangeFlightFeeForMoreThan60Days(Double changeFlightFeeForMoreThan60Days) {
        this.changeFlightFeeForMoreThan60Days = changeFlightFeeForMoreThan60Days;
    }

    public Double getChangeFlightFeeForLessThan60Days() {
        return changeFlightFeeForLessThan60Days;
    }

    public void setChangeFlightFeeForLessThan60Days(Double changeFlightFeeForLessThan60Days) {
        this.changeFlightFeeForLessThan60Days = changeFlightFeeForLessThan60Days;
    }

    public Double getCancellationRefundForMoreThan60Days() {
        return cancellationRefundForMoreThan60Days;
    }

    public void setCancellationRefundForMoreThan60Days(Double cancellationRefundForMoreThan60Days) {
        this.cancellationRefundForMoreThan60Days = cancellationRefundForMoreThan60Days;
    }

    public Double getCancellationRefundForLessThan60Days() {
        return cancellationRefundForLessThan60Days;
    }

    public void setCancellationRefundForLessThan60Days(Double cancellationRefundForLessThan60Days) {
        this.cancellationRefundForLessThan60Days = cancellationRefundForLessThan60Days;
    }

    public Double getMileageAccrual() {
        return mileageAccrual;
    }

    public void setMileageAccrual(Double mileageAccrual) {
        this.mileageAccrual = mileageAccrual;
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
        if (!(object instanceof BookingClassRuleEntity)) {
            return false;
        }
        BookingClassRuleEntity other = (BookingClassRuleEntity) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "imas.inventory.entity.BookingClassRuleEntity[ id=" + id + " ]";
    }
    
}
