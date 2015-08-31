/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package imas.common.entity;

import java.io.Serializable;
import java.util.List;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;

/**
 *
 * @author Scarlett
 */
@Entity
public class StaffEntity implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    
    private String staffNo;
    
    private String displayName;
    
    private String password;
    
    private String email;

    private String handphone;
    
    private String department;
    
    @OneToMany(mappedBy = "receiver")
    private List<InternalAnnouncementEntity> announcements;
    
    @OneToMany(mappedBy = "sender")
    private List<InternalMessageEntity> sentMessages;

    @OneToMany(mappedBy = "receiver")
    private List<InternalMessageEntity> receivedMessages;
    
    public StaffEntity() {
    }
    
    public StaffEntity(String staffNo, String displayName, String password, String email, String handphone, String department) {
        this.staffNo = staffNo;
        this.displayName = displayName;
        this.password = password;
        this.email = email;
        this.handphone = handphone;
        this.department = department;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }
    
    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getHandphone() {
        return handphone;
    }

    public void setHandphone(String handphone) {
        this.handphone = handphone;
    }

    public String getDepartemnt() {
        return department;
    }

    public void setDepartemnt(String department) {
        this.department = department;
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
    
    @Override
    public int hashCode() {
        int hash = 0;
        hash += (id != null ? id.hashCode() : 0);
        return hash;
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
