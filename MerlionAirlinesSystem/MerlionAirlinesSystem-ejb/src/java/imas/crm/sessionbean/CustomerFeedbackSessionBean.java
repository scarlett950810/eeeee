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
        String addressTo = "";
        if(feedbackType.equals("General Enquiry")){
            addressTo = "howe0819@gmail.com";
        }else if(feedbackType.equals("Ticketing Office")){
            addressTo = "cairui0112@gmail.com";
        }else if(feedbackType.equals("Customer Service")){
            addressTo = "Scarlett.Dongyan@gmail.com";
        }else if(feedbackType.equals("Membereship Service")){
            addressTo = "yinlei1993@gmail.com";
        }
        EmailManager.run(addressTo, "From " + customerEmail + ": " + title , content);
//        EmailManager.run("A0119509@u.nus.edu", "From "+ customerEmail + ": " + title, content);
    }

    
}
