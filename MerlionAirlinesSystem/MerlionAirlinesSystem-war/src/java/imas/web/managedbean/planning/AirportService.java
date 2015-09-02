/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package imas.web.managedbean.planning;

import imas.planning.entity.AirportEntity;
import imas.planning.sessionbean.AirportSessionBeanLocal;
import java.util.List;
import javax.annotation.ManagedBean;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.enterprise.context.RequestScoped;
import javax.faces.bean.ApplicationScoped;
import javax.inject.Named;

/**
 *
 * @author Howard
 */

@Named(value = "airportService")
@ManagedBean
@ApplicationScoped
public class AirportService {
    @EJB
    private AirportSessionBeanLocal airportSessionBean;

    /**
     * Creates a new instance of AirportService
     */
    private List<AirportEntity> airports;

    public List<AirportEntity> getAirports() {
        return airports;
    }
    
    @PostConstruct
    public void init() {
        airports = airportSessionBean.fetchAirport();
        System.out.print("Airport Srevice Init");
    }
        
}
