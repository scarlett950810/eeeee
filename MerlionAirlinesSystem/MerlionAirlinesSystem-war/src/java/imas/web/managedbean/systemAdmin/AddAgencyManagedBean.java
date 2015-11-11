/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package imas.web.managedbean.systemAdmin;

import DDS.sessionbean.AgencySessionBeanLocal;
import java.io.IOException;
import java.io.Serializable;
import javax.ejb.EJB;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.inject.Named;
import javax.faces.view.ViewScoped;

/**
 *
 * @author wutong
 */
@Named(value = "addAgencyManagedBean")
@ViewScoped
public class AddAgencyManagedBean implements Serializable {
    
    private String account;
    private String email;
    private String contactNumber;
    private String pin;
    private String name;
    @EJB
    private AgencySessionBeanLocal agencySessionBeanLocal;

    /**
     * Creates a new instance of AddAgencyManagedBean
     */
    public AddAgencyManagedBean() {
    }

    public String getAccount() {
        return account;
    }

    public void setAccount(String account) {
        this.account = account;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getContactNumber() {
        return contactNumber;
    }

    public void setContactNumber(String contactNumber) {
        this.contactNumber = contactNumber;
    }

    public String getPin() {
        return pin;
    }

    public void setPin(String pin) {
        this.pin = pin;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void save() throws IOException{
        String pin = "";
        for (int i = 0; i < 6; i++) {
            int random = (int) (Math.random() * 9);
            pin = pin + String.valueOf(random);
        }
        System.err.println("Pin number generated" + pin);
        agencySessionBeanLocal.createAgency(account, pin, name, contactNumber, email);
        FacesContext fc = FacesContext.getCurrentInstance();
        ExternalContext ec = fc.getExternalContext();
        ec.redirect("systemAdminHome.xhtml");

    }

}
