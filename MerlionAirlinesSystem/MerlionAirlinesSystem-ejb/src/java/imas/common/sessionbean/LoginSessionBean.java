/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package imas.common.sessionbean;

import GDS.sessionbean.GDSAirportSessionBeanLocal;
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
public class LoginSessionBean implements LoginSessionBeanLocal,LoginSessionBeanRemote{
    
    @EJB
    private GDSAirportSessionBeanLocal gDSAirportSessionBean;

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
        
        gDSAirportSessionBean.getAllGDSAirport();

        AircraftTypeEntity aircraftType1 = new AircraftTypeEntity("A380", (double) 10000, (double) 550, (double) 600, (double) 3000, (double) 4400, (double) 20, "Gas", (double) 60);
        AircraftTypeEntity aircraftType2 = new AircraftTypeEntity("A330", (double) 5000, (double) 400, (double) 450, (double) 3800, (double) 6400, (double) 28, "Gas", (double) 55);
        AircraftTypeEntity aircraftType3 = new AircraftTypeEntity("B777", (double) 10000, (double) 600, (double) 550, (double) 3800, (double) 6400, (double) 28, "Gas", (double) 65);
        AircraftTypeEntity aircraftType4 = new AircraftTypeEntity("B787", (double) 12000, (double) 500, (double) 600, (double) 3800, (double) 6400, (double) 28, "Gas", (double) 60);
        AircraftTypeEntity aircraftType5 = new AircraftTypeEntity("B747", (double) 8000, (double) 470, (double) 580, (double) 3800, (double) 6400, (double) 28, "Gas", (double) 55);
        AircraftTypeEntity aircraftType6 = new AircraftTypeEntity("B737", (double) 12000, (double) 520, (double) 600, (double) 3800, (double) 6400, (double) 28, "Gas", (double) 65);
        AircraftTypeEntity aircraftType7 = new AircraftTypeEntity("A320", (double) 8000, (double) 450, (double) 580, (double) 3800, (double) 6400, (double) 28, "Gas", (double) 70);
        entityManager.persist(aircraftType1);
        entityManager.persist(aircraftType2);
        entityManager.persist(aircraftType3);
        entityManager.persist(aircraftType4);
        entityManager.persist(aircraftType5);
        entityManager.persist(aircraftType6);
        entityManager.persist(aircraftType7);

        System.out.println("aircraftTypes added");
        //GDS(name, city, country, code, timezone)
        //airport(boolean, city, airport, airport code, nation name, timezone)
        //"Hong Kong Intl", "Hong Kong", "Hong Kong", "HKG", "VHHH", 22.308919, 113.914603, 28.0, 8.0, 'U', "Asia/Hong_Kong");
        //"Pudong", "Shanghai", "China", "PVG", "ZSPD", 31.143378, 121.805214, 13.0, 8.0, 'U', "Asia/Chongqing");
        AirportEntity a1 = new AirportEntity(true, "Singapore", "Changi Intl", "SIN", "Singapore", "Asia/Singapore");
        AirportEntity a2 = new AirportEntity(true, "Hong Kong", "Hong Kong Intl", "HKG", "Hong Kong", "Asia/Hong_Kong");
        AirportEntity a3 = new AirportEntity(false, "Pudong", "Pudong", "PVG", "China", "Asia/Chongqing");

        airportSessionBean.addAirport(a1);
        airportSessionBean.addAirport(a2);
        airportSessionBean.addAirport(a3);

        System.out.println("Airports added");

