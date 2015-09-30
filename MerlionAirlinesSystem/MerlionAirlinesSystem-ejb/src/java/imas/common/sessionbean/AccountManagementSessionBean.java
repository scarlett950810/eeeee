/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package imas.common.sessionbean;

import imas.common.entity.CabinCrewEntity;
import imas.common.entity.PilotEntity;
import imas.common.entity.StaffEntity;
import imas.common.entity.StaffRole;
import imas.planning.entity.AirportEntity;
import imas.planning.entity.FlightEntity;
import imas.utility.sessionbean.EmailManager;
import static java.lang.Boolean.TRUE;
import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.UUID;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.ejb.Stateless;
import javax.mail.MessagingException;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import util.security.CryptographicHelper;

/**
 *
 * @author Howard
 */
@Stateless
public class AccountManagementSessionBean implements AccountManagementSessionBeanLocal {

    private static final Random RANDOM = new SecureRandom();
    public static final int SALT_LENGTH = 8;
    CryptographicHelper cp = new CryptographicHelper();
    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public void resetStaffPassword(String email) {
        String password = generatePassword();
        String tempPassword;

        Query query = entityManager.createQuery("SELECT s FROM StaffEntity s WHERE s.email = :email");
        query.setParameter("email", email);
        try {
            StaffEntity staff = (StaffEntity) query.getSingleResult();
            tempPassword = cp.doMD5Hashing(password + staff.getSalt());
            staff.setPassword(tempPassword);
        } catch (NoResultException exception) {
            System.out.println("No such staff");
        }

        try {
            sendResetEmail(email, password);
        } catch (MessagingException ex) {
            Logger.getLogger(AccountManagementSessionBean.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private void sendResetEmail(String email, String password) throws MessagingException {
        String subject = "Test Email Function";
        String content = "Hello world! Welcome to the Merlion Airline Internal System. You temporary password is: " + password;
        System.out.print(password);
        EmailManager.run(email, subject, content);
    }

    private void sendNewStaffEmail(String email, String password, String staffName, String staffNo) throws MessagingException {
        String subject = "Welcome to Merlion Airlines";
        String content = "Hi " + staffName + ", " + "<br><br>Welcome to Merlion Airlines and start your dream career here.<br><br>Please refer below for the details to access the internal system: <br><br><br>Staff Number: " + staffNo + "<br><br>Password: " + password + "<br><br>Thank you.<br><br>Merlion Airline HR Manager";
        EmailManager.run(email, subject, content);
    }

    @Override
    public Boolean addStaff(String staffNo, String name, String email, String contactNumber, String address, String gender, String businessUnit, String division, String position, String location, String base, String workingStatus, List<String> aircraftTypeCapabilities, Boolean mileageLimit, Boolean isPilot, Boolean isCabinCrew) {
        String password = generatePassword();
        System.out.print(staffNo);
        System.out.print(name);
        System.out.print(email);
        System.out.print(contactNumber);
        System.out.print(address);
        System.out.print(gender);
        System.out.print(businessUnit);
        System.out.print(division);
        System.out.print(position);
        System.out.print(location);
        System.out.print(base);
        System.out.print(isPilot);
        System.out.print(isCabinCrew);
        String tempPassword;
        String salt = "";
        String letters = "0123456789abcdefghjkmnpqrstuvwxyzABCDEFGHJKMNPQRSTUVWXYZ23456789";
        for (int i = 0; i < SALT_LENGTH; i++) {
            int index = (int) (RANDOM.nextDouble() * letters.length());
            salt += letters.substring(index, index + 1);
        }

        tempPassword = cp.doMD5Hashing(password + salt);

        Query query = entityManager.createQuery("SELECT s FROM StaffEntity s WHERE s.staffNo = :staffNumber");
        query.setParameter("staffNumber", staffNo);

        List<StaffEntity> staffs = (List<StaffEntity>) query.getResultList();
        if (staffs.isEmpty()) {

            if (isPilot == false && isCabinCrew == false) {

                StaffEntity staff = new StaffEntity(staffNo, name, tempPassword, email, contactNumber, address, gender);
                StaffRole role = new StaffRole(businessUnit, position, division, location, null);

                entityManager.persist(role);

                staff.setRole(role);
                entityManager.persist(staff);
                staff.setSalt(salt);

                if (base != null) {
                    query = entityManager.createQuery("SELECT a FROM AirportEntity a WHERE a.airportCode = :base");
                    query.setParameter("base", base);
                    AirportEntity airport = (AirportEntity) query.getSingleResult();

                    staff.setBase(airport);
                }

            } else if (isPilot == true) {

                PilotEntity pilot = new PilotEntity(staffNo, name, tempPassword, email,
                        contactNumber, address, gender, workingStatus, aircraftTypeCapabilities, null, mileageLimit);
                StaffRole role = new StaffRole(businessUnit, position, division, location, null);

                query = entityManager.createQuery("SELECT a FROM AirportEntity a WHERE a.airportCode = :base");
                query.setParameter("base", base);
                AirportEntity airport = (AirportEntity) query.getSingleResult();

                entityManager.persist(role);

                pilot.setRole(role);
                entityManager.persist(pilot);
                pilot.setSalt(salt);
                pilot.setBase(airport);
            } else if (isCabinCrew == true) {

                CabinCrewEntity cabinCrew = new CabinCrewEntity(staffNo, name, tempPassword, email, contactNumber,
                        address, gender, "available", null);
                StaffRole role = new StaffRole(businessUnit, position, division, location, null);

                query = entityManager.createQuery("SELECT a FROM AirportEntity a WHERE a.airportCode = :base");
                query.setParameter("base", base);
                AirportEntity airport = (AirportEntity) query.getSingleResult();

                entityManager.persist(role);

                cabinCrew.setRole(role);
                entityManager.persist(cabinCrew);
                cabinCrew.setSalt(salt);
                cabinCrew.setBase(airport);
            }

            assignAccessRight(staffNo, businessUnit, division, position);

            try {
                sendNewStaffEmail(email, password, name, staffNo);
            } catch (MessagingException ex) {
                Logger.getLogger(AccountManagementSessionBean.class.getName()).log(Level.SEVERE, null, ex);
            }
            return true;

        } else {
            return false;
        }

    }

    private String generatePassword() {
        String password = UUID.randomUUID().toString();
        password = password.replaceAll("-", "").substring(0, 8);
        System.out.println("uuid = " + password);
        return password;
    }

    @Override
    public Boolean checkEmailExistence(String email) {
        Query query = entityManager.createQuery("SELECT s FROM StaffEntity s WHERE s.email = :email");
        query.setParameter("email", email);

        List<StaffEntity> staff = (List<StaffEntity>) query.getResultList();
        if (staff.isEmpty()) {
            return false;
        } else {
            return true;
        }
    }

    @Override
    public List<StaffEntity> fetchStaff() {
        Query query = entityManager.createQuery("SELECT s FROM StaffEntity s");

        List<StaffEntity> staff = (List<StaffEntity>) query.getResultList();
        return staff;
    }

    @Override
    public void deleteStaff(String staffNo) {
        Query query = entityManager.createQuery("DELETE FROM StaffEntity s WHERE s.staffNo = :staffNo");
        query.setParameter("staffNo", staffNo);
        query.executeUpdate();
        System.out.println("staff deleted");
    }

    @Override
    public void updateStaff(StaffEntity staffEntity) {
//        Query query = entityManager.createQuery("SELECT s FROM StaffEntity s WHERE s.staffNo = :staffNo");
//        query.setParameter("staffNo", staffNo);

        entityManager.merge(staffEntity);
        System.out.println(staffEntity);
//        List<StaffEntity> staffs = (List<StaffEntity>) query.getResultList();
//        if (staffs.isEmpty()) {
//            System.out.print("no such user");
//        } else {
//            StaffEntity staff = staffs.get(0);
//            staff.setEmail(email);
//            staff.setContactNumber(contactNumber);
//            staff.setAddress(address);
//
//        }

    }

    @Override
    public StaffEntity getStaff(String staffNo) {
        Query query = entityManager.createQuery("SELECT s FROM StaffEntity s WHERE s.staffNo = :staffNo");
        query.setParameter("staffNo", staffNo);

        List<StaffEntity> staffs = (List<StaffEntity>) query.getResultList();
        if (staffs.isEmpty()) {
            System.out.print("no such user");
            return null;
        } else {
            return staffs.get(0);
        }
    }

    @Override
    public List<String> fetchBases() {
        Query query = entityManager.createQuery("SELECT a.airportCode FROM AirportEntity a");

        List<String> bases = (List<String>) query.getResultList();
        if (bases.isEmpty()) {
            System.out.print("no results");
            return null;
        } else {
            return bases;
        }
    }

    @Override
    public void activateAccount(String staffNo) {
        Query query = entityManager.createQuery("SELECT s FROM StaffEntity s WHERE s.staffNo = :staffNo");
        query.setParameter("staffNo", staffNo);

        List<StaffEntity> staffs = (List<StaffEntity>) query.getResultList();

        if (!staffs.isEmpty()) {
            StaffEntity staff = staffs.get(0);
            staff.setActivationStatus(TRUE);
            System.out.print(staff.getActivationStatus());
        } else {
            System.out.print("The staff does not exist");
        }
    }

    @Override
    public void createRootUser() {
        Query query = entityManager.createQuery("SELECT s FROM StaffEntity s WHERE s.staffNo = 'admin'");
        List<StaffEntity> staffs = (List<StaffEntity>) query.getResultList();
        if (staffs.isEmpty()) {
            String password = "123";
            String tempPassword;
            String salt = "";
            String letters = "0123456789abcdefghjkmnpqrstuvwxyzABCDEFGHJKMNPQRSTUVWXYZ23456789";
            for (int i = 0; i < SALT_LENGTH; i++) {
                int index = (int) (RANDOM.nextDouble() * letters.length());
                salt += letters.substring(index, index + 1);
            }

            tempPassword = cp.doMD5Hashing(password + salt);
            ArrayList<String> accessRight = new ArrayList<>();
            accessRight.add("all");
            StaffEntity staff1 = new StaffEntity("admin", "System Administrator", tempPassword, "systemadmin@merlionairline.sg", "12345678", "ABC Street", "male");

            entityManager.persist(staff1);

            StaffRole role1 = new StaffRole("Administration", "Manager", "Information Technology", "Singapore", accessRight);

            entityManager.persist(role1);

            staff1.setSalt(salt);
            staff1.setRole(role1);

        }
    }

    @Override
    public AirportEntity fetchBase(String base) {
        Query query = entityManager.createQuery("SELECT a FROM AirportEntity a WHERE a.airportCode = :base");
        query.setParameter("base", base);

        List<AirportEntity> airports = (List<AirportEntity>) query.getResultList();

        if (airports.isEmpty()) {
            System.out.print("This is an invalid airport code");
            return null;
        } else {
            return airports.get(0);
        }

    }

    @Override
    public void assignAccessRight(String staffNo, String businessUnit, String division, String position) {
        ArrayList<String> accessRight = new ArrayList<>();

        Query query = entityManager.createQuery("SELECT s FROM StaffEntity s WHERE s.staffNo = :staffNumber");
        query.setParameter("staffNumber", staffNo);

        List<StaffEntity> staffs = (List<StaffEntity>) query.getResultList();
        StaffEntity staff = staffs.get(0);

        accessRight.add("/common/userProfile.xhtml");
        accessRight.add("/common/common_landing.xhtml");
        accessRight.add("/operation/operationDisplayFlights.xhtml");

        if (businessUnit.equals("Operation")) {
            accessRight.add("/operation/operationHomePage.xhtml");
            if (division.equals("Crew Management")) {
                
            } else if (division.equals("Cockpit Crew")) {
                accessRight.add("/operation/operationPostFlightReport.xhtml");
                accessRight.add("/operation/retrieveDuty.xhtml");
            } else if (division.equals("Cabin Crew")) {
                accessRight.add("/operation/retrieveDuty.xhtml");
            } else if (division.equals("planning")) {
                accessRight.add("/operation/viewFlightSchedule.xhtml");
                accessRight.add("/operation/viewMaintenanceSchedule.xhtml");
                accessRight.add("/planning/planningHomePage.xhtml");
                if (position.toLowerCase().equals("manager")) {
                    accessRight.add("/planning/planningAddAircraft.xhtml");
                    accessRight.add("/planning/planningAddAircraftType.xhtml");
                    accessRight.add("/planning/planningAddRoute.xhtml");
                    accessRight.add("/planning/planningDeleteAircraftType.xhtml");
                    accessRight.add("/planning/planningAddAirport.xhtml");
                    accessRight.add("/planning/planningDeleteAirport.xhtml");
                    accessRight.add("/planning/planningDeleteRoute.xhtml");
                    accessRight.add("/planning/planningEditDeleteAircraft.xhtml");
                    accessRight.add("/planning/planningFleetAssignment.xhtml");
                    accessRight.add("/planning/planningFleetAssignmentDisplay.xhtml");
                }else if (position.toLowerCase().equals("staff")) {
                    accessRight.add("/planning/planningAirport.xhtml");
                    accessRight.add("/planning/planningAircraftType.xhtml");
                    accessRight.add("/planning/planningManageAircraftType.xhtml");
                    accessRight.add("/planning/planningRoute.xhtml");
                    accessRight.add("/planning/planningSetFrequency.xhtml");
                    accessRight.add("/planning/planningSetSchedulePerDay.xhtml");
                    accessRight.add("/planning/planningSetSchedulePerWeek.xhtml");
                }
            }

        } else if (businessUnit.equals("Maintenance")) {
            accessRight.add("/operation/viewMaintenanceSchedule.xhtml");

        } else if (businessUnit.equals("Administration")) {
            if (division.equals("Human Resources")) {
                accessRight.add("/systemAdmin/systemAdminHome.xhtml");
                accessRight.add("/systemAdmin/systemAdminAddStaff.xhtml");
                accessRight.add("/systemAdmin/systemAdminSendAnnouncement.xhtml");
                accessRight.add("/systemAdmin/systemAdminViewStaff.xhtml");
            } else if (division.equals("Information Technology")) {
                System.out.print("added all");
                accessRight.add("all");
            }

        } else if (businessUnit.equals("Sales and Marketing")) {
            if (division.equals("Sales")) {
                accessRight.add("/inventory/inventoryBookingClassManagement.xhtml");
                accessRight.add("/inventory/inventoryCost.xhtml");
                accessRight.add("/inventory/inventoryHomePage.xhtml");
                accessRight.add("/inventory/inventoryRevenueManagement.xhtml");
                accessRight.add("/inventory/inventoryRulesManagement.xhtml");
                accessRight.add("/inventory/inventorySeatsManagement.xhtml");
            }
        } else if (businessUnit.equals("Operation Control")) {
            if (division.equals("Ground Crew")){
                accessRight.add("/operation/operationCrewBoarding.xhtml");
                accessRight.add("/operation/OperationCrewCheckIn.xhtml");
            }
        }

        staff.getRole().setAccessRight(accessRight);
    }

}
