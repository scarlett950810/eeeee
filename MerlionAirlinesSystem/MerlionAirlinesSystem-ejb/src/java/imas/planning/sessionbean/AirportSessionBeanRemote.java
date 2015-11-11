/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package imas.planning.sessionbean;

import imas.planning.entity.AirportEntity;
import java.util.List;
import javax.ejb.Remote;

/**
 *
 * @author wutong
 */
@Remote
public interface AirportSessionBeanRemote {

    void addAirport(AirportEntity airport);

    Boolean checkAirport(String airportCode);

    Boolean deleteAirport(String airportCode);

    List<AirportEntity> fetchAirport();

    void updateAirport(Boolean hubOrSpoke, String cityName, String airportName, String airportCode, String nationName, Long airportID);

    AirportEntity getAirport(String airportCode);

    Boolean checkRelatedRoute(AirportEntity a);

    Boolean checkRelatedAircraft(AirportEntity a);
}
