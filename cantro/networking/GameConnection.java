/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package cantro.networking;

import java.net.*;
import java.io.*;
import java.util.*;
import javax.swing.*;
import cantro.*;
import cantro.entity.Player;

public class GameConnection {

    public Socket socket;
    public BufferedReader in;
    public PrintWriter out;
    public Cantro owner;
    public String username;
    public boolean connected;

    public TCPListener chatListener;
    public Thread chatListenerThread;

    public static final int port = 4444;

    public GameConnection(Cantro o)
    {
        connected = false;
        owner = o;
    }

    public String connectTo(String hostname, String u)
    {
    	updateListOfPlayersInUI();
        try
        {
            socket = new Socket(hostname, port);
        }
        catch(Exception e)
        {
            return "Could not connect to host " + hostname;
        }
        try
        {
            in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            out = new PrintWriter(socket.getOutputStream(), true);
        }
        catch(Exception e)
        {
            return "I/O Error";
        }
        if(applyForUsername(u))
        {
            connected = true;
        }
        else return "Username already in use on this host.";

        chatListener = new TCPListener(this);
        chatListenerThread = new Thread(chatListener);
        chatListenerThread.setDaemon(true);
        chatListenerThread.start();

        return null;
    }

    public boolean applyForUsername(String u)
    {
        out.println(Commands.USERNAMEAPPLICATION.val+":"+u);
        out.flush();
        boolean notRespond = true;
        while(notRespond)
        {
            String read = "";
            try
            {
                if(in.ready()) read = in.readLine();
            }
            catch(IOException e){}
            if(read.indexOf(Commands.USERNAMEAPPLICATIONACCEPTED.val+"") == 0)
            {
                username = u;
                owner.p.UID = Integer.parseInt(read.substring(read.indexOf(":")+1,read.length()));
                return true;
            }
            else if(read.indexOf(Commands.USERNAMEAPPLICATIONREJECTED.val+"") == 0)
            {
                JOptionPane.showMessageDialog(null,read.substring(read.indexOf(":")+1),"Illegal username",JOptionPane.OK_OPTION);
                return false;
            }
        }
        return false;
    }

    public void send(String msg)
    {
    	if(connected)
        out.println(msg);
    	out.flush();
    }
    
    public void updateListOfPlayersInUI()
    {
    	System.out.println("ASDF");
    	Vector<String> v = new Vector<String>();
    	v.add(owner.p.name);
    	for(Player p : Player.players)
    	{
    		v.add(p.name);
    	}
    	//owner.chatPanel.jList1.setListData(v);
    }
    
    public void lineIn(String in)
    {
    	String[] split = in.split(":");
    	if(Integer.parseInt(split[0]) == Commands.INPUTUPDATE.val)
    	{
    		Player.playerByID(Integer.parseInt(split[1])).applyNetworkInputUpdate(split[2]);
    	}
    	if(Integer.parseInt(split[0]) == Commands.POSUPDATE.val)
    	{
    		Player.playerByID(Integer.parseInt(split[1])).applyNetworkPosUpdate(Integer.parseInt(split[2]),Integer.parseInt(split[3]));
    	}
    	if(Integer.parseInt(split[0]) == Commands.ADDUSERTOLIST.val)
    	{
    		new Player(split[1],Integer.parseInt(split[2]),true,owner);
    		System.out.println(Player.players.size());
    		updateListOfPlayersInUI();
    	}
    	if(Integer.parseInt(split[0]) == Commands.REMOVEUSERFROMLIST.val)
    	{
    		synchronized(Player.players)
    		{
    			Player.players.remove(Player.playerByID(Integer.parseInt(split[1])));
    		}
    		updateListOfPlayersInUI();
    	}
    	if(Integer.parseInt(split[0]) == Commands.PING.val)
    	{
    		send(Commands.PING.val+"");
    	}
    }

}
