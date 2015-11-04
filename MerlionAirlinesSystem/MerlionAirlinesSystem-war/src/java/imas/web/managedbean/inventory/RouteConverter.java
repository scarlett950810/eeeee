/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package imas.web.managedbean.inventory;

import imas.planning.entity.RouteEntity;
import java.util.List;
import javax.faces.application.FacesMessage;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.ConverterException;
import javax.faces.convert.FacesConverter;

/**
 *
 * @author ruicai
 */
@FacesConverter("routeConverterForAllRoutes")
public class RouteConverter implements Converter {
    
    @Override
    public Object getAsObject(FacesContext fc, UIComponent uic, String value) {
        if (value != null && value.trim().length() > 0) {
            try {
               
                List<RouteEntity> routeEntities = (List<RouteEntity>)fc.getExternalContext().getSessionMap().get("routeList");
                
                Long routeEntityId = Long.valueOf(Long.parseLong(value));
                
                for(RouteEntity routeEntity:routeEntities)
                {
                    if(routeEntity.getId().equals(routeEntityId))
                    {
                        return routeEntity;
                    }
                }
                
                return null;
            } catch (NumberFormatException e) {
                throw new ConverterException(new FacesMessage(FacesMessage.SEVERITY_ERROR, "Conversion Error", "Not a valid airport."));
            }
        } else {
            return null;
        }}
 

    @Override
    public String getAsString(FacesContext fc, UIComponent uic, Object object) {
        if (object != null) {
            return String.valueOf(((RouteEntity) object).getId());
        } else {
            return null;
        }
    }
    
} 
