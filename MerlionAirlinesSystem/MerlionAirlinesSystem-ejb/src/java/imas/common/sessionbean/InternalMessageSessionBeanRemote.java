/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package imas.common.sessionbean;

import imas.common.entity.InternalMessageEntity;
import imas.common.entity.StaffEntity;
import java.util.List;
import javax.ejb.Remote;

/**
 *
 * @author Lei
 */
@Remote
public interface InternalMessageSessionBeanRemote {

    public List<StaffEntity> getAllStaff();

    public StaffEntity getStaffEntityByStaffNo(String staffNo);

    public void sendMessage(StaffEntity loggedInStaff, StaffEntity receiver, String content);

    public List<InternalMessageEntity> getAllMessages(StaffEntity staff);

    public void toggleRead(InternalMessageEntity message);

}
