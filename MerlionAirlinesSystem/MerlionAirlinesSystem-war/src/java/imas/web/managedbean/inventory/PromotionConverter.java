/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package imas.web.managedbean.inventory;

import imas.inventory.entity.PromotionEntity;
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
@FacesConverter("promotionConverter")
public class PromotionConverter implements Converter {
    
    @Override
    public Object getAsObject(FacesContext fc, UIComponent uic, String value) {
        if (value != null && value.trim().length() > 0) {
            try {
                
                List<PromotionEntity> bookingClassEntities = (List<PromotionEntity>)fc.getExternalContext().getSessionMap().get("PromotionList");
                
                Long bookingClassEntityId = Long.valueOf(Long.parseLong(value));
                
                for(PromotionEntity bookingClassEntity:bookingClassEntities)
                {
                    if(bookingClassEntity.getId().equals(bookingClassEntityId))
                    {
                        return bookingClassEntity;
                    }
                }
                
                return null;
            } catch (NumberFormatException e) {
                throw new ConverterException(new FacesMessage(FacesMessage.SEVERITY_ERROR, "Conversion Error", "Not a valid promotion."));
            }
        } else {
            return null;
        }}
 

    @Override
    public String getAsString(FacesContext fc, UIComponent uic, Object object) {
        if (object != null) {    
            return String.valueOf(((PromotionEntity) object).getId());
        } else {
            return null;
        }
    }
    
} 
