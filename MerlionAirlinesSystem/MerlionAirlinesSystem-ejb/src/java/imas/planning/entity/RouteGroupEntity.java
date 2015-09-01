package imas.planning.entity;

import java.io.Serializable;
import java.util.ArrayList;
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
public class RouteGroupEntity implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private Long minRange;
    private Long maxRange;

    @OneToMany(cascade = {CascadeType.PERSIST}, mappedBy = "routeGroup")
    private List<RouteEntity> group = new ArrayList<RouteEntity>();

    public RouteGroupEntity() {

    }

    public RouteGroupEntity(Long id, Long minRange, Long maxRange) {
        this.id = id;
        this.minRange = minRange;
        this.maxRange = maxRange;
    }

    public Long getMinRange() {
        return minRange;
    }

    public void setMinRange(Long minRange) {
        this.minRange = minRange;
    }

    public Long getMaxRange() {
        return maxRange;
    }

    public void setMaxRange(Long maxRange) {
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
