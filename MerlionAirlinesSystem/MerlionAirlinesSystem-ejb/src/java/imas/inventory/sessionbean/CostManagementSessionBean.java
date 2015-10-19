/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package imas.inventory.sessionbean;

import imas.inventory.entity.CostPairEntity;
import imas.planning.entity.RouteEntity;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import org.primefaces.model.DefaultTreeNode;
import org.primefaces.model.TreeNode;

/**
 *
 * @author Lei
 */
@Stateless

public class CostManagementSessionBean implements CostManagementSessionBeanLocal {

    @PersistenceContext
    private EntityManager em;

    private Double showRate;

    @Override
    public void initCostTable(RouteEntity selectedRoute) {
        List<CostPairEntity> list = new ArrayList<CostPairEntity>();
        list.add(new CostPairEntity("Cost per seat per mile", 0.134, 0));//0*
        list.add(new CostPairEntity("Fixed cost per seat per mile", 0.0554, 1));//1*
        list.add(new CostPairEntity("Flight distance per seat per day", 3657.390, 2));//2
        list.add(new CostPairEntity("Fixed cost per seat", 6078.6, 3));//3*
        list.add(new CostPairEntity("Operating cost per seat", 3071.2, 4));//4
        list.add(new CostPairEntity("Other cost per seat", 3007.4, 5));//5
        list.add(new CostPairEntity("Flight variable cost per seat per mile", 0.0602, 6));//6*
        list.add(new CostPairEntity("Oil cost per seat per mile", 0.0372, 7));//7
        list.add(new CostPairEntity("Crew cost per seat per mile", 0.00956, 8));//8
        list.add(new CostPairEntity("Maintainence cost per seat per mile", 0.00425, 9));//9
        list.add(new CostPairEntity("Tolls per seat per mile for take-off/landing", 0.00885, 10));//10
        list.add(new CostPairEntity("Other variable cost per seat per mile", 0.000354, 11));//11
        list.add(new CostPairEntity("Passenger cost per seat per mile", 0.0185, 12));//12*
        list.add(new CostPairEntity("Show rate", 1.0, 13));//13
        list.add(new CostPairEntity("Average flight distance per passenger", 871.784, 14));//14
        list.add(new CostPairEntity("Average cost per passenger", 21.103, 15));//15
        list.add(new CostPairEntity("Sales cost per passenger", 5.188, 16));//16
        list.add(new CostPairEntity("Airport fee per passenger", 1.740, 17));//17
        list.add(new CostPairEntity("Check-in cost per passenger", 9.544, 18));//18
        list.add(new CostPairEntity("Meal cost per passenger", 4.121, 19));//19
        list.add(new CostPairEntity("Service cost per passenger", 0.163, 20));//20
        list.add(new CostPairEntity("First class service cost per passenger", 0.308, 21));//21
        list.add(new CostPairEntity("Delay cost per passenger", 0.0389, 22));//22
        list = correctList(list);
        for (int i = 0; i < list.size(); i++) {
            list.get(i).setRoute(selectedRoute);
        }
        selectedRoute.setCostPairs(list);
        em.merge(selectedRoute);
    }

    @Override
    public TreeNode createRoot(List<CostPairEntity> list) {
        TreeNode root = new DefaultTreeNode(new CostPairEntity("Costs", 0.0, -1), null);
        TreeNode L1A = new DefaultTreeNode(list.get(1), root);
        TreeNode L1B = new DefaultTreeNode(list.get(6), root);
        TreeNode L1C = new DefaultTreeNode(list.get(12), root);
        TreeNode L1D = new DefaultTreeNode(list.get(0), root);
        //L1A
        TreeNode L1AL2A = new DefaultTreeNode(list.get(2), L1A);
        TreeNode L1AL2B = new DefaultTreeNode(list.get(3), L1A);
        TreeNode L1AL2BL3A = new DefaultTreeNode(list.get(4), L1AL2B);
        TreeNode L1AL2BL3B = new DefaultTreeNode(list.get(5), L1AL2B);

        //L1B
        TreeNode L1BL2A = new DefaultTreeNode(list.get(7), L1B);
        TreeNode L1BL2B = new DefaultTreeNode(list.get(8), L1B);
        TreeNode L1BL2C = new DefaultTreeNode(list.get(9), L1B);
        TreeNode L1BL2D = new DefaultTreeNode(list.get(10), L1B);
        TreeNode L1BL2E = new DefaultTreeNode(list.get(11), L1B);

        //L1C 
        TreeNode L1CL2A = new DefaultTreeNode(list.get(13), L1C);
        TreeNode L1CL2B = new DefaultTreeNode(list.get(14), L1C);
        TreeNode L1CL2C = new DefaultTreeNode(list.get(15), L1C);
        TreeNode L1CL2CL3A = new DefaultTreeNode(list.get(16), L1CL2C);
        TreeNode L1CL2CL3B = new DefaultTreeNode(list.get(17), L1CL2C);
        TreeNode L1CL2CL3C = new DefaultTreeNode(list.get(18), L1CL2C);
        TreeNode L1CL2CL3D = new DefaultTreeNode(list.get(19), L1CL2C);
        TreeNode L1CL2CL3E = new DefaultTreeNode(list.get(20), L1CL2C);
        TreeNode L1CL2CL3F = new DefaultTreeNode(list.get(21), L1CL2C);
        TreeNode L1CL2CL3G = new DefaultTreeNode(list.get(22), L1CL2C);
        //L1CL2CL3G.isExpanded()
        L1A.setExpanded(true);
        L1B.setExpanded(true);
        L1C.setExpanded(true);
        return root;
    }

//    @Override
//    public void writeList(List<CostPairEntity> list) {
//        for (int i = 0; i < list.size(); i++) {
//            em.merge(list.get(i));
//        }
//    }
//    @Override
//    public List<CostPairEntity> getList() {
//        Query query;
//        query = em.createQuery("SELECT c FROM CostPairEntity c");
//        List<CostPairEntity> list = query.getResultList();
//        if (list == null || list.isEmpty() || list.size() < 23) {
//            return createTable();
//        } else {
//            return list;
//        }
//    }
    @Override
    public void updateCost(RouteEntity selectedRoute, String costName, Double costFigure) {

        List<CostPairEntity> costList = selectedRoute.getCostPairs();
        if (!costList.isEmpty()) {
            for (int i = 0; i < costList.size(); i++) {
                if (costList.get(i).getCostType().equals(costName)) {
                    System.out.print(i);
                    costList.get(i).setCostFigure(costFigure);
                    break;
                }
            }

            costList = correctList(costList);
            selectedRoute.setCostPairs(costList);
            for (int i = 0; i < costList.size(); i++) {
                em.merge(costList.get(i));
            }
            em.merge(selectedRoute);
        }
    }

