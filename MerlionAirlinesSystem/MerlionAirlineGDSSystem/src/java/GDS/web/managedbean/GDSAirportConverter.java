/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package GDS.web.managedbean;

import GDS.entity.GDSAirportEntity;
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
@FacesConverter(value = "gDSAirportConverter")
public class GDSAirportConverter implements Converter {

    @Override
    public Object getAsObject(FacesContext fc, UIComponent uic, String value) {

        if (value != null && value.trim().length() > 0) {
            try {

                List<GDSAirportEntity> gDSAirportEntities = (List<GDSAirportEntity>) fc.getExternalContext().getSessionMap().get("GDSAirportList");

                Long gDSAirportEntityId = Long.valueOf(Long.parseLong(value));

                for (GDSAirportEntity gDSAirportEntity : gDSAirportEntities) {
                    if (gDSAirportEntity.getId().equals(gDSAirportEntityId)) {
                        return gDSAirportEntity;
                    }
                }

                return null;
            } catch (NumberFormatException e) {
                throw new ConverterException(new FacesMessage(FacesMessage.SEVERITY_ERROR, "Conversion Error", "Not a valid GDS Airport Entity."));
            }
        } else {
            return null;
        }

    }

    @Override
    public String getAsString(FacesContext fc, UIComponent uic, Object object) {
        if (object != null) {
            String str = String.valueOf(((GDSAirportEntity) object).getId());
            return str;
        } else {
            return null;
        }
    }

}
