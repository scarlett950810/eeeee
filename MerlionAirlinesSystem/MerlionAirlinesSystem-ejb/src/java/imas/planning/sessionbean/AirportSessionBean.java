/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package imas.planning.sessionbean;

import imas.planning.entity.AirportEntity;
import java.util.List;
import javax.ejb.Stateful;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;


/**
 *
 * @author Howard
 */
@Stateful
public class AirportSessionBean implements AirportSessionBeanLocal {
    @PersistenceContext
    private EntityManager em;

    public AirportSessionBean() {
    }
    
    @Override
    public void addAirport(AirportEntity airport) {
        em.persist(airport);
    }
    
    
    
//    public void addAirport(String cityName, String airportName, boolean hubOrSpoke,String airportCode) {
//        AirportEntity airport = new AirportEntity(hubOrSpoke, cityName, airportName, airportCode);
//        em.persist(airport);
//    }
    
    // Add business logic below. (Right-click in editor and choose
    // "Insert Code > Add Business Method")

    @Override
    public Boolean checkAirport(String airportCode) {
        Query query = em.createQuery("SELECT a FROM AirportEntity a WHERE a.airportCode = :airportCode");
        query.setParameter("airportCode", airportCode);
        
        List<AirportEntity> airport = (List<AirportEntity>)query.getResultList();
        if(airport.isEmpty()){
            System.out.print("true");
            return true;
        }else{
            System.out.print("false");
            return false;
            
        }
        
    }

    @Override
    public Boolean deleteAirport(String airportCode) {
        
        Query query = em.createQuery("DELETE FROM AirportEntity a WHERE a.airportCode = :airportCode");
        query.setParameter("airportCode", airportCode);
        query.executeUpdate();
        
        return true;
    }

    @Override
    public List<AirportEntity> fetchAirport() {
        Query query = em.createQuery("SELECT a FROM AirportEntity a");
        List<AirportEntity> airport = (List<AirportEntity>)query.getResultList();
        return airport;
    }

    @Override
    public void updateAirport(Boolean hubOrSpoke, String cityName, String airportName, String airportCode) {
        Query query = em.createQuery("SELECT a FROM AirportEntity a WHERE a.airportCode = :airportCode");
        query.setParameter("airportCode", airportCode);
        
        try{
            AirportEntity airport = (AirportEntity)query.getSingleResult();
            if(hubOrSpoke != null){
                airport.setHubOrSpoke(hubOrSpoke);
            }
            if(cityName != null){
                airport.setCityName(cityName);
            }
            if(airportName != null){
                airport.setAirportName(airportName);
            }
            if(airportCode != null){
                airport.setAirportCode(airportCode);
            }
        }catch(NoResultException exception){
            System.out.println("No such airport");
        }
    }

    @Override
    public AirportEntity getAirport(String airportCode) {
        Query query = em.createQuery("SELECT a FROM AirportEntity a WHERE a.airportCode = :airportCode");
        query.setParameter("airportCode", airportCode);
        
        List<AirportEntity> airport = (List<AirportEntity>)query.getResultList();
        if(!airport.isEmpty()){
            return airport.get(0);
        }else{
            return null;
        }
    }
    
    
    
}
