/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package imas.inventory.sessionbean;

import imas.crm.entity.MemberEntity;
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

    private MemberEntity getMemberEntity(String memberID) {
        Query queryForMember = em.createQuery("SELECT m FROM MemberEntity m WHERE m.memberID = :memberID");
        queryForMember.setParameter("memberID", memberID);
        if (queryForMember.getResultList().isEmpty()) {
            return null;
        } else {
            return (MemberEntity) queryForMember.getResultList().get(0);
        }
    }

    private PromotionEntity getPromotionEntity(String promoCode) {
        Query queryForPromotion = em.createQuery("SELECT p FROM PromotionEntity p WHERE p.promoCode = :promoCode");
        queryForPromotion.setParameter("promoCode", promoCode);
        if (queryForPromotion.getResultList().isEmpty()) {
            return null;
        } else {
            return (PromotionEntity) queryForPromotion.getResultList().get(0);
        }
    }
    
    @Override
    public boolean memberHasUsedPromotion(String memberID, String promoCode) {
        MemberEntity member = getMemberEntity(memberID);
        PromotionEntity promotion = getPromotionEntity(promoCode);
        List<PromotionEntity> promotions = member.getPromotionEntities();
        return (promotions.contains(promotion));
    }

    @Override
    public boolean promotionWithinTime(PromotionEntity promotion) {
        Date now = new Date();
        return (now.after(promotion.getStartDate()) && promotion.getEndDate().after(now));
    }

    @Override
    public void memberUsePromotion(String memberID, String promoCode) {
        MemberEntity memberManaged = getMemberEntity(memberID);
        PromotionEntity promotionManaged = getPromotionEntity(promoCode);
        List<PromotionEntity> originalPromotions = memberManaged.getPromotionEntities();
        originalPromotions.add(promotionManaged);
        memberManaged.setPromotionEntities(originalPromotions);
        List<MemberEntity> originalMembers = promotionManaged.getMembers();
        originalMembers.add(memberManaged);
        promotionManaged.setMembers(originalMembers);
        em.flush();
    }

}
