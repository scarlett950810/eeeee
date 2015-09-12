/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package imas.inventory.sessionbean;

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
public class inventoryRevenueManagementSessionBean implements inventoryRevenueManagementSessionBeanLocal {

    @PersistenceContext
    private EntityManager em;
    
    @Override
    public List<FlightEntity> fetchFlight() {
        Query query = em.createQuery("SELECT f FROM FlightEntity f");
        
        List<FlightEntity> flights = (List<FlightEntity>)query.getResultList();
        if(flights.isEmpty()){
            System.out.print("true");
            return null;
        }else{
            System.out.print("false");
            return flights;
            
        }
    }

    
}
