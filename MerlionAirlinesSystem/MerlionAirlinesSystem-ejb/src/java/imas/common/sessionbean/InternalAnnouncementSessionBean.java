/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package imas.common.sessionbean;

import imas.common.entity.InternalAnnouncementEntity;
import imas.common.entity.StaffEntity;
import java.util.ArrayList;
import java.util.List;
import javax.ejb.Stateful;
import javax.faces.context.FacesContext;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

/**
 *
 * @author Scarlett
 */
@Stateful
public class InternalAnnouncementSessionBean implements InternalAnnouncementSessionBeanLocal {

    
    
    @PersistenceContext
    private EntityManager entityManager;
    
    
    @Override
    public List<InternalAnnouncementEntity> getAllAnnouncements() {
        StaffEntity staffEntity = (StaffEntity) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("staffEntity");
        Query queryForAllAnnoucements = entityManager.createQuery("SELECT a FROM InternalAnnouncementEntity a WHERE a.receiver = :staffEntity");
        queryForAllAnnoucements.setParameter("staffEntity", (StaffEntity) staffEntity);
        
        List<InternalAnnouncementEntity> announcements = new ArrayList();
        queryForAllAnnoucements.getResultList().stream().forEach((o) -> {
            InternalAnnouncementEntity a = (InternalAnnouncementEntity) o;
            announcements.add(a);
        });

        return (List<InternalAnnouncementEntity>) announcements;
    }
    
    
}
