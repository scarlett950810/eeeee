/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package imas.crm.sessionbean;

import imas.utility.sessionbean.EmailManager;
import javax.ejb.Stateless;

/**
 *
 * @author Howard
 */
@Stateless
public class CustomerFeedbackSessionBean implements CustomerFeedbackSessionBeanLocal {

    @Override
    public void createFeedback(String content, String title, String feedbackType, String customerEmail) {
        EmailManager.run("A0119509@u.nus.edu", "From "+ customerEmail + ": " + title, content);
    }

    
}
