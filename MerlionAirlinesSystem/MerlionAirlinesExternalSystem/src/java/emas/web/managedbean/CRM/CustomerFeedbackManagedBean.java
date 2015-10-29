/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package emas.web.managedbean.CRM;

import imas.crm.sessionbean.CustomerFeedbackSessionBeanLocal;
import java.io.IOException;
import java.io.Serializable;
import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;

/**
 *
 * @author Howard
 */
@ManagedBean
@ViewScoped
public class CustomerFeedbackManagedBean implements Serializable{
    @EJB
    private CustomerFeedbackSessionBeanLocal customerFeedbackSessionBean;
    
    
    
    private String feedback;
    private String title;
    private String feedbackType;
    private String customerEmail;
    /**
     * Creates a new instance of FeedbackManagedBean
     */
    public CustomerFeedbackManagedBean() {
    }
    
    
    
    public void createFeedback() throws IOException{
        customerFeedbackSessionBean.createFeedback(feedback, title, feedbackType, customerEmail);
        feedback = null;
        title = null;
        feedback = null;
        customerEmail = null;
    }

    public String getFeedback() {
        return feedback;
    }

    public void setFeedback(String feedback) {
        this.feedback = feedback;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getFeedbackType() {
        return feedbackType;
    }

    public void setFeedbackType(String feedbackType) {
        this.feedbackType = feedbackType;
    }

    public String getCustomerEmail() {
        return customerEmail;
    }

    public void setCustomerEmail(String customerEmail) {
        this.customerEmail = customerEmail;
    }
    
}
