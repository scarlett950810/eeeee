/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package imas.common.sessionbean;

import imas.common.entity.InternalAnnouncementEntity;
import imas.planning.entity.AirportEntity;
import java.util.List;
import javax.ejb.Remote;

/**
 *
 * @author Lei
 */
@Remote
public interface InternalAnnouncementSessionBeanRemote {

    public List<InternalAnnouncementEntity> getAllAnnouncements(String staffNo);

    public String sendInternalAnnouncements(List<String> departments, List<AirportEntity> bases, String title, String content);

    public void toggleRead(InternalAnnouncementEntity internalAnnouncementEntity);
}
