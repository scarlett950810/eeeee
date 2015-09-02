/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package imas.planning.sessionbean;

import imas.planning.entity.AircraftGroupEntity;
import imas.planning.entity.AirportEntity;
import java.util.ArrayList;
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
public class AircraftSessionBean implements AircraftSessionBeanLocal {

    @PersistenceContext
    private EntityManager em;

    @Override
    public List<AircraftGroupEntity> getAircraftGroups() {
        Query query = em.createQuery("SELECT a FROM AircraftGroupEntity a");
        List<AircraftGroupEntity> aircraftGroups = (List<AircraftGroupEntity>) query.getResultList();
        return aircraftGroups;
    }

    @Override
    public List<AirportEntity> getAirports() {
        System.out.print("AircraftSessionBean.getAirports called.");
        Query query = em.createQuery("SELECT a FROM AirportEntity a");
        List<AirportEntity> airports = (List<AirportEntity>) query.getResultList();
        System.out.print(airports);
        return airports;
    }
    
    @Override
    public List<String> getSeatClasses() {
        List<String> seatClasses = new ArrayList();
        seatClasses.add("First Class");
        seatClasses.add("Business Class");
        seatClasses.add("Premuim Economy Class");
        seatClasses.add("Economy Class");

        return seatClasses;
    }
}
