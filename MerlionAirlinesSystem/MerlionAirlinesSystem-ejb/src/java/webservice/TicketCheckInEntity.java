/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package webservice;

import java.io.Serializable;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

/**
 *
 * @author ruicai
 */
@XmlRootElement
@XmlType
@Entity
public class TicketCheckInEntity implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private String originCity;
    private String destinationCity; 
    private String originAN;
    private String destinationAN;
    private String flightNo; 
    private Long flightId;
    private String depD;
    private String ariD;

    public String getOriginCity() {
        return originCity;
    }

    public String getDepD() {
        return depD;
    }

    public void setDepD(String depD) {
        this.depD = depD;
    }

    public String getAriD() {
        return ariD;
    }

    public void setAriD(String ariD) {
        this.ariD = ariD;
    }

    public Long getFlightId() {
        return flightId;
    }

    public void setFlightId(Long flightId) {
        this.flightId = flightId;
    }

    public void setOriginCity(String originCity) {
        this.originCity = originCity;
    }

    public String getDestinationCity() {
        return destinationCity;
    }

    public void setDestinationCity(String destinationCity) {
        this.destinationCity = destinationCity;
    }

    public String getOriginAN() {
        return originAN;
    }

    public void setOriginAN(String originAN) {
        this.originAN = originAN;
    }

    public String getDestinationAN() {
        return destinationAN;
    }

    public void setDestinationAN(String destinationAN) {
        this.destinationAN = destinationAN;
    }

    public String getFlightNo() {
        return flightNo;
    }

    public void setFlightNo(String flightNo) {
        this.flightNo = flightNo;
    }
    
    
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
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
        if (!(object instanceof TicketCheckInEntity)) {
            return false;
        }
        TicketCheckInEntity other = (TicketCheckInEntity) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "webservice.TicketCheckInEntity[ id=" + id + " ]";
    }
    
}
