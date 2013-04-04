package cantro.entity;

import cantro.Cantro;
import java.util.*;
import cantro.input.*;

public class Zombie extends Player{
	
	public static ArrayList<Zombie> zombies;
	
	public int counter;
	public int nextJobLength;
	
	public Zombie(int xx, int yy, Cantro o)
	{
		super("Zombie",-1,false,o);//(String n, int uid, boolean add, Cantro o)
		x = xx;
		y = yy;
		xaccel = 5;
		yaccel = 35;
		jumpTimeLim = 2500;
		/*synchronized(zombies){
		zombies.add(this);}*/
	}
	
	public void AICycle()
	{
		setInput();
		applyInput();
		momentum();
	}
	
    public void goRight()
    {
    	xspeed = xaccel;
    }

    public void goLeft()
    {
    	xspeed = -xaccel;
    }
    
    public void jump()
    {
    	super.jump();
    	jumpTimeLim = (int)(Math.random()*2000)+1000;
    }
	
	public void setInput()
	{
		input.clear();
		if(x < owner.p.x)
		{
			input.addKey(Input.RIGHT);
		}
		else 
		{
			input.addKey(Input.LEFT);
		}
		if(y < owner.p.y)
		{
			input.addKey(Input.DOWN);
		}
		else if(y != owner.p.y)
		{
			input.addKey(Input.UP);
		}
		/*
		if(lastVerticalMoveInPixels == 0)
		{
			input.addKey(Input.UP);
			input.remKey(Input.DOWN);
		}*/
			
	}

}
