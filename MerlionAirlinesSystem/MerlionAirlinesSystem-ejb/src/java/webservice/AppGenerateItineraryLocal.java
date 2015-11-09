/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package webservice;

import imas.distribution.entity.TicketEntity;
import imas.planning.entity.FlightEntity;
import javax.ejb.Local;

/**
 *
 * @author ruicai
 */
@Local
public interface AppGenerateItineraryLocal {

    FlightEntity findFlightById(long id);

    TicketEntity findTicketById(long id);
    
}
