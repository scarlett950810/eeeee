/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package imas.common.sessionbean;

import imas.common.entity.CabinCrewEntity;
import imas.common.entity.PilotEntity;
import imas.common.entity.StaffEntity;
import imas.inventory.entity.BookingClassEntity;
import imas.planning.entity.AircraftEntity;
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
import javax.annotation.PostConstruct;
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
    private Integer leftChance = 7;

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
        //insertData();

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
//                        System.out.println("success1");
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
                                    leftChance = 10 - tempStaff.getLoginAttempt().size();
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

    @Override
    public Integer getLeftChance() {
        return leftChance;
    }

    
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
        AircraftTypeEntity aircraftType1 = new AircraftTypeEntity("A380", (double) 10000, 50, (double) 100000, (double) 600, (double) 3000, (double) 4400, (double) 20, "Gas", (double) 60);
        AircraftTypeEntity aircraftType2 = new AircraftTypeEntity("A330", (double) 5000, 80, (double) 180000, (double) 450, (double) 3800, (double) 6400, (double) 28, "Gas", (double) 55);
        AircraftTypeEntity aircraftType3 = new AircraftTypeEntity("B777", (double) 10000, 80, (double) 180000, (double) 550, (double) 3800, (double) 6400, (double) 28, "Gas", (double) 65);
        AircraftTypeEntity aircraftType4 = new AircraftTypeEntity("B787", (double) 12000, 80, (double) 180000, (double) 600, (double) 3800, (double) 6400, (double) 28, "Gas", (double) 60);
        AircraftTypeEntity aircraftType5 = new AircraftTypeEntity("B747", (double) 8000, 80, (double) 180000, (double) 580, (double) 3800, (double) 6400, (double) 28, "Gas", (double) 55);
        AircraftTypeEntity aircraftType6 = new AircraftTypeEntity("B737", (double) 12000, 80, (double) 180000, (double) 600, (double) 3800, (double) 6400, (double) 28, "Gas", (double) 65);
        AircraftTypeEntity aircraftType7 = new AircraftTypeEntity("A320", (double) 8000, 80, (double) 180000, (double) 580, (double) 3800, (double) 6400, (double) 28, "Gas", (double) 70);
        entityManager.persist(aircraftType1);
        entityManager.persist(aircraftType2);
        entityManager.persist(aircraftType3);
        entityManager.persist(aircraftType4);
        entityManager.persist(aircraftType5);
        entityManager.persist(aircraftType6);
        entityManager.persist(aircraftType7);

        System.out.println("aircraftTypes added");

        AirportEntity a1 = new AirportEntity(false, "Shijiazhuang", "ZD Airport", "SJZ", "China");
        AirportEntity a2 = new AirportEntity(false, "Guangzhou", "Baiyun Airport", "CAN", "China");
        AirportEntity a3 = new AirportEntity(false, "Beijing", "BJ International Airport", "PEK", "China");
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
        aircraftSessionBean.addAircraft("003", aircraftType1, (double) 20000000, (double) 1000000, (double) 19000000, (double) 20, (double) 0, "All is well", a4, a4, 0, 0, 4, 6, 6, 30, 7, 50, (double) 30);
        aircraftSessionBean.addAircraft("004", aircraftType2, (double) 20000000, (double) 1000000, (double) 19000000, (double) 18, (double) 0, "All is well", a8, a8, 4, 5, 4, 6, 6, 30, 7, 30, (double) 50);
        aircraftSessionBean.addAircraft("005", aircraftType2, (double) 20000000, (double) 1000000, (double) 19000000, (double) 18, (double) 0, "All is well", a4, a4, 4, 5, 4, 6, 6, 30, 7, 30, (double) 47);
        aircraftSessionBean.addAircraft("006", aircraftType2, (double) 20000000, (double) 1000000, (double) 19000000, (double) 20, (double) 0, "All is well", a8, a8, 0, 0, 4, 6, 6, 30, 7, 50, (double) 42);
        aircraftSessionBean.addAircraft("007", aircraftType3, (double) 20000000, (double) 1000000, (double) 19000000, (double) 18, (double) 0, "All is well", a4, a4, 4, 5, 4, 6, 6, 30, 7, 30, (double) 50);
        aircraftSessionBean.addAircraft("008", aircraftType3, (double) 20000000, (double) 1000000, (double) 19000000, (double) 18, (double) 0, "All is well", a8, a8, 4, 5, 4, 6, 6, 30, 7, 30, (double) 47);
        aircraftSessionBean.addAircraft("009", aircraftType3, (double) 20000000, (double) 1000000, (double) 19000000, (double) 20, (double) 0, "All is well", a4, a4, 0, 0, 4, 6, 6, 30, 7, 50, (double) 30);
        aircraftSessionBean.addAircraft("010", aircraftType4, (double) 20000000, (double) 1000000, (double) 19000000, (double) 18, (double) 0, "All is well", a8, a8, 4, 5, 4, 6, 6, 30, 7, 30, (double) 65);
        aircraftSessionBean.addAircraft("011", aircraftType4, (double) 20000000, (double) 1000000, (double) 19000000, (double) 18, (double) 0, "All is well", a4, a4, 4, 5, 4, 6, 6, 30, 7, 30, (double) 37);
        aircraftSessionBean.addAircraft("012", aircraftType4, (double) 20000000, (double) 1000000, (double) 19000000, (double) 20, (double) 0, "All is well", a8, a8, 0, 0, 4, 6, 6, 30, 7, 50, (double) 51);
        aircraftSessionBean.addAircraft("013", aircraftType5, (double) 20000000, (double) 1000000, (double) 19000000, (double) 18, (double) 0, "All is well", a4, a4, 4, 5, 4, 6, 6, 30, 7, 30, (double) 50);
        aircraftSessionBean.addAircraft("014", aircraftType5, (double) 20000000, (double) 1000000, (double) 19000000, (double) 18, (double) 0, "All is well", a8, a8, 4, 5, 4, 6, 6, 30, 7, 30, (double) 47);
        aircraftSessionBean.addAircraft("015", aircraftType5, (double) 20000000, (double) 1000000, (double) 19000000, (double) 20, (double) 0, "All is well", a4, a4, 0, 0, 4, 6, 6, 30, 7, 50, (double) 42);
        

        System.out.println("aircrafts added");
        List<String> l1 = new ArrayList<String>();
        l1.add("A380");
        l1.add("A880");
        l1.add("B777");

        List<String> l2 = new ArrayList<String>();
        l2.add("A880");
        l2.add("B700");
        l2.add("B787");

        List<String> l3 = new ArrayList<String>();
        l3.add("A380");
        l3.add("A880");
        l3.add("B777");
        l3.add("B787");
        l3.add("B700");

        List<String> l4 = new ArrayList<String>();
        l4.add("A880");
        l4.add("B777");

        List<String> l5 = new ArrayList<String>();
        l5.add("A380");
        l5.add("B787");

        List<String> l6 = new ArrayList<String>();
        l6.add("A380");
        l6.add("A880");
        l6.add("B777");
        l6.add("B787");

        List<String> l7 = new ArrayList<String>();
        l7.add("B777");
        l7.add("B787");
        l7.add("B700");

        List<String> l8 = new ArrayList<String>();
        l8.add("A380");

        List<String> l9 = new ArrayList<String>();
        l9.add("A880");
        l9.add("B700");

        List<String> l10 = new ArrayList<String>();
        l10.add("B787");
        l10.add("B700");

        System.out.println("List of aircraft types created for pilot scheduling testing");

        PilotEntity p1 = new PilotEntity("p001", "Tom", "123456", "abc@163.com", "123", "Street No 1", "male", "available", l1, null, false);
        PilotEntity p2 = new PilotEntity("p002", "Tommy", "123456", "abc@163.com", "123", "Street No 1", "male", "available", l2, null, true);
        PilotEntity p3 = new PilotEntity("p003", "Kurt", "123456", "abc@163.com", "123", "Street No 1", "male", "available", l3, null, false);
        PilotEntity p4 = new PilotEntity("p004", "Yin Lei", "123456", "abc@163.com", "123", "Street No 1", "male", "available", l4, null, true);
        PilotEntity p5 = new PilotEntity("p005", "Hao", "123456", "abc@163.com", "123", "Street No 1", "male", "available", l5, null, false);
        PilotEntity p6 = new PilotEntity("p006", "Jerry", "123456", "abc@163.com", "123", "Street No 1", "female", "available", l6, null, true);
        PilotEntity p7 = new PilotEntity("p007", "Dog", "123456", "abc@163.com", "123", "Street No 1", "male", "available", l7, null, false);
        PilotEntity p8 = new PilotEntity("p008", "Cat", "123456", "abc@163.com", "123", "Street No 1", "male", "available", l8, null, true);
        PilotEntity p9 = new PilotEntity("p009", "Pig", "123456", "abc@163.com", "123", "Street No 1", "male", "available", l9, null, false);
        PilotEntity p10 = new PilotEntity("p010", "Zebra", "123456", "abc@163.com", "123", "Street No 1", "male", "available", l10, null, true);
        PilotEntity p11 = new PilotEntity("p011", "Ant", "123456", "abc@163.com", "123", "Street No 1", "male", "available", l1, null, false);
        PilotEntity p12 = new PilotEntity("p012", "Flower", "123456", "abc@163.com", "123", "Street No 1", "male", "available", l2, null, true);
        PilotEntity p13 = new PilotEntity("p013", "Bird", "123456", "abc@163.com", "123", "Street No 1", "male", "available", l3, null, false);
        PilotEntity p14 = new PilotEntity("p014", "Butterfly", "123456", "abc@163.com", "123", "Street No 1", "female", "available", l4, null, true);
        PilotEntity p15 = new PilotEntity("p015", "Kimberly", "123456", "abc@163.com", "123", "Street No 1", "male", "available", l5, null, false);
        PilotEntity p16 = new PilotEntity("p016", "One", "123456", "abc@163.com", "123", "Street No 1", "male", "available", l6, null, false);
        PilotEntity p17 = new PilotEntity("p017", "Two", "123456", "abc@163.com", "123", "Street No 1", "male", "available", l7, null, true);
        PilotEntity p18 = new PilotEntity("p018", "Three", "123456", "abc@163.com", "123", "Street No 1", "male", "available", l8, null, false);
        PilotEntity p19 = new PilotEntity("p019", "Four", "123456", "abc@163.com", "123", "Street No 1", "male", "available", l9, null, true);
        PilotEntity p20 = new PilotEntity("p020", "Five", "123456", "abc@163.com", "123", "Street No 1", "male", "available", l10, null, false);
        PilotEntity p21 = new PilotEntity("p021", "Six", "123456", "abc@163.com", "123", "Street No 1", "female", "available", l1, null, true);
        PilotEntity p22 = new PilotEntity("p022", "Seven", "123456", "abc@163.com", "123", "Street No 1", "male", "available", l2, null, false);
        PilotEntity p23 = new PilotEntity("p023", "Eight", "123456", "abc@163.com", "123", "Street No 1", "male", "available", l3, null, true);
        PilotEntity p24 = new PilotEntity("p024", "Nine", "123456", "abc@163.com", "123", "Street No 1", "male", "available", l4, null, false);
        PilotEntity p25 = new PilotEntity("p025", "Ten", "123456", "abc@163.com", "123", "Street No 1", "male", "available", l5, null, true);
        PilotEntity p26 = new PilotEntity("p026", "Elevan", "123456", "abc@163.com", "123", "Street No 1", "male", "available", l6, null, false);
        PilotEntity p27 = new PilotEntity("p027", "Twlve", "123456", "abc@163.com", "123", "Street No 1", "male", "available", l7, null, true);
        PilotEntity p28 = new PilotEntity("p028", "Thirteen", "123456", "abc@163.com", "123", "Street No 1", "male", "available", l8, null, false);
        PilotEntity p29 = new PilotEntity("p029", "Fourteen", "123456", "abc@163.com", "123", "Street No 1", "female", "available", l9, null, true);
        PilotEntity p30 = new PilotEntity("p030", "Fifteen", "123456", "abc@163.com", "123", "Street No 1", "male", "available", l10, null, false);

        entityManager.persist(p1);
        entityManager.persist(p2);
        entityManager.persist(p3);
        entityManager.persist(p4);
        entityManager.persist(p5);
        entityManager.persist(p6);
        entityManager.persist(p7);
        entityManager.persist(p8);
        entityManager.persist(p9);
        entityManager.persist(p10);
        entityManager.persist(p11);
        entityManager.persist(p12);
