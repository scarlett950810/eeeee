/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package imas.web.managedbean.common;

import imas.common.entity.StaffEntity;
import imas.common.sessionbean.LoginSessionBeanLocal;
import imas.common.sessionbean.UserProfileManagementSessionBeanLocal;
import java.io.IOException;
import java.io.Serializable;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.application.FacesMessage;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.inject.Named;
import javax.faces.view.ViewScoped;
import org.primefaces.context.RequestContext;
import org.primefaces.event.RowEditEvent;

/**
 *
 * @author Howard
 */
@Named(value = "userProfileManagedBean")
@ViewScoped
public class UserProfileManagedBean implements Serializable {

    @EJB
    private LoginSessionBeanLocal loginSessionBean;

    @EJB
    private UserProfileManagementSessionBeanLocal userProfileManagementSessionBean;

    private StaffEntity staff;
    private String email;
    private String contactNumber;
    private String staffNo;
    private String displayName;
    private String department;
    private String address;
    private String gender;
    private String base;
    private String originPassword;
    private String newPassword;
    private String newRepeatPassword;
    private String newEmail;

    @PostConstruct
    public void init(){
        staffNo = (String) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("staffNo");
        if(staffNo != null){
            staff = loginSessionBean.fetchStaff(staffNo);
        }
    }

    public UserProfileManagedBean() {
    }

    public StaffEntity getStaff() {
        return staff;
    }

    public void setStaff(StaffEntity staff) {
        this.staff = staff;
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

    public String getStaffNo() {
        return staffNo;
    }

    public void setStaffNo(String staffNo) {
        this.staffNo = staffNo;
    }

    public String getDisplayName() {
        return displayName;
    }

    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }

    public String getDepartment() {
        return department;
    }

    public void setDepartment(String department) {
        this.department = department;
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

    public String getOriginPassword() {
        return originPassword;
    }

    public void setOriginPassword(String originPassword) {
        this.originPassword = originPassword;
    }

    public String getNewPassword() {
        return newPassword;
    }

    public void setNewPassword(String newPassword) {
        this.newPassword = newPassword;
    }

    public String getNewRepeatPassword() {
        return newRepeatPassword;
    }

    public void setNewRepeatPassword(String newRepeatPassword) {
        this.newRepeatPassword = newRepeatPassword;
    }

    public String getNewEmail() {
        return newEmail;
    }

    public void setNewEmail(String newEmail) {
        this.newEmail = newEmail;
    }

    public void changeEmail() throws IOException {
        userProfileManagementSessionBean.updateEmail(staffNo, newEmail);
        FacesContext fc = FacesContext.getCurrentInstance();
        ExternalContext ec = fc.getExternalContext();
        ec.redirect("userProfile.xhtml");
    }

    public void changeContact() throws IOException {
        userProfileManagementSessionBean.updateContact(staffNo, contactNumber);
        RequestContext context = RequestContext.getCurrentInstance();
        FacesMessage msg = new FacesMessage("Successful", "You have changed your contact number");
        FacesContext fc = FacesContext.getCurrentInstance();
        fc.addMessage(null, msg);
        fc.getExternalContext().redirect("userProfile.xhtml");

//        FacesContext fc = FacesContext.getCurrentInstance();
//        ExternalContext ec = fc.getExternalContext();
//        ec.redirect("userProfile.xhtml");
    }

    public void changePassword() throws IOException {
        FacesContext fc = FacesContext.getCurrentInstance();
        if (userProfileManagementSessionBean.getOldPassword(staffNo, originPassword)) {

            if (newPassword.equals(newRepeatPassword)) {

                userProfileManagementSessionBean.updatePassword(newPassword, staffNo);

                FacesMessage msg = new FacesMessage("Successful", "You have changed your password");
                fc.addMessage(null, msg);
                ExternalContext ec = fc.getExternalContext();
                ec.redirect("userProfile.xhtml");
            } else {
                FacesMessage msg = new FacesMessage("Sorry", "Please repeat your password again");
                fc.addMessage(null, msg);
            }
        } else {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Incorrect old password", "Contact admin."));
            System.out.print("here");
        }

    }

    public void onRowEdit(RowEditEvent event) {
        System.out.print(email + ", " + contactNumber);
    }

    public void onRowCancel(RowEditEvent event) {
        System.out.print("edit cancelled.");
    }

}
