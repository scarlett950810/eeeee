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
    private GDSAirportSessionBeanLocal gDSAirportSessionBean;
    
    @EJB
    private GDSCompanySessionBeanLocal gDSCompanySessionBean;

    private String logInAccount;
    private String logInPassword;
    
    private String account;
    private String password1;
    private String password2;
    private String name;
    private String HQCountry;
    private String mainPage;
    private String email;
    private String contactNo;
    
    public GDSLoginManagedBean() {
    }
    
    @PostConstruct
    public void init() {
        gDSAirportSessionBean.getAllGDSAirport();
    }

    public void registerCompany() {
        System.out.println("register");
        if (!gDSCompanySessionBean.accountNotExist(account)) {
            FacesContext.getCurrentInstance().addMessage("register", 
                    new FacesMessage(FacesMessage.SEVERITY_ERROR, "Account exists", "Please use a new account."));
        } else if(password1 == null ? password2 != null : !password1.equals(password2)) {
            FacesContext.getCurrentInstance().addMessage("register", 
                    new FacesMessage(FacesMessage.SEVERITY_ERROR, "Password different", "Please make sure you enter the same password twice."));
        } else {
            gDSCompanySessionBean.register(account, password1, name, HQCountry, mainPage, email, contactNo);
            FacesContext.getCurrentInstance().addMessage("register", 
                    new FacesMessage(FacesMessage.SEVERITY_INFO, "Succecced", "You may log in now."));
        }
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
    
    public String getAccount() {
        return account;
    }

    public void setAccount(String account) {
        this.account = account;
    }

    public String getPassword1() {
        return password1;
    }

    public void setPassword1(String password1) {
        this.password1 = password1;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getHQCountry() {
        return HQCountry;
    }

    public void setHQCountry(String HQCountry) {
        this.HQCountry = HQCountry;
    }

    public String getMainPage() {
        return mainPage;
    }

    public void setMainPage(String mainPage) {
        this.mainPage = mainPage;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getContactNo() {
        return contactNo;
    }

    public void setContactNo(String contactNo) {
        this.contactNo = contactNo;
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

    public String getPassword2() {
        return password2;
    }

    public void setPassword2(String password2) {
        this.password2 = password2;
    }
    
}
