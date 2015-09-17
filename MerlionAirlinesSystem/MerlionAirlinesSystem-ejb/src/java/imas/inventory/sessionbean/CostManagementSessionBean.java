/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package imas.inventory.sessionbean;

import imas.inventory.entity.CostPairEntity;
import static java.lang.Math.round;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.List;
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

    // Add business logic below. (Right-click in editor and choose
    // "Insert Code > Add Business Method")
    @PersistenceContext
    private EntityManager em;

    @Override
    public List<CostPairEntity> createTable() {
        List<CostPairEntity> list = new ArrayList<CostPairEntity>();

        list.add(new CostPairEntity("Cost per seat per mile", 0.134));//0*
        list.add(new CostPairEntity("Fixed cost per seat per mile", 0.0554));//1*
        list.add(new CostPairEntity("Flight distance per seat per day", 3657.390));//2
        list.add(new CostPairEntity("Fixed cost per seat", 6078.6));//3*
        list.add(new CostPairEntity("Operating cost per seat", 3071.2));//4
        list.add(new CostPairEntity("Other cost per seat", 3007.4));//5
        list.add(new CostPairEntity("Flight variable cost per seat per mile", 0.0602));//6*
        list.add(new CostPairEntity("Oil cost per seat per mile", 0.0372));//7
        list.add(new CostPairEntity("Crew cost per seat per mile", 0.00956));//8
        list.add(new CostPairEntity("Maintainence cost per seat per mile", 0.00425));//9
        list.add(new CostPairEntity("Tolls per seat per mile for take-off/landing", 0.00885));//10
        list.add(new CostPairEntity("Other variable cost per seat per mile", 0.000354));//11
        list.add(new CostPairEntity("Passenger cost per seat per mile", 0.0185));//12*
        list.add(new CostPairEntity("Show rate", 0.764));//13
        list.add(new CostPairEntity("Average flight distance per passenger", 871.784));//14
        list.add(new CostPairEntity("Average cost per passenger", 21.103));//15
        list.add(new CostPairEntity("Sales cost per passenger", 5.188));//16
        list.add(new CostPairEntity("Airport fee per passenger", 1.740));//17
        list.add(new CostPairEntity("Check-in cost per passenger", 9.544));//18
        list.add(new CostPairEntity("Meal cost per passenger", 4.121));//19
        list.add(new CostPairEntity("Service cost per passenger", 0.163));//20
        list.add(new CostPairEntity("First class service cost per passenger", 0.308));//21
        list.add(new CostPairEntity("Delay cost per passenger", 0.0389));//22
        list = correctList(list);
        writeList(list);
        
        return list;
    }

    @Override
    public TreeNode createRoot() {
        List<CostPairEntity> list = getList();

        TreeNode root = new DefaultTreeNode(new CostPairEntity("Costs", 0.0), null);

        TreeNode L1A = new DefaultTreeNode(new CostPairEntity(list.get(1).getCostType(), list.get(1).getCostFigure()), root);
        TreeNode L1B = new DefaultTreeNode(new CostPairEntity(list.get(6).getCostType(), list.get(6).getCostFigure()), root);
        TreeNode L1C = new DefaultTreeNode(new CostPairEntity(list.get(12).getCostType(), list.get(12).getCostFigure()), root);
        TreeNode L1D = new DefaultTreeNode(new CostPairEntity(list.get(0).getCostType(), list.get(0).getCostFigure()), root);
        //L1A
        TreeNode L1AL2A = new DefaultTreeNode(new CostPairEntity(list.get(2).getCostType(), list.get(2).getCostFigure()), L1A);
        TreeNode L1AL2B = new DefaultTreeNode(new CostPairEntity(list.get(3).getCostType(), list.get(3).getCostFigure()), L1A);
        TreeNode L1AL2BL3A = new DefaultTreeNode(new CostPairEntity(list.get(4).getCostType(), list.get(4).getCostFigure()), L1AL2B);
        TreeNode L1AL2BL3B = new DefaultTreeNode(new CostPairEntity(list.get(5).getCostType(), list.get(5).getCostFigure()), L1AL2B);

        //L1B
        TreeNode L1BL2A = new DefaultTreeNode(new CostPairEntity(list.get(7).getCostType(), list.get(7).getCostFigure()), L1B);
        TreeNode L1BL2B = new DefaultTreeNode(new CostPairEntity(list.get(8).getCostType(), list.get(8).getCostFigure()), L1B);
        TreeNode L1BL2C = new DefaultTreeNode(new CostPairEntity(list.get(9).getCostType(), list.get(9).getCostFigure()), L1B);
        TreeNode L1BL2D = new DefaultTreeNode(new CostPairEntity(list.get(10).getCostType(), list.get(10).getCostFigure()), L1B);
        TreeNode L1BL2E = new DefaultTreeNode(new CostPairEntity(list.get(11).getCostType(), list.get(11).getCostFigure()), L1B);

        //L1C 
        TreeNode L1CL2A = new DefaultTreeNode(new CostPairEntity(list.get(13).getCostType(), list.get(13).getCostFigure()), L1C);
        TreeNode L1CL2B = new DefaultTreeNode(new CostPairEntity(list.get(14).getCostType(), list.get(14).getCostFigure()), L1C);
        TreeNode L1CL2C = new DefaultTreeNode(new CostPairEntity(list.get(15).getCostType(), list.get(15).getCostFigure()), L1C);
        TreeNode L1CL2CL3A = new DefaultTreeNode(new CostPairEntity(list.get(16).getCostType(), list.get(16).getCostFigure()), L1CL2C);
        TreeNode L1CL2CL3B = new DefaultTreeNode(new CostPairEntity(list.get(17).getCostType(), list.get(17).getCostFigure()), L1CL2C);
        TreeNode L1CL2CL3C = new DefaultTreeNode(new CostPairEntity(list.get(18).getCostType(), list.get(18).getCostFigure()), L1CL2C);
        TreeNode L1CL2CL3D = new DefaultTreeNode(new CostPairEntity(list.get(19).getCostType(), list.get(19).getCostFigure()), L1CL2C);
        TreeNode L1CL2CL3E = new DefaultTreeNode(new CostPairEntity(list.get(20).getCostType(), list.get(20).getCostFigure()), L1CL2C);
        TreeNode L1CL2CL3F = new DefaultTreeNode(new CostPairEntity(list.get(21).getCostType(), list.get(21).getCostFigure()), L1CL2C);
        TreeNode L1CL2CL3G = new DefaultTreeNode(new CostPairEntity(list.get(22).getCostType(), list.get(22).getCostFigure()), L1CL2C);

        return root;
    }

    @Override
    public void writeList(List<CostPairEntity> list) {
        for (int i = 0; i < list.size(); i++) {
            em.persist(list.get(i));
        }
    }

    @Override
    public List<CostPairEntity> getList() {
        Query query;
        query = em.createQuery("SELECT c FROM CostPairEntity c");
        List<CostPairEntity> list = query.getResultList();
        if (list == null || list.isEmpty() || list.size() < 23) {
            return createTable();
        } else {
            return list;
        }
    }

    @Override
    public void updateCost(String costName, Double costFigure) {
   
        Query query = em.createQuery("SELECT c FROM CostPairEntity c WHERE c.costType =:costname ");
        query.setParameter("costname", costName);
        List<CostPairEntity> costList= (List<CostPairEntity>) query.getResultList();

        if (!costList.isEmpty()) {
            //get same staffNo 
            CostPairEntity tempCost = costList.get(0);
            tempCost.setCostFigure(costFigure);
            costList=correctList(getList());
            writeList(costList);
            
            System.out.println(costName);
            System.out.println(costFigure);
           
            
            
        }
    }

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

}

//    @Override
//    public CostEntity getFisrtCostEntity() {
//        Query query = em.createQuery("SELECT a FROM CostEntity a");
//        List<CostEntity> costs = (List<CostEntity>) query.getResultList();
//        if (!costs.isEmpty()) {
//            return costs.get(0);
//        } else {
//            return null;
//        }
//    }

