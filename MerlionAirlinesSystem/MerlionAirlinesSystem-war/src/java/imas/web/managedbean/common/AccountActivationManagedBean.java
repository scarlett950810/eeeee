/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package imas.web.managedbean.common;

import imas.common.entity.StaffEntity;
import imas.common.entity.StaffRole;
import imas.common.sessionbean.AccountManagementSessionBeanLocal;
import imas.common.sessionbean.UserProfileManagementSessionBeanLocal;
import java.io.IOException;
import java.io.Serializable;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.application.FacesMessage;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;
import javax.inject.Named;
import javax.faces.view.ViewScoped;
import org.primefaces.event.FlowEvent;

/**
 *
 * @author Howard
 */
@Named(value = "accountActivationManagedBean")
@ViewScoped
public class AccountActivationManagedBean implements Serializable {

    @EJB
    private AccountManagementSessionBeanLocal accountManagementSessionBean;
    @EJB
    private UserProfileManagementSessionBeanLocal userProfileManagementSessionBean;

    private StaffEntity staff;
    private boolean skip;
    private String staffNo;
    private String role;
    private StaffRole staffRole;
    private String originPassword;
    private String newPassword;
    private String newRepeatPassword;

    public AccountActivationManagedBean() {
    }

    @PostConstruct
    public void init() {
        staffNo = (String) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("staffNo");
        staff = accountManagementSessionBean.getStaff(staffNo);
        staffRole = staff.getRole();
        role = staffRole.getPosition() + ", " + staffRole.getBusinessUnit() + " " + staffRole.getDivision() + "\n";
        if(staffRole.getBase() != null){
            role = role + staffRole.getBase();
        }
        role = role + staffRole.getLocation();
    }

    public String onFlowProcess(FlowEvent event) {
        return event.getNewStep();

    }

    public StaffEntity getStaff() {
        return staff;
    }

    public void setStaff(StaffEntity staff) {
        this.staff = staff;
    }

    public boolean isSkip() {
        return skip;
    }

    public void setSkip(boolean skip) {
        this.skip = skip;
    }

    public String getStaffNo() {
        return staffNo;
    }

    public void setStaffNo(String staffNo) {
        this.staffNo = staffNo;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public StaffRole getStaffRole() {
        return staffRole;
    }

    public void setStaffRole(StaffRole staffRole) {
        this.staffRole = staffRole;
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

    public void changePassword(ActionEvent event) throws IOException {
        System.out.print("123");
        FacesContext fc = FacesContext.getCurrentInstance();
        if (userProfileManagementSessionBean.getOldPassword(staffNo, originPassword)) {
            System.out.print("old password correct");
            if (newPassword.equals(newRepeatPassword)) {
                System.out.print("new password equal");
                userProfileManagementSessionBean.updatePassword(newPassword, staffNo);

                FacesMessage msg = new FacesMessage("Successful", "You have changed your password");
                fc.addMessage(null, msg);
            } else {
                FacesMessage msg = new FacesMessage("Sorry", "Please repeat your password again");
                fc.addMessage(null, msg);
            }
        } else {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Incorrect old password", "Contact admin."));
            System.out.print("here");
        }
    }

    public void activateAccount() throws IOException {

        accountManagementSessionBean.activateAccount(staff.getStaffNo());
        FacesContext fc = FacesContext.getCurrentInstance();
        ExternalContext ec = fc.getExternalContext();
        ec.redirect("common_landing.xhtml");
    }

}
