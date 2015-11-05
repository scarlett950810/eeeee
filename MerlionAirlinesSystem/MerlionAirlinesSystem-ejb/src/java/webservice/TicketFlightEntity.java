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
public class TicketFlightEntity implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    
    private String flightNo;
    private String departureDate;
    private String arrivalDate;
    private Double price;
    private String bookingClassName;
    private String origin;
    private String destination;
    private String oriAirportName;
    private String desAirportName;
    private String oriAirportCode;
    private String desAirportCode;
    private String aircraftTailN;
    private String depDayWE;
    private String depTimeE;
    private String ariDayWE;
    private String ariTimeE;
    private String timeDuration;

    public String getOrigin() {
        return origin;
    }

    public String getTimeDuration() {
        return timeDuration;
    }

    public void setTimeDuration(String timeDuration) {
        this.timeDuration = timeDuration;
    }

    public void setOrigin(String origin) {
        this.origin = origin;
    }

    public String getAircraftTailN() {
        return aircraftTailN;
    }

    public String getDesAirportName() {
        return desAirportName;
    }

    public void setDesAirportName(String desAirportName) {
        this.desAirportName = desAirportName;
    }

    public String getDesAirportCode() {
        return desAirportCode;
    }

    public void setDesAirportCode(String desAirportCode) {
        this.desAirportCode = desAirportCode;
    }

    public String getDepDayWE() {
        return depDayWE;
    }

    public void setDepDayWE(String depDayWE) {
        this.depDayWE = depDayWE;
    }

    public String getDepTimeE() {
        return depTimeE;
    }

    public void setDepTimeE(String depTimeE) {
        this.depTimeE = depTimeE;
    }

    public String getAriDayWE() {
        return ariDayWE;
    }

    public void setAriDayWE(String ariDayWE) {
        this.ariDayWE = ariDayWE;
    }

    

    public String getAriTimeE() {
        return ariTimeE;
    }

    public void setAriTimeE(String ariTimeE) {
        this.ariTimeE = ariTimeE;
    }

    public void setAircraftTailN(String aircraftTailN) {
        this.aircraftTailN = aircraftTailN;
    }

    public String getDestination() {
        return destination;
    }

    public void setDestination(String destination) {
        this.destination = destination;
    }

    public String getOriAirportName() {
        return oriAirportName;
    }

    public void setOriAirportName(String oriAirportName) {
        this.oriAirportName = oriAirportName;
    }


    public String getOriAirportCode() {
        return oriAirportCode;
    }

    public void setOriAirportCode(String oriAirportCode) {
        this.oriAirportCode = oriAirportCode;
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

    public String getFlightNo() {
        return flightNo;
    }

    public void setFlightNo(String flightNo) {
        this.flightNo = flightNo;
    }

    public String getDepartureDate() {
        return departureDate;
    }

    public void setDepartureDate(String departureDate) {
        this.departureDate = departureDate;
    }

    public String getArrivalDate() {
        return arrivalDate;
    }

    public void setArrivalDate(String arrivalDate) {
        this.arrivalDate = arrivalDate;
    }

    public Double getPrice() {
        return price;
    }

    public void setPrice(Double price) {
        this.price = price;
    }

    public String getBookingClassName() {
        return bookingClassName;
    }

    public void setBookingClassName(String bookingClassName) {
        this.bookingClassName = bookingClassName;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof TicketFlightEntity)) {
            return false;
        }
        TicketFlightEntity other = (TicketFlightEntity) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "webservice.TicketFlightEntity[ id=" + id + " ]";
    }
    
}
