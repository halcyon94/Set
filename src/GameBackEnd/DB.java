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
 * @author Skorpion
 */

public class DB{

   private Connection connect = null;
   private Statement statement = null;
   private PreparedStatement preparedStatement = null;
   private ResultSet resultSet = null;

   //Insert user into SetDB database: performed at "Register"
   //return uid of new user
   public int insertUser(String username, String password) throws Exception{
      try{
    Class.forName("com.mysql.jdbc.Driver").newInstance();  
        connect = DriverManager.getConnection("jdbc:mysql://localhost:3306/testing?" + "user=root&password=recognitio");
    statement = connect.createStatement();
          
        String sql = "insert into users (login,password,score,gp)"+ "values (?,?,0,0)";
        
    preparedStatement = connect.prepareStatement(sql,Statement.RETURN_GENERATED_KEYS);
    preparedStatement.setString(1, username);
    preparedStatement.setString(2, password);
          preparedStatement.executeUpdate();
          ResultSet rs = preparedStatement.getGeneratedKeys();
          int out=0;
          if(rs.next())
             out = rs.getInt(1);

    return out;

      } catch (ClassNotFoundException | SQLException e) {
          e.printStackTrace();
    }
        finally {
        close();
    }
       return 0;
   }

   //Confirm user credentials: performed at "Sign In"
   //Check if input username/password exist in database
   //return uid
   public int findUser(String username, String password) throws Exception{
        try{
        	Class.forName("com.mysql.jdbc.Driver");  
        connect = DriverManager.getConnection("jdbc:mysql://localhost:3306/testing?" + "user=root&password=recognitio");
    statement = connect.createStatement();
      
        String sql = "SELECT id,login,password FROM users WHERE login = ? and password = ?";
        
    preparedStatement = connect.prepareStatement(sql);
    preparedStatement.setString(1, username);
    preparedStatement.setString(2, password);
    ResultSet k = preparedStatement.executeQuery();
        int uid=0;
        if(k.next())
            uid = Integer.parseInt(k.getString("id"));
        
    return uid;

   } catch (ClassNotFoundException | SQLException e) {
       e.printStackTrace();
    }
        finally {
        close();
    }
       return 0;
   }
   
   public int returnRating(int uid) throws Exception{
        try{
        	Class.forName("com.mysql.jdbc.Driver");  
        connect = DriverManager.getConnection("jdbc:mysql://localhost:3306/testing?" + "user=root&password=recognitio");
    statement = connect.createStatement();
      
        String sql = "SELECT score FROM users WHERE id = ?";
        
    preparedStatement = connect.prepareStatement(sql);
    preparedStatement.setString(1, Integer.toString(uid));
    ResultSet k = preparedStatement.executeQuery();
    int rating=0;
        if(k.next())
            rating = Integer.parseInt(k.getString("score"));
        
    return rating;

   } catch (ClassNotFoundException | SQLException e) {
       e.printStackTrace();
    }
        finally {
        close();
    }
       return 0;
   }
  
   //Performed at end of Game or at disconnect, whenever a player leaves a game
   //adds score to the current score of the player stored in the Database
   //increases games played by 1
   public void updateUserScore(int uid, int score) throws Exception{
        try{
    Class.forName("com.mysql.jdbc.Driver");  
        connect = DriverManager.getConnection("jdbc:mysql://localhost:3306/testing?" + "user=root&password=recognitio");
    statement = connect.createStatement();

        String sql = "UPDATE users SET score=score + ?, gp=gp+1 WHERE id = ?";
    preparedStatement = connect.prepareStatement(sql);
    preparedStatement.setInt(1,score);
    preparedStatement.setInt(2,uid);
    preparedStatement.executeUpdate();
      
            } catch (ClassNotFoundException | SQLException e) {
                e.printStackTrace();
            }
            finally {
                close();
            }
    }
 
   //Performed at "Ranking"
   //queries the database using a group by and returns a sorted list of all users sorted by score
   public String returnRankings() throws Exception{
       String message="R";
        try{
    Class.forName("com.mysql.jdbc.Driver");  
        connect = DriverManager.getConnection("jdbc:mysql://localhost:3306/testing?" + "user=root&password=recognitio");
    statement = connect.createStatement();

    resultSet = statement.executeQuery("select id,login,score from users where score IS NOT NULL order by score DESC;");

    //convert resultSet to message here
    String uid,score,username;
        while(resultSet.next()){
            uid = resultSet.getString("id");
            score = resultSet.getString("score");
            username = resultSet.getString("login");
            message =message + uid + "`" + username + "`" +score + "`";
        }
        
    return message;

    }catch (ClassNotFoundException | SQLException e) {
        e.printStackTrace();
    }
        finally {
        close();
    }
       return null;
   }
  // You need to close the resultSet
  private void close() {
    try {
      if (resultSet != null) {
        resultSet.close();
      }

      if (statement != null) {
        statement.close();
      }

      if (connect != null) {
        connect.close();
      }
    } catch (Exception e) {

    }
  }
  


}