/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package imas.web.managedbean.systemAdmin;

import imas.common.entity.StaffEntity;
import imas.common.sessionbean.AccountManagementSessionBeanLocal;
import java.io.Serializable;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import javax.inject.Named;

/**
 *
 * @author Lei
 */
@ManagedBean
@ViewScoped

public class UnlockStaffManagedBean implements Serializable {

    @EJB
    private AccountManagementSessionBeanLocal accountManagementSessionBean;
    private List<StaffEntity> staffList;
    private StaffEntity staff;
    private List<StaffEntity> filteredStaff;

    @PostConstruct
    public void init() {
        System.out.print("Start Start");
        staffList = accountManagementSessionBean.fetchLockStaffs();

    }

    public void unlockStaff() {
        FacesContext fc = FacesContext.getCurrentInstance();
        if (staff == null) {
            fc.addMessage(null, new FacesMessage("Warning", "Please select a staff to be unlocked"));
        } else {
            accountManagementSessionBean.unlockStaff(staff);
             fc.addMessage(null, new FacesMessage("Reminder",staff.getStaffNo() + " " + staff.getDisplayName()+" has been unlocked" ));
            staffList = accountManagementSessionBean.fetchLockStaffs();

        }
    }

    public List<StaffEntity> getStaffList() {
        return staffList;
    }

    public void setStaffList(List<StaffEntity> staffList) {
        this.staffList = staffList;
    }

    public StaffEntity getStaff() {
        return staff;
    }

    public void setStaff(StaffEntity staff) {
        this.staff = staff;
    }

    public List<StaffEntity> getFilteredStaff() {
        return filteredStaff;
    }

    public void setFilteredStaff(List<StaffEntity> filteredStaff) {
        this.filteredStaff = filteredStaff;
    }

}
