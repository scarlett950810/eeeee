/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package imas.common.entity;

import imas.planning.entity.AirportEntity;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;

/**
 *
 * @author Scarlett
 */
@Entity
@Inheritance(strategy = InheritanceType.JOINED)
public class StaffEntity implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private String staffNo;

    private String displayName;

    private String password;

    private String email;

    private String contactNumber;

    private String address;

    private String gender;

    @OneToOne
    private AirportEntity base;

    private Boolean activationStatus;

    private Boolean deleteStatus;

    private List<Date> loginAttempt;

    private String salt;

    private Boolean working;

    @OneToMany(mappedBy = "receiver")
    private List<InternalAnnouncementEntity> announcements;

    @OneToMany(mappedBy = "sender")
    private List<InternalMessageEntity> sentMessages;

    @OneToMany(mappedBy = "receiver")
    private List<InternalMessageEntity> receivedMessages;

    @OneToOne
    private StaffRole role;

    public StaffEntity() {
    }

    public StaffEntity(String staffNo, String displayName, String password,
            String email, String contactNumber, String address, String gender) {
        this.staffNo = staffNo;
        this.displayName = displayName;
        this.password = password;
        this.email = email;
        this.contactNumber = contactNumber;
        this.address = address;
        this.gender = gender;
        this.activationStatus = false;
        this.deleteStatus = false;
        this.loginAttempt = new ArrayList();
        this.salt = null;
        this.working = false;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public AirportEntity getBase() {
        return base;
    }

    public void setBase(AirportEntity base) {
        this.base = base;
    }

//    public String getDepartment() {
//        return department;
//    }
//
//    public void setDepartment(String department) {
//        this.department = department;
//    }
    public Boolean getActivationStatus() {
        return activationStatus;
    }

    public void setActivationStatus(Boolean activationStatus) {
        this.activationStatus = activationStatus;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
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

    public String getStaffNo() {
        return staffNo;
    }

    public void setStaffNo(String staffNo) {
        this.staffNo = staffNo;
    }

    public String getDisplayName() {
        return displayName;
    }

    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public List<InternalAnnouncementEntity> getAnnouncements() {
        return announcements;
    }

    public void setAnnouncements(List<InternalAnnouncementEntity> announcements) {
        this.announcements = announcements;
    }

    public List<InternalMessageEntity> getSentMessages() {
        return sentMessages;
    }

    public void setSentMessages(List<InternalMessageEntity> sentMessages) {
        this.sentMessages = sentMessages;
    }

    public List<InternalMessageEntity> getReceivedMessages() {
        return receivedMessages;
    }

    public void setReceivedMessages(List<InternalMessageEntity> receivedMessages) {
        this.receivedMessages = receivedMessages;
    }

    public StaffRole getRole() {
        return role;
    }

    public void setRole(StaffRole role) {
        this.role = role;
    }

//    public List<OrganizationUnit> getOrganizationUnit() {
//        return organizationUnit;
//    }
//
//    public void setOrganizationUnit(List<OrganizationUnit> organizationUnit) {
//        this.organizationUnit = organizationUnit;
//    }
    public List<Date> getLoginAttempt() {
        return loginAttempt;
    }

    public void setLoginAttempt(List<Date> loginAttempt) {
        this.loginAttempt = loginAttempt;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (id != null ? id.hashCode() : 0);
        return hash;
    }

    public Boolean getDeleteStatus() {
        return deleteStatus;
    }

    public void setDeleteStatus(Boolean deleteStatus) {
        this.deleteStatus = deleteStatus;
    }

    public String getSalt() {
        return salt;
    }

    public void setSalt(String salt) {
        this.salt = salt;
    }

    public Boolean getWorking() {
        return working;
    }

    public void setWorking(Boolean working) {
        this.working = working;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof StaffEntity)) {
            return false;
        }
        StaffEntity other = (StaffEntity) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "irms.common.entity.StaffEntity[ id=" + id + " ]";
    }

}
