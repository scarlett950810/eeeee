package imas.web.managedbean.operation;

import imas.operation.sessionbean.PostFlightReportSessionBeanLocal;
import imas.planning.entity.FlightEntity;
import java.io.Serializable;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;
import org.primefaces.context.RequestContext;

/**
 *
 * @author Lei
 */
//@Named(value="postFlightReportManagedBean")
@ManagedBean
@ViewScoped
public class PostFlightReportManagedBean implements Serializable {

    /**
     * Creates a new instance of PostFlightReportManagedBean
     */
    private List<FlightEntity> list;
    @EJB
    private PostFlightReportSessionBeanLocal postFlightReportSessionBean;

    private FlightEntity selectedFlight;
    private String newEmergencyOfAtcViolation;//1
    private String newMechanicalFailures;//2
    private String newFuelDumping;//3
    private String newPassengerSpecialStatus;//4
    private String newCrewSpecialStatus;//5
    private String newPassengerMisconduct;//6
    private String newHazmatIssue;//7
    private String newDiversions;//8
    private String newHighSpeedAbort;//9
    private String newLightningStrikers;//10
    private String newNearAirCollisions;//11
    private String newOthers;//12
    private Integer updateType = 0;

    public PostFlightReportManagedBean() {

    }

    @PostConstruct
    public void init() {
        System.out.println("start");
        if (postFlightReportSessionBean.getList() == null || postFlightReportSessionBean.getList().isEmpty()) {
            postFlightReportSessionBean.init();
        }
        this.list = postFlightReportSessionBean.getList();
    }

    public void updateFlightReportActionListener(ActionEvent event) {
        selectedFlight = (FlightEntity) event.getComponent().getAttributes().get("flight");
        RequestContext requestContext = RequestContext.getCurrentInstance();
        requestContext.execute("PF('flightDialog').show()");

    }

    public void updateFlightSpecialReportActionListener(ActionEvent event) {
        selectedFlight = (FlightEntity) event.getComponent().getAttributes().get("flight");
        RequestContext requestContext = RequestContext.getCurrentInstance();
        requestContext.execute("PF('flightSpecialDialog').show()");

    }
//1

    public void updateEmergencyOfAtcViolation() {
        if (!newEmergencyOfAtcViolation.isEmpty()) {
            this.selectedFlight.setEmergencyOfAtcViolation(newEmergencyOfAtcViolation);
            updateType = 1;
            postFlightReportSessionBean.updateReport(selectedFlight, updateType);
            FacesMessage msg = new FacesMessage("Reminder", "Emergency Of Atc Violation" + " has been changed to  " + newEmergencyOfAtcViolation);
            FacesContext.getCurrentInstance().addMessage(null, msg);
        } else {
            FacesMessage msg = new FacesMessage("Sorry", "Please try to update again");
            FacesContext.getCurrentInstance().addMessage(null, msg);
        }
    }
//2

    public void updateMechanicalFailures() {
        if (!newMechanicalFailures.isEmpty()) {
            this.selectedFlight.setMechanicalFailures(newMechanicalFailures);
            updateType = 2;
            postFlightReportSessionBean.updateReport(selectedFlight, updateType);
            FacesMessage msg = new FacesMessage("Reminder", "Mechanical failures" + " has been changed to  " + newMechanicalFailures);
            FacesContext.getCurrentInstance().addMessage(null, msg);
        } else {
            FacesMessage msg = new FacesMessage("Sorry", "Please try to update again");
            FacesContext.getCurrentInstance().addMessage(null, msg);
        }
    }
    //3  

    public void updateFuelDumping() {
        if (!newFuelDumping.isEmpty()) {
            this.selectedFlight.setFuelDumping(newFuelDumping);
            updateType = 3;
            postFlightReportSessionBean.updateReport(selectedFlight, updateType);
            FacesMessage msg = new FacesMessage("Reminder", "Fuel dumping: " + " has been changed to  " + newFuelDumping);
            FacesContext.getCurrentInstance().addMessage(null, msg);
        } else {
            FacesMessage msg = new FacesMessage("Sorry", "Please try to update again");
            FacesContext.getCurrentInstance().addMessage(null, msg);
        }
    }
//4

