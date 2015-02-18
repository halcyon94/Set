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
    
    public static void main(String[] args) throws IOException{
        Connect();
        BufferedReader in = new BufferedReader(new InputStreamReader(System.in));
        String s;
        while((s=in.readLine())!=null && (s.length() !=0)){
            OUT.println(s);
            OUT.flush();
        }
    }
    
    public static void Connect()
    {
            try
            {
                   final int PORT=3000;
                   final String HOST="LocalHost";
                   Socket SOCK=new Socket(HOST,PORT);
                   ClientSideThread cst=new ClientSideThread(SOCK);
                   OUT=new PrintWriter(SOCK.getOutputStream());
                   Thread X=new Thread(cst);
                   X.start();
            }
            catch(Exception X)
            {
                    System.out.println("ERROR:"+X+" in SetClient.connect()");
            }
    }
    public static void Connect(Client c)
    {
            try
            {
                   final int PORT=3000;
                   final String HOST="LocalHost";
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