        for (int i = 0; i < 5; i++) {

            aircraftSessionBean.addAircraft("001" + i, aircraftType1, (double) 20000000, (double) 1000000, (double) 19000000, (double) 20, (double) 0, "All is well", a1, a1, 4, 5, 4, 6, 6, 10, 7, 50, (double) 30);
            aircraftSessionBean.addAircraft("002" + i, aircraftType1, (double) 22000000, (double) 1000000, (double) 19000000, (double) 18, (double) 0, "All is well", a1, a1, 4, 5, 4, 6, 6, 30, 7, 30, (double) 35);
            aircraftSessionBean.addAircraft("003" + i, aircraftType1, (double) 23000000, (double) 1000000, (double) 19000000, (double) 20, (double) 0, "All is well", a1, a1, 0, 0, 4, 6, 6, 30, 7, 50, (double) 30);
            aircraftSessionBean.addAircraft("004" + i, aircraftType2, (double) 30000000, (double) 1000000, (double) 19000000, (double) 18, (double) 0, "All is well", a1, a1, 4, 5, 4, 6, 6, 30, 7, 30, (double) 50);
            aircraftSessionBean.addAircraft("005" + i, aircraftType2, (double) 30000000, (double) 1000000, (double) 19000000, (double) 18, (double) 0, "All is well", a1, a1, 4, 5, 4, 6, 6, 30, 7, 30, (double) 47);
            aircraftSessionBean.addAircraft("006" + i, aircraftType2, (double) 30000000, (double) 1000000, (double) 19000000, (double) 20, (double) 0, "All is well", a1, a1, 0, 0, 4, 6, 6, 30, 7, 50, (double) 42);
            aircraftSessionBean.addAircraft("007" + i, aircraftType3, (double) 32000000, (double) 1000000, (double) 19000000, (double) 18, (double) 0, "All is well", a1, a1, 4, 5, 4, 6, 6, 30, 7, 30, (double) 50);
            aircraftSessionBean.addAircraft("008" + i, aircraftType3, (double) 29000000, (double) 1000000, (double) 19000000, (double) 18, (double) 0, "All is well", a2, a2, 4, 5, 4, 6, 6, 30, 7, 30, (double) 47);
            aircraftSessionBean.addAircraft("009" + i, aircraftType3, (double) 28000000, (double) 1000000, (double) 19000000, (double) 20, (double) 0, "All is well", a2, a2, 0, 0, 4, 6, 6, 30, 7, 50, (double) 30);
            aircraftSessionBean.addAircraft("010" + i, aircraftType4, (double) 10000000, (double) 1000000, (double) 19000000, (double) 18, (double) 0, "All is well", a2, a2, 4, 5, 4, 6, 6, 30, 7, 30, (double) 65);
            aircraftSessionBean.addAircraft("011" + i, aircraftType4, (double) 12000000, (double) 1000000, (double) 19000000, (double) 18, (double) 0, "All is well", a2, a2, 4, 5, 4, 6, 6, 30, 7, 30, (double) 37);
            aircraftSessionBean.addAircraft("012" + i, aircraftType4, (double) 12000000, (double) 1000000, (double) 19000000, (double) 20, (double) 0, "All is well", a2, a2, 0, 0, 4, 6, 6, 30, 7, 50, (double) 51);
            aircraftSessionBean.addAircraft("013" + i, aircraftType5, (double) 14000000, (double) 1000000, (double) 19000000, (double) 18, (double) 0, "All is well", a2, a2, 4, 5, 4, 6, 6, 30, 7, 30, (double) 50);
            aircraftSessionBean.addAircraft("014" + i, aircraftType5, (double) 13000000, (double) 1000000, (double) 19000000, (double) 18, (double) 0, "All is well", a2, a2, 4, 5, 4, 6, 6, 30, 7, 30, (double) 47);
            aircraftSessionBean.addAircraft("015" + i, aircraftType5, (double) 18000000, (double) 1000000, (double) 19000000, (double) 20, (double) 0, "All is well", a2, a2, 0, 0, 4, 6, 6, 30, 7, 50, (double) 42);

        }

        System.out.println("150 aircrafts added");
        
        
        
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
        l3.add("A330");
        l3.add("B777");
        l3.add("B787");
        l3.add("B747");
        l3.add("A320");
        l3.add("B737");

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

