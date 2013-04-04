/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package cantroserver.networking;

import cantroserver.networking.NetworkController;
import java.net.*;
import java.io.*;

/**
 *
 * @author James
 */
public class ConnectionListener implements Runnable {

    public ServerSocket socket;
    public NetworkController owner;
    public DatagramSocket UDPsocket;
    public ConnectionListener(NetworkController o, ServerSocket s, DatagramSocket udps)
    {
        owner = o;
        socket = s;
        UDPsocket = udps;
    }

    public void run()
    {
        while(true)
        {
            try
            {
                Socket clientSocket = socket.accept();
                if(owner.owner.roomPass != null)
                {
                    InputStreamReader tempReader = new InputStreamReader(clientSocket.getInputStream());
                    boolean accept = false;
                    if(tempReader.ready())
                    {
                        BufferedReader br = new BufferedReader(tempReader);
                        if(br.readLine().equals(owner.owner.roomPass)) accept = true;
                    }
                    if(accept) notifyOwnerOfConnection(clientSocket);
                }
                else notifyOwnerOfConnection(clientSocket);
            }
            catch (IOException e)
            {
                System.err.println("Accept failed.");
            }
        }
    }

    public void notifyOwnerOfConnection(Socket s)
    {
        System.out.println("Listener thread found connection!");
        owner.addConnection(s);
    }
}
