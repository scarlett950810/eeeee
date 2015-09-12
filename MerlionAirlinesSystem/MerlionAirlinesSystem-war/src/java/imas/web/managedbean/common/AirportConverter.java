/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package imas.web.managedbean.common;

import imas.planning.entity.AirportEntity;
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
@FacesConverter(value = "airportConverter")
public class AirportConverter implements Converter {

    @Override
    public Object getAsObject(FacesContext fc, UIComponent uic, String value) {
        if (value != null && value.trim().length() > 0) {
            try {
                
                List<AirportEntity> airportEntities = (List<AirportEntity>)fc.getExternalContext().getSessionMap().get("airportList");
                
                Long airportEntityId = Long.valueOf(Long.parseLong(value));
                
                for(AirportEntity airportEntity:airportEntities)
                {
                    if(airportEntity.getId().equals(airportEntityId))
                    {
                        return airportEntity;
                    }
                }
                
                return null;
            } catch (NumberFormatException e) {
                throw new ConverterException(new FacesMessage(FacesMessage.SEVERITY_ERROR, "Conversion Error", "Not a valid airport."));
            }
        } else {
            return null;
        }
    }

    @Override
    public String getAsString(FacesContext fc, UIComponent uic, Object object) {
        if (object != null) {    
            return String.valueOf(((AirportEntity) object).getId());
        } else {
            return null;
        }
    }
}
