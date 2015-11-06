/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package webservice;

import imas.planning.entity.FlightEntity;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

/**
 *
 * @author ruicai
 */
@Stateless
public class AppGenerateItinerary implements AppGenerateItineraryLocal {
    
    @PersistenceContext
    private EntityManager em;
    @Override
    public FlightEntity findFlightById(long id) {
        
        FlightEntity f = em.find(FlightEntity.class, id);
        return f;
    }
    
    

    // Add business logic below. (Right-click in editor and choose
    // "Insert Code > Add Business Method")
}
