/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package communication;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Scanner;
import java.util.concurrent.BlockingQueue;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Skorpion
 */
class ServSideThread implements Runnable {
    Socket sock;
    private Scanner input;
    private PrintWriter out;
    String message="";
    public ServSideThread(Socket SOCK) {
        this.sock = SOCK;

    }

    @Override
    public void run()
    {
        try {
            input=new Scanner(sock.getInputStream());
        } catch (IOException ex) {
            System.out.println("ERROR:"+ex+" in ServSideThread.run() (1)");
        }
        while(true)
        {
                if(!input.hasNext()||!sock.isConnected())
                {
                    break;
                }
                message=input.nextLine(); //queue message in blocking queue
                if(message != null){
                    try {
                        SetServer.bqueue.put(message);
                    } catch (InterruptedException except) {
                        System.out.println("ERROR:"+except+" in ServSideThread.run() (2)");
                    }
                }
        }        
    }
}
	
	
    
  