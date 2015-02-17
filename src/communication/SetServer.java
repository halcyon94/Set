/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package communication;

/**
 *
 * @author Skorpion
 */
import GameBackEnd.GameLobby;
import GameBackEnd.Player;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.*;
import java.util.Map;
import java.util.Scanner;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.LinkedBlockingQueue;

public class SetServer{
    
    public static ConcurrentMap<Integer,Socket> SocketList = new ConcurrentHashMap<>(); //uid to socket map
    public static BlockingQueue<String> bqueue = new LinkedBlockingQueue<String>();
    
    public static void main(String[] args)throws IOException
    {
            try{
                    final int port=3000;
                    ServerSocket SERVER=new ServerSocket(port);
                    System.out.println("Waiting for clients...");
                    MessageProcessor pm = new MessageProcessor();
                    Thread processM = new Thread(pm);
                    processM.start();
                    while(true)
                    {
                            Socket SOCK=SERVER.accept();
                            if(validCredentials(SOCK)){
                                ServSideThread sst=new ServSideThread(SOCK);
                                Thread X=new Thread(sst);
                                X.start();
                           } 
                    }
            }
            catch(Exception X){System.out.print(X);}
    }

    public static boolean validCredentials(Socket X) throws IOException, Exception
    {
        int uid;
        Scanner INPUT=new Scanner(X.getInputStream());
        String message=INPUT.nextLine();
        switch(message.substring(0,1)){
            case "S": 
                {
                    String username, password;
                    int result;
                    String[] data = message.substring(1,message.length()).split("`");
                    username = data[0];
                    password = data[1];
                    result = GameLobby.enterLobby(username,password,false);
                    if(result >1){
                        SocketList.put(result, X);
                        PrintWriter OUT=new PrintWriter(X.getOutputStream());
                        OUT.println("V"+result);
                        OUT.flush();
                        OUT.println(GameLobby.returnGames()); 
                        OUT.flush();
                        OUT.println(GameLobby.returnPlayers()); 
                        OUT.flush();
                        for(Map.Entry<Integer,Player> entry1 : GameLobby.playerCollection.entrySet()){
                            uid = entry1.getKey();
                            Socket Temp_Sock = SocketList.get(uid);
                            PrintWriter OUT2=new PrintWriter(Temp_Sock.getOutputStream());
                            OUT2.println(GameLobby.returnPlayers());
                            OUT2.flush();
                        }
                        return true;
                    }
                    else{
                        PrintWriter OUT=new PrintWriter(X.getOutputStream());
                        OUT.println("I");
                        OUT.flush();
                        return false;
                    }
                }
            case "R":
            {
                    String username, password;
                    int result;
                    String[] data = message.substring(1,message.length()).split("`");
                    username = data[0];
                    password = data[1];
                    result = GameLobby.enterLobby(username,password,true);
                    SocketList.put(result, X);
                    PrintWriter OUT=new PrintWriter(X.getOutputStream());
                    OUT.println("V"+result);
                    OUT.flush();
                    OUT.println(GameLobby.returnGames()); 
                    OUT.flush();
                    OUT.println(GameLobby.returnPlayers()); 
                    OUT.flush();
                    for(Map.Entry<Integer,Player> entry1 : GameLobby.playerCollection.entrySet()){
                        uid = entry1.getKey();
                        Socket Temp_Sock = SocketList.get(uid);
                        PrintWriter OUT2=new PrintWriter(Temp_Sock.getOutputStream());
                        OUT2.println(GameLobby.returnPlayers());
                        OUT2.flush();
                    }
                    return true;
            }
            default:
            {
                    System.err.println("Error: Wrong Protocol in SetServer.checkUser()");
                    System.exit(01);
            }
        }
        return false;
    }
}
