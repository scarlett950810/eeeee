/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package imas.common.sessionbean;

import imas.common.entity.InternalAnnouncementEntity;
import imas.common.entity.StaffEntity;
import java.util.List;
import javax.ejb.Local;

/**
 *
 * @author Howard
 */
@Local
public interface LoginSessionBeanLocal {

    public StaffEntity doLogin(String staffNo, String password);

    public List<InternalAnnouncementEntity> getAllAnnoucements(StaffEntity staffEntity);

    public List<InternalAnnouncementEntity> getUnreadAnnoucements(StaffEntity staffEntity);
    
}
