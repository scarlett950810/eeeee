/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package imas.web.managedbean.planning;

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
@FacesConverter(value = "pilotConverter")
public class PilotConverter  implements Converter {
    
    @Override
    public Object getAsObject(FacesContext fc, UIComponent uic, String value) {
        if (value != null && value.trim().length() > 0) {
            try {
                
                List<PilotEntity> pilotEntities = (List<PilotEntity>)fc.getExternalContext().getSessionMap().get("pilotList");
                
                Long pilotEntityId = Long.valueOf(Long.parseLong(value));
                
                System.err.println("PRINT"+pilotEntities);
                for(PilotEntity pilotEntity:pilotEntities)
                {
                    if(pilotEntity.getId().equals(pilotEntityId))
                    {
                        return pilotEntity;
                    }
                }
                
                return null;
            } catch (NumberFormatException e) {
                throw new ConverterException(new FacesMessage(FacesMessage.SEVERITY_ERROR, "Conversion Error", "Not a valid pilot."));
            }
        } else {
            return null;
        }
    }
    
    @Override
    public String getAsString(FacesContext fc, UIComponent uic, Object object) {
        if (object != null) {    
            return String.valueOf(((PilotEntity) object).getId());
        } else {
            return null;
        }
    }
    
}