    public void updatePassengerSpecialStatus() {
        if (!newPassengerSpecialStatus.isEmpty()) {
            this.selectedFlight.setPassengerSpecialStatus(newPassengerSpecialStatus);
            updateType = 4;
            postFlightReportSessionBean.updateReport(selectedFlight, updateType);
            FacesMessage msg = new FacesMessage("Reminder", "Illness/injury/death of a passenger:" + " has been changed to  " + newPassengerSpecialStatus);
            FacesContext.getCurrentInstance().addMessage(null, msg);
        } else {
            FacesMessage msg = new FacesMessage("Sorry", "Please try to update again");
            FacesContext.getCurrentInstance().addMessage(null, msg);
        }
    }
//5

    public void updateCrewSpecialStatus() {
        if (!newCrewSpecialStatus.isEmpty()) {
            this.selectedFlight.setCrewSpecialStatus(newCrewSpecialStatus);
            updateType = 5;
            postFlightReportSessionBean.updateReport(selectedFlight, updateType);
            FacesMessage msg = new FacesMessage("Reminder", "Illness/injury/death of a crew member:" + " has been changed to  " + newCrewSpecialStatus);
            FacesContext.getCurrentInstance().addMessage(null, msg);
        } else {
            FacesMessage msg = new FacesMessage("Sorry", "Please try to update again");
            FacesContext.getCurrentInstance().addMessage(null, msg);
        }
    }
//6

    public void updatePassengerMisconduct() {
        if (!newPassengerMisconduct.isEmpty()) {
            this.selectedFlight.setPassengerMisconduct(newPassengerMisconduct);
            updateType = 6;
            postFlightReportSessionBean.updateReport(selectedFlight, updateType);
            FacesMessage msg = new FacesMessage("Reminder", "Passenger misconduct (e.g. smoking):" + " has been changed to  " + newPassengerMisconduct);
            FacesContext.getCurrentInstance().addMessage(null, msg);
        } else {
            FacesMessage msg = new FacesMessage("Sorry", "Please try to update again");
            FacesContext.getCurrentInstance().addMessage(null, msg);
        }
    }
//7

    public void updateHazmatIssue() {
        if (!newHazmatIssue.isEmpty()) {
            this.selectedFlight.setHazmatIssue(newHazmatIssue);
            updateType = 7;
            postFlightReportSessionBean.updateReport(selectedFlight, updateType);
            FacesMessage msg = new FacesMessage("Reminder", "HAZMAT issue: " + " has been changed to  " + newHazmatIssue);
            FacesContext.getCurrentInstance().addMessage(null, msg);
        } else {
            FacesMessage msg = new FacesMessage("Sorry", "Please try to update again");
            FacesContext.getCurrentInstance().addMessage(null, msg);
        }
    }
//8

    public void updateDiversions() {
        if (!newDiversions.isEmpty()) {
            this.selectedFlight.setDiversions(newDiversions);
            updateType = 8;
            postFlightReportSessionBean.updateReport(selectedFlight, updateType);
            FacesMessage msg = new FacesMessage("Reminder", "Diversions:" + " has been changed to " + newDiversions);
            FacesContext.getCurrentInstance().addMessage(null, msg);
        } else {
            FacesMessage msg = new FacesMessage("Sorry", "Please try to update again");
            FacesContext.getCurrentInstance().addMessage(null, msg);
        }
    }
//9

    public void updateHighSpeedAbort() {
        if (!newHighSpeedAbort.isEmpty()) {
            this.selectedFlight.setHighSpeedAborts(newHighSpeedAbort);
            updateType = 9;
            postFlightReportSessionBean.updateReport(selectedFlight, updateType);
            FacesMessage msg = new FacesMessage("Reminder", "High-speed aborts: " + " has been changed to  " + newHighSpeedAbort);
            FacesContext.getCurrentInstance().addMessage(null, msg);
        } else {
            FacesMessage msg = new FacesMessage("Sorry", "Please try to update again");
            FacesContext.getCurrentInstance().addMessage(null, msg);
        }
    }
//10

    public void updateLightningStrikers() {
        if (!newLightningStrikers.isEmpty()) {
            this.selectedFlight.setLightningStrikers(newLightningStrikers);
            updateType = 10;
            postFlightReportSessionBean.updateReport(selectedFlight, updateType);
            FacesMessage msg = new FacesMessage("Reminder", "Lightning strikes: " + " has been changed to  " + newLightningStrikers);
            FacesContext.getCurrentInstance().addMessage(null, msg);
        } else {
            FacesMessage msg = new FacesMessage("Sorry", "Please try to update again");
            FacesContext.getCurrentInstance().addMessage(null, msg);
        }
    }

