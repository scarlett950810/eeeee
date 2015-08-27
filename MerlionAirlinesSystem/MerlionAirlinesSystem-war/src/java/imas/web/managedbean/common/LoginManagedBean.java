/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package imas.web.managedbean.common;

import imas.common.sessionbean.LoginSessionBeanLocal;
import java.io.IOException;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.RequestScoped;
import javax.ejb.EJB;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.inject.Named;

/**
 *
 * @author Howard
 */
@Named(value="loginManagedBean")
@ManagedBean
@RequestScoped
public class LoginManagedBean {
    @EJB
    private LoginSessionBeanLocal loginSessionBean;
    
    /**
     * Creates a new instance of LoginManagedBean
     */
    public LoginManagedBean() {
        
    }
    
    public void doLogin(String staffNo, String password) throws IOException
    {
        FacesContext fc = FacesContext.getCurrentInstance();
        ExternalContext ec = fc.getExternalContext();
        
        if(loginSessionBean.doLogin(staffNo, password)==true){
            ec.redirect("LandingPage.xhtml");
        }else{
            System.out.print("log in failed");
        }
    }
    
}
