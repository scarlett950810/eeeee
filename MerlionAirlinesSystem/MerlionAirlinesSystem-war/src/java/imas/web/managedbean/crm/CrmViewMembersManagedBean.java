/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package imas.web.managedbean.crm;


import imas.crm.entity.MemberEntity;
import imas.crm.sessionbean.CustomerAccountManagementSessionBeanLocal;
import java.io.IOException;
import java.io.Serializable;
import static java.lang.Boolean.FALSE;
import static java.lang.Boolean.TRUE;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;

/**
 *
 * @author wutong
 */
@ManagedBean
@SessionScoped
public class CrmViewMembersManagedBean implements Serializable {
    
    @EJB
    private CustomerAccountManagementSessionBeanLocal customerAccountManagementSessionBeanLocal;
    private List<MemberEntity> memberList;
    private List<MemberEntity> filteredMemberList;
    
    private Boolean view = FALSE;
    private Boolean table = TRUE;
    private MemberEntity member;

    /**
     * Creates a new instance of CrmViewMembersManagedBean
     */
    public CrmViewMembersManagedBean() {
    }
    
    @PostConstruct
    public void init() {
        memberList = customerAccountManagementSessionBeanLocal.retrieveMembers();
    }
    
    public void viewStaffAccount() throws IOException {
        view = TRUE;
        table = FALSE;
        System.out.print(member);
        System.out.print(member.getMemberID());

        FacesContext fc = FacesContext.getCurrentInstance();
        ExternalContext ec = fc.getExternalContext();
        ec.redirect("crmViewMembers.xhtml");
    }
    
    public void returnBack() throws IOException {
        System.out.print("returnBack Called");
        view = FALSE;
        table = TRUE;

        FacesContext fc = FacesContext.getCurrentInstance();
        ExternalContext ec = fc.getExternalContext();
        ec.redirect("crmViewMembers.xhtml");
    }

    public List<MemberEntity> getMemberList() {
        memberList = customerAccountManagementSessionBeanLocal.retrieveMembers();
        return memberList;
    }

    public void setMemberList(List<MemberEntity> memberList) {
        this.memberList = memberList;
    }

    public List<MemberEntity> getFilteredMemberList() {
        return filteredMemberList;
    }

    public void setFilteredMemberList(List<MemberEntity> filteredMemberList) {
        this.filteredMemberList = filteredMemberList;
    }

    public boolean isView() {
        return view;
    }

    public void setView(boolean view) {
        this.view = view;
    }

    public boolean isTable() {
        return table;
    }

    public void setTable(boolean table) {
        this.table = table;
    }

    public MemberEntity getMember() {
        return member;
    }

    public void setMember(MemberEntity member) {
        this.member = member;
    }
    
    public String getDateString(){
        if(member==null) return null;
        SimpleDateFormat simpleDateFormat1 = new SimpleDateFormat("MMM d yyyy");
        return simpleDateFormat1.format(member.getBirthDate());
    }
}
