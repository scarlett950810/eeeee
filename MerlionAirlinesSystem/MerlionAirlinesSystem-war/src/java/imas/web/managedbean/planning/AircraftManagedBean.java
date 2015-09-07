/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package imas.web.managedbean.planning;

import imas.planning.entity.AircraftEntity;
import imas.planning.entity.AircraftGroupEntity;
import imas.planning.entity.AircraftTypeEntity;
import imas.planning.entity.AirportEntity;
import imas.planning.sessionbean.AircraftSessionBeanLocal;
import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;
import javax.faces.view.ViewScoped;
import javax.inject.Named;
import javax.persistence.PostRemove;
import org.primefaces.event.RowEditEvent;

/**
 *
 * @author Scarlett
 */
@Named(value = "aircraftManagedBean")
@ViewScoped
public class AircraftManagedBean implements Serializable {

    @EJB
    private AircraftSessionBeanLocal aircraftSessionBean;

    private String tailId;
    private AircraftTypeEntity aircraftType;
    private List<AircraftTypeEntity> aircraftTypes; // list of Aircrafts to choose from
    private Double purchasePrice;
    private Double deprecation;
    private Double netAssetValue;
    private Double aircraftLife;
    private Double operationYear;
    private String conditionDescription;
    private AirportEntity airportHub;
    private List<AirportEntity> airports; // list of Airports to choose from
    private AirportEntity currentAirport;
    private AircraftGroupEntity aircraftGroup;
    private List<AircraftGroupEntity> aircraftGroups; // list of aircraftGroups to choose from
    private int FirstClassColumnNo;
    private int BusinessClassColumnNo;
    private int PremiumEconomyClassColumnNo;
    private int EconomyClassColumnNo;
    private int FirstClassRowNo;
    private int BusinessClassRowNo;
    private int PremiumEconomyClassRowNo;
    private int EconomyClassRowNo;
    
    private List<AircraftEntity> aircrafts;
  
    @PostConstruct
    public void init()
    {
        FacesContext.getCurrentInstance().getExternalContext().getSessionMap().put("aircraftTypes", this.getAircraftTypes());
        FacesContext.getCurrentInstance().getExternalContext().getSessionMap().put("aircraftGroups", this.getAircraftGroups());
        FacesContext.getCurrentInstance().getExternalContext().getSessionMap().put("airportList", this.getAirports());
    }
    
    @PostRemove
    public void destroy()
    {
        FacesContext.getCurrentInstance().getExternalContext().getSessionMap().remove("aircraftTypes");
        FacesContext.getCurrentInstance().getExternalContext().getSessionMap().remove("aircraftGroups");
        FacesContext.getCurrentInstance().getExternalContext().getSessionMap().remove("airportList");
    }

    public AircraftManagedBean() {
        this.aircraftTypes = new ArrayList();
    }

    public AircraftSessionBeanLocal getAircraftSessionBean() {
        return aircraftSessionBean;
    }

    public void setAircraftSessionBean(AircraftSessionBeanLocal aircraftSessionBean) {
        this.aircraftSessionBean = aircraftSessionBean;
    }

    public String getTailId() {
        return tailId;
    }

    public void setTailId(String tailId) {
        this.tailId = tailId;
    }

    public AircraftTypeEntity getAircraftType() {
        return aircraftType;
    }

    public void setAircraftType(AircraftTypeEntity aircraftType) {
        this.aircraftType = aircraftType;
    }

    public List<AircraftTypeEntity> getAircraftTypes() {
        return aircraftSessionBean.getAircraftTypes();
    }

    public void setAircraftTypes(List<AircraftTypeEntity> aircraftTypes) {
        this.aircraftTypes = aircraftTypes;
    }

    public Double getPurchasePrice() {
        return purchasePrice;
    }

    public void setPurchasePrice(Double purchasePrice) {
        this.purchasePrice = purchasePrice;
    }

    public Double getDeprecation() {
        return deprecation;
    }

    public void setDeprecation(Double deprecation) {
        this.deprecation = deprecation;
    }

    public Double getNetAssetValue() {
        return netAssetValue;
    }

    public void setNetAssetValue(Double netAssetValue) {
        this.netAssetValue = netAssetValue;
    }

    public Double getAircraftLife() {
        return aircraftLife;
    }

    public void setAircraftLife(Double aircraftLife) {
        this.aircraftLife = aircraftLife;
    }

    public Double getOperationYear() {
        return operationYear;
    }

    public void setOperationYear(Double operationYear) {
        this.operationYear = operationYear;
    }

    public String getConditionDescription() {
        return conditionDescription;
    }

    public void setConditionDescription(String conditionDescription) {
        this.conditionDescription = conditionDescription;
    }

    public AirportEntity getAirportHub() {
        return airportHub;
    }

    public void setAirportHub(AirportEntity airportHub) {
        this.airportHub = airportHub;
    }

    public List<AirportEntity> getAirports() {
//        System.out.print("aircraftManagedBean.getAirports called.");
        return aircraftSessionBean.getAirports();
    }

    public void setAirports(List<AirportEntity> airports) {
        this.airports = airports;
    }

    public AirportEntity getCurrentAirport() {
        return currentAirport;
    }

    public void setCurrentAirport(AirportEntity currentAirport) {
        this.currentAirport = currentAirport;
    }

    public AircraftGroupEntity getAircraftGroup() {
        return aircraftGroup;
    }

    public void setAircraftGroup(AircraftGroupEntity aircraftGroup) {
        this.aircraftGroup = aircraftGroup;
    }

    public List<AircraftGroupEntity> getAircraftGroups() {
//        System.out.print("AircraftManagedBean.getAircraftGroups called.");
        return aircraftSessionBean.getAircraftGroups();
    }

