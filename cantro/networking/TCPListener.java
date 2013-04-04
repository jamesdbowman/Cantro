/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package cantro.networking;

import java.net.*;
import java.io.*;
import java.util.*;
/**
 *
 * @author James
 */
public class TCPListener implements Runnable{
    public GameConnection owner;
    public TCPListener(GameConnection o)
    {
        owner = o;
    }

    public void run()
    {
        while(true)
        {
            try
            {
                if(owner.in.ready())
                {
                    owner.lineIn(owner.in.readLine());
                }
            }
            catch(Exception e){e.printStackTrace(System.out);}
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
