/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package GDS.entity;

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
public class GDSCompanyEntity implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    
    private String account;
    private String password;
    
    private String name;
    private String HQCountry;
    private String mainPage;
    private String email;
    private String contactNo;
    
    @OneToMany(mappedBy = "GDSCompany")
    private List<GDSFlightEntity> flights;

    public GDSCompanyEntity() {
    }

    public GDSCompanyEntity(String account, String password, String name, String HQCountry, String mainPage, String email, String contactNo) {
        this.account = account;
        this.password = password;
        this.name = name;
        this.HQCountry = HQCountry;
        this.mainPage = mainPage;
        this.email = email;
        this.contactNo = contactNo;
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

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
    
    public String getHQCountry() {
        return HQCountry;
    }

    public void setHQCountry(String HQCountry) {
        this.HQCountry = HQCountry;
    }

    public String getMainPage() {
        return mainPage;
    }

    public void setMainPage(String mainPage) {
        this.mainPage = mainPage;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getContactNo() {
        return contactNo;
    }

    public void setContactNo(String contactNo) {
        this.contactNo = contactNo;
    }

    public List<GDSFlightEntity> getFlights() {
        return flights;
    }

    public void setFlights(List<GDSFlightEntity> flights) {
        this.flights = flights;
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
        if (!(object instanceof GDSCompanyEntity)) {
            return false;
        }
        GDSCompanyEntity other = (GDSCompanyEntity) object;
        return !((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id)));
    }

    @Override
    public String toString() {
        return "dds.entity.DDSCompanyEntity[ id=" + id + " ]";
    }
    
}
