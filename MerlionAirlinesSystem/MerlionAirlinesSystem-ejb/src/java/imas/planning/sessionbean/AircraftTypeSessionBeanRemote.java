/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package imas.planning.sessionbean;

import imas.planning.entity.AircraftEntity;
import imas.planning.entity.AircraftTypeEntity;
import java.util.List;

/**
 *
 * @author wutong
 */
interface AircraftTypeSessionBeanRemote {
    List<AircraftTypeEntity> getAllAircraftTypes();
    void updateAircraftType(AircraftTypeEntity aircraftType);

    Boolean checkAircraftType(String IATACode);

    void addAircraftType(AircraftTypeEntity aircraftType);

    Boolean deleteAircraftType(String IATACode);

    List<AircraftEntity> getAircraftsFromAircraftType(AircraftTypeEntity aircraftType);
   
}
