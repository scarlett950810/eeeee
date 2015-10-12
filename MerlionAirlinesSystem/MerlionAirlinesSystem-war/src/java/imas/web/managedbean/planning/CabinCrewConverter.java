/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package imas.web.managedbean.planning;

import imas.common.entity.CabinCrewEntity;
import imas.common.entity.PilotEntity;
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
@FacesConverter(value = "cabinCrewConverter")
public class CabinCrewConverter  implements Converter {
    
    @Override
    public Object getAsObject(FacesContext fc, UIComponent uic, String value) {
        if (value != null && value.trim().length() > 0) {
            try {
                
                List<CabinCrewEntity> cabinCrewEntities = (List<CabinCrewEntity>)fc.getExternalContext().getSessionMap().get("cabinCrewList");
                
                Long cabinCrewEntityId = Long.valueOf(Long.parseLong(value));
                
                System.err.println("PRINT"+cabinCrewEntities);
                for(CabinCrewEntity cabinCrewEntity:cabinCrewEntities)
                {
                    if(cabinCrewEntity.getId().equals(cabinCrewEntityId))
                    {
                        return cabinCrewEntity;
                    }
                }
                
                return null;
            } catch (NumberFormatException e) {
                throw new ConverterException(new FacesMessage(FacesMessage.SEVERITY_ERROR, "Conversion Error", "Not a valid cabin crew."));
            }
        } else {
            return null;
        }
    }
    
    @Override
    public String getAsString(FacesContext fc, UIComponent uic, Object object) {
        if (object != null) {    
            return String.valueOf(((CabinCrewEntity) object).getId());
        } else {
            return null;
        }
    }
    
}