//        entityManager.persist(p13);
//        entityManager.persist(p14);
//        entityManager.persist(p15);
//        entityManager.persist(p16);

        

        p1.setBase(a4);
        p2.setBase(a4);
        p3.setBase(a4);
        p4.setBase(a4);
        p5.setBase(a4);
        p6.setBase(a4);
        p7.setBase(a4);
        p8.setBase(a4);
        p9.setBase(a8);
        p10.setBase(a8);
        p11.setBase(a8);
        p12.setBase(a8);
//        p13.setBase(a8);
//        p14.setBase(a8);
//        p15.setBase(a8);
//        p16.setBase(a8);

        System.out.println("Pilots are created");

        CabinCrewEntity c1 = new CabinCrewEntity("c001", "Big pig", "123", "hahaha@merlion.com", "123", "Earth", "Male", "available", null);
        CabinCrewEntity c2 = new CabinCrewEntity("c002", "Small pig", "123", "hahaha@merlion.com", "123", "Earth", "Female", "available", null);
        CabinCrewEntity c3 = new CabinCrewEntity("c003", "Bunny", "123", "hahaha@merlion.com", "123", "Earth", "Male", "available", null);
        CabinCrewEntity c4 = new CabinCrewEntity("c004", "Wang fei", "123", "hahaha@merlion.com", "123", "Earth", "Female", "available", null);
        CabinCrewEntity c5 = new CabinCrewEntity("c005", "Dongyan", "123", "hahaha@merlion.com", "123", "Earth", "Male", "available", null);
        CabinCrewEntity c6 = new CabinCrewEntity("c006", "Li Dongyan", "123", "hahaha@merlion.com", "123", "Earth", "Female", "available", null);
        CabinCrewEntity c7 = new CabinCrewEntity("c007", "Hao", "123", "hahaha@merlion.com", "123", "Earth", "Male", "available", null);
        CabinCrewEntity c8 = new CabinCrewEntity("c008", "Cai Rui", "123", "hahaha@merlion.com", "123", "Earth", "Female", "available", null);
        CabinCrewEntity c9 = new CabinCrewEntity("c009", "Cherry", "123", "hahaha@merlion.com", "123", "Earth", "Male", "available", null);
        CabinCrewEntity c10 = new CabinCrewEntity("c010", "Wu Tong", "123", "hahaha@merlion.com", "123", "Earth", "Female", "available", null);
        CabinCrewEntity c11 = new CabinCrewEntity("c011", "Tong", "123", "hahaha@merlion.com", "123", "Earth", "Male", "available", null);
        CabinCrewEntity c12 = new CabinCrewEntity("c012", "Tom", "123", "hahaha@merlion.com", "123", "Earth", "Female", "available", null);
        CabinCrewEntity c13 = new CabinCrewEntity("c013", "Tommy", "123", "hahaha@merlion.com", "123", "Earth", "Male", "available", null);
        CabinCrewEntity c14 = new CabinCrewEntity("c014", "Liu Jingjing", "123", "hahaha@merlion.com", "123", "Earth", "Female", "available", null);
        CabinCrewEntity c15 = new CabinCrewEntity("c015", "Ma Weiwei", "123", "hahaha@merlion.com", "123", "Earth", "Male", "available", null);
        CabinCrewEntity c16 = new CabinCrewEntity("c016", "Chui Ji", "123", "hahaha@merlion.com", "123", "Earth", "Female", "available", null);
        CabinCrewEntity c17 = new CabinCrewEntity("c017", "Chen Yongkai", "123", "hahaha@merlion.com", "123", "Earth", "Male", "available", null);
        CabinCrewEntity c18 = new CabinCrewEntity("c018", "Wu Qingfeng", "123", "hahaha@merlion.com", "123", "Earth", "Female", "available", null);
        CabinCrewEntity c19 = new CabinCrewEntity("c019", "Beautiful", "123", "hahaha@merlion.com", "123", "Earth", "Male", "available", null);
        CabinCrewEntity c20 = new CabinCrewEntity("c020", "Ugly", "123", "hahaha@merlion.com", "123", "Earth", "Female", "available", null);
        CabinCrewEntity c21 = new CabinCrewEntity("c021", "East", "123", "hahaha@merlion.com", "123", "Earth", "Male", "available", null);
        CabinCrewEntity c22 = new CabinCrewEntity("c022", "West", "123", "hahaha@merlion.com", "123", "Earth", "Female", "available", null);
        CabinCrewEntity c23 = new CabinCrewEntity("c023", "Lewis", "123", "hahaha@merlion.com", "123", "Earth", "Male", "available", null);
        CabinCrewEntity c24 = new CabinCrewEntity("c024", "Xinyi", "123", "hahaha@merlion.com", "123", "Earth", "Female", "available", null);
        CabinCrewEntity c25 = new CabinCrewEntity("c025", "Gong Yuqi", "123", "hahaha@merlion.com", "123", "Earth", "Male", "available", null);
        CabinCrewEntity c26 = new CabinCrewEntity("c026", "Sodagreen", "123", "hahaha@merlion.com", "123", "Earth", "Female", "available", null);
        CabinCrewEntity c27 = new CabinCrewEntity("c027", "Pen", "123", "hahaha@merlion.com", "123", "Earth", "Male", "available", null);
        CabinCrewEntity c28 = new CabinCrewEntity("c028", "iphone", "123", "hahaha@merlion.com", "123", "Earth", "Female", "available", null);
        CabinCrewEntity c29 = new CabinCrewEntity("c029", "Pencil", "123", "hahaha@merlion.com", "123", "Earth", "Male", "available", null);
        CabinCrewEntity c30 = new CabinCrewEntity("c030", "Library", "123", "hahaha@merlion.com", "123", "Earth", "Female", "available", null);
