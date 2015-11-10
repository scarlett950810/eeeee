/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package webservice;

import imas.inventory.entity.BookingClassEntity;
import java.util.Comparator;
import java.util.Objects;

/**
 *
 * @author ruicai
 */
public class BookingClassComparator implements Comparator<BookingClassEntity>{
    @Override
    public int compare(BookingClassEntity o1, BookingClassEntity o2) {
        //throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
        double diff = (double)o1.getPrice() - (double)o2.getPrice();
        if (diff > 0)
            return 1;
        else if (diff < 0)
            return -1;
        else
            return 0;
    }
    
    
}
