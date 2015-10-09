/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package imas.distribution.sessionbean;

import imas.distribution.entity.PNREntity;
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
public class ModifyBookingSessionBean implements ModifyBookingSessionBeanLocal {

    @PersistenceContext
    private EntityManager entityManager;
    
    @Override
    public PNREntity retrievePNRRecord(String referenceNumber) {
        Query query = entityManager.createQuery("SELECT p FROM PNREntity p WHERE p.referenceNumber= :referenceNumber");
        query.setParameter("referenceNumber", referenceNumber);
        List<PNREntity> PNRList = (List<PNREntity>) query.getResultList();
        if(PNRList.isEmpty()){
            return null;
        }else{
            return PNRList.get(0);
        }
        
    }

    
}
