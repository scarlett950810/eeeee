/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package imas.operation.sessionbean;

import imas.common.entity.CabinCrewEntity;
import imas.common.entity.PilotEntity;
import imas.planning.entity.FlightEntity;
import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

/**
 *
 * @author Howard
 */
@Stateless
public class CrewCheckInSessionBean implements CrewCheckInSessionBeanLocal {
    
    @PersistenceContext
    private EntityManager em;
    
    @Override
    public List<FlightEntity> fetchFlights(String base) {
        Query query = em.createQuery("SELECT f FROM FlightEntity f WHERE f.route.originAirport.airportCode = :base");
        query.setParameter("base", base);
        
        List<FlightEntity> flights = (List<FlightEntity>)query.getResultList();
        
        if(flights.isEmpty()){
            return null;
        }else{
            return flights;  
        }
    }

    @Override
    public void doCrewCheckIn(List<PilotEntity> pilots, List<CabinCrewEntity> cabinCrew) {
        
        String workingStatus = "checked-in";
        for(int i=0; i<pilots.size();i++){
            pilots.get(i).setWorkingStatus(workingStatus);
            em.merge(pilots.get(i));
        }
        for(int j=0; j<cabinCrew.size(); j++){
            cabinCrew.get(j).setWorkingStatus(workingStatus);
            em.merge(cabinCrew.get(j));
        }
        
    }

    @Override
    public void doCrewBoarding(List<PilotEntity> pilots, List<CabinCrewEntity> cabinCrew) {
        String workingStatus = "in flight";
        for(int i=0; i<pilots.size();i++){
            pilots.get(i).setWorkingStatus(workingStatus);
            em.merge(pilots.get(i));
        }
        for(int j=0; j<cabinCrew.size(); j++){
            cabinCrew.get(j).setWorkingStatus(workingStatus);
            em.merge(cabinCrew.get(j));
        }
    }

    
    
}
