/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package imas.common.sessionbean;

import imas.common.entity.InternalAnnouncementEntity;
import imas.common.entity.StaffEntity;
import javax.ejb.Stateful;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
/**
 *
 * @author Howard
 */
@Stateful
public class LoginSessionBean implements LoginSessionBeanLocal {                

    @PersistenceContext
    private EntityManager entityManager;
    
    /**
     *
     * @param staffNo
     * @param password
     * @return staffId
     */
    @Override
    public StaffEntity doLogin(String staffNo, String password) {
        /*String url = "jdbc:mysql://localhost:3307/MerlionInternal";
        String username = "root";
        String password = "1234";

        System.out.println("Connecting database…");

        try (Connection connection = DriverManager.getConnection(url, username, password)) {
            System.out.println("Database connected!");
            return true;
        }
        catch (SQLException e) {
            throw new IllegalStateException("Cannot connect the database!", e);
        }
        */
//        insertdata();
        
        Query query = entityManager.createQuery("SELECT s FROM StaffEntity s WHERE s.staffNo = :staffNumber AND s.password = :password");
        query.setParameter("staffNumber", staffNo);
        query.setParameter("password", password);
        
        List<StaffEntity> staff = (List<StaffEntity>)query.getResultList();
        if(!staff.isEmpty()){
            return (StaffEntity) staff.get(0);
        }else{
            return null;
        }
        
    
    }
    
    @Override
    public List<InternalAnnouncementEntity> getAllAnnoucements(StaffEntity staffEntity) {
        System.out.print(staffEntity);
        Query queryForAllAnnoucements = entityManager.createQuery("SELECT a FROM InternalAnnouncementEntity a WHERE a.receiver = :staffEntity");
        queryForAllAnnoucements.setParameter("staffEntity", (StaffEntity) staffEntity);
        
        System.out.print("loginSessionBean.getAllAnnoucements called");
        System.out.print(queryForAllAnnoucements.getResultList());
        List<InternalAnnouncementEntity> announcements = new ArrayList();
        queryForAllAnnoucements.getResultList().stream().forEach((o) -> {
            InternalAnnouncementEntity a = (InternalAnnouncementEntity) o;
            announcements.add(a);
        });
        return (List<InternalAnnouncementEntity>) announcements;
    }
    
    @Override
    public List<InternalAnnouncementEntity> getUnreadAnnoucements(StaffEntity staffEntity) {
        System.out.print(staffEntity);
        Query queryForUnreadAnnoucements = entityManager.createQuery("SELECT a FROM InternalAnnouncementEntity a WHERE a.receiver = :staffEntity AND a.isRead = :isRead");
        queryForUnreadAnnoucements.setParameter("staffEntity", (StaffEntity) staffEntity);
        queryForUnreadAnnoucements.setParameter("isRead", false);
        
        System.out.print("loginSessionBean.getUnreadAnnoucements called");
        System.out.print(queryForUnreadAnnoucements.getResultList().size());
        List<InternalAnnouncementEntity> unreadAnnouncements = new ArrayList();
        queryForUnreadAnnoucements.getResultList().stream().map((o) -> (InternalAnnouncementEntity) o).map((a) -> {
            System.out.print(a);
            return a;
        }).forEach((a) -> {
            unreadAnnouncements.add((InternalAnnouncementEntity) a);
        });
        return (List<InternalAnnouncementEntity>) unreadAnnouncements;
    }

//    private void insertdata() {
//        StaffEntity s = new StaffEntity("1", "DY", "1", "scarlett.dongyan@gmail.com", "84316002", "admin");
//        entityManager.persist(s);
//        
//        InternalAnnouncementEntity i1 = new InternalAnnouncementEntity(s, "hello. This message is read.");
//        i1.setRead(true);
//        entityManager.persist(i1);
//        
//        InternalAnnouncementEntity i2 = new InternalAnnouncementEntity(s, "An unread message.");
//        entityManager.persist(i2);
//    }
}
