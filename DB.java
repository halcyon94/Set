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

class DB {

   private Connection connect = null;
   private Statement statement = null;
   private PreparedStatement preparedStatement = null;
   private ResultSet resultSet = null;

   //Insert user into SetDB database: performed at "Register"
   //return uid of new user
   public static int insertUser(String username, String password) throws Exception{
      try{
	Class.forName("com.mysql.jdbc.Driver");	
	connect = DriverManager.getConnection("cooper@199.98.20.114" + "user=sqluser&password=sqluserpw");
	statement = connect.createStatement();

	preparedStatement = connect.prepareStatement("insert into users (login,password) values (?, ?)");
	preparedStatement.setString(1, username);
	preparedStatement.setString(2, password);
	preparedStatement.executeUpdate();

	writeResultSet(resultSet);

      } catch (Exception e) {
		throw e;
	}  
		finally {
		close();
	}
   }

   //Confirm user credentials: performed at "Sign In"
   //Check if input username/password exist in database
   //return uid
   public static int findUser(String username, String password){
        
	Class.forName("com.mysql.jdbc.Driver");	
	connect = DriverManager.getConnection("cooper@199.98.20.114" + "user=sqluser&password=sqluserpw");
	statement = connect.createStatement();

	preparedStatement = connect.prepareStatement("select login,password from users where login = ? and password = sha1(?) ;");

	preparedStatement.setString(1, username);
	preparedStatement.setString(2, password);
	preparedStatement.executeUpdate();

	writeResultSet(resultSet);  
   } catch (Exception e) {
		throw e;
	}  
		finally {
		close();
	}
   }
    
   //Performed at end of Game or at disconnect, whenever a player leaves a game
   //adds score to the current score of the player stored in the Database
   //increases games played by 1
   public static void updateUserScore(int uid, int score){
        
	Class.forName("com.mysql.jdbc.Driver");	
	connect = DriverManager.getConnection("cooper@199.98.20.114" + "user=sqluser&password=sqluserpw");
	statement = connect.createStatement();

	preparedStatement = connect.prepareStatement("update users set score=score + ? where id = ?; update users set gp=gp+1 where id = ?");

	preparedStatement.setString(1, score);
	preparedStatement.setString(2, uid);
	preparedStatement.setString(3, uid);
	preparedStatement.executeUpdate();

	writeResultSet(resultSet);
        
   } catch (Exception e) {
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
   public static String returnRankings(){
        
	Class.forName("com.mysql.jdbc.Driver");	
	connect = DriverManager.getConnection("cooper@199.98.20.114" + "user=sqluser&password=sqluserpw");
	statement = connect.createStatement();

	resultSet = statement.executeQuery("select id,login,score,tts,gp from users where score IS NOT NULL order by score DESC;");

	writeResultSet(resultSet);

    }catch (Exception e) {
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
