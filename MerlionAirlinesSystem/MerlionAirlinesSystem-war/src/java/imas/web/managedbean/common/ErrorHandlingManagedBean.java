/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package imas.web.managedbean.common;

import imas.common.entity.StaffEntity;
import imas.common.entity.StaffRole;
import imas.common.sessionbean.AccountManagementSessionBeanLocal;
import java.io.IOException;
import javax.inject.Named;
import javax.enterprise.context.SessionScoped;
import java.io.Serializable;
import javax.ejb.EJB;
import javax.faces.context.FacesContext;

/**
 *
 * @author Howard
 */
@Named(value = "errorHandlingManagedBean")
@SessionScoped
public class ErrorHandlingManagedBean implements Serializable {

    @EJB
    private AccountManagementSessionBeanLocal accountManagementSessionBean;

    /**
     * Creates a new instance of ErrorHandlingManagedBean
     */
    public ErrorHandlingManagedBean() {
    }

    public void noLoginRedirection() throws IOException {
        FacesContext.getCurrentInstance().getExternalContext().redirect("http://localhost:8080/MerlionAirlinesSystem-war/common/common_login.xhtml");

    }

    public void noAccessRedirection() throws IOException {
        FacesContext.getCurrentInstance().getExternalContext().redirect("http://localhost:8080/MerlionAirlinesSystem-war/common/common_landing.xhtml");

    }

    public void noAccessActivationRedirection() throws IOException {
        FacesContext.getCurrentInstance().getExternalContext().redirect("http://localhost:8080/MerlionAirlinesSystem-war/common/common_landing.xhtml");

    }

    public void checkUser() {
        String staffNo = (String) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("staffNo");
//        System.out.print("Staff Number: " +staffNo);
        if (staffNo == null) {
//            System.out.print("staff number is null");
            try {
                FacesContext.getCurrentInstance().getExternalContext().redirect("http://localhost:8080/MerlionAirlinesSystem-war/common/noLoginErrorPage.xhtml");

            } catch (IOException ex) {
                System.out.print(ex);
            }
        } else {
//            System.out.print("staff number is not null");
            StaffEntity staff = accountManagementSessionBean.getStaff(staffNo);
            StaffRole role = staff.getRole();
//            System.out.print(role.getAccessRight());
            if (!role.getAccessRight().contains(FacesContext.getCurrentInstance().getViewRoot().getViewId()) && !role.getAccessRight().contains("all")) {
                try {
                    FacesContext.getCurrentInstance().getExternalContext().redirect("http://localhost:8080/MerlionAirlinesSystem-war/common/noAccessErrorPage.xhtml");
                } catch (IOException ex) {
                    System.out.print(ex);
                }

            }
        }
    }
}
