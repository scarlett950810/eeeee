/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package emas.web.managedbean.distribution;

import imas.distribution.entity.PNREntity;
import imas.distribution.sessionbean.ModifyBookingSessionBeanLocal;
import imas.planning.entity.FlightEntity;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;
import javax.servlet.http.HttpServletRequest;

/**
 *
 * @author Howard
 */
@ManagedBean
@SessionScoped
public class ModifyBookingManagedBean {
    @EJB
    private ModifyBookingSessionBeanLocal modifyBookingSessionBean;

    private String referenceNumber;
    private PNREntity PNR;
    
    /**
     * Creates a new instance of ModifyBookingManagedBean
     */
    public ModifyBookingManagedBean() {
    }
    
    @PostConstruct
    public void init() {
        referenceNumber = ((HttpServletRequest)FacesContext.getCurrentInstance().getExternalContext().getRequest()).getParameter("referenceNumber");
        System.out.print(referenceNumber);
        PNR = modifyBookingSessionBean.retrievePNRRecord(referenceNumber);
        
        
    }

    public String getReferenceNumber() {
        return referenceNumber;
    }

    public void setReferenceNumber(String referenceNumber) {
        this.referenceNumber = referenceNumber;
    }

    public PNREntity getPNR() {
        return PNR;
    }

    public void setPNR(PNREntity PNR) {
        this.PNR = PNR;
    }
    
    
}
