/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package imas.web.managedbean.common;

import imas.common.entity.StaffEntity;
import imas.common.sessionbean.AccountManagementSessionBeanLocal;
import java.io.Serializable;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.inject.Named;
import javax.faces.view.ViewScoped;
import javax.servlet.http.HttpServletRequest;

/**
 *
 * @author Howard
 */
@Named(value = "passwordResetManagedBean")
@ViewScoped
public class PasswordResetManagedBean implements Serializable{
    @EJB
    private AccountManagementSessionBeanLocal accountManagementSessionBean;

    
    
    private String staffNo;
    private String securityAnswer;
    private String token;
    private boolean questionPassed;
    private String newPassword;
    private String repeatNewPassword;
    private StaffEntity staff;
    
    /**
     * Creates a new instance of PasswordResetManagedBean
     */
    public PasswordResetManagedBean() {
        
    }

    @PostConstruct
    public void init() {
        HttpServletRequest req = (HttpServletRequest)FacesContext.getCurrentInstance().getExternalContext().getRequest();
        token = req.getParameter("token");
        System.out.println(token);
        staff = accountManagementSessionBean.getStaffBasedOnToken(token);
        if(staff == null){
            System.out.print("Staff in null");
        }
        questionPassed = false;

    }
    
    public void resetPassword(){
        if(newPassword.equals(repeatNewPassword)){
            accountManagementSessionBean.resetStaffPassword(newPassword);
        }else{
            FacesMessage msg = new FacesMessage("Sorry", "Please repeat your new password again");
            FacesContext.getCurrentInstance().addMessage(null, msg);
        }
    }
    
    public void answerSequrityQuestion(){
        boolean answerCorrect = accountManagementSessionBean.checkSecurityAnswer(staffNo, token);
        if(answerCorrect){
            questionPassed = true;
        }
    }
    
    public String getStaffNo() {
        return staffNo;
    }

    public void setStaffNo(String staffNo) {
        this.staffNo = staffNo;
    }

    public String getSecurityAnswer() {
        return securityAnswer;
    }

    public void setSecurityAnswer(String securityAnswer) {
        this.securityAnswer = securityAnswer;
    }

    public boolean isQuestionPassed() {
        return questionPassed;
    }

    public void setQuestionPassed(boolean questionPassed) {
        this.questionPassed = questionPassed;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getNewPassword() {
        return newPassword;
    }

    public void setNewPassword(String newPassword) {
        this.newPassword = newPassword;
    }

    public String getRepeatNewPassword() {
        return repeatNewPassword;
    }

    public void setRepeatNewPassword(String repeatNewPassword) {
        this.repeatNewPassword = repeatNewPassword;
    }
    
    
}
