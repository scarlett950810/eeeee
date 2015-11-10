/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package imas.web.managedbean.admin;

import imas.common.entity.StaffEntity;
import imas.common.sessionbean.AccountManagementSessionBeanLocal;
import java.io.IOException;
import javax.inject.Named;
import java.io.Serializable;
import static java.lang.Boolean.FALSE;
import static java.lang.Boolean.TRUE;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.faces.model.SelectItem;
import javax.faces.model.SelectItemGroup;

/**
 *
 * @author wutong
 */
@ManagedBean
@SessionScoped
public class AdminViewStaffManagedBean implements Serializable {

    @EJB
    private AccountManagementSessionBeanLocal accountManagementSessionBean;

    private String staffNo;
    private String name;
    private String email;
    private String contactNumber;
    private String address;
    private String gender;

//    private String unit;
    private String businessUnit;
    private String division;
    private String position;
    private String location;
    private String base;
    private String unit;

    private List<StaffEntity> staffList;
    private List<StaffEntity> filteredStaff;
    private List<String> divisions;
    private List<String> bases;
    private StaffEntity staff;
    private List<SelectItem> businessUnits;

    private Boolean view = FALSE;
    private Boolean table = TRUE;

    public AdminViewStaffManagedBean() {
    }

    @PostConstruct
    public void init() {
        fetchStaff();
        fetchBase();
        System.out.print(view);
        System.out.print(table);
    }

    public void fetchBase() {
        bases = accountManagementSessionBean.fetchBases();
    }

    public void viewStaffAccount() throws IOException {
//        System.out.print(staff);
//        System.out.print(staff.getDisplayName() + "is viewed.");
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

        view = TRUE;
        table = FALSE;
        System.out.print(staff);
        System.out.print(staff.getRole());

        unit = staff.getRole().getBusinessUnit() + "," + staff.getRole().getDivision();

        FacesContext fc = FacesContext.getCurrentInstance();
        ExternalContext ec = fc.getExternalContext();
        ec.redirect("adminViewStaff.xhtml");
    }

    public void deleteStaffAccount() throws IOException {
        accountManagementSessionBean.deleteStaff(staff.getStaffNo());
        System.out.print(staff.getDisplayName() + "is deleted.");
        fetchStaff();
        view = FALSE;
        table = TRUE;

        FacesContext fc = FacesContext.getCurrentInstance();
        ExternalContext ec = fc.getExternalContext();
        ec.redirect("adminViewStaff.xhtml");
    }

    public void returnBack() throws IOException {
        System.out.print("returnBack Called");
        view = FALSE;
        table = TRUE;

        FacesContext fc = FacesContext.getCurrentInstance();
        ExternalContext ec = fc.getExternalContext();
        ec.redirect("adminViewStaff.xhtml");
    }

    public void updateStaff() throws IOException {
        String[] temp = unit.split(",");
        businessUnit = temp[0];
        division = temp[1];
        staff.getRole().setBusinessUnit(businessUnit);
        staff.getRole().setDivision(division);

        staff.setBase(accountManagementSessionBean.fetchBase(base));
//        System.out.print(staff.getStaffNo() + ", " + staff.getEmail() + ", " + staff.getContactNumber() + ", " + staff.getAddress() + ", " + staff.getDepartment() + ", " + staff.getBase());
        accountManagementSessionBean.updateStaff(staff);

        staff = accountManagementSessionBean.getStaff(staff.getStaffNo());
        FacesContext fc = FacesContext.getCurrentInstance();
        ExternalContext ec = fc.getExternalContext();
        ec.redirect("adminViewStaff.xhtml");
    }

//        if (businessUnit.equals("Administration")) {
//            divisions = new ArrayList<>();
//            divisions.add("Human Resources");
//            divisions.add("Information Technology");
//            divisions.add("Finance");
//        }else if(businessUnit.equals("Operation Control")){
//            divisions = new ArrayList<>();
//            divisions.add("Ground Crew");
//            divisions.add("Luggage Management");
//            System.out.print("OC here");
//        }else if(businessUnit.equals("Sales and Marketing")){
//            divisions = new ArrayList<>();
//            divisions.add("Sales");
//            divisions.add("Branding");
//            divisions.add("Membership Management");
//            divisions.add("Complementary Service");
//        }else if(businessUnit.equals("Maintenance")){
//            divisions = new ArrayList<>();
//            divisions.add("Aircraft Maintenance");
//        }else{
//            divisions = new ArrayList<>();
//            divisions.add("Crew Management");
//            divisions.add("Cockpit Crew");
//            divisions.add("Cabin Crew");
//            divisions.add("Planning");
//        }
    public void fetchStaff() {
        this.staffList = accountManagementSessionBean.fetchStaff();
        System.out.print(staffList);
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

//    public String getUnit() {
//        return unit;
//    }
//
//    public void setUnit(String unit) {
//        this.unit = unit;
//    }
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

    public String getBase() {
        return base;
    }

    public void setBase(String base) {
        this.base = base;
    }

    public List<StaffEntity> getStaffList() {
        return staffList;
    }

    public void setStaffList(List<StaffEntity> staffList) {
        this.staffList = staffList;
    }

    public List<StaffEntity> getFilteredStaff() {
        return filteredStaff;
    }

    public void setFilteredStaff(List<StaffEntity> filteredStaff) {
        this.filteredStaff = filteredStaff;
    }

    public StaffEntity getStaff() {
        return staff;
    }

    public void setStaff(StaffEntity staff) {
        this.staff = staff;
    }

    public boolean isView() {
        return view;
    }

    public void setView(boolean view) {
        this.view = view;
    }

    public boolean isTable() {
        return table;
    }

    public void setTable(boolean table) {
        this.table = table;
    }

    public List<String> getDivisions() {
        return divisions;
    }

    public void setDivisions(List<String> divisions) {
        this.divisions = divisions;
    }

    public List<String> getBases() {
        return bases;
    }

    public void setBases(List<String> bases) {
        this.bases = bases;
    }

    public String getUnit() {
        return unit;
    }

    public void setUnit(String unit) {
        this.unit = unit;
    }

    public List<SelectItem> getBusinessUnits() {
        return businessUnits;
    }

    public void setBusinessUnits(List<SelectItem> businessUnits) {
        this.businessUnits = businessUnits;
    }

    public Boolean getView() {
        return view;
    }

    public void setView(Boolean view) {
        this.view = view;
    }

    public Boolean getTable() {
        return table;
    }

    public void setTable(Boolean table) {
        this.table = table;
    }

}
