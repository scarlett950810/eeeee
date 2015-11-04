/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package imas.web.managedbean.inventory;

import imas.inventory.entity.PromotionEntity;
import imas.inventory.sessionbean.InventoryPromotionManagementSessionBeanLocal;
import java.io.Serializable;
import java.util.Date;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.inject.Named;
import javax.faces.view.ViewScoped;
import javax.persistence.PostRemove;
import org.primefaces.event.RowEditEvent;
import org.primefaces.event.SelectEvent;

/**
 *
 * @author Scarlett
 */
@Named(value = "inventoryPromotionManagedBean")
@ViewScoped
public class inventoryPromotionManagedBean implements Serializable {

    @EJB
    private InventoryPromotionManagementSessionBeanLocal inventoryPromotionManagementSessionBean;

    private String promoCode;
    private Date start;
    private Date end;
    private double discountRate;
    private double waiveAmount;
    private List<PromotionEntity> promotions;
    private boolean ongoingPromotionsOnly;
    private Date today;
    private Date endMinDate;
    private String type;
    private boolean typeDiscount;

    public inventoryPromotionManagedBean() {
    }

    @PostConstruct
    public void init() {
        ongoingPromotionsOnly = false;
        promotions = inventoryPromotionManagementSessionBean.getAllPromotion();
        FacesContext.getCurrentInstance().getExternalContext().getSessionMap().put("PromotionList", promotions);
        today = new Date();
        endMinDate = today;
        type = "Discount";
        typeDiscount = true;
    }

    @PostRemove
    public void destroy() {
        FacesContext.getCurrentInstance().getExternalContext().getSessionMap().remove("PromotionList");
    }

    public void createDiscountPromotion() {
        if (inventoryPromotionManagementSessionBean.checkPromoCodeNotExist(promoCode)) {
            if (discountRate > 0 && discountRate < 1) {
                inventoryPromotionManagementSessionBean.createDiscountPromotion(promoCode, start, end, discountRate);
                FacesContext.getCurrentInstance().addMessage("Msg",
                        new FacesMessage(FacesMessage.SEVERITY_INFO, "Promotion Created.",
                                promoCode + ": from " + start + " to " + end + " " + discountRate * 100 + "% discount."));
                updatePromotionList();
            } else {
                FacesContext.getCurrentInstance().addMessage("Msg",
                        new FacesMessage(FacesMessage.SEVERITY_FATAL, "Failed:", "Discount rate should be within 0 to 1."));
            }
        } else {
            FacesContext.getCurrentInstance().addMessage("Msg",
                    new FacesMessage(FacesMessage.SEVERITY_FATAL, "Failed:", "Promo Code Exists."));
        }
    }

    public void createPriceWaivePromotion() {
        if (inventoryPromotionManagementSessionBean.checkPromoCodeNotExist(promoCode)) {
            if (waiveAmount > 0) {
                inventoryPromotionManagementSessionBean.createWaivePromotion(promoCode, start, end, waiveAmount);
                FacesContext.getCurrentInstance().addMessage("Msg",
                        new FacesMessage(FacesMessage.SEVERITY_INFO, "Promotion Created.",
                                promoCode + ": from " + start + " to " + end + " S$" + waiveAmount + " waive."));
                updatePromotionList();
            } else {
                FacesContext.getCurrentInstance().addMessage("Msg",
                        new FacesMessage(FacesMessage.SEVERITY_FATAL, "Failed:", "Please set the waive amount > 0."));
            }
        } else {
            FacesContext.getCurrentInstance().addMessage("Msg",
                    new FacesMessage(FacesMessage.SEVERITY_FATAL, "Failed:", "Promo Code Exists."));
        }
    }

    public void updatePromotionList() {
        toggleOngoingPromotionsOnly();
    }

    public void toggleOngoingPromotionsOnly() {
        System.out.println("ongoingPromotionsOnly = " + ongoingPromotionsOnly);
        if (ongoingPromotionsOnly) {
            promotions = inventoryPromotionManagementSessionBean.getAllOngoingPromotion();
        } else {
            promotions = inventoryPromotionManagementSessionBean.getAllPromotion();
        }
    }

    public void onSelectType() {
        typeDiscount = type.equals("Discount");
    }
    
    public void deletePromotion(PromotionEntity p) {
        inventoryPromotionManagementSessionBean.deletePromotion(p);
        FacesMessage msg = new FacesMessage("Promotion Deleted", null);
        FacesContext.getCurrentInstance().addMessage(null, msg);
        toggleOngoingPromotionsOnly();
    }
    

    public String getPromoCode() {
        return promoCode;
    }

    public void setPromoCode(String promoCode) {
        this.promoCode = promoCode;
    }

    public Date getStart() {
        return start;
    }

    public void setStart(Date start) {
        this.start = start;
    }

    public Date getEnd() {
        return end;
    }

    public void setEnd(Date end) {
        this.end = end;
    }

    public double getDiscountRate() {
        return discountRate;
    }

    public void setDiscountRate(double discountRate) {
        this.discountRate = discountRate;
    }

    public double getWaiveAmount() {
        return waiveAmount;
    }

    public void setWaiveAmount(double waiveAmount) {
        this.waiveAmount = waiveAmount;
    }

    public List<PromotionEntity> getPromotions() {
        return promotions;
    }

    public void setPromotions(List<PromotionEntity> promotions) {
        this.promotions = promotions;
    }

    public boolean isOngoingPromotionsOnly() {
        return ongoingPromotionsOnly;
    }

    public void setOngoingPromotionsOnly(boolean ongoingPromotionsOnly) {
        this.ongoingPromotionsOnly = ongoingPromotionsOnly;
    }

    public Date getToday() {
        return today;
    }

    public void setToday(Date today) {
        this.today = today;
    }

    public Date getEndMinDate() {
        return endMinDate;
    }

    public void setEndMinDate(Date endMinDate) {
        this.endMinDate = endMinDate;
    }

    public void onStartDateChange(SelectEvent event) {
        endMinDate = start;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public boolean isTypeDiscount() {
        return typeDiscount;
    }

    public void setTypeDiscount(boolean typeDiscount) {
        this.typeDiscount = typeDiscount;
    }

}
