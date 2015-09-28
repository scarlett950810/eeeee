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
//    private String newEmergencyOfAtcViolation;//1
//    private String newMechanicalFailures;//2
//    private String newFuelDumping;//3
//    private String newPassengerSpecialStatus;//4
//    private String newCrewSpecialStatus;//5
//    private String newPassengerMisconduct;//6
//    private String newHazmatIssue;//7
//    private String newDiversions;//8
//    private String newHighSpeedAbort;//9
//    private String newLightningStrikers;//10
//    private String newNearAirCollisions;//11
//    private String newOthers;//12
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

    public void update() {
        if (!selectedFlight.getEmergencyOfAtcViolation().isEmpty()||!selectedFlight.getMechanicalFailures().isEmpty()||
                !selectedFlight.getFuelDumping().isEmpty()||!selectedFlight.getPassengerSpecialStatus().isEmpty()||
                !selectedFlight.getCrewSpecialStatus().isEmpty()||!selectedFlight.getPassengerMisconduct().isEmpty()||
                !selectedFlight.getHazmatIssue().isEmpty()||!selectedFlight.getDiversions().isEmpty()||
                !selectedFlight.getHighSpeedAborts().isEmpty()||!selectedFlight.getHighSpeedAborts().isEmpty()||
                !selectedFlight.getNearAirCollisions().isEmpty()||!selectedFlight.getOthers().isEmpty() ) { 
            
            
            postFlightReportSessionBean.updateReport(selectedFlight);
            FacesMessage msg = new FacesMessage("Reminder", selectedFlight.getFlightNo()+ " has been updated " );
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

   
}
