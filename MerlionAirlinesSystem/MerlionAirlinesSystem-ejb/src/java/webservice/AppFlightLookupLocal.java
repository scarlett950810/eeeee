/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package webservice;

import imas.planning.entity.AirportEntity;
import javax.ejb.Local;

/**
 *
 * @author ruicai
 */
@Local
public interface AppFlightLookupLocal {
    AirportEntity getOneAirportFromCityName(String cityName);
    
}
