/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package emas.web.managedbean.CRM;

import imas.crm.entity.MemberEntity;
import imas.crm.sessionbean.MemberAccountActivationSessionBeanLocal;
import java.io.IOException;
import java.io.Serializable;
import javax.ejb.EJB;
import javax.faces.context.FacesContext;
import javax.inject.Named;
import javax.faces.view.ViewScoped;
import javax.servlet.http.HttpServletRequest;

/**
 *
 * @author Howard
 */
@Named(value = "memberAccountActivationManagedBean")
@ViewScoped
public class MemberAccountActivationManagedBean implements Serializable{
    @EJB
    private MemberAccountActivationSessionBeanLocal memberAccountActivationSessionBean;

    private String securityAnswer;
    private int questionIndex;
    private String token;
    private MemberEntity member;
    
    public MemberAccountActivationManagedBean() {
    }
    
    public void checkUser() throws IOException{
        HttpServletRequest req = (HttpServletRequest)FacesContext.getCurrentInstance().getExternalContext().getRequest();
        token = req.getParameter("token");
        
        member = memberAccountActivationSessionBean.getMemberBasedOnToken(token);
        if(member == null){
            FacesContext.getCurrentInstance().getExternalContext().redirect("noAccessErrorPage");
        }else{
            memberAccountActivationSessionBean.activateMemberAccount(member);
        }
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public MemberEntity getMember() {
        return member;
    }

    public void setMember(MemberEntity member) {
        this.member = member;
    }
}
