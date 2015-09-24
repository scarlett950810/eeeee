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
import javax.ejb.EJB;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import util.security.CryptographicHelper;

/**
 *
 * @author Howard
 */
@Stateful
public class LoginSessionBean implements LoginSessionBeanLocal {

    @PersistenceContext
    private EntityManager entityManager;
    CryptographicHelper cp = new CryptographicHelper();

    public LoginSessionBean() {
    }

    /**
     *
     * @param staffNo
     * @param password
     * @return staffId
     */
    @Override
    public String doLogin(String staffNo, String oldpassword) {

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
                String salt = tempStaff.getSalt();
                String password = cp.doMD5Hashing(oldpassword + salt);
                if (tempDate == null || tempDate.isEmpty()) {
                    if (password.equals(tempStaff.getPassword())) {
                        tempStaff.setLoginAttempt(null);
                        entityManager.merge(tempStaff);
                        System.out.println("success1");
                        return "success";
                    } else {
                        tempDate = new ArrayList<Date>();
                        tempDate.add(currentDate);
                        tempStaff.setLoginAttempt(tempDate);
                        entityManager.merge(tempStaff);
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
                            entityManager.merge(tempStaff);
                            System.out.println("success2");
                            return "success";
                        } else {
                            tempDate.add(currentDate);
                            tempStaff.setLoginAttempt(tempDate);
                            entityManager.merge(tempStaff);
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
                                entityManager.merge(tempStaff);
                                System.out.println("success3");
                                return "success";
                            } else {
                                if (tempStaff.getLoginAttempt().size() >= 3) {

                                    tempStaff.setLoginAttempt(tempDate);
                                    entityManager.merge(tempStaff);
                                    System.out.println("captcha");
                                    return "captcha";
                                } else {

                                    tempStaff.setLoginAttempt(tempDate);
                                    entityManager.merge(tempStaff);
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
        AircraftTypeEntity aircraftType1 = new AircraftTypeEntity("A380", (double)10000, 50, (double) 100000, (double) 600, (double) 3000, (double) 4400, (double) 20, "Gas", (double) 1000);
        AircraftTypeEntity aircraftType2 = new AircraftTypeEntity("A880", (double)5000, 80, (double) 180000, (double) 450, (double) 3800, (double) 6400, (double) 28, "Gas", (double) 800);
        AircraftTypeEntity aircraftType3 = new AircraftTypeEntity("B777", (double)10000, 80, (double) 180000, (double) 550, (double) 3800, (double) 6400, (double) 28, "Gas", (double) 1000);
        AircraftTypeEntity aircraftType4 = new AircraftTypeEntity("B787", (double)12000, 80, (double) 180000, (double) 600, (double) 3800, (double) 6400, (double) 28, "Gas", (double) 900);
        AircraftTypeEntity aircraftType5 = new AircraftTypeEntity("B700", (double)8000, 80, (double) 180000, (double) 580, (double) 3800, (double) 6400, (double) 28, "Gas", (double) 900);
        entityManager.persist(aircraftType1);
        entityManager.persist(aircraftType2);
        entityManager.persist(aircraftType3);
        entityManager.persist(aircraftType4);
        entityManager.persist(aircraftType5);

        
        System.out.println("aircraftTypes added");

        AirportEntity a1 = new AirportEntity(false, "Shijiazhuang", "ZD Airport", "SJZ", "China");
        AirportEntity a2 = new AirportEntity(false, "Guangzhou", "Baiyun Airport", "CAN", "China");
        AirportEntity a3 = new AirportEntity(false, "Beijing", "BJ International Airport", "BJIA", "China");
        AirportEntity a4 = new AirportEntity(true, "Singapore", "Changi Airport", "SGC", "Singapore");
        AirportEntity a5 = new AirportEntity(true, "Tokyo", "Natita Airport", "TNA", "Japan");
        AirportEntity a6 = new AirportEntity(false, "New York", "Lincoln Airport", "NYL", "U.S.");
        AirportEntity a7 = new AirportEntity(true, "Shanghai", "Pu Dong Airport", "SHP", "China");
        AirportEntity a8 = new AirportEntity(true, "Hong Kong", "HK International Airport", "HKI", "Hong Kong, China");
        AirportEntity a9 = new AirportEntity(false, "Taipei", "Tao Yuan Airport", "TPT", "Taiwan, China");
        AirportEntity a10 = new AirportEntity(false, "Syndney", "Syndney Opera Airport", "SOA", "Australia");
        AirportEntity a11 = new AirportEntity(false, "Melborne", "Melborne International Airport", "MIA", "Australia");
        AirportEntity a12 = new AirportEntity(false, "Hainan", "Hainan Airport", "HAT", "China");
        AirportEntity a13 = new AirportEntity(false, "Kuala Lumpur", "KL Airport", "KLA", "Malaysia");
        AirportEntity a14 = new AirportEntity(false, "London", "London International Airport", "LTA", "U.K.");
        AirportEntity a15 = new AirportEntity(true, "Los Angeles", "Los Angeles International Airport", "LAI", "U.S.");
        AirportEntity a16 = new AirportEntity(false, "Seoul", "Seoul International Airport", "SLA", "Korea");
        AirportEntity a17 = new AirportEntity(false, "Chengdu", "Chengdu International Airport", "CDA", "China");
        AirportEntity a18 = new AirportEntity(false, "Bangkok", "Bangkok International Airport", "BIA", "Thailand");
        AirportEntity a19 = new AirportEntity(false, "Paris", "Paris International Airport", "PIA", "France");
        AirportEntity a20 = new AirportEntity(false, "Frankfurt", "Frankfurt International Airport", "FIA", "Germany");

        airportSessionBean.addAirport(a1);
        airportSessionBean.addAirport(a2);
        airportSessionBean.addAirport(a3);
        airportSessionBean.addAirport(a4);
        airportSessionBean.addAirport(a5);
        airportSessionBean.addAirport(a6);
        airportSessionBean.addAirport(a7);
        airportSessionBean.addAirport(a8);
        airportSessionBean.addAirport(a9);
        airportSessionBean.addAirport(a10);
        airportSessionBean.addAirport(a11);
        airportSessionBean.addAirport(a12);
        airportSessionBean.addAirport(a13);
        airportSessionBean.addAirport(a14);
        airportSessionBean.addAirport(a15);
        airportSessionBean.addAirport(a16);
        airportSessionBean.addAirport(a17);
        airportSessionBean.addAirport(a18);
        airportSessionBean.addAirport(a19);
        airportSessionBean.addAirport(a20);

        System.out.println("Airports added");

        aircraftSessionBean.addAircraft("001", aircraftType1, (double) 20000000, (double) 1000000, (double) 19000000, (double) 20, (double) 0, "All is well", a4, a4, 4, 5, 4, 6, 6, 10, 7, 50, (double) 30);
        aircraftSessionBean.addAircraft("002", aircraftType1, (double) 20000000, (double) 1000000, (double) 19000000, (double) 18, (double) 0, "All is well", a8, a8, 4, 5, 4, 6, 6, 30, 7, 30, (double) 35);
        aircraftSessionBean.addAircraft("003", aircraftType1, (double) 20000000, (double) 1000000, (double) 19000000, (double) 20, (double) 0, "All is well", a5, a5, 0, 0, 4, 6, 6, 30, 7, 50, (double) 30);
        aircraftSessionBean.addAircraft("001", aircraftType2, (double) 20000000, (double) 1000000, (double) 19000000, (double) 18, (double) 0, "All is well", a5, a5, 4, 5, 4, 6, 6, 30, 7, 30, (double) 50);
        aircraftSessionBean.addAircraft("002", aircraftType2, (double) 20000000, (double) 1000000, (double) 19000000, (double) 18, (double) 0, "All is well", a7, a7, 4, 5, 4, 6, 6, 30, 7, 30, (double) 47);
        aircraftSessionBean.addAircraft("003", aircraftType2, (double) 20000000, (double) 1000000, (double) 19000000, (double) 20, (double) 0, "All is well", a7, a7, 0, 0, 4, 6, 6, 30, 7, 50, (double) 42);
        aircraftSessionBean.addAircraft("001", aircraftType3, (double) 20000000, (double) 1000000, (double) 19000000, (double) 18, (double) 0, "All is well", a8, a8, 4, 5, 4, 6, 6, 30, 7, 30, (double) 50);
        aircraftSessionBean.addAircraft("002", aircraftType3, (double) 20000000, (double) 1000000, (double) 19000000, (double) 18, (double) 0, "All is well", a15, a15, 4, 5, 4, 6, 6, 30, 7, 30, (double) 47);
        aircraftSessionBean.addAircraft("003", aircraftType3, (double) 20000000, (double) 1000000, (double) 19000000, (double) 20, (double) 0, "All is well", a5, a5, 0, 0, 4, 6, 6, 30, 7, 50, (double) 30);
        aircraftSessionBean.addAircraft("001", aircraftType4, (double) 20000000, (double) 1000000, (double) 19000000, (double) 18, (double) 0, "All is well", a15, a15, 4, 5, 4, 6, 6, 30, 7, 30, (double) 65);
        aircraftSessionBean.addAircraft("002", aircraftType4, (double) 20000000, (double) 1000000, (double) 19000000, (double) 18, (double) 0, "All is well", a7, a7, 4, 5, 4, 6, 6, 30, 7, 30, (double) 37);
        aircraftSessionBean.addAircraft("003", aircraftType4, (double) 20000000, (double) 1000000, (double) 19000000, (double) 20, (double) 0, "All is well", a7, a7, 0, 0, 4, 6, 6, 30, 7, 50, (double) 51);
        aircraftSessionBean.addAircraft("001", aircraftType5, (double) 20000000, (double) 1000000, (double) 19000000, (double) 18, (double) 0, "All is well", a5, a5, 4, 5, 4, 6, 6, 30, 7, 30, (double) 50);
        aircraftSessionBean.addAircraft("002", aircraftType5, (double) 20000000, (double) 1000000, (double) 19000000, (double) 18, (double) 0, "All is well", a8, a8, 4, 5, 4, 6, 6, 30, 7, 30, (double) 47);
        aircraftSessionBean.addAircraft("003", aircraftType5, (double) 20000000, (double) 1000000, (double) 19000000, (double) 20, (double) 0, "All is well", a7, a7, 0, 0, 4, 6, 6, 30, 7, 50, (double) 42);

        System.out.println("aircrafts added");

        //下面都不是我写的，不太知道删与否，你们如果不用了就自己删一下哈：）

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
