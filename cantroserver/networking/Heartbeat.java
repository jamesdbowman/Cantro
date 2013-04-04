/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package cantroserver.networking;

import java.net.*;
import java.io.*;

/**
 *
 * @author James
 */
public class Heartbeat implements Runnable {

    public NetworkController owner;
    GameClientConnection notify;
    public Heartbeat(NetworkController o)
    {
        owner = o;
    }

    public void run()
    {
        while(true)
        {
            owner.broadcastFromConnectionToOthers(null, Commands.PING.val+"");
            for(GameClientConnection cc : owner.connections)
            {
                cc.alive = false;
            }
            delay(2000);
            notify = null;
            for(GameClientConnection cc : owner.connections)
            {
                if(!cc.alive) notify = cc;
            }
            System.out.println(notify);
            if(notify!=null)notifyOwnerOfDroppedConnection(notify);
        }
    }

    public void notifyOwnerOfDroppedConnection(GameClientConnection cc)
    {
        owner.dropConnection(cc);
    }

    public void delay(int n)
    {
        try{
        Thread.sleep(n);}
        catch(Exception e){}
    }
}
