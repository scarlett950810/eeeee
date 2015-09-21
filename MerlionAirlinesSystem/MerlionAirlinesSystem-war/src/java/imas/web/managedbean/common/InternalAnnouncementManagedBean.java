/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package imas.web.managedbean.common;

import imas.common.entity.InternalAnnouncementEntity;
import imas.common.sessionbean.InternalAnnouncementSessionBeanLocal;
import imas.planning.sessionbean.AirportSessionBeanLocal;
import imas.planning.entity.AirportEntity;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;
import javax.inject.Named;
import javax.persistence.PostRemove;

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
    @EJB
    private AirportSessionBeanLocal airportSessionBean;

    private List<String> departments;

    private List<AirportEntity> airports;

    private String title;

    private String content;

    private List<AirportEntity> airportList;

    private List<InternalAnnouncementEntity> allAnnouncements;

    @PostConstruct
    public void init() {
        FacesContext.getCurrentInstance().getExternalContext().getSessionMap().put("airportList", this.airportList);
        String staffNo = (String) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("staffNo");
        allAnnouncements = (List<InternalAnnouncementEntity>) internalAnnouncementSessionBean.getAllAnnouncements(staffNo);
    
    }

    @PostRemove
    public void destroy() {
        FacesContext.getCurrentInstance().getExternalContext().getSessionMap().remove("airportList");
    }

    public InternalAnnouncementManagedBean() {

    }

    public List<String> getDepartments() {
        return departments;
    }

    public void setDepartments(List<String> departments) {
        this.departments = departments;
    }

    public List<AirportEntity> getAirports() {
        return airports;
    }

    public void setAirports(List<AirportEntity> airports) {
        this.airports = airports;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public List<AirportEntity> getAirportList() {
//        System.out.println("InternalAnnouncementManagedBean.getAirportList called.");
//        System.out.println(airportSessionBean);
//        System.out.println(airportSessionBean.fetchAirport());
        return airportSessionBean.fetchAirport();
    }

    public void setAirportList(List<AirportEntity> airportList) {
        this.airportList = airportList;
    }

    public void sendInternalAnnouncements() {
//        System.out.println("internalAccouncementManagedBean.send called.");
//        System.out.println(airports);
        List<String> airportCodes = new ArrayList();
        airports.stream().forEach((a) -> {
            airportCodes.add(a.getAirportCode());
        });

        String message = internalAnnouncementSessionBean.sendInternalAnnouncements(this.departments, airportCodes, this.title, this.content);

//        System.out.println(message);
        FacesMessage msg = new FacesMessage(message, "");
        FacesContext.getCurrentInstance().addMessage(null, msg);
    }

    public List<InternalAnnouncementEntity> getAllAnnouncements() {
        return this.allAnnouncements;
    }

    public void setAllAnnouncements(List<InternalAnnouncementEntity> allAnnouncements) {
        this.allAnnouncements = allAnnouncements;
    }
    
    public void toggleRead(InternalAnnouncementEntity announcementEntity) {
        internalAnnouncementSessionBean.toggleRead(announcementEntity);
    }

//    public String getFormatTime(Date d){
//        SimpleDateFormat dateF = new SimpleDateFormat("yyyy/MM/dd 'at' HH:mm:ss z\"");
//        return dateF.format(d);
//    }

}
