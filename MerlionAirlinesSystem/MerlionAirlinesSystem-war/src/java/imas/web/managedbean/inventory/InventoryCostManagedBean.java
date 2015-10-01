/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package imas.web.managedbean.inventory;

import imas.inventory.entity.CostPairEntity;
import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import java.io.Serializable;
import javax.ejb.EJB;
import imas.inventory.sessionbean.CostManagementSessionBeanLocal;
import java.util.List;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;
import javax.inject.Named;
import org.primefaces.context.RequestContext;
import org.primefaces.model.TreeNode;

/**
 *
 * @author Lei
 */
@Named(value = "inventoryCostManagedBean")
@ManagedBean
@ViewScoped
public class InventoryCostManagedBean implements Serializable {

    @EJB
    private CostManagementSessionBeanLocal costSession;
    private List<CostPairEntity> costTable;
    private TreeNode root;
    private CostPairEntity selectedCost;
    private Double newCost;
    private int cellId = 0;

    @PostConstruct
    public void init() {
//        costTable = costSession.createTable();
        root = costSession.createRoot();
    }

    public void updateCostActionListener(ActionEvent event) {
        selectedCost = (CostPairEntity) event.getComponent().getAttributes().get("costPair");

        if (selectedCost.getCostType().equals("Cost per seat per mile") || selectedCost.getCostType().equals("Fixed cost per seat per mile")
                || selectedCost.getCostType().equals("Fixed cost per seat") || selectedCost.getCostType().equals("Flight cost per seat per mile")
                || selectedCost.getCostType().equals("Passenger cost per seat per mile") || selectedCost.getCostType().equals("Average cost per passenger")
                || selectedCost.getCostType().equals("Show rate") ) {
            FacesMessage msg = new FacesMessage("Sorry", "Please change those editable costs");
            FacesContext.getCurrentInstance().addMessage(null, msg);
        } else {
            RequestContext requestContext = RequestContext.getCurrentInstance();
            requestContext.execute("PF('costDialog').show()");
        }

    }

    public void updateCost() {
        if (newCost == null) {
            FacesMessage msg = new FacesMessage("Sorry", "Please Enter the new cost");
            FacesContext.getCurrentInstance().addMessage(null, msg);
        } else {
            costSession.updateCost(selectedCost.getCostType(), newCost);
            root = costSession.createRoot();
            FacesMessage msg = new FacesMessage("Reminder", selectedCost.getCostType() + " has been changed to " + Double.toString(newCost));
            FacesContext.getCurrentInstance().addMessage(null, msg);
            
        }
//        System.out.println(costName);
//        System.out.println(costFigure);
    }

    public List<CostPairEntity> getCostTable() {
        return costTable;
    }

    public Double getNewCost() {
        return newCost;
    }

    public void setNewCost(Double newCost) {
        this.newCost = newCost;
    }

    public void setCostTable(List<CostPairEntity> costTable) {
        this.costTable = costTable;
    }

    public TreeNode getRoot() {
        return root;
    }

    public CostPairEntity getSelectedCost() {
        return selectedCost;
    }

    public void setSelectedCost(CostPairEntity selectedCost) {
        this.selectedCost = selectedCost;
    }

    public int getCellId() {
        cellId++;
        return cellId;
    }

    public void setCellId(int cellId) {
        this.cellId = cellId;
    }

}
