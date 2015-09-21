/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package imas.common.entity;

import java.io.Serializable;
import java.util.Date;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Temporal;

/**
 *
 * @author Scarlett
 */
@Entity
public class InternalMessageEntity implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @JoinColumn(referencedColumnName = "id")
    @ManyToOne
    private StaffEntity sender;
    
    @JoinColumn(referencedColumnName = "id")
    @ManyToOne
    private StaffEntity receiver;
    
    private String content;
    
    @Temporal(javax.persistence.TemporalType.TIMESTAMP)
    private Date sentTime;
    
    private boolean isRead;

    public InternalMessageEntity() {
    }

    public InternalMessageEntity(StaffEntity sender, StaffEntity receiver, String content) {
        this.sender = sender;
        this.receiver = receiver;
        this.content = content;
        this.sentTime = new Date();
        this.isRead = false;
    }
    
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public StaffEntity getSender() {
        return sender;
    }

    public void setSender(StaffEntity sender) {
        this.sender = sender;
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

    public Date getSentTime() {
        return sentTime;
    }

    public void setSentTime(Date sentTime) {
        this.sentTime = sentTime;
    }

    public boolean isIsRead() {
        return isRead;
    }

    public void setIsRead(boolean read) {
        this.isRead = read;
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
        if (!(object instanceof InternalMessageEntity)) {
            return false;
        }
        InternalMessageEntity other = (InternalMessageEntity) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "irms.common.entity.InternalMessageEntity[ id=" + id + " ]";
    }
    
}
