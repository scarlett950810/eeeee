/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package emas.web.managedbean.CRM;

import imas.crm.sessionbean.CustomerAccountManagementSessionBeanLocal;
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
 * @author wutong
 */
@Named(value = "externalLoginManagedBean")
@ViewScoped
public class ExternalLoginManagedBean implements Serializable{
    private String email;
    private String pin;
    @EJB
    private CustomerAccountManagementSessionBeanLocal customerAccountManagementSession;
    

    /**
     * Creates a new instance of ExternalLoginManagedBean
     */
    public ExternalLoginManagedBean() {
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPin() {
        return pin;
    }

    public void setPin(String pin) {
        this.pin = pin;
    }
    
    public void doLogin() throws IOException{
        FacesMessage msg;
        System.err.println("ENTER11111"+email+pin);
        if(!customerAccountManagementSession.checkValidity(email, pin)){
            msg = new FacesMessage(FacesMessage.SEVERITY_ERROR, "Your account and password do not match", "");
            FacesContext.getCurrentInstance().addMessage(null, msg);
        }
        else{
            FacesContext fc = FacesContext.getCurrentInstance();
            ExternalContext ec = fc.getExternalContext();
            FacesContext.getCurrentInstance().getExternalContext().getSessionMap().put("loginUserEmail", email);
            ec.redirect("crmWelcomePageAfterLogin.xhtml");
        }
            
            
    }
}
