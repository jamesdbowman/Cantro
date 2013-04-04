/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package cantroserver.networking;

import java.io.*;
import java.net.*;

public class GameClientConnection
{

    public Socket socket;
    public BufferedReader in;
    public PrintWriter out;
    public String username;
    public int UID;
    public boolean alive;

    public GameClientConnection(Socket s, BufferedReader i, PrintWriter o)
    {
        socket = s;
        in = i;
        out = o;
        alive = true;
    }

    public boolean equals(Object o)
    {
        GameClientConnection gcc = (GameClientConnection)o;
        return UID == gcc.UID;
    }
}