    public void setAircraftGroups(List<AircraftGroupEntity> aircraftGroups) {
        this.aircraftGroups = aircraftGroups;
    }

    public int getFirstClassColumnNo() {
        return FirstClassColumnNo;
    }

    public void setFirstClassColumnNo(int FirstClassColumnNo) {
        this.FirstClassColumnNo = FirstClassColumnNo;
    }

    public int getBusinessClassColumnNo() {
        return BusinessClassColumnNo;
    }

    public void setBusinessClassColumnNo(int BusinessClassColumnNo) {
        this.BusinessClassColumnNo = BusinessClassColumnNo;
    }

    public int getPremiumEconomyClassColumnNo() {
        return PremiumEconomyClassColumnNo;
    }

    public void setPremiumEconomyClassColumnNo(int PremiumEconomyClassColumnNo) {
        this.PremiumEconomyClassColumnNo = PremiumEconomyClassColumnNo;
    }

    public int getEconomyClassColumnNo() {
        return EconomyClassColumnNo;
    }

    public void setEconomyClassColumnNo(int EconomyClassColumnNo) {
        this.EconomyClassColumnNo = EconomyClassColumnNo;
    }

    public int getFirstClassRowNo() {
        return FirstClassRowNo;
    }

    public void setFirstClassRowNo(int FirstClassRowNo) {
        this.FirstClassRowNo = FirstClassRowNo;
    }

    public int getBusinessClassRowNo() {
        return BusinessClassRowNo;
    }

    public void setBusinessClassRowNo(int BusinessClassRowNo) {
        this.BusinessClassRowNo = BusinessClassRowNo;
    }

    public int getPremiumEconomyClassRowNo() {
        return PremiumEconomyClassRowNo;
    }

    public void setPremiumEconomyClassRowNo(int PremiumEconomyClassRowNo) {
        this.PremiumEconomyClassRowNo = PremiumEconomyClassRowNo;
    }

    public int getEconomyClassRowNo() {
        return EconomyClassRowNo;
    }

    public void setEconomyClassRowNo(int EconomyClassRowNo) {
        this.EconomyClassRowNo = EconomyClassRowNo;
    }

    public boolean addAircraft(ActionEvent event) throws IOException {
        if (this.tailId == null || this.aircraftType == null || this.purchasePrice == null || 
                this.deprecation == null || this.netAssetValue == null || this.aircraftLife == null || 
                this.operationYear == null || this.conditionDescription == null || this.airportHub == null || 
                this.currentAirport == null) {
            return false;
        } else if (this.FirstClassColumnNo == 0 && this.FirstClassRowNo == 0 && 
                this.BusinessClassColumnNo == 0 && this.BusinessClassRowNo == 0 && 
                this.PremiumEconomyClassColumnNo == 0 && this.PremiumEconomyClassRowNo ==0 && 
                this.EconomyClassColumnNo == 0 && this.EconomyClassRowNo == 0) {
            // to change to glow or message
            System.out.print("No seat added.");
            return false;
        } else if (this.FirstClassColumnNo == 0 && this.FirstClassRowNo != 0 || this.FirstClassColumnNo != 0 && this.FirstClassRowNo == 0) {
            // to change to glow or message
            System.out.print("To indicate No First class seats, enter both 0 for column and row number.");
            return false;
        } else if (this.BusinessClassColumnNo == 0 && this.BusinessClassRowNo != 0 || 
                this.BusinessClassColumnNo != 0 && this.BusinessClassRowNo == 0) {
            // to change to glow or message
            System.out.print("To indicate No Business class seats, enter both 0 for column and row number.");
            return false;
        } else if (this.PremiumEconomyClassColumnNo == 0 && this.PremiumEconomyClassRowNo != 0 || 
                this.PremiumEconomyClassColumnNo != 0 && this.PremiumEconomyClassRowNo == 0) {
            // to change to glow or message
            System.out.print("To indicate No Premium economy class seats, enter both 0 for column and row number.");
            return false;
        } else if (this.EconomyClassColumnNo == 0 && this.EconomyClassRowNo != 0 || 
                this.EconomyClassColumnNo != 0 && this.EconomyClassRowNo == 0) {
            // to change to glow or message
            System.out.print("To indicate No economy class seats, enter both 0 for column and row number.");
            return false;
        } else {
            return aircraftSessionBean.addAircraft(this.tailId, this.aircraftType, this.purchasePrice, this.deprecation, this.netAssetValue, 
                    this.aircraftLife, this.operationYear, this.conditionDescription, this.airportHub, this.currentAirport, this.aircraftGroup, 
                    this.FirstClassColumnNo, this.FirstClassRowNo, this.BusinessClassColumnNo, this.BusinessClassRowNo, 
                    this.PremiumEconomyClassColumnNo, this.PremiumEconomyClassRowNo, this.EconomyClassColumnNo, this.EconomyClassRowNo);
        }

    }

    public List<AircraftEntity> getAircrafts() {
        return aircraftSessionBean.getAircrafts();
    }

    public void setAircrafts(List<AircraftEntity> aircrafts) {
        this.aircrafts = aircrafts;
    }
    
    public void onRowEdit(RowEditEvent event) {
        
        System.out.println("row:");
        System.out.println(event.getSource().toString());
        FacesMessage msg = new FacesMessage("Aircraft Edited", "");
        FacesContext.getCurrentInstance().addMessage(null, msg);
        
    }
     
//    public void onRowCancel(RowEditEvent event) {
//        FacesMessage msg = new FacesMessage("Edit Cancelled", "");
//        FacesContext.getCurrentInstance().addMessage(null, msg);
//    }
    
    public void onAircraftDelete(AircraftEntity aircraft) {
        aircraftSessionBean.deleteAircraft(aircraft);
    }

}