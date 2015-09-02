/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package imas.web.managedbean.planning;

import imas.planning.entity.AirportEntity;
import imas.planning.sessionbean.AirportSessionBeanLocal;
import java.io.IOException;
import java.io.Serializable;
import java.util.List;
import javax.ejb.EJB;
import javax.faces.application.FacesMessage;
import javax.annotation.ManagedBean;
import javax.annotation.PostConstruct;
import javax.enterprise.context.RequestScoped;
import javax.faces.bean.ManagedProperty;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.inject.Named;

/**
 *
 * @author Howard
 */
@Named(value = "airportManagedBean")
@ManagedBean
@RequestScoped

public class AirportManagedBean implements Serializable{
    @EJB
    private AirportSessionBeanLocal airportSessionBean;
    private String cityName;
    private String airportName;
    private String airportCode;
    private Boolean hubOrSpoke;
    private List<AirportEntity> airportList;
    private AirportEntity airport;
    
//    @ManagedProperty("#{airportService}")
//    private AirportService service;
    
//
//    @PostConstruct
//    public void init() {
//        this.airportList = airportSessionBean.fetchAirport();
//        System.out.print(airportList.isEmpty());
//    }
    
    public AirportEntity getAirport() {
        return airport;
    }
    
    public String getCityName(){
        return cityName;
    }
    
    public void setCityName(String cityName){
        this.cityName = cityName;
    }
    
    public String getAirportName(){
        return airportName;
    }
    
    public void setAirportName(String airportName){
        this.airportName = airportName;
    }
    
    public String getAirportCode(){
        return airportCode;
    }
    
    public void setAirportCode(String airportCode){
        this.airportCode = airportCode;
    }
    
    public Boolean getHubOrSpoke() {
        return hubOrSpoke;
    }

    public void setHubOrSpoke(Boolean hubOrSpoke) {
        this.hubOrSpoke = hubOrSpoke;
    }
    
    public void save() throws IOException {
        if(hubOrSpoke == null || cityName == null || airportName == null || airportCode == null){
            FacesMessage msg = new FacesMessage("Sorry", "Please finished the required information");
            FacesContext.getCurrentInstance().addMessage(null, msg);
        }else{        
            if(airportSessionBean.checkAirport(airportCode)==true){
                AirportEntity airport = new AirportEntity(hubOrSpoke, cityName, airportName, airportCode);
                airportSessionBean.addAirport(airport);
                FacesContext fc = FacesContext.getCurrentInstance();
                ExternalContext ec = fc.getExternalContext();
                ec.redirect("planningHomePage.xhtml");
            }else{
                FacesMessage msg = new FacesMessage("Sorry", "this airport has already been added!");
                FacesContext.getCurrentInstance().addMessage(null, msg);
            }
        }
    }
    
    public void deleteAirport() throws IOException{
        System.out.print(airport.getAirportCode());
        airportSessionBean.deleteAirport(airport.getAirportCode());
        FacesContext fc = FacesContext.getCurrentInstance();
        ExternalContext ec = fc.getExternalContext();
        ec.redirect("planningHomePage.xhtml");
    }
    
    public void fetchAirports(){
        this.airportList = airportSessionBean.fetchAirport();
    }
    
//    public void fetchAirport(){
//        airportList = service.getAirports();
//    }


//    public void setService(AirportService service) {
//        this.service = service;
//    }

    public List<AirportEntity> getAirportList() {
        return airportList;
    }
    
    public void updateAirport() throws IOException{
        System.out.println(hubOrSpoke + "," + cityName + "," + airportName + ","+ airportCode);
        airport = airportSessionBean.getAirport(airportCode);
        airportSessionBean.updateAirport(hubOrSpoke, cityName, airportName, airportCode);
        FacesContext fc = FacesContext.getCurrentInstance();
        ExternalContext ec = fc.getExternalContext();
        ec.redirect("planningHomePage.xhtml");
    }
    
//    public AirportConverter getAirportConverter(){
//        return 
//    }
//    
}
