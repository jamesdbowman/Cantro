/*
 *Player class
 *By: James Bowman
 *
 *Represents a player in CashCade
 */
package cantroserver.calculation;

import cantroserver.*;
import java.awt.*;
import java.util.*;

public class Player {

      	public int height;
	public int width;

	public int UID;
	public int x;
	public int y;
	public int health;
	public int xspeed;
	public int yspeed;
	public int xaccel;
	public int yaccel;
	public boolean isOnGround;
	public boolean isCloseToBlack;
        public long lastJumpTime;
	public double gravity;

	public static ArrayList<Player> players;
        public Input input;
        public CantroserverApp owner;

    public Player(CantroserverApp o) {
    	this("Unnamed",0,o);
    }

    public Player(String n, int uid,CantroserverApp o)
    {
    	UID = uid;
        height = 50;
    	width = 30;
    	x = 100;
    	y = 415;
    	health = 100;
    	xspeed = 0;
    	yspeed = -10;
    	xaccel = 15;
    	yaccel = 35;
    	isOnGround = true;
    	isCloseToBlack = false;
    	lastJumpTime = 0;
    	gravity = .5;

        owner = o;
        input = new Input();

    	if(players == null)
    	{
    		players = new ArrayList<Player>();
    	}
    	else
    	{
    		players.add(this);
    	}

    }

    public static Player playerByID(int ID)
    {
    	for(Player p : players)
    	{
    		if(p.UID == ID)
    			return p;
    	}
    	System.out.println("Null found with ID "+ID);
    	for(Player p : players)
    	{
    		System.out.println(p.UID);
    	}
    	return null;
    }

    /*
	public void updateItems() //updates accels, etc. from new items.
	{
		Collections.sort(items);
		ArrayList<GameItem> remove = new ArrayList<GameItem>();
		for(GameItem gi : items)
		{
		}
		for(GameItem gi : remove)
		{
			items.remove(gi);
		}
	}
     *
     */

    public void setInputByChar(char c)
    {
        input.clear();
        int in = (int)c-40;
        if((in & 16) == 16) input.addKey(Input.SHOOT);
        if((in & 8) == 8) input.addKey(Input.UP);
        if((in & 4) == 4) input.addKey(Input.DOWN);
        if((in & 2) == 2) input.addKey(Input.LEFT);
        if((in & 1) == 1) input.addKey(Input.RIGHT);
    }
    public void goRight()
    {
    	if(isOnGround || xspeed > 10)
    	xspeed = xaccel;
    	else
    	xspeed = xaccel/2;
    }

    public void goLeft()
    {
    	if(isOnGround || xspeed < -10)
    	xspeed = -xaccel;
    	else
    	xspeed = -(xaccel/2);
    }

    public void goDown()
    {
    	int[] pixels = new int[width];
    	owner.getRGBofSkeleton(x,y+height+1,width,1,pixels,0,width);
    	boolean goDown = true;
    	for(int i = 0; i < pixels.length; i++)
    	{
    		if(pixels[i] == -16777216 || pixels[i] == -65281)
    		{
    			goDown = false;
    			i = pixels.length;
    		}
    	}
    	if(goDown) y+=1;
    	input.remKey(Input.DOWN);
    }

    public void jump()
    {
    	if(isOnGround && (System.currentTimeMillis()-lastJumpTime) > 750)
    	{
    		lastJumpTime = System.currentTimeMillis();
    		yspeed = yaccel; //(int)(Math.random() * 15 + 15);
    	}
    }

    public void momentum()
    {
    	//Black: -16777216
    	//Pink: -65281
    	int ylim = 530;
    	//Gravity
    	int deductFromY = (yspeed/2);
    	isCloseToBlack = false;
    	if(yspeed < 0)
    	{
	    	int[] pixels = new int[Math.abs(deductFromY)*width];
	    	owner.getRGBofSkeleton(x,y+height,width,Math.abs(deductFromY),pixels,0,width);
	    	for(int i = 0; i < pixels.length; i++)
	    	{
	    		if(pixels[i] == -16777216 || pixels[i] == -65281)
	    		{
	    			isCloseToBlack = true;
	    			deductFromY = -(i/width);
	    			i = pixels.length;
	    		}
	    	}

    	}
    	if(yspeed > 0)
    	{
	    	int[] pixels = new int[Math.abs(deductFromY)*width];
	    	owner.getRGBofSkeleton(x,y-Math.abs(deductFromY),width,Math.abs(deductFromY),pixels,0,width);
	    	for(int i = 0; i < pixels.length; i++)
	    	{
	    		if(pixels[i] == -65281)
	    		{
	    			isCloseToBlack = true;
	    			deductFromY = (i/width);
	    			yspeed = 0;
	    			i = pixels.length;
	    		}
	    	}
    	}
    	if(deductFromY == 0 && isCloseToBlack) isOnGround = true;
    	else
    	{
    		y-=deductFromY;
    		isOnGround = false;
    	}

    	if(yspeed > -10/gravity)//Terminal velocity
    		yspeed -= 1 / gravity + 1;

    	if(y > ylim)//At the ground
    	{
    		y = ylim;
    	}


    	int actualTravel = xspeed/4;
    	if(actualTravel < 0)
    	{
        	int absXSpeedDivBy4 = Math.abs(xspeed/4);
        	int[] pixels = new int[absXSpeedDivBy4*height];
        	owner.getRGBofSkeleton(x-absXSpeedDivBy4,y,absXSpeedDivBy4,height,pixels,0,absXSpeedDivBy4);
	    	for(int i = absXSpeedDivBy4-1; i < pixels.length-1; i+=absXSpeedDivBy4)
	    	{
	    		if(pixels[i] == -65281)
	    		{
	    			actualTravel = 0;
	    			i = pixels.length;
	    		}
	    	}
	    	if(actualTravel==xspeed/4)
	    	{
	    		for(int x = 0; x < absXSpeedDivBy4; x++)
	    		{
	    			for(int y = 0; y < height; y++)
	    			{
	    				int rgb = pixels[y*absXSpeedDivBy4+x];
	            		if(rgb == -65281)
	            		{
	            			actualTravel = -x;
	            			x = absXSpeedDivBy4;
	            			y = height;
	            		}
	    			}
	    		}
	    	}
    	}
    	else if(actualTravel > 0)
    	{
        	int absXSpeedDivBy4 = Math.abs(xspeed/4);
        	int[] pixels = new int[absXSpeedDivBy4*height];
        	owner.getRGBofSkeleton(x+width,y,absXSpeedDivBy4,height,pixels,0,absXSpeedDivBy4);
	    	for(int i = 0; i < pixels.length; i+=absXSpeedDivBy4)
	    	{
	    		if(pixels[i] == -65281)
	    		{
	    			actualTravel = 0;
	    			i = pixels.length;
	    		}
	    	}
	    	if(actualTravel==xspeed/4)
	    	{
	    		for(int x = 0; x < absXSpeedDivBy4; x++)
	    		{
	    			for(int y = 0; y < height; y++)
	    			{
	    				int rgb = pixels[y*absXSpeedDivBy4+x];
	            		if(rgb == -65281)
	            		{
	            			actualTravel = x;
	            			x = absXSpeedDivBy4;
	            			y = height;
	            		}
	    			}
	    		}
	    	}
    	}
    	x += actualTravel;
    	if(isOnGround)//On the ground, we have friction
    	{
	    	if(xspeed < 0)
	    		++xspeed;
	    	if(xspeed > 0)
	    		--xspeed;
    	}
    }
}