/**
 * @(#)Input.java
 *
 *
 * @author
 * @version 1.00 2010/8/31
 */
package cantro.input;

import java.util.*;

public class Input {

	public static final int UP = 3;
    public static final int DOWN = 2;
    public static final int LEFT = 1;
    public static final int RIGHT = 0;
    public static final int SHOOT = 4;

	public boolean[] down;
	public boolean change;

    public Input()
    {
		down = new boolean[5];
		change = true;
    }

    public void addKey(int add)
    {
    	if(!down[add])
    	{
    		down[add] = true;
    		change =true;
    	}
    	
    }

    public void remKey(int rem)
    {
    	down[rem] = false;
    	change = true;
    }

    public String toString()
    {
    	String ret = "";
    	for(boolean b : down)
    	{
    		ret += b;
    	}
    	return ret;
    }
    

    public void clear()
    {
        down = new boolean[5];
    }
}