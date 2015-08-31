/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package imas.common.sessionbean;

import imas.common.entity.StaffEntity;
import javax.ejb.Stateful;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
/**
 *
 * @author Howard
 */
@Stateful
public class LoginSessionBean implements LoginSessionBeanLocal {                

    @PersistenceContext
    private EntityManager entityManager;

    public LoginSessionBean() {
    }
    
    @Override
    public Boolean doLogin(String staffNo, String password) {
        /*String url = "jdbc:mysql://localhost:3307/MerlionInternal";
        String username = "root";
        String password = "1234";

        System.out.println("Connecting database…");

        try (Connection connection = DriverManager.getConnection(url, username, password)) {
            System.out.println("Database connected!");
            return true;
        }
        catch (SQLException e) {
            throw new IllegalStateException("Cannot connect the database!", e);
        }
        */
        
        
        Query query = entityManager.createQuery("SELECT s FROM StaffEntity s WHERE s.staffNo = :staffNumber AND s.password = :password");
        query.setParameter("staffNumber", staffNo);
        query.setParameter("password", password);
        
        List<StaffEntity> staff = (List<StaffEntity>)query.getResultList();
        if(!staff.isEmpty()){
            return true;
        }else{
            return false;
        }
        
    
    }
    
}
