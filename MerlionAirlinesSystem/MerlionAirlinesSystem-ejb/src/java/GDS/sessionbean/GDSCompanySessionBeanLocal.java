/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package GDS.sessionbean;

import javax.ejb.Local;

/**
 *
 * @author Scarlett
 */
@Local
public interface GDSCompanySessionBeanLocal {
    public boolean accountNotExist(String account);
    public void register(String account, String password, String name, String HQCountry, String mainPage, String email, String contactNo);
    public boolean logIn(String account, String password);
}
