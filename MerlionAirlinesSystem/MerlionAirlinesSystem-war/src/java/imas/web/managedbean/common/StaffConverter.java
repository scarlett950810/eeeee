/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package imas.web.managedbean.common;

import imas.common.entity.StaffEntity;
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
@FacesConverter(value = "staffConverter")
public class StaffConverter  implements Converter {
    
    @Override
    public Object getAsObject(FacesContext fc, UIComponent uic, String value) {
        if (value != null && value.trim().length() > 0) {
            try {
                System.out.println(fc);
                System.out.println(uic);
                System.out.println("getAsObject: " + value);
                
                List<StaffEntity> staffEntities = (List<StaffEntity>)fc.getExternalContext().getSessionMap().get("staffList");
                
                System.out.println(staffEntities);
                
                Long staffEntityId = Long.valueOf(Long.parseLong(value));
                
                System.out.println(staffEntityId);
                
                for(StaffEntity staffEntity:staffEntities)
                {
                    if(staffEntity.getId().equals(staffEntityId))
                    {
                        return staffEntity;
                    }
                }
                
                return null;
            } catch (NumberFormatException e) {
                throw new ConverterException(new FacesMessage(FacesMessage.SEVERITY_ERROR, "Conversion Error", "Not a valid staff."));
            }
        } else {
            return null;
        }
    }
    
    @Override
    public String getAsString(FacesContext fc, UIComponent uic, Object object) {
        if (object != null) {    
            String str = String.valueOf(((StaffEntity) object).getId());
            
            System.err.println("getAsString: " + str);
            
            return str;
        } else {
            return null;
        }
    }

}
