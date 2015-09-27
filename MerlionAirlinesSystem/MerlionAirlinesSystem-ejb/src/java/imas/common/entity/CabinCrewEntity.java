/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package imas.common.entity;

import imas.planning.entity.FlightEntity;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.Table;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlTransient;

/**
 *
 * @author Lei
 */
@Entity
@Table(name = "CabinCrew")
public class CabinCrewEntity extends StaffEntity implements Serializable {

    private static final long serialVersionUID = 1L;
    private String workingStatus;//available, checked-in, in flight, unavailable, temp rest
    @ManyToMany
    private List<FlightEntity> cabinCrewFligths;

//change
    public CabinCrewEntity() {

    }

    public CabinCrewEntity(String staffNo, String displayName, String password,
            String email, String contactNumber, String address,
            String gender, String workingStatus, List<FlightEntity> cabinCrewFlights) {
        super(staffNo, displayName, password, email, contactNumber, address, gender);
      
//        this.setStaffNo(staffNo);
//        this.setDisplayName(displayName);
//        this.setPassword(password);
//        this.setEmail(email);
//        this.setContactNumber(contactNumber);
//        this.setDepartemnt(department);
//        this.setAddress(address);
//        this.setGender(gender);
//        this.setBase(base);
        this.workingStatus = workingStatus;
        this.cabinCrewFligths = new ArrayList<>();
    }

    // the MountainBike subclass adds one method
    public String getWorkingStatus() {
        return workingStatus;
    }

    public void setWorkingStatus(String workingStatus) {
        this.workingStatus = workingStatus;
    }

    public List<FlightEntity> getCabinCrewFlights() {
        return cabinCrewFligths;
    }

    public void setCabinCrewFlights(List<FlightEntity> cabinCrewFlights) {
        this.cabinCrewFligths = cabinCrewFlights;
    }

}
