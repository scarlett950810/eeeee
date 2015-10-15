/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package imas.distribution.sessionbean;

import java.util.Objects;

/**
 *
 * @author Howard
 */
public class SeatHelperClass {
    
    private String seatNumber;
    private Boolean occupied;
    private Boolean eligible;
    private Boolean selected;

    public SeatHelperClass() {
        this.selected = false;
    }

    public SeatHelperClass(String seatNumber, Boolean eligible) {
        this.seatNumber = seatNumber;
        this.eligible = eligible;
        this.selected = false;
    }

    public String getSeatNumber() {
        return seatNumber;
    }

    public void setSeatNumber(String seatNumber) {
        this.seatNumber = seatNumber;
    }

    public Boolean getOccupied() {
        return occupied;
    }

    public void setOccupied(Boolean occupied) {
        this.occupied = occupied;
    }

    public Boolean getEligible() {
        return eligible;
    }

    public void setEligible(Boolean eligible) {
        this.eligible = eligible;
    }

    public Boolean getSelected() {
        return selected;
    }

    public void setSelected(Boolean selected) {
        this.selected = selected;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final SeatHelperClass other = (SeatHelperClass) obj;
        if (!Objects.equals(this.seatNumber, other.seatNumber)) {
            return false;
        }
        return true;
    }
    
    
}