//        CabinCrewEntity c31 = new CabinCrewEntity("c031", "Netbean", "123", "hahaha@merlion.com", "123", "Earth", "Male", "available", null);
//        CabinCrewEntity c32 = new CabinCrewEntity("c032", "Glassfish", "123", "hahaha@merlion.com", "123", "Earth", "Female", "available", null);
//        CabinCrewEntity c33 = new CabinCrewEntity("c033", "Sam", "123", "hahaha@merlion.com", "123", "Earth", "Male", "available", null);
//        CabinCrewEntity c34 = new CabinCrewEntity("c034", "Da Xiong", "123", "hahaha@merlion.com", "123", "Earth", "Female", "available", null);
//        CabinCrewEntity c35 = new CabinCrewEntity("c035", "Chen Yixun", "123", "hahaha@merlion.com", "123", "Earth", "Male", "available", null);
//        CabinCrewEntity c36 = new CabinCrewEntity("c036", "Wang Tong", "123", "hahaha@merlion.com", "123", "Earth", "Female", "available", null);
//        CabinCrewEntity c37 = new CabinCrewEntity("c037", "Lewis Phey", "123", "hahaha@merlion.com", "123", "Earth", "Male", "available", null);
//        CabinCrewEntity c38 = new CabinCrewEntity("c038", "Handphone", "123", "hahaha@merlion.com", "123", "Earth", "Female", "available", null);
//        CabinCrewEntity c39 = new CabinCrewEntity("c039", "Water", "123", "hahaha@merlion.com", "123", "Earth", "Male", "available", null);
//        CabinCrewEntity c40 = new CabinCrewEntity("c040", "East", "123", "hahaha@merlion.com", "123", "Earth", "Female", "available", null);
//        CabinCrewEntity c41 = new CabinCrewEntity("c041", "South", "123", "hahaha@merlion.com", "123", "Earth", "Male", "available", null);
//        CabinCrewEntity c42 = new CabinCrewEntity("c042", "West", "123", "hahaha@merlion.com", "123", "Earth", "Female", "available", null);
//        CabinCrewEntity c43 = new CabinCrewEntity("c043", "North", "123", "hahaha@merlion.com", "123", "Earth", "Male", "available", null);
//        CabinCrewEntity c44 = new CabinCrewEntity("c044", "Cable", "123", "hahaha@merlion.com", "123", "Earth", "Female", "available", null);
//        CabinCrewEntity c45 = new CabinCrewEntity("c045", "Left", "123", "hahaha@merlion.com", "123", "Earth", "Male", "available", null);
//        CabinCrewEntity c46 = new CabinCrewEntity("c046", "Right", "123", "hahaha@merlion.com", "123", "Earth", "Female", "available", null);
//        CabinCrewEntity c47 = new CabinCrewEntity("c047", "Table", "123", "hahaha@merlion.com", "123", "Earth", "Male", "available", null);
//        CabinCrewEntity c48 = new CabinCrewEntity("c048", "Sam", "123", "hahaha@merlion.com", "123", "Earth", "Female", "available", null);
//        CabinCrewEntity c49 = new CabinCrewEntity("c049", "Sunny", "123", "hahaha@merlion.com", "123", "Earth", "Male", "available", null);
//        CabinCrewEntity c50 = new CabinCrewEntity("c050", "Edwin", "123", "hahaha@merlion.com", "123", "Earth", "Female", "available", null);
//
//        CabinCrewEntity c51 = new CabinCrewEntity("c051", "Big pig", "123", "hahaha@merlion.com", "123", "Earth", "Male", "available", null);
//        CabinCrewEntity c52 = new CabinCrewEntity("c052", "Small pig", "123", "hahaha@merlion.com", "123", "Earth", "Female", "available", null);
//        CabinCrewEntity c53 = new CabinCrewEntity("c053", "Bunny", "123", "hahaha@merlion.com", "123", "Earth", "Male", "available", null);
//        CabinCrewEntity c54 = new CabinCrewEntity("c054", "Wang fei", "123", "hahaha@merlion.com", "123", "Earth", "Female", "available", null);
//        CabinCrewEntity c55 = new CabinCrewEntity("c055", "Dongyan", "123", "hahaha@merlion.com", "123", "Earth", "Male", "available", null);
//        CabinCrewEntity c56 = new CabinCrewEntity("c056", "Li Dongyan", "123", "hahaha@merlion.com", "123", "Earth", "Female", "available", null);
//        CabinCrewEntity c57 = new CabinCrewEntity("c057", "Hao", "123", "hahaha@merlion.com", "123", "Earth", "Male", "available", null);
//        CabinCrewEntity c58 = new CabinCrewEntity("c058", "Cai Rui", "123", "hahaha@merlion.com", "123", "Earth", "Female", "available", null);
//        CabinCrewEntity c59 = new CabinCrewEntity("c059", "Cherry", "123", "hahaha@merlion.com", "123", "Earth", "Male", "available", null);
//        CabinCrewEntity c60 = new CabinCrewEntity("c060", "Wu Tong", "123", "hahaha@merlion.com", "123", "Earth", "Female", "available", null);
//        CabinCrewEntity c61 = new CabinCrewEntity("c061", "Tong", "123", "hahaha@merlion.com", "123", "Earth", "Male", "available", null);
//        CabinCrewEntity c62 = new CabinCrewEntity("c062", "Tom", "123", "hahaha@merlion.com", "123", "Earth", "Female", "available", null);
//        CabinCrewEntity c63 = new CabinCrewEntity("c063", "Tommy", "123", "hahaha@merlion.com", "123", "Earth", "Male", "available", null);
//        CabinCrewEntity c64 = new CabinCrewEntity("c064", "Liu Jingjing", "123", "hahaha@merlion.com", "123", "Earth", "Female", "available", null);
//        CabinCrewEntity c65 = new CabinCrewEntity("c065", "Ma Weiwei", "123", "hahaha@merlion.com", "123", "Earth", "Male", "available", null);
//        CabinCrewEntity c66 = new CabinCrewEntity("c066", "Chui Ji", "123", "hahaha@merlion.com", "123", "Earth", "Female", "available", null);
//        CabinCrewEntity c67 = new CabinCrewEntity("c067", "Chen Yongkai", "123", "hahaha@merlion.com", "123", "Earth", "Male", "available", null);
//        CabinCrewEntity c68 = new CabinCrewEntity("c068", "Wu Qingfeng", "123", "hahaha@merlion.com", "123", "Earth", "Female", "available", null);
//        CabinCrewEntity c69 = new CabinCrewEntity("c069", "Beautiful", "123", "hahaha@merlion.com", "123", "Earth", "Male", "available", null);
//        CabinCrewEntity c70 = new CabinCrewEntity("c070", "Ugly", "123", "hahaha@merlion.com", "123", "Earth", "Female", "available", null);
//        CabinCrewEntity c71 = new CabinCrewEntity("c071", "East", "123", "hahaha@merlion.com", "123", "Earth", "Male", "available", null);
//        CabinCrewEntity c72 = new CabinCrewEntity("c072", "West", "123", "hahaha@merlion.com", "123", "Earth", "Female", "available", null);
//        CabinCrewEntity c73 = new CabinCrewEntity("c073", "Lewis", "123", "hahaha@merlion.com", "123", "Earth", "Male", "available", null);
//        CabinCrewEntity c74 = new CabinCrewEntity("c074", "Xinyi", "123", "hahaha@merlion.com", "123", "Earth", "Female", "available", null);
//        CabinCrewEntity c75 = new CabinCrewEntity("c075", "Gong Yuqi", "123", "hahaha@merlion.com", "123", "Earth", "Male", "available", null);
//        CabinCrewEntity c76 = new CabinCrewEntity("c076", "Sodagreen", "123", "hahaha@merlion.com", "123", "Earth", "Female", "available", null);
//        CabinCrewEntity c77 = new CabinCrewEntity("c077", "Pen", "123", "hahaha@merlion.com", "123", "Earth", "Male", "available", null);
//        CabinCrewEntity c78 = new CabinCrewEntity("c078", "iphone", "123", "hahaha@merlion.com", "123", "Earth", "Female", "available", null);
//        CabinCrewEntity c79 = new CabinCrewEntity("c079", "Pencil", "123", "hahaha@merlion.com", "123", "Earth", "Male", "available", null);
//        CabinCrewEntity c80 = new CabinCrewEntity("c080", "Library", "123", "hahaha@merlion.com", "123", "Earth", "Female", "available", null);
//        CabinCrewEntity c81 = new CabinCrewEntity("c081", "Netbean", "123", "hahaha@merlion.com", "123", "Earth", "Male", "available", null);
//        CabinCrewEntity c82 = new CabinCrewEntity("c082", "Glassfish", "123", "hahaha@merlion.com", "123", "Earth", "Female", "available", null);
//        CabinCrewEntity c83 = new CabinCrewEntity("c083", "Sam", "123", "hahaha@merlion.com", "123", "Earth", "Male", "available", null);
//        CabinCrewEntity c84 = new CabinCrewEntity("c084", "Da Xiong", "123", "hahaha@merlion.com", "123", "Earth", "Female", "available", null);
//        CabinCrewEntity c85 = new CabinCrewEntity("c085", "Chen Yixun", "123", "hahaha@merlion.com", "123", "Earth", "Male", "available", null);
//        CabinCrewEntity c86 = new CabinCrewEntity("c086", "Wang Tong", "123", "hahaha@merlion.com", "123", "Earth", "Female", "available", null);
//        CabinCrewEntity c87 = new CabinCrewEntity("c087", "Lewis Phey", "123", "hahaha@merlion.com", "123", "Earth", "Male", "available", null);
//        CabinCrewEntity c88 = new CabinCrewEntity("c088", "Handphone", "123", "hahaha@merlion.com", "123", "Earth", "Female", "available", null);
//        CabinCrewEntity c89 = new CabinCrewEntity("c089", "Water", "123", "hahaha@merlion.com", "123", "Earth", "Male", "available", null);
//        CabinCrewEntity c90 = new CabinCrewEntity("c090", "East", "123", "hahaha@merlion.com", "123", "Earth", "Female", "available", null);
//        CabinCrewEntity c91 = new CabinCrewEntity("c091", "South", "123", "hahaha@merlion.com", "123", "Earth", "Male", "available", null);
//        CabinCrewEntity c92 = new CabinCrewEntity("c092", "West", "123", "hahaha@merlion.com", "123", "Earth", "Female", "available", null);
//        CabinCrewEntity c93 = new CabinCrewEntity("c093", "North", "123", "hahaha@merlion.com", "123", "Earth", "Male", "available", null);
//        CabinCrewEntity c94 = new CabinCrewEntity("c094", "Cable", "123", "hahaha@merlion.com", "123", "Earth", "Female", "available", null);
//        CabinCrewEntity c95 = new CabinCrewEntity("c095", "Left", "123", "hahaha@merlion.com", "123", "Earth", "Male", "available", null);
//        CabinCrewEntity c96 = new CabinCrewEntity("c096", "Right", "123", "hahaha@merlion.com", "123", "Earth", "Female", "available", null);
//        CabinCrewEntity c97 = new CabinCrewEntity("c097", "Table", "123", "hahaha@merlion.com", "123", "Earth", "Male", "available", null);
//        CabinCrewEntity c98 = new CabinCrewEntity("c098", "Sam", "123", "hahaha@merlion.com", "123", "Earth", "Female", "available", null);
//        CabinCrewEntity c99 = new CabinCrewEntity("c099", "Sunny", "123", "hahaha@merlion.com", "123", "Earth", "Male", "available", null);
//        CabinCrewEntity c100 = new CabinCrewEntity("c100", "Edwin", "123", "hahaha@merlion.com", "123", "Earth", "Female", "available", null);

        entityManager.persist(c1);
        entityManager.persist(c2);
        entityManager.persist(c3);
        entityManager.persist(c4);
        entityManager.persist(c5);
        entityManager.persist(c6);
        entityManager.persist(c7);
        entityManager.persist(c8);
        entityManager.persist(c9);
        entityManager.persist(c10);
        entityManager.persist(c11);
        entityManager.persist(c12);
        entityManager.persist(c13);
        entityManager.persist(c14);
        entityManager.persist(c15);
        entityManager.persist(c16);
        entityManager.persist(c17);
        entityManager.persist(c18);
        entityManager.persist(c19);
        entityManager.persist(c20);
        entityManager.persist(c21);
        entityManager.persist(c22);
        entityManager.persist(c23);
        entityManager.persist(c24);
        entityManager.persist(c25);
        entityManager.persist(c26);
        entityManager.persist(c27);
        entityManager.persist(c28);
        entityManager.persist(c29);
        entityManager.persist(c30);
