/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package irms.common.sessionbean;

import javax.ejb.Stateful;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
/**
 *
 * @author Howard
 */
@Stateful
public class LoginSessionBean implements LoginSessionBeanLocal {                

    @Override
    public Boolean doLogin() {
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
        
    }
    
}
