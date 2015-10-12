package webservice;

import imas.planning.entity.FlightEntity;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlElements;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;


@XmlRootElement
@XmlType(propOrder = {
    "flightsList"
})

public class FlightsList implements Serializable
{
    private List<FlightEntity> flightsList;

    
    
    public FlightsList() 
    {
        flightsList = new ArrayList<>();
    }

    
    
    public FlightsList(List<FlightEntity> flightsList) 
    {
        this.flightsList = flightsList;
    }

    
    
    @XmlElements({
        @XmlElement(name = "flightsList", type = FlightEntity.class)
    })
    @XmlElementWrapper
    public List<FlightEntity> getProductCategories() {
        return flightsList;
    }

    public void setProductCategories(List<FlightEntity> flightsList) {
        this.flightsList = flightsList;
    }
}