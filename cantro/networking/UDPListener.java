/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package cantro.networking;

import java.net.*;
import java.io.*;
import cantro.graphics.*;

public class UDPListener implements Runnable
{

	ServerBrowserBox owner;
    DatagramSocket udpSocket;

    byte[] buffer;
    DatagramPacket packet;

    public UDPListener(ServerBrowserBox o, DatagramSocket ds)
    {
        owner = o;
        udpSocket = ds;
        buffer = new byte[50];
        packet = new DatagramPacket(buffer,buffer.length);
    }
    public void run()
    {
        while(true)
        {
            try
            {
                udpSocket.receive(packet);
                notifyOwnerOfUDPPacket(buffer);
            }
            catch(Exception e)
            {
                e.printStackTrace(System.out);
            }
        }
    }
    public void notifyOwnerOfUDPPacket(byte[] buff)
    {
        owner.UDPPacketIn(buff);
    }

}
