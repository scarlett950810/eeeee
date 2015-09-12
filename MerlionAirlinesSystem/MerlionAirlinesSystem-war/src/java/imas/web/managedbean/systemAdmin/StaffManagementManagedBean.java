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
import static java.lang.Boolean.FALSE;
import static java.lang.Boolean.TRUE;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;

/**
 *
 * @author Howard
 */
@ManagedBean
@SessionScoped
public class StaffManagementManagedBean implements Serializable {

    @EJB
    private AccountManagementSessionBeanLocal accountManagementSessionBean;

    private String staffNo;
    private String name;
    private String email;
    private String contactNumber;
    private String address;
    private String gender;
    private String base;
    private String department;
    private Integer tabIndex = 0;
    private Boolean view = FALSE;
    private Boolean table = TRUE;

    private List<StaffEntity> staffList;
    private List<StaffEntity> filteredStaff;
    private final List<String> departments = new ArrayList<>();
    private final List<String> bases = new ArrayList<>();
    private StaffEntity staff;

    @PostConstruct
    public void init() {
        fetchStaff();
        bases.add("SIN");
        bases.add("CAN");
        bases.add("XMN");
        departments.add("marketing");
        departments.add("administration");
        departments.add("logistics");
    }

    public StaffManagementManagedBean() {
    }

    public void addStaff() throws IOException {
        if (accountManagementSessionBean.addStaff(staffNo, name, email, contactNumber, address, gender, base, department)) {
            FacesMessage msg = new FacesMessage("Successful", name + " has already been added!");
            FacesContext.getCurrentInstance().addMessage(null, msg);
            FacesContext fc = FacesContext.getCurrentInstance();
            ExternalContext ec = fc.getExternalContext();
            ec.redirect("systemAdminHome.xhtml");
        } else {
            FacesMessage msg = new FacesMessage("Warning", name + " is already in the system");
            FacesContext.getCurrentInstance().addMessage(null, msg);
        }
        staffNo = null;
        name = null;
        email = null;
        contactNumber = null;
        address = null;
        gender = null;
        base = null;
        department = null;
        System.out.print("oh yea");
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

    public String getBase() {
        return base;
    }

    public void setBase(String base) {
        this.base = base;
    }

    public String getDepartment() {
        return department;
    }

    public void setDepartment(String department) {
        this.department = department;
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

    public List<String> getDepartments() {
        
        return departments;
    }

    public void viewStaffAccount() throws IOException {
        System.out.print(staff.getDisplayName() + "is viewed.");
        view = TRUE;
        table = FALSE;
        tabIndex = 1;
        FacesContext fc = FacesContext.getCurrentInstance();
        ExternalContext ec = fc.getExternalContext();
        ec.redirect("systemAdminStaffManagement.xhtml");
    }

    public void deleteStaffAccount() throws IOException {
        accountManagementSessionBean.deleteStaff(staff.getStaffNo());
        System.out.print(staff.getDisplayName() + "is deleted.");
        fetchStaff();
        view = FALSE;
        table = TRUE;
        tabIndex = 1;
        FacesContext fc = FacesContext.getCurrentInstance();
        ExternalContext ec = fc.getExternalContext();
        ec.redirect("systemAdminStaffManagement.xhtml");
    }

    public Integer getTabIndex() {
        return tabIndex;
    }

    public void setTabIndex(Integer tabIndex) {
        this.tabIndex = tabIndex;
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

    public void returnBack() throws IOException {
        System.out.print("returnBack Called");
        view = FALSE;
        table = TRUE;
        tabIndex = 1;
        FacesContext fc = FacesContext.getCurrentInstance();
        ExternalContext ec = fc.getExternalContext();
        ec.redirect("systemAdminStaffManagement.xhtml");
    }

    public void delete() {
        System.out.print(staff.getDisplayName());
    }

    public void fetchStaff() {
        this.staffList = accountManagementSessionBean.fetchStaff();
        System.out.print(staffList);
    }
    
    public void updateStaff() throws IOException{
        System.out.print(staff.getStaffNo() + ", " + staff.getEmail() + ", " + staff.getContactNumber() + ", " + staff.getAddress() + ", " + staff.getDepartment() + ", " + staff.getBase());
        accountManagementSessionBean.updateStaff(staff.getStaffNo(), staff.getEmail(), staff.getContactNumber(), staff.getAddress(), staff.getDepartment(), staff.getBase());
        staff = accountManagementSessionBean.getStaff(staff.getStaffNo());
        FacesContext fc = FacesContext.getCurrentInstance();
        ExternalContext ec = fc.getExternalContext();
        ec.redirect("systemAdminStaffManagement.xhtml");
    }

    public List<String> getBases() {
        
        return bases;
    }
    
    
}
