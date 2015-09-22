/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package imas.common.sessionbean;

import imas.common.entity.StaffEntity;
import java.util.List;
import javax.ejb.Local;

/**
 *
 * @author Howard
 */
@Local
public interface AccountManagementSessionBeanLocal {

    void resetStaffPassword(String email);
    
    Boolean addStaff(String staffNo, String name, String email, String contactNumber, String address, String gender, String businessUnit, String division, String position, String location);

    Boolean checkEmailExistence(String email);

    List<StaffEntity> fetchStaff();

    void deleteStaff(String staffNo);

    void updateStaff(String staffNo, String email, String contactNumber, String address);

    StaffEntity getStaff(String staffNo);

    List<String> fetchBases();

    void activateAccount(String staffNo);


}
