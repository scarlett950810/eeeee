/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package DDS.entity;

import imas.distribution.entity.TicketEntity;
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
public class AgencyEntity implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private String account;
    private String pin;
    private String name;
    private String contactNumber;
    private String email;
    @OneToMany(mappedBy = "agency")
    private List<TicketEntity> tickets;

    public AgencyEntity() {
    }

    public AgencyEntity(String account, String pin, String name, String contactNumber, String email) {
        this.account = account;
        this.pin = pin;
        this.name = name;
        this.contactNumber = contactNumber;
        this.email = email;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getAccount() {
        return account;
    }

    public void setAccount(String account) {
        this.account = account;
    }

    public String getPin() {
        return pin;
    }

    public void setPin(String pin) {
        this.pin = pin;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getContactNumber() {
        return contactNumber;
    }

    public void setContactNumber(String contactNumber) {
        this.contactNumber = contactNumber;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public List<TicketEntity> getTickets() {
        return tickets;
    }

    public void setTickets(List<TicketEntity> tickets) {
        this.tickets = tickets;
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
        if (!(object instanceof AgencyEntity)) {
            return false;
        }
        AgencyEntity other = (AgencyEntity) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "DDS.entity.AgencyEntity[ id=" + id + " ]";
    }
    
}
