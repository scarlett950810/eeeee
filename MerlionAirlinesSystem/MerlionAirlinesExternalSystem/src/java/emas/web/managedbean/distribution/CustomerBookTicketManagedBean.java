/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package emas.web.managedbean.distribution;

import imas.distribution.entity.PassengerEntity;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import javax.sql.DataSource;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperRunManager;
import org.primefaces.event.FlowEvent;
import org.primefaces.event.SelectEvent;

/**
 *
 * @author Howard
 */
@ManagedBean
@SessionScoped
public class CustomerBookTicketManagedBean {

    private int passengerNumber = 2;
    private List<PassengerEntity> passengers = new ArrayList<>();
    private String title;
    private String firstName;
    private String lastName;
    private String address;
    private String city;
    private String country;
    private String postCode;
    private String email;
    private String contactNumber;
    

    public CustomerBookTicketManagedBean() {
    }

    @PostConstruct
    public void init() {
        for (int i = 0; i < passengerNumber; i++) {
            passengers.add(new PassengerEntity());
        }

    }
    
    public void onDateChange(SelectEvent event){
        System.out.println(event.getObject());
    }

    public void confirm() throws IOException {
        FacesContext.getCurrentInstance().getExternalContext().redirect("../ReportController?User=Howard");
        System.out.print("finished");
    }
    
    public void makeBooking() throws IOException{
        FacesContext.getCurrentInstance().getExternalContext().redirect("BookingConfirmation.xhtml");
    }

    public String onFlowProcess(FlowEvent event) {

        return event.getNewStep();
    }

    public int getPassengerNumber() {
        return passengerNumber;
    }

    public void setPassengerNumber(int passengerNumber) {
        this.passengerNumber = passengerNumber;
    }

    public List<PassengerEntity> getPassengers() {
        return passengers;
    }

    public void setPassengers(List<PassengerEntity> passengers) {
        this.passengers = passengers;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getPostCode() {
        return postCode;
    }

    public void setPostCode(String postCode) {
        this.postCode = postCode;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getContactNumber() {
        return contactNumber;
    }

    public void setContactNumber(String contactNumber) {
        this.contactNumber = contactNumber;
    }
    
    
}
