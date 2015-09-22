/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package imas.planning.sessionbean;

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
    
    
}
