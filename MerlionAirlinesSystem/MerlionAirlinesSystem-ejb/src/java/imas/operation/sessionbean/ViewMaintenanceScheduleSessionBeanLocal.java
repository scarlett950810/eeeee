/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package imas.operation.sessionbean;

import java.util.Map;
import javax.ejb.Local;
import org.primefaces.model.ScheduleModel;

/**
 *
 * @author Lei
 */
@Local
public interface ViewMaintenanceScheduleSessionBeanLocal {
    public Map<String, String> getAircrafts();

//    public List<AircraftEntity> getAircraftList();

    public void create();

    public ScheduleModel createEvent(String aircraft);


    
}
