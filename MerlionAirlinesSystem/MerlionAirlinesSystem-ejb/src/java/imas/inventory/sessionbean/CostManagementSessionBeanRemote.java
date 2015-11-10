/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package imas.inventory.sessionbean;

import imas.inventory.entity.CostPairEntity;
import imas.planning.entity.RouteEntity;
import java.util.List;
import javax.ejb.Remote;
import org.primefaces.model.TreeNode;

/**
 *
 * @author Lei
 */
@Remote
public interface CostManagementSessionBeanRemote {

    public void initCostTable(RouteEntity selectedRoute);

    public TreeNode createRoot(List<CostPairEntity> list);

    public void updateCost(RouteEntity selectedRoute, String costName, Double costFigure);

    public void updateShowRate(RouteEntity selectedRoute, Double showRate);

    public List<CostPairEntity> correctList(List<CostPairEntity> list);

    public List<CostPairEntity> getCostPairList(RouteEntity selectedRoute);

    public Double getCostPerSeatPerMile(RouteEntity selectedRoute);
}
