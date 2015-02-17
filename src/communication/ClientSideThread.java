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
/**
 *
 * @author Skorpion
 */
class ClientSideThread implements Runnable {

    Socket sock;
    Scanner input;

    ClientSideThread(Socket SOCK) {
        this.sock = SOCK;
    }

    public void run()
	{
		try{
			try{
				input=new Scanner(sock.getInputStream());
				while(true)
                                {
                                    if(input.hasNext())
                                    {
                                        String MESSAGE =input.nextLine();
                                        //here, the received message from server is MESSAGE, so call process message here
                                        //update gui here
                                        System.out.println("RECEIVED FROM SERVER: "+MESSAGE);
                                    }

                                }
				
			}
			finally{
				sock.close();
			}
		}catch(Exception X){System.out.print(X);}
	}

    public void disconnect() throws IOException
    {
            sock.close();
            System.exit(0);
    }
    
}
