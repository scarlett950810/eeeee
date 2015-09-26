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
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.application.FacesMessage;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;
import javax.faces.view.ViewScoped;
import javax.inject.Named;
import javax.persistence.PostRemove;
import org.primefaces.event.RowEditEvent;

/**
 *
 * @author Howard
 */
@Named
@ViewScoped

public class AirportManagedBean implements Serializable{
    @EJB
    private AirportSessionBeanLocal airportSessionBean;
    private String cityName;
    private String airportName;
    private String airportCode;
    private String nationName;
    private Boolean hubOrSpoke;
    private List<AirportEntity> airportList;
    private AirportEntity airport;
    
    @PostConstruct
    public void init()
    {
        fetchAirports();
        
        FacesContext.getCurrentInstance().getExternalContext().getSessionMap().put("airportList", airportList);
    }
    
    @PostRemove
    public void destroy()
    {
        FacesContext.getCurrentInstance().getExternalContext().getSessionMap().remove("airportList");
    }
    
    public String getNationName() {
        return nationName;
        
    }

    public void setNationName(String nationName) {    
        this.nationName = nationName;
    }

    public AirportEntity getAirport() {
        return airport;
    }

    public void setAirportList(List<AirportEntity> airportList) {
        this.airportList = airportList;
    }

    public void setAirport(AirportEntity airport) {
        this.airport = airport;
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
                AirportEntity airport = new AirportEntity(hubOrSpoke, cityName, airportName, airportCode, nationName);
                airportSessionBean.addAirport(airport);
                FacesContext fc = FacesContext.getCurrentInstance();
                ExternalContext ec = fc.getExternalContext();
                ec.redirect("planningAirport.xhtml");
            }else{
                FacesMessage msg = new FacesMessage("Sorry", "this airport has already been added!");
                FacesContext.getCurrentInstance().addMessage(null, msg);
            }
        }
    }
    
    public void addAirport() throws IOException{
        System.out.print("Called");
        FacesContext fc = FacesContext.getCurrentInstance();
        ExternalContext ec = fc.getExternalContext();
        ec.redirect("planningAddAirport.xhtml");
    }
    public void goDeleteAirport() throws IOException{
        FacesContext fc = FacesContext.getCurrentInstance();
        ExternalContext ec = fc.getExternalContext();
        ec.redirect("planningDeleteAirport.xhtml");
    }
    public String getHubOrSpoke(AirportEntity a){
        System.err.println("lallalala");
        if(a.getHubOrSpoke()){
        
        return "hub";}
        else
            return "spoke";
    }
    
    public void deleteAirport() throws IOException{
        FacesMessage msg;
        System.err.println("enter deleteAirport()");
        if(airportSessionBean.deleteAirport(airport.getAirportCode())){            
            FacesContext fc = FacesContext.getCurrentInstance();
            ExternalContext ec = fc.getExternalContext();
            ec.redirect("planningAirport.xhtml");            
        }
        else {
            msg = new FacesMessage(FacesMessage.SEVERITY_ERROR, "Failed","Please delete associated routes or aircrafts first"); 
            FacesContext.getCurrentInstance().addMessage(null, msg);
        }
        
        
        

    }
    
    public void fetchAirports(){
        this.airportList = airportSessionBean.fetchAirport();
    }

    public List<AirportEntity> getAirportList() {
        return airportList;
    }
    
    public void updateAirport() throws IOException{
        System.out.println(hubOrSpoke + "," + cityName + "," + airportName + ","+ airportCode);
        airportSessionBean.updateAirport(hubOrSpoke, cityName, airportName, airportCode, nationName);
        FacesContext fc = FacesContext.getCurrentInstance();
        ExternalContext ec = fc.getExternalContext();
        ec.redirect("planningHomePage.xhtml");
    }
    
    public void onRowEdit(RowEditEvent event) {
        airport =  (AirportEntity) event.getObject();
        airportSessionBean.updateAirport(null, airport.getCityName(), airport.getAirportName(), airport.getAirportCode(), airport.getNationName());
        FacesMessage msg = new FacesMessage("Airport Edited", ((AirportEntity) event.getObject()).getAirportName());
        FacesContext.getCurrentInstance().addMessage(null, msg);
    }
     
    public void onRowCancel(RowEditEvent event) {
        FacesMessage msg = new FacesMessage("Edit Cancelled", ((AirportEntity) event.getObject()).getAirportCode());
        FacesContext.getCurrentInstance().addMessage(null, msg);
    }
    
    
}
