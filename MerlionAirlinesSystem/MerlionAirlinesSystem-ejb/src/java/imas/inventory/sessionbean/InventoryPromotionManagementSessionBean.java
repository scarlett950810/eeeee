/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package imas.inventory.sessionbean;

import imas.inventory.entity.PromotionEntity;
import java.util.Date;
import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

/**
 *
 * @author Scarlett
 */
@Stateless
public class InventoryPromotionManagementSessionBean implements InventoryPromotionManagementSessionBeanLocal {
    
    @PersistenceContext
    private EntityManager em;

    @Override
    public boolean checkPromoCodeNotExist(String promoCode) {
        Query queryFoDuplicateCode = em.createQuery("SELECT p FROM PromotionEntity p WHERE p.promoCode = :promoCode");
        queryFoDuplicateCode.setParameter("promoCode", promoCode);
        return queryFoDuplicateCode.getResultList().isEmpty();
    }
            
    @Override
    public void createDiscountPromotion(String promoCode, Date startDate, Date enddate, double discountRate) {
        PromotionEntity p = new PromotionEntity().createDiscountPromotion(promoCode, startDate, enddate, discountRate);
        em.persist(p);
    }

    @Override
    public void createWaivePromotion(String promoCode, Date startDate, Date enddate, double waiveAmount) {
        PromotionEntity p = new PromotionEntity().createWaivePromotion(promoCode, startDate, enddate, waiveAmount);
        em.persist(p);
    }

    @Override
    public List<PromotionEntity> getAllPromotion() {
        Query queryForAllPromotion = em.createQuery("SELECT p FROM PromotionEntity p");
        return queryForAllPromotion.getResultList();
    }

    @Override
    public List<PromotionEntity> getAllOngoingPromotion() {
        Query queryForAllOngoingPromotion = em.createQuery("SELECT p FROM PromotionEntity p WHERE p.startDate < :today and p.endDate > :today");
        queryForAllOngoingPromotion.setParameter("today", new Date());
        System.out.println("ongoing: " + queryForAllOngoingPromotion.getResultList());
        return queryForAllOngoingPromotion.getResultList();
    }
    
    @Override
    public void editPromotion(PromotionEntity promotion) {
        PromotionEntity promotionToUpdate = em.find(PromotionEntity.class, promotion.getId());
        promotionToUpdate.setStartDate(promotion.getStartDate());
        promotionToUpdate.setEndDate(promotion.getEndDate());
        if (promotion.isDiscount()) {
            promotionToUpdate.setDiscountRate(promotion.getDiscountRate());
        } else {
            promotionToUpdate.setWaiveAmount(promotion.getWaiveAmount());
        }
    }
    
    @Override
    public void deletePromotion(PromotionEntity promotion) {
        PromotionEntity promotionToDelete = em.find(PromotionEntity.class, promotion.getId());
        em.remove(promotionToDelete);
    }
}
