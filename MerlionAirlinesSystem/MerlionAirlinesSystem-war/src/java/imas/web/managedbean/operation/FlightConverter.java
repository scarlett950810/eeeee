/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package imas.web.managedbean.operation;


import imas.planning.entity.FlightEntity;
import java.util.List;
import javax.faces.application.FacesMessage;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.ConverterException;
import javax.faces.convert.FacesConverter;

/**
 *
 * @author Howard
 */
@FacesConverter(value = "flightConverter")
public class FlightConverter  implements Converter {
    
    @Override
    public Object getAsObject(FacesContext fc, UIComponent uic, String value) {
        if (value != null && value.trim().length() > 0) {
            try {
                
                List<FlightEntity> flightEntities = (List<FlightEntity>)fc.getExternalContext().getSessionMap().get("allFlights");
                
                Long flightEntityId = Long.valueOf(Long.parseLong(value));
                
                for(FlightEntity flightEntity: flightEntities)
                {
                    if(flightEntity.getId().equals(flightEntityId))
                    {
                        return flightEntity;
                    }
                }
                
                return null;
            } catch (NumberFormatException e) {
                throw new ConverterException(new FacesMessage(FacesMessage.SEVERITY_ERROR, "Conversion Error", "Not a valid flight."));
            }
        } else {
            return null;
        }
    }
    
    @Override
    public String getAsString(FacesContext fc, UIComponent uic, Object object) {
        if (object != null) {    
            return String.valueOf(((FlightEntity) object).getId());
        } else {
            return null;
        }
    }
    
}
