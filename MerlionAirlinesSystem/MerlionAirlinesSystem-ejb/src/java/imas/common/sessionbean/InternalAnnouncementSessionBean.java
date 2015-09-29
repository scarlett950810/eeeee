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
public class InternalAnnouncementSessionBean implements InternalAnnouncementSessionBeanLocal {

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public List<InternalAnnouncementEntity> getAllAnnouncements(String staffNo) {
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

            return (List<InternalAnnouncementEntity>) announcements;
        }
        return null;
    }

    @Override
    public String sendInternalAnnouncements(List<String> departments, List<AirportEntity> bases, String title, String content) {

        Query queryForAllStaff = entityManager.createQuery("SELECT s FROM StaffEntity s");
        List<StaffEntity> allStaff = queryForAllStaff.getResultList();
        for (StaffEntity s : allStaff) {
            if (departments.contains(s.getRole().getBusinessUnit()) || bases.contains(s.getBase())) {
                InternalAnnouncementEntity newAnnouncement = new InternalAnnouncementEntity(s, title, content);
                entityManager.persist(newAnnouncement);
            }
        }
        return "Message has been sent to all selected staff.";

    }

    @Override
    public void toggleRead(InternalAnnouncementEntity internalAnnouncementEntity) {
        InternalAnnouncementEntity internalAnnouncementEntityToUpdate = entityManager.find(InternalAnnouncementEntity.class, internalAnnouncementEntity.getId());
        if (internalAnnouncementEntityToUpdate.isIsRead()) {
            internalAnnouncementEntityToUpdate.setIsRead(false);
        } else {
            internalAnnouncementEntityToUpdate.setIsRead(true);
        }
    }

}