//        entityManager.persist(c31);
//        entityManager.persist(c32);
//        entityManager.persist(c33);
//        entityManager.persist(c34);
//        entityManager.persist(c35);
//        entityManager.persist(c36);
//        entityManager.persist(c37);
//        entityManager.persist(c38);
//        entityManager.persist(c39);
//        entityManager.persist(c40);
//        entityManager.persist(c41);
//        entityManager.persist(c42);
//        entityManager.persist(c43);
//        entityManager.persist(c44);
//        entityManager.persist(c45);
//        entityManager.persist(c46);
//        entityManager.persist(c47);
//        entityManager.persist(c48);
//        entityManager.persist(c49);
//        entityManager.persist(c50);
//        entityManager.persist(c51);
//        entityManager.persist(c52);
//        entityManager.persist(c53);
//        entityManager.persist(c54);
//        entityManager.persist(c55);
//        entityManager.persist(c56);
//        entityManager.persist(c57);
//        entityManager.persist(c58);
//        entityManager.persist(c59);
//        entityManager.persist(c60);
//        entityManager.persist(c61);
//        entityManager.persist(c62);
//        entityManager.persist(c63);
//        entityManager.persist(c64);
//        entityManager.persist(c65);
//        entityManager.persist(c66);
//        entityManager.persist(c67);
//        entityManager.persist(c68);
//        entityManager.persist(c69);
//        entityManager.persist(c70);
//        entityManager.persist(c71);
//        entityManager.persist(c72);
//        entityManager.persist(c73);
//        entityManager.persist(c74);
//        entityManager.persist(c75);
//        entityManager.persist(c76);
//        entityManager.persist(c77);
//        entityManager.persist(c78);
//        entityManager.persist(c79);
//        entityManager.persist(c80);
//        entityManager.persist(c81);
//        entityManager.persist(c82);
//        entityManager.persist(c83);
//        entityManager.persist(c84);
//        entityManager.persist(c85);
//        entityManager.persist(c86);
//        entityManager.persist(c87);
//        entityManager.persist(c88);
//        entityManager.persist(c89);
//        entityManager.persist(c90);
//        entityManager.persist(c91);
//        entityManager.persist(c92);
//        entityManager.persist(c93);
//        entityManager.persist(c94);
//        entityManager.persist(c95);
//        entityManager.persist(c96);
//        entityManager.persist(c97);
//        entityManager.persist(c98);
//        entityManager.persist(c99);
//        entityManager.persist(c100);

        c1.setBase(a4);
        c2.setBase(a4);
        c3.setBase(a4);
        c4.setBase(a4);
        c5.setBase(a4);
        c6.setBase(a4);
        c7.setBase(a4);
        c8.setBase(a4);
        c9.setBase(a4);
        c10.setBase(a4);
        c11.setBase(a4);
        c12.setBase(a4);
        c13.setBase(a4);
        c14.setBase(a4);
        c15.setBase(a4);
        c16.setBase(a8);
        c17.setBase(a8);
        c18.setBase(a8);
        c19.setBase(a8);
        c20.setBase(a8);
        c21.setBase(a8);
        c22.setBase(a8);
        c23.setBase(a8);
        c24.setBase(a8);
        c25.setBase(a8);
        c26.setBase(a8);
        c27.setBase(a8);
        c28.setBase(a8);
        c29.setBase(a8);
        c30.setBase(a8);
//        c31.setBase(a8);
//        c32.setBase(a8);
//        c33.setBase(a8);
//        c34.setBase(a8);
//        c35.setBase(a8);
//        c36.setBase(a8);
//        c37.setBase(a8);
//        c38.setBase(a8);
//        c39.setBase(a8);
//        c40.setBase(a8);
//        c41.setBase(a8);
//        c42.setBase(a8);
//        c43.setBase(a8);
//        c44.setBase(a8);
//        c45.setBase(a8);
//        c46.setBase(a4);
//        c47.setBase(a8);
//        c48.setBase(a4);
//        c49.setBase(a8);
//        c50.setBase(a4);

        System.out.println("Cabin crew are created.");

    }

    @Override
    public Integer getLeftChance() {
        return leftChance;
    }

    public void setLeftChance(Integer leftChance) {
        this.leftChance = leftChance;
    }

    @Override
    public StaffEntity fetchStaff(String staffNo) {
        Query query = entityManager.createQuery("SELECT s FROM StaffEntity s WHERE s.staffNo = :staffNumber");
        query.setParameter("staffNumber", staffNo);
        List<StaffEntity> staffs = (List<StaffEntity>) query.getResultList();
        System.out.print(staffs);
        return staffs.get(0);
    }

}
