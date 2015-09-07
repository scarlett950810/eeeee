/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package imas.web.managedbean.planning;

import imas.planning.entity.AircraftGroupEntity;
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
@FacesConverter(value = "aircraftGroupConverter")
public class AircraftGroupConverter  implements Converter {
    
    @Override
    public Object getAsObject(FacesContext fc, UIComponent uic, String value) {
        if (value != null && value.trim().length() > 0) {
            try {
                
                if (value == "") {
                    return null;
                }
                
                List<AircraftGroupEntity> aircraftGroupEntities = (List<AircraftGroupEntity>)fc.getExternalContext().getSessionMap().get("aircraftGroups");
                
                Long aircraftGroupEntityId = Long.valueOf(Long.parseLong(value));
                
                for(AircraftGroupEntity aircraftGroupEntity:aircraftGroupEntities)
                {
                    if(aircraftGroupEntity.getId().equals(aircraftGroupEntityId))
                    {
                        return aircraftGroupEntity;
                    }
                }
                
                return null;
            } catch (NumberFormatException e) {
                throw new ConverterException(new FacesMessage(FacesMessage.SEVERITY_ERROR, "Conversion Error", "Not a valid aircraft group."));
            }
        } else {
            return null;
        }
    }
    
    @Override
    public String getAsString(FacesContext fc, UIComponent uic, Object object) {
        if (object != null) {    
            return String.valueOf(((AircraftGroupEntity) object).getId());
        } else {
            return null;
        }
    }
    
}
