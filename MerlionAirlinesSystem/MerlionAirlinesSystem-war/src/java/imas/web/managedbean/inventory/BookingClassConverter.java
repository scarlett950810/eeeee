/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package imas.web.managedbean.inventory;

import imas.inventory.entity.BookingClassEntity;
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
@FacesConverter("bookingClassConverter")
public class BookingClassConverter implements Converter {
    
    @Override
    public Object getAsObject(FacesContext fc, UIComponent uic, String value) {
        if (value != null && value.trim().length() > 0) {
            try {
                
                List<BookingClassEntity> bookingClassEntities = (List<BookingClassEntity>)fc.getExternalContext().getSessionMap().get("bookingClassList");
                
                Long bookingClassEntityId = Long.valueOf(Long.parseLong(value));
                
                for(BookingClassEntity bookingClassEntity:bookingClassEntities)
                {
                    if(bookingClassEntity.getId().equals(bookingClassEntityId))
                    {
                        return bookingClassEntity;
                    }
                }
                
                return null;
            } catch (NumberFormatException e) {
                throw new ConverterException(new FacesMessage(FacesMessage.SEVERITY_ERROR, "Conversion Error", "Not a valid booking class."));
            }
        } else {
            return null;
        }}
 

    @Override
    public String getAsString(FacesContext fc, UIComponent uic, Object object) {
        if (object != null) {    
            return String.valueOf(((BookingClassEntity) object).getId());
        } else {
            return null;
        }
    }
    
} 
