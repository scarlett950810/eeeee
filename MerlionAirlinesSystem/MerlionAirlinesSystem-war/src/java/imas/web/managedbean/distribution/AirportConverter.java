/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package imas.web.managedbean.distribution;

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
        System.out.println("converter getAsObject value = " + value);
        if (value != null && value.trim().length() > 0) {
            try {
                
                List<AirportEntity> airportEntities = (List<AirportEntity>)fc.getExternalContext().getSessionMap().get("airportList");
                
                System.out.println("airportEntities in converter = " + airportEntities);
                
                Long airportEntityId = Long.valueOf(Long.parseLong(value));
                
                for(AirportEntity airportEntity:airportEntities)
                {
                    System.err.println("airportEntity: " + airportEntity.getAirportName());
                    
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
            AirportEntity newAirportEntity = new AirportEntity();
            newAirportEntity.setId(0l);
            newAirportEntity.setAirportName("");
            newAirportEntity.setAirportCode("");
            newAirportEntity.setCityName("");
            return newAirportEntity;
        }
    }

    @Override
    public String getAsString(FacesContext fc, UIComponent uic, Object object) {
        System.out.println("converter getAsString object = " + object);
        if (object != null) {    
            return String.valueOf(((AirportEntity) object).getId());
        } else {
            return "";
        }
    }
}
