/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package emas.web.managedbean.distribution;

import imas.distribution.entity.PassengerEntity;
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
@FacesConverter(value = "passengerConverter")
public class PassengerConverter implements Converter {

    @Override
    public Object getAsObject(FacesContext fc, UIComponent uic, String value) {
//        System.out.println("converter getAsObject value = " + value);
        if (value != null && value.trim().length() > 0) {
            try {
                
                List<PassengerEntity> passengerEntities = (List<PassengerEntity>)fc.getExternalContext().getSessionMap().get("passengerList");
                
//                System.out.println("airportEntities in converter = " + airportEntities);
                
                Long passengerEntityId = Long.valueOf(Long.parseLong(value));
                
                for(PassengerEntity passengerEntity:passengerEntities)
                {
//                    System.err.println("airportEntity: " + airportEntity.getAirportName());
                    
                    if(passengerEntity.getId().equals(passengerEntityId))
                    {
                        return passengerEntity;
                    }
                }
                
                return null;
            } catch (NumberFormatException e) {
                throw new ConverterException(new FacesMessage(FacesMessage.SEVERITY_ERROR, "Conversion Error", "Not a valid passenger."));
            }
        } else {
            return null;
        }
    }

    @Override
    public String getAsString(FacesContext fc, UIComponent uic, Object object) {
//        System.out.println("converter getAsString object = " + object);
        if (object != null) {    
            return String.valueOf(((PassengerEntity) object).getId());
        } else {
            return "";
        }
    }
}
