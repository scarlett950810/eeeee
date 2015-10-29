/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package imas.web.managedbean.systemAdmin;

import imas.common.entity.StaffEntity;
import imas.common.sessionbean.AccountManagementSessionBeanLocal;
import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.faces.model.SelectItem;
import javax.faces.model.SelectItemGroup;
import javax.faces.view.ViewScoped;

/**
 *
 * @author Howard
 */
@ManagedBean
@ViewScoped
public class AddStaffManagedBean implements Serializable {

    @EJB
    private AccountManagementSessionBeanLocal accountManagementSessionBean;

    private String staffNo;
    private String name;
    private String email;
    private String contactNumber;
    private String address;
    private String gender;

    private String unit;
    private String businessUnit;
    private String division;
    private String position;
    private String location;

    private List<SelectItem> businessUnits;
    List<String> Division;

    private StaffEntity staff;
    private List<String> aircraftType;

    private String base;
    private List<String> bases;
    private boolean mileageLimit;
    private boolean isPilot;
    private boolean isCabinCrew;

    @PostConstruct
    public void init() {

        fetchBase();

        SelectItemGroup g1 = new SelectItemGroup("Administration");
        g1.setSelectItems(new SelectItem[]{new SelectItem("Administration,Human Resources", "Human Resources"), new SelectItem("Administration,Information Technology", "Information Technology"), new SelectItem("Administration,Finance", "Finance")});

        SelectItemGroup g2 = new SelectItemGroup("Operation Control");
        g2.setSelectItems(new SelectItem[]{new SelectItem("Operation Control,Ground Crew", "Ground Crew"), new SelectItem("Operation Control,Luggage Management", "Luggage Management")});

        SelectItemGroup g3 = new SelectItemGroup("Sales and Marketing");
        g3.setSelectItems(new SelectItem[]{new SelectItem("Sales and Marketing,Sales", "Sales"), new SelectItem("Sales and Marketing,Branding", "Branding"), new SelectItem("Sales and Marketing,Membership Management", "Membership Management"), new SelectItem("Sales and Marketing,Complementary Service", "Complementary Service")});

        SelectItemGroup g4 = new SelectItemGroup("Maintenance");
        g4.setSelectItems(new SelectItem[]{new SelectItem("Maintenance,Aircraft Maintenance", "Aircraft Maintenance")});

        SelectItemGroup g5 = new SelectItemGroup("Operation");
        g5.setSelectItems(new SelectItem[]{new SelectItem("Operation,Crew Management", "Crew Management"), new SelectItem("Operation,Cockpit Crew", "Cockpit Crew"), new SelectItem("Operation,Cabin Crew", "Cabin Crew"), new SelectItem("Operation,Planning", "Planning")});

        businessUnits = new ArrayList<>();
        businessUnits.add(g1);
        businessUnits.add(g2);
        businessUnits.add(g3);
        businessUnits.add(g4);
        businessUnits.add(g5);
    }

    public AddStaffManagedBean() {
    }

