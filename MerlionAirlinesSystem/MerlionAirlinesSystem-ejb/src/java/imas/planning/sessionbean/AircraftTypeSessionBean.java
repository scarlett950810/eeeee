/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package imas.planning.sessionbean;

import imas.planning.entity.AircraftEntity;
import imas.planning.entity.AircraftTypeEntity;
import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

/**
 *
 * @author wutong
 */
@Stateless
public class AircraftTypeSessionBean implements AircraftTypeSessionBeanLocal {

    @PersistenceContext
    private EntityManager em;

    @Override
    public List<AircraftTypeEntity> getAllAircraftTypes() {
        Query query = em.createQuery("SELECT a FROM AircraftTypeEntity a");
        List<AircraftTypeEntity> aircraftTypes = (List<AircraftTypeEntity>) query.getResultList();
        return aircraftTypes;
    }

    @Override
    public void updateAircraftType(AircraftTypeEntity aircraftType) {
        Query query = em.createQuery("SELECT a FROM AircraftTypeEntity a WHERE a.id = :id");
        query.setParameter("id", aircraftType.getId());
        AircraftTypeEntity aircraftTypeOri = (AircraftTypeEntity) query.getSingleResult();
        aircraftTypeOri.setIATACode(aircraftType.getIATACode());
        //      System.out.println("Distance" + route.getDistance());
        aircraftTypeOri.setAircraftSpace(aircraftType.getAircraftSpace());
        //     System.out.println("before persist");
        aircraftTypeOri.setMaintenanceHoursRequiredACheck(aircraftType.getMaintenanceHoursRequiredACheck());
        aircraftTypeOri.setCruisingSpeed(aircraftType.getCruisingSpeed());
        aircraftTypeOri.setAircraftRange(aircraftType.getAircraftRange());
    }
    // Add business logic below. (Right-click in editor and choose
    // "Insert Code > Add Business Method")

    @Override
    public Boolean checkAircraftType(String IATACode) {
        Query query = em.createQuery("SELECT a FROM AircraftTypeEntity a WHERE a.IATACode = :IATACode");
        query.setParameter("IATACode", IATACode);

        List<AircraftTypeEntity> types = (List<AircraftTypeEntity>) query.getResultList();
        if (types.isEmpty()) {
            System.out.print("true");
            return true;
        } else {
            System.out.print("false");
            return false;

        }
    }

    @Override
    public void addAircraftType(AircraftTypeEntity aircraftType) {
        em.persist(aircraftType);
    }

    @Override
    public Boolean deleteAircraftType(String IATACode) {
//        System.err.println("进入了"+ IATACode);
        Query query = em.createQuery("SELECT a FROM AircraftTypeEntity a WHERE a.IATACode = :IATACode");
        query.setParameter("IATACode",IATACode);
        AircraftTypeEntity a = (AircraftTypeEntity) query.getSingleResult();
        Query q = em.createQuery("SELECT b FROM AircraftEntity b WHERE b.aircraftType = :a");
        q.setParameter("a", a);
        if(q.getResultList().isEmpty()){
            em.remove(a);
            
            return true;
        }
        else
            return false;
    }

    @Override
    public List<AircraftEntity> getAircraftsFromAircraftType(AircraftTypeEntity aircraftType) {
        Query query = em.createQuery("SELECT a FROM AircraftEntity a WHERE a.aircraftType = :aircraftType");
        query.setParameter("aircraftType", aircraftType);
        List<AircraftEntity> aircraftGroups = (List<AircraftEntity>) query.getResultList();
        
        return aircraftGroups;
    }
    
    
    
    
}
