/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package cantroserver.networking;

import java.net.*;
import java.io.*;
import java.util.*;
/**
 *
 * @author James
 */
public class TCPListener implements Runnable{
    public NetworkController owner;
    public TCPListener(NetworkController o)
    {
        owner = o;
    }

    public void run()
    {
        while(true)
        {
            synchronized(owner.connections)
            {
                for(GameClientConnection cc : owner.connections)
                {
                    try
                    {
                        if(cc.in.ready())
                        {
                            owner.lineIn(cc,cc.in.readLine());
                        }
                    }
                    catch(Exception e){e.printStackTrace(System.out);}
                }
            }
            delay(15);
        }
    }

    public void delay(int n)
    {
        try{
        Thread.sleep(n);}
        catch(Exception e){}
    }
}
