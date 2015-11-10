/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package imas.web.managedbean.planning;

import GDS.entity.GDSAirportEntity;
import GDS.sessionbean.GDSAirportSessionBeanLocal;
import imas.planning.entity.AirportEntity;
import imas.planning.sessionbean.AirportSessionBeanLocal;
import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.persistence.PostRemove;
import org.primefaces.event.RowEditEvent;

/**
 *
 * @author Howard
 */
@ManagedBean
@ViewScoped

public class AirportManagedBean implements Serializable{
    @EJB
    private AirportSessionBeanLocal airportSessionBean;
    @EJB
    private GDSAirportSessionBeanLocal GDSAirportSessionBean;
    private String cityName;
    private String airportName;
    private String airportCode;
    private String nationName;
    private Boolean hubOrSpoke;
    private List<AirportEntity> airportList;
    private List<GDSAirportEntity> GDSAirports;
    private List<GDSAirportEntity> GDSNewAirports;
    private GDSAirportEntity GDSAirport;
    private AirportEntity airport;
    private List<AirportEntity> filteredAirports;
    private String tz;
    
    @PostConstruct
    public void init()
    {
        System.err.println("Enter airport management bean init");
        fetchAirports();
        GDSAirports = GDSAirportSessionBean.getAllGDSAirport();
        GDSNewAirports = new ArrayList<>();
        for(GDSAirportEntity g: GDSAirports){
            Boolean check = true;
            for(AirportEntity a: airportList){
                if(g.getIATAorFAA().equals(a.getAirportCode())){
                    System.err.println("Duplicate appears"+g.getIATAorFAA()+a.getAirportCode());
                    check = false;
                    break;
                }
            }
            if(check)
                GDSNewAirports.add(g);
        }
        FacesContext.getCurrentInstance().getExternalContext().getSessionMap().put("GDSAirports", GDSNewAirports);
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

    public List<AirportEntity> getFilteredAirports() {
        return filteredAirports;
    }

    public void setFilteredAirports(List<AirportEntity> filteredAirports) {
        this.filteredAirports = filteredAirports;
    }
    
    public void save() throws IOException {
        System.err.println("Info: "+hubOrSpoke+cityName+airportName+airportCode);
        if(hubOrSpoke == null || cityName == null || airportName == null || airportCode == null){
            FacesMessage msg = new FacesMessage("Sorry", "Please finished the required information");
            FacesContext.getCurrentInstance().addMessage(null, msg);
        }else{        
            if(airportSessionBean.checkAirport(airportCode)==true){
                AirportEntity airport = new AirportEntity(hubOrSpoke, cityName, airportName, airportCode, nationName, tz);
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
//        System.out.print("Called");
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
        
        if(a.getHubOrSpoke()){
        
        return "hub";}
        else
            return "spoke";
    }
    
    public void deleteAirport() throws IOException{
        FacesMessage msg;
//        System.err.println("enter deleteAirport()");
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

    public List<GDSAirportEntity> getGDSAirports() {
        return GDSAirports;
    }

    public void setGDSAirports(List<GDSAirportEntity> GDSAirports) {
        this.GDSAirports = GDSAirports;
    }

    public GDSAirportEntity getGDSAirport() {
        return GDSAirport;
    }

    public void setGDSAirport(GDSAirportEntity GDSAirport) {
        this.GDSAirport = GDSAirport;
    }

    public List<GDSAirportEntity> getGDSNewAirports() {
        return GDSNewAirports;
    }

    public void setGDSNewAirports(List<GDSAirportEntity> GDSNewAirports) {
        this.GDSNewAirports = GDSNewAirports;
    }

    public String getTz() {
        return tz;
    }

    public void setTz(String tz) {
        this.tz = tz;
    }
    
    public void updateAirport() throws IOException{
//        System.out.println(hubOrSpoke + "," + cityName + "," + airportName + ","+ airportCode);
        airportSessionBean.updateAirport(hubOrSpoke, cityName, airportName, airportCode, nationName, null);
        FacesContext fc = FacesContext.getCurrentInstance();
        ExternalContext ec = fc.getExternalContext();
        ec.redirect("planningHomePage.xhtml");
    }
    
    public void onRowEdit(RowEditEvent event) throws IOException {
        airport =  (AirportEntity) event.getObject();
        airportSessionBean.updateAirport(null, airport.getCityName(), airport.getAirportName(), airport.getAirportCode(), airport.getNationName(), airport.getId());
        FacesMessage msg = new FacesMessage("Airport Edited", ((AirportEntity) event.getObject()).getAirportName());
        FacesContext.getCurrentInstance().addMessage(null, msg);
        FacesContext fc = FacesContext.getCurrentInstance();
        ExternalContext ec = fc.getExternalContext();
        ec.redirect("planningAirport.xhtml");
    }
     
    public void onRowCancel(RowEditEvent event) {
        FacesMessage msg = new FacesMessage("Edit Cancelled", ((AirportEntity) event.getObject()).getAirportCode());
        FacesContext.getCurrentInstance().addMessage(null, msg);
    }
    
    public void onGDSAirportSelect(){
        System.err.println("Enter On GDS Airport Select");
        airportName =  GDSAirport.getName();
        cityName = GDSAirport.getCity();
        System.err.println("City name"+cityName);
        nationName = GDSAirport.getCountry();
        airportCode = GDSAirport.getIATAorFAA();
        tz = GDSAirport.getTz();
        
    }
    
}
