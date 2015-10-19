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
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;

/**
 *
 * @author Scarlett
 */
@Entity
public class BookingClassEntity implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
   
    @ManyToOne
    private FlightEntity flight;
    
    private String seatClass; // can only be one of: First Class, Business Class, Premium Economy Class, Economy Class

    private String name; // booking class Name. 
                         // Can only be one of: 
                         // First Class, Business Class, Premium Economy Class,
                         // Full Service Economy, Economy Plus, Standard Economy, Economy Save, Economy Super Save
                         // Economy Class Agency
                         
    private Double price;
    
    private Integer quota;
    
    private boolean isAgencyBookingClass;
    @OneToOne
    private BookingClassRuleSetEntity bookingClassRuleSet;

    public BookingClassEntity() {
    }

    public BookingClassEntity(FlightEntity flight, String seatClass, String name, double price, int quota) {
        this.flight = flight;
        this.seatClass = seatClass;
        this.name = name;
        this.price = price;
        this.quota = quota;
    }
    
    public BookingClassEntity(FlightEntity flight, String seatClass, String name, Double price, Integer quota, boolean isAgencyBookingClass) {
        this.flight = flight;
        this.seatClass = seatClass;
        this.name = name;
        this.price = price;
        this.quota = quota;
        this.isAgencyBookingClass = isAgencyBookingClass;
    }
    
    public BookingClassEntity FirstClassBookingClassEntity(FlightEntity flight, Double price, Integer quota) {
        return new BookingClassEntity(flight, "First Class", "First Class", price, quota, false);       
    }
    
    public BookingClassEntity BusinessClassBookingClassEntity(FlightEntity flight, Double price, Integer quota) {
        return new BookingClassEntity(flight, "Business Class", "Business Class", price, quota, false);       
    }
    
    public BookingClassEntity PremiumEconomyClassBookingClassEntity(FlightEntity flight, Double price, Integer quota) {
        return new BookingClassEntity(flight, "Premium Economy Class", "Premium Economy Class", price, quota, false);       
    }
    
//    in total 5 economy booking classes with price 1 > 2 > 3 > 4 > 5.
//    1 and 2 are higher willingness to pay (WTP) classes while 3, 4, 5 are lower WTP classes
    
    public BookingClassEntity EconomyClass1BookingClassEntity(FlightEntity flight, Double price, Integer quota) {
        return new BookingClassEntity(flight, "Economy Class", "Full Service Economy", price, quota, false);       
    }
    
    public BookingClassEntity EconomyClass2BookingClassEntity(FlightEntity flight, Double price, Integer quota) {
        return new BookingClassEntity(flight, "Economy Class", "Economy Plus", price, quota, false);       
    }
    
    public BookingClassEntity EconomyClass3BookingClassEntity(FlightEntity flight, Double price, Integer quota) {
        return new BookingClassEntity(flight, "Economy Class", "Standard Economy", price, quota, false);       
    }
    
    public BookingClassEntity EconomyClass4BookingClassEntity(FlightEntity flight, Double price, Integer quota) {
        return new BookingClassEntity(flight, "Economy Class", "Economy Save", price, quota, false);       
    }
    
    public BookingClassEntity EconomyClass5BookingClassEntity(FlightEntity flight, Double price, Integer quota) {
        return new BookingClassEntity(flight, "Economy Class", "Economy Super Save", price, quota, false);       
    }
    
    // agency booking classes
    public BookingClassEntity FirstClassAgencyBookingClassEntity(FlightEntity flight, Double price, Integer quota) {
        return new BookingClassEntity(flight, "First Class", "First Class Agency", price, quota, true);       
    }
    
    public BookingClassEntity BusinessClassAgencyBookingClassEntity(FlightEntity flight, Double price, Integer quota) {
        return new BookingClassEntity(flight, "Business Class", "Business Class Agency", price, quota, true);       
    }
    
    public BookingClassEntity PremiumEconomyClassAgencyBookingClassEntity(FlightEntity flight, Double price, Integer quota) {
        return new BookingClassEntity(flight, "Premium Economy Class", "Premium Economy Class Agency", price, quota, true);       
    }
    
    public BookingClassEntity EconomyClassAgencyBookingClassEntity(FlightEntity flight, Double price, Integer quota) {
        return new BookingClassEntity(flight, "Economy Class", "Economy Class Agency", price, quota, true);       
    }
    
    public boolean isFirstClassBookingClassEntity() {
        return "First Class".equals(this.name);
    }
    
    public boolean isBusinessClassBookingClassEntity() {
        return "Business Class".equals(this.name);
    }
    
    public boolean isPremiumEconomyClassBookingClassEntity() {
        return "Premium Economy Class".equals(this.name);
    }
    
    public boolean isEconomyClass1BookingClassEntity() {
        return "Full Service Economy".equals(this.name);
    }
    
    public boolean isEconomyClass2BookingClassEntity() {
        return "Economy Plus".equals(this.name);
    }
    
    public boolean isEconomyClass3BookingClassEntity() {
        return "Standard Economy".equals(this.name);
    }
    
    public boolean isEconomyClass4BookingClassEntity() {
        return "Economy Save".equals(this.name);
    }
    
    public boolean isEconomyClass5BookingClassEntity() {
        return "Economy Super Save".equals(this.name);
    }
    
    public boolean isFirstClassAgencyBookingClassEntity() {
        return "First Class Agency".equals(this.name);
    }
    
    public boolean isBusinessClassAgencyBookingClassEntity() {
        return "Business Class Agency".equals(this.name);
    }
    
    public boolean isPremiumEconomyClassAgencyBookingClassEntity() {
        return "Premium Economy Class Agency".equals(this.name);
    }
    
    public boolean isEconomyClassAgencyBookingClassEntity() {
        return "Economy Class Agency".equals(this.name);
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

    public String getSeatClass() {
        return seatClass;
    }

    public void setSeatClass(String seatClass) {
        this.seatClass = seatClass;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Double getPrice() {
        return price;
    }

    public void setPrice(Double price) {
        this.price = price;
    }

    public Integer getQuota() {
        return quota;
    }

    public void setQuota(Integer quota) {
        this.quota = quota;
    }    

    public boolean isIsAgencyBookingClass() {
        return isAgencyBookingClass;
    }

    public void setIsAgencyBookingClass(boolean isAgencyBookingClass) {
        this.isAgencyBookingClass = isAgencyBookingClass;
    }

    public BookingClassRuleSetEntity getBookingClassRuleSet() {
        return bookingClassRuleSet;
    }

    public void setBookingClassRuleSet(BookingClassRuleSetEntity bookingClassRuleSet) {
        this.bookingClassRuleSet = bookingClassRuleSet;
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
        if (!(object instanceof BookingClassEntity)) {
            return false;
        }
        BookingClassEntity other = (BookingClassEntity) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "imas.inventory.entity.bookingClassEntity[ id=" + id + " ]";
    }
    
}
