/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package irms.web.managedbean.common;

import irms.common.sessionbean.LoginSessionBeanLocal;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.RequestScoped;
import javax.ejb.EJB;
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
    
    public Boolean doLogin()
    {
        return loginSessionBean.doLogin();
    }
    
}
