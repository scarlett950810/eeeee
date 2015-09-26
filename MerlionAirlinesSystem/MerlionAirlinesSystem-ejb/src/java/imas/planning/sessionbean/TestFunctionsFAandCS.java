/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package imas.planning.sessionbean;

import imas.common.entity.PilotEntity;
import imas.planning.entity.FlightEntity;
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
public class TestFunctionsFAandCS implements TestFunctionsFAandCSLocal {
   @PersistenceContext
    private EntityManager em;
    @Override
    public List<FlightEntity> getAllFlights() {
        
       Query q = em.createQuery("SELECT a FROM FlightEntity a");
       
        return (List<FlightEntity>)q.getResultList();
    }
    
    // Add business logic below. (Right-click in editor and choose
    // "Insert Code > Add Business Method")

    @Override
    public List<PilotEntity> getAllPilots() {
       Query q = em.createQuery("SELECT a FROM PilotEntity a");
       
        return (List<PilotEntity>)q.getResultList();
    }
}
