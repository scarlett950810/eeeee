/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package imas.web.managedbean.common;

import imas.common.entity.InternalAnnouncementEntity;
import imas.common.entity.StaffEntity;
import imas.common.entity.StaffRole;
import imas.common.sessionbean.InternalAnnouncementSessionBeanLocal;
import imas.common.sessionbean.InternalMessageSessionBeanLocal;
import imas.planning.sessionbean.AirportSessionBeanLocal;
import imas.planning.entity.AirportEntity;
import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;
import javax.inject.Named;
import javax.persistence.PostRemove;
import org.primefaces.context.RequestContext;

/**
 *
 * @author Scarlett
 */
@Named(value = "internalAnnouncementManagedBean")
@ManagedBean
@SessionScoped
public class InternalAnnouncementManagedBean implements Serializable {
    @EJB
    private InternalMessageSessionBeanLocal internalMessageSessionBean;

    @EJB
    private InternalAnnouncementSessionBeanLocal internalAnnouncementSessionBean;
    @EJB
    private AirportSessionBeanLocal airportSessionBean;
    
    
    private String loggedInStaffNo;

    private List<String> departments;

    private List<AirportEntity> airports;

    private String title;

    private String content;

    private List<AirportEntity> airportList;

    private List<InternalAnnouncementEntity> allAnnouncements;
    
    private StaffEntity staff;

    @PostConstruct
    public void init() {
        FacesContext.getCurrentInstance().getExternalContext().getSessionMap().put("airportList", this.airportList);
        loggedInStaffNo = (String) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("staffNo");
        
        allAnnouncements = (List<InternalAnnouncementEntity>) internalAnnouncementSessionBean.getAllAnnouncements(loggedInStaffNo);
        staff = internalMessageSessionBean.getStaffEntityByStaffNo(loggedInStaffNo);
        
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

    public String getLoggedInStaffNo() {
        return loggedInStaffNo;
    }

    public void setLoggedInStaffNo(String loggedInStaffNo) {
        this.loggedInStaffNo = loggedInStaffNo;
    }

    public StaffEntity getStaff() {
        return staff;
    }

    public void setStaff(StaffEntity staff) {
        this.staff = staff;
    }
    
    public void toggleRead(InternalAnnouncementEntity announcementEntity) {
        internalAnnouncementSessionBean.toggleRead(announcementEntity);
    }

//    public String getFormatTime(Date d){
//        SimpleDateFormat dateF = new SimpleDateFormat("yyyy/MM/dd 'at' HH:mm:ss z\"");
//        return dateF.format(d);
//    }
    
    public void refreshAnnouncements(ActionEvent event) {
        allAnnouncements = (List<InternalAnnouncementEntity>) internalAnnouncementSessionBean.getAllAnnouncements(loggedInStaffNo);
        RequestContext.getCurrentInstance().execute("PF('announcement').show();");
    }
    
    public void showAnnouncement(String title, String content) {
        FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_INFO, title, content);         
        RequestContext.getCurrentInstance().showMessageInDialog(message);
    }

}
