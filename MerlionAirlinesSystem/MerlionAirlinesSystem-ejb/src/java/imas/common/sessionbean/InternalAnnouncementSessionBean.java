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
        
        List<StaffEntity> staffs = filterStaffList(departments, bases, allStaff);
        
        for(StaffEntity s : staffs){
            InternalAnnouncementEntity newAnnouncement = new InternalAnnouncementEntity(s, title, content);
            entityManager.persist(newAnnouncement);
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

    
    private List<StaffEntity> filterStaffList(List<String> department, List<AirportEntity> base, List<StaffEntity> staffs) {
        List<StaffEntity> filteredStaff = new ArrayList<>();
        for(int i=0; i<staffs.size(); i++){
            StaffEntity staff = staffs.get(i);
            if(department.contains(staff.getRole().getBusinessUnit())){
                if(staff.getBase() != null){
                    if(base.contains(staff.getBase())){
                        filteredStaff.add(staff);
                    }
                }else{
                    filteredStaff.add(staff);
                }
            }
        }
        return filteredStaff;
    }

}
