/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package emas.web.managedbean.distribution;

import imas.distribution.sessionbean.MakeBookingSessionBeanLocal;
import imas.distribution.sessionbean.SeatHelperClass;
import java.util.ArrayList;
import java.util.List;
import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;

/**
 *
 * @author Howard
 */
@ManagedBean
@ViewScoped
public class WebCheckInManagedBean {
    
    @EJB
    private MakeBookingSessionBeanLocal makeBookingSessionBean;
    
    private String referenceNumber;
    private String passportNumber;
    private List<List<SeatHelperClass>> seatHelper = new ArrayList<>();

    public WebCheckInManagedBean() {
    }

    public List<List<SeatHelperClass>> fetchAllSeats(Long aircraftID, Long flightID, String seatClass) {
        return makeBookingSessionBean.fetchAllSeats(aircraftID, flightID, seatClass);
    }

    public String getReferenceNumber() {
        return referenceNumber;
    }

    public void setReferenceNumber(String referenceNumber) {
        this.referenceNumber = referenceNumber;
    }

    public String getPassportNumber() {
        return passportNumber;
    }

    public void setPassportNumber(String passportNumber) {
        this.passportNumber = passportNumber;
    }

    public List<List<SeatHelperClass>> getSeatHelper() {
        return seatHelper;
    }

    public void setSeatHelper(List<List<SeatHelperClass>> seatHelper) {
        this.seatHelper = seatHelper;
    }

}
