/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package emas.web.managedbean.CRM;

import imas.crm.entity.MemberEntity;
import imas.crm.sessionbean.CustomerAccountManagementSessionBeanLocal;
import java.io.IOException;
import java.io.Serializable;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.inject.Named;
import javax.servlet.http.HttpServletRequest;

/**
 *
 * @author wutong
 */
@ManagedBean
@SessionScoped
public class ExternalLoginManagedBean implements Serializable {

    private String pin;
    private String memberID;
    private boolean login; // to track whether a customer has logged in
    private MemberEntity member;

    @EJB
    private CustomerAccountManagementSessionBeanLocal customerAccountManagementSession;

    @PostConstruct
    public void init() {
        
    }
    
    /**
     * Creates a new instance of ExternalLoginManagedBean
     */
    public ExternalLoginManagedBean() {
        login = false;
    }

    public String getPin() {
        return pin;
    }

    public void setPin(String pin) {
        this.pin = pin;
    }

    public boolean isLogin() {
        return login;
    }

    public void setLogin(boolean login) {
        this.login = login;
    }

    public MemberEntity getMember() {
        return member;
    }

    public void setMember(MemberEntity member) {
        this.member = member;
    }

    public String getMemberID() {
        return memberID;
    }

    public void setMemberID(String memberID) {
        this.memberID = memberID;
    }

    public void viewProfile() throws IOException {
        FacesContext fc = FacesContext.getCurrentInstance();
        ExternalContext ec = fc.getExternalContext();
        ec.redirect("https://localhost:8181//MerlionAirlinesExternalSystem/CRM/crmMemberProfile.xhtml");

    }
    
    public void checkLogin() throws IOException{
        FacesContext fc = FacesContext.getCurrentInstance();
        ExternalContext ec = fc.getExternalContext();
        memberID = (String)ec.getSessionMap().get("memberID");
        if(memberID != null){
            ec.redirect("https://localhost:8181//MerlionAirlinesExternalSystem/CRM/crmMemberProfile.xhtml");
        }
        
    }

    public void doLogin() throws IOException {
        FacesMessage msg;
        member = customerAccountManagementSession.checkValidity(memberID, pin);
        if (member == null) {
            msg = new FacesMessage(FacesMessage.SEVERITY_ERROR, "Invalid match between account and PIN", "");
            FacesContext.getCurrentInstance().addMessage(null, msg);
        } else {
            login = true;
            FacesContext fc = FacesContext.getCurrentInstance();
            ExternalContext ec = fc.getExternalContext();
            ec.getSessionMap().put("memberID", memberID);
            System.out.print("here"+login);
            ec.redirect("crmMemberProfile.xhtml");
        }
    }

    public void doLogout() {
        FacesContext fc = FacesContext.getCurrentInstance();
        ExternalContext ec = fc.getExternalContext();
        ec.getSessionMap().remove("memberID");
        member = null;
        memberID = null;
        pin = null;
        login = false;
        System.out.print("log out "+login);
    }
}
