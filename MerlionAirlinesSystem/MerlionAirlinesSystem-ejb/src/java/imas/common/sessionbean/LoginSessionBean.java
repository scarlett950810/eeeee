/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package imas.common.sessionbean;

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
        
        //insertData();
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
         System.err.println("dd");
       
        AircraftTypeEntity aircraftType1 = new AircraftTypeEntity("A380", (double)10000, 50, (double) 100000, (double) 600, (double) 3000, (double) 4400, (double) 20, "Gas", (double) 1000);
        AircraftTypeEntity aircraftType2 = new AircraftTypeEntity("A880", (double)5000, 80, (double) 180000, (double) 450, (double) 3800, (double) 6400, (double) 28, "Gas", (double) 800);
        AircraftTypeEntity aircraftType3 = new AircraftTypeEntity("B777", (double)10000, 80, (double) 180000, (double) 550, (double) 3800, (double) 6400, (double) 28, "Gas", (double) 1000);
        AircraftTypeEntity aircraftType4 = new AircraftTypeEntity("B787", (double)12000, 80, (double) 180000, (double) 600, (double) 3800, (double) 6400, (double) 28, "Gas", (double) 900);
        AircraftTypeEntity aircraftType5 = new AircraftTypeEntity("B700", (double)8000, 80, (double) 180000, (double) 580, (double) 3800, (double) 6400, (double) 28, "Gas", (double) 900);
        System.err.println("dd");
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
        aircraftSessionBean.addAircraft("004", aircraftType2, (double) 20000000, (double) 1000000, (double) 19000000, (double) 18, (double) 0, "All is well", a5, a5, 4, 5, 4, 6, 6, 30, 7, 30, (double) 50);
        aircraftSessionBean.addAircraft("005", aircraftType2, (double) 20000000, (double) 1000000, (double) 19000000, (double) 18, (double) 0, "All is well", a7, a7, 4, 5, 4, 6, 6, 30, 7, 30, (double) 47);
        aircraftSessionBean.addAircraft("006", aircraftType2, (double) 20000000, (double) 1000000, (double) 19000000, (double) 20, (double) 0, "All is well", a7, a7, 0, 0, 4, 6, 6, 30, 7, 50, (double) 42);
        aircraftSessionBean.addAircraft("007", aircraftType3, (double) 20000000, (double) 1000000, (double) 19000000, (double) 18, (double) 0, "All is well", a8, a8, 4, 5, 4, 6, 6, 30, 7, 30, (double) 50);
        aircraftSessionBean.addAircraft("008", aircraftType3, (double) 20000000, (double) 1000000, (double) 19000000, (double) 18, (double) 0, "All is well", a15, a15, 4, 5, 4, 6, 6, 30, 7, 30, (double) 47);
        aircraftSessionBean.addAircraft("009", aircraftType3, (double) 20000000, (double) 1000000, (double) 19000000, (double) 20, (double) 0, "All is well", a5, a5, 0, 0, 4, 6, 6, 30, 7, 50, (double) 30);
        aircraftSessionBean.addAircraft("010", aircraftType4, (double) 20000000, (double) 1000000, (double) 19000000, (double) 18, (double) 0, "All is well", a15, a15, 4, 5, 4, 6, 6, 30, 7, 30, (double) 65);
        aircraftSessionBean.addAircraft("011", aircraftType4, (double) 20000000, (double) 1000000, (double) 19000000, (double) 18, (double) 0, "All is well", a7, a7, 4, 5, 4, 6, 6, 30, 7, 30, (double) 37);
        aircraftSessionBean.addAircraft("012", aircraftType4, (double) 20000000, (double) 1000000, (double) 19000000, (double) 20, (double) 0, "All is well", a7, a7, 0, 0, 4, 6, 6, 30, 7, 50, (double) 51);
        aircraftSessionBean.addAircraft("013", aircraftType5, (double) 20000000, (double) 1000000, (double) 19000000, (double) 18, (double) 0, "All is well", a5, a5, 4, 5, 4, 6, 6, 30, 7, 30, (double) 50);
        aircraftSessionBean.addAircraft("014", aircraftType5, (double) 20000000, (double) 1000000, (double) 19000000, (double) 18, (double) 0, "All is well", a8, a8, 4, 5, 4, 6, 6, 30, 7, 30, (double) 47);
        aircraftSessionBean.addAircraft("015", aircraftType5, (double) 20000000, (double) 1000000, (double) 19000000, (double) 20, (double) 0, "All is well", a7, a7, 0, 0, 4, 6, 6, 30, 7, 50, (double) 42);

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

        PilotEntity p1 = new PilotEntity ("p001", "Tom", "123456", "abc@163.com", "123", "Operation", "Street No 1", "male", "available", l1, null, false);
        PilotEntity p2 = new PilotEntity ("p002", "Tommy", "123456", "abc@163.com", "123", "Operation", "Street No 1", "male", "available", l2, null, true);
        PilotEntity p3 = new PilotEntity ("p003", "Kurt", "123456", "abc@163.com", "123", "Operation", "Street No 1", "male", "available", l3, null, false);
        PilotEntity p4 = new PilotEntity ("p004", "Yin Lei", "123456", "abc@163.com", "123", "Operation", "Street No 1", "male", "available", l4, null, true);
        PilotEntity p5 = new PilotEntity ("p005", "Hao", "123456", "abc@163.com", "123", "Operation", "Street No 1", "male", "available", l5, null, false);
        PilotEntity p6 = new PilotEntity ("p006", "Jerry", "123456", "abc@163.com", "123", "Operation", "Street No 1", "female", "available", l6, null, true);
        PilotEntity p7 = new PilotEntity ("p007", "Dog", "123456", "abc@163.com", "123", "Operation", "Street No 1", "male", "available", l7, null, false);
        PilotEntity p8 = new PilotEntity ("p008", "Cat", "123456", "abc@163.com", "123", "Operation", "Street No 1", "male", "available", l8, null, true);
        PilotEntity p9 = new PilotEntity ("p009", "Pig", "123456", "abc@163.com", "123", "Operation", "Street No 1", "male", "available", l9, null, false);
        PilotEntity p10 = new PilotEntity ("p010", "Zebra", "123456", "abc@163.com", "123", "Operation", "Street No 1", "male", "available", l10, null, true);
        PilotEntity p11 = new PilotEntity ("p011", "Ant", "123456", "abc@163.com", "123", "Operation", "Street No 1", "male", "available", l1, null, false);
        PilotEntity p12 = new PilotEntity ("p012", "Flower", "123456", "abc@163.com", "123", "Operation", "Street No 1", "male", "available", l2, null, true);
        PilotEntity p13 = new PilotEntity ("p013", "Bird", "123456", "abc@163.com", "123", "Operation", "Street No 1", "male", "available", l3, null, false);
        PilotEntity p14 = new PilotEntity ("p014", "Butterfly", "123456", "abc@163.com", "123", "Operation", "Street No 1", "female", "available", l4, null, true);
        PilotEntity p15 = new PilotEntity ("p015", "Kimberly", "123456", "abc@163.com", "123", "Operation", "Street No 1", "male", "available", l5, null, false);
        PilotEntity p16 = new PilotEntity ("p016", "One", "123456", "abc@163.com", "123", "Operation", "Street No 1", "male", "available", l6, null, false);
        PilotEntity p17 = new PilotEntity ("p017", "Two", "123456", "abc@163.com", "123", "Operation", "Street No 1", "male", "available", l7, null, true);
        PilotEntity p18 = new PilotEntity ("p018", "Three", "123456", "abc@163.com", "123", "Operation", "Street No 1", "male", "available", l8, null, false);
        PilotEntity p19 = new PilotEntity ("p019", "Four", "123456", "abc@163.com", "123", "Operation", "Street No 1", "male", "available", l9, null, true);
        PilotEntity p20 = new PilotEntity ("p020", "Five", "123456", "abc@163.com", "123", "Operation", "Street No 1", "male", "available", l10, null, false);
        PilotEntity p21 = new PilotEntity ("p021", "Six", "123456", "abc@163.com", "123", "Operation", "Street No 1", "female", "available", l1, null, true);
        PilotEntity p22 = new PilotEntity ("p022", "Seven", "123456", "abc@163.com", "123", "Operation", "Street No 1", "male", "available", l2, null, false);
        PilotEntity p23 = new PilotEntity ("p023", "Eight", "123456", "abc@163.com", "123", "Operation", "Street No 1", "male", "available", l3, null, true);
        PilotEntity p24 = new PilotEntity ("p024", "Nine", "123456", "abc@163.com", "123", "Operation", "Street No 1", "male", "available", l4, null, false);
        PilotEntity p25 = new PilotEntity ("p025", "Ten", "123456", "abc@163.com", "123", "Operation", "Street No 1", "male", "available", l5, null, true);
        PilotEntity p26 = new PilotEntity ("p026", "Elevan", "123456", "abc@163.com", "123", "Operation", "Street No 1", "male", "available", l6, null, false);
        PilotEntity p27 = new PilotEntity ("p027", "Twlve", "123456", "abc@163.com", "123", "Operation", "Street No 1", "male", "available", l7, null, true);
        PilotEntity p28 = new PilotEntity ("p028", "Thirteen", "123456", "abc@163.com", "123", "Operation", "Street No 1", "male", "available", l8, null, false);
        PilotEntity p29 = new PilotEntity ("p029", "Fourteen", "123456", "abc@163.com", "123", "Operation", "Street No 1", "female", "available", l9, null, true);
        PilotEntity p30 = new PilotEntity ("p030", "Fifteen", "123456", "abc@163.com", "123", "Operation", "Street No 1", "male", "available", l10, null, false);

        System.out.println("Pilots are created");
        
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
        p1.setBase(a4);
        p2.setBase(a4);
        p3.setBase(a4);
        p4.setBase(a4);
        p5.setBase(a4);
        p6.setBase(a4);
        p7.setBase(a4);
        p8.setBase(a4);
        p9.setBase(a4);
        p10.setBase(a4);
        p11.setBase(a8);
        p12.setBase(a8);
        p13.setBase(a8);
        p14.setBase(a8);
        p15.setBase(a8);
        p16.setBase(a8);
        p17.setBase(a8);
        p18.setBase(a8);
        p19.setBase(a8);
        p20.setBase(a8);
        p21.setBase(a8);
        p22.setBase(a8);
        p23.setBase(a8);
        p24.setBase(a8);
        p25.setBase(a8);
        p26.setBase(a4);
        p27.setBase(a4);
        p28.setBase(a4);
        p29.setBase(a4);
        p30.setBase(a4);

        //下面都不是我写的，不太知道删与否，你们如果不用了就自己删一下哈：）

       

    }
   
    
    @Override
    public StaffEntity fetchStaff(String staffNo) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

}
