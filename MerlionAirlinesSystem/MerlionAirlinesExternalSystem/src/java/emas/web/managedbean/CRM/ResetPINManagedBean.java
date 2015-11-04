/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package emas.web.managedbean.CRM;

import imas.crm.sessionbean.CustomerAccountManagementSessionBeanLocal;
import java.io.IOException;
import javax.inject.Named;
import javax.enterprise.context.SessionScoped;
import java.io.Serializable;
import javax.ejb.EJB;
import javax.faces.application.FacesMessage;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;

/**
 *
 * @author Howard
 */
@Named(value = "resetPINManagedBean")
@SessionScoped
public class ResetPINManagedBean implements Serializable {

    @EJB
    private CustomerAccountManagementSessionBeanLocal customerAccountManagementSessionBean;

    private String memberID;
    private String securityAnswer;
    private int securityQuestionIndex;
    private boolean questionPassed = false;
    private String password;
    private String repeatPassword;

    public ResetPINManagedBean() {
    }

    public void checkSequrityAnswer() {
        if (customerAccountManagementSessionBean.checkSequrityAnswer(securityQuestionIndex, securityAnswer, memberID)) {
            questionPassed = true;
        } else {
            FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_ERROR, "Incorrect Member ID or Security Answer", "");
            FacesContext.getCurrentInstance().addMessage(null, msg);
        }
    }

    public void resetPIN() throws IOException {
        if (password.equals(repeatPassword)) {
            if (password.length() == 6) {
                customerAccountManagementSessionBean.resetPIN(memberID, password);
                FacesContext fc = FacesContext.getCurrentInstance();
                ExternalContext ec = fc.getExternalContext();
                ec.redirect("crmHomePage.xhtml");
            } else {
                FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_ERROR, "Please make sure that your PIN number is 6 digit", "");
                FacesContext.getCurrentInstance().addMessage(null, msg);
            }
        } else {
            FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_ERROR, "Please repeat your new PIN again", "");
            FacesContext.getCurrentInstance().addMessage(null, msg);
        }
    }

    public String getMemberID() {
        return memberID;
    }

    public void setMemberID(String memberID) {
        this.memberID = memberID;
    }

    public String getSecurityAnswer() {
        return securityAnswer;
    }

    public void setSecurityAnswer(String securityAnswer) {
        this.securityAnswer = securityAnswer;
    }

    public int getSecurityQuestionIndex() {
        return securityQuestionIndex;
    }

    public void setSecurityQuestionIndex(int securityQuestionIndex) {
        this.securityQuestionIndex = securityQuestionIndex;
    }

    public boolean isQuestionPassed() {
        return questionPassed;
    }

    public void setQuestionPassed(boolean questionPassed) {
        this.questionPassed = questionPassed;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getRepeatPassword() {
        return repeatPassword;
    }

    public void setRepeatPassword(String repeatPassword) {
        this.repeatPassword = repeatPassword;
    }
}
