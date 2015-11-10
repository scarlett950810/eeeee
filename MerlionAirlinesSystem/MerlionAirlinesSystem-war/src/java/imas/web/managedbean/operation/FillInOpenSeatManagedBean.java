/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package imas.web.managedbean.operation;

import imas.common.entity.CabinCrewEntity;
import imas.common.entity.PilotEntity;
import imas.operation.sessionbean.FillInOpenSeatSessionBeanLocal;
import imas.planning.entity.FlightEntity;
import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.faces.view.ViewScoped;
import javax.inject.Named;

/**
 *
 * @author Lei
 */
@Named(value = "fillInOpenSeatManagedBean")
@ViewScoped
public class FillInOpenSeatManagedBean implements Serializable {

    @EJB
    private FillInOpenSeatSessionBeanLocal fillInOpenSeatSessionBean;

    private String base = "SGC";

    private List<FlightEntity> flights;
    private FlightEntity flight;

    private Map<FlightEntity, Map<String, Map<String, String>>> result;
    private Map<String, Map<String, String>> data;
    private Map<String, String> map;
    private String position;
    private String name;
    private Map<String, String> positions;
    private Map<String, String> names;

    private PilotEntity pilot;
    private CabinCrewEntity crew;
    private List<PilotEntity> availablePilots;
    private List<CabinCrewEntity> availableCrews;
    private PilotEntity selectedPilot;
    private CabinCrewEntity selectedCrew;

    private boolean displayPilot = false;
    private boolean displayCabinCrew = false;

    @PostConstruct
    public void init() {
        fetchFlights();
    }
//

    public void fetchFlights() {
        System.out.print("start start");
        flights = fillInOpenSeatSessionBean.fetchComingFlights(base);
        FacesContext.getCurrentInstance().getExternalContext().getSessionMap().put("allFlights", flights);
        data = new HashMap<>();
        positions = new HashMap<>();
        names = new HashMap<>();
        map = new HashMap<>();
        position = null;
        name = null;
    }
//event="change" process="@this position"   name, position   
//    

    public void onFlightChange() {
        System.out.print("flight flight ");
        displayCabinCrew = false;
        displayPilot = false;
        data = new HashMap<>();
        positions = new HashMap<>();
        names = new HashMap<>();
        position=null;
        name=null;
        if (flight != null) {
            positions.put("Pilot", "Pilot");
            positions.put("Cabin Crew", "Cabin Crew");
            map = fillInOpenSeatSessionBean.getMissingPilot(flight);
            data.put("Pilot", map);
            map = fillInOpenSeatSessionBean.getMissingCabinCrew(flight);
            data.put("Cabin Crew", map);

        } else {

        }
    }

    public void onPositionChange() {
        System.out.print("position");
        displayCabinCrew = false;
        displayPilot = false;
        if (position != null && !position.equals("") && !position.equals("Select Position")) {
            names = data.get(position);
        } else {
            names = new HashMap<>();
        }
    }

    public void onNameChange() {
        System.out.print("name name name");
        if (name != null && !name.equals("") && !name.equals("Select Staff")) {
            if (position.equals("Pilot")) {
                System.out.print("sdfffffffffff");
                displayPilot = true;
                displayCabinCrew = false;
                pilot = fillInOpenSeatSessionBean.getPilot(name);
                if (pilot != null) {
                    System.out.print("fffffff");
                    availablePilots = fillInOpenSeatSessionBean.doPilotFillIn(pilot, flight);
                }
            } else if (position.equals("Cabin Crew")) {
                System.out.print("serrre");
                displayCabinCrew = true;
                displayPilot = false;
                crew = fillInOpenSeatSessionBean.getCrew(name);
                if (crew != null) {
                    System.out.print("fffffff");
                    availableCrews = fillInOpenSeatSessionBean.doCrewFillIn(crew, flight);
                }

            } else {
                System.out.print("scvxxcvcv");
                displayCabinCrew = false;
                displayPilot = false;
            }
        }

    }

    public void doPilotFillIn() throws IOException {
        System.out.print("pilot pilot pilot");
        System.out.print(name);
        System.out.print(position);
        System.out.print(selectedPilot);
        if (selectedPilot != null && position.equals("Pilot")) {
            fillInOpenSeatSessionBean.replacePilot(pilot, selectedPilot, flight);
        }
//        FacesContext fc = FacesContext.getCurrentInstance();
//        ExternalContext ec = fc.getExternalContext();
//        ec.redirect("operationFillInOpenSeat.xhtml");

    }

    public void doCrewFillIn() throws IOException {
        System.out.print("crew  crew crew");
        System.out.print(name);
        System.out.print(position);
        System.out.print(selectedCrew);
        if (selectedCrew != null && position.equals("Cabin Crew")) {
            fillInOpenSeatSessionBean.replaceCrew(crew, selectedCrew, flight);
        }
//        FacesContext fc = FacesContext.getCurrentInstance();
//        ExternalContext ec = fc.getExternalContext();
//        ec.redirect("operationFillInOpenSeat.xhtml");

    }

    public List<FlightEntity> getFlights() {
        return flights;
    }

    public void setFlights(List<FlightEntity> flights) {
        this.flights = flights;
    }

    public FlightEntity getFlight() {
        return flight;
    }

    public void setFlight(FlightEntity flight) {
        this.flight = flight;
    }

    public List<PilotEntity> getAvailablePilots() {
        return availablePilots;
    }

    public void setAvailablePilots(List<PilotEntity> availablePilots) {
        this.availablePilots = availablePilots;
    }

    public List<CabinCrewEntity> getAvailableCrews() {
        return availableCrews;
    }

    public void setAvailableCrews(List<CabinCrewEntity> availableCrews) {
        this.availableCrews = availableCrews;
    }

    public PilotEntity getSelectedPilot() {
        return selectedPilot;
    }

    public void setSelectedPilot(PilotEntity selectedPilot) {
        this.selectedPilot = selectedPilot;
    }

    public CabinCrewEntity getSelectedCrew() {
        return selectedCrew;
    }

    public void setSelectedCrew(CabinCrewEntity selectedCrew) {
        this.selectedCrew = selectedCrew;
    }

    public boolean isDisplayPilot() {
        return displayPilot;
    }

    public void setDisplayPilot(boolean displayPilot) {
        this.displayPilot = displayPilot;
    }

    public boolean isDisplayCabinCrew() {
        return displayCabinCrew;
    }

    public void setDisplayCabinCrew(boolean displayCabinCrew) {
        this.displayCabinCrew = displayCabinCrew;
    }

    public String getPosition() {
        return position;
    }

    public void setPosition(String position) {
        this.position = position;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Map<String, String> getPositions() {
        return positions;
    }

    public void setPositions(Map<String, String> positions) {
        this.positions = positions;
    }

    public Map<String, String> getNames() {
        return names;
    }

    public void setNames(Map<String, String> names) {
        this.names = names;
    }

}
