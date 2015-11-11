/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package GDS.web.managedbean;

import GDS.sessionbean.GDSAirportSessionBeanLocal;
import GDS.sessionbean.GDSCompanySessionBeanLocal;
import java.io.IOException;
import java.io.Serializable;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;

/**
 *
 * @author Scarlett
 */
@ManagedBean
@SessionScoped
public class GDSLoginManagedBean implements Serializable {
    
    @EJB
    private GDSCompanySessionBeanLocal gDSCompanySessionBean;
    
    @EJB
    private GDSAirportSessionBeanLocal gDSAirportSessionBean;
    
    private String logInAccount;
    private String logInPassword;
    
    
    public GDSLoginManagedBean() {
    }
    
    @PostConstruct
    public void init() {
        gDSAirportSessionBean.getAllGDSAirport();
    }

    public void logIn() throws IOException {
        System.out.println("log in");
        if (gDSCompanySessionBean.logIn(logInAccount, logInPassword)) {
            FacesContext.getCurrentInstance().getExternalContext().getSessionMap().put("account", logInAccount);
            FacesContext.getCurrentInstance().getExternalContext().redirect("GDSAddFlight.xhtml");
        } else {
            FacesContext.getCurrentInstance().addMessage("logIn", 
                    new FacesMessage(FacesMessage.SEVERITY_ERROR, "Log in failed", "Account does not exist or password does not match."));  
        }
    }
    
    public boolean loggedIn() {
        return (FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("account") != null);
    }

    public String getLogInAccount() {
        return logInAccount;
    }

    public void setLogInAccount(String logInAccount) {
        this.logInAccount = logInAccount;
    }

    public String getLogInPassword() {
        return logInPassword;
    }

    public void setLogInPassword(String logInPassword) {
        this.logInPassword = logInPassword;
    }
    
}
