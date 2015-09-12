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
import java.security.SecureRandom;
import java.util.Random;
import javax.ejb.EJB;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.inject.Named;
import util.security.CryptographicHelper;

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
    CryptographicHelper cp = new CryptographicHelper();
    private String email;
    private Integer loginAttempts = 0;
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

    public Integer getLoginAttempts() {
        return loginAttempts;
    }

    public void setLoginAttempts(Integer loginAttempts) {
        this.loginAttempts = loginAttempts;
    }

//    public void salt(String password) {
//        String salt = "";
//        String letters = "0123456789abcdefghjkmnpqrstuvwxyzABCDEFGHJKMNPQRSTUVWXYZ23456789";
//        for (int i = 0; i < SALT_LENGTH; i++) {
//            int index = (int) (RANDOM.nextDouble() * letters.length());
//            salt += letters.substring(index, index + 1);
//        }
//    }
    public void doLogin(String staffNo, String password) throws IOException {
        FacesContext fc = FacesContext.getCurrentInstance();
        ExternalContext ec = fc.getExternalContext();
        //sort 

//        password = cp.doMD5Hashing(password+);
//        loginSessionBean.setPass(staffNo,password);
        String returnMsg = loginSessionBean.doLogin(staffNo, password);

        if (returnMsg.equals("success")) {
            ec.redirect(ec.getRequestContextPath() + "/common/common_landing.xhtml");//success
        } else if (returnMsg.equals("wrong password")) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Loggin Error", "invalid user or wrong password"));//success
        } else if (returnMsg.equals("empty")) {
            this.errorMsg("The staff ID is invalid ");//success            
        } else if (returnMsg.equals("delete")) {
            this.errorMsg("The user has been deleted.Please contact the system admin if you have any questions");//success
//            FacesContext.getCurrentInstance().getExternalContext().invalidateSession();
        } else if (returnMsg.equals("captcha")) {
            loginAttempts = 3;
            this.errorMsg("pls input captcha ");
//            FacesContext.getCurrentInstance().getExternalContext().invalidateSession();
        } else if (returnMsg.equals("lock")) {
            this.errorMsg("The staff ID is locked. Please try to login in after 24hrs");//success
//            FacesContext.getCurrentInstance().getExternalContext().invalidateSession();
        } else {
            this.errorMsg("wrong");
//            FacesContext.getCurrentInstance().getExternalContext().invalidateSession();
        }

    }

    private void errorMsg(String message) {
        FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_ERROR, message, "");
        FacesContext context = FacesContext.getCurrentInstance();
        context.addMessage(null, msg);
        context.getExternalContext().getFlash().setKeepMessages(true);

    }

    public void welcome() {
        FacesContext.getCurrentInstance().addMessage(
                null,
                new FacesMessage(FacesMessage.SEVERITY_INFO, "Welcome Back",
                        "Continue your works."));

    }

    public void doLogout() throws IOException {
        System.out.print("loginManagedBean.doLogout called.");

        ExternalContext ec = FacesContext.getCurrentInstance().getExternalContext();
        ec.invalidateSession();

        ec.redirect("http://localhost:8080/MerlionAirlinesSystem-war/common/common_login.xhtml");

        //      FacesContext.getCurrentInstance().addMessage(
//                null,
//                new FacesMessage(FacesMessage.SEVERITY_WARN,
//                        "You Have Logged Out!",
//                        "Thank you for using Merlion Airline Internal Portal!"));
//        FacesContext.getCurrentInstance().getExternalContext().invalidateSession();
//        FacesContext.getCurrentInstance().getExternalContext().getSessionMap().clear();
//        try {
////            return "http://localhost:8080/MerlionAirlinesSystem-war/common/common_login.xhtml";
//            FacesContext.getCurrentInstance().getExternalContext().redirect("http://localhost:8080/MerlionAirlinesSystem-war/common/common_login.xhtml");
//       
    }

    public void forgetPassword() throws IOException {
        if (accountManagementSessionBean.checkEmailExistence(email)) {
            accountManagementSessionBean.resetStaffPassword(email);
            FacesContext fc = FacesContext.getCurrentInstance();
            ExternalContext ec = fc.getExternalContext();
            ec.redirect("common_login.xhtml");
        } else {
            FacesMessage msg = new FacesMessage("Sorry", "Please enter the correct email address associated with your account.");
            FacesContext.getCurrentInstance().addMessage(null, msg);
        }

    }

    public void insertData() {
        loginSessionBean.insertData();
    }
}
