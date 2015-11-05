/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package webservice;

import imas.planning.entity.AirportEntity;
import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

/**
 *
 * @author ruicai
 */
@Stateless
public class AppFlightLookup implements AppFlightLookupLocal {
    @PersistenceContext
    private EntityManager em;
    // Add business logic below. (Right-click in editor and choose
    // "Insert Code > Add Business Method")
    
    public AirportEntity getOneAirportFromCityName(String cityName){
        Query q = em.createQuery("SELECT a FROM AirportEntity a WHERE a.cityName = :city");
        q.setParameter("city", cityName);
        List<AirportEntity> airportList = (List<AirportEntity>)q.getResultList();
        
        if(airportList == null)
            return null;
        else
            return airportList.get(0);
    }
}
