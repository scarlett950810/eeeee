/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package imas.web.managedbean.planning;

import imas.planning.entity.AircraftEntity;
import imas.planning.entity.AircraftTypeEntity;
import imas.planning.entity.AirportEntity;
import imas.planning.sessionbean.AircraftTypeSessionBeanLocal;
import java.io.IOException;
import java.io.Serializable;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.inject.Named;
import javax.faces.application.FacesMessage;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.faces.view.ViewScoped;
import org.primefaces.event.RowEditEvent;

/**
 *
 * @author wutong
 */
@Named(value = "aircraftTypeManagedBean")
@ViewScoped
public class AircraftTypeManagedBean implements Serializable {

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
    private Double maintenanceHoursRequiredACheck;
    private AircraftTypeEntity aircraftType;
    private List<AircraftEntity> aircrafts;

    @PostConstruct
    public void init() {
        aircraftTypes = aircraftTypeSession.getAllAircraftTypes();
        FacesContext.getCurrentInstance().getExternalContext().getSessionMap().put("aircraftTypes", aircraftTypes);
    }

    public AircraftTypeEntity getAircraftType() {
        return aircraftType;
    }

    public void setAircraftType(AircraftTypeEntity aircraftType) {
        this.aircraftType = aircraftType;
    }

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

    public void addAircraftType() throws IOException {
        FacesContext fc = FacesContext.getCurrentInstance();
        ExternalContext ec = fc.getExternalContext();
        ec.redirect("planningAddAircraftType.xhtml");
    }

    public void deleteAircraftType() throws IOException {
        FacesContext fc = FacesContext.getCurrentInstance();
        ExternalContext ec = fc.getExternalContext();
        ec.redirect("planningDeleteAircraftType.xhtml");
    }

    public void actualDeleteAircraftType() throws IOException {
        FacesMessage msg;
        if (aircraftTypeSession.deleteAircraftType(aircraftType.getIATACode())) {
            System.err.println("enter delete type" + aircraftType.getIATACode());
            FacesContext fc = FacesContext.getCurrentInstance();
            ExternalContext ec = fc.getExternalContext();
            ec.redirect("planningAircraftType.xhtml");
        } else {
            System.err.println("fail: enter delete type" + aircraftType.getIATACode());
            msg = new FacesMessage(FacesMessage.SEVERITY_ERROR, "Failed", "Please delete associated aircrafts first");
            FacesContext.getCurrentInstance().addMessage(null, msg);
        }
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
        return maintenanceHoursRequiredACheck;
    }

    public void setMaintenanceHoursRequiredACheck(Double MaintenanceHoursRequiredACheck) {
        this.maintenanceHoursRequiredACheck = MaintenanceHoursRequiredACheck;
    }

    public List<AircraftTypeEntity> getAircraftTypes() {
        return aircraftTypes;
    }

    public void setAircraftTypes(List<AircraftTypeEntity> aircraftTypes) {
        this.aircraftTypes = aircraftTypes;
    }

    public List<AircraftEntity> getAircrafts() {
        return aircrafts;
    }

    public void setAircrafts(List<AircraftEntity> aircrafts) {
        this.aircrafts = aircrafts;
    }

    /**
     * Creates a new instance of AircraftTypeManagedBean
     */
    public AircraftTypeManagedBean() {
    }

    public void save() throws IOException {
        if (IATACode == null || aircraftRange == null || aircraftSpace == null || cruisingSpeed == null || wingSpan == null || aircraftWeight == null || aircraftLength == null || aircraftHeight == null || powerPlant == null || maintenanceHoursRequiredACheck == null) {
//            System.err.println("aircraftRange"+ IATACode);
//            System.err.println(IATACode);
//            System.err.println(aircraftRange);
//            System.err.println(aircraftSpace);
//            System.err.println(cruisingSpeed);
//            System.err.println(wingSpan);
//            System.err.println(aircraftWeight);
//            System.err.println(aircraftLength);
//            System.err.println(aircraftHeight);
//            System.err.println(powerPlant);
//            System.err.println(maintenanceHoursRequiredACheck);

            FacesMessage msg = new FacesMessage("Sorry", "Please finished the required information");
            FacesContext.getCurrentInstance().addMessage(null, msg);
        } else {
            if (aircraftTypeSession.checkAircraftType(IATACode)) {
                AircraftTypeEntity aircraftType = new AircraftTypeEntity(IATACode, aircraftRange, aircraftSpace, cruisingSpeed, wingSpan, aircraftWeight, aircraftLength, aircraftHeight, powerPlant, maintenanceHoursRequiredACheck);
                aircraftTypeSession.addAircraftType(aircraftType);
                FacesContext fc = FacesContext.getCurrentInstance();
                ExternalContext ec = fc.getExternalContext();
                ec.redirect("planningAircraftType.xhtml");
            } else {
                FacesMessage msg = new FacesMessage("Sorry", "this aircraft type has already been added!");
                FacesContext.getCurrentInstance().addMessage(null, msg);
            }
        }
    }

    public void onAircraftTypeChange() {
        if (aircraftType != null) {
            aircrafts = aircraftTypeSession.getAircraftsFromAircraftType(aircraftType);
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
