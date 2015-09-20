package imas.planning.entity;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
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
public class RouteGroupEntity implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private String groupCode;
    private Double minRange;
    private Double maxRange;

    @OneToMany(mappedBy = "routeGroup")
    private List<RouteEntity> group = new ArrayList<>();

    public RouteGroupEntity() {
        
    }

    public RouteGroupEntity(String groupCode, Double minRange, Double maxRange ) {
        this.groupCode = groupCode;
        this.minRange = minRange;
        this.maxRange = maxRange;
    }

    public RouteGroupEntity(Long id, Double minRange, Double maxRange) {
        this.id = id;
        this.minRange = minRange;
        this.maxRange = maxRange;
    }

    public String getGroupCode() {
        return groupCode;
    }

    public void setGroupCode(String groupCode) {
        this.groupCode = groupCode;
    }

    public Double getMinRange() {
        return minRange;
    }

    public void setMinRange(Double minRange) {
        this.minRange = minRange;
    }

    public Double getMaxRange() {
        return maxRange;
    }

    public void setMaxRange(Double maxRange) {
        this.maxRange = maxRange;
    }

    public List<RouteEntity> getGroup() {
        return group;
    }

    public void setGroup(List<RouteEntity> group) {
        this.group = group;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
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
        if (!(object instanceof RouteGroupEntity)) {
            return false;
        }
        RouteGroupEntity other = (RouteGroupEntity) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        
        return "imas.planning.entity.RouteGroupEntity[ id=" + id + " ]";
    }

}
