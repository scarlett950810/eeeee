/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package imas.planning.entity;

import java.io.Serializable;
import java.util.List;
import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;

/**
 *
 * @author Lei
 */
@Entity
public class AircraftGroupEntity implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private String type;
    @OneToMany(mappedBy = "aircraftGroup")
    private List<AircraftEntity> AircraftsInGroup;

    public AircraftGroupEntity() {
    }

    public AircraftGroupEntity(String type) {
        this.type = type;
    }

    public AircraftGroupEntity(String type, List<AircraftEntity> AircraftsInGroup) {
        this.type = type;
        this.AircraftsInGroup = AircraftsInGroup;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public List<AircraftEntity> getAircraftsInGroup() {
        return AircraftsInGroup;
    }

    public void setAircraftsInGroup(List<AircraftEntity> AircraftsInGroup) {
        this.AircraftsInGroup = AircraftsInGroup;
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
        if (!(object instanceof AircraftGroupEntity)) {
            return false;
        }
        AircraftGroupEntity other = (AircraftGroupEntity) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "imas.planning.entity.AircraftGroupEntity[ id=" + id + " ]";
    }

}
