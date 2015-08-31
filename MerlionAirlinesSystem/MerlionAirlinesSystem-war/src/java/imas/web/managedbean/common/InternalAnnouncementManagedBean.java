/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package imas.web.managedbean.common;

import imas.common.entity.InternalAnnouncementEntity;
import imas.common.sessionbean.InternalAnnouncementSessionBeanLocal;
import java.util.List;
import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.inject.Named;

/**
 *
 * @author Scarlett
 */
@Named(value = "internalAnnouncementManagedBean")
@ManagedBean
@SessionScoped
public class InternalAnnouncementManagedBean {

    @EJB
    private InternalAnnouncementSessionBeanLocal internalAnnouncementSessionBean;

    private List<InternalAnnouncementEntity> allAnnouncements;

    public List<InternalAnnouncementEntity> getAllAnnouncements() {
        System.out.println("IAMB called.");
        System.out.print(internalAnnouncementSessionBean.getAllAnnouncements());
        return (List<InternalAnnouncementEntity>) internalAnnouncementSessionBean.getAllAnnouncements();
    }

    public void setAllAnnouncements(List<InternalAnnouncementEntity> allAnnouncements) {
        this.allAnnouncements = allAnnouncements;
    }

}
