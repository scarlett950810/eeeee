/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package imas.planning.entity;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;

/**
 *
 * @author Howard
 */
@Entity
public class AirportEntity implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private Boolean hubOrSpoke;
    private String cityName;
    private String airportName;  
    private String airportCode;

  /*@OneToMany(cascade = {CascadeType.PERSIST}, mappedBy = "airportHub")
    private List<AircraftEntity> AirportsH = new ArrayList<>();
    @OneToMany(cascade = {CascadeType.PERSIST}, mappedBy = "airportLocation")
    private List<AircraftEntity> AirportsLct = new ArrayList<>();*/
        
    public AirportEntity(){   
    }

    public AirportEntity(Boolean hubOrSpoke, String cityName, String airportName, String airportCode) {
        this.hubOrSpoke = hubOrSpoke;
        this.cityName = cityName;
        this.airportName = airportName;
        this.airportCode = airportCode;
    }
    
//    public List<AircraftEntity> getAirportsH() {
//        return AirportsH;
//    }
//
//    public void setAirportsH(List<AircraftEntity> AirportsH) {
//        this.AirportsH = AirportsH;
//    }

    // aircraft location
//    public List<AircraftEntity> getAirportsLct() {
//        return AirportsLct;
//    }
//
//    public void setAirportsLct(List<AircraftEntity> AirportsLct) {
//        this.AirportsLct = AirportsLct;
//    }

    public String getAirportCode() {
        return airportCode;
    }

    public void setAirportCode(String airportCode) {
        this.airportCode = airportCode;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    /**
     * Get the value of airportName
     *
     * @return the value of airportName
     */
    public String getAirportName() {
        return airportName;
    }

    /**
     * Set the value of airportName
     *
     * @param airportName new value of airportName
     */
    public void setAirportName(String airportName) {
        this.airportName = airportName;
    }

    /**
     * Get the value of cityName
     *
     * @return the value of cityName
     */
    public String getCityName() {
        return cityName;
    }

    /**
     * Set the value of cityName
     *
     * @param cityName new value of cityName
     */
    public void setCityName(String cityName) {
        this.cityName = cityName;
    }

    /**
     * Get the value of hubOrSpoke
     *
     * @return the value of hubOrSpoke
     */
    public Boolean isHubOrSpoke() {
        return hubOrSpoke;
    }

    /**
     * Set the value of hubOrSpoke
     *
     * @param hubOrSpoke new value of hubOrSpoke
     */
    public void setHubOrSpoke(Boolean hubOrSpoke) {
        this.hubOrSpoke = hubOrSpoke;
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
        if (!(object instanceof AirportEntity)) {
            return false;
        }
        AirportEntity other = (AirportEntity) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "imas.common.entity.AirportEntity[ id=" + id + " ]";
    }
    
}
