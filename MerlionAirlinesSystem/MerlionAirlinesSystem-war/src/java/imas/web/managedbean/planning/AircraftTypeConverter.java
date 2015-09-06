/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package imas.web.managedbean.planning;

import imas.planning.entity.AircraftTypeEntity;
import java.util.List;
import javax.faces.application.FacesMessage;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.ConverterException;
import javax.faces.convert.FacesConverter;

/**
 *
 * @author Scarlett
 */
@FacesConverter(value = "aircraftTypeConverter")
public class AircraftTypeConverter  implements Converter {
    
    @Override
    public Object getAsObject(FacesContext fc, UIComponent uic, String value) {
        if (value != null && value.trim().length() > 0) {
            try {
                
                List<AircraftTypeEntity> aircraftTypeEntities = (List<AircraftTypeEntity>)fc.getExternalContext().getSessionMap().get("aircraftTypes");
                
                Long aircraftTypeEntityId = Long.valueOf(Long.parseLong(value));
                
                for(AircraftTypeEntity aircraftTypeEntity:aircraftTypeEntities)
                {
                    if(aircraftTypeEntity.getId().equals(aircraftTypeEntityId))
                    {
                        return aircraftTypeEntity;
                    }
                }
                
                return null;
            } catch (NumberFormatException e) {
                throw new ConverterException(new FacesMessage(FacesMessage.SEVERITY_ERROR, "Conversion Error", "Not a valid aircraft Type."));
            }
        } else {
            return null;
        }
    }
    
    @Override
    public String getAsString(FacesContext fc, UIComponent uic, Object object) {
        if (object != null) {    
            return String.valueOf(((AircraftTypeEntity) object).getId());
        } else {
            return null;
        }
    }
    
}
