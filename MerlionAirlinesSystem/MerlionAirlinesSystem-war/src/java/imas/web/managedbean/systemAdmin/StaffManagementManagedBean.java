/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package imas.web.managedbean.systemAdmin;

import imas.common.sessionbean.AccountManagementSessionBeanLocal;
import java.io.IOException;
import java.io.Serializable;
import javax.ejb.EJB;
import javax.faces.application.FacesMessage;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.inject.Named;
import javax.faces.view.ViewScoped;

/**
 *
 * @author Howard
 */
@Named
@ViewScoped
public class StaffManagementManagedBean implements Serializable{
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
    /**
     * Creates a new instance of StaffManagementManagedBean
     */
    public StaffManagementManagedBean() {
    }
    
    public void addStaff() throws IOException{
        if(accountManagementSessionBean.addStaff(staffNo, name, email, contactNumber, address, gender, base, department)){
            FacesMessage msg = new FacesMessage("Successful", name + " has already been added!");
            FacesContext.getCurrentInstance().addMessage(null, msg);
            FacesContext fc = FacesContext.getCurrentInstance();
            ExternalContext ec = fc.getExternalContext();
            ec.redirect("systemAdminHome.xhtml");
        }else{
            FacesMessage msg = new FacesMessage("Warning", name + " is already in the system");
            FacesContext.getCurrentInstance().addMessage(null, msg);
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
    
    
}
