/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package imas.web.managedbean.planning;

import imas.planning.entity.AircraftTypeEntity;
import imas.planning.entity.AirportEntity;
import imas.planning.sessionbean.AircraftTypeSessionBeanLocal;
import java.io.IOException;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.inject.Named;
import javax.enterprise.context.Dependent;
import javax.faces.application.FacesMessage;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import org.primefaces.event.RowEditEvent;

/**
 *
 * @author wutong
 */
@Named(value = "aircraftTypeManagedBean")
@Dependent
public class AircraftTypeManagedBean {
    
    @EJB
    private AircraftTypeSessionBeanLocal aircraftTypeSession;
    
    
    private List<AircraftTypeEntity> aircraftTypes;
    private String IATACode;
    private Double aircraftRange;//km
    private Integer aircraftSpace;//
    private Double cruisingSpeed;//miles/
    private Double wingSpan;//ft
    private Double aircraftWeight;//tonnes
    private Double aircraftLength;//ft
    private Double aircraftHeight;//ft
    private String powerPlant;
    private Double MaintenanceHoursRequiredACheck;

    public String getIATACode() {
        return IATACode;
    }

    public void setIATACode(String IATACode) {
        this.IATACode = IATACode;
    }

    public Double getAircraftRange() {
        return aircraftRange;
    }

    public void setAircraftRange(Double aircraftRange) {
        this.aircraftRange = aircraftRange;
    }

    public Integer getAircraftSpace() {
        return aircraftSpace;
    }

    public void setAircraftSpace(Integer aircraftSpace) {
        this.aircraftSpace = aircraftSpace;
    }

    public Double getCruisingSpeed() {
        return cruisingSpeed;
    }

    public void setCruisingSpeed(Double cruisingSpeed) {
        this.cruisingSpeed = cruisingSpeed;
    }

    public Double getWingSpan() {
        return wingSpan;
    }

    public void setWingSpan(Double wingSpan) {
        this.wingSpan = wingSpan;
    }

    public Double getAircraftWeight() {
        return aircraftWeight;
    }

    public void setAircraftWeight(Double aircraftWeight) {
        this.aircraftWeight = aircraftWeight;
    }

    public Double getAircraftLength() {
        return aircraftLength;
    }

    public void setAircraftLength(Double aircraftLength) {
        this.aircraftLength = aircraftLength;
    }

    public Double getAircraftHeight() {
        return aircraftHeight;
    }

    public void setAircraftHeight(Double aircraftHeight) {
        this.aircraftHeight = aircraftHeight;
    }

    public String getPowerPlant() {
        return powerPlant;
    }

    public void setPowerPlant(String powerPlant) {
        this.powerPlant = powerPlant;
    }

    public Double getMaintenanceHoursRequiredACheck() {
        return MaintenanceHoursRequiredACheck;
    }

    public void setMaintenanceHoursRequiredACheck(Double MaintenanceHoursRequiredACheck) {
        this.MaintenanceHoursRequiredACheck = MaintenanceHoursRequiredACheck;
    }

    public List<AircraftTypeEntity> getAircraftTypes() {
        return aircraftTypes;
    }

    public void setAircraftTypes(List<AircraftTypeEntity> aircraftTypes) {
        this.aircraftTypes = aircraftTypes;
    }


    /**
     * Creates a new instance of AircraftTypeManagedBean
     */
    public AircraftTypeManagedBean() {
    }

    @PostConstruct
    public void init() {
        aircraftTypes = aircraftTypeSession.getAllAircraftTypes();
    }

    public void save() throws IOException {
        if (IATACode == null || aircraftRange == null || aircraftSpace == null || cruisingSpeed == null || wingSpan == null || aircraftWeight == null || aircraftLength == null || aircraftHeight == null || powerPlant == null || MaintenanceHoursRequiredACheck == null) {
            FacesMessage msg = new FacesMessage("Sorry", "Please finished the required information");
            FacesContext.getCurrentInstance().addMessage(null, msg);
        } else {
            if (aircraftTypeSession.checkAircraftType(IATACode)) {
                AircraftTypeEntity aircraftType = new AircraftTypeEntity(IATACode, aircraftRange, aircraftSpace, cruisingSpeed, wingSpan, aircraftWeight, aircraftLength, aircraftHeight, powerPlant, MaintenanceHoursRequiredACheck);
                aircraftTypeSession.addAircraftType(aircraftType);
                FacesContext fc = FacesContext.getCurrentInstance();
                ExternalContext ec = fc.getExternalContext();
                ec.redirect("planningAirport.xhtml");
            } else {
                FacesMessage msg = new FacesMessage("Sorry", "this aircraft type has already been added!");
                FacesContext.getCurrentInstance().addMessage(null, msg);
            }
        }
    }

    public void onRowEdit(RowEditEvent event) {
        aircraftTypeSession.updateAircraftType(((AircraftTypeEntity) event.getObject()));
        FacesMessage msg = new FacesMessage("Aircraft Type Edited", ((AircraftTypeEntity) event.getObject()).getIATACode());
        FacesContext.getCurrentInstance().addMessage(null, msg);
    }

    public void onRowCancel(RowEditEvent event) {
        FacesMessage msg = new FacesMessage("Edit Cancelled", ((AircraftTypeEntity) event.getObject()).getIATACode());
        FacesContext.getCurrentInstance().addMessage(null, msg);
    }

}
