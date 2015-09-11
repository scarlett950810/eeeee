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
                         //Can only be one of: First Class, Business Class, Premium Economy Class, Economy Class 1, Economy Class 2, Economy Class 3
    private Double price;
    
    private int quota;

    public BookingClassEntity() {
    }

    public BookingClassEntity(FlightEntity flight, String seatClass, String name, Double price, int quota) {
        this.flight = flight;
        this.seatClass = seatClass;
        this.name = name;
        this.price = price;
        this.quota = quota;
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
