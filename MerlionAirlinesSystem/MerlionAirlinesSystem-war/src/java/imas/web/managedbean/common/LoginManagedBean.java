/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package imas.web.managedbean.common;

import imas.common.entity.InternalAnnouncementEntity;
import imas.common.entity.StaffEntity;
import imas.common.sessionbean.LoginSessionBeanLocal;
import java.io.IOException;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.faces.bean.ManagedBean;
import javax.ejb.EJB;
import javax.faces.application.FacesMessage;
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
    private LoginSessionBeanLocal loginSessionBean;

    private StaffEntity staffEntity;
    
    private List<InternalAnnouncementEntity> allAnnouncements;
    
    private List<InternalAnnouncementEntity> unreadAnnouncements;
    

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

    public StaffEntity getStaffEntity() {
        return staffEntity;
    }

    public void setStaffEntity(StaffEntity staffEntity) {
        this.staffEntity = staffEntity;
    }

    public void doLogin(String staffNo, String password) throws IOException {
        FacesContext fc = FacesContext.getCurrentInstance();
        ExternalContext ec = fc.getExternalContext();
        StaffEntity staffEntity_result = loginSessionBean.doLogin(staffNo, password);
        
        if (staffEntity_result != null) {
            ec.redirect(ec.getRequestContextPath() + "/templates/DefaultTemplate.xhtml");
            this.setStaffEntity(staffEntity_result);
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
            FacesContext.getCurrentInstance().getExternalContext().redirect("http://localhost:8080/MerlionAirlinesSystem-war/common/common_login.xhtml");

        } catch (IOException ex) {
            Logger.getLogger(LoginManagedBean.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public List<InternalAnnouncementEntity> getAllAnnouncements() {
        return loginSessionBean.getAllAnnoucements(staffEntity);
    }

    public void setAllAnnouncements(List<InternalAnnouncementEntity> allAnnouncements) {
        this.allAnnouncements = allAnnouncements;
    }
    
    public List<InternalAnnouncementEntity> getUnreadAnnouncements() {
        return loginSessionBean.getUnreadAnnoucements(staffEntity);
    }
    
    public void setUnreadAnnouncements(List<InternalAnnouncementEntity> unreadAnnouncements) {
        this.unreadAnnouncements = unreadAnnouncements;
    }
}
