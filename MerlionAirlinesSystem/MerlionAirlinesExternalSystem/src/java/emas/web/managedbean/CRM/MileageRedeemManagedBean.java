/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package emas.web.managedbean.CRM;

import imas.crm.entity.MemberEntity;
import imas.crm.sessionbean.MemberProfileManagementSessionBeanLocal;
import imas.distribution.entity.TicketEntity;
import java.io.IOException;
import javax.inject.Named;
import java.io.Serializable;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.bean.SessionScoped;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;

/**
 *
 * @author Howard
 */
@Named(value = "mileageRedeemManagedBean")
@SessionScoped
public class MileageRedeemManagedBean implements Serializable {
    @EJB
    private MemberProfileManagementSessionBeanLocal memberProfileManagementSessionBean;

    private TicketEntity selectedTicket;
    private String memberID;
    private MemberEntity member;
    private String currentSeatClass;
    private double usedMileage;
    private String option;
    private double upgradeToPremiumEconomyClass = 0;
    private double upgradeToBusinessClass = 0;
    private double upgradeToFirstClass = 0;
    private boolean status;
    
    /**
     * Creates a new instance of MileageRedeemManagedBean
     */
    public MileageRedeemManagedBean() {
    }
    
    @PostConstruct
    public void init(){
        selectedTicket = (TicketEntity) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("selectedTicket");
        memberID = (String) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("memberID");
        member = memberProfileManagementSessionBean.getMember(memberID);
        currentSeatClass = selectedTicket.getSeat().getSeatClass();
        status = false;
        
        if(currentSeatClass.equals("Business Class")){
            upgradeToFirstClass = 5 * selectedTicket.getFlight().getRoute().getDistance();
        }else if(currentSeatClass.equals("Premium Economy Class")){
            upgradeToBusinessClass = 3 * selectedTicket.getFlight().getRoute().getDistance();
        }else if(currentSeatClass.equals("Economy Class")){
            upgradeToPremiumEconomyClass = 1.5 * selectedTicket.getFlight().getRoute().getDistance();
        }
    }
    
    public void redeem() throws IOException{
        if(option.equals("First Class")){
            status = memberProfileManagementSessionBean.upgradeToFirstClass(selectedTicket, member, upgradeToFirstClass);
        }else if(option.equals("Business Class")){
            status = memberProfileManagementSessionBean.upgradeToBusinessClass(selectedTicket, member, upgradeToBusinessClass);
        }else if(option.equals("Premium Economy Class")){
            status = memberProfileManagementSessionBean.upgradeToPremiumEconomyClass(selectedTicket, member, upgradeToPremiumEconomyClass);
        }
        FacesContext fc = FacesContext.getCurrentInstance();
        ExternalContext ec = fc.getExternalContext();
        ec.redirect("crmRedeemStatus.xhtml");
    }
    
    public void clear(){
        FacesContext.getCurrentInstance().getExternalContext().getSessionMap().remove("selectedTicket");
    }

    public TicketEntity getSelectedTicket() {
        return selectedTicket;
    }

    public void setSelectedTicket(TicketEntity selectedTicket) {
        this.selectedTicket = selectedTicket;
    }

    public String getMemberID() {
        return memberID;
    }

    public void setMemberID(String memberID) {
        this.memberID = memberID;
    }

    public MemberEntity getMember() {
        return member;
    }

    public void setMember(MemberEntity member) {
        this.member = member;
    }

    public double getUsedMileage() {
        return usedMileage;
    }

    public void setUsedMileage(double usedMileage) {
        this.usedMileage = usedMileage;
    }

    public double getUpgradeToPremiumEconomyClass() {
        return upgradeToPremiumEconomyClass;
    }

    public void setUpgradeToPremiumEconomyClass(double upgradeToPremiumEconomyClass) {
        this.upgradeToPremiumEconomyClass = upgradeToPremiumEconomyClass;
    }

    public double getUpgradeToBusinessClass() {
        return upgradeToBusinessClass;
    }

    public void setUpgradeToBusinessClass(double upgradeToBusinessClass) {
        this.upgradeToBusinessClass = upgradeToBusinessClass;
    }

    public double getUpgradeToFirstClass() {
        return upgradeToFirstClass;
    }

    public void setUpgradeToFirstClass(double upgradeToFirstClass) {
        this.upgradeToFirstClass = upgradeToFirstClass;
    }

    public String getCurrentSeatClass() {
        return currentSeatClass;
    }

    public void setCurrentSeatClass(String currentSeatClass) {
        this.currentSeatClass = currentSeatClass;
    }

    public String getOption() {
        return option;
    }

    public void setOption(String option) {
        this.option = option;
    }

    public boolean isStatus() {
        return status;
    }

    public void setStatus(boolean status) {
        this.status = status;
    }
}
