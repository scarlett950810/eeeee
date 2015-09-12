/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package imas.common.sessionbean;

import imas.common.entity.InternalAnnouncementEntity;
import imas.common.entity.StaffEntity;
import imas.planning.entity.AircraftGroupEntity;
import imas.planning.entity.AircraftTypeEntity;
import java.util.ArrayList;
import javax.ejb.Stateful;
import java.util.Date;
import java.util.List;
import java.util.Calendar;
import javax.faces.context.FacesContext;
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

    public LoginSessionBean() {
    }

    /**
     *
     * @param staffNo
     * @param password
     * @return staffId
     */
    @Override
    public String doLogin(String staffNo, String password) {

        Query query = entityManager.createQuery("SELECT s FROM StaffEntity s WHERE s.staffNo = :staffNumber");
        query.setParameter("staffNumber", staffNo);
        List<StaffEntity> staffs = (List<StaffEntity>) query.getResultList();

        if (!staffs.isEmpty()) {
            //get same staffNo 
            StaffEntity tempStaff = staffs.get(0);
            //check soft delete
            if (!tempStaff.getDeleteStatus()) {
                Date currentDate = new Date();
                ArrayList<Date> tempDate = tempStaff.getLoginAttempt();
                if (tempDate == null || tempDate.size() == 0) {
                    if (password.equals(tempStaff.getPassword())) {
                        tempStaff.setLoginAttempt(null);
                        entityManager.persist(tempStaff);
                        System.out.println("success1");
                        return "success";
                    } else {
                        tempDate = new ArrayList<Date>();
                        tempDate.add(currentDate);
                        tempStaff.setLoginAttempt(tempDate);
                        entityManager.persist(tempStaff);
                        System.out.println("wrong password1");
                        return "wrong password";
                    }
                } else {

                    Date pastDate = tempDate.get(0);
                    pastDate = new Date(pastDate.getTime() + (1000*60*60*24));
                    int checkOneDay = currentDate.compareTo(pastDate);
                    //more than one day
                    if (checkOneDay >= 0) {
                        tempStaff.setLoginAttempt(null);
                        if (password.equals(tempStaff.getPassword())) {
                            tempStaff.setLoginAttempt(null);
                            entityManager.persist(tempStaff);
                            System.out.println("success2");
                            return "success";
                        } else {
                            tempDate.add(currentDate);
                            tempStaff.setLoginAttempt(tempDate);
                            entityManager.persist(tempStaff);
                            System.out.println("wrong password2");
                            return "wrong password";
                        }
                    } else {
                        tempStaff.getLoginAttempt().add(currentDate);
                        if (tempStaff.getLoginAttempt().size() >= 10) {
                            System.out.println("lock");
                            return "lock";
                        } else {
                            if (password.equals(tempStaff.getPassword())) {
                                tempStaff.setLoginAttempt(null);
                                entityManager.persist(tempStaff);
                                System.out.println("success3");
                                return "success";
                            } else {
                                if (tempStaff.getLoginAttempt().size() >= 3) {
                                    
                                    tempStaff.setLoginAttempt(tempDate);
                                    entityManager.persist(tempStaff);
                                    System.out.println("captcha");
                                    return "captcha";
                                } else {
                                    
                                    tempStaff.setLoginAttempt(tempDate);
                                    entityManager.persist(tempStaff);
                                    System.out.println("wrong password3");
                                    return "wrong password";
                                }
                            }
                        }

                    }
                }

            } else {
                return "delete";
            }

        } else {
            return "empty";
        }

    }
    //    public void setPass(String staffNo, String password) {
    //        long id=111;
    //        StaffEntity newStaff = entityManager.find(StaffEntity.class,id);
    //        newStaff.setPassword(password);
    //    }

    public void insertData() {
        StaffEntity s = new StaffEntity("1", "DY", "1", "scarlett.dongyan@gmail.com", "84316002", "admin", "Thall", "F", "SIN");
        entityManager.persist(s);

        InternalAnnouncementEntity i1 = new InternalAnnouncementEntity(s, "read message", "hello. This message is read.");
        i1.setIsRead(true);
        entityManager.persist(i1);

        InternalAnnouncementEntity i2 = new InternalAnnouncementEntity(s, "unread message", "An unread message.");
        entityManager.persist(i2);
        AircraftGroupEntity group1 = new AircraftGroupEntity("A380");
        AircraftGroupEntity group2 = new AircraftGroupEntity("A880");

        entityManager.persist(group1);
        entityManager.persist(group2);
        AircraftTypeEntity aircraftType1 = new AircraftTypeEntity("A380", 10, 50, (double) 100000, (double) 200, (double) 3000, (double) 4400, (double) 20, "Gas");
        AircraftTypeEntity aircraftType2 = new AircraftTypeEntity("A880", 20, 80, (double) 180000, (double) 200, (double) 3800, (double) 6400, (double) 28, "Gas");
        entityManager.persist(aircraftType1);
        entityManager.persist(aircraftType2);
    }

    @Override
    public StaffEntity fetchStaff(String staffNo) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
}
