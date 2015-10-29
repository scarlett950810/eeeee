/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package imas.departure.sessionbean;

import imas.distribution.entity.PassengerEntity;
import java.util.Objects;

/**
 *
 * @author Howard
 */
public class SeatHelperClass {
    
    private String seatNumber;
    private boolean occupied;
    private boolean eligible;
    private boolean selected;
    private PassengerEntity passenger;

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

    public boolean isOccupied() {
        return occupied;
    }

    public void setOccupied(boolean occupied) {
        this.occupied = occupied;
    }

    public boolean isEligible() {
        return eligible;
    }

    public void setEligible(boolean eligible) {
        this.eligible = eligible;
    }

    public boolean isSelected() {
        return selected;
    }

    public void setSelected(boolean selected) {
        this.selected = selected;
    }

    public PassengerEntity getPassenger() {
        return passenger;
    }

    public void setPassenger(PassengerEntity passenger) {
        this.passenger = passenger;
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
