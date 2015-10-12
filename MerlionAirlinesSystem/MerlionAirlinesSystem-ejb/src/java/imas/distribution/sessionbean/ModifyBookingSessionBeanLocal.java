/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package imas.distribution.sessionbean;

import imas.distribution.entity.PNREntity;
import javax.ejb.Local;

/**
 *
 * @author Howard
 */
@Local
public interface ModifyBookingSessionBeanLocal {

    PNREntity retrievePNRRecord(String referenceNumber);
    
}
