/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package webservice;

import imas.planning.entity.AirportEntity;
import imas.planning.entity.FlightEntity;
import java.util.Date;
import java.util.List;
import javax.ejb.Local;

/**
 *
 * @author ruicai
 */
@Local
public interface AppFlightLookupLocal {
    AirportEntity getOneAirportFromCityName(String cityName);
    List<FlightEntity> getPromotedFlights(AirportEntity origin,Date lowerBound, Date upperBound );
}
