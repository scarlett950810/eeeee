/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package imas.planning.sessionbean;

import imas.planning.entity.AirportEntity;
import java.util.List;
import javax.ejb.Local;

/**
 *
 * @author Howard
 */
@Local
public interface AirportSessionBeanLocal {

    void addAirport(AirportEntity airport);

    Boolean checkAirport(String airportCode);

    Boolean deleteAirport(String airportCode);

    List<AirportEntity> fetchAirport();

    void updateAirport(Boolean hubOrSpoke, String cityName, String airportName, String airportCode);

    AirportEntity getAirport(String airportCode);
    
}
