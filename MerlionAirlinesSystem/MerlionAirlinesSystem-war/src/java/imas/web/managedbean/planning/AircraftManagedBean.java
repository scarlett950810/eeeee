/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package imas.web.managedbean.planning;

import imas.planning.sessionbean.AircraftSessionBeanLocal;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.ejb.LocalBean;

/**
 *
 * @author Scarlett
 */
@Stateless
@LocalBean
public class AircraftManagedBean {
    @EJB
    private AircraftSessionBeanLocal aircraftSessionBean;
    
    
    // Add business logic below. (Right-click in editor and choose
    // "Insert Code > Add Business Method")
}
