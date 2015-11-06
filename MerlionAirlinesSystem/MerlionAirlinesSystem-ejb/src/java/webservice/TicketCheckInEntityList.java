/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package webservice;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlElements;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

/**
 *
 * @author ruicai
 */

@XmlRootElement
@XmlType(propOrder = {
    "ticketCheckInEntity"
})

public class TicketCheckInEntityList implements Serializable
{
    private List<String> ticketCheckInEntityList;

    
    
    public TicketCheckInEntityList() 
    {
        ticketCheckInEntityList = new ArrayList<>();
    }

    
    
    public TicketCheckInEntityList(List<String> ticketCheckInEntity) 
    {
        this.ticketCheckInEntityList = ticketCheckInEntity;
    }

    
    
    @XmlElements({
        @XmlElement(name = "ticketCheckInEntity", type = String.class)
    })
    @XmlElementWrapper
    public List<String> getTicketCheckInEntity() {
        return ticketCheckInEntityList;
    }

    public void setTicketCheckInEntity(List<String> ticketCheckInEntity) {
        this.ticketCheckInEntityList = ticketCheckInEntity;
    }
}