/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package imas.web.managedbean.common;

import imas.common.sessionbean.LoginSessionBeanLocal;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.RequestScoped;
import javax.ejb.EJB;
import javax.faces.application.FacesMessage;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.inject.Named;
import org.primefaces.context.RequestContext;

/**
 *
 * @author Howard
 */
@Named(value = "loginManagedBean")
@ManagedBean
@RequestScoped
public class LoginManagedBean {

    @EJB
    private LoginSessionBeanLocal loginSessionBean;

    private boolean loggedIn;

    /**
     * Creates a new instance of LoginManagedBean
     */
    public LoginManagedBean() {
        this.loggedIn = false;
    }

    public LoginSessionBeanLocal getLoginSessionBean() {
        return loginSessionBean;
    }

    public void setLoginSessionBean(LoginSessionBeanLocal loginSessionBean) {
        this.loginSessionBean = loginSessionBean;
    }

    public boolean isLoggedIn() {
        return loggedIn;
    }

    public void setLoggedIn(boolean loggedIn) {
        this.loggedIn = loggedIn;
    }

    public void doLogin(String staffNo, String password) throws IOException {
        FacesContext fc = FacesContext.getCurrentInstance();
        ExternalContext ec = fc.getExternalContext();

        if (loginSessionBean.doLogin(staffNo, password) == true) {
            ec.redirect("http://localhost:8080/MerlionAirlinesSystem-war/common/LandingPage.xhtml");
            loggedIn = true;
        } else {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Loggin Error", "Invalid credentials"));
        }
    }

   public void welcome() {
        FacesContext.getCurrentInstance().addMessage(
                null,
                new FacesMessage(FacesMessage.SEVERITY_INFO, "Welcome Back",
                        "Continue your works."));

    }

    public void logout() {
        FacesContext.getCurrentInstance().addMessage(
                null,
                new FacesMessage(FacesMessage.SEVERITY_WARN,
                        "You Have Logged Out!",
                        "Thank you for using Island Furniture Internal Portal!"));

        // invalidate session, and redirect to other pages
        FacesContext.getCurrentInstance().getExternalContext().invalidateSession();
        FacesContext.getCurrentInstance().getExternalContext().getSessionMap().clear();
        try {
            String url = FacesContext.getCurrentInstance().getExternalContext().getRequestContextPath();
            FacesContext.getCurrentInstance().getExternalContext().redirect(url + "/common/common_login.xhtml");

        } catch (IOException ex) {
            Logger.getLogger(LoginManagedBean.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}

     
   
