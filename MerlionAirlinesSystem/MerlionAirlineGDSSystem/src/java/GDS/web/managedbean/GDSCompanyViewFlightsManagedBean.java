/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package GDS.web.managedbean;

import GDS.entity.GDSBookingClassEntity;
import GDS.entity.GDSCompanyEntity;
import GDS.entity.GDSFlightEntity;
import GDS.sessionbean.GDSCompanySessionBeanLocal;
import GDS.sessionbean.GDSFlightSessionBeanLocal;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import org.primefaces.context.RequestContext;

/**
 *
 * @author Scarlett
 */
@ManagedBean
@ViewScoped
public class GDSCompanyViewFlightsManagedBean implements Serializable {

    @EJB
    private GDSCompanySessionBeanLocal gDSCompanySessionBean;

    @EJB
    private GDSFlightSessionBeanLocal gDSFlightSessionBean;


    private GDSCompanyEntity company;
    private List<GDSFlightEntity> companyFlights;
    private GDSFlightEntity selectedFlight;
    private List<GDSBookingClassEntity> companyFlightsBookingClasses;

    public GDSCompanyViewFlightsManagedBean() {
    }
    
    @PostConstruct
    public void init() {
        getLoggedinCompany();
        companyFlights = gDSFlightSessionBean.getGDSCompanyFlights(company);
    }
    
    public void viewFlightBookingClasses(GDSFlightEntity flight) {
        selectedFlight = flight;
        companyFlightsBookingClasses = selectedFlight.getGDSBookingClassEntities();
        RequestContext.getCurrentInstance().execute("PF('companyFlightBookingClasses').show();");
    }

    private void getLoggedinCompany() {
        if (FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("account") != null) {
            String account = (String) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("account");
            company = gDSCompanySessionBean.getCompany(account);
        }
    }
    
    public void showBCAdditionalNotes(GDSBookingClassEntity bc) {
        FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_INFO, bc.getName(), bc.getNotes());
        RequestContext.getCurrentInstance().showMessageInDialog(message);
    }

    public GDSCompanyEntity getCompany() {
        return company;
    }

    public void setCompany(GDSCompanyEntity company) {
        this.company = company;
    }
        
    public List<GDSFlightEntity> getCompanyFlights() {
        return companyFlights;
    }

    public void setCompanyFlights(List<GDSFlightEntity> companyFlights) {
        this.companyFlights = companyFlights;
    }

    public List<GDSBookingClassEntity> getCompanyFlightsBookingClasses() {
        return companyFlightsBookingClasses;
    }

    public void setCompanyFlightsBookingClasses(List<GDSBookingClassEntity> companyFlightsBookingClasses) {
        this.companyFlightsBookingClasses = companyFlightsBookingClasses;
    }

    public GDSFlightEntity getSelectedFlight() {
        return selectedFlight;
    }

    public void setSelectedFlight(GDSFlightEntity selectedFlight) {
        this.selectedFlight = selectedFlight;
    }
    
}
