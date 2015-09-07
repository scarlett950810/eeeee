/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package imas.planning.sessionbean;

import imas.planning.entity.AircraftEntity;
import imas.planning.entity.AircraftGroupEntity;
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
public class AircraftGroupSessionBean implements AircraftGroupSessionBeanLocal {

    @PersistenceContext
    private EntityManager em;

    @Override
    public void addAircraftGroup(String type) {
        AircraftGroupEntity newAircraftGroup = new AircraftGroupEntity(type);
        em.persist(newAircraftGroup);
    }

    @Override
    public boolean checkAircraftGroupExistense(String type) {
        Query q = em.createQuery("SELECT a FROM AircraftGroupEntity a WHERE a.type = :type");
        q.setParameter("type", type);
        return !q.getResultList().isEmpty();
    }

    @Override
    public List<AircraftGroupEntity> getAircraftGroups() {
        Query query = em.createQuery("SELECT a FROM AircraftGroupEntity a");
        List<AircraftGroupEntity> aircraftGroups = (List<AircraftGroupEntity>) query.getResultList();

        return aircraftGroups;
    }

    @Override
    public List<AircraftEntity> getAircraftsFromGroup(AircraftGroupEntity aircraftGroup) {
        Query query = em.createQuery("SELECT a FROM AircraftEntity a WHERE a.aircraftGroup = :aircraftGroup");
        query.setParameter("aircraftGroup", aircraftGroup);
        List<AircraftEntity> aircraftGroups = (List<AircraftEntity>) query.getResultList();
        
        return aircraftGroups;
    }

    @Override
    public void aircraftDetachGroup(AircraftEntity aircraft) {
        AircraftEntity aircraftToUpdate = em.find(AircraftEntity.class, aircraft.getId());
        aircraftToUpdate.setAircraftGroup(null);
    }
    
    @Override
    public void aircraftChangeGroup(AircraftEntity aircraft, AircraftGroupEntity groupToUpdate) {
        AircraftEntity aircraftToUpdate = em.find(AircraftEntity.class, aircraft.getId());
        aircraftToUpdate.setAircraftGroup(groupToUpdate);
    }
    
    

}
