/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package cantroserver.networking;

import cantroserver.CantroserverApp;
import java.net.*;
import java.io.*;
import java.util.*;
import cantroserver.calculation.*;

/**
 *
 * @author James
 */
public class NetworkController {

    public ArrayList<GameClientConnection> connections;
    public ServerSocket serverSocket;
    public Socket clientSocket;
    public DatagramSocket udpSocket;
    public CantroserverApp owner;

    public Thread connectionListenerThread;
    public ConnectionListener connectionListener;

    public Thread TCPListenerThread;
    public TCPListener TCPListener;

    public Thread heartbeatThread;
    public Heartbeat heartbeat;

    public Thread UDPListenerThread;
    public UDPListener UDPListener;

    public int curUID;

    public NetworkController(CantroserverApp o)
    {
        owner = o;
        connections = new ArrayList<GameClientConnection>();
        try
        {
            serverSocket = new ServerSocket(4444);
            udpSocket = new DatagramSocket(4444);
        }
        catch(Exception e)
        {
            System.out.println("Could not listen on port");
        }

        connectionListener = new ConnectionListener(this,serverSocket,udpSocket);
        connectionListenerThread = new Thread(connectionListener);
        connectionListenerThread.setDaemon(true);

        TCPListener = new TCPListener(this);
        TCPListenerThread = new Thread(TCPListener);
        TCPListenerThread.setDaemon(true);

        heartbeat = new Heartbeat(this);
        heartbeatThread = new Thread(heartbeat);
        heartbeatThread.setDaemon(true);

        UDPListener = new UDPListener(this, udpSocket);
        UDPListenerThread = new Thread(UDPListener);
        UDPListenerThread.setDaemon(true);

        curUID = 1;
    }

    public void startThreads()
    {
        TCPListenerThread.start();
        connectionListenerThread.start();
        heartbeatThread.start();
        UDPListenerThread.start();
    }

    public void addConnection(Socket s)
    {
        try
        {
            connections.add(new GameClientConnection(s
                    ,new BufferedReader(new InputStreamReader(s.getInputStream()))
                    ,new PrintWriter(s.getOutputStream(), true)));
        }
        catch(IOException e)
        {
            e.printStackTrace(System.out);
        }
    }

    public boolean addUserApp(String in, GameClientConnection gcc)
    {
        String u = in.trim();
        System.out.println("asked for "+u+".");
        String allowed = null;
        for(GameClientConnection cc : connections)
        {
            if(cc.username != null)
            {
                if(cc.username.equals(u)) allowed = "That username is already in use.";
            }
        }
        if(u.equals("userApp") || u.equals("chat") || u.equals("UserListUpdate") || u.equals("")) allowed = "That username is reserved.";
        if(u.contains(":")) allowed = "Periods not allowed in username.";
        if(allowed == null)
        {
            if(Player.players == null) Player.players = new ArrayList<Player>();
            Player.players.add(new Player(u,curUID,owner));
            gcc.out.println(Commands.USERNAMEAPPLICATIONACCEPTED.val+":"+curUID);
            gcc.UID = curUID;
            gcc.username = u;
            curUID++;
            updateUserOfPeopleAlreadyHere(gcc);
            notifyOfAddUserToList(gcc);
            return true;
        }
        else gcc.out.println(Commands.USERNAMEAPPLICATIONREJECTED.val+":"+allowed);
        return false;
    }

    public void lineIn(GameClientConnection cc, String in)
    {
        String[] split = in.split(":");
        if(Integer.parseInt(split[0]) == Commands.INPUTUPDATE.val)
        {
            Player.playerByID(Integer.parseInt(split[1])).setInputByChar(split[2].charAt(0));
            broadcastFromConnectionToOthers(cc,in);
        }
        else if(Integer.parseInt(split[0]) == Commands.USERNAMEAPPLICATION.val)
        {
            addUserApp(split[1],cc);
        }
        else if(Integer.parseInt(split[0]) == Commands.PING.val)
        {
            cc.alive = true;
        }
    }

    public void broadcastFromConnectionToOthers(GameClientConnection gcc, String s)
    {
        for(GameClientConnection cc : connections)
        {
            if(gcc!=null?!gcc.equals(cc):true)
            {
                cc.out.println(s);
            }
        }
    }

    public void updateUserOfPeopleAlreadyHere(GameClientConnection gcc)
    {
        //updates the new guy of the list of ccs already here!
        for(GameClientConnection cc : connections)
        {
            if(!gcc.equals(cc))
            {
                gcc.out.println(Commands.ADDUSERTOLIST.val + ":" + cc.username + ":" + cc.UID);
            }
        }
    }

    public void notifyOfAddUserToList(GameClientConnection gcc)
    {
        //broadcast to all others about the new guy!
        broadcastFromConnectionToOthers(gcc, Commands.ADDUSERTOLIST.val + ":" + gcc.username + ":" + gcc.UID);
    }

 /*
    public void notifyUsersOfUserList()
    {
        Vector<String> listOfUsers = new Vector<String>();
        String send = Commands.USERLISTUPDATE.val+":";
        for(GameClientConnection cc : connections)
        {
            send += cc.username + ":" + cc.UID + ":";
            listOfUsers.add(cc.username);
        }
        send = send.substring(0,send.length()-1);
        if(owner.online)
        {
            int selected = owner.view.jList1.getSelectedIndex();
            owner.view.jList1.setListData(listOfUsers);
            owner.view.jList1.setSelectedIndex(selected);
        }
        for(GameClientConnection cc : connections)
        {
            cc.out.println(send);
        }
    }
*/
    public void dropConnection(GameClientConnection cc)
    {
        notifyOfAddUserToList(cc);
        synchronized(connections)
        {
            connections.remove(cc);
        } 
    }

    public void notifyOfRemoveUserFromList(GameClientConnection gcc)
    {
        //broadcast to all others about the drop.
        broadcastFromConnectionToOthers(gcc, Commands.REMOVEUSERFROMLIST.val + ":" + gcc.UID);
    }

    public void UDPPacketIn(byte[] info)
    {
        String in = new String(info).trim();
        try
        {
            DatagramSocket ds = new DatagramSocket();
            String sent = "REPLY:"+owner.roomName; //Room name
            sent += ":"+connections.size(); //Num People
            sent += ":"+InetAddress.getLocalHost().getHostName(); //HostName
            boolean passProtected = true;
            if(owner.roomPass == null) passProtected = false;
            sent += ":"+passProtected; //Password Protected
            System.out.println(sent);
            DatagramPacket p = new DatagramPacket(sent.getBytes(),sent.length(),InetAddress.getByName(in.substring(in.indexOf(":")+1,in.length())),4445);
            ds.send(p);
        }
        catch(Exception e){}
    }
}
