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
public interface LoginSessionBeanLocal {

    public boolean doLogin(String staffNo, String password);
    
    public void insertData();
}
