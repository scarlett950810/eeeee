/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package imas.common.sessionbean;

import imas.common.entity.InternalAnnouncementEntity;
import java.util.List;
import javax.ejb.Local;

/**
 *
 * @author Scarlett
 */
@Local
public interface InternalAnnouncementSessionBeanLocal {

    public List<InternalAnnouncementEntity> getAllAnnouncements(String staffNo);
    
    public String sendInternalAnnouncements(List<String> departments, List<String> bases, String title, String content);

}
