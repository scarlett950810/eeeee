/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package emas.web.managedbean.CRM;

import imas.crm.entity.MemberEntity;
import java.io.Serializable;
import java.util.Date;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.inject.Named;
import javax.faces.view.ViewScoped;
import javax.persistence.Temporal;

/**
 *
 * @author wutong
 */
@Named(value = "addMemberAccountManagedBean")
@ViewScoped
public class AddMemberAccountManagedBean implements Serializable {

    private String title;
    private String lastName;
    private String firstName;
    private String gender;
    @Temporal(javax.persistence.TemporalType.DATE)
    private Date birthdate;
    private String nationality;
    private String email;
    private String confirmEmail;
    private String contactNumber;
    private String pin;
    private String confirmPin;
    private String securityQuestion;
    private String answer;
    private MemberEntity member;

    /**
     * Creates a new instance of AddMemberAccountManagedBean
     */
    public AddMemberAccountManagedBean() {
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

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
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

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getConfirmEmail() {
        return confirmEmail;
    }

    public void setConfirmEmail(String confirmEmail) {
        this.confirmEmail = confirmEmail;
    }

    public String getContactNumber() {
        return contactNumber;
    }

    public void setContactNumber(String contactNumber) {
        this.contactNumber = contactNumber;
    }

    public String getPin() {
        return pin;
    }

    public void setPin(String pin) {
        this.pin = pin;
    }

    public String getConfirmPin() {
        return confirmPin;
    }

    public void setConfirmPin(String confirmPin) {
        this.confirmPin = confirmPin;
    }

    public String getSecurityQuestion() {
        return securityQuestion;
    }

    public void setSecurityQuestion(String securityQuestion) {
        this.securityQuestion = securityQuestion;
    }

    public String getAnswer() {
        return answer;
    }

    public void setAnswer(String answer) {
        this.answer = answer;
    }

    public void create() {
        FacesMessage msg;
        if (!email.equals(confirmEmail)) {
            msg = new FacesMessage(FacesMessage.SEVERITY_ERROR, "", "Please input the same e-mail address twice");
            FacesContext.getCurrentInstance().addMessage(null, msg);
        }
        else if (!pin.equals(confirmPin)) {
            msg = new FacesMessage(FacesMessage.SEVERITY_ERROR, "", "Please input the same pin number twice");
            FacesContext.getCurrentInstance().addMessage(null, msg);
        }
        else{
            member = new MemberEntity();
            member.setTitle(title);
            member.setLastName(lastName);
            member.setFirstName(firstName);
            member.setEmail(email);
            member.setGender(gender);
            member.setNationality(nationality);
            member.setSecurityQuestion(securityQuestion);
            member.setAnswer(answer);
            member.setBirthDate(birthdate);
            member.setPinNumber(pin);
            member.setContactNumber(contactNumber);
        }
        

    }

}