    //11
    public void updateNearAirCollisions() {
        if (!newNearAirCollisions.isEmpty()) {
            this.selectedFlight.setNearAirCollisions(newNearAirCollisions);
            updateType = 11;
            postFlightReportSessionBean.updateReport(selectedFlight, updateType);
            FacesMessage msg = new FacesMessage("Reminder", "Near-air collisions: " + " has been changed to  " + newNearAirCollisions);
            FacesContext.getCurrentInstance().addMessage(null, msg);
        } else {
            FacesMessage msg = new FacesMessage("Sorry", "Please try to update again");
            FacesContext.getCurrentInstance().addMessage(null, msg);
        }
    }

    //12
    public void updateOthers() {
        if (!newOthers.isEmpty()) {
            this.selectedFlight.setOthers(newOthers);
            updateType = 12;
            postFlightReportSessionBean.updateReport(selectedFlight, updateType);
            FacesMessage msg = new FacesMessage("Reminder", "Near-air collisions: " + " has been changed to  " + newOthers);
            FacesContext.getCurrentInstance().addMessage(null, msg);
        } else {
            FacesMessage msg = new FacesMessage("Sorry", "Please try to update again");
            FacesContext.getCurrentInstance().addMessage(null, msg);
        }
    }

    public List<FlightEntity> getList() {
        return list;
    }

    public void setList(List<FlightEntity> list) {
        this.list = list;
    }

    public FlightEntity getSelectedFlight() {
        return selectedFlight;
    }

    public void setSelectedFlight(FlightEntity selectedFlight) {
        this.selectedFlight = selectedFlight;
    }

    public String getNewEmergencyOfAtcViolation() {
        return newEmergencyOfAtcViolation;
    }

    public void setNewEmergencyOfAtcViolation(String newEmergencyOfAtcViolation) {
        this.newEmergencyOfAtcViolation = newEmergencyOfAtcViolation;
    }

    public String getNewMechanicalFailures() {
        return newMechanicalFailures;
    }

    public void setNewMechanicalFailures(String newMechanicalFailures) {
        this.newMechanicalFailures = newMechanicalFailures;
    }

    public Integer getUpdateType() {
        return updateType;
    }

    public void setUpdateType(Integer updateType) {
        this.updateType = updateType;
    }

    public String getNewFuelDumping() {
        return newFuelDumping;
    }

    public void setNewFuelDumping(String newFuelDumping) {
        this.newFuelDumping = newFuelDumping;
    }

    public String getNewPassengerSpecialStatus() {
        return newPassengerSpecialStatus;
    }

    public void setNewPassengerSpecialStatus(String newPassengerSpecialStatus) {
        this.newPassengerSpecialStatus = newPassengerSpecialStatus;
    }

    public String getNewCrewSpecialStatus() {
        return newCrewSpecialStatus;
    }

    public void setNewCrewSpecialStatus(String newCrewSpecialStatus) {
        this.newCrewSpecialStatus = newCrewSpecialStatus;
    }

    public String getNewPassengerMisconduct() {
        return newPassengerMisconduct;
    }

    public void setNewPassengerMisconduct(String newPassengerMisconduct) {
        this.newPassengerMisconduct = newPassengerMisconduct;
    }

    public String getNewHazmatIssue() {
        return newHazmatIssue;
    }

    public void setNewHazmatIssue(String newHazmatIssue) {
        this.newHazmatIssue = newHazmatIssue;
    }

    public String getNewDiversions() {
        return newDiversions;
    }

    public void setNewDiversions(String newDiversions) {
        this.newDiversions = newDiversions;
    }

    public String getNewHighSpeedAbort() {
        return newHighSpeedAbort;
    }

    public void setNewHighSpeedAbort(String newHighSpeedAbort) {
        this.newHighSpeedAbort = newHighSpeedAbort;
    }

    public String getNewLightningStrikers() {
        return newLightningStrikers;
    }

    public void setNewLightningStrikers(String newLightningStrikers) {
        this.newLightningStrikers = newLightningStrikers;
    }

    public String getNewNearAirCollisions() {
        return newNearAirCollisions;
    }

    public void setNewNearAirCollisions(String newNearAirCollisions) {
        this.newNearAirCollisions = newNearAirCollisions;
    }

    public String getNewOthers() {
        return newOthers;
    }

    public void setNewOthers(String newOthers) {
        this.newOthers = newOthers;
    }

}
