/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package imas.operation.sessionbean;

import imas.common.entity.CabinCrewEntity;
import imas.common.entity.PilotEntity;
import imas.planning.entity.FlightEntity;
import java.util.List;
import java.util.Map;
import javax.ejb.Local;

/**
 *
 * @author Lei
 */
@Local
public interface FillInOpenSeatSessionBeanLocal {

    public List<FlightEntity> fetchComingFlights(String base);



    public Map<String, String> getMissingPilot(FlightEntity selectedFlight);

    public Map<String, String> getMissingCabinCrew(FlightEntity selectedFlight);

    public List<PilotEntity> doPilotFillIn(PilotEntity selectedPilot, FlightEntity selectedFlight);

    public PilotEntity getPilot(String name);

    public void replacePilot(PilotEntity oldPilot, PilotEntity newPilot, FlightEntity selectedFlight);

    public CabinCrewEntity getCrew(String name);

    public List<CabinCrewEntity> doCrewFillIn(CabinCrewEntity selectedCrew, FlightEntity selectedFlight);

    public void replaceCrew(CabinCrewEntity oldCrew, CabinCrewEntity newCrew, FlightEntity selectedFlight);
    
    
}
