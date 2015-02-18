/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package GameBackEnd;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Date;
/**
 *
 * @author sokolo
 */
public class testDB {

    /**
     * @param args the command line arguments
     * @throws java.lang.Exception
     */
    public static void main(String[] args) throws Exception {
      
   Connection connect = null;
   Statement statement = null;
   PreparedStatement preparedStatement = null;
   ResultSet resultSet = null;
 
   /*try*/

        String sql = "CREATE TABLE REGISTRATION " +
                   "(id INTEGER not NULL, " +
                   " first VARCHAR(255), " +
                   " last VARCHAR(255), " +
                   " age INTEGER, " +
                   " PRIMARY KEY ( id ))";
       
        Class.forName("com.mysql.jdbc.Driver").newInstance();  
        //connect = DriverManager.getConnection("jdbc:mysql://199.98.20.114:5122/setdb" + "user=root&password=toor", "cooper", "repoocrepooc");
        connect = DriverManager.getConnection("jdbc:mysql://localhost:3306/world?" + "user=root&password=recognitio");
        statement = connect.createStatement();
        statement.executeUpdate(sql);
        //System.out.println(resultSet);
    }
    
}