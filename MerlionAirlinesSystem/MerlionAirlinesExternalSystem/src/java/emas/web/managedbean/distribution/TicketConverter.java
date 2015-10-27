/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package emas.web.managedbean.distribution;

import imas.distribution.entity.TicketEntity;
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
@FacesConverter(value = "ticketConverter")
public class TicketConverter implements Converter {

    @Override
    public Object getAsObject(FacesContext fc, UIComponent uic, String value) {
//        System.out.println("converter getAsObject value = " + value);
        if (value != null && value.trim().length() > 0) {
            try {
                
                List<TicketEntity> ticketEntities = (List<TicketEntity>)fc.getExternalContext().getSessionMap().get("ticketList");
                
                Long ticketEntityId = Long.valueOf(Long.parseLong(value));
                
                for(TicketEntity ticketEntity:ticketEntities)
                {
//                    System.err.println("airportEntity: " + airportEntity.getAirportName());
                    
                    if(ticketEntity.getId().equals(ticketEntityId))
                    {
                        return ticketEntity;
                    }
                }
                
                return null;
            } catch (NumberFormatException e) {
                throw new ConverterException(new FacesMessage(FacesMessage.SEVERITY_ERROR, "Conversion Error", "Not a valid ticket."));
            }
        } else {
//            AirportEntity newAirportEntity = new AirportEntity();
//            newAirportEntity.setId(0l);
//            newAirportEntity.setAirportName("");
//            newAirportEntity.setAirportCode("");
//            newAirportEntity.setCityName("");
//            return newAirportEntity;
            
            return null;
        }
    }

    @Override
    public String getAsString(FacesContext fc, UIComponent uic, Object object) {
//        System.out.println("converter getAsString object = " + object);
        if (object != null) {    
            return String.valueOf(((TicketEntity) object).getId());
        } else {
            return "";
        }
    }
}
