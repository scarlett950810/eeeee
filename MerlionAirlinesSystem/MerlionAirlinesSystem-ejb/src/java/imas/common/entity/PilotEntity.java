/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package imas.common.entity;

import imas.planning.entity.FlightEntity;
import java.io.Serializable;
import java.util.List;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.Table;


@Entity
@Table(name = "pilot")
public class PilotEntity extends StaffEntity implements Serializable {

    private static final long serialVersionUID = 1L;
    private String workingStatus;//available, checked-in, in flight, unavailable, temp rest
    private String aircraftType;
    @ManyToMany
    private List<FlightEntity> pilotFlights;
    private Boolean mileageLimit; // 5000 --> false; 10000--> true
// change

    public PilotEntity() {

    }

    public PilotEntity(String staffNo, String displayName, String password, String email,
            String contactNumber, String department, String address, String gender, String base,
            String workingStatus, String aircraftType, List<FlightEntity> pilotFlights, Boolean mileageLimit) {
        super(staffNo, displayName, password, email, contactNumber, address, gender);

        this.workingStatus = workingStatus;
        this.aircraftType = aircraftType;
        this.pilotFlights = pilotFlights;
        this.mileageLimit = false;
    }

    // the MountainBike subclass adds one method
    public String getWorkingStatus() {
        return workingStatus;
    }

    public void setWorkingStatus(String workingStatus) {
        this.workingStatus = workingStatus;
    }

    public String getAircraftType() {
        return aircraftType;
    }

    public void setAircraftType(String aircraftType) {
        this.aircraftType = aircraftType;
    }

    public List<FlightEntity> getPilotFlights() {
        return pilotFlights;
    }

    public void setPilotFlights(List<FlightEntity> pilotFlights) {
        this.pilotFlights = pilotFlights;
    }

    public Boolean getMileageLimit() {
        return mileageLimit;
    }

    public void setMileageLimit(Boolean mileageLimit) {
        this.mileageLimit = mileageLimit;
    }

}
