/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package imas.common.sessionbean;

import javax.ejb.Local;

/**
 *
 * @author Howard
 */
@Local
public interface AccountManagementSessionBeanLocal {

    void resetStaffPassword(String email);
    Boolean addStaff(String staffNo, String name, String email, String contactNumber, String address, String gender, String base, String department);

    Boolean checkEmailExistence(String email);


}
