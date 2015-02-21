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
import GameBackEnd.*;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.*;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.LinkedBlockingQueue;

public class SetServer{
    
    public static ConcurrentMap<Integer,Socket> SocketList = new ConcurrentHashMap<>(); //uid to socket map
    public static BlockingQueue<String> bqueue = new LinkedBlockingQueue<String>();
    public static ConcurrentMap<Integer,Socket> waitingSockets = new ConcurrentHashMap<>();
    private static int sid=0;
    
    public static void main(String[] args)throws IOException
    {
            try{
                    final int port=5909;
                    ServerSocket SERVER=new ServerSocket(port);
                    System.out.println("Waiting for clients on IP "+InetAddress.getLocalHost().getHostAddress());
                    MessageProcessor pm = new MessageProcessor();
                    Thread processM = new Thread(pm);
                    processM.start();
                    while(true)
                    {
                        Socket SOCK=SERVER.accept();
                        System.out.println("ConnectFrom="+SOCK.getInetAddress().getHostAddress());
                        sid++;
                        waitingSockets.put(sid,SOCK);
                        ServSideThread sst=new ServSideThread(SOCK,sid);
                        Thread X=new Thread(sst);
                        X.start();
                }
            }
            catch(Exception X){System.out.println("ERROR:"+X+" in SetServer.main() ");}
    }

}