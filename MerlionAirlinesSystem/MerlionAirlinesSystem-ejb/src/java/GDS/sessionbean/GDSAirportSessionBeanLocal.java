/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package GDS.sessionbean;

import GDS.entity.GDSAirportEntity;
import java.util.List;
import javax.ejb.Local;

/**
 *
 * @author Scarlett
 */
@Local
public interface GDSAirportSessionBeanLocal {

    public void insertAllGDSAirportData();
    public List<GDSAirportEntity> getAllGDSAirport();
    public List<String> getAllCountries();
    public List<GDSAirportEntity> getGDSAirportInCountry(String countryName);
    public GDSAirportEntity getGDSAirport(String IATAorFAA);
    
}
