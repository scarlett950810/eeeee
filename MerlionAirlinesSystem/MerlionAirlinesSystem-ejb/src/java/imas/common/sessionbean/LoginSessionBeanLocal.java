/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package imas.common.sessionbean;

import imas.common.entity.StaffEntity;
import javax.ejb.Local;

/**
 *
 * @author Howard
 */
@Local
public interface LoginSessionBeanLocal {

    public String doLogin(String staffNo, String password);
//    public void setPass(String staffNo, String password);
    public void insertData();

    public StaffEntity fetchStaff(String staffNo);

    public Integer getLeftChance();

    
}
