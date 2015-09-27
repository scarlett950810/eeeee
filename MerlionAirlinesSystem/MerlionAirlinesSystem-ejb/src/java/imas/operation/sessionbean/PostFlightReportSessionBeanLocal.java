/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package imas.operation.sessionbean;

import imas.planning.entity.FlightEntity;
import java.util.List;
import javax.ejb.Local;

/**
 *
 * @author Lei
 */
@Local
public interface PostFlightReportSessionBeanLocal {

    public List<FlightEntity> getList();

    public void init();

    public void updateReport(FlightEntity selectedFlight);

}