    @Override
    public List<CostPairEntity> getCostPairList(RouteEntity selectedRoute) {
        List<CostPairEntity> costTable;
        costTable = selectedRoute.getCostPairs();
        Collections.sort(costTable);
        return costTable;
    }

    @Override
    public void updateShowRate(RouteEntity selectedRoute, Double showRate) {
        if (selectedRoute.getCostPairs().isEmpty() || selectedRoute.getCostPairs() == null || selectedRoute.getCostPairs().size() < 23) {
            initCostTable(selectedRoute);
        }
        List<CostPairEntity> costList = this.getCostPairList(selectedRoute);
        costList.get(13).setCostFigure(showRate);
        correctList(costList);
        for (int i = 0; i < costList.size(); i++) {
            em.merge(costList.get(i));
        }
        em.merge(selectedRoute);

    }
//        List<CostPairEntity> costList = (List<CostPairEntity>) query.getResultList();
//
//        if (!costList.isEmpty()) {
//            //get show rate
//
//            costList.get(13).setCostFigure(showRate);
//            costList = correctList(costList);
//            writeList(costList);
//
//        }
//    }

//    @Override
//    public void updateShowRate(Selected Route, Double showRate) {
////        Query query = em.createQuery("SELECT c FROM CostPairEntit ");
////        List<CostPairEntity> costList = (List<CostPairEntity>) query.getResultList();
////
////        if (!costList.isEmpty()) {
////            //get show rate
////
////            costList.get(13).setCostFigure(showRate);
////            costList = correctList(costList);
////            writeList(costList);
////
////        }
////    }
    public static double round(double value, int places) {
        if (places < 0) {
            throw new IllegalArgumentException();
        }

        BigDecimal bd = new BigDecimal(value);
        bd = bd.setScale(places, RoundingMode.HALF_UP);
        return bd.doubleValue();
    }

    @Override
    public List<CostPairEntity> correctList(List<CostPairEntity> list) {

        list.get(3).setCostFigure(round((list.get(4).getCostFigure() + list.get(5).getCostFigure()), 4));//a=b+3 3=4+5
        list.get(1).setCostFigure(round(list.get(3).getCostFigure() / (30 * list.get(2).getCostFigure()), 4));//e=a/30d 1=3/30(2)
        list.get(6).setCostFigure(round(list.get(7).getCostFigure() + list.get(8).getCostFigure()
                + list.get(9).getCostFigure() + list.get(10).getCostFigure()
                + list.get(11).getCostFigure(), 4));
        list.get(15).setCostFigure(round(list.get(16).getCostFigure() + list.get(17).getCostFigure()
                + list.get(18).getCostFigure() + list.get(19).getCostFigure()
                + list.get(20).getCostFigure() + list.get(21).getCostFigure()
                + list.get(22).getCostFigure(), 4));
        list.get(12).setCostFigure(round(list.get(15).getCostFigure() / list.get(14).getCostFigure() * list.get(13).getCostFigure(), 4));
        list.get(0).setCostFigure(round(list.get(1).getCostFigure() + list.get(6).getCostFigure()
                + list.get(12).getCostFigure(), 4));
        return list;
    }

    public Double getShowRate() {
        return showRate;
    }

    public void setShowRate(Double showRate) {
        this.showRate = showRate;
    }

    @Override
    public Double getCostPerSeatPerMile(RouteEntity selectedRoute) {
        if (selectedRoute.getCostPairs().isEmpty() || selectedRoute.getCostPairs() == null || selectedRoute.getCostPairs().size() < 23) {
            initCostTable(selectedRoute);
        }
        List<CostPairEntity> costTable = this.getCostPairList(selectedRoute);
        return costTable.get(0).getCostFigure();
    }

}
