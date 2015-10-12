/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package imas.inventory.sessionbean;

import imas.inventory.entity.CostPairEntity;
import imas.planning.entity.RouteEntity;
import java.util.List;
import javax.ejb.Local;
import org.primefaces.model.TreeNode;

/**
 *
 * @author Lei
 */
@Local
public interface CostManagementSessionBeanLocal {

//    public List<CostPairEntity> createTable();
//     public CostEntity getFisrtCostEntity() ;
//    public void writeList(List<CostPairEntity> list);
    public List<CostPairEntity> correctList(List<CostPairEntity> list);

    public void updateCost(RouteEntity selectedRoute, String costName, Double costFigure);

    public Double getCostPerSeatPerMile();

//    public void updateShowRate(Double showRate);
    public void intiCostTable(RouteEntity selectedRoute);

    public TreeNode createRoot(List<CostPairEntity> list);

//    public Double getCostPerSeatPerMile(RouteEntity SelectedRoute);

    public Double getCostPerSeatPerMile(RouteEntity selectedRoute);

    public List<CostPairEntity> getList(RouteEntity selectedRoute);

}
