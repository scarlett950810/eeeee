/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package imas.web.managedbean.common;

import imas.common.entity.StaffEntity;
import imas.common.sessionbean.AccountManagementSessionBeanLocal;
import imas.common.sessionbean.LoginSessionBeanLocal;
import java.io.IOException;
import javax.ejb.EJB;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.inject.Named;

/**
 *
 * @author Howard
 */
@Named(value = "loginManagedBean")
@ManagedBean
@SessionScoped
public class LoginManagedBean {
    @EJB
    private AccountManagementSessionBeanLocal accountManagementSessionBean;

    @EJB
    private LoginSessionBeanLocal loginSessionBean;
    
    private String email;
    private StaffEntity staff;

    /**
     * Creates a new instance of LoginManagedBean
     */
    public LoginManagedBean() {
    }

    public LoginSessionBeanLocal getLoginSessionBean() {
        return loginSessionBean;
    }

    public void setLoginSessionBean(LoginSessionBeanLocal loginSessionBean) {
        this.loginSessionBean = loginSessionBean;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void doLogin(String staffNo, String password) throws IOException {
        FacesContext fc = FacesContext.getCurrentInstance();
        ExternalContext ec = fc.getExternalContext();

        if (loginSessionBean.doLogin(staffNo, password)) {
      
            FacesContext.getCurrentInstance().getExternalContext().getSessionMap().put("staffNo", staffNo);
            ec.redirect(ec.getRequestContextPath() + "/common/common_landing.xhtml");
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

    public void doLogout() throws IOException {
        System.out.print("loginManagedBean.doLogout called.");
        FacesContext.getCurrentInstance().addMessage(
                null,
                new FacesMessage(FacesMessage.SEVERITY_WARN,
                        "You Have Logged Out!",
                        "Thank you for using Merlion Airline Internal Portal!"));

        // invalidate session, and redirect to other pages
        FacesContext.getCurrentInstance().getExternalContext().invalidateSession();
        FacesContext.getCurrentInstance().getExternalContext().getSessionMap().clear();
//        try {
//        return "http://localhost:8080/MerlionAirlinesSystem-war/common/common_login.xhtml";
            FacesContext.getCurrentInstance().getExternalContext().redirect("http://localhost:8080/MerlionAirlinesSystem-war/common/common_login.xhtml");

//        } catch (IOException ex) {
//            Logger.getLogger(LoginManagedBean.class.getName()).log(Level.SEVERE, null, ex);
//        }
    }
    
    public void forgetPassword() throws IOException{
        if(accountManagementSessionBean.checkEmailExistence(email)){
            accountManagementSessionBean.resetStaffPassword(email);
            FacesContext fc = FacesContext.getCurrentInstance();
            ExternalContext ec = fc.getExternalContext();
            ec.redirect("common_login.xhtml");
        }else{
            FacesMessage msg = new FacesMessage("Sorry", "Please enter the correct email address associated with your account.");
            FacesContext.getCurrentInstance().addMessage(null, msg);
        }
        
    }

    public void insertData() {
        loginSessionBean.insertData();
    }
}
