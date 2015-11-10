/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package imas.inventory.sessionbean;

import imas.inventory.entity.PromotionEntity;
import java.util.Date;
import java.util.List;
import javax.ejb.Local;

/**
 *
 * @author Scarlett
 */
@Local
public interface InventoryPromotionManagementSessionBeanLocal {

    public boolean checkPromoCodeNotExist(String promoCode);
    public void createDiscountPromotion(String promoCode, Date startDate, Date enddate, double discountRate);
    public void createWaivePromotion(String promoCode, Date startDate, Date enddate, double waiveAmount);
    public List<PromotionEntity> getAllPromotion();
    public List<PromotionEntity> getAllOngoingPromotion();
    public void editPromotion(PromotionEntity promotion);
    public void deletePromotion(PromotionEntity promotion);
    public boolean memberHasUsedPromotion(String memberID, String promoCode);
    public void memberUsePromotion(String memberID, String promoCode);
    public boolean promotionNotInTimeRange(String promoCode);
    public PromotionEntity getPromotionEntity(String promoCode);
    
}
