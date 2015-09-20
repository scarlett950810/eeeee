/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package imas.inventory.sessionbean;


import imas.inventory.entity.CostPairEntity;
import java.util.List;
import javax.ejb.Local;
import org.primefaces.model.TreeNode;


/**
 *
 * @author Lei
 */
@Local
public interface CostManagementSessionBeanLocal {

    public List<CostPairEntity> createTable();
    
//     public CostEntity getFisrtCostEntity() ;

    public void writeList(List<CostPairEntity> list);

    public TreeNode createRoot();

    public List<CostPairEntity> getList();

    public List<CostPairEntity> correctList(List<CostPairEntity> list);

    public void updateCost(String costName, Double costFigure);

    
}