    public void addStaff() throws IOException {
        String[] temp = unit.split(",");
        businessUnit = temp[0];
        division = temp[1];
        if (division.equals("Cockpit Crew")) {
            System.out.print("this is a pilot");
            isPilot = true;
            isCabinCrew = false;
        } else if (division.equals("Cabin Crew")) {
            isPilot = false;
            isCabinCrew = true;
        } else {
            isPilot = false;
            isCabinCrew = false;
        }

        if (division.equals("Cabin Crew") || division.equals("Cockpit Crew") || businessUnit.equals("Operation Control")) {
            
            if (base == null) {
                
                FacesMessage msg = new FacesMessage("Warning", businessUnit + ", " + division + " must have a base.");
                FacesContext.getCurrentInstance().addMessage(null, msg);
            } else {

                if (accountManagementSessionBean.addStaff(staffNo, name, email, contactNumber, address, gender, businessUnit, division, position, location, base, "available", aircraftType, mileageLimit, isPilot, isCabinCrew)) {

                    FacesContext fc = FacesContext.getCurrentInstance();
                    ExternalContext ec = fc.getExternalContext();
                    ec.redirect("systemAdminHome.xhtml");
                } else {
                    FacesMessage msg = new FacesMessage("Warning", name + " is already in the system");
                    FacesContext.getCurrentInstance().addMessage(null, msg);
                }
            }
        } else {
            if (accountManagementSessionBean.addStaff(staffNo, name, email, contactNumber, address, gender, businessUnit, division, position, location, base, "available", aircraftType, mileageLimit, isPilot, isCabinCrew)) {

                FacesContext fc = FacesContext.getCurrentInstance();
                ExternalContext ec = fc.getExternalContext();
                ec.redirect("systemAdminHome.xhtml");
            } else {
                FacesMessage msg = new FacesMessage("Warning", "This staff No or this email is already in the system");
                FacesContext.getCurrentInstance().addMessage(null, msg);
            }
        }

        staffNo = null;
        name = null;
        email = null;
        contactNumber = null;
        address = null;
        gender = null;
        position = null;
        location = null;
        base = null;
    }

    public void fetchBase() {
        bases = accountManagementSessionBean.fetchBases();
    }

    public void onPilotChange() {
        String[] temp = unit.split(",");
        this.setBusinessUnit(temp[0]);
        this.setDivision(temp[1]);
        if (division.equals("Cockpit Crew")) {
            System.out.print("this is a pilot");
            isPilot = true;
            isCabinCrew = false;
        } else if (division.equals("Cabin Crew")) {
            isPilot = false;
            isCabinCrew = true;
        } else {
            isPilot = false;
            isCabinCrew = false;
        }
    }

    public String getStaffNo() {
        return staffNo;
    }

    public void setStaffNo(String staffNo) {
        this.staffNo = staffNo;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getContactNumber() {
        return contactNumber;
    }

    public void setContactNumber(String contactNumber) {
        this.contactNumber = contactNumber;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getUnit() {
        return unit;
    }

    public void setUnit(String unit) {
        this.unit = unit;
    }

    public String getBusinessUnit() {
        return businessUnit;
    }

    public void setBusinessUnit(String businessUnit) {
        this.businessUnit = businessUnit;
    }

    public String getDivision() {
        return division;
    }

    public void setDivision(String division) {
        this.division = division;
    }

    public String getPosition() {
        return position;
    }

    public void setPosition(String position) {
        this.position = position;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public List<SelectItem> getBusinessUnits() {
        return businessUnits;
    }

    public void setBusinessUnits(List<SelectItem> businessUnits) {
        this.businessUnits = businessUnits;
    }

    public void setDivision(List<String> Division) {
        this.Division = Division;
    }

    public StaffEntity getStaff() {
        return staff;
    }

    public void setStaff(StaffEntity staff) {
        this.staff = staff;
    }

    public List<String> getAircraftType() {
        return aircraftType;
    }

    public void setAircraftType(List<String> aircraftType) {
        this.aircraftType = aircraftType;
    }

    public String getBase() {
        return base;
    }

    public void setBase(String base) {
        this.base = base;
    }

    public List<String> getBases() {
        return bases;
    }

    public void setBases(List<String> bases) {
        this.bases = bases;
    }

    public boolean isMileageLimit() {
        return mileageLimit;
    }

    public void setMileageLimit(boolean mileageLimit) {
        this.mileageLimit = mileageLimit;
    }

    public boolean isIsPilot() {
        return isPilot;
    }

    public void setIsPilot(boolean isPilot) {
        this.isPilot = isPilot;
    }

    public boolean isIsCabinCrew() {
        return isCabinCrew;
    }

    public void setIsCabinCrew(boolean isCabinCrew) {
        this.isCabinCrew = isCabinCrew;
    }

}
