/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package imas.common.sessionbean;

import imas.common.entity.InternalAnnouncementEntity;
import imas.common.entity.StaffEntity;
import imas.inventory.entity.BookingClassEntity;
import imas.planning.entity.AircraftEntity;
import imas.planning.entity.AircraftGroupEntity;
import imas.planning.entity.AircraftTypeEntity;
import imas.planning.entity.AirportEntity;
import imas.planning.entity.FlightEntity;
import imas.planning.entity.RouteEntity;
import imas.planning.sessionbean.AircraftGroupSessionBeanLocal;
import imas.planning.sessionbean.AircraftSessionBeanLocal;
import imas.planning.sessionbean.AirportSessionBeanLocal;
import imas.planning.sessionbean.RouteSessionBeanLocal;
import java.util.ArrayList;
import javax.ejb.Stateful;
import java.util.Date;
import java.util.List;
import java.util.Calendar;
import javax.ejb.EJB;
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
                ArrayList<Date> tempDate = (ArrayList<Date>) tempStaff.getLoginAttempt();
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
                    pastDate = new Date(pastDate.getTime() + (1000 * 60 * 60 * 24));
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
    
    @EJB
    private AirportSessionBeanLocal airportSessionBean;
    @EJB
    private AircraftGroupSessionBeanLocal aircraftGroupSessionBean;
    @EJB
    private AircraftSessionBeanLocal aircraftSessionBean;
    @EJB
    private RouteSessionBeanLocal routeSessionBean;
    
    @Override
    public void insertData() {
//        System.out.println("Sessionbena called.");
        StaffEntity s = new StaffEntity("1", "DY", "1", "scarlett.dongyan@gmail.com", "84316002", "admin", "Thall", "F", "SIN");
        entityManager.persist(s);

        InternalAnnouncementEntity i1 = new InternalAnnouncementEntity(s, "read message", "hello. This message is read.");
        i1.setIsRead(true);
        entityManager.persist(i1);

        InternalAnnouncementEntity i2 = new InternalAnnouncementEntity(s, "unread message", "An unread message.");
        entityManager.persist(i2);
        
        AircraftTypeEntity aircraftType1 = new AircraftTypeEntity("A380", 10, 50, (double) 100000, (double) 200, (double) 3000, (double) 4400, (double) 20, "Gas");
        AircraftTypeEntity aircraftType2 = new AircraftTypeEntity("A880", 20, 80, (double) 180000, (double) 200, (double) 3800, (double) 6400, (double) 28, "Gas");
        entityManager.persist(aircraftType1);
        entityManager.persist(aircraftType2);
        
        System.out.println("safe 1");

        AirportEntity a1 = new AirportEntity(false, "Shijiazhuang", "ZD Airport", "SJZ", "China");
        AirportEntity a2 = new AirportEntity(true, "Guangzhou", "Baiyun Airport", "CAN", "China");
        AirportEntity a3 = new AirportEntity(true, "Beijing", "BJ International Airport", "BJIA", "China");
        airportSessionBean.addAirport(a1);
        airportSessionBean.addAirport(a2);
        airportSessionBean.addAirport(a3);

        System.out.println("safe 2");

        aircraftGroupSessionBean.addAircraftGroup("A380");
        aircraftGroupSessionBean.addAircraftGroup("A880");

        System.out.println("safe 3");

        Query queryForAircraftType = entityManager.createQuery("select at from AircraftTypeEntity at");
        AircraftTypeEntity at1 = (AircraftTypeEntity) queryForAircraftType.getResultList().get(0);
        Query queryForAircraftGroup = entityManager.createQuery("select at from AircraftGroupEntity at");
        AircraftGroupEntity ag1 = (AircraftGroupEntity) queryForAircraftGroup.getResultList().get(0);

        System.out.println("safe 4");

        aircraftSessionBean.addAircraft("AAA", at1, (double) 20000000, (double) 1000000, (double) 19000000, (double) 20, (double) 0, "All is well", a2, a1, ag1, 4, 5, 4, 6, 6, 10, 7, 50);
        aircraftSessionBean.addAircraft("AAB", at1, (double) 20000000, (double) 1000000, (double) 19000000, (double) 18, (double) 0, "All is well", a3, a2, ag1, 4, 5, 4, 6, 6, 30, 7, 30);
        aircraftSessionBean.addAircraft("AAC", at1, (double) 40000000, (double) 1000000, (double) 39000000, (double) 30, (double) 0, "All is well", a3, a2, ag1, 0, 0, 4, 6, 6, 30, 7, 50);

        System.out.println("safe 5");

        routeSessionBean.addRoute(a1, a3);
        routeSessionBean.addRoute(a2, a3);
        routeSessionBean.addRoute(a3, a2);

        System.out.println("safe 6");

        Query queryForRoute = entityManager.createQuery("select at from RouteEntity at");
        RouteEntity r1 = (RouteEntity) queryForRoute.getResultList().get(0);
        RouteEntity r2 = (RouteEntity) queryForRoute.getResultList().get(1);
        Query queryForAircraft = entityManager.createQuery("select at from AircraftEntity at");
        AircraftEntity aircraft1 = (AircraftEntity) queryForAircraft.getResultList().get(0);
        FlightEntity fe1 = new FlightEntity("FlightNo1", (double) 800000, 3.5, aircraft1, r1);
        FlightEntity fe2 = new FlightEntity("FlightNo2", (double) 800000, 3.5, aircraft1, r2);
        entityManager.persist(fe1);
        entityManager.persist(fe2);
        
        BookingClassEntity bc1 = new BookingClassEntity(fe1, "First Class", "First Class", 3000, 20);
        BookingClassEntity bc2 = new BookingClassEntity(fe1, "Business Class", "Business Class", 2000, 24);
        BookingClassEntity bc3 = new BookingClassEntity(fe1, "Premium Economy Class", "Premium Economy Class", 1000, 60);
        BookingClassEntity bc4 = new BookingClassEntity(fe1, "Economy Class", "Economy Class 1", 900, 200);
        BookingClassEntity bc5 = new BookingClassEntity(fe1, "Economy Class", "Economy Class 2", 600, 200);
        BookingClassEntity bc6 = new BookingClassEntity(fe1, "Economy Class", "Economy Class 3", 500, 200);
        BookingClassEntity bc7 = new BookingClassEntity().BusinessClassBookingClassEntity(fe2, 2200, 24);
        entityManager.persist(bc1);
        entityManager.persist(bc2);
        entityManager.persist(bc3);
        entityManager.persist(bc4);
        entityManager.persist(bc5);
        entityManager.persist(bc6);
        entityManager.persist(bc7);
     }
        
    @Override
    public StaffEntity fetchStaff(String staffNo) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

}
