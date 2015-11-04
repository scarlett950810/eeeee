/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package emas.web.managedbean.CRM;

import imas.crm.entity.MemberEntity;
import imas.crm.sessionbean.MemberProfileManagementSessionBeanLocal;
import imas.distribution.entity.TicketEntity;
import imas.planning.entity.FlightEntity;
import java.io.IOException;
import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.application.FacesMessage;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.inject.Named;
import javax.faces.view.ViewScoped;

/**
 *
 * @author Howard
 */
@Named(value = "memberProfileManagedBean")
@ViewScoped
public class MemberProfileManagedBean implements Serializable {

    @EJB
    private MemberProfileManagementSessionBeanLocal memberProfileManagementSessionBean;

    private MemberEntity member;
    private String memberID;
    private String title;
    private String lastName;
    private String firstName;
    private Date birthdate;
    private String birthdateString;
    private String nationality;
    private String passportNumber;
    private Date passportExpiration;
    private String passportExpirationString;
    private String email;
    private String contact;
    private String address;
    private String oldPassword;
    private String newPassword;
    private String repeatNewPassword;
    private List<FlightEntity> flights;
    private List<FlightEntity> filteredFlights;
    private List<TicketEntity> tickets;
    private List<TicketEntity> filteredTickets;

    private boolean edit;

    SimpleDateFormat format = new SimpleDateFormat("yyyy/MM/dd");

    @PostConstruct
    public void init() {
//        memberID = (String) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("memberID");
//        if(memberID == null){
//            System.out.print("You have not logged in");
//        }else{
        member = memberProfileManagementSessionBean.getMember(memberID);
        title = member.getTitle();
        lastName = member.getLastName();
        firstName = member.getFirstName();
        birthdate = member.getBirthDate();
        if (birthdate != null) {
            birthdateString = format.format(birthdate);
        }

        nationality = member.getNationality();
        passportNumber = member.getPassportNumber();
        passportExpiration = member.getPassportExpiryDate();
        if (passportExpiration != null) {
            passportExpirationString = format.format(passportExpiration);
        }

        email = member.getEmail();
        contact = member.getContactNumber();
        address = member.getAddress();
        edit = false;
        tickets = new ArrayList<>();
        tickets = memberProfileManagementSessionBean.getMemberTickets(memberID);
//        }

    }

    public MemberProfileManagedBean() {

    }

    public void editProfile() {
        edit = true;
    }

    public void save() {

        member.setNationality(nationality);
        member.setPassportNumber(passportNumber);
        member.setPassportExpiryDate(passportExpiration);
        member.setEmail(email);
        member.setContactNumber(contact);
        member.setAddress(address);
        memberProfileManagementSessionBean.updateProfile(member);
        edit = false;
        System.out.print(edit);
    }

    public void cancel() {
        nationality = member.getNationality();
        passportNumber = member.getPassportNumber();
        passportExpiration = member.getPassportExpiryDate();
        if (passportExpiration != null) {
            passportExpirationString = format.format(passportExpiration);
        }

        email = member.getEmail();
        contact = member.getContactNumber();
        address = member.getAddress();
        edit = false;
        System.out.print(edit);
    }

    public void changePassword() throws IOException {
        if (newPassword.equals(repeatNewPassword)) {
            if (newPassword.length() == 6) {
                if (memberProfileManagementSessionBean.resetPassword(oldPassword, newPassword, memberID)) {

                    FacesContext fc = FacesContext.getCurrentInstance();
                    ExternalContext ec = fc.getExternalContext();
                    ec.redirect("crmMemberProfile.xhtml");
                } else {
                    FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_ERROR, "Invalid Old PIN", "");
                    FacesContext.getCurrentInstance().addMessage(null, msg);
                }
            } else {
                FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_ERROR, "Please enter a 6-digit PIN", "");
                FacesContext.getCurrentInstance().addMessage(null, msg);
            }
        } else {
            FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_ERROR, "Please repeat the new password again", "");
            FacesContext.getCurrentInstance().addMessage(null, msg);
        }
    }

    public MemberEntity getMember() {
        return member;
    }

    public void setMember(MemberEntity member) {
        this.member = member;
    }

    public boolean isEdit() {
        return edit;
    }

    public void setEdit(boolean edit) {
        this.edit = edit;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public Date getBirthdate() {
        return birthdate;
    }

    public void setBirthdate(Date birthdate) {
        this.birthdate = birthdate;
    }

    public String getNationality() {
        return nationality;
    }

    public void setNationality(String nationality) {
        this.nationality = nationality;
    }

    public String getPassportNumber() {
        return passportNumber;
    }

    public void setPassportNumber(String passportNumber) {
        this.passportNumber = passportNumber;
    }

    public Date getPassportExpiration() {
        return passportExpiration;
    }

    public void setPassportExpiration(Date passportExpiration) {
        this.passportExpiration = passportExpiration;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getContact() {
        return contact;
    }

    public void setContact(String contact) {
        this.contact = contact;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getMemberID() {
        return memberID;
    }

    public void setMemberID(String memberID) {
        this.memberID = memberID;
    }

    public String getBirthdateString() {
        return birthdateString;
    }

    public void setBirthdateString(String birthdateString) {
        this.birthdateString = birthdateString;
    }

    public String getPassportExpirationString() {
        return passportExpirationString;
    }

    public void setPassportExpirationString(String passportExpirationString) {
        this.passportExpirationString = passportExpirationString;
    }

    public SimpleDateFormat getFormat() {
        return format;
    }

    public void setFormat(SimpleDateFormat format) {
        this.format = format;
    }

    public String getOldPassword() {
        return oldPassword;
    }

    public void setOldPassword(String oldPassword) {
        this.oldPassword = oldPassword;
    }

    public String getNewPassword() {
        return newPassword;
    }

    public void setNewPassword(String newPassword) {
        this.newPassword = newPassword;
    }

    public String getRepeatNewPassword() {
        return repeatNewPassword;
    }

    public void setRepeatNewPassword(String repeatNewPassword) {
        this.repeatNewPassword = repeatNewPassword;
    }

    public List<FlightEntity> getFlights() {
        return flights;
    }

    public void setFlights(List<FlightEntity> flights) {
        this.flights = flights;
    }

    public List<FlightEntity> getFilteredFlights() {
        return filteredFlights;
    }

    public void setFilteredFlights(List<FlightEntity> filteredFlights) {
        this.filteredFlights = filteredFlights;
    }

    public List<TicketEntity> getTickets() {
        if(tickets.isEmpty())
            return null;
        return tickets;
    }

    public void setTickets(List<TicketEntity> tickets) {
        this.tickets = tickets;
    }

    public List<TicketEntity> getFilteredTickets() {
        return filteredTickets;
    }

    public void setFilteredTickets(List<TicketEntity> filteredTickets) {
        this.filteredTickets = filteredTickets;
    }
    

}
