/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package imas.web.managedbean.planning;

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
@FacesConverter("routeConverter")
public class RouteConverter implements Converter {
    
    @Override
    public Object getAsObject(FacesContext fc, UIComponent uic, String value) {
        if (value != null && value.trim().length() > 0) {
            try {
                System.out.println("dadadadtry");
                List<RouteEntity> routeEntities = (List<RouteEntity>)fc.getExternalContext().getSessionMap().get("routesRangeList");
                System.out.println("dadadaaadaf"+routeEntities);
                
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
        System.out.println("dadadadagetasstring");
        System.out.println("object"+object);
        if (object != null) {    
            System.out.println("object!=null");
            System.out.println(String.valueOf(((RouteEntity) object).getId()));
            return String.valueOf(((RouteEntity) object).getId());
        } else {
            return null;
        }
    }
    
} 
