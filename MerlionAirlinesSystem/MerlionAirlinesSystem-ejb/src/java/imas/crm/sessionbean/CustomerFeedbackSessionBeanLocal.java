/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package imas.crm.sessionbean;

import javax.ejb.Local;

/**
 *
 * @author Howard
 */
@Local
public interface CustomerFeedbackSessionBeanLocal {

    void createFeedback(String content, String title, String feedbackType, String customerEmail);
    
}
