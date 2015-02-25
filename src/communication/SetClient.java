/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package communication;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import gui.Client;


/**
 *
 * @author Skorpion
 */
public class SetClient {
    public static PrintWriter OUT;
    
    public static void Connect(Client c)
    {
            try
            {
                   final int PORT=8052;
                   //final String HOST = "localhost";
                   final String HOST="199.98.20.114";
                   Socket SOCK=new Socket(HOST,PORT);
                   ClientSideThread cst=new ClientSideThread(SOCK, c);
                   OUT=new PrintWriter(SOCK.getOutputStream());
                   Thread X=new Thread(cst);
                   X.start();
            } catch (java.net.ConnectException e) {
            	javax.swing.JOptionPane.showMessageDialog(c,"Could not connect to server.","Error",javax.swing.JOptionPane.ERROR_MESSAGE);
            }
            catch(Exception X)
            {
                    System.out.println("ERROR:"+X+" in SetClient.connect(Client C)");
            }
    }
    //call from anywhere to send message to server. 
    public static void sendMessage(String message){
        OUT.println(message);
        OUT.flush();
    }
}

