/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package imas.web.managedbean.common;

import imas.common.entity.InternalMessageEntity;
import imas.common.entity.StaffEntity;
import imas.common.sessionbean.InternalMessageSessionBeanLocal;
import java.io.Serializable;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.inject.Named;
//import javax.enterprise.context.Dependent;
import javax.faces.context.FacesContext;
import javax.faces.view.ViewScoped;
import javax.persistence.PostRemove;

/**
 *
 * @author Scarlett
 */
@Named(value = "internalMessageManagedBean")
@ViewScoped
public class InternalMessageManagedBean implements Serializable {

    @EJB
    private InternalMessageSessionBeanLocal internalMessageSessionBean;

    private StaffEntity loggedInStaff;

    private StaffEntity receiver;

    private String content;
    
    private List<StaffEntity> staffList;
    
    private List<InternalMessageEntity> allMessages;

    /**
     * Creates a new instance of InternalMessageManagedBean
     */
    public InternalMessageManagedBean() {
    }

    @PostConstruct
    public void init() {
        String staffNo = (String) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("staffNo");
        System.out.println("staffNo" + staffNo);
        loggedInStaff = internalMessageSessionBean.getStaffEntityByStaffNo(staffNo);

        
        staffList = internalMessageSessionBean.getAllStaff();        
        FacesContext.getCurrentInstance().getExternalContext().getSessionMap().put("staffList", staffList);
        
//        System.out.println(internalMessageSessionBean.getAllStaff());
//        System.out.println(loggedInStaff);
    }

    @PostRemove
    public void destroy() {
        FacesContext.getCurrentInstance().getExternalContext().getSessionMap().remove("staffList");
    }

    public List<StaffEntity> getStaffList() {
        return staffList;
    }

    public void setStaffList(List<StaffEntity> staffList) {
        this.staffList = staffList;
    }

    public StaffEntity getLoggedInStaff() {
        return loggedInStaff;
    }

    public void setLoggedInStaff(StaffEntity loggedInStaff) {
        this.loggedInStaff = loggedInStaff;
    }

    public StaffEntity getReceiver() {
        return receiver;
    }

    public void setReceiver(StaffEntity receiver) {
        this.receiver = receiver;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public List<InternalMessageEntity> getAllMessages() {
        return internalMessageSessionBean.getAllMessages(loggedInStaff);
    }

    public void setAllMessages(List<InternalMessageEntity> allMessages) {
        this.allMessages = allMessages;
    }
    
    
    public void send() {
        internalMessageSessionBean.sendMessage(loggedInStaff, receiver, content);
    }
    

}