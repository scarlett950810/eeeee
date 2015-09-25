/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package imas.operation.sessionbean;

import imas.planning.entity.AircraftEntity;
import imas.planning.entity.FlightEntity;
import imas.planning.entity.RouteEntity;
import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

/**
 *
 * @author Scarlett
 */
@Stateless
public class DisplayFlightsSessionBean implements DisplayFlightsSessionBeanLocal {
    @PersistenceContext
    private EntityManager em;

    @Override
    public List<RouteEntity> getAllRoutes() {
        Query q = em.createQuery("SELECT r FROM RouteEntity r");
        return q.getResultList();
    }
    
    @Override
    public List<FlightEntity> getFlightsUnderRoute(RouteEntity route) {
        Query q = em.createQuery("SELECT f FROM FlightEntity f WHERE f.route = :route");
        q.setParameter("route", route);
        return q.getResultList();
    }

    @Override
    public List<AircraftEntity> getAllAircrafts() {
        Query q = em.createQuery("SELECT a FROM AircraftEntity a");
        return q.getResultList();
    }
    
    @Override
    public List<FlightEntity> getFlightsUnderAircraft(AircraftEntity aircraft) {
        Query q = em.createQuery("SELECT f FROM FlightEntity f WHERE f.aircraft = :aircraft");
        q.setParameter("aircraft", aircraft);
        return q.getResultList();
    }
    
}