        for (int i = 0; i < 10; i++) {

            PilotEntity p1 = new PilotEntity("p" + i + "001", "Tom", "123456", "abc@163.com", "123", "Street No 1", "male", "available", l3, null, false);
            PilotEntity p2 = new PilotEntity("p" + i + "002", "Tommy", "123456", "abc@163.com", "123", "Street No 1", "male", "available", l3, null, true);
            PilotEntity p3 = new PilotEntity("p" + i + "003", "Kurt", "123456", "abc@163.com", "123", "Street No 1", "male", "available", l3, null, false);
            PilotEntity p4 = new PilotEntity("p" + i + "004", "Yin Lei", "123456", "abc@163.com", "123", "Street No 1", "male", "available", l3, null, true);
            PilotEntity p5 = new PilotEntity("p" + i + "005", "Hao", "123456", "abc@163.com", "123", "Street No 1", "male", "available", l3, null, false);
            PilotEntity p6 = new PilotEntity("p" + i + "006", "Prienca", "123456", "abc@163.com", "123", "Street No 1", "female", "available", l3, null, true);
            PilotEntity p7 = new PilotEntity("p" + i + "007", "Najib", "123456", "abc@163.com", "123", "Street No 1", "male", "available", l3, null, false);
            PilotEntity p8 = new PilotEntity("p" + i + "008", "Arifah", "123456", "abc@163.com", "123", "Street No 1", "male", "available", l3, null, true);
            PilotEntity p9 = new PilotEntity("p" + i + "009", "Allwin", "123456", "abc@163.com", "123", "Street No 1", "male", "available", l3, null, false);
            PilotEntity p10 = new PilotEntity("p" + i + "010", "Jason", "123456", "abc@163.com", "123", "Street No 1", "male", "available", l3, null, true);
            PilotEntity p11 = new PilotEntity("p" + i + "011", "Mayor", "123456", "abc@163.com", "123", "Street No 1", "male", "available", l3, null, false);
            PilotEntity p12 = new PilotEntity("p" + i + "012", "Jack", "123456", "abc@163.com", "123", "Street No 1", "male", "available", l3, null, true);
            PilotEntity p13 = new PilotEntity("p" + i + "013", "Emma", "123456", "abc@163.com", "123", "Street No 1", "female", "available", l3, null, false);
            PilotEntity p14 = new PilotEntity("p" + i + "014", "Yu Feng", "123456", "abc@163.com", "123", "Street No 1", "female", "available", l4, null, true);
            PilotEntity p15 = new PilotEntity("p" + i + "015", "Kimberly", "123456", "abc@163.com", "123", "Street No 1", "female", "available", l5, null, false);
            PilotEntity p16 = new PilotEntity("p" + i + "016", "Yun Feng", "123456", "abc@163.com", "123", "Street No 1", "male", "available", l6, null, false);
            PilotEntity p17 = new PilotEntity("p" + i + "017", "Zhen Xiong", "123456", "abc@163.com", "123", "Street No 1", "male", "available", l7, null, true);
            PilotEntity p18 = new PilotEntity("p" + i + "018", "Dien", "123456", "abc@163.com", "123", "Street No 1", "female", "available", l8, null, false);
            PilotEntity p19 = new PilotEntity("p" + i + "019", "Sandoff", "123456", "abc@163.com", "123", "Street No 1", "male", "available", l9, null, true);
            PilotEntity p20 = new PilotEntity("p" + i + "020", "Dove", "123456", "abc@163.com", "123", "Street No 1", "male", "available", l10, null, false);
            PilotEntity p21 = new PilotEntity("p" + i + "021", "Sandz", "123456", "abc@163.com", "123", "Street No 1", "female", "available", l1, null, true);
            PilotEntity p22 = new PilotEntity("p" + i + "022", "William", "123456", "abc@163.com", "123", "Street No 1", "male", "available", l2, null, false);
            PilotEntity p23 = new PilotEntity("p" + i + "023", "Betty", "123456", "abc@163.com", "123", "Street No 1", "female", "available", l3, null, true);
            PilotEntity p24 = new PilotEntity("p" + i + "024", "Francios", "123456", "abc@163.com", "123", "Street No 1", "male", "available", l4, null, false);
            PilotEntity p25 = new PilotEntity("p" + i + "025", "Houng", "123456", "abc@163.com", "123", "Street No 1", "male", "available", l5, null, true);
            PilotEntity p26 = new PilotEntity("p" + i + "026", "Long", "123456", "abc@163.com", "123", "Street No 1", "male", "available", l6, null, false);
            PilotEntity p27 = new PilotEntity("p" + i + "027", "Eugene", "123456", "abc@163.com", "123", "Street No 1", "male", "available", l7, null, true);
            PilotEntity p28 = new PilotEntity("p" + i + "028", "Yi Fu", "123456", "abc@163.com", "123", "Street No 1", "male", "available", l8, null, false);
            PilotEntity p29 = new PilotEntity("p" + i + "029", "Franklin", "123456", "abc@163.com", "123", "Street No 1", "female", "available", l9, null, true);
            PilotEntity p30 = new PilotEntity("p" + i + "030", "Zens", "123456", "abc@163.com", "123", "Street No 1", "male", "available", l10, null, false);

            p30.setWorking(Boolean.TRUE);
            p30.setWorkingStatus("available");
            p29.setWorking(Boolean.TRUE);
            p29.setWorkingStatus("available");
            p28.setWorking(Boolean.TRUE);
            p28.setWorkingStatus("available");
            p27.setWorking(Boolean.TRUE);
            p27.setWorkingStatus("available");

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
            entityManager.persist(p13);
            entityManager.persist(p14);
            entityManager.persist(p15);
            entityManager.persist(p16);
            entityManager.persist(p17);
            entityManager.persist(p18);
            entityManager.persist(p19);
            entityManager.persist(p20);
            entityManager.persist(p21);
            entityManager.persist(p22);
            entityManager.persist(p23);
            entityManager.persist(p24);
            entityManager.persist(p25);
            entityManager.persist(p26);
            entityManager.persist(p27);
            entityManager.persist(p28);
            entityManager.persist(p29);
            entityManager.persist(p30);

            p1.setBase(a1);
            p2.setBase(a1);
            p3.setBase(a1);
            p4.setBase(a1);
            p5.setBase(a1);
            p6.setBase(a1);
            p7.setBase(a1);
            p8.setBase(a1);
            p9.setBase(a1);
            p10.setBase(a1);
            p11.setBase(a1);
            p12.setBase(a1);
            p13.setBase(a1);
            p14.setBase(a1);
            p15.setBase(a1);
            p16.setBase(a2);
            p17.setBase(a2);
            p18.setBase(a3);
            p19.setBase(a2);
            p20.setBase(a2);
            p21.setBase(a2);
            p22.setBase(a2);
            p23.setBase(a2);
            p24.setBase(a2);
            p25.setBase(a2);
            p26.setBase(a2);
            p27.setBase(a2);
            p28.setBase(a2);
            p29.setBase(a2);
            p30.setBase(a2);

            CabinCrewEntity c1 = new CabinCrewEntity("c" + i + "001", "Steve", "123", "hahaha@merlion.com", "123", "Earth", "Male", "available", null);
            CabinCrewEntity c2 = new CabinCrewEntity("c" + i + "002", "Steven", "123", "hahaha@merlion.com", "123", "Earth", "Female", "available", null);
            CabinCrewEntity c3 = new CabinCrewEntity("c" + i + "003", "Bunny", "123", "hahaha@merlion.com", "123", "Earth", "Male", "available", null);
            CabinCrewEntity c4 = new CabinCrewEntity("c" + i + "004", "Wang fei", "123", "hahaha@merlion.com", "123", "Earth", "Female", "available", null);
            CabinCrewEntity c5 = new CabinCrewEntity("c" + i + "005", "Dongyan", "123", "hahaha@merlion.com", "123", "Earth", "Male", "available", null);
            CabinCrewEntity c6 = new CabinCrewEntity("c" + i + "006", "Li Dongyan", "123", "hahaha@merlion.com", "123", "Earth", "Female", "available", null);
            CabinCrewEntity c7 = new CabinCrewEntity("c" + i + "007", "Hao", "123", "hahaha@merlion.com", "123", "Earth", "Male", "available", null);
            CabinCrewEntity c8 = new CabinCrewEntity("c" + i + "008", "Cai Rui", "123", "hahaha@merlion.com", "123", "Earth", "Female", "available", null);
            CabinCrewEntity c9 = new CabinCrewEntity("c" + i + "009", "Cherry", "123", "hahaha@merlion.com", "123", "Earth", "Male", "available", null);
            CabinCrewEntity c10 = new CabinCrewEntity("c" + i + "010", "Wu Tong", "123", "hahaha@merlion.com", "123", "Earth", "Female", "available", null);
            CabinCrewEntity c11 = new CabinCrewEntity("c" + i + "011", "Tong", "123", "hahaha@merlion.com", "123", "Earth", "Male", "available", null);
            CabinCrewEntity c12 = new CabinCrewEntity("c" + i + "012", "Tom", "123", "hahaha@merlion.com", "123", "Earth", "Female", "available", null);
            CabinCrewEntity c13 = new CabinCrewEntity("c" + i + "013", "Tommy", "123", "hahaha@merlion.com", "123", "Earth", "Male", "available", null);
            CabinCrewEntity c14 = new CabinCrewEntity("c" + i + "014", "Liu Jingjing", "123", "hahaha@merlion.com", "123", "Earth", "Female", "available", null);
            CabinCrewEntity c15 = new CabinCrewEntity("c" + i + "015", "Ma Weiwei", "123", "hahaha@merlion.com", "123", "Earth", "Male", "available", null);
            CabinCrewEntity c16 = new CabinCrewEntity("c" + i + "016", "Chui Ji", "123", "hahaha@merlion.com", "123", "Earth", "Female", "available", null);
            CabinCrewEntity c17 = new CabinCrewEntity("c" + i + "017", "Chen Yongkai", "123", "hahaha@merlion.com", "123", "Earth", "Male", "available", null);
            CabinCrewEntity c18 = new CabinCrewEntity("c" + i + "018", "Wu Qingfeng", "123", "hahaha@merlion.com", "123", "Earth", "Female", "available", null);
            CabinCrewEntity c19 = new CabinCrewEntity("c" + i + "019", "Andy", "123", "hahaha@merlion.com", "123", "Earth", "Male", "available", null);
            CabinCrewEntity c20 = new CabinCrewEntity("c" + i + "020", "Wony", "123", "hahaha@merlion.com", "123", "Earth", "Female", "available", null);
            CabinCrewEntity c21 = new CabinCrewEntity("c" + i + "021", "East", "123", "hahaha@merlion.com", "123", "Earth", "Male", "available", null);
            CabinCrewEntity c22 = new CabinCrewEntity("c" + i + "022", "Jester", "123", "hahaha@merlion.com", "123", "Earth", "Female", "available", null);
            CabinCrewEntity c23 = new CabinCrewEntity("c" + i + "023", "Lewis", "123", "hahaha@merlion.com", "123", "Earth", "Male", "available", null);
            CabinCrewEntity c24 = new CabinCrewEntity("c" + i + "024", "Xinyi", "123", "hahaha@merlion.com", "123", "Earth", "Female", "available", null);
            CabinCrewEntity c25 = new CabinCrewEntity("c" + i + "025", "Gong Yuqi", "123", "hahaha@merlion.com", "123", "Earth", "Male", "available", null);
            CabinCrewEntity c26 = new CabinCrewEntity("c" + i + "026", "Sodagreen", "123", "hahaha@merlion.com", "123", "Earth", "Female", "available", null);
            CabinCrewEntity c27 = new CabinCrewEntity("c" + i + "027", "Kok Tiong", "123", "hahaha@merlion.com", "123", "Earth", "Male", "available", null);
            CabinCrewEntity c28 = new CabinCrewEntity("c" + i + "028", "Yoke", "123", "hahaha@merlion.com", "123", "Earth", "Female", "available", null);
            CabinCrewEntity c29 = new CabinCrewEntity("c" + i + "029", "Ford", "123", "hahaha@merlion.com", "123", "Earth", "Male", "available", null);
            CabinCrewEntity c30 = new CabinCrewEntity("c" + i + "030", "Rohizat", "123", "hahaha@merlion.com", "123", "Earth", "Female", "available", null);
            CabinCrewEntity c31 = new CabinCrewEntity("c" + i + "031", "Werden", "123", "hahaha@merlion.com", "123", "Earth", "Male", "available", null);
            CabinCrewEntity c32 = new CabinCrewEntity("c" + i + "032", "Amy", "123", "hahaha@merlion.com", "123", "Earth", "Female", "available", null);
            CabinCrewEntity c33 = new CabinCrewEntity("c" + i + "033", "Sam", "123", "hahaha@merlion.com", "123", "Earth", "Male", "available", null);
            CabinCrewEntity c34 = new CabinCrewEntity("c" + i + "034", "Da Xiong", "123", "hahaha@merlion.com", "123", "Earth", "Female", "available", null);
            CabinCrewEntity c35 = new CabinCrewEntity("c" + i + "035", "Chen Yixun", "123", "hahaha@merlion.com", "123", "Earth", "Male", "available", null);
            CabinCrewEntity c36 = new CabinCrewEntity("c" + i + "036", "Wang Tong", "123", "hahaha@merlion.com", "123", "Earth", "Female", "available", null);
            CabinCrewEntity c37 = new CabinCrewEntity("c" + i + "037", "Lewis Phey", "123", "hahaha@merlion.com", "123", "Earth", "Male", "available", null);
            CabinCrewEntity c38 = new CabinCrewEntity("c" + i + "038", "Elle", "123", "hahaha@merlion.com", "123", "Earth", "Female", "available", null);
            CabinCrewEntity c39 = new CabinCrewEntity("c" + i + "039", "Annand", "123", "hahaha@merlion.com", "123", "Earth", "Male", "available", null);
            CabinCrewEntity c40 = new CabinCrewEntity("c" + i + "040", "Shun Jun", "123", "hahaha@merlion.com", "123", "Earth", "Female", "available", null);
            CabinCrewEntity c41 = new CabinCrewEntity("c" + i + "041", "Young", "123", "hahaha@merlion.com", "123", "Earth", "Male", "available", null);
            CabinCrewEntity c42 = new CabinCrewEntity("c" + i + "042", "West", "123", "hahaha@merlion.com", "123", "Earth", "Female", "available", null);
            CabinCrewEntity c43 = new CabinCrewEntity("c" + i + "043", "North", "123", "hahaha@merlion.com", "123", "Earth", "Male", "available", null);
            CabinCrewEntity c44 = new CabinCrewEntity("c" + i + "044", "Hanna", "123", "hahaha@merlion.com", "123", "Earth", "Female", "available", null);
            CabinCrewEntity c45 = new CabinCrewEntity("c" + i + "045", "Rasam", "123", "hahaha@merlion.com", "123", "Earth", "Male", "available", null);
            CabinCrewEntity c46 = new CabinCrewEntity("c" + i + "046", "Jenny", "123", "hahaha@merlion.com", "123", "Earth", "Female", "available", null);
            CabinCrewEntity c47 = new CabinCrewEntity("c" + i + "047", "Dalty", "123", "hahaha@merlion.com", "123", "Earth", "Male", "available", null);
            CabinCrewEntity c48 = new CabinCrewEntity("c" + i + "048", "Sam", "123", "hahaha@merlion.com", "123", "Earth", "Female", "available", null);
            CabinCrewEntity c49 = new CabinCrewEntity("c" + i + "049", "Sunny", "123", "hahaha@merlion.com", "123", "Earth", "Male", "available", null);
            CabinCrewEntity c50 = new CabinCrewEntity("c" + i + "050", "Edwin", "123", "hahaha@merlion.com", "123", "Earth", "Female", "available", null);
            CabinCrewEntity c51 = new CabinCrewEntity("c" + i + "051", "Biggy", "123", "hahaha@merlion.com", "123", "Earth", "Male", "available", null);
            CabinCrewEntity c52 = new CabinCrewEntity("c" + i + "052", "Saravanan", "123", "hahaha@merlion.com", "123", "Earth", "Female", "available", null);
            CabinCrewEntity c53 = new CabinCrewEntity("c" + i + "053", "Bunny", "123", "hahaha@merlion.com", "123", "Earth", "Male", "available", null);
            CabinCrewEntity c54 = new CabinCrewEntity("c" + i + "054", "Wang fei", "123", "hahaha@merlion.com", "123", "Earth", "Female", "available", null);
            CabinCrewEntity c55 = new CabinCrewEntity("c" + i + "055", "Dongyan", "123", "hahaha@merlion.com", "123", "Earth", "Male", "available", null);
            CabinCrewEntity c56 = new CabinCrewEntity("c" + i + "056", "Li Dongyan", "123", "hahaha@merlion.com", "123", "Earth", "Female", "available", null);
            CabinCrewEntity c57 = new CabinCrewEntity("c" + i + "057", "Hao", "123", "hahaha@merlion.com", "123", "Earth", "Male", "available", null);
            CabinCrewEntity c58 = new CabinCrewEntity("c" + i + "058", "Cai Rui", "123", "hahaha@merlion.com", "123", "Earth", "Female", "available", null);
            CabinCrewEntity c59 = new CabinCrewEntity("c" + i + "059", "Cherry", "123", "hahaha@merlion.com", "123", "Earth", "Male", "available", null);
            CabinCrewEntity c60 = new CabinCrewEntity("c" + i + "060", "Wu Tong", "123", "hahaha@merlion.com", "123", "Earth", "Female", "available", null);
            CabinCrewEntity c61 = new CabinCrewEntity("c" + i + "061", "Tong", "123", "hahaha@merlion.com", "123", "Earth", "Male", "available", null);
            CabinCrewEntity c62 = new CabinCrewEntity("c" + i + "062", "Tom", "123", "hahaha@merlion.com", "123", "Earth", "Female", "available", null);
            CabinCrewEntity c63 = new CabinCrewEntity("c" + i + "063", "Tommy", "123", "hahaha@merlion.com", "123", "Earth", "Male", "available", null);
            CabinCrewEntity c64 = new CabinCrewEntity("c" + i + "064", "Liu Jingjing", "123", "hahaha@merlion.com", "123", "Earth", "Female", "available", null);
            CabinCrewEntity c65 = new CabinCrewEntity("c" + i + "065", "Ma Weiwei", "123", "hahaha@merlion.com", "123", "Earth", "Male", "available", null);
            CabinCrewEntity c66 = new CabinCrewEntity("c" + i + "066", "Chui Ji", "123", "hahaha@merlion.com", "123", "Earth", "Female", "available", null);
            CabinCrewEntity c67 = new CabinCrewEntity("c" + i + "067", "Chen Yongkai", "123", "hahaha@merlion.com", "123", "Earth", "Male", "available", null);
            CabinCrewEntity c68 = new CabinCrewEntity("c" + i + "068", "Wu Qingfeng", "123", "hahaha@merlion.com", "123", "Earth", "Female", "available", null);
            CabinCrewEntity c69 = new CabinCrewEntity("c" + i + "069", "Ed", "123", "hahaha@merlion.com", "123", "Earth", "Male", "available", null);
            CabinCrewEntity c70 = new CabinCrewEntity("c" + i + "070", "Ugg", "123", "hahaha@merlion.com", "123", "Earth", "Female", "available", null);
            CabinCrewEntity c71 = new CabinCrewEntity("c" + i + "071", "East", "123", "hahaha@merlion.com", "123", "Earth", "Male", "available", null);
            CabinCrewEntity c72 = new CabinCrewEntity("c" + i + "072", "West", "123", "hahaha@merlion.com", "123", "Earth", "Female", "available", null);
            CabinCrewEntity c73 = new CabinCrewEntity("c" + i + "073", "Lewis", "123", "hahaha@merlion.com", "123", "Earth", "Male", "available", null);
            CabinCrewEntity c74 = new CabinCrewEntity("c" + i + "074", "Xinyi", "123", "hahaha@merlion.com", "123", "Earth", "Female", "available", null);
            CabinCrewEntity c75 = new CabinCrewEntity("c" + i + "075", "Gong Yuqi", "123", "hahaha@merlion.com", "123", "Earth", "Male", "available", null);
            CabinCrewEntity c76 = new CabinCrewEntity("c" + i + "076", "Sodagreen", "123", "hahaha@merlion.com", "123", "Earth", "Female", "available", null);
            CabinCrewEntity c77 = new CabinCrewEntity("c" + i + "077", "Sudam", "123", "hahaha@merlion.com", "123", "Earth", "Male", "available", null);
            CabinCrewEntity c78 = new CabinCrewEntity("c" + i + "078", "Emiley", "123", "hahaha@merlion.com", "123", "Earth", "Female", "available", null);
            CabinCrewEntity c79 = new CabinCrewEntity("c" + i + "079", "Shelton", "123", "hahaha@merlion.com", "123", "Earth", "Male", "available", null);
            CabinCrewEntity c80 = new CabinCrewEntity("c" + i + "080", "Penny", "123", "hahaha@merlion.com", "123", "Earth", "Female", "available", null);
            CabinCrewEntity c81 = new CabinCrewEntity("c" + i + "081", "Shawn", "123", "hahaha@merlion.com", "123", "Earth", "Male", "available", null);
            CabinCrewEntity c82 = new CabinCrewEntity("c" + i + "082", "Aldely", "123", "hahaha@merlion.com", "123", "Earth", "Female", "available", null);
            CabinCrewEntity c83 = new CabinCrewEntity("c" + i + "083", "Sam", "123", "hahaha@merlion.com", "123", "Earth", "Male", "available", null);
            CabinCrewEntity c84 = new CabinCrewEntity("c" + i + "084", "Da Xiong", "123", "hahaha@merlion.com", "123", "Earth", "Female", "available", null);
            CabinCrewEntity c85 = new CabinCrewEntity("c" + i + "085", "Chen Yixun", "123", "hahaha@merlion.com", "123", "Earth", "Male", "available", null);
            CabinCrewEntity c86 = new CabinCrewEntity("c" + i + "086", "Wang Tong", "123", "hahaha@merlion.com", "123", "Earth", "Female", "available", null);
            CabinCrewEntity c87 = new CabinCrewEntity("c" + i + "087", "Lewis Phey", "123", "hahaha@merlion.com", "123", "Earth", "Male", "available", null);
            CabinCrewEntity c88 = new CabinCrewEntity("c" + i + "088", "Shirley", "123", "hahaha@merlion.com", "123", "Earth", "Female", "available", null);
            CabinCrewEntity c89 = new CabinCrewEntity("c" + i + "089", "Warmmer", "123", "hahaha@merlion.com", "123", "Earth", "Male", "available", null);
            CabinCrewEntity c90 = new CabinCrewEntity("c" + i + "090", "Nancy", "123", "hahaha@merlion.com", "123", "Earth", "Female", "available", null);
            CabinCrewEntity c91 = new CabinCrewEntity("c" + i + "091", "Teddious", "123", "hahaha@merlion.com", "123", "Earth", "Male", "available", null);
            CabinCrewEntity c92 = new CabinCrewEntity("c" + i + "092", "Cherry", "123", "hahaha@merlion.com", "123", "Earth", "Female", "available", null);
            CabinCrewEntity c93 = new CabinCrewEntity("c" + i + "093", "Wei Ming", "123", "hahaha@merlion.com", "123", "Earth", "Male", "available", null);
            CabinCrewEntity c94 = new CabinCrewEntity("c" + i + "094", "Scarlett", "123", "hahaha@merlion.com", "123", "Earth", "Female", "available", null);
            CabinCrewEntity c95 = new CabinCrewEntity("c" + i + "095", "Louis", "123", "hahaha@merlion.com", "123", "Earth", "Male", "available", null);
            CabinCrewEntity c96 = new CabinCrewEntity("c" + i + "096", "Scarlet", "123", "hahaha@merlion.com", "123", "Earth", "Female", "available", null);
            CabinCrewEntity c97 = new CabinCrewEntity("c" + i + "097", "Sunny", "123", "hahaha@merlion.com", "123", "Earth", "Male", "available", null);
            CabinCrewEntity c98 = new CabinCrewEntity("c" + i + "098", "Sam", "123", "hahaha@merlion.com", "123", "Earth", "Female", "available", null);
            CabinCrewEntity c99 = new CabinCrewEntity("c" + i + "099", "Sadam", "123", "hahaha@merlion.com", "123", "Earth", "Male", "available", null);
            CabinCrewEntity c100 = new CabinCrewEntity("c" + i + "00", "Fiona", "123", "hahaha@merlion.com", "123", "Earth", "Female", "available", null);

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
            entityManager.persist(c31);
            entityManager.persist(c32);
            entityManager.persist(c33);
            entityManager.persist(c34);
            entityManager.persist(c35);
            entityManager.persist(c36);
            entityManager.persist(c37);
            entityManager.persist(c38);
            entityManager.persist(c39);
            entityManager.persist(c40);
            entityManager.persist(c41);
            entityManager.persist(c42);
            entityManager.persist(c43);
            entityManager.persist(c44);
            entityManager.persist(c45);
            entityManager.persist(c46);
            entityManager.persist(c47);
            entityManager.persist(c48);
            entityManager.persist(c49);
            entityManager.persist(c50);
            entityManager.persist(c51);
            entityManager.persist(c52);
            entityManager.persist(c53);
            entityManager.persist(c54);
            entityManager.persist(c55);
            entityManager.persist(c56);
            entityManager.persist(c57);
            entityManager.persist(c58);
            entityManager.persist(c59);
            entityManager.persist(c60);
            entityManager.persist(c61);
            entityManager.persist(c62);
            entityManager.persist(c63);
            entityManager.persist(c64);
            entityManager.persist(c65);
            entityManager.persist(c66);
            entityManager.persist(c67);
            entityManager.persist(c68);
            entityManager.persist(c69);
            entityManager.persist(c70);
            entityManager.persist(c71);
            entityManager.persist(c72);
            entityManager.persist(c73);
            entityManager.persist(c74);
            entityManager.persist(c75);
            entityManager.persist(c76);
            entityManager.persist(c77);
            entityManager.persist(c78);
            entityManager.persist(c79);
            entityManager.persist(c80);
            entityManager.persist(c81);
            entityManager.persist(c82);
            entityManager.persist(c83);
            entityManager.persist(c84);
            entityManager.persist(c85);
            entityManager.persist(c86);
            entityManager.persist(c87);
            entityManager.persist(c88);
            entityManager.persist(c89);
            entityManager.persist(c90);
            entityManager.persist(c91);
            entityManager.persist(c92);
            entityManager.persist(c93);
            entityManager.persist(c94);
            entityManager.persist(c95);
            entityManager.persist(c96);
            entityManager.persist(c97);
            entityManager.persist(c98);
            entityManager.persist(c99);
            entityManager.persist(c100);

            c1.setBase(a1);
            c2.setBase(a1);
            c3.setBase(a1);
            c4.setBase(a1);
            c5.setBase(a1);
            c6.setBase(a1);
            c7.setBase(a1);
            c8.setBase(a1);
            c9.setBase(a1);
            c10.setBase(a1);
            c11.setBase(a1);
            c12.setBase(a1);
            c13.setBase(a2);
            c14.setBase(a2);
            c15.setBase(a2);
            c16.setBase(a2);
            c17.setBase(a2);
            c18.setBase(a2);
            c19.setBase(a2);
            c20.setBase(a2);
            c21.setBase(a2);
            c22.setBase(a2);
            c23.setBase(a1);
            c24.setBase(a1);
            c25.setBase(a1);
            c26.setBase(a1);
            c27.setBase(a1);
            c28.setBase(a1);
            c29.setBase(a2);
            c30.setBase(a2);
            c31.setBase(a2);
            c32.setBase(a1);
            c33.setBase(a1);
            c34.setBase(a1);
            c35.setBase(a1);
            c36.setBase(a1);
            c37.setBase(a1);
            c38.setBase(a1);
            c39.setBase(a1);
            c40.setBase(a2);
            c41.setBase(a2);
            c42.setBase(a2);
            c43.setBase(a2);
            c44.setBase(a2);
            c45.setBase(a2);
            c46.setBase(a2);
            c47.setBase(a2);
            c48.setBase(a2);
            c49.setBase(a2);
            c50.setBase(a1);
            c51.setBase(a1);
            c52.setBase(a1);
            c53.setBase(a2);
            c54.setBase(a2);
            c55.setBase(a2);
            c56.setBase(a2);
            c57.setBase(a1);
            c58.setBase(a1);
            c59.setBase(a2);
            c60.setBase(a2);
            c61.setBase(a1);
            c62.setBase(a2);
            c63.setBase(a2);
            c64.setBase(a1);
            c65.setBase(a2);
            c66.setBase(a1);
            c67.setBase(a2);
            c68.setBase(a1);
            c69.setBase(a2);
            c70.setBase(a1);
            c71.setBase(a2);
            c72.setBase(a1);
            c73.setBase(a2);
            c74.setBase(a1);
            c75.setBase(a2);
            c76.setBase(a1);
            c77.setBase(a2);
            c78.setBase(a1);
            c79.setBase(a2);
            c80.setBase(a1);
            c81.setBase(a2);
            c82.setBase(a1);
            c83.setBase(a2);
            c84.setBase(a1);
            c85.setBase(a2);
            c86.setBase(a1);
            c87.setBase(a2);
            c88.setBase(a1);
            c89.setBase(a2);
            c90.setBase(a1);
            c91.setBase(a2);
            c92.setBase(a1);
            c93.setBase(a2);
            c94.setBase(a1);
            c95.setBase(a2);
            c96.setBase(a1);
            c97.setBase(a2);
            c98.setBase(a1);
            c99.setBase(a2);
            c100.setBase(a1);

        }

        System.out.println("300 Pilots are created");
        System.out.println("1000 Cabin crew are created.");
    }

    @Override
    public StaffEntity fetchStaff(String staffNo) {
        Query query = entityManager.createQuery("SELECT s FROM StaffEntity s WHERE s.staffNo = :staffNumber");
        query.setParameter("staffNumber", staffNo);
        List<StaffEntity> staffs = (List<StaffEntity>) query.getResultList();
        System.out.print(staffs);
        return staffs.get(0);
    }

    @Override
    public void tempUseInsertPilot() {

    }

    public void persist(Object object) {
        entityManager.persist(object);
    }

}
