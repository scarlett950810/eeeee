/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package GDS.entity;

import java.io.Serializable;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Lob;
import javax.persistence.ManyToOne;

/**
 *
 * @author Scarlett
 */
@Entity
public class GDSBookingClassEntity implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    @ManyToOne
    private GDSFlightEntity GDSflight;
    private String name;
    private Double price;
    @Lob
    private String notes;
    private Integer quota;

    public GDSBookingClassEntity() {
    }

    public GDSBookingClassEntity(GDSFlightEntity GDSflight, String name, Double price, String notes, Integer quota) {
        this.GDSflight = GDSflight;
        this.name = name;
        this.price = price;
        this.notes = notes;
        this.quota = quota;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public GDSFlightEntity getGDSflight() {
        return GDSflight;
    }

    public void setGDSflight(GDSFlightEntity GDSflight) {
        this.GDSflight = GDSflight;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Double getPrice() {
        return price;
    }

    public void setPrice(Double price) {
        this.price = price;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

    public Integer getQuota() {
        return quota;
    }

    public void setQuota(Integer quota) {
        this.quota = quota;
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
        if (!(object instanceof GDSBookingClassEntity)) {
            return false;
        }
        GDSBookingClassEntity other = (GDSBookingClassEntity) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "dds.entity.DDSBookingClassEntity[ id=" + id + " ]";
    }
    
}
