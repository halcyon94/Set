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

class DB{

   private Connection connect = null;
   private Statement statement = null;
   private PreparedStatement preparedStatement = null;
   private ResultSet resultSet = null;

   //Insert user into SetDB database: performed at "Register"
   //return uid of new user
   public int insertUser(String username, String password) throws Exception{
      try{
    Class.forName("com.mysql.jdbc.Driver").newInstance();  
        connect = DriverManager.getConnection("jdbc:mysql://localhost:3306/setdb?" + "user=root&password=recognitio");
    statement = connect.createStatement();
          
        String sql = "insert into users (login,password) values ("+ username +", "+ password +")";
    preparedStatement = connect.prepareStatement(sql);
    preparedStatement.executeQuery();

    //writeResultSet(resultSet);

        String id = resultSet.getString("id");
        System.out.println("ID: " + id);
    return Integer.parseInt(id);

      } catch (ClassNotFoundException | SQLException e) {
        throw e;
    }
        finally {
        close();
    }
   }

   //Confirm user credentials: performed at "Sign In"
   //Check if input username/password exist in database
   //return uid
   public int findUser(String username, String password) throws Exception{
        try{
    Class.forName("com.mysql.jdbc.Driver");  

        connect = DriverManager.getConnection("jdbc:mysql://localhost:3306/setdb?" + "user=root&password=recognitio");
    statement = connect.createStatement();
      
        String sql = "select login,password from users where login = "+username+" and password = sha1("+ password +")";
    preparedStatement = connect.prepareStatement(sql);
    preparedStatement.executeQuery();

    //writeResultSet(resultSet);

        String id = resultSet.getString("id");
        System.out.println("ID: " + id);
    return Integer.parseInt(id);

   } catch (ClassNotFoundException | SQLException e) {
        throw e;
    }
        finally {
        close();
    }
   }
  
   //Performed at end of Game or at disconnect, whenever a player leaves a game
   //adds score to the current score of the player stored in the Database
   //increases games played by 1
   public void updateUserScore(int uid, int score) throws Exception{
        try{
    Class.forName("com.mysql.jdbc.Driver");  
        connect = DriverManager.getConnection("jdbc:mysql://localhost:3306/setdb?" + "user=root&password=recognitio");
    statement = connect.createStatement();

        String sql = "update users set score=score + "+score+" where id = "+uid+"; update users set gp=gp+1 where id = "+uid+"";
    preparedStatement = connect.prepareStatement(sql);
    preparedStatement.executeUpdate();
      
        /* old version
        connect = DriverManager.getConnection("jdbc:mysql://cooper@199.98.20.114:5122?" + "user=root&password=toor");
      
    preparedStatement = connect.prepareStatement("update users set score=score + ? where id = ?; update users set gp=gp+1 where id = ?");
    preparedStatement.setString(1, Integer.toString(score));
    preparedStatement.setString(2, Integer.toString(uid));
    preparedStatement.setString(3, Integer.toString(uid));
    preparedStatement.executeUpdate();
        */
      
    //writeResultSet(resultSet);
      
   } catch (ClassNotFoundException | SQLException e) {
        throw e;
    }
        finally {
        close();
    }
   }
 
/*
   //update time to set
   public static void updateTTS(int uid, float something){
      
    Class.forName("com.mysql.jdbc.Driver");  
    connect = DriverManager.getConnection("cooper@199.98.20.114" + "user=sqluser&password=sqluserpw");
    statement = connect.createStatement();

    preparedStatement = connect.prepareStatement("update ?");

    preparedStatement.setString(1, score);
    preparedStatement.setString(2, uid);
    preparedStatement.executeUpdate();

    writeResultSet(resultSet);
      
   } catch (Exception e) {
        throw e;
    }
        finally {
        close();
    }
   }
*/

   //Performed at "Ranking"
   //queries the database using a group by and returns a sorted list of all users sorted by score
   public ResultSet returnRankings() throws Exception{
        try{
    Class.forName("com.mysql.jdbc.Driver");  
        connect = DriverManager.getConnection("jdbc:mysql://localhost:3306/setdb?" + "user=root&password=recognitio");
    statement = connect.createStatement();

    resultSet = statement.executeQuery("select id,login,score,tts,gp from users where score IS NOT NULL order by score DESC;");

    //writeResultSet(resultSet);

return resultSet;

    }catch (ClassNotFoundException | SQLException e) {
        throw e;
    }
        finally {
        close();
    }
   }

   private void writeResultSet(ResultSet resultSet) throws SQLException {

    while (resultSet.next()) {

        String username = resultSet.getString("login");
        String password = resultSet.getString("password");
        String id = resultSet.getString("id");
        String score = resultSet.getString("score");
        String tts = resultSet.getString("tts");
        String gp = resultSet.getString("gp");

        System.out.println("Login: " + username);
        System.out.println("PW: " + password);
        System.out.println("ID: " + id);
        System.out.println("Score: " + score);
        System.out.println("TTS: " + tts);
        System.out.println("GP: " + gp);
    }
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
