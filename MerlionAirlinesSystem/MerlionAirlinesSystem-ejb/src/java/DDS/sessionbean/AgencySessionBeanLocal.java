/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package DDS.sessionbean;

import DDS.entity.AgencyEntity;
import javax.ejb.Local;

/**
 *
 * @author Scarlett
 */
@Local
public interface AgencySessionBeanLocal {

    boolean accountExist(String account);
    public boolean checkLogin(String account, String pin);
    public void createAgency(String account, String pin, String name, String contactNumber, String email);
    public AgencyEntity getAgency(String account);

    
    
}
