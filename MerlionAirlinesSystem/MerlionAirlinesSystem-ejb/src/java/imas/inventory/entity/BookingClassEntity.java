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
                         
    private double price;
    
    private int quota;

    public BookingClassEntity() {
    }

    public BookingClassEntity(FlightEntity flight, String seatClass, String name, double price, int quota) {
        this.flight = flight;
        this.seatClass = seatClass;
        this.name = name;
        this.price = price;
        this.quota = quota;
    }
    
    public BookingClassEntity FirstClassBookingClassEntity(FlightEntity flight, double price, int quota) {
        return new BookingClassEntity(flight, "First Class", "First Class", price, quota);       
    }
    
    public BookingClassEntity BusinessClassBookingClassEntity(FlightEntity flight, double price, int quota) {
        return new BookingClassEntity(flight, "Business Class", "Business Class", price, quota);       
    }
    
    public BookingClassEntity PremiumEconomyClassBookingClassEntity(FlightEntity flight, double price, int quota) {
        return new BookingClassEntity(flight, "Premium Economy Class", "Premium Economy Class", price, quota);       
    }
    
//    in total 5 economy booking classes with price 1 > 2 > 3 > 4 > 5.
//    1 and 2 are higher willingness to pay (WTP) classes while 3, 4, 5 are lower WTP classes
    
    public BookingClassEntity EconomyClass1BookingClassEntity(FlightEntity flight, double price, int quota) {
        return new BookingClassEntity(flight, "Economy Class", "Full Service Economy", price, quota);       
    }
    
    public BookingClassEntity EconomyClass2BookingClassEntity(FlightEntity flight, double price, int quota) {
        return new BookingClassEntity(flight, "Economy Class", "Economy Plus", price, quota);       
    }
    
    public BookingClassEntity EconomyClass3BookingClassEntity(FlightEntity flight, double price, int quota) {
        return new BookingClassEntity(flight, "Economy Class", "Standard Economy", price, quota);       
    }
    
    public BookingClassEntity EconomyClass4BookingClassEntity(FlightEntity flight, double price, int quota) {
        return new BookingClassEntity(flight, "Economy Class", "Economy Save", price, quota);       
    }
    
    public BookingClassEntity EconomyClass5BookingClassEntity(FlightEntity flight, double price, int quota) {
        return new BookingClassEntity(flight, "Economy Class", "Economy Super Save", price, quota);       
    }
    
    public BookingClassEntity EconomyClassAgencyBookingClassEntity(FlightEntity flight, double price, int quota) {
        return new BookingClassEntity(flight, "Economy Class", "Economy Class Agency", price, quota);       
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

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public int getQuota() {
        return quota;
    }

    public void setQuota(int quota) {
        this.quota = quota;
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
