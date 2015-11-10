/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package imas.common.sessionbean;

import imas.common.entity.InternalAnnouncementEntity;
import imas.common.entity.StaffEntity;
import imas.planning.entity.AirportEntity;
import java.util.ArrayList;
import java.util.List;
import javax.ejb.Stateful;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

/**
 *
 * @author Scarlett
 */
@Stateful
public class InternalAnnouncementSessionBean implements InternalAnnouncementSessionBeanLocal,InternalAnnouncementSessionBeanRemote{

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public List<InternalAnnouncementEntity> getAllAnnouncements(String staffNo) {
//        System.out.println("get all announcements:");
        Query queryForStaff = entityManager.createQuery("SELECT s FROM StaffEntity s WHERE s.staffNo = :staffNo");
        queryForStaff.setParameter("staffNo", staffNo);
        List<StaffEntity> staffs = (List<StaffEntity>) queryForStaff.getResultList();
        if (!staffs.isEmpty()) {
            StaffEntity staff = (StaffEntity) queryForStaff.getResultList().get(0);
            Query queryForAllAnnoucements = entityManager.createQuery("SELECT a FROM InternalAnnouncementEntity a WHERE a.receiver = :staffEntity");
            queryForAllAnnoucements.setParameter("staffEntity", (StaffEntity) staff);

            List<InternalAnnouncementEntity> announcements = new ArrayList();
            queryForAllAnnoucements.getResultList().stream().forEach((o) -> {
                InternalAnnouncementEntity a = (InternalAnnouncementEntity) o;
                announcements.add(a);
            });
//            System.out.println(announcements);
            return (List<InternalAnnouncementEntity>) announcements;
        }
        return null;
    }

    @Override
    public String sendInternalAnnouncements(List<String> departments, List<AirportEntity> bases, String title, String content) {

        Query queryForAllStaff = entityManager.createQuery("SELECT s FROM StaffEntity s");
        List<StaffEntity> allStaff = queryForAllStaff.getResultList();

        System.out.println("departments: " + departments);
        System.out.println("bases: " + bases);
        
        if (departments.size() > 0 && bases.size() > 0) {
            // filter receiver by department and base
            
            for (StaffEntity s : allStaff) {
                if (s.getBase() != null && s.getRole() != null) {
                    if (departments.contains(s.getRole().getBusinessUnit()) && bases.contains(s.getBase())) {
                        InternalAnnouncementEntity newAnnouncement = new InternalAnnouncementEntity(s, title, content);
                        entityManager.persist(newAnnouncement);
                    }
                }
            }
        } else if (departments.size() > 0) {
            // filter receiver only by departemnt

            for (StaffEntity s : allStaff) {                                        
                if (s.getRole() != null) {
                    if (departments.contains(s.getRole().getBusinessUnit())) {
                        InternalAnnouncementEntity newAnnouncement = new InternalAnnouncementEntity(s, title, content);
                        entityManager.persist(newAnnouncement);
                    }
                }

            }
        } else if (bases.size() > 0) {
            // filter receiver only by base
            
            
            for (StaffEntity s : allStaff) {
                if (s.getBase() != null) {
                    System.out.println("s.getBase() = " + s.getBase());
                    System.out.println("bases.contains(s.getBase()) = " + bases.contains(s.getBase()));
                    
                    if (bases.contains(s.getBase())) {
                        InternalAnnouncementEntity newAnnouncement = new InternalAnnouncementEntity(s, title, content);
                        entityManager.persist(newAnnouncement);
                    }
                }
            }
        } else {
            // no criteria chosen, send to all.
            
            for (StaffEntity s : allStaff) {
                InternalAnnouncementEntity newAnnouncement = new InternalAnnouncementEntity(s, title, content);
                entityManager.persist(newAnnouncement);
            }
        }

        return "Message has been sent to all selected staff.";

    }

    @Override
    public
            void toggleRead(InternalAnnouncementEntity internalAnnouncementEntity) {
        InternalAnnouncementEntity internalAnnouncementEntityToUpdate = entityManager.find(InternalAnnouncementEntity.class, internalAnnouncementEntity.getId());
        if (internalAnnouncementEntityToUpdate.isIsRead()) {
            internalAnnouncementEntityToUpdate.setIsRead(false);
        } else {
            internalAnnouncementEntityToUpdate.setIsRead(true);
        }
    }

}
