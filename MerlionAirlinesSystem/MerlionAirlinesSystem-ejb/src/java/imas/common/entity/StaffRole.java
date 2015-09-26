/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package imas.common.entity;

import java.io.Serializable;
import java.util.ArrayList;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

/**
 *
 * @author Howard
 */
@Entity
public class StaffRole implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    
    private String businessUnit;
    
    private String Position;
    
    private String division;
    
    private String location;

    private ArrayList<String> accessRight;

    public StaffRole(String businessUnit, String Position, String division, String location, ArrayList<String> accessRight) {
        this.businessUnit = businessUnit;
        this.Position = Position;
        this.division = division;
        this.location = location;
        this.accessRight = accessRight;
    }

    public StaffRole() {
    }

    
    /**
     * Get the value of division
     *
     * @return the value of division
     */
    public String getDivision() {
        return division;
    }

    /**
     * Set the value of division
     *
     * @param division new value of division
     */
    public void setDivision(String division) {
        this.division = division;
    }


    /**
     * Get the value of Position
     *
     * @return the value of Position
     */
    public String getPosition() {
        return Position;
    }

    /**
     * Set the value of Position
     *
     * @param Position new value of Position
     */
    public void setPosition(String Position) {
        this.Position = Position;
    }


    /**
     * Get the value of businessUnit
     *
     * @return the value of businessUnit
     */
    public String getBusinessUnit() {
        return businessUnit;
    }

    /**
     * Set the value of businessUnit
     *
     * @param businessUnit new value of businessUnit
     */
    public void setBusinessUnit(String businessUnit) {
        this.businessUnit = businessUnit;
    }

    public ArrayList<String> getAccessRight() {
        return accessRight;
    }

    public void setAccessRight(ArrayList<String> accessRight) {
        this.accessRight = accessRight;
    }
    
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
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
        if (!(object instanceof StaffRole)) {
            return false;
        }
        StaffRole other = (StaffRole) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "imas.common.entity.StaffRole[ id=" + id + " ]";
    }
    
}
