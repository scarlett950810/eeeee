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
    private String staffNo;
    private String role;
    private StaffRole staffRole;
    private String originPassword;
    private String newPassword;
    private String newRepeatPassword;
    private String securityAnswer;
    private boolean passwordReset = true;
    private int questionIndex;

    public AccountActivationManagedBean() {
    }

    @PostConstruct
    public void init() {
        staffNo = (String) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("staffNo");
        if (staffNo == null) {
            try {
                FacesContext.getCurrentInstance().getExternalContext().redirect("noLoginErrorPage.xhtml");
            } catch (IOException ex) {
                System.out.print(ex);
            }
        } else {
            staff = accountManagementSessionBean.getStaff(staffNo);
            if (staff.getActivationStatus() == true) {
                try {
                    FacesContext.getCurrentInstance().getExternalContext().redirect("noAccessActivationPage.xhtml");
                } catch (IOException ex) {
                    System.out.print(ex);
                }
            } else {
                staffRole = staff.getRole();
                role = staffRole.getPosition() + ", " + staffRole.getBusinessUnit() + ", " + staffRole.getDivision() + ", " + staffRole.getLocation() + "\n";
            }
        }
    }

    public String onFlowProcess(FlowEvent event) throws IOException {

        if (event.getOldStep().equals("resetPassword")) {
            changePassword();
        }else if(event.getOldStep().equals("setSecurityAnswer")){
            userProfileManagementSessionBean.setSequrityQuestion(staffNo, securityAnswer, questionIndex);
        }

        return event.getNewStep();

    }

    public StaffEntity getStaff() {
        return staff;
    }

    public void setStaff(StaffEntity staff) {
        this.staff = staff;
    }

    public boolean isPasswordReset() {
        return passwordReset;
    }

    public void setPasswordReset(boolean passwordReset) {
        this.passwordReset = passwordReset;
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

    public String getSecurityAnswer() {
        return securityAnswer;
    }

    public void setSecurityAnswer(String securityAnswer) {
        this.securityAnswer = securityAnswer;
    }

    public int getQuestionIndex() {
        return questionIndex;
    }

    public void setQuestionIndex(int questionIndex) {
        this.questionIndex = questionIndex;
    }
    
    public void changePassword() throws IOException {
        FacesContext fc = FacesContext.getCurrentInstance();
        if (userProfileManagementSessionBean.getOldPassword(staffNo, originPassword)) {
            System.out.print("old password correct");
            if (newPassword.equals(newRepeatPassword)) {
                System.out.print("new password equal");
                userProfileManagementSessionBean.updatePassword(newPassword, staffNo);

                FacesMessage msg = new FacesMessage("Successful", "You have changed your password");
                fc.addMessage(null, msg);
            } else {
                FacesMessage msg = new FacesMessage("Sorry, please repeat your password again", "Please repeat your password again");
                fc.addMessage(null, msg);
            }
        } else {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Incorrect old password", "Contact admin."));
            System.out.print("here");
        }
        passwordReset = false;
        System.out.print(passwordReset);
    }

    public void setSecurityQuestionAnswer() {
        System.out.println(securityAnswer);
        userProfileManagementSessionBean.setSequrityQuestion(staffNo, securityAnswer, questionIndex);
//        FacesContext fc = FacesContext.getCurrentInstance();
//        FacesMessage msg = new FacesMessage("Success", "you have set the security answer");
//        fc.addMessage(null, msg);
    }

    public void activateAccount() throws IOException {

        accountManagementSessionBean.activateAccount(staff.getStaffNo());
        FacesContext fc = FacesContext.getCurrentInstance();
        ExternalContext ec = fc.getExternalContext();
        ec.redirect("common_landing.xhtml");
    }

}
